var userTable;
var userFormValidate;
$(function(){
var objData = [
    {'title' : 'ID','class' : 'center','width' : '80px','data' : 'id'},
    {'title':'用户名','class':'center','sortable':false,'data' : 'userName'},
    {'title':'联系电话','class':'center','sortable':false,'data' : 'mobile'},
    {'title':'姓名','class':'center','sortable':false,'data' : 'realName'},
    {'title':'角色','class':'center','sortable':false,'data' : 'roleStr'},
    {'title':'创建时间','class':'center','sortable':false,'data' : 'createTime'},
    {"title" : "状态","class" : "center","sortable" : false,"data" : 'enable',"width" : '100px',
		"mRender":function (data, display, row) {
			if(!data){
				return "<span class='red'>停用</span>";
			}else{
				return "启用";
			}
		}
	},
    { "sortable": false,"data":"id","class": "left","title":"操作",
		   "mRender":function (data, display, row) {
//			   if(isnull(editAuth)){
//				   return "暂无编辑权限！";
//			   }
			   return '<div class="action-buttons"><a class="blue" href="javascript:void(-1);" onclick=siMenu("user_edit","编辑用户","/user/edit?id='+data+'") title="编辑"><i class="ace-icon fa fa-pencil bigger-160"></i></a>'+
			   '<a class="blue" href="javascript:void(-1);" title="修改密码" onclick="editDialog(this)"><i class="ace-icon fa fa-key bigger-160"></i></a></div>';
 			}
 		}  
];
    userTable = initTables("userTable", "loadUser", objData, false,false,null, function() {});
    userFormValidate = formValidate("userForm",{
		password: {
			required: true,
			password: true
		},
		repeatPassword: {
			required: true,
			password: true,
			equalTo: "#password"
		},
		realName: {
			required: true
		}
	},{
		password:{
			required: "请输入密码"
		},
		repeatPassword:{
			required: "请再次输入密码"
		}
	});

});
function search(){
    searchButton(userTable);
}
function editDialog(obj){
	userFormValidate.resetForm();
	$('#userForm').find('.form-group').each(function(){
		$(this).removeClass('has-error');
	});
	var oo = $(obj).parents("tr");
	var aData = userTable.fnGetData(oo); // get datarow
	$("#id").val(aData.id);
	$("#realName").val(aData.realName);
	openDialog("userDialog","userDialogDiv","修改密码",700,380,true,"提交",function(){
		if($("#userForm").valid()){
			mask();
			$("#userForm").ajaxSubmit(function(data){
				unmask();
				if(data.success){
					$("#userDialog").close();
				}
				alertInfo(data.message);
			});
		}
	});
}
