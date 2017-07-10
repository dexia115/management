$(function() {
	userFormValidate = formValidate("userForm",{
		userName: {
			required: true
		},
		password: {
			required: true,
			password: true
		},
		repeatPassword: {
			required: true,
			password: true,
			equalTo: "#password"
		},
		roles: {
			required: true
		},
		mobile: {
			required: true,
			mobile: true,
			remote:{
				url:'/user/checkMobile',
				data:{
					'id':function() {
						var id = $('#id').val();
						if(!isnull(id)){
							id = Number(id);
						}else{
							id = 0;
						}
						return id;
					}
				}
			}
		},
		realName: {
			required: true,
			maxlength: 10,
			minlength: 2
		}
	},
	{
		mobile : {
			remote : "该电话已存在"
		},
		password:{
			required: "请输入密码"
		},
		repeatPassword:{
			required: "请再次输入密码"
		}
	});
});
function submitForm(){
	if($("#userForm").remoteCount()>0){
		alertInfo("正在远程校验中...");
		return false;
	}
	if($("#userForm").valid()){
		var url = $("#userForm").attr("action");
		var userUrl = url.substring(0,url.lastIndexOf("/")+1);
		mask();
		$("#userForm").ajaxSubmit(function(data){
			unmask();
			if(data.success){
				siMenu('user_','用户管理',userUrl);
				top.mainFrame.tab.close('user_add');
			}else{
				alertInfo(data.message);
			}
		});
	}
	
}