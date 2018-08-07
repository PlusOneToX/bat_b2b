
<template>
  <div class="group-buy-item rl-clear" :class="{ 'item-wrap': cateFlag === 'groupList' }">
    <!-- 'no-margin':( (index === 2 || index === 6)) ||( index > 0 && (index + 1) % 4 === 0), -->
    <div class="group-buy-pro cursor-pointer" v-for="(item, index) in groupData" :key="index"
      :class="{'recommend-wrap': item.isRecommended && cateFlag !== 'groupList',
        'no-margin':( index > 0 && (index + 1) % 4 === 0),'white-bg': cateFlag === 'groupList',}" @click="goDoodsDetail(item.id, item.goodsType)">
      <img v-if="$i18n.locale === 'zh'" class="img" :src="item.imageUrl1" :alt="item.goodsName"/>
      <img v-else class="img" :src="item.imageUrl1en" :alt="item.goodsNameEn"/>
      <div class="con-wrap">
        <h6 class="pro-name"
          :class="{'pro-name1':(cateFlag === 'groupList'  &&item.discount < 10) ||( item.discount < 10 && !item.isRecommended),}">
          {{ $i18n.locale === "zh" ? item.goodsName : item.goodsNameEn }}
        </h6>
        <template v-if="item.isRecommended && cateFlag !== 'groupList'">
          <div class="price-wrap rl-clear">
            <p class="price rl-fl">
              <span class="unit">{{ $i18n.locale === "zh" ? "¥" : "$" }}</span>
              <span>{{
                $i18n.locale === "zh"
                  ? fomatFloat(item.groupPrice, 2)
                  : fomatFloat(item.groupPriceEn, 2)
              }}</span>
            </p>
            <p class="labels rl-fl" v-if="item.discount && item.discount < 10">
              <span v-if="$i18n.locale === 'zh'"
                >低至{{ fomatFloor(item.discount, 1) }}折</span
              >
              <span v-else
                >Up to {{ 100 - fomatFloor(item.discount, 1) * 10 }}% off</span
              >
            </p>
            <p class="count-down rl-fl" v-if="$i18n.locale === 'zh'">
              <template v-if="item.status === 0">
                <span>距拼团开始:</span>
                <span class="lg">{{
                  Math.floor(item.diff / (24 * 3600 * 1000)) > 0
                    ? Math.floor(item.diff / (24 * 3600 * 1000))
                    : 0
                }}</span>
                <span>天</span>
                <span class="lg">{{
                  fixNum(Math.floor((item.diff / (1000 * 60 * 60)) % 24))
                }}</span>
                <span>时</span>
                <span class="lg">{{
                  fixNum(Math.floor((item.diff / (1000 * 60)) % 60))
                }}</span>
                <span>分</span>
              </template>
              <template v-if="item.status === 1">
                <span>距拼团结束:</span>
                <span class="lg">{{
                  Math.floor(item.diff / (24 * 3600 * 1000)) > 0
                    ? Math.floor(item.diff / (24 * 3600 * 1000))
                    : 0
                }}</span>
                <span>天</span>
                <span class="lg">{{
                  fixNum(Math.floor((item.diff / (1000 * 60 * 60)) % 24))
                }}</span>
                <span>时</span>
                <span class="lg">{{
                  fixNum(Math.floor((item.diff / (1000 * 60)) % 60))
                }}</span>
                <span>分</span>
              </template>
            </p>
            <p class="count-down rl-fl" v-else>
              <template v-if="item.status === 0">
                <span>Start after</span>
                <span class="lg">{{
                  "" +
                  (Math.floor(item.diff / (24 * 3600 * 1000)) > 0
                    ? Math.floor(item.diff / (24 * 3600 * 1000))
                    : 0)
                }}</span>
                <span>{{
                  Math.floor(item.diff / (24 * 3600 * 1000)) === 1
                    ? " day "
                    : " days "
                }}</span>
                <span class="lg">{{
                  fixNum(Math.floor((item.diff / (1000 * 60 * 60)) % 24))
                }}</span>
                <span>:</span>
                <span class="lg">{{
                  fixNum(Math.floor((item.diff / (1000 * 60)) % 60))
                }}</span>
              </template>
              <template v-if="item.status === 1">
                <span class="lg">{{
                  Math.floor(item.diff / (24 * 3600 * 1000)) > 0
                    ? Math.floor(item.diff / (24 * 3600 * 1000))
                    : 0
                }}</span>
                <span>{{
                  Math.floor(item.diff / (24 * 3600 * 1000)) === 1
                    ? " day "
                    : " days "
                }}</span>
                <span class="lg">{{
                  fixNum(Math.floor((item.diff / (1000 * 60 * 60)) % 24))
                }}</span>
                <span>:</span>
                <span class="lg">{{
                  fixNum(Math.floor((item.diff / (1000 * 60)) % 60))
                }}</span>
                <span>Left</span>
              </template>
            </p>
          </div>
          <div class="process rl-clear">
            <el-progress
              class="rl-fl"
              :stroke-width="12"
              :percentage="parseInt(0)"
              :show-text="false"
              v-if="item.status === 0"
            ></el-progress>
            <el-progress
              class="rl-fl"
              :stroke-width="12"
              :percentage="(parseInt(item.realSum/item.maxNum*100)>1)?parseInt(item.realSum/item.maxNum*100):1"
              :show-text="false"
              v-if="item.realSum&&item.status === 1"
            ></el-progress>
            <span class="btn rl-fr green" v-if="item.status === 0">{{
              $i18n.locale === "zh" ? "去购买" : $t("GroupBuy.BuyNow")
            }}</span>
            <span class="btn rl-fr" v-if="item.status === 1">{{
              $t("GroupBuy.BuyNow")
            }}</span>

            <template v-if="$i18n.locale === 'zh' && item.status === 1">
              <span
                v-if="item.realSum > 10000"
                class="count rl-padding-right-default rl-fr"
                >已拼 {{ fomatFloor(item.realSum / 10000, 2) }} 万+</span
              >
              <span v-else class="count rl-padding-right-default rl-fr"
                >已拼 {{ item.realSum }} 件</span
              >
            </template>
            <template v-if="$i18n.locale === 'en' && item.status === 1">
              <span
                v-if="item.realSum > 1000"
                class="count rl-padding-right-default rl-fr"
                >{{ fomatFloor(item.realSum / 1000, 1) }}K+ Sold</span
              >
              <span v-else class="count rl-padding-right-default rl-fr"
                >{{ item.realSum }} Sold</span
              >
            </template>
          </div>
        </template>
        <template v-else>
          <div class="count-down-wrap">
            <p class="count-down" :class="{ green: item.status === 0 }" v-if="$i18n.locale === 'zh'">
              <template v-if="item.status === 0">
                <span>距拼团开始:</span>
                <span class="lg green">{{Math.floor(item.diff / (24 * 3600 * 1000)) > 0? Math.floor(item.diff / (24 * 3600 * 1000)): 0}}</span>
                <span>天</span>
                <span class="lg green">{{fixNum(Math.floor((item.diff / (1000 * 60 * 60)) % 24))}}</span>
                <span>时</span>
                <span class="lg green">{{fixNum(Math.floor((item.diff / (1000 * 60)) % 60))}}</span>
                <span>分</span>
              </template>
              <template v-if="item.status === 1">
                <span>距拼团结束:</span>
                <span class="lg">{{Math.floor(item.diff / (24 * 3600 * 1000)) > 0? Math.floor(item.diff / (24 * 3600 * 1000)): 0}}</span>
                <span>天</span>
                <span class="lg">{{fixNum(Math.floor((item.diff / (1000 * 60 * 60)) % 24))}}</span>
                <span>时</span>
                <span class="lg">{{fixNum(Math.floor((item.diff / (1000 * 60)) % 60))}}</span>
                <span>分</span>
              </template>
            </p>
            <p class="count-down" :class="{ green: item.status === 0 }" v-else>
              <template v-if="item.status === 0">
                <span>Start after</span>
                <span class="lg green">{{" " +(Math.floor(item.diff / (24 * 3600 * 1000)) > 0? Math.floor(item.diff / (24 * 3600 * 1000)): 0)}}</span>
                <span>{{Math.floor(item.diff / (24 * 3600 * 1000)) === 1? " day ": " days "}}</span>
                <span class="lg green">{{fixNum(Math.floor((item.diff / (1000 * 60 * 60)) % 24))}}</span>
                <span>:</span>
                <span class="lg green">{{fixNum(Math.floor((item.diff / (1000 * 60)) % 60))}}</span>
              </template>
              <template v-if="item.status === 1">
                <span class="lg">{{Math.floor(item.diff / (24 * 3600 * 1000)) > 0? Math.floor(item.diff / (24 * 3600 * 1000)): 0}}</span>
                <span>{{Math.floor(item.diff / (24 * 3600 * 1000)) === 1? " day ": " days "}}</span>
                <span class="lg">{{fixNum(Math.floor((item.diff / (1000 * 60 * 60)) % 24))}}</span>
                <span>:</span>
                <span class="lg">{{fixNum(Math.floor((item.diff / (1000 * 60)) % 60))}}</span>
                <span>Left</span>
              </template>
            </p>
          </div>
          <!-- 折扣 -->
          <p class="labels" v-if="item.discount && item.discount < 10">
            <span v-if="$i18n.locale === 'zh'">低至{{ fomatFloor(item.discount, 1) }}折</span>
            <span v-else>Up to {{ 100 - fomatFloor(item.discount, 1) * 10 }}% off</span>
          </p>
          <!-- 已拼 -->
          <div class="process rl-clear" :class="{ top6: item.discount && item.discount >= 10 }">
            <el-progress class="rl-fl" :stroke-width="8" :percentage="parseInt(0)" :show-text="false"  v-if="item.status === 0"></el-progress>
            <el-progress class="rl-fl" :stroke-width="8" :percentage="(parseInt(item.realSum/item.maxNum*100)>1)?parseInt(item.realSum/item.maxNum*100):1" :show-text="false" v-if="item.realSum&&item.status === 1"></el-progress>
            <template v-if="$i18n.locale === 'zh' && item.status === 1">
              <span v-if="item.realSum > 10000" class="count rl-fr">已拼 {{ fomatFloor(item.realSum / 10000, 2) }} 万+</span>
              <span v-else class="count rl-fr">已拼 {{ item.realSum }} 件</span>
            </template>
            <template v-if="$i18n.locale === 'en' && item.status === 1">
              <span v-if="item.realSum > 1000" class="count rl-fr">{{ fomatFloor(item.realSum / 1000, 1) }}K+ Sold</span>
              <span v-else class="count rl-fr">{{ item.realSum }} Sold</span>
            </template>
          </div>
          <!-- 价格 -->
          <div class="price-wrap normal rl-clear">
            <p class="price rl-fl">
              <span class="unit">{{ $i18n.locale === "zh" ? "¥" : "$" }}</span>
              <span>{{$i18n.locale === "zh"? fomatFloat(item.groupSeckillPrice, 2): fomatFloat(item.groupSeckillPrice, 2)}}</span>
            </p>
            <span class="btn rl-fr green" v-if="item.status === 0">{{$i18n.locale === "zh" ? "去购买" : $t("GroupBuy.BuyNow")}}</span>
            <span class="btn rl-fr" v-if="item.status === 1">{{$t("GroupBuy.BuyNow")}}</span>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script>
import { fomatFloat, fomatFloor } from "@/assets/js/common.js";
export default {
  name: "groupBuyItem",
  props: ["groupData", "cateFlag",],
  data() {
    return {};
  },
  mounted(){
   
  },
  methods: {
    // 件数格式化
    fomatFloor(val, n) {
      return fomatFloor(val, n);
    },
    // 价格格式化
    fomatFloat(val, n) {
      return fomatFloat(val, n);
    },
    fixNum(time) {
      var timer = (Array(2).join(0) + time).slice(-2);
      if (timer > 0) {
        return timer;
      } else {
        return 0;
      }
    },
    // 跳转详情
    goDoodsDetail(id, goodsType) {
      let routeData = this.$router.resolve({
        name: "ShopDetail",
        query: {
          good_id: id,
          activityType: 1,  //1:拼团  2：秒杀  3：活动  4：其他
          goodsType: goodsType,
          accessType: 0,
        },
      });
      window.open(routeData.href, "_blank");
    },
  },
};
</script>

<style lang="less" scoped>
@import url("../../assets/less/variable.less");
.group-buy-item {
  &.item-wrap {
    .group-buy-pro:nth-child(1) {
      margin-top: 0;
    }
    .group-buy-pro:nth-child(2) {
      margin-top: 0;
    }
    .group-buy-pro:nth-child(3) {
      margin-top: 0;
    }
    .group-buy-pro:nth-child(4) {
      margin-top: 0;
    }
  }
  .group-buy-pro {
    position: relative;
    float: left;
    margin-top: 15px;
    margin-right: 16px;
    width: 288px;
    height: 424px;
    background-color: @proBg;
    border-radius: 8px;
    overflow: hidden;
    &.white-bg {
      background-color: @white;
    }
    .img {
      display: block;
      margin: 0 auto;
      height: 288px;
    }
    &.no-margin {
      margin-right: 0;
    }
    &:hover {
      box-shadow: 0 7px 17px 2px rgba(0, 0, 0, 0.11);
      transform: translate3d(0, -2px, 0);
    }
    .con-wrap {
      padding: 10px 15px;
    }
    .pro-name {
      font-size: 14px;
      width: 100%;
      height: 44px;
      font-family: FZHei-B01;
      font-weight: 400;
      color: @lightBlack;
      line-height: 20px;
      overflow: hidden;
      float: left;
      text-align: left;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      &.pro-name1 {
        height: 22px;
        -webkit-line-clamp: 1;
      }
    }
    .labels {
      span {
        border-radius: 18px;
        padding-left: 6px;
        padding-right: 6px;
        font-size: 12px;
        line-height: 17px;
        font-family: FZHei-B01;
        font-weight: 400;
        color: @red;
        -webkit-text-size-adjust: none;
        display: inline-block;
        border: 1px solid @red;
      }
    }
    .count {
      padding-left: 15px;
      font-size: 12px;
      color: @lightGray;
      background: url("../../assets/images/hot-big.png") no-repeat center left;
      background-size: 14px 14px;
    }
    .process {
      margin-top: 10px;
      &.top6 {
        top: 6px;
      }
    }
    /deep/ .el-progress {
      top: 3px;
      width: 150px;
      .el-progress-bar__outer {
        background-color: @progressBg;
      }
      .el-progress-bar__inner {
        background: linear-gradient(90deg, @groupBtnColor 0%, @red 100%);
      }
    }
    .price-wrap {
      &.normal {
        line-height: 30px;
        margin-top: 15px;
      }
      .price {
        font-size: 0;
        color: @red;
        font-weight: bold;
        span {
          font-size: 16px;
        }
        .unit {
          font-size: 12px;
        }
      }
    }
    .btn {
      width: 83px;
      height: 30px;
      line-height: 30px;
      font-size: 12px;
      color: @progressBg;
      text-align: center;
      background: linear-gradient(90deg, @groupBtnColor 0%, @red 100%);
      box-shadow: 0px 2px 10px 0px @groupBtnShadow;
      border-radius: 4px;
      &.green {
        background: linear-gradient(90deg, @lightGreen 0%, @green 100%);
        box-shadow: 0px 2px 10px 0px @greenShadow;
      }
    }
    .count-down-wrap {
      position: absolute;
      top: -25px;
      left: 0;
      width: 100%;
      text-align: center;
      .count-down {
        padding: 2px 12px;
        font-size: 0;
        color: @white;
        background-color: @red;
        display: inline-block;
        border-radius: 30px;
        &.green {
          background-color: @green;
        }
        span {
          font-size: 12px;
          &.lg {
            padding: 0 2px;
            font-size: 16px;
          }
        }
      }
    }
    &.recommend-wrap {
      margin-right: 16px;
      width: 592px;
      .labels {
        margin-top: 0;
        margin-bottom: 20px;
        margin-left: 5px;
      }
      .count-down {
        padding-left: 35px;
        font-size: 0;
        color: @lightBlack;
        span {
          font-size: 14px;
          &.lg {
            padding: 0 2px;
            font-size: 16px;
            color: @red;
            &.green {
              color: @green;
            }
          }
        }
      }
      .process {
        margin-top: 0;
        line-height: 30px;
      }
      /deep/ .el-progress {
        top: 10px;
        width: 320px;
      }
    }
  }
}
</style>