package com.ycjd.payslip.service.perm;

import com.ycjd.payslip.dao.perm.IPermDao;
import com.ycjd.payslip.pojo.Pager;
import com.ycjd.payslip.pojo.SequenceId;
import com.ycjd.payslip.pojo.perm.Perm;
import com.ycjd.payslip.util.ObjectKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PermService {
    @Autowired
    private IPermDao permDao;
    @Autowired
    private SequenceId sequenceId;
/*-------------------------------------------------generated code start,do not update-----------------------------------------------------------*/
    @Transactional(readOnly = true)
    public Perm findPermById(String id) {
        return permDao.findPermById(id);
    }
    @Transactional
    public void savePerm(Perm perm) {
        perm.setId(sequenceId.nextId());
        permDao.savePerm(perm);
    }
    @Transactional(readOnly = true)
    public List<Perm> findPermListByCondition(Perm perm) {
        return permDao.findPermListByCondition(perm);
    }
    @Transactional(readOnly = true)
    public Perm findOnePermByCondition(Perm perm) {
        return permDao.findOnePermByCondition(perm);
    }
    @Transactional(readOnly = true)
    public long findPermCountByCondition(Perm perm) {
        return permDao.findPermCountByCondition(perm);
    }
    @Transactional
    public void updatePerm(Perm perm) {
        permDao.updatePerm(perm);
    }
    @Transactional
    public void deletePerm(String id) {
        permDao.deletePerm(id);
    }
    @Transactional
    public void deletePermByCondition(Perm perm) {
        permDao.deletePermByCondition(perm);
    }
    @Transactional
    public void batchSavePerm(List<Perm> perms){
        perms.forEach(perm -> perm.setId(sequenceId.nextId()));
        permDao.batchSavePerm(perms);
    }


    /*-------------------------------------------------generated code end,do not update-----------------------------------------------------------*/
    public List<Perm> findAllTreeMenu() {
        Perm perm = new Perm();
        Pager p=new Pager();
        p.setSortField("sortNum");
        p.setSortOrder(Pager.ASC);
        p.setPaging(false);
        perm.setPager(p);
        List<Perm> Perms = findPermListByCondition(perm);
        return ObjectKit.buildTree(Perms,"-1");

    }

    public List<Perm> findWorkTreeMenu(String id) {
        List<Perm> perms = permDao.findWorkTreeMenu(id);
        return ObjectKit.buildTree(perms,"-1");
    }

    public void batchUpdateSortNum(List<Perm> perms) {
        permDao.batchUpdateSortNum(perms);

    }
}
