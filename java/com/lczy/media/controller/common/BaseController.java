package com.lczy.media.controller.common;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.web.Servlets;

import com.lczy.common.util.Files;
import com.lczy.common.web.WebHelper;
import com.lczy.media.util.FileServerUtils;

public abstract class BaseController {

	/**
	 * 组装设置查询条件.
	 */
	protected Map<String, Object> getSearchParams(HttpServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		
		String startTime = (String)searchParams.get("GTE_createTime");
		String endTime = (String)searchParams.get("LTE_createTime");
		if (StringUtils.isNotBlank(startTime)) {
			DateTime start = DateTime.parse(startTime + "T00:00:00");
			searchParams.put("GTE_createTime", start.toDate());
		}
		if (StringUtils.isNotBlank(endTime)) {
			DateTime end = DateTime.parse(endTime + "T23:59:59.999");
			searchParams.put("LTE_createTime", end.toDate());
		}
		
		return searchParams;
	}
	
	/**
	 * 获取排序字段，默认为按创建时间倒排序.
	 */
	protected String getSort(HttpServletRequest request) {
		String sort = WebHelper.getString(request, "sort");
		if (StringUtils.isBlank(sort)) {
			sort = "DESC_createTime";
		}
		
		return sort;
	}
	
	/**
	 * 设置暴露给视图的模型属性.
	 */
	protected void setModalAttrsForPaging(Model model,
			Map<String, Object> searchParams, Page<?> aPage, String sort) {
		
		model.addAttribute("data", aPage);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		model.addAttribute("sort", sort);
	}
	
	/**
	 * 把文件上传到文件服务器上.
	 */
	protected String uploadFile(MultipartFile mpf) {
		if (mpf != null && !mpf.isEmpty()) {
			String filename = mpf.getOriginalFilename();
			String filetype = filename.substring(filename.lastIndexOf(".") + 1,
					filename.length());
			File temp = null;
			try {
				temp = File.createTempFile("mediacube", "upd");
				Files.write(temp, mpf.getInputStream());
				String fid = FileServerUtils.upload(null, filename, temp,
						false, filetype, false);

				return fid;
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				if (temp != null)
					temp.delete();
			}
		} else {
			return null;
		}
	}
}
