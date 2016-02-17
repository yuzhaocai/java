<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>采媒在线</title>
</head>

<body>

<div class="breadcrumbs">
	<div class="container">
    	<div class="row">
        <ul class="breadcrumb">
        	<li>当前位置：</li>
        	<li><a href="${ctx}/">首页</a></li>
        	<li>更多媒体</li>
        </ul>
        </div>
    </div>
</div>

<div class="container">
    <div class="row">
        <div class="left-panel">
            <div class="panel panel-default">
                <div class="panel-heading h4">
					<strong>更多媒体</strong>
				</div>
                <div id="leftBar" class="list-group">
	                <c:forEach items="${categories }" var="item">
	                	<a class="list-group-item <c:if test="${item.itemCode eq param.search_EQ_category }">active</c:if>" href="${ctx }/other?search_EQ_category=${item.itemCode}" > ${item.itemName}</a>
	                </c:forEach> 
                </div>
            </div> 
        </div>
        <div class="right-panel">
           	<div class="headline">
            	<h4>${categoryName }资源筛选</h4>
            </div>
            <div class="screening">
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
                    <dt>地区：</dt>
                    <dd id="regions">
                    	<a href="javascript:void(0)" class="regions" data-value="">全国</a>
	                    <c:forEach items="${regions }" var="item">
	                    	<c:set var="active" value="" />
	                    	<c:if test="${item.id == param.regions }"><c:set var="active" value="active" /></c:if>
	                        <a href="javascript:void(0)" class="${active} regions" data-value="${item.id }">${item.name }</a>
	                    </c:forEach>
	                   <div class="moreRegions">
                       </div> 
                    </dd>
                </dl>    	
            </div>
            <div class="filter clearfix">
            	<div class="row">
                    <div class="col-xs-6 form-inline">
						<div class="sort-list">
                        	<div class="form-group form-group-sm">
                            <label class="control-label">排序条件:</label>
                            <select class="form-control sort-select">
                                <option data-value="">全部</option>
                                <option data-value="DESC_modifyTime">更改时间</option>
                                <option data-value="DESC_createTime">创建时间</option>
                            </select>
                            </div>
                        </div>
						<div class="input-group input-group-sm">
						  	<input type="text" value="" id="name" placeholder="在结果中搜索" class="form-control">
						 	<span class="input-group-btn">
							  	<button id="btn-search" type="button" class="btn btn-default"><span class="fa fa-search"></span> 搜索</button>
						  	</span>
						</div>
					</div>
                    <div class="col-xs-6 pag">
                    	<small>共&nbsp;<span class="total">${total }</span>&nbsp;个媒体&nbsp;&nbsp;</small>
                    	<small><span class="pageNum">${pageNum }</span>/<span class="totalPage">${totalPage }</span>&nbsp;&nbsp;</small>
                        <div class="btn-group">
                            <button class="btn btn-default btn-sm btn-first" type="button"><i class="glyphicon glyphicon-step-backward"></i></button>
                            <button class="btn btn-default btn-sm btn-previous" type="button" <c:if test="${!previous }">disabled="disabled"</c:if>><i class="glyphicon glyphicon-triangle-left"></i>上一页</button>
                            <button class="btn btn-default btn-sm btn-next" type="button" <c:if test="${!next }">disabled="disabled"</c:if>>下一页<i class="glyphicon glyphicon-triangle-right"></i></button>                        
                            <button class="btn btn-default btn-sm btn-last" type="button"><i class="glyphicon glyphicon-step-forward"></i></button>
                        </div>
                    </div>
                </div>
            </div>
            
            <ul class="prod-list-v2 clearfix" id="otherMedias">
            	<c:forEach items="${data.content}" var="item">
				<li>
					<div class="thumbnail">
						<a target="_blank" href="other/view/${item.id }">
							<img src="${item.showPic }">
						</a>
						<div class="caption text-center">
							<h3>
								<a target="_blank" href="other/view/${item.id }">${item.name }</a>
							</h3>
						</div>
					</div>
				</li>
				</c:forEach>
			</ul>
           	<div class="filter no-bg">
            	<div class="row">
                    <div class="col-xs-12 pag">
                    	<small>共&nbsp;<span class="total">${total }</span>&nbsp;个媒体&nbsp;&nbsp;</small>
                    	<small><span class="pageNum">${pageNum }</span>/<span class="totalPage">${totalPage }</span>&nbsp;&nbsp;</small>
                        <div class="btn-group">
                            <button type="button" class="btn btn-default btn-sm btn-first"><i class="glyphicon glyphicon-step-backward"></i></button>
                            <button type="button" class="btn btn-default btn-sm btn-previous" <c:if test="${!previous }">disabled="disabled"</c:if>><i class="glyphicon glyphicon-triangle-left"></i>上一页</button>
                            <button type="button" class="btn btn-default btn-sm btn-next" <c:if test="${!next }">disabled="disabled"</c:if>>下一页<i class="glyphicon glyphicon-triangle-right"></i></button>                        
                            <button type="button" class="btn btn-default btn-sm btn-last"><i class="glyphicon glyphicon-step-forward"></i></button>
                        </div>
                    </div>
                </div>
            </div>
            
        </div>
    </div>
</div>
<!-- 返回顶部按钮 -->
<div id="topcontrol" style="position: fixed; bottom: 5px; right: 5px; opacity: 0; cursor: pointer;" title="Scroll Back to Top"></div>  
<!--=== End Content ===-->
<!-- 返回顶部 -->
<script src="${ctx}/static/assets/plugins/back-to-top.js" type="text/javascript"></script>
<form id="search-form">
	<input type="hidden" name="category" value="${param.search_EQ_category }">
	<input type="hidden" name="industryType">
	<input type="hidden" name="regions">
	<input type="hidden" name="sort" value="">
	<input type="hidden" name="pageNum" value="1">
	<input type="hidden" name="totalPage" value="${totalPage }">
	<input type="hidden" name="name">
</form>
<script type="text/template" id="mediaTemp">
<li>
    <div class="thumbnail">
        <a target="_blank" href="other/view/{id}">
            <img src="{showPic}">
        </a>
        <div class="caption text-center">
            <h3>
                <a target="_blank" href="other/view/{id}">{name}</a>
            </h3>
        </div>
    </div>
</li>
</script>

<script type="text/javascript">
	var $form = $('#search-form');
	var $medias = $('#otherMedias');
	$(function(){
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
				loadOtherMedias(1);
			} else {
				var name = $('#name').val();
				if( name.replace(' ', '').length > 0 ) {
					data.status = 'search';
					$(this).html(cancelLabel);
					
					$form.find('[name="name"]').val(name);
					$form.find('[name="pageNum"]').val('1');
					loadOtherMedias(1);
				}
			}
		});
		
		//选中查询条件添加样式
		function active(el) {
			$(el).siblings('.active').removeClass('active');
			var value = $(el).data('value');
			if( '' != value ) {
				$(el).addClass('active');
			}
		}
		
		//form中添加值
		function setValue(el, name) {
			var value = $(el).data('value');
			$form.find('[name="' + name + '"]').val(value);
			loadOtherMedias(1);
		}
		//地区更多按钮
		$('.moreRegions').radioRegionModal();
		
		//点击行业查询条件
		$('.industryTypes').click(function() {
			active(this);
			setValue(this, 'industryType');
		});
		//点击地区查询条件
		$('#regions').delegate('.regions', 'click', function() {
			active(this);
			setValue(this, 'regions');
		});
		//点击排序查询条件
		$('.sort-select').change(function() {
			var select = $(this).find("option:selected");
			var value = select.data("value");
			if (value != ""){
				$form.find('[name="sort"]').val(value);
			} else {
				$form.find('[name="sort"]').val(null);
			}
			loadOtherMedias(1);
		});
	})
	function loadOtherMedias(n,toTop) {
		var url = '${ctx}/other/load';
		$form.find('[name="pageNum"]').val(n);
		$.post(url, $form.serialize(), function(rsp) { 
			rsp = $.parseJSON(rsp);
			if( rsp.result == true ) {
				var $temp = $('#mediaTemp');
				var html = [''];
				$.each(rsp.page.data, function(i, o) {
					html.push($temp.format(o));
				});
				$medias.html(html.join(''));
				setPaging(rsp.page)
				if(toTop){
					$(window).scrollTop(0);
				}
			} else {
				//TODO
			}
		});
	}
	//总页数
	function getTotalPage() {
		var num = parseInt($form.find('[name="totalPage"]').val());
		num = (num == NaN) ? 1 : num;

		return num;
	}
	//当前页数
	function getPageNum() {
		var num = parseInt($form.find('[name="pageNum"]').val());
		num = (num == NaN) ? 1 : num;

		return num;
	}
	var $btnPrev = $('.btn-previous').click(function() {
		loadOtherMedias(getPageNum() - 1,1);
	});
	var $btnNext = $('.btn-next').click(function() {
		loadOtherMedias(getPageNum() + 1,1);
	});
	var $btnFirst = $('.btn-first').click(function() {
		loadOtherMedias(1,1);
	});
	var $btnFirst = $('.btn-last').click(function() {
		loadOtherMedias(getTotalPage(),1);
	});
	
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

</script>

</body>
</html>

