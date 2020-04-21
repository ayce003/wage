package com.ycjd.payslip.service.overtime;

import cn.hutool.core.date.DateUtil;
import com.ycjd.payslip.dao.overtime.IOvertimeApplicationDao;
import com.ycjd.payslip.dao.overtime.IOvertimeCheckDao;
import com.ycjd.payslip.dao.pushmsg.IPushMsgDao;
import com.ycjd.payslip.pojo.SequenceId;
import com.ycjd.payslip.pojo.overtime.OvertimeApplication;
import com.ycjd.payslip.pojo.overtime.OvertimeCheck;
import com.ycjd.payslip.pojo.overtime.OvertimeObj;
import com.ycjd.payslip.pojo.pushmsg.PushMsg;
import com.ycjd.payslip.service.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.websocket.Session;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.ycjd.payslip.interceptor.LoginInterceptor.myId;

@Service
public class OvertimeCheckService {
    @Autowired
    private IOvertimeCheckDao overtimeCheckDao;
    @Autowired
    private SequenceId sequenceId;
    @Autowired
    private IOvertimeApplicationDao overtimeApplicationDao;
    @Autowired
    private IPushMsgDao pushMsgDao;
    @Transactional(readOnly = true)
    public OvertimeCheck findOvertimeCheckById(String id) {
        return overtimeCheckDao.findOvertimeCheckById(id);
    }
    @Transactional
    public void saveOvertimeCheck(OvertimeCheck overtimeCheck) {
        overtimeCheck.setId(sequenceId.nextId());
        overtimeCheckDao.saveOvertimeCheck(overtimeCheck);
    }
    @Transactional(readOnly = true)
    public List<OvertimeCheck> findOvertimeCheckListByCondition(OvertimeCheck overtimeCheck) {
        return overtimeCheckDao.findOvertimeCheckListByCondition(overtimeCheck);
    }
    @Transactional(readOnly = true)
    public OvertimeCheck findOneOvertimeCheckByCondition(OvertimeCheck overtimeCheck) {
        return overtimeCheckDao.findOneOvertimeCheckByCondition(overtimeCheck);
    }
    @Transactional(readOnly = true)
    public long findOvertimeCheckCountByCondition(OvertimeCheck overtimeCheck) {
        return overtimeCheckDao.findOvertimeCheckCountByCondition(overtimeCheck);
    }
    @Transactional
    public void updateOvertimeCheck(OvertimeCheck overtimeCheck) {
        overtimeCheckDao.updateOvertimeCheck(overtimeCheck);
    }

    @Transactional
    public void updateOvertimeCheckByAuditor(OvertimeCheck overtimeCheck) throws IOException {
        overtimeCheck.setAuditorId(myId());
        overtimeCheck.setThroughTime(DateUtil.now());
        overtimeCheckDao.updateOvertimeCheckByAuditor(overtimeCheck);
        overtimeCheck.setStatus(null);
        overtimeCheck.setThroughTime(null);
        OvertimeCheck oneOvertimeCheck = overtimeCheckDao.findOneOvertimeCheckByCondition(overtimeCheck);
        OvertimeApplication overtimeApplication=new OvertimeApplication();
        overtimeApplication.setId(overtimeCheck.getOvertimeId());
        if(!oneOvertimeCheck.getStatus().equals("2")){
            overtimeApplication.setProgress(oneOvertimeCheck.getNextSort());
            overtimeApplicationDao.updateOvertimeApplication(overtimeApplication);
        }
        if(WebSocketServer.onlineSessions.get(overtimeCheck.getApplicantId())==null){
              PushMsg pushMsg=new PushMsg();
              pushMsg.setId(sequenceId.nextId());
              pushMsg.setApplicantId(overtimeCheck.getApplicantId());
              pushMsg.setMessage(overtimeCheck.getAuditorName()+"审批了您的申请");
              pushMsgDao.savePushMsg(pushMsg);
        }else {
            WebSocketServer.onlineSessions.get(overtimeCheck.getApplicantId()).getBasicRemote().sendText(overtimeCheck.getAuditorName()+"审批了您的申请");
        }

    }

    @Transactional
    public void deleteOvertimeCheck(String id) {
        overtimeCheckDao.deleteOvertimeCheck(id);
    }
    @Transactional
    public void deleteOvertimeCheckByCondition(OvertimeCheck overtimeCheck) {
        overtimeCheckDao.deleteOvertimeCheckByCondition(overtimeCheck);
    }
    @Transactional
    public void batchSaveOvertimeCheck(List<OvertimeCheck> overtimeChecks){
        overtimeChecks.forEach(overtimeCheck -> overtimeCheck.setId(sequenceId.nextId()));
        overtimeCheckDao.batchSaveOvertimeCheck(overtimeChecks);
    }

    @Transactional
    public void updateAllByAuditor(OvertimeObj overtimeObj) throws IOException {
        OvertimeApplication overtimeApplication=new OvertimeApplication();
        for (int i = 0; i < overtimeObj.getOvertimeIds().length; i++) {
            overtimeApplication.setId(overtimeObj.getOvertimeIds()[i]);
            overtimeApplication.setAuditorId(myId());
            overtimeApplication.setStatus(overtimeObj.getStatus());
            overtimeApplication.setThroughTime(DateUtil.now());
            if(!overtimeObj.getStatus().equals("2")){
                overtimeApplication.setProgress(overtimeObj.getNextSorts()[i]);
            }
            overtimeApplicationDao.updateAllByAuditor(overtimeApplication);
            overtimeCheckDao.updateAllByAuditor(overtimeApplication);

            if(WebSocketServer.onlineSessions.get(overtimeObj.getApplicantIds()[i])==null){
                PushMsg pushMsg=new PushMsg();
                pushMsg.setId(sequenceId.nextId());
                pushMsg.setApplicantId(overtimeObj.getApplicantIds()[i]);
                pushMsg.setMessage(overtimeObj.getAuditorNames()[i]+"审批了您的申请");
                pushMsgDao.savePushMsg(pushMsg);
            }else{
                WebSocketServer.onlineSessions.get(overtimeObj.getApplicantIds()[i]).getBasicRemote().sendText(overtimeObj.getAuditorNames()[i]+"审核了您的申请");
            }
        }
    }

}
