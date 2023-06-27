package com.gq.crm.dao;

import com.gq.base.BaseDao;
import com.gq.base.BaseQuery;
import com.gq.crm.entity.Role;
import com.gq.crm.entity.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.type.JdbcType;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @date 2023/6/16
 */

@Repository
public interface RoleDao  extends BaseDao<Role,Integer> {

    /**
     * 关系字段的映射
     * @param userId
     * @return
     */
    @Results(id = "BaseResultMap", value = {
            @Result(property = "id", column = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(property = "roleName", column = "role_name", jdbcType = JdbcType.VARCHAR),
            @Result(property = "roleRemark", column = "role_remark", jdbcType = JdbcType.VARCHAR),
            @Result(property = "createDate", column = "create_date"),
            @Result(property = "updateDate", column = "update_date"),
            @Result(property = "isValid", column = "is_valid", jdbcType = JdbcType.INTEGER)
    })

    /**
     * 查询所有角色列表 （只需要id 和 roleName）
     * @param userId
     * @return
     */
    @Select("SELECT r.id, r.role_name AS roleName, CASE WHEN IFNULL(temp.id,0) = 0 THEN '' ELSE 'selected' END AS 'selected' " +
            "FROM t_role r " +
            "LEFT JOIN (SELECT r.id FROM t_role r LEFT JOIN t_user_role ur ON r.id = ur.role_id WHERE user_id = #{userId}) temp ON r.id = temp.id " +
            "WHERE r.is_valid = 1")
    List<Map<String,Object>> queryAllRoles(Integer userId);

    /**
     * 通过角色名查询角色记录
     * @param roleName
     * @return
     */
    @Select({"<script>",
            "select",
            "id, role_name, role_remark, create_date, update_date, is_valid",
            "from t_role",
            "where is_valid = 1 and role_name = #{roleName}",
            "</script>"})
    Role queryRoleByRoleName(String roleName);

    @Select("select id, role_name, role_remark, create_date, update_date, is_valid from t_role where id = #{id,jdbcType=INTEGER}")
    @Results(id = "t_role", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "role_name", property = "roleName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "role_remark", property = "roleRemark", jdbcType = JdbcType.VARCHAR),
            @Result(column = "create_date", property = "createDate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_date", property = "updateDate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "is_valid", property = "isValid", jdbcType = JdbcType.INTEGER)
    })
    Role selectByPrimaryKey(Integer id);

 /*   *//*根据id查询用户*//*
    @Select("select id, role_name, role_remark, create_date, update_date, is_valid from  t_role where id=#{id}")
    @ResultMap("BaseResultMap")
    @Override
    Role selectByPrimaryKey(Integer id) throws DataAccessException;*/

    @Select({"<script>",
            "select",
            "id, role_name, role_remark, create_date, update_date, is_valid",
            "from t_role",
            "<where>is_valid = 1",
            "<if test='null != roleName and \"\" != roleName'>",
            "and role_name like concat('%', #{roleName}, '%')",
            "</if>",
            "</where>",
            "</script>"})
    @ResultMap("t_role")
    @Override
    List<Role> selectByParams(BaseQuery baseQuery) throws DataAccessException;




    @InsertProvider(value = RoleSqlProvider.class,method = "insertSelective")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Override
    Integer insertSelective(Role entity) throws DataAccessException;



    @UpdateProvider(type = RoleSqlProvider.class,method = "updateByPrimaryKeySelective")
    @ResultMap("BaseResultMap")
    @Override
    Integer updateByPrimaryKeySelective(Role entity) throws DataAccessException;

    @Update("update t_role set is_valid=0 where id=#{id}")
    @Override
    Integer deleteByPrimaryKey(Integer id) throws DataAccessException;


    class RoleSqlProvider {

        public String insertSelective(Role role) {
            return new SQL() {{
                INSERT_INTO("t_role");
                if (role.getId() != null) {
                    VALUES("id", "#{id,jdbcType=INTEGER}");
                }
                if (role.getRoleName() != null) {
                    VALUES("role_name", "#{roleName,jdbcType=VARCHAR}");
                }
                if (role.getRoleRemark() != null) {
                    VALUES("role_remark", "#{roleRemark,jdbcType=VARCHAR}");
                }
                if (role.getCreateDate() != null) {
                    VALUES("create_date", "#{createDate,jdbcType=TIMESTAMP}");
                }
                if (role.getUpdateDate() != null) {
                    VALUES("update_date", "#{updateDate,jdbcType=TIMESTAMP}");
                }
                if (role.getIsValid() != null) {
                    VALUES("is_valid", "#{isValid,jdbcType=INTEGER}");
                }
            }}.toString();
        }



        public String updateByPrimaryKeySelective(Role entity) {
            return new SQL() {{
                UPDATE("t_role");
                if (entity.getRoleName() != null) {
                    SET("role_name = #{roleName,jdbcType=VARCHAR}");
                }
                if (entity.getRoleRemark() != null) {
                    SET("role_remark = #{roleRemark,jdbcType=VARCHAR}");
                }
                if (entity.getCreateDate() != null) {
                    SET("create_date = #{createDate,jdbcType=TIMESTAMP}");
                }
                if (entity.getUpdateDate() != null) {
                    SET("update_date = #{updateDate,jdbcType=TIMESTAMP}");
                }
                if (entity.getIsValid() != null) {
                    SET("is_valid = #{isValid,jdbcType=INTEGER}");
                }
                WHERE("id = #{id,jdbcType=INTEGER}");
            }}.toString();
        }
    }
}
