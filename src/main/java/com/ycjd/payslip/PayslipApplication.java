package com.ycjd.payslip;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {"com.ycjd.payslip"})
/*@MapperScan(basePackge="com.ycjd.payslip.dao")*/
/*@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})*/
@Configuration
@EnableMethodCache(basePackages = "com.ycjd.payslip")
@EnableCreateCacheAnnotation
@EnableAsync
public class PayslipApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayslipApplication.class,args);
    }
}
