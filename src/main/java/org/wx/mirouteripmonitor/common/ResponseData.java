package org.wx.mirouteripmonitor.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor                //dto层
@Builder
public class ResponseData<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String message;

    private T data;

    public ResponseData(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }


    public static <T> ResponseData<T> SUCCESS(T data) {
        return new ResponseData<T>(HttpStatusEnum.SUCCESS.getCode(), HttpStatusEnum.SUCCESS.getReasonPhraseCN(), data);
    }

    public static <T> ResponseData<T> SUCCESS() {
        return new ResponseData<T>(HttpStatusEnum.SUCCESS.getCode(), HttpStatusEnum.SUCCESS.getReasonPhraseCN(), null);
    }

    public static <T> ResponseData<T> FAIL(T data) {
        return new ResponseData<T>(HttpStatusEnum.FORBIDDEN.getCode(), HttpStatusEnum.FORBIDDEN.getReasonPhraseCN(), data);
    }

    public static <T> ResponseData<T> FAIL() {
        return new ResponseData<T>(HttpStatusEnum.FORBIDDEN.getCode(), HttpStatusEnum.FORBIDDEN.getReasonPhraseCN(), null);
    }

    public static <T> ResponseData<T> UNAUTHORIZED(T data) {
        return new ResponseData<T>(HttpStatusEnum.UNAUTHORIZED.getCode(), HttpStatusEnum.UNAUTHORIZED.getReasonPhraseCN(), data);
    }
}

