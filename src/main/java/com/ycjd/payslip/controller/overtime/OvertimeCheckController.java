package com.ycjd.payslip.controller.overtime;

import com.ycjd.payslip.pojo.ResponseJson;
import com.ycjd.payslip.pojo.overtime.OvertimeApplication;
import com.ycjd.payslip.pojo.overtime.OvertimeCheck;
import com.ycjd.payslip.pojo.overtime.OvertimeObj;
import com.ycjd.payslip.service.overtime.OvertimeApplicationService;
import com.ycjd.payslip.service.overtime.OvertimeCheckService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/overtimeCheck")
@Api(value = "/overtimeCheck",description = "模块")
public class OvertimeCheckController {
    @Autowired
    private OvertimeCheckService overtimeCheckService;
    @Autowired
    private OvertimeApplicationService overtimeApplicationService;
    @GetMapping("/findOvertimeCheckById/{id}")
    @ApiOperation(value = "通过id查找", notes = "返回对象")
    public OvertimeCheck findOvertimeCheckById(
            @ApiParam(value = "需要用到的id", required = true)
            @PathVariable String id){
        return overtimeCheckService.findOvertimeCheckById(id);
    }

    @PostMapping("/saveOvertimeCheck")
    @ApiOperation(value = "保存", notes = "返回对象")
    public OvertimeCheck saveOvertimeCheck(
            @ApiParam(value = "对象", required = true)
            @RequestBody OvertimeCheck overtimeCheck){
        overtimeCheckService.saveOvertimeCheck(overtimeCheck);
        return overtimeCheck;
    }

    @PostMapping("/findOvertimeCheckListByCondition")
    @ApiOperation(value = "根据条件查找列表", notes = "返回列表")
    public List<OvertimeCheck> findOvertimeCheckListByCondition(
            @ApiParam(value = "对象")
            @RequestBody OvertimeCheck overtimeCheck){
        return overtimeCheckService.findOvertimeCheckListByCondition(overtimeCheck);
    }
    @PostMapping("/findOvertimeCheckCountByCondition")
    @ApiOperation(value = "根据条件查找列表个数", notes = "返回总个数")
    public long findOvertimeCheckCountByCondition(
            @ApiParam(value = "对象")
            @RequestBody OvertimeCheck overtimeCheck){
        return overtimeCheckService.findOvertimeCheckCountByCondition(overtimeCheck);
    }

    @PostMapping("/updateOvertimeCheck")
    @ApiOperation(value = "修改", notes = "对象必传")
    public void updateOvertimeCheck(
            @ApiParam(value = "对象,对象属性不为空则修改", required = true)
            @RequestBody OvertimeCheck overtimeCheck){
        overtimeCheckService.updateOvertimeCheck(overtimeCheck);
    }

    @GetMapping("/deleteOvertimeCheck/{id}")
    @ApiOperation(value = "通过id删除")
    public void deleteOvertimeCheck(
            @ApiParam(value = "对象", required = true)
            @PathVariable String id){
        overtimeCheckService.deleteOvertimeCheck(id);
    }
    @PostMapping("/deleteOvertimeCheckByCondition")
    @ApiOperation(value = "根据条件删除")
    public void deleteOvertimeCheckByCondition(
            @ApiParam(value = "对象")
            @RequestBody OvertimeCheck overtimeCheck){
        overtimeCheckService.deleteOvertimeCheckByCondition(overtimeCheck);
    }
    @PostMapping("/findOneOvertimeCheckByCondition")
    @ApiOperation(value = "根据条件查找单个,结果必须为单条数据", notes = "返回单个,没有时为空")
    public OvertimeCheck findOneOvertimeCheckByCondition(
            @ApiParam(value = "对象")
            @RequestBody OvertimeCheck overtimeCheck){
        return overtimeCheckService.findOneOvertimeCheckByCondition(overtimeCheck);
    }


    @PostMapping("/updateOvertimeCheckByAuditor")
    @ApiOperation(value = "修改", notes = "对象必传")
    public ResponseJson updateOvertimeCheckByAuditor(
            @ApiParam(value = "对象,对象属性不为空则修改", required = true)
            @RequestBody OvertimeCheck overtimeCheck) throws IOException {
        OvertimeApplication overtimeApplicationById = overtimeApplicationService.findOvertimeApplicationById(overtimeCheck.getOvertimeId());
        if(overtimeApplicationById!=null){
            overtimeCheckService.updateOvertimeCheckByAuditor(overtimeCheck);
            return new ResponseJson();
        }else {
            return new ResponseJson(false,"该加班申请已撤销");
        }


    }


    @PostMapping("/updateAllByAuditor")
    public ResponseJson updateAllByAuditor( @ApiParam(value = "对象,对象属性不为空则修改", required = true) @RequestBody OvertimeObj overtimeObj) throws IOException {
        overtimeCheckService.updateAllByAuditor(overtimeObj);
        return new ResponseJson();
    }
}
