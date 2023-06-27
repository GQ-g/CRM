package com.gq.crm.controller;

import com.gq.base.BaseController;
import com.gq.base.ResultVo;
import com.gq.crm.entity.CusDevPlan;
import com.gq.crm.query.CusDevPlanQuery;
import com.gq.crm.service.CusDevPlanService;
import com.gq.crm.service.SaleChanceService;
import com.gq.crm.service.impl.CusDevPlanServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @date 2023/6/14
 */

@Controller
@RequestMapping("/cus_dev_plan")
public class CusDevPlanController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    @Resource
    private CusDevPlanServiceImpl cusDevPlanService;


    @RequestMapping("/index")
    public String index() {
        return "cus_dev_plan/cus_dev_plan";
    }


    @RequestMapping("/toCusDevPlanDataPage")
    public String toCusDevPlanDataPage(Integer id, Model model) {
        /*接受*/
        System.out.println(id + "id");
        if (id != null) {
            model.addAttribute("saleChance", saleChanceService.selectByPrimaryKey(id));
        }
        return "cus_dev_plan/cus_dev_plan_data";
    }


    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> queryCusDevByParams(CusDevPlanQuery cusDevPlanQuery) {
        return cusDevPlanService.queryCusDevByParams(cusDevPlanQuery);
    }


    @RequestMapping("/save")
    @ResponseBody
    public ResultVo saveCusDevPlan(CusDevPlan cusDevPlan) {
        cusDevPlanService.saveCusDevPlan(cusDevPlan);
        return ResultVo.success("计划项数据添加成功");
    }


    @RequestMapping("/update")
    @ResponseBody
    public ResultVo updateCusDevPlan(CusDevPlan cusDevPlan) {
        cusDevPlanService.updateCusDevPlan(cusDevPlan);
        return ResultVo.success("计划项数据修改成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ResultVo deleteCusDevPlan(CusDevPlan cusDevPlan) {
        cusDevPlanService.deleteCusDevPlan(cusDevPlan.getId());
        return ResultVo.success("计划项数据删除成功");
    }


    @RequestMapping("/addOrUpdateCusDevPlanPage")
    public String addOrUpdateCusDevPlanDialog(Integer sid, Integer id, Model model) {
        System.out.println(sid+"sid输出了");
        System.out.println(id+"id输出了");
        model.addAttribute("sid",sid);
        CusDevPlan cusDevPlan = cusDevPlanService.selectByPrimaryKey(id);
        /*查询出错，修改查询，或者重新测试*/
        if(id!=null){
            model.addAttribute("cusDevPlan",cusDevPlan);
        }
        return "cus_dev_plan/add_update";
    }



}
