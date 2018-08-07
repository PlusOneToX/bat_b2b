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
          <div class="delivery-wrap">
            <!--订单及时性 倒计时-->
            <!--<div class="rl-clear rl-margin-bottom-xxs" v-if="orderDetail.payStatus !== 3">-->
            <!--<div class="rl-fl rl-margin-right-default">等待分销商付款</div>-->
            <!--<div class="rl-fl"><count-down v-on:start_callback="countDownS_cb(1)" v-on:end_callback="countDownE_cb(1)" :currentTime="1481450100" :startTime="1481450110" :endTime="1489999995" tipText="距离开始文字1" :tipTextEnd="'距离结束文字1'" :endText="'结束自定义文字2'" :dayTxt="'天'" :hourTxt="'小时'" :minutesTxt="'分'" :secondsTxt="'秒'"></count-down></div>-->
            <!--</div>-->
            <div class="order-state rl-clear rl-bg-white">
              <div class="rl-fl order-no">
                <span>{{$t('UserCenter.OrderNo')}}：{{orderInfo.orderNo}}</span>
              </div>
              <div class="rl-fl order-time">
                <span>{{$t('UserCenter.OrderTime')}}：{{orderInfo.createTime | formatDate}}</span>
              </div>
              <div v-if="orderDetail.advanceSale === 1" class="rl-fl">
                <span class="tips rl-fl">{{$t('OrderSuccess.OrderAdvance')}}：</span>
                <el-tooltip
                  class="red rl-fl"
                  :class="($i18n.locale === 'zh') ? '' : 'mini'"
                  effect="dark"
                  :content="($i18n.locale === 'zh') ? '预售商品当前库存不足，预计发货周期较长' : 'Commodities subject to advance sales are currently short of inventory, so they are expected to have a longer delivery cycle.'"
                  placement="bottom"
                >
                  <span>{{($i18n.locale === 'zh') ? '预售商品当前库存不足，预计发货周期较长' : 'Commodities subject to advance sales are currently short of inventory, so they are expected to have a longer delivery cycle.'}}</span>
                </el-tooltip>
              </div>
              <!-- 订单状态 -->
              <div class="rl-fr order-status">
                <span>{{$t('UserCenter.OrderStatus')}}：</span>
                <span class="blue">{{getOrderStatus(orderDetail.frontOrderStatus)}}</span>
              </div>
            </div>
            <!--物流-->
            <div class="logistics order-wrap rl-clear"  v-if="deliveryList.length>0">
              <div  class="order-title">{{($i18n.locale === 'zh') ? '订单物流详情' : 'Order logistics details'}}</div>
              <div class="log-top rl-fl">
                <div  @click="changeTab(index)" class="content" v-for="(item,index) in deliveryList"
                  :key="item.id" :class="{'current': tabType === index}">
                  <i class="el-icon-caret-right" v-show="tabType === index"></i>
                  <p class="rl-clear">
                    <!-- {{$t('UserCenter.DeliveryMethod')}}： -->
                    {{item.orderDeliverBill.distributionName}}
                    <span class="btn-view rl-fr cursor-pointer" @click="faHuoDetailsFun(item.orderDeliverBill.id)" >{{$t('versionsps.ViewDeliverList')}}</span>
                  </p>
                  <p>{{$t('OrderSuccess.LogisticsNo')}}：{{item.orderDeliverBill.logisticsNo}}</p>
                </div>
              </div>

              <div class="log-bottom rl-fr">
                <div class="log-list"  :class="[{'current': tabType === index}, {'hidden': toggleMore }]" v-for="(delivery,index) in deliveryList" :key="delivery.id">
                  <el-steps
                    direction="vertical"
                    :active="1"
                    v-if="delivery.orderDeliveryTrace.length > 0">
                    <el-step v-for="item in delivery.orderDeliveryTrace" :key="item.id"  icon="el-icon-circle-check" v-show="tabType === index">
                      <template slot="description">
                        <p
                          class="rl-text-xxs span-left rl-margin-left-default"
                        >{{item.acceptTime | formatDate }}</p>
                        <p
                          class="rl-text-xxs rl-margin-left-default span-right"
                        >{{item.acceptStation}}</p>
                      </template>
                    </el-step>
                    <!--<el-step title="步骤 2" description="这是一段很长很长很长的描述性文字"></el-step>-->
                  </el-steps>
                  <div class="no-logistics rl-clear" v-if="delivery.orderDeliveryTrace.length === 0">
                    <div class="rl-fl no-logistics-img">
                      <img width="100%" src="../assets/images/no-logistics.png" alt />
                    </div>
                    <div
                      class="rl-fl rl-margin-left-default rl-text-xxss rl-margin-top-mid rl-text-gray"
                    >{{$t('OrderSuccess.NoLogisticsInfo')}}</div>
                  </div>
                </div>
                <!-- 展开折叠 -->
                <div class="toggle-more">
                  <i class="el-icon-arrow-down" v-show="toggleMore" @click="toggleDown"></i>
                  <i class="el-icon-arrow-up" v-show="!toggleMore" @click="toggleUp"></i>
                </div>
              </div>
            </div>
            <!-- 收货人信息 -->
            <div class="goods-info order-wrap">
              <div class="order-title">{{$t('UserCenter.SendInformation')}}</div>
              <div class="infos rl-bg-white">
                <div class="all rl-fl">
                  <div class="item rl-clear">
                    <span class="rl-fl">{{$t('UserCenter.RecipientName')}}：</span>
                    <div class="rl-fl">{{orderDelivery.userName}}</div>
                  </div>
                  <div class="item rl-clear">
                    <span class="rl-fl">{{$t('UserCenter.ShippingAddress')}}：</span>
                    <div
                      class="rl-fl"
                    >{{orderDelivery.provinceName}}{{orderDelivery.cityName}}{{orderDelivery.districtName}}</div>
                  </div>
                  <div class="item rl-clear">
                    <span class="rl-fl">{{$t('UserCenter.ZipCode')}}：</span>
                    <div class="rl-fl">{{orderDelivery.zipCode}}</div>
                  </div>
                  <div class="item rl-clear">
                    <span class="rl-fl">{{$t('UserCenter.Tel')}}：</span>
                    <div
                      class="rl-fl"
                    >{{orderDelivery.phone ? (orderDelivery.phone + '/') : ''}}{{orderDelivery.mobile}}</div>
                  </div>
                </div>
                <div class="all rl-fl">
                  <!-- 配送方式 -->
                  <div class="item rl-clear">
                    <span class="rl-fl">{{$t('UserCenter.DeliveryMethod')}}：</span>
                    <div class="rl-fl">{{orderDelivery.distributionName}}</div>
                  </div>
                  <div class="item rl-clear">
                    <span class="rl-fl">{{$t('UserCenter.CommodityWeight')}}：</span>
                    <div class="rl-fl">{{goodsWeight| keepTwoNum}}({{$t('ShopCarts.ke')}})</div>
                  </div>
                  <!-- 配送范式 -->
                  <div class="item rl-clear">
                    <span class="rl-fl">{{$t('UserCenter.PaymentCurrency')}}：</span>
                    <div class="rl-fl">{{orderPayStyle(orderDistribution.payWay)}}</div>
                  </div>
                  <!-- 收货地址 -->
                  <div class="item rl-clear">
                    <span class="rl-fl">{{$t('UserCenter.Address')}}：</span>
                    <div class="rl-fl">{{orderDelivery.address}}</div>
                  </div>
                </div>
                <!-- 留言 -->
                <div class="item-msg rl-clear">
                  <span class="rl-fl">{{$t('UserCenter.Remarks')}}：</span>
                  <div class="rl-fl">{{orderDistribution.remark ? orderDistribution.remark : '暂无留言~'}}</div>
                </div>
              </div>
            </div>
            <!-- 付款信息 -->
            <div v-show="bankShow" class="goods-info order-wrap">
              <div class="order-title">{{$t('OrderSuccess.PaymentInformation')}}</div>
              <div class="infos rl-bg-white">
                <div class="all-all rl-fl">
                  <div class="item rl-clear">
                    <span class="rl-fl">{{$t('OrderSuccess.BeneficiaryCompanyName')}}</span>
                  </div>
                  <div class="item rl-clear">
                    <span class="rl-fl">{{$t('OrderSuccess.BankName')}}</span>
                  </div>
                  <div class="item rl-clear">
                    <span class="rl-fl">{{$t('OrderSuccess.Beneficiary')}}</span>
                  </div>
                  <div class="item rl-clear" v-if="$i18n.locale === 'en'">
                    <span class="rl-fl">{{$t('OrderSuccess.BankAddress')}}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- 订单详情 -->
          <div class="shop-list order-wrap rl-margin-top-default">
            <div class="order-title rl-clear">
              <div class="rl-fl">{{$t('UserCenter.OrderDetails')}}</div>
              <!-- 导出--暂时不做注释 -->
              <!-- <div class="rl-fr rl-margin-left-default orderDownLoad cursor-pointer" @click="orderDownLoad">{{$t('OrderSuccess.ExportList')}}</div> -->
              <!-- 活动 -->
              <div v-if="hasActivity === true" class="rl-fr orderDownLoad cursor-pointer" @click="lookActivityDetails">{{$t('OrderSuccess.ViewActivityDetails')}}</div>
            </div>
            <!-- 订单货品列表 -->
            <div class="shop-table rl-bg-white">
              <table>
                <tr>
                  <th width="240">{{($i18n.locale === 'zh') ? '商品' : 'Goods'}}</th>
                  <th>{{($i18n.locale === 'zh') ? '编码/条形码' : 'Code'}}</th>
                  <th v-if="isCollectFee===1">{{ $t("Quotation.SERVICEFEE") }}</th>
                  <th v-if="isCollectFee===1">{{ $t("Quotation.UNIT") }}</th>
                  <th>{{$t('ShopCarts.MemPrice')}}</th>
                  <th>{{$t('ShopCarts.DiscountPrice')}}</th>
                  <th>{{$t('ShopCarts.Quantity')}}</th>
                  <th>{{$t('versionsps.DeliveredQuantity')}}</th>
                  <th>{{$t('versionsps.GoodsStatus')}}</th>
                  <th>{{$t('ShopCarts.Total')}}</th>
                  <th v-if="hasDiy === true">{{$t('ShopCarts.Operation')}}</th>
                </tr>
                <!-- 普通货品 -->
                <tr class="goods-list" v-for="item in commonGoodsList" :key="item.id">
                  <td class="goods-info-wrap">
                    <div class="shop-img">
                      <img v-if="item.orderGoods.goodsType == 1" :src="item.orderGoods.imageUrl" alt />
                      <img v-if="item.orderGoods.goodsType == 2" :src="item.orderGoodsDiy.previewImage" alt />
                    </div>
                    <div class="shop-info">
                      <div class="goods-name">
                        <el-tooltip
                          class="item"
                          effect="dark"
                          :content="item.orderGoods.itemName"
                          placement="bottom"
                          v-show="$i18n.locale === 'zh' || !item.itemNameEn == true"
                        >
                          <span>{{item.orderGoods.itemName}}</span>
                        </el-tooltip>
                        <el-tooltip
                          class="item"
                          effect="dark"
                          :content="item.orderGoods.itemNameEn"
                          placement="bottom"
                          v-show="$i18n.locale === 'en'"
                        >
                          <span>{{item.orderGoods.itemNameEn}}</span>
                        </el-tooltip>
                      </div>
                      <div class="goods-model" v-if="item.orderGoods.goodsType == 2">
                        {{$t('P.model')}}：
                        <span>{{item.orderGoodsDiy.modelName}}</span>
                      </div>
                      <div class="goods-model" v-else>
                        {{$t('ShopCarts.Spe')}}：
                        <span
                          v-show="$i18n.locale === 'zh' "
                        >{{item.orderGoods.specsName}}</span>
                        <span v-show="$i18n.locale === 'en'">{{item.orderGoods.specsName}}</span>
                      </div>
                      <div class="goods-color" v-if="item.orderGoods.goodsType == 2">
                        {{$t('ShopCarts.Material')}}：
                        <span>{{item.orderGoodsDiy.materialName}}</span>
                      </div>
                      <div v-else>
                        {{$t('ShopCarts.Colors')}}：
                        <span
                          v-show="$i18n.locale === 'zh' "
                        >{{item.orderGoods.colorName}}</span>
                        <span v-show="$i18n.locale === 'en'">{{item.orderGoods.colorName}}</span>
                      </div>
                    </div>
                  </td>
                  <td>
                    <span>{{item.orderGoods.itemCode}}</span>
                    <br />
                    <span>{{item.orderGoods.barCode}}</span>
                  </td>
                  <!----服务单价--->
                  <td v-if="isCollectFee===1">
                    <span v-if="orderDetail.currencyType === 'CNY'">{{item.serviceFee?'￥'+item.serviceFee+'/片':'--'}}</span>
                    <span v-else>{{item.serviceFee?'$'+item.serviceFee+'/pc':'--'}}</span>
                  </td>
                  <!----单位--->
                  <td v-if="isCollectFee===1">
                    <span v-if="orderDetail.currencyType === 'CNY'">{{item.serviceBoxNum?item.serviceBoxNum+'片/盒':'--'}}</span>
                    <span v-else>{{item.serviceBoxNum?item.serviceBoxNum+'pc/box':'--'}}</span>
                  </td>
                  <td>
                    <i v-if="$i18n.locale == 'zh'">￥</i>
                    <i v-else>$</i>
                    {{($i18n.locale == 'zh'?(item.orderGoodsDistributorCost.salePrice):(Number(item.orderGoodsDistributorCost.salePrice)*exchange)) | keepTwoNum}}
                  </td>
                  <td>
                    <i v-if="$i18n.locale == 'zh'">￥</i>
                    <i v-else>$</i>
                    {{($i18n.locale == 'zh'?item.orderGoodsDistributorCost.actualPrice:(Number(item.orderGoodsDistributorCost.actualPrice)*exchange)) | keepTwoNum}}
                  </td>
                  <td>{{item.orderGoods.itemCount}}</td>
                  <td>{{item.orderGoods.deliverCount?item.orderGoods.deliverCount:0}}</td>
                  <td>{{GoodsItemStatus(((item.orderGoods.deliverCount&&item.orderGoods.deliverCount>0)?2:4))}}</td>
                  <td class="red">
                    <span>
                      <i v-if="$i18n.locale == 'zh'">￥</i>
                      <i v-else>$</i>
                      {{($i18n.locale == 'zh'?(item.orderGoodsDistributorCost.actualPrice*item.orderGoods.itemCount):(Number(item.orderGoodsDistributorCost.actualPrice*item.orderGoods.itemCount)*exchange)) | keepTwoNum}}
                    </span>
                    <br />
                    <span>（{{(item.orderGoods.weight*item.orderGoods.itemCount)| keepTwoNum}}{{$t('ShopCarts.ke')}}）</span>
                  </td>
                  <td v-if="hasDiy === true" class="blue cursor-pointer">
                    <span
                      @click="lookDiyImage(item.diyList)"
                      v-if="item.diyList.length > 0"
                    >{{$t('ShopCarts.View')}}</span>
                  </td>
                </tr>
              </table>
            </div>
          </div>
          <!-- 赠品-Y -->
          <div class="shop-list rl-margin-top-default" v-if="this.songGoodsList.length !== 0">
            <div class="rl-padding-top-xxxs rl-padding-bottom-xxxs">{{$t('UserCenter.SongShop')}}</div>
            <div class="shop-table rl-bg-white">
              <table>
                <tr>
                  <th>{{$t('ShopCarts.ItemNo')}}</th>
                  <th>{{$t('ShopCarts.ItemName')}}</th>
                  <th>{{$t('ShopCarts.BarCode')}}</th>
                  <th>{{$t('ShopCarts.Spe')}}</th>
                  <th>{{$t('ShopCarts.Colors')}}</th>
                  <th>{{$t('ShopCarts.Quantity')}}</th>
                </tr>
                <tr v-for="item in songGoodsList"  :key="item.id">
                  <td>{{item.orderGoods.itemCode}}</td>
                  <td>{{item.orderGoods.itemName}}</td>
                  <td>{{item.orderGoods.barCode}}</td>
                  <td>{{item.orderGoods.specsName}}</td>
                  <td>{{item.orderGoods.colorName}}</td>
                  <td>{{item.orderGoods.itemCount}}</td>
                </tr>
              </table>
            </div>
          </div>
          <!-- 订单总信息--Y -->
          <div class="totals rl-bg-white rl-margin-top-default">
            <div class="total-head rl-tr">
              <!-- 商品总价 -->
              <div class="item">
                {{$t('P.AggregatePriceA')}}：
                <span class="red">
                  <i v-if="$i18n.locale !== 'zh'">$</i>
                  {{(orderDistributorCost.goodsAmount?($i18n.locale == 'zh'?orderDistributorCost.goodsAmount:(Number(orderDistributorCost.goodsAmount)*exchange)):0) | keepTwoNum}}
                  <i v-if="$i18n.locale == 'zh'">元</i>
                </span>
              </div>
              <!-- 商品优惠 -->
              <div class="item rl-clear" v-if="orderDistributorCost.goodsPromotionAmount !== 0 && orderDistributorCost.goodsPromotionAmount !== null">
                {{$t('P.AggregatePriceB')}}：
                <span class="red">
                  <i v-if="$i18n.locale !== 'zh'">$</i>
                  {{($i18n.locale == 'zh'?(orderDistributorCost.goodsPromotionAmount):(Number(orderDistributorCost.goodsPromotionAmount)*exchange)) | keepTwoNum}}
                 <i v-if="$i18n.locale == 'zh'">元</i>
                </span>
              </div>
             
              <!-- 订单折扣金额 -->
              <div class="item rl-clear"  v-if="orderDistributorCost.orderPromotionAmount !== 0 && orderDistributorCost.orderPromotionAmount !== null">
                {{$t('P.AggregatePriceD')}}：
                <span class="red">
                  <i v-if="$i18n.locale !== 'zh'">$</i>
                  {{($i18n.locale == 'zh'?orderDistributorCost.orderPromotionAmount:(Number(orderDistributorCost.orderPromotionAmount)*exchange)) | keepTwoNum}}
                  <i v-if="$i18n.locale == 'zh'">元</i>
                </span>
              </div>
              
              <!-- 配送费用 -->
              <div class="item rl-clear">
                {{$t('P.AggregatePriceF')}}：
                <span class="red">
                  <i v-if="$i18n.locale !== 'zh'">$</i>
                  {{($i18n.locale == 'zh'?orderDistributorCost.distributionAmount:(Number(orderDistributorCost.distributionAmount)*exchange)) | keepTwoNum}}
                  <i v-if="$i18n.locale == 'zh'">元</i>
                </span>
              </div>
              <!-- 余额金额 -->
              <div class="item rl-clear" v-if="orderDistribution.payWay === 5">
                {{$t('ConfirmOrder.BalancePaid')}}：
                <span class="red">
                  <i v-if="$i18n.locale !== 'zh'">$</i>
                  {{($i18n.locale == 'zh'?orderDistributorCost.depositAmount:(Number(orderDistributorCost.depositAmount)*exchange)) | keepTwoNum}}
                  <i v-if="$i18n.locale == 'zh'">元</i>
                </span>
              </div>
              <!-- 代金券 -->
              <div class="item rl-clear"  v-if="orderDistributorCost.rebateVoucherAmount && orderDistributorCost.rebateVoucherAmount !== 0 && orderDistributorCost.rebateVoucherAmount !== null">
                {{$t('UserCenter.Voucher')}}：
                <span class="red">
                  -<i v-if="$i18n.locale !== 'zh'">$</i>
                  {{($i18n.locale == 'zh'?orderDistributorCost.rebateVoucherAmount:(Number(orderDistributorCost.rebateVoucherAmount)*exchange)) | keepTwoNum}}
                  <i v-if="$i18n.locale == 'zh'">元</i>
                </span>
              </div>
              <!-- 订单总金额 -->
              <div
                class="item item-total rl-clear"
                v-if="orderDistribution.payWay === 5 || orderDistribution.payStatus === 3 || orderDistribution.orderStatus === 5">
                {{$t('P.AggregatePriceG')}}：
                <span class="red">
                  <i v-if="$i18n.locale !== 'zh'">$</i>
                  {{($i18n.locale == 'zh'?orderDistributorCost.payAmount:(Number(orderDistributorCost.payAmount)*exchange)) | keepTwoNum}}
                  <i
                    v-if="$i18n.locale == 'zh'"
                  >元</i>
                </span>
              </div>
              <div class="item item-total rl-clear" v-else>
                {{$t('P.AggregatePriceG')}}：
                <span class="red">
                  <i v-if="$i18n.locale !== 'zh'">$</i>
                  {{($i18n.locale == 'zh'?orderDistributorCost.payAmount:(Number(orderDistributorCost.payAmount)*exchange)) | keepTwoNum}}
                  <i
                    v-if="$i18n.locale == 'zh'"
                  >元</i>
                </span>
              </div>
            </div>
            <div class="comfirm-order rl-tr">
              <template v-if="(orderDistribution.payStatus == 1 || orderDistribution.payStatus == 2)
                &&(orderDistribution.orderStatus != 5&&orderDistribution.orderStatus != 4)
                &&(orderDistribution.payWay==1||orderDistribution.payWay==2||orderDistribution.payWay==5||orderDistribution.payWay==6)">
                <div  @click="goPay"  class="rl-text-white cursor-pointer red-btn" :class="{disabled: !clickFlag}" v-if="clickAgain === true">{{$t('P.Payment')}}</div>
                <div class="rl-text-white red-btn" v-else>{{$t('P.Payment')}}</div>
              </template>
              <template v-if="orderDistribution.payWay === 3 || orderDistribution.payWay === 4 || orderDistribution.payStatus === 3 || orderDistribution.orderStatus === 5 || orderDistribution.orderStatus === 6">
                <div @click="goUpBack" class="rl-text-white cursor-pointer blue-btn">{{$t('P.Back')}}</div>
              </template>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!--支付二维码-->
    <div class="cover" v-if="showCode"></div>
    <div v-show="showCode" class="ewm">
      <div
        class="rl-margin-bottom-default rl-padding-top-xxxs rl-padding-bottom-xxxs rl-padding-left-xxxs rl-tl"
      >
        <span @click="shutLog" class="shut cursor-pointer"></span>
      </div>
      <div class="rl-padding-bottom-lllg time-state" v-show="qrCodeState === true">
        <p class="rl-tc" v-if="$i18n.locale === 'zh'">
          距离二维码过期还剩
          <span
            class="rl-text-blue-xs rl-margin-right-xxxxs rl-margin-left-xxxxs"
          >{{qrCodeTime}}</span>秒，
        </p>
        <p class="rl-tc" v-else>
          The qr code expired
          <span
            class="rl-text-blue-xs rl-margin-right-xxxxs rl-margin-left-xxxxs"
          >{{qrCodeTime}}</span> s，
        </p>
        <p class="rl-tc" v-if="$i18n.locale === 'zh'">过期后请刷新页面重新获取二维码。</p>
        <p
          class="rl-tc rl-text-xxs"
          v-else
        >After expiration, please refresh the page to get the qr code again。</p>
      </div>
      <div class="rl-padding-bottom-lllg time-state" v-show="qrCodeState === false">
        <span v-if="$i18n.locale === 'zh'">二维码已过期，请</span>
        <span v-else class="rl-text-xxs">Qr code expired,please</span>
        <span
          v-if="$i18n.locale === 'zh'"
          class="rl-text-orange-sm rl-margin-right-xxxxs rl-margin-left-xxxxs"
        >刷新</span>
        <span
          v-else
          class="rl-text-orange-sm rl-margin-right-xxxxs rl-margin-left-xxxxs rl-text-xxs"
        >refresh</span>
        <span v-if="$i18n.locale === 'zh'">二维码重新获取</span>
        <span v-else class="rl-text-xxs">Retrieve qr code</span>
      </div>
      <div class="qrcode">
        <div id="qrcode"></div>
        <div class="state" v-show="qrCodeState === false"></div>
        <div class="state-other" v-show="qrCodeState === true"></div>
        <div class="fair-img cursor-pointer" @click="reFresh" v-show="qrCodeState === false">
          <img src="../assets/images/refresh.png" alt />
        </div>
      </div>
      <div class="zhifu rl-clear">
        <div class="icon rl-margin-right-mid rl-fl" v-if="orderDistribution.payWay === 1">
          <img src="../assets/images/zhifubao.png" alt />
        </div>
        <div class="rl-fl" v-if="orderDistribution.payWay === 1">
          <div v-if="$i18n.locale === 'zh'">
            <p class="rl-tl spc-p">
              请使用
              <span class="rl-text-orange-sm">支付宝</span>扫一扫
            </p>
            <p class="rl-tl">扫描二维码支付</p>
          </div>
          <div v-else>
            <p class="rl-tl spc-p rl-text-xxs">
              Please use the
              <span class="rl-text-orange-sm">Alipay</span> scan
            </p>
            <p class="rl-tl rl-text-xxs">Pay by scanning qr code</p>
          </div>
        </div>
        <div class="icon rl-margin-right-mid rl-fl" v-if="orderDistribution.payWay === 2">
          <img src="../assets/images/wechat.png" alt />
        </div>
        <div class="rl-fl" v-if="orderDistribution.payWay === 2">
          <div v-if="$i18n.locale === 'zh'">
            <p class="rl-tl spc-p">
              请使用
              <span class="rl-text-orange-sm">微信</span>扫一扫
            </p>
            <p class="rl-tl">扫描二维码支付</p>
          </div>
          <div v-else>
            <p class="rl-tl spc-p rl-text-xxs">
              Please use the
              <span class="rl-text-orange-sm">WeChat</span> scan
            </p>
            <p class="rl-tl rl-text-xxs">Pay by scanning qr code</p>
          </div>
        </div>
      </div>
    </div>
    <!--支付成功-->
    <div class="cover" v-if="showPay"></div>
    <div v-show="showPay" class="pay-result">
      <div
        class="rl-margin-bottom-default rl-bdb-gray-sm rl-padding-top-xxxs rl-padding-bottom-xxxs rl-bg-gray-mm rl-tc"
      >{{$t('OrderSuccess.PayResults')}}</div>
      <span @click="shutPay" class="shut cursor-pointer"></span>
      <div class="rl-margin-top-double rl-margin-bottom-default">
        <img src="../assets/images/pay-success.png" alt />
      </div>
      <p class="rl-tc rl-text-lg rl-text-gray">{{$t('OrderSuccess.PaySuccess')}}</p>
      <div class="result">
        <span
          @click="shutPay"
          class="rl-tc rl-bg-blue-mm rl-text-white rl-text-mid cursor-pointer"
        >{{$t('P.OK')}}</span>
      </div>
    </div>
    <!--查看diy图片-->
    <el-dialog class="diy-all" title :visible.sync="diyDialogVisible">
      <div
        class="diy-css max-height300 rl-padding-top-default rl-padding-left-double rl-padding-left-double rl-clear"
      >
        <div class="banner rl-fl">
          <el-carousel
            trigger="click"
            :loop="falseState"
            :autoplay="falseState"
            height="414px"
            arrow="always"
            indicator-position="none"
          >
            <el-carousel-item v-for="item in diyImages" :key="item.id">
              <div class="banner-img cursor-pointer">
                <img :src="item.diyPic + '?x-oss-process=image/resize,h_200,l_400'" alt />
              </div>
              <div class="rl-tc rl-text-base sum">x{{item.diyNum}}</div>
            </el-carousel-item>
          </el-carousel>
        </div>
      </div>
      <div slot="footer" class="dialog-footer rl-tc">
        <el-button @click="diyDialogVisible = false">{{$t('P.Cancel')}}</el-button>
      </div>
    </el-dialog>
    <div v-if="this.conditionIds.length > 0 || this.gradeDiscountId !== ''">
      <activityDetail
        @shutDialog="shutDialog"
        :conditionIds="conditionIds"
        :gradeDiscountId="gradeDiscountId"
        :currencyType="orderDistribution.currencyType"
        :commonGoodsList="goodsList"
      ></activityDetail>
    </div>
    <!--物流发货清单弹窗-->
    <el-dialog
      :title="$t('versionsps.LogisticsDeliverList')"
      :visible.sync="diaShippedListVisible"
      width="50%"
      class="logi-list">
      <el-table :data="faHuoDetails">
        <el-table-column property="orderGoodsId" :label="$t('versionsps.OrderNo')" width="80"></el-table-column>
        <el-table-column property="createTime" :label="$t('UserCenter.OrderTime')" width="200">
         
        </el-table-column>
        <el-table-column property="itemCode" :label="$t('ShopCarts.ItemNo')" width="130"></el-table-column>
        <el-table-column property="barCode" :label="$t('ShopCarts.BarCode')" width="130"></el-table-column>
        <el-table-column property="itemName" :label="$t('ShopCarts.ItemName')"></el-table-column>
        <el-table-column property="count" :label="$t('versionsps.DeliverQuantity')" width="90"></el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="diaShippedListVisible = false">{{$t('P.OK')}}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import Left from '@/components/Left.vue';
import QRCode from 'qrcodejs2';
import { formatDate} from '@/assets/js/common.js';
import CountDown from 'vue2-countdown';
import activityDetail from '@/components/activityDetail.vue';
import GD from '@/assets/js/globalData';
import countDown from '@/components/countDown.vue';
// liu
import {orderDetails,payCreateTrade, userDeposit, payQueryTrade,orderDeliverBillDetail} from '@/apiService/api'
export default {
  name: 'WaitPayment',
  components: { Header, Left, CountDown, activityDetail, countDown },
  data () {
    return {
      falseState: false,
      userState: 2,
      diyDialogVisible: false,
      id: this.$route.query.order_id,
      showCode: false,
      showPay: false,
      Url: '', // 二维码
      payOrderId: '', // 支付二维码返回id
      qrCodeState: true, // 二维码状态
      qrCodeTime: 0, // 二维码失效时间
      isDeposit: 0, // 预存款抵扣
      depositValue: '',
      payState: 1,
      orderType: 0,
      orderDetail: {
        id: 0,
        actEndTime: 0,
        payStatus: 0,
        orderStatus: 0,
        createTime: 0
      },
      orderCost: {},
      goodsList: [],
      commonGoodsList: [], // 普通商品
      songGoodsList: [], // 赠品
      orderDelivery: {},  //收货人信息
      
      orderDistribution: {},  
      intervalid: '',
      tabType: 0, // 控制物流切换
      clickAgain: true, // 用于防止二次点击
      hasDiy: false, // 判断是否含有diy
      deliveryList: [],  //物流信息
      diyImages: [],
      hasActivity: false, // 订单是否参与活动
      activityDialogVisible: false,
      conditionIds: [],
      gradeDiscountId: '',
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: 'zh-RMB', // 语种
      bankShow: false,
      shippedList: [],
      diaShippedListVisible: false, // 物流发货清单
      toggleMore: true, // 物流信息展开/折叠
      isCollectFee: null, // 是否收取服务费
      goodsWeight:0,  //总重量
      orderInfo:{},  //订单信息
      orderDistributorCost:{},  
      faHuoDetails:[],  //发货清单
      exchange:1, //汇率
      clickFlag: true, // 防重复点击
    };
  },
  filters: {
    formatDate (time) {
      var date = new Date(time);
      return formatDate(date, 'yyyy-MM-dd hh:mm:ss');
    },
    keepTwoNum (value) {
      value = Number(value);
      return value.toFixed(2);
    }
  },
  methods: {
    // 货品状态
    GoodsItemStatus (value) {
      switch (value) {
        case 1:
          return this.$t('UserCenter.PartOfShipped');
        case 2:
          return this.$t('UserCenter.Shipped');
        case 3:
          return this.$t('UserCenter.Closed');
        case 4:
          return this.$t('UserCenter.Unshipped');
        case 9:
          return this.$t('UserCenter.InTheOutbound');  
      }
    },
    
    
    fLangChange (value) {
      window.localStorage.setItem('bLang', value);
      this.$i18n.locale = value.split('-')[0];
    },
    lookDiyImage (imgList) {
      // 查看diy图片
      if (imgList) {
        this.diyDialogVisible = true;
        this.diyImages = imgList;
      }
    },
    lookActivityDetails () {
      // 查看活动详情
      var hash = [];
      this.goodsList.forEach((item) => {
        if (item.orderPromotionId) {
          this.conditionIds.push(item.orderPromotionId);
        }
        if (item.goodsPromotionId) {
          this.conditionIds.push(item.goodsPromotionId);
        }
        if (item.gradeDiscountId) {
          this.gradeDiscountId = item.gradeDiscountId;
        }
      });
      for (var i = 0; i < this.conditionIds.length; i++) {
        // 数组去重
        if (this.conditionIds.indexOf(this.conditionIds[i]) === i) {
          hash.push(this.conditionIds[i]);
        }
      }
      this.conditionIds = hash;
    },
    shutDialog (target) {
      if (target) {
        this.conditionIds = [];
        this.gradeDiscountId = '';
      }
    },
    // 订单状态--Y
    getOrderStatus (row) {    
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
          return this.$t("UserCenter.InTheOutbound");  //出库中  
          
      }
    },
    
    getPayStatus (row) {
      // 订单付款状态
      switch (row) {
        case 1:
          return this.$t('UserCenter.PendingPayment');
        case 2:
          return this.$t('UserCenter.PartOfPayment');
        case 3:
          return this.$t('UserCenter.Paid');
        case 4:
          return this.$t('UserCenter.Refunded');
        case 5:
          return this.$t('UserCenter.PartOfRefunded');
        case 6:
          return this.$t('UserCenter.Refunding');
      }
    },
    orderPayStyle (row) {
      // 订单付款方式
      switch (row) {
        case 1:
          return this.$t('P.Alipay');
        case 2:
          return this.$t('P.WechatPay');
        case 3:
          return this.$t('P.RangeCheckout');
        case 4:
          return this.$t('P.BankTransfer');
        case 5:
          return '余额支付';
        case 6:
          return '网银支付';
      }
    },
    goUpBack () {
      this.$router.go(-1);
    },
    shutLog () {
      // 关闭支付弹框
      // this.qrCodeTime = 300;
      this.showCode = false;
      this.qrCodeState = true;
      this.paySuccessState(1);
      
      this.clickFlag = true;
    },
    shutPay () {
      this.showPay = false;
      this.getOrderDetail();
    },
    changeTab (index) {
      // 切换物流
      this.tabType = index;
    },
    // 去商品详情
    goDoodsDetail (id) {
      let routeData = this.$router.resolve({
        name: 'ShopDetail',
        query: { good_id: id }
      });
      window.open(routeData.href, '_blank');
    },
    // 获得订单详情
    getOrderDetail () {
      let userId=localStorage.getItem('userId')
      this.songGoodsList = [];
      this.commonGoodsList = [];
      orderDetails({distributorId:userId,orderId:this.id}).then((res) => {
        if (res.success) {
          console.log('订单详情：',res.data);
          this.orderDetail = res.data;
          this.orderDelivery = res.data.orderDelivery;  //收货人信息-Y
          this.orderDistribution = res.data.orderDistributorData;  //分销商信息 orderDetail
          this.goodsList = res.data.orderInfoDetailGoods; //Y
          this.orderInfo=res.data.orderInfo;  //订单信息
          this.deliveryList = res.data.orderDeliverDetail; //  获取物流信息
          console.log('物流信息',this.deliveryList);
          this.orderDistributorCost=res.data.orderDistributorCost;
           this.goodsList.forEach((item) => {
              //  计算总重量
              this.goodsWeight+=(item.orderGoods.itemCount*item.orderGoods.weight);
              // 根据itemType判断赠品
              
              if (item.orderGoodsDistributorCost.itemType == 2) {  
                this.songGoodsList.push(item);
              } else {
                this.commonGoodsList.push(item);
              }
              // 判断是否有活动
              if (item.orderPromotionId||item.goodsPromotionId||item.spellGroupId) {
                this.hasActivity = true;
              }
              
           })
          
         
          // 银行转账方式
          if (this.orderDistribution.payWay === 4) {
            this.bankShow = true;
          }
        }
      });
    },

    // 查看发货清单
    faHuoDetailsFun(id){
        orderDeliverBillDetail({orderDeliverBillId :id}).then(res=>{
            if(res.success){
              this.faHuoDetails=res.data;
              this.diaShippedListVisible=true;
            }
        })
    },
    
    // 支付-y
    goPay () {
      if (this.clickFlag) {
        this.clickFlag = false;
        clearInterval(this.intervalid)
        clearInterval(this.intervalids)
        let ids= this.orderDistribution.orderId
        let userId=localStorage.getItem('userId');
        let userName=localStorage.getItem('userName');
        let payMethod='';
        if(this.orderDistribution.payWay==1){
          payMethod='alipay_face_to_face'
        }else if(this.orderDistribution.payWay==2){
          payMethod='wxpay_native'
        }else if(this.orderDistribution.payWay==5){
          payMethod="balance";
        }else if(this.orderDistribution.payWay==6){
          payMethod='kuaiqian_merchant'
        }
      
        let payParams={
          customerFlag:0,
          orderId:ids,
          payMethod:payMethod,
          payerId:userId,
          payerName:userName,
          redirectUrl: "https://www.bat.com/#/OrderManage"
        }
        payCreateTrade(payParams).then(payRes=>{
          console.log('支付',payRes.data);
          if(payRes.success){
            if (this.orderDistribution.payWay === 6) { // 网银支付--即是快钱支付
              window.location.href = payRes.data.kuaiQianReap.h5Url; // 跳转链接
            }else{
          
              let urlStr=this.orderDistribution.payWay==1?payRes.data.alipayResp.codeUrl:payRes.data.wxResp.codeUrl;
              if(this.orderDistribution.payWay==1||this.orderDistribution.payWay==2){
                this.qrCodeTime = 60;
                this.intervalids = setInterval(() => {
                  this.qrCodeTime--
                  if (this.qrCodeTime === 0) {
                    this.qrCodeState = false;  //二维码状态
                    clearInterval(this.intervalids)
                  }
                }, 1000)
                this.makeCodes(urlStr,ids,payMethod);
              }else{
                
                this.$message('支付成功！');
              } 
            }
          }else{
            this.$message(payRes.errMessage);
            this.clickFlag = true;
          }
        })
      }
    },
   
    // 重新获取二维码
    reFresh () {
      // this.qrCodeTime = 300;
      this.qrCodeState = true;
      this.goPay();
    },
    // 前往首页
    goIndex () {
      this.$router.push({ name: 'Index' });
    },
    // 预存款抵扣
    changeState () {
      if (this.orderCost.depositAmount < Number(this.depositValue)) {
        if (this.$i18n.locale === 'zh') {
          this.$message.error('余额抵扣不能小于预存余额!');
        } else {
          this.$message.error(
            'The balance deduction shall not be less than the deposit balance.'
          );
        }
        this.depositValue = '';
      }
    },
    // 生成二维码
    makeCodes (url,id,payMethod) {
      // console.log(this.Url)
      this.showCode = true;
      document.getElementById('qrcode').innerHTML = '';
      var qrcode = new QRCode(document.getElementById('qrcode'), {
        width: 250,
        height: 250
      });
      qrcode.makeCode(url);
      this.paySuccessState();
      this.paySuccessState(2,id,payMethod)
    },
   
    // 获取账户余额--y  
    getDeposit() {
      let id=localStorage.getItem('userId');
      userDeposit({id:id}).then((res) => {
          if (res.success) {
            this.depositDetail  = res.data;
          }
        });
    },
     // 监听支付成功状态---Y  
    paySuccessState (num,id,payMethod) {
      let secondsArea = 600 // 设置支付时间10分钟
      if (num === 1) { // 关闭支付窗口
        clearInterval(this.intervalid)
      } else {
        clearInterval(this.intervalid) // 先关闭之前的定时器
        this.intervalid = setInterval(() => {
          secondsArea--
          
          if (secondsArea <= 600 && secondsArea > 540) {
            if (secondsArea % 5 === 0) {
              this.payQueryTradeFun(id,payMethod);
            }
          } else if (secondsArea <= 540 && secondsArea > 300) {
            if (secondsArea % 10 === 0) {
              this.payQueryTradeFun(id,payMethod);
            }
          } else if (secondsArea <= 300 && secondsArea > 0) {
            if (secondsArea % 20 === 0) {
              this.payQueryTradeFun(id,payMethod);
            }
          } else {
            if (this.$i18n.locale === 'zh') {
              this.$message.error('支付超时！')
            } else { this.$message.error('Pay overtime.') }
            clearInterval(this.intervalid)
          }
        }, 1000)
      }
    },
    // 查询交易的状态--Y
    payQueryTradeFun(id,payMethod){
      let userId=localStorage.getItem('userId');
        let params={
          orderId:id,  //订单id
          payMethod :payMethod,  //交易方法
          customerId: userId, // 客户id
        }
        let secondsAreas = 5 // 设置支付成功弹框消失时间
        let secondsRefres = 1 // 设置支付成功刷新数据请求时间
        payQueryTrade(params).then(res=>{
            if (res.success) {
              if (res.data.tradeState=='SUCCESS'||res.data.tradeState=='CLOSED'||res.data.tradeState=='REVOKED'||res.data.tradeState=='PAYERROR') {
                 let orderStatus='';
                  if(res.data.tradeState=='SUCCESS'){
                    orderStatus='支付成功！';
                  }
                  if(res.data.tradeState=='REFUND'){
                    orderStatus='转入退款！';
                  }
                  if(res.data.tradeState=='NOTPAY'){
                    orderStatus='未支付！';
                  }
                  if(res.data.tradeState=='CLOSED'){
                      orderStatus='已关闭！';
                  }
                  if(res.data.tradeState=='REVOKED'){
                      orderStatus='已撤销！';
                  }
                  if(res.data.tradeState=='USERPAYING'){
                    orderStatus='用户支付中！';
                  }
                  if(res.data.tradeState=='PAYERROR'){
                    orderStatus='支付失败！';
                  }
                  if(res.data.tradeState=='ACCEPT'){
                      orderStatus='已接收，等待扣款！';
                  }
                  this.$message(orderStatus);
                  this.showCode = false
                  this.showPay = true
                  this.orderList = [] // 清空之前的数据
                  this.orderAllCost = 0
                  this.intervalidres = setInterval(() => { // 延迟数据更新，防止后台付款状态给得慢
                    secondsRefres--
                    if (secondsRefres === 0) {
                      // for (var i = 0; i < this.orderIds.length; i++) {
                      //   this.getOrderDetail(this.orderIds[i])  goOrderDetail
                      // }
                      this.goOrderDetail(id)
                      clearInterval(this.intervalidres)
                    }
                  }, 1000)
                  this.intervalids = setInterval(() => {
                    secondsAreas--
                    if (secondsAreas === 0) {
                      this.showPay = false
                      clearInterval(this.intervalids)
                    }
                  }, 1000)
                  clearInterval(this.intervalid)
                }
              }else{
               clearInterval(this.intervalid) 
            }
        })
    },
   
   
    // 导出订单详情
    orderDownLoad () {
      this.$api
        .exportData(this, 'user/u/order/orderDownLoad', { id: this.id })
        .then((res) => {
          const content = res;
          let fileName = '';
          if (this.$i18n.locale === 'zh') {
            fileName = '购买清单.xls';
          } else {
            fileName = 'purchasing list.xls';
          }
          let blob = new Blob([content], {
            type: 'application/ms-excel'
          });
          let url = URL.createObjectURL(blob);
          if ('download' in document.createElement('a')) {
            let link = document.createElement('a');
            link.style.display = 'none';
            link.href = url;
            link.setAttribute('download', fileName);
            document.body.appendChild(link);
            link.click();
            URL.revokeObjectURL(link.href); // 释放URL 对象
            document.body.removeChild(link);
          } else {
            navigator.msSaveBlob(blob, fileName);
          }
        });
    },
    // 物流信息 - 展开
    toggleDown () {
      this.toggleMore = false;
    },
    // 物流信息 - 折叠
    toggleUp () {
      this.toggleMore = true;
    }
  },
  created () {
    this.exchange=Number( localStorage.getItem('exchange')); 
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem('bLang')
      ? window.localStorage.getItem('bLang')
      : 'zh-RMB';
    this.getOrderDetail();
    // this.getDeposit()
  }
};
</script>
<style lang='less'>
@import url("../assets/less/variable.less");
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
  &.logi-list {
    .el-dialog {
      border-radius: 10px;
      .el-dialog__header {
        padding: 40px 30px 10px;
        box-sizing: border-box;
        text-align: left;
        .el-dialog__title {
          font-size: 16px;
          font-weight: bold;
        }
      }
      .el-dialog__body {
        margin: 20px 30px;
        box-sizing: border-box;
        .el-table {
          border-right: 1px solid @lightGrayBg;
          th {
            border-top: 1px solid @lightGrayBg;
            .cell {
              color: @lightBlack;
            }
          }
          td:first-child {
            color: @blue;
          }
          td,
          th {
            border-bottom: 1px solid @lightGrayBg;
            border-right: 1px solid @lightGrayBg;
            &:first-child {
              border-left: 1px solid @lightGrayBg;
            }
          }
        }
      }
      .el-table__empty-block {
        border-left: 1px solid @lightGrayBg;
      }
      .el-dialog__footer {
        .el-button {
          padding: 12px 40px;
          box-sizing: border-box;
          font-size: 16px;
          border-color: @blue;
          background-color: @blue;
        }
      }
    }
  }
}
</style>
<style scoped="scoped" lang='less'>
@import url("../assets/less/variable.less");
.user {
  width: 100%;
}
.user-main {
  width: 1200px;
}
.user-right {
  width: 1030px;
  .delivery-wrap {
    padding: 24px 40px 0;
    border: 2px solid @bdLightColor;
    border-radius: 8px;
  }
  .order-state {
    padding-bottom: 22px;
    height: 25px;
    line-height: 25px;
    .order-no {
      margin-right: 60px;
      font-size: 16px;
      color: @lightBlack;
    }
    .order-time {
      margin-right: 40px;
      font-size: 12px;
      color: @lightBlack;
    }
    .order-tips {
      font-size: 12px;
      .red {
        width: 240px;
        color: @red;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        &.mini {
          width: 200px;
        }
      }
    }
    .order-status {
      font-size: 12px;
      .blue {
        color: @blue;
      }
    }
  }
  .order-wrap {
    border-top: 1px solid @bdLightColor;
    padding: 20px 0;
    .order-title {
      font-size: 14px;
      color: @lightBlack;
    }
  }
  /*物流*/
  .logistics {
    padding-bottom: 50px;
    background-color: #fff;
    .order-title {
      margin-bottom: 15px;
    }
    .log-top {
      width: 269px;
      height: 253px;
      border: 1px solid @bdLighterColor;
      overflow: hidden;
      overflow-y: auto;
      .content {
        padding: 15px 30px;
        p {
          font-size: 12px;
          color: @lightBlack;
          line-height: 20px;
        }
      }
      .content.current {
        position: relative;
        .el-icon-caret-right {
          position: absolute;
          top: 16px;
          left: -15px;
          font-size: 38px;
          color: @blue;
        }
        p {
          color: @blue;
        }
        .btn-view {
          font-size: 12px;
          color: @blue;
        }
      }
    }
    .log-bottom {
      position: relative;
      width: 651px;
      border-bottom: 1px solid @bdLighterColor;
      .log-list {
        display: none;
        &.current {
          display: block;
        }
        &.hidden {
          height: 254px;
          overflow: hidden;
        }
      }
      .toggle-more {
        position: absolute;
        bottom: -25px;
        left: 16px;
        cursor: pointer;
        i {
          padding: 1px 10px;
          color: @white;
          border: 1px solid @blue;
          border-top: 0;
          background-color: @blue;
          font-size: 22px;
          border-radius: 0 0 4px 4px;
        }
      }
      /deep/ .el-steps {
        .el-icon-circle-check:before {
          content: "";
          display: block;
          width: 18px;
          height: 18px;
          background-color: @bdLighterColor;
          border-radius: 50%;
        }
        .is-finish {
          .el-icon-circle-check:before {
            background-color: @blue;
          }
        }
      }
      .span-left {
        margin-bottom: 10px;
        line-height: 18px;
      }
      .span-right {
        margin-bottom: 10px;
      }
      .no-logistics {
        margin: 0 auto;
        padding-top: 65px;
        width: 160px;
        .no-logistics-img {
          width: 48px;
        }
      }
    }
  }
  .goods-info {
    .infos {
      padding: 30px 40px 0;
      overflow: hidden;
      .all {
        width: 50%;
        .item {
          margin-bottom: 15px;
          font-size: 12px;
          color: @lightBlack;
          span {
            display: block;
            text-align: right;
          }
          div {
            width: 315px;
            font-size: 12px;
            color: @lightBlack;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
        }
      }
      .item-msg {
        margin-bottom: 15px;
        font-size: 12px;
        color: @lightBlack;
        div {
          padding: 7px 22px;
          width: 474px;
          max-height: 50px;
          font-size: 12px;
          color: @lighterGray;
          background-color: @lightGrayBg;
          border-radius: 4px;
        }
      }
      .all-all {
        width: 100%;
        .item {
          margin-bottom: 15px;
          font-size: 12px;
          color: @lightBlack;
          span {
            display: block;
            text-align: right;
          }
        }
      }
    }
  }
  .shop-list {
    padding: 30px 40px;
    border: 2px solid @bdLightColor;
    border-radius: 8px;
    .orderDownLoad {
      padding: 5px;
      font-size: 12px;
      color: @blue;
      border-radius: 5px;
    }
    .shop-table {
      margin-top: 10px;
      width: 100%;
      overflow: hidden;
      table {
        width: 100%;
        tr {
          & + tr {
            border-top: 1px dashed @bdLighterColor;
          }
          th {
            width: 193px;
            height: 30px;
            line-height: 1;
            text-align: center;
            background-color: @bdLightColor;
            font-size: 12px;
            color: @gray;
            font-weight: normal;
          }
          td {
            padding: 5px 0;
            height: 70px;
            font-size: 12px;
            color: @lightBlack;
            text-align: center;
            &.red {
              color: @lighterRed;
            }
            &.blue {
              color: @blue;
            }
          }
        }
      }
      .goods-list:hover {
        background-color: @lightGrayBg;
      }
      .goods-info-wrap {
        width: 200px;
        padding-left: 100px;
        position: relative;
        .shop-img {
          position: absolute;
          top: 5px;
          left: 0;
          width: 70px;
          overflow: hidden;
          img {
            max-width: 100%;
            max-height: 70px;
            vertical-align: middle;
          }
        }
        .shop-info {
          div {
            width: 150px;
            font-size: 12px;
            color: @lightBlack;
            text-align: left;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            & + div {
              margin-top: 8px;
            }
          }
        }
      }
    }
  }
  .totals {
    .total-head {
      padding: 25px 40px;
      border: 2px solid @bdLightColor;
      border-radius: 8px;
      .item {
        font-size: 12px;
        color: @lightBlack;
        line-height: 25px;
        & + .item {
          margin-top: 5px;
        }
        &.item-total {
          margin-top: 20px;
          .red {
            font-size: 18px;
            font-weight: bold;
            i {
              font-size: 12px;
              font-weight: normal;
            }
          }
        }
        .red {
          color: @lighterRed;
        }
      }
    }
    .comfirm-order {
      padding: 16px 23px 0 0;
      div {
        display: inline-block;
        width: 97px;
        height: 40px;
        line-height: 40px;
        border-radius: 4px;
        text-align: center;
        &.red-btn {
          background-color: @redLabel;

          &.disabled {
            background-color: #999;
          }
        }
        &.blue-btn {
          background-color: @blue;
        }
        & + div {
          margin-left: 15px;
        }
      }
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
.ewm {
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  display: block;
  width: 500px;
  height: 490px;
  z-index: 99;
  margin: auto;
  background: #fff;
  text-align: center;
  border-radius: 10px;
  .shut {
    position: absolute;
    top: 10px;
    right: 10px;
    display: block;
    width: 18px;
    height: 18px;
    background: url("../../src/assets/images/wrong-de.png") no-repeat center
      center;
    background-size: 100%;
  }
  .time-state {
    width: 280px;
    height: 38px;
    margin: 0 auto;
    font-size: 15px;
  }
}
.pay-result {
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  display: block;
  width: 600px;
  height: 460px;
  z-index: 99;
  margin: auto;
  background: #fff;
  text-align: center;
  .shut {
    position: absolute;
    top: -8px;
    right: -8px;
    display: block;
    width: 18px;
    height: 18px;
    background: url("../../src/assets/images/shut.png") no-repeat center center;
  }
  .result {
    margin-top: 60px;
    span {
      display: inline-block;
      width: 120px;
      height: 35px;
      line-height: 35px;
      border-radius: 5px;
    }
  }
}
#qrcode {
  position: relative;
  margin: 0 auto;
  width: 250px;
  height: 250px;
}
.qrcode {
  position: relative;
  margin: 0 auto;
  width: 280px;
  height: 280px;
  .state {
    position: absolute;
    top: -15px;
    left: 0;
    background: #b2b2b2;
    z-index: 99;
    opacity: 0.95;
    width: 280px;
    height: 280px;
  }
  .state-other {
    position: absolute;
    top: -15px;
    left: 0;
    width: 275px;
    height: 275px;
    border: 3px solid #e0e0e0;
  }
  .fair-img {
    position: absolute;
    top: 35%;
    left: 40%;
    z-index: 999;
    width: 49px;
  }
}
.zhifu {
  padding-top: 10px;
  width: 280px;
  margin: 0 auto;
  .icon {
    margin-left: 55px;
    width: 40px;
    img {
      width: 100%;
    }
  }
  .spc-p {
    padding-top: 2px;
  }
}
/*diy查看图片*/
.diy-all {
  .diy-css {
    .banner {
      width: 515px;
      .banner-img {
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 0;
        img {
          height: 397px;
        }
      }
      .el-carousel {
        z-index: 0;
      }
    }
  }
}
</style>
