<template>
    <view>
        <!-- #ifdef MP-WEIXIN -->
        <uni-nav-bar left-icon="left" @clickLeft="back" dark :fixed="true" shadow background-color="#ffffff" color="#000000" status-bar title="预览" />
        <!-- #endif -->
        <cover-image v-if="!isShowcanvas" style="height: ;" src="https://bat.oss-cn-shenzhen.aliyuncs.com//diy/h51675849670851.png" class="up-box" :style="{
          width: '750rpx', height:bgwindowHeight+'rpx',zIndex: 12}">
        </cover-image>
        <image v-else style="height: ;" src="https://bat.oss-cn-shenzhen.aliyuncs.com//diy/h51675849670851.png" class="up-box" :style="{
          width: '750rpx', height:bgwindowHeight+'rpx'}">
        </image>
        <view :class="isShowcanvas?'':'is_show_canvas'">
            <canvas-drag ref="canvasRef" id="canvas-drag" :hollowOut="hollowOut" :modelHeight="modelHeight" :windowHeight="windowHeight" :graph="graph" :backgroundImage="defaultImage" :floorImage="backgroundImage" :height="height" :width="width" :proportion="proportion" class="canvasArea" :enableUndo="true"></canvas-drag>
        </view>
        <!-- v-show="IsbgCanvas" -->
        <canvas v-show="IsbgCanvas" style="position: absolute;" :style="{width:'750rpx', height: bgwindowHeight+'rpx',zIndex: 1,top: '200rpx'}" canvas-id="firstCanvas" id="firstCanvas"></canvas>

        <view class="operation_type_box">
            <view class="intelligibility_box" @click="collect" style="margin-left: 40rpx;">
                <image src="../../static/icons/collect.png"></image>
                <view>收藏</view>
            </view>
            <view class="phone_box">
                <view class="phone_name"> {{phoneName}} - {{materialsName}} {{materialsName1}}</view>
                <view class="phone_price">¥ {{price}}</view>
            </view>
            <view class="intelligibility_box" @click="onExportJSON" style="margin-right: 40rpx;">
                <image src="../../static/icons/shop.png"></image>
                <view>加购</view>
            </view>
        </view>
        <Home_Tabar></Home_Tabar>
    </view>
</template>
<script>
import { pathToBase64, base64ToPath } from 'image-tools'
import canvasDrag from "./components/canvas-drag/glance_over";        //画板
import Home_Tabar from "./components/homeTabar";                      //底部导航栏
import api from "../../common/js/allApi";
export default {
    components: {
        canvasDrag,
        Home_Tabar
    },
    data() {
        return {
            isShowcanvas: false,
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
            bgwindowHeight: 1,
            canvasX: 0,
            canvasY: 0,
            ratio: 1,
            modelHeight: 1,//模型高度
            modelWidth: 1,//模型宽
            hollowOut: '',
            IsbgCanvas: true,
        }
    },
    onLoad(data) {
        //#ifdef H5
        let oss = document.createElement("script");
        oss.type = "text/javascript";
        oss.src = "http://gosspublic.alicdn.com/aliyun-oss-sdk-4.4.4.min.js";
        document.body.appendChild(oss);
        this.windowHeight = uni.getStorageSync("nakedHeight") - 200;
        this.bgwindowHeight = this.windowHeight - 140;
        // #endif
        // #ifdef MP-WEIXIN
        this.windowHeight = uni.getStorageSync("nakedHeight")
        this.bgwindowHeight = this.windowHeight - 180;
        // #endif
        uni.showLoading({
            title: '加载中',
            mask: true
        });
        uni.setStorageSync("brandName", data.brandName);
        this.modelHeight = this.windowHeight - 550;
        this.ratio = uni.getStorageSync("ratio");
        this.bs = data.img;
        this.hollowOut = data.hollowOut;

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
        this.GetmodelMaterialRelevance();


    },
    methods: {
        back() {
            uni.navigateBack({ delta: 1 })
        },
        //收藏
        collect() {
            if (this.iscollect) {
                this.$refs.canvasRef.exportFun().then(imgArr => {
                    // #ifdef H5
                    this.collectH5(imgArr);
                    // #endif
                    // #ifdef MP-WEIXIN
                    var that = this;
                    let file = imgArr;
                    let randomName = that.random_string(6) + "_" + new Date().getTime() + "." + file.split(".").pop();
                    that.$api.get(that, api.getOSSSts, { dir: 'diy/' }).then((result) => {
                        const index = file.lastIndexOf("\.");
                        const imgExtension = file.substring(index + 1, file.length);
                        const imgPath = 'diy/h5' + new Date().getTime() + "." + imgExtension;

                        uni.uploadFile({
                            url: result.data.hostname, //仅为示例，非真实的接口地址
                            filePath: file,
                            name: 'file',
                            formData: {
                                name: randomName,
                                key: imgPath,
                                policy: result.data.policy,
                                OSSAccessKeyId: result.data.accessKeyId,
                                success_action_status: "200",
                                signature: result.data.signature,
                                'x-oss-security-token': result.data.securityToken
                            },
                            success: (uploadFileRes) => {
                                var img = result.data.hostname + '/' + imgPath;
                                this.collection(img);
                            }, fail: function (res) {
                                console.log("失败了", res)
                            }
                        });
                    })
                    // #endif
                }).catch(e => {
                    console.error(e);
                });

            }
            else {
                uni.showToast({
                    title: "已加入收藏",
                    icon: "none",
                    duration: 2000
                });
            }

        },
        collection(img) {
            this.$api.post(this, api.collection, {
                brandId: this.brandId,
                brandName: this.brandName,
                categoryId: 1,
                categoryName: '手机壳',
                generateImage: this.bs,
                itemCode: this.itemCode,
                itemId: this.itemId,
                materialId: this.materialsId,
                materialName: this.materialsName,
                modelId: this.modelId,
                modelName: this.phoneName,
                pictureId: this.pictureId,
                pictureName: this.picture,
                previewImage: img,
            }).then((res) => {
                if (res.success) {
                    this.iscollect = false;//是否收藏
                    uni.showToast({
                        title: "收藏成功",
                        icon: "none",
                        duration: 2000
                    });
                }
                else {
                    uni.showToast({
                        title: res.errMessage,
                        icon: "none",
                        duration: 2000
                    });
                }
            })
        },
        collectH5(imgArr) {
            var that = this;
            let _fileBlob = that.dataURLtoBlob(imgArr);
            // blob转换成file文件
            let file = new File([_fileBlob], new Date() + '.png');
            // 随机命名
            let randomName = that.random_string(6) + "_" + new Date().getTime() + ".png";
            if (this.manufactors == "KM_DH") {
                file = new File([_fileBlob], new Date() + '.jpg');
                // 随机命名
                randomName = that.random_string(6) + "_" + new Date().getTime() + ".jpg";
            }
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
                client.multipartUpload(result.data.pathName + "/" + randomName, file, {}).then((results) => {
                    return new Promise((resolve, reject) => {
                        var img = result.data.hostname + results.name;
                        this.collection(img);
                        resolve(true);
                    });
                })
                    .catch((err) => {
                        console.log("失败了", err);
                    });
            })
        },
        onExportJSON() {
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
            // #ifdef H5
            this.file_imgH5(imgArr);
            // #endif
            // #ifdef MP-WEIXIN
            var that = this;
            let file = imgArr;
            // 随机命名
            let randomName = that.random_string(6) + "_" + new Date().getTime() + "." + file.split(".").pop();
            that.$api.get(that, api.getOSSSts, { dir: 'diy/' }).then((result) => {
                this.picture = randomName;
                const index = file.lastIndexOf("\.");
                const imgExtension = file.substring(index + 1, file.length);
                const imgPath = 'diy/h5' + new Date().getTime() + "." + imgExtension;
                uni.uploadFile({
                    url: result.data.hostname, //仅为示例，非真实的接口地址
                    filePath: file,
                    name: 'file',
                    formData: {
                        name: randomName,
                        key: imgPath,
                        policy: result.data.policy,
                        OSSAccessKeyId: result.data.accessKeyId,
                        success_action_status: "200",
                        signature: result.data.signature,
                        'x-oss-security-token': result.data.securityToken
                    },
                    success: (uploadFileRes) => {
                        var img = result.data.hostname + '/' + imgPath;
                        this.addToShopcart(img);

                    }, fail: function (res) {
                        console.log("失败了", res)
                    }
                });

            })
            // #endif
        },
        addToShopcart(img) {
            let info = {
                categoryId: 1,
                categoryName: '手机壳',
                brandId: this.brandId,
                brandName: this.brandName || 'apple',
                generateImage: this.bs,
                materialId: this.materialsId,
                materialName: this.materialsName,
                modelId: this.modelId,
                modelName: this.phoneName,
                pictureId: this.pictureId,
                pictureName: this.picture,
                previewImage: img,
                manufactors: this.manufactors//,//YC云创 MK麦客 LHW联辉王
            };
            this.$api.post(this, api.addToShopcart, {
                diy: info,
                itemCode: this.itemCode,
                itemCount: 1,
                itemType: 1, // 是否赠品：1 非赠品，2 赠品
                salePrice: this.price, // 价格
            }).then((res) => {
                if (res.success) {
                    this.isonExportJSON = false;
                    uni.showToast({
                        title: '成功加入购物车',
                        icon: "none",
                        duration: 2000
                    });
                } else {
                    uni.showToast({
                        title: res.errMessage,
                        icon: "none",
                        duration: 2000
                    });
                }

            })
        },
        file_imgH5(imgArr) {
            var that = this;
            let _fileBlob = that.dataURLtoBlob(imgArr);
            // blob转换成file文件
            let file = new File([_fileBlob], new Date() + '.png');
            // 随机命名
            let randomName = that.random_string(6) + "_" + new Date().getTime() + ".png";
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
                console.log("材质信息：", res);
                this.defaultImage = res.data.outImage;//模型图
                this.backgroundImage = res.data.floorImage;//背景图
                this.height = res.data.length;//高度
                this.width = res.data.width;//宽
                this.proportion = res.data.length / res.data.width;
                this.leftFrame = res.data.leftFrame;//左边框
                this.rightFrame = res.data.rightFrame;//右边框
                this.topFrame = res.data.topFrame == 63.2 ? 59 : res.data.topFrame;//上边框
                this.underFrame = res.data.underFrame;//边框


                this.canvasX = (750 - (this.modelHeight / this.proportion)) / 2;
                this.canvasY = 100;
                this.modelWidth = this.modelHeight / this.proportion;
                this.setImage();

            })
        },
        setImage() {
            // #ifdef MP-WEIXIN
            var getBase64ImageUrl = this.getBase64ImageUrl(this.hollowOut);
            base64ToPath(getBase64ImageUrl).then(path => {
                this.getImageInfoDate(path, 6);
            })
            // #endif

            // #ifdef H5
            this.getImageInfoDate(this.hollowOut, 4.6);
            // var getBase64ImageUrl = this.getBase64ImageUrl(this.hollowOut);
            // base64ToPath(getBase64ImageUrl).then(path => {
            //     this.getImageInfoDate(path);
            // })
            // #endif
        },

        getImageInfoDate(path, multiple) {

            var width = Math.floor((this.modelWidth / this.ratio) * 1.3);
            var height = Math.floor((this.modelHeight / this.ratio) * 1.3);
            var context = uni.createCanvasContext('firstCanvas')
            var y1 = (Math.floor((this.topFrame * multiple)) / this.ratio) * 1.3;
            var x1 = (Math.floor((this.leftFrame * multiple)) / this.ratio) * 1.3;
            var width1 = Math.floor((this.modelWidth - ((this.leftFrame * 2) * multiple)) / this.ratio) * 1.3;
            var height1 = Math.floor((this.modelHeight - ((this.topFrame + this.underFrame) * multiple)) / this.ratio) * 1.3;
            uni.getImageInfo({
                src: this.bs,
                success: res1 => {
                    context.drawImage(res1.path, (x1 + 0), (y1 + 0), width1, height1);
                    // 如果在Canvas之中将某个物体（源）绘制在另一个物体（目标）之上
                    //显示方式
                    context.globalCompositeOperation = 'destination-out';
                    context.drawImage(path, 0, 0, width, height);
                    context.fill();
                    context.draw();
                    let _this = this
                    let canvasName = 'firstCanvas'
                    let w = width;
                    let h = height;
                    let timer2 = setInterval(() => {
                        uni.canvasToTempFilePath({
                            x: 0,
                            y: 0,
                            width: w,
                            height: h,
                            destWidth: w * 3,
                            destHeight: h * 3,
                            canvasId: canvasName,
                            success: (res) => {
                                _this.hollowOut = res.tempFilePath;
                                _this.IsbgCanvas = false;
                                _this.onImport2();
                                let timer1 = setInterval(() => {
                                    _this.onImport1();
                                    clearTimeout(timer1)
                                }, 400);
                                let timer = setInterval(() => {
                                    _this.onImport();
                                    clearTimeout(timer)
                                }, 800);
                            },
                            fail(res) {
                                console.log("失败了：", res);
                            }
                        })
                        clearTimeout(timer2)
                    }, 200);
                }
            })

        },
        onImport() {
            uni.getImageInfo({
                src: this.defaultImage,
                success: res => {
                    this.setData({
                        graph: {
                            y: 0,
                            x: 0,
                            w: this.modelWidth / this.ratio,
                            h: this.modelHeight / this.ratio,
                            type: 'image',
                            url: res.path,
                        }
                    });
                    uni.hideLoading();
                    this.isShowcanvas = true;
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
                            y: 0,
                            x: 0,
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
        getBase64ImageUrl(data) {
            /// 获取到base64Data
            var base64Data = data.slice(22);
            /// 通过微信小程序自带方法将base64转为二进制去除特殊符号，再转回base64
            base64Data = uni.arrayBufferToBase64(uni.base64ToArrayBuffer(base64Data));
            /// 拼接请求头，data格式可以为image/png或者image/jpeg等，看需求
            const base64ImgUrl = "data:image/png;base64," + base64Data;
            /// 刷新数据
            return base64ImgUrl;
        },
        onImport1() {
            // uni.getImageInfo({
            //     src: this.hollowOut,//this.bs,
            //     success: res => {
            //         uni.setStorageSync("bs", res.path)
            this.setData({
                graph: {
                    y: 0,
                    x: 0,
                    w: this.modelWidth / this.ratio,
                    h: this.modelHeight / this.ratio,
                    type: 'image',
                    url: this.hollowOut,
                }
            });
            //     },
            //     fail: res => {
            //         console.log(`缓存失败：${res.errMsg}`);
            //     }
            // })

        },
    }
}
</script>
<style scoped lang="scss">
.is_show_canvas {
    opacity: 0;
}
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
    z-index: 20;
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
    padding-bottom: constant(safe-area-inset-bottom); // 兼容 IOS<11.2
    padding-bottom: env(safe-area-inset-bottom); // 兼容 IOS>=11.2
    // padding: 0rpx 50rpx;
    bottom: 100rpx;
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
            margin-top: -14rpx;
            -webkit-transform: scale(0.8);
        }
    }
    .phone_box {
        width: 440rpx;
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
            width: 320rpx;
        }
        .phone_price {
            width: 80rpx;
            color: rgba(252, 126, 27, 1);
            font-size: 24rpx;
            font-weight: 600;
            text-align: right;
            font-family: PingFangSC-regular;
        }
    }
}
</style>