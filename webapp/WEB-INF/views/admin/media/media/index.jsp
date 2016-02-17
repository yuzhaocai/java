<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>媒体查询</title>
</head>
<body>
       <div class="panel panel-default">                    
           <div class="panel-heading">
               <ul class="breadcrumb">
                 <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
                 <li class="active">媒体查询</li>
             </ul>
           </div>
           <div class="panel-body">
               <div class="topwrap">
                   <form class="form-inline">                             
                       <div class="form-group form-group-sm"> 
                           <label>手机</label>
                           <input name="search_LIKE_customer.mobPhone" value="${param['search_LIKE_customer.mobPhone'] }" type="text" class="form-control"> 
                       </div>                             
                       <div class="form-group form-group-sm"> 
                           <label>昵称</label>
                           <input name="search_LIKE_name" value="${param.search_LIKE_name }" type="text" class="form-control"> 
                       </div> 
                       <div class="form-group form-group-sm"> 
                           <label>创建时间</label>
                           <div class="input-group">
                           <input type="text" class="form-control" id="search_GTE_createTime" name="search_GTE_createTime" value="${param.search_GTE_createTime }" 
                               onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',maxDate:'#F{$dp.$D(\'search_LTE_createTime\')}'})">
                           <div class="input-group-addon">至</div>
                           <input type="text" class="form-control" id="search_LTE_createTime" name="search_LTE_createTime" value="${param.search_LTE_createTime }" 
                               onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',minDate:'#F{$dp.$D(\'search_GTE_createTime\')}'})">
                           </div>
                       </div>
                       <div class="form-group form-group-sm"> 
                           <label>大类</label>
                           <select class="form-control" name="search_EQ_mediaType"> 
                               <option value="">不限</option>
                               <c:forEach items="${mediaTypes}" var="mediaType">
                               <option value="${mediaType.itemCode }" <c:if test="${mediaType.itemCode eq param.search_EQ_mediaType }">selected</c:if>>${mediaType.itemName }</option>
                               </c:forEach>
                           </select>
                       </div>                                
                       <div class="form-group form-group-sm"> 
                           <label>实名认证</label>
                           <select class="form-control" name="search_EQ_customer.certified"> 
                               <option value="">不限</option>
                               <option value="true" <c:if test="${param['search_EQ_customer.certified'] eq 'true' }">selected</c:if>>是</option>
                               <option value="false" <c:if test="${param['search_EQ_customer.certified'] eq 'false' }">selected</c:if>>否</option>
                           </select>
                       </div> 
                       <div class="form-group form-group-sm"> 
                           <label>级别</label>
                           <select class="form-control" name="search_EQ_level"> 
                               <option value="">不限</option>
                               <c:forEach items="${mediaLevels}" var="mediaLevel">
                               <option value="${mediaLevel.itemCode }" <c:if test="${mediaLevel.itemCode eq param.search_EQ_level }">selected</c:if>>${mediaLevel.itemName }</option>
                               </c:forEach>
                           </select>
                       </div>
                       <div class="form-group form-group-sm"> 
                           <label>状态</label>
                           <select class="form-control" name="search_EQ_status"> 
                               <option value="">不限</option>
                               <c:forEach items="${mediaStatus}" var="mediaStatus">
                               <option value="${mediaStatus.itemCode }" <c:if test="${mediaStatus.itemCode eq param.search_EQ_status }">selected</c:if>>${mediaStatus.itemName }</option>
                               </c:forEach>
                           </select>
                       </div>  
                       <div class="form-group form-group-sm">                                
                       <button type="submit" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
                       </div>
                   </form>
               </div>

               <table class="table table-bordered table-condensed table-hover table-photos">
                   <thead>
                       <tr class="thead">
                           <th>头像</th>
                           <th>大类</th>
                           <th>认证类型</th>
                           <th>昵称</th>
                           <th>创建时间</th>
                           <th>所属账号</th>
                           <th>实名认证</th>
                           <th>级别</th>
                           <th>星级</th>
                           <th>简介/案例</th>
                           <th></th>
                       </tr>
                   </thead>
                   <tbody>
                       <c:forEach items="${data.content }" var="item">
                       <tr <c:if test="${item.status eq 'MEDIA_S_DISABLED' }">style="background:#ccc"</c:if> >
                           <td><img src="<zy:fileServerUrl value="${item.showPic }"/>" class="photo"></td>
                           <td><zy:dic value="${item.mediaType }"/></td>
                           <td><zy:dic value="${item.category }"/></td>                            
                           <td>${item.name }</td>
                           <td><fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd"/></td>
                           <td>${item.customer.mobPhone }</td>
                           <td>
                               <c:if test="${item.customer.certified }"><a data-id="${item.customer.id }" class="view-certificate">查看</a></c:if>
                               <c:if test="${not item.customer.certified }">无</c:if>
                           </td>
                           <td><zy:dic value="${item.level }"/></td>                            
                           <td><zy:dic value="${item.star.name}"/></td>                            
                           <td class="tdw-lg"><tags:summaryDetail data="${item.description }" /></td>                              
                           <td><button data-id="${item.id }" class="btn btn-primary btn-sm preview-btn"><i class="fa fa-eye"></i> 预览</button>
                               
                               <div class="btn-group">
                                   <button class="btn btn-primary btn-sm" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" id="fenji">
                                   <i class="fa fa-list-ol"></i> 分级 <i class="fa fa-caret-down"></i>
                                   </button>
                                   <ul class="dropdown-menu radio" aria-labelledby="fenji" style="right:0;left:auto; float:right">
                                       <c:forEach items="${mediaLevels}" var="mediaLevel">
                                       <li><a href="javascript:void(0)"><label><input type="radio" name="media" media-id="${item.id }" media-level="${mediaLevel.itemCode }">${mediaLevel.itemName }</label></a></li>
                                       </c:forEach>
                                   </ul>
                               </div>
                               <c:choose>
								   <c:when test="${item.status eq 'MEDIA_S_DISABLED' }">
								   		<button onclick="return disable('${item.id}','${item.status}');" class="btn btn-success btn-sm">
			                               <i class="fa fa-edit"></i>开放
										</button>
								   </c:when>
								   <c:otherwise>
										<button onclick="return disable('${item.id}','${item.status}');" class="btn btn-danger btn-sm">
			                               <i class="fa fa-minus-circle"></i>屏蔽
										</button>
								   </c:otherwise>
							   </c:choose>
							   <div class="btn-group">
                                   <button class="btn btn-primary btn-sm" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" id="updateMedia">
                                   <i class="fa fa-edit"></i> 修改 <i class="fa fa-caret-down"></i>
                                   </button>
                                   <ul class="dropdown-menu radio" aria-labelledby="updateMedia" style="right:0;left:auto; float:right;background:#337ab7">
                                       <li><button onclick="return edit('${item.id}');" class="btn btn-primary btn-sm">媒体</button></li>
                                       <li><button onclick="return editCase('${item.id}');" class="btn btn-primary btn-sm">案例</button></li>
                                       <li><button onclick="return editQuote('${item.id}');" class="btn btn-primary btn-sm">结算价</button></li>
                                   </ul>
                               </div>
                               <c:if test="${not empty item.mediaQuoteLogs }">
                               	<div class="btn-group">
                               		<c:forEach items="${item.mediaQuoteLogs }" var="mediaQuoteLog">
                               			<c:set value="${mediaQuoteLog.id }" var="id"></c:set>
                               		</c:forEach>
                              	 <button href="" class="btn btn-primary btn-sm " onclick="editLog('${id }')">修改记录</button>
                               </div>
                               </c:if>
                           </td>
                       </tr>
                       </c:forEach>
                   </tbody>
               </table>
            <tags:pagination page="${data}" />
        </div>
    </div>
    <!--查看实名认证  -->
    <div class="modal fade" id="cert" tabindex="-1" role="dialog" data-width="640" aria-labelledby="myModalLabel">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"
                aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
            <h4 class="modal-title">申请人详细</h4>
        </div>
        <div class="modal-body">
            <div class="row">
                <div class="col-xs-3">
                    姓名：<a href="#"><span id="certName"></span></a>
                </div>
                <div class="col-xs-9">
                    认证身份：<a href="#"><span id="certIndentity"></span></a>
                </div>
            </div>
            <br>
            <div class="row">
	            <div class="col-xs-3">
	                <label>认证材料:</label>
	            </div>
	            <div class="col-xs-9">
                 	<img alt="" src="" width="200" height="150" id="certMatter1">
					<img alt="" src="" width="200" height="150" id="certMatter2">
					<img alt="" src="" width="200" height="150" id="certMatter3">
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn-u btn-u-red" type="button" data-dismiss="modal"
                    aria-label="Close">关闭</button>
            </div>
        </div>
    </div>
    <div class="modal fade" id="preview" tabindex="-1" role="dialog" data-width="750" data-replace="true">
    </div>   
<script type="text/javascript">

$(function() {
    menu.active('#media-media');
    
    $("input[ name='media']").change(function() {
        var id = $(this).attr("media-id");
        var level = $(this).attr("media-level");
        $.post("${ctx}/admin/media/media/assignLevel?ajax",{id: id, level: level},function(result){
            window.location.reload();
        });
    });
    
    $("input[ name='star']").change(function() {
        var mediaId = $(this).attr("media-id");
        var id = $(this).attr("media-star");
        $.post("${ctx}/admin/mediaStar/assign?ajax",{id: id, mediaId: mediaId},function(result){
        	if(result)
            	window.location.reload();
        });
    });
    
    /*
     *查看实名认证资料
     */
     
     $(".view-certificate").click(function() {
    	var customerId = $(this).attr("data-id");
        $.ajax({
            type : "GET",
            url : "${ctx}/admin/member/cert/certificate?ajax",
            data : {
                customerId : customerId
            },
            dataType : "json",
            success : function(data) {
            	$("#certName").html(data.certName);
				$("#certIndentity").html(data.certIndentity);
			   if (data.custType == 'CUST_T_ADVERTISER') {
					$("#name").html("联系人");
					$("#company").html("公司名称");
				} else {
					$("#name").html("姓名");
					$("#company").html("认证身份");
				}

				if (data.certMatter1 != null) {
					$("#certMatter1").show();
					$("#certMatter1").attr("src", data.certMatter1);
				} else {
					$("#certMatter1").hide();
				}
				if (data.certMatter2 != null) {
					$("#certMatter2").show();
					$("#certMatter2").attr("src", data.certMatter2);
				} else {
					$("#certMatter2").hide();
				}
				if (data.certMatter3 != null) {
					$("#certMatter3").show();
					$("#certMatter3").attr("src", data.certMatter3);
				} else {
					$("#certMatter3").hide();
				}
				$("#cert").modal("show");
            }
        });
    });
    
     
     $(".preview-btn").click(function() {
         $('#preview').loadModal('${ctx}/admin/media/media/view/?ajax', {id: $(this).attr("data-id")}, function() {
             $('#btn-close').click(function() {
                 $('#preview').modal('hide');
             });
         });
         return false;
     });      
});

function disable(id,status) {
	var msg = '';
	if(status=='MEDIA_S_DISABLED'){
		msg = '开放后需重新审核媒体，您确定要开放该媒体吗?';
	}else{
		msg = '您确定要屏蔽该媒体吗?';
	}
    bootbox.confirm(msg, function(result) {
        if (result) {
            $.post("${ctx}/admin/media/media/disable?ajax",{id: id,status:status},function(result){
                window.location.reload();
            });
        }
    });
    return false;
}

function edit(id) {
    window.location.href = "${ctx}/admin/media/media/update/" + id;
    return false;
}

function editLog(id) {
    window.location.href = "${ctx }/admin/mediaQuote/history/"+id;
    return false;
}

function editCase(id) {
    window.location.href = "${ctx}/admin/audit/media/case?mediaId=" + id;
    return false;
}

function editQuote(id) {
    window.location.href = "${ctx}/admin/audit/media/quote?mediaId=" + id;
    return false;
}

</script>        
</body>
</html>
