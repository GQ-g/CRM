package com.gq.crm.dao;

import com.gq.base.BaseDao;
import com.gq.crm.entity.CustomerOrder;
import com.gq.crm.query.CustomerOrderQuery;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @date 2023/6/19
 */

@Repository
public interface CustomerOrderDao extends BaseDao<CustomerOrder,Integer> {

    String BASE_COLUMN_LIST = "id, cus_id, order_no, order_date, address, state, create_date, update_date, is_valid";

    @Select("select " + BASE_COLUMN_LIST + " from t_customer_order where id = #{id,jdbcType=INTEGER}")
    @Results(id = "BaseResultMap", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "cus_id", property = "cusId", jdbcType = JdbcType.INTEGER),
            @Result(column = "order_no", property = "orderNo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "order_date", property = "orderDate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "address", property = "address", jdbcType = JdbcType.VARCHAR),
            @Result(column = "state", property = "state", jdbcType = JdbcType.INTEGER),
            @Result(column = "create_date", property = "createDate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_date", property = "updateDate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "is_valid", property = "isValid", jdbcType = JdbcType.INTEGER)
    })
    CustomerOrder selectByPrimaryKey(Integer id);

    @Delete("delete from t_customer_order where id = #{id,jdbcType=INTEGER}")
    Integer deleteByPrimaryKey(Integer id);

    @Insert({
            "insert into t_customer_order (",
            "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >",
            "<if test=\"id != null\" >id,</if>",
            "<if test=\"cusId != null\" >cus_id,</if>",
            "<if test=\"orderNo != null\" >order_no,</if>",
            "<if test=\"orderDate != null\" >order_date,</if>",
            "<if test=\"address != null\" >address,</if>",
            "<if test=\"state != null\" >state,</if>",
            "<if test=\"createDate != null\" >create_date,</if>",
            "<if test=\"updateDate != null\" >update_date,</if>",
            "<if test=\"isValid != null\" >is_valid,</if>",
            "</trim>",
            "<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >",
            "<if test=\"id != null\" >#{id,jdbcType=INTEGER},</if>",
            "<if test=\"cusId != null\" >#{cusId,jdbcType=INTEGER},</if>",
            "<if test=\"orderNo != null\" >#{orderNo,jdbcType=VARCHAR},</if>",
            "<if test=\"orderDate != null\" >#{orderDate,jdbcType=TIMESTAMP},</if>",
            "<if test=\"address != null\" >#{address,jdbcType=VARCHAR},</if>",
            "<if test=\"state != null\" >#{state,jdbcType=INTEGER},</if>",
            "<if test=\"createDate != null\" >#{createDate,jdbcType=TIMESTAMP},</if>",
            "<if test=\"updateDate != null\" >#{updateDate,jdbcType=TIMESTAMP},</if>",
            "<if test=\"isValid != null\" >#{isValid,jdbcType=INTEGER},</if>",
            "</trim>",
    })
    Integer insertSelective(CustomerOrder record);

    @Update({
            "update t_customer_order",
            "<set>",
            "<if test=\"cusId != null\" >cus_id = #{cusId,jdbcType=INTEGER},</if>",
            "<if test=\"orderNo != null\" >order_no = #{orderNo,jdbcType=VARCHAR},</if>",
            "<if test=\"orderDate != null\" >order_date = #{orderDate,jdbcType=TIMESTAMP},</if>",
            "<if test=\"address != null\" >address = #{address,jdbcType=VARCHAR},</if>",
            "<if test=\"state != null\" >state = #{state,jdbcType=INTEGER},</if>",
            "<if test=\"createDate != null\" >create_date = #{createDate,jdbcType=TIMESTAMP},</if>",
            "<if test=\"updateDate != null\" >update_date = #{updateDate,jdbcType=TIMESTAMP},</if>",
            "<if test=\"isValid != null\" >is_valid = #{isValid,jdbcType=INTEGER},</if>",
            "</set>",
            "where id = #{id,jdbcType=INTEGER}"
    })
    Integer updateByPrimaryKeySelective(CustomerOrder record);

    @Select({
            "<script>"+
                    "select",
            BASE_COLUMN_LIST,
            "from t_customer_order",
            "where is_valid = 1",
            "<if test=\"null != cusId\">",
            "and cus_id = #{cusId}",
            "</if> " +
                    "</script>"
    })
    @ResultMap("BaseResultMap")
    List<CustomerOrder> selectByParams(CustomerOrderQuery query);

    @Select({
            "SELECT",
            "o.id,",
            "o.order_no,",
            "o.address,",
            "CASE",
            "WHEN o.state = 1 THEN '已支付'",
            "WHEN o.state = 0 THEN '未支付'",
            "END status ,",
            "sum(od.sum) total",
            "FROM t_customer_order o",
            "LEFT JOIN t_order_details od ON o.id = od.order_id",
            "WHERE o.is_valid = 1 AND o.id = #{orderId} AND od.is_valid = 1"
    })
    @Results(id = "queryOrderByIdResultMap", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "order_no", property = "orderNo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "address", property = "address", jdbcType = JdbcType.VARCHAR),
            @Result(column = "status", property = "status", jdbcType = JdbcType.VARCHAR),
            @Result(column = "total", property = "total", jdbcType = JdbcType.DECIMAL)
    })
    Map<String, Object> queryOrderById(Integer orderId);

    @Select({
            "SELECT *",
            "FROM t_customer_order",
            "WHERE is_valid = 1 AND cus_id = #{customerId}",
            "ORDER BY order_date DESC",
            "LIMIT 1"
    })
    @ResultMap("BaseResultMap")
    CustomerOrder queryLossCustomerOrderByCustomerId(Integer customerId);
}
