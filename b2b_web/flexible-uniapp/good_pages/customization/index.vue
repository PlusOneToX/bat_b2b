
<template>
    <view>
        <view class="drawing_board_box">
            <canvasH5 ref="canvasRef" id="canvas-drag" @dpi="getdpi" :original="original" :modelHeight="modelHeight" :windowHeight="windowHeight" :hollowOut="hollowOut" :underFrame="underFrame" :rightFrame="rightFrame" :topFrame="topFrame" :leftFrame="leftFrame" :graph="graph" :backgroundImage="defaultImage" :floorImage="backgroundImage" :height="height" :width="width" :proportion="proportion" :enableUndo="true"></canvasH5>
        </view>
        <view>
            <cover-view v-show="hollowOut==''" style="width: 750rpx; height: 100vh;z-index: 100;position: absolute; top: 0;background-color: #efeff4;"></cover-view>
            <canvas v-show="hollowOut==''" style="width: 750rpx; height: 100vh;z-index: -1;" canvas-id="firstCanvas" id="firstCanvas"></canvas>
        </view>
        <!-- 效果说明 -->
        <Result @Iscanvas="canvasIs" :ResultPopup="ResultPopup"></Result>
        <!-- 图库 -->
        <mapDepot @Iscanvas="canvasIs" @seleImg="addImg" :showPopup="showPopup" :materialsId="materialsId" :modelId="modelId"></mapDepot>
        <!-- 贴图 -->
        <mapDepot @Iscanvas="canvasIs" @seleImg="addImg1" :TagsPopup="TagsPopup" :materialsId="materialsId" :modelId="modelId"></mapDepot>
        <!-- 文字 -->
        <Word @Iscanvas="canvasIs" @SetWord="onAddText" @SetWordImg="onAddTextImg" :TextPopup="TextPopup"></Word>
        <!-- 商品 -->
        <Commodity @Iscanvas="canvasIs" @modelMaterial="modelMaterial" :IsmodelId="modelId" :IstextureId="textureId" :IsbrandId="brandId" :IsphoneName="phoneName" :IsCommodityPopup="CommodityPopup" :IsmaterialsId="materialsId"></Commodity>
        <!-- 预览 -->
        <glanceOver :imgId="pictureId" @updateShoppingNumber="getshoppingNumber" @updateCollectNumber="getcollectNumber" :price="Price" :hollowOut="backgroundImage" :materialsName="materialsName" :itemId="itemId" :itemCode="itemCode" :manufactors="manufactors" :phoneName="phoneName" :textureName="textureName" :brandName="brandName" brandId:="brandId" :materialsId="materialsId" :img="img" :modelId="modelId" :IsglanceOver="IsglanceOver" :height="height" :defaultImage="defaultImage" :proportion="proportion" :underFrame="underFrame" :rightFrame="rightFrame" :topFrame="topFrame" :leftFrame="leftFrame"></glanceOver>

        <view class="production_box" @click="skip('/src/tabBar/works/works')">
            <uni-badge :text="collectNumber" absolute="rightTop">
                <image src="../../static/icons/home_page/production.png"></image>
                <view style="margin-top: -10rpx;">作品</view>
            </uni-badge>
        </view>
        <view class="shopping_box" @click="skip('/src/tabBar/shopping/shopping')">
            <uni-badge :text="shoppingNumber" absolute="rightTop">
                <image src="../../static/icons/home_page/shopping.png"></image>
                <view style="margin-top: -10rpx;">购物</view>
            </uni-badge>
        </view>
        <view class="operation_type_box">
            <view class="intelligibility_box" style="margin-left: 50rpx;" @click="IsResult">
                <image v-if="clearness==0" src="../../static/icons/distinct.png"></image>
                <image v-else-if="clearness==1" src="../../static/icons/distinct1.png"></image>
                <image v-else src="../../static/icons/distinct2.png"></image>
                <view> {{clearness==0?'清晰':clearness==1?'一般':'模糊'}}</view>
            </view>
            <view class="phone_box" @tap="examineModel">
                <view class="phone_name">
                    <view class="phone_name1">{{phoneName}} - {{textureName}} {{materialsName}}</view>
                    <uni-icons type="forward" size="20"></uni-icons>
                </view>
                <view class="phone_price">¥ {{Price}}</view>
            </view>
            <view class="intelligibility_box" @tap="orderConfirm" style="margin-right: 50rpx;">
                <image style="width: 44rpx;height: 44rpx;" src="../../static/icons/submit.png"></image>
                <view>提交</view>
            </view>
        </view>
        <uni-popup ref="message" type="dialog">
            <uni-popup-dialog type="info" cancelText="返回" confirmText="确认提交" title="提交确认" content="您提交的作品内有图片效果较模糊，将影响最终作品打印质量，是否提交" @close="close" @confirm="onExportJSON"> </uni-popup-dialog>
        </uni-popup>
        <uni-popup ref="canva" type="dialog">
            <uni-popup-dialog type="info" cancelText="取消" confirmText="继续" title="温馨提示" content="您选择使用外部提供图片模板，该服务暂不赠送相应主题，是否继续？" @close="close" @confirm="oncanvaJSON"></uni-popup-dialog>
        </uni-popup>
        <TabBar @handleCanva="handleCanva" @Iscanvas="canvasIs" @addImag="onAddImage" @mapdepot="mapDepot" @onAddtext="setText" @ontags="tags" @onCoverage="setCoverage"></TabBar>
    </view>
</template>
<script>
import { base64ToPath } from 'image-tools'
import TabBar from "./components/tabBar";                       //底部选项
import canvasDrag from "./components/canvas-drag/index";        //画板
import mapDepot from "./components/map_depot";                  //图库
import Word from "./components/word";                           //文字
import Commodity from "./components/commodity";                 //选择商品
import Result from "./components/result";                       //效果说明
import canvasH5 from "./components/canvas-drag/canvas_h5";      //画板
import glanceOver from "./components/glance_over";              //预览主键
import api from "../../common/js/allApi";
import { getClientOsVersion } from "common/js/saApi";
export default {
    components: {
        TabBar,
        canvasDrag,
        canvasH5,
        mapDepot,
        Word,
        Commodity,
        Result,
        glanceOver
    },
    data() {
        return {
            img: '',
            hollowOut: '',
            bs: '',
            graph: {},
            canvasBg: "",
            defaultImage: '',
            backgroundImage: '',
            showPopup: false,
            TextPopup: false,
            CommodityPopup: false,
            TagsPopup: false,
            ResultPopup: false,
            imageUrl: '',
            modelId: 0,//型号id
            textureId: 0,//材质id
            materialsId: 82,//二级材质id
            brandId: 0,//品牌id
            brandName: '',//品牌名称
            phoneName: '',//手机名称
            height: 0,//高度
            width: 0,//宽
            proportion: 1,//宽高比例
            textureName: '',
            materialsName: '',
            Price: 0,
            leftFrame: 0,//左边框
            rightFrame: 0,//右边框
            topFrame: 0,//上边框
            underFrame: 0,//边框
            itemCode: '',//货品编码
            itemId: '',//货品id
            manufactors: '',//生产商
            Iscanvas: true,
            parameterImage: '',//首页选择图片进入编辑器参数
            windowHeight: 0,
            canvasX: 0,
            canvasY: 0,
            ratio: 1,
            modelHeight: 1,//模型高度
            modelWidth: 1,//模型宽
            clearness: 0,
            samTitle: '',
            smVersion: "",
            designId: "", // Canva 图片id
            isPic: false,
            wallpImg: '',
            picId: '',
            picType: 1,
            imgData: {
                posX: null,
                posY: null,
                posuX: null,
                posuY: null,
                sizeW: 20,
                sizeH: 20,
                initW: 20,
                initH: 20,
                rotate: 0,
            },
            pictureId: '',
            Istemplates: false,
            darkMode: false,
            IsthemeDetails: false,
            IsglanceOver: false,
            shoppingNumber: 0,//购物车数量
            collectNumber: 0,//收藏数量
            original: 1,//原始屏幕高度
            IsImage: true,//部分机型拍照上传图片会重新调用onLoad函数，加一个参数判断是否重新加载onLoad()
        }
    },
    created() {
        uni.setStorageSync("setText", false);
        this.userNo = uni.getStorageSync("userNo") || "b2bsamsung";
        this.platform = uni.getStorageSync("platform");
        this.distributorId = uni.getStorageSync("distributorId");
        if (this.platform == 7) {
            this.samTitle = "主题图库(送同款手机主题)";
            let userAgent = navigator.userAgent;
            this.smVersion = getClientOsVersion(userAgent); // 23：Android 6.0
            if (this.smVersion && this.smVersion > 23) {
                // Canva JS
                if (this.$store.state.user.firstLoadCanva) {
                    let canvaJS = document.createElement("script");
                    canvaJS.type = "text/javascript";
                    canvaJS.src = "https://sdk.canva.cn/designbutton/v2/api.js";
                    var _self = this;
                    canvaJS.onload = function () {
                        // API 初始化
                        _self.initCanva(_self.userNo);
                    };
                    document.body.appendChild(canvaJS);
                    this.$store.commit("handleNotFirstLoadCanva");
                }
            }

        }
        //窗口发生变化刷新页面
        window.addEventListener('resize', this.onWindowResize);
    },
    onShow() {
        this.getshoppingNumber();
        this.getcollectNumber();
    },
    onLoad(parameter) {
        if (this.IsImage) {
            this.IsImage = false;
            //获取手机系统是否深色模式
            var darkMode = window.matchMedia && window.matchMedia("(prefers-color-scheme: dark)");
            this.darkMode = darkMode.matches;
            this.original = uni.getStorageSync("nakedHeight") - 200;
            this.windowHeight = uni.getStorageSync("nakedHeight") - 200;//500
            if (uni.getStorageSync("nakedHeight") > 1200) {
                this.modelHeight = this.windowHeight - 200;
            } else {
                this.modelHeight = this.windowHeight - 100;
            }
            this.ratio = uni.getStorageSync("ratio");
            //首页 tabBar栏定制索引
            uni.setStorageSync("index", 1)
            if (parameter.image != undefined) {
                this.parameterImage = parameter.image;
            }
            //图片id 下载主题用
            if (parameter.pictureId != undefined) {
                this.pictureId = parameter.pictureId;
            }
            if (parameter.designId != undefined) {
                this.Istemplates = true;
                this.designId = parameter.designId;
            }
            this.phoneName = uni.getStorageSync("modelNoOrName");
            //#ifdef H5
            uni.showLoading({
                title: '加载中',
                mask: true
            });
            let oss = document.createElement("script");
            oss.type = "text/javascript";
            oss.src = "http://gosspublic.alicdn.com/aliyun-oss-sdk-4.4.4.min.js";
            document.body.appendChild(oss);

            if (parameter.parentId != undefined) {
                this.IsthemeDetails = true;
                this.textureId = parameter.parentId;//一级材质id
                this.materialsId = parameter.materialId;//二级材质id
                this.manufactor = parameter.manufactor;//生产商户
            }
            this.GetmodelMaterialRelevance();

            // #endif
        }
    },
    methods: {
        onWindowResize() {
            var that = this;
            const res = uni.getSystemInfoSync();
            var windowWidth = 750 / res.windowWidth;
            var windowHeight = (res.windowHeight * windowWidth) - ((180 + res.safeAreaInsets.bottom + res.statusBarHeight) * windowWidth);
            if (!uni.getStorageSync("setText")) {
                uni.setStorageSync("devicePixelRatio", res.devicePixelRatio);//像素比
                uni.setStorageSync("nakedHeight", (res.windowHeight - (res.safeAreaInsets.bottom + res.statusBarHeight)) * windowWidth);
                uni.setStorageSync("statusBarHeight", res.statusBarHeight);
                uni.setStorageSync("windowHeight", windowHeight);
                uni.setStorageSync("ratio", windowWidth);//比例
                that.$router.go(0);
            }
        },
        //获取购物车数量
        getshoppingNumber() {
            this.$api.get(this, api.getShopcartList).then((res) => {
                this.shoppingNumber = res.data.length > 99 ? '..' : res.data.length;
            })
        },
        // 获取收藏数量
        getcollectNumber() {
            this.$api.get(this, api.getCollectionList, {
                page: 1,
                size: 1,
            }).then((res) => {
                this.collectNumber = res.data.total > 99 ? '..' : res.data.total;
            })
        },
        skip(url) {
            if (this.platform == 7 && url == '/src/tabBar/works/works') {
                uni.navigateTo({
                    url: '../../good_pages/theme/collect',
                });
            } else {
                uni.setStorageSync("index", 0)
                uni.switchTab({
                    url: url
                });
            }
        },
        initCanva(userNo) {
            let _self = this;
            var apiKey = "";
            var host = window.location.host;
            // if (host === "diy.bat.com") {
            apiKey = "_e38j4MhC7jdyXQoJ7Is7GQ8"; // 正式
            // } else {
            // apiKey = "7fyFZMishUEZeNTQ68POjxHv"; // 测试
            // }
            this.$api.get(this, api.getCanvaToken, { userNo: userNo }).then((res) => {
                if (res.success) {
                    // Canva
                    if (window.Canva && window.Canva.DesignButton) {
                        window.Canva.DesignButton.initialize({
                            apiKey: apiKey,
                            autoAuthToken: res.data,
                        }).then(function (api) {
                            // 使用 canvaApi 对象或将其保存下来以供日后使用
                            _self.$store.state.user.canvaApi = api;
                        }).catch((error) => {
                            console.log('错误信息：', error);
                        });
                    }
                }
            });
        },
        oncanvaJSON() {
            // 监听canva iframe元素，点击canva按钮设置block
            let canvaIframe = document.querySelector('[dir="ltr"]');
            canvaIframe.style.display = "block";
            // 判断是否存在 Canva designId
            uni.setStorageSync("setText", true);
            if (this.designId) {
                if (this.Istemplates) {
                    this.handleEditCanva1();
                }
                else {
                    this.handleEditCanva();
                }
            } else {
                this.handleCreateCanva();
            }

        },
        handleEditCanva1() {
            var _self = this;
            // 初始化对象 Canva
            this.$store.state.user.canvaApi.createDesign({
                design: {
                    type: "PhoneCase",
                    template: this.designId
                },
                onDesignOpen: function (options) {
                    // 编辑完成，保存 designId
                    _self.designId = options.designId;
                },
                onDesignPublish: function (options) {
                    // 在设计导出到图片上时触发
                    _self.exportUrl = options.exportUrl;
                    _self.designId = options.designId;
                    uni.setStorageSync("setText", false);
                    _self.changeImage(_self.exportUrl, 999999999, 0);
                    uni.setStorageSync("canvaUrl", _self.exportUrl)
                    let canvaIframe = document.querySelector('[dir="ltr"]');
                    canvaIframe.style.display = "";
                },
                onDesignClose: function () {
                    uni.setStorageSync("setText", false);
                    // 关闭编辑器时触发
                    // 监听canva iframe元素，点击关闭设置为""
                    let canvaIframe = document.querySelector('[dir="ltr"]');
                    canvaIframe.style.display = "";
                    _self.Istemplates = false;
                },
            });
        },
        handleEditCanva() {
            var _self = this;
            // 编辑 Canva
            this.$store.state.user.canvaApi.editDesign({
                design: {
                    type: "PhoneCase",
                    id: _self.designId,
                    // dimensions: {
                    //     units: "px",
                    //     width: 882,
                    //     height: 1887,
                    // },
                },
                onDesignOpen: function (options) {
                    // 编辑完成，保存 designId
                    _self.designId = options.designId;
                },
                onDesignPublish: function (options) {
                    // 在设计导出到图片上时触发
                    _self.exportUrl = options.exportUrl;
                    _self.designId = options.designId;
                    uni.setStorageSync("setText", false);
                    _self.changeImage(_self.exportUrl, 999999999, 0);
                    uni.setStorageSync("canvaUrl", _self.exportUrl)
                    let canvaIframe = document.querySelector('[dir="ltr"]');
                    canvaIframe.style.display = "";
                },
                onDesignClose: function () {
                    // 关闭编辑器时触发
                    // 监听canva iframe元素，点击关闭设置为""
                    let canvaIframe = document.querySelector('[dir="ltr"]');
                    canvaIframe.style.display = "";
                    uni.setStorageSync("setText", false);
                },
            });
        },
        handleCreateCanva() {
            var _self = this;
            // 新建 Canva
            this.$store.state.user.canvaApi.createDesign({
                design: {
                    type: "PhoneCase",
                },
                onDesignOpen: function (options) {
                    // 编辑完成，保存 designId
                    _self.designId = options.designId;
                },
                onDesignPublish: function (options) {
                    // 在设计导出到图片上时触发
                    _self.exportUrl = options.exportUrl;
                    _self.designId = options.designId;

                    _self.changeImage(_self.exportUrl, 999999999, 0);
                    uni.setStorageSync("canvaUrl", _self.exportUrl)
                    let canvaIframe = document.querySelector('[dir="ltr"]');
                    canvaIframe.style.display = "";
                    uni.setStorageSync("setText", false);
                },
                onDesignClose: function () {
                    // 关闭编辑器时触发
                    uni.setStorageSync("setText", false);
                    // 监听canva iframe元素，点击关闭设置为""
                    let canvaIframe = document.querySelector('[dir="ltr"]');
                    canvaIframe.style.display = "";
                },
            }).catch((error) => {
                console.log('Canva错误信息：', error);
            });;
        },
        changeImage(url, id, type) {
            uni.showLoading({
                title: '绘制中',
                mask: true
            });
            this.pictureId = id;
            this.isPic = false;
            this.wallpImg = url;
            this.Istemplates = false;
            uni.getImageInfo({
                src: this.wallpImg,
                success: res => {
                    var proportion = res.height / res.width;
                    var width = this.modelWidth / this.ratio;
                    var height = width * proportion;
                    this.setData({
                        graph: {
                            y: this.canvasY / this.ratio,
                            x: this.canvasX / this.ratio,
                            w: this.modelWidth / this.ratio,
                            h: this.modelHeight / this.ratio,
                            initw: width / this.ratio,
                            inith: height / this.ratio,
                            type: 'image',
                            imaType: 'map',
                            url: res.path,
                            id: id,
                            source: 1
                        }
                    });
                    uni.hideLoading()
                },
                fail: res => {
                    uni.hideLoading()
                    console.log(`缓存失败：${res.errMsg}`);
                }
            })
        },
        close() {
            this.Iscanvas = true;
        },
        orderConfirm() {
            var IsDpi = 0;
            this.$refs.canvasRef.exportFunDpi().then(imgArr => {
                var source = 0;
                imgArr.forEach(item => {
                    if (item.imaType == 'map' && IsDpi == 0) {
                        let scalew = item.initw / item.w;
                        let scaleh = item.inith / item.h;
                        let scale = scalew > scaleh ? scaleh : scalew;
                        if (scale < 0.3) {
                            IsDpi = 1;
                        }
                    }

                    // 图片来源：1:可画图片；2:网络图片：3：本地上传图片
                    if (item.source == 1) {
                        source = 1;
                        this.pictureId = item.id;
                    }
                    if (item.source == 2 && source != 1) {
                        source = 2;
                        this.pictureId = item.id;
                    }
                    if (item.source == 3 && source == 0) {
                        source = 3;
                        this.pictureId = item.id;
                    }
                });
                if (source == 0) {
                    this.pictureId = 0;
                }
                console.log("图片id：", this.pictureId);
                if (IsDpi == 1) {
                    this.$refs.message.open();
                    this.Iscanvas = false;
                }
                else {
                    this.onExportJSON();
                }
            }).catch(e => {
                console.error(e);
            });
        },
        getdpi(item) {
            // 得到缩放倍数
            let scalew = item.initw / item.w;
            let scaleh = item.inith / item.h;
            let scale = scalew > scaleh ? scaleh : scalew;
            if (scale < 0.3) {
                this.clearness = 2;
            } else if (scale < 0.35) {
                this.clearness = 1;
            }
            else {
                this.clearness = 0;
            }
        },
        setImage() {
            this.hollowOut = '';
            var width = Math.floor((this.width * 3) / this.ratio);
            var height = Math.floor((this.height * 3) / this.ratio);
            uni.getImageInfo({
                src: this.backgroundImage,
                success: res => {
                    var context = uni.createCanvasContext('firstCanvas')
                    context.clearRect(80, 15, width, height)
                    context.draw()
                    //先清空画布后再并绘制，否则使用同一个画布会导致重复绘制 
                    context.drawImage(res.path, 80, 15, width, height);
                    context.fill();
                    // context.draw();
                    context.draw(true, () => {
                        let _this = this
                        let canvasName = 'firstCanvas'
                        let w = width;
                        let h = height;
                        let timer1 = setInterval(() => {
                            //提取图像RGBA通道颜色值，每连续四个为一个像素点，分别代表RGBA
                            uni.canvasGetImageData({
                                canvasId: canvasName,
                                x: 80,
                                y: 15,
                                width: w,
                                height: h,
                                success(res) {
                                    let pxData = res.data;
                                    let yz = 0;
                                    var index = 0;
                                    console.log("主题模式：", _this.darkMode);
                                    for (let i = 0; i < pxData.length; i += 4) {
                                        if (pxData[i] == yz &&
                                            pxData[i + 1] == yz &&
                                            pxData[i + 2] == yz) {
                                            if (_this.darkMode) {
                                                pxData[i] = 38;
                                                pxData[i + 1] = 37;
                                                pxData[i + 2] = 42;
                                                pxData[i + 3] = 255;//理论上设置成1是不透明的；微信小程序的坑设置255不透明
                                            }
                                            else {
                                                pxData[i] = 239;
                                                pxData[i + 1] = 239;
                                                pxData[i + 2] = 244;
                                                pxData[i + 3] = 255;//理论上设置成1是不透明的；微信小程序的坑设置255不透明
                                            }
                                        }
                                        else {
                                            pxData[i] = 0;
                                            pxData[i + 1] = 0;
                                            pxData[i + 2] = 0;
                                            pxData[i + 3] = 1;
                                        }
                                        index = i;
                                    }
                                    //先清空画布后再并绘制，否则使用同一个画布会导致重复绘制 
                                    context.clearRect(0, 0, w, h)
                                    context.draw()
                                    //把处理好的像素点放回到画布中
                                    if (index + 4 == pxData.length) {
                                        uni.canvasPutImageData({
                                            canvasId: canvasName,
                                            x: 80,
                                            y: 15,
                                            width: w,
                                            height: h,
                                            data: pxData,
                                            success(res) {
                                                // 如需要提取请执行以下方法
                                                uni.canvasToTempFilePath({
                                                    x: 80,
                                                    y: 15,
                                                    width: w,
                                                    height: h,
                                                    destWidth: w,
                                                    destHeight: h,
                                                    canvasId: canvasName,
                                                    success: (res) => {
                                                        // #ifdef MP-WEIXIN
                                                        _this.file_img(res.tempFilePath, 2);
                                                        // #endif
                                                        // #ifdef H5
                                                        _this.file_img(res.tempFilePath, 2);
                                                        _this.hollowOut = res.tempFilePath;
                                                        console.log('镂空吧：', res.tempFilePath);
                                                        uni.hideLoading()

                                                        // #endif
                                                    },
                                                    fail(res) {
                                                        uni.hideLoading()
                                                        console.log("失败了：", res);
                                                    }
                                                })
                                            },
                                            fail(res) {
                                                uni.hideLoading()
                                                _this.hollowOut = '1';
                                                console.log("失败了2：", res);
                                            }
                                        })
                                    }
                                }
                            })
                            clearTimeout(timer1)
                        }, 500);
                    })
                },
                fail: res => {
                    console.log(`缓存失败：${res.errMsg}`);
                }
            })
        },

        IsResult() {
            this.Iscanvas = false;
            this.ResultPopup = !this.ResultPopup;
        },
        back() {
            uni.switchTab({
                url: '../../src/components/index/index'
            });
        },
        canvasIs(e) {
            this.Iscanvas = e;
        },
        modelMaterial(e) {
            uni.showLoading({
                title: '加载中',
                mask: true
            });
            this.hollowOut = '';
            this.$api.get(this, api.getByModelIdAndMaterialId, {
                modelId: e.modelId,
                materialId: e.materialsId
            }).then((res) => {
                if (res.success) {
                    this.defaultImage = res.data.outImage;//模型图
                    this.backgroundImage = res.data.floorImage;//背景图
                    this.height = res.data.length;//高度
                    this.width = res.data.width;//宽
                    this.proportion = res.data.length / res.data.width;
                    this.leftFrame = res.data.leftFrame || 1;//左边框
                    this.rightFrame = res.data.rightFrame || 1;//右边框
                    this.topFrame = res.data.topFrame || 1;//上边框
                    this.underFrame = res.data.underFrame || 1;//边框
                    if (e.brandName != '') {
                        this.brandName = e.brandName;
                    }
                    this.modelId = e.modelId;
                    this.textureId = e.textureId;
                    this.materialsId = e.materialsId;
                    this.phoneName = e.phoneName;
                    this.textureName = e.textureName;
                    this.materialsName = e.materialsName;
                    this.manufactors = e.manufactors;
                    this.itemCode = e.itemCode;
                    this.itemId = e.itemId;
                    this.Price = e.Price;
                    if (this.modelHeight / this.proportion > 560) {
                        this.windowHeight = this.windowHeight / ((this.modelHeight / this.proportion) / 560)
                    }
                    if (uni.getStorageSync("nakedHeight") > 1200) {
                        this.modelHeight = this.windowHeight - 200;
                    } else {
                        this.modelHeight = this.windowHeight - 100;
                    }

                    this.canvasX = (750 - (this.modelHeight / this.proportion)) / 2;
                    this.canvasY = (this.original - this.modelHeight) / 2;
                    this.modelWidth = this.modelHeight / this.proportion;

                    //清空画布
                    // this.onClearCanvas();
                    // if (this.wallpImg != '') {
                    //     this.changeImage(this.wallpImg, 999999999, 0);
                    // }
                    let timer2 = setInterval(() => {
                        this.setImage();
                        clearTimeout(timer2)
                    }, 500);
                }
                else {
                    uni.showToast({
                        title: res.errMessage,
                        icon: "none",
                        duration: 2000,
                    });
                }
            })
        },
        modelMaterial1(e) {
            this.$api.get(this, api.getByModelIdAndMaterialId, {
                modelId: e.modelId,
                materialId: e.materialsId
            }).then((res) => {
                if (res.success) {
                    this.defaultImage = res.data.outImage;//模型图
                    this.backgroundImage = res.data.floorImage;//背景图
                    this.height = res.data.length;//高度
                    this.width = res.data.width;//宽
                    this.proportion = res.data.length / res.data.width;
                    this.leftFrame = res.data.leftFrame || 1;//左边框
                    this.rightFrame = res.data.rightFrame || 1;//右边框
                    this.topFrame = res.data.topFrame || 1;//上边框
                    this.underFrame = res.data.underFrame || 1;//边框
                    if (this.modelHeight / this.proportion > 560) {
                        this.windowHeight = this.windowHeight / ((this.modelHeight / this.proportion) / 560)
                    }
                    if (uni.getStorageSync("nakedHeight") > 1200) {
                        this.modelHeight = this.windowHeight - 200;
                    } else {
                        this.modelHeight = this.windowHeight - 100;
                    }
                    this.canvasX = (750 - (this.modelHeight / this.proportion)) / 2;
                    this.canvasY = (this.original - this.modelHeight) / 2;;
                    this.modelWidth = this.modelHeight / this.proportion;


                    //清空画布
                    this.onClearCanvas();
                    let timer2 = setInterval(() => {
                        this.setImage();
                        clearTimeout(timer2)
                    }, 500);
                    //首页选择图片进入编辑器参数
                    if (this.parameterImage != '') {
                        this.addImg2(this.parameterImage);
                    }
                    if (this.designId != '') {
                        setTimeout(() => {
                            this.oncanvaJSON();
                        }, 1000);
                    }
                }
                else {
                    uni.showToast({
                        title: res.errMessage,
                        icon: "none",
                        duration: 2000,
                    });
                }
            })
        },
        GetmodelMaterialRelevance() {
            this.$api.get(this, api.defaultmodelMaterialRelevance, {
                modelNoOrName: this.phoneName,
            }).then((res) => {
                if (res.data.findFlag == 0) {
                    this.phoneName = res.data.model.name;
                    uni.showToast({
                        title: '未匹配到当前手机型号，已展示默认机型',
                        icon: "none",
                        duration: 2000,
                    });
                }
                this.phoneName = res.data.model.name;

                this.modelId = res.data.model.id;//型号id
                this.brandName = res.data.model.parentPName;
                //品牌id
                this.brandId = res.data.model.parentPId;
                this.manufactors = res.data.material.manufactor;

                this.itemCode = res.data.material.itemCode;
                this.itemId = res.data.material.itemId;
                this.textureName = res.data.material.parentName;
                this.materialsName = res.data.material.name;

                if (!this.IsthemeDetails) {
                    this.materialsId = res.data.material.id;//二级材质id
                    this.textureId = res.data.material.parentId;//一级材质id
                    var data = {
                        modelId: this.modelId,
                        materialsId: this.materialsId
                    }
                    this.modelMaterial1(data);
                } else {
                    var data = {};
                    if (this.materialsId == 118) {
                        data = {
                            Price: this.Price,
                            brandId: 10013,
                            brandName: this.brandName,
                            itemCode: "8010101000005",
                            itemId: 111,
                            manufactors: "KDS_FK",
                            materialsId: 118,
                            materialsName: "黑色",
                            modelId: 10489,
                            phoneName: this.phoneName,
                            textureId: 117,
                            textureName: "晶透玻璃壳",
                        }
                    }
                    else if (this.materialsId == 114) {
                        data = {
                            Price: this.Price,
                            brandId: 10013,
                            brandName: this.brandName,
                            itemCode: "8010101000008",
                            itemId: 114,
                            manufactors: "KDS_FK",
                            materialsId: 114,
                            materialsName: "薰衣草灰",
                            modelId: 10489,
                            phoneName: this.phoneName,
                            textureId: 113,
                            textureName: "炫彩磨砂壳",
                        }
                    }
                    else if (this.materialsId == 109) {
                        data = {
                            Price: this.Price,
                            brandId: 10013,
                            brandName: this.brandName,
                            itemCode: "8010101000002",
                            itemId: 2,
                            manufactors: "KM-DH",
                            materialsId: 109,
                            materialsName: "经典黑",
                            modelId: 10489,
                            phoneName: this.phoneName,
                            textureId: 107,
                            textureName: "肤感硅胶壳",
                        }
                    } else {
                        data = {
                            Price: this.Price,
                            brandId: 10013,
                            brandName: this.brandName,
                            itemCode: "8010101000002",
                            itemId: 2,
                            manufactors: "KM-DH",
                            materialsId: 108,
                            materialsName: "古董白",
                            modelId: 10489,
                            phoneName: this.phoneName,
                            textureId: 107,
                            textureName: "肤感硅胶壳",
                        }
                    }
                    this.IsthemeDetails = false;
                    this.modelMaterial(data)
                }
                this.getPrice();
            })
        },
        getPrice() {
            this.$api.get(this, api.getPrice, {
                distributorId: uni.getStorageSync("distributorId"),
                orderSource: uni.getStorageSync("platform"), // 平台,
                materialId: this.materialsId,
            }).then((res) => {
                this.Price = res.data;
            })
        },
        examineModel() {
            this.Iscanvas = false;
            this.CommodityPopup = !this.CommodityPopup;
        },
        //打开贴纸
        tags() {
            this.TagsPopup = !this.TagsPopup;
        },
        //打开文字编辑器
        setText() {
            uni.setStorageSync("setText", true);
            this.TextPopup = !this.TextPopup;
        },
        //打开图库
        mapDepot() {
            this.showPopup = !this.showPopup;
        },
        //编辑图层
        setCoverage() {
            this.$refs.canvasRef.setCoverage();
        },
        /**
     
    
        /**
         * 添加图片
         */
        addImg2(e) {
            uni.showLoading({
                title: '绘制中',
                mask: true
            });
            uni.getImageInfo({
                src: e,
                success: res => {
                    var proportion = res.height / res.width;
                    var width = this.modelWidth / this.ratio;
                    var height = width * proportion;
                    console.log("res:", res);
                    // #ifdef H5
                    this.setData({
                        graph: {
                            y: this.canvasY / this.ratio,
                            x: this.canvasX / this.ratio,
                            w: this.modelWidth / this.ratio,
                            h: this.modelHeight / this.ratio,
                            initw: width / this.ratio,
                            inith: height / this.ratio,
                            type: 'image',
                            imaType: 'map',
                            url: res.path,
                            id: this.pictureId,
                            source: 2
                        }
                    });
                    uni.hideLoading()
                    // #endif
                    // #ifdef MP-WEIXIN
                    //图片压缩
                    // uni.compressImage({
                    //     src: res.path,
                    //     quality: 80,
                    //     success: res1 => {
                    this.setData({
                        graph: {
                            y: this.canvasY / this.ratio,
                            x: this.canvasX / this.ratio,
                            w: this.modelWidth / this.ratio,
                            h: this.modelHeight / this.ratio,
                            initw: width / this.ratio,
                            inith: height / this.ratio,
                            type: 'image',
                            imaType: 'map',
                            url: res.path,
                            id: this.pictureId,
                            source: 2
                        }
                    });
                    //     }
                    // })
                    // #endif
                },
                fail: res => {
                    uni.hideLoading()
                    console.log(`缓存失败：${res.errMsg}`);
                }
            })
        },
        addImg(e) {
            uni.showLoading({
                title: '绘制中',
                mask: true
            });
            this.pictureId = e.id;
            uni.getImageInfo({
                src: e.originImage,
                success: res => {
                    var proportion = res.height / res.width;
                    var width = this.modelWidth / this.ratio;
                    var height = width * proportion;
                    console.log("res:", res);
                    // #ifdef H5
                    this.setData({
                        graph: {
                            y: this.canvasY / this.ratio,
                            x: this.canvasX / this.ratio,
                            w: this.modelWidth / this.ratio,
                            h: this.modelHeight / this.ratio,
                            initw: width / this.ratio,
                            inith: height / this.ratio,
                            type: 'image',
                            imaType: 'map',
                            url: res.path,
                            id: this.pictureId,
                            source: 2
                        }
                    });
                    uni.hideLoading()
                    // #endif
                    // #ifdef MP-WEIXIN
                    //图片压缩
                    // uni.compressImage({
                    //     src: res.path,
                    //     quality: 80,
                    //     success: res1 => {
                    this.setData({
                        graph: {
                            y: this.canvasY / this.ratio,
                            x: this.canvasX / this.ratio,
                            w: this.modelWidth / this.ratio,
                            h: this.modelHeight / this.ratio,
                            initw: width / this.ratio,
                            inith: height / this.ratio,
                            type: 'image',
                            imaType: 'map',
                            url: res.path,
                            id: this.pictureId,
                            source: 2
                        }
                    });
                    //     }
                    // })
                    // #endif
                },
                fail: res => {
                    console.log(`缓存失败：${res.errMsg}`);
                }
            })
        },
        addImg1(e) {
            uni.showLoading({
                title: '绘制中',
                mask: true
            });
            this.pictureId = e.id;
            uni.getImageInfo({
                src: e.originImage,
                success: res => {
                    var proportion = res.height / res.width;
                    var width = 100;
                    var height = 100 * proportion;
                    this.setData({
                        graph: {
                            y: this.canvasY * 2,
                            x: this.canvasX,
                            w: width,
                            h: height,
                            // initw: 666,
                            // inith: 666,
                            type: 'image',
                            url: res.path,
                            id: this.pictureId,
                            source: 2
                        }
                    });
                    uni.hideLoading()
                },
                fail: res => {
                    console.log(`缓存失败：${res.errMsg}`);
                }
            })
        },

        onAddTextImg(bodyData) {
            base64ToPath(bodyData).then(path => {
                uni.getImageInfo({
                    src: path,
                    success: res => {
                        var width = res.width / 8;
                        var height = res.height / 8;
                        this.setData({
                            graph: {
                                y: this.canvasY * 2,
                                x: this.canvasX,
                                w: width,
                                h: height,
                                // initw: 666,
                                // inith: 666,
                                type: 'image',
                                url: res.path,
                            }
                        });
                    },
                    fail: res => {
                        console.log(`缓存失败：${res.errMsg}`);
                    }
                })
            })
                .catch(error => {
                    console.error(error)
                })

        },
        //选择模板
        handleCanva() {
            this.$refs.canva.open()
        },
        onAddImage() {

            uni.setStorageSync("setText", true);
            uni.chooseImage({
                count: 1, //默认9
                sizeType: ['compressed'], //可以指定是原图还是压缩图，默认二者都有
                sourceType: ['album'],
                quality: 60,
                success: res => {
                    uni.showLoading({
                        title: '您选择的图片稍大正在绘制中',
                        mask: true
                    });
                    // 判断图片大小
                    var imageSize = res.tempFiles[0].size / 1024 / 1024;//图片大小kb
                    if (imageSize > 10) {
                        uni.hideLoading();
                        uni.showToast({
                            title: '图片过大请重新选择',
                            icon: "none",
                            duration: 1500,
                        });
                    } else {
                        uni.getImageInfo({
                            src: res.tempFilePaths[0],
                            success: res1 => {
                                var proportion = res1.height / res1.width;
                                var width = this.modelWidth / this.ratio;
                                var height = width * proportion;
                                this.setData({
                                    graph: {
                                        y: this.canvasY / this.ratio,
                                        x: this.canvasX / this.ratio,
                                        w: width,
                                        h: height,
                                        initw: width / this.ratio,
                                        inith: height / this.ratio,
                                        type: 'image',
                                        url: res1.path,
                                        id: 0,
                                        source: 3
                                    }
                                });
                                uni.hideLoading()
                                let timer4 = setInterval(() => {
                                    uni.setStorageSync("setText", false);
                                    clearTimeout(timer4)
                                }, 5000);

                            }
                        })
                    }
                },
                fail: res => {
                    uni.showToast({
                        title: '图片格式错误',
                        icon: "none",
                        duration: 2000,
                    });
                    uni.setStorageSync("setText", false);
                },
                complete: qw => {
                    console.log("取消了");
                    this.Iscanvas = true
                }
            });
            //防止h5取消选择照片没有回调 倒计时10秒没回调则放开
            let timer3 = setInterval(() => {
                uni.setStorageSync("setText", false);
                clearTimeout(timer3)
            }, 10000);
        },

        /**
         * 添加文本
         */
        onAddText(text, color, wordStyle) {
            this.setData({
                graph: {
                    "type": "text",
                    "text": text,
                    "color": color,
                    "fontSize": 20,
                    "y": 243,
                    "x": 97,
                    "rotate": 0,
                    "wordStyle": wordStyle
                }
            })
        },
        // 改变文字
        changeText() {
            this.$refs.canvasRef.changeText('改变了');
        },

        /**
         * 导出图片
         */
        onExport() {
            this.$refs.canvasRef.exportJson().then(filePath => {
                console.log(filePath);
            }).catch(e => {
                console.error(e);
            });
        },

        /**
         * 导出当前画布为模板
         */
        onExportJSON() {
            uni.showLoading({
                title: '绘制中',
                mask: true
            });
            this.Iscanvas = true;
            this.$refs.canvasRef.exportFun().then(imgArr => {
                this.file_img(imgArr, 1);
            }).catch(e => {
                console.error(e);
            });
        },
        //oss是不可以直接上传base64的，需要把base64转换成blob,然后把blob转换成file文件的形式
        // 我们在之前拿到了base64文件，现在把他转换成blob形式的：
        // type:1(提交生成生产图) 2(编辑镂空模型图)
        file_img(imgArr, type) {
            // #ifdef H5
            this.file_imgH5(imgArr, type);
            // #endif
            // #ifdef MP-WEIXIN
            var that = this;
            let file = imgArr;
            let randomName = that.random_string(6) + "_" + new Date().getTime() + "." + file.split(".").pop();

            // 随机命名
            that.$api.get(that, api.getOSSSts, { dir: 'diy/' }).then((result) => {
                const index = file.lastIndexOf("\.");
                const imgExtension = file.substring(index + 1, file.length);
                const imgPath = 'diy/h5' + new Date().getTime() + "." + imgExtension;
                uni.uploadFile({
                    url: result.data.hostname, //仅为示例，非真实的接口地址
                    filePath: file,
                    name: 'file',
                    formData: {
                        name: randomName,
                        key: imgPath,
                        policy: result.data.policy,
                        OSSAccessKeyId: result.data.accessKeyId,
                        success_action_status: "200",
                        signature: result.data.signature,
                        'x-oss-security-token': result.data.securityToken
                    },
                    success: (uploadFileRes) => {
                        var img = result.data.hostname + '/' + imgPath;
                        if (type == 1) {
                            uni.hideLoading()
                            uni.navigateTo({
                                url: './glance_over?img=' + img +
                                    '&modelId=' + this.modelId +
                                    '&materialsId=' + this.materialsId +
                                    '&brandId=' + this.brandId +
                                    '&brandName=' + this.brandName +
                                    '&textureName=' + this.textureName +
                                    '&phoneName=' + this.phoneName +
                                    '&price=' + this.Price +
                                    '&manufactors=' + this.manufactors +
                                    '&itemCode=' + this.itemCode +
                                    '&itemId=' + this.itemId +
                                    '&materialsName=' + this.materialsName +
                                    '&hollowOut=' + this.hollowOut
                            });
                        } else {
                            uni.hideLoading()
                            this.hollowOut = img;
                            uni.hideLoading()
                            console.log("镂空模型图：", img);
                        }

                    }, fail: function (res) {
                        console.log("失败了", res)
                    }
                });

            })
            // #endif
        },
        file_imgH5(imgArr, type) {
            var that = this;
            let _fileBlob = that.dataURLtoBlob(imgArr);
            // blob转换成file文件
            let file = new File([_fileBlob], new Date() + '.png');
            // 随机命名
            let randomName = that.random_string(6) + "_" + new Date().getTime() + ".png";

            that.$api.get(that, api.uploadFile).then((result) => {
                let client = new OSS.Wrapper({
                    region: result.data.region,
                    accessKeyId: result.data.accessKeyId,
                    accessKeySecret: result.data.accessKeySecret,
                    stsToken: result.data.securityToken,
                    bucket: result.data.bucketName,
                    endpoint: result.data.endpoint,
                    secure: true,
                });
                client.multipartUpload(result.data.pathName + "/" + randomName, file, {})
                    .then((results) => {
                        return new Promise((resolve, reject) => {
                            var img = result.data.hostname + results.name;
                            // this.defaultImage//模型图
                            // this.backgroundImage//背景图
                            if (type == 1) {
                                // uni.navigateTo({
                                //     url: './glance_over?img=' + img +
                                //         '&modelId=' + this.modelId +
                                //         '&materialsId=' + this.materialsId +
                                //         '&brandId=' + this.brandId +
                                //         '&brandName=' + this.brandName +
                                //         '&textureName=' + this.textureName +
                                //         '&phoneName=' + this.phoneName +
                                //         '&price=' + this.Price +
                                //         '&manufactors=' + this.manufactors +
                                //         '&itemCode=' + this.itemCode +
                                //         '&itemId=' + this.itemId +
                                //         '&materialsName=' + this.materialsName +
                                //         '&hollowOut=' + this.hollowOut
                                // });
                                this.img = img;
                                this.IsglanceOver = !this.IsglanceOver;
                            } else {
                                this.hollowOut = img;
                                uni.hideLoading()
                                console.log("镂空模型图：", img);
                            }
                            resolve(true);
                        });
                    })
                    .catch((err) => {
                        console.log("失败了", err);
                    });
            })
        },
        // base64转换成blob
        dataURLtoBlob(dataurl) {
            var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
                bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
            while (n--) {
                u8arr[n] = bstr.charCodeAt(n);
            }
            return new Blob([u8arr], { type: mime });
        },
        // 随机生成文件名
        random_string(len) {
            len = len || 32;
            let chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678";
            let maxPos = chars.length;
            let pwd = "";
            for (let i = 0; i < len; i++) {
                pwd += chars.charAt(Math.floor(Math.random() * maxPos));
            }
            return pwd;
        },
        onClearCanvas: function (event) {
            let _this = this;
            _this.setData({
                canvasBg: null
            });
            this.$refs.canvasRef.clearCanvas();
        }
    }
}
</script>

<style scoped lang="scss">
.default_image {
    position: absolute;
    height: 296rpx;
    z-index: 1;
    max-width: 200rpx;
}
.btn {
    padding: 10rpx 20rpx;
    float: left;
    margin: 10rpx;
    border: solid 1px #dfdfdf;
    border-radius: 10rpx;
}
.home {
    width: 100vw;
    height: 100vh;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
}
.shopping_box {
    width: 90rpx;
    height: 80rpx;
    border-radius: 0rpx 100rpx 100rpx 0rrpx;
    background-color: rgba(255, 255, 255, 0.5);
    color: rgba(44, 44, 44, 1);
    font-size: 24rpx;
    text-align: center;
    box-shadow: 0rpx 4rpx 12rpx 0rpx rgba(0, 0, 0, 0.4);
    border-radius: 40rpx 0rpx 0rpx 40rpx;
    font-family: Arial;
    z-index: 20;
    position: absolute;
    right: 0;
    top: 50%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-between;
    padding: 5rpx 0;
    box-sizing: border-box;
    image {
        width: 40rpx;
        height: 40rpx;
    }
}
.production_box {
    width: 90rpx;
    height: 80rpx;
    border-radius: 0rpx 100rpx 100rpx 0rrpx;
    background-color: rgba(255, 255, 255, 0.5);
    color: rgba(44, 44, 44, 1);
    font-size: 24rpx;
    text-align: center;
    box-shadow: 0rpx 4rpx 12rpx 0rpx rgba(0, 0, 0, 0.4);
    border-radius: 0rpx 40rpx 40rpx 0rpx;
    font-family: Arial;
    z-index: 20;
    position: absolute;
    left: 0;
    top: 50%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-between;
    padding: 5rpx 0;
    box-sizing: border-box;
    image {
        width: 40rpx;
        height: 40rpx;
    }
}
.operation_type_box {
    z-index: 20;
    width: 750rpx;
    height: 120rpx;
    // background-color: rgba(255, 255, 255, 1);
    color: rgba(16, 16, 16, 1);
    font-size: 28rpx;
    text-align: center;
    font-family: Arial;
    display: flex;
    justify-content: space-between;
    align-items: center;
    position: absolute;
    bottom: 100rpx;
    // padding: 0 50rpx;
    // box-sizing: border-box;
    padding-bottom: constant(safe-area-inset-bottom); // 兼容 IOS<11.2
    padding-bottom: env(safe-area-inset-bottom); // 兼容 IOS>=11.2
    .intelligibility_box {
        color: rgba(44, 44, 44, 1);
        font-size: 24rpx;
        font-family: PingFangSC-regular;
        width: 80rpx;
        height: 80rpx;
        background-color: rgba(255, 255, 255, 0.25);
        box-shadow: 0rpx 4rpx 12rpx 0rpx rgba(0, 0, 0, 0.4);
        border-radius: 40rpx;
        padding-top: 10rpx;
        box-sizing: border-box;
        image {
            width: 40rpx;
            height: 40rpx;
        }
        view {
            margin-top: -14rpx;
            -webkit-transform: scale(0.8);
        }
    }
    .phone_box {
        width: 440rpx;
        height: 80rpx;
        border-radius: 100rpx;
        background-color: rgba(255, 255, 255, 0.5);
        color: rgba(44, 44, 44, 1);
        font-size: 24rpx;
        text-align: center;
        box-shadow: 0rpx 4rpx 12rpx 0rpx rgba(0, 0, 0, 0.4);
        font-family: Arial;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 0 20rpx;
        box-sizing: border-box;
        .phone_name {
            display: flex;
            align-items: center;
            justify-content: space-between;
            width: 330rpx;
            .phone_name1 {
                overflow: hidden;
                white-space: nowrap;
                text-overflow: ellipsis;
                -o-text-overflow: ellipsis;
                width: 320rpx;
            }
        }
        .phone_price {
            width: 70rpx;
            color: rgba(252, 126, 27, 1);
            font-size: 24rpx;
            font-weight: 600;
            text-align: center;
            font-family: PingFangSC-regular;
        }
    }
}
.drawing_board_box {
    width: 750rpx;
    // background-color: #f5f5f5;
    margin: 0 auto;
}
</style>