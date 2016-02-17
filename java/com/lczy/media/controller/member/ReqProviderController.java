package com.lczy.media.controller.member;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.lczy.common.util.MessageBean;
import com.lczy.common.web.Token;
import com.lczy.common.web.Token.Type;
import com.lczy.common.web.WebHelper;
import com.lczy.media.controller.common.BaseController;
import com.lczy.media.entity.ReqMedia;
import com.lczy.media.entity.Requirement;
import com.lczy.media.service.CustomerService;
import com.lczy.media.service.ReqMediaService;
import com.lczy.media.service.RequirementService;
import com.lczy.media.service.common.AreaProvider;
import com.lczy.media.service.common.DicProvider;
import com.lczy.media.util.Constants;
import com.lczy.media.util.FileServerUtils;
import com.lczy.media.util.JsonBean;
import com.lczy.media.util.UserContext;


/**
 * @Description 媒体主待处理需求控制器.
 * @author Zhang.CJ
 * @date 2015年7月31日 下午3:27:37
 */

@Controller
@RequestMapping("/member/req/deal")
public class ReqProviderController extends BaseController{

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ReqMediaService reqMediaService;
	
	@Autowired
	private RequirementService requirementService;
	
	@Autowired
	protected DicProvider dicProvider;
	
	@Autowired
	private AreaProvider areaProvider;
	
	public static final List<String> ALLOWED_MATTER_FILE_TYPES = Lists.newArrayList("doc", "docx", "pdf","jpg", "png", "bmp");
	
	/**
	 * 待我处理的需求列表.
	 */
	@RequestMapping({ "","/workList" })
	public String workList(Model model,HttpServletRequest request) {
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);

		String sort = "ASC_fbTime,DESC_createTime";
		Map<String, Object> searchParams = getSearchParams(request);
		searchParams.put("EQ_requirement.status", Constants.ReqStatus.NORMAL);
		searchParams.put("EQ_media.customer.id", UserContext.getCurrent()
				.getCustomer().getId());
		Page<ReqMedia> aPage = reqMediaService.find(searchParams, page, size, sort);
		
		setModalAttrsForPaging(model, searchParams, aPage, sort);
		return "member/req/deal/workList";
	}

	/**
	 * 待我处理的需求的应邀.
	 */
	@RequestMapping({ "/accept" })
	public String accept(Model model, String reqMediaId) {
		reqMediaService.dealAccept(reqMediaId);
		return "redirect:/member/req/deal/workList";
	}

	
	/**
	 * 弹出拒绝.
	 */
	@RequestMapping(value = "/preRefuse")
	@Token
	public String preRefuse(Model model,String id) {
		
		model.addAttribute("reqmedia", reqMediaService.get(id));
		model.addAttribute("rejectReasons", dicProvider.getDic(Constants.RejectReason.DIC_CODE).getDicItems());
		
		return "member/req/deal/refuseModal";
	}
	
	
	/**
	 * 弹出改稿应邀.
	 */
	@RequestMapping(value = "/preChange")
	@Token
	public String preChange(Model model, String id) {
		model.addAttribute("reqmedia", reqMediaService.get(id));
		return "member/req/deal/changeFileModal";
	}
	
	/**
	 * 待我处理的需求的拒绝.
	 */
	@RequestMapping(value = "/refuse", method = RequestMethod.POST)
	@Token(Type.REMOVE)
	public String refuse(Model model, String reqMediaId,
			String refuseReason) {
		reqMediaService.dealRefuse(reqMediaId, refuseReason);
		return "redirect:/member/req/deal/workList";
	}
	
	/**
	 * 待我处理需求的改稿应邀.
	 * 
	 */
	@RequestMapping(value = "/change", method = RequestMethod.POST)
	@Token(Type.REMOVE)
	public String rewrite(Model model, String reqMediaId,
			@RequestParam("image") MultipartFile image, String changedReason,
			RedirectAttributes redirectAttributes) throws Exception {
		MessageBean bean = null;
		String suffix = image.getOriginalFilename().substring(
				image.getOriginalFilename().lastIndexOf(".") + 1);
		if (ALLOWED_MATTER_FILE_TYPES.contains(suffix)) {
			String fileId = FileServerUtils.upload(null,
					image.getOriginalFilename(), image.getBytes(), false,
					suffix);
			Date now = new Date();
			ReqMedia reqMedia = reqMediaService.findOne(reqMediaId);
			if (reqMedia != null) {
				reqMedia.setFbStatus(Constants.MediaFeedback.ACCEPT);
				reqMedia.setFbTime(now);
				reqMedia.setChanged(true);
				reqMedia.setChangedReason(changedReason);
				reqMedia.setChangedArticle(fileId);
				reqMediaService.update(reqMedia);
			}
		} else {
			bean = new MessageBean(0, "请您上传正确格式的附件！");
			redirectAttributes.addFlashAttribute("message", bean.toJSON());
		}
		return "redirect:/member/req/deal/workList";
	}
	
	
	/**
	 * 查看需求详情
	 */
	@RequestMapping(value = "/view")
	public String view(String id, Model model){
		Requirement requirement = requirementService.get(id);
		requirement.setRegions(areaProvider.getAreaNames(Arrays.asList(requirement.getRegions().split(","))));
		
		model.addAttribute("req", requirement);
		return "/member/req/deal/view";
	}
}
