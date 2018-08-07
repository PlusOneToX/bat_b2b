<template>
  <div class="service-price">
    <header>
      <h4>服务费专区列表</h4>
      <el-button class="mini-add-btn btn-home" @click="handleAdd">添加新的服务费活动</el-button>
      <el-button class="mini-add-btn btn-home el-icon-upload2" @click="batchImport">批量导入</el-button>
    </header>
    <div v-loading="loading" class="service-wrapper">
      <div class="service-header">
        <div class="Fleft">
            <el-select
              size="mini"
              v-model="pageInfo.status"
              placeholder="活动状态"
              style="width: 100px;"
              @change="Csearch()"
              clearable
            >
              <el-option label="未开始" :value="0"></el-option>
              <el-option label="进行中" :value="1"></el-option>
              <el-option label="已过期" :value="2"></el-option>
              <el-option label="暂停中" :value="3"></el-option>
            </el-select>
          </div>
          <div class="Fsearch">
            <button class="mini-search-btn box-btn" @click="Csearch()">搜索</button>
            <el-input
              v-model.trim="pageInfo.content"
              size="mini"
              style="width:320px;"
              clearable
              @change="contentChange"
              @keyup.enter.native="Csearch()"
              placeholder="请输入关联分销商名称/指定分销商名称/活动名称"
              class="box-input"
            ></el-input>
          </div>
      </div>
      <!----服务费活动数据列表----->
      <el-table :data="tableData" header-row-class-name="header-row" border style="text-align:center;"  >
        <el-table-column align="center" label="ID" prop="id" width="60"></el-table-column>
        <el-table-column align="center" label="活动名称" prop="name" show-overflow-tooltip>
        </el-table-column>
        <el-table-column align="center" label="关联分销商" prop="relationDistributorName" show-overflow-tooltip>
        </el-table-column>
        <el-table-column align="center" label="指定分销商" prop="appointDistributorName">
        </el-table-column>
        <!-- <el-table-column align="center" label="已购数量（货品）">
           <template slot-scope="scope">
            <div>{{getItemCount(scope.row.activityFeeTradeTotal.itemCount)}}</div>
          </template>
        </el-table-column> -->
        <!-- <el-table-column align="center" label="服务费单价" prop="name" show-overflow-tooltip>
        </el-table-column> -->
        <el-table-column align="center" label="应缴总次数" show-overflow-tooltip width="110">
          <template slot-scope="scope">
            <div v-if="scope.row.edit === 1">{{getBoxNum(scope.row.needChargeNum)}}</div>
            <el-input v-else size="mini" v-model="scope.row.needChargeNum" @input="scope.row.needChargeNum=/^\d+\.?\d{0,0}$/.test(scope.row.needChargeNum)||scope.row.needChargeNum == '' ? scope.row.needChargeNum:Number(scope.row.needChargeNum.toString().match(/^\d+(?:\.\d{0,0})?/))" ></el-input>
          </template>
        </el-table-column>
        <el-table-column align="center" label="原缴次数" show-overflow-tooltip width="90">
          <template slot-scope="scope">
            <div>{{getBoxNum(scope.row.oldServiceBoxNumAll)}}</div>
          </template>
        </el-table-column>
        <el-table-column align="center" label="原缴费用总计" show-overflow-tooltip width="120">
          <template slot-scope="scope">
            <div>{{getBoxNum(scope.row.oldFeeTotalAll)}}</div>
          </template>
        </el-table-column>
        <el-table-column align="center" label="矫正次数" show-overflow-tooltip width="90">
          <template slot-scope="scope">
            <div v-if="scope.row.edit === 1">{{getBoxNum(scope.row.correctCount)}}</div>
            <el-input v-else size="mini" v-model="scope.row.correctCount" @input="scope.row.correctCount=/^[+-]?\d+\.?\d{0,0}$/.test(scope.row.correctCount)||scope.row.correctCount == '' ? scope.row.correctCount:Number(scope.row.correctCount.toString().match(/^[+-]?\d+(?:\.\d{0,0})?/))" ></el-input>
          </template>
        </el-table-column>
        <el-table-column align="center" label="矫正费用金额" show-overflow-tooltip width="120">
          <template slot-scope="scope">
            <div v-if="scope.row.edit === 1">{{getFeeTotal(scope.row.correctAmount)}}</div>
            <el-input v-else size="mini" v-model="scope.row.correctAmount" @input="scope.row.correctAmount=/^[+-]?\d+\.?\d{0,2}$/.test(scope.row.correctAmount)||scope.row.correctAmount == '' ? scope.row.correctAmount:Number(scope.row.correctAmount.toString().match(/^[+-]?\d+(?:\.\d{0,2})?/))" ></el-input>
          </template>
        </el-table-column>
        <el-table-column align="center" label="矫正后次数" show-overflow-tooltip width="110">
          <template slot-scope="scope">
            <div>{{getBoxNum(scope.row.activityFeeTradeTotal.serviceBoxNumAll)}}</div>
          </template>
        </el-table-column>
        <el-table-column align="center" label="矫正后费用总计" show-overflow-tooltip width="130">
          <template slot-scope="scope">
            <div>{{getFeeTotal(scope.row.activityFeeTradeTotal.feeTotalAll)}}</div>
          </template>
        </el-table-column>
        <el-table-column align="center" label="状态" prop="status" show-overflow-tooltip width="80">
          <template slot-scope="scope">
            <div>{{getStatus(scope.row.status)}}</div>
          </template>
        </el-table-column>
        <el-table-column label="操作" :width="380" align="center">
          <template slot-scope="scope">
            <el-button class="mini-browse-btn" @click="handleEdit(scope.row)">{{scope.row.edit === 1 ?'编辑':'保存'}}</el-button>
            <el-button v-if="scope.row.status===3||scope.row.status===1" class="mini-tableadd-btn" @click="handleMove(scope.row.id, 1)">上移</el-button>
            <el-button v-if="scope.row.status===3||scope.row.status===1" class="mini-freeze-btn" @click="handleMove(scope.row.id, 2)">下移</el-button>
            <button class="mini-search-btn" @click="handleSelect(scope.row.id)">查看</button>
            <el-button v-if="scope.row.status===3||scope.row.status===1" class="mini-freeze-btn" @click="handleStatu(scope.row.id, scope.row.status)">{{statuBtn(scope.row.status)}}</el-button>
            <el-button class="mini-delete-btn" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
    </div>
  </div>
</template>

<script type="text/javascript">
import page from "@/components/pagination";
import {
  getServiceFeeList,
  putServiceFeeList,
  editServiceFee
} from "@/views/marketingPromotion/serviceFee/serviceData";
export default {
  data () {
    return {
      loading:false,
      pageInfo: {
        page: 1,
        count: 10,
        status: "",
        content: ""
      },
      total: 0,
      tableData: [],
    }
  },
  components: {
    page
  },
  mounted() {
    this.getServiceList()
  },
  methods: {
    editBtn(row){
      return row.edit === undefined ? '编辑':row.edit
    },
    statuBtn (val) {
      return val===3?'启用':'禁用'
    },
    // 已购数量（货品）
    getItemCount (val) {
      return !val ? 0 : val
    },
    // 已购数量（片）
    getBoxNum (val) {
      return !val ? 0 : val
    },
    // 服务费总价
    getFeeTotal (val) {
      return !val ? 0 : val
    },
    // 状态
    getStatus (type) {
      switch(type) {
        case 0 :
          return "未开始";
          break;
        case 1:
          return '进行中';
          break;
        case 2 :
          return "已过期";
          break;
        case 3:
          return '暂停中';
          break;
        default:
          return '-'
      }
    },
    getServiceList () {
      getServiceFeeList(this, this.pageInfo).then(res => {
        if (res.code === 0) {
          if(res.pageInfo.list !== undefined && res.pageInfo.list !== null && res.pageInfo.list.length>0){
            res.pageInfo.list.forEach(element => {
              element.edit = 1
              element.activityFeeTradeTotal.needChargeNum = !element.activityFeeTradeTotal.needChargeNum ? 0 : element.activityFeeTradeTotal.needChargeNum
              element.activityFeeTradeTotal.serviceBoxNumAll = !element.activityFeeTradeTotal.serviceBoxNumAll ? 0 : element.activityFeeTradeTotal.serviceBoxNumAll
              element.activityFeeTradeTotal.feeTotalAll = !element.activityFeeTradeTotal.feeTotalAll ? 0 : element.activityFeeTradeTotal.feeTotalAll
              element.correctCount = !element.correctCount ? 0 : element.correctCount
              element.correctAmount = !element.correctAmount ? 0 : element.correctAmount
              element.oldServiceBoxNumAll = (element.activityFeeTradeTotal.serviceBoxNumAll - element.correctCount)>0?(element.activityFeeTradeTotal.serviceBoxNumAll - element.correctCount):0
              element.oldFeeTotalAll = (element.activityFeeTradeTotal.feeTotalAll - element.correctAmount)>0?(element.activityFeeTradeTotal.feeTotalAll - element.correctAmount):0
            });
          }
          this.tableData = res.pageInfo.list
          this.total = res.pageInfo.total
        }
      }).catch(err => {
        console.log(err);
      })
    },
    handleAdd () {
      this.$router.push({ name: 'serviceAdd' })
    },
    batchImport () {
      this.$router.push({ name: 'serviceImport' })
    },
     sizeChange(size) {
      this.pageInfo.count = size;
      this.pageInfo.page = 1;
      this.getServiceList();
    },
    currentChange(page) {
      this.pageInfo.page = page;
      this.getServiceList();
    },
    // 上移/下移
    handleMove (id, type) {
      putServiceFeeList(this, {
        id: id,
        move: type
      }).then(res => {
        if (res.code === 0) {
          this.$message({
            type: "success",
            message: "操作成功",
          });
          this.getServiceList();
        }
      })
    },
    // 查看
    handleSelect (id) {
      this.$router.push({ name: 'serviceAdd', query: {sid: id} })
    },
    handleEdit(row){
      if(row.edit === undefined || row.edit === 1){
        row.edit = 2
      }else {
        editServiceFee(this, {id:row.id,needChargeNum:row.needChargeNum,correctAmount:row.correctAmount,correctCount:row.correctCount}).then((res) => {
          if (Number(res.code) === 0) {
            this.$message({
              type: "success",
              message: "编辑成功",
            });
            this.getServiceList()
            row.edit = 1
          }
        });
      }
    },
    // 修改状态
    handleStatu (id, val) {
      let status = val === 3 ? 1 : 3
      putServiceFeeList(this, {
        id: id,
        status: status
      }).then(res => {
        if (res.code === 0) {
           this.$message({
            type: "success",
            message: "修改成功",
          });
          this.getServiceList();
        }
      }).catch(err => {
        console.log(err);
      })
    },
    // 删除
    handleDelete (id) {
      this.$confirm("是否确定删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true
      }).then(() => {
        putServiceFeeList(this, {
          id: id,
          isDelete: 1
        }).then(res => {
          if (res.code === 0) {
            this.$message({
              type: "success",
              message: "删除成功",
            });
            this.getServiceList();
          }
        }).catch(err => {
          console.log(err);
        })
      });
    },
    Csearch () {
      this.getServiceList()
    },
    contentChange () {}
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .service-price{
    height: 100%;
    header {
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
      h4 {
        display: inline-block;
        font-weight: 350;
        font-size: 16px;
        margin: 0 20px;
      }
      .btn-home{
        float: right;
        padding: 5px;
        margin-top: 8px;
        margin-right: 8px;
        margin-left: 0;
        font-size: 12px;
      }
    }
    .service-wrapper{
      .service-header {
        margin: 10px;
        display: flex !important;
        justify-content: space-between;
        align-items: center !important;
        .Fleft {
          overflow: hidden;
          float: left;
        }
        .Fsearch {
          overflow: hidden;
          float: right;
          .box-input {
            width: 215px;
            float: right;
          }
          .box-btn {
            float: right;
            margin-left: 5px;
          }
        }
      }
    }
  }
</style>