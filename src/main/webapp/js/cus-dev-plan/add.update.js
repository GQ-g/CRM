layui.use(['form', 'layer','laydate','http'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        laydate = layui.laydate
        $ = layui.jquery,
        http = layui.http;
     var path = http.path;

    //日期插件绑定
    laydate.render({
        elem:'#planDate'
    })
    //提交表单
    form.on('submit(addOrUpdateCusDevPlan)',function(data){
        var index = layer.msg("数据提交中，请稍后...",{icon:16,time:false,shade:0.8});
        var url = path+"/cus_dev_plan/save";
        if($("input[name='id']").val()){
            url = path+"/cus_dev_plan/update";
        }
        $.post(url,data.field,function(data){
            if(data.code==200){
                layer.msg(data.msg);
                layer.close(index);
                //layer.closeAll("iframe");
                //刷新父页面
                parent.location.reload();
            }else {
                layer.msg(data.msg);
            }
        })
        return false;
    })
});