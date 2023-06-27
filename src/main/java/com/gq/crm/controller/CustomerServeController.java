package com.gq.crm.controller;

import com.gq.base.BaseController;
import com.gq.base.ResultInfo;
import com.gq.base.ResultVo;
import com.gq.crm.converter.DateConverter;
import com.gq.crm.entity.CustomerServe;
import com.gq.crm.query.CustomerServeQuery;
import com.gq.crm.service.UserService;
import com.gq.crm.service.impl.CustomerServeService;
import com.gq.crm.utils.LoginUserUtil;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;


@RequestMapping("customer_serve")
@Controller
public class CustomerServeController extends BaseController {

    @Resource
    private CustomerServeService customerServeService;
    @Resource
    private UserService userService;
    /**
     * 多条件分页查询服务数据的列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryCustomerServeByParams(CustomerServeQuery customerServeQuery,
                                                         Integer flag, HttpServletRequest request) {
        // 判断是否执行服务处理，如果是则查询分配给当前登录用户的服务记录
        if (flag != null && flag == 1) {
            // 设置查询条件：分配人
            customerServeQuery.setAssigner(LoginUserUtil.releaseUserIdFromCookie(request));
        }
        return customerServeService.queryCustomerServeByParams(customerServeQuery);
    }

    /**
     * 通过不同的类型进入不同的服务页面
     */
    @RequestMapping("index/{type}")
    public String index(@PathVariable Integer type) {
        // 判断类型是否为空
        if (type != null) {
            if (type == 1) {
                // 服务创建CustomerServeMapper
                return "customer_serve/customer_serve";
            } else if (type == 2) {
                // 服务分配
                return "customer_serve/customer_serve_assign";
            } else if (type == 3) {
                // 服务处理
                return "customer_serve/customer_serve_proce";
            } else if (type == 4) {
                // 服务反馈
                return "customer_serve/customer_serve_feed_back";
            } else if (type == 5) {
                // 服务归档
                return "customer_serve/customer_serve_archive";
            } else {
                return "";
            }
        } else {
            return "";
        }
    }


    /**
     * 打开创建服务页面
     */
    @RequestMapping("addCustomerServePage")
    public String addCustomerServePage() {
        return "customer_serve/customer_serve_add";
    }


    /**
     * 创建服务
     */
    @RequestMapping ("save")
    @ResponseBody
    public ResultVo addCustomerServe(HttpServletRequest request,CustomerServe customerServe) {
        customerServe.setCreatePeople(userService.selectByPrimaryKey(LoginUserUtil.releaseUserIdFromCookie(request)).getTrueName());
        customerServeService.saveCustomerServe(customerServe);
        return ResultVo.success("添加服务成功！");
    }

    /**
     * 服务更新
     *     1. 服务分配
     *     2. 服务处理
     *     3.服务反馈
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateCustomerServe( CustomerServe customerServe) {
        customerServeService.updateCustomerServe(customerServe);
        return success("服务更新成功！");
    }


    /**
     * 打开服务分配页面
     * @param
     * @return java.lang.String
     */
    @RequestMapping("addCustomerServeAssignPage")
    public String addCustomerServeAssignPage(Integer id, Model model) {
        // 通过id查询服务记录，并设置到请求域中
        model.addAttribute("customerServe", customerServeService.selectByPrimaryKey(id));
        return "customer_serve/customer_serve_assign_add";
    }

    /**
     * 打开服务处理页面
     * @param
     * @return java.lang.String
     */
    @RequestMapping("addCustomerServeProcePage")
    public String toCustomerServeProcePage(Integer id, Model model) {
        // 通过id查询服务记录，并设置到请求域中
        model.addAttribute("customerServe", customerServeService.selectByPrimaryKey(id));
        return "customer_serve/customer_serve_proce_add";
    }

    /**
     * 打开服务反馈页面
     */
    @RequestMapping("addCustomerServeBackPage")
    public String addCustomerServeBackPage(Integer id,Model model){
        model.addAttribute("customerServe",customerServeService.selectByPrimaryKey(id));
        return "customer_serve/customer_serve_feed_back_add";
    }

}
