<!--
 * @Author: yaowei
 * @Date: 2018-05-25 09:08:36
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-23 22:19:52
-->
<template>
  <div class="group-buying">
    <!--公共头部-->
    <Header :userState="userState"></Header>
    <div class="content-wrap" v-if="groupData.length > 0">
      <div class="banner">
        <div class="inner" :class="{'en': $i18n.locale === 'en'}"></div>
      </div>
      <div class="content">
        <groupBuyItem :groupData="groupData" :cateFlag="'groupList'"></groupBuyItem>

        <!-- 分页 -->
        <div class="rl-tr rl-margin-top-default" v-if="groupData.length > 0 && totalCount > 20">
          <el-pagination
            background
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
            layout="prev, pager, next, jumper"
            :page-size="pageSize"
            :total="totalCount"
          ></el-pagination>
        </div>
      </div>
    </div>

    <!-- 暂无数据 -->
    <div class="no-goods" v-show="isGroupList">
      <div class="empty-car-img">
        <img width="100%" src="../assets/images/goods-empty.png" alt="BAT商城 暂无商品" />
      </div>
      <p class="tips">{{ ($i18n.locale === 'zh') ? "暂无商品哦！" : $t("P.NoCommodities") }}</p>
      <span class="go-around" @click="walkingAround">{{$t('ShopCarts.Around')}}</span>
    </div>
    <!--公共底部-->
    <Footer :cateFlag="'group'"></Footer>

    <!--加载动画-->
    <loading v-if="this.showLoading === true"></loading>
  </div>
</template>

<script>
import Footer from '@/components/Footer.vue';
import Header from '@/components/Header.vue';
import loading from '@/components/loading.vue';
import groupBuyItem from '@/components/groupBuy/groupBuyItem.vue';

export default {
  name: 'GroupBuying',
  components: { Footer, Header, loading, groupBuyItem },
  data () {
    return {
      userState: 2,
      cur_page: 1,
      pageSize: 20,
      groupData: [], // 团购列表
      isGroupList: false, // 是否有团购商品
      interval: null, // 倒计时
      showLoading: false
    };
  },
  created () {
    this.initListData(this.cur_page, this.pageSize);
  },
  methods: {
    // 初始化活动列表
    initListData (page, count, activityTyp) {
      this.showLoading = true;
      let params = {
        page: page,
        count: count,
        activityType: activityTyp || '',
        isIndex: 0 // 非首页
      };
      this.$api
        .get(this, 'user/u/marketing/spellGroup/page', params)
        .then((res) => {
          if (Number(res.code) === 0) {
            this.showLoading = false;
            if (res.pageInfo && res.pageInfo.list.length !== 0) {
              this.isGroupList = false;
              this.groupData = res.pageInfo.list;
              this.totalCount = res.pageInfo.total;
            } else {
              this.isGroupList = true;
            }
            this.intervalTime();
          } else if (res.code === 3) {
            this.showLoading = false;
            
          }
        });
    },
    // 当前页码
    handleCurrentChange (val) {
      this.cur_page = val;
      this.initListData(val, this.pageSize);
    },
    // 每页条数
    handleSizeChange (val) {
      this.pagesize = val;
      this.initListData(this.cur_page, val);
    },
    // 定时器
    intervalTime () {
      if (this.interval !== null) {
        clearInterval(this.interval);
      }
      if (this.groupData && this.groupData.length > 0) {
        this.interval = setInterval(() => {
          // 获取当前时间，同时得到订单付款截止时间数组
          let newTime = new Date().getTime();
          // 计算相差天数
          let isInterval = false;
          this.groupData.forEach((item) => {
            isInterval = true;
            let diff = '';
            if (item.status === 0) {
              diff = item.startTime - newTime;
            }
            if (item.status === 1) {
              diff = item.endTime - newTime;
            }

            if (diff <= 0) {
              diff = 0;
            }
            this.$set(item, 'diff', diff);
          });
          if (!isInterval) {
            clearInterval(this.interval);
          }
        }, 1000);
      }
    },
    // 到处逛逛
    walkingAround () {
      this.$router.push({ name: 'Index' });
    }
  }
};
</script>

<style lang="less" scoped>
@import url("../assets/less/variable.less");
.content-wrap {
  position: relative;
  min-width: 1260px;
  min-height: 465px;
  padding-bottom: 80px;
  margin: 0 auto;
  background-color: @groupBg;
  .banner {
    height: 465px;
    .inner {
      height: 600px;
      background: url("../assets/images/groupBuying/group-banner.png") no-repeat
        top center;
      &.en {
        background: url("../assets/images/groupBuying/group-banner-en.png")
          no-repeat top center;
      }
    }
  }
  .content {
    position: relative;
    top: 0;
    width: 1200px;
    margin: 0 auto;
  }
}
.no-goods {
  padding: 100px 0;
  text-align: center;
  background-color: @grayBg;
  min-height: 500px;
  .empty-car-img {
    margin: 0 auto;
    width: 300px;
  }
  .tips {
    margin-top: 30px;
    font-size: 18px;
    color: @lighterGray;
  }
  .go-around {
    display: inline-block;
    margin-top: 20px;
    width: 130px;
    height: 40px;
    line-height: 40px;
    font-size: 18px;
    color: @lighterGray;
    border: 1px solid @lighterGray;
    border-radius: 5px;
    cursor: pointer;
    &:hover {
      color: @blue;
      border: 1px solid @blue;
    }
  }
}
</style>
