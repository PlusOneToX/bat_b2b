<!--
 * @Author: yaowei
 * @Date: 2018-05-18 21:07:04
 * @LastEditors: litian
 * @LastEditTime: 2018-06-04 12:00:02
-->
<template>
  <div class="group-buy-wrap">
    <header>
      <h4>拼团列表</h4>
      <el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="handleGroupBuy(1)">添加拼团</el-button>
    </header>

    <div class="function">
      <div class="Fheader">
        <div class="Fleft">
          <!-- <el-select size="mini" v-model="pageInfo.activityType" placeholder="拼团类型" style="width: 100px"
            @change="Csearch()" clearable>
            <el-option label="商品团" :value="1"></el-option>
            <el-option label="阶梯团" :value="2"></el-option>
          </el-select> -->
          <el-select size="mini" v-model="pageInfo.groupSeckillStatus" placeholder="拼团状态" style="width: 100px" @change="Csearch()"
            clearable>
            <!-- <el-option label="未发布" :value="-1"></el-option> -->
            <el-option label="未开始" :value="0"></el-option>
            <el-option label="进行中" :value="1"></el-option>
            <el-option label="已暂停" :value="2"></el-option>
            <el-option label="已过期" :value="3"></el-option>
            <el-option label="提前结束" :value="4"></el-option>
          </el-select>
        </div>
        <div class="Fsearch">
          <button class="mini-search-btn box-btn" @click="Csearch()">
            搜索
          </button>
          <el-input v-model.trim="pageInfo.content" size="mini" clearable @change="contentChange"
            @keyup.enter.native="Csearch()" placeholder="请输入搜索内容" class="box-input"></el-input>
            <el-select
            class="content_select"
            placeholder="选择类型"
            size="mini"
            style="width:140px;float:right;"
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
        </div>
      </div>
    </div>

    <el-row v-loading="loading">
      <el-table :data="tableData" header-row-class-name="header-row" border class="tableCenter">
        <el-table-column label="ID" align="center" prop="id" :min-width="120"></el-table-column>
        <el-table-column label="排序" align="center" prop="sort" :min-width="120"></el-table-column>
        <el-table-column label="拼团名称" align="center" prop="name" show-overflow-tooltip :min-width="120"></el-table-column>
        <el-table-column label="描述" align="center" prop="groupSeckillDesc" show-overflow-tooltip :min-width="120"></el-table-column>
        <!-- <el-table-column label="拼团类型" align="center" width="90">
          <template slot-scope="scope">
            <span v-if="scope.row.activityType === 1">商品团</span>
            <span v-else-if="scope.row.activityType === 2">阶梯团</span>
            <span v-else>--</span>
          </template>
        </el-table-column> -->
        <!-- <el-table-column label="拼团数量" align="center" prop="maxQuantity" width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.activityType === 1"></span>
            <span v-else-if="scope.row.activityType === 2"></span>
          </template>
        </el-table-column> -->
        <!-- <el-table-column label="已拼团数量" align="center" prop="saleSum" width="120">
        </el-table-column> -->
        <el-table-column label="有效时间" align="center" prop="validTime" show-overflow-tooltip :min-width="190" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="状态" align="center" width="80">
          <template slot-scope="scope">
            <!-- <span v-if="scope.row.groupSeckillStatus === -1">未发布</span> -->
            <span v-if="scope.row.groupSeckillStatus === 0">未开始</span>
            <span v-else-if="scope.row.groupSeckillStatus === 1">进行中</span>
            <span v-else-if="scope.row.groupSeckillStatus === 2">已暂停</span>
            <span v-else-if="scope.row.groupSeckillStatus === 3">已过期</span>
            <span v-else-if="scope.row.groupSeckillStatus === 4">提前结束</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <!-- <el-table-column label="售卖类型" align="center" :min-width="170" show-overflow-tooltip>
          <template slot-scope="scope">
            <span v-if="scope.row.saleSkuType === 1">清仓（普通商品下单数量：{{scope.row.otherSaleQuantitySum}}）</span>
            <span v-else-if="scope.row.saleSkuType === 2">预售</span>
            <span v-else>--</span>
          </template>
        </el-table-column> -->
        <el-table-column label="操作" :min-width="260" align="center" >
          <template slot-scope="scope">
            <el-button v-if="Number(scope.row.groupSeckillStatus) !== 3 && !(scope.$index === 0 && pageInfo.page===1)" class="mini-freeze-btn"
              @click="handleMoveTop(scope.row.id)">置顶</el-button>
            <el-button v-if="Number(scope.row.groupSeckillStatus) !== 3 && !(scope.$index === 0 && pageInfo.page===1)" class="mini-browse-btn"
              @click="handleMove(scope.row, scope.$index, 1)">上移</el-button>
            <el-button v-if="Number(scope.row.groupSeckillStatus) !== 3 && !(scope.$index === tableData.length-1 && pageInfo.page===Math.ceil(total/pageInfo.size))" class="mini-pulloff-btn"
              @click="handleMove(scope.row, scope.$index, -1)">下移</el-button>
            <el-button v-if="Number(scope.row.groupSeckillStatus) === -1" class="mini-freeze-btn"
              @click="handleStatus(scope.row.id, 0)">发布</el-button>
            <el-button v-if="Number(scope.row.groupSeckillStatus) === 2" class="mini-tableadd-btn"
              @click="handleStatus(scope.row.id, 1)">启用</el-button>
            <el-button v-if="Number(scope.row.groupSeckillStatus) === 1" class="mini-freeze-btn"
              @click="handleStatus(scope.row.id, 2)">暂停</el-button>
            <el-button v-if="Number(scope.row.groupSeckillStatus) === -1" class="mini-search-btn"
              @click="handleGroupBuy(3, scope.row.id)">编辑</el-button>
            <el-button class="mini-search-btn" v-if="Number(scope.row.groupSeckillStatus) !== -1"
              @click="handleGroupBuy(2, scope.row.id)">查看</el-button>
            <el-button v-if="
                Number(scope.row.groupSeckillStatus) === -1 ||
                Number(scope.row.groupSeckillStatus) === 0
              " class="mini-delete-btn" @click="handleDelete(scope.row.id)">删除</el-button>
            <!-- <el-button class="mini-pulloff-btn" @click="handleCopy(scope.row.id)">复制</el-button> -->
            <el-button v-if="Number(scope.row.groupSeckillStatus)===1 || Number(scope.row.groupSeckillStatus)===2" class="el-button mini-delete-btn el-button--default"
              @click="handleStatus(scope.row.id, 4)">提前结束</el-button>
          </template>
        </el-table-column>
      </el-table>
      <page :total="total" :page="pageInfo.page" @sizeChange="sizeChange" @currentChange="currentChange"></page>
    </el-row>
  </div>
</template>

<script type="text/javascript">
  import page from "@/components/pagination";
  import {
    timeFormat
  } from "@/utils/timeFormat.js";
  import {
    getGroupBuyList,
    handleUpGroup,
    handleDownGroup,
    handleGroupStatus,
    handleDeleteGroup,
    handleCopyGroup,
    handleTop
  } from "@/views/marketingPromotion/groupBuy/groupBuyData";
  export default {
    name: "groupBuy",
    components: {
      page
    },
    data() {
      return {
        loading: false,
        pageInfo: {
          // 拼团搜索
          page: 1,
          size: 10,
          groupSeckillStatus: 1, // 拼团状态
          groupSeckillType: 1, // 拼团
          contentType: undefined,
          content: undefined
        },
        contentTypes: [
          { value: 1, label: '拼团活动名称' },
          { value: 3, label: '货品编号' },
          { value: 4, label: '分销商用户名' }
        ],
        tableData: [], // 拼团列表
        total: "", // 拼团列表总数
      };
    },
    created() {
      this.initData();
    },
    activated() {
      this.initData();
    },
    methods: {
      // 初始化列表数据
      initData() {
        // getGroupBuyList(this, this.pageInfo).then((res) => {
        this.$http.groupseckillList(this, this.pageInfo).then(res => {  
          if (res.success) {
            this.tableData = res.data.list;
            this.total = res.data.total;
            for (var i = 0; i < res.data.list.length; i++) {
              this.tableData.forEach((item) => {
                if (item.id === res.data.list[i].id) {
                  // 有效时间处理
                  var startTime = timeFormat(res.data.list[i].startTime);
                  var endTime = timeFormat(res.data.list[i].endTime);
                  item.validTime = startTime + " - " + endTime;
                }
              });
            }
          }
        });
      },
      // 拼团列表条数
      sizeChange(size) {
        this.pageInfo.count = size;
        this.pageInfo.page = 1;
        this.initData();
      },
      // 拼团列表页数
      currentChange(page) {
        this.pageInfo.page = page;
        this.initData();
      },
      // 搜索拼团列表
      Csearch() {
        this.pageInfo.page = 1;
        this.initData();
      },
      // 输入框输入搜索优惠券列表
      contentChange(val) {
        if (val === undefined || val === "" || val === null) {
          this.Csearch();
        }
      },
      // 添加/查看/编辑 - 拼团（1，添加，2，查看；3，编辑）
      handleGroupBuy(checkMsg, id) {
        var query = {};
        if (id) {
          query = {
            checkMsg: checkMsg,
            id: id
          };
        } else {
          query = {
            checkMsg: checkMsg
          };
        }
        this.$router.push({
          name: "addGroupBuy",
          query: query,
        });
      },
      // 置顶
      handleMoveTop(id) {
        // handleTop(this, id, {}).then((res) => {
        if (this.pageInfo.page !== 1) {
           this.pageInfo.page = 1
          this.$http.groupseckillList(this, this.pageInfo).then(res2 => {  
            if (res2.success) {
              let sort = res2.data.list[0].sort;
              let arr = [{
                id: id,
                sort: sort - 1
              }]
              this.$http.groupseckillSort(this, arr).then(res => {  
                if (res.success) {
                  this.$message({
                    type: "success",
                    message: "置顶成功",
                  });
                  this.initData();
                }
              });
            }
          });
        } else {
          let sort = this.tableData[0].sort - 1
          let arr = [{
            id: id,
            sort: sort
          }]
          this.$http.groupseckillSort(this, arr).then(res => {  
            if (res.success) {
              this.$message({
                type: "success",
                message: "置顶成功",
              });
              this.initData();
            }
          });
        }
      },
      // 上/下移（index：1 上移/-1 下移）
      handleMove(row, index, type) {
        let tabArr = this.tableData
        let arr = []
        if (type === 1) { // 上移
          let sort
          if (index === 0) {
            // 列表首位
            this.$http.groupseckillList(this, { 
              page: this.pageInfo.page - 1,
              size: 10,
              groupSeckillStatus: this.pageInfo.groupSeckillStatus, // 拼团状态
              groupSeckillType: 1, // 拼团
              contentType: this.pageInfo.contentType,
              content: this.pageInfo.content
            }).then(res2 => { 
              if (res2.success) {
                let id = res2.data.list[9].id
                if (res2.data.list[9].sort === row.sort) {
                  sort = res2.data.list[9].sort - 1
                } else {
                  sort = res2.data.list[9].sort
                }
                arr = [{
                  id: row.id,
                  sort: sort
                }, {
                  id: id,
                  sort: row.sort
                }]
                this.$http.groupseckillSort(this, arr).then(res => {
                  if (res.success) {
                    this.$message({
                      type: "success",
                      message: "上移成功",
                    });
                    this.initData();
                  }
                });
              }
            })
          } else {
            if (tabArr[index-1].sort === row.sort) {
              sort = tabArr[index].sort - 1
            } else {
              sort = tabArr[index-1].sort
            }
            arr = [{
              id: row.id,
              sort: sort
            }, {
              id: tabArr[index-1].id,
              sort: row.sort
            }]
            this.$http.groupseckillSort(this, arr).then(res => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "上移成功",
                });
                this.initData();
              }
            });
          }
        } else {
          // 下移
          let sort
          // 列表最后一位
          if (index === tabArr.length-1) {
              this.$http.groupseckillList(this, { 
                page: this.pageInfo.page + 1,
                size: 10,
                groupSeckillStatus: this.pageInfo.groupSeckillStatus, // 拼团状态
                groupSeckillType: 1, // 拼团
                contentType: this.pageInfo.contentType,
                content: this.pageInfo.content
              }).then(res2 => { 
                if (res2.success) {
                  let id = res2.data.list[0].id
                  if (res2.data.list[0].sort === row.sort) {
                    sort = res2.data.list[0].sort + 1
                  } else {
                    sort = res2.data.list[0].sort
                  }
                  arr = [{
                    id: row.id,
                    sort: sort
                  }, {
                    id: id,
                    sort: row.sort
                  }]
                  this.$http.groupseckillSort(this, arr).then(res => {
                    if (res.success) {
                      this.$message({
                        type: "success",
                        message: "下移成功",
                      });
                      this.initData();
                    }
                  });
                }
              })
          } else {
            if (tabArr[index+1].sort === row.sort) {
              sort = tabArr[index].sort + 1
            } else {
              sort = tabArr[index+1].sort
            }
            arr = [{
              id: row.id,
              sort: sort
            }, {
              id: tabArr[index+1].id,
              sort: row.sort
            }]
            this.$http.groupseckillSort(this, arr).then(res => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "下移成功",
                });
                this.initData();
              }
            });
          }
        }
      },
      // 发布/启用/暂停/提前结束 （status：0 发布/1 启用/2 暂停/4 提前结束）
      handleStatus(id, status) {
        let msg = "";
        if (status === 0) {
          msg = "此操作将发布该拼团，是否继续？";
        } else if (status === 1) {
          msg = "此操作将启用该拼团，是否继续？";
        } else if (status === 2) {
          msg = "此操作将暂停该拼团，是否继续？";
        } else if (status === 4) {
          msg = "此操作将提前结束该拼团，是否继续？";
        }
        this.$confirm(msg, "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
            center: true,
          })
          .then((_) => {
            // handleGroupStatus(this, {
            this.$http.groupseckillStatus(this, {  
              id: id,
              groupSeckillStatus: status
            }).then((res) => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "操作成功",
                });
                this.initData();
              }
            });
          })
          .catch((_) => {
            this.$message({
              type: "info",
              message: "已取消操作",
            });
          });
      },
      // 删除
      handleDelete(id) {
        this.$confirm("此操作将删除该拼团，是否继续？", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
            center: true,
          })
          .then((_) => {
            // handleDeleteGroup(this, id, {}).then((res) => {
            this.$http.delGroupseckill(this, {id: id}).then(res => {  
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "删除成功",
                });
                this.initData();
              }
            });
          })
          .catch((_) => {
            this.$message({
              type: "info",
              message: "已取消操作",
            });
          });
      },
      // 复制
      handleCopy(id) {
        this.$confirm("此操作将复制该拼团，是否继续？", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
            center: true,
          })
          .then((_) => {
            handleCopyGroup(this, id, {}).then((res) => {
              if (Number(res.code) === 0) {
                this.$message({
                  type: "success",
                  message: "复制成功",
                });
                this.initData();
              }
            });
          })
          .catch((_) => {
            this.$message({
              type: "info",
              message: "已取消操作",
            });
          });
      },
    },
  };

</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .el-table .cell {
    overflow: hidden;
    white-space: nowrap;
  }

  .group-buy-wrap {
    background-color: white;

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

      .btn-home {
        float: right;
        padding: 5px;
        margin-top: 8px;
        margin-right: 8px;
        margin-left: 0;
        font-size: 12px;
      }
    }
  }

  .function {
    background-color: white;
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
    }
  }

</style>
