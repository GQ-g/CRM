package com.gq.crm.query;

import com.gq.base.BaseQuery;

/**
 * @Auther: 胖叔讲java
 * @Date: 2022/10/21 - 10 - 21 - 17:23
 * @Decsription: com.study.crm.query
 * @version: 1.0
 */
public class CustomerOrderQuery extends BaseQuery {
    private Integer cusId;

    public Integer getCusId() {
        return cusId;
    }

    public void setCusId(Integer cusId) {
        this.cusId = cusId;
    }
}
