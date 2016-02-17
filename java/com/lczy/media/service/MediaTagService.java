package com.lczy.media.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lczy.common.service.AbstractService;
import com.lczy.media.controller.admin.AdminMediaTagImportController;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaTag;
import com.lczy.media.repositories.MediaTagDao;
import com.lczy.media.util.Constants;

@Service
@Transactional(readOnly=true)
public class MediaTagService extends AbstractService<MediaTag> {
	
	
	private static Logger log = LoggerFactory.getLogger(AdminMediaTagImportController.class);
	
	@Autowired
	private MediaService mediaService;

	public List<MediaTag> findHotTags() {
		
		return getDao().findHotTags();
	}
	
	public List<MediaTag> findRecTags() {
		
		return getDao().findRecTags();
	}
	
	public MediaTag findByTagName(String name) {
		return getDao().findTopByName(name);
	}
	
	public List<MediaTag> findFitTags(String[] tagsName) {
		return getDao().findFitTags(tagsName);
	}
	
	public MediaTagDao getDao() {
		return (MediaTagDao) super.getDao();
	}
	
	/**
	 * 标签的计数 +1。
	 */
	@Transactional(readOnly=false)
	public void increase(String id) {
		getDao().increase(id);
	}
	
	/**
	 * 标签的计数 -1。
	 */
	@Transactional(readOnly=false)
	public void decrease(String id) {
		getDao().decrease(id);
	}
	
	/**
	 * 查询媒体详情页的标签。
	 */
	public List<MediaTag> findMediaTags(String[] tagsId){
		return getDao().findMediaTags(tagsId);
	}
	@Transactional(readOnly = false)
	public Map<String, Object> importMediaTags(String mediaType, MultipartFile file) throws Exception {
		InputStream is = file.getInputStream();
		Workbook wb = WorkbookFactory.create(is);
		Sheet sheet = wb.getSheetAt(0);
		List<String> faildMsg = Lists.newArrayList();
		Map<String, Object> map = parseExcelRows(sheet, faildMsg);
		int success = 0;
		if (Constants.MediaType.WEIBO.equals(mediaType)){
			for (String key : map.keySet()) {
				Map<String, Object> searchParams = Maps.newLinkedHashMap();
				searchParams.put("EQ_name", key);
				searchParams.put("EQ_mediaType", Constants.MediaType.WEIBO);
				Media weibo = null;
				try {
					weibo = mediaService.findOne(searchParams);
				} catch (Exception e) {
					faildMsg.add("查询微博出错:"+ key);
					log.error("error in seach weibo:" + key, e);
					continue;
				}
				if (weibo == null) {
					log.error("未找到微博昵称:"+ key);
					faildMsg.add("未找到微博昵称:"+ key);
					continue;
				}
				String tags = (String) map.get(key);
				String[] tagValues = tags.split(";");
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < tagValues.length; i ++) {
					MediaTag tag = findByTagName(tagValues[i]);
					if (i != tagValues.length -1){
						sb.append(tag.getId()).append(",");
					} else {
						sb.append(tag.getId());
					}
				}
				if (sb.length() == 0) {
					log.error("未找到标签:"+ tags);
					faildMsg.add("未找到标签:"+ tags);
					continue;
				}
				weibo.setTags(sb.toString());
				mediaService.save(weibo);
				success ++;
			}
		} else {
			for (String key : map.keySet()) {
				Map<String, Object> searchParams = Maps.newLinkedHashMap();
				searchParams.put("EQ_account", key);
				searchParams.put("EQ_mediaType", Constants.MediaType.WEIXIN);
				Media weixin = null;
				try {
					weixin = mediaService.findOne(searchParams);
				} catch (Exception e) {
					faildMsg.add("查询微信出错:"+ key);
					log.error("error in seach weixin:" + key, e);
					continue;
				}
				if (weixin == null) {
					log.error("未找到微信账号:"+ key);
					faildMsg.add("未找到微信账号:"+ key);
					continue;
				}
				String tags = (String) map.get(key);
				String[] tagValues = tags.split(";");
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < tagValues.length; i ++) {
					MediaTag tag = findByTagName(tagValues[i]);
					if (i != tagValues.length -1){
						sb.append(tag.getId()).append(",");
					} else {
						sb.append(tag.getId());
					}
				}
				if (sb.length() == 0) {
					log.error("未找到标签:"+ tags);
					faildMsg.add("未找到标签:"+ tags);
					continue;
				}
				weixin.setTags(sb.toString());
				mediaService.save(weixin);
				success ++;
			}
			
		}
		Map<String, Object> result = Maps.newHashMap();
		result.put("success", success);
		result.put("total", map.size());
		result.put("faildMsg", faildMsg);
		return result;
	}
	
	private Map<String, Object> parseExcelRows(Sheet sheet, List<String> failMsg) {
		int rowNums = sheet.getLastRowNum() - sheet.getFirstRowNum() + 1;
		Map<String, Object> map = Maps.newHashMap();
        for (int i = sheet.getFirstRowNum() + 1; i < rowNums; i++) {
        	int currentRow = i;
            Row row = sheet.getRow(currentRow);
            if (row == null || row.getZeroHeight()) {
                continue;
            }
            if (row.getCell(0) == null || row.getCell(0) == null) {
            	log.warn("row is null in excel rowNum:", i);
            	continue;
            }
            try {
            	String name  = readCellValue(row.getCell(0));
            	String tags  = readCellValue(row.getCell(1));
            	map.put(name, tags);
            } catch (Exception e) {
            	log.error("error in read Cell Value" ,e);
            	failMsg.add("无法获取数据的值 在第:" + i + "行");
            }
        }
        return map;
	}
	
    /**
     * @param cell 单元格
     * @return 返回单元格的字符串值
     */
    protected String readCellValue(Cell cell) {
        Object cellValue = null;
        if (cell == null) {
            return null;
        }
        log.debug("cell type is " + cell.getCellType());
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                cellValue = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    cellValue = cell.getDateCellValue();
                } else {
                    cellValue = cell.getNumericCellValue();
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_BLANK:
                cellValue = "";
                break;
            default:
                cellValue = cell.toString();
                break;
        }
        if (cellValue != null && cellValue instanceof String) {
        	cellValue = (String) cellValue;
        	if ("".equals(cellValue)) {
        		cellValue = null;
        	}
        }
        if (log.isTraceEnabled()) {
        	log.trace("cell row {} column {}  value is {}" , cell.getRowIndex(), cell.getColumnIndex(), cellValue);
        }
        return String.valueOf(cellValue);
    }
	
}
