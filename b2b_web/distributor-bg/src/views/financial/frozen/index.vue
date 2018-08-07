<template>
  <main class="frozen">
    <header>
      <span>冻结列表</span>
      <el-button class="mini-back-btn btn-home" icon="el-icon-plus" @click="dialogVisShow()">
				添加冻结
			</el-button>
    </header>
    <section class="frozen-wrapper">
      <div class="frozen-body">
        <div class="Fheader">
          <el-select size="mini" v-model="pageInfo.businessType" clearable placeholder="冻结类型" style="width:120px;">
            <el-option label="提现冻结" :value="1"></el-option>
            <el-option label="其他冻结" :value="2"></el-option>
          </el-select>
        </div>
        <div class="f-search">
          <el-select
            class="content_select"
            placeholder="选择类型"
            size="mini"
            style="width:140px;"
            v-model="pageInfo.contentType"
            clearable
          >
            <el-option
              v-for="item in contentTypes"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
          <el-input v-model.trim="content" @keyup.enter.native="onSearch()" placeholder="分销商用户名" size="mini" class="box-search"></el-input>
          <button class="mini-search-btn box-btn" @click="onSearch()">搜索</button>
        </div>
      </div>
      <el-table border :data="tableData" max-height="700" style="width:100%" header-row-class-name="header-row" class="tableCenter" v-loading="loading">
        <el-table-column align="center" type="index" fixed> </el-table-column>
        <el-table-column align="center" prop="distributorName" label="分销商用户名" :min-width="120"></el-table-column>
        <el-table-column align="center" prop="businessId" label="业务单号" :min-width="120">
          <template slot-scope="scope">
            <div v-if="scope.row.businessId">{{scope.row.businessId}}</div>
            <div v-else>--</div>
          </template>
        </el-table-column>
        <el-table-column align="center" label="冻结金额" :min-width="120">
           <template slot-scope="scope">
            <i class="asmd">￥:&nbsp;</i>
            {{ scope.row.freezingAmount| NumFormat}}
          </template>
        </el-table-column>
        <!-- <el-table-column align="center" prop="freezingAmount" label="已解冻金额"></el-table-column> -->
        <el-table-column align="center" prop="createTime" label="冻结时间" :formatter="timeFormatter" :min-width="160"></el-table-column>
        <el-table-column align="center" prop="businessType" label="冻结类型" :formatter="formatStatus"  :min-width="160"></el-table-column>
        <el-table-column align="center" prop="remark" label="备注"  :min-width="120"></el-table-column>
        <el-table-column align="center" label="操作"  :min-width="120">
          <template slot-scope="scope">
            <el-button class="mini-search-btn" @click="disFreeze(scope.row)">解冻</el-button>
          </template>
        </el-table-column>
      </el-table>
      <page :total="total" :page="pageInfo.page" @sizeChange="sizeChange" @currentChange="currentChange"></page>
    </section>
    <!-- <el-dialog :modal-append-to-body="false" center title="选择用户" :visible.sync="dialogVisible" width="800px" style="padding-top: 0">
      <frozenWindow @confirmedFrozen="updateMainData"></frozenWindow>
    </el-dialog> -->

    <!-- 添加冻结 -->
    <el-dialog :visible="dialogVisible" :before-close="closeDialogVisible" title="选择用户" width="800px" center>
      <!-- <h4 class="for_dia_one">选择用户</h4> -->
      <div class="for_dia_header">
        <el-select size="mini" v-model="disType" placeholder="分销商类型" style="width:120px;">
          <el-option label="一级分销商" :value="1"></el-option>
          <el-option label="多级分销商" :value="2"></el-option>
        </el-select>
        <button class="mini-search-btn box-btn" @click.prevent="onSearchDistributors()" style="float:right;">搜索</button>
        <el-input placeholder="分销商用户名" size="mini" v-model.trim="forPageInfo.content" class="box-search" style="width: 140px;float:right;"></el-input>
        <el-select
          class="content_select"
          placeholder="选择类型"
          size="mini"
          style="width:140px;float:right;"
          v-model="forPageInfo.contentType"
          clearable
        >
          <el-option
            v-for="item in forcontentTypes"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </div>
      <el-table :data="frozenTableData" border max-height="600" @selection-change="onSelectionChange" v-loading="loading2">
        <el-table-column align="center" type="selection"> </el-table-column>
        <el-table-column align="center" prop="name" label="分销商用户名"></el-table-column>
        <el-table-column align="center" prop="id" label="B2B编码"></el-table-column>
      </el-table>
      <page :total="total1" :page="forPageInfo.page" @sizeChange="sizeChange1" @currentChange="currentChange1"></page>

      <div style="text-align: center;">
        <el-button class="mini-search-btn" @click="dialogShow()">确定</el-button>
      </div>
    </el-dialog>

    <!-- 确定添加冻结，设置金额 -->
    <el-dialog :visible="amountVisible" :before-close="closeAmountVisiblee" title="设置冻结金额" center>
      <el-table :data="amountData" border max-height="700" @selection-change="onSelectionChange">
        <el-table-column align="center" prop="name" label="用户"></el-table-column>
        <el-table-column align="center" prop="accountBalance" label="账号余额"></el-table-column>
        <el-table-column align="center" prop="accountAvailable" label="可用余额"></el-table-column>
        <el-table-column align="center" prop="freezingAmount" label="已冻结金额"></el-table-column>
        <el-table-column align="center" label="冻结金额">
          <template slot-scope="scope">
            <el-input v-model="scope.row.frozenAmount" class="box-search" 
            @input="scope.row.frozenAmount=/^\d+\.?\d{0,2}$/.test(scope.row.frozenAmount)||scope.row.frozenAmount == '' ? scope.row.frozenAmount:Number(scope.row.frozenAmount.toString().match(/^\d+(?:\.\d{0,1})?/))"  
            type="number" min="0" step="0.01" size='mini'></el-input>
          </template>
        </el-table-column>
        <el-table-column align="center" label="备注">
          <template slot-scope="scope">
            <el-input v-model="scope.row.remark" size="mini" class="box-search"></el-input>
          </template>
        </el-table-column>
      </el-table>

      <div style="text-align:center;padding-top: 20px">
        <el-button class="mini-search-btn" @click="onConfirm()" >确定</el-button>
      </div>
    </el-dialog>
  </main>
</template>

<script>
import eventBus from '@/views/order/eventBus'
import page from "@/components/pagination/index"
import {parseTime} from '@/utils/index'
import pagination from '@/components/pagination/index'
import {getFrozenList, getFrozenListCount, disFreeze} from '@/views/financial/financialData'
import frozenWindow from '@/views/financial/frozen/frozenWindow'
import {confirmCreator} from '@/views/order/orderUtils'
import {getDistributorsToFroze, getDistributorsToFrozeCount} from '@/views/financial/financialData'; // 冻结用户dialog列表数据
import { freezeCash, getPrechargesByIds, addFreezings} from '@/views/financial/financialData'
export default {
  name: 'frozen',
  activated(){
    eventBus.$on('addFreezingsShow',payLoad => {
      this.dialogVisible = true
      this.updateMainData();
    })
    this.updateMainData();
  },
  components: { pagination, frozenWindow, page } ,
  data(){
    return {
      loading: false,
      loading2: false,
      content: '',
      dialogVisible: false,
      amountVisible: false,
      tableData: [],
      frozenTableData: [],
      amountData: [],
      chosenItems: [], // 勾选的项目
      pageInfo: {
        page: 1,
        size: 10,
        userId: 10000,
        businessType: undefined,
        contentType: undefined,
        content: undefined
      },
      forPageInfo: {
        page: 1,
        size: 10,
        profileStatus:2,
        freezeStatus: 1,
        contentType: undefined,
        content: undefined
      },
      total: 1,
      total1: 1,
      foreeze: {
        frozenAmount: '',
        remark: ''
      },
      loading3: '',
      contentTypes: [
        { value: 1, label: '分销商用户名' }
      ],
      forcontentTypes: [
        { value: 1, label: '分销商用户名' },
        { value: 2, label: '客户名称' },
        { value: 3, label: '分销商ID' },
        { value: 5, label: '手机号' },
      ],
      disType: 1
    }
  },
  methods:{
    // ======== 操作 ========
    // TODO
    dialogShow() {
      if(this.chosenItems == '') {
        this.$message.warning({
          message: "请选择要冻结的分销商",
          duration: 3 * 1000,
        });
      }else {
        this.amountVisible = true
        this.amoData() //..请求冻结人的可冻结数据
      }
    },
    closeDialogVisible() {
      this.dialogVisible = false
    },
    dialogVisShow() {
      this.dialogVisible = true
      // eventBus.$emit('frozenUserData');
      this.upData()
    },
    upData(){  // 选择用户主要数据
       this.loading2 = true;
      if (this.disType === 1) {
         // 一级分销商
        this.$http.getDistributorFList(this, this.forPageInfo).then(res => {
          if (res.success) {
            this.frozenTableData = res.data.list
            this.total1 = res.data.total
          }
          res.success ? this.loading2 = false : this.looking2 = false
        })
      } else {
        // 多级分销商 
        this.forPageInfo.profileStatus = undefined
        this.forPageInfo.freezeStatus = undefined
        this.$http.getDistributorNList(this, this.forPageInfo).then(res => {
          if (res.success) {
            this.frozenTableData = res.data.list
            this.total1 = res.data.total
          }
           res.success ? this.loading2 = false : this.looking2 = false
        })
      }
    },
    sizeChange1(size){  // 分页
      this.forPageInfo.size = size
      this.forPageInfo.page = 1
      this.upData();
    },

    currentChange1(page){ // 分页
      this.forPageInfo.page = page
      this.upData();
    },
    // TODO
    closeAmountVisiblee() {
      this.amountVisible = false
    },
    amoData() { // 确定后的预存款余额详情
      if(this.chosenItems && this.chosenItems[0]){
        this.amountData = JSON.parse(JSON.stringify(this.chosenItems));
        this.amountData.forEach(obj => { // 初始化冻结金额和备注
          this.$set(obj, 'frozenAmount', 0);
          this.$set(obj, 'remark', '');
        });
      }
      const ids = []
      this.chosenItems.map(item => item.id)
        .reduce((sum, cur, index) => {
          // return sum + (index == 0? '' : ',') + cur
          ids.push(cur)
        }, '');

      if(ids.length === 0) return;

      // return getPrechargesByIds(this, {ids}).then(res => { // 得到与tableData一一对应的accounts数组
      return this.$http.freezingBalance(this, {ids: ids}).then(res => {
        return this.amountData.map(data => data.id).map(id => {
          if (res.data.length > 0) {
            for(let i = 0, len = res.data.length; i < len; i++){
              if(res.data[i].distributorId == id){
                return res.data[i]
              }
            }
          }
        });
      }).then(accounts => { // 拼装数据
        this.amountData.forEach((obj, index) => {
          if(accounts[index]){
            this.$set(obj, 'accountBalance', accounts[index].accountBalance);
            this.$set(obj, 'accountAvailable', accounts[index].accountAvailable);
            this.$set(obj, 'freezingAmount', accounts[index].freezingAmount);
          }
        })
      })
    },
    onConfirm(){ // 金额确定冻结操作（发出冻结请求）
      // 验证数据
      for(let i=0; i < this.amountData.length; i++) {
        if (this.amountData[i].accountAvailable === undefined || this.amountData[i].accountAvailable === '' || 
        this.amountData[i].accountAvailable === undefined || this.amountData[i].accountAvailable === 0) {
          this.$message.error('无可用余额可冻结！')
          return
        } else if (this.amountData[i].accountAvailable < this.amountData[i].frozenAmount) {
          this.$message.error('冻结金额大于可用余额，请重新填写！')
          return
        }
      }
      const freezings = this.amountData.map(distributor => {
        return {
          distributorId: distributor.id,
          distributorName: distributor.name,
          businessType: 2, // 其它冻结
          freezingAmount: distributor.frozenAmount,
          remark: distributor.remark
        }
      });
      
      this.$confirm('确定要冻结该用户，是否继续？','提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(_ => {
        this.loading3 = this.$loading({
          lock: true,
          text: '拼命冻结中，请稍等...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        this.$http.batchFreezing(this, freezings).then(res => {
        // addFreezings(this, {freezings: freezings}).then(res => {
          if(res.success) {
            this.$message.success({
              message: '操作成功',
              duration: 3 * 1000,
            })
            this.loading3.close();
            this.amountVisible = false
            this.upData()
            eventBus.$emit('addFreezingsShow');
          }else {
            this.loading3.close()
          }
        })
      })
    },
    // TODO
    
    onSearch() { // 搜索操作
      this.pageInfo.content = this.content;
      this.updateMainData()
    },

    onSearchDistributors(){
      this.forPageInfo.page = 1
      this.upData()
    },
    
    disFreeze(row){ // 解冻操作
      this.$confirm('此操作将进行解冻该用户，是否继续？','提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(_ => {
        // disFreeze(this, {id: row.id}).then(res => {
        this.$http.freezingThaw(this, {id: row.id}).then(res => { 
          if(res.success) {
            this.$message.success({
              message: '操作成功',
              duration: 3 * 1000,
            })
            this.updateMainData()
          }
        })
      })
    },
    // ======== 数据 ========
    updateMainData(){  // 冻结列表数据
      this.loading = true;
      this.$http.freezingList(this, this.pageInfo).then(res => {
        if (res.success) {
          this.tableData = res.data.list
          this.total = res.data.total
        }
         res.success ? this.loading = false : this.loading = false
      })
    },
    sizeChange(size){  // 分页
      this.pageInfo.size = size;
      this.updateMainData();
    },
    currentChange(page){ // 分页
      this.pageInfo.page = page
      this.updateMainData();
    },
    onSelectionChange(val){  // 上个dialog传下来的dialog选中的用户的值
      this.chosenItems = val;
    },
    // ======== 转换 ========
    timeFormatter(row, column, cellValue){ // 时间格式化
      return parseTime(cellValue)
    },
    formatStatus(row, col, val) { // 上架状态
      switch(val) {
        case 1:
          return "提现";
          break;
        case 2:
          return "其他";
          break;
        default: 
          return "-"
      }
    },
  },
  watch: {
    'pageInfo.businessType': {
       handler () {
        this.updateMainData()
      },
      deep: true
    },
    disType: {
      handler () {
        this.forPageInfo.page = 1
        this.upData()
      },
      deep: true
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .frozen{
    height: 100%;
    header{
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
      span{
        margin-left: 30px;
      }
      .btn-home{
        float: right;
        padding:5px;
        margin-top:7px;
        margin-right:8px;
      }
    }
    .frozen-wrapper {
      .frozen-body{
        background-color: white;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 10px;
        .f-search {
          overflow: hidden;
          .box-search {
            width: 220px;
          }
          .box-btn {
            float: right;
            margin-left: 5px;
          }
        }
      }
    }
    .for_dia_one {
      text-align: center;
      font-size: 18px;
      margin: 0;
      color: #303133;
      font-weight: inherit;
    }
    .for_dia_two {
      text-align: center;
      font-size: 16px;
      margin: 0;
      color: #303133;
    }
    .for_dia_header {
      display: inline-block;
      width:100%;
      // display: flex;
      // justify-content: flex-end;
      // align-items: center;
      margin: 0 0 10px 0;
      .box-btn {
        margin-left: 10px;
      }
    }
    div.el-tabs__nav{
      margin-left: 30px;
    }
  }
</style>
