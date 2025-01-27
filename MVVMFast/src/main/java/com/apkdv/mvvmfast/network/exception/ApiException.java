package com.apkdv.mvvmfast.network.exception;

import android.net.ParseException;

import com.apkdv.mvvmfast.network.config.HttpCode;
import com.bumptech.glide.load.HttpException;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.CancellationException;

/**
 * Created by lengyue on 2019/3/8.
 * Email : lengyue@apkdv.com
 * Description :自定义网络异常类
 */
public class ApiException extends IOException {

    private final long code;
    private String message;
    private String data;
    private ExceptionType exceptionType = ExceptionType.UNKNOWN;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
        this.message = throwable.getMessage();
    }

    public ApiException(long code, String msg) {
        this.code = code;
        this.data = "";
        this.message = msg;
    }


    public ApiException(long code) {
        this.code = code;
        this.data = "";
        this.message = "";
    }

    public ApiException(long code, String msg, String data) {
        this.code = code;
        this.data = data;
        this.message = msg;
    }

    public ExceptionType getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message == null ? "" : message;
    }

    public ApiException setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getData() {
        return data == null ? "" : data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return message + "(code:" + code + ")";
    }

    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ApiException(e, httpException.getStatusCode());
            switch (httpException.getStatusCode()) {
                case HttpCode.HTTP.INNER_EXCEPTION:
                    ex.setExceptionType(ExceptionType.SERVER);
                    ex.message = "服务器内部异常";
                    break;
                case HttpCode.HTTP.HTTP_UNLOGIN:
                    ex.setExceptionType(ExceptionType.SERVER);
                    ex.message = "用户未登录";
                    break;
                case HttpCode.HTTP.CAN_NOT_RECEIVE:
                case HttpCode.HTTP.CREATED:
                case HttpCode.HTTP.RECEIVED:
                case HttpCode.HTTP.UN_AUTH_CONTENT:
                case HttpCode.HTTP.NONE_CONTENT:
                case HttpCode.HTTP.RESET:
                case HttpCode.HTTP.PART_RESOLVED:
                case HttpCode.HTTP.ERROR:
                case HttpCode.HTTP.UNAUTHORIZED:
                case HttpCode.HTTP.REFUSE:
                case HttpCode.HTTP.NOT_FOUND:
                case HttpCode.HTTP.METHOD_REFUSE:
                case HttpCode.HTTP.WAIT_TIME_OUT:
                case HttpCode.HTTP.UMIMPLEMENTS:
                case HttpCode.HTTP.GATEWAY_EXCEPTION:
                case HttpCode.HTTP.SERVICE_UNUSEFUL:
                case HttpCode.HTTP.GATEWAY_TIME_OUT:
                case HttpCode.HTTP.HTTP_VERSION_UNSUPPORT:
                default:
                    ex.setExceptionType(ExceptionType.SERVER);
                    ex.message = "请检查网络连接";
                    break;
            }
            return ex;
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {
            ex = new ApiException(e, HttpCode.FRAME_WORK.PARSE_ERROR);
            ex.message = e.getMessage();
            ex.setExceptionType(ExceptionType.LOCAL);
            return ex;
        } else if (e instanceof ConnectException || e instanceof SocketTimeoutException
                || e instanceof UnknownHostException) {
            ex = new ApiException(e, HttpCode.FRAME_WORK.NETWORK_ERROR);
            ex.setExceptionType(ExceptionType.CONNECT);
            ex.message = "请检查网络连接";
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ApiException(e, HttpCode.FRAME_WORK.SSL_ERROR);
            ex.setExceptionType(ExceptionType.CONNECT);
            ex.message = "安全证书错误，请检查网络";
            return ex;
        } else if (e instanceof ApiException) {
            ApiException exception = (ApiException) e;
            exception.setExceptionType(ExceptionType.API);
            return exception;
        } else if (e instanceof CancellationException) {
            ex = new ApiException(e, HttpCode.FRAME_WORK.REQUEST_CANCEL);
            ex.setExceptionType(ExceptionType.REQUEST_CANCEL);
            ex.message = e.getMessage();
            return ex;
        } else {
            ex = new ApiException(e, HttpCode.FRAME_WORK.UNKNOWN);
            ex.setExceptionType(ExceptionType.UNKNOWN);
            ex.message = e.getMessage();
            return ex;
        }
    }
}
