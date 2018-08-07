<!--
 * @Author: yaowei
 * @Date: 2018-05-19 16:49:30
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-27 14:23:42
-->
<template>
  <div class="detail-wrap">
    <header>
      <h4 class="header_h4" v-if="Number(checkMsg) === 1">新增推荐</h4>
      <h4 class="header_h4" v-if="Number(checkMsg) === 3">编辑推荐</h4>
      <el-button
        class="mini-back-btn btn-home"
        icon="el-icon-d-arrow-left"
        @click="handleBack"
        >返回推荐管理</el-button
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
                v-if="detailData.distriData.length > 0"
                :data="detailData.distriData"
                header-row-class-name="header-row"
                border
                row-key="id"
                class="tableCenter"
              >
                <el-table-column
                  label="分销商用户名"
                  align="center"
                  prop="distributorName"
                  show-overflow-tooltip
                ></el-table-column>
                <el-table-column
                  label="公司名"
                  align="center"
                  prop="companyName"
                  show-overflow-tooltip
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

            <el-form-item label="选择图片:" prop="picData" :key="2">
              <div>
                <el-button
                  class="mini-search-btn add-goods-btn"
                  @click="handlePicModel"
                  >添加</el-button
                >
                <el-button
                  class="mini-delete-btn add-goods-btn"
                  @click="handleDeleteChoose"
                  >删除</el-button
                >
              </div>
              <el-table
                :data="detailData.picData"
                ref="multiplePicData"
                header-row-class-name="header-row"
                row-key="id"
                border
                @selection-change="handleSelectionPicture"
                class="tableCenter"
              >
                <el-table-column
                  align="center"
                  type="selection"
                  width="50"
                  reserve-selection
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
                <el-table-column label="状态" align="center" width="120">
                  <template slot-scope="scope">
                    <span v-if="Number(scope.row.openFlag) === 1">启用</span>
                    <span v-else>禁用</span>
                  </template>
                </el-table-column>
                <el-table-column label="操作" align="center" width="120">
                  <template slot-scope="scope">
                    <el-button
                      class="mini-delete-btn add-goods-btn"
                      @click="handleDetetePic(scope.$index)"
                      >删除</el-button
                    >
                  </template>
                </el-table-column>
              </el-table>
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
        :distributorData="detailData.distriData"
        ref="selectDistributor"
        @disClose="disClose"
        @disSubmit="disSubmit"
      >
      </select-distributor>
    </el-dialog>

    <!-- 添加图片 -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="picModelShow"
      :before-close="picCancel"
      width="80%"
    >
      <select-pic
        :pictureData="detailData.picData"
        :distributorId="distributorId"
        ref="selectPicture"
        @picClose="picClose"
        @picSubmit="picSubmit"
      >
      </select-pic>
    </el-dialog>
  </div>
</template>

<script>
import {
  addRecommend,
  recommendDetail,
  editRecommend,
} from "@/views/storeLayout/rxData";
import selectDistributor from "@/views/storeLayout/rxComponents/selectDistributor";
import selectPic from "@/views/storeLayout/rxComponents/selectPic";
export default {
  name: "recommendDetail",
  components: { selectDistributor, selectPic },
  data() {
    // 添加分销商
    var validateDistriData = (rule, value, callback) => {
      if (
        this.detailData.distriData &&
        this.detailData.distriData.length <= 0
      ) {
        callback(new Error("请选择分销商"));
      } else {
        callback();
      }
    };
    // 选择图片
    var validatePicData = (rule, value, callback) => {
      if (this.detailData.picData && this.detailData.picData.length <= 0) {
        callback(new Error("请选择图片"));
      } else {
        callback();
      }
    };

    return {
      id: this.$route.query.id,
      checkMsg: this.$route.query.checkMsg,
      detailData: {
        distriData: [],
        picData: [],
      },
      rules: {
        distriData: [
          {
            required: true,
            validator: validateDistriData,
            trigger: "change",
          },
        ],
        picData: [
          {
            required: true,
            validator: validatePicData,
            trigger: "change",
          },
        ],
      },
      distributorShow: false,
      multipleSelectPic: [],
      distributorId: 0,
      picModelShow: false,
    };
  },
  mounted() {
    if (Number(this.checkMsg) !== 1) {
      this.initData();
    }
  },
  methods: {
    initData() {
      // recommendDetail(this, { id: this.id }).then((res) => {
      this.$http.disRecommendDetail(this, { id: this.id }).then((res) => {  
        if (res.success) {
          this.detailData.distriData = [];
          res.data.distributorList.forEach((item) => {
            let option = {
              distributorName: item.distributorName,
              companyName: item.distributorCompanyName,
              distributorId: item.distributorId,
            };
            this.detailData.distriData.push(option);
          });
          this.distributorId = this.detailData.distriData[0].distributorId;

          this.detailData.picData = res.data.pictureList;
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
    // 提交
    handleSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false;
        } else {
          let distributorList = [];
          this.detailData.distriData.forEach((item) => {
            distributorList.push({
              distributorId: item.distributorId,
              distributorName: item.distributorName,
              distributorCompanyName: item.companyName
            });
          });

          let pictureIdList = [];
          this.detailData.picData.forEach((item) => {
            pictureIdList.push(item.pictureId);
          });
          if (Number(this.checkMsg) === 1) {
            // 新增
            // addRecommend(this, {
            this.$http.addDisRecommend(this, {  
              id: this.id,
              distributorList: distributorList,
              pictureIdList: pictureIdList,
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
            // editRecommend(this, {
            this.$http.editDisRecommend(this, {    
              id: this.id,
              distributorList: distributorList,
              pictureIdList: pictureIdList,
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
      this.detailData.distriData = msg;
      this.distributorId = this.detailData.distriData[0].distributorId;
      this.distributorShow = false;
    },
    disCancel() {
      this.$refs.selectDistributor.handleCancel();
    },
    disClose() {
      this.distributorShow = false;
    },
    handleDeleteDistributor(index) {
      this.detailData.distriData.splice(index, 1);
    },
    // 选择图片
    handlePicModel() {
      if (this.distributorId) {
        this.picModelShow = true;
      } else {
        this.$message({
          type: "warning",
          message: "请先选择分销商",
        });
      }
    },
    picSubmit(msg) {
      this.detailData.picData = msg;
      this.picModelShow = false;
    },
    picCancel() {
      this.$refs.selectPicture.handleCancel();
    },
    picClose() {
      this.picModelShow = false;
    },
    handleSelectionPicture(val) {
      this.multipleSelectPic = val;
    },
    // 删除图片
    handleDetetePic(index) {
      this.detailData.picData.splice(index, 1);
    },
    // 图片 - 批量删除
    handleDeleteChoose() {
      if (this.multipleSelectPic) {
        this.multipleSelectPic.forEach((item1, index1) => {
          this.detailData.picData.forEach((item2, index2) => {
            if (item1 === item2) {
              this.detailData.picData.splice(index2, 1);
            }
          });
        });
      }
      this.multipleSelectPic = this.detailData.picData;
      this.$refs.multiplePicData.clearSelection();
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