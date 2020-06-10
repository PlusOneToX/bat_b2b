<!--
 * @Author: yaowei
 * @Date: 2019-11-26 10:21:20
 * @LastEditors: yaowei
 * @LastEditTime: 2019-05-24 12:01:32
-->

<template>
  <view
    class="phone"
    :class="isClick || showLoading ? 'noClick' : ''"
    id="phone"
  >
    <view class="content-wrapper">
      <!---手机定制面板-->
      <view
        class="phone-wrapper"
        ref="phoneWrapper"
        :style="{ height: wheight + 'px' }"
      >
        <draw-container
          ref="canvasDraw"
          @submit="submit"
          @clear="clearText"
          @select="selectText"
          @dpi="getdpi"
          @handleClick="handleClick"
          @chooseImg="chooseImg"
          :materialsColorValue="materialsColorValue"
          :manufactor="manufactor"
          :materialsType="materialsType"
          :nw="nw"
          :nh="nh"
          :mw="mw"
          :mh="mh"
          :bgimg="phoneImg"
          :upimg="frameImg"
          :pwidth="phonenWidth"
          :pheight="phonenHeight"
          :wwidth="wwidth"
          :wheight="wheight"
          :pwidth2="phoneWidth"
          :pheight2="phoneHeight"
          :distributor="distributor"
          :curPicType="curPicType"
          :cutInfo="cutInfo"
          :frameValue="frameValue"
          :isAllPlace="isAllPlace"
          :canUpload="uploadValid"
        >
        </draw-container>
      </view>

      <!-- 打印效果 -->
      <view class="print-result" v-show="Number(dpiLevel) !== 0">
        <text>印刷效果</text>
        <text class="result" v-if="Number(dpiLevel) === 1">模糊</text>
        <text class="result" v-if="Number(dpiLevel) === 2">一般</text>
        <text class="result" v-if="Number(dpiLevel) === 3">清晰</text>
      </view>

      <view class="custom-b-wrap" ref="customBtnWrap" id="customBtnWrap">
        <!-- 机型/材质 -->
        <view class="current-mm">
          <view class="mm-item">
            <text>{{ materialsName }}</text>
            <text v-if="colorName" class="color">({{ colorName }})</text>
          </view>
          <view class="mm-item" @click.stop="pickerDialog()">
            <text>{{ brand }}</text>
            <text class="icon"></text>
          </view>
        </view>

        <!-- 文字 -->
        <!-- <li class="btn-item">
					<text class="sprite-icon customized_button_word" @click="openText()"></text>
				</li> -->

        <view class="btn-wrap">
          <view class="btn confirm-btn" @click="openSubmit()">设计完成 </view>
        </view>
      </view>
    </view>

    <!-- 上传 -->
    <view
      class="upload-wrap"
      @click.prevent="chooseImg()"
      v-show="Number(uploadValid) === 1"
    >
      <text class="icon icon_camera"></text>
    </view>

    <!--添加文字-->
    <!-- <div class="text-wrapper" v-show="textDialog">
			<div class="title">
				<span class="line"></span>
				<div class="text">添加文字</div>
				<i class="icon icon-close" @click="close"></i>
			</div>
			<view class="input">
				<u-field label-width="0" v-model="inputtext" placeholder="在此输入文字内容" clearable></u-field>
			</view>
			<div class="select-box">
				<view class="input" @click="toggle">
					<u-field label-width="0" v-model="fontName" placeholder="请选择" right-icon="arrow-down" readonly>
					</u-field>
				</view>
				<div class="select-list" v-show="showSelct">
					<div class="select-item" v-for="item in fontFamily" :key="item.id" @click="handleSelect(item)">
						{{ item.name }}
					</div>
				</div>
			</div>
			<div class="color-box">
				<picker-color :defaultColor="defaultColor" :bottom="0" @callback="getPickerColor" />
			</div>
			<div class="btn-box">
				<div class="btn btn-cancel" @click="removeText">取消</div>
				<div class="btn btn-submit" @click="addText">确定</div>
			</div>
		</div> -->

    <!---素材图库-->
    <div
      class="picture-wrapper"
      :class="{ shadow: showPicture, hidden: !showPicture }"
      :style="'height:' + curHeight + (showPicture ? '' : 'px')"
      @touchstart="picTouchStart"
      @touchmove="picTouchMove"
      @touchend="picTouchEnd"
    >
      <div class="handle-pic" @click="openPic">
        <span class="line line-arrow-up" v-if="!showPicture"></span>
        <span class="line line-arrow-down" v-else></span>
      </div>
      <div class="picture-data">
        <c-picture
          ref="picWrap"
          v-show="picData && picData.length > 0"
          @changeCategory="getPicList"
          @changeSelect="changeSelect"
        ></c-picture>
      </div>
    </div>

    <!---贴图-->
    <!-- <div class="piclist-dialog" v-show="mapDialog">
			<c-picture ref="mapList" v-show="mapData" :picData="mapData" @change="change"></c-picture>
		</div> -->

    <!-- 选择手机型号 -->
    <view class="mask-wrap" v-show="mask" @click="closeMask"></view>
    <view class="picker-wrap phone-picker" :class="{ show: showModelPicker }">
      <c-model
        ref="modelWrap"
        :mobile="modelName"
        @cancel="handleCancelModel"
        @confirm="handleConfirmModel"
      >
      </c-model>
    </view>

    <!-- 选择材质、颜色 -->
    <!-- <view class="picker-wrap material-picker" v-show="showMaterialPicker">
			<u-select title="请选择材质" v-model="showMaterialPicker" :safe-area-inset-bottom="true" mode="mutil-column-auto" :list="materialData"
			 cancel-color="#999999" confirm-color="#333333" @confirm="handleMaterialConfirm"></u-select>
		</view> -->

    <!-- “完成”确认弹出框 -->
    <u-popup mode="center" v-model="submitDialogVisible" border-radius="16">
      <view class="dialog-wrap material-dialog">
        <view class="title">请确认商品定制信息</view>
        <div class="item-content">
          <div class="item-box">
            <span class="left">材质：</span
            ><span class="text"
              >{{ materialsName }} <span v-show="colorName">-</span>
              {{ colorName }}</span
            >
          </div>
          <div class="item-box">
            <span class="left">型号：</span
            ><span class="text">{{ brand }}</span>
          </div>
        </div>
        <view class="btn-wrap">
          <p class="btn" @click="submitDialogVisible = false">返回设计</p>
          <p class="btn btn-confirm" @click="handlePreview">确认设计</p>
        </view>
      </view>
    </u-popup>

    <!-- 上传本地图片-温馨提示 -->
    <u-popup mode="center" v-model="uploadDialog" border-radius="16">
      <view class="dialog-wrap">
        <view class="title">温馨提示</view>
        <div class="item-content">
          <div class="item-box">
            尊敬的用户你好，请保证图片清晰可用，不可上传违反国家法律法规图片，对此造成的后果我方不予承担，印刷制作存在些许误差，介意请勿下单喔。
          </div>
        </div>
        <view class="btn-wrap">
          <p class="btn" @click="uploadDialog = false">返回</p>
          <p class="btn btn-confirm" @click="uploadImage">我知道了</p>
        </view>
      </view>
    </u-popup>

    <!-- 提示 -->
    <u-toast ref="uToast" />

    <!-- 初始化字体 -->
    <!-- <div :style="{ display: 'none', 'font-size': '14px', 'font-family': fontName }"></div> -->

    <!-- loading -->
    <view class="loading-wrap" v-show="showLoading">
      <view class="loading-content">
        <u-loading mode="flower" size="100"></u-loading>
        <text class="loading-msg" v-show="loadingMsg">{{ loadingMsg }}</text>
      </view>
    </view>
  </view>
</template>

<script type="text/ecmascript-6">
import store from "../../store";
// import pickerColor from "@/components/pickerColor/pickerColor.vue";
import { mapState, mapGetters, mapActions, mapMutations } from "vuex";
import {
  getModelList as _getModelList,
  getMaterialList as _getMaterialList,
  getPhoneInfo as _getPhoneInfo,
  getPictureCategory as _getPictureCategory,
  getPictureList as _getPictureList,
  getPrice as _getPrice,
  getFontList as _getFontList,
  getCustom as _getCustom,
} from "../api/allApi";
import CModel from "@/components/cModel";
import CPicture from "@/components/cPicture";
import DrawContainer from "@/components/drawContainer";

let winWidth = 0;
let winHeight = 0;

export default {
  name: "phone",
  data() {
    return {
      title: "定制",
      mobile: "", // 自动获取的机型
      isFlag: 0,
      distributor: "", // 分销商名字
      inputtext: "", // 添加文字
      fontFamily: [], // 字体列表
      fontName: "", // 字体
      family: "", // 当前字体
      defaultColor: "#98C963",
      fontColor: "",
      textDialog: false, // 文字弹框
      picDialog: false, // 图库弹框
      mapDialog: false, // 贴图弹框
      uploadDialog: false, // 上传图片弹框
      submitDialogVisible: false, // 确认弹框
      picCategory: [], // 图库分类
      curCategory: 0, // 当前图库分类id
      picData: [], // 图库列表
      mapData: [], // 贴图列表
      isData: true, // 检查数据是否完善
      count: 30,
      modelinfo: [],
      modelData: [], // 品牌型号列表
      materialData: [], // 材质列表
      anchor: [],
      materialList: [], // 材质列表，原始数据
      brandId: 0, // 品牌ID
      modelId: 0, // 型号ID
      materialsId: "", // 材质ID
      materialNo: "", // 材质编码
      materialsType: 0, // 材质类型
      manufactor: "", // 第三方工厂,上传类型：LHW图片	YC-pdf
      brand: "", // 手机品牌和型号
      brandName: "", // 手机品牌名
      modelName: "", // 手机型号名
      materialsName: "", // 手机材质名
      materialsColorValue: "", // 手机材质颜色值
      materialsImage: "", // 材质图片
      colorName: "", // 手机材质颜色属性名
      picId: 0, // 图片ID
      picName: "",
      picType: 0, // 图片类型
      price: 0,
      spriteArr: [], // 添加的数组
      phoneImg: "", // 手机模型图
      frameImg: "", // 手机框图
      wallpImg: "", // 当前使用的定制图片
      phonenWidth: 0, // 手机宽度
      phonenHeight: 0, // 手机高度
      phoneWidth: 0, // 手机宽度(加宽后)
      phoneHeight: 0, // 手机高度(加宽后)
      isMake: false, // IP图是否已有
      isPrice: true, // 是否有价格
      showSelct: false,
      showModelPicker: false,
      showMaterialPicker: false,
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
      textData: {
        posX: null,
        posY: null,
        posuX: null,
        posuY: null,
        sizeW: 20,
        sizeH: 20,
        rotate: 0,
      },
      wwidth: 0, // 画布宽
      wheight: 0, // 画布高
      index: null, // 当前选中图层
      mw: 0,
      mh: 0,
      nw: 0, // 未加宽的手机尺寸
      nh: 0, // 未加宽的手机尺寸
      pscale: 0,
      dpiValue: 0, // 图片dpi值
      dpiDesc: "", // dpi描述
      dpiRate: 0, // dpi评分
      dpiLevel: 0, // dpi等级
      dpiFlag: false,
      ratio: 1, // 缩放比
      skuNo: 0, // skuNo
      skuName: "",
      curPicType: 2, // 当前素材分类 - 1 自定义 2 IP图库 3 模板
      firstEnter: 1,
      mask: true, // 蒙版
      showPicture: false, // 图库（上滑下滑）
      curHeight: 0, // 当前图库高度
      showLoading: true,
      loadingMsg: "载入中",
      frameValue: "", // 透明间距
      cutInfo: "", // 切图信息
      isAllPlace: 0, // 是否强制铺满血位：1 是，0 否
      uploadValid: 1, // 是否允许用户上传（0 不允许，1 允许）
      // 图库素材移动记录Y值
      picStartY: "",
      picMoveY: "",
      isClick: false, // 是否点击
      underStockFlag: false, // 是否缺货
    };
  },
  computed: {
    ...mapState(["distributorId", "platform", "systemInfo", "taobao"]),
  },
  mounted() {
    // 设置画布宽高
    this.getDrawSize();
    // 初始化数据
    this._getQueryData();
  },
  methods: {
    // 画布宽高
    getDrawSize() {
      // 获取可使用宽高
      let titleBarHeight = this.systemInfo.titleBarHeight || 0;
      let statusBarHeight = this.systemInfo.statusBarHeight || 0;
      winWidth = this.systemInfo.windowWidth || document.body.clientWidth;
      winHeight = this.systemInfo.windowHeight || document.body.clientHeight;
      // canvas 画布高度
      this.wwidth = winWidth;
      this.wheight = 380;

      // 图库初始高度
      // #ifdef H5
      let customBtnH = 117;
      // #endif
      // #ifndef H5
      let customBtnH = 107;
      // #endif

      let curHeight =
        winHeight -
        (this.wheight + titleBarHeight + statusBarHeight + customBtnH);
      if (
        this.systemInfo &&
        (this.systemInfo.app === "TB" || this.systemInfo.app === "TM")
      ) {
        curHeight = winHeight - (this.wheight + customBtnH);
      }
      this.curHeight = parseInt(curHeight);
    },
    _getQueryData() {
      this.distributor = "bat"; // BAT
      uni.setStorage({
        key: "distributor",
        data: this.distributor,
      });
      this.materialsId = Number(store.state.outerId); // 获取淘宝传递材质id
      // this.materialsId = 82; // 获取淘宝传递材质id
      this.getBrand(); // 获取型号
    },
    // 字体选择
    handleSelect(item) {
      this.showSelct = false;
      this.family = item.englishName;
      this.initFont(item, this.family);

      this.fontName = this.getName(this.family);
    },
    // 初始化字体
    initFont(item, fontFamily) {
      uni.loadFontFace({
        family: item.englishName,
        source: "url('" + item.fontFile + "')",
        success: (res) => {
          console.log(res);
        },
        fail: (err) => {
          console.log(err);
        },
      });
    },
    // 根据品牌ID查询品牌名称
    getNameById() {
      let bName, mName;
      let bid = parseInt(this.brandId);
      let sid = parseInt(this.modelId);
      this.modelData.forEach((item) => {
        if (item.value === bid) {
          bName = item.label;
          if (item.children) {
            item.children.forEach((child) => {
              if (child.value === sid) {
                mName = child.label;
              }
            });
          }
        }
      });
      let str = bName + " " + mName;
      return str;
    },
    toggle() {
      this.showSelct = !this.showSelct;
    },
    // 升序
    dataUp(x, y) {
      return x.sequence - y.sequence;
    },
    // 获取所有品牌型号
    getBrand() {
      _getModelList({
        categoryId: 1, // 产品类型id：1 手机壳
        distributorId: this.distributorId,
        materialId: this.materialsId,
        pictureId: this.picId,
        platform: this.platform, // 平台
      }).then((res) => {
        if (res.success) {
          if (res.data && res.data.length > 0) {
            let dataArr = res.data;
            this.modelList = res.data;
            this.$refs.modelWrap.modelList = res.data;

            this.showModelPicker = true;

            // 根据型号查询品牌并显示
            if (this.mobile !== "" && this.mobile !== undefined) {
              this.getModelId(this.mobile);
            } else {
              this.brandId = dataArr[0].modelId;
              this.brandName = dataArr[0].name;
              if (dataArr[0].childrenList.length > 0) {
                this.modelId = dataArr[0].childrenList[0].modelId;
                this.modelName = dataArr[0].childrenList[0].name;

                // 是否缺货
                this.underStockFlag = dataArr[0].childrenList[0].underStockFlag;
              }

              this.brand = this.brandName + " " + this.modelName;
            }

            this.showLoading = false;
            this.getMaterialList();
            if (this.brand === "") {
              this.brand = this.getNameById();
            }
          }
        } else {
          this.showLoading = false;
          this.$refs.uToast.show({
            title: res.errMessage,
            type: "error",
          });
        }
      });
    },
    getBrandId(name) {
      if (this.modelList.length > 0) {
        let bIndex = 0;
        try {
          this.modelList.forEach((brand, index) => {
            if (name.replace(/\s*/g, "").toLowerCase().indexOf(str) !== -1) {
              this.brandId = brand.modelId;
              this.brandName = brand.name;
              bIndex = index;
              try {
                brand.childrenList.forEach((model) => {
                  if (
                    model.name.replace(/\s*/g, "").toLowerCase() ===
                    name.replace(/\s*/g, "").toLowerCase()
                  ) {
                    this.modelId = model.modelId;
                    this.modelName = model.name;

                    // 是否缺货
                    this.underStockFlag = model.underStockFlag;
                    throw new Error("end");
                  } else {
                    this.modelId =
                      this.modelList[bIndex].childrenList[0].modelId;
                    this.modelName =
                      this.modelList[bIndex].childrenList[0].name;

                    // 是否缺货
                    this.underStockFlag =
                      this.modelList[bIndex].childrenList[0].underStockFlag;
                  }
                });
              } catch (e) {}
            } else {
              this.brandId = this.modelList[0].modelId;
              this.brandName = this.modelList[0].name;
              this.modelId = this.modelList[0].childrenList[0].modelId;
              this.modelName = this.modelList[0].childrenList[0].name;

              // 是否缺货
              this.underStockFlag =
                this.modelList[0].childrenList[0].underStockFlag;
            }
          });
        } catch (e) {}
      }
    },
    // 根据手机型号查询型号及品牌
    getModelId(mname) {
      if (this.modelList.length > 0) {
        let bIndex = 0;
        let flag = false;
        try {
          this.modelList.forEach((brand) => {
            if (brand.childrenList.length > 0) {
              brand.childrenList.forEach((model, index) => {
                if (
                  model.name.replace(/\s*/g, "").toLowerCase() ===
                  mname.replace(/\s*/g, "").toLowerCase()
                ) {
                  this.modelId = model.modelId;
                  this.modelName = model.name;
                  this.brandId = brand.modelId;
                  this.brandName = brand.name;

                  // 是否缺货
                  this.underStockFlag = model.underStockFlag;

                  bIndex = index;
                  flag = true;
                  throw new Error("end");
                }
              });
            }
          });
          if (!flag) {
            this.brandId = this.modelList[0].modelId;
            this.brandName = this.modelList[0].name;
            this.modelId = this.modelList[0].childrenList[0].modelId;
            this.modelName = this.modelList[0].childrenList[0].name;

            // 是否缺货
            this.underStockFlag =
              this.modelList[0].childrenList[0].underStockFlag;

            this.$refs.uToast.show({
              title: "未匹配到当前手机型号，已展示默认机型",
              type: "warning",
            });
          }
          this.brand = this.brandName + " " + this.modelName;
        } catch (e) {}
      }
    },
    // 根据型号获取材质列表
    getMaterialList() {
      let flag = false;
      _getMaterialList({
        pictureId: this.picId === 0 ? "" : this.picId,
        categoryId: 1, // 产品类型id：1 手机壳
        distributorId: this.distributorId,
        modelId: this.modelId,
        platform: this.platform, // 平台
      }).then((res) => {
        if (res.success) {
          if (res.data && res.data.length > 0) {
            this.isData = true;
            let dataArr = res.data;
            this.materialList = res.data;
            let materialArr = [];
            this.materialData = [];
            // vue-awesome-picker：数据处理
            dataArr.forEach((item, k) => {
              if (item.childrenList) {
                item.childrenList.forEach((material, r) => {
                  materialArr.push({
                    value: material.materialId,
                    label: material.name,
                    color: material.colour || "",
                    materialNo: material.materialNo,
                    skuNo: material.itemCode,
                    manufactor: material.manufactor,
                    mandatoryCoveredBleedFlag:
                      material.mandatoryCoveredBleedFlag,
                  });
                });
                this.materialData.push({
                  value: item.materialId,
                  label: item.name,
                  color: item.colour || "",
                  manufactor: item.manufactor,
                  children: materialArr,
                });
              } else {
                this.materialData.push({
                  value: item.materialId,
                  label: item.name,
                  color: item.colour || "",
                  manufactor: item.manufactor,
                  children: [
                    {
                      value: item.materialId,
                      label: item.name,
                      color: item.colour || "",
                      materialNo: item.materialNo || "",
                      skuNo: item.itemCode || "",
                      manufactor: item.manufactor,
                      mandatoryCoveredBleedFlag: item.mandatoryCoveredBleedFlag,
                    },
                  ],
                });
              }
              materialArr = [];
            });
            // 添加manufactor参数，及材质二级分类
            if (this.materialsId) {
              this.getMaterialById(this.materialsId);
            } else {
              this.materialsName = dataArr[0].name;
              // 默认第一个
              if (
                dataArr[0].childrenList &&
                dataArr[0].childrenList.length > 0
              ) {
                this.colorName = dataArr[0].childrenList[0].name;
                this.materialsId = dataArr[0].childrenList[0].materialId;
                this.materialNo = dataArr[0].childrenList[0].materialNo;
                this.skuNo = dataArr[0].childrenList[0].itemCode;
                this.materialsColorValue = dataArr[0].childrenList[0].colour;
                this.manufactor = dataArr[0].childrenList[0].manufactor;

                // 是否强制铺满血位：1 是，0 否
                this.isAllPlace =
                  dataArr[0].childrenList[0].mandatoryCoveredBleedFlag;

                // 获取是否允许用户上传
                this.uploadValid = dataArr[0].childrenList[0].allowUploadFlag;
              } else {
                this.materialsId = dataArr[0].materialId;
                this.materialNo = "";
                this.skuNo = "";
                this.colorName = "";
                this.materialsColorValue = dataArr[0].colour;
                this.manufactor = dataArr[0].manufactor;
              }
            }

            this.showLoading = false;
            // 根据手机型号和材质获取手机定制信息
            this.getInfo();

            if (this.materialsId !== 0) {
              // 获取图库分类
              this.$refs.picWrap.curIndex = 0; // 默认选择第一个分类
              this.getPicCategory();
            }
          } else {
            this.showLoading = false;
            this.materialData = [];
            this.picData = [];
            this.$refs.picWrap.picData = [];
            this.materialsId = 0;
            this.materialNo = "";
            this.skuNo = "";
            this.materialsName = "";
            this.colorName = "";
            this.materialsColorValue = "";
            this.materialsImage = "";
            this.manufactor = "";
            this.isData = false;
            this.reset();
            this.$refs.uToast.show({
              title: "暂无关联的材质数据",
              type: "warning",
            });
          }
        } else {
          this.showLoading = false;
          this.isData = false;
          this.$refs.uToast.show({
            title: res.errMessage,
            type: "error",
          });
        }
      });
    },
    // 根据材质ID获取材质信息
    getMaterialById(id) {
      let flag = false;
      this.materialList.forEach((item, index) => {
        if (item.childrenList && item.childrenList.length > 0) {
          item.childrenList.forEach((second) => {
            if (second.materialId === Number(id)) {
              this.materialsName = item.name;
              this.materialNo = second.materialNo;
              this.skuNo = second.itemCode;
              this.colorName = second.name;
              this.materialsColorValue = second.colour;
              this.manufactor = second.manufactor;

              // 是否强制铺满血位：1 是，0 否
              this.isAllPlace = second.mandatoryCoveredBleedFlag;

              // 获取是否允许用户上传
              this.uploadValid = second.allowUploadFlag;

              flag = true;
            }
          });
        } else {
          if (item.materialId === Number(id)) {
            this.materialsName = item.name;
            this.materialNo = "";
            this.skuNo = "";
            this.colorName = "";
            this.materialsColorValue = item.colour;
            this.manufactor = item.manufactor;
            flag = true;
          }
        }
      });
      if (!flag) {
        this.materialsId = this.materialList[0].childrenList
          ? this.materialList[0].childrenList[0].materialId
          : this.materialList[0].materialId;
        this.materialNo = this.materialList[0].childrenList
          ? this.materialList[0].childrenList[0].materialNo
          : "";
        this.skuNo = this.materialList[0].childrenList
          ? this.materialList[0].childrenList[0].itemCode
          : "";
        this.materialsName = this.materialList[0].childrenList
          ? this.materialList[0].childrenList[0].name
          : this.materialList[0].name;
        this.colorName = this.materialList[0].childrenList
          ? this.materialList[0].childrenList[0].name
          : "";
        this.materialsColorValue = this.materialList[0].childrenList
          ? this.materialList[0].childrenList[0].colour
          : this.materialList[0].colour;
        this.manufactor = this.materialList[0].childrenList
          ? this.materialList[0].childrenList[0].manufactor
          : this.materialList[0].manufactor;

        // 是否强制铺满血位：1 是，0 否
        this.isAllPlace =
          this.materialList[0].childrenList[0].mandatoryCoveredBleedFlag;

        // 获取是否允许用户上传
        this.uploadValid = this.materialList[0].childrenList[0].allowUploadFlag;
      }
    },
    // 图库
    changeSelect(item) {
      this.showPicture = false;
      this.getDrawSize();

      // type 1-普通图片 2-IP图片（不可编辑） 3-模板 4-贴纸
      if (this.isPrice) {
        this.imgData.posX = null;
        this.showLoading = true;
        if (item.type === 4) {
          // 贴纸
          this.changeImage(item, item.originImage, item.id, item.type, 2);
        } else {
          this.picId = item.id;
          this.picName = item.pictureName;
          // 素材/模板
          this.changeImage(item, item.originImage, item.id, item.type);
        }
      }
    },
    // 获取图库分类
    getPicCategory() {
      _getPictureCategory({
        distributorId: this.distributorId || 2775,
        materialId: this.materialsId,
        modelId: this.modelId,
      }).then((res) => {
        if (res.success) {
          if (res.data && res.data.length > 0) {
            this.picCategory = res.data;
            this.$refs.picWrap.picCategory = res.data;
            this.curCategory = res.data[0].id;
            this.getPicList(this.curCategory, 1);
          } else {
            this.picCategory = [];
            this.curCategory = 0;
          }
        } else {
          this.showLoading = false;
          this.$refs.uToast.show({
            title: res.errMessage,
            type: "error",
          });
        }
      });
    },
    // 获取图库图片列表
    getPicList(categoryId, page, type) {
      this.curCategory = categoryId;

      if (type) {
        // 切换图库分类
        this.showLoading = true;
      }

      _getPictureList({
        distributorId: this.distributorId || 2775,
        categoryId: categoryId,
        page: page,
        size: 100,
        materialId: this.materialsId,
        modelId: this.modelId,
      }).then((res) => {
        if (res.success) {
          this.picData = [];
          this.$refs.picWrap.picData = [];
          if (res.data.list && res.data.list.length > 0) {
            this.picData = res.data.list;
            this.$refs.picWrap.picData = res.data.list;

            if (this.curPicType === 2 && this.wallpImg) {
              // IP图直接替换
              this.changeImage({}, this.wallpImg, this.picId, this.curPicType);
            } else {
              // 自定义/模板重绘
              this.$refs.canvasDraw.initCanvas();
              this.$refs.canvasDraw.draw();
            }
          } else {
            this.$refs.canvasDraw.dragArr.forEach((item, index) => {
              if (item.picId !== 0) {
                this.$refs.canvasDraw.dragArr.splice(index, 1);
              }
            });
            this.$refs.canvasDraw.initCanvas();
            this.$refs.canvasDraw.draw();
          }
        } else {
          this.$refs.uToast.show({
            title: res.errMessage,
            type: "error",
          });
        }
        this.showLoading = false;
      });
    },
    // 根据手机型号和材质获取手机定制信息
    getInfo() {
      /**
       * 判断材质类型：
       *	74		玻璃壳
       *	82		TPU（亮透彩印软壳）
       *	84		热升华（真彩创意壳）
       *	86		亲肤壳(晶透浮雕壳)
       *	91		防摔壳
       *	108	 炫彩肤感壳-古董白
       *	109	 炫彩肤感壳-经典黑
       */
      switch (Number(this.materialsId)) {
        case 74:
          this.materialsType = 1;
          break;
        case 82:
        case 91:
        case 108:
        case 109:
          this.materialsType = 2;
          break;
        case 84:
          this.materialsType = 3;
          break;
        case 86:
          this.materialsType = 4;
          break;
        default:
          this.materialsType = 2;
          break;
      }

      _getPhoneInfo({
        modelId: this.modelId,
        materialId: this.materialsId,
      })
        .then((res) => {
          if (res.success) {
            if (res.data) {
              this.isData = true;
              this.modelinfo = res.data;
              this.phoneImg = this.modelinfo.floorImage;
              this.frameImg = this.modelinfo.outImage;
              // 手机图长宽比
              this.pscale =
                this.pm2px(this.modelinfo.width) /
                this.pm2px(this.modelinfo.length);
              this.phonenHeight = this.wheight * 0.8;
              this.phonenWidth = this.phonenHeight * this.pscale;

              if (
                Number(this.materialsId) === 74 ||
                Number(this.materialsId) === 84
              ) {
                // 玻璃壳/热升华+2mm
                this.phoneHeight = this.wheight * 0.8 + this.pm2px(2);
                this.phoneWidth = this.phoneHeight * this.pscale;
                let mscale = this.modelinfo.width / this.modelinfo.length;
                if (this.modelinfo.width > this.modelinfo.length) {
                  this.mh = this.modelinfo.length + 2;
                  this.mw = this.mh * mscale;
                } else {
                  this.mw = this.modelinfo.width + 2;
                  this.mh = this.mw / mscale;
                }
                this.nh = this.modelinfo.length;
                this.nw = this.modelinfo.width;
              } else {
                // TPU/亲肤壳
                this.phoneHeight = this.wheight * 0.8;
                this.phoneWidth = this.phoneHeight * this.pscale;
                this.mh = this.nh = this.modelinfo.length;
                this.mw = this.nw = this.modelinfo.width;
              }

              this.showLoading = false;

              // 透明间距
              this.frameValue = JSON.stringify({
                topFrame: this.modelinfo.topFrame || 0,
                underFrame: this.modelinfo.underFrame || 0,
                leftFrame: this.modelinfo.leftFrame || 0,
                rightFrame: this.modelinfo.rightFrame || 0,
              });

              // 判断是否有切图信息（折叠屏）
              if (
                (this.modelinfo.cutType === 1 &&
                  this.modelinfo.slittingHeight) ||
                (this.modelinfo.cutType === 2 &&
                  this.modelinfo.crosscuttingWidth)
              ) {
                this.cutInfo = JSON.stringify({
                  cutType: this.modelinfo.cutType,
                  slittingHeight: this.modelinfo.slittingHeight,
                  slittingWhite: this.modelinfo.slittingWhite,
                  crosscuttingWidth: this.modelinfo.crosscuttingWidth,
                  crosscuttingWhite: this.modelinfo.crosscuttingWhite,
                });
              } else {
                this.cutInfo = "";
              }

              this.getPrice();
            }
          } else {
            this.showLoading = false;
            this.isData = false;
            this.$refs.uToast.show({
              title: "获取定制信息异常，请重新更换机型！",
              type: "warning",
            });
          }
        })
        .catch((error) => {
          this.showLoading = false;
          this.isData = false;
          console.log(error);
        });
    },
    getPrice() {
      _getPrice({
        distributorId: this.distributorId,
        orderSource: this.platform,
        materialId: this.materialsId,
      })
        .then((res) => {
          if (res.success) {
            this.isData = true;
            this.price = res.data || 0;
            this.isPrice = true;
          } else {
            this.showLoading = false;
            this.wallpImg = "";
            this.picId = 0;
            this.isPrice = false;
            this.price = 0;
            this.isData = false;
            this.picData = [];
            this.$refs.picWrap.picData = [];
            this.reset();
          }
        })
        .catch((err) => {
          this.showLoading = false;
          this.isData = false;
          this.reset();
          console.log(err);
        });
    },
    // 获取字体列表
    getFont() {
      _getFontList().then((res) => {
        if (res.success) {
          if (res.data && res.data.length > 0) {
            this.fontFamily = res.data;
            this.family = this.family
              ? this.family
              : this.fontFamily[0].englishName;
            this.initFont(this.fontFamily[0], this.family);
            this.fontName = this.getName(this.family);
            this.fontCoLor = this.defaultColor;
          }
        }
      });
    },
    getdpi(value, ratio) {
      this.dpiValue = value;
      this.ratio = ratio;
      this.getDpiValue(value);
    },
    getDpiValue(value) {
      this.dpiValue = value;
      let dpiRate = Number(value) / 50 >= 5 ? 5 : Math.ceil(Number(value) / 50);
      if (dpiRate >= 5) {
        this.dpiLevel = 3;
        return "cur-print";
      } else if (dpiRate > 2 && dpiRate < 5) {
        this.dpiLevel = 2;
        return "cur-print";
      } else if (dpiRate > 0 && dpiRate <= 2) {
        this.dpiLevel = 1;
        return "cur-print";
      } else {
        this.dpiLevel = 0;
      }
    },
    getScale(width, height) {
      return height / this.phoneHeight > width / this.phoneWidth
        ? width / this.phoneWidth
        : height / this.phoneHeight;
    },
    pm2px(d) {
      // mm转px
      var iswindow = /windows|win32|win64/i.test(navigator.userAgent);
      if (iswindow) {
        return Math.round((d * 96) / 25.4);
      } else {
        return Math.round((d * 72) / 25.4);
      }
    },
    // 图库
    change(item) {
      // type 1-普通图片 2-IP图片（不可编辑） 3-模板 4-贴图
      if (this.isPrice) {
        this.showLoading = true;

        this.imgData.posX = null;
        if (item.type === 4) {
          // 贴图
          this.changeImage(item, item.originImage, item.id, item.type, 2);
        } else {
          this.picId = item.id;
          this.picName = item.name;
          // 素材/模板
          this.changeImage(item, item.originImage, item.id, item.type);
        }
      }
    },
    changeImage(item, url, id, type, cateType) {
      // cateType：1（支持多次上传）例：本地图片，2 贴图
      if (this.materialsId !== 0) {
        this.wallpImg = url;
        if (type !== 4) {
          // 非贴图
          this.picId = id;
        }
        if (type !== 1 || type !== 2) {
          this.picName = "";
        }
        // type 0-Canva 1-普通素材 2-IP图片（不可更改） 3-模板（不可更改） 4-贴图
        this.picType = type;
        Promise.all([this.loadImage(url, type)]).then((img) => {
          this.showLoading = false;
          let imgData = img[0];
          let w = imgData.width;
          let h = imgData.height;
          let diffX = 0;
          let diffY = 0;
          let scale = this.getScale(w, h);
          let centerX, centeruX;
          let centerY, centeruY;
          this.imgData.initW = w / scale;
          this.imgData.initH = h / scale;

          if (
            this.imgData.posX === null ||
            this.picType === 2 ||
            this.picType === 3
          ) {
            // 画布中心点
            centerX = winWidth / 2;
            centerY = this.wheight / 2;
            this.imgData.sizeW = w / scale;
            this.imgData.sizeH = h / scale;
            this.imgData.posX = centerX - w / scale / 2;
            this.imgData.posY = centerY - h / scale / 2;
            // 手机中心点
            centeruX = this.phoneWidth / 2;
            centeruY = this.phoneHeight / 2;
            if (this.materialsType === 1 || this.materialsType === 3) {
              diffX = (this.phoneWidth - this.phonenWidth) / 2;
              diffY = (this.phoneHeight - this.phonenHeight) / 2;
            }
            this.imgData.posuX = centeruX - w / scale / 2 - diffX;
            this.imgData.posuY = centeruY - h / scale / 2 - diffY;
            this.imgData.rotate = 0;
            this.imgData.linew = 0;
            this.imgData.lineh = 0;
          } else {
            centerX = this.imgData.posX + this.imgData.sizeW / 2;
            centerY = this.imgData.posY + this.imgData.sizeH / 2;
            centeruX = this.imgData.posuX + this.imgData.sizeW / 2;
            centeruY = this.imgData.posuY + this.imgData.sizeH / 2;

            // 缩放更换型号再换图片
            this.imgData.sizeW = w / scale / this.ratio;
            this.imgData.sizeH = h / scale / this.ratio;

            this.imgData.posX = centerX - this.imgData.sizeW / 2;
            this.imgData.posY = centerY - this.imgData.sizeH / 2;
            this.imgData.posuX = centeruX - this.imgData.sizeW / 2;
            this.imgData.posuY = centeruY - this.imgData.sizeH / 2;
            this.imgData.linew = 0;
            this.imgData.lineh = 0;
          }

          // 模板 - 计算点击热区
          let hotClick = [];
          if (type === 3 && item !== {}) {
            if (item.templateEditList && item.templateEditList.length > 0) {
              item.templateEditList.forEach((edit, index) => {
                // 计算热区到中心点间距（mm）
                let distanceX =
                  Number(edit.editCenterX) -
                  Number(item.templateCenterX) -
                  Number(edit.width) / 2;
                let distanceY =
                  Number(edit.editCenterY) -
                  Number(item.templateCenterY) -
                  Number(edit.length) / 2;
                let hotPoint = {
                  hotX:
                    ((w / (Number(item.templateCenterX) * 2)) * distanceX) /
                    scale, // x轴
                  hotY:
                    ((h / (Number(item.templateCenterY) * 2)) * distanceY) /
                    scale, // y轴
                  hotW:
                    ((w / (Number(item.templateCenterX) * 2)) *
                      Number(edit.width)) /
                    scale, // 宽
                  hotH:
                    ((h / (Number(item.templateCenterY) * 2)) *
                      Number(edit.length)) /
                    scale, // 高
                  frame: edit.internalEditUrl, // 热区框图
                };
                hotClick.push(hotPoint);
              });
            }
          }

          // 贴图
          if (type === 4) {
            this.imgData.posX = winWidth / 2 - 50;
            this.imgData.posY = this.wheight / 2 - 50;
            this.imgData.posuX = this.phonenWidth / 2 - 50;
            this.imgData.posuY = this.phonenHeight / 2 - 50;
            this.imgData.sizeW = 45;
            this.imgData.sizeH = 45;
            this.imgData.rotate = 0;
          }

          this.$set(imgData, "src", url);
          let imgObj = {
            x: this.imgData.posX,
            y: this.imgData.posY,
            ux: this.imgData.posuX,
            uy: this.imgData.posuY,
            url: imgData,
            width: this.imgData.sizeW,
            height: this.imgData.sizeH,
            initW: this.imgData.initW,
            initH: this.imgData.initH,
            minW: 20,
            minH: 20 / (this.imgData.sizeW / this.imgData.sizeH),
            type: 1,
            picType: this.picType,
            rotate: this.imgData.rotate,
            selected: true,
            index: null,
            cateType: cateType ? cateType : "",
            picId: this.picId ? this.picId : 0,
            hotClick: hotClick,
            scale: scale,
          };

          this.spriteArr = [];
          this.spriteArr.push(imgObj);
          this.$refs.canvasDraw.spriteArr = [];
          this.$refs.canvasDraw.spriteArr.push(imgObj);
        });
      }
    },
    // 加载图片
    loadImage(url, type) {
      return new Promise((resolve, reject) => {
        // #ifndef H5
        uni.getImageInfo({
          src: url,
          success: function (image) {
            resolve(image);
          },
          fail: function (err) {
            reject(err);
          },
        });
        // #endif

        // #ifdef H5
        let img = new Image();
        img.crossOrigin = "anonymous";
        if (url.indexOf("data:") === 0 || type === 0) {
          img.src = url;
        } else {
          img.src = url + "?t=" + new Date().getTime();
        }
        if (img.complete) {
          resolve(img);
        }
        img.onload = () => resolve(img);
        // #endif
      });
    },
    // 添加文字
    addText() {
      if (this.validateText()) {
        if (this.textData.posX === null) {
          // 获取中心坐标
          this.textData.posX = winWidth / 2;
          this.textData.posY = this.wheight / 2;
          this.textData.posuX = this.phonenWidth / 2;
          this.textData.posuY = this.phonenHeight / 2;
          this.textData.sizeW = 20;
          this.textData.sizeH = 20;
          this.textData.rotate = 0;
          this.index = this.index === null ? this.index : null;
        }

        let _self = this;
        // 检查文字是否含有违背法律法规信息
        my.tb.textRiskIdentification({
          data: {
            text: _self.inputtext,
          },
          success(res) {
            if (res.data.result.suggestion === "pass") {
              let textObj = {
                x: _self.textData.posX,
                y: _self.textData.posY,
                ux: _self.textData.posuX,
                uy: _self.textData.posuY,
                url: "",
                width: _self.textData.sizeW,
                height: _self.textData.sizeH,
                initW: _self.textData.sizeW,
                initH: _self.textData.sizeH,
                minW: 10,
                minH: 10,
                fillstyle: _self.fontColor,
                filltext: _self.inputtext,
                fontsize: _self.textData.sizeH,
                fontfamily: _self.family,
                type: 2,
                picType: _self.curPicType,
                rotate: _self.textData.rotate,
                selected: true,
                index: _self.index,
              };
              _self.spriteArr = [];
              _self.spriteArr.push(textObj);
              _self.$refs.canvasDraw.spriteArr = [];
              _self.$refs.canvasDraw.spriteArr.push(textObj);
              _self.textDialog = false;
            } else {
              _self.$refs.uToast.show({
                title: "严禁上传图文含有违背法律法规信息！",
                type: "warning",
              });
              _self.inputtext = "";
            }
          },
        });
      }
    },
    getName(val) {
      let name;
      this.fontFamily.forEach((item) => {
        if (item.englishName === val) {
          name = item.name;
        }
      });
      return name;
    },
    selectText(sprite, index) {
      // 图片，清空文字信息
      if (sprite.type === 1 && index === null) {
        // this.clearText(sprite.type)
        this.imgData.posX = sprite.x;
        this.imgData.posY = sprite.y;
        this.imgData.posuX = sprite.ux;
        this.imgData.posuY = sprite.uy;
        this.imgData.sizeW = sprite.w;
        this.imgData.sizeH = sprite.h;
        this.imgData.rotate = sprite.rotate;
        this.imgData.linew = sprite.linew;
        this.imgData.lineh = sprite.lineh;
        this.textData.posX = null;
      } else if (sprite.type === 2) {
        // 切换至文字
        this.textData.posX = sprite.x;
        this.textData.posY = sprite.y;
        this.textData.posuX = sprite.ux;
        this.textData.posuY = sprite.uy;
        this.textData.sizeW = sprite.w;
        this.textData.sizeH = sprite.h;
        this.family = sprite.fontfamily;
        this.inputtext = sprite.filltext;
        this.fontColor = sprite.fillstyle;
        this.textData.rotate = sprite.rotate;
        this.index = index;
      }
    },
    // 清空文字
    clearText(type) {
      // 1--图片	2--文字 0--未选中
      if (type === 1) {
        this.imgData.posX = null;
        this.wallpImg = "";
        this.dpiValue = 0;
      } else if (type === 2) {
        this.inputtext = "";
        this.family = "";
        this.fontColor = this.defaultColor;
        this.textData.posX = null;
      } else if (type === 0) {
        this.imgData.posX = null;
        this.textData.posX = null;
        this.inputtext = "";
        this.family = "";
        this.dpiValue = 0; /*	*/
        this.fontColor = this.defaultColor;
      } else if (type === 4) {
        this.imgData.posX = null;
        this.textData.posX = null;
        this.inputtext = "";
        this.family = "";
        this.fontColor = this.defaultColor;
        this.dpiValue = 0;
      }
    },
    reset() {
      this.$refs.canvasDraw.reset();
    },
    // 完成定制
    handlePreview() {
      this.submitDialogVisible = false;

      // 遍历canvas画布中所有图层，判断是否有图片
      let hasPic = false;
      if (
        this.$refs.canvasDraw.dragArr &&
        this.$refs.canvasDraw.dragArr.length > 0
      ) {
        let arr = this.$refs.canvasDraw.dragArr;
        arr.forEach((item) => {
          if (Number(item.type) === 1 && Number(item.picType) !== 4) {
            // 有图片
            hasPic = true;
          }
        });
      }

      if (hasPic) {
        if (this.isData) {
          if (this.skuNo === "0" || this.skuNo === 0 || this.skuNo === "") {
            this.$refs.uToast.show({
              title: "信息异常, 请稍后重试！",
              type: "warning",
            });
            return;
          }
          // 判断IP图片是否已经定制
          if (Number(this.picType) === 2) {
            this.isMake = false;
            // 获取是否已生成定制信息
            _getCustom({
              pictureId: this.picId,
              materialId: this.materialsId,
              modelId: this.modelId,
            })
              .then((res) => {
                if (res.success) {
                  if (
                    res.data &&
                    ((this.manufactor === "MK" &&
                      res.data.generateImage.indexOf(".pdf") > -1) ||
                      res.data.generateImage.indexOf(".png") > -1)
                  ) {
                    // pdf 或 png
                    let image = res.data.previewImage;
                    let generateImage = res.data.generateImage;
                    this.isMake = true;
                    this.submit(image, generateImage);
                  } else {
                    this.isMake = false;
                    this.$refs.canvasDraw.make();
                  }
                } else {
                  this.$refs.uToast.show({
                    title: res.msg,
                    type: "error",
                  });
                }
              })
              .catch((err) => {
                console.log(err);
              });
          } else {
            this.$refs.canvasDraw.make();
          }
        } else {
          this.$refs.uToast.show({
            title: "定制信息异常，请更换机型重新获取！",
            type: "warning",
          });
        }
      } else {
        this.$refs.uToast.show({
          title: "请先选择素材！",
          type: "warning",
        });
      }
    },
    // 定制成功并跳转
    submit(image, generateImage) {
      // 保存定制数据
      let materialsValue = this.colorName
        ? this.materialsName + " - " + this.colorName
        : this.materialsName;

      let phoneW = this.nw;
      let phoneH = this.nh;
      if (this.materialsType === 1 || this.materialsType === 3) {
        // 玻璃壳/热升华+2mm
        phoneW = phoneW + 2;
        phoneH = phoneH + 2;
      }
      // 隐藏图层列表
      this.$refs.canvasDraw.showHandleWrap = false;

      let info = {
        image: image,
        pdf: generateImage,
        manufactor: this.manufactor,
        materialId: this.materialsId,
        modelId: this.modelId,
        material: materialsValue,
        model: this.modelName,
        brandId: this.brandId,
        brandName: this.brandName,
        pictureId: this.picId,
        picture: this.picName,
        commodityPrice: this.price,
        commodity: this.skuName,
        commodityCode: this.skuNo,
        commodityCount: 1,
        phoneW: phoneW,
        phoneH: phoneH,
      };
      this.buyNow(info, image);
    },
    // 立即购买
    buyNow(info, image) {
      let modelName = this.modelName.replace("+", "＋"); // 处理+不显示问题
      let that = this;
      my.tb.confirmCustomOrder({
        data: {
          itemId: that.taobao.onLaunchParams.itemId,
          skuId: that.taobao.onLaunchParams.skuId,
          quantity: 1,
          customization: {
            pic: [
              {
                id: 1,
                url: image,
              },
            ],
            text: [
              {
                id: 1,
                content: "机型：" + modelName,
              },
              {
                id: 2,
                content: "材质：" + that.materialsName,
              },
            ],
            info: info,
          },
        },
        success: function (e) {
          that.isClick = false;
        },
        fail: function (e) {
          that.isClick = false;
          my.alert({
            title: "fail",
            content: JSON.stringify(e),
          });
        },
      });
    },
    pickerDialog() {
      if (!this.isClick) {
        this.showModelPicker = true;
        this.mask = true;
      }
    },
    materialDialog() {
      if (this.materialData.length > 1) {
        this.showMaterialPicker = true;
      }
    },
    // 取消（机型选择）
    handleCancelModel() {
      this.showModelPicker = false;
      this.mask = false;
    },
    // 确定（机型选择）
    handleConfirmModel(val) {
      if (!val.underStockFlag) {
        this.showLoading = true;

        this.modelName = val.name;
        this.modelId = val.modelId;
        this.brandId = val.parentId;
        this.brandName = val.parentName;
        this.brand = this.brandName + " " + this.modelName;

        this.showModelPicker = false;
        this.mask = false;

        this.getPicCategory();
        this.getInfo();
      } else {
        this.$refs.uToast.show({
          title: "该机型缺货暂时无法下单",
          type: "warning",
        });
      }
    },
    handleMaterialConfirm(arr) {
      this.showLoading = true;
      this.showMaterialPicker = false;
      // 获取材质
      this.materialsName = arr[0].value;
      this.materialsId = arr[1].label ? arr[1].label : arr[0].label;
      this.materialNo = arr[1].materialNo;
      this.skuNo = arr[1].skuNo;
      this.colorName = arr[1].value === arr[0].value ? "" : arr[1].value;
      this.materialsColorValue = arr[1].color ? arr[1].color : arr[0].color;
      this.manufactor = arr[1].manufactor;

      // 是否强制铺满血位：1 是，0 否
      this.isAllPlace = arr[1].mandatoryCoveredBleedFlag;

      // 获取是否允许用户上传
      this.uploadValid = arr[1].allowUploadFlag;

      this.getInfo();
    },
    removeText() {
      this.clearText();
      this.textDialog = false;
    },
    validateText() {
      if (this.family === "") {
        this.$refs.uToast.show({
          title: "请选择字体",
          type: "warning",
        });
        return false;
      }
      if (this.inputtext === "") {
        this.$refs.uToast.show({
          title: "请输入文字",
          type: "warning",
        });
        return false;
      }
      return true;
    },
    // 打开确认弹框
    openSubmit() {
      if (!(this.showLoading || this.isClick)) {
        // 遍历canvas画布中所有图层，判断是否有图片
        let hasPic = false;
        if (
          this.$refs.canvasDraw.dragArr &&
          this.$refs.canvasDraw.dragArr.length > 0
        ) {
          let arr = this.$refs.canvasDraw.dragArr;
          arr.forEach((item) => {
            if (Number(item.type) === 1 && Number(item.picType) !== 4) {
              // 有图片
              hasPic = true;
            }
          });
        }

        if (hasPic) {
          if (this.underStockFlag) {
            this.$refs.uToast.show({
              title: "该机型缺货暂时无法下单",
              type: "warning",
            });
            return;
          }

          if (this.isData) {
            if (this.skuNo === "0" || this.skuNo === 0 || this.skuNo === "") {
              this.$refs.uToast.show({
                title: "信息异常, 请稍后重试！",
                type: "warning",
              });
              return;
            } else {
              this.submitDialogVisible = true;
            }
          } else {
            this.$refs.uToast.show({
              title: "请先完善数据！",
              type: "warning",
            });
          }
        } else {
          this.$refs.uToast.show({
            title: "请先选择素材！",
            type: "warning",
          });
        }
      }
    },
    // 素材
    openPic() {
      this.showPicture = !this.showPicture;
      if (this.showPicture) {
        this.curHeight = "72%";
      } else {
        this.getDrawSize();
      }
    },
    // 文字
    openText() {
      // 遍历canvas画布中所有图层，判断是否有图片
      let hasPic = false;
      if (
        this.$refs.canvasDraw.dragArr &&
        this.$refs.canvasDraw.dragArr.length > 0
      ) {
        let arr = this.$refs.canvasDraw.dragArr;
        arr.forEach((item) => {
          if (Number(item.type) === 1 && Number(item.picType) !== 4) {
            // 有图片
            hasPic = true;
          }
        });
      }

      if (hasPic) {
        // 获取字体
        if (this.fontFamily.length === 0) {
          this.getFont();
        }

        this.textDialog = true;
        this.clearText();
        this.spriteArr.forEach((item) => {
          if (item.selected && item.type === 0) {
            this.inputtext = item.filltext;
            this.family = item.fontFamily;
            this.fontColor = item.fontColor;
          }
        });
      } else {
        if (Number(this.curPicType) === 1) {
          this.$refs.uToast.show({
            title: "请先选择素材！",
            type: "warning",
          });
        } else if (Number(this.curPicType) === 3) {
          this.$refs.uToast.show({
            title: "请先选择模板！",
            type: "warning",
          });
        }
        if (!this.picDialog) {
          this.openPic(this.curPicType); // 显示素材/模板
        }
      }
    },
    close() {
      this.textDialog = false;
      this.showSelct = false;
    },
    closeMask() {
      this.textDialog = false;
      this.uploadDialog = false;
      this.showModelPicker = false;
      this.picDialog = false;
      this.mapDialog = false;
      this.mask = false;
      this.wwidth = winWidth;
    },
    toback() {
      this.$router.push("/index");
    },
    // 本地上传提示
    chooseImg() {
      // 统计已上传本地图片数量
      let count = 0;
      this.$refs.canvasDraw.dragArr.forEach((item) => {
        if (Number(item.picId) === 0) {
          count++;
        }
      });
      if (count >= 3) {
        this.$refs.uToast.show({
          title: "本地图片最多只支持上传3张哦~",
          type: "warning",
        });
      } else {
        if (this.isFlag === 0) {
          this.uploadDialog = true;
          this.isFlag++;
        } else {
          this.uploadImage();
        }
      }
    },
    // 上传图片
    uploadImage() {
      this.uploadDialog = false;
      let _self = this;
      uni.chooseImage({
        count: 1, // 默认9
        success: (res) => {
          this.showLoading = true;
          let fileTempPath = res.tempFilePaths[0];
          let cloud = this.$cloud;
          cloud.file
            .uploadFile({
              filePath: res.tempFilePaths[0],
              fileType: "image",
              fileName: "images.png",
            })
            .then((file) => {
              // 检查图片是否含有违背法律法规信息
              my.tb.imgRisk({
                data: {
                  cloudFileId: file.fileId,
                },
                success(res) {
                  if (res.data.result.suggestion === "pass") {
                    _self.changeImage({}, fileTempPath, 0, 1);
                  } else {
                    _self.$refs.uToast.show({
                      title: "严禁上传图文含有违背法律法规信息！",
                      type: "warning",
                    });
                    this.showLoading = false;
                  }
                  // 删除图片
                  cloud.file.deleteFile({
                    fileId: file.fileId,
                  });
                },
                fail(err) {
                  this.showLoading = false;
                  // 删除图片
                  cloud.file.deleteFile({
                    fileId: file.fileId,
                  });
                },
              });
            });
        },
        fail: (err) => {
          console.log(err);
        },
      });
    },
    initCanvasDate(picType) {
      let curPicType = 0;
      uni.getStorage({
        key: "curPicType",
        success(e) {
          curPicType = e.data;
        },
      });
      if (picType !== Number(curPicType)) {
        // 关闭所有显示图层
        this.closeMask();
        // 重置画布
        this.picId = 0;
        this.picName = "";
        this.imgData.posX = null;
        this.wallpImg = "";
        this.reset();
        // 清空模板可点击热区数据
        this.$refs.canvasDraw.clickedArea = [];
        this.$refs.canvasDraw.curClicked = "";

        // 打开素材/模板
        this.openPic(picType);

        // 当前图库分类，默认第一项
        this.$refs.picWrap.chooseTab(0);
        this.$refs.picWrap.curCategory = 0;
      }

      this.firstEnter = 0;
      this.curPicType = picType;
    },
    getPickerColor(color) {
      if (color) {
        this.fontColor = color;
      }
    },
    // 图库上滑
    picTouchStart(e) {
      e.preventDefault();
      this.picStartY = e.touches[0].pageY;
    },
    picTouchMove(e) {
      e.preventDefault();
      this.picMoveY = e.touches[0].pageY;
      // 上滑
      if (!this.showPicture) {
        if (
          this.picMoveY < this.picStartY &&
          this.picStartY - this.picMoveY >= 20
        ) {
          this.showPicture = true;
          this.curHeight = "72%";
        }
      }
    },
    picTouchEnd(e) {
      e.preventDefault();
    },
    // 是否点击
    handleClick(isClick) {
      this.isClick = isClick;
    },
  },
  watch: {
    materialsId(value) {
      // 材质ID
      this.materialsId = value;
    },
    modelId(value) {
      // 型号ID
      this.modelId = value;
    },
    wallpImg(value) {
      this.wallpImg = value;
    },
    wheight(value) {
      this.wheight = value;
      this.phoneHeight = this.wheight * 0.8;
      this.phoneWidth = this.phoneHeight * this.pscale;
    },
  },
  components: {
    // "picker-color": pickerColor,
    CModel,
    CPicture,
    DrawContainer,
  },
};
</script>

<style scoped lang="stylus">
$white = #FFFFFF;
$dark = #333333;
$gray = #666666;
$light-gray = #999999;
$main = #FFDA01;
$color-main-lighter = #FFF8CC;
$color-orange = #FF622B;
$color-line-opacity = rgba(216, 216, 216, 0.5);

.noClick {
  pointer-events: none;
}

.sprite-icon {
  position: relative;
  top: 2px;
}

.content-wrapper {
  background-color: $white;
  border-radius: 0 0 16px 16px;
}

.print-result {
  position: fixed;
  top: 150px;
  left: 44px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 1em;
  height: 107px;
  font-size: 11px;
  color: $light-gray;
  line-height: 15px;

  .result {
    color: $color-orange;
  }
}

.custom-b-wrap {
  width: 100%;
  text-align: center;
  box-sizing: border-box;

  .current-mm {
    height: 28px;
    display: flex;
    justify-content: space-between;
    padding: 0 20px;
    font-size: 14px;
    color: $dark;
    text-align: center;

    .mm-item {
      flex: 1;
      position: relative;
      padding-right: 16px;
      line-height: 28px;
      text-align: right;
      white-space: nowrap;
      text-overflow: ellipsis;
      overflow: hidden;

      &:first-child {
        text-align: left;
      }
    }

    .color {
      position: relative;
      top: 50%;
      display: inline-block;
      transform: translateY(-50%) scale(0.8);
    }

    .icon {
      position: absolute;
      top: 50%;
      right: 0;
      width: 14px;
      height: 14px;
      background: url('../../static/common/image/icon_arrow-right.png');
      background-size: 100% 100%;
      transform: translateY(-50%);
    }
  }

  .btn-wrap {
    padding: 6px 15px 28px;

    .btn {
      font-size: 16px;
      color: $dark;
      text-align: center;
      height: 45px;
      line-height: 45px;
      background-color: $main;
      border-radius: 8px;
    }
  }
}

.phone {
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;

  .phone-wrapper {
    position: relative;
    right: 0;
    left: 0;
    overflow: hidden;
  }
}

.text-wrapper {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 15px 15px 20px;
  box-sizing: border-box;
  border-radius: 16px 16px 0 0;
  background-color: $white;
  overflow-y: scroll;
  z-index: 102;
  -webkit-overflow-scrolling: touch;

  &::-webkit-scrollbar {
    display: none;
  }

  .title {
    position: relative;
    text-align: left;

    .line {
      position: absolute;
      top: 0;
      left: -15px;
      width: 3px;
      height: 16px;
      border-radius: 0 50% 50% 0;
      background: $main;
    }

    .text {
      display: inline-block;
      margin-left: 5px;
      font-size: 16px;
      color: $gray;
    }

    .icon {
      display: inline-block;
      font-size: 20px;
      float: right;
    }
  }

  >>>.u-border-bottom:after {
    border: none;
  }

  .input {
    width: 100%;
    margin-top: 30px;
    border-radius: 6px;
    box-sizing: border-box;
    border: none;
    background-color: rgba(155, 155, 155, 0.08);

    >>>input {
      background-color: transparent;
    }
  }

  .select-box {
    position: relative;

    &::after {
      position: absolute;
      top: -6px;
      left: 10px;
      margin-right: 3px;
      border-top-width: 0;
      border-bottom-color: #EBEEF5;
      background-color: red;
    }

    .input {
      margin-top: 10px;
    }

    .select-list {
      position: absolute;
      top: 40px;
      left: 0;
      width: 100%;
      padding: 0 16px;
      border: 1px solid #E4E7ED;
      border-radius: 4px;
      background-color: $white;
      -webkit-box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      -webkit-box-sizing: border-box;
      box-sizing: border-box;
      float: left;
      z-index: 22;

      .select-item {
        height: 36px;
        line-height: 36px;
        font-size: 14px;
        color: $gray;
      }
    }
  }

  .color-box {
    width: 100%;
    margin: 20px 0;
    display: flex;
  }

  .btn-box {
    display: flex;
    flex-wrap: nowrap;
    align-content: flex-center;
    align-items: center;
    justify-content: space-around;

    .btn {
      display: inline-block;
      width: 140px;
      height: 36px;
      line-height: 36px;
      font-size: 14px;
      text-align: center;
      border-radius: 20px;
      color: $white;
    }

    .btn-cancel {
      background-color: rgba(0, 0, 0, 0.2);
    }

    .btn-submit {
      background: $main;
    }
  }
}

.picture-wrapper {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding-top: 10px;
  z-index: 100;
  transition: all 0.3s;

  >>>.no-data-wrap {
    position: relative;
    width: 100%;
    height: 100%;

    .no-data {
      position: relative;
      top: 30px;
      transform: translate(-50%, 0);
    }
  }

  >>>.img-list {
    overflow-y: hidden;
  }

  &.shadow {
    transition: all 0.3s;

    .handle-pic {
      box-shadow: -1px -2px 8px -2px rgba(153, 153, 153, 0.3);
    }

    >>>.img-list {
      overflow-y: scroll;
    }

    >>>.no-data-wrap {
      .no-data {
        position: absolute;
        top: 50%;
        transform: translate(-50%, -50%);
      }
    }
  }

  .picture-data {
    height: 100%;
    background-color: $white;
  }

  .handle-pic {
    position: relative;
    padding: 12px 0 0;
    text-align: center;
    background-color: $white;
    border-radius: 16px 16px 0 0;

    .line {
      position: relative;
      width: 46px;
      height: 22px;
      display: inline-block;

      &::after {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        border-radius: 14px;
        border: 1px solid $color-line-opacity;
      }
    }

    .line-arrow-up {
      background: url('../../static/common/image/icon_down_2.png') no-repeat center;
      background-size: 18px 18px;
      transform: rotate(180deg);
      -webkit-animation: upAndDown 0.8s infinite;
      animation: upAndDown 0.8s infinite;
      -webkit-animation-fill-mode: both;
      animation-fill-mode: both;
      z-index: 10;
    }

    @keyframes upAndDown {
      0%, to {
        background: url('../../static/common/image/icon_down_1.png') no-repeat center;
        background-size: 18px 18px;
      }

      50% {
        background: url('../../static/common/image/icon_down_2.png') no-repeat center;
        background-size: 18px 18px;
      }
    }

    .line-arrow-down {
      background: url('../../static/common/image/icon_down_1.png') no-repeat center;
      background-size: 18px 18px;
    }
  }
}

.msg-dialog {
  display: inline-block;
  width: 90%;
  padding: 18px 0 0;
  text-align: center;
  box-sizing: border-box;
  border-radius: 8px;

  >>>.van-dialog__confirm {
    color: $main;
  }

  .title {
    display: inline-block;
    font-weight: bold;
    color: $color-text;
    font-size: $font-size-large;
    text-align: center;

    .icon {
      display: inline-block;
      margin-top: -2px;
      width: 20px;
      height: 20px;
      bg-image('remind');
      background-size: 20px 20px;
      vertical-align: middle;
    }

    .text {
      vertical-align: middle;
    }
  }

  .info {
    display: block;
    margin: 16px 24px 20px;

    .text {
      display: inline-block;
      margin-top: 4px;
      color: $color-text-b;
      font-size: $font-size-medium;
      line-height: 1.5;
      text-align: justify;
    }

    .error {
      display: inline-block;
      margin-top: 4px;
      color: $color-text-b;
      font-size: $font-size-medium-x;
      line-height: 1.5;
      text-align: justify;
    }
  }
}

.dialog-wrap {
  width: 320px;
  box-sizing: border-box;
  padding: 30px 20px;
  background-color: $white;
  border-radius: 10px;
  overflow: hidden;

  &.material-dialog {
    .item-box {
      padding-left: 20px;
    }
  }

  .title {
    font-size: 16px;
    color: $dark;
    text-align: center;
  }

  .item-content {
    padding: 25px 10px;

    .item-box {
      display: flex;
      font-size: 14px;
      color: $light-gray;
      text-align: left;
      line-height: 1.8;

      .left {
        flex: 2;
      }

      .text {
        flex: 9;
        color: $dark;
      }
    }
  }

  .btn-wrap {
    display: flex;

    .btn {
      flex: 1;
      height: 35px;
      line-height: 35px;
      color: $dark;
      text-align: center;
      background-color: #F4F4F4;
      border-radius: 35px;

      &+.btn {
        margin-left: 15px;
      }

      &.btn-confirm {
        background-color: $main;
      }
    }
  }
}

@media (prefers-color-scheme: dark) {
  .phone {
    background-color: $white;
  }
}

.mask-wrap {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 100;
}

.phone-picker {
  position: fixed;
  bottom: -100%;
  left: 0;
  right: 0;
  background-color: $white;
  border-radius: 16px 16px 0 0;
  transition: all 0.3s;
  z-index: 101;

  &.show {
    bottom: 0;
  }
}

.picker-wrap {
  width: 100%;

  >>>.u-drawer-content {
    border-radius: 8px 8px 0 0;

    .u-select__header__title {
      font-size: 16px;
      text-align: center;
    }
  }
}

.loading-wrap {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 100;
  pointer-events: none;

  .loading-content {
    position: absolute;
    top: 50%;
    left: 50%;
    min-width: 120px;
    min-height: 120px;
    padding: 20px 10px;
    color: $white;
    text-align: center;
    background-color: rgba(0, 0, 0, 0.8);
    border-radius: 8px;
    box-sizing: border-box;
    transform: translate(-50%, -50%);
  }

  .loading-msg {
    display: block;
    margin-top: 10px;
    font-size: 14px;
  }
}

.upload-wrap {
  position: fixed;
  top: 276px;
  right: 0;
  width: 60px;
  height: 46px;
  line-height: 46px;
  background-color: $color-main-lighter;
  border-radius: 46px 0 0 46px;
  z-index: 20;

  .icon_camera {
    position: absolute;
    top: 8px;
    left: 20px;
    display: inline-block;
    width: 30px;
    height: 30px;
    background: url('../../static/common/image/icon_camera.png') no-repeat center;
    background-size: 100% 100%;
  }
}
</style>
<style lang="stylus" rel="stylesheet/stylus">
[v-cloak] {
  display: none !important;
}

.van-overlay {
  background-color: rgba(0, 0, 0, 0.5);
}
</style>
