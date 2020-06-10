<template>
    <view>
        <view v-if="hollowOut==''">
            <cover-view style=" width: 750rpx; height: 100vh;z-index: 100;position: absolute;background-color: #efeff4; top: 0;">
            </cover-view>
            <canvas style="width: 750rpx; height: 100vh; z-index: 1;" canvas-id="firstCanvas" id="firstCanvas"></canvas>
        </view>
        <view v-else>
            <view style="position: absolute;top: 0;left: 0; z-index: 1;">
                <view class="shop_box" :style="{marginTop: (globalData.statusBarHeight+globalData.navigationBarHeight)+'rpx'}">
                </view>
            </view>
            <view class="uni-margin-wrap">
                <swiper class="swiper" indicator-active-color="#ffffff" circular :indicator-dots="indicatorDots" :autoplay="autoplay" :interval="interval" :duration="duration">
                    <swiper-item class="swiper_item" v-for="(item,index) in BannerList" :key="index" @click="goPath(item, {}, item.type)">
                        <image :src="item.bannerUrl"></image>
                    </swiper-item>
                </swiper>
            </view>
            <view v-if="platform!=7" class="custom_entrance_box" @click="gocustomization()">
                <image src="https://bat-com.oss-cn-shenzhen.aliyuncs.com/diy/h51686972631961.png"></image>
                <!-- <image src="https://bat.oss-cn-shenzhen.aliyuncs.com/diy/h51684390165831.png"></image> -->
            </view>
            <view style="height: 180rpx;" v-if="platform!=7"></view>
            <!-- 主题 -->
            <Recommended @clickCount="clickCount" @imgTypeId="gainImgId"></Recommended>
            <!-- 主题列表 -->
            <SeriesList v-if="(selCount==-1)" v-for="(item,index) in themeList" :key="index" :themeId="item.id" :item="item"></SeriesList>
            <view v-if="(selCount==-1&&themeList.length!=0)" class="no_more">没有更多了哦 ~</view>
            <view v-if="(selCount==-1&&themeList.length==0)" class="no_more">暂无推荐主题 ~</view>
            <!-- 作品列表 -->
            <PictureList v-if="(selCount!=-1)" :imgTypeId="imgTypeId"></PictureList>
            <!-- 兑换券弹窗 -->
            <!-- <Convertibility></Convertibility> -->
        </view>
    </view>
</template>
<script>
// #ifdef H5
import wx from "weixin-js-sdk";
// #endif
import api from "common/js/allApi.js";
import Recommended from "components/recommended.vue";               //主    题
import SeriesList from "components/seriesList.vue";                 //主题列表
import PictureList from "components/pictureList.vue";               //作品列表
import Convertibility from "components/convertibility";             //兑换券弹窗
export default {
    components: {
        Recommended,
        SeriesList,
        PictureList,
        Convertibility
    },
    data() {
        return {
            hollowOut: '',
            path: 'src/components/index/index',
            Ispath: false,
            indicatorDots: true,
            autoplay: true,
            interval: 3000,
            duration: 500,
            BannerList: [],
            distributorId: '',
            page: 1,
            size: 10,
            nextPage: 0,
            themeList: [],//主题列表
            selCount: -1,//选择的主题
            imgTypeId: '',//图片类型id
            shopId: '',//店铺id
            globalData: {
                statusBarHeight: 0, // 状态导航栏高度
                navHeight: 0, // 总体高度
                navigationBarHeight: 0, // 导航栏高度(标题栏高度)
            },
            platform: 0,
            shopLogo: '',
            h5userId: '',
            IsWebClose: false,
            Iscertificate: false,
            discount: 0,
        }
    },
    onPullDownRefresh() {
        //获取店铺信息
        if (this.shopId != '') {
            this.ShopStatus(this.shopId);
        }
        if (uni.getStorageSync("openId") == '' || uni.getStorageSync("openId") == null || uni.getStorageSync("openId") == undefined) {
            this.getOpenid();
        }
        else {
            this.openidlogin();
        }
        setTimeout(function () {
            uni.stopPullDownRefresh();
        }, 1000);
    },
    onReachBottom() {
        if (this.page < this.nextPage) {
            this.page += 1;
            uni.showLoading({
                title: '加载中',
                mask: true
            });
            this.getThemeLis();
        }
    },
    onShow() {
        // wxd111f057f262d51b 测试
        // wx05cb4496de7a20d7 正式
        //首页 tabBar栏定制索引
        uni.setStorageSync("index", 0)
        uni.setStorageSync("setText", false);
        if (uni.getStorageSync("platform") == 7) {
            if (this.themeList.length == 0) {
                this.getThemeLis();
            }
        }
    },
    onLoad(options) {
        // uni.setStorageSync("platform", 'JKWXAPP');
        // uni.setStorageSync("distributorId", 9486);//本地调试使用 
        // uni.setStorageSync("shopId", 1030);//本地调试使用

        // uni.setStorageSync("platform", 1);
        // uni.setStorageSync("distributorId", 9230);
        // uni.setStorageSync("shopId", 1045);//本地调试使用

        // 分销员分享二维码进入
        if (options.q != undefined) {
            var qrCode = decodeURIComponent(options.q);
            var data = qrCode.split('?')[1];
            data.split('&').forEach(item => {
                if (item.split('=')[0] == 'distributorId') {
                    uni.setStorageSync("distributorId", item.split('=')[1]);
                }
                else if (item.split('=')[0] == 'platform') {
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
                else if (item.split('=')[0] == 'staffCode') {
                    if (item.split('=')[1] == '{4}') {
                        uni.setStorageSync("superiorCode", '');
                    } else {
                        uni.setStorageSync("superiorCode", item.split('=')[1]);
                    }
                }
                else if (item.split('=')[0] == 'userNo') {
                    uni.setStorageSync("superiorCode", item.split('=')[1]);
                }
            });
        }
        //如果参数中带有用户编码，说明是从h5进来授权的
        if (options.staffCode != undefined) {
            uni.setStorageSync("distributorId", options.distributorId);
            uni.setStorageSync("shopId", options.shopId);
            uni.setStorageSync("shopCode", options.shopCode);
            uni.setStorageSync("staffCode", options.staffCode);
            this.distributorId = options.distributorId;
            this.shopId = options.shopId;

            uni.setStorageSync("shopName", '');
            uni.setStorageSync("qrUrl", '');
            uni.setStorageSync("thirdQrUrl", '');
            uni.setStorageSync("h5userId", '');
            uni.setStorageSync("h5openId", '');
            this.getOpenid();
            uni.navigateTo({
                url: '../../getuser/web_userid'
            });
        }
        //公众号菜单进入
        else {
            if (options.distributorId != undefined) {
                uni.setStorageSync("distributorId", options.distributorId);
                uni.setStorageSync("shopId", '');
                uni.setStorageSync("shopCode", '');
                uni.setStorageSync("shopName", '');
                uni.setStorageSync("qrUrl", '');
                uni.setStorageSync("thirdQrUrl", '');
                uni.setStorageSync("h5userId", '');
                uni.setStorageSync("h5openId", '');
                uni.setStorageSync("aliAuthorize", 0);//是否绑定支付宝
                uni.setStorageSync("wxAuthorize", 0);//是否绑定微信
                //如果是苏宁 跳转兑换券领取
                if (options.distributorId == 9526) {
                    this.Iscertificate = true;
                }
            }
            if (options.shopId != undefined) {
                uni.setStorageSync("shopId", options.shopId);
            }
            if (options.shopCode != undefined) {
                uni.setStorageSync("shopCode", options.shopCode);
            }
            if (options.userNo != undefined) {
                uni.setStorageSync("superiorCode", options.userNo);
            }
            if (options.platform != undefined) {
                if (uni.getAccountInfoSync().miniProgram.appId == 'wx05cb4496de7a20d7') {
                    uni.setStorageSync("platform", options.platform == 1 ? 'DZXCX' : options.platform);
                }
                else {
                    uni.setStorageSync("platform", options.platform == 1 ? 'JKWXAPP' : options.platform);
                }
            }
            if (uni.getStorageSync("platform") == '') {
                uni.setStorageSync("platform", 'DZXCX');
            }
            if (uni.getStorageSync("distributorId") == '') {
                uni.setStorageSync("distributorId", 2601);//本地调试使用 
            }


            var ratio = uni.getStorageSync("ratio");//比例
            this.distributorId = uni.getStorageSync("distributorId");
            this.platform = uni.getStorageSync("platform");
            this.shopId = uni.getStorageSync("shopId");
            // #ifdef MP-WEIXIN
            wx.showShareMenu({
                withShareTicket: true,
                //设置下方的Menus菜单，才能够让发送给朋友与分享到朋友圈两个按钮可以点击
                menus: ["shareAppMessage", "shareTimeline"]
            })
            // 状态栏高度
            this.globalData.statusBarHeight = uni.getSystemInfoSync().statusBarHeight
            // 获取微信胶囊的位置信息 width,height,top,right,left,bottom
            const custom = uni.getMenuButtonBoundingClientRect()
            // 导航栏高度(标题栏高度) = 胶囊高度 + (顶部距离 - 状态栏高度) * 2
            this.globalData.navigationBarHeight = custom.height + (custom.top - this.globalData.statusBarHeight) * 2
            // 总体高度 = 状态栏高度 + 导航栏高度
            this.globalData.navHeight = (this.globalData.navigationBarHeight + this.globalData.statusBarHeight + 4) * ratio;
            if (uni.getStorageSync("openId") == '' || uni.getStorageSync("openId") == null || uni.getStorageSync("openId") == undefined) {
                this.getOpenid();
            }
            else {
                this.openidlogin();
            }
            // #endif
            // #ifdef H5
            if (this.platform == 7) {
                if (this.platform == 7 && (uni.getStorageSync("loginSan") == undefined || uni.getStorageSync("loginSan") == '' || uni.getStorageSync("loginSan") != 71) && uni.getStorageSync("platform") == 7) {
                    localStorage.removeItem("staffType");
                    localStorage.removeItem("customerShopCheck");
                    localStorage.removeItem("rxShopSwitch");
                    localStorage.removeItem("shopCode");
                    localStorage.removeItem("shopName");
                    localStorage.removeItem("userId");
                    localStorage.removeItem("userNo");
                    localStorage.removeItem("phone");
                    localStorage.removeItem("goods");
                    localStorage.removeItem("uid");
                    localStorage.removeItem("auth");
                    localStorage.removeItem("authenticateUserID");
                    localStorage.removeItem("sa");
                    localStorage.removeItem("aliAuthorize");
                    localStorage.removeItem("wxAuthorize");
                    localStorage.removeItem("shopId");
                    uni.redirectTo({
                        url: '/good_pages/login/login?platform=7&distributorId=2529&sa=1'
                    });
                }
                let userId = uni.getStorageSync("userId");
                if (userId == null || userId == undefined || userId == "") {
                    // 未登录跳转登录
                    uni.redirectTo({
                        url: '/good_pages/login/login?platform=' + uni.getStorageSync("platform") + '&distributorId=' + this.distributorId + '&sa=1'
                    });
                } else {
                    this.getMaterialList();
                    this.getBanner();
                    this.getThemeLis();
                }
            } else {
                this.getMaterialList();
                this.getBanner();
                this.getThemeLis();
            }
            // #endif
            //获取店铺信息
            if (this.shopId != '') {
                this.ShopStatus(this.shopId);
            }
        }
        // this.getMiniAppUrl();
    },
    mounted() {

    },
    methods: {
        setImage() {
            var aspectRatio = uni.getStorageSync("aspectRatio");//宽高比例
            var height = 600;
            var width = Math.floor(600 / aspectRatio);
            uni.getImageInfo({
                src: uni.getStorageSync("floorImage"),
                success: res => {
                    var context = uni.createCanvasContext('firstCanvas')
                    context.clearRect(0, 0, width, height);
                    context.draw()
                    //先清空画布后再并绘制，否则使用同一个画布会导致重复绘制 
                    context.drawImage(res.path, 0, 0, width, height);
                    context.fill();
                    // context.draw();
                    context.draw(true, () => {
                        let _this = this
                        let canvasName = 'firstCanvas'
                        let w = width;
                        let h = height;
                        //提取图像RGBA通道颜色值，每连续四个为一个像素点，分别代表RGBA
                        let timer1 = setInterval(() => {
                            uni.canvasGetImageData({
                                canvasId: canvasName,
                                x: 0,
                                y: 0,
                                width: w,
                                height: h,
                                success(res) {
                                    let pxData = res.data;
                                    if (pxData.length == 0) {
                                        _this.hollowOut = '6';
                                    }
                                    let yz = 0;
                                    var index = 0;
                                    for (let i = 0; i < pxData.length; i += 4) {
                                        if (pxData[i] == yz &&
                                            pxData[i + 1] == yz &&
                                            pxData[i + 2] == yz) {
                                            pxData[i] = 255;
                                            pxData[i + 1] = 255;
                                            pxData[i + 2] = 255;
                                            pxData[i + 3] = 255;//理论上设置成1是不透明的；微信小程序的坑设置255不透明
                                        }
                                        else {
                                            pxData[i] = 0;
                                            pxData[i + 1] = 0;
                                            pxData[i + 2] = 0;
                                            pxData[i + 3] = 1;
                                        }
                                        index = i;
                                    }
                                    //先清空画布后再并绘制，否则使用同一个画布会导致重复绘制 
                                    context.clearRect(0, 0, 750, 1500)
                                    context.draw()

                                    //把处理好的像素点放回到画布中
                                    if (index + 4 == pxData.length) {
                                        uni.canvasPutImageData({
                                            canvasId: canvasName,
                                            x: 0,
                                            y: 0,
                                            width: w,
                                            height: h,
                                            data: pxData,
                                            success(res) {
                                                // 如需要提取请执行以下方法
                                                uni.canvasToTempFilePath({
                                                    x: 0,
                                                    y: 0,
                                                    width: w,
                                                    height: h,
                                                    destWidth: w,
                                                    destHeight: h,
                                                    canvasId: canvasName,
                                                    success: (res) => {
                                                        console.log("镂空图：", res);
                                                        _this.hollowOut = res.tempFilePath;
                                                        uni.setStorageSync("withFillingImg", res.tempFilePath)
                                                        uni.hideLoading()
                                                    },
                                                    fail(res) {
                                                        uni.hideLoading()
                                                        _this.hollowOut = '6';
                                                        console.log("失败了：", res);
                                                    }
                                                })
                                            },
                                            fail(res) {
                                                uni.hideLoading()
                                                _this.hollowOut = '6';
                                                console.log("失败了：", res);
                                            }
                                        })
                                    }
                                }, complete(e) {

                                }

                            })
                            clearTimeout(timer1)
                        }, 500);
                    })
                },
                fail: res => {
                    console.log(`缓存失败：${res.errMsg}`);
                }
            })
        },
        //h5 短链接跳转小程序
        getMiniAppUrl() {
            this.$api.post(this, api.getMiniUrl, {
                appId: 'wx05cb4496de7a20d7',
                distributorId: this.distributorId,
                pageUrl: 'good_pages/conversion/centre',
                param: 'distributorId=9526&platform=SNYG',
                permanent: true,//生成的小程序链接类型，短期有效：false(29天)，永久有效：true(urlType为3时有效)
                platform: this.platform,
                urlType: 3//url类型 1 微信小程序URL scheme 2 微信小程序URL Link 3 微信小程序Short Link 4 H5链接
            }
            ).then((res) => {
                if (res.success) {
                    this.IsWebClose = true;
                }
                console.log("链接返回参数：", res);
            })
        },
        close() {
            this.Ispath = false;
        },

        getUrlCode() {
            let url = location.search;
            /* eslint-disable */
            let theRequest = new Object();
            if (url.indexOf("?") !== -1) {
                let str = url.substr(1);
                let strs = str.split("&");
                for (let i = 0; i < strs.length; i++) {
                    theRequest[strs[i].split("=")[0]] = strs[i].split("=")[1];
                }
                return theRequest;
            }
        },
        //获取openid
        getOpenid() {
            var that = this;
            uni.login({
                provider: 'weixin',
                success: function (loginRes) {
                    that.$api.post(that, api.getOpenId, {
                        appId: uni.getAccountInfoSync().miniProgram.appId,
                        code: loginRes.code,
                        platformType: 2,//微信平台类型：1 公众号 2 小程序(不填默认为公众号)
                        distributorId: that.distributorId,
                    }).then((res) => {
                        uni.setStorageSync("openId", res.data)
                        that.openidlogin();
                    })
                },
                fail: function (err) {
                    console.log("获取openid失败：", err);
                }
            });
        },
        //openid登录
        openidlogin() {
            console.log("openid:", uni.getStorageSync("openId"));

            this.h5userId = uni.getStorageSync("h5userId");
            var that = this;
            var openIds = [{
                appId: uni.getAccountInfoSync().miniProgram.appId,
                openId: uni.getStorageSync("openId"),
                openType: 2//1 微信公众号 2 微信小程序 3 支付宝
            }]
            if (uni.getStorageSync("h5openId") != undefined && uni.getStorageSync("h5openId") != '') {
                var obj = {
                    appId: 'wx7f20d1112a4e3559',
                    openId: uni.getStorageSync("h5openId"),
                    openType: 1//1 微信公众号 2 微信小程序 3 支付宝
                }
                openIds.push(obj);
            }

            that.$api.post(that, api.openidlogin, {
                customerId: this.h5userId,//C端客户第三方登录 仅第一次公众号授权登录用
                openId: uni.getStorageSync("openId"),
                otherUid: "",//第三方系统其他标识码
                phone: '',
                openIds: openIds
            }).then((res) => {
                if (res.success) {
                    uni.setStorageSync("h5userId", '')
                    this.shopLogo = res.data.logo;
                    uni.setStorageSync("staffType", res.data.staffType);//店铺员类型 1.店长 2.导购员 3.消费者
                    uni.setStorageSync("userId", res.data.id);
                    uni.setStorageSync("userNo", res.data.no);
                    uni.setStorageSync("phone", res.data.phone);
                    uni.setStorageSync("customerShopCheck", res.data.customerShopCheck || 0);//店铺审核：0 否 1.是;无返回或为零都为否
                    uni.setStorageSync("rxShopSwitch", res.data.rxShopSwitch || 0);//是否启用店铺，1启用 0 不启用;无返回或为零都为否
                    uni.setStorageSync("aliAuthorize", res.data.aliAuthorize);//是否绑定支付宝
                    uni.setStorageSync("wxAuthorize", res.data.wxAuthorize);//是否绑定微信
                    if (res.data.payWay) {
                        uni.setStorageSync("payWay", res.data.payWay);
                    }
                    that.getMaterialList();
                    that.getBanner();
                    that.getThemeLis();
                    if (that.shopId != '') {
                        that.ShopStatus(that.shopId);
                    }
                    if (this.Iscertificate) {
                        uni.hideLoading()
                        uni.navigateTo({
                            url: "../../../good_pages/conversion/centre",
                        });
                    }
                }
                else {
                    uni.navigateTo({
                        url: "../../../good_pages/login/login",
                    });
                }
            })
        },
        // 获取店铺状态
        ShopStatus(id) {
            this.$api.get(this, api.getShopStatus, {
                id: id,
            }).then((res) => {
                if (res.success) {
                    uni.setStorageSync("shopId", this.shopId);
                    uni.setStorageSync("shopCode", res.data.shopCode);
                    uni.setStorageSync("shopName", res.data.shopName);
                    uni.setStorageSync("qrUrl", res.data.qrUrl);
                    uni.setStorageSync("thirdQrUrl", res.data.thirdQrUrl);
                } else {
                    uni.showToast({
                        title: res.errMessage,
                        icon: "none",
                        duration: 2000,
                    });
                }
            });
        },
        // 重新获取手机型号
        getMaterialList() {
            // 重新获取手机屏幕宽高、app.vue获取的屏幕宽高如果是扫码进来会先去公众号授权 没有屏幕地下tab栏 屏幕高度不适配
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
                if (res.data == undefined) {
                    this.hollowOut = "6"
                }
                uni.setStorageSync("defaultImage", res.data.outImage)
                uni.setStorageSync("leftFrame", res.data.leftFrame)
                uni.setStorageSync("topFrame", res.data.topFrame)
                var aspectRatio = res.data.length / res.data.width;
                var frameRatio = 300 / res.data.length;
                uni.setStorageSync("aspectRatio", aspectRatio);//宽高比例
                uni.setStorageSync("frameRatio", frameRatio);//边框比例
                uni.setStorageSync("floorImage", res.data.floorImage)
                if (this.hollowOut == '' || this.hollowOut == "6") {
                    // uni.showLoading({
                    //     title: '加载中',
                    //     mask: true
                    // });
                    this.setImage();
                } else {
                    this.hollowOut = "6"
                }
            })
        },
        gocustomization() {
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
        },
        //获取图片类型id
        gainImgId(id) {
            this.imgTypeId = id;
        },
        //获取选择的主题
        clickCount(index) {
            if (this.themeList.length == 0) {
                uni.hideLoading()
            }
            this.selCount = index;
        },
        //获取主题列表
        getThemeLis() {
            this.$api.get(this, api.getThemeList, {
                page: this.page,
                size: this.size,
                // openFlag: 1
            }).then((res) => {
                this.nextPage = res.data.pages;
                let list = res.data.list;
                this.themeList = this.page == 1 ? list : [...this.themeList, ...list];
                uni.hideLoading()
            })
        },
        //获取轮播图
        getBanner() {
            this.$api.get(this, api.getBanner, {
                distributorId: this.distributorId,
            }).then((res) => {
                this.BannerList = res.data;
            })
        },
        goPath(path, params, type) {
            if (type) {
                // type：1 首页IP系列， 2 活动页链接
                if (type === 2 && path.externalLink !== undefined
                    && path.externalLink !== ""
                    && path.externalLink.trim().length > 0) {
                    window.location.href = path.externalLink;
                } else {
                }
            } else {
                // 路由跳转
                this.$router.push({
                    path: path,
                    query: params,
                });
            }

        },
    }
}
</script>
<style scoped lang="scss">
.advance_dialog {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.9);
    z-index: 2000;
    display: flex;
    justify-content: center;
    align-items: center;

    .advance_reminder_box {
        position: absolute;
        width: 600rpx;
        height: 240rpx;
        background-color: #ffffff;
        border-radius: 24rpx;

        .advance_reminder_button {
            width: 600r;
            height: 100rpx;
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 16rpx;

            .cancel {
                width: 300rpx;
                height: 80rpx;
                text-align: center;
                line-height: 80rpx;
                background-color: rgba(0, 118, 164, 0.5);
                color: rgba(255, 255, 255, 1);
                font-size: 32rpx;
                border-radius: 0rpx 0rpx 0rpx 20rpx;
            }

            .confirm {
                width: 300rpx;
                height: 80rpx;
                background-color: rgba(0, 118, 164, 1);
                display: flex;
                justify-content: center;
                align-items: center;
                font-size: 32rpx;
                border-radius: 0rpx 0rpx 20rpx 0rpx;
            }
        }

        .advance_reminder_title {
            width: 100%;
            height: 100rpx;
            text-align: center;
            line-height: 100rpx;
            font-size: 40rpx;
            font-weight: 600;
            color: rgba(0, 0, 0, 1);
        }

        .advance_reminder_warn {
            width: 100%;
            font-size: 28rpx;
            font-weight: 500;
            text-align: center;
        }
    }
}

.shop_box {
    display: flex;
    align-items: center;
    color: rgba(16, 16, 16, 1);
    font-size: 32rpx;
    font-weight: 600;
    padding-left: 30rpx;
    image {
        width: 72rpx;
        height: 72rpx;
    }
}
.no_more {
    text-align: center;
    margin-top: 100rpx;
    font-size: 24rpx;
    color: rgba(59, 59, 59, 0.72);
    font-family: OPPOSans-bold;
    padding-bottom: 50rpx;
    height: 200rpx;
}

.custom_entrance_box {
    width: 750rpx;
    height: 240rpx;
    background-color: #f5f5f5 !important;
    position: absolute;
    margin-top: -80rpx;
    border-radius: 40rpx;
    display: flex;
    justify-content: center;
    align-items: center;

    image {
        width: 700rpx;
        height: 200rpx;
    }
}

.uni-margin-wrap {
    width: 100%;
    margin-bottom: 30rpx;

    /deep/ .uni-swiper-dots {
        // 指示点整个区域的位置
        top: 420rpx;
    }

    .swiper {
        height: 500rpx;

        .swiper_item {
            height: 600rpx;

            image {
                width: 750rpx;
                height: 500rpx;
            }
        }
    }
}
</style>
