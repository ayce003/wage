package com.ycjd.payslip.controller.overtime;

import com.ycjd.payslip.service.breakoff.BreakoffApplicationService;
import com.ycjd.payslip.service.overtime.OvertimeApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/*@Component
@EnableScheduling*/
public class OvertimeMsgTask {
    /*@Autowired
    private OvertimeApplicationService overtimeApplicationService;
    @Autowired
    private BreakoffApplicationService breakoffApplicationService;


    @Async
    @Scheduled(cron = "0 0 0 1 1 *")
    public void ClearTime(){
        overtimeApplicationService.ClearOvertime();
        breakoffApplicationService.ClearBreakoff();
    }*/
}
