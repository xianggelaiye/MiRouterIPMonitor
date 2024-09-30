package org.wx.mirouteripmonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wx.mirouteripmonitor.service.MailSenderService;

@RestController(value = "/email")
public class EmailController {
    @Autowired
    private MailSenderService mailSenderService;
    @GetMapping("send")
    public String sendMsg(){
        JavaMailSenderImpl mailSender = mailSenderService.getMailSender();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("1796207106@qq.com");
        mailMessage.setFrom(mailSender.getUsername());
        mailMessage.setSubject("这是测试邮件2");
        mailMessage.setText("这是内容2");
        mailSender.send(mailMessage);
        return "send mail success";
    }
}
