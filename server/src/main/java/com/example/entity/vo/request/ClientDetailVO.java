package com.example.entity.vo.request;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClientDetailVO {
    @NotNull
    String osArch;
    @NotNull
    String osName;
    @NotNull
    String osVersion;
    @NotNull
    int osBit;
    @NotNull
    String cpuName;
    @NotNull
    int cpuCore;
    @NotNull
    double memory;
    @NotNull
    double disk;
    @NotNull
    String ip;
}
