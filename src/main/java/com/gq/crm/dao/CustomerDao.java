package com.gq.crm.dao;

import com.gq.base.BaseDao;
import com.gq.crm.entity.Customer;
import com.gq.crm.query.CustomerQuery;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @date 2023/6/19
 */

@Repository
public interface CustomerDao extends BaseDao<Customer,Integer> {


    String BASE_COLUMN_LIST = "id, khno, name, area, cus_manager, level, myd, xyd, address, post_code, phone, fax, web_site, yyzzzch, fr, zczj, nyye, khyh, khzh, dsdjh, gsdjh, state, is_valid, create_date, update_date";

    @Select("SELECT " + BASE_COLUMN_LIST + " FROM t_customer WHERE id = #{id}")
   @ResultMap("BaseResultMap")
    Customer selectByPrimaryKey(Integer id);

    @Delete("DELETE FROM t_customer WHERE id = #{id}")
    Integer deleteByPrimaryKey(Integer id);

    @Insert("INSERT INTO t_customer (" + BASE_COLUMN_LIST + ") VALUES (" +
            "#{id}, #{khno}, #{name}, #{area}, #{cusManager}, #{level}, #{myd}, #{xyd}, #{address}, #{postCode}, #{phone}, #{fax}, #{webSite}, #{yyzzzch}, #{fr}, #{zczj}, #{nyye}, #{khyh}, #{khzh}, #{dsdjh}, #{gsdjh}, #{state}, #{isValid}, #{createDate}, #{updateDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @ResultMap("BaseResultMap")
    Integer insertSelective(Customer customer);

    @Update({
            "<script>",
            "UPDATE t_customer ",
            "<set>",
            "<if test='khno != null'>khno = #{khno},</if>",
            "<if test='name != null'>name = #{name},</if>",
            "<if test='area != null'>area = #{area},</if>",
            "<if test='cusManager != null'>cus_manager = #{cusManager},</if>",
            "<if test='level != null'>level = #{level},</if>",
            "<if test='myd != null'>myd = #{myd},</if>",
            "<if test='xyd != null'>xyd = #{xyd},</if>",
            "<if test='address != null'>address = #{address},</if>",
            "<if test='postCode != null'>post_code = #{postCode},</if>",
            "<if test='phone != null'>phone = #{phone},</if>",
            "<if test='fax != null'>fax = #{fax},</if>",
            "<if test='webSite != null'>web_site = #{webSite},</if>",
            "<if test='yyzzzch != null'>yyzzzch = #{yyzzzch},</if>",
            "<if test='fr != null'>fr = #{fr},</if>",
            "<if test='zczj != null'>zczj = #{zczj},</if>",
            "<if test='nyye != null'>nyye = #{nyye},</if>",
            "<if test='khyh != null'>khyh = #{khyh},</if>",
            "<if test='khzh != null'>khzh = #{khzh},</if>",
            "<if test='dsdjh != null'>dsdjh = #{dsdjh},</if>",
            "<if test='gsdjh != null'>gsdjh = #{gsdjh},</if>",
            "<if test='state != null'>state = #{state},</if>",
            "<if test='isValid != null'>is_valid = #{isValid},</if>",
            "<if test='createDate != null'>create_date = #{createDate},</if>",
            "<if test='updateDate != null'>update_date = #{updateDate}</if>",
            "</set>",
            "WHERE id = #{id}",
            "</script>"
    })
    @ResultMap("BaseResultMap")
    Integer updateByPrimaryKeySelective(Customer customer);



    @Select({"<script>",
            "select ", BASE_COLUMN_LIST, " from t_customer",
            "<where>",
            "is_valid = 1 and state = 0",
            "<if test='customerName != null and customerName != &quot;&quot;'>",
            "and name like concat('%',#{customerName},'%')",
            "</if>",
            "<if test='customerNo != null and customerNo != &quot;&quot;'>",
            "and khno = #{customerNo}",
            "</if>",
            "<if test='level != null and level != &quot;&quot;'>",
            "and level = #{level}",
            "</if>",
            "</where>",
            "</script>"})
    @Results(id = "BaseResultMap", value = {
            @Result(column = "id", jdbcType = JdbcType.INTEGER, property = "id", id = true),
            @Result(column = "khno", jdbcType = JdbcType.VARCHAR, property = "khno"),
            @Result(column = "name", jdbcType = JdbcType.VARCHAR, property = "name"),
            @Result(column = "area", jdbcType = JdbcType.VARCHAR, property = "area"),
            @Result(column = "cus_manager", jdbcType = JdbcType.VARCHAR, property = "cusManager"),
            @Result(column = "level", jdbcType = JdbcType.VARCHAR, property = "level"),
            @Result(column = "myd", jdbcType = JdbcType.VARCHAR, property = "myd"),
            @Result(column = "xyd", jdbcType = JdbcType.VARCHAR, property = "xyd"),
            @Result(column = "address", jdbcType = JdbcType.VARCHAR, property = "address"),
            @Result(column = "post_code", jdbcType = JdbcType.VARCHAR, property = "postCode"),
            @Result(column = "phone", jdbcType = JdbcType.VARCHAR, property = "phone"),
            @Result(column = "fax", jdbcType = JdbcType.VARCHAR, property = "fax"),
            @Result(column = "web_site", jdbcType = JdbcType.VARCHAR, property = "webSite"),
            @Result(column = "yyzzzch", jdbcType = JdbcType.VARCHAR, property = "yyzzzch"),
            @Result(column = "fr", jdbcType = JdbcType.VARCHAR, property = "fr"),
            @Result(column = "zczj", jdbcType = JdbcType.VARCHAR, property = "zczj"),
            @Result(column = "nyye", jdbcType = JdbcType.VARCHAR, property = "nyye"),
            @Result(column = "khyh", jdbcType = JdbcType.VARCHAR, property = "khyh"),
            @Result(column = "khzh", jdbcType = JdbcType.VARCHAR, property = "khzh"),
            @Result(column = "dsdjh", jdbcType = JdbcType.VARCHAR, property = "dsdjh"),
            @Result(column = "gsdjh", jdbcType = JdbcType.VARCHAR, property = "gsdjh"),
            @Result(column = "state", jdbcType = JdbcType.INTEGER, property = "state"),
            @Result(column = "is_valid", jdbcType = JdbcType.INTEGER, property = "isValid"),
            @Result(column = "create_date", jdbcType = JdbcType.TIMESTAMP, property = "createDate"),
            @Result(column = "update_date", jdbcType = JdbcType.TIMESTAMP, property = "updateDate")
    })
    List<Customer> selectByParams(CustomerQuery customerQuery);

    @Select("SELECT " + BASE_COLUMN_LIST + " FROM t_customer WHERE is_valid = 1 and name = #{name}")
    @ResultMap("BaseResultMap")
    Customer queryCustomerByName(String name);


    @Select("SELECT * FROM t_customer c WHERE is_valid = 1 and state = 0 AND DATE_ADD(c.create_date,INTERVAL 6 MONTH) < NOW() AND c.id NOT IN (SELECT DISTINCT cus_id FROM t_customer_order o WHERE is_valid = 1 AND o.state = 1 AND DATE_ADD(o.order_date,INTERVAL 6 MONTH) > NOW())")
    @ResultMap("BaseResultMap")
    List<Customer> queryLossCustomers();

    @Update("<script>update t_customer set state = 1 where id in "
            + "<foreach collection='list' item='item' open='(' close=')' separator=','>"
            + "#{item}"
            + "</foreach></script>")
    Integer updateCustomerStateByIds(List<Integer> list);


 @Select("<script>SELECT c.`name`, sum(od.sum) total "
         + "FROM t_customer c "
         + "LEFT JOIN t_customer_order o ON c.id = o.cus_id "
         + "LEFT JOIN t_order_details od ON o.id = od.order_id "
         + "WHERE c.is_valid = 1 AND c.state = 0 AND o.is_valid = 1 AND o.state = 1 AND od.is_valid = 1 "
         + "<if test='null != customerName and customerName != &quot;&quot;'>"
         + "AND c.name like concat('%',#{customerName},'%') "
         + "</if>"
         + "<if test='null != time and time != &quot;&quot;'>"
         + "AND o.order_date &gt; #{time} "
         + "</if>"
         + "GROUP BY c.`name` "
         + "<if test='null != type'>"
         + "<choose>"
         + "<when test='type==1'> HAVING total &gt;= 0 and total &lt;= 1000 </when>"
         + "<when test='type==2'> HAVING total &gt; 1000 and total &lt;= 3000 </when>"
         + "<when test='type==3'> HAVING total &gt; 3000 and total &lt;= 5000 </when>"
         + "<when test='type==4'> HAVING total &gt; 5000 </when>"
         + "</choose>"
         + "</if>"
         + "ORDER BY total DESC </script>")
    List<Map<String, Object>> queryCustomerContributionByParams(CustomerQuery customerQuery);

    @Select("SELECT `level`,count(1) total FROM t_customer WHERE is_valid = 1 AND state = 0 GROUP BY `level`")
    List<Map<String, Object>> countCustomerMake();
}
