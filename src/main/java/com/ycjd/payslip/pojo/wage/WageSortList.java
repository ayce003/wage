package com.ycjd.payslip.pojo.wage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WageSortList {
    @ApiModelProperty(value = "Id",dataType = "String")
    private String id;
    @ApiModelProperty(value = "属性名称",dataType = "String")
    private String attributeName;
    @ApiModelProperty(value = "权重",dataType = "String")
    private String sort;
    @ApiModelProperty(value = "工资类型id",dataType = "String")
    private String wageTypeId;
    @ApiModelProperty(value = "学校id",dataType = "String")
    private String schoolId;



}
