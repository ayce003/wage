package com.ycjd.payslip.controller.perm;

import com.ycjd.payslip.pojo.ResponseJson;
import com.ycjd.payslip.pojo.perm.Perm;
import com.ycjd.payslip.pojo.perm.Role;
import com.ycjd.payslip.service.perm.PermService;
import com.ycjd.payslip.service.perm.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
@Api(value = "/role",description = "角色模块")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermService permService;

    @PostMapping("/saveRole")
    @ApiOperation(value = "保存角色对象", notes = "返回保存好的角色对象", response=Role.class)
    public ResponseJson saveRole(
            @ApiParam(value = "角色对象", required = true)
            @RequestBody Role role){
        if(role.getDefaultRoleStatus().equals("1")){
            if (checkDefaultRoleNum()){
                return new ResponseJson(false, "已存在默认角色");
            }
        }

        roleService.saveRole(role);
        return new ResponseJson();
    }

    private boolean checkDefaultRoleNum() {
        Role role1=new Role();
        role1.setDefaultRoleStatus("1");
        long count=roleService.findRoleCountByCondition(role1);
        if(count>0){
            return true;
        }
        return false;
    }

    @GetMapping("/update/findRoleById/{id}")
    @ApiOperation(value = "去更新页面,通过id查找角色", notes = "返回响应对象", response=Role.class)
    public ResponseJson findRoleById(
            @ApiParam(value = "去更新页面,需要用到的id", required = true)
            @PathVariable String id){

        Role role=roleService.findRoleById(id);
        return new ResponseJson(role);
    }

    @PostMapping("/update/updateRole")
    @ApiOperation(value = "修改角色对象", notes = "返回响应对象")
    public ResponseJson updateRole(
            @ApiParam(value = "被修改的角色对象,对象属性不为空则修改", required = true)
            @RequestBody Role role){
        if(role.getDefaultRoleStatus().equals("1")){
            if (checkDefaultRoleNum()){
                return new ResponseJson(false, "已存在默认角色");
            }
        }
        roleService.updateRole(role);
        return new ResponseJson();
    }

    @GetMapping("/look/lookRoleById/{id}")
    @ApiOperation(value = "去查看页面,通过id查找角色", notes = "返回响应对象", response=Role.class)
    public ResponseJson lookRoleById(
            @ApiParam(value = "去查看页面,需要用到的id", required = true)
            @PathVariable String id){
        Role role=roleService.findRoleById(id);
        return new ResponseJson(role);
    }

    @PostMapping("/findRolesByCondition")
    @ApiOperation(value = "根据条件查找角色", notes = "返回响应对象", response=Role.class)
    public ResponseJson findRolesByCondition(
            @ApiParam(value = "属性不为空则作为条件查询")
            @Validated
            @RequestBody Role role){
        List<Role> data=roleService.findRoleListByCondition(role);
        long count=roleService.findRoleCountByCondition(role);
        return new ResponseJson(data,count);
    }
    @PostMapping("/findOneRoleByCondition")
    @ApiOperation(value = "根据条件查找单个角色,结果必须为单条数据", notes = "没有时返回空", response=Role.class)
    public ResponseJson findOneRoleByCondition(
            @ApiParam(value = "属性不为空则作为条件查询")
            @RequestBody Role role){
        Role one=roleService.findOneRoleByCondition(role);
        return new ResponseJson(one);
    }
    @GetMapping("/deleteRole/{id}")
    @ApiOperation(value = "根据id删除", notes = "返回响应对象")
    public ResponseJson deleteRole(
            @ApiParam(value = "被删除记录的id", required = true)
            @PathVariable String id){
        roleService.deleteRole(id);
        return new ResponseJson();
    }


    @PostMapping("/findRoleListByCondition")
    @ApiOperation(value = "根据条件查找角色列表", notes = "返回响应对象,不包含总条数", response=Role.class)
    public ResponseJson findRoleListByCondition(
            @ApiParam(value = "属性不为空则作为条件查询")
            @Validated
            @RequestBody Role role){
        List<Role> data=roleService.findRoleListByCondition(role);
        return new ResponseJson(data);
    }

    @GetMapping("/findPermsByRoleId/{roleId}")
    @ApiOperation(value = "根据角色id获取权限树", notes = "返回响应对象")
    public ResponseJson findSysPermsByRoleId(@PathVariable("roleId") String roleId){
        List<Perm> perms=permService.findAllTreeMenu();
        List<String> checked=roleService.findPermChecked(roleId);
        return new ResponseJson(perms,checked);
    }

    @PostMapping("/delsertRolePerms")
    @ApiOperation(value = "先删除角色权限,在增加角色权限", notes = "返回响应对象")
    public void delsertRolePerms(@RequestBody Map<String, String> map){
        roleService.delsertRolePerms(map);
    }


    @GetMapping("/findRolesByAdminId/{id}")
    @ApiOperation(value = "根据adminId获取角色列表", notes = "返回响应对象")
    public ResponseJson findRolesByAdminId(@PathVariable("id") String id){
        List<Role> roles= roleService.findRoleListByCondition(new Role());
        List<String> checkedIds= roleService.findCheckedRoloIdsByAdminId(id);
        return new ResponseJson(roles,checkedIds);
    }



    @PostMapping("/delsertAdminRoles")
    public ResponseJson delsertAdminRoles(@RequestBody Map<String,String> map){
        roleService.delsertAdminRoles(map);
        return new ResponseJson();
    }
}
