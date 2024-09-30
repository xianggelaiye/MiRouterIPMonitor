package org.wx.mirouteripmonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wx.mirouteripmonitor.common.ResponseData;
import org.wx.mirouteripmonitor.service.SenderService;

@RestController(value = "/sender")
public class SenderController {
    @Autowired
    private SenderService senderService;

    @PostMapping("/email/upload")
    public ResponseData uploadUserEmail(String host, String userName, String passWord){
        return senderService.uploadSenderEmail(host,userName,passWord);
    }
    //查看用户邮箱信息
    @GetMapping("/email/getInfo")
    public ResponseData getUserEmail(){
        return senderService.getSenderEmail();
    }

}
