package org.wx.mirouteripmonitor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.wx.mirouteripmonitor.service.MailSenderService;
import org.wx.mirouteripmonitor.service.MiRouterService;
import org.wx.mirouteripmonitor.service.MonitorService;
import org.wx.mirouteripmonitor.service.ReceiverService;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

@Service
public class MonitorServiceImpl implements MonitorService {
    @Autowired
    RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private MailSenderService mailSenderService;
    @Autowired
    private ReceiverService receiverService;
    @Autowired
    private MiRouterService miRouterService;


    public final static String ROUTER_WAN_IP = "mirouteripmonitor:wan-ip";

    //定时任务注解，每一分钟执行一次
    @Scheduled(cron = "0 0/1 * * * ?")
    public void monitor() {
        String wanIp = null;
        try {
            wanIp = miRouterService.getRouterWanIP();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (!redisTemplate.hasKey(ROUTER_WAN_IP)) {
            redisTemplate.opsForValue().set(ROUTER_WAN_IP,wanIp);
        }else if (!wanIp.equals(redisTemplate.opsForValue().get(ROUTER_WAN_IP)) && isNetWorkAvailable()){
            //TODO: 发送邮件通知
            List<String> receivers = (LinkedList<String>)receiverService.listReceiverEmail().getData();
            for (String receiver : receivers){
                mailSenderService.sendMsg(receiver,"服务器IP改变","服务器IP由 "+redisTemplate.opsForValue().get(ROUTER_WAN_IP)+" 变为 "+wanIp);
            }
            System.out.println("IP改变："+wanIp);
            redisTemplate.opsForValue().set(ROUTER_WAN_IP,wanIp);
        }
    }

    public static boolean isNetWorkAvailable() {
        try {
            InetAddress address = InetAddress.getByName("www.baidu.com");
            Socket socket = new Socket(address, 80);
            socket.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
