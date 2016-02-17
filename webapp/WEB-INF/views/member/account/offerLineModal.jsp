<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>
<!-- 线下服务 -->
 <form id="offerLine-form" action="${ctx }/member/account/offerLine"  method="post">
   <zy:token/>
   <input value="${account.customer.id }" name="customerId" type="hidden"/>
   <div class="modal-header">
	     <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	     <h4 class="modal-title">购买线下服务</h4>
   </div>
   <div class="modal-body">
   		<div class="form-group">
			<strong>
				余额： 
				<span class="" style="color:red"> 
					<fmt:formatNumber value="${account.avBalance }" currencyCode="CNY" type="currency" />
				</span>
			</strong>
   		</div>
   		<div class="form-group has-feedback">
   			请输入您要支付的金额&nbsp;<input type="text" name="money" id="money" />&nbsp;元
   		</div>
   </div>
   <div class="modal-footer">
	   	<button class="btn-u btn-u-red" type="submit" id="btn-send-offerLine">确定</button>
	   	<button class="btn-u btn-u-red" type="button" data-dismiss="modal" aria-label="Close">取消</button>
   </div>
</form>

<script>
$(function(){
	
	$('#offerLine-form').validate({
		debug: false,
		submitHandler: function(form) {
			form.submit();
		},
		rules: {
			money: {
				required:true,
				digits:true,
//					min: 100,
				max: '${account.avBalance}'
			}
		},
		messages: {
			money:{
				required: '请输入支付金额',
//	                min: '最少提100元',
				max: '您的余额只有 ${account.avBalance}元'
			}
		}
	});
})

</script>