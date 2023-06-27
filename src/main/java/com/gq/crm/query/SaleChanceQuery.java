package com.gq.crm.query;

import com.gq.base.BaseQuery;

/**
 * @date 2023/6/13
 */


public class SaleChanceQuery extends BaseQuery {
    //客户名字
    private String customerName;
    //创建人
    private String createMan;
    //分配状态
    private Integer state;

    // 客户开发计划 条件查询
    private String devResult; // 开发状态
    private Integer assignMan; // 指派人


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }


    public String getDevResult() {
        return devResult;
    }

    public void setDevResult(String devResult) {
        this.devResult = devResult;
    }

    public Integer getAssignMan() {
        return assignMan;
    }

    public void setAssignMan(Integer assignMan) {
        this.assignMan = assignMan;
    }
}
