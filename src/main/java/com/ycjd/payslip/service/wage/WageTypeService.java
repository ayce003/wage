package com.ycjd.payslip.service.wage;

import com.ycjd.payslip.dao.wage.IWageAttributeTypeDao;
import com.ycjd.payslip.dao.wage.IWageTypeDao;
import com.ycjd.payslip.pojo.SequenceId;
import com.ycjd.payslip.pojo.wage.WageAttributeType;
import com.ycjd.payslip.pojo.wage.WageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WageTypeService {
    @Autowired
    private IWageTypeDao wageTypeDao;
    @Autowired
    private IWageAttributeTypeDao wageAttributeTypeDao;
    @Autowired
    private SequenceId sequenceId;

    @Transactional(readOnly = true)
    public WageType findWageTypeById(String id) {
        return wageTypeDao.findWageTypeById(id);
    }
    @Transactional
    public void saveWageType(WageType wageType) {
        wageType.setId(sequenceId.nextId());
        wageTypeDao.saveWageType(wageType);
        for (int i = 0; i <wageType.getSortList().size() ; i++) {
            WageAttributeType wageAttributeType=new WageAttributeType();
            wageAttributeType.setId(sequenceId.nextId());
            wageAttributeType.setWageTypeId(wageType.getId());
            wageAttributeType.setWageAttributeId(wageType.getSortList().get(i).getId());
            wageAttributeType.setSort(wageType.getSortList().get(i).getSort());
            wageAttributeType.setSchoolId(wageType.getSchoolId());
            wageAttributeTypeDao.saveWageAttributeType(wageAttributeType);
        }
    }
    @Transactional(readOnly = true)
    public List<WageType> findWageTypeListByCondition(WageType wageType) {
        return wageTypeDao.findWageTypeListByCondition(wageType);
    }
    @Transactional(readOnly = true)
    public List<WageType> findWageTypeListByConditionNotState(WageType wageType) {
        return wageTypeDao.findWageTypeListByConditionNotState(wageType);
    }

    @Transactional(readOnly = true)
    public WageType findOneWageTypeByCondition(WageType wageType) {
        return wageTypeDao.findOneWageTypeByCondition(wageType);
    }
    @Transactional(readOnly = true)
    public long findWageTypeCountByCondition(WageType wageType) {
        return wageTypeDao.findWageTypeCountByCondition(wageType);
    }
    @Transactional
    public void updateWageType(WageType wageType) {
        wageTypeDao.updateWageType(wageType);
        wageAttributeTypeDao.deleteWageAttributeTypeByTypeId(wageType.getId());
        for (int i = 0; i <wageType.getSortList().size() ; i++) {
            WageAttributeType wageAttributeType=new WageAttributeType();
            wageAttributeType.setId(sequenceId.nextId());
            wageAttributeType.setWageTypeId(wageType.getId());
            wageAttributeType.setWageAttributeId(wageType.getSortList().get(i).getId());
            wageAttributeType.setSort(wageType.getSortList().get(i).getSort());
            wageAttributeType.setSchoolId(wageType.getSchoolId());
            wageAttributeTypeDao.saveWageAttributeType(wageAttributeType);
        }

    }
    @Transactional
    public void deleteWageType(String id) {
        wageTypeDao.deleteWageType(id);
        wageAttributeTypeDao.deleteWageAttributeTypeByTypeId(id);
    }
    @Transactional
    public void deleteWageTypeByCondition(WageType wageType) {
        wageTypeDao.deleteWageTypeByCondition(wageType);
    }
    @Transactional
    public void batchSaveWageType(List<WageType> wageTypes){
        wageTypes.forEach(wageType -> wageType.setId(sequenceId.nextId()));
        wageTypeDao.batchSaveWageType(wageTypes);
    }

    @Transactional(readOnly = true)
    public List<WageType> findWageTypeListByCondition1(WageType wageType) {
        return wageTypeDao.findWageTypeListByCondition1(wageType);
    }

    @Transactional
    public void updateWageTypeState(String id) {
        wageTypeDao.updateWageTypeState(id);
    }

    @Transactional(readOnly = true)
    public List<WageType> findWageTypeListByState(WageType wageType) {
        return wageTypeDao.findWageTypeListByState(wageType);
    }

    @Transactional(readOnly = true)
    public List<WageType> findWageAttributeListByTypeId(WageType wageType) {
        return wageTypeDao.findWageAttributeListByTypeId(wageType);
    }
}
