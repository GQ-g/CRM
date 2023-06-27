package com.gq.crm.service.impl;

import com.gq.base.BaseService;
import com.gq.crm.Model.TreeModel;
import com.gq.crm.dao.ModuleDao;
import com.gq.crm.dao.PermissionDao;
import com.gq.crm.entity.Module;
import com.gq.crm.utils.CheckParamsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @date 2023/6/18
 */

@Service
public class ModuleService extends BaseService<Module,Integer> {
    @Autowired
    private ModuleDao moduleDao;
    @Autowired
    private PermissionDao permissionDao;

    public List<TreeModel> queryAllModules(Integer roleId){
        List<TreeModel> treeModels = moduleDao.queryAllModules();
        List<Integer> mids = permissionDao.queryRoleHasModuleIdsByRoleId(roleId);
        if (null != mids && mids.size() > 0){
            treeModels.forEach(treeModel -> {
                //判断是否包含
                if (mids.contains(treeModel.getId())) {
                    //角色已分配该菜单
                    treeModel.setChecked(true);
                }
            });
        }
        return treeModels;
    }


    /**
     * 菜单管理
     * @return
     */
    public Map<String,Object> queryModule(){
        Map<String,Object> result = new HashMap<>();
        List<Module> modules = moduleDao.queryModuleList();
        result.put("count",modules.size());
        result.put("data",modules);
        result.put("code",0);
        result.put("msg","");
        return result;
    }


    public void saveModule(Module module){
        /* 1. 参数校验  */
        // 层级 grade 非空，0|1|2
        Integer grade = module.getGrade();
        CheckParamsUtils.isTrue(null == grade || !(grade == 0 || grade == 1 || grade == 2),"菜单层级不合法！");

        // 模块名称 moduleName  非空
        CheckParamsUtils.isTrue(StringUtils.isBlank(module.getModuleName()), "模块名称不能为空！");
        // 模块名称 moduleName  同一层级下模块名称唯一
        CheckParamsUtils.isTrue(null != moduleDao.queryModuleByGradeAndModuleName(grade, module.getModuleName()), "改层级下模块名称已存在！");
        // 如果是二级菜单 （grade=1)
        if (grade == 1) {
            // 地址 url   二级菜单（grade=1），非空
            CheckParamsUtils.isTrue(StringUtils.isBlank(module.getUrl()),"URL不能为空！");
            // 地址 url   二级菜单（grade=1），且同一层级下不可重复
            CheckParamsUtils.isTrue(null != moduleDao.queryModuleByGradeAndUrl(grade,module.getUrl()),"URL不可重复！");
        }

        // 父级菜单 parentId    一级菜单（目录 grade=0）    -1
        if (grade == 0) {
            module.setParentId(-1);
        }
        // 父级菜单 parentId    二级|三级菜单（菜单|按钮 grade=1或2）    非空，父级菜单必须存在
        if (grade != 0) {
            // 非空
            CheckParamsUtils.isTrue(null == module.getParentId(),"父级菜单不能为空！");
            // 父级菜单必须存在 (将父级菜单的ID作为主键，查询资源记录)
            CheckParamsUtils.isTrue(null == moduleDao.selectByPrimaryKey(module.getParentId()), "请指定正确的父级菜单！");
        }

        // 权限码 optValue     非空
        CheckParamsUtils.isTrue(StringUtils.isBlank(module.getOptValue()),"权限码不能为空！");
        // 权限码 optValue     不可重复
        CheckParamsUtils.isTrue(null != moduleDao.queryModuleByOptValue(module.getOptValue()),"权限码已存在！");


        /* 2. 设置参数的默认值  */
        // 是否有效 isValid    1
        module.setIsValid((byte) 1);
        // 创建时间createDate  系统当前时间
        module.setCreateDate(new Date());
        // 修改时间updateDate  系统当前时间
        module.setUpdateDate(new Date());

        /* 3. 执行添加操作，判断受影响的行数 */
        CheckParamsUtils.isTrue(moduleDao.insertSelective(module) < 1, "添加资源失败！");

    }


    public void updateModule(Module module){
        /* 1. 参数校验 */
        // id 非空，数据存在
        // 非空判断
        CheckParamsUtils.isTrue(null == module.getId(), "待更新记录不存在！");
        // 通过id查询资源对象
        Module temp = moduleDao.selectByPrimaryKey(module.getId());
        // 判断记录是否存在
        CheckParamsUtils.isTrue(null == temp, "待更新记录不存在！");

        // 层级 grade  非空 0|1|2
        Integer grade = module.getGrade();
        CheckParamsUtils.isTrue(null == grade || !(grade == 0 || grade == 1 || grade == 2), "菜单层级不合法！");

        // 模块名称 moduleName      非空，同一层级下模块名称唯一 （不包含当前修改记录本身）
        CheckParamsUtils.isTrue(StringUtils.isBlank(module.getModuleName()), "模块名称不能为空！");
        // 通过层级与模块名称查询资源对象
        temp = moduleDao.queryModuleByGradeAndModuleName(grade, module.getModuleName());
        if (temp != null) {
            CheckParamsUtils.isTrue(!(temp.getId()).equals(module.getId()), "该层级下菜单名已存在！");
        }

        // 地址 url   二级菜单（grade=1），非空且同一层级下不可重复（不包含当前修改记录本身）
        if (grade == 1) {
            CheckParamsUtils.isTrue(StringUtils.isBlank(module.getUrl()), "菜单URL不能为空！");
            // 通过层级与菜单URl查询资源对象
            temp = moduleDao.queryModuleByGradeAndUrl(grade, module.getUrl());
            // 判断是否存在
            if (temp != null) {
                CheckParamsUtils.isTrue(!(temp.getId()).equals(module.getId()), "该层级下菜单URL已存在！");
            }
        }

        // 权限码 optValue     非空，不可重复（不包含当前修改记录本身）
        CheckParamsUtils.isTrue(StringUtils.isBlank(module.getOptValue()), "权限码不能为空！");
        // 通过权限码查询资源对象
        temp = moduleDao.queryModuleByOptValue(module.getOptValue());
        // 判断是否为空
        if (temp != null) {
            CheckParamsUtils.isTrue(!(temp.getId()).equals(module.getId()),"权限码已存在！");
        }

        /* 2. 设置参数的默认值  */
        // 修改时间 系统当前时间
        module.setUpdateDate(new Date());

        /* 3. 执行更新操作，判断受影响的行数 */
        CheckParamsUtils.isTrue(moduleDao.updateByPrimaryKeySelective(module) < 1, "修改资源失败！");
    }



    /**
     * 通过id删除子菜单  如果下面有子菜单，暂不支持删除
     */
    public void deleteModule(Integer id) {
        // 判断id是否为空
        CheckParamsUtils.isTrue(null == id, "待删除记录不存在！");
        // 通过id查询资源对象
        Module temp = moduleDao.selectByPrimaryKey(id);
        // 判断资源对象是否为空
        CheckParamsUtils.isTrue(null == temp, "待删除记录不存在！");

        // 如果当前资源存在子记录(将id当做父Id查询资源记录)
        Integer count = moduleDao.queryModuleByParentId(id);
        // 如果存在子记录，则不可删除
        CheckParamsUtils.isTrue(count > 0, "该资源存在子记录，不可删除！");

        // 通过资源id查询权限表中是否存在数据
        count = permissionDao.countPermissionByModuleId(id);
        // 判断是否存在，存在则删除
        if (count > 0) {
            // 删除指定资源ID的权限记录
            permissionDao.deletePermissionByModuleId(id);
        }

        // 设置记录无效
        temp.setIsValid((byte) 0);
        temp.setUpdateDate(new Date());

        // 执行更新
        CheckParamsUtils.isTrue(moduleDao.updateByPrimaryKeySelective(temp) < 1, "删除资源失败！");
    }
}
