package com.ycjd.payslip.interceptor;

import cn.hutool.json.JSONUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.ycjd.payslip.pojo.Constant;
import com.ycjd.payslip.pojo.ResponseJson;
import com.ycjd.payslip.pojo.worker.Worker;
import com.ycjd.payslip.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class LoginInterceptor implements HandlerInterceptor {
    public static ThreadLocal<Worker> tl=new ThreadLocal<>();
    @CreateCache(name = Constant.Redis.WORKER_CACHE,expire = Constant.Redis.WORKER_CACHE_TIMEOUT)
    private Cache<String,Worker> workerCache;
    //123
    public static Worker currentWorker(){
        return tl.get();
    }
    public static String myId(){
        return currentWorker().getId();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String token = request.getHeader(Constant.TOKEN);
        if(token==null||token==""){
            return  writeResponse(new ResponseJson(false,Constant.HAVEN_LOGIN,"登录信息失效"),response);
        }else {
            Claims claims = null;
            try {
                claims = JwtUtil.parseJWT(token);
            } catch (Exception e) {
                System.out.println("解析token失败:");
                System.out.println(e.getMessage());
                return  writeResponse (new ResponseJson(false, Constant.HAVEN_LOGIN,"登录信息失效"),response);
            }
            if(claims!=null) {
                String workerId = claims.getId();
                if (workerId == null) {
                    return  writeResponse(new ResponseJson(false,Constant.HAVEN_LOGIN,"登录信息失效"),response);
                }else{
                    final Worker exist = workerCache.get(workerId);
                    if(exist==null){
                        return writeResponse(new ResponseJson(false, Constant.HAVEN_LOGIN,"登录信息失效"),response);
                    }
                    workerCache.put(workerId,exist);
                    tl.set(exist);
                }
            }
    }
      return true;
    }

    private boolean writeResponse(ResponseJson responseJson, HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "application/json; charset=UTF-8");
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=1");
        PrintWriter writer = response.getWriter();
        writer.write(JSONUtil.toJsonStr(responseJson));
        return false;
    }
}
