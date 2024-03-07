package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RuntimeDetail {
    long timestamp;
    double cpuUsage;
    double memoryUsage;
    double diskUsage;
    double networkUpload;
    double networkDownload;
    double diskWrite;
    double diskRead;
}
