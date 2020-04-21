package com.ycjd.payslip.pojo.breakoff;

import com.ycjd.payslip.pojo.Pager;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class BreakoffObj {
    @ApiModelProperty("申请人id")
    private String applicantId;
    private String auditorId;//审核人id
    private String name;//姓名查询  （人事审批页面用到）
    private String status;//审核状态
    private String breakoffIds[];//调休申请id数组
    private Integer nextSorts[];
    private String auditTime;
    private String auditTimes[];
    private String workerId;//员工id
    private String startTime;//开始时间
    private String endTime;//结束时间
    private String departmentId;//部门
    @Transient
    @NotNull(message = "pager不能为空")
    @Valid
    private Pager pager;
    private String rangeTime[];//调休时间数组
    private String applicantIds[];
    private String auditorNames[];
}
