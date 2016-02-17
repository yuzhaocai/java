/**
 * 
 */
package com.lczy.media.mobile.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.lczy.common.util.MyGson;
import com.lczy.common.web.WebHelper;
import com.lczy.media.mobile.util.Response;
import com.lczy.media.service.WordToPdfService;
import com.lczy.media.util.FileServerUtils;

/**
 * 移动端下载文件控制器.
 * 
 * @author wu
 *
 */
@Controller
@RequestMapping("/mobile/download")
public class MobileDownloadController {
	
	private Logger log = LoggerFactory.getLogger(MobileDownloadController.class);
	
	private Gson gson = MyGson.getInstance();
	
	@Autowired
	private WordToPdfService wordToPdfService;
	
	/**
	 * 需要授权的文件类型列表.
	 */
	private static final List<String> AUTH_LIST = Lists.newArrayList("article", "cert");
	
	@RequestMapping("/{type}/{fid}")
	public String download(@PathVariable String type, @PathVariable String fid,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			String token = "";
			if( isAuth(type) ) { //需要提供访问token
				token += "?token=";
				token += FileServerUtils.token();
			}
			String folder = System.getProperty("java.io.tmpdir");
			URL httpurl = new URL(FileServerUtils.getFileUrl(fid) + token);
	    	HttpURLConnection urlconnection  = (HttpURLConnection) httpurl.openConnection();
	    	urlconnection.connect();
	    	List<String> fileData = urlconnection.getHeaderFields().get("Content-Disposition");
	    	String fileName = null;
	    	if (fileData != null){
	    		for (String data : fileData){
	    			if (data.contains("filename")) {
	    				fileName = data.substring(data.lastIndexOf("=") + 1);
	    				break;
	    			}
	    		}
	    	}
	    	urlconnection.disconnect();
	    	token = "";
			if( isAuth(type) ) { //需要提供访问token
				token += "?token=";
				token += FileServerUtils.token();
			}
	    	if (fileName != null && (fileName.endsWith(".doc") || fileName.endsWith(".docx"))) {
	    		httpurl = new URL(FileServerUtils.getFileUrl(fid) + token);
	    		String outPdf = folder + "/" + fid + ".pdf";
	    		File pdfFlie = new File(outPdf);
	    		if (!pdfFlie.exists()) {
	    			String wordFile = folder + "/" + fileName;
	    			if (!new File(wordFile).exists()) {
	    				FileUtils.copyURLToFile(httpurl, new File(wordFile));
	    			}
	    			wordToPdfService.execute(wordFile, outPdf);
	    		}
	    		wirteStream(pdfFlie, response);
	    	} else if ("article".equals(type)) {
	    		try {
	    			httpurl = new URL(FileServerUtils.getFileUrl(fid) + token);
	    			fileName = fid + ".doc";
	    			String outPdf = folder + "/" + fid + ".pdf";
	    			File pdfFlie = new File(outPdf);
	    			if (!pdfFlie.exists()) {
	    				String wordFile = folder + "/" + fileName;
	    				if (!new File(wordFile).exists()) {
	    					FileUtils.copyURLToFile(httpurl, new File(wordFile));
	    				}
	    				wordToPdfService.execute(wordFile, outPdf);
	    			}
	    			wirteStream(pdfFlie, response);
	    		} catch (Exception e) {
	    			log.error("error in convert to pdf!");
	    			token = "";
	    			if( isAuth(type) ) { //需要提供访问token
	    				token += "?token=";
	    				token += FileServerUtils.token();
	    			}
	    			response.sendRedirect(FileServerUtils.getFileUrl(fid) + token);
	    		}
	    	} else {
	    		response.sendRedirect(FileServerUtils.getFileUrl(fid) + token);
	    	}
		} catch (Exception e) {
			log.warn("下载文件发生异常", e);
			Response rsp = new Response(900, e.getMessage());
			WebHelper.responseJSON(response, gson.toJson(rsp));
		}
		return null;
	}
	private void wirteStream(File file, HttpServletResponse response) throws IOException{
		InputStream in = null;
		ServletOutputStream output = null;
		try {  
            in = new FileInputStream(file);  
            output = response.getOutputStream();  
            byte[] b = new byte[4096];  
            int n;  
            while ((n = in.read(b)) != -1) {  
                output.write(b, 0, n);  
            }  
        } catch (IOException e) {  
        	log.error("文件读写失败", e);
        	throw e;
        } finally {  
            try { 
            	if (in != null) {
            		in.close();  
            	}
            	if (output != null) {
            		output.close();  
            	}
            } catch (IOException e) {  
            	log.error("文件读写失败", e);
            	throw e;
            }  
        }  
	}
	
	private boolean isAuth(String type) {
		return AUTH_LIST.contains(type);
	}
}
