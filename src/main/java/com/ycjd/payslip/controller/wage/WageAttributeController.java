package com.ycjd.payslip.controller.wage;

import cn.hutool.core.date.DateUtil;
import com.ycjd.payslip.pojo.ResponseJson;
import com.ycjd.payslip.pojo.wage.WageAttribute;
import com.ycjd.payslip.pojo.wage.WageAttributeType;
import com.ycjd.payslip.service.wage.WageAttributeService;
import com.ycjd.payslip.service.wage.WageAttributeTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/wageAttribute")
@Api(value = "/wageAttribute",description = "模块")
public class WageAttributeController {
    @Autowired
    private WageAttributeService wageAttributeService;
    @Autowired
    private WageAttributeTypeService wageAttributeTypeService;
    @PostMapping("/saveWageAttribute")
    @ApiOperation(value = "保存对象", notes = "返回响应对象")
    public ResponseJson saveWageAttribute(
            @ApiParam(value = "对象", required = true)
            @RequestBody WageAttribute wageAttribute){
        List<WageAttribute> wageAttributeList = wageAttributeService.findWageAttributeListByCondition(wageAttribute);
        if(wageAttributeList.size()==0){
            wageAttribute.setUpdateTime(DateUtil.now());
            wageAttributeService.saveWageAttribute(wageAttribute);
            return new ResponseJson(true,"增加成功");
        }else{
            return new ResponseJson(false,"属性名称已存在");
        }

    }

    @GetMapping("/update/findWageAttributeById/{id}")
    @ApiOperation(value = "去更新页面,通过id查找", notes = "返回响应对象")
    public ResponseJson findWageAttributeById(
            @ApiParam(value = "去更新页面,需要用到的id", required = true)
            @PathVariable String id){
        WageAttribute wageAttribute=wageAttributeService.findWageAttributeById(id);
        return new ResponseJson(wageAttribute);
    }

    @PostMapping("/update/updateWageAttribute")
    @ApiOperation(value = "修改对象", notes = "返回响应对象")
    public ResponseJson updateWageAttribute(
            @ApiParam(value = "被修改的对象,对象属性不为空则修改", required = true)
            @RequestBody WageAttribute wageAttribute){
        WageAttribute wageAttributeById = wageAttributeService.findWageAttributeById(wageAttribute.getId());
        if(wageAttributeById.getAttributeName().equals(wageAttribute.getAttributeName())){
            return new ResponseJson(false,"属性名称已存在");
        }
        wageAttributeService.updateWageAttribute(wageAttribute);
        return new ResponseJson(true,"修改成功");
    }

    @GetMapping("/look/lookWageAttributeById/{id}")
    @ApiOperation(value = "去查看页面,通过id查找", notes = "返回响应对象")
    public ResponseJson lookWageAttributeById(
            @ApiParam(value = "去查看页面,需要用到的id", required = true)
            @PathVariable String id){
        WageAttribute wageAttribute=wageAttributeService.findWageAttributeById(id);
        return new ResponseJson(wageAttribute);
    }

    @PostMapping("/findWageAttributesByCondition")
    @ApiOperation(value = "根据条件查找", notes = "返回响应对象")
    public ResponseJson findWageAttributesByCondition(
            @ApiParam(value = "属性不为空则作为条件查询")
            @Validated
            @RequestBody WageAttribute wageAttribute){
        List<WageAttribute> data=wageAttributeService.findWageAttributeListByCondition(wageAttribute);
        long count=wageAttributeService.findWageAttributeCountByCondition(wageAttribute);
        return new ResponseJson(data,count);
    }
    @PostMapping("/findOneWageAttributeByCondition")
    @ApiOperation(value = "根据条件查找单个,结果必须为单条数据", notes = "没有时返回空")
    public ResponseJson findOneWageAttributeByCondition(
            @ApiParam(value = "属性不为空则作为条件查询")
            @RequestBody WageAttribute wageAttribute){
        WageAttribute one=wageAttributeService.findOneWageAttributeByCondition(wageAttribute);
        return new ResponseJson(one);
    }
    @GetMapping("/deleteWageAttribute/{id}")
    @ApiOperation(value = "根据id删除", notes = "返回响应对象")
    public ResponseJson deleteWageAttribute(
            @ApiParam(value = "被删除记录的id", required = true)
            @PathVariable String id){
        WageAttributeType wageAttributeType=new WageAttributeType();
        wageAttributeType.setWageAttributeId(id);
        List<WageAttributeType> wageAttributeTypeList = wageAttributeTypeService.findWageAttributeTypeListByCondition(wageAttributeType);
        if(wageAttributeTypeList.size()>0){
            return new ResponseJson(false,"该属性已被使用");
        }else {
            wageAttributeService.deleteWageAttribute(id);
            return new ResponseJson(true,"删除成功");
        }


    }


    @PostMapping("/update/findWageAttributeListByCondition")
    @ApiOperation(value = "根据条件查找列表", notes = "返回响应对象,不包含总条数")
    public ResponseJson findWageAttributeListByCondition(
            @ApiParam(value = "属性不为空则作为条件查询")
            @Validated
            @RequestBody WageAttribute wageAttribute){
        wageAttribute.getPager().setPaging(false);
        List<WageAttribute> data=wageAttributeService.findWageAttributeListByCondition(wageAttribute);
        return new ResponseJson(data);
    }



}
