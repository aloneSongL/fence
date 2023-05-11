package com.bs.fence.service.impl;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import com.bs.fence.config.BaiduConfig;
import com.bs.fence.service.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 针对百度的REST接口编写通用的请求实现
 */
@Service
@Slf4j
public class BaiduApiService {

    @Autowired
    private BaiduConfig baiduConfig;

    /**
     * 通用的请求，支持失败重试，总共会执行4次，每次停顿2秒后重试
     */
    @Retryable(value = RuntimeException.class, maxAttempts = 4, backoff = @Backoff(delay = 2000L, multiplier = 0))
    public <T> T execute(String url, Method method, Map<String, Object> param, Function<T, HttpResponse> function) {
        HttpRequest httpRequest;
        switch (method) {
            case GET: {
                httpRequest = HttpRequest.get(url);
                break;
            }
            case POST: {
                httpRequest = HttpRequest.post(url);
                break;
            }
            case PUT: {
                httpRequest = HttpRequest.put(url);
                break;
            }
            case DELETE: {
                httpRequest = HttpRequest.delete(url);
                break;
            }
            default: {
                return null;
            }
        }

        //设置通用的参数
        httpRequest.form(param)
                .form("ak", this.baiduConfig.getAk())
                .form("service_id", this.baiduConfig.getServerId());

        //发起请求
        try {
            HttpResponse response = httpRequest
                    .header(Header.CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .timeout(20000)//超时，毫秒
                    .execute();
            return function.callback(response);
        } catch (Exception e) {
            //出现错误，抛出异常进行重试
            throw new RuntimeException();
        }
    }

    @Recover //全部重试失败后执行
    public HttpResponse recover(RuntimeException e) {
        //重试全部失败的情况非常可能是被封号了
        //程序已经无法自己修复，需要人工介入
        //程序必须通知相关人员进行排除，如何通知：可以采用短信、邮件等形式进行通知
        log.error("百度api调用失败，请检查！" + e);
        return null; //返回默认
    }

}
