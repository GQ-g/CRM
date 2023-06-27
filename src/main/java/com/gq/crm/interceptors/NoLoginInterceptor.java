package com.gq.crm.interceptors;

import com.gq.crm.exception.NoLoginException;
import com.gq.crm.service.UserService;
import com.gq.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @date 2023/6/13
 */


public class NoLoginInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*true 放行
        *false 拦截
        * 通过登录的状态
        * 获取Cookie 解析用户ID
        *   如果用户ID存在 并且数据库中存在对应记录  请求合法，反之 用户未登录  请求非法
        * */
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        if (userId == 0 || null == userService.selectByPrimaryKey(userId)) {
            throw new NoLoginException();
        }
        return super.preHandle(request, response, handler);
    }
}
