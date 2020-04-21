package com.ycjd.payslip.controller.wage;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.ycjd.payslip.pojo.Constant;
import com.ycjd.payslip.pojo.ResponseJson;
import com.ycjd.payslip.pojo.mail.MailVo;
import com.ycjd.payslip.pojo.wage.WageType;
import com.ycjd.payslip.pojo.wage.WageTypeWorker;
import com.ycjd.payslip.pojo.worker.Worker;
import com.ycjd.payslip.service.mail.MailService;
import com.ycjd.payslip.service.wage.WageTypeService;
import com.ycjd.payslip.service.wage.WageTypeWorkerService;
import com.ycjd.payslip.service.worker.WorkerService;
import com.ycjd.payslip.util.AliMsn;
import com.ycjd.payslip.util.Msn;
import com.ycjd.payslip.util.SaltUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.ycjd.payslip.interceptor.LoginInterceptor.myId;

@RestController
@RequestMapping("/wageTypeWorker")
@Api(value = "/wageTypeWorker",description = "模块")
public class WageTypeWorkerController {
    private Logger logger = LoggerFactory.getLogger(getClass());//提供日志类
    @Autowired
    private WageTypeWorkerService wageTypeWorkerService;
    @Autowired
    private WorkerService workerService;
    @Autowired
    private WageTypeService wageTypeService;
    @Autowired
    private MailService mailService;
    private  ExecutorService executorService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private AliMsn aliMsn;
   /* @Autowired
    private AmqpTemplate rabbitTemplate;*/

    @PostMapping("/saveWageTypeWorker")
    @ApiOperation(value = "保存对象", notes = "返回响应对象")
    public ResponseJson saveWageTypeWorker(
            @ApiParam(value = "对象", required = true)
            @RequestBody WageTypeWorker wageTypeWorker){
       wageTypeWorkerService.saveWageTypeWorker(wageTypeWorker);
        return new ResponseJson();
    }

    @GetMapping("/update/findWageTypeWorkerById/{id}")
    @ApiOperation(value = "去更新页面,通过id查找", notes = "返回响应对象")
    public ResponseJson findWageTypeWorkerById(
            @ApiParam(value = "去更新页面,需要用到的id", required = true)
            @PathVariable String id){
        WageTypeWorker wageTypeWorker=wageTypeWorkerService.findWageTypeWorkerById(id);
        return new ResponseJson(wageTypeWorker);
    }

    @PostMapping("/update/updateWageTypeWorker")
    @ApiOperation(value = "修改对象", notes = "返回响应对象")
    public ResponseJson updateWageTypeWorker(
            @ApiParam(value = "被修改的对象,对象属性不为空则修改", required = true)
            @RequestBody WageTypeWorker wageTypeWorker){
        wageTypeWorkerService.updateWageTypeWorker(wageTypeWorker);
        return new ResponseJson();
    }

    @GetMapping("/look/lookWageTypeWorkerById/{id}")
    @ApiOperation(value = "去查看页面,通过id查找", notes = "返回响应对象")
    public ResponseJson lookWageTypeWorkerById(
            @ApiParam(value = "去查看页面,需要用到的id", required = true)
            @PathVariable String id){
        WageTypeWorker wageTypeWorker=wageTypeWorkerService.findWageTypeWorkerById(id);
        return new ResponseJson(wageTypeWorker);
    }

    @PostMapping("/findWageTypeWorkersByCondition")
    @ApiOperation(value = "根据条件查找", notes = "返回响应对象")
    public ResponseJson findWageTypeWorkersByCondition(
            @ApiParam(value = "属性不为空则作为条件查询")
            @Validated
            @RequestBody WageTypeWorker wageTypeWorker){
        List<WageTypeWorker> data=wageTypeWorkerService.findWageTypeWorkerListByCondition(wageTypeWorker);
        long count=wageTypeWorkerService.findWageTypeWorkerCountByCondition(wageTypeWorker);
        return new ResponseJson(data,count);
    }
    @PostMapping("/findOneWageTypeWorkerByCondition")
    @ApiOperation(value = "根据条件查找单个,结果必须为单条数据", notes = "没有时返回空")
    public ResponseJson findOneWageTypeWorkerByCondition(
            @ApiParam(value = "属性不为空则作为条件查询")
            @RequestBody WageTypeWorker wageTypeWorker){
        WageTypeWorker one=wageTypeWorkerService.findOneWageTypeWorkerByCondition(wageTypeWorker);
        return new ResponseJson(one);
    }
    @GetMapping("/deleteWageTypeWorker/{id}")
    @ApiOperation(value = "根据id删除", notes = "返回响应对象")
    public ResponseJson deleteWageTypeWorker(
            @ApiParam(value = "被删除记录的id", required = true)
            @PathVariable String id){
        wageTypeWorkerService.deleteWageTypeWorker(id);
        return new ResponseJson();
    }


    @PostMapping("/findWageTypeWorkerListByCondition")
    @ApiOperation(value = "根据条件查找列表", notes = "返回响应对象,不包含总条数")
    public ResponseJson findWageTypeWorkerListByCondition(
            @ApiParam(value = "属性不为空则作为条件查询")
            @Validated
            @RequestBody WageTypeWorker wageTypeWorker){
        List<WageTypeWorker> data=wageTypeWorkerService.findWageTypeWorkerListByCondition(wageTypeWorker);
        return new ResponseJson(data);
    }

    @PostMapping("/saveWageTypeValue")
    @ApiOperation(value = "保存对象", notes = "返回响应对象")
    public ResponseJson saveWageTypeValue(
            @ApiParam(value = "对象", required = true)
            @RequestBody WageTypeWorker wageTypeWorker){
        Worker Worker=new Worker();
        Worker.setName(wageTypeWorker.getName());
        Worker.setWorkNumber(wageTypeWorker.getWorkNumber());
        List<Worker> WorkerList = workerService.findWorkerListByCondition(Worker);
        if(WorkerList.size()==0){
            return new ResponseJson("false","该职工不存在");
        }
        List<WageTypeWorker> wageTypeWorkerList = wageTypeWorkerService.findWageTypeWorkerListByWorkNum(wageTypeWorker);
        if(wageTypeWorkerList.size()>0){
            return new ResponseJson("false","该职工信息已存在");
        }
        wageTypeWorker.setWorkerId(WorkerList.get(0).getId());
        wageTypeWorkerService.saveWageTypeValue(wageTypeWorker);
        return new ResponseJson("success","保存成功");
    }

    @PostMapping("/update/findWageValueByTypeId")//传入TypeId显示属性列表
    @ApiOperation(value = "根据条件查找列表", notes = "返回响应对象,不包含总条数")
    public ResponseJson findWageValueByTypeId(
            @ApiParam(value = "属性不为空则作为条件查询")
            @RequestBody WageTypeWorker wageTypeWorker){
        if(wageTypeWorker.getWageTypeId()==null){
            return new ResponseJson();
        }
        WageType wageType=new WageType();
        wageType.setId(wageTypeWorker.getWageTypeId());
        List<WageType>wageTypeList=wageTypeService.findWageAttributeListByTypeId(wageType);
        Map<String,Object>wageTypeWorkerMap=new HashMap<>();
        wageTypeWorkerMap.put("id",wageTypeWorker.getWageTypeId());
        wageTypeWorkerMap.put("pager",wageTypeWorker.getPager());
        wageTypeWorkerMap.put("wageTypeList",wageTypeList);
        if(wageTypeWorker.getName()!=""&&wageTypeWorker.getName()!=null){
            wageTypeWorkerMap.put("name",wageTypeWorker.getName());
        }
        if(wageTypeWorker.getReleaseState()!=""&&wageTypeWorker.getReleaseState()!=null){
            wageTypeWorkerMap.put("releaseState",wageTypeWorker.getReleaseState());
        }

        if(wageTypeWorker.getRangeTime()!=null){
            String startTime = DateUtil.format( DateUtil.beginOfDay(DateUtil.parse(wageTypeWorker.getRangeTime()[0])), "yyyy-MM-dd HH:mm");
            String endTime = DateUtil.format(DateUtil.endOfDay(DateUtil.parse(wageTypeWorker.getRangeTime()[1])), "yyyy-MM-dd HH:mm");
            wageTypeWorkerMap.put("startTime",startTime);
            wageTypeWorkerMap.put("endTime",endTime);
            wageTypeWorker.setStartTime(startTime);
            wageTypeWorker.setEndTime(endTime);
        }

        List<Map<String, Object>> data=wageTypeWorkerService.findWageValueByTypeId(wageTypeWorkerMap);
        for (int i = 0; i < data.size(); i++) {
            for (Map.Entry<String, Object> entry : data.get(i).entrySet()) {
                for (int j = 0; j < wageTypeList.size(); j++) {
                    if(wageTypeList.get(j).getWageAttributeId().equals(entry.getKey())){
                       String salt= data.get(i).get(entry.getKey()).toString().substring(data.get(i).get(entry.getKey()).toString().lastIndexOf(",")+1);
                       String value= data.get(i).get(entry.getKey()).toString().substring(0,data.get(i).get(entry.getKey()).toString().lastIndexOf(","));
                        entry.setValue(SaltUtil.base64Decode(value,salt));
                    }
                }
            }
        }
        long count=wageTypeWorkerService.findWageValueByTypeIdCount(wageTypeWorker);
        return new ResponseJson(data,count);
    }

    @PostMapping("/update/findWageValueByRecordId/{id}")//传入RecordId显示属性列表
    @ApiOperation(value = "根据条件查找列表", notes = "返回响应对象,不包含总条数")
    public ResponseJson findWageValueByRecordId(
            @ApiParam(value = "属性不为空则作为条件查询")
            @PathVariable String id){
        WageTypeWorker wageTypeWorker=new WageTypeWorker();
        wageTypeWorker.setId(id);
        List<WageTypeWorker>WageTypeRecordList=wageTypeWorkerService.findWageAttributeListByRecordId(wageTypeWorker);
        Map<String,Object>wageTypeRecordMap=new HashMap<>();
        wageTypeRecordMap.put("id",id);
        wageTypeRecordMap.put("WageTypeRecordList",WageTypeRecordList);
        List<Map<String, Object>> data=wageTypeWorkerService.findWageValueByRecordId(wageTypeRecordMap);

        for (int i = 0; i < data.size(); i++) {
            for (Map.Entry<String, Object> entry : data.get(i).entrySet()) {
                for (int j = 0; j < WageTypeRecordList.size(); j++) {
                    if(WageTypeRecordList.get(j).getWageAttributeId().equals(entry.getKey())){
                        String salt= data.get(i).get(entry.getKey()).toString().substring(data.get(i).get(entry.getKey()).toString().lastIndexOf(",")+1);
                        String value= data.get(i).get(entry.getKey()).toString().substring(0,data.get(i).get(entry.getKey()).toString().lastIndexOf(","));
                        entry.setValue(SaltUtil.base64Decode(value,salt));
                    }
                }
            }
        }

        return new ResponseJson(data);
    }

    @PostMapping("/update/updateWageTypeValue")
    @ApiOperation(value = "保存对象", notes = "返回响应对象")
    public ResponseJson updateWageTypeValue(
            @ApiParam(value = "对象", required = true)
            @RequestBody WageTypeWorker wageTypeWorker){
        Worker Worker=new Worker();
        Worker.setName(wageTypeWorker.getName());
        Worker.setWorkNumber(wageTypeWorker.getWorkNumber());
        List<Worker> WorkerList = workerService.findWorkerListByCondition(Worker);
        if(WorkerList.size()==0){
            return new ResponseJson("false","该职工不存在");
        }
        wageTypeWorkerService.updateWageTypeValue(wageTypeWorker);
        return new ResponseJson("success","修改成功");
    }

    @PostMapping("/update/exportWage")
    public void exportWage(@ApiParam(value = "工资信息对象") @RequestBody WageTypeWorker wageTypeWorker, HttpServletResponse response) {

        // 告诉浏览器用什么软件可以打开此文件
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=student.xls");
        try (ServletOutputStream s = response.getOutputStream()) {
            wageTypeWorker.getPager().setSortOrder("desc");
            wageTypeWorker.getPager().setSortField("createTime");
            wageTypeWorker.getPager().setPaging(false);
            Workbook workbook = wageTypeWorkerService.exportWage(wageTypeWorker);
            workbook.write(s);
        } catch (Exception e) {

        }
    }
    @PostMapping("/update/exportWageTemplate")
    public void exportWageTemplate(@ApiParam(value = "工资信息对象") @RequestBody WageTypeWorker wageTypeWorker, HttpServletResponse response) {
        // 告诉浏览器用什么软件可以打开此文件
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=student.xls");
        try (ServletOutputStream s = response.getOutputStream()) {
            Workbook workbook = wageTypeWorkerService.exportWageTemplate(wageTypeWorker);
            workbook.write(s);
        } catch (Exception e) {

        }
    }


    @PostMapping("/update/uploadWage")
    public ResponseJson uploadWage(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String id = request.getParameter("wageTypeId");
        return new ResponseJson(wageTypeWorkerService.uploadWage(file,id));
    }

    @GetMapping("/update/deleteWageValueByRecordId/{id}")
    @ApiOperation(value = "根据id删除", notes = "返回响应对象")
    public ResponseJson deleteWageValueByRecordId(
            @ApiParam(value = "被删除记录的id", required = true)
            @PathVariable String id){
        wageTypeWorkerService.deleteWageValueByRecordId(id);
        return new ResponseJson();
    }


    @PostMapping("/update/updateWageTypeWorkerReleaseState")
    @ApiOperation(value = "修改对象", notes = "返回响应对象")
    public ResponseJson updateWageTypeWorkerReleaseState(
            @ApiParam(value = "被修改的对象,对象属性不为空则修改", required = true)
            @RequestBody WageTypeWorker wageTypeWorker){
        wageTypeWorkerService.updateWageTypeWorkerReleaseState(wageTypeWorker);
        return new ResponseJson("success","发放成功");
    }

    @PostMapping("/sendEmailToWorker")
    @ApiOperation(value = "发送对象", notes = "返回响应对象")
    public ResponseJson sendEmailToWorker(@ApiParam(value = "被发送的对象", required = true) @RequestBody WageTypeWorker wageTypeWorker){
        WageType wageType=new WageType();
        wageType.setId(wageTypeWorker.getWageTypeId());
        List<WageType>wageTypeList=wageTypeService.findWageAttributeListByTypeId(wageType);
        Map<String,Object>wageTypeWorkerMap=new HashMap<>();
        wageTypeWorkerMap.put("typeId",wageTypeWorker.getWageTypeId());
        wageTypeWorkerMap.put("recordId",wageTypeWorker.getId());
        wageTypeWorkerMap.put("pager",wageTypeWorker.getPager());
        wageTypeWorkerMap.put("wageTypeList",wageTypeList);
        List<Map<String, Object>> data=wageTypeWorkerService.findWageValueByRecordAndTypeId(wageTypeWorkerMap);
        if(data.size()>0){
            /*  List<Map<String,Object>>salaryList=new ArrayList<>();*/
            StringBuffer stringBuffer=new StringBuffer();
            StringBuffer stringBuffer1=new StringBuffer();
            MailVo mailVo=new MailVo();
            String email="";
            for (int j = 0; j < data.size(); j++) {
                email=data.get(0).get("email").toString();
                //stringBuffer.append("职工姓名:"+data.get(0).get("name")+","+"工资月份:"+data.get(0).get("salary_time")+",");
                stringBuffer.append("<html><style>#class td{vertical-align: middle;text-align: center;}</style><body><table id=\"class\" border=\"1\" style=\"border-collapse:collapse\"><tr><td>职工姓名</td><td>工资月份</td>");
                stringBuffer1.append("<tr><td>"+data.get(0).get("name")+"</td><td>"+data.get(0).get("salary_time")+"</td>");
                for (Map.Entry<String, Object> entry : data.get(0).entrySet()) {
                    for (int i = 0; i < wageTypeList.size(); i++) {
                        if(wageTypeList.get(i).getWageAttributeId().equals(entry.getKey())){
                            //  stringBuffer.append(wageTypeList.get(i).getAttributeName()+":"+data.get(0).get(entry.getKey())+",");
                            /*salaryMsg.put(wageTypeList.get(i).getAttributeName(),wageTypeList.get(i).getAttributeName()+":"+data.get(0).get(entry.getKey()));*/
                            stringBuffer.append("<td>"+wageTypeList.get(i).getAttributeName()+"</td>");
                            stringBuffer1.append("<td>"+ SaltUtil.base64Decode(data.get(0).get(entry.getKey()).toString().substring(0,data.get(0).get(entry.getKey()).toString().lastIndexOf(",")),data.get(0).get(entry.getKey()).toString().substring(data.get(0).get(entry.getKey()).toString().lastIndexOf(",")+1))  +"</td>");
                        }
                    }

                }
            }
            String text= (stringBuffer.toString()+stringBuffer1.append("</tr></table></body></html>").toString());
            mailVo.setText(text);
            mailVo.setTo(email);
            mailVo.setSubject("工资条");

          /*  new Thread(()->{*/
                try {
                  /*  rabbitTemplate.convertAndSend("email",mailVo.getTo()+"的用户收到工资条");*/
                    mailService.sendMail(mailVo);
                }catch (Exception e){
                    logger.error(mailVo.getTo()+"发送邮件失败：",e);
                    e.printStackTrace();
                }
         /*   }).start();*/
            return new ResponseJson(true,"邮件已发送，请稍后查看");
        }else{
            return new ResponseJson(false,"该工资还未确认发放");
        }

    }

    @PostMapping("/sendAllEmailToWorker")
    @ApiOperation(value = "发送对象", notes = "返回响应对象")
    public void sendAllEmailToWorker(@ApiParam(value = "被发送的对象", required = true) @RequestBody WageTypeWorker wageTypeWorker){
        WageType wageType=new WageType();
        wageType.setId(wageTypeWorker.getWageTypeId());
        List<WageType>wageTypeList=wageTypeService.findWageAttributeListByTypeId(wageType);
        Map<String,Object>wageTypeWorkerMap=new HashMap<>();
        wageTypeWorkerMap.put("typeId",wageTypeWorker.getWageTypeId());
        /*wageTypeWorkerMap.put("allEmail",wageTypeWorker.getSelAllEmail());
        wageTypeWorkerMap.put("pager",wageTypeWorker.getPager());*/
        wageTypeWorkerMap.put("wageTypeList",wageTypeList);
        List<Map<String, Object>> data=wageTypeWorkerService.findWageValueByEamilAndTypeId(wageTypeWorkerMap);

        List<MailVo>mailVoList=new ArrayList<>();

        for (int i = 0; i <data.size(); i++) {
            MailVo mailVo=new MailVo();
            StringBuffer stringBuffer=new StringBuffer();
            StringBuffer stringBuffer1=new StringBuffer();
            String email=data.get(i).get("email").toString();
            /*StringBuffer stringBuffer=new StringBuffer();
            stringBuffer.append("职工姓名:"+data.get(i).get("name")+","+"工资月份:"+data.get(i).get("salary_time")+",");*/
            stringBuffer.append("<html><style>#class td{vertical-align: middle;text-align: center;}</style><body><table id=\"class\" border=\"1\" style=\"border-collapse:collapse\"><tr><td>职工姓名</td><td>工资月份</td>");
            stringBuffer1.append("<tr><td>"+data.get(i).get("name")+"</td><td>"+data.get(i).get("salary_time")+"</td>");
            for (Map.Entry<String, Object> entry : data.get(i).entrySet()) {
                for (int j = 0; j < wageTypeList.size(); j++) {
                    if(wageTypeList.get(j).getWageAttributeId().equals(entry.getKey())){
                        stringBuffer.append("<td>"+wageTypeList.get(j).getAttributeName()+"</td>");
                        stringBuffer1.append("<td>"+ SaltUtil.base64Decode(data.get(i).get(entry.getKey()).toString().substring(0,data.get(i).get(entry.getKey()).toString().lastIndexOf(",")),data.get(i).get(entry.getKey()).toString().substring(data.get(i).get(entry.getKey()).toString().lastIndexOf(",")+1))  +"</td>");
                    }
                }
            }
            String text= (stringBuffer.toString()+stringBuffer1.append("</tr></table></body></html>").toString());
            mailVo.setText(text);
            mailVo.setTo(email);
            mailVo.setSubject("工资条");
            mailVoList.add(mailVo);
        }
            getExecutor().execute(() ->{
                for (MailVo mailvo: mailVoList) {
                    try {
                        /*rabbitTemplate.convertAndSend("email",mailvo.getTo()+"的用户收到工资条");*/
                        mailService.sendMail(mailvo);
                    }catch (Exception e){
                        logger.error(mailvo.getTo()+"发送邮件失败：",e); //打印错误信息
                        e.printStackTrace();
                    }
                } }
            );
    }


    @PostMapping("/sendSalaryToAllTel")
    @ApiOperation(value = "发送对象", notes = "返回响应对象")
    public void sendSalaryToAllTel(@ApiParam(value = "被发送的对象", required = true) @RequestBody WageTypeWorker wageTypeWorker) {
        WageType wageType=new WageType();
        wageType.setId(wageTypeWorker.getWageTypeId());
        List<WageType>wageTypeList=wageTypeService.findWageAttributeListByTypeId(wageType);
        Map<String,Object>wageTypeWorkerMap=new HashMap<>();
        wageTypeWorkerMap.put("typeId",wageTypeWorker.getWageTypeId());
        /*wageTypeWorkerMap.put("allTel",wageTypeWorker.getSelAllTel());*/
        /*wageTypeWorkerMap.put("pager",wageTypeWorker.getPager());*/
        wageTypeWorkerMap.put("wageTypeList",wageTypeList);
        List<Map<String, Object>> data=wageTypeWorkerService.findWageValueByTelAndTypeId(wageTypeWorkerMap);
        List<Msn> msnList=new ArrayList<>();
        for (int i = 0; i <data.size() ; i++) {
            Msn msn=new Msn();
            String tel=data.get(i).get("tel").toString();
            StringBuffer stringBuffer=new StringBuffer();
            stringBuffer.append("\n"+"职工姓名:"+data.get(i).get("name")+"\n"+"工资月份:"+data.get(i).get("salary_time")+"\n");
            for (Map.Entry<String, Object> entry : data.get(i).entrySet()) {
                for (int j = 0; j < wageTypeList.size(); j++) {
                    if(wageTypeList.get(j).getWageAttributeId().equals(entry.getKey())){
                        stringBuffer.append(wageTypeList.get(j).getAttributeName()+":"+SaltUtil.base64Decode(data.get(i).get(entry.getKey()).toString().substring(0,data.get(i).get(entry.getKey()).toString().lastIndexOf(",")),data.get(i).get(entry.getKey()).toString().substring(data.get(i).get(entry.getKey()).toString().lastIndexOf(",")+1))+"\n");
                    }
                }
            }
            String code=stringBuffer.toString();
            msn.setCode(code);
            msn.setTel(tel);
            msn.setProduct("亿策");
            msnList.add(msn);
        }
        for (Msn msn:msnList) {
            try {
              /*  rabbitTemplate.convertAndSend("tel",msn.getTel()+"的手机号收到工资条");*/
                aliMsn.sendVerifySms(msn);
            } catch (Exception e) {
                logger.error(msn.getTel()+"的手机号发送工资条失败",e);
                e.printStackTrace();
            }
        }
    }

    @PostMapping("/findWageAttributeNameByWorkerId")
    @ApiOperation(value = "根据条件查找列表", notes = "返回列表")
    public ResponseJson findWageAttributeNameByWorkerId(
            @ApiParam(value = "对象")
            @RequestBody WageTypeWorker wageTypeWorker){
        wageTypeWorker.setWorkerId(myId());
        List<WageTypeWorker> date = wageTypeWorkerService.findWageAttributeNameByWorkerId(wageTypeWorker);
        return new ResponseJson(date);
    }


    @PostMapping("/FindMyPayslipByWorkerId")
    @ApiOperation(value = "查询对象", notes = "返回响应对象")
    public ResponseJson FindMyPayslipByWorkerId(
            @ApiParam(value = "被查询的对象,对象属性不为空则修改", required = true)
            @RequestBody WageTypeWorker wageTypeWorker){
        List<Map<String, String>> data=new ArrayList<>();
        wageTypeWorker.setWorkerId(myId());
        List<WageTypeWorker>WageTypeWorkerList=wageTypeWorkerService.findWageAttributeListByWorkerId(wageTypeWorker);
        if(WageTypeWorkerList.size()>0){
            Map<String,Object>wageTypeWorkerMap=new HashMap<>();
            wageTypeWorkerMap.put("id",myId());
            wageTypeWorkerMap.put("salaryTime",wageTypeWorker.getSalaryTime());
            wageTypeWorkerMap.put("pager",wageTypeWorker.getPager());
            wageTypeWorkerMap.put("WageTypeWorkerList",WageTypeWorkerList);
            data=wageTypeWorkerService.findWageValueByWorkerId(wageTypeWorkerMap);
            for (int i = 0; i < data.size() ; i++) {
                for (Map.Entry<String, String> entry : data.get(i).entrySet()) {
                    for (int j = 0; j < WageTypeWorkerList.size(); j++) {
                        if(WageTypeWorkerList.get(j).getWageAttributeId().equals(entry.getKey())){
                            data.get(i).put(entry.getKey(),SaltUtil.base64Decode(data.get(i).get(entry.getKey()).toString().substring(0,data.get(i).get(entry.getKey()).toString().lastIndexOf(",")),data.get(i).get(entry.getKey()).toString().substring(data.get(i).get(entry.getKey()).toString().lastIndexOf(",")+1)));
                        }
                    }
                }
            }
        }
        long count=wageTypeWorkerService.findWageValueByWorkerIdCount(wageTypeWorker);
        return new ResponseJson(data,count);
    }


    /**
     * 定义线程池
     */
    public  ExecutorService getExecutor() {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(1);
        }
        return executorService;
    }
}
