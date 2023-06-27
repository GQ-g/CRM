package com.gq.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gq.base.BaseService;
import com.gq.crm.dao.CustomerLossDao;
import com.gq.crm.dao.CustomerReprDao;
import com.gq.crm.entity.CustomerReprieve;
import com.gq.crm.query.CustomerReprQuery;
import com.gq.crm.utils.CheckParamsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @date 2023/6/20
 */

@Service
public class CustomerReprService extends BaseService<CustomerReprieve,Integer> {

    @Resource
    private CustomerReprDao customerReprDao;
    @Resource
    private CustomerLossDao customerLossDao;

    public Map<String, Object> queryCustomerReprieveByParams(CustomerReprQuery customerReprieveQuery) {
        Map<String, Object> map = new HashMap<>();
        // 开启分页
        PageHelper.startPage(customerReprieveQuery.getPage(), customerReprieveQuery.getLimit());
        // 得到对应分页对象
        PageInfo<CustomerReprieve> pageInfo = new PageInfo<>(customerReprDao.selectByParams(customerReprieveQuery));
        // 设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        // 设置分页好的列表
        map.put("data",pageInfo.getList());

        return map;
    }


    public void saveCustomerRepr(CustomerReprieve customerReprieve) {
        /* 1. 参数校验 */
        checkParams(customerReprieve.getLossId(), customerReprieve.getMeasure());

        /* 2. 设置参数的默认值 */
        customerReprieve.setIsValid(1);
        customerReprieve.setCreateDate(new Date());
        customerReprieve.setUpdateDate(new Date());

        /* 3. 执行添加操作，判断受影响的行数 */
        CheckParamsUtils.isTrue(customerReprDao.insertSelective(customerReprieve) < 1, "添加暂缓数据失败！");
    }

    public void updateCustomerRepr(CustomerReprieve customerReprieve) {
        /* 1. 参数校验 */
        // 主键ID    id
        CheckParamsUtils.isTrue(null == customerReprieve.getId()
                || customerReprDao.selectByPrimaryKey(customerReprieve.getId()) == null, "待更新记录不存在！");
        // 参数校验
        checkParams(customerReprieve.getLossId(), customerReprieve.getMeasure());
        /* 2. 设置参数的默认值 */
        customerReprieve.setUpdateDate(new Date());
        /* 3. 执行修改操作，判断受影响的行数 */
        CheckParamsUtils.isTrue(customerReprDao.updateByPrimaryKeySelective(customerReprieve) < 1, "修改暂缓数据失败！");
    }

    public void deleteCustomerRepr(Integer id) {
        // 判断id是否为空
        CheckParamsUtils.isTrue(null == id, "待删除记录不存在！");
        // 通过id查询暂缓数据
        CustomerReprieve customerReprieve = customerReprDao.selectByPrimaryKey(id);
        // 判断数据是否存在
        CheckParamsUtils.isTrue(null == customerReprieve, "待删除记录不存在！");

        // 设置isValid
        customerReprieve.setIsValid(0);
        customerReprieve.setUpdateDate(new Date());

        // 执行更新操作，判断受影响的行数
        CheckParamsUtils.isTrue(customerReprDao.updateByPrimaryKeySelective(customerReprieve) < 1, "删除暂缓数据失败！");
    }

    private void checkParams(Integer lossId, String measure) {
        // 流失客户ID lossId    非空，数据存在
        CheckParamsUtils.isTrue(null == lossId
                || customerLossDao.selectByPrimaryKey(lossId) == null, "流失客户记录不存在！");
        // 暂缓措施内容 measure   非空
        CheckParamsUtils.isTrue(StringUtils.isBlank(measure), "暂缓措施内容不能为空！");
    }
}
