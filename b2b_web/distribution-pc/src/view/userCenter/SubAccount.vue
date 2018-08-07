<template>
  <div class="store-manage">
    <!--公共头部-->
    <Header :userState="userState"></Header>
    <!--主内容-->
    <div class="store-main rl-clear rl-margin-zero">
      <!--公共左边-->
      <Left></Left>
      <div class="store-right rl-fr rl-bg-white rl-margin-top-default rl-padding-bottom-double">
        <div class="content">
          <h6 class="store-right-title">{{$t('UserCenter.subAccountRecord')}}</h6>
          <div class="search-box">
            <el-input
              :placeholder="$t('UserCenter.OrderNo')"
              type="text"
              clearable
              v-model.trim="pageInfo.orderNo"
              @clear="handleClear"
            ></el-input>
            <!--分账状态--->
            <el-select class="custom_select ml-15" :placeholder="$t('OrderSuccess.Status')"
              size="mini" v-model="pageInfo.status" clearable  @change="handleSelect">
              <el-option label="关闭" :value="0"></el-option>
              <el-option label="待分账" :value="1"></el-option>
              <el-option label="部分分账" :value="2"></el-option>
              <el-option label="全部分账" :value="3"></el-option>
              <el-option label="分账失败" :value="4"></el-option>
            </el-select>
            <!-- 开始时间--结束时间 -->
            <!-- <div class="items date-items rl-fl rl-clear">
                <div class="rl-fl">
                <el-date-picker
                    v-model="filters.column.startTime"
                    type="datetime"
                    :placeholder="$t('ExportOrder.StartTime')"
                    :picker-options="pickerStarDate"
                ></el-date-picker>
                </div>
                <span class="rl-fl">－</span>
                <div class="rl-fl">
                <el-date-picker
                    v-model="filters.column.endTime"
                    type="datetime"
                    :placeholder="$t('ExportOrder.EndTime')"
                    :picker-options="pickerEndDate"
                ></el-date-picker>
                </div>
            </div> -->
             <el-date-picker
                v-model="pageInfo.time"
                style="width: 330px"
                type="datetimerange"
                value-format="timestamp"
                range-separator="至"
                start-placeholder="下单开始日期"
                end-placeholder="下单结束日期"
              ></el-date-picker>
            <!-- 搜索 -->
            <el-button  class="mini-search-btn ml-15" type="info" @click="search" size="mini">{{$t('P.Search')}}</el-button>
            <!-- 导出分账记录 -->
            <div class="handle-wrap rl-tr"  @click="handleExport">
              <i class="iconfont icon-export"></i>
              {{$t('UserCenter.ExportSubAccount')}}
            </div>

          </div>
          <div class="table-box">
            <el-table
              :data="tableData"
              header-row-class-name="header-row"
              border
              style="text-align:center;"
              v-loading="loading"
            >
              <el-table-column align="center" :label="$t('UserCenter.OrderTime')"  prop="createTime" width="160"></el-table-column>
              <el-table-column align="center" :label="$t('UserCenter.OrderNo')" prop="orderNo" :min-width="160"></el-table-column>
              <el-table-column align="center" :label="$t('UserCenter.StoreName')"  prop="shopName" width="160"></el-table-column>
              <el-table-column align="center" :label="$t('UserCenter.ActuallyPay')" prop="payAmount" :min-width="160">
                  <template slot-scope="scope">
                    <div >￥{{scope.row.payAmount}}</div>
                </template>
              </el-table-column>
              <el-table-column align="center" :label="$t('UserCenter.TotalSubAccount')" prop="maxSubAccountAmount" :min-width="160">
                  <template slot-scope="scope">
                    <span class="log rl-tc cursor-pointer" @click="handleDetail(scope.row.id, 1)">￥{{scope.row.maxSubAccountAmount}}</span>
                </template>
              </el-table-column>
              <el-table-column align="center" :label="$t('OrderSuccess.Status')" prop="status" width="120" show-overflow-tooltip>
                <template slot-scope="scope">
                    <div>{{getStatus(scope.row.status)}}</div>
                </template>
              </el-table-column>
              <el-table-column align="center" :label="$t('UserCenter.Remark')"  prop="remark" width="160"></el-table-column>
              <el-table-column align="center"  key="12" :label="$t('ShopCarts.Operation')" :min-width="180">
                <template slot-scope="scope">
                  <div v-if="scope.row.status===2 || scope.row.status===4">
                    <span class="log rl-tc cursor-pointer"  :class="scope.row.Status==='1'?'disabled':''" @click="handleDetail(scope.row.id, 2)">{{$t('P.Modify')}}</span>
                    <span class="log rl-tc cursor-pointer" @click="onChange(scope.row.id)">{{$t('UserCenter.ReSubAccount')}}</span>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
        <div class="rl-tr rl-margin-top-default" v-if="totalCount > 0">
          <el-pagination background :current-page="pageInfo.page" :page-sizes="[10, 20, 30]" :page-size="pageInfo.size"
            @size-change="onSizeChange" @current-change="onCurrentChange"
            layout=" prev, pager, next , total , sizes , jumper"
            :total="totalCount"
            class="page"
          ></el-pagination>
        </div>
      </div>
    </div>
    <!----分账详情弹框----->
    <el-dialog
      title="分账详情"
      :visible.sync="detailDialogVisible"
      width="70%"
      center>
      <el-table
        :data="gradeList"
        header-row-class-name="header-row"
        border
        style="text-align:center;"
        v-loading="loading"
    >
        <el-table-column align="center" :label="$t('UserCenter.Grade')"  prop="levelName" width="160"></el-table-column>
        <el-table-column align="center" :label="$t('UserCenter.Username')" prop="salemanName" :min-width="160"></el-table-column>
        <el-table-column align="center" label="openID/商户号" prop="openID" :min-width="200" show-overflow-tooltip>
            <template slot-scope="scope">
                <div>{{scope.row.openId}} / {{scope.row.merchantNumber}}</div>
            </template>
        </el-table-column>
        <el-table-column align="center" label="分账金额" prop="actualSubAccountAmount" :min-width="160">
          <template slot-scope="scope">
            <div>￥{{scope.row.actualSubAccountAmount}}</div>
          </template>
        </el-table-column>
        <el-table-column align="center" :label="$t('OrderSuccess.Status')" prop="status" width="160">
          <template slot-scope="scope">
              <div>{{getStatus(scope.row.status)}}</div>
          </template>
        </el-table-column>
        <el-table-column align="center" :label="$t('UserCenter.CauseOfFailure')"  prop="failReason" :min-width="160">
            <template slot-scope="scope">
              <div>{{scope.row.failReason?scope.row.failReason:'--'}}</div>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
    <!----分账修改弹框----->
    <el-dialog
      title="分账详情"
      :visible.sync="EditDialogVisible"
      width="70%"
      center>
      <el-table
        :data="gradeList"
        header-row-class-name="header-row"
        border
        style="text-align:center;"
        v-loading="loading"
        :summary-method="getSummaries"
        show-summary
    >
        <el-table-column align="center" :label="$t('UserCenter.Grade')"  prop="levelName" width="160"></el-table-column>
        <el-table-column align="center" :label="$t('UserCenter.Username')" prop="salemanName" :min-width="160"></el-table-column>
        <el-table-column align="center" :label="$t('OrderSuccess.Status')" prop="status" width="160">
            <template slot-scope="scope">
                <div>{{getStatus(scope.row.status)}}</div>
            </template>
        </el-table-column>
        <el-table-column align="center" :label="$t('UserCenter.CauseOfFailure')"  prop="failReason" width="160">
           <template slot-scope="scope">
              <div>{{scope.row.failReason?scope.row.failReason:'--'}}</div>
          </template>
        </el-table-column>
        <el-table-column align="center" :label="$t('UserCenter.BeforeSubAccount')" prop="maxSubAccountAmount" :min-width="100">
            <template slot-scope="scope">
                <span>￥{{scope.row.maxSubAccountAmount}}</span>
            </template>
        </el-table-column>
        <el-table-column align="center" :label="$t('UserCenter.afterSubAccount')" prop="actualSubAccountAmount" :min-width="160">
            <template slot-scope="scope">
                <el-input class="input-text" v-if="scope.row.status===1" v-model="scope.row.actualSubAccountAmount" placeholder="请输入">--aaa</el-input>
                <span v-else-if="scope.row.status===3">￥{{scope.row.actualSubAccountAmount}}</span>
                <span v-else-if="scope.row.status===0">--</span>
            </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleSubmit()">确定并发起分账</el-button>
      </span>
    </el-dialog>
    <!----导出弹框----->
    <!-- <el-dialog
      title="导出表格"
      :visible.sync="ExportDialogVisible"
      width="40%"
      center>
        <div class="raido-group">
          <el-radio v-model="radioType" label="1">
            <i class="icon screen"></i>
            <div class="text">筛选后信息</div>
          </el-radio>
          <el-radio v-model="radioType" label="2">
            <i class="icon all"></i>
            <div class="text">全部信息</div>
          </el-radio>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleExport">确 定</el-button>
        <el-button @click="ExportDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog> -->
  </div>
</template>

<script>
import Header from '@/components/Header.vue'
import Left from '@/components/Left.vue'
import { subAccountAgain,orderSubAccountList,orderSubAccountDetail,updateActualSubAccountAmount} from '@/apiService/api'
export default {
  components: {
    Header,
    Left
  },
  data () {
    return {
      action:this.locationUrl_L + 'flexible/v1/web/user/shop/import',
      loading: false,
      loading2: '',
      userState: 2,
      appId: null,
      platform:'',
      pageInfo: {
        status: undefined,
        orderNo: undefined,
        page: 1,
        size: 10
      },
      tableData: [],
      gradeList: [],
      totalCount: 0,
      detailDialogVisible: false,
      EditDialogVisible: false,
      ExportDialogVisible: false,
      radioType: '2',
      isSelect: false,
      filters: {
        column: {
          startTime: "",
          endDate: "",
        },
      },
      pickerStarDate: {
        disabledDate: (time) => {
          let beginDateVal = this.endTime;
          if (beginDateVal) {
            return time.getTime() > beginDateVal;
          }
        },
      },
      pickerEndDate: {
        disabledDate: (time) => {
          let beginDateVal = this.filters.column.startTime;
          if (beginDateVal) {
            return time.getTime() < beginDateVal;
          }
        },
      }
    }
  },
  mounted () {
    this._initData()
  },
  methods: {
    // 获取数据列表
    _initData () {
      let params={
        orderNo:this.pageInfo.orderNo,  // 订单号
        status:this.pageInfo.status,  //状态 0、关闭 1、待分账 2、部分分账 3、全部分账 4、分账失败
        page:this.pageInfo.page,
        size:this.pageInfo.size,
        startTime: this.pageInfo.startTime,
        endTime: this.pageInfo.endTime
      }
      orderSubAccountList(params).then(res=>{
         if(res.success){
            this.tableData = res.data.list
            this.totalCount = res.data.total
         }
      })
    },
    // 分账详情
    handleDetail(id, type) {
      orderSubAccountDetail({id: id}).then(res => {
        if (res.success) {
          this.gradeList = res.data
          type === 1 ? this.detailDialogVisible = true : this.EditDialogVisible = true
        } else {
          this.$message.error(res.errMessage)
        }
      })
    },
    // 发起分账并修改
    handleSubmit() {
      let info = []
      try {
        this.gradeList.forEach(item => {
          if (item.status === 4 && item.actualSubAccountAmount === '') {
            info = []
            this.$message.warning('请填写修改后的分账金额')
            throw new Error()
          }
          console.log(parseInt(item.actualSubAccountAmount), parseInt(item.maxSubAccountAmount), parseInt(item.actualSubAccountAmount) > parseInt(item.maxSubAccountAmount));
          if (item.status === 4 && parseInt(item.actualSubAccountAmount) > parseInt(item.maxSubAccountAmount)) {
            info = []
            this.$message.warning('修改后的分账金额不能大于原分账金额！')
            throw new Error()
          }
          info.push({
            id: item.id,
            actualSubAccountAmount: item.actualSubAccountAmount
          })
        })
        // 提交
        updateActualSubAccountAmount({billAmountCmdList: info}).then(res => {
          if (res.success) {
            this.$message.success('操作成功！');
            this.EditDialogVisible=false;
            this._initData()
          } else {
            this.$message.error(res.errMessage)
          }
        })
      } catch (error) {
        console.log(error);
      }
     
     
    },
    // 获取状态
    getStatus (val) {
      if (Number(val) === 0) {
        return '关闭'
      } else if (Number(val) === 1) {
        return '待分账'
      } else if (Number(val) === 2) {
        return '部分分账'
      } else if (Number(val) === 3) {
        return '全部分账'
      } else if (Number(val) === 4) {
        return '分账失败'
      } else {
        return '--'
      }
    },
    // 获取相反状态
    getfStatus (val) {
      if (Number(val) === 0) {
        return '开启'
      } else if (Number(val) === 1) {
        return '停用'
      }
    },
     // 重新分账
    onChange (id) {
      subAccountAgain({id:id}).then(res => {
        if (res.success) {
          this.$message.success('更新成功！')
          this._initData()
        } else {
          this.$message.error(res.errMessage)
        }
      })
    },
    // 更换状态搜索
    handleSelect (val) {
      this.pageInfo.openFlag = val === '' ? null : val
      this._initData()
    },
    getSummaries(param) {
      const { columns, data } = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计';
          return;
        }
        const values = data.map(item => Number(item[column.property]));
        if (!values.every(value => isNaN(value))) {
         
          if (index === columns.length - 1) {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr);
              if (!isNaN(value)) {
                return prev + curr;
              } else {
                return prev;
              }
            }, 0);
            sums[index] += '';
          }
        } else {
          sums[index] = '';
        }
      });
        return sums;
    },
    // 搜索
    search () {
      this.pageInfo.page = 1
      this.pageInfo.startTime = this.filters.column.startTime
      this.pageInfo.endTime = this.filters.column.endTime
      this._initData()
    },
    handleClear() {
      this.pageInfo.orderNo = undefined
    },
    // 编辑
    onEdit (row) {
      this.$router.push({ name: 'StoreAdd', query: { id: row.id } })
    },
    // 导出分账记录
    handleExport () {
      this.pageInfo.size = this.totalCount
      orderSubAccountList(this.pageInfo).then(res=>{
         if(res.success){
            if (res.data.list.length>0) {
              res.data.list.forEach(item => {
                item.statusName = this.getStatus(item.status)
              })
              let downData = res.data.list
              import("../../assets/js/Export2Excel").then((excel) => {
              const tHeader = [
                "下单时间",
                "订单号",
                "门店名称",
                "实付金额",
                "总分账金额",
                "状态",
                "备注"
              ];
              const filterVal = [
                "createTime",
                "orderNo",
                "shopName",
                "payAmount",
                "maxSubAccountAmount",
                "statusName",
                "remark"
              ];
              const data = this.formatJson(filterVal, downData);
                excel.export_json_to_excel({
                  header: tHeader,
                  data,
                  filename: "分账记录列表",
                });
              });
            } else {
              this.$message({
                type: 'error',
                message: '暂无数据'
              })
            }
           
        } else {
          this.$messag.error('导出失败！')
        }
      })
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map((v) =>
        filterVal.map((j) => {
          if (j === "timestamp") {
            return "parseTime(v[j])";
          } else {
            return v[j];
          }
        })
      );
    },
    onSizeChange (val) {
      // 分页方法
      this.pageInfo.size = val
      this._initData()
    },
    onCurrentChange (val) {
      // 分页方法
      this.pageInfo.page = val
      this._initData()
    },
    parseTime(time, cFormat) {
      if (time === 0) {
        return '-'
      }
      const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}'
      let date
      if (typeof time === 'object') {
        date = time
      } else {
        if (('' + time).length === 10) time = parseInt(time) * 1000
        date = new Date(time)
      }
      const formatObj = {
        y: date.getFullYear(),
        m: date.getMonth() + 1,
        d: date.getDate(),
        h: date.getHours(),
        i: date.getMinutes(),
        s: date.getSeconds(),
        a: date.getDay()
      }
      const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
        let value = formatObj[key]
        if (key === 'a') return ['一', '二', '三', '四', '五', '六', '日'][value - 1]
        if (result.length > 0 && value < 10) {
          value = '0' + value
        }
        return value || 0
      })
      return time_str
    }
  },
  watch: {
    "pageInfo.time": {
      handler(val, oldVal) {
        if (val) {
          this.pageInfo.startTime = this.parseTime(val[0])
          this.pageInfo.endTime = this.parseTime(val[1])
        } else {
          this.pageInfo.startTime = undefined
          this.pageInfo.endTime = undefined
        }
        if (val !== oldVal) {
          this.pageInfo.page = 1
          this._initData()
        }
      },
      deep:true
    }
  }
}
</script>

<style scoped="scoped" lang='less'>
  @import url("../../assets/less/variable.less");
  .store-manage {
    width: 100%;
  }
  .store-main {
    width: 1200px;
    .store-right{
      width: 1030px;
      .content{
        padding: 24px 40px 0;
        border: 2px solid @bdLightColor;
        border-radius: 8px;
      }
      .store-right-title {
        padding-bottom: 10px;
        border-bottom: 1px solid @bdLightColor;
        font-size: 20px;
      }
      .top-box{
        margin-top:25px;
        .upload-excel{
          display: inline-block;
          .el-button--primary{
            background-color: #15BED6 !important;
            border: 1px solid #15BED6;
            padding: 7px 15px;
            font-size: 12px;
            border-color: #15BED6;
          }
          /deep/.el-upload-list{
            display:none;
          }
        }
      }
      .search-box{
        margin-top:20px;
        .el-input{
          display: inline-block;
          width:160px;
          margin-left:15px;
          /deep/.el-input__inner{
            height: 38px;
            line-height: 38px;
          }
        }
        .el-select{
          display: inline-block;
          width:120px;
          /deep/.el-input__inner{
            height: 38px;
            line-height: 38px;
          }
        }
        .el-date-editor{
          margin-left:15px;
          height: 38px;
          line-height: 38px;
          input{
            height: 32px;
            line-height: 32px;
          }
        }
        .el-button{
          height: 38px;
          vertical-align: middle;
        }
        .ml-15{
          margin-left:15px;
        }
        .handle-wrap {
          position: absolute;
          right: 0;
          bottom: 15px;
          font-size: 14px;
          color: @blue;
          line-height: 1;
          cursor: pointer;
          &:hover{
            opacity: 0.6;
          }
          .mini-export{
            color: @blue;
          }
          .iconfont {
            margin-right: 2px;
            color: @lighterGray;
          }
        }
      }
      .table-box {
        width: 100%;
        margin-top: 20px;
        margin-bottom: 40px;
        /deep/ .el-table {
          width: 100%;
          word-wrap: break-word;
          word-break: break-all;
          border-collapse: collapse;
          tr {
            &:hover {
              background-color: @lightGrayBg;
            }
            th {
              padding: 0;
              height: 32px;
              line-height: 32px;
              text-align: center;
              background-color: @bdLightColor;
              font-size: 12px;
              color: @gray;
              font-weight: normal;
            }
            td {
              height: 53px;
              text-align: center;
              font-size: 12px;
              color: @lightBlack;
              border: none;
              border-top: 1px dashed @bdLighterColor;
              .log {
                margin: 0 10px;
                color: @blue;
                &.disabled{
                  pointer-events: none;
                  color: #3333;
                }
                &:hover {
                  opacity: 0.6;
                }
              }
              .cell{
                canvas{
                  width:40px;
                  height:40px;
                  vertical-align: middle;
                }
              }
            }
          }
        }
      }
    }
  }
  /deep/.el-dialog__wrapper{
    .el-dialog__header{
      padding-top:30px;
    }
    .el-dialog__body{
      margin:10px auto 65px;
      width:80%;
      text-align: center;
      .raido-group{
        .el-radio{
          display: inline-block;
          margin:0 30px;
          text-align: center;
          .el-radio__input{
            display: none;
          }
          .el-radio__label{
            padding: 0;
            .icon{
              display: inline-block;
              width:64px;
              height:64px;
              background-size: 64px 64px;
                background-repeat: no-repeat;
              &.screen{
                background-image: url('../../assets/images/store/icon_screen@2x.png');
              }
              &.all{
                background-image: url('../../assets/images/store/icon_all@2x.png');
              }
            }
            .text{
              margin-top:10px;
              color:#555555
            }
          }
          &.is-checked{
            .el-radio__label{
              .screen{
                background-image: url('../../assets/images/store/icon_screen_pre@2x.png');
              }
              .all{
                background-image: url('../../assets/images/store/icon_all_pre@2x.png');
              }
            }
            .text{
              color:#0DB8CF
            }
          }
        }
      }
      .el-table__body-wrapper{
        .el-table__body{
          td{
            input{
               text-align: center;
            }
          }
        }
      }
    }
    .el-dialog__footer{
      padding-bottom:40px;
      .el-button{
        margin:0 30px;
        &.el-button--primary{
          background-color: #0DB8CF;
          border-color: #0DB8CF;
        }
      }
    }
  }
</style>
