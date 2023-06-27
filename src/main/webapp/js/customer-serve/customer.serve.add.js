layui.use(['table','layer',"form","http"],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table,
        form = layui.form,
        http = layui.http,
        path = http.path;

    //提交表单
    form.on('submit(addCustomerServe)',function (data) {
        console.log(data.field);
        console.log(data);
        var index= top.layer.msg("数据提交中,请稍后...",{icon:16,time:false,shade:0.8});
        var url = path+"/customer_serve/save";
        /*通过前端获取缓存的方式设置用户值*/
        $.post(url,data.field,function (res) {
            if(res.code==200){
                top.layer.msg("操作成功");
                top.layer.close(index);
                layer.closeAll("iframe");
                // 刷新父页面
                parent.location.reload();
            }else{
                layer.msg(res.msg);
            }
        },"json");
        return false;
    });



});