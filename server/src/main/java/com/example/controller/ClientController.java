package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.Client;
import com.example.entity.vo.request.ClientDetailVO;
import com.example.entity.vo.request.RuntimeDetailVO;
import com.example.entity.vo.request.SshConnectionVO;
import com.example.service.ClientService;
import com.example.utils.Const;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/monitor")
@Tag(name = "主机操作相关", description = "包括主机注册删除，运行时数据，主机硬件信息等操作。")
public class ClientController {

    @Resource
    ClientService clientService;

    @GetMapping("/register")
    public RestBean<Void> registerClient(@RequestHeader("Authorization") String token){
        return clientService.verifyAndRegister(token) ?
                RestBean.success() : RestBean.failure(401,"注册失败");
    }

    @PostMapping("/detail")
    public RestBean<Void> updateClientDetails(@RequestAttribute(Const.ATTR_CLIENT) Client client,
                                              @RequestBody @Valid ClientDetailVO vo) {
        clientService.updateClientDetail(vo, client);
        return RestBean.success();
    }


    @PostMapping("/runtime")
    public RestBean<Void> updateRuntimeDetail(@RequestAttribute(Const.ATTR_CLIENT) Client client,
                                              @RequestBody @Valid RuntimeDetailVO vo){
        clientService.updateRuntimeDetail(vo,client);
        return RestBean.success();
    }



}
