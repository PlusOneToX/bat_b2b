<script>
import api from "./api/allApi";
export default {
    onLaunch: function () {
    },
    onShow: function () {
        this.getMaterialList();
    },
    onHide: function () {
    },
    methods: {
        //获取手机壳材质
        getMaterialList() {
            var modelNoOrName = '';
            uni.getSystemInfo({
                success: function (res) {
                    console.log("设备信息：", res);
                    var windowWidth = 750 / res.screenWidth;
                    var windowHeight = (res.screenHeight * windowWidth) - ((180 + res.safeAreaInsets.bottom + res.statusBarHeight) * windowWidth);
                    uni.setStorageSync("devicePixelRatio", res.devicePixelRatio);//像素比
                    uni.setStorageSync("nakedHeight", (res.screenHeight - (res.safeAreaInsets.bottom + res.statusBarHeight)) * windowWidth);
                    uni.setStorageSync("statusBarHeight", res.statusBarHeight);
                    uni.setStorageSync("windowHeight", windowHeight);
                    uni.setStorageSync("ratio", windowWidth);//比例
                    if (res.model.indexOf("<") != -1) {
                        modelNoOrName = res.model.split('<')[0];
                    } else if (res.model.indexOf("/") != -1 && res.osName == 'ios') {
                        modelNoOrName = "iPhone " + res.model.split('/')[1];
                    }
                    else {
                        modelNoOrName = res.model;
                    }
                    // modelNoOrName.split('(')[0] + modelNoOrName.split('(')[1].split(')')[0]
                }
            });
            if (modelNoOrName.indexOf("(") != -1) {
                modelNoOrName = modelNoOrName.split('(')[0] + modelNoOrName.split('(')[1].split(')')[0];
            }
            console.log("手机型号：", modelNoOrName);
            uni.setStorageSync("modelNoOrName", modelNoOrName)
        },
    }
}
</script>

<style>
/* 以下样式用于 hello uni-app 演示所需 */
page {
    background-color: #efeff4;
    height: 100%;
    font-size: 28rpx;
    /* line-height: 1.8; */
}

.fix-pc-padding {
    padding: 0 50px;
}

.uni-header-logo {
    padding: 30rpx;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    margin-top: 10rpx;
}

.uni-header-image {
    width: 100px;
    height: 100px;
}

.uni-hello-text {
    color: #7a7e83;
}

.uni-hello-addfile {
    text-align: center;
    line-height: 300rpx;
    background: #fff;
    padding: 50rpx;
    margin-top: 10px;
    font-size: 38rpx;
    color: #808080;
}
</style>
