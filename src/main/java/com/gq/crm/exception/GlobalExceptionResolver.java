package com.gq.crm.exception;

import com.alibaba.druid.support.json.JSONUtils;
import com.gq.base.ResultVo;
import com.gq.crm.utils.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @date 2023/6/6
 */

@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    /**
     * 
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o   handler   判断当前访问的是哪一个方法
     * @param e
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        /**
         * 判断返回值类型
         *          视图
         *          JSON
         *  如何判断方法的返回的是视图   还是JSON？
         *  约定：如果方法级别配置@ResponseBody  方法响应内容为JSON   反之  方法响应内容为HTML
         *
         */
        ModelAndView modelAndView = new ModelAndView();
        /*设置错误信息*/
        modelAndView.setViewName("error");
        /*设置错误状态吗和信息*/
        modelAndView.addObject("code",400);
        modelAndView.addObject("msg","系统错误，请稍后再试.....");

        /*首先判断登录异常  未登录异常*/
        if (e instanceof NoLoginException){
            modelAndView.setViewName("no_login");
            modelAndView.addObject("msg","用户未登录!");
            /*获取上下文路径*/
            modelAndView.addObject("path",httpServletRequest.getContextPath());
            System.out.println("地址"+httpServletRequest.getContextPath().toString());
            return modelAndView;
        }

        if (o instanceof HandlerMethod){
            HandlerMethod hm  = (HandlerMethod) o;
            ResponseBody responseBody = hm.getMethod().getDeclaredAnnotation(ResponseBody.class);
            if (null == responseBody){
                /**
                 * 方法响应的内容为视图
                 */
                if (e instanceof ParamsException){
                    ParamsException pe = (ParamsException) e;
                    modelAndView.addObject("msg",pe.getMsg());
                    modelAndView.addObject("code",pe.getCode());
                }else if (e instanceof AuthException){//认证异常
                    AuthException ae = (AuthException) e;
                    modelAndView.addObject("msg",ae.getMsg());
                    modelAndView.addObject("code",ae.getCode());
                }
                return modelAndView;
            }else{
                /**
                 * 方法响应的内容JSON
                 */
                ResultVo resultVo = new ResultVo();
                resultVo.setCode(300);
                resultVo.setMsg("系统错误，请稍后再试. . .");

                /* e  异常参数异常  */
                if (e instanceof ParamsException){
                    ParamsException pe = (ParamsException) e;
                    resultVo.setCode(pe.getCode());
                    resultVo.setMsg(pe.getMsg());
                }

                /*响应头信息*/
                httpServletResponse.setContentType("application/json;charset=utf-8");
                httpServletResponse.setCharacterEncoding("utf-8");

                PrintWriter pw = null ;
                try {
                    pw = httpServletResponse.getWriter();
                    pw.write(JSONUtils.toJSONString(resultVo));
                    pw.flush();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }finally {
                    if (null != pw){
                        /*关闭流*/
                        pw.close();
                    }
                }
                return null;
            }
        }else{
            return modelAndView;
        }
    }
}
