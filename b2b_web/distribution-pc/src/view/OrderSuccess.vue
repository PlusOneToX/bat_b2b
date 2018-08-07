<template>
  <div>
    <div class="index rl-margin-zero">
      <!--公共头部-->
      <Header></Header>
      <!--主内容-->
      <div class="main rl-margin-zero rl-padding-top-default">
        <div v-if="showLoading === false" class="rl-margin-bottom-xxxs">{{$t('OrderSuccess.Title')}}！</div>
        <div class="">
          <div v-if="Number(orderType) == 1" class="rl-clear rl-padding-left-double rl-bg-green_xs rl-padding-top-default rl-padding-bottom-default rl-padding-right-xxxs rl-bdb-gray-sm">
            <div class="rl-fl rl-text-xxss">{{commomTime}} </div>
            <div class="rl-fr rl-text-xxss" v-if="orderGoodsType === 2">
              <p v-if="$i18n.locale === 'zh'">您订单中的商品由于在库数量不足，故拆分成以下订单分开配送，给您带来不便敬请谅解。</p>
              <p v-else class="rl-text-xxs">The goods in your order are not enough in the warehouse, so they are divided into the following orders for separate distribution. Sorry for the inconvenience caused to you</p>
            </div>
          </div>
          <div v-if="Number(orderType) === 1" class="rl-clear rl-padding-left-double rl-bg-green_xs rl-padding-top-default rl-padding-bottom-default rl-padding-right-default">
            <!-- 收货人 -->
            <div class="rl-fl rl-text-xxss">{{$t('OrderSuccess.SendMan')}}: <span>{{deliveryName}}</span></div>
            <!-- 订单金额 -->
            <div class="rl-fl rl-margin-left130 rl-text-xxss">{{$t('OrderSuccess.Amount')}}：<span><i v-if=" $i18n.locale == 'zh'">￥</i><i v-else>$</i>{{($i18n.locale === 'zh'?orderAllCost:(Number(orderAllCost)*exchange).toFixed(2)) | keepTwoNum}}</span></div>
            <!-- 支付方式 -->
            <div class="rl-fl rl-margin-left130 rl-text-xxss">{{$t('Foot.PaymentMethod')}}：<span>{{orderPayStyle(allOrderPayWay)}}</span></div>
            <!-- 订单拆分 -->
            <div class="rl-fr rl-text-xxss"><span v-if="$i18n.locale === 'zh'">订单已拆分</span><span v-else>Order split</span></div>
          </div>
          <div v-if="orderList.length > 0">
            <div class="order-list" v-for="order in orderList" :key="order.id">
              <div :class ="{'rl-bg-green_xs': Number(orderType) === 0 }" class="rl-clear items rl-padding-left-double rl-padding-top-mid rl-padding-bottom-mid rl-bg-white rl-padding-right-default rl-bdb-gray-sm">
                <div class="rl-fl order-div" v-if="Number(orderType) === 0">{{order.orderInfo.createTime}}</div>
                <div class="rl-fl rl-text-xxss order-div" v-if="Number(order.type) === 1">{{$t('OrderSuccess.OrderInstock')}}</div>
                <div class="rl-fl rl-text-xxss order-div" v-if="Number(order.type) === 2">{{$t('OrderSuccess.OrderIntransit')}}</div>
                <div class="rl-fl rl-text-xxss order-div" v-if="Number(order.type) === 3">{{$t('OrderSuccess.OrderCustomized')}}</div>
                <!-- 订单编号 -->
                <div class="rl-fl rl-text-xxss" v-if="Number(orderType) === 0">{{$t('UserCenter.OrderNo')}}：{{order.orderInfo.orderNo}}</div>
                <div class="rl-fl rl-text-xxss rl-margin-left95" v-if="Number(orderType) === 1">{{$t('UserCenter.OrderNo')}}：{{order.orderInfo.orderNo}}</div>
                <!-- 支付方式 -->
                <div class="rl-fl rl-text-xxss rl-margin-left-double" v-if="Number(orderType) === 0">{{$t('Foot.PaymentMethod')}}：<span>{{orderPayStyle(order.orderDistributor.orderDistributorDataDTO.payWay)}}</span></div>
               
                <!-- 订单状态 -->
                <div class="rl-fr rl-text-xxss order-state rl-margin-left-xxxs">{{$t('UserCenter.PaymentStatus')}}：<span>{{getPayStatus(order.orderDistributor.orderDistributorDataDTO.payStatus)}}</span></div>
                <!-- 待付款 -->
                <div class="rl-fr rl-text-xxss order-state">{{$t('UserCenter.OrderStatus')}}：<span>{{getOrderStatus(order.orderDistributor.orderDistributorDataDTO.orderStatus)}}</span></div>
              </div>
              <div class="infos rl-bg-white">
                <!-- 货品列表 -->
                <div class="table">
                  <table>
                    <tbody>
                    <tr v-if="index<2"  v-for="(item,index) in order.orderInfoDetailGoods" :key="item.id">
                      <td class="rl-tl">
                        <span v-if="$i18n.locale === 'en' && item.orderGoods.itemNameEn !== undefined && item.orderGoods.itemNameEn !== null && item.orderGoods.itemNameEn !== ''">{{item.orderGoods.itemNameEn}}</span>
                        <span v-else>{{item.orderGoods.itemName}}</span>
                      </td>
                      <td class="rl-tc">{{item.orderGoods.itemCode}}</td>
                      <td class="rl-tc">
                        {{item.orderGoods.specsName}}
                      </td>
                      <td class="rl-tc">x{{item.orderGoods.itemCount}}</td>
                    </tr>
                    <tr>  
                      <td colspan="10" @click="goOrderDetail(order.orderInfo.id)">
                         <div class="rl-tl rl-text-blue-mm cursor-pointer">{{$t('OrderSuccess.More')}}>></div>
                      </td>
                    </tr>
                    </tbody>
                  </table>
                </div>
                <!--订单金额 -->
                <div class="datas">
                  <div class="">
                    <div class="word rl-tc">{{$t('OrderSuccess.Amount')}}：<span class="rl-text-orange-sm" >
                      {{$i18n.locale === 'zh'?'￥':'$'}}{{$i18n.locale === 'zh'?order.orderDistributor.orderDistributorCostDTO.payAmount:(Number(order.orderDistributor.orderDistributorCostDTO.payAmount)*exchange).toFixed(2)}}</span>
                    </div>
                  </div>
                </div>
                <div class="operate">
                  <div class="item"> 
                    <!-- 查看详情 -->
                    <div @click="goOrderDetail(order.orderInfo.id)" class="btns rl-text-xxs rl-bd-orange-sm rl-text-orange-sm cursor-pointer">{{$t('UserCenter.ViewDetails')}}</div>
                    <!-- 付款 -->
                    <div @click="goPay(order.orderInfo.id,order.orderDistributor.orderDistributorDataDTO.payWay,1)" 
                    class="btns rl-text-xxs rl-bg-orange-sm rl-text-white rl-margin-top-xxxs cursor-pointer" 
                    :class="{disabled: !clickFlag}"
                    v-if="clickAgain === true && (order.orderDistributor.orderDistributorDataDTO.payStatus === 1 || order.orderDistributor.orderDistributorDataDTO.payStatus === 2) 
                          && order.orderDistributor.orderDistributorDataDTO.payWay !==3&& order.orderDistributor.orderDistributorDataDTO.payWay !==5 && order.orderDistributor.orderDistributorDataDTO.payWay !==4 
                           && order.orderDistributor.orderDistributorDataDTO.orderStatus !== 5">{{$t('P.Payment')}}</div>
                    <!-- 已付款--付款 -->
                    <div class="btns rl-text-xxs rl-bg-orange-sm rl-text-white rl-margin-top-xxxs cursor-pointer" 
                    v-if="clickAgain === false && (order.orderDistributor.orderDistributorDataDTO.payStatus === 1 || order.orderDistributor.orderDistributorDataDTO.payStatus === 2) 
                          && order.orderDistributor.orderDistributorDataDTO.payWay !==3&& order.orderDistributor.orderDistributorDataDTO.payWay !==5 && order.orderDistributor.orderDistributorDataDTO.payWay !==4 
                           && order.orderDistributor.orderDistributorDataDTO.orderStatus !== 5">{{$t('P.Payment')}}</div>
                  
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="order-foot rl-bg-white rl-clear rl-padding-top-mid rl-padding-bottom-mid rl-padding-left-double rl-padding-right-lllg" v-if="!allCostState" >
            <div v-if="this.showLoading === false" class="rl-fl go-index cursor-pointer"><router-link to="/Index">{{$t('P.BackHome')}}</router-link></div>
            <div @click="goPay(orderList[0].orderInfo.id,orderList[0].orderDistributor.orderDistributorDataDTO.payWay,2)" class="all-pay rl-bg-orange-sm rl-text-white rl-text-xxs cursor-pointer rl-fr" :class="{disabled: !clickFlag}" v-if="clickAllAgain === true && this.allCostState === false && this.allOrderPayWay !==3 && this.allOrderPayWay !==4 && this.allOrderPayWay !== 5 && Number(this.orderType) === 1">{{$t('OrderSuccess.MergePayments')}}</div>
            <div class="all-pay rl-bg-orange-sm rl-text-white rl-text-xxs rl-fr" v-if="clickAllAgain === false && this.allCostState === false && this.allOrderPayWay !==3 && this.allOrderPayWay !==4 && this.allOrderPayWay !== 5 && Number(this.orderType) === 1">{{$t('OrderSuccess.MergePayments')}}</div>
          </div>
        </div>
      </div>
    </div>
    <!--支付二维码-->
    <div class="cover" v-if="showCode"></div>
    <div v-show="showCode" class="ewm">
      <div class="rl-margin-bottom-default rl-padding-top-xxxs rl-padding-bottom-xxxs rl-padding-left-xxxs rl-tl">
        <span @click="shutLog" class="shut cursor-pointer"></span>
      </div>
      <div class="rl-padding-bottom-lllg time-state" v-show="qrCodeState === true">
        <p class="rl-tc" v-if="$i18n.locale === 'zh'">距离二维码过期还剩<span class="rl-text-blue-xs rl-margin-right-xxxxs rl-margin-left-xxxxs">{{qrCodeTime}}</span>秒，</p>
        <p class="rl-tc" v-else>The qr code expired <span class="rl-text-blue-xs rl-margin-right-xxxxs rl-margin-left-xxxxs">{{qrCodeTime}}</span> s，</p>
        <p class="rl-tc" v-if="$i18n.locale === 'zh'">过期后请刷新页面重新获取二维码。</p>
        <p class="rl-tc rl-text-xxs" v-else>After expiration, please refresh the page to get the qr code again。</p>
      </div>
      <div class="rl-padding-bottom-lllg time-state" v-show="qrCodeState === false">
        <span v-if="$i18n.locale === 'zh'">二维码已过期，请</span><span v-else class="rl-text-xxs">Qr code expired,please</span>
        <span v-if="$i18n.locale === 'zh'" class="rl-text-orange-sm rl-margin-right-xxxxs rl-margin-left-xxxxs">刷新</span><span v-else class="rl-text-orange-sm rl-margin-right-xxxxs rl-margin-left-xxxxs rl-text-xxs">refresh</span>
        <span v-if="$i18n.locale === 'zh'">二维码重新获取</span><span v-else class="rl-text-xxs">Retrieve qr code</span>
      </div>
      <div class="qrcode">
        <div id="qrcode">
        </div>
        <div class="state" v-show="qrCodeState === false"></div>
        <div class="state-other" v-show="qrCodeState === true"></div>
        <div class="fair-img cursor-pointer" @click="reFresh" v-show="qrCodeState === false"><img src="../assets/images/refresh.png" alt=""></div>
      </div>
      <div class="zhifu rl-clear">
        <div class="icon rl-margin-right-mid rl-fl" v-if="allOrderPayWay === 1"><img src="../assets/images/zhifubao.png" alt=""></div>
        <div class="rl-fl"  v-if="allOrderPayWay === 1">
          <div v-if="$i18n.locale === 'zh'">
            <p class="rl-tl spc-p">请使用<span class="rl-text-orange-sm">支付宝</span>扫一扫</p>
            <p class="rl-tl">扫描二维码支付</p>
          </div>
          <div v-else>
            <p class="rl-tl spc-p rl-text-xxs">Please use the <span class="rl-text-orange-sm">Alipay</span> scan</p>
            <p class="rl-tl rl-text-xxs">Pay by scanning qr code</p>
          </div>
        </div>
        <div class="icon rl-margin-right-mid rl-fl" v-if="allOrderPayWay === 2"><img src="../assets/images/wechat.png" alt=""></div>
        <div class="rl-fl"  v-if="allOrderPayWay === 2">
          <div v-if="$i18n.locale === 'zh'">
            <p class="rl-tl spc-p">请使用<span class="rl-text-orange-sm">微信</span>扫一扫</p>
            <p class="rl-tl">扫描二维码支付</p>
          </div>
          <div v-else>
            <p class="rl-tl spc-p rl-text-xxs">Please use the <span class="rl-text-orange-sm">WeChat</span> scan</p>
            <p class="rl-tl rl-text-xxs">Pay by scanning qr code</p>
          </div>
        </div>
      </div>
    </div>
    <!--支付成功-->
    <div class="cover" v-if="showPay"></div>
    <div v-show="showPay" class="pay-result">
      <div class="rl-margin-bottom-default rl-bdb-gray-sm rl-padding-top-xxxs rl-padding-bottom-xxxs rl-bg-gray-mm rl-tc">{{$t('OrderSuccess.PayResults')}}</div>
      <span @click="shutPay" class="shut cursor-pointer"></span>
      <div class="rl-margin-top-double rl-margin-bottom-default"><img src="../assets/images/pay-success.png" alt=""></div>
      <p class="rl-tc rl-text-lg rl-text-gray">{{$t('OrderSuccess.PaySuccess')}}</p>
      <div class="result">
        <span @click="shutPay" class="rl-tc rl-bg-blue-mm rl-text-white rl-text-mid cursor-pointer">{{$t('P.OK')}}</span>
      </div>
    </div>
    <!--加载动画-->
    <loading v-if="this.showLoading === true"></loading>
    <!-- 银行信息提示dialog -->
    <el-dialog width="36%" :modal-append-to-body="false" :visible="bankShow" :before-close="closeBankShow">
      <div style="text-align: center;">
        <span class="bankHint1"><i class="el-icon-check bankHint"></i> {{$t('OrderSuccess.OrderPlaced')}}</span>
      </div>
      <div class="bankInfo">
        <div style="margin-bottom:5px">{{$t('OrderSuccess.BeneficiaryCompanyName')}}</div>
        <div style="margin-bottom:5px">{{$t('OrderSuccess.BankName')}}</div>
        <div style="margin-bottom:5px">{{$t('OrderSuccess.Beneficiary')}}</div>
        <div v-if="$i18n.locale === 'en'">{{$t('OrderSuccess.BankAddress')}}</div>
      </div>
      <div style="text-align: center; margin-bottom: 20px;margin-left: 55px;margin-right: 50px;">
        <span class="bankHint2">{{$t('OrderSuccess.ContactCustomer')}}</span>
      </div>
      <div style="text-align: center;">
        <el-button class="mini-search-btn" @click="closeBankShow" >{{$t('Message.Confirm')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Header from '@/components/Header.vue'
import loading from '@/components/loading.vue'
import QRCode from 'qrcodejs2'
import GD from '@/assets/js/globalData'
import countDown from '@/components/countDown.vue'
// liu--
import { orderResults,payCreateTrade,payQueryTrade} from '@/apiService/api'
export default {
  name: 'OrderSuccess',
  components: {
    Header,
    loading,
    countDown
  },
  
  data () {
    return {
      num: 0,
      orderId: '', // 单个订单Id
      payOrderId: '', // 支付二维码返回id
      showCode: false,
      showPay: false,
      Url: '', // 二维码
      qrCodeState: true, // 二维码状态
      qrCodeTime: 0, // 二维码失效时间
      isDeposit: 0, // 预存款抵扣
      orderIds: [],
      deliveryName: '', // 收货人
      commomTime: '', // 下单时间
      orderAllCost: 0, // 订单总金额
      orderList: [],
      orderGoodsType: 0, // 单个订单的类型：1-在库 2-在途  3-制定'
      orderType: 0, // 0 单个订单 1 多个订单
      allCostState: true, // 全部付款状态
      payStateType: 0, // 支付类型 1单个订单支付 2 全部支付
      showLoading: false, // 加载动画
      clickAgain: true, // 用于防止二次点击
      clickAllAgain: true, // 用于防止二次点击
      allOrderPayWay: 0, // 合并订单的支付方式 1支付宝 2微信 3区间 4银行转账 5余额支付
      useLang: false, // 是否启用多语种
      langList: GD.langList, // 语种列表
      lang: 'zh-CNY', // 语种
      bankShow: false, // 银行信息对话框是否显示
      exchange:1,  //汇率
      clickFlag: true, // 防重复点击
    }
  },
  mounted () {
    
   this.exchange=Number( localStorage.getItem('exchange'));
    this.getResultsFun();
  },
  filters: {
    keepTwoNum (value) {
      value = Number(value)
      return value.toFixed(2)
    }
  },
  methods: {
    // 获取订单结果信息-Y
    getResultsFun(){      
        let orderId=this.$route.query.orderId;
        let oeserIdArr=orderId.split(",");
        this.orderType = (oeserIdArr.length>1)?1:0;
       orderResults({ids:orderId}).then(res=>{
         console.log('订单结果：',res.data);
         if(res.success){
            this.orderList=res.data.item;
            this.commomTime =res.data.item[0].orderDistributor.orderDistributorCostDTO.createTime;
            this.deliveryName=res.data.userName;
            this.allOrderPayWay=res.data.item[0].orderDistributor.orderDistributorDataDTO.payWay;
            this.orderAllCost=res.data.amount;
            res.data.item.forEach(items => {
              if(items.type==2){
                 this.orderGoodsType=2
              }
         
              if(items.orderDistributor.orderDistributorDataDTO.payStatus==1||items.orderDistributor.orderDistributorDataDTO.payStatus==2){
                  this.allCostState=false;
                  
              }
            });    
            
         }
       })
    },
    closeBankShow () {
      this.bankShow = false
    },
    fLangChange (value) {
      window.localStorage.setItem('bLang', value);
      this.$i18n.locale = value.split('-')[0];
      this.btnSendCodeTitle = this.$t('Register.GetVerificationCode')
    },
    getOrderStatus (row) { // 订单状态
      switch (row) {
        case 1:
          return this.$t('UserCenter.ToBeConfirmed')
        case 2:
          return this.$t('UserCenter.ToBeShipped')
        case 3:
          return this.$t('UserCenter.PartOfShipped')
        case 4:
          return this.$t('UserCenter.Shipped')
        case 5:
          return this.$t('UserCenter.Closed')
        case 6:
          return this.$t('UserCenter.Completed')
      }
    },
    getPayStatus (row) { // 订单付款状态
      switch (row) {
        case 1:
          return this.$t('UserCenter.PendingPayment')
        case 2:
          return this.$t('UserCenter.PartOfPayment')
        case 3:
          return this.$t('UserCenter.Paid')
        case 4:
          return this.$t('UserCenter.Refunded')
        case 5:
          return this.$t('UserCenter.PartOfRefunded')
        case 6:
          return this.$t('UserCenter.Refunding')
      }
    },
    orderPayStyle (row) { // 订单付款方式-Y
      switch (row) {
        // 支付宝
        case 1:
          return this.$t('P.Alipay')
          // 微信
        case 2:
          return this.$t('P.WechatPay')
          // 区间结算(月结)
        case 3:
          return this.$t('P.RangeCheckout')
          // 银行转账(现款)
        case 4:
          return this.$t('P.BankTransfer')
        case 5:
          return '余额支付'
        case 6:
          return '网银支付'
      }
    },
    
    shutLog () { // 关闭支付弹框--y
      // this.qrCodeTime = 300
      this.showCode = false
      this.qrCodeState = true
      this.paySuccessState(1)

      this.clickFlag = true;
    },
    shutPay () {
      this.showPay = false
      this.getResultsFun();
    },

    // 前往单个订单详情-Y
    goOrderDetail (id) {
      this.$router.push({name: 'WaitPayment', query: {order_id: id}})
    },
    // 支付-y
    goPay (id, payWay,type) {
      if (this.clickFlag) {
        this.clickFlag = false;
        clearInterval(this.intervalid)
        clearInterval(this.intervalids)
        let ids= (type==1?id:this.$route.query.orderId)
        let userId=localStorage.getItem('userId');
        let userName=localStorage.getItem('userName');
        let payMethod='';
        if(payWay==1){
          payMethod='alipay_face_to_face'
        }else if(payWay==2){
          payMethod='wxpay_native'
        }else if(payWay==5){
          payMethod="balance";
        }else{
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
            if (payWay === 6) { // 网银支付--即是快钱支付
              if(type==1){
                this.clickAgain = true;
              }else{
                this.clickAllAgain=true;
              }
              
              window.location.href = payRes.data.kuaiQianReap.h5Url; // 跳转链接
            }else{
              
              if(type==1){
                this.clickAgain = true;
              }else{
                this.clickAllAgain=true;
              }
              
              let urlStr=payWay==1?payRes.data.alipayResp.codeUrl:payRes.data.wxResp.codeUrl;
              if(payWay==1||payWay==2){
                this.qrCodeTime = 60;
                this.intervalids = setInterval(() => {
                  this.qrCodeTime--
                  if (this.qrCodeTime === 0) {
                    this.qrCodeState = false;  //二维码状态
                    clearInterval(this.intervalids)
                  }
                }, 1000)
                this.makeCodes(urlStr,id,payMethod);
              }else{
                
                this.$message('支付成功！');
              } 
            }
          }else{
            this.$message(payRes.errMessage);
            if(type==1){
                this.clickAgain = false;
              }else{
                this.clickAllAgain=false;
              }
            this.clickFlag = true;
          }
        })
      }
    },
    // 生成二维码-Y
    makeCodes (url,id,payMethod) {
      console.log(url);
      this.showCode = true
      document.getElementById('qrcode').innerHTML = ''
      var qrcode = new QRCode(document.getElementById('qrcode'), {
        width: 250,
        height: 250
      })
      qrcode.makeCode(url)
      this.paySuccessState(2,id,payMethod)
    },
   
    // 重新获取二维码
    reFresh () {
      // this.qrCodeTime = 300
      this.qrCodeState = true
      this.goPay();
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
  },
  created () {
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem('bLang') ? window.localStorage.getItem('bLang') : 'zh-RMB';
  }
}
</script>

<style scoped="scoped" lang='less'>
  .rl-margin-left130{margin-left: 130px}
  .rl-margin-left95{margin-left: 95px}
  .index{
    width: 100%;
  }
  .main {
    width: 1190px;
    .item{
      div{
        margin-right: 50px;
      }
    }
    .infos{
      display: flex;
      height: 100%;
      overflow: hidden;
      .table{
        width: 846px;
        border-right: 1px solid #c8c7cd;
        table{
          tr{
            padding-left: 40px;
            border-bottom: 1px solid #c8c7cd;
            td{
              width: 211.5px;
              height: 70px;
              font-weight: 400;
              color:rgba(0,0,0,1);
            }
            td:first-child{
              padding-left: 40px;
            }
          }
        }
      }
      .datas{
        border-right: 1px solid #c8c7cd;
        border-bottom: 1px solid #c8c7cd;
        display: flex;
        align-items:center;
        .word{
          width: 206px;
        }
      }
      .operate{
        display: flex;
        align-items:center;
        width: 140px;
        border-bottom: 1px solid #c8c7cd;
        .item{
          width: 75px;
          margin: auto;
          .btns{
            width: 75px;
            height: 20px;
            line-height: 20px;
            text-align: center;
            border-radius: 5px;

            &.disabled {
              background-color: #999;
            }
          }
        }
      }
    }
    .order-list{
      .items{
        .order-div{
          min-width: 80px;
          margin-right: 40px;
        }
        .order-state{
          min-width: 160px;
        }
      }
    }
    .order-foot{
      .go-index{
        height: 35px;
        line-height: 35px;
        a{
          color:rgba(3,146,166,1);
        }
      }
      .all-pay{
        width: 90px;
        height: 35px;
        line-height: 35px;
        text-align: center;
        border-radius: 5px;

        &.disabled {
          background-color: #999;
        }
      }
    }
  }
  /*弹框*/
  .cover{
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: #000;
    z-index: 99;
    opacity: 0.5;
  }
  .ewm{
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
    .shut{
      position: absolute;
      top:10px;
      right:10px;
      display: block;
      width: 18px;
      height: 18px;
      background: url("../../src/assets/images/wrong-de.png") no-repeat center center;
      background-size: 100%;
    }
    .time-state{
      width: 280px;
      height: 38px;
      margin:  0 auto;
      font-size: 15px;
    }
  }
  .pay-result{
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
    .shut{
      position: absolute;
      top:-8px;
      right:-8px;
      display: block;
      width: 18px;
      height: 18px;
      background: url("../../src/assets/images/shut.png") no-repeat center center;
    }
    .result{
      margin-top: 60px;
      span{
        display: inline-block;
        width: 120px;
        height: 35px;
        line-height: 35px;
        border-radius: 5px;
      }
    }
  }
  #qrcode{
    position: relative;
    margin: 0 auto;
    width: 250px;
    height:250px;
  }
  .qrcode{
    position: relative;
    margin: 0 auto;
    width:280px;
    height:280px;
    .state{
      position: absolute;
      top: -15px;
      left: 0;
      background: #b2b2b2;
      z-index: 99;
      opacity: 0.95;
      width:280px;
      height:280px;
    }
    .state-other{
      position: absolute;
      top: -15px;
      left: 0;
      width:275px;
      height:275px;
      border: 3px solid #e0e0e0
    }
    .fair-img{
      position: absolute;
      top: 35%;
      left: 40%;
      z-index: 999;
      width:49px;
    }
  }
  .zhifu{
    padding-top: 10px;
    width: 280px;
    margin: 0 auto;
    .icon{
      margin-left: 55px;
      width: 40px;
      img{width: 100%}
    }
    .spc-p{padding-top: 2px}
  }
  .bankHint1{
    font-family: "Helvetica Neue";
    color: rgb(33, 184, 203);
    font-weight: bold;
  }
  .bankHint2{
    font-size: 10px;
  }
  .bankInfo{
    background-color: #eef8fa;
    padding-top: 25px;
    padding-left: 20px;
    padding-right: 20px;
    padding-bottom: 25px;
    margin-top: 10px;
    margin-left: 20px;
    margin-right: 20px;
    margin-bottom: 10px;
    border-radius: 4px;
    word-break: break-all;
  }
</style>
