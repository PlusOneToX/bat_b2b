<template>
  <view class="classify searchResult">
    <view class="top-moudle searchResult-top">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title searchResult-topTitle">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view class="searchResult-topLogo">搜索结果</view>
      </view>
    </view>

    <view class="searchResult-input">
      <input
        confirm-type="search"
        placeholder="手机壳"
        v-model="searchContent"
        @confirm="searchFun"
      />
      <image src="../../static/imgs/icon_search.png" @click="searchFun"></image>
    </view>
    <view class="searchResult-screen" v-if="goodsList.length > 0">
      <view
        v-for="item in screenList"
        :key="item.id"
        class="ks-screen-line"
        :class="tabIndex == item.id ? 'ks-screen-Hover' : ''"
        @click.stop="tabFun(item.id)"
      >
        <text>{{ item.name }}</text>
        <image
          src="../../static/imgs/icon_sorting_up.png"
          v-if="item.id == 1 && (sortWay == 1 || sortWay == '')"
          class="ks-screen-priceIcon"
        ></image>
        <image
          src="../../static/imgs/icon_sorting_down.png"
          v-if="item.id == 1 && sortWay == 2"
          class="ks-screen-priceIcon"
        ></image>
        <!-- @click="priceSortFun(sortWay,item.id)" -->
      </view>
    </view>
    <!-- 筛选 -->
    <!-- <view class="qo-screen-module search-top-nav">
			   <view v-for="item in screenList" :key="item.id" class="qo-screen-line" @click="tabFun(item.id)">
				   <text :class="tabIndex==item.id?'fontColor':''" >{{item.name}}</text>
				   <text class="iconfont icon-Thepricesorted sortIcon" v-if="item.id==1" ></text>
				   
			   </view>
			   <view class="qo-screen-line">
				   <text class="qo-screenBorder"></text>
				   <text class="iconfont rowIcon" :class="rowOrLine?'icon-Classification-List':'icon-Classification-Charts'" @click="styleChoice()"></text>
			   </view>
		</view> -->
    <view class="searchResult-noData" v-if="goodsList.length == 0">
      <image src="../../static/imgs/noSearch.png"></image>
      <view>未搜索出结果，请重新输入</view>
    </view>
    <!-- 横向风格 -->
    <view class="cfL-listModule" v-if="rowOrLine">
      <view
        class="cfL-listLine"
        v-for="(item, index) in goodsList"
        :key="index"
      >
        <text class="label-new" v-if="item.newFlag == 1">新品</text>
        <text class="label-new" v-if="item.groupFlag == 1">拼团</text>
        <text class="label-new" v-if="item.promotionFlag == 1">促销</text>
        <text class="label-new" v-if="item.seckillFlag == 1">秒杀</text>
        <image :src="item.imageUrl1" @click="toGoodsDetails(item)"></image>
        <view class="cfL-listRg" @click="toGoodsDetails(item)">
          <view class="cfL-listName">{{ item.goodsName }}</view>
          <view class="label-module">
            <text
              class="orangeLabel"
              v-for="tagItem in item.tags"
              :key="tagItem.id"
              >{{ tagItem.name }}</text
            >
            <!-- <text class="redLabel">标签1</text>
						<text class="greenLabel">标签1</text> -->
          </view>
          <view class="cfL-listBtm">
            <view class="cfL-listBtm-Lf">
              <template v-if="userId != ''">
                <view v-if="item.promotionType == 0" class="cfL-listBtm-LfPrice"
                  ><text>￥</text>{{ item.minPrice }}
                </view>
                <view v-else class="cfL-listBtm-LfPrice"
                  ><text>￥</text>{{ item.promotionMinPrice }}
                </view>
              </template>
              <template v-else>
                <view class="cfL-listBtm-LfPrice"
                  ><text>登录后查看</text>
                </view>
              </template>
              <view class="cfL-listBtm-LfNum"
                >销量：{{ item.saleNum ? item.saleNum : 0 }}</view
              >
            </view>
          </view>
        </view>
        <text
          class="iconfont icon-purchased cartIconColor searchCarIcon"
          @click="open(item.id)"
        ></text>
      </view>
    </view>
    <!--  瀑布流风格-->
    <view class="cfL-listModule cfL-waterList" v-if="!rowOrLine">
      <waterfallsFlow
        ref="waterfalls_flow_nav"
        :list="goodsList"
        imageSrcKey="imageUrl1"
        :offset="15"
        @wapper-lick="toGoodsDetails($event)"
        v-if="goodsList.length > 0"
        class="clf-labelP"
      >
        <!--  #ifdef  MP-WEIXIN -->
        <view
          v-for="(item, index) of goodsList"
          :key="index"
          slot="slot{{index}}"
        >
          <text class="label-new cfL-label" v-if="item.newFlag == 1">新品</text>
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
              <!-- <text class="redLabel">标签1</text>
							<text class="greenLabel">标签1</text> -->
            </view>
            <view class="cts-list-text">
              <view class="cfL-price"><text>￥</text>{{ item.minPrice }}</view>
              <view class="cfL-sales"
                >销量：{{ item.saleNum ? item.saleNum : 0 }}</view
              >
            </view>
          </view>
        </view>
        <!--  #endif -->

        <!-- #ifndef  MP-WEIXIN -->
        <template v-slot:default="item">
          <text class="label-new cfL-label" v-if="item.newFlag == 1">新品</text>
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
                ><text>￥</text>{{ item.minPrice }}</view
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

    <view class="no-more" v-if="page == totalPage">没有更多啦~</view>

    <!-- 货品列表组件-->
    <goodsItem
      ref="goodItem"
      :goodsItems="goodsItems"
      :goodsInfo="goodsInfo"
      :stockShowFlag="stockShowFlag"
      :stockShowNumber="stockShowNumber"
      :onWayFlags="onWayFlags"
      :num="1"
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
} from "../../common/api.js";
import goodsItem from "../../components/myComponents/goodsItem.vue";
export default {
  components: { waterfallsFlow, goodsItem },
  data() {
    return {
      title: "", //分类名
      searchContent: "", //搜索
      screenList: [
        { id: 0, name: "综合推荐" },
        { id: 2, name: "销量" },
        { id: 3, name: "新品" },
        { id: 1, name: "价格" },
      ],
      tabIndex: 0,
      sortType: "", //排序：1 价格 2 销量 3 时间，不传为综合
      sortWay: 1, //升序降序：1 升序 2 降序
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
      unitObj: {
        letter: "", //件
        box: "", //盒
      }, //购买单位
      totalNum: 0, //共几件
      totalPrice: 0, //共计金额
      // 弹框顶部商品信息
      goodsInfo: {},
      goodsItemsIndex: 0,
      onWayFlags: null,
      userId: "",
    };
  },
  onReachBottom() {
    if (this.page < this.totalPage) {
      this.page += 1;
      this.getGoodsListFun(this.classifyId);
    }
  },
  onLoad(option) {
    console.log(option);
    let userId = uni.getStorageSync("userId");
    if (userId && userId != "" && userId != "undefined") {
      this.userId = userId;
    }
    this.searchContent = option.seatchContent;
    this.getGoodsListFun();
  },
  methods: {
    //返回
    toback() {
      uni.navigateBack({
        delta: 1,
      });
    },
    // 点击搜索
    searchFun() {
      this.goodsList = [];
      this.getGoodsListFun();
    },
    // 获取商品列表
    getGoodsListFun() {
      let that = this;

      let params = {
        content: that.searchContent, //分类id
        page: that.page,
        size: that.size,
        sortType: that.sortType, //排序：1 价格 2 销量 3 时间，不传为综合
        sortWay: that.sortWay, //升序降序：1 升序 2 降序
        hotType: that.newFlag, //是否筛选新品 热门：1 活动热销(只有登录后的用户支持) 2 新品上市，不传为全部
      };

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
        console.log("商品列表", that.goodsList);
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
        this.sortWay = this.sortWay == 1 ? 2 : 1;
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
      this.getGoodsListFun();
    },

    // 价格的升序或者降序
    priceSortFun(sortWay) {
      this.goodsList = [];
      this.page = 1;
      this.sortWay = sortWay == 1 ? 2 : 1;
      this.getGoodsListFun();
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
          that.$set(item, "buyNum", 0);
          that.$set(item, "itemCount", 0);
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
        console.log("货品列表：", this.goodsItems);
        console.log("第一个货品信息", this.goodsInfo);
        console.log("货品详情对象：", this.detailsObj);
      });
    },
    // 根据商品ID获取活动
    getActivityFun(id) {
      let that = this;
      promotiongroupseckill({ id: id }).then((res) => {
        console.log("活动列表", res.data);

        if (res.success) {
          let list = res.data;
          let goodsItems = that.goodsItems;
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
    // 根据商品货品ids查询价格
    getPriceItemListFun(ids) {
      let params = {
        goodsItemIds: ids,
      };
      let that = this;
      priceItemList(params).then((res) => {
        if (res.success) {
          console.log("二级价格", res.data.length);
          let list = res.data;
          that.goodsItems.forEach((item) => {
            list.forEach((items) => {
              if (items.itemId == item.id) {
                item.retailPrice = items.retailPrice;
                item.salePrice = items.salePrice;
              }
            });
          });
          console.log("二级价格shuchu", that.goodsItems);
        }
      });
    },
    // 是否选择在途
    choiceTutype() {
      this.isInTransit = !this.isInTransit;
    },

    // 打开货品列表
    open(id) {
      if (this.userId != "") {
        this.getDetails(id);
        this.$refs.goodItem.initialFun();
      } else {
        uni.showToast({ title: "登录后才能购买", icon: "none" });
      }
    },
    closePopup() {
      // 通过组件定义的ref调用uni-popup方法
      this.$refs.popup.close();
    },

    // 进入详情
    toGoodsDetails(item) {
      uni.navigateTo({
        url: "/pages/classify/goodsDetails?id=" + item.id,
      });
    },

    // 选择列表风格
    styleChoice() {
      this.rowOrLine = !this.rowOrLine;
    },
  },
};
</script>

<style lang="scss">
.searchResult {
  font-family: PingFangSC-Medium, PingFang SC;
  height: 100vh;
  background: #fff;
}

.search-top-nav {
  position: fixed;
  z-index: 999;
  width: 100%;
  border-bottom: 15rpx solid #f2f3f8;
}
.searchCarIcon {
  position: absolute;
  right: 20rpx;
  top: 52%;
}
.searchResult-top {
  border: none;
  padding-top: 38rpx;
  // #ifdef H5
  padding-top: 0;
  // #endif
}
.searchResult-topTitle {
  border-bottom: 1rpx solid #f3f4f8;
}
.searchResult-topLogo {
  font-size: 36rpx !important;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 600;
  color: #424242;
}
.searchResult-input {
  position: fixed;
  left: 0;
  right: 0;
  padding: calc(136rpx + var(--status-bar-height)) 30rpx 20rpx;
  // #ifdef H5
  padding-top: 124rpx;
  // #endif
  background: #fff;
  z-index: 999;
  border: 1px solid #ffffff;
  input {
    text-align: left;
    width: 100%;
    height: 80rpx;
    line-height: 80rpx;
    padding-left: 82rpx;
    border-radius: 40rpx;
    background: #f3f4f8;
    font-size: 28rpx;
    box-sizing: border-box;
  }
  image {
    width: 44rpx;
    height: 44rpx;
    position: absolute;
    bottom: 37rpx;
    left: 58rpx;
    z-index: 999;
  }
}
// 筛选
.searchResult-screen {
  position: fixed;
  top: 290rpx;
  // #ifdef H5
  top: 212rpx;
  // #endif
  z-index: 9999;
  width: 700rpx;
  display: flex;
  align-items: center;
  justify-content: space-around;
  padding: 8rpx 25rpx;
  color: #333333;
  background: #fff;
  .ks-screen-line {
    display: flex;
    align-items: center;
    font-size: 32rpx;
    .ks-screen-priceIcon {
      width: 32rpx;
      height: 32rpx;
      margin-left: 8rpx;
    }
  }
  .ks-screen-line:nth-child(2),
  .ks-screen-line:nth-child(3),
  .ks-screen-line:nth-child(4) {
    margin-left: 85rpx;
  }
  .ks-screen-Hover {
    font-size: 36rpx;
    font-weight: 600;
    color: #333333;
  }
}
//无数据
.searchResult-noData {
  padding-top: 150rpx;
  image {
    width: 502rpx;
    height: 496rpx;
    margin-left: 124rpx;
  }
  view {
    color: #424242;
    font-size: 28rpx;
    text-align: center;
    margin-top: 10rpx;
  }
}
.classify {
  font-size: 26rpx;
  .rowIcon {
    font-size: 34rpx;
    color: #999;
  }
  .fontColor {
    color: $base-color1;
  }

  // 横向列表
  .cfL-listModule {
    position: absolute;
    top: 365rpx;
    // #ifdef H5
    top: 287rpx;
    // #endif
    left: 0;
    width: 100%;
    background: #fff;
    .cfL-listLine {
      display: flex;
      padding: 30rpx;
      position: relative;
      image {
        width: 177rpx;
        height: 177rpx;
      }
      .cfL-listRg {
        margin-left: 20rpx;
        width: 490rpx;
        .cfL-listName {
          font-weight: bold;
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
    // background: #fff;

    padding-top: 270rpx;
    /* #ifdef H5 */
    padding-top: 230rpx;
    /* #endif*/
    background: #fff;
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
        padding: 15rpx;
      }
      .cts-list-text {
        margin: 15rpx;
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
</style>
