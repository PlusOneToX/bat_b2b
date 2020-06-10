<!--
 * @Author: yaowei
 * @Date: 2019-05-08 10:20:23
 * @LastEditors: yaowei
 * @LastEditTime: 2019-12-08 16:39:17
-->
<template>
  <div
    class="phone"
    ref="phoneWrapper"
    id="phone"
    :class="isLoading ? 'noclick' : ''"
  >
    <van-icon
      name="arrow-left"
      class="back"
      @click="handleBack"
      v-show="!isMiniProgram && Number(distributorId) !== 6175"
    />
    <div class="phone-t" ref="phoneT">
      <!-- 手机定制面板 -->
      <div class="phone-wrapper" :style="{ height: wheight + 'px' }">
        <draw-container
          ref="mcanvas"
          @submit="submit"
          @clear="clearText"
          @select="selectText"
          @dpi="getdpi"
          @handleLoading="handleLoading"
          @chooseImg="chooseImg"
          :materialsColorValue="materialsColorValue"
          :spriteArr="spriteArr"
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
          :curPicType="curPicType"
          :canUpload="uploadValid"
        >
        </draw-container>
      </div>

      <!-- 印刷效果 -->
      <div
        class="print-result"
        v-show="dpiLevel"
        :class="[getDpiValue(dpiValue)]"
      >
        <span>印刷效果</span>
        <span class="result" v-show="dpiLevel === 1">模糊</span>
        <span class="result" v-show="dpiLevel === 2">一般</span>
        <span class="result" v-show="dpiLevel === 3">清晰</span>
      </div>

      <!-- 多图层/其他操作按钮 -->
      <div class="handle-wrap">
        <div
          class="handle-list"
          :class="[
            { more: showMoreHandle && Number(distributorId) !== 6175 },
            { miniMore: showMoreHandle && Number(distributorId) === 6175 },
          ]"
        >
          <span
            class="sprite-icon icon-handle"
            v-show="!showMoreHandle"
            @click="showMoreHandle = true"
          ></span>

          <div class="more-handle" v-show="showMoreHandle">
            <van-icon name="arrow" @click="showMoreHandle = false" />
            <div
              class="handle-item"
              :class="{ disable: Number(uploadValid) !== 1 }"
              @click="chooseImg"
            >
              <span class="sprite-icon icon-picture"></span>
              <input
                type="file"
                style="display: none; height: 0; width: 0"
                accept="image/*"
                id="picfile"
                ref="file"
                @change="picUpload($event)"
              />
              <p>传图</p>
            </div>
            <div
              class="handle-item"
              @click="openMap"
              v-if="Number(distributorId) !== 6175"
            >
              <span class="sprite-icon icon-paster"></span>
              <p>贴纸</p>
            </div>
            <div
              class="handle-item"
              :class="{ disable: Number(materialId) === 86 }"
              @click="openText"
            >
              <span class="sprite-icon icon-text"></span>
              <p>文字</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 材质/型号 -->
      <ul class="material-model">
        <li v-if="!canSelectMaterial">
          {{ materialName }}
          <span class="color" v-show="colorName">{{
            "(" + colorName + ")"
          }}</span>
        </li>
        <li @click="handleMaterial" v-else>
          {{ materialName }}
          <span class="color" v-show="colorName">{{
            "(" + colorName + ")"
          }}</span>
          <van-icon name="arrow" />
        </li>
        <li @click="handleModel">
          <p>{{ modelName }}</p>
          <van-icon name="arrow" />
        </li>
      </ul>

      <div
        class="btn-wrap"
        v-if="platform === 'GF60006' || platform === 'GF60007' || platform === 'GF60008'"
      >
        <p
          class="btn confirm-btn"
          :class="{ 'default-btn': underStockFlag }"
          @click="handleSubmit"
        >
          <span class="price" v-show="price"
            >( <i>¥</i>{{ Number(price).toFixed(2) }} )</span
          >
          加入购物车
        </p>
      </div>
      <div class="btn-wrap" v-else-if="platform === 'GF60005'">
        <p
          class="btn confirm-btn"
          :class="{ 'default-btn': underStockFlag }"
          @click="handleSubmit"
        >
          <span class="price" v-show="price"
            >( <i>¥</i>{{ Number(price).toFixed(2) }} )</span
          >
          设计完成
        </p>
      </div>
      <div class="btn-wrap" v-else>
        <p
          class="btn confirm-btn"
          :class="{ 'default-btn': underStockFlag }"
          @click="handleSubmit"
        >
          兑换
        </p>
      </div>
    </div>

    <!-- 材质 -->
    <transition name="fadeDialog">
      <div class="material-wrapper" v-show="showMaterial">
        <div class="material">
          <h6 class="title">
            <span class="title-l" @click="handleCancelMaterial">取消</span>
            <span class="title-c">选择材质</span>
            <span class="title-r" @click="handleConfirmMaterial">确定</span>
          </h6>

          <div class="material-scroll">
            <!-- 材质介绍 -->
            <div class="material-intro">
              <MaterialDetail :materialInfo="detailIntro"></MaterialDetail>
            </div>

            <!-- 材质/颜色 -->
            <Material
              class="material-wrap"
              :materialName="materialName"
              :materialId="materialId"
              :materialList="materialList"
              :colorList="colorList"
              @changeMaterial="handleChangeMaterial"
              @changeColor="handleChangeColor"
            ></Material>

            <!-- 产品详情 -->
            <Divider class="divider-wrap" :text="'产品详情'"></Divider>

            <div class="img-wrap" v-show="proDetail" v-html="proDetail"></div>
          </div>
        </div>
      </div>
    </transition>

    <div class="mask" v-show="showMask"></div>

    <!-- 机型 -->
    <transition name="fadeDialog">
      <div class="model-wrapper" v-show="showModel">
        <Model
          class="model"
          :type="'bottom'"
          :mobile="customerModelName"
          :modelList="modelList"
          :isFirstEnter="isFirstEnter"
          @cancel="handleCancelModel"
          @confirm="handleConfirmModel"
        ></Model>
      </div>
    </transition>

    <!-- 图库 -->
    <div
      class="picture-wrapper"
      :class="{ shadow: showPicture, hidden: !showPicture }"
      :style="'height:' + curHeight + (showPicture ? '' : 'px')"
      v-swipeup="{ fn: swipeup }"
    >
      <div class="handle-pic" @click="openPic" v-swipedown="{ fn: swipedown }">
        <span class="line line-arrow-up" v-show="!showPicture"></span>
        <span class="line line-arrow-down" v-show="showPicture"></span>
      </div>
      <div class="picture-data" :class="isLoading ? 'noclick' : ''">
        <Picture
          ref="pictureList"
          :curCategory="curCategory"
          :picCategory="picCategory"
          :isFirstEnter="isFirstEnter"
          :uploadValid="uploadValid"
          @changeCategory="getPicCategoryMore"
          @changeCategoryMore="getPicList"
          @changeSelect="changeSelect"
          @chooseImg="chooseImg"
          v-show="picCategory && picCategory.length > 0"
        ></Picture>

        <div
          class="no-data-wrap"
          v-show="picCategory && picCategory.length <= 0"
        >
          <NoData :showBtn="false"></NoData>
        </div>
      </div>
    </div>

    <!-- 添加文字 -->
    <transition name="fadeDialog">
      <div
        class="text-wrapper"
        id="textWrapper"
        ref="textWrapper"
        v-show="textDialog"
      >
        <div class="title">
          <div class="text">添加文字</div>
          <van-icon name="close" @click="closeTextDialog" />
        </div>
        <van-field
          class="input"
          v-model="inputtext"
          placeholder="在此输入文字内容"
          @focus="focusInput"
          clearable
        ></van-field>
        <div class="select-box">
          <van-field
            class="select"
            :value="getFamilyName(family)"
            placeholder="请选择"
            right-icon="arrow-down"
            @click="toggle"
            readonly
          >
          </van-field>
          <div class="select-list" v-show="showSelct">
            <div
              class="select-item"
              v-for="item in fontFamily"
              :key="item.id"
              @click="handleSelect(item)"
            >
              {{ item.name }}
            </div>
          </div>
        </div>
        <div class="color-box">
          <div class="color" :style="{ backgroundColor: fontColor }"></div>
          <slider-picker v-model="colors" :swatches="swatches"></slider-picker>
        </div>
        <div class="btn-box">
          <div class="btn cancel-btn" @click="removeText">取消</div>
          <div class="btn confirm-btn" @click="addText">确定</div>
        </div>
      </div>
    </transition>

    <!-- 贴图 -->
    <transition name="fadeDialog">
      <div class="map-wrapper" v-show="mapDialog">
        <van-icon name="close" @click="mapDialog = false" />
        <van-tabs
          class="picture-category"
          @click="chooseMapTab"
          v-model="curIndex"
        >
          <van-tab
            v-for="(map, index) in mapCategory"
            :key="index"
            :swipe-threshold="3"
          >
            <template #title>{{ map.name }}</template>

            <div class="pic-list" v-show="mapData && mapData.length > 0">
              <div class="img-list clearfix">
                <div
                  class="img-wrap"
                  v-for="(picture, picIndex) in mapData"
                  :key="picIndex"
                  @click.stop="changeSelect(picture)"
                >
                  <div class="img">
                    <img
                      v-lazy="
                        picture.originImage +
                        '?x-oss-process=image/resize,h_400,l_400'
                      "
                      :alt="picture.pictureName"
                    />
                  </div>
                </div>
              </div>
            </div>
            <div class="no-data-wrap" v-show="mapData && mapData.length <= 0">
              <NoData></NoData>
            </div>
          </van-tab>
        </van-tabs>
      </div>
    </transition>

    <Loading v-show="isLoading" :message="message"></Loading>
  </div>
</template>

<script>
// 组件
import DrawContainer from "components/diyCanvas/diyCanvas";
import Divider from "components/divider/divider";
import Material from "components/material/material";
import MaterialDetail from "components/material/detail";
import Model from "components/model/model";
import Picture from "components/picture/picture";
import Loading from "components/loading/loading";
import NoData from "components/noData/noData";
// js/插件/vuex
import wx from "weixin-js-sdk";
import { Slider } from "vue-color";
import { mapState } from "vuex";
import "../../common/js/vue-touch.js";
import EXIF from "exif-js";
import MobileDetect from "common/js/getDevice/mobile-detect.min.js";
import { getRenderer } from "common/js/getDevice/renderer.min.js";
import { getModel } from "common/js/getDevice/iphone-device.js";
// api
import api from "api/allApi.js";

// 使用Vue.mixin的方法拦截了路由离开事件，并在该拦截方法中实现了销毁页面缓存的功能
Vue.mixin({
  beforeRouteLeave: function (to, from, next) {
    if (to.path === "/previewer" || to.path === "/template") {
      from.meta.keepAlive = true;
    } else {
      if (this.$vnode && this.$vnode.data.keepAlive) {
        if (
          this.$vnode.parent &&
          this.$vnode.parent.componentInstance &&
          this.$vnode.parent.componentInstance.cache
        ) {
          if (this.$vnode.componentOptions) {
            var key =
              this.$vnode.key == null
                ? this.$vnode.componentOptions.Ctor.cid +
                  (this.$vnode.componentOptions.tag
                    ? `::${this.$vnode.componentOptions.tag}`
                    : "")
                : this.$vnode.key;
            var cache = this.$vnode.parent.componentInstance.cache;
            var keys = this.$vnode.parent.componentInstance.keys;
            if (cache[key]) {
              if (keys.length) {
                var index = keys.indexOf(key);
                if (index > -1) {
                  keys.splice(index, 1);
                }
              }
              delete cache[key];
            }
          }
        }
        from.meta.keepAlive = true;
      }
    }
    next();
  },
});

export default {
  name: "Custom",
  data() {
    return {
      isLoading: false,
      isPic: false,
      isFlag: 0,
      userNo: "",
      distributor: "", // 分销商名字
      inputtext: "", // 添加文字
      fontFamily: [], // 字体列表
      colors: {
        hex: "#194d33",
        hsl: {
          h: 150,
          s: 0.5,
          l: 0.2,
          a: 1,
        },
        hsv: {
          h: 150,
          s: 0.66,
          v: 0.3,
          a: 1,
        },
        rgba: {
          r: 25,
          g: 77,
          b: 51,
          a: 1,
        },
        a: 1,
      },
      swatches: ["100", ".80", ".65", ".50", ".35", ".20", "0"], // 颜色板
      family: "", // 当前字体
      fontColor: "",
      mapDialog: false, // 贴纸弹框
      uploadDialog: false, // 上传图片弹框
      mapData: [], // 贴纸列表
      mapCategory: [], // 贴纸分类
      loading: false,
      isData: true, // 检难数据是否完善
      modelInfo: [],
      materialsType: 0, // 材质类型
      manufactor: "", // 第三方工厂,上传类型：LWH-图片  YC-pdf
      materialsColorValue: "", // 手机材质颜色值
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
      dpiLevel: 0, // dpi等级
      ratio: 1, // 缩放比
      skuNo: 0, // skuNo
      skuName: "",
      message: "", // loading提示
      error: "", // 错误提示信息
      curPicType: 1, // 当前素材分类 - 1 自定义 2 IP图库 3 模板
      distributorId: 0, // 分销商ID
      platform: "", // 平台
      exchangeId: "", // 兑换码id
      uploadValid: 1, // 是否允许用户上传（0 不允许，1 允许）
      showMask: false, // 蒙版
      showModel: false, // 机型弹窗
      brandName: "", // 手机品牌
      brandId: 0, // 品牌ID
      modelId: 0, // 型号ID
      modelName: "", // 手机型号
      showMaterial: false, // 材质弹窗
      modelList: [], // 型号列表
      materialId: "", // 材质ID
      materialNo: "", // 材质编码
      materialName: "", // 材质名
      materialList: [], // 材质列表
      colorName: "", // 材质颜色
      colorList: [], // 颜色列表
      showMoreHandle: false, // 是否显示更多操作
      curHeight: 0, // 当前图库高度
      curCategory: 0, // 当前图库分类
      picCategory: [], // 图库分类
      picData: [], // 图库图片
      textDialog: false, // 文字弹框
      showPicture: false, // 图库（上滑下滑）
      proDetail: "", // 产品详情
      originMaterial: {}, // 选择材质前材质信息
      copyrightCost: 0, // 图片版权费
      isMiniProgram: false, // 是否是小程序
      canSelectMaterial: true, // 是否可以切换材质
      isZFold: false, 
      isBack: false, // 是否回退
      isFirstEnter: false, // 是否第一次进入
      loadingTimer: null, // 加载定时器（提示网络较差）
      underStockFlag: false, // 是否缺货
      customerModelName: "", // 识别机型
    };
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      if (from.path === "/previewer" || from.path === "/template") {
        vm.isBack = true;
      }
      vm.mapDialog = false;
      vm.message = "";
      vm.isLoading = false;
      vm._getQueryData();
    });
  },
  created() {
    var enterFlag = this.getQueryVariable("enterFlag");
    if (enterFlag === "diyCustom") {
      // 小程序进入
      var params = this.getQueryVariable("enterParams");
      if (params) {
        var enterParams = JSON.parse(params);
        localStorage.setItem("userId", enterParams.userId);
        localStorage.setItem("phone", enterParams.phone);
        localStorage.setItem("userNo", enterParams.userNo);
        localStorage.setItem("auth", enterParams.auth);
        localStorage.setItem("openId", enterParams.openid);
        localStorage.setItem("distributorId", enterParams.distributorId);
        localStorage.setItem("exchangeId", enterParams.exchangeId);
        localStorage.setItem("platform", enterParams.platform);
        localStorage.setItem("orderSource", enterParams.orderSource);

        sessionStorage.setItem("modelId", enterParams.modelId);
        sessionStorage.setItem(
          "modelName",
          decodeURIComponent(enterParams.modelName)
        );
        sessionStorage.setItem("materialId", enterParams.materialId);

        this.canSelectMaterial = enterParams.canSelectMaterial ? false : true;
        this.isMiniProgram = true;
        sessionStorage.setItem("isMiniProgram", this.isMiniProgram);
      }
    }

    this.isMiniProgram = sessionStorage.getItem("isMiniProgram") || false;

    this.userNo = localStorage.getItem("userNo") || "";
    this.platform = localStorage.getItem("platform") || 28;
    this.distributorId = localStorage.getItem("distributorId");
    this.exchangeId = localStorage.getItem("exchangeId");

    // 是否有图片id，根据图片id获取图片信息
    this.picId = this.$route.query.pid || "";
    if (this.picId) {
      this.getPictureInfoById(this.picId);
    }

    this.modelId = this.$route.query.modelId
      ? this.$route.query.modelId
      : sessionStorage.getItem("modelId")
      ? sessionStorage.getItem("modelId")
      : "";
    this.modelName = this.$route.query.modelName
      ? this.$route.query.modelName
      : sessionStorage.getItem("modelName")
      ? sessionStorage.getItem("modelName")
      : "";
    this.materialId = this.$route.query.materialId
      ? this.$route.query.materialId
      : sessionStorage.getItem("materialId")
      ? sessionStorage.getItem("materialId")
      : "";

    // 爱思定制
    let distributorId = this.getQueryVariable("distributorId");
    let orderSource = this.getQueryVariable("orderSource");
    let platform = this.getQueryVariable("Platform");
    if (distributorId === "6175" && orderSource === "GF60005") {
      localStorage.removeItem("auth");
      let aAttach = this.$route.query.attach || "";
      sessionStorage.setItem("aAttach", aAttach);
      document.title = "i定制";

      this.distributorId = distributorId;
      this.platform = platform;

      localStorage.setItem("distributorId", distributorId);
      localStorage.setItem("orderSource", orderSource);
      localStorage.setItem("platform", platform);
    } else {
      document.title = "DIY定制";
    }

    // 记录数据埋点
    let hasRecord = sessionStorage.getItem("hasRecord");
    if (!hasRecord) {
      this.handleRecordData();
    }
  },
  mounted() {
    // 画布宽高
    this.getDrawSize();
  },
  activated() {
    // 画布宽高
    this.getDrawSize();
  },
  computed: {
    ...mapState(["loading"]),
  },
  methods: {
    getQueryVariable(name) {
      var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
      var r = window.location.search.substr(1).match(reg);
      if (r != null) {
        return unescape(r[2]);
      } else {
        return null;
      }
    },
    // 记录埋点数据
    handleRecordData() {
      let userId = localStorage.getItem("userId") || "";
      this.$api
        .post(this, api.recordPoint, {
          source: this.platform,
          userId: userId,
          distributorId: this.distributorId,
          networkType: 1,
        })
        .then((res) => {
          if (res.success) {
            sessionStorage.setItem("hasRecord", 1);
          }
        });
    },
    // 画布宽高
    getDrawSize() {
      this.wwidth = window.screen.width;
      this.wheight = 400;
      let phoneT = parseInt(window.getComputedStyle(this.$refs.phoneT).height);
      let sHeight = `${document.documentElement.clientHeight}`;
      let H = Number(sHeight) - phoneT;
      this.curHeight = H;

      document.body.style.overflow = "auto";
    },
    _getQueryData() {
      // 判断是否有图片
      let pid = this.$route.query.pid;
      let url = this.$route.query.picUrl;
      if (pid || url) {
        this.isPic = true;
      }

      this.initData();
    },
    // 初始化数据
    initData() {
      this.message = "数据加载中";
      this.isLoading = true;

      // 获取传过来的材质参数
      let materialNo = this.$route.query.materialsNo || "";
      if (this.isBack) {
        // 获取所有品牌型号
        this.getBrand();
      } else {
        if (materialNo) {
          // 已传材质编码，优先适配材质（爱思）
          this.getMaterialList("isMaterial");
        } else if (this.modelName) {
          // 已选择过型号，直接匹配
          this.getBrand();
        } else {
          // 第一次进入，自动获取机型
          this.getMobileDevice();
        }
      }
    },
    // 自动获取机型
    getMobileDevice(gainType) {
      // 判断数组中是否包含某字符串（安卓机型获取用到）
      /* eslint-disable */
      Array.prototype.contains = function (needle) {
        for (i in this) {
          if (this[i].toString().indexOf(needle) >= 0) {
            return i;
          }
        }
        return -1;
      };
      // 获取 UA
      let userAgent = navigator.userAgent;
      // 初始化 mobile-detect
      var md = new MobileDetect(userAgent);
      var os = md.os(); // 获取系统
      // iOS 系统的处理
      if (os === "iOS") {
        os = md.os() + md.version("iPhone");
        // 通过 renderer.js 获取苹果机具体的 GPU
        let rendererVal;
        getRenderer((renderer) => {
          rendererVal = renderer;
          if (renderer === "Unknown") {
            debugger;
            var canvas = document.createElement("canvas");
            if (canvas != null) {
              var context = canvas.getContext("experimental-webgl");
              if (context) {
                var info = context.getExtension("WEBGL_debug_renderer_info");
                if (info) {
                  rendererVal = context.getParameter(
                    info.UNMASKED_RENDERER_WEBGL // GPU 型号
                  );
                }
              }
            }
          }
        });

        let networkModel = this.$route.query.productType || "";
        if (networkModel) {
          // 爱思，苹果机型产品类型
          this.getPhoneModelByNetwork(networkModel.trim()).then((res) => {
            if (res.success && res.data) {
              this.modelName = res.data.name;
              this.customerModelName = res.data.name;
            } else {
              this.modelName = "";
              this.customerModelName = "";
              this.showModel = true;
              this.showMask = true;
            }
            this.getBrand(gainType);
          });
        } else {
          // 通过 iphone-device.js 获取苹果机具体的机型
          let phone = "";
          if (this.$route.query.phoneName) {
            phone = this.$route.query.phoneName;
          } else {
            phone = getModel(rendererVal);
          }

          if (phone.indexOf(",") > 0) {
            this.modelName = phone.substring(0, phone.indexOf(","));
            this.customerModelName = phone.substring(0, phone.indexOf(","));
          } else {
            this.modelName = phone;
            this.customerModelName = phone;
          }
          this.getBrand(gainType);
        }
      } else if (os === "AndroidOS") {
        // Android 系统的处理
        os = md.os() + md.version("Android");
        // 判断 UA 里边有没有 Build 或者 AppleWebkit信息，通过这个拿到安卓的具体机型
        let str;
        if (userAgent.indexOf("Build/") > -1) {
          str = "Build/";
        } else {
          str = "AppleWebKit";
        }
        var sss = userAgent.split(";");
        var i = sss.contains(str);
        if (i > -1) {
          // Android 入网型号
          let networkModel;
          if (str === "Build/") {
            networkModel = sss[i].substring(0, sss[i].indexOf(str));
          } else {
            if (sss[i - 1].indexOf("Android") > 0) {
              let strArr = userAgent.split(" ");
              let j = strArr.contains(str);
              let len = strArr[j - 1];
              networkModel = len.substring(0, len.length - 1);
            } else {
              networkModel = sss[i - 1];
            }
          }
          this.getPhoneModelByNetwork(networkModel.trim()).then((res) => {
            if (res.success && res.data) {
              this.modelName = res.data.name;
              this.customerModelName = res.data.name;
            } else {
              this.modelName = "";
              this.customerModelName = "";
              this.showModel = true;
              this.showMask = true;
            }
            this.getBrand(gainType);
          });
        }
      }
    },
    // 根据入网型号获取具体机型
    getPhoneModelByNetwork(networkModel) {
      return new Promise((resolve, reject) => {
        this.$api
          .get(this, api.getModelByNetwork, {
            networkModel: networkModel,
          })
          .then((res) => {
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // 字体选择
    handleSelect(item) {
      this.showSelct = false;
      this.family = item.englishName;
      this.initFont(item, this.family);
    },
    // 引入字体文件，并初始化字体
    initFont(item, fontFamily) {
      let style = document.createElement("style");
      style.type = "text/css";
      let str =
        '@font-face {font-family: "' +
        item.englishName +
        '"; src: url("' +
        item.fontFile +
        '"); font-weight: normal; font-style: normal;}';
      style.innerText = str;
      document.getElementsByTagName("head")[0].appendChild(style);

      let dom = document.createElement("div");
      dom.style.fontFamily = fontFamily;
      dom.style.fontSize = "14px";
      dom.style.opacity = "0";
      dom.innerHTML = "test";
      document.getElementById("phone").appendChild(dom);
    },
    // 添加文字高度自适应
    focusInput() {
      if (window.screen.width > 500) {
        this.$refs.textWrapper.style.height = 350 + "px";
      }
    },
    toggle() {
      this.$refs.textWrapper.style.height = "auto";
      this.showSelct = !this.showSelct;
    },
    // 获取所有品牌型号
    getBrand(gainType) {
      this.$api
        .get(this, api.getModelListNew, {
          categoryId: 1, // 产品类型id：1 手机壳
          distributorId: this.distributorId,
          materialId: this.materialId,
          pictureId: this.picId,
          platform: this.platform, // 平台
        })
        .then((res) => {
          if (res.success) {
            if (res.data && res.data.length > 0) {
              let dataArr = res.data;
              this.modelList = res.data;

              // 判断是否是第一次进入，默认展示选择手机型号
              let isFirstEnter = sessionStorage.getItem("isFirstEnter") || "";
              if (!isFirstEnter && Number(this.distributorId) === 6175) {
                // 爱思定制
                this.showModel = true;
                this.showMask = true;
                this.isFirstEnter = true;
                sessionStorage.setItem("isFirstEnter", 1);
              }

              // 根据型号查询品牌并显示
              if (this.modelName !== "" && this.modelName !== undefined) {
                this.getModelId(this.modelName);
              } else {
                // 默认获取适用的机型
                try {
                  dataArr.forEach((brand) => {
                    if (brand.childrenList.length > 0) {
                      brand.childrenList.forEach((model) => {
                        if (model.validFlag) {
                          this.modelId = model.modelId;
                          this.modelName = model.name;
                          this.brandId = brand.modelId;
                          this.brandName = brand.name;

                          // 是否缺货
                          this.underStockFlag = model.underStockFlag;

                          throw ""; // 中断forEach循环
                        }
                      });
                    }
                  });
                } catch (error) {
                  console.log(error);
                }
              }

              if (gainType) {
                if (gainType === "changePic") {
                  // 切换图片或删除图片
                  if (Number(this.distributorId) === 6175) {
                    // 爱思，根据手机型号和材质获取手机定制信息
                    this.getInfo(gainType);
                  } else {
                    // 获取材质
                    this.getMaterialList(gainType);
                  }
                } else {
                  if (gainType === "modelType") {
                    // 选择型号不适用材质时
                    this.getMaterialList(); // 根据当前适用型号重新获取材质
                    this.handleModel(gainType); // 显示型号选择弹窗
                  } else if (gainType === "pictureType") {
                    // 选择图片不适用材质时
                    this.getInfo(); // 根据手机型号和材质获取手机定制信息
                    // 获取图库分类
                    this.$refs.pictureList.curIndex = 0; // 默认选择第一个分类
                    this.getPicCategory(gainType);
                  } else {
                    // 已有材质/选择材质不适用型号时
                    if (gainType === "isMaterial") {
                      // 获取材质详情
                      this.getDetailIntro(this.materialId);
                    }

                    // 根据手机型号和材质获取手机定制信息
                    this.getInfo();
                    // 获取图库分类
                    this.$refs.pictureList.curIndex = 0; // 默认选择第一个分类
                    this.getPicCategory(gainType);
                  }
                }
              } else {
                // 根据型号获取材质
                this.getMaterialList();
              }
              if (this.modelName === "") {
                this.modelName = this.getModelNameById();
              }
            }
          } else {
            this.message = "";
            this.isLoading = false;
          }
        });
    },
    // 根据型号 id 查询型号
    getModelNameById() {
      let mName;
      let bid = parseInt(this.brandId);
      let sid = parseInt(this.modelId);
      this.modelList.forEach((item) => {
        if (item.modelId === bid) {
          if (item.childrenList) {
            item.childrenList.forEach((child) => {
              if (child.modelId === sid) {
                mName = child.name;
              }
            });
          }
        }
      });
      let str = mName;
      return str;
    },
    // 根据手机型号查询型号及品牌
    getModelId(mname) {
      if (this.modelList.length > 0) {
        let flag = false;
        try {
          this.modelList.forEach((brand) => {
            if (brand.childrenList.length > 0) {
              brand.childrenList.forEach((model) => {
                if (
                  model.name.replace(/\s*/g, "").toLowerCase() ===
                    mname.replace(/\s*/g, "").toLowerCase() &&
                  model.validFlag
                ) {
                  this.modelId = model.modelId;
                  this.modelName = model.name;
                  this.brandId = brand.modelId;
                  this.brandName = brand.name;

                  this.customerModelName = model.name; // 用户机型识别

                  // 是否缺货
                  this.underStockFlag = model.underStockFlag;

                  flag = true;

                  throw ""; // 中断forEach循环
                }
              });
            }
          });
        } catch (error) {
          console.log(error);
        }
        if (!flag) {
          try {
            this.modelList.forEach((brand) => {
              if (brand.childrenList.length > 0) {
                brand.childrenList.forEach((model) => {
                  if (model.validFlag) {
                    this.modelId = model.modelId;
                    this.modelName = model.name;
                    this.brandId = brand.modelId;
                    this.brandName = brand.name;

                    // 是否缺货
                    this.underStockFlag = model.underStockFlag;

                    throw ""; // 中断forEach循环
                  }
                });
              }
            });
            // this.$toast.fail("未匹配到当前手机型号，已展示默认机型");
            this.showModel = true;
            this.showMask = true;
          } catch (error) {
            console.log(error);
          }
        }
      }
    },
    // 根据型号获取材质列表
    getMaterialList(gainType) {
      let isZFold = this.modelName.toUpperCase().indexOf("FOLD3") >= 0 ? 1 : 0;

      // 获取传过来的材质参数
      let materialNo = this.$route.query.materialsNo || "";
      let mName = this.$route.query.materialsName || "";

      this.$api
        .get(this, api.getMaterialListNew, {
          pictureId: this.picId === 0 || isZFold ? "" : this.picId,
          categoryId: 1, // 产品类型id：1 手机壳
          distributorId: this.distributorId,
          modelId: this.modelId,
          platform: this.platform, // 平台
        })
        .then((res) => {
          if (res.success) {
            if (res.data && res.data.length > 0) {
              this.isData = true;
              let dataArr = res.data;

              if (materialNo) {
                this.materialList = [];
                // 材质编号
                dataArr.forEach((item) => {
                  let curItem = item;
                  if (item.childrenList && item.childrenList.length > 0) {
                    item.childrenList.forEach((material, r) => {
                      if (material.materialNo === materialNo) {
                        this.$set(curItem, "childrenList", [material]);
                        this.materialList.push(curItem);
                        this.colorName = material.name;
                        this.materialId = material.materialId;
                        this.materialNo = material.materialNo;
                        this.skuNo = material.itemCode;
                        this.materialsColorValue = material.colour;
                        this.manufactor = material.manufactor;

                        // 获取是否允许用户上传
                        this.uploadValid = material.allowUploadFlag;
                        // 是否强制铺满血位：1 是，0 否
                        let isAllPlace = material.mandatoryCoveredBleedFlag;
                        sessionStorage.setItem("isAllPlace", isAllPlace);

                        // 是否缺货
                        this.underStockFlag = material.underStockFlag;

                        return;
                      }
                      return;
                    });
                  } else {
                    if (item.materialNo === materialNo) {
                      this.materialList.push(item);
                      this.colorName = item.name;
                      this.materialId = item.materialId;
                      this.materialNo = "";
                      this.skuNo = "";
                      this.materialsColorValue = item.colour;
                      this.manufactor = item.manufactor;

                      return;
                    }
                  }
                });

                if (this.materialList.length <= 0) {
                  this.materialId = "";
                  this.materialName = "";
                  this.materialNo = "";
                  this.skuNo = "";
                  this.colorName = "";
                  this.materialsColorValue = "";
                  this.manufactor = "";
                  this.message = "";
                  this.isLoading = false;
                  this.isData = false;

                  this.$dialog
                    .confirm({
                      title: "温馨提示",
                      message:
                        "暂无关联的材质数据，请选择其它材质或联系客服处理~",
                      className: "confirm-v-dialog tl",
                      confirmButtonText: "关闭",
                      showConfirmButton:
                        Number(this.distributorId) === 6175 ? false : true, // 爱思 不显示关闭按钮
                      showCancelButton: false,
                      confirmButtonColor: "#333333",
                    })
                    .then(() => {})
                    .catch((error) => {
                      console.log(error);
                    });
                }
              } else if (mName) {
                this.materialList = [];
                // 材质名称
                dataArr.forEach((item) => {
                  let curItem = item;
                  if (item.childrenList && item.childrenList.length > 0) {
                    item.childrenList.forEach((material, r) => {
                      if (mName.indexOf(material.name) >= 0) {
                        this.$set(curItem, "childrenList", [material]);
                        this.materialList.push(curItem);
                        this.colorName = material.name;
                        this.materialId = material.materialId;
                        this.materialNo = material.materialNo;
                        this.skuNo = material.itemCode;
                        this.materialsColorValue = material.colour;
                        this.manufactor = material.manufactor;

                        // 获取是否允许用户上传
                        this.uploadValid = material.allowUploadFlag;
                        // 是否强制铺满血位：1 是，0 否
                        let isAllPlace = material.mandatoryCoveredBleedFlag;
                        sessionStorage.setItem("isAllPlace", isAllPlace);

                        // 是否缺货
                        this.underStockFlag = material.underStockFlag;

                        return;
                      }
                      return;
                    });
                  } else {
                    if (mName.indexOf(item.name) >= 0) {
                      this.materialList.push(item);
                      this.colorName = item.name;
                      this.materialId = item.materialId;
                      this.materialNo = "";
                      this.skuNo = "";
                      this.materialsColorValue = item.colour;
                      this.manufactor = item.manufactor;

                      return;
                    }
                  }
                });

                if (this.materialList.length <= 0) {
                  this.materialId = "";
                  this.materialName = "";
                  this.materialNo = "";
                  this.skuNo = "";
                  this.colorName = "";
                  this.materialsColorValue = "";
                  this.manufactor = "";
                  this.message = "";
                  this.isLoading = false;
                  this.isData = false;

                  this.$dialog
                    .confirm({
                      title: "温馨提示",
                      message:
                        "暂无关联的材质数据，请选择其它材质或联系客服处理~",
                      className: "confirm-v-dialog tl",
                      confirmButtonText: "关闭",
                      showConfirmButton:
                        Number(this.distributorId) === 6175 ? false : true, // 爱思 不显示关闭按钮
                      showCancelButton: false,
                      confirmButtonColor: "#333333",
                    })
                    .then(() => {})
                    .catch((error) => {
                      console.log(error);
                    });
                }
              } else {
                this.materialList = res.data;

                // 添加manufactor参数，及材质二级分类
                if (this.materialId) {
                  this.getMaterialById(this.materialId);
                } else {
                  // 默认获取适用的材质
                  try {
                    dataArr.forEach((material) => {
                      if (
                        material.childrenList &&
                        material.childrenList.length > 0
                      ) {
                        material.childrenList.forEach((child) => {
                          if (child.validFlag) {
                            this.colorName = child.name;
                            this.materialId = child.materialId;
                            this.materialNo = child.materialNo;
                            this.skuNo = child.itemCode;
                            this.materialsColorValue = child.colour;
                            this.manufactor = child.manufactor;
                            this.materialName = material.name;

                            // 获取是否允许用户上传
                            this.uploadValid = child.allowUploadFlag;
                            // 是否强制铺满血位：1 是，0 否
                            let isAllPlace = child.mandatoryCoveredBleedFlag;
                            sessionStorage.setItem("isAllPlace", isAllPlace);

                            // 是否缺货
                            this.underStockFlag = child.underStockFlag;

                            throw ""; // 中断forEach循环
                          }
                        });
                      }
                    });
                  } catch (error) {
                    console.log(error);
                  }
                }
              }

              this.getCurrentMaterial();

              if (this.materialId) {
                if (gainType) {
                  if (gainType === "isMaterial") {
                    // 已有材质
                    this.getMobileDevice(gainType);
                  } else if (gainType === "materialType") {
                    // 选择材质不适用型号时
                    this.getBrand("isMaterial"); // 根据当前适用材质重新获取型号
                    this.handleMaterial(gainType); // 显示材质选择弹窗
                  } else if (gainType === "pictureType") {
                    // 选择图片不适用型号时
                    this.isData = false;
                    // 清空图片
                    this.$refs.mcanvas.dragArr.forEach((item, index) => {
                      if (item.picId !== 0) {
                        this.$refs.mcanvas.dragArr.splice(index, 1);
                      }
                    });

                    this.picId = "";
                    this.picName = "";
                    this.wallpImg = "";
                    this.curPicType = 1;
                    this.getBrand(gainType); // 根据当前适用材质重新获取型号
                  } else if (gainType === "changePic") {
                    // 切换图片或删除图片，根据手机型号和材质获取手机定制信息
                    this.getInfo(gainType);
                  }
                } else {
                  // 根据手机型号和材质获取手机定制信息
                  this.getInfo();
                  // 获取材质详情
                  this.getDetailIntro(this.materialId);

                  // 获取图库分类
                  this.$refs.pictureList.curIndex = 0; // 默认选择第一个分类
                  this.getPicCategory();
                }
              } else {
                this.$dialog
                  .confirm({
                    title: "温馨提示",
                    message:
                      "暂无关联的材质数据，请选择其它材质或联系客服处理~",
                    className: "confirm-v-dialog tl",
                    confirmButtonText: "关闭",
                    showConfirmButton:
                      Number(this.distributorId) === 6175 ? false : true, // 爱思 不显示关闭按钮
                    showCancelButton: false,
                    confirmButtonColor: "#333333",
                  })
                  .then(() => {
                    this.message = "";
                    this.isLoading = false;
                    this.isData = false;
                    // 清空型号
                    this.modelId = "";
                    this.modelName = "";
                    this.handleModel(gainType);
                  })
                  .catch((error) => {
                    console.log(error);
                  });
              }
            } else {
              this.picData = [];
              this.materialId = "";
              this.materialName = "";
              this.materialNo = "";
              this.skuNo = "";
              this.colorName = "";
              this.materialsColorValue = "";
              this.manufactor = "";
              this.message = "";
              this.isLoading = false;
              this.isData = false;
              this.reset();

              this.$dialog
                .confirm({
                  title: "温馨提示",
                  message: "暂无关联的材质数据，请选择其它材质或联系客服处理~",
                  className: "confirm-v-dialog tl",
                  confirmButtonText: "关闭",
                  showConfirmButton:
                    Number(this.distributorId) === 6175 ? false : true, // 爱思 不显示关闭按钮
                  showCancelButton: false,
                  confirmButtonColor: "#333333",
                })
                .then(() => {})
                .catch((error) => {
                  console.log(error);
                });
            }
          } else {
            this.isData = false;
            this.message = "";
            this.isLoading = false;
            this.$toast.fail(res.errMessage);
          }
        });
    },
    // 根据材质ID获取材质信息
    getMaterialById(id) {
      let flag = false;
      try {
        this.materialList.forEach((material) => {
          if (material.childrenList && material.childrenList.length > 0) {
            material.childrenList.forEach((child) => {
              if (child.materialId === Number(id) && child.validFlag) {
                this.materialName = material.name;
                this.materialNo = child.materialNo;
                this.skuNo = child.itemCode;
                this.colorName = child.name;
                this.materialsColorValue = child.colour;
                this.manufactor = child.manufactor;

                // 获取是否允许用户上传
                this.uploadValid = child.allowUploadFlag;
                // 是否强制铺满血位：1 是，0 否
                let isAllPlace = child.mandatoryCoveredBleedFlag;
                sessionStorage.setItem("isAllPlace", isAllPlace);

                // 是否缺货
                this.underStockFlag = child.underStockFlag;

                flag = true;

                throw ""; // 中断forEach循环
              }
            });
          }
        });
      } catch (error) {
        console.log(error);
      }

      if (!flag) {
        try {
          this.materialList.forEach((material) => {
            if (material.childrenList && material.childrenList.length > 0) {
              material.childrenList.forEach((child) => {
                if (child.validFlag) {
                  this.materialId = child.materialId;
                  this.materialNo = child.materialNo;
                  this.skuNo = child.itemCode;
                  this.materialName = material.name;
                  this.colorName = child.name;
                  this.materialsColorValue = child.colour;
                  this.manufactor = child.manufactor;

                  // 获取是否允许用户上传
                  this.uploadValid = child.allowUploadFlag;
                  // 是否强制铺满血位：1 是，0 否
                  let isAllPlace = child.mandatoryCoveredBleedFlag;
                  sessionStorage.setItem("isAllPlace", isAllPlace);

                  // 是否缺货
                  this.underStockFlag = child.underStockFlag;

                  throw ""; // 中断forEach循环
                }
              });
            }
          });
        } catch (error) {
          console.log(error);
        }
      }
    },
    // 获取图库分类
    getPicCategory(gainType) {
      this.$api
        .get(this, api.getPictureCategory, {
          distributorId: this.distributorId,
          // exchangeId: this.exchangeId,
          materialId: this.materialId,
          modelId: this.modelId,
        })
        .then((res) => {
          if (res.success) {
            if (res.data && res.data.length > 0) {
              this.picCategory = res.data;
              this.curCategory = res.data[0].id;
              this.getPicCategoryMore(this.curCategory, gainType);
            } else {
              this.picCategory = [];
              this.curCategory = 0;
            }
          } else {
            this.message = "";
            this.isLoading = false;
            this.$toast.fail(res.errMessage);
          }
        });
    },
    // 获取图库二级分类
    getPicCategoryMore(categoryId, gainType) {
      this.curCategory = categoryId;
      
      this.$api
        .get(this, api.getPictureCategoryMore, {
          distributorId: this.distributorId,
          // exchangeId: this.exchangeId,
          materialId: this.materialId,
          modelId: this.modelId,
          categoryId: categoryId,
        })
        .then((res) => {
          if (res.success) {
            let curCategory = "";
            if (res.data && res.data.length > 0) {
              curCategory = res.data[0].id;
              // 二级分类
              this.picCategory.forEach((item) => {
                this.$set(item, "twoPictureCategory", []);
                if (item.id === categoryId) {
                  this.$set(item, "twoPictureCategory", res.data);
                }
              })
            } else {
              curCategory = categoryId;
            }
            this.getPicList(curCategory, gainType);
          } else {
            this.message = "";
            this.isLoading = false;
            this.$toast.fail(res.errMessage);
          }
        });
    },
    // 获取图库图片列表
    getPicList(categoryId, gainType) {
      this.$api
        .get(this, api.getPicture, {
          distributorId: this.distributorId,
          categoryId: categoryId,
          page: 1,
          size: 100,
          materialId: this.materialId,
          modelId: this.modelId,
          // exchangeId: this.exchangeId,
        })
        .then((res) => {
          if (res.success) {
            // 爱思，第一次进去自动弹出图库（已有材质）/选择图片不适用材质、型号时
            if (
              gainType &&
              (gainType === "next" || gainType === "pictureType")
            ) {
              // 判断是否传图
              if (!this.picId) {
                // 未传图，弹出图库
                this.swipeup();
              }
            }

            this.picData = res.data;
            this.picCategory.forEach((item) => {
              if (item.id === this.curCategory) {
                this.$set(item, "picList", this.picData.list);
              }
            });

            if (this.picData.list.length > 0) {
              if (this.isPic) {
                // 进入页面，有传图
                let pid = this.$route.query.pid || 0;
                // 是否有图片id
                if (pid) {
                  this.changeImage(
                    {},
                    this.wallpImg,
                    this.picId,
                    this.curPicType
                  );
                } else {
                  let url = this.$route.query.picUrl;
                  let type = this.$route.query.picType || 1;
                  this.changeImage({}, url, pid, type);
                }
              } else {
                if (this.curPicType === 2) {
                  // IP图直接替换
                  this.changeImage(
                    {},
                    this.wallpImg,
                    this.picId,
                    this.curPicType
                  );
                } else {
                  this.message = "";
                  this.isLoading = false;
                  // 判断是否允许用户上传
                  if (Number(this.uploadValid) !== 1) {
                    // 否
                    this.$refs.mcanvas.dragArr.forEach((item, index) => {
                      if (item.picId === 0) {
                        // 清空已上传图片
                        this.$refs.mcanvas.dragArr.splice(index, 1);
                      }
                    });
                  }
                }
              }
            } else {
              this.message = "";
              this.isLoading = false;
              this.wwidth = window.screen.width;
              this.mapDialog = false;
              this.$refs.mcanvas.dragArr.forEach((item, index) => {
                if (item.picId !== 0) {
                  this.$refs.mcanvas.dragArr.splice(index, 1);
                } else {
                  // 判断是否允许用户上传
                  if (Number(this.uploadValid) !== 1) {
                    // 否，清空已上传图片
                    this.$refs.mcanvas.dragArr.splice(index, 1);
                  }
                }
              });
            }

            this.$refs.mcanvas.initCanvas();
            this.$refs.mcanvas.draw();
            this.$refs.phoneWrapper.dispatchEvent(new MouseEvent("click"));
          } else {
            this.message = "";
            this.isLoading = false;
            this.$toast.fail(res.errMessage);
          }
        });
    },
    // 根据图片id获取图片信息
    getPictureInfoById(pid) {
      this.$api
        .get(this, api.getPictureInfoById, {
          id: pid,
        })
        .then((res) => {
          if (res.success) {
            this.picId = res.data.id;
            this.wallpImg = res.data.originImage;
            this.curPicType = res.data.type;
            this.picName = res.data.pictureName;
            this.copyrightCost = res.data.copyrightCost;
          } else {
            this.$toast.fail(res.errMessage);
          }
        });
    },
    // 根据手机型号和材质获取手机定制信息
    getInfo(gainType) {
      let isZFold = this.modelName.toUpperCase().indexOf("FOLD3") >= 0 ? 1 : 0;
      this.isZFold = isZFold;
      if (Number(sessionStorage.getItem("isZFold")) !== isZFold) {
        // 清空画布
        this.$refs.mcanvas.dragArr = [];
        this.$refs.mcanvas.clear();
      }
      sessionStorage.setItem("isZFold", isZFold);

      this.$api
        .get(this, api.getCustomInfo, {
          modelId: this.modelId,
          materialId: this.materialId,
        })
        .then((res) => {
          if (res.success) {
            if (res.data) {
              this.isData = true;
              this.modelInfo = res.data;
              this.phoneImg = this.modelInfo.floorImage;
              this.frameImg = this.modelInfo.outImage;

              // 手机图长宽比
              this.pscale =
                this.pm2px(this.modelInfo.width) /
                this.pm2px(this.modelInfo.length);
              this.phonenHeight = this.wheight * 0.8;
              this.phonenWidth = this.phonenHeight * this.pscale;

              /**
               * 判断材质类型：
               *  74    玻璃壳
               *  82    TPU（亮透彩印软壳）
               *  84    热升华（真彩创意壳）
               *  86    亲肤壳(晶透浮雕壳)
               *  91    防摔壳
               *  108   炫彩肤感壳-古董白
               *  109   炫彩肤感壳-经典黑
               */
              switch (Number(this.materialId)) {
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

              if (
                Number(this.materialId) === 74 ||
                Number(this.materialId) === 84
              ) {
                // 玻璃壳/热升华+2mm
                this.phoneHeight = this.wheight * 0.8 + this.pm2px(2);
                this.phoneWidth = this.phoneHeight * this.pscale;
                let mscale = this.modelInfo.width / this.modelInfo.length;
                if (this.modelInfo.width > this.modelInfo.length) {
                  this.mh = this.modelInfo.length + 2;
                  this.mw = this.mh * mscale;
                } else {
                  this.mw = this.modelInfo.width + 2;
                  this.mh = this.mw / mscale;
                }
                this.nh = this.modelInfo.length;
                this.nw = this.modelInfo.width;
              } else {
                // TPU/亲肤壳
                this.phoneHeight = this.wheight * 0.8;
                this.phoneWidth = this.phoneHeight * this.pscale;
                this.mh = this.nh = this.modelInfo.length;
                this.mw = this.nw = this.modelInfo.width;
              }

              sessionStorage.setItem("nw", this.nw.toFixed(2));
              sessionStorage.setItem("nh", this.nh.toFixed(2));

              // 透明间距
              let frameValue = {
                topFrame: this.modelInfo.topFrame || 0,
                underFrame: this.modelInfo.underFrame || 0,
                leftFrame: this.modelInfo.leftFrame || 0,
                rightFrame: this.modelInfo.rightFrame || 0,
              };
              sessionStorage.setItem("frameValue", JSON.stringify(frameValue));

              // 判断是否有切图信息（折叠屏）
              if (
                (this.modelInfo.cutType === 1 &&
                  this.modelInfo.slittingHeight) ||
                (this.modelInfo.cutType === 2 &&
                  this.modelInfo.crosscuttingWidth)
              ) {
                let cutInfo = JSON.stringify({
                  cutType: this.modelInfo.cutType,
                  slittingHeight: this.modelInfo.slittingHeight,
                  slittingWhite: this.modelInfo.slittingWhite,
                  crosscuttingWidth: this.modelInfo.crosscuttingWidth,
                  crosscuttingWhite: this.modelInfo.crosscuttingWhite,
                });
                sessionStorage.setItem("cutInfo", cutInfo);
              } else {
                sessionStorage.setItem("cutInfo", "");
              }

              this.getPrice(gainType);
            }
          } else {
            this.message = "";
            this.isLoading = false;
            this.isData = false;
            this.$toast.fail(res.errMessage);
          }
        });
    },
    getPrice(gainType) {
      // 获取传过来的价格参数
      let price = this.$route.query.price;
      if (price) {
        this.price = price;
        this.isData = true;
        this.isPrice = true;
      } else {
        this.$api
          .get(this, api.getPrice, {
            distributorId: this.distributorId,
            orderSource: this.platform,
            materialId: this.materialId,
          })
          .then((res) => {
            if (res.success) {
              this.isData = true;
              if (res.data) {
                this.price = res.data + this.copyrightCost;
              } else {
                this.price = this.copyrightCost;
              }
              this.isPrice = true;

              if (gainType && gainType === "changePic") {
                this.message = "";
                this.isLoading = false;
              }
            } else {
              this.message = "";
              this.isLoading = false;
              this.wallpImg = "";
              this.isPrice = false;
              this.price = 0;
              this.isData = false;
              this.picData = [];
              this.$toast.fail(res.errMessage);
              this.reset();
            }
          });
      }
    },
    // 获取字体列表
    getFont() {
      this.$api.get(this, api.getFontList).then((res) => {
        if (res.success) {
          if (res.data && res.data.length > 0) {
            this.fontFamily = res.data;
            this.family = this.family
              ? this.family
              : this.fontFamily[0].englishName;
            this.initFont(this.fontFamily[0], this.family);
            this.fontColor = this.colors.hex;
          } else {
            this.message = "";
            this.isLoading = false;
            this.$toast.fail(res.errMessage);
          }
        }
      });
    },
    getdpi(value, ratio) {
      this.dpiValue = value;
      this.ratio = ratio;
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
      }
    },
    getScale(width, height) {
      let phoneWidth = this.phoneWidth;
      if (this.isZFold) {
        phoneWidth = this.phoneWidth / 2;
      }
      return height / this.phoneHeight > width / phoneWidth
        ? width / phoneWidth
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
    changeSelect(item) {
      this.showPicture = false;
      this.getDrawSize();

      // 图片版权费
      this.copyrightCost = 0;
      if (item.copyrightCost) {
        this.copyrightCost = item.copyrightCost;
      }

      // type 1-普通图片 2-IP图片（不可编辑） 3-模板 4-贴纸
      if (this.isPrice) {
        this.imgData.posX = null;
        this.message = "载入中";
        this.isLoading = true;
        if (item.type === 4) {
          // 贴纸
          this.changeImage(
            item,
            item.originImage,
            item.id,
            item.type,
            2,
            "changePic"
          );
        } else {
          this.picId = item.id;
          this.picName = item.pictureName;
          // 素材/模板
          this.changeImage(
            item,
            item.originImage,
            item.id,
            item.type,
            "changePic"
          );
        }
      }

      // 切换图片，更新型号信息
      this.getBrand("changePic");
    },
    changeImage(item, url, id, type, cateType, gainType) {
      // cateType：1（支持多次上传）例：本地图片，2 贴纸
      this.isPic = false;
      if (type !== 4) {
        this.curPicType = type;
      }
      if (this.materialId !== 0) {
        this.wallpImg = url;
        if (type !== 4) {
          // 非贴纸
          this.picId = id;
        }
        if (type !== 1 || type !== 2) {
          this.picName = "";
        }
        // type 0-Canva 1-普通素材 2-IP图片（不可更改） 3-模板（不可更改） 4-贴纸
        this.picType = type;
        Promise.all([this.loadImage(url, type)])
          .then((img) => {
            let w = img[0].width;
            let h = img[0].height;
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
              centerX = window.screen.width / 2;
              if (this.isZFold) {
                centerX = (window.screen.width - this.phonenWidth / 2) / 2;
              }
              centerY = this.wheight / 2;
              this.imgData.sizeW = w / scale;
              this.imgData.sizeH = h / scale;
              this.imgData.posX = centerX - w / scale / 2;
              this.imgData.posY = centerY - h / scale / 2;
              // 手机中心点
              centeruX = this.phoneWidth / 2;
              centeruY = this.phoneHeight / 2;
              diffX = (this.phoneWidth - this.phonenWidth) / 2;
              if (this.isZFold) {
                diffX = (this.phoneWidth - this.phonenWidth / 2) / 2;
              }
              diffY = (this.phoneHeight - this.phonenHeight) / 2;
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

            // 贴纸
            if (type === 4) {
              this.imgData.posX = window.screen.width / 2 - 50;
              this.imgData.posY = this.wheight / 2 - 50;
              this.imgData.posuX = this.phonenWidth / 2 - 50;
              this.imgData.posuY = this.phonenHeight / 2 - 50;
              this.imgData.sizeW = 45;
              this.imgData.sizeH = 45;
              this.imgData.rotate = 0;
            }

            let imgObj = {
              x: this.imgData.posX,
              y: this.imgData.posY,
              ux: this.imgData.posuX,
              uy: this.imgData.posuY,
              url: img[0],
              width: this.imgData.sizeW,
              height: this.imgData.sizeH,
              initW: this.imgData.initW,
              initH: this.imgData.initH,
              // 最小宽高
              minW: type !== 4 ? this.imgData.sizeW : 20,
              minH:
                type !== 4
                  ? this.imgData.sizeH
                  : 20 / (this.imgData.sizeW / this.imgData.sizeH),
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

            if (!gainType) {
              // 非切换图片，例如：自定义上传图片
              this.message = "";
              this.isLoading = false;
            }
          })
          .catch((error) => {
            if (!gainType) {
              // 非切换图片，例如：自定义上传图片
              this.message = "";
              this.isLoading = false;
            }
            this.$toast("您手机当前的网络较差，图片加载失败，请重试~");
          });
      }
    },
    // 加载图片
    loadImage(url, type) {
      let img = new Image();
      img.crossOrigin = "anonymous";
      if ((url && url.indexOf("data:") === 0) || Number(type) === 0) {
        img.src = url;
      } else {
        img.src = url + "?t=" + new Date().getTime();
      }
      return new Promise((resolve) => {
        if (img.complete) {
          resolve(img);
        }
        img.onload = () => resolve(img);
      });
    },
    // 添加文字
    addText() {
      if (this.validateText()) {
        if (this.textData.posX === null) {
          // 获取中心坐标
          this.textData.posX = window.screen.width / 2;
          this.textData.posY = this.wheight / 2;
          this.textData.posuX = this.phonenWidth / 2;
          this.textData.posuY = this.phonenHeight / 2;
          this.textData.sizeW = 20;
          this.textData.sizeH = 20;
          this.textData.rotate = 0;
          if (this.isZFold) {
            this.textData.posX =
              (window.screen.width - this.phonenWidth / 2) / 2;
            this.textData.posuX = this.phonenWidth / 4;
          }
          this.index = this.index === null ? this.index : null;
        }
        let textObj = {
          x: this.textData.posX,
          y: this.textData.posY,
          ux: this.textData.posuX,
          uy: this.textData.posuY,
          url: "",
          width: this.textData.sizeW,
          height: this.textData.sizeH,
          initW: this.textData.sizeW,
          initH: this.textData.sizeH,
          minW: 10,
          minH: 10,
          fillstyle: this.fontColor,
          filltext: this.inputtext,
          fontsize: this.textData.sizeH,
          fontfamily: this.family,
          type: 2,
          picType: this.curPicType,
          rotate: this.textData.rotate,
          selected: true,
          index: this.index,
        };
        this.spriteArr = [];
        this.spriteArr.push(textObj);
        this.textDialog = false;
      }
    },
    getFamilyName(val) {
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

        this.$refs.mcanvas.draw();  // 重绘，以免删减文字错位
      }
    },
    // 清空文字
    clearText(type) {
      // 1--图片  2--文字 0--未选中
      if (type === 1) {
        this.imgData.posX = null;
        this.wallpImg = "";
        this.dpiValue = 0;
      } else if (type === 2) {
        this.inputtext = "";
        this.fontColor = this.colors.hex;
        this.textData.posX = null;
      } else if (type === 0) {
        this.imgData.posX = null;
        this.textData.posX = null;
        this.inputtext = "";
        this.fontColor = this.colors.hex;
      } else if (type === 4) {
        this.imgData.posX = null;
        this.textData.posX = null;
        this.inputtext = "";
        this.fontColor = this.colors.hex;
        this.dpiValue = 0;
      }
    },
    reset() {
      this.$refs.mcanvas.reset();
    },
    // 定制成功并跳转
    submit(image, generateImage) {
      // 保存定制数据
      let materialsName = this.colorName
        ? this.materialName + " - " + this.colorName
        : this.materialName;

      // 隐藏图层列表
      this.$refs.mcanvas.showHandleWrap = false;

      if (Number(this.picType === 2) && !this.isMake) {
        // 缓存IP图
        this.$api
          .post(this, api.handleCacheIP, {
            generateImage: generateImage,
            materialId: this.materialId,
            modelId: this.modelId,
            pictureId: this.picId,
            previewImage: image,
          })
          .then((res) => {
            console.log(res);
          })
          .catch((err) => {
            console.log(err);
          });
      }

      if (Number(this.distributorId) === 6175) {
        // 爱思，提交订单（第三方临时订单）
        this.handleCreateOrder(materialsName, image, generateImage);
      } else {
        // 加入购物车
        this.handleAddToShopcart(materialsName, image, generateImage);
      }
    },
    // 提交订单（第三方临时订单）
    handleCreateOrder(materialsName, image, generateImage) {
      let info = {
        image: image,
        generateImage: generateImage,
        manufactor: this.manufactor,
        materialId: this.materialId,
        material: materialsName,
        brandId: this.brandId,
        brandName: this.brandName,
        modelId: this.modelId,
        modelName: this.modelName,
        pictureId: this.picId,
        price: this.price,
        skuNo: this.skuNo,
        count: 1,
        totalPrice: this.price,
      };

      // 爱思定制 attach
      let aAttach = sessionStorage.getItem("aAttach");
      if (aAttach) {
        // 是否有传 attach（例：爱思定制）
        info.extendField = {
          attach: aAttach,
        };
      }

      this.$api
        .post(this, api.handleOrder, {
          orderDetail: info,
        })
        .then((res) => {
          if (res.success) {
            this.message = "提交成功";
            this.isLoading = true;
            let url = res.data.url;

            // 判断当前环境
            var ua = navigator.userAgent.toLowerCase();
            if (ua.match(/MicroMessenger/i) == "micromessenger") {
              // 微信浏览器
              wx.miniProgram.getEnv((res) => {
                if (res.miniprogram) {
                  window.location.href = url;
                } else {
                  window.location.href = url;
                }
              });
            } else {
              // 其他环境
              window.location.href = url;
            }
          } else {
            this.message = "";
            this.isLoading = false;
            this.$toast.fail(res.errMessage);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    // 加入购物车
    handleAddToShopcart(materialsName, image, generateImage) {
      let categoryId = 1;
      let categoryName = "手机壳";

      let info = {
        categoryId: categoryId,
        categoryName: categoryName,
        brandId: this.brandId,
        brandName: this.brandName,
        generateImage: generateImage,
        materialId: this.materialId,
        materialName: materialsName,
        modelId: this.modelId,
        modelName: this.modelName,
        pictureId: this.picId,
        pictureName: this.picName,
        previewImage: image,
        manufactors: this.manufactor,
      };
      this.$api
        .post(this, api.addToShopcart, {
          diy: info,
          itemCode: this.skuNo,
          itemCount: 1,
          itemType: 1, // 是否赠品：1 非赠品，2 赠品
          salePrice: this.price, // 价格
        })
        .then((res) => {
          if (res.success) {
            let pid = res.data.id;
            if (this.platform === "GF60006") {
              // 字节小程序
              tt.miniProgram.switchTab({
                url: "/pages/shopcart/shopcart?pid=" + pid,
              });
            } else if (this.platform === "GF60007" || this.platform === "GF60008") {
              // 支付宝小程序
              my.switchTab({
                url: "/pages/shopcart/shopcart?pid=" + pid,
              });
            } else if (Number(this.platform) === 28) {
              wx.miniProgram.navigateTo({
                url: "/pages/shopcart/shopcart?pid=" + pid,
              });
            } else {
              this.$router.push({ path: "/shopcart", query: { pid: pid } });
            }
            setTimeout(() => {
              this.message = "";
              this.isLoading = false;
            }, 2000);
          } else {
            this.message = "";
            this.isLoading = false;
            this.$toast.fail(res.errMessage);
          }
        })
        .catch((error) => {
          // 判断是否登录
          if (error.errCode === "B_AUTH_FAIL") {
            // 跳转小程序授权登录
            var enterParams = JSON.stringify({
              categoryId: categoryId,
              categoryName: encodeURI(categoryName),
              brandId: this.brandId,
              brandName: encodeURI(this.brandName),
              generateImage: generateImage,
              materialId: this.materialId,
              materialsName: encodeURI(materialsName),
              modelId: this.modelId,
              modelName: encodeURI(this.modelName),
              picId: this.picId,
              picName: encodeURI(this.picName),
              image: image,
              manufactor: this.manufactor,
              itemCode: this.skuNo,
              itemCount: 1,
              itemType: 1, // 是否赠品：1 非赠品，2 赠品
              salePrice: this.price, // 价格
            });
            if (this.platform === "GF60006") {
              // 字节小程序
              tt.miniProgram.navigateTo({
                url:
                  "/pages/login/login?enterFlag=shopcart&comingType=addShopCart&enterParams=" +
                  enterParams,
              });
            } else if (this.platform === "GF60007" || this.platform === "GF60008") {
              // 支付宝小程序
              my.navigateTo({
                url:
                  "/pages/login/login?enterFlag=shopcart&comingType=addShopCart&enterParams=" +
                  enterParams,
              });
            } else if (Number(this.platform) === 28) {
              wx.miniProgram.navigateTo({
                url:
                  "/pages/login/login?enterFlag=shopcart&comingType=addShopCart&enterParams=" +
                  enterParams,
              });
            }
            setTimeout(() => {
              this.message = "";
              this.isLoading = false;
            }, 2000);
          }
        });
    },
    // 上传图片
    picUpload(event) {
      let file = event.target.files[0];
      this.getUploadImg(file);
    },
    getUploadImg(file) {
      this.message = "载入中";
      this.isLoading = true;

      let reader = this.handleBeforeUpload(file);
      reader.addEventListener("load", () => {
        Promise.all([this.loadImage(reader.result)])
          .then((img) => {
            // let pic = this.handleImgRotate(img[0]); // 验证图片是否需要旋转
            let pic = this.handleImage(img[0]); // 不旋转图片
            this.uploadDialog = false;
            this.wallpImg = pic;
            this.imgData.posX = null;
            // id： 0-网络图片 1-普通图片 type: 1-普通素材 2-IP图 3-模板 4-贴纸
            this.changeImage({}, this.wallpImg, 0, this.curPicType, 1);
          })
          .catch((error) => {
            this.message = "";
            this.isLoading = false;
            this.$toast("您手机当前的网络较差，图片加载失败，请重试~");
          });
      });
    },
    // 不翻转
    handleImage(imgObj) {
      let img;
      let canvas = document.createElement("canvas");
      let ctx = canvas.getContext("2d");

      EXIF.getData(imgObj, function () {
        canvas.width = imgObj.width;
        canvas.height = imgObj.height;

        // 压缩图片（最小边>=1400）
        if (imgObj.width >= imgObj.height && imgObj.height >= 1400) {
          canvas.height = 1400;
          canvas.width = imgObj.width / (imgObj.height / 1400);
        }
        if (imgObj.height >= imgObj.width && imgObj.width >= 1400) {
          canvas.width = 1400;
          canvas.height = imgObj.height / (imgObj.width / 1400);
        }

        ctx.drawImage(imgObj, 0, 0, canvas.width, canvas.height);
        img = canvas.toDataURL("image/png");
      });
      return img;
    },
    // 翻转图片
    handleImgRotate(imgObj) {
      let img;
      let canvas = document.createElement("canvas");
      let ctx = canvas.getContext("2d");
      let orientation = null;

      EXIF.getData(imgObj, function () {
        // 获取某个数据方向参数
        orientation = EXIF.getTag(this, "Orientation");
        // 为6或8的时候，图片需宽高反转
        if (orientation && (orientation == 6 || orientation == 8)) {
          canvas.width = imgObj.height;
          canvas.height = imgObj.width;
        } else {
          canvas.width = imgObj.width;
          canvas.height = imgObj.height;
        }
        ctx.drawImage(imgObj, 0, 0, imgObj.width, imgObj.height);
        if (orientation) {
          // 为1，正常
          switch (Number(orientation)) {
            case 3: // 需要180度旋转
              ctx.rotate((180 * Math.PI) / 180);
              ctx.drawImage(
                imgObj,
                -imgObj.width,
                -imgObj.height,
                imgObj.width,
                imgObj.height
              );
              break;
            case 6: // 需要顺时针（向左）90度旋转
              ctx.rotate((90 * Math.PI) / 180);
              ctx.drawImage(imgObj, 0, 0, imgObj.width, -imgObj.height);
              break;
            case 8: // 需要逆时针（向右）90度旋转
              ctx.rotate((270 * Math.PI) / 180);
              ctx.drawImage(
                imgObj,
                -imgObj.height,
                0,
                imgObj.height,
                imgObj.width
              );
              break;
            default:
              ctx.drawImage(imgObj, 0, 0, imgObj.width, imgObj.height);
              break;
          }
        }
        img = canvas.toDataURL("image/png");
      });
      return img;
    },
    // 上传图片验证
    handleBeforeUpload(file) {
      let reader = new FileReader();
      if (
        file.type &&
        !(
          file.type === "image/png" ||
          file.type === "image/jpg" ||
          file.type === "image/jpeg"
        )
      ) {
        this.$toast.fail({
          title: "警告",
          message: "请上传格式为image/png, image/jpg, image/jpeg的图片",
        });

        this.message = "";
        this.isLoading = false;
        return;
      } else {
        reader.readAsDataURL(file);
      }
      let isLt10M = file.size / 1024 / 1024 < 10;
      if (!isLt10M) {
        this.$toast.fail("上传图片大小不能超过 10MB!");
        this.message = "";
        this.isLoading = false;
        return;
      }
      return reader;
    },
    removeText() {
      this.clearText();
      this.textDialog = false;
    },
    // 判断当前图片是否可用
    checkPic() {
      let flag = false;
      this.picCategory.forEach((item) => {
        if (item.picList && item.picList.length > 0) {
          item.picList.forEach((child) => {
            if (child.id == this.picId) {
              flag = true;
            }
          });
        }
      });
      return flag;
    },
    validateText() {
      if (this.family === "") {
        this.$toast("请选择字体");
        return false;
      }
      if (this.inputtext === "") {
        this.$toast("请输入文字");
        return false;
      }
      return true;
    },
    // 确认提交
    handleSubmit() {
      if (this.underStockFlag) {
        this.$toast("该机型缺货暂时无法下单");
        return;
      }

      if (this.modelId === "") {
        this.$toast.fail("请选择手机型号");
        return;
      }

      // 遍历canvas画布中所有图层，判断是否有图片
      let hasPic = false;
      if (this.$refs.mcanvas.dragArr && this.$refs.mcanvas.dragArr.length > 0) {
        let arr = this.$refs.mcanvas.dragArr;
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
            this.$toast.fail("信息异常, 请稍后重试！");
            return;
          }
          this.message = "作品制作中";
          this.isLoading = true;
          // 判断IP图片是否已经定制
          if (this.picType === 2) {
            this.isMake = false;
            // 获取是否已生成定制信息
            this.$api
              .get(this, api.getCacheIP, {
                pictureId: this.picId,
                materialId: this.materialId,
                modelId: this.modelId,
              })
              .then((res) => {
                if (res.success) {
                  if (
                    res.data &&
                    ((this.manufactor === "MK" &&
                      res.data.generateImage.indexOf(".pdf") > -1) ||
                      (this.manufactor === "LHW" &&
                        res.data.generateImage.indexOf(".png") > -1))
                  ) {
                    // pdf 或 png
                    let image = res.data.previewImage;
                    let generateImage = res.data.generateImage;
                    this.isMake = true;
                    this.submit(image, generateImage);
                  } else {
                    this.isMake = false;
                    this.$refs.mcanvas.make();
                  }
                } else {
                  this.message = "";
                  this.isLoading = false;
                  this.$toast.fail(res.errMessage);
                }
              });
          } else {
            this.$refs.mcanvas.make();
          }
        } else {
          this.$toast("请先完善数据！");
        }
      } else {
        this.$toast("请先上传图片！");
      }
    },
    // 图库
    openPic() {
      this.showPicture = !this.showPicture;
      if (this.showPicture) {
        this.curHeight = "70%";

        document.body.style.overflow = "hidden";
      } else {
        this.getDrawSize();
      }
    },
    // 贴纸
    openMap() {
      if (Number(this.curPicType) === 2) {
        this.$toast("正版图片不可编辑");
        return;
      }
      // 遍历canvas画布中所有图层，判断是否有图片
      let hasPic = false;
      if (this.$refs.mcanvas.dragArr && this.$refs.mcanvas.dragArr.length > 0) {
        let arr = this.$refs.mcanvas.dragArr;
        arr.forEach((item) => {
          if (Number(item.type) === 1 && Number(item.picType) !== 4) {
            // 有图片
            hasPic = true;
          }
        });
      }

      if (hasPic) {
        if (this.mapDialog) {
          this.mapDialog = false;
        } else {
          this.message = "数据加载中";
          this.isLoading = true;
          // 没有贴纸数据，请求贴纸数据
          this.$api
            .get(this, api.getMapCategory, {
              distributorId: this.distributorId,
              materialId: this.materialId,
              modelId: this.modelId,
              // exchangeId: this.exchangeId,
            })
            .then((res) => {
              if (res.success) {
                if (res.data && res.data.length > 0) {
                  this.mapCategory = res.data;

                  this.getMapData(res.data[0].id, 1);
                  this.mapDialog = true;

                  this.showMoreHandle = false;

                  this.showModel = false;
                  this.showMask = false;
                  this.showMaterial = false;
                  this.showPicture = false;
                  this.getDrawSize();
                  this.textDialog = false;
                  this.$refs.mcanvas.showHandleWrap = false;
                } else {
                  this.$toast("暂无贴纸数据~");
                }
              } else {
                this.$toast.fail(res.errMessage);
              }
              this.message = "";
              this.isLoading = false;
            });
        }
      } else {
        this.$toast("请先上传图片！");
      }
    },
    // 获取贴图数据
    getMapData(categoryId, page) {
      this.$api
        .get(this, api.getPicture, {
          distributorId: this.distributorId,
          categoryId: categoryId,
          page: page,
          size: 100,
          materialId: this.materialId,
          modelId: this.modelId,
          // exchangeId: this.exchangeId,
        })
        .then((res) => {
          this.message = "";
          this.isLoading = false;
          if (res.success) {
            this.mapData = res.data.list;
          } else {
            this.$toast.fail(res.errMessage);
          }
        });
    },
    // 选择分类
    chooseMapTab(index) {
      this.curIndex = index;
      let curCategory = this.mapCategory[index].id;
      this.getMapData(curCategory, 1);
    },
    // 文字
    openText(e) {
      if (Number(this.curPicType) === 2) {
        this.$toast("正版图片不可编辑");
        return;
      }
      if (Number(this.materialId) === 86) {
        this.$toast("当前材质暂不支持文字功能，如有疑问请联系客服~");
        return;
      }
      // 遍历canvas画布中所有图层，判断是否有图片
      let hasPic = false;
      if (this.$refs.mcanvas.dragArr && this.$refs.mcanvas.dragArr.length > 0) {
        let arr = this.$refs.mcanvas.dragArr;
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

        this.$refs.textWrapper.style.height = "auto";
        this.textDialog = true;

        this.showModel = false;
        this.showMask = false;
        this.showMaterial = false;
        this.showPicture = false;
        this.getDrawSize();
        this.mapDialog = false;
        this.$refs.mcanvas.showHandleWrap = false;

        this.clearText();
        this.spriteArr.forEach((item) => {
          if (item.selected && item.type === 0) {
            this.inputtext = item.filltext;
            this.family = item.fontFamily;
            this.fontColor = item.fontColor;
          }
        });

        this.showMoreHandle = false;
      } else {
        this.$toast("请先上传图片！");
      }
    },
    closeTextDialog() {
      this.textDialog = false;
      this.showSelct = false;
    },
    // 上传图片 - 提示
    chooseImg(uploadType) {
      if (uploadType && uploadType === "picture") {
        // 图库，上传图片隐藏图库
        this.swipedown();
      }

      if (Number(this.uploadValid) === 1) {
        // 统计已上传本地图片数量
        let count = 0;
        this.$refs.mcanvas.dragArr.forEach((item) => {
          if (Number(item.picId) === 0) {
            count++;
          }
        });
        if (count >= 3) {
          this.$toast("本地图片最多只支持上传3张哦~");
        } else {
          if (this.isFlag > 0) {
            this.$refs.file.value = ""; // 再次点击清空input="file"数据，以免同一图片不能重复上传
            this.$refs.file.dispatchEvent(new MouseEvent("click"));
          } else {
            this.$dialog
              .confirm({
                title: "温馨提示",
                message:
                  "尊敬的用户你好，请保证图片清晰可用，不可上传违反国家法律法规图片，对此造成的后果我方不予承担，印刷制作存在些许误差，介意请勿下单喔。",
                className: "confirm-v-dialog tl",
                confirmButtonText: "我知道了",
                cancelButtonText: "返回",
                confirmButtonColor: "#333333",
                cancelButtonColor: "#999999",
              })
              .then(() => {
                this.isFlag++;
                this.$refs.file.dispatchEvent(new MouseEvent("click"));

                this.showMoreHandle = false;
              })
              .catch((error) => {
                console.log(error);
              });
          }
        }
      } else {
        this.$toast("当前材质不支持图片上传");
      }
    },
    handleContent() {
      // 隐藏换图/编辑提示窗
      this.$refs.mcanvas.hasClickedArea = false;
    },
    // 材质
    handleMaterial(gainType) {
      if (!this.isLoading || gainType) {
        // 选择材质前材质信息
        this.originMaterial = {
          materialName: this.materialName,
          colorName: this.colorName,
        };

        this.showMaterial = true;
        this.showMask = true;

        this.showModel = false;
        this.showPicture = false;
        this.getDrawSize();
        this.textDialog = false;
        this.mapDialog = false;
        this.$refs.mcanvas.showHandleWrap = false;
      }
    },
    // 获取主题详情 - 介绍
    getDetailIntro(materialId, type) {
      this.$api
        .get(this, api.getMaterialDetail, {
          id: materialId,
        })
        .then((res) => {
          if (res.success) {
            this.detailIntro = res.data;

            if (res.data && res.data.content) {
              this.proDetail = res.data.content;
            }

            // 材质切换
            if (type && type === "material") {
              this.message = "";
              this.isLoading = false;
            }
          } else {
            this.message = "";
            this.isLoading = false;
          }
        });
    },
    // 获取当前材质id获取对应颜色
    getCurrentMaterial() {
      if (this.materialList && this.materialList.length > 0) {
        let curPid = 0;
        this.materialList.forEach((parent, pIndex) => {
          if (parent.childrenList && parent.childrenList.length > 0) {
            parent.childrenList.forEach((son) => {
              if (Number(son.materialId) === Number(this.materialId)) {
                // 当前颜色
                this.colorName = son.name;
                this.materialsColorValue = son.colour;
                // 当前工厂
                this.manufactor = son.manufactor;

                this.materialNo = son.materialNo;
                this.skuNo = son.itemCode;

                // 获取是否允许用户上传
                this.uploadValid = son.allowUploadFlag;
                // 是否强制铺满血位：1 是，0 否
                let isAllPlace = son.mandatoryCoveredBleedFlag;
                sessionStorage.setItem("isAllPlace", isAllPlace);

                // 是否缺货
                this.underStockFlag = son.underStockFlag;

                curPid = pIndex;
              }
            });
          }
        });

        // 当前材质
        this.materialName = this.materialList[curPid].name;
        // 颜色列表
        this.colorList = this.materialList[curPid].childrenList;
      }
    },
    // 选择材质
    handleChangeMaterial(val, gainType) {
      this.message = "数据加载中";
      this.isLoading = true;

      this.materialName = val;
      this.changeColorByMaterial(val, "material", gainType);
    },
    // 根据材质切换颜色
    changeColorByMaterial(materialName, type, gainType) {
      if (this.materialList && this.materialList.length > 0) {
        this.materialList.forEach((parent) => {
          if (parent.name === materialName) {
            if (parent.childrenList && parent.childrenList.length > 0) {
              // 当前颜色
              this.colorName = parent.childrenList[0].name;
              this.materialsColorValue = parent.childrenList[0].colour;
              // 当前工厂
              this.manufactor = parent.childrenList[0].manufactor;
              // 当前材质id
              this.materialId = parent.childrenList[0].materialId;

              this.materialNo = parent.childrenList[0].materialNo;
              this.skuNo = parent.childrenList[0].itemCode;

              // 获取是否允许用户上传
              this.uploadValid = parent.childrenList[0].allowUploadFlag;
              // 是否强制铺满血位：1 是，0 否
              let isAllPlace = parent.childrenList[0].mandatoryCoveredBleedFlag;
              sessionStorage.setItem("isAllPlace", isAllPlace);

              // 是否缺货
              this.underStockFlag = parent.childrenList[0].underStockFlag;

              this.getDetailIntro(this.materialId, type);

              if (gainType) {
                if (gainType === "modelType") {
                  // 选择型号不适用材质时
                  this.isData = false;
                  if (this.picId) {
                    // 清空图片
                    this.$refs.mcanvas.dragArr.forEach((item, index) => {
                      if (item.picId !== 0) {
                        this.$refs.mcanvas.dragArr.splice(index, 1);
                      }
                    });

                    this.picId = "";
                    this.picName = "";
                    this.wallpImg = "";
                    this.curPicType = 1;
                    this.$toast("材质属性已更变，请重新选择图片");
                  }
                  // 清空型号
                  this.modelId = "";
                  this.modelName = "";
                }

                if (gainType === "pictureType") {
                  // 选择图片不适用材质时
                  this.isData = false;
                  // 清空图片
                  this.$refs.mcanvas.dragArr.forEach((item, index) => {
                    if (item.picId !== 0) {
                      this.$refs.mcanvas.dragArr.splice(index, 1);
                    }
                  });

                  this.picId = "";
                  this.picName = "";
                  this.wallpImg = "";
                  this.curPicType = 1;
                }

                this.handleConfirmMaterial(gainType);
              }

              // 颜色列表
              this.colorList = parent.childrenList;
            }
          }
        });
      }
    },
    // 材质颜色
    handleChangeColor(item, index, gainType) {
      this.colorName = item.name;
      this.materialId = item.materialId;

      if (gainType) {
        if (gainType === "modelType") {
          // 选择型号不适用材质时
          this.isData = false;
          if (this.picId) {
            // 清空图片
            this.$refs.mcanvas.dragArr.forEach((item, index) => {
              if (item.picId !== 0) {
                this.$refs.mcanvas.dragArr.splice(index, 1);
              }
            });

            this.picId = "";
            this.picName = "";
            this.wallpImg = "";
            this.curPicType = 1;
            this.$toast("材质属性已更变，请重新选择图片");
          }
          // 清空型号
          this.modelId = "";
          this.modelName = "";
        }

        if (gainType === "pictureType") {
          // 选择图片不适用材质时
          this.isData = false;
          // 清空图片
          this.$refs.mcanvas.dragArr.forEach((item, index) => {
            if (item.picId !== 0) {
              this.$refs.mcanvas.dragArr.splice(index, 1);
            }
          });

          this.picId = "";
          this.picName = "";
          this.wallpImg = "";
          this.curPicType = 1;
        }

        this.handleConfirmMaterial(gainType);
      }
    },
    // 获取当前颜色获取对应材质 id
    getCurrentMaterialId(colorName, gainType) {
      if (this.materialList && this.materialList.length > 0) {
        this.materialList.forEach((parent) => {
          if (parent.childrenList && parent.childrenList.length > 0) {
            parent.childrenList.forEach((son) => {
              if (son.name === colorName) {
                this.materialsColorValue = son.colour;
                this.materialId = son.materialId;
                // 当前工厂
                this.manufactor = son.manufactor;

                this.materialNo = son.materialNo;
                this.skuNo = son.itemCode;

                // 获取是否允许用户上传
                this.uploadValid = son.allowUploadFlag;
                // 是否强制铺满血位：1 是，0 否
                let isAllPlace = son.mandatoryCoveredBleedFlag;
                sessionStorage.setItem("isAllPlace", isAllPlace);

                // 是否缺货
                this.underStockFlag = son.underStockFlag;

                // 重新获取型号
                let type = gainType ? gainType : "material";
                this.getBrand(type);
              }
            });
          }
        });
      }
    },
    // 跳过（材质选择）
    handleCancelMaterial() {
      this.showMaterial = false;
      this.showMask = false;
      this.materialName = this.originMaterial.materialName;
      this.colorName = this.originMaterial.colorName;
      this.changeColorByMaterial(this.materialName);
    },
    // 确定（材质选择）
    handleConfirmMaterial(gainType) {
      this.message = "数据加载中";
      this.isLoading = true;

      this.showMaterial = false;
      this.showMask = false;

      // 获取当前颜色获取对应材质 id
      this.getCurrentMaterialId(this.colorName, gainType);
    },
    // 机型
    handleModel(gainType) {
      if (!this.isLoading || gainType) {
        this.isFirstEnter = false;
        this.showModel = true;
        this.showMask = true;

        this.showMaterial = false;
        this.showPicture = false;
        this.getDrawSize();
        this.textDialog = false;
        this.mapDialog = false;
        this.$refs.mcanvas.showHandleWrap = false;
      }
    },
    // 跳过（机型选择）
    handleCancelModel() {
      this.showModel = false;
      this.showMask = false;
    },
    // 确定（机型选择）
    handleConfirmModel(val, gainType) {
      this.message = "数据加载中";
      this.isLoading = true;

      this.modelName = val.name;
      this.modelId = val.modelId;
      this.brandId = val.parentId;
      this.brandName = val.parentName;
      // 缓存机型id、机型名称
      sessionStorage.setItem("modelName", this.modelName);
      sessionStorage.setItem("modelId", this.modelId);
      // 缓存机型品牌id、品牌名称
      sessionStorage.setItem("brandId", this.brandId);
      sessionStorage.setItem("brandName", this.brandName);

      this.showModel = false;
      this.showMask = false;

      // 重置图片，文字
      this.imgData.posX = null;
      this.textData.posX = null;

      // gainType 获取来源
      if (gainType) {
        if (gainType === "materialType") {
          this.isData = false;
          // 选择材质不适用型号时
          this.materialId = ""; // 清空材质
          this.getMaterialList(gainType); // 获取当前选择机型适用材质
        } else if (gainType === "pictureType") {
          // 选择图片不适用型号时
          this.isData = false;
          // 清空图片
          this.$refs.mcanvas.dragArr.forEach((item, index) => {
            if (item.picId !== 0) {
              this.$refs.mcanvas.dragArr.splice(index, 1);
            }
          });

          this.picId = "";
          this.picName = "";
          this.wallpImg = "";
          this.curPicType = 1;
          this.getBrand(gainType); // 获取当前选择机型适用材质
        } else {
          // 第一次进入自动弹出机型（下一步）
          this.getInfo(); // 根据手机型号和材质获取手机定制信息
          this.getDetailIntro(this.materialId); // 获取材质详情

          // 获取图库分类
          this.$refs.pictureList.curIndex = 0; // 默认选择第一个分类
          this.getPicCategory(gainType);
        }
      } else {
        this.getMaterialList();
      }
    },
    // Loading
    handleLoading(isLoading, message) {
      this.message = message;
      this.isLoading = isLoading;
    },
    // 上滑
    swipeup(e) {
      this.showPicture = true;
      this.curHeight = "70%";

      document.body.style.overflow = "hidden";

      this.showModel = false;
      this.showMask = false;
      this.showMaterial = false;
      this.textDialog = false;
      this.mapDialog = false;
      this.$refs.mcanvas.showHandleWrap = false;
    },
    // 下滑
    swipedown(e) {
      this.showPicture = false;
      this.getDrawSize();
    },
    // 返回上一页
    handleBack() {
      this.$router.replace("/custom-intro");
    },
  },
  watch: {
    colors(value) {
      // 字体颜色
      this.fontColor = value.hex;
    },
    materialId(value) {
      // 材质ID
      this.materialId = value;
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
    isLoading(value) {
      // 设置 loading 时长，超出 10s 提示网络较差
      if (Number(value) === 1) {
        this.loadingTimer = setTimeout(() => {
          this.$toast("您手机当前的网络较差，请稍后再试~");
        }, 10000);
      } else {
        clearTimeout(this.loadingTimer);
      }
    },
  },
  components: {
    DrawContainer,
    Divider,
    Material,
    MaterialDetail,
    Model,
    Picture,
    Loading,
    NoData,
    "slider-picker": Slider,
  },
};
</script>

<style lang="stylus" scoped>
@import '~common/styles/variable.styl';
@import '~common/styles/mixin.styl';

.noclick {
  pointer-events: none;
}

.phone {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;

  .back.van-icon-arrow-left {
    position: fixed;
    top: $spacing-sm;
    left: $spacing-base;
    font-size: 20px;
    color: $color-bg-white;
    padding: 3px;
    background-color: rgbaBlack(0.3);
    border-radius: $radius-circle;
    z-index: 99;

    &::before {
      position: relative;
      left: -1px;
    }
  }

  .phone-t {
    background-color: $color-bg-white;
    border-radius: 0 0 $radius-xl $radius-xl;
  }

  .phone-wrapper {
    position: relative;
    overflow: hidden;

    &.noclick {
      pointer-events: none;
    }
  }

  .print-result {
    position: fixed;
    top: 150px;
    left: 40px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    width: 1em;
    height: 107px;
    font-size: $font-sm;
    color: $color-font-grey;
    line-height: 15px;

    .result {
      color: $color-orange;
    }
  }

  .handle-wrap {
    position: fixed;
    top: 276px;
    right: 0;
    height: 48px;
    z-index: 20;

    .handle-list {
      position: absolute;
      top: 0;
      right: 0;
      width: 60px;
      height: 48px;
      background-color: $color-main-lighter;
      border-radius: 48px 0 0 48px;
      transition: width 0.25s linear;

      > .sprite-icon {
        position: absolute;
        top: 50%;
        left: 16px;
        transform: translateY(-50%);
      }

      &.more {
        width: 200px;
      }

      &.miniMore {
        width: 140px;
      }
    }

    .more-handle {
      display: flex;
      height: 100%;
      padding: 0 $spacing-sm 0 38px;

      .van-icon-arrow {
        position: absolute;
        top: 50%;
        left: $spacing-lg;
        font-size: $font-base;
        transform: translateY(-50%);
      }

      .handle-item {
        flex: 1;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        align-items: center;
        position: relative;
        top: 2px;
        height: 40px;
        font-size: $font-sm;

        &.disable {
          color: $color-font-grey;
        }

        .icon-paster {
          top: 2px;
        }

        p {
          margin-top: 4px;
          transform: scale(0.9);
        }
      }
    }
  }

  .material-model {
    display: flex;
    padding: 0 $spacing-base 0 $spacing-lg;
    justify-content: space-between;

    li {
      display: flex;
      position: relative;
      width: 50%;
      lineHeight(16px);
      font-size: $font-base;
      color: $color-font-base;
    }

    p {
      no-wrap();
      align(right);
      width: 88%;
      display: inline-block;
    }

    .color {
      position: relative;
      top: 50%;
      display: inline-block;
      transform: translateY(-50%) scale(0.85);
    }

    .van-icon {
      top: 1px;
    }
  }

  .btn-wrap {
    padding: $spacing-lg $spacing-base 28px;

    .btn {
      font-size: $font-lg;
      align(center);
      lineHeight(45px);
      border-radius: $radius-xm;
    }

    .price {
      font-size: $font-base;
      color: $color-orange;

      i {
        margin-right: 2px;
        font-size: $font-sm;
        font-style: normal;
      }
    }
  }

  .mask {
    mask();
    z-index: 100;
  }

  .material-wrapper {
    mask();
    background-color: transparent;
    z-index: 101;

    .material {
      position: absolute;
      bottom: 0;
      left: 0;
      width: 100%;
      height: 614px;
      font-size: $font-base;
      line-height: 20px;
      border-radius: $radius-base $radius-base 0 0;
      background-color: $color-bg-white;
      overflow: hidden;
    }

    .title {
      position: relative;
      padding: 24px $spacing-base $spacing-base;
      align(center);
      background-color: $color-bg-white;
      border-radius: $radius-base $radius-base 0 0;
      z-index: 1;

      .title-c {
        font-size: $font-lg;
      }

      .title-l {
        float: left;
        color: $color-font-grey;
      }

      .title-r {
        float: right;
        font-weight: 500;
      }
    }

    .material-scroll {
      position: absolute;
      top: 54px;
      bottom: 0;
      width: 100%;
      overflow: scroll;
    }

    .material-intro {
      padding: 13px $spacing-base;
      background-color: $color-bg-white;
    }

    .material-wrap {
      padding: 0 $spacing-base 28px;
      background-color: $color-bg-white;
    }

    .divider-wrap {
      border-top: $spacing-sm solid $color-bg;
      padding: 28px 0;
      background-color: $color-bg-white;

      >>>.van-divider {
        margin: 0;
      }
    }
  }

  .img-wrap {
    line-height: 0;
    background-color: $color-bg-white;

    >>>img {
      width: 100%;
    }
  }

  .no-data {
    padding-bottom: 65px;
    background-color: $color-bg-white;
  }

  .model-wrapper {
    mask();
    background-color: transparent;
    z-index: 101;

    .model {
      position: absolute;
      bottom: 0;
      left: 0;
      width: 100%;
      height: 400px;
      background-color: $color-bg-white;
      border-radius: $radius-base $radius-base 0 0;
      overflow: hidden;
    }
  }

  .picture-wrapper {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    padding-top: $spacing-sm;
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
        box-shadow: $color-shadow;
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
      position: relative;
      top: -1px;
      height: 100%;
      background-color: $color-bg-white;
    }

    .handle-pic {
      position: relative;
      padding: 12px 0 1px;
      align(center);
      background-color: $color-bg-white;
      border-radius: $radius-xl $radius-xl 0 0;

      .line {
        position: relative;
        width: 46px;
        height: 22px;
        display: inline-block;

        &:after {
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
        background-image: url('../../common/images/icon_down_2.png');
        background-repeat: no-repeat;
        background-size: 18px 18px;
        background-position: center center;
        transform: rotate(180deg);
        animation: upAndDown 0.8s steps(1, start) infinite;
        z-index: 10;
      }

      @keyframes upAndDown {
        0%, 100% {
          background-image: url('../../common/images/icon_down_1.png');
        }

        50% {
          background-image: url('../../common/images/icon_down_2.png');
        }
      }

      .line-arrow-down {
        background: url('../../common/images/icon_down_1.png') no-repeat center center;
        background-size: 18px 18px;
      }
    }
  }

  .fadeDialog-enter-active, .fadeDialog-leave-active {
    transition: all 0.3s;
  }

  .fadeDialog-enter, .fadeDialog-leave-to {
    transform: translate3d(0, 100%, 0);
  }

  .text-wrapper {
    position: fixed;
    left: 0;
    right: 0;
    bottom: 0;
    padding: $spacing-lg $spacing-base;
    background-color: $color-bg-white;
    border-radius: $radius-lg $radius-lg 0 0;
    overflow-y: scroll;
    z-index: 101;
    -webkit-overflow-scrolling: touch;
    box-shadow: $color-shadow;

    &::-webkit-scrollbar {
      display: none;
    }

    .title {
      lineHeight(25px);

      .text {
        display: inline-block;
        font-size: $font-lg;
        color: $color-font-base;
        font-weight: 500;
      }

      .van-icon {
        display: inline-block;
        font-size: 25px;
        float: right;
      }
    }

    .input {
      width: 100%;
      margin-top: 30px;
      padding: 8px 16px;
      border-radius: 6px;
      box-sizing: border-box;
      border: none;
      background-color: rgba(155, 155, 155, 0.09);

      >>>.van-cell__value {
        lineHeight(20px);
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

      .select {
        width: 100%;
        margin-top: 20px;
        lineHeight(32px);
        padding: 0 $spacing-base;
        border-radius: $radius-sm;
        border: none;
        box-sizing: border-box;
        background-color: $color-bg-input;
        -webkit-appearance: none;
        -moz-appearance: none;
        appearance: non;
        cursor: pointer;
        outline: none;
      }

      .select-list {
        position: absolute;
        top: 40px;
        left: 0;
        width: 100%;
        padding: 0 $spacing-base;
        border: 1px solid $color-border;
        border-radius: $radius-xs;
        background-color: $color-bg-white;
        -webkit-box-shadow: 0 1px 6px 0 rgba(0, 0, 0, 0.1);
        box-shadow: 0 1px 6px 0 rgba(0, 0, 0, 0.1);
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
        float: left;
        z-index: 22;

        .select-item {
          lineHeight(32px);
        }
      }
    }

    .el-radio-group {
      display: flex;
      flex-wrap: wrap;
    }

    >>>.el-radio {
      position: relative;
      display: inline-block;
      width: 16.6%;
      margin: 28px 0 0 0;

      .el-radio__input {
        display: none;
      }

      .el-radio__label {
        padding: 0;
      }

      .color {
        display: inline-block;
        padding: 0;
        width: 18px;
        height: 18px;
        border-radius: 50%;
      }

      .iscolor {
        position: relative;
        display: inline-block;
        padding: 0;
        width: 18px;
        height: 18px;
        border-radius: 50%;

        &:after {
          content: '';
          position: absolute;
          left: 50%;
          top: 50%;
          width: 24px;
          height: 24px;
          border-radius: 50%;
          transform: translate3d(-50%, -50%, 0);
          border: 1px solid;
          border-color: inherit;
        }
      }
    }

    .color-box {
      width: 100%;
      margin: 40px 0 50px;
      display: flex;

      .color {
        display: inline-block;
        margin-top: 1px;
        width: 40px;
        height: 40px;
        border-radius: $radius-circle;
        box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.2);
      }

      .vc-slider {
        flex: 1;
        margin-left: 30px;

        >>>.vc-slider-hue-warp {
          height: 12px;
        }

        >>>.vc-slider-hue-warp, >>>.vc-slider-swatches {
          box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.2);
        }
      }
    }

    .btn-box {
      display: flex;
      flex-wrap: nowrap;
      align-content: flex-center;
      align-items: center;
      justify-content: space-around;
      margin-top: 28px;

      .btn {
        display: inline-block;
        width: 140px;
        lineHeight(36px);
        font-size: $font-base;
        text-align: center;
        border-radius: 36px;
      }
    }
  }

  .map-wrapper {
    position: fixed;
    right: 0;
    bottom: 0;
    left: 0;
    height: 350px;
    background-color: $color-bg-white;
    border-radius: $radius-lg $radius-lg 0 0;
    overflow-y: scroll;
    z-index: 101;
    -webkit-overflow-scrolling: touch;
    box-shadow: $color-shadow;
    overflow: hidden;

    &::-webkit-scrollbar {
      display: none;
    }

    .picture-category {
      position: relative;
      width: 100%;
      height: 100%;
      padding: 0 3px;
    }

    .no-data-wrap {
      position: relative;
      width: 100%;
      height: 100%;
    }

    .van-icon {
      position: absolute;
      top: $spacing-base;
      right: $spacing-lg;
      font-size: 25px;
      z-index: 1;
    }

    >>>.van-tabs__wrap {
      height: 53px;

      .van-tabs__nav {
        padding: $spacing-base 60px $spacing-sm $spacing-base;
        height: 28px;
      }

      .van-tab {
        position: relative;
        flex: inherit;
        padding: 0;
        font-size: $font-xm;
        color: $color-font-grey;

        & + .van-tab {
          margin-left: 30px;
        }
      }

      .van-tab--active {
        font-size: $font-lg;
        color: $color-font-base;
      }

      .van-tabs__line {
        position: absolute;
        bottom: 7px;
        width: 20px;
        height: 10px;
        bg-image('home-tab');
        background-color: transparent;
        background-size: 100% 100%;
      }
    }

    >>>.van-tab__pane {
      position: absolute;
      top: 53px;
      bottom: 60px;
      width: 100%;
    }

    .pic-list {
      position: relative;
      padding: $spacing-sm 7px 50px;
      width: 100%;
      height: 100%;

      .img-list {
        position: absolute;
        top: 0;
        right: 7px;
        bottom: 0;
        left: 7px;
        padding-top: $spacing-base;
        overflow-y: scroll;

        &::-webkit-scrollbar {
          display: none;
        }
      }

      .img-wrap {
        float: left;
        width: calc((100% / 4));
        padding: 7px;

        .img {
          width: 100%;
          height: 100%;
          border-radius: $radius-xs;
          overflow: hidden;
        }

        img {
          position: relative;
          left: 50%;
          min-width: 100%;
          height: 75px;
          transform: translateX(-50%);
        }
      }
    }
  }
}
</style>
