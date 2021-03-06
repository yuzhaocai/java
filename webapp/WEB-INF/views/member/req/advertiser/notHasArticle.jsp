<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>我的需求</title>
    <style type="text/css">
    .form-inline .form-control.Wdate {
    	float: none;
    }
    </style>
</head>

<body>
        <!-- Main -->
        <div class="main-panel">
            <div class="headline"><h5>我的需求</h5></div>
            <!-- mTab v2 -->                
                <div class="pad mtab-v2">                
               		<ul class="nav nav-tabs">
                        <li><a href="${ctx}/member/req/advertiser/list" >投放需求</a></li>
                        <li class="active"><a >定制服务</a></li>
                    </ul>
                    <div class="tab-content">
                    	<div class="tab-pane active">
                            <div class="topwrap">
                                <form class="form-inline">
                                    <div class="form-group form-group-sm">
			                        	<label class="control-label">创建时间</label>
			                            <input class="form-control input-sm Wdate " id="search_GTE_createTime" name="search_GTE_createTime" 
			                            	value="${param.search_GTE_createTime }" placeholder=" 从" onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'#F{$dp.$D(\'search_LTE_createTime\')||\'%y-%M-%d\'}'})" type="text">                             
			                        </div>
			                        <div class="form-group form-group-sm">
			                            <input class="form-control input-sm Wdate " id="search_LTE_createTime" name="search_LTE_createTime" 
			                            	value="${param.search_LTE_createTime }" placeholder="至" onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',minDate:'#F{$dp.$D(\'search_GTE_createTime\')}'})" type="text">                              
			                        </div>
			                        <button class="btn-u btn-u-sm btn-u-dark"><i class="fa fa-search"></i> 查询</button>
			                        <a class="btn-u btn-u-sm btn-u-red pull-right" onClick="$('#send-order-modal').loadModal('${ctx}/order/direct/send?ajax')"><span class="glyphicon glyphicon-plus"></span> 创建新需求</a> 
                                </form>
                            </div>
                            <table width="100%" class="table table-bordered table-striped">
                                <thead>
                                  <tr>
                                    <th scope="col" >编号</th>
                                    <th scope="col" >标题</th>
                                    <th scope="col" >项目预算</th>
                                    <th scope="col" >最迟响应时间</th>
                                  </tr>
                                </thead>
			                    <tbody>
			                    <c:forEach items="${data.content }" var="item">
			                      <tr>
			                        <td>${item.id }</td>
			                        <td class="">
			                        	<a href="${ctx}/member/req/advertiser/view/${item.id}" title="${item.name }" data-id="${item.id }"class="preview-requirement" >
			                        		${item.name }
		                        		</a>
	                        		</td>
			                        <td><span class="color-orange">${item.budget }</span></td>
			                        <td><fmt:formatDate value="${item.deadline }" pattern="yyyy/MM/dd" /></td>
			                      </tr>
			                    </c:forEach>
			                    </tbody>                                
                            </table>
                        </div>
                    </div>
                    <!--// tab-content end-->
                </div>
                <!-- End Pad -->

                <tags:pagination page="${data }" />
        </div>
        <!-- End Main -->
<!-- 选定接单媒体 -->
<div id="select-media" class="modal container fade" tabindex="-1" role="dialog"  data-backdrop="static"></div>

<!-- 查看改稿申请 -->
<div id="chakan" class="modal fade " tabindex="-1" role="dialog" data-width="640" data-backdrop="static"></div>

<!-- 需求详情 -->
<div class="modal fade" id="requirement-view" tabindex="-1" role="dialog" data-width="700" data-replace="true"></div>


<!--一键下单模态框-->
	<div class="modal fade" id="send-order-modal" tabindex="-1"
		role="dialog" data-width="520" data-height="180" aria-labelledby="myModalLabel"
		data-replace="true">

	</div>

<script type="text/javascript">

$(function() {
	menu.active('#my-req');
	//需求详情 
	common.showRequirement(".preview-requirement");
});
var $selectModal = $('#select-media');
var reqEndTime;

function bindSelectMediaModal() {
	//--查看改稿申请
	$('.chakan').click(function() {
		var id = $(this).data('id');
		var data = {id: id};
		var url = '${ctx}/member/req/advertiser/viewChangedArticle?ajax';
		$('#chakan').loadModal(url, data);
	});
	//--/查看改稿申请
	
	//--生成订单
	$('.accept').click(function() {
		var id = $(this).data('id');
		var $tr = $(this).closest('tr');
		var data = {id:id,endTime:reqEndTime};
		$selectModal.modal('loading');
		
		$.post('${ctx}/member/req/advertiser/accept?ajax',data, function(data) {
			data = $.parseJSON(data);
			if( data.result ) {			
				common.showMessage(data.content);
				$tr.remove();
			} else {
				common.showMessage(data.content, 'warn');
			}
			
			$selectModal.modal('loading');
		});
		
	});
	//--/生成订单
	
	//--拒绝
	$('.refuse').click(function() {
		var id = $(this).data('id');
		var changed = $(this).data('changed');
		var invite = $(this).data('invite-type');
		
		var $tr = $(this).closest('tr');
		
		function refuse() {
			$selectModal.modal('loading');
			$.post('${ctx}/member/req/advertiser/refuse/' + id + '?ajax', function(data) {
				$tr.remove();
				common.showMessage('拒绝媒体操作成功!');
				$selectModal.modal('loading');
			});
		}
		
		if( !changed && invite === 'INVITE_T_PASSIVE' ) {
			common.confirm({
				//width: 500,
				message: '此媒体为您主动邀请，且对方并未修改您的稿件。如果拒绝，将会扣除您的 50 信用值。',
				ok: refuse
			});
		} else {
			refuse();
		}
	});
	//--/拒绝
}

//-弹出选择媒体对话框
$('.select-media').click(function() {
	var id = $(this).data('req-id');
	reqEndTime = $(this).data('req-time');
	var data = {id: id};
	$('#select-media').loadModal('${ctx}/member/req/advertiser/selectMedia?ajax', data, bindSelectMediaModal);
});
//-/弹出选择媒体对话框

</script>
   
</body>
</html>
