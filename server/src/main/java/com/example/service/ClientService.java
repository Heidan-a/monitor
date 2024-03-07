package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Client;
import com.example.entity.vo.request.ClientDetailVO;
import com.example.entity.vo.request.RuntimeDetailVO;
import com.example.utils.Const;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;

public interface ClientService extends IService<Client> {
    boolean verifyAndRegister(String token);
    Client findClientById(int cid);
    Client findClientByToken(String token);
    String registerToken();
    void updateClientDetail(ClientDetailVO vo,Client client);
    void updateRuntimeDetail(RuntimeDetailVO vo, Client client);
}
