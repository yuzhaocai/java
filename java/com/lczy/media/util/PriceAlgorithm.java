package com.lczy.media.util;

/**
 * 价格算法
 * 
 * @author wang.haibin
 *
 */
public class PriceAlgorithm {
	
	/**
	 * 从结算价计算报价
	 * 
	 * @param price              结算价
	 * @param provideInvoice     是否提供发票
	 * @param percent            星比例
	 * @param taxRate            税点
	 * @return
	 */
	public static int calcPrice(int price, boolean provideInvoice, float percent, float taxRate) {
		if (provideInvoice) {
			// 媒体报价（提供发票）   = 结算价 * 活动折扣 *（星比例 + 1）
			return regulatePrice(Math.round(price * (percent + 1)));
		} else {
			// 媒体报价（不提供发票） = 结算价 * 活动折扣 *（星比例 + 1）+ 结算价 * 6.72%
			return regulatePrice(Math.round(price * (percent + 1) + price * taxRate));
		}
	}
	
	/**
	 * 从结算价计算活动价
	 * 
	 * @param price              结算价
	 * @param provideInvoice     是否提供发票
	 * @param percent            星比例
	 * @param taxRate            税点
	 * @param discount           活动折扣
	 * @return
	 */
	public static int calcPriceActivity(int price, boolean provideInvoice, float percent, float taxRate, float discount) {
		if (provideInvoice) {
			// 媒体报价（提供发票）   = 结算价 * 活动折扣 *（星比例 + 1）
			return regulatePrice(Math.round(price * discount * (percent + 1)));
		} else {
			// 媒体报价（不提供发票） = 结算价 * 活动折扣 *（星比例 + 1）+ 结算价 * 6.72%
			return regulatePrice(Math.round(price * discount * (percent + 1) + price * taxRate));
		}
	}
	
	/**
	 * 从结算价计算税款
	 * 
	 * @param price              结算价
	 * @param provideInvoice     是否提供发票
	 * @param taxRate            税点
	 * @return
	 */
	public static int calcTax(int price, boolean provideInvoice, float taxRate) {
		return provideInvoice ? 0 : Math.round(price * taxRate);
	}
	
	/**
	 * 规整显示价格：
	 *   媒体报价 < 1000
	 *     个位数为准，5以下取5，5以上取10，5不变
	 *     
	 *   1000 < 媒体报价 < 20000
	 *     十位数为准，50以下取50，50以上取100，50不变
	 *     
	 *   媒体报价 > 20000
	 *     百位数为准，500以下取500，500以上取1000，500不变
	 * @param price
	 * @return
	 */
	public static int regulatePrice(int price) {
		if (price < 1000) {
			return (price + 4) - (price + 4) % 5;
		} else if (price < 20000) {
			return (price + 49) - (price + 49) % 50;
		} else {
			return (price + 499) - (price + 499) % 500;
		}
	}
	
	public static void main(String[] args) {
		for (int i=2400; i < 2460; i++) {
			System.out.println(regulatePrice(i));
		}
	}
}
