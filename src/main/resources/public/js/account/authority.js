var lastRowId,subgridTableId,parentId;
var secondTable,secondId;
var authorityValidate;
$(function(){
	var objList = [];
	var colNames = ['id','','','','权限名称','权限代码','类型','图标','访问路径','排序','操作'];
	var colModels = [
	     	{name:'id',index:'id', sortable:true, width:30,editable: true,hidden:true},
	     	{name:'method',sortable:true, editable: true,hidden:true},
	     	{name:'parentId',sortable:true, editable: true,hidden:true},
	     	{name:'parentName',sortable:true, editable: true,hidden:true},
	     	{name:'name',sortable:false, width:60,editable: true},
	     	{name:'code',sortable:false,editable: true},
	     	{name:'methodName',sortable:false,editable: true,formatter:function(cellvalue, options, rowObject){
	     		var result = "菜单";
	     		if(rowObject.method==2){
	     			result = "权限";
	     		}
	     		return result;
	     	}},
	     	{name:'icons',sortable:false,editable: true},
	     	{name:'url',sortable:false,editable: true},
	     	{name:'sort',sortable:true,editable: true},
	     	{name:'operate',sortable:false,editable: true,width:60,formatter:function(cellvalue, options, rowObject){
	     		var id = rowObject.id;
	     		var delBtn = '<a class="blue" href="javascript:void(-1);" onclick=deleteAuthority('+id+',this) title="删除" ><i class="ace-icon fa fa-trash red bigger-150"></i></a>';
	     		return '<div class="action-buttons"><a class="blue" href="javascript:void(-1);" onclick=editAuthority('+id+',this) title="编辑"><i class="ace-icon fa fa-pencil bigger-160"></i></a>'+delBtn+'</div>';
	     	}}
	    ];
	var url = "loadAuthority";
	var object = {};
	object.url = url;
	object.colNames = colNames;
	object.colModels = colModels;
	objList.push(object);
	object = {};
	object.url = url;
	object.colNames = colNames;
	object.colModels = colModels;
	objList.push(object);
	object = {};
	object.url = url;
	object.colNames = colNames;
	object.colModels = colModels;
	objList.push(object);
	createSubGrid("itemGrid","itemGridPager",objList,0,lastRowId,function(rowId,tableId,num){
		parentId = rowId;
		subgridTableId = tableId;
		if(num==1){
			secondTable = subgridTableId;
			secondId = parentId;
			$("#levels").val(2);
		}else if(num==2){
			$("#levels").val(3);
		}else{
			$("#levels").val(1);
		}
	},function(num){
		if(num==1){
			$("#levels").val(2);
		}else if(num==2){
			$("#levels").val(3);
		}else{
			$("#levels").val(1);
		}
	});
	
	createSpinner($("#sort"),1,100);
	authorityValidate = formValidate("authorityForm",{
		name: {
			required: true
		},
		code: {
			required: true
		},
		sort: {
			digits: true
		}
	},{});
});

function search(){
	var searchs = getSearchGroup();
	var str = "[" + searchs.join(",") + "]";
	str = encodeURI(str);
	var obj = $("#itemGrid");
	var levels = $("#levels").val();
	var other = "";
	if(!isnull(subgridTableId)){
		if(levels == 2){
			obj = $("#"+secondTable);
			other = "&id="+secondId;
		}else if(levels == 3){
			obj = $("#"+subgridTableId);
			other = "&id="+parentId;
		}
	}
	var url = "/authority/loadAuthority?params="+str;
	if(!isnull(other)){
		url += other;
	}
	obj.jqGrid("setGridParam", {
		url : url
	});
	obj.jqGrid("setGridParam", {
		search : true
	}).trigger("reloadGrid", [{
				page : 1
			}]);
}

function addAuthority(){
	authorityValidate.resetForm();
	$('#authorityForm').find('.form-group').each(function(){
		$(this).removeClass('has-error');
	});
	$("#parentId").val("");
	var levels = $("#levels").val();
	var itemGrid = $("#itemGrid");
	if(!isnull(subgridTableId)){
		if(levels == 2){
			itemGrid = $("#"+secondTable);
		}else if(levels == 3){
			itemGrid = $("#"+subgridTableId);
		}
	}
	openDialog("authorityDialog","authorityDialogDiv","添加权限",700,580,true,"提交",function(){
		if($("#authorityForm").valid()){
			$("#authorityForm").attr("action","add");
			mask();
			$("#authorityForm").ajaxSubmit(function(data){
				unmask();
				if(data.success){
					$("#authorityDialog").close();
					reloadGrid(itemGrid);
				}else{
					alertInfo(data.message);
				}
			});
		}
	});
}
function editAuthority(id,obj){
	authorityValidate.resetForm();
	$('#authorityForm').find('.form-group').each(function(){
		$(this).removeClass('has-error');
	});
	$("#parentId").val("");
	var levels = $("#levels").val();
	var trObj = $(obj).parents("tr");
	var itemGrid = trObj.parents("table");
	
	var rowData = itemGrid.jqGrid("getRowData", id);
	$("#id").val(rowData.id);
	$("#name").val(rowData.name);
	$("#code").val(rowData.code);
	$("#url").val(rowData.url);
	$("#method").val(rowData.method);
	$("#sort").val(rowData.sort);
	$("#icons").val(rowData.icons);
	var parentId = rowData.parentId;
	if(!isnull(parentId)){
		$("#parentId").val(parentId);
	}
	var parentName = rowData.parentName;
	if(!isnull(parentName)){
		$("#parentName").val(parentName);
	}
	openDialog("authorityDialog","authorityDialogDiv","编辑权限",700,580,true,"提交",function(){
		if($("#authorityForm").valid()){
			$("#authorityForm").attr("action","edit");
			mask();
			$("#authorityForm").ajaxSubmit(function(data){
				unmask();
				if(data.success){
					$("#authorityDialog").close();
					reloadGrid(itemGrid);
				}else{
					alertInfo(data.message);
				}
			});
		}
	});
}
var treeObj;
function showTree1() {
	treeObj = loadSimpleTree("treeDemo","findAuthorityForTree",false,function(){
		showMenu($("#parentName"), $("#zTreeDemoBackground"));
	},function(treeNode){
		$("#parentId").val(treeNode.id);		
		$("#parentName").val(treeNode.name);
		hideMenu($("#zTreeDemoBackground"));
	});
}
function closeZtreeDemo(){
	hideMenu($("#zTreeDemoBackground"));
}
function clearParentData(){
	$("#parentId").val("");
	$("#parentName").val("");
}

function deleteAuthority(id,obj){
	window.top.confirmDialog(function() {
		var trObj = $(obj).parents("tr");
		var itemGrid = trObj.parents("table");
		mask();
		ajaxRequest("delete",{"id":id},function(data){
			unmask();
			if(data.success){
				reloadGrid(itemGrid);
			}else{
				alertInfo(data.message);
			}
		});
	}, "删除权限操作", "请确定是否要删除权限？");
}

