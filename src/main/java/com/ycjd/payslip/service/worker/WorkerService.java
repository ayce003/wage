package com.ycjd.payslip.service.worker;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.hutool.core.util.PinyinUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.ycjd.payslip.dao.worker.IWorkerDao;
import com.ycjd.payslip.pojo.Constant;
import com.ycjd.payslip.pojo.Pager;
import com.ycjd.payslip.pojo.SequenceId;
import com.ycjd.payslip.pojo.perm.Role;
import com.ycjd.payslip.pojo.worker.Dept;
import com.ycjd.payslip.pojo.worker.Post;
import com.ycjd.payslip.pojo.worker.Worker;
import com.ycjd.payslip.service.perm.RoleService;
import io.netty.util.internal.StringUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class WorkerService {
    @Autowired
    private IWorkerDao workerDao;
    @Autowired
    private SequenceId sequenceId;

    @CreateCache(name= Constant.Redis.DEPT_CACHE)
    private Cache<String,Dept> deptCache;


    @Autowired
    private DeptService deptService;

    @Autowired
    private PostService postService;

    @Autowired
    private RoleService roleService;

    @Transactional(readOnly = true)
    public Worker findWorkerById(String id) {
        Worker worker = workerDao.findWorkerById(id);
        if(worker!=null){
            Dept dept = deptCache.get(worker.getDepartmentId());
            if(dept!=null){
                String[] pathArr = dept.getPath().split(";");
                worker.setDeptPath(pathArr);
            }
            worker.setStatusType(worker.getStatus()==0?false:true);
        }
        return worker;
    }

    @Transactional
    public String saveWorker(Worker worker) {
        String id = sequenceId.nextId();
        //设置id
        worker.setId(id);
        //设置默认启用状态
        worker.setStatus(worker.getStatusType()==true?1:0);
        //设置初始密码
        String s = DigestUtil.md5Hex(Constant.INITIAL_PASSWORD);
        String s1 = DigestUtil.sha1Hex(s);
        worker.setPassword(s1);
        //设置删除状态
        worker.setDel(0);
        //设置默认角色
        Role r = new Role();
        r.setDefaultRoleStatus(Constant.DEFAULT_ROLE_STATUS);
        Role role = roleService.findOneRoleByCondition(r);
        if(role!=null&&role.getId()!=null){
            Map<String,String> map=new HashMap<>();
            map.put("adminId",id);
            map.put("roleIds",role.getId());
            roleService.delsertAdminRoles(map);
            workerDao.saveWorker(worker);
            return "0";
        }else {
            return "1";
        }

    }

    @Transactional(readOnly = true)
    public List<Worker> findWorkerListByCondition(Worker worker) {
        return workerDao.findWorkerListByCondition(worker);
    }

    @Transactional(readOnly = true)
    public Worker findOneWorkerByCondition(Worker worker) {
        return workerDao.findOneWorkerByCondition(worker);
    }

    @Transactional(readOnly = true)
    public long findWorkerCountByCondition(Worker worker) {
        return workerDao.findWorkerCountByCondition(worker);
    }

    @Transactional
    public void updateWorker(Worker worker) {
        if(worker.getStatusType()!=null){
            worker.setStatus(worker.getStatusType()==true?1:0);
        }
        workerDao.updateWorker(worker);
    }

    @Transactional
    public void deleteWorker(String id) {
        workerDao.deleteWorker(id);
    }

    @Transactional
    public void deleteWorkerByCondition(Worker worker) {
        workerDao.deleteWorkerByCondition(worker);
    }

    @Transactional
    public void batchSaveWorker(List<Worker> workers,String roleId) {
        workers.forEach(worker -> {
            String id = sequenceId.nextId();
            worker.setId(id);
            Map<String,String> map = new HashMap<>();
            map.put("adminId",id);
            map.put("roleIds",roleId);
            roleService.delsertAdminRoles(map);
        });
        workerDao.batchSaveWorker(workers);
    }


    @Transactional
    public List<Worker> findWorkerListByCondition2(Worker worker) {

        List<Worker> workerList = workerDao.findWorkerListByCondition2(worker);

        workerList.forEach(w ->{

            String subDeptName = this.getSubDeptName(w.getDepartmentId());
            w.setDeptName(subDeptName);
            w.setStatusType(w.getStatus()==0?false:true);
        } );

        return workerList;

    }

    public String getSubDeptName(String deptId){
        Dept dept = deptCache.get(deptId);
        if(dept!=null&&dept.getPath()!=null){
            String[] pathArr = dept.getPath().split(";");
            String subDeptName = "";
            for (int i = 0; i <pathArr.length; i++) {

                Dept d = deptCache.get(pathArr[i]);
                if(d==null){
                    d = deptService.findDeptById(pathArr[i]);
                    deptCache.put(pathArr[i],d);
                }
                if(i==pathArr.length-1){
                    subDeptName +=d.getDeptName();
                }else {
                    subDeptName +=d.getDeptName()+"/";
                }
            }
           return subDeptName;
        }else {
            Dept deptById = deptService.findDeptById(deptId);
            if(deptById!=null){

                String[] pathArr = deptById.getPath().split(";");
                String subDeptName = "";
                for (int i = 0; i <pathArr.length; i++) {
                    Dept d = deptService.findDeptById(pathArr[i]);
                    if(i==pathArr.length-1){
                        subDeptName +=d.getDeptName();
                    }else {
                        subDeptName +=d.getDeptName()+"/";
                    }
                }
                deptCache.put(deptId,deptById);
                return subDeptName;

            }

        }

        return null;
    }

    public Workbook exportTemplate() {      // 导入员工模板
        List<Worker> workerList = new ArrayList<>();
        Worker worker = new Worker();
        worker.setWorkNumber("员工工号");
        worker.setName("姓名");
        worker.setSex("男/女");
        worker.setAge("员工年龄");
        worker.setTel("11位数字-手机号");
        worker.setEmail("电子邮件格式必须正确");
        worker.setDeptName("部门名称,父子部门格式,如:销售部/销售一部");
        worker.setPostName("岗位名称");
        workerList.add(worker);
        return ExcelExportUtil.exportExcel(new ExportParams("员工导入模板", "员工"),
                Worker.class, workerList);
    }

    public Map<String, Object> uploadStudent(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        try (InputStream is = file.getInputStream()) {
            List<Worker> list = ExcelImportUtil.importExcel(is,
                    Worker.class, params);
            list = list.stream().filter(s1 -> !isAllFieldNull(s1)).collect(Collectors.toList());
            final List<Worker> workerList = new ArrayList<>();
            workerList.addAll(list);
            List<String> errors = new ArrayList<>();


            final List<Dept> data = new ArrayList<>();//部门list
            final List<Post> data2 = new ArrayList<>();//岗位list
            final List<Worker> data3 = new ArrayList<>();//账号list
            if (workerList.size() > 10000) {
                result.put("code", "202");  //导入数量超过10000条
                result.put("error", "超出导入上限：10000条");
                return result;
            } else if (workerList.size() == 0) {
                result.put("code", "201"); //导入为空
                result.put("error", "请勿上传空文件");
                return result;
            }


            //一次查出所有的部门,岗位减少查库次数

            //设置查询时不分页
            Pager pager = new Pager();
            pager.setPaging(false);

            Dept dept = new Dept();

            dept.setPager(pager);
            data.addAll(deptService.findDeptListByCondition(dept));

            Post post = new Post();
            post.setPager(pager);
            data2.addAll(postService.findPostListByCondition(post));

            Worker worker = new Worker();
            worker.setPager(pager);
            data3.addAll(this.findWorkerListByCondition(worker));

            //查询是否存在默认角色
            //设置默认角色
            Role r = new Role();
            r.setDefaultRoleStatus(Constant.DEFAULT_ROLE_STATUS);
            Role role = roleService.findOneRoleByCondition(r);

            //判断是否有默认角色
            if(role==null||role.getId()==null){
                result.put("code","403");
            }else {
                List<Worker> data4 = workerList.stream().filter(w -> !(w.getWorkNumber() == null) && !(w.getWorkNumber().equals(""))).collect(Collectors.toList());
                workerList.forEach(s -> {

                    Pattern pattern;
                    int i = workerList.indexOf(s) + 1;//获取当前所在条数
                    StringBuffer error = new StringBuffer();//异常提示
                    //设置初始密码
                    s.setPassword(DigestUtil.sha1Hex(DigestUtil.md5Hex(Constant.INITIAL_PASSWORD)));
                    //设置默认状态
                    s.setStatus(1);
                    //设置删除状态
                    s.setDel(0);

                    if (StringUtil.isNullOrEmpty(s.getName())) {
                        error.append("姓名不能为空；");
                    }
                    if (!StringUtil.isNullOrEmpty(s.getSex()) && !(s.getSex().equals(Constant.SEX_TYPE.MAN) || s.getSex().equals(Constant.SEX_TYPE.WOMAN))) {
                        error.append("性别只能填写男或者女；");
                    } else if (StringUtil.isNullOrEmpty(s.getSex())) {
                        error.append("性别不能为空");
                    }

                    if (StringUtil.isNullOrEmpty(s.getTel())) {
                        error.append("联系方式必须填写；");
                    } else {
                        pattern = Pattern.compile("^1\\d{10}$");
                        if (!pattern.matcher(s.getTel()).matches()) {
                            error.append("联系方式必须11位国内手机号；");
                        }
                    }

                    if (!StringUtil.isNullOrEmpty(s.getEmail())) {
                        pattern = Pattern.compile("^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}$");
                        if (!pattern.matcher(s.getEmail()).matches()) {
                            error.append("请正确填写邮箱；");
                        }
                    }



                    if (!StringUtil.isNullOrEmpty(s.getDeptName())) {
                        boolean flag = true;
                        for (Dept d : data) {
                            String subDeptName = this.getSubDeptName(d.getId());
                            if (subDeptName.equals(s.getDeptName())) {
                                s.setDepartmentId(d.getId());
                                flag = false;
                            }
                        }
                        if (flag) {
                            error.append("请正确填写部门名称；");
                        }
                    }

                    if (!StringUtil.isNullOrEmpty(s.getPostName())) {
                        boolean flag = true;
                        for (Post p : data2) {
                            if (p.getPostName().equals(s.getPostName())) {
                                s.setPostId(p.getId());
                                flag = false;
                            }
                        }
                        if (flag) {
                            error.append("请正确填写岗位名称；");
                        }
                    }


                    if (StringUtil.isNullOrEmpty(s.getWorkNumber())) {
                        error.append("工号不能为空；");
                    }else {
                        boolean flag1 = false;
                        for (Worker w : data3) {
                            if (s.getWorkNumber().equals(w.getWorkNumber())) {
                                flag1 = true;
                            }
                        }

                        int k=0;
                        for (int j = 0; j < data4.size(); j++) {
                            if(s.getWorkNumber().equals(data4.get(j).getWorkNumber())){
                                k++;
                            }

                        }

                        if(flag1){
                            error.append("工号已存在;");
                        }

                        if(k>1){
                            error.append("导入表中工号不唯一;");
                        }
                    }

                    //异常添加list
                    if (error.length() > 0) {
                        error.insert(0, "第" + i + "条,");
                        errors.add(error.toString());
                    } else {
                        //没有异常则添加 id、状态、头像
                        //s.setImgUrl(s.getSex().equals("4") ? Constant.AVATAR.BOY : Constant.AVATAR.GIRL);
                    }

                });
            }


            if (errors.size() > 0) {
                result.put("code", "222");
                result.put("errors", errors);
                return result;
            }

            this.batchSaveWorker(workerList,role.getId());
            result.put("code", "200");
        } catch (Exception e) {
            System.out.println("excel" + e);
        }


        return result;
    }


    //判断该对象是否: 返回ture表示所有属性为null  返回false表示不是所有属性都是null
    public boolean isAllFieldNull(Object obj) {
        Class stuCla = (Class) obj.getClass();// 得到类对象
        Field[] ff = stuCla.getDeclaredFields();//得到属性集合
        boolean flag1 = true;
        try {
            for (Field f1 : ff) {//遍历属性
                f1.setAccessible(true); // 设置属性是可以访问的(私有的也可以)
                Object val = f1.get(obj);// 得到此属性的值
                if (val != null && !val.equals("null")) {//只要有1个属性不为空,那么就不是所有的属性值都为空
                    flag1 = false;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return flag1;
    }

    //导出的方法
    public Workbook exportWorker(Worker worker) {
        Pager pager = worker.getPager().setPaging(false);
        worker.setPager(pager);
        List<Map<String, Object>> workerList = workerDao.findWorkerListForExportWorkerByCondition(worker);
        if(workerList.size()!=0){
            workerList.forEach(w->{
                String department_id = (String) w.get("department_id");
                String subDeptName = this.getSubDeptName(department_id);
                w.put("dept_name",subDeptName);
            });
        }
        List<ExcelExportEntity> entity = new ArrayList<ExcelExportEntity>();
        entity.add(new ExcelExportEntity("员工工号", "work_number"));
        entity.add(new ExcelExportEntity("员工姓名", "name"));
        entity.add(new ExcelExportEntity("性别", "sex"));
        entity.add(new ExcelExportEntity("年龄", "age"));
        entity.add(new ExcelExportEntity("邮箱", "email"));
        entity.add(new ExcelExportEntity("电话号码", "tel"));
        entity.add(new ExcelExportEntity("部门", "dept_name"));
        entity.add(new ExcelExportEntity("岗位", "post_name"));
        entity.add(new ExcelExportEntity("角色", "title"));
        return ExcelExportUtil.exportExcel(new ExportParams("员工列表","员工"),
                entity, workerList);
    }






}