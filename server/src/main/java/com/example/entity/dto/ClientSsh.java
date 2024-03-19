package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("db_client_ssh")
public class ClientSsh {
    @TableId
    int id;
    int port;
    String username;
    String password;
}
