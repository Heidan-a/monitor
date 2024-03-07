package com.example.utils;

import com.alibaba.fastjson2.JSONObject;
import com.example.entity.BaseDetail;
import com.example.entity.ConnectConfig;
import com.example.entity.Response;
import com.example.entity.RuntimeDetail;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Component
public class NetUtil {

    @Lazy
    @Resource
    ConnectConfig config;

    private final HttpClient client = HttpClient.newHttpClient();

    /**
     * 向服务端注册
     * @param address 注册机的网络地址
     * @param token 注册需要的密钥
     * @return
     */
    public boolean registerToServer(String address,String token) {
        log.info("正在向服务器发送请求");
        Response response = this.doGet("/register",address,token);
        if(response.success()){
            log.info("客户端已注册完成");
        }else {
            log.error("客户端注册失败：{}",response.message());
        }
        return response.success();
    }

    private Response doGet(String url){
        return this.doGet(url,config.getAddress(),config.getToken());
    }

    private Response doGet(String url,String address,String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder().GET()
                    .uri(new URI(address + "/monitor" + url))
                    .header("Authorization",token)
                    .build();
            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
            return JSONObject.parseObject(response.body()).to(Response.class);
        }
        catch (Exception e) {
            log.error("在发起服务器请求时出现问题",e);
            return Response.errorResponse(e);
        }
    }
    public void updateBaseDetail(BaseDetail baseDetail){
        Response response = this.doPost("/detail", baseDetail);
        if(response.success()){
            log.info("更新主机基础信息成功");
        }else {
            log.error("更新主机基础信息失败: {}",response.message());
        }
    }

    public void updateRuntimeDetail(RuntimeDetail detail){
        Response response = this.doPost("/runtime", detail);
        if(!response.success()){
            log.error("更新主机运行时信息失败: {}",response.message());
        }
    }

    /**
     *
     * @param url post方法路径
     * @param data 传输过来的数据
     * @return
     */
    private Response doPost(String url, Object data) {
        try {
            String rawData = JSONObject.from(data).toJSONString();
            HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(rawData))
                    .uri(new URI(config.getAddress() + "/monitor" + url))
                    .header("Authorization", config.getToken())
                    .header("Content-Type", "application/json")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return JSONObject.parseObject(response.body()).to(Response.class);
        } catch (Exception e) {
            log.error("在发起服务端请求时出现问题", e);
            return Response.errorResponse(e);
        }
    }
}