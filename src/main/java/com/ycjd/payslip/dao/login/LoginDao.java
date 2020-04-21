package com.ycjd.payslip.dao.login;


import com.ycjd.payslip.pojo.worker.Worker;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LoginDao {
    List<Worker> findLoginListByCondition(Worker worker);
}
