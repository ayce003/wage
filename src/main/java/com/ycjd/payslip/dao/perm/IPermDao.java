package com.ycjd.payslip.dao.perm;

import java.util.List;

import com.ycjd.payslip.pojo.perm.Perm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IPermDao {
/*-------------------------------------------------generated code start,do not update-----------------------------------------------------------*/
    List<Perm> findPermListByCondition(Perm perm);

    long findPermCountByCondition(Perm perm);

    Perm findOnePermByCondition(Perm perm);

    Perm findPermById(@Param("id") String id);

    void savePerm(Perm perm);

    void updatePerm(Perm perm);

    void deletePerm(@Param("id") String id);

    void deletePermByCondition(Perm perm);

    void batchSavePerm(List<Perm> perms);


    /*-------------------------------------------------generated code end,do not update-----------------------------------------------------------*/

    List<Perm> findWorkTreeMenu(@Param("workId")String id);

    void batchUpdateSortNum(List<Perm> perms);
}
