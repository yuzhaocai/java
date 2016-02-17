<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>
<div class="container">
	<div class="row">
	
        <!-- Main -->
        <div class="">     
            
            <div class="headline">
            	<h4>媒介资源筛选</h4>
            	<%-- 
            	<span class="pull-right" id="more-condition" style="cursor: pointer;">
            		更多筛选条件 <span class="glyphicon glyphicon-chevron-down"></span>
            	</span>
            	--%>
            </div>
            <div class="screening">
                <dl class="dl-horizontal">
                	<dt>认证类别：</dt>
                    <dd>
                    	<a href="javascript:void(0)" class="categories" data-value="">不限</a> 
                    <c:forEach items="${categories }" var="item">
                    	<c:set var="active" value="" />
                    	<c:if test="${item.itemCode == param.category }">
                    		<c:set var="active" value="active" />
                    	</c:if>
                    	<a href="javascript:void(0)" class="${active } categories" data-value="${item.itemCode }">${item.itemName }</a>
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
                	<dt>粉丝数：</dt>
                    <dd>
                    	<a href="javascript:void(0)" class="fans" data-value="">不限</a>
                        <a href="javascript:void(0)" class="fans" data-value="0,5000">0~5千</a>
                        <a href="javascript:void(0)" class="fans" data-value="5001,10000">5千~1万</a>
                        <a href="javascript:void(0)" class="fans" data-value="10001,30000">1万~3万</a>
                        <a href="javascript:void(0)" class="fans" data-value="30001,100000">3万~10万</a>
                        <a href="javascript:void(0)" class="fans" data-value="100001,200000">10万~20万</a>
                        <a href="javascript:void(0)" class="fans" data-value="200001,500000">20万~50万</a>
                        <a href="javascript:void(0)" class="fans" data-value="500001,1000000">50万~100万</a>
                        <a href="javascript:void(0)" class="fans" data-value="1000001,5000000">100万~500万</a>
                        <a href="javascript:void(0)" class="fans" data-value="5000001">500万以上</a>
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
                <dl class="dl-horizontal more ">
                    <dt>标签：</dt>
                    <dd>
                    	<a href="javascript:void(0)" class="tags" data-value="">不限</a>
					<c:forEach items="${tags }" var="item">                   
						<c:set var="active" value="" />
                    	<c:if test="${item.id == param.tags }">
                    		<c:set var="active" value="active" />
                    	</c:if>
                    	<a href="javascript:void(0)" class="${active } tags" data-value="${item.id }">${item.name }</a>
                    </c:forEach>
                    </dd>
                </dl>                                 
            </div>
            
            <%-- <div class="sort" style="padding: 5px 0">
            	
            	<i class="fa fa-sort"></i> 排序
                <div class="btn-group">
                	<button class="btn-u btn-u-sm btn-u-red">销量</button>
                	<button class="btn-u btn-u-sm btn-u-default">上架时间</button>
                	<button class="btn-u btn-u-sm btn-u-default">价格</button>
                	<button class="btn-u btn-u-sm btn-u-default">更新时间</button>
                </div>
            	

          		<div class="col-xs-2 input-group input-group-sm pull-left">
                      <input class="form-control" placeholder="在结果中搜索" type="text" id="name">
                      <span class="input-group-btn">
                          <button class="btn btn-default" type="button" id="btn-search"><span class="fa fa-search"></span> 搜索</button>
                      </span>
                </div>

                <div class="btn-group pull-right">
                	<button type="button" class="btn-u btn-u-sm btn-u-default btn-previous" data-loading-text="加载中.." disabled>上一页</button>
                	<button type="button" class="btn-u btn-u-sm btn-u-default btn-next" data-loading-text="加载中.." disabled>下一页</button>
                </div>         
                <div class="pull-right">
                	共&nbsp;<span class="total">0</span>&nbsp;个媒体&nbsp;&nbsp;
                	<span class="pageNum">0</span>/<span class="totalPage">0</span>&nbsp;&nbsp;
                </div>         
            </div> --%>
            
            <div class="filter">
            	<div class="row">
                    <div class="col-xs-6 form-inline">
						<div class="sort-list">
                        	<div class="form-group form-group-sm">
                            <label class="control-label">排序条件:</label>
                            <select class="form-control sort-select">
                                <option data-value="">全部</option>
                                <option data-value="modifyTime desc" >更改时间</option>
                                <option data-value="createTime desc" >创建时间</option>
                                <option data-value="fans desc" >粉丝数由高到低</option>
                            </select>
                            </div>
                        </div>
						<div class="input-group input-group-sm">
						  	<input class="form-control" placeholder="在结果中搜索" type="text" id="name" value="${param.name }">
						 	<span class="input-group-btn">
						 		<c:choose >
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
                    	<small>共&nbsp;<span class="total">0</span>&nbsp;个媒体&nbsp;&nbsp;</small>
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
            
            <ul id="medias" class="prod-list clearfix" style="height:auto; min-height:400px">
				<span class="loading"></span>    
            </ul>
            <div class="filter no-bg">
            	<div class="row">
                    <div class="col-xs-12 pag">
                    	<small>共&nbsp;<span class="total">0</span>&nbsp;个媒体&nbsp;&nbsp;</small>
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

<!-- 媒体模板 -->
<script type="text/template" id="mediaTemp">
	<li>
	    <div class="thumbnail thumbnail-display {stick}">
			<div class="photo-wrap">
		        <a href="${ctx}/${param.prefix}/detail/{id}">
		            <img src="{showPic}" class="photo" alt="{name}">
		            <span class="photo-mask"></span>
		        </a>
				<button type="button" onclick="location='${ctx}/${param.prefix}/detail/{id}#e'">查看报价</button>
			</div>
	        <div class="thumbnail-block caption">
	            <h1 class="thumbnail-title"><a href="${ctx}/${param.prefix}/detail/{id}">{name}</a></h1> 
	            <ul class="list-unstyled thumbnail-text">                                   
	                <li>类别：<span>{category}</span></li>
	                <li>粉丝：<span>{fans}</span></li>
	                <li>行业：<p class="trades">
						<span>{industryType1}</span>
						<span>{industryType2}</span>
					</p>
					</li>
	            </ul>
	        </div>
	        <div class="thumbnail-block">
	        	<hr>
	            <p class="thumbnail-text">简介：<span>{description}</span><a href="${ctx}/${param.prefix}/detail/{id}#a">[详情]</a></p>
	        </div>
	    </div> 
		<div class="thumbnail card hide">
			<div class="thumbnail-block">
				<h1 class="thumbnail-title"><a href="${ctx}/${param.prefix}/detail/{id}">{name}</a></h1>
				<hr class="hr-xs">
				<ul class="list-unstyled thumbnail-text">
					<li><i>类</i>类别：<span>{category}</span></li>
					<li><i>粉</i>粉丝：<span>{fans}</span></li>
					<li><i>行</i>行业：<span>{industryTypes}</span></li>
				</ul>
			</div>
		</div>
	</li>
</script>
 
<form id="search-form" action="${ctx}/${param.prefix}/load">
	<input type="hidden" name="category" value="${param.category }">
	<input type="hidden" name="industryTypes">
	<input type="hidden" name="fans">
	<input type="hidden" name="regions" value="${param.regions }">
	<input type="hidden" name="tags" value="${param.tags }">
	<input type="hidden" name="sort" value="">
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
<script type="text/javascript">

$(function() {
	//---------- 高亮筛选条件
	function active(el) {
		$(el).siblings('.active').removeClass('active');
		var value = $(el).data('value');
		if( '' != value ) {
			$(el).addClass('active');
		}
	}
	
	var $form = $('#search-form');
	function setValue(el, name) {
		var value = $(el).data('value');
		$form.find('[name="' + name + '"]').val(value);
		loadMedias(1);
	}
	
	$('.categories').click(function() {
		active(this);
		setValue(this, 'category');
	});
	$('.industryTypes').click(function() {
		active(this);
		setValue(this, 'industryTypes');
	});
	$('.fans').click(function() {
		active(this);
		setValue(this, 'fans');
	});
	$('#regions').delegate('.regions', 'click', function() {
		active(this);
		setValue(this, 'regions');
	});
	$('.tags').click(function() {
		active(this);
		setValue(this, 'tags');
	});
	//高亮筛选条件
	function activeSort(el) {
		if ($(el).hasClass('active')){
			var sort = $(el).data('sort');
			if ("desc"==sort){
				$(el).data('sort', 'asc');
				$(el).find("i").removeClass("glyphicon-sort-by-attributes-alt");
				$(el).find("i").addClass("glyphicon-sort-by-attributes");
			} else {
				$(el).data('sort', 'desc');
				$(el).find("i").removeClass("glyphicon-sort-by-attributes");
				$(el).find("i").addClass("glyphicon-sort-by-attributes-alt");
			}
		} else {
			$(el).siblings('.active').removeClass('active');
			var value = $(el).data('value');
			if( '' != value ) {
				$(el).addClass('active');
			}
		}
	}
	
	$('.sort-select').change(function() {
		var select = $(this).find("option:selected");
		var value = select.data("value");
		if (value != ""){
			$form.find('[name="sort"]').val(value);
		} else {
			$form.find('[name="sort"]').val(null);
		}
		loadMedias(1);
	});
	
	//---------- /高亮筛选条件
	
	$('.moreRegions').radioRegionModal();
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
	//---------- /更多筛选条件
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
			loadMedias(1);
		} else {
			var name = $('#name').val();
			if( name.replace(' ', '').length > 0 ) {
				data.status = 'search';
				$(this).html(cancelLabel);
				
				$form.find('[name="name"]').val(name);
				$form.find('[name="pageNum"]').val('1');
				loadMedias(1);
			}
		}
	});
	
	//---------- 加载媒体列表
	var $medias = $('#medias');
	function loadMedias(n,toTop) {
		var url = '${ctx}/${param.prefix}/load';
		$form.find('[name="pageNum"]').val(n);
		$.post(url, $form.serialize(), function(rsp) { 
			//common.log(rsp);
			rsp = $.parseJSON(rsp);
			if( rsp.result == true ) {
				var $temp = $('#mediaTemp');
				var html = [''];
				$.each(rsp.page.data, function(i, o) {
					if (o.cooperate){
						o.stick = 'stick';
					}
					html.push($temp.format(o));
				});
				$medias.html(html.join(''));
				setPaging(rsp.page)
				if(toTop){
					$(window).scrollTop(0);	
				}
				$('.prod-list > li >.thumbnail-display').each(function(index ,element) {
					var element = $(element);
					var id = element.attr('id');
					var txt = $(this).next().prop("outerHTML").replace("hide", "");
					var direction;
					if ((index + 1) % 4 == 0) {
						direction = 'left';
					} else {
						direction = 'right';
					}
					element.popover({
					  animation:'false',
					  trigger: 'manual',
					  placement: direction, //top, bottom, left or right
					  title: '',
					  html: 'true',
					  content: txt,
					}).on("mouseenter", function () {
					  var _this = this;
					  $(this).popover("show");
					  $(this).siblings(".popover").on("mouseleave", function () {
						$(_this).popover('hide');
					  });
					  			 
					}).on("mouseleave", function () {
					  var _this = this;
					  setTimeout(function () {
						if (!$(".popover:hover").length) {
						  $(_this).popover("hide")
						}
					  });
					});
					
				 });
			} else {
				//TODO
			}
		});
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
	loadMedias(1);
	//---------- /加载媒体列表
	
	function getTotalPage() {
		var num = parseInt($form.find('[name="totalPage"]').val());
		num = (num == NaN) ? 1 : num;

		return num;
	}
	function getPageNum() {
		var num = parseInt($form.find('[name="pageNum"]').val());
		num = (num == NaN) ? 1 : num;

		return num;
	}
	var $btnPrev = $('.btn-previous').click(function() {
		loadMedias(getPageNum() - 1,1);
	});
	var $btnNext = $('.btn-next').click(function() {
		loadMedias(getPageNum() + 1,1);
	});
	var $btnFirst = $('.btn-first').click(function() {
		loadMedias(1,1);
	});
	var $btnFirst = $('.btn-last').click(function() {
		loadMedias(getTotalPage(),1);
	});
	/*
	 * 初始化按钮的loading功能，点击后将显示Loading字样，并且按钮被disabled掉，无法连续点击，防止二次提交
	 * 超过2秒后按钮将恢复原状
	 */
	/* $('button[data-loading-text]').click(function () {
	    var btn = $(this).button('loading');
	    setTimeout(function () {
	        btn.button('reset');
	    }, 2000);
	});	 */
});

function addInviteMedia(id,name,imgSrc,category,industryTypes,fans,prices){
	var param = {"id":id,"name":name,"imgSrc":imgSrc,"category":category,"industryTypes":industryTypes,"fans":fans,'prices':prices};
	genCartHtml('${ctx}/shoppingcart/addMedias',param);
}

function delMediafromCart(id){
	genCartHtml('${ctx}/shoppingcart/delMediaFromCart',{"id":id});
}
/**
 * 显示购物车中媒体信息
 */
function genCartHtml(url,param){
	$('.basket').children().not('.imgtxt-list').remove();
	var cartLi = [""];
	var $temp = $('#cartTemp');
	$.post(url,param,function(result){
		var medias = eval(result['medias']);
		$.each(medias, function(i, o) {
			cartLi.push($temp.format(o));
		});
		$('#inviteMediasNum').text(medias.length);
	$('.imgtxt-list').html(cartLi.join(''));
	if(medias.length>0){
		$('.imgtxt-list').after('<p class="footer"><button type="button" class="btn-u btn-u-hitcolor" onclick="javascript:creatReq();"><i class="fa fa-file-text-o"></i>创建需求</button></p>');
	}else{
		$('.imgtxt-list').after('<p class="footer">请添加欲邀约媒体</p>');
	}
		
		if(medias.length==0){
			common.showMessage('购物车已清空');
		}else{
			common.showMessage(result['msg']);
		}
	},'json');
}

function creatReq(){
	window.location.href='${ctx}/shoppingcart/createReqToStep3';
}
</script>
