package com.ycjd.payslip.service.wage;

import com.ycjd.payslip.dao.wage.IWageAttributeDao;
import com.ycjd.payslip.pojo.SequenceId;
import com.ycjd.payslip.pojo.wage.WageAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WageAttributeService {
    @Autowired
    private IWageAttributeDao IWageAttributeDao;
    @Autowired
    private SequenceId sequenceId;

    @Transactional(readOnly = true)
    public WageAttribute findWageAttributeById(String id) {
        return IWageAttributeDao.findWageAttributeById(id);
    }
    @Transactional
    public void saveWageAttribute(WageAttribute wageAttribute) {
        wageAttribute.setId(sequenceId.nextId());
        IWageAttributeDao.saveWageAttribute(wageAttribute);
    }
    @Transactional(readOnly = true)
    public List<WageAttribute> findWageAttributeListByCondition(WageAttribute wageAttribute) {
        return IWageAttributeDao.findWageAttributeListByCondition(wageAttribute);
    }
    @Transactional(readOnly = true)
    public WageAttribute findOneWageAttributeByCondition(WageAttribute wageAttribute) {
        return IWageAttributeDao.findOneWageAttributeByCondition(wageAttribute);
    }
    @Transactional(readOnly = true)
    public long findWageAttributeCountByCondition(WageAttribute wageAttribute) {
        return IWageAttributeDao.findWageAttributeCountByCondition(wageAttribute);
    }
    @Transactional
    public void updateWageAttribute(WageAttribute wageAttribute) {
        IWageAttributeDao.updateWageAttribute(wageAttribute);
    }
    @Transactional
    public void deleteWageAttribute(String id) {
        IWageAttributeDao.deleteWageAttribute(id);
    }
    @Transactional
    public void deleteWageAttributeByCondition(WageAttribute wageAttribute) {
        IWageAttributeDao.deleteWageAttributeByCondition(wageAttribute);
    }
    @Transactional
    public void batchSaveWageAttribute(List<WageAttribute> wageAttributes){
        wageAttributes.forEach(wageAttribute -> wageAttribute.setId(sequenceId.nextId()));
        IWageAttributeDao.batchSaveWageAttribute(wageAttributes);
    }

}
