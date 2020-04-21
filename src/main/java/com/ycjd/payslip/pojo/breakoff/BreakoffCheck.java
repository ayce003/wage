package com.ycjd.payslip.pojo.breakoff;

import com.ycjd.payslip.pojo.Pager;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;


@Data
@ApiModel("")
public class BreakoffCheck{

    @ApiModelProperty("主键id")
    private String id;
    @ApiModelProperty("审核人id")
    private String auditorId;
    @ApiModelProperty("审核状态(0:待审核1：通过2:否决)")
    private String status;
    @ApiModelProperty("调休申请id")
    private String breakoffId;
    @ApiModelProperty("审核权重")
    private Integer sort;
    @ApiModelProperty("下一审核权重")
    private Integer nextSort;
    @ApiModelProperty("审核时间")
    private String auditTime;
    @ApiModelProperty("理由")
    private String remarks;
    @ApiModelProperty("通过时间")
    private String throughTime;
    //分页排序等
    @Transient
    @NotNull(message = "pager不能为空")
    @Valid
    private Pager pager;

    //额外字段
    private String myTime;//审核通过时长
    private String totalTime;
    private String auditorRemarks;
    private String auditorName;
    private String auditorStatus;
    private String applicantId;
}
