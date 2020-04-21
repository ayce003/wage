package com.ycjd.payslip.pojo.worker;

import com.ycjd.payslip.pojo.Pager;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class Dept implements Serializable {

    private String id;
    private String deptName;//部门名称
    private String parentId;//父节点id
    private Integer level;//树级别
    private Integer leaf;//是否是叶子节点(1是 2.否）
    private String path;//path
    private Integer sort;//排序
    private String createTime;//创建时间
    private String updateTime;//修改时间
    //分页排序等
    private Pager pager;

    //额外字段
    private List<Dept> children;
}
