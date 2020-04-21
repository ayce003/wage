package com.ycjd.payslip.controller.forget;

import cn.hutool.crypto.digest.DigestUtil;
import com.ycjd.payslip.pojo.worker.Worker;
import com.ycjd.payslip.service.worker.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ycjd.payslip.interceptor.LoginInterceptor.myId;

@RestController
@RequestMapping("/forget")
public class forgetController {
    @Autowired
    private WorkerService workerService;
    @PostMapping("/updatePassword")
    public void updatePassword(@RequestBody Worker worker){
        String newpassword= DigestUtil.sha1Hex(worker.getNewPassword());
        worker.setId(myId());
        worker.setPassword(newpassword);
        workerService.updateWorker(worker);
    }
}
