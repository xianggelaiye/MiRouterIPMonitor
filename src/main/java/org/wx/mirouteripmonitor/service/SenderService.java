package org.wx.mirouteripmonitor.service;


import org.wx.mirouteripmonitor.common.ResponseData;

public interface SenderService {
    ResponseData uploadSenderEmail(String host, String userName, String passWord);

    ResponseData getSenderEmail();
}
