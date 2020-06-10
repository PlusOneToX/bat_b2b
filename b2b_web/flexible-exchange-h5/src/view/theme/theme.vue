<!--
 * @Author: yaowei
 * @Date: 2019-05-10 15:19:46
 * @LastEditors: yaowei
 * @LastEditTime: 2019-08-17 17:34:31
-->
<template>
  <div class="theme-wrap">
    <div class="container">
      <ul
        class="theme-title"
        :class="{ 'bg-white': themeList && themeList.length <= 0 }"
      >
        <li
          :class="{ active: curTab === index }"
          v-for="(theme, index) in themeTitle"
          :key="index"
          @click="handleClickTheme(index, theme)"
        >
          {{ theme.name }}
        </li>
      </ul>

      <template v-if="themeList && themeList.length > 0">
        <ul
          class="theme-list"
          :class="{ active: curTab === index }"
          v-for="(theme, index) in themeTitle"
          :key="index"
        >
          <li
            @click="goThemeDetail(theme.type, theme.categoryId)"
            v-for="(theme, index) in themeList"
            :key="index"
          >
            <van-cell
              :title="theme.themeName"
              is-link
              :value="'共' + theme.pageInfo.total + '款'"
              @click="showModel = true"
            />
            <div class="inner-con">
              <div class="bg-img">
                <img :src="theme.themeImg" :alt="theme.themeName" />
              </div>

              <div class="mask"></div>

              <div class="img-list">
                <div
                  class="img-wrap"
                  v-for="(image, index) in theme.pageInfo.list"
                  :key="index"
                >
                  <img
                    :src="image.noCameraVacancyPreview"
                    :alt="image.pictureName"
                  />
                </div>
              </div>
            </div>
          </li>
        </ul>
      </template>
      <div class="no-data-wrap" v-else>
        <NoData :showBtn="false"></NoData>
      </div>
    </div>
    <Tabs :curTab="'theme'"></Tabs>

    <Loading v-show="isLoading" :message="message"></Loading>
  </div>
</template>

<script>
// 组件
import NoData from "components/noData/noData";
import Tabs from "components/tabs/tabs";
import Loading from "components/loading/loading";
// api
import api from "api/allApi.js";

export default {
  name: "Theme",
  data() {
    return {
      curTab: 0, // 当前tab
      // 主题类型
      themeTitle: [
        {
          name: "全部",
          id: "",
        },
        {
          name: "IP素材",
          id: 2,
        },
        {
          name: "原创素材",
          id: 1,
        },
        // {
        //   name: "模板",
        //   id: 3,
        // },
      ],
      distributorId: "", // 分销商id
      page: 1, // 主题列表分页
      size: 3, // 主题列表条数
      exchangeId: "", // 兑换id
      modelId: "", // 型号id
      modelName: "", // 型号
      materialId: "", // 材质id
      curType: "", // 当前主题类型
      themeList: [], // 主题列表
      isLoading: true, // 加载
      message: "数据加载中", // 加载内容
    };
  },
  created() {
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
    // 获取官方主题列表
    this.getThemeListData();
  },
  methods: {
    // 获取官方主题列表
    getThemeListData() {
      this.isLoading = true;
      this.message = "数据加载中";

      this.$api
        .get(this, api.getThemeList, {
          distributorId: this.distributorId,
          page: this.page,
          size: this.size,
          // exchangeId: this.exchangeId,
          modelId: this.modelId,
          materialId: this.materialId,
          type: this.curType,
        })
        .then((res) => {
          if (res.success) {
            this.themeList = res.data ? res.data : [];
          }
          this.isLoading = false;
          this.message = "";
        });
    },
    // 点击主题名称
    handleClickTheme(index, theme) {
      this.curTab = index;
      this.curType = theme.id;
      // 获取官方主题列表
      this.getThemeListData();
    },
    // 跳转主题详情
    goThemeDetail(type, categoryId) {
      this.$router.push({
        name: "themeDetail",
        query: {
          modelId: this.modelId,
          modelName: this.modelName,
          materialId: this.materialId,
          type: type,
          categoryId: categoryId,
        },
      });
    },
  },
  components: {
    NoData,
    Tabs,
    Loading,
  },
};
</script>

<style lang="stylus" scoped>
@import '~common/styles/variable.styl';
@import '~common/styles/mixin.styl';

.theme-wrap {
  position: fixed;
  width: 100%;
  top: 0;
  bottom: 49px;
  background-color: $color-bg;

  .container {
    position: relative;
    width: 100%;
    height: 100%;
  }

  .theme-title {
    display: flex;
    justify-content: space-between;
    padding: 10px 20px;
    height: 80px;
    background: linear-gradient(180deg, #FFFFFF 0%, #F6F6F6 100%);

    &.bg-white {
      background: $color-bg-white;
    }

    li {
      line-height: 18px;
      font-size: $font-xm;
      color: $color-font-grey;

      &.active {
        position: relative;
        font-size: $font-lg;
        color: $color-font-base;
        font-weight: 500;

        &::after {
          content: '';
          position: absolute;
          bottom: 32px;
          left: 50%;
          width: 20px;
          height: 10px;
          bg-image('home-tab');
          background-size: 100% 100%;
          transform: translateX(-50%);
        }
      }

      i {
        position: relative;
        font-style: normal;
        z-index: 2;
      }
    }
  }

  .theme-list {
    position: absolute;
    width: 100%;
    top: 45px;
    bottom: 0;
    padding: 0 $spacing-base $spacing-lg;
    display: none;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;

    &::-webkit-scrollbar {
      display: none;
    }

    &.active {
      display: block;
    }

    li {
      position: relative;
      top: -10px;
      height: 308px;

      & + li {
        margin-top: 27px;
        border-top: 1px solid $color-border;
      }
    }

    .van-cell {
      padding: 24px $spacing-base 16px;
      background-color: transparent;

      &::after {
        border: 0;
      }

      >>>.van-cell__title {
        font-size: 18px;
        color: $color-font-base;
        font-weight: 500;
        flex: 3;
      }

      >>>.van-cell__value {
        font-size: $font-base;
        color: $color-font-darkGrey;
        flex: 1;
      }

      >>>.van-cell__right-icon {
        color: $color-font-darkGrey;
      }
    }

    .inner-con {
      position: relative;
      height: 250px;
      border-radius: $radius-base;
      overflow: hidden;
    }

    .bg-img {
      position: absolute;
      top: 0;
      left: 0;
      height: 84%;

      img {
        width: 100%;
        height: 100%;
      }
    }

    .mask {
      position: absolute;
      bottom: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: linear-gradient(180deg, rgba(255, 255, 255, 0) 0%, #FFFFFF 75%, #FFFFFF 100%);
    }

    .img-list {
      position: absolute;
      width: 100%;
      height: 50%;
      bottom: 18px;
      display: flex;
      justify-content: center;
      padding: 0 36px;
      overflow: hidden;

      .img-wrap {
        position: relative;
        width: calc((100% / 3));
        border-radius: $radius-xm;
        overflow: hidden;

        & + .img-wrap {
          margin-left: 10%;
        }

        img {
          position: absolute;
          top: 50%;
          left: 50%;
          height: 100%;
          transform: translate(-50%, -50%);
        }
      }
    }
  }

  .no-data-wrap {
    position: absolute;
    width: 100%;
    height: 100%;
    background-color: $color-bg-white;

    >>>.no-data {
      position: fixed;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
    }
  }
}
</style>