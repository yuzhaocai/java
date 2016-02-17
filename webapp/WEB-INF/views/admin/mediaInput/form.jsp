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
        <li class="active">
          <zy:dic value="${mediaType }"/>媒体录入
        </li>
    </ul>
  </div><!-- / 右侧标题 -->
  
  <div class="panel-body"><!-- 右侧主体内容 -->
  
	<form id="inputForm" action="${ctx }/admin/mediaInput/${action}" method="post" class="form-horizontal" enctype="multipart/form-data">
    <zy:token/>
		<input type="hidden" name="id" value="${media.id}"/>
		<input type="hidden" name="mediaType" value="${media.mediaType}"/>
        
		<fieldset>
            <div class="form-group form-group-sm">
                <label for="mobPhone" class="col-md-3 control-label">联系电话:</label>
                <div class="col-md-6 has-feedback">
                    <input type="text" class="form-control" id="mobPhone" name="mobPhone" value="${media.mobPhone}" />
                </div>
            </div>
          
            <div class="form-group form-group-sm">
                <label for="password" class="col-md-3 control-label">密码:</label>
                <div class="col-md-6 has-feedback">
                    <input type="text" class="form-control" id="password" name="password" value="${media.password}" />
                </div>
            </div>
          
		    <div class="form-group form-group-sm">
		        <label for="name" class="col-md-3 control-label">媒体名称:</label>
		        <div class="col-md-6 has-feedback">
		            <input type="text" class="form-control" id="name" name="name" value="${media.name}" />
		        </div>
		    </div>
	      
            <div class="form-group form-group-sm">
                <label for="account" class="col-md-3 control-label">账号:</label>
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
                <label for="category" class="col-md-3 control-label">认证类型:</label>
                <div class="col-md-6 has-feedback">
                  <select class="form-control" name="category"> 
                      <c:forEach items="${categories}" var="item">
                      <option value="${item.itemCode }" <c:if test="${item.itemCode eq media.category }">selected</c:if>>${item.itemName }</option>
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
				<label for="description" class="col-md-3 control-label">简介:</label>
				<div class="col-md-6 has-feedback">
					<textarea class="form-control" id="description"	name="description" style="height: auto;" rows="3">${media.description }</textarea>
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
            
            <div class="form-group form-group-sm">
                <label for="caseTitle" class="col-md-3 control-label">案例一标题:</label>
                <div class="col-md-6 has-feedback">
                    <input type="text" class="form-control" id="caseTitle" name="caseTitle" value="${media.caseTitle}" />
                </div>
            </div>
      
            <div class="form-group form-group-sm">
                <label for="caseLight" class="col-md-3 control-label">案例一亮点:</label>
                <div class="col-md-6 has-feedback">
                    <input type="text" class="form-control" id="caseLight" name="caseLight" value="${media.caseLight}" />
                </div>
            </div>

            <div class="form-group form-group-sm">
                <label for="caseContent" class="col-md-3 control-label">案例一内容:</label>
                <div class="col-md-6 has-feedback">
                    <textarea class="form-control" id="caseContent" name="caseContent" style="height: auto;" rows="6">${media.caseContent }</textarea>
                </div>
            </div>
            
            <div class="form-group form-group-sm">
                <label for="caseTitle1" class="col-md-3 control-label">案例二标题:</label>
                <div class="col-md-6 has-feedback">
                    <input type="text" class="form-control" id="caseTitle1" name="caseTitle1" value="${media.caseTitle1}" />
                </div>
            </div>
      
            <div class="form-group form-group-sm">
                <label for="caseLight1" class="col-md-3 control-label">案例二亮点:</label>
                <div class="col-md-6 has-feedback">
                    <input type="text" class="form-control" id="caseLight1" name="caseLight1" value="${media.caseLight1}" />
                </div>
            </div>

            <div class="form-group form-group-sm">
                <label for="caseContent1" class="col-md-3 control-label">案例二内容:</label>
                <div class="col-md-6 has-feedback">
                    <textarea class="form-control" id="caseContent1" name="caseContent1" style="height: auto;" rows="6">${media.caseContent1 }</textarea>
                </div>
            </div>
            
            <div class="form-group form-group-sm">
                <label for="quoteType" class="col-md-3 control-label">服务类别一:</label>
                <div class="col-md-6 has-feedback">
	                <select class="form-control" name="quoteType"> 
                        <option value="">请选择</option>
	                    <c:forEach items="${services}" var="item">
	                    <option value="${item.itemCode }" <c:if test="${item.itemCode eq media.quoteType }">selected</c:if>>${item.itemName }</option>
	                    </c:forEach>
	                </select>
                </div>
            </div>
          
            <div class="form-group form-group-sm">
                <label for="quotePrice" class="col-md-3 control-label">报价:</label>
                <div class="col-md-6 has-feedback">
                    <input type="number" class="form-control" id="quotePrice" name="quotePrice" value="${media.quotePrice}" />
                </div>
            </div>

            <div class="form-group form-group-sm">
                <label for="quoteType" class="col-md-3 control-label">服务类别二:</label>
                <div class="col-md-6 has-feedback">
	                <select class="form-control" name="quoteType1"> 
                        <option value="">请选择</option>
	                    <c:forEach items="${services}" var="item">
	                    <option value="${item.itemCode }" <c:if test="${item.itemCode eq media.quoteType1 }">selected</c:if>>${item.itemName }</option>
	                    </c:forEach>
	                </select>
                </div>
            </div>
          
            <div class="form-group form-group-sm">
                <label for="quotePrice1" class="col-md-3 control-label">报价:</label>
                <div class="col-md-6 has-feedback">
                    <input type="number" class="form-control" id="quotePrice1" name="quotePrice1" value="${media.quotePrice1}" />
                </div>
            </div>

            <div class="form-group form-group-sm">
                <label for="quoteType2" class="col-md-3 control-label">服务类别三:</label>
                <div class="col-md-6 has-feedback">
	                <select class="form-control" name="quoteType2"> 
                        <option value="">请选择</option>
	                    <c:forEach items="${services}" var="item">
	                    <option value="${item.itemCode }" <c:if test="${item.itemCode eq media.quoteType2 }">selected</c:if>>${item.itemName }</option>
	                    </c:forEach>
	                </select>
                </div>
            </div>
          
            <div class="form-group form-group-sm">
                <label for="quotePrice2" class="col-md-3 control-label">报价:</label>
                <div class="col-md-6 has-feedback">
                    <input type="number" class="form-control" id="quotePrice2" name="quotePrice2" value="${media.quotePrice2}" />
                </div>
            </div>

            <div class="form-group form-group-sm">
                <label for="quoteType3" class="col-md-3 control-label">服务类别四:</label>
                <div class="col-md-6 has-feedback">
	                <select class="form-control" name="quoteType3"> 
                        <option value="">请选择</option>
	                    <c:forEach items="${services}" var="item">
	                    <option value="${item.itemCode }" <c:if test="${item.itemCode eq media.quoteType3 }">selected</c:if>>${item.itemName }</option>
	                    </c:forEach>
	                </select>
                </div>
            </div>
          
            <div class="form-group form-group-sm">
                <label for="quotePrice3" class="col-md-3 control-label">报价:</label>
                <div class="col-md-6 has-feedback">
                    <input type="number" class="form-control" id="quotePrice3" name="quotePrice3" value="${media.quotePrice3}" />
                </div>
            </div>

            <div class="form-group form-group-sm">
                <label for="quoteType4" class="col-md-3 control-label">服务类别五:</label>
                <div class="col-md-6 has-feedback">
	                <select class="form-control" name="quoteType4">
                        <option value="">请选择</option>
	                    <c:forEach items="${services}" var="item">
	                    <option value="${item.itemCode }" <c:if test="${item.itemCode eq media.quoteType4 }">selected</c:if>>${item.itemName }</option>
	                    </c:forEach>
	                </select>
                </div>
            </div>
          
            <div class="form-group form-group-sm">
                <label for="quotePrice4" class="col-md-3 control-label">报价:</label>
                <div class="col-md-6 has-feedback">
                    <input type="number" class="form-control" id="quotePrice4" name="quotePrice4" value="${media.quotePrice4}" />
                </div>
            </div>
      
		</fieldset>
		
		<br/>
		
		 <div class="form-group">
			<div class="col-md-offset-3 col-md-2">
			   <a class="btn btn-default btn-block" href="${ctx}/admin/mediaInput"><span class="glyphicon glyphicon-remove"></span> 返回</a>
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
	menu.active('#media-input');
	
	$('#inputForm').validate({
		rules: {
			name: {
				required: true
			}
			
		}
	});
	
	
	if ("create" == "${action}") {
		// 地区默认全国
		$('#region').html("<option value='ALL' selected>全国</option>")
		
		// 粉丝方向默认全选
		$("input[name='fansDir']").click() 
        
        // 适用产品
        $("input[name='products']").each(function(index, item) {
        	// 生活服务;数码家电;母婴用品;娱乐/传媒广告;汽车/汽车用品
        	switch ($(item).attr("data-label")) {
            case "生活服务":
            	$(item).click(); break;
            case "数码家电":
            	$(item).click(); break;
            case "娱乐/传媒广告":
            	$(item).click(); break;
            case "汽车/汽车用品":
            	$(item).click(); break;
            case "母婴用品/童装/玩具":
            	$(item).click(); break;
        	}
        });
        
        // 行业类型
        $("input[name='industryType']").each(function(index, item) {
            // 时事新闻;财经理财;房产家居;IT科技;汽车3C;旅游休闲;时尚搭配;文学艺术;生活购物
            switch ($(item).attr("data-label")) {
            case "时事新闻":
                $(item).click(); break;
            case "财经理财":
                $(item).click(); break;
            case "房产家居":
                $(item).click(); break;
            case "IT科技":
                $(item).click(); break;
            case "汽车3C":
                $(item).click(); break;
            case "旅游休闲":
                $(item).click(); break;
            case "时尚搭配":
                $(item).click(); break;
            case "文学艺术":
                $(item).click(); break;
            case "生活购物":
                $(item).click(); break;
            }
        });
        
        // 微博服务类型一（默认）：单次推送（原发）
        $("option", "select[name='quoteType']").each(function(index, item) {
        	if ($(item).html() == "单次推送(原发)") {
        		$(item).attr("selected", true);
        	}
        });
        // 微博服务类型二（默认）：单次推送(转发)
        $("option", "select[name='quoteType1']").each(function(index, item) {
            if ($(item).html() == "单次推送(转发)") {
            	$(item).attr("selected", true);
            }
        });
        
        
        // 微信服务类型一（默认）：单图文
        $("option", "select[name='quoteType']").each(function(index, item) {
            if ($(item).html() == "单图文") {
                $(item).attr("selected", true);
            }
        });
        
        // 微信服务类型二（默认）：多图文第一条                
        $("option", "select[name='quoteType1']").each(function(index, item) {
            if ($(item).html() == "多图文第一条") {
                $(item).attr("selected", true);
            }
        });
        
        $("input[name='quotePrice']").val("0");
        $("input[name='quotePrice1']").val("0");
        $("input[name='quotePrice2']").val("0");
        $("input[name='quotePrice3']").val("0");
        $("input[name='quotePrice4']").val("0");
	}
	
	// 地区选择控件初始化
	$('#region').regionModal();
});
</script>
</body>
</html>
