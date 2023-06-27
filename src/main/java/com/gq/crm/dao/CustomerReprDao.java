package com.gq.crm.dao;

import com.gq.base.BaseDao;
import com.gq.crm.entity.CustomerReprieve;
import com.gq.crm.query.CustomerReprQuery;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @date 2023/6/20
 */

@Repository
public interface CustomerReprDao extends BaseDao<CustomerReprieve,Integer> {
    @Results(id = "BaseResultMap", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "loss_id", property = "lossId", jdbcType = JdbcType.INTEGER),
            @Result(column = "measure", property = "measure", jdbcType = JdbcType.VARCHAR),
            @Result(column = "is_valid", property = "isValid", jdbcType = JdbcType.INTEGER),
            @Result(column = "create_date", property = "createDate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_date", property = "updateDate", jdbcType = JdbcType.TIMESTAMP)
    })
    @Select("select id, loss_id, measure, is_valid, create_date, update_date from t_customer_reprieve where id = #{id,jdbcType=INTEGER}")
    CustomerReprieve selectByPrimaryKey(Integer id);

    @Delete("delete from t_customer_reprieve where id = #{id,jdbcType=INTEGER}")
    Integer deleteByPrimaryKey(Integer id);

    @Insert({
            "<script>",
            "insert into t_customer_reprieve",
            "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >",
            "<if test=\"id != null\" >",
            "id,",
            "</if>",
            "<if test=\"lossId != null\" >",
            "loss_id,",
            "</if>",
            "<if test=\"measure != null\" >",
            "measure,",
            "</if>",
            "<if test=\"isValid != null\" >",
            "is_valid,",
            "</if>",
            "<if test=\"createDate != null\" >",
            "create_date,",
            "</if>",
            "<if test=\"updateDate != null\" >",
            "update_date,",
            "</if>",
            "</trim>",
            "<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >",
            "<if test=\"id != null\" >",
            "#{id,jdbcType=INTEGER},",
            "</if>",
            "<if test=\"lossId != null\" >",
            "#{lossId,jdbcType=INTEGER},",
            "</if>",
            "<if test=\"measure != null\" >",
            "#{measure,jdbcType=VARCHAR},",
            "</if>",
            "<if test=\"isValid != null\" >",
            "#{isValid,jdbcType=INTEGER},",
            "</if>",
            "<if test=\"createDate != null\" >",
            "#{createDate,jdbcType=TIMESTAMP},",
            "</if>",
            "<if test=\"updateDate != null\" >",
            "#{updateDate,jdbcType=TIMESTAMP},",
            "</if>",
            "</trim>",
            "</script>"
    })
    Integer insertSelective(CustomerReprieve record);

    @Update({
            "<script>",
            "update t_customer_reprieve",
            "<set>",
            "<if test=\"lossId != null\" >",
            "loss_id = #{lossId,jdbcType=INTEGER},",
            "</if>",
            "<if test=\"measure != null\" >",
            "measure = #{measure,jdbcType=VARCHAR},",
            "</if>",
            "<if test=\"isValid != null\" >",
            "is_valid = #{isValid,jdbcType=INTEGER},",
            "</if>",
            "<if test=\"createDate != null\" >",
            "create_date = #{createDate,jdbcType=TIMESTAMP},",
            "</if>",
            "<if test=\"updateDate != null\" >",
            "update_date = #{updateDate,jdbcType=TIMESTAMP},",
            "</if>",
            "</set>",
            "where id = #{id,jdbcType=INTEGER}",
            "</script>"
    })
    Integer updateByPrimaryKeySelective(CustomerReprieve record);

    @Select({
            "<script>",
            "select",
            " id, loss_id, measure, is_valid, create_date, update_date",
            "from t_customer_reprieve",
            "<where>",
            "is_valid = 1",
            "<if test=\"null != lossId\">",
            "and loss_id = #{lossId}",
            "</if>",
            "</where>",
            "</script>"
    })
    @ResultMap("BaseResultMap")
    List<CustomerReprieve> selectByParams(CustomerReprQuery customerReprQuery);
}
