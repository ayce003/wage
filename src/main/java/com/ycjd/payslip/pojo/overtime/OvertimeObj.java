package com.ycjd.payslip.pojo.overtime;

import com.ycjd.payslip.pojo.Pager;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class OvertimeObj {
    private String auditorId;//审核人id
    private String status;//审核状态
    private String name;//姓名查询  （人事审批页面用到）
    private String startTime;//开始时间
    private String endTime;//结束时间
    private String department;//部门
    private String departmentId;//部门
    private String workerId;//员工id
    private String overtimeIds[];//加班申请id数组
    private Integer nextSorts[];
    //分页排序等
    @Transient
    @NotNull(message = "pager不能为空")
    @Valid
    private Pager pager;
    private String rangeTime[];//加班时间数组
    private String applicantIds[];
    private String auditorNames[];
}
