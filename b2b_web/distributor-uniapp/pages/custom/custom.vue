<!--
 * @Author: yaowei
 * @Date: 2019-11-15 16:32:48
 * @LastEditors: yaowei
 * @LastEditTime: 2019-05-26 11:27:34
-->
<template>
  <view class="custom-wrap">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view class="classifyLisy-title">{{ title }}</view>
      </view>
    </view>

    <!-- banner -->
    <view class="banner-wrap" v-if="banner">
      <image :src="banner" mode="widthFix"></image>
    </view>

    <!-- 筛选 -->
    <view
      class="qo-screen-module classifyList-navTab"
      :class="banner ? 'hasBanner' : ''"
    >
      <view
        v-for="item in screenList"
        :key="item.id"
        class="qo-screen-line"
        @click="tabFun(item.id)"
      >
        <text :class="tabIndex == item.id ? 'fontColor' : ''">{{
          item.name
        }}</text>
        <text
          class="sortIcon"
          :class="{
            sort_up: sortWay === 1,
            sort_down: sortWay === 2,
          }"
          v-if="item.id == 1"
        ></text>
      </view>
      <view class="qo-screen-line">
        <text
          class="row-line-icon"
          :class="rowOrLine ? 'icon-line-list' : 'icon-row-list'"
          @click="styleChoice()"
        ></text>
      </view>
    </view>
    <!-- 横向风格 -->
    <view
      class="cfL-listModule"
      :class="banner ? 'hasBanner' : ''"
      v-if="rowOrLine"
    >
      <view
        class="cfL-listLine"
        :class="{ 'border-top': index === 0 }"
        v-for="(item, index) in goodsList"
        :key="index"
      >
        <text class="label-new" v-if="item.newFlag == 1">新品</text>
        <text class="label-new" v-if="item.groupFlag == 1">拼团</text>
        <text class="label-new" v-if="item.promotionFlag == 1">促销</text>
        <text class="label-new" v-if="item.seckillFlag == 1">秒杀</text>
        <image
          :src="item.imageUrl1"
          @click="toGoodsDetails(item)"
          mode="scaleToFill"
        ></image>
        <view class="cfL-listRg">
          <view class="cfL-listName" @click="toGoodsDetails(item)">{{
            item.goodsName
          }}</view>
          <view class="label-module" @click="toGoodsDetails(item)">
            <text
              class="orangeLabel"
              v-for="tagItem in item.tags"
              :key="tagItem.id"
              >{{ tagItem.name }}</text
            >
          </view>
          <view class="cfL-listBtm">
            <view class="cfL-listBtm-Lf" @click="toGoodsDetails(item)">
              <template v-if="userId != ''">
                <view v-if="item.promotionType == 0" class="cfL-listBtm-LfPrice"
                  ><text>￥</text
                  >{{
                    item.minPrice
                      ? item.minPrice
                      : item.maxPrice
                      ? item.maxPrice
                      : "暂未定价"
                  }}
                </view>
                <view v-else class="cfL-listBtm-LfPrice"
                  ><text>￥</text>{{ item.promotionMinPrice }}
                </view>
              </template>
              <template v-else>
                <view class="cfL-listBtm-LfPrice"><text>登录后查看</text></view>
              </template>
              <view class="cfL-listBtm-LfNum"
                >销量：{{ item.saleNum ? item.saleNum : 0 }}</view
              >
            </view>
            <text
              class="iconfont icon-purchased cartIconColor"
              @click="open(item.id)"
            ></text>
          </view>
        </view>
      </view>
    </view>
    <!--  瀑布流风格-->
    <view
      class="cfL-listModule cfL-waterList"
      :class="banner ? 'hasBanner' : ''"
      v-if="!rowOrLine"
    >
      <view class="padding-top" v-if="goodsList.length > 0">
        <waterfallsFlow
          ref="waterfalls_flow_nav"
          :list="goodsList"
          imageSrcKey="imageUrl1"
          :offset="15"
          @wapper-lick="toGoodsDetails($event)"
          class="clf-labelP"
        >
          <!--  #ifdef  MP-WEIXIN -->
          <view
            v-for="(item, index) of goodsList"
            :key="index"
            slot="slot{{index}}"
          >
            <text class="label-new cfL-label" v-if="item.newFlag == 1"
              >新品</text
            >
            <text class="label-new cfL-label" v-if="item.groupFlag == 1"
              >拼团</text
            >
            <text class="label-new cfL-label" v-if="item.promotionFlag == 1"
              >促销</text
            >
            <text class="label-new cfL-label" v-if="item.seckillFlag == 1"
              >秒杀</text
            >
            <view class="cts-list-cnt">
              <view class="cts-list-title">{{ item.goodsName }}</view>
              <view class="cfL-listLabel label-module">
                <text
                  class="orangeLabel"
                  v-for="tagItem in item.tags"
                  :key="tagItem.id"
                  >{{ tagItem.name }}</text
                >
              </view>
              <view class="cts-list-text">
                <view class="cfL-price" v-if="userId != ''"
                  ><text>￥</text
                  >{{
                    item.minPrice
                      ? item.minPrice
                      : item.maxPrice
                      ? item.maxPrice
                      : "暂未定价"
                  }}</view
                >
                <view class="cfL-price" v-else><text>登录后查看</text></view>
                <view class="cfL-sales"
                  >销量：{{ item.saleNum ? item.saleNum : 0 }}</view
                >
              </view>
            </view>
          </view>
          <!--  #endif -->

          <!-- #ifndef  MP-WEIXIN -->
          <template v-slot:default="item">
            <text class="label-new cfL-label" v-if="item.newFlag == 1"
              >新品</text
            >
            <text class="label-new cfL-label" v-if="item.groupFlag == 1"
              >拼团</text
            >
            <text class="label-new cfL-label" v-if="item.promotionFlag == 1"
              >促销</text
            >
            <text class="label-new cfL-label" v-if="item.seckillFlag == 1"
              >秒杀</text
            >
            <view class="cts-list-cnt">
              <view class="cts-list-title">{{ item.goodsName }}</view>
              <view class="cfL-listLabel label-module">
                <text
                  class="orangeLabel"
                  v-for="tagItem in item.tags"
                  :key="tagItem.id"
                  >{{ tagItem.name }}</text
                >
                <!-- <text class="redLabel">标签1</text> -->
              </view>
              <view class="cts-list-text">
                <view class="cfL-price" v-if="userId != ''"
                  ><text>￥</text
                  >{{
                    item.minPrice
                      ? item.minPrice
                      : item.maxPrice
                      ? item.maxPrice
                      : "暂未定价"
                  }}</view
                >
                <view class="cfL-price" v-else><text>登录后查看</text></view>
                <view class="cfL-sales"
                  >销量：{{ item.saleNum ? item.saleNum : 0 }}</view
                >
              </view>
            </view>
          </template>
          <!-- #endif -->
        </waterfallsFlow>
      </view>
    </view>

    <view class="no-more" v-if="goodsList.length > 0 && page == totalPage"
      >没有更多啦~</view
    >
    <view class="nodata-style" v-if="goodsList.length == 0">
      <image
        src="../../static/img/noAddress_img.png"
        mode="scaleToFill"
      ></image>
      <view>暂无数据</view>
    </view>

    <!-- 货品列表组件-->
    <goodsItem
      class="category-goodsItem"
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
import waterfallsFlow from "@/components/maramlee-waterfalls-flow/maramlee-waterfalls-flow.vue";
import {
  userGoodsList,
  priceGoodsList,
  goodsDetails,
  userShopSetting,
  listStockByCondition,
  priceItemList,
  promotiongroupseckill,
  getColumn,
  getColumnList,
} from "../../common/api.js";
import goodsItem from "../../components/myComponents/goodsItem.vue";
export default {
  components: { waterfallsFlow, goodsItem },
  data() {
    return {
      title: "", // 标题
      towId: "", // 分类id
      columnId: "", // 栏目id
      screenList: [
        { id: 0, name: "综合推荐" },
        { id: 3, name: "只看新品" },
        { id: 2, name: "销量" },
        { id: 1, name: "价格" },
      ],
      tabIndex: 0,
      sortType: "", //排序：1 价格 2 销量 3 时间，不传为综合
      sortWay: "", //升序降序：1 升序 2 降序
      newFlag: "", //是否筛选新品 0-否 1-是
      rowOrLine: true,
      page: 1,
      size: 10,
      totalPage: 0,
      goodsList: [],
      detailsObj: {}, //详情
      goodsItems: [], //详情列表
      isInTransit: false, //是否包含在途
      goodsItems: [],
      stockShowFlag: 0, // 0 实际库存 1模糊库存
      stockShowNumber: 0, // 库存临界值

      goodsInfo: {},
      goodsItemsIndex: 0,
      onWayFlags: null,
      userId: "",

      banner: "", // 栏目banner
    };
  },
  onReachBottom() {
    if (this.page < this.totalPage) {
      this.page += 1;
      if (this.columnId) {
        this.getColumnById(this.columnId);
        this.getColumnList();
      } else {
        this.getGoodsListFun();
      }
    }
  },
  onLoad(option) {
    let userId = uni.getStorageSync("userId");
    if (userId && userId != "" && userId != "undefined") {
      this.userId = userId;
    }

    this.title = option.title;

    if (option.columnId) {
      // 首页栏目
      this.columnId = Number(option.columnId);
      this.getColumnById(this.columnId);
      this.getColumnList();
    } else {
      // 默认定制
      this.title = "个性定制";
      this.towId = Number(option.id) || "";
      this.getGoodsListFun();
    }
  },
  methods: {
    //返回
    toback() {
      uni.navigateBack({
        delta: 1,
      });
    },
    // 获取商品列表
    getGoodsListFun() {
      let params = {
        classifyId: this.towId, //分类id
        page: this.page,
        size: this.size,
        sortType: this.sortType, //排序：1 价格 2 销量 3 时间，不传为综合
        sortWay: this.sortWay, //升序降序：1 升序 2 降序
        hotType: this.newFlag, //是否筛选新品 热门：1 活动热销(只有登录后的用户支持) 2 新品上市，不传为全部
        goodsType: 2, // 商品类型：1 普通 2 定制（不传查全部）
      };
      let that = this;
      userGoodsList(params).then((res) => {
        if (res.success) {
          let list = res.data.list;
          that.totalPage = res.data.pages;
          that.goodsList = [...that.goodsList, ...list];
          let ids = [];
          list.forEach((item) => {
            ids.push(item.id);
          });
          if (ids.length > 0 && that.userId != "") {
            that.getPriceData(ids);
          }
        }
      });
    },
    // 根据ids查询价格
    getPriceData(goodsIdList) {
      let that = this;
      priceGoodsList({ goodsIds: goodsIdList }).then((res) => {
        if (res.success) {
          let list = JSON.parse(JSON.stringify(that.goodsList));
          list.forEach((item) => {
            for (let i = 0; i < res.data.length; i++) {
              if (item.id === res.data[i].goodsId) {
                item.minPrice = res.data[i].minPrice;
                item.maxPrice = res.data[i].maxPrice;
                item.promotionMaxPrice = res.data[i].promotionMaxPrice
                  ? res.data[i].promotionMaxPrice
                  : 0;
                item.promotionMinPrice = res.data[i].promotionMinPrice
                  ? res.data[i].promotionMinPrice
                  : 0;
                item.promotionType = res.data[i].promotionType; //活动类型，0 没活动 1促销活动 2拼团 3秒杀
              }
            }
          });
          that.goodsList = list;
        }
      });
    },

    // tab选择
    tabFun(id) {
      this.goodsList = [];
      this.page = 1;
      this.tabIndex = id;
      if (id == 0) {
        this.sortType = "";
        this.sortWay = "";
        this.newFlag = "";
      } else if (id == 1) {
        this.sortType = id;
        if (this.sortType) {
          this.sortWay = this.sortWay == 1 ? 2 : 1;
        } else {
          this.sortWay == 1;
        }
        this.newFlag = "";
      } else if (id == 2) {
        this.sortType = id;
        this.sortWay = "";
        this.newFlag = "";
      } else if (id == 3) {
        this.sortType = "";
        this.sortWay = "";
        this.newFlag = 2;
      }

      if (this.columnId) {
        this.getColumnById(this.columnId);
        this.getColumnList();
      } else {
        this.getGoodsListFun();
      }
    },

    // 价格的升序或者降序
    priceSortFun(sortWay) {
      this.goodsList = [];
      this.page = 1;
      this.sortWay = sortWay == 1 ? 2 : 1;
      if (this.columnId) {
        this.getColumnById(this.columnId);
        this.getColumnList();
      } else {
        this.getGoodsListFun();
      }
    },
    // 获取详情数据
    getDetails(id) {
      let that = this;
      this.totalNum = 0;
      this.totalPrice = 0;
      this.goodsInfo = {};
      this.goodsItems = [];
      goodsDetails({ id: id }).then((res) => {
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
          item.zaiKuCount = 0; // 下单在库库存
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
        this.detailsObj = detais;
        this.goodsItems = detais.goodsItems;
        this.goodsInfo = detais.goodsItems[0];
        this.getInventoryFun(goodsIdList); //获取商品库存
        this.getPriceItemListFun(twoGoodsIdList); //
        this.getActivityFun(id); //获取活动
      });
    },
    // 根据商品ID获取活动
    getActivityFun(id) {
      let that = this;
      promotiongroupseckill({ id: id }).then((res) => {
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
                that.$set(gItem, "promotions", list.orderPromotions);
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
        }
      });
    },
    // 根据商品货品ids查询价格
    getPriceItemListFun(ids) {
      let params = {
        goodsItemIds: ids,
      };
      priceItemList(params).then((res) => {
        if (res.success) {
          let list = res.data;
          this.goodsItems.forEach((item) => {
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
    // 是否选择在途
    choiceTutype() {
      this.isInTransit = !this.isInTransit;
    },

    open(id) {
      if (this.userId != "") {
        this.getDetails(id);
        this.$refs.goodItem.initialFun();
      } else {
        uni.showToast({ title: "登录后才能购买", icon: "none" });
      }
    },

    // 进入详情
    toGoodsDetails(item) {
      console.log(item);
      uni.navigateTo({
        url: "/pages/classify/goodsDetails?id=" + item.id,
      });
    },

    // 选择列表风格
    styleChoice() {
      this.rowOrLine = !this.rowOrLine;
    },

    // 首页栏目
    getColumnById(id) {
      let params = {
        id: id,
      };
      getColumn(params).then((res) => {
        if (res.success) {
          if (res.data.bannerImg) {
            this.banner = res.data.bannerImg;
          }
        }
      });
    },
    getColumnList() {
      let params = {
        columnId: this.columnId, //栏目id
        page: this.page,
        size: this.size,
        sortType: this.sortType, //排序：1 价格 2 销量 3 时间，不传为综合
        sortWay: this.sortWay, //升序降序：1 升序 2 降序
        hotType: this.newFlag, //是否筛选新品 热门：1 活动热销(只有登录后的用户支持) 2 新品上市，不传为全部
      };
      let that = this;
      getColumnList(params).then((res) => {
        if (res.success) {
          let list = res.data.list;
          that.totalPage = res.data.pages;
          that.goodsList = [...that.goodsList, ...list];
          let ids = [];
          list.forEach((item) => {
            ids.push(item.id);
          });
          if (ids.length > 0 && that.userId != "") {
            that.getPriceData(ids);
          }
        }
      });
    },
  },
};
</script>

<style lang="scss">
.banner-wrap {
  position: fixed;
  padding-top: 145rpx;
  width: 100%;
  height: 330rpx;
  z-index: 999;
  top: 20rpx;

  image {
    width: 100%;
    max-height: 100%;
  }
}
.classifyList-navTab {
  position: fixed;
  width: 100%;
  z-index: 999;
  top: 20rpx;

  &.hasBanner {
    padding-top: 0;
    top: 495rpx;
  }
}
.classifyLisy-title {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  padding-left: 20rpx;
}
.custom-wrap {
  font-size: 26rpx;

  // 横向列表
  .cfL-listModule {
    padding-top: 250rpx;
    /* #ifdef H5 */
    padding-top: 200rpx;
    /* #endif*/
    background: #fff;

    &.hasBanner {
      padding-top: 580rpx;
      /* #ifdef H5 */
      padding-top: 530rpx;
      /* #endif*/
    }
    .cfL-listLine {
      display: flex;
      padding: 20rpx 30rpx;
      position: relative;

      &.border-top {
        padding-top: 40rpx;
        border-top: 20rpx solid $opacity-border;
      }
      .label-new {
        position: absolute;
        top: 30rpx;
      }
      image {
        width: 177rpx;
        height: 177rpx;
        background-color: $opacity-img-bg;
        border-radius: 10rpx;
        box-sizing: border-box;
      }
      .cfL-listRg {
        margin-left: 20rpx;
        width: 490rpx;
        .cfL-listName {
          font-weight: 500;
          height: 80rpx;
        }
        .cfL-listBtm {
          display: flex;
          justify-content: space-between;
          align-items: flex-end;
          margin-top: 10rpx;
          .cfL-listBtm-Lf {
            display: flex;
            align-items: center;
            .cfL-listBtm-LfPrice {
              font-size: 30rpx;
              color: $base-color2;
              width: 250rpx;
              text {
                font-size: 20rpx;
              }
            }
            .cfL-listBtm-LfNum {
              color: #999;
              font-size: 20rpx;
              margin-left: 20rpx;
            }
          }
        }
      }
    }
  }
  .cfL-listLabel {
    margin: 10rpx 15rpx;
  }
  // 瀑布流列表
  .cfL-waterList {
    .padding-top {
      border-top: 20rpx solid $opacity-border;
      padding-top: 30rpx;
    }
    .clf-labelP {
      position: relative;
      .cfL-label {
        top: 0;
      }
    }
    .cts-list-cnt {
      .cts-list-title {
        font-size: 26rpx;
        color: #333;
        font-weight: 500;
        margin: 15rpx;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
      }
      .cts-list-text {
        padding: 15rpx;
        display: flex;
        align-items: center;
        justify-content: space-between;
        .cfL-price {
          font-size: 30rpx;
          color: $base-color2;
          text {
            font-size: 22rpx;
          }
        }

        .cfL-sales {
          font-size: 22rpx;
          color: #999;
        }
      }
    }
  }
}
.category-goodsItem {
  /deep/.qo-popup-btmFix {
    padding: 30rpx 20rpx 40rpx;
    bottom: 0;
  }
}
</style>
