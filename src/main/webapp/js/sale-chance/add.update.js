layui.use(['form', 'layer','http'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery
        http = layui.http;
	var path = http.path;

    $.post(path+"/user/queryAllSales",function(res){
        // alert(res);
        var assignMan = $("input[name='assignMan']").val();
        // console.log(assignMan);
        for(var i=0;i<res.length;i++){
            if($("input[name='man']").val() ==res[i].id){
                $("#assignMan").append("<option  value=\""+res[i].id+"\"  selected='selected'>"+res[i].uname+"</option>");
            } else {
                $("#assignMan").append("<option value=\""+res[i].id+"\">"+res[i].uname+"</option>");
            }
        }
        /*重新渲染下拉框内容*/
        form.render("select");
    })
    //提交数据
    form.on('submit(addOrUpdateSaleChance)',function(data){
        var index = layer.msg("数据提交中,请稍后",{icon:16,time:false,shade:0.8});
        var url = path+"/sale_chance/save";
        if($("input[name='id']").val()){
            url = path+"/sale_chance/update";
        }
        $.post(url,data.field,function(data){
            if(data.code==200){
                layer.msg("操作成功");
                layer.close(index);
                layer.closeAll("iframe");
                //刷新父页面
                parent.location.reload();
            }else {
                layer.close(index);
                layer.msg(data.msg);
            }
        },"json");
        return false;
    });
});