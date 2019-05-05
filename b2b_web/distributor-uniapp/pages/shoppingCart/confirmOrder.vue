<template>
  <view class="confirmOrder">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>确认订单</view>
      </view>
    </view>
    <!-- 选择地址 -->
    <view class="co-choiceAddress mgTop" @click="toAddress">
      <view
        class="co-choiceAddress-Lf"
        :class="{ 'no-address': addressLists.length == 0 }"
      >
        <text class="iconfont icon-Addresslocation co-locationIcon"></text>
        <view class="co-choiceAddress-NO" v-if="addressLists.length == 0"
          >请选择地址
        </view>
        <view class="co-choiceAddress-center" v-if="addressLists.length > 0">
          <view class="sc-address-top">
            <!-- addressInfo.defaultFlag==1 -->
            <text class="co-address-default" v-if="addressInfo.defaultFlag == 1"
              >默认
            </text>
            <view>
              {{ addressInfo.provinceName ? addressInfo.provinceName : "" }}
              {{ addressInfo.cityName ? addressInfo.cityName : "" }}
              {{ addressInfo.districtName ? addressInfo.districtName : "" }}
            </view>
          </view>
          <view class="co-address-detail">{{ addressInfo.address }}</view>
          <view class="co-address-phone">
            <text>{{ addressInfo.userName }}</text>
            <text>{{ addressInfo.phone }}</text>
          </view>
        </view>
      </view>
      <image src="../../static/img/auditTo_icon.png" class="toIcon"></image>
    </view>

    <!--列表 -->
    <view class="co-listModule">
      <view
        class="co-listModule-line"
        v-if="splitItemList && splitItemList.length > 0"
      >
        <view class="co-listModule-lineTitle">包裹1-普通商品</view>
        <view
          class="co-listModule-lineBg"
          v-for="(item, sIndex) in splitItemList"
          :key="sIndex"
        >
          <image :src="item.imageUrl"></image>
          <view class="co-lineBg-rg">
            <view class="co-lineBg-rgName">{{ item.itemName }}</view>
            <view class="co-lineBg-rgNum">
              <view>商品编号：{{ item.itemCode }}</view>
              <view class="co-lineBg-rgNumPrice">
                <text>￥</text>
                <text>{{ item.lastPrice }}</text>
              </view>
            </view>
            <view class="co-lineBg-rgNum">
              <view class="co-lineBg-rgNum-view">
                <view v-if="item.colorName">颜色：{{ item.colorName }}</view>
                <view v-if="item.specsName">规格：{{ item.specsName }}</view>
                <!-- <view>{{item.boxNum}}/{{item.boxType}}</view> -->
              </view>
              <view class="co-lineBg-rgNum-view">
                <view>在库：{{ item.zaiKuCount }}</view>
                <view>在途：{{ item.zaiTuCount }}</view>
              </view>
            </view>
          </view>
        </view>
      </view>

      <view
        class="co-listModule-line"
        style="margin-top: 20rpx"
        v-if="presentList && presentList.length > 0"
      >
        <view class="co-listModule-lineTitle">赠品</view>
        <view
          class="co-listModule-lineBg"
          v-for="(item, preIndex) in presentList"
          :key="preIndex"
        >
          <image :src="item.imageUrl ? item.imageUrl : item.imageUrl1"></image>
          <view class="co-lineBg-rg">
            <view class="co-lineBg-rgName">{{ item.itemName }}</view>
            <view class="co-lineBg-rgNum">
              <view>商品编号：{{ item.itemCode }}</view>
              <view class="co-lineBg-rgNumPrice"></view>
            </view>
            <view class="co-lineBg-rgNum">
              <view class="co-lineBg-rgNum-view">
                <view v-if="item.colorName">颜色：{{ item.colorName }}</view>
                <view v-if="item.specsName">规格：{{ item.specsName }}</view>
              </view>
              <view class="co-lineBg-rgNum-view">
                <view>在库：{{ item.zaiKuCount }}</view>
                <view>在途：{{ item.zaiTuCount }}</view>
              </view>
            </view>
          </view>
        </view>
      </view>

      <view class="co-inform">
        <view class="co-inform-line" v-if="isTwoWay != 'false'">
          <view class="co-inform-title">是否拆单</view>
          <view class="co-inform-RG" @click="openChaiDanPopup">
            <view>
              <text>{{ splitFlagName }}</text>
            </view>
            <image
              src="../../static/img/auditTo_icon.png"
              class="toIcon"
            ></image>
          </view>
        </view>
        <view class="co-inform-line" v-if="distributeInfo.name">
          <view class="co-inform-title">配送方式</view>
          <view class="co-inform-RG" @click="openDistributionPopup">
            <view>
              <text>{{ distributeInfo.name }}</text>
              <text>{{ distributeInfo.description }}</text>
            </view>
            <image
              src="../../static/img/auditTo_icon.png"
              class="toIcon"
            ></image>
          </view>
        </view>
      </view>
    </view>

    <!-- 定制商品 -->
    <view class="co-listModule" v-if="diyGoodsList.length > 0">
      <view
        class="co-listModule-line"
        v-for="(diyItem, diyIndex) in diyGoodsList"
        :key="diyIndex"
        style="margin-top: 20rpx"
      >
        <view
          class="co-listModule-lineTitle"
          v-if="splitItemList.length > 0 || presentList.length > 0"
          >包裹{{ diyIndex + 2 }}-定制商品
        </view>
        <view class="co-listModule-lineTitle" v-else
          >包裹{{ diyIndex + 1 }}-定制商品
        </view>
        <view
          class="co-listModule-lineBg"
          v-for="(item, index) in diyItem.children"
          :key="index"
        >
          <image :src="item.diy.previewImage" mode="aspectFit"></image>
          <view class="co-lineBg-rg">
            <view class="co-lineBg-rgName">{{ item.itemName }}</view>
            <view class="co-lineBg-rgNum">
              <view>商品编号：{{ item.itemCode }}</view>
              <view class="co-lineBg-rgNumPrice">
                <text>￥</text>
                <text>{{ item.lastPrice }}</text>
              </view>
            </view>
            <view class="co-lineBg-rgNum">
              <view class="co-lineBg-rgNum-view">
                <view v-if="item.diy.materialName"
                  >材质：{{ item.diy.materialName }}
                </view>
                <view v-if="item.diy.modelName"
                  >型号：{{ item.diy.modelName }}
                </view>
              </view>
            </view>
          </view>
        </view>
        <view class="co-inform">
          <view class="co-inform-line">
            <view class="co-inform-title">配送方式</view>
            <view class="co-inform-RG" @click="openDiyDeliveryPopup(diyItem)">
              <view>
                <text>{{ diyItem.choosedDistri.name }}</text>
                <text>{{ diyItem.choosedDistri.description }}</text>
              </view>
              <image
                src="../../static/img/auditTo_icon.png"
                class="toIcon"
              ></image>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- inform-1 -->
    <view class="co-inform margin-top">
      <view class="co-inform-line">
        <view class="co-inform-title">购买数量</view>
        <view class="co-inform-RG">
          <view>
            <text class="co-orangColor">共 {{ summation.totalCount }} 件</text>
          </view>
          <!-- <image src="../../static/img/auditTo_icon.png" class="toIcon"></image> -->
        </view>
      </view>
      <view class="co-inform-line">
        <view class="co-inform-title">开具发票</view>
        <view class="co-inform-RG" @click="openInvoicePopup">
          <view v-if="invoiceType === 0">
            <text>不开具发票</text>
          </view>
          <view v-else>
            <text>{{ voiceType }}</text>
            <text v-if="invoiceType === 1">{{ voiceTitle }}</text>
          </view>
          <image src="../../static/img/auditTo_icon.png" class="toIcon"></image>
        </view>
      </view>
      <view class="co-inform-line2">
        <view class="co-inform-title">买家留言</view>
        <view class="co-inform-RG">
          <textarea
            placeholder="选填(请输入和客户协商好的备注)"
            v-model="remark"
          ></textarea>
        </view>
      </view>
    </view>

    <!-- 费用计算 -->
    <view class="co-inform margin-top">
      <view class="co-inform-line3">
        <view class="co-inform-title">商品总价</view>
        <view>￥{{ summation.totalPrice.toFixed(2) }}</view>
      </view>

      <view class="co-inform-line3">
        <view class="co-inform-title">配送费</view>
        <view>￥{{ summation.shippingPrice.toFixed(2) }}</view>
      </view>

      <view class="co-inform-line3" v-if="summation.discountPrice > 0">
        <view class="co-inform-title">活动优惠</view>
        <view>￥{{ summation.discountPrice.toFixed(2) }}</view>
      </view>

      <view class="co-inform-line">
        <view class="co-inform-title">代金券</view>
        <view class="co-inform-RG">
          <view v-if="rebateVoucherAmount > 0" @click="openVoucherPopup">
            <text>-￥{{ Number(rebateVoucherAmount).toFixed(2) }}</text>
          </view>
          <view v-else>
            <text>无可用</text>
          </view>
          <image src="../../static/img/auditTo_icon.png" class="toIcon"></image>
        </view>
      </view>
    </view>

    <view class="co-btm">
      <view class="co-btm-Lf">
        <text>应付总额</text><text>(含运费)</text>
        <text
          >￥{{
            Number(summation.orderPrice - rebateVoucherAmount).toFixed(2)
          }}</text
        >
      </view>
      <view class="co-btm-rg" @click="submitOrder()" v-if="isAgainFlag == false"
        >提交订单
      </view>
      <view class="co-btm-rg co-btm-btnNo" v-if="isAgainFlag == true"
        >提交订单
      </view>
    </view>

    <!--选择配送方式 弹框 - 普通商品 -->
    <uni-popup ref="distributionPopup" type="bottom" class="myCollect-popup">
      <view class="myCollect-popup-content">
        <view class="myCollect-popup-title">
          <text>配送方式</text>
          <image
            src="../../static/imgs/icon_close.png"
            class="popup-colose"
            @click="closeDistributionPopup"
          ></image>
        </view>

        <view class="co-ps-list" @touchmove.stop.prevent="disabledScroll">
          <scroll-view
            :scroll-top="scrollTop"
            scroll-y="true"
            class="co-ps-listLine-Y"
          >
            <view
              class="co-ps-listLine"
              v-for="(item, index) in distributeList"
              :key="index"
            >
              <view class="co-ps-list-Lf" @click="selectPeiSongFun(item.id)">
                <text
                  class="iconfont"
                  :class="
                    item.isCheck
                      ? 'icon-icon_selected co-iconCheck'
                      : 'icon-icon_selected_def co-iconCheckN'
                  "
                ></text>
                <view class="co-ps-listInform">
                  <view>{{ item.name }}</view>
                  <view>{{ item.description }}</view>
                </view>
              </view>
              <view class="co-ps-listPrice">
                <text>￥</text>
                <text>{{ item.cost }}</text>
              </view>
            </view>
          </scroll-view>
        </view>

        <view class="myCollect-popup-btm" style="z-index: 1000 !important">
          <view class="myCollect-popup-btn" @click="peiSongConfirm()">
            <text class="popup-tijiao">确定</text>
          </view>
        </view>
      </view>
    </uni-popup>

    <!--选择配送方式 弹框 - 定制商品 -->
    <uni-popup ref="diyDeliveryPopup" type="bottom" class="myCollect-popup">
      <view class="myCollect-popup-content">
        <view class="myCollect-popup-title">
          <text>配送方式</text>
          <image
            src="../../static/imgs/icon_close.png"
            class="popup-colose"
            @click="closeDiyDeliveryPopup"
          ></image>
        </view>

        <view class="co-ps-list" @touchmove.stop.prevent="disabledScroll">
          <scroll-view
            :scroll-top="scrollTop"
            scroll-y="true"
            class="co-ps-listLine-Y"
          >
            <view
              class="co-ps-listLine"
              v-for="(item, index) in diyDistributeList"
              :key="index"
            >
              <view class="co-ps-list-Lf" @click="selectDiyPeiSongFun(item.id)">
                <text
                  class="iconfont"
                  :class="
                    item.isCheck
                      ? 'icon-icon_selected co-iconCheck'
                      : 'icon-icon_selected_def co-iconCheckN'
                  "
                ></text>
                <view class="co-ps-listInform">
                  <view>{{ item.name }}</view>
                  <view>{{ item.description }}</view>
                </view>
              </view>
              <view class="co-ps-listPrice">
                <text>￥</text>
                <text>{{ item.cost }}</text>
              </view>
            </view>
          </scroll-view>
        </view>

        <view class="myCollect-popup-btm" style="z-index: 1000 !important">
          <view class="myCollect-popup-btn" @click="diyPeiSongConfirm()">
            <text class="popup-tijiao">确定</text>
          </view>
        </view>
      </view>
    </uni-popup>

    <!--选择是否拆单 弹框 -->
    <uni-popup ref="chaidanPopup" type="bottom" class="myCollect-popup">
      <view class="myCollect-popup-content chaidan-popup">
        <view class="myCollect-popup-title">
          <text>是否拆单</text>
          <image
            src="../../static/imgs/icon_close.png"
            class="popup-colose"
            @click="closeChaiDanPopup"
          ></image>
        </view>

        <view class="co-ps-list">
          <view
            class="co-ps-listLine2"
            v-for="(item, index) in splitFlagList"
            :key="index"
          >
            <text
              class="iconfont"
              :class="
                item.isCheck
                  ? 'icon-Checkthe co-iconCheck'
                  : 'icon-uncheck co-iconCheckN'
              "
              @click="selectChaiDanFun(item.id)"
            ></text>
            <view class="co-ps-listInform">
              <view>{{ item.name }}</view>
              <view>{{ item.tip }}</view>
            </view>
          </view>
        </view>

        <view class="myCollect-popup-btm">
          <view class="myCollect-popup-btn">
            <text class="popup-tijiao" @click="splitConfirm">确定</text>
          </view>
        </view>
      </view>
    </uni-popup>

    <!--选择发票 弹框 -->
    <uni-popup ref="invoicePopup" type="bottom" class="myCollect-popup">
      <view class="myCollect-popup-content invoice-popup">
        <view class="myCollect-popup-title">
          <text>发票</text>
          <image
            src="../../static/imgs/icon_close.png"
            class="popup-colose"
            @click="closeInvoicePopup"
          ></image>
        </view>
        <scroll-view
          :scroll-top="scrollTop"
          scroll-y="true"
          class="co-ps-listLine-Y"
        >
          <view class="invoice-popup-list">
            <view class="invoice-popupTitle">发票类型</view>
            <view class="invoice-popup-line">
              <text
                v-for="(item, iIndex) in invoiceTypeList"
                :key="iIndex"
                @click="invoiceTypeClick(item)"
                :class="invoiceType == item.id ? 'invoiceHover' : ''"
                >{{ item.text }}
              </text>
            </view>
            <view class="invoice-popupTitle">寄往省份</view>
            <view class="invoice-popup-line">
              <text
                v-for="(item, pIndex) in provinceTypeList"
                :key="pIndex"
                @click="provinceTypeClick(item)"
                :class="provinceType == item.id ? 'invoiceHover' : ''"
                >{{ item.text }}
              </text>
            </view>
            <view class="invoice-popupTitle" v-if="invoiceType == 1"
              >发票抬头</view
            >
            <view class="invoice-popup-line" v-if="invoiceType == 1">
              <text
                v-for="(item, index) in invoiceTitleList"
                :key="index"
                @click="invoiceHeaderClick(item)"
                :class="invoiceTitle == item.id ? 'invoiceHover' : ''"
                >{{ item.text }}
              </text>
            </view>
            <view
              class="invoice-popup-line2"
              v-if="invoiceTitle == 1 && invoiceType == 1"
            >
              <text>姓名</text>
              <input v-model="userInfo.name" :disabled="true" />
            </view>
            <view
              class="invoice-popup-line2"
              v-if="invoiceTitle == 2 && userInfo.financial"
            >
              <text>单位名称</text>
              <input
                v-model="userInfo.financial.invoiceTitleName"
                :disabled="true"
              />
            </view>
            <view
              class="invoice-popup-line2"
              v-if="invoiceTitle == 2 && userInfo.financial"
            >
              <text>纳税人识别号</text>
              <input
                v-model="userInfo.financial.taxpayerNumber"
                :disabled="true"
              />
            </view>
            <view
              class="invoice-popup-line2"
              v-if="invoiceTitle == 2 && invoiceType == 2 && userInfo.financial"
            >
              <text>开票地址</text>
              <input
                v-model="userInfo.financial.registeredAddress"
                :disabled="true"
              />
            </view>
            <view
              class="invoice-popup-line2"
              v-if="invoiceTitle == 2 && invoiceType == 2 && userInfo.financial"
            >
              <text>电话</text>
              <input
                v-model="userInfo.financial.registeredPhone"
                :disabled="true"
              />
            </view>
            <view
              class="invoice-popup-line2"
              v-if="invoiceTitle == 2 && invoiceType == 2 && userInfo.financial"
            >
              <text>开户银行</text>
              <input
                v-model="userInfo.financial.registeredBankDepositName"
                :disabled="true"
              />
            </view>
            <view
              class="invoice-popup-line2"
              v-if="invoiceTitle == 2 && invoiceType == 2 && userInfo.financial"
            >
              <text>银行账户</text>
              <input
                v-model="userInfo.financial.registeredBankAccount"
                :disabled="true"
              />
            </view>
          </view>
        </scroll-view>

        <view class="myCollect-popup-btm">
          <view class="invoice-popup-tip"
            >首次开发票的客户，请提供相关证件和合同原件给我司，
            以避免造成无法开票问题</view
          >
          <view class="myCollect-popup-btn">
            <text class="popup-tijiao" @click="fpSubmitFun">确定</text>
          </view>
        </view>
      </view>
    </uni-popup>

    <!-- 选择代金券 弹框 -->
    <uni-popup
      ref="voucherPopup"
      type="bottom"
      class="myCollect-popup voucher-popup"
    >
      <view class="myCollect-popup-content">
        <view class="myCollect-popup-title">
          <text>代金券</text>
          <image
            src="../../static/imgs/icon_close.png"
            class="popup-colose"
            @click="closeVoucherPopup"
          ></image>
        </view>

        <view class="co-ps-list" @touchmove.stop.prevent="disabledScroll">
          <scroll-view
            :scroll-top="scrollTop"
            scroll-y="true"
            class="co-ps-listLine-Y"
          >
            <view
              class="co-ps-listLine"
              v-for="item in voucherList"
              :key="item.id"
              @click="chooseVoucher(item)"
            >
              <view class="co-ps-list-Lf">
                <text
                  class="iconfont"
                  :class="
                    item.choosed
                      ? 'icon-icon_selected co-iconCheck'
                      : 'icon-icon_selected_def co-iconCheckN'
                  "
                ></text>
                <view class="co-ps-listInform">
                  <view>{{ item.name }}</view>
                  <view>券号: {{ item.voucherNo }}</view>
                  <view
                    >有效期: {{ item.startTime | formatDate }} 至
                    {{ item.endTime | formatDate }}
                  </view>
                </view>
              </view>
              <view class="co-ps-listPrice">
                <text>余额: ￥</text>
                <text>{{ item.balance }}</text>
              </view>
            </view>
          </scroll-view>
        </view>
        <view class="myCollect-popup-btm" style="z-index: 1000 !important">
          <view class="myCollect-popup-btn" @click="closeVoucherPopup">
            <text class="popup-tijiao">确定</text>
          </view>
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<script>
import {
  addressList,
  addressApi,
  logisticsCalculations,
  paymentMode,
  userDeposit,
  userInfo,
  placeOrder,
  orderResults,
  payCreateTrade,
  logisticsCalculationss,
  deleteShoppingcart,
  regionList,
  region2,
  programMiniOpenId,
  voucherList,
} from "../../common/api.js";
import { isH5, formatDate } from "../../common/common.js";
export default {
  data() {
    return {
      scrollTop: 0,
      invoiceType: 0, //发票类型
      invoiceTypeList: [
        { id: 0, text: "不开具" },
        { id: 1, text: "普通发票" },
        { id: 2, text: "增值税发票" },
      ],
      voiceType: "", // 发票类型值
      provinceTypeList: [
        { id: 0, text: "广东省内" },
        { id: 1, text: "广东省外" },
      ],
      provinceType: 0,
      invoiceTitleList: [
        { id: 1, text: "个人" },
        { id: 2, text: "单位" },
      ],
      invoiceTitle: 2,
      voiceTitle: "单位", // 发票抬头值
      addressLists: [], //收货地址
      addressInfo: {}, //地址信息
      addressIndex: 100, // 地址选中索引
      countryId: "",
      countryName: "",
      provinceId: "",
      cityId: "",
      townId: "", //区域id
      deliveryId: "", // 收货地址编号
      userName: "",
      zipCode: "",
      phone: "",
      isTwoWay: false, //是否有两种库存
      splitFlagList: [
        {
          id: 1,
          isCheck: false,
          name: "库存拆分发货",
          tip: "订单将会根据库存拆分为两个订单分别发出，邮费可能会 增加",
        },
        {
          id: 0,
          isCheck: true,
          name: "到货统一发货",
          tip: "在库在途商品到仓库后再统一发货",
        },
      ],
      splitFlagId: 0, //选择的拆单的id
      splitFlagName: "到货统一发货", //拆单名称
      showItemList: [], //订单列表
      summation: {
        totalCount: 0, //购买总数
        totalPrice: 0, //下单原总价
        discountPrice: 0, //优惠价格
        shippingPrice: 0, //配送费用
        orderPrice: 0, //订单总金额
        useRange: "", //适用范围 1.普通商品，2.定制商品 3 普通商品和定制商品
        weight: 0, //订单总重量
      }, // 总计obj
      materialId: [], //定制的材质id
      distributeList: [], // 配送方式列表 - 普通商品
      diyDistributeList: [], // 配送方式列表 - 定制商品
      curHandleDiy: {}, // 当前操作定制商品列表
      curVal: "", // 选择的配送方式名称
      curId: "", // 选择的配送方式id
      distributeInfo: {}, //选择的物流信息
      shippingPrice: 0, //普通商品--配送费用
      diyShippingPrice: 0, //定制商品--配送费用
      diyGoodsList: [], //定制订单
      remark: "", // 留言
      userInfo: {}, //用户信息
      isAgainFlag: false, //提交订单是否点击过
      enterFlag: "", // 跳转来源
      enterParams: [], // 跳转传参
      // 代金券
      voucherList: [], // 代金券列表
      choosedVoucherAmount: 0, // 已选择代金券总金额
      rebateVoucherAmount: 0, // 代金券使用总金额
      rebateVoucherIds: [], // 代金券使用id列表
    };
  },
  computed: {
    // 购买商品列表(定制)
    splitDiyItemList() {
      return this.showItemList.filter((data) => {
        return data.itemType == 1 && data.goodsType == 2;
      });
    },
    // 购买商品列表(非赠品)
    splitItemList() {
      return this.showItemList.filter((data) => {
        return data.itemType == 1 && data.goodsType == 1;
      });
    },
    // 购买商品列表（赠品）
    presentList() {
      return this.showItemList.filter((data) => {
        return data.itemType == 2 && data.goodsType == 1;
      });
    },
  },
  filters: {
    formatDate(time) {
      var date = new Date(time);
      return formatDate(date, "yyyy-MM-dd");
    },
  },
  onShow() {
    this.getCountry(); //获取国家列表，获取收货地址
  },
  onLoad(option) {
    if (option.enterFlag) {
      this.enterFlag = option.enterFlag;
      this.enterParams = JSON.parse(option.enterParams);
    }

    this.isTwoWay = option.isTwoWay ? option.isTwoWay : "false";
    this.goodListFun(); //处理货品数据
    this.getUserInfoFun();

    this.getVoucherList(); // 代金券
  },
  onHide() {},
  onUnload() {
    uni.removeStorageSync("orderAddress");
  },
  methods: {
    disabledScroll() {
      return;
    },
    //返回
    toback() {
      uni.navigateBack({
        delta: 1,
      });
    },

    // 获取区域列表type:(1:国家，2：省，3：市，4：区)-y
    regionListFun(parentId, id, type, noAddress) {
      let that = this;
      region2(parentId).then((res) => {
        let list = res.data.list;
        if (type == 1) {
          that.countryList = list;
          if (
            noAddress !== "noList" ||
            noAddress == null ||
            noAddress == "undefined" ||
            noAddress == ""
          ) {
            that.addressListFun(); //获取收货地址列表
          }
          // console.log('国家：',res.data.list);
        } else if (type == 2) {
          that.provinceList = list;
          // console.log('省：',res.data.list);
        } else if (type == 3) {
          that.cityList = list;
          // console.log('市：',res.data.list);
        } else if (type == 4) {
          that.areaList = list;
          // console.log('区：',res.data.list);
        }
      });
    },
    // 获取国家列表-y
    getCountry(noList) {
      this.regionListFun(0, -1, 1, noList);
      // this.countryId=37;
    },
    // 地址列表
    addressListFun() {
      let that = this;
      let id = uni.getStorageSync("userId");
      let params = {
        id: id,
        page: 1,
        size: 100,
      };

      addressList(params).then((res) => {
        if (res.success) {
          let addressList = res.data.list;
          console.log("收货地址", addressList);
          //  provinceName cityName districtName
          let list = res.data.list;
          let ResItem = {};
          let defaultIndex = 0;
          list.forEach((item, index) => {
            region2(item.countryId).then((countryRes) => {
              if (countryRes.success) {
                let provinceList = countryRes.data.list;
                provinceList.forEach((item2) => {
                  if (item2.id == item.provinceId) {
                    item.provinceName = item2.regionName;
                    region2(item.provinceId).then((provinRes) => {
                      if (provinRes.success) {
                        let cityList = provinRes.data.list;
                        cityList.forEach((item3) => {
                          if (item3.id == item.cityId) {
                            item.cityName = item3.regionName;

                            region2(item.cityId).then((cityRes) => {
                              if (cityRes.success) {
                                let areaList = cityRes.data.list;
                                areaList.forEach((item4) => {
                                  if (item4.id == item.districtId) {
                                    item.districtName = item4.regionName;
                                  }
                                });
                              }
                            });
                          }
                        });
                      }
                    });
                  }
                });
              }
            });

            if (item.defaultFlag == 1) {
              defaultIndex = index;
            }
          });
          ResItem = list[defaultIndex];
          // 默认选中第一条
          let orderAddress = uni.getStorageSync("orderAddress");
          let addressItem =
            orderAddress && orderAddress != "undefined"
              ? orderAddress
              : ResItem;
          if (addressItem) {
            that.addressInfo = addressItem;
            that.addressIndex = 0;
            that.countryId = addressItem.countryId;
            that.deliveryId = addressItem.id;
            that.provinceId = addressItem.provinceId;
            that.cityId = addressItem.cityId;
            that.townId = addressItem.districtId;
            that.userName = addressItem.userName;
            that.zipCode = addressItem.zipCode;
            that.phone = addressItem.phone;
            let countryList = that.countryList;
            countryList.forEach((items) => {
              if (items.id == addressItem.countryId) {
                that.countryName = items.regionName;
              }
            });
          }

          setTimeout(() => {
            console.log("下单地址：", addressList);
            that.addressLists = list;
            this.diyListFun(); //处理定制货品列表（包括物流信息）
            that.postList();
          }, 300);
        }
      });
    },

    // 选择地址
    toAddress() {
      uni.navigateTo({
        url: "../set/addressList?isTwoWay=" + this.isTwoWay,
      });
    },
    // 打开拆单弹框
    openChaiDanPopup() {
      this.$refs.chaidanPopup.open();
    },
    // 关闭拆单弹框
    closeChaiDanPopup() {
      this.$refs.chaidanPopup.close();
    },
    // 拆单单选
    selectChaiDanFun(id) {
      this.splitFlagList.forEach((item) => {
        if (item.id == id) {
          item.isCheck = !item.isCheck;
        } else {
          item.isCheck = false;
        }
      });
    },
    // 拆单确定-Y
    splitConfirm() {
      let that = this;

      this.splitFlagList.forEach((item) => {
        if (item.isCheck) {
          that.splitFlagName = item.name;
          that.splitFlagId = item.id;
        }
      });
      this.postList(); //计算物流费用 （拆单和不拆单物流费用计算方式不同）

      this.$refs.chaidanPopup.close();
    },

    // 处理下单的货品
    goodListFun() {
      let that = this;

      let goodList = "";
      if (this.enterFlag) {
        goodList = this.enterParams;
      } else {
        goodList = JSON.parse(uni.getStorageSync("shopOrderList"));
      }
      this.showItemList = goodList;

      this.summation = {
        totalCount: 0,
        totalPrice: 0, //下单原总价
        discountPrice: 0, //优惠价格
        shippingPrice: 0, //配送费用
        orderPrice: 0, //订单总金额
        useRange: 1, //适用范围 1.普通商品，2.定制商品 3 普通商品和定制商品
        weight: 0, //订单总重量
      };
      let promitionPrice = 0;
      let puType = false;
      let dzType = false;
      goodList.forEach((item) => {
        if (item.itemType == 1) {
          that.summation.totalPrice +=
            item.salePrice * (item.zaiKuCount + item.zaiTuCount);
          promitionPrice += Number(item.lastPrice); //优惠后的总价

          if (item.goodsType == 2) {
            that.materialId.push(item.diy.materialId); //定制订单材质id
            dzType = true;
          } else {
            puType = true;
          }
          if (item.weight && item.weight > 0) {
            that.summation.weight +=
              item.weight * (item.zaiKuCount + item.zaiTuCount);
          }
        }
        that.summation.totalCount += item.zaiKuCount + item.zaiTuCount;

        that.summation.discountPrice =
          that.summation.totalPrice - promitionPrice; //优惠差额
      });

      if (puType && !dzType) {
        this.summation.useRange = 1;
      } else if (!puType && dzType) {
        this.summation.useRange = 2;
      } else if (puType && dzType) {
        this.summation.useRange = 3;
      }
      console.log("下单数据处理2：", goodList);
      //  this.summation.orderPrice=this.summation.totalPrice-this.summation.discountPrice+
    },

    // 购买商品列表（定制）
    diyListFun() {
      let userId = uni.getStorageSync("userId");
      let goodList = [];
      if (this.enterFlag) {
        goodList = this.enterParams;
      } else {
        goodList = JSON.parse(uni.getStorageSync("shopOrderList"));
      }
      // let goodList=JSON.parse(uni.getStorageSync('shopOrderList'));
      let list = goodList.filter((data) => {
        return data.goodsType == 2;
      });
      if (list.length > 0) {
        console.log("DINGZHIHIIHI定制：", list);
        list.forEach((item) =>
          this.$set(item, "manufactors", item.diy.manufactors)
        );
        this.diyGoodsList = this.sort_pro(list, ["manufactors"]);
        console.log("dingzhi---", this.diyGoodsList);
        let diyList = this.diyGoodsList;
        let wuliuPrice = 0;
        for (let i = 0; i < diyList.length; i++) {
          let weight = 0;
          let price = 0;
          for (let j = 0; j < diyList[i].children.length; j++) {
            console.log("dssd :", diyList[i].children[j].weight);
            weight +=
              diyList[i].children[j].weight * diyList[i].children[j].itemCount;
            price +=
              diyList[i].children[j].salePrice *
              diyList[i].children[j].itemCount;
          }
          console.log("定制重量1：", weight);
          let params = [
            {
              distributorId: userId, //分销商id
              billingMethod: 1, //订单结算方式，1重量计费，2体积计费
              countryId: this.countryId,
              provinceId: this.provinceId,
              cityId: this.cityId,
              manufactors: diyList[i].manufactors, //工厂--生产商
              price: price.toFixed(2), //订单总金额
              useRange: 2, //适用范围 1.普通商品，2.定制商品 3 普通商品和定制商品
              weight: weight, //订单总重量
            },
          ];

          // 获取配送方式
          logisticsCalculationss(params).then((res) => {
            if (res.success) {
              let ocjV = {
                logisticsId: res.data[0].id, //配送方式id
                logisticsName: res.data[0].name, //配送方式名称
                logisticsType: 2, //配送方式类型：1 普通商品（标品） 2 定制工厂
                manufactors: diyList[i].manufactors, //生产商 YC云创 MK麦客 LHW联辉王
              };
              this.$set(diyList[i], "distributeList", res.data); //配送方式列表
              this.$set(diyList[i], "choosedDistri", res.data[0]); //默认配送方式
              this.$set(diyList[i], "logisticss", ocjV);
              this.$set(diyList[i], "cost", res.data[0].cost);
              this.$set(diyList[i], "curVal", res.data[0].name);
              this.$set(diyList[i], "curId", res.data[0].id);
              this.$set(diyList[i], "postPrompt", false);
              wuliuPrice += res.data[0].cost;

              this.diyShippingPrice = wuliuPrice; //定制物流费用
              this.summation.shippingPrice =
                wuliuPrice + (this.shippingPrice ? this.shippingPrice : 0);
              this.summation.orderPrice =
                this.summation.totalPrice -
                this.summation.discountPrice +
                wuliuPrice +
                (this.shippingPrice ? this.shippingPrice : 0); //订单总价
            }
          });
        }
        console.log("定制商品", this.diyGoodsList);
      }
    },
    // 把对象数组按照某一个属性（或某几个属性）进行分类
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

    // 打开配送方式弹框
    openDistributionPopup() {
      this.$refs.distributionPopup.open();
    },
    // 关闭配送方式弹框
    closeDistributionPopup() {
      this.$refs.distributionPopup.close();
    },
    // 获取配送方式列表--普通商品-Y
    postList() {
      let that = this;
      let goodList1 = "";
      if (this.enterFlag) {
        goodList1 = this.enterParams;
      } else {
        goodList1 = JSON.parse(uni.getStorageSync("shopOrderList"));
      }
      let goodList = goodList1.filter((item) => item.goodsType == 1);
      console.log("pes配送商品列表：", goodList);
      if (goodList.length > 0) {
        let price = 0;
        let zaituWeight = 0;
        let zaikuWeight = 0;
        let zaituPrice = 0;
        let zaikuPrice = 0;
        for (let i = 0; i < goodList.length; i++) {
          if (goodList[i].itemType == 1) {
            price += Number(goodList[i].lastPrice);
            zaituPrice +=
              (Number(goodList[i].lastPrice) / goodList[i].itemCount) *
              goodList[i].zaiTuCount;
            zaikuPrice +=
              (Number(goodList[i].lastPrice) / goodList[i].itemCount) *
              goodList[i].zaiKuCount;
          }
          if (goodList[i].weight && goodList[i].weight != 0) {
            zaituWeight += goodList[i].weight * goodList[i].zaiTuCount;
            zaikuWeight += goodList[i].weight * goodList[i].zaiKuCount;
          }
        }
        let userId = uni.getStorageSync("userId");
        let obj1 = [
          {
            distributorId: userId, //分销商id
            billingMethod: 1, //订单结算方式，1重量计费，2体积计费
            countryId: this.countryId,
            provinceId: this.provinceId,
            cityId: this.cityId,
            price: zaituPrice, //订单总金额
            useRange: 1, //适用范围 1.普通商品，2.定制商品 3 普通商品和定制商品
            weight: zaituWeight, //订单总重量
          },
        ];
        let params = [
          {
            distributorId: userId, //分销商id
            billingMethod: 1, //订单结算方式，1重量计费，2体积计费
            countryId: this.countryId,
            provinceId: this.provinceId,
            cityId: this.cityId,
            price: this.splitFlagId == 1 ? zaikuPrice : price, //订单总金额
            useRange: 1, //适用范围 1.普通商品，2.定制商品 3 普通商品和定制商品
            weight:
              this.splitFlagId == 1 ? zaikuWeight : zaikuWeight + zaituWeight, //订单总重量
          },
        ];
        params = this.splitFlagId == 1 ? [...params, ...obj1] : [...params];

        logisticsCalculationss(params).then((res) => {
          if (res.success) {
            let peisongList = res.data;
            peisongList.forEach((peiItem, index) => {
              that.$set(peiItem, "isCheck", false);
              if (index === 0) {
                that.$set(peiItem, "isCheck", true);
              }
            });
            that.distributeList = peisongList;
            that.curVal = res.data[0].name;
            that.curId = res.data[0].id;
            that.distributeInfo = res.data[0];
            that.shippingPrice = res.data[0].cost;
            if (that.diyGoodsList.length > 0) {
              let list = that.diyGoodsList;
              let costPrice = 0;
              for (let i = 0; i < list.length; i++) {
                costPrice += list[i].cost;
              }
              that.summation.shippingPrice = costPrice + res.data[0].cost;
              that.summation.orderPrice = (
                Number(that.summation.totalPrice) -
                Number(that.summation.discountPrice) +
                Number(res.data[0].cost) +
                Number(costPrice)
              ).toFixed(2);
              console.log(res.data[0].cost + "," + costPrice);
            } else {
              that.summation.shippingPrice = res.data[0].cost;
              that.summation.orderPrice = (
                Number(that.summation.totalPrice) -
                Number(that.summation.discountPrice) +
                Number(res.data[0].cost)
              ).toFixed(2);
            }
          } else {
            that.summation.orderPrice = (
              Number(that.summation.totalPrice) -
              Number(that.summation.discountPrice)
            ).toFixed(2);
          }
        });
      }
    },

    // 单选配送方式
    selectPeiSongFun(id) {
      this.distributeList.forEach((item) => {
        if (item.id == id) {
          item.isCheck = !item.isCheck;
        } else {
          item.isCheck = false;
        }
      });
    },

    // 配送方式--确定
    peiSongConfirm() {
      let that = this;
      this.distributeList.forEach((item) => {
        if (item.isCheck) {
          let curCost = item.cost;
          that.curVal = item.name;
          that.curId = item.id;
          that.distributeInfo = item;
          that.summation.shippingPrice =
            that.summation.shippingPrice - that.shippingPrice + curCost; // 计算运费
          that.summation.orderPrice = (
            Number(that.summation.totalPrice) -
            Number(that.summation.discountPrice) +
            Number(that.summation.shippingPrice)
          ).toFixed(2);
          that.shippingPrice = curCost; // 重置当前选择普通商品配送费
        }
      });
      this.$refs.distributionPopup.close();
    },

    // 定制商品 - 配送方式
    // 打开配送方式弹框
    openDiyDeliveryPopup(list) {
      // 当前
      this.curHandleDiy = list;
      if (list.distributeList && list.distributeList.length > 0) {
        list.distributeList.forEach((item) => {
          this.$set(item, "isCheck", false);
          if (item.id === list.curId) {
            this.$set(item, "isCheck", true);
          }
        });
      }
      this.diyDistributeList = list.distributeList;

      this.$refs.diyDeliveryPopup.open();
    },
    // 关闭配送方式弹框
    closeDiyDeliveryPopup() {
      this.$refs.diyDeliveryPopup.close();
    },
    // 单选配送方式
    selectDiyPeiSongFun(id) {
      this.diyDistributeList.forEach((item) => {
        if (item.id == id) {
          this.$set(item, "isCheck", !item.isCheck);
          console.log(item);
        } else {
          this.$set(item, "isCheck", false);
        }
      });
    },
    // 配送方式--确定
    diyPeiSongConfirm() {
      let that = this;
      this.diyDistributeList.forEach((item) => {
        if (item.isCheck) {
          that.summation.shippingPrice =
            that.summation.shippingPrice - this.curHandleDiy.cost + item.cost;
          that.summation.orderPrice = (
            Number(that.summation.totalPrice) -
            Number(that.summation.discountPrice) +
            Number(that.summation.shippingPrice)
          ).toFixed(2);

          // 重置当前选中信息
          this.curHandleDiy.choosedDistri = item;
          this.curHandleDiy.logisticss = item;
          this.curHandleDiy.cost = item.cost;
          this.curHandleDiy.curId = item.id;
          this.curHandleDiy.logisticsType = 2;
          this.curHandleDiy.curVal = item.name;
        }
      });

      // 重置定制商品信息
      this.diyGoodsList.forEach((diy) => {
        if (diy.curId === this.curHandleDiy.curId) {
          diy = this.curHandleDiy;
        }
      });

      this.$refs.diyDeliveryPopup.close();
    },
    // 定制商品 - 配送方式

    // 获得用户信息-Y
    getUserInfoFun() {
      let that = this;
      let id = uni.getStorageSync("userId");
      userInfo({ id: id }).then((res) => {
        if (res.success) {
          that.userInfo = res.data;
        }
      });
    },

    // 打开发票弹框
    openInvoicePopup() {
      this.$refs.invoicePopup.open();
    },
    // 关闭拆单弹框
    closeInvoicePopup() {
      this.$refs.invoicePopup.close();
    },
    // 发票--选择发票类型
    invoiceTypeClick(item) {
      this.invoiceType = item.id;
      this.voiceType = item.text;
      // 默认值
      this.invoiceTitle = 2;
      this.voiceTitle = "单位";
    },

    // 发票---选择省份
    provinceTypeClick(item) {
      this.provinceType = item.id;
    },
    // 发票--抬头
    invoiceHeaderClick(item) {
      this.invoiceTitle = item.id;
      this.voiceTitle = item.text;
    },

    // 开具发票确定按钮  --根据开票类型是否选择了普通还是增殖来确定是否选择了  invoiceType=0不开具发票
    fpSubmitFun() {
      this.$refs.invoicePopup.close();
    },

    // 获取微信小程序登录授权
    programMiniLoginFun() {
      uni.login({
        provider: "weixin",
        success: (res) => {
          // console.log('登录成功：', res);
          //获取临时登录凭证code
          let Code = res.code;
          let params = {
            appId: uni.getStorageSync("appId"),
            code: res.code,
          };
          programMiniOpenId(params).then((res) => {
            console.log("微信获取openid：", res);
            if (res.success) {
              uni.setStorageSync("openId", res.data); //是否开启分销模式 0 不开启, 1 开启
            }
          });
        },
        fail: (err) => {
          console.log("登录失败：", err);
        },
      });
    },

    // 提交订单
    submitOrder() {
      this.isAgainFlag = true;

      // 收货地址编号--判断是否选择收货地址
      if (this.deliveryId === "") {
        uni.showToast({ title: "填写收货人信息", icon: "none" });
        this.isAgainFlag = false;
        return;
      }
      console.log("---", uni.getStorageSync("openId"));
      if (
        !isH5 &&
        (uni.getStorageSync("openId") == null ||
          uni.getStorageSync("openId") == "undefined" ||
          uni.getStorageSync("openId") == "")
      ) {
        uni.showToast({
          title: "获取openId失败，请重新提交订单",
          icon: "none",
        });
        this.programMiniLoginFun();
        this.isAgainFlag = false;
        return;
      }
      let list1 = "";
      if (this.enterFlag) {
        list1 = this.enterParams;
      } else {
        list1 = JSON.parse(uni.getStorageSync("shopOrderList"));
      }
      let list = list1;
      console.log("下单：", list);
      let goodList = [];
      let delegetIds = [];
      for (let i = 0; i < list.length; i++) {
        delegetIds.push(list[i].id);
        let objL = {
          diyType: list[i].diyType,
          goodsPromotionId:
            list[i].goodsPromotionId &&
            list[i].goodsPromotionId != "no" &&
            list[i].conditionsId
              ? list[i].conditionsId
              : "",
          goodsType: list[i].goodsType,
          groupSeckillId:
            list[i].groupSeckillId && list[i].groupSeckillId != "no"
              ? list[i].groupSeckillId
              : "",
          itemCode: list[i].itemCode,
          itemCount: list[i].zaiTuCount + list[i].zaiKuCount,
          itemId: list[i].itemId,
          itemInCount: list[i].zaiKuCount, //下单在库数量
          itemOnWayCount: list[i].zaiTuCount, //下单在途数量
          itemType: list[i].itemType,
          itemMtoCount: 0, //货品预售数量
          mtoType: list[i].mtoFlag ? list[i].mtoFlag : 0, //是否预售 1 是 0 否
          oversoldFlag: 0, //是否支持超卖 1、支持 0 不支持
        };
        if (list[i].goodsType == 2) {
          objL = { ...objL, ...{ diy: list[i].diy } };
        }
        goodList.push(objL);
      }
      let diyList = this.diyGoodsList;
      let logisticss = [];
      for (let i = 0; i < diyList.length; i++) {
        logisticss.push(diyList[i].logisticss);
      }
      // 普通商品物流信息
      if (this.curId != "") {
        let ptLogisticss = {
          logisticsId: this.curId,
          logisticsName: this.curVal,
          logisticsType: 1,
        };
        logisticss.push(ptLogisticss);
      }

      let params = {
        currencyType: "CNY", //币种
        //收货信息 --Y
        delivery: {
          userName: this.userName,
          countryId: this.countryId,
          countryName: this.countryName,
          provinceId: this.provinceId,
          provinceName: this.addressInfo.provinceName,
          cityId: this.cityId ? this.cityId : 0,
          cityName: this.addressInfo.cityName,
          districtId: this.townId ? this.townId : 0,
          districtName: this.addressInfo.districtName,
          address: this.addressInfo.address,
          mobile: this.phone,
          zipCode: this.zipCode ? this.zipCode : "",
        },
        distributionAmount: this.summation.shippingPrice.toFixed(2), //物流费用-y
        // orderAmount:Number(this.summation.orderPrice).toFixed(2),  //商品结算金额(优惠后金额)-y
        orderAmount: Number(
          this.summation.orderPrice - this.rebateVoucherAmount
        ).toFixed(2), //商品结算金额(优惠后金额)-y
        //  订单发票信息
        invoiceFlag: this.invoiceType == 0 ? 0 : 1, //是否开具发票 0.否，1.是-y
        invoice: {
          invoiceType: this.invoiceType, //发票类型 1.普通 2.增值税发票-y
          invoiceTitleType: this.invoiceTitle, //发票抬头类型 1.个人 2.单位-y
          name:
            this.invoiceTitle == 2 && this.userInfo.financial
              ? this.userInfo.financial.invoiceTitleName
              : this.userInfo.name, //发票名称 (个人姓名或单位名称)-Y
          taxpayerNumber:
            this.invoiceTitle == 2 && this.userInfo.financial
              ? this.userInfo.financial.taxpayerNumber
              : "", //纳税人识别号(发票抬头为2时必填)-Y
          bankAccount:
            this.invoiceTitle == 2 && this.userInfo.financial
              ? this.userInfo.financial.registeredBankAccount
              : "", //银行账号(发票抬头为2时必填)-Y
          bankAccountName:
            this.invoiceTitle == 2 && this.userInfo.financial
              ? this.userInfo.financial.registeredBankDepositName
              : "", //银行账户名(发票抬头为2时必填)-Y
          registerPhone:
            this.invoiceTitle == 2 && this.userInfo.financial
              ? this.userInfo.financial.registeredPhone
              : "", //公司注册电话(发票抬头为2时必填)-Y
          registerAddress:
            this.invoiceTitle == 2 && this.userInfo.financial
              ? this.userInfo.financial.registeredAddress
              : "", //公司注册地址(发票抬头为2时必填)-Y
        },
        logisticss: logisticss, //配送方式列表
        onWaySplitFlag: this.splitFlagId, //订单是否拆分(在途在库) 1、拆 0、否-Y
        orderSource: isH5 ? "GF60003" : "GF60002", //订单来源：平台编码 分销PC端B2Bpc和分销移动端B2Bh5
        payWay: 2, //1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,-Y  默认微信
        remark: this.remark, //订单备注-Y
        goodss: goodList, //  商品明细列表
        // 代金券
        rebateVoucherAmount: Number(this.rebateVoucherAmount).toFixed(2), // 代金券使用总金额
        rebateVoucherIds: this.rebateVoucherIds, // 代金券使用id列表
      };
      uni.setStorageSync("orderParams", JSON.stringify(params));
      uni.setStorageSync("delegetIds", JSON.stringify(delegetIds));
      uni.navigateTo({
        url: "checkstand?isTwoWay=" + this.isTwoWay,
      });
      setTimeout(() => {
        this.isAgainFlag = false;
      }, 1000);
    },
    // 打开代金券弹框
    openVoucherPopup() {
      this.$refs.voucherPopup.open();
    },
    // 关闭代金券弹框
    closeVoucherPopup() {
      this.$refs.voucherPopup.close();
    },
    // 代金券
    getVoucherList() {
      let userId = uni.getStorageSync("userId");
      let parmas = {
        page: 1,
        size: 5,
        voucherStatusStr: "5", // 可用
        distributorId: userId,
        // 优先显示快到期的代金券
        sortType: 1, // 1 正序, 2 倒序
        sortColumn: 4, // 1 创建时间, 2 更新时间, 3 开始时间, 4 结束时间
      };
      voucherList(parmas).then((res) => {
        if (res.success) {
          let voucherList = res.data.list;

          voucherList.forEach((item, index) => {
            this.$set(item, "choosed", false);
            // 默认第一个选中
            if (index === 0) {
              this.$set(item, "choosed", true);
            }
          });

          this.voucherList = voucherList;
          this.calcVoucher(voucherList);
        }
      });
    },
    calcVoucher(arr) {
      if (arr && arr.length > 0) {
        this.rebateVoucherIds = [];
        this.choosedVoucherAmount = 0;
        arr.forEach((item) => {
          if (item.choosed) {
            // 已选择代金券总金额
            this.choosedVoucherAmount += item.balance;
            // 代金券使用id列表
            this.rebateVoucherIds.push(item.id);
          }
        });
      }
    },
    // 选择代金券
    chooseVoucher(item) {
      if (item.choosed) {
        this.$set(item, "choosed", false);
      } else {
        if (this.choosedVoucherAmount > this.summation.orderPrice) {
          this.$set(item, "choosed", false);
        } else {
          this.$set(item, "choosed", true);
        }
      }

      this.calcVoucher(this.voucherList);
    },
  },
  watch: {
    choosedVoucherAmount: {
      handler(val) {
        if (val > 0) {
          // 代金券使用总金额
          if (val >= this.summation.orderPrice) {
            this.rebateVoucherAmount = this.summation.orderPrice;
          } else {
            this.rebateVoucherAmount = val;
          }
        } else {
          this.rebateVoucherAmount = 0;
        }
      },
    },
    "summation.orderPrice": {
      handler(val) {
        if (val > 0) {
          // 代金券使用总金额
          if (this.choosedVoucherAmount >= val) {
            this.rebateVoucherAmount = val;
          } else {
            this.rebateVoucherAmount = this.choosedVoucherAmount;
          }
        }
      },
    },
  },
};
</script>

<style lang="scss">
.co-locationIcon {
  font-size: 49rpx;
  color: $base-color1;
}
.co-iconCheck {
  font-size: 35rpx;
  color: $base-color1;
}
.co-iconCheckN {
  font-size: 35rpx;
  color: #999;
}
.confirmOrder {
  font-size: 26rpx;
  padding-bottom: 140rpx;
  .co-choiceAddress {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: #fff;
    .co-choiceAddress-Lf {
      display: flex;

      &.no-address {
        padding: 20rpx 0;
      }
    }
    .co-choiceAddress-NO {
      font-size: 28rpx;
      margin-left: 30rpx;
      height: 50rpx;
      line-height: 50rpx;
    }
    .co-choiceAddress-center {
      width: 580rpx;
      margin-left: 20rpx;
      .sc-address-top {
        display: flex;
        .co-address-default {
          background: rgba($color: $base-color1, $alpha: 0.1);
          border-radius: 5rpx;
          color: $base-color1;
          font-size: 20rpx;
          display: block;
          padding: 3rpx 10rpx;
          margin-right: 15rpx;
        }
        view {
          width: 470rpx;
          color: #666666;
        }
      }
      .co-address-detail {
        font-size: 28rpx;
        font-weight: bold;
        margin-top: 15rpx;
      }
      .co-address-phone {
        margin-top: 15rpx;
        color: #666;
        text:nth-child(2) {
          margin-left: 30rpx;
        }
      }
    }
  }
  .co-listModule {
    margin-top: 20rpx;
    .co-listModule-line {
      background: #fff;
      .co-listModule-lineTitle {
        padding: 25rpx 30rpx;
        border-bottom: 1rpx solid $opacity-border;
        font-size: 30rpx;
        color: $uni-text-color;
        font-weight: bold;
      }
      .co-listModule-lineBg {
        display: flex;
        padding: 30rpx 0;
        // box-shadow: 0rpx 5rpx 19rpx 1rpx rgba(9, 213, 238, 0.08);
        border-bottom: 1rpx solid $opacity-border;
        margin: 0 30rpx;
        image {
          width: 160rpx;
          height: 160rpx;
        }
        .co-lineBg-rg {
          margin-left: 30rpx;
          width: 500rpx;
          .co-lineBg-rgName {
            font-weight: bold;
            margin-bottom: 20rpx;
          }
          .co-lineBg-rgNum {
            color: #999;
            font-size: 24rpx;
            margin-top: 8rpx;
            display: flex;
            align-items: center;
            justify-content: space-between;
            width: 500rpx;
            .co-lineBg-rgNumPrice {
              color: #ed5307;
              text:nth-child(1) {
                font-size: 22rpx;
              }
              text:nth-child(2) {
                font-size: 34rpx;
              }
            }
            .co-lineBg-rgNum-view {
              view {
                margin-bottom: 5rpx;
              }
            }
          }
        }
      }
      .co-listModule-lineSm {
        display: flex;
        justify-content: space-between;
        padding: 30rpx;
        .co-smText {
          font-size: 22rpx;
          color: #999;
          display: block;
        }
        .co-smText:nth-child(2),
        .co-smText:nth-child(3) {
          margin-top: 6rpx;
        }
        .co-lineSm-Lf {
          display: flex;
          image {
            width: 111rpx;
            height: 111rpx;
            margin-right: 25rpx;
          }
        }
        .co-lineSm-Rg {
          .co-smTextPrice {
            font-size: 30rpx;
            font-weight: 400;
            text {
              font-size: 20rpx;
            }
          }
        }
      }
    }
  }
  .co-inform {
    background: #fff;
    &.margin-top {
      margin-top: 20rpx;
    }
    .co-inform-line {
      padding: 30rpx;
      display: flex;
      align-items: center;
      justify-content: space-between;
      .co-inform-RG {
        display: flex;
        align-items: center;
        view {
          text-align: right;
          margin-right: 20rpx;
          .co-orangColor {
            color: #ed5307;
          }
          text {
            display: block;
          }
          text:nth-child(2) {
            font-size: 22rpx;
            color: #999;
            margin-top: 8rpx;
          }
        }
      }
    }
    .co-inform-line2 {
      padding: 30rpx;
      textarea {
        width: 630rpx;
        font-size: 26rpx;
        height: 120rpx;
        background: #f2f3f8;
        border-radius: 10rpx;
        padding: 30rpx;
        margin-top: 20rpx;
      }
    }
    .co-inform-line3 {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 30rpx;
      view:nth-child(2) {
        color: #666;
      }
    }
  }
  .co-btm {
    position: fixed;
    width: 100%;
    bottom: 0;
    left: 0;
    background: #fff;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 20rpx 30rpx;
    box-sizing: border-box;
    z-index: 99;
    .co-btm-Lf {
      text:nth-child(2) {
        font-size: 22rpx;
      }
      text:nth-child(3) {
        font-size: 30rpx;
        color: #ed5307;
        margin-left: 15rpx;
      }
    }
    .co-btm-rg {
      width: 220rpx;
      height: 80rpx;
      line-height: 80rpx;
      background: $bg-gradient-btn;
      border-radius: 40rpx;
      color: #fff;
      text-align: center;
    }
    .co-btm-btnNo {
      color: #999;
      background: #ebebeb;
    }
  }

  // 配送弹框
  .co-ps-list {
    padding: 0 30rpx;
    height: 80vh;
    .co-ps-listLine-Y {
      height: 60vh;
    }
    .co-ps-listLine {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 30rpx 0;
      border-bottom: 1rpx solid $opacity-border;
      .co-ps-list-Lf {
        display: flex;
        align-items: center;
        justify-content: center;
      }
      .co-ps-listPrice {
        color: #ed5307;
        font-size: 30rpx;
        text:nth-child(1) {
          font-size: 22rpx;
        }
      }
    }

    .co-ps-listInform {
      font-size: 28rpx;
      margin-left: 30rpx;
      line-height: 40rpx;
      view:nth-child(2),
      view:nth-child(3) {
        font-size: 24rpx;
        color: #999;
      }
    }

    .co-ps-listLine2 {
      display: flex;
      align-items: center;
      padding: 30rpx 0;
      border-bottom: 1rpx solid $opacity-border;
    }
  }
}
.chaidan-popup {
  height: 530rpx !important;
}
.invoice-popup {
  .invoice-popup-list {
    height: 60vh;
    overflow-y: auto;
    padding-bottom: 50rpx;
  }
  .invoice-popup-tip {
    font-size: 24rpx;
    color: #999;
    text-align: center;
    width: 600rpx;
    line-height: 40rpx;
    margin: 0 auto;
  }
  .invoice-popupTitle {
    font-size: 28rpx;
    padding: 0 30rpx;
  }
  .invoice-popup-line {
    display: flex;
    align-items: center;
    padding: 20rpx 30rpx;
    text {
      display: block;
      width: 184rpx;
      height: 56rpx;
      line-height: 56rpx;
      background: #f5f5f5;
      border-radius: 24rpx;
      text-align: center;
      color: #999;
      margin-left: 30rpx;
    }
    text:nth-child(1) {
      margin-left: 0rpx;
    }
    .invoiceHover {
      color: $base-color1;
      background: rgba($color: $base-color1, $alpha: 0.1);
    }
  }
  .invoice-popup-line2 {
    display: flex;
    align-items: center;
    padding: 0 30rpx;
    text {
      width: 170rpx;
    }
    input {
      width: 450rpx;
      font-size: 26rpx;
      padding: 20rpx 30rpx;
    }
  }
}

.voucher-popup {
  .myCollect-popup-content {
    max-height: 50vh;
  }
}
</style>
