<template>
  <div>
    <div class="user rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState" ref="header"></Header>
      <!--主内容-->
      <div class="user-main rl-clear rl-margin-zero">
        <!--公共左边-->
        <Left></Left>
        <div  class="user-right rl-fr rl-bg-white rl-margin-top-default rl-padding-bottom-double">
          <div class="order-list-wrap">
            <div class="user-right-title2">
              <h6 >{{ $t("P.Order") }}</h6>
              <span v-if="erpNo&&erpNo!='undefined'"> 我的专属售后编码：{{erpNo}}</span>
            </div>
            <div class="query rl-padding-top-default">
              <div class="search rl-clear">
                <!-- 订单号 -->
                <div class="item rl-fl rl-margin-right-xxs">
                  <input
                    type="text"
                    v-model.trim="orderNo"
                    onkeyup="this.value=this.value.replace(/\D/g,'')"
                    :placeholder="$t('UserCenter.OrderNo')"/>
                </div>
                <!-- 收货人 -->
                <div class="item rl-fl rl-margin-right-xxs">  
                  <input
                    type="text"
                    v-model.trim="name"
                    :placeholder="$t('UserCenter.RecipientName')"
                  />
                </div>
                <!-- 订单类型 -->
                <div class="item rl-fl rl-margin-right-xxs">
                  <el-select class="select" placeholder="订单类型" v-model.trim="orderMType" clearable>
                    <el-option :key="item.erpType"  :label="item.name" :value="item.id" v-for="item in orderTypes"></el-option>
                  </el-select>
                </div>
                <!-- 查询订单 -->
                <div class="search-order rl-fl">
                  <el-button
                    @click="queryOrder"
                    class="search-btn rl-text-white">{{ $t("UserCenter.OrderQuery") }}</el-button>
                  <el-button
                    @click="showSenior"
                    class="search-btn rl-text-white">{{ $t("UserCenter.AdvancedSearch") }}</el-button>
                </div>
              </div>
              <!-- 高级搜索 -->
              <div class="search-info rl-clear rl-padding-top-xxxs" v-show="senior === true">
                <!-- 联系电话 -->
                <div class="items rl-fl rl-margin-right-xxs">
                  <div class="common-input rl-fl">
                    <input v-model.trim="phone" :placeholder="$t('UserCenter.Tel')" type="text"/>
                  </div>
                </div>
                <!-- 手机号 -->
                <div class="items rl-fl rl-margin-right-xxs">
                  <div class="common-input rl-fl">
                    <input
                      v-model.trim="mobile"
                      :placeholder="$t('UserCenter.Phone')"
                      type="text"
                    />
                  </div>
                </div>
                <!-- 联系地址 -->
                <div class="items rl-fl rl-margin-right-xxs">
                  <div class="common-input addr-input rl-fl">
                    <input
                      v-model.trim="address"
                      :placeholder="$t('UserCenter.ContactAddress')"
                      type="text"
                    />
                  </div>
                </div>
                <!-- 开始时间--结束时间 -->
                <div class="items date-items rl-fl rl-clear">
                  <div class="rl-fl">
                    <el-date-picker
                      v-model="filters.column.starDate"
                      type="datetime"
                      :placeholder="$t('ExportOrder.StartTime')"
                      :picker-options="pickerStarDate"
                    ></el-date-picker>
                  </div>
                  <span class="rl-fl">－</span>
                  <div class="rl-fl">
                    <el-date-picker
                      v-model="filters.column.endDate"
                      type="datetime"
                      :placeholder="$t('ExportOrder.EndTime')"
                      :picker-options="pickerEndDate"
                    ></el-date-picker>
                  </div>
                </div>
              </div>
            </div>

            
            <div class="query-detail">
              <!-- 订单类型--Y -->
              <div class="nav rl-clear">
                <ul class="rl-fl" :class="$i18n.locale === 'en' ? 'navEn' : ''">
                  <!-- 全部订单 -->
                  <li
                    :class="orderStatus === 0 ? 'current' : ''"
                    @click="changeState(0)" >
                    {{ $t("UserCenter.AllOrders") }}
                  </li> 
                  <!--待付款 -->
                  <li
                    :class="orderStatus === 7 ? 'current' : ''"
                    @click="changeState(7)">
                    {{ $t("UserCenter.PendingPayment") }}
                  </li>
                  <!--待确认--->
                  <li
                    :class="orderStatus === 1 ? 'current' : ''"
                    @click="changeState(1)"
                  >
                    {{ $t("UserCenter.ToBeConfirmed") }}
                  </li>
                  <!-- 待发货 -->
                  <li
                    :class="orderStatus === 2 ? 'current' : ''"
                    @click="changeState(2)"
                  >
                    {{ $t("UserCenter.ToBeShipped") }}
                  </li>
                   <!-- 出库中 -->
                  <li
                    :class="orderStatus === 9 ? 'current' : ''"
                    @click="changeState(9)"
                  >
                    {{ $t("UserCenter.InTheOutbound") }}
                  </li>
                  <!--部分发货--->
                  <li
                    :class="orderStatus === 3 ? 'current' : ''"
                    @click="changeState(3)"
                  >
                    {{ $t("UserCenter.PartOfShipped") }}
                  </li>
                  <!-- 已发货 -->
                  <li
                    :class="orderStatus === 4 ? 'current' : ''"
                    @click="changeState(4)"
                  >
                    {{ $t("UserCenter.Shipped") }}
                  </li>
                  <!-- 已完成 -->
                  <li
                    :class="orderStatus === 6 ? 'current' : ''"
                    @click="changeState(6)"
                  >
                    {{ $t("UserCenter.Completed") }}
                  </li>
                  <!-- <li :class="orderStatus === 5? 'current': ''" class="rl-padding-left-default rl-padding-right-default" @click="changeState(5)">{{$t('UserCenter.Refunded')}}</li> -->
                  <!-- 已关闭 -->
                  <li
                    :class="orderStatus === 5 ? 'current' : ''"
                    @click="changeState(5)"
                  >
                    {{ $t("UserCenter.Closed") }}
                  </li>
                </ul>
                <!-- 导出订单---->
                <div @click="showExportOrder" class="rl-fr export-btn">
                  <i class="iconfont icon-export"></i>
                  {{ $t("UserCenter.ExportOrder") }}
                </div>
              </div>
              <!-- 表格列表数据 --y-->
              <div class="table">
                <table>
                  <tr>
                    <th>{{ $t("UserCenter.OrderNo") }}</th>
                    <th>{{ $t("UserCenter.RecipientName") }}</th>
                    <th>{{ $t("UserCenter.OrderDate") }}</th>
                    <th>{{ $t("UserCenter.TotalAmount") }}</th>
                    <th>{{ $t("UserCenter.OrderStatus") }}</th>
                    <th>{{ $t("UserCenter.Operation") }}</th>
                  </tr>
                  <tr v-if="orderList.length === 0 || totalCount === 0">
                    <td class="empty" colspan="6">{{ $t("P.NoData") }}</td>
                  </tr>
                  <tr v-else class="goods-list" v-for="(order, index) in orderList" :key="index">
                    <td>{{ order.orderNo }}</td>
                    <td>{{ order.userName }}</td>
                    <td>{{ order.createTime | formatDate }}</td>
                    <td>
                      <i v-if="$i18n.locale === 'zh'">￥</i>
                      <i v-else>$</i>
                      {{ $i18n.locale === 'zh'?order.payAmount:(Number(order.payAmount)*exchange).toFixed(2) }}
                    </td>
                    <td>{{ confirmOrderStatus(order.frontOrderStatus) }}</td>
                    <td width="20%" class="rl-clear">
                      <div
                        class="views rl-fl rl-margin-right-default"
                        v-if="order.deliverStatus === 3"
                      >
                        <!--<span class="rl-text-xxs" v-if="order.deliverStatus === 1 && order.orderStatus !== 5">待发货</span>
                      <span class="rl-text-xxs" v-if="order.deliverStatus === 2 && order.orderStatus !== 5">部分发货</span>
                        <span class="rl-text-xxs" v-if="order.deliverStatus === 3 && order.orderStatus !== 5">已发货</span>-->
                        <!--<span @click="checkLog" class="log rl-tc rl-text-white rl-bg-blue rl-text-xxss cursor-pointer rl-fl">查看物流</span>-->
                      </div>
                      <!--再来一单-->
                      <div class="views rl-fl rl-margin-left-xxxs">
                        <span
                          @click="orderAgain(order.id)"
                          class="log rl-tc cursor-pointer rl-fl"
                          >{{ $t("UserCenter.OrderAgain") }}</span
                        >
                      </div>
                      <!--<div class="views rl-margin-zero" v-if="order.deliverStatus !== 3 && order.orderStatus !== 1"><span @click="goOrderDetail(order.id)" class="log rl-tc rl-text-white rl-bg-orange-mm rl-text-xxss cursor-pointer rl-margin-right-xxxs">查看详情</span></div>-->
                      <div class="views rl-fl rl-margin-left-mid">
                        <span
                          @click="goOrderDetail(order.id)"
                          class="log rl-tc cursor-pointer"
                          >{{ $t("UserCenter.ViewDetails") }}</span
                        >
                      </div>
                      <!-- <div class="countdown-box rl-fr rl-padding-left-xxxs">
                        <countDown
                          :createTime="order.createTime"
                          :orderId="order.id"
                          :payStatus="order.payStatus"
                          :orderStatus="order.orderStatus"
                          :actEndTime="order.invalidTime"
                        ></countDown>
                      </div>-->
                    </td>
                  </tr>
                </table>
              </div>
            </div>
          </div>
          <!-- 分页 -->
          <div class="apply rl-clear">
            <!--<div @click="goAfterApplicate" class="rl-fl after-sale rl-bg-orange-mm rl-tc rl-text-white rl-text-xxss cursor-pointer">申请售后</div>-->
            <div class="rl-tr rl-margin-top-default">
              <el-pagination
                v-if="paginationShow && this.totalCount > 0"
                background
                :current-page.sync="cur_page"
                @current-change="handleCurrentChange"
                @size-change="handleSizeChange"
                layout="prev, pager, next, jumper"
                :page-size="pagesize"
                :total="totalCount"
              ></el-pagination>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!--再来一单---无法购买列表-->
    <el-dialog class="alls" :title="$t('OrderSuccess.UnableList')" :visible.sync="dialogVisible">
      <div class="shop-table max-height300 rl-padding-left-default rl-padding-right-default" >
        <table>
          <tbody>
            <tr>
              <th>{{ $t("ShopCarts.ItemNo") }}</th>
              <th>{{ $t("ShopCarts.ItemName") }}</th>
              <th>{{ $t("ShopCarts.Quantity") }}</th>
              <th>{{ $t("OrderSuccess.Status") }}</th>
            </tr>
            <tr v-for="item in dialogItemList" :key="item.id">
              <td width="150px" class="rl-text-xxs">{{ item.itemCode }}</td>
              <td width="250px" class="rl-text-xxs">
                <span
                  v-show="$i18n.locale === 'zh' || !item.itemNameEn == true"
                  >{{ item.itemName }}</span
                >
                <span v-show="$i18n.locale === 'en'">{{
                  item.itemNameEn
                }}</span>
              </td>
              <td width="100px">
                <div v-if="item.saleStatus !== 3">x{{ item.count }}</div>
                <div v-else>x{{ item.count - item.stockItemCount }}</div>
              </td>
              <td width="100px">
                <span v-if="item.saleStatus !== 3" class="rl-text-xxs">{{
                  $t("P.SoldOut")
                }}</span>
                <span
                  class="rl-text-xxs"
                  v-else-if="
                    Number(item.stockItemCount) <= 0 ||
                    Number(item.count) > Number(item.stockItemCount)
                  "
                  >{{ $t("P.OutStock") }}</span
                >
                <i
                  class="rl-text-xxs"
                  v-if="item.advanceSaleFlag === 1 && item.saleStatus === 3"
                  >,{{ $t("P.CanBook") }}</i
                >
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="confirmDialog">{{
          $t("ShopCarts.ConShopping")
        }}</el-button>
        <el-button @click="dialogVisible = false">{{
          $t("P.Cancel")
        }}</el-button>
      </div>
    </el-dialog>

    <!-- 再来一单 -->
    <div v-if=" onceAgainDialogVisible === true &&(activityPromotions.length > 0 || withoutActivityItem.length > 0)">
      <onceAgain
        @shutDialog="shutDialog"
        :activityDialogIsVisible="onceAgainDialogVisible"
        :activityPromotions="activityPromotions"
        :customizedAttendEventFlag="0"
        :mtoAttendEventFlag="0"
        :onWayAttendEventFlag="0"
        :orderType="1"
      ></onceAgain>
    </div>
    <!--加载动画-->
    <loading v-if="this.showLoading === true"></loading>

    <!-- 导出订单dialog -->
    <el-dialog
      width="60%"
      :modal-append-to-body="false"
      :visible="showExport"
      :before-close="closeExportOrder">
      <el-form
        style="margin-top: 20px; margin-right: 50px"
        ref="form"
        :model="form"
        label-width="200px">
        <!-- Y -->
        <el-form-item label="导出范围">
          <el-select v-model="form.activityFlag"  placeholder="请选择">
              <el-option key="0" label="全部订单" value="0"></el-option>
              <el-option key="1" label="全部参与活动订单" value="1"></el-option>
              <el-option key="2" label="全部未参与活动订单" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="订单类型">
          <div class="export-price">
            <div class="all-file">
               <el-checkbox :label="0" class="om-orderStatus-all" @change="orderTypeIdAllChangeFun">全部</el-checkbox>
            </div>
            <div class="file">
            <el-checkbox-group v-model="form.orderTypeIdList">
               <el-checkbox :label="item.id" v-for="item in orderTypes" :key="item.id">{{item.name}}</el-checkbox>
            </el-checkbox-group>
            </div>
          </div>
        </el-form-item>

  
        <el-form-item label="订单状态">
           <div class="om-orderStatus-checkbox">
              <el-checkbox :label="0" class="om-orderStatus-all" @change="orderStatusAllChangeFun">全部</el-checkbox>
              <el-checkbox-group v-model="orderStatusList"> 
                <el-checkbox :label="1">待确认</el-checkbox>
                <el-checkbox :label="2">已确认</el-checkbox>
                <el-checkbox :label="3">已拒绝</el-checkbox>
                <el-checkbox :label="4">已取消</el-checkbox>
                <el-checkbox :label="5">已完成</el-checkbox>
                <el-checkbox :label="9">出库中</el-checkbox>
              </el-checkbox-group> 
           </div>
        </el-form-item>
        <el-form-item label="发货状态">
           <div class="om-orderStatus-checkbox">
              <el-checkbox :label="0" class="om-orderStatus-all" @change="orderFahuoAllChangeFun">全部</el-checkbox>
              <el-checkbox-group v-model="deliverStatus"> 
                <el-checkbox :label="1">未发货</el-checkbox>
                <el-checkbox :label="2">出库中</el-checkbox>
                <el-checkbox :label="3">部分发货</el-checkbox>
                <el-checkbox :label="4">已发货</el-checkbox>
              </el-checkbox-group> 
           </div>
        </el-form-item>
        <el-form-item label="付款状态">
           <div class="om-orderStatus-checkbox">
               <el-checkbox :label="0" class="om-orderStatus-all" @change="orderPayAllChangeFun">全部</el-checkbox>
               <el-checkbox-group v-model="payStatusList"> 
                <el-checkbox :label="1">未付款</el-checkbox>
                <el-checkbox :label="2">部分付款</el-checkbox>
                <el-checkbox :label="3">已付款</el-checkbox>
                <el-checkbox :label="4">部分退款</el-checkbox>
                <el-checkbox :label="5">退款申请中</el-checkbox>
                <el-checkbox :label="6">已退款</el-checkbox>
              </el-checkbox-group> 
           </div>
           
          
        </el-form-item>

        <el-form-item label="货品编码">
           <el-input v-model="form.itemCode" placeholder="请输入货品编码" style="width:200px;"></el-input>
        </el-form-item>
        <!-- 订单时间 -->
         <el-form-item :label="$t('ExportOrder.SelectionTime')">
          <template>
            <el-radio-group v-model="recentDays">
              <el-radio :label="1">{{
                $t("ExportOrder.ShipWithin1Days")
              }}</el-radio>
              <el-radio :label="3">{{
                $t("ExportOrder.ShipWithin3Days")
              }}</el-radio>
              <el-radio :label="7">{{
                $t("ExportOrder.ShipWithin7Days")
              }}</el-radio>
              <el-radio :label="0">{{
                $t("ExportOrder.SelectionPeriod")
              }}</el-radio>
            </el-radio-group>
            <el-date-picker
              v-if="recentDays === 0"
              size="mini"
              v-model="deliverTimes"
              style="margin-left: 15px; width: 330px"
              type="datetimerange"
              value-format="timestamp"
              :range-separator="$t('ExportOrder.TO')"
              :start-placeholder="$t('ExportOrder.StartTime')"
              :end-placeholder="$t('ExportOrder.EndTime')"
              @change="dateConfirmChange"
            ></el-date-picker>
          </template>
        </el-form-item>

        <el-form-item label="导出字段">
          <div class="export-price">
            <div class="all-file">
              <el-checkbox
                :indeterminate="isAllIndeterminate"
                @change="handleCheckAllChange"
                v-model="isAll">{{ $t("UserCenter.SelectAll") }}</el-checkbox>
            </div>
            
            <div class="file"> 
              <el-checkbox-group style="margin-bottom: 10px" v-model="form.fieldList">
                <el-checkbox label="CREATE_TIME">下单日期</el-checkbox>
                <!-- <el-checkbox label="ORDER_ERP_NO">ERP订单号</el-checkbox> -->
                <el-checkbox label="ITEM_CODE"> 货品编码</el-checkbox>
                <!-- <el-checkbox label="ORDER_FACTORY_NO"> 工厂订单号</el-checkbox> -->
                <el-checkbox label="SALE_PRICE">价格（含税单价）</el-checkbox>
                <el-checkbox label="ORDER_NO"> B2B订单号</el-checkbox>
                <!-- <el-checkbox label="ERP_OUTBOUND_NO">ERP出库单号</el-checkbox> -->
                <el-checkbox label="RECEIVE_ADDRESS">收货地址</el-checkbox>
                <el-checkbox label="ITEM_NAME">货品名称</el-checkbox>
                <!-- <el-checkbox label="ORDER_THIRDPARTY_NO">第三方订单号</el-checkbox> -->
                <el-checkbox label="ACTUAL_PRICE">折后价（折扣后含税单价）</el-checkbox>
                <el-checkbox label="ORDER_TYPE_ID">订单类型</el-checkbox>
                <!-- <el-checkbox label="ORDER_DELIVER_BILL_ID"> B2B发货单号</el-checkbox> -->
                <el-checkbox label="CONTACT_MOBILE"> 联系电话</el-checkbox>
                <el-checkbox label="ITEM_COUNT">销售数量</el-checkbox>
                <el-checkbox label="MATERIAL_NAME">材质</el-checkbox>
                <el-checkbox label="SALE_PRICE_SUM">总额（折前含税总额）</el-checkbox>
                <el-checkbox label="ORDER_STATUS"> 订单状态</el-checkbox>
                <el-checkbox label="DISTRIBUTION_NAME">配送方式</el-checkbox>
                <el-checkbox label="DELIVER_COUNT">已发货数</el-checkbox>
                <el-checkbox label="MODEL_NAME">型号</el-checkbox>
                <el-checkbox label="ACTUAL_PRICE_SUM">总额（折后含税总额）</el-checkbox>
                <el-checkbox label="CURRENCY_TYPE">订单币种</el-checkbox>
                <el-checkbox label="LOGISTICS_NO">物流单号</el-checkbox>
                <el-checkbox label="USER_ACTUAL_PRICE">C端实付总金额</el-checkbox>
                <el-checkbox label="CURRENCY_RATES">订单汇率</el-checkbox>
                <el-checkbox label="PROMOTION_ID">促销活动编码</el-checkbox>
                <el-checkbox label="PAY_STATUS">付款状态</el-checkbox>
                <el-checkbox label="PAY_WAY">支付方式</el-checkbox>
                <el-checkbox label="REMARK">订单备注</el-checkbox>
                <el-checkbox label="SHOP_CODE">门店编码</el-checkbox>
                <el-checkbox label="SHOP_NAME">门店名称</el-checkbox>
                <el-checkbox label="PICTURE_ID">图片编码</el-checkbox>
              </el-checkbox-group>

              <!-- 旧 -->
              <!-- <el-checkbox-group
                style="margin-bottom: 10px"
                v-model="baseInfos"
                @change="handleCheckedChange">
                <el-checkbox label="orderNo">{{
                  $t("ExportOrder.OrderNo")
                }}</el-checkbox>
                <el-checkbox label="orderTime">{{
                  $t("ExportOrder.OrderTime")
                }}</el-checkbox>
                <el-checkbox label="orderStatus">{{
                  $t("ExportOrder.OrderStatus")
                }}</el-checkbox>
                <el-checkbox label="userName">{{
                  $t("ExportOrder.UserName")
                }}</el-checkbox>
                <el-checkbox label="itemCode">{{
                  $t("ExportOrder.Code")
                }}</el-checkbox>
                <el-checkbox label="itemName">{{
                  $t("ExportOrder.Description")
                }}</el-checkbox>
                <el-checkbox label="sku">{{
                  $t("ExportOrder.SKU")
                }}</el-checkbox>
                <el-checkbox label="specName">{{
                  $t("ExportOrder.Specification")
                }}</el-checkbox>
                <el-checkbox label="color">{{
                  $t("ExportOrder.Color")
                }}</el-checkbox>
                <el-checkbox label="memberPrice">{{
                  $t("ExportOrder.MembershipPrice")
                }}</el-checkbox>
                <el-checkbox label="discountPrice">{{
                  $t("ExportOrder.DiscountPrice")
                }}</el-checkbox>
                <el-checkbox label="quantity">{{
                  $t("ExportOrder.Quantity")
                }}</el-checkbox>
                <el-checkbox label="deliveredQuantity">{{
                  $t("ExportOrder.DeliveredQuantity")
                }}</el-checkbox>
                <el-checkbox label="unsentQuantity">{{
                  $t("ExportOrder.UnsentQuantity")
                }}</el-checkbox>
                <el-checkbox label="goodsStatus">{{
                  $t("ExportOrder.GoodsStatus")
                }}</el-checkbox>
                <el-checkbox label="total">{{
                  $t("ExportOrder.Total")
                }}</el-checkbox>
                <el-checkbox label="consigneName">{{
                  $t("ExportOrder.ConsigneName")
                }}</el-checkbox>
                <el-checkbox label="shippingAddress">{{
                  $t("ExportOrder.ShippingAddress")
                }}</el-checkbox>
                <el-checkbox label="zipCode">{{
                  $t("ExportOrder.ZipCode")
                }}</el-checkbox>
                <el-checkbox label="tel">{{
                  $t("ExportOrder.Tel")
                }}</el-checkbox>
                <el-checkbox label="phoneNumber">{{
                  $t("ExportOrder.PhoneNumber")
                }}</el-checkbox>
                <el-checkbox label="deliveryMethod">{{
                  $t("ExportOrder.DeliveryMethod")
                }}</el-checkbox>
                <el-checkbox label="goodsWeight">{{
                  $t("ExportOrder.GoodsWeight")
                }}</el-checkbox>
                <el-checkbox label="paymentMethod">{{
                  $t("ExportOrder.PaymentMethod")
                }}</el-checkbox>
                <el-checkbox label="shippingAddressDetail">{{
                  $t("ExportOrder.shippingAddressDetail")
                }}</el-checkbox>
                <el-checkbox label="totalAmount">{{
                  $t("ExportOrder.TotalAmount")
                }}</el-checkbox>
                <el-checkbox label="deliveryFee">{{
                  $t("ExportOrder.DeliveryFee")
                }}</el-checkbox>
                <el-checkbox label="totalOrderAmount">{{
                  $t("ExportOrder.TotalOrderAmount")
                }}</el-checkbox>
                <el-checkbox label="remarks">{{
                  $t("ExportOrder.Remarks")
                }}</el-checkbox>
                <el-checkbox label="logisticsNo">{{
                  $i18n.locale === "zh" ? "快递编号" : "Tracking No."
                }}</el-checkbox>
                <el-checkbox label="pictureId">{{
                  $i18n.locale === "zh"
                    ? "定制图片编码"
                    : "Customized Picture No."
                }}</el-checkbox>
                <el-checkbox label="modelName">{{
                  $i18n.locale === "zh" ? "定制型号" : "Customized Model"
                }}</el-checkbox>
              </el-checkbox-group> -->
            </div>
          </div>
        </el-form-item>
       
       
      </el-form>

      <div style="text-align: center; margin-bottom: 10px">
        <el-button class="mini-search-btn" @click="exportOrder">{{
          $t("Message.Confirm")
        }}</el-button>
        <el-button
          style="margin-left: 20px"
          class="mini-pulloff-btn"
          @click="closeExportOrder"
          >{{ $t("Message.Cancel") }}</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Vue from "vue";
import Header from "@/components/Header.vue";
import Left from "@/components/Left.vue";
import { formatDate, dateToNum, } from "@/assets/js/common.js";
import onceAgain from "@/components/onceAgain.vue";
import GD from "@/assets/js/globalData";
import loading from "@/components/loading.vue";
import countDown from "@/components/countDown.vue";
import { getToken } from '@/utils/auth'
import axios from 'axios'

// liu--
import { orderLists,orderTypeList,orderonemore,shoppingcartList,orderExport} from '@/apiService/api'
export default {
  name: "OrderManage",
  components: { Header, Left, onceAgain, loading, countDown },
  data() {
    return {
      userState: 2,
      totalCount: 0,
      cur_page: 1,
      pagesize: 10,
      onceAgainDialogVisible: false,
      showLoading: false,
      onWayAttendEventFlag: 0, // 在途商品是否参与活动 1.参与，0.不参与
      customizedAttendEventFlag: 0, // 定制商品是否参与活动 1.参与，0.不参与
      mtoAttendEventFlag: 0, // mto订单是否参与活动 1.参与，0.不参与
      tradeType: false, // 是否显示交易状态
      showcov: false, // 是否显示物流信息
      orderStatus: 0, // 订单状态
      orderStatusText: "全部订单",
      senior: false, // 订单高级搜索显隐
      currentOrderId: "",
      orderList: [],
      orderNo: "",
      name: "",
      mobile: "",
      phone: "",
      address: "",
      paginationShow: true, // 分页控制
      startTime: new Date(),
      endTime: new Date(),
      orderMType: "", // 订单类型
      orderType: "",
      filters: {
        column: {
          starDate: "",
          endDate: "",
        },
      },
      dialogVisible: false, // 再来一单选择框显隐--无法购买
      dialogItemList: [], // 再来一单弹框显示货品
      addShopCarItemList: [], // 再来一单加入购物车货品
      pickerStarDate: {
        disabledDate: (time) => {
          let beginDateVal = this.endTime;
          if (beginDateVal) {
            return time.getTime() > beginDateVal;
          }
        },
      },
      pickerEndDate: {
        disabledDate: (time) => {
          let beginDateVal = this.filters.column.starDate;
          if (beginDateVal) {
            return time.getTime() < beginDateVal;
          }
        },
      },
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: "zh-RMB", // 语种
      withoutActivityItem: [],  //未参与活动的订单
      activityPromotions: [],  //有活动的订单
      showExport: false,
      isAllIndeterminate: false,
      isAll: false,
      allBaseInfos: [
        "orderNo",
        "orderTime",
        "orderStatus",
        "userName",
        "itemCode",
        "itemName",
        "sku",
        "specName",
        "color",
        "memberPrice",
        "discountPrice",
        "quantity",
        "deliveredQuantity",
        "unsentQuantity",
        "goodsStatus",
        "total",
        "consigneName",
        "shippingAddress",
        "zipCode",
        "tel",
        "phoneNumber",
        "deliveryMethod",
        "goodsWeight",
        "paymentMethod",
        "shippingAddressDetail",
        "totalAmount",
        "deliveryFee",
        "totalOrderAmount",
        "remarks",
        "logisticsNo",
        "pictureId",
        "modelName",
      ],
      initBaseInfosZH: [
        "orderNo",
        "orderTime",
        "orderStatus",
        "userName",
        "itemCode",
        "itemName",
        "sku",
        "discountPrice",
        "quantity",
        "deliveredQuantity",
        "unsentQuantity",
        "goodsStatus",
        "total",
        "consigneName",
        "memberPrice",
      ],
      initBaseInfosEN: [
        "orderNo",
        "orderTime",
        "orderStatus",
        "userName",
        "itemCode",
        "itemName",
        "sku",
        "discountPrice",
        "quantity",
        "deliveredQuantity",
        "unsentQuantity",
        "goodsStatus",
        "total",
        "consigneName",
        "memberPrice",
      ],
      baseInfos: [],
      isAllExportGoods: 1,
      deliverStatus:[], //发货状态
      payStatusList:[],  //付款状态
      orderStatusList:[],
      recentDays: 1,  //制定类型时间
      form: {
        distributorId:localStorage.getItem('userId'),
        activityFlag:'',  //是否参与活动
        orderTypeIdList:[],  //订单类型
        orderStatusList:[], //订单状态列表
        deliverStatus:[], //发货状态
        payStatusList:[],  //付款状态
        itemCode:'',  //货品编码
        fieldList: [],
        startTime:'', 
        endTime:'',
      },
      //全部 导出字段
      fieldList:[
          'SHOP_CODE',
          'SHOP_NAME',
          'CREATE_TIME',
          // 'ORDER_ERP_NO',
          'ITEM_CODE',
          // 'ORDER_FACTORY_NO',
          'SALE_PRICE',
          'ORDER_NO',
          // 'ERP_OUTBOUND_NO',
          'RECEIVE_ADDRESS',
          'ITEM_NAME',
          // 'ORDER_THIRDPARTY_NO',
          'ACTUAL_PRICE',
          'ORDER_TYPE_ID',
          // 'ORDER_DELIVER_BILL_ID',
          'CONTACT_MOBILE',
          'ITEM_COUNT',
          'MATERIAL_NAME',
          'ACTUAL_PRICE_SUM',
          'SALE_PRICE_SUM',
           'ORDER_STATUS',
           'DISTRIBUTION_NAME',
            'DELIVER_COUNT',
            'MODEL_NAME',
            'CURRENCY_TYPE',
            'LOGISTICS_NO',
            'USER_ACTUAL_PRICE',
            'CURRENCY_RATES',
            'PROMOTION_ID',
            'PAY_STATUS',
            'PAY_WAY',
            'REMARK',
            'PICTURE_ID'
      ],
      deliverTimes: [new Date().getTime(), new Date().getTime()],
      orderTypes: [], // 订单类型列表
      exchange:1, //汇率
      erpNo:localStorage.getItem('erpNo'),  //售后编码
    };
  },
  filters: {
    formatDate(time) {
      var date = new Date(time);
      return formatDate(date, "yyyy-MM-dd hh:mm:ss");
    },
  },
  methods: {
    changeFormPage(val) {
      this.form.page = val;
    },
    changeFormCount(val) {
      this.form.count = val;
    },
    // 导出--订单类型--all
    orderTypeIdAllChangeFun(val){
       let list=this.orderTypes;
       let arrId=[];
       for(let i=0;i<list.length;i++){
          arrId.push(list[i].id);
       }
       this.form.orderTypeIdList=val?arrId:[];
    },

    // 导出--订单状态--all
    orderStatusAllChangeFun(val){
      this.orderStatusList=val?[1,2,3,4,5,6,7]:[];
    },
    // 导出--发货状态--all
    orderFahuoAllChangeFun(val){
      this.deliverStatus=val?[1,2,3,4]:[];
    },
    // 导出--支付状态--all
    orderPayAllChangeFun(val){
      this.payStatusList=val?[1,2,3,4,5,6]:[];
    },

    // 导出订单--Y
    exportOrder2() {
      console.log('订单导出：',this.form);
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
      if (this.form.recentDays === 0) {
        if (Number(this.form.timeType) === 1) {
          this.form.deliverStartTime = this.deliverTimes[0];
          this.form.deliverEndTime = this.deliverTimes[1];
        } else {
          this.form.startTime = this.deliverTimes[0];
          this.form.endTime = this.deliverTimes[1];
        }
      } else {
        this.form.deliverStartTime = undefined;
        this.form.deliverEndTime = undefined;
        this.form.startTime = undefined;
        this.form.endTime = undefined;
      }
      this.form.exportFields = "";
      if (this.baseInfos.length > 0) {
        this.form.exportFields = this.baseInfos.join(",");
      }
      if (this.form.exportFields === "") {
        this.$message.warning(this.$t("ExportOrder.SelectAtLeastOneField"));
        return;
      }
      if (
        this.orderNo !== undefined &&
        this.orderNo !== null &&
        this.orderNo !== ""
      ) {
        this.form.orderIdQryStr = this.orderNo;
      } else {
        this.form.orderIdQryStr = undefined;
      }
      
      if (this.name !== undefined && this.name !== null && this.name !== "") {
        this.form.name = this.name;
      } else {
        this.form.name = undefined;
      }
      if (
        this.mobile !== undefined &&
        this.mobile !== null &&
        this.mobile !== ""
      ) {
        this.form.mobile = this.mobile;
      } else {
        this.form.mobile = undefined;
      }
      if (
        this.phone !== undefined &&
        this.phone !== null &&
        this.phone !== ""
      ) {
        this.form.phone = this.phone;
      } else {
        this.form.phone = undefined;
      }
      if (
        this.address !== undefined &&
        this.address !== null &&
        this.address !== ""
      ) {
        this.form.address = this.address;
      } else {
        this.form.address = undefined;
      }
      this.form.tradeStatus = 0;
      if (this.orderStatus !== undefined) {
        this.form.tradeStatus = this.orderStatus;
      }
      if (this.form.exportFields != undefined && this.form.exportFields != 0) {
        this.showExport = false;
        this.showLoading = true;
        this.$message.success(this.$t("ExportOrder.ExportRequest"));
        // this.$api
        //   .exportData(this, "user/u/user/exportOrderExcel", this.form)
          orderExport(this.form).then((res) => {
            if (res === undefined || res.code == undefined) {
              this.timeData = []; // ..成功下载Excel后初始化选中时间
              // this.formData.status = [] //..成功夏侯Excel后初始化订单状态
            }
            const content = res;
            let blob = new Blob([content], {
              type: "application/ms-excel",
            });
            let url = window.URL.createObjectURL(blob);
            if ("download" in document.createElement("a")) {
              let link = document.createElement("a");
              link.style.display = "none";
              link.href = url;
              link.setAttribute(
                "download",
                this.$t("ExportOrder.ExportOrderDetails") + ".xls"
              );
              document.body.appendChild(link);
              link.click();
            } else {
              navigator.msSaveBlob(
                blob,
                this.$t("ExportOrder.ExportOrderDetails") + ".xls"
              );
            }
            this.showLoading = false;
          });
      } else {
        this.$message.warning(this.$t("UserCenter.PleaseSelect"));
      }
    },

    // 获取几日后的时间
    getTiemDateFun(num){
        let date1=new Date();
        let date2 = new Date(date1);
        date2.setDate(date1.getDate() - num);
        let year=date2.getFullYear();
        let month=date2.getMonth() + 1;
        month=(month<10)?('0'+month):month;
        let day=date2.getDate();
        day=(day<10)?('0'+day):day;
        let hours=date2.getHours();
        hours=(hours<10)?('0'+hours):hours;
        let Minutes=date2.getMinutes();
        Minutes=(Minutes<10)?('0'+Minutes):Minutes;
        let Second=date2.getSeconds();
        Second=(Second<10)?('0'+Second):Second
        return (year + "-" + month+ "-" +day+ " "+ hours+":"+Minutes+":"+Second);
    },

    // 把时间戳转化为yy-mm-dd hh:mm:ss
    getTimestampFun(dateTime){
        let date2=new Date(dateTime);
        let year=date2.getFullYear();
        let month=date2.getMonth() + 1;
        month=(month<10)?('0'+month):month;
        let day=date2.getDate();
        day=(day<10)?('0'+day):day;
        let hours=date2.getHours();
        hours=(hours<10)?('0'+hours):hours;
        let Minutes=date2.getMinutes();
        Minutes=(Minutes<10)?('0'+Minutes):Minutes;
        let Second=date2.getSeconds();
        Second=(Second<10)?('0'+Second):Second
        return (year + "-" + month+ "-" +day+ " "+ hours+":"+Minutes+":"+Second);
    },

    // 日期选择确定的操作
    dateConfirmChange(){
        
        let time1=this.deliverTimes[0]/ 1000;
        let time2=this.deliverTimes[1]/ 1000;
        var s = time1 - time2;
        if (s < 0) {
          s = Math.abs(s);
        }
         console.log(parseInt(s / 2592000) );
         let monthNum=parseInt(s / 2592000);
         if(monthNum>2){
            this.$message('最多只能导出两个月的订单数据！')
            this.deliverTimes=[new Date().getTime(), new Date().getTime()];
         }
        
    },
    // 导出订单--Y
    exportOrder() {
      console.log('订单导出：',this.form);
      if (this.recentDays === 0) {
        this.form.startTime =this.getTimestampFun(this.deliverTimes[0]) ;
        this.form.endTime = this.getTimestampFun(this.deliverTimes[1]);
      } else {
        this.form.startTime = this.getTiemDateFun(this.recentDays); 
        this.form.endTime =this.getTiemDateFun(0) ;
      }
      if (this.form.fieldList.length ==0) {
        this.$message.warning(this.$t("ExportOrder.SelectAtLeastOneField"));
        return;
      }
      this.form.activityFlag=this.form.activityFlag==0?'':(this.form.activityFlag==1?true:false);
      if(this.form.orderTypeId==0){
          this.form.orderTypeId='';
      }
      this.form.orderStatusList=this.orderStatusList;
      this.form.deliverStatus=this.deliverStatus; //发货状态
       this.form.payStatusList=this.payStatusList;  //付款状态

      if (this.form.fieldList != undefined && this.form.fieldList != 0) {
        this.showExport = false;
        this.showLoading = true;
        this.$message.success(this.$t("ExportOrder.ExportRequest"));
        let url=this.locationUrl_L + 'order/v1/web/admin/orderInfo/export';
        axios({
          method: 'post',
          url: url,
          data: this.form,
          responseType: 'arraybuffer',
          headers: {
            'Content-Type': 'application/json',
            token: getToken()
          }
        }).then((res) => { 
            // let enc = new TextDecoder('utf-8')
            // let resData = JSON.parse(enc.decode(new Uint8Array(res.data))) 
            // if(!resData.success){
            //    this.$message(resData.errMessage);
            //    this.showLoading = false;
            // }else{
                
                const content = res.data;
                let blob = new Blob([content], {
                  type: "application/ms-excel",
                });
                let url = window.URL.createObjectURL(blob);
                if ("download" in document.createElement("a")) {
                  let link = document.createElement("a");
                  link.style.display = "none";
                  link.href = url;
                  link.setAttribute(
                    "download",
                    this.$t("ExportOrder.ExportOrderDetails") + ".xls"
                  );
                  document.body.appendChild(link);
                  link.click();
                } else {
                  navigator.msSaveBlob(
                    blob,
                    this.$t("ExportOrder.ExportOrderDetails") + ".xls"
                  );
                }
                this.showLoading = false;
            // }
          });
      } else {
        this.$message.warning(this.$t("UserCenter.PleaseSelect"));
      }
    },
    // 导出
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
              if (item.recordType === 3) {
                // 前台订单导出
                if (item.language === 1) {
                  // 1.中文  2.英文
                  this.initBaseInfosZH = item.exportFields.split(",");
                } else {
                  this.initBaseInfosEN = item.exportFields.split(",");
                }
              }
            });
          }
          this.getBaseInfos();
        });
    },
    
   
    getBaseInfos() {
      this.baseInfos = [];
      if (this.$i18n.locale === "en") {
        this.baseInfos = this.initBaseInfosEN;
      } else {
        this.baseInfos = this.initBaseInfosZH;
      }
      this.isAllIndeterminate = !(
        this.baseInfos.length === this.allBaseInfos.length ||
        this.baseInfos.length === 0
      );
      this.isAll = this.baseInfos.length === this.allBaseInfos.length;
    },
    handleCheckedChange(value) {
      this.isAllIndeterminate = !(
        this.baseInfos.length === this.allBaseInfos.length ||
        this.baseInfos.length === 0
      );
      this.isAll = this.baseInfos.length === this.allBaseInfos.length;
    },
    handleCheckAllChange(val) {
      
      this.isAllIndeterminate = false;
      this.form.fieldList=val?this.fieldList:[];
    },
    closeExportOrder() {
      this.showExport = false;
    },
    showExportOrder() {
      this.showExport = true;
      this.deliverStatus=[];
      this.payStatusList=[];
      this.orderStatusList=[];
    },
    fLangChange(value) {
      window.localStorage.setItem("bLang", value);
      this.$i18n.locale = value.split("-")[0];
    },
     // 订单状态--Y
    confirmOrderStatus(row) {
    //  前端订单状态 0.全部 1.待确认 2待发货 3部分发货 4待收货（已发货） 5已关闭 6已完成 7 待付款
      switch (row) {
        case 1:
          return this.$t("UserCenter.ToBeConfirmed"); //待确认
        case 2:
          return this.$t("UserCenter.ToBeShipped");  //待发货
        case 3:
          return this.$t("UserCenter.PartOfShipped");  //部分发货
        case 4:
          return this.$t("UserCenter.Shipped");  //已发货
        case 5: 
          return this.$t("UserCenter.Closed");  //已关闭
        case 6:
          return this.$t("UserCenter.Completed"); //已完成
        case 7:
          return this.$t("UserCenter.PendingPayment");  //待付款
        case 9:
          return this.$t("UserCenter.InTheOutbound");  //待付款  
      }
    },
    getPayStatus(row) {
      // 订单付款状态
      switch (row) {
        case 1:
          return this.$t("UserCenter.PendingPayment");
        case 2:
          return this.$t("UserCenter.PartOfPayment");
        case 3:
          return this.$t("UserCenter.Paid");

        case 5:
          return this.$t("UserCenter.PartOfRefunded");
        case 6:
          return this.$t("UserCenter.Refunding");
      }
    },
    // 高级搜索显隐
    showSenior() {
      this.senior = !this.senior;
    },
    // 当前页码
    handleCurrentChange(val) {
      this.cur_page = val;
      this.getOrderList();
    },
    // 每页条数
    handleSizeChange(val) {
      this.pagesize = val;
      this.getOrderList(); 
    },
    dateToNum,
    
    getTradeStateType() {
      this.tradeType = !this.tradeType;
    },
    checkLog() {
      this.showcov = true;
    },
    shutLog() {
      this.showcov = false;
    },
    // 前往售后申请
    goAfterApplicate() {
      this.$router.push({ name: "AfterApplication" });
    },
    // 改变订单状态（查询）--Y
    changeState(type) {
      this.paginationShow = false;
      this.orderStatus = type;
      this.cur_page = 1;
      this.getOrderList()
      this.$nextTick(function () {
        this.paginationShow = true;
      });
    },
    // 活动订单状态（查询）
    getOrderStatus(type) {
      this.orderStatus = type;
      this.tradeType = false;
      this.cur_page = 1;
      if (type === 0) {
        this.orderStatusText = "全部订单";
      } else if (type === 1) {
        this.orderStatusText = "待付款";
      } else if (type === 2) {
        this.orderStatusText = "待发货";
      } else if (type === 3) {
        this.orderStatusText = "已发货";
      } else if (type === 4) {
        this.orderStatusText = "已完成";
      } else if (type === 5) {
        this.orderStatusText = "已退款";
      } else if (type === 6) {
        this.orderStatusText = "已关闭";
      }
    },
    // 获取订单列表
    getOrderList(startTime,endTime) {
      this.orderList =[];
      let userId=localStorage.getItem('userId');
      let params={
        orderNo:String(this.orderNo)!=''?String(this.orderNo):null,  //订单编号
        userName:this.name!=''?this.name:null,  //收货人
        orderTypeId:this.orderMType!=''?this.orderMType:null, //订单类型id
        phone:this.phone!=''?this.phone:null,  //固定电话
        mobile:this.mobile!=''?this.mobile:null,  //手机号
        address:this.address!=''?this.address:null,  //地址
        startTime:startTime!=''?startTime:null, //开始时间
        endTime:endTime!=''?endTime:null, //结束时间
        deliverStatus:0,  //发货状态 0.全部 1.未发货2.出库中 3.部分发货 4.已发货 (如果订单状态为已确认 才判断发货状态)
        frontOrderStatus:this.orderStatus, //订单状态 0.全部 1.待确认 2待发货 3部分发货 4待收货 5已关闭 6已完成  7 待付款
        distributorId:userId,
        page:this.cur_page,
        size:this.pagesize,
      }
      orderLists(params).then(res=>{    
          if(res.success){
             console.log('订单列表：',res.data.list);
             this.orderList = res.data.list;
             this.totalCount =res.data.total;
          }
          
      })
      
    },

    formatNumber(n) {
      n = n.toString()
      return n[1] ? n : '0' + n
    },

    formatTime (number) {
        let formateArr = ['Y', 'M', 'D', 'h', 'm', 's'];
        let returnArr = [];
        let date = new Date(number);
        let year=date.getFullYear();
        let month=this.formatNumber(date.getMonth() + 1);
        let day=this.formatNumber(date.getDate());
        let hours=this.formatNumber(date.getHours());
        let Minutes=this.formatNumber(date.getMinutes());
        let Seconds=this.formatNumber(date.getSeconds());
        let format =year+'-'+month+'-'+day+" "+hours+":"+Minutes+":"+Seconds; 
        return format;
    },

    
   
    // 订单查询--Y
    queryOrder() {
      this.paginationShow = false;
      this.cur_page = 1;
      let endDateFlag=(this.filters.column.endDate !== undefined &&this.filters.column.endDate !== null &&this.filters.column.endDate !== "");
      let endTimes = endDateFlag?this.formatTime(this.filters.column.endDate):'';
      let startDateFlag=(this.filters.column.starDate !== undefined &&this.filters.column.starDate !== null &&this.filters.column.starDate !== "");
      let startTimes = startDateFlag?this.formatTime(this.filters.column.starDate):'';  
      this.orderStatus = 0;  //订单状态
      this.getOrderList(startTimes,endTimes);
      this.$nextTick(function () {
        this.paginationShow = true;
      });
    },
    // 订单详情--y
    goOrderDetail(id) {
      this.$router.push({ name: "WaitPayment", query: { order_id: id } });
    },
    confirmDialog() {
      this.onceAgainDialogVisible = true;
      this.dialogVisible = false;
      this.activityPromotions.forEach((item, index) => {
        if (
          (item.saleStatus !== 3 || item.stockItemCount === 0) &&
          item.advanceSaleFlag === 0
        ) {
          this.activityPromotions.splice(index, 1);
        } else if (
          item.stockItemCount > 0 &&
          item.stockItemCount < item.count
        ) {
          item.count = item.stockItemCount;
          item.showNumInWarehouse = item.numInWarehouse;
          item.showStockItemCount = item.count - item.numInWarehouse;
        }
      });
      this.withoutActivityItem.forEach((item, index) => {
        if (
          (item.saleStatus !== 3 || item.stockItemCount === 0) &&
          item.advanceSaleFlag === 0
        ) {
          this.withoutActivityItem.splice(index, 1);
        } else if (
          item.stockItemCount > 0 &&
          item.stockItemCount < item.count
        ) {
          item.count = item.stockItemCount;
          item.showNumInWarehouse = item.numInWarehouse;
          item.showStockItemCount = item.count - item.numInWarehouse;
        }
      });
      if (
        this.activityPromotions.length === 0 &&
        this.withoutActivityItem.length === 0
      ) {
        if (this.$i18n.locale === "zh") {
          this.$message.warning("无可购买的商品，无法再来一单!");
        } else {
          this.$message.warning("No purchasable products and No more orders.");
        }
      }
    },
   

    // 再来一单
    orderAgain(id) {   
      this.activityPromotions = []; // 清空
      this.dialogItemList = [];
      this.showLoading = true;
      this.onceAgainDialogVisible = false; 
      orderonemore({id:id})
        .then((res) => {
          if (res.success) {  
            // this.onWayAttendEventFlag = res.onWayAttendEventFlag; // 在途商品是否参与活动
            // this.customizedAttendEventFlag = res.customizedAttendEventFlag; // 定制商品是否参与活动
            // this.mtoAttendEventFlag = res.mtoAttendEventFlag; // mto订单是否参与活动
            //withoutActivityItem: [],  //未参与活动的订单
            //activityPromotions: [],  //有活动的订单
            let list=res.data.filter(item=>item.itemType==1);
            let proList=[];  //促销活动列表
            let groupList=[]; //拼团秒杀活动货品列表
            list.forEach(item=>{
              let activityList=[];
              if((item.goodsPromotionId&&item.promotions)||(item.groupSeckillId&&item.groupSeckills)){
                 // 促销活动  
                 if(item.goodsPromotionId&&item.promotions){ 
                    item.promotions.forEach(proItem=>{        
                       proItem.rules.forEach(ruleItem=>{
                          if(ruleItem.id==item.goodsPromotionId){
                             this.$set(item,'ruleName',ruleItem.ruleName)
                             this.$set(item,'conditions',ruleItem.conditions)           
                          }
                          this.$set(ruleItem,'rulesId',ruleItem.id);
                          this.$set(ruleItem,'ruleType',1); //活动类型  1：促销 2：拼团
                          this.$set(ruleItem,'isChecked',false)
                          activityList.push(ruleItem);
                       })
                    })
                    
                    proList.push(item);
                 }else{  //拼团
                    item.groupSeckills.forEach(groupItem=>{
                       if(groupItem.id==item.groupSeckillId){
                          this.$set(item,'ruleName',groupItem.name)
                          this.$set(item,'conditions',[])
                       }
                       this.$set(groupItem,'rulesId',groupItem.groupSeckillId);
                       this.$set(groupItem,'ruleName',ggroupItem.name);
                       this.$set(ruleItem,'ruleType',2);  //活动类型  1：促销 2：拼团
                       this.$set(groupItem,'isChecked',false)
                       activityList.push(groupItem);
                    })
                    groupList.push(item);
                 }
                //  this.activityPromotions.push(item);  ////有活动的订单
              }else{
                this.$set(item,'goodsPromotionId','no');
                if(item.promotions&&item.promotions.length>0){
                    item.promotions.forEach(proItem=>{        
                       proItem.rules.forEach(ruleItem=>{
                          this.$set(ruleItem,'rulesId',ruleItem.id);
                          this.$set(ruleItem,'ruleType',1); //活动类型  1：促销 2：拼团
                          this.$set(ruleItem,'isChecked',false)
                          activityList.push(ruleItem);
                       })
                    })
                }

                if(item.groupSeckills&&item.groupSeckills.length>0){
                    item.groupSeckills.forEach(groupItem=>{
                       this.$set(groupItem,'rulesId',groupItem.groupSeckillId);
                       this.$set(groupItem,'ruleName',ggroupItem.name);
                       this.$set(ruleItem,'ruleType',2);  //活动类型  1：促销 2：拼团
                       this.$set(groupItem,'isChecked',false)
                       activityList.push(groupItem);
                    })
                }
                proList.push(item);
                this.withoutActivityItem.push(item);  //未参与活动的订单  
              }
              this.$set(item,'activityList',activityList);  //切换活动的活动列表
            })

            // 拼团秒杀  
              if(groupList.length>0){ 
                  let newGroup=  this.sort_pro(groupList ,['groupSeckillId']);
                  newGroup.forEach(grItem=>{
                      this.$set(grItem,'ruleName',grItem.children[0].ruleName);
                      this.$set(grItem,'conditions',[]);
                  })
                  this.activityPromotions=[...this.activityPromotions,...newGroup];
                   
              }

            if(proList.length>0){
                // 把没有活动的商品排序到后面
                let proList1=[]; //没活动的商品
                let proList2=[]; //有活动的商品
                let newPlist=[]; //新组合     
                proList.forEach(pItem=>{
                    if(pItem.goodsPromotionId=='no'){
                      proList1.push(pItem);
                    }else{
                      proList2.push(pItem);
                    }
                })
                newPlist=[...proList2,...proList1];
                let newList=this.sort_pro(newPlist ,['goodsPromotionId']);
                newList.forEach(newItem=>{
                   if(newItem.goodsPromotionId!='no'){
                       this.$set(newItem,'ruleName',newItem.children[0].ruleName);
                       this.$set(newItem,'conditions',newItem.children[0].conditions);
                   }else{
                       this.$set(newItem,'ruleName','未参加活动');
                       this.$set(newItem,'conditions',[]);
                   }
                   
                })
                this.activityPromotions=[...this.activityPromotions,...newList];
            }

            


            console.log('没参加活动：',this.withoutActivityItem);
            console.log('参加活动：',this.activityPromotions);
            this.showLoading = false;
            if (this.dialogItemList.length > 0) {
              this.dialogVisible = true;
              return false;
            }
            this.onceAgainDialogVisible = true;
          } else  {
            this.showLoading = false;
            this.$message(res.errMessage);
          } 
        })
        
    },
    // 把对象数组按照某一个属性（或某几个属性）进行分类-Y
    sort_pro(data, keys = []) {     //keys可以传一个数组
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
            children: [element]
          });
          d[element_keyStr] = element;
        } else {
          for (var ele of c) {
            let isTrue = keys.some(key => {
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
    
    // 关闭切换活动-liu
    shutDialog(target) {
      if (target) {
        this.onceAgainDialogVisible = false;
      }
    },
    // 取消订单
    cancelOrder(id) {
      let info = "";
      if (this.$i18n.locale === "zh") {
        info = "此操作将取消该订单, 是否继续?";
      } else {
        info = "The order will be canceled. Do you want to continue";
      }
      this.$confirm(info, this.$t("P.Prompt"), {
        confirmButtonText: this.$t("P.OK"),
        cancelButtonText: this.$t("P.Cancel"),
        type: "warning",
      })
        .then(() => {
          this.$api.put(this, "user/u/order/cancel", { id: id }).then((res) => {
            if (res.code === 0) {
              if (this.$i18n.locale === "zh") {
                this.$message.success("取消订单成功");
              } else {
                this.$message.info("Cancel orders successfully");
              }
              this.getOrderList();
             
            } 
          });
        })
        .catch(() => {
          if (this.$i18n.locale === "zh") {
            this.$message.info("已取消删除");
          } else {
            this.$message.info("Deletion canceled");
          }
        });
    },
    // 获取订单类型-Y
    getOrderTypes() {
      orderTypeList({page:1,size:100}).then((res) => {
        if(res.success){
          console.log('订单类型：',res.data.list);
           this.orderTypes = res.data.list;
        } 
      });
    },
    // 查询购物车列表
    shoppingcartListFun(){
        shoppingcartList().then(res=>{
          if(res.success){
              this.$refs.header.cartCount = res.data.length;
          }
        })
    },
  },
  created() {
    this.exchange=Number( localStorage.getItem('exchange')); 
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem("bLang")
      ? window.localStorage.getItem("bLang")
      : "zh-RMB";
    this.getOrderList();
    
    // this.getLastChoose(3);
    this.getOrderTypes();
  },
};
</script>

<style scoped="scoped" lang='less'>
@import url("../../assets/less/variable.less");
.om-orderStatus-checkbox{
   display: flex;
   .om-orderStatus-all{
     margin-right: 15px;
   }
}
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
  .user-right-title2 {
    padding-bottom: 10px;
    border-bottom: 1px solid @bdLightColor;
   
    display: flex;
    align-items: center;
    justify-content: space-between;
    h6{
      font-size: 20px;
    }
  }
  .order-list-wrap {
    padding: 24px 40px 0;
    border: 2px solid @bdLightColor;
    border-radius: 8px;
  }
  .query {
    margin-bottom: 25px;
    .search {
      .item {
        input[type="text"] {
          padding-left: 10px;
          width: 140px;
          height: 38px;
          font-size: 12px;
          box-sizing: border-box;
          border: 1px solid @bdLighterColor;
          border-radius: 4px;
        }
        input[type="button"] {
          padding: 0 12px;
          height: 38px;
          line-height: 38px;
          box-sizing: border-box;
          border-radius: 4px;
        }
      }
      .select {
        /deep/ .el-input__inner {
          width: 180px;
        }
      }
    }
    .search-info {
      .items {
        .common-input {
          input {
            padding-left: 10px;
            width: 140px;
            height: 38px;
            font-size: 12px;
            box-sizing: border-box;
            border: 1px solid @bdLighterColor;
            border-radius: 4px;
          }
          &.addr-input {
            input {
              width: 180px;
            }
          }
        }
        &.date-items {
          line-height: 40px;
        }
      }
      /deep/ .el-date-editor.el-input,
      .el-date-editor.el-input__inner {
        width: 200px;
      }
    }
    /deep/ .el-input__inner {
      height: 38px;
      font-size: 12px;
      border: 1px solid @bdLighterColor;
      border-radius: 4px;
    }
  }
  button {
    padding: 0 16px;
    height: 40px;
    line-height: 40px;
    border: none;
  }
  .search-btn {
    background-color: @blue;
    &:hover,
    &:active,
    &:focus {
      color: @white;
      opacity: 0.6;
    }
  }
  .query-detail {
    .nav {
      margin-bottom: 15px;
      width: 100%;
      height: 40px;
      line-height: 40px;
      ul {
        overflow: hidden;
        background-color: @bdLightColor;
        border-radius: 4px;
        &.navEn {
          li {
            width: auto;
            padding: 0 12px;
            font-size: 13px;
          }
        }
        li {
          float: left;
          width: 93px;
          cursor: pointer;
          font-size: 14px;
          color: @lighterBlack;
          text-align: center;
          &:hover,
          &.current {
            color: @white;
            background-color: @blue;
          }
        }
      }
    }
    .export-btn {
      padding-top: 24px;
      color: @blue;
      line-height: 1;
      cursor: pointer;
      .iconfont {
        margin-right: 2px;
        color: @lighterGray;
      }
      &:hover {
        opacity: 0.6;
      }
    }
    .table {
      width: 100%;
      margin-bottom: 30px;
      table {
        width: 100%;
        word-wrap: break-word;
        word-break: break-all;
        border-collapse: collapse;
        tr {
          &.goods-list:hover {
            background-color: @lightGrayBg;
          }
          & + tr {
            border-top: 1px dashed @bdLighterColor;
          }
          th {
            height: 30px;
            line-height: 30px;
            text-align: center;
            background-color: @bdLightColor;
            font-size: 12px;
            color: @gray;
            font-weight: normal;
          }
          td {
            height: 50px;
            text-align: center;
            font-size: 12px;
            color: @lightBlack;
            &.empty {
              width: 100%;
              font-size: 16px;
              text-align: center;
            }
            .views {
              min-width: 65px;
              span {
                display: block;
              }
              .log {
                width: 100%;
                font-size: 12px;
                color: @blue;
                &:hover {
                  opacity: 0.6;
                }
              }
            }
            .countdown-box {
              width: 100%;
              margin-top: 12px;
              text-align: left;
              box-sizing: border-box;
            }
          }
        }
      }
    }
  }
  .apply {
    .after-sale {
      z-index: 11;
      width: 70px;
      height: 30px;
      line-height: 30px;
      border-radius: 5px;
    }
  }
}
/*弹框*/
.cover {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: #000;
  z-index: 99;
  opacity: 0.5;
}
.pro-cover {
  width: 555px;
  height: 350px;
  border: 1px solid #ccc;
  border-radius: 5px;
  z-index: 99;
  background: #fefefe;
}
.cover-box {
  box-sizing: border-box;
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  margin: auto;
  z-index: 99;
  .shut {
    position: absolute;
    top: -8px;
    right: -8px;
    display: block;
    width: 18px;
    height: 18px;
    background: url("../../assets/images/shut.png") no-repeat center center;
  }
}
/*再来一单*/
.max-height300 {
  max-height: 300px;
}
.shop-table {
  margin-bottom: 20px;
  overflow-y: scroll;
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
</style>
<style lang='less'>
.el-dialog__wrapper {
  .el-dialog {
    width: 600px;
    .el-dialog__header {
      text-align: center;
    }
    .el-dialog__body {
      padding: 0;
      padding-bottom: 10px;
    }
    .el-dialog__footer {
      text-align: center;
    }
  }
}
</style>
