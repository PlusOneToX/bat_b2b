<!--
 * @Author: yaowei
 * @Date: 2018-05-16 14:47:06
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-08 12:26:20
-->
<template>
  <div class="list-wrap">
    <header>
      <h4>营销专题活动</h4>
      <el-button
        class="mini-add-btn btn-home"
        icon="el-icon-plus"
        @click="handleList(1)"
        >创建营销专题活动</el-button
      >
    </header>

    <div class="function">
      <div class="Fheader">
        <div class="Fleft">
          <el-select
            size="mini"
            v-model="pageInfo.type"
            placeholder="活动类型"
            style="width: 100px"
            @change="Csearch()"
            clearable
          >
            <el-option label="转发活动" :value="1"></el-option>
          </el-select>
          <el-select
            size="mini"
            v-model="pageInfo.activityPlatform"
            placeholder="所属平台"
            style="width: 100px"
            @change="Csearch()"
            clearable
          >
            <el-option label="兑换商城" :value="1"></el-option>
            <el-option label="定制商城" :value="2"></el-option>
          </el-select>
        </div>
        <div class="Fsearch">
          <button class="mini-search-btn box-btn" @click="Csearch()">
            搜索
          </button>
          <el-input
            v-model.trim="searchContent"
            size="mini"
            clearable
            @change="contentChange"
            @keyup.enter.native="Csearch()"
            placeholder="请输入活动标题/B2B编码"
            class="box-input"
          ></el-input>
          <el-select
            size="mini"
            v-model="contentType"
            style="width: 100px"
            @change="Csearch()"
            clearable
          >
            <el-option label="活动标题" :value="1"></el-option>
            <el-option label="B2B编码" :value="2"></el-option>
          </el-select>
        </div>
      </div>
    </div>

    <el-row v-loading="loading">
      <el-table
        :data="tableData"
        header-row-class-name="header-row"
        border
        class="tableCenter"
      >
        <el-table-column
          label="活动ID"
          align="center"
          prop="id"
          :min-width="100"
        ></el-table-column>
        <el-table-column
          label="专题活动标题"
          align="center"
          prop="title"
          show-overflow-tooltip
          :min-width="150"
        ></el-table-column>
        <el-table-column
          label="活动所属平台"
          align="center"
          show-overflow-tooltip
          :min-width="120"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.activityPlatform === 1">兑换商城</span>
            <span v-else-if="scope.row.activityPlatform === 2">定制商城</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column
          label="发卡分销商"
          align="center"
          show-overflow-tooltip
          :min-width="110"
        >
          <template slot-scope="scope">
            <span class="check" @click="handleDistri(scope.row.id)">查看</span>
          </template>
        </el-table-column>
        <el-table-column
          label="结束时间"
          align="center"
          prop="endTime"
          show-overflow-tooltip
          :min-width="180"
        >
        </el-table-column>
        <el-table-column label="状态" align="center" width="90">
          <template slot-scope="scope">
            <span v-if="scope.row.timeStatus === 1">进行中</span>
            <span v-else>已过期</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" :min-width="280" align="center">
          <template slot-scope="scope">
            <el-button
              class="mini-search-btn"
              @click="handleList(2, scope.row.id)"
              >查看</el-button
            >
            <el-button
              v-if="
                Number(scope.row.timeStatus) === 1 &&
                Number(scope.row.status) === 1
              "
              class="mini-freeze-btn"
              @click="handleStatus(scope.row.id, 0)"
              >停用</el-button
            >
            <el-button
              v-if="
                Number(scope.row.timeStatus) === 1 &&
                Number(scope.row.status) !== 1
              "
              class="mini-tableadd-btn"
              @click="handleStatus(scope.row.id, 1)"
              >启用</el-button
            >
            <el-button
              class="mini-search-btn"
              @click="handleDistri(scope.row.id)"
              >二维码及短链接</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <page
        :total="total"
        :page="pageInfo.page"
        @sizeChange="sizeChange"
        @currentChange="currentChange"
      ></page>
    </el-row>

    <!-- 查看发卡分销商/二维码及短链接 -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="distriShow"
      :before-close="distriCancel"
      width="80%"
    >
      <div class="search-wrap">
        <el-button
          style="float: right; margin-left: 10px"
          class="mini-search-btn"
          @click="filter"
          >搜索</el-button
        >
        <el-input
          size="mini"
          style="width: 20%; float: right"
          v-model="distriPageInfo.distributorId"
          placeholder="B2B编码"
        ></el-input>

        <!-- <el-button
          style="float: right; margin-right: 10px"
          class="mini-search-btn"
          >导出全部</el-button
        > -->
      </div>
      <el-table
        :data="distriData"
        header-row-class-name="header-row"
        border
        class="tableCenter"
      >
        <el-table-column
          label="分销商用户名"
          align="center"
          prop="distributorName"
          :min-width="150"
        ></el-table-column>
        <el-table-column
          label="B2B编码"
          align="center"
          prop="distributorId"
          show-overflow-tooltip
          :min-width="100"
        ></el-table-column>
        <el-table-column
          label="页面访问次数"
          align="center"
          prop="pageVisits"
          show-overflow-tooltip
          :min-width="120"
        >
        </el-table-column>
        <el-table-column
          label="一次/二次转发次数"
          align="center"
          prop="forwardTimes"
          show-overflow-tooltip
          :min-width="150"
        >
          <template slot-scope="scope">
            <span>{{
              scope.row.oneForwardTimes + "/" + scope.row.twoForwardTimes
            }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="福利领取次数"
          align="center"
          prop="receiveTimes"
          show-overflow-tooltip
          :min-width="120"
        >
        </el-table-column>
        <el-table-column
          label="链接"
          align="center"
          prop="link"
          show-overflow-tooltip
          :min-width="180"
        >
        </el-table-column>
        <el-table-column label="分享链接" :min-width="320" align="center">
          <template slot-scope="scope">
            <el-button
              v-show="Number(scope.row.status) === 1"
              class="mini-search-btn"
              @click="downloadQrcode(scope.row.id)"
              >下载二维码</el-button
            >
            <el-button
              v-show="Number(scope.row.status) === 1"
              class="mini-tableadd-btn"
              @click="handleCopyUrl(1, scope.row.link)"
              >专题链接</el-button
            >
            <el-button
              v-show="Number(scope.row.status) === 1"
              class="mini-tableadd-btn"
              @click="handleCopyUrl(2, scope.row.id)"
              >专题短链</el-button
            >
            <span v-show="Number(scope.row.status) !== 1">停用</span>
          </template>
        </el-table-column>
      </el-table>
      <page
        :total="distriTotal"
        :page="distriPageInfo.page"
        @sizeChange="distriSizeChange"
        @currentChange="currentDistriChange"
      ></page>
    </el-dialog>
  </div>
</template>

<script type="text/javascript">
import page from "@/components/pagination";
import { timeFormat } from "@/utils/timeFormat.js";
export default {
  name: "exchangeSpecial",
  components: {
    page,
  },
  data() {
    return {
      loading: false,
      pageInfo: {
        page: 1,
        size: 10,
        type: 1, // 活动类型
        activityPlatform: 1, // 所属平台
        title: "", // 活动标题
        distributorId: "", // B2B编码
      },
      searchContent: "",
      contentType: 1, // 搜索内容类型
      tableData: [], // 列表
      total: "", // 列表总数
      // 查看发卡分销商/二维码及短链接
      distriShow: false, // 弹窗显示
      distriData: [], // 表格数据
      distriTotal: "", // 总数
      distriPageInfo: {
        distributorId: "",
        page: 1,
        size: 10,
      },
      exchangeSpecialId: "",
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
      this.$http.exchangeSpecialList(this, this.pageInfo).then((res) => {
        if (res.success) {
          this.tableData = res.data.list;
          this.total = res.data.total;
          let now = new Date();
          for (var i = 0; i < res.data.list.length; i++) {
            this.tableData.forEach((item) => {
              if (item.id === res.data.list[i].id) {
                // 结束时间
                var endTime = timeFormat(res.data.list[i].endTime);
                item.endTime = endTime;

                // 状态
                let end = new Date(Date.parse(item.endTime.replace(/-/g, "/")));
                if (now > end) {
                  // 已下线
                  this.$set(item, "timeStatus", 2);
                } else {
                  // 已上线
                  this.$set(item, "timeStatus", 1);
                }
              }
            });
          }
        }
      });
    },
    // 列表条数
    sizeChange(size) {
      this.pageInfo.size = size;
      this.pageInfo.page = 1;
      this.initData();
    },
    // 列表页数
    currentChange(page) {
      this.pageInfo.page = page;
      this.initData();
    },
    // 搜索列表
    Csearch() {
      this.pageInfo.page = 1;
      this.initData();
    },
    // 输入框输入搜索列表
    contentChange(val) {
      if (val === undefined || val === "" || val === null) {
        this.Csearch();
      }
    },
    // 添加/查看/编辑 - （1，添加，2，查看/编辑）
    handleList(checkMsg, id) {
      var query = {};
      if (id) {
        query = {
          checkMsg: checkMsg,
          id: id,
        };
      } else {
        query = {
          checkMsg: checkMsg,
        };
      }
      this.$router.push({
        name: "cardSpecialActivityDetail",
        query: query,
      });
    },
    // 启用/暂停 （status：1 启用/0 停用）
    handleStatus(id, status) {
      let msg = "";
      if (status === 1) {
        msg = "此操作将启用该营销专题活动，是否继续？";
      } else {
        msg = "此操作将停用该营销专题活动，是否继续？";
      }
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          this.$http
            .editExchangeSpecial(this, {
              id: id,
              status: status,
            })
            .then((res) => {
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

    /* 发卡分销商/二维码及短链接 */
    // 数据
    handleDistri(id) {
      this.exchangeSpecialId = id;
      this.$http
        .exchangeSpecialDistriList(this, {
          exchangeSpecialId: id,
          distributorId: this.distriPageInfo.distributorId,
          page: this.distriPageInfo.page,
          size: this.distriPageInfo.size,
        })
        .then((res) => {
          if (res.success) {
            this.distriData = res.data.list;
            if (this.distriData && this.distriData.length > 0) {
              this.distriData.forEach((distri) => {
                // 分销商详情
                this.getDistributorByIds([distri.distributorId], distri);

                // 专题链接
                this.$http
                  .exchangeSpecialDistriLink(this, { id: distri.id })
                  .then((res) => {
                    if (res.success) {
                      this.$set(distri, "link", res.data);
                    }
                  });
              });
            }
            this.distriTotal = res.data.total;
            this.distriShow = true;
          }
        });
    },
    // 分销商详情
    getDistributorByIds(ids, item) {
      this.$http.getDistributorByIds(this, { ids: ids }).then((res) => {
        if (res.success) {
          if (res.data && res.data.length > 0) {
            let distri = "";
            res.data.forEach((item) => {
              distri += item.name + ",";
            });

            this.$set(
              item,
              "distributorName",
              distri.substring(0, distri.length - 1)
            );
          }
        }
      });
    },
    // 关闭弹窗
    distriCancel() {
      this.distriShow = false;
    },
    // 搜索
    filter() {
      this.handleDistri(this.exchangeSpecialId);
    },
    // 列表条数
    distriSizeChange(size) {
      this.distriPageInfo.size = size;
      this.distriPageInfo.page = 1;
      this.handleDistri(this.exchangeSpecialId);
    },
    // 列表页数
    currentDistriChange(page) {
      this.distriPageInfo.page = page;
      this.handleDistri(this.exchangeSpecialId);
    },
    // 下载二维码
    downloadQrcode(id) {
      this.$http.exchangeSpecialDistriQrcode(this, { id: id }).then((res) => {
        if (res.success) {
          let name = "qrcode";
          var userAgent = navigator.userAgent; // 取得浏览器的 userAgent 字符串
          var isOpera = userAgent.indexOf("Opera") > -1;
          let image = new Image();
          image.src = res.data;
          // 解决跨域 Canvas 污染问题
          image.setAttribute("crossOrigin", "anonymous");
          image.onload = function () {
            let canvas = document.createElement("canvas");
            canvas.width = image.width;
            canvas.height = image.height;
            let context = canvas.getContext("2d");
            context.drawImage(image, 0, 0, image.width, image.height);
            let url = canvas.toDataURL("image/png"); // 得到图片的 base64 编码数据
            if (
              userAgent.indexOf("Trident") > -1 ||
              (userAgent.indexOf("compatible") > -1 &&
                userAgent.indexOf("MSIE") > -1 &&
                !isOpera)
            ) {
              // 判断是否 Edge/IE 浏览器
              var bstr = atob(url.split(",")[1]);
              var n = bstr.length;
              var u8arr = new Uint8Array(n);
              while (n--) {
                u8arr[n] = bstr.charCodeAt(n);
              }
              // 创建blob对象
              var blob = new Blob([u8arr]);
              // 调用浏览器的方法，调起 IE 的下载流程
              window.navigator.msSaveOrOpenBlob(blob, name + ".png");
            } else {
              let a = document.createElement("a"); // 生成一个 a 元素
              let event = new MouseEvent("click"); // 创建一个单击事件
              a.download = name || "photo"; // 设置图片名称
              a.href = url; // 将生成的 URL 设置为 a.href 属性
              a.dispatchEvent(event); // 触发 a 的单击事件
            }
          };
        }
      });
    },
    // 复制专题链接/专题短链
    handleCopyUrl(type, con) {
      if (type === 2) {
        let host = window.location.host;
        let pathDomain = "";
        if (host === "admin.bat.com") {
          // 正式
          pathDomain = "https://api.bat.com/";
        } else {
          // 测试
          pathDomain = "https://test.bat.com/";
        }

        // 专题短链
        this.$http
          .exchangeSpecialDistriShortLink(this, { id: con })
          .then((res) => {
            if (res.success) {
              let link = pathDomain + res.data;
              this.handleCopy(link);
            }
          });
      } else {
        // 专题链接
        this.handleCopy(con);
      }
    },
    // 复制链接
    handleCopy(link) {
      var _input = document.createElement("input");
      _input.value = link;
      document.body.appendChild(_input);
      _input.select();
      document.execCommand("Copy");
      this.$message({
        message: "复制成功",
        type: "success",
      });
      document.body.removeChild(_input);
    },
    /* 发卡分销商/二维码/专题链接及短链接 */
  },
  watch: {
    searchContent: {
      handler(newVal, oldVal) {
        if (this.contentType === 1) {
          // 活动标题
          this.$set(this.pageInfo, "title", newVal);
        } else {
          // B2B编码
          this.$set(this.pageInfo, "distributorId", newVal);
        }
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

.list-wrap {
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

.check {
  color: #21b8cb;
  cursor: pointer;
}

.search-wrap {
  margin-bottom: 10px;
  height: 40px;
}
</style>
