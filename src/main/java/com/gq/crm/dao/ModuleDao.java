package com.gq.crm.dao;

import com.gq.base.BaseDao;
import com.gq.crm.Model.TreeModel;
import com.gq.crm.entity.Module;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @date 2023/6/18
 */

@Repository
public interface ModuleDao extends BaseDao<Module,Integer> {
    String BASE_COLUMN_LIST = "id, module_name, module_style, url, parent_id, parent_opt_value, " +
            "grade, opt_value, orders, is_valid, create_date, update_date";

    @Select("select " + BASE_COLUMN_LIST +
            " from t_module" +
            " where id = #{id,jdbcType=INTEGER}")
    @Results(id = "BaseResultMap", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER),
            @Result(column = "module_name", property = "moduleName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "module_style", property = "moduleStyle", jdbcType = JdbcType.VARCHAR),
            @Result(column = "url", property = "url", jdbcType = JdbcType.VARCHAR),
            @Result(column = "parent_id", property = "parentId", jdbcType = JdbcType.INTEGER),
            @Result(column = "parent_opt_value", property = "parentOptValue", jdbcType = JdbcType.VARCHAR),
            @Result(column = "grade", property = "grade", jdbcType = JdbcType.INTEGER),
            @Result(column = "opt_value", property = "optValue", jdbcType = JdbcType.VARCHAR),
            @Result(column = "orders", property = "orders", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_valid", property = "isValid", jdbcType = JdbcType.TINYINT),
            @Result(column = "create_date", property = "createDate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_date", property = "updateDate", jdbcType = JdbcType.TIMESTAMP)
    })
    Module selectByPrimaryKey(Integer id);

    @Delete("delete from t_module where id = #{id,jdbcType=INTEGER}")
    Integer deleteByPrimaryKey(Integer id);

    @Insert("<script>" +
            "insert into t_module" +
            "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >" +
            "<if test=\"id != null\">id,</if>" +
            "<if test=\"moduleName != null\">module_name,</if>" +
            "<if test=\"moduleStyle != null\">module_style,</if>" +
            "<if test=\"url != null\">url,</if>" +
            "<if test=\"parentId != null\">parent_id,</if>" +
            "<if test=\"parentOptValue != null\">parent_opt_value,</if>" +
            "<if test=\"grade != null\">grade,</if>" +
            "<if test=\"optValue != null\">opt_value,</if>" +
            "<if test=\"orders != null\">orders,</if>" +
            "<if test=\"isValid != null\">is_valid,</if>" +
            "<if test=\"createDate != null\">create_date,</if>" +
            "<if test=\"updateDate != null\">update_date,</if>" +
            "</trim>" +
            "<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >" +
            "<if test=\"id != null\">#{id,jdbcType=INTEGER},</if>" +
            "<if test=\"moduleName != null\">#{moduleName,jdbcType=VARCHAR},</if>" +
            "<if test=\"moduleStyle != null\">#{moduleStyle,jdbcType=VARCHAR},</if>" +
            "<if test=\"url != null\">#{url,jdbcType=VARCHAR},</if>" +
            "<if test=\"parentId != null\">#{parentId,jdbcType=INTEGER},</if>" +
            "<if test=\"parentOptValue != null\">#{parentOptValue,jdbcType=VARCHAR},</if>" +
            "<if test=\"grade != null\">#{grade,jdbcType=INTEGER},</if>" +
            "<if test=\"optValue != null\">#{optValue,jdbcType=VARCHAR},</if>" +
            "<if test=\"orders != null\">#{orders,jdbcType=INTEGER},</if>" +
            "<if test=\"isValid != null\">#{isValid,jdbcType=TINYINT},</if>" +
            "<if test=\"createDate != null\">#{createDate,jdbcType=TIMESTAMP},</if>" +
            "<if test=\"updateDate != null\">#{updateDate,jdbcType=TIMESTAMP},</if>" +
            "</trim>" +
            "</script>")
    Integer insertSelective(Module module);

    @Update("<script>" +
            "update t_module" +
            "<set>" +
            "<if test=\"moduleName != null\">module_name = #{moduleName,jdbcType=VARCHAR},</if>" +
            "<if test=\"moduleStyle != null\">module_style = #{moduleStyle,jdbcType=VARCHAR},</if>" +
            "<if test=\"url != null\">url = #{url,jdbcType=VARCHAR},</if>" +
            "<if test=\"parentId != null\">parent_id = #{parentId,jdbcType=INTEGER},</if>" +
            "<if test=\"parentOptValue != null\">parent_opt_value = #{parentOptValue,jdbcType=VARCHAR},</if>" +
            "<if test=\"grade != null\">grade = #{grade,jdbcType=INTEGER},</if>" +
            "<if test=\"optValue != null\">opt_value = #{optValue,jdbcType=VARCHAR},</if>" +
            "<if test=\"orders != null\">orders = #{orders,jdbcType=INTEGER},</if>" +
            "<if test=\"isValid != null\">is_valid = #{isValid,jdbcType=TINYINT},</if>" +
            "<if test=\"createDate != null\">create_date = #{createDate,jdbcType=TIMESTAMP},</if>" +
            "<if test=\"updateDate != null\">update_date = #{updateDate,jdbcType=TIMESTAMP},</if>" +
            "</set>" +
            "where id = #{id,jdbcType=INTEGER}" +
            "</script>")
    Integer updateByPrimaryKeySelective(Module module);

    /**
     *查询所有有效菜单
     * 包括子类和父类菜单
     * @return
     */
    @Select("select id, parent_id as pId, module_name as name from t_module where is_valid = 1")
    List<TreeModel> queryAllModules();

    /**
     *
     * @return
     */
    @Select("select " + BASE_COLUMN_LIST + " from t_module where is_valid = 1")
    @ResultMap("BaseResultMap")
    List<Module> queryModuleList();

    @Select("select " + BASE_COLUMN_LIST + " from t_module where is_valid = 1 and grade = #{grade} and module_name = #{moduleName}")
    @ResultMap("BaseResultMap")
    Module queryModuleByGradeAndModuleName(@Param("grade") Integer grade, @Param("moduleName") String moduleName);

    @Select("select " + BASE_COLUMN_LIST + " from t_module where is_valid = 1 and grade = #{grade} and url = #{url}")
    @ResultMap("BaseResultMap")
    Module queryModuleByGradeAndUrl(@Param("grade") Integer grade, @Param("url") String url);

    @Select("select " + BASE_COLUMN_LIST + " from t_module where is_valid = 1 and opt_value = #{optValue}")
    @ResultMap("BaseResultMap")
    Module queryModuleByOptValue(String optValue);

    @Select("select count(1) from t_module where is_valid = 1 and parent_id = #{id}")
    @ResultMap("BaseResultMap")
    Integer queryModuleByParentId(Integer id);

}
