package com.example.entity.vo.response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SshSettingsVO {
    String ip;
    int port = 22;
    String username;
    String password;
}
