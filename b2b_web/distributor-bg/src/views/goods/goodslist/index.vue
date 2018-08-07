<template>
  <div class="category-goods-list">
    <header>
      <h4>商品列表</h4>
      <el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="addpuls">添加新商品</el-button>
      <!-- <el-button class="mini-add-btn btn-home" icon="el-icon-download" @click="exportPrice">导出商品</el-button> -->
    </header>

    <div class="nav clearfix">
      <el-form :inline="true">
        <!------搜索内容------->
        <button
          type="info"
          class="mini-search-btn box_btn"
          size="mini"
          @click.prevent="filter()"
        >搜索</button>
        <el-input
          class="box_input"
          size="mini"
          placeholder="商品名称/商品编号/货品编号/条形码"
          clearable
          @change="changeContent"
          @keyup.enter.native="filter()"
          v-model.trim="content"
        ></el-input>
        <el-select
          class="content_select"
          placeholder="选择类型"
          size="mini"
          v-model="contentType"
          clearable
        >
          <el-option
            v-for="item in contentTypes"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
        <el-tooltip content="同步ERP货品信息" placement="bottom">
          <el-button
            :loading="syncLoading"
            class="mini-batch-btn ERP_btn"
            size="mini"
            @click.prevent="syncGoodsItem"
          >同步货品信息</el-button>
        </el-tooltip>
        <el-tooltip content="同步ERP货品装箱信息" placement="bottom">
          <el-button
            :loading="syncBoxLoading"
            class="mini-batch-btn ERP_btn"
            size="mini"
            @click.prevent="syncGoodsItemBox"
          >同步货品装箱信息</el-button>
        </el-tooltip>
        <el-tooltip content="同步ERP销售价目表价格" placement="bottom">
          <el-button
            :loading="syncPriceLoading"
            class="mini-batch-btn"
            size="mini"
            @click.prevent="syncGoodsItemPrice"
          >同步货品价格</el-button>
        </el-tooltip>
        <!------商品分类------->
        <el-select
          class="custom_select"
          placeholder="商品分类"
          size="mini"
          v-model="pageInfo.classifyId"
          clearable
        >
          <el-option
            :key="item.id"
            :label="item.label"
            :value="item.id"
            v-for="item in categoryList"
          >
            <div v-if="item.parentId === 0">{{item.label}}</div>
            <div v-else>&nbsp;&nbsp;&nbsp;├{{item.label}}</div>
          </el-option>
        </el-select>
        <!------品牌------->
        <el-select
          class="custom_select"
          placeholder="品牌"
          size="mini"
          v-model="pageInfo.brandId"
          clearable
        >
          <el-option
            :key="item.id"
            :label="item.name"
            :value="item.id"
            v-for="item in brandList"
          ></el-option>
        </el-select>
        <!------品类------->
        <el-select
          class="custom_select"
          placeholder="品类"
          size="mini"
          v-model="pageInfo.categoryId"
          clearable
        >
          <el-option
            :key="item.id"
            :label="item.name"
            :value="item.id"
            v-for="item in productlineList"
          ></el-option>
        </el-select>
        <!------上架状态------->
        <el-select
          class="custom_select"
          placeholder="上架状态"
          size="mini"
          v-model="pageInfo.saleStatus"
          clearable
        >
          <el-option
            v-for="item in state"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
        <!------商品类型------->
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
      </el-form>
    </div>
    <div>
      <el-table
        :data="tableData"
        @selection-change="handleSelectionChange"
        border
        header-row-class-name="header-row"
        ref="multipleSelect"
        class="tableCenter"
        v-loading="loading"
        :height="tableHeight"
      >
        <el-table-column label="商品编号" align="center" prop="goodsNo" fixed="left" :width="150"></el-table-column>
        <el-table-column
          label="商品名称"
          align="center"
          show-overflow-tooltip
          prop="goodsName"
          :min-width="140"
        ></el-table-column>
        <el-table-column
          label="商品分类"
          align="center"
          show-overflow-tooltip
          prop="categoryName"
          :min-width="120"
        ></el-table-column>
        <el-table-column
          :formatter="formatStatus"
          label="上架状态"
          prop="saleStatus"
          align="center"
          :min-width="120"
        ></el-table-column>
        <el-table-column label="品牌" prop="brandName" align="center" :min-width="120"></el-table-column>
        <el-table-column
          label="类型"
          prop="goodsType"
          align="center"
          :min-width="120"
          :formatter="formatGoodsType"
        ></el-table-column>
        <el-table-column
          :formatter="timeFormat"
          align="center"
          label="首次上架时间"
          prop="saleTime"
          :min-width="160"
        ></el-table-column>
        <el-table-column label="操作" :width="290" align="center">
          <template slot-scope="scope">
            <el-button
              @click="handleShelve(scope.row,1)"
              class="mini-pulloff-btn"
              v-if="scope.row.saleStatus == 3"
            >下架</el-button>
            <el-button
              @click="handleShelve(scope.row,3)"
              class="mini-browse-btn"
              v-if="scope.row.saleStatus == 1"
            >上架</el-button>
            <el-button @click="handleEdit(scope.row)" class="mini-search-btn">查看</el-button>
            <!-- <el-button @click="handleDelete(scope.row)" class="mini-freeze-btn"> 删除 </el-button> -->
            <el-button
              @click="handleFreeze(scope.row)"
              class="mini-freeze-btn"
              v-if="scope.row.saleStatus == 1"
            >冻结</el-button>
            <el-button @click="handleWindow(scope.row)" class="mini-browse-btn">浏览</el-button>
          </template>
        </el-table-column>
      </el-table>
      <page
        :page="pageInfo.page"
        :total="total"
        @sizeChange="sizeChange"
        @currentChange="currentChange"
      ></page>
    </div>

    <!-- 等级价格明细dialog -->
    <el-dialog :modal-append-to-body="false" :visible="priceShow" :before-close="closePriceShow">
      <el-table
        :data="priceData"
        border
        header-row-class-name="header-row"
        class="tableCenter"
        :max-height="600"
      >
        <el-table-column align="center" label="等级" prop="gradeName"></el-table-column>
        <el-table-column align="center" label="价格" prop="salePrice"></el-table-column>
      </el-table>
    </el-dialog>

    <!-- 库存明细dialog -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="warehouseShow"
      :before-close="closeWarehouseShow"
    >
      <el-table
        :data="warehouseDatav"
        border
        header-row-class-name="header-row"
        class="tableCenter"
        :max-height="600"
      >
        <el-table-column align="center" label="仓库名" prop="warehouseName"></el-table-column>
        <el-table-column align="center" label="在库数量" prop="numInWarehouse"></el-table-column>
      </el-table>
    </el-dialog>

    <!-- 导出报价单dialog -->
    <el-dialog
      width="60%"
      :modal-append-to-body="false"
      :visible="exportPriceShow"
      :before-close="closeExportPriceShow"
    >
      <el-form ref="form" :model="form" label-width="120px">
        <el-form-item label="报价单版本：">
          <el-radio-group v-model="form.language">
            <el-radio label="zh_CNY">中文版</el-radio>
            <el-radio label="en_USD">英文版</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="选择字段：">
          <div class="export-price">
            <div class="all-file">
              <el-checkbox
                :indeterminate="isAllIndeterminate"
                v-model="isAll"
                @change="handleCheckAllChange"
              >全部选择</el-checkbox>
            </div>
            <div class="file1">
              <el-checkbox
                :indeterminate="isBaseIndeterminate"
                @change="handleCheckAllBaseChange"
                v-model="isBase"
              >基本信息</el-checkbox>
              <el-checkbox-group
                style="padding-left: 25px;"
                v-model="baseInfos"
                @change="handleCheckedBaseChange"
              >
                <el-checkbox label="barCode">条形码</el-checkbox>
                <el-checkbox label="itemCode">存货编码</el-checkbox>
                <el-checkbox label="goodsNo">商品编码</el-checkbox>
                <el-checkbox label="goodsName">商品名称</el-checkbox>
                <el-checkbox label="brandName">品牌</el-checkbox>
                <el-checkbox label="goodsType">商品类型</el-checkbox>
                <el-checkbox label="categoryName">商品分类</el-checkbox>
                <el-checkbox label="imageUrl1">商品图片</el-checkbox>
                <el-checkbox label="imageUrl5">包装图</el-checkbox>
                <el-checkbox label="model">型号</el-checkbox>
                <el-checkbox label="color">颜色</el-checkbox>
                <el-checkbox label="specName">规格</el-checkbox>
                <el-checkbox label="series">系列</el-checkbox>
                <el-checkbox label="appliedDevice">应用设备</el-checkbox>
                <el-checkbox label="outerBoxNum">装箱数量</el-checkbox>
                <el-checkbox label="moq">最小起订量</el-checkbox>
                <el-checkbox label="purchaseCycle">购买周期</el-checkbox>
                <el-checkbox label="productMethod">生产方式</el-checkbox>
                <el-checkbox label="weight">重量</el-checkbox>
                <el-checkbox label="stockNum">库存</el-checkbox>
                <el-checkbox label="numOnWay">在途库存</el-checkbox>
                <el-checkbox label="sizeStr">尺寸</el-checkbox>
                <el-checkbox label="saleStatus">上架状态</el-checkbox>
                <el-checkbox label="gradeLevelName">货品等级</el-checkbox>
                <el-checkbox label="saleTime">上架时间</el-checkbox>
                <el-checkbox label="innerBox">内箱规格</el-checkbox>
                <el-checkbox label="outerBox">外箱规格</el-checkbox>
              </el-checkbox-group>
              <el-checkbox
                :indeterminate="isPriceIndeterminate"
                @change="handleCheckAllPriceChange"
                v-model="isPrice"
              >价格相关</el-checkbox>
              <el-checkbox-group
                style="padding-left: 25px;"
                @change="handleCheckPriceChange"
                v-model="prices"
              >
                <el-checkbox label="retailPrice">建议零售价</el-checkbox>
                <el-checkbox label="wholesalePrice">建议批发价</el-checkbox>
                <el-checkbox label="gradleOnePrice">一级价</el-checkbox>
                <el-checkbox label="gradleTwoPrice">二级价</el-checkbox>
              </el-checkbox-group>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="选择页码：">
          <el-radio-group v-model="isAllExportGoods">
            <el-radio :label="1">全部导出</el-radio>
            <el-radio :label="2">分页导出</el-radio>
          </el-radio-group>
          <el-pagination
            v-if="isAllExportGoods === 2"
            style="margin-top: 5px;"
            background
            layout="total, sizes, prev, pager, next"
            :page-sizes="[50, 100, 200]"
            @current-change="changeFormPage"
            @size-change="changeFormCount"
            :total="exportPriceItemCount"
          ></el-pagination>
        </el-form-item>
      </el-form>

      <div style="text-align: center;">
        <el-button class="mini-search-btn" @click="exportGoodsPrice()">确认</el-button>
        <el-button
          style="margin-left:20px"
          class="mini-pulloff-btn"
          @click="closeExportPriceShow"
        >取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import page from "@/components/pagination";
import {
  getGoodsList,
  syncGoodsItem,
  syncGoodsItemPrice,
  syncCollect,
  clearBrandCategory
} from "./data/goodslistManage.js";
import { timeFormat } from "@/utils/timeFormat";
import api from  '@/api/allApi'
export default {
  name: "goodslist",
  components: { page },
  created() {
    const documentHeight = document.body.clientHeight;
    this.tableHeight = parseInt(documentHeight * 0.8 - 85); // 计算表高度，固定表头
    this.baseInfos = this.initBaseInfosZH;
    // this.getLastChoose(2);
  },
  activated() {
    this.dataFot();
    clearBrandCategory();
  },
  data() {
    return {
      tableHeight: 600,
      exportPriceItemCount: 0,
      noCache: false,
      loading: false,
      syncLoading: false,
      syncBoxLoading: false,
      syncPriceLoading: false,
      syncCollectLoading: false,
      page: 1,
      form: {
        page: 1,
        count: 50,
        language: "zh_CNY",
        classifyId: "",
        brandId: "",
        categoryId: "",
        freezeStatus: 1,
        saleStatus: "",
        goodsType: "",
        content: "",
        exportFields: ""
      },
      isAllExportGoods: 1,
      exportFields: [],
      pageInfo: {
        page: 1,
        size: 10,
        freezeStatus: 1
      },
      categoryId: [],
      pageInfos: {
        page: 1,
        size: 10000,
        openFlag: 1
      },
      state: [
        { value: 1, label: "已下架" },
        { value: 3, label: "已上架" },
        { value: 2, label: "审批中" },
      ],
      goodsTypes: [
        { value: 1, label: "普通" },
        { value: 2, label: "新定制" }
      ],
      contentTypes: [
        { value: 1, label: '商品名称' },
        { value: 2, label: '商品编号' },
        { value: 3, label: '货品编号' },
        { value: 4, label: '条形码' }
      ],
      total: 0,
      totals: 0,
      categoryList: [],
      brandId: "",
      brandList: [],
      productlineId: "",
      productlineList: [],
      contentType: 3,
      content: "",
      tableData: [],
      multipleSelect: [],
      action: "",
      priceData: [],
      priceShow: false,
      warehouseData: [],
      warehouseShow: false,
      warehouseDatav: [],
      checkHint: "",
      checkSuccess: "",
      needCheckFlag: 0,
      exportPriceShow: false,
      isAll: false,
      isBase: false,
      isPrice: false,
      isAllIndeterminate: false,
      isBaseIndeterminate: false,
      isPriceIndeterminate: false,
      allBaseInfos: [
        "barCode",
        "itemCode",
        "goodsNo",
        "goodsName",
        "brandName",
        "goodsType",
        "categoryName",
        "imageUrl1",
        "imageUrl5",
        "model",
        "color",
        "specName",
        "series",
        "appliedDevice",
        "outerBoxNum",
        "moq",
        "purchaseCycle",
        "productMethod",
        "weight",
        "stockNum",
        "numOnWay",
        "sizeStr",
        "saleStatus",
        "gradeLevelName",
        "saleTime",
        "innerBox",
        "outerBox"
      ],
      allPrices: [
        "wholesalePrice",
        "retailPrice",
        "gradleOnePrice",
        "gradleTwoPrice"
      ],
      initBaseInfosZH: [
        "barCode",
        "itemCode",
        "imageUrl1",
        "imageUrl5",
        "color",
        "moq",
        "purchaseCycle",
        "wholesalePrice",
        "retailPrice",
        ,
        "gradleOnePrice",
        "gradleTwoPrice"
      ],
      initBaseInfosEN: [
        "barCode",
        "itemCode",
        "imageUrl1",
        "imageUrl5",
        "model",
        "color",
        "specName",
        "series",
        "appliedDevice",
        "outerBoxNum",
        "moq",
        "purchaseCycle",
        "wholesalePrice",
        "retailPrice",
        ,
        "gradleOnePrice",
        "gradleTwoPrice"
      ],
      baseInfos: [],
      prices: []
    };
  },
  methods: {
    getLastChoose(recordType) {
      this.$api
        .get(this, api.getLastChoose, {
          recordType: recordType
        })
        .then(res => {
          if (
            res.code === 0 &&
            res.recordList !== undefined &&
            res.recordList !== null &&
            res.recordList.length > 0
          ) {
            res.recordList.forEach(item => {
              if (item.recordType === 2) {
                // 后台报价单
                this.prices = [];
                this.baseInfos = [];
                let arr = item.exportFields.split(",");
                if (this.form.language === "en_USD") {
                  this.initBaseInfosEN = arr;
                } else {
                  this.initBaseInfosZH = arr;
                }
              }
            });
          }
          this.getBaseInfos();
        });
    },
    getBaseInfos() {
      let arr = null;
      this.prices = [];
      this.baseInfos = [];
      if (this.form.language === "en_USD") {
        arr = this.initBaseInfosEN;
      } else {
        arr = this.initBaseInfosZH;
      }
      arr.forEach(field => {
        if (
          field === "retailPrice" ||
          field === "gradleOnePrice" ||
          field === "gradleTwoPrice" ||
          field === "wholesalePrice"
        ) {
          this.prices.push(field);
        } else {
          this.baseInfos.push(field);
        }
      });
    },
    changeFormPage(val) {
      this.form.page = val;
    },
    changeFormCount(val) {
      this.form.count = val;
    },
    exportGoodsPrice() {
      if (this.isAllExportGoods === 1) {
        this.form.page = undefined;
        this.form.count = undefined;
      } else {
        if (this.form.page === undefined) {
          this.form.page = 1;
        }
        if (this.form.count === undefined) {
          this.form.count = 50;
        }
      }
      this.form.classifyId = this.pageInfo.classifyId;
      this.form.brandId = this.pageInfo.brandId;
      this.form.categoryId = this.pageInfo.categoryId;
      this.form.goodsType = this.pageInfo.goodsType;
      this.form.saleStatus = this.pageInfo.saleStatus;
      this.form.content = this.pageInfo.content;
      this.form.exportFields = "";
      if (this.baseInfos.length > 0) {
        this.form.exportFields = this.baseInfos.join(",");
      }
      if (this.prices.length > 0) {
        if (this.form.exportFields !== "") {
          this.form.exportFields =
            this.form.exportFields + "," + this.prices.join(",");
        } else {
          this.$message.warning("存货编码必选");
          return;
        }
      }
      if (this.form.exportFields != undefined && this.form.exportFields != 0) {
        this.exportPriceShow = false;
        this.$message.success(
          "报价单导出请求提交成功，数据量可能较大，数据下载期间，请勿关闭浏览器，但您可同时做其他事项，谢谢！"
        );
        this.$api
          .exportData(this, api.exportPriceExcel, this.form)
          .then(res => {
            if (res === undefined || res.code == undefined) {
              this.timeData = []; //..成功下载Excel后初始化选中时间
              // this.formData.status = [] //..成功夏侯Excel后初始化订单状态
            }
            const content = res;
            // res.replace(/%/g, '%25');
            let blob = new Blob([content], {
              type: "application/ms-excel"
            });
            let url = window.URL.createObjectURL(blob);
            if ("download" in document.createElement("a")) {
              let link = document.createElement("a");
              link.style.display = "none";
              link.href = url;
              link.setAttribute("download", "报价单明细.xls");
              document.body.appendChild(link);
              link.click();
            } else {
              navigator.msSaveBlob(blob, "报价单明细.xls");
            }
          });
      } else {
        this.$message.warning("请先选择需要导出的字段");
      }
    },
    handleCheckAllChange(val) {
      this.isAllIndeterminate = false;
      this.baseInfos = val ? this.allBaseInfos : [];
      this.prices = val ? this.allPrices : [];
      this.isBase = val;
      this.isBaseIndeterminate = false;
      this.isPrice = val;
      this.isPriceIndeterminate = false;
    },
    handleCheckAllBaseChange(val) {
      this.baseInfos = val ? this.allBaseInfos : [];
      this.isBase = val;
      this.isBaseIndeterminate = false;
      this.isAllIndeterminate = !(
        (this.baseInfos.length === this.allBaseInfos.length &&
          this.prices.length === this.allPrices.length) ||
        (this.baseInfos.length === 0 && this.prices.length === 0)
      );
      this.isAll =
        this.baseInfos.length === this.allBaseInfos.length &&
        this.prices.length === this.allPrices.length;
    },
    handleCheckedBaseChange(value) {
      this.isBase = value.length === this.allBaseInfos.length;
      this.isBaseIndeterminate =
        value.length > 0 && value.length < this.allBaseInfos.length;
      this.isAllIndeterminate = !(
        (this.baseInfos.length === this.allBaseInfos.length &&
          this.prices.length === this.allPrices.length) ||
        (this.baseInfos.length === 0 && this.prices.length === 0)
      );
      this.isAll =
        this.baseInfos.length === this.allBaseInfos.length &&
        this.prices.length === this.allPrices.length;
    },
    handleCheckAllPriceChange(val) {
      this.prices = val ? this.allPrices : [];
      this.isPrice = val;
      this.isPriceIndeterminate = false;
      this.isAllIndeterminate = !(
        (this.baseInfos.length === this.allBaseInfos.length &&
          this.prices.length === this.allPrices.length) ||
        (this.baseInfos.length === 0 && this.prices.length === 0)
      );
      this.isAll =
        this.baseInfos.length === this.allBaseInfos.length &&
        this.prices.length === this.allPrices.length;
    },
    handleCheckPriceChange(value) {
      this.isPrice = value.length === this.allPrices.length;
      this.isPriceIndeterminate =
        value.length > 0 && value.length < this.allPrices.length;
      this.isAllIndeterminate = !(
        (this.baseInfos.length === this.allBaseInfos.length &&
          this.prices.length === this.allPrices.length) ||
        (this.baseInfos.length === 0 && this.prices.length === 0)
      );
      this.isAll =
        this.baseInfos.length === this.allBaseInfos.length &&
        this.prices.length === this.allPrices.length;
    },
    changeContent(val) {
      if (val === undefined || val === "" || val === null) {
        this.filter();
      }
    },
    dataFot() {
      // 获取所有商品分类
      this.$http.getAllClassifyPoList(this).then(res => {
        let arr = []
        for(let i = 0;i<res.length;i++){
          arr.push(res[i])
          if(res[i].children != undefined && res[i].children.length >0){
            res[i].children.forEach(item => {
              arr.push(item)
            })
          }
        }
        this.categoryList = arr;
      })

      // 获取所有品类
      this.$http.getCategoryPoList(this, this.pageInfos).then(res => {
        if (res.success) {
          this.productlineList = res.data.list
        }
      })
      
      // 获取所有品牌列表
      this.$http.getBrandPoList(this, this.pageInfos).then(res => {
        if (res.success) {
          this.brandList = res.data.list
        }
      })
      // 获取所有未冻结商品列表
      this.getTableData();
    },

    syncGoodsItem() {
      //同步货品操作
      this.syncLoading = true;
      // syncGoodsItem(this).then(res => {
      this.$http.syncAllItem(this).then(res => {  
        this.syncLoading = false;
        if (res.success) {
          this.$message({
            message: "同步货品信息成功",
            type: "success",
            duration: 3 * 1000
          });
        }
      });
    },

    syncGoodsItemBox() {
      //同步货品装箱操作
      this.syncBoxLoading = true;
      this.$http.syncAllItemBox(this).then(res => {  
        this.syncBoxLoading = false;
        if (res.success) {
          this.$message({
            message: "同步货品装箱信息成功",
            type: "success",
            duration: 3 * 1000
          });
        }
      });
    },

    syncGoodsItemPrice() {
      this.syncPriceLoading = true;
      this.$http.syncAllPrice(this).then(res => {    
        this.syncPriceLoading = false;
        if (res.success) {
          this.$message({
            message: "同步价格成功",
            type: "success",
            duration: 3 * 1000
          });
        }
      });
    },

    handleWindow(row) {
      // 跳转浏览商品
      var host = window.location.host;
      if (host === "admin.bat.com") {
        // 正式
        window.open('https://www.bat.com/#/ShopDetail?good_id='+row.id+'&goodsType='+row.goodsType+'&accessType=1')
      } else {
        // 测试
        window.open("https://test.bat.com/proscenium/#/ShopDetail?good_id=" +row.id +"&goodsType=" +row.goodsType +"&accessType=1");
      }
    },

    addpuls() {
      // 添加商品操作
      this.$router.push({ name: "addgoods" });
    },

    exportPrice() {
      // 导出报价单
      this.exportPriceShow = true;
    },

    sizeChange(size) {
      // 分页
      this.pageInfo.size = size;
      this.getTableData()
    },

    currentChange(page) {
      // 分页
      this.pageInfo.page = page;
      this.getTableData()
    },
    // 获取所有未冻结商品列表
    getTableData() {
      // 主要数据
      this.$http.getGoodsListP(this, this.pageInfo).then(res => {
        this.tableData = res.tableData.list;
        this.total = res.tableData.total;
        this.exportPriceItemCount = res.tableData.total
        // this.needCheckFlag = res.needCheckFlag;
        // if (this.needCheckFlag === 0) {
          this.checkHint = "此操作将改变选中商品的上架状态，是否继续?";
          this.checkSuccess = "上下架成功";
        // } else {
        //   this.checkHint =
        //     "此操作将改变选中商品的上架状态，更改商品上下架状态需通过审批才能生效，是否继续?";
        //   this.checkSuccess = "上下架成功，需要管理员审批通过方可生效";
        // }
      });
      // this.getExportPriceItemCount();
    },
    // getExportPriceItemCount() {
    //   this.form.classifyId = this.pageInfo.classifyId;
    //   this.form.brandId = this.pageInfo.brandId;
    //   this.form.categoryId = this.pageInfo.categoryId;
    //   this.form.goodsType = this.pageInfo.goodsType;
    //   this.form.saleStatus = this.pageInfo.saleStatus;
    //   this.form.content = this.content;
    //   this.$api.get(this, api.getGoodsItemCount, this.form).then(res => {
    //     if (res.code == 0) {
    //       this.exportPriceItemCount = res.count;
    //     }
    //   });
    // },

    filter() {
      // 搜索操作
      if (this.pageInfo.classifyId === -1 || this.pageInfo.classifyId === "") {
        this.pageInfo.classifyId = undefined;
      }
      if (this.pageInfo.brandId === -1 || this.pageInfo.brandId === "") {
        this.pageInfo.brandId = undefined;
      }
      if (
        this.pageInfo.categoryId === -1 ||
        this.pageInfo.categoryId === ""
      ) {
        this.pageInfo.categoryId = undefined;
      }
      if (this.pageInfo.saleStatus === -1 || this.pageInfo.saleStatus === "") {
        this.pageInfo.saleStatus = undefined;
      }
      this.pageInfo.contentType = this.contentType;
      this.pageInfo.content = this.content;
      if (this.page == this.pageInfo.page) {
        this.pageInfo.page = 1;
      }
      this.page = this.pageInfo.page;
      this.getTableData();
    },

    timeFormat(row, col, val) {
      // 时间戳转换
      if (val === 0) {
        return "-";
      } else {
        return timeFormat(val);
      }
    },

    formatStatus(row, col, val) {
      // 上架状态
      switch (val) {
        case '1':
          return "下架";
          break;
        case '2':
          return "审批中";
          break;
        case '3':
          return "上架";
          break;
      }
    },
    formatGoodsType(row, col, val) {
      //商品类型
      switch (val) {
        case 1:
          return "普通";
          break;
        case 2:
          return "新定制";
          break;
      }
    },

    closePriceShow() {
      // 等级价格明细dialog默认为隐藏
      this.priceShow = false;
    },

    closeWarehouseShow() {
      // 库存明细dialog默认为隐藏
      this.warehouseShow = false;
    },

    closeExportPriceShow() {
      // 导出报价单dialog默认为隐藏
      this.exportPriceShow = false;
    },
    handleSelectionChange(val) {
      // 勾选中的值
      this.multipleSelect = val;
    },

    handleDelete(row) {
      // 删除操作
      this.$confirm("此操作将永久删除该商品, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true
      }).then(() => {
        this.$http.deleteGoods(this, { id: row.id }).then(res => {  
          if (res.success) {
            this.$message({
              message: "删除成功",
              type: "success",
              duration: 3 * 1000,
              onClose: () => {}
            });
            this.getTableData();
          }
        });
      });
    },

    handleEdit(row) {
      // 查看操作
      this.$router.push({
        name: "addgoods",
        query: { id: row.id, saleStatus: row.saleStatus }
      });
    },

    handleFreeze(row) {
      // 冻结操作
      this.$confirm("此操作将改变该商品的冻结状态, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true
      }).then(() => {
        this.$http.freezeStatus(this, { id: row.id, freezeStatus: 2 }).then(res => {
          if (res.success) {
            this.$message({
              message: "更改成功,请到冻结商品列表查看已冻结商品",
              type: "success",
              duration: 3 * 1000
            })
            this.getTableData()
          }
        })
      });
    },

    handleShelve(row, type) {
      // 上下架操作
      // if (this.needCheckFlag === 0) {
        if (type === 1) {
          this.checkSuccess = "下架成功";
        } else if (type === 3) {
          this.checkSuccess = "上架成功";
        }
      // } else {
      //   if (type === 1) {
      //     this.checkSuccess = "下架提交成功，需要管理员审批通过方可生效";
      //   } else if (type === 3) {
      //     this.checkSuccess = "上架提交成功，需要管理员审批通过方可生效";
      //   }
      // }
      this.$confirm(this.checkHint, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true
      }).then(() => {
          this.$http.goodsSaleStatus(this, { id: row.id, saleStatus: type }).then(res => {
            if (res.success) {
              this.$message({
                message: this.checkSuccess,
                type: "success",
                duration: 3 * 1000,
                onClose: () => {}
              })
              this.getTableData()
            }
          })
      });
    }
  },
  watch: {
    "form.language": {
      handler() {
        this.getLastChoose(2);
      },
      deep: true
    },
    pageInfo: {
      handler() {
        this.filter();
      },
      deep: true
    }
  }
};
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.el-table .cell {
  overflow: hidden;
  white-space: nowrap;
  text-align: center !important;
}
.el-table {
  /deep/.cell{
    white-space: pre;
  }
}
.custom_select {
  width: 120px;
}
.category-goods-list {
  background-color: white;
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
    // position: absolute;
    // bottom: auto;
    // top: expression(eval(document.documentElement.scrollTop));
  }
  h4 {
    display: inline-block;
    font-weight: 350;
    font-size: 16px;
    margin: 0 20px;
  }
  .btn-home {
    float: right;
    margin-top: 8px;
    margin-right: 8px;
    margin-left: 0;
  }
  .nav {
    padding: 10px 0;
    .box_btn {
      float: right;
      margin-left: 5px;
      margin-right: 10px;
    }
    .box_input {
      float: right;
      width: 200px;
    }
    .ERP_btn {
      margin-left: 10px;
    }
    .content_select{
      float:right;
      width: 120px;
      /deep/.el-input{
        .el-input__inner{
          border-top-right-radius: 0;
          border-bottom-right-radius: 0;
        }
      }
    }
    .box_input{
      /deep/.el-input__inner{
        border-top-left-radius: 0;
        border-bottom-left-radius: 0;
        border-left: none;
      }
    }
  }
  .dialog-table {
    width: 100%;
  }
  .export-price {
    background-color: #eef8fa;
    .all-file {
      border-bottom: 1px solid #d2d2d2;
      padding-left: 10px;
      padding-right: 10px;
    }
    .file1 {
      padding-left: 10px;
      padding-right: 10px;
    }
  }
}
</style>
