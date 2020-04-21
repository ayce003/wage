package com.ycjd.payslip.service.breakoff;

import cn.hutool.core.date.DateUtil;
import com.ycjd.payslip.dao.breakoff.IBreakoffApplicationDao;
import com.ycjd.payslip.dao.breakoff.IBreakoffCheckDao;
import com.ycjd.payslip.dao.worker.IWorkerDao;
import com.ycjd.payslip.pojo.Constant;
import com.ycjd.payslip.pojo.SequenceId;
import com.ycjd.payslip.pojo.breakoff.BreakoffApplication;
import com.ycjd.payslip.pojo.breakoff.BreakoffCheck;
import com.ycjd.payslip.pojo.breakoff.BreakoffForPersonnelMatters;
import com.ycjd.payslip.pojo.breakoff.BreakoffObj;
import com.ycjd.payslip.service.worker.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BreakoffApplicationService {
    @Autowired
    private IBreakoffApplicationDao breakoffApplicationDao;
    @Autowired
    private SequenceId sequenceId;
    @Autowired
    private IBreakoffCheckDao breakoffCheckDao;
    @Autowired
    private WorkerService workerService;
    @Transactional(readOnly = true)
    public BreakoffApplication findBreakoffApplicationById(String id) {
        return breakoffApplicationDao.findBreakoffApplicationById(id);
    }
    @Transactional
    public void saveBreakoffApplication(BreakoffApplication breakoffApplication) {
        breakoffApplication.setId(sequenceId.nextId());
        breakoffApplicationDao.saveBreakoffApplication(breakoffApplication);
    }

    @Transactional
    public void saveBreakoffApplication1(BreakoffApplication breakoffApplication) {
        breakoffApplication.setId(sequenceId.nextId());
        breakoffApplication.setStartTime(breakoffApplication.getRangeTime()[0]);
        breakoffApplication.setEndTime(breakoffApplication.getRangeTime()[1]);
        breakoffApplication.setTotalTime(String.valueOf(Double.parseDouble(breakoffApplication.getTotalTime())));
        breakoffApplication.setAvailableTime(String.valueOf(Double.parseDouble(breakoffApplication.getTotalTime())));
        breakoffApplication.setProgress(0);
        breakoffApplication.setStatus(Constant.Overtime.DEFAULTSTATUS);
        breakoffApplicationDao.saveBreakoffApplication(breakoffApplication);


        BreakoffCheck breakoffCheck=new BreakoffCheck();

        for (int i = 0; i <breakoffApplication.getBreakoffSortList().size() ; i++) {
            breakoffCheck.setId(sequenceId.nextId());
            breakoffCheck.setBreakoffId(breakoffApplication.getId());
            breakoffCheck.setStatus(Constant.Overtime.UNAUDITED);
            breakoffCheck.setAuditorId(breakoffApplication.getBreakoffSortList().get(i).getWorkerId());
            breakoffCheck.setSort(breakoffApplication.getBreakoffSortList().get(i).getSort());
            if(i==breakoffApplication.getBreakoffSortList().size()-1){
                breakoffCheck.setNextSort(-1);
            }else {
                breakoffCheck.setNextSort(breakoffApplication.getBreakoffSortList().get(i).getSort()+1);
            }
            breakoffCheckDao.saveBreakoffCheck(breakoffCheck);
        }
    }

    @Transactional(readOnly = true)
    public List<BreakoffApplication> findBreakoffApplicationListByCondition(BreakoffApplication breakoffApplication) {
        return breakoffApplicationDao.findBreakoffApplicationListByCondition(breakoffApplication);
    }

    @Transactional(readOnly = true)
    public List<BreakoffApplication> findBreakoffApplicationListByCondition1(BreakoffApplication breakoffApplication) {
        return breakoffApplicationDao.findBreakoffApplicationListByCondition1(breakoffApplication);
    }

    @Transactional(readOnly = true)
    public List<BreakoffApplication> findBreakoffApplicationsByAuditor(BreakoffObj breakoffObj) {
        return breakoffApplicationDao.findBreakoffApplicationsByAuditor(breakoffObj);
    }

    @Transactional(readOnly = true)
    public List<BreakoffApplication> findBreakoffApplicationListByApId(BreakoffApplication breakoffApplication) {
        return breakoffApplicationDao.findBreakoffApplicationListByApId(breakoffApplication);
    }

    @Transactional(readOnly = true)
    public List<BreakoffApplication> findBreakoffApplicationDetail(String id) {
        return breakoffApplicationDao.findBreakoffApplicationDetail(id);
    }

    @Transactional(readOnly = true)
    public BreakoffApplication findOneBreakoffApplicationByCondition(BreakoffApplication breakoffApplication) {
        return breakoffApplicationDao.findOneBreakoffApplicationByCondition(breakoffApplication);
    }
    @Transactional(readOnly = true)
    public long findBreakoffApplicationCountByCondition(BreakoffApplication breakoffApplication) {
        return breakoffApplicationDao.findBreakoffApplicationCountByCondition(breakoffApplication);
    }
    @Transactional(readOnly = true)
    public long findBreakoffApplicationCountByAuditor(BreakoffObj breakoffObj) {
        return breakoffApplicationDao.findBreakoffApplicationCountByAuditor(breakoffObj);
    }

    @Transactional
    public void updateBreakoffApplication(BreakoffApplication breakoffApplication) {
        BreakoffApplication breakoffApplication1 = this.findBreakoffApplicationById(breakoffApplication.getId());
        if(breakoffApplication.getStatus().equals("1")){
            breakoffApplication.setAvailableTime(breakoffApplication1.getTotalTime());
            breakoffApplication.setSaveTime(breakoffApplication1.getTotalTime());
        }else{
            breakoffApplication.setAvailableTime("0");
            breakoffApplication.setSaveTime("0");
        }
        breakoffApplication.setPersonnelTime(DateUtil.now());
        breakoffApplicationDao.updateBreakoffApplication(breakoffApplication);
    }
    @Transactional
    public void deleteBreakoffApplication(String id) {
        breakoffApplicationDao.deleteBreakoffApplication(id);
    }
    @Transactional
    public void deleteBreakoffApplicationByBreakoffId(String id) {
        breakoffCheckDao.deleteBreakoffCheckByBreakoffId(id);
        breakoffApplicationDao.deleteBreakoffApplication(id);
    }
    @Transactional
    public void deleteBreakoffApplicationByCondition(BreakoffApplication breakoffApplication) {
        breakoffApplicationDao.deleteBreakoffApplicationByCondition(breakoffApplication);
    }
    @Transactional
    public void batchSaveBreakoffApplication(List<BreakoffApplication> breakoffApplications){
        breakoffApplications.forEach(breakoffApplication -> breakoffApplication.setId(sequenceId.nextId()));
        breakoffApplicationDao.batchSaveBreakoffApplication(breakoffApplications);
    }

    @Transactional
    public BreakoffApplication findMyRemainingTime(BreakoffApplication breakoffApplication){
        return breakoffApplicationDao.findMyRemainingTime(breakoffApplication);
    }

    @Transactional(readOnly = true)
    public List<BreakoffForPersonnelMatters> findBreakoffApplicationsByPersonnelMatters(BreakoffObj breakoffObj) {
        List<BreakoffForPersonnelMatters> breakoffForPersonnelMattersList = breakoffApplicationDao.findBreakoffApplicationsByPersonnelMatters(breakoffObj);
        for (int i = 0; i < breakoffForPersonnelMattersList.size(); i++) {
            String subDeptName = workerService.getSubDeptName(breakoffForPersonnelMattersList.get(i).getDepartmentId());
            breakoffForPersonnelMattersList.get(i).setDeptName(subDeptName);
        }
        return breakoffForPersonnelMattersList;
    }

    @Transactional(readOnly = true)
    public long findBreakoffApplicationsByPersonnelMattersCount(BreakoffObj breakoffObj) {
        return breakoffApplicationDao.findBreakoffApplicationsByPersonnelMattersCount(breakoffObj);
    }

    @Transactional(readOnly = true)
    public List<BreakoffApplication> findbreakoffApplicationsByWorkerId(BreakoffObj breakoffObj) {
        return breakoffApplicationDao.findbreakoffApplicationsByWorkerId(breakoffObj);
    }

    @Transactional(readOnly = true)
    public long findbreakoffApplicationsByWorkerIdCount(BreakoffObj breakoffObj) {
        return breakoffApplicationDao.findbreakoffApplicationsByWorkerIdCount(breakoffObj);
    }

    @Transactional
    public BreakoffApplication findPersonnelMattersRemainingTime(BreakoffObj breakoffObj){
        return breakoffApplicationDao.findPersonnelMattersRemainingTime(breakoffObj);
    }

    @Transactional
    public void updateAllByPersonnelMatters(BreakoffObj breakoffObj) {
        BreakoffApplication breakoffApplication=new BreakoffApplication();
        for (int i = 0; i <breakoffObj.getBreakoffIds().length ; i++) {
            breakoffApplication.setId(breakoffObj.getBreakoffIds()[i]);
            BreakoffApplication breakoffApplicationById = this.findBreakoffApplicationById(breakoffObj.getBreakoffIds()[i]);
            if(breakoffObj.getStatus().equals("1")){
                breakoffApplication.setAvailableTime(breakoffApplicationById.getTotalTime());
                breakoffApplication.setSaveTime(breakoffApplicationById.getTotalTime());
            }else {
                breakoffApplication.setAvailableTime("0");
                breakoffApplication.setSaveTime("0");
            }
            breakoffApplication.setStatus(breakoffObj.getStatus());
            breakoffApplication.setPersonnelTime(DateUtil.now());
            breakoffApplicationDao.updateAllByPersonnelMatters(breakoffApplication);
        }
    }
    @Transactional
    public void ClearBreakoff(){
        breakoffApplicationDao.ClearBreakoff();
    }
}
