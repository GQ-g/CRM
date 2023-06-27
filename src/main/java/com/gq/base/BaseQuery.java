package com.gq.base;

/**
 * 接收请求参数的基类封装
 */
public class BaseQuery {
    //请求参数当前页
    private Integer page = 1;
    //请求参数页面大小
    private Integer limit = 10;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
