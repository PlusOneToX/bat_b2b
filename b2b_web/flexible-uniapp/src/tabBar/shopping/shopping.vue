<template>
    <view class="shoppingCart">
        <uni-nav-bar v-if="(mGoods.length > 0&&editStatus)" dark :fixed="true" shadow background-color="#ffffff" color="#000000" status-bar title="购物车" left-text="退出编辑" @clickLeft="(editStatus = !editStatus)" />
        <uni-nav-bar v-else-if="(mGoods.length > 0&&!editStatus)" dark :fixed="true" shadow background-color="#ffffff" color="#000000" status-bar title="购物车" left-text="编辑" @clickLeft="(editStatus = !editStatus)" />
        <uni-nav-bar v-else dark :fixed="true" shadow background-color="#ffffff" color="#000000" status-bar title="购物车" />

        <!-- 11-17 -->
        <view class="sc-list-scroll" v-if="mGoods.length > 0">
            <view v-for="(item, index) in mGoods" :key="index">
                <view class="sc-list" v-for="(items, index1) in item" :key="index1">
                    <view class="sc-list-smUniAction">
                        <view class="sc-list-line">
                            <view class="sc-list-lineItem">
                                <view class="select_box1" @click="handleSelectSingle(items.id)">
                                    <image src="../../../static/icons/multiple_choice1.png" v-if="allSelectedGoods.indexOf(items.id) >= 0"></image>
                                    <image src="../../../static/icons/multiple_choice.png" v-else></image>
                                </view>
                                <view class="sc-list-lineImgBox">
                                    <!-- <view @click="preview(items.diy.previewImage)" style="position: absolute;height: 160rpx;z-index: 2;left: 120rpx;width: 100rpx;"></view> -->
                                    <image @click="preview(items.diy.previewImage)" :src="(items.diy.previewImage)+'?x-oss-process=image/resize,h_400,l_400/'" class="sc-smGood" mode="aspectFit"></image>
                                </view>
                                <view class="sc-Rg" @click="handleSelectSingle(items.id)">
                                    <view class="sc-RgLf">
                                        <view class="sc-list-bgRgName">
                                            {{ items.itemName }}</view>
                                        <view class="sc-RgLf-info">
                                            <text>规格：</text>
                                            <text>{{ items.diy.materialName }}</text>
                                        </view>
                                        <view class="sc-RgLf-info" v-if="distributorId == 1217">
                                            <text>图片ID：</text>
                                            <text>{{ items.diy.pictureId}}</text>
                                        </view>
                                        <view class="sc-RgLf-info" v-if="items.diy && items.diy.modelName">
                                            <text>机型：</text>
                                            <text>{{ items.diy.modelName }}</text>
                                        </view>
                                    </view>

                                </view>
                            </view>
                            <view class="sc-list-lineItemBtm">
                                <view class="sc-sm-stock">

                                </view>
                                <view class="sc-sm-btm">
                                    <view class="sc-smRg-topPrice">￥{{ items.salePrice }}
                                    </view>
                                    <!-- 加减货品 -->
                                    <view class="sc-uni-num">
                                        <view class="ks-calculator">
                                            <text @click="changeNum(items.id, items.itemCount, -1)">-</text>
                                            <input :value="items.itemCount" disabled :data-id="items.id" @click="handleShowKeyboard" />
                                            <text @click="changeNum(items.id, items.itemCount, 1)">+
                                            </text>
                                        </view>
                                    </view>
                                </view>
                            </view>
                        </view>

                    </view>
                </view>
            </view>
            <view class="no-more">没有更多了~</view>
        </view>
        <view class="sc-btm-total" v-if="(mGoods.length > 0 && !editStatus)">
            <view style="display: flex;justify-content: space-between;align-items: center;height: 120rpx;">
                <view class="sc-btm-totalAll" @click="handleSelectAll">
                    <view class="select_box1">
                        <image src="../../../static/icons/multiple_choice1.png" v-if="allSelectedGoods.length == goods.length && goods"></image>
                        <image src="../../../static/icons/multiple_choice.png" v-else></image>
                    </view>
                    <text>全选</text>
                </view>
                <view class="sc-btm-totalNum">
                    <view class="sc-btm-totalNum-price">
                        <view style="height: 40rpx;">
                            <text>总计：</text>
                            <text>￥{{ totalPrice }}</text>
                        </view>
                        <view style="height: 40rpx;">
                            <text>{{number}}种 {{totalCount}}件不含运费</text>
                        </view>
                    </view>
                    <view class="sc-btm-totalBtn" @click="handleSubmit">立即下单</view>
                </view>
            </view>
        </view>
        <!-- 删除 -->
        <view class="sc-btm-total" v-if="editStatus&&!noPro">
            <view style="display: flex;justify-content: space-between;align-items: center;height: 120rpx;">
                <view class="sc-btm-totalAll" @click="handleSelectAll">
                    <view class="select_box1">
                        <image src="../../../static/icons/multiple_choice1.png" v-if="allSelectedGoods.length == goods.length && goods"></image>
                        <image src="../../../static/icons/multiple_choice.png" v-else></image>
                    </view>
                    <text>全选</text>
                </view>
                <view class="sc-btm-detete" @click="handleDeleteAll(allSelectedGoods)">删除</view>
            </view>
        </view>
        <uni-popup ref="message" type="dialog">
            <uni-popup-dialog type="info" cancelText="取消" confirmText="确定" title="提示" content="确定要删除选中的商品吗" @confirm="dialogConfirm"></uni-popup-dialog>
        </uni-popup>
        <uni-popup ref="message1" type="dialog">
            <uni-popup-dialog type="info" cancelText="我再想想" confirmText="确认提交" title="温馨提示" content="您的勾选存在专属定制的商品，非质量问题不支持无理由退换喔，是否继续提交结算？" @confirm="orderConfirm"></uni-popup-dialog>
        </uni-popup>
        <!-- 空数据 展示-->
        <view class="shopping-no-data" v-if="noPro">
            <image src="../../../static/images/no-data.png"></image>
        </view>
    </view>
</template>

<script>
import api from "common/js/allApi.js";
export default {
    data() {
        return {
            pid: 0,
            flag: false,
            isTabIn: false, // 是否是点击tab进入
            noPro: true, // 购物车是否为空
            editStatus: false, // 是否是编辑状态
            allSelectedGoods: [], // 已选商品
            allChecked: false, // 全选
            goods: [], // 购物车商品
            mGoods: [], // 购物车拆单商品
            invalidGoods: [], // 失效商品
            showKeyboard: false, // 是否显示数字键盘
            value: "", // 数字键盘输入值
            originVal: "", // 输入前的值
            currentId: false, // 当前修改数量的商品id
            curVersion: "",
            userNo: "",
            distributorId: "", // 分销商ID
            showFlexible: false, //是否显示柔性关闭弹窗
            number: 0,
            totalCount: 0
        };
    },
    onShow() {
        uni.setStorageSync("index", 0)
        this.allSelectedGoods = []; // 已选商品
        this.goods = []; // 购物车商品
        this.mGoods = []; // 购物车拆单商品
        this.distributorId = uni.getStorageSync("distributorId");
        this.curVersion = uni.getStorageSync("curVersion"); // 获取当前进入版本
        this.userNo = uni.getStorageSync("userNo");
        this.initData();
    },
    methods: {
        //预览图片
        preview(img) {
            uni.previewImage({
                urls: [img],
            });
        },
        // 初始化数据
        initData() {
            // this.pid = parseInt(this.$route.query.pid || '');
            this.$api.get(this, api.getShopcartList).then((res) => {
                if (res.success) {
                    if (res.data && res.data.length > 0) {
                        this.noPro = false;
                        let goodsData = res.data;
                        // 未返回价格，默认设置为0
                        goodsData.forEach((item) => {
                            if (!item.salePrice) {
                                this.$set(item, "salePrice", 0);
                            }
                        });

                        // 根据 openFlag 判断商品是否失效（1 有效，0 无效）
                        let validData = goodsData.filter((item) => {
                            return item.openFlag;
                        });
                        let invalidData = goodsData.filter((item) => {
                            return !item.openFlag;
                        });
                        this.goods = validData.sort(this.dataDown);
                        this.invalidGoods = invalidData; // 失效商品

                        // 设置默认选中项，加入购物车
                        this.allSelectedGoods = [];

                        // 设置是否选中属性
                        if (this.pid) {
                            this.allSelectedGoods.push(this.pid);
                        }

                        // 根据货品编码分组显示
                        this.mGoods = this.arrayGroupBy(this.goods, "itemCode");

                    } else {
                        this.noPro = true;
                    }

                    console.log(this.goods);
                    console.log(this.allSelectedGoods);
                }
            });
        },
        // 增减数量
        changeNum(k, num, type) {
            // 如果是减少，要判断 > 1，最少是1
            if (type === -1) {
                this.goods.forEach((item) => {
                    if (k === item.id) {
                        if (item.itemCount > 1) {
                            item.itemCount = parseInt(item.itemCount) + type;
                            // 更新数据
                            this.refreshData(k, item.goodsType, item.itemCount);
                        } else {
                            // this.handleDelete([k]);
                        }
                    }
                });
            } else {
                this.goods.forEach((item) => {
                    if (k === item.id) {
                        // 设置商品增加上限为 1000
                        if (item.itemCount < 1000) {
                            item.itemCount = parseInt(item.itemCount) + parseInt(type);
                            // 更新数据
                            this.refreshData(k, item.goodsType, item.itemCount);
                        } else {
                            uni.showToast({
                                icon: 'none',
                                title: '不能购买更多商品了',
                                duration: 2000
                            });
                            return false;
                        }
                    }
                });
            }
        },
        // 更新数据
        refreshData(id, goodsType, itemCount) {
            this.$api.put(this, api.updataShopcart, {
                id: id,
                goodsType: goodsType,
                itemCount: itemCount,
                itemType: 1, // 是否赠品：1 非赠品，2 赠品
            })
                .then((res) => {
                    if (!res.success) {
                        uni.showToast({
                            icon: 'none',
                            title: res.errMessage,
                            duration: 2000
                        });
                    }
                });
        },
        // 显示数字键盘
        handleShowKeyboard(e) {
            this.currentId = Number(e.currentTarget.dataset.id);
            this.showKeyboard = true;
            this.goods.forEach((good) => {
                if (good.id === this.currentId) {
                    this.originVal = good.itemCount;
                }
            });

            this.value = "";
        },
        // 数字键盘输入
        handleKeyInput(key) {
            console.log("输入: " + key);
            this.value = (this.value + key).replace(/^0|\D/g, "");
            if (Number(this.value) <= 0) {
                uni.showToast({
                    icon: 'none',
                    title: "购买商品最低数量需为1",
                    duration: 2000
                });
                this.value = 1;
            }

            if (Number(this.value) >= 1000) {
                uni.showToast({
                    icon: 'none',
                    title: "不能购买更多商品了~",
                    duration: 2000
                });
                this.value = 1000;
            }

            this.goods.forEach((good) => {
                if (good.id === this.currentId) {
                    this.$set(good, "itemCount", this.value);
                }
            });
        },
        // 数字键盘删除
        handleDelInput() {
            this.goods.forEach((good) => {
                if (good.id === this.currentId) {
                    this.value = good.itemCount + "";
                    this.value = this.value.substring(0, this.value.length - 1);
                    if (this.value == "") {
                        uni.showToast({
                            icon: 'none',
                            title: "商品数量最低为1",
                            duration: 2000
                        });
                        this.value = 1
                    }
                    this.$set(good, "itemCount", this.value);
                }
            });
        },
        // 关闭数字键盘
        handleCloseKeyboard() {
            this.showKeyboard = false;

            let curGoodsTye = "";
            let curItemCount = "";
            this.goods.forEach((good) => {
                if (good.id === this.currentId) {
                    if (good.itemCount === 0) {
                        this.$set(good, "itemCount", this.originVal);
                    }

                    curGoodsTye = good.goodsType;
                    curItemCount = good.itemCount;
                }
            });

            this.refreshData(this.currentId, curGoodsTye, curItemCount);
        },
        // 全选
        handleSelectAll() {
            if (this.allSelectedGoods.length == this.goods.length && this.goods) {
                this.allSelectedGoods = [];
                this.number = this.allSelectedGoods.length;
            } else {
                this.allSelectedGoods = []; // 先置空，然后再重新添加
                this.goods.forEach((v, k) => {
                    if (!v.isStockOut) {
                        this.allSelectedGoods.push(v.id);
                    }
                });
                this.number = this.allSelectedGoods.length;
            }
        },
        // 单选
        handleSelectSingle(id) {
            if (this.allSelectedGoods.indexOf(id) < 0) {
                this.allSelectedGoods.push(id);
                this.number = this.allSelectedGoods.length;
            } else {
                for (var i = 0; i < this.allSelectedGoods.length; i++) {
                    if (id === this.allSelectedGoods[i]) {
                        this.allSelectedGoods.splice(i, 1);
                        break;
                    }
                }
                this.number = this.allSelectedGoods.length;
            }
        },
        // 结算
        orderConfirm() {
            let goodsArr = [];
            this.goods.forEach((good) => {
                if (this.allSelectedGoods.indexOf(good.id) >= 0) {
                    goodsArr.push(good);
                }
            });
            uni.setStorageSync("goods", JSON.stringify(goodsArr));
            uni.setStorageSync("totalCount", this.totalCount);
            uni.navigateTo({
                url: '../../../good_pages/confirmOrder/confirmOrder'
            });
        },
        handleSubmit() {
            // && this.totalPrice > 0
            if (this.allSelectedGoods.length > 0) {
                this.$refs.message1.open();
            } else {
                uni.showToast({
                    icon: "none",
                    title: "请选择需要购买的商品~",
                    duration: 2000,
                });
            }
        },
        // 删除确认
        handleDelete(ids) {
            if (ids.length <= 0) {
                uni.showToast({
                    icon: "none",
                    title: "请选择要删除的商品~",
                    duration: 2000,
                });
            } else {
                this.$refs.message.open();
            }
        },
        dialogConfirm() {
            this.deleteData(this.allSelectedGoods);
            this.$refs.message.open()
        },
        // 删除数据
        deleteData(ids) {
            this.$api.delete(this, api.deleteShopcart, {
                ids: ids,
            })
                .then((res) => {
                    if (res.success) {
                        uni.showToast({
                            icon: "none",
                            title: "删除成功！",
                            duration: 2000,
                        });
                        this.goods = [];
                        this.mGoods = [];
                        this.initData();
                    } else {
                        uni.showToast({
                            icon: 'none',
                            title: res.errMessage,
                            duration: 2000
                        });
                    }
                }).catch(() => { });
        },
        // 删除全部
        handleDeleteAll(ids) {
            if (ids.length <= 0) {
                uni.showToast({
                    icon: "none",
                    title: "未选择任何商品",
                    duration: 2000,
                });
                return false;
            } else {
                this.handleDelete(ids);
            }
        },
        // 清空失效
        handleEmpty() {
            var ids = [];
            this.invalidGoods.forEach((v, k) => {
                ids.push(v.id);
            });
            this.$dialog
                .confirm({
                    message: "确定要清空失效商品吗？",
                    className: "confirm-v-dialog",
                    confirmButtonColor: "#0076A5",
                })
                .then(() => {
                    this.deleteData(ids);
                })
                .catch(() => { });
        },
        // 倒序
        dataDown(x, y) {
            return y.id - x.id;
        },
        // 分组函数
        groupBy(array, f) {
            let groups = {};
            array.forEach((o) => {
                var group = JSON.stringify(f(o));
                groups[group] = groups[group] || [];
                groups[group].push(o);
            });
            return Object.keys(groups).map((group) => {
                return groups[group];
            });
        },
        arrayGroupBy(list, groupId) {
            let sorted = this.groupBy(list, (item) => {
                return [item[groupId]];
            });
            return sorted;
        },

        //点击知道了隐藏柔性关闭弹窗
        clickFlexible() {
            this.showFlexible = false;
        }
    },
    computed: {
        // totalPrice 计算总价
        totalPrice() {
            var totalprice = 0;
            var totalCount = 0;
            this.goods.forEach((v, k) => {
                if (this.allSelectedGoods.indexOf(v.id) !== -1) {
                    totalprice += v.salePrice * v.itemCount;
                    totalCount += v.itemCount;

                }
            });
            this.totalCount = totalCount;
            return totalprice.toFixed(2);
        },
    },
};
</script>

<style scoped lang="stylus" >
.no-more {
    padding: 20px 0;
    font-size: 12px;
    color: $lightBlack;
    text-align: center;
}

.select_box1 {
    width: 36rpx;
    height: 36rpx;
    border-radius: 18rpx;
    display: flex;
    justify-content: center;
    align-items: center;

    // background-color: rgba(0, 118, 164, 0.1);
    // background-color: #ffffff;
    // border: 1rpx solid rgba(59, 59, 59, 0.5);
    image {
        width: 36rpx;
        height: 36rpx;
    }
}

.pitch_on {
    border-radius: 14rpx;
    width: 28rpx;
    height: 28rpx;
    // background-color: #0076A4;
    background-color: #000;
}

.pitch_on1 {
    border-radius: 16rpx;
    width: 32rpx;
    height: 32rpx;
    background-color: rgba(0, 118, 164, 0.1);
}

.self-numberBox {
    display: flex;
    align-items: center;

    text:nth-child(1), text:nth-child(3) {
        display: block;
        font-size: 40rpx;
        color: #999;
        width: 50rpx;
        height: 45rpx;
        line-height: 45rpx;
        margin-left: 10rpx;
        background: #fafafc;
        text-align: center;
        border-radius: 5rpx;
    }

    input {
        width: 80rpx;
        height: 45rpx;
        line-height: 45rpx;
        background: #f2f3f8;
        border-radius: 10rpx;
        margin-left: 10rpx;
        text-align: center;
        font-size: 26rpx;
    }
}

.shoppingtop-title {
    display: flex;
    align-items: center;
    padding: 0 30rpx;
    background: #fff;

    text {
        display: block;
        position: absolute;
        width: auto;
        font-size: 26rpx;
    }

    view {
        font-size: 32rpx;
        font-weight: 400;
        width: calc(100% - 200rpx);
        height: 80rpx;
        line-height: 80rpx;
        text-align: center;
        margin: 0 auto;
    }
}

// 计算器
.ks-calculator {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    width: 350rpx;

    text {
        width: 50rpx;
        height: 50rpx;
        line-height: 50rpx;
        font-size: 40rpx;
        color: #647077;
        text-align: center;
        border-radius: 10rpx;
        background-color: rgba(255, 255, 255, 1);
        border: 1rpx solid rgba(247, 247, 247, 1);
    }

    input {
        width: 70rpx;
        height: 40rpx;
        font-size: 26rpx;
        color: #333;
        text-align: center;
        margin: 0 10rpx;
    }
}

.shoppingCart {
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #333333;

    .guize-listBox {
        padding: 30rpx 85rpx;
        font-size: 26rpx;

        view {
            padding: 10rpx;
        }
    }

    .sc-list-scroll {
        width: 100%;
        // height: calc(100vh - 60rpx - var(--status-bar-height) - 15rpx);
        padding: 30rpx 30rpx 0 30rpx;
        padding-bottom: 150rpx;
        // #ifdef H5
        height: calc(100vh - 211rpx - var(--window-bottom));
        padding-top: 20rpx;
        // #endif
        overflow-y: auto;
        box-sizing: border-box;
    }

    .sc2-iconCheck {
        font-size: 40rpx;
        margin-top: 30rpx;
    }

    .sc-list {
        border-radius: 16rpx;
        margin-bottom: 20rpx;

        .sc-list-line {
            .sc-list-lineItem {
                display: flex;
                align-items: flex-start;
                align-items: center;
            }

            .sc-list-linePresent {
                width: 35rpx;
                background: #ed5307;
                color: #fff;
                text-align: center;
                font-size: 22rpx;
                border-radius: 5rpx;
                padding: 5rpx 0;
                margin-top: 30rpx;
            }

            .sc-list-lineImgBox {
                // margin-left: 30rpx;
                view {
                    width: 120rpx;
                    text-align: center;
                    margin-top: 10rpx;
                    font-size: 28rpx;
                }

                .sc-smGood {
                    width: 160rpx;
                    height: 160rpx;
                    border-radius: 8rpx;
                    overflow: hidden;
                }
            }

            .sc-Rg {
                margin-left: 10rpx;
                width: 440rpx;

                .sc-RgLf {
                    display: flex;
                    flex-direction: column;
                    justify-content: space-between;
                    min-height: 140rpx;

                    // height: 160rpx;
                    .sc-list-bgRgName {
                        font-weight: 600;
                        font-size: 32rpx;
                        width: 440rpx;
                        display: -webkit-box;
                        -webkit-box-orient: vertical;
                        -webkit-line-clamp: 2;
                        overflow: hidden;
                    }

                    .sc-RgLf-info {
                        font-size: 24rpx;
                        display: flex;
                        margin-top: 8rpx;

                        text:nth-child(1) {
                            color: rgb(48, 48, 48);
                        }

                        text:nth-child(2) {
                            color: #999;
                            width: 320rpx;
                            overflow: hidden;
                            text-overflow: ellipsis;
                            white-space: nowrap;
                        }
                    }
                }
            }
        }

        // 在途在库---计算器
        .sc-list-lineItemBtm {
            display: flex;
            align-items: center;
            justify-content: space-between;

            // padding-top: 28rpx;
            .sc-sm-stock {
                font-size: 24rpx;

                text:nth-child(2) {
                    margin-left: 32rpx;
                }
            }

            .sc-sm-btm {
                display: flex;
                align-items: center;
                justify-content: space-between;
                width: 430rpx;

                .sc-smRg-topPrice {
                    color: rgba(252, 126, 27, 1);
                    font-size: 26rpx;
                    text-align: left;
                    font-family: PingFangSC-semiBold;
                }

                .sc-sm-btm-LF {
                    font-size: 24rpx;
                    color: #999;

                    text {
                        display: block;
                        margin-top: 6rpx;
                    }
                }

                .sc-uni-num {
                    position: relative;

                    .sc-listItem-KuCunBox {
                        position: absolute;
                        left: 15rpx;
                        bottom: 80rpx;
                        z-index: 999;

                        .ks-listItem-KuCun {
                            position: relative;
                            background: #fff;
                            box-shadow: 0px 4rpx 20rpx 0px #e0e3ee;
                            width: 204rpx;
                            height: 108rpx;
                            line-height: 108rpx;
                            text-align: center;

                            .ks-listItem-KuCunTrangle {
                                position: absolute;
                                bottom: -10rpx;
                                left: 90rpx;
                                width: 0;
                                height: 0;
                                border-left: 15rpx solid transparent;
                                border-right: 15rpx solid transparent;
                                border-top: 15rpx solid #fff;
                            }

                            .ks-listItem-KuCunText {
                                color: #f94021;
                                font-size: 24rpx;
                            }
                        }
                    }

                    .num-label {
                        font-size: 24rpx;
                    }
                }
            }
        }

        .sc-list-label {
            position: relative;
            padding: 24rpx 30rpx;
            min-height: 42rpx;
            border-bottom: 1rpx solid #f3f4f8;
            display: flex;
            align-items: center;
            justify-content: space-between;

            &.no-promotion {
                justify-content: space-between;

                .sc-list-labelTj {
                    text-align: right;
                }

                .sc-list-labelBtn {
                    float: right;
                }
            }

            view {
                display: flex;
                align-items: center;

                &.hasGift {
                    padding-right: 120rpx;
                    width: 50%;
                    box-sizing: border-box;
                }

                text {
                    display: block;
                }
            }

            .sc-list-labelName {
                font-size: 28rpx;
            }

            .sc-list-labelTj {
                display: block;
                color: #999;
                font-size: 24rpx;
                margin-left: 30rpx;
                max-width: 350rpx;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
            }

            .sc-list-labelIcon {
                width: 24rpx;
                height: 24rpx;
                margin-left: 10rpx;
            }

            .sc-list-labelBtn {
                position: absolute;
                right: 30rpx;
                border: 1rpx solid $base-color1;
                color: $base-color1;
                font-size: 20rpx;
                display: block;
                padding: 5rpx 10rpx;
                border-radius: 10rpx;
            }
        }
    }

    .sc-list-smUniAction {
        padding: 24rpx 30rpx 24rpx 20rpx;
        margin-bottom: 20rpx;
        background-color: #ffffff;
        border-radius: 30rpx;
        border: 1rpx solid rgba(239, 239, 239, 1);

        .sc-list-switchActivity {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 20rpx 30rpx 0 30rpx;

            .sc-list-switchActivity-title {
                color: #333;
                font-size: 24rpx;
            }

            .switchActivity-btn {
                color: $base-color1;
                cursor: pointer;

                text {
                    font-size: 22rpx !important;
                    margin-left: 15rpx;
                }
            }
        }
    }

    .sc-btm-totalAll {
        font-size: 28rpx;
        display: flex;
        align-items: center;
        margin-left: 20rpx;

        .sc3-iconCheck {
            font-size: 40rpx;
            margin-left: 0;
        }

        text {
            margin-left: 12rpx;
        }
    }

    .sc-btm-detete {
        width: 200rpx;
        height: 70rpx;
        line-height: 70rpx;
        border-radius: 50rpx 0rpx 0rpx 50rpx;
        background-color: rgba(0, 118, 164, 1);
        color: #ffffff;
        font-size: 26rpx;
        text-align: center;
        font-family: Arial;
    }

    // 购买总计
    .sc-btm-total {
        background: #fff;
        position: fixed;
        z-index: 999;
        bottom: 0rpx;
        // #ifdef H5
        bottom: var(--window-bottom);
        // #endif
        width: 100%;
        border-top: 1rpx solid #f3f4f8;

        .sc-btm-totalTop {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 24rpx 30rpx;

            .sc-btm-totalNum-jina {
                font-size: 24rpx;

                text {
                    padding: 0 6rpx;
                }
            }
        }

        .sc-btm-totalNum {
            padding-left: 32rpx;
            display: flex;
            align-items: center;

            // justify-content: space-between;
            // padding-bottom: 18rpx;
            .sc-btm-totalNum-discounts {
                color: #f94021;
                font-family: ArialMT;
                font-size: 24rpx;

                text {
                    font-family: PingFangSC-Regular, PingFang SC;
                }
            }

            .sc-btm-totalNum-price {
                // width: 300rpx;
                text-align: right;
                margin-right: 20rpx;
                height: 80rpx;
                font-size: 24rpx;

                text:nth-child(2) {
                    font-family: Arial-BoldMT, Arial;
                    font-weight: normal;
                    color: #fc7e1b;
                }

                text:nth-child(1) {
                    font-size: 24rpx;
                }
            }

            .sc-btm-totalBtn {
                // width: 192rpx;
                // line-height: 80rpx;
                // height: 80rpx;
                // color: #fff;
                // text-align: center;
                // font-size: 30rpx;
                width: 200rpx;
                height: 70rpx;
                line-height: 70rpx;
                border-radius: 50rpx 0rpx 0rpx 50rpx;
                background-color: rgba(252, 126, 27, 1);
                color: #fff;
                font-size: 26rpx;
                text-align: center;
                font-family: Arial;
            }
        }
    }

    .sc-activity-iconY {
        color: $base-color1;
        font-size: 40rpx;
        margin-top: 5rpx;
    }

    .sc-activity-iconN {
        color: #999;
        font-size: 40rpx;
        margin-top: 5rpx;
    }

    // 切换活动
    .sc-aCivityPopup-content {
        padding-bottom: 50rpx;
        background: #fff;
        border-radius: 30rpx 30rpx 0px 0px;

        .myCollect-scroll-Y {
            max-height: 52vh;
            height: auto;
        }

        .sc-activity-list {
            .sc-activity-listBox {
                margin: 0 30rpx;
                padding: 20rpx 0;

                & + .sc-activity-listBox {
                    border-top: 1rpx solid $opacity-border;
                }

                .sc-activity-listName {
                    color: #333;
                    font-size: 26rpx;
                    font-weight: bold;
                    margin-left: 20rpx;
                }

                .sc-activity-listGuizeBox {
                    margin-left: 50rpx;
                    margin-top: 20rpx;
                    font-size: 24rpx;
                    color: #999;
                    display: flex;

                    .sc-activity-listGuizeTitle {
                        width: 160rpx;
                    }

                    .sc-activity-listGuizeList {
                        text {
                            display: block;
                            padding: 5rpx 0;
                        }
                    }
                }
            }
        }

        .sc-activity-listBtn {
            color: #fff;
            text-align: center;
            width: 690rpx;
            height: 80rpx;
            line-height: 80rpx;
            font-size: 28rpx;
            margin: 30px auto 0;
            border-radius: 40rpx;
        }
    }

    // 选择赠品
    .popup-giftsList {
        padding: 30rpx;

        .popup-giftsList-line {
            display: flex;
            align-items: center;
            padding: 30rpx 0;

            & + .popup-giftsList-line {
                border-top: 1rpx solid $opacity-border;
            }

            image {
                width: 111rpx;
                height: 111rpx;
                margin-left: 15rpx;
            }

            .giftsList-line-rg {
                margin-left: 20rpx;

                .popup-giftsList-top {
                    display: flex;
                    justify-content: space-between;

                    .popup-giftsList-name {
                        width: 320rpx;
                        font-weight: bold;
                        display: -webkit-box;
                        -webkit-box-orient: vertical;
                        -webkit-line-clamp: 2;
                        overflow: hidden;
                    }

                    .popup-giftsList-numBox {
                        margin-left: 25rpx;
                        text-align: right;
                        width: 200rpx !important;
                    }
                }

                .popup-giftsList-num {
                    font-size: 22rpx;
                    color: #999;
                    margin-top: 10rpx;
                    display: flex;
                    justify-content: space-between;
                }
            }
        }
    }

    .popup-giftsList-btm {
        padding: 30rpx;

        .popup-giftsList-total {
            margin-bottom: 30rpx;
            display: flex;
            justify-content: flex-end;
            color: #999;

            text {
                color: #ed5307;
                padding: 0 5rpx;
            }
        }

        .popup-giftsList-btn {
            width: 100%;
            height: 80rpx;
            line-height: 80rpx;
            font-size: 28rpx;
            color: #fff;
            border-radius: 40rpx;
            text-align: center;
        }
    }
}

.shopping-no-data {
    padding-top: 220rpx;
    padding-bottom: 44rpx;
    background: #fff;
    text-align: center;
    min-height: 100vh;
    // #ifdef H5
    min-height: calc(100vh - var(--window-bottom));
    // #endif
    box-sizing: border-box;

    image {
        width: 502rpx;
        height: 496rpx;
    }

    view {
        color: #fff;
        margin: 86rpx auto 0 auto;
        width: 360rpx;
        height: 80rpx;
        line-height: 80rpx;
        text-align: center;
        border-radius: 40rpx;
        font-size: 28rpx;
    }
}

.checkbox {
    width: 20px;
    height: 20px;
    border: 1px solid #cccccc;
    border-radius: 50%;

    &.isStock {
        width: 18px;
        height: 18px;
        background-color: #cccccc;
    }

    &:checked {
        position: relative;
        border-color: $darkBlue;
        background-color: $darkBlue;

        &::after {
            content: '';
            position: absolute;
            top: 5px;
            left: 4px;
            display: inline-block;
            width: 9px;
            height: 4px;
            border-bottom: 2px solid $white;
            border-left: 2px solid $white;
            transform: rotate(-45deg);
        }
    }
}
</style>
