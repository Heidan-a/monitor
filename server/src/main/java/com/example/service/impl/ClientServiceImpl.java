package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Client;
import com.example.entity.dto.ClientDetail;
import com.example.entity.dto.RuntimeData;
import com.example.entity.vo.request.ClientDetailVO;
import com.example.entity.vo.request.RenameClientVO;
import com.example.entity.vo.request.RenameNodeVO;
import com.example.entity.vo.request.RuntimeDetailVO;
import com.example.entity.vo.response.ClientDetailsVO;
import com.example.entity.vo.response.ClientPreviewVO;
import com.example.entity.vo.response.RuntimeHistoryVO;
import com.example.mapper.ClientDetailMapper;
import com.example.mapper.ClientMapper;
import com.example.service.ClientService;
import com.example.utils.InfluxdbUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements ClientService {

    @Resource
    ClientDetailMapper clientDetailMapper;

    @Resource
    ClientMapper clientMapper;

    @Resource
    InfluxdbUtil influx;
    private String registerToken = this.generateNewToken();
    private final Map<Integer,Client> clientIdCache = new ConcurrentHashMap<>();
    private final Map<String,Client> clientTokenCache = new ConcurrentHashMap<>();

    @PostConstruct
    private void initClientCache(){
        this.list().forEach(this::addClientCache);
    }

    private void addClientCache(Client client){
        clientTokenCache.put(client.getToken(),client);
        clientIdCache.put(client.getId(),client);
    }

    @Override
    public boolean verifyAndRegister(String token) {
        if(registerToken.equals(token)){
            int cid = RandomClientId();
            Client client = new Client(cid,"未命名主机",token,"cn","未命名节点",new Date());
            if(this.save(client)){
                //成功注册主机了那么服务器注册用的token就要换了
                registerToken = generateNewToken();
                this.addClientCache(client);
                return true;
            }
        }
        return false;
    }

    @Override
    public RuntimeHistoryVO clientRuntimeDetailsHistory(int clientId) {
        RuntimeHistoryVO vo = influx.readRuntimeData(clientId);
        ClientDetail detail = clientDetailMapper.selectById(clientId);
        BeanUtils.copyProperties(detail, vo);
        return vo;
    }

    @Override
    public RuntimeDetailVO clientRuntimeDetailsNow(int clientId) {
        return currentRuntime.get(clientId);
    }

    @Override
    public String registerToken() {
        return registerToken;
    }

    public int RandomClientId(){
        return new Random().nextInt(90000000) + 10000000;
    }

    private String generateNewToken(){
        String Characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(24);
        for (int i = 0; i < 24; i++) {
            sb.append(Characters.charAt(random.nextInt(Characters.length())));
        }
        System.out.println(sb);
        return sb.toString();
    }

    @Override
    public Client findClientByToken(String token) {
        return clientTokenCache.get(token);
    }

    @Override
    public Client findClientById(int cid) {
        return clientIdCache.get(cid);
    }

    @Override
    public ClientDetailsVO clientDetails(int clientId) {
        ClientDetailsVO vo = this.clientIdCache.get(clientId).asViewObject(ClientDetailsVO.class);
        BeanUtils.copyProperties(clientDetailMapper.selectById(clientId),vo);
        vo.setOnline(isOnline(currentRuntime.get(clientId)));
        return vo;
    }

    private boolean isOnline(RuntimeDetailVO vo){
        return vo != null &&  System.currentTimeMillis() - vo.getTimestamp() < 60 * 1000;
    }

    @Override
    public void updateClientDetail(ClientDetailVO vo, Client client) {
        ClientDetail detail = new ClientDetail();
        BeanUtils.copyProperties(vo, detail);
        detail.setId(client.getId());
        if(Objects.nonNull(clientDetailMapper.selectById(client.getId()))) {
            clientDetailMapper.updateById(detail);
        } else {
            clientDetailMapper.insert(detail);
        }
    }

    @Override
    public void renameClient(RenameClientVO vo) {
        this.update(Wrappers.<Client>update().eq("id",vo.getId()).set("name",vo.getName()));
        this.initClientCache();
    }

    @Override
    public void renameNode(RenameNodeVO vo) {
        this.update(Wrappers.<Client>update().eq("id",vo.getId()).set("node",vo.getNode()).set("location",vo.getLocation()));
        this.initClientCache();
    }

    private final Map<Integer, RuntimeDetailVO> currentRuntime = new ConcurrentHashMap<>();

    @Override
    public List<ClientPreviewVO> listClients() {
        return clientIdCache.values().stream().map(client -> {
            ClientPreviewVO vo = client.asViewObject(ClientPreviewVO.class);
            BeanUtils.copyProperties(clientDetailMapper.selectById(vo.getId()),vo);
            RuntimeDetailVO runtime = currentRuntime.get(vo.getId());
            if(this.isOnline(runtime)){
                BeanUtils.copyProperties(runtime,vo);
                vo.setOnline(true);
            }
            return vo;
        }).toList();
    }

    /**
     * 往influx写入数据，并且写入currentRuntime中
     * @param vo
     * @param client
     */
    @Override
    public void updateRuntimeDetail(RuntimeDetailVO vo, Client client) {
        currentRuntime.put(client.getId(),vo);
        influx.writeRuntimeData(client.getId(),vo);
    }
}
