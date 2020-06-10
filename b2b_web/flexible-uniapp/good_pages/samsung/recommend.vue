<template>
    <view></view>
</template>
<script>
export default {

    data() {
        return {

        }
    },
    onShow() {
        uni.switchTab({
            url: '/src/components/index/index?platform=7&distributorId=2529&appid=wx7f20d1112a4e3559'
        });
    }
}
</script>