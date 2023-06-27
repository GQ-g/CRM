package com.gq.crm.service.impl;

import com.gq.base.BaseService;
import com.gq.crm.dao.PermissionDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @date 2023/6/19
 */

@Service
public class PermissionService{

    @Resource
    private PermissionDao permissionDao;

    public List<String> queryUserHasRoleHasPermissionByUserId(Integer userId) {
        return permissionDao.queryUserHasRoleHasPermissionByUserId(userId);
    }
}
