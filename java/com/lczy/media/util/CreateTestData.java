package com.lczy.media.util;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.joda.time.DateTime;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lczy.common.util.PropertyUtils;
import com.lczy.common.util.Randoms;
import com.lczy.media.entity.Area;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaWeibo;
import com.lczy.media.entity.MediaWeixin;
import com.lczy.media.entity.Requirement;
import com.lczy.media.service.CustomerService;
import com.lczy.media.service.MediaService;
import com.lczy.media.service.RequirementService;
import com.lczy.media.service.common.AreaProvider;
import com.lczy.media.solr.RequirementDoc;
import com.lczy.media.vo.CustomerVO;

/**
 * 创建测试数据.
 * 
 * @author wu
 *
 */
public class CreateTestData {
	
	private CustomerService customerService;
	
	private MediaService mediaService;
	
	private AreaProvider areaProvider;
	
	private RequirementService requirementService;
	
	private long timestamp;
	
	private AtomicInteger counter = new AtomicInteger();
	
	public CreateTestData() {
		timestamp = System.currentTimeMillis();
	}
	
	public void printCount() {
		System.out.printf("counter: %d, avg: %f/s\n", counter.get(), counter.get() * 1.0 / ((System.currentTimeMillis() - timestamp / 1000)));
	}

	public static void main(String[] args) throws InterruptedException {
		if(args.length < 2) {
			System.err.println("参数错误");
			System.err.println("Usage:");
			System.err.println("\t mvn exec:java -Dexec.mainClass=\"com.lczy.media.util.CreateTestData\" -Dexec.args=\"threadNum dataNum [proPrefix] [advPrefix]\"");
			return;
		}
		
		final int threadNum = Integer.parseInt(args[0]);
		final int dataNum = Integer.parseInt(args[1]);
		final int avg = dataNum / threadNum;
		
		String proPrefix = "1301";
		String advPrefix = "1302";
		if(args.length >= 4) {
			proPrefix = args[2];
			advPrefix = args[3];
		}
		
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-common.xml");
		final CreateTestData creator = new CreateTestData();
		creator.customerService = ctx.getBean(CustomerService.class);
		creator.mediaService = ctx.getBean(MediaService.class);
		creator.areaProvider = ctx.getBean(AreaProvider.class);
		creator.requirementService = ctx.getBean(RequirementService.class);
		
		creator.advPrefix = advPrefix;
		creator.proPrefix = proPrefix;
		
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
			public void run() {
				creator.printCount();
			}
		}, 1, 1, TimeUnit.SECONDS);
		
		final CountDownLatch latch = new CountDownLatch(threadNum);
		
		for(int i = 0; i < threadNum; i++) {
			final int idx = i;
			new Thread(new Runnable() {
				public void run() {
					int len = avg;
					if (idx == threadNum - 1) {
						len += dataNum % threadNum;
					}
					
					creator.createProvider(idx * avg, len);
					creator.createAdvertiser(idx * avg, len);
					creator.createRequirement(idx * avg, len);

					latch.countDown();
				}
			}).start();
		}
		
		latch.await();
		
		ctx.close();
	}
	
	private static String summary = "采媒在线是中国大型广告在线交易平台，提供广告交易、新闻素材（版权）交易、舆情监控三大服务，"
                   + "涵盖平面媒体、网络媒体、电视媒体、户外媒体以及传统媒体的新媒体。";
	
	/**
	 * 创建需求.
	 */
	private void createRequirement(int offset, int len) {
		try {
			List<Requirement> dataList = Lists.newArrayList();
			Map<String, Object> searchParams = Maps.newLinkedHashMap();
			searchParams.put("LIKE_name", "测试广告主-");
			List<Customer> customers = customerService.find(searchParams);
			
			for( int i = offset; i < len; i++ ) {
				String type = getRandomMediaType();
				int idx = i % customers.size();
				Customer customer = customers.get(idx);
				Requirement r = new Requirement();
				r.setCustomer(customer);
				r.setName(customer.getName() + "-需求");
				r.setSummary(summary);
				r.setBudget(2000);
				r.setInviteNum("INVITE_0-10");
				r.setHasArticle(true);
				r.setArticle("bb49d42d30f94615ac9d439d523241c8");
				r.setAllowChange(true);
				r.setCreateBy("TEST");
				r.setCreateTime(new Date());
				r.setModifyBy("TEST");
				r.setModifyTime(r.getCreateTime());
				r.setStartTime(new Date());
				r.setEndTime(DateTime.now().plusDays(15).toDate());
				r.setDeadline(DateTime.now().plusDays(7).toDate());
				r.setStatus(Constants.MediaStatus.NORMAL);
				r.setDeleted(false);
				r.setIsPublic(true);
				r.setMediaTypes(type);
				r.setIndustryTypes(StringUtils.join(getRandomIndustryTypes(), ","));
				r.setRegions(StringUtils.join(getRandomRegions(), ","));
				r.setServiceTypes(StringUtils.join(getRandomServiceTypes(type), ","));
				r.setStatus(Constants.ReqStatus.NORMAL);
				
				dataList.add(r);
			}
			
			saveRequirements(dataList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void saveRequirements(List<Requirement> dataList) throws SolrServerException, IOException {
		
		dataList = (List<Requirement>) requirementService.save(dataList);
		
		ConcurrentUpdateSolrClient client = new ConcurrentUpdateSolrClient(PropertyUtils.getProperty("solr.baseUrl") + RequirementDoc.C_NAME, 5000, 10);
		List<RequirementDoc> docs = Lists.newArrayList();
		
		for( Requirement r : dataList ) {
			docs.add(new RequirementDoc(r));
		}
		
		client.addBeans(docs);
		
		UpdateResponse resp = client.commit();
		
		System.out.println(resp);
		
		client.close();
	}
	
	private List<String> weixinServices = Lists.newArrayList(
		"WEIXIN_S_SINGLE_P",
		"WEIXIN_S_MULTI_P_F",
		"WEIXIN_S_MULTI_P_S",
		"WEIXIN_S_MULTI_P_T",
		"WEIXIN_S_MULTI_P_O",
		"WEIXIN_S_FRIEND");
		
	private List<String> weiboServices = Lists.newArrayList(
		"WEIBO_S_H_RELAY",
		"WEIBO_S_H_DIRECTLY",
		"WEIBO_S_S_RELAY",
		"WEIBO_S_S_DIRECTLY",
		"WEIBO_S_RANDC_HARD",
		"WEIBO_S_RANDC_SOFT");

	
	private List<String> getRandomServiceTypes(String mediaType) {
		List<String> data;
		
		if( Constants.MediaType.WEIXIN.equals(mediaType) ) {
			data = weixinServices;
		} else {
			data = weiboServices;
		}
		int l = data.size();
		return Lists.newArrayList(data.get( Randoms.nextInt(137) % l));
	}
	
	private List<String> mediaTypes = Lists.newArrayList("MEDIA_T_WEIXIN", "MEDIA_T_WEIBO");
	
	private String getRandomMediaType() {
		int l = mediaTypes.size();
		return (mediaTypes.get( Randoms.nextInt(137) % l));
	}

	/**
	 * 创建广告主账号.
	 */
	private void createAdvertiser(int offset, int len) {
		for(int i = offset; i <= len; i++ ) {
			//executor.execute(new CreateAdvertiserWorker(i));
			try {
				customerService.register(newCustomerVO(i, "A"));
				counter.incrementAndGet();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 创建媒体账号.
	 */
	private void createProvider(int offset, int len) {
		for(int i = offset; i <= len; i++ ) {
			try {
				//executor.execute(new CreateProviderWorker(i));
				Customer cust = customerService.register(newCustomerVO(i, "P"));
				counter.incrementAndGet();
				List<Media> medias = Lists.newArrayList();
				medias.add(newWeibo(cust));
				medias.add(newWeixin(cust));
				mediaService.save(medias);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private Media newWeixin(Customer customer) {
		MediaWeixin m = new MediaWeixin();
		m.setCategory("");
		m.setMediaType(Constants.MediaType.WEIXIN);
		m.setCustomer(customer);
		m.setName(customer.getName() + "-微信媒体");
		m.setWeixinCode(m.getName());

		m.setDescription(m.getName() + m.getName() + m.getName());

		m.setCategory(getRandomWeixinCat());
		m.setIndustryType(StringUtils.join(getRandomIndustryTypes(), ","));
		m.setRegion(StringUtils.join(getRandomRegions(), ","));
		m.setFans(Randoms.nextInt(99999));
		m.setCreateTime(new Date());
		m.setModifyTime(m.getCreateTime());
		m.setStatus(Constants.MediaStatus.NORMAL);
		
		return m;
	}

	private Media newWeibo(Customer customer) {
		MediaWeibo m = new MediaWeibo();
		m.setCategory("");
		m.setMediaType(Constants.MediaType.WEIBO);
		m.setCustomer(customer);
		m.setName(customer.getName() + "-微博媒体");
		m.setWeiboPlatform("SINA");

		m.setDescription(m.getName() + m.getName() + m.getName());

		m.setCategory(getRandomWeiboCat());
		m.setIndustryType(StringUtils.join(getRandomIndustryTypes(), ","));
		m.setRegion(StringUtils.join(getRandomRegions(), ","));
		m.setFans(Randoms.nextInt(99999));
		m.setCreateTime(new Date());
		m.setModifyTime(m.getCreateTime());
		m.setStatus(Constants.MediaStatus.NORMAL);
		
		return m;
	}

	private List<String> weixinCats = Lists.newArrayList(
			"WEIXIN_C_PERSONAL",
			"WEIXIN_C_SUBSCRIBE",
			"WEIXIN_C_SERVICE");

	private String getRandomWeixinCat() {
		int l = weixinCats.size();
		return (weixinCats.get( Randoms.nextInt(137) % l));
	}
	
	private List<String> weiboCats = Lists.newArrayList(
			"WEIBO_C_BLUE",
			"WEIBO_C_YELLOW",
			"WEIBO_C_NULL");
	
	private String getRandomWeiboCat() {
		int l = weiboCats.size();
		return (weiboCats.get( Randoms.nextInt(137) % l));
	}
	
	private List<String> industryTypes = Lists.newArrayList(
			"101602", "101603", "101604", "101605", "101606", "101607", "101608", 
			"101609", "101610", "101611", "101612", "101613", "101614", "101615", 
			"101616", "101617", "101618", "101619", "101620", "101621", "101622", 
			"101623", "101624", "101625", "101626"	);
	
	private List<String> getRandomIndustryTypes() {
		int l = industryTypes.size();
		List<String> list = Lists.newArrayList(industryTypes.get( Randoms.nextInt(137) % l));
		return list;
	}
	
	private List<Area> areas;

	private List<String> getRandomRegions() {
		if( areas == null ) {
			areas = Lists.newArrayList(areaProvider.getAreaMap().values());
		}
		List<String> regions = Lists.newArrayList();
		regions.add((areas.get(Randoms.nextInt(areas.size() + 1) % areas.size())).getId());
		return regions;
	}
	
	/**
	 * 注册一个新账号.
	 * @param idx
	 * @param type
	 * @return
	 */
	private CustomerVO newCustomerVO(int idx, String type) {
		CustomerVO vo = new CustomerVO();
		vo.setLoginName(getLoginName(idx, type));
		vo.setMobPhone(vo.getLoginName());
		if( "P".equals(type) ) { //注册媒体账号
			vo.setCustType(Constants.CustType.CUST_PRO);
			vo.setName("测试媒体账号-" + vo.getLoginName());
		} else { //注册广告主账号
			vo.setCustType(Constants.CustType.CUST_ADV);
			vo.setName("测试广告主-" + vo.getLoginName());
		}
		vo.setPassword("12345678");
		
		return vo;
	}

	private String advPrefix; //广告主前缀
	
	private String proPrefix; //媒体账号前缀
	
	/**
	 * 生成登录名.
	 * @param idx
	 * @param type
	 * @return
	 */
	private String getLoginName(int idx, String type) {
		String prefix;
		if( "P".equals(type) ) {
			prefix = proPrefix;
		} else {
			prefix = advPrefix;
		}
		
		return prefix + String.format("%07d", idx);
	}
	
	

}
