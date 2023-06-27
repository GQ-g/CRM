package com.gq.base;

import java.util.List;

/**
 * 统一分页结果封装
 */
public class PageData<T> {
    private Integer code = 0;
    private String msg="";
    private Long count;
    private List<T> data;

    public PageData(Long count, List<T> data) {
        this.count = count;
        this.data = data;
    }
    public static PageData data(List list,Long count){
        return new PageData(count,list);
    }
    public PageData() {
    }

    public PageData(Integer code, String msg, Long count, List<T> data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
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

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
