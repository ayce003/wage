package com.ycjd.payslip.pojo.worker;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.ycjd.payslip.pojo.Pager;
import lombok.Data;

import javax.validation.constraints.Pattern;


@Data
public class Worker{
    private String username;
    private String id;
    private String password;
    @Excel(name="员工工号")
    private String workNumber;
    @Excel(name="员工姓名")
    private String name;
    @Excel(name="性别")
    private String sex;
    @Excel(name="年龄")
    private String age;
    private String imgUrl;
    private String postId;
    @Excel(name="邮箱")
    @Pattern(regexp = "^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}$",message = "邮箱格式不正确")
    private String email;
    @Excel(name="电话号码")
    @Pattern(regexp = "^1\\d{10}$",message = "手机号码必须为11位")
    private String tel;
    private String roleType;//角色类型(1.管理员,2.普通员工)
    private String roleName;
    private String departmentId;
    private Integer status;//启用状态(0禁用,1启用)
    private Boolean statusType;//禁用/启用
    private String createTime;
    private String updateTime;
    private Integer del;//删除状态(0:未删除,1:已删除)
    //分页排序等
    private Pager pager;
    @Excel(name="部门")
    private String deptName;
    @Excel(name="岗位")
    private String postName;

    private String[] deptPath;//部门路径

    private String newPassword;//新密码
    private String code;//验证码
}
