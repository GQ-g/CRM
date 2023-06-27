//页面加载事件
$(function(){
    loadModuleInfo();
})
var path = "/crm";
var zTreeObj;
function loadModuleInfo(){
    $.ajax({
        type:'post',
        url:path+"/module/queryAllModules?roleId="+$("input[name='roleId']").val(),
        dataType:'json',
        success:function(data){
            var setting = {
                check: {
                    enable: true
                },
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                callback: {
                    onCheck: zTreeOnCheck
                }
            };
            zTreeObj = $.fn.zTree.init($("#test1"), setting, data);
        }
    })
}
function zTreeOnCheck() {
    //获得选中的节点
   var nodes = zTreeObj.getCheckedNodes(true);
  /* console.log(nodes);*/
   //拼接选中的菜单ids
    var mids = "mids=";
    for(var i=0;i<nodes.length;i++){
        if(i<nodes.length-1){
            mids=mids+nodes[i].id+"&mids=";
        }else {
            mids=mids+nodes[i].id;
        }
    }
    console.log(mids);
    $.ajax({
        type:'post',
        url:path+"/role/addGrant",
        data:mids+"&roleId="+$("input[name='roleId']").val(),
        dataType:'json',
        success:function(data){
            if(data.code==200){
                alert("授权成功");
            }
        }
    })
};
