package com.gq.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gq.base.BaseService;
import com.gq.crm.dao.CustomerOrderDao;
import com.gq.crm.entity.CustomerOrder;
import com.gq.crm.query.CustomerOrderQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @date 2023/6/19
 */

@Service
public class CustomerOrderService extends BaseService<CustomerOrder,Integer> {

    @Resource
    private CustomerOrderDao customerOrderDao;

    public Map<String, Object> queryCustomerOrdersByParams(CustomerOrderQuery customerOrderQuery) {
        Map<String, Object> map = new HashMap<>();

        // 开启分页
        PageHelper.startPage(customerOrderQuery.getPage(), customerOrderQuery.getLimit());
        // 得到对应分页对象
        PageInfo<CustomerOrder> pageInfo = new PageInfo<>(customerOrderDao.selectByParams(customerOrderQuery));

        // 设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        // 设置分页好的列表
        map.put("data",pageInfo.getList());

        return map;
    }

    public Map<String,Object> queryCustomerOrderByOrderId(Integer orderId) {
        return customerOrderDao.queryOrderById(orderId);
    }

    public Map<String, Object> queryOrderById(Integer orderId) {
        return customerOrderDao.queryOrderById(orderId);
    }
}
