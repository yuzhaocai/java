<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>

<style type="text/css">
.navbar-brand {
	width: 500px;
	line-height: 70px;
	padding-left: 170px;
	color: #fff;
}

.breadcrumb {
	margin: 0;
	padding: 0;
	background-color: transparent;
}

/*以下是新增样式*/
.sys-media .pull-left {
	margin-right: 10px;
}

.sys-media .media-avatar {
	width: 100px;
	height: 100px;
}

.sys-media .media-body {
	display: block;
	width: auto;
}

.sys-media .media-body .row .col- ^* {
	padding: 5px 0;
}

.border {
	border: 1px solid #ddd;
}

.border-dotted {
	border: 1px dotted #ddd;
}

.p-xs {
	padding: 10px;
}

.m-b-xs {
	margin-bottom: 10px;
}

.bg-grey {
	background-color: #eee;
}
</style>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title">${mediaName }-用户信息</h4>
</div>
<div class="modal-body">

	<div class="panel pad pd15">
    	<div id="div1" class="panel-body">
    		<form  class="form-horizontal" style="width:600px; margin-left:100px;" >
				<div class="form-group">
                    <label class="col-xs-4 control-label">联系人:</label>
                    <div class="col-xs-6 has-feedback">
                        <input class="form-control" type="text" value="${cust.linkman }" readonly="readonly">                      
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-xs-4 control-label">手机号:</label>
                    <div class="col-xs-6 has-feedback">
                        <input class="form-control" type="text" value="${cust.email }" readonly="readonly">                      
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-xs-4 control-label">邮箱:</label>
                    <div class="col-xs-6 has-feedback">
                        <input class="form-control" type="email"  value="${cust.mobPhone }" readonly="readonly">                      
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-xs-4 control-label">QQ:</label>
                    <div class="col-xs-6 has-feedback">
                        <input class="form-control" type="text"  value="${cust.qq }" readonly="readonly">                      
                    </div>
                </div>
            </form>
		</div>
	</div>
</div>
