<template>
  <div class="category">
    <div class="category-wrap">
      <c-header></c-header>
      <img class="category-banner" :src="categoryImage" alt="" />
      <div></div>

      <van-list
        class="category-item-wrap"
        v-model="loading"
        :finished="finished"
        :immediate-check="false"
        @load="onLoad"
        :offset="10"
      >
        <theme-item
          class="category-item"
          v-for="(category, index) in categoryData"
          :key="index"
          :itemData="category"
        >
        </theme-item>
      </van-list>
    </div>

    <bottom-tips></bottom-tips>
  </div>
</template>

<script>
import BottomTips from "../components/bottomTips.vue";
import CHeader from "../components/cHeader.vue";
import themeItem from "../components/themeItem.vue";

import api from "common/js/allApi.js";
export default {
  components: {
    themeItem,
    BottomTips,
    CHeader,
  },
  name: "recommend",
  data() {
    return {
      seriesId: this.$route.query.seriesId, // 系列展示id
      bannerId: this.$route.query.bannerId,
      categoryImage: this.$route.query.categoryImage, // 系列展示banner
      categoryData: [], // 系列展示列表
      loading: false,
      finished: false,
      page: 1,
      count: 4,
      total: 0,
    };
  },
  mounted() {
    this._initData();
  },
  methods: {
    // 返回上一页
    handleBack() {
      this.$router.go(-1);
    },
    _initData() {
      if (this.bannerId) {
        // banner进入
        this.getBannerList();
      } else {
        // 查看更多进入
        this.getMore();
      }
    },
    // 根据系列id分页查询
    getMore() {
      this.$api
        .get(this, api.getCategoryListById, {
          seriesId: this.seriesId, // 系列展示id
          page: this.page,
          count: this.count,
        })
        .then((res) => {
          if (res.success) {
            this.loading = false;

            let rows = res.data.list;
            if (rows == null || rows.length === 0) {
              // 加载结束
              this.finished = true;
              return;
            }

            // 将新数据与老数据进行合并
            this.categoryData = this.categoryData.concat(rows);
            // 如果列表数据条数>=总条数，不再触发滚动加载
            this.total = res.data.total;
            if (this.categoryData.length >= this.total) {
              this.finished = true;
            }
          } else {
            this.$toast.fail(res.errMessage);
          }
        })
        .catch((error) => {
          //this.$toast.fail(error);
		  console.log("未登录，抛出异常");
        });
    },
    // 根据bannerId分页查询
    getBannerList() {
      this.$api
        .get(this, api.getBannerListById, {
          seriesId: this.seriesId, // 系列展示id
          page: this.page,
          count: this.count,
          bannerId: this.bannerId,
        })
        .then((res) => {
          if (res.success) {
            this.loading = false;

            let rows = res.data.list;
            if (rows == null || rows.length === 0) {
              // 加载结束
              this.finished = true;
              return;
            }

            // 将新数据与老数据进行合并
            this.categoryData = this.categoryData.concat(rows);
            // 如果列表数据条数>=总条数，不再触发滚动加载
            this.total = res.data.total;
            if (this.categoryData.length >= this.total) {
              this.finished = true;
            }
          } else {
            this.$toast.fail(res.errMessage);
          }
        })
        .catch((error) => {
          //this.$toast.fail(error);
		  console.log("未登录，抛出异常");
        });
    },
    onLoad() {
      this.page++;
      if (this.bannerId) {
        this.getCategoryList();
      } else {
        this.getMore();
      }
    },
  },
};
</script>

<style lang="stylus" scoped>
$white = #fff;
$light-gray = #B9B9B9;

.category-wrap {
  position: relative;

  .van-icon-arrow-left {
    position: absolute;
    top: 15px;
    left: 10px;
    font-size: 20px;
    color: $white;
  }

  .category-banner {
    width: 100%;
  }

  .category-item-wrap {
    padding: 0 15px;

    .category-item {
      margin-top: 20px;
      width: 47.5%;
      border-radius: 5px;

      &:nth-child(2n) {
        margin-left: 5%;
      }
    }
  }

  .more-category {
    margin-top: 20px;
    font-size: 14px;
    color: $light-gray;
    text-align: center;

    .van-icon {
      top: 2px;
    }
  }
}
</style>
