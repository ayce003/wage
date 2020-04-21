package com.ycjd.payslip.dao.wage;

import com.ycjd.payslip.pojo.wage.WageType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IWageTypeDao {
    List<WageType> findWageTypeListByCondition(WageType wageType);

    List<WageType> findWageTypeListByConditionNotState(WageType wageType);

    WageType findOneWageTypeByCondition(WageType wageType);

    long findWageTypeCountByCondition(WageType wageType);

    WageType findWageTypeById(@Param("id") String id);

    void saveWageType(WageType wageType);

    void updateWageType(WageType wageType);

    void deleteWageType(@Param("id") String id);

    void deleteWageTypeByCondition(WageType wageType);

    void batchSaveWageType(List<WageType> wageTypes);

    List<WageType> findWageTypeListByCondition1(WageType wageType);

    void updateWageTypeState(@Param("id") String id);

    List<WageType> findWageTypeListByState(WageType wageType);

    List<WageType> findWageAttributeListByTypeId(WageType wageType);
}
