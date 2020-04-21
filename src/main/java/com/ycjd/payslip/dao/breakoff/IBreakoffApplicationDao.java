package com.ycjd.payslip.dao.breakoff;

import java.util.List;

import com.ycjd.payslip.pojo.breakoff.BreakoffApplication;
import com.ycjd.payslip.pojo.breakoff.BreakoffForPersonnelMatters;
import com.ycjd.payslip.pojo.breakoff.BreakoffObj;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IBreakoffApplicationDao {
    List<BreakoffApplication> findBreakoffApplicationListByCondition(BreakoffApplication breakoffApplication);

    List<BreakoffApplication> findBreakoffApplicationsByAuditor(BreakoffObj breakoffObj);

    List<BreakoffApplication> findBreakoffApplicationListByCondition1(BreakoffApplication breakoffApplication);

    List<BreakoffApplication> findBreakoffApplicationListByApId(BreakoffApplication breakoffApplication);

    List<BreakoffApplication> findBreakoffApplicationDetail(String id);

    long findBreakoffApplicationCountByCondition(BreakoffApplication breakoffApplication);


    long findBreakoffApplicationCountByAuditor(BreakoffObj breakoffObj);

    BreakoffApplication findOneBreakoffApplicationByCondition(BreakoffApplication breakoffApplication);

    BreakoffApplication findBreakoffApplicationById(@Param("id") String id);

    void saveBreakoffApplication(BreakoffApplication breakoffApplication);

    void updateBreakoffApplication(BreakoffApplication breakoffApplication);

    void deleteBreakoffApplication(@Param("id") String id);

    void deleteBreakoffApplicationByCondition(BreakoffApplication breakoffApplication);

    void batchSaveBreakoffApplication(List<BreakoffApplication> breakoffApplications);

    BreakoffApplication findMyRemainingTime(BreakoffApplication breakoffApplication);

    void updateAllByAuditor(BreakoffApplication breakoffApplication);

    List<BreakoffForPersonnelMatters> findBreakoffApplicationsByPersonnelMatters(BreakoffObj breakoffObj);

    long findBreakoffApplicationsByPersonnelMattersCount(BreakoffObj breakoffObj);

    long findbreakoffApplicationsByWorkerIdCount(BreakoffObj breakoffObj);

    List<BreakoffApplication> findbreakoffApplicationsByWorkerId(BreakoffObj breakoffObj);

    BreakoffApplication findPersonnelMattersRemainingTime(BreakoffObj breakoffObj);

    void updateAllByPersonnelMatters(BreakoffApplication breakoffApplication);

    void ClearBreakoff();
}
