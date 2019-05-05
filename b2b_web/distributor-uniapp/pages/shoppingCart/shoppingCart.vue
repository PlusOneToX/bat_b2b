<template>
  <view class="shoppingCart">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="shoppingtop-title">
        <text @click="handleEdit()">{{ isEdit ? "完成" : "编辑" }}</text>
        <view>购物车</view>
      </view>
    </view>
    <!-- 11-17 -->
    <view class="sc-list-scroll" v-if="allShopingCartList.length > 0">
      <view v-for="(item, index) in allShopingCartList" :key="index">
        <view class="sc-list" v-for="(items, index) in item.list" :key="index">
          <view
            class="sc-list-label"
            :class="items.reduceOrPresent != 2 ? 'no-promotion' : ''"
          >
            <text class="sc-list-labelName" v-if="item.goodsType == 1">{{
              items.goodsPromotionId == "no" ? "普通商品" : items.ruleName
            }}</text>
            <text class="sc-list-labelName" v-else>定制商品</text>
            <view
              :class="{ hasGift: items.reduceOrPresent == 2 }"
              @click="openGuizePopup(items.rules.conditions)"
            >
              <text
                class="sc-list-labelTj"
                v-if="
                  items.goodsPromotionId &&
                  items.goodsPromotionId != 'no' &&
                  items.rules
                "
                >{{ items.rules.conditions[0].conditionName }}
              </text>
              <image
                src="../../static/imgs/personalMore.png"
                class="sc-list-labelIcon"
                v-if="items.goodsPromotionId && items.goodsPromotionId != 'no'"
              ></image>
            </view>
            <text
              class="sc-list-labelBtn"
              @click="
                openGifts(
                  items.reduceOrPresentId,
                  items.rules.conditions,
                  items
                )
              "
              v-if="items.reduceOrPresent == 2"
              >选择赠品
            </text>
          </view>
          <!-- <uni-swipe-action>
					   <uni-swipe-action-item     class="sc-list-smUniAction" v-for="(childItem,childIndex) in items.children" :key="childItem.id"> -->
          <view
            class="sc-list-smUniAction"
            v-for="(childItem, childIndex) in items.children"
            :key="childIndex"
          >
            <view class="sc-list-line">
              <view class="sc-list-lineItem">
                <!-- 勾选框 -->
                <text
                  v-if="childItem.itemType == 1"
                  class="iconfont sc2-iconCheck"
                  @click="selecetGoodItemFun(index, childItem, items)"
                  :class="
                    childItem.isCheck
                      ? 'icon-icon_selected'
                      : 'icon-icon_selected_def'
                  "
                  :style="{ color: themeColor }"
                >
                </text>
                <!-- 赠品标志 -->
                <view v-if="childItem.itemType == 2" class="sc-list-linePresent"
                  >赠品
                </view>
                <view class="sc-list-lineImgBox">
                  <image
                    :src="
                      childItem.goodsType == 1
                        ? childItem.imageUrl
                          ? childItem.imageUrl
                          : itemImgNo
                        : childItem.diy.previewImage
                        ? childItem.diy.previewImage
                        : itemImgNo
                    "
                    class="sc-smGood"
                    @click="toGoodsDetails(childItem.goodsId)"
                    mode="aspectFit"
                  ></image>
                  <view
                    @click="
                      openActivityPopup(
                        items.children,
                        childItem,
                        items.goodsPromotionId
                      )
                    "
                    :style="{ color: themeColor }"
                    v-if="
                      childItem.promotions ||
                      (childItem.groupSeckills &&
                        childItem.groupSeckills.length > 0)
                    "
                    >切换活动
                  </view>
                </view>
                <view class="sc-Rg">
                  <view class="sc-RgLf">
                    <view class="sc-list-bgRgName">{{
                      childItem.itemName
                    }}</view>
                    <view class="sc-RgLf-info">
                      <text>编码：</text>
                      <text>{{ childItem.itemCode }}</text>
                    </view>
                    <view
                      class="sc-RgLf-info"
                      v-if="childItem.specsName && childItem.specsName != ''"
                    >
                      <text>规格：</text>
                      <text>{{ childItem.specsName }}</text>
                    </view>
                    <view class="sc-RgLf-info" v-if="childItem.colorName">
                      <text>颜色：</text>
                      <text>{{ childItem.colorName }}</text>
                    </view>
                    <view
                      class="sc-RgLf-info"
                      v-if="childItem.diy && childItem.diy.materialName"
                    >
                      <text>材质：</text>
                      <text>{{ childItem.diy.materialName }}</text>
                    </view>
                    <view
                      class="sc-RgLf-info"
                      v-if="childItem.diy && childItem.diy.modelName"
                    >
                      <text>机型：</text>
                      <text>{{ childItem.diy.modelName }}</text>
                    </view>
                  </view>
                  <view class="sc-smRg-topPrice"
                    >￥{{ fomatFloat(Number(childItem.salePrice), 2) }}
                  </view>
                </view>
              </view>
              <view class="sc-list-lineItemBtm">
                <view class="sc-sm-stock">
                  <text v-if="item.goodsType == 1"
                    >在库：{{ childItem.zaiKuCount }}</text
                  >
                  <text v-if="item.goodsType == 1"
                    >在途：{{ childItem.zaiTuCount }}</text
                  >
                </view>
                <!-- <view class="sc-sm-stock"><text v-if="childItem.diyType !== 1">在库：{{childItem.zaiKuCount}}</text><text v-if="childItem.diyType !== 1">在途：{{childItem.zaiTuCount}}</text></view> -->
                <view class="sc-sm-btm">
                  <!-- 加减货品 -->
                  <view class="sc-uni-num">
                    <view
                      v-if="childItem.itemType != 2"
                      class="ks-calculator"
                      :style="{
                        'border-color':
                          childItem.count > 0 ? themeColor : '#C2C2C2 ',
                      }"
                    >
                      <text
                        @click="
                          numberChange(
                            childItem.itemCount,
                            childItem,
                            items,
                            childItem.rules && childItem.rules.conditions
                              ? childItem.rules.conditions
                              : '',
                            1
                          )
                        "
                        :style="{
                          color: childItem.count > 0 ? themeColor : '#C2C2C2 ',
                        }"
                        >-
                      </text>
                      <input
                        :disabled="childItem.itemType == 2"
                        v-model.number="childItem.count"
                        @blur="
                          numberChange(
                            childItem.itemCount,
                            childItem,
                            items,
                            childItem.rules && childItem.rules.conditions
                              ? childItem.rules.conditions
                              : '',
                            3
                          )
                        "
                        :style="{
                          'border-color':
                            childItem.count > 0 ? themeColor : '#C2C2C2 ',
                          color: childItem.count > 0 ? themeColor : '#C2C2C2 ',
                        }"
                      />
                      <text
                        @click="
                          numberChange(
                            childItem.itemCount,
                            childItem,
                            items,
                            childItem.rules && childItem.rules.conditions
                              ? childItem.rules.conditions
                              : '',
                            2
                          )
                        "
                        :style="{
                          color: childItem.count > 0 ? themeColor : '#C2C2C2 ',
                        }"
                        >+
                      </text>
                    </view>
                    <!-- 赠品数量 -->
                    <view v-else>
                      <text class="num-label">数量：</text>{{ childItem.count }}
                    </view>
                    <!-- 库存不足提示 -->
                    <view
                      class="sc-listItem-KuCunBox"
                      v-if="childItem.kuCunShow"
                    >
                      <view class="ks-listItem-KuCun">
                        <view class="ks-listItem-KuCunTrangle"></view>
                        <view class="ks-listItem-KuCunText">库存不足</view>
                      </view>
                    </view>
                  </view>
                </view>
              </view>
            </view>

            <!-- <template v-slot:right>
							 <view class="sc-swipe-delete" @click="deleteShopCarItems(childItem.id,item)"><text>删除</text></view>
						   </template> -->
          </view>
          <!-- </uni-swipe-action-item>
				   </uni-swipe-action> -->
        </view>
      </view>
      <view class="no-more">没有更多了~</view>
    </view>

    <view class="sc-btm-total" v-if="allShopingCartList.length > 0 && !isEdit">
      <view class="sc-btm-totalTop">
        <view class="sc-btm-totalAll">
          <text
            class="iconfont sc3-iconCheck"
            @click="selectAllItemFun"
            :class="
              allCheckFlag ? 'icon-icon_selected' : 'icon-icon_selected_def'
            "
            :style="{ color: themeColor }"
          >
          </text>
          <text>全选</text>
        </view>
        <view class="sc-btm-totalNum-jina">
          合计<text>{{ totalCount }}</text
          >件
        </view>
      </view>
      <view class="sc-btm-totalNum">
        <!-- <view class="sc-btm-totalNum-discounts"><text>已优惠:￥</text>9999.000</view> -->
        <!-- <view class="sc-btm-totalNum-discounts"></view> -->
        <view class="sc-btm-totalNum-price">
          <text>总计：</text>
          <text>￥{{ fomatFloat(Number(totalPrice), 2) }}</text>
        </view>
        <view
          class="sc-btm-totalBtn"
          :style="{ background: themeColor }"
          @click="goTheOrder"
          >去结算</view
        >
      </view>
    </view>

    <!-- 删除 -->
    <view class="sc-btm-total" v-if="isEdit">
      <view class="sc-btm-totalTop">
        <view class="sc-btm-totalAll">
          <text
            class="iconfont sc3-iconCheck"
            @click="selectAllItemFun"
            :class="
              allCheckFlag ? 'icon-icon_selected' : 'icon-icon_selected_def'
            "
            :style="{ color: themeColor }"
          >
          </text>
          <text>全选</text>
        </view>
        <view class="sc-btm-detete" @click="deleteMoreFun">删除</view>
      </view>
    </view>

    <uni-load-more
      status="loading"
      v-if="loadingVal"
      class="loading-more"
    ></uni-load-more>

    <!-- 空数据 展示-->
    <view class="shopping-no-data" v-if="!(allShopingCartList.length > 0)">
      <image src="../../static/imgs/no-data.png"></image>
      <view
        @click="toClassifyFun"
        :style="{ background: themeColor }"
        v-if="userId != undefined && userId != null && userId != ''"
        >去选购
      </view>
      <view @click="tologinFun" :style="{ background: themeColor }" v-else
        >登录后查看
      </view>
    </view>
    <!--选择活动 弹框 -->
    <uni-popup ref="activityPopup" type="bottom" class="myCollect-popup">
      <view class="sc-aCivityPopup-content">
        <view class="myCollect-popup-title">
          <text>请选择活动</text>
          <image
            src="../../static/imgs/icon_close.png"
            class="popup-colose"
            @click="closeActivityPopup"
          ></image>
        </view>
        <scroll-view scroll-y="true" class="myCollect-scroll-Y">
          <view class="sc-activity-list">
            <view
              class="sc-activity-listBox"
              v-for="(item, aIndex) in activityList"
              :key="aIndex"
            >
              <view class="sc-activity-listNameBox">
                <text
                  class="iconfont"
                  :class="
                    item.isCheck
                      ? 'sc-activity-iconY icon-icon_selected'
                      : 'sc-activity-iconN icon-icon_selected_def'
                  "
                  @click="seleceActivityFun(item)"
                ></text>
                <text class="sc-activity-listName"
                  >活动名称：{{ item.ruleName }}
                </text>
              </view>
              <view class="sc-activity-listGuizeBox" v-if="item.conditions">
                <view class="sc-activity-listGuizeTitle">活动条件：</view>
                <view class="sc-activity-listGuizeList">
                  <text v-for="(items, indexs) in item.conditions" :key="indexs"
                    >{{ indexs + 1 }}、{{ items.conditionName }}
                  </text>
                </view>
              </view>
            </view>
          </view>
        </scroll-view>
        <view
          class="sc-activity-listBtn"
          :style="{ 'background-color': themeColor }"
          @click="activityPopupConfirm"
          >确定</view
        >
      </view>
    </uni-popup>

    <!-- 活动规则 -Y-->
    <uni-popup ref="guizePopup" type="bottom" class="myCollect-popup">
      <view class="sc-aCivityPopup-content">
        <view class="myCollect-popup-title">
          <text>活动规则</text>
          <image
            src="../../static/imgs/icon_close.png"
            class="popup-colose"
            @click="closeGuizePopup"
          ></image>
        </view>
        <view class="guize-listBox">
          <view v-for="(item, index) in acivityConditions" :key="index"
            >条件{{ index + 1 }}：{{ item.conditionName }}
          </view>
        </view>
      </view>
    </uni-popup>

    <!--选择赠品 弹框 -y-->
    <uni-popup
      ref="giftsPopup"
      type="bottom"
      class="myCollect-popup gift-popup"
    >
      <view class="sc-aCivityPopup-content">
        <view class="myCollect-popup-title">
          <text>请选择赠品</text>
          <image
            src="../../static/imgs/icon_close.png"
            class="popup-colose"
            @click="closeGiftsPopup"
          ></image>
        </view>
        <scroll-view scroll-y="true" class="myCollect-scroll-Y">
          <view class="popup-giftsList">
            <view
              class="popup-giftsList-line"
              v-for="item in presentList"
              :key="item.id"
            >
              <!-- <text class="iconfont" :class="1?'sc-activity-iconY icon-Checkthe':'sc-activity-iconN icon-uncheck'" @click="seleceActivityFun(1)"></text> -->
              <image
                :src="item.itemImg ? item.itemImg : item.imageUrl1"
              ></image>
              <view class="giftsList-line-rg">
                <view class="popup-giftsList-top">
                  <view class="popup-giftsList-name">{{ item.itemName }}</view>
                  <!-- <uni-number-box  :max="presentCount" @input="giftHandleChange($event,item)"  class="popup-giftsList-numBox"></uni-number-box> -->
                  <view class="self-numberBox">
                    <text @click="giftHandleChange(item.itemCount, item, 1)"
                      >-
                    </text>
                    <input
                      type="number"
                      v-model="item.num"
                      @blur="giftHandleChange(item.itemCount, item, 3)"
                      onkeyup="this.value=this.value.replace(/\D/g,'')"
                    />
                    <text @click="giftHandleChange(item.itemCount, item, 2)"
                      >+
                    </text>
                  </view>
                </view>
                <view class="popup-giftsList-num">
                  <text>编码：{{ item.itemCode }}</text>
                  <text>库存：{{ item.stockNum }}</text>
                </view>
              </view>
            </view>
          </view>
        </scroll-view>

        <view class="popup-giftsList-btm">
          <view class="popup-giftsList-total">
            最多可领取<text>{{ presentCount }}</text
            >件，已选 <text>{{ selectSongCount }}</text
            >件
          </view>
          <view
            class="popup-giftsList-btn"
            :style="{ 'background-color': themeColor }"
            @click="confirmGiftShop"
            >确定
          </view>
        </view>
      </view>
    </uni-popup>

    <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>

    <!-- 底部导航 -->
    <!-- <tabBar :themeColor="themeColor" :pageIndex="4"></tabBar> -->

    <!--加载动画-->
    <loading v-show="loadingFlag" :message="message"></loading>
  </view>
</template>

<script>
import { fomatFloat } from "../../common/common.js";
import loading from "../../components/myComponents/loading.vue";
import tabBar from "../../components/myComponents/tabBar.vue";
import {
  addShoppingcart,
  shoppingcartList,
  listStockByCondition,
  ModifyShoppingcart,
  deleteShoppingcart,
  promotionPresentList,
  deleteShoppingcartOne,
  diyGetByModelIdAndMaterialId,
} from "../../common/api.js";
export default {
  components: { loading, tabBar },
  data() {
    return {
      themeColor: "",
      shopCartList: [], //普通商品列表
      dzShopCartList: [], //定制商品列表
      allShopingCartList: [], //所有商品列表
      loadingVal: false,
      acivityConditions: [], //活动规则列表
      activityGoodsItem: {}, //切换活动对应的货品
      activityGoodList: [], //切换活动同类列表
      deletePresentsIds: [], //切换活动的时候把原有赠品删掉
      presentsPromotionId: "", //选择赠品的活动规则id
      presentCount: 0, //赠品的最大可选数量
      selectSongCount: 0, //赠品的选择的数量
      presentList: 0,
      activityList: [], //活动弹框列表
      // totalCount:0,  //商品总件数
      // totalPrice:0,  //商品总价格
      allCheckFlag: false, //全选
      isAllType: false, //货品在库在途是否同时存在
      userId: "",
      tipTextShow: false,
      tipText: "",
      isEdit: false, //是否编辑
      itemImgNo: "../../static/imgs/img-wu.png",
      handleGoodTimer: null, // 监听商品更新
      handleSongTimer: null, // 监听赠品更新
      // 加载动画
      loadingFlag: false,
      message: "加载中",
    };
  },
  computed: {
    // 总数-Y
    totalCount: {
      get() {
        let total = 0;
        this.allShopingCartList.forEach((item) => {
          item.list.forEach((item2) => {
            item2.children.forEach((item3) => {
              if (item3.isCheck) {
                if (item3.diyType === 1) {
                  // 定制商品
                  total += item3.itemCount;
                } else {
                  total += item3.zaiKuCount + item3.zaiTuCount;
                }
              }
            });
          });
        });
        return total;
      },
      set(newValue) {
        return newValue;
      },
    },
    // 总金额-y
    totalPrice: {
      get() {
        let total = 0;
        this.allShopingCartList.forEach((item) => {
          item.list.forEach((item2) => {
            item2.children.forEach((item3) => {
              if (item3.isCheck && item3.itemType == 1) {
                total += Number(item3.lastPrice);
              }
            });
          });
        });
        return fomatFloat(total, 2);
      },
      set(newValue) {
        return newValue;
      },
    },

    // 总重量--Y
    totalWeight() {
      var total = 0;
      this.allShopingCartList.forEach((item) => {
        item.list.forEach((item2) => {
          item2.children.forEach((item3) => {
            if (item3.isCheck) {
              total +=
                (item3.weight != 0 ? item3.weight : 1) *
                (item3.zaiKuCount + item3.zaiTuCount);
            }
          });
        });
      });
      return total;
    },
  },
  onShow() {
    let userId = uni.getStorageSync("userId");
    if (userId && userId != "" && userId != "undefined") {
      this.userId = userId;
    }
    if (this.userId != "") {
      this.shoppingcartListFun();
    }
    this.allCheckFlag = false;
  },
  onLoad() {
    this.themeColor = uni.getStorageSync("themeColor");
  },
  // 触底分页
  onReachBottom() {
    // if(this.page<this.totalPage){
    // 	this.page+=1;
    // }
  },
  methods: {
    // 轻提示弹框
    tipFun(text) {
      let that = this;
      this.tipText = text;
      this.tipTextShow = true;
      setTimeout(function () {
        that.tipTextShow = false;
      }, 3000);
    },
    //返回
    toback() {
      uni.navigateBack({
        delta: 1,
      });
    },
    fomatFloat(num, n) {
      var f = parseFloat(num);
      if (isNaN(f)) {
        return false;
      }
      // 判断第三位是否是五
      let ls = Math.round(4.225 * 1000)
        .toString()
        .split(".");
      let threeNum = ls[0].charAt(ls[0].length - 1);
      if (threeNum == 5) {
        num = (num * 1000 + 1) / 1000;
      }
      f = Math.round(num * Math.pow(10, n)) / Math.pow(10, n); // n 幂
      var s = f.toString();
      var rs = s.indexOf(".");

      // 判定如果是整数，增加小数点再补0
      if (rs < 0) {
        rs = s.length;
        s += ".";
      }
      while (s.length <= rs + n) {
        s += "0";
      }
      return s;
    },
    // 获取购物车列表
    async shoppingcartListFun() {
      let that = this;
      this.loadingVal = true;
      this.totalCount = 0;
      this.totalPrice = 0;
      this.shopCartList = {
        allCkeck: false,
        diyType: 0,
        goodsType: 1,
        list: [],
        promoList: [],
        groundList: [],
      };
      // 定制还没做--暂时注释
      this.dzShopCartList = {
        allCkeck: false,
        goodsType: 2,
        list: [],
      };
      this.allShopingCartList = [];

      let res = await shoppingcartList();

      if (!res.success || res.data.length < 1) {
        this.loadingVal = false;
        return;
      }
      // let list=res.data.filter(item=>item.goodsType==1);
      let list = res.data;
      console.log("购物车列表：", list);
      let ids = [];
      for (let i = 0; i < list.length; i++) {
        this.$set(list[i], "kuCunShow", false);
        this.$set(list[i], "setInter", "");

        if (list[i].goodsType == 1) {
          ids.push(list[i].itemId);
        } else {
          //定制
          console.log("定制货品：", list[i]);
          if (list[i].diy) {
            let diyParams = {
              materialId: list[i].diy.materialId, //材质id
              modelId: list[i].diy.modelId, //型号id
            };
            let diyStockRes = await diyGetByModelIdAndMaterialId(diyParams);
            console.log("定制库存", diyStockRes.data);
            if (diyStockRes.success) {
              let diyStockData = diyStockRes.data;
              // openFlag 状态值 1、启用 0、禁用  underStockFlag  是否缺货 0否 1是
              this.$set(
                list[i],
                "diyUnderStockFlag",
                diyStockData.openFlag == 1 && diyStockData.underStockFlag == 0
                  ? 1
                  : 0
              );
              //定制添加是否缺货  diyUnderStockFlag  1：不缺  0：缺
              that.$set(list[i], "zaiKuCount", list[i].itemCount);
              that.$set(list[i], "zaiTuCount", 0);
            }
          }
        }
        // this.totalCount+=list[i].itemCount;  //商品总件数
      }
      // 普通商品获取库存--start
      let nesIds = [...new Set(ids)]; //去重
      let userId = uni.getStorageSync("userId");
      if (nesIds.length > 0) {
        let params = { distributorId: userId, itemIdList: nesIds };
        let stockRes = await listStockByCondition(params);
        // 在途的判断
        // let sysAutoDelivery= uni.getStorageSync('autoDelivery')  //分销商是否是直发客户：1 是，0 否
        // let sysOnWayFlag = uni.getStorageSync('onWayFlag');  //分销商是否支持在途库存 1是 0否 默认是1

        if (stockRes.success) {
          let stockResData = stockRes.data;
          for (let i = 0; i < stockResData.length; i++) {
            list.forEach((items, index) => {
              if (items.itemId === stockResData[i].itemId) {
                //onwaySaleFlag 直发客户是否支持在途：0-否 1-是
                // let onWayFlag=0;
                // if(sysAutoDelivery==1){
                // 	onWayFlag =(sysOnWayFlag==1?((items.onwaySaleFlag==1)?1:0):0);
                // }else{
                // 	onWayFlag =sysOnWayFlag;
                // }
                items.inStockUsableCount =
                  stockResData[i].inStockUsableCount > 0
                    ? stockResData[i].inStockUsableCount
                    : 0; // 在库库存
                items.onWayUsableCount =
                  stockResData[i].onWayUsableCount >
                  stockResData[i].inStockUsableCount
                    ? stockResData[i].onWayUsableCount -
                      stockResData[i].inStockUsableCount
                    : 0; // 在途库存
                let zaiTuCount =
                  stockResData[i].inStockUsableCount >= items.itemCount
                    ? 0
                    : items.itemCount - stockResData[i].inStockUsableCount;
                let zaiKuCount =
                  stockResData[i].inStockUsableCount >= items.itemCount
                    ? items.itemCount
                    : stockResData[i].inStockUsableCount;
                that.$set(items, "zaiKuCount", zaiKuCount);
                that.$set(items, "zaiTuCount", zaiTuCount);
              }
            });
          }
        }
      }
      // 获取库存--end

      let puList = [];
      let groupList = [];
      list = list.filter((item) => item.salePrice); //过滤掉没价格的货品
      // 处理货品价格--start
      list.forEach((item, index) => {
        that.$set(item, "isCheck", false);
        that.$set(item, "boxsList", []);
        that.$set(item, "count", item.itemCount);
        that.$set(item, "boxType", "件");
        that.$set(item, "boxNum", 1);
        // 处理装箱规格--start
        // if(item.boxs&&item.boxs.length>0){
        // 	item.boxs.push({boxType:'件',boxNum:1});
        // 	if(item.boxs[0].defaultFlag==1){
        // 		that.$set(item,'count',item.itemCount/item.boxs[0].boxNum);
        // 		that.$set(item,'boxType',item.boxs[0].boxType);
        // 		that.$set(item,'boxNum',item.boxs[0].boxNum);
        // 	}
        // }else{
        // 	that.$set(item,'boxs',{boxType:'件',boxNum:1,})
        // }
        // 处理装箱规格--end

        // 设置默认在途商品参与活动
        that.$set(item, "onWayFlag", 1);
        let lastPrice = item.salePrice * item.itemCount;
        let reduceOrPresent = 1; //促销统计方式：1满减 2满赠
        let reduceOrPresentId = ""; //活动条件id
        let presentCount = 0; //满赠数量
        //  计算拼团秒杀价格 --start
        if (item.groupSeckillId) {
          item.groupSeckills.forEach((groupItem) => {
            if (groupItem.groupSeckillId == item.groupSeckillId) {
              if (item.itemCount >= groupItem.minNum) {
                lastPrice = item.itemCount * groupItem.groupSeckillPrice;
                that.$set(item, "groupId", groupItem.groupSeckillId);
              }
            }
          });
        }
        //  计算拼团秒杀价格 --end

        // 活动价---start
        if (
          item.goodsPromotionId &&
          item.goodsPromotionId != "no" &&
          item.promotions
        ) {
          item.promotions.forEach((pItem) => {
            let onWayFlag = pItem.onWayFlag; //在途商品是否参与活动 1.参与，0.不参与
            let promoStatus = pItem.promoStatus; //促销状态：0 未开始, 1 促销中 2 已过期 3 提前结束
            let promoType = pItem.promoType; //活动类型，1 普通活动，2 阶梯活动
            pItem.rules.forEach((ruleItem) => {
              let isOneBuyCount =
                onWayFlag == 0 ? item.zaiKuCount : item.itemCount;
              let isOneBuyMoney =
                onWayFlag == 0
                  ? item.zaiKuCount * item.salePrice
                  : item.itemCount * item.salePrice;

              if (item.goodsPromotionId == ruleItem.id) {
                ruleItem.conditions.forEach((cItem) => {
                  if (promoStatus == 1) {
                    //moneyOrCount 规则形式：1金额 2数量
                    if (
                      (ruleItem.moneyOrCount == 1 &&
                        isOneBuyMoney >= cItem.oneBuyMoney) ||
                      (ruleItem.moneyOrCount == 2 &&
                        isOneBuyCount >= cItem.oneBuyCount)
                    ) {
                      that.$set(item, "rulesId", ruleItem.id);
                      that.$set(item, "conditionsId", cItem.id);
                      that.$set(item, "conditionsName", cItem.conditionName);

                      //specialFlag是否特价，1是 0否
                      if (cItem.specialFlag == 1) {
                        lastPrice =
                          onWayFlag == 0
                            ? item.zaiKuCount * cItem.specialPrice +
                              item.zaiTuCount * item.salePrice
                            : (item.zaiKuCount + item.zaiTuCount) *
                              cItem.specialPrice;
                      } else {
                        //不是特价
                        //reduceOrPresent 促销统计方式：1满减 2满赠

                        if (cItem.reduceOrPresent == 1) {
                          // reduceType 满减类型， 2折扣  1减免
                          if (cItem.reduceType == 2) {
                            let zheKouPrice = fomatFloat(
                              (item.salePrice * cItem.discount) / 100,
                              2
                            );
                            lastPrice =
                              onWayFlag == 0
                                ? item.zaiKuCount * zheKouPrice +
                                  item.zaiTuCount * item.salePrice
                                : item.itemCount * zheKouPrice;
                          } else {
                            // reduceOrPresent促销统计方式：1满减 2满赠,
                            let reduction = cItem.reduction;
                            // 判断是否叠加的减免金额
                            if (cItem.reductionPresentAddFlag == 1) {
                              reduction =
                                ruleItem.moneyOrCount == 1
                                  ? onWayFlag == 0
                                    ? item.zaiKuCount *
                                      item.salePrice *
                                      (cItem.reduction / cItem.oneBuyMoney)
                                    : item.itemCount *
                                      item.salePrice *
                                      (cItem.reduction / cItem.oneBuyMoney)
                                  : onWayFlag == 0
                                  ? item.zaiKuCount *
                                    (cItem.reduction / cItem.oneBuyCount)
                                  : item.itemCount *
                                    (cItem.reduction / cItem.oneBuyCount);
                            }

                            lastPrice =
                              onWayFlag == 0
                                ? item.zaiKuCount * item.salePrice -
                                  reduction +
                                  item.zaiTuCount * item.salePrice
                                : item.itemCount * item.salePrice - reduction;
                          }
                        } else {
                          // reduceOrPresent促销统计方式：1满减 2满赠,
                          reduceOrPresent = cItem.reduceOrPresent;
                          reduceOrPresentId = cItem.id;
                          presentCount = cItem.presentCount;
                          // 判断是否叠加
                          if (cItem.reductionPresentAddFlag == 1) {
                            //计算叠加的数量
                            let dieJiaNum =
                              ruleItem.moneyOrCount == 1
                                ? parseInt(isOneBuyMoney / cItem.oneBuyMoney)
                                : parseInt(isOneBuyCount / cItem.oneBuyCount);
                            presentCount = cItem.presentCount * dieJiaNum;
                          }
                        }
                      }
                    }
                  }
                });
              }
            });
          });
        }
        // 活动价---end
        that.$set(item, "lastPrice", lastPrice ? Number(lastPrice) : 0);
        that.$set(item, "reduceOrPresent", reduceOrPresent);
        that.$set(item, "reduceOrPresentId", reduceOrPresentId);
        that.$set(item, "presentCount", presentCount);

        // this.totalPrice+=item.lastPrice; //商品总价格
        if (item.goodsPromotionId) {
          puList.push(item);
        } else if (!item.groupSeckillId && !item.goodsPromotionId) {
          item.goodsPromotionId = "no";
          puList.push(item);
        }
        if (item.groupSeckillId) {
          groupList.push(item);
        }
      });
      // 处理货品价格--end

      console.log("普通：", puList);
      console.log("拼团活动groupList：", groupList);
      // 促销活动
      if (puList.length > 0) {
        let pList = puList.filter((item) => item.goodsType == 1); //普通活动
        // 把没有活动的商品排序到最前面
        let pList1 = []; //没活动的商品
        let pList2 = []; //有活动的商品
        let newPlist = []; //新组合
        pList.forEach((pItem) => {
          if (pItem.goodsPromotionId == "no") {
            pList1.push(pItem);
          } else {
            pList2.push(pItem);
          }
        });
        newPlist = [...pList1, ...pList2];
        let dList = puList.filter((item) => item.goodsType == 2); //定制
        that.shopCartList.list = [
          ...that.shopCartList.list,
          ...that.sort_pro(newPlist, ["goodsPromotionId"]),
        ];
        that.dzShopCartList.list = [
          ...that.dzShopCartList.list,
          ...that.sort_pro(dList, ["goodsPromotionId"]),
        ];
      }
      // 拼团秒杀
      if (groupList.length > 0) {
        let pList = groupList.filter((item) => item.goodsType == 1); //普通活动
        let dList = groupList.filter((item) => item.goodsType == 2); //定制
        // that.shopCartList.list=[...that.shopCartList.list,...that.sort_pro(groupList ,['groupSeckillId'])];
        that.shopCartList.list = [
          ...that.shopCartList.list,
          ...that.sort_pro(pList, ["groupSeckillId"]),
        ];
        that.dzShopCartList.list = [
          ...that.dzShopCartList.list,
          ...that.sort_pro(dList, ["goodsPromotionId"]),
        ];
      }
      console.log("LLL:", that.shopCartList);
      console.log("定制：", this.dzShopCartList);
      that.shopCartList.list.forEach((item) => {
        //  拼团活动
        if (item.children[0].groupSeckills) {
          item.children[0].groupSeckills.forEach((groupItem) => {
            if (groupItem.groupSeckillId == item.groupSeckillId) {
              that.$set(
                item,
                "ruleName",
                groupItem.name ? groupItem.name : "拼团活动"
              );
              that.$set(
                item,
                "ruleNameEn",
                groupItem.name ? groupItem.name : "拼团活动"
              );
              that.$set(item, "rules", groupItem);
            }
          });
        }

        // 判断是否有赠品 1满减 2满赠
        that.$set(item, "reduceOrPresent", 1);
        item.children.forEach((preItem) => {
          if (preItem.reduceOrPresent == 2) {
            that.$set(item, "reduceOrPresent", 2);
            that.$set(item, "reduceOrPresentId", preItem.reduceOrPresentId);
            that.$set(item, "presentCount", preItem.presentCount);
          }
        });

        //  促销活动
        if (item.children[0].promotions) {
          item.children[0].promotions.forEach((promotionItem) => {
            promotionItem.rules.forEach((rulItem) => {
              if (rulItem.id == item.goodsPromotionId) {
                that.$set(item, "ruleName", rulItem.ruleName);
                that.$set(item, "ruleNameEn", rulItem.ruleNameEn);
                that.$set(item, "rules", rulItem);
                // 在途不参与活动
                if (promotionItem.onWayFlag == 0) {
                  item.children.forEach((childrenItem) => {
                    that.$set(childrenItem, "onWayFlag", 0);
                    // this.$set(childrenItem,'zaiTuCount',0);
                  });
                }
              }
            });
          });
        }
      });
      that.loadingVal = false;
      that.allShopingCartList.push(that.shopCartList);
      this.allShopingCartList.push(that.dzShopCartList);
      console.log("最终购物车列表：", that.allShopingCartList);
    },
    // 把对象数组按照某一个属性（或某几个属性）进行分类-Y
    sort_pro(data, keys = []) {
      //keys可以传一个数组
      var c = [];
      var d = {};
      for (var element of data) {
        let element_keyStr = "";
        let element_key = [];
        let element_keyObj = {};
        for (var key of keys) {
          element_key.push(element[key]);
          element_keyObj[key] = element[key];
        }
        element_keyStr = element_key.join("_");
        if (!d[element_keyStr]) {
          c.push({
            ...element_keyObj,
            children: [element],
          });
          d[element_keyStr] = element;
        } else {
          for (var ele of c) {
            let isTrue = keys.some((key) => {
              return ele[key] != element[key];
            });
            if (!isTrue) {
              ele.children.push(element);
            }
          }
        }
      }
      return c;
    },

    // 切换活动弹框
    openActivityPopup(goodsList, goodsItem, goodsPromotionId) {
      console.log("打开--", goodsItem);
      let that = this;
      this.activityList = [];
      this.$refs.activityPopup.open();
      this.activityGoodsItem = goodsItem;
      this.activityGoodList = goodsList;
      let hasCheck = false; // 默认是否有选择活动
      if (goodsItem.promotions && goodsItem.promotions.length > 0) {
        goodsItem.promotions.forEach((item) => {
          item.rules.forEach((ruleItem) => {
            if (ruleItem.id == goodsPromotionId) {
              that.$set(ruleItem, "isCheck", true);
              hasCheck = true;
            } else {
              that.$set(ruleItem, "isCheck", false);
            }

            that.activityList.push(ruleItem);
          });
        });
      }
      if (goodsItem.groupSeckills && goodsItem.groupSeckills.length > 0) {
        goodsItem.groupSeckills.forEach((item) => {
          if (item.groupSeckillId == goodsItem.groupSeckillId) {
            that.$set(item, "isCheck", true);
          }
          that.activityList.push({
            id: item.groupSeckillId,
            ruleName: item.name,
            isCheck: item.isCheck,
            conditions: [],
          });
        });
      }
      this.activityList.push({
        ruleName: "不参与活动",
        isCheck: !hasCheck, // 有活动，不选中
      });
    },

    // 关闭活动弹框-Y
    closeActivityPopup() {
      this.$refs.activityPopup.close();
    },

    //单选活动-Y
    seleceActivityFun(item) {
      this.activityList.forEach((items) => {
        if (item.id == items.id) {
          this.$set(item, "isCheck", !item.isCheck);
        } else {
          this.$set(items, "isCheck", false);
        }
      });
    },

    // 切换活动确定-Y
    activityPopupConfirm() {
      let that = this;
      let promotionId = "";
      let goodItem = this.activityGoodsItem;
      this.activityGoodList.forEach((item) => {
        if (item.itemType == 2) {
          this.deletePresentsIds.push(item.id);
        }
      });

      this.activityList.forEach((items) => {
        if (items.isCheck) {
          promotionId = items.id;
        }
      });
      if (promotionId != "") {
        let promotionId2 = promotionId != "" ? promotionId : "";
        let params = {
          diyType: goodItem.diyType, //定制类型 0-标准定制 1-DIY定制
          goodsPromotionId: goodItem.promotions ? promotionId2 : "", //商品促销活动Id(活动条件id)
          goodsType: goodItem.goodsType, //商品类型 1-普通 2-定制
          groupSeckillId: goodItem.groupSeckills ? promotionId2 : "",
          itemCount: goodItem.itemCount, //购物数量
          itemType: goodItem.itemType, //是否赠品 1 非赠品 2 赠品
          id: goodItem.id,
        };
        //添加购物车
        ModifyShoppingcart(params).then((res) => {
          if (res.success) {
            that.closeActivityPopup();
            that.tipFun("切换活动成功!");
            if (that.deletePresentsIds.length > 0) {
              that.deletePresentFun(that.deletePresentsIds, "refreshCart");
            } else {
              that.shoppingcartListFun(); // 重新拉取购物车列表
            }
          } else {
            that.tipFun(res.errMessage);
          }
        });
      } else {
        that.tipFun("请选择要切换的活动!");
      }
    },

    //当有赠品的活动选择切换为不参与活动--- 删除赠品--即调用批量删除-Y
    deletePresentFun(ids, type) {
      deleteShoppingcart({ ids: ids }).then((res) => {
        if (res.success) {
          if (type && type === "refreshCart") {
            this.shoppingcartListFun(); // 重新拉取购物车列表
          }
        }
      });
    },
    // 打开活动规则-Y
    openGuizePopup(list) {
      console.log(list);
      this.$refs.guizePopup.open();
      this.acivityConditions = list;
    },
    // 关闭活动规则-Y
    closeGuizePopup() {
      this.$refs.guizePopup.close();
    },

    onClick(e) {
      console.log(
        "点击了" +
          (e.position === "left" ? "左侧" : "右侧") +
          e.content.text +
          "按钮"
      );
    },
    swipeChange(e, index) {
      console.log("当前状态：" + open + "，下标：" + index);
    },

    // 计数器 --Y
    numberChange(oldCount, childItem, item, conditions, type) {
      let that = this;
      console.log("当前行：", childItem);
      console.log("活动条件列表:", conditions);
      console.log("同类行集合：", item);
      if (type == 1 && oldCount <= 1) {
        return;
      } //零不能减
      if (childItem.itemType == 2) {
        return;
      } //赠品不能加减
      let numCount =
        type == 3 ? childItem.count : type == 1 ? oldCount - 1 : oldCount + 1;
      if (numCount <= 0) {
        childItem.count = childItem.itemCount;
        return;
      }
      console.log("input的值：", numCount);
      let lastPrice = childItem.salePrice * numCount; //货品价格
      let goodsPromotionId = "no";
      let groupSeckillId = "";
      let reduceOrPresent = 1; //是否是赠品  2：是  1：否
      let reduceOrPresentId = "";
      let presentCount = 0;
      this.presentCount = 0;
      let conditionsId = "";
      let zaiKuCount =
        numCount <= childItem.inStockUsableCount
          ? numCount
          : childItem.inStockUsableCount; //在库下单数量
      let zaiTuCount =
        numCount <= childItem.inStockUsableCount
          ? 0
          : numCount - childItem.inStockUsableCount; //在途下单数量
      // 普通商品
      if (childItem.goodsType == 1) {
        // 拼团
        if (childItem.groupSeckillId && childItem.groupSeckillId != "") {
          childItem.groupSeckills.forEach((groupItem) => {
            if (
              childItem.groupSeckillId == groupItem.groupSeckillId &&
              groupItem.groupSeckillStatus == 1
            ) {
              if (numCount > groupItem.minNum) {
                lastPrice = numCount * groupItem.groupSeckillPrice;
                if (
                  numCount >
                  childItem.inStockUsableCount + childItem.onWayUsableCount
                ) {
                  // 是否支持超卖或预售 1、支持 0、不支持
                  if (groupItem.mtoFlag == 1) {
                    if (
                      groupItem.maxNum &&
                      childItem.maxNum != 0 &&
                      numCount > groupItem.maxNum
                    ) {
                      that.$set(childItem, "itemCount", oldCount);
                      that.$set(childItem, "count", oldCount);
                      let title =
                        "货品订购数量不能大于拼团限购量：" + groupItem.maxNum;

                      that.tipFun(title);
                      return;
                    }
                  } else {
                    that.$set(childItem, "itemCount", oldCount);
                    that.$set(childItem, "count", oldCount);
                    that.tipFun("购买数量不能大于库存!");
                    return;
                  }
                }
              }
            }
          });
        } else {
          if (
            numCount >
            childItem.inStockUsableCount + childItem.onWayUsableCount
          ) {
            that.$set(childItem, "itemCount", oldCount);
            that.$set(childItem, "count", oldCount);
            that.tipFun("购买数量不能大于库存!");
            return;
          }
        }
        // 促销活动
        if (childItem.goodsPromotionId && childItem.goodsPromotionId != "no") {
          if (childItem.promotions) {
            childItem.promotions.forEach((promoItem) => {
              let onWayFlag = promoItem.onWayFlag; //在途商品是否参与活动 1.参与，0.不参与
              let promoStatus = promoItem.promoStatus; //促销状态：0 未开始, 1 促销中 2 已过期 3 提前结束
              let promoType = promoItem.promoType; //活动类型，1 普通活动，2 阶梯活动
              promoItem.rules.forEach((ruleItem) => {
                if (
                  childItem.goodsPromotionId == ruleItem.id &&
                  promoStatus == 1
                ) {
                  let isOneBuyCount = onWayFlag == 0 ? zaiKuCount : numCount;
                  let isOneBuyMoney =
                    onWayFlag == 0
                      ? zaiKuCount * childItem.salePrice
                      : numCount * childItem.salePrice;
                  //货品活动是否累计 addUpFlag，1是 0否( ruleType 规则对象是2或3时有效)
                  if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
                    let childrenList = item.children;
                    let zCoun = zaiKuCount;
                    let tCount = zaiTuCount;
                    let zPrice = zaiKuCount * childItem.salePrice;
                    let tPrice = zaiTuCount * childItem.salePrice;
                    for (let i = 0; i < childrenList.length; i++) {
                      if (
                        childrenList[i].isCheck &&
                        childrenList[i].itemId != childItem.itemId &&
                        childrenList[i].itemType != 2
                      ) {
                        zCoun += childrenList[i].zaiKuCount;
                        tCount += childrenList[i].zaiTuCount;
                        zPrice +=
                          childrenList[i].zaiKuCount *
                          childrenList[i].salePrice;
                        tPrice +=
                          childrenList[i].zaiTuCount *
                          childrenList[i].salePrice;
                      }
                    }
                    isOneBuyCount = onWayFlag == 0 ? zCoun : zCoun + tCount;
                    isOneBuyMoney = onWayFlag == 0 ? zPrice : zPrice + tPrice;
                  }
                  // 是否累计 --END
                  ruleItem.conditions.forEach((cItem) => {
                    //moneyOrCount 规则形式：1金额 2数量
                    if (
                      (ruleItem.moneyOrCount == 1 &&
                        isOneBuyMoney >= cItem.oneBuyMoney) ||
                      (ruleItem.moneyOrCount == 2 &&
                        isOneBuyCount >= cItem.oneBuyCount)
                    ) {
                      goodsPromotionId = ruleItem.id;
                      conditionsId = cItem.id;
                      // specialFlag 是否特价，1是 0否
                      if (cItem.specialFlag == 1) {
                        lastPrice =
                          onWayFlag == 0
                            ? zaiKuCount * cItem.specialPrice +
                              zaiTuCount * childItem.salePrice
                            : numCount * cItem.specialPrice;
                        //  活动累计情况下，勾选上的货品都按特价计算
                        if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
                          let childrenList = item.children;
                          for (let i = 0; i < childrenList.length; i++) {
                            if (
                              childrenList[i].isCheck &&
                              childrenList[i].itemId != childItem.itemId
                            ) {
                              let childrenTotalPrice =
                                onWayFlag == 0
                                  ? childrenList[i].zaiKuCount *
                                      cItem.specialPrice +
                                    childrenList[i].zaiTuCount *
                                      childrenList[i].salePrice
                                  : childrenList[i].itemCount *
                                    cItem.specialPrice;
                              that.$set(
                                childrenList[i],
                                "lastPrice",
                                Number(childrenTotalPrice)
                              );
                              that.$set(
                                childrenList[i],
                                "conditionsId",
                                cItem.id
                              );
                            }
                          }
                        }
                      } else {
                        //reduceOrPresent 促销统计方式：1满减 2满赠
                        if (cItem.reduceOrPresent == 1) {
                          // reduceType 满减类型， 1减免 2折扣
                          if (cItem.reduceType == 1) {
                            //  活动累计情况下，勾选上的货品都按减免计算
                            if (
                              ruleItem.addUpFlag == 1 &&
                              ruleItem.ruleType != 1
                            ) {
                              let childrenList = item.children;
                              for (let i = 0; i < childrenList.length; i++) {
                                if (childrenList[i].isCheck) {
                                  let reduction2 = cItem.reduction;
                                  if (cItem.reductionPresentAddFlag == 1) {
                                    reduction2 =
                                      ruleItem.moneyOrCount == 1
                                        ? onWayFlag == 0
                                          ? childrenList[i].zaiKuCount *
                                            childrenList[i].salePrice *
                                            (cItem.reduction /
                                              cItem.oneBuyMoney)
                                          : childrenList[i].itemCount *
                                            childrenList[i].salePrice *
                                            (cItem.reduction /
                                              cItem.oneBuyMoney)
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
                                  let childrenTotalPrice = fomatFloat(
                                    childrenList[i].salePrice *
                                      childrenList[i].itemCount -
                                      reduction2,
                                    2
                                  );
                                  if (childrenList[i].itemId == item.itemId) {
                                    lastPrice = childrenTotalPrice;
                                    totalPrice = childrenTotalPrice;
                                  }
                                  that.$set(
                                    childrenList[i],
                                    "lastPrice",
                                    childrenTotalPrice
                                  );
                                  that.$set(
                                    childrenList[i],
                                    "conditionsId",
                                    cItem.id
                                  );
                                }
                              }
                            } else {
                              //减免金额
                              let reduction = cItem.reduction;
                              // 判断是否叠加的减免金额
                              if (cItem.reductionPresentAddFlag == 1) {
                                reduction =
                                  ruleItem.moneyOrCount == 1
                                    ? onWayFlag == 0
                                      ? zaiKuCount *
                                        childItem.salePrice *
                                        (cItem.reduction / cItem.oneBuyMoney)
                                      : numCount *
                                        childItem.salePrice *
                                        (cItem.reduction / cItem.oneBuyMoney)
                                    : onWayFlag == 0
                                    ? zaiKuCount *
                                      (cItem.reduction / cItem.oneBuyCount)
                                    : numCount *
                                      (cItem.reduction / cItem.oneBuyCount);
                              }
                              lastPrice = fomatFloat(
                                Number(
                                  numCount * childItem.salePrice - reduction
                                ),
                                2
                              );
                              if (ruleItem.ruleType == 1) {
                                let orserIndex = 0;
                                item.children.forEach(
                                  (orderItem, orserIndex) => {
                                    console.log(orserIndex);
                                    if (
                                      orderItem.itemId != item.itemId &&
                                      orderItem.goodsPromotionId ==
                                        item.goodsPromotionId
                                    ) {
                                      this.$set(
                                        orderItem,
                                        "lastPrice",
                                        Number(
                                          orderItem.salePrice *
                                            orderItem.itemCount
                                        )
                                      );
                                    }
                                  }
                                );
                              }
                            }
                          } else {
                            //折扣
                            let zheKouPrice = fomatFloat(
                              (childItem.salePrice * cItem.discount) / 100,
                              2
                            );
                            lastPrice =
                              onWayFlag == 0
                                ? zaiKuCount * zheKouPrice +
                                  zaiTuCount * childItem.salePrice
                                : numCount * zheKouPrice;
                            //  活动累计情况下，勾选上的货品都按减免计算
                            if (
                              ruleItem.addUpFlag == 1 &&
                              ruleItem.ruleType != 1
                            ) {
                              let childrenList = item.children;
                              for (let i = 0; i < childrenList.length; i++) {
                                if (
                                  childrenList[i].isCheck &&
                                  childrenList[i].itemId != childItem.itemId
                                ) {
                                  let zheKouPrice2 = fomatFloat(
                                    (childrenList[i].salePrice *
                                      cItem.discount) /
                                      100,
                                    2
                                  );
                                  let childrenTotalPrice =
                                    onWayFlag == 0
                                      ? childrenList[i].zaiKuCount *
                                          zheKouPrice2 +
                                        childrenList[i].zaiTuCount *
                                          childrenList[i].salePrice
                                      : childrenList[i].itemCount *
                                        zheKouPrice2;
                                  that.$set(
                                    childrenList[i],
                                    "lastPrice",
                                    childrenTotalPrice
                                  );
                                  that.$set(
                                    childrenList[i],
                                    "conditionsId",
                                    cItem.id
                                  );
                                }
                              }
                            }
                          }
                        } else {
                          //赠品
                          reduceOrPresent = 2;
                          reduceOrPresentId = cItem.id;
                          presentCount = cItem.presentCount;
                          // 判断是否叠加
                          if (cItem.reductionPresentAddFlag == 1) {
                            //计算叠加的数量
                            let dieJiaNum =
                              ruleItem.moneyOrCount == 1
                                ? parseInt(isOneBuyMoney / cItem.oneBuyMoney)
                                : parseInt(isOneBuyCount / cItem.oneBuyCount);
                            presentCount = cItem.presentCount * dieJiaNum;
                            this.presentCount += presentCount;
                          } else {
                            this.presentCount = presentCount;
                          }
                        }
                      }
                    }
                  });
                }
              });
            });
          }
        }
        that.$set(childItem, "zaiKuCount", zaiKuCount);
        that.$set(childItem, "zaiTuCount", zaiTuCount);
        that.$set(childItem, "itemCount", numCount);
        that.$set(childItem, "count", numCount);
        that.$set(childItem, "lastPrice", Number(lastPrice));
        that.$set(childItem, "conditionsId", conditionsId);
        that.$set(childItem, "reduceOrPresent", reduceOrPresent);
        that.$set(childItem, "reduceOrPresentId", reduceOrPresentId);
        that.$set(childItem, "presentCount", presentCount);
        that.$set(item, "reduceOrPresent", reduceOrPresent);
        that.$set(item, "reduceOrPresentId", reduceOrPresentId);
        that.$set(item, "presentCount", presentCount);
      } else {
        //定制商品
        console.log("定制商品", childItem);
        let count = Number(numCount);
        if (childItem.diyUnderStockFlag == 1) {
          //不缺货
          that.$set(childItem, "zaiKuCount", count);
          that.$set(childItem, "zaiTuCount", 0);
          that.$set(childItem, "itemCount", numCount);
          that.$set(childItem, "count", numCount);
          that.$set(childItem, "lastPrice", Number(lastPrice));
        } else {
          //缺货

          that.tipFun("购买数量不能大于库存!");
          return;
        }
      }
      clearTimeout(this.handleGoodTimer);
      this.handleGoodTimer = setTimeout(() => {
        this.loadingFlag = true;
        that.ModifyShoppingcartFun(
          childItem,
          goodsPromotionId,
          groupSeckillId,
          numCount
        );
        let ids = [];
        item.children.forEach((presentItem) => {
          if (presentItem.reduceOrPresent == 2) {
            reduceOrPresent = 2;
          }
        });
        item.children.forEach((gItem, gIndex) => {
          if (goodsPromotionId == "no" && gItem.itemType == 2) {
            ids.push(gItem.id);
            item.children.splice(gIndex, 1); //前端删除赠品
          }
        });
        if (ids.length > 0) {
          that.deleteShoppingcartFun(ids);
        }

        // 赠品活动
        if (reduceOrPresent == 2) {
          clearTimeout(this.handleSongTimer);
          this.handleSongTimer = setTimeout(() => {
            that.openGifts(
              item.reduceOrPresentId,
              item.rules.conditions,
              item,
              "handleChange"
            );
          }, 300);
        } else {
          this.loadingFlag = false;
        }
      }, 300);
    },

    // 修改购物列表单项
    ModifyShoppingcartFun(
      childItem,
      goodsPromotionId,
      groupSeckillId,
      numCount
    ) {
      let params = {
        diyType: childItem.diyType, //定制类型 0-标准定制 1-DIY定制
        goodsPromotionId: goodsPromotionId != "no" ? goodsPromotionId : "", //商品促销活动Id(活动条件id)
        goodsType: childItem.goodsType, //商品类型 1-普通 2-定制
        groupSeckillId: groupSeckillId != "no" ? groupSeckillId : "",
        itemCount: numCount, //购物数量
        itemType: childItem.itemType, //是否赠品 1 非赠品 2 赠品
        id: childItem.id,
      };
      //修改购物车
      ModifyShoppingcart(params).then((res) => {
        if (res.success) {
        }
      });
    },

    // 批量删除购物车
    deleteShoppingcartFun(ids) {
      deleteShoppingcart({ ids: ids }).then((res) => {
        if (res.success) {
          // this.shoppingcartListFun();
        }
      });
    },

    open() {
      // 通过组件定义的ref调用uni-popup方法
      this.$refs.popup.open();
    },
    closePopup() {
      // 通过组件定义的ref调用uni-popup方法
      this.$refs.popup.close();
    },

    // 打开-选择赠品
    async openGifts(reduceOrPresentId, conditions, goodItem, type) {
      console.log("条件id", reduceOrPresentId);
      console.log("活动条件列表", conditions);
      console.log("货品列表", goodItem);

      this.presentsPromotionId = goodItem.goodsPromotionId;
      // this.presentCount = goodItem.presentCount;
      this.selectSongCount = 0;
      this.presentCount = 0;
      for (let i = 0; i < goodItem.children.length; i++) {
        if (goodItem.children[i].itemType == 2) {
          this.selectSongCount += goodItem.children[i].itemCount;
        } else {
          this.presentCount += goodItem.children[i].presentCount;
        }
      }
      let res = await promotionPresentList({ id: reduceOrPresentId });
      let ids = [];
      // let flag=false;
      if (res.success) {
        console.log("赠品列表", res.data);
        res.data.forEach((item) => {
          this.$set(item, "num", 0);
          ids.push(item.itemId);
          // 已选赠品数量回显
          goodItem.children.forEach((good) => {
            if (good.itemType == 2 && good.itemCode === item.itemCode) {
              item.num = good.itemCount;
              item.goodsPromotionId = good.goodsPromotionId;
            }
          });
        });
        this.presentList = res.data;
      }

      let nesIds = [...new Set(ids)]; //去重
      let userId = uni.getStorageSync("userId");
      let stockRes = await listStockByCondition({
        distributorId: userId,
        itemIdList: nesIds,
      }); //库存
      if (this.presentList.length > 0) {
        this.presentList.forEach((item) => {
          this.$set(item, "itemCount", item.num);
          stockRes.data.forEach((item2) => {
            if (item.itemId == item2.itemId) {
              this.$set(
                item,
                "stockNum",
                item2.inStockUsableCount + (item2.numOnWaySum || 0)
              );
            }
          });
        });
      }

      // 更新赠品数量
      if (type && type === "handleChange") {
        this.handleSongNum(goodItem);
      } else {
        // 赠品弹窗显示
        this.loadingFlag = false;
        this.$refs.giftsPopup.open();
      }
    },

    // 更新赠品数量
    handleSongNum(goodList) {
      let presentCount = this.presentCount; // 可选赠品数
      let selectSongCount = 0; // 已选赠品数
      let presentStockNum = 0; // 赠品库存总数
      let presentList = []; // 赠品列表
      for (let i = 0; i < goodList.children.length; i++) {
        if (goodList.children[i].itemType == 2) {
          selectSongCount += goodList.children[i].itemCount;

          let kucunCount =
            goodList.children[i].inStockUsableCount +
            goodList.children[i].onWayUsableCount;
          presentStockNum += kucunCount;
          goodList.children[i].kucunCount = kucunCount;
          presentList.push(goodList.children[i]);
        }
      }

      let yuCount = presentCount - selectSongCount; // 需新增赠品数
      let hasChange = false;
      // 判断可选赠品数大于0且可选赠品数跟已选赠品数不相等
      if (presentCount > 0 && presentCount != selectSongCount) {
        // 判断可选数量与赠品库存总数
        if (presentCount >= presentStockNum) {
          // 超出总库存，分配给所有赠品
          presentList.forEach((pre) => {
            pre.itemCount = pre.kucunCount;
          });
        } else {
          // 低于，判断赠品列表单个库存与可选赠品数，默认给第一个，不足分配给下一个
          presentList.forEach((pre) => {
            if (yuCount > 0) {
              hasChange = true;
              // 剩余库存数大于或等于需新增赠品数
              if (pre.itemCount + yuCount <= pre.kucunCount) {
                pre.itemCount = pre.itemCount + yuCount;
                yuCount -= yuCount;
              } else {
                let num = pre.itemCount + yuCount - pre.kucunCount;
                pre.itemCount = pre.kucunCount;
                yuCount = num;
              }
            } else if (yuCount < 0) {
              hasChange = true;
              if (pre.itemCount + yuCount <= 0) {
                let num = pre.itemCount + yuCount;
                pre.itemCount = 0;
                yuCount = num;
              } else {
                let num = pre.itemCount + yuCount;
                pre.itemCount = num;
                yuCount -= yuCount;
              }
            }
          });
        }

        // 更新购物车赠品数量
        if (hasChange && presentList.length > 0) {
          let ids = []; // 删除赠品
          // 已修改赠品列表
          presentList.forEach((pre) => {
            if (pre.itemCount > 0) {
              let songParams = {
                diyType: pre.diyType, //定制类型 0-标准定制 1-DIY定制
                goodsPromotionId:
                  pre.goodsPromotionId &&
                  pre.goodsPromotionId != "no" &&
                  pre.goodsPromotionId != ""
                    ? pre.goodsPromotionId
                    : "", //商品促销活动Id(活动条件id)
                goodsType: pre.goodsType, //商品类型 1-普通 2-定制
                groupSeckillId: "",
                itemCount: pre.itemCount, //购物数量
                itemType: pre.itemType, //是否赠品 1 非赠品 2 赠品
                id: pre.id,
              };
              // 修改购物车
              ModifyShoppingcart(songParams).then((res) => {
                if (res.success) {
                }
              });
            } else {
              ids.push(pre.id);
            }
          });

          if (ids.length > 0) {
            // 删除赠品
            this.deleteShoppingcartFun(ids);
          }

          setTimeout(() => {
            this.loadingFlag = false;
            this.shoppingcartListFun(); // 重新拉取购物车列表
          }, 1000);
        } else {
          this.loadingFlag = false;
        }
      } else {
        this.loadingFlag = false;
      }
    },

    // 赠品弹框加减-y
    giftHandleChange(count, item, type) {
      if (type == 1 && Number(item.num) <= 0) {
        return;
      }

      type == 3
        ? (this.selectSongCount =
            this.selectSongCount + (Number(item.num) - item.itemCount))
        : type == 1
        ? (this.selectSongCount -= 1)
        : (this.selectSongCount += 1);

      // 判断已选数量
      if (this.selectSongCount > 0) {
        // 大于0
        if (
          Number(item.num) < item.stockNum &&
          this.selectSongCount <= this.presentCount
        ) {
          type == 3
            ? (item.num = item.num)
            : type == 1
            ? (item.num -= 1)
            : (item.num += 1);
          this.$set(item, "num", Number(item.num));
          this.$set(item, "itemCount", Number(item.num));
        } else {
          if (type == 3) {
            this.$set(item, "num", Number(item.itemCount));
            this.$set(item, "itemCount", Number(item.num));
          }

          this.tipFun("已超出最多可选的赠品数");
        }
      } else {
        // 小于或等于0，设置为0
        this.$set(item, "num", 0);
        this.$set(item, "itemCount", Number(item.num));
      }

      let selectSongCount = 0;
      this.presentList.forEach((song) => {
        selectSongCount += song.num;
      });
      this.selectSongCount = selectSongCount;
    },
    // 提交确认赠品-y
    confirmGiftShop() {
      let that = this;
      let list = that.presentList;
      let presentsList = [];
      list.forEach((item) => {
        if (item.itemCount > 0) {
          let objL = {
            imageUrl: item.imageUrl1,
            cartType: 1, //加购类型：1 分销商 2 C端客户
            diyType: 1, //定制类型 0-标准定制 1-DIY定制
            goodsId: item.goodsId, //商品id
            goodsName: item.goodsName,
            goodsNo: item.goodsNo, //商品编码
            goodsPromotionId: that.presentsPromotionId, //商品促销活动Id(活动条件id)
            goodsType: 1, //商品类型 1-普通 2-定制
            itemCode: item.itemCode, //货品编码
            itemCount: item.itemCount, //购物数量
            itemNum: item.itemCount, //购物数量
            itemId: item.itemId, //货品id
            itemName: item.itemName, //货品名称
            itemType: 2, //是否赠品 1 非赠品 2 赠品
            specsName: item.specsName, //货品规格
            colorName: item.colorName, //货品颜色
            weight: item.weight, //重量
            lenght: item.length,
            width: item.width,
            height: item.height,
            barCode: item.barCode,
          };

          presentsList.push(objL);
        }
      });

      let changedPreList = []; // 已修改赠品
      let addPreList = []; // 新增赠品
      let ids = []; // 需删除赠品id
      that.allShopingCartList.forEach((cart) => {
        if (cart.goodsType === 1 && cart.list.length > 0) {
          cart.list.forEach((item) => {
            // 购物车参与活动货品
            if (item.goodsPromotionId && item.goodsPromotionId !== "no") {
              item.children.forEach((child) => {
                // 已选赠品
                presentsList.forEach((present) => {
                  // 赠品弹窗已选货品编码与购物车赠品货品编码匹配
                  if (
                    child.itemType == 2 &&
                    present.itemCode === child.itemCode &&
                    present.goodsPromotionId === child.goodsPromotionId
                  ) {
                    present.id = child.id;
                    present.itemCount = present.itemCount - child.itemCount; // 需减去购物车已选数量，以免超出可选总数

                    changedPreList.push(present);
                  }

                  // 赠品弹窗已选货品编码与购物车赠品货品编码不匹配
                  if ((present.itemCode !== child.itemCode) || (child.itemType != 2 && present.itemCode === child.itemCode)) {
                    addPreList.push(present);
                  }
                });

                // 赠品列表
                list.forEach((song) => {
                  // 判断赠品弹窗列表货品编码与购物车赠品货品编码是否匹配，并且数量是否为0
                  if (
                    child.itemType == 2 &&
                    song.itemCode === child.itemCode &&
                    song.goodsPromotionId === child.goodsPromotionId &&
                    (song.itemCount == 0 || song.num == 0)
                  ) {
                    ids.push(child.id);
                  }
                });
              });
            }
          });
        }
      });

      // 数组去重
      changedPreList = [...new Set(changedPreList)];
      addPreList = [...new Set(addPreList)];

      // 判断赠品已选数量与赠品可选数量
      if (that.selectSongCount <= that.presentCount) {
        // 判断是否选择赠品
        if (presentsList.length > 0) {
          // 新增赠品
          if (addPreList.length > 0) {
            // 加入购物车
            addShoppingcart(presentsList).then((res) => {
              if (res.success) {
                that.tipFun("加入购物车成功");
                that.$refs.giftsPopup.close();

                // 判断已修改赠品
                if (changedPreList.length > 0) {
                  // 修改购物车
                  changedPreList.forEach((item) => {
                    if (item.itemNum > 0) {
                      let params = {
                        diyType: item.diyType, //定制类型 0-标准定制 1-DIY定制
                        goodsPromotionId:
                          item.goodsPromotionId &&
                          item.goodsPromotionId != "no" &&
                          item.goodsPromotionId != ""
                            ? item.goodsPromotionId
                            : "", //商品促销活动Id(活动条件id)
                        goodsType: item.goodsType, //商品类型 1-普通 2-定制
                        groupSeckillId:
                          item.groupSeckillId != "no"
                            ? item.groupSeckillId
                            : "",
                        itemCount: item.itemNum, //购物数量
                        itemType: item.itemType, //是否赠品 1 非赠品 2 赠品
                        id: item.id,
                      };
                      ModifyShoppingcart(params).then((res) => {
                        if (res.success) {
                          that.$refs.giftsPopup.close();
                        }
                      });
                    }
                  });

                  // 删除赠品
                  if (ids.length > 0) {
                    that.deletePresentFun(ids, "refreshCart");
                  } else {
                    that.shoppingcartListFun(); // 重新拉取购物车列表
                  }
                } else {
                  // 无新增，无修改
                  that.$refs.giftsPopup.close();
                  // 删除赠品
                  if (ids.length > 0) {
                    that.deletePresentFun(ids, "refreshCart");
                  } else {
                    that.shoppingcartListFun(); // 重新拉取购物车列表
                  }
                }
              } else {
                that.tipFun(res.errMessage);
              }
            });
          } else {
            // 判断已修改赠品
            if (changedPreList.length > 0) {
              // 修改购物车
              changedPreList.forEach((item) => {
                if (item.itemNum > 0) {
                  let params = {
                    diyType: item.diyType, //定制类型 0-标准定制 1-DIY定制
                    goodsPromotionId:
                      item.goodsPromotionId &&
                      item.goodsPromotionId != "no" &&
                      item.goodsPromotionId != ""
                        ? item.goodsPromotionId
                        : "", //商品促销活动Id(活动条件id)
                    goodsType: item.goodsType, //商品类型 1-普通 2-定制
                    groupSeckillId:
                      item.groupSeckillId != "no" ? item.groupSeckillId : "",
                    itemCount: item.itemNum, //购物数量
                    itemType: item.itemType, //是否赠品 1 非赠品 2 赠品
                    id: item.id,
                  };
                  ModifyShoppingcart(params).then((res) => {
                    if (res.success) {
                      that.$refs.giftsPopup.close();
                    }
                  });
                }
              });

              // 删除赠品
              if (ids.length > 0) {
                that.deletePresentFun(ids, "refreshCart");
              } else {
                that.shoppingcartListFun(); // 重新拉取购物车列表
              }
            } else {
              // 无新增，无修改
              that.$refs.giftsPopup.close();
              // 删除赠品
              if (ids.length > 0) {
                that.deletePresentFun(ids, "refreshCart");
              } else {
                that.shoppingcartListFun(); // 重新拉取购物车列表
              }
            }
          }
        } else {
          // 未选赠品
          uni.showModal({
            title: "确定不需要赠品",
            content: "您还未选择赠品，可以取消重新选择赠品",
            success: function (mRes) {
              if (mRes.confirm) {
                // 删除赠品
                if (ids.length > 0) {
                  that.deletePresentFun(ids, "refreshCart");
                }
                that.$refs.giftsPopup.close();
              } else if (mRes.cancel) {
                console.log("用户点击取消");
              }
            },
          });
        }
      } else {
        that.tipFun("选择的赠品数量超出了最多可领取的件数");
      }
    },
    // 关闭赠品-y
    closeGiftsPopup() {
      this.$refs.giftsPopup.close();
    },
    // 删除购物车--Y
    deleteShopCarItems(id, shopItem) {
      let that = this;
      let list = shopItem.list;
      let info = "此操作将删除该货品, 是否继续?";
      uni.showModal({
        title: "删除提示",
        content: info,
        success: function (mRes) {
          if (mRes.confirm) {
            deleteShoppingcartOne({ id: id }).then((res) => {
              if (res.success) {
                that.tipFun("删除成功！");
                // 删除货品
                list.forEach((item, Gindex) => {
                  // 还一个货品的时候，直接删除一个分类
                  if (item.children.length == 1) {
                    list.splice(Gindex, 1);
                  } else {
                    item.children.forEach((items, index) => {
                      if (items.id == id) {
                        item.children.splice(index, 1);
                      }
                    });
                  }
                });
              }
            });
          } else if (mRes.cancel) {
            console.log("用户点击取消");
          }
        },
      });
    },

    // 批量删除
    deleteMoreFun() {
      let that = this;
      let list = this.allShopingCartList;
      console.log(list);
      let ids = [];
      list.forEach((item) => {
        item.list.forEach((items) => {
          items.children.forEach((item3) => {
            if (item3.isCheck) {
              ids.push(item3.id);
            }
          });
        });
      });
      if (ids.length > 0) {
        console.log(ids);
        let info = "此操作将删除选中货品, 是否继续?";
        uni.showModal({
          title: "删除提示",
          content: info,
          success: function (mRes) {
            if (mRes.confirm) {
              deleteShoppingcart({ ids: ids }).then((res) => {
                if (res.success) {
                  that.tipFun("删除成功！");
                  that.shoppingcartListFun();
                }
              });
            } else if (mRes.cancel) {
              console.log("用户点击取消");
            }
          },
        });
      }
    },

    //全选
    selectAllItemFun() {
      let that = this;
      let shopList = this.allShopingCartList;
      this.allCheckFlag = !this.allCheckFlag;
      console.log("购物车全选货品：", shopList);
      shopList.forEach((item1) => {
        item1.list.forEach((item2) => {
          // 处理活动
          let isOneBuyCount = 0;
          let isOneBuyPrice = 0;
          let presentIds = []; //赠品列表
          // let reduceOrPresent=1;  //是否是赠品 1：否  2：是
          // let reduceOrPresentId='';
          // let presentCount=0;
          let conditionsId = "";
          console.log(item2.children);
          item2.children.forEach((item) => {
            // 标品判断库存  定制判断是否缺货  diyUnderStockFlag
            let zaiKuFlag =
              item.goodsType == 1
                ? item.inStockUsableCount >= item.zaiKuCount
                  ? true
                  : false
                : item.diyUnderStockFlag == 1
                ? true
                : false;
            let zaiTuFlag =
              item.goodsType == 1
                ? item.onWayUsableCount >= item.zaiTuCount
                  ? true
                  : false
                : item.diyUnderStockFlag == 1
                ? true
                : false;
            if (item.openFlag == 1) {
              if (zaiKuFlag && zaiTuFlag) {
                // 有库存
                that.$set(item, "isCheck", that.allCheckFlag);
                that.$set(item1, "allCkeck", that.allCheckFlag);
              } else {
                if (that.isEdit) {
                  // 编辑状态
                  that.$set(item, "isCheck", that.allCheckFlag);
                  that.$set(item1, "allCkeck", that.allCheckFlag);
                } else {
                  that.$set(item, "kuCunShow", true);
                  item.setInter = setInterval(function () {
                    that.$set(item, "kuCunShow", false);
                  }, 2000);

                  if (item.isCheck) {
                    // 无库存且已被选，设置反选
                    that.$set(item, "isCheck", !that.allCheckFlag);
                  }
                  that.$set(item1, "allCkeck", !that.allCheckFlag);
                  return;
                }
              }
            }

            // 只累加不是赠品的货品
            if (item.itemType == 1 && item.isCheck) {
              //itemType:是否赠品 1 非赠品 2 赠品
              //  onWayFlag:在途商品是否参与活动 1.参与，0.不参与
              isOneBuyCount +=
                item.onWayFlag == 1 ? item.itemCount : item.zaiKuCount;
              isOneBuyPrice +=
                item.onWayFlag == 1
                  ? item.itemCount * item.salePrice
                  : item.zaiKuCount * item.salePrice;
            }
            if (item.itemType == 2) {
              presentIds.push(item.id);
            }

            if (item2.goodsPromotionId && item2.goodsPromotionId != "no") {
              item2.rules.conditions.forEach((cItem) => {
                //addUpFlag是否累计，1是 0否( 规则对象是2或3时有效)
                if (item2.rules.addUpFlag == 1) {
                  let oneBuyMoneyFlag =
                    item2.rules.moneyOrCount == 1 &&
                    isOneBuyPrice >= cItem.oneBuyMoney;
                  let oneBuyCountFlag =
                    item2.rules.moneyOrCount == 2 &&
                    isOneBuyCount >= cItem.oneBuyCount;
                  if (oneBuyMoneyFlag || oneBuyCountFlag) {
                    // if(cItem.reduceOrPresent==2){
                    // 	reduceOrPresent=2;
                    // 	reduceOrPresentId=cItem.id;
                    // 	presentCount=cItem.presentCount;
                    // }
                    // 勾选上的货品都加上规则条件id
                    item2.children.forEach((gItem2) => {
                      if (gItem2.isCheck) {
                        that.$set(gItem2, "conditionsId", cItem.id);
                        conditionsId = cItem.id;
                      }
                    });
                  }
                } else {
                  let NisOneBuyCount =
                    item.onWayFlag == 1 ? item.itemCount : item.zaiKuCount;
                  let NisOneBuyPrice =
                    item.onWayFlag == 1
                      ? item.itemCount * item.salePrice
                      : item.zaiKuCount * item.salePrice;
                  if (
                    (item2.rules.moneyOrCount == 1 &&
                      NisOneBuyCount >= cItem.oneBuyMoney) ||
                    (item2.rules.moneyOrCount == 2 &&
                      NisOneBuyPrice >= cItem.oneBuyCount)
                  ) {
                    // if(cItem.reduceOrPresent==2){
                    // reduceOrPresent=2;
                    // reduceOrPresentId=cItem.id;
                    // presentCount=cItem.presentCount;
                    // }
                    //  单个货品加上规则条件id
                    that.$set(item, "conditionsId", cItem.id);

                    conditionsId = cItem.id;
                  }
                }
              });
            }
          });

          // 给赠品加上条件id
          item2.children.forEach((item4) => {
            if (item4.itemType == 2) {
              that.$set(item4, "conditionsId", conditionsId);
            }
          });
          // that.$set(item2,'reduceOrPresent',reduceOrPresent);
          // that.$set(item2,'reduceOrPresentId',reduceOrPresentId);
          // that.$set(item2,'presentCount',presentCount);
          // // 有赠品但是不满足条件的删除赠品商品
          // if(reduceOrPresent==1&&presentIds.length>0){
          // 	that.deletePresentFun(presentIds);
          // 	item1.list.forEach(sItem=>{
          // 		if(sItem.goodsPromotionId==item.goodsPromotionId){
          // 			let arrList= sItem.children.filter(fItem=>fItem.itemType==2);
          // 			that.$set(sItem,'children',arrList);
          // 		}
          // 	})
          // }
        });
      });
    },
    // 单选--new
    selecetGoodItemFun(index, item, goodList) {
      let that = this;
      //totalCount:0,商品总件数
      //totalPrice:0,  商品总价格
      console.log("选择的下标：", index);
      console.log("当前项货品：", item);

      let shopList = this.allShopingCartList;
      // 标品判断库存  定制判断是否缺货  diyUnderStockFlag
      let zaiKuFlag =
        item.goodsType == 1
          ? item.inStockUsableCount >= item.zaiKuCount
            ? true
            : false
          : item.diyUnderStockFlag == 1
          ? true
          : false;
      let zaiTuFlag =
        item.goodsType == 1
          ? item.onWayUsableCount >= item.zaiTuCount
            ? true
            : false
          : item.diyUnderStockFlag == 1
          ? true
          : false;
      if (item.openFlag == 1) {
        if (zaiKuFlag && zaiTuFlag) {
          that.$set(item, "isCheck", !item.isCheck);
        } else {
          if (that.isEdit) {
            // 编辑状态
            that.$set(item, "isCheck", !item.isCheck);
          } else {
            that.$set(item, "kuCunShow", true);
            item.setInter = setInterval(function () {
              that.$set(item, "kuCunShow", false);
            }, 2000);
            return;
          }
        }
      }
      let presentIds = []; //赠品ids列表
      let reduceOrPresent = 1; //是否是赠品 1：否  2：是
      let reduceOrPresent2 = 1; //是否是赠品 1：否  2：是
      let reduceOrPresentId = "";
      let presentCount = 0;
      let conditionsId = "";
      let childrenList = goodList.children;
      let zCoun = item.zaiKuCount;
      let tCount = item.zaiTuCount;
      for (let i = 0; i < childrenList.length; i++) {
        if (
          childrenList[i].isCheck &&
          childrenList[i].itemId != item.itemId &&
          childrenList[i].itemType != 2
        ) {
          zCoun += childrenList[i].zaiKuCount;
          tCount += childrenList[i].zaiTuCount;
        }
        if (childrenList[i].reduceOrPresent == 2) {
          reduceOrPresent = 2;
        }
        if (childrenList[i].itemType == 2) {
          presentIds.push(childrenList[i].id);
        }
      }
      if (goodList.goodsPromotionId && goodList.goodsPromotionId != "no") {
        item.promotions.forEach((promoItem) => {
          let onWayFlag = promoItem.onWayFlag; //在途商品是否参与活动 1.参与，0.不参与
          let promoStatus = promoItem.promoStatus; //促销状态：0 未开始, 1 促销中 2 已过期 3 提前结束
          let promoType = promoItem.promoType; //活动类型，1 普通活动，2 阶梯活动
          promoItem.rules.forEach((ruleItem) => {
            if (item.goodsPromotionId == ruleItem.id && promoStatus == 1) {
              let isOneBuyCount =
                onWayFlag == 0 ? item.zaiKuCount : item.itemCount;
              let isOneBuyMoney =
                onWayFlag == 0
                  ? item.zaiKuCount * item.salePrice
                  : item.itemCount * item.salePrice;
              //货品活动是否累计 addUpFlag，1是 0否( ruleType 规则对象是2或3时有效)
              if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
                isOneBuyCount = onWayFlag == 0 ? zCoun : zCoun + tCount;
                isOneBuyMoney =
                  onWayFlag == 0
                    ? zCoun * item.salePrice
                    : (zCoun + tCount) * item.salePrice;
              }
              // 是否累计 --END
              ruleItem.conditions.forEach((cItem) => {
                //moneyOrCount 规则形式：1金额 2数量
                if (
                  (ruleItem.moneyOrCount == 1 &&
                    isOneBuyMoney >= cItem.oneBuyMoney) ||
                  (ruleItem.moneyOrCount == 2 &&
                    isOneBuyCount >= cItem.oneBuyCount)
                ) {
                  conditionsId = cItem.id;
                  if (cItem.reduceOrPresent == 2) {
                    reduceOrPresent = 2;
                    reduceOrPresent2 = 2;
                    reduceOrPresentId = cItem.id;
                    presentCount = cItem.presentCount;
                    // 判断是否叠加
                    if (cItem.reductionPresentAddFlag == 1) {
                      //计算叠加的数量
                      let dieJiaNum =
                        ruleItem.moneyOrCount == 1
                          ? parseInt(isOneBuyMoney / cItem.oneBuyMoney)
                          : parseInt(isOneBuyCount / cItem.oneBuyCount);
                      presentCount = cItem.presentCount * dieJiaNum;
                    }
                    that.$set(item, "presentCount", presentCount);
                    // 累计 勾选上的货品都加上规则条件id
                    if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
                      goodList.children.forEach((gItem2) => {
                        if (gItem2.isCheck || gItem2.itemType == 2) {
                          that.$set(gItem2, "conditionsId", cItem.id);
                          if (gItem2.itemType == 2) {
                            that.$set(gItem2, "isCheck", true);
                          }
                        }
                      });
                    }
                  }
                } else {
                  //不满足条件的把勾选上的货品都去掉规则条件id
                  goodList.children.forEach((gItem2) => {
                    that.$set(gItem2, "conditionsId", "");
                  });
                }
              });
            }
          });
        });
      }
      that.$set(goodList, "conditionsId", conditionsId);
      that.$set(goodList, "reduceOrPresent", reduceOrPresent);
      that.$set(goodList, "reduceOrPresentId", reduceOrPresentId);
      that.$set(goodList, "presentCount", presentCount);
      // 有赠品但是不满足条件的删除赠品商品
      if (reduceOrPresent == 1 && presentIds.length > 0) {
        that.deletePresentFun(presentIds);
        shopList[index].list.forEach((sItem) => {
          if (sItem.goodsPromotionId == goodList.goodsPromotionId) {
            let arrList = sItem.children.filter((fItem) => fItem.itemType == 1);
            that.$set(sItem, "children", arrList);
          }
        });
      }
      let flag = true;
      shopList[index].list.forEach((item1) => {
        item1.children.forEach((items) => {
          if (!items.isCheck) {
            flag = false;
            return;
          }
          //有赠品的时候，勾选货品把赠品也勾选上
          if (
            items.itemId == item.itemId &&
            items.itemType == 1 &&
            items.goodsPromotionId != "no" &&
            items.goodsPromotionId != "" &&
            reduceOrPresent2 == 2
          ) {
            item1.children.forEach((item3) => {
              if (item3.itemType == 2) {
                that.$set(item3, "isCheck", item.isCheck);
                that.$set(item3, "conditionsId", conditionsId);
              }
            });
          }
        });
      });

      shopList[index].allCkeck = flag;
      // 全选判断
      let shopFlag = true;
      shopList.forEach((shopItem) => {
        if (!shopItem.allCkeck) {
          shopFlag = false;
        }
      });
      that.allCheckFlag = shopFlag;
      console.log("当前同类项货品列表", goodList);
    },
    // 单选货品--old
    selecetGoodItemFun2(index, item, goodItem) {
      let that = this;
      //totalCount:0,商品总件数
      //totalPrice:0,  商品总价格
      console.log("选择的下标：", index);
      console.log("当前项货品：", item);
      console.log("当前同类项货品列表", goodItem);
      // 处理勾选的货品  diyUnderStockFlag
      let shopList = this.allShopingCartList;
      // 标品判断库存  定制判断是否缺货  diyUnderStockFlag
      let zaiKuFlag =
        item.goodsType == 1
          ? item.inStockUsableCount >= item.zaiKuCount
            ? true
            : false
          : item.diyUnderStockFlag == 1
          ? true
          : false;
      let zaiTuFlag =
        item.goodsType == 1
          ? item.onWayUsableCount >= item.zaiTuCount
            ? true
            : false
          : item.diyUnderStockFlag == 1
          ? true
          : false;
      if (item.openFlag == 1) {
        if (zaiKuFlag && zaiTuFlag) {
          that.$set(item, "isCheck", !item.isCheck);
        } else {
          that.$set(item, "kuCunShow", true);
          item.setInter = setInterval(function () {
            that.$set(item, "kuCunShow", false);
          }, 2000);
          // that.tipFun('库存不足');
          return;
        }
      }
      // 处理活动
      let isOneBuyCount = 0;
      let isOneBuyPrice = 0;
      let presentIds = []; //赠品列表
      goodItem.children.forEach((gItem) => {
        // 只累加不是赠品的货品
        if (gItem.itemType == 1 && gItem.isCheck) {
          //itemType:是否赠品 1 非赠品 2 赠品
          //  onWayFlag:在途商品是否参与活动 1.参与，0.不参与
          isOneBuyCount +=
            gItem.onWayFlag == 1 ? gItem.itemCount : gItem.zaiKuCount;
          isOneBuyPrice +=
            gItem.onWayFlag == 1
              ? gItem.itemCount * gItem.salePrice
              : gItem.zaiKuCount * gItem.salePrice;
        }
        if (gItem.itemType == 2) {
          presentIds.push(gItem.id);
        }
      });
      let reduceOrPresent = 1; //是否是赠品 1：否  2：是
      let reduceOrPresentId = "";
      let presentCount = 0;
      let conditionsId = "";
      if (goodItem.goodsPromotionId && goodItem.goodsPromotionId != "no") {
        goodItem.rules.conditions.forEach((cItem) => {
          //addUpFlag是否累计，1是 0否( 规则对象是2或3时有效)
          if (goodItem.rules.addUpFlag == 1) {
            let oneBuyMoneyFlag =
              goodItem.rules.moneyOrCount == 1 &&
              isOneBuyPrice >= cItem.oneBuyMoney;
            let oneBuyCountFlag =
              goodItem.rules.moneyOrCount == 2 &&
              isOneBuyCount >= cItem.oneBuyCount;
            if (oneBuyMoneyFlag || oneBuyCountFlag) {
              if (cItem.reduceOrPresent == 2) {
                reduceOrPresent = 2;
                reduceOrPresentId = cItem.id;
                presentCount = cItem.presentCount;
                // 判断是否叠加
                if (cItem.reductionPresentAddFlag == 1) {
                  //计算叠加的数量
                  let dieJiaNum =
                    ruleItem.moneyOrCount == 1
                      ? parseInt(isOneBuyMoney / cItem.oneBuyMoney)
                      : parseInt(isOneBuyCount / cItem.oneBuyCount);
                  presentCount = cItem.presentCount * dieJiaNum;
                }
              }
              // 勾选上的货品都加上规则条件id
              goodItem.children.forEach((gItem2) => {
                if (gItem2.isCheck) {
                  that.$set(gItem2, "conditionsId", cItem.id);
                  conditionsId = cItem.id;
                }
              });
            }
          } else {
            let NisOneBuyCount =
              item.onWayFlag == 1 ? item.itemCount : item.zaiKuCount;
            let NisOneBuyPrice =
              item.onWayFlag == 1
                ? item.itemCount * item.salePrice
                : item.zaiKuCount * item.salePrice;
            if (
              (goodItem.rules.moneyOrCount == 1 &&
                NisOneBuyCount >= cItem.oneBuyMoney) ||
              (goodItem.rules.moneyOrCount == 2 &&
                NisOneBuyPrice >= cItem.oneBuyCount)
            ) {
              if (cItem.reduceOrPresent == 2) {
                reduceOrPresent = 2;
                reduceOrPresentId = cItem.id;
                presentCount = cItem.presentCount;
              }
              //  单个货品加上规则条件id
              that.$set(item, "conditionsId", cItem.id);

              conditionsId = cItem.id;
            }
          }
        });
      }

      // 给赠品加上条件id
      goodItem.children.forEach((item4) => {
        if (item4.itemType == 2) {
          that.$set(item4, "conditionsId", conditionsId);
        }
      });
      that.$set(goodItem, "reduceOrPresent", reduceOrPresent);
      that.$set(goodItem, "reduceOrPresentId", reduceOrPresentId);
      that.$set(goodItem, "presentCount", presentCount);

      // 有赠品但是不满足条件的删除赠品商品
      if (reduceOrPresent == 1 && presentIds.length > 0) {
        that.deletePresentFun(presentIds);
        shopList[index].list.forEach((sItem) => {
          if (sItem.goodsPromotionId == goodItem.goodsPromotionId) {
            let arrList = sItem.children.filter((fItem) => fItem.itemType == 2);
            that.$set(sItem, "children", arrList);
          }
        });
      }
      let flag = true;

      shopList[index].list.forEach((item1) => {
        item1.children.forEach((items) => {
          if (!items.isCheck) {
            flag = false;
            return;
          }
          //有赠品的时候，勾选货品把赠品也勾选上
          if (
            items.itemId == item.itemId &&
            items.itemType == 1 &&
            items.goodsPromotionId != "no"
          ) {
            item1.children.forEach((item3) => {
              if (item3.itemType == 2) {
                that.$set(item3, "isCheck", item.isCheck);
                that.$set(item3, "itemCount", item3.presentCount);
              }
            });
          }
        });
      });
      shopList[index].allCkeck = flag;
      // 全选判断
      let shopFlag = true;
      shopList.forEach((shopItem) => {
        if (shopItem.allCkeck == false) {
          shopFlag = false;
        }
      });
      that.allCheckFlag = shopFlag;
    },

    // 结账-y
    goTheOrder() {
      if (!this.loadingFlag) {
        console.log("结账：", this.allShopingCartList);
        let that = this;
        let orderGood = [];
        let shopList = this.allShopingCartList;
        let isAllType = false; //货品在库在途是否同时存在
        let zaiKuAllType = false;
        let zaiTuAllType = false;
        let orderFlag = true;
        shopList.forEach((item) => {
          item.list.forEach((item2) => {
            item2.children.forEach((item3) => {
              //判断库存是否足够
              // 拼团-需要是否支持预售
              let totalPrice = item3.itemCount * item3.salePrice;
              if (item3.groupSeckillId && item3.groupSeckillId != "") {
                item3.groupSeckills.forEach((groupItem) => {
                  if (
                    item3.groupSeckillId == groupItem.groupSeckillId &&
                    groupItem.groupSeckillStatus == 1
                  ) {
                    if (item3.itemCount > groupItem.minNum) {
                      if (
                        item3.itemCount >
                        item.inStockUsableCount + item.onWayUsableCount
                      ) {
                        // 是否支持超卖或预售 1、支持 0、不支持
                        if (groupItem.mtoFlag == 1) {
                          if (
                            groupItem.maxNum &&
                            item.maxNum != 0 &&
                            item3.itemCount > groupItem.maxNum
                          ) {
                            let title =
                              "编号：" +
                              item3.itemCode +
                              "货品订购数量不能大于拼团限购量：" +
                              groupItem.maxNum;

                            that.tipFun(title);
                            that.$set(item3, "isCheck", false);
                          } else {
                            totalPrice =
                              item3.itemCount * groupItem.groupSeckillPrice;
                            that.$set(item3, "totalPrice", totalPrice);
                          }
                        } else {
                          let title =
                            "编号：" + item3.itemCode + "的货品库存不足!";
                          orderFlag = false;

                          that.tipFun(title);
                          that.$set(item3, "isCheck", false);
                        }
                      } else {
                        totalPrice =
                          item3.itemCount * groupItem.groupSeckillPrice;
                        that.$set(
                          item3,
                          "lastPrice",
                          fomatFloat(Number(totalPrice), 2)
                        );
                      }
                    } else {
                      that.$set(item3, "groupSeckillId", "");
                    }
                  }
                });
              } else {
                if (
                  item3.itemCount >
                  item3.inStockUsableCount + item3.onWayUsableCount
                ) {
                  let title = "编号：" + item3.itemCode + "的货品库存不足!";
                  orderFlag = false;
                  that.tipFun(title);
                  that.$set(item3, "isCheck", false);
                }
              }
            });
            item2.children.forEach((item3) => {
              if (item3.isCheck) {
                if (item3.zaiKuCount > 0) {
                  zaiKuAllType = true;
                }
                if (item3.zaiTuCount > 0) {
                  zaiTuAllType = true;
                }
                // 如果赠品的货品数量大于可选的最大数，把下单的赠品数量改为可选的最大数
                // if(item3.itemType==2){
                // 	if(item3.itemCount>item3.presentCount){
                // 		let itemCount=0;
                // 		for(let i=0;i<item2.children.length;i++){
                // 			if(item2.children[i].isCheck&&item2.children[i].itemType==1){
                // 				itemCount+=item2.children[i].presentCount
                // 			}
                // 		}
                // 		that.$set(item3,'itemCount',itemCount)
                // 		that.$set(item3,'zaiKuCount',itemCount);
                // 	}

                // }
                // 在库、在途是否同时存在--end
                let totalPrice = item3.itemCount * item3.salePrice;
                // 促销活动
                if (
                  item3.goodsPromotionId &&
                  item3.goodsPromotionId != "no" &&
                  item3.goodsPromotionId != "" &&
                  item3.itemType == 1
                ) {
                  let conditionsId = "";
                  if (item3.promotions) {
                    item3.promotions.forEach((promoItem) => {
                      let onWayFlag = promoItem.onWayFlag; //在途商品是否参与活动 1.参与，0.不参与
                      let promoStatus = promoItem.promoStatus; //促销状态：0 未开始, 1 促销中 2 已过期 3 提前结束
                      let promoType = promoItem.promoType; //活动类型，1 普通活动，2 阶梯活动
                      promoItem.rules.forEach((ruleItem) => {
                        if (item3.goodsPromotionId == ruleItem.id) {
                          let isOneBuyCount =
                            onWayFlag == 0 ? item3.zaiKuCount : item3.itemCount;
                          let isOneBuyMoney =
                            onWayFlag == 0
                              ? item3.zaiKuCount * item3.salePrice
                              : item3.itemCount * item3.salePrice;
                          //货品活动是否累计 addUpFlag，1是 0否( ruleType 规则对象是2或3时有效)
                          if (
                            ruleItem.addUpFlag == 1 &&
                            ruleItem.ruleType != 1
                          ) {
                            let childrenList = item2.children;
                            let zCoun = item3.zaiKuCount;
                            let tCount = item3.zaiTuCount;
                            let zPrice = item3.zaiKuCount * item3.salePrice;
                            let tPrice = item3.zaiTuCount * item3.salePrice;
                            for (let i = 0; i < childrenList.length; i++) {
                              if (
                                childrenList[i].isCheck &&
                                childrenList[i].itemId != item3.itemId &&
                                childrenList[i].itemType != 2
                              ) {
                                zCoun += childrenList[i].zaiKuCount;
                                tCount += childrenList[i].zaiTuCount;
                                zPrice +=
                                  childrenList[i].zaiKuCount *
                                  childrenList[i].salePrice;
                                tPrice +=
                                  childrenList[i].zaiTuCount *
                                  childrenList[i].salePrice;
                              }
                            }
                            isOneBuyCount =
                              onWayFlag == 0 ? zCoun : zCoun + tCount;
                            isOneBuyMoney =
                              onWayFlag == 0 ? zPrice : zPrice + tPrice;
                          }
                          // 是否累计 --END
                          ruleItem.conditions.forEach((cItem) => {
                            //moneyOrCount 规则形式：1金额 2数量
                            if (
                              (ruleItem.moneyOrCount == 1 &&
                                isOneBuyMoney >= cItem.oneBuyMoney) ||
                              (ruleItem.moneyOrCount == 2 &&
                                isOneBuyCount >= cItem.oneBuyCount)
                            ) {
                              conditionsId = cItem.id;
                              // specialFlag 是否特价，1是 0否
                              if (cItem.specialFlag == 1) {
                                totalPrice =
                                  onWayFlag == 0
                                    ? item3.zaiKuCount * cItem.specialPrice +
                                      item3.zaiTuCount * item3.salePrice
                                    : item3.itemCount * cItem.specialPrice;
                                //  活动累计情况下，勾选上的货品都按特价计算
                                if (
                                  ruleItem.addUpFlag == 1 &&
                                  ruleItem.ruleType != 1
                                ) {
                                  let childrenList = item2.children;
                                  for (
                                    let i = 0;
                                    i < childrenList.length;
                                    i++
                                  ) {
                                    if (
                                      childrenList[i].isCheck &&
                                      childrenList[i].itemId != item.itemId
                                    ) {
                                      for (
                                        let j = 0;
                                        j < childrenList[i].promotions.length;
                                        j++
                                      ) {
                                        let rulesF =
                                          childrenList[i].promotions[j].rules;
                                        for (
                                          let a = 0;
                                          a < rulesF.length;
                                          a++
                                        ) {
                                          if (
                                            item3.goodsPromotionId ==
                                            rulesF[a].id
                                          ) {
                                            let conditions =
                                              rulesF[a].conditions;
                                            for (
                                              let b = 0;
                                              b < conditions.length;
                                              b++
                                            ) {
                                              let childrenTotalPrice =
                                                onWayFlag == 0
                                                  ? childrenList[i].zaiKuCount *
                                                      conditions[b]
                                                        .specialPrice +
                                                    childrenList[i].zaiTuCount *
                                                      childrenList[i].salePrice
                                                  : childrenList[i].itemCount *
                                                    conditions[b].specialPrice;
                                              that.$set(
                                                childrenList[i],
                                                "lastPrice",
                                                childrenTotalPrice
                                              );
                                              that.$set(
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
                              } else {
                                //reduceOrPresent 促销统计方式：1满减 2满赠
                                if (cItem.reduceOrPresent == 1) {
                                  // reduceType 满减类型， 1减免 2折扣
                                  if (cItem.reduceType == 1) {
                                    //  活动累计情况下，勾选上的货品都按减免计算
                                    if (
                                      ruleItem.addUpFlag == 1 &&
                                      ruleItem.ruleType != 1
                                    ) {
                                      let childrenList = item2.children;
                                      for (
                                        let i = 0;
                                        i < childrenList.length;
                                        i++
                                      ) {
                                        if (childrenList[i].isCheck) {
                                          let reduction2 = cItem.reduction;
                                          if (
                                            cItem.reductionPresentAddFlag == 1
                                          ) {
                                            reduction2 =
                                              ruleItem.moneyOrCount == 1
                                                ? onWayFlag == 0
                                                  ? childrenList[i].zaiKuCount *
                                                    childrenList[i].salePrice *
                                                    (cItem.reduction /
                                                      cItem.oneBuyMoney)
                                                  : childrenList[i].itemCount *
                                                    childrenList[i].salePrice *
                                                    (cItem.reduction /
                                                      cItem.oneBuyMoney)
                                                : onWayFlag == 0
                                                ? childrenList[i].zaiKuCount *
                                                  (cItem.reduction /
                                                    cItem.oneBuyCount)
                                                : childrenList[i].itemCount *
                                                  (cItem.reduction /
                                                    cItem.oneBuyCount);
                                          } else {
                                            reduction2 =
                                              (reduction2 / isOneBuyCount) *
                                              childrenList[i].itemCount;
                                          }

                                          let childrenTotalPrice = fomatFloat(
                                            childrenList[i].itemCount *
                                              childrenList[i].salePrice -
                                              reduction2,
                                            2
                                          );
                                          console.log(childrenTotalPrice);
                                          if (
                                            childrenList[i].itemId ==
                                            item3.itemId
                                          ) {
                                            totalPrice = childrenTotalPrice;
                                          }

                                          that.$set(
                                            childrenList[i],
                                            "lastPrice",
                                            childrenTotalPrice
                                          );
                                          that.$set(
                                            childrenList[i],
                                            "conditionsId",
                                            cItem.id
                                          );
                                        }
                                      }
                                    } else {
                                      //减免金额
                                      let reduction = cItem.reduction;
                                      // 判断是否叠加的减免金额
                                      if (cItem.reductionPresentAddFlag == 1) {
                                        reduction =
                                          ruleItem.moneyOrCount == 1
                                            ? onWayFlag == 0
                                              ? item3.zaiKuCount *
                                                item3.salePrice *
                                                (cItem.reduction /
                                                  cItem.oneBuyMoney)
                                              : item3.itemCount *
                                                item3.salePrice *
                                                (cItem.reduction /
                                                  cItem.oneBuyMoney)
                                            : onWayFlag == 0
                                            ? item3.zaiKuCount *
                                              (cItem.reduction /
                                                cItem.oneBuyCount)
                                            : item3.itemCount *
                                              (cItem.reduction /
                                                cItem.oneBuyCount);
                                      }
                                      totalPrice = fomatFloat(
                                        item3.itemCount * item3.salePrice -
                                          reduction,
                                        2
                                      );
                                      // 订单活动
                                      if (ruleItem.ruleType == 1) {
                                        let orserIndex = 0;
                                        item2.children.forEach(
                                          (orderItem, orserIndex) => {
                                            console.log(orserIndex);
                                            if (
                                              orderItem.itemId != item.itemId &&
                                              orderItem.goodsPromotionId ==
                                                item.goodsPromotionId
                                            ) {
                                              this.$set(
                                                orderItem,
                                                "lastPrice",
                                                Number(
                                                  orderItem.salePrice *
                                                    orderItem.itemCount
                                                )
                                              );
                                            }
                                          }
                                        );
                                      }
                                    }
                                  } else {
                                    //折扣
                                    let zheKouPrice = fomatFloat(
                                      (item3.salePrice * cItem.discount) / 100,
                                      2
                                    );
                                    totalPrice =
                                      onWayFlag == 0
                                        ? item3.zaiKuCount * zheKouPrice +
                                          item3.zaiTuCount * item3.salePrice
                                        : item3.itemCount * zheKouPrice;
                                    //  活动累计情况下，勾选上的货品都按减免计算
                                    if (
                                      ruleItem.addUpFlag == 1 &&
                                      ruleItem.ruleType != 1
                                    ) {
                                      let childrenList = item2.children;
                                      for (
                                        let i = 0;
                                        i < childrenList.length;
                                        i++
                                      ) {
                                        let zheKouPrice2 = fomatFloat(
                                          (childrenList[i].salePrice *
                                            cItem.discount) /
                                            100,
                                          2
                                        );
                                        let childrenTotalPrice =
                                          onWayFlag == 0
                                            ? childrenList[i].zaiKuCount *
                                                zheKouPrice2 +
                                              childrenList[i].zaiTuCount *
                                                childrenList[i].salePrice
                                            : childrenList[i].itemCount *
                                              zheKouPrice2;
                                        that.$set(
                                          childrenList[i],
                                          "lastPrice",
                                          childrenTotalPrice
                                        );
                                        that.$set(
                                          childrenList[i],
                                          "conditionsId",
                                          cItem.id
                                        );
                                      }
                                    }
                                  }
                                } else {
                                  item2.children.forEach((item4) => {
                                    if (item4.itemType == 2) {
                                      that.$set(
                                        item4,
                                        "conditionsId",
                                        cItem.id
                                      );
                                    }
                                  });
                                }
                              }
                            }
                          });
                        }
                      });
                    });
                  }

                  that.$set(item3, "conditionsId", conditionsId);
                  that.$set(
                    item3,
                    "lastPrice",
                    fomatFloat(Number(totalPrice), 2)
                  );
                }
                orderGood.push(item3);
              }
            });
          });
        });
        isAllType = zaiKuAllType & zaiTuAllType ? true : false;

        that.orderGoodList = orderGood;
        console.log("要下单的货品：", orderGood);
        console.log("是否包含在途在库:", isAllType);
        if (orderGood.length == 0) {
          that.tipFun("请选择需要购买的商品");
          return false;
        }

        if (orderFlag) {
        }
        uni.setStorageSync("shopOrderList", JSON.stringify(orderGood));

        uni.navigateTo({
          url: "confirmOrder?isTwoWay=" + isAllType,
        });
      }
    },
    // 进入详情
    toGoodsDetails(id) {
      uni.navigateTo({
        url: "/pages/classify/goodsDetails?id=" + id,
      });
    },

    // 跳转分类
    toClassifyFun() {
      uni.switchTab({
        url: "/pages/classify/classify",
      });
    },

    tologinFun() {
      uni.navigateTo({
        url: "/pages/login/login",
      });
    },

    // 编辑/完成
    handleEdit() {
      this.isEdit = !this.isEdit;
      let that = this;
      let shopList = this.allShopingCartList;
      // 非编辑状态
      shopList.forEach((item1) => {
        item1.list.forEach((item2) => {
          item2.children.forEach((item) => {
            // 标品判断库存  定制判断是否缺货  diyUnderStockFlag
            let zaiKuFlag =
              item.goodsType == 1
                ? item.inStockUsableCount >= item.zaiKuCount
                  ? true
                  : false
                : item.diyUnderStockFlag == 1
                ? true
                : false;
            let zaiTuFlag =
              item.goodsType == 1
                ? item.onWayUsableCount >= item.zaiTuCount
                  ? true
                  : false
                : item.diyUnderStockFlag == 1
                ? true
                : false;
            if (item.openFlag == 1) {
              if (!(zaiKuFlag && zaiTuFlag) && item.isCheck) {
                // 无库存且已选，设置为未选
                that.$set(item, "isCheck", false);
              }
            }
          });
        });
      });
    },
  },
};
</script>

<style lang="scss">




.self-numberBox {
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
.shoppingtop-title {
  display: flex;
  align-items: center;
  padding: 0 30rpx;
  background: #fff;
  text {
    display: block;
    width: 100rpx;
    font-size: 32rpx;
  }
  view {
    font-size: 32rpx;
    font-weight: 400;
    width: calc(100% - 200rpx);
    height: 80rpx;
    line-height: 80rpx;
    text-align: center;
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
.shoppingCart {
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #333333;

  .guize-listBox {
    padding: 30rpx 85rpx;
    font-size: 26rpx;
    view {
      padding: 10rpx;
    }
  }
  .sc-list-scroll {
    width: 100%;
    height: calc(100vh - 146rpx - var(--status-bar-height) - 15rpx);
    padding: calc(136rpx + var(--status-bar-height)) 30rpx 0 30rpx;
    // #ifdef H5
    height: calc(100vh - 211rpx - var(--window-bottom));
    padding-top: 124rpx;
    // #endif
    overflow-y: auto;
    box-sizing: border-box;
  }
  .sc2-iconCheck {
    font-size: 40rpx;
    margin-top: 40rpx;
  }

  .sc-list {
    background: #fff;
    border-radius: 16rpx;
    margin-bottom: 20rpx;
    .sc-list-line {
      .sc-list-lineItem {
        display: flex;
        align-items: flex-start;
      }

      .sc-list-linePresent {
        width: 35rpx;
        background: #ed5307;
        color: #fff;
        text-align: center;
        font-size: 22rpx;
        border-radius: 5rpx;
        padding: 5rpx 0;
        margin-top: 30rpx;
      }
      .sc-list-lineImgBox {
        margin-left: 20rpx;
        view {
          width: 120rpx;
          text-align: center;
          margin-top: 10rpx;
          font-size: 28rpx;
        }
        .sc-smGood {
          width: 120rpx;
          height: 120rpx;
          border-radius: 8rpx;
          overflow: hidden;
          background: rgba(243, 244, 248, 0.5);
        }
      }

      .sc-Rg {
        margin-left: 28rpx;
        width: 460rpx;
        display: flex;
        justify-content: space-between;
        .sc-RgLf {
          .sc-list-bgRgName {
            font-size: 26rpx;
            width: 298rpx;
            font-size: 24rpx;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
          .sc-RgLf-info {
            font-size: 24rpx;
            display: flex;
            margin-top: 8rpx;
            text:nth-child(1) {
              color: #999;
            }
            text:nth-child(2) {
              color: #333;
              width: 200rpx;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            }
          }
        }

        .sc-smRg-topPrice {
          font-size: 30rpx;
          font-family: ArialMT;
          color: #000000;
        }
      }
    }
    // 在途在库---计算器
    .sc-list-lineItemBtm {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding-top: 28rpx;
      .sc-sm-stock {
        font-size: 24rpx;
        text:nth-child(2) {
          margin-left: 32rpx;
        }
      }
      .sc-sm-btm {
        display: flex;
        align-items: center;
        justify-content: space-between;
        .sc-sm-btm-LF {
          font-size: 24rpx;
          color: #999;
          text {
            display: block;
            margin-top: 6rpx;
          }
        }
        .sc-uni-num {
          position: relative;
          .sc-listItem-KuCunBox {
            position: absolute;
            left: 15rpx;
            bottom: 80rpx;
            z-index: 999;
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
          .num-label {
            font-size: 24rpx;
          }
        }
      }
    }

    .sc-list-label {
      position: relative;
      padding: 24rpx 30rpx;
      min-height: 42rpx;
      border-bottom: 1rpx solid #f3f4f8;
      display: flex;
      align-items: center;
      justify-content: space-between;
      &.no-promotion {
        justify-content: space-between;
        .sc-list-labelTj {
          text-align: right;
        }

        .sc-list-labelBtn {
          float: right;
        }
      }
      view {
        display: flex;
        align-items: center;
        &.hasGift {
          padding-right: 120rpx;
          width: 50%;
          box-sizing: border-box;
        }
        text {
          display: block;
        }
      }
      .sc-list-labelName {
        font-size: 28rpx;
      }
      .sc-list-labelTj {
        display: block;
        color: #999;
        font-size: 24rpx;
        margin-left: 30rpx;
        max-width: 350rpx;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      .sc-list-labelIcon {
        width: 24rpx;
        height: 24rpx;
        margin-left: 10rpx;
      }
      .sc-list-labelBtn {
        position: absolute;
        right: 30rpx;
        border: 1rpx solid $base-color1;
        color: $base-color1;
        font-size: 20rpx;
        display: block;
        padding: 5rpx 10rpx;
        border-radius: 10rpx;
      }
    }
  }
  .sc-list-smUniAction {
    padding: 24rpx 30rpx;
    & + .sc-list-smUniAction {
      border-top: 2rpx solid #f3f4f8;
    }
    .sc-list-switchActivity {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20rpx 30rpx 0 30rpx;
      .sc-list-switchActivity-title {
        color: #333;
        font-size: 24rpx;
      }
      .switchActivity-btn {
        color: $base-color1;
        cursor: pointer;
        text {
          font-size: 22rpx !important;
          margin-left: 15rpx;
        }
      }
    }
  }
  // 购买总计
  .sc-btm-total {
    background: #fff;
    position: fixed;
    z-index: 999;
    bottom: 0rpx;
    // #ifdef H5
    bottom: var(--window-bottom);
    // #endif
    width: 100%;
    border-top: 1rpx solid #f3f4f8;
    .sc-btm-totalTop {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 24rpx 30rpx;
      .sc-btm-totalAll {
        font-size: 28rpx;
        display: flex;
        align-items: center;
        .sc3-iconCheck {
          font-size: 40rpx;
          margin-left: 0;
        }
        text {
          margin-left: 12rpx;
        }
      }
      .sc-btm-totalNum-jina {
        font-size: 24rpx;
        text {
          padding: 0 6rpx;
        }
      }
      .sc-btm-detete {
        width: 214rpx;
        height: 80rpx;
        line-height: 80rpx;
        border-radius: 40rpx;
        border: 2rpx solid #f94021;
        text-align: center;
        font-size: 32rpx;
        color: #f94021;
      }
    }

    .sc-btm-totalNum {
      padding-left: 32rpx;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding-bottom: 18rpx;
      .sc-btm-totalNum-discounts {
        color: #f94021;
        font-family: ArialMT;
        font-size: 24rpx;
        text {
          font-family: PingFangSC-Regular, PingFang SC;
        }
      }
      .sc-btm-totalNum-price {
        width: 300rpx;
        text-align: left;

        text:nth-child(2) {
          font-family: Arial-BoldMT, Arial;
          font-weight: normal;
          color: #f94021;
          font-size: 40rpx;
          padding-right: 10rpx;
        }
        text:nth-child(1) {
          font-size: 24rpx;
        }
      }

      .sc-btm-totalBtn {
        width: 192rpx;
        line-height: 80rpx;
        height: 80rpx;
        color: #fff;
        text-align: center;
        font-size: 30rpx;
      }
    }
  }
  .sc-activity-iconY {
    color: $base-color1;
    font-size: 40rpx;
    margin-top: 5rpx;
  }
  .sc-activity-iconN {
    color: #999;
    font-size: 40rpx;
    margin-top: 5rpx;
  }

  // 切换活动
  .sc-aCivityPopup-content {
    padding-bottom: 50rpx;
    background: #fff;
    border-radius: 30rpx 30rpx 0px 0px;
    .myCollect-scroll-Y {
      max-height: 52vh;
      height: auto;
    }
    .sc-activity-list {
      .sc-activity-listBox {
        margin: 0 30rpx;
        padding: 20rpx 0;

        & + .sc-activity-listBox {
          border-top: 1rpx solid $opacity-border;
        }

        .sc-activity-listName {
          color: #333;
          font-size: 26rpx;
          font-weight: bold;
          margin-left: 20rpx;
        }
        .sc-activity-listGuizeBox {
          margin-left: 50rpx;
          margin-top: 20rpx;
          font-size: 24rpx;
          color: #999;
          display: flex;
          .sc-activity-listGuizeTitle {
            width: 160rpx;
          }
          .sc-activity-listGuizeList {
            text {
              display: block;
              padding: 5rpx 0;
            }
          }
        }
      }
    }
    .sc-activity-listBtn {
      color: #fff;
      text-align: center;
      width: 690rpx;
      height: 80rpx;
      line-height: 80rpx;
      font-size: 28rpx;
      margin: 30px auto 0;
      border-radius: 40rpx;
    }
  }

  // 选择赠品
  .popup-giftsList {
    padding: 30rpx;
    .popup-giftsList-line {
      display: flex;
      align-items: center;
      padding: 30rpx 0;
      & + .popup-giftsList-line {
        border-top: 1rpx solid $opacity-border;
      }
      image {
        width: 111rpx;
        height: 111rpx;
        margin-left: 15rpx;
      }
      .giftsList-line-rg {
        margin-left: 20rpx;
        .popup-giftsList-top {
          display: flex;
          justify-content: space-between;
          .popup-giftsList-name {
            width: 320rpx;
            font-weight: bold;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;
            overflow: hidden;
          }
          .popup-giftsList-numBox {
            margin-left: 25rpx;
            text-align: right;
            width: 200rpx !important;
          }
        }
        .popup-giftsList-num {
          font-size: 22rpx;
          color: #999;
          margin-top: 10rpx;
          display: flex;
          justify-content: space-between;
        }
      }
    }
  }
  .popup-giftsList-btm {
    padding: 30rpx;
    .popup-giftsList-total {
      margin-bottom: 30rpx;
      display: flex;
      justify-content: flex-end;
      color: #999;
      text {
        color: #ed5307;
        padding: 0 5rpx;
      }
    }
    .popup-giftsList-btn {
      width: 100%;
      height: 80rpx;
      line-height: 80rpx;
      font-size: 28rpx;
      color: #fff;
      border-radius: 40rpx;
      text-align: center;
    }
  }
}

.shopping-no-data {
  padding-top: 220rpx;
  padding-bottom: 44rpx;
  background: #fff;
  text-align: center;
  min-height: 100vh;
  // #ifdef H5
  min-height: calc(100vh - var(--window-bottom));
  // #endif
  box-sizing: border-box;
  image {
    width: 502rpx;
    height: 496rpx;
  }

  view {
    color: #fff;
    margin: 86rpx auto 0 auto;
    width: 360rpx;
    height: 80rpx;
    line-height: 80rpx;
    text-align: center;
    border-radius: 40rpx;
    font-size: 28rpx;
  }
}
</style>
