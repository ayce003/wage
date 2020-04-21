package com.ycjd.payslip.pojo.perm;

import com.ycjd.payslip.pojo.Pager;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;


@Data
@ApiModel("角色")
public class Role{

    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("角色名称")
    private String title;
    @ApiModelProperty("创建时间")
    private String createTime;
    @ApiModelProperty("默认角色状态")
    private String defaultRoleStatus;
    //分页排序等
    @Transient
    @NotNull(message = "pager不能为空")
    @Valid
    private Pager pager;
}
