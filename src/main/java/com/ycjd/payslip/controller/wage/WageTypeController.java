package com.ycjd.payslip.controller.wage;

import com.ycjd.payslip.pojo.ResponseJson;
import com.ycjd.payslip.pojo.wage.WageAttributeType;
import com.ycjd.payslip.pojo.wage.WageType;
import com.ycjd.payslip.service.wage.WageAttributeTypeService;
import com.ycjd.payslip.service.wage.WageTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wageType")
@Api(value = "/wageType",description = "模块")
public class WageTypeController {
    @Autowired
    private WageTypeService wageTypeService;
    @Autowired
    private WageAttributeTypeService wageAttributeTypeService;

    @PostMapping("/saveWageType")
    @ApiOperation(value = "保存对象", notes = "返回响应对象")
    public ResponseJson saveWageType(
            @ApiParam(value = "对象", required = true)
            @RequestBody WageType wageType){
        List<WageType> wageTypeList = wageTypeService.findWageTypeListByConditionNotState(wageType);
        if(wageTypeList.size()==0){
            wageTypeService.saveWageType(wageType);
            return new ResponseJson();
        }else{
            return new ResponseJson(false,"类型名称已存在");
        }

    }

    @GetMapping("/update/findWageTypeById/{id}")
    @ApiOperation(value = "去更新页面,通过id查找", notes = "返回响应对象")
    public ResponseJson findWageTypeById(
            @ApiParam(value = "去更新页面,需要用到的id", required = true)
            @PathVariable String id){
        WageType wageType=wageTypeService.findWageTypeById(id);
        return new ResponseJson(wageType);
    }

    @PostMapping("/update/updateWageType")
    @ApiOperation(value = "修改对象", notes = "返回响应对象")
    public ResponseJson updateWageType(
            @ApiParam(value = "被修改的对象,对象属性不为空则修改", required = true)
            @RequestBody WageType wageType){
        wageTypeService.updateWageType(wageType);
        return new ResponseJson();
    }

    @GetMapping("/look/lookWageTypeById/{id}")
    @ApiOperation(value = "去查看页面,通过id查找", notes = "返回响应对象")
    public ResponseJson lookWageTypeById(
            @ApiParam(value = "去查看页面,需要用到的id", required = true)
            @PathVariable String id){
        WageType wageType=wageTypeService.findWageTypeById(id);
        return new ResponseJson(wageType);
    }

    @PostMapping("/findWageTypesByCondition")
    @ApiOperation(value = "根据条件查找", notes = "返回响应对象")
    public ResponseJson findWageTypesByCondition(
            @ApiParam(value = "属性不为空则作为条件查询")
            @Validated
            @RequestBody WageType wageType){
        List<WageType> data=wageTypeService.findWageTypeListByCondition(wageType);
        long count=wageTypeService.findWageTypeCountByCondition(wageType);
        return new ResponseJson(data,count);
    }
    @PostMapping("/findOneWageTypeByCondition")
    @ApiOperation(value = "根据条件查找单个,结果必须为单条数据", notes = "没有时返回空")
    public ResponseJson findOneWageTypeByCondition(
            @ApiParam(value = "属性不为空则作为条件查询")
            @RequestBody WageType wageType){
        WageType one=wageTypeService.findOneWageTypeByCondition(wageType);
        return new ResponseJson(one);
    }
    @GetMapping("/deleteWageType/{id}")
    @ApiOperation(value = "根据id删除", notes = "返回响应对象")
    public ResponseJson deleteWageType(
            @ApiParam(value = "被删除记录的id", required = true)
            @PathVariable String id){
        wageTypeService.deleteWageType(id);
        return new ResponseJson();
    }


    @PostMapping("/findWageTypeListByCondition")
    @ApiOperation(value = "根据条件查找列表", notes = "返回响应对象,不包含总条数")
    public ResponseJson findWageTypeListByCondition(
            @ApiParam(value = "属性不为空则作为条件查询")
            @Validated
            @RequestBody WageType wageType){
        List<WageType> data=wageTypeService.findWageTypeListByCondition(wageType);
        return new ResponseJson(data);
    }

    @GetMapping("/update/findWageTypeByTypeId/{id}") //属性修改页面展示属性名称
    @ApiOperation(value = "去更新页面,通过id查找", notes = "返回响应对象")
    public ResponseJson findWageTypeByTypeId(
            @ApiParam(value = "去更新页面,需要用到的id", required = true)
            @PathVariable String id){
        List<WageAttributeType>wageAttributeTypeList=wageAttributeTypeService.findWageAttributeTypeByTypeId(id);
        return new ResponseJson(wageAttributeTypeList);
    }


    @PostMapping("/findWageTypesByCondition1")
    @ApiOperation(value = "根据条件查找列表", notes = "返回响应对象,不包含总条数")
    public ResponseJson findWageTypesByCondition1(
            @ApiParam(value = "属性不为空则作为条件查询")
            @Validated
            @RequestBody WageType wageType){
        List<WageType> data=wageTypeService.findWageTypeListByCondition1(wageType);
        long count=wageTypeService.findWageTypeCountByCondition(wageType);
        return new ResponseJson(data,count);
    }


    @GetMapping("/update/updateWageTypeState/{id}")
    @ApiOperation(value = "根据id修改", notes = "返回响应对象")
    public ResponseJson updateWageTypeState(
            @ApiParam(value = "被修改记录的id", required = true)
            @PathVariable String id){
        wageTypeService.updateWageTypeState(id);
        return new ResponseJson();
    }

    @PostMapping("/update/findWageTypeListByState")
    @ApiOperation(value = "根据条件查找列表", notes = "返回响应对象,不包含总条数")
    public ResponseJson findWageTypeListByState(
            @ApiParam(value = "属性不为空则作为条件查询")
            @RequestBody WageType wageType){
        wageType.setState("1");
        List<WageType> data=wageTypeService.findWageTypeListByState(wageType);
        return new ResponseJson(data);
    }


    @PostMapping("/update/findWageAttributeListByTypeId")//显示属性列表
    @ApiOperation(value = "根据条件查找列表", notes = "返回响应对象,不包含总条数")
    public ResponseJson findWageAttributeListByTypeId(
            @ApiParam(value = "属性不为空则作为条件查询")
            @RequestBody WageType wageType){
        List<WageType> data=wageTypeService.findWageAttributeListByTypeId(wageType);
        return new ResponseJson(data);
    }






}
