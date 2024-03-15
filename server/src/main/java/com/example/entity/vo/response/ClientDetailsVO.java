package com.example.entity.vo.response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClientDetailsVO {
    @NotNull
    int id;
    @NotNull
    boolean online;
    @NotNull
    String name;
    @NotNull
    String osName;
    @NotNull
    String cpuName;
    @NotNull
    String osVersion;
    @NotNull
    int cpuCore;
    @NotNull
    String node;
    @NotNull
    String location;
    @NotNull
    double memory;
    @NotNull
    double disk;
    @NotNull
    String ip;
}
