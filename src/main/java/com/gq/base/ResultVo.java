package com.gq.base;

/**
 * 统一响应结果封装
 */
public class ResultVo<T> {
    //响应状态码
    private Integer code;
    //响应消息
    private String msg;
    private T data;

    //响应成功无数据
    public static ResultVo success(){
        return new ResultVo(StatusCode.SUCCESS_CODE,"ok",null);
    }
    public static ResultVo success(String msg){
        return new ResultVo(StatusCode.SUCCESS_CODE,msg,null);
    }
    public static ResultVo success(Integer code,String msg){
        return new ResultVo(code,msg,null);
    }
    //响应成功有数据封装
    public static ResultVo success(Object data){
        return new ResultVo(StatusCode.SUCCESS_CODE,"ok",data);
    }
    public static ResultVo success(String msg,Object data){
        return new ResultVo(StatusCode.SUCCESS_CODE,msg,data);
    }
    public static ResultVo success(Integer code,String msg,Object data){
        return new ResultVo(code,msg,data);
    }

    //响应失败封装
    public static ResultVo error(){
        return new ResultVo(StatusCode.ERROR_CODE,"error",null);
    }
    public static ResultVo error(String msg){
        return new ResultVo(StatusCode.ERROR_CODE,msg,null);
    }
    public static ResultVo error(Integer code,String msg){
        return new ResultVo(code,msg,null);
    }

    public ResultVo(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultVo() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "ResultVo{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
