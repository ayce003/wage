package com.ycjd.payslip.service.breakoff;

import cn.hutool.core.date.DateUtil;
import com.ycjd.payslip.dao.breakoff.IBreakoffApplicationDao;
import com.ycjd.payslip.dao.breakoff.IBreakoffCheckDao;
import com.ycjd.payslip.dao.pushmsg.IPushMsgDao;
import com.ycjd.payslip.pojo.SequenceId;
import com.ycjd.payslip.pojo.breakoff.BreakoffApplication;
import com.ycjd.payslip.pojo.breakoff.BreakoffCheck;
import com.ycjd.payslip.pojo.breakoff.BreakoffObj;
import com.ycjd.payslip.pojo.pushmsg.PushMsg;
import com.ycjd.payslip.service.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static com.ycjd.payslip.interceptor.LoginInterceptor.myId;

@Service
public class BreakoffCheckService {
    @Autowired
    private IBreakoffCheckDao breakoffCheckDao;
    @Autowired
    private SequenceId sequenceId;
    @Autowired
    private IBreakoffApplicationDao breakoffApplicationDao;
    @Autowired
    private IPushMsgDao pushMsgDao;
    @Transactional(readOnly = true)
    public BreakoffCheck findBreakoffCheckById(String id) {
        return breakoffCheckDao.findBreakoffCheckById(id);
    }
    @Transactional
    public void saveBreakoffCheck(BreakoffCheck breakoffCheck) {
        breakoffCheck.setId(sequenceId.nextId());
        breakoffCheckDao.saveBreakoffCheck(breakoffCheck);
    }
    @Transactional(readOnly = true)
    public List<BreakoffCheck> findBreakoffCheckListByCondition(BreakoffCheck breakoffCheck) {
        return breakoffCheckDao.findBreakoffCheckListByCondition(breakoffCheck);
    }
    @Transactional(readOnly = true)
    public BreakoffCheck findOneBreakoffCheckByCondition(BreakoffCheck breakoffCheck) {
        return breakoffCheckDao.findOneBreakoffCheckByCondition(breakoffCheck);
    }
    @Transactional(readOnly = true)
    public long findBreakoffCheckCountByCondition(BreakoffCheck breakoffCheck) {
        return breakoffCheckDao.findBreakoffCheckCountByCondition(breakoffCheck);
    }
    @Transactional
    public void updateBreakoffCheck(BreakoffCheck breakoffCheck) {
        breakoffCheckDao.updateBreakoffCheck(breakoffCheck);
    }

    @Transactional
    public void updateBreakoffCheckByAuditor(BreakoffCheck breakoffCheck) throws IOException {
        breakoffCheck.setAuditorId(myId());
        breakoffCheck.setThroughTime(DateUtil.now());
        breakoffCheckDao.updateBreakoffCheckByAuditor(breakoffCheck);
        BreakoffApplication breakoffApplication=new BreakoffApplication();
        breakoffApplication.setId(breakoffCheck.getBreakoffId());
        if(breakoffCheck.getStatus().equals("1")){
            if(breakoffCheck.getNextSort()==-1){
                breakoffCheck.setAuditTime(breakoffCheck.getTotalTime());
                breakoffApplication.setAvailableTime(breakoffCheck.getTotalTime());
            }else {
                breakoffCheck.setAuditTime("0");
                breakoffApplication.setAvailableTime("0");
            }
            breakoffCheckDao.updateAuditTime(breakoffCheck);

        }else {
            breakoffApplication.setAvailableTime("0");
        }
        breakoffCheck.setStatus(null);
        breakoffCheck.setThroughTime(null);
        BreakoffCheck oneBreakoffCheck = breakoffCheckDao.findOneBreakoffCheckByCondition(breakoffCheck);
        if(!oneBreakoffCheck.getStatus().equals("2")){
            breakoffApplication.setProgress(oneBreakoffCheck.getNextSort());
        }
        breakoffApplicationDao.updateBreakoffApplication(breakoffApplication);

        if(WebSocketServer.onlineSessions.get(breakoffCheck.getApplicantId())==null){
            PushMsg pushMsg=new PushMsg();
            pushMsg.setId(sequenceId.nextId());
            pushMsg.setApplicantId(breakoffCheck.getApplicantId());
            pushMsg.setMessage(breakoffCheck.getAuditorName()+"审批了您的申请");
            pushMsgDao.savePushMsg(pushMsg);
        }else {
            WebSocketServer.onlineSessions.get(breakoffCheck.getApplicantId()).getBasicRemote().sendText(breakoffCheck.getAuditorName()+"审批了您的申请");
        }
    }

    @Transactional
    public void deleteBreakoffCheck(String id) {
        breakoffCheckDao.deleteBreakoffCheck(id);
    }
    @Transactional
    public void deleteBreakoffCheckByCondition(BreakoffCheck breakoffCheck) {
        breakoffCheckDao.deleteBreakoffCheckByCondition(breakoffCheck);
    }
    @Transactional
    public void batchSaveBreakoffCheck(List<BreakoffCheck> breakoffChecks){
        breakoffChecks.forEach(breakoffCheck -> breakoffCheck.setId(sequenceId.nextId()));
        breakoffCheckDao.batchSaveBreakoffCheck(breakoffChecks);
    }

    @Transactional
    public void updateAllByAuditor(BreakoffObj breakoffObj) throws IOException {
        BreakoffApplication breakoffApplication=new BreakoffApplication();
        BreakoffCheck breakoffCheck=new BreakoffCheck();
        BreakoffApplication breakoff=new BreakoffApplication();
        for (int i = 0; i < breakoffObj.getBreakoffIds().length; i++) {
            if(breakoffObj.getStatus().equals("1")){
                breakoffCheck.setBreakoffId(breakoffObj.getBreakoffIds()[i]);
                breakoffCheck.setAuditorId(myId());
                if(breakoffObj.getNextSorts()[i]==-1){
                    breakoffCheck.setAuditTime(breakoffObj.getAuditTimes()[i]);
                }else {
                    breakoffCheck.setAuditTime("0");
                }
                breakoffCheckDao.updateAuditTime(breakoffCheck);
            }else {
                breakoff.setId(breakoffObj.getBreakoffIds()[i]);
                breakoff.setAvailableTime("0");
                breakoffApplicationDao.updateBreakoffApplication(breakoff);
            }
            breakoffApplication.setId(breakoffObj.getBreakoffIds()[i]);
            breakoffApplication.setAuditorId(myId());
            breakoffApplication.setStatus(breakoffObj.getStatus());
            breakoffApplication.setThroughTime(DateUtil.now());
            if(!breakoffObj.getStatus().equals("2")){
                breakoffApplication.setProgress(breakoffObj.getNextSorts()[i]);
            }
            breakoffApplicationDao.updateAllByAuditor(breakoffApplication);
            breakoffCheckDao.updateAllByAuditor(breakoffApplication);

            if(WebSocketServer.onlineSessions.get(breakoffObj.getApplicantIds()[i])==null){
                PushMsg pushMsg=new PushMsg();
                pushMsg.setId(sequenceId.nextId());
                pushMsg.setApplicantId(breakoffObj.getApplicantIds()[i]);
                pushMsg.setMessage(breakoffObj.getAuditorNames()[i]+"审批了您的申请");
                pushMsgDao.savePushMsg(pushMsg);
            }else{
                WebSocketServer.onlineSessions.get(breakoffObj.getApplicantIds()[i]).getBasicRemote().sendText(breakoffObj.getAuditorNames()[i]+"审核了您的申请");
            }
        }
    }

    @Transactional
    public BreakoffCheck findWorkerRemainingTime(BreakoffObj breakoffObj){
        return breakoffCheckDao.findWorkerRemainingTime(breakoffObj);
    }
}
