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
            <div class="title rl-clear rl-bg-gray-mm">
              <div @click="toApply" class="rl-fl rl-text-xxss rl-tc cursor-pointer" :class="tabs=='apply' ? 'current': ''">售后申请</div>
              <div @click="toRecord" class="rl-fl rl-text-xxss rl-tc cursor-pointer" :class="tabs=='record' ? 'current': ''">申请记录</div>
            </div>
            <div v-show="tabs=='apply'" class="tab">
              <div class="add-return rl-margin-top-mid rl-margin-bottom-mid">
                <div @click="checkLog" class="rl-text-xxss rl-tc rl-bg-blue-xs rl-text-white cursor-pointer">添加退换货品</div>
              </div>
              <div class="return-table">
                <table>
                  <tr>
                    <th width="89px">订单号</th>
                    <th width="89px">商品名称</th>
                    <th width="89px">货号</th>
                    <th width="89px">规格</th>
                    <th width="89px">数量</th>
                    <th width="83px">退换数量</th>
                    <th width="89px">下单时间</th>
                    <th width="89px">价格</th>
                    <th width="89px">操作</th>
                  </tr>
                  <tr class="rl-bdb-gray-sm rl-bdl-gray-sm rl-bdr-gray-sm" v-for="confirmShop in confirmApplyShopList" :key="confirmShop.id">
                    <td>{{confirmShop.orderId}}</td>
                    <td>{{confirmShop.goodsName}}</td>
                    <td>{{confirmShop.itemCode}}</td>
                    <td>{{confirmShop.specificationName}}</td>
                    <td>{{confirmShop.itemCount}}</td>
                    <td width="100px"><applyNum ref="applyNum" :confirmShop="confirmShop"></applyNum></td>
                    <td>{{new Date(parseInt(confirmShop.createTime)).toLocaleDateString()}}</td>
                    <td class="rl-text-orange-sm">{{confirmShop.salePrice}}</td>
                    <td><div @click="deleteAfterApply(confirmShop.id)" class="opetate rl-text-white rl-tc rl-text-xxss rl-bg-orange-mm cursor-pointer rl-margin-zero">删除</div></td>
                  </tr>
                </table>
              </div>
              <div class="note">
                <div class="note-cons rl-clear rl-padding-left-mid rl-margin-top-double rl-margin-bottom-lllg">
                  <div class="rl-fl rl-text-xxss">备注:</div>
                  <div class="rl-fl text-area rl-margin-left-default"><textarea class="rl-padding-left-xxxs" v-model="remarks"></textarea></div>
                </div>
                <div class="footer-btn rl-margin-zero">
                  <span @click="submitApply" class="rl-bg-blue-xs rl-text-white rl-tc rl-text-xxss cursor-pointer">提交申请</span>
                </div>
              </div>
            </div>
            <div v-show="tabs=='record'" class="tab rl-margin-top-mid">
              <div class="apply-table">
                <table>
                  <tr>
                    <th>申请单号</th>
                    <th>申请时间</th>
                    <th>对账时间</th>
                    <th>申请原因</th>
                    <th>操作</th>
                  </tr>
                  <tr class="rl-bdb-gray-sm rl-bdl-gray-sm rl-bdr-gray-sm" v-for="apply in afterApplyList" :key="apply.id">
                    <td>{{apply.courierNo}}</td>
                    <td>{{new Date(parseInt(apply.createTime)).toLocaleDateString()}}</td>
                    <td v-if="apply.status === 0">申请中</td>
                    <td v-if="apply.status === 1">待到货</td>
                    <td v-if="apply.status === 2">部分到货</td>
                    <td v-if="apply.status === 3">处理中</td>
                    <td v-if="apply.status === 4">已完成</td>
                    <td v-if="apply.status === 5">已拒绝</td>
                    <td v-if="apply.status === 6">退款中</td>
                    <td>{{apply.remark}}</td>
                    <td @click="goAfterApplyDetail(apply.id)"><div class="opetate rl-text-white rl-tc rl-text-xxss rl-bg-blue-xs cursor-pointer rl-margin-zero">查看</div></td>
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
    <!--筛选订单弹框-->
    <div class="cover" v-if="showcov"></div>
    <div class="pro-cover cover-box rl-padding-bottom-lllg rl-padding-left-xxxs rl-padding-right-xxxs rl-padding-top-mid rl-relative" v-if="showcov">
      <span @click="shutLog" class="shut cursor-pointer"></span>
      <div class="buy">
        <div class="search rl-clear">
          <div class="item rl-fl rl-clear rl-margin-bottom-xxxs rl-margin-left-xxxs">
            <span class="rl-fl rl-margin-right-double rl-margin-top-xxxxs">购买时间</span>
            <!--<div class="time-input rl-fl"><input class="cursor-pointer " type="text" placeholder="单行输入" readonly="readonly"></div>-->
            <div class="rl-fl star-time">
              <el-date-picker v-model="filters.column.starDate"  type="date" placeholder="开始日期" :picker-options='pickerStarDate'>
            </el-date-picker>
            </div>
            <span class="rl-fl rl-padding-left-xxxs rl-padding-right-xxxs">－</span>
            <!--<div class="time-input rl-fl"><input class="cursor-pointer " type="text" placeholder="单行输入" readonly="readonly"></div>-->
            <div class="rl-fl end-time">
              <el-date-picker v-model="filters.column.endDate"  type="date" placeholder="结束日期" :picker-options="pickerEndDate">
              </el-date-picker>
            </div>
          </div>
          <div @click="choose" class="item rl-fl rl-margin-left-mid rl-text-xxss"><span class="screen rl-bg-blue-xs rl-text-white rl-tc cursor-pointer">筛选</span></div>
          <div class="item rl-fr">
            <div class="search-input rl-fl rl-margin-right-mid rl-margin-top-xxxxs"><input type="text"></div>
            <span class="search-span rl-fl rl-bg-blue-xs rl-text-white rl-tc cursor-pointer">搜索</span>
          </div>
        </div>
      </div>
      <div class="buy-table">
        <table>
          <tr>
            <th class="spe" width="30px"><span></span></th>
            <th width="100px">订单号</th>
            <th width="100px">商品名称</th>
            <th width="100px">货号</th>
            <th width="100px">规格</th>
            <th width="95px">数量</th>
            <th width="100px">下单时间</th>
            <th width="160px">货品单价</th>
          </tr>
          <tr class="rl-bdb-gray-sm rl-bdl-gray-sm rl-bdr-gray-sm" v-for="applyShop in applyShopList" :key="applyShop.id">
            <td class="spe cursor-pointer"><span :class ="{'gou':applyShop.gou === 0}" @click="chooseGou(applyShop,applyShop.gou)"></span></td>
            <td>{{applyShop.orderId}}</td>
            <td>{{applyShop.goodsName}}</td>
            <td>{{applyShop.itemCode}}</td>
            <td>{{applyShop.specificationName}}</td>
            <td>{{applyShop.itemCount}}</td>
            <td>{{new Date(parseInt(applyShop.createTime)).toLocaleDateString()}}</td>
            <td class="rl-text-orange-sm">￥{{applyShop.salePrice}}</td>
          </tr>
        </table>
      </div>
      <div class="footer-btn rl-margin-zero rl-padding-top-double">
        <span class="rl-bg-blue-xs rl-text-white rl-tc rl-text-xxss cursor-pointer" @click="confirmOrder">确定</span>
        <span @click="shutLog" class="rl-bg-blue-xs rl-text-white rl-tc rl-text-xxss cursor-pointer">取消</span>
      </div>
    </div>
  </div>
</template>

<script>
import Vue from 'vue'
import Header from '@/components/Header.vue'
import Left from '@/components/Left.vue'
import applyNum from '@/components/applyNum.vue'
import {dateToNum} from '@/assets/js/common.js'
export default {
  name: 'AfterApplication',
  components: {
    Header,
    Left,
    applyNum
  },
  data () {
    return {
      userState: 2,
      tabs: 'apply',
      totalCount: 0,
      cur_page: 1,
      pagesize: 8,
      showcov: false, // 是否显示筛选订单
      applyShopList: [],
      confirmApplyShopList: [],
      filters: {
        column: {
          starDate: new Date(),
          endDate: new Date()
        }
      },
      pickerStarDate: {
        disabledDate: (time) => {
          let beginDateVal = this.filters.column.endDate
          if (beginDateVal) {
            return time.getTime() > beginDateVal
          }
        }
      },
      pickerEndDate: {
        disabledDate: (time) => {
          let beginDateVal = this.filters.column.starDate
          if (beginDateVal) {
            return time.getTime() < beginDateVal
          }
        }
      },
      remarks: '', // 备注
      submitApplyList: [],
      afterApplyList: [] // 售后申请列表
    }
  },
  watch: {
    startTime () {
      this.checkTimes()
    },
    endTime () {
      this.checkTimes()
    }
  },
  methods: {
    // tab切换
    toApply () {
      this.tabs = 'apply'
    },
    toRecord () {
      this.tabs = 'record'
    },
    // 当前页码
    handleCurrentChange (val) {
      this.cur_page = val
      this.getApplyList()
    },
    // 每页条数
    handleSizeChange (val) {
      this.pagesize = val
      this.getApplyList()
    },
    checkLog () {
      this.showcov = true
    },
    shutLog () {
      this.showcov = false
    },
    // 选中售后订单
    chooseGou (stockProduct, gou) {
      if (gou === 1) {
        stockProduct.gou = 0
      } else {
        stockProduct.gou = 1
      }
    },
    // 确认勾选的售后订单
    confirmOrder () {
      this.showcov = false
      this.applyShopList.forEach(item => {
        if (item.gou === 0) {
          this.confirmApplyShopList.push(item)
        }
      })
      // console.log(this.confirmApplyShopList)
    },
    // 确认提交申请
    submitApply () {
      var tempArray = [] // 临时数组
      if (this.confirmApplyShopList !== null) {
        this.confirmApplyShopList.forEach((obj) => {
          var product = {'orderId': obj.orderId, 'itemId': obj.itemId, 'goodsName': obj.goodsName, 'specificationId': obj.specificationId, 'specificationValueId': obj.specificationValueId, 'applyCount': obj.applyCount}
          tempArray.push(product)
        })
      }
      this.submitApplyList = this.confirmApplyShopList
      if (tempArray.length === 0) {
        this.$message.warning('请添加退换货')
        return false
      }
      this.$api.post(this, 'user/u/afterservice', {remark: this.remarks, list: tempArray}).then(res => {
        if (res.code === 0) {
          this.getApplyList()
          this.$message({
            message: '提交成功',
            type: 'success',
            duration: 1000
          })
          this.confirmApplyShopList = null
          this.remarks = ''
          this.getApplyCount()
        }
      })
    },
    // 申请订单列表
    choose () {
      if (this.filters.column.starDate === '') {
        this.$message({
          type: 'warning',
          message: '请选择开始时间！'
        })
        return false
      }
      if (this.filters.column.endDate === '') {
        this.$message({
          type: 'warning',
          message: '请选择截止时间！'
        })
        return false
      }
      this.checkTimes()
      var startTimes = ''
      var endTimes = ''
      startTimes = this.filters.column.starDate.getTime()
      endTimes = this.filters.column.endDate.getTime()
      this.$api.get(this, 'user/u/order/apply/list', {startTime: startTimes, endTime: endTimes}).then(res => {
        if (res.code === 0) {
          this.$message({
            message: '请求成功',
            type: 'success',
            duration: 1000
          })
          this.applyShopList = res.orders
          this.applyShopList.forEach((stockProduct) => {
            Vue.set(stockProduct, 'gou', 1)
            Vue.set(stockProduct, 'applyCount', 0)
          })
        }
      })
    },
    dateToNum,
    checkTimes () {
      const startTime = this.dateToNum(this.filters.column.starDate)
      const endTime = this.dateToNum(this.filters.column.endDate)
      const now = this.dateToNum(new Date())
      if (startTime < now) {
        this.$message({message: '请选择未来的时间', type: 'warning'})
        this.startTime = new Date()
      } else {
        if (startTime > endTime) {
          this.$message({message: '终点时间必须在起点时间之后', type: 'warning'})
          this.endTime = this.startTime
        }
      }
    },
    // 售后列表（申请记录）
    getApplyList () {
      this.$api.get(this, 'user/u/afterservice/list', {page: this.cur_page, count: this.pagesize}).then(res => {
        if (res.code === 0) {
          this.afterApplyList = res.list
        }
      })
    },
    // 售后列表总数（申请记录）
    getApplyCount () {
      this.$api.get(this, 'user/u/afterservice/count').then(res => {
        if (res.code === 0) {
          this.totalCount = res.count
        }
      })
    },
    // 售后申请详情（申请记录）
    goAfterApplyDetail (id) {
      this.$router.push({name: 'AfterApplicationDetail', params: {apply_id: id}})
    },
    // 删除售后申请（单条）
    deleteAfterApply (id) {
      this.$confirm('此操作将删除该售后需退换货品, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.confirmApplyShopList.forEach((items, index) => {
          if (items.id === id) {
            this.confirmApplyShopList.splice(index, 1)
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: this.$t('P.Canceled')
        })
      })
    }
  },
  mounted () {
    this.getApplyList()
    this.getApplyCount()
  }
}
</script>

<style scoped="scoped" lang='less'>
  .user{width: 100%;}
  .user-main{width: 1024px}
  .user-right {
    width: 794px;
    .content{
      .title{
        width: 100%;
        div{
          width: 90px;
          height: 43px;
          line-height: 45px;
          border:1px solid #a0a0a0;
        }
        .current{
          color:#fff;
          background-color: #00c9dc;
          border: 0;
          height: 45px;
        }
      }
      .add-return{
        div{
          width: 115px;
          height: 30px;
          line-height: 30px;
          border-radius: 5px;
        }
      }
      .return-table{
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
          width: 106px;
          span{
            display: inline-block;
            width: 105px;
            height: 32px;
            line-height: 32px;
            border-radius: 5px;
          }
        }
      }
      .apply-table{
        tr{
          th{
            width: 159px;
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
  .pro-cover{
    width: 800px;
    /*height: 375px;*/
    border: 1px solid #ccc;
    border-radius: 5px;
    z-index: 99;
    background: #fefefe;
    .buy{
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
              width: 113px;
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
    }
    .buy-table{
      width: 100%;
      max-height: 720px;
      overflow-y: auto;
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
            span.gou{
              background: url("../../assets/images/gou.png") no-repeat center center;
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
            height: 70px;
            text-align: center;
            font-size: 12px;
          }
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
  .cover-box{
    box-sizing: border-box;
    position: fixed;
    top: 20px;
    left: 0;
    bottom: 20px;
    right: 0;
    margin: auto;
    z-index: 99;
    .shut{
      position: absolute;
      top:-8px;
      right:-8px;
      display: block;
      width: 18px;
      height: 18px;
      background: url("../../assets/images/shut.png") no-repeat center center;
    }
  }
  .star-time{
    .el-date-editor{
      width: 140px;
    }
    .el-input{
      .el-input__inner{
        padding-right: 0;
      }
    }
  }
  .end-time{
    .el-date-editor{width: 140px}
  }
</style>
