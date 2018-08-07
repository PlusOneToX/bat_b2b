<!--
 * @Author: yaowei
 * @Date: 2018-03-23 14:08:15
 * @LastEditors: yaowei
 * @LastEditTime: 2018-03-24 10:00:41
-->
<template>
  <div class="category-goods-list">
    <header>
      <h4>商品销量管理</h4>
    </header>
    <div class="nav">
      <div class="Fheader">
        <el-select
          placeholder="商品分类"
          clearable
          size="mini"
          v-model.trim="pageInfo.categoryId"
          style="margin-left: 10px; width: 130px"
        >
          <el-option
            :key="item.id"
            :label="item.name"
            :value="item.id"
            v-for="item in categoryList"
          >
            <div v-if="item.parentId === 0">{{ item.name }}</div>
            <div v-else>&nbsp;&nbsp;&nbsp;├{{ item.name }}</div>
          </el-option>
        </el-select>

        <el-select
          placeholder="品牌"
          clearable
          size="mini"
          v-model="pageInfo.brandId"
          style="width: 130px"
        >
          <el-option
            :key="item.id"
            :label="item.name"
            :value="item.id"
            v-for="item in brandList"
          >
          </el-option>
        </el-select>

        <el-select
          class="custom_select"
          placeholder="商品类型"
          size="mini"
          v-model="pageInfo.goodsType"
          clearable
        >
          <el-option
            v-for="item in goodsTypes"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>

        <el-button
          type="info"
          class="mini-search-btn"
          @click.prevent="filter()"
          style="float: right; margin-left: 5px; margin-right: 10px"
          >搜索</el-button
        >
        <el-input
          style="float: right; width: 250px"
          size="mini"
          @keyup.enter.native="filter()"
          placeholder="商品名称/商品编号"
          v-model.trim="content"
        ></el-input>
      </div>
    </div>

    <div>
      <el-table
        :data="tableData"
        border
        header-row-class-name="header-row"
        ref="multipleSelect"
        class="tableCenter"
        v-loading="loading"
        :height="tableHeight"
      >
        <el-table-column
          label="商品编号"
          align="center"
          :min-width="150"
          prop="goodsNo"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          label="商品名称"
          align="center"
          :min-width="210"
          prop="goodsName"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          label="真实销量"
          align="center"
          :min-width="120"
          prop="saleCount"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span>{{ scope.row.saleCount ? scope.row.saleCount : 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="销量显示为"
          align="center"
          :min-width="120"
          prop="showType"
        >
          <template slot-scope="scope">
            <span v-if="Number(scope.row.showType) === 1">虚拟销量</span>
            <span v-else>真实销量</span>
          </template>
        </el-table-column>
        <el-table-column
          label="虚拟销量基数"
          prop="virtualNum"
          align="center"
          :min-width="120"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.virtualNum ? scope.row.virtualNum : "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="前台显示销量" align="center" :min-width="120">
          <template slot-scope="scope">
            <span>{{
              Number(scope.row.saleCount) + Number(scope.row.virtualNum)
            }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="最后更新时间"
          prop="updateTime"
          align="center"
          :min-width="180"
          :formatter="timeFormat"
        >
        </el-table-column>
        <el-table-column label="操作" align="center" :width="100">
          <template slot-scope="scope">
            <el-button
              @click="handleSetting(scope.row)"
              class="mini-search-btn"
            >
              设置
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <page
        :page="pageInfo.page"
        :total="total"
        @currentChange="currentChange"
        @sizeChange="sizeChange"
      >
      </page>
    </div>

    <el-dialog
      title="销量设置"
      :modal-append-to-body="false"
      :visible="setSalesShow"
      :before-close="handleCloseSetting"
      width="480px"
    >
      <el-form
        ref="virtualSales"
        :model="virtualSales"
        :rules="virtualRules"
        label-width="120px"
      >
        <el-form-item label="销量显示为：">
          <el-radio-group v-model="virtualSales.showType">
            <el-radio :label="0">真实数量</el-radio>
            <el-radio :label="1">虚拟销量</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          label="虚拟销量基数："
          v-show="Number(virtualSales.showType) === 1"
          prop="virtualNum"
        >
          <el-input
            size="mini"
            v-model.number="virtualSales.virtualNum"
            placeholder="请输入合理数值"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="setSalesShow = false">返回</el-button>
        <el-button class="mini-search-btn box-btn" @click="handleConfirmSetting"
          >确定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>
<script>
import page from "@/components/pagination";
import { timeFormat } from "@/utils/timeFormat";

import { getList, editSales } from "./data.js";
export default {
  name: "salesManage",
  components: { page },
  data() {
    return {
      tableHeight: 600,
      loading: false,
      pageInfo: {
        page: 1,
        count: 10,
        categoryId: "",
        brandId: "",
        goodsTypes: "",
      },
      page: 1,
      total: 0,
      categoryList: [],
      brandList: [],
      goodsTypes: [
        { value: 1, label: "普通" },
        { value: 2, label: "新定制" },
      ],
      content: "",
      tableData: [],
      setSalesShow: false, //销量设置弹窗
      //销量设置
      virtualSales: {
        showType: 1,
        virtualNum: "",
      },
      curGoodsId: "", //当前设置商品
      virtualRules: {
        virtualNum: [
          {
            validator: (rule, value, callback) => {
              if (
                Number(this.virtualSales.showType) === 1 &&
                Number(value) === 0
              ) {
                callback(new Error("请输入虚拟销量基数"));
              } else {
                callback();
              }
            },
          },
        ],
      },
    };
  },
  created() {
    const documentHeight = document.body.clientHeight;
    this.tableHeight = parseInt(documentHeight * 0.8 - 100); // 计算表高度，固定表头
  },
  activated() {
    // 获取所有分类级联
    this.$http.getClassifyPoList(this, {page:1, size:10000, openFlag: 1}).then(res => {
      this.categoryList = res.tableData
      this.loading = false;
    })
    // 获取所有品牌列表
    this.$http.getBrandPoList(this, {page:1, size:10000, openFlag: 1}).then(res => {
      if (res.success) {
        this.brandList = res.data.list
      }
    })
    this.getTableData();
  },
  methods: {
    filter() {
      // 搜索
      if (this.page === this.pageInfo.page) {
        this.pageInfo.page = 1;
      }
      this.page = this.pageInfo.page;
      this.getTableData();
    },
    getTableData() {
      // 主数据
      this.loading = true;
      this.pageInfo.content = this.content;
      getList(this, this.pageInfo).then((res) => {
        this.tableData = res.pageInfo.list;
        this.total = res.pageInfo.total;
        res.code == 0 ? (this.loading = false) : (this.loading = false);
      });
    },
    sizeChange(size) {
      // 总条数
      this.pageInfo.count = size;
    },
    currentChange(page) {
      // 分页
      this.pageInfo.page = page;
    },
    timeFormat(row, col, val) {
      // 时间格式化
      return timeFormat(val);
    },
    // 销量设置
    handleSetting(row) {
      this.setSalesShow = true;
      this.curGoodsId = row.goodsId;
      this.virtualSales.showType = row.showType;
      if (Number(row.virtualNum) !== 0) {
        this.virtualSales.virtualNum = row.virtualNum;
      } else {
        this.virtualSales.virtualNum = "";
      }
    },
    handleCloseSetting() {
      this.setSalesShow = false;
      // 重置
      this.virtualSales.showType = 1;
      this.virtualSales.virtualNum = "";
    },
    handleConfirmSetting() {
      if (this.virtualSales.showType !== 1) {
        this.virtualSales.virtualNum = "";
      }
      this.virtualSales.goodsId = this.curGoodsId;
      this.$refs["virtualSales"].validate((valid) => {
        if (!valid) {
          return false;
        } else {
          editSales(this, this.virtualSales).then((res) => {
            if (res.code == 0) {
              this.getTableData();
            }
          });
          this.setSalesShow = false;
        }
      });
    },
  },
  watch: {
    pageInfo: {
      handler() {
        this.filter();
      },
      deep: true,
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-table .cell {
  overflow: hidden;
  white-space: nowrap;
}
.category-goods-list {
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
  }
  .header {
    background-color: $lakeBlue;
    color: white;
    padding-left: 20px;
  }
  .nav {
    background-color: white;
    .search {
      float: right;
    }
    .box-search {
      width: 120px;
    }
    .Fheader {
      margin: 10px auto;
      margin-left: 0;
      overflow: hidden;
    }
  }
  .header-row {
    @include table-header-color;
    min-width: 100px;
  }
}
</style>
