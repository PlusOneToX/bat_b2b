<template>
    <view class="personalCenter">
        <view class="personalCenter-newTop">
            <image class="personalCenter_img" src="https://bat.oss-cn-shenzhen.aliyuncs.com/diy/h51684390031375.jpg"></image>
            <view class="personalCenter-newTop-info">
                <view style="display: flex;">
                    <image :src="avatarImg||'../../../static/images/index_login.png'" class="headerIcon"></image>
                    <view style="margin-left: 20rpx;">
                        <view style="height: 130rpx;line-height: 130rpx;" v-if="userId == ''">
                            <text @click="toClick('../../../good_pages/login/login')">立即登录</text>
                        </view>
                        <view v-else>
                            <view class="info_name"> {{ nikeName }}</view>
                            <view style="display: flex;align-items: center;">
                                <view class="code">ID:{{userNo}}</view>
                                <!-- v-if="shopName!=''" -->
                                <image @click="qrUrl" v-if="shopName!=''&&platform!=7" style="margin-left: 10rpx;margin-top: 10rpx; width: 40rpx;height: 40rpx;" src="../../../static/icons/shopname.png"></image>
                            </view>
                        </view>
                    </view>
                </view>
                <image style="width: 60rpx;height: 60rpx;" @click="toClick('../../../good_pages/set/set')" src="../../../static/images/set_up_the.png"></image>
            </view>
        </view>
        <!-- 我的订单 -->
        <view class="order_box">
            <view class="personalCenter-order">
                <view class="personalCenter-orderAll">
                    <view>我的订单</view>
                    <view @click="toOrderFun(0)">全部</view>
                </view>
                <view class="personalCenter-order-status">
                    <view @click="toOrderFun(1)" class="personalCenter-order-line">
                        <image mode="aspectFill" src="../../../static/icons/my/order/obligation.png" class="queren_icon"></image>
                        <view class="personal-tab-lineText">待付款</view>
                    </view>
                    <view @click="toOrderFun(2)" class="personalCenter-order-line">
                        <image mode="aspectFill" src="../../../static/icons/my/order/wait_for_receiving.png" class="queren_icon"></image>
                        <view class="personal-tab-lineText">待发货</view>
                    </view>
                    <view @click="toOrderFun(4)" class="personalCenter-order-line">
                        <image mode="aspectFill" src="../../../static/icons/my/order/off_the_stocks.png" class="queren_icon"></image>
                        <view class="personal-tab-lineText">待收货</view>
                    </view>
                    <view @click="toOrderFun(6)" class="personalCenter-order-line">
                        <image mode="aspectFill" src="../../../static/icons/my/order/in_production.png" class="queren_icon"></image>
                        <view class="personal-tab-lineText">已完成</view>
                    </view>
                </view>
            </view>
            <view class="personalCenter-order" v-if="staffType==1&&platform!=7">
                <view class="personalCenter-orderAll">
                    <view>店铺管理</view>
                    <view><text></text>
                    </view>
                </view>
                <!-- 开启分销 -->
                <view class="personalCenter-order-status" v-if="staffType==1">
                    <view class="personalCenter-order-line" @click="toClick('../../../good_pages/distribution/user/distribution_user')">
                        <image mode="aspectFill" src="../../../static/icons/my/order/distributor.png" class="queren_icon"></image>
                        <view class="personal-tab-lineText">分销员</view>
                    </view>
                    <view class="personalCenter-order-line" @click="toClick('../../../good_pages/store/store_order?shopOrder=1')">
                        <image mode="aspectFill" src="../../../static/icons/my/order/order_form.png" class="queren_icon"></image>
                        <view class="personal-tab-lineText">店铺订单</view>
                    </view>
                    <view class="personalCenter-order-line" @click="toClick('../../../good_pages/distribution/finance/management')">
                        <image mode="aspectFill" src="../../../static/icons/my/order/finance.png" class="queren_icon"></image>
                        <view class="personal-tab-lineText">财务管理</view>
                    </view>
                    <view class="personalCenter-order-line" @click="toClick('../../../good_pages/distribution/shop/shop')">
                        <image mode="aspectFill" src="../../../static/icons/my/order/set.png" class="queren_icon"></image>
                        <view class="personal-tab-lineText">店铺设置</view>
                    </view>
                </view>
                <view class="personalCenter-order-status" v-else>
                    <view class="personalCenter-order-line" @click="toClick('../../../good_pages/store/store_order?shopOrder=1')">
                        <image mode="aspectFill" src="../../../static/icons/my/order/order_form.png" class="queren_icon"></image>
                        <view class="personal-tab-lineText">店铺订单</view>
                    </view>
                    <view class="personalCenter-order-line">
                    </view>
                    <view class="personalCenter-order-line">
                    </view>
                    <view class="personalCenter-order-line">
                    </view>
                </view>
            </view>
            <!-- 我的分销 -->
            <!-- 分销员 -->
            <view class="personalCenter-order" v-if="staffType!=3&&platform!=7">
                <view class="personalCenter-orderAll">
                    <view>我的分销</view>
                    <view><text></text>
                    </view>
                </view>
                <view class="personalCenter-order-status">
                    <!-- <view class="personalCenter-order-line" @click="toClick('../../../good_pages/myDistribution/team/team')">
                        <image mode="aspectFill" src="../../../static/icons/my/order/order_form.png" class="queren_icon"></image>
                        <view class="personal-tab-lineText">我的团队</view>
                    </view> -->
                    <view class="personalCenter-order-line" @click="toClick('../../../good_pages/store/store_order?shopOrder=0')">
                        <image mode="aspectFill" src="../../../static/icons/my/order/order_form.png" class="queren_icon"></image>
                        <view class="personal-tab-lineText">分销订单</view>
                    </view>
                    <view class="personalCenter-order-line" @click="toClick('../../../good_pages/myDistribution/earnings/earnings')">
                        <image mode="aspectFill" src="../../../static/icons/my/order/earnings.png" class="queren_icon"></image>
                        <view class="personal-tab-lineText">我的收益</view>
                    </view>
                    <view class="personalCenter-order-line" @click="toClick('../../../good_pages/distribution/user/invitation?type=1')">
                        <image mode="aspectFill" src="../../../static/icons/my/order/material.png" class="queren_icon"></image>
                        <view class="personal-tab-lineText">推广素材</view>
                    </view>
                    <view class="personalCenter-order-line"></view>
                </view>
            </view>
            <view class="personalCenter-order">
                <view class="personalCenter-order-status">
                    <view class="personalCenter-order-line" @click="toClick('../../../good_pages/discount_coupon/discount')">
                        <image mode="aspectFill" src="../../../static/icons/my/discount_oupon.png" class="queren_icon"></image>
                        <view class="personal-tab-lineText">优惠券</view>
                    </view>
                    <view @click="handleWechat()" class="personalCenter-order-line">
                        <image mode="aspectFill" src="../../../static/icons/my/service.png" class="queren_icon"></image>
                        <view class="personal-tab-lineText">联系客服</view>
                    </view>
                    <view @click="toClick('../../../good_pages/address/address')" class="personalCenter-order-line">
                        <image mode="aspectFill" src="../../../static/icons/my/address.png" class="queren_icon"></image>
                        <view class="personal-tab-lineText">地址管理</view>
                    </view>
                    <view v-if="platform==7" class="personalCenter-order-line" @click="toClick('../../../good_pages/theme/collect')">
                        <image mode="aspectFill" src="../../../static/icons/my/order/material.png" class="queren_icon"></image>
                        <view class="personal-tab-lineText">我的作品</view>
                    </view>
                    <view v-else class="personalCenter-order-line">
                        <view class="personal-tab-lineText"></view>
                    </view>
                </view>
            </view>
            <uni-popup ref="server" type="bottom" :safeArea="false" backgroundColor="#fff">
                <view class="service-box">
                    <view class="service-item" @click="callPhone">
                        <uni-icons type="phone" size="30"></uni-icons>
                        <view class="text">客服热线</view>
                    </view>
                    <view class="service-item" @click="wechat">
                        <uni-icons type="weixin" size="30"></uni-icons>
                        <view class="text">微信客服</view>
                        <view class="wechat" v-show="false">{{ wechatStr }}</view>
                    </view>
                </view>
                <view class="btn-cancel" @click="close">取消</view>
            </uni-popup>
            <uni-popup ref="alertDialog" type="dialog">
                <uni-popup-dialog type="info" cancelText="取消" confirmText="确认" title="拨打热线" :content="phoneNumber" @confirm="dialogConfirm"></uni-popup-dialog>
            </uni-popup>
            <uni-popup ref="wxserver" type="dialog">
                <uni-popup-dialog type="info" cancelText="取消" confirmText="复制" title="客服微信号" :content="wechatStr" @confirm="dialogwx"></uni-popup-dialog>
            </uni-popup>
            <!-- 主题 -->
            <Recommended @clickCount="clickCount" @imgTypeId="gainImgId"></Recommended>
            <!-- 主题列表 -->
            <SeriesList v-if="(selCount==-1)" v-for="(item,index) in themeList" :key="index" :themeId="item.id" :item="item"></SeriesList>
            <view v-if="(selCount==-1&&themeList.length!=0)" class="no_more">没有更多了哦 ~</view>
            <view v-if="(selCount==-1&&themeList.length==0)" class="no_more">暂无推荐主题 ~</view>
            <!-- 作品列表 -->
            <PictureList v-if="(selCount!=-1)" :imgTypeId="imgTypeId"></PictureList>
            <uni-popup ref="popup2" type="center" :safeArea="false" @maskClick="closeqr">
                <view class="center_box">
                    <view class="center_title">店铺信息</view>
                    <view class="center_txt">
                        {{shopName}}
                    </view>
                    <view>
                        <image :src="qrUrlimg"></image>
                    </view>
                </view>
                <view style="text-align: center;margin-top: 60rpx;">
                    <uni-icons type="clear" color="#FFFFFF" size="60" @click="closeqr"></uni-icons>
                </view>
            </uni-popup>
        </view>
    </view>
</template>

<script>
import Recommended from "components/recommended.vue";               //主题
import SeriesList from "components/seriesList.vue";                 //主题列表
import PictureList from "components/pictureList.vue";               //作品列表
import api from "common/js/allApi.js";
export default {
    components: {
        Recommended,
        SeriesList,
        PictureList
    },
    data() {
        return {
            phoneNumber: "15994794151",
            wechatStr: "YYff-33",
            userId: '',
            distributorId: '',
            themeList: [],//主题列表
            selCount: -1,//选择的主题
            imgTypeId: '',//图片类型id
            avatarImg: '',
            userNo: '',
            companyName: '',
            nikeName: '',
            rxShopSwitch: 0,//是否启用店铺，1启用 0 不启用;无返回或为零都为否
            page: 1,
            size: 10,
            platform: 1,
            nextPage: 0,
            shopName: '',
            qrUrlimg: '',
            staffType: 3
        };
    },
    onShow() {
        uni.setStorageSync("setText", false);
        this.distributorId = uni.getStorageSync("distributorId");
        this.shopName = uni.getStorageSync("shopName");
        this.platform = uni.getStorageSync("platform");
        this.rxShopSwitch = uni.getStorageSync("rxShopSwitch");
        this.userNo = uni.getStorageSync("userNo");
        this.userId = uni.getStorageSync("userId");

        if (this.platform == 7) {
            this.qrUrlimg = uni.getStorageSync("thirdQrUrl");
            this.userId = uni.getStorageSync("userId");
        }
        //判断是否存在userId;
        //获取分销员审核状态
        let user_Id = uni.getStorageSync("userId");
        if (this.platform != 7) {
            this.qrUrlimg = uni.getStorageSync("qrUrl");
            if (user_Id != null && user_Id != undefined && user_Id != "") {
                this.userIdLogin(user_Id);
            }
        }
        uni.showLoading({
            title: '加载中',
            mask: true
        });
        this.getThemeLis();
        if (user_Id !== "" && user_Id !== null && user_Id !== undefined) {
            this.$api.get(this, api.getUserInfo, {
                id: user_Id,
            }).then((res) => {
                if (res.success) {
                    this.userNo = res.data.no;
                    this.phone = res.data.phone;
                    if (res.data.phone !== null && res.data.phone !== "" && res.data.phone !== undefined) {
                        this.phone = res.data.phone;
                    }
                    if (res.data.nikeName !== null && res.data.nikeName !== "" && res.data.nikeName !== undefined) {
                        this.nikeName = res.data.nikeName;
                    }
                    if (res.data.headPortrait !== null && res.data.headPortrait !== "" && res.data.headPortrait !== undefined
                    ) {
                        this.avatarImg = res.data.headPortrait;
                    }
                } else {
                }
            });

        }
    },
    onLoad() {

    },
    onReachBottom() {
        console.log("触底了");
        if (this.page < this.nextPage) {
            this.page += 1;
            uni.showLoading({
                title: '加载中',
                mask: true
            });
            this.getThemeLis();
        }
    },
    methods: {
        userIdLogin(user_Id) {
            this.$api.post(this, api.userIdLogin, {
                id: user_Id
            }).then((res) => {
                if (res.success) {
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
                    this.userNo = res.data.no;
                    this.userId = res.data.id;
                    this.rxShopSwitch = res.data.rxShopSwitch || 0;
                    this.staffType = res.data.staffType;
                }
            })
        },
        closeqr() {
            this.$refs.popup2.close();
        },
        qrUrl() {
            this.$refs.popup2.open('center')
        },
        dialogwx() {
            uni.setClipboardData({
                data: this.wechatStr,
                success: () => {
                    uni.showToast({
                        title: `复制成功`,
                        icon: 'none'
                    })
                }
            }, true);
        },
        dialogConfirm() {
            uni.makePhoneCall({
                phoneNumber: this.phoneNumber
            });
        },
        close() {
            this.$refs.server.close()
        },
        // 客服热线
        callPhone() {
            this.$refs.alertDialog.open()
            this.$refs.server.close()
        },
        // 微信客服
        wechat() {
            this.$refs.wxserver.open()
            this.$refs.server.close()
        },
        handleWechat() {
            // #ifdef MP-WEIXIN
            wx.openCustomerServiceChat({
                extInfo: { url: 'https://work.weixin.qq.com/kfid/kfcde61518122a13f52' },
                corpId: 'wwc20c39b5f2093e4a',
                success(res) {
                    console.log("返回值：", res);
                },
                fail(res) {
                    console.log("失败了：", res);
                }
            })
            // #endif

            // #ifdef H5
            this.wechat();
            // #endif
        },
        toOrderFun(type) {
            uni.navigateTo({
                url: "../../../good_pages/order/orderList?active=" + type,
            });
        },
        // 跳转
        toClick(name) {
            uni.navigateTo({
                url: name,
            });
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
            }).then((res) => {
                this.nextPage = res.data.pages;
                let list = res.data.list;
                this.themeList = this.page == 1 ? list : [...this.themeList, ...list];
                uni.hideLoading()
            })
        },
    },
};
</script>

<style lang="scss" scoped>
.center_box {
    width: 640rpx;
    height: 740rpx;
    border-radius: 30rpx;
    background-color: rgba(255, 255, 255, 1);
    color: rgba(16, 16, 16, 1);
    font-size: 28rpx;
    text-align: center;
    font-family: Arial;
    border: 1rpx solid rgba(239, 239, 239, 1);
    image {
        width: 400rpx;
    }
    .center_title {
        color: rgba(59, 59, 59, 1);
        font-size: 32rpx;
        text-align: center;
        font-weight: 600;
        height: 120rpx;
        line-height: 120rpx;
    }
    .center_txt {
        display: flex;
        align-items: center;
        margin: 0 auto;
        width: 500rpx;
        color: #595959;
        font-size: 28rpx;
        height: 120rpx;
    }
}
.queren_icon {
    width: 50rpx;
    height: 50rpx;
}
.service-box {
    text-align: left;
    padding: 30rpx 0rpx;
    border-bottom: 1rpx solid #e5e5e5;
    .service-item {
        display: inline-block;
        width: 30%;
        text-align: center;

        .text {
            display: block;
            margin-top: 12rpx;
            font-size: 28rpx;
            color: #4a4a4a;
        }
    }
}
.btn-cancel {
    display: block;
    font-size: 36rpx;
    text-align: center;
    color: #4a4a4a;
    height: 80rpx;
    line-height: 80rpx;
}
.no_more {
    text-align: center;
    margin-top: 50rpx;
    height: 300rpx;
    font-size: 24rpx;
    color: rgba(59, 59, 59, 0.72);
    font-family: OPPOSans-bold;
}

.operation {
    width: 400rpx;
    display: flex;
    justify-content: space-between;
    margin: 0 auto;
    margin-top: 100rpx;
    .img {
        width: 100rpx;
        height: 100rpx;
        border-radius: 50rpx;
        background-color: rgba(255, 255, 255, 1);
        display: flex;
        justify-content: center;
        align-items: center;
    }
}
.myQrCode_module {
    width: 520rpx;
    height: 520rpx;
    background: #fff;
    border-radius: 30rpx;
    display: flex;
    justify-content: center;
    align-items: center;
    .qrcodeImg {
        width: 500rpx;
        height: 500rpx;
    }
}
.personalCenter {
    overflow-x: hidden;
    font-size: 26rpx;
    font-family: PingFangSC-Regular, PingFang SC;
    .personalCenter-newTop {
        position: relative;
        width: 750rpx;
        height: 500rpx;
        z-index: 1;
        .personalCenter_img {
            width: 750rpx;
            height: 500rpx;
            border-radius: 0rpx 0rpx 100rpx 100rpx;
        }
        .personalCenter-newTop-info {
            position: absolute;
            z-index: 999;
            font-size: 34rpx;
            font-family: PingFangSC-Medium, PingFang SC;
            font-weight: 500;
            color: #ffffff;
            top: 180rpx;
            left: 50%;
            transform: translateX(-50%);
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 690rpx;
            image {
                width: 130rpx;
                height: 130rpx;
                border-radius: 50%;
            }
            .the_company {
                font-size: 26rpx;
                margin-top: 10rpx;
            }
            .info_name {
                font-size: 22rpx;
                margin-top: 30rpx;
                display: flex;
                align-items: center;
                image {
                    width: 30rpx;
                    height: 30rpx;
                    margin-left: 10rpx;
                }
            }
            .code {
                padding: 0 10rpx;
                background-color: #ffffff;
                color: #1a7ac9;
                font-size: 20rpx;
                text-align: center;
                border-radius: 20rpx;
                margin-top: 10rpx;
            }
        }
    }
    .order_box {
        width: 750rpx;
        // background-color: #fafafc;
        border-radius: 30rpx 30rpx 0 0;
        position: relative;
        top: -88rpx;
        z-index: 99;
        .money_meu_box {
            width: 600rpx;
            margin: 0 auto;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 30rpx 0;
            text-align: center;
            font-size: 24rpx;
            line-height: 40rpx;
            span {
                font-weight: 600;
                font-size: 26rpx;
            }
        }
        // 订单
        .personalCenter-order {
            background: #ffffff;
            border: 1rpx solid rgba(239, 239, 239, 1);
            width: calc(100% - 60rpx);
            border-radius: 40rpx;
            margin: 0 auto;
            margin-bottom: 30rpx;
            .personalCenter-orderAll {
                padding: 28rpx 28rpx 0 28rpx;
                display: flex;
                align-items: center;
                justify-content: space-between;
                font-size: 26rpx;
                view:nth-child(2) {
                    image {
                        width: 24rpx;
                        height: 24rpx;
                    }
                }
            }
            .personalCenter-order-status {
                display: flex;
                align-items: center;
                padding: 24rpx 14rpx;
                .personalCenter-order-line {
                    flex: 1;
                    text-align: center;
                    font-size: 24rpx;
                }
            }
        }
    }
    // tab
    .personal-List {
        position: relative;
        top: -68rpx;
        width: calc(100% - 60rpx);
        margin-left: 30rpx;
        border-radius: 8rpx;
        background: #fff;
        font-size: 28rpx;
        .personal-List-line {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 30rpx;
            .personal-List-lineLf {
                display: flex;
                align-items: center;
                font-size: 28rpx;
                color: #555;
                image {
                    width: 48rpx;
                    height: 48rpx;
                    margin-right: 15rpx;
                }
            }
            .personal-List-lineRg {
                display: flex;
                align-items: center;
                text {
                    margin-right: 15rpx;
                }
                image {
                    width: 24rpx;
                    height: 24rpx;
                }
            }
        }
    }
}
</style>
