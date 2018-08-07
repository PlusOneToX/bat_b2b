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
            <div class="search rl-clear">
              <div class="item rl-fl">
                <span class="rl-fl rl-margin-right-mid">订单号</span>
                <div class="search-input rl-fl rl-margin-right-mid"><input type="text" v-model="billOrderId"></div>
                <span @click="queryBill" class="search-span rl-fl rl-bg-blue-xs rl-text-white rl-tc cursor-pointer">搜索</span>
              </div>
              <div class="item rl-fl rl-clear rl-margin-bottom-xxxs rl-margin-left-double">
                <span class="rl-fl rl-margin-right-xxs">发货时间</span>
                <div class="time-input rl-fl"><input class="cursor-pointer " type="text" placeholder="单行输入" readonly="readonly"></div>
                <span class="rl-fl rl-padding-left-xxxs rl-padding-right-xxxs">－</span>
                <div class="time-input rl-fl"><input class="cursor-pointer " type="text" placeholder="单行输入" readonly="readonly"></div>
              </div>
            </div>
            <div class="ticketDetail-table rl-margin-top-xxxs">
              <table>
                <tr>
                  <th>商品编码</th>
                  <th>商品名称</th>
                  <th>规格型号</th>
                  <th>订单号</th>
                  <th>下单时间</th>
                  <th>发货单号</th>
                  <th>发货时间</th>
                  <th>支付方式</th>
                  <th>付款状态</th>
                  <th>商品单价</th>
                  <th>折扣率</th>
                </tr>
                <tr class="rl-bdb-gray-sm rl-bdl-gray-sm rl-bdr-gray-sm" v-for="bill in billListDelivers" :key="bill.id">
                  <td>{{bill.goodsNo}}</td>
                  <td>{{bill.goodsName}}</td>
                  <td>{{bill.specificationName}}</td>
                  <td>{{bill.billOrderId}}</td>
                  <td>{{new Date(parseInt(bill.createTime)).toLocaleDateString()}}</td>
                  <td></td>
                  <td></td>
                  <td v-if="bill.status === 1">发起对账</td>
                  <td v-if="bill.status === 2">拒绝对账</td>
                  <td v-if="bill.status === 3">发起折扣申请</td>
                  <td v-if="bill.status === 4">取消对账</td>
                  <td v-if="bill.status === 5">对账完成</td>
                  <td v-if="bill.payType === 1">立即支付</td>
                  <td v-if="bill.payType === 2">期间结算</td>
                  <td>{{bill.billAmount}}</td>
                  <td class="rl-text-orange-sm">{{bill.discountPercentage}}</td>
                </tr>
              </table>
            </div>
            <div class="totle rl-clear rl-padding-top-default rl-padding-right-default rl-padding-bottom-default">
              <div class="rl-fr">
                <div class="item"><span class="rl-margin-right-lllg">总金额：</span><span class="rl-text-orange-sm">45.000</span></div>
                <div class="item"><span class="rl-margin-right-lllg">折扣金额：</span><span class="rl-text-orange-sm">45.000</span></div>
                <div class="item"><span class="rl-margin-right-lllg">对账金额：</span><span class="rl-text-orange-sm">45.000</span></div>
              </div>
            </div>
            <div class="note" v-if="this.billOrder.status == 1 || this.billOrder.status == 3 || this.billOrder.status == 4">
              <div class="note-cons rl-clear rl-padding-left-mid rl-margin-top-double rl-margin-bottom-lllg">
                <div class="rl-fl rl-text-xxss">备注:</div>
                <div class="rl-fl text-area rl-margin-left-default"><textarea class="rl-padding-left-xxxs" v-model="remark"></textarea></div>
              </div>
              <div class="footer-btn rl-margin-zero">
                <span @click="changeState(5)" class="rl-bg-blue-xs rl-text-white rl-tc rl-text-xxss cursor-pointer">确认</span>
                <span @click="changeState(2)" class="rl-bg-orange-mm rl-text-white rl-tc rl-text-xxss cursor-pointer">拒绝</span>
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
  name: 'Invoice',
  components: {
    Header,
    Left
  },
  data () {
    return {
      userState: 2,
      id: this.$route.params.bill_id,
      remark: '',
      billOrderId: '',
      billOrder: {},
      billListDelivers: [],
      billDetailList: []
    }
  },
  methods: {
    // 对账单详情
    getBillDetail () {
      this.$api.get(this, 'user/u/bill', {id: this.id}).then(res => {
        if (res.code === 0) {
          var tempArray = [] // 临时数组
          this.billListDelivers = res.delivers
          this.billOrder = res.order
          this.billListDelivers.forEach((obj) => {
            var product = {billAmount: obj.billAmount, billOrderId: obj.billOrderId, count: obj.count, createTime: obj.createTime, deliverOrderId: obj.deliverOrderId, deliverTime: obj.deliverTime, discountAmount: obj.discountAmount, discountPercentage: obj.discountPercentage, goodsId: obj.goodsId, goodsName: obj.goodsName, goodsNo: obj.goodsNo, id: obj.id, itemCode: obj.itemCode, itemId: obj.itemId, itemName: obj.itemName, orderId: obj.orderId, orderTime: obj.orderTime, saleAmount: obj.saleAmount, specificationId: obj.specificationId, specificationName: obj.specificationName, specificationValueId: obj.specificationValueId, specificationValueName: obj.specificationValueName, updateTime: obj.updateTime, payType: res.order.payType, status: res.order.status, distributorId: res.order.distributorId}
            tempArray.push(product)
          })
          this.billListDelivers = tempArray
        }
      })
    },
    // 对账单查询(根据订单号)
    queryBill () {
      this.$api.get(this, 'user/u/bill/log/list', {billOrderId: Number(this.billOrderId)}).then(res => {
        if (res.code === 0) {
        }
      })
    },
    // 对账单状态变更
    changeState (status) {
      this.$api.put(this, 'user/u/bill/status', {id: this.billOrder.id, status: status, remark: this.remark}).then(res => {
        if (res.code === 0) {
          this.remark = ''
          this.$message.success('状态修改成功')
        }
      })
    }
  },
  mounted () {
    this.getBillDetail()
  }
}
</script>

<style scoped="scoped" lang='less'>
  .user{width: 100%;}
  .user-main{width: 1024px}
  .user-right {
    width: 794px;
    .content{
      .search{
        .item{
          input.trade-input{
            padding-left: 10px;
            width: 70px;
            height: 30px;
            line-height: 30px;
            box-sizing: border-box;
            border: 1px solid #ccc;
            font-size: 13px;
            background: url("../../assets/images/selectUl.png") no-repeat 58px center;
            border-radius: 5px;
          }
          span{
            line-height: 30px;
          }
          .time-input{
            input{
              padding-left: 10px;
              width: 90px;
              height: 30px;
              box-sizing: border-box;
              border: 1px solid #ccc;
            }
          }
          .screen{
            display: block;
            width: 60px;
            height: 30px;
            border-radius: 5px;
          }
          .search-input{
            width: 166px;
            height: 28px;
            border:1px solid #ccc;
            input{
              padding-left: 10px;
              width: 156px;
              border:0;
              line-height: 28px;
            }
          }
          .search-span{
            display: block;
            width: 70px;
            height: 30px;
            border-radius: 5px;
          }
        }
      }
      .ticketDetail-table{
        table{
          tr{
            th{
              width: 72px;
              height: 45px;
              line-height: 45px;
              text-align: center;
              background-color: #00c9dc;
              font-size: 13px;
            }
            td{
              height: 60px;
              line-height: 60px;
              text-align: center;
              font-size: 12px;
              .state{
                span{
                  display: inline-block;
                  width: 60px;
                  height: 30px;
                  line-height: 30px;
                  border-radius: 5px;
                }
              }
            }
          }
        }
      }
      .totle{
        border-left:1px solid #c8c7cd;
        border-right:1px solid #c8c7cd;
        border-bottom:1px solid #c8c7cd;
        .item{
          line-height: 30px;
          span{
            display: inline-block;
            &:first-child{
              width: 100px;
              text-align: right;
            }
          }
        }
      }
      .note{
        .note-cons{
          .text-area{
            textarea{
              display: block;
              width: 715px;
              height: 115px;
              border-radius: 5px;
            }
          }
        }
        .footer-btn{
          width: 130px;
          span{
            display: inline-block;
            width: 60px;
            height: 32px;
            line-height: 32px;
            border-radius: 5px;
          }
        }
      }
    }
  }
</style>
