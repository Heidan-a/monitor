package com.example.task;

import com.example.entity.RuntimeDetail;
import com.example.utils.MonitorUtil;
import com.example.utils.NetUtil;
import jakarta.annotation.Resource;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class MonitorJobBean extends QuartzJobBean {
    @Resource
    NetUtil net;
    @Resource
    MonitorUtil monitor;
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        RuntimeDetail runtime = monitor.monitorRunTimeDetail();
        net.updateRuntimeDetail(runtime);
    }
}
