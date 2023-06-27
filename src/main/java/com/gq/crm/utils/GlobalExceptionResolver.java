package com.gq.crm.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gq.base.ResultVo;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;


/**
 * @Auther: 胖叔讲java
 * @Date: 2022/10/13 - 10 - 13 - 16:31
 * @Decsription: com.study.crm.exception
 * @version: 1.0
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        //封装请求转发响应结果
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("code","500");
        modelAndView.addObject("msg","系统内部异常");
        modelAndView.setViewName("error");
        //判断是否是controller层方法引起的异常
        if(handler instanceof HandlerMethod){
            //获取controller层方法的@ResponseBody注解
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            ResponseBody responseBody = method.getAnnotation(ResponseBody.class);
            //如果方法上有@ResponseBody注解，前后分离开发异常处理
            if(responseBody!=null){
                try {
                    handlerException(request,response,e);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return modelAndView;
    }
    //根据不同异常响应不同的json数据
    private void handlerException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
        //根据不同类型异常封装不同类型json数据
        ResultVo resultVo = null;
        if(e instanceof ParamsException){
            ParamsException paramsException = (ParamsException)e;
            resultVo = ResultVo.error(paramsException.getCode(),paramsException.getMsg());
        }else if(e instanceof AccessDeniedException){
            resultVo = ResultVo.error("权限不足");
        } else {
            e.printStackTrace();
            resultVo = ResultVo.error("系统内部异常");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(resultVo);
        //设置响应json数据格式数据
        response.setContentType("application/json;charset=utf-8");
        //响应数据
        PrintWriter writer = response.getWriter();
        writer.print(result);
        //刷数据，结束响应，防止运行后面的代码
        writer.flush();
        writer.close();
    }
}
