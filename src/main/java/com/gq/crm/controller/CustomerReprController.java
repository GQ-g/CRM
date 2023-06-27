package com.gq.crm.controller;

import com.gq.base.ResultVo;
import com.gq.crm.entity.CustomerReprieve;
import com.gq.crm.query.CustomerReprQuery;
import com.gq.crm.service.impl.CustomerReprService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @date 2023/6/20
 */

@Controller
@RequestMapping("/customer_rep")
public class CustomerReprController {
    @Resource
    private CustomerReprService customerReprService;

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryCustomerReprieveByParams(CustomerReprQuery customerReprQuery) {
        return customerReprService.queryCustomerReprieveByParams(customerReprQuery);
    }


    @RequestMapping("save")
    @ResponseBody
    public ResultVo saveCustomerRepr(CustomerReprieve customerReprieve){
            customerReprService.saveCustomerRepr(customerReprieve);
            return ResultVo.success("暂缓记录添加成功");
    }


    /**
     * 更新有问题
     * 需要调试， 更新走的是保存的方法
     * @param customerReprieve
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultVo updateCustomerRepr(CustomerReprieve customerReprieve){
        customerReprService.updateCustomerRepr(customerReprieve);
        return ResultVo.success("暂缓记录更新成功");
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultVo deleteCustomerRepr(Integer id){
        customerReprService.deleteCustomerRepr(id);
        return ResultVo.success("暂缓记录删除成功");
    }

}
