<script>
import {
    mapMutations
} from 'vuex'
import {
    version
} from './package.json'
import checkUpdate from '@/uni_modules/uni-upgrade-center-app/utils/check-update';
import api from "common/js/allApi.js";
export default {
    onLaunch: function () {

        // #ifdef H5
        var href = location.href;
        if (href.includes('webView')) {
            uni.setStorageSync("IswebView", true)
        }
        if (href.includes('recommend')) {
            uni.setStorageSync("platform", 7);
            uni.setStorageSync("appId", 'wx7f20d1112a4e3559');
            uni.setStorageSync("distributorId", '2529');
            // uni.setStorageSync("shopId", 937);//本地调试使用 
            uni.switchTab({
                url: '/src/components/index/index'
            });
        }
        if (href.includes('samsungS23Coupon')) {
            uni.setStorageSync("platform", 7);
            uni.setStorageSync("appId", 'wx7f20d1112a4e3559');
            uni.setStorageSync("distributorId", '2529');
            // uni.setStorageSync("shopId", 937);//本地调试使用 
            let url = location.search;
            let params = new URLSearchParams(url.split('?')[1]);
            uni.setStorageSync("shopCode", params.get('fcp_shopId'));
            uni.setStorageSync("superiorCode", params.get('salerId'));//导购员编码
            if (uni.getStorageSync("shopCode") != undefined) {
                this.ShopStatus(uni.getStorageSync("shopCode"));
            }
            uni.redirectTo({
                url: '/good_pages/samsung/samsungS23Coupon'
            });
        }
        if (href.includes('discountCoupon')) {
            uni.setStorageSync("platform", 7);
            uni.setStorageSync("appId", 'wx7f20d1112a4e3559');
            uni.setStorageSync("distributorId", '2529');
            let userId = uni.getStorageSync("userId");
            if (userId == null || userId == undefined || userId == "") {
                // 未登录跳转登录
                uni.reLaunch({
                    url: '/good_pages/login/login?platform=7&distributorId=2529&sa=5'
                });
            } else {
                uni.redirectTo({
                    url: '/good_pages/discount_coupon/discount'
                });
            }
        }
        if (href.includes('orderDetail')) {
            uni.setStorageSync("platform", 7);
            uni.setStorageSync("appId", 'wx7f20d1112a4e3559');
            uni.setStorageSync("distributorId", '2529');

            let url = location.search;
            let params = new URLSearchParams(url.split('?')[1]);
            let userId = uni.getStorageSync("userId");
            if (userId == null || userId == undefined || userId == "") {
                uni.setStorageSync("orderId", params.get('id'));
                // 未登录跳转登录
                uni.reLaunch({
                    url: '/good_pages/login/login?platform=7&distributorId=2529&sa=2'
                });
            } else {
                uni.redirectTo({
                    url: '/good_pages/order/orderDetails?id=' + params.get('id')
                });
            }
        }

        if (href.includes('phone')) {
            uni.setStorageSync("platform", 7);
            uni.setStorageSync("distributorId", '2529');
            let url = location.search;
            let params = new URLSearchParams(url.split('?')[1]);
            uni.redirectTo({
                url: '/good_pages/customization/index?image=' + params.get('url') + '&pictureId=' + params.get('pid')
            });
        }

        // #endif
        console.log('App Launch');
        // #ifdef APP-PLUS
        if (plus.runtime.appid !== 'HBuilder') {
            checkUpdate()
        }
        // #endif
    },
    onShow: function () {
        this.getMaterialList();
    },
    onHide: function () {
    },
    globalData: {
        test: ''
    },
    mounted() {
        // #ifdef H5
        //窗口发生变化刷新页面
        window.addEventListener('resize', this.onWindowResize);
        // #endif
    },
    methods: {
        // 根据店铺编码获取店铺信息
        ShopStatus(shopCode) {
            this.$api.get(this, api.getShopStatusByShopCode, {
                distributorId: 7,
                shopCode: shopCode,
            }).then((res) => {
                if (res.success) {
                    this.name = res.data.shopName;
                    uni.setStorageSync("shopId", res.data.id);
                    uni.setStorageSync("shopCode", res.data.shopCode);
                    uni.setStorageSync("shopName", res.data.shopName);
                    uni.setStorageSync("qrUrl", res.data.qrUrl);
                    uni.setStorageSync("thirdQrUrl", res.data.thirdQrUrl);

                } else {
                    uni.showToast({
                        title: res.errMessage,
                        icon: "none",
                        duration: 2000
                    });
                }
            });
        },
        onWindowResize() {
            var that = this;
            const res = uni.getSystemInfoSync();
            var windowWidth = 750 / res.windowWidth;
            var windowHeight = (res.windowHeight * windowWidth) - ((180 + res.safeAreaInsets.bottom + res.statusBarHeight) * windowWidth);
            if (uni.getStorageSync("ratio") != windowWidth && !uni.getStorageSync("setText")) {
                uni.setStorageSync("devicePixelRatio", res.devicePixelRatio);//像素比
                uni.setStorageSync("nakedHeight", (res.windowHeight - (res.safeAreaInsets.bottom + res.statusBarHeight)) * windowWidth);
                uni.setStorageSync("statusBarHeight", res.statusBarHeight);
                uni.setStorageSync("windowHeight", windowHeight);
                uni.setStorageSync("ratio", windowWidth);//比例
                that.$router.go(0);
            }
        },
        //获取手机壳材质
        getMaterialList() {
            var modelNoOrName = '';
            uni.getSystemInfo({
                success: function (res) {
                    var windowWidth = 750 / res.windowWidth;
                    console.log("设备信息", res);
                    var windowHeight = (res.windowWidth * windowWidth) - ((180 + res.safeAreaInsets.bottom + res.statusBarHeight) * windowWidth);
                    uni.setStorageSync("devicePixelRatio", res.devicePixelRatio);//像素比
                    uni.setStorageSync("nakedHeight", (res.windowHeight - (res.safeAreaInsets.bottom + res.statusBarHeight)) * windowWidth);
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
                }
            });
            if (modelNoOrName.indexOf("(") != -1) {
                modelNoOrName = modelNoOrName.split('(')[0] + modelNoOrName.split('(')[1].split(')')[0];
            }
            uni.setStorageSync("modelNoOrName", modelNoOrName)
            this.$api.get(this, api.GetmodelMaterialRelevance, {
                modelNoOrName: modelNoOrName,
            }).then((res) => {
                var aspectRatio = res.data.length / res.data.width;
                var frameRatio = 300 / res.data.length;
                uni.setStorageSync("aspectRatio", aspectRatio);//宽高比例
                uni.setStorageSync("frameRatio", frameRatio);//边框比例
                uni.setStorageSync("defaultImage", res.data.outImage)
                uni.setStorageSync("floorImage", res.data.floorImage)
                uni.setStorageSync("leftFrame", res.data.leftFrame)
                uni.setStorageSync("topFrame", res.data.topFrame)
            })
        },
        ...mapMutations(['setUniverifyErrorMsg', 'setUniverifyLogin'])
    }
}
</script>

<style>
/* #ifndef APP-PLUS-NVUE */
/* uni.css - 通用组件、模板样式库，可以当作一套ui库应用 */
@import "./common/uni.css";
@import "@/static/customicons.css";
/* H5 兼容 pc 所需 */
/* #ifdef H5 */
@media screen and (min-width: 768px) {
    body {
        overflow-y: scroll;
    }
}

/* 顶栏通栏样式 */
/* .uni-top-window {
	    left: 0;
	    right: 0;
	} */

/* uni-page-body {
    background-color: #f5f5f5 !important;
    min-height: 100% !important;
    height: auto !important;
} */

.uni-top-window uni-tabbar .uni-tabbar {
    background-color: #fff !important;
}

.uni-app--showleftwindow .hideOnPc {
    display: none !important;
}

/* #endif */

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

/* #endif*/
</style>
