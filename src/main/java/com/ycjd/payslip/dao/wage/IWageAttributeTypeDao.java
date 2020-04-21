package com.ycjd.payslip.dao.wage;

import com.ycjd.payslip.pojo.wage.WageAttributeType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IWageAttributeTypeDao {
    List<WageAttributeType> findWageAttributeTypeListByCondition(WageAttributeType wageAttributeType);

    WageAttributeType findOneWageAttributeTypeByCondition(WageAttributeType wageAttributeType);

    long findWageAttributeTypeCountByCondition(WageAttributeType wageAttributeType);

    WageAttributeType findWageAttributeTypeById(@Param("id") String id);

    void saveWageAttributeType(WageAttributeType wageAttributeType);

    void updateWageAttributeType(WageAttributeType wageAttributeType);

    void deleteWageAttributeType(@Param("id") String id);

    void deleteWageAttributeTypeByCondition(WageAttributeType wageAttributeType);

    void batchSaveWageAttributeType(List<WageAttributeType> wageAttributeTypes);

    void deleteWageAttributeTypeByTypeId(@Param("id") String id);

    List<WageAttributeType> findWageAttributeTypeByTypeId(@Param("id") String id);

}
