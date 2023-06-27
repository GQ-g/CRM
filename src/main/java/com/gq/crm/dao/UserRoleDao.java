package com.gq.crm.dao;

import com.gq.base.BaseDao;
import com.gq.crm.entity.UserRole;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @date 2023/6/16
 */

@Repository
public interface UserRoleDao extends BaseDao<UserRole,Integer> {

    @Select("select id, user_id, role_id, create_date, update_date from t_user_role where id = #{id}")
    @Results(id = "baseResultMap", value = {
            @Result(property = "id", column = "id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "userId", column = "user_id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "roleId", column = "role_id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "createDate", column = "create_date", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "updateDate", column = "update_date", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP)
    })
    UserRole selectByPrimaryKey(Integer id);

    @Delete("delete from t_user_role where id = #{id}")
    Integer deleteByPrimaryKey(Integer id);

    @Insert("insert into t_user_role (id, user_id, role_id, create_date, update_date) values (#{id}, #{userId}, #{roleId}, #{createDate}, #{updateDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(UserRole record);

    @Insert({
            "<script>",
            "insert into t_user_role (user_id, role_id, create_date, update_date) values ",
            "<foreach collection='list' item='item' index='index' separator=','>",
            "(#{item.userId}, #{item.roleId}, #{item.createDate}, #{item.updateDate})",
            "</foreach>",
            "</script>"
    })
    Integer insertBatch(List<UserRole> userRoles);

    @Update("update t_user_role set user_id = #{userId}, role_id = #{roleId}, create_date = #{createDate}, update_date = #{updateDate} where id = #{id}")
    Integer updateByPrimaryKey(UserRole record);

    @Update({
            "<script>",
            "update t_user_role",
            "<set>",
            "<if test='userId != null'>user_id = #{userId}, </if>",
            "<if test='roleId != null'>role_id = #{roleId}, </if>",
            "<if test='createDate != null'>create_date = #{createDate}, </if>",
            "<if test='updateDate != null'>update_date = #{updateDate}, </if>",
            "</set>",
            "where id = #{id}",
            "</script>"
    })
    Integer updateByPrimaryKeySelective(UserRole record);

    @Select("select count(1) from t_user_role where user_id = #{userId}")
    int countUserRoleByUserId(Integer userId);

    @Delete("delete from t_user_role where user_id = #{userId}")
    int deleteUserRoleByUserId(Integer userId);


    @Select("select count(1) from t_user_role where role_id = #{roleId}")
    int countUserRoleByRoleId(Integer roleId);

    @Delete("delete from t_user_role where role_id = #{roleId}")
    int deleteUserRoleByRoleId(Integer roleId);

}
