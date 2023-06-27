package com.gq.crm.query;

import com.gq.base.BaseQuery;

/**
 * @Auther: 胖叔讲java
 * @Date: 2022/10/24 - 10 - 24 - 15:15
 * @Decsription: com.study.crm.query
 * @version: 1.0
 */
public class CustomerLossQuery extends BaseQuery {
    private String cusNo;
    private String cusName;
    private Integer state;

    public String getCusNo() {
        return cusNo;
    }

    public void setCusNo(String cusNo) {
        this.cusNo = cusNo;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
