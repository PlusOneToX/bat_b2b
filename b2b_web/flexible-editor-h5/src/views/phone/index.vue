<template>
  <div class="phone" :class="isLoading ? 'noclick' : ''" id="phone">
    <div class="content-wrapper">
      <!---手机定制面板---->
      <div
        class="phone-wrapper"
        ref="phoneWrapper"
        :style="{ height: wheight + 'px' }"
      >
        <!--        <canvas-drag ref="mCanvas" @submit="submit" @clear="clearText" @select="selectText" @dpi="getdpi" :delIcon="delIcon" :scaleIcon="scaleIcon" :spriteArr="spriteArr" :manufactor="manufactor" :mw="mw" :mh="mh" :bgimg="phoneImg" :upimg="frameImg" :pwidth="phoneWidth" :pheight="phoneHeight" :wheight="wheight"></canvas-drag>-->
        <canvas-drag
          ref="mCanvas"
          @submit="submit"
          @clear="clearText"
          @select="selectText"
          @dpi="getdpi"
          @chooseImg="openTips"
          :materialsColorValue="materialsColorValue"
          :delIcon="delIcon"
          :scaleIcon="scaleIcon"
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
          :wheight="wheight"
          :pwidth2="phoneWidth"
          :pheight2="phoneHeight"
          :curPicType="curPicType"
        ></canvas-drag>
        <!---材质颜色属性----->
        <!--        <div class="color-icon" v-show="showColorIcon" @click="openColor"></div>-->
      </div>
      <!---手机型号材质选择---->
      <div class="brand-type" ref="brandType">
        <div class="item dpi-type" :class="getDpiValue(dpiValue)">
          <span class="title">打印效果：</span>
          <el-rate
            v-model="dpiRate"
            :colors="['#FF5061', '#FCDF4F', '#1EC15F']"
            disabled
          >
          </el-rate>
          <span class="name" v-if="dpiRate > 0"
            >(<i class="dpi-desc" :style="'color: ' + dpiColor">{{ dpiDesc }}</i
            >)</span
          >
        </div>
        <div class="item phone-brand">
          <span class="title">修改机型：</span
          ><span class="name" @click="modelDialog">{{ brand }}</span>
        </div>
        <div class="item phone-type">
          <span
            class="name"
            :class="materialData.length > 1 ? 'border-bottome' : ''"
            @click="materialDialog"
            >{{ materialsName }}<span v-show="colorName">-</span>
            {{ colorName }}</span
          >
        </div>
      </div>
    </div>
    <!---tab栏---->
    <div class="nav-wrapper" ref="navWrapper">
      <div class="nav-list">
        <div class="nav-item" @click="openPic">
          <span class="icon icon-pics"></span>
          <div class="text">图库</div>
        </div>
        <div
          class="nav-item"
          v-show="!(Number(uploadValid) !== 1) && !hiddenUpload"
          @click.prevent="openTips('showPic')"
        >
          <input
            type="file"
            style="display: none; height: 0; width: 0"
            accept="image/*"
            ref="file"
            @change="picUpload($event)"
          />
          <span class="icon icon-upload"></span>
          <div class="text">图片</div>
        </div>
        <div
          class="nav-item"
          v-show="!(Number(materialsId) === 86 || Number(curPicType) === 2)"
          @click="openText"
        >
          <span class="icon icon-text"></span>
          <div class="text">文字</div>
        </div>
        <div class="nav-item" v-show="showTemp" @click="handleTemp">
          <span class="icon icon-temp"></span>
          <div class="text">模板</div>
        </div>
      </div>
      <div
        class="btn-submit"
        :class="{ disable: underStockFlag }"
        @click="openSubmit"
      >
        <div
          class="price"
          v-show="distributorId !== '3696' && distributorId !== '4478'"
        >
          ¥ {{ price }}
        </div>
        <div class="text">{{ underStockFlag ? "缺货" : "设计完成" }}</div>
      </div>
    </div>
    <!---添加文字---->
    <transition name="fadeText">
      <div class="text-wrapper modalDialog" v-show="textDialog">
        <div class="title">
          <span class="line"></span>
          <div class="text">添加文字</div>
          <i class="icon el-icon-close" @click="close"></i>
        </div>
        <el-input
          v-model="inputtext"
          placeholder="在此输入文字内容"
          clearable
        ></el-input>
        <el-select v-model="family" placeholder="请选择">
          <el-option
            v-for="item in fontFamily"
            :key="item.id"
            :label="item.name"
            :value="item.englishName"
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
    <!---图库---->
    <transition name="fadePic">
      <div class="pic-wrapper modalDialog" v-if="picDialog">
        <!--        <v-header :back="true" :title="'图库'" @back="closePic"></v-header>-->
        <div class="title">
          <div class="text">图库</div>
          <i class="icon icon-close" @click="closePic"></i>
        </div>
        <picture-list
          ref="picList"
          v-show="picData"
          :picData="picData"
          :showPicName="false"
          @change="change"
        ></picture-list>
      </div>
    </transition>
    <!---图库---->
    <transition name="fadePic">
      <div class="pic-wrapper modalDialog" v-if="tempDialog">
        <div class="title">
          <div class="text">模板</div>
          <i class="icon icon-close" @click="closeTemp"></i>
        </div>
        <picture-list
          ref="picList"
          v-show="tempData"
          :picData="tempData"
          :showPicName="false"
          @change="change"
        ></picture-list>
      </div>
    </transition>
    <!---选择颜色属性---->
    <!--<transition name="fadeColor">
      <div class="color-wrapper modalDialog" id="colorWrapper" ref="textWrapper" v-show="colorDialog">
        <div class="title">
          <span class="line"></span>
          <div class="text">颜色选择</div>
          <i class="icon icon-close" @click="closeColor"></i>
        </div>
        <div class="color-list">
          <el-radio-group v-model="materialColor">
            <el-radio :label="item.id" v-for="(item, idx) in materialColorList" :key="idx">
              <span class="radio-icon" :style="(materialColor===item.id)?'border:2px solid ' + item.colour: ''">
                  <span class="radio-icon-r" :style="'background-color:' + item.colour"></span>
              </span>
            </el-radio>
          </el-radio-group>
        </div>
        <div class="btn-box">
          <div class="btn btn-cancel" @click="closeColor">取消</div>
          <div class="btn btn-submit" @click="submitColor">确定</div>
        </div>
      </div>
    </transition>-->
    <!-- 上传图片提示 - 4478 -->
    <div class="image-tips" v-show="tipsDialog">
      <h6 class="title">温馨提示</h6>
      <p class="content">
        尊敬的用户你好，请保证图片清晰可用，不可上传违反国家法律法规图片，对此造成的后果我方不予承担，印刷制作存在些许误差，介意请勿下单喔。
      </p>
      <div class="btn-wrap">
        <div class="btn" @click="closeTips">返回</div>
        <div class="btn btn-confirm" @click="closeTips">
          <input
            class="input-file"
            type="file"
            accept="image/*"
            @change="picUpload($event, 'showHandle')"
          />
          我知道了
        </div>
      </div>
    </div>
    <!---上传图片---->
    <transition name="fadeImage">
      <div
        class="image-wrapper modalDialog"
        v-show="uploadDialog"
        @touchmove.prevent
      >
        <div class="title">
          <div class="line"></div>
          <div class="text">图片选择</div>
          <i class="icon el-icon-close" @click="closeUp"></i>
        </div>
        <div class="photograph" v-show="upVersion">
          <input
            type="file"
            accept="image/*"
            capture="camera"
            ref="photoFile"
            id="photofile"
            @change="takePhoto($event)"
          />
          <label for="photofile">
            <div class="text">拍照<i class="icon icon-camera"></i></div>
          </label>
        </div>
        <div class="photo">
          <input
            type="file"
            accept="image/photoalbum"
            id="picfile2"
            ref="file2"
            @change="picUpload($event)"
          />
          <label for="picfile2">
            <div class="text">从手机相册选择<i class="icon icon-pic"></i></div>
          </label>
        </div>
      </div>
    </transition>
    <!---修改材质弹出框---->
    <awesome-picker
      class="phone-picker"
      ref="materialPicker"
      textTitle="请选择材质、颜色"
      :data="materialData"
      @confirm="handleMaterialConfirm"
    ></awesome-picker>
    <!---选择手机型号弹出框---->
    <awesome-picker
      class="phone-picker"
      ref="modelPicker"
      textTitle="选择手机型号"
      :data="modelData"
      @confirm="handleModelConfirm"
    ></awesome-picker>
    <!-- 禁用蒙版点击事件 "mask", on:{click:t.hide} -->
    <!---确认弹出框---->
    <el-dialog
      :visible.sync="submitDialogVisible"
      title="请确认商品定制信息"
      center
      width="90%"
      class="material-dialog confirm"
      @confirm="make"
    >
      <div class="content" @touchmove.prevent>
        <div class="item-content no-shadow">
          <!--          <img class="item-img" :src="materialsImage" alt />-->
          <div class="item-box">
            <div class="name xs">
              材质：<span class="text"
                >{{ materialsName }} <span v-show="colorName">-</span>
                {{ colorName }}</span
              >
            </div>
            <div class="dec">
              <span class="left">型号：</span
              ><span class="text">{{ brand }}</span>
            </div>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="submitDialogVisible = false">返回设计</el-button>
        <el-button type="primary" @click="make">生成预览</el-button>
      </span>
    </el-dialog>
    <div
      ref="mask"
      id="mask"
      class="mask"
      v-show="mask"
      @click.stop="closeMask"
    ></div>
    <div class="error">{{ error }}</div>
	
	
	<!-- 柔性关闭弹窗 -->
	<div class="flexible-dialog" v-show="showFlexible">
	  <div class="flexible-dialog-content">
	      <div class="flexible-cotent-top">
	      	<img class="flexible-cotent-top-img" src="../../common/images/flexible-logo.png" mode="widthFix"/>
	      </div>
		  
		  <div class="flexible-cotent-middle-title">
		  	 通知提醒
		  </div>
		  <div class="flexible-cotent-middle-info">
		  	 尊敬的客户，本商城已于6月30日停止运营，仅保留订单查询服务。感谢您的信任和支持。
		  </div>
		  <div class="flexible-cotent-bottom" @click="clickFlexible">
		  	 知道了
		  </div>
	  </div>
	</div>
	
    <Loading v-show="isLoading" :message="message"></Loading>
  </div>
</template>

<script type="text/ecmascript-6">
import { Slider } from "vue-color";
import MobileDetect from "common/js/lib/mobile-detect.min.js";
import { getRenderer } from "common/js/lib/renderer.min.js";
import { getModel } from "common/js/iphone-device.js";

import api from "api/allApi.js";

// 使用Vue.mixin的方法拦截了路由离开事件，并在该拦截方法中实现了销毁页面缓存的功能
Vue.mixin({
  beforeRouteLeave: function (to, from, next) {
    if (to.path === "/preview") {
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
  name: "phone",
  data() {
    return {
      title: "定制",
      dtitle: "温馨提示",
      text: "1.请不要上传违反国家法律法规、精度不足背景虚化的图片， 由此产生的后果我方不予承担",
      text2: "2.印刷制作时都会有一定色差，如介意请勿下单。",
      lenw: "",
      lenh: "",
      len2w: "",
      len2h: "",
      mobile: "", // 自动获取的机型
      show: false,
      showMsg: false,
      isPic: false,
      isBack: false,
      isFlag: 0,
      userNo: "",
      dialogVisible: false,
      platform: "", // 平台
      distributorId: 0, // 分销商ID
      distributor: "", // 分销商名字
      inputtext: "", // 添加文字
      fontFamily: [], // 字体列表
      colors: {
        hex: "#194d33",
        hsl: { h: 150, s: 0.5, l: 0.2, a: 1 },
        hsv: { h: 150, s: 0.66, v: 0.3, a: 1 },
        rgba: { r: 25, g: 77, b: 51, a: 1 },
        a: 1,
      },
      swatches: ["100", ".80", ".65", ".50", ".35", ".20", "0"], // 颜色板
      family: "", // 当前字体
      fontColor: "",
      categoryDialog: false, // 切换商品
      textDialog: false, // 文字弹框
      colorDialog: false, // 颜色弹框
      picDialog: false, // 图库弹框
      uploadDialog: false, // 上传图片弹框
      sxPicsDialog: false, // 图片列表弹框
      submitDialogVisible: false, // 确认弹框
      picData: [],
      showPicName: false, // 是否显示图片名称
      isLoading: false,
      isData: true, // 检难数据是否完善
      count: 30,
      materialColorList: [], // 材质颜色属性列表
      materialColor: 0, // 材质颜色ID
      showColorIcon: false, // 材质颜色图标
      modelinfo: [],
      modelData: [], // 品牌型号列表
      materialData: [], // 材质列表
      anchor: [],
      materialList: [], // 材质列表，原始数据
      brandId: 0, // 品牌ID
      modelId: "", // 型号ID
      materialsId: 0, // 材质ID
      materialsType: 0, // 材质类型
      manufactor: "", // 第三方工厂,上传类型：LWH-图片  YC-pdf
      brand: "", // 手机品牌和型号
      brandname: "", // 手机品牌名
      modelName: "", // 手机型号名
      materialsName: "", // 手机材质名
      materialsColorValue: "", // 手机材质颜色值
      materialsImage: "", // 材质图片
      colorName: "", // 手机材质颜色属性名
      picId: 0, // 图片ID
      picName: "", // 图片名称
      picUrl: "", // 图片地址
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
      mask: false, // 遮罩
      isMake: false, // IP图是否已有
      isPrice: true, // 是否有价格
      showSelct: false,
      showPicker: false,
      columns: [],
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
      dpiColor: "", // dpi颜色
      dpiFlag: false,
      ratio: 1, // 缩放比
      skuNo: 0, // skuNo
      skuName: "",
      message: "", // loading提示
      error: "", // 错误提示信息
      upVersion: false,
      delIcon:
        "data:image/png;base64,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",
      scaleIcon:
        "data:image/png;base64,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",
      underStockFlag: false, //是否缺货
      uploadValid: 1, // 是否允许用户上传（0 不允许，1 允许）
      copyrightCost: 0, // 图片版权费
      isBack: false,
      isZFold: false, 
      tipsDialog: false, // 温馨提示
      curPicType: 0, // 当前图片类型
      hasClickedPic: false, // 是否点击图片按钮
      // 模板
      tempDialog: false,
      tempData: [],
      showTemp: false, // 是否显示模板
      hiddenUpload: false, // 是否隐藏上传
	  
	  showFlexible: false ,//是否显示柔性关闭弹窗
    };
  },
  beforeCreate() {
    this.message = "数据加载中";
    this.isLoading = true;
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      if (from.path === "/preview") {
        vm.isBack = true;
        vm.isLoading = false;
        vm.message = "";
      }
    });
  },
  created() {
    this.message = "数据加载中";
    this.isLoading = true;

    let version = this.$route.query.v;
    // 分销商ID
    this.distributorId = this.$route.query.distributorId;
    // 订单来源
    let orderSource = this.$route.query.orderSource;
    // 平台
    let Platform = this.$route.query.Platform
      ? this.$route.query.Platform
      : orderSource;

    localStorage.setItem("distributorId", this.distributorId);
    localStorage.setItem("orderSource", orderSource);
    localStorage.setItem("Platform", Platform);
    localStorage.setItem("distributor", this.distributor);

    // 获取传递图片id及图片url
    if (!this.isBack) {
      this.picUrl = this.$route.query.picUrl || "";
    }

    // 获取传递是否显示模板标识符
    this.showTemp = Number(this.$route.query.showTemp) || false;

    // 获取传递是否隐藏上传标识符
    this.hiddenUpload = Number(this.$route.query.hiddenUpload) || false;

    // 记录数据埋点
    let hasRecord = sessionStorage.getItem("hasRecord");
    if (!hasRecord) {
      this.handleRecordData();
    }

    this.initData();
  },
  mounted() {
    // 画布高
    var brandH = parseInt(window.getComputedStyle(this.$refs.brandType).height);
    var navH = parseInt(window.getComputedStyle(this.$refs.navWrapper).height);
    let sHeight = `${document.documentElement.clientHeight}`;
    let H = Number(sHeight) - brandH - navH - 60;
    this.wheight = H;
    this.checkVersion();
  },
  computed: {
    noMore() {
      return this.count >= 20;
    },
  },
  methods: {
    initData() {
      if (this.isBack) {
        this.getBrand();
        this.picUrl = "";
      } else {
        let materialNo = this.$route.query.materialsNo || "";
        if (materialNo) {
          // 已传材质编码，优先适配材质
          this.getMaterialList("isMaterial");
        } else {
          // 自动获取机型
          this.getMobileDevice();
        }
      }
    },
    // 记录埋点数据
    handleRecordData() {
      let userId = localStorage.getItem("userId") || "";
      let distributorId = localStorage.getItem("distributorId") || 752;
      let source = localStorage.getItem("Platform") || 5;
      this.$api
        .post(this, api.recordPoint, {
          source: source,
          userId: userId,
          distributorId: distributorId,
          networkType: 1,
        })
        .then((res) => {
          if (res.success) {
            sessionStorage.setItem("hasRecord", 1);
          }
        });
    },
    getNameById() {
      let bName, mName;
      let bid = parseInt(this.brandId);
      let sid = parseInt(this.modelId);
      this.modelData.forEach((item) => {
        if (item.id === bid) {
          bName = item.value;
          if (item.children) {
            item.children.forEach((child) => {
              if (child.id === sid) {
                mName = child.value;
              }
            });
          }
        }
      });
      let str = bName + " " + mName;
      return str;
    },
    // 获取所有品牌型号
    getBrand(gainType) {
      let picId = this.picId === 0 ? "" : this.picId;
      let materialId = this.materialsId === 0 ? "" : this.materialsId;
      let materialNo = this.$route.query.materialsNo || "";
      let platform = localStorage.getItem("Platform") || 5;
      this.$api
        .get(this, api.getModelList, {
          categoryId: 1, // 产品类型id：1 手机壳
          distributorId: this.distributorId,
          materialId: materialId,
          materialNo: materialNo,
          pictureId: picId,
          platform: platform, // 平台
        })
        .then((res) => {
          if (res.success) {
            this.modelData = [];
            if (res.data && res.data.length > 0) {
              let dataArr = res.data;

              // 重组数据
              let modelArr = [];
              dataArr.forEach((item) => {
                item.childrenList.forEach((model) => {
                  modelArr.push({
                    id: model.modelId,
                    value: model.underStockFlag
                      ? model.name + "(缺货)"
                      : model.name,
                    englishValue: model.englishName,
                    underStockFlag: model.underStockFlag || 0,
                  });
                });
                this.modelData.push({
                  id: item.modelId,
                  value: item.name,
                  englishValue: item.englishName,
                  children: modelArr,
                });
                modelArr = [];
              });
              // 根据型号查询品牌并显示
              if (this.mobile !== "" && this.mobile !== undefined) {
                this.getModelId(this.mobile);
              } else {
                this.brandId = dataArr[0].modelId;
                this.brandname = dataArr[0].name;
                if (dataArr[0].childrenList.length > 0) {
                  this.modelId = dataArr[0].childrenList[0].modelId;
                  this.modelName = dataArr[0].childrenList[0].name;
                }
                // 是否缺货
                this.underStockFlag = dataArr[0].childrenList[0].underStockFlag
                  ? true
                  : false;
              }

              if (gainType && gainType === "isMaterial") {
                // 根据手机型号和材质获取手机定制信息
                this.getInfo();
              } else {
                // 根据型号获取材质
                this.getMaterialList();
              }
              this.brand = this.getNameById();
            }
          } else {
            this.$message.error(res.errMessage);
            this.message = "";
            this.isLoading = false;
          }
        });
    },
    getModelId(mname) {
      if (this.modelData.length > 0) {
        let bIndex = 0;
        let flag = false;
        try {
          this.modelData.forEach((brand) => {
            if (brand.children.length > 0) {
              brand.children.forEach((model, index) => {
                if (
                  model.value.replace(/\s*/g, "").toLowerCase() ===
                  mname.replace(/\s*/g, "").toLowerCase()
                ) {
                  this.modelId = model.id;
                  this.modelName = model.value;
                  this.brandId = brand.id;
                  this.brandname = brand.value;

                  // 是否缺货
                  this.underStockFlag = model.underStockFlag ? true : false;

                  bIndex = index;
                  flag = true;
                  throw new Error("end");
                }
              });
            }
          });
          if (!flag) {
            this.brandId = this.modelData[0].id;
            this.brandname = this.modelData[0].value;
            this.modelId = this.modelData[0].children[0].id;
            this.modelName = this.modelData[0].children[0].value;

            // 是否缺货
            this.underStockFlag = this.modelData[0].children[0].underStockFlag
              ? true
              : false;

            this.$message.warning("未匹配到当前手机型号，已展示默认机型");
          }
          this.brand = this.brandname + " " + this.modelName;
        } catch (e) {}
      }
    },
    // 根据型号获取材质列表
    getMaterialList(gainType) {
      // 获取传过来的材质参数
      let materialNo = this.$route.query.materialsNo || "";
      let mName = this.$route.query.materialsName || "";
      let isZFold = this.modelName.toUpperCase().indexOf("FOLD3") >= 0 ? 1 : 0;
      let picId = this.picId === 0 || isZFold ? "" : this.picId;
      let platform = localStorage.getItem("Platform") || 5;

      this.$api
        .get(this, api.getMaterialList, {
          pictureId: picId,
          categoryId: 1, // 产品类型id：1 手机壳
          distributorId: this.distributorId,
          modelId: this.modelId,
          platform: platform, // 平台
        })
        .then((res) => {
          if (res.success) {
            if (res.data && res.data.length > 0) {
              this.isData = true;
              let dataArr = res.data;
              this.materialList = res.data;
              this.materialsName = res.data[0].name;
              this.materialNo = res.data[0].materialNo;

              if (materialNo) {
                this.materialData = [];
                // 材质编号
                dataArr.forEach((item) => {
                  if (item.childrenList && item.childrenList.length > 0) {
                    item.childrenList.forEach((material, r) => {
                      if (material.materialNo === materialNo) {
                        this.materialData.push(material);
                        this.materialsId = material.materialId;
                        this.materialsName = material.name;
                        this.materialNo = material.materialNo;
                        this.skuNo = material.itemCode || "";
                        this.materialsImage = material.image;
                        this.materialsColorValue = material.colour;
                        this.manufactor = material.manufactor;

                        // 获取是否允许用户上传
                        this.uploadValid = material.allowUploadFlag;
                        // 是否强制铺满血位：1 是，0 否
                        let isAllPlace = material.mandatoryCoveredBleedFlag;
                        sessionStorage.setItem("isAllPlace", isAllPlace);

                        // 是否缺货
                        this.underStockFlag = material.underStockFlag
                          ? true
                          : false;

                        return;
                      }
                      return;
                    });
                  } else {
                    if (item.materialNo === materialNo) {
                      this.materialData.push(item);
                      this.materialsId = item.materialId;
                      this.materialsName = item.name;
                      this.materialNo = "";
                      this.skuNo = "";
                      this.materialsImage = item.image;
                      this.materialsColorValue = item.colour;
                      this.manufactor = item.manufactor;
                      return;
                    }
                  }
                });
                this.getMaterialById(this.materialsId);
                if (gainType && gainType === "isMaterial") {
                  this.getMobileDevice(gainType);
                } else {
                  // 根据手机型号和材质获取手机定制信息
                  this.getInfo();
                }

                if (this.materialData.length <= 0) {
                  this.message = "";
                  vm.isLoading = false;
                  this.materialsName = "暂无关联材质";
                  this.colorName = "";
                  this.materialsId = "";
                  this.skuNo = "";
                  this.$confirm(
                    "暂无关联的材质数据，请选择其他机型或联系客服处理~",
                    "温馨提示",
                    {
                      customClass: "confirm-v-dialog-upload",
                      confirmButtonText: "关闭",
                      confirmButtonColor: "#f21e1c",
                      type: "warning",
                      iconClass: "el-warning",
                      showCancelButton: false,
                      center: true,
                    }
                  )
                    .then(() => {})
                    .catch((error) => {
                      console.log(error);
                    });
                }
              } else if (mName) {
                this.materialData = [];
                // 材质名称
                dataArr.forEach((item) => {
                  if (item.childrenList && item.childrenList.length > 0) {
                    item.childrenList.forEach((material, r) => {
                      if (mName.indexOf(material.name) > 0) {
                        this.materialData.push(material);
                        this.materialsId = material.materialId;
                        this.materialsName = material.name;
                        this.materialNo = material.materialNo;
                        this.skuNo = material.itemCode || "";
                        this.materialsImage = material.image;
                        this.materialsColorValue = material.colour;
                        this.manufactor = material.manufactor;

                        // 获取是否允许用户上传
                        this.uploadValid = material.allowUploadFlag;
                        // 是否强制铺满血位：1 是，0 否
                        let isAllPlace = material.mandatoryCoveredBleedFlag;
                        sessionStorage.setItem("isAllPlace", isAllPlace);

                        // 是否缺货
                        this.underStockFlag = material.underStockFlag
                          ? true
                          : false;

                        return;
                      }
                      return;
                    });
                  } else {
                    if (mName.indexOf(item.name) > 0) {
                      this.materialData.push(item);
                      this.materialsId = item.materialId;
                      this.materialsName = item.name;
                      this.materialNo = "";
                      this.skuNo = "";
                      this.materialsImage = item.image;
                      this.materialsColorValue = item.colour;
                      this.manufactor = item.manufactor;
                      return;
                    }
                  }
                });
                this.getMaterialById(this.materialsId);
                if (gainType && gainType === "isMaterial") {
                  this.getMobileDevice(gainType);
                } else {
                  // 根据手机型号和材质获取手机定制信息
                  this.getInfo();
                }

                if (this.materialData.length <= 0) {
                  this.message = "";
                  this.isLoading = false;
                  this.materialsName = "暂无关联材质";
                  this.colorName = "";
                  this.materialsId = "";
                  this.skuNo = "";
                  this.$confirm(
                    "暂无关联的材质数据，请选择其他机型或联系客服处理~",
                    "温馨提示",
                    {
                      customClass: "confirm-v-dialog-upload",
                      confirmButtonText: "关闭",
                      confirmButtonColor: "#f21e1c",
                      type: "warning",
                      iconClass: "el-warning",
                      showCancelButton: false,
                      center: true,
                    }
                  )
                    .then(() => {})
                    .catch((error) => {
                      console.log(error);
                    });
                }
              } else {
                let materialArr = [];
                this.materialData = [];
                dataArr.forEach((item, k) => {
                  if (item.childrenList) {
                    item.childrenList.forEach((material, r) => {
                      materialArr.push({
                        id: material.materialId,
                        materialNo: material.materialNo,
                        value: material.name,
                        englishValue: material.englishName,
                        color: material.colour || "",
                        manufactor: material.manufactor,
                        underStockFlag: material.underStockFlag || 0,
                        allowUploadFlag: material.allowUploadFlag,
                        mandatoryCoveredBleedFlag:
                          material.mandatoryCoveredBleedFlag,
                        skuNo: material.itemCode || "",
                      });
                    });
                    this.materialData.push({
                      id: item.materialId,
                      value: item.name,
                      englishValue: item.englishName,
                      color: item.colour || "",
                      manufactor: item.manufactor,
                      children: materialArr,
                    });
                  } else {
                    this.materialData.push({
                      id: item.materialId,
                      value: item.name,
                      englishValue: item.englishName,
                      color: item.colour || "",
                      manufactor: item.manufactor,
                      children: [
                        {
                          id: item.materialId,
                          materialNo: item.materialNo,
                          value: item.name,
                          englishValue: item.englishName,
                          color: item.colour || "",
                          manufactor: item.manufactor,
                          underStockFlag: item.underStockFlag || 0,
                          allowUploadFlag: item.allowUploadFlag,
                          mandatoryCoveredBleedFlag:
                            item.mandatoryCoveredBleedFlag,
                          skuNo: material.itemCode || "",
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
                    this.skuNo = dataArr[0].childrenList[0].itemCode || "";
                    this.materialsColorValue =
                      dataArr[0].childrenList[0].colour;
                    this.manufactor = dataArr[0].childrenList[0].manufactor;

                    // 获取是否允许用户上传
                    this.uploadValid =
                      dataArr[0].childrenList[0].allowUploadFlag;
                    // 是否强制铺满血位：1 是，0 否
                    let isAllPlace =
                      dataArr[0].childrenList[0].mandatoryCoveredBleedFlag;

                    // 是否缺货
                    this.underStockFlag = dataArr[0].childrenList[0]
                      .underStockFlag
                      ? true
                      : false;

                    sessionStorage.setItem("isAllPlace", isAllPlace);
                  } else {
                    this.materialsId = dataArr[0].materialId;
                    this.materialNo = "";
                    this.skuNo = "";
                    this.colorName = "";
                    this.materialsColorValue = dataArr[0].colour;
                    this.manufactor = dataArr[0].manufactor;
                  }
                }
                if (gainType && gainType === "isMaterial") {
                  this.getMobileDevice(gainType);
                } else {
                  // 根据手机型号和材质获取手机定制信息
                  this.getInfo();
                }
              }
            } else {
              this.materialData = [];
              this.picData = [];
              this.materialsId = 0;
              this.materialsName = "";
              this.materialNo = "";
              this.skuNo = "";
              this.materialsColorValue = "";
              this.materialsImage = "";
              this.colorName = "";
              this.isData = false;
              this.curPicType = 0;
              this.reset();
              this.message = "";
              this.$message.error("暂无关联的材质数据");
              this.isLoading = false;
            }
          } else {
            this.isData = false;
            this.client = "";
            this.message = "";
            this.isLoading = false;
            this.$message.error(res.errMessage);
            this.getBrand();
          }
        });
    },
    // 根据材质ID获取材质信息
    getMaterialById(id) {
      let flag = false;
      this.materialList.forEach((item, index) => {
        if (item.childrenList && item.childrenList.length > 0) {
          item.childrenList.forEach((second) => {
            if (second.materialId === id) {
              this.materialsName = item.name;
              this.materialNo = second.materialNo;
              this.skuNo = second.itemCode || "";
              this.colorName = second.name;
              this.materialsColorValue = second.colour;
              this.manufactor = second.manufactor;

              // 获取是否允许用户上传
              this.uploadValid = second.allowUploadFlag;
              // 是否强制铺满血位：1 是，0 否
              let isAllPlace = second.mandatoryCoveredBleedFlag;
              sessionStorage.setItem("isAllPlace", isAllPlace);

              // 是否缺货
              this.underStockFlag = second.underStockFlag ? true : false;

              flag = true;
            }
          });
        } else {
          if (item.materialId === id) {
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
        this.materialsId =
          this.materialList[0].childrenList.length > 0
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
        this.materialsName = this.materialList[0].name;
        this.colorName =
          this.materialList[0].childrenList.length > 0
            ? this.materialList[0].childrenList[0].name
            : "";
        this.materialsColorValue =
          this.materialList[0].childrenList.length > 0
            ? this.materialList[0].childrenList[0].colour
            : this.materialList[0].colour;
        this.manufactor =
          this.materialList[0].childrenList.length > 0
            ? this.materialList[0].childrenList[0].manufactor
            : this.materialList[0].manufactor;

        // 获取是否允许用户上传
        this.uploadValid = this.materialList[0].childrenList[0].allowUploadFlag;
        // 是否强制铺满血位：1 是，0 否
        let isAllPlace =
          this.materialList[0].childrenList[0].mandatoryCoveredBleedFlag;
        sessionStorage.setItem("isAllPlace", isAllPlace);

        // 是否缺货
        this.underStockFlag = this.materialList[0].childrenList[0]
          .underStockFlag
          ? true
          : false;
      }
    },
    // 获取图片列表
    getPicList(typeList) {
      this.$api
        .get(this, api.getPictureList, {
          distributorId: this.distributorId,
          materialId: this.materialsId,
          materialNo: this.materialNo,
          modelId: this.modelId,
          productCategoryId: 1, // 产品类型id：1 手机壳
          typeList: typeList, // 1 普通素材，2 IP素材，3 模板
        })
        .then((res) => {
          if (res.success) {
            if (res.data && res.data.length > 0) {
              if (Number(typeList) === 3) {
                // 模板
                this.tempData = res.data;
                this.tempDialog = true;
                this.mask = true;
              } else {
                this.picData = res.data;
              }

              // 判断图片id是否匹配
              let flag = this.checkPic();
              if (flag) {
                // 匹配
                // 判断是否有图片地址
                if (this.picUrl) {
                  this.wallpImg = this.picUrl;
                }
              } else {
                // 不匹配
                // 判断是否有图片id
                if (
                  this.$route.query.pid &&
                  Number(this.$route.query.pid) !== 0 &&
                  this.distributorId === "6175"
                ) {
                  this.message = "";
                  this.isLoading = false;
                  // 爱思
                  this.$confirm(
                    "当前图片不可用，请点击左下角“图库”重新选择图片~",
                    "温馨提示",
                    {
                      customClass: "confirm-v-dialog-upload",
                      confirmButtonText: "我知道了",
                      confirmButtonColor: "#f21e1c",
                      type: "warning",
                      iconClass: "el-warning",
                      showCancelButton: false,
                      center: true,
                    }
                  )
                    .then(() => {})
                    .catch((error) => {
                      console.log(error);
                    });

                  this.wallpImg = "";
                  this.picId = 0;
                  this.picName = "";
                  this.imgData.posX = null;
                  this.reset();
                }
                // 判断图片id
                if (this.picId !== 0) {
                  this.message = "";
                  this.isLoading = false;
                  this.$message.warning("当前图片不可用");
                  this.wallpImg = "";
                  this.picId = 0;
                  this.picName = "";
                  this.imgData.posX = null;
                  this.reset();
                }
              }

              if (this.curPicType === 2) {
                this.changeImage(
                  this.wallpImg,
                  this.picId,
                  this.picType,
                  this.picName,
                  {}
                );
              } else {
                // 重绘
                this.$refs.mCanvas.initCanvas();
                this.$refs.mCanvas.draw();
                this.$refs.phoneWrapper.dispatchEvent(new MouseEvent("click"));
              }
              this.message = "";
              this.isLoading = false;
            } else {
              this.$message("暂无图片！");
            }
          } else {
            this.message = "";
            this.isLoading = false;
            this.$message.error(res.errMessage);
          }
        });
    },
    // 根据手机型号和材质获取手机定制信息
    getInfo() {
      let isZFold = this.modelName.toUpperCase().indexOf("FOLD3") >= 0 ? 1 : 0;
      this.isZFold = isZFold;
      if (Number(sessionStorage.getItem("isZFold")) !== isZFold) {
        // 清空画布
        this.$refs.mCanvas.dragArr = [];
        this.$refs.mCanvas.clear();
      }
      sessionStorage.setItem("isZFold", isZFold);

      this.$api
        .get(this, api.getCustomInfo, {
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
              this.phonenHeight = this.wheight * 0.68;
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
              switch (Number(this.materialsId)) {
                case 74:
                  this.materialsType = 1;
                  break;
                case 82:
                  this.materialsType = 2;
                  break;
                case 84:
                  this.materialsType = 3;
                  break;
                case 86:
                  this.materialsType = 4;
                  break;
                case 91:
                case 108:
                case 109:
                  this.materialsType = 5;
                  break;
                default:
                  this.materialsType = 2;
                  break;
              }

              if (
                Number(this.materialsId) === 74 ||
                Number(this.materialsId) === 84
              ) {
                // 玻璃壳/热升华+2mm
                this.phoneHeight = this.wheight * 0.68 + this.pm2px(2);
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
                this.phoneHeight = this.wheight * 0.68;
                this.phoneWidth = this.phoneHeight * this.pscale;
                this.mh = this.nh = this.modelinfo.length;
                this.mw = this.nw = this.modelinfo.width;
              }

              // 透明间距
              let frameValue = {
                topFrame: this.modelinfo.topFrame || 0,
                underFrame: this.modelinfo.underFrame || 0,
                leftFrame: this.modelinfo.leftFrame || 0,
                rightFrame: this.modelinfo.rightFrame || 0,
              };
              sessionStorage.setItem("frameValue", JSON.stringify(frameValue));

              // 判断是否有切图信息（折叠屏）
              if (
                (this.modelinfo.cutType === 1 &&
                  this.modelinfo.slittingHeight) ||
                (this.modelinfo.cutType === 2 &&
                  this.modelinfo.crosscuttingWidth)
              ) {
                let cutInfo = JSON.stringify({
                  cutType: this.modelinfo.cutType,
                  slittingHeight: this.modelinfo.slittingHeight,
                  slittingWhite: this.modelinfo.slittingWhite,
                  crosscuttingWidth: this.modelinfo.crosscuttingWidth,
                  crosscuttingWhite: this.modelinfo.crosscuttingWhite,
                });
                sessionStorage.setItem("cutInfo", cutInfo);
              } else {
                sessionStorage.setItem("cutInfo", "");
              }

              if (this.materialsId !== 0) {
                if (this.curPicType !== 3) {
                  // 非模板
                  if (this.distributorId === "4478") {
                    this.getPicList("1,2"); // 1 普通素材，2 IP素材
                  } else {
                    this.getPicList(null); // 全部
                  }
                } else {
                  // 模板，重绘
                  this.$refs.mCanvas.initCanvas();
                  this.$refs.mCanvas.draw();
                  setTimeout(() => {
                    this.$refs.phoneWrapper.dispatchEvent(
                      new MouseEvent("click")
                    );
                    this.message = "";
                    this.isLoading = false;
                  }, 500);
                }
                this.getPrice();
              }
            }
          } else {
            this.isData = false;
            this.$message.error(res.errMessage);
            this.message = "";
            this.isLoading = false;
          }
        });
    },
    getPrice() {
      let orderSource = localStorage.getItem("orderSource") || 5;
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
            orderSource: orderSource,
            materialId: this.materialsId,
          })
          .then((res) => {
            if (res.success) {
              if (res.data) {
                this.price = (res.data + this.copyrightCost).toFixed();
              } else {
                this.price = this.copyrightCost.toFixed();
              }
              this.isData = true;
              this.isPrice = true;
            } else {
              this.isPrice = false;
              this.price = 0;
              this.isData = false;
              this.$message.error(res.errMessage);
              this.reset();
            }
          })
          .catch((err) => {
            console.log(err);
          });
      }
    },
    // 获取字体列表
    getFont() {
      this.$api.get(this, api.getFontList).then((res) => {
        if (res.success) {
          if (res.data && res.data.length > 0) {
            this.fontFamily = res.data;
            // 引入字体文件
            let style = document.createElement("style");
            style.type = "text/css";
            let str = "";
            this.fontFamily.forEach((font) => {
              str +=
                '@font-face {font-family: "' +
                font.englishName +
                '";src: url("' +
                font.fontFile +
                '") ;font-weight: normal;font-style: normal;}';
              let dom = document.createElement("div");
              dom.style.fontFamily = font.englishName;
              dom.style.fontSize = "14px";
              dom.style.height = "0";
              dom.style.opacity = "0";
              dom.innerHTML = "test";
              document.getElementById("phone").appendChild(dom);
            });
            style.innerText = str;
            document.getElementsByTagName("head")[0].appendChild(style);

            this.family = this.family
              ? this.family
              : this.fontFamily[0].englishName;
            this.fontColor = this.colors.hex;
          }
        }
      });
    },
    getdpi(value, ratio) {
      this.dpiValue = value;
      this.ratio = ratio;
    },
    getDpiValue(value) {
      if (value) {
        this.dpiValue = value;
        this.dpiRate =
          Number(value) / 50 >= 5 ? 5 : Math.ceil(Number(value) / 50);
        if (this.dpiRate >= 5) {
          this.dpiDesc = "清晰可使用";
          this.dpiColor = "#1EC15F";
          return "clearly";
        } else if (this.dpiRate > 2 && this.dpiRate < 5) {
          this.dpiDesc = "模糊，画质一般";
          this.dpiColor = "#FCDF4F";
          return "general";
        } else if (this.dpiRate > 0 && this.dpiRate <= 2) {
          this.dpiDesc = "图片无法使用";
          this.dpiColor = "#FF5061";
          return "dim";
        }
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
    change(url, pic) {
      this.picDialog = false;
      this.tempDialog = false;
      this.mask = false;
      this.picId = pic.id;
      this.curPicType = pic.type;

      // 计算价格（加上图片版权费），如果已传价格，不计算
      let price = this.$route.query.price;
      this.copyrightCost = 0;
      if (!price && pic.copyrightCost) {
        this.copyrightCost = pic.copyrightCost;
      }

      // 图片类型 1-普通图片 2-IP图片（不可更改） 3-模板
      if (this.isPrice) {
        this.imgData.posX = null;
        this.message = "载入中";
        this.isLoading = true;
        this.changeImage(url, pic.id, pic.type, pic.name, pic);
      }
    },
    changeImage(url, id, type, name, item, cateType) {
      // cateType：1（支持多次上传）例：本地图片，2 贴图
      if (this.materialsId !== 0) {
        this.wallpImg = url;
        this.picUrl = "";
        this.picId = id;
        this.picName = name;
        // 图片类型 1-普通图片 2-IP图片（不可更改） 3-模板
        this.picType = type;
        Promise.all([this.loadImage(url)]).then((img) => {
          let w = img[0].width;
          let h = img[0].height;
          let diffX = 0;
          let diffY = 0;
          let scale = this.getScale(w, h);
          let centerX, centeruX;
          let centerY, centeruY;
          this.imgData.initW = w / scale;
          this.imgData.initH = h / scale;
          if (this.imgData.posX === null || this.picType === 2) {
            centerX = window.screen.width / 2;
            if (this.isZFold) {
              centerX = (window.screen.width - this.phonenWidth / 2) / 2;
            }
            centerY = this.wheight / 2;
            this.imgData.sizeW = w / scale;
            this.imgData.sizeH = h / scale;
            this.imgData.posX = centerX - w / scale / 2;
            this.imgData.posY = centerY - h / scale / 2;
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

          let imgObj = {
            x: this.imgData.posX,
            y: this.imgData.posY,
            initx: this.imgData.posX,
            inity: this.imgData.posY,
            ux: this.imgData.posuX,
            uy: this.imgData.posuY,
            url: img[0],
            width: this.imgData.sizeW,
            height: this.imgData.sizeH,
            initW: this.imgData.initW,
            initH: this.imgData.initH,
            // 最小宽高
            minW: this.imgData.sizeW,
            minH: this.imgData.sizeH,
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

          this.message = "";
          this.isLoading = false;
        });
      }
    },
    // 加载图片
    loadImage(url) {
      let img = new Image();
      img.crossOrigin = "anonymous";
      if (url.indexOf("data:") === 0) {
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
          this.textData.posuX = this.phoneWidth / 2;
          this.textData.posuY = this.phoneHeight / 2;
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
          picType: this.picType,
          rotate: this.textData.rotate,
          selected: true,
          index: this.index,
        };
        this.spriteArr = [];
        this.spriteArr.push(textObj);
        this.textDialog = false;
        this.mask = false;
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

        this.$refs.mCanvas.draw(); // 重绘，以免删减文字错位
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
        this.dpiValue = 0;
      }
    },
    reset() {
      this.$refs.mCanvas.reset();
    },
    // 完成定制
    make() {
      this.submitDialogVisible = false;
      if (this.wallpImg !== "") {
        if (this.isData) {
          if (this.skuNo === "0" || this.skuNo === 0 || this.skuNo === "") {
            this.$message.warning(
              "当前机型或图片无可用材质，请尝试切换机型或更换图片~"
            );
            return;
          }
          this.isLoading = true;
          this.message = "正在生成预览";
          // 判断IP图片是否已经定制
          if (Number(this.picType) === 2) {
            this.isMake = false;
            // 获取是否已生成定制信息
            this.$api
              .get(this, api.getCacheIP, {
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
                      (this.manufactor === "LHW" &&
                        res.data.generateImage.indexOf(".png") > -1))
                  ) {
                    let imgUrl = res.data.previewImage;
                    let PdfUrl = res.data.generateImage;
                    this.isMake = true;
                    this.isLoading = false;
                    this.message = "";
                    this.submit(imgUrl, PdfUrl);
                  } else {
                    this.isMake = false;
                    this.$refs.mCanvas.make();
                  }
                } else {
                  this.$message.error(res.errMessage);
                }
              })
              .catch((err) => {
                console.log(err);
                this.isLoading = false;
                this.message = "";
              });
          } else {
            this.$refs.mCanvas.make();
          }
        } else {
          this.$message.warning("请先完善数据！");
        }
      } else {
        this.$message.warning("请添加图片！");
      }
    },
    // 定制成功并跳转
    submit(imgUrl, pdfUrl) {
      let materialsValue = this.colorName
        ? this.materialsName + " - " + this.colorName
        : this.materialsName;

      let skuId = this.$route.query.skuId;
      this.$router.push({
        path: "/preview",
        query: {
          picType: this.picType,
          isMake: this.isMake,
          diyPic: imgUrl,
          diyPdf: pdfUrl,
          materialId: this.materialsId,
          modelId: this.modelId,
          materialsName: materialsValue,
          modelName: this.modelName,
          brandId: this.brandId,
          brandName: this.brandname,
          pictureId: this.picId,
          picType: this.picType,
          picture: this.picName,
          price: this.price,
          sku: this.skuNo,
          skuId: skuId,
          skuName: this.skuName,
          manufactor: this.manufactor,
        },
      });
    },
    getPixelRatio() {
      let canvas = document.createElement("canvas");
      let context = canvas.getContext("2d");
      let backingStore =
        context.backingStorePixelRatio ||
        context.webkitBackingStorePixelRatio ||
        context.mozBackingStorePixelRatio ||
        context.msBackingStorePixelRatio ||
        context.oBackingStorePixelRatio ||
        context.backingStorePixelRatio ||
        1;
      return (window.devicePixelRatio || 1) / backingStore;
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
        // 通过 iphone-device.js 获取苹果机具体的机型
        let phone = "";
        if (this.$route.query.phoneName) {
          phone = this.$route.query.phoneName;
        } else {
          phone = getModel(rendererVal);
        }
        // this.mobile = getModel(rendererVal)
        if (phone.indexOf(",") > 0) {
          this.$message("机型适配可能存在偏差，请确认或手动选择！");
          this.mobile = phone.substring(0, phone.indexOf(","));
        } else {
          this.mobile = phone;
        }
        this.getBrand(gainType);
      } else if (os === "AndroidOS") {
        // Android 系统的处理
        os = md.os() + md.version("Android");
        // 判断 UA 里边有没有 Build 或者 AppleWebkit信息，通过这个拿到安卓的具体机型
        let str;
        if (userAgent.indexOf("Build/") > 0) {
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
          this.getAndroidMobile(networkModel.trim()).then((res) => {
            if (res.success && res.data) {
              this.mobile = res.data.name;
            } else {
              this.mobile = "";
              this.$message.warning("未匹配到当前手机型号，已展示默认机型");
            }
            this.getBrand(gainType);
          });
        } else {
          this.getBrand(gainType);
        }
      }
    },
    // 获取 Android 手机型号
    getAndroidMobile(networkModel) {
      return new Promise((resolve, reject) => {
        this.$api
          .get(this, api.getModelByNetwork, { networkModel: networkModel })
          .then((res) => {
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // 拍照
    takePhoto(e) {
      let file = this.$refs.photoFile.files[0];
      this.getUploadImg(file);
    },
    // 上传图片
    picUpload(event, loadType) {
      if (this.hasClickedPic) {
        if (this.curPicType === 2 || this.curPicType === 3) {
          // 清空画布
          this.reset();
        }
        this.curPicType = 0
      }
      let file = event.target.files[0];
      if (this.distributorId === "4478") {
        this.tipsDialog = false;
        this.mask = false;
      }
      this.getUploadImg(file, loadType);
    },
    getUploadImg(file, loadType) {
      this.message = "载入中";
      this.isLoading = true;

      let reader = this.handleBeforeUpload(file);
      reader.addEventListener("load", () => {
        this.uploadDialog = false;
        this.mask = false;
        this.wallpImg = reader.result;
        this.imgData.posX = null;
        // id： 0-网络图片 1-普通图片 type: 1-普通图片 2-IP图
        this.changeImage(this.wallpImg, 0, this.curPicType, "", {}, 1);

        // 图片上传完成，显示模板操作栏 3s 后消失
        if (loadType && loadType === "showHandle") {
          // 操作按钮显示后隐藏
          this.$refs.mCanvas.hasClickedArea = true;
          if (this.$refs.mCanvas.hasClickedArea) {
            setTimeout(() => {
              this.$refs.mCanvas.hasClickedArea = false;
            }, 2500);
          }
        }
      });
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
        this.$message.warning({
          title: "警告",
          message: "请上传格式为image/png, image/jpg, image/jpeg的图片",
        });

        this.message = "";
        this.isLoading = false;
        return;
      } else {
        if (this.distributorId === "1783") {
          reader.onload = function () {
            document.getElementById("picfile2").setAttribute("type", "text");
          };
        }
        reader.readAsDataURL(file);
      }
      let isLt10M = file.size / 1024 / 1024 < 10;
      if (!isLt10M) {
        this.$message.error("上传头像图片大小不能超过 10MB!");

        this.message = "";
        this.isLoading = false;
        return;
      }
      return reader;
    },
    // 型号
    modelDialog() {
      if (!this.isLoading) {
        this.$refs.modelPicker.show();
      }
    },
    handleModelConfirm(arr) {
      this.message = "数据加载中";
      this.isLoading = true;
      // 判断是否缺货
      if (arr[1].value.indexOf("缺货") > 0) {
        // 是
        this.underStockFlag = true;
      } else {
        // 否
        this.underStockFlag = false;
      }
      // 获取品牌型号
      let bIndex = arr[0].index;
      let mIndex = arr[1].index;
      this.brand = arr[0].value + " " + arr[1].value;
      this.brandId = this.modelData[bIndex].id;
      this.modelId = this.modelData[bIndex].children[mIndex].id;
      this.brandname = arr[0].value;
      this.modelName = arr[1].value;
      this.mobile = arr[1].value;
      // 重置图片，文字
      this.imgData.posX = null;
      this.textData.posX = null;
      this.getMaterialList();
      if (this.materialData.length > 0) {
        this.materialsName = this.materialData[0].value;
        this.colorName =
          this.materialData[0].children > 0
            ? this.materialData[0].children[0].value
            : "";
      }
    },
    // 材质
    materialDialog() {
      if (this.materialData.length > 1) {
        this.$refs.materialPicker.show();
      }
    },
    handleMaterialConfirm(arr) {
      this.message = "数据加载中";
      this.isLoading = true;
      // 获取材质、颜色
      let bIndex = arr[0].index;
      let mIndex = arr[1].index;
      this.materialsName = arr[0].value;
      this.materialsId =
        this.materialData[bIndex].children.length > 0
          ? this.materialData[bIndex].children[mIndex].id
          : this.materialData[bIndex].id;
      this.materialNo =
        this.materialData[bIndex].children.length > 0
          ? this.materialData[bIndex].children[mIndex].materialNo
          : "";
      this.skuNo =
        this.materialData[bIndex].children.length > 0
          ? this.materialData[bIndex].children[mIndex].skuNo
          : "";
      this.colorName =
        this.materialData[bIndex].children.length > 0
          ? this.materialData[bIndex].children[mIndex].value
          : "";
      this.materialsColorValue =
        this.materialData[bIndex].children.length > 0
          ? this.materialData[bIndex].children[mIndex].colour
          : this.materialData[bIndex].colour;
      this.manufactor =
        this.materialData[bIndex].children.length > 0
          ? this.materialData[bIndex].children[mIndex].manufactor
          : this.materialData[bIndex].manufactor;

      // 获取是否允许用户上传
      this.uploadValid =
        this.materialData[bIndex].children[mIndex].allowUploadFlag;
      // 是否强制铺满血位：1 是，0 否
      let isAllPlace =
        this.materialData[bIndex].children[mIndex].mandatoryCoveredBleedFlag;
      sessionStorage.setItem("isAllPlace", isAllPlace);

      this.getBrand("isMaterial");
    },
    load() {
      this.isLoading = true;
      setTimeout(() => {
        this.isLoading = false;
      }, 2000);
    },
    removeText() {
      this.clearText();
      this.textDialog = false;
      this.mask = false;
    },
    // 判断当前图片是否可用
    checkPic() {
      this.picId = this.$route.query.pid || 0;
      let flag = false;
      this.picData.forEach((item) => {
        if (item.childrenList && item.childrenList.length > 0) {
          item.childrenList.forEach((child) => {
            if (child.pictureList && child.pictureList.length > 0) {
              child.pictureList.forEach((pic) => {
                if (pic.id == this.picId) {
                  flag = true;
                  // 获取图片信息
                  this.picUrl = pic.originImage; // 图片地址
                  this.picType = pic.type; // 图片类型
                  this.picName = pic.name; // 图片名称
                }
              });
            }
          });
        } else {
          item.pictureList.forEach((pic) => {
            if (pic.id == this.picId) {
              flag = true;
              // 获取图片信息
              this.picUrl = pic.originImage; // 图片地址
              this.picType = pic.type; // 图片类型
              this.picName = pic.name; // 图片名称
            }
          });
        }
      });
      return flag;
    },
    checkVersion() {
      let u = navigator.userAgent;
      let isAndroid = u.indexOf("Android") > -1 || u.indexOf("Linux") > -1; // android终端或者uc浏览器
      // let type = u.toLowerCase().match(/QQ/i)
      let isiOS = !!u.match(/\(i[^]+( U)? CPU.+Mac OS X/); // ios终端
      if (isAndroid) {
        this.$refs.file2.setAttribute("accept", "image/photoalbum");
      } else {
        this.$refs.file2.setAttribute("accept", "image/*");
      }
    },
    validateText() {
      if (this.family === "") {
        this.$message.warning("请选择字体");
        return false;
      }
      if (this.inputtext === "") {
        this.$message.warning("请输入文字");
        return false;
      }
      return true;
    },
    // 确认材质颜色属性
    submitColor() {
      this.materialColorList.forEach((item) => {
        if (item.id === this.materialColor) {
          this.colorName = item.name;
          this.manufactor = item.manufactor;
        }
      });
      this.materialsId = this.materialColor;
      this.mask = false;
      this.colorDialog = false;
      // 根据手机型号和材质获取手机定制信息
      this.getInfo();
    },
    // 打开确认弹框
    openSubmit() {
	  console.log("H5编辑器点击设计完成");
      // 遍历canvas画布中所有图层，判断是否有图片
      let hasPic = false;
      if (this.$refs.mCanvas.dragArr && this.$refs.mCanvas.dragArr.length > 0) {
        let arr = this.$refs.mCanvas.dragArr;
        arr.forEach((item) => {
          if (
            Number(item.type) === 1 &&
            Number(item.picType) !== 4 &&
            ((Number(item.picType) === 3 && !item.picId) ||
              Number(item.picType) !== 3)
          ) {
            // 有图片
            hasPic = true;
          }
        });
      }

      if (!this.underStockFlag) {
        if (hasPic) {
          if (this.isData) {
            if (this.skuNo === "0" || this.skuNo === 0 || this.skuNo === "") {
              this.$message.warning(
                "当前机型或图片无可用材质，请尝试切换机型或更换图片~"
              );
              return;
            } else {
              this.submitDialogVisible = true;
            }
          } else {
            this.$message("请先完善数据！");
          }
        } else {
          this.$message("请添加图片！");
        }
      }
    },
    openPic(e) {
      if (this.picData.length > 0) {
        this.mask = true;
        this.picDialog = true;
      } else {
        if (this.distributorId === "4478") {
          this.getPicList("1,2"); // 1 普通素材，2 IP素材
        } else {
          this.getPicList(null); // 全部
        }
      }
    },
    // 上传图片提示
    openTips(type) {
      // 判断是否允许用户上传
      if (Number(this.uploadValid) === 1) {
        if (this.isFlag === 0) {
          // 提示
          if (this.distributorId === "4478") {
            this.isFlag++;
            this.tipsDialog = true;
            this.mask = true;
            if (type && type === "showPic") {
              this.hasClickedPic = true;
            }
          } else {
            this.$confirm(
              "尊敬的用户你好，请保证图片清晰可用，不可上传违反国家法律法规图片，对此造成的后果我方不予承担，印刷制作存在些许误差，介意请勿下单喔。",
              "温馨提示",
              {
                customClass: "confirm-v-dialog-upload",
                confirmButtonText: "我知道了",
                confirmButtonColor: "#f21e1c",
                cancelButtonText: "返回",
                type: "warning",
                iconClass: "el-warning",
                center: true,
              }
            )
              .then(() => {
                this.isFlag++;
                if (this.distributorId === "1783") {
                  this.mask = true;
                  this.uploadDialog = true;
                } else {
                  this.$refs.file.dispatchEvent(new MouseEvent("click"));
                }
                
                if (type && type === "showPic") {
                  this.hasClickedPic = true;
                }
              })
              .catch((error) => {
                this.hasClickedPic = false;
                console.log(error);
              });
          }
        } else {
          if (this.distributorId === "1783") {
            this.mask = true;
            this.uploadDialog = true;
          } else {
            this.$refs.file.value = ""; // 再次点击清空input="file"数据，以免同一图片不能重复上传
            this.$refs.file.dispatchEvent(new MouseEvent("click"));
          }
          if (type && type === "showPic") {
            this.hasClickedPic = true;
          }
        }
      } else {
        this.$message.warning("当前材质不支持图片上传");
      }
    },
    openImage(e) {
      this.uploadDialog = true;
      this.tipsDialog = false;
    },
    openText(e) {
      if (Number(this.materialsId) === 86) {
        this.$message.warning("当前材质暂不支持文字功能，如有疑问请联系客服~");
      } else {
        if (this.picType !== 2) {
          // 获取字体
          this.getFont();

          this.mask = true;
          this.textDialog = true;
          this.clearText();
          if (this.spriteArr.length > 0) {
            this.spriteArr.forEach((item) => {
              if (item.selected && item.type === 0) {
                this.inputtext = item.filltext;
                this.family = item.fontFamily;
                this.fontColor = item.fontColor;
              }
            });
          }
        } else {
          this.$message.warning("此图片不支持文字");
        }
      }
    },
    // 选择材质颜色属性
    openColor() {
      this.mask = true;
      this.colorDialog = true;
    },
    closeColor() {
      this.mask = false;
      this.colorDialog = false;
    },
    close() {
      this.mask = false;
      this.textDialog = false;
    },
    closePic() {
      this.mask = false;
      this.picDialog = false;
    },
    closeUp() {
      this.mask = false;
      this.uploadDialog = false;
    },
    closeTips() {
      this.mask = false;
      this.tipsDialog = false;
    },
    closeMask() {
      this.mask = false;
      this.textDialog = false;
      this.uploadDialog = false;
      this.picDialog = false;
      this.tempDialog = false;
      this.colorDialog = false;
      this.dialogVisible = false;
      this.tipsDialog = false;
    },
    // 模板
    handleTemp() {
      this.hasClickedPic = false;
      if (this.tempData && this.tempData.length > 0) {
        this.mask = true;
        this.tempDialog = true;
      } else {
        this.getPicList("3"); // 3 模板
      }
    },
    closeTemp() {
      this.mask = false;
      this.tempDialog = false;
    },
	
	//点击知道了隐藏柔性关闭弹窗
	clickFlexible(){
	  this.showFlexible = false;
	}
	
  },
  watch: {
    colors(value) {
      // 字体颜色
      this.fontColor = value.hex;
    },
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
      this.phoneHeight = this.wheight * 0.9;
      this.phoneWidth = this.phoneHeight * this.pscale;
    },
    mask(value) {
      if (value) {
        document.body.style.cssText = "overflow: hidden";
      } else {
        document.body.style.cssText = "overflow: auto";
        if (this.distributorId === "1783") {
          // 防止多次上传同一张图片不生效问题
          document.getElementById("picfile2").setAttribute("type", "file");
        }
      }
    },
  },
  components: {
    pictureList: () => import("components/picture-list/picture-list"),
    canvasDrag: () => import("components/canvasDrag/canvas-drag"),
    Loading: () => import("components/loading/loading"),
    "slider-picker": Slider,
  },
};
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
@import '../../common/stylus/mixin.styl';

.noclick {
  pointer-events: none;
}

.phone {
  position: relative;
  display: flex;
  display: -webkit-flex;
  flex-direction: column;
  justify-content: center;
  width: 100%;
  height: 100%;
  margin: 0 auto;
  text-align: center;
  background-color: #ffffff;
  overflow: hidden;

  .content-wrapper {
    width: 100%;
    flex: 1;

    .phone-wrapper {
      position: relative;
      width: 100%;
      overflow: hidden;

      .bgimg {
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate3d(-50%, -50%, 0);
        background-repeat: no-repeat;
      }

      >>>.canvas-container {
        margin: 0 auto;
      }

      .bg-box {
        position: absolute;
        left: 0;
        top: 0;
        right: 0;
        bottom: 0;
        height: 466px;
        background-size: 186px 388px;
        z-index: 1;
      }

      .up-box {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 466px;
        background-size: 186px 388px;
        background-repeat: no-repeat;
        background-position: 50% 50%;
        background-clip: content-box;
        z-index: 10;

        .icon {
          position: absolute;
          top: -10px;
          left: -10px;
          font-size: 18px;
          color: #fff;
          background-color: red;
          border-radius: 50%;
        }
      }

      .activeBox {
        position: absolute;
        left: 20%;
        top: 0;
        border: 1px solid red;
        opacity: 1;

        .icon {
          position: absolute;
          top: -10px;
          left: -10px;
          font-size: 18px;
          color: #fff;
          background-color: red;
          border-radius: 50%;
        }
      }

      .dpi-box {
        position: absolute;
        top: 20px;
        right: 20px;
        border-radius: 50%;
        z-index: 999;

        .dpi-icon {
          position: relative;
          display: inline-block;
          width: 30px;
          height: 30px;
          line-height: 30px;
          padding: 3px;
          box-sizing: border-box;
          border-radius: 50%;
          text-align: center;
          z-index: 1000;

          .icon {
            position: absolute;
            display: inline-block;
            top: 50%;
            left: 50%;
            width: 24px;
            height: 24px;
            line-height: 24px;
            border-radius: 50%;
            font-size: 18px;
            color: #ffffff;
            transform: translate3d(-50%, -50%, 0);
            background-size: 100% 100%;
            background-repeat: no-repeat;

            &.success {
              bg-image('success');
            }

            &.warning {
              bg-image('warning');
            }

            &.close {
              bg-image('close');
            }
          }

          &.clearly {
            background-color: #a0eea8;

            .icon {
              background-color: #2AC466;
            }
          }

          &.general {
            background-color: #ffcc77;

            .icon {
              background-color: #f6a92b;
            }
          }

          &.dim {
            background-color: #ffab9b;

            .icon {
              background-color: #f07159;
            }
          }
        }

        .desc {
          position: absolute;
          top: 0;
          right: 1px;
          width: 0;
          height: 30px;
          padding-right: 30px;
          line-height: 30px;
          font-size: 12px;
          color: #ffffff;
          text-align: center;
          border-radius: 72px;
          transition: all 0.4s;
          opacity: 0;
          overflow: hidden;
          z-index: 998;

          &.clearly {
            background-color: #2AC466;
          }

          &.general {
            background-color: rgba(246, 169, 43, 1);
          }

          &.dim {
            background-color: rgba(240, 113, 89, 1);
          }
        }

        .text {
          margin-top: 4px;
          text-align: center;
          font-size: 12px;
          color: #4A4A4A;
        }
      }

      .color-icon {
        display: inline-block;
        position: absolute;
        top: 80px;
        right: 20px;
        width: 30px;
        height: 30px;
        bg-image('color');
        background-size: 30px 30px;
        z-index: 99;
      }
    }

    .brand-type {
      display: flex;
      flex-direction: column;
      font-size: 14px;

      .item {
        display: inline-block;
        line-height: 30px;
        text-align: center;
        font-size: 14px;
        color: #333333;

        .title {
          color: #F21E1C;
          font-weight: bold;
        }
      }

      .dpi-type {
        margin-bottom: 5px;

        >>>.el-rate {
          display: inline-block;

          .el-rate__icon {
            margin-right: 0;
          }
        }

        .name {
          padding: 0 5px;
        }

        .dpi-desc {
          font-size: 12px;
          font-style: normal;
        }
      }

      .phone-brand {
        .name {
          padding-bottom: 2px;
          border-bottom: 1px solid #F21E1C;
        }
      }

      .phone-type {
        .name {
          padding-bottom: 2px;

          &.border-bottome {
            border-bottom: 1px solid #F21E1C;
          }
        }
      }
    }
  }

  .nav-wrapper {
    position: fixed;
    left: 0;
    right: 0;
    bottom: 0;
    flex: none;
    display: flex;
    height: 54px;
    padding: 0 5px;
    align-items: center;
    background: rgba(255, 255, 255, 0.9);
    box-shadow: 0 2px 12px 0 rgba(235, 232, 232, 0.5);
    z-index: 98;

    .nav-list {
      display: flex;
      flex: 1;

      .nav-item {
        flex: 1;
        text-align: center;

        .icon {
          display: inline-block;
          width: 22px;
          height: 22px;
          margin-bottom: 10px;
          background-size: 22px 22px;
          background-repeat: no-repeat;
        }

        .icon-pics {
          bg-image('m-icon/icon_pic');
        }

        .icon-upload {
          bg-image('m-icon/icon_camera');
        }

        .icon-text {
          bg-image('m-icon/icon_text');
        }

        .icon-temp {
          bg-image('m-icon/icon_temp');
        }

        .text {
          margin-top: -8px;
          font-size: 10px;
          text-align: center;
          color: #4A4A4A;
        }
      }
    }

    .btn-submit {
      display: flex;
      justify-content: center;
      flex-direction: column;
      width: 45%;
      height: 36px;
      margin: 0 5px;
      line-height: 36px;
      font-size: 16px;
      color: #ffffff;
      text-align: center;
      border-radius: 20px;
      background: linear-gradient(90deg, rgba(255, 98, 0, 1) 0%, rgba(242, 30, 28, 1) 100%);

      .price {
        display: block;
        margin-top: 2px;
        font-size: 14px;
        line-height: 16px;
      }

      .text {
        font-size: 12px;
        line-height: 16px;
      }

      &.disable {
        color: #333;
        background: #f4f4f4;
      }
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
    background-color: #ffffff;
    z-index: 100;

    .title {
      text-align: left;

      .line {
        display: inline-block;
        width: 3px;
        height: 14px;
        margin-left: -15px;
        border-radius: 0 50% 50% 0;
        background: linear-gradient(90deg, rgba(255, 98, 0, 1) 0%, rgba(242, 30, 28, 1) 100%);
      }

      .text {
        display: inline-block;
        margin-left: 15px;
        font-size: 16px;
        color: #4A4A4A;
      }

      .icon {
        display: inline-block;
        font-size: 20px;
        float: right;
      }
    }

    .el-input, .input-text {
      width: 100%;
      margin-top: 30px;
      border-radius: 8px;
    }

    >>>.el-input__inner, >>>.van-cell__value {
      height: 36px;
      line-height: 36px;
      border: none;
      background-color: rgba(155, 155, 155, 0.09);
    }

    .el-select {
      width: 100%;
      margin-top: 20px;
      border-radius: 8px;
    }

    >>>.el-input__inner {
      height: 36px;
      line-height: 36px;
      border: none;
      background-color: rgba(155, 155, 155, 0.09);
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
      margin: 40px 0 50px;
      display: flex;

      .color {
        display: inline-block;
        margin-top: 2px;
        width: 40px;
        height: 40px;
        border-radius: 2px;
        box-shadow: 1px 1px 5px rgba(0, 0, 0, 0.2);
      }

      .vc-slider {
        flex: 1;
        margin-left: 30px;

        >>>.vc-slider-hue-warp, >>>.vc-slider-swatches {
          box-shadow: 1px 1px 5px rgba(0, 0, 0, 0.2);
        }
      }
    }

    .btn-box {
      display: flex;
      flex-wrap: nowrap;
      align-content: flex-center;
      align-items: center;
      justify-content: space-around;
      margin-top: 36px;

      .btn {
        display: inline-block;
        width: 140px;
        height: 36px;
        line-height: 36px;
        font-size: 14px;
        text-align: center;
        border-radius: 20px;
        color: #ffffff;
      }

      .btn-cancel {
        background-color: rgba(0, 0, 0, 0.2);
      }

      .btn-submit {
        background: linear-gradient(90deg, rgba(255, 98, 0, 1) 0%, rgba(242, 30, 28, 1) 100%);
      }
    }
  }

  .pic-wrapper {
    position: fixed;
    left: 0;
    bottom: 0;
    width: 100%;
    height: calc(100vh - 150px);
    box-sizing: border-box;
    border-radius: 16px 16px 0 0;
    background-color: #ffffff;
    z-index: 1000;

    .title {
      text-align: left;
      padding: 0 5px 0 15px;
      height: 40px;
      line-height: 40px;

      .text {
        display: inline-block;
        font-size: 16px;
        font-weight: 600;
        color: #333333;
      }

      .icon {
        display: inline-block;
        font-size: 24px;
        padding: 0 10px;
        height: 40px;
        line-height: 40px;
        color: $color-text-h;
        float: right;
      }
    }
  }

  .color-wrapper {
    position: fixed;
    left: 0;
    right: 0;
    bottom: 0;
    padding: 15px 15px 20px;
    box-sizing: border-box;
    border-radius: 16px 16px 0 0;
    background-color: #ffffff;
    overflow-y: scroll;
    z-index: 100;
    -webkit-overflow-scrolling: touch;

    &::-webkit-scrollbar {
      display: none;
    }

    .title {
      text-align: left;

      .line {
        display: inline-block;
        width: 3px;
        height: 14px;
        margin-left: -15px;
        border-radius: 0 50% 50% 0;
        background: $color-theme;
        vertical-align: middle;
      }

      .text {
        display: inline-block;
        margin-left: 15px;
        font-size: 16px;
        color: #4A4A4A;
        vertical-align: middle;
      }

      .icon {
        display: inline-block;
        font-size: 20px;
        float: right;
      }
    }

    .color-list {
      width: 100%;
      margin: 40px 0 0;
      padding: 0 20px;
      box-sizing: border-box;

      .el-radio-group {
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;

        .el-radio {
          display: inline-block;
          margin: 0 auto 30px;
          width: 16.66%;
          height: 40px;
          text-align: center;

          >>>.el-radio__input {
            display: none;
            height: auto;

            .radio-icon {
              display: inline-block;
              position: relative;
              width: 27px;
              height: 27px;
              border-radius: 50%;
              border: 1px solid #ffffff;

              .radio-icon-r {
                display: inline-block;
                position: absolute;
                top: 50%;
                left: 50%;
                width: 20px;
                height: 20px;
                border-radius: 50%;
                transform: translate3d(-50%, -50%, 0);
              }
            }

            &.van-radio__icon--checked {
              .radio-icon {
                border-color: inherit;
              }
            }
          }

          >>> .el-radio__label {
            display: inline-block;
            margin: 0;
            padding: 0;
            height: auto;

            .radio-icon {
              position: relative;
              display: inline-block;
              width: 26px;
              height: 26px;
              border-radius: 50%;

              .radio-icon-r {
                display: inline-block;
                position: absolute;
                top: 50%;
                left: 50%;
                width: 20px;
                height: 20px;
                border-radius: 50%;
                transform: translate3d(-50%, -50%, 0);
              }
            }
          }
        }

        .color-item {
          display: inline-block;
          margin-bottom: 30px;
          width: 16.66%;
          height: 30px;
          text-align: center;

          .color-div {
            display: inline-block;
            width: 30px;
            height: 30px;
            background-color: #333;
            border-radius: 50%;
          }
        }
      }
    }

    .btn-box {
      display: flex;
      flex-wrap: nowrap;
      align-content: flex-center;
      align-items: center;
      justify-content: space-around;
      margin-top: 20px;

      .btn {
        display: inline-block;
        width: 140px;
        height: 36px;
        line-height: 36px;
        font-size: 14px;
        text-align: center;
        border-radius: 20px;
        color: #ffffff;
      }

      .btn-cancel {
        background-color: rgba(0, 0, 0, 0.2);
      }

      .btn-submit {
        background: linear-gradient(90deg, rgba(255, 98, 0, 1) 0%, rgba(242, 30, 28, 1) 100%);
      }
    }
  }

  .image-wrapper {
    position: fixed;
    left: 0;
    right: 0;
    bottom: 0;
    max-height: 644px;
    padding: 20px 15px 20px;
    box-sizing: border-box;
    border-radius: 16px 16px 0 0;
    background-color: #ffffff;
    z-index: 100;

    .title {
      text-align: left;

      .line {
        display: inline-block;
        width: 3px;
        height: 14px;
        margin-left: -15px;
        border-radius: 0 50% 50% 0;
        background: linear-gradient(90deg, rgba(255, 98, 0, 1) 0%, rgba(242, 30, 28, 1) 100%);
      }

      .text {
        display: inline-block;
        margin-left: 15px;
        font-size: 16px;
        color: #4A4A4A;
      }

      .icon {
        display: inline-block;
        font-size: 20px;
        float: right;
      }
    }

    .photograph {
      text-align: left;
      border-bottom: 1px solid #F4F4F4;

      input {
        display: none;
      }

      .text {
        display: block;
        padding: 25px 20px;
        height: 18px;
        line-height: 18px;
        font-size: 14px;
        color: #4A4A4A;
      }

      .icon {
        display: inline-block;
        width: 18px;
        height: 18px;
        float: right;
        bg-image('camera');
        background-size: 18px 18px;
        background-repeat: no-repeat;
      }
    }

    .photo {
      text-align: left;

      input {
        display: none;
      }

      .text {
        display: block;
        padding: 25px 20px;
        height: 18px;
        line-height: 18px;
        font-size: 14px;
        color: #4A4A4A;
      }

      .icon {
        display: inline-block;
        width: 18px;
        height: 18px;
        float: right;
        bg-image('photo');
        background-size: 18px 18px;
        background-repeat: no-repeat;
      }

      .upload-pic {
        >>>.el-upload-list {
          display: none;
        }

        >>>.el-upload {
          width: 100%;
          text-align: left;

          .text {
            display: inline-block;
            height: 18px;
            line-height: 18px;
            font-size: 14px;
            color: #4A4A4A;
          }

          .icon {
            display: inline-block;
            width: 18px;
            height: 18px;
            float: right;
            bg-image('photo');
            background-size: 18px 18px;
            background-repeat: no-repeat;
          }
        }
      }
    }
  }

  .el-dialog__wrapper {
    overflow: hidden;

    >>>.el-dialog {
      position: absolute;
      top: 50%;
      left: 50%;
      margin: 0 !important;
      padding: 20px 25px;
      background-color: #FFFFFF;
      border-radius: 10px;
      transform: translate3D(-50%, -50%, 0);

      .el-dialog__header {
        padding: 0 0 20px;

        .el-dialog__title {
          font-size: 16px;
          color: #333333;
        }

        .el-dialog__headerbtn {
          display: none;
        }
      }

      .el-dialog__body {
        padding: 0;
        max-height: 50vh;
        overflow-y: scroll;

        &::-webkit-scrollbar {
          width: 0;
          display: none;
        }

        .item-content {
          display: flex;
          padding: 10px;
          margin-bottom: 20px;
          background-color: #ffffff;
          border-radius: 4px;
          align-items: center;

          .item-img {
            display: inline-block;
            width: 65px;
            height: 65px;
            margin-right: 16px;
            border-radius: 4px;
            background-color: #999;
          }

          .item-box {
            flex: 1;
            text-align: left;
            overflow: hidden;
            margin-left: 30px;

            .name {
              font-size: 16px;
              color: #999999;

              &.xs {
                font-size: 14px;
              }

              .text {
                color: #333333;
              }
            }

            .dec {
              display: flex;
              margin-top: 14px;
              font-size: 14px;
              color: #999999;

              .left {
                display: inline-block;
              }

              .text {
                flex: 1;
                flex-wrap: wrap;
                line-height: 1.2;
                margin-top: -1px;
                color: #333333;
              }
            }
          }
        }
      }

      .el-dialog__footer {
        text-align: center;
        padding: 0 0 10px;

        .dialog-footer {
          display: flex;
          flex-direction: row;
          justify-content: space-between;
          text-align: center;
          padding: 30px 0 10px;

          .el-button {
            flex: 1;
            justify-content: space-between;
            display: inline-block;
            height: 40px;
            padding: 0;
            margin: 0 10px !important;
            line-height: 40px;
            text-align: center;
            font-size: 14px;
            color: #ffffff;
            border-radius: 20px;
            border-color: transparent;

            &.el-button--primary {
              background: #f21e1c;
              margin: 0 auto;
            }

            &.el-button--default {
              color: #333333;
              background: #F4F4F4;
            }
          }
        }

        .btn-submit {
          display: inline-block;
          width: 140px;
          height: 36px;
          line-height: 36px;
          text-align: center;
          font-size: 14px;
          color: #ffffff;
          background: linear-gradient(90deg, rgba(255, 98, 0, 1) 0%, rgba(242, 30, 28, 1) 100%);
          border-radius: 20px;
        }
      }
    }
  }

  .phone-picker {
    >>>.picker {
      top: 50%;
      padding-bottom: 50px;
      transform: translateY(-50%);
      border-radius: 10px;

      .picker-title {
        &::after {
          border-bottom: none;
        }

        .pt-cancel {
          display: none;
        }

        .pt-submit {
          position: fixed;
          bottom: 20px;
          left: 50%;
          border-radius: 20px;
          color: #ffffff !important;
          transform: translateX(-50%);
          width: 140px;
          background: linear-gradient(90deg, rgba(255, 98, 0, 1) 0%, rgba(242, 30, 28, 1) 100%);
          height: 36px;
          line-height: 36px;
          z-index: 10001;
        }
      }

      .picker-panel {
        .picker-wheel-wrapper {
          .picker-wheel {
            flex: none;

            &:nth-child(1) {
              width: 35%;
            }

            &:nth-child(2) {
              width: 65%;
            }
          }
        }
      }
    }
  }

  .mask {
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 99;
    background: rgba(0, 0, 0, 0.2);
    pointer-event: none;
  }

  .fadePic-enter-active, .fadePic-leave-active {
    transition: all 0.3s;
  }

  .fadePic-enter, .fadePic-leave-to {
    transform: translate3d(0, 100%, 0);
  }

  .fadeImage-enter-active, .fadeImage-leave-active {
    transition: all 0.3s;
  }

  .fadeImage-enter, .fadeImage-leave-to {
    transform: translate3d(0, 100%, 0);
  }

  .fadeColor-enter-active, .fadeColor-leave-active {
    transition: all 0.3s;
  }

  .fadeColor-enter, .fadeColor-leave-to {
    transform: translate3d(0, 100%, 0);
  }

  .fadeText-enter-active, .fadeText-leave-active {
    transition: all 0.3s;
  }

  .fadeText-enter, .fadeText-leave-to {
    transform: translate3d(0, 100%, 0);
  }

  .image-tips {
    position: fixed;
    left: 5%;
    width: 90%;
    background-color: #fff;
    border-radius: 4px;
    border-radius: 8px;
    z-index: 100;

    .title {
      padding-top: 18px;
      padding-left: 15px;
      font-size: 18px;
      color: #4A4A4A;
      bg-image('remind');
      background-size: 20px 20px;
      background-position: calc(50% - 40px) 18px;
      background-repeat: no-repeat;
    }

    .content {
      margin: 0;
      padding: 0 15px;
      font-size: 14px;
      color: #666666;
      text-align: left;
      line-height: 24px;
      white-space: pre-wrap;
    }

    .btn-wrap {
      border-top: 1px solid #EBEBEB;
      display: flex;

      .btn {
        flex: 1;
        padding: 15px 0;
        font-size: 18px;
        color: #4A4A4A;
        text-align: center;

        &.btn-confirm {
          position: relative;
          color: #f21e1c;
          border-left: 1px solid #EBEBEB;

          .input-file {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            opacity: 0;
          }
        }
      }
    }
  }
}

.flexible-dialog{
  	position: fixed;
  	top: 0;
  	left: 0;
  	width: 100%;
  	height: 100%;
  	background-color: rgba(0, 0, 0, 0.5);
  	z-index: 1000;
	display: flex;
	justify-content center;
	align-items: center;
	.flexible-dialog-content {
	  position: absolute;
	  width: 90%;
	  background-size: 100% 100%;
	  height: 390px;
	  background-color #ffffff;
	  border-radius 12px
	  display flex;
	  align-items: center;
	  justify-content: center;
	  flex-direction: column;
	  .flexible-cotent-top{
		  width: 90%;
		  height: 100px;
		  display flex;
		  align-items: center;
		  justify-content: center;
		  .flexible-cotent-top-img{
			  width: 100%;
			  height: auto;
		  }
	  }
	  
	  .flexible-cotent-middle-title{
		  width: 85%;
		  height: 50px;
		  display flex;
		  align-items: center;
		  justify-content: center;
		  margin-top 20px
		  font-size 18px;
		  color: #333333;
		  font-weight: bold;
	  }
	  
	  .flexible-cotent-middle-info{
		  width: 85%;
		  display flex;
		  justify-content space-between;
		  font-size 15px;
		  color: #666666;
		  letter-spacing: 0.8px;
		  margin-top 5px
		  line-height: 23px;
		  text-align: center;
	  }
	  
	  .flexible-cotent-bottom{
		  width: 195px;
		  height: 43px;
		  line-height: 43px;
		  text-align: center;
		  display flex;
		  justify-content: center;
		  font-size 18px;
		  color: #ffffff;
		  background: linear-gradient(90deg, #ff6200 0%, #f21e1c 100%);
		  border-radius: 21.5px
		  margin-top: 40px;
	  }
	}
}
</style>

<style>
.confirm-v-dialog-upload {
  width: 90%;
  border-radius: 8px;
  padding: 0;
}
.confirm-v-dialog-upload .el-message-box__header {
  padding-top: 18px;
  font-size: 18px;
  color: #4a4a4a;
}
.confirm-v-dialog-upload .el-message-box__header .el-warning {
  display: inline-block;
  width: 20px;
  height: 20px;
  margin-right: 2px;
  background: #ffffff url("../../common/images/remind@2x.png") center no-repeat;
  background-size: 20px 20px;
}
.confirm-v-dialog-upload .el-message-box__content {
  font-size: 14px;
  color: #666666;
  text-align: left;
  line-height: 24px;
}

.confirm-v-dialog-upload .el-message-box__btns {
  display: flex;
  padding: 0;
}
.confirm-v-dialog-upload .el-message-box__btns .el-button {
  flex: 1;
  margin: 0;
  padding: 0;
  height: 50px;
  line-height: 50px;
  font-size: 18px;
  color: #4a4a4a;
  text-align: center;
  background-color: #fff;
  border-color: transparent;
  border-radius: 0;
  border-top: 1px solid #ebedf0;
}

.confirm-v-dialog-upload .el-message-box__btns .el-button--primary {
  color: #f21e1c;
  border-left: 1px solid #ebedf0;
}

.el-message {
  min-width: 320px;
  padding: 15px 8px;
}
.el-message__icon {
  margin-right: 5px;
}
.el-message--warning {
  color: #ffffff;
  border: none;
  background-color: rgba(0, 0, 0, 0.9);
}
</style>
