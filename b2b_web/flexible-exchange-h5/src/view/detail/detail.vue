<!--
 * @Author: yaowei
 * @Date: 2019-05-10 14:09:37
 * @LastEditors: yaowei
 * @LastEditTime: 2019-12-06 19:30:05
-->
<template>
  <div class="detail-wrap" :class="isLoading ? 'noclick' : ''">
    <THeader
      class="header-wrap"
      :title="hasBg ? '详情' : ''"
      :darkIcon="false"
      :hasBg="hasBg"
      v-show="!isMiniProgram"
    ></THeader>
    <div class="content">
      <div
        class="pic-wrap"
        :style="{ height: wheight + 'px' }"
        ref="contentWrap"
      >
        <div
          class="bg-img"
          :style="
            'background-image: url(' +
            detailData.originImage +
            '?x-oss-process=image/resize,h_400,l_400)'
          "
        ></div>
        <div
          v-if="modelId"
          class="phone-wrapper"
          ref="phoneWrapper"
          :style="{ height: wheight + 'px' }"
        >
          <draw-container
            ref="mcanvas"
            @dpi="getdpi"
            @submit="submit"
            @handleLoading="handleLoading"
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
            :canEdit="false"
          >
          </draw-container>
        </div>
        <img
          v-else
          :src="
            detailData.noCameraVacancyPreview +
            '?x-oss-process=image/resize,h_400,l_400'
          "
          :alt="picName"
        />
      </div>
      <div class="info-wrap">
        <h6 class="title" v-show="detailData.pictureName">
          {{ detailData.pictureName }}
        </h6>

        <!-- 材质/颜色 -->
        <Material
          class="material-wrap"
          :scanMaterial="scanMaterial"
          :materialName="materialName"
          :colorName="colorName"
          :materialId="materialId"
          :materialList="materialList"
          :colorList="colorList"
          :isDetail="true"
          @changeMaterial="handleChangeMaterial"
          @changeColor="handleChangeColor"
        ></Material>

        <van-cell
          class="sub-title"
          title="选择型号"
          is-link
          :value="modelName ? modelName : '请选择机型'"
          @click="handleModel"
        />
      </div>

      <!-- 产品详情 -->
      <Divider class="divider-wrap" :text="'产品详情'"></Divider>

      <!-- 材质介绍 -->
      <div class="material-intro" v-show="hasDetail">
        <MaterialDetail :materialInfo="detailIntro"></MaterialDetail>
        <div
          class="img-wrap"
          v-show="detailIntro.content"
          v-html="detailIntro.content"
        ></div>
      </div>

      <NoData v-show="!hasDetail" :showBtn="false"></NoData>
    </div>

    <div
      class="fixed-b hasPrice"
      v-if="platform === 'GF60006' || platform === 'GF60007' || platform === 'GF60008'"
    >
      <p class="price"><span>¥</span>{{ Number(price).toFixed(2) }}</p>
      <div class="btn-wrap">
        <p class="btn border-btn" @click="handleCustom">选图定制</p>
        <p class="btn confirm-btn" @click="handleSubmit">加入购物车</p>
      </div>
    </div>
    <div class="fixed-b btn-wrap" v-else>
      <p class="btn border-btn" @click="handleCustom">选图定制</p>
      <p class="btn confirm-btn" @click="handleSubmit">立即兑换</p>
    </div>

    <div class="mask" v-show="showMask"></div>

    <!-- 机型 -->
    <transition name="fadeDialog">
      <div class="model-wrap" v-show="showModel">
        <Model
          class="model"
          :type="'bottom'"
          :mobile="modelName"
          :modelList="modelList"
          :isDetail="true"
          @cancel="handleCancel"
          @confirm="handleConfirm"
        ></Model>
      </div>
    </transition>

    <Loading v-show="isLoading" :message="message"></Loading>
  </div>
</template>

<script>
// 组件
import THeader from "components/tHeader/tHeader";
import DrawContainer from "components/diyCanvas/diyCanvas";
import Divider from "components/divider/divider";
import Material from "components/material/material";
import MaterialDetail from "components/material/detail";
import Model from "components/model/model";
import NoData from "components/noData/noData";
import Loading from "components/loading/loading";
// js
import wx from "weixin-js-sdk";
// api
import api from "api/allApi.js";

export default {
  name: "Detail",
  data() {
    return {
      distributorId: "", // 分销商id
      platform: "", // 平台
      showModel: false, // 是否显示型号
      showMask: false, // 蒙版
      modelList: [], // 型号列表
      modelId: "", // 当前型号id
      modelName: "", // 当前选择机型
      brandId: "", // 当前机型品牌id
      brandName: "", // 当前机型品牌名称
      materialId: "", // 当前材质id
      pictureId: 0, // 当前图片id
      picName: "", // 当前图片名称
      scanMaterial: "", // 扫码材质
      materialName: "", // 当前材质
      materialList: [], // 材质列表
      colorName: "", // 当前颜色
      curMaterialIndex: 0, // 当前材质索引
      colorList: [], // 颜色列表
      detailData: {}, // 详情数据
      detailIntro: {}, // 详情介绍
      hasDetail: true, // 是否有详情介绍
      userId: "", // 用户id
      isLoading: false, // 加载
      message: "", // 加载内容
      hasBg: false, // header是否有背景色

      // 定制信息
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
      materialsColorValue: "",
      spriteArr: [],
      manufactor: "",
      materialsType: "",
      nw: "",
      nh: "",
      mw: "",
      mh: "",
      bgimg: "",
      upimg: "",
      pwidth: "",
      pheight: "",
      wwidth: "",
      wheight: 280,
      pwidth2: "",
      pheight2: "",
      curPicType: "",
      ratio: 1, // 缩放比
      price: 0,
      skuNo: 0, // skuNo
      skuName: "",
      isMake: "",
      isMiniProgram: false, // 是否是小程序
      isZFold: false, 
      loadingTimer: null, // 加载定时器（提示网络较差）
    };
  },
  created() {
    var enterFlag = this.getQueryVariable("enterFlag");
    if (enterFlag === "details") {
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

        this.modelName = decodeURIComponent(enterParams.modelName);
        this.materialId = Number(enterParams.materialId);
        this.modelId = Number(enterParams.modelId) || "";
        this.brandId = enterParams.brandId;
        this.brandName = decodeURIComponent(enterParams.brandName);

        // 获取主题详情 - 图片/材质
        this.pictureId = Number(enterParams.pictureId);
        this.picName = decodeURIComponent(enterParams.picName);
        this.curPicType = Number(enterParams.picType);

        this.detailData = {
          pictureName: this.picName,
          originImage: enterParams.originImage,
          noCameraVacancyPreview: enterParams.noCameraVacancyPreview,
        };

        this.isMiniProgram = true;
        sessionStorage.setItem("isMiniProgram", this.isMiniProgram);
      }
    } else {
      this.modelName = this.$route.query.modelName || "";
      this.materialId = Number(this.$route.query.materialId);
      this.modelId = Number(this.$route.query.modelId) || "";
      this.brandId = sessionStorage.getItem("brandId");
      this.brandName = sessionStorage.getItem("brandName");

      // 获取主题详情 - 图片/材质
      this.pictureId = Number(this.$route.query.picture_id);
      this.picName = this.$route.query.picName;
      this.curPicType = Number(this.$route.query.picType);

      this.detailData = {
        pictureName: this.picName,
        originImage: this.$route.query.originImage,
        noCameraVacancyPreview: this.$route.query.noCameraVacancyPreview,
      };
    }

    this.isMiniProgram = sessionStorage.getItem("isMiniProgram") || false;

    this.distributorId = localStorage.getItem("distributorId");
    this.platform = localStorage.getItem("platform") || 28;
    this.userId = localStorage.getItem("userId");

    this.message = "数据加载中";
    this.isLoading = true;

    this.getMaterialList(this.pictureId);
  },
  mounted() {
    window.addEventListener("scroll", this.handleScroll, true);
  },
  destroyed() {
    window.removeEventListener("scroll", this.handleScroll, true);
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
    // 页面滚动
    handleScroll: function () {
      let offsetTop = Math.abs(
        this.$refs.contentWrap.getBoundingClientRect().top
      );
      let offsetHeight = this.$refs.contentWrap.getBoundingClientRect().height;
      if (offsetTop >= offsetHeight - 30) {
        this.hasBg = true;
      } else {
        this.hasBg = false;
      }
    },
    // 根据型号获取材质列表
    getMaterialList(picId, type) {
      this.$api
        .get(this, api.getMaterialList, {
          pictureId: picId,
          categoryId: 1, // 产品类型id：1 手机壳
          distributorId: this.distributorId,
          modelId: this.modelId,
          platform: this.platform, // 平台
        })
        .then((res) => {
          if (res.success) {
            if (res.data && res.data.length > 0) {
              this.materialList = res.data;

              if (Number(this.platform) === 28) {
                this.materialList.forEach((parent) => {
                  if (parent.childrenList && parent.childrenList.length > 0) {
                    parent.childrenList.forEach((son) => {
                      // 根据材质id获取可用兑换卡数量
                      if (this.userId) {
                        this.getCardNumByMaterial(son);
                      }
                    });
                  }
                });
              }

              // 获取主题详情 - 介绍
              this.getDetailIntro(
                this.materialId
                  ? this.materialId
                  : this.materialList[0].childrenList[0].materialId
              );

              if (type) {
                // 切换型号，默认选择第一个材质
                this.materialId = this.materialId
                  ? this.materialId
                  : this.materialList[0].childrenList[0].materialId;
                this.skuNo =
                  this.materialList[0].childrenList.length > 0
                    ? this.materialList[0].childrenList[0].itemCode
                    : "";
              } else {
                if (this.modelId) {
                  this.materialId = this.materialId
                    ? this.materialId
                    : this.materialList[0].childrenList[0].materialId;
                  this.skuNo =
                    this.materialList[0].childrenList.length > 0
                      ? this.materialList[0].childrenList[0].itemCode
                      : "";
                }
              }

              this.getModelList();

              // 获取当前选择材质对应颜色
              this.getCurrentMaterial(type);
            } else {
              this.message = "";
              this.isLoading = false;
            }
          } else {
            this.message = "";
            this.isLoading = false;
          }
        });
    },
    // 获取主题详情 - 介绍
    getDetailIntro(materialId) {
      this.$api
        .get(this, api.getMaterialDetail, {
          id: materialId,
        })
        .then((res) => {
          if (!this.materialId || !this.modelId) {
            this.message = "";
            this.isLoading = false;
          }

          if (res.success) {
            this.detailIntro = res.data;

            if (
              res.data.content ||
              res.data.description ||
              res.data.subtitle ||
              res.data.image
            ) {
              // 判断是否有描述，有显示
              this.hasDetail = true;
            } else {
              this.hasDetail = false;
            }
          } else {
            this.$toast.fail(res.errMessage);
          }
        });
    },
    escape2Html(str) {
      var arrEntities = { lt: "<", gt: ">", nbsp: " ", amp: "&", quot: '"' };
      return str.replace(/&(lt|gt|nbsp|amp|quot);/gi, function (all, t) {
        return arrEntities[t];
      });
    },
    // 获取当前材质id获取对应颜色
    getCurrentMaterial(type) {
      if (this.materialList && this.materialList.length > 0) {
        let curPid = 0;
        let flag = false;
        this.materialList.forEach((parent, pIndex) => {
          if (parent.childrenList && parent.childrenList.length > 0) {
            parent.childrenList.forEach((son, index) => {
              if (Number(son.materialId) === Number(this.materialId)) {
                // 当前颜色
                this.colorName = son.name;
                this.materialsColorValue = son.colour;
                // 当前工厂
                this.manufactor = son.manufactor;

                this.skuNo = son.itemCode;

                // 当前材质索引
                this.curMaterialIndex = index;
                curPid = pIndex;
                flag = true;
              }
            });
          }
        });

        if (!flag) {
          // 当前颜色
          this.colorName = this.materialList[curPid].childrenList[0].name;
          this.materialsColorValue =
            this.materialList[curPid].childrenList[0].colour;
          // 当前工厂
          this.manufactor =
            this.materialList[curPid].childrenList[0].manufactor;

          this.skuNo = this.materialList[curPid].childrenList[0].itemCode;

          // 当前材质索引
          this.curMaterialIndex = 0;
        }

        if (!type) {
          // 扫码材质
          this.scanMaterial = this.materialList[curPid].name;
        }
        // 当前材质
        this.materialName = this.materialList[curPid].name;
        // 颜色列表
        this.colorList = this.materialList[curPid].childrenList;
      }
    },
    // 选择材质
    handleChangeMaterial(val) {
      this.message = "数据加载中";
      this.isLoading = true;

      this.materialName = val;
      this.changeColorByMaterial(val);
    },
    // 根据材质切换颜色
    changeColorByMaterial(materialName) {
      if (this.materialList && this.materialList.length > 0) {
        this.materialList.forEach((parent) => {
          if (parent.name === materialName) {
            if (parent.childrenList && parent.childrenList.length > 0) {
              // 当前颜色
              this.colorName = parent.childrenList[0].name;
              this.materialsColorValue = parent.childrenList[0].colour;
              // 当前工厂
              this.manufactor = parent.childrenList[0].manufactor;

              this.skuNo = parent.childrenList[0].itemCode;

              // 当前材质索引
              this.curMaterialIndex = 0;
              // 当前材质id
              this.materialId = parent.childrenList[0].materialId;
              this.getDetailIntro(this.materialId);
              this.getModelList();
              // 颜色列表
              this.colorList = parent.childrenList;
            }
          }
        });
      }
    },
    // 材质颜色
    handleChangeColor(item, index) {
      this.colorName = item.name;
      this.materialId = item.materialId;
      // 当前材质索引
      this.curMaterialIndex = index;
      this.getCurrentMaterialId(item.name);
    },
    // 获取当前颜色获取对应材质 id
    getCurrentMaterialId(colorName) {
      if (this.materialList && this.materialList.length > 0) {
        this.materialList.forEach((parent) => {
          if (parent.childrenList && parent.childrenList.length > 0) {
            parent.childrenList.forEach((son) => {
              if (son.name === colorName) {
                this.materialsColorValue = son.colour;
                this.materialId = son.materialId;
                // 当前工厂
                this.manufactor = son.manufactor;

                this.skuNo = son.itemCode;

                this.getModelList();
              }
            });
          }
        });
      }
    },
    // 选择型号
    handleModel() {
      if (!this.isLoading) {
        this.showModel = true;
        this.showMask = true;
        // this.getModelList();
      }
    },
    // 获取型号列表
    getModelList() {
      this.message = "数据加载中";
      this.isLoading = true;
      this.$api
        .get(this, api.getModelList, {
          categoryId: 1, // 产品类型id：1 手机壳
          distributorId: this.distributorId,
          materialId: this.materialId,
          pictureId: this.pictureId,
          platform: this.platform, // 平台
        })
        .then((res) => {
          if (res.success) {
            if (res.data && res.data.length > 0) {
              this.modelList = res.data;
              this.modelList.forEach((brand) => {
                if (brand.childrenList.length > 0) {
                  brand.childrenList.forEach((model) => {
                    if (
                      model.name.replace(/\s*/g, "").toLowerCase() ===
                      this.modelName.replace(/\s*/g, "").toLowerCase()
                    ) {
                      this.brandId = brand.modelId;
                      this.brandName = brand.name;
                    }
                  });
                }
              });

              if (this.modelId) {
                this.getInfo(this.materialId);
              }
            }
          } else {
            this.$toast.fail(res.errMessage);
          }
          this.message = "";
          this.isLoading = false;
        });
    },
    // 跳过（机型选择）
    handleCancel() {
      this.showModel = false;
      this.showMask = false;
    },
    // 确定（机型选择）
    handleConfirm(val) {
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

      // 重置图片
      this.imgData.posX = null;

      // 获取材质信息
      this.getMaterialList(this.pictureId, "hasChange");

      this.showModel = false;
      this.showMask = false;
    },
    // 根据材质id获取可用兑换卡数量
    getCardNumByMaterial(item) {
      this.$api
        .get(this, api.getCardNum, {
          userId: this.userId,
          materialId: item.materialId,
          distributorId: this.distributorId,
          status: 1, // 1、未使用 2、已使用 3、已过期
        })
        .then((res) => {
          if (res.success) {
            this.$set(item, "cardNum", res.data);
          } else {
            this.$toast.fail(res.errMessage);
          }
        });
    },
    // 选图定制
    handleCustom() {
      this.$router.push({
        name: "custom",
        query: {
          modelId: this.modelId,
          materialId: this.materialId,
          pid: this.pictureId,
          modelName: this.modelName,
          picUrl: this.detailData.originImage,
          picType: this.curPicType,
        },
      });
    },
    // 根据手机型号和材质获取手机定制信息
    getInfo(materialId) {
      let isZFold = this.modelName.toUpperCase().indexOf("FOLD3") >= 0 ? 1 : 0;
      this.isZFold = isZFold;
      // if (Number(sessionStorage.getItem("isZFold")) !== isZFold) {
      //   // 清空画布
      //   this.$refs.mcanvas.dragArr = [];
      //   this.$refs.mcanvas.clear();
      // }
      sessionStorage.setItem("isZFold", isZFold);
      this.$api
        .get(this, api.getCustomInfo, {
          modelId: this.modelId,
          materialId: materialId,
        })
        .then((res) => {
          this.message = "";
          this.isLoading = false;

          if (res.success) {
            if (res.data) {
              let modelInfo = res.data;
              this.phoneImg = modelInfo.floorImage;
              this.frameImg = modelInfo.outImage;
              // 手机图长宽比
              let pscale =
                this.pm2px(modelInfo.width) / this.pm2px(modelInfo.length);
              this.phonenHeight = this.wheight * 0.8;
              this.phonenWidth = this.phonenHeight * pscale;

              // 判断材质类型：74 玻璃壳，82 TPU，84 热升华，86 亲肤壳(晶透)，91 防摔壳
              switch (Number(materialId)) {
                case 74:
                  this.materialsType = 1;
                  break;
                case 82:
                case 91:
                  this.materialsType = 2;
                  break;
                case 84:
                  this.materialsType = 3;
                  break;
                case 86:
                  this.materialsType = 4;
                  break;
                default:
                  this.materialsType = 4;
                  break;
              }
              if (
                Number(materialId) === 74 ||
                Number(materialId) === 84
              ) {
                // 玻璃壳/热升华+2mm
                this.phoneHeight = this.wheight * 0.8 + this.pm2px(2);
                this.phoneWidth = this.phoneHeight * pscale;
                let mscale = modelInfo.width / modelInfo.length;
                if (modelInfo.width > modelInfo.length) {
                  this.mh = modelInfo.length + 2;
                  this.mw = this.mh * mscale;
                } else {
                  this.mw = modelInfo.width + 2;
                  this.mh = this.mw / mscale;
                }
                this.nh = modelInfo.length;
                this.nw = modelInfo.width;
              } else {
                // TPU/亲肤壳
                this.phoneHeight = this.wheight * 0.8;
                this.phoneWidth = this.phoneHeight * pscale;
                this.mh = this.nh = modelInfo.length;
                this.mw = this.nw = modelInfo.width;
              }

              sessionStorage.setItem("nw", this.nw.toFixed(2));
              sessionStorage.setItem("nh", this.nh.toFixed(2));

              // 透明间距
              let frameValue = {
                topFrame: modelInfo.topFrame || 0,
                underFrame: modelInfo.underFrame || 0,
                leftFrame: modelInfo.leftFrame || 0,
                rightFrame: modelInfo.rightFrame || 0,
              };
              sessionStorage.setItem("frameValue", JSON.stringify(frameValue));

              // 判断是否有切图信息（折叠屏）
              if (
                (modelInfo.cutType === 1 && modelInfo.slittingHeight) ||
                (modelInfo.cutType === 2 && modelInfo.crosscuttingWidth)
              ) {
                let cutInfo = JSON.stringify({
                  cutType: modelInfo.cutType,
                  slittingHeight: modelInfo.slittingHeight,
                  slittingWhite: modelInfo.slittingWhite,
                  crosscuttingWidth: modelInfo.crosscuttingWidth,
                  crosscuttingWhite: modelInfo.crosscuttingWhite,
                });
                sessionStorage.setItem("cutInfo", cutInfo);
              } else {
                sessionStorage.setItem("cutInfo", "");
              }

              // 是否强制铺满血位
              if (res.data.material) {
                let isAllPlace = parseInt(res.data.material.isAllPlace);
                sessionStorage.setItem("isAllPlace", isAllPlace);
              }

              this.getPrice(materialId);
            }
          } else {
            this.$toast.fail(res.errMessage);
          }
        });
    },
    // 获取价格
    getPrice(materialId) {
      this.$api
        .get(this, api.getPrice, {
          distributorId: this.distributorId,
          orderSource: this.platform,
          materialId: materialId,
        })
        .then((res) => {
          if (res.success) {
            this.price = res.data || 0;

            this.changeImage(
              {},
              this.detailData.originImage,
              this.pictureId,
              this.curPicType
            );
          } else {
            this.$toast.fail(res.errMessage);
          }
        });
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
    // mm转px
    pm2px(d) {
      var iswindow = /windows|win32|win64/i.test(navigator.userAgent);
      if (iswindow) {
        return Math.round((d * 96) / 25.4);
      } else {
        return Math.round((d * 72) / 25.4);
      }
    },
    getdpi(value, ratio) {
      this.ratio = ratio;
    },
    changeImage(item, url, id, type, cateType) {
      // cateType：1（支持多次上传）例：本地图片，2 贴纸
      if (this.materialId !== 0) {
        // type 0-Canva 1-普通素材 2-IP图片（不可更改） 3-模板（不可更改） 4-贴纸
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
          if (this.imgData.posX === null) {
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
            picType: this.curPicType,
            rotate: this.imgData.rotate,
            selected: true,
            index: null,
            cateType: cateType ? cateType : "",
            picId: this.pictureId ? this.pictureId : 0,
            scale: scale,
          };

          this.spriteArr = [];
          this.spriteArr.push(imgObj);
        });
      }
    },
    // 加载图片
    loadImage(url, type) {
      let img = new Image();
      img.crossOrigin = "anonymous";
      if (url.indexOf("data:") === 0 || type === 0) {
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
    // 确认提交
    handleSubmit() {
      if (this.modelId === "") {
        this.$toast.fail("请选择手机型号");
        return;
      }
      this.message = "作品制作中";
      this.isLoading = true;
      // 判断IP图片是否已经定制
      if (this.curPicType === 2) {
        this.isMake = false;
        // 获取是否已生成定制信息
        this.$api
          .get(this, api.getCacheIP, {
            pictureId: this.pictureId,
            materialId: this.materialId,
            modelId: this.modelId,
          })
          .then((res) => {
            if (res.success) {
              if (
                res.data &&
                (res.data.generateImage.indexOf(".pdf") > -1 ||
                  res.data.generateImage.indexOf(".png") > -1)
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
              this.$toast.fail(res.errMessage);
            }
          });
      } else {
        this.$refs.mcanvas.make();
      }
    },
    // 定制成功并跳转
    submit(image, generateImage) {
      // 保存定制数据
      let materialsName = this.colorName
        ? this.materialName + " - " + this.colorName
        : this.materialName;

      // 隐藏图层列表
      this.$refs.mcanvas.showHandleWrap = false;

      if (Number(this.curPicType === 2) && !this.isMake) {
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

      let categoryId = 1;
      let categoryName = "手机壳";

      // 加入购物车
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
        pictureId: this.pictureId,
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
          } else {
            this.$toast.fail(res.errMessage);
          }
          this.message = "";
          this.isLoading = false;
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
              picId: this.pictureId,
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
            } else {
              wx.miniProgram.navigateTo({
                url:
                  "/pages/login/login?enterFlag=shopcart&comingType=addShopCart&enterParams=" +
                  enterParams,
              });
            }
          }
          this.message = "";
          this.isLoading = false;
        });
    },
    // Loading
    handleLoading(isLoading, message) {
      this.message = message;
      this.isLoading = isLoading;
    },
  },
  watch: {
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
    THeader,
    DrawContainer,
    Divider,
    Material,
    MaterialDetail,
    Model,
    NoData,
    Loading,
  },
};
</script>

<style lang="stylus" scoped>
@import '~common/styles/variable.styl';
@import '~common/styles/mixin.styl';

.noclick {
  pointer-events: none;
}

.detail-wrap {
  position: fixed;
  width: 100%;
  top: 0;
  bottom: 0;
  background-color: $color-bg;
  overflow-y: scroll;
  z-index: 1;
  -webkit-overflow-scrolling: touch;
  scrollbar-width: none;
  -ms-overflow-style: none;

  &::-webkit-scrollbar {
    display: none;
  }

  .content {
    width: 100%;
    height: 100%;
    padding-bottom: 65px;
    overflow: hidden;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;
    scrollbar-width: none;
    -ms-overflow-style: none;

    &::-webkit-scrollbar {
      display: none;
    }
  }

  .pic-wrap {
    position: relative;
    width: 100%;
    overflow: hidden;

    .bg-img {
      position: absolute;
      top: 50%;
      left: 50%;
      width: calc(100% + 50px);
      height: calc(100% + 50px);
      filter: blur(20px);
      background-color: $color-bg-white;
      background-size: 100% 100%;
      background-position: center center;
      background-repeat: no-repeat;
      transform: translate(-50%, -50%);
      opacity: 0.5;
    }

    img {
      position: absolute;
      bottom: 21px;
      left: 50%;
      height: 224px;
      border-radius: $radius-base;
      transform: translateX(-50%);
    }
  }

  .info-wrap {
    padding: 28px $spacing-base $spacing-lg;
    background-color: $color-bg-white;

    .title {
      font-size: $font-lg;
      font-weight: 500;
      margin-bottom: 28px;
    }

    .sub-title {
      margin-top: 18px;
      font-size: $font-base;
      color: $color-font-base;

      &.van-cell {
        padding: 5px 0;

        >>>.van-cell__value, >>>.van-cell__right-icon {
          color: $color-font-base;
        }

        >>>.van-cell__value {
          flex: 2;
          font-size: $font-sm;
          white-space: nowrap;
        }
      }
    }
  }

  .divider-wrap {
    margin-top: $spacing-sm;
    padding: 28px 0 0;
    background-color: $color-bg-white;

    >>> .van-divider {
      margin: 0;
    }

    >>>.text {
      color: $color-font-base;
    }
  }

  .material-intro {
    padding: 30px 0 0;
    background-color: $color-bg-white;
  }

  .img-wrap {
    padding-top: 20px;
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

  .fixed-b {
    position: fixed;
    right: 0;
    bottom: 0;
    left: 0;
    padding: $spacing-sm $spacing-base;
    background-color: $color-bg-white;
    box-shadow: 0px -1px 5px -3px rgbaMain(0.5);
    z-index: 100;

    &.hasPrice {
      padding-left: 35%;

      .price {
        position: absolute;
        top: 50%;
        left: $spacing-base;
        font-size: 20px;
        color: $color-orange;
        transform: translateY(-50%);

        span {
          margin-right: 2px;
          font-size: $font-base;
        }
      }

      .btn {
        border-radius: 45px 0 0 45px;

        & + .btn {
          margin-left: 0;
          border-radius: 0 45px 45px 0;
        }
      }
    }
  }

  .btn-wrap {
    display: flex;

    .btn {
      flex: 1;
      lineHeight(45px);
      font-size: $font-lg;
      align(center);
      border-radius: 45px;

      & + .btn {
        margin-left: $spacing-base;
      }
    }
  }

  .fadeDialog-enter-active, .fadeDialog-leave-active {
    transition: all 0.3s;
  }

  .fadeDialog-enter, .fadeDialog-leave-to {
    transform: translate3d(0, 100%, 0);
  }

  .mask {
    mask();
    z-index: 100;
  }

  .model-wrap {
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
}
</style>