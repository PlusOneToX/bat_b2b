<template>
  <div>
    <div class="index rl-margin-zero">
      <!--公共头部-->
      <Header
        :activitys="activitys"
        :currentTab="'activity'"
        ref="header"
      ></Header>
      <!--主内容-->
      <div class="content-wrap">
        <div class="banner">
          <div class="inner"></div>
        </div>
        <!-- 顶部提示 -->
        <div
          class="
            shopcar-activity
            app-flex app-justify-content-start
            rl-margin-zero
          "
          v-if="
            onWayAttendEventFlag === 0 ||
            mtoAttendEventFlag === 0 ||
            customizedAttendEventFlag === 0
          "
        >
          <div
            class="shopcar-activity-img app-flex app-align-items-flex-center"
          >
            <i class="el-icon-warning"></i>
          </div>
          <div class="shopCart-activity-state">
            <!-- <span v-if="onWayAttendEventFlag === 0">{{$t('Activitys.InTransitGoods')}}</span> -->
            <span v-if="mtoAttendEventFlag === 0">{{
              $t("Activitys.CommoditiesAdvanceSales")
            }}</span>
            <span v-if="customizedAttendEventFlag === 0">{{
              $t("Activitys.CustomizedCommodities")
            }}</span>
            <span v-if="$i18n.locale === 'zh'"
              >不参与活动，凑单时请仔细检查。</span
            >
            <span v-else
              >Do not participate in the activities, please check carefully when
              gathering orders</span
            >
          </div>
        </div>

        <div class="main rl-margin-zero rl-clear">
          <!-- LEFT--START -->
          <div
            class="
              column-left
              rl-padding-top-default rl-padding-bottom-default rl-bg-white rl-fl
            "
            v-if="activityDataList.length > 0"
          >
            <!-- 搜索--start -->
            <div class="logo-box rl-clear">
              <div class="input-box">
                <input
                  @keyup.enter="goSearch"
                  :placeholder="$t('Activitys.Search')"
                  v-model="searchContent"
                  type="text"
                />
              </div>
              <span
                @click="goSearch"
                class="search rl-tc rl-text-white cursor-pointer"
                >{{ $t("P.Search") }}</span
              >
            </div>
            <!-- 搜索--end -->
            <!-- 活动信息列表-左边-start -->
            <div class="activity-list">
              <div
                v-for="(item, index) in activityDataList"
                :key="index"
                @click="changeActivity(item, index)"
                class="item cursor-pointer"
                :class="activityIndex === index ? 'active' : ''"
              >
                <div class="activity-name">
                  <span
                    v-show="$i18n.locale === 'zh' || !item.nameEn == true"
                    >{{ item.name }}</span
                  >
                  <span v-show="$i18n.locale === 'en'">{{ item.nameEn }}</span>
                </div>
                <div class="activity-time">
                  {{ item.startTime | formatDate }} --
                  {{ item.endTime | formatDate }}
                </div>
              </div>
              <el-pagination
                small
                @current-change="ahandleCurrentChange"
                @size-change="ahandleSizeChange"
                layout="prev, pager, next"
                :page-size="apageSize"
                :total="atotalCount"
              ></el-pagination>
            </div>
            <!-- 活动信息列表-end -->
          </div>
          <!-- RIGHT--START -->
          <div class="column-right rl-fl">
            <div
              class="right-cons"
              v-for="(item, index) in activityRuleDetails"
              :key="index"
            >
              <!-- 规则-start -->
              <div class="rule-title rl-bg-white">
                <div class="rule-name rl-clear">
                  <div
                    class="rule-label rl-fl"
                    :class="{ en: $i18n.locale === 'en' }"
                  >
                    {{ $t("ShopCarts.Rule") }}:
                  </div>
                  <div class="rl-fl label app-single-limit">
                    <span>{{
                      $i18n.locale === "zh" || !item.ruleNameEn == true
                        ? item.ruleName
                        : item.ruleNameEn
                    }}</span>
                    <span v-if="item.addUpFlag === 0"
                      >(各产品需单独满足条件即可参与)</span
                    >
                  </div>
                </div>
                <div class="rule-describe app-flex app-justify-content-start">
                  <div
                    class="rule-label app-flex app-align-items-flex-center"
                    :class="{ en: $i18n.locale === 'en' }"
                  >
                    <span v-if="$i18n.locale === 'zh'">描述:</span>
                    <span v-else>Description:</span>
                  </div>
                  <div
                    class="item"
                    :class="{ active: value.meet === true }"
                    v-for="(value, cindex) in item.conditions"
                    :key="cindex"
                  >
                    <span>{{ cindex + 1 }}.</span>
                    <span
                      v-show="
                        $i18n.locale === 'zh' || !value.conditionNameEn == true
                      "
                      >{{ value.conditionName }}；</span
                    >
                    <span v-show="$i18n.locale === 'en'"
                      >{{ value.conditionNameEn }}；</span
                    >
                  </div>
                </div>
                <!-- 已为您选择赠品数量 -->
                <div
                  class="rule-describe app-flex app-justify-content-start"
                  v-show="giftNum > 0"
                >
                  <div
                    class="rule-label app-flex app-align-items-flex-center"
                    :class="{ en: $i18n.locale === 'en' }"
                  ></div>
                  <div
                    class="item gift-num cursor-pointer"
                    @click="presentDialogVisible = true"
                  >
                    <span>已为您选择 {{ giftNum }} 个赠品</span>
                    <i class="el-icon-arrow-right"></i>
                  </div>
                </div>
              </div>
              <!-- 规则-end -->
              <!-- 货品列表 -->
              <div
                class="
                  user-table
                  rl-bg-white rl-margin-top-xxxs rl-margin-bottom-xxxs
                "
              >
                <div
                  @click="changeTu(item, item.tuType)"
                  class="zaitu rl-clear cursor-pointer rl-fr"
                >
                  <span
                    class="rl-fl"
                    :class="{ gou: item.tuType === true }"
                  ></span>
                  {{ $t("P.ConTran") }}
                </div>
                <!-- 产品--N -->
                <div class="group" v-if="item.groups.length > 0">
                  <div
                    @click="changeGroup(item, index, group)"
                    class="rl-bg-gray-xm cursor-pointer"
                    :class="{ active: item.groupIndex === index }"
                    v-for="(group, index) in item.groups"
                    :key="index"
                  >
                    {{ $t("Activitys.Group") + " " + (index + 1) }}
                  </div>
                </div>
                <!-- 货品列表table -->
                <el-table :data="item.itemData" class="activity-el-table rl-tc">
                  <!-- 图片 -->
                  <el-table-column
                    :min-width="60"
                    :label="$t('ShopCarts.Picture')"
                  >
                    <template slot-scope="scope">
                      <div
                        @click="
                          goDoodsDetail(scope.row.goodsId, scope.row.goodsType)
                        "
                        class="shop-img cursor-pointer"
                      >
                        <img width="100%" :src="scope.row.itemImg" alt />
                      </div>
                    </template>
                  </el-table-column>
                  <!-- 编码 -->
                  <el-table-column
                    :label="$t('ShopCarts.ItemNo')"
                    prop="itemCode"
                    :min-width="85"
                  ></el-table-column>
                  <!-- 名称 -->
                  <el-table-column
                    :label="$t('ShopCarts.ItemName')"
                    :min-width="110"
                    show-overflow-tooltip
                  >
                    <template slot-scope="scope">
                      <div
                        class="rl-text-xxs"
                        v-if="
                          $i18n.locale === 'zh' || !scope.row.itemNameEn == true
                        "
                      >
                        {{ scope.row.itemName }}
                      </div>
                      <div class="rl-text-xxs" v-else>
                        {{ scope.row.itemNameEn }}
                      </div>
                    </template>
                  </el-table-column>
                  <!-- 规格 -->
                  <el-table-column
                    :label="$t('ShopCarts.Spe')"
                    show-overflow-tooltip
                  >
                    <template slot-scope="scope">
                      <div
                        class="rl-text-xxs"
                        v-show="
                          $i18n.locale === 'zh' ||
                          !scope.row.specificationNameEn == true
                        "
                      >
                        {{ scope.row.specsName }}
                      </div>
                      <div class="rl-text-xxs" v-show="$i18n.locale === 'en'">
                        {{ scope.row.specsName }}
                      </div>
                    </template>
                  </el-table-column>
                  <!-- 颜色 -->
                  <el-table-column
                    :label="$t('ShopCarts.Colors')"
                    width="60"
                    show-overflow-tooltip
                  >
                    <template slot-scope="scope">
                      <div
                        class="rl-text-xxs"
                        v-show="
                          $i18n.locale === 'zh' ||
                          !scope.row.colorNameEn == true
                        "
                      >
                        {{ scope.row.colorName }}
                      </div>
                      <div class="rl-text-xxs" v-show="$i18n.locale === 'en'">
                        {{ scope.row.colorNameEn }}
                      </div>
                    </template>
                  </el-table-column>
                  <!-- 会员价 -->
                  <el-table-column
                    :label="$t('ShopCarts.MemPrice')"
                    prop="salePrice"
                    :min-width="80"
                  >
                    <template slot-scope="scope">
                      <div
                        class="rl-text-xxs red"
                        v-if="
                          Number(scope.row.salePrice) !== 0 &&
                          scope.row.salePrice !== null
                        "
                      >
                        <i
                          v-if="
                            $i18n.locale === 'en' && $root.currency === 'USD'
                          "
                          >$</i
                        >
                        <i v-else>￥</i>
                        <span>{{
                          $root.currency === "USD"
                            ? scope.row.salePrice
                            : scope.row.salePrice | keepTwoNum
                        }}</span>
                      </div>
                      <div class="rl-text-xxs red" v-else>
                        {{
                          scope.row.salePrice === undefined ||
                          scope.row.salePrice === null
                            ? !userId == "" &&
                              freezeStatus !== undefined &&
                              freezeStatus !== null &&
                              freezeStatus === "2" &&
                              capitalStatus !== undefined &&
                              capitalStatus !== null &&
                              capitalStatus === "2"
                              ? $t("P.Calculating")
                              : $t("P.ViewAfterLogin")
                            : $t("P.NoPricing")
                        }}
                      </div>
                    </template>
                  </el-table-column>
                  <!-- 在库/在途数量 -->
                  <el-table-column
                    :label="$t('ShopCarts.AllQuantity')"
                    :min-width="120"
                  >
                    <template slot-scope="scope">
                      <div
                        v-if="scope.row.goodsType == 1"
                        class="
                          app-flex app-justify-content-space-between
                          rl-padding-left-default rl-padding-right-default
                        "
                      >
                        <div class="rl-text-xxs">
                          {{ scope.row.zaiKuCount || 0 }}
                        </div>
                        <div class="rl-text-xxs">/</div>
                        <div class="rl-text-xxs">
                          {{ scope.row.zaiTuCount || 0 }}
                        </div>
                      </div>
                      <div v-if="scope.row.goodsType == 2">
                        <el-input-number
                          v-model="scope.row.num"
                          disabled
                          :min="0"
                          label="描述文字"
                          size="mini"
                        ></el-input-number>
                      </div>
                      <div v-else>
                        <el-input-number
                          v-model="scope.row.num"
                          @change="handleChange($event, scope.row, item)"
                          :min="0"
                          :max="
                            item.tuType
                              ? scope.row.inStockUsableCount +
                                scope.row.onWayUsableCount
                              : scope.row.inStockUsableCount
                          "
                          label="描述文字"
                          size="mini"
                        ></el-input-number>
                      </div>
                    </template>
                  </el-table-column>
                  <!--  库存-->
                  <el-table-column :label="$t('P.Inventory')" :min-width="80">
                    <template slot-scope="scope">
                      <!-- 定制 -->
                      <div v-if="scope.row.goodsType == 2">
                        <el-button
                          class="mini-search-btn"
                          @click="goCustrom(scope.row)"
                          >{{ $t("ShopDetail.Customized") }}</el-button
                        >
                      </div>
                      <!-- 普通 -->
                      <div class="rl-text-xxs" v-else>
                        <span v-if="item.tuType === false">{{
                          scope.row.numInWarehouse
                        }}</span>
                        <span v-else>{{ scope.row.stockItemCount }}</span>
                      </div>
                    </template>
                  </el-table-column>
                  <!-- 查看图片 -->
                  <el-table-column
                    :label="$t('ShopCarts.Picture')"
                    v-if="item.isNotShowImg === true"
                  >
                    <template
                      slot-scope="scope"
                      v-if="
                        scope.row.diyList &&
                        ($i18n.locale === 'zh' ||
                          ($i18n.locale === 'en' && scope.row.goodsType !== 1))
                      "
                    >
                      <el-button class="mini-search-btn">{{
                        $t("ShopCarts.View")
                      }}</el-button>
                    </template>
                  </el-table-column>
                </el-table>

                <div class="rl-margin-top-default" style="height: 32px">
                  <!-- 合计 -->
                  <div class="total rl-fl">
                    {{ $t("P.Total") }}：{{ item.chooseTotalCount }}
                  </div>
                  <!-- 分页 -->
                  <div class="rl-fr">
                    <el-pagination
                      background
                      @current-change="handleCurrentChange($event, item)"
                      @size-change="handleSizeChange"
                      layout="prev, pager, next, jumper"
                      :page-size="curPageSize"
                      :total="curPageTotal"
                    ></el-pagination>
                  </div>
                </div>
                <div class="btn-wrap">
                  <!-- 立即购买 -->
                  <span class="buy-btn" @click="activityBuyGoods(item)">{{
                    $t("P.BuyNow")
                  }}</span>
                  <!-- 加入购物车 -->
                  <span
                    class="join-btn"
                    :loading="loadsing"
                    @click="activityJoinCat(item)"
                    >{{ $t("P.AddToCart") }}</span
                  >
                </div>
              </div>
            </div>
          </div>
        </div>

        <!--商品为空-start-->
        <div
          class="empty-car rl-margin-zero"
          v-if="activityRuleDetails.length == 0 && !showLoading"
        >
          <div class="empty-car-img rl-margin-zero">
            <img width="100%" src="../assets/images/goods-empty.png" alt />
          </div>
          <p
            class="
              rl-tc
              rl-margin-top-default
              rl-margin-bottom-default
              rl-text-sm
              rl-text-gray
            "
          >
            {{ $t("P.NoData") }}
          </p>
        </div>
        <!--商品为空-end-->
      </div>
      <!--公共底部-->
      <Footer :cateFlag="'activity'"></Footer>
    </div>
    <!--    选择赠品弹框--start-->
    <el-dialog
      class="alls"
      :title="$t('P.ChooseGift')"
      :visible.sync="presentDialogVisible"
    >
      <div v-for="(items, pIndex) in presentList" :key="pIndex">
        <div
          class="
            rl-padding-left-default
            rl-padding-bottom-xxxs
            rl-padding-top-xxxs
            rl-bd-gray-sm
          "
        >
          {{ $t("P.Receive") }} {{ items.presentCount }}
          {{ $t("P.MostPieces") }}, {{ $t("P.HaveSelected") }}
          {{ items.count }} {{ $t("P.Pieces") }}
        </div>
        <div
          class="
            shop-table
            max-height300
            overflow-y
            rl-padding-left-default rl-padding-right-default
          "
        >
          <div
            class="rl-tc rl-padding-top-default rl-text-gray"
            v-if="presentList.length === 0"
          >
            {{ $t("P.NoData") }}
          </div>
          <table>
            <tbody>
              <tr v-for="(item, index) in items.presents" :key="index">
                <!-- 货品编码 -->
                <td width="120px" class="rl-text-xxs">{{ item.itemCode }}</td>
                <!-- 图片 -->
                <td width="120px" class="rl-tc">
                  <div class="songImg rl-margin-zero">
                    <img
                      width="100%"
                      :src="item.itemImg ? item.itemImg : item.imageUrl1"
                      alt
                    />
                  </div>
                </td>
                <!-- 名称 -->
                <td width="180px" class="rl-text-xxs">
                  {{
                    $i18n.locale === "en" &&
                    item.itemNameEn !== undefined &&
                    item.itemNameEn !== null &&
                    item.itemNameEn !== ""
                      ? item.itemNameEn
                      : item.itemName
                  }}
                </td>
                <!-- 添加数量 -->
                <td width="150px">
                  <!-- 自封装计数器 -->
                  <div
                    class="el-input-number el-input-number--mini"
                    style="width: 100px"
                  >
                    <!--减号 -->
                    <span
                      class="el-input-number__decrease"
                      :class="item.num == 0 ? 'is-disabled' : ''"
                      @click="presentHandleChange(item, items, 1)"
                      ><i class="el-icon-minus"></i
                    ></span>
                    <!-- 加号 -->
                    <span
                      class="el-input-number__increase"
                      :class="
                        items.count == items.presentCount ||
                        item.num >= item.kucunCount
                          ? 'is-disabled'
                          : ''
                      "
                      @click="presentHandleChange(item, items, 2)"
                      ><i class="el-icon-plus"></i
                    ></span>
                    <!-- 数字框 -->
                    <div class="el-input el-input--mini">
                      <input
                        v-model="item.num"
                        min="0"
                        :max="items.presentCount"
                        class="el-input__inner"
                        @change="presentHandleChange(item, items, 3)"
                      />
                    </div>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="presentDialogVisible = false">{{
          $t("P.OK")
        }}</el-button
        ><!-- 确定   -->
        <el-button @click="presentDialogVisible = false">{{
          $t("P.Cancel")
        }}</el-button
        ><!-- 取消   -->
      </div>
    </el-dialog>
    <!--    选择赠品弹框--end-->

    <!--预售弹框-->
    <el-dialog class="alls" title="预售" :visible.sync="visibleDialog">
      <div class="shop-table rl-padding-left-double rl-padding-right-default">
        <div class="item rl-text-gray rl-clear rl-margin-bottom-xxxs">
          <div class="rl-fl rl-margin-right-double">货 号</div>
          <div class="rl-fl">{{ presellItem.itemCode }}</div>
        </div>
        <div class="item rl-text-gray rl-clear rl-margin-bottom-xxxs">
          <div class="rl-fl rl-margin-right-double">规 格</div>
          <div class="rl-fl">{{ presellItem.specificationName }}</div>
        </div>
        <div class="item rl-text-gray rl-clear rl-margin-bottom-xxxs">
          <div class="rl-fl rl-margin-right-double">颜 色</div>
          <div class="rl-fl">{{ presellItem.colorName }}</div>
        </div>
        <div class="item rl-text-gray rl-clear rl-margin-bottom-xxxs">
          <div class="rl-fl rl-margin-right-default left">订购数量</div>
          <div class="rl-fl">
            <el-input-number
              v-model="presellNum"
              @change="presellHandleChange($event, presellItem)"
              :min="0"
              label="描述文字"
              size="mini"
            ></el-input-number>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="warning" @click="confirmpSesellShop">{{
          $t("P.BuyNow")
        }}</el-button>
        <el-button type="primary" @click="joinShopCarts">{{
          $t("P.AddToCart")
        }}</el-button>
        <el-button @click="visibleDialog = false">{{
          $t("P.Cancel")
        }}</el-button>
      </div>
    </el-dialog>
    <!--加载动画-->
    <loading v-if="this.showLoading === true"></loading>

    <!-- 配送方式选择 -->
    <chooseDelivery
      v-show="showChooseDelivery"
      :deliveryData="deliveryData"
      :values="2"
      @handleClose="handleCloseDelivery"
    ></chooseDelivery>
  </div>
</template>

<script>
import Vue from "vue";
import Footer from "@/components/Footer.vue";
import Header from "@/components/Header.vue";
import { fomatFloat, formatDate, orderHint } from "@/assets/js/common.js";
import { MessageBox } from "element-ui";
import sha1 from "js-sha1";
import songSum from "@/components/songSum.vue";
import loading from "@/components/loading.vue";
import GD from "@/assets/js/globalData";
import chooseDelivery from "@/components/dialog/chooseDelivery.vue";
// liu--
import {
  promotionList,
  promotionRuleList,
  promotionRuleGoodList,
  goodShopSetting,
  listStockByCondition,
  priceItemList,
  addShoppingcart,
  shoppingcartList,
} from "@/apiService/api";

export default {
  name: "ActivityColumn",
  components: { Footer, Header, songSum, loading, chooseDelivery },
  data() {
    return {
      activitys: true,
      showLoading: false,
      activityIndex: 0,
      presentDialogVisible: false,
      presentCount: 0,
      presentGoodsList: [], // 赠品
      userId: "",
      gradeId: "",
      curPage: 1,
      curPageSize: 10,
      curPageTotal: 0,
      apage: 1,
      apageSize: 10,
      atotalCount: 0,
      loadsing: false,
      visibleDialog: false, // 库存不足弹框
      presellNum: 0,
      presellItem: {}, // 预售货品
      currentRuleId: "",
      searchContent: "",
      onWayAttendEventFlag: 0, // 在途商品是否参与活动 1.参与，0.不参与
      customizedAttendEventFlag: 0, // 定制商品是否参与活动 1.参与，0.不参与
      mtoAttendEventFlag: 0, // mto订单是否参与活动 1.参与，0.不参与
      shopCatShop: [], // 立即购买选中的货品
      activityDataList: [], //活动列表
      activityRuleName: "",
      activityRuleNameEn: "",
      activityRuleStartTime: "",
      activityRuleEndTime: "",
      hasCommonOrder: false, // 判断是否有普通商品
      hasCustromOrder: false, // 判断是否有定制商品
      activityRuleDetails: [],
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: "zh-RMB", // 语种
      showChooseDelivery: false, // 配送方式选择弹窗
      deliveryData: [], // 配送方式选择弹窗列表数据
      presentList: [], //赠品列表
      presentBuyGoodsList: [], // 已选赠品列表
      giftNum: 0, // 已为您选择赠品数量
      presentStockNum: 0, // 赠品总库存数
    };
  },
  filters: {
    keepTwoNum(value) {
      value = Number(value);
      return value.toFixed(2);
    },
    formatDate(time) {
      var date = new Date(time);
      return formatDate(date, "yyyy-MM-dd hh:mm:ss");
    },
  },
  computed: {
    hasChoose() {
      var total = 0;
      this.presentGoodsList.forEach((item) => {
        if (item.num) {
          total += Number(item.num);
        }
      });
      return total;
    },
  },
  mounted() {
    this.goodShopSettingFun(); //获取定制或者mto是否参与活动
  },
  methods: {
    //获取定制或者mto是否参与活动
    goodShopSettingFun() {
      goodShopSetting().then((res) => {
        this.onWayAttendEventFlag = res.data.onWayAttendEventFlag;
        this.customizedAttendEventFlag = res.data.customizedAttendEventFlag; // 定制商品是否参与活动
        this.mtoAttendEventFlag = res.data.mtoAttendEventFlag; // mto订单是否参与活动
      });
    },
    fLangChange(value) {
      window.localStorage.setItem("bLang", value);
      this.$i18n.locale = value.split("-")[0];
    },
    // 在途在库切换--Y
    changeTu(goods, type) {
      this.$forceUpdate(); // 更新页面数据
      if (type === true) {
        this.$set(goods, "tuType", false);
      } else {
        this.$set(goods, "tuType", true);
      }
    },

    // 货品列表分页-Y
    handleCurrentChange(val, item) {
      this.curPage = val;
      this.getRuleData();
    },
    // 货品列表切换码数
    handleSizeChange(val) {
      this.curPageSize = val;
      this.getRuleData(1);
    },
    ahandleCurrentChange(val) {
      this.apage = val;
      this.getActivityData();
    },
    ahandleSizeChange(val) {
      this.apageSize = val;
      this.getActivityData();
    },

    // 改变预售货品数量
    presellHandleChange(val, item) {
      if (val < item.moq) {
        this.$message.warning("订购数量不可小于" + item.moq);
        this.presellNum = item.moq;
      }
    },
    // Y
    caculateData(items, item) {
      let totalPrice = 0;
      let totalCounts = 0;
      let totalAllCounts = 0;
      let totalPriceGroup = 0;
      let totalCountsGroup = 0;
      let maxNum = 0;
      let maxPrice = 0;
      let t = 0;
      if (items.isAddUp === 0) {
        // 活动商品是否累计，1是 0否
        if (this.onWayAttendEventFlag === 1) {
          // 在途参与活动
          maxNum = items.chooseItem[0].num;
          maxPrice = items.chooseItem[0].num * items.chooseItem[0].salePrice;
          for (var i = 1; i < items.chooseItem.length; i++) {
            if (items.chooseItem[i].num > maxNum) {
              maxNum = items.chooseItem[i].num;
              maxPrice =
                items.chooseItem[i].num * items.chooseItem[i].salePrice;
            }
          }
        } else {
          maxNum = items.chooseItem[0].showNumInWarehouse;
          maxPrice =
            items.chooseItem[0].showNumInWarehouse *
            items.chooseItem[0].salePrice;
          for (var j = 1; j < items.chooseItem.length; j++) {
            if (items.chooseItem[j].showNumInWarehouse > maxNum) {
              maxNum = items.chooseItem[j].showNumInWarehouse;
              maxPrice =
                items.chooseItem[j].showNumInWarehouse *
                items.chooseItem[j].salePrice;
            }
          }
        }
        totalCounts = maxNum;
        totalAllCounts = maxNum;
        totalPrice = maxPrice;
      } else {
        items.chooseItem.forEach((obj) => {
          // 总价计算
          if (this.onWayAttendEventFlag === 1) {
            // 在途参与活动
            totalPrice += obj.num * obj.salePrice;
            totalCounts += obj.num;
            totalAllCounts += obj.num;
          } else {
            totalPrice += obj.showNumInWarehouse * obj.salePrice;
            totalCounts += obj.showNumInWarehouse;
            totalAllCounts += obj.num;
          }
        });
      }
      items.totalMoney = totalPrice;
      items.chooseCount = totalCounts;
      items.chooseTotalCount = totalAllCounts;
      console.log("---++:", items);
      // 产品
      if (items.groups.length > 0) {
        items.groups.forEach((obj) => {
          if (obj.id === items.currentGroupId) {
            obj.chooseGroupItem.forEach((val) => {
              if (item.id === val.id) {
                val.num = item.num;
                t = 1;
              }
            });
            if (t === 0) {
              obj.chooseGroupItem.push(item);
            }
            obj.chooseGroupItem.forEach((val) => {
              totalPriceGroup += val.num * val.salePrice;
              totalCountsGroup += val.num;
            });
            obj.groupTotalMoney = totalPriceGroup;
            obj.groupChooseCount = totalCountsGroup;
          }
        });
      }

      let ruleCondition = null; // 判断满足条件选择最优
      items.conditions.forEach((obj) => {
        obj.meet = false; // 先把满足条件状态设置为false
        if (obj.groupConditions && obj.groupConditions.length > 0) {
          // 有分组
          Vue.set(obj, "ruleConditionsCount", 0); // 组合计算总数
          Vue.set(obj, "ruleConditionsMoney", 0); // 组合计算总金额
          obj.groupConditions.forEach((item, index) => {
            let temp = [];
            Vue.set(item, "meetType", false); // 组合活动规则条件满足标志
            Vue.set(item, "groupTotalCount", 0); // 组合活动规则条件货品购买总数
            Vue.set(item, "groupTotalMoney", 0); // 组合活动规则条件货品购买金额
            temp = item.goodsItemIds.split(",");
            for (let i = 0; i < temp.length; i++) {
              for (let j = 0; j < items.groups.length; j++) {
                for (
                  let k = 0;
                  k < items.groups[j].chooseGroupItem.length;
                  k++
                ) {
                  if (
                    Number(temp[i]) ===
                    Number(items.groups[j].chooseGroupItem[k].id)
                  ) {
                    item.groupTotalCount +=
                      items.groups[j].chooseGroupItem[k].num;
                    item.groupTotalMoney +=
                      items.groups[j].chooseGroupItem[k].num *
                      items.groups[j].chooseGroupItem[k].salePrice;
                  }
                }
              }
            }
            if (items.moneyOrCount === 1) {
              // 一次性购买按金额还是数量 1金额 2数量
              obj.ruleConditionsMoney += item.oneBuyMoney;
              if (item.groupTotalMoney >= item.oneBuyMoney) {
                item.meetType = true;
              }
            } else if (items.moneyOrCount === 2) {
              obj.ruleConditionsCount += item.oneBuyCount;
              if (item.groupTotalCount >= item.oneBuyCount) {
                item.meetType = true;
              }
            }
          });
          let b = true;
          for (let n = 0; n < obj.groupConditions.length; n++) {
            if (obj.groupConditions[n].meetType === false) {
              b = false;
              break;
            }
          }
          if (b) {
            if (
              ruleCondition === null ||
              (ruleCondition !== null &&
                ((items.moneyOrCount === 2 &&
                  ruleCondition.ruleConditionsCount <
                    obj.ruleConditionsCount) ||
                  (items.moneyOrCount === 1 &&
                    ruleCondition.ruleConditionsMoney <
                      obj.ruleConditionsMoney)))
            ) {
              ruleCondition = obj;
            }
          }
        } else {
          if (
            (items.moneyOrCount === 1 && items.totalMoney >= obj.oneBuyMoney) ||
            (items.moneyOrCount === 2 && items.chooseCount >= obj.oneBuyCount)
          ) {
            // 一次性购买按金额还是数量 1金额 2数量
            if (ruleCondition === null) {
              ruleCondition = obj;
            } else if (
              ruleCondition !== null &&
              ((items.moneyOrCount === 1 &&
                obj.oneBuyMoney > ruleCondition.oneBuyMoney) ||
                (items.moneyOrCount === 2 &&
                  obj.oneBuyCount > ruleCondition.oneBuyCount))
            ) {
              ruleCondition = obj;
            }
          }
        }
      });
      if (ruleCondition !== null) {
        // 满足条件最优那条状态设置为true，其他设为false
        ruleCondition.meet = true;
      }
    },
    // 切换分组--产品  ---N
    changeGroup(item, index, group) {
      this.$nextTick(() => {
        item.chooseCount = 0; // 切换分组清0
        item.chooseTotalCount = 0;
        item.totalMoney = 0;
        item.groupIndex = index;
        item.currentGroupId = group.id;
        this.showLoading = true;
        this.activityRuleDetails.forEach((itemm) => {
          let goodss = [];
          this.getItemData(itemm, goodss, group.id);
          this.$api
            .get(this, "user/u/marketing/promotion/goods/count", {
              ruleId: itemm.id,
              ruleGroupId: group.id,
            })
            .then((res) => {
              // 获取总数
              if (res.code === 0) {
                itemm.totalCount = res.count;
              }
            });
        });
      });
    },
    // 切换活动--Y
    changeActivity(val, index) {
      this.apage = 1;
      this.curPage = 1;
      this.activityIndex = index;
      this.getRuleListDta(val.id, val.onWayFlag);
      this.giftNum = 0;
    },
    // 搜索--Y
    goSearch() {
      this.activityIndex = 0;
      this.getActivityData();
    },

    // 获取活动列表
    getActivityData() {
      this.showLoading = true;
      let params = {
        content: this.searchContent,
        page: this.apage,
        size: this.apageSize,
      };
      promotionList(params).then((res) => {
        if (res.success) {
          this.showLoading = false;
          this.atotalCount = res.data.total;
          if (res.data.list && res.data.list.length > 0) {
            this.activityDataList = res.data.list;
            console.log("活动列表：", res.data.list);
            
            this.getRuleListDta(res.data.list[0].id,res.data.list[0].onWayFlag);
          } else {
            this.activityRuleDetails = [];
          }
        } else {
          this.showLoading = false;
          this.$message(res.errMessage);
        }
      });
    },

    // 根据活动id获取规则列表
    getRuleListDta(id, onWayFlag) {
      promotionRuleList({ id: id }).then((res) => {
        if (res.success) {
          console.log("活动规则", res.data);
          res.data.forEach((item) => {
            this.$set(item, "onWayFlag", onWayFlag);
          });
          this.activityRuleDetails = res.data;

          this.getRuleData();
        }
      });
    },

    // 获取规则下的相关信息
    getRuleData() {
      this.activityRuleDetails.forEach((itemm) => {
        let ruleGroupId = "";
        Vue.set(itemm, "itemData", []);
        Vue.set(itemm, "tuType", false); // 未勾选包含在途库存
        Vue.set(itemm, "totalCount", 0); // 货品总数
        Vue.set(itemm, "chooseCount", 0); // 选中的货品总数
        Vue.set(itemm, "chooseTotalCount", 0); // 选中的最终货品总数（含购物车）
        Vue.set(itemm, "totalMoney", 0); // 总计金额
        Vue.set(itemm, "chooseItem", []); // 选中的货品
        Vue.set(itemm, "groupIndex", 0); // 选中的分组索引
        Vue.set(itemm, "currentGroupId", 0); // 选中的分组id
        Vue.set(itemm, "groups", []); // 产品列表
        if (itemm.groups.length > 0) {
          ruleGroupId = itemm.groups[0].id;
          itemm.currentGroupId = itemm.groups[0].id;
          itemm.groups.forEach((item) => {
            Vue.set(item, "chooseGroupItem", []);
            Vue.set(item, "groupTotalMoney", 0);
            Vue.set(item, "groupChooseCount", 0);
          });
        }
        this.getItemData(itemm); // 根据规则id获取货品
      });
    },
    // 根据规则id获取货品
    getItemData(itemm) {
      let params = {
        id: itemm.id,
        page: this.curPage,
        size: this.curPageSize,
      };
      promotionRuleGoodList(params).then((res) => {
        if (res.success) {
          this.showLoading = false;
          Vue.set(itemm, "isNotShowImg", false); // 是否展示图片
          itemm.totalCount = res.data.total;
          itemm.itemData = res.data.list || [];

          this.curPageTotal = res.data.total;

          if (itemm.chooseItem && itemm.chooseItem.length > 0) {
            for (var m = 0; m < itemm.itemData.length; m++) {
              for (var n = 0; n < itemm.chooseItem.length; n++) {
                if (itemm.itemData[m].id === itemm.chooseItem[n].id) {
                  Vue.set(itemm.itemData[m], "num", itemm.chooseItem[n].num); // 数量
                  break;
                }
              }
            }
          }
          let goodsids = []; //货品ids
          let twoGoodsIdList = []; //查询货品的价格
          for (var i = 0; i < itemm.itemData.length; i++) {
            goodsids.push(itemm.itemData[i].id);
            let itemsIdObj = {
              goodsId: itemm.itemData[i].goodsId, //商品id
              itemId: itemm.itemData[i].id, //货品id
            };
            twoGoodsIdList.push(itemsIdObj);
            if (itemm.itemData[i].num && itemm.itemData[i].num > 0) {
              itemm.chooseTotalCount += itemm.itemData[i].num;
            } else {
              Vue.set(itemm.itemData[i], "num", 0); // 数量
            }

            if (itemm.itemData[i].diyList) {
              itemm.isNotShowImg = true;
            }
          }
          if (goodsids.length > 0) {
            this.stockDataFun(goodsids, itemm.itemData, itemm.id);
          }

          this.getGoodsPrice(twoGoodsIdList, itemm.itemData);
        } else {
          this.showLoading = false;
        }
      });
    },

    // 获取商品库存
    stockDataFun(itemIds, goodsItems, ruleId) {
      let userId = localStorage.getItem("userId");
      // 获取商品库存
      let params = {
        distributorId: userId,
        itemIdList: itemIds,
      };
      listStockByCondition(params).then((stockRes) => {
        if (stockRes.success) {
          let stockResData = stockRes.data;
          for (let i = 0; i < stockResData.length; i++) {
            goodsItems.forEach((items) => {
              Vue.set(items, "showNumInWarehouse", 0); // 显示在库数量
              Vue.set(items, "showStockItemCount", 0); // 显示在途数量
              Vue.set(items, "ruleId", ruleId); // 规则id
              Vue.set(items, "itemType", 1);
              Vue.set(items, "orderType", 1);
              if (items.id === stockResData[i].itemId) {
                this.$set(
                  items,
                  "numInWarehouse",
                  stockResData[i].inStockUsableCount
                ); // 在库库存
                this.$set(
                  items,
                  "stockItemCount",
                  stockResData[i].onWayUsableCount
                ); // 在途库存
                this.$set(
                  items,
                  "inStockUsableCount",
                  stockResData[i].inStockUsableCount
                ); // 在库库存
                this.$set(
                  items,
                  "onWayUsableCount",
                  stockResData[i].onWayUsableCount
                ); // 在途库存
                if (items.numInWarehouse >= items.num) {
                  // 在库数量大于等于购买数量
                  this.$set(items, "showNumInWarehouse", items.num);
                  this.$set(items, "showStockItemCount", 0);
                } else if (items.numInWarehouse <= 0) {
                  // 在库数量小于等于0
                  items.numInWarehouse = 0;
                  this.$set(items, "showNumInWarehouse", 0);
                  this.$set(items, "showStockItemCount", items.num);
                } else if (items.stockItemCount <= 0) {
                  this.$set(items, "stockItemCount", 0); // 在途库存
                } else {
                  this.$set(items, "showNumInWarehouse", items.numInWarehouse);
                  this.$set(
                    items,
                    "showStockItemCount",
                    items.num - items.numInWarehouse
                  );
                }
              }
            });
          }
          console.log("获取库存的货品", goodsItems);
        }
      });
    },

    // 获取货品价格
    getGoodsPrice(twoGoodsIdList, goodsItems) {
      let paramsPrice = {
        goodsItemIds: twoGoodsIdList,
      };
      if (twoGoodsIdList && twoGoodsIdList.length > 0) {
        priceItemList(paramsPrice).then((res) => {
          if (res.success) {
            let priceList = res.data;
            goodsItems.forEach((item) => {
              priceList.forEach((items) => {
                if (items.itemId == item.id) {
                  this.$set(item, "retailPrice", items.retailPrice);
                  this.$set(item, "salePrice", items.salePrice);
                }
              });
            });
          }
        });
      }
    },
    //商品加减号操作
    handleChange(val, item, items) {
      console.log("添加的数量：", val);
      console.log("货品信息", item);
      console.log("货品活动模块信息", items);
      console.log("合计：", items.chooseTotalCount);
      let duiBiNum = items.tuType
        ? item.inStockUsableCount + item.onWayUsableCount
        : item.inStockUsableCount;

      if (Number(val) > duiBiNum) {
        this.$message("库存不足");
        return;
      }
      if (val <= item.inStockUsableCount) {
        this.$set(item, "zaiKuCount", Number(val));
        this.$set(item, "zaiTuCount", 0);
      } else {
        this.$set(item, "zaiKuCount", item.inStockUsableCount);
        this.$set(item, "zaiTuCount", Number(val) - item.inStockUsableCount);
      }

      let chooseTotalCount = 0;
      items.itemData.forEach((item1) => {
        if (item1.num > 0) {
          chooseTotalCount += item1.num;
        }
      });
      this.$set(items, "chooseTotalCount", chooseTotalCount);

      // 计算活动价格
      let lastPrice = item.salePrice * item.num;
      let presentList = []; //赠品列表
      let presentCount = 0; //赠品最多选择的数量
      let addUpFlag = items.addUpFlag; //活动是否累计
      let onWayFlag = items.onWayFlag; //在途商品是否参与活动 1.参与，0.不参与
      let zaiKuCount =
        val <= item.inStockUsableCount ? val : item.inStockUsableCount;
      let zaiTuCount =
        val <= item.inStockUsableCount ? 0 : val - item.inStockUsableCount;
      let isOneBuyCount = onWayFlag == 0 ? zaiKuCount : zaiKuCount + zaiTuCount;
      let isOneBuyMoney =
        onWayFlag == 0
          ? zaiKuCount * item.salePrice
          : item.num * item.salePrice;
      if (addUpFlag == 1) {
        let childrenList = items.itemData;
        let zCoun = zaiKuCount;
        let tCount = zaiTuCount;
        let zPrice = zaiKuCount * item.salePrice;
        let tPrice = zaiTuCount * item.salePrice;
        for (let i = 0; i < childrenList.length; i++) {
          // 判断同个活动累计数量
          if (childrenList[i].id != item.id && childrenList[i].num > 0) {
            zCoun += childrenList[i].zaiKuCount;
            tCount += childrenList[i].zaiTuCount;
            zPrice += childrenList[i].zaiKuCount * childrenList[i].salePrice;
            tPrice += childrenList[i].zaiTuCount * childrenList[i].salePrice;
          }
        }
        isOneBuyCount = onWayFlag == 0 ? zCoun : zCoun + tCount;
        isOneBuyMoney = onWayFlag == 0 ? zPrice : zPrice + tPrice;
      }

      items.conditions.forEach((cItem) => {
        // moneyOrCount 规则形式：1金额 2数量
        if (
          (items.moneyOrCount == 1 && isOneBuyMoney >= cItem.oneBuyMoney) ||
          (items.moneyOrCount == 2 && isOneBuyCount >= cItem.oneBuyCount)
        ) {
          this.$set(item, "conditionsId", cItem.id);
          this.$set(item, "conditionName", cItem.conditionName);
          if (addUpFlag == 1) {
            let childrenList = items.itemData;
            for (let i = 0; i < childrenList.length; i++) {
              this.$set(childrenList[i], "conditionsId", cItem.id);
              this.$set(item, "conditionName", cItem.conditionName);
            }
          }
          // specialFlag 是否特价，1是 0否
          if (cItem.specialFlag == 1) {
            lastPrice =
              onWayFlag == 0
                ? zaiKuCount * cItem.specialPrice + zaiTuCount * item.salePrice
                : item.num * cItem.specialPrice;
            //  活动累计情况下，相同的货品都按特价计算
            if (addUpFlag == 1) {
              let childrenList = items.itemData;
              for (let i = 0; i < childrenList.length; i++) {
                if (childrenList[i].id != item.id && childrenList[i].num > 0) {
                  let childrenTotalPrice =
                    onWayFlag == 0
                      ? childrenList[i].zaiKuCount * cItem.specialPrice +
                        childrenList[i].zaiTuCount * childrenList[i].salePrice
                      : childrenList[i].count * cItem.specialPrice;
                  this.$set(childrenList[i], "lastPrice", childrenTotalPrice);
                }
                this.$set(childrenList[i], "conditionsId", cItem.id);
                this.$set(
                  childrenList[i],
                  "conditionName",
                  cItem.conditionName
                );
              }
            }
          } else {
            // reduceOrPresent 促销统计方式：1满减 2满赠
            if (cItem.reduceOrPresent == 1) {
              //  满减类型，1减免 2折扣
              if (cItem.reduceType == 1) {
                //  活动累计情况下，勾选上的货品都按减免计算
                if (addUpFlag == 1) {
                  let childrenList = items.itemData;
                  for (let i = 0; i < childrenList.length; i++) {
                    if (childrenList[i].num > 0) {
                      let reduction2 = cItem.reduction;
                      if (cItem.reductionPresentAddFlag == 1) {
                        reduction2 =
                          items.moneyOrCount == 1
                            ? onWayFlag == 0
                              ? childrenList[i].zaiKuCount *
                                childrenList[i].salePrice *
                                (cItem.reduction / cItem.oneBuyMoney)
                              : childrenList[i].num *
                                childrenList[i].salePrice *
                                (cItem.reduction / cItem.oneBuyMoney)
                            : onWayFlag == 0
                            ? childrenList[i].zaiKuCount *
                              (cItem.reduction / cItem.oneBuyCount)
                            : childrenList[i].num *
                              (cItem.reduction / cItem.oneBuyCount);
                      } else {
                        reduction2 =
                          (reduction2 / isOneBuyCount) * childrenList[i].num;
                      }

                      let childrenTotalPrice = fomatFloat(
                        childrenList[i].num * childrenList[i].salePrice -
                          reduction2,
                        2
                      );
                      if (childrenList[i].id == item.id) {
                        lastPrice = childrenTotalPrice;
                      }
                      this.$set(
                        childrenList[i],
                        "lastPrice",
                        childrenTotalPrice
                      );
                    }
                    this.$set(childrenList[i], "conditionsId", cItem.id);
                    this.$set(
                      childrenList[i],
                      "conditionName",
                      cItem.conditionName
                    );
                  }
                } else {
                  let reduction = cItem.reduction;
                  // 判断是否叠加的减免金额
                  if (cItem.reductionPresentAddFlag == 1) {
                    reduction =
                      items.moneyOrCount == 1
                        ? onWayFlag == 0
                          ? zaiKuCount *
                            item.salePrice *
                            (cItem.reduction / cItem.oneBuyMoney)
                          : item.num *
                            item.salePrice *
                            (cItem.reduction / cItem.oneBuyMoney)
                        : onWayFlag == 0
                        ? zaiKuCount * (cItem.reduction / cItem.oneBuyCount)
                        : item.num * (cItem.reduction / cItem.oneBuyCount);
                  }

                  lastPrice = fomatFloat(
                    item.num * item.salePrice - reduction,
                    2
                  );
                }
              } else {
                //折扣
                let zhekouPrice = fomatFloat(
                  (item.salePrice * cItem.discount) / 100,
                  2
                );
                lastPrice =
                  onWayFlag == 0
                    ? zaiKuCount * zhekouPrice + zaiTuCount * item.salePrice
                    : (zaiKuCount + zaiTuCount) * zhekouPrice;
                //  活动累计情况下，相同的货品都按减免计算
                if (addUpFlag == 1) {
                  let childrenList = items.itemData;
                  for (let i = 0; i < childrenList.length; i++) {
                    if (
                      childrenList[i].id != item.id &&
                      childrenList[i].num > 0
                    ) {
                      let zhekouPrice2 = fomatFloat(
                        (childrenList[i].salePrice * cItem.discount) / 100,
                        2
                      );
                      let childrenTotalPrice =
                        onWayFlag == 0
                          ? childrenList[i].zaiKuCount * zhekouPrice2 +
                            childrenList[i].zaiTuCount *
                              childrenList[i].salePrice
                          : childrenList[i].num * zhekouPrice2;
                      this.$set(
                        childrenList[i],
                        "lastPrice",
                        childrenTotalPrice
                      );
                    }
                    this.$set(childrenList[i], "conditionsId", cItem.id);
                    this.$set(
                      childrenList[i],
                      "conditionName",
                      cItem.conditionName
                    );
                  }
                }
              }
            } else {
              cItem.presents.forEach((preSentItem) => {
                this.$set(preSentItem, "num", 0);
                this.$set(preSentItem, "itemCount", 0);
              });
              
              let parseIntCount =
                items.moneyOrCount == 1 
                  ? parseInt(item.totalPrice / cItem.oneBuyMoney) > 0
                    ? parseInt(item.totalPrice / cItem.oneBuyMoney)
                    : 0
                  : parseInt(item.num / cItem.oneBuyCount) > 0
                    ? parseInt(item.num / cItem.oneBuyCount)
                    : 0;
              let totalPresentCount = 
                cItem.reductionPresentAddFlag == 1
                  ? parseIntCount * cItem.presentCount
                  : cItem.presentCount;
              presentCount =
                totalPresentCount > cItem.presents[0].count
                  ? cItem.presents[0].count
                  : totalPresentCount;
              
              // 是否叠加 reductionPresentAddFlag：1 是，0 否
              if (cItem.reductionPresentAddFlag == 1) {
                let dieJiaNum = 0;
                // 计算叠加的数量/金额
                if (items.moneyOrCount == 1) {
                  dieJiaNum = parseInt(isOneBuyMoney / cItem.oneBuyMoney);
                } else {
                  dieJiaNum = parseInt(isOneBuyCount / cItem.oneBuyCount);
                }
                presentCount = cItem.presentCount * dieJiaNum;
              }

              presentList = cItem.presents;
            }
          }
        }
      });
      this.$set(item, "lastPrice", lastPrice);
      this.$set(items, "presentCount", presentCount);
      this.$set(item, "presentList", presentList);
      console.log("添加的赠品列表后的数据：", item);

      // 计算已选赠品数量
      this.handleGiftNum(items);
    },

    // 计算已选赠品数量
    handleGiftNum(val) {
      let list = val.itemData;
      let presentTotalCount = val.presentCount;
      list.forEach((item) => {
        if (item.num && item.num > 0) {
          this.$set(item, "itemCount", item.num);
          this.$set(item, "totalPrice", item.lastPrice);
          this.$set(item, "goodsPromotionId", item.promotionId);
          if (item.num <= item.inStockUsableCount) {
            this.$set(item, "zaiKuCount", item.num);
            this.$set(item, "zaiTuCount", 0);
          } else {
            this.$set(item, "zaiKuCount", item.inStockUsableCount);
            this.$set(item, "zaiTuCount", item.num - item.inStockUsableCount);
          }

          let handlePresentList = []; // 过滤无库存 - 赠品列表
          let ptesentIds = []; //赠品ids
          // 集合赠品列表
          if (item.presentList && item.presentList.length > 0) {
            item.presentList.forEach((itemP) => {
              ptesentIds.push(itemP.itemId);
            });

            // 获取商品库存
            let stockParams = {
              distributorId: this.userId,
              itemIdList: ptesentIds,
            };

            listStockByCondition(stockParams).then((stockRes) => {
              if (stockRes.success) {
                let stockResData = stockRes.data;
                for (let i = 0; i < stockResData.length; i++) {
                  item.presentList.forEach((item2) => {
                    this.$set(item2, "isDisabled", false);
                    if (item2.itemId === stockResData[i].itemId) {
                      item2.inStockUsableCount =
                        stockResData[i].inStockUsableCount > 0
                          ? stockResData[i].inStockUsableCount
                          : 0; // 在库库存
                      item2.onWayUsableCount =
                        stockResData[i].numOnWaySum > 0
                          ? stockResData[i].numOnWaySum
                          : 0; // 在途库存
                      let kucunCount =
                        item2.inStockUsableCount + item2.onWayUsableCount;
                      this.$set(item2, "kucunCount", kucunCount);
                      this.$set(item2, "presentCount", item.presentCount);

                      // 过滤无库存赠品
                      if (kucunCount > 0) {
                        handlePresentList.push(item2);
                      }
                    }
                  });
                }

                let presentStockNum = 0; // 赠品库存数
                handlePresentList.forEach((pre) => {
                  if (pre.kucunCount > 0) {
                    presentStockNum += pre.kucunCount;
                  }
                });

                // 赠品列表
                let preList = {
                  id: item.promotionRuleId,
                  ruleName: val.ruleName,
                  conditionName: item.conditionName, // 条件名称
                  presentCount: presentTotalCount, // 满赠数量
                  count: presentTotalCount, // 已选数量（默认已选）
                  presents: handlePresentList,
                  presentStockNum: presentStockNum, // 赠品库存总数
                };
                this.presentList = [].concat(preList);

                this.handleGiftCount(); // 赠品默认值
              }
            });
          }
        }
      });

      this.giftNum = presentTotalCount;
    },

    // 赠品默认值
    handleGiftCount() {
      let giftNum = 0; // 已为您选择赠品数量默认值
      this.presentList.forEach((present) => {
        giftNum += present.presentCount;
        this.presentStockNum = present.presentStockNum; // 赠品总库存数

        // 判断可选数量
        if (present.presentCount > 0) {
          // 平均分配数
          let count = Math.floor(
            present.presentCount / present.presents.length
          );
          // 平均分配剩余数量
          let yuCount = present.presentCount % present.presents.length;

          let queCount = 0; // 库存不足时，缺少数量值
          // 平均分配选择赠品数量
          present.presents.forEach((child, cIndex) => {
            // 库存判断
            if (count > child.kucunCount) {
              // 库存不足，取库存数
              this.$set(child, "num", child.kucunCount);

              queCount += count - child.kucunCount;
            } else {
              // 库存充足，取平均值
              this.$set(child, "num", count);
            }
            this.$set(child, "itemCount", child.num);
          });

          // 多出的，判断库存，分配给库存充足货品
          let totalCount = count + yuCount + queCount;
          present.presents.forEach((child) => {
            // 判断多出数量
            if (totalCount > 0) {
              // 判断货品库存与多出库数量
              if (child.kucunCount > totalCount) {
                // 库存充足，直接赋值
                this.$set(child, "num", totalCount);
                totalCount -= totalCount;
              } else {
                // 库存不足，判断货品库存与已选数量差值
                let num = child.kucunCount - child.num;
                if (num > 0) {
                  this.$set(child, "num", child.kucunCount);
                  totalCount -= num;
                }
              }
            }
            this.$set(child, "itemCount", child.num);
          });
        }
      });

      // 判断赠品总库存数与选择赠品数，设置已选赠品值
      if (this.presentStockNum >= giftNum) {
        this.giftNum = giftNum;
        this.presentList.forEach((present) => {
          this.$set(present, "count", giftNum);
        });
      } else {
        this.giftNum = this.presentStockNum;
        this.presentList.forEach((present) => {
          this.$set(present, "count", this.presentStockNum);
        });
      }
    },

    // 立即购买
    activityBuyGoods(val) {
      console.log("活动立即购买：", val);
      if (val.chooseTotalCount === 0) {
        this.$message.warning(this.$t("P.GoodsItemNull"));
        return false;
      }

      let list = val.itemData;
      let orderList = [];
      list.forEach((item) => {
        if (item.num && item.num > 0) {
          orderList.push(item);
        }
      });

      if (orderList.length > 0) {
        this.deliveryData = orderList;
      }

      // 赠品
      if (this.presentList.length > 0) {
        this.presentsConfirmFun(); // 赠品确定
        if (this.presentBuyGoodsList.length > 0) {
          this.deliveryData = [
            ...this.deliveryData,
            ...this.presentBuyGoodsList,
          ];
          localStorage.setItem(
            "shopOrderList",
            JSON.stringify(this.deliveryData)
          );
          console.log("要下单的货品：", this.deliveryData);
          this.$router.push({
            name: "ConsigneeInfor",
            query: { values: 1, onWaySplitFlag: 0, isTwoWay: 0 },
          }); // 跳转下单页；
        } else {
          // 未选赠品弹窗
          MessageBox.confirm(this.$t("Message.One"), this.$t("Message.Two"), {
            confirmButtonText: this.$t("Message.Confirm"),
            cancelButtonText: this.$t("Message.Cancel"),
            type: "warning",
          })
            .then(() => {
              localStorage.setItem(
                "shopOrderList",
                JSON.stringify(this.deliveryData)
              );
              this.$router.push({
                name: "ConsigneeInfor",
                query: { values: 1, onWaySplitFlag: 0, isTwoWay: 0 },
              }); // 跳转下单页；
            })
            .catch(() => {
              this.presentDialogVisible = true;
            });
        }
      } else {
        console.log("要下单的货品列表：", orderList);
        // 判断是否在途在库一起下单情况
        let zaiTuFlag = false;
        let zaiKuFlag = false;
        orderList.forEach((item) => {
          if (item.zaiKuCount > 0) {
            zaiKuFlag = true;
          }
          if (item.zaiTuCount > 0) {
            zaiTuFlag = true;
          }
        });
        if (zaiKuFlag && zaiTuFlag) {
          this.showChooseDelivery = true;
        } else {
          localStorage.setItem("shopOrderList", JSON.stringify(orderList));
          // this.$router.push({name: 'ConsigneeInfor',query: {values: 1,onWaySplitFlag: 0,isTwoWay:0,}});  //跳转下单页；
        }
      }
    },

    // 加入购物车
    activityJoinCat(val) {
      this.loadsing = false;
      console.log("加入购物车：", val);
      if (val.chooseTotalCount === 0) {
        this.$message.warning(this.$t("P.GoodsItemNull"));
        return false;
      }
      let list = val.itemData;
      let paramsList = [];
      list.forEach((item) => {
        if (item.num > 0) {
          let objL = {
            imageUrl: item.itemImg,
            cartType: 1, //加购类型：1 分销商 2 C端客户
            diyType: item.diyType, //定制类型 0-标准定制 1-DIY定制
            goodsId: item.goodsId, //商品id
            goodsName: item.goodsName,
            goodsNo: item.goodsNo, //商品编码
            goodsPromotionId: item.promotionRuleId ? item.promotionRuleId : "", //商品促销活动Id(活动条件id)
            goodsType: item.goodsType, //商品类型 1-普通 2-定制
            groupSeckillId: item.groupSeckillId ? item.groupSeckillId : "",
            itemCode: item.itemCode, //货品编码
            itemCount: item.num, //购物数量
            itemId: item.id, //货品id
            itemName: item.itemName, //货品名称
            itemNameEn: item.itemNameEn ? item.itemNameEn : "", //货品英文名称
            itemType: item.itemType, //是否赠品 1 非赠品 2 赠品
            specsName: item.specsName, //货品规格
            colorName: item.colorName, //货品颜色
            weight: item.weight, //重量
            lenght: item.length,
            width: item.width,
            height: item.height,
            barCode: item.barCode,
          };
          paramsList.push(objL);
        }
      });

      // 赠品
      if (this.presentList.length > 0) {
        this.presentsConfirmFun(); // 赠品确定
        this.presentBuyGoodsList.forEach((item) => {
          if (item.num > 0) {
            let objL = {
              imageUrl: item.itemImg ? item.itemImg : item.imageUrl1,
              cartType: 1, //加购类型：1 分销商 2 C端客户
              diyType: 1, //定制类型 0-标准定制 1-DIY定制
              goodsId: item.goodsId, //商品id
              goodsName: item.goodsName,
              goodsNo: item.goodsNo, //商品编码
              goodsPromotionId: item.goodsPromotionId, //商品促销活动Id(活动条件id)
              goodsType: item.goodsType, //商品类型 1-普通 2-定制
              itemCode: item.itemCode, //货品编码
              itemCount: item.num, //购物数量
              itemNum: item.num, //购物数量
              itemId: item.itemId, //货品id
              itemName:
                this.$i18n.locale === "zh"
                  ? item.itemName
                  : item.itemNameEn
                  ? item.itemNameEn
                  : item.itemName, //货品名称
              itemNameEn: item.itemNameEn ? item.itemNameEn : "", //货品英文名称
              itemType: item.itemType, //是否赠品 1 非赠品 2 赠品
              specsName:
                this.$i18n.locale === "zh"
                  ? item.specsName
                  : item.specsNameEn
                  ? item.specsNameEn
                  : item.specsName, //货品规格
              colorName:
                this.$i18n.locale === "zh"
                  ? item.colorName
                  : item.colorNameEn
                  ? item.colorNameEn
                  : item.colorName, //货品颜色
              weight: item.weight, //重量
              lenght: item.length,
              width: item.width,
              height: item.height,
              barCode: item.barCode,
            };
            paramsList.push(objL);
          }
        });
      }

      if (paramsList.length > 0) {
        addShoppingcart(paramsList).then((res) => {
          if (res.success) {
            this.$message.success("加入购物车成功");
            this.shoppingcartListFun(); //通过查购物车列表的长度来更新购物车的数量
          } else {
            this.$message(res.errMessage);
          }
        });
      }
    },
    // 查询购物车列表--Y
    shoppingcartListFun() {
      shoppingcartList().then((res) => {
        if (res.success) {
          this.$refs.header.cartCount = res.data.length;
        }
      });
    },

    // 获取赠品库存-Y
    presentStockDataFun(itemIds, goodsItems) {
      let userId = localStorage.getItem("userId");
      // 获取商品库存
      let params = {
        distributorId: userId,
        itemIdList: itemIds,
      };
      listStockByCondition(params).then((stockRes) => {
        if (stockRes.success) {
          let stockResData = stockRes.data;
          for (let i = 0; i < stockResData.length; i++) {
            goodsItems.forEach((items) => {
              Vue.set(items, "numInWarehouse", 0); // 在库库存
              Vue.set(items, "stockItemCount", 0); // 在途库存
              if (items.id === stockResData[i].itemId) {
                items.numInWarehouse = stockResData[i].inStockUsableCount; // 在库库存
                if (items.numInWarehouse <= 0) {
                  // 在库数量小于等于0
                  items.numInWarehouse = 0;
                }
              }
            });
          }
        }
      });
    },

    // 赠品弹框加减号计算
    presentHandleChange(item, items, type) {
      let disCount = Number(item.itemCount) - Number(item.num);

      if (type == 1) {
        // 减
        if (item.num > 0) {
          this.$set(item, "num", Number(item.num) - 1);
          this.$set(item, "itemCount", item.num);
          this.$set(items, "count", Number(items.count) - 1);
        }
      } else if (type == 2) {
        // 加，判断已选数量与可选数量或者已选数量与库存数量
        if (items.count >= items.presentCount || item.num >= item.kucunCount) {
          this.$set(item, "num", Number(item.num));
          this.$set(item, "itemCount", item.num);
          this.$message("赠品数量已达上限");
        } else {
          this.$set(item, "num", Number(item.num) + 1);
          this.$set(item, "itemCount", Number(item.num));
          this.$set(items, "count", Number(items.count) + 1);
        }
      } else {
        // 输入框输入
        if (Number(item.num) >= 0) {
          // 判断输入数量与当前数量
          if (Number(item.num) > Number(item.itemCount)) {
            // 大于，判断已选数量加当前数量与可选数量或者已选数量与库存数量
            if (items.count + Number(item.num) > items.presentCount || item.num > item.kucunCount) {
              // 大于
              this.$set(
                item,
                "num",
                item.itemCount
              );
              this.$set(item, "itemCount", item.num);
              this.$message("赠品数量已达上限");
            } else {
              // 小于或等于
              this.$set(item, "num", Number(item.num));
              this.$set(item, "itemCount", item.num);
              this.$set(items, "count", Number(items.count) - disCount);
            }
          } else {
            // 小于或等于，更新已选数量
            this.$set(item, "num", Number(item.num));
            this.$set(item, "itemCount", item.num);
            this.$set(items, "count", Number(items.count) - disCount);
          }
        } else {
          // 小于或等于，设置为当前数量
          this.$set(item, "num", item.itemCount);
        }
      }

      this.giftNum = items.count;
    },

    // 赠品确定
    presentsConfirmFun() {
      this.presentDialogVisible = false;
      console.log("赠品列表", this.presentList);
      this.presentBuyGoodsList = [];
      let list = this.presentList;
      list.forEach((item) => {
        item.presents.forEach((item2) => {
          if (item2.num > 0) {
            this.$set(item2, "goodsType", 1);
            this.$set(item2, "itemCount", item2.num);
            this.$set(item2, "conditionsId", item2.promotionRuleConditionId);
            this.$set(item2, "goodsPromotionId", item.id);
            this.$set(item2, "totalPrice", 0);
            this.$set(item2, "itemType", 2); //是否赠品 1 非赠品 2 赠品
            if (item2.num <= item2.inStockUsableCount) {
              this.$set(item2, "zaiKuCount", item2.num);
              this.$set(item2, "zaiTuCount", 0);
            } else {
              this.$set(item2, "zaiKuCount", item2.inStockUsableCount);
              this.$set(
                item2,
                "zaiTuCount",
                item2.num - item2.inStockUsableCount
              );
            }
            this.presentBuyGoodsList.push(item2);
          }
        });
      });
    },

    // 去商品详情
    goDoodsDetail(id, goodsType) {
      let routeData = null;
      if (goodsType === 1) {
        // 普通商品

        routeData = this.$router.resolve({
          name: "ShopDetail",
          query: {
            good_id: id,
            activityType: 3, //1:拼团  2：秒杀  3：活动  4：其他
            goodsType: goodsType,
            accessType: 0,
          },
        });
      } else {
        // 定制商品
        routeData = this.$router.resolve({
          name: "ShopDetail",
          query: {
            good_id: id,
            activityType: 3, //1:拼团  2：秒杀  3：活动  4：其他
            goodsType: goodsType,
            accessType: 0,
          },
        });
      }
      window.open(routeData.href, "_blank");
    },

    // 定制
    goCustrom(code) {
      console.log(code);
      // 获取DIY编辑器地址
      this.$confirm("该货品为DIY定制商品，暂未定制，是否去定制?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          // this.custromUrl(code);
          // 跳转定制页面
          this.customize(code);
        })
        .catch(() => {
          this.$message.info("已取消定制!");
        });
    },
    // 跳转定制
    customize(item) {
      localStorage.setItem("diyItem", JSON.stringify(item)); // 存储当前选中货品（定制页面需要）
      // 手机定制 1， goodsType : 1-普通商品 2-原定制商品 3-新定制商品
      this.$router.push({
        path: "/customize",
        query: {
          goodsId: item.goodsId, // 商品id
          itemId: item.id, // 货品id
          itemCode: item.itemCode, // 货品编码
        },
      });
    },
    custromUrl(code) {
      var timestamp = Date.parse(new Date()); // 获取时间戳
      let diyItemCode = code;
      let Source = 0; // 0：b2b diy定制 1：收单diy定制
      let Signature = "";
      let AppID = "5652338659";
      let AppKey = "14b94847d9744cf6988526fcac9b6107";
      this.showLoading = true;
      this.$api.get(this, "user/goodsdiy/getEditUrl").then((res) => {
        if (res.code === 0) {
          let code80 = AppID + diyItemCode + Source + res.token;
          let StringSha1 = AppID + AppKey + code80 + timestamp;
          this.showLoading = false;
          Signature = sha1(StringSha1);
          Signature = Signature.toLocaleUpperCase(); // 小写字母转大写字母
          window.location.href =
            res.editorUrl +
            "?Token=" +
            res.token +
            "&Timestamp=" +
            timestamp +
            "&Code80=" +
            diyItemCode +
            "&Signature=" +
            Signature +
            "&AppID=" +
            AppID +
            "&Source=" +
            Source; // 跳转链接
        }
      });
    },
    confirmpSesellShop() {
      // 购买预售商品
      let songGoodsShop = [];
      let term = [];
      Vue.set(this.presellItem, "count", this.presellNum);
      term.push(this.presellItem);
      window.localStorage.removeItem("shopCatShop");
      window.localStorage.removeItem("spesShopCatShop");
      window.localStorage.setItem(
        "songGoodsShop",
        JSON.stringify(songGoodsShop)
      ); // 赠品空数组
      window.localStorage.setItem("goodsItems", JSON.stringify(term));
      // 订单结算提示
      orderHint().then((res) => {
        if (res.code === 0) {
          this.$router.push({
            name: "ConsigneeInfor",
            query: { goodsType: 4, values: 0 },
          });
        }
      });
    },
    joinShopCarts() {
      // 预售商品加购
      var tempArray = [];
      var product = {
        ruleId: this.presellItem.ruleId,
        goodsId: Number(this.presellItem.goodsId),
        itemId: this.presellItem.id,
        itemType: 1,
        num: this.presellNum,
        orderType: 2,
      };
      tempArray.push(product);
      this.$api
        .post(this, "user/u/shoppingCarts", { shoppingCars: tempArray })
        .then((res) => {
          if (res.code === 0) {
            if (this.$i18n.locale === "zh") {
              this.$message.success("加入购物车成功");
            } else {
              this.$message.success("Add to shopping cart successfully.");
            }
            this.visibleDialog = false;
          } else if (res.code === 3) {
          }
        });
    },
    // 配送方式选择弹窗显示
    handleChooseDelivery(generalList, type, tempArray) {
      console.log("配送方式检查1", generalList);
      console.log("配送方式检查", tempArray);
      let userId = localStorage.getItem("userId");
      let params = {
        distributorId: userId,
        itemIdList: generalList,
      };
      // 获取商品库存
      listStockByCondition(params).then((stockRes) => {
        if (stockRes.success) {
          let stockResData = stockRes.data;
          if (stockResData.length == 0) {
            this.goConsigneeInfor(type, true, "notOnwayAll");
          } else {
            let curTemp = [];
            let len = 0;
            tempArray.forEach((item1) => {
              this.$set(item1, "inStockCount", item1.count);
              this.$set(item1, "inWayCount", 0);
              this.$set(item1, "orderCount", item1.count);
              stockResData.forEach((item2) => {
                if (item2.itemId === item1.id) {
                  if (item2.inStockUsableCount === 0) {
                    len++;
                  }
                  let inStockCount =
                    item2.inStockUsableCount > 0 ? item2.inStockUsableCount : 0;
                  // let inWayCount = item2.orderCount - inStockCount;
                  this.$set(item1, "inStockCount", inStockCount);
                  // this.$set(item1, 'inWayCount', inWayCount);
                  // this.$set(item1, 'orderCount', item2.orderCount);
                }
              });
              curTemp.push(item1);
            });
            if (tempArray.length > len) {
              this.deliveryData = curTemp;
              this.showChooseDelivery = true;
            } else {
              this.goConsigneeInfor(type, false); // 跳转下单页面
            }
          }
        } else {
          this.showLoading = false;
        }
      });
    },
    // 配送方式选择弹窗关闭--y
    handleCloseDelivery() {
      this.showChooseDelivery = false;
      this.showLoading = false;
    },
    // 跳转下单页面--Y
    goConsigneeInfor(songGoods, isInventoryEnough, isOnwayFlag) {
      orderHint().then((res) => {
        if (res) {
          if (songGoods === 1) {
            this.$router.push({
              name: "ConsigneeInfor",
              query: {
                values: 2,
                goodsType: 1,
                songGoods: songGoods,
                splitFlag: 0,
                isInventoryEnough: isInventoryEnough,
                isOnwayFlag: isOnwayFlag || "",
              },
            });
          } else {
            this.$router.push({
              name: "ConsigneeInfor",
              query: {
                values: 2,
                goodsType: 1,
                splitFlag: 0,
                isInventoryEnough: isInventoryEnough,
                isOnwayFlag: isOnwayFlag || "",
              },
            });
          }
        }
      });
    },
  },
  created: function () {
    let id = window.localStorage.getItem("userId");
    let gid = window.localStorage.getItem("gradeId");
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem("bLang")
      ? window.localStorage.getItem("bLang")
      : "zh-RMB";
    this.userId = id;
    this.gradeId = gid;
    if (this.$route.query.activityName !== undefined) {
      this.searchContent = this.$route.query.activityName;
    }
    this.getActivityData();
  },
};
</script>

<style lang='less'>
.alls {
  .el-dialog {
    width: 600px;
    .el-dialog__header {
      text-align: center;
    }
    .el-dialog__body {
      padding: 0;
      padding-bottom: 10px;
      overflow-y: auto;
      table {
        width: 100%;
      }
    }
    .el-dialog__footer {
      text-align: center;
    }
  }
}
</style>
<style scoped="scoped" lang='less'>
@import url("../assets/less/variable.less");
.index {
  width: 100%;
}
.content-wrap {
  position: relative;
  min-height: 500px;
  padding-top: 10px;
  padding-bottom: 80px;
  background-color: @grayBg;
  .banner {
    position: absolute;
    top: 0;
    right: 0;
    left: 0;
    height: 373px;
    background-color: rgba(241, 110, 123, 0.25);
    .inner {
      height: 470px;
      background: url("../assets/images/groupBuying/activity-banner.png")
        no-repeat top center;
    }
  }
  .shopcar-activity {
    position: relative;
    width: 1200px;
    margin: 0 auto;
    .shopcar-activity-img {
      margin-right: 10px;
      font-size: 22px;
      color: @orange;
    }
    .shopCart-activity-state {
      font-size: 12px;
      color: @lightBlack;
      line-height: 20px;
    }
  }

  .main {
    position: relative;
    padding-top: 5px;
    width: 1200px;
    .column-left {
      width: 318px;
      margin-right: 12px;
      padding-left: 15px;
      padding-right: 15px;
      box-sizing: border-box;
      border-radius: 8px;
      .logo-box {
        position: relative;
        .input-box {
          width: 288px;
          height: 33px;
          background-color: rgba(151, 151, 151, 0.11);
          border-radius: 33px;
          overflow: hidden;
          input {
            padding-left: 36px;
            width: 187px;
            height: 33px;
            line-height: 33px;
            font-size: 12px;
            color: @lightBlack;
            background: url(../../src/assets/images/search.png) no-repeat 14px
              center;
          }
        }
        .search {
          position: absolute;
          top: 0;
          right: 0;
          width: 57px;
          height: 33px;
          line-height: 33px;
          background-color: @blue;
          border-radius: 0 33px 33px 0;
        }
      }
      .activity-list {
        width: 100%;
        overflow-y: auto;
        .item {
          margin-top: 10px;
          padding: 10px 0;
          line-height: 18px;
          text-align: center;
          background-color: @lightGrayBg;
          border-radius: 8px;
          .activity-name {
            font-size: 12px;
            color: @lightBlack;
          }
          .activity-time {
            font-size: 12px;
            color: @lighterBlack;
          }
          &.active {
            background: linear-gradient(90deg, @groupBtnColor 0%, @red 100%);
            box-shadow: 0px 2px 10px 0px @groupBtnShadow;
            .activity-name,
            .activity-time {
              color: @white;
            }
          }
        }
      }
      .el-pagination {
        margin-top: 15px;
        text-align: center;
      }
    }
    .column-right {
      width: 870px;
      .right-cons {
        width: 100%;
        .rule-title {
          padding: 10px 40px 10px 0;
          line-height: 17px;
          border-radius: 8px;
          .rule-label {
            margin-right: 10px;
            width: 60px;
            font-size: 12px;
            color: @lightBlack;
            text-align: right;
            &.en {
              width: 90px;
            }
            span {
              display: block;
              width: 100%;
              text-align: right;
            }
          }
        }
        .rule-name {
          width: 100%;
          .label {
            font-size: 12px;
            color: @lightBlack;
          }
        }
        .rule-describe {
          padding-top: 5px;
          .item {
            font-size: 12px;
            color: @lightBlack;
            &.active {
              color: @red;
            }
          }
        }
        .user-table {
          padding: 10px 15px;
          border-radius: 8px;
          .group {
            display: flex;
            div {
              width: 120px;
              height: 30px;
              line-height: 30px;
              text-align: center;
              font-size: 12px;
            }
            div.active {
              color: @white;
              background-color: @blue;
            }
          }
          /deep/ .el-table {
            padding-top: 5px;
            &::before {
              background-color: @white;
            }
            th {
              height: 45px;
              line-height: 45px;
              font-size: 12px;
              color: @darkGray;
              font-weight: 400;
              background-color: @lightGrayBg;
              &.is-leaf {
                border-bottom: none;
              }
            }
            td {
              border-top: 1px dashed @bdColor;
            }
            .shop-img {
              margin: auto;
              margin-top: 5px;
              width: 50px;
              img {
                width: 100%;
                height: 50px;
              }
            }
            .red {
              color: @red;
            }
          }
        }
      }
      .total {
        height: 32px;
        line-height: 32px;
        color: @red;
      }
      .btn-wrap {
        padding: 20px 0 25px;
        text-align: center;
        span {
          display: inline-block;
          width: 130px;
          height: 40px;
          line-height: 40px;
          font-size: 16px;
          color: @white;
          border-radius: 4px;
          cursor: pointer;
          & + span {
            margin-left: 20px;
          }
        }
        .buy-btn {
          text-align: center;
          background-color: @redLabel;
        }
        .join-btn {
          text-align: center;
          background-color: @blueLabel;
        }
      }
    }
  }
}
.mini-search-btn {
  font-size: 12px;
  padding: 5px;
}
.zaitu {
  min-width: 90px;
  font-size: 12px;
  line-height: 20px;
  span {
    display: block;
    width: 16px;
    height: 20px;
    background: url("../assets/images/choose.png") no-repeat center center;
  }
  span.gou {
    background: url("../assets/images/gou.png") no-repeat center center;
  }
}
/*选择赠品*/
.max-height300 {
  max-height: 300px;
  overflow-y: auto;
}
.shop-table {
  margin-bottom: 20px;
  table {
    tr {
      td {
        height: 45px;
        text-align: center;
        .buy-sum {
          width: 92px;
          height: 22px;
          line-height: 22px;
          border: 1px solid #ebeff5;
          div {
            height: 22px;
            box-sizing: border-box;
            background-color: #fff;
            input {
              width: 62px;
            }
          }
          .buyac {
            width: 22px;
            font-size: 22px;
            color: #9b9b9b;
            cursor: pointer;
            text-align: center;
          }
          .buyb {
            width: 48px;
            line-height: 22px;
            color: #3a3a3a;
            border-left: 1px solid #ebeff5;
            border-right: 1px solid #ebeff5;
            input {
              width: 46px;
            }
          }
        }
        .songImg {
          width: 50px;
          img {
            margin-top: 5px;
            height: 50px;
          }
        }
      }
    }
  }
}
.empty-car {
  width: 860px;
  padding-bottom: 180px;
  .empty-car-img {
    margin-top: 120px;
    width: 153px;
  }
}

.gift-num {
  color: @red !important;
  .el-icon-arrow-right {
    margin-left: 13px;
  }
}
</style>
