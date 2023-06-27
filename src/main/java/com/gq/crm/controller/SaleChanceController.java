package com.gq.crm.controller;

import com.gq.base.ResultVo;
import com.gq.crm.dao.SaleChanceDao;
import com.gq.crm.entity.SaleChance;
import com.gq.crm.query.SaleChanceQuery;
import com.gq.crm.service.SaleChanceService;
import com.gq.crm.service.UserService;
import com.gq.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @date 2023/6/13
 */

@Controller
@RequestMapping("/sale_chance")
public class SaleChanceController {
    @Resource
    private SaleChanceService saleChanceService;

    @Resource
    private UserService userService;

    @Resource
    private SaleChanceDao saleChanceDao;

    /*营销机会*/
    @RequestMapping("/index")
    public String index(){
        return "sale_chance/sale_chance";
    }

    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> querySaleChanceByParams(Integer flag,HttpServletRequest request,SaleChanceQuery saleChanceQuery){
        if (null != flag && flag == 1){
            /*分配给指定用户的机会数据*/
            saleChanceQuery.setAssignMan(LoginUserUtil.releaseUserIdFromCookie(request));
        }
        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
    }

    @RequestMapping("/save")
    @ResponseBody
    public ResultVo  saveSaleChance(HttpServletRequest request,SaleChance saleChance){
//        /*通过获取登录的用户名查询用户，然后设置saleChance中的创建人的值*/
        saleChance.setCreateMan(userService.selectByPrimaryKey(LoginUserUtil.releaseUserIdFromCookie(request)).getTrueName());
        saleChanceService.saveSaleChance(saleChance);
        return ResultVo.success("机会添加成功");
    }


    @RequestMapping("/update")
    @ResponseBody
    public ResultVo  updateSaleChance(SaleChance saleChance){
        saleChanceService.updateSaleChance(saleChance);
        return ResultVo.success("机会数据更新成功");
    }

    /**
     * 打开窗口添加修改
     * 报系统错误
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/addOrUpdateSaleChancePage")
    public String addOrUpdateSaleChancePage(Integer id, Model model){
        if (id != null){
            model.addAttribute("saleChance",saleChanceService.selectByPrimaryKey(id));
        }
        return "/sale_chance/add_update";
    }


    @RequestMapping("/delete")
    @ResponseBody
    public  ResultVo deleteSaleChance(Integer[] ids){
        saleChanceService.deleteSaleChance(ids);
        return ResultVo.success("机会数据删除成功!");
    }

    @RequestMapping("/updateSaleChanceDevResultVo")
    @ResponseBody
    public ResultVo updateSaleChanceDevResultVo(Integer id,Integer devResult){
        saleChanceService.updateSaleChanceDevResultVo(id,devResult);
        return ResultVo.success("开发状态更新成功");
    }

}


