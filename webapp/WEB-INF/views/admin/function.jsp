<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<html>
<head>
<title>系统功能管理</title>

<script type="text/javascript" src="${ctx}/static/legacy/js/jquery/jquery-ztree/js/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="${ctx}/static/legacy/js/jquery.ztree.helper.js"></script>
<script src="${ctx}/static/legacy/js/bootstrap-validation/validate.js" type="text/javascript"></script>
<script src="${ctx}/static/legacy/js/bootstrap-validation/messages_zh.js" type="text/javascript"></script>

<link rel="stylesheet" href="${ctx}/static/legacy/js/jquery/jquery-ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
	
<style type="text/css">
<!--
.ztree * {
	font-family: "Helvetica Neue", Helvetica, Arial, "Microsoft Yahei UI",
		simsun, sans-serif;
	font-size: 14px;
}

.ztree li {
	margin: 3px 0;
}

.ztree li a.curSelectedNode {
	height: 18px;
}

/*冻结根结节*/
.ztree li span.button.switch.level0 {
	visibility: hidden;
	width: 1px;
}

.ztree li ul.level0 {
	padding: 0;
	background: none;
}

/*根节点图标样式*/
.ztree li span.button.root_ico_open,.ztree li span.button.root_ico_close
	{
	width: 0px;
	height: 0px;
}


/*编辑按钮图标样式*/
.ztree li a span.button.edit
, .ztree li a span.button.remove
, .ztree li a span.button.add {
	margin-left: 10px;
	margin-right: -5px;
}
.ztree li span.button.add {
	background-position: -144px 0;
	vertical-align: top;
	*vertical-align: middle
}

.tree-container {
	border: #efefef 1px solid;
	overflow: auto;
}
-->
</style>
	
</head>

<body>

<div class="panel panel-default">

  <div class="panel-heading"><!-- 右侧标题 -->
    <ul class="breadcrumb">
        <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
        <li class="active">功能管理</li>
        <span id="loading" class="pull-right"><img src="${ctx }/static/legacy/img/loading.gif" /></span>
    </ul>
  </div><!-- / 右侧标题 -->
  
  
  <div class="panel-body"><!-- 右侧主体内容 -->
    <div class="row">
			<div class="col-md-4"><!-- 功能树 -->
			  <div class="tree-container">
				<ul id="funcTree" class="ztree"></ul>
			  </div>
			</div><!-- /功能树-->
			
			<div class="col-md-8" id="func-form" ></div>
		
		</div>
	</div>

</div>

<form id="actionForm" action="#" method="post"></form>

<script type="text/javascript" >

var func = {};

/*
 * 调整包含树的容器的高度.
 */
func.adjustHeight = function() {
	  var ph = document.body.clientHeight;
	  
	  var $tc = $('.tree-container');
	  var $footer = $('#footer');
	  var fh = 0;
	  if ($footer.length) {
	    fh = $footer.outerHeight();
	  }
	  if ($tc.size() > 0) {
	    var th = ph - $tc.offset().top - fh - 38;
	    $tc.height(th);
	  }
};

/*
 * 初始化zTree.
 * @tid zTree的实例容器#id
 * @opt Ztree设置项.
 */
func.initTree = function(tid, opt) {
    //$.fn.zTree.destroy(tid);
    return $.fn.zTree.init($('#' + tid), opt);
};

func.delFunction =  function (id, name) {
	var node = zTree.getZTreeObj('funcTree').getNodeByParam('id', id);
	if (!!node && node.isParent) {
		common.showMessage('无法删除父功能！', 'warn');
		return;
	}
    bootbox.confirm('您确定要删除 "'+ name + '" 功能吗？', function(result) {
        if(result) {
            var $form = $('#actionForm');
            $form.attr('action', ctx + '/admin/func/delete/' + id);
            $form[0].submit();
            common.loading();
        }
    });
};

$(function() {
	//左侧菜单高亮显示
	menu.active('#func-man');
	
	func.adjustHeight();

	/************************ 配置功能树 ************************/
	var settings = new TreeSetting(ctx + '/admin/func/tree');
	$.extend(true, settings, {
        view: {
            addHoverDom: addHoverDom,
            removeHoverDom: removeHoverDom,
            selectedMulti: false
        },		
	    edit: {
		  enable: true,
		  editNameSelectAll: true,
		  removeTitle: '删除',
		  renameTitle: '编辑',
		  showRemoveBtn: function (treeId, treeNode) {
		      return !treeNode.isParent;
		  },
		  showRenameBtn: function (treeId, treeNode) {
		      return false;
		  },
		  drag: {
			  isCopy: false
		  }
		},
		
		async: {
		  dataFilter: dataFilter
		  , autoParam: ["id=pid"]
		},
		
		callback: {
		  onAsyncSuccess: onAsyncSuccess
		  , onClick: onClick
		  , beforeRemove: beforeRemove
		  , onDrop : onDrop
		}
	});

	
	function dataFilter(treeId, parentNode, childNodes) {
        if (!childNodes) return null;
        for (var i=0, l=childNodes.length; i<l; i++) {
          if (childNodes[i].id == '0') {
              //修改根节点的样式
              childNodes[i].iconSkin = 'root';
          }
        }
        return childNodes;
	}
	
	function onAsyncSuccess(event, treeId, treeNode, msg) {
        if (!!treeNode) {
            asyncNodes(treeNode.children);
        } else {
            asyncNodes(funcTree.getNodes());
        }
    }
	
	function onClick(event, treeId, treeNode, clickFlag) {
		loadForm(treeNode, false);
	}
	
	function beforeRemove(treeId, treeNode) {
		func.delFunction(treeNode.id, treeNode.name);
		return false;
	}
	
	function loadForm(treeNode, addFlag) {
		$('#func-form').empty();
		
		var url = ctx + '/admin/func/form?ajax';
		if (addFlag)
		    $('#func-form').load(url, {pId:treeNode.id});
		else
		    $('#func-form').load(url, {id:treeNode.id});
	}
	
	function onDrop(event, treeId, treeNodes, targetNode, moveType, isCopy) {
		var node = treeNodes[0];
		var pId = node.pId;
		var seqNum = node.data.seqNum;
		
		if (moveType == 'inner') {
			pId = targetNode.id;
		} else if (moveType == 'prev' || moveType == 'next') {
			pId = targetNode.pId;
			seqNum = moveType == 'prev' ? targetNode.data.seqNum -1 : targetNode.data.seqNum + 1;
		}
		
		if (pId != node.pId || seqNum != node.seqNum) {
			$.post(ctx + "/admin/func/change", {id:node.id, pId: pId, seqNum: seqNum}, function(data) {
				common.log(data);
			});
		}
	}
	
    /*
    * 异步加载并展开子节点.
    */
	function asyncNodes(nodes) {
	   if (!nodes) return;
	   var zTree = funcTree;
	   for (var i=0, l=nodes.length; i<l; i++) {
	     if (nodes[i].isParent && nodes[i].zAsync) {
	       asyncNodes(nodes[i].children);
	     } else {
	      if (nodes[i].level < 2)
	       zTree.reAsyncChildNodes(nodes[i], "refresh", false);
	     }
	   }
	 }
    
    //----------- 添加自定义的编辑按钮 ----------
	function addHoverDom(treeId, treeNode) {
	   var sObj = $("#" + treeNode.tId + "_span");
	   if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
	   var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
	     + "' title='添加' onfocus='this.blur();'></span>";
	   sObj.after(addStr);
	   var btn = $("#addBtn_"+treeNode.tId);
	   if (btn) btn.bind("click", function(){
		   loadForm(treeNode, true);
	       return false;
	   });
	 };
   
	 function removeHoverDom(treeId, treeNode) {
	   $("#addBtn_"+treeNode.tId).unbind().remove();
	 };	
	//----------- /添加自定义的编辑按钮 ----------
	
	/************************ 配置功能树 end ************************/
	
	//初始化zTree
	var funcTree = func.initTree('funcTree', settings);

});
</script>

</body>
</html>
