package com.ycjd.payslip.controller.login;



import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.ycjd.payslip.pojo.Constant;
import com.ycjd.payslip.pojo.ResponseJson;
import com.ycjd.payslip.pojo.worker.Worker;
import com.ycjd.payslip.service.login.LoginService;
import com.ycjd.payslip.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginService loginService;
    @CreateCache(name= Constant.Redis.CODE_CACHE,expire = Constant.Redis.CODE_IMG_CACHE_TIMEOUT)
    private Cache<String,Object> codeCache;
    @PostMapping("/login") // 普通登录
    public ResponseJson login(@RequestBody Worker worker, HttpServletRequest request){
        try {
            HttpSession httpSession=request.getSession();
            String code1= codeCache.get(httpSession.getId()).toString();

            if(worker.getCode().equalsIgnoreCase(code1)){
                List<Worker> workerList = loginService.findLoginListByCondition(worker);
                if(workerList.size()>0&& workerList !=null){
                    String token = JwtUtil.createJWT(workerList.get(0).getId(), "{}", null, -1);
                    loginService.saveWorkerToCache(workerList.get(0));
                    return new ResponseJson(token,workerList.get(0));
                }else{
                    return new ResponseJson(false,"登录失败,用户名或密码错误");
                }
            }else {
                return new ResponseJson(false,"验证码错误");
            }
        }catch (Exception e){
            System.out.println(e);
            return new ResponseJson(false,"验证码失效");
        }



    }
}
