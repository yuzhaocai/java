/**
 * 
 */
package com.lczy.media.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lczy.common.util.MessageBean;
import com.lczy.media.solr.IndexBuilder4Media;
import com.lczy.media.solr.IndexBuilder4Requirement;

/**
 * @author wu
 *
 */
@Controller
@RequestMapping("/admin/solr")
public class SolrAdminController {
	
	private Logger log = LoggerFactory.getLogger(SolrAdminController.class);
	
	@Autowired
	private IndexBuilder4Media indexBuilder4Media;
	
	@Autowired
	private IndexBuilder4Requirement indexBuilder4Requirement;

	@RequestMapping({"", "/"})
	public String main() {
		return "admin/solr/main";
	}
	
	@RequestMapping(value="rebuildMedia", method = RequestMethod.POST)
	public String rebuildMedia(RedirectAttributes redirectAttrs) {
		try {
			indexBuilder4Media.rebuild();
			redirectAttrs.addFlashAttribute("message", new MessageBean(1, "操作成功！").toJSON());
		} catch (Exception e) {
			log.warn("重建媒体索引失败", e);
			redirectAttrs.addFlashAttribute("message", new MessageBean(0, "操作失败！").toJSON());
		}
		
		return "redirect:/admin/solr";
	}
	
	@RequestMapping(value="rebuildRequirement", method = RequestMethod.POST)
	public String rebuildRequirement(RedirectAttributes redirectAttrs) {
		try {
			indexBuilder4Requirement.rebuild();
			redirectAttrs.addFlashAttribute("message", new MessageBean(1, "操作成功！").toJSON());
		} catch (Exception e) {
			log.warn("重建需求索引失败", e);
			redirectAttrs.addFlashAttribute("message", new MessageBean(0, "操作失败！").toJSON());
		}
		return "redirect:/admin/solr";
	}
	
}
