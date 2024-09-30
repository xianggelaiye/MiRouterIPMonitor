package org.wx.mirouteripmonitor.service.impl;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.wx.mirouteripmonitor.service.MiRouterService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class MiRouterServiceImpl implements MiRouterService {

    private final static String ROUTER_WAN_IP = "192.168.31.1";
    private final static String ROUTER_USER_NAME = "admin";
    private final static String ROUTER_PASSWORD = "admin";

    private String stok;
    private OkHttpClient client = new OkHttpClient();
    @PostConstruct
    @Override
    public String getRouterWanIP() throws IOException, NoSuchAlgorithmException {
        login();
        Request request = new Request.Builder()
                .url("http://"+ROUTER_WAN_IP+"/cgi-bin/luci/;stok="+stok+"/api/xqnetwork/pppoe_status")
                .build();
        Response loginResponse = client.newCall(request).execute();
        String responseBody = loginResponse.body().string();
        JSONObject resultJson = JSONObject.parseObject(responseBody);
        //IP
        String wanIP = resultJson.getJSONObject("ip").getString("address");
        //子网掩码
        String subnetMask = resultJson.getJSONObject("ip").getString("mask");
        //DNS
        String dns1 = (String) resultJson.getJSONArray("dns").get(0);
        String dns2 = (String) resultJson.getJSONArray("dns").get(1);
//        System.out.println("WAN IP: " + wanIP);
//        System.out.println("Subnet Mask: " + subnetMask);
//        System.out.println("DNS: " + dns1 + ", " + dns2);
        return wanIP;
    }

    public String login() throws IOException, NoSuchAlgorithmException {
        Request getInitialDataRequest = new Request.Builder()
                .url("http://" + ROUTER_WAN_IP + "/cgi-bin/luci/web")
                .build();
        String responseBody = client.newCall(getInitialDataRequest).execute().body().string();
        //正则提取
        String key = getKey(responseBody);
        String deviceId = getDeviceId(responseBody);
        String nonce = generateNonce(deviceId);
        String oldPwd = computePassword(key, nonce, ROUTER_PASSWORD);
//        System.out.println("Key: " + key);
//        System.out.println("Device ID: " + deviceId);
//        System.out.println("Nonce: " + nonce);
//        System.out.println("Old Password: " + oldPwd);
        // Prepare login data
        RequestBody loginRequestBody = new FormBody.Builder()
                .add("username", ROUTER_USER_NAME)
                .add("password", oldPwd)
                .add("logtype", "2")
                .add("nonce", nonce)
                .build();

        // Send login request
        Request loginRequest = new Request.Builder()
                .url("http://" + ROUTER_WAN_IP + "/cgi-bin/luci/api/xqsystem/login")
                .post(loginRequestBody)
                .build();

        Response loginResponse = client.newCall(loginRequest).execute();
        String loginResponseBody = loginResponse.body().string();
        JSONObject loginResultJson = JSONObject.parseObject(loginResponseBody);
        String token = loginResultJson.getString("token");
        String stok = getStok(loginResultJson.getString("url"));
        this.stok = stok;
//        System.out.println("Token: " + token);
//        System.out.println("STOK: " + stok);
        return key;
    }
    private static String getKey(String responseBody) {
        Pattern pattern = Pattern.compile("key:.*?'(.*?)',");
        Matcher matcher = pattern.matcher(responseBody);
        return matcher.find() ? matcher.group(1) : null;
    }

    private static String getDeviceId(String responseBody) {
        Pattern pattern = Pattern.compile("deviceId = '(.*?)';");
        Matcher matcher = pattern.matcher(responseBody);
        return matcher.find() ? matcher.group(1) : null;
    }

    private static String generateNonce(String deviceId) {
        int type = 0;
        long time = Math.floorDiv(System.currentTimeMillis(), 1000);
        int random = (int) (Math.random() * 10000);
        return String.format("%d_%s_%d_%d", type, deviceId, time, random);
    }

    private static String computePassword(String key, String nonce,String password) throws NoSuchAlgorithmException, NoSuchAlgorithmException {
        MessageDigest digest;
        digest = MessageDigest.getInstance("SHA-1");

        byte[] hash1 = digest.digest((password + key).getBytes());
        byte[] finalHash = digest.digest((nonce + hashToString(hash1)).getBytes());
        return hashToString(finalHash);
    }

    private static String hashToString(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static String getStok(String url) {
        Pattern pattern = Pattern.compile(";stok=(.*?)/");
        Matcher matcher = pattern.matcher(url);
        return matcher.find() ? matcher.group(1) : null;
    }


}
