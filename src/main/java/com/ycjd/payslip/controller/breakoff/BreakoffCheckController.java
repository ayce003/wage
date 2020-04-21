package com.ycjd.payslip.controller.breakoff;

import com.ycjd.payslip.pojo.ResponseJson;
import com.ycjd.payslip.pojo.breakoff.BreakoffApplication;
import com.ycjd.payslip.pojo.breakoff.BreakoffCheck;
import com.ycjd.payslip.pojo.breakoff.BreakoffObj;
import com.ycjd.payslip.service.breakoff.BreakoffApplicationService;
import com.ycjd.payslip.service.breakoff.BreakoffCheckService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/breakoffCheck")
@Api(value = "/breakoffCheck",description = "模块")
public class BreakoffCheckController {
    @Autowired
    private BreakoffCheckService breakoffCheckService;
    @Autowired
    private BreakoffApplicationService breakoffApplicationService;

    @PostMapping("/saveBreakoffCheck")
    @ApiOperation(value = "保存对象", notes = "返回保存好的对象", response= BreakoffCheck.class)
    public ResponseJson saveBreakoffCheck(
            @ApiParam(value = "对象", required = true)
            @RequestBody BreakoffCheck breakoffCheck){
        breakoffCheckService.saveBreakoffCheck(breakoffCheck);
        return new ResponseJson();
    }

    @GetMapping("/update/findBreakoffCheckById/{id}")
    @ApiOperation(value = "去更新页面,通过id查找", notes = "返回响应对象", response=BreakoffCheck.class)
    public ResponseJson findBreakoffCheckById(
            @ApiParam(value = "去更新页面,需要用到的id", required = true)
            @PathVariable String id){
        BreakoffCheck breakoffCheck=breakoffCheckService.findBreakoffCheckById(id);
        return new ResponseJson(breakoffCheck);
    }

    @PostMapping("/update/updateBreakoffCheck")
    @ApiOperation(value = "修改对象", notes = "返回响应对象")
    public ResponseJson updateBreakoffCheck(
            @ApiParam(value = "被修改的对象,对象属性不为空则修改", required = true)
            @RequestBody BreakoffCheck breakoffCheck){
        breakoffCheckService.updateBreakoffCheck(breakoffCheck);
        return new ResponseJson();
    }

    @PostMapping("/update/updateBreakoffCheckByAuditor")
    @ApiOperation(value = "修改对象", notes = "返回响应对象")
    public ResponseJson updateBreakoffCheckByAuditor(
            @ApiParam(value = "被修改的对象,对象属性不为空则修改", required = true)
            @RequestBody BreakoffCheck breakoffCheck) throws IOException {
        BreakoffApplication breakoffApplicationById = breakoffApplicationService.findBreakoffApplicationById(breakoffCheck.getBreakoffId());
        if(breakoffApplicationById!=null){
            breakoffCheckService.updateBreakoffCheckByAuditor(breakoffCheck);
            return new ResponseJson();
        } else {
            return new ResponseJson(false,"该调休申请已撤销");
        }

    }

    @GetMapping("/look/lookBreakoffCheckById/{id}")
    @ApiOperation(value = "去查看页面,通过id查找", notes = "返回响应对象", response=BreakoffCheck.class)
    public ResponseJson lookBreakoffCheckById(
            @ApiParam(value = "去查看页面,需要用到的id", required = true)
            @PathVariable String id){
        BreakoffCheck breakoffCheck=breakoffCheckService.findBreakoffCheckById(id);
        return new ResponseJson(breakoffCheck);
    }

    @PostMapping("/findBreakoffChecksByCondition")
    @ApiOperation(value = "根据条件查找", notes = "返回响应对象", response=BreakoffCheck.class)
    public ResponseJson findBreakoffChecksByCondition(
            @ApiParam(value = "属性不为空则作为条件查询")
            @Validated
            @RequestBody BreakoffCheck breakoffCheck){
        List<BreakoffCheck> data=breakoffCheckService.findBreakoffCheckListByCondition(breakoffCheck);
        long count=breakoffCheckService.findBreakoffCheckCountByCondition(breakoffCheck);
        return new ResponseJson(data,count);
    }
    @PostMapping("/findOneBreakoffCheckByCondition")
    @ApiOperation(value = "根据条件查找单个,结果必须为单条数据", notes = "没有时返回空", response=BreakoffCheck.class)
    public ResponseJson findOneBreakoffCheckByCondition(
            @ApiParam(value = "属性不为空则作为条件查询")
            @RequestBody BreakoffCheck breakoffCheck){
        BreakoffCheck one=breakoffCheckService.findOneBreakoffCheckByCondition(breakoffCheck);
        return new ResponseJson(one);
    }
    @GetMapping("/deleteBreakoffCheck/{id}")
    @ApiOperation(value = "根据id删除", notes = "返回响应对象")
    public ResponseJson deleteBreakoffCheck(
            @ApiParam(value = "被删除记录的id", required = true)
            @PathVariable String id){
        breakoffCheckService.deleteBreakoffCheck(id);
        return new ResponseJson();
    }


    @PostMapping("/findBreakoffCheckListByCondition")
    @ApiOperation(value = "根据条件查找列表", notes = "返回响应对象,不包含总条数", response=BreakoffCheck.class)
    public ResponseJson findBreakoffCheckListByCondition(
            @ApiParam(value = "属性不为空则作为条件查询")
            @Validated
            @RequestBody BreakoffCheck breakoffCheck){
        List<BreakoffCheck> data=breakoffCheckService.findBreakoffCheckListByCondition(breakoffCheck);
        return new ResponseJson(data);
    }


    @PostMapping("/updateAllByAuditor")
    public ResponseJson updateAllByAuditor( @ApiParam(value = "对象,对象属性不为空则修改", required = true) @RequestBody BreakoffObj breakoffObj) throws IOException {
        breakoffCheckService.updateAllByAuditor(breakoffObj);
        return new ResponseJson();
    }


}
