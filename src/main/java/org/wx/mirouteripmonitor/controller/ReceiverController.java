package org.wx.mirouteripmonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wx.mirouteripmonitor.common.ResponseData;
import org.wx.mirouteripmonitor.service.ReceiverService;

import javax.sound.midi.Receiver;

@RestController("/receiver")
public class ReceiverController {
    @Autowired
    private ReceiverService receiverService;

    @PostMapping("/email/add")
    public ResponseData addReceiverEmail(String email) {
        return receiverService.addReceiverEmail(email);
    }
    @GetMapping("/email/delete")
    public ResponseData deleteReceiverEmail(String email) {
        return receiverService.deleteReceiverEmail(email);
    }
    @GetMapping("/email/list")
    public ResponseData listReceiverEmail() {
        return receiverService.listReceiverEmail();
    }

}
