layui.use(['form','jquery','jquery_cookie','http'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);
    var http = layui.http;
    var path = http.path;

    // 进行登录操作
    form.on('submit(login)', function (data) {
        var data = data.field;
        console.log(data);
        if ( data.username =="undefined" || data.username =="" || data.username.trim()=="") {
            layer.msg('用户名不能为空');
            return false;
        }
        if ( data.password =="undefined" || data.password =="" || data.password.trim()=="")  {
            layer.msg('密码不能为空');
            return false;
        }
        $.ajax({
            type:"post",
            url:path+"/user/login",
            /*进行前后端数据绑定*/
            data:{
                userName:data.username,
                userPwd:data.password,
                rememberMe:data.rememberMe
            },
            dataType:"json",
            success:function (date) {
                if(date.code==200){
                    /*定义缓存数据的变量*/
                    var result = date.data;
                    if ($("#rememberMe").prop("checked")) {
                        // 选中，则设置cookie对象7天生效
                        // 将用户信息设置到cookie中
                        $.cookie("userIdStr",result.userIdStr, {expires:7});
                        $.cookie("userName",result.userName, {expires:7});
                        $.cookie("trueName",result.trueName, {expires:7});
                    } else {
                        // 将用户信息设置到cookie中
                        $.cookie("userIdStr",result.userIdStr);
                        $.cookie("userName",result.userName);
                        $.cookie("trueName",result.trueName);
                    }
                    /*设置缓存后跳转*/
                    window.location.href=path+"/main";
                }else{
                    layer.msg(data.msg);
                }
            }
        });
        return false;
    });
});