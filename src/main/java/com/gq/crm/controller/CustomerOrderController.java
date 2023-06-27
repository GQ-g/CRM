package com.gq.crm.controller;

import com.gq.crm.query.CustomerOrderQuery;
import com.gq.crm.service.impl.CustomerOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @date 2023/6/19
 */

@Controller
@RequestMapping("order")
public class CustomerOrderController {

    @Resource
    private CustomerOrderService customerOrderService;

    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryCustomerOrderByParams(CustomerOrderQuery customerOrderQuery){
        return customerOrderService.queryCustomerOrdersByParams(customerOrderQuery);
    }


}
