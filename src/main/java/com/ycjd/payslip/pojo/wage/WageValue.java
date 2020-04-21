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
public class WageValue{

    @ApiModelProperty(value = "主键id",dataType = "String")
    private String id;
    @ApiModelProperty(value = "属性id",dataType = "String")
    private String wageAttributeId;
    @ApiModelProperty(value = "属性名称下的值",dataType = "String")
    private String valueSize;
    @ApiModelProperty(value = "记录id",dataType = "String")
    private String recordId;
    @ApiModelProperty(value = "盐",dataType = "String")
    private String salt;

    //分页排序等
    @Transient
    @NotNull(message = "pager不能为空")
    @Valid
    private Pager pager;
}
