layui.use(['form', 'layer','formSelects','http'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        http = layui.http,
        formSelects = layui.formSelects;
    var path = http.path;

    // 角色数据回显
    var userId = $("input[name='id']").val();
    formSelects.config('selectId',{
        type:'post',
        searchUrl:path+"/role/queryAllRoles?userId="+userId,
        keyName: 'roleName',            //自定义返回数据中name的key, 默认 name
        keyVal: 'id',//自定义返回数据中value的key, 默认 value
        keySel: 'selected'
    },true);

    //表单提交
    form.on('submit(addOrUpdateUser)',function(data){
        var index = layer.msg("数据提交中，请稍后...",{icon:16,time:false,shade:0.8});
        var url = path+"/user/save";
        if($("input[name='id']").val()){
            url = path+"/user/update";
        }
        $.post(url,data.field,function(data){
            if(data.code==200){
                layer.msg(data.msg);
                //刷新父页面
                parent.location.reload();
            }else{
                layer.msg(data.msg);
            }
        },"json")
        return false;
    })
});
