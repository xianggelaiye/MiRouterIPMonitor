package org.wx.mirouteripmonitor.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface MiRouterService {
    String getRouterWanIP() throws IOException, NoSuchAlgorithmException;
}
