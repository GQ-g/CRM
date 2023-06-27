package com.gq.crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @date 2023/6/21
 */

@Controller
@RequestMapping("/report")
public class ReportController {

    @RequestMapping("/{type}")
    public String index(@PathVariable Integer type){
        if (type != null) {
            if (type == 0){
                return "report/customer_contri";
            }else if (type == 1) {
                // 客户构成分析
                return "report/customer_make";
            } else if (type == 2) {
                // 客户服务分析
            } else if (type == 3) {
                // 客户流失分析
                return "report/customer_loss";
            }
        }
        return "";
    }
}
