package com.ycjd.payslip.pojo.overtime;

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
public class OvertimeApplication{

    @ApiModelProperty("主键id")
    private String id;
    @ApiModelProperty("申请人id")
    private String applicantId;
    @ApiModelProperty("加班事由")
    private String overtimeReason;
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;
    @ApiModelProperty("总时长")
    private String totalTime;
    @ApiModelProperty("当前进度")
    private Integer progress;
    @ApiModelProperty("生效时间（审核通过时长）")
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
    private List<OvertimeSort> overtimeSortList;
    private String auditors;//审核人
    private String auditorStatuses;//审核人状态
    private String myTime;//加班总时长
    private String rangeTime[];//加班时间数组
    private String name;//申请人姓名
    private String auditorId;//审核人Id
    private String auditorName;//审核人姓名
    private Integer nextSort;
    private Integer sort;
    private List<OvertimeCheck> overtimeCheckList;
    private String auditorRemarks;
    private String  personnelRemarks;
    private List<String> ids;
    private String throughTime;
    private String applicantName;//申请人姓名
  /*private String days;//天数
    private String num;//号*/
   private String[] imgPaths;//图片路径（可多张）
}
