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
              <div class="item rl-fl rl-relative rl-margin-top-xxxxs">
                <input class="cursor-pointer trade-input" type="text" readonly="readonly" v-model="invoiceStatusText"  @click= "getTradeStateType()">
                <ul class="selectUl rl-bg-white" v-if="tradeType ===true ">
                  <li @click="getInvoiceStatus('')">全部</li>
                  <li @click="getInvoiceStatus(1)">未付款</li>
                  <li @click="getInvoiceStatus(2)">部分付款</li>
                  <li @click="getInvoiceStatus(3)">已付款</li>
                </ul>
              </div>
              <div class="item rl-fl rl-clear rl-margin-bottom-xxxs rl-margin-left-xxxs">
                <span class="rl-fl rl-margin-right-xxs rl-margin-top-xxxxs">起始时间</span>
                <div class="time-input rl-fl star-time">
                  <el-date-picker v-model="startTime"  type="date" placeholder="开始日期" :picker-options='pickerStarDate'>
                  </el-date-picker>
                </div>
                <span class="rl-fl rl-padding-left-xxxs rl-padding-right-xxxs">－</span>
                <div class="time-input rl-fl end-time">
                  <el-date-picker v-model="endTime"  type="date" placeholder="结束日期" :picker-options="pickerEndDate">
                  </el-date-picker>
                </div>
              </div>
              <div class="item rl-fl rl-margin-left-mid rl-text-xxss"><span class="screen rl-bg-blue-xs rl-text-white rl-tc cursor-pointer rl-margin-top-xxxxs">筛选</span></div>
              <div class="item rl-fr rl-margin-top-xxxxs">
                <div class="search-input rl-fl rl-margin-right-mid"><input type="text" v-model="content" placeholder="请输入"></div>
                <span class="search-span rl-fl rl-bg-blue-xs rl-text-white rl-tc cursor-pointer">搜索</span>
              </div>
            </div>
          </div>
          <div class="query-detail rl-margin-top-default">
            <div class="table">
              <table>
                <tr>
                  <th class="spe" width="30px"><span></span></th>
                  <th width="100px">发票号</th>
                  <th width="135px">创建时间</th>
                  <th width="130px">发票类型</th>
                  <th width="100px">发票金额</th>
                  <th width="100px">付款状态</th>
                  <th width="199px">操作</th>
                </tr>
                <tr class="rl-bdb-gray-sm rl-bdl-gray-sm rl-bdr-gray-sm" v-for="invoice in invoiceList" :key="invoice.id">
                  <td class="spe"><span></span></td>
                  <td>{{invoice.id}}</td>
                  <td>{{new Date(parseInt(invoice.createTime)).toLocaleDateString()}}</td>
                  <td></td>
                  <td class="rl-text-orange-sm">￥{{invoice.invoiceAmount}}</td>
                  <td v-if="invoice.payState === 1">未付款</td>
                  <td v-if="invoice.payState === 2">部分付款</td>
                  <td v-if="invoice.payState === 3">已付款</td>
                  <td><span @click="goInvoiceDetail(invoice.id)" class="log rl-margin-zero rl-tc rl-text-white rl-bg-blue-xs rl-text-xxss cursor-pointer">查看</span></td>
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
  name: 'Invoice',
  components: {
    Header,
    Left
  },
  data () {
    return {
      userState: 2,
      tradeType: false, // 是否显示交易状态
      totalCount: 0,
      cur_page: 1,
      pagesize: 5,
      payState: '',
      invoiceStatusText: '全部',
      startTime: '',
      endTime: '',
      content: '',
      invoiceList: [],
      pickerStarDate: {
        disabledDate: (time) => {
          let beginDateVal = this.endTime
          if (beginDateVal) {
            return time.getTime() > beginDateVal
          }
        }
      },
      pickerEndDate: {
        disabledDate: (time) => {
          let beginDateVal = this.startTime
          if (beginDateVal) {
            return time.getTime() < beginDateVal
          }
        }
      }
    }
  },
  methods: {
    // 当前页码
    handleCurrentChange (val) {
      this.cur_page = val
      this.getInvoiceList()
    },
    // 每页条数
    handleSizeChange (val) {
      this.pagesize = val
      this.getInvoiceList()
    },
    // 发票列表
    getInvoiceList () {
      this.$api.get(this, 'user/u/invoice/list', {page: this.cur_page, count: this.pagesize}).then(res => {
        if (res.code === 0) {
          this.invoiceList = res.invoices
        }
      })
    },
    // 获取发票列表总数
    getInvoiceListSum () {
      this.$api.get(this, 'user/u/invoice/count').then(res => {
        if (res.code === 0) {
          this.totalCount = res.count
        }
      })
    },
    // 前往发票详情
    goInvoiceDetail (id) {
      this.$router.push({name: 'InvoiceDetail', params: {invoice_id: id}})
    },
    // 发票管理状态（查询）
    getTradeStateType () {
      this.tradeType = !this.tradeType
    },
    getInvoiceStatus (type) {
      this.payState = type
      this.tradeType = false
      if (type === '') {
        this.invoiceStatusText = '全部'
      } else if (type === 1) {
        this.invoiceStatusText = '未付款'
      } else if (type === 2) {
        this.invoiceStatusText = '部分付款'
      } else if (type === 3) {
        this.invoiceStatusText = '已付款'
      }
    }
  },
  mounted () {
    this.getInvoiceList()
    this.getInvoiceListSum()
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
              padding-left: 20px;
              width: 100px;
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
            width: 160px;
            height: 28px;
            border:1px solid #ccc;
            input{
              padding-left: 10px;
              width: 150px;
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
            li:hover{
              background: #00c9dc;
              color: #fff;
            }
          }
        }
      }
    }
    .query-detail{
      .table{
        width: 100%;
        table{
          word-wrap: break-word;
          word-break: break-all;
          border-collapse: collapse;
          tr{
            th,td.spe{
              span{
                display: block;
                width: 30px;
                height: 45px;
                background: url("../../assets/images/choose.png") no-repeat center center;
              }
            }
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
              font-size: 12px;
              .log{
                display: block;
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
  .star-time{
    .el-date-editor{
      width: 139px;
    }
    .el-input{
      .el-input__inner{
        padding-right: 0;
      }
    }
  }
  .end-time{
    .el-date-editor{width: 139px}
  }
</style>
