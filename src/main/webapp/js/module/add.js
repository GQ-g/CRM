layui.use(['form', 'layer','http'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        http = layui.http,
        path = http.path;

    form.on('submit(addModule)',function(data){
        var index = layer.msg("数据提交中，请稍后...",{icon:16,time:false,shade:0.8});
        $.post(path+"/module/save",data.field,function(data){
            if(data.code==200){
                layer.msg(data.msg);
                layer.close(index);
                layer.closeAll("iframe");
                parent.location.reload();
            }else {
                layer.msg(data.msg);
            }
        },"json");
        return false;
    })
   
});