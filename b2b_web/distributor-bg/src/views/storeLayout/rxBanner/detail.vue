<!--
 * @Author: yaowei
 * @Date: 2018-05-19 10:46:08
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-09 09:17:25
-->
<template>
  <div class="detail-wrap">
    <header>
      <h4 class="header_h4" v-if="Number(checkMsg) === 1">新增轮播图</h4>
      <h4 class="header_h4" v-if="Number(checkMsg) === 3">编辑轮播图</h4>
      <el-button
        class="mini-back-btn btn-home"
        icon="el-icon-d-arrow-left"
        @click="handleBack"
        >返回轮播图管理</el-button
      >
    </header>

    <div class="content">
      <el-form
        :model="detailData"
        :rules="rules"
        label-width="20%"
        label-position="right"
        ref="ruleForm"
      >
        <el-row>
          <el-col :span="18">
            <el-form-item label="选择分销商:" prop="distriData" :key="1">
              <el-button
                class="mini-search-btn add-goods-btn"
                @click="distributorShow = true"
                >添加</el-button
              >
              <el-table
                v-if="distriData.length > 0"
                :data="distriData"
                header-row-class-name="header-row"
                border
                row-key="id"
                class="tableCenter"
              >
                <el-table-column
                  label="分销商用户名"
                  align="center"
                  prop="distributorName"
                ></el-table-column>
                <el-table-column
                  label="公司名"
                  align="center"
                  prop="companyName"
                ></el-table-column>
                <el-table-column label="操作" align="center">
                  <template slot-scope="scope">
                    <el-button
                      class="mini-delete-btn"
                      @click="handleDeleteDistributor(scope.$index)"
                      >删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-form-item>

            <el-form-item label="添加轮播图:" prop="bannerUrl">
              <el-upload
                class="uploader-wrap"
                :action="action"
                :show-file-list="false"
                :on-remove="handleRemove"
                :before-upload="beforeUpload"
              >
                <img
                  v-if="detailData.bannerUrl"
                  :src="detailData.bannerUrl"
                  class="avatar"
                />
                <i v-else class="el-icon-plus uploader-wrap-icon"></i>
              </el-upload>
              <span>（图片大小：750*500）</span>
            </el-form-item>
            <el-form-item label="轮播图展示时间:" prop="validTime">
              <el-date-picker
                v-model="detailData.validTime"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="timestamp"
                :picker-options="expireTimeOption"
                @input="handleDate"
              ></el-date-picker>
            </el-form-item>
            <el-form-item label="运营位置:" prop="showLocation">
              <el-select
                v-model="detailData.showLocation"
                size="mini"
                placeholder="运营位置"
                style="width: 200px"
                clearable
              >
                <el-option label="首页主banner位" :value="1"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="备注信息:">
              <el-input
                type="textarea"
                :rows="5"
                v-model="detailData.remark"
                placeholder="维护人员填写，方便管理~"
              />
            </el-form-item>
            <el-form-item label="运营类型:" prop="type">
              <el-select
                v-model="detailData.type"
                size="mini"
                placeholder="运营类型"
                style="width: 200px"
                clearable
              >
                <el-option label="运营IP图某系列" :value="1"></el-option>
                <el-option
                  label="活动专题页用途（定制开发）"
                  :value="2"
                ></el-option>
              </el-select>
            </el-form-item>

            <el-form-item
              label="主题名称:"
              prop="seriesId"
              v-if="detailData.type === 1 && this.distributorId"
            >
              <el-select
                v-model="detailData.seriesId"
                size="mini"
                placeholder="主题名称"
                style="width: 200px"
                @change="initThemeData(detailData.seriesId)"
                clearable
              >
                <el-option
                  v-for="item in themeNameList"
                  :key="item.id"
                  :label="item.themeName"
                  :value="item.id"
                >
                </el-option>
              </el-select>

              <el-table
                :data="picData"
                header-row-class-name="header-row"
                row-key="id"
                border
                class="tableCenter"
                ref="multipleSelect"
                @selection-change="handleSelectionPicture"
              >
                <el-table-column
                  align="center"
                  type="selection"
                  width="50"
                  reserve-selection
                ></el-table-column>
                <el-table-column
                  label="主题名称"
                  align="center"
                  prop="themeName"
                  width="100"
                  show-overflow-tooltip
                ></el-table-column>
                <el-table-column
                  label="图片分类名称"
                  align="center"
                  prop="categoryName"
                  width="100"
                  show-overflow-tooltip
                ></el-table-column>
                <el-table-column
                  label="图片ID"
                  align="center"
                  prop="pictureId"
                  width="100"
                ></el-table-column>
                <el-table-column
                  label="图片编码"
                  align="center"
                  prop="code"
                  width="100"
                  show-overflow-tooltip
                >
                </el-table-column>
                <el-table-column
                  label="图片名称"
                  align="center"
                  prop="pictureName"
                  width="100"
                  show-overflow-tooltip
                >
                </el-table-column>
                <el-table-column
                  label="图片英文名称"
                  align="center"
                  prop="englishName"
                  :min-width="170"
                  show-overflow-tooltip
                ></el-table-column>
                <el-table-column
                  label="分销商名称"
                  align="center"
                  prop="distributorName"
                  :min-width="170"
                  show-overflow-tooltip
                ></el-table-column>
                <el-table-column
                  label="公司名称"
                  align="center"
                  prop="companyName"
                  :min-width="170"
                  show-overflow-tooltip
                ></el-table-column>
                <el-table-column label="产品图" align="center" :min-width="170">
                  <template
                    slot-scope="scope"
                    style="text-align: center; width: 60px; height: 60px"
                  >
                    <el-image
                      style="
                        text-align: center;
                        width: 60px;
                        height: 60px;
                        padding-right: 0px;
                      "
                      fit="contain"
                      :src="
                        scope.row.thumbnail !== undefined &&
                        scope.row.thumbnail !== null &&
                        scope.row.thumbnail !== ''
                          ? scope.row.thumbnail
                          : scope.row.originImage +
                            '?x-oss-process=image/resize,h_60,l_60'
                      "
                      :preview-src-list="[
                        scope.row.thumbnail !== undefined &&
                        scope.row.thumbnail !== null &&
                        scope.row.thumbnail !== ''
                          ? scope.row.thumbnail
                          : scope.row.originImage,
                      ]"
                    >
                    </el-image>
                  </template>
                </el-table-column>
                <el-table-column
                  label="适用货品"
                  align="center"
                  prop="itemCode"
                  width="150"
                >
                </el-table-column>
              </el-table>
              <!-- <pagination
                :total="total"
                :page="pageInfo.page"
                @sizeChange="onSizeCHange"
                @currentChange="onCurrentChange"
              ></pagination> -->
            </el-form-item>

            <el-form-item
              label="定制开发链接:"
              prop="externalLink"
              v-if="detailData.type === 2"
            >
              <el-input v-model="detailData.externalLink"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div class="clearfix footbtn">
        <el-button
          class="mini-search-btn box-btn"
          @click="handleSubmit('ruleForm')"
          >保存提交</el-button
        >
        <el-button size="mini" @click="handleBack">返回</el-button>
      </div>
    </div>

    <!-- 添加分销商 -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="distributorShow"
      :before-close="disCancel"
      width="80%"
    >
      <select-distributor
        :distributorData="distriData"
        ref="selectDistributor"
        @disClose="disClose"
        @disSubmit="disSubmit"
      >
      </select-distributor>
    </el-dialog>
  </div>
</template>

<script>
import selectDistributor from "@/views/storeLayout/rxComponents/selectDistributor";
import pagination from "@/components/pagination/index";
import {monthDay} from '@/utils/timeFormat.js'
export default {
  name: "bannerDetail",
  components: { selectDistributor, pagination },
  data() {
    // 添加分销商
    var validateDistriData = (rule, value, callback) => {
      if (this.distriData && this.distriData.length <= 0) {
        callback(new Error("请选择分销商"));
      } else {
        callback();
      }
    };
    // 主题名称/图片选择
    var validateThemeData = (rule, value, callback) => {
      if (this.detailData.seriesId === "") {
        callback(new Error("请选择主题名称"));
      } else if (this.picData && this.picData.length <= 0) {
        callback(new Error("请选择主题图片"));
      } else {
        callback();
      }
    };

    return {
      id: this.$route.query.id,
      checkMsg: this.$route.query.checkMsg,
      detailData: {
        bannerUrl: "",
        validTime: "",
        startTime: "",
        endTime: "",
        remark: "",
        showLocation: 1,
        type: 1,
        seriesId: "",
        externalLink: "",
      },
      rules: {
        distriData: [
          {
            required: true,
            validator: validateDistriData,
            trigger: "change",
          },
        ],
        bannerUrl: [
          {
            required: true,
            message: "请添加轮播图",
            trigger: "change",
          },
        ],
        validTime: [
          {
            required: true,
            message: "请选择轮播图展示时间",
            trigger: "change",
          },
        ],
        showLocation: [
          {
            required: true,
            message: "请选择运营位置",
            trigger: "change",
          },
        ],
        type: [
          {
            required: true,
            message: "请选择运营类型",
            trigger: "change",
          },
        ],
        seriesId: [
          {
            required: true,
            validator: validateThemeData,
            trigger: "change",
          },
        ],
        externalLink: [
          {
            required: true,
            message: "请设置定制开发链接",
            trigger: "blur",
          },
        ],
      },
      expireTimeOption: {
        // 限制可选日期
        disabledDate(date) {
          return date.getTime() < Date.now() - 24 * 60 * 60 * 1000;
        },
      },
      action: process.env.BASE_API + "system/v1/web/admin/oss/sts",
      themeNameList: [], // 主题名称
      distributorId: "",
      distributorShow: false,
      distriData: [],
      picData: [],
      multipleSelectPic: [],
      pageInfo: {
        // page: 1,
        // size: 10,
        themeId: ''
      },
      total: 0
    };
  },
  mounted() {
    if (Number(this.checkMsg) !== 1) {
      this.initData();
    }
  },
  methods: {
    initData() {
      this.$http.disBannerDetail(this, { id: this.id }).then((res) => { 
        if (res.success) {
          this.detailData = res.data;
          this.distriData = [];
          if (res.data.distributorList.length > 0) {
            res.data.distributorList.forEach((item) => {
              let option = {
                distributorName: item.distributorName,
                companyName: item.distributorCompanyName,
                distributorId: item.distributorId,
              };
              this.distriData.push(option);
            });
            this.distributorId = this.distriData[0].distributorId;
          }
          this.detailData.validTime = [res.data.startTime, res.data.endTime];
          this.getThemeName();
          this.initThemeData(res.data.seriesId, res.data.pictureIdList)
        }
      });
    },
    // 主题名称
    getThemeName() {
      // themeName(this, {
      this.$http.disThemeListByCondition(this, {  
        distributorId: this.distributorId,
        search: "",
      }).then((res) => {
        if (res.success) {
          this.themeNameList = res.data;
        }
      });
    },
    // 主题列表
    initThemeData(id, dataList) {
      this.pageInfo.themeId = id
      // categoryListAll(this, {
      this.$http.disThemePicList(this, this.pageInfo).then((res) => {
        if (res.success) {
          this.picData = res.data;
          // this.total = res.data.total
          if (Number(this.checkMsg) !== 1 && dataList) {
            // 已选主题图片
            this.multipleSelectPic = [];
            res.data.forEach((item) => {
              dataList.forEach((item2) => {
                if (item.pictureId === item2) {
                  this.$refs.multipleSelect.toggleRowSelection(item);
                }
              });
            });
          }
        }
      });
    },
    // 返回列表
    handleBack() {
      this.resetValidate("ruleForm");
      this.clickLeave();
    },
    // 页面跳转
    clickLeave() {
      this.$router.go(-1);
    },
    // 重置表单验证
    resetValidate(formName) {
      this.$refs[formName].resetFields();
    },
    // 监听时间选择
    handleDate(value) {
      this.detailData.validTime = value;
      this.$forceUpdate();
    },
    // 添加轮播图
    handleRemove(file) {
      this.detailData.bannerUrl = "";
    },
    beforeUpload(file) {
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
      _self.$http.getFileSts(this).then(result => { 	
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
              _self.detailData.bannerUrl = result.data.hostname + results.name;
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
    // 选择主题图片
    handleSelectionPicture(val) {
      this.multipleSelectPic = val;
    },
    handleSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false;
        } else {
          let distributorList = [];
          this.distriData.forEach((item) => {
            distributorList.push({
              // id: item.id,
              distributorId: item.distributorId,
              distributorName: item.distributorName,
              distributorCompanyName: item.companyName
            });
          });

          let pictureIdList = [];
          this.multipleSelectPic.forEach((item) => {
            pictureIdList.push(item.pictureId);
          });
          this.detailData.startTime = this.detailData.validTime[0];
          this.detailData.endTime = this.detailData.validTime[1];
          if (Number(this.checkMsg) === 1) {
            // 新增
            // addBanner(this, {
            this.$http.addDisBanner(this, {  
              id: this.id,
              distributorList: distributorList,
              bannerUrl: this.detailData.bannerUrl,
              startTime: this.detailData.startTime,
              endTime: this.detailData.endTime,
              showLocation: this.detailData.showLocation,
              remark: this.detailData.remark,
              type: this.detailData.type,
              seriesId: this.detailData.seriesId,
              pictureIdList: pictureIdList,
              externalLink: this.detailData.externalLink,
            }).then((res) => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "新增成功",
                });
                this.clickLeave();
              }
            });
          } else if (Number(this.checkMsg) === 3) {
            // 编辑
            // editBanner(this, {
            this.$http.editDisBanner(this, {    
              id: this.id,
              distributorList: distributorList,
              bannerUrl: this.detailData.bannerUrl,
              startTime: this.detailData.startTime,
              endTime: this.detailData.endTime,
              showLocation: this.detailData.showLocation,
              remark: this.detailData.remark,
              type: this.detailData.type,
              seriesId: this.detailData.seriesId,
              pictureIdList: pictureIdList,
              externalLink: this.detailData.externalLink,
            }).then((res) => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "编辑成功",
                });
                this.clickLeave();
              }
            });
          }
        }
      });
    },
    // 选择分销商
    disSubmit(msg) {
      this.picData = [];
      this.detailData.seriesId = "";
      this.distriData = msg;
      this.distributorId = this.distriData[0].distributorId;
      this.distributorShow = false;
      this.getThemeName();
    },
    disCancel() {
      this.$refs.selectDistributor.handleCancel();
    },
    disClose() {
      this.distributorShow = false;
    },
    handleDeleteDistributor(index) {
      this.distriData.splice(index, 1);
    },
     onSizeCHange(val) {
      // 分页方法
      this.pageInfo.size = val;
      this.updateMainData();
    },
    onCurrentChange(val) {
      // 分页方法
      this.pageInfo.page = val;
      this.updateMainData();
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.detail-wrap {
  background-color: #fff;
  position: relative;
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
    .header_h4 {
      margin: 0 0 0 30px;
    }
    h4 {
      margin-left: 30px;
      display: inline-block;
      font-weight: 400;
    }
    .btn-home {
      float: right;
      padding: 5px;
      margin-top: 7px;
      margin-right: 8px;
      margin-left: 0;
    }
  }
  .content {
    padding-top: 30px;
    min-width: 1200px;
  }

  .uploader-wrap {
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
    .uploader-wrap-icon {
      font-size: 28px;
      color: #8c939d;
      width: 300px;
      height: 200px;
      line-height: 200px;
      text-align: center;
    }
    .avatar {
      width: 300px;
      height: 200px;
      display: block;
    }
  }

  .footbtn {
    padding-bottom: 30px;
    text-align: center;
    .box-btn + .box-btn {
      margin-left: 10px;
    }
  }
}
</style>