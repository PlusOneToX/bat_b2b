<!--
 * @Author: yaowei
 * @Date: 2018-05-11 15:38:31
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-16 10:37:21
-->
<template>
  <div class="quick-order-wrap">
    <!--公共头部-->
    <Header :userState="userState"></Header>

    <!-- 主内容 -->
    <div class="quick-order-content">
      <h6
        class="quick-order-title rl-padding-top-default rl-padding-bottom-default"
      >{{$t('P.QuickOrder')}}</h6>
      <div class="query-wrap rl-padding-top-default rl-padding-bottom-default rl-clear">
        <div class="query-l rl-fl">
          <el-select
            v-model="pageInfo.classify"
            :placeholder="$t('P.pleaseSelect')"
            @change="getSubClassify"
            class="rl-margin-right-xxxs"
          >
            <el-option
              v-for="item in classifyList"
              :key="item.id"
              :label="($i18n.locale === 'zh' || !item.nameEn == true) ? item.name : item.nameEn"
              :value="item.id"
            ></el-option>
          </el-select>
          <el-select
            v-model="pageInfo.subClassify"
            :placeholder="$t('P.pleaseSelect')"
            class="rl-margin-right-xxxs"
          >
            <el-option
              v-for="item in subClassifyList"
              :key="item.id"
              :label="($i18n.locale === 'zh' || !item.nameEn == true) ? item.name : item.nameEn"
              :value="item.id"
            ></el-option>
          </el-select>
          <el-select v-model="pageInfo.type" :placeholder="$t('P.pleaseSelect')">
            <el-option
              v-for="item in proType"
              :key="item.id"
              :label="($i18n.locale === 'zh' || !item.nameEn == true) ? item.name : item.nameEn"
              :value="item.id"
            ></el-option>
          </el-select>
        </div>
        <div class="query-r rl-fr">
          <el-input
            v-model="pageInfo.content"
            :placeholder="$t('QuickOrder.SearchPlaceholder')"
            class="rl-margin-right-xxxs"
          ></el-input>
          <el-button class="mini-search-btn">{{$t('P.Search')}}</el-button>
        </div>
      </div>

      <div @click="changeTu" class="rl-fr rl-text-xxs zaitu rl-clear cursor-pointer">
        <span class="rl-fl" :class="{'gou':tuType === true}"></span>
        {{$t('P.ConTran')}}
      </div>
      <el-table :data="tableData">
        <el-table-column :label="$t('ShopCarts.Picture')">
          <template slot-scope="scope">
            <img :src="scope.row" alt />
          </template>
        </el-table-column>
        <el-table-column :label="$t('ShopCarts.ItemName')">
          <template
            slot-scope="scope"
          >{{$i18n.locale === 'zh' ? scope.row.itemName : scope.row.itemNameEn}}</template>
        </el-table-column>
        <el-table-column :label="($i18n.locale === 'zh') ? '商品编码/条形码' : 'Code'">
          <template slot-scope="scope">
            <span class="code">{{ scope.row.itemCode}}</span>
            <br />
            <span class="code">{{ scope.row.barCode }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('ShopCarts.Spe')">
          <template slot-scope="scope">
            <el-tooltip
              v-show="$i18n.locale === 'zh' || !scope.row.specificationValueNameEn == true"
              effect="dark"
              :content="scope.row.specificationValueName"
              placement="bottom"
            >
              <el-button>
                <span>{{ scope.row.specificationValueName }}</span>
              </el-button>
            </el-tooltip>
            <el-tooltip
              v-show="$i18n.locale === 'en'"
              effect="dark"
              :content="scope.row.specificationValueNameEn"
              placement="bottom"
            >
              <el-button>
                <span>{{ scope.row.specificationValueNameEn }}</span>
              </el-button>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column :label="$t('ShopCarts.Colors')">
          <template
            slot-scope="scope"
          >{{$i18n.locale === 'zh' ? scope.row.colorValueName : scope.row.colorValueNameEn}}</template>
        </el-table-column>
        <el-table-column :label="$t('ShopCarts.MemPrice')">
          <template slot-scope="scope" v-if="!userId === ''">
            <div v-if="!userId == ''">
              <span v-if="Number(scope.row.salePrice) !== 0 && scope.row.salePrice !== null">
                <i
                  v-if="$i18n.locale === 'en' && $root.currency === 'USD'"
                >${{ scope.row.salePriceEn }}</i>
                <i v-else>￥{{ scope.row.salePrice }}</i>
              </span>
              <span
                v-else
              >{{ scope.row.salePrice === undefined || scope.row.salePrice === null ? !userId == "" ? $t("P.Calculating") : $t("P.ViewAfterLogin") : $t("P.NoPricing") }}</span>
            </div>
            <span v-else>{{ $t("P.ViewAfterLogin") }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('QuickOrder.PriceAndMOQ')"></el-table-column>
        <el-table-column :label="$t('P.Inventory')" prop="stockItemCount"></el-table-column>
        <el-table-column :label="$t('ShopCarts.Quantity')"></el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="rl-margin-top-double rl-tr" v-if="tableData && totalCount > 20">
        <el-pagination
          background
          @current-change="handleCurrentChange"
          @size-change="fastHandleSizeChange"
          layout="prev, pager, next, jumper"
          :page-size="pageInfo.pagesize"
          :total="totalCount"
        ></el-pagination>
      </div>
    </div>

    <!--公共底部-->
    <Footer></Footer>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
export default {
  name: "QuickOrder",
  components: { Header, Footer },
  data() {
    return {
      userState: 2,
      pageInfo: {
        userId: "",
        classify: "",
        subClassify: "",
        type: "",
        content: "",
        page: 1,
        pagesize: 20,
      },
      classifyList: [], // 分类
      subClassifyList: [], // 子分类
      proType: [
        {
          id: 0,
          name: "新品上市",
          nameEn: "New Product",
        },
        {
          id: 1,
          name: "拼团抢新",
          nameEn: "Group Buying",
        },
      ], // 产品类型
      tuType: false, // 是否包含在途库存
      tableData: [],
      totalCount: 0,
    };
  },
  methods: {
    // 排序
    theSort(property) {
      return function (a, b) {
        var value1 = a[property];
        var value2 = b[property];
        return value1 - value2;
      };
    },
    // 获取分类（一级分类）
    getClassify() {
      this.$api.get(this, "user/category").then((res) => {
        if (res.code === 0) {
          this.classifyList = res.categorys;
          this.classifyList.sort(this.theSort("sort")); // 按分类从小到大排序
          if (this.$i18n.locale === "zh") {
            this.pageInfo.classify = this.classifyList[0].name;
          } else {
            this.pageInfo.classify = this.classifyList[0].nameEn;
          }

          this.getSubClassify(this.classifyList[0].id);
        }
      });
    },
    // 获取分类（二级分类）
    getSubClassify(id) {
      this.$api.get(this, "user/category", { parentId: id }).then((res) => {
        if (res.code === 0) {
          this.subClassifyList = res.categorys;
          if (this.$i18n.locale === "zh") {
            this.pageInfo.subClassify = this.subClassifyList[0].name;
          } else {
            this.pageInfo.subClassify = this.subClassifyList[0].nameEn;
          }
        }
      });
    },
    // 在途
    changeTu() {
      this.tuType = !this.tuType;
    },
    // 分页
    handleCurrentChange(val) {
      this.pageInfo.page = val;
    },
    handleSizeChange(val) {
      this.pageInfo.pagesize = val;
    },
    // 获取数据
    getTableData() {},
  },
  created() {
    this.getClassify();
    this.getTableData();
  },
};
</script>

<style lang="less" scoped>
@import url("../assets/less/variable.less");
.quick-order-content {
  margin: 30px auto 0;
  padding: 0 35px 35px;
  width: @center;
  border: 2px solid @bdLightColor;
  border-radius: 8px;
  box-sizing: border-box;
  .quick-order-title {
    border-bottom: 1px solid @bdLightColor;
    font-size: 16px;
    color: @lightBlack;
  }
  .query-wrap {
    /deep/ .el-input__inner {
      width: 140px;
      height: 38px;
      font-size: 12px;
      border: 1px solid @bdLightColor;
      border-radius: 4px;
    }
    .el-input {
      width: 220px;
      /deep/ .el-input__inner {
        width: 100%;
      }
    }
    .mini-search-btn {
      padding: 0 20px;
      height: 38px;
      line-height: 38px;
    }
  }
  /deep/ .el-table {
    top: 5px;
    &::before {
      background-color: @white;
    }
    th {
      padding: 0;
      height: 45px;
      line-height: 45px;
      font-size: 12px;
      color: @lightBlack;
      font-weight: normal;
      background-color: @lightGrayBg;
      &.is-leaf {
        border-bottom: none;
      }
    }
    td {
      height: 70px;
      border-bottom: 1px dashed @bdColor;
    }
  }
}
// 在途
.zaitu {
  height: 20px;
  span {
    display: block;
    position: relative;
    top: 3px;
    width: 16px;
    height: 20px;
    background: url("../assets/images/choose.png") no-repeat center top;
    &.gou {
      background: url("../assets/images/gou.png") no-repeat center top;
    }
  }
}
</style>