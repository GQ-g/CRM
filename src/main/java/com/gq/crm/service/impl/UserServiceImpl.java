package com.gq.crm.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gq.crm.Model.UserModel;
import com.gq.crm.dao.UserDao;
import com.gq.crm.dao.UserRoleDao;
import com.gq.crm.entity.User;
import com.gq.crm.entity.UserRole;
import com.gq.crm.query.UserQuery;
import com.gq.crm.service.UserService;
import com.gq.crm.utils.CheckParamsUtils;
import com.gq.crm.utils.PhoneUtil;
import com.gq.crm.utils.UserIDBase64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


/**
 * @date 2023/6/1
 */

@Service
public class UserServiceImpl  implements UserService {

    @Autowired
    private UserDao userDao;

    @Resource
    private UserRoleDao userRoleDao;

    @Override
    public UserModel login(String userName, String userPwd) {
        /**
         * 1.参数效验
         * 用户名 密码 非空
         * 2.根据用户名  查询用户记录
         * 3.用户存在效验
         * 不存在 -->记录不存在 方法结束
         * 4.用户存在
         *  效验密码
         *  密码错误  -->密码不正确  方法结束
         *  5.密码正确
         *  用户登录成功
         */
        checkLoginParams(userName, userPwd);

        User user = userDao.queryUserByUserName(userName);
        System.out.println(user);
        /*通过工具类判断用户是否存在*/
        CheckParamsUtils.isTrue(user == null,"用户不存在或已注销");
        System.out.println("用户输入的用户密码"+user.getUserPwd());
        CheckParamsUtils.isTrue(!(user.getUserPwd().equals(userPwd)),"用户密码不正确，请重新输入");
        return buildUserModel(user);
    }

    @Override
    public User selectByPrimaryKey(Integer id) {
        return userDao.selectByPrimaryKey(id);
    }

    private UserModel buildUserModel(User user){
        UserModel userModel = new UserModel();
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        System.out.println("真实姓名："+user.getTrueName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    private void checkLoginParams(String userName, String userPwd) {
        CheckParamsUtils.isTrue(StringUtils.isBlank(userName),"service用户名不能为空");
        CheckParamsUtils.isTrue(StringUtils.isBlank(userPwd),"service密码不能为空");
    }

    /*修改密码*/
    public Integer updateUserPassword(Integer userId, String oldPassword,String newPassword,String confirmPassword){

        checkParams(userId,oldPassword,newPassword,confirmPassword);
        User user = selectByPrimaryKey(userId);
//        user.setUserPwd(Md5Util.encode(newPassword));
        user.setUserPwd(newPassword);
        System.out.println(user.getUserPwd()+"修改后的密码");
        Integer integer = userDao.updatePwd(user);
        CheckParamsUtils.isTrue(integer < 1,"密码更新失败！");
        return integer;
    }

    @Override
    public List<Map<String, Object>> queryAllSales() {
        return userDao.queryAllSales();
    }

    @Override
    public Map<String, Object> queryUserByParams(UserQuery userQuery) {
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(userQuery.getPage(),userQuery.getLimit());
        PageInfo<User> pageInfo = new PageInfo<User>(userDao.selectByParams(userQuery));
        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 用户管理
     * 添加用户的功能
     * @param user
     */
    @Override
    public Boolean saveUser(User user){
        /**
         * 变换思路  如果查询到了用户，那么我就只更改用户信息
         */
      /*  User user1 = userDao.queryUserByUserName(user.getUserName());
        CheckParamsUtils.isTrue(user1 != null ,"用户已存在");*/
        /*通过try  catch 的方式解决异常的界面响应问题   程序的崩溃*/
            try{
                checkFormParams(user.getUserName(),user.getEmail(),user.getPhone(),null);
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
            user.setIsValid(1);
            user.setCreateDate(new Date());
            user.setUpdateDate(new Date());
            user.setUserPwd("123456");
            CheckParamsUtils.isTrue(userDao.insertSelective(user) < 1,"用户记录添加失败");

        /**
         * 用户角色管理   user_id  role_id
         */
        Integer userId = userDao.queryUserByUserName(user.getUserName()).getId();
        String roleIds = user.getRoleIds();
        /**
         * 批量添加用户角色记录到用户角色表
         */
        relationUserRoles(userId,roleIds);
        return true;
    }

    /**
     * 用户角色管理
     * @param userId
     * @param roleIds
     */
    private void relationUserRoles(Integer userId, String roleIds) {
        int total = userRoleDao.countUserRoleByUserId(userId);
        if (total > 0 ){
            CheckParamsUtils.isTrue(userRoleDao.deleteUserRoleByUserId(userId)!=total,"用户角色记录关联失败");
        }
        if (StringUtils.isNotBlank(roleIds)){
            List<UserRole> userRoles = new ArrayList<>();
            for (String s : roleIds.split(",")){
                UserRole userRole = new UserRole();
                userRole.setCreateDate(new Date());
                userRole.setRoleId(Integer.parseInt(s));
                userRole.setUpdateDate(new Date());
                userRole.setUserId(userId);
                userRoles.add(userRole);
            }
            CheckParamsUtils.isTrue(userRoleDao.insertBatch(userRoles) != userRoles.size(),"用户角色记录管理失败");
        }
    }


    /**
     * 用户管理
     * 更新用户的功能
     * @param user
     */
    @Override
    public void updateUser(User user) {

        User  temp = userDao.selectByPrimaryKey(user.getId());
        CheckParamsUtils.isTrue(null == temp,"带更新的用户记录不存在");
        checkFormParams(user.getUserName(),user.getEmail(),user.getPhone(),user.getId());

        temp = userDao.queryUserByUserName(user.getUserName());
        CheckParamsUtils.isTrue(null != temp && !(temp.getId().equals(user.getId())),"该用户已存在");
        user.setUpdateDate(new Date());
        CheckParamsUtils.isTrue(userDao.updateByPrimaryKeySelective(user) < 1,"用户记录修改失败");

        /**
         * 修改用户角色记录到用户角色表
         * 先删除原先的角色，在添加现有用户角色
         */
        relationUserRoles(user.getId(),user.getRoleIds());
    }

    @Override
    public void deleteUserByIds(Integer[] ids) {
        /**
         * 判断是否选中
         * 判断删除行数和返回的结果数是否相同
         */
        CheckParamsUtils.isTrue(null==ids || ids.length == 0, "请选择要删除的用户");
        CheckParamsUtils.isTrue(userDao.deleteBatch(ids) != ids.length,"用户记录删除失败");
    }

    @Override
    public List<Map<String, Object>> queryAllCustomerManagers() {
            return userDao.queryAllCustomerManagers();
    }

    private void checkFormParams(String userName, String email, String phone,Integer userId) {

        // 判断用户名是否为空
        CheckParamsUtils.isTrue(StringUtils.isBlank(userName), "用户名不能为空！");
        // 判断用户名的唯一性
        // 通过用户名查询用户对象
        User temp = userDao.queryUserByName(userName);
        // 如果用户对象为空，则表示用户名可用；如果用户对象不为空，则表示用户名不可用
        // 如果是添加操作，数据库中无数据，只要通过名称查到数据，则表示用户名被占用
        // 如果是修改操作，数据库中有对应的记录，通过用户名查到数据，可能是当前记录本身，也可能是别的记录
        // 如果用户名存在，且与当前修改记录不是同一个，则表示其他记录占用了该用户名，不可用
        CheckParamsUtils.isTrue(null != temp && !(temp.getId().equals(userId)), "用户名已存在，请重新输入！");



        System.out.println("输出了  用户名不能为空");
        CheckParamsUtils.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        // 邮箱 非空
        CheckParamsUtils.isTrue(StringUtils.isBlank(email), "用户邮箱不能为空！");
        // 手机号 非空
        CheckParamsUtils.isTrue(StringUtils.isBlank(phone), "用户手机号不能为空！");
        // 手机号 格式判断
        CheckParamsUtils.isTrue(!PhoneUtil.isMobile(phone), "手机号格式不正确！");
    }

    private void checkParams(Integer userId, String oldPassword, String newPassword, String confirmPassword) {
        User user = userDao.selectByPrimaryKey(userId);
        CheckParamsUtils.isTrue(userId == null  || user == null ,"用户未登录或不存在");
        CheckParamsUtils.isTrue(StringUtils.isBlank(oldPassword),"请输入旧密码");
        CheckParamsUtils.isTrue(StringUtils.isBlank(newPassword),"请输入新密码");
        CheckParamsUtils.isTrue(StringUtils.isBlank(confirmPassword),"请输入确认密码");
        CheckParamsUtils.isTrue(!(newPassword.equals(confirmPassword)),"新密码输入不一致");
        CheckParamsUtils.isTrue(!(user.getUserPwd().equals(oldPassword)),"原始密码不正确");
        CheckParamsUtils.isTrue(oldPassword.equals(newPassword),"新密码不能和旧密码相同");

    }






}
