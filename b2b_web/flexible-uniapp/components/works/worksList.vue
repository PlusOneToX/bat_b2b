<template>
    <view>
        <uni-nav-bar v-if="(themeList.length > 0&&editStatus)" dark :fixed="true" shadow background-color="#ffffff" color="#000000" status-bar title="作品" left-text="退出编辑" @clickLeft="(editStatus = !editStatus)" />
        <uni-nav-bar v-else-if="(themeList.length > 0&&!editStatus)" dark :fixed="true" shadow background-color="#ffffff" color="#000000" status-bar title="作品" left-text="编辑" @clickLeft="(editStatus = !editStatus)" />
        <uni-nav-bar v-else dark :fixed="true" shadow background-color="#ffffff" color="#000000" status-bar title="作品" />

        <view class="works_list_box">
            <view class="works_meu_box" v-for="item in themeList" :key="item" @click="clickImg(item)">
                <view class="image_box">
                    <image :src="item.previewImage"></image>
                </view>
                <view class="works_name">{{item.modelName}} - {{item.categoryName}} {{item.modelName}}</view>
                <view class="works_operation_box">
                    <view class="amount_of_money">¥ {{item.price}}</view>
                    <view class="collection_meu" @click.stop="additionalPurchase(item)" v-if="!editStatus">
                        <image src="../../static/icons/shopping_trolley.png"></image>
                    </view>
                    <view class="collection_trash" @click.stop="deleteTrash(item.id)" v-else>
                        <uni-icons type="trash" color="#FC7E1B" size="24"></uni-icons>
                    </view>
                </view>
            </view>
            <view class="nothemeList" v-if="themeList.length==0">暂无收藏作品~</view>
            <view class="nothemeList1" v-if="themeList.length>0">已经到底啦~</view>
        </view>
    </view>
</template>
<script>
import api from "../../common/js/allApi.js";
export default {
    props: {
        bottomOut: {
            type: Boolean,
            default: true
        },
        upload: {
            type: Boolean,
            default: false
        },
    },
    data() {
        return {
            editStatus: false, // 是否是编辑状态
            page: 1,
            size: 8,
            nextPage: 0,
            themeList: []
        }
    },
    watch: {
        bottomOut: {
            handler(newName, oldName) {
                if (this.page < this.nextPage) {
                    this.page += 1;
                    uni.showLoading({
                        title: '加载中',
                        mask: true
                    });

                    this.getCollectionList();
                }
            },
            immediate: true
        },
        upload: {
            handler(newName, oldName) {
                if (oldName != undefined) {
                    this.page = 1;
                    this.size = 8;
                    this.nextPage = 0;
                    this.themeList = [];
                    this.getCollectionList();
                }
            },
            immediate: true
        }
    },

    mounted() {
        uni.setStorageSync("index", 0)
        this.getCollectionList();
    },
    methods: {
        //取消收藏
        deleteTrash(id) {
            this.$api.delete(this, api.getCollectionList, {
                id: id
            }).then((res) => {
                if (res.success) {
                    uni.showToast({
                        title: '取消成功',
                        icon: "none",
                        duration: 2000
                    });
                    this.themeList = this.themeList.filter((x) => x.id !== id);
                } else {
                    uni.showToast({
                        title: res.errMessage,
                        icon: "none",
                        duration: 2000
                    });
                }

            })
        },
        getPrice(materialsId) {
            var data = 0
            this.$api.get(this, api.getPrice, {
                distributorId: uni.getStorageSync("distributorId"),
                orderSource: uni.getStorageSync("platform"), // 平台,
                materialId: materialsId,
            }).then((res) => {
                data = res.data;

            })
            return data
        },
        additionalPurchase(e) {
            let info = {
                categoryId: 1,
                categoryName: '手机壳',
                brandId: e.brandId,
                brandName: e.brandName || 'apple',
                generateImage: e.generateImage,
                materialId: e.materialId,
                materialName: e.materialName,
                modelId: e.modelId,
                modelName: e.modelName,
                pictureId: e.pictureId,
                pictureName: e.pictureName,
                previewImage: e.previewImage,
                manufactors: e.manufactor//,//YC云创 MK麦客 LHW联辉王
            };
            this.$api.post(this, api.addToShopcart, {
                diy: info,
                itemCode: e.itemCode,
                itemCount: 1,
                itemType: 1, // 是否赠品：1 非赠品，2 赠品
                salePrice: e.price, // 价格
            }).then((res) => {
                if (res.success) {
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
        clickImg(e) {
            if (!this.editStatus) {
                // #ifdef MP-WEIXIN
                uni.navigateTo({
                    url: '/good_pages/customization/vessel?image=' + e.generateImage
                });
                // #endif
                // #ifdef H5
                uni.navigateTo({
                    url: '/good_pages/customization/index?image=' + e.generateImage + '&pictureId=' + e.pictureId
                });
                // #endif
            }
        },
        //获取收藏列表
        getCollectionList() {
            this.$api.get(this, api.getCollectionList, {
                page: this.page,
                size: this.size,
            }).then((res) => {
                this.nextPage = res.data.pages;
                let list = [];
                res.data.list.forEach(e => {
                    this.$api.get(this, api.getPrice, {
                        distributorId: uni.getStorageSync("distributorId"),
                        orderSource: uni.getStorageSync("platform"), // 平台,
                        materialId: e.materialId,
                    }).then((res) => {
                        e.price = res.data;
                        this.themeList.push(e);
                    })
                });
                uni.hideLoading()


            })
        },
    }
}
</script>
<style scoped lang="stylus">
.nothemeList {
    text-align: center;
    width: 100%;
    margin: 0 auto;
    line-height: 400rpx;
}

.nothemeList1 {
    text-align: center;
    width: 100%;
    height: 200rpx;
    line-height: 150rpx;
}

.works_list_box {
    width: 680rpx;
    margin: 0 auto;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;

    .works_meu_box {
        height: 480rpx;
        width: 320rpx;
        border-radius: 30rpx;
        background-color: rgba(255, 255, 255, 1);
        color: rgba(16, 16, 16, 1);
        font-size: 28rpx;
        text-align: center;
        box-shadow: 0rpx 4rpx 12rpx 0rpx rgba(0, 0, 0, 0.08);
        font-family: Arial;
        border: 1px solid rgba(239, 239, 239, 0.19);
        margin-top: 30rpx;

        .image_box {
            width: 320rpx;
            height: 360rpx;
            display: flex;
            justify-content: center;
            align-items: center;

            image {
                width: 160rpx;
                height: 296rpx;
            }
        }

        .works_name {
            width: 280rpx;
            margin: 0 auto;
            color: rgba(16, 16, 16, 1);
            font-size: 24rpx;
            text-align: left;
            font-family: PingFangSC-regular;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
            -o-text-overflow: ellipsis;
        }

        .works_operation_box {
            width: 280rpx;
            margin: 0 auto;
            display: flex;
            align-items: center;
            justify-content: space-between;
            height: 80rpx;

            .amount_of_money {
                color: rgba(252, 126, 27, 1);
                font-size: 30rpx;
                font-weight: 600;
                text-align: left;
                font-family: PingFangSC-regular;
            }

            .collection_trash {
                width: 50rpx;
                height: 50rpx;
                border-radius: 25rpx;
                background-color: rgba(252, 126, 27, 0.1);
                display: flex;
                justify-content: center;
                align-items: center;
            }

            .collection_meu {
                width: 50rpx;
                height: 50rpx;
                border-radius: 25rpx;
                background-color: rgba(0, 118, 164, 0.1);
                display: flex;
                justify-content: center;
                align-items: center;

                image {
                    width: 40rpx;
                    height: 40rpx;
                }
            }
        }
    }
}
</style>