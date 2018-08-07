<template>
  <div>
    <div class="user rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState"></Header>
      <!--主内容-->
      <div class="user-main rl-clear rl-margin-zero">
        <!--公共左边-->
        <Left></Left>
        <div class="user-right rl-fr rl-padding-horizontal-lllg rl-bg-white rl-padding-top-default rl-padding-bottom-double">
          <div class="content">
            <div class="title rl-bg-gray-mm">
              <div class="rl-padding-left-lllg title-cons rl-text-white rl-text-mid">发票详情</div>
            </div>
            <div class="invoice-info rl-padding-top-default rl-bd-black-sm rl-padding-bottom-lllg rl-margin-top-mid">
              <div class="rl-tc">开票信息</div>
              <div class="info">
                <div class="item">
                  <span class="rl-fl rl-tr">创建时间:</span>
                  <div class="rl-fl rl-margin-left-lllg">{{new Date(parseInt(invoiceDetail.createTime)).toLocaleDateString()}}</div>
                </div>
                <div class="item">
                  <span class="rl-fl rl-tr">开票状态:</span>
                  <div class="rl-fl rl-margin-left-lllg" v-if="invoiceDetail.payState === 1">未付款</div>
                  <div class="rl-fl rl-margin-left-lllg" v-if="invoiceDetail.payState === 2">部分付款</div>
                  <div class="rl-fl rl-margin-left-lllg" v-if="invoiceDetail.payState === 3">已付款</div>
                </div>
                <div class="item">
                  <span class="rl-fl rl-tr">发票类型:</span>
                  <div class="rl-fl rl-margin-left-lllg" v-if="invoiceDetail.invoiceType === 1">普通</div>
                  <div class="rl-fl rl-margin-left-lllg" v-if="invoiceDetail.invoiceType === 2">增值税发票</div>
                </div>
                <div class="item">
                  <span class="rl-fl rl-tr">发票抬头:</span>
                  <div class="rl-fl rl-margin-left-lllg" v-if="invoiceDetail.invoiceTitle === 1">个人</div>
                  <div class="rl-fl rl-margin-left-lllg" v-if="invoiceDetail.invoiceTitle === 2">单位</div>
                </div>
                <div class="item">
                  <span class="rl-fl rl-tr">纳税人识别号:</span>
                  <div class="rl-fl rl-margin-left-lllg">{{invoiceDetail.taxpayerNumber}}</div>
                </div>
                <div class="item">
                  <span class="rl-fl rl-tr">增值税发票注册地址:</span>
                  <div class="rl-fl rl-margin-left-lllg">{{invoiceDetail.registerAddress}}</div>
                </div>
                <div class="item">
                  <span class="rl-fl rl-tr">增值税发票注册电话:</span>
                  <div class="rl-fl rl-margin-left-lllg">{{invoiceDetail.registerPhone}}</div>
                </div>
                <div class="item">
                  <span class="rl-fl rl-tr">增值税发票开户银行:</span>
                  <div class="rl-fl rl-margin-left-lllg">{{invoiceDetail.bankDepositName}}</div>
                </div>
                <div class="item">
                  <span class="rl-fl rl-tr">增值税发票银行账户:</span>
                  <div class="rl-fl rl-margin-left-lllg">{{invoiceDetail.bankAccount}}</div>
                </div>
                <div class="item">
                  <span class="rl-fl rl-tr">发票号码:</span>
                  <div class="rl-fl rl-margin-left-lllg">{{invoiceDetail.invoiceNo}}</div>
                </div>
              </div>
            </div>
            <div class="bill rl-margin-top-xxxs">
              <div class="rl-tl rl-text-bold rl-padding-top-xxxs rl-padding-bottom-xxxs ">开票账单</div>
              <table>
                <tr>
                  <th>对账单号</th>
                  <th>对账时间</th>
                  <th>支付方式</th>
                  <th>付款状态</th>
                  <th>订单价格</th>
                </tr>
                <tr class="rl-bdb-gray-sm rl-bdl-gray-sm rl-bdr-gray-sm" v-for="bill in billsList" :key="bill.id" v-if="invoiceDetail.payState === 1">
                  <td>{{bill.billOrderId}}</td>
                  <td>{{new Date(parseInt(bill.createTime)).toLocaleDateString()}}</td>
                  <td v-if="bill.payType === 1">立即支付</td>
                  <td v-if="bill.payType === 2">期间结算</td>
                  <td>未付款</td>
                  <td class="rl-text-orange-sm rl-text-xs">￥{{bill.billAmount}}</td>
                </tr>
                <tr class="rl-bdb-gray-sm rl-bdl-gray-sm rl-bdr-gray-sm" v-for="bill in billsList" :key="bill.id" v-if="invoiceDetail.payState === 2">
                  <td>{{bill.billOrderId}}</td>
                  <td>{{new Date(parseInt(bill.createTime)).toLocaleDateString()}}</td>
                  <td v-if="bill.payType === 1">立即支付</td>
                  <td v-if="bill.payType === 2">期间结算</td>
                  <td>部分付款</td>
                  <td class="rl-text-orange-sm rl-text-xs">￥{{bill.billAmount}}</td>
                </tr>
                <tr class="rl-bdb-gray-sm rl-bdl-gray-sm rl-bdr-gray-sm" v-for="bill in billsList" :key="bill.id" v-if="invoiceDetail.payState === 3">
                  <td>{{bill.billOrderId}}</td>
                  <td>{{new Date(parseInt(bill.createTime)).toLocaleDateString()}}</td>
                  <td v-if="bill.payType === 1">立即支付</td>
                  <td v-if="bill.payType === 2">期间结算</td>
                  <td>已付款</td>
                  <td class="rl-text-orange-sm rl-text-xs">￥{{bill.billAmount}}</td>
                </tr>
              </table>
            </div>
            <div class="gather-info rl-padding-top-default  rl-bd-black-sm rl-margin-top-default rl-padding-bottom-default">
              <div class="rl-tc rl-padding-bottom-default">收款信息</div>
              <div class="info-detail">
                <div class="item rl-clear">
                  <span class="rl-fl rl-tr">发票金额:</span>
                  <div class="rl-fl rl-margin-left-lllg rl-text-orange-sm">￥{{invoicePayment.invoiceAmount}}</div>
                </div>
                <div class="item rl-clear">
                  <span class="rl-fl rl-tr">已收金额:</span>
                  <div class="rl-fl rl-margin-left-lllg rl-text-orange-sm">￥{{invoicePayment.paidAmount}}</div>
                </div>
                <div class="item rl-clear">
                  <span class="rl-fl rl-tr">未收金额:</span>
                  <div class="rl-fl rl-margin-left-lllg rl-text-orange-sm">￥{{invoicePayment.unpaidAmount}}</div>
                </div>
              </div>
            </div>
            <div class="ticket-info rl-padding-top-default  rl-bd-black-sm rl-margin-top-default rl-padding-bottom-default">
              <div class="rl-tc rl-padding-bottom-default">收票信息</div>
              <div class="info-detail">
                <div class="info-left rl-fl">
                  <div class="item rl-clear">
                    <span class="rl-fl rl-tr">收件人:</span>
                    <div class="rl-fl rl-margin-left-lllg">{{invoiceReceive.name}}</div>
                  </div>
                  <div class="item rl-clear">
                    <span class="rl-fl rl-tr">邮箱:</span>
                    <div class="rl-fl rl-margin-left-lllg">{{invoiceReceive.zipCode}}</div>
                  </div>
                  <div class="item rl-clear">
                    <span class="rl-fl rl-tr">配送方式:</span>
                    <div class="rl-fl rl-margin-left-lllg">{{invoiceReceive.distributionName}}</div>
                  </div>
                </div>
                <div class="info-right rl-fl">
                  <div class="item rl-clear">
                    <span class="rl-fl rl-tr">收件地址:</span>
                    <div class="rl-fl rl-margin-left-lllg">{{invoiceReceive.address}}</div>
                  </div>
                  <div class="item rl-clear">
                    <span class="rl-fl rl-tr">联系电话:</span>
                    <div class="rl-fl rl-margin-left-lllg">{{invoiceReceive.mobile}}</div>
                  </div>
                  <div class="item rl-clear">
                    <span class="rl-fl rl-tr">物流单号:</span>
                    <div class="rl-fl rl-margin-left-lllg">{{invoiceReceive.logisticsNo}}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Header from '@/components/Header.vue'
import Left from '@/components/Left.vue'
export default {
  name: 'InvoiceDetail',
  components: {
    Header,
    Left
  },
  data () {
    return {
      userState: 2,
      id: this.$route.params.invoice_id,
      invoiceDetail: {},
      billsList: [],
      invoicePayment: {},
      invoiceReceive: {}
    }
  },
  methods: {
    // 获取发票详情
    getInvoiceDetail () {
      this.$api.get(this, 'user/u/invoice', {id: this.id}).then(res => {
        if (res.code === 0) {
          this.invoiceDetail = res.invoice
          this.billsList = res.invoice.bills
          this.invoicePayment = res.invoice.payment
          this.invoiceReceive = res.invoice.receive
        }
      })
    }
  },
  mounted () {
    this.getInvoiceDetail()
  }
}
</script>

<style scoped="scoped" lang='less'>
  .user{width: 100%;}
  .user-main{width: 1024px}
  .user-right {
    width: 794px;
    .content{
      .title {
        width: 100%;
        .title-cons {
          width: 117px;
          height: 35px;
          line-height: 35px;
          background: url("../../assets/images/jiao-blue.png") center center no-repeat;
          img {
            vertical-align: -3px;
          }
        }
      }
      .invoice-info{
        .info{
          padding-left: 60px;
          .item{
            width: 734px;
            height: 30px;
            line-height: 30px;
            span{
              width: 130px;
            }
          }
        }
      }
      .bill{
        table{
          tr{
            th{
              width: 158px;
              height: 45px;
              line-height: 45px;
              background-color: #00c9dc;
              font-size: 13px;
            }
            td{
              height: 80px;
              line-height: 80px;
              text-align: center;
            }
          }
        }
      }
      .gather-info{
        .info-detail{
          padding-left: 30px;
          .item{
            width: 764px;
            height: 30px;
            line-height: 30px;
          }
        }
      }
      .ticket-info{
        height: 130px;
        .info-detail{
          padding-left: 30px;
          .item{
            width: 370px;
            height: 30px;
            line-height: 30px;
            span{
              width: 60px;
            }
          }
        }
      }
    }
  }
</style>
