package com.lczy.media.controller;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lczy.common.web.WebHelper;
import com.lczy.media.controller.common.BaseController;
import com.lczy.media.entity.FavoritesMedia;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.OtherMedia;
import com.lczy.media.service.FavoritesMediaService;
import com.lczy.media.util.Constants;
import com.lczy.media.util.FileServerUtils;
import com.lczy.media.util.JsonBean;
import com.lczy.media.util.UserContext;

/** 收藏媒体控制器
 * @author wang.xiaoxiang
 *
 */
@Controller
@RequestMapping("/member/favoritesMedia")
public class FavoritesMediaController extends BaseController {
	
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private FavoritesMediaService favoritesMediaService;
	
	@RequestMapping(value={"", "list"})
	public String list(Model model, HttpServletRequest request) {
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		String sort = WebHelper.getString(request, "sort");
		Map<String, Object> searchParams = getSearchParams(request);
		searchParams.put("EQ_customer.id", UserContext.getCurrentCustomer().getId());
		searchParams.put("EQ_type", Constants.FavoritesMediaType.WEIXIN);
		Page<FavoritesMedia> aPage =  favoritesMediaService.find(searchParams, page, size, sort);
		for (FavoritesMedia fm : aPage.getContent()){
			fm.getMedia().setShowPic(FileServerUtils.getFileUrl(fm.getMedia().getShowPic()));
		}
		setModalAttrsForPaging(model, searchParams, aPage, sort);
		return "member/favorites/media/list";
	}
	
	@RequestMapping(value="weibo")
	public String loadWeixin(Model model, HttpServletRequest request) {
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		String sort = WebHelper.getString(request, "sort");
		Map<String, Object> searchParams = getSearchParams(request);
		searchParams.put("EQ_customer.id", UserContext.getCurrentCustomer().getId());
		searchParams.put("EQ_type", Constants.FavoritesMediaType.WEIBO);
		Page<FavoritesMedia> aPage =  favoritesMediaService.find(searchParams, page, size, sort);
		for (FavoritesMedia fm : aPage.getContent()){
			fm.getMedia().setShowPic(FileServerUtils.getFileUrl(fm.getMedia().getShowPic()));
		}
		setModalAttrsForPaging(model, searchParams, aPage, sort);
		return "member/favorites/media/weibo";
	}
	
	@RequestMapping("other")
	public String otherMedia(Model model, HttpServletRequest request) {

		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		String sort = WebHelper.getString(request, "sort");
		Map<String, Object> searchParams = getSearchParams(request);
		searchParams.put("EQ_customer.id", UserContext.getCurrentCustomer().getId());
		searchParams.put("EQ_type", Constants.FavoritesMediaType.OTHERMEDIA);
		Page<FavoritesMedia> aPage =  favoritesMediaService.find(searchParams, page, size, sort);
		for (FavoritesMedia fm : aPage.getContent()){
			fm.getOtherMedia().setShowPic(FileServerUtils.getFileUrl(fm.getOtherMedia().getShowPic()));
		}
		setModalAttrsForPaging(model, searchParams, aPage, sort);
	
		return "member/favorites/media/other";
	}
	
	@ResponseBody
	@RequestMapping(value="add")
	public String add(Model model, HttpServletRequest request) {
		String mediaId = (String) request.getParameter("mediaId");
		String type = (String) request.getParameter("type");
		JsonBean result = new JsonBean();
		try {
			FavoritesMedia entity = new FavoritesMedia();
			entity.setType(type);
			if (type.equals(Constants.FavoritesMediaType.OTHERMEDIA)){
				OtherMedia otherMedia = new OtherMedia();
				otherMedia.setId(mediaId);
				entity.setOtherMedia(otherMedia);
			} else {
				Media media = new Media();
				media.setId(mediaId);
				entity.setMedia(media);
			}
			entity.setCustomer(UserContext.getCurrentCustomer());
			favoritesMediaService.save(entity);
			result.put("result", true);
		} catch (Exception e) {
			log.error("添加收藏失败", e);
			result.put("result", false);
		}
		return result.toJson();
	}
	
	@ResponseBody
	@RequestMapping(value="delete")
	public String delete(Model model, HttpServletRequest request) {
		String mediaId = (String) request.getParameter("mediaId");
		String type = (String) request.getParameter("type");
		JsonBean result = new JsonBean();
		try {
			Map<String, Object> searchParams = getSearchParams(request);
			searchParams.put("EQ_customer.id", UserContext.getCurrentCustomer().getId());
			if (type.equals(Constants.FavoritesMediaType.OTHERMEDIA)){
				searchParams.put("EQ_otherMedia.id", mediaId);
			} else {
				searchParams.put("EQ_media.id", mediaId);
			}
			FavoritesMedia favoritesMedia = favoritesMediaService.findOne(searchParams);
			favoritesMediaService.remove(favoritesMedia);
			result.put("result", true);
		} catch (Exception e){
			log.error("取消关注失败", e);
			result.put("result", false);
		}
		return result.toJson();
	}
	
	@ResponseBody
	@RequestMapping(value="isFavorites")
	public String isFavorites(Model model, HttpServletRequest request) {
		String mediaId = (String) request.getParameter("mediaId");
		String type = (String) request.getParameter("type");
		JsonBean result = new JsonBean();
		try {
			Map<String, Object> searchParams = getSearchParams(request);
			searchParams.put("EQ_customer.id", UserContext.getCurrentCustomer().getId());
			if (type.equals(Constants.FavoritesMediaType.OTHERMEDIA)){
				searchParams.put("EQ_otherMedia.id", mediaId);
			} else {
				searchParams.put("EQ_media.id", mediaId);
			}
			FavoritesMedia favoritesMedia = favoritesMediaService.findOne(searchParams);
			if (favoritesMedia != null){
				result.put("favorites", true);
			} else {
				result.put("favorites", false);
			}
			result.put("result", true);
		} catch (Exception e){
			log.error("获取是否关注失败", e);
			result.put("favorites", false);
			result.put("result", false);
		}
		return result.toJson();
	}
	
}
