<template>
  <div>
    <el-dialog
      class="activityAlls"
      title
      :visible.sync="dialogVisible"
      :closeOnClickModal="noClick"
      :lockScroll="noClick"
    >
      <div
        class="rl-tc rl-padding-top-default rl-padding-bottom-default rl-text-bold"
      >
        {{ $t("Promotion.SelectPromotion") }}
      </div>
      <div class="shopcar-activity">
        <span class="shopcar-activity-img">
          <i class="el-icon-warning"></i>
        </span>
        <span class="shopCart-activity-state">{{
          $t("Promotion.PromotionHint")
        }}</span>
      </div>
      <div class="column-right rl-bg-white">
        <div
          class="right-cons"
          v-for="item in dataList"
          :key="item.id"
          v-if="item.activityItem.length > 0"
        >
          <div class="rule-name rl-clear">
            <div class="rl-fl rl-text-xxs">
              <span v-if="item.activityType === 2">{{ item.policyName }}</span>
              <span v-else>{{ item.label }}</span>
              <span>{{ getStatus(item.isEnjoy) }}</span>
            </div>
            <div class="rl-fr rl-text-xxss">
              {{ item.startTime | formatDate }} --
              {{ item.endTime | formatDate }}
            </div>
          </div>
          <div
            class="rule-describe app-flex app-justify-content-space-between rl-padding-left-default rl-bg-gray-mm"
          >
            <div class="app-flex">
              <div class="name rl-margin-right-default">
                {{ $t("Promotion.RuleDetails") }}
              </div>
              <div class="item">
                <div class="item-context" v-if="item.activityType === 2">
                  {{ item.ruleDescription }}
                  <span v-if="item.meet === true" class="chenked"></span>
                </div>
                <div
                  class="item-context"
                  v-else
                  v-for="(value, index) in item.conditions"
                  :key="index"
                >
                  <span class="rl-margin-right-default"
                    >{{ $t("ShopCarts.Condition") }}{{ index + 1 }}:</span
                  >
                  <span>{{ value.label }}</span>
                  <span v-if="value.meet === true" class="chenked"></span>
                </div>
              </div>
            </div>
            <div
              v-if="item.onWayAttendEventFlag === 1"
              @click="changeTu(zaituType)"
              class="zaitu rl-clear cursor-pointer rl-fr"
            >
              <span
                class="rl-fl rl-text-xxs"
                :class="{ gou: zaituType === true }"
              ></span>
              {{ $t("P.ConTran") }}
            </div>
          </div>
          <div class="user-table">
            <el-table
              :data="item.activityItem"
              border
              max-height="300"
              header-row-class-name="header-row"
              class="activity-el-table rl-tc"
            >
              <el-table-column :label="$t('ShopCarts.Picture')" width="80">
                <template slot-scope="scope">
                  <div
                    class="shop-img cursor-pointer"
                    v-if="scope.row.itemImg !== null"
                  >
                    <img width="100%" :src="scope.row.itemImg" alt />
                  </div>
                  <div class="shop-img cursor-pointer" else>
                    <img width="100%" :src="item.goodsImg" alt />
                  </div>
                </template>
              </el-table-column>
              <el-table-column
                :label="$t('ShopCarts.ItemNo')"
                prop="itemCode"
                width="110"
              ></el-table-column>
              <el-table-column
                :label="$t('ShopCarts.ItemName')"
                :min-width="130"
              >
                <template slot-scope="scope">
                  <div class="rl-text-xxs">
                    <span
                      v-show="
                        $i18n.locale === 'zh' || !scope.row.itemNameEn == true
                      "
                      >{{ scope.row.itemName }}</span
                    >
                    <span v-show="$i18n.locale === 'en'">{{
                      scope.row.itemNameEn
                    }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('ShopCarts.Spe')" width="120">
                <template slot-scope="scope">
                  <div class="rl-text-xxs">
                    <span
                      v-show="
                        $i18n.locale === 'zh' ||
                        !scope.row.specificationValueNameEn == true
                      "
                      >{{ scope.row.specificationValueName }}</span
                    >
                    <span v-show="$i18n.locale === 'en'">{{
                      scope.row.specificationValueNameEn
                    }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('ShopCarts.Colors')" width="65">
                <template slot-scope="scope">
                  <div class="rl-text-xxs">
                    <span
                      v-show="
                        $i18n.locale === 'zh' ||
                        !scope.row.colorValueNameEn == true
                      "
                      >{{ scope.row.colorValueName }}</span
                    >
                    <span v-show="$i18n.locale === 'en'">{{
                      scope.row.colorValueNameEn
                    }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column
                :label="$t('ShopCarts.MemPrice')"
                prop="salePrice"
                width="110"
              >
                <template slot-scope="scope">
                  <div v-if="$i18n.locale === 'en' && $root.currency === 'USD'">
                    <i class="asmd">$</i>
                    {{ scope.row.salePriceEn | keepTwoNum }}
                  </div>
                  <div v-else>
                    <i class="asmd">￥</i>
                    {{ scope.row.salePrice | keepTwoNum }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('ShopCarts.Quantity')" width="155">
                <template slot-scope="scope">
                  <el-input-number
                    v-model="scope.row.currentNum"
                    @change="handleChange($event, scope.row, item)"
                    :min="0"
                    label="描述文字"
                    size="mini"
                  ></el-input-number>
                </template>
              </el-table-column>
              <el-table-column
                :label="$t('Promotion.SurplusQuantity')"
                prop="remainNum"
                width="100"
              ></el-table-column>
              <el-table-column
                :label="$t('Promotion.MinimumPurchase')"
                prop="moq"
                width="110"
                v-if="goodsType === 4"
              ></el-table-column>
              <el-table-column
                :label="$t('P.Inventory')"
                prop="numInWarehouse"
                width="110"
                v-else
              >
                <template slot-scope="scope">
                  <div v-if="scope.row.diyType === 1">
                    <el-button
                      class="mini-search-btn"
                      @click="goCustrom(scope.row.itemCode)"
                      >定制</el-button
                    >
                  </div>
                  <div class="rl-text-xxs" v-else>
                    <span v-if="zaituType === false">{{
                      scope.row.numInWarehouse
                    }}</span>
                    <span v-else>{{ scope.row.stockItemCount }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column
                :label="$t('Promotion.TotalQuantity')"
                prop="totalNum"
                width="120"
              ></el-table-column>
            </el-table>
          </div>
        </div>
        <div v-if="withoutActivityItem.length > 0">
          <div class="rl-padding-top-xxs rl-padding-bottom-xxs">
            {{ $t("Promotion.NoActivity") }}
          </div>
          <div class="user-table">
            <el-table
              :data="withoutActivityItem"
              border
              max-height="300"
              header-row-class-name="header-row"
              class="activity-el-table rl-tc"
            >
              <el-table-column :label="$t('ShopCarts.Picture')" width="80">
                <template slot-scope="scope">
                  <div
                    class="shop-img cursor-pointer"
                    v-if="scope.row.itemImg !== null"
                  >
                    <img width="100%" :src="scope.row.itemImg" alt />
                  </div>
                </template>
              </el-table-column>
              <el-table-column
                :label="$t('ShopCarts.ItemNo')"
                prop="itemCode"
                width="110"
              ></el-table-column>
              <el-table-column
                :label="$t('ShopCarts.ItemName')"
                :min-width="130"
              >
                <template slot-scope="scope">
                  <div class="rl-text-xxs">
                    <span
                      v-show="
                        $i18n.locale === 'zh' || !scope.row.itemNameEn == true
                      "
                      >{{ scope.row.itemName }}</span
                    >
                    <span v-show="$i18n.locale === 'en'">{{
                      scope.row.itemNameEn
                    }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('ShopCarts.Spe')" width="120">
                <template slot-scope="scope">
                  <div class="rl-text-xxs">
                    <span
                      v-show="
                        $i18n.locale === 'en' &&
                        scope.row.specificationValueNameEn !== undefined &&
                        scope.row.specificationValueNameEn !== null &&
                        scope.row.specificationValueNameEn !== ''
                      "
                      >{{ scope.row.specificationValueNameEn }}</span
                    >
                    <span v-show="$i18n.locale === 'zh'">{{
                      scope.row.specificationValueName
                    }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('ShopCarts.Colors')" width="65">
                <template slot-scope="scope">
                  <div class="rl-text-xxs">
                    <span
                      v-show="
                        $i18n.locale === 'zh' ||
                        !scope.row.colorValueNameEn == true
                      "
                      >{{ scope.row.colorValueName }}</span
                    >
                    <span v-show="$i18n.locale === 'en'">{{
                      scope.row.colorValueNameEn
                    }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column
                :label="$t('ShopCarts.MemPrice')"
                prop="salePrice"
                width="110"
              >
                <template slot-scope="scope">
                  <div v-if="$i18n.locale === 'en' && $root.currency === 'USD'">
                    <i class="asmd">$</i>
                    {{ scope.row.salePriceEn | keepTwoNum }}
                  </div>
                  <div v-else>
                    <i class="asmd">￥</i>
                    {{ scope.row.salePrice | keepTwoNum }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column
                :label="$t('Promotion.AllQuantity')"
                width="155"
                :render-header="renderheader"
              >
                <template slot-scope="scope">
                  <div
                    class="app-flex app-justify-content-space-between rl-padding-left-default rl-padding-right-default"
                  >
                    <div class="rl-text-xxs">
                      {{ scope.row.lookNumInWarehouse }}
                    </div>
                    <div class="rl-text-xxs">/</div>
                    <div class="rl-text-xxs">
                      {{ scope.row.lookStockItemCount }}
                    </div>
                  </div>
                  <el-input-number
                    v-model="scope.row.remainNum"
                    :min="0"
                    label="描述文字"
                    size="mini"
                    disabled
                  ></el-input-number>
                </template>
              </el-table-column>
              <el-table-column
                :label="$t('Promotion.MinimumPurchase')"
                prop="moq"
                width="100"
                v-if="goodsType === 4"
              ></el-table-column>
              <el-table-column
                :label="$t('P.Inventory')"
                prop="numInWarehouse"
                width="110"
                v-else
              ></el-table-column>
              <el-table-column
                :label="$t('Promotion.TotalQuantity')"
                prop="totalNum"
                width="120"
              ></el-table-column>
            </el-table>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button
          class="mini-search-btn"
          @click.native.prevent="confirmGoOrder(1)"
          type="primary"
          >{{ $t("P.ConfirmOrder") }}</el-button
        >
        <!-- <el-button class="mini-price-btn" v-if="promotionsDifferential !== undefined && promotionsDifferential != null" @click.native.prevent="confirmGoOrder(2)" type="primary">{{$t('P.ConfirmPriceOrder')}}</el-button> -->
        <el-button class="mini-cancel-btn" @click="cancelGoOrder">{{
          $t("Message.Cancel")
        }}</el-button>
      </div>
    </el-dialog>
    <!--    选择赠品-->
    <el-dialog
      class="alls"
      :title="$t('P.ChooseGift')"
      :visible.sync="presentDialogVisible"
      :before-close="cancelSongShop"
    >
      <div
        class="rl-padding-left-default rl-padding-bottom-xxxs rl-padding-top-xxxs rl-bd-gray-sm"
      >
        {{ $t("P.Receive") }} {{ presentCount }} {{ $t("P.MostPieces") }}，{{
          $t("P.HaveSelected")
        }}
        {{ hasChoose }} {{ $t("P.MostPieces") }}
      </div>
      <div
        class="shop-table max-height300 overflow-y rl-padding-left-default rl-padding-right-default"
      >
        <div
          class="rl-tc rl-padding-top-default rl-text-gray"
          v-if="presentGoodsList.length === 0"
        >
          {{ $t("P.NoData") }}
        </div>
        <table>
          <tbody>
            <tr v-for="(item, index) in presentGoodsList" :key="index">
              <td width="120px" class="rl-text-xxs">{{ item.itemCode }}</td>
              <td width="120px" class="rl-tc">
                <div class="songImg rl-margin-zero">
                  <img width="100%" :src="item.imageUrl" alt />
                </div>
              </td>
              <td width="180px" class="rl-text-xxs">
                {{
                  $i18n.locale === "en" &&
                  !item.itemNameEn !== undefined &&
                  item.itemNameEn !== null &&
                  item.itemNameEn !== ""
                    ? item.itemNameEn
                    : item.itemName
                }}
              </td>
              <td width="150px">
                <songSum
                  ref="songSum"
                  :songShop="item"
                  :maxSongSum="presentCount"
                  :hasChoose="hasChoose"
                ></songSum>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button class="mini-search-btn" @click="confirmSongShop">{{
          $t("P.OK")
        }}</el-button>
        <el-button class="mini-cancel-btn" @click="cancelSongShop">{{
          $t("P.Cancel")
        }}</el-button>
      </div>
    </el-dialog>

    <!-- 配送方式选择 -->
    <chooseDelivery
      v-show="showChooseDelivery"
      :deliveryData="deliveryData"
      :values="3"
      @handleClose="handleCloseDelivery"
    ></chooseDelivery>
  </div>
</template>

<script>
import Vue from "vue";
import { formatDate } from "@/assets/js/common.js";
import songSum from "@/components/songSum.vue";
import { MessageBox } from "element-ui";
import GD from "@/assets/js/globalData";
import chooseDelivery from "@/components/dialog/chooseDelivery.vue";
export default {
  name: "buyNow",
  components: { songSum, chooseDelivery },
  props: {
    // promotionsDifferential:{
    //   type:Object
    // },
    activityPromotions: {
      type: Array,
    },
    withoutActivityItem: {
      type: Array,
    },
    activityDialogVisible: {
      type: Boolean,
    },
    goodsType: {
      type: Number,
    },
    onWayAttendEventFlag: {
      type: Number,
    },
  },
  data() {
    return {
      zaituType: false,
      presentDialogVisible: false,
      noClick: false,
      presentCount: 0,
      presentGoodsList: [],
      dataList: [],
      activityItems: [],
      langList: GD.langList, // 语种列表
      dialogVisible: true,
      showChooseDelivery: false, // 配送方式选择弹窗
      deliveryData: [], // 配送方式选择弹窗列表数据
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
  methods: {
    renderheader(h, { column, $index }) {
      // 表头换行
      return h("span", {}, [
        h("span", {}, column.label.split("/")[0]),
        h("br"),
        h("span", {}, column.label.split("/")[1]),
      ]);
    },
    getStatus(row) {
      // 订单是否同享状态
      switch (row) {
        case 0:
          return "(与订单活动不同享)";
        case 1:
          return "(与订单活动同享)";
      }
    },
    changeTu(type) {
      // 在途在库切换
      this.$forceUpdate(); // 更新页面数据
      if (type === true) {
        this.zaituType = false;
      } else {
        this.zaituType = true;
      }
    },
    handleChange(val, item, items) {
      // 改变货品数量
      let chooseItemSum = 0; // 选择货品总数
      let stock = 0;
      let surplus = 0; // 剩余数量
      let oldRemainNum = 0; // 旧的剩余数量
      if (this.goodsType === 4) {
        // 预售商品
        if (val < item.moq) {
          item.currentNum = item.moq;
        } else {
          item.currentNum = val;
        }
        this.withoutActivityItem[0].remainNum = 0; // 未参与活动的商品
      } else {
        if (val === undefined) {
          this.$nextTick(() => {
            item.currentNum = 0;
            for (let i = 0; i < this.dataList.length; i++) {
              for (let j = 0; j < this.dataList[i].activityItem.length; j++) {
                if (item.id === this.dataList[i].activityItem[j].id) {
                  chooseItemSum += this.dataList[i].activityItem[j].currentNum;
                  this.dataList[i].activityItem[j].remainNum = surplus;
                  if (this.zaituType) {
                    stock = this.dataList[i].activityItem[j].stockItemCount;
                  } else {
                    stock = this.dataList[i].activityItem[j].numInWarehouse;
                  }
                }
              }
            }
          });
        } else {
          for (let i = 0; i < this.dataList.length; i++) {
            for (let j = 0; j < this.dataList[i].activityItem.length; j++) {
              if (item.id === this.dataList[i].activityItem[j].id) {
                chooseItemSum += this.dataList[i].activityItem[j].currentNum;
                this.dataList[i].activityItem[j].remainNum = surplus;
                if (this.zaituType) {
                  stock = this.dataList[i].activityItem[j].stockItemCount;
                } else {
                  stock = this.dataList[i].activityItem[j].numInWarehouse;
                }
              }
            }
          }
        }
        if (chooseItemSum > stock) {
          this.$message.warning(this.$t("P.GoodsItemQuantityError"));
          this.$nextTick(() => {
            chooseItemSum = chooseItemSum - item.currentNum;
            if (item.oldNum !== undefined) {
              item.currentNum = item.oldNum;
            } else {
              item.currentNum = 0;
            }
            for (let i = 0; i < this.dataList.length; i++) {
              for (let j = 0; j < this.dataList[i].activityItem.length; j++) {
                if (item.id === this.dataList[i].activityItem[j].id) {
                  if (
                    this.dataList[i].activityItem[j].oldRemainNum !== undefined
                  ) {
                    this.dataList[i].activityItem[j].remainNum = this.dataList[
                      i
                    ].activityItem[j].oldRemainNum;
                  }
                }
              }
            }
            // for (let i = 0; i < this.dataList.length; i++) {
            //   for (let j = 0; j < this.dataList[i].activityItem.length; j++) {
            //     if (item.id === this.dataList[i].activityItem[j].id) {
            //       this.dataList[i].activityItem[j].remainNum = surplus
            //       if (this.dataList[i].activityItem[j].remainNum > this.dataList[i].activityItem[j].numInWarehouse) { // 当购买数量大于在库数量
            //         this.dataList[i].activityItem[j].lookNumInWarehouse = this.dataList[i].activityItem[j].numInWarehouse
            //         this.dataList[i].activityItem[j].lookStockItemCount = this.dataList[i].activityItem[j].remainNum - this.dataList[i].activityItem[j].numInWarehouse
            //       } else {
            //         this.dataList[i].activityItem[j].lookNumInWarehouse = this.dataList[i].activityItem[j].remainNum
            //         this.dataList[i].activityItem[j].lookStockItemCount = 0
            //       }
            //     }
            //   }
            // }
            // for (let n = 0; n < this.withoutActivityItem.length; n++) { // 未参与活动商品的货品数量
            //   if (this.withoutActivityItem[n].id === item.id) {
            //     this.withoutActivityItem[n].remainNum = item.remainNum
            //     this.withoutActivityItem[n].totalNum = chooseItemSum + this.withoutActivityItem[n].remainNum
            //     if (this.withoutActivityItem[n].lookNumInWarehouse > 0) {
            //       this.withoutActivityItem[n].lookNumInWarehouse = this.withoutActivityItem[n].remainNum - this.withoutActivityItem[n].lookStockItemCount
            //     } else if (this.withoutActivityItem[n].remainNum <= this.withoutActivityItem[n].numInWarehouse) {
            //       this.withoutActivityItem[n].lookNumInWarehouse = this.withoutActivityItem[n].numInWarehouse - this.withoutActivityItem[n].lookStockItemCount
            //       this.withoutActivityItem[n].lookStockItemCount = this.withoutActivityItem[n].remainNum - this.withoutActivityItem[n].lookNumInWarehouse
            //     } else {
            //       // this.withoutActivityItem[n].lookStockItemCount = this.withoutActivityItem[n].remainNum
            //       this.withoutActivityItem[n].lookNumInWarehouse = this.withoutActivityItem[n].remainNum - this.withoutActivityItem[n].lookStockItemCount
            //     }
            //   }
            // }
            // this.caculateData(items)
          });
        } else {
          if (item.count - chooseItemSum >= 0) {
            surplus = item.count - chooseItemSum;
          }
          for (let i = 0; i < this.dataList.length; i++) {
            for (let j = 0; j < this.dataList[i].activityItem.length; j++) {
              if (item.id === this.dataList[i].activityItem[j].id) {
                this.dataList[i].activityItem[j].remainNum = surplus;
                this.dataList[i].activityItem[j].totalNum =
                  chooseItemSum + this.dataList[i].activityItem[j].remainNum;
                if (
                  this.dataList[i].activityItem[j].remainNum >
                  this.dataList[i].activityItem[j].numInWarehouse
                ) {
                  // 当购买数量大于在库数量
                  this.dataList[i].activityItem[
                    j
                  ].lookNumInWarehouse = this.dataList[i].activityItem[
                    j
                  ].numInWarehouse;
                  this.dataList[i].activityItem[j].lookStockItemCount =
                    this.dataList[i].activityItem[j].remainNum -
                    this.dataList[i].activityItem[j].numInWarehouse;
                } else {
                  this.dataList[i].activityItem[
                    j
                  ].lookNumInWarehouse = this.dataList[i].activityItem[
                    j
                  ].remainNum;
                  this.dataList[i].activityItem[j].lookStockItemCount = 0;
                }
              }
            }
          }
          for (let n = 0; n < this.withoutActivityItem.length; n++) {
            // 未参与活动商品的货品数量
            if (this.withoutActivityItem[n].id === item.id) {
              this.withoutActivityItem[n].remainNum = item.remainNum;
              this.withoutActivityItem[n].totalNum =
                chooseItemSum + this.withoutActivityItem[n].remainNum;
              if (this.withoutActivityItem[n].lookNumInWarehouse >= 0) {
                this.withoutActivityItem[n].lookNumInWarehouse =
                  this.withoutActivityItem[n].remainNum -
                  this.withoutActivityItem[n].lookStockItemCount;
              } else {
                this.withoutActivityItem[
                  n
                ].lookStockItemCount = this.withoutActivityItem[n].remainNum;
              }
              // if (this.withoutActivityItem[n].numInWarehouse >= this.withoutActivityItem[n].remainNum) { // 在库数量大于等于购买数量
              //   this.withoutActivityItem[n].lookNumInWarehouse = this.withoutActivityItem[n].remainNum
              //   this.withoutActivityItem[n].lookStockItemCount = 0
              // } else if (this.withoutActivityItem[n].numInWarehouse <= 0) { // 在库数量小于等于0
              //   this.withoutActivityItem[n].lookNumInWarehouse = 0
              //   this.withoutActivityItem[n].lookStockItemCount = this.withoutActivityItem[n].remainNum
              // } else {
              //   this.withoutActivityItem[n].lookNumInWarehouse = this.withoutActivityItem[n].numInWarehouse
              //   this.withoutActivityItem[n].lookStockItemCount = this.withoutActivityItem[n].remainNum - this.withoutActivityItem[n].numInWarehouse
              // }
            }
          }
          item.oldNum = item.currentNum; // 记录当前值值
          for (let i = 0; i < this.dataList.length; i++) {
            for (let j = 0; j < this.dataList[i].activityItem.length; j++) {
              // 记录剩余数量
              if (item.id === this.dataList[i].activityItem[j].id) {
                this.dataList[i].activityItem[j].oldRemainNum = item.remainNum;
              }
            }
          }
          this.caculateData(items);
        }
      }
    },
    caculateData(items) {
      let totalPrice = 0;
      let totalCounts = 0;
      let maxNum = 0;
      let maxPrice = 0;
      this.$forceUpdate();
      if (items.activityType === 2) {
        // 等级折扣
        items.activityItem.forEach((obj) => {
          // 总价计算
          totalPrice += obj.currentNum * obj.salePrice;
          totalCounts += obj.currentNum;
        });
        items.meet = false;
        if (items.discountBeforeAfter === 1) {
          // 折扣前
          if (
            (items.moneyOrCount === 1 && totalPrice > items.oneBuyMoney) ||
            (items.moneyOrCount === 2 && totalCounts > items.oneBuyCount)
          ) {
            items.meet = true;
          }
        } else {
          if (
            (items.moneyOrCount === 1 && totalPrice > items.oneBuyMoney) ||
            (items.moneyOrCount === 2 && totalCounts > items.oneBuyCount)
          ) {
            items.meet = true;
          }
        }
      } else {
        if (items.isAddUp === 0) {
          // 活动商品是否累计，1是 0否
          maxNum = items.activityItem[0].currentNum;
          maxPrice =
            items.activityItem[0].currentNum * items.activityItem[0].salePrice;
          for (var j = 1; j < items.activityItem.length; j++) {
            if (items.activityItem[j].currentNum > maxNum) {
              maxNum = items.activityItem[j].currentNum;
              maxPrice =
                items.activityItem[j].currentNum *
                items.activityItem[j].salePrice;
            }
          }
          totalCounts = maxNum;
          totalPrice = maxPrice;
        } else {
          items.activityItem.forEach((obj) => {
            // 总价计算
            totalPrice += obj.currentNum * obj.salePrice;
            totalCounts += obj.currentNum;
          });
        }
        items.totalMoney = totalPrice;
        items.chooseCount = totalCounts;
        let ruleCondition = null; // 判断满足条件选择最优
        items.conditions.forEach((obj) => {
          obj.meet = false; // 先把满足条件状态设置为false
          if (obj.groupConditions.length > 0) {
            // 有组合活动
            obj.meet = true;
            obj.groupConditions.forEach((val) => {
              let temp = [];
              Vue.set(val, "meetType", false); // 组合活动规则条件满足标志
              Vue.set(val, "groupTotalCount", 0); // 组合活动规则条件货品购买总数
              Vue.set(val, "groupTotalMoney", 0); // 组合活动规则条件货品购买金额
              temp = val.goodsItemIds.split(",");
              for (let i = 0; i < temp.length; i++) {
                for (let j = 0; j < items.activityItem.length; j++) {
                  if (
                    Number(temp[i]) === Number(items.activityItem[j].itemId)
                  ) {
                    val.groupTotalCount += items.activityItem[j].num;
                    val.groupTotalMoney +=
                      items.activityItem[j].num *
                      items.activityItem[j].salePrice;
                  }
                }
              }
              if (items.moneyOrCount === 1) {
                // 一次性购买按金额还是数量 1金额 2数量
                if (val.groupTotalMoney >= obj.oneBuyMoney) {
                  val.meetType = true;
                }
              } else if (items.moneyOrCount === 2) {
                if (val.groupTotalCount >= val.oneBuyCount) {
                  val.meetType = true;
                }
              }
            });
            for (let n = 0; n < obj.groupConditions.length; n++) {
              if (obj.groupConditions[n].meetType === false) {
                obj.meet = false;
                break;
              }
            }
          } else {
            if (
              (items.moneyOrCount === 1 &&
                items.totalMoney >= obj.oneBuyMoney) ||
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
      }
    },
    cancelGoOrder() {
      this.$emit("shutDialog", true);
      this.resetDataList();
    },
    resetDataList() {
      this.dataList.forEach((item) => {
        item.activityItem.forEach((obj) => {
          // 重置
          obj.currentNum = 0;
        });
      });
    },
    confirmGoOrder(type) {
      // 确认下单
      let itemTemp = []; // 选择参与活动的货品
      let songGoodsShop = [];
      let hasPresentActivity = false; // 是否有满赠
      for (let i = 0; i < this.dataList.length; i++) {
        if (this.dataList[i].conditions) {
          for (let n = 0; n < this.dataList[i].conditions.length; n++) {
            if (this.dataList[i].conditions[n].reduceOrPresent === 2) {
              // 满赠
              hasPresentActivity = true;
              break;
            }
          }
        }
        if (this.dataList[i].policyId) {
          // 等级折扣
          for (let j = 0; j < this.dataList[i].activityItem.length; j++) {
            if (this.dataList[i].activityItem[j].currentNum > 0) {
              Vue.set(
                this.dataList[i].activityItem[j],
                "gradeDiscountId",
                this.dataList[i].id
              );
              Vue.set(this.dataList[i].activityItem[j], "ruleId", ""); // ruleId 为空
              itemTemp.push(this.dataList[i].activityItem[j]);
            }
          }
        } else {
          for (let j = 0; j < this.dataList[i].activityItem.length; j++) {
            if (this.dataList[i].activityItem[j].currentNum > 0) {
              Vue.set(
                this.dataList[i].activityItem[j],
                "ruleId",
                this.dataList[i].id
              ); // 每个货品参与活动的id
              itemTemp.push(this.dataList[i].activityItem[j]);
            }
          }
        }
      }
      for (var n = 0; n < this.withoutActivityItem.length; n++) {
        // 未参与活动的商品
        if (Number(this.withoutActivityItem[n].remainNum) > 0) {
          this.withoutActivityItem[n].ruleId = "";
          this.withoutActivityItem[n].currentNum = Number(
            this.withoutActivityItem[n].remainNum
          );
          itemTemp.push(this.withoutActivityItem[n]);
        }
      }
      this.activityItems = itemTemp;
      if (hasPresentActivity === true) {
        this.lookSongGoods(this.activityItems);
        return false;
      }
      window.localStorage.removeItem("shopCatShop");
      window.localStorage.removeItem("spesShopCatShop");
      window.localStorage.removeItem("presellShop");
      window.localStorage.setItem(
        "songGoodsShop",
        JSON.stringify(songGoodsShop)
      ); // 赠品空数组
      window.localStorage.setItem(
        "activityItems",
        JSON.stringify(this.activityItems)
      );
      if (this.goodsType === 4) {
        this.$router.push({
          name: "ConsigneeInfor",
          query: { values: 3, goodsType: this.goodsType },
        });
      } else {
        var tempArrayOne = this.activityItems.concat(songGoodsShop);
        var generalList = [];
        tempArrayOne.forEach((item) => {
          var count = item.currentNum ? item.currentNum : item.num;
          var option = {
            itemId: item.id || item.itemId,
            count: count,
          };
          this.$set(item, "count", count);
          generalList.push(option);
        });
        this.handleChooseDelivery(generalList, [], [], 0, tempArrayOne);
      }
      // this.resetDataList()
    },
    lookSongGoods(itemTemp) {
      let tempArray = [];
      let songGoodsShop = [];
      this.presentGoodsList = [];
      this.presentCount = 0;
      itemTemp.forEach((item) => {
        if (item.currentNum && item.currentNum > 0) {
          var product = {
            ruleId: item.ruleId,
            gradeDiscountId: item.gradeDiscountId,
            itemCount: item.currentNum,
            goodsId: item.goodsId,
            itemId: item.id,
          };
          tempArray.push(product);
        }
      });
      this.$api
        .post(this, "user/u/marketing/promotion/calculatePresent", {
          goodss: tempArray,
        })
        .then((res) => {
          if (res.code === 0) {
            if (res.rules.length > 0) {
              this.presentDialogVisible = true;
              this.dialogVisible = false;
              res.rules.forEach((item) => {
                let ablePresentCount = 0;
                item.condition.presents.forEach((val) => {
                  Vue.set(val, "num", 0);
                  Vue.set(val, "ruleId", item.id);
                  this.presentGoodsList.push(val);
                  ablePresentCount = ablePresentCount + val.presentCount;
                });
                // debugger
                if (item.condition.presentCountTotal > ablePresentCount) {
                  this.presentCount += ablePresentCount;
                } else {
                  this.presentCount += item.condition.presentCountTotal;
                }
              });
            } else {
              window.localStorage.removeItem("shopCatShop");
              window.localStorage.removeItem("spesShopCatShop");
              window.localStorage.removeItem("presellShop");
              window.localStorage.setItem(
                "songGoodsShop",
                JSON.stringify(songGoodsShop)
              ); // 赠品空数组
              window.localStorage.setItem(
                "activityItems",
                JSON.stringify(this.activityItems)
              );
              if (this.goodsType === 4) {
                this.$router.push({
                  name: "ConsigneeInfor",
                  query: { values: 3, goodsType: this.goodsType },
                });
              } else {
                var tempArrayOne = this.activityItems.concat(songGoodsShop);
                var generalList = [];
                tempArrayOne.forEach((item) => {
                  var count = item.currentNum ? item.currentNum : item.num;
                  var option = {
                    itemId: item.id || item.itemId,
                    count: count,
                  };
                  this.$set(item, "count", count);
                  generalList.push(option);
                });
                this.handleChooseDelivery(generalList, [], [], 0, tempArrayOne);
              }
            }
            // this.resetDataList()
          }
        });
    },
    cancelSongShop() {
      this.presentDialogVisible = false;
      this.$emit("shutDialog", true);
      this.resetDataList();
    },
    confirmSongShop() {
      var tempArray = [];
      if (this.presentCount < this.hasChoose) {
        if (this.$i18n.locale === "zh") {
          this.$message.warning("赠品数量已达上限");
        } else {
          this.$message.warning(
            "The quantity of gifts has reached upper limit."
          );
        }
        return false;
      }
      if (this.hasChoose === 0) {
        let info = "";
        let infoTwo = "";
        if (this.$i18n.locale === "zh") {
          info = "您还未选择赠品，可以取消重新选择赠品";
          infoTwo = "确定不需要赠品";
        } else {
          info =
            "You have not chosen the gift yet. You can cancel and re-choose the gift";
          infoTwo = "make sure you do not need the gift.";
        }
        MessageBox.confirm(info, infoTwo, {
          confirmButtonText: this.$t("P.OK"),
          cancelButtonText: this.$t("P.Cancel"),
          type: "warning",
        }).then(() => {
          this.presentDialogVisible = false;
          // this.$emit("shutDialog", true);
          window.localStorage.removeItem("shopCatShop");
          window.localStorage.removeItem("spesShopCatShop");
          window.localStorage.removeItem("presellShop");
          window.localStorage.setItem(
            "songGoodsShop",
            JSON.stringify(tempArray)
          ); // 赠品空数组
          window.localStorage.setItem(
            "activityItems",
            JSON.stringify(this.activityItems)
          );
          if (this.goodsType === 4) {
            this.$router.push({
              name: "ConsigneeInfor",
              query: { values: 3, goodsType: this.goodsType },
            });
          } else {
            var tempArrayOne = this.activityItems.concat(tempArray);
            var generalList = [];
            tempArrayOne.forEach((item) => {
              var count = item.currentNum ? item.currentNum : item.num;
              var option = {
                itemId: item.id || item.itemId,
                count: count,
              };
              this.$set(item, "count", count);
              generalList.push(option);
            });
            this.handleChooseDelivery(generalList, [], [], 0, tempArrayOne);
          }
        });
        return false;
      }
      if (this.hasChoose > 0) {
        this.presentDialogVisible = false;
        // this.$emit("shutDialog", true);
        this.presentGoodsList.forEach((item) => {
          this.$set(item, "itemImg", item.imageUrl);
          this.$set(item, "currentNum", item.num);
          if (item.num > 0) {
            tempArray.push(item);
          }
        });
        window.localStorage.removeItem("shopCatShop");
        window.localStorage.removeItem("spesShopCatShop");
        window.localStorage.removeItem("presellShop");
        window.localStorage.setItem("songGoodsShop", JSON.stringify(tempArray)); // 赠品
        window.localStorage.setItem(
          "activityItems",
          JSON.stringify(this.activityItems)
        );
        var tempArrayOne = this.activityItems.concat(tempArray);
        var generalList = [];
        tempArrayOne.forEach((item) => {
          var count = item.currentNum ? item.currentNum : item.num;
          var option = {
            itemId: item.id || item.itemId,
            count: count,
          };
          this.$set(item, "count", count);
          generalList.push(option);
        });
        this.handleChooseDelivery(generalList, [], [], 1, tempArrayOne);
      }
    },
    // 配送方式选择弹窗显示
    handleChooseDelivery(generalList, diyList, groupList, type, tempArray) {
      // 检验库存
      this.$api
        .post(this, "user/u/checkSku", {
          generalList: generalList,
          diyList: diyList,
          groupList: groupList,
        })
        .then((res) => {
          if (res.code === 0) {
            if (res.data.length === 0) {
              this.goConsigneeInfor(type, true, "notOnwayAll");
            } else {
              // 拼团商品 - 不拆单
              if (groupList.length > 0) {
                let len = 0;
                groupList.forEach((item1) => {
                  res.data.forEach((item2) => {
                    if (item2.itemId === item1.itemId) {
                      if (Number(item2.inStockCount) === 0) {
                        len++;
                      }
                    }
                  });
                });
                if (len > 0 && (groupList.length != len)) {
                  this.goConsigneeInfor(type, false, "notOnwayAll");
                } else {
                  this.goConsigneeInfor(type, false);
                }
              } else {
                // 其他商品
                let curTemp = [];
                let len = 0;
                tempArray.forEach((item1) => {
                  this.$set(item1, "inStockCount", item1.count);
                  this.$set(item1, "inWayCount", 0);
                  this.$set(item1, "orderCount", item1.count);
                  res.data.forEach((item2) => {
                    if (item2.itemId === item1.id) {
                      if (item2.inStockCount === 0) {
                        len++;
                      }
                      let inStockCount =
                        item2.inStockCount > 0 ? item2.inStockCount : 0;
                      let inWayCount = item2.orderCount - inStockCount;
                      this.$set(item1, "inStockCount", inStockCount);
                      this.$set(item1, "inWayCount", inWayCount);
                      this.$set(item1, "orderCount", item2.orderCount);
                    }
                  });
                  curTemp.push(item1);
                });
                if (tempArray.length > len) {
                  this.deliveryData = curTemp;
                  this.showChooseDelivery = true;
                } else {
                  this.goConsigneeInfor(type, false);
                }
              }
            }
          } else {
            this.showLoading = false;
          }
        });
    },
    // 配送方式选择弹窗关闭
    handleCloseDelivery() {
      this.showChooseDelivery = false;
      this.showLoading = false;
    },
    // 跳转下单页面
    goConsigneeInfor(songGoods, isInventoryEnough, isOnwayFlag) {
      if (songGoods === 1) {
        this.$router.push({
          name: "ConsigneeInfor",
          query: {
            values: 3,
            goodsType: 1,
            songGoods: songGoods,
            splitFlag: 0,
            isInventoryEnough: isInventoryEnough,
            isOnwayFlag: isOnwayFlag ? isOnwayFlag : "",
          },
        });
      } else {
        this.$router.push({
          name: "ConsigneeInfor",
          query: {
            values: 3,
            goodsType: 1,
            splitFlag: 0,
            isInventoryEnough: isInventoryEnough,
            isOnwayFlag: isOnwayFlag ? isOnwayFlag : "",
          },
        });
      }
    },
  },
  created: function () {
    this.activityPromotions.forEach((item) => {
      // 转化数组，取消指针（当同个货品在不同数组）
      Vue.set(item, "totalMoney", 0);
      Vue.set(item, "chooseCount", 0);
      if (item.activityType === 2) {
      } else {
        item.conditions.forEach((obj) => {
          Vue.set(obj, "meet", false); // 是否满足条件
        });
      }
      const temp = JSON.stringify(item);
      this.dataList.push(JSON.parse(temp));
    });
  },
  watch: {
    activityDialogVisible(val) {
      if (val) {
        this.dialogVisible = true;
        this.presentDialogVisible = false;
      }
    },
  },
};
</script>
<style lang='less'>
.activityAlls {
  overflow: visible;
  top: -12%;
  .el-dialog {
    width: 1200px !important;
    max-height: 80%;
    overflow-y: scroll;
    .el-dialog__header {
      display: none;
    }
    .el-dialog__body {
      padding: 0;
      padding-left: 20px;
      padding-right: 20px;
      padding-bottom: 10px;
    }
    .el-dialog__footer {
      text-align: center;
    }
  }
}
</style>
<style scoped lang='less'>
@import url("../assets/less/variable.less");
/*活动*/
.shopcar-activity {
  padding-left: 20px;
  background-color: #e5f9fb;
  line-height: 40px;
  height: 40px;
  .shopcar-activity-img {
    margin-right: 5px;
    font-size: 22px;
    color: @orange;
    vertical-align: middle;
  }
  .shopCart-activity-state {
    vertical-align: middle;
    display: inline-block;
    font-size: 12px;
  }
}
.right-cons {
  .rule-name {
    width: 100%;
    font-size: 12px;
    padding-top: 10px;
    padding-bottom: 10px;
  }
  .rule-describe {
    padding-top: 10px;
    padding-bottom: 10px;
    height: 100%;
    font-size: 12px;
    font-family: FZHei-B01;
    font-weight: 400;
    color: rgba(102, 102, 102, 1);
    .name {
      display: flex;
      align-items: center;
      justify-content: center;
      min-width: 58px;
      font-size: 12px;
      font-family: FZHei-B01;
      font-weight: 400;
      color: rgba(102, 102, 102, 1);
    }

    .item {
      font-size: 12px;
      font-family: FZHei-B01;
      font-weight: 400;
      color: rgba(102, 102, 102, 1);
      .chenked {
        width: 19px;
        height: 19px;
        display: inline-block;
        vertical-align: -5px;
        -webkit-appearance: none;
        appearance: none;
        outline: none;
        margin-left: 15px;
        font-size: 12px;
        font-family: FZHei-B01;
        font-weight: 400;
        color: rgba(102, 102, 102, 1);
        background: url(../../src/assets/images/selected.png) no-repeat center
          center;
      }
      .item-context {
        font-size: 12px;
        font-family: FZHei-B01;
        font-weight: 400;
        color: rgba(102, 102, 102, 1);
      }
    }
  }
  .user-table {
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
        background-color: #00c9dc;
        color: #fff;
      }
    }

    table {
      width: 840px;
      word-wrap: break-word;
      word-break: break-all;
      border-collapse: collapse;

      tr {
        th {
          height: 45px;
          line-height: 45px;
        }

        td {
          height: 80px;
          line-height: 80px;
          font-size: 12px;
          text-align: center;

          .delet {
            display: inline-block;
            width: 68px;
            height: 35px;
            line-height: 35px;
            border-radius: 5px;

            .el-button--text {
              color: #fff;
              width: 68px;
              height: 35px;
            }
          }
        }
      }
    }
  }

  .total {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
}
.shop-img {
  margin: auto;
  margin-top: 5px;
  width: 55px;
  img {
    width: 100%;
    height: 55px;
  }
}
/*选择赠品*/
.max-height300 {
  max-height: 300px;
}
.shop-table {
  margin-bottom: 20px;
  table {
    tr {
      td {
        height: 45px;
        text-align: center;
        border-bottom: 1px solid #ccc;
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
.zaitu {
  font-size: 12px;
  font-family: FZHei-B01;
  font-weight: 400;
  color: rgba(102, 102, 102, 1);
  padding-right: 20px;
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
</style>
