<template>
  <div>
    <div class="user rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState"></Header>
      <!--主内容-->
      <div class="user-main rl-clear rl-margin-zero">
        <!--公共左边-->
        <Left></Left>
        <div
          class="
            user-right
            rl-fr rl-bg-white rl-margin-top-default rl-padding-bottom-double
          "
        >
          <div class="content">
            <h6 class="user-right-title">
              <span v-if="noHandle">{{ $t("versionsps.UnOrderDetail") }}</span>
              <span v-if="orderHandle">{{
                $t("versionsps.OrderedDetail")
              }}</span>
              <span class="back" @click="back"
                ><i class="el-icon-d-arrow-left"></i
                >{{ $t("versionsps.BackList") }}</span
              >
            </h6>
            <div class="main-content" v-loading="loading2">
              <!-- 订单基本信息 -->
              <div class="box-has-border">
                <div class="title" style="margin-top: 0px">
                  <span>{{ $t("versionsps.OrderInfo") }}</span>
                </div>
                <div class="half-width">
                  <el-form
                    :model="formData"
                    status-icon
                    :rules="rules"
                    label-position="right"
                    ref="formData1"
                    v-if="formData"
                  >
                    <el-form-item
                      :label="$t('versionsps.OrderNo')"
                      v-if="checkhHandleFlag == 1"
                      class="el-form-pad"
                    >
                      <template>
                        <el-button
                          size="mini"
                          type="text"
                          @click="onEdit(item)"
                          v-for="(item, index) in allIds"
                          :key="index"
                        >
                          {{ item }}
                        </el-button>
                      </template>
                    </el-form-item>
                    <el-form-item
                      :label="$t('Foot.PaymentMethod')"
                      prop="payWay"
                    >
                      <el-select
                        :placeholder="$t('P.Select')"
                        size="mini"
                        v-model="formData.payWay"
                        :disabled="disableIsShow"
                      >
                        <el-option :value="1" :label="$t('P.Alipay')">
                        </el-option>
                        <el-option :value="2" :label="$t('P.WechatPay')">
                        </el-option>
                        <el-option :value="3" :label="$t('P.RangeCheckout')">
                        </el-option>
                        <el-option
                          :value="4"
                          :label="$t('versionsps.CompanyTransfer')"
                        >
                        </el-option>
                        <el-option
                          v-show="userTradeType === 1"
                          :value="5"
                          :label="$t('ConfirmOrder.BalancePaid')"
                        >
                        </el-option>
                        <el-option
                          :value="6"
                          :label="$t('versionsps.OnlinePay')"
                        >
                        </el-option>
                      </el-select>
                    </el-form-item>
                    <el-form-item
                      :label="$t('Foot.DeliveryMethod')"
                      prop="distributionId"
                    >
                      <el-select
                        v-model="formData.distributionId"
                        :placeholder="$t('P.Select')"
                        size="mini"
                        :disabled="disableIsShow"
                      >
                        <el-option
                          v-for="item in orderDistriList"
                          :key="item.id"
                          :label="item.name"
                          :value="item.id"
                        ></el-option>
                      </el-select>
                    </el-form-item>
                  </el-form>
                </div>
                <div class="half-width">
                  <el-form
                    :model="formData"
                    status-icon
                    :rules="rules"
                    label-position="right"
                    ref="formData2"
                  >
                    <!-- <el-form-item label="订单类型:" prop="orderTypeValue">
                      <el-select v-model="formData.orderTypeValue" placeholder="请选择" size="mini" :disabled="disableIsShow" @change="orderTypeValueChange">
                        <el-option v-for="item in orderTypeList" :key="item.orderTypeValue" :label="item.orderTypeName" :value="item.orderTypeValue"></el-option>
                      </el-select>
                    </el-form-item> -->
                    <el-form-item
                      :label="$t('UserCenter.OrderTime')"
                      v-if="checkhHandleFlag == 1"
                    >
                      {{ formData.orderCreateTime | formatDate }}
                    </el-form-item>
                    <el-form-item :label="$t('versionsps.IsInvoice')">
                      <el-radio-group v-model="formData.isInvoice">
                        <el-radio :label="1" :disabled="disableIsShow">{{
                          $t("P.Yes")
                        }}</el-radio>
                        <el-radio :label="0" :disabled="disableIsShow">{{
                          $t("P.No")
                        }}</el-radio>
                      </el-radio-group>
                      <!-- <el-select placeholder="请选择" size="mini" v-model="formData.isInvoice" :disabled="disableIsShow">
                        <el-option :value="1" label="是"> </el-option>
                        <el-option :value="0" label="否"> </el-option>
                      </el-select> -->
                    </el-form-item>
                    <el-form-item
                      :label="$t('versionsps.InvoiceType')"
                      v-if="invoiceShow"
                    >
                      <el-select
                        :placeholder="$t('P.Select')"
                        size="mini"
                        v-model="formData.invoiceType"
                        v-if="invoiceShow"
                        :disabled="disableIsShow"
                      >
                        <el-option
                          :value="1"
                          :label="$t('UserCenter.Ordinary')"
                        >
                        </el-option>
                        <el-option
                          :value="2"
                          :label="$t('versionsps.VATInvoice')"
                        >
                        </el-option>
                      </el-select>
                    </el-form-item>
                  </el-form>
                </div>
              </div>
              <!-- 收货信息 -->
              <div class="box-has-border">
                <div class="title">
                  <span>{{ $t("versionsps.ShippingInfo") }}</span>
                </div>
                <div class="half-width one">
                  <el-form
                    :model="formData"
                    status-icon
                    :rules="rules"
                    label-position="right"
                    ref="formData3"
                  >
                    <el-form-item
                      :label="$t('UserCenter.RecipientName')"
                      prop="userName"
                    >
                      <el-input
                        v-model="formData.userName"
                        size="mini"
                        class="box-width"
                        :disabled="disableIsShow"
                      />
                    </el-form-item>
                    <el-form-item :label="$t('UserCenter.Tel')" prop="mobile">
                      <el-input
                        v-model="formData.mobile"
                        size="mini"
                        class="box-width"
                        oninput="value=value.replace(/[^\d]/g,'')"
                        :disabled="disableIsShow"
                      />
                    </el-form-item>
                    <el-form-item
                      :label="$t('UserCenter.Address')"
                      prop="provinceId"
                    >
                      <el-select
                        v-model="formData.provinceId"
                        :placeholder="$t('P.Select')"
                        class="box-width"
                        size="mini"
                        :disabled="disableIsShow"
                        @change="provinceIdChange($event)"
                      >
                        <el-option
                          v-for="province in this.RegionForChose.provinceId"
                          :key="province.id"
                          :label="province.regionName"
                          :value="province.id"
                        ></el-option>
                      </el-select>
                      <el-select
                        v-model="formData.cityId"
                        v-if="cityShow"
                        :placeholder="$t('P.Select')"
                        class="box-width"
                        size="mini"
                        :disabled="disableIsShow"
                        @change="cityIdChange($event)"
                      >
                        <el-option
                          v-for="city in this.RegionForChose.cityId"
                          :key="city.id"
                          :label="city.regionName"
                          :value="city.id"
                        ></el-option>
                      </el-select>
                      <el-select
                        v-model="formData.districtId"
                        v-if="townShow"
                        :placeholder="$t('P.Select')"
                        class="box-width"
                        size="mini"
                        :disabled="disableIsShow"
                      >
                        <el-option
                          v-for="districtId in this.RegionForChose.districtId"
                          :key="districtId.id"
                          :label="districtId.regionName"
                          :value="districtId.id"
                        ></el-option>
                      </el-select>
                    </el-form-item>
                    <el-form-item
                      :label="$t('versionsps.DetailAdress')"
                      prop="address"
                    >
                      <el-input
                        v-model="formData.address"
                        size="mini"
                        class="box-width long"
                        :disabled="disableIsShow"
                      />
                    </el-form-item>
                    <el-form-item :label="$t('UserCenter.Remarks')">
                      <el-input
                        v-model="formData.remark"
                        size="mini"
                        class="box-width long"
                        :disabled="disableIsShow"
                      />
                    </el-form-item>
                  </el-form>
                </div>
              </div>
              <!-- 商品信息 -->
              <div class="box-has-border">
                <div class="title">
                  <span>{{ $t("versionsps.ProductInfo") }}</span>
                </div>
                <!-- <div class="order-message rl-tr">
                  <h4 class="rl-fl">{{$t('versionsps.UnderstockMsg')}}</h4>
                  <span class="educe" v-if="orderShow" @click="exportExcel()"
                    ><i class="iconfont icon-export"></i
                    >{{ $t("versionsps.ExportStockoutList") }}</span
                  >
                  <span
                    class="lookPromotion"
                    v-if="
                      (promotionIds.ruleIds !== undefined &&
                        promotionIds.ruleIds !== null &&
                        promotionIds.ruleIds.length > 0) ||
                      (promotionIds.gradeRuleId !== undefined &&
                        promotionIds.gradeRuleId !== null)
                    "
                    @click="lookPromotion()"
                    >{{ $t("versionsps.ViewActivity")
                    }}<i class="el-icon-d-arrow-right"></i
                  ></span>
                </div> -->
                <el-table
                  :data="formData.orderGoods"
                  class="tableCenter orderGoods"
                  id="formData"
                >
                  <el-table-column
                    align="center"
                    type="index"
                    :min-width="50"
                  ></el-table-column>
                  <el-table-column
                    align="center"
                    prop="itemCode"
                    :label="$t('UserCenter.InventoryNo')"
                    :min-width="110"
                  ></el-table-column>
                  <el-table-column
                    align="center"
                    prop="barCode"
                    :label="$t('ShopCarts.BarCode')"
                    :width="120"
                  ></el-table-column>
                  <el-table-column
                    align="center"
                    :label="$t('versionsps.SoldOutStatus')"
                    :width="80"
                  >
                    <template slot-scope="scope">
                      {{ saleStatusFormatter(scope.row.saleStatus) }}
                    </template>
                  </el-table-column>
                  <el-table-column
                    v-if="
                      formData.orderGoods !== undefined &&
                      formData.orderGoods[0].goodsType === 3
                    "
                    align="center"
                    :label="$t('ShopCarts.Picture')"
                    :width="80"
                  >
                    <template slot-scope="scope">
                      <el-image
                        v-if="scope.row.newDiy.image"
                        style="width: 50px; height: 50px"
                        :src="scope.row.newDiy.image"
                        fit="contain"
                        :preview-src-list="[scope.row.newDiy.image]"
                      >
                      </el-image>
                      <el-button
                        v-else
                        type="text"
                        @click="diyPicture(scope.row)"
                        >定制图片</el-button
                      >
                    </template>
                  </el-table-column>
                  <el-table-column
                    align="center"
                    prop="itemName"
                    :label="$t('UserCenter.InventoryName')"
                    :min-width="120"
                  ></el-table-column>
                  <el-table-column
                    align="center"
                    v-if="
                      formData.orderGoods !== undefined &&
                      formData.orderGoods[0].goodsType === 3
                    "
                    :label="$t('ShopCarts.Material')"
                    :min-width="110"
                  >
                    <template slot-scope="scope">
                      <span>{{ scope.row.newDiy.materialName }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column
                    align="center"
                    v-if="
                      formData.orderGoods !== undefined &&
                      formData.orderGoods[0].goodsType === 3
                    "
                    :label="$t('P.model')"
                    :min-width="110"
                  >
                    <template slot-scope="scope">
                      <span>{{ scope.row.newDiy.modelName }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column
                    align="center"
                    :label="$t('P.Price')"
                    :min-width="80"
                  >
                    <template slot-scope="scope">
                      <i class="asmd">{{
                        currencyType === "CNY" ? "￥" : "$"
                      }}</i
                      >{{ scope.row.distributorPrice | NumFormat }}
                    </template>
                  </el-table-column>
                  <el-table-column
                    align="center"
                    :label="$t('versionsps.PriceDiscount')"
                    :min-width="100"
                  >
                    <template slot-scope="scope">
                      <i class="asmd">{{
                        currencyType === "CNY" ? "￥" : "$"
                      }}</i
                      >{{ scope.row.promotionAmount | NumFormat }}
                    </template>
                  </el-table-column>
                  <el-table-column
                    align="center"
                    prop="itemCount"
                    :label="$t('versionsps.initImportQua')"
                    :min-width="110"
                  >
                  </el-table-column>
                  <el-table-column
                    align="center"
                    :label="$t('versionsps.TotalQuantityOrder')"
                    :min-width="160"
                  >
                    <template slot-scope="scope">
                      <div class="batch_input_number">
                        <span
                          class="batch_minus"
                          @click="handelMinus(scope.row)"
                        >
                          -
                        </span>
                        <div>
                          <!-- checkhHandleFlag 1 已下单 2 未下单 -->
                          <input
                            class="batch_input1"
                            v-if="checkhHandleFlag == 1"
                            @blur="handelChangeNum(scope.row)"
                            type="text"
                            v-model="scope.row.actualOrderCount"
                            maxlength="8"
                            onkeyup="this.value=this.value.replace(/\D/g,'')"
                            :disabled="
                              disableIsShow ||
                              (scope.row.itemType !== undefined &&
                                scope.row.itemType !== null &&
                                scope.row.itemType === 2)
                            "
                          />
                          <input
                            class="batch_input1"
                            v-else
                            :max="
                              scope.row.goodsType == 1
                                ? scope.row.maxCount
                                : Infinity
                            "
                            @blur="handelChangeNum(scope.row)"
                            type="text"
                            v-model="scope.row.actualOrderCount"
                            maxlength="8"
                            onkeyup="this.value=this.value.replace(/\D/g,'')"
                            :disabled="
                              disableIsShow ||
                              (scope.row.itemType !== undefined &&
                                scope.row.itemType !== null &&
                                scope.row.itemType === 2)
                            "
                          />
                        </div>
                        <span
                          class="batch_plusSign"
                          @click="handelPlusSing(scope.row)"
                          >+</span
                        >
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column
                    align="center"
                    v-if="
                      formData.orderGoods !== undefined &&
                      formData.orderGoods[0].goodsType !== 3
                    "
                    prop="inWarehouseCount"
                    :label="$t('versionsps.OrderInstockQuan')"
                    :min-width="110"
                  >
                  </el-table-column>
                  <el-table-column
                    align="center"
                    v-if="
                      formData.orderGoods !== undefined &&
                      formData.orderGoods[0].goodsType !== 3
                    "
                    prop="onWayCount"
                    :label="$t('versionsps.OrderIntransitQuan')"
                    :min-width="110"
                  >
                  </el-table-column>
                  <el-table-column
                    align="center"
                    v-if="
                      formData.orderGoods !== undefined &&
                      formData.orderGoods[0].goodsType !== 3
                    "
                    prop="itemLoseCount"
                    :label="$t('versionsps.StockoutQuan')"
                    :min-width="110"
                  >
                  </el-table-column>
                  <el-table-column
                    align="center"
                    :label="$t('versionsps.Subtotal')"
                    :min-width="120"
                  >
                    <template slot-scope="scope">
                      <i class="asmd">{{
                        currencyType === "CNY" ? "￥" : "$"
                      }}</i
                      >{{ scope.row.amountTotal | NumFormat }}
                    </template>
                  </el-table-column>

                  <el-table-column
                    align="center"
                    :label="$t('ShopCarts.Operation')"
                    v-if="checkhHandleFlag == 2"
                    :min-width="150"
                  >
                    <template slot-scope="scope">
                      <span
                        v-if="
                          (scope.row.itemType === undefined ||
                            scope.row.itemType === null ||
                            scope.row.itemType !== 2) &&
                          scope.row.rules !== undefined &&
                          (scope.row.rules.length > 1 ||
                            (scope.row.rules.length === 1 &&
                              (scope.row.ruleId === undefined ||
                                scope.row.ruleId === null) &&
                              (scope.row.gradeDiscountId === undefined ||
                                scope.row.gradeDiscountId === null)))
                        "
                        @click="changePromotion(scope.row)"
                        >{{ $t("ShopCarts.Switch") }}</span
                      >
                      <span
                        class="mini-delete-btn"
                        @click="handleDelete(scope.$index, scope.row)"
                        >{{ $t("ShopCarts.Delete") }}</span
                      >
                    </template>
                  </el-table-column>
                </el-table>
                <div class="orderMoney" v-if="checkhHandleFlag == 1">
                  <div class="cost-info">
                    <span class="cost-label">
                      &nbsp; {{ $t("P.AggregatePriceA") }}</span
                    >
                    <span class="cost-val asmd"
                      >{{ currencyType === "CNY" ? "￥" : "$"
                      }}{{ formData.goodsAmount | NumFormat }}
                    </span>
                  </div>
                  <div class="cost-info">
                    <span class="cost-label">
                      &nbsp; {{ $t("P.AggregatePriceF") }}</span
                    >
                    <span class="cost-val asmd">
                      {{ currencyType === "CNY" ? "￥" : "$"
                      }}{{ formData.distributionMoney | NumFormat }}
                    </span>
                  </div>
                  <!----服务费金额---->
                  <div class="cost-info" v-if="formData.goodsServiceAmount > 0">
                    <span class="cost-label">
                      &nbsp; {{ $t("P.ServiceAmount") }}</span
                    >
                    <span v-if="currencyType === 'USD'" class="cost-val asmd">
                      {{ currencyType === "CNY" ? "￥" : "$"
                      }}{{ formData.foreignGoodsServiceAmount | NumFormat }}
                    </span>
                    <span v-if="currencyType === 'CNY'" class="cost-val asmd">
                      {{ currencyType === "CNY" ? "￥" : "$"
                      }}{{ formData.goodsServiceAmount | NumFormat }}
                    </span>
                  </div>
                  <div class="cost-info">
                    <span class="cost-label">
                      &nbsp; {{ $t("P.AggregatePriceG") }}</span
                    >
                    <span class="cost-val asmd">
                      {{ currencyType === "CNY" ? "￥" : "$"
                      }}{{ formData.orderAmount | NumFormat }}
                    </span>
                  </div>
                </div>
              </div>
              <div v-if="checkhHandleFlag == 2">
                <div class="tableCenters">
                  <el-button
                    class="mini-search-btn"
                    @click="handleSave('formData')"
                    :disabled="disabledShow"
                  >
                    {{ $t("versionsps.SaveOrder") }}
                  </el-button>
                  <el-button
                    class="mini-search-btn"
                    @click="handleSubmit('formData')"
                    :disabled="disabledShow"
                  >
                    {{ $t("ShopCarts.SubmitOrder") }}
                  </el-button>
                </div>
              </div>
            </div>
            <el-dialog
              :visible="changePromotionShow"
              :before-close="cancelChange"
            >
              <el-table
                :data="goods.rules"
                :show-header="false"
                ref="table"
                highlight-current-row
                style="text-align: center"
              >
                <el-table-column align="center" width="60">
                  <template slot-scope="scope">
                    <el-radio
                      @change="changeRule(scope.row)"
                      style="padding-left: 10px"
                      v-model="ruleId"
                      :label="scope.row.id"
                    ></el-radio>
                  </template>
                </el-table-column>
                <el-table-column>
                  <template slot-scope="scope">
                    <div class="pomotion-rule">
                      <span style="font-weight: 500">{{
                        $t("ShopCarts.Rule") + "：" + scope.row.label
                      }}</span>
                      <span
                        v-if="scope.row.target !== 1"
                        class="present-label"
                        >{{
                          !scope.row.isGrade && scope.row.isEnjoy === 1
                            ? $t("versionsps.OrderUse")
                            : $t("versionsps.OrderNouse")
                        }}</span
                      >
                      <div v-if="scope.row.isGrade">
                        <span class="span"
                          >{{ $t("ShopCarts.Condition") + "："
                          }}{{
                            scope.row.discountBeforeAfter === 1
                              ? $t("versionsps.BeforeDiscount")
                              : $t("versionsps.AfterDiscount")
                          }}{{
                            scope.row.moneyOrCount === 1
                              ? scope.row.oneBuyMoney + $t("P.RMB")
                              : scope.row.oneBuyCount +
                                $t("Activitys.OnlyGift2")
                          }}{{
                            $t("versionsps.Discount") + scope.row.discount + "%"
                          }}</span
                        >
                      </div>
                      <!-- <span style="font-weight: 500;" >{{$t('ShopCarts.Rule') + "："+ scope.row.label}}</span> <span v-if="scope.row.target !== 1" class="present-label">{{(!scope.row.isGrade && scope.row.isEnjoy === 1) ? 'aa' : 'bb'}}</span>
                      <div v-if="scope.row.isGrade">
                        <span class="span">{{$t('ShopCarts.Condition') + "："}}{{scope.row.discountBeforeAfter===1 ? 'a' : 'b'}}{{scope.row.moneyOrCount === 1 ? (scope.row.oneBuyMoney+$t('P.RMB')):(scope.row.oneBuyCount+$t('Activitys.OnlyGift2'))}}{{'cc'+scope.row.discount+"%"}}</span>
                      </div> -->
                      <div v-else>
                        <span
                          class="span"
                          v-for="(ruleCondition, index) in scope.row
                            .ruleConditions"
                          :key="ruleCondition.id"
                        >
                          {{
                            $t("ShopCarts.Condition") +
                            (index + 1) +
                            "：" +
                            ruleCondition.label
                          }}
                        </span>
                      </div>
                    </div>
                  </template>
                </el-table-column>
              </el-table>
              <div class="foot-btn" style="text-align: center">
                <el-button class="mini-search-btn" @click="submitChange">{{
                  $t("P.OK")
                }}</el-button>
                <el-button size="mini" @click="cancelChange">{{
                  $t("P.Cancel")
                }}</el-button>
              </div>
            </el-dialog>

            <el-dialog :visible="promotionShow" :before-close="cancelLook">
              <div v-loading="loading3">
                <div v-if="gradeRule !== null">
                  <el-row align="middle" type="flex" class="bg-purple-light">
                    <el-col :span="8" class="bg-purple">
                      <div>
                        <div style="font-weight: 500">
                          {{ $t("versionsps.GradeRules") }}:
                          {{ gradeRule.policyName }}
                        </div>
                      </div>
                    </el-col>
                    <el-col :span="16">
                      <div style="padding: 10px">
                        <div>{{ gradeRule.description }}</div>
                      </div>
                    </el-col>
                  </el-row>
                  <el-table
                    border
                    :data="gradeRule.goods"
                    ref="table"
                    header-row-class-name="header-row"
                    class="tableCenter orderGoods"
                    style="text-align: center"
                  >
                    <el-table-column
                      align="center"
                      :label="$t('versionsps.GoodsCode')"
                      prop="itemCode"
                    ></el-table-column>
                    <el-table-column
                      align="center"
                      :label="$t('ShopCarts.ItemName')"
                      prop="itemName"
                    ></el-table-column>
                    <el-table-column
                      align="center"
                      :label="$t('ShopCarts.Quantity')"
                      prop="itemCount"
                    ></el-table-column>
                    <el-table-column
                      align="center"
                      :label="$t('P.Price')"
                      prop="distributorPrice"
                    >
                      <template slot-scope="scope">
                        <i class="asmd">{{
                          currencyType === "CNY" ? "￥" : "$"
                        }}</i
                        >{{ scope.row.distributorPrice | NumFormat }}
                      </template>
                    </el-table-column>
                    <el-table-column
                      align="center"
                      :label="$t('versionsps.DiscountPrice')"
                      prop="actualPrice"
                    >
                      <template slot-scope="scope">
                        <i class="asmd">{{
                          currencyType === "CNY" ? "￥" : "$"
                        }}</i
                        >{{ scope.row.actualPrice | NumFormat }}
                      </template>
                    </el-table-column>
                  </el-table>
                </div>
                <div v-if="promotionRules.length > 0">
                  <div
                    v-for="promotionRule in promotionRules"
                    :key="promotionRule.id"
                  >
                    <el-row align="middle" type="flex" class="bg-purple-light">
                      <el-col :span="8" class="bg-purple">
                        <div>
                          <div style="font-weight: 500">
                            {{ $t("versionsps.ActivityRules") }}:
                            {{ promotionRule.label }}
                          </div>
                        </div>
                      </el-col>
                      <el-col :span="13">
                        <div style="padding: 10px">
                          <div
                            v-for="(
                              condition, index
                            ) in promotionRule.conditions"
                            :key="condition.id"
                          >
                            {{
                              $t("versionsps.ActivityCondition") +
                              (index + 1) +
                              "：" +
                              condition.label
                            }}
                          </div>
                        </div>
                      </el-col>
                      <el-col :span="4">
                        <div
                          style="text-align: center"
                          v-if="
                            promotionRule.selectCondition !== undefined &&
                            ((promotionRule.selectCondition.isSpecial !==
                              undefined &&
                              promotionRule.selectCondition.isSpecial === 1 &&
                              promotionRule.selectCondition.isEnjoy === 1 &&
                              promotionRule.selectCondition.reduceOrPresent ===
                                2) ||
                              ((promotionRule.selectCondition.isSpecial ===
                                undefined ||
                                promotionRule.selectCondition.isSpecial ===
                                  0) &&
                                promotionRule.selectCondition
                                  .reduceOrPresent === 2))
                          "
                        >
                          <el-button
                            class="mini-present-btn"
                            :loading="selectPresentLoading"
                            @click="selectPresent(promotionRule)"
                            >{{ $t("ShopCarts.ViewGifts") }}</el-button
                          >
                        </div>
                      </el-col>
                    </el-row>
                    <el-table
                      border
                      :data="promotionRule.goods"
                      ref="table"
                      header-row-class-name="header-row"
                      class="tableCenter orderGoods"
                      style="text-align: center"
                    >
                      <el-table-column
                        align="center"
                        :label="$t('versionsps.GoodsCode')"
                        prop="itemCode"
                      ></el-table-column>
                      <el-table-column
                        align="center"
                        :label="$t('ShopCarts.ItemName')"
                        prop="itemName"
                      ></el-table-column>
                      <el-table-column
                        align="center"
                        :label="$t('ShopCarts.Quantity')"
                        prop="actualOrderCount"
                      >
                        <template slot-scope="scope">
                          <!-- {{formData.onWayAttendEventFlag === 1?scope.row.actualOrderCount:scope.row.inWarehouseCount}} -->
                          {{
                            promotionRule.onWayAttendEventFlag === 1
                              ? scope.row.actualOrderCount
                              : scope.row.inWarehouseCount
                          }}
                        </template>
                      </el-table-column>
                      <el-table-column
                        align="center"
                        :label="$t('P.Price')"
                        prop="distributorPrice"
                      >
                        <template slot-scope="scope">
                          <i class="asmd">{{
                            currencyType === "CNY" ? "￥" : "$"
                          }}</i
                          >{{ scope.row.distributorPrice | NumFormat }}
                        </template>
                      </el-table-column>
                      <el-table-column
                        align="center"
                        :label="$t('versionsps.DiscountPrice')"
                        prop="actualPrice"
                      >
                        <template slot-scope="scope">
                          <i class="asmd">{{
                            currencyType === "CNY" ? "￥" : "$"
                          }}</i
                          >{{ scope.row.actualPrice | NumFormat }}
                        </template>
                      </el-table-column>
                    </el-table>
                  </div>
                </div>
              </div>
            </el-dialog>

            <el-dialog
              :visible="selectPresentShow"
              :before-close="cancelPresentChange"
            >
              <div v-loading="selectPresentLoading">
                <div class="pomotion-present" v-if="presentRule !== null">
                  <div class="half-left">
                    <span class="present-label">{{
                      this.presentRule.label
                    }}</span>
                  </div>
                  <div class="half-right">
                    <div style="margin-left: 20px; font-weight: 500">
                      {{ $t("versionsps.MaxSelect") }}{{ presentTotal
                      }}{{ $t("P.Pieces") }}，{{ $t("P.HaveSelected")
                      }}{{ selectCount }}{{ $t("P.Pieces") }}
                    </div>
                  </div>
                  <div
                    style="
                      font: 0px/0px sans-serif;
                      clear: both;
                      display: block;
                    "
                  ></div>
                </div>
                <el-table
                  :data="presentRule.presentGoodss"
                  :show-header="true"
                  ref="table"
                  header-row-class-name="header-row"
                  class="tableCenter orderGoods"
                  style="text-align: center"
                >
                  <el-table-column
                    align="center"
                    :label="$t('versionsps.GoodsCode')"
                    prop="itemCode"
                  ></el-table-column>
                  <el-table-column
                    align="center"
                    :label="$t('ShopCarts.ItemName')"
                    prop="itemName"
                  ></el-table-column>
                  <el-table-column
                    align="center"
                    :label="$t('versionsps.TotalQuantityOrder')"
                    :min-width="120"
                  >
                    <template slot-scope="scope">
                      <div class="batch_input_number">
                        <span
                          class="batch_minus"
                          @click="handelPresentMinus(scope.row)"
                        >
                          -
                        </span>
                        <div>
                          <!-- checkhHandleFlag 1 已下单 2 未下单 -->
                          <input
                            class="batch_input1"
                            @blur="handelPresentNum(scope.row)"
                            type="text"
                            v-model="scope.row.itemCount"
                            maxlength="8"
                            onkeyup="this.value=this.value.replace(/\D/g,'')"
                            :disabled="disableIsShow"
                          />
                        </div>
                        <span
                          class="batch_plusSign"
                          @click="handelPresentPlusSing(scope.row)"
                          >+</span
                        >
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column
                    align="center"
                    :label="$t('versionsps.SurplusOptionQuan')"
                    prop="presentCount"
                  ></el-table-column>
                  <el-table-column
                    align="center"
                    :label="$t('ShopCarts.InventoryQuantity')"
                    prop="numInWarehouse"
                  ></el-table-column>
                </el-table>
                <div class="foot-btn" style="text-align: center">
                  <el-button
                    :loading="changePresentLoading"
                    class="mini-search-btn"
                    @click="submitPresentChange"
                    >{{ $t("P.OK") }}</el-button
                  >
                  <el-button size="mini" @click="cancelPresentChange">{{
                    $t("P.Cancel")
                  }}</el-button>
                </div>
              </div>
            </el-dialog>
          </div>
        </div>
      </div>
    </div>
    <cutFilmMachineDialog
      :showOpen="showOpenCut"
      :select="true"
      @close="closeUserProtocol(2)"
    ></cutFilmMachineDialog>
    <proCustomizeDialog
      :showOpen="showOpenPro"
      :select="true"
      @close="closeUserProtocol(3)"
    ></proCustomizeDialog>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import Left from "@/components/Left.vue";
import cutFilmMachineDialog from "@/components/cutFilmMachineDialog";
import proCustomizeDialog from "@/components/proCustomizeDialog";
import { formatDate, orderHint } from "@/assets/js/common.js";

import {
  orderTypeList,
  logisticsList,
  getImportOrderDetail,
  updateImportOrderDetail,
  ordersImportOrder,
  regionList,
} from "@/apiService/api";

export default {
  components: {
    Header,
    Left,
    cutFilmMachineDialog,
    proCustomizeDialog,
  },
  data() {
    // 手机正则验证
    const validatePhone = (rule, value, callback) => {
      if (
        /^(13[0-9]|14[5-9]|15[012356789]|166|17[0-8]|18[0-9]|19[8-9])[0-9]{8}$/.test(
          value
        ) === false
      ) {
        callback(new Error(this.$t("versionsps.InputTelMsg")));
      } else {
        callback();
      }
    };
    return {
      id: this.$route.query.id,
      userState: 2,
      moqCount: false,
      cityShow: false,
      townShow: false,
      invoiceShow: false,
      noHandle: false,
      orderHandle: false,
      disableIsShow: false,
      disabledShow: false,
      changePromotionShow: false,
      isChangePromotion: false,
      saveWin: false, // 提交订单时保存订单是否成功
      data: {
        importOrderId: "",
      },
      loading: "",
      loading2: "",
      loading1: "",
      loading3: false,
      selectPresentLoading: false,
      selectPresentShow: false,
      promotionShow: false,
      allIds: [],
      modifyGoodsList: [],
      maxCount: "",
      orderShow: "",
      orderTypeList: [], // 订单类型arr
      orderDistriList: [], // 配送方式arr
      payWayList: [],
      counts: [],
      arr1: [],
      orderDeleteIds: "", // 货品删除ids
      currencyType: "CNY",
      goods: [
        {
          rules: [],
        },
      ],
      ruleId: "",
      rule: "",
      Num: [],
      regionParams: {
        // 用来请求region数据  地区信息，省份
        parentId: 37,
        size: 300,
        page: 1,
      },
      promotionIds: {
        ruleIds: [],
      },
      formData: {
        orderTypeValue: "", // 订单类型
        distributionId: "", // 配送方式
        invoiceType: "", // 发票类型
        payWay: "", // 支付方式
        isInvoice: 0, // 是否开票
        provinceId: "", // 省份id
        cityId: "", // 城市id
        districtId: "", // 区id
        importOrderId: "",
        manufactor: "", // 工厂
        zipCode: "", // 邮编
      },
      rules: {
        // 必填输入提示
        payWay: [
          {
            required: true,
            message: this.$t("versionsps.ChooseModeOfPayment"),
            trigger: "change",
          },
        ],
        distributionId: [
          {
            required: true,
            message: this.$t("versionsps.ChooseModeOfDistribution"),
            trigger: "change",
          },
        ],
        orderTypeValue: [
          {
            required: true,
            message: this.$t("versionsps.ChooseOrderType"),
            trigger: "change",
          },
        ],
        userName: [
          {
            required: true,
            message: this.$t("versionsps.InputGoodsName"),
            trigger: "blur",
          },
        ],
        provinceId: [
          {
            required: true,
            message: this.$t("versionsps.ChooseRegion"),
            trigger: "change",
          },
        ],
        address: [
          {
            required: true,
            message: this.$t("versionsps.InputStreetAddress"),
            trigger: "blur",
          },
        ],
        mobile: [
          {
            required: true,
            message: this.$t("versionsps.InputTelMsg"),
            trigger: "blur",
          },
          { validator: validatePhone },
        ],
      },
      RegionForChose: {
        // 被选region
        provinceId: [],
        cityId: [],
        districtId: [],
      },
      promotionRules: [],
      gradeRule: null,
      selectCount: 0,
      presentTotal: 0,
      presentRule: {
        presentGoodss: [],
      },
      useRange: "", // 适用范围 1-普通商品 2-定制商品
      isPresentChange: false,
      changePresentLoading: false,
      changeCount: false,
      isError: false,
      userTradeType: 1, // 1为现款支付，2为期间结算
      showOpenCut: false, // 膜切机弹框
      showOpenPro: false, // 定制弹框
      brandIds: [], // 当前提交商品品牌，判断协议弹框
    };
  },
  created() {
    this.orderType(); // 订单详情
    // this.getUserTradeType(); // 获取结算方式
    if (this.$route.query.handleFlag === 1) {
      // 1、已下单 2、未下单
      this.editorShow = true;
      this.disableIsShow = true;
      this.orderHandle = true;
      this.noHandle = false;
    } else if (this.$route.query.handleFlag === 2) {
      this.noHandle = true;
      this.orderHandle = false;
      this.disableIsShow = false;
      this.disabledShow = false;
    }
    // 获取省份
    regionList(this.regionParams).then((res) => {
      this.RegionForChose.provinceId = res.data.list;
    });
  },
  filters: {
    formatDate(time) {
      var date = new Date(time);
      return formatDate(date, "yyyy-MM-dd hh:mm:ss");
    },
    NumFormat(value) {
      if (!value) return "0.00";
      var intPart = parseInt(value); // 获取整数部分
      value = value.toString();
      // intPart = intPart.substring(0, intPart.lastIndexOf('.'))
      var intPartFormat = intPart
        .toString()
        .replace(/(\d)(?=(?:\d{3})+$)/g, "$1,"); // 将整数部分逢三一断
      var floatPart = ".00"; // 预定义小数部分
      var value2Array = value.split(".");
      // =2表示数据有小数位
      if (value2Array.length === 2) {
        floatPart = value2Array[1].toString(); // 拿到小数部分
        if (floatPart.length === 1) {
          // 补0,实际上用不着
          return intPartFormat + "." + floatPart + "0";
        } else {
          return intPartFormat + "." + floatPart;
        }
      } else {
        return intPartFormat + floatPart;
      }
    },
  },
  methods: {
    // 获取用户预存款方式
    getUserTradeType() {
      var myDate = new Date();
      this.$api
        .get(
          this,
          "user/u/order/userTradeType?" +
            myDate.getMinutes() +
            myDate.getSeconds()
        )
        .then((res) => {
          if (res.code === 0) {
            // 1为现款支付，2为期间结算
            this.userTradeType = res.userTradeType;
            // if (Number(this.goodsType === 2)) { // 定制商品
            //   this.payWay = 6 // 网银支付
            // } else {
            // }
            if (this.$i18n.locale === "zh") {
              if (this.userTradeType === 2) {
                this.payWay = 3; // 区间结算
              } else {
                this.payWay = 4; // 银行转账
              }
            } else {
              this.payWay = 4; // 银行转账
            }
          }
        });
    },
    diyPicture(row) {
      this.$router.push({
        name: "CustomizeDiy",
        query: {
          url: window.location.href,
          pictureId: row.newDiy.pictureId,
          materialId: row.newDiy.materialId,
          brandId: row.newDiy.brandId,
          modelId: row.newDiy.modelId,
          userId: window.localStorage.getItem("userId"),
          goodsId: row.goodsId,
        },
      });
    },
    orderType() {
      this.loading2 = true;
      orderTypeList({ page: 1, size: 1000 }).then((data) => {
        this.orderTypeList = data.data.list;
        this.orderImportOrder(); // 订单详情
        this.orderDistribution();
      });
    },
    orderDistribution() {
      // 配送方式
      return logisticsList({
        page: 1,
        size: 100,
        manufactor: this.formData.manufactor,
        countryId: 37,
        provinceId: this.formData.provinceId,
        cityId: this.formData.cityId,
        districtId: this.formData.districtId,
        enable: 1,
        // useRange: this.useRange,
      }).then((res) => {
        this.orderDistriList = res.data.list;
        let arr = [];
        this.orderDistriList.forEach((item, index) => {
          arr.push(item.id);
        });
        if (arr.indexOf(this.formData.distributionId) < 0) {
          this.formData.distributionId = "";
        }
      });
    },
    orderImportOrder() {
      // 订单详情
      return getImportOrderDetail({ id: this.id }).then((res) => {
        this.promotionIds.ruleIds = [];
        res.data.orderGoods.forEach((item) => {
          // 组合活动规则Id
          this.useRange = item.goodsType;
          this.counts.push(item.actualOrderCount);
          if (
            item.ruleId !== undefined &&
            item.ruleId !== null &&
            this.promotionIds.ruleIds.indexOf(item.ruleId) < 0
          ) {
            this.promotionIds.ruleIds.push(item.ruleId);
            if (
              item.orderRuleId !== undefined &&
              item.orderRuleId !== null &&
              this.promotionIds.ruleIds.indexOf(item.orderRuleId) < 0
            ) {
              this.promotionIds.ruleIds.push(item.orderRuleId);
            }
          } else if (
            item.gradeDiscountId !== undefined &&
            item.gradeDiscountId !== null
          ) {
            // 商品等级折扣活动Id
            this.promotionIds.gradeRuleId = item.gradeDiscountId;
          }
          // if (item.rules.length > 0) {
          //   item.rules.forEach((raw) => {
          //     raw.isGrade = false;
          //   });
          // }
          // if (item.gradeRule !== undefined && item.gradeRule !== null) {
          //   item.rules.push({
          //     id: item.gradeRule.id,
          //     label: item.gradeRule.policyName,
          //     isGrade: true,
          //     description: item.gradeRule.description,
          //     discount: item.gradeRule.discount,
          //     moneyOrCount: item.gradeRule.moneyOrCount,
          //     oneBuyMoney: item.gradeRule.oneBuyMoney,
          //     discountBeforeAfter: item.gradeRule.discountBeforeAfter,
          //     oneBuyCount: item.gradeRule.oneBuyCount,
          //   });
          // }
        });
        this.formData = res.data;
        this.currencyType = this.formData.currencyType;
        this.promotionRules.forEach((item) => {
          item.goods = this.formData.orderGoods;
        });
        if (
          res.data.provinceId === 0 &&
          res.data.cityId === 0 &&
          res.data.districtId === 0
        ) {
          this.formData.provinceId = "";
          this.formData.cityId = "";
          this.formData.districtId = "";
        } else {
          if (res.data.provinceId !== 0 || res.data.provinceId !== "") {
            if (res.data.cityId === 0) {
              this.formData.cityId = "";
            }
            this.cityShow = true;
            regionList({
              page: 1,
              size: 200,
              parentId: this.formData.provinceId,
            }).then((res) => {
              res.data.list.forEach((item) => {
                if (this.formData.cityId === item.id) {
                  this.RegionForChose.cityId = res.data.list;
                  this.townShow = true;
                  regionList({
                    page: 1,
                    size: 200,
                    parentId: this.formData.cityId,
                  }).then((res) => {
                    if (this.formData.districtId === 0) {
                      this.townShow = false;
                      this.formData.districtId = 0;
                    } else {
                      res.data.list.forEach((item) => {
                        if (this.formData.districtId === item.id) {
                          this.RegionForChose.districtId = res.data.list;
                        }
                      });
                    }
                  });
                }
              });
            });
          }
        }
        this.isChangePromotion = false;
        res.code === 0 ? (this.loading2 = false) : (this.loading2 = false);
        if (res.data.distributionId === 0) {
          // 配送方式
          this.formData.distributionId = "";
        }
        if (res.data.invoiceType === 0) {
          // 发票类型
          this.invoiceShow = false;
          this.formData.invoiceType = "";
        }
        if (res.data.payWay === 0) {
          // 支付方式
          this.formData.payWay = "";
        }
        if (this.checkhHandleFlag === 1) {
          // 订单号拆分
          this.allIds = res.data.orderId.split(",");
        }
        if (this.isPresentChange && res.code === 0) {
          this.isPresentChange = false;
          this.lookPromotion();
        } else if (this.isPresentChange) {
          this.isPresentChange = false;
          this.selectPresentLoading = false;
        }
      });
    },
    // orderTypeValueChange (val) { // ..订单类型变化
    //   if (val === 'XSDD10_SYS') { // ..MTO订单
    //     this.$message.warning('订单类型已变更为MTO订单，可享有的活动可能发生变化')
    //   } else if (val === 'XSDD04_SYS') { // ..直运订单
    //     this.$message.warning('订单类型已变更为直运订单，可享有的活动可能发生变化')
    //   } else {
    //     this.$message.warning('订单类型已变更，可享有的活动可能发生变化')
    //   }
    // },
    provinceIdChange(event) {
      // 省
      regionList({
        page: 1,
        size: 200,
        parentId: event,
      }).then((res) => {
        if (res.success) {
          this.cityShow = true;
          this.RegionForChose.cityId = res.data.list;
          this.formData.cityId = res.data.list[0].id;
          if (res.data.list[0].haveNext === 1) {
            regionList({
              page: 1,
              size: 200,
              parentId: this.formData.cityId,
            }).then((res) => {
              this.RegionForChose.districtId = res.data.list;
              this.formData.districtId = this.RegionForChose.districtId[1].id;
            });
            this.townShow = true;
          } else {
            this.formData.districtId = 0;
            this.townShow = false;
          }
        }
      });
    },
    cityIdChange(event) {
      // 市
      regionList({
        page: 1,
        size: 200,
        parentId: event,
      }).then((res) => {
        if (res.data.list.length === 0) {
          this.townShow = false;
          this.formData.districtId = 0;
        } else {
          this.RegionForChose.districtId = res.data.list;
          this.formData.districtId = this.RegionForChose.districtId[1].id;
        }
      });
    },
    handleDelete(index, row) {
      // 商品信息删除操作
      this.arr1.push(row.id);
      this.orderDeleteIds = this.arr1;
      this.formData.orderGoods.splice(index, 1);
      this.changeCount = true;
    },
    handleSave(formData) {
      // 保存操作
      this.saveWin = false;
      this.saveOrderForm();
    },
    handleSubmit() {
      // 提交操作
      if (this.formData.orderGoods.length !== 0) {
        for (let i = 0; i < this.formData.orderGoods.length; i++) {
          let item = this.formData.orderGoods[i];
          this.brandIds.push(item.brandId);
          if (
            item.goodsType === 3 &&
            (item.newDiy.image === undefined ||
              item.newDiy.image === null ||
              item.newDiy.image === "")
          ) {
            // 新定制,定制图片为空
            this.$message.error(
              this.$t("versionsps.diyPicture1") +
                (i + 1) +
                this.$t("versionsps.diyPicture2")
            );
            return;
          }
        }
      }
      this.saveWin = true;
      this.saveOrderForm();
    },
    // 协议弹框判断
    getProtocolStatus() {
      this.$api
        .post(this, "user/brand/agreement/status", { brandIds: this.brandIds })
        .then((res) => {
          if (res.code === 0) {
            let brandarr = [];
            // 是否存在膜切机
            res.distributorBrandAgreementList.forEach((item) => {
              // status 2 未弹出
              brandarr.push(item.brandId);
              // 7-膜切机  33-定制（正式）42-定制（测试）
              if (item.brandId === 7 && item.status === 2) {
                // 膜切机
                this.showOpenCut = true;
              } else if (item.brandId === 42 && item.status === 2) {
                // 定制
                this.showOpenPro = true;
              }
            });
            if (brandarr.indexOf(7) === -1 && this.brandIds.indexOf(7) > -1) {
              // 膜切机
              this.showOpenCut = true;
            } else if (
              brandarr.indexOf(42) === -1 &&
              this.brandIds.indexOf(42) > -1
            ) {
              // 膜切机
              this.showOpenPro = true;
            }
            if (!this.showOpenPro && !this.showOpenCut) {
              this.submitForm(); // ..保存成功时帮用户进行提交操作，便于后台做取数据
            }
          }
        });
    },
    // 关闭协议
    closeUserProtocol(value) {
      this.$api
        .post(this, "user/brand/agreement", {
          status: 1,
          brandIds: this.brandIds,
        })
        .then((res) => {
          if (res.code === 0) {
            if (value === 2) {
              // 膜切机
              this.showOpenCut = false;
            } else if (value === 3) {
              // 定制
              this.showOpenPro = false;
            }
            this.submitForm(); // ..保存成功时帮用户进行提交操作，便于后台做取数据
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    saveOrderForm() {
      // 保存订单操作
      if (this.isError) {
        return;
      }
      const formArr = ["formData1", "formData2", "formData3"]; // 三个form表单的ref
      var resultArr = []; // 用来接受返回结果的数组
      var _self = this;
      function checkForm(formName) {
        // 封装验证表单的函数
        var result = new Promise(function (resolve, reject) {
          _self.$refs[formName].validate((valid) => {
            if (valid) {
              resolve();
            } else {
              reject();
            }
          });
        });
        resultArr.push(result); // push 得到promise的结果
      }
      formArr.forEach((item) => {
        // 根据表单的ref校验
        checkForm(item);
      });
      Promise.all(resultArr)
        .then((values) => {
          // XSDD10_SYS MTO订单
          // XSDD04_SYS 直运订单
          // XSDD02_SYS 寄售订单
          // advanceSaleFlag 1 MTO需判断是否为1，是否支持预售
          let moqCount = 0;
          if (this.formData.orderGoods.length === 0) {
            this.$message.error(this.$t("versionsps.OrderRemind"));
            this.saveWin = false;
            return;
          }
          for (let i = 0; i < this.formData.orderGoods.length; i++) {
            if (
              this.formData.orderGoods[i].actualOrderCount === undefined ||
              this.formData.orderGoods[i].actualOrderCount === 0 ||
              this.formData.orderGoods[i].actualOrderCount <= 0
            ) {
              this.$message.error(
                this.formData.orderGoods[i].itemName +
                  this.$t("versionsps.QuantityRemind")
              );
              this.saveWin = false;
              return;
            } else if (
              this.formData.orderGoods[i].goodsType === 1 &&
              this.formData.orderGoods[i].actualOrderCount >
                this.formData.orderGoods[i].maxCount
            ) {
              this.$message.warning(
                this.formData.orderGoods[i].itemName +
                  this.$t("P.ShortInventory") +
                  "," +
                  this.$t("versionsps.maxAvaliableQuan") +
                  this.formData.orderGoods[i].maxCount
              );
              this.saveWin = false;
              return;
            }
          }

          if (!moqCount) {
            // this.getProtocolStatus();
            this.submitForm();
            moqCount = 0;
          }
          this.changeCount = false;
        })
        .catch((_) => {
          this.saveWin = false;
          console.log(this.$t("versionsps.UserInfoRemind"));
        });
    },
    submitForm() {
      // 保存订单请求
      this.disabledShow = true;
      if (!this.saveWin) {
        // 首次未保存做提交动作不做保存loading（保存）
        this.loading = this.$loading({
          lock: true,
          text: this.$t("versionsps.SaveOrdering"),
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, 0.7)",
        });
      } else if (this.saveWin) {
        // ..提交
        this.loading1 = this.$loading({
          lock: true,
          text: this.$t("versionsps.SubmitOrdering"),
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, 0.7)",
        });
      }
      if (
        this.formData.invoiceType === "请选择" ||
        this.formData.invoiceType === "Select"
      ) {
        // 发票类型
        this.formData.invoiceType = 0;
      }
      this.modifyGoodsList = this.formData.orderGoods.map((item) => ({
        importOderGoodsId: item.id,
        actualOrderCount: item.actualOrderCount,
      }));
      // 导入订单修改
      updateImportOrderDetail({
        id: parseInt(this.$route.query.id), // 导入订单id
        orderTypeValue: this.formData.orderTypeValue, // 订单类型
        countryId: 37, // 国家
        provinceId: this.formData.provinceId, // 省份id
        cityId: this.formData.cityId, // 市id
        districtId: this.formData.districtId, // 区/县id
        address: this.formData.address, // 地址
        userName: this.formData.userName, // 收货人
        mobile: this.formData.mobile, // 手机
        payWay: this.formData.payWay, // 支付方式
        distributionId: this.formData.distributionId, // 配送方式id
        isInvoice: this.formData.isInvoice, // 是否开具发票 0.否，1.是
        invoiceType: this.formData.invoiceType, // 发票类型 1.普通 2.增值税发票
        remark: this.formData.remark, // 配送方式id
        goodsDelIds: this.orderDeleteIds.toString(), // 货品删除ids，多个以','分隔
        modifyGoodsList: this.modifyGoodsList,
      }).then((res) => {
        if (res.success) {
          //   Promise.all([this.$api.get(this, 'user/u/user/importOrder', { id: this.$route.query.id }).then(res => {
          //     this.promotionIds.ruleIds = []
          //     res.data.orderGoods.forEach(item => { // 组合活动规则Id
          //       if (item.ruleId !== undefined && item.ruleId !== null && this.promotionIds.ruleIds.indexOf(item.ruleId) < 0) {
          //         this.promotionIds.ruleIds.push(item.ruleId)
          //         if (item.orderRuleId !== undefined && item.orderRuleId !== null && this.promotionIds.ruleIds.indexOf(item.orderRuleId) < 0) {
          //           this.promotionIds.ruleIds.push(item.orderRuleId)
          //         }
          //       } else if (item.gradeDiscountId !== undefined && item.gradeDiscountId !== null) { // 商品等级折扣活动Id
          //         this.promotionIds.gradeRuleId = item.gradeDiscountId
          //       }
          //       if (item.rules.length > 0) {
          //         item.rules.forEach(raw => {
          //           raw.isGrade = false
          //         })
          //       }
          //       if (item.gradeRule !== undefined && item.gradeRule !== null) {
          //         item.rules.push({
          //           id: item.gradeRule.id,
          //           label: item.gradeRule.policyName,
          //           isGrade: true,
          //           description: item.gradeRule.description,
          //           discount: item.gradeRule.discount,
          //           moneyOrCount: item.gradeRule.moneyOrCount,
          //           oneBuyMoney: item.gradeRule.oneBuyMoney,
          //           discountBeforeAfter: item.gradeRule.discountBeforeAfter,
          //           oneBuyCount: item.gradeRule.oneBuyCount
          //         })
          //       }
          //   })
          //   this.formData = res.data
          //   this.promotionRules.forEach(item => {
          //     item.goods = this.formData.orderGoods
          //   })
          //   if (res.data.provinceId === 0 && res.data.cityId === 0 && res.data.districtId === 0) {
          //     this.formData.provinceId = ''
          //     this.formData.cityId = ''
          //     this.formData.districtId = ''
          //   } else {
          //     if (res.data.provinceId !== 0 || res.data.provinceId !== '') {
          //       this.cityShow = true
          //       this.$api.get(this, 'region', { parentId: this.formData.provinceId }).then(res => {
          //         res.regions.forEach(item => {
          //           if (this.formData.cityId === item.id) {
          //             this.RegionForChose.cityId = res.regions
          //             this.townShow = true
          //             this.$api.get(this, 'region', { parentId: this.formData.cityId }).then(res => {
          //               if (this.formData.districtId === 0) {
          //                 this.townShow = false
          //                 this.formData.districtId = 0
          //               } else {
          //                 res.regions.forEach(item => {
          //                   if (this.formData.districtId === item.id) {
          //                     this.RegionForChose.districtId = res.regions
          //                   }
          //                 })
          //               }
          //             })
          //           }
          //         })
          //       })
          //     }
          //   }
          //   this.isChangePromotion = false
          //   res.code === 0 ? this.loading2 = false : this.loading2 = false
          //   // TODO
          //   if (res.data.distributionId === 0) { // 配送方式
          //     this.formData.distributionId = ''
          //   }
          //   if (res.data.invoiceType === 0) { // 发票类型
          //     this.invoiceShow = false
          //     this.formData.invoiceType = ''
          //   }
          //   if (res.data.payWay === 0) { // 支付方式
          //     this.formData.payWay = ''
          //   }
          //   if (this.checkhHandleFlag === 1) { // 订单号拆分
          //     this.allIds = res.data.orderId.split(',')
          //   }
          //   if (this.isPresentChange && res.code === 0) {
          //     this.isPresentChange = false
          //     this.lookPromotion()
          //   } else if (this.isPresentChange) {
          //     this.isPresentChange = false
          //     this.selectPresentLoading = false
          //   }
          // })]).then(values => {
          //       if (!this.saveWin) { // 首次未保存做提交动作不提示保存成功 （保存）
          //         this.$message.success({
          //           message: this.$t('versionsps.SumbitOrderUpdate'),
          //           duration: 3 * 1000
          //         })
          //         this.loading.close()
          //         this.disabledShow = false
          //       } else if (this.saveWin) { // ..提交
          //         this.importOrderForm()
          //       }
          //   })
          if (!this.saveWin) {
            // 首次未保存做提交动作不提示保存成功 （保存）
            this.$message.success({
              message: this.$t("versionsps.SumbitOrderUpdate"),
              duration: 3 * 1000,
            });
            this.loading.close();
            this.disabledShow = false;
          } else if (this.saveWin) {
            // ..提交
            this.importOrderForm();
          }
        } else {
          if (this.saveWin) {
            this.loading1.close();
            this.disabledShow = false;
          } else {
            this.loading.close();
            this.disabledShow = false;
          }
        }
      });
    },
    importOrderForm() {
      // 提交订单请求
      // 订单结算提示
      orderHint().then((res) => {
        if (res) {
          ordersImportOrder({
            ids: parseInt(this.$route.query.id),
          }).then((res) => {
            if (res.success) {
              this.$message.success({
                message: this.$t("versionsps.SumbitOrderSuc"),
                duration: 3 * 1000,
                onClose: () => {},
              });
              this.disabledShow = true;
              this.loading1.close();
              this.$router.push("/BatchImport");
            } else {
              this.loading1.close();
              this.disabledShow = false;
              this.saveWin = false;
            }
          });
        } else {
          this.loading1.close();
          this.disabledShow = false;
          this.saveWin = false;
        }
      });
    },
    renderHeader(h, col) {
      // 页面右上角的规格值
      switch (col.$index) {
        case 5:
          return h("div", [
            h("span", "价格折扣"),
            h(
              "el-tooltip",
              {
                props: {
                  content: "价格折扣为货品在该订单中优惠的金额。",
                  effect: "light",
                  placement: "top",
                },
              },
              [h("span", { class: "el-icon-question question-color" })]
            ),
          ]);
          break;
        case 7:
          return h("div", [
            h("span", "缺货数量"),
            h(
              "el-tooltip",
              {
                props: {
                  content: "下单时会自动扣除缺货数量，点击右上角可导出缺货清单",
                  effect: "light",
                  placement: "top",
                },
              },
              [h("span", { class: "el-icon-question question-color" })]
            ),
          ]);
          break;
      }
    },
    saleStatusFormatter(stateCode) {
      // 导入订单详情上下架状态
      switch (stateCode) {
        case 1:
          return this.$t("versionsps.SoldOut");
        case 2:
          return this.$t("versionsps.SoldOut");
        case 3:
          return this.$t("versionsps.InTheShelf");
        case 4:
          return this.$t("versionsps.InTheShelf");
        default:
          return this.$t("versionsps.StateLess");
      }
    },
    exportExcel() {
      // 导出缺货清单
      this.$api
        .exportData(this, "user/u/p/importOrder/orderDownLoad", {
          importOrderId: this.formData.importOrderId,
          orderFlag: 2,
        })
        .then((res) => {
          const content = res;
          const blob = new Blob([content], {
            type: "application/ms-excel",
          });
          const url = window.URL.createObjectURL(blob);
          if ("download" in document.createElement("a")) {
            const link = document.createElement("a");
            link.style.display = "none";
            link.href = url;
            link.setAttribute("download", "excel.xls");
            document.body.appendChild(link);
            link.click();
          } else {
            navigator.msSaveBlob(blob, "excel.xls");
          }
        });
    },
    onEdit(item) {
      // 订单号查看操作
      this.$router.push({ name: "WaitPayment", query: { order_id: item } });
    },
    cancelLook() {
      this.promotionShow = false;
    },
    selectPresent(rule) {
      this.presentRule = rule;
      this.selectPresentLoading = true;
      this.selectPresentShow = true;
      let selectRatio = 0.0;
      if (this.presentRule.target !== 1 && this.presentRule.isCompose === 1) {
        // 组合活动
        this.presentRule.selectCondition.groupConditions.forEach(
          (groupCondition) => {
            if (
              this.presentRule.moneyOrCount === 1 &&
              (selectRatio === 0 ||
                selectRatio >
                  groupCondition.totalMoney / groupCondition.oneBuyMoney)
            ) {
              if (
                this.presentRule.selectCondition.reductionPresentIsAdd === 1
              ) {
                // 累计且叠加
                selectRatio =
                  groupCondition.totalMoney / groupCondition.oneBuyMoney;
              } else {
                selectRatio = 1;
              }
            } else if (
              this.presentRule.moneyOrCount === 2 &&
              (selectRatio === 0 ||
                selectRatio >
                  groupCondition.totalCount / groupCondition.oneBuyCount)
            ) {
              if (
                this.presentRule.selectCondition.reductionPresentIsAdd === 1
              ) {
                // 累计且叠加
                selectRatio =
                  groupCondition.totalCount / groupCondition.oneBuyCount;
              } else {
                selectRatio = 1;
              }
            }
          }
        );
      } else {
        if (this.presentRule.moneyOrCount === 1) {
          // 金额
          if (this.presentRule.selectCondition.reductionPresentIsAdd === 1) {
            // 累计且叠加
            selectRatio =
              this.presentRule.selectCondition.totalMoney /
              this.presentRule.selectCondition.oneBuyMoney;
          } else {
            // 不叠加
            if (
              this.presentRule.target === 1 ||
              this.presentRule.isAddUp === 1
            ) {
              selectRatio = 1;
            } else {
              this.presentRule.goods.forEach((item) => {
                if (
                  item.itemType === undefined ||
                  item.itemType === null ||
                  item.itemType !== 2
                ) {
                  selectRatio += 1;
                }
              });
            }
          }
        } else {
          // 数量
          if (this.presentRule.selectCondition.reductionPresentIsAdd === 1) {
            // 累计且叠加
            selectRatio =
              this.presentRule.selectCondition.totalCount /
              this.presentRule.selectCondition.oneBuyCount;
          } else {
            // 不叠加
            if (
              this.presentRule.target === 1 ||
              this.presentRule.isAddUp === 1
            ) {
              selectRatio = 1;
            } else {
              this.presentRule.goods.forEach((item) => {
                if (
                  item.itemType === undefined ||
                  item.itemType === null ||
                  item.itemType !== 2
                ) {
                  selectRatio += 1;
                }
              });
            }
          }
        }
      }
      this.presentTotal = Math.floor(
        this.presentRule.selectCondition.presentCount * selectRatio
      ); // 赠送总数
      this.presentRule.presentGoodss = []; // 赠品列表
      this.selectCount = 0; // 已选赠品总数
      this.$api
        .get(this, "user/u/po/marketing/promotion/getPresentGoods", {
          conditionId: this.presentRule.selectCondition.id,
          distributorId: this.formData.distributorId,
        })
        .then((res) => {
          // 货品赠品列表
          if (res.code === 0) {
            res.goods.forEach((item) => {
              let b = true;
              this.presentRule.goods.forEach((goodsItem) => {
                if (
                  goodsItem.itemType === 2 &&
                  item.itemId === goodsItem.itemId
                ) {
                  // 已选赠品
                  this.selectCount = this.selectCount + goodsItem.itemCount;
                  item.itemCount = goodsItem.itemCount;
                  b = false;
                  this.presentRule.presentGoodss.push(item);
                }
              });
              if (b) {
                item.itemCount = 0;
                this.presentRule.presentGoodss.push(item);
              }
            });
          }
          this.selectPresentLoading = false;
        });
    },
    lookPromotion() {
      if (this.changeCount) {
        this.$message.info({
          message: this.$t("versionsps.Fresh"),
          duration: 3 * 1000,
        });
        return;
      }
      if (this.isChangePromotion) {
        this.$message.info({
          message: this.$t("versionsps.SwitchFresh"),
          duration: 3 * 1000,
        });
        return;
      }
      this.loading3 = true;
      this.promotionShow = true;
      this.gradeRule = null;
      this.promotionRules = [];
      this.$api
        .post(this, "user/u/po/marketing/promotion/lookRule", this.promotionIds)
        .then((res) => {
          if (res.code === 0) {
            if (
              res.rules !== undefined &&
              res.rules !== null &&
              res.rules.length > 0
            ) {
              res.rules.forEach((rule) => {
                rule.goods = [];
                let totalCount = 0.0;
                let totalMoney = 0.0;
                let totalCounts = [];
                let totalMoneys = [];
                if (rule.target !== 1 && rule.isCompose === 1) {
                  // 组合情况
                  rule.conditions[0].groupConditions.forEach(
                    (groupCondition) => {
                      totalCounts.push(0.0);
                      totalMoneys.push(0.0);
                    }
                  );
                }
                this.formData.orderGoods.forEach((item) => {
                  // 组装活动规则对应的货品列表
                  if (
                    (rule.target === 1 &&
                      item.orderRuleId !== undefined &&
                      item.orderRuleId !== null &&
                      item.orderRuleId === rule.id) ||
                    (rule.target !== 1 &&
                      item.ruleId !== undefined &&
                      item.ruleId !== null &&
                      item.ruleId === rule.id)
                  ) {
                    if (
                      item.itemType === undefined ||
                      item.itemType === null ||
                      item.itemType !== 2
                    ) {
                      if (rule.target !== 1 && rule.isCompose === 1) {
                        // 组合情况
                        rule.conditions[0].groupConditions.forEach(
                          (groupCondition, index) => {
                            groupCondition.goodsItemIds =
                              groupCondition.goodsItemIds instanceof Array
                                ? groupCondition.goodsItemIds
                                : groupCondition.goodsItemIds.split(",");
                            if (
                              (rule.target === 2 &&
                                groupCondition.goodsItemIds.indexOf(
                                  String(item.goodsId)
                                ) >= 0) ||
                              (rule.target === 3 &&
                                groupCondition.goodsItemIds.indexOf(
                                  String(item.itemId)
                                ) >= 0)
                            ) {
                              // 商品
                              if (
                                rule.onWayAttendEventFlag === 1 ||
                                item.inWarehouseCount === undefined ||
                                item.inWarehouseCount === null ||
                                item.inWarehouseCount === 0
                              ) {
                                // 在途也参与活动
                                totalCounts[index] =
                                  totalCounts[index] + item.actualOrderCount;
                                totalMoneys[index] =
                                  totalMoneys[index] +
                                  item.actualOrderCount * item.actualPrice;
                              } else {
                                totalCounts[index] =
                                  totalCounts[index] + item.inWarehouseCount;
                                totalMoneys[index] =
                                  totalMoneys[index] +
                                  item.inWarehouseCount * item.actualPrice;
                              }
                            }
                          }
                        );
                      } else {
                        if (rule.onWayAttendEventFlag === 1) {
                          // 在途也参与活动
                          totalCount = totalCount + item.actualOrderCount;
                          totalMoney =
                            totalMoney +
                            item.actualOrderCount * item.actualPrice;
                        } else {
                          totalCount = totalCount + item.inWarehouseCount;
                          totalMoney =
                            totalMoney +
                            item.inWarehouseCount * item.actualPrice;
                        }
                      }
                    }
                    rule.goods.push(item);
                  }
                });
                let selectTotal = 0.0; // 选择的条件
                rule.conditions.forEach((condition) => {
                  // 针对赠品计算，选出最优活动规则条件
                  let conditionTotal = 0.0;
                  let isCheck = true;
                  if (rule.moneyOrCount === 1) {
                    // 按金额
                    if (rule.target !== 1 && rule.isCompose === 1) {
                      // 组合情况
                      condition.groupConditions.forEach(
                        (groupCondition, index) => {
                          if (totalMoneys[index] < groupCondition.oneBuyMoney) {
                            isCheck = false;
                            return;
                          }
                          groupCondition.totalMoney = totalMoneys[index];
                          conditionTotal =
                            conditionTotal + groupCondition.oneBuyMoney;
                        }
                      );
                    } else {
                      if (totalMoney < condition.oneBuyMoney) {
                        isCheck = false;
                      }
                      condition.totalMoney = totalMoney;
                      conditionTotal = condition.oneBuyMoney;
                    }
                  } else {
                    // 按数量
                    if (rule.target !== 1 && rule.isCompose === 1) {
                      // 组合情况
                      condition.groupConditions.forEach(
                        (groupCondition, index) => {
                          if (totalCounts[index] < groupCondition.oneBuyCount) {
                            isCheck = false;
                            return;
                          }
                          groupCondition.totalCount = totalCounts[index];
                          conditionTotal =
                            conditionTotal + groupCondition.oneBuyCount;
                        }
                      );
                    } else {
                      if (totalCount < condition.oneBuyCount) {
                        isCheck = false;
                      }
                      condition.totalCount = totalCount;
                      conditionTotal = condition.oneBuyCount;
                    }
                  }
                  if (isCheck && selectTotal < conditionTotal) {
                    selectTotal = conditionTotal;
                    rule.selectCondition = condition;
                  }
                });
              });
              this.promotionRules = res.rules;
            }
            if (res.gradeRule !== undefined && res.gradeRule !== null) {
              res.gradeRule.goods = [];
              this.formData.orderGoods.forEach((item) => {
                if (
                  item.gradeDiscountId !== undefined &&
                  item.gradeDiscountId !== null
                ) {
                  // 商品等级折扣活动Id
                  res.gradeRule.goods.push(item);
                }
              });
              this.gradeRule = res.gradeRule;
            }
          }
          this.loading3 = false;
          this.selectPresentLoading = false;
        });
    },
    cancelPresentChange() {
      this.selectPresentShow = false;
      this.isPresentChange = false;
    },
    submitPresentChange() {
      if (this.isPresentChange) {
        this.changePresentLoading = true;
        let changePresent = {
          importOrderId: this.formData.importOrderId,
          presentGoodss: [],
        };
        this.presentRule.presentGoodss.forEach((presentGoods) => {
          if (
            presentGoods.itemCount !== undefined &&
            presentGoods.itemCount !== null &&
            presentGoods.itemCount !== "" &&
            presentGoods.itemCount !== "0" &&
            presentGoods.itemCount !== 0
          ) {
            changePresent.presentGoodss.push({
              actualOrderCount: presentGoods.itemCount,
              goodsId: presentGoods.goodsId,
              itemId: presentGoods.itemId,
              itemType: 2,
              ruleId: this.presentRule.id,
            });
          }
        });
        this.$api
          .put(this, "user/u/p/importOrder/present", changePresent)
          .then((res) => {
            if (res.code === 0) {
              this.$message.success({
                message: this.$t("versionsps.GiftUpdateSuc"),
                duration: 3 * 1000,
              });
              this.selectPresentShow = false;
              this.loading2 = true;
              this.selectPresentLoading = true;
              this.orderImportOrder();
            }
            this.changePresentLoading = false;
          });
      } else {
        this.changePresentLoading = false;
        this.selectPresentShow = false;
      }
    },
    cancelChange() {
      this.changePromotionShow = false;
      this.ruleId = "";
      this.rule = "";
    },
    submitChange() {
      let request = {
        id: this.goods.id,
        importOrderId: this.formData.importOrderId,
        count: this.goods.itemCount,
      };
      if (!this.rule.isGrade) {
        this.goods.ruleId = this.rule.id;
        this.goods.gradeDiscountId = null;
        request.ruleId = this.rule.id;
      } else {
        this.goods.ruleId = null;
        this.goods.gradeDiscountId = this.rule.id;
        request.gradeDiscountId = this.rule.id;
      }
      this.$api
        .put(this, "user/u/user/importOrder/order/change/promotion", request)
        .then((res) => {
          if (res.code === 0) {
            this.$message.success({
              message: this.$t("versionsps.Switched"),
              duration: 3 * 1000,
            });
            // this.loading2 = true
            this.changePromotionShow = false;
            this.isChangePromotion = true;
            // this.orderImportOrder();
          }
        });
    },
    changePromotion(raw) {
      this.goods = raw;
      if (this.goods.ruleId !== undefined && this.goods.ruleId !== null) {
        this.ruleId = this.goods.ruleId;
      } else if (
        this.goods.gradeDiscountId !== undefined &&
        this.goods.gradeDiscountId !== null
      ) {
        this.ruleId = this.goods.gradeDiscountId;
      }
      this.changePromotionShow = true;
    },
    changeRule(val) {
      // 切换活动
      this.rule = val;
      this.ruleId = this.rule.id;
    },
    handelPresentPlusSing(row) {
      if (this.checkhHandleFlag === 2) {
        row.itemCount = Number(row.itemCount) + 1;
        this.selectCount = this.selectCount + 1;
        if (row.itemCount > row.presentCount) {
          // 赠送数量不能大于赠送总数量
          this.$message.warning(
            this.$t("versionsps.Goods") +
              row.itemCode +
              this.$t("versionsps.GiveQuantityLess") +
              row.presentCount
          );
          row.itemCount = Number(row.itemCount) - 1;
          this.selectCount = this.selectCount - 1;
        }
        if (this.selectCount > this.presentTotal) {
          this.$message.warning(this.$t("versionsps.GiveMaxQuantity"));
          row.itemCount = Number(row.itemCount) - 1;
          this.selectCount = this.selectCount - 1;
        }
        this.isPresentChange = true;
      }
    },
    handelPresentMinus(row) {
      if (this.checkhHandleFlag === 2) {
        if (row.itemCount > 0) {
          this.isPresentChange = true;
          row.itemCount = Number(row.itemCount) - 1;
          this.selectCount = this.selectCount - 1;
        }
      }
    },
    // ======== 操作 ========
    handelMinus(row) {
      // ..减号操作
      if (
        this.checkhHandleFlag === 2 &&
        (row.itemType === undefined ||
          row.itemType === null ||
          row.itemType !== 2) &&
        row.actualOrderCount > 0
      ) {
        // ..已下单不能操作
        row.actualOrderCount--;
        if (row.goodsType === 1 && row.actualOrderCount > row.maxCount) {
          this.$message({
            message: this.$t("P.ShortInventory"),
            type: "warning",
            duration: 3 * 1000,
          });
          row.actualOrderCount = row.maxCount;
        }
      }
      this.changeCount = false;
      this.formData.orderGoods.forEach((item, index) => {
        if (this.counts[index] !== item.actualOrderCount) {
          this.changeCount = true;
        }
      });
    },
    handelPlusSing(row) {
      // ..加号操作
      if (
        this.checkhHandleFlag === 2 &&
        (row.itemType === undefined ||
          row.itemType === null ||
          row.itemType !== 2)
      ) {
        // ..已下单不能操作
        row.actualOrderCount++;
        if (row.goodsType === 1 && row.actualOrderCount > row.maxCount) {
          this.$message({
            message: this.$t("P.ShortInventory"),
            type: "warning",
            duration: 3 * 1000,
          });
          row.actualOrderCount = row.maxCount;
        }
      }
      this.changeCount = false;
      this.formData.orderGoods.forEach((item, index) => {
        if (this.counts[index] !== item.actualOrderCount) {
          this.changeCount = true;
        }
      });
    },
    handelPresentNum(row) {
      if (row.itemCount > row.presentCount) {
        // 赠送数量不能大于赠送总数量
        this.$message.warning(
          this.$t("versionsps.Goods") +
            row.itemCode +
            this.$t("versionsps.GiveQuantityLess") +
            row.presentCount
        );
        row.itemCount = row.presentCount;
      }
      let count = 0;
      this.presentRule.presentGoodss.forEach((goods) => {
        count = count + Number(goods.itemCount);
      });
      if (count > this.presentTotal) {
        this.$message.warning(this.$t("versionsps.GiveMaxQuantity"));
        row.itemCount = Number(row.itemCount) - (count - this.presentTotal);
        this.selectCount = this.presentTotal;
      } else {
        this.selectCount = count;
      }
      this.isPresentChange = true;
    },
    handelChangeNum(row, index) {
      // ..总订购数量
      if (row.goodsType === 1 && row.actualOrderCount > row.maxCount) {
        this.$message({
          message: this.$t("P.ShortInventory"),
          type: "warning",
          duration: 3 * 1000,
        });
        row.actualOrderCount = row.maxCount;
      }
      // if (this.formData.orderTypeValue === 'XSDD10_SYS') { // ..订单为MTO订单
      //   if (row.actualOrderCount < Number(row.moq)) {
      //     this.$message.warning('订购数量不可小于' + Number(row.moq))
      //     setTimeout(() => {
      //       row.actualOrderCount = Number(row.moq)
      //     }, 300)
      //   }
      //   if (row.actualOrderCount === '') {
      //     setTimeout(() => {
      //       row.actualOrderCount = Number(row.moq)
      //     }, 300)
      //   }
      // } else if (this.formData.orderTypeValue === 'XSDD04_SYS') { // ..订单为直运订单
      //   if (row.actualOrderCount < Number(row.moq)) {
      //     this.$message.warning('订购数量不可小于' + Number(row.moq))
      //     setTimeout(() => {
      //       row.actualOrderCount = Number(row.moq)
      //     }, 300)
      //   }
      //   if (row.actualOrderCount === '') {
      //     setTimeout(() => {
      //       row.actualOrderCount = Number(row.moq)
      //     }, 300)
      //   }
      // } else if (this.formData.orderTypeValue === 'DIY_SYS') { // 定制订单
      // } else {
      //   if (row.actualOrderCount > row.maxCount) {
      //     this.$message.warning(row.itemName + '库存不足,' + '当前最大可购数量为' + row.maxCount)
      //     this.isError = true
      //     setTimeout(() => {
      //       row.actualOrderCount = row.maxCount
      //       this.isError = false
      //     }, 300)
      //   }
      //   if (row.actualOrderCount === '') {
      //     setTimeout(() => {
      //       row.actualOrderCount = Number(row.moq)
      //     }, 300)
      //   }
      // }
      this.changeCount = false;
      this.formData.orderGoods.forEach((item, index) => {
        if (this.counts[index] !== Number(item.actualOrderCount)) {
          this.changeCount = true;
        }
      });
    },
    back() {
      this.$router.push("/BatchImport");
    },
  },
  computed: {
    checkhHandleFlag() {
      return this.$route.query.handleFlag; // 1、已下单 2、未下单
    },
  },
  watch: {
    "formData.isInvoice": function () {
      // ..是否开票 0 否 1 是
      if (this.formData.isInvoice === 0) {
        this.invoiceShow = false;
        this.formData.invoiceType = "";
      } else {
        this.invoiceShow = true;
      }
    },
    "formData.orderTypeValue": function () {
      // ..订单类型
      if (
        this.formData.orderTypeValue === "XSDD10_SYS" ||
        this.formData.orderTypeValue === "XSDD04_SYS"
      ) {
        // MTO订单 || 直运订单
        this.orderShow = false;
      } else {
        this.orderShow = true;
      }
    },
    "formData.provinceId": function () {
      // ..省
      this.orderDistribution();
    },
    "formData.cityId": function () {
      // ..市
      this.orderDistribution();
    },
    "formData.districtId": function () {
      // ..市
      this.orderDistribution();
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
    position: relative;
    padding-bottom: 10px;
    border-bottom: 1px solid @bdLightColor;
    font-size: 20px;
    .back {
      position: absolute;
      right: 0;
      bottom: 10px;
      font-size: 14px;
      color: @blue;
      cursor: pointer;
      &:hover {
        opacity: 0.6;
      }
    }
  }
  .content {
    padding: 24px 40px 0;
    border: 2px solid @bdLightColor;
    border-radius: 8px;
  }
}
.user-right {
  .content {
    .main-content {
      padding-bottom: 30px;
      .half-width {
        width: 50%;
        padding-left: 40px;
        box-sizing: border-box;
        float: left;
        /deep/.el-form-item__label {
          display: inline-block;
          width: 100px;
          line-height: 1.5;
          text-align: left;
        }
        /deep/.el-form-item__content {
          display: inline-block;
          line-height: 1.5;
          .el-form-item__error {
            width: 200%;
            line-height: 1.2;
          }
          .long {
            .el-input__inner {
              width: 600px;
            }
          }
          .el-input__inner {
            display: inline-block;
            width: 152px;
            line-height: 1.5;
            border: 1px solid @bdLightColor;
            border-radius: 0;
          }
        }
        &.one {
          width: 100%;
        }
      }
      .text-center {
        text-align: center;
        overflow: hidden;
        margin-bottom: 10px;
      }
      .text-center.title {
        padding-top: 10px;
        padding-bottom: 10px;
      }
      .order-message {
        width: 100%;
        padding-bottom: 10px;
        font-size: 14px;
        color: @lightBlack;
        span {
          color: @blue;
          cursor: pointer;
          i {
            margin-right: 2px;
          }
          .iconfont {
            font-size: 14px;
            color: @lighterGray;
          }
          & + span {
            margin-left: 15px;
          }
        }
      }
      .box-btn-top {
        padding: 20px;
      }
      .box-has-border {
        margin-top: 20px;
        overflow: hidden;
        .title {
          margin-bottom: 20px;
          font-size: 16px;
          color: @lightBlack;
        }
        /deep/.el-table {
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
          .batch_input_number {
            font-size: 0;
            .batch_minus {
              display: inline-block;
              width: 25px;
              padding: 0px;
              cursor: pointer;
              height: 22px;
              line-height: 22px;
              font-size: 12px;
              background-color: @white;
              text-align: center;
              vertical-align: middle;
              border-radius: 4px 0 0 4px;
              border: 1px solid @bdColor;
              border-right: 0;
            }
            div {
              display: inline-block;
              vertical-align: middle;
              .batch_input1 {
                text-align: center;
                width: 60px;
                height: 22px;
                font-size: 12px;
                border: 1px solid @bdColor;
                line-height: 22px;
                margin: 0 auto;
              }
            }
            .batch_plusSign {
              display: inline-block;
              padding: 0px;
              width: 25px;
              cursor: pointer;
              height: 22px;
              line-height: 22px;
              font-size: 12px;
              background-color: #f5f7fa;
              text-align: center;
              vertical-align: middle;
              border-radius: 0 4px 4px 0;
              border: 1px solid #dcdfe6;
              border-left: 0;
            }
          }
          .mini-delete-btn {
            white-space: nowrap;
            cursor: pointer;
            font-size: 12px;
            color: @red !important;
            -webkit-appearance: none;
            text-align: center;
          }
        }
        .align-right {
          text-align: right;
        }
        .orderMoney {
          text-align: right;
          margin-top: 20px;
          font-size: 12px;
          .cost-info {
            margin-top: 10px;
            color: @lightBlack;
            .cost-label {
              display: inline-block;
              text-align: left;
            }
            .cost-val {
              display: inline-block;
              width: 80px;
              text-align: right;
              font-size: 12px;
              color: @red;
            }
          }
        }
        div.form {
          margin-top: 30px;
          margin-bottom: 40px;
          form.el-form {
            margin-right: 0;
            width: 80%;
            min-width: 800px;
            max-width: 1000px;
          }
        }
      }
      .tableCenters {
        text-align: center;
        margin: 20px;
        .el-button {
          padding: 0 20px;
          height: 40px;
          line-height: 40px;
          & + .el-button {
            margin-left: 20px;
          }
        }
      }
    }
    .mini-present-btn {
      display: inline-block;
      background-color: #fab42d !important;
      border: 1px solid #fab42d;
      padding: 7px 15px;
      font-size: 12px;
      color: white !important;
      border-color: #fab42d;
      font-weight: 500;
      line-height: 1;
      white-space: nowrap;
      cursor: pointer;
      box-sizing: border-box;
      outline: none;
      border-radius: 4px;
      &:hover {
        opacity: 0.6;
      }
    }
    /deep/.el-dialog__wrapper {
      .el-dialog__body {
        padding: 20px;
        .el-table__body-wrapper {
          .pomotion-rule {
            border-radius: 5px;
            text-align: left;
            padding: 10px 10px 10px 10px;
            margin-bottom: 5px;
            border: 1px solid #ccc;
            background-color: #f8f8f8;
          }
        }
        .foot-btn {
          margin-top: 20px;
        }
        .bg-purple-light {
          background: #e5e9f2;
          .bg-purple {
            padding: 10px;
            background: #d3dce6;
          }
        }
        .pomotion-present {
          vertical-align: middle;
          position: relative;
          background-color: #acf2ff;
          color: #333;
          .half-left {
            width: 30%;
            float: left;
            .present-label {
              margin: 10px;
              background-color: #fab42d !important;
              border: 1px solid #fab42d;
              padding: 5px;
              font-size: 14px;
              color: white !important;
              border-color: #fab42d;
              display: block;
              font-weight: 500;
              line-height: 1.1em;
              -webkit-appearance: none;
              text-align: left;
              -webkit-box-sizing: border-box;
              box-sizing: border-box;
              outline: none;
              font-weight: 500;
              user-select: none;
              -moz-user-select: none;
              -webkit-user-select: none;
              -ms-user-select: none;
              border-radius: 4px;
            }
          }
          .half-right {
            width: 70%;
            position: absolute;
            text-align: left;
            margin-left: 30%;
            top: 50%;
            -webkit-transform: translateY(-50%);
            transform: translateY(-50%);
          }
        }
        .batch_input_number {
          font-size: 0;
          .batch_minus {
            display: inline-block;
            width: 25px;
            padding: 0px;
            cursor: pointer;
            height: 22px;
            line-height: 22px;
            font-size: 12px;
            background-color: #f5f7fa;
            text-align: center;
            vertical-align: middle;
            border-radius: 4px 0 0 4px;
            border: 1px solid #dcdfe6;
            border-right: 0;
          }
          div {
            display: inline-block;
            vertical-align: middle;
            .batch_input1 {
              text-align: center;
              width: 60px;
              height: 22px;
              font-size: 12px;
              border: 1px solid #dcdfe6;
              line-height: 22px;
              margin: 0 auto;
            }
          }
          .batch_plusSign {
            display: inline-block;
            padding: 0px;
            width: 25px;
            cursor: pointer;
            height: 22px;
            line-height: 22px;
            font-size: 12px;
            background-color: #f5f7fa;
            text-align: center;
            vertical-align: middle;
            border-radius: 0 4px 4px 0;
            border: 1px solid #dcdfe6;
            border-left: 0;
          }
        }
        .mini-delete-btn {
          padding: 7px 15px;
          line-height: 1;
          white-space: nowrap;
          cursor: pointer;
          font-size: 12px;
          color: white !important;
          -webkit-appearance: none;
          text-align: center;
          border: 1px solid #ff6565;
          background-color: #ff6565;
        }
      }
    }
  }
}
</style>
