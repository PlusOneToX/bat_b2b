<template>
  <div>
    <div class="warehouse-list-wrap">
      <header>
        <h4 class="header_h4">分销商申请列表</h4>
      </header>
      <div class="function">
        <div class="Fheader">
            <el-select size="mini"  v-model="pageInfo.treeNode" placeholder="级数" style="width: 100px;" clearable>
              <el-option :key="item" :label="item" :value="item" v-for="item in treeNodes"></el-option>
            </el-select>
            <div class="Fsearch">
              <button class="mini-search-btn box-btn" @click="Csearch()">搜索</button>
              <el-input v-model.trim="mytext" size="mini" clearable @change="contentChange" @keyup.enter.native="Csearch()" placeholder="请输入关键词搜索" class="box-input"></el-input>
              <el-select size="mini"  v-model="pageInfo.contentType" placeholder="分销商用户名" style="width: 140px;" clearable>
                <el-option label="分销商用户名" value="1"> </el-option>
                <el-option label="客户名称" value="2"> </el-option>
                <el-option label="分销商ID" value="3"> </el-option>
                <el-option label="上级分销商客户名" value="4"> </el-option>
                <el-option label="手机号" value="5"> </el-option>
              </el-select>
            </div>
        </div>
      </div>
      <el-row>
        <el-table :data="tableData" header-row-class-name="header-row" border ref="multipleTable" class="tableCenter" v-loading="loading" :height="tableHeight">
          <el-table-column label="上级分销商" align="center" prop="upCompanyName" :min-width="120"></el-table-column>
          <el-table-column label="B2B编码" align="center" prop="id" :min-width="120"></el-table-column>
          <el-table-column label="分销商用户名" align="center" prop="name" :min-width="120"></el-table-column>
          <el-table-column label="级数" align="center" prop="treeNode" :min-width="120"></el-table-column>
          <el-table-column label="联系人" align="center" prop="userName" :min-width="120"></el-table-column>
          <el-table-column label="联系人手机号" align="center" prop="phone" :min-width="120"></el-table-column>
          <el-table-column sortable  align="center" label="审核时间" prop="checkTime" :formatter="formatTime" :min-width="160"></el-table-column>
          <el-table-column label="申请状态" align="center" prop="applyStatus" :formatter="forstatus" :min-width="120"></el-table-column>
          <el-table-column label="操作" align="center" :min-width="150">
            <template slot-scope="scope">
              <el-button class="mini-search-btn" @click="handleEdit(scope.row,scope.$index)">审核</el-button>
            </template>
          </el-table-column>
        </el-table>
        <page :total="total" @sizeChange="sizeChange" @currentChange="currentchange"></page>
      </el-row>
    </div>
  </div>
</template>

<script>
import page from '@/components/pagination'
import { timeFormat } from "@/utils/timeFormat";
  export default {
    name: 'Dapplication',
    components: {
      page
    },
    created() {
      const documentHeight = document.body.clientHeight;
      this.tableHeight = parseInt(documentHeight  *  0.8  -  100); // 计算表高度，固定表头
      this.iniTree()
      this.dataForamt()
    },
    data() {
      return {
        tableHeight: 600,
        loading: false,
        pageInfo: {
          content: '',
          page: 1,
          size: 10,
          treeNode: '',
          applyStatus: 1 // 申请中
        },
        states: "",
        mytext: '',
        total: 0,
        tableData: [],
        treeNodes: []
      }
    },
    methods: {
      // ======== 操作 ========
      contentChange(val){
        if(val === undefined || val === '' || val === null){
          this.Csearch()
        }
      },
      Csearch() { //搜索操作
        this.pageInfo.content = this.mytext;
        this.dataForamt();
      },
      
      handleEdit(row,index) { // 审核
        this.$confirm('审核通过或拒绝', '分销商审核', {
          confirmButtonText: '通过',
          cancelButtonText: '拒绝',
          center: true
        }).then(_ => {
          // 通过
        }).catch(_ => {
          // 拒绝
        })     
      },
      
      iniTree () {
        this.$http.baseSetting(this, {key: 'distribution_layers'}).then(res => {
          if (res.success) {
            this.treeNodes = []
            for(let i=2;i<=res.data.value;i++) {
              this.treeNodes.push(i)
            }
          }
        })
      },
      // ======== 数据 ========
      dataForamt() { // 数据列表
        this.loading = true;
        this.$http.getDistributorFList(this, this.pageInfo).then(res => {
          if (res.success) {
            let ary = [];
            res.data.list.forEach(item => {
              ary.push(item)
            })
            ary.sort((a, b) => {  // 用户名排序，从大到小
              return a.sort - b.sort > 0
            })
            this.tableData = ary;
            this.total = res.data.total
            this.loading = false // 查看加载效果
          }
        })
      },

      sizeChange(size) { // 分页页数
        this.pageInfo.size = size;
        this.dataForamt()
      },

      currentchange(page) { // 分页总数
        this.pageInfo.page = page;
        this.dataForamt()
      },
      // ======== 转换 ========
      formatTime(row, col, val) { // 表格时间格式化
        return timeFormat(val);
      }, 

      forstatus(row) { // 申请状态
        switch(row.applyStatus) {
          case 0:
            return '未提交';
          case 1: 
            return '待审核';
          case 2:
            return '已通过';
          case 3:
            return '已拒绝';
        }
      },
    },
    watch: {
      "pageInfo.treeNode":function(){
        this.pageInfo.page = 1
        this.dataForamt()
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.warehouse-list-wrap {
  background-color: #fff;
  .header_h4 {
    margin: 0 0 0 30px;
  }
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
    h4 {
      margin-left: 30px;
      display: inline-block;
      font-weight: 400;
    }
  }
}
.function {
  background-color: white;
  .choose {
    position: relative;
    top: 1px;
  }
  .search {
    float: right;
  }
  .box-search {
    width: 140px;
  }
  .box-btn {
    position: relative;
    top: -1px;
  }
  .Fheader {
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
      /deep/.el-input{
        .el-input__inner{
          border-top-right-radius: 0;
          border-bottom-right-radius: 0;
        }
      }
      /deep/.box-input{
        .el-input__inner{
          border-top-left-radius: 0;
          border-bottom-left-radius: 0;
          border-left-width: 0;
        }
      }
    }
  }
}
</style>