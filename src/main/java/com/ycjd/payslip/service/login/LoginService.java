package com.ycjd.payslip.service.login;


import cn.hutool.crypto.digest.DigestUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.ycjd.payslip.dao.login.LoginDao;

import com.ycjd.payslip.pojo.Constant;
import com.ycjd.payslip.pojo.worker.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class LoginService {
    @Autowired
    private LoginDao loginDao;
    @CreateCache(name= Constant.Redis.WORKER_CACHE,expire = Constant.Redis.WORKER_CACHE_TIMEOUT)
    private Cache<String,Worker> workerCache;
    public List<Worker> findLoginListByCondition(Worker worker){
        String sha1=worker.getPassword();
        String sha1Hex = DigestUtil.sha1Hex(sha1);
        worker.setPassword(sha1Hex);
        return loginDao.findLoginListByCondition(worker);
    }
    public void saveWorkerToCache(Worker worker){
        workerCache.put(worker.getId(),worker);
    }
}
