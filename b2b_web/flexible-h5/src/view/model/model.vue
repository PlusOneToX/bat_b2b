<!--
 * @Author: yaowei
 * @Date: 2019-11-20 19:09:27
 * @LastEditors: yaowei
 * @LastEditTime: 2019-10-16 09:37:56
-->
<template>
  <div class="choose-model">
    <c-header :title="title" :hasBg="true"></c-header>
    <p class="cur-model">
      <span class="sprite-icon model_icon_phone"></span>
      <span class="title">机型：</span>
      <span @click="goPath(curItem)">{{ mobile }}</span>
    </p>
    <p class="used-model" v-if="usedModel">
      最近使用：<span @click="goPath(usedItem)">{{ usedModel }}</span>
    </p>

    <div class="model-wrap">
      <div class="model-l">
        <p
          v-for="(item, index) in modelList"
          :key="index"
          class="model-l-item"
          :class="{ active: curIndex === index }"
          @click="changeModel(index)"
        >
          <span>{{ item.name }}</span>
        </p>
      </div>
      <div class="model-r">
        <div
          v-for="(model, index) in modelList"
          :key="index"
          class="model-list-wrap"
          :class="{ active: curIndex === index }"
        >
          <p class="title">{{ model.name }} - 型号</p>
          <span
            v-for="(item, index) in model.childrenList"
            :key="index"
            class="model-r-item"
            @click="goPath(item)"
            >{{ item.name }}</span
          >
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import cHeader from "../components/cHeader.vue";

import MobileDetect from "common/js/lib/mobile-detect.min.js";
import { getRenderer } from "common/js/lib/renderer.min.js";
import { getModel } from "common/js/iphone-device.js";
import api from "common/js/allApi.js";
export default {
  components: { cHeader },
  name: "model",
  data() {
    return {
      title: "选择你的型号",
      mobile: "", // 当前机型
      usedModel: "", // 最近使用机型
      curIndex: 0, // 当前型号列表
      modelList: [], // 型号列表
      distributorId: "",
      platform: "",
      curTop: 0,
      pictureId: "", // 图片ID（推荐页/分类页 去定制所选图片）
      curItem: {}, // 当前机型
      usedItem: {}, // 最近使用
    };
  },
  mounted() {
    // 获取进入flag，如果不是点击去定制进入，移除缓存图片信息
    let comingFlag = this.$route.query.comingFlag;
    if (comingFlag === undefined || this.comingFlag === null || this.comingFlag === "" || !comingFlag) {
      sessionStorage.removeItem("imageInfo");
    }

    // 获取distributorId/platform
    this.distributorId = localStorage.getItem("distributorId");
    this.platform = localStorage.getItem("platform");

    // 获取最近使用机型
    if (localStorage.getItem("usedItemInfo")) {
      localStorage.removeItem("usedItem");
      this.usedItem = JSON.parse(localStorage.getItem("usedItemInfo"));
      this.usedModel = this.usedItem.name;
    }

    // 获取图片id
    if (sessionStorage.getItem("imageInfo")) {
      let imageInfo = JSON.parse(sessionStorage.getItem("imageInfo"));
      this.pictureId = imageInfo.pictureId;
    }

    this.getMobileDevice();
    this.getModelList();
  },
  methods: {
    // 获取型号列表
    getModelList() {
      this.$api
        .get(this, api.getModelList, {
          categoryId: 1, // 产品类型id：1 手机壳
          distributorId: this.distributorId,
          pictureId: this.pictureId,
          platform: this.platform, // 平台
        })
        .then((res) => {
          if (res.success) {
            this.modelList = res.data;
            if (this.mobile === "") {
              this.mobile = res.data[0].childrenList[0].name;
              this.curItem = res.data[0].childrenList[0];
            } else {
              res.data.forEach((item) => {
                item.childrenList.forEach((subItem) => {
                  if (subItem.name === this.mobile) {
                    this.curItem = subItem;
                  }
                });
              });
            }
          } else {
            this.$toast.fail(res.errMessage);
          }
        })
        .catch((error) => {
          this.$toast.fail(error);
        });
    },
    // 页面跳转
    goPath(item) {
      // 存储已选机型，用于记录最近使用
      localStorage.setItem("usedItemInfo", JSON.stringify(item));

      // 存储当前选择机型id
      sessionStorage.setItem("modelInfo", JSON.stringify(item));

      // this.$router.push({
      //   path: "/material",
      // });

      this.$router.push({
        path: "/custom",
        query: {
          comingFlag: "mmPage",
        },
      });
    },
    // 自动获取机型
    getMobileDevice() {
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
          this.$toast("机型适配可能存在偏差，请确认或手动选择！");
          this.mobile = phone.substring(0, phone.indexOf(","));
        } else {
          this.mobile = phone;
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
          this.getAndroidMobile(networkModel.trim()).then((res) => {
            if (res.success && res.data) {
              this.mobile = res.data.name;
            } else {
              this.$toast.fail("未匹配到当前手机型号，已展示默认机型");
            }
          });
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
    // 型号列表切换
    changeModel(index) {
      this.curIndex = index;
    },
  },
};
</script>

<style lang="stylus" scoped>
$border = #F5F5F5;
$white = #fff;
$dark = #000000;
$gray = #5A5A5A;
$blue = #0076A5;
$model-bg = #F7F7F7;
$model-bd = #F1F1F1;

.choose-model {
  position: fixed;
  width: 100%;
  height: 100%;
  padding-top: 44px;

  .cur-model, .used-model {
    padding-left: 15px;
    width: 100%;
    height: 43px;
    font-size: 13px;
    line-height: 43px;
    background-color: $white;
    border-bottom: 1px solid $border;
    box-sizing: border-box;
  }

  .cur-model {
    color: $gray;

    .sprite-icon {
      top: 6px;
    }

    .title {
      color: $dark;
    }
  }

  .used-model {
    color: $blue;

    & + .model-wrap {
      top: 130px;
    }
  }

  .model-wrap {
    position: fixed;
    top: 87px;
    left: 0;
    width: 100%;
    bottom: 0;

    .model-l {
      position: absolute;
      top: 0;
      bottom: 0;
      left: 0;
      width: 91px;
      background-color: $model-bg;
      overflow: hidden;
      overflow-y: scroll;
      -webkit-overflow-scrolling: touch;
      scrollbar-width: none;
      -ms-overflow-style: none;
      z-index: 1;

      &::-webkit-scrollbar {
        display: none;
      }

      .model-l-item {
        height: 44px;
        font-size: 14px;
        color: $dark;
        text-align: center;
        line-height: 44px;

        &.active {
          span {
            display: inline-block;
            width: 76px;
            height: 28px;
            line-height: 28px;
            border-radius: 28px;
            background-color: $blue;
          }
        }
      }
    }

    .model-r {
      padding-left: 91px;
      height: 100%;
      background-color: $white;
      overflow: hidden;
      overflow-y: scroll;
      -webkit-overflow-scrolling: touch;
      scrollbar-width: none;
      -ms-overflow-style: none;

      &::-webkit-scrollbar {
        display: none;
      }

      .model-list-wrap {
        display: none;

        &.active {
          display: block;
        }
      }

      .title {
        padding: 26px 13px 15px;
        font-size: 15px;
        color: $dark;
      }

      .model-r-item {
        display: inline-block;
        margin: 0 0 13px 13px;
        padding: 10px 8px;
        font-size: 13px;
        color: $dark;
        background-color: $model-bg;
        border: 2px solid $model-bd;
        border-radius: 3px;
      }
    }
  }
}
</style>