package com.gq.base;

/**
 * 统一状态码封装
 */
public interface StatusCode {
    //成功状态码
    public static final int SUCCESS_CODE = 200;
    //失败状态码
    public static final int ERROR_CODE = 500;
    //请求参数
    public static final int ERROR_PARAMS = 400;
    //未登录
    public static final int NO_LOGIN = 401;
    //权限不足
    public static final int NO_AUTH = 403;
}
