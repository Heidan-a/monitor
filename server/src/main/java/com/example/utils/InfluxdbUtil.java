package com.example.utils;

import com.alibaba.fastjson2.JSONObject;
import com.example.entity.dto.RuntimeData;
import com.example.entity.vo.request.RuntimeDetailVO;
import com.example.entity.vo.response.RuntimeHistoryVO;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class InfluxdbUtil {
        private InfluxDBClient client;
        @Value("${spring.influx.url}")
        String url;
        @Value("${spring.influx.user}")
        String user;
        @Value("${spring.influx.password}")
        String password;
        String BUCKET = "monitor";
        String ORG = "heidan";
        @PostConstruct
        public void init(){
            client = InfluxDBClientFactory.create(url,user,password.toCharArray());
        }

        public void writeRuntimeData(int clientId, RuntimeDetailVO vo){
            RuntimeData data = new RuntimeData();
            BeanUtils.copyProperties(vo,data);
            data.setClientId(clientId);
            data.setTimestamp(new Date().toInstant());
            WriteApiBlocking writeApi = client.getWriteApiBlocking();
            writeApi.writeMeasurement(BUCKET, ORG, WritePrecision.NS, data);
        }
    public RuntimeHistoryVO readRuntimeData(int clientId) {
        RuntimeHistoryVO vo = new RuntimeHistoryVO();
        String query = """
                from(bucket: "%s")
                |> range(start: %s)
                |> filter(fn: (r) => r["_measurement"] == "runtime")
                |> filter(fn: (r) => r["clientId"] == "%s")
                """;
        String format = String.format(query, BUCKET, "-1h", clientId);
        List<FluxTable> tables = client.getQueryApi().query(format, ORG);
        int size = tables.size();
        if (size == 0) return vo;
        List<FluxRecord> records = tables.get(0).getRecords();
        //table对应字段，record对应每个字段的数据
        for (int i = 0; i < records.size(); i++) {
            JSONObject object = new JSONObject();
            object.put("timestamp", records.get(i).getTime());
            for (int j = 0; j < size; j++) {
                FluxRecord record = tables.get(j).getRecords().get(i);
                object.put(record.getField(), record.getValue());
            }
            vo.getList().add(object);
        }
        return vo;
    }
}

