<template>
    <view>
        <uni-popup ref="popup" type="bottom" :safeArea="false" @maskClick="close">
            <view class="coverage_box">
                <view class="sele_img_title_box">
                    <view>图层</view>
                    <view class="coverage_set_box">
                        <view class="">
                            <uni-icons type="trash" size="26" @click="trash"></uni-icons>
                        </view>
                        <uni-icons type="closeempty" size="28" @click="close"></uni-icons>
                    </view>
                </view>
                <scroll-view class="scroll_view_y" :show-scrollbar="false" :scroll-y="true">
                    <!-- .slice().reverse() -->
                    <view class="coverage_list_box" v-for="(item,index) in data.slice().reverse()" :key="index">
                        <view class="let_box">
                            <image v-if="item.type=='image'" mode="aspectFit" :src="item.fileUrl"></image>
                            <view class="tetx_style" v-else :style="{'color':item.color,fontFamily: item.wordStyle.fontFile,fontWeight:item.wordStyle.fontWeight,textDecoration:item.wordStyle.textDecoration, fontStyle:item.wordStyle.fontStyle}">{{item.text}}</view>
                        </view>
                        <view class="right_box">
                            <view class="move_box" @click="move((index),'上')" v-if="index!=0">
                                <image src="../../../static/icons/up.png"></image>
                            </view>
                            <view class="move_box" @click="move(index,'下')" v-if="index+1!=data.length">
                                <image style="transform: rotate(180deg);" src="../../../static/icons/up.png"></image>
                            </view>
                            <view class="delete_box" @click="delet(index)">
                                <image style="width: 60rpx;height: 60rpx;" src="../../../static/icons/delete.png"></image>
                            </view>
                        </view>
                    </view>

                </scroll-view>
            </view>
        </uni-popup>
    </view>
</template>
<script>
export default {
    data() {
        return {
            data: []
        }
    },
    mounted() {
        // this.$refs.popup.open('bottom')
    },
    methods: {
        close() {
            this.$refs.popup.close();
            let timer = setInterval(() => {
                this.$emit("Iscanvas", true);
                clearTimeout(timer)
            }, 200);
        },
        setCoverage(e) {
            this.data = e;
            this.$refs.popup.open('bottom');
        },
        trash() {
            this.$emit("onCoverage", []);
            this.close();
        },
        delet(index) {
            index = this.data.length - (index + 1);
            this.data.splice(index, 1);
            this.$set(this.data)
            this.$emit("onCoverage", this.data);
        },
        move(index, pu) {
            index = this.data.length - (index + 1);
            if (pu == '下') {
                let arr = this.data;
                arr.splice(index - 1, 1, ...arr.splice(index, 1, arr[index - 1]));
            }
            else {
                let arr = this.data;
                arr.splice(index, 1, ...arr.splice(index + 1, 1, arr[index]));
            }
            this.$emit("onCoverage", this.data);
        },
    }
}
</script>
<style scoped lang="scss">
@import "./font/font.css";
.coverage_box {
    width: 750rpx;
    height: 900rpx;
    border-radius: 40rpx 40rpx 0rpx 0rpx;
    background-color: rgba(253, 253, 253, 1);
    color: rgba(16, 16, 16, 1);
    font-family: Arial;
    border-top: 1rpx solid rgba(232, 232, 232, 0.5);
    .scroll_view_y {
        height: 820rpx;
        .coverage_list_box {
            width: 690rpx;
            height: 120rpx;
            border-bottom: 1rpx dotted rgba(0, 118, 164, 0.1);
            margin: 0 auto;
            display: flex;
            justify-content: space-between;
            align-items: center;
            .let_box {
                width: 450rpx;
                color: rgba(59, 59, 59, 1);
                font-size: 26rpx;
                text-align: left;
                font-family: OPPOSans-bold;
                image {
                    max-width: 80rpx;
                    max-height: 100rpx;
                }
                .tetx_style {
                    width: 450rpx;
                    overflow: hidden;
                    white-space: nowrap;
                    text-overflow: ellipsis;
                    -o-text-overflow: ellipsis;
                }
            }
            .right_box {
                width: 240rpx;
                display: flex;
                justify-content: flex-end;
                align-items: center;
                .move_box {
                    width: 60rpx;
                    height: 60rpx;
                    line-height: 60rpx;
                    background-color: rgba(0, 118, 164, 0.1);
                    text-align: center;
                    font-family: Arial;
                    border-radius: 30rpx;
                    margin-right: 20rpx;
                    image {
                        width: 60rpx;
                        height: 60rpx;
                    }
                }
                .delete_box {
                    width: 60rpx;
                    height: 60rpx;
                    line-height: 60rpx;
                    background-color: rgba(217, 59, 17, 0.1);
                    text-align: center;
                    font-family: Arial;
                    border-radius: 30rpx;
                }
            }
        }
    }
    .sele_img_title_box {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 750rpx;
        height: 80rpx;
        padding: 0 40rpx;
        box-sizing: border-box;
        color: rgba(59, 59, 59, 1);
        font-size: 32rpx;
        font-family: OPPOSans-bold;
        .coverage_set_box {
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 160rpx;
            .delete_icon_box {
                width: 60rpx;
                height: 60rpx;
                line-height: 60rpx;
                background-color: rgba(232, 232, 232, 0.5);
                color: rgba(16, 16, 16, 1);
                text-align: center;
                font-family: Arial;
                border-radius: 40rpx;
            }
        }
    }
}
</style>