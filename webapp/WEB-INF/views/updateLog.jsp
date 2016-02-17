<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>更新日志</title>
    <style>
		.logs { padding:15px; margin-bottom:20px; box-shadow: 0 3px 0px #ccc; border:1px solid #ddd; border-radius:8px!important; color:#000!important;}
		.logs li { line-height:26px;}
		.logs hr { border-style:dotted;}
		.logs h2, .logs h4, .logs h5 {
			font-weight:bold;
		}
		.logs h2, .logs h3, .logs h4, .logs h5 {
			color:#333;
		}
		.logs h2 {
			margin-bottom:40px;
		}
		.logs h4 {
			font-size:16px;
			color:#333;
		}
	</style>
</head>

<body>
<!--=== End Header v1 ===-->
<div class="breadcrumbs">
	<div class="container">
    	<div class="row">
        <ul class="breadcrumb">
        	<li>当前位置：</li>
        	<li><a href="home.html">首页</a></li>
        	<li>更新日志</li>
        </ul>
        </div>
    </div>
</div>

<!--=== Content ===-->
<div class="container">
	<div class="row">
    	<h2><strong>采媒在线网站更新日志</strong></h2>
    	<div class="logs">
        	<h4>2015.12.8公测第一版（1.1）发布</h4>
            <hr class="hr-xs">
            <h5>新增功能</h5>
            <ol>
            <li>新增“官方媒体”频道<p>收录具有官方属性的微信、微博、电视、报纸、杂志媒体</p></li>
			<li>实现“搜索框”算法<p>关键词搜索将按照媒体名称、行业、标签、简介等优先级呈现搜索结果</p></li>
            <li>首页全新改版</li>
			<li>新增“新手教程”（媒体版）</li>
            </ol>
            <h5>功能改进</h5>
            <ol>
            <li>“注册成功”页增加用户引导：返回首页、进入会员中心</li>
			<li>首页改版
            	<ol style="list-style:lower-alpha">
                	<li>树状导航增加“媒体标签”</li>
                    <li>增加“登陆框”</li>
                    <li>导航栏新增：“官方媒体”，“更多媒体”</li>
                    <li>单一“搜索框”修改为“分类搜索”</li>
                    <li>“成功案例”修改为“翻页展示”</li>
                    <li>新增“合作伙伴”</li>
                    <li>树状导航新增“滑动详情浮层”</li>
                    <li>“微信频道”、“微博频道”新增“媒体标签云”</li>
                </ol>
            </li>
			<li>全面更新了产品文案、交互术语</li>
            </ol>
        </div>
        
        <div class="logs">
        	<h4>2015.12.01  内测第六版（1.0.4）发布</h4>
            <hr class="hr-xs">
            <h5>新增功能</h5>
            <ol>
            	<li>新增“更多媒体”频道
                	<p>新增“电视广告”，“门户网站广告”，“视频网站广告”，“APP广告”，“报纸广告”，“户外广告”，“杂志广告”，“院线广告”八类媒体</p>
                </li>
                <li>微信媒体新增“微信号”字段</li>
                <li>微信、微博媒体详情页新增逐个“服务类别”的图片介绍</li>
            </ol>
            <h5>功能改进</h5>
            <ol>
			<li>改版“广告悬赏”列表页
           		<ol style="list-style:lower-alpha">
                	<li>新增“排序条件”</li>
                    <li>新增“需求详情”信息模块</li>
                    <li>新增“下拉”示意键</li>
                    <li>筛选条件“酬金”修改为“预算”</li>
                </ol>
            </li>
            </ol>
        </div>
        
        <div class="logs">
        	<h4>2015.11.24  内测第五版（1.0.3）发布</h4>
            <hr class="hr-xs">
            <h5>新增功能</h5>
            <ol>
            	<li>新增“主管机构”用户中心，可对所辖媒体进行操作和管理</li>
				<li>自媒体创建时不再提示选择“主管机构”，改由系统统一指配</li>
            </ol>
            <h5>功能改进</h5>
            <ol>
			<li>改版“微信频道”、“微博频道”列表页
           		<ol style="list-style:lower-alpha;">
                	<li>新增“排序条件”</li>
                    <li>更新“媒体信息模块”样式及交互</li>
                    <li>更新“服务报价”样式及交互</li>
                    <li>新增“滑动详情浮层”</li>
                    <li>解决了媒体字段折行问题</li>
                </ol>
            </li>
            </ol>
        </div>
        
        <div class="logs">
        	<h4>2015.11.17  内测第四版（1.0.2）发布</h4>
            <hr class="hr-xs">
			<h5>新增功能：</h5>
            <ol>
            	<li>需求“开始时间”和“结束时间”合并为为“发布时间”</li>
                <li>注册页新增：《采媒在线用户协议》</li>                
            </ol>
            <h5>功能改进</h5>
            <ol>
                <li>优化媒体详情页，界面展现更美观
                    <ol style="list-style:lower-alpha">
                        <li>更新“服务报价”样式</li>
                        <li>鼠标悬停在服务报价的任一“服务缩略图”上在该图下方展示大图</li>
                        <li>“媒体简介”、“服务报价”、 “案例”三项分页改为整页显示，点击页卡跳转至对应位置锚链接</li>
                        <li>服务报价选中方式更新：报价数字修改为按键，上下方同步选中</li>
                        <li>解决了媒体信息为空时的留白问题：调整各模块分割线（媒体信息模块、简介模块、服务报价模块、案例模块、尾部文件）跟本模块内容最后一行自适应对齐</li>
                        <li>调整页面布局，优化界面</li>
                    </ol>
                </li>
                <li>网站所有交互提示框移至屏幕居中显示</li>
                <li>更新了首页交易数据模块样式及数据组成</li>
            </ol>
        </div>
        
        <div class="logs">
        	<h4>2015.11.10  内测第三版（1.0.1）发布</h4>
            <hr class="hr-xs">
            <h5>新增功能</h5>
            <ol>
                <li>上传“媒体LOGO”的裁剪功能</li>
                <li>“消息”全部加入用户指引操作的跳转链接</li>
                <li>创建媒体时新增字段：微信粉丝截图</li>
                <li>媒体上传案例时增加了“添加图片”选项，添加图片大小及格式要求。</li>
                <li>用户信息“修改”和“保存”作位置区分</li>
            </ol>
            <h5>功能改进</h5>
            <ol>
            	<li>改版媒体用户中心
                    <ol style="list-style:lower-alpha;">
                        <li>所有弹出的“提示框”设置为居中显示</li>
                        <li>创建媒体，选择“适用产品”，“行业类型”，“粉丝方向”，“地区”时，添加了提示语：可多选</li>
                        <li>媒体填写报价时，增加提示语“元”</li>
                    </ol>
                <li>优化了首页成功案例模块的案例展示效果</li>
			</ol>
        </div>
        
        <div class="logs">
        	<h4>2015.11.03  内测第二版（1.0）发布</h4>
            <hr class="hr-xs">
            <h5>新增功能</h5>
            <ol>
            	<li>更新了媒体系统推荐方案算法，提高推荐媒体匹配度</li>
                <li>登录页面增加了“忘记密码”功能</li>
            </ol>
            <h5>功能改进</h5>
            <ol>
            	<li>广告主用户中心
                	<ol style="list-style:lower-alpha;">
                    	<li>“消息”全部加链接，点击进入相应页面</li>
                        <li>所有弹出的“提示框”居中显示</li>
                        <li>选择媒体类别，地区，行业类型，添加“可多选”提示语</li>
                    </ol>
                </li>
                <li>15分钟未操作，账户自动退出，提高安全性</li>
                <li>“会员中心-消息”新增了未读消息数量的数字提示</li>
                <li>新增了会员中心“实名认证进度条”的滚动效果</li>
            </ol>
        </div>
        
        <div class="logs">
        	<h4>2015.10.29  内测第一版（beta版）发布</h4>
            <p>采媒在线PC端第一个版本</p>
        </div>

        
    	<div class="logs hide">
    		<h2>采媒在线网站更新日志</h2>
            <h4>MLF 1.2 版本</h4>
            <hr class="hr-xs">
            <ol>
            <li>更新：取消加密，现在可以自己折腾了；</li>
            <li>修复：侧边栏偶尔错位问题；</li>
            <li>修复：404页面图片不显示；</li>
            <li>修复：文章页插入视频自适应宽高；</li>
            <li>更新：文章页视频居中展示；</li>
            <li>修复：手机端文章页面标题下分类不显示的问题；</li>
            <li>更新：文章页面内容分页模块；</li>
            <li>修复：多个位置的头像变形；</li>
            <li>更新：登录或注册弹框显示后，点击透明遮罩可关闭；</li>
            <li>更新：登录或注册弹框显示后，第一个输入框获取焦点，可直接输入；</li>
            <li>更新：登录或注册弹框输入框内按CTRL键将自动提交并验证表单；</li>
            <li>更新：登录支持用户名和邮箱；</li>
            <li>更新：注册时发送密码至注册邮箱，以验证邮箱有效性；</li>
            <li>更新：会员中心关于registration.php文件的引用的错误；</li>
            <li>更新：会员中心关于PRC和escape的错误；</li>
            <li>更新：会员中心修改资料页面调整，邮箱不可修改；</li>
            <li>更新：站点标题过长引起的LOGO宽度问题；</li>
            <li>更新：去除链接focus时的虚线；</li>
            <li>更新：分享模块的文字错位问题；</li>
            </ol>
            <h4>MLF 1.1 版本</h4>
            <hr class="hr-xs">
            <ol>
            <li>更新：取消加密，现在可以自己折腾了；</li>
            <li>修复：侧边栏偶尔错位问题；</li>
            <li>修复：404页面图片不显示；</li>
            <li>修复：文章页插入视频自适应宽高；</li>
            <li>更新：文章页视频居中展示；</li>
            <li>修复：手机端文章页面标题下分类不显示的问题；</li>
            <li>更新：文章页面内容分页模块；</li>
            <li>修复：多个位置的头像变形；</li>
            <li>更新：登录或注册弹框显示后，点击透明遮罩可关闭；</li>
            <li>更新：登录或注册弹框显示后，第一个输入框获取焦点，可直接输入；</li>
            <li>更新：登录或注册弹框输入框内按CTRL键将自动提交并验证表单；</li>
            <li>更新：登录支持用户名和邮箱；</li>
            <li>更新：注册时发送密码至注册邮箱，以验证邮箱有效性；</li>
            <li>更新：会员中心关于registration.php文件的引用的错误；</li>
            <li>更新：会员中心关于PRC和escape的错误；</li>
            <li>更新：会员中心修改资料页面调整，邮箱不可修改；</li>
            <li>更新：站点标题过长引起的LOGO宽度问题；</li>
            <li>更新：去除链接focus时的虚线；</li>
            <li>更新：分享模块的文字错位问题；</li>
            </ol>
        </div>
    </div>
</div>    
<!--=== End Content ===-->


</body>
</html>

