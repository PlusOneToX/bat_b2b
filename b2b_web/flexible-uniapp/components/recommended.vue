<template>
    <view class="recommended_box">
        <view class="recommended_title_box">靓图推荐</view>
        <scroll-view class="scroll-view_H" :show-scrollbar="false" :scroll-x="true">
            <view class="scroll_view" @click="chooseCount(-1)" :class="selCount==-1?'selected':'not_selected'">推荐</view>
            <view class="scroll_view" v-for="(item,index) in imgType" :key="index" @click="chooseCount(index)" :class="selCount==index?'selected':'not_selected'">{{item.name}}</view>
        </scroll-view>
    </view>
</template>
<script>
import api from "../common/js/allApi.js";
export default {
    data() {
        return {
            selCount: -1,
            imgType: [],
            distributorId: ''
        }
    },
    mounted() {
        this.distributorId = uni.getStorageSync("distributorId");
        this.getimgTypeList();
    },
    methods: {
        getimgTypeList() {
            this.$api.get(this, api.getImageTypeList, {
                parentId: 0,
                type: 1
            }).then((res) => {
                this.imgType = res.data;
            })
        },
        chooseCount(index) {
            this.selCount = index;
            this.$emit("clickCount", this.selCount);
            if (index != -1) {
                this.$emit("imgTypeId", this.imgType[index].id);
            }
        }
    }
}
</script>
<style scoped lang="stylus">
/* 去除scroll滚动条 */
/deep/ ::-webkit-scrollbar {
    display: none;
    width: 0;
    height: 0;
    background-color: transparent;
}

.recommended_box {
    width: 700rpx;
    margin: 0 auto;

    .recommended_title_box {
        color: rgba(59, 59, 59, 1);
        font-size: 32rpx;
        text-align: left;
        color: #3B3B3B;
        font-weight: 600;
        font-family: OPPOSans-bold;
    }
}

.scroll-view_H {
    white-space: nowrap;
    width: 100%;

    .selected {
        background-color: #0076A4;
        color: #ffffff;
    }

    .not_selected {
        background-color: rgba(0, 118, 164, 0.1);
        color: rgba(59, 59, 59, 1);
    }

    .scroll_view {
        display: inline-block;
        font-size: 24rpx;
        text-align: center;
        font-family: Arial;
        height: 50rpx;
        line-height: 50rpx;
        border-radius: 100rpx;
        padding: 0 40rpx;
        margin-right: 20rpx;
        margin-top: 30rpx;
    }
}
</style>