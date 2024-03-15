<template>
    <el-container class="main-container">
        <el-header class="main-header">
            <el-image style="height: 35px" src="../public/monitor.svg"></el-image>
            <div class="tabs">
                <tab-item v-for="item in tabs" @click="changePage(item)" :name = item.name :active="item.id === tab"/>
                <el-switch v-model="dark" border-color="#424242" style="margin: 0px 20px"
                           :active-action-icon = "Moon"
                           :inactive-action-icon = "Sunny"/>
                <el-dropdown>
                    <el-avatar class="avater" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
                    <template #dropdown>
                        <el-dropdown-item @click="userLogout()">
                            <el-icon><Back/></el-icon>
                            退出登录
                        </el-dropdown-item>
                    </template>
                </el-dropdown>
            </div>
        </el-header>
        <el-main class="main-content">
            <router-view v-slot = "{Component}">
                <transition name = "el-fade-in-linear" mode="out-in">
                    <Component :is = "Component"></Component>
                </transition>
            </router-view>
        </el-main>
    </el-container>
</template>

<script setup>
import { logout } from '@/net'
import router from "@/router";
import {Back, Moon, Sunny} from "@element-plus/icons-vue";
import {useDark} from "@vueuse/core";
import {reactive, ref} from "vue";
import TabItem from "@/component/TabItem.vue";
import {useRoute} from "vue-router";

const dark = ref(useDark())
const route = useRoute()


const tabs = [
    {id:1,name: "管理",route: "manage"},
    {id:2,name: "安全",route: "security"}
]
function defaultIndex(){
    for (let tab of tabs){
        if(tab.route === route)
            return tab.id
    }
    return 1
}
const tab = ref(defaultIndex())
const changePage = (item) =>{
    tab.value = item.id
    router.push({name: item.route})
}
function userLogout() {
  logout(() => router.push("/"))
}
</script>

<style scoped>
.main-container{
    height: 100vh;
    width: 100vw;
    .main-header{
        height: 60px;
        background-color: var(--el-bg-color);
        border-bottom: 2px solid var(--el-border-color);
        display: flex;
        align-items: center;
        .tabs{
            height: 60px;
            display: flex;
            flex: 1;
            align-items: center;
            gap: 10px;
            justify-content: right;
        }
    }
    .main-content{
        height: 100%;
        background: #f5f5f5;
    }
}
.dark .main-container .main-content{
    background: #232323;
}
</style>
