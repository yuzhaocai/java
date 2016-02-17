package com.lczy.media.controller.admin;

import java.io.File;
import java.util.List;

import javax.servlet.ServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.lczy.media.service.MediaImportService;

/**
 * 媒体导入
 * 
 * @author tiger
 * 
 */
@Controller
@RequestMapping("/admin/mediaImport")
public class AdminMediaImportController {

	private static Logger log = LoggerFactory.getLogger(AdminMediaImportController.class);

	@Autowired
	private MediaImportService mediaImportService;

	/**
	 * 表单页
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "", "/" })
	public String index(ServletRequest request, Model model) {
		return "/admin/mediaImport/index";
	}

	/**
	 * 导入媒体
	 * 
	 * @param media
	 * @param logo
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importMedia(MultipartFile media, MultipartFile logo, Model model) {
		try {
			// 在java临时文件夹解压文件
			String tmpdir = System.getProperty("java.io.tmpdir");
			log.info("tmp dir: {}", tmpdir);
			File dir = new File(new File(tmpdir), "mediaImport");
			log.info("media import dir: {}", dir.getAbsolutePath());
			
			if (!dir.exists()) {
				dir.mkdirs();
			}

			File mediaFile = new File(dir, media.getOriginalFilename());
			media.transferTo(mediaFile);
			File logoFile = new File(dir, logo.getOriginalFilename());
			logo.transferTo(logoFile);

			// 解压logo文件，并等待其结束
			String command = "unzip -o " + logoFile.getAbsolutePath() + " -d " + dir.getAbsolutePath();
			log.info("unzip command: {}", command);
			Process process = Runtime.getRuntime().exec(command);
			int exit = process.waitFor();
			log.info("unzip exit value: {}", exit);
			
			mediaImportService.setLogoDir(new File(dir, "logo").getAbsolutePath());
			mediaImportService.setXls(mediaFile.getAbsolutePath());
			List<String> result = mediaImportService.importMedia();
			model.addAttribute("result", result);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		return "/admin/mediaImport/result";
	}
}
