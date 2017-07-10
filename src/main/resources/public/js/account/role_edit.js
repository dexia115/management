var chooseIds;
$(function() {
	formValidate("roleForm",{
		code: {
			required: true
		},
		name: {
			required: true
		}
	},{});
	chooseIds = [];
	$("#authorityIdsDiv").find("input").each(function(){
		chooseIds.push($(this).val());
	});
	var treeObj = loadSimpleTree("treeDemo","/authority/findAuthorityAllTree",true,function(treeNode){
		if(isnull(treeNode)){
			var nodes = treeObj.getNodesByParam("pId", null, null);
			for(var i=0;i<nodes.length;i++){
				if(chooseIds.indexOf(nodes[i].id)>-1){
					treeObj.checkNode(nodes[i], true, true);
				}
				treeNode = nodes[i];
				loopChoosed(treeNode,treeObj);
			}
		}
	});
});
function loopChoosed(treeNode,treeObj){
	if(!isnull(treeNode)){
		var nodes = treeObj.getNodesByParam("pId", treeNode.id, null);
		for(var i=0;i<nodes.length;i++){
			treeNode = nodes[i];
			loopChoosed(treeNode,treeObj);
			if(chooseIds.indexOf(nodes[i].id)>-1){
				treeObj.checkNode(nodes[i], true, true);
			}
		}
	}
}

function submitForm(){
	if($("#roleForm").valid()){
		$("#authorityIdsDiv").html("");
		var url = $("#roleForm").attr("action");
		url = url.substring(0,url.lastIndexOf("/")+1);
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		var nodes = treeObj.getCheckedNodes(true);
		for(var i=0;i<nodes.length;i++){
			var id = nodes[i].id;
			$("#authorityIdsDiv").append('<input type="hidden" name="authorityIds" value="'+id+'"/>');
		}
		mask();
		$("#roleForm").ajaxSubmit(function(data){
			unmask();
			if(data.success){
				siMenu('role_','角色管理',url);
				var id = $("#id").val();
				if(isnull(id)){
					top.mainFrame.tab.close('role_add');
				}else{
					top.mainFrame.tab.close('role_edit');
				}
			}else{
				alertInfo(data.message);
			}
		});
	}
	
}
		