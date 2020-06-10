<template>
    <view></view>
</template>
<script>
export default {
    data() {
        return {
            index: 0
        }
    },
    onShow() {
        //页面索引
        this.index = uni.getStorageSync("index");
        if (this.index == 1) {
            this.index = 0;
            this.put1();
        } else {
            this.index = 1;
            this.put();
        }
    },
    methods: {
        put1() {
            uni.switchTab({
                url: '../../components/index/index'
            });
        },
        put() {
            // #ifdef MP-WEIXIN
            uni.navigateTo({
                url: '/good_pages/customization/vessel'
            });
            // #endif
            // #ifdef H5
            uni.navigateTo({
                url: '/good_pages/customization/index'
            });
            // #endif
        }
    }
}
</script>