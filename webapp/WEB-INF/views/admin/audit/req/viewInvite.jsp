<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	<h4 class="modal-title">邀请媒体列表</h4>
</div>
  
<div class="modal-body">
    <table width="100%" border="0" class="table table-bordered">
      <tr>
        <th scope="col">邀请方式</th>
        <th scope="col">大类</th>
        <th scope="col">媒体名称</th>
        <th scope="col">认证类型</th>
        <th scope="col">手机</th>
        <th scope="col">是否改稿</th>
        <th scope="col">粉丝数</th>
        <th scope="col">价格</th>
        <th scope="col">应邀状态</th>
      </tr>
      <c:forEach items="${reqMedias }" var="item">
      <tr>
        <td><zy:dic value="${item.inviteType }"/></td>
        <td><zy:dic value="${item.media.mediaType }"/></td>
        <td><zy:dic value="${item.media.name }"/></td>
        <td><zy:dic value="${item.media.category }"/></td>
        <td><zy:dic value="${item.media.customer.mobPhone }"/></td>
        <c:choose>
            <c:when test="${item.changed }">
                        <td class="hightlight">是&nbsp; <small> <a class=""
                                href="${ctx }/member/req/download/article/${item.requirement.article}">下载</a>
                        </small>
                        </td>
                    </c:when>
            <c:otherwise><td>否</td></c:otherwise>
        </c:choose>
        <td>${item.media.fans }</td>
        <td><span class="color-orange">${item.price }</span></td>
        <!-- 应邀状态(未反馈、拒绝、接受) -->
        <c:choose>
            <c:when test="${item.fbStatus eq 'MEDIA_FB_NULL'}">
               <td><span class="color-orange">未反馈</span></td>
            </c:when>
            <c:when test="${item.fbStatus eq 'MEDIA_FB_ACCEPT'}">
               <td><span class="color-green">同意</span></td>
            </c:when>
            <c:when test="${item.fbStatus eq 'MEDIA_FB_REFUSE'}">
               <td><span class="color-red">拒绝</span></td>
            </c:when>
        </c:choose>
      </tr>
      </c:forEach>
      <c:if test="${empty reqMedias }">
      <tr>
        <td colspan="9">当前没有可选的媒体</td>
      </tr>
      </c:if>
    </table>
</div>
  
<div class="modal-footer">
    <button class="btn-u btn-u-red" type="button" id="btn-close">关闭</button>
</div>
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
