<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
<%-- 
	<meta name="decorator" content="plain">
--%>
    <title>创建需求</title>
    
</head>

<body>
        
  <!-- Main -->
  <div class="main-panel">
      <div class="headline">
      	<h5>创建新需求</h5>
      	<button type="button" class="btn-u btn-u-sm btn-u-dark pull-right" 
      		onclick="location.replace(document.referrer);"><i class="fa fa-angle-left"></i> 返回</button>
      </div>
      <!-- pad -->         
      <div class="pad">
          <div class="topwrap">
	          <%-- 显示后台验证错误的标签 --%>
			  <tags:fieldErrors commandName="req" />
			  
	          <form id="create-form" class="form-horizontal" action="${ctx}/member/req/advertiser/createStep3" method="post">
		          <input type="hidden" name="hasArticle" value="${req.hasArticle }">
		          <input type="hidden" name="medias" >
		          <input type="hidden" name="prices" >
		          <input type="hidden" name="services" >
		          
	              <%@include file="/WEB-INF/views/member/req/advertiser/create-common-fields.jspf" %>
	                
	          </form>
          </div>
      
          <!-- 选定接单媒体 -->
          <div class="panel panel-default select-media">
              <div class="panel-heading" data-spy="affix" data-offset-top="480">
              	<span class="color-red">*</span>选择接单媒体(<span class="mediaCount highlight">0</span>)
              </div>
              <div class="panel-body media-group"> 
 
              	<ul id="media-list" class="prod-list list-group"></ul>                           

                <div class="text-center" id="load-more" style="display:none"><button type="button" class="btn-u btn-u-lg btn-u-default" id="btn-more">加载更多</button></div>

                <hr class="hr-md">
                <div class="row">
                 <div class="col-xs-6">
                 <!-- space -->
                 </div>
                 <div class="col-xs-6 text-right highlight">
                     <p class="price">已选择 <span class="mediaCount highlight">0</span> 家媒体，订单金额：<strong><small>￥</small><span class="totalAmount">0</span></strong>
                     <button class="btn-u btn-u-red ml20" type="button" id="btn-submit">下一步</button>
                     </p>
                     </div>
                 </div>
                </div> 
              </div><!-- end panel-body --> 
           </div><!-- end panel -->

      </div><!-- End Tab v2 -->
  </div>
  <!-- End Main -->

<!-- 媒体模板 -->
<script type="text/template" id="mediaTemp">
<li>
  	<div class="thumbnail-v2">
          <div class="toparea">
              <div class="photo-wrap">
                  <img src="{showPic}" class="photo">
                  <span class="photo-mask"></span>
              </div>
              <div class="info-wrap">
                  <ul class="ul-detail">
                      <li class="tt"><a href="#">{name}</a></li>                                    
                      <li><p>类别：<span>{category}</span></p><p><a href="#" target="_blank" class="highlight">查看报价</a></p></li>
                      <li>
                      	<p>粉丝：<span>{fans}</span></p>
                      	<p>行业：<span>{industryTypes}</span></p>
                      </li>
                  </ul>
              </div>
          </div>                                    
          <div class="innerarea">
              <ul class="ul-detail">
                  <li class="jj">简介：<span>{description}</span></li>
              </ul>
          </div>
          <div class="float-floor"></div>
          <div class="float-checkbox">
			<i class="fa fa-check"></i><input type="checkbox" name="media" value="{id}">
			<input type="hidden" name="price" value="{prices}">
		  </div>
      </div>
</li>
</script>

<script type="text/javascript">

$(function() {
	menu.active('#my-req');
	
	$('#btn-more').click(function() {
		var data = $(this).data();
		var pageNum = 1;
		if( data.pageNum ) {
			pageNum = parseInt(data.pageNum) + 1;
		}
		queryMedias(pageNum);
	});
	
	$('#btn-submit').click(function() {
		$('#create-form').submit();
	})
	
	$('#mediaTypes, #serviceTypes, #regions, #industryTypes').change(function() {
		queryMedias(1);
	});
});

/* 绑定选择媒体事件 */
function bindSelectMedia() {

	$('.float-checkbox').off('click');
	
	$('.float-checkbox').on('click',function() {
		var checkbox=$(this).find("input[name='media']")[0];
		if( checkbox.checked ) {
			checkbox.checked = false;
			$(this).parents('.thumbnail-v2').removeClass('selected');
		} else{
			checkbox.checked = true;
			$(this).parents('.thumbnail-v2').addClass('selected');
	   }
	   updateTotal();
	});
}

/* 更新选择的媒体数量和订单金额 */
function updateTotal() {
	var $medias = $('.float-checkbox [name="media"]:checked');
	var totalMedias = $medias.length;
	var totalAmount = 0;
	$medias.each(function() {
		totalAmount += parseInt($(this).siblings('[name="price"]').val());
	});
	$('.mediaCount').text(totalMedias);
	$('.totalAmount').text(totalAmount);
};

var $createForm = $('#create-form');

/*
 * 获取查询参数.
 */
function getParams() {
	var params = $createForm.serialize();
	return params;
};

/* 查询媒体 */
function queryMedias(pageNum) {
	
	var $head = $('.panel-heading');
	$head.append('<span class="loading"></span>');
	
	var params = getParams();
	params += '&pageNum=' + pageNum;
	
	$.post('${ctx}/member/req/advertiser/queryMedias', params, function(data) {
		var page = $.parseJSON(data);
		if( page.result ) {
			var $temp = $('#mediaTemp');
			var html = [];
			$.each(page.data, function(i, o) {
				html.push($temp.format(o));
			});
			
			$('#media-list').append(html.join(''));
			
			bindSelectMedia();
			
			$head.find('.loading').remove();
			
			if( page.pageNum < page.totalPage ) {
				showLoadMore(page);
			}
		}
	});
	
};

/* 显示加载更多按钮 */
function showLoadMore(page) {
	
	var data = $('#btn-more').data();
	data.pageTotal = page.totalPage;
	data.pageNum = page.pageNum;
	
	$('#load-more').show();
}

//-------------------------------------------------------- 校验表单
/*
 * 提交表单时, 组装接单媒体信息.
 */
function setMediaPrice(form) {
	var medias = [];
	var prices = [];
	var services = [];
	
	var serviceTypes = $('#serviceTypes').val();
	var $medias = $('[name="media"]:checked');
	$medias.each(function() {
		medias.push(this.value);
		prices.push($(this).siblings('[name="price"]').val());
		services.push(serviceTypes);
	});
	
	form.medias.value = medias.join(',');
	form.prices.value = prices.join(',');
	form.services.value = services.join(',');
}

$.validator.setDefaults({ ignore: ":hidden:not(select)" }); //解决 chosen 插件冲突的问题

$createForm.validate({
	debug: false,
	submitHandler: function(form) {
		if( $('[name="media"]:checked').length == 0 ) {
			common.showMessage('您还没有选择接单媒体!', 'warn');
			return false;
		}
		
		setMediaPrice(form);
		
		//common.disabled('#btn-next');
		
		form.submit(); 
	}, 
	rules: {
		budget: {
			required:true,
			digits:true
		},
		inviteNum: {
			required:true,
			digits:true
		},
		mediaTypes: {
			required: true
		},
		serviceTypes: {
			required: true
		},
		industryTypes: {
			required:true
		},
		regions: {
			required:true
		}
	},
	messages: {
		budget: {
			required:'请输入本次需求的预算',
			digits: '只能输入整数'
		},
		inviteNum: {
			required:'请输入拟邀的媒体数',
			digits:'只能输入整数'
		},
		mediaTypes: {
			required:'请选择媒体类型'
		},
		serviceTypes: {
			required: '请选择服务类别'
		},
		industryTypes: {
			required:'请选择行业类型'
		},
		regions: {
			required:'请选择地区'
		}
	}
});
//- /校验表单	
</script>

<%@include file="/WEB-INF/views/member/req/advertiser/create-common-script.jspf" %>

<script type="text/javascript">
	setTimeout(function(){queryMedias(1)}, 1000);
</script>

</body>
</html>
