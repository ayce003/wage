package com.ycjd.payslip.service.wage;

import com.ycjd.payslip.dao.wage.IWageValueDao;
import com.ycjd.payslip.pojo.SequenceId;
import com.ycjd.payslip.pojo.wage.WageValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WageValueService {
    @Autowired
    private IWageValueDao wageValueDao;
    @Autowired
    private SequenceId sequenceId;

    @Transactional(readOnly = true)
    public WageValue findWageValueById(String id) {
        return wageValueDao.findWageValueById(id);
    }
    @Transactional
    public void saveWageValue(WageValue wageValue) {
        wageValue.setId(sequenceId.nextId());
        wageValueDao.saveWageValue(wageValue);
    }
    @Transactional(readOnly = true)
    public List<WageValue> findWageValueListByCondition(WageValue wageValue) {
        return wageValueDao.findWageValueListByCondition(wageValue);
    }
    @Transactional(readOnly = true)
    public WageValue findOneWageValueByCondition(WageValue wageValue) {
        return wageValueDao.findOneWageValueByCondition(wageValue);
    }
    @Transactional(readOnly = true)
    public long findWageValueCountByCondition(WageValue wageValue) {
        return wageValueDao.findWageValueCountByCondition(wageValue);
    }
    @Transactional
    public void updateWageValue(WageValue wageValue) {
        wageValueDao.updateWageValue(wageValue);
    }
    @Transactional
    public void deleteWageValue(String id) {
        wageValueDao.deleteWageValue(id);
    }
    @Transactional
    public void deleteWageValueByCondition(WageValue wageValue) {
        wageValueDao.deleteWageValueByCondition(wageValue);
    }
    @Transactional
    public void batchSaveWageValue(List<WageValue> wageValues){
        wageValues.forEach(wageValue -> wageValue.setId(sequenceId.nextId()));
        wageValueDao.batchSaveWageValue(wageValues);
    }

}
