package com.ycjd.payslip.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.ycjd.payslip.pojo.Constant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {
    @Bean
    public DefaultKaptcha defaultKaptcha() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 字体
        properties.setProperty(Constant.Kaptcha.KAPTCHA_TEXTPRODUCER_FONT_NAMES, "宋体,楷体,微软雅黑");
        properties.setProperty(Constant.Kaptcha.KAPTCHA_BORDER, "no");
        properties.setProperty(Constant.Kaptcha.KAPTCHA_IMAGE_WIDTH ,"100");
        properties.setProperty(Constant.Kaptcha.KAPTCHA_IMAGE_HEIGHT,"40");
        properties.setProperty(Constant.Kaptcha.KAPTCHA_OBSCURIFICATOR_IMPL,"com.google.code.kaptcha.impl.ShadowGimpy");
        properties.setProperty(Constant.Kaptcha.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH,"4");
        properties.setProperty(Constant.Kaptcha.KAPTCHA_TEXTPRODUCER_CHAR_STRING,"123456789");

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
