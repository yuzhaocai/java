package com.lczy.media.util;



public interface Constants {

	
	/**
	 * 用户状态.
	 * @author Wu.Yanhong
	 *
	 */
	public  interface UserStatus {
		String DIC_CODE = "USER_STAT";
		
		/**
		 * 启用
		 */
		String ENABLED = "USER_ENABLED";
		
		/**
		 * 停用
		 */
		String DISABLED = "USER_DISABLED";
		
		/**
		 * 过期
		 */
		String EXPIRED = "USER_EXPIRED";
		
		/**
		 * 离职
		 */
		String DIMISSION = "USER_DIMISSION";
	}

	/**
	 * 日志动作类型.
	 * 
	 * @author Wu.Yanhong
	 *
	 */
	public  interface ActionType {
		String LOGIN = "ACT_LOGIN";//用户登录
	}

	/**
	 * 客户类型操作的常量
	 *
	 */
	public interface CustType{
		
		String DIC_CODE = "CUST_TYPE";//客户类型

		/**
		 * 广告主
		 */
		String CUST_ADV= "CUST_T_ADVERTISER";
		
		/**
		 * 媒体供应商
		 */
		String CUST_PRO = "CUST_T_PROVIDER";

		/**
		 * 组织机构
		 */
		String CUST_ORG = "CUST_T_ORG";
		
		/**
		 * 系统
		 */
		String CUST_SYS = "CUST_T_SYS";

	}
	
	/**
	 * 客户类型操作的常量
	 *
	 */
	public interface CustProperty{
		
		String DIC_CODE = "CUST_PROPERTY";//客户属性
		
		/**
		 * 个人
		 */
		String CUST_P= "CUST_P_PERSONAL";
		
		/**
		 * 企业
		 */
		String CUST_C = "CUST_P_COMPANY";
		
	}

	/**
	 * 媒体类型.
	 */
	interface MediaType {
		
		String DIC_CODE = "MEDIA_TYPE";
		String WEIXIN = "MEDIA_T_WEIXIN";
		String WEIBO = "MEDIA_T_WEIBO";
		
	}
	/**
	 * 微信服务类型
	 *
	 */
	interface WeixinService{
		String DIC_CODE ="WEIXIN_SERVICE";
		String WEIXIN_S_SINGLE_P ="WEIXIN_S_SINGLE_P";
		String WEIXIN_S_MULTI_P_F = "WEIXIN_S_MULTI_P_F";
		String WEIXIN_S_MULTI_P_S = "WEIXIN_S_MULTI_P_S";
		String WEIXIN_S_MULTI_P_T = "WEIXIN_S_MULTI_P_T";
		String WEIXIN_S_MULTI_P_O = "WEIXIN_S_MULTI_P_O";
		String WEIXIN_S_SLICE = "WEIXIN_S_SLICE";
		String WEIXIN_S_FRIEND = "WEIXIN_S_FRIEND";
	}
	/**
	 * 微博服务类型
	 *
	 */
	interface WeiboService{
		String DIC_CODE = "WEIBO_SERVICE";
		String WEIBO_S_01 = "WEIBO_S_01";
		String WEIBO_S_02 = "WEIBO_S_02";
		String WEIBO_S_03 = "WEIBO_S_03";
		String WEIBO_S_04 = "WEIBO_S_04";
		String WEIBO_S_05 = "WEIBO_S_05";
		String WEIBO_S_06 = "WEIBO_S_06";
		String WEIBO_S_07 = "WEIBO_S_07";
	}
	/**
	 * 媒体级别.
	 */
	interface MediaLevel {
		String DIC_CODE = "MEDIA_LEVEL";
		
		// 未分级媒体
		String UNLEVELED = "MEDIA_L_UNLEVELED";
		
		// 一类媒体
		String LEVEL1 = "MEDIA_L_1";
		
		// 二类媒体+
		String LEVEL2_P = "MEDIA_L_2+";
		
		// 二类媒体-
		String LEVEL2_S = "MEDIA_L_2-";
		
		// 三类媒体+
		String LEVEL3_P = "MEDIA_L_3+";
		
		// 三类媒体-
		String LEVEL3_S = "MEDIA_L_3-";
		
		// 四类媒体
		String LEVEL4 = "MEDIA_L_4";
	}
	
	interface UserType {
		/**
		 * 系统用户.
		 */
		String SYSTEM = "USER_T_SYSTEM";
		
		/**
		 * 会员.
		 */
		String MEMBER = "USER_T_MEMBER";
	}

	
	/**
	 * 客户状态.
	 * @author wu
	 *
	 */
	interface CustStatus {
		String DIC_CODE = "CUST_STATUS";
		
		/**
		 * 审核中. 
		 */
		String AUDIT = "CUST_S_AUDIT";
		
		String NORMAL = "CUST_S_NORMAL";
		
		String DISABLED  = "CUST_S_DISABLED";
	}
	
	interface RoleId {
		String ADVERTISER = "2001";
		String PROVIDER = "2002";
		String ORGANIZATION = "2003";
	}
	
	interface RoleCode {
		String ADVERTISER = "advertiser";
		String PROVIDER = "provider";
		String ORGANIZATION = "organization";
	}

	interface WeixinCategory {
		String DIC_CODE = "WEIXIN_CATEGORY";
	}
	interface WeiboCategory {
		String DIC_CODE = "WEIBO_CATEGORY";
	}
	
	/**
	 * 更多媒体类别
	 * 
	 * @author wang.haibin
	 *
	 */
	interface OtherMediaCategory {
		String DIC_CODE = "OTHER_MEDIA_CATEGORY";
	}
	
	/*
	 * 
	 */
	interface IndustryType{
		String DIC_CODE = "INDUSTRY_TYPE";
		
	}
	interface MediaArea{
		String DIC_CODE = "MEDIA_AREA";
		
	}
	interface WeixinFans{
		String DIC_CODE = "WEIXIN_FANS";
	}
	interface WeiboFans{
		String DIC_CODE = "WEIBO_FANS";
	}

	interface WeixinFitProduct{
		String DIC_CODE = "WEIXIN_FITPRODUCT";
		
	}
	
	
	/**
	 *　需求状态.
	 */
	interface ReqStatus {
		String DIC_CODE = "REQ_STATUS";
		String AUDIT = "REQ_S_AUDIT";
		String NORMAL = "REQ_S_NORMAL";
		String DISABLED = "REQ_S_DISABLED";
		String PENDING = "REQ_S_PENDING";
	}
	
	/**
	 *　媒体状态.
	 */
	interface MediaStatus {
		String DIC_CODE = "MEDIA_STATUS";
		String AUDIT = "MEDIA_S_AUDIT";
		String NORMAL = "MEDIA_S_NORMAL";
		String DISABLED = "MEDIA_S_DISABLED";
	}
	
	String CURRENT_REQ_KEY = "CURRENT_REQ_ID";
	
	String BADGE_NUM_KEY = "badgeNum";
	
	/**
	 *  预约媒体反馈状态.
	 *
	 */
	interface AcceptResult {
		String DIC_CODE = "ACCEPT_RESULT";
		
		String YES = "ACCEPT_S_YES";
		String NO = "ACCEPT_S_NO";
	}
	
	
	/**
	 *  广告主对报价的反馈状态.
	 *
	 */
	interface AgreedResult {
		String DIC_CODE = "AGREED_RESULT";
		
		String YES = "AGREED_S_YES";
		String NO = "AGREED_S_NO";
	}
	
	interface Result {
		String SUCCESS = "success";
		String REASON = "reason";
		String DATA = "data";
	}
	
	interface OrderStatus {
		String DIC_CODE = "ORDER_STATUS";
		
		String PROGRESS = "ORDER_S_PROGRESS";
		String DELIVERED = "ORDER_S_DELIVERED";
		String ACCEPTANCE = "ORDER_S_ACCEPTANCE";
		String REFUSEPAY = "ORDER_S_REFUSEPAY";
		String REPORT = "ORDER_S_REPORT";
		String FINISHED = "ORDER_S_FINISHED";
	}
	
	interface IntentionStatus {
		String DIC_CODE = "INTENTION_STATUS";
		/** 待处理 */
		String PENDING = "INTENTION_S_PENDING";
		/** 已处理 */
		String PROCESSED = "INTENTION_S_PROCESS";
		/** 已完成 */
		String FINISHED = "INTENTION_S_FINISHED";
	}
	
	interface TransactionType {
		
		String DIC_CODE = "TRANS_TYPE";
		
		/**
		 * 消费
		 */
		String CONSUME = "TRANS_T_CONSUME";

		/**
		 * 收入
		 */
		String INCOME = "TRANS_T_INCOME";

		/**
		 * 购买线下服务
		 */
		String OFFLINE = "TRANS_T_OFFLINE";

		/**
		 * 垫资
		 */
		String REPAYMENT = "TRANS_T_REPAYMENT";
	}
	
	interface ArticleStatus {
		
		String DIC_CODE = "ARTICLE_STATUS";
		
		String DRAFT = "ARTICLE_S_DRAFT";
		
		String AUDIT = "ARTICLE_S_AUDIT";
		
		String PUBLISH = "ARTICLE_S_PUBLISH";
	}
	
	interface ArticleVisable {
		
		String DIC_CODE = "ARTICLE_VISABLE";
		
		String SHOW = "ARTICLE_V_SHOW";
		
		String HIDE = "ARTICLE_V_HIDE";
		
	}

	/*
	 * 媒体反馈常量
	 */
	interface MediaFeedback {
		String NULL = "MEDIA_FB_NULL";
		String REFUSE = "MEDIA_FB_REFUSE";
		String ACCEPT = "MEDIA_FB_ACCEPT";
	}

	/*
	 * 广告主确认状态
	 */
	interface AdverConfirm {
		String NULL = "ADVER_CF_NULL";
		String REFUSE = "ADVER_CF_REFUSE";
		String ACCEPT = "ADVER_CF_ACCEPT";
	}
	
	
	/**
	 * 冻结单状态.
	 */
	interface FreezedStatus {
		String DIC_CODE = "FREEZED_STATUS";
		String FREEZED = "FREEZED_S_FREEZED";
		String PAID = "FREEZED_S_PAID";
		String REFUND = "FREEZED_S_REFUND";
	}
	
	/**
	 * 投诉类型.
	 */
	interface ComplaintType {
		String DIC_CODE = "COMP_TYPE";
		String REJECT = "COMP_T_REJECT";
		String COMPLAINT = "COMP_T_COMPLAINT";
	}
	
	/**
	 * 处理结果.
	 */
	interface HandleResult {
		String DIC_CODE = "HANDLE_RESULT";
		String CREATED = "RESULT_T_CREATED";
		String REFUND = "RESULT_T_REFUND";
		String PAY = "RESULT_T_PAY";
		String DEAL = "RESULT_T_DEAL";
		String UNDEAL = "RESULT_T_UNDEAL";
	}
	
	/**
	 * 发票提供方.
	 */
	interface InvoiceProvider {
		String DIC_CODE = "INVOICE_PROVIDER";
		String SYSTEM = "INVOICE_P_SYSTEM";
		String MEDIA = "INVOICE_P_MEDIA";
	}
	
	/**
	 * 发票类型.
	 */
	interface InvoiceType {
		String DIC_CODE = "INVOICE_TYPE";
		String VAT = "INVOICE_T_VAT";
		String NORM = "INVOICE_T_NORM";
	}
	
	/**
	 * 发票状态.
	 */
	interface InvoiceStatus {
		String DIC_CODE = "INVOICE_STATUS";
		String CREATED = "INVOICE_S_CREATED";
		String FINISHED = "INVOICE_S_FINISHED";
		String TAXED = "INVOICE_S_TAXED";
	}
	
	/**
	 * 支付平台.
	 *
	 */
	interface PayPlatform {
		String DIC_CODE = "PAY_PLATFROM";
		String ALIPAY = "PAY_P_ALIPAY";
		String WXPAY = "PAY_P_WXPAY";
		String NETBANK = "PAY_P_NETBANK";
	}
	
	/**
	 * 媒体邀请方式.
	 *
	 */
	interface InviteType {
		String DIC_CODE = "INVITE_TYPE";
		
		/**
		 * 广告主邀请
		 */
		String PASSIVE = "INVITE_T_PASSIVE";
		
		/**
		 * 主动应征
		 */
		String ACTIVE = "INVITE_T_ACTIVE";
	}
	
	/**
	 * 机构类型.
	 *
	 */
	interface OrganizationType {
		String DIC_CODE = "ORGANIZATION_TYPE";
		
	}
	
	
	
	/**
	 * 实名认证状态.
	 * 
	 */
	interface CertStatus {
		String DIC_CODE = "CERT_STATUS";
		String NULL = "CERT_S_NULL";
		String AUDIT = "CERT_S_AUDIT";
		String UNPASS = "CERT_S_UNPASS";
		String PASS = "CERT_S_PASS";
	}
	
	interface ReqMediaStatus {
		//预约单状态：REQ_MEDIA_S_NORMAL（正常），REQ_MEDIA_S_CANCELED（广告主撤消），REQ_MEDIA_S_DELETED（已删除）
		String NORMAL = "REQ_MEDIA_S_NORMAL";
		String CANCELED = "REQ_MEDIA_S_CANCELED";
		String DELETED = "REQ_MEDIA_S_DELETED";
	}
	
	/**
	 * 角色类型.
	 *
	 */
	interface RoleType {
		String SYSTEM = "SYSTEM";
		String MEMBER = "MEMBER";
	}
	
	/**
	 * 充值日志状态
	 */
	interface ChargeLogStatus {
		String CREATED = "CL_S_CREATED";
		String FINISHED = "CL_S_FINISHED";
	}
	
	interface InviteNum {
		String DIC_CODE = "INVITE_NUM";
	}

	/**
	 * 提现申请状态.
	 * @author wanghaibin
	 *
	 */
	interface WithdrawStatus {
		String DIC_CODE = "WITHDRAW_STATUS";
		
		/**
		 * 已申请. 
		 */
		String CREATED = "WITHDRAW_S_CREATED";
		
		/**
		 * 成功
		 */
		String FINISHED = "WITHDRAW_S_FINISHED";
		
		/**
		 * 失败
		 */
		String FAILED = "WITHDRAW_S_FAILED";
	}
	
	
	
	/**
	 * 黑名称类型.
	 * @author wu
	 *
	 */
	interface BlacklistType {
		String DIC_CODE = "BLACKLIST_TYPE";
		
		String PHONE = "BL_T_PHONE";
	}
	
	/**
	 * 抽奖奖品类型
	 * 
	 * @author wanghaibin
	 *
	 */
	interface LotteryPrizeCategory {
		String DIC_CODE = "LOTTERY_PRIZE_CATEGORY";
		
		/**
		 * 一元广告
		 */
		String ADVERTISE = "LOTTERY_C_ADVERTISE";
		
		/**
		 * 参与奖
		 */
		String PARTICIPATE = "LOTTERY_C_PARTICIPATE";
		
	}
	
	/** 拒付理由
	 * @author wang.xiaoxiang
	 *
	 */
	interface RejectReason {
		String DIC_CODE = "MEDIA_REJECT_REASON";
	}
	
	/** 意见类别
	 * @author jia.qi
	 *
	 */
	interface Opinion {
		String DIC_CODE = "OPINION_TYPE";
		
		/**
		 * 业务咨询
		 */
		String BUSINESS ="OPINION_BUSINESS";
		
		/**
		 * 网站吐槽
		 */
		String WEB ="OPINION_WEB";
		
		/**
		 * 技术问题
		 */
		String TECHNOLOGY ="OPINION_TECHNOLOGY";
	}
	
	
	/** 报价的变更类型
	 * @author wang.xiaoxiang
	 *
	 */
	interface QuoteModifyType {
		
		String DIC_CODE = "MODIFY_TYPE";
		
		/**
		 * 管理员变更结算价
		 */
		String ADMIN_MEDIAQUOTE = "ADMIN_MEDIAQUOTE";
		
		/**
		 * 管理员变更报价
		 */
		String ADMIN_QUOTE = "ADMIN_QUOTE";
		
		/**
		 *   媒体修改结算价
		 */
		String MEDIA = "MEDIA";
		
		/**
		 *   星级修改
		 */
		String STAR = "STAR";
		
		/**
		 *   媒体发票修改
		 */
		String INVOICE = "INVOICE";
		
		/**
		 *   管理员发票修改
		 */
		String ADMIN_INVOICE = "ADMIN_INVOICE";
		
	}
	
	
	/**
	 *  媒体报价记录状态
	 * @author wang.xiaoxiang
	 *
	 */
	interface QuoteLogStatus {
		
		String DIC_CODE = "QUOTELOG_STATUS";
		
		/**
		 * 通过
		 */
		String PASS = "PASS";
		
		/**
		 *  拒绝
		 */
		String REJECT = "REJECT";
		
		/**
		 * 待审核
		 */
		String AUDIT = "AUDIT";
		
	}
	
	/** 活动状态
	 * @author wang.xiaoxiang
	 *
	 */
	interface ActivityStatus {
		String DIC_CODE = "ACTIVITY_STATUS";
		
		String ACTIVE = "ACTIVE";
		
		String INACTIVE = "INACTIVE";
	}

	interface FavoritesMediaType {
		String DIC_CODE = "FAVORITES_MEDIA_TYPE";
		
		String WEIBO = "FAVORITES_WEIBO";
		
		String WEIXIN = "FAVORITES_WEIXIN";
		
		String OTHERMEDIA = "FAVORITES_OTHERMEDIA";
	}
	
	/** 
	 * 支付方式
	 * 
	 * @author wang.haibin
	 *
	 */
	interface PaymentType {
		String DIC_CODE = "PAYMENT_TYPE";
		
		// 线上支付
		String ONLINE = "PAYMENT_T_ONLINE";
		// 线下垫资支付
		String OFFLINE = "PAYMENT_T_OFFLINE";
	}
	
	
	/** 
	 * 线下垫资支付审核状态
	 * 
	 * @author wang.haibin
	 *
	 */
	interface LoanStatus {
		String DIC_CODE = "LOAN_STATUS";
		
		/**
		 * 通过
		 */
		String PASS = "PASS";
		
		/**
		 *  拒绝
		 */
		String REJECT = "REJECT";
		
		/**
		 * 待审核
		 */
		String AUDIT = "AUDIT";
	}
	
}


