package com.gq.crm.dao;

import com.gq.crm.entity.User;
import com.gq.crm.query.UserQuery;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserDao  {

    /*用户登录方法*/
    @Select("select * from t_user where user_name = #{userName}")
    @Results(id = ("t_user"), value = {
            @Result(id=true,property = "id",column = "id"),
            @Result(property = "userName",column = "user_name"),
            @Result(property = "userPwd",column = "user_pwd"),
            @Result(property = "trueName",column = "true_name"),
            @Result(property = "email",column = "email"),
            @Result(property = "phone",column = "phone"),
            @Result(property = "isValid",column = "is_valid"),
            @Result(property = "createDate",column = "create_date"),
            @Result(property = "updateDate",column = "update_date")
    })
    User queryUserByUserName(String userName);


    @Select("select  id, user_name, user_pwd, true_name, email, phone, is_valid, create_date, update_date from t_user where id=#{id}")
    @ResultMap("t_user")
    User selectByPrimaryKey(Integer id);


    @Update("update t_user set user_pwd = #{userPwd} where id = #{id}")
    Integer updatePwd(User user);

    // 查询所有的销售人员
    @Select("SELECT " +
            "u.id, u.user_name AS uname " +
            "FROM " +
            "t_user u " +
            "LEFT JOIN " +
            "t_user_role ur " +
            "ON " +
            "u.id = ur.user_id " +
            "LEFT JOIN " +
            "t_role r " +
            "ON " +
            "r.id = ur.role_id " +
            "WHERE " +
            "u.is_valid = 1 AND r.is_valid = 1 AND r.role_name = '销售'")
    List<Map<String,Object>>queryAllSales();

    /**
     * 根据用户查询用户
     * @param userQuery
     * @return
     */
    @SelectProvider(type = UserSqlProvider.class,method = "selectByParams")
    @ResultMap("t_user")
    List<User> selectByParams(UserQuery userQuery);


    @InsertProvider(type = UserSqlProvider.class,method = "insertUser")
   /* @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")*/
    Integer insertSelective(User user);


    /**
     * 系统设置  用户管理
     * 删除用户
     * @param ids
     * @return
     */
    @UpdateProvider(type = UserSqlProvider.class,method = "deleteBatch")
    Integer deleteBatch(@Param("ids") Integer[] ids);

    /**
     * 更改用户信息
     *
     */
    @UpdateProvider(type = UserSqlProvider.class,method = "updateByPrimaryKeySelective")
    Integer updateByPrimaryKeySelective(User user);

    @Select("SELECT id, user_name, user_pwd, true_name, email, phone, is_valid, create_date, update_date " +
            "FROM t_user " +
            "WHERE user_name = #{userName}")
    User queryUserByName(String userName);

    @Select(" SELECT\n" +
            "\t    u.id,u.user_name uname\n" +
            "      from\n" +
            "          t_user u\n" +
            "      left join\n" +
            "          t_user_role ur\n" +
            "      on\n" +
            "        u.id = ur.user_id\n" +
            "      left join\n" +
            "          t_role r\n" +
            "      on\n" +
            "          r.id = ur.role_id\n" +
            "      where\n" +
            "       u.is_valid = 1\n" +
            "      and\n" +
            "       r.is_valid = 1\n" +
            "      and\n" +
            "       r.role_name = '客户经理'")
    List<Map<String, Object>> queryAllCustomerManagers();



    /**
     * 内部类 通过在内部类里面编写SQL 运行
     */
    class UserSqlProvider{
        /*通过调用方法的形式来实现 Sql注解的美化*/
        String sql = "id, user_name, user_pwd, true_name, email, phone, is_valid, create_date, update_date";
        public String selectByParams(UserQuery userQuery) {
            return new SQL() {{
                SELECT(sql);
                FROM("t_user");
                WHERE("is_valid = 1");
                if (userQuery.getUserName() != null && !"".equals(userQuery.getUserName())) {
                    AND().WHERE("user_name like concat('%', #{userName}, '%')");
                }
                if (userQuery.getEmail() != null && !"".equals(userQuery.getEmail())) {
                    AND().WHERE("email like concat('%', #{email}, '%')");
                }
                if (userQuery.getPhone() != null && !"".equals(userQuery.getPhone())) {
                    AND().WHERE("phone like concat('%', #{phone}, '%')");
                }
            }}.toString();
        }

        /*插入用户管理数据*/
        public String insertUser(User user) {
            return new SQL(){{
                INSERT_INTO("t_user");
                /*if (user.getId() != null) VALUES("id", "#{id}");*/
                if (user.getUserName() != null) VALUES("user_name", "#{userName}");
                if (user.getUserPwd() != null) VALUES("user_pwd", "#{userPwd}");
                if (user.getTrueName() != null) VALUES("true_name", "#{trueName}");
                if (user.getEmail() != null) VALUES("email", "#{email}");
                if (user.getPhone() != null) VALUES("phone", "#{phone}");
                if (user.getIsValid() != null) VALUES("is_valid", "#{isValid}");
                if (user.getCreateDate() != null) VALUES("create_date", "#{createDate}");
                if (user.getUpdateDate() != null) VALUES("update_date", "#{updateDate}");
            }}.toString();
        }

        public String updateByPrimaryKeySelective(User user) {
            return new SQL() {{
                UPDATE("t_user");
                if (user.getUserName() != null) {
                    SET("user_name = #{userName,jdbcType=VARCHAR}");
                }
                if (user.getUserPwd() != null) {
                    SET("user_pwd = #{userPwd,jdbcType=VARCHAR}");
                }
                if (user.getTrueName() != null) {
                    SET("true_name = #{trueName,jdbcType=VARCHAR}");
                }
                if (user.getEmail() != null) {
                    SET("email = #{email,jdbcType=VARCHAR}");
                }
                if (user.getPhone() != null) {
                    SET("phone = #{phone,jdbcType=VARCHAR}");
                }
                if (user.getIsValid() != null) {
                    SET("is_valid = #{isValid,jdbcType=INTEGER}");
                }
                if (user.getCreateDate() != null) {
                    SET("create_date = #{createDate,jdbcType=TIMESTAMP}");
                }
                if (user.getUpdateDate() != null) {
                    SET("update_date = #{updateDate,jdbcType=TIMESTAMP}");
                }
                WHERE("id = #{id,jdbcType=INTEGER}");
            }}.toString();
        }


        public static String deleteBatch(Map<String, Object> map) {
            Integer[] ids = (Integer[]) map.get("ids");
            StringBuilder sb = new StringBuilder();
            sb.append("update t_user set is_valid = 0 where id in (");
            for (int i = 0; i < ids.length; i++) {
                sb.append("#{ids[").append(i).append("]},");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(")");
            return sb.toString();
        }
    }

}
