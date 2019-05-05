<template>
  <uni-popup ref="popup" type="bottom" class="myCollect-popup">
    <view class="myCollect-popup-content">
      <view class="myCollect-popup-title">
        <text></text>
        <view class="goodItem-close" @click="closePopup">
          <image
            src="../../static/imgs/icon_close.png"
            class="popup-colose"
          ></image>
        </view>
      </view>
      <view class="myCollect-popup-inform">
        <image :src="goodsInfos.itemImg"></image>
        <view>
          <text>编码：{{ goodsInfos.itemCode }}</text>
          <text>条形码：{{ goodsInfos.barCode }}</text>
          <text>建议零售价：￥{{ goodsInfos.retailPrice }}</text>
        </view>
      </view>
      <view class="myCollect-popup-label">
        <!-- <view class="myCollect-fuwufei"><text>服务费：￥5元/片</text></view> -->
        <view class="myCollect-label-btm">
          <view class="myCollect-zaitu" v-if="onWayFlag == 1">
            <text
              class="iconfont"
              :class="
                isInTransit ? 'tuTypeHover icon-Checkthe' : 'icon-uncheck'
              "
              @click="choiceTutype()"
            ></text>
            <text>包含在途库存</text>
          </view>
          <view
            class="myCollect-buyDanwei"
            v-if="goodsInfos.boxs"
            @click="guiGeShow = !guiGeShow"
          >
            购买单位：{{ goodsInfos.boxNum }}件/{{ goodsInfos.boxType }}
            <text class="iconfont icon-Packup Message-boxUp"></text>
          </view>
          <!-- 选择规格 -->
          <view class="self-Message-box" v-if="guiGeShow">
            <view class="Message-box-content">
              <view class="Message-box-list">
                <text
                  v-for="(item, bIndex) in goodsInfos.boxs"
                  :key="bIndex"
                  @click="selectBoxsFun(item)"
                  >{{ item.boxType }}：{{ item.boxNum }}件
                </text>
              </view>
              <!-- 小三角 -->
              <view class="Message-box-sanjao"></view>
            </view>
          </view>
        </view>
      </view>
      <scroll-view
        scroll-y="true"
        class="myCollect-scroll-Y"
        :style="{
          height: num == 1 ? (isH5s == true ? '528rpx' : '528rpx') : '600rpx',
        }"
      >
        <view class="myCollect-categoryList">
          <view
            v-for="(item, index) in goodsItems2"
            :key="item.id"
            class="myCollect-categoryList-line"
            :class="goodsItemsIndex == index ? 'categoryList-Hover' : ''"
            @click="goodsItemsClick(index, item)"
          >
            <view class="myCollect-categoryLine-Lf">
              <text>颜色：{{ item.colorName ? item.colorName : "" }}</text>
              <text v-if="item.specsName && item.specsName != ''"
                >规格：{{ item.specsName }}
              </text>
            </view>
            <view class="myCollect-categoryLine-center">
              <text>￥</text>{{ item.salePrice }}
            </view>
            <view class="myCollect-categoryLine-RG">
              <!-- <uni-number-box @change="numberChange($event,item)" :min='0' :max="isInTransit?(item.onWayUsableCount+item.inStockUsableCount):item.inStockUsableCount" v-model="item.buyNum"></uni-number-box> -->
              <view class="self-numberBox3">
                <text @click="numberChange(item.itemCount, item, 1)">-</text>
                <input
                  v-model="item.buyNum"
                  @blur="numberChange(item.itemCount, item, 3)"
                />
                <text @click="numberChange(item.itemCount, item, 2)">+</text>
              </view>
              <view class="categoryLine-RG-kucun" v-if="!isInTransit">
                库存：
                <text v-if="stockShowFlag == 0">{{
                  item.inStockUsableCount
                }}</text>
                <text
                  v-else-if="
                    stockShowFlag == 1 &&
                    stockShowNumber < item.inStockUsableCount
                  "
                  >{{ item.inStockUsableCount }}
                </text>
                <text
                  v-else-if="stockShowFlag == 1 && item.inStockUsableCount == 0"
                  >无货
                </text>
                <text
                  v-else-if="
                    stockShowFlag == 1 &&
                    0 < item.inStockUsableCount <= stockShowNumber
                  "
                  >库存紧张
                </text>
              </view>
              <view class="categoryLine-RG-kucun" v-if="isInTransit">
                库存：
                <text v-if="stockShowFlag === 0">
                  {{ item.onWayUsableCount + item.inStockUsableCount }}
                </text>
                <text
                  v-else-if="
                    stockShowFlag === 1 &&
                    stockShowNumber <
                      item.onWayUsableCount + item.inStockUsableCount
                  "
                >
                  {{ item.onWayUsableCount + item.inStockUsableCount }}
                </text>
                <text
                  v-else-if="
                    stockShowFlag === 1 &&
                    item.onWayUsableCount + item.inStockUsableCount === 0
                  "
                  >无货
                </text>
                <text
                  v-else-if="
                    stockShowFlag === 1 &&
                    0 <
                      item.onWayUsableCount + item.inStockUsableCount <=
                      stockShowNumber
                  "
                  >库存紧张
                </text>
              </view>
              <view
                class="myCollect-activityTip popup-activityTip"
                v-if="item.activityType == 1"
                >拼团
              </view>
              <view
                class="myCollect-activityTip popup-activityTip"
                v-if="item.activityType == 2"
                >秒杀
              </view>
              <view
                class="myCollect-activityTip popup-activityTip"
                v-if="item.activityType == 3"
                >活动
              </view>
            </view>
          </view>
        </view>
      </scroll-view>
      <!-- <view class="myCollect-popup-btm" :class="num==1?'classifyPopup-btm':''">
			   <view class="myCollect-popup-total">合计<text>{{totalNum}}</text>件，共计金额<text>￥{{Number(totalPrice).toFixed(2)}}</text></view>
			   <view class="myCollect-popup-btn">
				   <text class="add-shoppingCart" @click="addShopCart">加入购物车</text>
				   <text class="popup-buy" @click="buyClick">立即购买</text>
			   </view>
			</view> -->

      <!-- 底部按钮 -->
      <view
        class="qo-popup-btmFix"
        :class="num == 1 ? 'classifyPopup-btm' : ''"
      >
        <view class="qo-popup-btmFixLf">
          <view>
            合计<text>{{ totalNum }}</text
            >件
          </view>
          <view>
            共计金额<text>￥{{ totalPrice }}</text>
          </view>
        </view>
        <view class="qo-popup-btmFixRG">
          <text @click="addShopCart">加入购物车</text>
          <text @click="buyClick">立即购买</text>
        </view>
      </view>
    </view>
  </uni-popup>
</template>

<script>
import { addShoppingcart } from "../../common/api.js";
import { isH5, isMpWeixin } from "../../common/common.js";
export default {
  props: {
    goodsItems: {
      type: Array,
    },
    goodsInfo: {
      type: Object,
    },
    stockShowFlag: {
      // 0 实际库存 1模糊库存
      type: Number,
    },
    stockShowNumber: {
      // 库存临界值
      type: Number,
    },
    num: {},
    onWayFlags: {},
  },
  watch: {
    goodsInfo(newValue, oldValue) {
      this.goodsInfos = newValue;
    },
    onWayFlags(newValue) {
      this.onWayFlag = newValue;
    },
    goodsItems(newValue) {
      this.goodsItems2 = newValue;
    },
  },
  data() {
    return {
      isInTransit: false, //是否包含在途
      totalNum: 0, //共几件
      totalPrice: 0, //共计金额
      discountsSalePrice: 0, //优惠价格
      goodsItemsIndex: 0,
      guiGeShow: false,
      goodsInfos: {},
      goodsItems2: [],
      userId: "",
      capitalStatus: "",
      freezeStatus: "",
      onWayFlag: null,
      isH5s: true,
    };
  },
  onLoad() {
    console.log("组件货品：", this.goodsItems2);
    this.userId = uni.getStorageSync("userId"); //获取分销商id
    this.capitalStatus = uni.getStorageSync("capitalStatus");
    this.freezeStatus = uni.getStorageSync("freezeStatus");

    this.isH5s = isH5;
  },
  methods: {
    // 打开货品列表
    initialFun() {
      this.guiGeShow = false;
      this.$refs.popup.open();
    },
    // 关闭货品列表
    closePopup() {
      this.totalPrice = 0;
      this.totalNum = 0;
      this.guiGeShow = false;
      this.$refs.popup.close();
    },
    // 商品弹框选中单项查看信息
    goodsItemsClick(index, item) {
      this.goodsItemsIndex = index;
      this.goodsInfos = item;
      this.onWayFlag = item.sysOnWayFlag;
    },
    // 选择规格
    selectBoxsFun(item) {
      console.log("选择的规格：", item);
      this.guiGeShow = false;
      // totalNum:0,  //共几件
      // totalPrice:0,   //共计金额
      this.totalNum = 0;
      this.totalPrice = 0;
      this.goodsItems2.forEach((gItem) => {
        if (gItem.id == this.goodsInfo.id) {
          // 计算当前货品总购买数
          let buyTotalNum = Number(gItem.buyNum) * item.boxNum;
          //当前货品总库存--- （是否包含在途）
          let stockTotalNum = this.isInTransit
            ? gItem.inStockUsableCount + gItem.onWayUsableCount
            : gItem.inStockUsableCount;
          if (stockTotalNum >= buyTotalNum) {
            this.$set(this.goodsInfo, "boxType", item.boxType);
            this.$set(this.goodsInfo, "boxNum", item.boxNum);
            this.$set(gItem, "boxType", item.boxType);
            this.$set(gItem, "boxNum", item.boxNum);
            this.$set(gItem, "itemCount", buyTotalNum);
            if (gItem.inStockUsableCount >= buyTotalNum) {
              this.$set(gItem, "zaiKuCount", buyTotalNum);
              this.$set(gItem, "zaiTuCount", 0);
            } else {
              this.$set(gItem, "zaiKuCount", gItem.inStockUsableCount);
              this.$set(
                gItem,
                "zaiTuCount",
                buyTotalNum - gItem.inStockUsableCount
              );
            }
          } else {
            uni.showToast({ title: "库存不足", icon: "none" });
          }
        }

        //总件数
        this.totalNum += gItem.buyNum * gItem.boxNum;
        // 总价格--要去匹配活动计算价格
        let lastPrice = gItem.itemCount * gItem.salePrice;

        // 促销活动
        if (
          gItem.promotions &&
          gItem.promotions.length > 0 &&
          gItem.itemCount > 0
        ) {
          let rulesList = gItem.promotions[0].rules;
          let onWayFlag = gItem.promotions[0].onWayFlag; //在途商品是否参与活动 1.参与，0.不参与
          let promoStatus = gItem.promotions[0].promoStatus; //促销状态：0 未开始, 1 促销中 2 已过期 3 提前结束
          let promoType = gItem.promotions[0].promoType; //活动类型，1 普通活动，2 阶梯活动
          let ruleItem = rulesList[0]; //默认选中第一活动规则

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
            let childrenList = this.goodsItems2;
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
                zPrice +=
                  childrenList[i].zaiKuCount * childrenList[i].salePrice;
                tPrice +=
                  childrenList[i].zaiTuCount * childrenList[i].salePrice;
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
                (ruleItem.moneyOrCount == 2 &&
                  isOneBuyCount >= cItem.oneBuyCount)
              ) {
                this.$set(gItem, "rulesId", ruleItem.id);
                this.$set(gItem, "conditionsId", cItem.id);
                // specialFlag 是否特价，1是 0否
                if (cItem.specialFlag == 1) {
                  lastPrice =
                    onWayFlag == 0
                      ? gItem.zaiKuCount * cItem.specialPrice +
                        gItem.zaiTuCount * gItem.salePrice
                      : (gItem.zaiKuCount + cItem.zaiTuCount) *
                        cItem.specialPrice;
                  //  活动累计情况下，相同的货品都按特价计算
                  if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
                    let childrenList = this.goodsItems2;
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
                        this.$set(
                          childrenList[i],
                          "lastPrice",
                          childrenTotalPrice
                        );
                        this.$set(childrenList[i], "conditionsId", cItem.id);
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
                        let childrenList = this.goodsItems2;
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
                            this.$set(
                              childrenList[i],
                              "lastPrice",
                              childrenTotalPrice
                            );
                            this.$set(
                              childrenList[i],
                              "conditionsId",
                              cItem.id
                            );
                          }
                        }
                      }
                    } else {
                      let reduction = cItem.reduction;
                      // 判断是否叠加的减免金额
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
                      //  活动累计情况下，勾选上的货品都按减免计算
                      if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
                        let childrenList = this.goodsItems2;
                        for (let i = 0; i < childrenList.length; i++) {
                          if (
                            childrenList[i].id != gItem.id &&
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
                            this.$set(
                              childrenList[i],
                              "lastPrice",
                              childrenTotalPrice
                            );
                            this.$set(
                              childrenList[i],
                              "conditionsId",
                              cItem.id
                            );
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          });
        }
        //拼团活动
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
            this.$set(gItem, "groupId", groupItem.groupSeckillId);
            this.$set(gItem, "groupSeckillId", groupItem.groupSeckillId);
            lastPrice = gItem.itemCount * groupItem.groupSeckillPrice;
          } else {
            this.$set(gItem, "groupSeckillId", "");
          }
        }

        this.totalPrice += lastPrice;
        // 更新单个货品的最终价格
        this.$set(gItem, "lastPrice", lastPrice);
      });
    },

    // 是否选择在途
    choiceTutype() {
      this.isInTransit = !this.isInTransit;
    },

    // 计数器
    numberChange(oldCount, item, type) {
      let that = this;
      console.log("当前货品：", oldCount);
      if (type == 1 && oldCount == 0) {
        return;
      } //零不能减
      let itemNum =
        type == 3
          ? item.buyNum
          : type == 1
          ? parseInt(oldCount / item.boxNum) - 1
          : parseInt(oldCount / item.boxNum) + 1;
      console.log("数量：", itemNum);
      let oldItemNum = Number(itemNum) * item.boxNum;
      let otherTotalPrice = 0; //除去当前货品其他活动的总价
      let otherTotalCount = 0;
      let totalSalePrice = 0; //原总价

      this.totalPrice = 0;
      this.totalNum = 0;

      let goodList = this.goodsItems2;

      // 计算其他货品的价格
      for (let i = 0; i < goodList.length; i++) {
        if (item.id != goodList[i].id) {
          otherTotalPrice += goodList[i].lastPrice
            ? Number(goodList[i].lastPrice)
            : 0;
          otherTotalCount += Number(goodList[i].buyNum) * goodList[i].boxNum;
        }
        totalSalePrice +=
          Number(goodList[i].buyNum) *
          goodList[i].boxNum *
          goodList[i].salePrice;
      }

      let isHaveStockNum = true; //判断是否够库存
      // ------计算数量----start
      // 货品数量=计数器数 * 装箱规格数量
      let goodNum = Number(itemNum) * item.boxNum;

      // this.isInTransit 是否包含在途
      let goodStockNum = this.isInTransit
        ? item.inStockUsableCount + item.onWayUsableCount
        : item.inStockUsableCount;

      if (item.groupSeckillId && item.groupSeckillId != "") {
        item.groupSeckills.forEach((groupItem) => {
          if (groupItem.groupSeckillId == item.groupSeckillId) {
            if (
              goodNum >= groupItem.minNum &&
              (groupItem.mtoFlag == 1
                ? goodNum <= groupItem.maxNum - groupItem.realSum
                : true)
            ) {
              if (goodNum > goodStockNum) {
                // 是否支持超卖或预售 1、支持 0、不支持
                if (groupItem.mtoFlag == 1) {
                  if (
                    groupItem.maxNum &&
                    childItem.maxNum != 0 &&
                    numCount > groupItem.maxNum
                  ) {
                    that.$set(item, "itemCount", oldCount);
                    that.$set(item, "buyNum", parseInt(oldCount / item.boxNum));
                    let title =
                      "货品订购数量不能大于拼团限购量：" + groupItem.maxNum;
                    uni.showToast({ title: title, icon: "none" });
                    return;
                  }
                } else {
                  that.$set(item, "itemCount", oldCount);
                  that.$set(item, "buyNum", parseInt(oldCount / item.boxNum));
                  uni.showToast({
                    title: "购买数量不能大于库存rer!",
                    icon: "none",
                  });
                  return;
                }
              }
            } else {
              that.$set(item, "groupSeckillId", "");
            }
          }
        });
      } else {
        if (goodNum > goodStockNum || goodStockNum == 0) {
          that.$set(item, "itemCount", oldCount);
          that.$set(item, "buyNum", parseInt(oldCount / item.boxNum));
          uni.showToast({ title: "购买数量不能大于库存!", icon: "none" });
          return;
        }
      }
      this.$set(item, "buyNum", itemNum);
      this.$set(item, "itemCount", goodNum);
      this.$set(
        item,
        "zaiKuCount",
        goodNum <= item.inStockUsableCount ? goodNum : item.inStockUsableCount
      );
      this.$set(
        item,
        "zaiTuCount",
        goodNum <= item.inStockUsableCount
          ? 0
          : goodNum - item.inStockUsableCount
      );
      this.totalNum = otherTotalCount + goodNum;
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
          this.$set(item, "groupId", groupItem.groupSeckillId);
          this.$set(item, "groupSeckillId", groupItem.groupSeckillId);
          lastPrice = goodNum * groupItem.groupSeckillPrice;
        } else {
          this.$set(item, "groupSeckillId", "");
        }
      } else if (item.activityType == 3) {
        console.log("88---", item);
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
          let childrenList = this.goodsItems2;
          let zCoun = zaiKuCount;
          let tCount = zaiTuCount;
          let zPrice = zaiKuCount * item.salePrice;
          let tPrice = zaiTuCount * item.salePrice;
          for (let i = 0; i < childrenList.length; i++) {
            // 判断同个活动累计数量
            if (
              childrenList[i].id != item.id &&
              childrenList[i].goodsPromotionId == item.goodsPromotionId &&
              childrenList[i].buyNum > 0
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
              this.$set(item, "rulesId", ruleItem.id);
              this.$set(item, "conditionsId", cItem.id);
              this.$set(item, "goodsPromotionId", cItem.id);
              //specialFlag是否特价，1是 0否
              if (cItem.specialFlag == 1) {
                lastPrice =
                  onWayFlag == 0
                    ? item.zaiKuCount * cItem.specialPrice +
                      item.zaiTuCount * item.salePrice
                    : (item.zaiKuCount + cItem.zaiTuCount) * cItem.specialPrice;
                //  活动累计情况下，相同的货品都按特价计算
                if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
                  let childrenList = this.goodsItems2;
                  for (let i = 0; i < childrenList.length; i++) {
                    if (
                      childrenList[i].id != item.id &&
                      childrenList[i].goodsPromotionId ==
                        item.goodsPromotionId &&
                      childrenList[i].buyNum > 0
                    ) {
                      let childrenTotalPrice =
                        onWayFlag == 0
                          ? childrenList[i].zaiKuCount * cItem.specialPrice +
                            childrenList[i].zaiTuCount *
                              childrenList[i].salePrice
                          : childrenList[i].itemCount * cItem.specialPrice;
                      this.$set(
                        childrenList[i],
                        "lastPrice",
                        childrenTotalPrice
                      );
                      this.$set(childrenList[i], "conditionsId", cItem.id);
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
                      let childrenList = this.goodsItems2;
                      for (let i = 0; i < childrenList.length; i++) {
                        if (
                          childrenList[i].id != item.id &&
                          childrenList[i].goodsPromotionId ==
                            item.goodsPromotionId &&
                          childrenList[i].buyNum > 0
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
                          this.$set(
                            childrenList[i],
                            "lastPrice",
                            childrenTotalPrice
                          );
                          this.$set(childrenList[i], "conditionsId", cItem.id);
                        }
                      }
                    }
                  } else {
                    //  活动累计情况下，勾选上的货品都按减免计算
                    if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
                      let childrenList = this.goodsItems2;
                      for (let i = 0; i < childrenList.length; i++) {
                        if (
                          childrenList[i].goodsPromotionId ==
                            item.goodsPromotionId &&
                          childrenList[i].buyNum > 0
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
                          } else {
                            reduction2 =
                              (reduction2 / isOneBuyCount) *
                              childrenList[i].itemCount;
                          }
                          console.log("减免金额：--", reduction2);
                          let childrenTotalPrice = (
                            childrenList[i].salePrice *
                              childrenList[i].itemCount -
                            reduction2
                          ).toFixed(2);
                          console.log("sdskdjsdksjd---", childrenTotalPrice);
                          if (childrenList[i].id == item.id) {
                            lastPrice = childrenTotalPrice;
                          }
                          this.$set(
                            childrenList[i],
                            "lastPrice",
                            childrenTotalPrice
                          );
                          this.$set(childrenList[i], "conditionsId", cItem.id);
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

                      if (ruleItem.ruleType == 1) {
                        let orserIndex = 0;
                        this.goodsItems2.forEach((orderItem, orserIndex) => {
                          console.log(orserIndex);
                          if (
                            orderItem.itemId != item.itemId &&
                            orderItem.goodsPromotionId == item.goodsPromotionId
                          ) {
                            this.$set(
                              orderItem,
                              "lastPrice",
                              Number(orderItem.salePrice * orderItem.itemCount)
                            );
                          }
                        });
                      }
                    }
                  }
                }
              }
            }
          }
        });
      }
      console.log("当前货品价格", lastPrice);
      this.totalPrice = Number(lastPrice) + Number(otherTotalPrice);
      this.discountsSalePrice = (
        Number(totalSalePrice) - this.totalPrice
      ).toFixed(2);
      this.$set(item, "lastPrice", Number(lastPrice));
      // 普通活动-end
    },

    // 加入购物车
    addShopCart() {
      let usertId = uni.getStorageSync("userId");
      let params = {
        distributorId: usertId,
        itemCount: "",
        itemId: "",
      };
      console.log("加入购物车原货品列表：", this.goodsItems2);
      let paramsList = [];
      let list = this.goodsItems2;
      for (let i = 0; i < list.length; i++) {
        if (Number(list[i].buyNum) > 0) {
          let objItem = {
            barCode: list[i].barCode, //货品条码
            colorName: list[i].colorName, //货品颜色
            diyType: list[i].diyType, //定制类型 0-标准定制 1-DIY定制
            goodsId: list[i].goodsId, //商品id
            goodsName: list[i].goodsName, //商品名称
            goodsNo: list[i].goodsNo, //商品编码
            goodsPromotionId: list[i].conditionsId ? list[i].conditionsId : "", //活动条件id
            goodsType: list[i].goodsType, //商品类型 1-普通 2-定制
            groupSeckillId: list[i].groupId, //拼团秒杀活动id
            height: list[i].height,
            imageUrl: list[i].itemImg,
            itemCode: list[i].itemCode, //货品编码
            itemCount: list[i].itemCount,
            itemId: list[i].id,
            itemName: list[i].itemName,
            itemType: 1, //是否赠品 1 非赠品 2 赠品
            length: list[i].length,
            specsName: list[i].specsName, //货品规格
            weight: list[i].weight,
            width: list[i].width,
          };
          paramsList.push(objItem);
        }
      }
      if (paramsList.length < 1) {
        uni.showToast({ title: "请选择要加入购物车成功！", icon: "none" });
        return;
      }
      addShoppingcart(paramsList).then((res) => {
        if (res.success) {
          uni.showToast({ title: "加入购物车成功！", icon: "none" });
          this.$refs.popup.close();
        } else {
          uni.showToast({ title: res.errMessage, icon: "none" });
        }
      });
    },
    // 购买
    buyClick() {
      console.log("购买的商品列表：", this.goodsItems2);
      let list = this.goodsItems2;
      let isAllType = false; //货品在库在途是否同时存在
      let zaiKuAllType = false;
      let zaiTuAllType = false;

      list.forEach((item3) => {
        // onWayUsableCount判断是否缺货
        // if(item3.isCheck&&(item3.goodsType==1?(item3.onWayUsableCount>0):true)){
        if (item3.isCheck) {
          if (item3.zaiKuCount > 0) {
            zaiKuAllType = true;
          }
          if (item3.zaiTuCount > 0) {
            zaiTuAllType = true;
          }
        }
      });

      isAllType = zaiKuAllType & zaiTuAllType ? true : false;

      this.orderList = [];
      list.forEach((item) => {
        if (Number(item.buyNum) && Number(item.buyNum) > 0) {
          this.$set(item, "imageUrl", item.itemImg);
          // this.$set(item,'itemCount',Number(item.buyNum));
          this.$set(item, "totalPrice", item.lastPrice);
          this.$set(item, "itemType", 1); //是否赠品 1 非赠品 2 赠品
          if (Number(item.buyNum) <= item.inStockUsableCount) {
            this.$set(item, "zaiKuCount", Number(item.itemCount));
            this.$set(item, "zaiTuCount", 0);
          } else {
            this.$set(item, "zaiKuCount", item.inStockUsableCount);
            this.$set(
              item,
              "zaiTuCount",
              Number(item.itemCount) - item.inStockUsableCount
            );
          }
          this.orderList.push(item);
        }
      });
      if (this.orderList.length == 0) {
        uni.showToast({ title: "请选择至少一个货品！", icon: "none" });
      } else {
        uni.setStorageSync("shopOrderList", JSON.stringify(this.orderList));
        uni.navigateTo({
          url: "/pages/shoppingCart/confirmOrder?isTwoWay=" + isAllType,
        });
      }
    },
    // 进入详情
    toGoodsDetails(item) {
      uni.navigateTo({
        url: "goodsDetails?id=" + item.id,
      });
    },
  },
};
</script>

<style  lang="scss">
.goodItem-close {
  width: 50rpx;
  height: 50rpx;
  text-align: right;
}
.self-numberBox3 {
  display: flex;
  align-items: center;
  text:nth-child(1),
  text:nth-child(3) {
    display: block;
    font-size: 40rpx;
    color: #999;
    width: 50rpx;
    height: 45rpx;
    line-height: 45rpx;
    margin-left: 10rpx;
    background: #fafafc;
    text-align: center;
    border-radius: 5rpx;
  }
  input {
    width: 80rpx;
    height: 45rpx;
    line-height: 45rpx;
    background: #f2f3f8;
    border-radius: 10rpx;
    margin-left: 10rpx;
    text-align: center;
    font-size: 26rpx;
  }
}
.classifyPopup-btm {
  bottom: 0rpx !important;
  /* #ifdef H5 */
  bottom: 100rpx !important;
  /* #endif*/
}

.qo-popup-btmFix {
  position: fixed;
  bottom: 0;
  /* #ifdef H5 */
  bottom: 0rpx;
  /* #endif*/

  left: 0;
  width: 710rpx;
  padding: 20rpx;
  box-shadow: $border-colorShadow;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  z-index: 1000;
  .qo-popup-btmFixLf {
    color: #999;
    text {
      color: $base-color2;
    }

    text {
      padding: 0 5rpx;
    }
  }
  .qo-popup-btmFixRG {
    display: flex;
    align-items: center;
    text {
      display: block;
      width: 200rpx;
      height: 80rpx;
      line-height: 80rpx;
      text-align: center;
      font-size: 28rpx;
      border-radius: 40rpx;
    }
    text:nth-child(1) {
      border: 3rpx solid $base-color1;
      color: $base-color1;
    }
    text:nth-child(2) {
      background: $bg-gradient-btn;
      color: #fff;
      margin-left: 20rpx;
    }
  }
}
</style>
