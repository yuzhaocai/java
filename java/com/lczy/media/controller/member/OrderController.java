/**
 * 
 */
package com.lczy.media.controller.member;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lczy.common.util.MessageBean;
import com.lczy.common.web.Token;
import com.lczy.media.controller.common.BaseController;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.Order;
import com.lczy.media.entity.OrderMessage;
import com.lczy.media.entity.User;
import com.lczy.media.service.OrderMessageService;
import com.lczy.media.service.OrderService;
import com.lczy.media.service.UserService;
import com.lczy.media.util.Constants;
import com.lczy.media.util.UserContext;

/**
 * @author wu
 *
 */
@Controller
@RequestMapping("/member/order")
public class OrderController extends BaseController{
	
	private Logger log = LoggerFactory.getLogger(OrderController.class);	
	
	@Autowired
	private OrderMessageService orderMessageService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	/**
	 * 订单留言板.
	 */
	@RequestMapping(value="orderMessage")
	@Token
	public String orderMessage(String id, Model model,HttpServletRequest request,RedirectAttributes redirectAttrs) {
		String userId = UserContext.getCurrent().getId();
		
		Customer customer = customer4UserId(userId);
		String custType = customer.getCustType();
		//检查权限
		MessageBean bean = orderMessagePermission(id,customer);
		
		if( bean == null ) {
			Order order = orderService.get(id);
			
			if(custType.equals(Constants.CustType.CUST_ADV)){
				order.setHasMessage4P(false);
			}else if(custType.equals(Constants.CustType.CUST_PRO)){
				order.setHasMessage4A(false);
			}
			orderService.save(order);
			List<OrderMessage> oms = orderMessageService.findByOrderId(id);
			model.addAttribute("data", oms);
			model.addAttribute("orderId", id);
			return "member/order/orderMessage";
		}else{
			redirectAttrs.addFlashAttribute("message", bean.toJSON());
			return "redirect:/member/userInfo";
		}
		
	}
	
	/**
	 * 留言.
	 */
	@RequestMapping(value="saveOrderMessage", method = RequestMethod.POST)
	@ResponseBody
	public MessageBean saveOrderMessage(@RequestParam(required=true) String id,
			@RequestParam(required=true) String content,
			RedirectAttributes redirectAttrs) {
		String userId = UserContext.getCurrent().getId();
		MessageBean bean = orderMessagePermission(id,customer4UserId(userId));
		bean = (bean == null) ? userShutup(userId) : bean;
		if(bean==null){
			OrderMessage oma = new OrderMessage();
			//发言人
			User user = userService.get(userId);
			Order order = orderService.get(id);
			oma.setOrder(order);
			if(user.getCustomer().getCustType().equals(Constants.CustType.CUST_PRO)){
				//媒体主
				oma.setSpeaker(oma.getOrder().getMedia().getName());
				//媒体主有新留言并且广告主未查看
				if(order.isHasMessage4P()!=true){
					order.setHasMessage4P(true);
					orderService.save(order);
				}
			}else if(user.getCustomer().getCustType().equals(Constants.CustType.CUST_ADV)){
				//广告主
				oma.setSpeaker(user.getCustomer().getName());
				//广告主有新留言并且媒体主未查看
				if(order.isHasMessage4A()!=true){
					order.setHasMessage4A(true);
					orderService.save(order);
				}
			}
			oma.setCreateBy(userId);
			oma.setContent(content.trim());
			oma.setCreateTime(new Date());
			orderMessageService.save(oma);
			
			bean = new MessageBean(1, "留言成功！");
			bean.getData().put("speaker", oma.getSpeaker());
			bean.getData().put("content", oma.getContent());
			bean.getData().put("createTime", new DateTime(oma.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"));
		}
		return bean;
	}
	
	/**
	 * 检查当前用户是否有权限操作.
	 * @param id 订单ID custmoer 当前登录会员
	 * @return 有权限则返回 null，无权限则返回 MessageBean 对象.
	 */
	protected MessageBean orderMessagePermission(String id,Customer custmoer) {
		MessageBean bean = null;
		if( !hasOrderMessagePermission(id,custmoer) ) {
			bean = new MessageBean(0, "您无权在此订单留言：" + id);
		}
		return bean;
	}
	
	/**
	 * 根据用户类型判断是否有权限操作当前订单.
	 * @param id 订单ID custmoer 当前登录会员
	 * @return 是否有权限查看此订单留言.
	 */
	private boolean hasOrderMessagePermission(String id,Customer custmoer) {
		String custId = custmoer.getId();
		String custType = custmoer.getCustType();
		Order order = orderService.get(id);
		if(custType.equals(Constants.CustType.CUST_ADV)){
			//当前登陆会员为广告主  查看登陆会员ID与订单内广告主ID是否相同
			return order.getReqOwner().getId().equals(custId);
		}else if(custType.equals(Constants.CustType.CUST_PRO)){
			//当前登陆会员为媒体主  查看登陆会员ID与订单内媒体主ID是否相同
			return order.getMediaOwner().getId().equals(custId);
		}
		return false;
	}
	
	/**
	 * 根据登陆用户ID查询会员
	 * @param userId 当前登陆用户ID
	 * @return 当前会员
	 */
	protected Customer customer4UserId(String userId){
		Customer custmoer = userService.get(userId).getCustomer();
		return custmoer;
	}

	/**
	 * 查看登陆用户是否被禁言
	 * @param userId 当前登陆用户ID
	 */
	protected MessageBean userShutup(String userId){
		MessageBean bean = null;
		if(userService.get(userId).isShutup()) {
			bean = new MessageBean(0, "您已被禁言！");
		}
		return bean;
	}
}
