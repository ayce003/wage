package com.ycjd.payslip.dao.worker;

import com.ycjd.payslip.pojo.worker.Worker;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface IWorkerDao {
    List<Worker> findWorkerListByCondition(Worker worker);

    long findWorkerCountByCondition(Worker worker);

    Worker findOneWorkerByCondition(Worker worker);

    Worker findWorkerById(@Param("id") String id);

    void saveWorker(Worker worker);

    void updateWorker(Worker worker);

    void deleteWorker(@Param("id") String id);

    void deleteWorkerByCondition(Worker worker);

    void batchSaveWorker(List<Worker> workers);

    List<Worker> findWorkerListByCondition2(Worker worker);

    //导出查询
    List<Map<String, Object>> findWorkerListForExportWorkerByCondition(Worker worker);
}
