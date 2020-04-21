package com.ycjd.payslip.dao.overtime;

import java.util.List;
import java.util.Map;

import com.ycjd.payslip.pojo.breakoff.BreakoffApplication;
import com.ycjd.payslip.pojo.overtime.OvertimeApplication;
import com.ycjd.payslip.pojo.overtime.OvertimeForPersonnelMatters;
import com.ycjd.payslip.pojo.overtime.OvertimeObj;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IOvertimeApplicationDao {
    List<OvertimeApplication> findOvertimeApplicationListByCondition(OvertimeApplication overtimeApplication);

    List<OvertimeApplication> findOvertimeApplicationListByCondition1(OvertimeApplication overtimeApplication);

    List<OvertimeApplication> findOvertimeApplicationListByApId(OvertimeApplication overtimeApplication);

    List<OvertimeApplication> findOvertimeApplicationDetail(String id);

    List<OvertimeApplication> findOvertimeApplicationsByAuditor(OvertimeObj overtimeObj);

    long findOvertimeApplicationsCountByAuditor(OvertimeObj overtimeObj);

    List<OvertimeForPersonnelMatters> findOvertimeApplicationsByPersonnelMatters(OvertimeObj overtimeObj);

    long findOvertimeApplicationsByPersonnelMattersCount(OvertimeObj overtimeObj);

    List<OvertimeApplication> findOvertimeApplicationsByWorkerId(OvertimeObj overtimeObj);

    long findOvertimeApplicationsByWorkerIdCount(OvertimeObj overtimeObj);

    long findOvertimeApplicationCountByCondition(OvertimeApplication overtimeApplication);

    OvertimeApplication findOneOvertimeApplicationByCondition(OvertimeApplication overtimeApplication);

    OvertimeApplication findOvertimeApplicationById(@Param("id") String id);

    void saveOvertimeApplication(OvertimeApplication overtimeApplication);

    void updateOvertimeApplication(OvertimeApplication overtimeApplication);

    void deleteOvertimeApplication(@Param("id") String id);

    void deleteOvertimeApplicationByCondition(OvertimeApplication overtimeApplication);

    void batchSaveOvertimeApplication(List<OvertimeApplication> overtimeApplications);

    OvertimeApplication findMyRemainingTime(OvertimeApplication overtimeApplication);

    void updateAllByAuditor(OvertimeApplication overtimeApplication);

    void updateAllByPersonnelMatters(OvertimeApplication overtimeApplication);

    void ClearOvertime();

    List<Map<String,Object>> exportOvertime(@Param("dataTime") String dataTime);

    List<OvertimeApplication> myOvertimeRecord(OvertimeApplication overtimeApplication);

}
