package com.ycjd.payslip.pojo.overtime;

import lombok.Data;

@Data
public class OvertimeSort {
    private String workerId;//员工id
    private Integer sort;//审核权重
}
