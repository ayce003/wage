package com.ycjd.payslip.util;

import cn.hutool.json.JSONUtil;
import com.aliyuncs.exceptions.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MsnReceiver {
    private static final Logger logger = LoggerFactory.getLogger(MsnReceiver.class);
    @Autowired
    private AliMsn aliMsn;


    public void receiveMessage(String json) throws ClientException {
        Msn msn = JSONUtil.toBean(json, Msn.class);
        aliMsn.sendVerifySms(msn);
    }
}
