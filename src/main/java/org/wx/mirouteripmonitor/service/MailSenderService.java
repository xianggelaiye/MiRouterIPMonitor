package org.wx.mirouteripmonitor.service;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.wx.mirouteripmonitor.common.ResponseData;

public interface MailSenderService {
    JavaMailSenderImpl getMailSender();

    ResponseData sendMsg(String email, String subject, String content);
}
