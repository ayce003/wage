package com.ycjd.payslip.controller.overtime;

import cn.hutool.core.date.DateUtil;
import com.ycjd.payslip.pojo.ResponseJson;
import com.ycjd.payslip.pojo.overtime.*;
import com.ycjd.payslip.pojo.pushmsg.PushMsg;
import com.ycjd.payslip.service.breakoff.BreakoffApplicationService;
import com.ycjd.payslip.service.overtime.OvertimeApplicationService;
import com.ycjd.payslip.service.pushmsg.PushMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.ycjd.payslip.interceptor.LoginInterceptor.myId;

@RestController
@RequestMapping("/overtimeApplication")
@Api(value = "/overtimeApplication",description = "模块")
public class OvertimeApplicationController {
    @Autowired
    private OvertimeApplicationService overtimeApplicationService;
    @Autowired
    private PushMsgService pushMsgService;
    @Autowired
    private BreakoffApplicationService breakoffApplicationService;

    @PostMapping("/saveOvertimeApplication")
    @ApiOperation(value = "保存对象", notes = "返回保存好的对象", response=OvertimeApplication.class)
    public ResponseJson saveOvertimeApplication(
            @ApiParam(value = "对象", required = true)
            @RequestBody OvertimeApplication overtimeApplication){
        overtimeApplicationService.saveOvertimeApplication(overtimeApplication);
        return new ResponseJson();
    }

    @PostMapping("/saveOvertimeApplication1")
    @ApiOperation(value = "保存对象", notes = "返回保存好的对象", response=OvertimeApplication.class)
    public ResponseJson saveOvertimeApplication1(
            @ApiParam(value = "对象", required = true)
            @RequestBody OvertimeApplication overtimeApplication){
        overtimeApplication.setApplicantId(myId());
        overtimeApplication.setDel("0");
        String imgs="";
        if(overtimeApplication.getImgPaths().length>0){
            for (int i = 0; i < overtimeApplication.getImgPaths().length; i++) {
                imgs+=overtimeApplication.getImgPaths()[i]+",";
            }
            imgs=imgs.substring(0,imgs.length()-1);
            overtimeApplication.setImgUrl(imgs);
        }

        overtimeApplicationService.saveOvertimeApplication1(overtimeApplication);
        return new ResponseJson();
    }

    @GetMapping("/update/findOvertimeApplicationById/{id}")
    @ApiOperation(value = "去更新页面,通过id查找", notes = "返回响应对象", response=OvertimeApplication.class)
    public ResponseJson findOvertimeApplicationById(
            @ApiParam(value = "去更新页面,需要用到的id", required = true)
            @PathVariable String id){
        OvertimeApplication overtimeApplication=overtimeApplicationService.findOvertimeApplicationById(id);
        return new ResponseJson(overtimeApplication);
    }

    @PostMapping("/update/updateOvertimeApplication")
    @ApiOperation(value = "修改对象", notes = "返回响应对象")
    public ResponseJson updateOvertimeApplication(
            @ApiParam(value = "被修改的对象,对象属性不为空则修改", required = true)
            @RequestBody OvertimeApplication overtimeApplication){
        overtimeApplicationService.updateOvertimeApplication(overtimeApplication);
        return new ResponseJson();
    }

    @GetMapping("/look/lookOvertimeApplicationById/{id}")
    @ApiOperation(value = "去查看页面,通过id查找", notes = "返回响应对象", response=OvertimeApplication.class)
    public ResponseJson lookOvertimeApplicationById(
            @ApiParam(value = "去查看页面,需要用到的id", required = true)
            @PathVariable String id){
        OvertimeApplication overtimeApplication=overtimeApplicationService.findOvertimeApplicationById(id);
        return new ResponseJson(overtimeApplication);
    }

    @PostMapping("/findOvertimeApplicationsByCondition")
    @ApiOperation(value = "根据条件查找", notes = "返回响应对象", response=OvertimeApplication.class)
    public ResponseJson findOvertimeApplicationsByCondition(
            @ApiParam(value = "属性不为空则作为条件查询")
            @Validated
            @RequestBody OvertimeApplication overtimeApplication){
        List<OvertimeApplication> data=overtimeApplicationService.findOvertimeApplicationListByCondition(overtimeApplication);
        long count=overtimeApplicationService.findOvertimeApplicationCountByCondition(overtimeApplication);
        return new ResponseJson(data,count);
    }

    @PostMapping("/findOvertimeApplicationsByCondition1")
    @ApiOperation(value = "根据条件查找", notes = "返回响应对象", response=OvertimeApplication.class)
    public ResponseJson findOvertimeApplicationsByCondition1(
            @ApiParam(value = "属性不为空则作为条件查询")
            @Validated
            @RequestBody OvertimeApplication overtimeApplication){
        overtimeApplication.setApplicantId(myId());
        if(overtimeApplication.getRangeTime()!=null){
            overtimeApplication.setStartTime(overtimeApplication.getRangeTime()[0]);
            overtimeApplication.setEndTime(dateToStamp1(overtimeApplication.getRangeTime()[1]));
        }

        List<OvertimeApplication> overtimeApplicationListByApId = overtimeApplicationService.findOvertimeApplicationListByApId(overtimeApplication);
        List<String>stringList=new ArrayList<>();
        for (int i = 0; i < overtimeApplicationListByApId.size(); i++) {
            stringList.add(overtimeApplicationListByApId.get(i).getId());
        }
        overtimeApplication.setIds(stringList);

        List<OvertimeApplication> data=overtimeApplicationService.findOvertimeApplicationListByCondition1(overtimeApplication);
        long count=overtimeApplicationService.findOvertimeApplicationCountByCondition(overtimeApplication);

        Map<String, List<OvertimeApplication>> data1 = data.stream().collect(Collectors.groupingBy(OvertimeApplication::getId));
        List<OvertimeApplication>data2=new ArrayList<>();


        data1.forEach((k,v)->{
            List<OvertimeApplication> data3=v;
            String result=data3.stream().sorted(Comparator.comparing(OvertimeApplication::getSort)).map(OvertimeApplication::getAuditors).collect(Collectors.joining("->"));
            String result1=data3.stream().sorted(Comparator.comparing(OvertimeApplication::getSort)).map(OvertimeApplication::getAuditorStatuses).collect(Collectors.joining(","));
            data3.forEach(overtimeApplication1 -> {
                overtimeApplication1.setAuditors(result);
                overtimeApplication1.setAuditorStatuses(result1);
            });
            data2.add(data3.get(0));
        });

        data2.sort((OvertimeApplication c1,OvertimeApplication c2)->dateToStamp(c1.getCreateTime())>dateToStamp(c2.getCreateTime())? -1 : 1);
        return new ResponseJson(data2,count);
    }

    @PostMapping("/findOvertimeApplicationsByAuditor")
    @ApiOperation(value = "根据条件查找", notes = "返回响应对象", response=OvertimeApplication.class)
    public ResponseJson findOvertimeApplicationsByAuditor(
            @ApiParam(value = "属性不为空则作为条件查询")
            @Validated
            @RequestBody OvertimeObj overtimeObj){
        overtimeObj.setAuditorId(myId());
        if(overtimeObj.getRangeTime()!=null){
            overtimeObj.setStartTime(overtimeObj.getRangeTime()[0]);
            overtimeObj.setEndTime(dateToStamp1(overtimeObj.getRangeTime()[1]));
        }
        List<OvertimeApplication> data=overtimeApplicationService.findOvertimeApplicationsByAuditor(overtimeObj);
        long count=overtimeApplicationService.findOvertimeApplicationsCountByAuditor(overtimeObj);
        data.sort((OvertimeApplication c1,OvertimeApplication c2)->dateToStamp(c1.getCreateTime())>dateToStamp(c2.getCreateTime())? -1 : 1);
        return new ResponseJson(data,count);
    }
    @GetMapping("/findOvertimeApplicationsCountByAuditor")
    @ApiOperation(value = "根据条件查找", notes = "返回响应对象", response=OvertimeApplication.class)
    public ResponseJson findOvertimeApplicationsCountByAuditor(){
        OvertimeObj overtimeObj=new OvertimeObj();
        overtimeObj.setAuditorId(myId());
        overtimeObj.setStatus("0");
        long count=overtimeApplicationService.findOvertimeApplicationsCountByAuditor(overtimeObj);
        return new ResponseJson(count);
    }

    @PostMapping("/findOvertimeApplicationsByPersonnelMatters")
    @ApiOperation(value = "根据条件查找", notes = "返回响应对象", response=OvertimeApplication.class)
    public ResponseJson findOvertimeApplicationsByPersonnelMatters(
            @ApiParam(value = "属性不为空则作为条件查询")
            @Validated
            @RequestBody OvertimeObj overtimeObj){
        List<OvertimeForPersonnelMatters> data=overtimeApplicationService.findOvertimeApplicationsByPersonnelMatters(overtimeObj);
        long count=overtimeApplicationService.findOvertimeApplicationsByPersonnelMattersCount(overtimeObj);
        return new ResponseJson(data,count);
    }

    @PostMapping("/findOvertimeApplicationsByWorkerId")
    @ApiOperation(value = "根据条件查找", notes = "返回响应对象", response=OvertimeApplication.class)
    public ResponseJson findOvertimeApplicationsByWorkerId(
            @ApiParam(value = "属性不为空则作为条件查询")
            @Validated
            @RequestBody OvertimeObj overtimeObj){
        if(overtimeObj.getRangeTime()!=null){
            overtimeObj.setStartTime(overtimeObj.getRangeTime()[0]);
            overtimeObj.setEndTime(dateToStamp1(overtimeObj.getRangeTime()[1]));
        }
        List<OvertimeApplication> data=overtimeApplicationService.findOvertimeApplicationsByWorkerId(overtimeObj);
        long count=overtimeApplicationService.findOvertimeApplicationsByWorkerIdCount(overtimeObj);
        data.sort((OvertimeApplication c1,OvertimeApplication c2)->dateToStamp(c1.getCreateTime())>dateToStamp(c2.getCreateTime())? -1 : 1);
        return new ResponseJson(data,count);
    }



    @PostMapping("/findOneOvertimeApplicationByCondition")
    @ApiOperation(value = "根据条件查找单个,结果必须为单条数据", notes = "没有时返回空", response=OvertimeApplication.class)
    public ResponseJson findOneOvertimeApplicationByCondition(
            @ApiParam(value = "属性不为空则作为条件查询")
            @RequestBody OvertimeApplication overtimeApplication){
        OvertimeApplication one=overtimeApplicationService.findOneOvertimeApplicationByCondition(overtimeApplication);
        return new ResponseJson(one);
    }
    @GetMapping("/deleteOvertimeApplication/{id}")
    @ApiOperation(value = "根据id删除", notes = "返回响应对象")
    public ResponseJson deleteOvertimeApplication(
            @ApiParam(value = "被删除记录的id", required = true)
            @PathVariable String id){
        overtimeApplicationService.deleteOvertimeApplication(id);
        return new ResponseJson();
    }


    @PostMapping("/findOvertimeApplicationListByCondition")
    @ApiOperation(value = "根据条件查找列表", notes = "返回响应对象,不包含总条数", response=OvertimeApplication.class)
    public ResponseJson findOvertimeApplicationListByCondition(
            @ApiParam(value = "属性不为空则作为条件查询")
            @Validated
            @RequestBody OvertimeApplication overtimeApplication){
        List<OvertimeApplication> data=overtimeApplicationService.findOvertimeApplicationListByCondition(overtimeApplication);
        return new ResponseJson(data);
    }

    @PostMapping("/updateAllByPersonnelMatters")
    public ResponseJson updateAllByPersonnelMatters( @ApiParam(value = "对象,对象属性不为空则修改", required = true) @RequestBody OvertimeObj overtimeObj){
        overtimeApplicationService.updateAllByPersonnelMatters(overtimeObj);
        return new ResponseJson();
    }


    @GetMapping("/deleteOvertimeApplicationByOvertimeId/{id}")
    @ApiOperation(value = "根据id删除", notes = "返回响应对象")
    public ResponseJson deleteOvertimeApplicationByOvertimeId(
            @ApiParam(value = "被删除记录的id", required = true)
            @PathVariable String id){
        overtimeApplicationService.deleteOvertimeApplicationByOvertimeId(id);
        return new ResponseJson();
    }

    @GetMapping("/findOvertimeApplicationDetail/{id}")
    @ApiOperation(value = "根据条件查找", notes = "返回响应对象")
    public ResponseJson findOvertimeApplicationDetail(@ApiParam(value = "属性不为空则作为条件查询") @PathVariable String id){
        OvertimeApplication overtimeApplicationById = overtimeApplicationService.findOvertimeApplicationById(id);
        if(overtimeApplicationById!=null){
            List<OvertimeApplication> data=overtimeApplicationService.findOvertimeApplicationDetail(id);
            for (int i = 0; i < data.size(); i++) {
                if(data.get(i).getImgUrl()!=null){
                    data.get(i).setImgPaths(data.get(i).getImgUrl().split(","));
                }
            }

            Map<String, List<OvertimeApplication>> data1 = data.stream().collect(Collectors.groupingBy(OvertimeApplication::getId));
            List<OvertimeApplication>data2=new ArrayList<>();

            data1.forEach((k,v)->{
                List<OvertimeApplication> data3=v;
                List<OvertimeCheck>list=new ArrayList<>();
                data3.stream().sorted(Comparator.comparing(OvertimeApplication::getSort));
                data3.forEach(overtimeApplication1 -> {
                    OvertimeCheck overtimeCheck=new OvertimeCheck();
                    overtimeCheck.setAuditorName(overtimeApplication1.getAuditors());
                    overtimeCheck.setAuditorStatus(overtimeApplication1.getAuditorStatuses());
                    overtimeCheck.setAuditorRemarks(overtimeApplication1.getRemarks());
                    overtimeCheck.setThroughTime(overtimeApplication1.getThroughTime());
                    list.add(overtimeCheck);
                });
                data3.get(0).setOvertimeCheckList(list);
                data2.add(data3.get(0));
            });
            return new ResponseJson(data2.get(0));
        }else {
            return new ResponseJson(false,"该加班申请已撤销");
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

    public String dateToString(String s) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d1=simpleDateFormat.parse(s);
        String date1=simpleDateFormat.format(d1);
        return date1;
    }

    @PostMapping("/pushMsg")
    public ResponseJson pushMsg(){
        PushMsg pushMsg=new PushMsg();
        pushMsg.setApplicantId(myId());
        List<PushMsg> pushMsgList = pushMsgService.findPushMsgListByCondition(pushMsg);
        pushMsgService.deletePushMsgByCondition(pushMsg);
        return new ResponseJson(pushMsgList);
    }

    @GetMapping("/exportOvertime/{dataTime}")
    public void exportOvertime(HttpServletResponse response,@PathVariable String dataTime) {

        // 告诉浏览器用什么软件可以打开此文件
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=overtime.xls");
        try (ServletOutputStream s = response.getOutputStream()) {
            Workbook workbook = overtimeApplicationService.exportOvertime(dataTime);
            workbook.write(s);
        } catch (Exception e) {

        }
    }


    @GetMapping("/clearOvertimes")
    public void clearOvertimes(){
        overtimeApplicationService.ClearOvertime();
        breakoffApplicationService.ClearBreakoff();
    }

    @PostMapping("/myOvertimeRecord")
    public ResponseJson myOvertimeRecord(@RequestBody OvertimeApplication overtimeApplication){
        overtimeApplication.setApplicantId(myId());
        List<OvertimeApplication> overtimeApplicationList = overtimeApplicationService.myOvertimerecord(overtimeApplication);
        Map<String, List<OvertimeApplication>> data = overtimeApplicationList.stream().collect(Collectors.groupingBy(OvertimeApplication::getId));
        List<OvertimeApplication>data2=new ArrayList<>();
        data.forEach((k,v)->{
            List<OvertimeApplication> data3=v;
            for (int i = 0; i <data3.size() ; i++) {
                  if(data3.get(i).getNextSort()==-1){
                      data2.add(data3.get(i));
                  }
            }
        });

        Map<String,Object>m1=new HashMap<>();
        data2.stream().forEach(e->{
            OvertimeRecord overtimeRecord=new OvertimeRecord();
            overtimeRecord.setId(e.getId());
            overtimeRecord.setTimeRange(e.getStartTime().split(" ")[1]+"-"+e.getEndTime().split(" ")[1]);
            //判断状态
            if(e.getAuditorStatuses().equals("0")){
                overtimeRecord.setStatus("0");           //领导未审核
            }
            if(e.getAuditorStatuses().equals("1")){
                overtimeRecord.setStatus("1");           //领导已审核
            }
            if(e.getStatus().equals("1")){
                overtimeRecord.setStatus("2");           //人事已审核
            }
            if(e.getAuditorStatuses().equals("2")||e.getStatus().equals("2")){
                overtimeRecord.setStatus("3");           //领导，人事否决
            }
            try {
                m1.put(dateToString(e.getStartTime()),overtimeRecord);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        });
        return  new ResponseJson(m1);
    }
}
