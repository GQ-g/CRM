package com.gq.crm.dao;

import com.gq.base.BaseDao;
import com.gq.base.BaseQuery;
import com.gq.crm.entity.CusDevPlan;
import org.apache.ibatis.annotations.*;
import org.springframework.dao.DataAccessException;

import javax.annotation.Resource;
import java.util.List;

/**
 * @date 2023/6/14
 */

@Resource
public interface CusDevPlanDao extends BaseDao<CusDevPlan,Integer> {

    @Select("select\n" +
            "id, sale_chance_id, plan_item, plan_date, exe_affect, create_date, update_date, is_valid \n" +
            "    from\n" +
            "        t_cus_dev_plan\n" +
            "    where\n" +
            "        is_valid = 1\n" +
            "    and\n" +
            "        sale_chance_id = #{sId}")
    @Results(id = "BaseResultMap", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "sale_chance_id", property = "saleChanceId"),
            @Result(column = "plan_item", property = "planItem"),
            @Result(column = "plan_date", property = "planDate"),
            @Result(column = "exe_affect", property = "exeAffect"),
            @Result(column = "create_date", property = "createDate"),
            @Result(column = "update_date", property = "updateDate"),
            @Result(column = "is_valid", property = "isValid")
    })
    @Override
    List<CusDevPlan> selectByParams(BaseQuery baseQuery) throws DataAccessException;

    @Insert("insert into t_cus_dev_plan (id, sale_chance_id, plan_item, plan_date, exe_affect, create_date, update_date, is_valid)"
            + " values (#{id,jdbcType=INTEGER}, #{saleChanceId,jdbcType=INTEGER}, #{planItem,jdbcType=VARCHAR},"
            + " #{planDate,jdbcType=TIMESTAMP}, #{exeAffect,jdbcType=VARCHAR}, #{createDate,jdbcType=TIMESTAMP},"
            + " #{updateDate,jdbcType=TIMESTAMP}, #{isValid,jdbcType=INTEGER})")
    @Override
    Integer insertSelective(CusDevPlan entity) throws DataAccessException;

    @Select(" select \n" +
            "      id, sale_chance_id, plan_item, plan_date, exe_affect, create_date, update_date, is_valid \n" +
            "    from t_cus_dev_plan\n" +
            "    where id = #{id,jdbcType=INTEGER}")
    @Override
    @ResultMap("BaseResultMap")
    CusDevPlan selectByPrimaryKey(Integer id) throws DataAccessException;


    @Delete("delete from t_cus_dev_plan where id = #{id}")
    Integer deleteByPrimaryKey(Integer id);

    @Insert("insert into t_cus_dev_plan (id, sale_chance_id, plan_item, plan_date, exe_affect, create_date, update_date, is_valid) values (#{id}, #{saleChanceId}, #{planItem}, #{planDate}, #{exeAffect}, #{createDate}, #{updateDate}, #{isValid})")
    int insert(CusDevPlan record);


    @Update("update t_cus_dev_plan set sale_chance_id = #{saleChanceId}, plan_item = #{planItem}, plan_date = #{planDate},"
            + " exe_affect = #{exeAffect}, create_date = #{createDate}, update_date = #{updateDate}, is_valid = #{isValid} where id = #{id}")
    int updateByPrimaryKey(CusDevPlan record);

    @Update("<script>update t_cus_dev_plan "
            + "<set>"
            + "<if test='saleChanceId != null'>sale_chance_id = #{saleChanceId},</if>"
            + "<if test='planItem != null'>plan_item = #{planItem},</if>"
            + "<if test='planDate != null'>plan_date = #{planDate},</if>"
            + "<if test='exeAffect != null'>exe_affect = #{exeAffect},</if>"
            + "<if test='createDate != null'>create_date = #{createDate},</if>"
            + "<if test='updateDate != null'>update_date = #{updateDate},</if>"
            + "<if test='isValid != null'>is_valid = #{isValid},</if>"
            + "</set>"
            + " where id = #{id}</script>")
    Integer updateByPrimaryKeySelective(CusDevPlan record);

  /*  @Select("select id, sale_chance_id, plan_item, plan_date, exe_affect, create_date, update_date, is_valid from t_cus_dev_plan where is_valid = 1 and sale_chance_id = #{saleChanceId}")
    List<CusDevPlan> selectByParams(CusDevPlanQuery query);*/

}
