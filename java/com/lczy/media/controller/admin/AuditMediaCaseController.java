package com.lczy.media.controller.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.lczy.common.util.MessageBean;
import com.lczy.common.web.Token;
import com.lczy.common.web.Token.Type;
import com.lczy.media.controller.common.BaseController;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.Dic;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaCase;
import com.lczy.media.service.MediaCaseService;
import com.lczy.media.service.MediaService;
import com.lczy.media.service.common.DicProvider;
import com.lczy.media.util.Constants;
import com.lczy.media.util.FileServerUtils;
import com.lczy.media.util.UserContext;
import com.lczy.media.vo.MediaCaseVO;

/**
 * 案例 controller
 * @author zhang.cj
 *
 */
@Controller
@RequestMapping(value = "/admin/audit/media/case")
public class AuditMediaCaseController extends BaseController{

	@Autowired
	private DicProvider dicProvider;

	@Autowired
	private MediaService mediaService;

	@Autowired
	private MediaCaseService mediaCaseService;
	
	public static final List<String> ALLOWED_MATTER_FILE_TYPES = Lists.newArrayList("jpg", "png", "bmp");

	@RequestMapping({ "", "/list" })
	public String list(Model model, String mediaId,
			RedirectAttributes redirectAttributes) throws Exception {
		Media media = mediaService.get(mediaId);
			Set<MediaCase> cases = media.getMediaCases();
			model.addAttribute("cases", cases);

			String[] dics = new String[] { Constants.WeixinService.DIC_CODE,
					Constants.WeiboService.DIC_CODE };
			for (int i = 0; i < dics.length; i++) {
				Dic data = dicProvider.getDicMap().get(dics[i]);
				if (i == 0)
					model.addAttribute("weixinService", data);
				if (i == 1)
					model.addAttribute("weiboService", data);
			}
			model.addAttribute("media", media);
			return "admin/media/case/list";
		
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@Token(Type.REMOVE)
	public String save(@RequestParam("images") MultipartFile[] images,
			MediaCase mCase, String mediaId, MediaCaseVO vo,
			@RequestParam(value="pics", required=false) String[] pics,
			RedirectAttributes redirectAttributes) throws Exception {
		MessageBean bean = null;
		if (images != null && images.length > 0) {
			if (checkPicAvailable(images)) {
				bean = new MessageBean(0, "请您上传2Mb以内的图片格式文件！");
				redirectAttributes.addFlashAttribute("message", bean.toJSON());
				return "redirect:list?mediaId=" + mediaId;
			}
		}

		Customer customer = UserContext.getCurrent().getCustomer();
		String customerId = customer.getId();
		Date now = new Date();
		Media media = mediaService.get(mediaId);

		String showPicUrl = "";// 媒体创建案例图片
		if (images != null && images.length > 0) {
			for (int i = 0; i < images.length; i++) {
				MultipartFile img = null;
				String url = null;
				if (images[i].getSize() > 0) {
					img = images[i];
					String fileId = FileServerUtils.upload(null,
							img.getOriginalFilename(), img.getBytes(), false,
							"image");
					url = fileId + ";";
					showPicUrl += url;
				} else {
					if(null!=pics&&pics.length>0)
						if(pics[i].trim().length()>0)
							showPicUrl += pics[i] + ";";
				}
			}
		}

		if (mCase != null && mCase.getId().trim().length() < 1) {
			mCase.setMedia(media);
			mCase.setCreateBy(customerId);
			mCase.setCreateTime(now);
			mCase.setModifyBy(customerId);
			mCase.setModifyTime(now);
			if (!Strings.isNullOrEmpty(showPicUrl))
				mCase.setShowPic(showPicUrl);
			mediaCaseService.save(mCase);
		} else {
			MediaCase temp = mediaCaseService.get(mCase.getId());
			temp.setTitle(mCase.getTitle());
			temp.setLight(mCase.getLight());
			temp.setContent(mCase.getContent());
			temp.setShowPic(showPicUrl);
			temp.setModifyBy(customerId);
			temp.setModifyTime(now);
			mediaCaseService.save(temp);
		}
		return "redirect:list?mediaId=" + mediaId;
	}
	
	
	@RequestMapping(value = "/create/{mediaId}")
	@Token
	public String create(@PathVariable String mediaId, Model model){
		model.addAttribute("mediaId", mediaId);
		return "admin/media/case/createModal";
	}

	@RequestMapping(value = "edit")
	@Token
	public String edit(String caseId, Model model) throws Exception{
		MediaCase mediaCase = mediaCaseService.get(caseId);
		
		model.addAttribute("mediaId", mediaCase.getMedia().getId());
		model.addAttribute("caseId", caseId);
		model.addAttribute("case", mediaCase);
		model.addAttribute("pics", getPics(mediaCase));
		
		return "admin/media/case/editModal";
	}

	@RequestMapping("/view")
	public String certificate(String caseId,Model model) throws Exception {
		MediaCase mCase = mediaCaseService.get(caseId);

		model.addAttribute("pics", getPics(mCase));
		return "admin/media/case/viewCaseModal";
	}

	@RequestMapping({ "/delete" })
	public String delete(String mediaId, String caseId) {
		mediaCaseService.remove(caseId);
		return "redirect:list?mediaId=" + mediaId;
	}
	
	
	/**
	 * 获取图片id
	 * @param mCase
	 * @return
	 */
	private List<String> getPics(MediaCase mCase) {

		List<String> mCasePic = new ArrayList<String>();
		if (StringUtils.isNotBlank(mCase.getShowPic())) {
			String[] pics = mCase.getShowPic().split(";");
			for (String pic : pics) {
				mCasePic.add(pic);
			}
		}
		return mCasePic;
	}
	
	/**
	 * 判断文件格式，图片大小是否可用.
	 * 
	 * @param images
	 * @return
	 */
	private boolean checkPicAvailable(MultipartFile[] images) {
		boolean flag = false;
		if (images != null && images.length > 0) {
			for (int i = 0; i < images.length; i++) {
				if (images[i].getSize() > 0) {
					String suffix = images[i].getOriginalFilename()
							.substring(
									images[i].getOriginalFilename()
											.lastIndexOf(".") + 1);
					if (!ALLOWED_MATTER_FILE_TYPES.contains(suffix))
						flag = true;
					if (images[i].getSize() > 2 * 1024 * 1024) {
						flag = true;
						break;
					}
				}
			}
		}
		return flag;
	}
	
}
