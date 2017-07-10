//菜单状态切换
function siMenu(id,MENU_NAME,MENU_URL){
	top.mainFrame.tabAddHandler(id,MENU_NAME,MENU_URL);
}

var pageIndex = 0;
var pageSize = 10;
var totalRecords = 0;
var sSearch = '';
function loadSystemsUsers(iDisplayStart,sSearch){
	ajaxRequest("/user/loadUser",{"iDisplayLength":pageSize,"iDisplayStart":iDisplayStart,"sSearch":sSearch},function(data){
		pageIndex = iDisplayStart;
		totalRecords = data.iTotalRecords;
		var template = $.templates('{{for aaData}}<div class="profile-activity clearfix">'
				+'<div><a class="user" onclick=showDetails("{{>userName}}") href="#">'
				+'<img class="pull-left" src="/js/bootstrap/assets/avatars/avatar5.png" /> {{>userName}} </a></div></div>{{/for}}');
		var html = template.render(data);
		$("#systemUsers").append(html);
	});
}
function searchUsers(){
	var searchKeyword = $("#searchKeyword").val();
	sSearch = '';
	$("#systemUsers").html("");
	if(!isnull(searchKeyword)){
		sSearch = '[{"tempMatchType":"5","propertyName":"userName","propertyValue1":"'+searchKeyword+'","tempType":"String"}]';
	}
	loadSystemsUsers(0,sSearch);
}
$(function(){
	$("#posts-content").scroll(function() {
		var scrollTop = $(this).scrollTop();
		if (scrollTop<=0){
			//alert("滚动条已经到达顶部为0");
		}
		var viewH =$(this).height();//可见高度  
        var contentH =$(this).get(0).scrollHeight;//内容高度  
		if (contentH - viewH - scrollTop <= 1) {
			nextPage();
		}
	});
});
function nextPage(){
	var index = pageIndex + pageSize;
	if(index >= totalRecords){
		return;
	}
	loadSystemsUsers(index,sSearch);
}
function showMessage() {
	loadSystemsUsers(0,"");
	$("#systemUsers").html("");
	openDialog("messageDialog", "messageDialogDiv", "站内通讯", 700, 670, true,"发送", function() {
		var msgContent = $("#msgContent").val();
		if(!isnull(msgContent)){
			$("#messageListDiv").append('<div class="profile-activity clearfix"><div class="left-per-40"><img class="pull-left" src="/js/bootstrap/assets/avatars/avatar5.png" />'
					+' <a class="user" href="#"> Alex Doe </a>'+msgContent+'</div></div>');
			$("#msgContent").val("");
			var h = $("#messageListDiv").height();
			if(h>280){
				$("#messageList").animate({scrollTop: h}, 1);
			}
		}
	});
}

function showDetails(userName){
	$("#currentUser").html(userName);
	var h = $("#messageListDiv").height();
	if(h>280){
		$("#messageList").animate({scrollTop: h}, 1);
	}
}