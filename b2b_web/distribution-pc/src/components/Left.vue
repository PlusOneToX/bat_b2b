<template>
  <div class="common-left rl-fl">
    <div class="contents">
      <!--暂时注释没有接口 <dl :class="{'en': ($i18n.locale === 'en')}">
        <dt>{{$t('UserCenter.PurchaseCenter')}}</dt>
        <dd>
          <router-link to="/Quotation">{{$t('UserCenter.Quotation')}}</router-link>
        </dd>
      </dl> -->
      <dl :class="{ en: $i18n.locale === 'en' }">
        <dt>{{ $t("UserCenter.OrderCenter") }}</dt>
        <dd>
          <router-link to="/OrderManage">{{ $t("P.Order") }}</router-link>
        </dd>
        <dd>
          <router-link to="/BatchImport">{{
            $t("UserCenter.BatchImport")
          }}</router-link>
        </dd>
        <!-- <dd><router-link to="/AfterApplication">售后</router-link></dd> -->
        <!-- <dd>
          <router-link to="/WantBook">{{$t('UserCenter.Shortage')}}</router-link>
        </dd>-->
      </dl>
      <dl :class="{ en: $i18n.locale === 'en' }">
        <dt>{{ $t("UserCenter.FollowCenter") }}</dt>
        <dd>
          <router-link to="/MyCollect">{{
            $t("UserCenter.Favorites")
          }}</router-link>
        </dd>
        <!-- <dd v-show="$i18n.locale === 'zh'">
          <router-link to="/MyCustom">{{$t('UserCenter.DIY')}}</router-link>
        </dd> -->
      </dl>
      <dl
        :class="{ en: $i18n.locale === 'en' }"
      >
        <dt>{{ $t("UserCenter.Asset") }}</dt>
        <dd v-show="$i18n.locale === 'zh' && userTradeType === 1">
          <router-link to="/MoneyDetails">{{
            $t("UserCenter.AssetDetails")
          }}</router-link>
        </dd>
        <dd>
          <router-link to="/Voucher">{{
            $t("UserCenter.VoucherDetails")
          }}</router-link>
        </dd>
        <dd v-show="$i18n.locale === 'zh' && userTradeType === 1">
          <router-link to="/Recharge">{{
            $t("UserCenter.Recharge")
          }}</router-link>
        </dd>
        <dd v-if="subAccountFlag==1">
          <router-link to="/SubAccount">{{$t('UserCenter.subAccountRecord')}}</router-link>
        </dd>
        <!-- <dd><router-link to="/Withdrawal">{{$t('UserCenter.Cash')}}</router-link></dd> -->
        <!-- <dd><router-link to="/Statements">对账单</router-link></dd>
        <dd><router-link to="/Invoice">发票管理</router-link></dd>-->
      </dl>
      <dl
        :class="{ en: $i18n.locale === 'en' }"
        v-show="$i18n.locale === 'zh' && rxShopSwitch == 1"
      >
        <dt>{{ $t("UserCenter.MyStore") }}</dt>
        <dd>
          <router-link to="/StoreManage">{{
            $t("UserCenter.StoreManage")
          }}</router-link>
        </dd>
        <dd>
          <router-link to="/StoreOrder">{{
            $t("UserCenter.StoreOrder")
          }}</router-link>
        </dd>
        <dd v-if="subAccountFlag==1">
          <router-link to="/SubAccountConfig">{{$t('UserCenter.SubAccountConfig')}}</router-link>
        </dd>
        <dd v-if="subAccountFlag==1">
          <router-link to="/SubAccountSalesman">{{$t('UserCenter.SubSalesman')}}</router-link>
        </dd>
      </dl>
      <!-- <dl>
        <dt>活动中心</dt>
        <dd><router-link to="/Coupons">优惠券</router-link></dd>
      </dl>-->
      <dl :class="{'en': ($i18n.locale === 'en')}" v-show="$i18n.locale === 'zh'&&distributionFlag==1 ">
        <dt>分销管理</dt>
        <dd>
          <router-link to="/distributorsList">分销商</router-link>
        </dd>
        <dd>
          <router-link to="/distributorsOrder">分销商订单</router-link>
        </dd>
        <dd>
          <router-link to="/priceLevel">价格等级</router-link>
        </dd>
      </dl> 
      <dl :class="{'en': ($i18n.locale === 'en')}">
        <dt>{{$t('UserCenter.Set')}}</dt>
        <dd>
          <router-link to="/UserInfos">{{
            $t("UserCenter.Information")
          }}</router-link>
        </dd>
        <dd>
          <router-link to="/AccountManage">{{
            $t("UserCenter.AccountManagement")
          }}</router-link>
        </dd>
        <dd>
          <router-link to="/Address">{{
            $t("UserCenter.Address")
          }}</router-link>
        </dd>
        <dd>
          <router-link to="/ModifyPassword">{{
            $t("UserCenter.Password")
          }}</router-link>
        </dd>
      </dl>
    </div>
  </div>
</template>

<script>
import GD from "@/assets/js/globalData";
import { businessGetBusiness } from "@/apiService/api";
export default {
  name: "Left",
  data() {
    return {
      useLang: false, // 是否使用多语种
      userTradeType: 1, // 1为现款支付，2为期间结算
      langList: GD.langList, // 语种列表
      lang: 'zh-RMB', // 语种
      rxShopSwitch: null,
      distributionFlag:0,
      subAccountFlag: 0 // 是否开启分账功能
    };
  },
  mounted(){
      this.distributionFlag=localStorage.getItem('distributionFlag');
      this.subAccountFlag = localStorage.getItem('subAccountFlag');
  },
  methods: {
    fLangChange(value) {
      window.localStorage.setItem("bLang", value);
      this.$i18n.locale = value.split("-")[0];
      this.$i18n.curren = value.slice(3, 6);
    },
    // getDepositSet () {
    //   this.$api.get(this, 'user/u/deposit/depositSet').then((res) => {
    //     if (res.code === 0) {
    //     }
    //   });
    // },
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
          }
        });
    },
    // 判断是否开启店铺管理--Y
    getStore() {
      let usderId = localStorage.getItem("userId");
      businessGetBusiness({ id: usderId }).then((res) => {
        // 1-启用  0-不启用
        this.rxShopSwitch = res.data.rxShopSwitch;
      });
    },
  },
  created() {
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem("bLang")
      ? window.localStorage.getItem("bLang")
      : "zh-RMB";
    // this.getDepositSet();
    // this.getUserTradeType();
    // 判断是否开启店铺管理
    this.getStore();
  },
};
</script>

<style scoped="scoped" lang='less'>
@import url("../assets/less/variable.less");
.common-left {
  margin-top: 20px;
  width: 150px;
  height: 100%;
  border: 2px solid @bdLightColor;
  border-radius: 8px;
  .contents {
    padding-top: 10px;
    padding-bottom: 10px;
    dl {
      padding-top: 10px;
      padding-left: 36px;
      margin-bottom: 10px;
      & + dl {
        border-top: 1px solid @bdLightColor;
      }
      &.en {
        padding-left: 0;
        dt {
          text-align: center;
        }
        dd {
          text-align: center;
          a {
            font-size: 13px;
          }
        }
      }
      dt {
        font-size: 14px;
        color: @lightBlack;
        font-weight: bold;
        line-height: 28px;
      }
      dd {
        line-height: 28px;
        a {
          display: block;
          font-size: 14px;
          color: @lightGray;
          &.router-link-active {
            color: @blue;
          }
          &:hover {
            color: @blue;
          }
        }
      }
    }
  }
}
</style>
