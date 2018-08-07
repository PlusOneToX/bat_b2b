<!--
 * @Author: litian
 * @Date: 2018-02-22 17:10:15
 * @LastEditors: litian
-->
<template>
  <div
    class="group-buy-item rl-clear"
    :class="{ 'item-wrap': cateFlag === 'groupList' }"
  >
    <div
      class="group-buy-pro cursor-pointer"
      v-for="(item, index) in groupData"
      :key="index"
      :class="{
        'recommend-wrap': item.isRecommended && cateFlag !== 'groupList',
        'no-margin':
          (hasRecommend && (index === 2 || index === 6)) ||
          (!hasRecommend && index > 0 && (index + 1) % 4 === 0),
        'white-bg': cateFlag === 'groupList',
      }"
      @click="goDoodsDetail(item.id, item.goodsType)"
    >
      <div class="group-item">
        <img
        v-if="$i18n.locale === 'zh'"
        class="img"
        :src="
          item.isRecommended && cateFlag !== 'groupList'
            ? item.recommendPicUrl
            : item.imageUrl1
        "
        :alt="item.goodsName"
        />
        <img
          v-else
          class="img"
          :src="
            item.isRecommended && cateFlag !== 'groupList'
              ? item.recommendPicUrlEn
              : item.imageUrl1en
          "
          :alt="item.goodsNameEn"
        />
        <div class="con-wrap">
          <h6
            class="pro-name"
            :class="{
              'pro-name1':
                (cateFlag === 'groupList' &&
                  item.discount &&
                  item.discount < 10) ||
                (item.discount && item.discount < 10 && !item.isRecommended),
            }"
          >
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
                :percentage="0"
                :show-text="false"
                v-if="item.status === 0"
              ></el-progress>
              <el-progress
                class="rl-fl"
                :stroke-width="12"
                :percentage="(item.realSum/item.maxNum * 100>1)?(item.realSum/item.maxNum * 100):1"
                :show-text="false"
                v-if="item.status === 1"
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
                  >{{ fomatFloor(item.saleSum / 1000, 1) }}K+ Sold</span
                >
                <span v-else class="count rl-padding-right-default rl-fr"
                  >{{ item.realSum }} Sold</span
                >
              </template>
            </div>
          </template>
          <template v-else>
            <p class="labels" v-if="item.discount && item.discount < 10">
              <span v-if="$i18n.locale === 'zh'"
                >低至{{ fomatFloor(item.discount, 1) }}折</span
              >
              <span v-else
                >Up to {{ 100 - fomatFloor(item.discount, 1) * 10 }}% off</span
              >
            </p>
            <div
              class="process rl-clear"
              :class="{ top6: item.discount && item.discount >= 10 }"
            >
              <el-progress
                class="rl-fl"
                :stroke-width="8"
                :percentage="0"
                :show-text="false"
                v-if="item.status === 0"
              ></el-progress>
              <el-progress
                class="rl-fl"
                :stroke-width="8"
                :percentage="(item.realSum /item.maxNum* 100>1)?(item.realSum/item.maxNum * 100):1"
                :show-text="false"
                v-if="item.status === 1"
              ></el-progress>
              <template v-if="$i18n.locale === 'zh' && item.status === 1">
                <span v-if="item.realSum > 10000" class="count rl-fr"
                  >已拼 {{ fomatFloor(item.realSum / 10000, 2) }} 万+</span
                >
                <span v-else class="count rl-fr">已拼 {{ item.realSum }} 件</span>
              </template>
              <template v-if="$i18n.locale === 'en' && item.status === 1">
                <span v-if="item.realSum > 1000" class="count rl-fr"
                  >{{ fomatFloor(item.realSum / 1000, 1) }}K+ Sold</span
                >
                <span v-else class="count rl-fr">{{ item.realSum }} Sold</span>
              </template>
            </div>
            <div class="price-wrap normal rl-clear">
              <p class="price rl-fl">
                <span class="unit">{{ $i18n.locale === "zh" ? "¥" : "$" }}</span>
                <span>{{
                  $i18n.locale === "zh"
                    ? fomatFloat(item.groupSeckillPrice, 2)
                    : fomatFloat(item.groupSeckillPrice, 2)
                }}</span>
              </p>
              <div class="count-down-wrap">
                <p
                  class="count-down"
                  :class="{ green: item.status === 0 }"
                  v-if="$i18n.locale === 'zh'"
                >
                  <template v-if="item.status === 0">
                    <span>距拼团开始:</span>
                    <span class="lg green">{{
                      Math.floor(item.diff / (24 * 3600 * 1000)) > 0
                        ? Math.floor(item.diff / (24 * 3600 * 1000))
                        : 0
                    }}</span>
                    <span>天</span>
                    <span class="lg green">{{
                      fixNum(Math.floor((item.diff / (1000 * 60 * 60)) % 24))
                    }}</span>
                    <span>时</span>
                    <span class="lg green">{{
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
                <p class="count-down" :class="{ green: item.status === 0 }" v-else>
                  <template v-if="item.status === 0">
                    <span>Start after</span>
                    <span class="lg green">{{
                      " " +
                      (Math.floor(item.diff / (24 * 3600 * 1000)) > 0
                        ? Math.floor(item.diff / (24 * 3600 * 1000))
                        : 0)
                    }}</span>
                    <span>{{
                      Math.floor(item.diff / (24 * 3600 * 1000)) === 1
                        ? " day "
                        : " days "
                    }}</span>
                    <span class="lg green">{{
                      fixNum(Math.floor((item.diff / (1000 * 60 * 60)) % 24))
                    }}</span>
                    <span>:</span>
                    <span class="lg green">{{
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
              <span class="btn rl-fr green" v-if="item.status === 0">{{
                $i18n.locale === "zh" ? "去购买" : $t("GroupBuy.BuyNow")
              }}</span>
              <span class="btn rl-fr" v-if="item.status === 1">{{
                $t("GroupBuy.BuyNow")
              }}</span>
            </div>
          </template>
        </div>
      </div>
      <!---货品列表-已废弃---->
      <div class="group-table" v-if="tabs==='two' && item.itemList.length > 0">
        <table>
          <tr>
            <th width="120">{{ $t("ShopCarts.Picture") }}</th>
            <th>{{ $t("ShopCarts.ItemName") }}</th>
            <th width="140">{{ $t("GroupBuy.Price") }}</th>
            <th width="140">
              {{ $t("GroupBuy.SoldCount") }}
              <span class="box-unit">件</span>
            </th>
            <th width="140" class="packing-unit">
              {{ $t("Promotion.SurplusQuantity") }}
              <span class="box-unit">件</span>
            </th>
          </tr>
          <tr class="goods-item rl-relative" v-for="(good, index) in item.itemList"
            @click="selectGoodsItem(index, good)" tabindex="0" hidefocus="true"
            :key="index">
            <td>
              <div class="shop-img cursor-pointer">
                <img v-if="
                    good.itemImg !== undefined &&
                    good.itemImg !== null &&
                    good.itemImg !== ''
                  " @click="magnify(good.itemImg)" :src="
                    good.itemImg +
                    '?x-oss-process=image/resize,h_56,l_56'
                  " />
                  <span class="activity">拼团</span>
              </div>
            </td>
            <td class="name">
              <span class="code" v-if="$i18n.locale === 'zh'">{{ good.itemName }}</span>
              <span class="code" v-else>{{ good.itemNameEn }}</span>
            </td>
            <td>
              <span class="unit">{{ $i18n.locale === "zh" ? "¥" : "$" }}</span>
              <span class="code">{{ fomatFloat(good.groupPrice, 2) }}</span>
            </td>
            <td>
            <span class="code">{{ good.saleNum }}</span>
          </td>
          <td>
            <span class="code">{{ good.surplusQuantity }}</span>
          </td>
          </tr>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import { fomatFloat, fomatFloor } from '@/assets/js/common.js';
export default {
  name: 'groupBuyItem',
  props: ['groupData', 'cateFlag', 'hasRecommend', 'tabs'],
  data () {
    return {};
  },
  methods: {
    // 件数格式化
    fomatFloor (val, n) {
      return fomatFloor(val, n);
    },
    // 价格格式化
    fomatFloat (val, n) {
      return fomatFloat(val, n);
    },
    fixNum (time) {
      var timer = (Array(2).join(0) + time).slice(-2);
      if (timer > 0) {
        return timer;
      } else {
        return 0;
      }
    },
    // 跳转详情
    goDoodsDetail (id, goodsType) {
      let routeData = this.$router.resolve({
        name: 'ShopDetail',
        query: {
          good_id: id,
          activityType: 0,
          goodsType: goodsType,
          accessType: 0
        }
      });
      window.open(routeData.href, '_blank');
    }
  }
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
    padding: 28px 50px;
    margin-bottom: 16px;
    width: 100%;
    background-color: @proBg;
    border-radius: 8px;
    box-sizing: border-box;
    overflow: hidden;
    &.white-bg {
      background-color: @white;
    }
    &:hover {
      box-shadow: 0 7px 17px 2px rgba(0, 0, 0, 0.11);
      transform: translate3d(0, -2px, 0);
    }
    .group-item{
      display: flex;
      .img {
        display: inline-block;
        width:100px;
        height: 100px;
      }
      &.no-margin {
        margin-right: 0;
      }

      .con-wrap {
        flex:1;
        display: inline-block;
        margin-left:45px;
      }
      .pro-name {
        display: inline-block;
        font-size: 20px;
        height: 44px;
        font-family: FZHei-B01;
        font-weight: 400;
        color: @lightBlack;
        line-height: 20px;
        overflow: hidden;
        text-align: left;
        text-overflow: ellipsis;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        vertical-align: middle;
        &.pro-name1 {
          height: 22px;
          -webkit-line-clamp: 1;
        }
      }
      .labels {
        display: inline-block;
        margin-left:10px;
        vertical-align: middle;
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
        margin-top:10px;
        padding-left: 15px;
        font-size: 12px;
        color: @lightGray;
        background: url("../../assets/images/hot-big.png") no-repeat center left;
        background-size: 14px 14px;
      }
      .process {
        width:150px;
        float:right;
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
          margin-top: 36px;
        }
        .price {
          height: 30px;
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
        display: inline-block;
        margin-left:24px;
        height: 30px;
        line-height: 30px;
        text-align: center;
        .count-down {
          height: 30px;
          padding: 0 12px;
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
    .group-table{
      margin-top:30px;
      width: 100%;
      border-bottom: 1px solid @bdColor;
      &.hidden {
        max-height: 352px;
        overflow: hidden;
      }

      .code {
        line-height: 20px;
      }

      table {
        word-wrap: break-word;
        word-break: break-all;
        border-collapse: collapse;
        width: 100%;

        tr {

          &.goods-item:hover,
          &.goods-item:active {
            background-color: @grayBg;
          }

          th,
          td.spe {
            word-wrap: break-word;
            word-break: break-word;
          }

          th {
            height: 35px;
            text-align: center;
            background-color: #F5F7FA;
            font-size: 13px;
            font-weight: 400;
            word-wrap: break-word;
            word-break: break-word;
          }

          td.td-h1 {
            white-space: normal;
            border-left: 0;

            .activity-label {
              width: 22px;
              height: 50px;
              line-height: 25px;
              border-radius: 5px;
              word-break: break-word;
            }
          }

          td {
            height: 60px;
            max-height: 60px;
            text-align: center;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
            color: @lightBlack;
            font-size: 12px;
            border-top: 1px dashed @bdColor;
            &.name{
              max-width:300px;
              padding:0 20px;
            }
            .shop-img{
              position: relative;
              width:56px;
              height: 56px;
              margin:auto;
              overflow: hidden;
              .activity {
                padding: 0 16px;
                position: absolute;
                top: 1px;
                left: -16px;
                font-size: 12px;
                height: 20px;
                line-height: 20px;
                background: @actLabel;
                font-family: FZHei-B01;
                font-weight: 400;
                color: @white;
                transform: rotate(-45deg);
                background: @red;
              }
            }
            button {
              padding: 0;
              border: 0;
              font-size: 12px;
              color: @lightBlack;
              background-color: transparent;
            }

            .register {
              width: 55px;
              color: #ff7900;
              border-bottom: 1px solid #ff7900;
            }
          }
        }

        .rl-single-limit {
          width: 80px;

          &+.rl-single-limit {
            margin-left: 0;
          }
        }
      }

      .el-input-number {
        position: relative;
        width: 90px;
      }
    }
  }
  .box-unit{
    padding: 0 2px;
    min-width: 18px;
    min-height: 18px;
    font-size: 12px;
    color: @blue;
    text-align: center;
    line-height: 18px;
    border: 1px solid @blue;
    border-radius: 22px;
  }
}
</style>
