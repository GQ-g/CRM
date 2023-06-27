package com.gq.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gq.base.BaseService;
import com.gq.crm.dao.ModuleDao;
import com.gq.crm.dao.PermissionDao;
import com.gq.crm.dao.RoleDao;
import com.gq.crm.dao.UserRoleDao;
import com.gq.crm.entity.Permission;
import com.gq.crm.entity.Role;
import com.gq.crm.entity.User;
import com.gq.crm.query.RoleQuery;
import com.gq.crm.query.UserQuery;
import com.gq.crm.utils.CheckParamsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @date 2023/6/16
 */


@Service
public class RoleService extends BaseService<Role,Integer> {
    @Resource
    private RoleDao roleDao;
    @Resource
    private UserRoleDao userRoleDao;
    @Resource
    private PermissionDao permissionDao;
    @Resource
    private ModuleDao moduleDao;
    /**
     * 查询所有角色
     * @param userId
     * @return
     */
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleDao.queryAllRoles(userId);
    }


    /**
     * 查询角色根据角色名称
     * @param roleQuery
     * @return
     */
    public Map<String, Object> queryRolesByParams(RoleQuery roleQuery) {
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(roleQuery.getPage(),roleQuery.getLimit());
        PageInfo<Role> pageInfo = new PageInfo<Role>(roleDao.selectByParams(roleQuery));
        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }


    /**
     * 用户角色添加
     */
    public void saveRole(Role role){
        /**
         * 分析
         * 1.参数效验
         *      角色名 非空 唯一
         * 2.参数默认值设置
         *      isValid
         *      createDate
         *      updateDate
         *  3.执行添加  判断结果
         */
        CheckParamsUtils.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名称");
        CheckParamsUtils.isTrue(roleDao.queryRoleByRoleName(role.getRoleName()) != null,"该角色已存在");
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        CheckParamsUtils.isTrue(roleDao.insertSelective(role) < 1 ,"角色添加失败");
    }


    @Override
    public Role selectByPrimaryKey(Integer id) throws DataAccessException {
        System.out.println("哈哈哈哈哈哈哈哈哈");
        Role role = roleDao.selectByPrimaryKey(id);
        System.out.println("role");
        return roleDao.selectByPrimaryKey(id);
    }

    /**
     * 角色更新
     * @param role
     */
    public void updateRole(Role role){
        /**
         * 分析
         * 1.参数效验
         *      角色名 非空 唯一
         * 2.参数默认值设置
         *      updateDate
         *  3.执行添加  判断结果
         */
        CheckParamsUtils.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名称");
        /*通过查询用户的角色名称判断用户是否存在*/
        Role temp = roleDao.queryRoleByRoleName(role.getRoleName());
        /*更新的逻辑*/
        CheckParamsUtils.isTrue(temp != null  && !(temp.getId().equals(role.getId())),"该角色已存在");
        role.setUpdateDate(new Date());
        CheckParamsUtils.isTrue(roleDao.updateByPrimaryKeySelective(role) < 1 ,"角色更新失败");
    }

    public void deleteRole(Integer  roleId){
        // 判断角色ID是否为空
        CheckParamsUtils.isTrue(null == roleId, "待删除记录不存在！");
        // 通过角色ID查询角色记录
        Role role = roleDao.selectByPrimaryKey(roleId);

        int total= userRoleDao.countUserRoleByRoleId(roleId);
        if (total>0){
            CheckParamsUtils.isTrue(userRoleDao.deleteUserRoleByRoleId(roleId) != total,"角色删除失败!");
        }
        // 设置删除状态
        role.setIsValid(0);
        role.setUpdateDate(new Date());
        CheckParamsUtils.isTrue(roleDao.deleteByPrimaryKey(roleId) < 1,"角色删除失败!");
    }

    /**
     * 角色授权
     * @param mids
     * @param roleId
     */
    public  void addGrant(Integer[] mids, Integer roleId){
        /**
         * 授权思路  核心表
         *      直接批量添加  不适合，有可能对角色权限进行更新（权限更新后有可能添加新的菜单，删除旧的菜单，添加新的菜单）
         *      角色存在原始权限时，先删除原始权限记录，然后批量添加新的角色权限
         */
        int total = permissionDao.countPermissionByRoleId(roleId);
        if (total  > 0){
            CheckParamsUtils.isTrue(total != permissionDao.deletePermissionByRoleId(roleId),"角色授权失败!");
        }
        if (null != mids && mids.length > 0){
            List<Permission> permissions = new ArrayList<>();
            for (Integer mid:mids
                 ) {
                Permission permission = new Permission();
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                permission.setModuleId(mid);
                permission.setRoleId(roleId);
                permission.setAclValue(moduleDao.selectByPrimaryKey(mid).getOptValue());
                permissions.add(permission);
            }
            CheckParamsUtils.isTrue((permissionDao.insertBatch(permissions)!=permissions.size()),"角色授权失败！");
        }


    }

}
