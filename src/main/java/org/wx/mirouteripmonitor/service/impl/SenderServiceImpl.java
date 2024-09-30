package org.wx.mirouteripmonitor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.wx.mirouteripmonitor.common.ResponseData;
import org.wx.mirouteripmonitor.service.SenderService;

import java.util.Map;

@Service
public class SenderServiceImpl implements SenderService {
    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    public final static String SENDER_KEY = "mirouteripmonitor:sender";
    @Override
    public ResponseData uploadSenderEmail(String host, String userName, String passWord) {
        //构造存入redis中的map
        redisTemplate.opsForHash().put(SENDER_KEY,"host",host);
        redisTemplate.opsForHash().put(SENDER_KEY,"userName",userName);
        redisTemplate.opsForHash().put(SENDER_KEY,"passWord",passWord);
        //将redis的值取出来
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(SENDER_KEY);
        //每个键值对进行比对
        if (entries.get("host").equals(host) && entries.get("userName").equals(userName) && entries.get("passWord").equals(passWord)){
            return ResponseData.SUCCESS();
        }else {
            return ResponseData.FAIL();
        }
    }

    @Override
    public ResponseData getSenderEmail() {
        //判断是否存在
        if (redisTemplate.hasKey(SENDER_KEY)){
            //取出来
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(SENDER_KEY);
            return ResponseData.SUCCESS(entries);
        }else {
            return ResponseData.FAIL("未设置邮箱信息");
        }
    }
}
