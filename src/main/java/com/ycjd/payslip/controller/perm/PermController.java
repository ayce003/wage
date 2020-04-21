package com.ycjd.payslip.controller.perm;

import com.ycjd.payslip.pojo.ResponseJson;
import com.ycjd.payslip.pojo.perm.Perm;
import com.ycjd.payslip.service.perm.PermService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ycjd.payslip.interceptor.LoginInterceptor.myId;

@RestController
@RequestMapping("/perm")
@Api(value = "/perm",description = "权限模块")
public class PermController {
    @Autowired
    private PermService permService;

    @GetMapping("/findPermById/{id}")
    @ApiOperation(value = "通过id查找权限", notes = "返回权限对象")
    public ResponseJson findPermById(
            @ApiParam(value = "需要用到的id", required = true)
            @PathVariable String id){

        Perm perm=permService.findPermById(id);
        return new ResponseJson(perm);
    }

    @PostMapping("/savePerm")
    @ApiOperation(value = "保存权限", notes = "返回权限对象")
    public ResponseJson savePerm(
            @ApiParam(value = "权限对象", required = true)
            @RequestBody Perm perm){
        if(perm.getParentId()==null){
            perm.setParentId("-1");
        }
       permService.savePerm(perm);
        return new ResponseJson(perm);
    }


    @GetMapping("/findAllTreeMenu")
    public ResponseJson findAllTreeMenu(){
        List<Perm> perms=permService.findAllTreeMenu();
        return new ResponseJson(perms);
    }


    @PostMapping("/findPermListByCondition")
    @ApiOperation(value = "根据条件查找权限列表", notes = "返回权限列表")
    public List<Perm> findPermListByCondition(
            @ApiParam(value = "权限对象")
            @RequestBody Perm perm){
        return permService.findPermListByCondition(perm);
    }
    @PostMapping("/findPermCountByCondition")
    @ApiOperation(value = "根据条件查找权限列表个数", notes = "返回权限总个数")
    public long findPermCountByCondition(
            @ApiParam(value = "权限对象")
            @RequestBody Perm perm){
        return permService.findPermCountByCondition(perm);
    }

    @PostMapping("/updatePerm")
    @ApiOperation(value = "修改权限", notes = "权限对象必传")
    public void updatePerm(
            @ApiParam(value = "权限对象,对象属性不为空则修改", required = true)
            @RequestBody Perm perm){
        permService.updatePerm(perm);
    }

    @GetMapping("/deletePerm/{id}")
    @ApiOperation(value = "通过id删除权限")
    public void deletePerm(
            @ApiParam(value = "权限对象", required = true)
            @PathVariable String id){
        permService.deletePerm(id);
    }
    @PostMapping("/deletePermByCondition")
    @ApiOperation(value = "根据条件删除权限")
    public void deletePermByCondition(
            @ApiParam(value = "权限对象")
            @RequestBody Perm perm){
        permService.deletePermByCondition(perm);
    }
    @PostMapping("/findOnePermByCondition")
    @ApiOperation(value = "根据条件查找单个权限,结果必须为单条数据", notes = "返回单个权限,没有时为空")
    public Perm findOnePermByCondition(
            @ApiParam(value = "权限对象")
            @RequestBody Perm perm){
        return permService.findOnePermByCondition(perm);
    }



    @GetMapping("/findWorkTreeMenu")
    @ApiOperation(value = "获取登录用户的权限树")
    public ResponseJson findWorkTreeMenu() {
        List<Perm> perms = permService.findWorkTreeMenu(myId());
        return new ResponseJson(perms);
    }

    @PostMapping("/batchUpdateSortNum")
    @ApiOperation(value = "修改权限树的权重")
    public void batchUpdateSortNum(@RequestBody List<Perm> perms){
        permService.batchUpdateSortNum(perms);
    }
}
