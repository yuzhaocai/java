package com.lczy.media.controller.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lczy.media.service.MediaTagService;

@Controller
@RequestMapping("/admin/media/mediaTagImport")
public class AdminMediaTagImportController {
	
	private static Logger log = LoggerFactory.getLogger(AdminMediaTagImportController.class);
	
	@Autowired
	private MediaTagService mediaTagService;
	
	/**
	 * 媒体标签 - 首页
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"", "/"})
	public String index(ServletRequest request, Model model) {
		
		return "/admin/media/mediaTagImport/index";
	}
	
	
	/**
	 * 媒体标签 - 创建
	 * 
	 * @param tag
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importTags(@RequestParam("file") MultipartFile file,@RequestParam("mediaType") String mediaType,  RedirectAttributes redirectAttributes) {
		try {
			Map<String, Object> result = mediaTagService.importMediaTags(mediaType, file);
			List<String> faildMsgs = (List<String>) result.get("faildMsg");
			StringBuilder errorMsg = new StringBuilder();
			for (String error : faildMsgs) {
				errorMsg.append(error).append(";");
			}
			String errors = errorMsg.toString();
			String msg = "导入结果：" + "总共" + result.get("total") + "条；成功" + result.get("success") + "条;";
			if (errors.length() > 0) {
				msg = msg + errors;
			}
			redirectAttributes.addFlashAttribute("message", msg);
		} catch (Exception e){
			log.error("导入失败", e);
			redirectAttributes.addFlashAttribute("message", "导入失败 :" + e.getMessage());
		}
		return "redirect:/admin/media/mediaTagImport";
	}
	
	
}
