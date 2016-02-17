package com.lczy.media.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaCase;
import com.lczy.media.entity.MediaQuote;
import com.lczy.media.entity.MediaWeibo;
import com.lczy.media.entity.MediaWeixin;
import com.lczy.media.util.Constants;
import com.lczy.media.util.FileServerUtils;
import com.lczy.media.vo.CustomerVO;

/**
 * 从excel中导入媒体
 * 
 * @author wanghaibin
 *
 */
@Service
public class MediaImportService {
	
	private static Logger log = LoggerFactory.getLogger(MediaImportService.class);

	@Autowired
	private CustomerService customerService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private DicService dicService;

	private String logoDir;
	
	private String xls;

	private String DEFAULT_PASSWORD = "12345678";
	
	private long mobPhoneSeed = System.currentTimeMillis()/100;

	public List<String> importMedia() throws Exception {
		List<String> result = new ArrayList<>();
		InputStream is = new FileInputStream(xls);
		HSSFWorkbook wb = new HSSFWorkbook(is);

		// 只读第一个sheet
		HSSFSheet sheet = wb.getSheetAt(0);
		int rowNum = sheet.getLastRowNum();
		// 从第二行开始
		for (int i = 1; i <= rowNum; i++) {
			try {
				log.error("line -------------------------------------------------- {}", i);
				// 每行忽略前两列:联系方式和密码
				HSSFRow row = sheet.getRow(i);
				String mobPhone = getStringCellValue(row.getCell(0));
				String password = getStringCellValue(row.getCell(1));
				String mediaType = getStringCellValue(row.getCell(2));
				String mediaName = getStringCellValue(row.getCell(3));
				String logo = getStringCellValue(row.getCell(4));
				String category = getStringCellValue(row.getCell(5));
				String area = getStringCellValue(row.getCell(6));
				String industry = getStringCellValue(row.getCell(7));
				String description = getStringCellValue(row.getCell(8));
				
				int fans = getIntCellValue(row.getCell(9));
				String fansDir = getStringCellValue(row.getCell(10));
				String products = getStringCellValue(row.getCell(11));
				String account = getStringCellValue(row.getCell(31));
				
				if (StringUtils.isBlank(mediaName) && StringUtils.isBlank(mediaType)) {
					log.error("遇到空行 ---------- 结束！");
					break;
				}
				
				log.error("媒体名称: {}", mediaName);
				
				// 媒体主对象
				CustomerVO mediaVO = new CustomerVO();
				mediaVO.setName(mediaName);
				mediaVO.setMobPhone(StringUtils.isBlank(mobPhone) ? this.getMobPhone() : mobPhone);
				mediaVO.setLoginName(mediaVO.getMobPhone());
				mediaVO.setPassword(StringUtils.isBlank(password) ? this.DEFAULT_PASSWORD : password);

				// 媒体对象
				Media media = getMedia(mediaType);
				if (media instanceof MediaWeixin) {
					media.setAccount(account);
				}
				media.setName(mediaName);
				media.setRegion(this.getRegion(area));
				media.setCategory(getItemCode(category));
				media.setIndustryType(getItemCodes(industry));
				media.setDescription(description);
				media.setFans(fans);
				media.setFansDir(getItemCodes(fansDir));
				media.setProducts(getItemCodes(products));

				getMediaCases(row, media);
				
				getMediaQuotes(row, media);

				// 上传logo
				File file = new File(new File(logoDir), logo);
				if (file.exists()) {
					String fileId = FileServerUtils.upload(null, logo, file, false, "file",	false);
					media.setShowPic(fileId);
					log.error("logo: {}", file.getName());
				} else {
					throw new Exception(String.format("logo: %s文件不存在!", logo));
				}
				
				log.error("大类: {}({})", mediaType, media.getMediaType());
				log.error("类别: {}({})", category, media.getCategory());
				log.error("地区: {}({})", area, media.getRegion());
				log.error("行业: {}({})", industry, media.getIndustryType());
				log.error("简介: {}", description);
				
				customerService.importMedia(media, mediaVO);
				log.error(String.format("行{} ----------------- 导入成功!"), i+1);
				result.add(String.format("行%04d ----------------- 导入成功!", i+1));
			} catch (Exception e) {
				log.error("导入失败: " + e.getMessage(), e);
				result.add(String.format("行%04d ----------------- 导入失败：%s", i+1, e.getMessage()));
			}
		}
		is.close();
		
		return result;
	}

	/**
	 * 媒体报价列表
	 * 
	 * @param row
	 * @param media
	 * @throws Exception 
	 */
	private void getMediaQuotes(HSSFRow row, Media media) throws Exception {
		Date now = new Date();
		int index = 18;
		Set<MediaQuote> qs = new HashSet<>();
		
		for (int i=0; i<5; i++) {
			String quoteType = getStringCellValue(row.getCell(index++));
			if (StringUtils.isNotBlank(quoteType)) {
				
				MediaQuote mq = new MediaQuote();
				String type = getItemCode(quoteType);
				int quotePrice = 0;
				try {
					quotePrice = getIntCellValue(row.getCell(index++));
				} catch (Exception e) {
					throw new Exception("价格非法！");
				}
				
				mq.setType(type);
				mq.setPrice(quotePrice);
				mq.setMedia(media);
//				mq.setCreateBy(UserContext.getSystemUser().getId());
//				mq.setModifyBy(UserContext.getSystemUser().getId());
				mq.setCreateTime(now);
				mq.setModifyTime(now);
				
				qs.add(mq);
			} else {
				break;
			}
		}
		
		media.setMediaQuotes(qs);
	}

	/**
	 * 媒体案例
	 * 
	 * @param row
	 * @param media
	 */
	private void getMediaCases(HSSFRow row, Media media) {
		Date now = new Date();
		int index = 12;
		Set<MediaCase> cases = new HashSet<MediaCase>();
		
		for (int i=0; i<2; i++) {
			String title = getStringCellValue(row.getCell(index++));
			String light = getStringCellValue(row.getCell(index++));
			String content = getStringCellValue(row.getCell(index++));
			if (StringUtils.isNotBlank(title)) {
				MediaCase mc = new MediaCase();
				mc.setTitle(title);
				mc.setLight(light);
				mc.setContent(content);
				mc.setMedia(media);
//				mc.setCreateBy(UserContext.getSystemUser().getId());
//				mc.setModifyBy(UserContext.getSystemUser().getId());
				mc.setCreateTime(now);
				mc.setModifyTime(now);
				cases.add(mc);
			} else {
				break;
			}
		}

		media.setMediaCases(cases);
	}

	/**
	 * 获取excel单元格内容,去掉前后空格
	 * 
	 * @param cell
	 * @return
	 */
	private String getStringCellValue(HSSFCell cell) {
		try {
			if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
				return String.valueOf((long)cell.getNumericCellValue());
			}
			return cell.getStringCellValue().trim();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取excel单元格内容
	 * 
	 * @param cell
	 * @return
	 * @throws Exception 
	 */
	private int getIntCellValue(HSSFCell cell) throws Exception {
		if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			return Integer.parseInt(cell.getStringCellValue());
		}
		if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			return (int) cell.getNumericCellValue();
		}
		
		throw new Exception("单元格格式不对！");
	}

	/**
	 * 按名称获取地区,没有找到返回"ALL"(中国)
	 * 
	 * @param name
	 * @return
	 */
	private String getRegion(String name) {
		try {
			return areaService.findTopByName(name).getId();
		} catch (Exception e) {
			return "ALL";
		}
	}

	/**
	 * 按名称获取字典项
	 * 
	 * @param itemName
	 * @return
	 * @throws Exception 
	 */
	private String getItemCode(String itemName) throws Exception {
		try {
			return dicService.findByItemName(itemName).getItemCode();
		} catch (Exception e) {
			throw new Exception(String.format("字典项【%s】未找到!", itemName));
		}
	}
	

	/**
	 * 按名称获取适合产品
	 * 
	 * @param names
	 * @return
	 * @throws Exception 
	 */
	private String getItemCodes(String names) throws Exception {
		List<String> list = new ArrayList<>();
		for (String name : names.split(";")) {
			String itemCode = this.getItemCode(name);
			if (itemCode != null) {
				list.add(itemCode);
			}
		}
		return StringUtils.join(list, ",");
	}

	/**
	 * 按大类获取媒体对象:大类名字中包含字眼"微博",返回微博媒体,否则返回微信媒体
	 * 
	 * @param type
	 * @return
	 * @throws Exception 
	 */
	private Media getMedia(String type) throws Exception {
		if (type == null) {
			throw new Exception("媒体大类为空");
		} else if (type.equals("微博")) {
			MediaWeibo media = new MediaWeibo();
			media.setMediaType(Constants.MediaType.WEIBO);
			media.setWeiboPlatform("SINA");
			return media;
		} else if (type.equals("微信")) {
			MediaWeixin media = new MediaWeixin();
			media.setMediaType(Constants.MediaType.WEIXIN);
			media.setWeixinCode("TENCENT");
			return media;
		} else {
			throw new Exception("非法媒体大类：" + type);
		}
	}

	/**
	 * 返回生成11数字当做电话号码
	 * 
	 * @return
	 */
	private String getMobPhone() {
		return String.valueOf(this.mobPhoneSeed++);
	}

	public String getLogoDir() {
		return logoDir;
	}

	public void setLogoDir(String logoDir) {
		this.logoDir = logoDir;
	}

	public String getXls() {
		return xls;
	}

	public void setXls(String xls) {
		this.xls = xls;
	}

}
