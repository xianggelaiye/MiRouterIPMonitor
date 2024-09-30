package org.wx.mirouteripmonitor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.wx.mirouteripmonitor.common.ResponseData;
import org.wx.mirouteripmonitor.service.ReceiverService;

import java.util.LinkedList;

@Service
public class ReceiverServiceImpl implements ReceiverService {
    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    public final static String RECEIVER_KEY = "mirouteripmonitor:receiver";
    @Override
    public ResponseData addReceiverEmail(String email) {
        //redis 存入list
        redisTemplate.opsForList().rightPush(RECEIVER_KEY,email);
        //遍历该队列，查看redis是否存在该email
        for (Object o : redisTemplate.opsForList().range(RECEIVER_KEY, 0, -1)){
            if (((String)o).equals(email)){
                return ResponseData.SUCCESS("添加成功");
            }
        }
        return ResponseData.FAIL("添加失败");
    }

    @Override
    public ResponseData deleteReceiverEmail(String email) {
        redisTemplate.opsForList().remove(RECEIVER_KEY,1,email);
        return ResponseData.SUCCESS("删除成功");
    }

    @Override
    public ResponseData listReceiverEmail() {
        LinkedList<String> list = new LinkedList<>();
        for (Object o : redisTemplate.opsForList().range(RECEIVER_KEY, 0, -1)){
            list.add((String)o);
        }
        return ResponseData.SUCCESS(list);
    }
}
