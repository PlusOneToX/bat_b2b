<template>
  <div>
    <div class="user rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState"></Header>
      <!--主内容-->
      <div class="user-main rl-clear rl-margin-zero">
        <!--公共左边-->
        <Left></Left>
        <div class="user-right rl-fr rl-bg-white rl-margin-top-default rl-padding-bottom-double">
          <div class="content">
            <h6 class="user-right-title">{{$t('UserCenter.Quotation')}}</h6>
            <div class="query rl-padding-top-default">
              <el-button
                class="mini-search-btn rl-fr"
                type="info"
                @click="search"
                size="mini"
              >{{$t('P.Search')}}</el-button>
              <el-input
                style="float: right;width:200px;margin-right: 5px;"
                size="mini"
                :placeholder="$t('UserCenter.InventoryNameNo')"
                type="text"
                clearable
                @change="changeContent"
                v-model.trim="pageInfo.content"
              ></el-input>
              <el-select
                class="custom_select"
                :placeholder="$t('UserCenter.CommodityClassification')"
                size="mini"
                v-model="pageInfo.categoryId"
                clearable
              >
                <el-option
                  :key="item.id"
                  :label="$i18n.locale === 'zh' || !item.labelEn == true ? item.label:item.labeleEn"
                  :value="item.id"
                  v-for="item in categoryList"
                >
                  <div
                    v-if="item.parentId === 1"
                  >{{$i18n.locale === 'zh' || !item.labelEn == true ? item.label:item.labelEn}}</div>
                  <div
                    v-else
                  >&nbsp;&nbsp;&nbsp;├{{$i18n.locale === 'zh' || !item.labelEn == true ? item.label:item.labelEn}}</div>
                </el-option>
              </el-select>

              <el-select
                class="custom_select"
                :placeholder="$t('ShopDetail.Brand')"
                size="mini"
                v-model="pageInfo.brandId"
                clearable
              >
                <el-option
                  :key="item.id"
                  :label="($i18n.locale === 'zh' || !item.titleEn == true ? item.title:item.titleEn)"
                  :value="item.id"
                  v-for="item in brandList"
                ></el-option>
              </el-select>

              <el-select
                class="custom_select"
                :placeholder="$t('ShopDetail.Category')"
                size="mini"
                v-model="pageInfo.productlineId"
                clearable
              >
                <el-option
                  :key="item.id"
                  :label="($i18n.locale === 'zh' || !item.nameEn == true ? item.name:item.nameEn)"
                  :value="item.id"
                  v-for="item in productlineList"
                ></el-option>
              </el-select>
              <el-select
                class="custom_select"
                :placeholder="$t('UserCenter.CommodityType')"
                size="mini"
                v-model="pageInfo.goodsType"
                clearable
              >
                <el-option
                  v-for="item in goodsTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </div>
            <div class="rl-tr">
              <span
                v-if="canExportPrice === 1"
                class="mini-export cursor-pointer"
                @click="exportPrice"
              ><i class="iconfont icon-export"></i>{{$t('UserCenter.ExportProductList')}}</span>
              <span v-else class="mini-white-btn" type="info" size="mini"></span>
              <el-popover style="height: 100px;" placement="bottom-end" width="400" trigger="click">
                <div style="border-bottom: 1px solid #d2d2d2;">
                  <el-checkbox
                    style="margin-bottom: 5px;"
                    :indeterminate="isAllFielIndeterminate"
                    v-model="isAllFiel"
                    @change="handleCheckAllFielChange"
                  >{{$t('UserCenter.SelectAll')}}</el-checkbox>
                </div>
                <el-checkbox
                  style="margin-top: 5px;"
                  :indeterminate="isBaseFielIndeterminate"
                  @change="handleCheckAllBaseFielChange"
                  v-model="isBaseFiel"
                >{{$t('Register.BasicInformation')}}</el-checkbox>
                <el-checkbox-group
                  style="padding-left: 25px;margin-bottom: 10px;"
                  v-model="baseFielInfos"
                  @change="handleCheckedBaseFielChange"
                >
                  <el-checkbox label="goodsNo">{{$t('Quotation.GOODSNUMBER')}}</el-checkbox>
                  <el-checkbox label="barCode">{{$t('Quotation.BARCODE')}}</el-checkbox>
                  <el-checkbox label="itemCode">{{$t('Quotation.CODE')}}</el-checkbox>
                  <el-checkbox label="goodsName">{{$t('Quotation.DESCRIPTION')}}</el-checkbox>
                  <el-checkbox label="brandName">{{$t('Quotation.BRAND')}}</el-checkbox>
                  <el-checkbox label="goodsType">{{$t('Quotation.GOODSTYPE')}}</el-checkbox>
                  <el-checkbox label="categoryName">{{$t('Quotation.COMMODITYCLASSIFICATION')}}</el-checkbox>
                  <el-checkbox label="color">{{$t('Quotation.COLOR')}}</el-checkbox>
                  <el-checkbox label="specName">{{$t('Quotation.SPECIFICATION')}}</el-checkbox>
                  <!-- <el-checkbox label="barCode" >{{$t('Quotation.CTN')}}</el-checkbox> -->
                  <el-checkbox label="moq">{{$t('Quotation.MOQ')}}</el-checkbox>
                  <el-checkbox label="weight">{{$t('Quotation.WEIGHT')}}</el-checkbox>
                  <el-checkbox label="stockNum">{{$t('Quotation.INVENTORY')}}</el-checkbox>
                  <el-checkbox label="numOnWay">{{$t('Quotation.TRANSPORTATIONINVENTORY')}}</el-checkbox>
                  <el-checkbox label="sizeStr">{{$t('Quotation.DIMENSION')}}</el-checkbox>
                </el-checkbox-group>
                <el-checkbox
                  :indeterminate="isPriceFielIndeterminate"
                  @change="handleCheckAllPriceFielChange"
                  v-model="isPriceFiel"
                >{{$t('UserCenter.PriceRelated')}}</el-checkbox>
                <el-checkbox-group
                  style="padding-left: 25px;"
                  @change="handleCheckPriceFielChange"
                  v-model="pricesFiel"
                >
                  <el-checkbox label="retailPrice">{{$t('Quotation.SRP')}}</el-checkbox>
                  <el-checkbox label="memberPrice">{{$t('Quotation.MEMBERSHIPPRICE')}}</el-checkbox>
                  <el-checkbox label="wholesalePrice">{{$t('Quotation.WHOLESALEPRICE')}}</el-checkbox>
                  <el-checkbox label="activityInvolved">{{$t('Quotation.ACTIVITIESINVOLVED')}}</el-checkbox>
                </el-checkbox-group>
                <el-button slot="reference" class="export-btn" type="text" size="mini">
                  {{$t('UserCenter.SeeMoreParameters')}}
                  <i class="el-icon-d-arrow-right"></i>
                </el-button>
              </el-popover>
            </div>
            <div class="table">
              <el-table v-loading="loading" :data="tableData">
                <el-table-column
                  v-if="baseFielInfos.indexOf('goodsNo')>=0"
                  :label="$t('Quotation.GOODSNUMBER')"
                  align="center"
                  prop="goodsNo"
                  :width="150"
                ></el-table-column>
                <el-table-column
                  v-if="baseFielInfos.indexOf('barCode')>=0"
                  :label="$t('Quotation.BARCODE')"
                  align="center"
                  show-overflow-tooltip
                  prop="barCode"
                  :min-width="130"
                ></el-table-column>
                <el-table-column
                  v-if="baseFielInfos.indexOf('itemCode')>=0"
                  :label="$t('Quotation.CODE')"
                  align="center"
                  prop="itemCode"
                  :width="130"
                ></el-table-column>
                <el-table-column
                  v-if="baseFielInfos.indexOf('goodsName')>=0"
                  :label="$t('Quotation.DESCRIPTION')"
                  align="center"
                  show-overflow-tooltip
                  prop="itemName"
                  :min-width="120"
                ></el-table-column>
                <el-table-column
                  v-if="baseFielInfos.indexOf('brandName')>=0"
                  :label="$t('Quotation.BRAND')"
                  prop="brandName"
                  align="center"
                  show-overflow-tooltip
                  :min-width="120"
                ></el-table-column>
                <el-table-column
                  v-if="baseFielInfos.indexOf('goodsType')>=0"
                  :label="$t('Quotation.GOODSTYPE')"
                  prop="goodsType"
                  show-overflow-tooltip
                  :formatter="formatGoodsType"
                  align="center"
                  :min-width="110"
                ></el-table-column>
                <el-table-column
                  v-if="baseFielInfos.indexOf('categoryName')>=0"
                  :label="$t('Quotation.COMMODITYCLASSIFICATION')"
                  prop="categoryName"
                  show-overflow-tooltip
                  align="center"
                  :min-width="140"
                ></el-table-column>
                <el-table-column
                  v-if="baseFielInfos.indexOf('color')>=0"
                  :label="$t('Quotation.COLOR')"
                  align="center"
                  prop="colorValue"
                  show-overflow-tooltip
                  :min-width="80"
                ></el-table-column>
                <el-table-column
                  v-if="baseFielInfos.indexOf('specName')>=0"
                  :label="$t('Quotation.SPECIFICATION')"
                  align="center"
                  prop="specValue"
                  show-overflow-tooltip
                  :min-width="130"
                ></el-table-column>
                <el-table-column
                  v-if="baseFielInfos.indexOf('moq')>=0"
                  :label="$t('Quotation.MOQ')"
                  align="center"
                  prop="moq"
                  show-overflow-tooltip
                  :min-width="80"
                ></el-table-column>
                <el-table-column
                  v-if="baseFielInfos.indexOf('weight')>=0"
                  :label="$t('Quotation.WEIGHT')"
                  align="center"
                  prop="weight"
                  show-overflow-tooltip
                  :min-width="80"
                ></el-table-column>
                <el-table-column
                  v-if="baseFielInfos.indexOf('stockNum')>=0"
                  :label="$t('Quotation.INVENTORY')"
                  align="center"
                  prop="stockNum"
                  show-overflow-tooltip
                  :min-width="120"
                ></el-table-column>
                <el-table-column
                  v-if="baseFielInfos.indexOf('numOnWay')>=0"
                  :label="$t('Quotation.TRANSPORTATIONINVENTORY')"
                  align="center"
                  prop="numOnWay"
                  show-overflow-tooltip
                  :min-width="150"
                ></el-table-column>
                <el-table-column
                  v-if="baseFielInfos.indexOf('sizeStr')>=0"
                  :label="$t('Quotation.DIMENSION')"
                  align="center"
                  prop="sizeStr"
                  show-overflow-tooltip
                  :min-width="140"
                ></el-table-column>
                <el-table-column
                  v-if="pricesFiel.indexOf('retailPrice')>=0"
                  :label="$t('Quotation.SRP')"
                  align="center"
                  prop="retailPrice"
                  show-overflow-tooltip
                  :min-width="100"
                >
                  <template slot-scope="scope">
                    <div
                      v-show="$root.currency === 'CNY'"
                      class="rl-text-xxs"
                    >￥{{scope.row.retailPriceZh | keepTwoNum}}</div>
                    <div
                      v-show="$root.currency === 'USD'"
                      class="rl-text-xxs"
                    >${{scope.row.retailPriceEn | keepTwoNum}}</div>
                  </template>
                </el-table-column>
                <el-table-column
                  v-if="pricesFiel.indexOf('memberPrice')>=0"
                  :label="$t('Quotation.MEMBERSHIPPRICE')"
                  align="center"
                  prop="memberPrice"
                  show-overflow-tooltip
                  :min-width="100"
                >
                  <template slot-scope="scope">
                    <div
                      v-show="$root.currency === 'CNY'"
                      class="rl-text-xxs"
                    >￥{{scope.row.memberPriceZh | keepTwoNum}}</div>
                    <div
                      v-show="$root.currency === 'USD'"
                      class="rl-text-xxs"
                    >${{scope.row.memberPriceEn | keepTwoNum}}</div>
                  </template>
                </el-table-column>
                <el-table-column
                  v-if="pricesFiel.indexOf('wholesalePrice')>=0"
                  :label="$t('Quotation.WHOLESALEPRICE')"
                  align="center"
                  prop="wholesalePrice"
                  show-overflow-tooltip
                  :min-width="100"
                >
                  <template slot-scope="scope">
                    <div
                      v-show="$root.currency === 'CNY'"
                      class="rl-text-xxs"
                    >￥{{scope.row.wholesalePriceZh | keepTwoNum}}</div>
                    <div
                      v-show="$root.currency === 'USD'"
                      class="rl-text-xxs"
                    >${{scope.row.wholesalePriceEn | keepTwoNum}}</div>
                  </template>
                </el-table-column>
                <el-table-column
                  v-if="pricesFiel.indexOf('activityInvolved')>=0"
                  :label="$t('Quotation.ACTIVITIESINVOLVED')"
                  align="center"
                  show-overflow-tooltip
                  prop="activityInvolved"
                  :min-width="120"
                ></el-table-column>
              </el-table>
            </div>
          </div>

          <div class="rl-tr rl-margin-top-default">
            <el-pagination
              background
              @current-change="handleCurrentChange"
              @size-change="handleSizeChange"
              layout="total, sizes, prev, pager, next, jumper"
              :page-sizes="[10, 20, 30]"
              :page-size="pageSize"
              :total="totalCount"
            ></el-pagination>
          </div>
        </div>
      </div>
    </div>
    <!-- 导出报价单dialog -->
    <el-dialog
      width="60%"
      :modal-append-to-body="false"
      :visible="exportPriceShow"
      :before-close="closeExportPriceShow"
    >
      <el-form
        class="rl-margin-top-xxxs rl-margin-right-lllg"
        ref="form"
        :model="form"
        label-width="120px"
      >
        <el-form-item :label="$t('UserCenter.SelectionField')">
          <div class="export-price">
            <div class="all-file">
              <el-checkbox
                :indeterminate="isAllIndeterminate"
                v-model="isAll"
                @change="handleCheckAllChange"
              >{{$t('UserCenter.SelectAll')}}</el-checkbox>
            </div>
            <div class="file">
              <el-checkbox
                :indeterminate="isBaseIndeterminate"
                @change="handleCheckAllBaseChange"
                v-model="isBase"
              >{{$t('Register.BasicInformation')}}</el-checkbox>
              <el-checkbox-group
                style="padding-left: 25px;margin-bottom: 10px;"
                v-model="baseInfos"
                @change="handleCheckedBaseChange"
              >
                <el-checkbox label="goodsNo">{{$t('Quotation.GOODSNUMBER')}}</el-checkbox>
                <el-checkbox label="barCode">{{$t('Quotation.BARCODE')}}</el-checkbox>
                <el-checkbox label="itemCode">{{$t('Quotation.CODE')}}</el-checkbox>
                <el-checkbox label="goodsName">{{$t('Quotation.DESCRIPTION')}}</el-checkbox>
                <el-checkbox label="brandName">{{$t('Quotation.BRAND')}}</el-checkbox>
                <el-checkbox label="goodsType">{{$t('Quotation.GOODSTYPE')}}</el-checkbox>
                <el-checkbox label="categoryName">{{$t('Quotation.COMMODITYCLASSIFICATION')}}</el-checkbox>
                <el-checkbox label="imageUrl1">{{$t('Quotation.GRAPHIC')}}</el-checkbox>
                <el-checkbox label="imageUrl5">{{$t('Quotation.PACKING')}}</el-checkbox>
                <el-checkbox label="model">{{$t('Quotation.MODEL')}}</el-checkbox>
                <el-checkbox label="color">{{$t('Quotation.COLOR')}}</el-checkbox>
                <el-checkbox label="specName">{{$t('Quotation.SPECIFICATION')}}</el-checkbox>
                <!-- <el-checkbox label="series" >{{$t('Quotation.SERIES')}}</el-checkbox>
                <el-checkbox label="appliedDevice" >{{$t('Quotation.APPLIEDDEVICES')}}</el-checkbox>-->
                <el-checkbox label="outerBoxNum">{{$t('Quotation.CTN')}}</el-checkbox>
                <el-checkbox label="moq">{{$t('Quotation.MOQ')}}</el-checkbox>
                <el-checkbox label="purchaseCycle">{{$t('Quotation.PURCHASECYCLE')}}</el-checkbox>
                <el-checkbox label="productMethod">{{$t('Quotation.PRODUCTIONMETHODS')}}</el-checkbox>
                <el-checkbox label="weight">{{$t('Quotation.WEIGHT')}}</el-checkbox>
                <el-checkbox label="stockNum">{{$t('Quotation.INVENTORY')}}</el-checkbox>
                <el-checkbox label="numOnWay">{{$t('Quotation.TRANSPORTATIONINVENTORY')}}</el-checkbox>
                <el-checkbox label="sizeStr">{{$t('Quotation.DIMENSION')}}</el-checkbox>
                <el-checkbox label="innerBox">{{$t('Quotation.INNERBOX')}}</el-checkbox>
                <el-checkbox label="outerBox">{{$t('Quotation.OUTERBOX')}}</el-checkbox>
              </el-checkbox-group>
              <el-checkbox
                :indeterminate="isPriceIndeterminate"
                @change="handleCheckAllPriceChange"
                v-model="isPrice"
              >{{$t('UserCenter.PriceRelated')}}</el-checkbox>
              <el-checkbox-group
                style="padding-left: 25px;"
                @change="handleCheckPriceChange"
                v-model="prices"
              >
                <el-checkbox label="retailPrice">{{$t('Quotation.SRP')}}</el-checkbox>
                <el-checkbox label="memberPrice">{{$t('Quotation.MEMBERSHIPPRICE')}}</el-checkbox>
                <el-checkbox label="wholesalePrice">{{$t('Quotation.WHOLESALEPRICE')}}</el-checkbox>
                <el-checkbox label="activityInvolved">{{$t('Quotation.ACTIVITIESINVOLVED')}}</el-checkbox>
              </el-checkbox-group>
            </div>
          </div>
        </el-form-item>
        <el-form-item :label="$t('UserCenter.SelectPage')">
          <el-radio-group v-model="isAllExportGoods">
            <el-radio :label="1">{{$t('UserCenter.ExportAll')}}</el-radio>
            <el-radio :label="2">{{$t('UserCenter.PagingExport')}}</el-radio>
          </el-radio-group>
          <el-pagination
            v-if="isAllExportGoods === 2"
            style="margin-top: 5px;"
            background
            layout="total, sizes, prev, pager, next"
            @current-change="changeFormPage"
            @size-change="changeFormCount"
            :page-sizes="[50, 100, 200]"
            :total="exportPriceItemCount"
          ></el-pagination>
        </el-form-item>
      </el-form>

      <div style="text-align: center;margin-bottom: 10px;">
        <el-button class="mini-search-btn" @click="exportGoodsPrice">{{$t('Message.Confirm')}}</el-button>
        <el-button
          style="margin-left:20px"
          class="mini-pulloff-btn"
          @click="closeExportPriceShow"
        >{{$t('Message.Cancel')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import Left from "@/components/Left.vue";

import {
  getAllCategory,
  getBrandPoList,
  getPriceList,
} from "@/view/userCenter/userCenter";
import GD from "@/assets/js/globalData";
import Vue from "vue";
import { PopupManager } from "element-ui/src/utils/popup";
import Progress from "@/components/loadingProgress";

const LoadingProgress = Vue.extend(Progress);

export default {
  name: "WantBook",
  components: {
    Header,
    Left,
  },
  data() {
    return {
      userState: 2,
      canExportPrice: 2,
      tableHeight: 600,
      loading: false,
      categoryList: [],
      pageSize: 10,
      brandList: [],
      productlineList: [],
      goodsTypes: [
        { value: 1, label: this.$t("UserCenter.Ordinary") },
        { value: 2, label: this.$t("UserCenter.Customized") },
      ],
      pageInfo: {
        page: 1,
        count: 10,
        categoryId: "",
        brandId: "",
        productlineId: "",
        goodsType: "",
        content: "",
      },
      form: {
        // language:'zh_CNY',
        page: 1,
        count: 50,
        categoryId: "",
        brandId: "",
        productlineId: "",
        goodsType: "",
        content: "",
        exportFields: [],
      },
      exportPriceItemCount: 0,
      tableData: [],
      totalCount: 0,
      losegoodsList: [],
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: "zh-RMB", // 语种
      isAllFiel: false,
      isBaseFiel: false,
      isPriceFiel: false,
      isAllFielIndeterminate: true,
      isBaseFielIndeterminate: true,
      isPriceFielIndeterminate: true,
      allBaseFielInfos: [
        "goodsNo",
        "itemCode",
        "barCode",
        "goodsName",
        "model",
        "brandName",
        "goodsType",
        "categoryName",
        "specName",
        "color",
        "weight",
        "stockNum",
        "numOnWay",
        "sizeStr",
        "moq",
      ],
      baseFielInfos: [
        "itemCode",
        "barCode",
        "goodsName",
        "model",
        "specName",
        "color",
        "weight",
        "sizeStr",
      ],
      allFielPrices: [
        "retailPrice",
        "memberPrice",
        "wholesalePrice",
        "activityInvolved",
      ],
      pricesFiel: ["retailPrice", "memberPrice", "wholesalePrice"],
      exportPriceShow: false,
      isAll: false,
      isBase: false,
      isPrice: false,
      isAllIndeterminate: false,
      isBaseIndeterminate: false,
      isPriceIndeterminate: false,
      allBaseInfos: [
        "goodsNo",
        "barCode",
        "itemCode",
        "goodsName",
        "brandName",
        "goodsType",
        "categoryName",
        "imageUrl1",
        "imageUrl5",
        "model",
        "color",
        "specName",
        "outerBoxNum",
        "moq",
        "purchaseCycle",
        "productMethod",
        "weight",
        "stockNum",
        "numOnWay",
        "sizeStr",
        "innerBox",
        "outerBox",
      ],
      baseInfos: [],
      initBaseInfosZH: [
        "barCode",
        "itemCode",
        "goodsName",
        "imageUrl1",
        "imageUrl5",
        "color",
        "moq",
        "purchaseCycle",
        "productMethod",
        "retailPrice",
        "memberPrice",
      ],
      initBaseInfosEN: [
        "barCode",
        "itemCode",
        "imageUrl1",
        "imageUrl5",
        "model",
        "color",
        "specName",
        "outerBoxNum",
        "moq",
        "purchaseCycle",
        "retailPrice",
        "memberPrice",
      ],
      allPrices: [
        "retailPrice",
        "memberPrice",
        "wholesalePrice",
        "activityInvolved",
      ],
      prices: [],
      isAllExportGoods: 1,
    };
  },
  filters: {
    keepTwoNum(value) {
      value = Number(value);
      return value.toFixed(2);
    },
  },
  methods: {
    getLastChoose(recordType) {
      this.$api
        .get(this, "user/u/user/chooseRecord/getLastChoose", {
          recordType: recordType,
        })
        .then((res) => {
          if (
            res.code === 0 &&
            res.recordList !== undefined &&
            res.recordList !== null &&
            res.recordList.length > 0
          ) {
            res.recordList.forEach((item) => {
              if (item.recordType === 1) {
                // 前台报价单
                this.prices = [];
                this.baseInfos = [];
                if (item.language === 1) {
                  // 1.中文  2.英文
                  this.initBaseInfosZH = item.exportFields.split(",");
                } else if (item.language === 2) {
                  this.initBaseInfosEN = item.exportFields.split(",");
                }
              }
            });
          }
          this.getBaseInfos();
        });
    },
    getBaseInfos() {
      let arr = null;
      if (this.$i18n.locale === "en") {
        arr = this.initBaseInfosEN;
      } else {
        arr = this.initBaseInfosZH;
      }
      arr.forEach((field) => {
        if (
          field === "retailPrice" ||
          field === "memberPrice" ||
          field === "activityInvolved" ||
          field === "wholesalePrice"
        ) {
          this.prices.push(field);
        } else {
          this.baseInfos.push(field);
        }
      });
      this.isPrice = this.prices.length === this.allPrices.length;
      this.isPriceIndeterminate =
        this.prices.length > 0 && this.prices.length < this.allPrices.length;
      this.isBase = this.baseInfos.length === this.allBaseInfos.length;
      this.isBaseIndeterminate =
        this.baseInfos.length > 0 &&
        this.baseInfos.length < this.allBaseInfos.length;
      this.isAllIndeterminate = !(
        (this.baseInfos.length === this.allBaseInfos.length &&
          this.prices.length === this.allPrices.length) ||
        (this.baseInfos.length === 0 && this.prices.length === 0)
      );
      this.isAll =
        this.baseInfos.length === this.allBaseInfos.length &&
        this.prices.length === this.allPrices.length;
    },
    changeFormPage(val) {
      this.form.page = val;
    },
    changeFormCount(val) {
      this.form.count = val;
    },
    search() {
      this.getData();
    },
    startLoadingProgress() {
      this.$loadingProgress = new LoadingProgress();
      this.$store.commit("SET_PROGRESS", this.$loadingProgress);
      if (this.$i18n.locale === "zh") {
        this.$loadingProgress.title = "报价单正在计算中";
      } else {
        this.$loadingProgress.title = "The quotation is being calculated";
      }
      this.$loadingProgress.locale = this.$i18n.locale;
      this.$loadingProgress.form = this.form;
      this.$loadingProgress.$mount();
      document.body.appendChild(this.$loadingProgress.$el);
      this.$loadingProgress.visible = true;
      this.$loadingProgress.dom = this.$loadingProgress.$el;
      this.$loadingProgress.dom.style.zIndex = PopupManager.nextZIndex();
    },
    exportGoodsPrice() {
      if (this.$loadingProgress !== null && !this.$loadingProgress.finish) {
        this.$message.warning(this.$t("UserCenter.QuotationExportedError"));
      } else {
        if (this.isAllExportGoods === 1) {
          this.form.page = undefined;
          this.form.count = undefined;
        } else {
          if (this.form.page === undefined) {
            this.form.page = 1;
          }
          if (this.form.count === undefined) {
            this.form.count = 50;
          }
        }
        this.form.categoryId = this.pageInfo.categoryId;
        this.form.brandId = this.pageInfo.brandId;
        this.form.productlineId = this.pageInfo.productlineId;
        this.form.goodsType = this.pageInfo.goodsType;
        this.form.content = this.pageInfo.content;
        this.form.exportFields = "";
        if (this.baseInfos.length > 0) {
          this.form.exportFields = this.baseInfos;
        }
        if (this.prices.length > 0) {
          if (this.form.exportFields !== "") {
            // 数组去重
            this.form.exportFields = [
              ...new Set(this.form.exportFields.concat(this.prices)),
            ];
            this.form.exportFields = this.form.exportFields.join(",");
          } else {
            this.$message.warning(this.$t("UserCenter.InventoryCodeRequired"));
            return;
          }
        }else{
          //修复导出报价单出错的问题
          this.form.exportFields = this.form.exportFields.join(",")
        }
        if (
          this.form.exportFields !== undefined &&
          this.form.exportFields !== 0
        ) {
          this.exportPriceShow = false;
          this.startLoadingProgress();
          this.$message.success(this.$t("UserCenter.ExportRequest"));
          // this.$api.exportData(this, 'user/u/user/goods/exportPriceExcel', this.form).then(res => {
          //   if (res === undefined || res.code === undefined) {
          //     this.timeData = [] // ..成功下载Excel后初始化选中时间
          //     // this.formData.status = [] //..成功夏侯Excel后初始化订单状态
          //   }
          //   const content = res
          //   let blob = new Blob([content], {
          //     type: 'application/ms-excel'
          //   })
          //   let url = window.URL.createObjectURL(blob)
          //   if ('download' in document.createElement('a')) {
          //     let link = document.createElement('a')
          //     link.style.display ='none'
          //     link.href = url
          //     link.setAttribute('download', this.$t('UserCenter.Quotation') + '.xls')
          //     document.body.appendChild(link)
          //     link.click()
          //   } else {
          //     navigator.msSaveBlob(blob, this.$t('UserCenter.Quotation') + '.xls')
          //   }
          // })
        } else {
          this.$message.warning(this.$t("UserCenter.PleaseSelect"));
        }
      }
    },
    dataFot() {
      getAllCategory(this).then((res) => {
        //初始化分类名称显示
        let arr = [];
        for (let i = 0; i < res.length; i++) {
          arr.push(res[i]);
          if (res[i].children != undefined && res[i].children.length > 0) {
            res[i].children.forEach((item) => {
              arr.push(item);
            });
          }
        }
        this.categoryList = arr;
      });
      this.$api
        .get(this, "user/productline/list", {
          page: 1,
          count: 10000,
          isOpen: 1,
        })
        .then((res) => {
          //品类列表
          if (res.code == 0) {
            this.productlineList = res.productlines;
          } 
        });
      getBrandPoList(this).then((res) => {
        //品牌列表
        if (res.code === 0) {
          this.brandList = res.brands;
        } 
      });
    },
    closeExportPriceShow() {
      // 导出报价单dialog默认为隐藏
      this.exportPriceShow = false;
    },
    exportPrice() {
      // 导出报价单
      this.exportPriceShow = true;
    },
    formatGoodsType(row, col, val) {
      //商品类型
      switch (val) {
        case 1:
          return this.$t("UserCenter.Ordinary");
          break;
        case 2:
          return this.$t("UserCenter.Customized");
          break;
      }
    },
    handleCheckAllFielChange(val) {
      this.isAllFielIndeterminate = false;
      this.baseFielInfos = val ? this.allBaseFielInfos : [];
      this.pricesFiel = val ? this.allFielPrices : [];
      this.isBaseFiel = val;
      this.isBaseFielIndeterminate = false;
      this.isPriceFiel = val;
      this.isPriceFielIndeterminate = false;
    },
    handleCheckAllBaseFielChange(val) {
      this.baseFielInfos = val ? this.allBaseFielInfos : [];
      this.isBaseFiel = val;
      this.isBaseFielIndeterminate = false;
      this.isAllFielIndeterminate = !(
        (this.baseFielInfos.length === this.allBaseFielInfos.length &&
          this.pricesFiel.length === this.allFielPrices.length) ||
        (this.baseFielInfos.length === 0 && this.pricesFiel.length === 0)
      );
      this.isAllFiel =
        this.baseFielInfos.length === this.allBaseFielInfos.length &&
        this.pricesFiel.length === this.allFielPrices.length;
    },
    handleCheckedBaseFielChange(value) {
      this.isBaseFiel = value.length === this.allBaseFielInfos.length;
      this.isBaseFielIndeterminate =
        value.length > 0 && value.length < this.allBaseFielInfos.length;
      this.isAllFielIndeterminate = !(
        (this.baseFielInfos.length === this.allBaseFielInfos.length &&
          this.pricesFiel.length === this.allFielPrices.length) ||
        (this.baseFielInfos.length === 0 && this.pricesFiel.length === 0)
      );
      this.isAllFiel =
        this.baseFielInfos.length === this.allBaseFielInfos.length &&
        this.pricesFiel.length === this.allFielPrices.length;
    },
    handleCheckAllPriceFielChange(val) {
      this.pricesFiel = val ? this.allFielPrices : [];
      this.isPriceFiel = val;
      this.isPriceFielIndeterminate = false;
      this.isAllFielIndeterminate = !(
        (this.baseFielInfos.length === this.allBaseFielInfos.length &&
          this.pricesFiel.length === this.allFielPrices.length) ||
        (this.baseFielInfos.length === 0 && this.pricesFiel.length === 0)
      );
      this.isAllFiel =
        this.baseFielInfos.length === this.allBaseFielInfos.length &&
        this.pricesFiel.length === this.allFielPrices.length;
    },
    handleCheckPriceFielChange(value) {
      this.isPriceFiel = value.length === this.allFielPrices.length;
      this.isPriceFielIndeterminate =
        value.length > 0 && value.length < this.allFielPrices.length;
      this.isAllFielIndeterminate = !(
        (this.baseFielInfos.length === this.allBaseFielInfos.length &&
          this.pricesFiel.length === this.allFielPrices.length) ||
        (this.baseFielInfos.length === 0 && this.pricesFiel.length === 0)
      );
      this.isAllFiel =
        this.baseFielInfos.length === this.allBaseFielInfos.length &&
        this.pricesFiel.length === this.allFielPrices.length;
    },
    handleCheckAllChange(val) {
      this.isAllIndeterminate = false;
      this.baseInfos = val ? this.allBaseInfos : [];
      this.prices = val ? this.allPrices : [];
      this.isBase = val;
      this.isBaseIndeterminate = false;
      this.isPrice = val;
      this.isPriceIndeterminate = false;
    },
    handleCheckAllBaseChange(val) {
      this.baseInfos = val ? this.allBaseInfos : [];
      this.isBase = val;
      this.isBaseIndeterminate = false;
      this.isAllIndeterminate = !(
        (this.baseInfos.length === this.allBaseInfos.length &&
          this.prices.length === this.allPrices.length) ||
        (this.baseInfos.length === 0 && this.prices.length === 0)
      );
      this.isAll =
        this.baseInfos.length === this.allBaseInfos.length &&
        this.prices.length === this.allPrices.length;
    },
    handleCheckedBaseChange(value) {
      this.isBase = value.length === this.allBaseInfos.length;
      this.isBaseIndeterminate =
        value.length > 0 && value.length < this.allBaseInfos.length;
      this.isAllIndeterminate = !(
        (this.baseInfos.length === this.allBaseInfos.length &&
          this.prices.length === this.allPrices.length) ||
        (this.baseInfos.length === 0 && this.prices.length === 0)
      );
      this.isAll =
        this.baseInfos.length === this.allBaseInfos.length &&
        this.prices.length === this.allPrices.length;
    },
    handleCheckAllPriceChange(val) {
      this.prices = val ? this.allPrices : [];
      this.isPrice = val;
      this.isPriceIndeterminate = false;
      this.isAllIndeterminate = !(
        (this.baseInfos.length === this.allBaseInfos.length &&
          this.prices.length === this.allPrices.length) ||
        (this.baseInfos.length === 0 && this.prices.length === 0)
      );
      this.isAll =
        this.baseInfos.length === this.allBaseInfos.length &&
        this.prices.length === this.allPrices.length;
    },
    handleCheckPriceChange(value) {
      this.isPrice = value.length === this.allPrices.length;
      this.isPriceIndeterminate =
        value.length > 0 && value.length < this.allPrices.length;
      this.isAllIndeterminate = !(
        (this.baseInfos.length === this.allBaseInfos.length &&
          this.prices.length === this.allPrices.length) ||
        (this.baseInfos.length === 0 && this.prices.length === 0)
      );
      this.isAll =
        this.baseInfos.length === this.allBaseInfos.length &&
        this.prices.length === this.allPrices.length;
    },
    fLangChange(value) {
      window.localStorage.setItem("bLang", value);
      this.$i18n.locale = value.split("-")[0];
    },
    // 当前页码
    handleCurrentChange(val) {
      this.pageInfo.page = val;
      this.getTableData();
    },
    // 每页条数
    handleSizeChange(val) {
      this.pageInfo.count = val;
      this.pageInfo.page = 1;
      this.getTableData();
    },
    getTableData() {
      this.loading = true;
      this.$api
        .get(this, "user/u/user/goods/getPrice", this.pageInfo)
        .then((res) => {
          this.loading = false;
          if (res.code === 0) {
            this.tableData = res.goods;
          }
        });
    },
    getData() {
      getPriceList(this, this.pageInfo).then((res) => {
        this.loading = false;
        if (res.code === 0) {
          this.tableData = res.tableData;
          this.totalCount = res.count;
          this.exportPriceItemCount = this.totalCount;
          this.canExportPrice = res.canExportPrice;
        }
      });
     
    },
    changeContent(val) {
      if (val === undefined || val === "" || val === null) {
        this.getData();
      }
    },
  },
  created() {
    // const documentHeight = document.body.clientHeight;
    // this.tableHeight = parseInt(documentHeight  *  0.8  -  85); // 计算表高度，固定表头
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem("bLang")
      ? window.localStorage.getItem("bLang")
      : "zh-RMB";
    this.dataFot();
    this.getData();
    this.getLastChoose(1);
  },
  activated() {
    this.getData();
  },
  watch: {
    "pageInfo.categoryId": {
      handler() {
        this.pageInfo.page = 1;
        this.getData();
      },
      deep: true,
    },
    "pageInfo.brandId": {
      handler() {
        this.pageInfo.page = 1;
        this.getData();
      },
      deep: true,
    },
    "pageInfo.productlineId": {
      handler() {
        this.pageInfo.page = 1;
        this.getData();
      },
      deep: true,
    },
    "pageInfo.goodsType": {
      handler() {
        this.pageInfo.page = 1;
        this.getData();
      },
      deep: true,
    },
  },
};
</script>
<style lang="less" scoped>
@import url("../../assets/less/variable.less");
.user {
  width: 100%;
}
.user-main {
  width: 1200px;
}
.user-right {
  width: 1030px;
  .user-right-title {
    padding-bottom: 10px;
    border-bottom: 1px solid @bdLightColor;
    font-size: 20px;
  }
  .content {
    padding: 24px 40px 0;
    border: 2px solid @bdLightColor;
    border-radius: 8px;
    .iconfont {
      margin-right: 2px;
      color: @lighterGray;
    }
    .mini-export {
      margin-right: 20px;
      color: @blue;
      &:hover {
        opacity: 0.6;
      }
    }
  }
  .query {
    margin-bottom: 25px;
    /deep/ .el-input {
      width: 140px;
      font-size: 12px;
      box-sizing: border-box;
      border-radius: 4px;
    }
    /deep/ .el-input--mini .el-input__inner {
      height: 38px;
      line-height: 38px;
      border: 1px solid @bdLighterColor;
    }
    .el-button {
      padding: 0 12px;
      height: 38px;
      line-height: 38px;
      box-sizing: border-box;
      border-radius: 4px;
    }
    .mini-search-btn {
      background-color: @blue;
    }
  }
  .export-btn {
    font-size: 14px;
    color: @blue;
    cursor: pointer;
    &:hover {
      opacity: 0.6;
    }
  }
  .table {
    width: 100%;
    margin-top: 5px;
    margin-bottom: 30px;
    /deep/ .el-table {
      width: 100%;
      word-wrap: break-word;
      word-break: break-all;
      border-collapse: collapse;
      tr {
        &:hover {
          background-color: @lightGrayBg;
        }
        th {
          padding: 0;
          height: 32px;
          line-height: 32px;
          text-align: center;
          background-color: @bdLightColor;
          font-size: 12px;
          color: @gray;
          font-weight: normal;
        }
        td {
          height: 53px;
          text-align: center;
          font-size: 12px;
          color: @lightBlack;
          border: none;
          border-top: 1px dashed @bdLighterColor;
        }
      }
    }
  }
}
.export-price {
  border: 1px solid transparent;
  background-color: #eef8fa;
  .all-file {
    border-bottom: 1px solid #d2d2d2;
    padding-left: 10px;
    padding-right: 10px;
  }
  .file {
    padding-left: 10px;
    padding-right: 10px;
  }
}
.custom_select {
  width: 150px;
}
</style>
