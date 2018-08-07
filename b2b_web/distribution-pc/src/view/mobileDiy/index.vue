<!--
 * @Author: yaowei
 * @Date: 2018-05-05 10:28:39
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-08 10:47:11
-->
<template>
  <div
    class="phone"
    ref="phoneWrapper"
    id="phone"
    :class="isLoading ? 'noclick' : ''"
  >
    <!-- <i
      class="el-icon-arrow-left back"
      @click="handleBack"
      v-show="!isMiniProgram"
    ></i> -->
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

      <!-- 材质/型号 -->
      <ul class="material-model">
        <li @click="handleModel">
          <p>{{ modelName }}</p>
          <i class="el-icon-arrow-right"></i>
        </li>
      </ul>

      <!-- 多图层/其他操作按钮 -->
      <div class="handle-wrap">
        <div class="handle-list">
          <!-- 传图 -->
          <div
            class="handle-item"
            :class="{ disable: Number(uploadValid) !== 1 }"
            @click="chooseImg"
          >
            <span class="sprite-icon icon-photo"></span>
            <input
              type="file"
              style="display: none; height: 0; width: 0"
              accept="image/*"
              id="picfile"
              ref="file"
              @change="picUpload($event)"
            />
          </div>
          <!-- 文字 -->
          <div class="handle-item" @click="openText">
            <span class="sprite-icon icon-text"></span>
          </div>
          <!-- 重置 -->
          <div class="handle-item" @click="reset">
            <span class="sprite-icon icon-reset"></span>
          </div>
        </div>
      </div>

      <div class="btn-wrap">
        <p class="btn bg-btn" :class="{'default': underStockFlag}" @click="make()">下一步</p>
      </div>
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

    <div class="mask" v-show="showMask"></div>

    <!-- 机型 -->
    <transition name="fadeDialog">
      <div class="model-wrapper" v-show="showModel">
        <Model
          class="model"
          :type="'bottom'"
          :mobile="modelName"
          :modelList="modelList"
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
        <!-- <span class="line line-pingpu" v-show="!showPicture"></span>
        <span class="line line-zheqi" v-show="showPicture"></span> -->
        <span class="line line-arrow-up" v-show="!showPicture"></span>
        <span class="line line-arrow-down" v-show="showPicture"></span>
      </div>
      <div class="picture-data" :class="isLoading ? 'noclick' : ''">
        <Picture
          ref="pictureList"
          :picData="picData"
          @changeSelect="changeSelect"
          v-if="picData && picData.length > 0"
        ></Picture>
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
          <i class="el-icon-close" @click="closeTextDialog"></i>
        </div>
        <el-input
          class="input"
          v-model="inputtext"
          placeholder="在此输入文字内容"
          @focus="focusInput"
          clearable
        ></el-input>
        <el-select v-model="family" placeholder="请选择">
          <el-option
            v-for="item in fontFamily"
            :key="item.id"
            :label="item.name"
            :value="item.englishName"
            @click="handleSelect(item)"
          ></el-option>
        </el-select>
        <div class="color-box">
          <div class="color" :style="{ backgroundColor: fontColor }"></div>
          <slider-picker v-model="colors" :swatches="swatches"></slider-picker>
        </div>
        <div class="btn-box">
          <div class="btn btn-cancel" @click="removeText">取消</div>
          <div class="btn btn-submit" @click="addText">确定</div>
        </div>
      </div>
    </transition>

    <!-- Loading -->
    <Loading v-show="isLoading" :message="message"></Loading>

    <!-- Toast -->
    <div class="toast" v-show="showToast">{{ toastMsg }}</div>

    <!-- 购物车 -->
    <div class="shopcart-wrap" @click="goShopCart">
      <span class="icon-shopping"></span>
      <span class="num" v-show="cartCount > 0"
        ><i>{{ cartCount }}</i></span
      >
    </div>

    <!-- 加入购物车/立即购买 -->
    <el-dialog
      class="custom-dialog-wrap"
      title="请确认定制信息"
      :close-on-click-modal="false"
      :visible.sync="diyDialogVisible"
      append-to-body
      @close="cancel"
    >
      <div class="dialog-content">
        <div class="left-img">
          <div class="img-wrap">
            <img
              :src="diyPic + '?x-oss-process=image/resize,h_300,l_300'"
              alt=""
            />
          </div>
        </div>
        <div class="right-cons">
          <div class="item">
            <span class="item-name">货品名称：</span>
            <span>{{ diyItems.itemName }}</span>
          </div>
          <div class="item">
            <span class="item-name">货品编码：</span>
            <span>{{ diyItems.itemCode }}</span>
          </div>
          <div class="item">
            <span class="item-name">型号：</span>
            <span>{{ diyItems.specValueName }}</span>
          </div>
          <div class="item">
            <span class="item-name">材质：</span>
            <span>{{ diyItems.materialValueName }}</span>
          </div>
          <div class="item">
            <span class="item-name">购买数量：</span>
            <el-input-number
              v-model="diyNum"
              :min="1"
              label="描述文字"
              size="mini"
              @keyup.native="numChange($event)"
              @change="handleChange"
            ></el-input-number>
          </div>
        </div>
      </div>
      <p v-show="isMaxNum" class="tips">
        订购数量过多，请先与客服联系，否则有色差问题不予退货。客服微信号：
        W-666357。
      </p>
      <div slot="footer" class="dialog-footer">
        <p class="btn border-btn" :loading="btnLoading" @click="diyJoinCat">
          加入购物车
        </p>
        <p class="btn bg-btn" @click="diyBuy">立即购买</p>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// 组件
import DrawContainer from "@/view/mobileDiy/components/diyCanvas/diyCanvas";
import Model from "@/view/mobileDiy/components/model/model";
import Picture from "@/view/mobileDiy/components/picture/picture";
import Loading from "@/view/mobileDiy/components/loading/loading";
// js/插件/vuex
import wx from "weixin-js-sdk";
import { Slider } from "vue-color";
import "../../common/js/vue-touch.js";
import "../../common/js/rem.js";
import EXIF from "exif-js";
// api
import {
  recordPoint,
  getMaterial,
  getModel,
  getPicture,
  getFont,
  getInfo,
  getCacheIP,
  handleCacheIP,
  addToShopcart,
} from "@/apiService/custom";
import {
  goodsDetails,
  priceItemList,
  shoppingcartList,
} from "@/apiService/api";
import { setToken } from "@/utils/auth";

export default {
  name: "MobileDiy",
  data() {
    return {
      isLoading: false,
      isFlag: 0,
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
      uploadDialog: false, // 上传图片弹框
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
      message: "", // loading提示
      error: "", // 错误提示信息
      exportUrl: "", // Canva 图片
      designId: "", // Canva 图片id
      smVersion: "",
      curPicType: 1, // 当前素材分类 - 1 自定义 2 IP图库 3 模板
      distributorId: 0, // 分销商ID
      platform: "", // 平台
      exchangeId: "", // 兑换码id
      uploadValid: 1, // 是否允许用户上传（0 不允许，1 允许）
      showMask: false, // 蒙版
      showModel: false, // 机型弹窗
      brandName: "", // 手机品牌
      brandId: 0, // 品牌ID
      modelId: "", // 型号ID
      modelName: "", // 手机型号
      modelList: [], // 型号列表
      materialId: "", // 材质ID
      materialNo: "", // 材质编码
      materialName: "", // 材质名
      materialList: [], // 材质列表
      colorName: "", // 材质颜色
      curHeight: 0, // 当前图库高度
      curCategory: 0, // 当前图库分类
      picData: [], // 图库图片
      textDialog: false, // 文字弹框
      showPicture: false, // 图库（上滑下滑）
      originMaterial: {}, // 选择材质前材质信息
      copyrightCost: 0, // 图片版权费
      isMiniProgram: false, // 是否是小程序
      isZFold: false, 
      showToast: false, // toast
      toastMsg: "", // toast 提示语
      goodsId: "", // 商品id
      itemId: "", // 货品id
      itemCode: "", // 货品编码
      diyPic: "", // 定制预览图
      diyNum: 1, // 定制数量
      diyItems: {}, // 定制信息
      isMaxNum: false, // 真彩创意壳（设置最大10个提示语）
      diyDialogVisible: false, // 加入购物车/立即购买弹窗
      btnLoading: false, // 是否点击加入购物车
      cartCount: 0, // 购物车数量
      itemWeight: 0, // 重量
      underStockFlag: false, // 是否缺货
    };
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      vm.message = "";
      vm.isLoading = false;
      vm._getQueryData();
    });
  },
  created() {
    // 记录数据埋点
    let hasRecord = sessionStorage.getItem("hasRecord");
    if (!hasRecord) {
      this.handleRecordData();
    }
  },
  mounted() {
    this.goodsId = this.$route.query.goodsId; // 商品id
    this.itemId = this.$route.query.itemId; // 货品id
    this.itemCode = this.$route.query.itemCode; // 货品编码

    // 获取 userId，存储
    let userId = this.$route.query.userId; // 用户id
    localStorage.setItem("userId", userId);
    // 获取 token
    let tokenVal = this.$route.query.tokenVal; // token
    setToken(tokenVal);

    // 画布宽高
    this.getDrawSize(1);
    // 获取商品详情
    this.getGoodsDetail();
    if (userId) {
      // 获取购物车商品数量
      shoppingcartList().then((res) => {
        if (res.success) {
          this.cartCount = res.data.length;
        }
      });
    }
  },
  activated() {
    // 画布宽高
    this.getDrawSize();
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
      recordPoint({
        source: this.platform,
        userId: userId,
        distributorId: this.distributorId,
        networkType: 1,
      }).then((res) => {
        if (res.success) {
          sessionStorage.setItem("hasRecord", 1);
        }
      });
    },
    // 画布宽高
    getDrawSize(type) {
      this.wwidth = window.screen.width;
      this.wheight = window.screen.width >= 375 ? 380 : 350;
      let phoneT = parseInt(window.getComputedStyle(this.$refs.phoneT).height);
      let sHeight = `${document.documentElement.clientHeight}`;
      let H = Number(sHeight) - phoneT - 10;
      if (type) {
        H = Number(sHeight) - phoneT - 10 - this.wheight;
      }
      this.curHeight = H;

      document.body.style.overflow = "auto";
    },
    // 初始化数据
    _getQueryData() {
      this.message = "数据加载中";
      this.isLoading = true;
      // 获取材质
      this.getMaterialList();
    },
    // 获取商品详情
    getGoodsDetail() {
      goodsDetails({
        id: this.goodsId,
        itemId: this.itemId,
      }).then((res) => {
        if (res.success) {
          let data = res.data;

          priceItemList({
            goodsItemIds: [
              {
                goodsId: this.goodsId, // 商品id
                itemId: data.goodsItems[0].id, // 货品id
              },
            ],
          }).then((res) => {
            this.$set(this.diyItems, "salePrice", res.data[0].salePrice); // 货品价格
          });

          this.$set(this.diyItems, "goodsId", data.id); // 商品id
          this.$set(this.diyItems, "goodsNo", data.goodsNo); // 商品编码
          this.$set(this.diyItems, "goodsName", data.goodsName); // 商品名称
          this.$set(this.diyItems, "diyType", data.diyType); // 定制类型
          this.$set(this.diyItems, "goodsType", data.goodsType); // 商品类型
          this.$set(this.diyItems, "itemName", data.goodsItems[0].itemName); // 货品名称
          this.$set(this.diyItems, "itemCode", data.goodsItems[0].itemCode); // 货品编码
          this.$set(this.diyItems, "barCode", data.goodsItems[0].barCode); // 条形码

          document.title = res.data.goodsName; // 商品名称（标题）
        } else {
          this.showToast = true;
          this.toastMsg = res.errMessage;
          setTimeout(() => {
            this.showToast = false;
          }, 2000);
        }
      });
    },
    // 字体选择
    handleSelect(item) {
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
    // 获取所有品牌型号
    getBrand() {
      let userId = localStorage.getItem("userId") || "";
      getModel({
        categoryId: 1, // 产品类型id：1 手机壳
        distributorId: userId,
        itemId: this.itemId, // 货品id
        materialId: this.materialId,
        pictureId: this.picId,
        platform: 1, // 平台：1 前台下单
        search: "", // 搜索型号
      }).then((res) => {
        if (res.success) {
          if (res.data && res.data.length > 0) {
            let dataArr = res.data;
            this.modelList = res.data;

            // 根据型号查询品牌并显示
            if (this.modelName !== "" && this.modelName !== undefined) {
              this.getModelId(this.modelName);
            } else {
              this.brandId = dataArr[0].modelId;
              this.brandName = dataArr[0].name;
              if (dataArr[0].childrenList.length > 0) {
                this.modelId = dataArr[0].childrenList[0].modelId;
                this.modelName = dataArr[0].childrenList[0].name;

                // 是否缺货
                this.underStockFlag = dataArr[0].childrenList[0].underStockFlag;
              }
            }
            // 根据手机型号和材质获取手机定制信息
            this.getInfo();
            if (this.modelName === "") {
              this.modelName = this.getModelNameById();
            }
          }
        } else {
          this.showToast = true;
          this.toastMsg = res.errMessage;
          setTimeout(() => {
            this.showToast = false;
          }, 2000);

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
            this.underStockFlag = this.modelList[0].childrenList[0].underStockFlag;

            this.showToast = true;
            this.toastMsg = "未匹配到当前手机型号，已展示默认机型";
            setTimeout(() => {
              this.showToast = false;
            }, 2000);
          }
          this.modelName = this.modelName;
        } catch (error) {
          this.showToast = true;
          this.toastMsg = error;
          setTimeout(() => {
            this.showToast = false;
          }, 2000);
        }
      }
    },
    // 获取材质列表
    getMaterialList() {
      let userId = localStorage.getItem("userId") || "";
      getMaterial({
        categoryId: 1, // 产品类型id：1 手机壳
        distributorId: userId,
        itemId: this.itemId, // 货品id
        modelId: this.modelId,
        pictureId: this.picId,
        platform: 1, // 平台：1 前台下单
      }).then((res) => {
        if (res.success) {
          if (res.data && res.data.length > 0) {
            this.isData = true;
            let dataArr = res.data;
            this.materialList = res.data;

            // 添加manufactor参数，及材质二级分类
            if (this.materialId) {
              this.getMaterialById(this.materialId);
            } else {
              this.materialName = dataArr[0].name;
              // 默认第一个
              if (
                dataArr[0].childrenList &&
                dataArr[0].childrenList.length > 0
              ) {
                this.colorName = dataArr[0].childrenList[0].name;
                this.materialId = dataArr[0].childrenList[0].materialId;
                this.materialNo = dataArr[0].childrenList[0].materialNo;
                this.skuNo = dataArr[0].childrenList[0].itemCode;
                this.materialsColorValue = dataArr[0].childrenList[0].colour;
                this.manufactor = dataArr[0].childrenList[0].manufactor;

                // 获取是否允许用户上传
                this.uploadValid = dataArr[0].childrenList[0].allowUploadFlag;
                // 是否强制铺满血位：1 是，0 否
                let isAllPlace =
                  dataArr[0].childrenList[0].mandatoryCoveredBleedFlag;
                sessionStorage.setItem("isAllPlace", isAllPlace);
              } else {
                this.materialId = dataArr[0].materialId;
                this.materialNo = "";
                this.skuNo = "";
                this.colorName = "";
                this.materialsColorValue = dataArr[0].colour;
                this.manufactor = dataArr[0].manufactor;
              }
            }
            // 获取所有品牌型号
            this.getBrand();
          } else {
            this.picData = [];
            this.materialId = 0;
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

            this.showToast = true;
            this.toastMsg = "暂无关联的材质数据";
            setTimeout(() => {
              this.showToast = false;
            }, 2000);
          }
        } else {
          this.isData = false;
          this.message = "";
          this.isLoading = false;

          this.showToast = true;
          this.toastMsg = res.errMessage;
          setTimeout(() => {
            this.showToast = false;
          }, 2000);
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
              this.materialName = item.name;
              this.materialNo = second.materialNo;
              this.skuNo = second.itemCode;
              this.colorName = second.name;
              this.materialsColorValue = second.colour;
              this.manufactor = second.manufactor;

              // 获取是否允许用户上传
              this.uploadValid = second.allowUploadFlag;
              // 是否强制铺满血位：1 是，0 否
              let isAllPlace = second.mandatoryCoveredBleedFlag;
              sessionStorage.setItem("isAllPlace", isAllPlace);

              flag = true;
            }
          });
        } else {
          if (item.pid === Number(id)) {
            this.materialName = item.name;
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
        this.materialId = this.materialList[0].childrenList
          ? this.materialList[0].childrenList[0].materialId
          : this.materialList[0].materialId;
        this.materialNo =
          this.materialList[0].childrenList.length > 0
            ? this.materialList[0].childrenList[0].materialNo
            : "";
        this.skuNo =
          this.materialList[0].childrenList.length > 0
            ? this.materialList[0].childrenList[0].itemCode
            : "";
        this.materialName = this.materialList[0].name;
        this.colorName = this.materialList[0].childrenList
          ? this.materialList[0].childrenList[0].name
          : "";
        this.materialsColorValue = this.materialList[0].childrenList
          ? this.materialList[0].childrenList[0].colour
          : this.materialList[0].colour;
        this.manufactor = this.materialList[0].childrenList
          ? this.materialList[0].childrenList[0].manufactor
          : this.materialList[0].manufactor;

        // 获取是否允许用户上传
        this.uploadValid = this.materialList[0].childrenList[0].allowUploadFlag;
        // 是否强制铺满血位：1 是，0 否
        let isAllPlace =
          this.materialList[0].childrenList[0].mandatoryCoveredBleedFlag;
        sessionStorage.setItem("isAllPlace", isAllPlace);
      }
    },
    getPicList() {
      let userId = localStorage.getItem("userId") || "";
      getPicture({
        distributorId: userId,
        materialId: this.materialId,
        materialNo: this.materialNo,
        modelId: this.modelId,
        productCategoryId: 1, // 产品类型id：1 手机壳
      }).then((res) => {
        if (res.success) {
          this.picData = res.data;

          let flag = this.checkPic();
          if (!flag && this.picId !== 0) {
            this.showToast = true;
            this.toastMsg = "当前图片不可用";
            setTimeout(() => {
              this.showToast = false;
            }, 2000);

            this.picId = 0;
            this.picName = "";
            this.imgData.posX = null;
            this.wallpImg = "";
            this.reset();
          } else {
            if (
              this.wallpImg !== "" &&
              this.picId !== 0 &&
              this.picType !== 0
            ) {
              this.changeImage(
                {},
                this.wallpImg,
                this.picId,
                this.picType,
                this.picName
              );
            } else {
              // 重绘
              this.$refs.mcanvas.initCanvas();
              if (Number(this.picType) === 2) {
                this.$refs.mcanvas.drawIpImage();
              } else {
                this.$refs.mcanvas.draw();
              }
            }
          }
        } else {
          this.showToast = true;
          this.toastMsg = res.errMessage;
          setTimeout(() => {
            this.showToast = false;
          }, 2000);
        }

        this.message = "";
        this.isLoading = false;
      });
    },
    // 根据手机型号和材质获取手机定制信息
    getInfo() {
      let isZFold = this.modelName.toUpperCase().indexOf("FOLD3") >= 0 ? 1 : 0;
      this.isZFold = isZFold;
      if (Number(sessionStorage.getItem("isZFold")) !== isZFold) {
        // 清空画布
        this.$refs.mcanvas.dragArr = [];
        this.$refs.mcanvas.clear();
      }
      sessionStorage.setItem("isZFold", isZFold);

      getInfo({
        modelId: this.modelId,
        materialId: this.materialId,
      }).then((res) => {
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

            // 获取重量
            this.itemWeight = this.modelInfo.weight;

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
              (this.modelInfo.cutType === 1 && this.modelInfo.slittingHeight) ||
              (this.modelInfo.cutType === 2 && this.modelInfo.crosscuttingWidth)
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

            if (this.materialId !== 0) {
              // 根据型号和材质获取图库
              this.getPicList();
            }
          }
        } else {
          this.message = "";
          this.isLoading = false;
          this.isData = false;

          this.showToast = true;
          this.toastMsg = res.errMessage;
          setTimeout(() => {
            this.showToast = false;
          }, 2000);
        }
      });
    },
    // 获取字体列表
    getFont() {
      getFont().then((res) => {
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

            this.showToast = true;
            this.toastMsg = res.errMessage;
            setTimeout(() => {
              this.showToast = false;
            }, 2000);
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
          this.changeImage(item, item.originImage, item.id, item.type, 2);
        } else {
          this.picId = item.id;
          this.picName = item.pictureName;
          // 素材/模板
          this.changeImage(item, item.originImage, item.id, item.type);
        }
      }
    },
    changeImage(item, url, id, type, cateType) {
      // cateType：1（支持多次上传）例：本地图片，2 贴纸
      if (type !== 4) {
        this.curPicType = Number(type);
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
        Promise.all([this.loadImage(url, type)]).then((img) => {
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
            if (this.manufactor === "YC" || this.manufactor === "MK") {
              diffX = (this.phoneWidth - this.phonenWidth) / 2;
              if (this.isZFold) {
                diffX = (this.phoneWidth - this.phonenWidth / 2) / 2;
              }
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
          this.isLoading = false;
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
        this.family = "";
        this.fontColor = this.colors.hex;
        this.textData.posX = null;
      } else if (type === 0) {
        this.imgData.posX = null;
        this.textData.posX = null;
        this.inputtext = "";
        this.family = "";
        this.fontColor = this.colors.hex;
      } else if (type === 4) {
        this.imgData.posX = null;
        this.textData.posX = null;
        this.inputtext = "";
        this.family = "";
        this.fontColor = this.colors.hex;
        this.dpiValue = 0;
      }
    },
    reset() {
      this.$refs.mcanvas.reset();
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
        Promise.all([this.loadImage(reader.result)]).then((img) => {
          // let pic = this.handleImgRotate(img[0]); // 验证图片是否需要旋转
          let pic = this.handleImage(img[0]); // 不旋转图片
          this.uploadDialog = false;
          this.wallpImg = pic;
          this.imgData.posX = null;
          // id： 0-网络图片 1-普通图片 type: 1-普通素材 2-IP图 3-模板 4-贴纸
          this.changeImage({}, this.wallpImg, 0, this.curPicType, 1);
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

        ctx.drawImage(imgObj, 0, 0, imgObj.width, imgObj.height);
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
        this.showToast = true;
        this.toastMsg = "请上传格式为image/png, image/jpg, image/jpeg的图片";
        setTimeout(() => {
          this.showToast = false;
        }, 2000);
        return;
      } else {
        reader.readAsDataURL(file);
      }
      let isLt10M = file.size / 1024 / 1024 < 10;
      if (!isLt10M) {
        this.showToast = true;
        this.toastMsg = "上传图片大小不能超过 10MB!";
        setTimeout(() => {
          this.showToast = false;
        }, 2000);
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
      this.picData.forEach((item) => {
        if (item.childrenList && item.childrenList.length > 0) {
          item.childrenList.forEach((child) => {
            if (child.pictureList && child.pictureList.length > 0) {
              child.pictureList.forEach((pic) => {
                if (Number(pic.id) === Number(this.picId)) {
                  flag = true;
                }
              });
            }
          });
        } else {
          if (item.pictureList && item.pictureList.length > 0) {
            item.pictureList.forEach((pic) => {
              if (Number(pic.id) === Number(this.picId)) {
                flag = true;
              }
            });
          }
        }
      });
      return flag;
    },
    validateText() {
      if (this.family === "") {
        this.showToast = true;
        this.toastMsg = "请选择字体";
        setTimeout(() => {
          this.showToast = false;
        }, 2000);

        return false;
      }
      if (this.inputtext === "") {
        this.showToast = true;
        this.toastMsg = "请输入文字";
        setTimeout(() => {
          this.showToast = false;
        }, 2000);

        return false;
      }
      return true;
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
    // 文字
    openText(e) {
      if (Number(this.curPicType) === 2) {
        this.showToast = true;
        this.toastMsg = "正版图片不可编辑";
        setTimeout(() => {
          this.showToast = false;
        }, 2000);

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
        this.showPicture = false;
        this.getDrawSize();
        this.$refs.mcanvas.showHandleWrap = false;

        this.clearText();
        this.spriteArr.forEach((item) => {
          if (item.selected && item.type === 0) {
            this.inputtext = item.filltext;
            this.family = item.fontFamily;
            this.fontColor = item.fontColor;
          }
        });
      } else {
        this.showToast = true;
        this.toastMsg = "请先上传图片！";
        setTimeout(() => {
          this.showToast = false;
        }, 2000);
      }
    },
    closeTextDialog() {
      this.textDialog = false;
    },
    // 上传图片 - 提示
    chooseImg() {
      if (Number(this.uploadValid) === 1) {
        // 统计已上传本地图片数量
        let count = 0;
        this.$refs.mcanvas.dragArr.forEach((item) => {
          if (Number(item.picId) === 0) {
            count++;
          }
        });
        if (count >= 3) {
          this.showToast = true;
          this.toastMsg = "本地图片最多只支持上传3张哦~";
          setTimeout(() => {
            this.showToast = false;
          }, 2000);
        } else {
          if (this.isFlag > 0) {
            this.$refs.file.value = ""; // 再次点击清空input="file"数据，以免同一图片不能重复上传
            this.$refs.file.dispatchEvent(new MouseEvent("click"));
          } else {
            this.$confirm(
              "尊敬的用户你好，请保证图片清晰可用，不可上传违反国家法律法规图片，对此造成的后果我方不予承担，印刷制作存在些许误差，介意请勿下单喔。",
              "温馨提示",
              {
                customClass: "confirm-v-dialog",
                confirmButtonText: "我知道了",
                cancelButtonText: "返回",
                confirmButtonColor: "#333333",
                cancelButtonColor: "#999999",
              }
            )
              .then(() => {
                this.isFlag++;
                this.$refs.file.dispatchEvent(new MouseEvent("click"));
              })
              .catch((error) => {
                console.log(error);
              });
          }
        }
      } else {
        this.showToast = true;
        this.toastMsg = "当前材质不支持图片上传";
        setTimeout(() => {
          this.showToast = false;
        }, 2000);
      }
    },
    // 机型
    handleModel() {
      if (!this.isLoading) {
        this.showModel = true;
        this.showMask = true;

        this.showPicture = false;
        this.getDrawSize();
        this.textDialog = false;
        this.$refs.mcanvas.showHandleWrap = false;
      }
    },
    // 跳过（机型选择）
    handleCancelModel() {
      this.showModel = false;
      this.showMask = false;
    },
    // 确定（机型选择）
    handleConfirmModel(val) {
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

      // 是否缺货
      this.underStockFlag = val.underStockFlag;

      this.showModel = false;
      this.showMask = false;

      // 重置图片，文字
      this.imgData.posX = null;
      this.textData.posX = null;

      this.getInfo();
    },
    // 下一步
    make() {
      if (this.underStockFlag) {
        this.showToast = true;
        this.toastMsg = "该机型缺货暂时无法下单！";
        setTimeout(() => {
          this.showToast = false;
        }, 2000);
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
            this.showToast = true;
            this.toastMsg = "信息异常, 请稍后重试！";
            setTimeout(() => {
              this.showToast = false;
            }, 2000);

            return;
          }
          this.message = "作品制作中";
          this.isLoading = true;
          // 判断IP图片是否已经定制
          if (this.picType === 2) {
            // 获取缓存IP图
            getCacheIP({
              materialId: this.materialId,
              modelId: this.modelId,
              pictureId: this.picId,
            }).then((res) => {
              if (res.success) {
                if (
                  res.data &&
                  (res.data.generateImage.indexOf(".pdf") > -1 ||
                    res.data.generateImage.indexOf(".png") > -1)
                ) {
                  // pdf 或 png
                  let imgUrl = res.data.previewImage;
                  let pdfUrl = res.data.generateImage;
                  this.isMake = true;
                  this.submit(imgUrl, pdfUrl);
                } else {
                  this.$refs.mcanvas.make();
                }
              }
            });
          } else {
            this.$refs.mcanvas.make();
          }
        } else {
          this.showToast = true;
          this.toastMsg = "请先完善数据！";
          setTimeout(() => {
            this.showToast = false;
          }, 2000);
        }
      } else {
        this.showToast = true;
        this.toastMsg = "请先上传图片！";
        setTimeout(() => {
          this.showToast = false;
        }, 2000);
      }
    },
    // 定制成功并跳转
    submit(imgUrl, pdfUrl) {
      let materialsName = this.colorName
        ? this.materialName + " - " + this.colorName
        : this.materialName;

      let flag = true;
      // IP图
      if (this.picType === 2 && !this.isMake) {
        // 缓存IP图
        handleCacheIP({
          generateImage: pdfUrl,
          materialId: this.materialId,
          modelId: this.modelId,
          pictureId: this.picId,
          previewImage: imgUrl,
        })
          .then((res) => {
            if (res.success) {
              flag = true;
            } else {
              flag = false;
            }
          })
          .catch((err) => {
            flag = false;
            console.log(err);
          });
      }
      if (flag) {
        this.diyItemCode = this.itemCode;
        this.info = {
          modelId: this.modelId,
          modelName: this.modelName,
          materialId: this.materialId,
          manufactor: this.manufactor,
          materialName: materialsName,
          brandId: this.brandId,
          brandName: this.brandName,
          pictureId: this.picId,
        };
        this.diyDialogVisible = true;
        this.message = "";
        this.isLoading = false;
        this.diyPic = imgUrl;
        this.diyPdf = pdfUrl;
        this.diyShopData();
      }
    },
    diyShopData() {
      this.showLoading = true;
      this.diyDialogVisible = true;

      this.$set(this.diyItems, "count", 0);
      this.$set(this.diyItems, "itemType", 1); // 是否赠品： 1 非赠品， 2 赠品
      this.$set(this.diyItems, "brandId", this.info.brandId);
      this.$set(this.diyItems, "diyPic", this.diyPic);
      this.$set(this.diyItems, "specValueName", this.info.modelName);
      this.$set(this.diyItems, "materialValueName", this.info.materialName);
      this.$set(this.diyItems, "manufactor", this.info.manufactor);
    },
    cancel() {
      this.diyDialogVisible = false;
      this.btnLoading = false;
      this.showLoading = false;
      this.message = "";
      this.isLoading = false;
    },
    diyBuy() {
      let shopOrderList = [];
      let diy = {
        brandId: this.info.brandId,
        brandName: this.info.brandName,
        categoryId: 1,
        categoryName: "手机壳",
        generateImage: this.diyPdf,
        materialId: this.info.materialId,
        materialName: this.info.materialName,
        modelId: this.info.modelId,
        modelName: this.info.modelName,
        pictureId: this.info.pictureId,
        pictureName: this.info.pictureId,
        previewImage: this.diyPic,
        manufactors: this.info.manufactor,
        openFlag: 1,
      };
      // 获取缓存定制货品信息
      let curDiyItem = this.diyItems;
      let goodsItem = {
        barCode: curDiyItem.barCode,
        diy: diy,
        diyType: curDiyItem.diyType,
        goodsId: curDiyItem.goodsId,
        goodsName: curDiyItem.goodsName,
        goodsNo: curDiyItem.goodsNo,
        goodsType: curDiyItem.goodsType,
        itemCode: curDiyItem.itemCode,
        itemCount: this.diyNum,
        itemId: this.itemId,
        itemName: curDiyItem.itemName,
        itemType: 1,
        openFlag: 1,
        promotions: [],
        salePrice: curDiyItem.salePrice,
        lastPrice: curDiyItem.salePrice,
        weight: this.itemWeight,
        zaiTuCount: 0,
        zaiKuCount: this.diyNum,
        conditionsId: "",
        isCheck: true,
        boxsList: [],
        count: this.diyNum,
        totalPrice: curDiyItem.salePrice * this.diyNum,
        onWayFlag: 1,
        reduceOrPresent: 1,
        reduceOrPresentId: "",
        presentCount: 0,
        goodsPromotionId: "no",
      };
      shopOrderList.push(goodsItem);

      this.diyDialogVisible = false;
      this.btnLoading = false;
      this.showLoading = false;
      this.message = "";
      this.isLoading = false;
      wx.miniProgram.navigateTo({
        url:
          "/pages/shoppingCart/confirmOrder?enterFlag=custom&enterParams=" +
          JSON.stringify(shopOrderList),
      });
    },
    diyJoinCat() {
      let product = {
        brandId: this.info.brandId,
        brandName: this.info.brandName,
        categoryId: 1,
        categoryName: "手机壳",
        generateImage: this.diyPdf,
        materialId: this.info.materialId,
        materialName: this.info.materialName,
        modelId: this.info.modelId,
        modelName: this.info.modelName,
        pictureId: this.info.pictureId,
        pictureName: this.info.pictureId,
        previewImage: this.diyPic,
        manufactors: this.info.manufactor,
      };
      this.btnLoading = true;
      addToShopcart({
        diy: product,
        diyType: this.diyItems.diyType,
        goodsId: this.diyItems.goodsId,
        goodsName: this.diyItems.goodsName,
        goodsNo: this.diyItems.goodsNo,
        goodsType: this.diyItems.goodsType,
        itemId: this.itemId,
        itemCode: this.itemCode,
        itemName: this.diyItems.itemName,
        itemCount: this.diyNum,
        itemType: this.diyItems.itemType,
        barCode: this.diyItems.barCode,
        weight: this.itemWeight,
        salePrice: this.diyItems.salePrice,
      })
        .then((res) => {
          if (res.success) {
            this.diyDialogVisible = false;
            this.btnLoading = false;
            this.showLoading = false;

            this.message = "";
            this.isLoading = false;
            if (this.$i18n.locale === "zh") {
              this.$message({
                message: "加入购物车成功",
                type: "success",
                customClass: "custom-message-box",
              });
              // 获取购物车商品数量
              shoppingcartList().then((res) => {
                if (res.success) {
                  this.cartCount = res.data.length;
                }
              });
            } else {
              this.$message({
                message: "Add to shopping cart successfully.",
                type: "success",
                customClass: "custom-message-box",
              });
            }
          } else {
            this.btnLoading = false;
            this.$message({
              message: res.errMessage,
              type: "warning",
              customClass: "custom-message-box",
            });
          }
        })
        .catch(() => {
          this.btnLoading = false;
        });
    },
    // 购物车
    goShopCart() {
      wx.miniProgram.switchTab({
        url: "/pages/shoppingCart/shoppingCart",
      });
    },
    // 监听键盘输入
    numChange(e) {
      let targetVal = e.target.value.replace(/[^0-9]/g, "");
      // 每次keyup时把值赋给 v-model
      let value = 0;
      if (!targetVal) {
        // 这里给undefined是因为不给值就会默认变为1，不会为空，导致用户输入不了别的数值
        value = undefined;
        this.diyNum = value;
      } else {
        value = targetVal;
        this.diyNum = value;
      }

      this.handleChange(value);
    },
    handleChange(value) {
      // 真彩创意壳判断数量是否大于等于10
      if (Number(this.materialsId) === 84 && value >= 10) {
        this.isMaxNum = true;
      } else {
        this.isMaxNum = false;
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
      this.textDialog = false;
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
  },
  components: {
    DrawContainer,
    Model,
    Picture,
    Loading,
    "slider-picker": Slider,
  },
};
</script>

<style scoped="scoped" lang='less'>
@import url("../../assets/less/variable.less");
@import url("../../assets/less/mixin.less");

.noclick {
  pointer-events: none;
}

.phone {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: @color-bg;

  .back.el-icon-arrow-left {
    position: fixed;
    top: @spacing-sm;
    left: @spacing-base;
    font-size: 2rem;
    color: @color-bg-white;
    padding: 0.3rem;
    background-color: rgba(0, 0, 0, 0.3);
    border-radius: @radius-circle;
    z-index: 99;

    &::before {
      position: relative;
      left: -0.1rem;
    }
  }

  .phone-t {
    background-color: @color-bg-white;
    border-radius: 0 0 @radius-xl @radius-xl;
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
    top: 15rem;
    left: 4rem;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    width: 1em;
    height: 10.7rem;
    font-size: @font-sm;
    color: @color-font-base;
    line-height: 1.5rem;

    .result {
      color: @color-main;
    }
  }

  .material-model {
    display: flex;
    padding: 0 @spacing-base 0 @spacing-lg;

    li {
      flex: 1;
      .lineHeight(1.6rem);
      .align(center);
      font-size: @font-base;
      color: @color-font-base;
    }

    p {
      display: inline-block;
    }
  }

  .handle-wrap {
    padding-top: 3rem;
    height: 5.6rem;

    .handle-list {
      display: flex;
      padding: 0 5.2rem;
      height: 5.6rem;
      justify-content: space-between;
    }

    .handle-item {
      display: flex;
      width: 5.6rem;
      height: 5.6rem;
      background-color: @color-main-lighter;
      border-radius: 50%;
      justify-content: center;
      align-items: center;

      .sprite-icon {
        width: 2.8rem;
        height: 2.8rem;

        &.icon-photo {
          background: url("../../assets/images/mobile/icon_photo.png") no-repeat;
          background-size: 100% 100%;
        }

        &.icon-text {
          background: url("../../assets/images/mobile/icon_text.png") no-repeat;
          background-size: 100% 100%;
        }

        &.icon-reset {
          background: url("../../assets/images/mobile/icon_reset.png") no-repeat;
          background-size: 100% 100%;
        }
      }
    }
  }

  .btn-wrap {
    display: flex;
    padding: 2.8rem @spacing-base @spacing-base;
    .lineHeight(4.6rem);
    overflow: hidden;

    .btn {
      flex: 1;
      font-size: @font-base;
      .align(center);

      &.bg-btn {
        color: @color-font-white;
        background-color: @color-main;
        border-radius: 4.6rem;
      }

      &.default {
        color: #999999;
        background-color: @color-bg;
      }
    }
  }

  .mask {
    .mask();
    z-index: 100;
  }

  .no-data {
    padding-bottom: 6.5rem;
    background-color: @color-bg-white;
  }

  .model-wrapper {
    .mask();
    background-color: transparent;
    z-index: 101;

    .model {
      position: absolute;
      bottom: 0;
      left: 0;
      width: 100%;
      height: 40rem;
      background-color: @color-bg-white;
      border-radius: @radius-base @radius-base 0 0;
      overflow: hidden;
      box-sizing: border-box;
    }
  }

  .picture-wrapper {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    padding-top: @spacing-sm;
    z-index: 100;
    transition: all 0.3s;

    /deep/ .pic-box {
      overflow-y: hidden;
    }

    &.shadow {
      transition: all 0.3s;

      .handle-pic {
        box-shadow: @color-shadow;
      }

      /deep/ .pic-box {
        overflow-y: scroll;
      }
    }

    .picture-data {
      position: relative;
      top: -0.2rem;
      height: 100%;
      background-color: @color-bg-white;
    }

    .handle-pic {
      position: relative;
      padding: 1.2rem 0 0;
      .align(center);
      background-color: @color-bg-white;
      border-radius: @radius-xl @radius-xl 0 0;

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
          border: 1px solid @color-border;
        }
      }

      .line-arrow-up {
        background-image: url('../../assets/images/mobile/icon_down_2.png');
        background-repeat: no-repeat;
        background-size: 18px 18px;
        background-position: center center;
        transform: rotate(180deg);
        animation: upAndDown 0.8s steps(1, start) infinite;
        z-index: 10;
      }

      @keyframes upAndDown {
        0%, 100% {
          background-image: url('../../assets/images/mobile/icon_down_1.png');
        }

        50% {
          background-image: url('../../assets/images/mobile/icon_down_2.png');
        }
      }

      .line-arrow-down {
        background: url('../../assets/images/mobile/icon_down_1.png') no-repeat center center;
        background-size: 18px 18px;
      }
    }
  }

  .fadeDialog-enter-active,
  .fadeDialog-leave-active {
    transition: all 0.3s;
  }

  .fadeDialog-enter,
  .fadeDialog-leave-to {
    transform: translate3d(0, 100%, 0);
  }

  .text-wrapper {
    position: fixed;
    left: 0;
    right: 0;
    bottom: 0;
    padding: @spacing-lg @spacing-base;
    background-color: @color-bg-white;
    border-radius: @radius-lg @radius-lg 0 0;
    overflow-y: scroll;
    z-index: 101;
    -webkit-overflow-scrolling: touch;
    box-shadow: @color-shadow;

    &::-webkit-scrollbar {
      display: none;
    }

    .title {
      .lineHeight(2.5rem);

      .text {
        display: inline-block;
        font-size: @font-lg;
        color: @color-font-base;
        font-weight: 500;
      }

      .el-icon-close {
        font-size: 2.5rem;
        float: right;
      }
    }

    .el-input,
    .input-text {
      width: 100%;
      margin-top: 3rem;
      border-radius: 0.8rem;
    }

    /deep/ .el-input__inner,
    /deep/ .van-cell__value {
      .lineHeight(3.6rem);
      border: none;
      background-color: rgba(155, 155, 155, 0.09);
    }

    .el-select {
      width: 100%;
      margin-top: @spacing-lg;
      border-radius: 0.8rem;
    }

    /deep/ .el-input__inner {
      .lineHeight(3.6rem);
      border: none;
      background-color: rgba(155, 155, 155, 0.09);
    }

    .el-radio-group {
      display: flex;
      flex-wrap: wrap;
    }

    /deep/ .el-radio {
      position: relative;
      display: inline-block;
      width: 16.6%;
      margin: 2.8rem 0 0 0;

      .el-radio__input {
        display: none;
      }

      .el-radio__label {
        padding: 0;
      }

      .color {
        display: inline-block;
        padding: 0;
        width: 1.8rem;
        height: 1.8rem;
        border-radius: 50%;
      }

      .iscolor {
        position: relative;
        display: inline-block;
        padding: 0;
        width: 1.8rem;
        height: 1.8rem;
        border-radius: 50%;

        &:after {
          content: "";
          position: absolute;
          left: 50%;
          top: 50%;
          width: 2.4rem;
          height: 2.4rem;
          border-radius: 50%;
          transform: translate3d(-50%, -50%, 0);
          border: 0.1rem solid;
          border-color: inherit;
        }
      }
    }

    .color-box {
      width: 100%;
      margin: 4rem 0 5rem;
      display: flex;

      .color {
        display: inline-block;
        margin-top: 0.1rem;
        width: 4rem;
        height: 4rem;
        border-radius: @radius-circle;
        box-shadow: 0.1rem 0.1rem 0.3rem rgba(0, 0, 0, 0.2);
      }

      .vc-slider {
        flex: 1;
        margin-left: 3rem;

        /deep/ .vc-slider-hue-warp {
          height: 1.2rem;
        }

        /deep/ .vc-slider-hue-warp,
        /deep/ .vc-slider-swatches {
          box-shadow: 0.1rem 0.1rem 0.3rem rgba(0, 0, 0, 0.2);
        }
      }
    }

    .btn-box {
      display: flex;
      flex-wrap: nowrap;
      align-content: flex-center;
      align-items: center;
      justify-content: space-around;
      margin-top: 3.6rem;

      .btn {
        display: inline-block;
        width: 14rem;
        font-size: @font-base;
        .align(center);
        .lineHeight(3.6rem);
        border-radius: 2rem;
        color: @color-bg-white;
      }

      .btn-cancel {
        background-color: rgba(0, 0, 0, 0.2);
      }

      .btn-submit {
        background: @color-main;
      }
    }
  }
}

.toast {
  position: fixed;
  top: 50%;
  left: 50%;
  padding: 0.8rem 1.6rem;
  max-width: 60%;
  font-size: @font-base;
  color: @color-font-white;
  line-height: 2rem;
  border-radius: @radius-base;
  background: rgba(0, 0, 0, 0.8);
  transform: translate(-50%, -50%);
  z-index: 1000;
}

.shopcart-wrap {
  position: fixed;
  display: flex;
  justify-content: center;
  align-items: center;
  right: 1.9rem;
  bottom: 13rem;
  width: 4.5rem;
  height: 4.5rem;
  background: @color-bg-white;
  box-shadow: 0 0.2rem 0.7rem 0 rgba(44, 44, 44, 0.12);
  border-radius: @radius-circle;
  z-index: 10000;

  .icon-shopping {
    width: 2.9rem;
    height: 2.9rem;
    background: url("../../assets/images/mobile/icon_shopping.png") no-repeat;
    background-size: 100% 100%;
  }

  .num {
    position: absolute;
    top: 0.8rem;
    right: 0.8rem;
    padding: 0 0.3rem;
    font-size: 1rem;
    color: @color-font-white;
    background: @color-main;
    border-radius: 1.5rem;
    transform: scale(0.85);
  }
}

/deep/ .custom-message-box {
  width: 90%;
  border-radius: @radius-xm;
  padding: 0;

  .el-message-box__header {
    padding-top: 1.8rem;
    font-size: @font-xl;
    color: @color-font-base;
  }

  .el-message-box__content {
    font-size: @font-base;
    color: @color-font-darkGrey;
    line-height: 2.4rem;
  }

  .el-message-box__btns {
    display: flex;
    padding: 0;

    .el-button {
      flex: 1;
      margin: 0;
      padding: 0;
      font-size: @font-lg;
      color: @color-font-grey;
      text-align: center;
      .align(center);
      .lineHeight(5rem);
      background-color: @color-bg-white;
      border-color: transparent;
      border-radius: 0;
      border-top: 0.1rem solid @color-bg;
    }

    .el-button--primary {
      color: @color-font-base;
      border-left: 0.1rem solid @color-bg;
    }
  }
}

.custom-dialog-wrap {
  /deep/ .el-dialog {
    top: 50%;
    left: 50%;
    margin: 0 !important;
    width: 90%;
    transform: translate(-50%, -50%);
    border-radius: @radius-xm;

    .el-dialog__header {
      text-align: center;

      .el-dialog__title {
        font-size: @font-base;
        line-height: @font-xl;
      }

      .el-dialog__headerbtn {
        font-size: @font-xl;
      }
    }

    .el-dialog__body {
      padding: 1.7rem @spacing-lg;

      .dialog-content {
        position: relative;
        height: 13rem;
      }

      .left-img {
        position: absolute;
        left: 0;
        padding: @spacing-sm;
        width: 10.3rem;
        height: 100%;
        background-color: @color-bg;
        border-radius: @radius-xs;
        box-sizing: border-box;

        .img-wrap {
          position: relative;
          width: 100%;
          height: 100%;
        }

        img {
          position: absolute;
          top: 50%;
          left: 50%;
          max-width: 100%;
          max-height: 100%;
          transform: translate(-50%, -50%);
        }
      }

      .right-cons {
        padding-left: 11.3rem;
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
      }

      .item {
        font-size: @font-sm;
        line-height: 1.6rem;
      }

      .item-name {
        color: @color-font-grey;
      }
    }

    .dialog-footer {
      display: flex;
      .align(center);

      .btn {
        flex: 1;
        .lineHeight(4rem);
        font-size: @font-lg;
        border-radius: 4rem;
        box-sizing: border-box;

        &.bg-btn {
          color: @color-font-white;
          background-color: @color-main;
          border-radius: 0 4.6rem 4.6rem 0;
        }

        &.border-btn {
          color: @color-main;
          border: 0.1rem solid @color-main;
          border-radius: 4.6rem 0 0 4.6rem;
        }
      }
    }
  }

  /deep/ .el-input-number {
    width: 11.5rem;

    .el-input-number__decrease,
    .el-input-number__increase {
      background-color: transparent;
    }

    .el-input-number__decrease {
      border-right: 0.1rem solid @color-input-border;
    }

    .el-input-number__increase {
      border-left: 0.1rem solid @color-input-border;
    }

    .el-input__inner {
      border: 0.1rem solid @color-input-border;
      border-radius: 0;
      font-size: @font-lg;
      color: @color-font-base;
    }
  }
}
</style>
