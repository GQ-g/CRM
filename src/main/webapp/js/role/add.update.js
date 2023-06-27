layui.use(['form', 'layer','http'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        http = layui.http;

    var path = http.path;

    //表单提交
    form.on('submit(addOrUpdateRole)',function(data){
        alert(data.field);
        var index = layer.msg("数据提交中，请稍后...",{icon:16,time:false,shade:0.8});
        var url = path+"/role/save";
        if($("input[name='id']").val()){
            url = path+"/role/update";
        }
        $.post(url,data.field,function(data){
            if(data.code==200){
                layer.msg(data.msg);
                layer.close(index);
                layer.closeAll("iframe");
                //刷新父页面
                parent.location.reload();
            }else{
                layer.msg(data.msg);
            }
        },"json")

        return false;
    })
    
});