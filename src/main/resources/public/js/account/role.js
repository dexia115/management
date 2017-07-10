var roleTable;
$(function(){
	var editAuth = $("#editAuth").val();
	var objData = [{
		"title" : "ID",
		"class" : "center",
		"sortable" : true,
		"visible" : true,
		"width" : "80px",
		"data" : 'id'
	}, {
		"title" : "角色名称",
		"class" : "center",
		"sortable" : true,
		"data" : 'name'
	}, {
		"title" : "角色标识",
		"class" : "center",
		"sortable" : false,
		"data" : 'code'
	}, {
		"title" : "权限",
		"class" : "center",
		"sortable" : false,
		"data" : 'authStr'
	}, {
		"title" : "状态",
		"class" : "center",
		"sortable" : false,
		"visible" : true,
		"data" : 'enable',
		"width" : '150px',
		"mRender":function (data, display, row) {
			if(!data){
				return "<span class='red'>停用</span>";
			}else{
				return "启用";
			}
		}
	},{ "sortable": false,"data":"id","class": "left","title":"操作",
		   "mRender":function (data, display, row) {
//			   if(isnull(editAuth)){
//				   return "暂无编辑权限！";
//			   }
			   return '<div class="action-buttons"><a class="blue" href="javascript:void(-1);" onclick=siMenu("role_edit","编辑角色","/role/edit?id='+data+'") title="编辑"><i class="ace-icon fa fa-pencil bigger-160"></i></a></div>';
			}
		}];
	roleTable = initTables("roleTable", "loadRole", objData, false,false,null, function() {
	});
});
function search(){
	searchButton(roleTable);
}