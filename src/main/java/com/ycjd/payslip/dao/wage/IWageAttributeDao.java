package com.ycjd.payslip.dao.wage;

import com.ycjd.payslip.pojo.wage.WageAttribute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IWageAttributeDao {
    List<WageAttribute> findWageAttributeListByCondition(WageAttribute wageAttribute);

    WageAttribute findOneWageAttributeByCondition(WageAttribute wageAttribute);

    long findWageAttributeCountByCondition(WageAttribute wageAttribute);

    WageAttribute findWageAttributeById(@Param("id") String id);

    void saveWageAttribute(WageAttribute wageAttribute);

    void updateWageAttribute(WageAttribute wageAttribute);

    void deleteWageAttribute(@Param("id") String id);

    void deleteWageAttributeByCondition(WageAttribute wageAttribute);

    void batchSaveWageAttribute(List<WageAttribute> wageAttributes);
}
