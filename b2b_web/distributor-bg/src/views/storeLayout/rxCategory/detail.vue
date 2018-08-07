<!--
 * @Author: yaowei
 * @Date: 2018-05-19 16:40:55
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-26 11:43:04
-->
<template>
  <div class="detail-wrap">
    <header>
      <h4 class="header_h4" v-if="Number(checkMsg) === 1">新增系列展示</h4>
      <h4 class="header_h4" v-if="Number(checkMsg) === 3">编辑系列展示</h4>
      <el-button
        class="mini-back-btn btn-home"
        icon="el-icon-d-arrow-left"
        @click="handleBack"
        >返回系列展示管理</el-button
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
            <div style="text-align: right">
              <el-button
                v-if="Number(checkMsg) !== 1"
                class="mini-delete-btn add-goods-btn"
                @click="handleDeteteTheme()"
                >删除主题系列</el-button
              >
            </div>
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

            <el-form-item label="主题名称：" prop="seriesName">
              <el-input
                v-model="detailData.seriesName"
                placeholder="系列展示标题"
              ></el-input>
            </el-form-item>
            <!-- <el-form-item label="排序:" prop="sortNo">
              <el-select
                v-model="detailData.sortNo"
                size="mini"
                placeholder="排序"
                style="width: 200px"
                clearable
              >
                <el-option label="A级 - 置顶" :value="1"></el-option>
                <el-option label="B级 - 第二位" :value="2"></el-option>
                <el-option label="C级 - 随机" :value="3"></el-option>
              </el-select>
            </el-form-item> -->
            <el-form-item label="图片分类:" prop="pictureCategoryId">
              <el-select
                v-model="detailData.pictureCategoryId"
                size="mini"
                placeholder="图片分类"
                style="width: 200px"
                @change="picCategoryChange"
                clearable
              >
                <el-option
                  v-for="item in pictureCategoryList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                >
                </el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="选择图片:" prop="picData">
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
                border
                row-key="id"
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
                  label="产品图"
                  align="center"
                  :min-width="170"
                  show-overflow-tooltip
                >
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
        :distributorData="distriData"
        ref="selectDistributor"
        @disClose="disClose"
        @disSubmit="disSubmit"
      >
      </select-distributor>
    </el-dialog>

    <!-- 添加图片 ---->
    <el-dialog
      :modal-append-to-body="false"
      :visible="picModelShow"
      :before-close="picCancel"
      width="80%"
    >
      <select-pic
        :pictureData="detailData.picData"
        :distributorId="distributorId"
        :categoryId="detailData.pictureCategoryId"
        :comingFlag="'rxCategory'"
        ref="selectPicture"
        @picClose="picClose"
        @picSubmit="picSubmit"
      >
      </select-pic>
    </el-dialog>
  </div>
</template>

<script>
import selectDistributor from "@/views/storeLayout/rxComponents/selectDistributor";
import selectPic from "@/views/storeLayout/rxComponents/selectPic";
export default {
  name: "categoryDetail",
  components: { selectDistributor, selectPic },
  data() {
    // 添加分销商
    var validateDistriData = (rule, value, callback) => {
      if (this.distriData && this.distriData.length <= 0) {
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
        seriesName: "",
        // sortNo: 1,
        pictureCategoryId: "",
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
        seriesName: [
          {
            required: true,
            message: "请输入系列展示标题",
            trigger: ["change", "blur"],
          },
        ],
        // sortNo: [
        //   {
        //     required: true,
        //     message: "请选择系列排序",
        //     trigger: "change",
        //   },
        // ],
        pictureCategoryId: [
          {
            required: true,
            message: "请选择图片分类",
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
      expireTimeOption: {
        // 限制可选日期
        disabledDate(date) {
          return date.getTime() < Date.now() - 24 * 60 * 60 * 1000;
        },
      },
      action: process.env.BASE_API + "system/v1/web/admin/oss/sts",
      pictureCategoryList: [], // 图片分类
      distributorShow: false,
      multipleSelectPic: [],
      distributorId: 0,
      picModelShow: false,
      distriData: [],
    };
  },
  mounted() {
    this.getPictureCategory();
    if (Number(this.checkMsg) !== 1) {
      this.initData();
    }
  },
  methods: {
    initData() {
      this.$http.disThemeDetail(this, { id: this.id }).then((res) => {  
        if (res.success) {
          this.detailData = res.data;
          this.distriData = [];
          // 选择分销商
          if (res.data.distributorList && res.data.distributorList.length > 0) {
            res.data.distributorList.forEach((item) => {
              let option = {
                distributorName: item.distributorName,
                companyName: item.distributorCompanyName,
                distributorId: item.distributorId,
              };
              this.distriData.push(option);
            });
            this.distributorId = this.distriData[0].distributorId;
            this.detailData.picData = res.data.pictureList;
          }
        }
      });
    },
    // 图片分类
    getPictureCategory() {
      this.$http.pictureCategoryPoList(this, {
        page:1,
        size:1000, 
        atLastTrademark: 1
      })
      .then((res) => {
        this.pictureCategoryList = res.data.list;
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
    // 提交
    handleSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false;
        } else {
          let distributorList = [];
          this.distriData.forEach((item) => {
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
            this.$http.addDisTheme(this, {  
              id: this.id,
              distributorList: distributorList,
              pictureIdList: pictureIdList,
              seriesName: this.detailData.seriesName,
              sortNo: this.detailData.sortNo,
              pictureCategoryId: this.detailData.pictureCategoryId,
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
            this.$http.editDisTheme(this, {    
              id: this.id,
              distributorList: distributorList,
              pictureIdList: pictureIdList,
              seriesName: this.detailData.seriesName,
              sortNo: this.detailData.sortNo,
              pictureCategoryId: this.detailData.pictureCategoryId,
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
      this.distriData = msg;
      this.distributorId = this.distriData[0].distributorId;
      this.distributorShow = false;
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
    // 删除主题系列
    handleDeteteTheme() {
      this.$confirm("此操作将删除该主题系列，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          // deleteTheme(this, this.id).then((res) => {
          this.$http.delDisTheme(this, this.id).then((res) => {  
            if (res.success) {
              this.$message({
                type: "success",
                message: "删除成功",
              });
              this.clickLeave();
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
    // 图片选择
    picCategoryChange(val) {
      // 清空已选图片列表
      this.detailData.picData = [];
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