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
      <!-- mTab v2 -->          
      <div class="pad pd15">
          <form id="create-form" class="form-horizontal" action="${ctx}/member/req/advertiser/createStep3" method="get">
	          <input type="hidden" name="hasArticle" value="${param.hasArticle }">
	          <input type="hidden" name="medias" >
	          <input type="hidden" name="prices" >
	          <input type="hidden" name="services" >
	          
              <%@include file="/WEB-INF/views/member/req/advertiser/create-common-fields.jspf" %>
              
              <p><span class="color-red">*</span>媒体推荐方案:<small class="highlight pull-right">注：系统估价根据媒体该服务的报价、过往实际成交价综合计算得出</small></p>
              
              <table width="100%" border="0" class="table table-condensed">                      
                <thead>
                <tr class="thead-yel">
                	<th colspan="11" style="text-align: left">一类媒体 <span class="loading"></span>
                		<button type="button" class="btn-u btn-u-sm btn-u-dark pull-right btn-load"
                			data-level="1"><i class="fa fa-refresh"></i> 换一批</button>
                	</th>
                </tr>
                <tr class="thead-white">
                  <th scope="col">媒体名称</th>
                  <th scope="col">大类</th>
                  <th scope="col">认证类别</th>
                  <th scope="col">行业</th>
                  <th scope="col">地区</th>
                  <th scope="col">粉丝数</th>
                  <th scope="col">粉丝方向</th>
                  <th scope="col">简介及案例</th>
                  <th scope="col">系统估价</th>
                  <th scope="col">选择</th>
                </tr>
                </thead>
                <tbody id="level1">
				
                </tbody>
              </table>
              
              <table width="100%" border="0" class="table table-condensed">                      
                <thead>
                <tr class="thead-yel">
                	<th colspan="11" style="text-align: left">二类媒体 <span class="loading"></span>
                		<button type="button" class="btn-u btn-u-sm btn-u-dark pull-right btn-load" 
                			data-level="2"><i class="fa fa-refresh"></i> 换一批</button></th></tr>
                <tr class="thead-white">
                  <th scope="col">媒体名称</th>
                  <th scope="col">大类</th>
                  <th scope="col">认证类别</th>
                  <th scope="col">行业</th>
                  <th scope="col">地区</th>
                  <th scope="col">粉丝数</th>
                  <th scope="col">粉丝方向</th>
                  <th scope="col">简介及案例</th>
                  <th scope="col">系统估价</th>
                  <th scope="col">选择</th>
                </tr>
                </thead>
                <tbody id="level2">

                </tbody>
              </table>
              
              <table width="100%" border="0" class="table table-condensed">                      
                <thead>
                <tr class="thead-yel">
                	<th colspan="11" style="text-align: left">自媒体 <span class="loading"></span>
                		<button type="button" class="btn-u btn-u-sm btn-u-dark pull-right btn-load"
                			data-level="3"><i class="fa fa-refresh"></i> 换一批</button></th></tr>
                <tr class="thead-white">
                  <th scope="col">媒体名称</th>
                  <th scope="col">大类</th>
                  <th scope="col">认证类别</th>
                  <th scope="col">行业</th>
                  <th scope="col">地区</th>
                  <th scope="col">粉丝数</th>
                  <th scope="col">粉丝方向</th>
                  <th scope="col">简介及案例</th>
                  <th scope="col">系统估价</th>
                  <th scope="col">选择</th>
                </tr>
                </thead>
                <tbody id="level3">

                </tbody>
              </table>
              
              <hr class="hr-md">
              <div class="row">
                  <div class="col-xs-6">
                      <button class="btn-u btn-u-red" type="button" id="btn-self-select" >自行挑选媒体</button>
                  <!-- space -->
                  </div>
                  <div class="col-xs-6 text-right highlight">
                      <p class="price">共选择媒体：<span id="totalMedias" class="mediaCount highlight">0</span> 家，订单共计金额：<strong><small>￥</small><span id="totalAmount">0</span></strong>元
                      <button class="btn-u btn-u-red ml20" type="submit" >下一步</button>
                      </p>
                      </div>
                  </div>

          </form>
      </div><!-- End Pad -->
    </div>
	<!-- End Main -->

<!-- 媒体列表模板 -->
<script type="text/template" id="mediaTemp">
<tr>
  <td class="text-left">{name}</td>
  <td width="120">{mediaType}</td>
  <td width="120">{category}</td>
  <td width="100">{industryTypes}</td>
  <td width="80">{regions}</td>
  <td width="80">{fans}</td>
  <td width="100">{fansDir}</td>
  <td width="100"><a href="{id}">查看</a></td>
  <td width="80">
	<input type="text" class="form-control input-sm text-right" name="price" value="{prices}" maxlength="10"
      onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" />
  </td>
  <td width="80" class="center">
	<input type="checkbox" name="media" value="{id}" onclick="updateTotal()" />
  </td>
</tr>
</script>
                
<script type="text/javascript">

$(function() {
	menu.active('#my-req');
	
	// 点击“换一批”按钮
	$('.btn-load').click(function() {
		var $this = $(this);
		
		var level = $this.data('level');
		var data = $this.data();
		var num = 1, total = 1;
		
		if( data.pageNum ) {
			num = parseInt(data.pageNum) + 1;
		}
		if( data.pageTotal ) {
			total = parseInt(data.pageTotal);
		}
		
		if( total <= 1 )
			return false;
		
		$this.before('<span class="loading"></span>');
		
		num = num > total ? num % total : num;
		eval('loadMedias(' + level + ', ' + num +')');
	});
	
	// 点击“自行挑选媒体”按钮
	$('#btn-self-select').click(function() {
		var form = $('#create-form')[0];
		form.action = '${ctx}/member/req/advertiser/createStep4';
		form.submit();
	});
	
	$('#mediaTypes, #serviceTypes, #regions, #industryTypes').change(function() {
		reloadMedias()
	});
});

//更新总金额和选择的媒体总数
function updateTotal() {
	var $medias = $('[name="media"]:checked');
	var totalMedias = $medias.length;
	var totalAmount = 0;
	$medias.each(function() {
		totalAmount += parseInt($(this).closest('tr').find('[name="price"]').val());
	});
	$('#totalMedias').text(totalMedias);
	$('#totalAmount').text(totalAmount);
}

var $createForm = $('#create-form');

/*
 * 获取查询参数.
 */
function getParams() {
	var params = $createForm.serialize();
	/* params.mediaTypes    = $('#mediaTypes').val();
	params.serviceTypes  = $('#serviceTypes').val();
	params.regions       = $('#regions').val();
	params.industryTypes = $('#industryTypes').val();
	params.budget        = $('#budget').val();
	params.inviteNum     = $('#inviteNum').val(); */
	return params;
}

/*
 * ajax 请求成功后，更新媒体列表的内容.
 */
function flushMedias(group, page) {
	//common.log(page);
	var $temp = $('#mediaTemp');
	var html = [];
	$.each(page.data, function(i, o) {
		html.push($temp.format(o));
	});
	
	var $group = $(group);
	$group.html(html);
	$group.closest('table').find('.loading').remove();
	
	var pageNum = page.pageNum;
	var totalPage = page.totalPage;
	var data = $group.closest('table').find('.btn-load').data();
	data.pageNum = pageNum;
	data.pageTotal = totalPage;
}


/*
 * 分级加载推荐的媒体列表.
 */
function loadMedias(level, pageNum) {
	var url = '${ctx}/member/req/advertiser/loadMedias';
	var params = getParams();
	params += '&level=' + level;
	params += '&pageNum=' + pageNum;
	//common.log(params);
	$.post(url, params, function(data) {
		var page = $.parseJSON(data);
		if( page.result ) {
			flushMedias('#level'+level, page);
		}
	});
}

/*
 * 筛选条件变化后，重新加载媒体列表.
 */
function reloadMedias() {
	loadMedias(1, 1);
	loadMedias(2, 1);
	loadMedias(3, 1);
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
		prices.push($(this).closest('tr').find('[name="price"]').val());
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
	setTimeout(reloadMedias, 1000);
</script>

</body>
</html>
