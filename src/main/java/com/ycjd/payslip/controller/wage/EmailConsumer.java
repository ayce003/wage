package com.ycjd.payslip.controller.wage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*@Component
@RabbitListener(queues = "email")
public class EmailConsumer {
    @Autowired
    private AmqpTemplate rabbitmqTemplate;
    private Logger logger = LoggerFactory.getLogger(getClass());//提供日志类
    *//**
     * 消息消费
     * @RabbitHandler 代表此方法为接受到消息后的处理方法
     *//*
    @RabbitHandler
    public void recieved(String msg) {
        logger.info(msg);
    }
}*/
