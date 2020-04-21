package com.ycjd.payslip.pojo.perm;

import com.ycjd.payslip.pojo.Pager;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.NotNull;

/**
*
*
*
*/
@Data
public class WorkRole {

    private String id;
    private String workId;//关联work id
    private String roleId;//关联role id
    //分页排序等
    @Transient
    @NotNull
    private Pager pager;
}
