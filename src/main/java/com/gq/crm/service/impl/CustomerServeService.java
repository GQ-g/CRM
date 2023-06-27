package com.gq.crm.service.impl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gq.base.BaseService;
import com.gq.crm.dao.CustomerDao;
import com.gq.crm.dao.CustomerServeDao;
import com.gq.crm.dao.UserDao;
import com.gq.crm.entity.CustomerServe;
import com.gq.crm.enums.CustomerServeStatus;
import com.gq.crm.query.CustomerServeQuery;
import com.gq.crm.utils.CheckParamsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerServeService extends BaseService<CustomerServe, Integer> {

        @Resource
        private CustomerServeDao customerServeDao;
        @Resource
        private CustomerDao customerDao;
        @Resource
        private UserDao userDao;

    public Map<String, Object> queryCustomerServeByParams(CustomerServeQuery customerServeQuery) {
        Map<String, Object> map = new HashMap<>();
        // 开启分页
        PageHelper.startPage(customerServeQuery.getPage(), customerServeQuery.getLimit());
        // 得到对应分页对象
        PageInfo<CustomerServe> pageInfo = new PageInfo<>(customerServeDao.selectByParams(customerServeQuery));
        // 设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        // 设置分页好的列表
        map.put("data",pageInfo.getList());
        return map;
    }


    public void saveCustomerServe(CustomerServe customerServe) {

        /* 1. 参数校验 */
        // 客户名 customer     非空
        CheckParamsUtils.isTrue(StringUtils.isBlank(customerServe.getCustomer()), "客户名不能为空！");
        // 客户名 customer     客户表中存在客户记录
        CheckParamsUtils.isTrue(customerDao.queryCustomerByName(customerServe.getCustomer()) == null, "客户不存在！" );

        // 服务类型 serveType  非空
        CheckParamsUtils.isTrue(StringUtils.isBlank(customerServe.getServeType()), "请选择服务类型！");

        //  服务请求内容  serviceRequest  非空
        CheckParamsUtils.isTrue(StringUtils.isBlank(customerServe.getServiceRequest()), "服务请求内容不能为空！");

        /* 2. 设置参数的默认值 */
        //  服务状态    服务创建状态  fw_001
        customerServe.setState(CustomerServeStatus.CREATED.getState());
        customerServe.setIsValid(1);
        customerServe.setCreateDate(new Date());
        customerServe.setUpdateDate(new Date());

        /* 2. 执行添加操作，判断受影响的行数 */
        CheckParamsUtils.isTrue(customerServeDao.insertSelective(customerServe) < 1, "添加服务失败！");
    }


    public void updateCustomerServe(CustomerServe customerServe) {
        // 客户服务ID  非空且记录存在
        CheckParamsUtils.isTrue(customerServe.getId() == null
                || customerServeDao.selectByPrimaryKey(customerServe.getId()) == null, "待更新的服务记录不存在！");
        // 判断客户服务的服务状态
        if (CustomerServeStatus.ASSIGNED.getState().equals(customerServe.getState())) {
            // 服务分配操作
            // 分配人       非空，分配用户记录存在
            CheckParamsUtils.isTrue(StringUtils.isBlank(customerServe.getAssigner()), "待分配用户不能为空！");
            CheckParamsUtils.isTrue(userDao.selectByPrimaryKey(Integer.parseInt(customerServe.getAssigner())) == null, "待分配用户不存在！");
            // 分配时间     系统当前时间
            customerServe.setAssignTime(new Date());
        } else if (CustomerServeStatus.PROCED.getState().equals(customerServe.getState())) {
            // 服务处理操作
            // 服务处理内容   非空
            CheckParamsUtils.isTrue(StringUtils.isBlank(customerServe.getServiceProce()), "服务处理内容不能为空！");
            // 服务处理时间   系统当前时间
            System.out.println(new Date());
            customerServe.setServiceProceTime(new Date());
        } else if (CustomerServeStatus.FEED_BACK.getState().equals(customerServe.getState())) {
            // 服务反馈操作
            // 服务反馈内容   非空
            CheckParamsUtils.isTrue(StringUtils.isBlank(customerServe.getServiceProceResult()), "服务反馈内容不能为空！");
            // 服务满意度     非空
            CheckParamsUtils.isTrue(StringUtils.isBlank(customerServe.getMyd()), "请选择服务反馈满意度！");
            // 服务状态      设置为 服务归档状态 fw_005
            customerServe.setState(CustomerServeStatus.ARCHIVED.getState());
        }

        // 更新时间     系统当前时间
        customerServe.setUpdateDate(new Date());

        // 执行更新操作，判断受影响的行数
        CheckParamsUtils.isTrue(customerServeDao.updateByPrimaryKeySelective(customerServe)< 1, "服务更新失败！");

    }
}
