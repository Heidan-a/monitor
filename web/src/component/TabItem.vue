<script setup>
defineProps({
    name: String,
    active: Boolean
})
</script>

<template>
    <div :class="`tab-item ${active ? 'active' : ''}`">
        {{name}}
    </div>
</template>

<style scoped>
.tab-item{
    height: 60px;
    width: 60px;
    font-size: 15px;
    text-align: center;
    line-height: 60px;
    box-sizing: border-box;
    transition: color .3s;
    &:hover{
        cursor: pointer;
        color: var(--el-color-primary);
    }
}
.active{
    border-bottom: 2px solid var(--el-color-primary);
}
</style>