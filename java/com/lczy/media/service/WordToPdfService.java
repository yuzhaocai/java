package com.lczy.media.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.lczy.common.util.UUID;


@Service
@Transactional(readOnly = true)
public class WordToPdfService {
	
	private static final Logger log = LoggerFactory.getLogger(WordToPdfService.class);
	
	
	
	public void execute(String inputWord, String outPdf) throws Exception {
		try {
			log.debug("start to convert word:{} to pdf:{} ", inputWord, outPdf);
			convertWordToPdf(inputWord, outPdf);
			log.debug("convert word to pdf success");
		} catch (Exception e) {
			log.error("error in convert word to pdf", e);
			throw e;
		}
	}
	
	/** 将word文件转化为pdf
	 * @param fileName word文件路径
	 * @param outPdf pdf文件路径
	 * @throws Exception 抛出异常
	 */
	private void convertWordToPdf(final String fileName, String outPdf) throws Exception {
		String parent = new File(fileName).getParent();
		String wordName = new File(fileName).getName();
		String name = wordName.substring(0, wordName.lastIndexOf("."));
		String outHtml = parent + "/" + name + ".html";
		final String fileDir = UUID.get();
		final String htmlFileDir = parent + "/" + fileDir;
		log.debug("convert word to html, html pic dir:{}", htmlFileDir);
		if (fileName.endsWith(".doc")) {
			WordToHtmlConverter wordToHtmlConverter = 
					new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
			wordToHtmlConverter.setPicturesManager(new PicturesManager() {
				public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches,
						float heightInches) {
					log.debug("get html pic:{}", suggestedName);
					return htmlFileDir +"/" + suggestedName;
				}
			});
			HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(fileName));
			// save pictures
			List<Picture> pics = wordDocument.getPicturesTable().getAllPictures();
			if (pics != null) {
				for (int i = 0; i < pics.size(); i++) {
					Picture pic = (Picture) pics.get(i);
					log.debug("convert pic:{}", pic.suggestFullFileName());
					try {
						File dir = new File(htmlFileDir);
						if (!dir.exists()){
							dir.mkdirs();
						}
						pic.writeImageContent(new FileOutputStream(htmlFileDir +"/" + pic.suggestFullFileName()));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
			wordToHtmlConverter.processDocument(wordDocument);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer serializer = tf.newTransformer();
			serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty(OutputKeys.METHOD, "html");
			
			Document htmlDocument = wordToHtmlConverter.getDocument();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			DOMSource domSource = new DOMSource(htmlDocument);
			StreamResult streamResult = new StreamResult(out);
			serializer.transform(domSource, streamResult);
			out.close();
			log.debug("convert to html success!, begin to write html to file");
			writeFile(new String(out.toByteArray()), outHtml);
			log.debug("write to html success!, begin to convert html to pdf");
			convertHtmlToPdf(outHtml, outPdf);
		} else if (fileName.endsWith(".docx")){
			XWPFDocument document = new XWPFDocument(new FileInputStream(fileName));  
			XHTMLOptions options = XHTMLOptions.create().URIResolver(new FileURIResolver(new File(outHtml).getParentFile()));
            options.setExtractor(new FileImageExtractor(new File(outHtml).getParentFile())); 
            OutputStream out = new FileOutputStream(new File(outHtml));  
            XHTMLConverter.getInstance().convert(document, out, options);  
            org.jsoup.nodes.Document doc = Jsoup.parse(new File(outHtml), "GB2312", "");
            doc.getElementsByTag("head").append("<meta http-equiv=Content-Type content=\"text/html; charset=gb2312\">");
            writeFile(doc.html(), outHtml);
            convertHtmlToPdf(outHtml, outPdf);
		}
		log.debug("begin to delete tmp file");
		//删除转化时的临时文件
		File htmlFile = new File(outHtml);
		if (htmlFile.exists()) {
			log.debug("delete tmp html:{}", htmlFile);
			org.apache.commons.io.FileUtils.forceDelete(htmlFile);
		}
		File dir = new File(htmlFileDir);
		if (dir.exists()) {
			log.debug("delete tmp htmlDir:{}", dir);
			org.apache.commons.io.FileUtils.deleteDirectory(dir);
		}
		log.debug("delete tmp file complite");
	}
	/** 将文件内容写入html
	 * @param content html的内容
	 * @param path 写入的html文件路径
	 * @throws IOException 抛出文件关闭异常
	 */
	private void writeFile(String content, String path) throws IOException {
		FileOutputStream fos = null;
		BufferedWriter bw = null;
		org.jsoup.nodes.Document doc = Jsoup.parse(content);
		content = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\">" + doc.html();
		content = content.replace("<html>", "");
		try {
			File file = new File(path);
			fos = new FileOutputStream(file);
			bw = new BufferedWriter(new OutputStreamWriter(fos, "GB2312"));
			bw.write(content);
		} catch (FileNotFoundException fnfe) {
			log.error("file not find in write to html file:" + path, fnfe);
			throw fnfe;
		} catch (IOException ioe) {
			log.error("error in write  to file:" + path, ioe);
			throw ioe;
		} finally {
			if (bw != null) {
				bw.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
	}
	
	private void convertHtmlToPdf(String htmlFile, String outputFile) throws Exception {
		 try {
             String encoding="GBK";
             File file = new File(htmlFile);
             if (file.isFile() && file.exists()) { //判断文件是否存在
                 InputStreamReader read = new InputStreamReader(
                 new FileInputStream(file),encoding);//考虑到编码格式
                 BufferedReader bufferedReader = new BufferedReader(read);
                 StringBuilder sb = new StringBuilder();
                 String line = null;
                 while((line = bufferedReader.readLine()) != null){
                	 if (line.contains("<meta") && !line.endsWith("/>")){
                		 line = line + "</meta>";
                	 }
                	 if (line.contains("<img")){
                		 String regex = "(?i)(\\<img [^\\>]+\\>)";
                		 line = line.replaceAll(regex, "$1</img>");
                	 }
                	 if (line.contains("<div style")){
                		 String regex = "<div\\s*style=[\"']?.*?[\"']?\\s*>";
                		 line = line.replaceAll(regex, "<div>");
                	 }
                	 line = line.replaceAll("<br>", "<br/>");
                	 log.debug("read html file:{}", line);
                	 sb.append(line);
                 }
                 read.close();
                 log.debug("read html file success, begin to convert to pdf!");
                 convertStrToPdf(sb.toString(), outputFile);
                 log.debug("end to convert to pdf!");
		     } else {
		         log.warn("file is not find in htmlFile:{}, return", htmlFile);
		     }
	     } catch (Exception e) {
	    	 log.error("error in read file", e);
	    	 throw e;
	     }
	}
	
	/** 将html文件写入pdf
	 * @param str html文件的内容
	 * @param outputFile pdf文件
	 * @throws Exception 抛出异常
	 */
	public void convertStrToPdf(String str, String outputFile) throws Exception {
		com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.LETTER);
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputFile));
		document.open();
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(str.getBytes()), null,Charset.forName("UTF-8"),   new AsianFontProvider());
		document.close();
	}
	
	
	 class AsianFontProvider extends XMLWorkerFontProvider {  
		  
		    public Font getFont(final String fontname, final String encoding,  
		            final boolean embedded, final float size, final int style,  
		            final BaseColor color) {  
		        BaseFont bf = null;  
		        try {  
//		            bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		        	String fontPath = getClass().getClassLoader().getResource("fonts/msyh.ttf").getPath();
		            bf =  BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);  
		        } catch (Exception e) {  
		           log.error("error in get fonts", e);
		        }  
		        Font font = new Font(bf, size, style, color);  
		        font.setColor(color);  
		        return font;  
		    }  
	}
	 
}
