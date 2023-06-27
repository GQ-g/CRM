package com.gq.crm.controller;

import com.gq.base.ResultVo;
import com.gq.base.StatusCode;
import com.gq.crm.Model.UserModel;
import com.gq.crm.entity.User;
import com.gq.crm.query.UserQuery;
import com.gq.crm.service.UserService;
import com.gq.crm.utils.LoginUserUtil;
import com.gq.crm.utils.ParamsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @date 2023/6/1
 */
@RequestMapping("/user")
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ResponseBody
    public ResultVo login(String userName,String userPwd){
        /*统一结果集*/
        ResultVo resultVo = new ResultVo();
        try {
            UserModel userModel = userService.login(userName,userPwd);
            System.out.println(userModel+"userModel");
            if (userModel != null){
                Integer code = userModel != null ? StatusCode.SUCCESS_CODE : StatusCode.ERROR_CODE;
                if (code == StatusCode.SUCCESS_CODE){
                    String msg = "success";
                    resultVo.setCode(code);
                    resultVo.setMsg(msg);
                    resultVo.setData(userModel);
//                    return new ResultVo(code, msg, resultVo.getData());
                    System.out.println("resultVo"+resultVo);
                    return resultVo;
                }
            }
        }catch (ParamsException e){
            /*捕捉参数异常   ParamsException*/
            resultVo.setCode(e.getCode());
            resultVo.setMsg(e.getMsg());
            e.printStackTrace();
        }catch (Exception e){
            resultVo.setCode(500);
            resultVo.setMsg("操作失败");
            e.printStackTrace();
        }
        return resultVo;
    }


    @PostMapping("/login02")
    @ResponseBody
    public ResultVo login02(String userName,String userPwd){
        UserModel userModel = userService.login(userName, userPwd);
        return ResultVo.success("用户登录成功",userModel);
    }

    @PostMapping("updatePassword")
    @ResponseBody
    public ResultVo updatePassword(HttpServletRequest request,String oldPassword,String newPassword,String confirmPassword){

      /*   ResultVo resultVo = new ResultVo();
       try {
            Integer res = userService.updateUserPassword(LoginUserUtil.releaseUserIdFromCookie(request), oldPassword, newPassword, confirmPassword);
            if (res > 0){
                resultVo.setCode(200);
                resultVo.setMsg("修改成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultVo.setMsg("failed");
            resultVo.setCode(500);
        }
        return resultVo;*/

        /*重新改写方法，提高执行效率，简化代码*/
        Integer res = userService.updateUserPassword(LoginUserUtil.releaseUserIdFromCookie(request), oldPassword, newPassword, confirmPassword);
        if (res < 0){
            return ResultVo.error(500,"failed");
        }
        return  ResultVo.success();
    }

    @RequestMapping("/toPasswordPage")
    public String toPasswordPage(){
        return "user/password";
    }


    /**
     * 查询所有销售的数据
     * @return
     */
    @RequestMapping("/queryAllSales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSales(){
        return userService.queryAllSales();
    }



    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryUsersByParams(UserQuery userQuery){
        return userService.queryUserByParams(userQuery);
    }

    /**
     *用户管理界面
     * @return
     */
    @RequestMapping("/index")
    public String index(){
        return "user/user";
    }

    /**
     * 系统设置  用户管理
     * 用户管理界面 添加
     */
    @RequestMapping("/addOrUpdateUserPage")
    public String  addOrUpdateUserPage(Integer id, Model model){
        model.addAttribute("user",userService.selectByPrimaryKey(id));
        return "user/add_update";
    }


    @RequestMapping("/save")
    @ResponseBody
    public ResultVo  saveUser(User user){
        Boolean flag = userService.saveUser(user);
        if (flag) {
            return ResultVo.success("用户记录添加成功");
        }else{
            return ResultVo.error(StatusCode.ERROR_PARAMS,"用户已存在");
        }
    }

    @RequestMapping("/update")
    @ResponseBody
    public ResultVo  updateUser(User user){
        userService.updateUser(user);
        return ResultVo.success("用户记录更新成功");
    }


    @RequestMapping("/delete")
    @ResponseBody
    public ResultVo  deleteUser(Integer[] ids){
        System.out.println(ids.toString()+"ids");
        userService.deleteUserByIds( ids);
        return ResultVo.success("用户记录更新成功");
    }

    /**
     * 查询所有的客户经理
     * @return
     */
    @RequestMapping("queryAllCustomerManager")
    @ResponseBody
    public List<Map<String,Object>> queryAllCustomerManager(){
        return userService.queryAllCustomerManagers();
    }

}
