package com.ycjd.payslip.controller.breakoff;

import cn.hutool.core.date.DateUtil;
import com.ycjd.payslip.pojo.ResponseJson;
import com.ycjd.payslip.pojo.breakoff.BreakoffApplication;
import com.ycjd.payslip.pojo.breakoff.BreakoffCheck;
import com.ycjd.payslip.pojo.breakoff.BreakoffForPersonnelMatters;
import com.ycjd.payslip.pojo.breakoff.BreakoffObj;
import com.ycjd.payslip.pojo.overtime.OvertimeApplication;
import com.ycjd.payslip.service.breakoff.BreakoffApplicationService;
import com.ycjd.payslip.service.breakoff.BreakoffCheckService;
import com.ycjd.payslip.service.overtime.OvertimeApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.ycjd.payslip.interceptor.LoginInterceptor.myId;

@RestController
@RequestMapping("/breakoffApplication")
@Api(value = "/breakoffApplication",description = "模块")
public class BreakoffApplicationController {
    @Autowired
    private BreakoffApplicationService breakoffApplicationService;
    @Autowired
    private OvertimeApplicationService overtimeApplicationService;
    @Autowired
    private BreakoffCheckService breakoffCheckService;
    @PostMapping("/saveBreakoffApplication")
    @ApiOperation(value = "保存对象", notes = "返回保存好的对象", response= BreakoffApplication.class)
    public ResponseJson saveBreakoffApplication(
            @ApiParam(value = "对象", required = true)
            @RequestBody BreakoffApplication breakoffApplication){
        breakoffApplicationService.saveBreakoffApplication(breakoffApplication);
        return new ResponseJson();
    }

    @PostMapping("/saveBreakoffApplication1")
    @ApiOperation(value = "保存对象", notes = "返回保存好的对象", response=OvertimeApplication.class)
    public ResponseJson saveBreakoffApplication1(
            @ApiParam(value = "对象", required = true)
            @RequestBody BreakoffApplication breakoffApplication){
        breakoffApplication.setApplicantId(myId());
        breakoffApplication.setDel("0");
        String imgs="";
        if(breakoffApplication.getImgPaths().length>0){
            for (int i = 0; i < breakoffApplication.getImgPaths().length; i++) {
                imgs+=breakoffApplication.getImgPaths()[i]+",";
            }
            imgs=imgs.substring(0,imgs.length()-1);
            breakoffApplication.setImgUrl(imgs);
        }

        breakoffApplicationService.saveBreakoffApplication1(breakoffApplication);
        return new ResponseJson();
    }

    @GetMapping("/update/findBreakoffApplicationById/{id}")
    @ApiOperation(value = "去更新页面,通过id查找", notes = "返回响应对象", response=BreakoffApplication.class)
    public ResponseJson findBreakoffApplicationById(
            @ApiParam(value = "去更新页面,需要用到的id", required = true)
            @PathVariable String id){
        BreakoffApplication breakoffApplication=breakoffApplicationService.findBreakoffApplicationById(id);
        return new ResponseJson(breakoffApplication);
    }

    @PostMapping("/update/updateBreakoffApplication")
    @ApiOperation(value = "修改对象", notes = "返回响应对象")
    public ResponseJson updateBreakoffApplication(
            @ApiParam(value = "被修改的对象,对象属性不为空则修改", required = true)
            @RequestBody BreakoffApplication breakoffApplication){
        breakoffApplicationService.updateBreakoffApplication(breakoffApplication);
        return new ResponseJson();
    }

    @GetMapping("/look/lookBreakoffApplicationById/{id}")
    @ApiOperation(value = "去查看页面,通过id查找", notes = "返回响应对象", response=BreakoffApplication.class)
    public ResponseJson lookBreakoffApplicationById(
            @ApiParam(value = "去查看页面,需要用到的id", required = true)
            @PathVariable String id){
        BreakoffApplication breakoffApplication=breakoffApplicationService.findBreakoffApplicationById(id);
        return new ResponseJson(breakoffApplication);
    }

    @PostMapping("/findBreakoffApplicationsByCondition")
    @ApiOperation(value = "根据条件查找", notes = "返回响应对象", response=BreakoffApplication.class)
    public ResponseJson findBreakoffApplicationsByCondition(
            @ApiParam(value = "属性不为空则作为条件查询")
            @Validated
            @RequestBody BreakoffApplication breakoffApplication){
        List<BreakoffApplication> data=breakoffApplicationService.findBreakoffApplicationListByCondition(breakoffApplication);
        long count=breakoffApplicationService.findBreakoffApplicationCountByCondition(breakoffApplication);
        return new ResponseJson(data,count);
    }

    @PostMapping("/findBreakoffApplicationsByCondition1")
    @ApiOperation(value = "根据条件查找", notes = "返回响应对象", response=BreakoffApplication.class)
    public ResponseJson findBreakoffApplicationsByCondition1(
            @ApiParam(value = "属性不为空则作为条件查询")
            @Validated
            @RequestBody BreakoffApplication breakoffApplication){
        breakoffApplication.setApplicantId(myId());
        if(breakoffApplication.getRangeTime()!=null){
            breakoffApplication.setStartTime(breakoffApplication.getRangeTime()[0]);
            breakoffApplication.setEndTime(dateToStamp1(breakoffApplication.getRangeTime()[1]));
        }
        List<BreakoffApplication> breakoffApplicationListByApId = breakoffApplicationService.findBreakoffApplicationListByApId(breakoffApplication);
        List<String>stringList=new ArrayList<>();
        for (int i = 0; i < breakoffApplicationListByApId.size(); i++) {
            stringList.add(breakoffApplicationListByApId.get(i).getId());
        }
        breakoffApplication.setIds(stringList);

        List<BreakoffApplication> data=breakoffApplicationService.findBreakoffApplicationListByCondition1(breakoffApplication);
        long count=breakoffApplicationService.findBreakoffApplicationCountByCondition(breakoffApplication);

        Map<String, List<BreakoffApplication>> data1 = data.stream().collect(Collectors.groupingBy(BreakoffApplication::getId));
        List<BreakoffApplication>data2=new ArrayList<>();

        data1.forEach((k,v)->{
            List<BreakoffApplication> data3=v;
            String result=data3.stream().sorted(Comparator.comparing(BreakoffApplication::getSort)).map(BreakoffApplication::getAuditors).collect(Collectors.joining("->"));
            String result1=data3.stream().sorted(Comparator.comparing(BreakoffApplication::getSort)).map(BreakoffApplication::getAuditorStatuses).collect(Collectors.joining(","));
            data3.forEach(breakoffApplication1 -> {
                breakoffApplication1.setAuditors(result);
                breakoffApplication1.setAuditorStatuses(result1);
            });
            data2.add(data3.get(0));
        });
        data2.sort((BreakoffApplication c1,BreakoffApplication c2)->dateToStamp(c1.getCreateTime())>dateToStamp(c2.getCreateTime())? -1 : 1);
        return new ResponseJson(data2,count);
    }



    @PostMapping("/findOneBreakoffApplicationByCondition")
    @ApiOperation(value = "根据条件查找单个,结果必须为单条数据", notes = "没有时返回空", response=BreakoffApplication.class)
    public ResponseJson findOneBreakoffApplicationByCondition(
            @ApiParam(value = "属性不为空则作为条件查询")
            @RequestBody BreakoffApplication breakoffApplication){
        BreakoffApplication one=breakoffApplicationService.findOneBreakoffApplicationByCondition(breakoffApplication);
        return new ResponseJson(one);
    }
    @GetMapping("/deleteBreakoffApplication/{id}")
    @ApiOperation(value = "根据id删除", notes = "返回响应对象")
    public ResponseJson deleteBreakoffApplication(
            @ApiParam(value = "被删除记录的id", required = true)
            @PathVariable String id){
        breakoffApplicationService.deleteBreakoffApplication(id);
        return new ResponseJson();
    }


    @PostMapping("/findBreakoffApplicationListByCondition")
    @ApiOperation(value = "根据条件查找列表", notes = "返回响应对象,不包含总条数", response=BreakoffApplication.class)
    public ResponseJson findBreakoffApplicationListByCondition(
            @ApiParam(value = "属性不为空则作为条件查询")
            @Validated
            @RequestBody BreakoffApplication breakoffApplication){
        List<BreakoffApplication> data=breakoffApplicationService.findBreakoffApplicationListByCondition(breakoffApplication);
        return new ResponseJson(data);
    }

    @PostMapping("/findMyRemainingTime")
    public ResponseJson findMyRemainingTime(){
        BreakoffApplication breakoffApplication=new BreakoffApplication();
        OvertimeApplication overtimeApplication=new OvertimeApplication();
        breakoffApplication.setApplicantId(myId());
        overtimeApplication.setApplicantId(myId());

        BreakoffApplication myTime1 = breakoffApplicationService.findMyRemainingTime(breakoffApplication);
        Double times1;
        Double times2;
        Double surplusTime;
        if(myTime1==null){
            times1=0.0;
        }else{
            times1=Double.parseDouble(myTime1.getMyTime());
        }
        OvertimeApplication myTime2 = overtimeApplicationService.findMyRemainingTime(overtimeApplication);
        if(myTime2==null){
            times2=0.0;
        }else{
            times2=Double.parseDouble(myTime2.getMyTime());
        }
        if(times2%4!=0){
            surplusTime=2.0;
            times2=times2-surplusTime;
        }else {
            surplusTime=0.0;
        }

        Double times=times2-times1;
        return new ResponseJson(times,surplusTime);
    }

    @PostMapping("/findWorkerRemainingTime")
    public ResponseJson findWorkerRemainingTime(@RequestBody BreakoffObj breakoffObj){
        OvertimeApplication overtimeApplication=new OvertimeApplication();
        overtimeApplication.setApplicantId(breakoffObj.getApplicantId());

        BreakoffCheck myTime1 = breakoffCheckService.findWorkerRemainingTime(breakoffObj);
        Double times1;
        Double times2;
        if(myTime1==null){
            times1=0.0;
        }else{
            times1=Double.parseDouble(myTime1.getMyTime());
        }
        OvertimeApplication myTime2 = overtimeApplicationService.findMyRemainingTime(overtimeApplication);
        if(myTime2==null){
            times2=0.0;
        }else{
            times2=Double.parseDouble(myTime2.getMyTime());
        }
        Double times=times2-times1;
        return new ResponseJson(times);
    }

    @PostMapping("/findPersonnelMattersRemainingTime")
    public ResponseJson findPersonnelMattersRemainingTime(@RequestBody BreakoffObj breakoffObj){
        OvertimeApplication overtimeApplication=new OvertimeApplication();
        overtimeApplication.setApplicantId(breakoffObj.getApplicantId());

        BreakoffApplication myTime1 = breakoffApplicationService.findPersonnelMattersRemainingTime(breakoffObj);
        Double times1;
        Double times2;
        if(myTime1==null){
            times1=0.0;
        }else{
            times1=Double.parseDouble(myTime1.getMyTime());
        }
        OvertimeApplication myTime2 = overtimeApplicationService.findMyRemainingTime(overtimeApplication);
        if(myTime2==null){
            times2=0.0;
        }else{
            times2=Double.parseDouble(myTime2.getMyTime());
        }
        Double times=times2-times1;
        return new ResponseJson(times);
    }


    @PostMapping("/findBreakoffApplicationsByAuditor")
    @ApiOperation(value = "根据条件查找", notes = "返回响应对象", response=BreakoffApplication.class)
    public ResponseJson findBreakoffApplicationsByAuditor(
            @ApiParam(value = "属性不为空则作为条件查询")
            @Validated
            @RequestBody BreakoffObj breakoffObj){
        breakoffObj.setAuditorId(myId());
        if(breakoffObj.getRangeTime()!=null){
            breakoffObj.setStartTime(breakoffObj.getRangeTime()[0]);
            breakoffObj.setEndTime(dateToStamp1(breakoffObj.getRangeTime()[1]));
        }
        List<BreakoffApplication> data=breakoffApplicationService.findBreakoffApplicationsByAuditor(breakoffObj);
        long count=breakoffApplicationService.findBreakoffApplicationCountByAuditor(breakoffObj);
        data.sort((BreakoffApplication c1,BreakoffApplication c2)->dateToStamp(c1.getCreateTime())>dateToStamp(c2.getCreateTime())? -1 : 1);
        return new ResponseJson(data,count);
    }

    @PostMapping("/findBreakoffApplicationsByPersonnelMatters")
    @ApiOperation(value = "根据条件查找", notes = "返回响应对象", response=BreakoffApplication.class)
    public ResponseJson findBreakoffApplicationsByPersonnelMatters(
            @ApiParam(value = "属性不为空则作为条件查询")
            @Validated
            @RequestBody BreakoffObj breakoffObj){
        breakoffObj.setAuditorId(myId());
        List<BreakoffForPersonnelMatters> data=breakoffApplicationService.findBreakoffApplicationsByPersonnelMatters(breakoffObj);
        long count=breakoffApplicationService.findBreakoffApplicationsByPersonnelMattersCount(breakoffObj);
        return new ResponseJson(data,count);
    }

    @PostMapping("/findBreakoffApplicationsByWorkerId")
    @ApiOperation(value = "根据条件查找", notes = "返回响应对象", response=BreakoffApplication.class)
    public ResponseJson findBreakoffApplicationsByWorkerId(
            @ApiParam(value = "属性不为空则作为条件查询")
            @Validated
            @RequestBody BreakoffObj breakoffObj){
        if(breakoffObj.getRangeTime()!=null){
            breakoffObj.setStartTime(breakoffObj.getRangeTime()[0]);
            breakoffObj.setEndTime(dateToStamp1(breakoffObj.getRangeTime()[1]));
        }
        List<BreakoffApplication> data=breakoffApplicationService.findbreakoffApplicationsByWorkerId(breakoffObj);
        long count=breakoffApplicationService.findbreakoffApplicationsByWorkerIdCount(breakoffObj);
        data.sort((BreakoffApplication c1,BreakoffApplication c2)->dateToStamp(c1.getCreateTime())>dateToStamp(c2.getCreateTime())? -1 : 1);
        return new ResponseJson(data,count);
    }

    @PostMapping("/updateAllByPersonnelMatters")
    public ResponseJson updateAllByPersonnelMatters( @ApiParam(value = "对象,对象属性不为空则修改", required = true) @RequestBody BreakoffObj breakoffObj){
        breakoffApplicationService.updateAllByPersonnelMatters(breakoffObj);
        return new ResponseJson();
    }

    @GetMapping("/deleteBreakoffApplicationByBreakoffId/{id}")
    @ApiOperation(value = "根据id删除", notes = "返回响应对象")
    public ResponseJson deleteBreakoffApplicationByBreakoffId(
            @ApiParam(value = "被删除记录的id", required = true)
            @PathVariable String id){
        breakoffApplicationService.deleteBreakoffApplicationByBreakoffId(id);
        return new ResponseJson();
    }

    @GetMapping("/findBreakoffApplicationDetail/{id}")
    @ApiOperation(value = "根据条件查找", notes = "返回响应对象")
    public ResponseJson findBreakoffApplicationDetail(@ApiParam(value = "属性不为空则作为条件查询") @PathVariable String id){
        BreakoffApplication breakoffApplicationById = breakoffApplicationService.findBreakoffApplicationById(id);
        if(breakoffApplicationById!=null){
            List<BreakoffApplication> data=breakoffApplicationService.findBreakoffApplicationDetail(id);
            for (int i = 0; i < data.size(); i++) {
                if(data.get(i).getImgUrl()!=null){
                    data.get(i).setImgPaths(data.get(i).getImgUrl().split(","));
                }
            }

            Map<String, List<BreakoffApplication>> data1 = data.stream().collect(Collectors.groupingBy(BreakoffApplication::getId));
            List<BreakoffApplication>data2=new ArrayList<>();

            data1.forEach((k,v)->{
                List<BreakoffApplication> data3=v;
                List<BreakoffCheck>list=new ArrayList<>();
                data3.stream().sorted(Comparator.comparing(BreakoffApplication::getSort));
                data3.forEach(breakoffApplication1 -> {
                    BreakoffCheck breakoffCheck=new BreakoffCheck();
                    breakoffCheck.setAuditorName(breakoffApplication1.getAuditors());
                    breakoffCheck.setAuditorStatus(breakoffApplication1.getAuditorStatuses());
                    breakoffCheck.setAuditorRemarks(breakoffApplication1.getRemarks());
                    breakoffCheck.setThroughTime(breakoffApplication1.getThroughTime());
                    list.add(breakoffCheck);
                });
                data3.get(0).setBreakoffCheckList(list);
                data2.add(data3.get(0));
            });
            return new ResponseJson(data2.get(0));
        }else {
            return new ResponseJson(false,"该调休申请已撤销");
        }

    }
    public long dateToStamp(String s)  {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        return ts;
    }
    public String dateToStamp1(String s)  {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = DateUtil.endOfDay(simpleDateFormat.parse(s));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return simpleDateFormat.format(date);
    }

}
