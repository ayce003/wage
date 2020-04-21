package com.ycjd.payslip.service.pushmsg;

import com.ycjd.payslip.dao.pushmsg.IPushMsgDao;
import com.ycjd.payslip.pojo.SequenceId;
import com.ycjd.payslip.pojo.pushmsg.PushMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PushMsgService {
    @Autowired
    private IPushMsgDao pushMsgDao;
    @Autowired
    private SequenceId sequenceId;

    @Transactional(readOnly = true)
    public PushMsg findPushMsgById(String id) {
        return pushMsgDao.findPushMsgById(id);
    }
    @Transactional
    public void savePushMsg(PushMsg pushMsg) {
        pushMsg.setId(sequenceId.nextId());
        pushMsgDao.savePushMsg(pushMsg);
    }
    @Transactional(readOnly = true)
    public List<PushMsg> findPushMsgListByCondition(PushMsg pushMsg) {
        return pushMsgDao.findPushMsgListByCondition(pushMsg);
    }
    @Transactional(readOnly = true)
    public PushMsg findOnePushMsgByCondition(PushMsg pushMsg) {
        return pushMsgDao.findOnePushMsgByCondition(pushMsg);
    }
    @Transactional(readOnly = true)
    public long findPushMsgCountByCondition(PushMsg pushMsg) {
        return pushMsgDao.findPushMsgCountByCondition(pushMsg);
    }
    @Transactional
    public void updatePushMsg(PushMsg pushMsg) {
        pushMsgDao.updatePushMsg(pushMsg);
    }
    @Transactional
    public void deletePushMsg(String id) {
        pushMsgDao.deletePushMsg(id);
    }
    @Transactional
    public void deletePushMsgByCondition(PushMsg pushMsg) {
        pushMsgDao.deletePushMsgByCondition(pushMsg);
    }
    @Transactional
    public void batchSavePushMsg(List<PushMsg> pushMsgs){
        pushMsgs.forEach(pushMsg -> pushMsg.setId(sequenceId.nextId()));
        pushMsgDao.batchSavePushMsg(pushMsgs);
    }

}
