package org.wx.mirouteripmonitor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.wx.mirouteripmonitor.common.ResponseData;
import org.wx.mirouteripmonitor.service.MailSenderService;

import java.util.Properties;

import static org.wx.mirouteripmonitor.service.impl.SenderServiceImpl.SENDER_KEY;

@Service
public class MailSenderServiceImpl implements MailSenderService {
    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Override
    public JavaMailSenderImpl getMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost((String) redisTemplate.opsForHash().get(SENDER_KEY,"host"));
        sender.setDefaultEncoding("UTF-8");
        sender.setUsername((String) redisTemplate.opsForHash().get(SENDER_KEY,"userName"));
        sender.setPassword((String) redisTemplate.opsForHash().get(SENDER_KEY,"passWord"));
        Properties p = new Properties();
        p.setProperty("mail.smtp.auth", "true");
        sender.setJavaMailProperties(p);
        return sender;
    }

    @Override
    public ResponseData sendMsg(String email, String subject, String content){
        JavaMailSenderImpl mailSender = getMailSender();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setFrom(mailSender.getUsername());
        mailMessage.setSubject(subject);
        mailMessage.setText(content);
        mailSender.send(mailMessage);
        return ResponseData.SUCCESS();
    }
}