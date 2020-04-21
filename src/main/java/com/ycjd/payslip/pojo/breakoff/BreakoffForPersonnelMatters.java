package com.ycjd.payslip.pojo.breakoff;

import lombok.Data;

@Data
public class BreakoffForPersonnelMatters {
    private String id;
    private String name;//申请人姓名
    private String deptName;//部门名称
    private String totalUnaudited;//未审核总数
    private String departmentId;//部门id
}
