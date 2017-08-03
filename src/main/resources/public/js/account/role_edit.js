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
	
	
	var $overflow = '';
	var colorbox_params = {
		rel : 'colorbox',
		reposition : true,
		scalePhotos : true,
		scrolling : false,
		previous : '<i class="ace-icon fa fa-arrow-left"></i>',
		next : '<i class="ace-icon fa fa-arrow-right"></i>',
		close : '&times;',
		current : '{current} of {total}',
		maxWidth : '100%',
		maxHeight : '100%',
		speed : 500,
		loop : false,
		slideshow : true,
		slideshowAuto : false,// 自动滚动图片
		slideshowSpeed : 1000,// 设置时间，毫秒
		slideshowStart : '<button class="glyphicon glyphicon-play green" title="播放"></button>',
		slideshowStop : '<button class="glyphicon glyphicon-pause red" title="停止"></button>',
		onOpen : function() {
			$overflow = document.body.style.overflow;
			document.body.style.overflow = 'hidden';
		},
		onClosed : function() {
			document.body.style.overflow = $overflow;
			$('#picTab').animate({
				scrollTop : '0px'
			}, 100);
		},
		onComplete : function() {
			$.colorbox.resize();
		}
	};
	$('.ace-thumbnails [data-rel="colorbox"]').colorbox(colorbox_params);
	$('#roleForm input[type=file]').change(function(){
		var currentFile = $(this);
		var type = $(this).attr("name");
		var lable = $(this).parent();
		var files = this.files;
		mask("图片上传中...");
		fileTransferBase64(files,function(dataUrl,fileName){
			ajaxRequest("/files/uploadBase64File",{"base64Url":dataUrl,"fileName":fileName},function(data){
				unmask();
				currentFile.val("");
				if(data.success){
					var vo = data.resultObject;
					var image = vo.filePath;
					var original = image.replace("small_","");
					var imageUrlPrefix = vo.urlPrefix;
					var ul = lable.prev("ul");
					if(ul.length == 0){
						lable.before('<ul class="ace-thumbnails"></ul>');
						ul = lable.prev("ul");
					}
					ul.append('<li><a href="'+imageUrlPrefix+'/'+original+'" data-rel="colorbox" class="cboxElement"><img alt="150x150" src="'+imageUrlPrefix+'/'+image+'" width="150" height="150" /></a>'
							+'<div class="tools tools-bottom"><a href="javascript:void(-1)" title="删除"><i class="ace-icon fa fa-times red"></i></a></div></li>');
				}
			});
		});
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
		