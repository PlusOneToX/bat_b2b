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
              <div class="item rl-fl rl-relative">
                <input class="cursor-pointer trade-input" type="text" readonly="readonly" v-model="ticketState" @click= "getTicketStateType()">
                <ul class="selectUl rl-bg-white"  v-if="ticketType ===true ">
                  <li>已开票</li>
                  <li>未开票</li>
                  <li>待开票</li>
                </ul>
              </div>
              <div class="item rl-fl rl-relative">
                <input class="rl-margin-left-xxs cursor-pointer trade-input" type="text" readonly="readonly" v-model="tradeState" @click= "getTradeStateType()">
                <ul class="selectUl selectUl-spe rl-bg-white" v-if="tradeType ===true ">
                  <li>已支付</li>
                  <li>未付款</li>
                  <li>待发货</li>
                </ul>
              </div>
              <div class="item rl-fl rl-clear rl-margin-bottom-xxxs rl-margin-left-default">
                <span class="rl-fl rl-margin-right-xxs">对账时间</span>
                <div class="time-input rl-fl"><input class="cursor-pointer " type="text" placeholder="单行输入" readonly="readonly"></div>
                <span class="rl-fl rl-padding-left-xxxs rl-padding-right-xxxs">－</span>
                <div class="time-input rl-fl"><input class="cursor-pointer " type="text" placeholder="单行输入" readonly="readonly"></div>
              </div>
              <div class="item rl-fl rl-margin-left-mid rl-text-xxss"><span class="screen rl-bg-blue-xs rl-text-white rl-tc cursor-pointer">筛选</span></div>
              <div class="item rl-fr">
                <div class="search-input rl-fl rl-margin-right-mid"><input type="text"></div>
                <span class="search-span rl-fl rl-bg-blue-xs rl-text-white rl-tc cursor-pointer">搜索</span>
              </div>
            </div>
            <div class="ticket-table rl-margin-top-xxxs">
              <table>
                <tr>
                  <th width="70px">对账单号</th>
                  <th width="70px">开票状态</th>
                  <th width="70px">对账时间</th>
                  <th width="70px">对账金额</th>
                  <th width="70px">对账状态</th>
                  <th width="70px">付款状态</th>
                  <th width="70px">逾期时间</th>
                  <th width="85px">备注</th>
                  <th width="219px">状态</th>
                </tr>
                <tr  class="rl-bdb-gray-sm rl-bdl-gray-sm rl-bdr-gray-sm" v-for="bill in billList" :key="bill.id">
                  <td>{{bill.distributorId}}</td>
                  <td></td>
                  <td>{{new Date(parseInt(bill.createTime)).toLocaleDateString()}}</td>
                  <td>{{bill.totalAmount}}</td>
                  <td v-if="bill.status === 1">发起对账</td>
                  <td v-if="bill.status === 2">拒绝对账</td>
                  <td v-if="bill.status === 3">发起折扣申请</td>
                  <td v-if="bill.status === 4">取消对账</td>
                  <td v-if="bill.status === 5">对账完成</td>
                  <td v-if="bill.payType === 1">立即支付</td>
                  <td v-if="bill.payType === 2">期间结算</td>
                  <td></td>
                  <td></td>
                  <td>
                    <div class="state">
                      <span @click="changeState(bill.id,5)" class="rl-tc rl-text-white rl-bg-blue cursor-pointer rl-txt-xxss" v-if="bill.status !== 5 && bill.status !== 2">确认</span>
                      <span @click="changeState(bill.id,2)" class="rl-tc rl-text-white rl-bg-orange-mm cursor-pointer rl-txt-xxss" v-if="bill.status !== 5 && bill.status !== 2">拒绝</span>
                      <span @click="goBillDetial(bill.id)" class="rl-tc rl-text-white rl-bg-blue-xs cursor-pointer rl-txt-xxss">查看</span>
                    </div>
                  </td>
                </tr>
              </table>
            </div>
            <div class="rl-tr rl-margin-top-default">
              <el-pagination
                background
                @current-change ="handleCurrentChange"
                @size-change="handleSizeChange"
                layout="prev, pager, next, jumper"
                :page-size="pagesize"
                :total="totalCount"
              >
              </el-pagination>
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
  name: 'Statements',
  components: {
    Header,
    Left
  },
  data () {
    return {
      userState: 2,
      tradeState: '已支付',
      tradeType: false, // 是否显示交易状态
      ticketState: '已开票',
      ticketType: false, // 是否显示交易状态
      totalCount: 5,
      cur_page: 1,
      pagesize: 5,
      billList: []
    }
  },
  methods: {
    // 当前页码
    handleCurrentChange (val) {
      this.cur_page = val
      this.getLosegoodsList()
    },
    // 每页条数
    handleSizeChange (val) {
      this.pagesize = val
      this.getLosegoodsList()
    },
    getTradeStateType () {
      this.tradeType = !this.tradeType
    },
    getTicketStateType () {
      this.ticketType = !this.ticketType
    },
    checkLog () {
      this.showcov = true
    },
    shutLog () {
      this.showcov = false
    },
    // 对账单列表
    getBillList () {
      this.$api.get(this, 'user/u/bill/list', {page: this.cur_page, count: this.pagesize}).then(res => {
        if (res.code === 0) {
          this.billList = res.bills
        }
      })
    },
    // 前往对账单详情
    goBillDetial (id) {
      this.$router.push({name: 'StatementsDetail', params: {bill_id: id}})
    },
    // 对账单状态变更
    changeState (id, status) {
      this.$api.put(this, 'user/u/bill/status', {id: id, status: status}).then(res => {
        if (res.code === 0) {
          this.$message.success('状态修改成功')
          this.getBillList()
        }
      })
    }

  },
  mounted () {
    this.getBillList()
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
          .selectUl{
            position: absolute;
            top:30px;
            left:0;
            z-index: 11;
            width: 120px;
            box-sizing: border-box;
            border: 1px solid #ccc;
            li{
              padding-left: 10px;
              line-height: 30px;
              cursor: pointer;
            }
          }
          .selectUl-spe{
            left:12px;
          }
        }
      }
      .ticket-table{
        table{
          tr{
            th{
              height: 45px;
              line-height: 45px;
              text-align: center;
              background-color: #00c9dc;
              font-size: 13px;
            }
            td{
              height: 80px;
              line-height: 80px;
              text-align: center;
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
    }
  }
</style>
