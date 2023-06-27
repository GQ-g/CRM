package com.gq.crm.dao;

import com.gq.base.BaseDao;
import com.gq.crm.entity.OrderDetails;
import com.gq.crm.query.OrderDetailsQuery;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @date 2023/6/19
 */

@Repository
public interface OrderDetailsDao extends BaseDao<OrderDetails,Integer> {


    String BASE_RESULT_MAP =
            "id, order_id, goods_name, goods_num, unit, price, sum, is_valid, create_date, update_date";

    @Select("select " + BASE_RESULT_MAP + " from t_order_details where id = #{id,jdbcType=INTEGER}")
    @Results(id = "BaseResultMap", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "order_id", property = "orderId", jdbcType = JdbcType.INTEGER),
            @Result(column = "goods_name", property = "goodsName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "goods_num", property = "goodsNum", jdbcType = JdbcType.INTEGER),
            @Result(column = "unit", property = "unit", jdbcType = JdbcType.VARCHAR),
            @Result(column = "price", property = "price", jdbcType = JdbcType.REAL),
            @Result(column = "sum", property = "sum", jdbcType = JdbcType.REAL),
            @Result(column = "is_valid", property = "isValid", jdbcType = JdbcType.INTEGER),
            @Result(column = "create_date", property = "createDate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_date", property = "updateDate", jdbcType = JdbcType.TIMESTAMP)
    })
    OrderDetails selectByPrimaryKey(Integer id);

    @Delete("delete from t_order_details where id = #{id,jdbcType=INTEGER}")
    Integer deleteByPrimaryKey(Integer id);

    @Insert("insert into t_order_details " +
            "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >" +
            "<if test=\"id != null\" > id, </if>" +
            "<if test=\"orderId != null\" > order_id, </if>" +
            "<if test=\"goodsName != null\" > goods_name, </if>" +
            "<if test=\"goodsNum != null\" > goods_num, </if>" +
            "<if test=\"unit != null\" > unit, </if>" +
            "<if test=\"price != null\" > price, </if>" +
            "<if test=\"sum != null\" > sum, </if>" +
            "<if test=\"isValid != null\" > is_valid, </if>" +
            "<if test=\"createDate != null\" > create_date, </if>" +
            "<if test=\"updateDate != null\" > update_date, </if>" +
            "</trim>" +
            "<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >" +
            "<if test=\"id != null\" > #{id,jdbcType=INTEGER},</if>" +
            "<if test=\"orderId != null\" > #{orderId,jdbcType=INTEGER},</if>" +
            "<if test=\"goodsName != null\" > #{goodsName,jdbcType=VARCHAR},</if>" +
            "<if test=\"goodsNum != null\" > #{goodsNum,jdbcType=INTEGER},</if>" +
            "<if test=\"unit != null\" > #{unit,jdbcType=VARCHAR},</if>" +
            "<if test=\"price != null\" > #{price,jdbcType=REAL},</if>" +
            "<if test=\"sum != null\" > #{sum,jdbcType=REAL},</if>" +
            "<if test=\"isValid != null\" > #{isValid,jdbcType=INTEGER},</if>" +
            "<if test=\"createDate != null\" > #{createDate,jdbcType=TIMESTAMP},</if>" +
            "<if test=\"updateDate != null\" > #{updateDate,jdbcType=TIMESTAMP},</if>" +
            "</trim>")
    Integer insertSelective(OrderDetails record);

    @Update("<script>" +
            "update t_order_details " +
            "<set>" +
            "<if test=\"orderId != null\" > order_id = #{orderId,jdbcType=INTEGER}, </if>" +
            "<if test=\"goodsName != null\" > goods_name = #{goodsName,jdbcType=VARCHAR}, </if>" +
            "<if test=\"goodsNum != null\" > goods_num = #{goodsNum,jdbcType=INTEGER}, </if>" +
            "<if test=\"unit != null\" > unit = #{unit,jdbcType=VARCHAR}, </if>" +
            "<if test=\"price != null\" > price = #{price,jdbcType=REAL}, </if>" +
            "<if test=\"sum != null\" > sum = #{sum,jdbcType=REAL}, </if>" +
            "<if test=\"isValid != null\" > is_valid = #{isValid,jdbcType=INTEGER}, </if>" +
            "<if test=\"createDate != null\" > create_date = #{createDate,jdbcType=TIMESTAMP}, </if>" +
            "<if test=\"updateDate != null\" > update_date = #{updateDate,jdbcType=TIMESTAMP}, </if>" +
            "</set>" +
            "where id = #{id,jdbcType=INTEGER}" +
            "</script>")
    Integer updateByPrimaryKeySelective(OrderDetails record);

    @Select("select " + BASE_RESULT_MAP + " from t_order_details where is_valid = 1 and order_id=#{orderId}")
    @ResultMap("BaseResultMap")
    List<OrderDetails> selectByParams(OrderDetailsQuery query);
}
