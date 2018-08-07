<template>
  <div class="detail-wrap">
    <header>
      <h4 class="header_h4">
        {{ enterFlag === "add" ? "添加产品推广" : "产品推广详情" }}
      </h4>
    </header>

    <div class="content">
      <el-form
        :model="detailData"
        :rules="rules"
        label-width="18%"
        label-position="right"
        ref="ruleForm"
      >
        <el-row>
          <el-col :span="24">
            <el-form-item label="产品推广名称:" prop="extensionGoodsName">
              <el-input
                style="width: 350px"
                placeholder="请输入产品推广名称"
                v-model="detailData.extensionGoodsName"
              ></el-input>
            </el-form-item>

            <el-form-item label="PC端产品图:">
              <div class="upload-img-list">
                <div class="upload-img-item">
                  <div class="img-uploader-big">
                    <uploadImg
                      class="avatar-uploader-plate"
                      :isEdit="true"
                      :area="'zh'"
                      :index="1"
                      :curUrl="detailData.pcCnExtensionImgUrl"
                      @getImgUrl="getImgUrl"
                    ></uploadImg>
                    <div class="img-hint-plate">
                      <div class="area">国内</div>
                      <el-button
                        class="mini-delete-btn delete-img"
                        @click.prevent="clear(1, 'zh')"
                        >清除</el-button
                      >
                      <div class="text">(上传尺寸：900*500)</div>
                    </div>
                  </div>
                  <div class="img-url-plate">
                    <span class="input-hint-plate">跳转目标</span>
                    <el-input
                      class="input-url-plate"
                      v-model="detailData.pcCnExtensionGoodsUrl"
                      placeholder="相关产品跳转链接"
                    />
                  </div>
                </div>
                <div class="upload-img-item">
                  <div class="img-uploader-big">
                    <uploadImg
                      class="avatar-uploader-plate"
                      :isEdit="true"
                      :area="'en'"
                      :index="1"
                      :curUrl="detailData.pcEnExtensionImgUrl"
                      @getImgUrl="getImgUrl"
                    ></uploadImg>
                    <div class="img-hint-plate">
                      <div class="area">国外</div>
                      <el-button
                        class="mini-delete-btn delete-img"
                        @click.prevent="clear(1, 'en')"
                        >清除</el-button
                      >
                      <div class="text">(上传尺寸：900*500)</div>
                    </div>
                  </div>
                  <div class="img-url-plate">
                    <span class="input-hint-plate">跳转目标</span>
                    <el-input
                      class="input-url-plate"
                      v-model="detailData.pcEnExtensionGoodsUrl"
                      placeholder="相关产品跳转链接"
                    />
                  </div>
                </div>
              </div>
            </el-form-item>

            <el-form-item label="移动端产品图:">
              <div class="upload-img-list">
                <div class="upload-img-item">
                  <div class="img-uploader-big mobile">
                    <uploadImg
                      class="avatar-uploader-plate"
                      :isEdit="true"
                      :area="'zh'"
                      :index="2"
                      :curUrl="detailData.moCnExtensionImgUrl"
                      @getImgUrl="getImgUrl"
                    ></uploadImg>
                    <div class="img-hint-plate">
                      <div class="area">国内</div>
                      <el-button
                        class="mini-delete-btn delete-img"
                        @click.prevent="clear(2, 'zh')"
                        >清除</el-button
                      >
                      <div class="text">(上传尺寸：518*518)</div>
                    </div>
                  </div>
                  <div class="img-url-plate">
                    <span class="input-hint-plate">跳转目标</span>
                    <el-input
                      class="input-url-plate"
                      v-model="detailData.moCnExtensionGoodsUrl"
                      placeholder="相关产品跳转链接"
                    />
                  </div>
                </div>
                <div class="upload-img-item">
                  <div class="img-uploader-big mobile">
                    <uploadImg
                      class="avatar-uploader-plate"
                      :isEdit="true"
                      :area="'en'"
                      :index="2"
                      :curUrl="detailData.moEnExtensionImgUrl"
                      @getImgUrl="getImgUrl"
                    ></uploadImg>
                    <div class="img-hint-plate">
                      <div class="area">国外</div>
                      <el-button
                        class="mini-delete-btn delete-img"
                        @click.prevent="clear(2, 'en')"
                        >清除</el-button
                      >
                      <div class="text">(上传尺寸：518*518)</div>
                    </div>
                  </div>
                  <div class="img-url-plate">
                    <span class="input-hint-plate">跳转目标</span>
                    <el-input
                      class="input-url-plate"
                      v-model="detailData.moEnExtensionGoodsUrl"
                      placeholder="相关产品跳转链接"
                    />
                  </div>
                </div>
              </div>
            </el-form-item>

            <el-form-item label="推广时间:" prop="validTime">
              <el-date-picker
                v-model="detailData.validTime"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="timestamp"
                :picker-options="expireTimeOption"
                @input="handleDate"
              ></el-date-picker>
            </el-form-item>

            <!-- 可视范围 -->
            <distributor
              ref="distributor"
              :labelTitle="'可视范围:'"
              :type="detailData.distributorScope"
              :gIds="detailData.scalePriceIds"
              :dIds="detailData.distributors"
              :businessIds="detailData.businessIds"
              :departmentIds="detailData.departmentIds"
              :adminIds="detailData.adminIds"
              @change="getChange"
            ></distributor>
          </el-col>
        </el-row>
      </el-form>

      <div class="clearfix footbtn">
        <el-button
          class="mini-search-btn box-btn"
          @click="handleSubmit('ruleForm')"
          :loading="loadingStatus"
          >保存</el-button
        >
        <el-button size="mini" @click="handleBack">返回</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import distributor from "@/views/marketingPromotion/compomemts/distributor/discountPromotion";
import uploadImg from "@/views/storeLayout/compomemts/uploadImg";
export default {
  name: "productPromotionDetail",
  components: {
    distributor,
    uploadImg,
  },
  data() {
    return {
      enterFlag: "",
      id: "",
      detailData: {
        extensionGoodsName: "",
        pcCnExtensionImgUrl: "",
        pcCnExtensionGoodsUrl: "",
        pcEnExtensionImgUrl: "",
        pcEnExtensionGoodsUrl: "",
        moCnExtensionImgUrl: "",
        moCnExtensionGoodsUrl: "",
        moEnExtensionImgUrl: "",
        moEnExtensionGoodsUrl: "",
        validTime: [],
        extensionStartTime: "",
        extensionEndTime: "",
        // 可视范围
        distributorScope: 1,
        scalePriceIds: [],
        distributorIds: [],
        businessIds: [],
        departmentIds: [],
        adminIds: [],
        distributors: [],
      },
      rules: {
        extensionGoodsName: [
          {
            required: true,
            message: "请输入产品推广名称",
            trigger: "blur",
          },
        ],
        validTime: [
          {
            required: true,
            message: "请选择推广时间",
            trigger: ["change", "blur"],
          },
        ],
      },
      expireTimeOption: {
        // 限制可选日期
        disabledDate(date) {
          return date.getTime() < Date.now() - 24 * 60 * 60 * 1000;
        },
      },
      loadingStatus: false,
      count: 0,
      distributorData: [],
    };
  },
  created() {
    this.enterFlag = this.$route.query.enterFlag;
    this.id = this.$route.query.id;
    if (this.enterFlag && this.enterFlag !== "add") {
      this.initDetail(Number(this.id));
    }
  },
  activated() {
    if (this.enterFlag && this.enterFlag !== "add") {
      this.initDetail(Number(this.id));
    }
  },
  methods: {
    initDetail(id) {
      this.$http.proPromotionDetail(this, { id: id }).then((res) => {
        if (res.success) {
          this.detailData = res.data;

          // 有效时间
          this.detailData.validTime = [
            new Date(res.data.extensionStartTime).getTime(),
            new Date(res.data.extensionEndTime).getTime(),
          ];
          this.detailData.distributors = res.data.distributors;

          if (this.detailData.distributorScope === 1) {
            this.count = 5;
          } else {
            this.count = 6;
          }
        }
      });
    },

    getImgUrl(index, url, type) {
      // index: , url: 图片地址, type: 国内/国外
      this.changeData(index, url, type);
    },
    clear(index, type) {
      this.changeData(index, "", type);
    },
    changeData(index, url, type) {
      if (index === 1) {
        if (type === "zh") {
          this.detailData.pcCnExtensionImgUrl = url;
        } else {
          this.detailData.pcEnExtensionImgUrl = url;
        }
      } else {
        if (type === "zh") {
          this.detailData.moCnExtensionImgUrl = url;
        } else {
          this.detailData.moEnExtensionImgUrl = url;
        }
      }
    },
    // 监听时间选择
    handleDate(value) {
      this.detailData.validTime = value;
      this.$forceUpdate();
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
    handleSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false;
        } else {
          // 产品图
          if (
            !this.detailData.pcCnExtensionImgUrl &&
            !this.detailData.pcEnExtensionImgUrl &&
            !this.detailData.moCnExtensionImgUrl &&
            !this.detailData.moEnExtensionImgUrl
          ) {
            this.$message.error("请设置产品推广图");
            return;
          }

          // 可视范围
          this.count = 5;
          if (this.detailData.distributorScope === 1) {
            this.detailData.scalePriceIds = undefined;
            this.detailData.distributorIds = undefined;
            this.detailData.departmentIds = undefined;
            this.detailData.adminIds = undefined;
          }

          if (
            this.detailData.distributorScope === 2 &&
            (this.detailData.scalePriceIds === undefined ||
              this.detailData.scalePriceIds.length === 0)
          ) {
            this.$message.error("请先指定分销商等级范围");
            return;
          } else if (this.detailData.distributorScope === 2) {
            this.detailData.distributorIds = undefined;
            this.detailData.departmentIds = undefined;
            this.detailData.adminIds = undefined;
          }

          if (
            this.detailData.distributorScope === 4 &&
            (this.detailData.departmentIds === undefined ||
              this.detailData.departmentIds.length === 0)
          ) {
            this.$message.error("请先指定销售部门范围");
            return;
          } else if (this.detailData.distributorScope === 4) {
            this.detailData.scalePriceIds = undefined;
            this.detailData.distributorIds = undefined;
            this.detailData.adminIds = undefined;
          }

          if (
            this.detailData.distributorScope === 5 &&
            (this.detailData.adminIds === undefined ||
              this.detailData.adminIds.length === 0)
          ) {
            this.$message.error("请先指定业务员范围");
            return;
          } else if (this.detailData.distributorScope === 5) {
            this.detailData.scalePriceIds = undefined;
            this.detailData.distributorIds = undefined;
            this.detailData.departmentIds = undefined;
          }

          if (
            this.detailData.distributorScope === 3 &&
            this.distributorData.length === 0
          ) {
            this.$message.error("请先指定分销商范围");
            return;
          } else if (this.detailData.distributorScope === 3) {
            this.detailData.distributorIds = [];
            for (let i = 0; i < this.distributorData.length; i++) {
              this.detailData.distributorIds.push(this.distributorData[i].id);
            }
            this.detailData.scalePriceIds = undefined;
            this.detailData.departmentIds = undefined;
            this.detailData.adminIds = undefined;
          }

          // 推广时间
          if (this.detailData.validTime.length > 0) {
            this.detailData.extensionStartTime = this.detailData.validTime[0];
            this.detailData.extensionEndTime = this.detailData.validTime[1];
          }

          this.loadingStatus = true;
          delete this.detailData.validTime;
          if (this.enterFlag === "add") {
            // 新增
            this.$http.addProPromotion(this, this.detailData).then((res) => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "新增成功",
                });
                this.clickLeave();
              }
              this.loadingStatus = false;
            });
          } else {
            // 编辑
            this.detailData.id = Number(this.id);
            this.$http.handleProPromotion(this, this.detailData).then((res) => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "编辑成功",
                });
                this.clickLeave();
              }
              this.loadingStatus = false;
            });
          }
        }
      });
    },
    // 可视范围
    getChange(val) {
      console.log(val);
      if (this.count === 0) {
        this.detailData.distributorScope = val.distributorType; //..可视范围
        this.detailData.scalePriceIds = val.distributorGradeIds; //..指定分销商等级
        this.detailData.departmentIds = val.distributorDepartmentIds; // 指定部门
        this.detailData.adminIds = val.distributorAdminIds; // 指定业务员
        this.detailData.distributorIds = val.distributorIds; //..指定分销商用户ID
        this.distributorData = val.distributorData;
      } else {
        this.count--;
      }
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
  }
  .content {
    padding-top: 30px;
    min-width: 1200px;
  }

  .footbtn {
    padding-bottom: 30px;
    text-align: center;
    .box-btn + .box-btn {
      margin-left: 10px;
    }
  }
}

.upload-img-list {
  overflow: hidden;
  .upload-img-item {
    width: 450px;
    background-color: #f5f7fa;
    display: inline-block;
    & + .upload-img-item {
      margin-left: 15px;
    }
  }
  .img-uploader-big {
    display: flex;
    display: -webkit-flex;
    justify-content: space-between;
    padding: 20px 30px 0;
    height: 160px;
    &.mobile {
      .index_big {
        /deep/.oss_file_big {
          width: 140px;
        }
      }
    }
    .index_big {
      overflow: hidden;
      /deep/.oss_file_big {
        display: inline-block;
        width: 252px;
        height: 140px;
        line-height: 140px;
        border: 1px solid #dbdbdb;
        border-radius: 4px;
        span {
          display: block;
          height: 100%;
          width: 100%;
          text-align: center;
          img {
            width: auto;
            max-height: 100%;
            vertical-align: middle;
          }
        }
      }
    }
    .img-hint-plate {
      height: 140px;
      line-height: 30px;
      color: #333;
      text-align: right;

      .area {
        font-size: 14px;
      }

      .delete-img {
        margin-top: 24px;
        margin-bottom: 26px;
      }

      .text {
        font-size: 10px;
      }
    }
  }
  .img-url-plate {
    position: relative;
    padding: 20px 20px 5px 90px;
    .input-hint-plate {
      position: absolute;
      left: 20px;
    }
  }
}

/deep/ .el-date-editor .el-range-separator {
  width: 8%;
}
</style>