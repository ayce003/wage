package com.ycjd.payslip.dao.overtime;

import java.util.List;

import com.ycjd.payslip.pojo.overtime.OvertimeApplication;
import com.ycjd.payslip.pojo.overtime.OvertimeCheck;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IOvertimeCheckDao {
    List<OvertimeCheck> findOvertimeCheckListByCondition(OvertimeCheck overtimeCheck);

    long findOvertimeCheckCountByCondition(OvertimeCheck overtimeCheck);

    OvertimeCheck findOneOvertimeCheckByCondition(OvertimeCheck overtimeCheck);

    OvertimeCheck findOvertimeCheckById(@Param("id") String id);

    void saveOvertimeCheck(OvertimeCheck overtimeCheck);

    void updateOvertimeCheck(OvertimeCheck overtimeCheck);

    void deleteOvertimeCheck(@Param("id") String id);

    void deleteOvertimeCheckByOvertimeId(@Param("id") String id);

    void deleteOvertimeCheckByCondition(OvertimeCheck overtimeCheck);

    void batchSaveOvertimeCheck(List<OvertimeCheck> overtimeChecks);

    void updateOvertimeCheckByAuditor(OvertimeCheck overtimeCheck);

    void updateAllByAuditor(OvertimeApplication overtimeApplication);
}
