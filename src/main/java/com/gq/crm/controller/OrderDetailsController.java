package com.gq.crm.controller;

import com.gq.base.ResultInfo;
import com.gq.base.ResultVo;
import com.gq.crm.entity.OrderDetails;
import com.gq.crm.query.OrderDetailsQuery;
import com.gq.crm.service.impl.OrderDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @date 2023/6/19
 */

@Controller
@RequestMapping("/order_details")
public class OrderDetailsController {
    @Resource
    private OrderDetailsService orderDetailsService;

    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryDetailsByParams(OrderDetailsQuery orderDetailsQuery){
        return orderDetailsService.queryOrdersDetailsByParams(orderDetailsQuery);
    }
}
