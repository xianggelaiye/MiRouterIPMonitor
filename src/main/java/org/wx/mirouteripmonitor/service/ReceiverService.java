package org.wx.mirouteripmonitor.service;

import org.wx.mirouteripmonitor.common.ResponseData;

public interface ReceiverService {
    ResponseData addReceiverEmail(String email);

    ResponseData deleteReceiverEmail(String email);

    ResponseData listReceiverEmail();
}
