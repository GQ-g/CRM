package com.gq.crm.service;

import com.gq.crm.Model.UserModel;
import com.gq.crm.entity.User;
import com.gq.crm.query.UserQuery;

import java.util.List;
import java.util.Map;

/**
 * @date 2023/6/1
 */


public interface UserService {

    /*用户登录接口*/
    UserModel login(String userName, String userPwd);
    /**/
    User selectByPrimaryKey(Integer id);

    Integer updateUserPassword(Integer userId, String oldPwd, String newPwd, String repeatPwd);

    List<Map<String,Object>> queryAllSales();

    Map<String,Object> queryUserByParams(UserQuery userQuery);


    Boolean saveUser(User user);

    void updateUser(User user);


    void deleteUserByIds(Integer[] ids);


    public List<Map<String, Object>> queryAllCustomerManagers();
}
