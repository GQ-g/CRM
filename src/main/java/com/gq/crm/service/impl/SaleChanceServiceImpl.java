package com.gq.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gq.base.BaseService;
import com.gq.crm.dao.SaleChanceDao;
import com.gq.crm.entity.SaleChance;
import com.gq.crm.enums.DevResult;
import com.gq.crm.enums.StateStatus;
import com.gq.crm.query.SaleChanceQuery;
import com.gq.crm.service.SaleChanceService;
import com.gq.crm.utils.CheckParamsUtils;
import com.gq.crm.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @date 2023/6/13
 */

@Service
public class SaleChanceServiceImpl extends BaseService<SaleChance, Integer> implements SaleChanceService {
    @Resource
    private SaleChanceDao saleChanceDao;

    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery){
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getLimit());
        /*传入实体类的参数 作为查询条件*/
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChanceDao.selectByParams(saleChanceQuery));
        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 参数效验
     * 效验那些参数不能为空
     * @param saleChance
     */
    @Override
    public void saveSaleChance(SaleChance saleChance) {
        /**
         * 1.参数效验
         *    客户名非空   customerName
         *    linkMan  非空
         *    linkPhone 非空
         * 2.设置相关参数的默认值
         *      status 默认未分配  如果选择分配人 status 为已分配状态
         *      assignTime 默认空 如果选择分配人  分配时间为系统当前时间
         *      isValid 默认有效（1~有效  0~无效）
         *      createDate  updateDate 默认系统时间
         * 3.执行添加 判断添加结果
         */
        checkParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());

        saleChance.setState(StateStatus.UNSTATE.getType());
        saleChance.setDevResult(DevResult.UNDEV.getStatus());
        /*判断分配人是否为空*/
        if (StringUtils.isNotBlank(saleChance.getAssignMan())){
            saleChance.setState(StateStatus.STATED.getType());
            saleChance.setDevResult(DevResult.DEVING.getStatus());
            saleChance.setAssignTime(new Date());
        }
        saleChance.setIsValid(1);
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());

        CheckParamsUtils.isTrue(insertSelective(saleChance) < 1,"添加失败");

    }

    /**
     * 更新机会
     * @param saleChance
     */
    @Override
    public void updateSaleChance(SaleChance saleChance) {
        /**
         * 1.参数校验
         *  id 记录存在校验
         *  customerName:非空
         *  linkMan:非空
         *  linkPhone:非空 11位手机号
         * 2. 设置相关参数值
         *      updateDate:系统当前时间
         *         原始记录 未分配 修改后改为已分配(由分配人决定)
         *            state 0->1
         *            assginTime 系统当前时间
         *            devResult 0-->1
         *         原始记录  已分配  修改后 为未分配
         *            state  1-->0
         *            assignTime  待定  null
         *            devResult 1-->0
         * 3.执行更新 判断结果
         */
        System.out.println("saleChance.getId()"+saleChance.getId());
        CheckParamsUtils.isTrue( null ==saleChance.getId(),"待更新记录不存在!");
        SaleChance temp =saleChanceDao.selectByPrimaryKey(saleChance.getId());
        CheckParamsUtils.isTrue( null ==temp,"待更新记录不存在!");
        checkParams(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());
        if(StringUtils.isBlank(temp.getAssignMan()) && StringUtils.isNotBlank(saleChance.getAssignMan())){
            saleChance.setState(StateStatus.STATED.getType());
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(DevResult.DEVING.getStatus());
        }else if(StringUtils.isNotBlank(temp.getAssignMan()) && StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setAssignMan("");
            saleChance.setState(StateStatus.UNSTATE.getType());
            saleChance.setAssignTime(null);
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
        }
        saleChance.setUpdateDate(new Date());
        CheckParamsUtils.isTrue(saleChanceDao.updateByPrimaryKeySelective(saleChance)<1,"机会数据更新失败!");
    }

    /**
     * 验证参数
     * @param customerName
     * @param linkMan
     * @param linkPhone
     */
    private void checkParams(String customerName, String linkMan, String linkPhone) {
        CheckParamsUtils.isTrue(StringUtils.isBlank(customerName),"请输入客户名");
        CheckParamsUtils.isTrue(StringUtils.isBlank(linkMan),"请输入人");
        CheckParamsUtils.isTrue(StringUtils.isBlank(linkPhone),"请输入手机号");
        CheckParamsUtils.isTrue(PhoneUtil.isMobile(customerName),"手机号格式不正确");
    }

    @Override
    public SaleChance selectByPrimaryKey(Integer integer) throws DataAccessException {
        return saleChanceDao.selectByPrimaryKey(integer);
    }

    /**
     * 删除营销机会
     * @param ids
     */
    @Override
    public void deleteSaleChance(Integer[] ids) {
        /*保证参数不能为空*/
        CheckParamsUtils.isTrue(null == ids || ids.length == 0,"请选择待删除记录");
        CheckParamsUtils.isTrue(saleChanceDao.deleteBatch(ids)!=ids.length,"删除失败");
    }

    @Override
    public void updateSaleChanceDevResultVo(Integer id, Integer devResult) {
        SaleChance temp = saleChanceDao.selectByPrimaryKey(id);
        CheckParamsUtils.isTrue(null == temp , "待更新记录不存在！");
        temp.setDevResult(devResult);
        CheckParamsUtils.isTrue(saleChanceDao.updateByPrimaryKeySelective(temp) < 1,"机会数据更新失败 ");
    }


}
