<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<div id="dic-item-modal" class="modal fade" tabindex="-1" role="dialog"
  aria-labelledby="titleModalLabel" aria-hidden="true"><!-- 新建字典项对话框 -->
    
    <div class="modal-header"> <!-- modal-header -->
      <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
      <h4 class="modal-title" id="titleModalLabel">
        <c:if test="${empty item.id}">新建</c:if>
        <c:if test="${!empty item.id}">修改</c:if>-字典项</h4>
    </div> <!-- /modal-header -->
    
    <div class="modal-body"> <!-- modal-body -->
    
    <form id="dic-item-form" action="${ctx}/admin/dic/item/save" method="post" class="form-horizontal">
      <zy:token/>
      <input id="dicId" type="hidden" name="dic.id" value="${item.dic.id }" />
      <input id="id" type="hidden" name="id" value="${item.id }" />
      <input id="id" type="hidden" name="itemId" value="${item.id }" />
      
      <div class="form-group form-group-sm">
        <label for="itemCode" class="col-sm-3 control-label"><span class="text-red">* </span>字典项编码:</label>
        <div class="col-sm-6 has-feedback">
          <input type="text" class="form-control" id="itemCode" name="itemCode" required
            value="${item.itemCode }" <c:if test="${!empty item.id }">readonly</c:if> />
        </div>
      </div>
      
      <div class="form-group form-group-sm">
        <label for="itemName" class="col-sm-3 control-label"><span class="text-red">* </span>字典项名称:</label>
        <div class="col-sm-6 has-feedback">
          <input type="text" class="form-control" id="itemName" name="itemName" required="required" value="${item.itemName }"/>
        </div>
      </div>
      
      <div class="form-group form-group-sm">
        <label for="seqNum" class="col-sm-3 control-label"><span class="text-red"> </span>排序号:</label>
        <div class="col-sm-6 has-feedback">
          <input type="text" class="form-control" id="seqNum" name="seqNum" required="required" value="${item.seqNum }"/>
        </div>
      </div>
      
    </form>
    
    </div><!-- /modal-body -->
    
    <div class="modal-footer">
      <button type="button" id="dic-item-form-cancel" class="btn"><i class="icon-remove"></i> 取消</button>
      <button type="button" id="dic-item-form-save" class="btn btn-primary"><i class="icon-ok icon-white"></i> 保存</button>
    </div>

</div>

<script type="text/javascript">

var checkDicItemNameUrl = '${ctx}/admin/dic/checkDicItemName?oldName=${item.itemName}&dicId=${item.dic.id}';
var checkDicItemCodeUrl = '${ctx}/admin/dic/checkDicItemCode?oldCode=${item.itemCode}&dicId=${item.dic.id}';

var dicItemValidator = $('#dic-item-form').validate({
  rules: {
    itemName: {
      required: true,
      remote: checkDicItemNameUrl
    },
    itemCode: {
      required: true,
      remote: checkDicItemCodeUrl
    },
    seqNum : {
    	range: [0, 999999]
    }
  },
  messages: {
    itemName: {
      remote: '字典项已存在',
    },
    itemCode: {
      remote: '字典项编码已存在'
    },
  }
});


//保存表单
$('#dic-item-form-save').click(function(event) {
  if(dicItemValidator.form()) {
    var $form = $('#dic-item-form');
    var url = '${ctx}/admin/dic/item/save';
    $.post(url, $form.serialize(), function(data) {
      common.hideModal('#dic-item-modal');
      zTree.getZTreeObj("dicItemTree").reAsyncChildNodes(null, "refresh");
    });
  }
});

//取消表单
$('#dic-item-form-cancel').click(function( event ) {
  common.hideModal('#dic-item-modal');
});
</script>