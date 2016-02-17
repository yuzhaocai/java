<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>新手引导页</title>
</head>

<body>

<div class="">
	<div class="article-box newUser">
        <ul class="nav nav-tabs">
        	<li class="active" >
        		<a id="ad" href="#广告主" data-toggle="tab"  onclick="tabFn(this)">
        			<span><img src="${ctx }/static/assets/img/guidepage/guanggaozhu_xuanzhong.png"></span>
        			广告主
        		</a>
        	</li>
            <li>
            	<a id="media" href="#媒体主" data-toggle="tab" onclick="tabFn(this)">
            		<span><img src="${ctx }/static/assets/img/guidepage/meiti_weixuanzhong.png"></span>
            		媒体
           		</a>
         	</li>
        </ul>
		<div class="row tab-content">
			<div style="text-align:center; margin-top:15px; min-height:600px; cursor:pointer;" class="tab-pane active" id="广告主">
				<div id="adimg1" style="display: block;" onclick="fn(1,'ad')">
					<img src="${ctx }/static/assets/img/guidepage/1guanggaozhu1.jpg">
				</div>
				<div id="adimg2" style="display: none;" onclick="fn(2,'ad')">
					<img src="${ctx }/static/assets/img/guidepage/1guanggaozhu2.jpg">
				</div>
				<div id="adimg3" style="display: none;" onclick="fn(3,'ad')">
					<img src="${ctx }/static/assets/img/guidepage/1guanggaozhu3.jpg">
				</div>
				<div id="adimg4" style="display: none;" onclick="fn(4,'ad')">
					<img src="${ctx }/static/assets/img/guidepage/1guanggaozhu4.jpg">
				</div>
				<div id="adimg5" style="display: none;" onclick="fn(5,'ad')">
					<img src="${ctx }/static/assets/img/guidepage/1guanggaozhu5.jpg">
				</div>
				<div id="adimg6" style="display: none;" onclick="fn(6,'ad')">
					<img src="${ctx }/static/assets/img/guidepage/1guanggaozhu6.jpg">
				</div>
				<div id="adimg7" style="display: none;" onclick="fn(7,'ad')">
					<img src="${ctx }/static/assets/img/guidepage/1guanggaozhu7.jpg">
				</div>
				<div id="adimg8" style="display: none;" onclick="fn(8,'ad')">
					<img src="${ctx }/static/assets/img/guidepage/1guanggaozhu8.jpg">
				</div>
			</div>
			<div style="text-align:center; margin-top:15px; min-height:600px; cursor:pointer;" class="tab-pane" id="媒体主">
				<div id="mediaimg1" style="display: block;" onclick="fn(1,'media')">
					<img src="${ctx }/static/assets/img/guidepage/1meitizhu1.jpg">
				</div>
				<div id="mediaimg2" style="display: none;" onclick="fn(2,'media')">
					<img src="${ctx }/static/assets/img/guidepage/1meitizhu2.jpg">
				</div>
				<div id="mediaimg3" style="display: none;" onclick="fn(3,'media')">
					<img src="${ctx }/static/assets/img/guidepage/1meitizhu3.jpg">
				</div>
				<div id="mediaimg4" style="display: none;" onclick="fn(4,'media')">
					<img src="${ctx }/static/assets/img/guidepage/1meitizhu4.jpg">
				</div>
				<div id="mediaimg5" style="display: none;" onclick="fn(5,'media')">
					<img src="${ctx }/static/assets/img/guidepage/1meitizhu5.jpg">
				</div>
				<div id="mediaimg6" style="display: none;" onclick="fn(6,'media')">
					<img src="${ctx }/static/assets/img/guidepage/1meitizhu6.jpg">
				</div>
				<div id="mediaimg7" style="display: none;" onclick="fn(7,'media')">
					<img src="${ctx }/static/assets/img/guidepage/1meitizhu7.jpg">
				</div>
				<div id="mediaimg8" style="display: none;" onclick="fn(8,'media')">
					<img src="${ctx }/static/assets/img/guidepage/1meitizhu8.jpg">
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" language="JavaScript">
		function tabFn(thiz){
			var $this = $(thiz);
			var thisStr = $this.find('img').attr('src').replace('_weixuanzhong','_xuanzhong');
			var otherStr = $this.parent().siblings().find('img').attr('src').replace('_xuanzhong','_weixuanzhong');
			$this.find('img').attr('src',thisStr);
// 			$this.attr('style','color:red')
			$this.parent().siblings().find('img').attr('src',otherStr);
// 			$this.parent().siblings().find('a').attr('style','');
		}
        function fn(n,str){
       		var nextn;
        	if(n==8){
        		nextn=1;
       		}else{
       			nextn=n+1;
      		}
	        document.getElementById(str+"img"+n).style.display="none";
	        document.getElementById(str+"img"+nextn).style.display="block";
        }
        
</script>
</body>
</html>

