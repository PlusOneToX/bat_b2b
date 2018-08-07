<template>
  <div>
    <div class="user rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState" ref="header"></Header>
      <!--主内容-->
      <div class="user-main rl-clear rl-margin-zero">
        <!--公共左边-->
        <Left></Left>
        <div
          class="
            user-right
            rl-fr rl-bg-white rl-margin-top-default rl-padding-bottom-double
          "
        >
          <div class="order-list-wrap">
            <div class="user-right-title2">
              <h6>分销商</h6>
            </div>
            <div class="query rl-padding-top-default">
              <div class="search rl-clear">
                <!-- 商户名称 -->
                <!-- <div class="item rl-fl rl-margin-right-xxs">
                  <input type="text" v-model.trim="orderNo" placeholder="商户名称"/>
                </div> -->
                <!-- 商户名称 -->
                <!-- <div class="item rl-fl rl-margin-right-xxs">  
                  <input type="text" v-model.trim="name" placeholder="商户名称"/>
                </div> -->
                <!-- 手机号 -->
                <!-- <div class="item rl-fl rl-margin-right-xxs">
                  <input type="text" v-model.trim="name" placeholder="手机号"/>
                </div> -->
                <!-- 查询订单 -->
                <!-- <div class="search-order rl-fl">
                  <el-button
                    @click="queryOrder"
                    class="search-btn rl-text-white">{{ $t("UserCenter.OrderQuery") }}</el-button> 
                </div> -->
              </div>
            </div>

            <div class="query-detail">
              <!-- 订单类型--Y -->
              <div class="nav rl-clear">
                <ul class="rl-fl" :class="$i18n.locale === 'en' ? 'navEn' : ''">
                  <!-- 全部订单 -->
                  <!-- <li :class="applyStatus === 0 ? 'current' : ''"  @click="changeState(0)" >
                   全部
                  </li>  -->
                  <!--待审核 -->
                  <li
                    :class="applyStatus === 1 ? 'current' : ''"
                    @click="changeState(1)"
                  >
                    待审核
                  </li>
                  <!--合作中--->
                  <li
                    :class="applyStatus === 2 ? 'current' : ''"
                    @click="changeState(2)"
                  >
                    合作中
                  </li>
                  <!-- 已拒绝 -->
                  <li
                    :class="applyStatus === 3 ? 'current' : ''"
                    @click="changeState(3)"
                  >
                    已拒绝
                  </li>
                </ul>
              </div>
              <!-- 表格列表数据 --y-->
              <div class="table">
                <table>
                  <tr>
                    <th>商户编号</th>
                    <th>商户名称</th>
                    <th>登录账号</th>
                    <th>联系人</th>
                    <th>手机号</th>
                    <th>申请时间</th>
                    <th>状态</th>
                    <th>价格等级</th>
                    <th>品牌可视</th>
                    <th>商品可视</th>
                    <th>操作</th>
                  </tr>
                  <tr v-if="orderList.length === 0 || totalCount === 0">
                    <td class="empty" colspan="11">{{ $t("P.NoData") }}</td>
                  </tr>
                  <tr
                    v-else
                    class="goods-list"
                    v-for="(order, index) in orderList"
                    :key="index"
                  >
                    <td>{{ order.id }}</td>
                    <td>{{ order.companyName }}</td>
                    <td>{{ order.userName }}</td>
                    <td>{{ order.userName }}</td>
                    <td>{{ order.phone }}</td>
                    <td>{{ order.applyTime }}</td>
                    <td>
                      {{
                        order.applyStatus == 1
                          ? "待审核"
                          : order.applyStatus == 2
                          ? "合作中"
                          : "已拒绝"
                      }}
                    </td>
                    <td>{{ order.scalePriceName }}</td>
                    <td>
                      <div class="views rl-fl rl-margin-left-xxxs">
                        <span v-if="order.applyStatus != 2">-</span>
                        <span
                          v-else
                          @click="nobrandListFun(order.id)"
                          class="log rl-tc cursor-pointer rl-fl"
                          >查看</span
                        >
                      </div>
                    </td>
                    <td class="rl-clear">
                      <div class="views rl-fl rl-margin-left-xxxs">
                        <span v-if="order.applyStatus != 2">-</span>
                        <span
                          v-else
                          @click="getNoGoodsDataList(order.id)"
                          class="log rl-tc cursor-pointer rl-fl"
                          >查看</span
                        >
                      </div>
                    </td>
                    <td class="rl-clear">
                      <div class="views rl-fl rl-margin-left-xxxs">
                        <span
                          @click="auditFun(order.id)"
                          class="log rl-tc cursor-pointer rl-fl"
                          v-if="order.applyStatus == 1"
                          >审核</span
                        >
                        <span
                          @click="levelSetFun(order.id)"
                          class="log rl-tc cursor-pointer rl-fl"
                          v-if="order.applyStatus == 2"
                          >设置</span
                        >
                      </div>
                    </td>
                  </tr>
                </table>
              </div>
            </div>
          </div>
          <!-- 分页 -->
          <div class="apply rl-clear">
            <div class="rl-tr rl-margin-top-default">
              <el-pagination
                v-if="this.totalCount > 0"
                background
                :current-page.sync="cur_page"
                @current-change="handleCurrentChange"
                @size-change="handleSizeChange"
                layout="prev, pager, next, jumper"
                :page-size="pagesize"
                :total="totalCount"
              ></el-pagination>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!--品牌可视--->
    <el-dialog class="alls" title="品牌可视" :visible.sync="brandDialogVisible">
      <div class="brandDialog-box">
        <div class="export-price">
          <div class="all-file">
            <el-checkbox
              label="0"
              v-model="allBrandChecked"
              class="om-orderStatus-all"
              @change="brandAllChangeFun"
              >全部</el-checkbox
            >
          </div>
          <div class="file">
            <el-checkbox-group
              v-model="selectBrandList"
              @change="brandChangeFun"
            >
              <el-checkbox
                :label="item.id"
                v-for="item in brandListData"
                :key="item.id"
                class="randDialog-box-checkBox"
                >{{ item.name }}</el-checkbox
              >
            </el-checkbox-group>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="brandSaveFun">保存</el-button>
        <el-button @click="brandDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>

    <!--商品可视--->
    <el-dialog class="alls" title="商品可视" :visible.sync="goodDialogVisible">
      <div class="goodDialog-div">
        <ul>
          <li class="goodDialog-div-li1">
            <el-checkbox v-model="allIsVisual" @change="goodAllChangeFun"
              >全选</el-checkbox
            >
          </li>
          <li
            class="goodDialog-div-li2"
            v-for="item in goodList"
            :key="item.id"
          >
            <el-checkbox
              v-model="item.isVisual"
              @change="goodOneChangeFun($event, item.id)"
            ></el-checkbox>
            <div class="goodDialog-div-name">{{ item.goodsName }}</div>
          </li>
        </ul>
      </div>

      <div class="goodDialog-div-pagination">
        <el-pagination
          v-if="this.goodTotalPage > 0"
          background
          :current-page.sync="goodPage"
          @current-change="goodCurrentChange"
          layout="prev, pager, next, jumper"
          :page-size="10"
          :total="goodTotalPage"
        ></el-pagination>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="goodSaveFun">保存</el-button>
        <el-button @click="goodDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>

    <!--设置等级价--->
    <el-dialog class="alls" title="设置" :visible.sync="levelDialogVisible">
      <div class="levelDialog-box">
        <div class="levelDialog-box-line">
          <span class="levelDialog-box-span">价格等级：</span>
          <el-select v-model="selectLevelVal" placeholder="请选择">
            <el-option
              v-for="item in levelList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </div>
        <div class="levelDialog-box-line">
          <span class="levelDialog-box-span">品牌可视：</span>
          <span class="levelDialog-box-span2" @click="brandDataListFun"
            >点击前往设置</span
          >
        </div>

        <div class="levelDialog-box-line">
          <span class="levelDialog-box-span">商品可视：</span>
          <span class="levelDialog-box-span2" @click="getGoodsList"
            >点击前往设置</span
          >
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="bindPickerChange">保存</el-button>
        <el-button @click="levelDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>

    <!--加载动画-->
    <loading v-if="this.showLoading === true"></loading>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import Left from "@/components/Left.vue";
import { formatDate, dateToNum } from "@/assets/js/common.js";
import onceAgain from "@/components/onceAgain.vue";
import GD from "@/assets/js/globalData";
import loading from "@/components/loading.vue";
import countDown from "@/components/countDown.vue";
import {
  userNextList,
  distributorNextCheck,
  brandList,
  nobrandList,
  goodsListD,
  nogoodsList,
  scalepriceList,
  adjustScaleprice,
} from "@/apiService/api";
export default {
  components: { Header, Left, onceAgain, loading, countDown },
  data() {
    return {
      cur_page: 1,
      pagesize: 10,
      userState: 2,
      showLoading: false,
      totalCount: 0,
      orderList: [],
      applyStatus: 1, //状态
      noBrandListData: [], //不可视品牌列表
      brandListData: [], //品牌可视列表
      brandDialogVisible: false, //品牌弹框可视
      selectBrandList: [], //选择的可视品牌
      noSelectBrandList: [], //取消可视
      allBrandChecked: false, //品牌可视全选
      goodDialogVisible: false, //商品可视弹框
      goodList: [], //商品列表
      goodDataList: [],
      noGoodList: [], //不可视商品
      goodPage: 1, //商品分页
      goodTotalPage: 0, //商品总页数
      allIsVisual: false, //商品全选
      distrbutionId: "",
      // 等级价设置
      levelList: [{ id: 0, name: "默认等级价" }],
      levelDialogVisible: false,
      selectLevelVal: "",

      name: "",
      orderNo: "",
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: "zh-RMB", // 语种
      exchange: 1, //汇率
    };
  },
  filters: {
    formatDate(time) {
      var date = new Date(time);
      return formatDate(date, "yyyy-MM-dd hh:mm:ss");
    },
  },
  created() {
    this.exchange = Number(localStorage.getItem("exchange"));
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem("bLang")
      ? window.localStorage.getItem("bLang")
      : "zh-RMB";
    this.getOrderList();
    this.brandListFun();
    this.scalepriceListFun();
  },
  methods: {
    // 获取订单列表
    getOrderList(startTime, endTime) {
      this.orderList = [];
      let userId = localStorage.getItem("userId");
      let params = {
        applyStatus: this.applyStatus == 0 ? "" : this.applyStatus, //订单状态 0.全部 1审核中 2合作中 3已拒绝
        distributorId: userId,
        page: this.cur_page,
        size: this.pagesize,
      };
      userNextList(params).then((res) => {
        if (res.success) {
          console.log("订单列表：", res.data.list);
          this.orderList = res.data.list;
          this.totalCount = res.data.total;
        }
      });
    },
    // 改变订单状态（查询）--Y
    changeState(type) {
      this.applyStatus = type;
      this.cur_page = 1;
      this.getOrderList();
    },
    // 审核
    auditFun(id) {
      this.$confirm("请审核分销商的资料，确认是否通过审核?", "提示", {
        confirmButtonText: "同意",
        cancelButtonText: "拒绝",
        type: "warning",
      })
        .then(() => {
          let params = {
            applyStatus: 2, // 2 审核通过
            id: id,
          };
          distributorNextCheck(params).then((res) => {
            if (res.success) {
              this.$message("审核通过");
              this.getOrderList();
            } else {
              this.$message(res.errMessage);
            }
          });
        })
        .catch(() => {
          let params = {
            applyStatus: 3, // 3 审核拒绝
            id: id,
          };
          distributorNextCheck(params).then((res) => {
            if (res.success) {
              this.$message("审核拒绝成功！");
              this.getOrderList();
            } else {
              this.$message(res.errMessage);
            }
          });
        });
    },

    // 根据分销商id获取品牌可视
    brandListFun() {
      brandList({ distributorId: localStorage.getItem("userId") }).then(
        (res) => {
          this.brandListData = res.data;
          res.data.forEach((item) => {
            this.selectBrandList.push(item.id);
          });
        }
      );
    },

    // 品牌数据整合
    brandDataListFun() {
      this.brandDialogVisible = true;
      this.selectBrandList = [];
      nobrandList("GET", { distributorId: this.distrbutionId }).then((res) => {
        let list = res.data;
        let list2 = this.brandListData;
        for (let i = 0; i < list2.length; i++) {
          let flag = true;
          for (let j = 0; j < list.length; j++) {
            if (list2[i].id == list[j]) {
              flag = false;
            }
          }
          if (flag) {
            this.selectBrandList.push(list2[i].id);
          }
        }
        this.brandChangeFun();
      });
    },

    // 获取品牌不可视
    nobrandListFun(id) {
      this.distrbutionId = id;
      this.brandDataListFun();
    },

    // 品牌可视--全选
    brandAllChangeFun(val) {
      let list = this.brandListData;
      let arr = [];
      for (let i = 0; i < list.length; i++) {
        arr.push(list[i].id);
      }
      this.selectBrandList = val ? arr : [];
      this.noSelectBrandList = val ? [] : arr;
    },

    // 品牌可视--单选
    brandChangeFun() {
      let lengthVal = this.selectBrandList.length;
      let lengthVal2 = this.brandListData.length;
      if (lengthVal == lengthVal2) {
        this.allBrandChecked = true;
      } else {
        this.allBrandChecked = false;
      }
      let list1 = this.brandListData;
      let list2 = this.selectBrandList;
      for (let i = 0; i < list1.length; i++) {
        let flag = true;
        for (let j = 0; j < list2.length; j++) {
          if (list1[i].id == list2[j]) {
            flag = false;
          }
        }
        if (flag) {
          this.noSelectBrandList.push(list1[i].id);
        }
      }
    },

    // 设置品牌可视
    brandSaveFun() {
      let params = {
        brandIds: this.noSelectBrandList,
        distributorId: this.distrbutionId,
      };
      nobrandList("POST", params).then((res) => {
        if (res.success) {
          this.$message("设置品牌成功");
          this.brandDialogVisible = false;
          this.getOrderList();
        } else {
          this.$message(res.errMessage);
        }
      });
    },

    // 获取商品
    async getGoodsList() {
      this.goodDialogVisible = true;
      let that = this;
      let params = {
        distributorId: localStorage.getItem("userId"),
        page: this.goodPage,
        size: 10,
      };
      let res = await goodsListD(params);
      let res2 = await nogoodsList("GET", {
        distributorId: this.distrbutionId,
      });
      if (res.success) {
        that.goodTotalPage = res.data.total;
        that.goodList = res.data.list;
        if (res2.success) {
          let list = res2.data;
          let flag2 = true;
          that.goodList.forEach((item) => {
            if (list.length == 0) {
              this.$set(item, "isVisual", true);
              this.allIsVisual = true;
            } else {
              let flag = true;
              for (let i = 0; i < list.length; i++) {
                if (item.id == list[i]) {
                  flag = false;
                  flag2 = false;
                }
              }
              this.$set(item, "isVisual", flag);
            }
          });
          this.allIsVisual = flag2;
        }
      }
    },

    // 获取下级不可视商品
    getNoGoodsDataList(id) {
      this.distrbutionId = id;
      console.log("分销商id：", id);
      this.getGoodsList();
    },
    // 商品可视--全选
    goodAllChangeFun(val) {
      this.allIsVisual = true;
      this.goodList.forEach((item) => {
        this.$set(item, "isVisual", val);
        if (!item.isVisual) {
          this.allIsVisual = false;
        }
      });
    },

    // 商品可视--单选
    goodOneChangeFun(val, id) {
      console.log(val);
      console.log(id);
      this.goodList.forEach((item) => {
        if (item.id == id) {
          this.$set(item, "isVisual", val);
        }
      });
    },

    // 商品可视分页
    goodCurrentChange(val) {
      this.goodPage = val;
      this.getGoodsList();
    },

    // 设置商品可视--保存
    goodSaveFun() {
      let checkList = [];

      for (let i = 0; i < this.goodList.length; i++) {
        if (!this.goodList[i].isVisual) {
          checkList.push(this.goodList[i].id);
        }
      }
      let params = {
        goodsIds: checkList,
        distributorId: this.distrbutionId,
      };
      nogoodsList("POST", params).then((res) => {
        if (res.success) {
          this.$message("设置商品可视成功");
          this.getGoodsList();
        } else {
          this.$message(res.errMessage);
        }
      });
    },
    // 获取等级价格
    scalepriceListFun() {
      let that = this;
      let id = localStorage.getItem("userId");
      scalepriceList({ distributorId: id }).then((res) => {
        if (res.success) {
          console.log(res.data);
          that.levelList = [...that.levelList, ...res.data];
        }
      });
    },

    // 设置等级价
    levelSetFun(id) {
      this.levelDialogVisible = true;
      this.distrbutionId = id;
      this.selectLevelVal = "";
    },
    // 调整价格价格
    bindPickerChange() {
      let parames = {
        applyStatus: 2,
        id: this.distrbutionId,
        scalePriceId: this.selectLevelVal,
      };

      if (this.selectLevelVal === "") {
        this.$message("请选择价格等级");
        return;
      }
      adjustScaleprice(parames).then((res) => {
        if (res.success) {
          this.$message("价格等级调整成功！");
          this.levelDialogVisible = false;
        } else {
          this.$message(res.errMessage);
        }
      });
    },

    // 当前页码
    handleCurrentChange(val) {
      this.cur_page = val;
      this.getOrderList();
    },
    // 每页条数
    handleSizeChange(val) {
      this.pagesize = val;
      this.getOrderList();
    },

    formatNumber(n) {
      n = n.toString();
      return n[1] ? n : "0" + n;
    },

    formatTime(number) {
      let formateArr = ["Y", "M", "D", "h", "m", "s"];
      let returnArr = [];
      let date = new Date(number);
      let year = date.getFullYear();
      let month = this.formatNumber(date.getMonth() + 1);
      let day = this.formatNumber(date.getDate());
      let hours = this.formatNumber(date.getHours());
      let Minutes = this.formatNumber(date.getMinutes());
      let Seconds = this.formatNumber(date.getSeconds());
      let format =
        year +
        "-" +
        month +
        "-" +
        day +
        " " +
        hours +
        ":" +
        Minutes +
        ":" +
        Seconds;
      return format;
    },

    // 订单查询--Y
    queryOrder() {},
  },
};
</script>

<style scoped="scoped" lang='less'>
@import url("../../assets/less/variable.less");
.levelDialog-box {
  border-top: 1px solid #f3f3f3;
  padding: 20px 0 20px 100px;
  .levelDialog-box-line {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
    .levelDialog-box-span {
      width: 100px;
    }
    .levelDialog-box-span2 {
      color: #409eff;
    }
  }
}
.goodDialog-div {
  padding-left: 5%;
  border-top: 1px solid #f3f3f3;
  padding-top: 15px;
  ul {
    border: 1px solid #f3f3f3;
    width: 90%;
    .goodDialog-div-li1 {
      background: #eef8fa;
      padding: 10px 15px;
    }
    .goodDialog-div-li2 {
      display: flex;
      align-items: center;
      padding: 10px 15px;
      .goodDialog-div-name {
        margin-left: 10px;
        width: 95%;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
  }
}
.goodDialog-div-pagination {
  margin-left: 30px;
  margin-top: 15px;
}
.om-orderStatus-checkbox {
  display: flex;
  .om-orderStatus-all {
    margin-right: 15px;
  }
}
.brandDialog-box {
  height: 300px;
  padding: 30px;
  .all-file {
    border-bottom: 1px solid #d2d2d2;
    padding: 10px;
  }
  .file {
    padding: 10px;
  }
  .randDialog-box-checkBox {
    margin-top: 10px;
  }
}
.user {
  width: 100%;
}
.user-main {
  width: 1200px;
}
.user-right {
  width: 1030px;
  .user-right-title {
    padding-bottom: 10px;
    border-bottom: 1px solid @bdLightColor;
    font-size: 20px;
  }
  .user-right-title2 {
    padding-bottom: 10px;
    border-bottom: 1px solid @bdLightColor;

    display: flex;
    align-items: center;
    justify-content: space-between;
    h6 {
      font-size: 20px;
    }
  }
  .order-list-wrap {
    padding: 24px 40px 0;
    border: 2px solid @bdLightColor;
    border-radius: 8px;
  }
  .query {
    margin-bottom: 25px;
    .search {
      .item {
        input[type="text"] {
          padding-left: 10px;
          width: 140px;
          height: 38px;
          font-size: 12px;
          box-sizing: border-box;
          border: 1px solid @bdLighterColor;
          border-radius: 4px;
        }
        input[type="button"] {
          padding: 0 12px;
          height: 38px;
          line-height: 38px;
          box-sizing: border-box;
          border-radius: 4px;
        }
      }
      .select {
        /deep/ .el-input__inner {
          width: 180px;
        }
      }
    }
    .search-info {
      .items {
        .common-input {
          input {
            padding-left: 10px;
            width: 140px;
            height: 38px;
            font-size: 12px;
            box-sizing: border-box;
            border: 1px solid @bdLighterColor;
            border-radius: 4px;
          }
          &.addr-input {
            input {
              width: 180px;
            }
          }
        }
        &.date-items {
          line-height: 40px;
        }
      }
      /deep/ .el-date-editor.el-input,
      .el-date-editor.el-input__inner {
        width: 200px;
      }
    }
    /deep/ .el-input__inner {
      height: 38px;
      font-size: 12px;
      border: 1px solid @bdLighterColor;
      border-radius: 4px;
    }
  }
  button {
    padding: 0 16px;
    height: 40px;
    line-height: 40px;
    border: none;
  }
  .search-btn {
    background-color: @blue;
    &:hover,
    &:active,
    &:focus {
      color: @white;
      opacity: 0.6;
    }
  }
  .query-detail {
    .nav {
      margin-bottom: 15px;
      width: 100%;
      height: 40px;
      line-height: 40px;
      ul {
        overflow: hidden;
        background-color: @bdLightColor;
        border-radius: 4px;
        &.navEn {
          li {
            width: auto;
            padding: 0 12px;
            font-size: 13px;
          }
        }
        li {
          float: left;
          width: 93px;
          cursor: pointer;
          font-size: 14px;
          color: @lighterBlack;
          text-align: center;
          &:hover,
          &.current {
            color: @white;
            background-color: @blue;
          }
        }
      }
    }
    .export-btn {
      padding-top: 24px;
      color: @blue;
      line-height: 1;
      cursor: pointer;
      .iconfont {
        margin-right: 2px;
        color: @lighterGray;
      }
      &:hover {
        opacity: 0.6;
      }
    }
    .table {
      width: 100%;
      margin-bottom: 30px;
      table {
        width: 100%;
        word-wrap: break-word;
        word-break: break-all;
        border-collapse: collapse;
        tr {
          &.goods-list:hover {
            background-color: @lightGrayBg;
          }
          & + tr {
            border-top: 1px dashed @bdLighterColor;
          }
          th {
            height: 30px;
            line-height: 30px;
            text-align: center;
            background-color: @bdLightColor;
            font-size: 12px;
            color: @gray;
            font-weight: normal;
          }
          td {
            height: 50px;
            text-align: center;
            font-size: 12px;
            color: @lightBlack;
            &.empty {
              width: 100%;
              font-size: 16px;
              text-align: center;
            }
            .views {
              min-width: 65px;
              span {
                display: block;
              }
              .log {
                width: 100%;
                font-size: 12px;
                color: @blue;
                &:hover {
                  opacity: 0.6;
                }
              }
            }
            .countdown-box {
              width: 100%;
              margin-top: 12px;
              text-align: left;
              box-sizing: border-box;
            }
          }
        }
      }
    }
  }
  .apply {
    .after-sale {
      z-index: 11;
      width: 70px;
      height: 30px;
      line-height: 30px;
      border-radius: 5px;
    }
  }
}
/*弹框*/
.cover {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: #000;
  z-index: 99;
  opacity: 0.5;
}
.pro-cover {
  width: 555px;
  height: 350px;
  border: 1px solid #ccc;
  border-radius: 5px;
  z-index: 99;
  background: #fefefe;
}
.cover-box {
  box-sizing: border-box;
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  margin: auto;
  z-index: 99;
  .shut {
    position: absolute;
    top: -8px;
    right: -8px;
    display: block;
    width: 18px;
    height: 18px;
    background: url("../../assets/images/shut.png") no-repeat center center;
  }
}
/*再来一单*/
.max-height300 {
  max-height: 300px;
}
.shop-table {
  margin-bottom: 20px;
  overflow-y: scroll;
  table {
    tr {
      td {
        height: 45px;
        text-align: center;
        border-bottom: 1px solid #ccc;
        .buy-sum {
          width: 92px;
          height: 22px;
          line-height: 22px;
          border: 1px solid #ebeff5;
          div {
            height: 22px;
            box-sizing: border-box;
            background-color: #fff;
            input {
              width: 62px;
            }
          }
          .buyac {
            width: 22px;
            font-size: 22px;
            color: #9b9b9b;
            cursor: pointer;
            text-align: center;
          }
          .buyb {
            width: 48px;
            line-height: 22px;
            color: #3a3a3a;
            border-left: 1px solid #ebeff5;
            border-right: 1px solid #ebeff5;
            input {
              width: 46px;
            }
          }
        }
        .songImg {
          width: 50px;
          img {
            margin-top: 5px;
            height: 50px;
          }
        }
      }
    }
  }
}
.export-price {
  border: 1px solid transparent;
  background-color: #eef8fa;
  .all-file {
    border-bottom: 1px solid #d2d2d2;
    padding-left: 10px;
    padding-right: 10px;
  }
  .file {
    padding-left: 10px;
    padding-right: 10px;
  }
}
</style>
<style lang='less'>
.el-dialog__wrapper {
  .el-dialog {
    width: 600px;
    .el-dialog__header {
      text-align: center;
    }
    .el-dialog__body {
      padding: 0;
      padding-bottom: 10px;
    }
    .el-dialog__footer {
      text-align: center;
    }
  }
}
</style>
