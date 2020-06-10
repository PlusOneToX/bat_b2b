<template>
    <view>
        <uni-popup ref="popup" type="center" :safeArea="false">
            <view class="preview_box">
                <view class="sele_img_title_box">
                    <uni-icons style="opacity: 0;" type="closeempty" size="26"></uni-icons>
                    <view>商品效果预览</view>
                    <uni-icons @click="close" type="closeempty" size="26"></uni-icons>
                </view>
                <canvas style="width:680rpx; height: 780rpx;" canvas-id="glanceOvertCanvas" id="glanceOvertCanvas"></canvas>
                <view class="model_type_box">
                    <view class="model_type_mue">
                        <image src="../../../static/icons/phone.png"></image>
                        {{phoneName}}
                    </view>
                    <view class="model_type_mue">
                        <image src="../../../static/icons/coverage.png"></image>{{textureName}} - {{materialsName}}
                    </view>
                </view>
            </view>
            <view class="operation_box">
                <view class="operation_meu" @click="collect">
                    <uni-icons type="heart" size="24" v-if="iscollect"></uni-icons>
                    <uni-icons type="heart-filled" color="#0076a4" size="24" v-else></uni-icons>
                    收藏
                </view>
                <view class="operation_meu" @click="onExportJSON">
                    <image v-if="isonExportJSON" src="../../../static/icons/shop1.png"></image>
                    <image v-else src="../../../static/icons/shop.png"></image>
                    加购
                </view>
                <view class="operation_meu" @click="orderConfirm">
                    <image src="../../../static/icons/pay.png"></image>
                    付款
                </view>
            </view>
        </uni-popup>
    </view>
</template>
<script>
import api from "../../../common/js/allApi";
export default {
    props: {
        //生产图
        img: {
            type: String,
            default: '',
        },
        //背景图
        hollowOut: {
            type: String,
            default: '',
        },
        //模型图
        defaultImage: {
            type: String,
            default: '',
        },
        materialsId: {
            type: Number,
            default: 0,
        },
        modelId: {
            type: Number,
            default: 0,
        },
        brandId: {
            type: Number,
            default: 0,
        },
        brandName: {
            type: String,
            default: '',
        },
        textureName: {
            type: String,
            default: '',
        },
        materialsName: {
            type: String,
            default: '',
        },
        phoneName: {
            type: String,
            default: '',
        },
        price: {
            type: Number,
            default: 0,
        },
        manufactors: {
            type: String,
            default: 0,
        },
        itemCode: {
            type: String,
            default: '',
        },
        itemId: {
            type: String,
            default: '',
        },
        //宽高比例
        proportion: {
            type: Number,
            default: 1,
        },
        underFrame: {
            type: Number,
            default: 1
        },
        topFrame: {
            type: Number,
            default: 1
        },
        rightFrame: {
            type: Number,
            default: 1
        },
        leftFrame: {
            type: Number,
            default: 1
        },
        height: {
            type: Number,
            default: 1
        },
        IsglanceOver: {
            type: Boolean,
            default: false
        },
        Price: {
            type: Number,
            default: 0
        },
        imgId: {
            type: Number,
            default: 0
        }
    },
    data() {
        return {
            bgwindowHeight: 1,
            ImageWidth: 1,//图片宽
            x: 0,
            picture: '',
            iscollect: true,//是否收藏
            collectId: '',//收藏id
            goodsId: '',//加购id
            isonExportJSON: true,//是否加购
            previewImage: '',
            pictureId: 0,
        }
    },
    watch: {
        IsglanceOver: {
            handler(newName, oldName) {
                if (oldName != undefined) {
                    uni.showLoading({
                        title: '加载中',
                        mask: true
                    });
                    this.ImageWidth = this.windowHeight / this.proportion;
                    this.x = (uni.upx2px(680) - this.ImageWidth) / 2;
                    this.height = this.windowHeight / this.height;
                    this.$refs.popup.open('center');
                    this.iscollect = true;
                    this.isonExportJSON = true;
                    this.previewImage = '';
                    this.collectId = '';//收藏id
                    this.goodsId = '';//加购id
                    this.pictureId = this.imgId;
                    this.getImageInfoDate();
                }
            },
            immediate: true
        }
    },
    mounted() {
        this.windowHeight = uni.upx2px(780);
        // this.ImageWidth = this.windowHeight / this.proportion;
        // this.x = (uni.upx2px(680) - this.ImageWidth) / 2;
        // this.height = this.windowHeight / this.height;
        // this.$refs.popup.open('center');
        // this.iscollect = true;
        // this.getImageInfoDate();
    },
    methods: {
        orderConfirm() {
            if (this.previewImage == '') {
                let w = Math.floor(this.ImageWidth);
                let h = Math.floor(this.windowHeight);
                let canvasName = 'glanceOvertCanvas';
                uni.canvasToTempFilePath({
                    x: this.x,
                    y: 0,
                    width: w,
                    height: h,
                    destWidth: w * 1.5,
                    destHeight: h * 1.5,
                    canvasId: canvasName,
                    success: (res) => {
                        this.collectH5(res.tempFilePath, 2);
                    },
                    fail(res) {
                        console.log("失败了：", res);
                    }
                })
            } else {
                let info = {
                    diy: {
                        categoryId: 1,
                        categoryName: '手机壳',
                        brandId: this.brandId,
                        brandName: this.brandName || 'apple',
                        generateImage: this.img,
                        materialId: this.materialsId,
                        materialName: this.materialsName,
                        modelId: this.modelId,
                        modelName: this.phoneName,
                        pictureId: this.pictureId,
                        pictureName: this.picture,
                        previewImage: this.previewImage,
                        manufactors: this.manufactors//,//YC云创 MK麦客 LHW联辉王
                    },
                    itemName: this.textureName,
                    itemCode: this.itemCode,
                    itemCount: 1,
                    itemType: 1, // 是否赠品：1 非赠品，2 赠品
                    salePrice: this.price, // 价格
                };
                var goods = [];
                goods.push(info);
                uni.setStorageSync("totalCount", 1);
                uni.setStorageSync("goods", JSON.stringify(goods));
                uni.navigateTo({
                    url: '../../good_pages/confirmOrder/confirmOrder',
                });
            }
        },
        close() {
            this.$refs.popup.close();
        },
        onExportJSON() {
            uni.showLoading({
                title: '加载中',
                mask: true
            });
            if (this.isonExportJSON) {
                if (!this.iscollect) {
                    this.addToShopcart(this.previewImage);
                } else {
                    let w = Math.floor(this.ImageWidth);
                    let h = Math.floor(this.windowHeight);
                    let canvasName = 'glanceOvertCanvas';
                    uni.canvasToTempFilePath({
                        x: this.x,
                        y: 0,
                        width: w,
                        height: h,
                        destWidth: w * 1.5,
                        destHeight: h * 1.5,
                        canvasId: canvasName,
                        success: (res) => {
                            this.collectH5(res.tempFilePath, 1);
                        },
                        fail(res) {
                            console.log("失败了：", res);
                        }
                    })
                }
            } else {
                this.$api.delete(this, api.deleteShopcart, {
                    ids: [this.goodsId],
                }).then((res) => {
                    if (res.success) {
                        uni.showToast({
                            icon: "none",
                            title: "已删除购物车",
                            duration: 2000,
                        });
                        this.$emit("updateShoppingNumber", '');
                        this.isonExportJSON = true;
                        this.goodsId = '';
                    } else {
                        uni.showToast({
                            icon: 'none',
                            title: res.errMessage,
                            duration: 2000
                        });
                    }
                    uni.hideLoading()
                }).catch(() => { });
            }
        },
        //收藏
        collect() {
            uni.showLoading({
                title: '加载中',
                mask: true
            });
            if (this.iscollect) {
                if (!this.isonExportJSON) {
                    this.collection(this.previewImage);
                }
                else {
                    let w = Math.floor(this.ImageWidth);
                    let h = Math.floor(this.windowHeight);
                    let canvasName = 'glanceOvertCanvas';
                    uni.canvasToTempFilePath({
                        x: this.x,
                        y: 0,
                        width: w,
                        height: h,
                        destWidth: w * 1.5,
                        destHeight: h * 1.5,
                        canvasId: canvasName,
                        success: (res) => {
                            this.collectH5(res.tempFilePath, 0);
                        },
                        fail(res) {
                            console.log("失败了：", res);
                        }
                    })
                }
            }
            else {
                this.$api.delete(this, api.getCollectionList, {
                    id: this.collectId
                }).then((res) => {
                    if (res.success) {
                        uni.showToast({
                            title: "已取消收藏",
                            icon: "none",
                            duration: 2000
                        });
                        this.iscollect = true;//是否收藏
                        this.collectId = '';
                        this.$emit("updateCollectNumber", '');
                    } else {
                        uni.showToast({
                            title: res.errMessage,
                            icon: "none",
                            duration: 2000
                        });
                    }
                    uni.hideLoading()
                })
            }
        },
        collectH5(imgArr, type) {
            var that = this;
            let _fileBlob = that.dataURLtoBlob(imgArr);
            // blob转换成file文件
            let file = new File([_fileBlob], new Date() + '.png');
            // 随机命名
            let randomName = that.random_string(6) + "_" + new Date().getTime() + ".png";
            that.$api.get(that, api.uploadFile).then((result) => {
                this.picture = randomName;
                let client = new OSS.Wrapper({
                    region: result.data.region,
                    accessKeyId: result.data.accessKeyId,
                    accessKeySecret: result.data.accessKeySecret,
                    stsToken: result.data.securityToken,
                    bucket: result.data.bucketName,
                    endpoint: result.data.endpoint,
                    secure: true,
                });
                client.multipartUpload(result.data.pathName + "/" + randomName, file, {}).then((results) => {
                    return new Promise((resolve, reject) => {
                        var img = result.data.hostname + results.name;
                        this.previewImage = img;
                        if (type == 0) {
                            this.collection(img);
                        } else if (type == 2) {
                            this.orderConfirm(img);
                        } else {
                            this.addToShopcart(img);
                        }

                        resolve(true);
                    });
                })
                    .catch((err) => {
                        console.log("失败了", err);
                    });
            })
        },
        addToShopcart(img) {
            this.previewImage = img;
            let info = {
                categoryId: 1,
                categoryName: '手机壳',
                brandId: this.brandId,
                brandName: this.brandName || 'apple',
                generateImage: this.img,
                materialId: this.materialsId,
                materialName: this.materialsName,
                modelId: this.modelId,
                modelName: this.phoneName,
                pictureId: this.pictureId,
                pictureName: this.picture,
                previewImage: this.previewImage,
                manufactors: this.manufactors//,//YC云创 MK麦客 LHW联辉王
            };
            this.$api.post(this, api.addToShopcart, {
                diy: info,
                itemName: this.textureName,
                itemCode: this.itemCode,
                itemCount: 1,
                itemType: 1, // 是否赠品：1 非赠品，2 赠品
                salePrice: this.price, // 价格
            }).then((res) => {
                if (res.success) {
                    this.isonExportJSON = false;
                    this.goodsId = res.data;
                    uni.showToast({
                        title: '成功加入购物车',
                        icon: "none",
                        duration: 2000
                    });
                    this.$emit("updateShoppingNumber", '');
                } else {
                    uni.showToast({
                        title: res.errMessage,
                        icon: "none",
                        duration: 2000
                    });
                }
                uni.hideLoading()
            })
        },
        collection(img) {
            this.previewImage = img;
            this.$api.post(this, api.collection, {
                brandId: this.brandId,
                brandName: this.brandName,
                categoryId: 1,
                categoryName: '手机壳',
                generateImage: this.img,
                itemName: this.textureName,
                itemCode: this.itemCode,
                itemId: this.itemId,
                materialId: this.materialsId,
                materialName: this.materialsName,
                modelId: this.modelId,
                modelName: this.phoneName,
                pictureId: this.pictureId,
                pictureName: this.picture,
                previewImage: this.previewImage,
                manufactors: this.manufactors//,//YC云创 MK麦客 LHW联辉王
            }).then((res) => {
                if (res.success) {
                    this.iscollect = false;//是否收藏
                    this.collectId = res.data;
                    uni.showToast({
                        title: "收藏成功",
                        icon: "none",
                        duration: 2000
                    });
                    this.$emit("updateCollectNumber", '');
                }
                else {
                    uni.showToast({
                        title: res.errMessage,
                        icon: "none",
                        duration: 2000
                    });
                }
                uni.hideLoading()
            })
        },
        getImageInfoDate() {
            this.topFrame = this.topFrame == 63.2 ? 59 : this.topFrame;//上边框
            var cont = uni.createCanvasContext('glanceOvertCanvas');
            var secondX = this.x + (this.leftFrame * this.height);
            var secondY = this.topFrame * this.height;
            uni.getImageInfo({
                src: this.img,
                success: res => {
                    cont.clearRect(0, 0, this.ImageWidth, this.windowHeight)
                    cont.drawImage(this.hollowOut, this.x, 0, this.ImageWidth, this.windowHeight);
                    cont.globalCompositeOperation = 'source-atop';
                    cont.drawImage(this.img, secondX, secondY, this.ImageWidth - (this.leftFrame * this.height * 2), this.windowHeight - (secondY * 2));
                    cont.globalCompositeOperation = 'source-over';
                    cont.drawImage(this.defaultImage, this.x, 0, this.ImageWidth, this.windowHeight);
                    cont.fill();
                    cont.draw();
                    uni.hideLoading()
                }
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
        //px转rpx
        pxToRpx(px) {
            //计算比例
            let scale = uni.upx2px(100) / 100;
            return px / scale
        },
    }
}
</script>
<style scoped lang="scss">
.preview_box {
    width: 680rpx;
    height: 1000rpx;
    border-radius: 30rpx;
    background-color: rgba(255, 255, 255, 1);
    color: rgba(16, 16, 16, 1);
    border: 1rpx solid rgba(239, 239, 239, 1);
    .sele_img_title_box {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 680rpx;
        height: 80rpx;
        padding: 0 20rpx;
        box-sizing: border-box;
        color: rgba(59, 59, 59, 1);
        font-size: 32rpx;
        font-family: OPPOSans-bold;
    }
    .model_type_box {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 680rpx;
        height: 80rpx;
        padding: 0 40rpx;
        box-sizing: border-box;
        color: rgba(59, 59, 59, 1);
        font-size: 24rpx;
        font-family: OPPOSans-bold;
        margin-top: 60rpx;
        .model_type_mue {
            display: flex;
            align-items: center;
            image {
                width: 40rpx;
                height: 40rpx;
                margin-right: 5rpx;
            }
        }
    }
}
.operation_box {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 680rpx;
    margin-top: 40rpx;
    .operation_meu {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 160rpx;
        height: 80rpx;
        border-radius: 100rpx;
        background-color: rgba(255, 255, 255, 1);
        color: rgba(16, 16, 16, 1);
        font-size: 28rpx;
        text-align: center;
        font-family: Arial;
        border: 1rpx solid rgba(239, 239, 239, 1);
        image {
            width: 42rpx;
            height: 42rpx;
        }
    }
}
</style>