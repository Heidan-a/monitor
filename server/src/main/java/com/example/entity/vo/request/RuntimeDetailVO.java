package com.example.entity.vo.request;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RuntimeDetailVO {
    @NotNull
    long timestamp;
    @NotNull
    double cpuUsage;
    @NotNull
    double memoryUsage;
    @NotNull
    double diskUsage;
    @NotNull
    double networkUpload;
    @NotNull
    double networkDownload;
    @NotNull
    double diskWrite;
    @NotNull
    double diskRead;
}
