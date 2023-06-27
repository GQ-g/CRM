layui.use(['form','http','layer','jquery','jquery_cookie'], function () {
    var   form = layui.form,
        layer = layui.layer,
        http = layui.http,
        path = http.path,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);


    form.on('submit(saveBtn)', function(data){
        var data = data.field;
        console.log(data);
        //发送ajax
        $.ajax({
            url:path+"/user/updatePassword",
            type:'post',
            /*     data:data,*/
            data:{
                oldPassword: data.old_password,
                newPassword: data.new_password,
                confirmPassword: data.again_password,
            },
            dataType:'json',
            success:function(data){
                if(data.code==200){
                    layer.msg('修改成功,系统3秒后退出......', function () {
                        /*清除Cookie*/
                        $.removeCookie("userIdStr",{domain:"localhost",path:"/crm"});
                        $.removeCookie("userName",{domain:"localhost",path:"/crm"});
                        $.removeCookie("trueName",{domain:"localhost",path:"/crm"});
                        /*设置退出时间*/
                        setTimeout(function (){
                            window.parent.location.href=path+"/index";
                        },3000)
                    });
                }else{
                    layer.msg(data.msg);
                }
            }
        })

    });
});