package com.ycjd.payslip.controller.kaptcha;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.ycjd.payslip.pojo.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class KaptchaController {
    @Autowired
    private DefaultKaptcha defaultKaptcha;
    @CreateCache(name= Constant.Redis.CODE_CACHE,expire = Constant.Redis.CODE_IMG_CACHE_TIMEOUT)
    private Cache<String,Object> codeCache;
    /**
     * 验证码生成
     */
    @RequestMapping("/captcha.jpg")
    public void captcha(HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        HttpSession httpSession=request.getSession();
        // 生成文字验证码
        String text = defaultKaptcha.createText();
        // 生成图片验证码
        BufferedImage image = defaultKaptcha.createImage(text);
        // 保存到redis
        codeCache.put(httpSession.getId(),text);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        out.flush();
    }




    /**
     * 验证码生成
     */
    @RequestMapping("/linecaptcha.jpg")
    public void linecaptcha(HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        HttpSession httpSession=request.getSession();
        //定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100,4,50);
        lineCaptcha.createCode();
        codeCache.put(httpSession.getId(),lineCaptcha.getCode());
        lineCaptcha.write(response.getOutputStream());
    }

}
