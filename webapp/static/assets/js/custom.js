$(function() {	

	App.init();
	
	//Activate a tab or pill navigation by mouseover
	/*$('.nav-tabs a, .nav-pills a').mouseover(function (e) {		
		e.preventDefault()
		$(this).tab('show')
	})*/
	
	$('.tab-v4 .nav-tabs a,.floor .nav-pills a').mouseover(function (e) {		
		e.preventDefault()
		$(this).tab('show')
	})
	$('.nav-tabs a').click(function (e) {	
		//修改a标签不跳转页面问题
//		e.preventDefault()
		$(this).tab('show')
	})
	
	//产品详情页-产品规格导航
	/*
	$("#prod-detail-nav").affix({
		offset: {
            top: function(){
				return (this.top = $('.prod-page .main-wrap .prod-detail-nav').offset().top)
				//$('.prod-page .main-wrap .prod-detail-nav').offset().top;
			}
     	}
    });
	$("body").scrollspy({ target: '#prod-detail-nav' })//产品规格导航激活滚动监听
	*/
	
	
	
	

	//左侧导航默认打开
	$('.sidebar-menu .collapse').collapse('show');

	//顶部菜单
	$('.topbar-v1 ul > li').hover(
		function(){
			$(this).children('.dropdown-menu').slideDown();
		},
		function(){
			$(this).children('.dropdown-menu').hide();
	});
		
	/*-- 首页全部分类 --*/
	//显示隐藏
	$('.cate-list > li').hover(function(){
		$(this).addClass('current');
		
	},function(){
		$(this).removeClass('current');	
	});

	//控制全部分类高度	
	var $cateAll = $('.cate-list > li > .cate-item > .all'),
		$cateWrap_h = $('.prime .cate-wrap').height();
		//$cateAll.outerHeight($cateWrap_h);//设置首屏左侧子导航高度为父导航总高度
	
	/*-- 其它页全部分类 --*/	
	$('.m-category').hover(function(){
		$(this).children('.category').show();
		//控制全部分类高度
		var $cateList_h = $('.cate-list').height(),
			$cateAll = $('.cate-list > li > .cate-item > .all');
			//$cateAll.outerHeight($cateList_h);//设置首屏左侧子导航高度为父导航总高度
		
	},function(){
		$(this).children('.category').hide();		
	});
	
	//控制热门搜索关键词		
	//前台头部搜索
	var  $hotwords = $('.hotwords > li > a');
	     $formControl = $('.h-search-block input');
		 //$hotwordsList = $('.hotwords_list > li > a');
		 $hotwords.click(function() {
           var $text=$(this).text();
		   $formControl.val($text);		   
         });  
		 
		$('.h-search-block .form-control').focus(function(e) {
			$(this).parents('.h-search-block').addClass('h-search-focus');
			//$(this).parent('.input-group').siblings('.hotwords_list').show().css('opacity','1');
		}).blur(function(e) {			
			$(this).parents('.h-search-block').removeClass('h-search-focus');	
			//$(this).parent('.input-group').siblings('.hotwords_list').css('opacity','0');
		 /*
			 $hotwordsList.click(function(e) {
				   var $text=$(this).text();
				   $formControl.val($text);	
				   $(this).parent().parent('.hotwords_list').hide(); 
			}); 
		*/			
			    
		});
		
	
	//首屏Metro模块翻页隐藏显示
	$('.prime .carousel').hover(function(){
		$('.carousel-indicators, .carousel-control').fadeIn();			
	},function(){
		$('.carousel-indicators, .carousel-control').fadeOut();			
	});
	
	
	//注册页
	/*
	$('.funny-radio > label > input[type=radio]').on('focus',function(){	//<input>元素获得焦点	
			
		$(this).siblings('span').addClass('checked')	//为同辈元素<span>添加选中效果
			.parent('label').siblings('label').children('span').removeClass('checked');	 //移除funny-radio下其他<span>的选中效果
	});
	*/
	
	//选择合作媒体类别
	$('#seletedMediaType label > [name=mediatype]:radio').click(function(){
		if(this.checked){
			$(this).siblings('span').addClass('checked');
			$(this).parents('.col-xs-2').siblings().children('label').children('input').prop('checked',false);
			$(this).parents('.col-xs-2').siblings().children('label').children('span').removeClass('checked');
		}		
	});
	
	//右侧浮动工具 QQ 投诉/建议
//	var $complaint = $('<a id="complaintControl" data-toggle="modal" data-target="#modal-complaint">'+'</a>'+'<a id="qqControl">'+'</a>');
//	$('body').append($complaint);
	
	//弹出投诉建议模态框
	$('#complaintControl').click(function(){
	    $.get(ctx + '/suggestion/suggestionModal?ajax', function(data) {
	    	$('#modal-complaint').remove();//从DOM删除已有的对话框
	        $('body').append(data);
	        common.showModal('#modal-complaint', false);
	    });	
	    return false;	
	});
	
	$('body').delegate("#qqControl", "mouseover", function(){
		$('#qqWpa').show();
	}).delegate("#qqControl", "mouseout", function(){
		$('#qqWpa').hide();
	});
	
	$('body').delegate("#qqWpa", "mouseover", function(){
		$(this).show();
		$('#qqControl').addClass('qqControlshow');
	}).delegate("#qqWpa", "mouseout", function(){
		$(this).hide();
		$('#qqControl').removeClass('qqControlshow');
	});
	
	
	//用户信息下修改手机号
	$('#changeNumb').click(function(e) {        
		$('#getCaptcha').removeClass('hide');
    });
	
	//选择媒体	
	$('.float-checkbox').on('click',function(){
		var prodCheckbox=$(this).children("input[name='checkbox']");
		if(prodCheckbox.attr("checked"))
		{
    		prodCheckbox.removeAttr("checked");
			$(this).parents('.thumbnail').removeClass('selected');
		}
		else
	   {
			prodCheckbox.attr("checked",'true');
			$(this).parents('.thumbnail').addClass('selected');
		
	   }
	   //计算选中产品数量
		var countChecked = function() {
			var count = $("input[name='checkbox']:checked" ).length;
			$(".mediaCount").text(count);
			//$(".mediaCount").text("已选择 " + count + " 家媒体" );
		};
		countChecked();		 
		$( "input[name='checkbox']" ).on( "click", countChecked);

	});
	
	
	
	//用户中心滚动监听
	var dh= $('.c-body .panel-heading[data-spy="affix"]').parent().width();
	$('.c-body .panel-heading[data-spy="affix"]').css({
		'top':'0',
		'z-index':'1000'
	}).outerWidth(dh);
	
	
	
	
});

	$(function () {
	  $('[data-toggle="tooltip"]').tooltip();
	});

//收藏本站
	function AddFavorite(sURL, sTitle)
	{
		try
		{
			window.external.addFavorite(sURL, sTitle);
		}
		catch (e)
		{
			try
			{
				window.sidebar.addPanel(sTitle, sURL, "");
			}
			catch (e)
			{
				alert("加入收藏失败，请使用Ctrl+D进行添加");
			}
		}
}



var winh = $(window).height();
	doch = $(document).height();
	if(doch<=winh){
		$(".footer-v1").css({
			'position':'fixed',
			'bottom':'0',
			'width':'100%'
			
			});
	}
	else {
		$(".footer-v1").css({
			'position':'relative'			
			});			
	}

	
	
