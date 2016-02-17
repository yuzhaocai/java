package com.lczy.media.controller.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lczy.common.web.Token;
import com.lczy.media.entity.Area;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaQuote;
import com.lczy.media.service.MediaQuoteService;
import com.lczy.media.service.MediaService;
import com.lczy.media.service.common.AreaProvider;
import com.lczy.media.service.common.DicProvider;
import com.lczy.media.solr.MediaDoc;
import com.lczy.media.util.Constants;
import com.lczy.media.util.Constants.IndustryType;
import com.lczy.media.util.Constants.MediaType;
import com.lczy.media.vo.MediaQuoteVo;
import com.lczy.media.vo.RequirementVO;


/**
 * 采媒车
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/shoppingcart")
public class ShoppingCartController {

	private static final String MEDIAS_ARRAY = "mediasArray"; 
	
	private static final String ADD_SUCCESS_MESSAGE ="成功添加该媒体至采媒车";
	private static final String DELETE_SUCCESS_MESSAGE = "成功删除采媒车中该媒体信息";
	private static final String DELETE_SUCCESS_MESSAGE_FAILED = "删除失败，该媒体已经不在采媒车中";
	private static final String ALREADY_EXIST_MESSAGE = "已存在于采媒车中，不要重复添加";
	
	//private Logger log = LoggerFactory.getLogger(ReqAdvertiserController.class);
	
	
	@Autowired
	private MediaQuoteService mediaQuoteService;
	
	
	/**
	 *用于head.jsp生成采媒车页面信息 
	 * @param session
	 * @return
	 */
	@RequestMapping(value="queryMedias")
	public String queryMedias(HttpSession session, Model model){
		List<MediaQuoteVo> list = (List<MediaQuoteVo>) session.getAttribute(MEDIAS_ARRAY);
		model.addAttribute("datas", list);
		return "shoppingCarModal";
	}
	/**
	 * 从采媒车中删除媒体信息
	 * @param session
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="delMediaFromCart")
	public String delMediaFromCart(HttpSession session, String id){
		List<MediaQuoteVo> list = (List<MediaQuoteVo>) session.getAttribute(MEDIAS_ARRAY);//从session中取出采媒车中媒体list
		boolean hasDelete = false;
		for(int i=0;i<list.size();i++){
			if(id.trim().equals(list.get(i).getId().trim())){//根据id找出要删除的媒体对象
				list.remove(list.get(i));
				hasDelete = true;
				break;
			}
		}
		if (hasDelete) {
			return DELETE_SUCCESS_MESSAGE;
		} else {
			return DELETE_SUCCESS_MESSAGE_FAILED;
		}
	}
	
	
	/**
	 * 详细页中添加媒体至采媒车
	 * @param session
	 * @param id
	 * @param name
	 * @param imgSrc
	 * @param category
	 * @param industryTypes
	 * @param fans
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value="addMediasFromDetail")
	public Map<String,Object> addInviteMediasFromDetail(HttpSession session, HttpServletRequest request){
		String mediaQuoteId = (String) request.getParameter("mediaQuote");
		Map<String,Object> map = new HashMap<String,Object>();
		String msg = null;
		MediaQuote mediaQuote = mediaQuoteService.get(mediaQuoteId);
		List<MediaQuoteVo> medias = null;
		MediaQuoteVo mediaQuoteVo = new MediaQuoteVo(mediaQuote);
		if(session.getAttribute(MEDIAS_ARRAY) == null){
			medias = new ArrayList<MediaQuoteVo>();
			medias.add(mediaQuoteVo);
			msg = ADD_SUCCESS_MESSAGE;
		} else {
			medias = (List<MediaQuoteVo>) session.getAttribute(MEDIAS_ARRAY);
			boolean contains = false;
			for (MediaQuoteVo vo : medias) {
				if (vo.getId().equals(mediaQuoteVo.getId())){
					contains = true;
					break;
				}
			}
			if (contains) {
				msg = ALREADY_EXIST_MESSAGE;
			} else {
				medias.add(mediaQuoteVo);
				msg = ADD_SUCCESS_MESSAGE;
			}
			
		}
		session.setAttribute(MEDIAS_ARRAY, medias);
		map.put("mediaQuotes", medias);
		map.put("msg",msg);
		return map;
	}
	
	@ResponseBody
	@RequestMapping(value="getNum")
	public String getNum(HttpSession session) {
		List<MediaQuoteVo> medias = (List<MediaQuoteVo>) session.getAttribute(MEDIAS_ARRAY);
		return (medias != null && medias.size() > 0) ? String.valueOf(medias.size()) : "";
	}
	
}
