layui.use(['form','http','layer','jquery'], function () {
    var form = layui.form;
    var layer = layui.layer;
    var http = layui.http;
    var path = http.path;
    var $ = layui.jquery;

    form.on('submit(saveBtn)', function(data){
        var data = data.field;
        //发送ajax
        $.ajax({
            url:path+"/user/setting",
            type:'post',
            data:data,
            dataType:'json',
            success:function(data){
                if(data.code==200){
                    layer.msg('修改成功');
                }else{
                    layer.msg(data.msg);
                }
            }
        })

    });
});