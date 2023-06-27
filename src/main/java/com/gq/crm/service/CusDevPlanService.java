package com.gq.crm.service;

import com.gq.base.BaseService;
import com.gq.crm.entity.CusDevPlan;
import com.gq.crm.query.CusDevPlanQuery;

import java.util.Map;

/**
 * @date 2023/6/14
 */

public interface CusDevPlanService {

    Map<String, Object> queryCusDevByParams(CusDevPlanQuery cusDevPlanQuery);

    void saveCusDevPlan(CusDevPlan cusDevPlan);

    void updateCusDevPlan(CusDevPlan cusDevPlan);

    void deleteCusDevPlan(Integer id);

    CusDevPlan selectByPrimaryKey1(Integer id);
}
