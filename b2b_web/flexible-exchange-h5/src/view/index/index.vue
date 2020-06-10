<!--
 * @Author: yaowei
 * @Date: 2019-05-08 09:24:27
 * @LastEditors: yaowei
 * @LastEditTime: 2019-12-07 11:43:02
-->
<template>
  <div class="home-wrap">
    <div class="content">
      <div class="home-top">
        <div class="cur-info">
          <p class="model" @click="handleShowModel">
            {{ modelName ? modelName : "请选择机型" }}
            <van-icon name="arrow" />
          </p>
          <p class="material">{{ materialName }}</p>
        </div>

        <!-- 轮播图 -->
        <van-swipe
          :autoplay="5000"
          class="banner-list"
          v-if="bannerList && bannerList.length > 0"
        >
          <van-swipe-item
            v-for="(banner, index) in bannerList"
            :key="index"
            @click="goDetailList(banner)"
          >
            <img :src="banner.bannerUrl" />
          </van-swipe-item>
        </van-swipe>

        <div class="banner-list" v-else>
          <img src="../../common/images/default.png" alt="banner" />
        </div>
      </div>

      <div class="menu-list-wrap">
        <div class="menu-list">
          <ul class="menu-t">
            <li
              @click="
                goPath('theme', {
                  modelId: modelId,
                  materialId: materialId,
                  modelName: modelName,
                })
              "
            >
              <span class="sprite-icon icon-theme"></span>
              <h6 class="menu-title">官方主题</h6>
              <p class="menu-con">海量IP授权图库</p>
            </li>
            <li
              @click="
                goPath('custom', {
                  modelId: modelId,
                  materialId: materialId,
                })
              "
            >
              <span class="sprite-icon icon-custom"></span>
              <h6 class="menu-title">传图定制</h6>
              <p class="menu-con">做自己的设计师</p>
            </li>
          </ul>
          <div class="menu-b" @click="goCode">
            <span class="sprite-icon icon-card"></span>
            <span class="menu-title">卡包中心</span>
            <div class="menu-con">
              <span v-if="userId">您有 {{ cardNum }} 张兑换卡可用</span
              ><van-icon name="arrow" />
            </div>
          </div>
        </div>
      </div>

      <!----公告----->
      <div class="notice-box" v-if="showNotice">
        <Notice></Notice>
      </div>

      <div
        class="recommend-wrap"
        ref="zyScroll"
        :style="'height: ' + windowHeight + 'px'"
      >
        <van-tabs
          class="recommend-title-wrap"
          sticky
          @click="handleClickSeries"
          :ellipsis="false"
        >
          <van-tab
            v-for="(series, index) in seriesList"
            :key="index"
            :title="series.themeName"
            :name="series.id"
          >
            <!-- 推荐 -->
            <div class="recommend-con" :class="{ active: curTab === 0 }">
              <van-list
                v-model="loadingRecommend"
                :finished="finishedRecommend"
                :immediate-check="false"
                @load="onLoadRecommend"
                :offset="8"
              >
                <ul class="recommend-list clearfix">
                  <li
                    v-for="(recommend, index) in recommendData"
                    :key="index"
                    @click="goDetail(recommend)"
                  >
                    <Goods :goodsInfo="recommend"></Goods>
                  </li>
                </ul>
              </van-list>

              <div
                class="no-data-wrap"
                v-show="recommendData && recommendData.length <= 0"
              >
                <NoData :showBtn="false"></NoData>
              </div>
              <!-- 底线 -->
              <Divider
                ref="divider"
                class="divider-wrap"
                :text="'你看到我的底线啦'"
                v-show="
                  recommendData && recommendData.length > 0 && finishedRecommend
                "
              ></Divider>
            </div>
            <!-- 系列 -->
            <div
              class="recommend-con"
              :class="{
                active: curTab === series.id,
              }"
              v-for="(series, pIndex) in seriesList"
              :key="pIndex"
              :data-index="pIndex + 1"
            >
              <van-list
                v-model="loadingSeries"
                :finished="finishedSeries"
                :immediate-check="false"
                @load="onLoadSeries"
                :offset="8"
              >
                <ul class="recommend-list clearfix">
                  <li
                    v-for="(item, index) in seriesData"
                    :key="index"
                    @click="goDetail(item)"
                  >
                    <Goods :goodsInfo="item"></Goods>
                  </li>
                </ul>
              </van-list>

              <div
                class="no-data-wrap"
                v-show="seriesData && seriesData.length <= 0"
              >
                <NoData :showBtn="false"></NoData>
              </div>
              <!-- 底线 -->
              <Divider
                ref="divider"
                class="divider-wrap"
                :text="'你看到我的底线啦'"
                v-show="seriesData && seriesData.length > 0 && finishedSeries"
              ></Divider>
            </div>
          </van-tab>
        </van-tabs>
      </div>
    </div>

    <Tabs :curTab="'index'"></Tabs>

    <!-- 选择机型 -->
    <div class="model-wrap" v-show="showModel">
      <Model
        class="model"
        :mobile="mobile"
        :modelList="modelList"
        :isDetail="true"
        @cancel="handleCancel"
        @confirm="handleConfirm"
      ></Model>
    </div>

    <Loading v-show="isLoading" :message="message"></Loading>
  </div>
</template>

<script>
// 组件
import Notice from "components/notice/notice";
import Model from "components/model/model";
import Goods from "components/goods/goods";
import Divider from "components/divider/divider";
import Tabs from "components/tabs/tabs";
import NoData from "components/noData/noData";
import Loading from "components/loading/loading";
// js
import wx from "weixin-js-sdk";
import MobileDetect from "common/js/getDevice/mobile-detect.min.js";
import { getRenderer } from "common/js/getDevice/renderer.min.js";
import { getModel } from "common/js/getDevice/iphone-device.js";
// api
import api from "api/allApi.js";
import { setToken } from "api/auth";

export default {
  name: "Home",
  data() {
    return {
      title: "",
      exchangeId: "", // 兑换 id
      showModel: false, // 是否显示型号
      modelList: [], // 型号列表
      modelName: "", // 当前选择机型
      materialList: [], // 材质列表
      materialName: "", // 当前选择材质
      materialId: "", // 当前选择材质id
      modelId: "", // 当前选择型号id
      pictureId: "", // 图片id
      distributorId: 2601, // 分销商id
      platform: 28, // 平台
      orderSource: 28, // 订单来源
      bannerList: [], // 轮播图
      zyFixed: false, // 是否吸顶
      windowHeight: 0, // 当前屏幕高度
      curTab: 0, // 当前tab
      // 推荐
      recommendData: [], // 推荐数据
      recommendPage: 0, // 当前分页
      recommendCount: 8, // 分页条数
      loadingRecommend: false, // 加载
      finishedRecommend: false, // 加载完成
      // 系列
      seriesId: "", // 系列id
      seriesList: [], // 系列列表
      seriesPage: 1, // 当前分页
      seriesSize: 8, // 分页条数
      seriesData: [], // 系列列表数据
      loadingSeries: false, // 加载
      finishedSeries: false, // 加载完成
      showNotice: false, // 显示公告
      cardNum: 0, // 可用兑换卡数量
      userId: "", // 用户id
      mobile: "", // 识别型号
      firstEnter: 0, // 是否第一次进入
      isLoading: true, // 加载
      message: "数据加载中", // 加载内容
    };
  },
  mounted() {
    localStorage.setItem("platform", this.platform);
    localStorage.setItem("orderSource", this.orderSource);
    localStorage.setItem("distributorId", this.distributorId);

    var enterFlag = this.getQueryVariable("enterFlag");
    if (enterFlag === "index") {
      var params = this.getQueryVariable("enterParams");
      var enterParams = JSON.parse(params);
      localStorage.setItem("userId", enterParams.userId);
      localStorage.setItem("phone", enterParams.phone);
      localStorage.setItem("userNo", enterParams.userNo);
      localStorage.setItem("auth", enterParams.auth);
      setToken(enterParams.auth);
      localStorage.setItem("openId", enterParams.openid);

      this.userId = enterParams.userId;
    } else {
      this.userId = localStorage.getItem("userId");
    }

    // 获取默认 exchangeId
    let exchangeId = this.getQueryVariable("exchangeId")
      ? this.getQueryVariable("exchangeId")
      : localStorage.getItem("exchangeId") &&
        localStorage.getItem("exchangeId") !== "undefined"
      ? localStorage.getItem("exchangeId")
      : 0;
    this.initExchangeId(exchangeId);

    this.modelName = sessionStorage.getItem("modelName");
    this.mobile = this.modelName;
    this.modelId = sessionStorage.getItem("modelId");

    this.firstEnter = sessionStorage.getItem("firstEnter") || 0;

    // 获取可用兑换卡数量
    if (this.userId) {
      this.getCardNum(this.userId);
    }

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
      this.windowHeight = window.screen.height;
      let offsetTop = this.$refs.zyScroll.getBoundingClientRect().top;
      this.zyFixed = offsetTop <= 0;
    },
    // 获取默认 exchangeId
    initExchangeId(exchangeId) {
      this.$api
        .get(this, api.getDefaultExchangeId, {
          id: exchangeId,
        })
        .then((res) => {
          if (res.success) {
            this.exchangeId = res.data.exchangeId;
            if (res.data.distributorId) {
              this.distributorId = res.data.distributorId;
            } else {
              this.distributorId = 2601;
            }
            localStorage.setItem("distributorId", this.distributorId);
            localStorage.setItem("exchangeId", this.exchangeId);

            if (!this.firstEnter) {
              sessionStorage.setItem("firstEnter", 1);
              // 机型适配
              this.getMobileDevice(this.exchangeId);
            } else {
              this.getModelAndMaterial(this.exchangeId);
            }

            // 获取轮播图
            this.getBannerList();
          } else {
            this.isLoading = false;
            this.message = "";
            this.$toast.fail(res.errMessage);
          }
        });
    },
    // 自动获取机型
    getMobileDevice(exchangeId) {
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

        // 通过 iphone-device.js 获取苹果机具体的机型
        let phone = getModel(rendererVal);
        if (phone.indexOf(",") > 0) {
          this.mobile = phone.substring(0, phone.indexOf(","));
          this.showModel = true; // 显示型号选择弹窗
        } else {
          this.mobile = phone;
        }
        // 根据 exchangeId 获取型号/材质列表
        this.getModelAndMaterial(exchangeId);
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
          this.getAndroidMobile(networkModel.trim()).then((res) => {
            if (res.success && res.data) {
              this.mobile = res.data.name;
            } else {
              this.mobile = "";
              this.showModel = true; // 显示型号选择弹窗
            }
            // 根据 exchangeId 获取型号/材质列表
            this.getModelAndMaterial(exchangeId);
          });
        }
      }
    },
    // 获取 Android 手机型号
    getAndroidMobile(networkModel) {
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
    // 根据 exchangeId 获取型号/材质列表
    getModelAndMaterial(exchangeId) {
      this.$api
        .get(this, api.getModelAndMaterial, {
          id: exchangeId,
        })
        .then((res) => {
          this.isLoading = false;
          this.message = "";
          if (res.success) {
            // 获取型号列表
            if (res.data.modelList && res.data.materialList.length > 0) {
              this.modelList = res.data.modelList;

              // 根据型号查询品牌并显示
              if (
                this.mobile !== "" &&
                this.mobile !== undefined &&
                this.mobile !== null
              ) {
                this.getModelId(this.mobile);
              }
            }

            // 获取材质列表
            if (res.data.materialList && res.data.materialList.length > 0) {
              this.materialList = res.data.materialList;
              if (res.data.materialList[0].childrenList) {
                this.materialName =
                  res.data.materialList[0].name +
                  "-" +
                  res.data.materialList[0].childrenList[0].name;
                this.materialId =
                  res.data.materialList[0].childrenList[0].materialId;
              } else {
                this.materialName = res.data.materialList[0].name;
                this.materialId = res.data.materialList[0].materialId;
              }

              sessionStorage.setItem("materialId", this.materialId);
            }

            // 获取推荐位
            this.getRecommendList(1);
            // 获取首页系列
            this.getSeriesList();
          } else {
            this.$toast.fail(res.msg);
          }
        });
    },
    // 根据手机型号查询型号及品牌
    getModelId(mname) {
      if (this.modelList.length > 0) {
        let flag = false;
        this.modelList.forEach((brand) => {
          if (brand.childrenList.length > 0) {
            brand.childrenList.forEach((model) => {
              if (
                model.name.replace(/\s*/g, "").toLowerCase() ===
                mname.replace(/\s*/g, "").toLowerCase()
              ) {
                this.modelId = model.modelId;
                this.modelName = model.name;
                this.brandId = brand.modelId;
                this.brandName = brand.name;
                this.mobile = model.name;
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
          this.mobile = this.modelList[0].childrenList[0].name;
        }

        // 缓存机型id、机型名称
        sessionStorage.setItem("modelName", this.modelName);
        sessionStorage.setItem("modelId", this.modelId);
        // 缓存机型品牌id、品牌名称
        sessionStorage.setItem("brandId", this.brandId);
        sessionStorage.setItem("brandName", this.brandName);
      }
    },

    // 显示（机型选择）
    handleShowModel() {
      this.showModel = true;
    },
    // 跳过（机型选择）
    handleCancel() {
      this.showModel = false;
    },
    // 确定（机型选择）
    handleConfirm(val) {
      this.modelName = val.name;
      this.modelId = val.modelId;
      this.mobile = val.name;
      // 缓存机型id、机型名称
      sessionStorage.setItem("modelName", this.modelName);
      sessionStorage.setItem("modelId", this.modelId);
      // 缓存机型品牌id、品牌名称
      let brandId = val.modelId;
      let brandName = val.name;
      sessionStorage.setItem("brandId", brandId);
      sessionStorage.setItem("brandName", brandName);
      this.showModel = false;
      // 获取推荐位
      this.getRecommendList(1);
      // 获取首页系列
      this.getSeriesList();
    },
    // 获取轮播图
    getBannerList() {
      this.$api
        .get(this, api.getBanner, {
          distributorId: this.distributorId,
        })
        .then((res) => {
          if (res.success) {
            this.bannerList = res.data;
          } else {
            this.isLoading = false;
            this.message = "";

            this.$toast.fail(res.errMessage);
          }
        });
    },
    // 获取推荐图库
    getRecommendList(recommendPage) {
      this.recommendPage = recommendPage;
      this.$api
        .get(this, api.getRecommend, {
          distributorId: this.distributorId,
          // exchangeId: this.exchangeId,
          modelId: this.modelId,
          materialId: this.materialId,
          page: recommendPage,
          count: this.recommendCount,
        })
        .then((res) => {
          if (res.success) {
            this.loadingRecommend = false;

            let rows = res.data.list;
            if (rows == null || rows.length === 0) {
              // 加载结束
              this.finishedRecommend = true;
              return;
            }

            // 将新数据与老数据进行合并
            this.recommendData = this.recommendData.concat(rows);

            // 如果列表数据条数>=总条数，不再触发滚动加载
            let recommendTotal = res.data.total;
            if (this.recommendData.length >= recommendTotal) {
              this.finishedRecommend = true;
            }
          } else {
            this.isLoading = false;
            this.message = "";

            this.$toast.fail(res.errMessage);
          }
        });
    },
    // 推荐加载更多
    onLoadRecommend() {
      this.recommendPage++;
      if (this.distributorId) {
        this.getRecommendList(this.recommendPage);
      }
    },
    // 获取首页系列
    getSeriesList() {
      this.$api
        .get(this, api.getSeries, {
          distributorId: this.distributorId,
          materialId: this.materialId,
          modelId: this.modelId,
          // exchangeId: this.exchangeId,
        })
        .then((res) => {
          if (res.success) {
            this.seriesList = res.data;
            this.seriesList.unshift({
              themeName: "推荐",
            });
          } else {
            this.isLoading = false;
            this.message = "";

            this.$toast.fail(res.errMessage);
          }
        });
    },
    // 点击首页系列
    handleClickSeries(name, title) {
      this.curTab = name;
      this.seriesId = name;
      if (name) {
        this.seriesPage = 1;
        this.seriesData = [];
        this.finishedSeries = false;
        this.getSeriesListData(name);
      } else {
        this.curTab = 0;
      }
    },
    // 获取首页系列数据
    getSeriesListData(themeId) {
      this.$api
        .get(this, api.getSeriesData, {
          distributorId: this.distributorId,
          themeId: themeId,
          page: this.seriesPage,
          size: this.seriesSize,
          // exchangeId: this.exchangeId,
          materialId: this.materialId,
          modelId: this.modelId,
        })
        .then((res) => {
          if (res.success) {
            this.loadingSeries = false;

            let rows = res.data.list;
            if (rows == null || rows.length === 0) {
              // 加载结束
              this.finishedSeries = true;
              return;
            }

            if (this.seriesPage === 1) {
              this.seriesData = [];
            }

            // 将新数据与老数据进行合并
            this.seriesData = this.seriesData.concat(rows);

            // 如果列表数据条数>=总条数，不再触发滚动加载
            let seriesTotal = res.data.total;
            if (this.seriesData.length >= seriesTotal) {
              this.finishedSeries = true;
            } else {
              this.seriesPage++;
            }
          } else {
            this.isLoading = false;
            this.message = "";

            this.$toast.fail(res.errMessage);
          }
        });
    },
    // 系列加载更多
    onLoadSeries() {
      if (this.seriesId) {
        this.getSeriesListData(this.seriesId);
      }
    },
    // 页面跳转
    goPath(name, query) {
      this.$router.push({
        name: name,
        query: query,
      });
    },
    // 卡包中心
    goCode() {
      let userId = localStorage.getItem("userId");
      let openId = localStorage.getItem("openId");
      if (userId && openId && openId !== "undefined") {
        this.$router.push({
          name: "code",
        });
      } else {
        var enterParams = JSON.stringify({});
        let platform = localStorage.getItem("platform");
        if (platform === "GF60006") {
          // 字节小程序
          tt.miniProgram.navigateTo({
            url:
              "/pages/login/login?enterFlag=codeList&enterParams=" +
              enterParams,
          });
        } else if (platform === "GF60007" || platform === "GF60008") {
          // 支付宝小程序
          my.navigateTo({
            url:
              "/pages/login/login?enterFlag=codeList&enterParams=" +
              enterParams,
          });
        } else {
          wx.miniProgram.navigateTo({
            url:
              "/pages/login/login?enterFlag=codeList&enterParams=" +
              enterParams,
          });
        }
      }
    },
    // 跳转详情
    goDetail(item) {
      this.$router.push({
        name: "detail",
        query: {
          materialId: this.materialId,
          modelId: this.modelId,
          picture_id: item.pictureId,
          picName: item.pictureName,
          picType: item.type,
          modelName: this.modelName,
          noCameraVacancyPreview: item.noCameraVacancyPreview,
          originImage: item.originImage,
        },
      });
    },
    // 跳转图库详情
    goDetailList(banner) {
      if (banner.externalLink) {
        window.location.href = banner.externalLink;
      } else {
        this.$router.push({
          name: "themeDetail",
          query: {
            modelId: this.modelId,
            modelName: this.modelName,
            materialId: this.materialId,
            type: banner.type,
            categoryId: banner.seriesId,
            url: banner.bannerUrl,
            comingFlag: "index",
          },
        });
      }
    },
    // 获取可用兑换卡数量
    getCardNum(userId) {
      this.$api
        .get(this, api.getCardNum, {
          distributorId: this.distributorId,
          userId: userId,
          status: 1, // 1、未使用 2、已使用 3、已过期
        })
        .then((res) => {
          if (res.success) {
            this.cardNum = res.data;
          } else {
            this.isLoading = false;
            this.message = "";
            this.$toast.fail(res.errMessage);
          }
        });
    },
  },
  components: {
    Notice,
    Model,
    Goods,
    Divider,
    Tabs,
    NoData,
    Loading,
  },
};
</script>

<style lang="stylus" scoped>
@import '~common/styles/variable.styl';
@import '~common/styles/mixin.styl';

.home-wrap {
  position: fixed;
  top: 0;
  bottom: 49px;
  width: 100%;
  background-color: $color-bg-white;

  .content {
    position: relative;
    width: 100%;
    height: 100%;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;

    &::-webkit-scrollbar {
      display: none;
    }
  }
}

.home-top {
  position: relative;
}

.cur-info {
  position: absolute;
  top: 0;
  left: 0;
  padding: $spacing-sm $spacing-base;
  color: $color-font-white;
  line-height: 14px;
  z-index: 1;

  .material {
    margin-top: 8px;
  }
}

.banner-list {
  width: 100%;
  height: 248px;
  text-align: center;
  overflow: hidden;

  img {
    width: 100%;
  }
}

.menu-list-wrap {
  height: 185px;
  background: linear-gradient(180deg, #FBFBFB 0%, #F5F5F5 100%);

  .menu-list {
    position: relative;
    top: -38px;
    margin: 0 $spacing-base;
    background-color: $color-bg-white;
    border-radius: $radius-base;
    overflow: hidden;

    .menu-title {
      font-size: $font-lg;
      font-weight: 600;
      line-height: 17px;
    }

    .menu-con {
      font-size: $font-sm;
      color: $color-font-grey;
      line-height: 12px;
    }

    .menu-t {
      display: flex;
      align(center);
      height: 161px;

      li {
        flex: 1;

        & + li {
          position: relative;

          &::before {
            content: '';
            position: absolute;
            top: 50%;
            left: 0;
            width: 1px;
            height: 115px;
            background-color: $color-border;
            transform: translateY(-50%);
          }
        }
      }

      .sprite-icon {
        margin-top: 13px;
      }

      .menu-title {
        margin-top: 1px;
      }

      .menu-con {
        margin-top: 8px;
      }
    }

    .menu-b {
      position: relative;
      padding: 0 12px;
      lineHeight(51px);
      background-color: rgbaMain(0.05);

      .sprite-icon {
        top: 50%;
        transform: translateY(-50%);
      }

      .menu-title {
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
      }

      .menu-con {
        position: absolute;
        top: 50%;
        right: 12px;
        transform: translateY(-50%);
      }
    }
  }
}

.notice-box {
  padding: $spacing-sm $spacing-base $spacing-lg;
  background-color: $color-bg;
}

.recommend-wrap {
  background-color: $color-bg-white;

  .recommend-title-wrap {
    background-color: $color-bg-white;

    &.fixed {
      position: fixed;
      top: 0;
      right: 0;
      bottom: 0;
      left: 0;
      overflow: hidden;
      z-index: 85;

      .recommend-con {
        position: absolute;
        width: 100%;
        height: 100%;
        overflow-y: scroll;

        &::-webkit-scrollbar {
          display: none;
        }
      }
    }
  }

  >>>.van-tabs__wrap {
    padding: 0 $spacing-base;
    height: 53px;
    background-color: $color-bg-white;

    .van-tabs__nav {
      padding: $spacing-base 0 $spacing-sm;
      height: 28px;
    }

    .van-tab {
      position: relative;
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

  .recommend-con {
    padding: 0 7px;
    background-color: $color-bg-white;
    display: none;

    &.active {
      display: block;
    }

    .recommend-list {
      li {
        float: left;
        width: 50%;
        padding: 7px;
      }
    }
  }
}

.no-data-wrap {
  position: relative;
  padding-bottom: 59px;

  >>>.no-data {
    position: relative;
    top: 100px;
    transform: tanslate(-50%, 0);
  }
}

.divider-wrap {
  padding: 35px 0 94px;
  background-color: $color-bg-white;

  .van-divider {
    margin: 0;
  }

  >>>.text {
    font-size: $font-sm;
    color: $color-font-grey;
    transform: scale(0.9);
  }
}

.model-wrap {
  mask();
  z-index: 101;

  .model {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 90%;
    height: 510px;
    background-color: $color-bg-white;
    border-radius: $radius-base;
    transform: translate(-50%, -50%);
    overflow: hidden;
  }
}

.van-icon-arrow {
  top: 1px;
}
</style>
