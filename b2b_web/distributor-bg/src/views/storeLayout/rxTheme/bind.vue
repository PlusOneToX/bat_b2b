<!--
 * @Author: yaowei
 * @Date: 2018-05-25 16:11:40
 * @LastEditors: yaowei
 * @LastEditTime: 2018-06-03 17:11:02
-->
<template>
  <div class="detail-wrap">
    <header>
      <h4 class="header_h4">编辑主题项目</h4>
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
            <el-form-item label="主题项目名称:">
              <p>{{ themeName }}</p>
            </el-form-item>
            <el-form-item label="关联图片分类:" prop="pictureCategorys">
              <el-button
                class="mini-search-btn add-goods-btn"
                @click="categoryShow = true"
                >添加图片分类</el-button
              >
              <el-table
                v-if="detailData.pictureCategorys.length > 0"
                :data="detailData.pictureCategorys"
                header-row-class-name="header-row"
                border
                row-key="id"
                class="tableCenter"
              >
                <el-table-column
                  label="编号"
                  align="center"
                  prop="id"
                  width="80"
                ></el-table-column>
                <el-table-column
                  label="分类名称"
                  align="center"
                  prop="name"
                ></el-table-column>
                <el-table-column label="图片类型" align="center" prop="status">
                  <template slot-scope="scope">
                    <span v-if="Number(scope.row.type) === 1">普通素材</span>
                    <span v-else-if="Number(scope.row.type) === 2">IP素材</span>
                    <span v-else-if="Number(scope.row.type) === 3">模板</span>
                  </template>
                </el-table-column>
                <el-table-column label="操作" align="center">
                  <template slot-scope="scope">
                    <el-button
                      class="mini-search-btn"
                      @click="handleMove(scope.$index, 'up')"
                      v-show="scope.$index !== 0"
                      >上移
                    </el-button>
                    <el-button
                      class="mini-search-btn"
                      @click="handleMove(scope.$index, 'down')"
                      v-show="
                        scope.$index !== detailData.pictureCategorys.length - 1
                      "
                      >下移
                    </el-button>
                    <el-button
                      class="mini-delete-btn"
                      @click="handleDelete(scope.$index)"
                      >删除
                    </el-button>
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
          :loading="loadingStatus"
          >确定</el-button
        >
        <el-button size="mini" @click="handleBack">返回</el-button>
      </div>
    </div>

    <!-- 添加图库分类 -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="categoryShow"
      :before-close="picCancel"
      width="80%"
    >
      <selectPicCategory
        :categoryData="detailData.pictureCategorys"
        ref="selectPicture"
        @picClose="picClose"
        @picSubmit="picSubmit"
      ></selectPicCategory>
    </el-dialog>
  </div>
</template>

<script>
import { getThemeDetail, bindTheme } from "@/views/storeLayout/rxData";
import selectPicCategory from "@/views/storeLayout/rxComponents/selectPicCategory";

export default {
  name: "bindTheme",
  components: { selectPicCategory },
  data() {
    // 添加图片分类
    var validateCategory = (rule, value, callback) => {
      if (
        this.detailData.pictureCategorys &&
        this.detailData.pictureCategorys.length <= 0
      ) {
        callback(new Error("请添加图片分类"));
      } else {
        callback();
      }
    };

    return {
      id: this.$route.query.id, // 主题id
      themeName: "", // 主题名称
      detailData: {
        pictureCategorys: [], // 图库分类数据
      },
      rules: {
        pictureCategorys: [
          {
            required: true,
            validator: validateCategory,
            trigger: "blur",
          },
        ],
      },
      categoryShow: false,
      loadingStatus: false,
    };
  },
  created() {
    this.initData();
  },
  methods: {
    initData() {
      // getThemeDetail(this, this.id).then((res) => {
      this.$http.pictureThemeDetail(this, {id: this.id}).then((res) => {   
        if (res.success) {
          this.themeName = res.data.pictureTheme.name;
          this.detailData.pictureCategorys = res.data.pictureCategorys;
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
    handleSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false;
        } else {
          this.loadingStatus = true;
          let pictureCategoryThemeRelas = [];

          this.detailData.pictureCategorys.forEach((item, index) => {
            let cate = {
              id: this.id,
              categoryId: item.id,
              sort: index,
            };

            pictureCategoryThemeRelas.push(cate);
          });

          // bindTheme(this, {
          this.$http.pictureThemeRelation(this, {  
            id: this.id,
            pictureCategoryThemeRelas: pictureCategoryThemeRelas,
          }).then((res) => {
            if (res.success) {
              this.$message({
                type: "success",
                message: "关联成功",
              });
              this.clickLeave();
            }
            this.loadingStatus = false;
          });
        }
      });
    },
    // 上/下移
    handleMove(index, type) {
      if (type === "up") {
        // 上移，调整位置
        let temp = this.detailData.pictureCategorys[index - 1];
        this.$set(
          this.detailData.pictureCategorys,
          index - 1,
          this.detailData.pictureCategorys[index]
        );
        this.$set(this.detailData.pictureCategorys, index, temp);
      } else {
        // 下移，调整位置
        let temp = this.detailData.pictureCategorys[index + 1];
        this.$set(
          this.detailData.pictureCategorys,
          index + 1,
          this.detailData.pictureCategorys[index]
        );
        this.$set(this.detailData.pictureCategorys, index, temp);
      }
    },
    // 删除
    handleDelete(index) {
      this.detailData.pictureCategorys.splice(index, 1);
    },
    // 图片分类
    picSubmit(msg) {
      this.detailData.pictureCategorys = msg;
      this.categoryShow = false;
    },
    picCancel() {
      this.$refs.selectPicture.handleCancel();
    },
    picClose() {
      this.categoryShow = false;
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

  .footbtn {
    padding-bottom: 30px;
    text-align: center;
    .box-btn + .box-btn {
      margin-left: 10px;
    }
  }
}
</style>