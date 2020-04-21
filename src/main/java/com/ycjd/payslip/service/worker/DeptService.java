package com.ycjd.payslip.service.worker;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.ycjd.payslip.dao.worker.IDeptDao;
import com.ycjd.payslip.pojo.Constant;
import com.ycjd.payslip.pojo.SequenceId;
import com.ycjd.payslip.pojo.worker.Dept;
import com.ycjd.payslip.pojo.worker.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeptService {
    @Autowired
    private IDeptDao deptDao;
    @Autowired
    private SequenceId sequenceId;
    @Autowired
    private WorkerService workerService;

    @CreateCache(name= Constant.Redis.DEPT_CACHE)
    private Cache<String,Dept> deptCache;

    @Transactional(readOnly = true)
    public Dept findDeptById(String id) {
        return deptDao.findDeptById(id);
    }
    @Transactional(readOnly = true)
    public List<Dept> findDeptByParentId(String parentId) {
        return deptDao.findDeptByParentId(parentId);
    }
    @Transactional(readOnly = true)
    public List<Dept> findDeptListByCondition(Dept dept) {
        return deptDao.findDeptListByCondition(dept);
    }
    @Transactional(readOnly = true)
    public Dept findOneDeptByCondition(Dept dept) {
        return deptDao.findOneDeptByCondition(dept);
    }
    @Transactional(readOnly = true)
    public long findDeptCountByCondition(Dept dept) {
        return deptDao.findDeptCountByCondition(dept);
    }
    @Transactional
    public void updateDept(Dept dept) {
        deptDao.updateDept(dept);
        deptCache.put(dept.getId(),dept);
    }

    @Transactional
    public void deleteDeptByCondition(Dept dept) {
        deptDao.deleteDeptByCondition(dept);
    }
    @Transactional
    public void batchSaveDept(List<Dept> depts){
        depts.forEach(dept -> dept.setId(sequenceId.nextId()));
        deptDao.batchSaveDept(depts);
    }


    @Transactional
    public void saveDept(Dept dept) {
        String sequId = sequenceId.nextId();

        dept.setId(sequId);

        String parentId = dept.getParentId();

        if(parentId==null||"-1".equals(parentId)){
            //根部门存储
            dept.setParentId("-1");
            dept.setLevel(1);
            dept.setLeaf(2);
            dept.setPath(sequId+";");
            deptDao.saveDept(dept);
            deptCache.put(sequId,dept);
        }else {

            Dept deptByParentId = this.findDeptById(parentId);

            if(deptByParentId!=null){
                dept.setLevel(deptByParentId.getLevel().intValue()+1);
                dept.setLeaf(1);
                dept.setPath(deptByParentId.getPath()+sequId+";");
                deptDao.saveDept(dept);
                deptCache.put(sequId,dept);
            }
        }

    }



    @Transactional
    public void deleteDept(String id) {
        deptDao.deleteDept(id);
        deptCache.remove(id);
        //修改员工信息的部门展示
        Worker w = new Worker();
        w.setDepartmentId(id);
        List<Worker> workerList = workerService.findWorkerListByCondition(w);
        if(workerList!=null&&workerList.size()>0){
            workerList.forEach(worker -> {
                worker.setDepartmentId("");
                workerService.updateWorker(worker);
            });

        }
        this.deleteDeptByPIdRecursive(id);
    }

    @Transactional
    public void deleteDeptByPIdRecursive(String pId) {
        List<Dept> deptList = deptDao.findDeptByParentId(pId);
        if(deptList!=null&&deptList.size()>0){
            for (Dept dept : deptList) {
                deptDao.deleteDept(dept.getId());
                deptCache.remove(dept.getId());

                //修改员工信息的部门展示
                Worker w = new Worker();
                w.setDepartmentId(dept.getId());
                List<Worker> workerList = workerService.findWorkerListByCondition(w);
                if(workerList!=null&&workerList.size()>0){
                    workerList.forEach(worker -> {
                        worker.setDepartmentId("");
                        workerService.updateWorker(worker);
                    });

                }

                this.deleteDeptByPIdRecursive(dept.getId());
            }

        }

    }




}
