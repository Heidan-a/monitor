<script setup>

import PreviewCard from "@/component/PreviewCard.vue";
import {get} from "@/net";
import {reactive, ref} from "vue";
import ClientDetails from "@/component/ClientDetails.vue";

const list = ref([])
const detail = reactive({
    show: false,
    id: -1
})
const displayClientDetail = (id) => {
    detail.show = true
    detail.id = id
}
const updateList = () => get('/api/monitor/list',data => {
    list.value = data
})
setInterval(updateList,10 * 1000)
updateList()
</script>

<template>
    <div class="manage-main">
        <div class="title"><i class="fa-solid fa-server"></i> 主机管理列表</div>
        <div class="desc">这里是主机实例管理列表，你可以在这里管理你的主机</div>
        <el-divider/>
        <div class="card-list">
            <preview-card v-for="item in list" :data="item" :update="updateList" @click="displayClientDetail(item.id)"/>
        </div>
        <el-drawer size="520" :show-close="false" :with-header="false" v-model="detail.show" v-if="list.length" @close="detail.id = -1">
            <client-details :id="detail.id" :update="updateList"></client-details>
        </el-drawer>
    </div>
</template>

<style scoped>
:deep(.el-drawer){
    margin: 20px;
    max-height: calc(100% - 40px);
    border-radius: 10px;
    min-width: 550px;
}
:deep(.el-drawer__body){
    padding: 0;
}


.manage-main{
    margin: 0 50px;

    .title{
        font-size: 22px;
        font-weight: bold;
    }
    .desc{
        font-size: 15px;
        color: grey;
    }
}
.card-list{
    display: flex;
    gap: 20px;
    flex-wrap: wrap;
}
</style>