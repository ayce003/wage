package com.ycjd.payslip.pojo.wage;

import com.ycjd.payslip.pojo.Pager;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
*
*
*
*/
@Data
public class WageAttributeType{

    @ApiModelProperty(value = "主键id",dataType = "String")
    private String id;
    @ApiModelProperty(value = "工资类型id",dataType = "String")
    private String wageTypeId;
    @ApiModelProperty(value = "工资属性id",dataType = "String")
    private String wageAttributeId;
    @ApiModelProperty(value = "属性排序",dataType = "String")
    private String sort;
    @ApiModelProperty(value = "学校id",dataType = "String")
    private String schoolId;
    //分页排序等
    @Transient
    @NotNull(message = "pager不能为空")
    @Valid
    private Pager pager;
    //额外字段
    @ApiModelProperty(value = "属性名",dataType = "String")
    private String attributeName;
}
