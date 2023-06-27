package com.gq.crm.dao;

import com.gq.base.BaseDao;
import com.gq.crm.entity.CustomerLoss;
import com.gq.crm.query.CustomerLossQuery;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @date 2023/6/19
 */

@Repository
public interface CustomerLossDao extends BaseDao<CustomerLoss,Integer> {
    @Results(id = "baseResultMap", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "cus_no", property = "cusNo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "cus_name", property = "cusName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "cus_manager", property = "cusManager", jdbcType = JdbcType.VARCHAR),
            @Result(column = "last_order_time", property = "lastOrderTime", jdbcType = JdbcType.DATE),
            @Result(column = "confirm_loss_time", property = "confirmLossTime", jdbcType = JdbcType.DATE),
            @Result(column = "state", property = "state", jdbcType = JdbcType.INTEGER),
            @Result(column = "loss_reason", property = "lossReason", jdbcType = JdbcType.VARCHAR),
            @Result(column = "is_valid", property = "isValid", jdbcType = JdbcType.INTEGER),
            @Result(column = "create_date", property = "createDate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_date", property = "updateDate", jdbcType = JdbcType.TIMESTAMP)
    })
    @Select("select * from t_customer_loss where id = #{id}")
    CustomerLoss selectByPrimaryKey(Integer id);

    @Delete("delete from t_customer_loss where id = #{id}")
    Integer deleteByPrimaryKey(Integer id);

    @Insert("<script>" +
            "insert into t_customer_loss " +
            "<trim prefix='(' suffix=')' suffixOverrides=','>" +
            "    <if test='id != null'>id,</if>" +
            "    <if test='cusNo != null'>cus_no,</if>" +
            "    <if test='cusName != null'>cus_name,</if>" +
            "    <if test='cusManager != null'>cus_manager,</if>" +
            "    <if test='lastOrderTime != null'>last_order_time,</if>" +
            "    <if test='confirmLossTime != null'>confirm_loss_time,</if>" +
            "    <if test='state != null'>state,</if>" +
            "    <if test='lossReason != null'>loss_reason,</if>" +
            "    <if test='isValid != null'>is_valid,</if>" +
            "    <if test='createDate != null'>create_date,</if>" +
            "    <if test='updateDate != null'>update_date,</if>" +
            "</trim>" +
            "<trim prefix='values (' suffix=')' suffixOverrides=','>" +
            "    <if test='id != null'>#{id},</if>" +
            "    <if test='cusNo != null'>#{cusNo},</if>" +
            "    <if test='cusName != null'>#{cusName},</if>" +
            "    <if test='cusManager != null'>#{cusManager},</if>" +
            "    <if test='lastOrderTime != null'>#{lastOrderTime},</if>" +
            "    <if test='confirmLossTime != null'>#{confirmLossTime},</if>" +
            "    <if test='state != null'>#{state},</if>" +
            "    <if test='lossReason != null'>#{lossReason},</if>" +
            "    <if test='isValid != null'>#{isValid},</if>" +
            "    <if test='createDate != null'>#{createDate},</if>" +
            "    <if test='updateDate != null'>#{updateDate},</if>" +
            "</trim>" +
            "</script>")
    Integer insertSelective(CustomerLoss customerLoss);

    @Update("<script>" +
            "update t_customer_loss " +
            "<set>" +
            "    <if test='cusNo != null'>cus_no = #{cusNo},</if>" +
            "    <if test='cusName != null'>cus_name = #{cusName},</if>" +
            "    <if test='cusManager != null'>cus_manager = #{cusManager},</if>" +
            "    <if test='lastOrderTime != null'>last_order_time = #{lastOrderTime},</if>" +
            "    <if test='confirmLossTime != null'>confirm_loss_time = #{confirmLossTime},</if>" +
            "    <if test='state != null'>state = #{state},</if>" +
            "    <if test='lossReason != null'>loss_reason = #{lossReason},</if>" +
            "    <if test='isValid != null'>is_valid = #{isValid},</if>" +
            "    <if test='createDate != null'>create_date = #{createDate},</if>" +
            "    <if test='updateDate != null'>update_date = #{updateDate},</if>" +
            "</set>" +
            "where id = #{id}" +
            "</script>")
    Integer updateByPrimaryKeySelective(CustomerLoss customerLoss);

    @Insert("<script>" +
            "insert into t_customer_loss (cus_no, cus_name, cus_manager, last_order_time, confirm_loss_time, state, loss_reason, is_valid, create_date, update_date) values " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.cusNo}, #{item.cusName}, #{item.cusManager}, #{item.lastOrderTime}, #{item.confirmLossTime}, #{item.state}, #{item.lossReason}, 1, now(), now())" +
            "</foreach>" +
            "</script>")
    Integer insertBatch(List<CustomerLoss> customerLossList);

    @Select("<script>" +
            "select * from t_customer_loss " +
            "where is_valid = 1 " +
            "<if test='cusNo != null and cusNo != \"\"'>" +
            "    and cus_no = #{cusNo}" +
            "</if>" +
            "<if test='cusName != null and cusName != \"\"'>" +
            "    and cus_name like concat('%', #{cusName}, '%')" +
            "</if>" +
            "<if test='state != null'>" +
            "    and state = #{state}" +
            "</if>" +
            "</script>")
    @ResultMap("baseResultMap")
    List<CustomerLoss> selectByParams(CustomerLossQuery customerLossQuery);

}
