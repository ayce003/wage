package com.ycjd.payslip.service.overtime;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.hutool.core.date.DateUtil;
import com.ycjd.payslip.dao.overtime.IOvertimeApplicationDao;
import com.ycjd.payslip.dao.overtime.IOvertimeCheckDao;
import com.ycjd.payslip.pojo.Constant;
import com.ycjd.payslip.pojo.SequenceId;
import com.ycjd.payslip.pojo.breakoff.BreakoffApplication;
import com.ycjd.payslip.pojo.overtime.*;
import com.ycjd.payslip.service.worker.WorkerService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ycjd.payslip.interceptor.LoginInterceptor.myId;

@Service
public class OvertimeApplicationService {
    @Autowired
    private IOvertimeApplicationDao overtimeApplicationDao;
    @Autowired
    private SequenceId sequenceId;
    @Autowired
    private IOvertimeCheckDao overtimeCheckDao;
    @Autowired
    private WorkerService workerService;
    @Transactional(readOnly = true)
    public OvertimeApplication findOvertimeApplicationById(String id) {
        return overtimeApplicationDao.findOvertimeApplicationById(id);
    }
    @Transactional
    public void saveOvertimeApplication(OvertimeApplication overtimeApplication) {
        overtimeApplication.setId(sequenceId.nextId());
        overtimeApplicationDao.saveOvertimeApplication(overtimeApplication);
    }

    @Transactional
    public void saveOvertimeApplication1(OvertimeApplication overtimeApplication) {
        overtimeApplication.setId(sequenceId.nextId());
        overtimeApplication.setStartTime(overtimeApplication.getRangeTime()[0]);
        overtimeApplication.setEndTime(overtimeApplication.getRangeTime()[1]);
        overtimeApplication.setTotalTime(String.valueOf(Double.parseDouble(overtimeApplication.getTotalTime())));
        overtimeApplication.setProgress(0);
        overtimeApplication.setStatus(Constant.Overtime.DEFAULTSTATUS);
        overtimeApplicationDao.saveOvertimeApplication(overtimeApplication);


        OvertimeCheck overtimeCheck=new OvertimeCheck();

        for (int i = 0; i <overtimeApplication.getOvertimeSortList().size() ; i++) {
            overtimeCheck.setId(sequenceId.nextId());
            overtimeCheck.setOvertimeId(overtimeApplication.getId());
            overtimeCheck.setStatus(Constant.Overtime.UNAUDITED);
            overtimeCheck.setAuditorId(overtimeApplication.getOvertimeSortList().get(i).getWorkerId());
            overtimeCheck.setSort(overtimeApplication.getOvertimeSortList().get(i).getSort());
            if(i==overtimeApplication.getOvertimeSortList().size()-1){
                overtimeCheck.setNextSort(-1);
            }else {
                overtimeCheck.setNextSort(overtimeApplication.getOvertimeSortList().get(i).getSort()+1);
            }
            overtimeCheckDao.saveOvertimeCheck(overtimeCheck);
        }
    }


    @Transactional(readOnly = true)
    public List<OvertimeApplication> findOvertimeApplicationListByCondition(OvertimeApplication overtimeApplication) {
        return overtimeApplicationDao.findOvertimeApplicationListByCondition(overtimeApplication);
    }

    @Transactional(readOnly = true)
    public List<OvertimeApplication> findOvertimeApplicationListByCondition1(OvertimeApplication overtimeApplication) {
        return overtimeApplicationDao.findOvertimeApplicationListByCondition1(overtimeApplication);
    }
    @Transactional(readOnly = true)
    public List<OvertimeApplication> findOvertimeApplicationListByApId(OvertimeApplication overtimeApplication) {
        return overtimeApplicationDao.findOvertimeApplicationListByApId(overtimeApplication);
    }

    @Transactional(readOnly = true)
    public List<OvertimeApplication> findOvertimeApplicationDetail(String id) {
        return overtimeApplicationDao.findOvertimeApplicationDetail(id);
    }

    @Transactional(readOnly = true)
    public List<OvertimeApplication> findOvertimeApplicationsByAuditor(OvertimeObj overtimeObj) {
        return overtimeApplicationDao.findOvertimeApplicationsByAuditor(overtimeObj);
    }

    @Transactional(readOnly = true)
    public List<OvertimeForPersonnelMatters> findOvertimeApplicationsByPersonnelMatters(OvertimeObj overtimeObj) {
        List<OvertimeForPersonnelMatters> overtimeApplicationsByPersonnelMatters = overtimeApplicationDao.findOvertimeApplicationsByPersonnelMatters(overtimeObj);
        for (int i = 0; i < overtimeApplicationsByPersonnelMatters.size(); i++) {
            String subDeptName = workerService.getSubDeptName(overtimeApplicationsByPersonnelMatters.get(i).getDepartmentId());
            overtimeApplicationsByPersonnelMatters.get(i).setDeptName(subDeptName);
        }
        return overtimeApplicationsByPersonnelMatters;
    }

    @Transactional(readOnly = true)
    public long findOvertimeApplicationsByPersonnelMattersCount(OvertimeObj overtimeObj) {
        return overtimeApplicationDao.findOvertimeApplicationsByPersonnelMattersCount(overtimeObj);
    }

    @Transactional(readOnly = true)
    public List<OvertimeApplication> findOvertimeApplicationsByWorkerId(OvertimeObj overtimeObj) {
        return overtimeApplicationDao.findOvertimeApplicationsByWorkerId(overtimeObj);
    }

    @Transactional(readOnly = true)
    public OvertimeApplication findOneOvertimeApplicationByCondition(OvertimeApplication overtimeApplication) {
        return overtimeApplicationDao.findOneOvertimeApplicationByCondition(overtimeApplication);
    }
    @Transactional(readOnly = true)
    public long findOvertimeApplicationCountByCondition(OvertimeApplication overtimeApplication) {
        return overtimeApplicationDao.findOvertimeApplicationCountByCondition(overtimeApplication);
    }

    @Transactional(readOnly = true)
    public long findOvertimeApplicationsByWorkerIdCount(OvertimeObj overtimeObj) {
        return overtimeApplicationDao.findOvertimeApplicationsByWorkerIdCount(overtimeObj);
    }

    @Transactional(readOnly = true)
    public long findOvertimeApplicationsCountByAuditor(OvertimeObj overtimeObj) {
        return overtimeApplicationDao.findOvertimeApplicationsCountByAuditor(overtimeObj);
    }

    @Transactional
    public void updateOvertimeApplication(OvertimeApplication overtimeApplication) {
        OvertimeApplication overtimeApplicationById = this.findOvertimeApplicationById(overtimeApplication.getId());
        if(overtimeApplication.getStatus().equals("1")){
            overtimeApplication.setAvailableTime(overtimeApplicationById.getTotalTime());
            overtimeApplication.setSaveTime(overtimeApplicationById.getTotalTime());
        }else{
            overtimeApplication.setAvailableTime("0");
            overtimeApplication.setSaveTime("0");
        }
        overtimeApplication.setPersonnelTime(DateUtil.now());
        overtimeApplicationDao.updateOvertimeApplication(overtimeApplication);
    }
    @Transactional
    public void deleteOvertimeApplication(String id) {
        overtimeApplicationDao.deleteOvertimeApplication(id);
    }
    @Transactional
    public void deleteOvertimeApplicationByOvertimeId(String id) {
        overtimeCheckDao.deleteOvertimeCheckByOvertimeId(id);
        overtimeApplicationDao.deleteOvertimeApplication(id);
    }
    @Transactional
    public void deleteOvertimeApplicationByCondition(OvertimeApplication overtimeApplication) {
        overtimeApplicationDao.deleteOvertimeApplicationByCondition(overtimeApplication);
    }
    @Transactional
    public void batchSaveOvertimeApplication(List<OvertimeApplication> overtimeApplications){
        overtimeApplications.forEach(overtimeApplication -> overtimeApplication.setId(sequenceId.nextId()));
        overtimeApplicationDao.batchSaveOvertimeApplication(overtimeApplications);
    }

    @Transactional
    public OvertimeApplication findMyRemainingTime(OvertimeApplication overtimeApplication){
        return overtimeApplicationDao.findMyRemainingTime(overtimeApplication);
    }

    @Transactional
    public void updateAllByPersonnelMatters(OvertimeObj overtimeObj) {
        OvertimeApplication overtimeApplication=new OvertimeApplication();
        for (int i = 0; i <overtimeObj.getOvertimeIds().length ; i++) {
            overtimeApplication.setId(overtimeObj.getOvertimeIds()[i]);
            OvertimeApplication overtimeApplicationById = this.findOvertimeApplicationById(overtimeObj.getOvertimeIds()[i]);
            if(overtimeObj.getStatus().equals("1")){
                overtimeApplication.setAvailableTime(overtimeApplicationById.getTotalTime());
                overtimeApplication.setSaveTime(overtimeApplicationById.getTotalTime());
            }else {
                overtimeApplication.setAvailableTime("0");
                overtimeApplication.setSaveTime("0");
            }
            overtimeApplication.setStatus(overtimeObj.getStatus());
            overtimeApplication.setPersonnelTime(DateUtil.now());
            overtimeApplicationDao.updateAllByPersonnelMatters(overtimeApplication);
        }
    }
    @Transactional
    public void ClearOvertime(){
        overtimeApplicationDao.ClearOvertime();
    }
    @Transactional(readOnly = true)
    public List<OvertimeApplication> myOvertimerecord(OvertimeApplication overtimeApplication){
        return  overtimeApplicationDao.myOvertimeRecord(overtimeApplication);
    }

    public Workbook exportOvertime(String dataTime){
        List<ExcelExportEntity> exportEntityList=new ArrayList<>();
        exportEntityList.add(new ExcelExportEntity("姓名","name",15));
        exportEntityList.add(new ExcelExportEntity("开始时间","start_time",30));
        exportEntityList.add(new ExcelExportEntity("结束时间","end_time",30));
        exportEntityList.add(new ExcelExportEntity("加班时长(天)","days",20));
        List<Map<String,Object>> overtimeList= overtimeApplicationDao.exportOvertime(dataTime);
        return ExcelExportUtil.exportExcel(new ExportParams("加班申请表","加班申请"),exportEntityList,overtimeList);
    }
}
