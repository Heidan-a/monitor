package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.RuntimeData;
import com.example.entity.vo.request.RenameClientVO;
import com.example.entity.vo.request.RenameNodeVO;
import com.example.entity.vo.request.RuntimeDetailVO;
import com.example.entity.vo.response.ClientDetailsVO;
import com.example.entity.vo.response.ClientPreviewVO;
import com.example.entity.vo.response.RuntimeHistoryVO;
import com.example.service.ClientService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monitor")
public class MonitorController {
    @Resource
    ClientService clientService;

    @GetMapping("/list")
    public RestBean<List<ClientPreviewVO>> listAllClient(){
        return RestBean.success(clientService.listClients());
    }
    @PostMapping("/rename")
    public RestBean<Void> renameClient(@RequestBody @Valid RenameClientVO vo){
        clientService.renameClient(vo);
        return RestBean.success();
    }
    @GetMapping("/details")
    public RestBean<ClientDetailsVO> details(int clientId){
        return RestBean.success(clientService.clientDetails(clientId));
    }
    @PostMapping("/rename-node")
    public RestBean<Void> renameNode(@RequestBody @Valid RenameNodeVO vo){
        clientService.renameNode(vo);
        return RestBean.success();
    }

    @GetMapping("/runtime-history")
    public RestBean<RuntimeHistoryVO> runtimeHistoryDetails(int clientId){
        return RestBean.success(clientService.clientRuntimeDetailsHistory(clientId));
    }
    @GetMapping("/runtime-now")
    public RestBean<RuntimeDetailVO> runtimeNowDetails(int clientId){
        return RestBean.success(clientService.clientRuntimeDetailsNow(clientId));
    }
}
