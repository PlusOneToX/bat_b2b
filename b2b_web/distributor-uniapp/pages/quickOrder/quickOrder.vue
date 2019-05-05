<template>
  <view class="quickOrder" @click="hiddenBoxFun">
    <view class="ks-topSearch">
      <view class="search-view">
        <image src="../../static/imgs/icon_search.png"></image>
        <input placeholder="搜索商品" disabled="true" @click.stop="toSearch" />
      </view>
    </view>
    <!-- 包含在途 -@click="choiceTutype()" -->
    <!-- 筛选 -->
    <view class="ks-screen">
      <view
        v-for="item in screenList"
        :key="item.id"
        class="ks-screen-line"
        :class="tabIndex == item.id ? 'ks-screen-Hover' : ''"
        @click.stop="tabFun(item.id)"
        :style="{ color: tabIndex == item.id ? themeColor : '' }"
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
    <!-- 分类-new -->
    <view class="ks-classify">
      <scroll-view scroll-x="true">
        <view class="ks-classify-list" :style="{ width: typeWidth }">
          <view
            v-for="item in classifyList"
            :key="item.id"
            class="ks-classify-item"
            :class="classifyIndex == item.id ? 'ks-classify-itemHover' : ''"
            @click.stop="choiceType(item)"
            :style="{
              border: classifyIndex == item.id ? '2rpx solid' + themeColor : '',
            }"
          >
            <text>{{ item.name }}</text>
          </view>
        </view>
      </scroll-view>
      <!-- 图标 -->
      <view class="ks-classify-moreIcon" @click.stop="showDrawer">
        <image src="../../static/imgs/icon_more.png"></image>
      </view>
    </view>
    <!-- 列表-new -->
    <view class="ks-listView">
      <view v-for="(item, index) in goodsList" :key="index" class="ks-listLine">
        <!-- 商品 -->
        <view class="ks-listGoodName" @click.stop="openGoodsInfoPopupFun(item)">
          <view>{{ item.goodsName }}</view>
          <image src="../../static/imgs/icon_chakan.png"></image>
        </view>
        <!-- 货品 -->
        <template v-for="(items, indexs) in item.goodsItems">
          <view
            class="ks-listItem"
            :key="items.id"
            :style="{
              'border-bottom': (
                item.indexs == 2
                  ? indexs == 1
                  : indexs == item.goodsItems.length - 1
              )
                ? 'none'
                : '1rpx solid #F3F4F8',
            }"
            v-if="
              indexs < item.indexs &&
              ((underStockFlag == 0 &&
                (isInTransit
                  ? items.onWayUsableCount + items.inStockUsableCount > 0
                  : items.inStockUsableCount > 0)) ||
                underStockFlag == '')
            "
          >
            <view class="ks-listItem-info" @click.stop="toGoodsDetails(item)">
              <image
                class="ks-listItem-infoItemImg"
                :lazy-load="true"
                :src="items.itemImg ? items.itemImg : itemImgNo"
              ></image>
              <view class="ks-listItem-infoCenter">
                <view class="ks-listItem-infoLine">
                  <text>编码：</text>
                  <text>{{ items.itemCode }}</text>
                </view>
                <view class="ks-listItem-infoLine">
                  <text>规格：</text>
                  <text>{{ items.specsName }}</text>
                </view>
                <view
                  class="ks-listItem-infoLine"
                  @click.stop="openBoxShow(items)"
                >
                  <text>购买单位：</text>
                  <text>{{ items.boxNum }}/{{ items.boxType }}</text>
                  <image src="../../static/imgs/icon_down.png"></image>
                </view>
                <!--装箱规格  -->
                <view class="ks-listItem-boxsView" v-if="items.boxShow">
                  <view class="ks-listItem-boxs">
                    <view class="ks-listItem-boxsTrangle"></view>
                    <view class="ks-listItem-boxsTr">
                      <text>装箱数量</text>
                      <text>单位</text>
                    </view>
                    <view
                      v-for="(boxItem, index) in items.boxs"
                      :key="index"
                      class="ks-listItem-boxsTd"
                    >
                      <text @click.stop="selectBoxsFun(boxItem, items, item)">{{
                        boxItem.boxNum
                      }}</text>
                      <text>{{ boxItem.boxType }}</text>
                    </view>
                  </view>
                </view>
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
                <text
                  @click.stop="numberChange(items.itemCount, items, item, 1)"
                  :style="{ color: items.buyNum > 0 ? themeColor : '#C2C2C2 ' }"
                  >-
                </text>
                <input
                  v-model="items.buyNum"
                  @blur.stop="numberChange(items.itemCount, items, item, 3)"
                  :style="{
                    'border-color': items.buyNum > 0 ? themeColor : '#C2C2C2 ',
                    color: items.buyNum > 0 ? themeColor : '#333',
                  }"
                />
                <text
                  @click.stop="numberChange(items.itemCount, items, item, 2)"
                  :style="{ color: items.buyNum > 0 ? themeColor : '#C2C2C2 ' }"
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
            <view
              class="ks-listItem-tip"
              v-if="items.buyNum > items.inStockUsableCount"
              >加购数量含有在途商品，本商品将会分批次发货。
            </view>
          </view>
        </template>
        <!-- 查看更多 -->
        <template v-if="item.goodsItems && item.goodsItems.length > 2">
          <view
            class="ks-listItem-moreCheck"
            @click.stop="moreListShow(index)"
            v-if="item.indexs == 2"
            >查看更多
          </view>
          <view
            class="ks-listItem-moreCheck"
            @click.stop="packUpList(index)"
            v-if="item.indexs == item.goodsItems.length + 1"
            >收起
          </view>
        </template>
      </view>
    </view>

    <!-- 全部分类抽屉式 -->
    <uni-drawer ref="showRight" mode="right" :width="drawerWidth">
      <view class="ks-drawer">
        <view class="ks-drawer-screen">
          <view class="ks-drawer-screenItem" @click="tabDrewFun">
            <text
              class="iconfont icon-icon_selected_def"
              v-if="!tabIndexDraw"
            ></text>
            <text
              class="iconfont icon-icon_selected"
              :style="{ color: themeColor }"
              v-if="tabIndexDraw"
            ></text>
            <text class="ks-drawer-screenName">只看有货</text>
          </view>
        </view>
        <view class="ks-drawer-box">
          <view class="ks-drawer-title">全部分类</view>
          <view class="ks-drawer-boxList">
            <view
              v-for="item in classifyList"
              :key="item.id"
              class="ks-drawer-item"
              @click="drawerChicoeFun(item)"
            >
              <image :src="item.imageUrl"></image>
              <view class="ks-drawer-itemName">{{ item.name }}</view>
              <text
                class="iconfont icon-icon_selected01"
                :style="{ color: themeColor }"
                v-if="drawerClassifyIndex == item.id"
              ></text>
            </view>
            <view style="clear: both"></view>
          </view>
        </view>

        <view class="ks-drawer--btm">
          <text
            :style="{ border: '1rpx solid' + themeColor, color: themeColor }"
            @click="drawerResetFun"
            >重置
          </text>
          <text
            :style="{
              border: '1rpx solid' + themeColor,
              background: themeColor,
            }"
            @click="drawerConfirmFun"
            >确定
          </text>
        </view>
      </view>
    </uni-drawer>
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

    <view class="ks-btmTip" v-if="!loadingVal && page == totalPage"
      >您已翻到底啦！
    </view>

    <uni-load-more
      status="loading"
      v-if="loadingVal"
      class="loading-more"
    ></uni-load-more>
    <!-- no-moreData -->

    <view class="nodata-style" v-if="goodsList.length == 0 && !loadingVal">
      <image src="../../static/img/noAddress_img.png"></image>
      <view>暂无数据</view>
    </view>

    <!-- 快速下单-右边悬浮 -->
    <view class="ks-rgOrder" @click="openQuickOrderPopup">
      <image src="../../static/imgs/shopCart.png"></image>
      <text>{{ totalNum }}</text>
    </view>

    <!-- 分类顶部弹框 -Y-->
    <uni-popup ref="typePopup" type="top" class="qo-typePopup">
      <view class="qo-typePopup-content">
        <view
          class="qo-typePopup-one"
          :class="twoClassifyIndex == item.id ? 'font-colorHoverL' : ''"
          v-for="item in twoClassifyList"
          :key="item.id"
          @click="twoClassifyTabFun(item.id)"
          >{{ item.name }}
        </view>
      </view>
    </uni-popup>

    <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>

    <!-- 底部导航 -->
    <!-- <tabBar :themeColor="themeColor" :pageIndex="3"></tabBar> -->
  </view>
</template>

<script>
import {
  classifyList,
  userGoodsList,
  priceGoodsList,
  goodsDetails,
  userShopSetting,
  listStockByCondition,
  priceItemList,
  promotiongroupseckill,
  addShoppingcart,
} from "../../common/api.js";
import tabBar from "../../components/myComponents/tabBar.vue";
export default {
  components: { tabBar },
  data() {
    return {
      themeColor: "",
      loadingVal: false, //加载
      drawerWidth: 300,
      classifyList: [], //分类
      classifyIndex: 0, //一级分类选中的index
      drawerClassifyIndex: 0, //一级分类选中的index
      drawerClassifyItem: {},
      classifyId: "", //分类id
      twoClassifyList: [], //二级分类
      twoClassifyIndex: "", //二级分类选中的index
      page: 1,
      size: 10,
      totalPage: 0,
      goodsList: [], //商品列表
      typeWidth: "750rpx",
      typeIndex: 0,
      isOpen: false,
      // 筛选
      screenList: [
        { id: 0, name: "综合推荐" },
        { id: 3, name: "只看新品" },
        // {id:4,name:'只看有货'},
        { id: 2, name: "销量" },
        { id: 1, name: "价格" },
      ],
      tabIndex: 0, //筛选index
      tabIndexDraw: false, //筛选抽屉
      sortType: "", //排序：1 价格 2 销量 3 时间，不传为综合
      sortWay: 1, //升序降序：1 升序 2 降序
      newFlag: "", //是否筛选  1 活动热销(只有登录后的用户支持) 2 新品上市，不传为全部
      underStockFlag: "", //只看有货 0不缺货，1缺货
      detailsObj: {}, //详情
      goodsItems: [], //详情列表
      isInTransit: true, //是否包含在途
      stockShowFlag: 0, // 0 实际库存 1模糊库存
      stockShowNumber: 0, // 库存临界值
      //购买单位
      totalNum: 0, //共几件
      totalPrice: 0, //共计金额
      shopTotalNum: 0,
      quickList: [], //快速下单的列表
      guiGeShow: true, //规格弹框
      userId: "", //分销商id
      shaixuanHover: true,

      zongHeName: "综合推荐",
      zongHeIndex: 0,
      zongHeShow: false,
      isOneGood: false, //是否只看有货
      tipTextShow: false,
      tipText: "",
      goodsItemObj: {}, //商品信息
      sortWayNum: 0, //是否是第一次进入价格
      itemImgNo: "../../static/imgs/img-wu.png",
    };
  },
  onShow() {
    this.themeColor = uni.getStorageSync("themeColor");
    let userId = uni.getStorageSync("userId");
    if (userId && userId != "" && userId != "undefined") {
      this.userId = userId;
    }
    console.log("分销商Id", this.userId);
    this.totalNum = 0;
    this.classifyListFun();
    // this.closeDrawer();
    // this.closeGoodsInfoPopupFun();
  },
  onLoad() {},
  // 触底分页
  onReachBottom() {
    if (this.page < this.totalPage) {
      this.page += 1;
      this.getGoodsList();
    }
  },
  methods: {
    //返回
    toback() {
      uni.navigateBack({
        delta: 1,
      });
    },
    toSearch() {
      this.hiddenBoxFun();
      uni.navigateTo({
        url: "/pages/index/search",
      });
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
    // 装箱规格
    openBoxShow(items) {
      this.$set(items, "boxShow", !items.boxShow);
      // 处理装箱规格的展示隐藏
      this.goodsList.forEach((item6) => {
        item6.goodsItems.forEach((item7) => {
          if (item7.id !== items.id) {
            this.$set(item7, "boxShow", false);
          }
        });
      });
    },
    // 关闭所有的装箱规格函数
    hiddenBoxFun() {
      // 处理装箱规格的展示隐藏
      this.goodsList.forEach((item) => {
        if (item.goodsItems && item.goodsItems.length > 0) {
          item.goodsItems.forEach((items) => {
            this.$set(items, "boxShow", false);
          });
        }
      });
    },

    // 抽屉打开
    showDrawer() {
      this.hiddenBoxFun();
      this.$refs.showRight.open();
    },
    // 抽屉关闭
    closeDrawer() {
      this.$refs.showRight.close();
    },
    // 商品弹框---打开
    openGoodsInfoPopupFun(item) {
      this.hiddenBoxFun();
      this.goodsItemObj = item;
      this.$refs.goodsInfoPopup.open();
    },
    // 商品弹框---关闭
    closeGoodsInfoPopupFun() {
      this.goodsItemObj = {};
      this.$refs.goodsInfoPopup.close();
    },

    // 获取分类
    classifyListFun() {
      let that = this;
      classifyList().then((res) => {
        if (res.success) {
          that.classifyList = res.data.classify;
          that.classifyIndex = res.data.classify[0].id;
          let width = that.classifyList.length * 176;
          // const query = uni.createSelectorQuery().in(that);
          // query
          //   .select(".ks-classify-list")
          //   .boundingClientRect()
          //   .exec((res) => {
          // 		console.log(res[0])
          //     // width = res[0].width;
          //   });
          // console.log(width);
          if (width > 750) {
            that.typeWidth = width + "rpx";
          }

          that.classifyId = res.data.classify[0].id;
          that.drawerClassifyIndex = res.data.classify[0].id;
          that.drawerClassifyItem = res.data.classify[0];

          // 获取商品列表
          that.page = 1;
          that.goodsList = [];
          that.getGoodsList();

          if (res.data.classify[0].subClassifies) {
            that.twoClassifyList = res.data.classify[0].subClassifies;
            console.log("侧边分类", that.twoClassifyList);
          }
        }
      });
    },
    // 获取商品列表
    async getGoodsList() {
      let that = this;
      this.loadingVal = true;
      let params = {
        classifyId: this.classifyId, //分类id
        goodsType: 1,
        page: this.page,
        size: this.size,
        sortType: this.sortType, //排序：1 价格 2 销量 3 时间，不传为综合
        sortWay: this.sortWay, //升序降序：1 升序 2 降序
        hotType: this.newFlag, //是否筛选新品 0-否 1-是
        underStockFlag: this.underStockFlag, //只看有货
      };
      let res = await userGoodsList(params);
      if (res.success && res.data.list) {
        let list = res.data.list;
        that.totalPage = res.data.pages;
        let ids = [];
        let goodsIdList = [];
        let twoGoodsIdList = [];
        for (let i = 0; i < list.length; i++) {
          ids.push(list[i].id);
          that.$set(list[i], "isCheck", false);
          let detailRes = await goodsDetails({ id: list[i].id });
          if (detailRes.success) {
            that.$set(list[i], "goodsItems", detailRes.data.goodsItems);
            that.$set(list[i], "indexs", 2);
            if (detailRes.data.goodsItems) {
              let goodsItems = detailRes.data.goodsItems;
              console.log("=====", detailRes.data.goodsItems);
              goodsItems.forEach((items) => {
                that.$set(items, "isCheck", false);
                that.$set(items, "activityType", 0); //初始没有活动（ 1：拼团  2：秒杀  3：普通活动  ）
                that.$set(items, "boxType", "件");
                that.$set(items, "boxNum", 1);
                that.$set(items, "inStockUsableCount", 0); // 在库库存
                that.$set(items, "onWayUsableCount", 0); // 在途库存
                that.$set(items, "zaiKuCount", 0); // 下单在库库存
                that.$set(items, "zaiTuCount", 0); // 下单在途库存
                that.$set(items, "retailPrice", 0); //建议零售价
                that.$set(items, "salePrice", 0); //会员价
                that.$set(items, "diyType", list[i].diyType); //定制类型 0-标准定制 1-DIY定制
                that.$set(items, "goodsId", list[i].id);
                that.$set(items, "goodsName", list[i].goodsName);
                that.$set(items, "goodsType", list[i].goodsType); //商品类型 1-普通 2-定制
                that.$set(items, "goodsNo", list[i].goodsNo); //商品编码
                that.$set(items, "buyNum", 0);
                that.$set(items, "itemCount", 0);
                that.$set(items, "guiGeShow", false);
                that.$set(items, "kuCunShow", false); //库存展示
                that.$set(items, "setInter", ""); //库存展示定时器
                that.$set(items, "boxShow", false);
                if (items.boxs.length > 0) {
                  items.boxs.push({ boxNum: 1, boxType: "件" });
                } else {
                  that.$set(items, "boxs", [{ boxNum: 1, boxType: "件" }]);
                }
                goodsIdList.push(items.id);
                let itemsIdObj = {
                  goodsId: list[i].id,
                  itemId: items.id,
                };
                twoGoodsIdList.push(itemsIdObj);
              });
            }
          }
        }

        that.goodsList = [...that.goodsList, ...list];
        that.loadingVal = false;
        if (this.userId != "") {
          // 获取货品价格
          let priceRes = await priceItemList({ goodsItemIds: twoGoodsIdList });
          if (priceRes.success) {
            let priceItemData = priceRes.data;
            console.log("000---");
            if (that.goodsList && that.goodsList.length > 0) {
              that.goodsList.forEach((item) => {
                if (item.goodsItems && item.goodsItems.length > 0) {
                  item.goodsItems.forEach((items) => {
                    priceItemData.forEach((priItem) => {
                      if (priItem.itemId == items.id) {
                        that.$set(items, "retailPrice", priItem.retailPrice);
                        that.$set(items, "salePrice", priItem.salePrice);
                      }
                    });
                  });
                }
              });
            }
          }
          // 获取库存
          let stockParams = {
            distributorId: this.userId,
            itemIdList: goodsIdList,
          };
          let stockRes = await listStockByCondition(stockParams);
          if (stockRes.success) {
            let stockList = stockRes.data;
            if (stockList.length > 0) {
              stockList.forEach((items) => {
                if (that.goodsList && that.goodsList.length > 0) {
                  that.goodsList.forEach((item) => {
                    if (item.goodsItems && item.goodsItems.length > 0) {
                      item.goodsItems.forEach((item2) => {
                        if (item2.id == items.itemId) {
                          let inStockUsableCount = items.inStockUsableCount; // 在库库存
                          let onWayUsableCount =
                            items.onWayUsableCount > items.inStockUsableCount
                              ? items.onWayUsableCount -
                                items.inStockUsableCount
                              : 0; // 在途库存
                          that.$set(
                            item2,
                            "inStockUsableCount",
                            inStockUsableCount
                          );
                          that.$set(
                            item2,
                            "onWayUsableCount",
                            onWayUsableCount
                          );
                        }
                      });
                    }
                  });
                }
              });
            }
          }
        }
      }
      console.log("快速下单列表：", that.goodsList);
    },
    getGoodsList2() {
      let that = this;
      this.loadingVal = true;
      let params = {
        classifyId: this.classifyId, //分类id
        page: this.page,
        size: this.size,
        sortType: this.sortType, //排序：1 价格 2 销量 3 时间，不传为综合
        sortWay: this.sortWay, //升序降序：1 升序 2 降序
        hotType: this.newFlag, //是否筛选新品 0-否 1-是
      };

      userGoodsList(params).then((res) => {
        if (res.success && res.data.list) {
          let list = res.data.list;
          that.totalPage = res.data.pages;
          let ids = [];
          let goodsIdList = [];
          let twoGoodsIdList = [];
          list.forEach((item) => {
            ids.push(item.id);
            that.$set(item, "isCheck", false);
            goodsDetails({ id: item.id }).then((Dres) => {
              that.$set(item, "goodsItems", Dres.data.goodsItems);
              that.$set(item, "indexs", 2);
              let goodsItems = Dres.data.goodsItems;
              if (item.goodsItems && item.goodsItems.length > 0) {
                goodsItems.forEach((items) => {
                  that.$set(items, "isCheck", false);
                  that.$set(items, "activityType", 0); //初始没有活动（ 1：拼团  2：秒杀  3：普通活动  ）
                  that.$set(items, "boxType", "件");
                  that.$set(items, "boxNum", 1);
                  that.$set(items, "inStockUsableCount", 0); // 在库库存
                  that.$set(items, "onWayUsableCount", 0); // 在途库存
                  that.$set(items, "zaiKuCount", 0); // 下单在库库存
                  that.$set(items, "zaiTuCount", 0); // 下单在途库存
                  that.$set(items, "retailPrice", 0); //建议零售价
                  that.$set(items, "salePrice", 0); //会员价
                  that.$set(items, "diyType", item.diyType); //定制类型 0-标准定制 1-DIY定制
                  that.$set(items, "goodsId", item.id);
                  that.$set(items, "goodsName", item.goodsName);
                  that.$set(items, "goodsType", item.goodsType); //商品类型 1-普通 2-定制
                  that.$set(items, "goodsNo", item.goodsNo); //商品编码
                  that.$set(items, "buyNum", 0);
                  that.$set(items, "itemCount", 0);
                  that.$set(items, "guiGeShow", false);
                  if (items.boxs.length > 0) {
                    items.boxs.push({ boxNum: 1, boxType: "件" });
                  } else {
                    that.$set(items, "boxs", [{ boxNum: 1, boxType: "件" }]);
                  }
                  goodsIdList.push(items.id);
                  let itemsIdObj = {
                    goodsId: item.id,
                    itemId: items.id,
                  };
                  twoGoodsIdList.push(itemsIdObj);
                });
              }
            });
          });
          that.goodsList = [...that.goodsList, ...list];
          // 登录状态下才执行接口
          // 获取货品价格

          if (this.userId != "") {
            setTimeout(function () {
              that.priceGoodsListFun(ids);
              that.priceItemListFun(twoGoodsIdList);
              that.listStockByConditionFun(goodsIdList);
            }, 100);
          }
        } else {
          that.loadingVal = false;
        }
      });
    },

    // 查找商品价格
    priceGoodsListFun(ids) {
      let that = this;
      alert();
      priceGoodsList({ goodsIds: ids }).then((res) => {
        if (res.success && res.data && res.data.length > 0) {
          if (that.goodsList && that.goodsList.length > 0) {
            that.goodsList.forEach((itemP) => {
              res.data.forEach((priItem) => {
                if (itemP.id === priItem.goodsId) {
                  that.$set(itemP, "minPrice", priItem.minPrice);
                  that.$set(itemP, "maxPrice", priItem.maxPrice);
                }
              });
            });
          }
        }
      });
    },
    // 查询货品价格
    async priceItemListFun(twoGoodsIdList) {
      let that = this;
      await priceItemList({ goodsItemIds: twoGoodsIdList }).then((res) => {
        if (res.success) {
          let priceItemData = res.data;
          if (that.goodsList && that.goodsList.length > 0) {
            that.goodsList.forEach((item) => {
              if (item.goodsItems && item.goodsItems.length > 0) {
                item.goodsItems.forEach((items) => {
                  priceItemData.forEach((priItem) => {
                    if (priItem.itemId == items.id) {
                      that.$set(items, "retailPrice", priItem.retailPrice);
                      that.$set(items, "salePrice", priItem.salePrice);
                    }
                  });
                });
              }
            });
          }
          that.loadingVal = false;
        }
      });
    },

    // 查询货品库存
    async listStockByConditionFun(goodsIdList) {
      let that = this;
      let stockParams = {
        distributorId: this.userId,
        itemIdList: goodsIdList,
      };
      await listStockByCondition(stockParams).then((res) => {
        if (res.success) {
          let stockList = res.data;
          if (stockList.length > 0) {
            stockList.forEach((items) => {
              if (that.goodsList && that.goodsList.length > 0) {
                that.goodsList.forEach((item) => {
                  if (item.goodsItems && item.goodsItems.length > 0) {
                    item.goodsItems.forEach((item2) => {
                      if (item2.id == items.itemId) {
                        let inStockUsableCount = items.inStockUsableCount; // 在库库存
                        let onWayUsableCount =
                          items.onWayUsableCount > items.inStockUsableCount
                            ? items.onWayUsableCount - items.inStockUsableCount
                            : 0; // 在途库存
                        that.$set(
                          item2,
                          "inStockUsableCount",
                          inStockUsableCount
                        );
                        that.$set(item2, "onWayUsableCount", onWayUsableCount);
                      }
                    });
                  }
                });
              }
            });
          }
        }
      });
    },

    // 根据商品ID获取活动--Y
    getActivityFun(id, goodsItems) {
      let that = this;
      promotiongroupseckill({ id: id }).then((res) => {
        let list = res.data;
        if (res.success) {
          // 货品活动
          if (that.goodsList && that.goodsList.length > 0) {
            that.goodsList.forEach((item) => {
              if (item.goodsItems && item.goodsItems.length > 0) {
                item.goodsItems.forEach((gItem) => {
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
                          that.$set(
                            gItem,
                            "groupSeckills",
                            pItem.groupSeckills
                          );
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
          }
        }
      });
    },

    // 根据商品货品ids查询价格--y
    getPriceItemListFun(ids) {
      let that = this;
      let params = {
        goodsItemIds: ids,
      };
      priceItemList(params).then((res) => {
        if (res.success) {
          let list = res.data;
          if (item.goodsItems && item.goodsItems.length > 0) {
            that.goodsItems.forEach((item) => {
              list.forEach((items) => {
                if (items.itemId == item.id) {
                  that.$set(item, "salePrice", items.salePrice);
                  that.$set(item, "retailPrice", items.retailPrice);
                }
              });
            });
          }
        }
      });
    },
    // 选择规格--y
    selectBoxsFun(item, gItem, goodsItems) {
      let that = this;
      console.log("规格：", item);
      console.log("此货品：", gItem);
      // 计算当前货品总购买数
      let buyTotalNum = Number(gItem.buyNum) * item.boxNum;
      console.log("购买的数量：", buyTotalNum);
      //当前货品总库存--- （是否包含在途）
      let stockTotalNum = gItem.inStockUsableCount + gItem.onWayUsableCount;
      console.log("库存：", stockTotalNum);

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
        that.$set(gItem, "boxShow", false);
      } else {
        this.$set(item, "kuCunShow", true);
        setInterval(function () {
          that.$set(item, "kuCunShow", false);
        }, 2000);
        // that.tipFun('库存不足');
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
    // 抽屉--选择分类
    drawerChicoeFun(item) {
      this.drawerClassifyIndex = item.id;
      this.drawerClassifyItem = item;
    },
    // 抽屉--确定
    drawerConfirmFun() {
      if (JSON.stringify(this.drawerClassifyItem) == "{}") {
        this.tipFun("请选择分类");
      } else {
        this.choiceType(this.drawerClassifyItem);
        this.closeDrawer();
      }
    },

    // 抽屉--重置
    drawerResetFun() {
      this.tabIndexDraw = false;
      this.drawerClassifyIndex = this.classifyList[0].id;
      this.drawerClassifyItem = this.classifyList[0];
    },

    // 一级选择分类
    choiceType(item) {
      this.hiddenBoxFun();
      this.zongHeShow = false;
      this.totalNum = 0;
      this.classifyIndex = item.id;
      this.page = 1;
      this.goodsList = [];
      this.classifyId = item.id;
      this.getGoodsList();
      this.twoClassifyList = [];
      if (item.subClassifies) {
        this.twoClassifyList = item.subClassifies;
      }
    },
    // 二级分类选中
    twoClassifyTabFun(id) {
      this.page = 1;
      this.goodsList = [];
      this.twoClassifyIndex = id;
      this.classifyId = id;
      this.getGoodsList();
      this.closeTypePopup();
    },
    // 价格的升序或者降序
    priceSortFun(sortWay, id) {
      this.goodsList = [];
      this.page = 1;
      this.sortWay = sortWay == 1 ? 2 : 1;
      this.getGoodsList();
    },
    // 打开分类弹框
    openTypePopup(item, isOpen) {
      this.zongHeShow = false;
      this.classifyId = item.id;
      if (this.twoClassifyList.length > 0) {
        this.isOpen = true;
        this.$refs.typePopup.open();
      }
    },
    // 关闭分类弹框
    closeTypePopup() {
      this.zongHeShow = false;
      this.isOpen = false;
      this.$refs.typePopup.close();
    },

    // 抽屉只看有货的操作
    tabDrewFun() {
      this.tabIndex = 4;
      this.sortType = "";
      this.sortWay = "";
      this.newFlag = "";
      if (this.tabIndexDraw) {
        this.underStockFlag = "";
        this.tabIndex = "";
        this.tabIndexDraw = false;
      } else {
        this.tabIndexDraw = true;
        this.underStockFlag = 0;
      }
    },

    // tab选择
    tabFun(id) {
      this.hiddenBoxFun();
      this.goodsList = [];
      this.totalNum = 0;
      this.page = 1;

      this.zongHeShow = false;

      if (this.tabIndex == 4) {
        this.tabIndex = 0;
      } else {
        this.tabIndex = id;
      }

      if (id == 0) {
        this.sortWayNum = 0;
        this.sortType = "";
        this.sortWay = "";
        this.newFlag = "";
        this.underStockFlag = "";
        // this.zongHeName='综合推荐';
        this.zongHeIndex = 0;
      } else if (id == 1) {
        this.shaixuanHover = !this.shaixuanHover;
        this.sortWayNum += 1;
        this.sortType = id;
        if (this.sortWayNum > 1) {
          this.sortWay = this.sortWay == 1 || this.sortWay == "" ? 2 : 1;
        }
        this.newFlag = "";
        this.underStockFlag = "";
      } else if (id == 2) {
        this.sortWayNum = 0;
        this.sortType = id;
        this.sortWay = "";
        this.newFlag = "";
        this.underStockFlag = "";
      } else if (id == 3) {
        this.sortWayNum = 0;
        this.sortType = "";
        this.sortWay = "";
        this.underStockFlag = "";
        this.newFlag = 2;
        // this.zongHeName='只看新品',
        this.zongHeIndex = 3;

        console.log("---", this.zongHeIndex);
      } else if (id == 4) {
        this.sortWayNum = 0;
        this.sortType = "";
        this.sortWay = "";
        this.newFlag = "";
        if (this.isOneGood) {
          this.underStockFlag = "";
          this.tabIndex = "";
          this.isOneGood = false;
        } else {
          this.isOneGood = true;
          this.underStockFlag = 0;
        }
      }
      this.getGoodsList();
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

    // 是否选择在途
    choiceTutype() {
      this.isInTransit = !this.isInTransit;
    },
    // 更多小分类
    moreListShow(num) {
      this.zongHeShow = false;
      let list = this.goodsList;
      list.forEach((item, index) => {
        if (index == num) {
          item.indexs = item.goodsItems.length + 1;
        } else {
          item.indexs = 2;
        }
      });
      this.hiddenBoxFun();
    },
    // 收起小分类
    packUpList(num) {
      this.goodsList[num].indexs = 2;
      this.zongHeShow = false;
      this.hiddenBoxFun();
    },

    // 计数器
    numberChange(itemNum, item, goodsItems, type) {
      this.hiddenBoxFun();
      this.zongHeShow = false;
      console.log("老数据", itemNum);
      clearInterval(item.setInter);
      let that = this;
      this.totalNum = 0;
      this.totalPrice = 0;

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
      } else {
        // this.$set(item,'buyNum',Number(itemNum-1));
        that.$set(item, "kuCunShow", true);

        item.setInter = setInterval(function () {
          that.$set(item, "kuCunShow", false);
        }, 2000);

        // that.tipFun('库存不足');
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
      that.$set(item, "lastPrice", lastPrice);
      // 计算价格-end
      let list = JSON.parse(JSON.stringify(that.goodsList));
      list.forEach((item) => {
        item.goodsItems.forEach((items) => {
          that.totalNum += items.buyNum * items.boxNum;
        });
      });

      console.log("加减商品列表：", that.goodsList);
    },

    // 打开快速下单弹框-Y
    openQuickOrderPopup() {
      this.zongHeShow = false;
      this.quickList = [];
      let newArray = [];
      let list = JSON.parse(JSON.stringify(this.goodsList));
      // 过滤掉goodsItems数量为0的
      list.forEach((item, index) => {
        newArray[index] = item;
        newArray[index].goodsItems = item.goodsItems.filter((items) => {
          return items.buyNum > 0;
        });
      });
      list = list.filter((item) => {
        return item.goodsItems.length > 0;
      }); //过滤goodsItems长度为0的值
      this.quickList = list;
      console.log("已经选择的货品：", list);
      if (this.totalNum > 0) {
        // this.$refs.quickOrderPopup.open();
        uni.navigateTo({
          url: "quickOrder_list",
        });
        uni.setStorageSync("ksOrderList", JSON.stringify(this.quickList));
      } else {
        this.tipFun("请添加至少一个商品");
      }
    },
    // 关闭快速下单弹框
    closeQuickOrderPopup() {
      this.$refs.quickOrderPopup.close();
    },
    // 快速下单移动
    swipeChange(e, id, itemsTd) {
      console.log(e);
    },

    // 快速下单点击删除
    swipeClick(itemsTd) {
      let that = this;
      this.shopTotalNum = 0;
      this.totalPrice = 0;
      let list = JSON.parse(JSON.stringify(this.quickList));
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

      this.quickList = list;
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
      this.quickList.forEach((item) => {
        item.goodsItems.forEach((item3) => {
          if (item3.isCheck) {
            that.shopTotalNum += Number(item3.buyNum) * item3.boxNum;
            that.totalPrice += Number(item3.lastPrice);
          }
        });
      });
    },

    // 快速下单选中要购买的产品--二级--Y
    checkTwoFun(items) {
      let that = this;
      this.shopTotalNum = 0;
      this.totalPrice = 0;
      this.$set(items, "isCheck", !items.isCheck);
      console.log(this.quickList);
      this.quickList.forEach((item) => {
        item.goodsItems.forEach((item3) => {
          if (item3.isCheck) {
            that.shopTotalNum += Number(item3.buyNum) * item3.boxNum;
            that.totalPrice += Number(item3.lastPrice);
          }
        });
      });
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
      let list = this.quickList;
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
          this.$refs.quickOrderPopup.close();
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
      let list = this.quickList;
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
                Number(list[i].goodsItems[j].buyNum) *
                  list[i].goodsItems[j].boxNum
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
                Number(list[i].goodsItems[j].buyNum) *
                  list[i].goodsItems[j].boxNum -
                  list[i].goodsItems[j].inStockUsableCount
              );
            }
            orderList.push(list[i].goodsItems[j]);
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
      // 处理装箱规格的展示隐藏
      this.hiddenBoxFun();
      uni.navigateTo({
        url: "/pages/classify/goodsDetails?id=" + item.id,
      });
    },
  },
};
</script>

<style lang="scss">
img[lazy="loading"] {
  /*加载中*/
  background: url(../../static/imgs/img-wu.png);
}
.close-openIcon {
  width: 60rpx;
  text-align: right;
  height: 50rpx;
}
.qo-smBuyBox {
  position: relative;
}

.ks-btmTip {
  text-align: center;
  font-size: 24rpx;
  color: #5f5f5f;
  padding: 0rpx 0 50rpx 0;
}
.uni-drawer {
  z-index: 9999 !important;
}

.quickOrder {
  font-size: 26rpx;
  font-family: PingFangSC-Medium, PingFang SC;
  // 顶部搜索
  .ks-topSearch {
    position: fixed;
    top: 0;
    width: 100%;
    border-top: 1rpx solid #f3f4f8;
    z-index: 9999;
    background: #fff;
    .search-view {
      display: flex;
      background: #fff;
      padding: 22rpx 30rpx;
      position: relative;
      image {
        width: 44rpx;
        height: 44rpx;
        position: absolute;
        left: 55rpx;
        top: 40rpx;
      }
      input {
        background: #f3f4f8;
        width: 100%;
        height: 80rpx;
        background: #f3f4f8;
        border-radius: 40rpx;
        font-size: 28rpx;
        padding-left: 78rpx;
      }
    }
  }
  // 筛选
  .ks-screen {
    position: fixed;
    top: 120rpx;
    z-index: 9999;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-around;
    padding: 8rpx 34rpx;
    color: #333333;
    background: #fff;
    box-sizing: border-box;
    .ks-screen-line {
      display: flex;
      align-items: center;
      font-size: 32rpx;
      white-space: nowrap;
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
    }
  }
  // 分类
  .ks-classify {
    position: fixed;
    top: 175rpx;
    z-index: 9999;
    background: #fff;
    padding: 28rpx 30rpx;
    width: 100%;
    box-sizing: border-box;
    .ks-classify-list {
      .ks-classify-item {
        display: inline-block;
        background: #f3f4f8;
        padding: 8rpx;
        font-size: 24rpx;
        border-radius: 36rpx;
        margin-right: 32rpx;
        min-width: 140rpx;
        text-align: center;
        box-sizing: border-box;
      }
      .ks-classify-itemHover {
        border: 2rpx solid #d9d9d9;
      }
    }
    .ks-classify-moreIcon {
      position: absolute;
      right: 30rpx;
      top: 28rpx;
      width: 110rpx;
      height: 50rpx;
      background: linear-gradient(
        90deg,
        rgba(255, 255, 255, 0.36) 0%,
        rgba(255, 255, 255, 0.99) 29%,
        #ffffff 100%
      );
      text-align: right;
      image {
        margin-top: 4rpx;
        width: 44rpx;
        height: 44rpx;
      }
    }
  }
  // 列表
  .ks-listView {
    padding: 280rpx 30rpx 24rpx 30rpx;
    // #ifdef H5
    padding-top: 195rpx;
    // #endif
    .ks-listLine {
      background: #ffffff;
      border-radius: 16rpx;
      margin-top: 24rpx;
      .ks-listGoodName {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 24rpx 30rpx;
        color: #333;
        border-bottom: 1rpx solid #f3f4f8;
        view {
          width: 574rpx;
          font-size: 32rpx;
          font-weight: 600;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
        image {
          width: 40rpx;
          height: 40rpx;
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
          .ks-listItem-infoItemImg {
            width: 120rpx;
            height: 124rpx;
            border-radius: 8rpx;
            background: rgba(243, 244, 248, 0.5);
          }
          .ks-listItem-infoCenter {
            font-size: 24rpx;
            margin-left: 24rpx;
            width: 400rpx;
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
      color: #333;
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
  // 抽屉
  .ks-drawer {
    width: 600rpx;
    height: 100vh;
    background: #f3f4f8;
    border-top: 1rpx solid #f3f4f8;
    .ks-drawer-screen {
      background: #fff;
      padding: 36rpx 40rpx;
      display: flex;
      justify-content: center;
      width: 520rpx;
      .ks-drawer-screenItem {
        display: flex;
        align-items: center;
        .iconfont {
          margin-right: 6rpx;
        }
        text:nth-child(1) {
          font-size: 34rpx;
        }
        text:nth-child(2) {
          font-size: 26rpx;
        }
      }
    }
    .ks-drawer-box {
      background-color: #fff;
      margin-top: 24rpx;
      padding: 30rpx 0;
      .ks-drawer-title {
        font-size: 28rpx;
        margin-left: 24rpx;
      }
      .ks-drawer-boxList {
        .ks-drawer-item {
          float: left;
          text-align: center;
          width: 120rpx;
          height: 155rpx;
          margin-top: 24rpx;
          margin-left: 24rpx;
          position: relative;

          image {
            width: 84rpx;
            height: 84rpx;
            margin-bottom: 8rpx;
            margin-top: 5rpx;
          }
          .ks-drawer-itemName {
            font-size: 24rpx;
          }
          text {
            font-size: 40rpx;
            position: absolute;
            top: -10rpx;
            right: 0;
            border-radius: 100%;
            background: #fff;
          }
        }
      }
    }

    .ks-drawer--btm {
      display: flex;
      align-items: center;
      left: 32rpx;
      position: fixed;
      bottom: 50rpx;
      text {
        width: 270rpx;
        height: 80rpx;
        line-height: 80rpx;
        text-align: center;
        // border:2rpx solid #0076A5;
      }
      text:nth-child(1) {
        border-top-left-radius: 50rpx;
        border-bottom-left-radius: 50rpx;
        // color:#0076A5 ;
      }
      text:nth-child(2) {
        border-top-right-radius: 50rpx;
        border-bottom-right-radius: 50rpx;
        color: #fff;
        // background: #0076A5;
      }
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
  // 右边悬浮图标
  .ks-rgOrder {
    background: #fff;
    border-radius: 100%;
    width: 100rpx;
    height: 100rpx;
    color: #fff;
    text-align: center;
    font-size: 0;
    position: fixed;
    bottom: 138rpx;
    right: 30rpx;
    box-shadow: 0px 4rpx 14rpx 0px rgba(44, 44, 44, 0.12);
    image {
      width: 60rpx;
      height: 54rpx;
      margin-top: 28rpx;
      margin-left: 0rpx;
    }
    text {
      position: absolute;
      font-size: 14rpx;
      display: block;
      background: #f94021;
      top: 24rpx;
      right: 8rpx;
      min-width: 36rpx;
      height: 24rpx;
      line-height: 24rpx;
      border-radius: 16rpx;
    }
  }

  .qo-typePopup {
    /* #ifdef H5 */
    top: 180rpx;
    /* #endif*/
    .qo-typePopup-content {
      background: #fff;
      /* #ifndef H5 */
      min-height: 30rpx;
      padding-top: 230rpx;
      /* #endif*/
      .qo-typePopup-one {
        padding: 30rpx;
        font-size: 28rpx;
        color: #333;
      }

      .qo-typePopup-two {
        padding: 0 0 30rpx 70rpx;
        font-size: 26rpx;
        color: $base-color4;
      }
      .qo-typePopup-two:nth-child(1) {
        padding-top: 0 !important;
      }
    }
  }
  .qoScreenTop {
    width: 100%;
    background: #fff;
  }

  // 快速下单弹框
  .quick-list-popup {
    .myCollect-popup-content {
      height: 80vh;
    }

    .myCollect-scroll-Y {
      height: 70vh;
    }
  }

  .qo-popupMoule-box {
    padding-bottom: 230rpx;
    .quickTip {
      left: 70rpx;
    }
  }
  .qo-popupMoule {
    & + .qo-popupMoule {
      border-top: 10rpx solid $opacity-border;
    }
    .qo-popup-bg {
      display: flex;
      padding: 30rpx 20rpx;
      border: 1rpx solid $opacity-border;

      image {
        width: 162rpx;
        height: 162rpx;
        margin-left: 15rpx;
      }
      .qo-popup-bgRG {
        margin-left: 20rpx;
        width: 450rpx;
        .qo-popup-name {
          font-size: 26rpx;
        }
        .qo-popup-rgNumClass {
          font-size: 22rpx;
          color: #999;
          margin-top: 8rpx;
        }
      }
    }
    .qo-popup-bgLf {
      display: flex;
      align-items: center;
      text {
        color: #999;
      }
    }
    .qo-popup-sm {
      display: flex;
      padding: 30rpx 20rpx;
      image {
        width: 120rpx;
        height: 120rpx;
        margin-left: 15rpx;
      }
      .qo-popup-smRG {
        width: 520rpx;
        margin-left: 20rpx;
        font-size: 22rpx;
        .qo-popup-smRgTop {
          display: flex;
          align-items: center;
          justify-content: space-between;

          .qo-popup-smInform {
            color: #999;
            text {
              display: block;
            }
          }
          .qo-popup-smNum {
            display: flex;
          }
          .qo-popup-smPrice {
            color: $base-color2;
            margin-right: 10rpx;
            font-size: 30rpx;
            text {
              font-size: 22rpx;
            }
          }
        }
        .qo-popup-smRgBtm {
          color: #999;
          margin-top: 15rpx;
          .qo-popup-zaiku {
            text-align: right;
          }
          .qo-popup-zaitu {
            display: flex;
            justify-content: space-between;
          }
        }
      }
    }
  }
  .qo-popup-btmFix {
    position: fixed;
    bottom: 0;
    /* #ifdef H5 */
    bottom: 100rpx;
    /* #endif*/
    left: 0;
    z-index: 999;
    width: 710rpx;
    padding: 20rpx;
    box-shadow: $border-colorShadow;
    display: flex;
    align-items: center;
    justify-content: space-between;
    background: #fff;
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
.qo-top-shaixuan {
  position: relative;
  width: 32rpx;
  height: 35rpx;

  text {
    position: absolute;
    font-size: 35rpx;
  }
  text:nth-child(1) {
    top: -10rpx;
    /* #ifdef H5 */
    top: -15rpx;
    /* #endif*/
  }
  text:nth-child(2) {
    bottom: -5rpx;
  }
  .top-shaixuan-hover {
    color: $base-color1 !important;
  }
}
.qo-screen-line2 {
  position: relative;
  .qo-screen-lineNext {
    position: absolute;
    top: 208rpx;
    left: 0;
    background: #fff;
    padding: 0 25rpx 15rpx 20rpx;
    color: #666;
    width: 150rpx;
    text-align: center;
    border: 1rpx solid #f8f8f8;
    text {
      display: block;
      padding-top: 20rpx;
    }
  }
}
.qo-top-shaixuan2 {
  margin-left: 10rpx;
  .iconfont {
    font-size: 24rpx !important;
    font-weight: normal !important;
  }
}
</style>
