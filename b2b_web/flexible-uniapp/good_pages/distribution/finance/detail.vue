<template>
    <view>
        <uni-nav-bar left-icon="left" :border="false" @clickLeft="back" dark :fixed="false" background-color="#ffffff" color="#000000" status-bar title="余额明细" />
        <view class="head_box">
            <view style="width: 170rpx;">
                <uni-data-select :clear="false" v-model="businessType" :localdata="selectDate" @change="change"></uni-data-select>
            </view>
            <view style="width: 530rpx;">
                <uni-datetime-picker v-model="range" type="daterange" @change="maskClick" />
            </view>
            <!-- <view>筛选</view> -->
        </view>
        <view scroll-y="true" class="balance_detail_box">
            <view class="balance_detail_head">
                <view>项目/单据</view>
                <view>收支/日期</view>
            </view>
            <scroll-view scroll-y="true" class="balance_detail_y" @scrolltolower="ReachBottom()">
                <view class="balance_detail_list_box" v-for="item in detailsList" :key="item.id">
                    <view class="title_box">
                        <view>{{getType(item.businessType)}}</view>
                        <view class="money" v-if="item.businessType==3||item.businessType==2">¥ -{{item.amount}}</view>
                        <view class="add_money" v-else>¥ +{{item.amount}}</view>

                    </view>
                    <view class="serial_number_box">
                        <view>单据编号：-</view>
                        <view>{{item.createTime}}</view>
                    </view>
                </view>
                <view v-if="detailsList.length==0" style="text-align: center;line-height: 400rpx;">暂无消费记录~</view>
            </scroll-view>
        </view>

    </view>
</template>
<script>
import api from "../../../common/js/allApi";
export default {
    data() {
        return {
            range: ['', ''],
            detailsList: [],
            page: 1,
            size: 10,
            totalCount: 0,
            businessType: '',
            selectDate: [
                { value: '', text: "全部" },
                { value: 1, text: "充值" },
                { value: 3, text: "订单消费" }
            ],
        };
    },
    onShow() {

    },
    onLoad() {
        this.getDopsitDataDetailList();
    },

    methods: {
        change(e) {
            this.businessType = e;
            this.page = 1;
            this.size = 10;
            this.totalCount = 0;
            this.getDopsitDataDetailList();
        },
        ReachBottom() {
            if (this.detailsList.length < this.totalCount) {
                this.page += 1;
                this.getDopsitDataDetailList();
            }
        },
        getType(val) {
            switch (val) {
                case 1:
                    return "余额充值";
                case 2:
                    return "提现";
                case 3:
                    return "订单消费";
                case 4:
                    return "订单取消增加";
                case 5:
                    return "调整";
                case 6:
                    return "ERP增量变化";
                case 7:
                    return "ERP全量变化";
                case 8:
                    return "分销佣金";
            }
        },
        // 获取明细列表
        getDopsitDataDetailList() {
            this.$api.get(this, api.depositDetailList, {
                distributorId: uni.getStorageSync("distributorId"),
                page: this.page,
                size: this.size,
                startTime: this.range[0],//开始时间
                endTime: this.range[1],//结束时间
                businessType: this.businessType,
            }).then((res) => {
                if (res.success) {
                    this.totalCount = res.data.total;
                    if (this.page !== 1) {
                        this.detailsList = [...that.detailsList, ...res.data.list];
                    } else {
                        this.detailsList = res.data.list;
                    }
                }
            });


        },
        maskClick(e) {
            if (e.length != 0) {
                this.range = e;
                this.range[0] = this.range[0] + ' 00:00:00'
                this.range[1] = this.range[1] + ' 00:00:00'
            }
            else {
                this.range = ['', '']
            }
            this.page = 1;
            this.size = 10;
            this.totalCount = 0;
            this.getDopsitDataDetailList();
        },
        back() {
            uni.navigateBack({
                delta: 1
            });
        },
    },
};
</script>

<style lang="scss" scoped>
.balance_detail_box {
    height: 83vh;
    width: 750rpx;
    border-radius: 30rpx 30rpx 0 0;
    background-color: #ffffff;
    margin: 0 auto;
    .balance_detail_y {
        height: 78vh;
    }
    .balance_detail_head {
        height: 80rpx;
        width: 690rpx;
        margin: 0 auto;
        color: rgba(16, 16, 16, 1);
        font-size: 24rpx;
        font-weight: 600;
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-bottom: 1rpx solid rgba(245, 245, 245, 1);
    }
    .balance_detail_list_box {
        width: 690rpx;
        height: 120rpx;
        margin: 0 auto;
        border-bottom: 1rpx solid rgba(245, 245, 245, 1);
        .title_box {
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 690rpx;
            height: 40rpx;
            color: rgba(59, 59, 59, 1);
            font-size: 24rpx;
            margin-top: 30rpx;
            padding: 0 20rpx;
            box-sizing: border-box;
            .money {
                color: rgba(0, 118, 164, 1);
                font-size: 24rpx;
            }
            .add_money {
                color: #fc7e1b;
                font-size: 24rpx;
            }
        }
        .serial_number_box {
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 690rpx;
            height: 40rpx;
            color: rgba(136, 136, 136, 1);
            font-size: 20rpx;
            padding: 0 20rpx;
            box-sizing: border-box;
        }
    }
}
.head_box {
    height: 6vh;
    width: 710rpx;
    margin: 0 auto;
    display: flex;
    justify-content: space-between;
    align-items: center;
}
</style>