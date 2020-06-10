<!--
 * @Author: yaowei
 * @Date: 2019-05-10 15:22:08
 * @LastEditors: yaowei
 * @LastEditTime: 2019-08-03 10:33:12
-->
<template>
  <div class="theme-detail">
    <THeader
      class="header-wrap"
      :title="hasBg ? '主题详情' : ''"
      :darkIcon="false"
      :hasBg="hasBg"
      v-show="!isMiniProgram"
    ></THeader>
    <div class="detail-t" ref="contentWrap">
      <img
        class="bg-img"
        :src="categoryData.themeImg ? categoryData.themeImg : bannerUrl"
        alt="背景图"
      />
      <div class="detail-title">
        <van-cell
          :title="categoryData.themeName ? categoryData.themeName : ''"
          :value="'共' + total + '款'"
        />
      </div>
    </div>
    <van-list
      v-model="loadingData"
      :finished="finishedData"
      :immediate-check="false"
      @load="onLoadData"
      :offset="8"
    >
      <ul class="detail-list clearfix">
        <li
          v-for="(item, index) in categoryList"
          :key="index"
          @click="goDetail(item)"
        >
          <Goods :goodsInfo="item"></Goods>
        </li>
      </ul>
    </van-list>

    <!-- 底线 -->
    <Divider class="divider-wrap" :text="'你看到我的底线啦'"></Divider>

    <Loading v-show="isLoading" :message="message"></Loading>
  </div>
</template>

<script>
// 组件
import THeader from "components/tHeader/tHeader";
import Goods from "components/goods/goods";
import Divider from "components/divider/divider";
import Loading from "components/loading/loading";
// api
import api from "api/allApi.js";

export default {
  name: "ThemeDetail",
  data() {
    return {
      distributorId: "", // 分销商id
      page: 1, // 主题列表分页
      size: 6, // 主题列表条数
      total: 0, // 主题总数
      exchangeId: "", // 兑换id
      modelId: "", // 型号id
      modelName: "", // 型号
      materialId: "", // 材质id
      curType: "", // 当前主题类型
      categoryId: "", // 当前主题id
      categoryData: [], // 主题数据
      categoryList: [], // 主题列表
      loadingData: false, // 加载
      finishedData: false, // 加载完成
      bannerUrl: "", // 背景图
      hasBg: false, // header是否有背景色
      isLoading: true, // 加载
      message: "数据加载中", // 加载内容
      isMiniProgram: false, // 是否是小程序
    };
  },
  created() {
    var enterFlag = this.getQueryVariable("enterFlag");

    if (enterFlag === "themeDetail") {
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
        sessionStorage.setItem("modelName", decodeURIComponent(enterParams.modelName));
        sessionStorage.setItem("materialId", enterParams.materialId);
        sessionStorage.setItem("type", enterParams.type);
        sessionStorage.setItem("categoryId", enterParams.categoryId);
        if (enterParams.comingFlag) {
          sessionStorage.setItem("comingFlag", enterParams.comingFlag);
          sessionStorage.setItem("url", enterParams.url);
        }

        this.isMiniProgram = true;
        sessionStorage.setItem("isMiniProgram", this.isMiniProgram);
      }
    }

    this.isMiniProgram = sessionStorage.getItem("isMiniProgram") || false;

    this.distributorId = localStorage.getItem("distributorId");
    this.exchangeId = localStorage.getItem("exchangeId");

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
    this.curType = this.$route.query.type
      ? this.$route.query.type
      : sessionStorage.getItem("type")
      ? sessionStorage.getItem("type")
      : "";
    this.categoryId = this.$route.query.categoryId
      ? this.$route.query.categoryId
      : sessionStorage.getItem("categoryId")
      ? sessionStorage.getItem("categoryId")
      : "";

    // 获取图库详情
    let comingFlag = this.$route.query.comingFlag
      ? this.$route.query.comingFlag
      : sessionStorage.getItem("comingFlag")
      ? sessionStorage.getItem("comingFlag")
      : "";
    if (comingFlag) {
      this.bannerUrl = this.$route.query.url
        ? this.$route.query.url
        : sessionStorage.getItem("url")
        ? sessionStorage.getItem("url")
        : "";
      this.getCateDetail();
    } else {
      this.getThemeDetail();
    }
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
    // 获取系列详情
    getCateDetail() {
      this.$api
        .get(this, api.getSeriesData, {
          distributorId: this.distributorId,
          themeId: this.categoryId,
          page: this.page,
          size: this.size,
          // exchangeId: this.exchangeId,
          materialId: this.materialId,
          modelId: this.modelId,
        })
        .then((res) => {
          if (res.success) {
            this.loadingData = false;

            let rows = res.data.list;
            if (rows == null || rows.length === 0) {
              // 加载结束
              this.finishedData = true;
              return;
            }

            // 将新数据与老数据进行合并
            this.categoryList = this.categoryList.concat(rows);

            // 如果列表数据条数>=总条数，不再触发滚动加载
            this.total = res.data.total;
            if (this.categoryList.length >= this.total) {
              this.finishedData = true;
            }
          }
          this.isLoading = false;
          this.message = "";
        });
    },
    // 获取主题详情
    getThemeDetail() {
      this.$api
        .get(this, api.getTheme, {
          distributorId: this.distributorId,
          page: this.page,
          size: this.size,
          // exchangeId: this.exchangeId,
          modelId: this.modelId,
          materialId: this.materialId,
          type: this.curType,
          categoryId: this.categoryId,
        })
        .then((res) => {
          if (res.success) {
            this.categoryData = res.data;
            this.loadingData = false;

            let rows = res.data.pageInfo.list;
            if (rows == null || rows.length === 0) {
              // 加载结束
              this.finishedData = true;
              return;
            }

            // 将新数据与老数据进行合并
            this.categoryList = this.categoryList.concat(rows);

            // 如果列表数据条数>=总条数，不再触发滚动加载
            this.total = res.data.pageInfo.total;
            if (this.categoryList.length >= this.total) {
              this.finishedData = true;
            }
          }
          this.isLoading = false;
          this.message = "";
        })
        .catch((error) => {
          this.isLoading = false;
          this.message = "";
          this.$toast.fail(error);
        });
    },
    // 推荐加载更多
    onLoadData() {
      this.page++;

      // 获取图库详情
      let comingFlag = this.$route.query.comingFlag
        ? this.$route.query.comingFlag
        : sessionStorage.getItem("comingFlag")
        ? sessionStorage.getItem("comingFlag")
        : "";
      if (comingFlag) {
        this.getCateDetail();
      } else {
        this.getThemeDetail();
      }
    },
  },
  components: {
    THeader,
    Goods,
    Divider,
    Loading,
  },
};
</script>

<style lang="stylus" scoped>
@import '~common/styles/variable.styl';
@import '~common/styles/mixin.styl';

.theme-detail {
  .detail-t {
    position: relative;
    height: 210px;
    overflow: hidden;

    .bg-img {
      width: 100%;
      min-height: 100%;
    }

    .detail-title {
      position: absolute;
      bottom: 0;
      left: 0;
      width: 100%;
      background-color: rgbaBlack($opacity-light);

      .van-cell {
        padding: 0 $spacing-base;
        lineHeight(35px);
        background-color: transparent;
      }

      >>>.van-cell__title {
        font-size: $font-lg;
      }

      >>>.van-cell__title, >>>.van-cell__value, >>>.van-cell__right-icon {
        color: $color-font-white;
      }
    }
  }

  .detail-list {
    padding: 21px 7px;
    background-color: $color-bg-white;

    li {
      float: left;
      width: 50%;
      padding: 7px;
    }
  }

  .divider-wrap {
    padding-top: 25px;
    padding-bottom: 50px;
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
}
</style>