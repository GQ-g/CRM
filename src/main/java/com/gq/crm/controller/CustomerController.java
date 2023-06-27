package com.gq.crm.controller;

import com.gq.base.BaseController;
import com.gq.base.ResultVo;
import com.gq.crm.entity.Customer;
import com.gq.crm.query.CustomerQuery;
import com.gq.crm.service.impl.CustomerOrderService;
import com.gq.crm.service.impl.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @date 2023/6/19
 */

@Controller
@RequestMapping("customer")
public class CustomerController extends BaseController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerOrderService customerOrderService;
    /**
     * 客户界面展示
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "customer/customer";
    }

    /**
     * 客户信息展示
     * @param customerQuery
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryCustomerByParams(CustomerQuery customerQuery){
        return customerService.queryCustomerByParams(customerQuery);
    }

    @RequestMapping("/save")
    @ResponseBody
    public ResultVo saveCustomer(Customer customer){
        customerService.saveCustomer(customer);
        return ResultVo.success("添加成功");
    }

    @RequestMapping("/update")
    @ResponseBody
    public ResultVo updateCustomer(Customer customer){
        customerService.updateCustomer(customer);
        return ResultVo.success("更新成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ResultVo deleteCustomer(Integer  id){
        customerService.deleteCustomer(id);
        return ResultVo.success("删除成功");
    }

    @RequestMapping("/addOrUpdateCustomerPage")
    public String addOrUpdateCustomerPage(Integer id, HttpServletRequest request){
        // 如果id不为空，则查询客户记录
        if (null != id) {
            // 通过id查询客户记录
            Customer customer = customerService.selectByPrimaryKey(id);
            // 将客户记录存到作用域中
            request.setAttribute("customer",customer);
        }
        return "customer/add_update";
    }

    @RequestMapping("/orderInfoPage")
    public String orderInfoPage(Integer cid, Model model){
        model.addAttribute("customer",customerService.selectByPrimaryKey(cid));
        return "/customer/customer_order";
    }

    @RequestMapping("/orderDetailPage")
    public String openOrderDetailPage(Integer orderId,Model model){
        model.addAttribute("order",customerOrderService.queryCustomerOrderByOrderId(orderId));
        System.out.println("执行结束了，跳转界面");
        return "customer/customer_order_detail";
    }

    @RequestMapping("/queryCustomerContributionByParams")
    @ResponseBody
    public Map<String, Object> queryCustomerContributionByParams(CustomerQuery customerQuery) {
        return customerService.queryCustomerContributionByParams(customerQuery);
    }


    @RequestMapping("/countCustomerMake")
    @ResponseBody
    public Map<String, Object> countCustomerMake() {
        return customerService.countCustomerMake();
    }
    @RequestMapping("/countCustomerMake02")
    @ResponseBody
    public Map<String, Object> countCustomerMake02() {
        return customerService.countCustomerMake02();
    }
}
