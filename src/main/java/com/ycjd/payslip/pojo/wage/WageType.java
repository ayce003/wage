package com.ycjd.payslip.pojo.wage;

import com.ycjd.payslip.pojo.Pager;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
*
*
*
*/
@Data
public class WageType{

    @ApiModelProperty(value = "主键id",dataType = "String")
    private String id;
    @ApiModelProperty(value = "新增时间",dataType = "String")
    private String createTime;
    @ApiModelProperty(value = "类型名称",dataType = "String")
    private String typeName;
    @ApiModelProperty(value = "学校id",dataType = "String")
    private String schoolId;
    @ApiModelProperty(value = "使用状态 0未使用，1使用",dataType = "String")
    private String state;
    //分页排序等
    @Transient
    @NotNull(message = "pager不能为空")
    @Valid
    private Pager pager;

    //额外字段
    @ApiModelProperty(value = "权重列表",dataType = "String")
    private List<WageSortList> sortList;
    @ApiModelProperty(value = "包含属性",dataType = "String")
    private String inclusionproperty;
    @ApiModelProperty(value = "属性id",dataType = "String")
    private String wageAttributeId;
    @ApiModelProperty(value = "属性名",dataType = "String")
    private String attributeName;
    @ApiModelProperty(value = "权重",dataType = "String")
    private String sort;
}
