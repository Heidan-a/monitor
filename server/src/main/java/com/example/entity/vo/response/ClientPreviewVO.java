package com.example.entity.vo.response;

import lombok.Data;

@Data
public class ClientPreviewVO {
    int id;
    String name;
    boolean online;
    int cpuCore;
    String cpuName;
    String osName;
    String osVersion;
    String location;
    String ip;
    double memoryUsage;
    double cpuUsage;
    double memory;
    double networkUpload;
    double networkDownload;

}
