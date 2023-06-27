package com.gq.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gq.base.BaseService;
import com.gq.crm.dao.CusDevPlanDao;
import com.gq.crm.dao.SaleChanceDao;
import com.gq.crm.entity.CusDevPlan;
import com.gq.crm.query.CusDevPlanQuery;
import com.gq.crm.service.CusDevPlanService;
import com.gq.crm.utils.CheckParamsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @date 2023/6/14
 */

@Service
public class CusDevPlanServiceImpl extends BaseService<CusDevPlan,Integer> implements CusDevPlanService {

    @Resource
    private CusDevPlanDao cusDevPlanDao;

    @Resource
    private SaleChanceDao saleChanceDao;

    public Map<String ,Object> queryCusDevByParams(CusDevPlanQuery cusDevPlanQuery){
        Map<String,Object> map = new HashMap<>();
        //开启分页，设置起始页和每页的条数
        PageHelper.startPage(cusDevPlanQuery.getPage(),cusDevPlanQuery.getLimit());
        PageInfo<CusDevPlan> pageInfo = new PageInfo<>(cusDevPlanDao.selectByParams(cusDevPlanQuery));
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    public void saveCusDevPlan(CusDevPlan cusDevPlan){
        /**
         * 1.效验参数
         *      机会id非空 记录必须存在
         *      计划项内容非空
         *      计划项时间非空
         *  2.参数默认值
         *      is_valid 1
         *      createDate 系统时间
         *      updateDate 系统时间
         *  3.执行添加 判断结果
         */
        checkParams(cusDevPlan.getSaleChanceId(),cusDevPlan.getPlanItem(),cusDevPlan.getPlanDate());
        cusDevPlan.setIsValid(1);
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setUpdateDate(new Date());
        CheckParamsUtils.isTrue(insertSelective(cusDevPlan) < 1 ,"计划项记录添加失败！");

    }

    /*判断参数是否为空的操作*/
    private void checkParams(Integer saleChanceId, String planItem, Date planDate) {
        CheckParamsUtils.isTrue(saleChanceId==null || null == saleChanceDao.selectByPrimaryKey(saleChanceId),"设置营销机会id");
        CheckParamsUtils.isTrue(StringUtils.isBlank(planItem),"请输入计划项内容");
        CheckParamsUtils.isTrue(null == planDate ,"请指定计划项日期!");
    }

    public void updateCusDevPlan(CusDevPlan cusDevPlan){
        /**
         * 1.效验参数
         *      机会id非空 记录必须存在
         *      计划项内容非空
         *      计划项时间非空
         *  2.参数默认值
         *      is_valid 1
         *      createDate 系统时间
         *      updateDate 系统时间
         *  3.执行更新 判断结果
         */
        CheckParamsUtils.isTrue(null == cusDevPlan.getId() || null == selectByPrimaryKey(cusDevPlan.getId())," 待更新的记录不存在");
        checkParams(cusDevPlan.getSaleChanceId(),cusDevPlan.getPlanItem(),cusDevPlan.getPlanDate());
        cusDevPlan.setUpdateDate(new Date());
        CheckParamsUtils.isTrue(updateByPrimaryKeySelective(cusDevPlan) < 1 ,"记录更新失败");
    }

    public void deleteCusDevPlan(Integer id){
        CusDevPlan cusDevPlan = selectByPrimaryKey(id);
        CheckParamsUtils.isTrue(null == cusDevPlan,"待删除的记录不存在！");
        cusDevPlan.setIsValid(0);
        CheckParamsUtils.isTrue(updateByPrimaryKeySelective(cusDevPlan) < 1 ,"删除记录事变 ");
    }

    @Override
    public CusDevPlan selectByPrimaryKey1(Integer id)  {
        return cusDevPlanDao.selectByPrimaryKey(id);
    }
}

