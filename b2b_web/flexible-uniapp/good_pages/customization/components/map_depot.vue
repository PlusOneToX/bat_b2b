<template>
    <view>
        <uni-popup ref="popup" type="bottom" :safeArea="false" @maskClick="close">
            <view class="sele_img_box">
                <view class="sele_img_title_box">
                    <view v-if="platform!=7">选择图片</view>
                    <view v-else style="display: flex;align-items: center;">
                        主题图库
                        <!-- -webkit-transform: scale(0.8); -->
                        <view style="font-size: 24rpx;background-color: #fc7e1b;color: #ffffff;border-radius: 20rpx;padding: 6rpx 20rpx;margin-left: 10rpx;">
                            赠送同款主题
                        </view>
                    </view>
                    <uni-icons @click="close" type="closeempty" size="26"></uni-icons>
                </view>
                <view class="sele_img_title_box" v-show="type!=4&&platform!=7">
                    <view style="font-size: 24rpx;color: #808080;">
                        图片叠加
                    </view>
                    <view>
                        <switch :checked="Isreplace" color="#0076a4" style="transform:scale(0.7)" @change="switch1Change" />
                    </view>
                    <!-- <uni-icons @click="close" type="closeempty" size="26"></uni-icons> -->
                </view>
                <scroll-view class="image_type_meu_box" :show-scrollbar="false" :scroll-x="true">
                    <view @click="chooseCount(index,item.id)" v-for="(item,index) in picDataList" :key="index" class="image_type_meu" :class="selCount==index?'selected':'not_selected'">{{item.name}}</view>
                </scroll-view>
                <view></view>
                <scroll-view class="scroll_view_y" :show-scrollbar="false" :scroll-y="true" v-if="ImageList.length>0" @scrolltolower="ReachBottom">
                    <view class="image_box" v-if="type==1">
                        <image mode="scaleToFill" @click="seleImg(item)" v-for="(item) in ImageList" :key="item.id" :src="(item.originImage)+'?x-oss-process=image/resize,h_400,l_400'"></image>
                    </view>
                    <view class="image_box" v-else>
                        <image mode="aspectFit" @click="seleImg(item)" v-for="(item) in ImageList" :key="item.id" :src="(item.originImage)+'?x-oss-process=image/resize,h_400,l_400'"></image>
                    </view>
                    <view style="height: 100rpx;"></view>
                </scroll-view>
                <view v-if="ImageList.length==0" style="text-align: center;padding-top: 200rpx;">暂无图片~</view>
            </view>
        </uni-popup>
    </view>
</template>
<script>
import api from "../../../common/js/allApi";
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
            size: 12,
            totalPage: 0,
            ImageTypeId: '',
            imageUrl: '',
            Isreplace: false,
            type: 1,
            platform: uni.getStorageSync("platform")
        }
    },
    mounted() {
        this.Isreplace = uni.getStorageSync("Isreplace");
        if (this.Isreplace == '' || this.Isreplace == null || this.Isreplace == undefined) {
            this.Isreplace = false;
        }
        // console.log("这是：", this.Isreplace);
        // this.$refs.popup.open('bottom')
        // this.$refs.message.open();
    },
    watch: {
        showPopup: {
            handler(newName, oldName) {
                if (oldName != undefined) {
                    uni.showLoading({
                        title: '加载中',
                        mask: true
                    });
                    this.imageUrl = '';
                    this.selCount = 0;
                    this.type = 1;
                    this.page = 1;
                    this.size = 12;
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
                    uni.showLoading({
                        title: '加载中',
                        mask: true
                    });
                    this.imageUrl = '';
                    this.selCount = 0;
                    this.type = 4;
                    this.page = 1;
                    this.size = 12;
                    this.totalPage = 0;
                    this.open();
                    this.getPicList()
                }
            },
            immediate: true
        },
    },

    methods: {
        switch1Change: function (e) {
            this.Isreplace = e.detail.value;
            uni.setStorageSync("Isreplace", this.Isreplace);
        },
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
            this.size = 12;
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
                    uni.hideLoading()
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
            // height: 300rpx;
            // width: 220rpx;
            height: 320rpx;
            width: 160rpx;
            margin-bottom: 20rpx;
            margin-left: 23rpx;
        }
    }
}
.image_type_meu_box {
    margin-top: 20rpx;
    width: 700rpx;
    margin-left: 25rpx;
    white-space: nowrap;
    float: left;
    .selected {
        background-color: #0076a4;
        color: #ffffff;
    }

    .not_selected {
        background-color: rgba(0, 118, 164, 0.1);
        color: rgba(59, 59, 59, 1);
    }

    .image_type_meu {
        display: inline-block;
        font-size: 24rpx;
        text-align: center;
        font-family: Arial;
        // height: 50rpx;
        // line-height: 50rpx;
        border-radius: 100rpx;
        padding: 12rpx 40rpx;
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
