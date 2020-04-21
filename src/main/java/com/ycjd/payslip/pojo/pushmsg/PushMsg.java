package com.ycjd.payslip.pojo.pushmsg;

import com.ycjd.payslip.pojo.Pager;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;


@Data
@ApiModel("")
public class PushMsg{

    @ApiModelProperty("主键id")
    private String id;
    @ApiModelProperty("申请人id")
    private String applicantId;
    @ApiModelProperty("信息")
    private String message;
    //分页排序等
    @Transient
    @NotNull(message = "pager不能为空")
    @Valid
    private Pager pager;
}
