package com.gq.crm.controller;

import com.gq.base.BaseController;
import com.gq.base.ResultInfo;
import com.gq.crm.entity.Role;
import com.gq.crm.query.RoleQuery;
import com.gq.crm.service.impl.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @date 2023/6/16
 */

@RequestMapping("/role")
@Controller
public class RoleController extends BaseController{

    @Resource
    private RoleService roleService;
    /**
     *
     */
    @RequestMapping("/queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> queryAllUser(Integer userId){
        return roleService.queryAllRoles(userId);
    }

    /**
     * 视图界面展示接口
     */
    @RequestMapping("/index")
    public String index(){
        return "role/role";
    }

    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryRolesByParams(RoleQuery roleQuery){
        return roleService.queryRolesByParams(roleQuery);
    }


    /**
     * 添加角色信息
     * @param role
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public ResultInfo saveRole(Role role){
        roleService.saveRole(role);
        return success("角色记录添加成功");
    }

    @RequestMapping("/update")
    @ResponseBody
    public ResultInfo updateRole(Role role){
        roleService.updateRole(role);
        return success("角色记录更新成功");
    }


    @RequestMapping("/delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer id){
        roleService.deleteRole(id);
        return success("角色记录删除成功");
    }


    /**
     * 弹框添加和更新角色
     */
    @RequestMapping("/addOrUpdateRolePage")
    public String openAddOrUpdateRoleDialog(Integer id, Model model){
        if (id != null){
            model.addAttribute("role",roleService.selectByPrimaryKey(id));
        }
        return "role/add_update";
    }



    @RequestMapping("/toAddGrantPage")
    public String toAddGrantPage(Integer roleId,Model model){
        model.addAttribute("roleId",roleId);
        return "role/grant";
    }

    @RequestMapping("/addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer[] mids,Integer roleId){
        roleService.addGrant(mids,roleId);
        return success("角色授权成功");
    }

}
