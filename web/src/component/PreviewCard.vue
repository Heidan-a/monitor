<script setup>
import {copyIp, fitByUnit, percentageToStatus, rename} from '@/util'
const props = defineProps({
    data: Object,
    update: Function
})

</script>

<template>
    <div class="instance-card">
       <div style="display: flex;justify-content: space-between">
          <div>
              <div class="name">
                  <span :class="`flag-icon flag-icon-${data.location}`"></span>
                  <span style="padding: 0 5px">{{ data.name }}</span>
                  <i class="fa-solid fa-pen-to-square" @click.stop="rename(data.id,data.name,update)"></i>
              </div>
              <div class="os">
                  <span>{{`${data.osName}${data.osVersion}`}}</span>
              </div>
          </div>
              <div class="status" v-if="data.online">
                  <i style="color: #18cb18" class="fa-solid fa-circle-play" ></i>运行中
              </div>
              <div class="status" v-else>
                  <i style="color: #8a8a8a" class="fa-solid fa-circle-stop" ></i>离线
              </div>
       </div>
        <el-divider style="margin: 10px 0"/>
        <div class="network">
            <span style="margin-right: 10px">公网IP: {{data.ip}}</span>
            <i class="fa-solid fa-copy interact-item" @click.stop="copyIp(data.ip)" style="color: dodgerblue"></i>
        </div>
        <div class="cpu">
            <span style="margin-right: 10px">处理器: {{data.cpuName}}</span>
        </div>
        <div class="handware">
            <i class="fa-solid fa-microchip" style="color: dodgerblue"></i>
            <span style="margin: 0 10px">{{`${data.cpuCore}CPU`}}</span>
            <i class="fa-solid fa-memory" style="color: dodgerblue"></i>
            <span style="margin: 0 10px">{{`${data.memory.toFixed(1)}GB`}}</span>
        </div>
        <div class="progress">
            <span>CPU: {{(data.cpuUsage * 100).toFixed(1)}}%</span>
            <el-progress :status="percentageToStatus(data.cpuUsage * 100)" :percentage="`${data.cpuUsage * 100}`" :stroke-width="5" :show-text="false" />
        </div>
        <div class="progress">
            <span>内存: <b>{{ data.memoryUsage.toFixed(1) }} GB</b></span>
            <el-progress :status="percentageToStatus(data.memoryUsage / data.memory * 100)"  :percentage="data.memoryUsage / data.memory * 100" :stroke-width="5" :show-text="false" />
        </div>
        <div class="network-flow">
            <span>网络流量:</span>
            <div>
                <span>上传: {{ `${fitByUnit(data.networkUpload,'KB')}/s` }}</span>
                <i class="fa-solid fa-arrow-up" style="color: dodgerblue"></i>
                <el-divider direction="vertical"/>
                <span>下载: {{ `${fitByUnit(data.networkDownload,'KB')}/s` }}</span>
                <i class="fa-solid fa-arrow-down" style="color: dodgerblue"></i>
            </div>

        </div>
    </div>
</template>

<style scoped>
.dark .instance-card{color: #d9d9d9}

.interact-item{
    transition: .3s;
    &:hover{
        cursor: pointer;
        scale: 1.1;
        opacity: 0.7;
    }
}

.instance-card{
    padding: 15px;
    width: 340px;
    background-color: var(--el-bg-color);
    border-radius: 5px;
    box-sizing: border-box;
    color: #6b6b6b;
    transition: .3s;
    &:hover{
        cursor: pointer;
        scale: 1.04;
    }
    .name{
        font-size: 16px;
    }
    .status{
        font-size: 16px;
    }
    .os{
        font-size: 14px;
        color: grey;
    }
    .network{
        font-size: 14px;
    }
    .handware{
        margin-top: 5px;
        font-size: 14px;
    }
    .progress{
        font-size: 14px;
        margin-top: 10px;
    }
    .network-flow{
        font-size: 14px;
        margin-top: 10px;
        display: flex;
        justify-content: space-between;
    }
}
</style>