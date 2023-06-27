package com.gq.crm.controller;

import com.gq.crm.service.UserService;
import com.gq.crm.service.impl.PermissionService;
import com.gq.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @date 2023/6/1
 */

@Controller
public class indexController {
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;

    @RequestMapping("/index")
    public String index(){
        System.out.println("进入方法并且访问了该方法");
        return "index";
    }

    @RequestMapping("/welcome")
    public String  welcome(){
        return "welcome";
    }

    @RequestMapping("/main")
    public String main(HttpServletRequest request){

        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        System.out.println("userid"+userId);
        request.setAttribute("user",userService.selectByPrimaryKey(userId));
        System.out.println("userService.selectByPrimaryKey(userId)"+userService.selectByPrimaryKey(userId));

        List<String> permissions = permissionService.queryUserHasRoleHasPermissionByUserId(userId);
        request.getSession().setAttribute("permissions", permissions);
        System.out.println(permissions);
        return "main";
    }
}
