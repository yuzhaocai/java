<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>删除广告</title>
</head>
<body>
    <div class="panel panel-default">                    
        <div class="panel-heading">
            <ul class="breadcrumb">
              <li><span class="glyphicon glyphicon-home"></span> 广告管理</li>
              <li class="active">删除广告</li>
          </ul>
        </div>
        <div class="panel-body">  
        	 	 <div class="topwrap">
	                   <form class="form-inline">   
	                       <div class="form-group form-group-sm"> 
	                           <label>筛选：</label>
	                           <select class="form-control" name="search_LIKE_type"> 
	                                <option value="">请选择</option>
									<option value="像素墙" <c:if test="${param.search_LIKE_type eq '像素墙'}">selected</c:if>>像素墙</option>
									<option value="首页海报" <c:if test="${param.search_LIKE_type eq '首页海报'}">selected</c:if>>首页海报</option>
									<option value="优媒推荐" <c:if test="${param.search_LIKE_type eq '优媒推荐'}">selected</c:if>>优媒推荐</option>
									<option value="名企客户" <c:if test="${param.search_LIKE_type eq '名企客户'}">selected</c:if>>名企客户</option>
									<option value="微信频道-推荐媒体" <c:if test="${param.search_LIKE_type eq '微信频道-推荐媒体'}">selected</c:if>>微信频道-推荐媒体</option>
									<option value="微博频道-推荐媒体" <c:if test="${param.search_LIKE_type eq '微博频道-推荐媒体'}">selected</c:if>>微博频道-推荐媒体</option>
									<option value="合作媒体" <c:if test="${param.search_LIKE_type eq '合作媒体'}">selected</c:if>>合作媒体</option>
									<option value="友情链接" <c:if test="${param.search_LIKE_type eq '友情链接'}">selected</c:if>>友情链接</option>
									<option value="营销经典图文头条" <c:if test="${param.search_LIKE_type eq '营销经典图文头条'}">selected</c:if>>营销经典图文头条</option>
									<option value="小编推荐" <c:if test="${param.search_LIKE_type eq '小编推荐'}">selected</c:if>>小编推荐</option>
	                           </select>
	                       </div>                             
	                       <div class="form-group form-group-sm">                                
	                       <button type="submit" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
	                       </div>
	                   </form>
	               </div>                  
             <table class="table table-bordered table-condensed table-hover">
                 <thead>
                     <tr class="thead">
                    	 <th>权重</th>
                         <th>类型</th>
                         <th>标题</th>
                         <th>操作</th>
                     </tr>
                 </thead>
                 <tbody>
                 	 <c:forEach items="${data.content }" var="item">
	                     <tr>
	                     	 <td>${item.weight }</td>
	                         <td>${item.type }</td>
	                         <td>${item.title }</td>
	                         <td>
	                         	<button class="btn btn-danger btn-sm item-delete" data-id="${item.id }">
									<i class="fa fa-remove"></i>删除
								</button>
								<button class="btn btn-primary btn-sm item-weight" data-id="${item.id }" data-weight="${item.weight }">
									<i class="fa fa-edit"></i>修改权重
								</button>
							 </td>      
	                     </tr>
                     </c:forEach>
                 </tbody>
             </table>
         <tags:pagination page="${data}" />
        </div>
    </div>
<!-- 修改权重 -->
<div class="modal fade" id="updateWeight" tabindex="-1" role="dialog" data-replace="false" data-backdrop="static">

    <form id="weight-form" action="${ctx}/admin/advSetting/weight" method="post" onsubmit="return saveReport();">
      <input type="hidden" name="id">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">修改权重</h4>
      </div>
      <div class="modal-body">
      		<div class="row">
	      		<label class="col-sm-3 control-label">权重：</label>
	      		<div class="col-sm-9 has-feedback">
	      			<input class="form-control" name="weight" maxlength="2">
	      		</div>
	      	</div>
      </div>
      <div class="modal-footer">
      	<button class="btn-u btn-u-red" type="submit" 
      		id="btn-refuse-ok"
      		data-loading-text="正在处理...">确定</button>
      </div>
    </form>

</div>
<script type="text/javascript" src="${ctx }/static/bower_components/jquery-form/jquery.form.js"></script>
<script type="text/javascript">

$(function() {
	
    menu.active('#del-adv');
    
    

    //删除广告位
    $('.item-delete').click(function(){
    	var id = $(this).data('id');
        bootbox.confirm("您确定要删除选中广告位吗?", function(result) {
   			 if (result) {
   	        	$.post('${ctx}/admin/advSetting/delete',{id:id},function(result){
   	        		window.location.reload();
   	        	});
   	         }
        });
    });
  	//修改权重
    $('.item-weight').click(function(){
    	var id = $(this).data('id');
    	var weight = $(this).data('weight');
 	    $('#weight-form input[name="id"]').val(id);
 	    $('#weight-form input[name="weight"]').val(weight);
 	    $('#updateWeight').showModal();
 	    
    });
    
});

function saveReport() {
	//验证表单
	if($("#weight-form .has-error").length>0){
		return false;
	}
	// jquery 表单提交
	$("#weight-form").ajaxSubmit(function(message) {
// 		$('#updateWeight').modal('hide');
		window.location.reload();
		common.showMessage(message.content);
	});
	return false; // 必须返回false，否则表单会自己再做一次提交操作，并且页面跳转
} 

//为表单注册validate函数
$("#weight-form").validate({
	rules : {
		weight : {
			required : true,
			digits : true
		},
	},
	messages : {
		weight : {
			required : "请输入权重",
			minlength : "请输入正确的权重"
		}
	}
});

</script>        
</body>
</html>
