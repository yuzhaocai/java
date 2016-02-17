package com.lczy.media.controller.admin;

import java.util.Date;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.ctc.wstx.util.StringUtil;
import com.lczy.common.util.MessageBean;
import com.lczy.common.web.WebHelper;
import com.lczy.media.entity.AdvSetting;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.OtherMedia;
import com.lczy.media.service.AdvSettingService;
import com.lczy.media.service.MediaService;
import com.lczy.media.service.OtherMediaService;
import com.lczy.media.util.Constants;
import com.lczy.media.util.FileServerUtils;
import com.lczy.media.util.UserContext;

@Controller
@RequestMapping(value = "/admin/advSetting")
public class AdminAdvSettingController {
	
	@Autowired
	private AdvSettingService advSettingService;
	
	@Autowired
	private MediaService mediaService;
	
	@Autowired
	private OtherMediaService otherMediaService;
	
	@RequestMapping(value={"", "/index"})
	public String index(Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		String sort = WebHelper.getString(request, "sort");
		if (StringUtils.isBlank(sort)) {
			//按权重排序
			sort = "DESC_weight";
		}
		Page<AdvSetting> data= advSettingService.find(searchParams, page, size, sort);
		
		model.addAttribute("data", data);
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/admin/advSetting/index";
	}
	
	
	
	@RequestMapping(value="/addAdv")
	public String advIndex(Model model,RedirectAttributes redirectAttributes) {
		return "/admin/advSetting/addAdv";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String addAdv(@RequestParam("images") MultipartFile[] images,
			AdvSetting advSetting,RedirectAttributes redirectAttributes) throws Exception  {
		if(StringUtils.isNotBlank(advSetting.getMediaId())){
			//上传媒体ID
			Media media = mediaService.get(advSetting.getMediaId());
			String type="";
			if(null!=media){
				if(media.getMediaType().equals(Constants.MediaType.WEIXIN)){
					type="weixin";
				}else if(media.getMediaType().equals(Constants.MediaType.WEIBO)){
					type="weibo";
				}
				if(StringUtils.isNotBlank(media.getTags())&&media.getTags().contains("000151")){
					advSetting.setCoop(true);
				}
				advSetting.setTitle(media.getName());
				advSetting.setLink(type+"/detail/"+media.getId());
				advSetting.setPic(media.getShowPic());
			}else{
				OtherMedia omedia = otherMediaService.get(advSetting.getMediaId());
				if(null!=omedia){
					advSetting.setTitle(omedia.getName());
					advSetting.setLink("other/view/"+omedia.getId());
					advSetting.setPic(omedia.getShowPic());
				}else{
					//上传媒体ID错误
					redirectAttributes.addFlashAttribute("message","不存在媒体，请确认后再次上传！");
					return "redirect:/admin/advSetting/addAdv";
				}
				
			}
		}else{
			String pic = "";// 媒体创建案例图片
			if (images != null && images.length > 0) {
					MultipartFile img = null;
					img = images[0];
					String fileId = FileServerUtils.upload(null,
							img.getOriginalFilename(), img.getBytes(), false,
							"image");
					pic = fileId;
			}
			advSetting.setPic(pic);
		}
		advSetting.setCreateBy(UserContext.getCurrent().getId());
		advSetting.setCreateTime(new Date());
		advSettingService.save(advSetting);
		return "/admin/advSetting/success";
	}
	
	@RequestMapping(value = "/delete")
	public String delete(String id, RedirectAttributes redirectAttributes) throws Exception  {
		AdvSetting as = advSettingService.get(id);
		if(null!=as){
			advSettingService.deleteAdv(as);
			redirectAttributes.addFlashAttribute("message","删除成功！");
		}
		return "redirect:/admin/advSetting";
	}
	
	
	/**
	 * 修改权重
	 * @param id
	 * @param weight
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/weight")
	@ResponseBody
	public MessageBean weight(String id,int weight, RedirectAttributes redirectAttributes) throws Exception  {
		AdvSetting as = advSettingService.get(id);
		MessageBean bean = null;
		if(null!=as){
			as.setWeight(weight);
			advSettingService.save(as);
			bean = new MessageBean(1,"修改成功！");
		}
		return bean;
	}
}
