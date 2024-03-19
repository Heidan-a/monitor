<script setup>

import PreviewCard from "@/component/PreviewCard.vue";
import {get} from "@/net";
import {computed, reactive, ref} from "vue";
import ClientDetails from "@/component/ClientDetails.vue";
import {Plus} from "@element-plus/icons-vue";
import RegisterCard from "@/component/RegisterCard.vue";
import { useRoute } from "vue-router";
import {useStore} from "@/store";

const locations = [
    {name: 'cn', desc: '中国大陆'},
    {name: 'hk', desc: '香港'},
    {name: 'jp', desc: '日本'},
    {name: 'us', desc: '美国'},
    {name: 'sg', desc: '新加坡'},
    {name: 'kr', desc: '韩国'},
    {name: 'de', desc: '德国'}
]
const store = useStore()
const route = useRoute()
const checkedNodes = ref([])

const clientList = computed(() => {
    if(checkedNodes.value.length === 0){
        return list.value
    }else {
        return list.value.filter(item => checkedNodes.value.indexOf(item.location) >=0)
    }
})

const list = ref([])
const detail = reactive({
    show: false,
    id: -1
})
const displayClientDetail = (id) => {
    detail.show = true
    detail.id = id
}
const updateList = () =>{ 
    if(route.name === 'manage')
        get('/api/monitor/list',data => list.value = data)
}
const register = reactive({
    show:false,
    token:''
})
const refreshToken = () =>{
    get('/api/monitor/register',token => register.token = token)
}
setInterval(updateList,10 * 1000)
updateList()
</script>

<template>
    <div class="manage-main">
        <div style="display: flex;justify-content: space-between;align-items: end">
           <div>
               <div class="title"><i class="fa-solid fa-server"></i> 主机管理列表</div>
               <div class="desc">这里是主机实例管理列表，你可以在这里管理你的主机</div>
           </div>
            <div>
                <el-button type="primary" :icon="Plus" @click="register.show = true" :disabled="!store.isAdmin">添加新主机</el-button>
            </div>
        </div>

        <el-divider/>
        <div style="margin-bottom: 20px">
            <el-checkbox-group v-model="checkedNodes">
                <el-checkbox v-for="node in locations" :key="node" :label="node.name" border>
                    <span :class="`flag-icon flag-icon-${node.name}`"></span>
                    <span style="font-size: 13px;margin-left: 10px">{{node.desc}}</span>
                </el-checkbox>
            </el-checkbox-group>
        </div>
        <div class="card-list" v-if="list.length">
            <preview-card v-for="item in clientList" :data="item" :update="updateList" @click="displayClientDetail(item.id)"/>
        </div>
        <el-empty v-else description="还没有任何主机哦，快点击右上角注册一个吧"></el-empty>
        <el-drawer size="520" :show-close="false" :with-header="false" v-model="detail.show" v-if="list.length" @close="detail.id = -1">
            <client-details :id="detail.id" :update="updateList" @delete="updateList"></client-details>
        </el-drawer>
        <el-drawer @open="refreshToken" v-model="register.show" direction="btt" :with-header="false" style="min-height: 350px;max-width: 800px;margin: 10px auto">
            <register-card :token="register.token"></register-card>
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
:deep(.el-checkbox-group .el-checkbox){
    margin-right: 10px;
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