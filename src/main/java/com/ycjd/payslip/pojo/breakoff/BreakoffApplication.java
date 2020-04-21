package com.ycjd.payslip.pojo.breakoff;

import com.ycjd.payslip.pojo.Pager;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;

import java.util.List;


@Data
@ApiModel("")
public class BreakoffApplication{

    @ApiModelProperty("主键id")
    private String id;
    @ApiModelProperty("申请人id")
    private String applicantId;
    @ApiModelProperty("调休事由")
    private String breakoffReason;
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;
    @ApiModelProperty("调休总时长")
    private String totalTime;
    @ApiModelProperty("审核进度")
    private Integer progress;
    @ApiModelProperty("生效时间(审核通过时长)")
    private String availableTime;
    @ApiModelProperty("最终审核状态")
    private String status;
    @ApiModelProperty("删除状态(0:未删除,1:已删除)")
    private String del;
    @ApiModelProperty("理由")
    private String remarks;
    @ApiModelProperty("创建时间")
    private String createTime;
    @ApiModelProperty("人事审核时间")
    private String personnelTime;
    @ApiModelProperty("记录时间")
    private String saveTime;
    @ApiModelProperty("图片路径")
    private String imgUrl;
    //分页排序等
    @Transient
    @NotNull(message = "pager不能为空")
    @Valid
    private Pager pager;

    //额外字段
    private String myTime;//调休总时长
    private String rangeTime[];//调休时间数组
    private List<BreakoffSort> breakoffSortList;
    private String auditors;//审核人
    private String auditorStatuses;//审核人状态
    private Integer nextSort;
    private String auditorId;//审核人Id
    private String auditorName;//审核人姓名
    private String name;//申请人姓名
    private Integer sort;
    private List<BreakoffCheck> breakoffCheckList;
    private String auditorRemarks;
    private String  personnelRemarks;
    private List<String> ids;
    private String throughTime;
    private String applicantName;
    private String[] imgPaths;//图片路径（可多张）


}
