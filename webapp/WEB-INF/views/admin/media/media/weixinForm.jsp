<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>修改媒体</title>
</head>

<body>

<div class="panel panel-default">

  <div class="panel-heading"><!-- 右侧标题 -->
    <ul class="breadcrumb">
        <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
        <li>媒体管理</li>
        <li class="active">
          修改媒体
        </li>
    </ul>
  </div><!-- / 右侧标题 -->
  
  <div class="panel-body"><!-- 右侧主体内容 -->
  
	<form id="inputForm" action="${ctx }/admin/media/media/update" method="post" class="form-horizontal" enctype="multipart/form-data">
    <zy:token/>
		<input type="hidden" name="id" value="${media.id}"/>
		
		<fieldset>
	    <div class="form-group form-group-sm">
         <label for="name" class="col-md-3 control-label"><span class="text-red">* </span>媒体名称:</label>
         <div class="col-md-6 has-feedback">
           <input type="text" class="form-control" id="name" name="name" value="${media.name}" />
         </div>
      </div>
      
      <div class="form-group form-group-sm">
         <label for="mediaType" class="col-md-3 control-label"><span class="text-red">* </span>认证类型:</label>
         <div class="col-md-6 has-feedback">
           <select class="form-control" name="category"> 
               <c:forEach items="${categories}" var="item">
               <option value="${item.itemCode }" <c:if test="${item.itemCode eq media.category }">selected</c:if>>${item.itemName }</option>
               </c:forEach>
           </select>
         </div>
      </div>
      
      <div class="form-group form-group-sm">
         <label for="description" class="col-md-3 control-label"><span class="text-red">* </span>简介:</label>
         <div class="col-md-6 has-feedback">
           <textarea class="form-control" id="description" name="description" style="height: auto;" rows="3">${media.description }</textarea>
         </div>
      </div>
      
      <div class="form-group form-group-sm">
          <label for="account" class="col-md-3 control-label"><span class="text-red">* </span>微信号:</label>
          <div class="col-md-6 has-feedback">
              <input type="text" class="form-control" id="account" name="account" value="${media.account}" />
          </div>
      </div>
    
      <div class="form-group form-group-sm">
          <label for="region" class="col-md-3 control-label">地区:</label>
          <div class="col-md-6 has-feedback">
            <select class="form-control" name="region" id="region" multiple="multiple"> 
                <c:forEach items="${fn:split(media.region, ',') }" var="item" >
                <option value="${item }" selected><zy:area id="${item }" /></option>
                </c:forEach>
            </select>
          </div>
      </div>
          
        <div class="form-group form-group-sm">
            <label for="" class="col-md-3 control-label">行业:</label>
            <div class="col-md-6 has-feedback">
                <div class="btn-group" data-toggle="buttons">
                    <c:forEach var="item" items="${industryTypes }">
                        <c:if test="${fn:contains(media.industryType, item.itemCode) }">
                            <label class="btn btn-primary active">
                                <input type="checkbox" name="industryType" value="${item.itemCode }" data-label="${item.itemName }" autocomplete="off" checked>${item.itemName }
                            </label>
                        </c:if>
                        <c:if test="${not fn:contains(media.industryType, item.itemCode) }">
                            <label class="btn btn-primary">
                                <input type="checkbox" name="industryType" value="${item.itemCode }" data-label="${item.itemName }" autocomplete="off"> ${item.itemName }
                            </label>
                        </c:if>
                    </c:forEach>
                </div>
            
            </div>
        </div>
  
        <div class="form-group form-group-sm">
            <label for="fans" class="col-md-3 control-label">粉丝数:</label>
            <div class="col-md-6 has-feedback">
                <input type="number" class="form-control" id="fans" name="fans" value="${media.fans}" />
            </div>
        </div>
  
        <div class="form-group form-group-sm">
            <label for="fansDir" class="col-md-3 control-label">粉丝方向:</label>
            <div class="col-md-6 has-feedback">
                <div class="btn-group" data-toggle="buttons">
                    <c:forEach var="item" items="${fansDirs }">
                        <c:if test="${fn:contains(media.fansDir, item.itemCode) }">
                            <label class="btn btn-primary active">
                                <input type="checkbox" name="fansDir" value="${item.itemCode }" autocomplete="off" checked>${item.itemName }
                            </label>
                        </c:if>
                        <c:if test="${not fn:contains(media.fansDir, item.itemCode) }">
                            <label class="btn btn-primary">
                                <input type="checkbox" name="fansDir" value="${item.itemCode }" autocomplete="off"> ${item.itemName }
                            </label>
                        </c:if>
                    </c:forEach>
                </div>
            </div>
        </div>
        
        <div class="form-group form-group-sm">
            <label for="products" class="col-md-3 control-label">适用产品:</label>
            <div class="col-md-6 has-feedback">
                <div class="btn-group" data-toggle="buttons">
                    <c:forEach var="item" items="${products }">
                        <c:if test="${fn:contains(media.products, item.id) }">
                            <label class="btn btn-primary active">
                                <input type="checkbox" name="products" value="${item.id }" data-label="${item.itemName }" autocomplete="off" checked>${item.itemName }
                            </label>
                        </c:if>
                        <c:if test="${not fn:contains(media.products, item.id) }">
                            <label class="btn btn-primary">
                                <input type="checkbox" name="products" value="${item.id }" data-label="${item.itemName }" autocomplete="off"> ${item.itemName }
                            </label>
                        </c:if>
                    </c:forEach>
                </div>
            </div>
        </div>
        
        <div class="form-group form-group-sm has-feedback">
            <label for="description" class="col-md-3 control-label"></label>
            <label class="">媒体LOGO:</label>
            <div class="row img">
                <label for="description" class="col-md-3"></label>
                <div class="col-md-6">
                    <div class="fileinput fileinput-new" data-provides="fileinput">
                        <div class="fileinput-new thumbnail"
                            style="width: 180px; height: 180px;">
                            <c:if test="${empty media.showPic }">
                            <img data-src="" src="${ctx}/static/assets/img/No_image_available.png">
                            </c:if>
                            <c:if test="${not empty media.showPic }">
                            <img data-src="" src="<zy:fileServerUrl value="${media.showPic }"/>">
                            </c:if>
                        </div>
                        <div class="fileinput-preview fileinput-exists thumbnail"
                            style="width: 180px; height: 180px;"></div>
                        <div>
                            <span class="btn btn-default btn-file">
                            <span class="fileinput-new">选择图片</span>
                            <span class="fileinput-exists">更改图片</span>
                            <input type="file" name="logo"></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>           
      
		</fieldset>
		
		<br/>
		
		 <div class="form-group">
			<div class="col-md-offset-3 col-md-2">
			   <a class="btn btn-default btn-block" href="${ctx}/admin/media/media"><span class="glyphicon glyphicon-remove"></span> 返回</a>
			</div>
			<div class="col-md-2">
			  <button type="submit" class="btn btn-primary btn-block"><span class="glyphicon glyphicon-ok"></span> 保存</button>
			</div>
		</div>

	</form>

  </div>
	
</div>

<script type="text/javascript">
$(function() {
	menu.active('#media-media');
	// 地区选择控件初始化
    $('#region').regionModal();
	   
	$('#inputForm').validate({
		rules: {
			name: {
                required: true
            },
            account: {
                remote: {
                    url: "${ctx}/admin/media/media/checkWeixinAccount?ajax",
                    data: {
                        oldAccount: "${media.account}"
                    }
                },              
                required: true
            },
            fans: {
                required: true
            }
		},
		messages: {
		    account: {
		        remote: "微信号重复，请重新输入！"
		    }
	    }
	});
});
</script>
</body>
</html>
