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
              <div class="rl-padding-left-lllg title-cons rl-text-white rl-text-mid">申请详情</div>
            </div>
            <div class="apply-info">
              <div class="invoice-info rl-padding-bottom-default rl-margin-top-mid">
                <div class="info rl-padding-left-default">
                  <div class="item">
                    <span class="rl-fl rl-tr">申请单号:</span>
                    <div class="rl-fl rl-margin-left-lllg">{{afterApplyDetail.courierNo}}</div>
                  </div>
                  <div class="item">
                    <span class="rl-fl rl-tr">申请时间:</span>
                    <div class="rl-fl rl-margin-left-lllg">{{new Date(parseInt(afterApplyDetail.createTime)).toLocaleDateString()}}</div>
                  </div>
                  <div class="item">
                    <span class="rl-fl rl-tr">申请状态:</span>
                    <div class="rl-fl rl-margin-left-lllg" v-if="afterApplyDetail.status === 0">申请中</div>
                    <div class="rl-fl rl-margin-left-lllg" v-if="afterApplyDetail.status === 1">待到货</div>
                    <div class="rl-fl rl-margin-left-lllg" v-if="afterApplyDetail.status === 2">部分到货</div>
                    <div class="rl-fl rl-margin-left-lllg" v-if="afterApplyDetail.status === 3">处理中</div>
                    <div class="rl-fl rl-margin-left-lllg" v-if="afterApplyDetail.status === 4">已完成</div>
                    <div class="rl-fl rl-margin-left-lllg" v-if="afterApplyDetail.status === 5">已拒绝</div>
                    <div class="rl-fl rl-margin-left-lllg" v-if="afterApplyDetail.status === 6">退款中</div>
                  </div>
                  <div class="item">
                    <span class="rl-fl rl-tr">申请原因:</span>
                    <div class="rl-fl rl-margin-left-lllg">{{afterApplyDetail.remark}}</div>
                  </div>
                </div>
              </div>
            </div>
            <div class="title rl-bg-gray-mm">
              <div class="rl-padding-left-lllg title-cons rl-text-white rl-text-mid">商品列表</div>
            </div>
            <div class="apply-table rl-margin-top-mid">
              <table>
                <tr>
                  <th>商品名称</th>
                  <th>商品编号</th>
                  <th>订单号</th>
                  <th>规格</th>
                  <th>申请数量</th>
                  <th>到货数量</th>
                  <th>退货数量</th>
                  <th>换货数量</th>
                  <th>退款金额</th>
                </tr>
                <tr class="rl-bdb-gray-sm rl-bdl-gray-sm rl-bdr-gray-sm" v-for="apply in afterApplyList" :key="apply.id">
                  <td>{{apply.goodsName}}</td>
                  <td>{{apply.goodsNo}}</td>
                  <td>{{apply.orderId}}</td>
                  <td>{{apply.specificationName}}</td>
                  <td>{{apply.applyCount}}</td>
                  <td>{{apply.arrivalCount}}</td>
                  <td>{{apply.returnCount}}</td>
                  <td>{{apply.exchangeCount}}</td>
                  <td>-</td>
                </tr>
              </table>
              <div class="total rl-bg-gray-xm rl-bdb-gray-sm rl-bdl-gray-sm rl-bdr-gray-sm">
                <div class="cons rl-margin-zero"><span>总计：</span><span>{{totalApplyCount}}</span><span>{{totalArrivalCount}}</span><span>{{totalReturnCount}}</span><span>{{totalExchangeCount}}</span></div>
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
  name: 'AfterApplicationDetail',
  components: {
    Header,
    Left
  },
  data () {
    return {
      userState: 2,
      id: this.$route.params.apply_id,
      afterApplyDetail: {},
      afterApplyList: []
    }
  },
  computed: {
    totalApplyCount () { // 申请数量
      var total = 0
      this.afterApplyList.forEach((item) => {
        total += item.applyCount
      })
      return total
    },
    totalArrivalCount () { // 到货数量
      var total = 0
      this.afterApplyList.forEach((item) => {
        total += item.arrivalCount
      })
      return total
    },
    totalReturnCount () { // 退货数量
      var total = 0
      this.afterApplyList.forEach((item) => {
        total += item.returnCount
      })
      return total
    },
    totalExchangeCount () { // 换货数量
      var total = 0
      this.afterApplyList.forEach((item) => {
        total += item.exchangeCount
      })
      return total
    }
  },
  methods: {
    // 申请详情
    getAfterApplyDetail () {
      this.$api.get(this, 'user/u/afterservice', {id: this.id}).then(res => {
        if (res.code === 0) {
          this.afterApplyDetail = res.afterServiceBean
          this.afterApplyList = res.afterServiceBean.list
        }
      })
    }
  },
  mounted () {
    this.getAfterApplyDetail()
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
      .apply-info{
        .invoice-info{
          .info{
            .item{
              width: 734px;
              height: 30px;
              line-height: 30px;
            }
          }
        }
      }
      .apply-table{
        tr{
          th{
            width: 88px;
            height: 45px;
            line-height: 45px;
            text-align: center;
            background-color: #00c9dc;
            font-size: 13px;
          }
          td{
            height: 70px;
            text-align: center;
            font-size: 12px;
            .opetate{
              width: 50px;
              height: 30px;
              line-height: 30px;
              border-radius: 5px;
            }
          }
        }
        .total{
          width: 100%;
          .cons{
            padding-left: 176px;
            width: 440px;
            height: 55px;
            line-height: 55px;
            span{
              display: block;
              width: 88px;
              height: 55px;
              line-height: 55px;
              text-align: center;
              float: left;
            }
          }
        }
      }
    }
  }
</style>
