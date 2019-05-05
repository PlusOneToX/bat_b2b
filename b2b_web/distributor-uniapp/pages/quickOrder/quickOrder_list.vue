<template>
  <view class="ksListOrder">
    <!-- 顶部标题栏 -->
    <view class="status_bar"> <!-- 这里是状态栏 --></view>
    <view class="ks-top">
      <view class="ks-top-nav">
        <image src="../../static/imgs/icon_back.png" @click="toback"></image>
        <text>|</text>
        <image src="../../static/imgs/icon_nav.png" @click="openNav"></image>
      </view>
      <view class="ks-top-title">清单</view>
    </view>
    <!-- 导航 -->
    <view class="ksList-nav" v-if="navShow">
      <view class="ksList-navTrangle"></view>
      <view class="ksList-navTb">
        <view class="ksList-nav-line" @click="navToPage('/pages/index/index')">
          <image src="../../static/imgs/ks_index.png"></image>
          <text>首页</text>
        </view>
        <view
          class="ksList-nav-line"
          @click="navToPage('/pages/classify/classify')"
        >
          <image src="../../static/imgs/ks_classfisy.png"></image>
          <text>分类</text>
        </view>
        <view class="ksList-nav-line" @click="navToPage('quickOrder')">
          <image src="../../static/imgs/ks_icon.png"></image>
          <text>快速订货</text>
        </view>
        <view
          class="ksList-nav-line"
          @click="navToPage('/pages/shoppingCart/shoppingCart')"
        >
          <image src="../../static/imgs/ks_shopingCar.png"></image>
          <text>购物车</text>
        </view>
        <view
          class="ksList-nav-line"
          @click="navToPage('/pages/personalCenter/personalCenter')"
        >
          <image src="../../static/imgs/ks_personal.png"></image>
          <text>我的</text>
        </view>
      </view>
    </view>

    <!-- 列表-new -->
    <view class="ksList-listView">
      <view v-for="item in goodsList" :key="item.id" class="ks-listLine">
        <!-- 商品 -->
        <view class="ks-listGoodName">
          <text
            class="iconfont"
            :class="
              item.isCheck ? 'icon-icon_selected' : 'icon-icon_selected_def'
            "
            :style="{ color: themeColor }"
            @click="checkOneFun(item)"
          ></text>
          <view @click="openGoodsInfoPopupFun(item)" class="ks-listGoodNameRg">
            <view>{{ item.goodsName }}</view>
            <image src="../../static/imgs/icon_chakan.png"></image>
          </view>
        </view>
        <!-- 货品 -->
        <view
          class="ks-listItem"
          v-for="(items, indexs) in item.goodsItems"
          :key="items.id"
          :style="{
            'border-bottom':
              indexs == item.goodsItems.length - 1
                ? 'none'
                : '1rpx solid #F3F4F8',
          }"
        >
          <view class="ks-listItem-info" @click="toGoodsDetails(item)">
            <text
              class="iconfont ks-iconfont"
              :class="
                items.isCheck ? 'icon-icon_selected' : 'icon-icon_selected_def'
              "
              :style="{ color: themeColor }"
              @click.stop="checkTwoFun(items)"
            ></text>
            <image class="ks-listItem-infoItemImg" :src="items.itemImg"></image>
            <view class="ks-listItem-infoCenter">
              <view class="ks-listItem-infoLine">
                <text>编码：</text>
                <text>{{ item.goodsNo }}</text>
              </view>
              <view class="ks-listItem-infoLine">
                <text>规格：</text>
                <text>{{ items.specsName }}</text>
              </view>
              <view class="ks-listItem-infoLine">
                <text>购买单位：</text>
                <text>{{ items.boxNum }}/{{ items.boxType }}</text>
              </view>
              <!-- <view class="ks-listItem-infoLineTip">
							   <text>满500减100</text>
							   <text>秒杀</text>
						   </view> -->
            </view>
            <view class="ks-listItem-infoPrice" v-if="userId != ''"
              >￥{{ items.salePrice }}
            </view>
            <!-- <view class="ks-listItem-infoPrice" v-else>登录后查看</view> -->
          </view>
          <view class="ks-listItem-numItem">
            <view class="ks-listItem-numItem">
              <text>在库：{{ items.inStockUsableCount }}</text>
              <text>在途：{{ items.onWayUsableCount }}</text>
            </view>
            <!-- 计算器 -->
            <view
              class="ks-calculator"
              :style="{
                'border-color': items.buyNum > 0 ? themeColor : '#C2C2C2 ',
              }"
            >
              <text @click="numberChange(items.itemCount, items, item, 1)"
                >-
              </text>
              <input
                minNum="1"
                v-model="items.buyNum"
                @blur="numberChange(items.itemCount, items, item, 3)"
                :style="{
                  'border-color': items.buyNum > 0 ? themeColor : '#C2C2C2 ',
                  color: items.buyNum > 0 ? themeColor : '#C2C2C2 ',
                }"
              />
              <text @click="numberChange(items.itemCount, items, item, 2)"
                >+
              </text>
            </view>
            <!-- 库存不足提示 -->
            <view class="ks-listItem-KuCunBox" v-if="items.kuCunShow">
              <view class="ks-listItem-KuCun">
                <view class="ks-listItem-KuCunTrangle"></view>
                <view class="ks-listItem-KuCunText">库存不足</view>
              </view>
            </view>
          </view>

          <!--  超出库存数量之后，自动添加在途数量并显示影响发货时间提示-->
          <!-- <view class="ks-listItem-tip" v-if="items.buyNum>items.inStockUsableCount">此商品数量中有在途商品，可能会影响发货时</view> -->
        </view>
        <view class="ks-listItem-totalItem">
          <text class="ks-totalItem-num">共{{ item.totalNum }}件</text>
          <view class="ks-totalItem-priceItem">
            <!-- <text class="ks-totalItem-priceActivity">共计优惠：￥{{item.discountsPrice}}</text> -->
            <text class="ks-totalItem-pricetext">小计：</text>
            <text class="ks-totalItem-priceAll">￥{{ item.totalPrice }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部 -->
    <view class="ksList-btm">
      <view class="ksList-btm-top">
        <view class="ksList-btm-topLf" @click="allCheckFun">
          <text
            class="iconfont ks-iconfont"
            :class="allCheck ? 'icon-icon_selected' : 'icon-icon_selected_def'"
            :style="{ color: themeColor }"
          ></text>
          <text>全选</text>
        </view>
        <view class="ksList-btm-topRg">总共{{ shopTotalNum }}件</view>
      </view>
      <view class="ksList-btm-price">
        <!-- <text class="ksList-btm-priceDiscounts">优惠总计：￥{{discountsPrice}}</text> -->
        <text class="ksList-btm-priceTotalText">总计：</text>
        <text class="ksList-btm-priceNum">￥{{ totalPrice }}</text>
      </view>
      <view class="ksList-btm-btn">
        <text
          :style="{ border: '2rpx solid' + themeColor, color: themeColor }"
          @click="addShopCart"
          >加入购物车
        </text>
        <text
          :style="{ border: '2rpx solid' + themeColor, background: themeColor }"
          @click="buyClick"
          >立即购买
        </text>
      </view>
      <!-- <view class="ksList-btm-line"></view> -->
    </view>

    <!-- 查看商品的基本信息弹框 -->
    <uni-popup ref="goodsInfoPopup" class="ks-goodsInfo-popup">
      <view class="ks-goodsInfo-popup-content">
        <view
          class="ks-goodsInfo-popup-box"
          @click="toGoodsDetails(goodsItemObj)"
        >
          <image :src="goodsItemObj.imageUrl1"></image>
          <view class="ks-goodsInfo-popup-boxRG">
            <view>
              <text>上架：</text>
              <text>{{ goodsItemObj.saleTime }}</text>
            </view>
            <view>
              <text>编号：</text>
              <text>{{ goodsItemObj.goodsNo }}</text>
            </view>
            <view>
              <text>销量：</text>
              <text v-if="goodsItemObj.saleNum < 10000">{{
                goodsItemObj.saleTime
              }}</text>
              <text
                v-if="
                  goodsItemObj.saleNum > 10000 || goodsItemObj.saleNum == 10000
                "
                >{{ Math.trunc(goodsItemObj.saleNum / 10000) }}万+
              </text>
            </view>
          </view>
        </view>
        <view class="ks-goodsInfo-popup-btn" @click="closeGoodsInfoPopupFun"
          >关闭
        </view>
      </view>
    </uni-popup>

    <!-- 提示框 -->
    <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>
  </view>
</template>

<script>
import {
  goodsDetails,
  userShopSetting,
  addShoppingcart,
} from "../../common/api.js";
export default {
  data() {
    return {
      themeColor: "",
      goodsList: [], //商品列表
      navShow: false, //导航展示隐藏
      userId: "", //分销商id
      allCheck: false, //全选
      totalNum: 0,
      shopTotalNum: 0, //共几件
      totalPrice: 0, //共计金额
      discountsPrice: 0, //优惠总计
      goodsItems: [], //详情列表
      isInTransit: false, //是否包含在途
      //购买单位
      quickList: [], //快速下单的列表
      guiGeShow: true, //规格弹框

      tipTextShow: false,
      tipText: "",
      goodsItemObj: {}, //商品信息
      goodIds: [], //商品ids
    };
  },
  onShow() {
    this.themeColor = uni.getStorageSync("themeColor");
    let userId = uni.getStorageSync("userId");
    if (userId && userId != "" && userId != "undefined") {
      this.userId = userId;
    }
    this.goodsList = JSON.parse(uni.getStorageSync("ksOrderList"));
    console.log("下单货品：", this.goodsList);
    let that = this;
    this.goodsList.forEach((item) => {
      let totalNum = 0;
      let totalPrice = 0;
      item.goodsItems.forEach((items) => {
        let totalNum2 = items.boxNum * items.buyNum;
        let totalPrice2 = totalNum2 * items.salePrice;
        totalNum += totalNum2;
        totalPrice += totalPrice2;
      });
      that.$set(item, "totalNum", totalNum);
      that.$set(item, "totalPrice", totalPrice);
      that.$set(item, "discountsPrice", 0);
    });
  },
  onLoad() {},
  // 触底分页
  onReachBottom() {},
  methods: {
    //返回
    toback() {
      uni.switchTab({
        url: "quickOrder",
      });
      // uni.removeStorageSync("ksOrderList");
    },

    // 轻提示弹框
    tipFun(text) {
      let that = this;
      this.tipText = text;
      this.tipTextShow = true;
      setTimeout(function () {
        that.tipTextShow = false;
      }, 3000);
    },
    // 打开导航
    openNav() {
      this.navShow = !this.navShow;
    },
    // 导航跳转
    navToPage(pageName) {
      uni.switchTab({
        url: pageName,
      });
      uni.removeStorageSync("ksOrderList");
    },

    // 装箱规格
    openBoxShow(items) {
      this.$set(items, "boxShow", !items.boxShow);
    },

    // 商品弹框---打开
    openGoodsInfoPopupFun(item) {
      this.goodsItemObj = item;
      this.$refs.goodsInfoPopup.open();
    },
    // 商品弹框---关闭
    closeGoodsInfoPopupFun() {
      this.goodsItemObj = {};
      this.$refs.goodsInfoPopup.close();
    },

    // 选择规格--y
    selectBoxsFun(item, gItem, goodsItems) {
      let that = this;
      console.log("规格：", item);
      console.log("此货品：", gItem);
      // 计算当前货品总购买数
      let buyTotalNum = Number(gItem.buyNum) * item.boxNum;
      //当前货品总库存--- （是否包含在途）
      let stockTotalNum = that.isInTransit
        ? gItem.inStockUsableCount + gItem.onWayUsableCount
        : gItem.inStockUsableCount;
      if (stockTotalNum >= buyTotalNum) {
        that.$set(gItem, "boxType", item.boxType);
        that.$set(gItem, "boxNum", item.boxNum);
        if (gItem.inStockUsableCount >= buyTotalNum) {
          that.$set(gItem, "zaiKuCount", buyTotalNum);
          that.$set(gItem, "zaiTuCount", 0);
        } else {
          that.$set(gItem, "zaiKuCount", gItem.inStockUsableCount);
          that.$set(
            gItem,
            "zaiTuCount",
            buyTotalNum - gItem.inStockUsableCount
          );
        }
        that.$set(gItem, "itemCount", buyTotalNum);
      } else {
        this.$set(item, "kuCunShow", true);
        setInterval(function () {
          that.$set(item, "kuCunShow", false);
        }, 2000);
        that.tipFun("库存不足");
      }
      // 总价格--要去匹配活动计算价格
      let lastPrice = (gItem.zaiKuCount + gItem.zaiTuCount) * gItem.salePrice;
      // 促销活动
      if (gItem.promotions && gItem.promotions.length > 0) {
        let rulesList = gItem.promotions[0].rules;
        let onWayFlag = gItem.promotions[0].onWayFlag; //在途商品是否参与活动 1.参与，0.不参与
        let promoStatus = gItem.promotions[0].promoStatus; //促销状态：0 未开始, 1 促销中 2 已过期 3 提前结束
        let promoType = gItem.promotions[0].promoType; //活动类型，1 普通活动，2 阶梯活动
        let ruleItem = rulesList[0];
        // rulesList.forEach(ruleItem=>{
        let isOneBuyCount =
          onWayFlag == 0
            ? gItem.zaiKuCount
            : gItem.zaiKuCount + gItem.zaiTuCount;
        let isOneBuyMoney =
          onWayFlag == 0
            ? gItem.zaiKuCount * gItem.salePrice
            : Number(gItem.itemCount) * gItem.salePrice;
        //货品活动是否累计 addUpFlag，1是 0否( ruleType 规则对象是2或3时有效)
        if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
          let childrenList = goodsItems;
          let zCoun = gItem.zaiKuCount;
          let tCount = gItem.zaiTuCount;
          let zPrice = gItem.zaiKuCount * gItem.salePrice;
          let tPrice = gItem.zaiTuCount * gItem.salePrice;
          for (let i = 0; i < childrenList.length; i++) {
            // 判断同个活动累计数量
            if (
              childrenList[i].id != gItem.id &&
              childrenList[i].goodsPromotionId == gItem.goodsPromotionId &&
              childrenList[i].itemCount > 0
            ) {
              zCoun += childrenList[i].zaiKuCount;
              tCount += childrenList[i].zaiTuCount;
              zPrice += childrenList[i].zaiKuCount * childrenList[i].salePrice;
              tPrice += childrenList[i].zaiTuCount * childrenList[i].salePrice;
            }
          }
          isOneBuyCount = onWayFlag == 0 ? zCoun : zCoun + tCount;
          isOneBuyMoney = onWayFlag == 0 ? zPrice : zPrice + tPrice;
        }
        ruleItem.conditions.forEach((cItem) => {
          if (promoStatus == 1) {
            //moneyOrCount 规则形式：1金额 2数量
            if (
              (ruleItem.moneyOrCount == 1 &&
                isOneBuyMoney >= cItem.oneBuyMoney) ||
              (ruleItem.moneyOrCount == 2 && isOneBuyCount >= cItem.oneBuyCount)
            ) {
              that.$set(gItem, "rulesId", ruleItem.id);
              that.$set(gItem, "conditionsId", cItem.id);
              // specialFlag 是否特价，1是 0否
              if (cItem.specialFlag == 1) {
                lastPrice =
                  onWayFlag == 0
                    ? gItem.zaiKuCount * cItem.specialPrice +
                      gItem.zaiTuCount.gItem.salePrice
                    : (gItem.zaiKuCount + cItem.specialPrice) *
                      cItem.specialPrice;
                //  活动累计情况下，相同的货品都按特价计算
                if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
                  let childrenList = goodsItems;
                  for (let i = 0; i < childrenList.length; i++) {
                    if (
                      childrenList[i].id != gItem.id &&
                      childrenList[i].goodsPromotionId ==
                        gItem.goodsPromotionId &&
                      childrenList[i].itemCount > 0
                    ) {
                      let childrenTotalPrice =
                        onWayFlag == 0
                          ? childrenList[i].zaiKuCount * cItem.specialPrice +
                            childrenList[i].zaiTuCount *
                              childrenList[i].salePrice
                          : childrenList[i].itemCount * cItem.specialPrice;
                      that.$set(
                        childrenList[i],
                        "lastPrice",
                        childrenTotalPrice
                      );
                      that.$set(childrenList[i], "conditionsId", cItem.id);
                    }
                  }
                }
              } else {
                //reduceOrPresent 促销统计方式：1满减 2满赠
                if (cItem.reduceOrPresent == 1) {
                  // reduceType 满减类型， 2折扣  1减免
                  if (cItem.reduceType == 2) {
                    let zheKouPrice = (
                      (gItem.salePrice * cItem.discount) /
                      100
                    ).toFixed(2);
                    lastPrice =
                      onWayFlag == 0
                        ? gItem.zaiKuCount * zheKouPrice +
                          gItem.zaiTuCount * gItem.salePrice
                        : (gItem.zaiKuCount + gItem.zaiTuCount) * zheKouPrice;
                    //  活动累计情况下，相同的货品都按减免计算
                    if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
                      let childrenList = goodsItems;
                      for (let i = 0; i < childrenList.length; i++) {
                        if (
                          childrenList[i].id != gItem.id &&
                          childrenList[i].goodsPromotionId ==
                            gItem.goodsPromotionId &&
                          childrenList[i].itemCount > 0
                        ) {
                          let zheKouPrice2 = (
                            (childrenList[i].salePrice * cItem.discount) /
                            100
                          ).toFixed(2);
                          let childrenTotalPrice =
                            onWayFlag == 0
                              ? childrenList[i].zaiKuCount * zheKouPrice2 +
                                childrenList[i].zaiTuCount *
                                  childrenList[i].salePrice
                              : childrenList[i].itemCount * zheKouPrice2;
                          that.$set(
                            childrenList[i],
                            "lastPrice",
                            childrenTotalPrice
                          );
                          that.$set(childrenList[i], "conditionsId", cItem.id);
                        }
                      }
                    }
                  } else {
                    //  活动累计情况下，勾选上的货品都按减免计算
                    if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
                      let childrenList = goodsItems;
                      for (let i = 0; i < childrenList.length; i++) {
                        if (
                          childrenList[i].goodsPromotionId ==
                            gItem.goodsPromotionId &&
                          childrenList[i].itemCount > 0
                        ) {
                          let reduction2 = cItem.reduction;
                          if (cItem.reductionPresentAddFlag == 1) {
                            reduction2 =
                              ruleItem.moneyOrCount == 1
                                ? onWayFlag == 0
                                  ? childrenList[i].zaiKuCount *
                                    childrenList[i].salePrice *
                                    (cItem.reduction / cItem.oneBuyMoney)
                                  : childrenList[i].itemCount *
                                    childrenList[i].salePrice *
                                    (cItem.reduction / cItem.oneBuyMoney)
                                : onWayFlag == 0
                                ? childrenList[i].zaiKuCount *
                                  (cItem.reduction / cItem.oneBuyCount)
                                : childrenList[i].itemCount *
                                  (cItem.reduction / cItem.oneBuyCount);
                          }
                          let childrenTotalPrice = (
                            childrenList[i].totalPrice - reduction2
                          ).toFixed(2);
                          if (childrenList[i].id == gItem.id) {
                            lastPrice = childrenTotalPrice;
                          }
                          that.$set(
                            childrenList[i],
                            "lastPrice",
                            childrenTotalPrice
                          );
                          that.$set(childrenList[i], "conditionsId", cItem.id);
                        }
                      }
                    } else {
                      // 判断是否叠加的减免金额
                      let reduction = cItem.reduction;
                      if (cItem.reductionPresentAddFlag == 1) {
                        reduction =
                          ruleItem.moneyOrCount == 1
                            ? onWayFlag == 0
                              ? gItem.zaiKuCount *
                                gItem.salePrice *
                                (cItem.reduction / cItem.oneBuyMoney)
                              : gItem.itemCount *
                                gItem.salePrice *
                                (cItem.reduction / cItem.oneBuyMoney)
                            : onWayFlag == 0
                            ? gItem.zaiKuCount *
                              (cItem.reduction / cItem.oneBuyCount)
                            : gItem.itemCount *
                              (cItem.reduction / cItem.oneBuyCount);
                      }
                      lastPrice = (
                        gItem.itemCount * gItem.salePrice -
                        reduction
                      ).toFixed(2);
                    }
                  }
                }
              }
            }
          }
        });

        // })
      }
      // 拼团
      if (
        gItem.itemCount > 0 &&
        gItem.groupSeckills &&
        gItem.groupSeckills.length > 0
      ) {
        // groupSeckillStatus 拼团秒杀状态： 0、未开始、1、进行中 2、已暂停 3、已过期 4 提前结束
        let groupItem = gItem.groupSeckills[0];
        if (
          groupItem.groupSeckillStatus == 1 &&
          gItem.itemCount >= groupItem.minNum &&
          (groupItem.mtoFlag == 1
            ? gItem.itemCount <= groupItem.maxNum - groupItem.realSum
            : true)
        ) {
          that.$set(gItem, "groupId", groupItem.groupSeckillId);
          that.$set(gItem, "groupSeckillId", groupItem.groupSeckillId);
          lastPrice = gItem.itemCount * groupItem.groupSeckillPrice;
        } else {
          that.$set(gItem, "groupSeckillId", "");
        }
      }
      // 更新单个货品的最终价格
      that.$set(gItem, "lastPrice", lastPrice);
      that.$set(gItem, "guiGeShow", false);
      that.totalNum = 0;
      let list = JSON.parse(JSON.stringify(that.goodsList));
      list.forEach((item) => {
        item.goodsItems.forEach((items) => {
          that.totalNum += items.buyNum * items.boxNum;
        });
      });
      console.log("规格商品列表：", that.goodsList);
    },

    // 计数器
    numberChange(itemNum, item, goodsItems, type) {
      clearInterval(item.setInter);
      console.log("老数据", itemNum);
      console.log(item.lastPrice);
      let that = this;
      if (type == 1 && itemNum == 0) {
        return;
      } //零不能减
      let itemNums =
        type == 3
          ? item.buyNum
          : type == 1
          ? parseInt(itemNum / item.boxNum) - 1
          : parseInt(itemNum / item.boxNum) + 1;

      // ------计算数量----start
      // 货品数量=计数器数 * 装箱规格数量
      let goodNum = Number(itemNums) * item.boxNum;
      // this.isInTransit 是否包含在途
      let goodStockNum = that.isInTransit
        ? item.inStockUsableCount + item.onWayUsableCount
        : item.inStockUsableCount;
      if (goodNum <= goodStockNum) {
        that.$set(item, "buyNum", Number(itemNums));
        that.$set(item, "itemCount", goodNum);
        that.$set(
          item,
          "zaiKuCount",
          goodNum <= item.inStockUsableCount ? goodNum : item.inStockUsableCount
        );
        that.$set(
          item,
          "zaiTuCount",
          goodNum <= item.inStockUsableCount
            ? 0
            : goodNum - item.inStockUsableCount
        );
        that.$set(item, "kuCunShow", false);
        let totalNum = goodsItems.totalNum - itemNum + goodNum;
        this.$set(goodsItems, "totalNum", totalNum);
      } else {
        // this.$set(item,'buyNum',Number(itemNum-1));
        that.$set(item, "kuCunShow", true);

        item.setInter = setInterval(function () {
          that.$set(item, "kuCunShow", false);
        }, 2000);
      }
      // ------计算数量----end
      // -----计算价格--------start

      let lastPrice = goodNum * item.salePrice; //当前会员总价
      // activityType（0:无活动 1：拼团  2：秒杀  3：普通活动  ）
      if (item.activityType == 1 || item.activityType == 2) {
        let groupList = item.groupSeckills;
        let groupItem = groupList[0]; //默认选择第一个活动
        // groupSeckillStatus 拼团秒杀状态： 0、未开始、1、进行中 2、已暂停 3、已过期 4 提前结束
        if (
          groupItem.groupSeckillStatus == 1 &&
          goodNum >= groupItem.minNum &&
          (groupItem.mtoFlag == 1
            ? goodNum <= groupItem.maxNum - groupItem.realSum
            : true)
        ) {
          that.$set(item, "groupId", groupItem.groupSeckillId);
          that.$set(item, "groupSeckillId", groupItem.groupSeckillId);
          lastPrice = goodNum * groupItem.groupSeckillPrice;
        } else {
          that.$set(item, "groupSeckillId", "");
        }
      } else if (item.activityType == 3) {
        let zaiKuCount =
          goodNum <= item.inStockUsableCount
            ? goodNum
            : item.inStockUsableCount;
        let zaiTuCount =
          goodNum <= item.inStockUsableCount
            ? 0
            : goodNum - item.inStockUsableCount;
        let pItem = item.promotions[0]; //默认选中第一个活动--后期扩展
        let rulesList = pItem.rules;
        let onWayFlag = pItem.onWayFlag; //在途商品是否参与活动 1.参与，0.不参与
        let promoStatus = pItem.promoStatus; //促销状态：0 未开始, 1 促销中 2 已过期 3 提前结束
        let promoType = pItem.promoType; //活动类型，1 普通活动，2 阶梯活动
        let ruleItem = rulesList[0];
        let isOneBuyCount =
          onWayFlag == 0 ? item.zaiKuCount : item.zaiKuCount + item.zaiTuCount;
        let isOneBuyMoney =
          onWayFlag == 0
            ? item.zaiKuCount * item.salePrice
            : goodNum * item.salePrice;
        //货品活动是否累计 addUpFlag，1是 0否( ruleType 规则对象是2或3时有效)
        if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
          let childrenList = goodsItems;
          let zCoun = zaiKuCount;
          let tCount = zaiTuCount;
          let zPrice = zaiKuCount * item.salePrice;
          let tPrice = zaiTuCount * item.salePrice;
          for (let i = 0; i < childrenList.length; i++) {
            // 判断同个活动累计数量
            if (
              childrenList[i].id != item.id &&
              childrenList[i].goodsPromotionId == item.goodsPromotionId &&
              childrenList[i].itemCount > 0
            ) {
              zCoun += childrenList[i].zaiKuCount;
              tCount += childrenList[i].zaiTuCount;
              zPrice += childrenList[i].zaiKuCount * childrenList[i].salePrice;
              tPrice += childrenList[i].zaiTuCount * childrenList[i].salePrice;
            }
          }
          isOneBuyCount = onWayFlag == 0 ? zCoun : zCoun + tCount;
          isOneBuyMoney = onWayFlag == 0 ? zPrice : zPrice + tPrice;
        }

        ruleItem.conditions.forEach((cItem) => {
          if (promoStatus == 1) {
            //moneyOrCount 规则形式：1金额 2数量
            if (
              (ruleItem.moneyOrCount == 1 &&
                isOneBuyMoney >= cItem.oneBuyMoney) ||
              (ruleItem.moneyOrCount == 2 && isOneBuyCount >= cItem.oneBuyCount)
            ) {
              that.$set(item, "rulesId", ruleItem.id);
              that.$set(item, "conditionsId", cItem.id);
              that.$set(item, "goodsPromotionId", cItem.id);
              //specialFlag是否特价，1是 0否
              if (cItem.specialFlag == 1) {
                lastPrice =
                  onWayFlag == 0
                    ? item.zaiKuCount * cItem.specialPrice +
                      item.zaiTuCount * item.salePrice
                    : (item.zaiKuCount + cItem.specialPrice) *
                      cItem.specialPrice;
                //  活动累计情况下，相同的货品都按特价计算
                if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
                  let childrenList = goodsItems;
                  for (let i = 0; i < childrenList.length; i++) {
                    if (
                      childrenList[i].id != item.id &&
                      childrenList[i].goodsPromotionId ==
                        item.goodsPromotionId &&
                      childrenList[i].itemCount > 0
                    ) {
                      let childrenTotalPrice =
                        onWayFlag == 0
                          ? childrenList[i].zaiKuCount * cItem.specialPrice +
                            childrenList[i].zaiTuCount *
                              childrenList[i].salePrice
                          : childrenList[i].itemCount * cItem.specialPrice;
                      that.$set(
                        childrenList[i],
                        "lastPrice",
                        childrenTotalPrice
                      );
                      that.$set(childrenList[i], "conditionsId", cItem.id);
                    }
                  }
                }
              } else {
                //不是特价
                //reduceOrPresent 促销统计方式：1满减 2满赠
                if (cItem.reduceOrPresent == 1) {
                  // reduceType 满减类型， 2折扣  1减免
                  if (cItem.reduceType == 2) {
                    let zheKouPrice = (
                      (item.salePrice * cItem.discount) /
                      100
                    ).toFixed(2);
                    lastPrice =
                      onWayFlag == 0
                        ? item.zaiKuCount * zheKouPrice +
                          item.zaiTuCount * item.salePrice
                        : (item.zaiKuCount + item.zaiTuCount) * zheKouPrice;
                    //  活动累计情况下，相同的货品都按减免计算
                    if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
                      let childrenList = goodsItems;
                      for (let i = 0; i < childrenList.length; i++) {
                        if (
                          childrenList[i].id != item.id &&
                          childrenList[i].goodsPromotionId ==
                            item.goodsPromotionId &&
                          childrenList[i].itemCount > 0
                        ) {
                          let zheKouPrice2 = (
                            (childrenList[i].salePrice * cItem.discount) /
                            100
                          ).toFixed(2);
                          let childrenTotalPrice =
                            onWayFlag == 0
                              ? childrenList[i].zaiKuCount * zheKouPrice2 +
                                childrenList[i].zaiTuCount *
                                  childrenList[i].salePrice
                              : childrenList[i].itemCount * zheKouPrice2;
                          that.$set(
                            childrenList[i],
                            "lastPrice",
                            childrenTotalPrice
                          );
                          that.$set(childrenList[i], "conditionsId", cItem.id);
                        }
                      }
                    }
                  } else {
                    //  活动累计情况下，勾选上的货品都按减免计算
                    if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
                      let childrenList = that.goodsItems;
                      for (let i = 0; i < childrenList.length; i++) {
                        if (
                          childrenList[i].goodsPromotionId ==
                            item.goodsPromotionId &&
                          childrenList[i].itemCount > 0
                        ) {
                          let reduction2 = cItem.reduction;
                          if (cItem.reductionPresentAddFlag == 1) {
                            reduction2 =
                              ruleItem.moneyOrCount == 1
                                ? onWayFlag == 0
                                  ? childrenList[i].zaiKuCount *
                                    childrenList[i].salePrice *
                                    (cItem.reduction / cItem.oneBuyMoney)
                                  : childrenList[i].itemCount *
                                    childrenList[i].salePrice *
                                    (cItem.reduction / cItem.oneBuyMoney)
                                : onWayFlag == 0
                                ? childrenList[i].zaiKuCount *
                                  (cItem.reduction / cItem.oneBuyCount)
                                : childrenList[i].itemCount *
                                  (cItem.reduction / cItem.oneBuyCount);
                          }
                          let childrenTotalPrice = (
                            childrenList[i].totalPrice - reduction2
                          ).toFixed(2);
                          if (childrenList[i].id == item.id) {
                            lastPrice = childrenTotalPrice;
                          }
                          that.$set(
                            childrenList[i],
                            "lastPrice",
                            childrenTotalPrice
                          );
                          that.$set(childrenList[i], "conditionsId", cItem.id);
                        }
                      }
                    } else {
                      let reduction = cItem.reduction;
                      // 判断是否叠加的减免金额
                      if (cItem.reductionPresentAddFlag == 1) {
                        reduction =
                          ruleItem.moneyOrCount == 1
                            ? onWayFlag == 0
                              ? zaiKuCount *
                                item.salePrice *
                                (cItem.reduction / cItem.oneBuyMoney)
                              : item.itemCount *
                                item.salePrice *
                                (cItem.reduction / cItem.oneBuyMoney)
                            : onWayFlag == 0
                            ? zaiKuCount * (cItem.reduction / cItem.oneBuyCount)
                            : item.itemCount *
                              (cItem.reduction / cItem.oneBuyCount);
                      }
                      lastPrice = (
                        goodNum * item.salePrice -
                        reduction
                      ).toFixed(2);
                    }
                  }
                }
              }
            }
          }
        });
      }
      console.log("二次价格：", lastPrice);
      that.$set(
        goodsItems,
        "totalPrice",
        goodsItems.totalPrice - item.lastPrice + lastPrice
      );
      that.$set(item, "lastPrice", lastPrice);

      // 计算价格-end
      let list = JSON.parse(JSON.stringify(that.goodsList));
      // 计算价格-end
      that.shopTotalNum = 0;
      that.totalPrice = 0;
      list.forEach((item) => {
        let totalNum = 0;
        item.goodsItems.forEach((items) => {
          if (items.isCheck) {
            that.shopTotalNum += items.buyNum * items.boxNum;
            that.totalPrice += Number(items.lastPrice);
          }
          totalNum += items.buyNum * items.boxNum;
        });
        console.log(totalNum);
        that.$set(item, "totalNum", totalNum);
      });

      console.log("加减商品列表：", that.goodsList);
    },

    // 快速下单移动
    swipeChange(e, id, itemsTd) {
      console.log(e);
    },
    // 快速下单选中要购买的产品--一级--Y
    checkOneFun(item) {
      let that = this;
      this.$set(item, "isCheck", !item.isCheck);
      item.goodsItems.forEach((items) => {
        that.$set(items, "isCheck", item.isCheck);
      });
      this.shopTotalNum = 0;
      this.totalPrice = 0;
      let flag = true;
      this.allCheck = false;
      this.goodsList.forEach((item) => {
        item.goodsItems.forEach((item3) => {
          if (item3.isCheck) {
            that.shopTotalNum += Number(item3.buyNum) * item3.boxNum;
            that.totalPrice += Number(item3.lastPrice);
          } else {
            flag = false;
          }
        });
      });
      this.allCheck = flag;
    },

    // 快速下单选中要购买的产品--二级--Y
    checkTwoFun(items) {
      let that = this;
      this.shopTotalNum = 0;
      this.totalPrice = 0;
      this.$set(items, "isCheck", !items.isCheck);
      console.log(this.goodsList);
      let flagB = true;
      this.goodsList.forEach((item) => {
        let flag = true;
        item.goodsItems.forEach((item3) => {
          if (item3.isCheck) {
            that.shopTotalNum += Number(item3.buyNum) * item3.boxNum;
            that.totalPrice += Number(item3.lastPrice);
          }
          if (!item3.isCheck) {
            flag = false;
            flagB = false;
          }
        });
        this.$set(item, "isCheck", flag);
      });
      this.allCheck = flagB;
    },

    // 快速下单点击删除
    swipeClick(itemsTd) {
      let that = this;
      this.shopTotalNum = 0;
      this.totalPrice = 0;
      let list = JSON.parse(JSON.stringify(this.goodsList));
      let newArray = [];
      // 过滤掉goodsItems数量为0的
      list.forEach((item, index) => {
        newArray[index] = item;
        newArray[index].goodsItems = item.goodsItems.filter((items) => {
          return items.id != itemsTd;
        });
      });
      list = list.filter((item) => {
        return item.goodsItems.length > 0;
      }); //过滤goodsItems长度为0的值
      list.forEach((item) => {
        item.goodsItems.forEach((items) => {
          if (items.isCheck) {
            that.shopTotalNum += Number(items.buyNum) * items.boxNum; //共几件
            that.totalPrice += Number(items.lastPrice); //共计金额
          }
        });
      });

      this.goodsList = list;
    },

    // 下单
    toConfirmOrder() {
      uni.navigateTo({
        url: "confirmOrder?id=1",
      });
    },

    // 加入购物车
    addShopCart() {
      let that = this;
      let usertId = uni.getStorageSync("userId");
      let params = {
        distributorId: usertId,
        itemCount: "",
        itemId: "",
      };
      console.log("加入购物车原货品列表：", this.goodsItems);
      let paramsList = [];
      let list = this.goodsList;
      for (let i = 0; i < list.length; i++) {
        for (let j = 0; j < list[i].goodsItems.length; j++) {
          if (list[i].goodsItems[j].isCheck) {
            let objItem = {
              barCode: list[i].goodsItems[j].barCode, //货品条码
              colorName: list[i].goodsItems[j].colorName, //货品颜色
              diyType: list[i].goodsItems[j].diyType, //定制类型 0-标准定制 1-DIY定制
              goodsId: list[i].goodsItems[j].goodsId, //商品id
              goodsName: list[i].goodsItems[j].goodsName, //商品名称
              goodsNo: list[i].goodsItems[j].goodsNo, //商品编码
              goodsPromotionId: list[i].goodsItems[j].conditionsId
                ? list[i].goodsItems[j].conditionsId
                : "", //活动条件id
              goodsType: list[i].goodsItems[j].goodsType, //商品类型 1-普通 2-定制
              groupSeckillId: list[i].goodsItems[j].groupId, //拼团秒杀活动id
              height: list[i].goodsItems[j].height,
              imageUrl: list[i].goodsItems[j].itemImg,
              itemCode: list[i].goodsItems[j].itemCode, //货品编码
              itemCount: list[i].goodsItems[j].itemCount,
              itemId: list[i].goodsItems[j].id,
              itemName: list[i].goodsItems[j].itemName,
              itemType: 1, //是否赠品 1 非赠品 2 赠品
              length: list[i].goodsItems[j].length,
              specsName: list[i].goodsItems[j].specsName, //货品规格
              weight: list[i].goodsItems[j].weight,
              width: list[i].goodsItems[j].width,
            };
            paramsList.push(objItem);
          }
        }
      }
      if (paramsList.length < 1) {
        this.tipFun("请选择要加入购物车成功！");
        return;
      }
      addShoppingcart(paramsList).then((res) => {
        if (res.success) {
          that.tipFun("加入购物车成功！");
          uni.switchTab({
            url: "/pages/shoppingCart/shoppingCart",
          });
        } else {
          that.tipFun(res.errMessage);
        }
      });
    },
    // 购买
    buyClick() {
      console.log("购买的商品列表：", this.goodsItems);
      let that = this;
      let isAllType = false; //货品在库在途是否同时存在
      let zaiKuAllType = false;
      let zaiTuAllType = false;
      let list = this.goodsList;
      let orderList = [];
      for (let i = 0; i < list.length; i++) {
        for (let j = 0; j < list[i].goodsItems.length; j++) {
          if (list[i].goodsItems[j].isCheck) {
            if (list[i].goodsItems[j].zaiKuCount > 0) {
              zaiKuAllType = true;
            }
            if (list[i].goodsItems[j].zaiTuCount > 0) {
              zaiTuAllType = true;
            }

            if (
              Number(list[i].goodsItems[j].buyNum) &&
              Number(list[i].goodsItems[j].buyNum) > 0
            ) {
              this.$set(
                list[i].goodsItems[j],
                "imageUrl",
                list[i].goodsItems[j].itemImg
              );

              this.$set(
                list[i].goodsItems[j],
                "totalPrice",
                list[i].goodsItems[j].lastPrice
              );
              this.$set(list[i].goodsItems[j], "itemType", 1); //是否赠品 1 非赠品 2 赠品
              if (
                Number(list[i].goodsItems[j].buyNum) <=
                list[i].goodsItems[j].inStockUsableCount
              ) {
                this.$set(
                  list[i].goodsItems[j],
                  "zaiKuCount",
                  Number(list[i].goodsItems[j].buyNum)
                );
                this.$set(list[i].goodsItems[j], "zaiTuCount", 0);
              } else {
                this.$set(
                  list[i].goodsItems[j],
                  "zaiKuCount",
                  list[i].goodsItems[j].inStockUsableCount
                );
                this.$set(
                  list[i].goodsItems[j],
                  "zaiTuCount",
                  Number(list[i].goodsItems[j].buyNum) -
                    list[i].goodsItems[j].inStockUsableCount
                );
              }
              orderList.push(list[i].goodsItems[j]);
            }
          }
        }
      }
      isAllType = zaiKuAllType & zaiTuAllType ? true : false;
      if (orderList.length == 0) {
        that.tipFun("请选择至少一个货品！");
      } else {
        uni.setStorageSync("shopOrderList", JSON.stringify(orderList));
        uni.navigateTo({
          url: "/pages/shoppingCart/confirmOrder?isTwoWay=" + isAllType,
        });
      }
    },

    //进入商品详情
    toGoodsDetails(item) {
      this.zongHeShow = false;
      uni.navigateTo({
        url: "/pages/classify/goodsDetails?id=" + item.id,
      });
    },

    // 全选
    allCheckFun() {
      let that = this;
      let totalNum = 0;
      let totalPrice = 0;
      that.shopTotalNum = 0;
      that.totalPrice = 0;
      that.allCheck = !that.allCheck;
      that.goodsList.forEach((item) => {
        that.$set(item, "isCheck", that.allCheck);
        if (that.allCheck) {
          totalNum += item.totalNum;
          totalPrice += item.totalPrice;
        }
        item.goodsItems.forEach((items) => {
          that.$set(items, "isCheck", that.allCheck);
        });
      });
      that.shopTotalNum = totalNum;
      that.totalPrice = totalPrice;
    },
  },
};
</script>

<style lang="scss">
.uni-drawer {
  z-index: 9999 !important;
}
.typePopup-oneHover {
  color: $base-color1 !important;
}
.ksListOrder {
  font-size: 26rpx;
  font-family: PingFangSC-Medium, PingFang SC;
  .status_bar {
    position: fixed;
    top: 0;
    left: 0;
    background: #fff;
    z-index: 10;
  }
  .ks-top {
    position: fixed;
    top: var(--status-bar-height);
    width: 100%;
    z-index: 999;
    background: #fff;
    text-align: center;
    height: 84rpx;
    line-height: 104rpx;
    padding-top: 25rpx;
    padding-bottom: 12rpx;
    // #ifdef H5
    top: 0;
    height: 64rpx;
    line-height: 64rpx;
    padding-bottom: 25rpx;
    // #endif
    .ks-top-nav {
      position: absolute;
      left: 22rpx;
      display: flex;
      align-items: center;
      border-radius: 50rpx;
      width: 128rpx;
      height: 64rpx;
      line-height: 64rpx;
      margin-top: 20rpx;
      // #ifdef H5
      margin-top: 0;
      // #endif
      border: 1rpx solid #979797;
      padding: 0 24rpx;
      image {
        width: 46rpx;
        height: 44rpx;
      }
      text {
        color: #f3f4f8;
        font-size: 36rpx;
        margin: 0 20rpx;
      }
    }
    .ks-top-title {
      font-size: 34rpx;
    }
  }
  // 导航
  .ksList-nav {
    position: fixed;
    background: #fff;
    box-shadow: 0px 4rpx 8rpx 0px rgba(0, 0, 0, 0.23);
    top: 180rpx;
    // #ifdef H5
    top: 120rpx;
    // #endif
    left: 40rpx;
    z-index: 9999;
    .ksList-navTrangle {
      position: absolute;
      top: -15rpx;
      left: 100rpx;
      width: 0;
      height: 0;
      border-left: 18rpx solid transparent;
      border-right: 18rpx solid transparent;
      border-bottom: 18rpx solid #fff;
    }
    .ksList-navTb {
      .ksList-nav-line {
        padding: 16rpx 24rpx;
        display: flex;
        align-items: center;
        image {
          width: 60rpx;
          height: 60rpx;
        }
        text {
          font-size: 24rpx;
          margin-left: 8rpx;
        }
      }
    }
  }
  // 列表
  .ksList-listView {
    padding: 170rpx 30rpx 310rpx;
    // #ifdef H5
    padding-top: 114rpx;
    // #endif

    .ks-listLine {
      background: #ffffff;
      border-radius: 16rpx;
      margin-top: 24rpx;
      .ks-listGoodName {
        display: flex;
        align-items: center;
        padding: 24rpx 30rpx;
        color: #333;
        border-bottom: 1rpx solid #f3f4f8;
        text {
          font-size: 40rpx;
          margin-right: 16rpx;
        }
        .ks-listGoodNameRg {
          display: flex;
          align-items: center;
          justify-content: space-between;
          view {
            width: 525rpx;
            font-size: 28rpx;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
          image {
            width: 40rpx;
            height: 40rpx;
          }
        }
      }
      // 货品
      .ks-listItem {
        font-family: PingFangSC-Regular, PingFang SC;
        margin: 24rpx 30rpx;
        border-bottom: 1rpx solid #f3f4f8;
        padding-bottom: 24rpx;
        .ks-listItem-info {
          display: flex;
          align-items: center;

          .ks-listItem-infoItemImg {
            width: 120rpx;
            height: 124rpx;
            border-radius: 8rpx;
            background: rgba(243, 244, 248, 0.5);
          }
          .ks-listItem-infoCenter {
            font-size: 24rpx;
            margin-left: 24rpx;
            width: 380rpx;
            position: relative;
            .ks-listItem-infoLine {
              display: flex;
              align-items: center;
              text:nth-child(1) {
                color: #999999;
              }
              text:nth-child(2) {
                color: #333;
              }
            }
            .ks-listItem-infoLine:nth-child(2),
            .ks-listItem-infoLine:nth-child(3) {
              margin-top: 10rpx;
            }
            .ks-listItem-infoLineTip {
              margin-top: 18rpx;
              text {
                border: 1rpx solid #f94021;
                border-radius: 2rpx;
                padding: 2rpx 6rpx;
                font-size: 16rpx;
                color: #f94021;
                margin-right: 10rpx;
              }
            }
            image {
              width: 34rpx;
              height: 36rpx;
              margin-left: 5rpx;
            }
            .ks-listItem-boxsView {
              position: absolute;
              z-index: 999;
              top: 140rpx;
              .ks-listItem-boxs {
                width: 386rpx;
                background: #ffffff;
                box-shadow: 0px 4rpx 20rpx 0px #e0e3ee;
                position: relative;

                .ks-listItem-boxsTrangle {
                  position: absolute;
                  top: -10rpx;
                  left: 170rpx;
                  width: 0;
                  height: 0;
                  border-left: 15rpx solid transparent;
                  border-right: 15rpx solid transparent;
                  border-bottom: 15rpx solid #fff;
                }
                .ks-listItem-boxsTr {
                  height: 82rpx;
                  line-height: 82rpx;
                  color: #999;
                  font-size: 24rpx;
                  display: flex;
                  text-align: center;
                  text:nth-child(1) {
                    width: 230rpx;
                  }
                  text:nth-child(2) {
                    width: 156rpx;
                  }
                }
                .ks-listItem-boxsTd {
                  height: 82rpx;
                  line-height: 82rpx;
                  font-size: 24rpx;
                  display: flex;
                  text-align: center;
                  border-top: 2rpx solid #f3f4f8;
                  text:nth-child(1) {
                    width: 230rpx;
                  }
                  text:nth-child(2) {
                    width: 156rpx;
                  }
                }
              }
            }
          }
          .ks-listItem-infoPrice {
            width: 100rpx;
            text-align: right;
            font-size: 30rpx;
            font-family: ArialMT;
            color: #000000;
            margin-top: -70rpx;
          }
        }
        .ks-listItem-numItem {
          display: flex;
          align-items: center;
          justify-content: space-between;
          margin-top: 20rpx;
          position: relative;
          .ks-listItem-numItem {
            font-size: 24rpx;
            text:nth-child(2) {
              margin-left: 34rpx;
            }
          }
          .ks-listItem-KuCunBox {
            position: absolute;
            left: 410rpx;
            bottom: 80rpx;
            .ks-listItem-KuCun {
              position: relative;
              background: #fff;
              box-shadow: 0px 4rpx 20rpx 0px #e0e3ee;
              width: 204rpx;
              height: 108rpx;
              line-height: 108rpx;
              text-align: center;
              .ks-listItem-KuCunTrangle {
                position: absolute;
                bottom: -10rpx;
                left: 90rpx;
                width: 0;
                height: 0;
                border-left: 15rpx solid transparent;
                border-right: 15rpx solid transparent;
                border-top: 15rpx solid #fff;
              }
              .ks-listItem-KuCunText {
                color: #f94021;
                font-size: 24rpx;
              }
            }
          }
        }

        .ks-listItem-tip {
          color: #f94021;
          font-size: 20rpx;
          margin-top: 20rpx;
        }
      }
      .ks-listItem-noBorder {
        border: none !important;
      }

      // 查看更多
      .ks-listItem-moreCheck {
        border-top: 1rpx solid #f3f4f8;
        color: #333;
        font-size: 28rpx;
        text-align: center;
        padding: 24rpx 0;
      }
      .ks-listItem-totalItem {
        border-top: 1rpx solid #f3f4f8;
        padding: 30rpx;
        display: flex;
        align-items: center;
        justify-content: space-between;
        .ks-totalItem-num {
          font-size: 20rpx;
        }
        .ks-totalItem-priceItem {
          .ks-totalItem-priceActivity {
            font-size: 20rpx;
            color: #f94021;
            margin-right: 20rpx;
          }
          .ks-totalItem-pricetext {
            font-size: 24rpx;
          }
          .ks-totalItem-priceAll {
            font-size: 28rpx;
            color: #f94021;
          }
        }
      }
    }
  }
  // 计算器
  .ks-calculator {
    border: 2rpx solid #c2c2c2;
    display: flex;
    align-items: center;
    width: 232rpx;
    font-size: 0;
    text {
      width: 50rpx;
      height: 50rpx;
      line-height: 50rpx;
      font-size: 36rpx;
      color: #a6a6a6;
      text-align: center;
    }
    input {
      width: 130rpx;
      height: 50rpx;
      border-left: 2rpx solid #c2c2c2;
      border-right: 2rpx solid #c2c2c2;
      font-size: 32rpx;
      color: #333;
      text-align: center;
    }
  }

  // 商品信息弹框
  .ks-goodsInfo-popup {
    .ks-goodsInfo-popup-content {
      background: #fff;
      width: 622rpx;
      height: 426rpx;
      border-radius: 16rpx;
      .ks-goodsInfo-popup-box {
        display: flex;
        margin-left: 62rpx;
        padding-top: 78rpx;
        image {
          width: 160rpx;
          height: 160rpx;
          border-radius: 8rpx;
          background: #f3f4f8;
        }
        view {
          margin-left: 14rpx;
          font-size: 24rpx;
          margin-bottom: 28rpx;
          text:nth-child(1) {
            color: #999999;
          }
        }
      }
      .ks-goodsInfo-popup-btn {
        margin-top: 30rpx;
        padding: 32rpx 0;
        text-align: center;
        font-size: 32rpx;
        border-top: 1rpx solid #f3f4f8;
      }
    }
  }
  .ks-iconfont {
    font-size: 40rpx;
    margin-right: 20rpx;
  }
  // 底部
  .ksList-btm {
    background: #fff;
    position: fixed;
    left: 0;
    bottom: 0;
    width: 100%;
    padding-bottom: 50rpx;
    z-index: 999;
    .ksList-btm-top {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 14rpx 30rpx;
      .ksList-btm-topLf {
        display: flex;
        align-items: center;
        font-size: 28rpx;
        text:nth-child(1) {
          margin-right: 12rpx !important ;
        }
      }
      .ksList-btm-topRg {
        font-size: 24rpx;
      }
    }
    .ksList-btm-price {
      text-align: right;
      padding-right: 30rpx;
      .ksList-btm-priceDiscounts {
        font-size: 24rpx;
        color: #f94021;
        margin-right: 30rpx;
      }
      .ksList-btm-priceTotalText {
        font-size: 24rpx;
      }
      .ksList-btm-priceNum {
        font-size: 40rpx;
        color: #f94021;
        font-family: Arial-BoldMT, Arial;
        font-weight: normal;
        color: #f94021;
      }
    }
    .ksList-btm-btn {
      display: flex;
      align-items: center;
      justify-content: flex-end;
      padding-right: 30rpx;
      padding-top: 26rpx;
      text {
        display: block;
        width: 192rpx;
        height: 80rpx;
        line-height: 80rpx;
        font-size: 24rpx;
        text-align: center;
      }
      text:nth-child(1) {
        border-radius: 200rpx 0px 0px 200rpx;
      }
      text:nth-child(2) {
        border-radius: 0px 200rpx 200rpx 0px;
        color: #ffff;
      }
    }
    .ksList-btm-line {
      padding-bottom: 50rpx;
    }
  }
}
</style>
