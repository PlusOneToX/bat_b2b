<template>
    <view>
        <!-- https://diy.bat.com/wxh5/good_pages/userid -->
        <web-view :src="webviewusrl"></web-view>
    </view>
</template>
<script>

export default {

    data() {
        return {
            webviewusrl: ''
        }
    },
    onShow() {
    },
    onLoad(options) {
        // 通过用户编码判断是否h5跳转授权
        if (uni.getStorageSync("staffCode") != undefined && uni.getStorageSync("staffCode") != '') {
            this.webviewusrl = "https://diy.bat.com/wxh5/good_pages/userid?platform=1&distributorId=" + uni.getStorageSync("distributorId");
        } else {
            var qrCodeUrl = decodeURIComponent(options.q);
            if (qrCodeUrl != 'undefined') {
                var data = qrCodeUrl.split('?')[1];
                data.split('&').forEach(item => {
                    if (item.split('=')[0] == 'platform') {
                        if (uni.getAccountInfoSync().miniProgram.appId == 'wx05cb4496de7a20d7') {
                            uni.setStorageSync("platform", item.split('=')[1] == 1 ? 'DZXCX' : item.split('=')[1]);
                        }
                        else {
                            uni.setStorageSync("platform", item.split('=')[1] == 1 ? 'JKWXAPP' : item.split('=')[1]);
                        }
                    }
                    else if (item.split('=')[0] == 'shopId') {
                        uni.setStorageSync("shopId", item.split('=')[1]);
                    }
                    else if (item.split('=')[0] == 'shopCode') {
                        uni.setStorageSync("shopCode", item.split('=')[1]);
                    }
                    else if (item.split('=')[0] == 'distributorId') {
                        uni.setStorageSync("distributorId", item.split('=')[1]);
                    }
                    else if (item.split('=')[0] == 'staffCode') {
                        uni.setStorageSync("superiorCode", item.split('=')[1]);
                    }
                });
            }
            this.webviewusrl = "https://diy.bat.com/wxh5/good_pages/userid?platform=1&distributorId=" + uni.getStorageSync("distributorId");
        }
    },
    methods: {

    }
}
</script>
<style scoped lang="scss">
</style>
