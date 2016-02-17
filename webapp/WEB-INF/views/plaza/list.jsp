<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>采媒在线</title>
    <style type="text/css">
	.funny-table .table-row-detail {display: none;}
	.funny-table .table-row .stick {display: none;}
	.funny-table .table-row-detail dt {width: 80px; text-align: right;}
	</style>    
</head>

<body>

<div class="breadcrumbs">
	<div class="container">
    	<div class="row">
        <ul class="breadcrumb">
        	<li>当前位置：</li>
        	<li><a href="${ctx}/">首页</a></li>
        	<li>广告悬赏</li>
        </ul>
        </div>
    </div>
</div>

<div class="container">
	<div class="row">
        
        <!-- Main -->
        <div class="">     
            
            <div class="headline">
            	<h4>需求筛选</h4>
            	<%-- 
            	<span class="pull-right" id="more-condition" style="cursor: pointer;">
            		更多筛选条件 <span class="glyphicon glyphicon-chevron-down"></span>
            	</span>
            	--%>
            </div>
            <div class="screening">            	
                <dl class="dl-horizontal">
                	<dt>类别：</dt>
                    <dd>
                    	<a href="javascript:void(0)" class="mediaTypes" data-value="${item.itemCode }">不限</a>
                    <c:forEach items="${mediaTypes }" var="item">
                    	<c:set var="active" value="" />
                    	<c:if test="${item.itemCode == param.mediaType }">
                    		<c:set var="active" value="active" />
                    	</c:if>
                    	<a href="javascript:void(0)" class="${active } mediaTypes" data-value="${item.itemCode }">${item.itemName }</a>
                    </c:forEach>
                    </dd>
                </dl>    	
                <dl class="dl-horizontal">
                    <dt>行业：</dt>
                    <dd> 
                    	<a href="javascript:void(0)" class="industryTypes" data-value="">不限</a>
                    <c:forEach items="${industryTypes }" var="item">                   
                    	<a href="javascript:void(0)" class="industryTypes" data-value="${item.itemCode }">${item.itemName }</a>
                    </c:forEach>
                    </dd>
                </dl>   	
                <dl class="dl-horizontal more ">
                    <dt>预算：</dt>
                    <dd>
                    	<a href="javascript:void(0)" class="budget" data-value="">不限</a>
                        <a href="javascript:void(0)" class="budget" data-value="0,500">0~500</a>
                        <a href="javascript:void(0)" class="budget" data-value="501">500+</a>
                        <a href="javascript:void(0)" class="budget" data-value="2000">2000+</a>
                        <a href="javascript:void(0)" class="budget" data-value="5000">5000+</a>
                        <a href="javascript:void(0)" class="budget" data-value="10000">1万+</a>
                        <a href="javascript:void(0)" class="budget" data-value="50000">5万+</a>
                        <a href="javascript:void(0)" class="budget" data-value="500000">50万+</a>
                    </dd>
                </dl>                                  
            </div>
            
            <div class="filter">
            	<div class="row">
					<div class="col-xs-6 form-inline">
                    	<div class="sort-list">
                        	<div class="form-group form-group-sm">
                            <label class="control-label">排序条件:</label>
                            <select class="form-control sort-select">
                                <option data-value="">全部</option>
                                <option data-value="modifyTime desc">更改时间</option>
                                <option data-value="startTime desc">开始时间</option>
                                <option data-value="endTime desc">结束时间</option>
                                <option data-value="deadline desc">响应截止时间</option>
                                <option data-value="budget desc">预算由高到低</option>
                            </select>
                            </div>
                        </div>
                        <div class="input-group input-group-sm">
                          <input class="form-control" placeholder="在结果中搜索" type="text" id="name" value="${param.name }">
                          <span class="input-group-btn">
                              <c:choose>
				 			  	<c:when test="${empty param.name }">
								  	<button class="btn btn-default" type="button" id="btn-search"><span class="fa fa-search"></span> 搜索</button>
					 			</c:when>    
					 			<c:otherwise>
								  	<button class="btn btn-default" type="button" id="btn-search"><span class="fa fa-remove"></span> 取消</button>
					 			</c:otherwise>
						  	  </c:choose>
                          </span>
                          </div>
                    </div>
                    <div class="col-xs-6 pag">
                    	<small>共&nbsp;<span class="total">0</span>&nbsp;个需求&nbsp;&nbsp;</small>
                    	<small><span class="pageNum">0</span>/<span class="totalPage">0</span>&nbsp;&nbsp;</small>
                        <div class="btn-group">
                            <button type="button" class="btn btn-default btn-sm btn-first"><i class="glyphicon glyphicon-step-backward"></i></button>
                            <button type="button" class="btn btn-default btn-sm btn-previous"><i class="glyphicon glyphicon-triangle-left"></i>上一页</button>
                            <button type="button" class="btn btn-default btn-sm btn-next">下一页<i class="glyphicon glyphicon-triangle-right"></i></button>                        
                            <button type="button" class="btn btn-default btn-sm btn-last"><i class="glyphicon glyphicon-step-forward"></i></button>
                        </div>
                    </div>
                </div>
            </div>
            
			<table width="100%" border="0" class="funny-table table table-hover breakpoint">
            	<thead>
                  <tr>
                    <th scope="col" width="10%" >大类</th>
                    <th scope="col" width="10%" >预算</th>
                    <th scope="col" width="30%" >广告活动发布时间</th>
<!--                     <th scope="col" width="15%" >结束时间</th> -->
                    <th scope="col" width="20%" >名称</th>
                    <th scope="col" width="30%" >响应截止时间</th>
                 </tr>
             	</thead>
                <tbody id="data-list"></tbody>
             </table>
             <!-- <div class="sort">
                <div class="btn-group pull-right">
                	<button type="button" class="btn-u btn-u-sm btn-u-default btn-previous" data-loading-text="加载中.." >上一页</button>
                	<button type="button" class="btn-u btn-u-sm btn-u-default btn-next" data-loading-text="加载中.."  disabled>下一页</button>
                </div>         
                <div class="pull-right">
                	共&nbsp;<span class="total">0</span>&nbsp;个媒体&nbsp;&nbsp;
                	<span class="pageNum">0</span>/<span class="totalPage">0</span>&nbsp;&nbsp;
                </div>         
            </div> -->
            
            <div class="filter no-bg">
            	<div class="row">
                    <div class="col-xs-12 pag">
                    	<small>共&nbsp;<span class="total">0</span>&nbsp;个需求&nbsp;&nbsp;</small>
                    	<small><span class="pageNum">0</span>/<span class="totalPage">0</span>&nbsp;&nbsp;</small>
                        <div class="btn-group">
                            <button type="button" class="btn btn-default btn-sm btn-first"><i class="glyphicon glyphicon-step-backward"></i></button>
                            <button type="button" class="btn btn-default btn-sm btn-previous"><i class="glyphicon glyphicon-triangle-left"></i>上一页</button>
                            <button type="button" class="btn btn-default btn-sm btn-next">下一页<i class="glyphicon glyphicon-triangle-right"></i></button>                        
                            <button type="button" class="btn btn-default btn-sm btn-last"><i class="glyphicon glyphicon-step-forward"></i></button>
                        </div>
                    </div>
                </div>
            </div>
            
            
      </div>
        <!-- End Main -->
       </div>
</div>

<form id="search-form">
	<input type="hidden" name="mediaTypes" value="${param.mediaType }">
	<input type="hidden" name="industryTypes">
	<input type="hidden" name="budget">
	<input type="hidden" name="sort" value="modifyTime desc" >
	<input type="hidden" name="pageNum" value="1">
	<input type="hidden" name="totalPage" value="1">
	<input type="hidden" name="name" value="${param.name }">
</form>
<script type="text/javascript">
	var paramName = '${param.name }';
	if ((typeof paramName) != 'undefined' && paramName != '') {
		$('#btn-search').data().status="search";
	}
</script>
<%-- 需求详情模态框 --%>
<div class="modal fade xiangqing" id="detail-modal" data-width="640"></div>

<%-- 进行抢单对话框 --%>
<div class="modal fade container" id="enlist-modal" data-replace="true" data-backdrop="static"></div>

<%-- 需求列表模板 --%>
<script type="text/template" id="req-temp">
<tr class="table-row">
 	<td>{mediaTypes}</td>
     <td class="color-red h4">{budget}</td>
     <td>{startTime} 至 {endTime}</td>
     <td><a href="#" class="view-detail" data-id="{id}" data-deadline="{deadline}">{name}</a></td>
	 <td>{deadline}
     <i class="fa fa-caret-down"></i>
<shiro:hasRole name="provider">
		<button type="button" class="btn-u btn-u-sm btn-u-red pull-right favorites" style="margin-left:10px" id="btn-favorites{id}" data-id="{id}" data-deadline="{deadline}">收藏</button>
		<button type="button" class="btn-u btn-u-sm btn-u-red pull-right btn-enlist" id="btn-enlist{id}" data-id="{id}" data-deadline="{deadline}">抢单</button>
</shiro:hasRole>
	</td>
</tr>
<tr class="table-row-detail">
 	<td colspan="6">
         <dl class="dl-inline">
           <dt>服务类型：</dt>
           <dd>{serviceTypes}</dd>
           <dt>行业：</dt>
           <dd>{industryTypes}</dd>
           <dt>改稿：</dt>
           <dd>{allowChange}</dd>
         </dl>
         <dl class="dl-inline">
           <dt>需求概述：</dt>
           <dd>{summary} <a href="#" class="view-detail" data-id="{id}" data-deadline="{deadline}">查看详细</a></dd>
         </dl>
     </td>
</tr>
</script>
<script type="text/template" id="req-temp-2">
<tr class="table-row">
 	<td>{mediaTypes}</td>
     <td class="color-red h4">{budget}</td>
     <td>{startTime} 至 {endTime}</td>
     <td><a href="#" class="view-detail" data-id="{id}" data-deadline="{deadline}">{name}</a></td>
	 <td>{deadline}
     <i class="fa fa-caret-down"></i>
<shiro:hasRole name="provider">
		<button type="button" class="btn-u btn-u-sm btn-u-red pull-right {favorites}" style="margin-left:10px" id="btn-favorites{id}" data-id="{id}" data-deadline="{deadline}">取消收藏</button>
		<button type="button" class="btn-u btn-u-sm btn-u-red pull-right btn-enlist" id="btn-enlist{id}" data-id="{id}" data-deadline="{deadline}">抢单</button>
</shiro:hasRole>
	</td>
</tr>
<tr class="table-row-detail">
 	<td colspan="6">
         <dl class="dl-inline">
           <dt>服务类型：</dt>
           <dd>{serviceTypes}</dd>
           <dt>行业：</dt>
           <dd>{industryTypes}</dd>
           <dt>改稿：</dt>
           <dd>{allowChange}</dd>
         </dl>
         <dl class="dl-inline">
           <dt>需求概述：</dt>
           <dd>{summary} <a href="#" class="view-detail" data-id="{id}" data-deadline="{deadline}">查看详细</a></dd>
         </dl>
     </td>
</tr>
</script>
	
<script type="text/javascript">

$(function() {
	var $form = $('#search-form');
	
	load(1);//加载第一页需求列表
	
	//------------------------------------------- 筛选条件 
	
	//高亮筛选条件
	function active(el) {
		$(el).siblings('.active').removeClass('active');
		var value = $(el).data('value');
		if( '' != value ) {
			$(el).addClass('active');
		}
	}
	
	// 设置表单字段值
	function setValue(el, name) {
		var value = $(el).data('value');
		$form.find('[name="' + name + '"]').val(value);
		load(1);
	}
	
	$('.mediaTypes').click(function() {
		active(this);
		setValue(this, 'mediaTypes');
	});
	$('.industryTypes').click(function() {
		active(this);
		setValue(this, 'industryTypes');
	});
	$('.budget').click(function() {
		active(this);
		setValue(this, 'budget');
	});
	$('.sort-select').change(function() {
		var select = $(this).find("option:selected");
		var value = select.data("value");
		if (value != ""){
			$form.find('[name="sort"]').val(value);
		} else {
			$form.find('[name="sort"]').val(null);
		}
		load(1);
	});
	
	
	//---------- 更多筛选条件
	var mFlag = 0;
	$('#more-condition').click(function() {
		if( mFlag == 0) {
			mFlag = 1;
			$(this).html('隐藏筛选条件 <span class="glyphicon glyphicon-chevron-up"></span>');
			$('.more').removeClass('hide').show();
		} else {
			mFlag = 0;
			$(this).html('更多筛选条件 <span class="glyphicon glyphicon-chevron-down"></span>');
			$('.more').hide();
		}
	});
	
	//------------------------------------------- /筛选条件
	
	
	//在结果中搜索
	$('#btn-search').click(function() {
		var searchLabel = '<span class="fa fa-search"></span> 搜索';
		var cancelLabel = '<span class="fa fa-remove"></span> 取消';
		
		var data = $(this).data();
		if( data.status && data.status == 'search' ) {
			data.status = 'cancel';
			$('#name').val('');
			$(this).html(searchLabel);
			
			$form.find('[name="name"]').val('');
			$form.find('[name="pageNum"]').val('1');
			load(1);
		} else {
			var name = $('#name').val();
			if( name.replace(' ', '').length > 0 ) {
				data.status = 'search';
				$(this).html(cancelLabel);
				
				$form.find('[name="name"]').val(name);
				$form.find('[name="pageNum"]').val('1');
				load(1);
			}
		}
	});
	
	
	
	//---------- 加载需求列表
	var $dataList = $('#data-list');
	
	function load(n,toTop) {
		var url = '${ctx}/plaza/load';
		
		$form.find('[name="pageNum"]').val(n);
		$.post(url, $form.serialize(), function(rsp) {
			rsp = $.parseJSON(rsp);
			if( rsp.result == true ) {
				var $temp = $('#req-temp');
				var $temp2 = $('#req-temp-2');
				var html = [];
				$.each(rsp.page.data, function(i, o) {
					if (o.favorites){
						o.favorites = 'cancelFavorites';
						html.push($temp2.format(o));
					} else {
						o.favorites = 'favorites';
						html.push($temp.format(o));
					}
				});
				
				$dataList.html(html.join(''));
				
				setPaging(rsp.page);
				if(toTop){
					$(window).scrollTop(0);	
				}
			} else {
				//TODO 查询失败
			}
		});
	}
	//---------- /加载需求列表
	
	//----- 表格行自动扩展和粘滞
/* 	$dataList.delegate("tr.table-row", "mouseover", function(){
// 		$(this).find('.stick').show();
	    $(this).next('.table-row-detail').show();
	}).delegate("tr.table-row", "mouseout", function(){
		var data = $(this).data();
		if( ! data.stick ) {			
// 	    	$(this).find('.stick').hide();
    		$(this).next('.table-row-detail').hide();
		}
	}).delegate("tr.table-row", "click", function(){
		var data = $(this).data();
		if( data.stick ) {
			data.stick = false;
			$(this).find('.stick').hide();
		} else {
			data.stick = true;
			$(this).find('.stick').show();
		}
	}); */
	
	$dataList.delegate("i.fa", "click", function(){
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
	$dataList.delegate("button.favorites", "click", function(){
		var id = $(this).data("id");
		var that = $(this);
		$.post("${ctx}/member/favoritesReq/add", {reqId:id}, function(rsp) {
			rsp = $.parseJSON(rsp);
			if (rsp.result){
				common.showMessage("收藏成功");
				that.text("取消收藏");
				that.removeClass("favorites");
				that.addClass("cancelFavorites");
			}
		});
	});
	$dataList.delegate("button.cancelFavorites", "click", function(){
		var id = $(this).data("id");
		var that = $(this);
		bootbox.confirm("您确定要取消收藏？",function(result){
			if(result){
				$.post("${ctx}/member/favoritesReq/delete", {reqId:id}, function(rsp) {
					rsp = $.parseJSON(rsp);
					if (rsp.result){
						common.showMessage("取消收藏成功");
						that.text("收藏");
						that.removeClass("cancelFavorites");
						that.addClass("favorites");
					}
				});
			}
		});
	});
	
/* 	$dataList.delegate("tr.table-row-detail", "mouseover", function(){
		$(this).show();
	}).delegate("tr.table-row-detail", "mouseout", function(){
		var data = $(this).prev('.table-row').data();
		if(! data.stick ){
			$(this).hide();
		}
	}).delegate("tr.table-row-detail", "click", function(){
		var data = $(this).prev('.table-row').data();
		if( data.stick ) {
			data.stick = false;
			$(this).prev('.table-row').find('.stick').hide();
		} else {
			data.stick = true;
			$(this).prev('.table-row').find('.stick').show();
		}
	}); */
	//----- /表格行自动扩展和粘滞
	
	
	//----- 查看详情
	
	$dataList.delegate('.view-detail', 'click', function(event) {
		var url = '${ctx}/plaza/detail/' + $(this).data('id') + "?ajax";
		$('#detail-modal').loadModal(url);
		
		event.preventDefault();
		return false;//阻止事件冒泡
	});
	//----- /查看详情
	
	
	//----- 进行抢单
	var $enlistModal = $('#enlist-modal');
	
	//点击列表的“抢单”
	$dataList.delegate('.btn-enlist', 'click', function(event) {
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

		//----- /抢单

		//---------- 分页按钮
		var $btnPrev = $('.btn-previous').click(function() {
			load(getPageNum() - 1,1);
		});
		var $btnNext = $('.btn-next').click(function() {
			load(getPageNum() + 1,1);
		});
		var $btnFirst = $('.btn-first').click(function() {
			load(1,1);
		});
		var $btnFirst = $('.btn-last').click(function() {
			load(getTotalPage(),1);
		});

		function getPageNum() {
			var num = parseInt($form.find('[name="pageNum"]').val());
			num = (num == NaN) ? 1 : num;

			return num;
		}
		function getTotalPage() {
			var num = parseInt($form.find('[name="totalPage"]').val());
			num = (num == NaN) ? 1 : num;

			return num;
		}

		function setPaging(page) {
			var d = 'disabled';

			$('.total').text(page.total);
			$('.pageNum').text(page.pageNum);
			$('.totalPage').text(page.totalPage);
			$form.find('[name="totalPage"]').val(page.totalPage);
			if (page.previous) {
				$('.btn-previous').removeClass(d).removeAttr(d);
			} else {
				$('.btn-previous').addClass(d).attr(d, d);
			}

			if (page.next) {
				$btnNext.removeClass(d).removeAttr(d);
			} else {
				$btnNext.addClass(d).attr(d, d);
			}
		}
		//---------- /分页按钮

	});
</script>
</body>
</html>

