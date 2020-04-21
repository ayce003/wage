package com.ycjd.payslip.config;

import javax.servlet.http.HttpServletResponse;

import com.ycjd.payslip.pojo.ResponseJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger=LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody //在返回自定义相应类的情况下必须有，这是@ControllerAdvice注解的规定
    public ResponseJson exceptionHandler(RuntimeException e, HttpServletResponse response) {
        e.printStackTrace();
        logger.error(e.getMessage());
        ResponseJson resp = new ResponseJson(false,String.format("服务器异常:%s",e.getMessage()));
        resp.setData(String.format("服务器异常:%s",getStackMsg(e)));
        return resp;
    }

    private  String getStackMsg(Exception e) {

        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stackArray = e.getStackTrace();
        for (int i = 0; i < stackArray.length; i++) {
            StackTraceElement element = stackArray[i];
            sb.append(element.toString() + "\n");
        }
        return sb.toString();
    }
}