<template>
    <view>
        <canvas :style="{height:canvasHeight+'rpx'}" class="image_box" canvas-id="firstCanvas" id="firstCanvas"></canvas>
        <view style="height: 200rpx;"></view>
        <cover-view class="share_box">
            <cover-view class="share_item_box" @click="poster">
                <cover-image class="share_item_image" src="../../../static/icons/my/order/material.png"></cover-image>
                <cover-view>海报下载</cover-view>
            </cover-view>
            <cover-view class="share_item_box" @click="download">
                <cover-image class="share_item_image" src="../../../static/icons/my/url.png"></cover-image>
                <cover-view>二维码下载</cover-view>
            </cover-view>
            <!-- <cover-view class="share_item_box" @click="dialogwx">
                <image class="share_item_image" src="../../../static/icons/my/url.png"></image>
                复制链接
            </cover-view> -->

            <cover-view class="share_item_box">
                <button :plain="true" id="shareBtn" open-type="share">
                    <cover-image class="share_item_image" src="../../../static/icons/my/share.png"></cover-image>
                    <cover-view class="share_txt">分享</cover-view>
                </button>
            </cover-view>

        </cover-view>

    </view>
</template>
<script>
import api from "../../../common/js/allApi";
export default {
    onShareAppMessage(res) {
        // https://bat-com.oss-cn-shenzhen.aliyuncs.com/diy/h51686618598458.jpg
        if (res.from === 'button') {// 来自页面内分享按钮
            console.log(res.target)
        }
        if (this.type == 2) {
            return {
                title: '合作邀请',
                imageUrl: '分享图片路径', //自定义图片路径，可以是本地文件路径、代码包文件路径或者网络图片路径。支持PNG及JPG。显示图片长宽比是 5:4。
                path: '/good_pages/distribution/user/apply_for?distributorId=' + uni.getStorageSync("distributorId") + '&platform=' + uni.getStorageSync("platform") + '&shopId=' + uni.getStorageSync("shopId") + '&shopCode=' + uni.getStorageSync("shopCode") + '&staffCode=' + uni.getStorageSync("userNo"),
                success: function (res) {
                    //成功回调
                }
            }
        } else {
            return {
                title: '合作邀请',
                imageUrl: '分享图片路径', //自定义图片路径，可以是本地文件路径、代码包文件路径或者网络图片路径。支持PNG及JPG。显示图片长宽比是 5:4。
                path: '/src/components/index/index?distributorId=' + uni.getStorageSync("distributorId") + '&platform=' + uni.getStorageSync("platform") + '&shopId=' + uni.getStorageSync("shopId") + '&shopCode=' + uni.getStorageSync("shopCode") + '&userNo=' + uni.getStorageSync("userNo"),
                success: function (res) {
                    //成功回调
                }
            }
        }

    },
    data() {
        return {
            Height: 0,
            pr: '',
            canvasHeight: 0,
            type: 1,//1:分销员；2:分销商
        };
    },
    onLoad(parameter) {
        uni.showLoading({
            title: '加载中',
            mask: true
        });
        this.canvasHeight = this.pxToRpx(uni.upx2px(1300));
        this.Height = uni.upx2px(this.canvasHeight);
        this.type = parameter.type;
        if (parameter.type == 1) {
            uni.setNavigationBarTitle({
                title: '分享'
            });
            // this.getQrcode('https://bat.oss-cn-shenzhen.aliyuncs.com/diy/h51683859898543.jpg');
            this.getQrcode('https://bat-com.oss-cn-shenzhen.aliyuncs.com/diy/h51686625699920.blob:http://localhost:8080/d5f12c8a-48ea-42ac-9250-a4b5680346e0');
        } else {
            // this.getDistributorQrcode('https://bat-com.oss-cn-shenzhen.aliyuncs.com/diy/h51686625699920.blob:http://localhost:8080/d5f12c8a-48ea-42ac-9250-a4b5680346e0');
            this.getDistributorQrcode('https://bat.oss-cn-shenzhen.aliyuncs.com//diy/h51681973180813.jpg');
        }
    },
    methods: {
        pxToRpx(px) {
            //计算比例
            let scale = uni.upx2px(100) / 100;
            return px / scale
        },
        getDistributorQrcode(img) {
            this.$api.get(this, api.distributorQrcode, {
                distributorId: uni.getStorageSync("distributorId"),
                shopCode: uni.getStorageSync("shopCode"),
                staffCode: uni.getStorageSync("userNo")
            }).then((res1) => {
                this.pr = res1.data;
                const context = uni.createCanvasContext('firstCanvas')
                uni.getImageInfo({
                    src: img,
                    success: res => {
                        context.drawImage(res.path, 0, 0, uni.upx2px(750), this.Height);
                        var x = (uni.upx2px(750) - uni.upx2px(300)) / 2;
                        var y = this.Height - uni.upx2px(580);
                        uni.getImageInfo({
                            src: res1.data,
                            success: res => {
                                context.drawImage(res.path, x, y, uni.upx2px(300), uni.upx2px(300));
                                context.fill();
                                context.draw();
                                uni.hideLoading()
                            }
                        })
                    }
                })
            });
        },
        getQrcode(img) {
            this.$api.get(this, api.getQrcode, {
                distributorId: uni.getStorageSync("distributorId"),
                shopCode: uni.getStorageSync("shopCode"),
                staffCode: uni.getStorageSync("userNo")
            }).then((res1) => {
                this.pr = res1.data;
                const context = uni.createCanvasContext('firstCanvas')
                uni.getImageInfo({
                    src: img,
                    success: res => {
                        context.drawImage(res.path, 0, 0, uni.upx2px(750), this.Height);
                        // var x = (uni.upx2px(750) - uni.upx2px(300)) / 2;
                        // var y = this.Height - uni.upx2px(600);
                        var x = (uni.upx2px(750) - uni.upx2px(160)) / 2;
                        var y = this.Height - uni.upx2px(230);
                        uni.getImageInfo({
                            src: res1.data,
                            success: res => {
                                context.drawImage(res.path, x, y, uni.upx2px(160), uni.upx2px(160));
                                context.fill();
                                context.draw();
                                uni.hideLoading()
                            }
                        })
                    }
                })
            });
        },
        //下载海报
        poster() {
            uni.showLoading({
                title: '下载中',
                mask: true
            });
            uni.canvasToTempFilePath({
                x: 0,
                y: 20,
                width: uni.upx2px(750),
                height: this.Height,
                destWidth: uni.upx2px(750) * 3,
                destHeight: this.Height * 3,
                canvasId: 'firstCanvas',
                success: (res2) => {
                    var that = this;
                    let file = res2.tempFilePath;
                    // 随机命名
                    let randomName = that.random_string(6) + "_" + new Date().getTime() + "." + file.split(".").pop();
                    that.$api.get(that, api.getOSSSts, { dir: 'diy/' }).then((result) => {
                        const index = file.lastIndexOf("\.");
                        const imgExtension = file.substring(index + 1, file.length);
                        const imgPath = 'diy/h5' + new Date().getTime() + "." + imgExtension;
                        uni.uploadFile({
                            url: result.data.hostname,
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

                                uni.downloadFile({
                                    url: result.data.hostname + imgPath, // 这里是我已经请求好的图片数据
                                    success: (res) => {
                                        console.log(res, 'res')
                                        var tempFilePath = res.tempFilePath;
                                        uni.saveImageToPhotosAlbum({ // 然后调用这个方法
                                            filePath: tempFilePath,
                                            success: (res) => {
                                                uni.hideLoading()
                                                uni.showToast({
                                                    title: '图片已保存'
                                                })
                                            }
                                        })
                                    },
                                    fail: (e) => {
                                        uni.hideLoading()
                                        uni.showToast({
                                            title: '图片保存失败'
                                        })
                                    }
                                });
                            }, fail: function (res) {
                                uni.hideLoading()
                                console.log("失败了", res)
                            }
                        });

                    })





                }
            })
        },
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
        //下载二维码
        download() {
            uni.downloadFile({
                url: this.pr, // 这里是我已经请求好的图片数据
                success: (res) => {
                    console.log(res, 'res')
                    var tempFilePath = res.tempFilePath;
                    uni.saveImageToPhotosAlbum({ // 然后调用这个方法
                        filePath: tempFilePath,
                        success: (res) => {
                            uni.showToast({
                                title: '图片已保存'
                            })
                        }
                    })
                },
                fail: () => {
                    uni.showToast({
                        title: '图片保存失败'
                    })
                }
            });
        },
        //复制链接
        dialogwx() {
            uni.setClipboardData({
                data: '链接地址',
                success: () => {
                    uni.showToast({
                        title: `复制成功`,
                        icon: 'none'
                    })
                }
            }, true);
        },
    },

};
</script>
<style lang="scss" scoped>
#shareBtn {
    display: block;
    border: none;
    // height: 160rpx;
    // width: 130rpx;
    // opacity: 1;
    font-size: 24rpx;
    // text-align: center;
    // background-color: #ffffff;
    // opacity: 1;
    display: flex;
    justify-content: space-between;
    align-items: center;
    color: rgba(16, 16, 16, 1);
    font-size: 24rpx;
    .share_item_image {
        margin-top: 10rpx;
    }
    .share_txt {
        margin-top: -5rpx;
    }
}
.image_box {
    // margin: 0 auto;
    width: 750rpx;
    // height: 1000rpx;
}
.share_box {
    position: fixed;
    z-index: 999;
    bottom: 0;
    background-color: #ffffff;
    padding: 0 30rpx;
    box-sizing: border-box;
    width: 750rpx;
    // height: 180rpx;
    display: flex;
    justify-content: space-between;
    align-items: center;
    color: rgba(16, 16, 16, 1);
    font-size: 24rpx;
    padding-bottom: constant(safe-area-inset-bottom);
    padding-bottom: env(safe-area-inset-bottom);
    .share_item_box {
        height: 140rpx;
        width: 130rpx;
        text-align: center;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        align-items: center;

        .share_item_image {
            width: 100rpx;
            height: 100rpx;
        }

        image {
            width: 60rpx;
            height: 60rpx;
        }
    }
}
</style>