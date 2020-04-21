package com.ycjd.payslip.controller.worker;

import com.ycjd.payslip.pojo.Pager;
import com.ycjd.payslip.pojo.ResponseJson;
import com.ycjd.payslip.pojo.worker.Dept;
import com.ycjd.payslip.service.worker.DeptService;
import com.ycjd.payslip.util.ObjectKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dept")
public class DeptController {
    @Autowired
    private DeptService deptService;

    @PostMapping("/saveDept")
    public ResponseJson saveDept(
            @RequestBody Dept dept){
        deptService.saveDept(dept);
        return new ResponseJson();
    }

    @GetMapping("/update/findDeptById/{id}")
    public ResponseJson findDeptById(
            @PathVariable String id){
        Dept dept=deptService.findDeptById(id);
        return new ResponseJson(dept);
    }

    @PostMapping("/update/updateDept")
    public ResponseJson updateDept(
            @RequestBody Dept dept){
        deptService.updateDept(dept);
        return new ResponseJson();
    }

    @GetMapping("/look/lookDeptById/{id}")
    public ResponseJson lookDeptById(
            @PathVariable String id){
        Dept dept=deptService.findDeptById(id);
        return new ResponseJson(dept);
    }

    @PostMapping("/findDeptsByCondition")
    public ResponseJson findDeptsByCondition(
            @RequestBody Dept dept){
        List<Dept> data=deptService.findDeptListByCondition(dept);
        long count = deptService.findDeptCountByCondition(dept);
        return new ResponseJson(data,count);
    }
    @PostMapping("/findOneDeptByCondition")
    public ResponseJson findOneDeptByCondition(
            @RequestBody Dept dept){
        Dept one=deptService.findOneDeptByCondition(dept);
        return new ResponseJson(one);
    }
    @GetMapping("/deleteDept/{id}")
    public ResponseJson deleteDept(
            @PathVariable String id){
        deptService.deleteDept(id);
        return new ResponseJson();
    }


    @PostMapping("/findDeptListByCondition")
    public ResponseJson findDeptListByCondition(
            @RequestBody Dept dept){
        List<Dept> data=deptService.findDeptListByCondition(dept);
        return new ResponseJson(data);
    }


    @GetMapping("/findDeptsTreeByCondition")
    public ResponseJson findDeptsTreeByCondition(){
        Dept dept = new Dept();
        Pager pager = new Pager();
        pager.setSortField("sort");
        pager.setSortOrder("desc");
        pager.setPaging(false);
        dept.setPager(pager);
        List<Dept> data=deptService.findDeptListByCondition(dept);
        List<Dept> treeData = ObjectKit.buildTree(data, "-1");
        return new ResponseJson(treeData);
    }



}
