package com.ycjd.payslip.dao.wage;

import com.ycjd.payslip.pojo.wage.WageValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IWageValueDao {
    List<WageValue> findWageValueListByCondition(WageValue wageValue);

    WageValue findOneWageValueByCondition(WageValue wageValue);

    long findWageValueCountByCondition(WageValue wageValue);

    WageValue findWageValueById(@Param("id") String id);

    void saveWageValue(WageValue wageValue);

    void updateWageValue(WageValue wageValue);

    void deleteWageValue(@Param("id") String id);

    void deleteWageValueByCondition(WageValue wageValue);

    void batchSaveWageValue(List<WageValue> wageValues);

    void deleteWageValueByRecordId(@Param("id") String id);
}
