<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<div class="modal-header">
	<h4 class="modal-title">媒体预览</h4>
</div>

<div class="modal-body">
    <div class="sys-media">
        <div class="bg-grey border p-xs m-b-xs">
            <a class="pull-left">
            <img src="<zy:fileServerUrl value="${media.showPic }" />" class="img-circle media-avatar"/>
            </a>
            <div class="media-body m-b-xs">
                <div class="row">
                    <div class="col-md-12"><span class="text-muted col-md-3">媒体名称：</span>${media.name }</div>
                </div>
                <div class="row">
                    <div class="col-md-12"><span class="text-muted col-md-3">大类：</span><zy:dic value="${media.mediaType }"/></div>
                </div>
                <div class="row">
                    <div class="col-md-12"><span class="text-muted col-md-3">认证类别：</span><zy:dic value="${media.category }"/></div>
                </div>
                <div class="row">
                    <div class="col-md-12"><span class="text-muted col-md-3">账号：</span>${media.account }</div>
                </div>
                <div class="row">
                    <div class="col-md-12"><span class="text-muted col-md-3">联系电话：</span>${media.mobPhone }</div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <span class="text-muted col-md-3">行业类型：</span>
                        <zy:dic value="${media.industryType }"/>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <span class="text-muted col-md-3">地区：</span>
                        <zy:area id="${media.region }"/>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <span class="text-muted col-md-3">适用产品：</span>
                        <zy:dic value="${media.products }"/>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12"><span class="text-muted col-md-3">粉丝数：</span>${media.fans }</div>
                </div>
                
                <div class="row">
                    <div class="col-md-12">
                        <span class="text-muted col-md-3">粉丝方向：</span>
                        <zy:dic value="${media.fansDir }"/>
                    </div>
                </div>
            </div>
        </div>                
        
        <div class="row">
            <div class="col-lg-12">
				<div class="panel panel-default">
					<!-- Default panel contents -->
					<div class="panel-heading"><strong>媒体简介</strong></div>
					<div class="panel-body">
					   <p>${media.description }</p>
					</div>
				</div>            
            </div>
        </div>

        <c:if test="${empty media.caseTitle }">
        <div class="row">
            <div class="col-lg-12">
		        <div class="panel panel-default">
		            <!-- Default panel contents -->
		            <div class="panel-heading"><strong>案例</strong></div>
	                <div class="panel-body">
	                    <div class="col-md-6">该媒体暂无案例！</div>
	                </div>
                </div>            
            </div>
        </div>
        </c:if>

        <c:if test="${not empty media.caseTitle }">
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-default">
                    <!-- Default panel contents -->
                    <div class="panel-heading"><strong>案例一</strong></div>
		            <ul class="list-group">
		                <li class="list-group-item"><span class="text-muted">标题：</span>${media.caseTitle }</li>
		                <li class="list-group-item"><span class="text-muted">亮点：</span>${media.caseLight }</li>
		                <li class="list-group-item"><span class="text-muted">正文：</span>${media.caseContent }</li>
		            </ul>
                </div>            
            </div>
        </div>
        </c:if>

        <c:if test="${not empty media.caseTitle1 }">
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-default">
                    <!-- Default panel contents -->
                    <div class="panel-heading"><strong>案例二</strong></div>
                    <ul class="list-group">
                        <li class="list-group-item"><span class="text-muted">标题：</span>${media.caseTitle1 }</li>
                        <li class="list-group-item"><span class="text-muted">亮点：</span>${media.caseLight1 }</li>
                        <li class="list-group-item"><span class="text-muted">正文：</span>${media.caseContent1 }</li>
                    </ul>
                </div>            
            </div>
        </div>
        </c:if>

        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-default">
                    <!-- Default panel contents -->
                    <div class="panel-heading"><strong>服务及报价</strong></div>
                    <c:if test="${empty media.quoteType }">
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-6">该媒体暂无报价！</div>
                        </div>
                    </div>
                    </c:if>
                    <c:if test="${not empty media.quoteType }">
                    <ul class="list-group">
                        <li class="list-group-item"><span class="text-muted">服务类别：</span><zy:dic value="${media.quoteType }"/></li>
                        <li class="list-group-item"><span class="text-muted">报价：</span>${media.quotePrice }</li>
                        
                        <c:if test="${not empty media.quoteType1 }">
                        <li class="list-group-item"><span class="text-muted">服务类别：</span><zy:dic value="${media.quoteType1 }"/></li>
                        <li class="list-group-item"><span class="text-muted">报价：</span>${media.quotePrice1 }</li>
                        </c:if>
                        
                        <c:if test="${not empty media.quoteType2 }">
                        <li class="list-group-item"><span class="text-muted">服务类别：</span><zy:dic value="${media.quoteType2 }"/></li>
                        <li class="list-group-item"><span class="text-muted">报价：</span>${media.quotePrice2 }</li>
                        </c:if>
                        
                        <c:if test="${not empty media.quoteType3 }">
                        <li class="list-group-item"><span class="text-muted">服务类别：</span><zy:dic value="${media.quoteType3 }"/></li>
                        <li class="list-group-item"><span class="text-muted">报价：</span>${media.quotePrice3 }</li>
                        </c:if>
                        
                        <c:if test="${not empty media.quoteType4 }">
                        <li class="list-group-item"><span class="text-muted">服务类别：</span><zy:dic value="${media.quoteType4 }"/></li>
                        <li class="list-group-item"><span class="text-muted">报价：</span>${media.quotePrice4 }</li>
                        </c:if>
                    </ul>
                    </c:if>
                </div>            
            </div>
        </div>
    </div>
</div>
  
<div class="modal-footer">
    <button class="btn-u btn-u-red" type="submit" id="btn-audit" >审核通过</button>
</div>
<form id="audit-form" action="${ctx }/admin/mediaInput/audit/${media.id}" method="post">
</form>

<script type="text/javascript">
<!--
$(function() {
    menu.active('#media-input');
    
	$("#btn-audit").click(function() {
	    bootbox.confirm("您确定要审核通过吗?", function(result) {
	        if (result){
                $("#audit-form").submit();
	        }
	    })
	})
});
//-->
</script>


<style type="text/css">
.navbar-brand {width:500px;line-height: 70px;padding-left:170px;color:#fff;}
.breadcrumb {
  margin: 0;
  padding:0;
  background-color: transparent;
}

/*以下是新增样式*/
.sys-media .pull-left {
    margin-right:10px;
}
.sys-media .media-avatar {
    width:100px;
    height:100px;
}
.sys-media .media-body {
    display:block;
    width:auto;
}
.sys-media .media-body .row{
    padding:5px 0;
}

.border {
    border:1px solid #ddd;
}
.border-dotted {
    border:1px dotted #ddd;
}
.p-xs {
    padding:10px;
}
.m-b-xs {
    margin-bottom:10px;
}
.bg-grey {
    background-color:#eee;
}

</style>
