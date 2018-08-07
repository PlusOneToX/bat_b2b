<template>
  <div>
    <div class="warehouse-list-wrap">
      <header>
        <h4 class="header_h4">分销商申请列表</h4>
      </header>
      <div class="function">
        <div class="Fheader">
          <div class="Fleft">
            <el-select class="choose" v-model="pageInfo.applyStatus" placeholder="审核状态" size="mini" clearable>
              <el-option v-for="item in state" :key="item.value" :label="item.label" :value="item.value"> </el-option>
            </el-select>
        </div>
        <div class="Fsearch">
          <el-select size="mini"  v-model="pageInfo.contentType" placeholder="分销商用户名" clearable>
            <el-option label="分销商用户名" value="1"> </el-option>
            <el-option label="客户名称" value="2"> </el-option>
            <el-option label="手机号" value="5"> </el-option>
          </el-select>
          <el-input v-model.trim="mytext" placeholder="请输入关键词搜索" size="mini" class="box-search"  @change="contentChange" @keyup.enter.native="Csearch" clearable></el-input>
          <el-button class="mini-search-btn box-btn" @click.prevent="Csearch">搜索</el-button>
          </div>
        </div>
      </div>

      <el-row>
        <!-- @selection-change="handleSelectionChange" -->
        <el-table :data="tableData" header-row-class-name="header-row" border ref="multipleTable" class="tableCenter" v-loading="loading" :height="tableHeight">
          <el-table-column label="分销商用户名" align="center" prop="name" :min-width="120"></el-table-column>
          <!-- <el-table-column label="审核时间" align="center" prop="updateTime" :formatter="formatTime" :min-width="150"></el-table-column> -->
          <el-table-column label="申请状态" align="center" prop="applyStatus" :formatter="forstatus" :min-width="120">
            </el-table-column>
            <el-table-column label="操作" align="center" :min-width="150">
              <template slot-scope="scope">
                <el-button class="mini-search-btn" @click="handleEdit(scope.row,scope.$index)">查看</el-button>
                <el-button class="mini-delete-btn" @click="handleDelete(scope.row)" v-if="scope.row.applyStatus === 3">删除</el-button>
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
      this.dataForamt()
    },
    activated () {
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
          contentType: '',
          applyType: 2, // 前台注册申请
          applyStatus: 1
        },
        states: "",
        state: [{
          value: 1,
          label: '审核中'
        },{
          value: 2,
          label: '审核通过'
        },{
          value: 3,
          label: '审核失败'
        }],
        mytext: '',
        total: 0,
        tableData: [],
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
        // this.pageInfo.applyStatus = this.states;
        this.dataForamt();
      },
      
      handleEdit(row,index) { // 查看操作
        this.$router.push({ name: 'distributorcooperatingadd',
          query:{
            id: row.id,
            checkMsg: 3,
            applyStatus: row.applyStatus,
            node: 1
          }
        })
      },
      
      handleDelete(row) { // 删除操作
        this.$confirm('此操作将永久删除该申请中分销商，是否继续？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(_ => {
          this.$http.deleteDistributor(this, {id: row.id}).then(res => {  
            if (res.success) {
              this.$message.success({
                message: "删除成功",
                duration: 3 * 1000,
              })
              this.dataForamt();
            }
          })
        }).catch(_ => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
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

      handleSelectionChange(val) { // 多选框的value
        // this.multipleTable = val;
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
        // switch(row.profileStatus) {
          case 0:
            return '未提交';
          case 1: 
            return '申请中';
          case 2:
            return '已通过';
          case 3:
            return '已拒绝';
          // case 4:
          //   return '审核中';
        }
      },
    },
    watch: {
      'pageInfo.applyStatus': {
        handler(val) {
          this.pageInfo.applyStatus = val
          this.dataForamt()
        },
        deep:true
      },
      // states: function() {
      //   // states 1: 申请中 3: 已拒绝 4: 审核中
      //   // states 1: 审核中 2：审核通过 3：审核失败
      //   if(this.states == '') {
      //     this.dataForamt();
      //   }else if(this.states == 1) {
      //     this.dataForamt(1);
      //   }else if(this.states == 2) {
      //     this.dataForamt(2)
      //   }else if(this.states == 3) {
      //     this.dataForamt(3)
      //   }
      // }
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
    width: 180px;
  }
  .box-btn {
    position: relative;
    top: -1px;
  }
  .Fheader {
    padding: 0 10px;
    margin: 10px auto;
    display: flex !important;
    justify-content: space-between !important;
    align-items: center !important;
    .el-select{
      /deep/.el-input__inner{
        width:130px;
        border-top-right-radius: 0;
        border-bottom-right-radius: 0;
      }
    }
    .box-search {
      width:220px;
      margin-left:-5px;
      /deep/.el-input__inner{
        border-top-left-radius: 0;
        border-bottom-left-radius: 0;
        border-left-width: 0;
      }
    }
  }
}
</style>