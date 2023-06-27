package com.gq.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gq.base.BaseService;
import com.gq.crm.dao.OrderDetailsDao;
import com.gq.crm.entity.CustomerOrder;
import com.gq.crm.entity.OrderDetails;
import com.gq.crm.query.CustomerOrderQuery;
import com.gq.crm.query.OrderDetailsQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @date 2023/6/19
 */

@Service
public class OrderDetailsService extends BaseService<OrderDetails,Integer> {

    @Resource
    private OrderDetailsDao orderDetailsDao;

    public Map<String, Object> queryOrdersDetailsByParams(OrderDetailsQuery orderDetailsQuery) {
        Map<String, Object> map = new HashMap<>();
        // 开启分页
        PageHelper.startPage(orderDetailsQuery.getPage(), orderDetailsQuery.getLimit());
        // 得到对应分页对象
        PageInfo<OrderDetails> pageInfo = new PageInfo<>(orderDetailsDao.selectByParams(orderDetailsQuery));
        // 设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        // 设置分页好的列表
        map.put("data",pageInfo.getList());
        return map;
    }
}
