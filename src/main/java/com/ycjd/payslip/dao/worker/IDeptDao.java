package com.ycjd.payslip.dao.worker;

import java.util.List;

import com.ycjd.payslip.pojo.worker.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IDeptDao {
    List<Dept> findDeptListByCondition(Dept dept);

    long findDeptCountByCondition(Dept dept);

    Dept findOneDeptByCondition(Dept dept);

    Dept findDeptById(@Param("id") String id);

    void saveDept(Dept dept);

    void updateDept(Dept dept);

    void deleteDept(@Param("id") String id);

    void deleteDeptByCondition(Dept dept);

    void batchSaveDept(List<Dept> depts);

    List<Dept> findDeptByParentId(@Param("parentId")String parentId);
}
