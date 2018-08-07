<!--
 * @Author: yaowei
 * @Date: 2018-05-21 09:13:55
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-24 13:55:37
-->
<template>
  <div class="exchange-activity">
    <header>
      <h4>兑换活动列表</h4>
      <el-button
        class="mini-add-btn btn-home"
        icon="el-icon-plus"
        @click="handleCode(1)"
        >添加活动</el-button
      >
    </header>

    <div class="function">
      <div class="Fheader">
        <div class="Fleft">
          <el-select
            size="mini"
            v-model="pageInfo.type"
            placeholder="兑换类型"
            style="width: 100px"
            @change="Csearch()"
            clearable
          >
            <el-option label="专属码" :value="1"></el-option>
          </el-select>
          <el-select
            size="mini"
            v-model="pageInfo.exchangeWay"
            placeholder="优惠类型"
            style="width: 100px"
            @change="Csearch()"
            clearable
          >
            <el-option label="兑换" :value="1"></el-option>
            <el-option label="权益" :value="2"></el-option>
          </el-select>
          <el-select
            size="mini"
            v-model="pageInfo.status"
            placeholder="启用状态"
            style="width: 100px"
            @change="Csearch()"
            clearable
          >
            <el-option label="未发布" :value="0"></el-option>
            <el-option label="已发布" :value="1"></el-option>
            <el-option label="进行中" :value="2"></el-option>
            <el-option label="暂停中" :value="3"></el-option>
            <el-option label="已结束" :value="4"></el-option>
          </el-select>
          <el-select
            size="mini"
            v-model="pageInfo.isEntity"
            placeholder="兑换卡属性"
            style="width: 100px"
            @change="Csearch()"
            clearable
          >
            <el-option label="实体卡" :value="1"></el-option>
            <el-option label="电子卡" :value="0"></el-option>
          </el-select>
        </div>
        <div class="Fsearch">
          <button class="mini-search-btn box-btn" @click="Csearch()">
            搜索
          </button>
          <el-input
            v-model.trim="pageInfo.codeName"
            size="mini"
            clearable
            @change="contentChange"
            @keyup.enter.native="Csearch()"
            placeholder="请输入兑换活动名称"
            class="box-input"
          ></el-input>
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
          label="兑换活动名称"
          align="center"
          prop="codeName"
          :min-width="120"
        ></el-table-column>
        <el-table-column label="兑换类型" align="center" width="90">
          <template slot-scope="scope">
            <span v-if="scope.row.type === 1">专属码</span>
            <!-- <span v-else-if="scope.row.type === 2">通用码</span> -->
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column
          label="生成形式"
          align="center"
          prop="source"
          width="100"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.source === 1">系统生成</span>
            <span v-else-if="scope.row.source === 2">手动导入</span>
          </template>
        </el-table-column>
        <el-table-column
          label="优惠类型"
          align="center"
          prop="exchangeWay"
          width="100"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.exchangeWay === 1">兑换</span>
            <span v-if="scope.row.exchangeWay === 2">权益</span>
          </template>
        </el-table-column>
        <el-table-column
          label="兑换卡属性"
          align="center"
          prop="isEntity"
          width="100"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.isEntity === 1">实体卡</span>
            <span v-if="scope.row.isEntity === 0">电子卡</span>
          </template>
        </el-table-column>
        <el-table-column label="使用/发放数量" align="center" width="120">
          <template slot-scope="scope">
            <span
              >{{ scope.row.exchangeQuantity }}/{{
                scope.row.codeQuantity
              }}</span
            >
          </template>
        </el-table-column>
        <el-table-column
          label="有效时间"
          align="center"
          prop="validTime"
          :min-width="170"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column label="状态" align="center" width="80">
          <template slot-scope="scope">
            <span v-if="scope.row.status === 0">未发布</span>
            <span v-else-if="scope.row.status === 1">已发布</span>
            <span v-else-if="scope.row.status === 2">进行中</span>
            <span v-else-if="scope.row.status === 3">暂停中</span>
            <span v-else-if="scope.row.status === 4">已结束</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" :min-width="210" align="center">
          <template slot-scope="scope">
            <el-button
              class="mini-freeze-btn"
              v-if="Number(scope.row.status) !== 0"
              @click="handleCodeShow(scope.row.id, scope.row.exchangeWay)"
              >码库</el-button
            >
            <!-- 编辑 -->
            <el-button
              class="mini-search-btn"
              v-if="Number(scope.row.status) === 0"
              @click="handleCode(3, scope.row)"
              >查看</el-button
            >
            <!-- 查看 -->
            <el-button
              class="mini-search-btn"
              v-else
              @click="handleCode(2, scope.row)"
              >查看</el-button
            >
            <el-button
              class="mini-search-btn"
              v-if="Number(scope.row.status) === 0"
              @click="handleStatus(scope.row, scope.row.id, 1)"
              :loading="scope.row.isLoading"
              >发布</el-button
            >
            <el-button
              class="mini-browse-btn"
              v-if="Number(scope.row.status) === 3"
              @click="handleStatus(scope.row, scope.row.id, 2)"
              :loading="scope.row.isLoading"
              >启用</el-button
            >
            <el-button
              class="mini-pulloff-btn"
              v-if="Number(scope.row.status) === 2"
              @click="handleStatus(scope.row, scope.row.id, 3)"
              :loading="scope.row.isLoading"
              >停用</el-button
            >
            <el-button
              class="mini-delete-btn"
              v-if="
                Number(scope.row.status) !== 0 &&
                Number(scope.row.isUseMall) === 1
              "
              @click="handleQrcode(scope.row.id)"
              >链接/二维码</el-button
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

    <!-- 码库 -->
    <el-dialog
      class="code-wrap"
      width="70%"
      :visible.sync="codeShow"
      v-if="codeShow"
    >
      <codeList
        :curActivityId="curActivityId"
        @handleInit="initData"
      ></codeList>
    </el-dialog>
    <!-- 二维码 -->
    <el-dialog
      class="qrcode-wrap"
      width="500px"
      :visible.sync="qrcodeShow"
      v-if="qrcodeShow"
    >
      <div class="qrcode-content">
        <h6>复制H5推广链接</h6>
        <div class="link">
          <el-input size="mini" :value="copyLink" readonly></el-input>
          <el-button class="mini-search-btn" @click="handleCopy(copyLink)"
            >复制</el-button
          >
        </div>
        <h6>保存H5推广二维码</h6>
        <div class="qrcode">
          <img :src="copyLink" alt="推广二维码" />
          <el-button
            class="mini-browse-btn"
            @click="handleSaveQrcode(copyLink, '推广二维码')"
            >保存图片</el-button
          >
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import page from "@/components/pagination";
import codeList from "@/views/marketingPromotion/exchange/components/codeList";
import { timeFormat } from "@/utils/timeFormat.js";
import {monthDay} from '@/utils/timeFormat.js'


export default {
  name: "exchangeActivity",
  components: { page, codeList },
  data() {
    return {
      loading: false,
      pageInfo: {
        // 搜索
        page: 1,
        size: 10,
        type: 1,
        exchangeWay: 1,
        status: 2,
        codeName: "",
        isEntity: "",
      },
      tableData: [], // 表格
      total: "", // 总数
      codeShow: false, // 码库弹窗
      curActivityId: "", // 当前码库id
      exchangeWay: "", // 当前码库类型：1 兑换， 2 权益
      qrcodeShow: false, // 二维码弹窗
      copyLink: "", // 推广链接
      syncFactoryShow: false, //同步工厂 - 打印图片上传弹窗
      action: process.env.BASE_API + "system/v1/web/admin/oss/sts",
      positiveUrl: "", // 正面图片
      reverseUrl: "", // 反面图片
      isSending: false, // 是否发送打印图片
    };
  },
  methods: {
    // 初始化数据
    initData() {
      this.$http.exchangeCardList(this, this.pageInfo).then((res) => {
        if (res.success) {
          this.tableData = res.data.list;
          this.total = res.data.total;

          for (var i = 0; i < res.data.list.length; i++) {
            this.tableData.forEach((item) => {
              this.$set(item, "isLoading", false);
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
    // 添加/查看/编辑 - 兑换码（1，添加，2，查看；3，编辑）
    handleCode(checkMsg, row) {
      var query = {};
      if (row) {
        query = { checkMsg: checkMsg, id: row.id, status: row.status };
      } else {
        query = { checkMsg: checkMsg };
      }
      this.$router.push({
        name: "exchangeDetail",
        query: query,
      });
    },
    // 搜索兑换活动
    Csearch() {
      this.pageInfo.page = 1;
      this.initData();
    },
    // 输入框输入搜索兑换活动
    contentChange(val) {
      if (val === undefined || val === "" || val === null) {
        this.Csearch();
      }
    },
    // 兑换活动列表条数
    sizeChange(size) {
      this.pageInfo.size = size;
      this.pageInfo.page = 1;
      this.initData();
    },
    // 兑换活动列表页数
    currentChange(page) {
      this.pageInfo.page = page;
      this.initData();
    },
    // 发布/启用/停用（status：1 发布/2 启用/3 停用）
    handleStatus(row, id, status) {
      let msg = "";
      this.$set(row, "isLoading", true);
      if (row.isLoading) {
        if (status === 1) {
          msg = "此操作将发布该兑换活动，是否继续？";
        } else if (status === 2) {
          msg = "此操作将启用该兑换活动，是否继续？";
        } else if (status === 3) {
          msg = "此操作将停用该兑换活动，是否继续？";
        }
        this.$confirm(msg, "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
          center: true,
        })
          .then((_) => {
            // handleExchangeStatus(this, { id: id, status: status }).then(
            this.$http
              .exchangeCardStatus(this, { id: id, status: status })
              .then((res) => {
                if (res.success) {
                  this.$set(row, "isLoading", false);
                  this.$message({
                    type: "success",
                    message: "操作成功",
                  });
                  this.initData();
                } else {
                  this.$set(row, "isLoading", false);
                }
              });
          })
          .catch((_) => {
            this.$set(row, "isLoading", false);
            this.$message({
              type: "info",
              message: "已取消操作",
            });
          });
      }
    },
    // 码库
    handleCodeShow(id, exchangeWay) {
      this.codeShow = true;
      this.curActivityId = id;
      this.exchangeWay = exchangeWay;
    },
    // 链接/二维码
    handleQrcode(id) {
      this.qrcodeShow = true;
      // qrcodeLink(this, id).then((res) => {
      this.$http.qrCode(this, { id: id }).then((res) => {
        if (res.success) {
          this.copyLink = res.data;
        }
      });
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
    // 保存图片
    handleSaveQrcode(imgsrc, name) {
      var userAgent = navigator.userAgent; // 取得浏览器的 userAgent 字符串
      var isOpera = userAgent.indexOf("Opera") > -1;
      let image = new Image();
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
      image.src = imgsrc;
    },
    handleRemovePositive(file) {
      this.positiveUrl = "";
    },
    beforeUploadPositive(file) {
      if (
        file.type != "image/jpeg" &&
        file.type != "image/bmp" &&
        file.type != "image/jpg" &&
        file.type != "image/jpeg" &&
        file.type != "image/png" &&
        file.type != "image/gif"
      ) {
        this.$message.error("上传图片只能是bmp、jpg、jpeg、png、gif格式!");
        return false;
      }
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error("上传图片大小不能超过 2MB!");
        return isLt2M;
      }

      const _self = this;
      // 随机命名
      let random_name =
        _self.random_string(6) +
        "_" +
        new Date().getTime() +
        "." +
        file.name.split(".").pop();
      _self.$http.getFileSts(this).then((result) => {
        const client = new OSS.Wrapper({
          region: result.data.region,
          accessKeyId: result.data.accessKeyId,
          accessKeySecret: result.data.accessKeySecret,
          stsToken: result.data.securityToken,
          bucket: result.data.bucketName,
          endpoint: result.data.endpoint,
          secure: true,
        });
        // 上传
        client
          .multipartUpload('flexible/other/' + monthDay(new Date()) + '/' + random_name, file, {})
          .then((results) => {
            return new Promise((resolve, reject) => {
              _self.positiveUrl = result.data.hostname + results.name;
              resolve(true);
            });
          })
          .catch((err) => {
            console.log(err);
          });
      });
    },
    handleRemoveReverse(file) {
      this.reverseUrl = "";
    },
    beforeUploadReverse(file) {
      if (
        file.type != "image/jpeg" &&
        file.type != "image/bmp" &&
        file.type != "image/jpg" &&
        file.type != "image/jpeg" &&
        file.type != "image/png" &&
        file.type != "image/gif"
      ) {
        this.$message.error("上传图片只能是bmp、jpg、jpeg、png、gif格式!");
        return false;
      }
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error("上传图片大小不能超过 2MB!");
        return isLt2M;
      }

      const _self = this;
      // 随机命名
      let random_name =
        _self.random_string(6) +
        "_" +
        new Date().getTime() +
        "." +
        file.name.split(".").pop();
      _self.$http.getFileSts(this).then((result) => {
        const client = new OSS.Wrapper({
          region: result.data.region,
          accessKeyId: result.data.accessKeyId,
          accessKeySecret: result.data.accessKeySecret,
          stsToken: result.data.securityToken,
          bucket: result.data.bucketName,
          endpoint: result.data.endpoint,
          secure: true,
        });
        // 上传
        client
          .multipartUpload('flexible/other/' + monthDay(new Date()) + '/' + random_name, file, {})
          .then((results) => {
            return new Promise((resolve, reject) => {
              _self.reverseUrl = result.data.hostname + results.name;
              resolve(true);
            });
          })
          .catch((err) => {
            console.log(err);
          });
      });
    },
    // 随机生成文件名
    random_string(len) {
      len = len || 32;
      var chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678";
      var maxPos = chars.length;
      var pwd = "";
      for (let i = 0; i < len; i++) {
        pwd += chars.charAt(Math.floor(Math.random() * maxPos));
      }
      return pwd;
    },
    handleSyncFactory() {
      this.isSending = true;
      handleSyncFactory(this, {
        id: this.curActivityId,
        positiveUrl: this.positiveUrl,
        reverseUrl: this.reverseUrl,
      }).then((res) => {
        if (Number(res.code) === 0) {
          this.isSending = false;
          this.syncFactoryShow = false;
          this.$message({
            type: "success",
            message: "操作成功",
          });
          this.initData();
        } else {
          this.isSending = false;
          this.syncFactoryShow = false;
          this.$message({
            type: "success",
            message: res.msg,
          });
          this.initData();
        }
      });
    },
  },
  created() {
    this.initData();
  },
  activated() {
    this.initData();
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.exchange-activity {
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
.qrcode-content {
  h6 {
    font-size: 14px;
    margin-bottom: 5px;
  }
  .link {
    margin-bottom: 30px;
    .el-input {
      margin-right: 10px;
      width: 350px;
    }
  }
  .qrcode {
    img {
      width: 100px;
      height: 100px;
      vertical-align: bottom;
    }
    .el-button {
      margin-left: 10px;
    }
  }
}
.sync-factory-wrap {
  .pic-wrap {
    position: relative;
    margin-top: 20px;
    padding-left: 100px;
    span {
      position: absolute;
      top: 50%;
      left: 0;
      margin-top: -7px;
    }
  }
  .dialog-footer {
    text-align: center;
  }
}
.avatar-uploader {
  /deep/ .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    &:hover {
      border-color: #21b8cb;
    }
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 296px;
    height: 144px;
    line-height: 144px;
    text-align: center;
  }
  .avatar {
    width: 296px;
    height: 144px;
    display: block;
  }
}
</style>