package com.ycjd.payslip.dao.breakoff;

import java.util.List;

import com.ycjd.payslip.pojo.breakoff.BreakoffApplication;
import com.ycjd.payslip.pojo.breakoff.BreakoffCheck;
import com.ycjd.payslip.pojo.breakoff.BreakoffObj;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IBreakoffCheckDao {
    List<BreakoffCheck> findBreakoffCheckListByCondition(BreakoffCheck breakoffCheck);

    long findBreakoffCheckCountByCondition(BreakoffCheck breakoffCheck);

    BreakoffCheck findOneBreakoffCheckByCondition(BreakoffCheck breakoffCheck);

    BreakoffCheck findBreakoffCheckById(@Param("id") String id);

    void saveBreakoffCheck(BreakoffCheck breakoffCheck);

    void updateBreakoffCheck(BreakoffCheck breakoffCheck);

    void updateAuditTime(BreakoffCheck breakoffCheck);

    void deleteBreakoffCheck(@Param("id") String id);

    void deleteBreakoffCheckByBreakoffId(@Param("id") String id);

    void deleteBreakoffCheckByCondition(BreakoffCheck breakoffCheck);

    void batchSaveBreakoffCheck(List<BreakoffCheck> breakoffChecks);

    void updateBreakoffCheckByAuditor(BreakoffCheck breakoffCheck);

    void updateAllByAuditor(BreakoffApplication breakoffApplication);

    BreakoffCheck findWorkerRemainingTime(BreakoffObj breakoffObj);


}
