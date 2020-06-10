<template>
    <view>
        <view class="up-box" :style="{
            backgroundImage: 'url('')', 
             backgroundSize: '750rpx'+' ' + (windowHeight-160)+'rpx', height:(windowHeight) +'rpx'}">
        </view>
        <canvas-drag ref="canvasRef" id="canvas-drag" :modelHeight="modelHeight" :windowHeight="windowHeight" :graph="graph" :backgroundImage="defaultImage" :floorImage="backgroundImage" :height="height" :width="width" :proportion="proportion" class="canvasArea" :enableUndo="true"></canvas-drag>
        <!-- <image :src="bs" mode="aspectFit"></image> -->
        <view class="operation_type_box">
            <view class="phone_box">
                <view class="phone_name"> {{phoneName}} - {{materialsName}} {{materialsName1}}</view>
                <view class="phone_price">¥ {{price}}</view>
            </view>
            <view class="intelligibility_box" @click="orderConfirm" style="margin-right: 40rpx;">
                <image src="../../static/icons/submit.png"></image>
                <view>加购</view>
            </view>
        </view>
        <uni-popup ref="message" type="dialog">
            <uni-popup-dialog type="info" cancelText="我再想想" confirmText="确认提交" title="温馨提示" content="您购买的是专属定制商品，下单后，不支持取消订单，且非质量问题不支持无理由退换，请您确认无误后再提交。是否继续提交结算？" @confirm="onExportJSON"></uni-popup-dialog>
        </uni-popup>
        <!-- <Home_Tabar></Home_Tabar> -->
    </view>
</template>
<script>
import canvasDrag from "./components/canvas-drag/glance_over";        //画板
import Home_Tabar from "./components/homeTabar";                      //底部导航栏
import api from "../../api/allApi";
import wx from "weixin-js-sdk";
export default {
    components: {
        canvasDrag,
        Home_Tabar
    },
    data() {
        return {
            iscollect: true,//是否收藏
            isonExportJSON: true,//是否加购
            bs: '',
            graph: {},
            canvasBg: "",
            defaultImage: '',
            backgroundImage: '',
            showPopup: false,
            TextPopup: false,
            CommodityPopup: false,
            TagsPopup: false,
            imageUrl: '',
            modelId: 0,//型号id
            textureId: 0,//材质id
            materialsId: 0,//二级材质id
            brandId: 10016,//品牌id
            brandName: '',
            phoneName: '',//手机名称
            height: 0,//高度
            width: 0,//宽
            leftFrame: 0,//左边框
            rightFrame: 0,//右边框
            topFrame: 0,//上边框
            underFrame: 0,//边框
            proportion: 1,//宽高比例
            materialsName: '',
            materialsName1: '',
            pictureId: 0,//图片id（网络图传0）
            picture: '',
            price: 0,
            manufactors: '',
            itemCode: '',
            itemId: '',
            windowHeight: 1,
            canvasX: 0,
            canvasY: 0,
            ratio: 1,
            modelHeight: 1,//模型高度
            modelWidth: 1,//模型宽handleOrder
        }
    },
    onLoad(data) {


        //#ifdef H5
        let oss = document.createElement("script");
        oss.type = "text/javascript";
        oss.src = "http://gosspublic.alicdn.com/aliyun-oss-sdk-4.4.4.min.js";
        document.body.appendChild(oss);
        // #endif

        this.windowHeight = uni.getStorageSync("nakedHeight");
        this.modelHeight = this.windowHeight - 550;
        this.ratio = uni.getStorageSync("ratio");
        this.bs = data.img;
        this.materialsId = data.materialsId;
        this.modelId = data.modelId;
        this.brandId = data.brandId;
        this.brandName = data.brandName;
        this.materialsName = data.textureName;
        this.materialsName1 = data.materialsName;
        this.phoneName = data.phoneName;
        this.price = data.price;
        this.manufactors = data.manufactors;
        this.itemCode = data.itemCode;
        this.itemId = data.itemId;
        this.sku = data.sku;
        this.skuId = data.skuId;
        this.GetmodelMaterialRelevance();
    },
    methods: {

        orderConfirm() {
            this.$refs.message.open();
        },
        onExportJSON() {
            this.$refs.message.close()
            if (this.isonExportJSON) {
                this.$refs.canvasRef.exportFun().then(imgArr => {
                    this.file_img(imgArr);
                }).catch(e => {
                    console.error(e);
                });
            } else {
                uni.showToast({
                    title: '已加入购物车',
                    icon: "none",
                    duration: 2000
                });
            }
        },
        file_img(imgArr) {
            this.file_imgH5(imgArr);
        },
        addToShopcart(img) {

            let info = {
                image: img,
                generateImage: this.bs,
                manufactor: this.manufactors,
                materialId: this.materialsId,
                material: this.materialsName,
                brandId: this.brandId,
                brandName: this.brandName,
                modelId: this.modelId,
                modelName: this.phoneName,
                pictureId: this.pictureId,
                price: this.price,
                totalPrice: this.price,
                count: 1,
                skuNo: this.sku,
                skuId: this.skuId,
            };

            let distributorId = uni.getStorageSync("distributorId");
            let extendUserId = uni.getStorageSync("extendUserId");
            let blOrderType = uni.getStorageSync("blOrderType");
            let ipUserId = uni.getStorageSync("ipUserId");
            let ipUserProductId = uni.getStorageSync("ipUserProductId");
            let ipOrderId = uni.getStorageSync("ipOrderId");
            let ipBackUrl = uni.getStorageSync("ipBackUrl");
            let aAttach = uni.getStorageSync("aAttach");
            let vQuantity = Number(uni.getStorageSync("vQuantity"));
            let vSbomCode = uni.getStorageSync("vSbomCode");
            if (distributorId === "1410") {
                let wholeUrl = uni.getStorageSync("wholeUrl");
                info.extendField = { urlParam: wholeUrl }; //扩展字段
            } else if (extendUserId) {
                // 是否有传 userId
                info.extendField = { userId: extendUserId };
            } else if (blOrderType) {
                // 是否有传 orderType
                info.extendField = { orderType: blOrderType };
            } else if (ipUserId || ipBackUrl || ipOrderId || ipUserProductId) {
                // 是否有传 pId、jsonStr
                info.extendField = {
                    userId: ipUserId,
                    userProductId: ipUserProductId,
                    orderId: ipOrderId,
                    backUrl: ipBackUrl,
                };
            } else if (aAttach) {
                // 是否有传 attach
                info.extendField = {
                    attach: aAttach,
                };
            } else if (vQuantity) {
                // 是否有传 quantity/sbomCode
                info.extendField = {
                    skuCodeAndQtys: vSbomCode + ":" + vQuantity,
                };
            }
            this.$api.post(this, api.handleOrder, {
                orderDetail: info,
            }).then((res) => {
                if (res.success) {
                    let url = res.data.url;
                    // 判断当前环境
                    var ua = navigator.userAgent.toLowerCase();
                    if (ua.match(/MicroMessenger/i) == "micromessenger") {
                        // 微信浏览器
                        wx.miniProgram.getEnv((res) => {
                            if (res.miniprogram) {
                                // 小程序
                                if (distributorId === "3696") {
                                    // IPTV
                                    wx.miniProgram.navigateTo({
                                        url: url,
                                    });
                                } else {
                                    window.location.href = url;
                                }
                            } else {
                                window.location.href = url;
                            }
                        });
                    } else {
                        // 其他环境
                        window.location.href = url;
                    }
                } else {
                    uni.showToast({
                        title: res.errMessage,
                        icon: "none",
                        duration: 2000
                    });
                }
            })
                .catch((err) => {
                    console.log(err);
                });
        },
        file_imgH5(imgArr) {
            var that = this;
            let _fileBlob = that.dataURLtoBlob(imgArr);
            // blob转换成file文件
            let file = new File([_fileBlob], new Date() + '.jpg');
            // 随机命名
            let randomName = that.random_string(6) + "_" + new Date().getTime() + ".jpg";
            that.$api.get(that, api.uploadFile).then((result) => {
                let client = new OSS.Wrapper({
                    region: result.data.region,
                    accessKeyId: result.data.accessKeyId,
                    accessKeySecret: result.data.accessKeySecret,
                    stsToken: result.data.securityToken,
                    bucket: result.data.bucketName,
                    endpoint: result.data.endpoint,
                    secure: true,
                });
                client.multipartUpload(result.data.pathName + "/" + randomName, file, {})
                    .then((results) => {
                        return new Promise((resolve, reject) => {
                            var img = result.data.hostname + results.name;
                            this.addToShopcart(img);
                            resolve(true);
                        });
                    })
                    .catch((err) => {
                        console.log("失败了", err);
                    });
            })
        },
        // base64转换成blob
        dataURLtoBlob(dataurl) {
            var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
                bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
            while (n--) {
                u8arr[n] = bstr.charCodeAt(n);
            }
            return new Blob([u8arr], { type: mime });
        },
        // 随机生成文件名
        random_string(len) {
            len = len || 32;
            let chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678";
            let maxPos = chars.length;
            let pwd = "";
            for (let i = 0; i < len; i++) {
                pwd += chars.charAt(Math.floor(Math.random() * maxPos));
            }
            return pwd;
        },
        GetmodelMaterialRelevance() {
            this.$api.get(this, api.getByModelIdAndMaterialId, {
                modelId: this.modelId,
                materialId: this.materialsId
            }).then((res) => {
                this.defaultImage = res.data.outImage;//模型图
                this.backgroundImage = res.data.floorImage;//背景图
                this.height = res.data.length;//高度
                this.width = res.data.width;//宽
                this.proportion = res.data.length / res.data.width;
                this.leftFrame = res.data.leftFrame;//左边框
                this.rightFrame = res.data.rightFrame;//右边框
                this.topFrame = res.data.topFrame;//上边框
                this.underFrame = res.data.underFrame;//边框

                // this.canvasX = (750 - (this.width * 5)) / 2;
                // this.canvasY = ((this.windowHeight - 200) - (this.height * 5)) / 2;


                this.canvasX = (750 - (this.modelHeight / this.proportion)) / 2;
                this.canvasY = 100;
                this.modelWidth = this.modelHeight / this.proportion;

                this.onImport2();
                let timer1 = setInterval(() => {
                    this.onImport1();
                    clearTimeout(timer1)
                }, 200);
                let timer = setInterval(() => {
                    this.onImport();
                    clearTimeout(timer)
                }, 400);
            })
        },
        onImport() {
            uni.getImageInfo({
                src: this.defaultImage,
                success: res => {
                    this.setData({
                        graph: {
                            y: this.canvasY / this.ratio,
                            x: this.canvasX / this.ratio,
                            w: this.modelWidth / this.ratio,
                            h: this.modelHeight / this.ratio,
                            type: 'image',
                            url: res.path,
                        }
                    });
                },
                fail: res => {
                    console.log(`缓存失败：${res.errMsg}`);
                }
            })
        },
        onImport2() {
            uni.getImageInfo({
                src: this.backgroundImage,
                success: res => {
                    this.setData({
                        graph: {
                            y: this.canvasY / this.ratio,
                            x: this.canvasX / this.ratio,
                            w: this.modelWidth / this.ratio,
                            h: this.modelHeight / this.ratio,
                            type: 'image',
                            url: res.path,
                        }
                    });

                },
                fail: res => {
                    console.log(`缓存失败：${res.errMsg}`);
                }
            })
        },

        onImport1() {
            uni.getImageInfo({
                src: this.bs,
                success: res => {
                    uni.setStorageSync("bs", res.path)
                    this.setData({
                        graph: {
                            y: (this.canvasY + (this.topFrame * 6)) / this.ratio,
                            x: (this.canvasX + (this.leftFrame * 6)) / this.ratio,
                            w: (this.modelWidth - ((this.leftFrame * 2) * 6)) / this.ratio, // 选中canvas多宽
                            h: (this.modelHeight - ((this.topFrame * 2) * 6)) / this.ratio, // 选中canvas多高
                            type: 'image',
                            url: res.path,
                        }
                    });
                },
                fail: res => {
                    console.log(`缓存失败：${res.errMsg}`);
                }
            })

        },
    }
}
</script>
<style scoped lang="scss">
.up-box {
    position: absolute;
    // top: 100;
    left: 0;
    right: 0;
    background-repeat: no-repeat;
    background-position: 50% 0rpx;
    background-clip: content-box;
    // z-index: 10;
}
.operation_type_box {
    width: 750rpx;
    height: 120rpx;
    // background-color: rgba(255, 255, 255, 1);
    color: rgba(16, 16, 16, 1);
    font-size: 28rpx;
    text-align: center;
    font-family: Arial;
    display: flex;
    justify-content: space-between;
    align-items: center;
    position: absolute;
    // padding: 0rpx 50rpx;
    bottom: 20rpx;
    // margin-bottom: 100rpx;
    // box-sizing: border-box;
    .intelligibility_box {
        color: rgba(44, 44, 44, 1);
        font-size: 24rpx;
        font-family: PingFangSC-regular;
        width: 80rpx;
        height: 80rpx;
        background-color: #ffffff;
        box-shadow: 0rpx 4rpx 12rpx 0rpx rgba(0, 0, 0, 0.4);
        border-radius: 40rpx;
        padding-top: 10rpx;
        box-sizing: border-box;
        image {
            width: 40rpx;
            height: 40rpx;
        }
        view {
            margin-top: -10rpx;
            -webkit-transform: scale(0.8);
        }
    }
    .phone_box {
        margin-left: 20rpx;
        width: 580rpx;
        height: 80rpx;
        border-radius: 100rpx;
        background-color: #ffffff;
        color: rgba(44, 44, 44, 1);
        font-size: 24rpx;
        text-align: center;
        box-shadow: 0rpx 4rpx 12rpx 0rpx rgba(0, 0, 0, 0.4);
        font-family: Arial;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 0 20rpx;
        box-sizing: border-box;

        .phone_name {
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
            -o-text-overflow: ellipsis;
            width: 500rpx;
        }
        .phone_price {
            width: 80rpx;
            color: rgba(252, 126, 27, 1);
            font-size: 20rpx;
            text-align: right;
            font-family: PingFangSC-regular;
        }
    }
}
</style>