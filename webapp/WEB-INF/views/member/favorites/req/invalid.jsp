<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>我的订单</title>
    <style type="text/css">
	.funny-table .table-row-detail {display: none;}
	.funny-table .table-row .stick {display: none;}
	.funny-table .table-row-detail dt {width: 80px; text-align: right;}
	</style>   
</head>

<body>
        
  <!-- Main -->
<div class="main-panel">
	      <div class="headline"><h5>我的收藏</h5></div>
	      <!-- pad -->                
	          <div class="pad mtab-v2">
	              <ul class="nav nav-tabs">
	                  <li class=""><a href="${ctx}/member/favoritesReq/list">正在进行</a></li>
	                  <li class="active"><a>已失效</a></li>
	              </ul>                
	              <div class="tab-content">
	                  <div class="tab-pane active" id="transactions">
                		 <div class="topwrap">
                         	<form class="form-inline">
                             <div class="form-group form-group-sm">
                             		<label>名称</label>
                             		<input name="search_LIKE_req.name" value="${search_LIKE_item.req.name }" type="text" class="form-control"> 
                             </div>
                             <button class="btn-u btn-u-sm btn-u-dark"><i class="fa fa-search"></i> 查询</button>
                         	</form>
	                      </div>
	                      <table class="table table-bordered table-striped funny-table" width="100%">
	                      	<thead>
	                            <tr>
		                            <th scope="col" width="10%" >大类</th>
				                    <th scope="col" width="15%" >预算</th>
				                    <th scope="col" width="30%" >广告活动发布时间</th>
				<!--                     <th scope="col" width="15%" >结束时间</th> -->
				                    <th scope="col" width="23%" >名称</th>
				                    <th scope="col" width="25%" >响应截止时间</th>
	                            </tr>
	                          </thead>
	                          <tbody>
	                          <c:forEach items="${data.content }" var="item" varStatus="stat">
	                            <tr class="table-row">
	                              <td><zy:dic value="${item.req.mediaTypes }"/></td>
	                              <td class="text-left">
	                              	${item.req.budget }
                             	  </td>
	                              <td><fmt:formatDate value="${item.req.startTime}" pattern="yyyy/MM/dd"/> 至  <fmt:formatDate value="${item.req.endTime}" pattern="yyyy/MM/dd"/></td>
	                              <td><a href="#" class="view-detail" data-id="${item.req.id}" data-deadline="${item.req.deadline}">${item.req.name}</a></td>
	                              <td><fmt:formatDate value="${item.req.deadline}" pattern="yyyy/MM/dd"/>
	                              <i class="fa fa-caret-down"></i>
	                              <button type="button" class="btn-u btn-u-sm btn-u-red pull-right cancel-favorites" style="margin-left:10px" id="btn-cancel${item.req.id}" data-id="${item.req.id}">取消收藏</button>
	                              </td>
	                            </tr>
	                            <tr class="table-row-detail">
	                            	<td colspan="6">
							        <dl class="dl-inline">
							           <dt>服务类型：</dt>
							           <dd><zy:dic value="${item.req.serviceTypes }"/></dd>
							           <dt>行业：</dt>
							           <dd><zy:dic value="${item.req.industryTypes }"/></dd>
							           <dt>改稿：</dt>
							           <dd><c:choose><c:when  test="${item.req.allowChange}">是</c:when><c:otherwise>否</c:otherwise>
							           </c:choose></dd>
							        </dl>
							        <dl class="dl-inline">
							           <dt>需求概述：</dt>
							           <dd>${item.req.summary} <a href="#" class="view-detail" data-id="${item.req.id}" data-deadline="${item.req.deadline}">查看详细</a></dd>
							        </dl>
							     	</td>
	                            </tr>
	                          </c:forEach>
								<c:if test="${empty data.content }">
									<tr>
										<td colspan="7">
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
	
	<tags:pagination page="${data }" />
                
</div>
<!-- End Main -->

<%-- 需求详情模态框 --%>
<div class="modal fade xiangqing" id="detail-modal" data-width="640"></div>

<%-- 进行抢单对话框 --%>
<div class="modal fade container" id="enlist-modal" data-replace="true" data-backdrop="static"></div>


<script type="text/javascript">
$(function() {
	menu.active('#favorites-req');
	$("i.fa").click(function(){
		var parent = $(this).closest(".table-row");
		if ($(this).hasClass("fa-caret-down")) {
			$(this).removeClass("fa-caret-down");
			$(this).addClass("fa-caret-up");
			parent.next('.table-row-detail').show();
		} else {
			$(this).removeClass("fa-caret-up");
			$(this).addClass("fa-caret-down");
			parent.next('.table-row-detail').hide();
		}
	});
	$('.view-detail').click(function(event) {
		var url = '${ctx}/plaza/detail/' + $(this).data('id') + "?ajax";
		$('#detail-modal').loadModal(url);
		
		event.preventDefault();
		return false;//阻止事件冒泡
	});
	
	var $enlistModal = $('#enlist-modal');
	//点击列表的“抢单”
	$('.btn-enlist').click(function(event) {
			//最迟响应时间
			var deadlineStr = $(this).data('deadline');
			if(common.testDeadLine(deadlineStr)){
				var id = $(this).data('id');
				var url = '${ctx}/plaza/enlist/' + id + '?ajax';
				var checkEnlistUrl = '${ctx}/plaza/checkEnlist/' + id + '?ajax';
				
				$.get(checkEnlistUrl, function(data) {
					check = $.parseJSON(data);
					if (check.success) {
						$enlistModal.loadModal(url);
					} else {
						common.showMessage('该需求抢单数量已达上线！', 'warn');
					}
				});
			}else{
				common.showMessage("抢单失败，已超过最迟响应时间","warn");
			}
			event.preventDefault();
			return false;//阻止事件冒泡
		});
	$('.cancel-favorites').click(function(){
		var id = $(this).data('id');
		bootbox.confirm("您确定要取消收藏？",function(result){
			if(result){
				$.post("${ctx}/member/favoritesReq/delete", {reqId:id}, function(rsp) {
					rsp = $.parseJSON(rsp);
					if (rsp.result){
						common.showMessage("取消收藏成功");
						window.location.reload();
					}
				});
			}
		});
	});
	//点击详情对话框的“抢单”
	$('#detail-modal').delegate('.btn-enlist', 'click', function(event) {
		var id = $(this).data('id');
		//最迟响应时间
		var deadlineStr = $(this).prev().text();
		if(common.testDeadLine(deadlineStr)){
			var url = '${ctx}/plaza/enlist/' + id + '?ajax';
			$enlistModal.loadModal(url);
		}else{
			common.showMessage("抢单失败，已超过最迟响应时间","warn");
		}
		event.preventDefault();
		return false;//阻止事件冒泡
	});
	
	
	//勾选“应征”
	$enlistModal.delegate('.chk-enlist', 'click', function(event) {
		var d = "disabled";
		var cls = $(this).attr('class').split(' ')[1];
		var $table = $('.table');
		if (this.checked) {
			$(this).closest('td').find('input.quote-field').removeClass(d).removeAttr(d);
			
			$table.find('input.'+cls+'').not($(this)).each(function(){
				$(this).addClass(d).attr(d, d);
			});
		} else {
			$(this).closest('td').find('input.quote-field').addClass(d).attr(d, d);
			
			$table.find('input.'+cls+'').not($(this)).each(function(){
				$(this).removeClass(d).removeAttr(d);
			});
		}
	});

	//确定抢单
	$enlistModal.delegate('button:submit', 'click', function(event) {
		var id = $(this).data('id');
		var d = "disabled";
		var url = '${ctx}/plaza/enlist';
		var params = $('#enlist-form').serialize();

		if ($('#enlist-form input[name="mid"]:checked').length > 0) {
			$enlistModal.modal('loading');
			$.post(url, params, function(data) {
				common.log(data);
				data = $.parseJSON(data);
				if (data.status === '400' || data.status === '500') {
					common.showMessage(data.message, 'warn');
				} else if (data.status === '302') { //跳转到登录页面
					location.replace(data.url);
				} else { //成功
					common.showMessage(data.message);
					$('#btn-enlist' + id).addClass(d).attr(d, d);
				}

				$enlistModal.hideModal();
			});

			return false;
		} else {
			if($('#jGrowl').children().length<2){
				common.showMessage('请至少选择一组媒体报价后抢单！', 'warn');
			}else{
				common.shake($('#jGrowl'));
			}
			return false;
		}
	});
});
</script>

 
</body>
</html>
