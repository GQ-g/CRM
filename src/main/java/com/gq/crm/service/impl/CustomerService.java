package com.gq.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gq.base.BaseService;
import com.gq.crm.dao.CustomerDao;
import com.gq.crm.dao.CustomerLossDao;
import com.gq.crm.dao.CustomerOrderDao;
import com.gq.crm.entity.Customer;
import com.gq.crm.entity.CustomerLoss;
import com.gq.crm.entity.CustomerOrder;
import com.gq.crm.query.CustomerQuery;
import com.gq.crm.utils.CheckParamsUtils;
import com.gq.crm.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @date 2023/6/19
 */

@Service
public class CustomerService extends BaseService<Customer,Integer> {
    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerLossDao customerLossDao;

    @Autowired
    private CustomerOrderDao customerOrderDao;

    public Map<String, Object> queryCustomerByParams(CustomerQuery customerQuery) {

        Map<String, Object> map = new HashMap<>();

        // 开启分页
        PageHelper.startPage(customerQuery.getPage(), customerQuery.getLimit());
        // 得到对应分页对象
        PageInfo<Customer> pageInfo = new PageInfo<>(customerDao.selectByParams(customerQuery));

        // 设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        // 设置分页好的列表
        map.put("data",pageInfo.getList());
        return map;
    }

    public void saveCustomer(Customer customer){
        checkCustomerParams(customer.getName(),customer.getPhone(),customer.getFr());
        // 判断客户名的唯一性
        Customer temp = customerDao.queryCustomerByName(customer.getName());
        // 判断名客户名称是否存在
        CheckParamsUtils.isTrue(null != temp, "客户名称已存在，请重新输入！");

        /* 2. 设置参数的默认值 */
        customer.setIsValid(1);
        customer.setCreateDate(new Date());
        customer.setUpdateDate(new Date());
        customer.setState(0);
        // 客户编号 唯一的客户编号  系统当前时间的毫秒数
        String khno = "KH" + System.currentTimeMillis();
        customer.setKhno(khno);

        /* 3. 执行添加操作，判断受影响的行数 */
        CheckParamsUtils.isTrue(customerDao.insertSelective(customer) < 1, "添加客户信息失败！");
    }

    public void updateCustomer(Customer customer) {
        /* 1. 参数校验 */
        CheckParamsUtils.isTrue(null == customer.getId(), "待更新记录不存在！");
        // 通过客户ID查询客户记录
        Customer temp = customerDao.selectByPrimaryKey(customer.getId());
        // 判断客户记录是否存在
        CheckParamsUtils.isTrue(null == temp, "待更新记录不存在！");
        // 参数校验
        checkCustomerParams(customer.getName(), customer.getFr(), customer.getPhone());
        // 通过客户名称查询客户记录
        temp = customerDao.queryCustomerByName(customer.getName());
        // 判断客户记录 是否存在，且客户id是否与更新记录的id一致
        CheckParamsUtils.isTrue(null != temp && !(temp.getId()).equals(customer.getId()), "客户名称已存在，请重新输入！");

        /* 2. 设置参数的默认值  */
        customer.setUpdateDate(new Date());

        /* 3. 执行更新操作，判断受影响的行数 */
        CheckParamsUtils.isTrue(customerDao.updateByPrimaryKeySelective(customer) < 1, "修改客户信息失败！");
    }

    private void checkCustomerParams(String name, String fr, String phone) {
        // 客户名称 name    非空
        CheckParamsUtils.isTrue(StringUtils.isBlank(name), "客户名称不能为空！");
        // 法人代表 fr      非空
        CheckParamsUtils.isTrue(StringUtils.isBlank(fr), "法人代表不能为空！");
        // 手机号码 phone   非空
        // CheckParamsUtils.isTrue(StringUtils.isBlank(phone),"手机号码不能为空！");
        // 手机号码 phone   格式正确
        CheckParamsUtils.isTrue(!PhoneUtil.isMobile(phone), "手机号码格式不正确！");
    }

    public void deleteCustomer (Integer id){
        // 判断id是否为空，数据是否存在
        CheckParamsUtils.isTrue(null == id, "待删除记录不存在！");
        // 通过id查询客户记录
        Customer customer = customerDao.selectByPrimaryKey(id);
        CheckParamsUtils.isTrue(null == customer, "待删除记录不存在！");

        // 设置状态为失效
        customer.setIsValid(0);
        customer.setUpdateDate(new Date());

        // 执行删除（更新）操作，判断受影响的行数
        CheckParamsUtils.isTrue(customerDao.updateByPrimaryKeySelective(customer) < 1, "删除客户信息失败！");
    }

    @Override
    public Customer selectByPrimaryKey(Integer id) throws DataAccessException {
        return customerDao.selectByPrimaryKey(id);
    }

    /**
     * 客户流失管理
     * 1.查询待流失的客户
     * 2.将流失的客户数据批量化添加到客户流失表中
     * 3.批量更新客户流失状态
     * 4.通过定时任务 来执行方法
     */
    public void updateCustomerState(){
        /* 1. 查询待流失的客户数据 */
        List<Customer> lossCustomerList = customerDao.queryLossCustomers();
        /* 2. 将流失客户数据批量添加到客户流失表中 */
        // 判断流失客户数据是否存在
        if (lossCustomerList != null && lossCustomerList.size() > 0) {
            // 定义集合 用来接收流失客户的ID
            List<Integer> lossCustomerIds = new ArrayList<>();
            // 定义流失客户的列表
            List<CustomerLoss> customerLossList = new ArrayList<>();
            // 遍历查询到的流失客户的数据
            lossCustomerList.forEach(customer -> {
                // 定义流失客户对象
                CustomerLoss customerLoss = new CustomerLoss();
                // 创建时间  系统当前时间
                customerLoss.setCreateDate(new Date());
                // 客户经理
                customerLoss.setCusManager(customer.getCusManager());
                // 客户名称
                customerLoss.setCusName(customer.getName());
                // 客户编号
                customerLoss.setCusNo(customer.getKhno());
                // 是否有效  1=有效
                customerLoss.setIsValid(1);
                // 修改时间  系统当前时间
                customerLoss.setUpdateDate(new Date());
                // 客户流失状态   0=暂缓流失状态  1=确认流失状态
                customerLoss.setState(0);
                // 客户最后下单时间
                // 通过客户ID查询最后的订单记录（最后一条订单记录）
                CustomerOrder customerOrder = customerOrderDao.queryLossCustomerOrderByCustomerId(customer.getId());
                // 判断客户订单是否存在，如果存在，则设置最后下单时间
                if (customerOrder != null) {
                    customerLoss.setLastOrderTime(customerOrder.getOrderDate());
                }
                // 将流失客户对象设置到对应的集合中
                customerLossList.add(customerLoss);

                // 将流失客户的ID设置到对应的集合中
                lossCustomerIds.add(customer.getId());
            });

            // 批量添加流失客户记录
            CheckParamsUtils.isTrue(customerLossDao.insertBatch(customerLossList) != customerLossList.size(), "客户流失数据转移失败！");

            /* 3. 批量更新客户的流失状态 */
            CheckParamsUtils.isTrue(customerDao.updateCustomerStateByIds(lossCustomerIds) != lossCustomerIds.size(), "客户流失数据转移失败！");
        }
    }

    public Map<String, Object> queryCustomerContributionByParams(CustomerQuery customerQuery) {
        Map<String, Object> map = new HashMap<>();
        // 开启分页
        PageHelper.startPage(customerQuery.getPage(), customerQuery.getLimit());
        // 得到对应分页对象
        PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(customerDao.queryCustomerContributionByParams(customerQuery));
        // 设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        // 设置分页好的列表
        map.put("data",pageInfo.getList());
        return map;
    }


    public Map<String, Object> countCustomerMake() {
        Map<String,Object> result = new HashMap<>();
        List<Map<String,Object>> list = customerDao.countCustomerMake();
        List<Object> data1 = new ArrayList<>();
        List<Object> data2 = new ArrayList<>();
        for(Map<String,Object> map : list){
            data1.add(map.get("level"));
            data2.add(map.get("total"));
        }
        result.put("data1",data1);
        result.put("data2",data2);
        return result;
    }

    public Map<String, Object> countCustomerMake02() {
        Map<String,Object> result = new HashMap<>();
        List<Map<String,Object>> list = customerDao.countCustomerMake();
        List<Object> data1 = new ArrayList<>();
        List<Map<String,Object>> data2 = new ArrayList<>();
        for(Map<String,Object> map : list){
//            {total=4, level=战略合作伙伴}
////            {total=2, level=大客户}
////            {total=1, level=重点开发客户}
////            {total=1, level=合作伙伴}
            data1.add(map.get("level"));
            Map<String,Object> m = new HashMap<>();
            m.put("name",map.get("level"));
            m.put("value",map.get("total"));
            data2.add(m);
        }
        result.put("data1",data1);
        result.put("data2",data2);
        return result;
    }

}
