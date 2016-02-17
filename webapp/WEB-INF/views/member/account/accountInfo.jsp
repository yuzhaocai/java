<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>

<!--strat user-info  -->
<div class="user-info">
	<div class="row">
		<div class="col-xs-6">
			<div class="info-lcol">
				<div class="u-pic">
					<img src="${ctx}/static/assets/img/user.jpg">
					<div class="mask"></div>
				</div>
				<div class="info-m">
					<div class="row">
						<div class="col-lg-12 u-name">
							<c:choose>
								<c:when test="${empty account.customer.name }">
									<shiro:principal />
								</c:when>
								<c:otherwise>${account.customer.name }</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-5">
							<strong>余额： <span class="" style="color:red"> <fmt:formatNumber
										value="${account.avBalance }" currencyCode="CNY"
										type="currency" />
							</span>
							</strong>
						</div>
						<a href="${ctx }/member/account/charge" class="btn-u btn-u-xs btn-u-orange">充值</a>
						<shiro:hasRole name="provider">
							&nbsp;&nbsp;&nbsp; <a href="${ctx }/member/account/withdraw" class="btn-u btn-u-xs btn-u-orange">提现</a>
						</shiro:hasRole>
						<shiro:hasRole name="advertiser">
							&nbsp;&nbsp;&nbsp; 
							<a href="javascript:void(0)" data-id="${account.customer.id }"
								data-account="${account.avBalance }" class="btn-u btn-u-xs btn-u-orange offer-line">购买线下服务</a>
						</shiro:hasRole>
						<div class="margin-bottom-5"></div>
					</div>
					<div class="row">
						<div class="u-link">
							<div class="col-xs-9">
								<a href="${ctx}/member/account/transaction">交易明细</a> 
								| <a href="${ctx}/member/account/chargeList">充值记录</a> 
								<shiro:hasRole name="provider">
									| <a href="${ctx}/member/account/withdrawList">提现记录</a>
								</shiro:hasRole>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="col-xs-6">
			<div class="info-rcol">
<%-- 				<img src="${ctx}/static/assets/img/ads/ads-user-info.png" --%>
<!-- 					width="550" height="100"> -->
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="offerLineModal" tabindex="-1"  role="dialog" data-width="700" aria-labelledby="myModalLabel" data-replace="true"></div>
<script type="text/javascript">
	$(function(){
		$('.offer-line').click(function(){
			$('#offerLineModal').loadModal('${ctx }/member/account/offerLine?ajax');
		});
	})
</script>
<!-- end user-info -->