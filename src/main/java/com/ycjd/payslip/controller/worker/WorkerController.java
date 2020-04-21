package com.ycjd.payslip.controller.worker;

import cn.hutool.crypto.digest.DigestUtil;
import com.ycjd.payslip.pojo.Constant;
import com.ycjd.payslip.pojo.Pager;
import com.ycjd.payslip.pojo.ResponseJson;
import com.ycjd.payslip.pojo.worker.Dept;
import com.ycjd.payslip.pojo.worker.Worker;
import com.ycjd.payslip.service.worker.DeptService;
import com.ycjd.payslip.service.worker.WorkerService;
import com.ycjd.payslip.util.ObjectKit;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/worker")
public class WorkerController {
    @Autowired
    private WorkerService workerService;

    @Autowired
    private DeptService deptService;


    @Value("${uploadpic.path}")
    private String uploadPicPath;


    @PostMapping("/saveWorker")
    public ResponseJson saveWorker(
            @Validated
            @RequestBody Worker worker) {
        String s = workerService.saveWorker(worker);
        if(s.equals("1")){
            return new ResponseJson(false,"请先配置默认角色!");
        }
        return new ResponseJson();
    }

    @GetMapping("/update/findWorkerById/{id}")
    public ResponseJson findWorkerById(
            @PathVariable String id) {
        Worker worker = workerService.findWorkerById(id);
        if(worker!=null){
            return new ResponseJson(worker);
        }else {
            return new ResponseJson(false,"该员工已删除!");
        }

    }

    @PostMapping("/update/updateWorker")
    public ResponseJson updateWorker(
            @RequestBody Worker worker) {
        workerService.updateWorker(worker);
        return new ResponseJson();
    }

    @GetMapping("/look/lookWorkerById/{id}")
    public ResponseJson lookWorkerById(
            @PathVariable String id) {
        Worker worker = workerService.findWorkerById(id);
        return new ResponseJson(worker);
    }

    @PostMapping("/findWorkersByCondition")
    public ResponseJson findWorkersByCondition(
            //@Validated
            @RequestBody Worker worker) {
        System.out.println(worker);
        List<Worker> data = workerService.findWorkerListByCondition(worker);
        long count = workerService.findWorkerCountByCondition(worker);
        return new ResponseJson(data, count);
    }

    @PostMapping("/findOneWorkerByCondition")
    public ResponseJson findOneWorkerByCondition(
            @RequestBody Worker worker) {
        Worker one = workerService.findOneWorkerByCondition(worker);
        return new ResponseJson(one);
    }

    @GetMapping("/deleteWorker/{id}")
    public ResponseJson deleteWorker(
            @PathVariable String id) {
        workerService.deleteWorker(id);
        return new ResponseJson();
    }


    @GetMapping("/deletePhysWorker/{id}")
    public ResponseJson deletePhysWorker(
            @PathVariable String id) {
        Worker worker = new Worker();
        worker.setId(id);
        worker.setDel(1);
        workerService.updateWorker(worker);
        return new ResponseJson();
    }


    @PostMapping("/findWorkerListByCondition")
    public ResponseJson findWorkerListByCondition(
            @Validated
            @RequestBody Worker worker) {
        List<Worker> data = workerService.findWorkerListByCondition(worker);
        return new ResponseJson(data);
    }


    @PostMapping("/findWorkerListByConditionNotLimit")
    public ResponseJson findWorkerListByConditionNotLimit(
            @RequestBody Worker worker) {
        List<Worker> data = workerService.findWorkerListByCondition(worker);
        return new ResponseJson(data);
    }

////////////////////////////////////////////////////////////////////

    //连表查询
    @PostMapping("/findWorkersByCondition2")
    public ResponseJson findWorkersByCondition2(
            @RequestBody Worker worker) {
        List<Worker> workerList = workerService.findWorkerListByCondition2(worker);
        //List<Worker> newWorkerList = new ArrayList<>();
        long count = workerService.findWorkerCountByCondition(worker);
        return new ResponseJson(workerList, count);
    }



    /*批量导入方法*/
    @GetMapping("/upload/exportTemplate")   //导入模板
    public void exportTemplate(HttpServletResponse response) {
        // 告诉浏览器用什么软件可以打开此文件
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=worker.xls");
        try (ServletOutputStream s = response.getOutputStream()) {
            Workbook workbook = workerService.exportTemplate();
            workbook.write(s);
        } catch (Exception e) {

        }
    }



    @PostMapping("/upload/uploadStudent")
    public ResponseJson uploadStudent(MultipartFile file) {
        return new ResponseJson(workerService.uploadStudent(file));
    }


    //导出的方法
    @PostMapping("/download")
    public void exportTeacher(@RequestBody Worker worker, HttpServletResponse response) {

        // 告诉浏览器用什么软件可以打开此文件
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=worker.xls");
        try (ServletOutputStream s = response.getOutputStream()) {
            Workbook workbook = workerService.exportWorker(worker);
            workbook.write(s);
        } catch (Exception e) {

        }
    }



    /*-----------------图片上传----------------------------*/
    @PostMapping("/uploadAvatar")
   public String uploadAvatar(MultipartFile file,HttpServletRequest request) {

        return this.uploadImage(file, request);
    }



    /**上传图片
     * bmp,jpg,png,tiff,gif,pcx,tga,exif,fpx,svg,psd,cdr,pcd,dxf,ufo,eps,ai,raw,WMF,webp
     * @param file
     * @param request
     * @return
     */
     private String uploadImage(MultipartFile file,HttpServletRequest request){
        String ext = this.getExt(file);
        if(".jpg".equals(ext)
                ||".png".equals(ext)
                ||".gif".equals(ext)
                ||".bmp".equals(ext)
                ||".jpeg".equals(ext)
                ){
            return commonUploadFile(file,request);
        }else{
            throw new IllegalArgumentException("wrong file type");
        }

    }


    //得到上传文件的扩展名
    private String getExt(MultipartFile file){
        String filename = file.getOriginalFilename();
        String contentType=file.getContentType();
        return filename==null||filename.lastIndexOf(".") == -1 ? "." + contentType.substring(contentType.indexOf("/") + 1) : filename.substring(filename.lastIndexOf("."));
    }


    private String commonUploadFile(MultipartFile file,HttpServletRequest request) {
        String ext = this.getExt(file);
        //生成图片名称
        String uuidName = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
        String newName =  uuidName + ext;

        //服务器本地保存图片
        //获取文件在本地服务器的储存位置
        String localPath = uploadPicPath;

        String urlPath = request.getScheme() + "://" + this.getIpAddress() + ":" + request.getServerPort();

        File filePath = new File(localPath);
        System.out.println("文件的保存路径：" + localPath);
        if (!filePath.exists() && !filePath.isDirectory()) {
            //System.out.println("目录不存在，创建目录:" + filePath);
            filePath.mkdir();
        }

        //在指定路径下创建一个文件
        File targetFile = new File(localPath, newName);

        try {
            file.transferTo(targetFile);
            System.out.println(localPath+"/"+newName);
            return urlPath+"/"+newName;

        } catch (IOException e) {
            e.printStackTrace();
            return "wrong file type";
        }



    }



    //获取当前项目的访问地址
    private String getIpAddress() {

         String ip=null;
        //获取当前项目的访问地址
        Enumeration<NetworkInterface> networkInterfaces = null;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
            new ResponseJson(false,"图片上传失败!");
        }
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                if(!inetAddress.isLinkLocalAddress()&&!inetAddress.isLoopbackAddress()){
                   ip= inetAddress.toString();
                }
            }
        }

        return ip.replace("/","");
    }


        @GetMapping("/resetPwd/{id}")
    public ResponseJson resetPwd( @PathVariable String id) {
       Worker worker = new Worker();
       worker.setId(id);
        //设置初始密码
        String s = DigestUtil.md5Hex(Constant.INITIAL_PASSWORD);
        String s1 = DigestUtil.sha1Hex(s);
        worker.setPassword(s1);
        workerService.updateWorker(worker);

        return new ResponseJson();
    }


    @GetMapping("/findWorkersTreeByCondition")
    public ResponseJson findWorkersTreeByCondition(){
        Pager pager = new Pager();
        pager.setPaging(false);

        Dept dept = new Dept();
        dept.setPager(pager);

        Worker worker = new Worker();
        worker.setPager(pager);

        List<Worker> workerList = workerService.findWorkerListByCondition(worker);
        List<Dept> deptList=deptService.findDeptListByCondition(dept);

        for (Worker w : workerList) {
                Dept d = new Dept();
                d.setId(w.getId());
                d.setDeptName(w.getName());
                d.setParentId(w.getDepartmentId());
                deptList.add(d);

        }
        List<Dept> treeData = ObjectKit.buildTree(deptList, "-1");
        return new ResponseJson(treeData);
    }
}
