package com.gq.crm.service.impl;

import com.gq.base.BaseService;
import com.gq.crm.dao.UserDao;
import com.gq.crm.entity.UserRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @date 2023/6/16
 */

@Service
public class UserRoleService extends BaseService<UserRole,Integer> {

    @Resource
    private UserDao userDao;

    /**
     * 业务层代码
     */
}
