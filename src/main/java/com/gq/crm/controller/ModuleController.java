package com.gq.crm.controller;

import com.gq.base.ResultVo;
import com.gq.crm.Model.TreeModel;
import com.gq.crm.entity.Module;
import com.gq.crm.service.impl.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @date 2023/6/18
 */

@RequestMapping("module")
@Controller
public class ModuleController {
    @Autowired
    private ModuleService moduleService;

    @RequestMapping("/queryAllModules")
    @ResponseBody
    public List<TreeModel> queryAllModules(Integer roleId){
        return moduleService.queryAllModules(roleId);
    }

    @RequestMapping("/index")
    public String index(){
        return "module/module";
    }

    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryModules(){
        return moduleService.queryModule();
    }

    /**
     * 添加菜单接口
     * @param module
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public ResultVo saveModule(Module module){
        moduleService.saveModule(module);
        return ResultVo.success("菜单添加成功");
    }

    /**
     * 更新菜单
     * @param module
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResultVo updateModule(Module module){
        moduleService.updateModule(module);
        return ResultVo.success("菜单更新成功");
    }

    /**
     * 删除菜单
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResultVo deleteModule(Integer ids){
        moduleService.deleteModule(ids);
        return ResultVo.success("菜单删除成功");
    }

    @RequestMapping("/addModulePage")
    public String addModulePage(Integer grade, Integer parentId, Model model){
        model.addAttribute("grade",grade);
        model.addAttribute("parentId",parentId);
        return "module/add";
    }


    @RequestMapping("/updateModulePage")
    public String updateModulePage(Integer id,Model model){
        model.addAttribute("module",moduleService.selectByPrimaryKey(id));
        return "module/update";
    }

}
