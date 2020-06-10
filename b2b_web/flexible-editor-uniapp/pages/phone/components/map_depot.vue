<template>
    <view>
        <uni-popup ref="popup" type="bottom" :safeArea="false" @maskClick="close">
            <view class="sele_img_box">
                <view class="sele_img_title_box">
                    <view>选择图片</view>
                    <uni-icons @click="close" type="closeempty" size="26"></uni-icons>
                </view>
                <scroll-view class="scroll-view_H" :show-scrollbar="false" :scroll-x="true">
                    <view @click="chooseCount(index,item.id)" v-for="(item,index) in picDataList" :key="index" class="scroll_view" :class="selCount==index?'selected':'not_selected'">{{item.name}}</view>
                </scroll-view>
                <view></view>
                <scroll-view class="scroll_view_y" :show-scrollbar="false" :scroll-y="true" v-if="ImageList.length>0" @scrolltolower="ReachBottom">
                    <view class="image_box">
                        <image mode="aspectFill" @click="seleImg(item.thumbnail||item.originImage)" v-for="(item) in ImageList" :key="item.id" :src="item.thumbnail||item.originImage"></image>
                    </view>
                </scroll-view>
            </view>
        </uni-popup>
    </view>
</template>
<script>
import api from "../../../api/allApi";
export default {
    props: {
        //手机型号id
        modelId: {
            type: Number,
            default: 0,
        },
        //二级材质id
        materialsId: {
            type: Number,
            default: 0,
        },
        TagsPopup: {
            type: Boolean,
            default: false,
        },
        showPopup: {
            type: Boolean,
            default: false,
        }
    },
    data() {
        return {
            selCount: 0,
            picDataList: [],
            ImageList: [],
            page: 1,
            size: 9,
            totalPage: 0,
            ImageTypeId: ''
        }
    },
    mounted() {
        // this.$refs.popup.open('bottom')
    },
    watch: {
        showPopup: {
            handler(newName, oldName) {
                console.log(newName, oldName);
                if (oldName != undefined) {
                    this.selCount = 0;
                    this.type = 1;
                    this.page = 1;
                    this.size = 9;
                    this.totalPage = 0;
                    this.picDataList = [];
                    this.ImageList = [];
                    this.open();
                    this.getPicList()
                }
            },
            immediate: true
        },
        TagsPopup: {
            handler(newName, oldName) {
                if (oldName != undefined) {
                    this.selCount = 0;
                    this.type = 4;
                    this.page = 1;
                    this.size = 9;
                    this.totalPage = 0;
                    this.open();
                    this.getPicList()
                }
            },
            immediate: true
        },
    },

    methods: {
        ReachBottom() {
            if (this.page < this.totalPage) {
                this.page += 1;
                uni.showLoading({
                    title: '加载中',
                    mask: true
                });
                this.getImageList();
            }
        },
        open() {
            this.$refs.popup.open('bottom')
        },
        seleImg(e) {
            this.close();
            this.$emit("seleImg", e);
        },
        chooseCount(index, id) {
            uni.showLoading({
                title: '加载中',
                mask: true
            });
            this.page = 1;
            this.size = 9;
            this.totalPage = 0;
            this.selCount = index;
            this.ImageTypeId = id;
            this.getImageList();
        },
        close() {
            this.$refs.popup.close();
            let timer = setInterval(() => {
                this.$emit("Iscanvas", true);
                clearTimeout(timer)
            }, 200);
        },
        // 获取图片列表
        getPicList() {
            this.$api.get(this, api.getImageTypeList, {
                materialId: this.materialsId,
                modelId: this.modelId,
                parentId: 0,
                type: this.type
            }).then((res) => {
                if (res.success) {
                    this.picDataList = res.data;
                    this.ImageTypeId = this.picDataList[0].id;
                    this.getImageList();
                }
            })
        },
        getImageList() {
            this.$api.get(this, api.getImageList, {
                materialId: this.materialsId,
                modelId: this.modelId,
                categoryId: this.ImageTypeId,
                page: this.page,
                size: this.size
            }).then((res) => {
                if (res.success) {
                    this.totalPage = res.data.pages;
                    this.ImageList = this.page == 1 ? res.data.list : [...this.ImageList, ...res.data.list];
                }
                uni.hideLoading()
            })
        },
    }
}
</script>
<style scoped lang="scss">
/deep/ ::-webkit-scrollbar {
    display: none;
    width: 0;
    height: 0;
    background-color: transparent;
}
.scroll_view_y {
    height: 780rpx;
    width: 750rpx;
    margin-top: 100rpx;
    .image_box {
        width: 750rpx;
        margin: 0 auto;
        display: flex;
        flex-wrap: wrap;
        image {
            border-radius: 20rpx;
            height: 300rpx;
            width: 220rpx;
            margin-bottom: 23rpx;
            margin-left: 23rpx;
        }
    }
}
.scroll-view_H {
    margin-top: 20rpx;
    width: 700rpx;
    margin-left: 25rpx;
    white-space: nowrap;
    float: left;
    // background-color: red;
    .selected {
        background-color: #0076a4;
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
    }
}
.sele_img_box {
    width: 750rpx;
    height: 1000rpx;
    border-radius: 40rpx 40rpx 0rpx 0rpx;
    background-color: rgba(253, 253, 253, 1);
    color: rgba(16, 16, 16, 1);
    font-family: Arial;
    border-top: 1rpx solid rgba(232, 232, 232, 0.5);
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
    }
}
</style>
