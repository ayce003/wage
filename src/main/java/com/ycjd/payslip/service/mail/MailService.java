package com.ycjd.payslip.service.mail;

import com.ycjd.payslip.pojo.mail.MailVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service
public class MailService {

    private Logger logger = LoggerFactory.getLogger(getClass());//提供日志类

    @Autowired
    private JavaMailSenderImpl mailSender; //注入邮件工具

    /**
     *  发送邮件
     */

    public MailVo sendMail(MailVo mailVo){
        try{
          checkMail(mailVo);  //检测邮件
            sendMimeMail(mailVo);  //发送邮件
            return saveMail(mailVo);   //保存邮件
        }catch (Exception e){
            logger.error("发送邮件失败：",e); //打印错误信息
            mailVo.setStatus("fail");
            mailVo.setError(e.getMessage());
            return mailVo;

        }
    }

    //检测邮件信息
    public void checkMail(MailVo mailVo){
        if(StringUtils.isEmpty(mailVo.getTo())){
            throw new RuntimeException("邮件收信人不能为空");
        };                                                      //接收人邮箱
        if (StringUtils.isEmpty(mailVo.getSubject())) {
            throw new RuntimeException("邮件主题不能为空");
        }
        if (StringUtils.isEmpty(mailVo.getText())) {
            throw new RuntimeException("邮件内容不能为空");
        }
    }

    private void sendMimeMail(MailVo mailVo) {    //复杂的邮件
      try{
          MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailSender.createMimeMessage(),true); //true表示支持复杂类型
          mailVo.setFrom(getMailSendFrom()); //邮件发信人从配置项读取
          mimeMessageHelper.setFrom(mailVo.getFrom()); //邮件发信人
          mimeMessageHelper.setTo(mailVo.getTo());  //邮件收信人
          mimeMessageHelper.setSubject(mailVo.getSubject());  //邮件主题
          mimeMessageHelper.setText(mailVo.getText(),true);  //邮件内容
          if(!StringUtils.isEmpty(mailVo.getCc())){ //抄送
              mimeMessageHelper.setCc(mailVo.getCc().split(","));
           }
           if(!StringUtils.isEmpty(mailVo.getBcc())){  //密送
              mimeMessageHelper.setBcc(mailVo.getBcc().split(","));
           }
           if(mailVo.getMultipartFiles() != null ){  //添加邮件附件
               for ( MultipartFile multipartFile: mailVo.getMultipartFiles()) {
                   mimeMessageHelper.addAttachment(multipartFile.getOriginalFilename(),multipartFile);

               }
           }
           if( mailVo.getSentDate()==null){  //发送时间
              mailVo.setSentDate(new Date());
              mimeMessageHelper.setSentDate(mailVo.getSentDate());
           }
           mailSender.send(mimeMessageHelper.getMimeMessage());  //正式发送邮件
          mailVo.setStatus("ok");
          logger.info("发送邮件成功：{}->{}", mailVo.getFrom(), mailVo.getTo());
      }catch(Exception e){
        throw  new RuntimeException(e);//发送失败
      }
    }
    //保存邮件
    private MailVo saveMail(MailVo mailVo) {
        //将邮件保存到数据库..
        return mailVo;
    }
    //获取邮件发信人
    public String getMailSendFrom(){
        return mailSender.getJavaMailProperties().getProperty("from");
    }
    }
