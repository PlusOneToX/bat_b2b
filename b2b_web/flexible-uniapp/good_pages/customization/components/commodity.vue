<template>
    <uni-popup ref="popup" type="bottom" :safeArea="false" @maskClick="close">
        <view class="sele_img_box" v-if="IsPhone">
            <view class="sele_img_title_box">
                <view>选择商品</view>
                <uni-icons @click="close" type="closeempty" size="26"></uni-icons>
            </view>
            <view style="margin: 0 auto;text-align: center;">
                <image class="image_box" mode="aspectFit" :src="GetImage()"></image>
            </view>
            <view class="commodity_message_box">
                <view class="price_box" v-if="Price!=''&&Price!=undefined">￥{{Price}}</view>
                <view class="commodity_name_box">{{GetName()}}</view>
                <view class="brief_introduction_box">{{Getsynopsis()}}</view>
            </view>
            <view class="model_box">
                <view class="select_model_box">
                    <view class="select_model_title">型号</view>
                    <view class="select_model" @click="selectPhone">{{phoneName}} ></view>
                </view>
                <view class="texture_list_box">
                    <view class="texture_title">材质</view>
                    <view>
                        <scroll-view class="scroll_view_y" :show-scrollbar="false" :scroll-y="true">
                            <view class="texture_box">
                                <view class="texture_meu" @click="selectTexture(item.id)" :class="item.id==textureId?'select':''" v-for="(item,index) in textureList" :key="index" v-if="item.openFlag==1">{{item.name}}</view>
                            </view>
                        </scroll-view>
                        <scroll-view class="scroll_view_y1" :show-scrollbar="false" :scroll-y="true">
                            <view class="texture_box" style="margin-top: 18rpx;">
                                <!-- v-if="item.modelFlag==1" -->
                                <view @click="selectmaterials(item)" v-for="(item,index) in materialsList" :key="index" v-if="item.modelFlag==1">
                                    <uni-badge v-if="item.underStockFlag==1" class="uni-badge-left-margin" text="缺货" absolute="rightTop" size="small" type="info">
                                        <view class="texture_meu noselect">
                                            <text class="box-text">{{item.name}}</text>
                                        </view>
                                    </uni-badge>
                                    <view v-else class="texture_meu" :class="item.id==materialsId?'select':(item.modelFlag==1||item.underStockFlag==0?'':'noselect')">
                                        <text class="box-text">{{item.name}}</text>
                                    </view>
                                </view>
                            </view>
                        </scroll-view>
                    </view>
                </view>
            </view>
            <view v-if="Price!=''&&Price!=undefined" class="select_commodity_box" @click="confirm">确定</view>
            <view v-else class="select_commodity_box1">暂无商品</view>
        </view>
        <view class="sele_img_box" v-else>
            <view class="sele_img_title_box">
                <view>选择型号</view>
                <uni-icons @click="close" type="closeempty" size="26"></uni-icons>
            </view>
            <view class="model_type_box">
                <scroll-view class="model_type_y" :show-scrollbar="false" :scroll-y="true">
                    <view @click="selectBrand(item)" v-for="item in modelList" :key="item" class="model_type_itme" :class="brandId==item.id?'selece_model_type':''">{{item.name}}</view>
                </scroll-view>
                <scroll-view class="model_type_tiem" :show-scrollbar="false" :scroll-y="true">
                    <view>
                        <view class="model_type_meu_box">
                            <view @click="selectmodel(item)" v-for="item in phoneList" :key="item">
                                <view class="series_title" v-if="item.atLastTrademark==0">HUAWEI Mate系列</view>
                                <view v-if="item.atLastTrademark==1" class="model_type_meu">{{item.name}}</view>
                                <view v-else-if="item.atLastTrademark==0" @click="selectmodel(item)" v-for="item in item.models" :key="item" class="model_type_meu">{{item.name}}</view>
                            </view>
                        </view>
                    </view>
                </scroll-view>
            </view>
        </view>
    </uni-popup>
</template>

<script>

import api from "../../../common/js/allApi";
export default {
    props: {
        //手机型号id
        IsmodelId: {
            type: Number,
            default: 0,
        },
        //材质id
        IstextureId: {
            type: Number,
            default: 0,
        },
        //二级材质id
        IsmaterialsId: {
            type: Number,
            default: 0,
        },
        //手机品牌id
        IsbrandId: {
            type: Number,
            default: 0,
        },
        //手机品牌名称
        IsphoneName: {
            type: String,
            default: '',
        },
        IsCommodityPopup: {
            type: Boolean,
            default: false,
        }
    },
    data() {
        return {
            Isspecific: true,//是否选择了二级材质
            IsPhone: true,
            autoplay: true,
            interval: 2000,
            duration: 500,
            textureList: [],//一级材质列表
            materialsList: [],//二级材质列表
            Price: 0,
            modelList: [],//手机品牌列表
            phoneList: [],//手机型号列表
            textureName: '',
            materialsName: '',
            brandName: '',//品牌名称
            itemCode: '',//货品编码
            itemId: '',//货品id
            modelId: this.IsmodelId,
            //材质id
            textureId: this.IstextureId,
            //二级材质id
            materialsId: this.IsmaterialsId,
            //手机品牌id
            brandId: this.IsbrandId,
            //手机品牌名称
            phoneName: this.IsphoneName,
            CommodityPopup: this.IsCommodityPopup
        }
    },
    watch: {
        IsCommodityPopup: {
            handler(newName, oldName) {
                if (oldName != undefined) {
                    this.phoneName = this.IsphoneName;
                    this.modelId = this.IsmodelId;
                    this.textureId = this.IstextureId;
                    this.materialsId = this.IsmaterialsId;
                    this.brandId = this.IsbrandId;
                    this.CommodityPopup = this.IsCommodityPopup;
                    this.IsPhone = true;
                    this.open();
                    this.getmaterialList();
                    this.getPrice();
                }
            },
            immediate: true
        }
    },
    mounted() {

        // this.open();
    },
    methods: {
        //打开型号列表
        selectPhone() {
            this.getmodelList();
            this.getphoneList();
            this.IsPhone = false;
        },
        //返回选择的材质型号
        confirm() {
            if (this.Isspecific) {
                if (this.modelId == 0) {
                    this.modelId = this.IsmodelId;
                }
                var data = {
                    modelId: this.modelId,
                    textureId: this.textureId,
                    materialsId: this.materialsId,
                    brandId: this.brandId,
                    phoneName: this.phoneName,
                    textureName: this.textureName,
                    materialsName: this.materialsName,
                    Price: this.Price,
                    brandName: this.brandName,
                    manufactors: this.manufactors,
                    itemCode: this.itemCode,
                    itemId: this.itemId
                }
                this.$emit("modelMaterial", data);
                this.close();
            }
            else {
                uni.showToast({
                    title: '请选择具体材质',
                    icon: "none",
                    duration: 2000,
                });
            }
        },
        //选择型号
        selectmodel(item) {
            this.modelId = item.id;
            this.phoneName = item.name;
            this.IsPhone = true;
            this.Isspecific = false;
            this.materialsId = '';
            this.defaultTexture(0);
        },
        defaultTexture(subscript) {
            //获取一级材质列表
            this.$api.get(this, api.getNewmaterialList, {
                modelId: this.modelId,
                parentId: 0,//型号父节点ID,查询顶级材质分类传0
                // platform: uni.getStorageSync("platform"), // 平台
            }).then((res1) => {
                this.textureList = res1.data;
                this.textureList.forEach((one, index) => {
                    if (subscript == index && one.openFlag == 1) {
                        this.$api.get(this, api.getNewmaterialList, {
                            modelId: this.modelId,
                            parentId: one.id,//型号父节点ID,查询顶级材质分类传0
                        }).then((res) => {
                            if (this.materialsId == '') {
                                res.data.forEach(item => {
                                    if (item.modelFlag == 1 && this.materialsId == '' && item.underStockFlag == 0) {
                                        this.materialsList = res.data;
                                        this.textureId = one.id;
                                        if (this.textureId == 107) {
                                            this.materialsList = res.data.filter(item => item.modelFlag != 0);
                                        }
                                        this.materialsId = item.id;
                                        this.manufactors = item.manufactor;
                                        this.materialsName = item.name;
                                        this.itemCode = item.itemCode;
                                        this.itemId = item.itemId;
                                        this.Isspecific = true;
                                        this.getPrice();
                                    }
                                })
                                if (this.materialsId == '') {
                                    this.defaultTexture(subscript + 1);
                                }
                                console.log("有id了吗：", this.materialsId);
                            }
                        })
                    }
                    if (subscript == index && one.openFlag != 1) {
                        this.defaultTexture(subscript + 1);
                    }
                })
            })
        },
        // defaultTexture() {
        //     //获取一级材质列表
        //     this.$api.get(this, api.getNewmaterialList, {
        //         modelId: this.modelId,
        //         parentId: 0,//型号父节点ID,查询顶级材质分类传0
        //         // platform: uni.getStorageSync("platform"), // 平台
        //     }).then((res1) => {
        //         this.textureList = res1.data;
        //         // //根据一级材质查询出二级材质
        //         this.textureList.forEach((one, index) => {
        //             if (this.materialsId == '' && one.openFlag == 1) {
        //                 this.$api.get(this, api.getNewmaterialList, {
        //                     modelId: this.modelId,
        //                     parentId: one.id,//型号父节点ID,查询顶级材质分类传0
        //                     // platform: uni.getStorageSync("platform"), // 平台
        //                 }).then((res) => {
        //                     // 循环判断二级材质是否有可选择
        //                     // if (index == 0) {
        //                     //     this.materialsList = res.data;
        //                     // }
        //                     if (this.materialsId == '') {
        //                         res.data.forEach(item => {
        //                             if (item.modelFlag == 1 && this.materialsId == '' && item.underStockFlag == 0) {
        //                                 this.materialsList = res.data;
        //                                 this.textureId = one.id;
        //                                 if (this.textureId == 107) {
        //                                     this.materialsList = res.data.filter(item => item.modelFlag != 0);
        //                                 }
        //                                 this.materialsId = item.id;
        //                                 this.manufactors = item.manufactor;
        //                                 this.materialsName = item.name;
        //                                 this.itemCode = item.itemCode;
        //                                 this.itemId = item.itemId;
        //                                 this.Isspecific = true;
        //                                 this.getPrice();
        //                             }
        //                         })
        //                     }
        //                 })
        //             }
        //         })
        //     })
        // },
        //选择品牌
        selectBrand(item) {
            this.brandId = item.id;
            this.brandName = item.name;
            this.getphoneList();
        },
        //获取手机品牌列表
        getmodelList() {
            this.$api.get(this, api.getphoneList, {
                distributorId: uni.getStorageSync("distributorId"),
                parentId: 0,
                materialId: this.textureId,
                platform: uni.getStorageSync("platform"), // 平台
            }).then((res) => {
                this.modelList = res.data;
            })
        },
        //获取手机具体型号
        getphoneList() {
            this.$api.get(this, api.getphoneList, {
                parentId: this.brandId,
                materialId: this.materialsId,
                platform: uni.getStorageSync("platform"), // 平台
            }).then((res) => {
                this.phoneList = res.data;
            })
        },
        //获取价格
        getPrice() {
            this.$api.get(this, api.getPrice, {
                distributorId: uni.getStorageSync("distributorId"),
                orderSource: uni.getStorageSync("platform"), // 平台,
                materialId: this.materialsId,
            }).then((res) => {
                this.Price = res.data;
            })
        },
        //获取图片
        GetImage() {
            var txt = '';
            if (this.materialsId == '') {
                txt = this.materialsList[0].image;
            }
            else {
                this.materialsList.forEach(item => {
                    if (item.id == this.materialsId) {
                        txt = item.image;
                    }
                });
            }
            return txt;
        },
        //获取名称
        GetName() {
            var txt = '';
            this.textureList.forEach(item => {
                if (item.id == this.textureId) {
                    txt = item.name;
                }
            });
            this.textureName = txt;
            return txt;
        },
        //获取简介
        Getsynopsis() {
            var txt = '';
            this.textureList.forEach(item => {
                if (item.id == this.textureId) {
                    txt = item.description;
                }
            });
            return txt;
        },
        //选择材质
        selectTexture(id) {
            this.textureId = id;
            this.materialsId = '';
            this.Isspecific = false;
            this.Price = ''
            this.Getsynopsis();
            this.GetName();
            this.getsubordinateList();
        },
        //选择二级材质
        selectmaterials(item) {
            if (item.modelFlag == 1 && item.underStockFlag == 0) {
                this.manufactors = item.manufactor;
                this.itemCode = item.itemCode;
                this.itemId = item.itemId;
                this.materialsName = item.name;
                this.materialsId = item.id;
                this.Isspecific = true;
                this.getPrice();
            }
            else if (item.underStockFlag == 1) {
                uni.showToast({
                    title: '该材质缺货',
                    icon: "none",
                    duration: 2000,
                });
                this.Isspecific = false;
            }
            else {
                uni.showToast({
                    title: '材质不适配该机型',
                    icon: "none",
                    duration: 2000,
                });
                this.Isspecific = false;
            }
        },
        //根据手机型号获取材质列表
        getmaterialList() {
            this.$api.get(this, api.getNewmaterialList, {
                modelId: this.modelId,
                parentId: 0,//型号父节点ID,查询顶级材质分类传0
                // platform: uni.getStorageSync("platform"), // 平台
            }).then((res) => {
                this.textureList = res.data;
                this.getsubordinateList();
            })
        },
        //二级材质
        getsubordinateList() {
            this.$api.get(this, api.getNewmaterialList, {
                modelId: this.modelId,
                parentId: this.textureId,//型号父节点ID,查询顶级材质分类传0
                // platform: uni.getStorageSync("platform"), // 平台
            }).then((res) => {
                // 循环判断二级材质是否有可选择
                this.materialsList = res.data;
                this.Isspecific = false;
                this.materialsList.forEach(item => {
                    if (item.modelFlag == 1 && (this.materialsId == item.id || this.materialsId == '') && item.underStockFlag == 0) {
                        this.materialsId = item.id;
                        this.manufactors = item.manufactor;
                        this.materialsName = item.name;
                        this.itemCode = item.itemCode;
                        this.itemId = item.itemId;
                        this.Isspecific = true;
                        this.getPrice();
                    }
                })

            })

        },

        open() {
            this.$refs.popup.open('bottom')
        },
        close() {
            this.$refs.popup.close()
            let timer = setInterval(() => {
                this.$emit("Iscanvas", true);
                clearTimeout(timer)
            }, 200);

        }
    }
}
</script>
<style scoped lang="scss">
.stockout {
    position: absolute;
    // width: 100%;
    // margin-top: -80rpx;
    background-color: red;
}
.select_commodity_box {
    width: 600rpx;
    height: 70rpx;
    line-height: 70rpx;
    border-radius: 50rpx;
    background-color: rgba(0, 118, 164, 1);
    color: #ffffff;
    font-size: 32rpx;
    text-align: center;
    font-family: Arial;
    margin: 0 auto;
    margin-top: 10rpx;
}
.select_commodity_box1 {
    width: 600rpx;
    height: 70rpx;
    line-height: 70rpx;
    border-radius: 50rpx;
    background-color: rgb(181, 181, 181);
    color: #ffffff;
    font-size: 32rpx;
    text-align: center;
    font-family: Arial;
    margin: 0 auto;
    margin-top: 10rpx;
}
.image_box {
    height: 370rpx;
    margin: 20rpx 0;
}
.model_type_box {
    width: 750rpx;
    margin: 0 auto;
    margin-top: 10rpx;
    height: 1100rpx;
    display: flex;
    justify-content: space-between;
    .model_type_tiem {
        width: 480rpx;
        height: 1200rpx;
        .series_title {
            width: 100%;
            color: rgba(16, 16, 16, 1);
            font-size: 28rpx;
            font-weight: 600;
            text-align: left;
            font-family: PingFangSC-regular;
            margin-bottom: 20rpx;
        }
        .model_type_meu_box {
            display: flex;
            flex-wrap: wrap;
            width: 480rpx;
            margin-top: 30rpx;
            .model_type_meu {
                border-radius: 100rpx;
                background-color: #eeeded;
                color: rgba(16, 16, 16, 1);
                font-size: 24rpx;
                text-align: center;
                font-family: Arial;
                padding: 10rpx 20rpx;
                box-sizing: border-box;
                margin-left: 30rpx;
                margin-bottom: 20rpx;
            }
            .selece_model_type {
                background-color: rgba(0, 118, 164, 1);
                color: #ffffff;
            }
            .noselect {
                background-color: rgba(245, 245, 245, 1);
                color: rgba(222, 220, 220, 1);
            }
        }
    }
    .model_type_y {
        width: 250rpx;
        height: 1200rpx;
        border-radius: 0rpx 30rpx 0rpx 0rpx;
        background-color: rgba(247, 247, 247, 1);
        color: rgba(16, 16, 16, 1);
        border: 1rpx solid rgba(247, 247, 247, 1);
        .model_type_itme {
            text-align: center;
            color: rgba(16, 16, 16, 1);
            font-size: 32rpx;
            height: 70rpx;
            font-weight: 600;
            line-height: 70rpx;
            font-family: PingFangSC-regular;
        }
        .selece_model_type {
            background-color: rgba(0, 118, 164, 1);
            color: #ffffff;
        }
    }
}
/deep/ ::-webkit-scrollbar {
    display: none;
    width: 0;
    height: 0;
    background-color: transparent;
}
.scroll_view_y {
    max-height: 200rpx;
}
.scroll_view_y1 {
    max-height: 100rpx;
}
.model_box {
    width: 700rpx;
    height: 400rpx;
    border-radius: 20rpx;
    background-color: rgba(255, 255, 255, 1);
    color: rgba(16, 16, 16, 1);
    border: 1rpx solid rgba(247, 247, 247, 1);
    margin: 0 auto;
    margin-top: 20rpx;
    padding: 0 20rpx;
    box-sizing: border-box;

    .texture_list_box {
        display: flex;
        justify-content: space-between;
        width: 660rpx;
        margin-top: 20rpx;
        .texture_title {
            width: 100rpx;
            color: rgba(16, 16, 16, 1);
            font-size: 28rpx;
            font-weight: 600;
            text-align: left;
            font-family: PingFangSC-regular;
        }
        .texture_box {
            width: 560rpx;
            display: flex;
            flex-wrap: wrap;
            .texture_meu {
                padding: 0 25rpx;
                box-sizing: border-box;
                margin-bottom: 20rpx;
                margin-left: 20rpx;
                height: 50rpx;
                line-height: 50rpx;
                border-radius: 100rpx;
                background-color: #f2f2f2;
                color: rgba(16, 16, 16, 1);
                font-size: 24rpx;
                text-align: center;
                font-family: Arial;
            }
            .select {
                background-color: #0076a4;
                color: rgba(255, 255, 255, 1);
            }
            .noselect {
                background-color: rgba(245, 245, 245, 1);
                color: rgba(222, 220, 220, 1);
            }
        }
    }
    .select_model_box {
        display: flex;
        justify-content: space-between;
        align-items: center;
        height: 70rpx;
        .select_model_title {
            color: rgba(16, 16, 16, 1);
            font-size: 28rpx;
            font-weight: 600;
            text-align: left;
            font-family: PingFangSC-regular;
        }
        .select_model {
            // color: rgba(89, 89, 89, 1);
            color: #0076a4;
            font-size: 28rpx;
            font-family: PingFangSC-regular;
            display: flex;
            align-items: center;
        }
    }
}
.commodity_message_box {
    width: 700rpx;
    height: 280rpx;
    border-radius: 20rpx;
    background-color: rgba(255, 255, 255, 1);
    color: rgba(16, 16, 16, 1);
    border: 1rpx solid rgba(247, 247, 247, 1);
    margin: 0 auto;
    padding: 20rpx;
    box-sizing: border-box;
    .brief_introduction_box {
        height: 100rpx;
        color: rgba(202, 202, 202, 1);
        font-size: 24rpx;
        text-align: left;
        font-family: PingFangSC-regular;
        display: flex;
        align-items: center;
    }
    .commodity_name_box {
        color: rgba(16, 16, 16, 1);
        font-size: 36rpx;
        font-weight: 600;
        text-align: left;
        font-family: PingFangSC-regular;
        margin-top: 30rpx;
    }
    .price_box {
        color: rgba(252, 126, 27, 1);
        font-size: 36rpx;
        font-weight: 600;
        text-align: left;
        font-family: PingFangSC-regular;
    }
}

.sele_img_box {
    width: 750rpx;
    height: 1300rpx;
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
