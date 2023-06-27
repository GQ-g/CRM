package com.gq.crm.dao;

import com.gq.base.BaseDao;
import com.gq.base.BaseQuery;
import com.gq.crm.entity.SaleChance;
import org.apache.ibatis.annotations.*;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @date 2023/6/13
 */

@Repository
public interface SaleChanceDao extends BaseDao<SaleChance,Integer> {

    /*统一的方法在父接口中统一实现 */

    @Results(id = "BaseResultMap", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "chance_source", property = "chanceSource"),
            @Result(column = "customer_name", property = "customerName"),
            @Result(column = "cgjl", property = "cgjl"),
            @Result(column = "overview", property = "overview"),
            @Result(column = "link_man", property = "linkMan"),
            @Result(column = "link_phone", property = "linkPhone"),
            @Result(column = "description", property = "description"),
            @Result(column = "create_man", property = "createMan"),
            @Result(column = "assign_man", property = "assignMan"),
            @Result(column = "assign_time", property = "assignTime"),
            @Result(column = "state", property = "state"),
            @Result(column = "dev_result", property = "devResult"),
            @Result(column = "is_valid", property = "isValid"),
            @Result(column = "create_date", property = "createDate"),
            @Result(column = "update_date", property = "updateDate")
    })
    @Select("<script>"
            + "select s.id, chance_source, customer_name, cgjl, overview, link_man, link_phone, description,"
            + "create_man, assign_man, assign_time, state, dev_result, s.is_valid, s.create_date, s.update_date, "
            + "u.user_name as uname "
            + "from t_sale_chance s "
            + "left join t_user u on s.assign_man = u.id "
            + "<where> s.is_valid = 1 "
            + "<if test='customerName != null and customerName != \"\"'> and s.customer_name like concat('%', #{customerName}, '%') </if>"
            + "<if test='createMan != null and createMan != \"\"'> and s.create_man = #{createMan} </if>"
            + "<if test='state != null'> and s.state = #{state} </if>"
            + "<if test='devResult != null and devResult != \"\"'> and s.dev_result = #{devResult} </if>"
            + "<if test='assignMan != null'> and s.assign_man = #{assignMan} </if>"
            + "</where>"
            + "</script>")
    @Override
    List<SaleChance> selectByParams(BaseQuery baseQuery) throws DataAccessException;


    /**
     * 添加营销机会
     * @param saleChance
     * @return
     * @throws DataAccessException
     */
    @Insert("insert into t_sale_chance (id, chance_source, customer_name,cgjl, overview, link_man,link_phone, description, create_man, assign_man, assign_time, state,dev_result, is_valid, create_date,update_date)"+
            "values (#{id,jdbcType=INTEGER}, #{chanceSource,jdbcType=VARCHAR}, #{customerName,jdbcType=VARCHAR},"+
            " #{cgjl,jdbcType=INTEGER}, #{overview,jdbcType=VARCHAR}, #{linkMan,jdbcType=VARCHAR},"+
            " #{linkPhone,jdbcType=VARCHAR}, #{description,jdbcType=VARCHAR}, #{createMan,jdbcType=VARCHAR},"+
            " #{assignMan,jdbcType=VARCHAR}, #{assignTime,jdbcType=TIMESTAMP}, #{state,jdbcType=INTEGER},"+
            "#{devResult,jdbcType=INTEGER}, #{isValid,jdbcType=INTEGER}, #{createDate,jdbcType=TIMESTAMP},"+
            " #{updateDate,jdbcType=TIMESTAMP})")
    @Override
    Integer insertSelective(SaleChance saleChance) throws DataAccessException;


    @Select("select \n" +
            " id, chance_source, customer_name, cgjl, overview, link_man, link_phone, description,"+
            "create_man, assign_man, assign_time, state, dev_result, is_valid, create_date, update_date"+
            " from t_sale_chance\n" +
            "where id = #{id,jdbcType=INTEGER}")
    @Override
    @ResultMap("BaseResultMap")
    SaleChance selectByPrimaryKey(Integer integer) throws DataAccessException;

    /**
     * 更新营销机会
     */
    int updateByPrimaryKey(SaleChance record);


    @Update( "<script>" +"update t_sale_chance set " +
            "<if test='chanceSource != null'>chance_source = #{chanceSource},</if>" +
            "<if test='customerName != null'>customer_name = #{customerName},</if>" +
            "<if test='cgjl != null'>cgjl = #{cgjl},</if>" +
            "<if test='overview != null'>overview = #{overview},</if>" +
            "<if test='linkMan != null'>link_man = #{linkMan},</if>" +
            "<if test='linkPhone != null'>link_phone = #{linkPhone},</if>" +
            "<if test='description != null'>description = #{description},</if>" +
            "<if test='createMan != null'>create_man = #{createMan},</if>" +
            "<if test='state != null'>state = #{state},</if>" +
            "<if test='devResult != null'>dev_result = #{devResult},</if>" +
            "<if test='isValid != null'>is_valid = #{isValid},</if>" +
            "<if test='createDate != null'>create_date = #{createDate},</if>" +
            "<if test='updateDate != null'>update_date = #{updateDate},</if>" +
            "assign_man = #{assignMan}, " + // 加上空格
            "assign_time = #{assignTime} " + // 加上空格
            "where id = #{id}" +"</script>")   //添加<script></script>
    @Override
    Integer updateByPrimaryKeySelective(SaleChance entity) throws DataAccessException;


    /**
     * 删除数据  更新数据的状态
     */
    @Update( "<script>" +"update t_sale_chance set is_valid = false where id in "
            + "<foreach collection='array' item='id' open='(' separator=',' close=')'>"
            + "#{id}"
            + "</foreach>"+"</script>")
    @Override
    Integer deleteBatch(@Param("array") Integer[] ids) throws DataAccessException;
}
