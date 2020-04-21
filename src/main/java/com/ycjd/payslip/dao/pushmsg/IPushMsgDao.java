package com.ycjd.payslip.dao.pushmsg;

import java.util.List;

import com.ycjd.payslip.pojo.pushmsg.PushMsg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IPushMsgDao {
    List<PushMsg> findPushMsgListByCondition(PushMsg pushMsg);

    long findPushMsgCountByCondition(PushMsg pushMsg);

    PushMsg findOnePushMsgByCondition(PushMsg pushMsg);

    PushMsg findPushMsgById(@Param("id") String id);

    void savePushMsg(PushMsg pushMsg);

    void updatePushMsg(PushMsg pushMsg);

    void deletePushMsg(@Param("id") String id);

    void deletePushMsgByCondition(PushMsg pushMsg);

    void batchSavePushMsg(List<PushMsg> pushMsgs);
}
