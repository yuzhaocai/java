/**
 * 
 */
package com.lczy.media.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.data.JPAUtil;
import com.lczy.common.service.AbstractService;
import com.lczy.common.util.BeanMapper;
import com.lczy.common.util.Encrypts;
import com.lczy.media.entity.Account;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaWeibo;
import com.lczy.media.entity.MediaWeixin;
import com.lczy.media.entity.Role;
import com.lczy.media.entity.User;
import com.lczy.media.repositories.CustLevelDao;
import com.lczy.media.repositories.CustomerDao;
import com.lczy.media.repositories.MediaDao;
import com.lczy.media.repositories.RoleDao;
import com.lczy.media.repositories.UserDao;
import com.lczy.media.util.Constants;
import com.lczy.media.util.Constants.CustProperty;
import com.lczy.media.util.Constants.CustStatus;
import com.lczy.media.util.Constants.CustType;
import com.lczy.media.util.Constants.UserStatus;
import com.lczy.media.util.Constants.UserType;
import com.lczy.media.util.CreditConfig;
import com.lczy.media.util.UserContext;
import com.lczy.media.vo.CustomerVO;

/**
 * @author wu
 *
 */
@Service
@Transactional(readOnly=false)
public class CustomerService extends AbstractService<Customer> {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private CustLevelDao custLevelDao;
	
	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private MediaDao mediaDao;
	
	@Autowired
	private MediaService mediaService;
	
	@Autowired
	private CustCreditService custCreditService;
	
	@Transactional(readOnly=false)
	public Customer save(Customer customer) {
		return getDao().save(customer);
	}
	
	public CustomerDao getDao() {
		return (CustomerDao)super.getDao();
	}

	/**
	 * 新会员注册.
	 * @param vo
        */
	@Transactional(readOnly=false)
	public Customer register(CustomerVO vo) {
		Date now = new Date();
		// 创建客户信息 和 关联账户信息
		Customer cust = BeanMapper.map(vo, Customer.class); 
		cust.setCreateTime(now);
		cust.setModifyTime(now);
		cust.setStatus(CustStatus.NORMAL);
		cust.setCredit(0);
		cust.setCustLevel(custLevelDao.findOne("VIP1"));
		cust.setCertStatus(Constants.CertStatus.NULL);
		
		//创建客户账户
		Account account = createAccount(cust);
		cust.setAccount(account);
		getDao().save(cust);
		
		// 创建用户信息
		createUser(vo, cust);
		
		// 奖励
//		updateCredit(cust);
		
		return cust;
	}

	/**
	 * 新会员注册奖励信用和虚拟币.
	 */
	private void updateCredit(Customer cust) {
		String memo = "注册成功！信用值%+d, 虚拟币%+d。";
		int credit = CreditConfig.ACT_REGISTER[0];
		int vc     = CreditConfig.ACT_REGISTER[1];
		custCreditService.change(cust, credit, vc, String.format(memo, credit, vc));
	}

	private void createUser(CustomerVO vo, Customer cust) {
		User user = new User();
		Date now = new Date();
		
		user.setLoginName(vo.getLoginName());
		
		String[] hash = Encrypts.hashPassword(vo.getPassword());
		user.setPassword(hash[0]);
		user.setSalt(hash[1]);
		
		user.setNickname(getNickname(vo));
		user.setSecMobile(vo.getMobPhone());
		
		user.setCustomer(cust);
		user.setType(UserType.MEMBER);
		user.setStatus(UserStatus.ENABLED);
		user.setCreateTime(now);
		
		user.setRole(getUserRole(cust));// 创建用户角色关系
		
		userDao.save(user);
	}

	
	
	private String getNickname(CustomerVO vo) {
		if( StringUtils.isNotBlank(vo.getName()) )
			return vo.getName();
		else
			return vo.getMobPhone();
	}

	private Account createAccount(Customer cust) {
		Date now = new Date();
		Account account = new Account();
		account.setCustomer(cust);//建立与 customer 的关联关系
		account.setAvBalance(0);
		account.setCreateTime(now);
		account.setModifyTime(now);
		return account;
		//accountDao.save(account);
	}

	private Role getUserRole(Customer cust) {
		String custType = cust.getCustType();
		String roleId = null;
		if (CustType.CUST_ADV.equals(custType)) {
			roleId = Constants.RoleId.ADVERTISER;
		} else if (CustType.CUST_PRO.equals(custType)) {
			roleId = Constants.RoleId.PROVIDER;
		} else {
			roleId = Constants.RoleId.ORGANIZATION;
		}
		return roleDao.findOne(roleId);
	}
	

	public Page<Customer> find(Map<String, Object> searchParams, int page,
			int size, String sort) {
		Pageable pageable = JPAUtil.buildPageRequest(page, size, sort);
        Specification<Customer> spec = JPAUtil.buildSpecification(searchParams);
        
		return getDao().findAll(spec, pageable);
	}

	public Customer findByName(String name) {
		
		return getDao().findByName(name);
	}

	/**
	 * 创建组织机构.
	 * 
	 * @param vo
	 */
	@Transactional(readOnly=false)
	public Customer createOrganization(CustomerVO vo) {
		Date now = new Date();
		// 创建客户信息
		Customer org = BeanMapper.map(vo, Customer.class); 
		org.setCreateBy(UserContext.getSystemUser().getId());
		org.setCreateTime(now);
		org.setModifyBy(org.getCreateBy());
		org.setModifyTime(now);
		
		org.setCredit(0);
		org.setStatus(CustStatus.NORMAL);
		org.setCustType(CustType.CUST_ORG);
		org.setCustProperty(CustProperty.CUST_C);
		
		getDao().save(org);
		
		//创建登录账号
		createUser(vo, org);
		
		return org;
	}
	
		/**
	 * 修改组织机构
	 * 
	 * @param customer
	 */
	@Transactional(readOnly=false)
	public void updateOrganization(CustomerVO vo) {
		Customer customer = customerDao.findOne(vo.getId());
		customer.setMobPhone(vo.getLoginName());
		customer.setName(vo.getName());
		customer.setOrgSummary(vo.getOrgSummary());
		customer.setOrgType(vo.getOrgType());
		Date now = new Date();
		customer.setModifyBy(UserContext.getCurrent().getId());
		customer.setModifyTime(now);
		save(customer);
		
		User user = userDao.findByCustomerId(customer.getId());
		user.setLoginName(vo.getLoginName());
		user.setNickname(customer.getName());
		String[] hash = Encrypts.hashPassword(vo.getLoginName());
		user.setPassword(hash[0]);
		user.setSalt(hash[1]);
		userDao.save(user);
	}
	
	/**
	 * 删除组织机构
	 * 
	 * @param id
	 * @throws Exception 
	 */
	@Transactional(readOnly=false)
	public void deleteOrganization(String id) throws Exception {
		Customer customer = get(id);
		if (!CollectionUtils.isEmpty(mediaDao.findByOrganizationId(id))) {
			throw new Exception("此机构下属有媒体,暂时不能删除!");
		}
		
		userDao.delete(customer.getUsers());
		this.remove(customer);
	}
	
	
	
	/**
	 * 删除客户.
	 * @param id
	 * @return 被删除的客户
	 */
	@Transactional(readOnly=false)
	public Customer delete(String id) {
		Customer org = get(id);
		userDao.deleteByCustomerId(id);
		getDao().delete(org);
		
		return org;
	}

	
	public List<Customer> findAllOrgs() {
		List<Customer> orgs = getDao().findByCustTypeOrderByName(Constants.CustType.CUST_ORG);
		return orgs;
	}

	public int countBy(String field, Object value) {
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("EQ_" + field, value);
		searchParams.put("EQ_custType", Constants.CustType.CUST_ADV);
		Specification<Customer> spec = JPAUtil.buildSpecification(searchParams);
		return (int)getDao().count(spec);
	}

	/**
	 * 更新用户信息.
	 */
	@Transactional(readOnly=false)
	public void updateUserInfo(CustomerVO vo, Customer cust) {
		if( ! cust.getMobPhone().equals(vo.getMobPhone()) ) {
			updateLoginName(vo, cust);
			cust.setMobPhone(vo.getMobPhone());
		}
		if(null!=vo.getName()){
			cust.setName(vo.getName());
		}
		cust.setLinkman(vo.getLinkman());
		cust.setEmail(vo.getEmail());
		cust.setQq(vo.getQq());
		
		getDao().save(cust);
	}

	/**
	 * 更新登录名.
	 */
	private void updateLoginName(CustomerVO vo, Customer cust) {
		User user = userDao.findByLoginName(cust.getMobPhone());
		if( user != null) {
			userDao.updateLoginName(vo.getMobPhone(), cust.getMobPhone());
		}
	}
	
	/**
	 * 实名认证通过.
	 */
	public void customerPass(String customerId){
		customerDao.customerPass(customerId, Constants.CertStatus.PASS);
	}
	
	/**
	 * 实名认证不通过.
	 */
	public void customerUnPass(String customerId){
		customerDao.customerUnPass(customerId, Constants.CertStatus.UNPASS);
	}
	
	/**
	 * 查询所有的监管机构
	 * 
	 */
	public List<Customer> finaAllOrg(String custOrg) {
		return customerDao.findByCustTypeOrderByName(custOrg);
	}
	
	/**
	 * 导入媒体
	 * 
	 * @param media
	 * @param vo
	 * @param org
	 * @throws Exception
	 */
	public void importMedia(Media media, CustomerVO vo, CustomerVO org) throws Exception {
		// 按名称查询媒体是否存在,如果不存在,则创建
		if (mediaDao.findTopByNameAndMediaType(vo.getName(), media.getMediaType()) != null) {
			throw new Exception(vo.getName() + "已经存在！");
		}
		
		// 按名称查询媒体主是否存在,如果不存在,则创建
		Customer mediaOwner = customerDao.findByName(vo.getName());
		if (mediaOwner == null) {
			vo.setCustType(Constants.CustType.CUST_PRO);
			mediaOwner = this.register(vo);
		}
		media.setCustomer(mediaOwner);
		
		// 按名称查询监管机构是否存在,如果不存在,则创建监管机构
		Customer orgCustomer = customerDao.findByName(org.getName());
		if (org.getName() != null && orgCustomer == null) {
			orgCustomer = this.createOrganization(org);
		}
		
		// 设置媒体属性
		Date now = new Date();
		media.setCreateTime(now);
		media.setCreateBy(UserContext.getSystemUser().getId());
		media.setModifyTime(now);
		media.setModifyBy(UserContext.getSystemUser().getId());
		media.setLevel(Constants.MediaLevel.UNLEVELED);
		media.setStatus(Constants.MediaStatus.NORMAL);
		media.setOrganization(orgCustomer);
		media.setFans(0);
		
		if (media instanceof MediaWeibo) {
			mediaService.save((MediaWeibo)media);
		} else {
			mediaService.save((MediaWeixin)media);
		}
	}

	/**
	 * 导入媒体
	 * 
	 * @param media
	 * @param vo
	 * @param mediaCases
	 * @param mediaQuotes
	 * @throws Exception
	 */
	public void importMedia(Media media, CustomerVO vo) throws Exception {
		// 按名称查询媒体是否存在,如果不存在,则创建
		if (media.getMediaType().equals(Constants.MediaType.WEIBO)) {
			if (mediaDao.findTopByNameAndMediaType(media.getName(), media.getMediaType()) != null) {
				throw new Exception("微博【" + media.getName() + "】已经存在！");
			}
		} else if (StringUtils.isNotBlank(media.getAccount()) 
				&& mediaDao.findTopByAccountAndMediaType(media.getAccount(), media.getMediaType()) != null) {
			throw new Exception("微信【" + media.getAccount() + "】已经存在！");
		}
		
		// 按名称查询媒体主是否存在,如果不存在,则创建
		Customer mediaOwner = customerDao.findByName(vo.getName());
		if (mediaOwner == null) {
			vo.setCustType(Constants.CustType.CUST_PRO);
			mediaOwner = this.register(vo);
		}
		media.setCustomer(mediaOwner);
		
		// 设置媒体属性
		Date now = new Date();
		media.setCreateTime(now);
		media.setCreateBy(UserContext.getSystemUser().getId());
		media.setModifyTime(now);
		media.setModifyBy(UserContext.getSystemUser().getId());
		media.setLevel(Constants.MediaLevel.UNLEVELED);
		media.setStatus(Constants.MediaStatus.NORMAL);
		
		if (media instanceof MediaWeibo) {
			mediaService.save((MediaWeibo)media);
		} else {
			mediaService.save((MediaWeixin)media);
		}
	}

	/**
	 * 导入广告主
	 * 
	 * @param vo
	 * @throws Exception
	 */
	public void importAdivertiser(CustomerVO vo) throws Exception {
		if (this.countBy("mobPhone", vo.getMobPhone()) > 0) {
			throw new Exception("手机号码已经存在:" + vo.getMobPhone());
		}
		vo.setCustType(Constants.CustType.CUST_ADV);
		this.register(vo);		
	}
	
	
	/**
	 * 检查客户名称是否重复
	 * @param name 客户名称
	 * @return
	 */
	public int countBy(String name) {
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("EQ_name", name);
		searchParams.put("EQ_custType", Constants.CustType.CUST_ADV);
		Specification<Customer> spec = JPAUtil.buildSpecification(searchParams);
		return (int)getDao().count(spec);
	}
}
