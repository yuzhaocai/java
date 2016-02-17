<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
                        <li class=""><a href="${ctx}/member/order/advertiser"　>进行中订单</a></li>
                        <li class="active"><a >已完成订单</a></li>
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
                                    <th scope="col" width="20%">标题</th>
                                    <th scope="col" width="18%">接单媒体</th>
                                    <th scope="col" width="10%">订单金额</th>
                                    <th scope="col" width="10%">订单日期</th>
                                    <th scope="col" width="8%">状态</th>
                                    <th scope="col" width="14%">操作</th>
                                  </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${data.content }" var="item" varStatus="stat">
                                  <tr>
                                    <td>${item.id }</td>
                                    <td>
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
                                    <td>
                                    	<a href="#" class="btn-jubao" data-toggle="modal" data-id="${item.id }">举报</a>                        
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

<!-- 举报 -->
<div id="jubao" class="modal fade" tabindex="-1" role="dialog" data-backdrop="static">
    <form id="jubao-form">
      <input type="hidden" name="id">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">举报理由</h4>
      </div>
      <div class="modal-body has-feedback">
      		<textarea class="form-control" placeholder="请输入..." name="reason" id="reason"></textarea>
      </div>
      <div class="modal-footer">
      	<button class="btn-u btn-u-red" type="submit" id="btn-jubao-ok">确定</button>
      </div>
    </form>
</div>

<!-- 需求详情 -->
<div class="modal fade" id="requirement-view" tabindex="-1" role="dialog" data-width="700" data-replace="true"></div>

<!-- 媒体详情 -->
<div class="modal fade" id="media-view" tabindex="-1" role="dialog" data-width="900" data-replace="true"></div>

<script type="text/javascript">
$(function() {
	menu.active('#advertiser-order');
	//需求详情 
	common.showRequirement(".preview-requirement");
	//媒体详情
	common.showMedia(".preview-media");
	var $jubaoForm = $('#jubao-form');
	
	function resetJubaoForm() {
		$jubaoForm.find('.has-error').each(function() {
			$(this).removeClass('has-error');
			$(this).find('.help-block').remove();
		});
		$jubaoForm.find('[name=reason]').val('');
	}
	//举报
	$('.btn-jubao').click(function () {
		resetJubaoForm();
		var id = $(this).data('id');
		$jubaoForm.find('[name="id"]').val(id);
		
		$('#jubao').modal('show');
	});
	
	$jubaoForm.validate({
		submitHandler: function(form) {
			common.disabled('#btn-jubao-ok');
			var params = $(form).serialize();
			$.post('${ctx}/member/order/advertiser/complaint?ajax', params, function(data) {
				common.hideModal('#jubao');
				common.enabled('#btn-jubao-ok');
				
				data = $.parseJSON(data);
				if( data.result == 0 ) {
					common.showMessage(data.content, 'warn');
				} else {
					common.showMessage(data.content);
				}
			});
		}, 
		rules: {
			reason: {
				required: true,
				rangelength:[2, 200] 
			}
		},
		messages: {
			reason: {
				required: '请输入举报理由。',
				rangelength: '请输入2~200个字符。'
			}
		}
	});
	///举报
	
});
</script>
   
</body>
</html>
