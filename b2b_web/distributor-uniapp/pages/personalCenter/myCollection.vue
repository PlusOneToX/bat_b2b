<template>
  <view class="myCollect">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>我的收藏</view>
      </view>
    </view>

    <scroll-view
      scroll-y="true"
      class="myCollect-scroll"
      @scrolltolower="pageScrollClick"
    >
      <view class="myCollect-list">
        <view
          class="myCollect-listLine"
          v-for="item in collectList"
          :key="item.id"
        >
          <image
            :src="item.imageUrl1"
            class="myCollect-img"
            @click="toGoodsDetails(item)"
          ></image>
          <view class="myCollect-listLine-RG">
            <view class="myCollect-name">{{ item.goodsName }}</view>
            <view class="myCollect-label">
              <text
                class="orangeLabel"
                v-for="(tagItem, tagIndex) in item.tags"
                :key="tagIndex"
                >{{ tagItem.name }}
              </text>
              <!-- <text class="redLabel">标签2</text>
						<text class="greenLabel">标签3</text> -->
            </view>
            <view class="myCollect-price">
              <view>
                <text class="myCollect-priceLabel">￥</text>
                <text class="myCollect-priceNum"> {{ item.minPrice }}</text>
                -
                <text class="myCollect-priceLabel">￥</text>
                <text class="myCollect-priceNum">{{ item.maxPrice }}</text>
              </view>
              <!-- 购买 -->
              <!-- <text class="iconfont icon-purchased cartIconColor"  @click="open(item.id)"></text> -->
              <!-- 取消收藏 -->
              <text
                class="iconfont icon-icon3 collectSelcet"
                @click="calcelCollect(item.id)"
              ></text>
              <!-- <text class="iconfont icon-collect collectSelcet-no"></text> -->
            </view>
          </view>
          <view class="myCollect-activityTip" v-if="item.newFlag == 1"
            >新品
          </view>
          <view class="myCollect-activityTip" v-if="item.groupFlag == 1"
            >拼团
          </view>
          <view class="myCollect-activityTip" v-if="item.promotionFlag == 1"
            >促销
          </view>
          <view class="myCollect-activityTip" v-if="item.seckillFlag == 1"
            >秒杀
          </view>
        </view>
      </view>
    </scroll-view>

    <!-- 货品列表组件-->
    <goodsItem
      ref="goodItem"
      :goodsItems="goodsItems"
      :goodsInfo="goodsInfo"
      :stockShowFlag="stockShowFlag"
      :stockShowNumber="stockShowNumber"
      :onWayFlags="onWayFlags"
      :num="2"
    ></goodsItem>
  </view>
</template>

<script>
import {
  goodsCollection,
  userGoodsList,
  priceGoodsList,
  goodsDetails,
  userShopSetting,
  listStockByCondition,
  priceItemList,
  promotiongroupseckill,
} from "../../common/api.js";
import goodsItem from "../../components/myComponents/goodsItem.vue";
export default {
  components: {
    goodsItem,
  },
  data() {
    return {
      page: 1,
      size: 10,
      total: 0,
      collectList: [],
      detailsObj: {},
      isInTransit: false, //false:否 ，true：是  是否包含在途
      goodsItems: [],
      stockShowFlag: 0, // 0 实际库存 1模糊库存
      stockShowNumber: 0, // 库存临界值
      goodsInfo: {},
      goodsItemsIndex: 0,
      onWayFlags: "null",
    };
  },
  onLoad() {
    this.getData();
    this.userShopSettingFun();
  },
  methods: {
    //返回
    toback() {
      uni.navigateBack({
        delta: 1,
      });
    },

    // 获取收藏列表
    getData() {
      let that = this;
      let params = {
        page: this.page,
        size: this.size,
        collectionFlag: 1, //是否筛选收藏 0-否 1-是
      };
      userGoodsList(params).then((res) => {
        if (res.success) {
          that.total = res.data.pages;
          let list = res.data.list;
          let goodsIdList = [];
          that.collectList = [...that.collectList, ...list];
          list.forEach((item) => {
            goodsIdList.push(item.id);
          });
          if (goodsIdList.length > 0) {
            that.getPriceData(goodsIdList, 1);
          }
        }
      });
    },
    // 取消收藏
    calcelCollect(id) {
      console.log(id);
      let that = this;
      let methods = "DELETE";
      goodsCollection(methods, { id: id }).then((res) => {
        if (res.success) {
          uni.showToast({ title: "取消收藏成功", icon: "none" });
          that.collectList = [];
          that.getData();
        }
      });
    },

    // 根据ids查询价格
    getPriceData(goodsIdList) {
      let that = this;
      priceGoodsList({ goodsIds: goodsIdList }).then((res) => {
        if (res.success) {
          let list = [];
          list = JSON.parse(JSON.stringify(that.collectList));
          list.forEach((item) => {
            for (let i = 0; i < res.data.length; i++) {
              if (item.id === res.data[i].goodsId) {
                item.minPrice = res.data[i].minPrice;
                item.maxPrice = res.data[i].maxPrice;
              }
            }
          });
          that.collectList = list;
        }
      });
    },

    // 根据商品货品ids查询价格
    getPriceItemListFun(ids) {
      let that = this;
      let params = {
        goodsItemIds: ids,
      };
      priceItemList(params).then((res) => {
        if (res.success) {
          let list = res.data;
          that.goodsItems.forEach((item) => {
            list.forEach((items) => {
              if (items.itemId == item.id) {
                item.retailPrice = items.retailPrice;
                item.salePrice = items.salePrice;
              }
            });
          });
        }
      });
    },

    // 获取详情数据
    getDetails(id) {
      let that = this;
      this.totalNum = 0;
      this.totalPrice = 0;
      this.goodsInfo = {};
      this.goodsItems = [];
      goodsDetails({ id: id }).then((res) => {
        console.log("商品详情：", res.data);
        let detais = res.data;

        let goodsIdList = [];

        let twoGoodsIdList = [];

        detais.goodsItems.forEach((item, index) => {
          that.$set(item, "activityType", 0); //初始没有活动（ 1：拼团  2：秒杀  3：普通活动  ）
          that.$set(item, "itemCount", 0);
          item.buyNum = 0;
          goodsIdList.push(item.id);
          item.boxType = "件";
          item.boxNum = 1;
          item.inStockUsableCount = 0; // 在库库存
          item.onWayUsableCount = 0; // 在途库存
          item.zaikuCount = 0; // 下单在库库存
          item.zaiTuCount = 0; // 下单在途库存
          item.retailPrice = 0; //建议零售价
          item.salePrice = 0; //会员价
          item.diyType = detais.diyType; //定制类型 0-标准定制 1-DIY定制
          item.goodsId = detais.id;
          item.goodsName = detais.goodsName;
          item.goodsType = detais.goodsType; //商品类型 1-普通 2-定制
          item.goodsNo = detais.goodsNo; //商品编码
          // 在途的判断
          let sysAutoDelivery = uni.getStorageSync("autoDelivery"); //分销商是否是直发客户：1 是，0 否
          let sysOnWayFlag = uni.getStorageSync("onWayFlag"); //分销商是否支持在途库存 1是 0否 默认是1
          let onWayFlag = 0;
          //onwaySaleFlag 直发客户是否支持在途：0-否 1-是
          if (sysAutoDelivery == 1) {
            onWayFlag =
              sysOnWayFlag == 1 ? (item.onwaySaleFlag == 1 ? 1 : 0) : 0;
          } else {
            onWayFlag = sysOnWayFlag;
          }
          that.$set(item, "sysOnWayFlag", Number(onWayFlag));
          // 默认取第一个货品的在途值
          if (index == 0) {
            that.onWayFlags = item.sysOnWayFlag;
          }
          if (item.boxs.length > 0) {
            // item.boxNum=item.boxs[0].boxNum?item.boxs[0].boxNum:0;
            // item.boxType=item.boxs[0].boxType;
            item.boxs.push({ boxNum: 1, boxType: "件" });
          } else {
            that.$set(item, "boxs", [{ boxNum: 1, boxType: "件" }]);
          }
          if (index == 0) {
            that.$set(detais, "boxsList", item.boxs);
          }
          let itemsIdObj = {
            goodsId: id,
            itemId: item.id,
          };
          twoGoodsIdList.push(itemsIdObj);
        });
        that.detailsObj = detais;
        that.goodsItems = detais.goodsItems;
        that.goodsInfo = detais.goodsItems[0];
        that.getInventoryFun(goodsIdList); //获取商品库存
        that.getPriceItemListFun(twoGoodsIdList); //
        that.getActivityFun(id); //获取活动
        console.log("货品列表：", that.goodsItems);
        console.log("第一个货品信息", that.goodsInfo);
        console.log("货品详情对象：", that.detailsObj);
      });
    },
    // 根据商品ID获取活动
    getActivityFun(id) {
      let that = this;
      promotiongroupseckill({ id: id }).then((res) => {
        console.log("活动列表", res.data);
        let list = res.data;
        let goodsItems = that.goodsItems;
        if (res.success) {
          // 货品活动
          goodsItems.forEach((gItem) => {
            if (list.goodsItemPromotions.length > 0) {
              let promotionList = list.goodsItemPromotions;
              promotionList.forEach((pItem) => {
                if (gItem.id == pItem.id) {
                  // 促销活动
                  if (pItem.promotions) {
                    that.$set(gItem, "promotions", pItem.promotions);
                    that.$set(gItem, "activityType", 3);
                  }

                  // 拼团
                  if (pItem.groupSeckills) {
                    that.$set(gItem, "groupSeckills", pItem.groupSeckills);
                    if (pItem.groupSeckills[0].groupSeckillType) {
                      that.$set(
                        gItem,
                        "activityType",
                        pItem.groupSeckills[0].groupSeckillType
                      );
                    }
                  }
                }
              });
            }
            if (list.orderPromotions && list.orderPromotions.length > 0) {
              if (!gItem.promotions || gItem.promotions.length == 0) {
                that.$set(gItem, "promotions", list.orderPromotions[0]);
                that.$set(gItem, "activityType", 3);
              }
            }
          });
        }
      });
    },
    // 获取是否 0 实际库存 1模糊库存
    userShopSettingFun() {
      let that = this;
      userShopSetting().then((res) => {
        if (res.success) {
          that.stockShowFlag = res.data.stockShowFlag;
          that.stockShowNumber = res.data.stockShowNumber;
        }
        //  console.log('实际库存:',res.data);
      });
    },

    // 获取商品库存
    getInventoryFun(ids) {
      let that = this;
      let userid = uni.getStorageSync("userId");
      let params = {
        distributorId: userid,
        itemIdList: ids,
      };
      listStockByCondition(params).then((stockRes) => {
        if (stockRes.success) {
          let stockResData = stockRes.data;
          let goodItem = JSON.parse(JSON.stringify(that.goodsItems));
          for (let i = 0; i < stockResData.length; i++) {
            goodItem.forEach((items) => {
              if (items.id === stockResData[i].itemId) {
                items.inStockUsableCount = stockResData[i].inStockUsableCount; // 在库库存
                items.onWayUsableCount =
                  stockResData[i].onWayUsableCount >
                  stockResData[i].inStockUsableCount
                    ? stockResData[i].onWayUsableCount -
                      stockResData[i].inStockUsableCount
                    : 0; // 在途库存
              }
            });
          }
          that.goodsItems = goodItem;
          console.log("库存", that.goodsItems);
        }
      });
    },

    // 是否选择在途
    choiceTutype() {
      this.isInTransit = !this.isInTransit;
    },

    // 上拉分页
    pageScrollClick() {
      console.log("分页");
      if (this.page < this.total) {
        this.page += 1;
        this.getData();
      }
    },

    open(id) {
      this.getDetails(id);
      this.$refs.goodItem.initialFun();
    },

    // 进入详情
    toGoodsDetails(item) {
      uni.navigateTo({
        url: "/pages/classify/goodsDetails?id=" + item.id,
      });
    },
  },
};
</script>

<style lang="scss">
.myCollect {
  .myCollect-scroll {
    padding-top: calc(116rpx + var(--status-bar-height));
    // #ifdef H5
    padding-top: 104rpx;
    // #endif
  }
  .myCollect-list {
    padding-top: 20rpx;
    .myCollect-listLine {
      display: flex;
      background: #fff;
      padding: 30rpx;
      position: relative;
      .myCollect-img {
        width: 160rpx;
        height: 160rpx;
        border-radius: 10rpx;
        overflow: hidden;
      }

      .myCollect-listLine-RG {
        margin-left: 30rpx;
        .myCollect-name {
          font-size: 26rpx;
          font-weight: bold;
          height: 80rpx; //暂定的高度
        }

        .myCollect-price {
          display: flex;
          align-items: center;
          justify-content: space-between;
          color: $base-color2;
          width: 530rpx;
          margin-top: 10rpx;
          view {
            display: flex;
            align-items: center;
            .myCollect-priceLabel {
              font-size: 20rpx;
            }
            .myCollect-priceNum {
              font-size: 28rpx;
            }
          }
          .myCollect-shopCartImg {
            width: 52rpx;
            height: 46rpx;
          }
        }
      }
    }
  }

  .myCollect-CancelSanjiao {
    position: absolute;
    width: 0;
    height: 0;
    border-color: transparent #f3f3f3 transparent transparent;
    border-style: solid;
    border-width: 0 50rpx 50rpx 0; /*3.上边宽100，右边宽50，下左边宽0*/
    top: 0;
    right: 0;
  }
  .collectSelcet {
    color: $base-color1;
    font-size: 36rpx;
  }
  .collectSelcet-no {
    color: #999;
    font-size: 38rpx;
  }
}
</style>
