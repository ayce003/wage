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
public class WageAttribute {

    @ApiModelProperty(value = "主键id",dataType = "String")
    private String id;
    @ApiModelProperty(value = "属性名称",dataType = "String")
    private String attributeName;
    @ApiModelProperty(value = "更新时间",dataType = "String")
    private String updateTime;
    //分页排序等
    @Transient
    @NotNull(message = "pager不能为空")
    @Valid
    private Pager pager;
}
