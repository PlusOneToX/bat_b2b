<template>
  <div class="category-list">
    <header>
      <h4>优惠券列表</h4>
      <el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="handleCoupon(1)">添加优惠券</el-button>
    </header>

    <div class="function">
      <div class="Fheader">
        <div class="Fleft">
          <el-select
            size="mini"
            v-model="pageInfo.couponMethod"
            placeholder="优惠类型"
            style="width: 100px;"
            @change="Csearch()"
            clearable
          >
            <el-option label="满减" value="1"></el-option>
            <el-option label="满折" value="2"></el-option>
            <el-option label="兑换" value="3"></el-option>
          </el-select>
          <el-select
            size="mini"
            v-model="pageInfo.couponStatus"
            placeholder="状态"
            style="width: 100px;"
            @change="Csearch()"
            clearable
          >
            <el-option label="未发布" :value="0"></el-option>
            <el-option label="未开始" :value="1"></el-option>
            <el-option label="进行中" :value="2"></el-option>
            <el-option label="已过期" :value="3"></el-option>
            <el-option label="提前结束" :value="4"></el-option>
            <el-option label="已作废" :value="5"></el-option>
          </el-select>
        </div>
        <div class="Fsearch">
          <button class="mini-search-btn box-btn" @click="Csearch()">搜索</button>
          <el-input
            v-model.trim="pageInfo.content"
            size="mini"
            clearable
            @change="contentChange"
            @keyup.enter.native="Csearch()"
            placeholder="请输入优惠券名称"
            class="box-input"
          ></el-input>
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
        <el-table-column label="优惠券名称" align="center" prop="name" :min-width="120"></el-table-column>
        <el-table-column label="优惠券描述" align="center" prop="couponDesc" :min-width="120"></el-table-column>
        <el-table-column label="优惠形式" align="center" :min-width="90">
          <template slot-scope="scope">
            <span v-if="scope.row.couponMethod === 1">满减</span>
            <span v-else-if="scope.row.couponMethod === 2">满折</span>
            <span v-else-if="scope.row.couponMethod === 3">兑换</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column label="使用/发放数量" align="center" prop="codeCount" :min-width="100"></el-table-column>
        <el-table-column
          label="有效时间"
          align="center"
          prop="validTime"
          :min-width="160"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column label="申请状态" align="center" :min-width="90">
          <template slot-scope="scope">
            <span v-if="scope.row.applyStatus === 0">草稿</span>
            <span v-else-if="scope.row.applyStatus === 1">申请中</span>
            <span v-else-if="scope.row.applyStatus === 2">申请通过</span>
            <span v-else-if="scope.row.applyStatus === 3">申请失败</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column label="优惠券状态" align="center" :min-width="90">
          <template slot-scope="scope">
            <span v-if="scope.row.couponStatus === 0">未发布</span>
            <span v-else-if="scope.row.couponStatus === 1">未开始</span>
            <span v-else-if="scope.row.couponStatus === 2">进行中</span>
            <span v-else-if="scope.row.couponStatus === 3">已过期</span>
            <span v-else-if="scope.row.couponStatus === 4">提前结束</span>
            <span v-else-if="scope.row.couponStatus === 5">已作废</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column
          label="作废说明"
          align="center"
          prop="invalidExplain"
          :min-width="120"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column label="操作" :min-width="260" align="center">
          <template slot-scope="scope">
            <el-button
              v-if="Number(scope.row.couponStatus) !== 0"
              class="mini-browse-btn"
              @click="handleCode(scope.$index, scope.row.id)"
            >码库</el-button>
            <el-button
              v-if="Number(scope.row.couponStatus) === 0"
              class="mini-search-btn"
              @click="handleCoupon(3, scope.row.id)"
            >编辑</el-button>
            <el-button v-else class="mini-search-btn" @click="handleCoupon(2, scope.row.id)">查看</el-button>
            <el-button
              v-if="Number(scope.row.couponStatus) < 3"
              class="mini-browse-btn"
              @click="handleCouponCount(scope.$index, scope.row)"
            >编辑数量</el-button>
            <el-button
              v-if="Number(scope.row.couponStatus) === 0 && Number(scope.row.applyStatus) > 1"
              class="mini-tableadd-btn"
              @click="handleStart(scope.row.id)"
            >发布</el-button>
            <el-button
              v-if="(Number(scope.row.couponStatus) === 1) || (Number(scope.row.couponStatus) === 2)"
              class="mini-freeze-btn"
              @click="handleStop(scope.row.id)"
            >作废</el-button>
            <el-button
              v-if="Number(scope.row.couponStatus) === 2"
              class="mini-delete-btn"
              @click="handleEarlyStop(scope.row.id)"
            >提前结束</el-button>
            <el-button
              v-if="Number(scope.row.couponStatus) === 0"
              class="mini-delete-btn"
              @click="handleDelete(scope.row.id)"
            >删除</el-button>
            <!-- <el-button
              :class="{'mini-tableadd-btn':scope.row.isHide,'mini-delete-btn':!scope.row.isHide}"
              @click="handleHide(scope.row)"
            >{{scope.row.isHide?"显示":"隐藏"}}</el-button> -->
          </template>
        </el-table-column>
      </el-table>
      <page
        :total="couponTotal"
        :page="pageInfo.page"
        @sizeChange="sizeChange"
        @currentChange="currentChange"
      ></page>
    </el-row>

    <!-- 码库 -->
    <el-dialog class="code-wrap" width="70%" :visible.sync="codeShow" center>
      <div class="Fheader">
        <div class="Fleft">
          <el-button class="mini-freeze-btn" @click="handleInvalidCodeMore">批量作废</el-button>
          <el-select
            size="mini"
            v-model="codeInfo.platform"
            placeholder="注册渠道"
            style="width: 100px;"
            @change="searchCode()"
            clearable
          >
            <el-option v-for="item in platforms" :key="item.platformNo" :label="item.name" :value="item.platformNo"></el-option>
            <!-- <el-option label="微信" value="1"></el-option>
            <el-option label="支付宝" value="2"></el-option>
            -->
          </el-select>
          <el-select
            size="mini"
            v-model="codeInfo.couponStatus"
            placeholder="券码状态"
            style="width: 100px;"
            @change="searchCode()"
            clearable
          >
            <el-option label="未开始" value="1"></el-option>
            <el-option label="进行中" value="2"></el-option>
            <el-option label="已过期" value="3"></el-option>
            <el-option label="已作废" value="5"></el-option>
             <el-option label="已使用" value="6"></el-option>
          </el-select>
        </div>
        <div class="Fsearch">
          <button class="mini-search-btn box-btn" @click="searchCode()">搜索</button>
          <el-input
            v-model.trim="codeInfo.content"
            size="mini"
            clearable
            @change="codeChange"
            @keyup.enter.native="searchCode()"
            placeholder="请输入优惠券码"
            class="box-input"
          ></el-input>
          <el-select
            size="mini"
            v-model="codeInfo.contentType"
            placeholder="类型"
            style="width: 100px;"
            clearable
          >
            <el-option label="客户名称" value="1"></el-option>
            <el-option label="优惠券码" value="2"></el-option>
          </el-select>
        </div>
      </div>
      <el-table
        :data="codeData"
        header-row-class-name="header-row"
        border
        class="tableCenter"
        height="500"
        ref="multipleSelect"
        @select="select"
        @select-all="selectAll"
        @selection-change="handleSelectionChange"
      >
        <el-table-column align="center" type="selection" width="50" :selectable="selectable"></el-table-column>
        <el-table-column
          label="优惠券名称"
          align="center"
          prop="couponName"
          :min-width="120"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column label="优惠券码" show-overflow-tooltip align="center" prop="couponNo" :min-width="140"></el-table-column>
        <el-table-column
          label="生成时间"
          align="center"
          prop="createTime"
          :formatter="formatTime"
          :min-width="160"
        ></el-table-column>
        <el-table-column label="用户名" align="center" prop="name" :min-width="120"></el-table-column>
        <el-table-column label="手机号" align="center" prop="phone" :min-width="120"></el-table-column>
        <el-table-column label="手机昵称" align="center" prop="nikeName" :min-width="120"></el-table-column>
        <el-table-column label="注册渠道" align="center" :min-width="100" show-overflow-tooltip>
          <template slot-scope="scope">
            <span v-if="scope.row.platform === 1">微信</span>
            <span v-else-if="scope.row.platform === 2">支付宝</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column label="渠道ID" show-overflow-tooltip align="center" prop="openId" :min-width="85"></el-table-column>
        <el-table-column label="券码状态" align="center" :min-width="120" show-overflow-tooltip>
          <template slot-scope="scope">
            <span v-if="scope.row.couponStatus === 5">已作废</span>
            <span v-else-if="scope.row.couponStatus === 1">未开始</span>
            <span v-else-if="scope.row.couponStatus === 2">进行中</span>
            <span v-else-if="scope.row.couponStatus === 3">已过期</span>
            <span v-else-if="scope.row.couponStatus === 6">已使用</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column label="作废说明" align="center" prop="invalidExplain" :min-width="120"></el-table-column>
        <el-table-column label="操作" :min-width="100" align="center">
          <template slot-scope="scope">
            <el-button
              class="mini-search-btn"
              @click="handleInvalidCode([scope.row.id])"
              v-if="scope.row.couponStatus !== 5"
            >作废</el-button>
          </template>
        </el-table-column>
      </el-table>
      <page
        :total="codeTotal"
        :page="codeInfo.page"
        @sizeChange="codeSizeChange"
        @currentChange="codeCurrentChange"
      ></page>
    </el-dialog>

    <!-- 编辑优惠券发放总数量和限购数量 -->
		<el-dialog class="coupon-wrap" title="编辑" width="50%" :visible.sync="couponShow" center>
      <el-form :model="formData"  label-width="100px" ref="formData2">
        <el-form-item label="优惠券发放总数量" prop="generateCount">
          <el-input v-model="formData.generateCount" onkeyup="value=value.replace(/[^\d]/g,'')" placeholder="请输入优惠券发放数量，默认为无限制" clearable/>
        </el-form-item>
        <el-form-item label="限购数量" prop="limitCount">
          <el-input v-model="formData.limitCount" onkeyup="value=value.replace(/[^\d]/g,'')"  placeholder="请输入单个用户限领的数量上限，默认为不限制" clearable/>
        </el-form-item>
      </el-form>
      <el-button class="mini-search-btn check_btn" @click="handleSubmit()">确定</el-button>
      <el-button class="check_back_btn" size="mini" @click="couponShow=false">返回</el-button>
		</el-dialog>
  </div>
</template>

<script type="text/javascript">
import page from "@/components/pagination";
import { timeFormat } from "@/utils/timeFormat.js";
export default {
  name: "coupon",
  components: { page },
  data() {
    return {
      loading: false,
      pageInfo: {
        // 优惠券搜索
        page: 1,
        size: 10,
        couponMethod: "",
        couponStatus: "",
        contentType: '',
        content: "",
      },
      contentTypes: [
        { value: 1, label: '优惠券名称' },
        { value: 2, label: '材料名称' },
        { value: 3, label: '型号名称' },
        { value: 4, label: '分销商' }
      ],
      couponTotal: 0, // 消费券总数
      tableData: [], // 消费券列表
      codeShow: false, // 是否显示码库
      codeData: [], // 码库列表
      codeInfo: {
        // 码库搜索
        page: 1,
        size: 10,
        couponId: "",
        platform: "",
        couponStatus: "",
      },
      codeTotal: 0, // 码库总数
      multipleSelect: [], // 已选择优惠券 码库
      curCouponId: "", // 当前查看优惠券 码库
      couponShow: false, // 是否显示优惠券数量弹框
      cId: '', // 当前编辑优惠券Id
      formData: {
        id: '',
        generateCount: '',
        limitCount:''
      },
      platforms: []  // 注册渠道列表
    };
  },
  created() {
    this.initData();
  },
  activated() {
    this.initData();
  },
  methods: {
    // 表格时间格式化
    formatTime(row, col, val) {
      return timeFormat(val);
    },
    // 初始化数据
    initData() {
      this.initCouponData();
    },
    // 获取平台信息
    initPlatform () {
      this.$http.getSysPlatformList(this, {page:1, size:100, openFlag: 1}).then(res => {
        if (res.success) {
          this.platforms = res.data.list
        }
      })
    },
    // 初始化优惠券列表数据
    initCouponData() {
      this.loading = true;
      this.$http.couponList(this, this.pageInfo).then(res => {  
        if (res.success) {
          this.loading = false;
          this.tableData = res.data.list;
          this.couponTotal = res.data.total;
          for (var i = 0; i < res.data.list.length; i++) {
            this.tableData.forEach((item) => {
              if (item.id === res.data.list[i].id) {
                // 使用/发放数量处理
                var codeCount = 0;
                let generatedCount = res.data.list[i].generatedCount ? res.data.list[i].generatedCount : 0
                if (res.data.list[i].generateCount === null) {
                  codeCount = generatedCount + " / 0";
                } else {
                  codeCount =
                    generatedCount +
                    " / " +
                    res.data.list[i].generateCount;
                }
                item.codeCount = codeCount;
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
    // 优惠券条数
    sizeChange(size) {
      this.pageInfo.size = size;
      this.pageInfo.page = 1;
      this.initData();
    },
    // 优惠券页数
    currentChange(page) {
      this.pageInfo.page = page;
      this.initData();
    },
    // 搜索优惠券列表
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
    // 添加/查看/编辑 - 优惠券（1，添加，2，查看；3，编辑）
    handleCoupon(checkMsg, id) {
      var query = {};
      if (id) {
        query = { checkMsg: checkMsg, id: id };
      } else {
        query = { checkMsg: checkMsg };
      }
      this.$router.push({
        name: "addCoupon",
        query: query,
      });
    },
    // 优惠券发放总数量和限购数量弹框
    handleCouponCount (index, row) {
      this.formData.id = row.id
      this.formData.generateCount = row.generateCount 
      this.formData.limitCount = row.limitCount 
      this.couponShow = true
    },
    // 保存优惠券发放总数量和限购数量 
    handleSubmit () {
      if (this.formData.id) {
        this.$http.couponCount(this, this.formData).then(res => {
          if (res.success) {
            this.$message({
                type: "success",
                message: "更新成功",
              });
              this.couponShow = false
              this.initData();
          }
        })
      }
    },
    // 发布优惠券
    handleStart(id) {
      this.$confirm("此操作将发布该优惠券，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          this.loading = true;
          this.$http.couponStatus(this, {id: id, couponStatus: 1}).then((res) => {  
            if (res.success) {
              this.loading = false;
              this.$message({
                type: "success",
                message: "发布成功",
              });
              this.initData();
            } else {
              this.$message({
                type: "error",
                message: res.msg,
              });
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
    // 提前结束
    handleEarlyStop (id) {
      this.$confirm("此操作将提前结束该优惠券，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          this.loading = true;
          this.$http.couponStatus(this, {id: id, couponStatus: 4}).then((res) => {  
            if (res.success) {
              this.loading = false;
              this.$message({
                type: "success",
                message: "操作成功",
              });
              this.initData();
            } else {
              this.$message({
                type: "error",
                message: res.msg,
              });
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
    // 作废优惠券
    handleStop(id) {
      this.$prompt("请输入作废说明", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputValidator(value) {
          if (value) {
            return true;
          } else {
            return false;
          }
        },
        inputErrorMessage: "请输入作废说明，最多200个字",
      }).then(({ value }) => {
          this.loading = true;
          this.$http.couponStatus(this, {id: id, couponStatus: 5, invalidExplain: value}).then((res) => {   
            if (res.success) {
              this.loading = false;
              this.$message({
                type: "success",
                message: "作废成功",
              });
              this.initData();
            } else {
              this.$message({
                type: "error",
                message: res.msg,
              });
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消操作",
          });
        });
    },
    // 删除优惠券
    handleDelete(id) {
      this.$confirm("此操作将删除该优惠券，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          this.loading = true;
          // deleteCoupon(this, { id: id }).then((res) => {
          this.$http.delCoupon(this, {id: id}).then(res => {  
            if (res.success) {
              this.loading = false;
              this.$message({
                type: "success",
                message: "删除成功",
              });
              this.initData();
            } else {
              this.$message({
                type: "error",
                message: res.msg,
              });
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
    //显示隐藏优惠券
    // handleHide(row){
    //   let show = Number(!row.isHide);
    //   let text = show?"此操作将隐藏该优惠券，是否继续？":"此操作将显示该优惠券，是否继续？";
    //   this.$confirm(text, "提示", {
    //     confirmButtonText: "确定",
    //     cancelButtonText: "取消",
    //     type: "warning",
    //     center: true,
    //   })
    //     .then((_) => {
    //       this.loading = true;
    //       hideCoupon(this,{id:row.id,hide:show}).then(res=>{
    //         if (Number(res.code) === 0) {
    //           this.loading = false;
    //           this.$message({
    //             type: "success",
    //             message: "操作成功",
    //           });
    //           this.initData();
    //         } else {
    //           this.$message({
    //             type: "error",
    //             message: res.msg,
    //           });
    //         }
    //       })
    //     })
    //     .catch((_) => {
    //       this.$message({
    //         type: "info",
    //         message: "已取消操作",
    //       });
    //     });
    // },
    // 码库列表
    handleCode(index, id) {
      this.codeShow = true;
      this.curCouponId = id;
      this.initPlatform()
      this.initCodeData(id);
    },
    // 码库数据初始化
    initCodeData(id) {
      this.initCodeListData(id);
    },
    // 初始化码库列表数据
    initCodeListData(id) {
      this.loading = true;
      this.codeInfo.couponId = id;
      // getCounponCode(this, this.codeInfo).then((res) => {
      this.$http.couponNoList(this, this.codeInfo).then(res => {  
        if (res.success) {
          this.codeData = res.data.list
          this.codeTotal = res.data.total
        }
        res.success ? this.loading = false : this.loading = false
      });
    },
    // 优惠券码条数
    codeSizeChange(size) {
      this.codeInfo.size = size;
      this.codeInfo.page = 1;
      this.initCodeData(this.curCouponId);
    },
    // 优惠券码页数
    codeCurrentChange(page) {
      this.codeInfo.page = page;
      this.initCodeData(this.curCouponId);
    },
    // 搜索码库列表
    searchCode() {
      this.codeInfo.page = 1;
      this.initCodeData(this.curCouponId);
    },
    // 输入框输入搜索码库列表
    codeChange(val) {
      if (val === undefined || val === "" || val === null) {
        this.searchCode();
      }
    },
    // 码库 - 作废
    handleInvalidCode(ids) {
      this.$prompt("请输入作废说明", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputValidator(value) {
          if (value) {
            return true;
          } else {
            return false;
          }
        },
        inputErrorMessage: "请输入作废说明",
      })
        .then(({ value }) => {
          this.loading = true;
          // invalidCouponCode(this, { ids: ids, invalidExplain: value }).then(
          this.$http.couponNoStatus(this, {couponNos:ids, couponStatus: 5}).then(  
            (res) => {
              if (res.success) {
                this.loading = false;
                this.$message({
                  type: "success",
                  message: "作废成功",
                });
                this.initCodeData(this.curCouponId);
              } else {
                this.$message({
                  type: "error",
                  message: res.msg,
                });
                this.loading = false;
              }
            }
          );
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消操作",
          });
        });
    },
    // 码库 - 批量作废
    handleInvalidCodeMore() {
      let ids = [];
      for (let i = 0; i < this.multipleSelect.length; i++) {
        ids.push(this.multipleSelect[i].id);
      }
      if (ids.length > 0) {
        this.handleInvalidCode(ids);
      } else {
        this.$message({
          type: "error",
          message: "请先选择需要作废的优惠券码",
        });
      }
    },
    selectable(row) {
      if (row.couponStatus !== 0) {
        return true;
      } else {
        return false;
      }
    },
    // 码库 - 单选时调用
    select(selection, row) {
      this.isSelect = true;
      let d = false;
      for (let i = 0; i < this.multipleSelect.length; i++) {
        if (this.multipleSelect[i].id === row.id) {
          this.multipleSelect.splice(i, 1);
          d = true;
          break;
        }
      }
      if (!d) {
        this.multipleSelect.push(row);
        this.multipleSelect = this.setArr(this.multipleSelect);
      }
    },
    // 码库 - 全选时调用
    selectAll(selection) {
      this.isSelect = true;
      if (selection.length === 0) {
        this.codeData.forEach((row) => {
          for (let i = 0; i < this.multipleSelect.length; i++) {
            if (this.multipleSelect[i].id === row.id) {
              this.multipleSelect.splice(i, 1);
              break;
            }
          }
        });
      } else {
        this.multipleSelect = this.setArr(
          this.multipleSelect.concat(selection)
        );
      }
    },
    // 码库 - 当切换页面时的作用
    handleSelectionChange(val) {
      if (
        val.length === 0 &&
        this.multipleSelect.length != 0 &&
        !this.isSelect
      ) {
        this.multipleSelect.forEach((row1) => {
          this.codeData.forEach((row2) => {
            if (row1.id === row2.id) {
              this.$refs.multipleSelect.toggleRowSelection(row2);
            }
          });
        });
        this.isSelect = true;
      }
    },
    setArr(arr) {
      // 去重
      const obj = {};
      const temp = [];
      for (let i = 0; i < arr.length; i++) {
        const type = Object.prototype.toString.call(arr[i].id); // 不加类型 分不清 1 '1'
        if (!obj[arr[i].id + type]) {
          temp.push(arr[i]);
          obj[arr[i].id + type] = true; // 这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
        }
      }
      return temp;
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-table .cell {
  overflow: hidden;
  white-space: nowrap;
}

.category-list {
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
.coupon-wrap{
    .el-dialog__body{
      padding: 20px 40px;
      .el-form{
        max-width:800px;
        margin-bottom:20px;
        .el-form-item{
          margin-bottom:15px;
          /deep/.el-form-item__label{
            display: inline-block;
            width:25% !important;
          }
          /deep/.el-form-item__content{
            display: inline-block;
            width:60%;
            margin-left:10px !important;
          }
        }
      }
    }
    .check_btn {
      margin-left: 46%;
      margin-top: 10px;
    }
    .check_back_btn {
      margin-top: 10px;
    }
  }
</style>