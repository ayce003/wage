package com.ycjd.payslip.service.wage;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.hutool.core.date.DateUtil;
import com.ycjd.payslip.dao.wage.IWageTypeDao;
import com.ycjd.payslip.dao.wage.IWageTypeWorkerDao;
import com.ycjd.payslip.dao.wage.IWageValueDao;
import com.ycjd.payslip.dao.worker.IWorkerDao;
import com.ycjd.payslip.pojo.SequenceId;
import com.ycjd.payslip.pojo.wage.WageType;
import com.ycjd.payslip.pojo.wage.WageTypeWorker;
import com.ycjd.payslip.pojo.wage.WageValue;
import com.ycjd.payslip.pojo.worker.Worker;
import com.ycjd.payslip.util.SaltUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class WageTypeWorkerService {
    @Autowired
    private IWageTypeWorkerDao wageTypeWorkerDao;
    @Autowired
    private SequenceId sequenceId;
    @Autowired
    private IWageValueDao wageValueDao;
    @Autowired
    private IWorkerDao workerDao;
    @Autowired
    private IWageTypeDao wageTypeDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Transactional(readOnly = true)
    public WageTypeWorker findWageTypeWorkerById(String id) {
        return wageTypeWorkerDao.findWageTypeWorkerById(id);
    }

    @Transactional
    public void saveWageTypeWorker(WageTypeWorker wageTypeWorker) {
        wageTypeWorker.setId(sequenceId.nextId());
        wageTypeWorkerDao.saveWageTypeWorker(wageTypeWorker);
    }

    @Transactional(readOnly = true)
    public List<WageTypeWorker> findWageTypeWorkerListByCondition(WageTypeWorker wageTypeWorker) {
        return wageTypeWorkerDao.findWageTypeWorkerListByCondition(wageTypeWorker);
    }

    @Transactional(readOnly = true)
    public List<WageTypeWorker> findWageTypeWorkerListByWorkNum(WageTypeWorker wageTypeWorker) {
        return wageTypeWorkerDao.findWageTypeWorkerListByWorkNum(wageTypeWorker);
    }

    @Transactional(readOnly = true)
    public WageTypeWorker findOneWageTypeWorkerByCondition(WageTypeWorker wageTypeWorker) {
        return wageTypeWorkerDao.findOneWageTypeWorkerByCondition(wageTypeWorker);
    }

    @Transactional(readOnly = true)
    public long findWageTypeWorkerCountByCondition(WageTypeWorker wageTypeWorker) {
        return wageTypeWorkerDao.findWageTypeWorkerCountByCondition(wageTypeWorker);
    }

    @Transactional
    public void updateWageTypeWorker(WageTypeWorker wageTypeWorker) {
        wageTypeWorkerDao.updateWageTypeWorker(wageTypeWorker);
    }

    @Transactional
    public void deleteWageTypeWorker(String id) {
        wageTypeWorkerDao.deleteWageTypeWorker(id);
    }

    @Transactional
    public void deleteWageTypeWorkerByCondition(WageTypeWorker wageTypeWorker) {
        wageTypeWorkerDao.deleteWageTypeWorkerByCondition(wageTypeWorker);
    }
   /* @Transactional
    public void batchSaveWageTypeWorker(List<WageTypeWorker> wageTypeWorkers){
        wageTypeWorkers.forEach(wageTypeWorker -> wageTypeWorker.setId(sequenceId.nextId()));
        wageTypeWorkerDao.batchSaveWageTypeWorker(wageTypeWorkers);
    }*/


    @Transactional
    public void saveWageTypeValue(WageTypeWorker wageTypeWorker) {
        wageTypeWorker.setId(sequenceId.nextId());
        wageTypeWorker.setReleaseState("0");
        wageTypeWorkerDao.saveWageTypeWorker(wageTypeWorker);
        for (Map.Entry<String, Object> entry : wageTypeWorker.getPropmap().entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            Map<String, Object> m1 = SaltUtil.base64Encode(value);
            WageValue wageValue = new WageValue();
            wageValue.setId(sequenceId.nextId());
            wageValue.setWageAttributeId(key);
            wageValue.setSalt(m1.get("salt").toString());
            wageValue.setValueSize(m1.get("result").toString());
            wageValue.setRecordId(wageTypeWorker.getId());
            wageValueDao.saveWageValue(wageValue);
        }
    }

    @Transactional
    public void updateWageTypeValue(WageTypeWorker wageTypeWorker) {


        wageTypeWorkerDao.updateWageTypeWorker(wageTypeWorker);
        wageValueDao.deleteWageValueByRecordId(wageTypeWorker.getId());
        for (Map.Entry<String, Object> entry : wageTypeWorker.getPropmap().entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            Map<String, Object> m1 = SaltUtil.base64Encode(value);
            WageValue wageValue = new WageValue();
            wageValue.setId(sequenceId.nextId());
            wageValue.setWageAttributeId(key);
            wageValue.setSalt(m1.get("salt").toString());
            wageValue.setValueSize(m1.get("result").toString());
            wageValue.setRecordId(wageTypeWorker.getId());
            wageValueDao.saveWageValue(wageValue);
        }
    }


    @Transactional(readOnly = true)
    public List<Map<String, Object>> findWageValueByTypeId(Map<String, Object> wageTypeWorkerMap) {
        return wageTypeWorkerDao.findWageValueByTypeId(wageTypeWorkerMap);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> findWageValueByRecordAndTypeId(Map<String, Object> wageTypeWorkerMap) {
        return wageTypeWorkerDao.findWageValueByRecordAndTypeId(wageTypeWorkerMap);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> findWageValueByEamilAndTypeId(Map<String, Object> wageTypeWorkerMap) {
        return wageTypeWorkerDao.findWageValueByEamilAndTypeId(wageTypeWorkerMap);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> findWageValueByTelAndTypeId(Map<String, Object> wageTypeWorkerMap) {
        return wageTypeWorkerDao.findWageValueByTelAndTypeId(wageTypeWorkerMap);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> findWageValueByRecordId(Map<String, Object> wageTypeRecordMap) {
        return wageTypeWorkerDao.findWageValueByRecordId(wageTypeRecordMap);
    }

    /*@Transactional(readOnly = true)
    public List<Map<String, Object>> findWageValueByWorkerId(Map<String, Object> wageTypeWorkerMap) {
        return wageTypeWorkerDao.findWageValueByWorkerId(wageTypeWorkerMap);
    }*/

    @Transactional(readOnly = true)
    public List<WageTypeWorker> findWageAttributeListByRecordId(WageTypeWorker wageTypeWorker) {
        return wageTypeWorkerDao.findWageAttributeListByRecordId(wageTypeWorker);
    }


    @Transactional
    public void deleteWageValueByRecordId(String id) {
        wageTypeWorkerDao.deleteWageTypeWorker(id);
        wageValueDao.deleteWageValueByRecordId(id);
    }

    @Transactional
    public void updateWageTypeWorkerReleaseState(WageTypeWorker wageTypeWorker) {
        List<WageTypeWorker> wageTypeWorkers = new ArrayList<>();
        for (int i = 0; i < wageTypeWorker.getSelAllWorkId().length; i++) {
            Date date = DateUtil.date();
            String nowTime = DateUtil.format(date, "yyyy-MM-dd HH:mm");
            wageTypeWorker.setId(wageTypeWorker.getSelAllWorkId()[i]);
            wageTypeWorker.setReleaseState("1");
            wageTypeWorker.setReleaseTime(nowTime);
            wageTypeWorkerDao.updateWageTypeWorker(wageTypeWorker);
            WageTypeWorker wageWorker = wageTypeWorkerDao.findWageworkerIdByRecordId(wageTypeWorker); //获取员工(工资)对象
            wageTypeWorkers.add(wageWorker);
        }
      /*  if (wageTypeWorkers.size() > 0) { //获取员工id，对教师进行推送通知
            //处理推送
            final Push push = Push.newBuilder(JpushApp.TAP)
                    .setAlias(wageTypeWorkers.stream().map(WageTypeWorker::getWorkerId).toArray(String[]::new))
                    .setNotification(Push.Notification.newBuilder().setAlert("工资通知").setExtras(Extras.newBuilder().setType(Extras.TAP_Worker_WAGE).build()).setSound(Constant.JPUSH.SOUND_1).build())
                    .build();
            stringRedisTemplate.convertAndSend(Constant.MCS_CHANEL.JPUSH_SEND_PUSH, JSONUtil.toJsonStr(push));
        }*/
    }

    @Transactional(readOnly = true)
    public long findWageValueByTypeIdCount(WageTypeWorker wageTypeWorker) {
        return wageTypeWorkerDao.findWageValueByTypeIdCount(wageTypeWorker);
    }
    @Transactional(readOnly = true)
    public long findWageValueByWorkerIdCount(WageTypeWorker wageTypeWorker) {
        return wageTypeWorkerDao.findWageValueByWorkerIdCount(wageTypeWorker);
    }

    public List<WageTypeWorker> findWageAttributeListByWorkerId(WageTypeWorker wageTypeWorker) {
        return wageTypeWorkerDao.findWageAttributeListByWorkerId(wageTypeWorker);
    }

    public List<Map<String, String>> findWageValueByWorkerId(Map<String,Object>wageTypeWorkerMap) {
        return wageTypeWorkerDao.findWageValueByWorkerId(wageTypeWorkerMap);
    }

    public List<WageTypeWorker> findWageAttributeNameByWorkerId(WageTypeWorker wageTypeWorker) {
        return wageTypeWorkerDao.findWageAttributeNameByWorkerId(wageTypeWorker);
    }

    @Transactional
    public Map<String, Object> batchSaveWageTypeWorker(List<WageTypeWorker> wageTypeWorkerList) {
        Map<String, Object> result = new HashMap<>();
        Worker Worker = new Worker();
        List<Worker> WorkerList = workerDao.findWorkerListByCondition(Worker);
        for (int i = 0; i < wageTypeWorkerList.size(); i++) {
            WageTypeWorker wageTypeWorker = new WageTypeWorker();
            wageTypeWorker.setWorkNumber(wageTypeWorkerList.get(i).getWorkNumber());
            wageTypeWorker.setName(wageTypeWorkerList.get(i).getName());
            wageTypeWorker.setId(sequenceId.nextId());
            wageTypeWorker.setWageTypeId(wageTypeWorkerList.get(i).getWageTypeId());
            wageTypeWorker.setReleaseState("0");
            wageTypeWorker.setSalaryTime(wageTypeWorkerList.get(i).getSalaryTime());
            for (int j = 0; j < WorkerList.size(); j++) {
                if (wageTypeWorkerList.get(i).getName().equals(WorkerList.get(j).getName()) && wageTypeWorkerList.get(i).getWorkNumber().equals(WorkerList.get(j).getWorkNumber())) {
                    wageTypeWorker.setWorkerId(WorkerList.get(j).getId());
                }
            }
            wageTypeWorkerDao.saveWageTypeWorker(wageTypeWorker);
            for (Map.Entry<String, Object> entry : wageTypeWorkerList.get(i).getPropmap().entrySet()) {
                String key = entry.getKey().toString();
                String value = entry.getValue().toString();
                Map<String, Object> m1 = SaltUtil.base64Encode(value);
                WageValue wageValue = new WageValue();
                wageValue.setId(sequenceId.nextId());
                wageValue.setWageAttributeId(key);
                wageValue.setSalt(m1.get("salt").toString());
                wageValue.setValueSize(m1.get("result").toString());
                wageValue.setRecordId(wageTypeWorker.getId());
                wageValueDao.saveWageValue(wageValue);
            }
        }
        return result;
    }

    public Workbook exportWageTemplate(WageTypeWorker wageTypeWorker){
        WageType wageType=new WageType();
        wageType.setId(wageTypeWorker.getWageTypeId());
        List<WageType> wageAttributeList = wageTypeDao.findWageAttributeListByTypeId(wageType);
        List<ExcelExportEntity> exportEntityList=new ArrayList<>();
        List<Map<String,Object>>wageValueList=new ArrayList<>();
        Map<String,Object>map=new HashMap<>();
        exportEntityList.add(new ExcelExportEntity("工号","work_number"));
        exportEntityList.add(new ExcelExportEntity("职工姓名","name"));
        exportEntityList.add(new ExcelExportEntity("工资月份","salary_time"));
        map.put("work_number","例如:10001");
        map.put("name","例如:张三");
        map.put("salary_time","例如:2019-01");
        for (int i = 0; i <wageAttributeList.size() ; i++) {
            exportEntityList.add(new ExcelExportEntity(wageAttributeList.get(i).getAttributeName(),wageAttributeList.get(i).getWageAttributeId()));
            map.put(wageAttributeList.get(i).getWageAttributeId(),"例如:7000");
        }
        wageValueList.add(map);
        return ExcelExportUtil.exportExcel(new ExportParams("工资列表","工资"),exportEntityList,wageValueList);
    }

    public Workbook exportWage(WageTypeWorker wageTypeWorker){
        WageType wageType=new WageType();
        wageType.setId(wageTypeWorker.getWageTypeId());
        List<WageType> wageAttributeList = wageTypeDao.findWageAttributeListByTypeId(wageType);
        List<ExcelExportEntity> exportEntityList=new ArrayList<>();
        exportEntityList.add(new ExcelExportEntity("工号","work_number",20));
        exportEntityList.add(new ExcelExportEntity("职工姓名","name"));
        exportEntityList.add(new ExcelExportEntity("工资月份","salary_time"));
        for (int i = 0; i <wageAttributeList.size() ; i++) {
            exportEntityList.add(new ExcelExportEntity(wageAttributeList.get(i).getAttributeName(),wageAttributeList.get(i).getWageAttributeId()));
        }
        exportEntityList.add(new ExcelExportEntity("发放时间","release_time",30));
        exportEntityList.add(new ExcelExportEntity("发放状态","release_state"));
        Map<String,Object>wageTypeMap=new HashMap<>();
        wageTypeMap.put("id",wageTypeWorker.getWageTypeId());
        wageTypeMap.put("wageTypeList",wageAttributeList);
        wageTypeMap.put("pager",wageTypeWorker.getPager());
        List<Map<String, Object>> wageValueList = wageTypeWorkerDao.findWageValueByTypeId(wageTypeMap);

        for (int i = 0; i <wageValueList.size(); i++) {
            for (Map.Entry<String, Object> entry : wageValueList.get(i).entrySet()) {
                for (int j = 0; j < wageAttributeList.size(); j++) {
                    if(wageAttributeList.get(j).getWageAttributeId().equals(entry.getKey())){
                         wageValueList.get(i).put(entry.getKey(),SaltUtil.base64Decode(wageValueList.get(i).get(entry.getKey()).toString().substring(0,wageValueList.get(i).get(entry.getKey()).toString().lastIndexOf(",")),wageValueList.get(i).get(entry.getKey()).toString().substring(wageValueList.get(i).get(entry.getKey()).toString().lastIndexOf(",")+1)));
                    }
                }
            }
        }

        for (int i = 0; i <wageValueList.size() ; i++) {
            if( wageValueList.get(i).get("release_state").equals("0")){
                wageValueList.get(i).put("release_state","未发放");
            }else{
                wageValueList.get(i).put("release_state","已发放");
            }
        }
        return ExcelExportUtil.exportExcel(new ExportParams("工资列表","工资"),exportEntityList,wageValueList);
    }


    public Map<String,Object> uploadWage(MultipartFile file, String id){
        Map<String,Object> result = new HashMap<>();
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        try (InputStream is = file.getInputStream()){
            List<Map<String,Object>> list = ExcelImportUtil.importExcel(is, Map.class, params);
            list = list.stream().filter(stringObjectMap -> !isAllFieldNull(stringObjectMap)).collect(Collectors.toList());
            if(list == null||list.size()<=0){
                result.put("size",0);
                result.put("code","201");
                result.put("error","请勿上传空文件");
                return result;
            }
            if(list.size()>10000){
                result.put("code","202");
                result.put("error","超出导入上限：10000条");
                return result;
            }
            List<String> errors = new ArrayList<>();
            String teaNumber="";
            String name="";
            WageTypeWorker wageTypeWorker=null;
            WageType wageType=null;
            Worker worker=null;
            WageValue wageValue=null;

            wageType=new WageType();
            wageType.setId(id);
            List<WageType>wageTypeList=wageTypeDao.findWageAttributeListByTypeId(wageType);

            wageTypeWorker=new WageTypeWorker();
            List<WageTypeWorker> wageTypeWorkerList = wageTypeWorkerDao.findWageTypeWorkerListByCondition(wageTypeWorker);

            worker=new Worker();
            List<Worker> workerList = workerDao.findWorkerListByCondition(worker);

            List<WageTypeWorker>wageList=new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Pattern pattern;
                int a = i + 1;//获取当前所在条数
                StringBuffer error = new StringBuffer();//异常提示
                if (list.get(0).keySet().size() - 3 < wageTypeList.size()) {
                    error.append("导入模板有误  ");
                }

                wageTypeWorker=new WageTypeWorker();
                Map<String,Object>valueMap=new HashMap<>();
                for (Map.Entry<String, Object> entry : list.get(i).entrySet()) {
                    boolean flag = false;
                    boolean flag1 = false;
                    boolean flag2 = false;
                    if(entry.getKey().equals("工号")){
                        if(StringUtils.isNotEmpty(entry.getValue() == null ? null : entry.getValue().toString())) {
                            teaNumber=entry.getValue().toString();
                            wageTypeWorker.setWorkNumber(teaNumber);
                        }else {
                            error.append("工号不能为空；");
                        }
                        continue;
                    }
                    if(entry.getKey().equals("职工姓名")){
                        if(StringUtils.isNotEmpty(entry.getValue() == null ? null : entry.getValue().toString())) {
                            name=entry.getValue().toString();
                            wageTypeWorker.setName(name);
                        }else{
                            error.append("职工姓名不能为空；");
                        }
                        for (Worker workers:workerList) {
                            if(teaNumber.equals(workers.getWorkNumber())&&name.equals(workers.getName())){
                                flag2=true;
                            }
                        }
                        if(!flag2){
                            error.append("该职工不存在；");
                        }
                        continue;
                    }


                    if(entry.getKey().equals("工资月份")){
                        if(StringUtils.isNotEmpty(entry.getValue() == null ? null : entry.getValue().toString())) {
                            pattern = Pattern.compile("\\d{4}-(0[1-9]|1[0-2])");
                            if (!pattern.matcher(entry.getValue().toString()).matches()) {
                                error.append("工资月份格式：2018-01（文本类型）；");
                            }else {
                                wageTypeWorker.setSalaryTime(entry.getValue().toString());
                            }
                        }else {
                            error.append("工资月份格式：2018-01（文本类型）；");
                        }
                        for (WageTypeWorker wageWorker:wageTypeWorkerList) {
                            if(teaNumber.equals(wageWorker.getWorkNumber())&&name.equals(wageWorker.getName())&&entry.getValue().equals(wageWorker.getSalaryTime())){
                                flag1=true;
                            }
                        }
                        if(flag1){
                            error.append("工号已存在；");
                        }
                        continue;
                    }


                    for (int j = 0; j < wageTypeList.size(); j++) {
                        if(wageTypeList.get(j).getAttributeName().equals(entry.getKey())){
                            flag=true;
                            if(StringUtils.isNotEmpty(entry.getValue() == null ? null : entry.getValue().toString())) {
                                pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");
                                if(!pattern.matcher(entry.getValue().toString()).matches()){
                                    error.append(entry.getKey()+"格式错误(最多保留小数点两位)");
                                }else{
                                    valueMap.put(wageTypeList.get(j).getWageAttributeId(), entry.getValue().toString());
                                    wageTypeWorker.setPropmap(valueMap);
                                }
                            }else{
                                error.append(entry.getKey()+"不能为空；");
                            }
                        }
                    }
                    if(!flag){
                        error.append(entry.getKey()+"属性不存在;  ");
                    }
                    wageTypeWorker.setWageTypeId(id);
                }
                wageList.add(wageTypeWorker);
                //异常添加list
                if (error.length() > 0) {
                    error.insert(0, "第" + a + "条:");
                    errors.add(error.toString());
                }
            }
            if (errors.size() <= 0) {
                this.batchSaveWageTypeWorker(wageList);
                result.put("code", "200");
            } else {
                result.put("code", "222");
                result.put("errors", errors);
            }
        }catch (Exception e){
            System.out.println(e);
        }

        return result;
    }
    private boolean isAllFieldNull(Map<String,Object> map){
        boolean allFiedNull = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if(entry.getValue() != null && !StringUtils.isBlank(entry.getValue() + "")){
                allFiedNull=false;
                break;
            }
        }
        return  allFiedNull;
    }
}
