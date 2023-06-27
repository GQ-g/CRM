package com.gq.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gq.base.BaseService;
import com.gq.crm.dao.CustomerLossDao;
import com.gq.crm.entity.CustomerLoss;
import com.gq.crm.query.CustomerLossQuery;
import com.gq.crm.utils.CheckParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @date 2023/6/19
 */

@Service
public class CustomerLossService extends BaseService<CustomerLoss,Integer> {
    @Autowired
    private CustomerLossDao customerLossDao;

    public Map<String, Object> queryCustomerLossByParams(CustomerLossQuery customerLossQuery) {

        Map<String, Object> map = new HashMap<>();
        // 开启分页
        PageHelper.startPage(customerLossQuery.getPage(), customerLossQuery.getLimit());
        // 得到对应分页对象
        PageInfo<CustomerLoss> pageInfo = new PageInfo<>(customerLossDao.selectByParams(customerLossQuery));

        // 设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        // 设置分页好的列表
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 客户流失方法
     * @param id
     * @param lossReason
     */
    public void updateCustomerLossStateById(Integer id, String lossReason) {
        CustomerLoss customerLoss = customerLossDao.selectByPrimaryKey(id);
        CheckParamsUtils.isTrue(null == customerLoss,"待流失的客户不存在!");
        customerLoss.setState(1);
        customerLoss.setLossReason(lossReason);
        customerLoss.setConfirmLossTime(new Date());
        customerLoss.setUpdateDate(new Date());
        CheckParamsUtils.isTrue(updateByPrimaryKeySelective(customerLoss)<1,"确认流失失败");
    }
}
