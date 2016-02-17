<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>发布媒体</title>
	
    <script type="text/javascript" charset="utf-8" src="${ctx }/static/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${ctx }/static/ueditor/ueditor.all.min.js"> </script>
    <script type="text/javascript" charset="utf-8" src="${ctx }/static/ueditor/ueditor.lczy.js"> </script>
    <!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
    <!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
    <script type="text/javascript" charset="utf-8" src="${ctx }/static/ueditor/lang/zh-cn/zh-cn.js"></script>
</head>

<body>

<div class="panel panel-default">

  <div class="panel-heading"><!-- 右侧标题 -->
    <ul class="breadcrumb">
        <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
        <li class="active">
          更多媒体
        </li>
    </ul>
  </div><!-- / 右侧标题 -->
  
  <div class="panel-body"><!-- 右侧主体内容 -->
  
	<form id="inputForm" action="${ctx }/admin/otherMedia/${action}" method="post" class="form-horizontal" enctype="multipart/form-data">
    <zy:token/>
		<input type="hidden" name="id" value="${media.id}"/>
        
		<fieldset>
		    <div class="form-group form-group-sm">
		        <label for="name" class="col-md-3 control-label">媒体名称:</label>
		        <div class="col-md-6 has-feedback">
		            <input type="text" class="form-control" id="name" name="name" value="${media.name}" />
		        </div>
		    </div>
          
			<div class="form-group form-group-sm">
				<label for="region" class="col-md-3 control-label">地区:</label>
				<div class="col-md-6 has-feedback">
					<select class="form-control" name="region" id="region" multiple="multiple"> 
			             <c:if test="${not empty media.region }">
			             <c:forEach items="${fn:split(media.region, ',') }" var="item" >
			             <option value="${item }" selected><zy:area id="${item }" /></option>
			             </c:forEach>
			             </c:if>
                    </select>
				</div>
			</div>
	      
            <div class="form-group form-group-sm">
                <label for="category" class="col-md-3 control-label">媒体类别:</label>
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
                <label for="attachment" class="col-md-3 control-label">上传附件:</label>
                <div class="col-md-6 has-feedback">
                    <input type="file" class="form-control" id="attach" name="attach" />
                </div>
                <c:if test="${!empty media.attachment }">
	            	<a class="btn btn-sm btn-primary" href="<zy:fileServerUrl value="${media.attachment }" />">下载附件</a>
	            </c:if>
            </div>
            
            <c:if test="${ empty media.mediaTabs}">
            <div class="panel panel-default">
                <div class="panel-heading"><!-- 右侧标题 -->
                    <span class=""></span> 页卡
                    <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div><!-- / 右侧标题 -->
                
                <div class="panel-body"><!-- 右侧主体内容 -->
		            <div class="form-group form-group-sm">
		                <label for="caseTitle" class="col-md-3 control-label">标题:</label>
		                <div class="col-md-6 has-feedback">
		                    <input type="text" class="form-control" id="mediaTabs[0].title" name="mediaTabs[0].title" value="${tabs.title}" />
		                </div>
		            </div>
		      
                    <script id="mediaTabs[0].content" name="mediaTabs[0].content" type="text/plain"></script>
                </div>
            </div>
            </c:if>
            
            <c:if test="${ not empty media.mediaTabs}">
            <c:forEach items="${media.mediaTabs}" var="item" varStatus="status">
			<div class="panel panel-default">
				<div class="panel-heading"><!-- 右侧标题 -->
					<span class=""></span> 页卡
		            <button type="button" class="close" data-dismiss="modal"
		                aria-label="Close">
		                <span aria-hidden="true">&times;</span>
		            </button>
				</div><!-- / 右侧标题 -->
				
				<div class="panel-body"><!-- 右侧主体内容 -->
		            <div class="form-group form-group-sm">
		                <label for="caseTitle" class="col-md-3 control-label">标题:</label>
		                <div class="col-md-6 has-feedback">
		                    <input type="text" class="form-control" id="mediaTabs[${status.index}].title" name="mediaTabs[${status.index}].title" value="${item.title}" />
		                </div>
		            </div>
		      		<input type="hidden" name="mediaTabs[${status.index}].id" value="${item.id}"/>
                    <script id="mediaTabs[${status.index}].content" name="mediaTabs[${status.index}].content" type="text/plain">${item.content}</script>
			    </div>
			</div>
			</c:forEach>
            </c:if>

		</fieldset>
		
		<br/>
		
		 <div class="form-group">
			<div class="col-md-offset-3 col-md-2">
			<c:if test="${media.name ne null }">
			   <a class="btn btn-default btn-block" href="${ctx}/admin/otherMedia/"><span class="glyphicon glyphicon-remove"></span> 返回</a>
			</c:if>
			</div>
			<div class="col-md-2">
              <a id="add_tab_btn" class="btn btn-primary btn-block"><span class="glyphicon glyphicon-ok"></span> 添加页卡</a>
			</div>
            <div class="col-md-2">
              <button type="submit" class="btn btn-primary btn-block"><span class="glyphicon glyphicon-ok"></span> 保存</button>
            </div>
		</div>

	</form>
  </div>
</div>

<div id="__resource" class="hide">
            <div class="panel panel-default">
                <div class="panel-heading"><!-- 右侧标题 -->
                    <span class=""></span> 页卡
                    <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div><!-- / 右侧标题 -->
                
                <div class="panel-body"><!-- 右侧主体内容 -->
                    <div class="form-group form-group-sm">
                        <label for="caseTitle" class="col-md-3 control-label">标题:</label>
                        <div class="col-md-6 has-feedback">
                            <input type="text" class="form-control" id="mediaTabs[___].title" name="mediaTabs[___].title" value="${tabs.title}" />
                        </div>
                    </div>
              
                    <script id="mediaTabs[___].content" name="mediaTabs[___].content" type="text/plain"></script>
                </div>
            </div>
</div>




<script type="text/javascript">
var tabCount = 1;
<c:if test="${ not empty media.mediaTabs}">
tabCount = ${fn:length(media.mediaTabs)};
</c:if>

$(function() {
	<c:if test="${ not empty media.name}">
	menu.active('#other-media');
	</c:if>
	
	<c:if test="${empty media.name}">
	menu.active('#other-media-create');
	</c:if>	
	$('#inputForm').validate({
		rules: {
			name: {
				required: true
			}
		}
	});
	
	// 地区选择控件初始化
	$('#region').regionModal();
	
	var config = {
        toolbars: [
                    [   'cleardoc','|',
                        'bold', 'italic', 'underline','|',
                        'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', '|',
                        'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
                        'fontsize', '|',
                        'justifyleft', 'justifycenter', 'justifyright', '|',
                        'imageleft', 'imageright', 'imagecenter', '|',
                        'inserttable', 'deletetable', '|',
                        'simpleupload', /*'insertimage',*/'map'
                    ]
        ]
        , initialFrameHeight: 320
        , autoHeightEnabled: false
        , enableAutoSave: false
    }
	
    <c:if test="${ empty media.mediaTabs}">
	UE.getEditor('mediaTabs[0].content', config);
    </c:if>

    <c:if test="${ not empty media.mediaTabs}">
    <c:forEach items="${media.mediaTabs}" var="item" varStatus="status">
    UE.getEditor('mediaTabs[${status.index}].content', config);
    </c:forEach>
    </c:if>

    $("#add_tab_btn").click(function() {
    	var html = $("#__resource").html();
    	html = html.replace(/___/g, tabCount);
    	$("fieldset").append(html);
        UE.getEditor('mediaTabs[' + tabCount + '].content', config);
    	tabCount++;
    	
        $(".close").click(function() {
            close(this);
        });
    	return false;
    });
    
    $(".close").click(function() {
        close(this);
    });
    function close(self) {
    	if ($(".panel", "fieldset").size() > 1) {
    		var name = $("input", $(self).closest(".panel")).attr("id").split(".")[0] + ".content";
    	    $(self).closest(".panel").remove();
    	    $("textarea[name='" + name + "']").remove();
    	}
    	
    }
    
    
});
</script>
</body>
</html>
