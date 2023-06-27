package com.gq.crm.utils;

/**
 * 表单参数校验
 */
public class CheckParamsUtils {
    public  static void isTrue(Boolean flag,String msg){
        if(flag){
            throw  new ParamsException(msg);
        }
    }
}
