<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>会员注册-采媒在线</title>
</head>

<body style="background-color:#f7f7f7;">

<!--=== Content ===-->
<div class="container">
	<div class="form-page">
    	<div class="headline">
        	<h3>会员注册</h3>
            <a href="javascript:void(0)" onClick="$('#loginBtn').click()" class="btn btn-link pull-right"><i class="fa fa-arrow-circle-o-right"></i> 已有账号去登录</a>
        </div>
        
        <%-- 显示后台验证错误的标签 --%>
        <tags:fieldErrors commandName="vo" />
        <div class="register-wrap"> 
        	<img src="${ctx}/static/assets/img/form/register-title.jpg" width="1041" height="42" class="margin-bottom-60">       
            <form id="register-form" class="form-horizontal" style="width:520px;margin-left:200px;" action="${ctx }/register" enctype="multipart/form-data" method="post">
            	<zy:token/>        
                <div class="form-group">
                    <label class="col-xs-4 control-label highlight-blue">会员类型：</label>
                    <div class="col-xs-8 has-feedback">
                            <label class="radio-inline"><input type="radio" name="custType" value="CUST_T_ADVERTISER" <c:if test="${'CUST_T_ADVERTISER' eq param.custType }">checked="checked"</c:if> id="Advertiser">我是广告主</label>
                            <label class="radio-inline"><input type="radio" name="custType" value="CUST_T_PROVIDER" <c:if test="${'CUST_T_PROVIDER' eq param.custType }">checked="checked"</c:if> id="Media" >我是媒体</label>                    
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-xs-4 control-label"><span class="color-red">*</span>手机号：</label>
                    <div class="col-xs-8 has-feedback">
                        <div class="input-group">
                            <span class="input-group-addon"><img src="${ctx}/static/assets/img/form/shouji.png"></span>
                            <input type="text" class="form-control" id="mobPhone" name="mobPhone" value="${param.mobPhone }" maxlength="11">
                      </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-xs-4 control-label"><span class="color-red">*</span>短信验证码:</label>
                    <div class="col-xs-8 has-feedback">
                        <div class="input-group">
                       	 	<span class="input-group-addon"><img src="${ctx}/static/assets/img/form/yanzhengma.png"></span>
                            <input id="smscode" name="smscode" class="form-control" placeholder="输入短信验证码" 
                        	type="text" maxlength="6">
                            <span class="input-group-btn">
                                <button type="button" class="btn btn-default bg-color-grey" id="btn-smscode" >获取短信验证码</button>
                            </span>
                        </div>
                    </div>
                </div>                
                
                <div class="form-group">
                    <label class="col-xs-4 control-label"><span class="color-red">*</span>密码:</label>
                    <div class="col-xs-8 has-feedback">
                        <div class="input-group">
                            <span class="input-group-addon"><img src="${ctx}/static/assets/img/form/mima.png"></span>
                            <input type="password" class="form-control" id="password" name="password" placeholder="" maxlength="20">
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-xs-4 control-label"><span class="color-red">*</span>重复密码:</label>
                    <div class="col-xs-8 has-feedback">
                        <div class="input-group">
                            <span class="input-group-addon"><img src="${ctx}/static/assets/img/form/mima.png"></span>
                            <input type="password" class="form-control" id="checkPassword" name="checkPassword" placeholder="请再次输入上面的密码" maxlength="20">
                        </div>
                    </div>
                </div>
                <div id="adAdditional">
                    <div class="form-group">
                        <label class="col-xs-4 control-label"><span class="color-red">*</span>客户名称:</label>
                        <div class="col-xs-8 has-feedback">
                            <div class="input-group">
                                <span class="input-group-addon"><img src="${ctx}/static/assets/img/form/kehu.png"></span>
                                <input type="text" class="form-control" id="name" name="name" placeholder="个人姓名或者企业名称" maxlength="30">
                            </div>
                        </div>
                    </div>
                </div>
                <div id="mediaAdditional" class="hide">
                    <div class="form-group">
                        <label class="col-xs-4 control-label"><span class="color-red">*</span>媒体性质:</label>
                        <div class="col-xs-8 has-feedback">  
                            <label class="radio-inline"><input type="radio" name="mtype2" checked>个人</label>
                            <label class="radio-inline"><input type="radio" name="mtype2">企业/机构</label> 
                        </div>
                    </div>
                </div> 
                <div class="form-group">
                	<div class="checkbox col-xs-8 col-xs-offset-4 has-feedback">
                	<label>
                	<input type="checkbox" name="agreement" checked="checked" >我已阅读并同意
                	</label>
                	<a id="treaty-btn"  class="highlight highlight-blue" style="cursor: pointer;">《采媒在线免责条款》</a>
                    </div>
                
                </div>
                
                <div class="form-group">
                    <div class="col-xs-8 col-xs-offset-4">
                   		<button type="submit"  class="btn-u btn-u-red rounded">立即注册</button>
                    </div>
                </div>
        	</form>
    	</div><!--/ register wrap -->
    </div>
</div>
<!--=== End Content ===-->
<!--=== End Content ===-->
<!-- 采媒在线免责条款 -->
	<div id="treaty" class="modal  fade" tabindex="-1" data-width="760"  data-height="500">
 		 <div class="modal-header">
    		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
   		 <h3 class="modal-title text-center">采媒在线平台服务协议</h3>
  	</div>
  <div class="modal-body">
    <div class="pre">
       欢迎使用采媒在线平台提供的服务，为了保障用户合法的权益，请在使用采媒在线平台之前，请务必审慎阅读、充分理解《采媒在线平台服务协议》（以下简称“本协议”）各条款内容，特别是免除或者限制责任的条款、法律适用和争议解决条款。免除或者限制责任的条款将以粗体字标识，请用户重点阅读。如对协议有任何疑问，可向采媒在线平台客服咨询。
       本协议在采媒在线平台经营者（以下称“采媒在线”）和用户间具有合同上的法律效力，采媒在线依据《采媒在线平台服务协议》提供服务。用户只有完全同意下列所有服务条款并完成注册程序，才能成为采媒在线平台的用户并使用相应服务。用户一经注册或使用本协议下任何服务即视为对本协议条款的充分理解和完全接受，并自愿接受本协议各项条款的约束，包括采媒在线对服务条款随时所做的任何修改。届时用户不应以未阅读本协议的内容或者未获得采媒在线平台对用户问询的解答等理由，主张本协议无效或要求撤销。

第一条 定义
1、采媒在线平台：
采媒在线平台的性质为第三方电子商务交易平台，指在互联网广告在线交易活动中为交易双方（或多方）提供交易撮合及相关服务的信息网络系统的总和(包括但不限于一个网站（采媒在线平台的网址）或APP)。
2、采媒在线平台经营者：
    采媒在线平台的经营者为北京采媒在线传媒科技有限公司。 
3、平台用户：
平台用户指符合本协议所规定的条件，同意遵守本网站各种规则、条款（包括但不限于本协议），并使用本平台服务和产品的个人和组织。
    用户类型包括：
1）渠道用户：以具有商业渠道价值的资源为产品，在采媒在线平台上进行展示和交易的媒体，包括自然人、法人或其他组织。
2）品牌用户：是指有品牌整合传播诉求，需要定制宣传营销产品和服务的自然人、法人或其他组织。
    
第二条 注册与账户
1、 用户必须是具备完全民事权利能力和完全民事行为能力的自然人、法人或其他组织。若不具备前述主体资格，则采媒在线有权立即注销该用户，并追究其使用平台服务的法律责任。
2、自然人用户同意注册本平台时提供其本人真实准确的法定姓名、性别和居民身份证号码等个人身份识别资料和完整、详尽、真实的其他个人资料。企事业单位注册本平台时应提供真实、合法的名称、组织机构代码、联系方式以及其他资料等。
3、用户按照注册页面提示填写信息、阅读并同意本协议，完成全部注册程序并经采媒在线审核通过后，可获得采媒在线平台账户和密码，成为采媒在线平台注册用户。用户应妥善保管、使用其账户及密码信息，并对其账户项下发生的所有行为（包括但不限于在线签署各类协议、发布信息、购买产品、服务及披露信息等）负责。
4、用户在相关资料变更时及时更新注册信息。若发现任何非法使用其用户账号或存在安全漏洞的情况，应立即通知采媒在线平台。

第三条 采媒在线平台服务
1、采媒在线平台作为“互联网+媒介”的应用服务互联网提供者，拥有跨行业、跨地域的互联网媒介采购平台，以“媒体资源自采购”、“舆情监控”及“新闻素材交易”为三大主要功能模块，致力于为传统媒体转型提供架构设计和技术支持，同时凭借专业的公关策划与媒介团队，为品牌用户提供全案定制化营销服务，为渠道用户的资源产品提供展示和交易的网络平台。
2、通过采媒在线及其关联公司提供的采媒在线平台服务和其它服务，用户（渠道用户和品牌用户）可在采媒在线平台上发布交易信息、查询产品和服务信息、达成交易意向并进行线上线下交易、对其他用户进行评价、参加采媒在线组织的活动以及使用其它信息服务和技术服务，具体以平台开通的服务内容为准。

第四条 用户的权利与义务
1、用户在使用本平台的服务过程中，必须遵循国家的相关法律法规以及采媒在线平台的规范制度，不得利用采媒在线平台发布危害国家安全、色情、暴力等非法内容；不得利用采媒在线平台发布含有虚假、胁迫、侵害、中伤的违法内容或其它在道德上令人反感的内容。 
2、用户应保证发布信息所对应的产品和服务是真实、有效、合法，不得在本平台上传播任何假冒伪劣产品（包括但不限于假冒专利、认证；不符合国家标准；过期变质；虚假服务等产品），不得发布侵犯他人知识产权或其他合法权益的信息。
3、用户应对广告发布、公关发稿、合作活动所涉及的文字、图片、数据等全部信息进行审核，确保发布的信息不违反相关法律法规，不侵犯任何第三方的知识产权。
4、用户在交易过程中，遵守诚实信用原则，不采取不正当竞争行为，不扰乱平台交易的正常秩序，不从事与平台交易无关的行为。
5、用户在使用采媒在线平台服务过程中，所产生的应纳税赋，以及一切硬件、软件、服务及其它方面的费用，均由用户独自承担。
6、用户需要指定专人对接采媒在线平台的渠道资源信息收集工作，安排专人负责所属平台交易账号的日常刷新响应工作，当订单交易出现异常和纠纷时，与平台客服共同协调解决方案。
7、渠道用户承诺以自己的名义通过平台生成订单的方式与品牌用户形成契约关系，非因采媒在线的原因不能完成订单或完成订单不符合要求的，由交易双方各自承担相应的责任。采媒在线在提供的第三方网络平台服务及约定的义务范围内对用户负责，不承担其他法律责任。

第五条采媒在线的权利与义务
1、采媒在线提供采媒在线平台有关的技术支持，并负责平台系统的运营、维护和宣传推广。为了完善平台功能和拓展平台服务，采媒在线对服务名称、功能、域名的调整和优化，不影响本合同的效力。
2、采媒在线在现有技术水平的基础上努力确保采媒在线平台的正常运行，尽力避免服务中断，保证用户交易活动的顺利进行，及时回复和处理用户在使用本平台中所遇到的各种问题。
3、采媒在线有权对用户提交的资质资料进行审核，但不对用户资质承担真实性、合法性的保证责任，任何因用户提交虚假、伪造等资质材料而引发的一切责任和后果由用户承担。如采媒在线因此受到损失（包括但不限于可能的赔偿、律师费、诉讼费）由用户承担。
4、采媒在线为平台上产生的每一单交易提供全程的跟单客服服务，为交易双方提供必要的沟通协调渠道，并且在订单发生纠纷时介入调解和解释规则，保障双方的合理权益。
5、采媒在线有权应政府部门（包括司法及行政部门）的要求，向其提供用户向采媒在线提供的用户信息和交易记录等必要信息。如用户涉嫌侵犯他人知识产权等合法权益，则采媒在线亦有权在初步判断涉嫌侵权行为存在的情况下，向权利人提供其必要的身份信息。
6、采媒在线有权在不通知用户的前提下删除或采取其他限制性措施处理下列信息：包括但不限于存在欺诈等恶意或虚假内容、与网上交易无关或不是以交易为目的、存在恶意竞价或其他试图扰乱正常交易秩序因素、违反公共利益或可能严重损害本平台和其他用户合法利益的。
7、如根据采媒在线所掌握的事实依据，可以认定用户发布的信息不符合法律法规、不按约定履行交易订单、违反本协议和相关规则以及有其他在平台上的不当行为，采媒在线可视情节轻重，对用户采取警告、暂停交易、限制交易权限、注销交易帐号等处理措施（详见《采媒在线违规处罚规范》）。
8、如用户涉嫌违反有关法律或者本协议之规定，使采媒在线遭受任何损失，或受到任何第三方的索赔，或受到任何行政管理部门的处罚，用户应当赔偿采媒在线因此受到的损失和发生的费用。

第六条 平台服务费及支付方式
1、平台服务费
1）渠道用户与品牌用户达成广告投放类（包括但不限于常规广告、异型广告、软文广告等）订单，采媒在线收取实际投放费总额15%作为服务费。
2）渠道用户与品牌用户达成公关软文类（包括但不限于新闻通稿、产品介绍、企业专访、深度报告、活动信息等）订单，采媒在线收取实际刊发费总额20%作为服务费。
3) 渠道用户与品牌用户达成线上/线下活动（包括但不限于新闻发布会、产品推介会、论坛研讨会、文化活动、展览展示、公益活动等）订单，采媒在线收取实际活动费总额30%作为服务费。
2、支付方式
1）通过采媒在线平台冻结品牌用户的费用后，采媒在线有权根据订单执行验收情况进行结算。采媒在线按照比例提取服务费后在30日内向渠道用户支付广告投放、公关软文、合作活动等费用。
2）渠道用户与品牌用户因发生纠纷而选择采媒在线平台纠纷处理机制的，采媒在线有权按照处理结果进行结算。
3）采媒在线将按照品牌用户的充值金额开具发票；渠道用户方应按采媒在线的要求提供发票，否则采媒在线有权拒绝其支付费用的要求。
    
第七条 纠纷处理机制
1、在采媒在线平台交易过程中各用户间发生争议的，任何一方均有权选择以下途径解决：
1）与争议相对方自主协商；
2）使用采媒在线平台提供的争议处理服务；
3）请求依法成立的调解组织调解；
4）提请仲裁机构仲裁或向人民法院提起诉讼；
5）其他纠纷解决方式。
2、在采媒在线平台处理决定作出前，如选择使用采媒在线平台的争议处理服务（详见《采媒在线纠纷处理机制规则》），则表示争议双方认可采媒在线平台作为独立的第三方根据其所了解到的争议事实并依据采媒在线平台规则所作出的处理决定（包括调整相关订单的交易状态、判定将争议款项的全部或部分支付给交易一方或双方等）。用户也可选择其他途径解决争议以中止采媒在线平台的争议处理服务。
3、如果用户对采媒在线的处理决定不满意，有权采取其他争议解决途径解决争议，但通过其他争议解决途径未取得终局决定前，应先遵守采媒在线的处理决定。

第八条 知识产权
1、采媒在线平台由采媒在线独立开发或获得授权许可。采媒在线所提供的网络交易服务的相关著作权、专利权、商标权、营业机密及其它任何所有权或权利，均属北京采媒在线传媒科技有限公司所有。包括但不限于：文字表述及其组合、图标、图饰、图表、色彩、界面设计、版面框架、音频及或视频资料、电子文档、技术、所有程序、产品及服务名称等均受知识产权法律法规的保护。
2、未经采媒在线的许可，任何单位和个人不得私自复制、传播、展示、镜像、上载、下载、使用，或者从事任何其他侵犯知识产权的行为。否则，采媒在线将追究相关法律责任。

第九条 隐私保护政策
1、采媒在线非常重视对平台用户隐私的保护，保护隐私是采媒在线的一项基本政策。用户提供的登记资料及采媒在线平台保留的有关的若干其他个人资料将受到中国有关隐私的法律和采媒在线相关规范的保护。
2、采媒在线平台保证不对外公开或向第三方提供用户的注册资料及用户在使用网络服务时存储在平台的非公开内容，但下列情况除外：
1）事先获得用户的明确授权；
2）如果用户出现违反中国有关法律或者平台政策的情况，需要向第三方披露；
3）只有根据用户个人资料，才能提供用户所要求的产品和服务；
4）如达成交易的用户任何一方履行或部分履行了交易义务并提出信息披露请求的，采媒在线平台可以决定向该用户提供其交易对方的联络方式等必要信息，以促成交易的完成或纠纷的解决；
5）根据有关的法律法规要求；
6）按照相关政府主管部门的要求
7）为维护平台的合法权益；
8）因用户的保管疏忽导致帐号、口令遭他人非法使用。
3、采媒在线平台不允许任何第三方以任何手段收集、编辑、出售或者无偿传播在平台注册的用户的个人信息。用户同意在不透露单个用户隐私资料的前提下，采媒在线平台可以对整个用户数据库进行分析并对用户数据库进行商业上的利用。
4、采媒在线会采取行业惯用措施保护用户信息的安全，但不能确信或保证任何个人信息的安全性，用户应自行承担风险。
5、采媒在线平台会向用户发送订制的信息，如果用户不希望收到这样的信息，只需在提供个人信息时或其他任何时候告知即可。

第九条 免责声明
1、鉴于网络服务的特殊性，用户理解并同意如发生下述任一情况而导致服务中断及遭受损失的，采媒在线不承担责任：
1）发生不可抗力情形的，如罢工、战争、自然灾害等；
2）黑客攻击、计算机病毒侵入或发作的；
3）计算机系统遭到破坏、瘫痪或无法正常使用的；
4）电信部门技术调整的，信息网络正常的设备维护；
5）因政府管制而造成暂时性关闭的。
2、品牌用户了解采媒在线平台上的产品及服务的信息、价格等系渠道用户发布，可能存在风险和瑕疵。鉴于网络平台具备存在海量信息及信息网络环境下信息与实物相分离的特点，采媒在线平台作为用户获取媒体服务信息、就交易进行协商及开展交易的场所，无法控制交易所涉及产品和服务的质量、安全或合法性，商贸信息的真实性或准确性，以及交易各方履行其在服务协议中各项义务的能力。用户应自行谨慎判断确定相关信息的真实性、合法性和有效性，并自行承担因此产生的责任与损失。
3、采媒在线将基于普通人的判断，根据双方提供的证明材料对争议做出处理。采媒在线平台并非司法机关，对凭证/证据的鉴别能力及对争议的处理能力有限，采媒在线平台进行纠纷处理完全是基于用户不可撤销的授权，其无法保证争议处理结果符合全部用户的期望。因此，除存在故意外，不对做出的争议处理结果承担责任，如用户因此遭受损失，用户同意自行向受益人索偿。

第十条 服务协议条款的修改
1、由于国家法律法规变化以及用户及市场状况的不断发展，采媒在线保留修改本协议条款的权利，修改后的服务协议不另对用户进行个别通知，采媒在线将在其平台上刊载公告，用户可随时登陆平台协议页面查阅最新服务协议。
2、如用户对修改事项不同意的，应当于修改事项确定的生效之日起停止使用采媒在线平台服务；如在修改事项生效后仍继续使用采媒在线平台服务，则视为同意接受经修订的条款。除另行明确声明外，任何使服务范围扩大或功能增强的新内容均受本协议约束。

第十一条 违约责任及处理
为适应电子商务发展和满足海量用户对高效优质服务的需求，用户理解并同意，采媒在线可在采媒在线平台规则中约定违约认定的程序和标准。 
1、用户对采媒在线的违约
1）违约的认定
①使用采媒在线平台服务时违反有关法律法规规定的；
②违反本协议或采媒在线其他规则的。
2）违约处理措施
①如用户在采媒在线平台上发布的信息构成违约的，采媒在线可根据相应规则立即对相应信息进行删除、屏蔽处理或对产品进行下架、监管。
②用户在采媒在线平台上实施的行为对采媒在线平台及其他用户产生不良影响，采媒在线可限制其参加营销活动、中止提供部分或全部服务、划扣违约金等处理措施。
③如用户的行为构成根本违约的，采媒在线可以查封违约用户的的账户，并扣除平台服务费后终止提供服务。
3）赔偿责任
①因用户的行为使采媒在线及其关联公司遭受损失（包括自身的直接经济损失、商誉损失及对外支付的赔偿金、和解款、律师费、诉讼费等间接经济损失），应赔偿采媒在线及其关联公司上述全部损失。
②因用户的行为使采媒在线及其关联公司遭受第三人主张权利，采媒在线及其关联公司可在对第三人承担金钱给付等义务后就全部损失向该用户追偿。
2、用户之间的违约
1）渠道用户的违约认定
①不如实提供资源信息、违规虚报粉丝量和刷粉；
②订单有效生成后，未按订单约定时间、位置、字数推发媒介内容；
③未经品牌用户的书面同意，擅自修改订单约定的产品及服务的内容；
④广告发布、公关发稿、合作活动所涉及的文字、图片、数据等违反相关法律法规，侵犯任何第三方的知识产权；
⑤其他违约情形。
2）品牌用户的违约认定
①不具备拟发布广告、信息涉及产品或服务所需的经营资质和行政许可；
②提供的信息、数据、图片等与实际情况严重不符，涉及虚假宣传；
③广告发布、公关发稿、合作活动所涉及的文字、图片、数据等违反相关法律法规，侵犯任何第三方的知识产权；
④其他违约情形。
3）违约处理措施
①用户有以上情形的，采媒在线可依据《采媒在线违规处罚规范》对其进行处罚。
②用户选择采媒在线平台的争议处理服务，采媒在线可依据《采媒在线纠纷处理机制规则》进行处理。
③用户选择其他方式解决争议。

第十二条 服务终止及处理
1、在下述情况下，采媒在线有权终止为其提供服务：
1）用户违反法律法规以及本协议的相关约定时；
2）用户与采媒在线约定的服务期限到期后未延长服务期；
3）用户与采媒在线协商一致解除合同关系的。
2、服务终止后的处理
1）服务终止后，采媒在线没有义务为用户保留原账户中或与之相关的任何信息，亦不就终止服务而对用户或任何第三方承担任何责任。
2）不论采媒在线与用户之间的服务因任何原因以任何方式终止，采媒在线仍有权：
① 保存或不保存该用户的数据及以前的交易行为记录；
②对于用户在服务终止前实施的违法或违约行为所导致的任何赔偿和责任， 用户必须完全独立地承担，采媒在线有追索权。
3）对于服务终止之前在采媒在线平台形成的交易行为依下列原则处理：
①服务终止之前，生成的订单尚未交易或尚未交易完成的，采媒在线有权在中断、终止服务的同时删除此项交易信息。
②服务终止之前，交易用户就具体交易达成一致，采媒在线有权选择是否删除该项交易，并有权将终止服务的情况通知用户的交易相对方。

第十三条 法律的适用、管辖和其他
1、本协议的订立、执行和解释及争议的解决均应适用中华人民共和国法律并受中国法院管辖。
2、本协议任一条款被视为废止、无效或不可执行，该条应视为可分的且并不影响本协议其余条款的有效性及可执行性。
3、因使用采媒在线平台服务所产生的有关争议，由采媒在线与用户友好协商解决；协商不成时，任何一方均可向采媒在线平台经营者所在地有管辖权的人民法院提起诉讼。
4、本协议的附件包括：《采媒在线违规处罚规范》、《采媒在线纠纷处理机制规则》。附件为本协议不可分割的一部分，与协议正文具有相同法律效力。

采媒在线争议处理机制规则
第一章 适用范围
第一条 为及时解决采媒在线平台交易争议事件，保护交易用户的合法权益，依据《采媒在线平台服务协议》制定本规范。
第二条 交易用户双方在采媒在线平台因交易发生争议和纠纷，任何一方向采媒在线投诉和申请维权处理的，适用本规则的规定，采媒在线将根据本规则对交易双方的争议做出处理。
   
第二章 交易争议处理
第三条 广告主提交的预约订单不符合《采媒在线平台服务协议》中的规定，采媒在线经审核后有权做出审核不通过的决定或要求广告主按要求作出修改和完善后再次提交审核。
第四条 广告主的预约订单经采媒在线平台审核通过后，在媒体未接受订单前，广告主要求删除订单的，采媒在线作撤单处理。
第五条 媒体接受订单后24小时内，广告主要求撤销订单的，采媒在线作撤单处理并将款项退回广告主的平台账户。但要求撤销订单的时间与要求发布媒介的时间不足24以内的订单，广告主不得要求撤单。双方另有约定的，按照约定处理。
第六条 媒体接受订单后未在约定的时间发布、投放的，广告主要求退款的，采媒在线将款项退回广告主的平台账户。
第七条 广告主执行验收后，提出媒体未按订单约定的内容（包括但不限于字数、图片、位置、规格等）发布和投放的，采媒在线认可广告主的主张，将款项退回广告主的平台账户。媒体提出相反证据主张已按订单内容完成的，采媒在线认可媒体的主张，将款项汇入媒体的账户。
第八条 交易双方因约定不清发生争议的，由此受到的损失由交易双方共同承担，承担比例由采媒在线根据具体情况判断。

第三章 争议处理程序
第九条 产生交易争议时，用户间可以选择自行协商，如任何一方提起交易争议申请后2个工作日内，双方协商未果的，则可以申请采媒在线客服介入处理；同时双方也可以通过司法途径等其他方式解决相关争议。
第十条 申请采媒在线客服介入的，采媒在线客服即有权根据本规则对争议进行处理。
第十一条 采媒在线处理争议期间，交易双方应当按照采媒在线平台系统的提示、发送的短信、电话或邮件通知及时提供凭证。
第十二条 采媒在线收集到双方提供的凭证后，将在采媒在线平台系统提示的时间内，按照本规则对相应争议做出处理；本规则没有明确规定的，由采媒在线依其独立判断做出处理。
任何一方无正当理由，未按照前款规定提供凭证的，采媒在线有权按照实际收集到的凭证做出处理。
第十三条 采媒在线作为独立第三方，仅对双方提交的凭证进行形式审查，并作出独立判断，双方自行对凭证的真实性、完整性、准确性和及时性负责，并承担不能举证的后果。
第十四条 采媒在线处理争议期间，争议双方自行达成退款协议，但无法自行操作的，采媒在线将根据双方达成的退款协议，通过平台系统将相应的交易款项汇入对方账户。
第十五条 采媒在线对争议做出处理后，有权将款项退回广告主的账户或汇入媒体的账户。
第十六条 采媒在线将基于普通人的判断，根据本规则的规定对交易双方的争议做出处理。采媒在线并非司法机关，对凭证、证据的鉴别能力及对争议的处理能力有限，采媒在线不保证争议处理结果符合全部用户的期望，也不对依据本规则做出的争议处理结果承担任何责任。

第四章 附则
第十七条 处理争议期间，采媒在线通过争议处理系统、电子邮件、短信或电话等方式向争议双方发送的与争议处理相关的提示或通知，构成本规则的有效组成部分。
第十八条 采媒在线有权根据国家相关法律法规、政策及平台运营情况对本规则进行更新、调整，并在网站上予以公告。用户不同意相关变更的，可自行协商或通过其他途径解决争议。
第十九条 采媒在线对争议做出处理后，不免除争议双方基于与采媒在线签署的其他协议、规则应当承担的责任。
         </div>
  </div>
  <div class="modal-footer">
    <button type="button" data-dismiss="modal" class="btn btn-danger" id="agree">同意</button>
  </div>
</div>
	

<script type="text/javascript">


jQuery(document).ready(function() {
	

	$('#agree').click(function(e) {
		 $("[name='agreement']:checkbox").prop('checked',true);
	});
	
	$('#treaty-btn').click(function(e) {
		$('#treaty').modal('show');
    });
	
	function init() {
		if( isAdvertiser() ) {
			$('.advertiser').show();
		}
		if( isProvider() ) {
			$('.advertiser').hide();
		}
	}

	//是否为媒体账户
	function isProvider() {
		return $('input[name="custType"]:eq(1)').is(':checked');
	}
	
	//是否为广告主
	function isAdvertiser() {
		return $('input[name="custType"]:eq(0)').is(':checked');
	}
  	
	$('#Advertiser').focus(function(e) {
		$('#adAdditional').show();
        
    });
	$('#Media').focus(function(e) {	
		$('#adAdditional').hide();
		$('#adAdditional input').val(null);
        
    });
	
	init(); //执行页面初始化
	
	// 客户类型 radio 单选事件
  	$('input[name="custType"]').click(function() {
  		if ( isAdvertiser() ) {
  			$('.advertiser').show();
  		} else if( isProvider() ) {
  			$('.advertiser').hide();
  			$('#name').val('');
  		}
  	});
	
	//>> --------------------------------- 表单验证规则
	$('#register-form').validate({
		debug: false,
		submitHandler: function(form) {
			common.disabled('#btn-submit');
			form.submit();
		}, 
		errorPlacement: function(error, element) {  
			   error.appendTo(element.parent().parent());
// 			   if($(element).attr('name')=='mobPhone'&&!whetherInvalid(element)&&$('#btn-smscode').text()=='获取短信验证码'){
// 				   $('#btn-smscode').attr({disabled: false});
// 				   $('#btn-smscode').removeClass("disabled");
// 			   }else{
// 				   common.disabled('#btn-smscode');
// 			   }
		},
		rules: {
			custType: {
				required: true
			},
			password: {
				required: true, 
				rangelength:[6,20]
				}, 
			checkPassword: {
				required: true, 
				equalTo: '#password'
				}, 
			name: {
				required: {
					depends: isAdvertiser
				}, 
				rangelength:[2,30], 
				remote: '${ctx}/common/checkCustomerName'
				},
			mobPhone: {
				required: true,
				digits:true,
				minlength:11,
				remote: '${ctx}/common/checkMobPhone'
			},
			smscode: {
				required: true,
				digits:true,
				minlength:6,
				remote: {
					url: '${ctx}/common/checkSmscode',
					type:'post',
					data: {
						mobPhone: function() {
							return $( "#mobPhone" ).val();
						},
						smscode: function() {
							return $( "#smscode" ).val();
						}
					}
				 }
			},
			agreement: {
				required:true
			}
		},
		messages: {
			custType: {
				required: '请选择会员类型'
			},
			password :{
				required: '请您输入密码！',
				rangelength : '密码应为6~20位字符！'
			},
			checkPassword: {
				required: '请您输入此前填写的密码！', 
				equalTo: '与此前填写的密码不一致！'
			}, 
			name: {
				required : '请您输入个人姓名或企业名称！',
				remote: '该名称已被注册',
				rangelength : '请输入2~30个字符！'
			},
			mobPhone: {
				required: '请您输入手机号！',
				remote: '此手机号码已经被注册！',
				minlength : '请输入11位数字的手机号码'
			},
			smscode: {
				required: '请您输入验证码！',
				remote: '短信验证码不正确！',
				minlength: '请输入6位数字验证码'
			},
			agreement: {
				required: '必须同意才能注册媒体方会员'	
			}
		}
	});	
	//<< --------------------------------- 表单验证规则
	
	//绑定短信验证码组件
	app.bindSmsCode('btn-smscode', 'mobPhone');
	
});

function whetherInvalid(element){
	var validate= $(element).parent().parent().attr('class').split(' ');
	for(var i=0;i<validate.length;i++){
		if(validate[i]=='has-error'){
			return true;
		}
	}
	return false;
}
</script> 
</body>
</html>
