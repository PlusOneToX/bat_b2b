
<template>
    <view>
        <view class="drawing_board_box">
            <canvasH5 ref="canvasRef" id="canvas-drag" @dpi="getdpi" :modelHeight="modelHeight" :windowHeight="windowHeight" :hollowOut="hollowOut" :underFrame="underFrame" :rightFrame="rightFrame" :topFrame="topFrame" :leftFrame="leftFrame" :graph="graph" :backgroundImage="defaultImage" :floorImage="backgroundImage" :height="height" :width="width" :proportion="proportion" :enableUndo="true"></canvasH5>
        </view>
        <view>
            <canvas v-show="Iscanvas&&hollowOut==''" style="width: 750rpx; height: 900rpx;z-index: -1;opacity: 0;" canvas-id="firstCanvas" id="firstCanvas"></canvas>
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
            <uni-popup-dialog type="info" cancelText="返回" confirmText="确认提交" title="提交确认" content="您提交的作品内有图片效果较模糊，将影响最终作品打印质量，是否提交" @close="close" @confirm="onExportJSON"></uni-popup-dialog>
        </uni-popup>
        <TabBar @Iscanvas="canvasIs" @addImag="onAddImage" @mapdepot="mapDepot" @onAddtext="setText" @ontags="tags" @onCoverage="setCoverage"></TabBar>
    </view>
</template>
<script>
// 测试链接参数
// distributorId=752&platform=5&price=66.6&materialName=四角防摔壳&materialNo=大四角透明&materialChange=true&modelName=iphone 13 pro&modelNo=苹果13pro&extendProps=XX
import { base64ToPath } from 'image-tools'
import MobileDetect from "mobile-detect";
import { getRenderer } from "../../common/renderer.min.js";
import { getModel } from "../../common/iphone-device.js";
import TabBar from "./components/tabBar";                       //底部选项
import mapDepot from "./components/map_depot";                  //图库
import Word from "./components/word";                           //文字
import Commodity from "./components/commodity";                 //选择商品
import Result from "./components/result";                       //效果说明
import canvasH5 from "./components/canvas-drag/canvas_h5";      //画板
import api from "../../api/allApi";
export default {
    components: {
        TabBar,
        canvasH5,
        mapDepot,
        Word,
        Commodity,
        Result,
    },
    data() {
        return {
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
            clearness: 0,//清晰度
            distributor: '',
            showTemp: '',//获取传递是否显示模板标识符
            hiddenUpload: '',//获取传递是否隐藏上传标识符
            isBack: false,
            modelNo: '',
            sku: "",
            skuId: "",
        }
    },
    beforeRouteEnter(to, from, next) {
        next((vm) => {
            if (from.path === "./glance_over") {
                vm.isBack = true;
            }
        });
    },
    onLoad(parameter) {
        // uni.showLoading({
        //     title: '加载中',
        //     mask: true
        // });
        this.phoneName = parameter.modelName;
        this.modelNo = parameter.modelName;
        this.textureName = parameter.materialName;//材质名称
        this.materialsName = parameter.materialNo;//二级材质名称
        this.price = parameter.price;//价格

        this.skuId = parameter.skuId;
        this.sku = parameter.sku;

        let version = parameter.v;
        // 分销商ID
        this.distributorId = parameter.distributorId;
        // 订单来源
        let orderSource = parameter.orderSource;
        // 平台
        let Platform = parameter.Platform
            ? parameter.Platform
            : orderSource;
        uni.setStorageSync("distributorId", this.distributorId);
        uni.setStorageSync("orderSource", orderSource);
        uni.setStorageSync("Platform", Platform);
        uni.setStorageSync("distributor", this.distributor);

        // 获取传递是否显示模板标识符
        this.showTemp = parameter.showTemp || false;
        // 获取传递是否隐藏上传标识符
        this.hiddenUpload = parameter.hiddenUpload || false;
        // 记录数据埋点
        let hasRecord = uni.getStorageSync("hasRecord");
        if (!hasRecord) {
            this.handleRecordData();
        }

        //#ifdef H5
        let oss = document.createElement("script");
        oss.type = "text/javascript";
        oss.src = "http://gosspublic.alicdn.com/aliyun-oss-sdk-4.4.4.min.js";
        document.body.appendChild(oss);
        // #endif


        this.windowHeight = uni.getStorageSync("nakedHeight") - 300;
        this.modelHeight = this.windowHeight - 200;
        this.ratio = uni.getStorageSync("ratio");
        //首页 tabBar栏定制索引
        if (parameter.image != undefined) {
            this.parameterImage = parameter.image;
        }

        this.GetmodelMaterialRelevance();
        this.getMobile();
    },
    methods: {
        getMobile() {
            //判断数组中是否包含某字符串
            Array.prototype.contains = function (needle) {
                for (i in this) {
                    if (this[i].indexOf(needle) > 0) return i;
                }
                return -1;
            };
            var deviceType = navigator.userAgent; //获取userAgent信息
            var md = new MobileDetect(deviceType); //初始化mobile-detect
            var os = md.os(); //获取系统
            //系统的版本号
            var version = '';
            if (os == 'iOS') {
                //ios系统的处理
                version = md.version('iPhone');
                // 通过 renderer.js 获取苹果机具体的 GPU
                let rendererVal;
                getRenderer((renderer) => {
                    rendererVal = renderer;
                    if (renderer === "Unknown") {
                        var canvas = document.createElement("canvas");
                        if (canvas != null) {
                            var context = canvas.getContext("experimental-webgl");
                            if (context) {
                                var info = context.getExtension("WEBGL_debug_renderer_info");
                                if (info) {
                                    rendererVal = context.getParameter(
                                        info.UNMASKED_RENDERER_WEBGL
                                    );
                                }
                            }
                        }
                    }
                });


                os = md.os();
                console.log("手机名称：", rendererVal);
                console.log("手机名称：", getModel(rendererVal));
                // this.safeForm.model = md.mobile();    //设备名称
            } else if (os == 'AndroidOS') {
                //Android系统的处理
                os = md.os();
                version = md.version('Android');
                var sss = deviceType.split(';');
                var i = sss.contains('Build/');
                if (i > -1) {
                    console.log("手机名称：", sss[i].substring(0, sss[i].indexOf('Build/')));
                }
            }
        },
        // 记录埋点数据
        handleRecordData() {
            let userId = uni.getStorageSync("userId") || "";
            let distributorId = uni.getStorageSync("distributorId") || 752;
            let source = uni.getStorageSync("Platform") || 5;
            this.$api.post(this, api.recordPoint, {
                source: source,
                userId: userId,
                distributorId: distributorId,
                networkType: 1,
            }).then((res) => {
                if (res.success) {
                    uni.setStorageSync("hasRecord", 1);
                }
            });
        },
        close() {
            this.Iscanvas = true;
        },
        orderConfirm() {
            var IsDpi = 0;
            this.$refs.canvasRef.exportFunDpi().then(imgArr => {
                imgArr.forEach(item => {
                    if (item.imaType == 'map' && IsDpi == 0) {
                        let scalew = item.initw / item.w;
                        let scaleh = item.inith / item.h;
                        let scale = scalew > scaleh ? scaleh : scalew;
                        if (scale < 0.3) {
                            IsDpi = 1;
                        }
                    }
                });
                if (IsDpi == 1) {
                    this.$refs.message.open();
                    this.Iscanvas = false;
                }
                else if (imgArr.length == 0 || IsDpi == 0) {
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
            var width = Math.floor((this.width * 5) / this.ratio);
            var height = Math.floor((this.height * 5) / this.ratio);
            uni.getImageInfo({
                src: this.backgroundImage,
                success: res => {
                    var context = uni.createCanvasContext('firstCanvas')
                    context.drawImage(res.path, 80, 15, width, height);
                    context.fill();
                    context.draw();

                    let _this = this
                    let canvasName = 'firstCanvas'
                    let w = width;
                    let h = height;
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
                            for (let i = 0; i < pxData.length; i += 4) {
                                if (
                                    pxData[i] == yz &&
                                    pxData[i + 1] == yz &&
                                    pxData[i + 2] == yz
                                ) {
                                    pxData[i] = 239;
                                    pxData[i + 1] = 239;
                                    pxData[i + 2] = 244;
                                    pxData[i + 3] = 255;//理论上设置成1是不透明的；微信小程序的坑设置255不透明
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
                                                _this.file_img(res.tempFilePath, 2);
                                            },
                                            fail(res) {
                                            }
                                        })
                                    },
                                    fail(res) {
                                    }
                                })
                            }
                        }
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
                    // this.canvasX = (750 - (this.width * 5)) / 2;
                    // this.canvasY = (this.windowHeight - (this.height * 5)) / 2;
                    this.canvasX = (750 - (this.modelHeight / this.proportion)) / 2;
                    this.canvasY = 100;
                    this.modelWidth = this.modelHeight / this.proportion;
                    this.setImage();
                    //清空画布
                    this.onClearCanvas();
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
                    // this.canvasX = (750 - (this.width * 5)) / 2;
                    // this.canvasY = (this.windowHeight - (this.height * 5)) / 2;
                    this.canvasX = (750 - (this.modelHeight / this.proportion)) / 2;
                    this.canvasY = 100;
                    this.modelWidth = this.modelHeight / this.proportion;
                    this.setImage();
                    //清空画布
                    this.onClearCanvas();
                    //首页选择图片进入编辑器参数
                    if (this.parameterImage != '') {
                        this.addImg(this.parameterImage);
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
                this.itemCode = res.data.material.itemCode;
                this.itemId = res.data.material.itemId;
                this.textureName = res.data.material.parentName;
                this.materialsName = res.data.material.name;
                this.modelId = res.data.model.id;//型号id
                this.materialsId = res.data.material.id;//二级材质id
                this.textureId = res.data.material.parentId;//一级材质id
                this.brandName = res.data.model.parentPName;
                //品牌id
                this.brandId = res.data.model.parentPId;
                this.manufactors = res.data.material.manufactor;
                var data = {
                    modelId: this.modelId,
                    materialsId: this.materialsId
                }
                this.modelMaterial1(data);
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
         * 添加测试图片
         */
        onAddTest() {
            this.setData({
                graph: {
                    w: 120,
                    h: 120,
                    type: 'image',
                    url: "/static/assets/images/test.jpg"
                }
            });
        },

        /**
         * 添加图片
         */
        addImg(e) {
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
                        }
                    });
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
                        }
                        //     });
                        // }
                    })
                    // #endif
                },
                fail: res => {
                    console.log(`缓存失败：${res.errMsg}`);
                }
            })
        },
        addImg1(e) {
            uni.getImageInfo({
                src: e,
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
                        }
                    });
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
        onAddImage() {
            uni.chooseImage({
                count: 1, //默认9
                sizeType: ['compressed'], //可以指定是原图还是压缩图，默认二者都有
                success: res => {
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
                                    url: res1.path
                                }
                            });
                        }
                    })

                },
                complete: qw => {
                    this.Iscanvas = true
                }
            });
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
         * 改变文字颜色
         */
        onChangeColor() {
            this.$refs.canvasRef.changColor('blue');
        },

        /**
         * 改变背景颜色
         */
        onChangeBgColor() {
            this.$refs.canvasRef.changeBgColor('yellow');
        },

        /**
         * 改变背景照片
         */
        onChangeBgImage() {
            this.$refs.canvasRef.changeBgImage(this.defaultImage)
        },

        /**
         * 导出当前画布为模板
         */
        onExportJSON() {
            this.Iscanvas = true;
            this.$refs.canvasRef.exportFun().then(imgArr => {
                console.log("图片：", imgArr);
                this.bs = imgArr;
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
                                    '&materialsName=' + this.materialsName
                            });
                        } else {
                            this.hollowOut = img;
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
            let file = new File([_fileBlob], new Date() + '.jpg');
            // 随机命名
            let randomName = that.random_string(6) + "_" + new Date().getTime() + ".jpg";
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
                                        '&sku=' + this.sku +
                                        '&skuId=' + this.skuId
                                });
                            } else {
                                this.hollowOut = img;
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
        onImport() {
            // 有背景
            // let temp_theme = [{"type":"bgColor","color":"yellow"},{"type":"image","url":"../../assets/images/test.jpg","y":98.78423143832424,"x":143.78423143832424,"w":104.43153712335152,"h":104.43153712335152,"rotate":-12.58027482265038,"sourceId":null},{"type":"text","text":"helloworld","color":"blue","fontSize":24.875030530031438,"y":242.56248473498428,"x":119.57012176513672,"w":116.73966979980469,"h":34.87503053003144,"rotate":8.873370699754087}];
            // 无背景
            let temp_theme = [{
                "type": "image",
                "url": "/static/assets/images/test.jpg",
                "y": 103,
                "x": 91,
                "w": 120,
                "h": 120,
                "rotate": 0,
                "sourceId": null
            }, {
                "type": "text",
                "text": "helloworld",
                "color": "red",
                "fontSize": 20,
                "y": 243,
                "x": 97,
                "rotate": 0
            }];
            this.$refs.canvasRef.initByArr(temp_theme);
        },

        onClearCanvas: function (event) {
            let _this = this;

            _this.setData({
                canvasBg: null
            });

            this.$refs.canvasRef.clearCanvas();
        },
        onUndo: function (event) {
            this.$refs.canvasRef.undo();
        }
    }
}
</script>
<style scoped lang="scss">
.default_image {
    position: absolute;
    height: 296rpx;
    z-index: 1;
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
.operation_type_box {
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
            width: 340rpx;
            .phone_name1 {
                overflow: hidden;
                white-space: nowrap;
                text-overflow: ellipsis;
                -o-text-overflow: ellipsis;
                width: 320rpx;
            }
        }
        .phone_price {
            width: 60rpx;
            color: rgba(252, 126, 27, 1);
            font-size: 20rpx;
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