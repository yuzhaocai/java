<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>已处理的需求</title>
</head>

<body>
        
   <!-- Main -->
        <div class="main-panel">
            <div class="headline"><h5>我的订单</h5></div>
            <!-- mTab v2 -->                
                <div class="pad mtab-v2">
                    <ul class="nav nav-tabs">
                        <li class="active"><a href="${ctx}/member/order/provider" >进行中订单</a></li>
                        <li><a href="${ctx}/member/order/provider/finished" >已完成订单</a></li>
                    </ul>                
                    <div class="tab-content">
                        <div class="tab-pane active" id="transactions">
                          	<div class="topwrap">
                                <form class="form-inline">
                                    <div class="form-group form-group-sm">
                                        <input class="form-control input-sm Wdate " id="search_GTE_createTime" name="search_GTE_createTime" value="" placeholder="开始日期" onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'#F{$dp.$D(\'search_LTE_createTime\')||\'%y-%M-%d\'}'})" type="text">                             
                                    </div>
                                    <div class="form-group form-group-sm">
                                        <input class="form-control input-sm Wdate " id="search_LTE_createTime" name="search_LTE_createTime" value="" placeholder="结束日期" onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',minDate:'#F{$dp.$D(\'search_GTE_createTime\')}'})" type="text">                              
                                    </div>
                                    <button class="btn-u btn-u-sm btn-u-dark"><i class="fa fa-search"></i> 查询</button>
                                </form>
                            </div>
                            <table width="100%" class="table table-bordered table-striped">
                            	<thead>
                                  <tr>
                                    <th scope="col" width="10%">订单号</th>
                                    <th scope="col" width="10%">支付方式</th>
                                    <th scope="col" width="15%">需求名称</th>
                                    <th scope="col" width="8%">开始时间</th>
                                    <th scope="col" width="8%">结束时间</th>
                                    <th scope="col" width="15%">接单媒体</th>
                                    <th scope="col" width="8%">订单报价</th>
                                    <th scope="col" width="8%">状态</th>
                                    <th scope="col" width="14%">操作</th>
                                  </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${data.content }" var="order">
                                <tr>
                                    <td>${order.id }</td>
                                    <td><zy:dic value="${order.paymentType }"/></td>
									<td><a data-id="${order.requirement.id }"
										class="preview-requirement" href="#"><zy:out
												value="${order.requirement.name }" len="15" /></a></td>
									<td><fmt:formatDate value="${order.requirement.startTime }" pattern="yyyy/MM/dd"/></td>
                                    <td><fmt:formatDate value="${order.requirement.endTime }" pattern="yyyy/MM/dd"/></td>
                                    <td><a data-id="${order.media.id }"  class="preview-media" href="#">${order.media.name }</a></td>
                                    <td><span class="color-orange">${order.amountMedia }</span></td>
                                    <td><zy:dic value="${order.status }"/></td>
                                    <td class="manage">
	                                    <c:if test="${order.status eq 'ORDER_S_PROGRESS'}">
	                                    	<a href="javascript:void(0)" onclick="mediaDeliverTask('${order.id}')">订单交付</a>  | 
	                                    </c:if>
	                                    <c:if test="${(order.status eq 'ORDER_S_DELIVERED') and (order.paymentType eq 'PAYMENT_T_ONLINE')}">
	                                    	<a href="#" data-toggle="modal" class="cuikuan" data-target="#cuikuan" data-id="${order.id }">催款</a>  | 
	                                    </c:if>
	                                    <!-- 留言 -->
	                                    <a href="#" class="orderMessage" data-id="${order.id }">
	                                    	<c:if test="${order.hasMessage4A }"><i class="tips"></i></c:if>
	                                   		留言
	                                	</a>
                                    </td>
                                  </tr>
                                  </c:forEach>
                                  <c:if test="${empty data.content }">
									<tr>
										<td colspan="8">
										没有搜索到符合条件的记录！
										</td>
									</tr>
								</c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <!-- End Tab v2 --> 
                    <tags:pagination page="${data}" />
        </div>
        <!-- End Main -->
        
<!-- 订单交付 -->
	<div class="modal fade" id="deliverableModal" tabindex="-1"
		role="dialog" data-width="640" data-height="500" data-backdrop="static"></div>
	
	<!-- 催款 -->
	<div class="modal fade" id="cuikuan" tabindex="-1" role="dialog" data-width="640"
		aria-labelledby="myModalLabel" data-replace="true">
				<form id="cuikuan-form">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title">向广告商催款</h4>
					</div>
					<input type="hidden" name="id">
					<div class="modal-body">您可以向广告主发送催款消息，提醒广告主尽快付款。</div>
					<div class="modal-footer">
				<button class="btn-u btn-u-red" type="button" id="btn-send-cuikuan"
					data-loading-text="正在发送...">发送</button>
			</div>
				</form>
	</div>
	
	<!-- 需求详情 -->
	<div class="modal fade" id="requirement-view" tabindex="-1" role="dialog" data-width="700" data-replace="true">
    </div>
	
	<!-- 媒体详情 -->
	<div class="modal fade" id="media-view" tabindex="-1" role="dialog" data-width="900" data-replace="true">
    </div>

	<script type="text/javascript">
		$(function() {
			menu.active('#provider-order');
		});
		
		common.showRequirement(".preview-requirement");
		common.showMedia(".preview-media");
		
		/**
		弹出订单交付
		 */
		function mediaDeliverTask(id) {
			$('#deliverableModal').loadModal(
					'${ctx}/member/order/provider/deliver?ajax', {
						id : id
					});
		}
		
		var $cuikuanModal = $('#cuikuan');
		$('.cuikuan').click(function() {
			var id =  $(this).data('id');
			$('#cuikuan-form input[name="id"]').val(id);
			
			$cuikuanModal.showModal();
		});

		//催款
		$('#btn-send-cuikuan').click(function() {
			var btn = this;
			$(btn).button('loading');
			var url = '${ctx}/member/order/provider/remind?ajax';
			$.post(url, $("#cuikuan-form").serialize(), function(data) {
				$cuikuanModal.hideModal();
				$(btn).button('reset');
				common.showMessage('您已经成功向广告主发送催款消息！');
			});
		});
		///催款
	</script>
 <%@include file="/WEB-INF/views/member/order/queryOrderMessage.jspf" %> 
</body>
</html>
