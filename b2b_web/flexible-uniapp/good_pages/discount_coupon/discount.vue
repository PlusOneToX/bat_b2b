<template>
    <view style="background-color: #ffffff;min-height: 100vh;">
        <!-- #ifdef MP-WEIXIN -->
        <uni-nav-bar dark :fixed="true" background-color="#ffffff" color="#000" shadow status-bar left-icon="left" title="优惠券" @clickLeft="toback" />
        <scroll-view scroll-x="true" class="order-topNav">
            <!-- #endif -->
            <!-- #ifdef H5 -->
            <uni-nav-bar left-icon="left" title="优惠券" :fixed="true" shadow @clickLeft="toback" />
            <scroll-view scroll-x="true" class="order-topNav1">
                <!-- #endif -->
                <view class="top-nav">
                    <text v-for="item in couponTabs" :key="item.id" :class="status == item.id ? 'topNav-hover' : ''" @click="handleTab(item.id,item.title)">{{ item.title }}
                    </text>
                </view>
            </scroll-view>
            <view style="height: 140rpx;"></view>
            <view class="vouchers_list_box">
                <view class="vouchers_box_meu" :class="status=='2,3'||status==1?'vouchers_meu_box_default':'vouchers_meu_box_no'" v-for="(item,index) in couponList" :key="index">
                    <view class="vouchers_meu_let" :class="status=='2,3'||status==1?'default':'do_not_use'">
                        <view style="position:absolute;left: 236rpx;margin-top: -218rpx; width: 32rpx;height: 32rpx;background-color: #ffffff;border-radius: 16rpx;"></view>
                        <view style="position: absolute;left: 236rpx;margin-top: 218rpx;width: 32rpx;height: 32rpx;background-color: #ffffff;border-radius: 16rpx;"></view>
                        <view v-if="(item.couponMethod==1)">
                            <view>
                                ￥<span class="the_amount_of">{{item.reduction}}</span>
                            </view>
                            <view class="the_title" :class="status=='2,3'||status==1?'the_title_default':'the_title_do_not_use'">
                                满减券
                            </view>
                        </view>
                        <view v-if="(item.couponMethod==2)">
                            <view>
                                <span class="the_amount_of">{{item.discount / 10 }}折</span>
                            </view>
                            <view class="the_title" :class="status=='2,3'||status==1?'the_title_default':'the_title_do_not_use'">
                                满折
                            </view>
                        </view>
                        <view v-if="(item.couponMethod==3)">
                            <view class="the_title" :class="status=='2,3'||status==1?'the_title_default':'the_title_do_not_use'">
                                商品兑换
                            </view>
                        </view>
                    </view>
                    <view class="vouchers_meu_right">
                        <view class="vouchers_meu_name">{{item.couponName}}</view>
                        <view class="use_box" :class="(status=='2,3'||status==1)?'use_box_default':'use_box_do_not_use'">
                            <view class="ticket_no" v-if="item.orderMoney > 0&&item.couponType!=4">
                                满{{ item.orderMoney }}元可用
                            </view>
                            <view class="ticket_no" v-else-if="item.couponType==4">
                                新人可用
                            </view>
                            <view class="ticket_no" v-else-if="item.couponMethod==3">
                                商品兑换券
                            </view>
                            <view class="immediate_use" v-if="(status==1)" @click.stop="getCouponBtn(item)">待领取</view>
                            <view class="immediate_use" v-else-if="(status=='2,3')" @click="gohome">立即使用</view>
                            <view class="has_been_used" v-else-if="(status==4)">已使用</view>
                            <view class="has_been_used" v-else>已过期</view>
                        </view>
                        <view class="the_period_of_validity">
                            <view>{{dateToNum(item.startTime)}}至{{dateToNum(item.endTime)}}</view>
                            <view style="display: flex;align-items: center;" @click="examine(item)">
                                规则
                                <uni-icons style="margin-top: 4rpx;" type="forward" size="20" color="#a4a4a4"></uni-icons>
                            </view>
                        </view>
                    </view>
                </view>
                <view v-if="couponList.length==0" style="text-align: center;padding-top: 200rpx;font-size: 26rpx;color: #8A8A8A;">
                    暂无记录~
                </view>
            </view>
            <uni-popup ref="popup" type="center" :safeArea="false" :is-mask-click="false">
                <view class="rule_box1">
                    <view class="rule_title_box">使用规则</view>
                    <scroll-view class="rule_box" :show-scrollbar="false" :scroll-y="true">
                        <view class="describe_box" style="white-space: pre-line">{{details.couponExplain}}</view>
                        <view class="describe_box" v-if="details.models!=undefined||details.materials!=undefined">
                            注意:该兑换券只适用于部分机型、材质:
                            <view style="display: flex; flex-wrap: wrap;">
                                <view v-for="(materials,inx) in details.materials" :key="materials.materialId">
                                    {{inx>0?'、':''}}
                                    {{materials.materialName}}
                                </view>
                                <view v-if="details.models!=undefined&&details.materials!=undefined">；</view>
                                <view v-for="(phone,index) in details.models" :key="phone.modelId">
                                    {{index!=0?'、':''}}{{phone.modelName}}</view>
                                <view v-if="details.models!=undefined||details.materials!=undefined">；</view>
                            </view>
                        </view>

                    </scroll-view>
                    <view class="know_box" @click="close">我知道了</view>
                </view>
            </uni-popup>
    </view>
</template>
<script type="text/ecmascript-6">
import api from "common/js/allApi.js";
export default {
    name: "couponList",
    data() {
        return {
            title: "优惠券",
            platform: "",
            distributorId: "",
            // 优惠券tab
            couponTabs: [
                {
                    id: 1,
                    title: "待领取",
                    value: "立即领取",
                },
                {
                    id: '2,3',
                    title: "未使用",
                    value: "未使用",
                },
                {
                    id: 4,
                    title: "已使用",
                    value: "已使用",
                },
                {
                    id: 5,
                    title: "已过期",
                    value: "已过期",
                },
            ],
            userId: "",
            page: 1, // 页码
            size: 10, // 页数
            status: 1, // 0 全部，1 待领取，2 已领取，3 未使用，4 已使用，5 已过期
            totalPage: 0, // 总数
            couponList: [], // 优惠列表
            isLoading: false,
            message: "",
            details: {},
            discount: 0,
        };
    },
    mounted() {
        // 判断是否是第三方进入
        this.platform = uni.getStorageSync("platform");
        this.distributorId = uni.getStorageSync("distributorId");
        uni.setStorageSync("platform", this.platform);
        uni.setStorageSync("distributorId", this.distributorId);
        uni.setStorageSync("orderSource", this.platform);
        // 初始化优惠券
        this.userId = uni.getStorageSync("userId");
        if (this.userId) {
            // 已登录
            this.initData();
        } else {
            // 未登录
            uni.reLaunch({
                url: '/good_pages/login/login?platform=7&distributorId=2529&sa=5'
            });
        }
    },
    onLoad(options) {
        if (options.discount != undefined) {
            this.discount = options.discount;
            this.status = '2,3';
        }
    },
    onReachBottom() {
        if (this.couponList.length < this.totalPage) {
            this.getMore(this.status);
        }
    },
    methods: {

        toback() {
            if (this.discount == 1) {
                uni.switchTab({
                    url: '/src/components/index/index?platform=7&distributorId=2529&discount=1'
                });
            } else {
                uni.switchTab({
                    url: '/src/tabBar/my/my'
                });
            }
        },
        examine(e) {
            this.details = e;
            this.open();
        },
        open() {
            this.$refs.popup.open('center')
        },
        close() {
            this.$refs.popup.close('center')
        },
        gohome() {
            uni.switchTab({
                url: '../../src/components/index/index'
            })
        },
        // 领取优惠券
        getCouponBtn(item) {
            if (this.status === 1) {
                this.$api.post(this, api.receiveCoupon, { ids: [item.couponId] }).then((res) => {
                    if (res.success) {
                        uni.showToast({
                            icon: 'none',
                            title: '领取成功',
                            duration: 2000
                        });
                        this.initData();
                    } else {
                        uni.showToast({
                            icon: 'none',
                            title: res.errMessage,
                            duration: 2000
                        });
                    }
                })
                    .catch((error) => {
                        console.log(error);
                    });
            }
        },
        dateToNum(val) {
            return val.substring(0, 10)
        },
        initData() {
            this.couponList = [];
            // 获取优惠券列表
            this.getCouponList(this.size, this.status);
        },
        getMore(status) {
            this.page++;
            let size = this.size * this.page;
            this.getCouponList(size, status);
        },
        // 获取优惠券列表
        getCouponList(size, status) {
            this.message = "载入中";
            this.isLoading = true;

            // 优惠券状态：0 全部，1 待领取，2 已领取，3 未使用，4 已使用，5 已过期
            this.$api
                .get(this, api.getCouponList, {
                    page: 1,
                    size: size,
                    statuss: status,
                })
                .then((res) => {
                    if (res.success) {
                        this.totalPage = res.data.total;
                        this.couponList = res.data.list;
                        this.couponList.forEach((item) => {
                            // item.isShow = false
                            if (item.couponExplain !== "") {
                                item.couponExplainArr = item.couponExplain
                                    .trim()
                                    .split(/[\r|\n]/);
                            }
                        });
                    } else {
                        uni.showToast({
                            icon: 'none',
                            title: res.errMessage,
                            duration: 2000
                        });
                    }
                    this.message = "";
                    this.isLoading = false;
                });
        },
        // 切换tab
        handleTab(val, title) {
            this.couponList = [];
            this.status = val;
            this.getCouponList(this.size, this.status);
        },
        // 重新获取
        refresh(status) {
            this.getCouponList(this.size, status);
        },
        // 下拉展开收缩切换
        toggle(id) {
            this.couponList.map((item) => {
                if (item.id === id) {
                    item.show = !item.show;
                }
            });
        },
    },
};
</script>
<style lang="scss" scoped>
.rule_box1 {
    width: 500rpx;
    min-height: 200rpx;
    max-height: 800rpx;
    border-radius: 20rpx;
    background-color: #ffffff;
    padding-bottom: 30rpx;
    .rule_title_box {
        width: 500rpx;
        height: 80rpx;
        color: #545353;
        font-size: 30rpx;
        font-weight: 600;
        text-align: center;
        line-height: 80rpx;
    }
}
.rule_box {
    width: 500rpx;
    min-height: 200rpx;
    max-height: 500rpx;
    border-radius: 20rpx;
    background-color: #ffffff;
    padding-bottom: 30rpx;

    .describe_box {
        width: 500rpx;
        // min-height: 80rpx;
        padding: 0 40rpx;
        line-height: 40rpx;
        box-sizing: border-box;
        height: auto;
        font-size: 26rpx;
        color: #999999;
        padding-bottom: 20rpx;
    }
}
.know_box {
    width: 340rpx;
    text-align: center;
    height: 60rpx;
    line-height: 60rpx;
    background-color: #ff933f;
    color: #ffffff;
    border-radius: 50rpx;
    margin: 0 auto;
}
scroll-view ::-webkit-scrollbar {
    display: none;
    width: 0;
    height: 0;
    color: transparent;
}
.order-topNav {
    padding: 10rpx 0;
    background: #fff;
    border-top: 1px solid #f2f3f8;
    box-sizing: border-box;
    position: fixed;
    z-index: 90;
    top: 100;
}
.order-topNav1 {
    padding: 10rpx 0;
    background: #fff;
    border-top: 1px solid #f2f3f8;
    box-sizing: border-box;
    position: fixed;
    z-index: 90;
    top: 100;
}

.top-nav {
    display: flex;
    justify-content: space-between;
    padding: 0 40rpx;
    text {
        display: block;
        padding: 0 30rpx;
        color: #999999;
        font-size: 24rpx;
        font-weight: 300;
        white-space: nowrap;
        height: 80rpx;
        line-height: 80rpx;
    }
    .topNav-hover {
        position: relative;
        color: #0076a5;
        font-size: 28rpx;
        font-weight: 600;
        &::after {
            content: "";
            position: absolute;
            bottom: 0;
            left: 50%;
            width: 100rpx;
            height: 6rpx;
            background: #0076a5;
            border-radius: 6rpx;
            transform: translateX(-50%);
        }
    }
}
.vouchers_list_box {
    width: 690rpx;
    height: auto;
    margin: 0 auto;
    .vouchers_meu_box_default {
        background-color: #ffeaea;
    }
    .vouchers_meu_box_no {
        background-color: #eeeeee;
    }
    .vouchers_box_meu {
        width: 690rpx;
        height: 220rpx;
        border-radius: 20rpx;
        display: flex;
        margin-bottom: 20rpx;
        .default {
            background: linear-gradient(to top left, #ff933f, #ff4a65);
            border-right: 2rpx dotted #fd2a56;
        }
        .do_not_use {
            background: #a4a4a4;
        }
        .vouchers_meu_let {
            width: 220rpx;
            height: 220rpx;
            border-radius: 20rpx 0rpx 0rpx 20rpx;
            display: flex;
            align-items: center;
            justify-content: center;
            text-align: center;
            view {
                color: #ffffff;
            }
            .the_amount_of {
                font-size: 50rpx;
                font-weight: 600;
            }
            .the_title_default {
                background-color: #ffc5be;
            }
            .the_title_do_not_use {
                background-color: #545353;
            }
            .the_title {
                width: 120rpx;
                height: 40rpx;
                font-size: 26rpx;
                font-weight: 600;
                border-radius: 30rpx;
                text-align: center;
                line-height: 40rpx;
                margin: 0 auto;
            }
        }
        .vouchers_meu_right {
            width: 470rpx;
            height: 220rpx;
            padding: 10px 10px 10px 10px;
            box-sizing: border-box;
            .vouchers_meu_name {
                width: 420rpx;
                height: 40rpx;
                color: rgba(16, 16, 16, 1);
                font-size: 28rpx;
                text-align: left;
                font-family: SourceHanSansSC-bold;
                font-weight: 600;
                overflow: hidden;
                text-overflow: ellipsis;
                display: -webkit-box;
                -webkit-line-clamp: 1;
                -webkit-box-orient: vertical;
            }
            .use_box_default {
                border-bottom: 1rpx dotted #fd2a56;
            }
            .use_box_do_not_use {
                border-bottom: 1rpx dotted #e1e1e1;
            }
            .use_box {
                width: 420rpx;
                height: 90rpx;
                display: flex;
                align-items: center;
                justify-content: space-between;
                .ticket_no {
                    color: rgba(84, 83, 83, 1);
                    font-size: 22rpx;
                    text-align: left;
                    font-family: SourceHanSansSC-bold;
                    width: 280rpx;
                }
                .has_been_used {
                    cursor: pointer;
                    width: 120rpx;
                    height: 40rpx;
                    text-align: center;
                    line-height: 40rpx;
                    font-size: 24rpx;
                    background-color: #545353;
                    border-radius: 30rpx;
                    color: #ffffff;
                }
                .immediate_use {
                    cursor: pointer;
                    width: 120rpx;
                    height: 40rpx;
                    text-align: center;
                    line-height: 40rpx;
                    font-size: 24rpx;
                    background-image: linear-gradient(
                        to right,
                        #ff933f,
                        #ff4a65
                    );
                    border-radius: 30rpx;
                    color: #ffffff;
                }
            }
            .the_period_of_validity {
                height: 60rpx;
                width: 450rpx;
                line-height: 60rpx;
                color: rgba(164, 164, 164, 1);
                font-size: 20rpx;
                font-family: SourceHanSansSC-regular;
                display: flex;
                justify-content: space-between;
                padding-right: 20rpx;
                box-sizing: border-box;
            }
        }
    }
}
</style>