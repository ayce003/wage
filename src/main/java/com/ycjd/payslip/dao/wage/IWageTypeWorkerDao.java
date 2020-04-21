package com.ycjd.payslip.dao.wage;

import com.ycjd.payslip.pojo.wage.WageTypeWorker;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface IWageTypeWorkerDao {
    List<WageTypeWorker> findWageTypeWorkerListByCondition(WageTypeWorker wageTypeWorker);

    List<WageTypeWorker> findWageTypeWorkerListByWorkNum(WageTypeWorker wageTypeWorker);

    WageTypeWorker findOneWageTypeWorkerByCondition(WageTypeWorker wageTypeWorker);

    long findWageTypeWorkerCountByCondition(WageTypeWorker wageTypeWorker);

    WageTypeWorker findWageTypeWorkerById(@Param("id") String id);

    void saveWageTypeWorker(WageTypeWorker wageTypeWorker);

    void updateWageTypeWorker(WageTypeWorker wageTypeWorker);

    void deleteWageTypeWorker(@Param("id") String id);

    void deleteWageTypeWorkerByCondition(WageTypeWorker wageTypeWorker);

    void batchSaveWageTypeWorker(List<WageTypeWorker> wageTypeWorkers);

    List<Map<String, Object>> findWageValueByTypeId(Map<String, Object> WageTypeWorkerMap);

    List<Map<String, Object>> findWageValueByRecordAndTypeId(Map<String, Object> WageTypeWorkerMap);

    List<Map<String, Object>> findWageValueByEamilAndTypeId(Map<String, Object> WageTypeWorkerMap);

    List<Map<String, Object>> findWageValueByTelAndTypeId(Map<String, Object> WageTypeWorkerMap);

    List<WageTypeWorker> findWageAttributeListByRecordId(WageTypeWorker wageTypeWorker);

    List<Map<String, Object>> findWageValueByRecordId(Map<String, Object> wageTypeRecordMap);

    long findWageValueByTypeIdCount(WageTypeWorker wageTypeWorker);

    long findWageValueByWorkerIdCount(WageTypeWorker wageTypeWorker);

    WageTypeWorker findWageworkerIdByRecordId(WageTypeWorker wageTypeWorker);

    List<WageTypeWorker> findWageAttributeListByWorkerId(WageTypeWorker wageTypeWorker);

    List<Map<String, String>> findWageValueByWorkerId(Map<String,Object> WageTypeWorkerMap);

    List<WageTypeWorker> findWageAttributeNameByWorkerId(WageTypeWorker wageTypeWorker);
}
