package com.ycjd.payslip.pojo.perm;

import com.ycjd.payslip.pojo.Pager;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;

import java.util.List;


@Data
@ApiModel("权限")
public class Perm{

    @ApiModelProperty("id联合唯一")
    private String id;
    @ApiModelProperty("权限名称")
    private String title;
    @ApiModelProperty("图标")
    private String icon;
    @ApiModelProperty("父id")
    private String parentId;
    @ApiModelProperty("路由名称,可为空")
    private String routeName;
    @ApiModelProperty("权重")
    private String sortNum;
    //分页排序等
    @Transient
    @NotNull(message = "pager不能为空")
    @Valid
    private Pager pager;

    //额外字段
    private List<Perm> children;
}
