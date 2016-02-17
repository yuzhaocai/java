<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>我的订单</title>
</head>

<body>
        
  <!-- Main -->
<div class="main-panel">
	      <div class="headline"><h5>我的订单</h5></div>
	      <!-- pad -->                
	          <div class="pad mtab-v2">
	              <ul class="nav nav-tabs">
	                  <li class="active"><a>进行中订单</a></li>
	                  <li class=""><a href="${ctx}/member/order/advertiser/finished" >已完成订单</a></li>
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
	                      <table class="table table-bordered table-striped" width="100%">
	                      	<thead>
	                            <tr>
	                              <th scope="col" width="10%">订单号</th>
	                              <th scope="col">标题</th>
	                              <th scope="col" width="18%">接单媒体</th>
	                              <th scope="col" width="10%">订单金额</th>
	                              <th scope="col" width="10%">订单日期</th>
	                              <th scope="col" width="8%">状态</th>
	                              <th scope="col" width="18%">操作</th>
	                            </tr>
	                          </thead>
	                          <tbody>
	                          <c:forEach items="${data.content }" var="item" varStatus="stat">
	                            <tr>
	                              <td>${item.id }</td>
	                              <td class="">
	                              	<a href="#" data-id="${item.requirement.id }" class="preview-requirement">
	                              		${item.requirement.name }
	                              	</a>
                             	  </td>
	                              <td>
	                              	<a href="#" data-id="${item.media.id }"  class="preview-media">
	                              		${item.media.name }
	                              	</a>
                              	</td>
	                              <td><span class="color-orange">${item.amount }</span></td>
	                              <td><fmt:formatDate value="${item.createTime }" pattern="yyyy/MM/dd"/></td>
	                              <td><zy:dic value="${item.status }"/></td>
	                              <td class="manage text-right minWidthFour">
	                              <a data-id="${item.requirement.id }" class="preview-requirement" href="#">详情</a> |
	                              <c:choose>
	                              	<c:when test="${item.status == 'ORDER_S_PROGRESS' }">
	                              		<a href="#" class="cuidan" data-id="${item.id }">催单</a> | 
	                              	</c:when>
	                              	<c:when test="${item.status == 'ORDER_S_DELIVERED' }">
		                                  <a href="#" class="yanshou" data-id="${item.id }">验收</a> | 
	                              	</c:when>
                           		    <c:when test="${item.status == 'ORDER_S_ACCEPTANCE' }">
		                                  <a href="#" class="fukuan" data-id="${item.id }">付款</a>
		                                | <a href="#" class="jufu" data-id="${item.id }">拒付</a>  | 
	                              	</c:when>
	                              			                                
	                              </c:choose>
	                               <!-- 留言 -->
	                                  <a href="#" class="orderMessage" data-id="${item.id }"><c:if test="${item.hasMessage4P }"><i class="tips"></i></c:if>留言</a>                              
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

<!-- 催单 -->
<div class="modal fade" id="cuidan" tabindex="-1" role="dialog" data-replace="false" data-backdrop="static">

    <form id="cuidan-form">
      <input type="hidden" name="id">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">向媒体催单</h4>
      </div>
      <div class="modal-body">
      	您可以向媒体发送催单消息，提醒媒体尽快完成任务。
      </div>
      <div class="modal-footer">
      	<button class="btn-u btn-u-red" type="button" 
      		id="btn-send-cuidan"
      		data-loading-text="正在发送...">发送</button>
      </div>
    </form>

</div>

<!-- 拒付 -->
<div class="modal fade" id="jufu" tabindex="-1" role="dialog" data-replace="false" data-backdrop="static">

    <form id="jufu-form" action="${ctx}/member/order/advertiser/refuse" method="post">
      <input type="hidden" name="id">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">拒付理由</h4>
      </div>
      <div class="modal-body has-feedback">
      		<textarea class="form-control" placeholder="请输入..." id="refuseReason" name="refuseReason"></textarea>
      </div>
      <div class="modal-footer">
      	<button class="btn-u btn-u-red" type="submit" 
      		id="btn-refuse-ok"
      		data-loading-text="正在处理...">确定</button>
      </div>
    </form>

</div>

<!-- 付款 -->
<div class="modal fade" id="fukuan" tabindex="-1" role="dialog" data-width="640" data-replace="true" data-backdrop="static">

     <form id="pay-form" class="form-horizontal" action="${ctx}/member/order/advertiser/pay" method="post">
     <input type="hidden" name="id" >
     
     <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">提示</h4>
      </div>
      <div class="modal-body">
      	<h4 class="text-center">请确认订单已经验收通过，付款后接单媒体将收到订单中的金额！</h4>
      	<div class="checkbox text-right">
      	<label>
	      <input type="checkbox" id="needInvoice" name="needInvoice" value="1"> 是否需要开具发票？
	    </label>
      	</div>
      	<hr class="invoice hide">
      	<div class="form-group has-feedback invoice hide">
      		<label class="col-sm-3 control-label">发票类型：</label>
      		<div class="col-sm-4">
	      		<select class="form-control" name="type">
	      		<c:forEach items="${types }" var="type">
	      			<option value="${type.itemCode }">${type.itemName }
	      		</c:forEach>
	      		</select>
      		</div>
      		<div class="col-sm-4">
	      		<label class="radio-inline">
				  <input type="radio" name="billType" value="BILL_T_PERSONAL" checked="checked"> 个人
				</label>
	      		<label class="radio-inline">
				  <input type="radio" name="billType" value="BILL_T_COMPANY"> 公司
				</label>
      		</div>
      	</div>
      	<div class="form-group invoice hide">
      		<label class="col-sm-3 control-label">发票抬头：</label>
      		<div class="col-sm-9 has-feedback">
      			<input class="form-control" name="title" maxlength="50">
      		</div>
      	</div>
      </div>
      <div class="modal-footer">
      	<button class="btn-u btn-u-red" type="submit" 
      		id="btn-pay-ok"
      		data-loading-text="正在处理...">确认付款</button>
      </div>

    </form>

</div>

<!-- 验收 -->
<div class="modal fade" id="yanshou" tabindex="-1" role="dialog" data-width="750" data-replace="false" data-backdrop="static"></div>
	
<!-- 需求详情 -->
<div class="modal fade" id="requirement-view" tabindex="-1" role="dialog" data-width="700" data-replace="true"></div>

<!-- 媒体详情 -->
<div class="modal fade" id="media-view" tabindex="-1" role="dialog" data-width="900" data-replace="true"></div>
    
<script src="${ctx}/static/assets/plugins/jquery/zclip/jquery.zclip.min.js" type="text/javascript"></script>
<script type="text/javascript">
$(function() {
	menu.active('#advertiser-order');
	//媒体详情
	common.showMedia(".preview-media");
	
	common.showRequirement(".preview-requirement");
	//催单
	var $chuidanModal = $('#cuidan');
	$('.cuidan').click(function() {
		var id =  $(this).data('id');
		$('#cuidan-form input[name="id"]').val(id);
		
		$chuidanModal.showModal();
	});
	
	$('#btn-send-cuidan').click(function() {
		var btn = this;
		$(btn).button('loading');
		var url = '${ctx}/member/order/advertiser/remind?ajax';
		$.post(url, $("#cuidan-form").serialize(), function(data) {
			$chuidanModal.hideModal();
			$(btn).button('reset');
			common.showMessage('您已经成功向媒体发送催单消息！')
		});
	});
	///催单
	
	//拒付
	var $jufuModal = $('#jufu');
	$('.jufu').click(function() {
		var id =  $(this).data('id');
		$('#jufu-form input[name="id"]').val(id);
		
		$jufuModal.showModal();
	});
	
	$('#jufu-form').validate({
		submitHandler: function(form) {
			$jufuModal.modal('loading');
			$('#btn-refuse-ok').button('loading');
			form.submit();
		}, 
		rules: {
			refuseReason: {
				required: true,
				rangelength:[2, 200],
				unRegex:/\d{5}/
			}
		},
		messages: {
			refuseReason: {
				required: '请输入拒付理由。',
				rangelength: '请输入2~200个字符',
				unRegex: '为保证交易安全，不要留下手机号、QQ号等联系方式。'
			}
		}
	});
	///拒付
	
	//验收
	$('.yanshou').on('click', function () {
		var id = $(this).data('id');
		//ajax加载验收对话框
		$('#yanshou').loadModal('${ctx}/member/order/advertiser/check?ajax', {id: id}, function() {
			//验收通过，弹出付款确认对话框
			$('#btn-yanshou-pass').click(function() {
				$('#yanshou').hideModal();
// 				$('#pay-form input[name="id"]').val(id);
// 				$fukuanModal.showModal();
			});
		});
	});
	///验收
	
	//付款
	$fukuanModal = $('#fukuan');
	$('.fukuan').click(function() {
		var id = $(this).data('id');
		$('#pay-form')[0].reset();
		$('#pay-form input[name="id"]').val(id);
		$('#needInvoice').attr('checked', false);
		$('.invoice').hide();
		
		$fukuanModal.showModal();
	});
	
	//是否需要发票
	$('#needInvoice').click(function() {
		if( this.checked ) {
			$('.invoice').removeClass('hide').show();
		} else {
			$('.invoice').hide();
		}
		$fukuanModal.modal('layout');
	});
	
	function needInvoice() {
		return $('#pay-form input[name="needInvoice"]:checked').length > 0;
	}
	
	$('#pay-form').validate({
		submitHandler: function(form) {
			$fukuanModal.modal('loading');
			$('#btn-pay-ok').button('loading');
			form.submit();
		}, 
		rules: {
			title: {
				required: {
					depends: needInvoice
				},
				rangelength:[2, 200]
			}
		},
		messages: {
			title: {
				required: '请输入发票抬头！',
				rangelength: '请输入2~50个字符'
			}
		}
	});

	/// 付款
	
});
</script>

 <%@include file="/WEB-INF/views/member/order/queryOrderMessage.jspf" %> 
 
</body>
</html>
