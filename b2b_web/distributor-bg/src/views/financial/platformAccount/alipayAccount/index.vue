<template>
  <div class="alipayAccount-list">
    <header>
      <h4>支付宝支付账户</h4>
      <el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="handleAdd()"> 添加支付宝账户 </el-button>
    </header>
    <div class="alipayAccount-header">
      <div class="header-left">
        <el-select size="mini" clearable placeholder="所属组织" v-model="pageInfo.organizationId" style="width:220px">
           <el-option
            v-for="item in organizationList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </div>
      <div class="alipayAccount-block">
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
        <el-input class="box-input" size="mini" clearable @change="contentChange" placeholder="请输入关键词搜索" v-model.trim="pageInfo.content"></el-input>
        <button class="mini-search-btn box-btn" size="mini" @click.prevent="filter()">搜索</button>
      </div>
    </div>
    <el-table :data="tableData" border style="width:100%"  v-loading="loading" header-row-class-name="header-row" class="tableCenter">
      <el-table-column align="center" label="所属组织" prop="organizationName" show-overflow-tooltip></el-table-column>
      <el-table-column align="center" label="收款账户名称" prop="accountName" show-overflow-tooltip></el-table-column>
      <el-table-column align="center" label="AppId" prop="appId" show-overflow-tooltip></el-table-column>
      <el-table-column align="center" label="状态" :width="100">
        <template slot-scope="scope">
          <span v-if="scope.row.openFlag == 1">是</span>
          <span v-else>否</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" fixed="right" width="200">
        <template slot-scope="scope">
          <el-button class="mini-search-btn" @click="handleEdit(scope.$index, scope.row)">查看</el-button>
          <el-button class="mini-search-btn" @click="showPay(scope.$index, scope.row)">测试支付</el-button>
          <el-button class="mini-delete-btn" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <page :page="pageInfo.page" :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
    <!---测试支付---->
    <el-dialog class="dialog-content-pay" :modal-append-to-body="false" :visible.sync="dialogVisible" title="测试支付">
        <div class="dialog-wrapper">
          <el-input class="box-input" size="mini" clearable placeholder="请输入支付金额，最低0.01" v-model.trim="formData.amount"></el-input>
          <button class="mini-search-btn box-btn" size="mini" @click.prevent="handlePay">确认</button>
          <div id="qrcode" ref="qrcode" class="img-pay"></div>
        </div>
    </el-dialog>
  </div>
</template>

<script>
import page from '@/components/pagination'
import QRCode from "qrcodejs2" 
export default {
  name: 'rolelist',
  data() {
    return {
      tableData: [],
      tableHeadData: [],
      total: 0,
      pageInfo: {
        page: 1,
        size: 10,
        organizationId: undefined,
        contentType: undefined,
        content: undefined
      },
      loading: false,
      content:'',
      contentTypes: [
        { value: 1, label: '收款账户名称' },
        { value: 2, label: 'AppId' },
      ],
      organizationList: [],
      formData: {
        amount: '0.01',
        businessType: 2, // 在线充值
        currencyCode: 'CNY',
        description: 'description',
        payChannel: 'alipay',
        payMethod: 'alipay_face_to_face',
        payerId: 10086,
        productId: 0,
        title:'title',
        tradeMode: 1
      },
      dialogVisible: false
    }
  },
  components: {
    page,
  },
  created() {
    // 获取所属组织列表
    this.getOrganizationList()
    this.init()
  },
  methods: {
    formatStatus(row, col, val) {
      // 是否公司账户
      switch (val) {
        case 0:
          return "否";
          break;
        case 1:
          return "是";
          break;
      }
    },
    contentChange(val){
      if(val === undefined || val === '' || val === null){
        this.init()
      }
    },
    getOrganizationList() {
      this.$http.getOrganizationList(this, {page:1, size:1000}).then(res => {
        if (res.success) {
           this.organizationList = res.data.list 
        }
      })
    },
    // ======== 操作 ========
    filter(){ // 搜索操作
      this.pageInfo.page = 1
      this.init()
    },
    // 测试支付弹框
    showPay (index, row) {
      this.formData.organizationId = row.organizationId
      this.dialogVisible = true
      this.formData.amount = 0.01
      if (document.getElementById("qrcode")) {
        document.getElementById("qrcode").innerHTML = ''
      }
    },
    // 测试支付
    handlePay () {
      if (!this.formData.amount) {
        this.$message.error('请输入支付金额')
        return
      } else if (isNaN(this.formData.amount)) {
        this.$message.error('请输入数字')
        this.formData.amount = 0.01
        return
      } else if (parseFloat(this.formData.amount) < 0.01) {
        this.$message.error('支付金额最低0.01, 请重新输入')
        this.formData.amount = 0.01
        return
      }
      this.$http.payTrade(this, this.formData).then(res => {
        if (res.success) {
          document.getElementById("qrcode").innerHTML = ""
          new QRCode("qrcode", {
            width: 300, // 二维码宽度，单位像素
            height: 300, // 二维码高度，单位像素
            text: res.data.alipayResp.codeUrl // 生成二维码的链接
          });
        }
      })
    },
    handleEdit(index, row) { // 查看操作
      this.$router.push({ name: 'editAlipay',query: {id:row.id} })
    },

    handleAdd() { // 添加操作
      this.$router.push({ name: 'editAlipay'});
    },
    
    handleDelete(index, row) { // 删除操作
      this.$confirm('此操作将永久删除该支付宝账户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        this.$http.delPlatAccountAlipay(this, { id: row.id }).then(res => {  
          if(res.success){
            this.$message({
              message: '删除成功',
              type: 'success',
              duration: 3 * 1000,
            })
            this.pageInfo.page = 1;
            this.init()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    
    // ======== 数据 ========
    init() { // 列表数据
      this.loading = true;
      this.$http.platAccountAlipayList(this, this.pageInfo).then(res => {    
        if(res.success){
          this.tableData = res.data.list
          this.total = res.data.total
        }
        this.loading = false
      })
    },

    sizeChange(size) {
      this.pageInfo.size = size
      this.pageInfo.page = 1
      this.init()
    },

    currentChange(page) {
      this.pageInfo.page = page
      this.init()
    },
  },
  watch: {
    'pageInfo.organizationId': {
      handler() {
        this.pageInfo.page = 1
        this.init()
      },
      deep: true
    }
  }
}

</script>
<style rel="stylesheet/scss" lang="scss" >
.alipayAccount-list {
  background-color: white;
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
  }
  h4 {
    display: inline-block;
    font-weight: 350;
    font-size: 16px;
    margin: 0 20px;
  } 
  .header {
    height: 50px;
    line-height: 50px;
    border-bottom: 1px solid #ccc;
    button {
      margin-left: 10px;
    }
  }
  .alipayAccount-header {
    width:100%;
    overflow: hidden;
    .header-left{
      display: inline-block;
      margin: 10px 10px;
      .el-select{
        width:160px;
      }
    }
    .alipayAccount-block {
      float: right;
      margin: 10px 10px;
      .box-input {
        width:300px;
      }
      .box-btn {
        position: relative;
        top: -1px;
      }
    }
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
.dialog-content-pay{
  .dialog-wrapper{
    padding: 20px;
    text-align: center;
    .box-input{
      display: inline-block;
      width:200px;
      margin-bottom:20px;
      margin-right:20px;
    }
    .img-pay{
      display: block;
      width:300px;
      height: 300px;
      margin:0 auto;
    }
  }
}

</style>
