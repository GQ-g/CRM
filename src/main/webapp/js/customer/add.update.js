layui.use(['table','layer',"form","http"],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table,
        form = layui.form,
        http = layui.http,
        path = http.path;

    //提交表单数据
    form.on('submit(addOrUpdateCustomer)',function(data){
        var index = layer.msg("数据提交中，请稍后...",{icon:16,time:false,shade:0.8});
        var url = path+"/customer/save";
        if($("input[name='id']").val()){
            url = path+"/customer/update";
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