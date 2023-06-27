package com.gq.crm.controller;

import com.gq.base.ResultVo;
import com.gq.crm.query.CustomerLossQuery;
import com.gq.crm.service.impl.CustomerLossService;
import com.gq.crm.service.impl.CustomerReprService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @date 2023/6/20
 */

@Controller
@RequestMapping("/customer_loss")
public class CustomerLossController {
    @Autowired
    private CustomerLossService customerLossService;
    @Autowired
    private CustomerReprService customerReprService;

    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryCustomerByParams(CustomerLossQuery customerLossQuery){
        return customerLossService.queryCustomerLossByParams(customerLossQuery);
    }

    @RequestMapping("/index")
    public String index(){
        return "customer_loss/customer_loss";
    }


    @RequestMapping("/toCustomerReprPage")
    public String toCustomerReprPage(Integer id, Model model){
        model.addAttribute("customerLoss",customerLossService.selectByPrimaryKey(id));
        return "customer_loss/customer_rep";
    }


    @RequestMapping("/addOrUpdateCustomerReprPage")
    public String  addOrUpdateCustomerReprPage(Integer id, Integer lossId,Model model){
        model.addAttribute("customerRep",customerReprService.selectByPrimaryKey(id));
        model.addAttribute("lossId",lossId);
        return "customer_loss/customer_rep_add_update";
    }



    @RequestMapping("/updateCustomerLossStateById")
    @ResponseBody
    public ResultVo updateCustomerLossStateById(Integer id,String lossReason){
        customerLossService.updateCustomerLossStateById(id,lossReason);
        return ResultVo.success("客户流失成功");
    }
}
