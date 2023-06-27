package com.gq.crm.dao;

import com.gq.base.BaseDao;
import com.gq.crm.entity.Permission;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @date 2023/6/18
 */

@Repository
public interface PermissionDao {

    @Results(id = "BaseResultMap", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER),
            @Result(column = "role_id", property = "roleId", jdbcType = JdbcType.INTEGER),
            @Result(column = "module_id", property = "moduleId", jdbcType = JdbcType.INTEGER),
            @Result(column = "acl_value", property = "aclValue", jdbcType = JdbcType.VARCHAR),
            @Result(column = "create_date", property = "createDate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_date", property = "updateDate", jdbcType = JdbcType.TIMESTAMP)
    })
    @Select({
            "select",
            "id, role_id, module_id, acl_value, create_date, update_date",
            "from t_permission",
            "where id = #{id,jdbcType=INTEGER}"
    })
    Permission selectByPrimaryKey(Integer id);

    @Delete({
            "delete from t_permission",
            "where id = #{id,jdbcType=INTEGER}"
    })
    Integer deleteByPrimaryKey(Integer id);

    @Insert({
            "insert into t_permission (id, role_id, module_id, acl_value, create_date, update_date)",
            "values (#{id,jdbcType=INTEGER}, #{roleId,jdbcType=INTEGER}, ",
            "#{moduleId,jdbcType=INTEGER}, #{aclValue,jdbcType=VARCHAR}, #{createDate,jdbcType=TIMESTAMP}, ",
            "#{updateDate,jdbcType=TIMESTAMP})"
    })
    Integer insert(Permission record);

    @InsertProvider(type = PermissionSqlProvider.class, method = "insertSelective")
    Integer insertSelective(Permission record);

    @UpdateProvider(type = PermissionSqlProvider.class, method = "updateByPrimaryKeySelective")
    Integer updateByPrimaryKeySelective(Permission record);

    @Update({
            "update t_permission",
            "set role_id = #{roleId,jdbcType=INTEGER},",
            "module_id = #{moduleId,jdbcType=INTEGER},",
            "acl_value = #{aclValue,jdbcType=VARCHAR},",
            "create_date = #{createDate,jdbcType=TIMESTAMP},",
            "update_date = #{updateDate,jdbcType=TIMESTAMP}",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Permission record);

    @Select({
            "select",
            "count(1)",
            "from t_permission",
            "where role_id = #{roleId}"
    })
    Integer countPermissionByRoleId(int roleId);

    @Delete({
            "delete from t_permission",
            "where role_id = #{roleId}"
    })
    Integer deletePermissionByRoleId(int roleId);

    @Insert({"<script>"+"insert  into t_permission (role_id,module_id,acl_value,create_date,update_date)",
            "values ",
            "<foreach collection=\"list\" item=\"item\" separator=\",\">",
            "(#{item.roleId},#{item.moduleId},#{item.aclValue},now(),now())",
            "</foreach>"+"</script>"
    })
    Integer insertBatch(List<Permission> permissions);

    @Select({
            "select",
            "module_id",
            "from t_permission",
            "where role_id = #{roleId}"
    })
    List<Integer> queryRoleHasModuleIdsByRoleId(int roleId);

    @Select({
            "SELECT DISTINCT acl_value",
            "FROM t_user_role ur",
            "LEFT JOIN t_permission p ON ur.role_id = p.role_id",
            "WHERE ur.user_id = #{userId}"
    })
    List<String> queryUserHasRoleHasPermissionByUserId(int userId);

    @Select({
            "select",
            "count(1)",
            "from t_permission",
            "where module_id=#{id}"
    })
    Integer countPermissionByModuleId(int id);

    @Delete({
            "delete from t_permission",
            "where module_id=#{id}"
    })
    void deletePermissionByModuleId(int id);



     class PermissionSqlProvider {

        public String insertSelective(Permission record) {
            SQL sql = new SQL();
            sql.INSERT_INTO("t_permission");

            if (record.getId() != null) {
                sql.VALUES("id", "#{id,jdbcType=INTEGER}");
            }
            if (record.getRoleId() != null) {
                sql.VALUES("role_id", "#{roleId,jdbcType=INTEGER}");
            }
            if (record.getModuleId() != null) {
                sql.VALUES("module_id", "#{moduleId,jdbcType=INTEGER}");
            }
            if (record.getAclValue() != null) {
                sql.VALUES("acl_value", "#{aclValue,jdbcType=VARCHAR}");
            }
            if (record.getCreateDate() != null) {
                sql.VALUES("create_date", "#{createDate,jdbcType=TIMESTAMP}");
            }
            if (record.getUpdateDate() != null) {
                sql.VALUES("update_date", "#{updateDate,jdbcType=TIMESTAMP}");
            }

            return sql.toString();
        }

        public String updateByPrimaryKeySelective(Permission record) {
            SQL sql = new SQL();
            sql.UPDATE("t_permission");

            if (record.getRoleId() != null) {
                sql.SET("role_id = #{roleId,jdbcType=INTEGER}");
            }
            if (record.getModuleId() != null) {
                sql.SET("module_id = #{moduleId,jdbcType=INTEGER}");
            }
            if (record.getAclValue() != null) {
                sql.SET("acl_value = #{aclValue,jdbcType=VARCHAR}");
            }
            if (record.getCreateDate() != null) {
                sql.SET("create_date = #{createDate,jdbcType=TIMESTAMP}");
            }
            if (record.getUpdateDate() != null) {
                sql.SET("update_date = #{updateDate,jdbcType=TIMESTAMP}");
            }

            sql.WHERE("id = #{id,jdbcType=INTEGER}");

            return sql.toString();
        }
    }
}
