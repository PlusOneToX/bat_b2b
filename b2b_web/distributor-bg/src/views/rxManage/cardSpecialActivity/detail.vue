<!--
 * @Author: yaowei
 * @Date: 2018-05-16 14:47:12
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-08 10:28:30
-->
<template>
  <div class="add-wrap">
    <header>
      <h4 class="header_h4" v-if="Number(checkMsg) === 1">创建营销专题活动</h4>
      <h4 class="header_h4" v-if="Number(checkMsg) === 2">营销专题活动详情</h4>
      <el-button
        class="mini-back-btn btn-home"
        icon="el-icon-d-arrow-left"
        @click="handleBack"
        >返回营销专题活动</el-button
      >
    </header>

    <div class="content">
      <el-form
        :model="formData"
        :rules="rules"
        label-width="21%"
        label-position="right"
        ref="ruleForm"
      >
        <el-row>
          <el-col :span="18">
            <el-form-item label="活动所属平台" prop="activityPlatform">
              <el-select
                v-model="formData.activityPlatform"
                placeholder="活动平台"
              >
                <el-option label="兑换商城" :value="1"> </el-option>
                <!-- <el-option label="定制商城" :value="2"> </el-option> -->
              </el-select>
            </el-form-item>
            <el-form-item label="活动标题" prop="title">
              <el-input v-model="formData.title" placeholder="不超30字" />
            </el-form-item>
            <!-- 活动页面配图 -->
            <el-form-item label="活动页面配图" prop="pageImg">
              <template>
                <el-upload
                  class="avatar-uploader size1"
                  :action="actionImg"
                  :show-file-list="false"
                  :on-success="handleSuccessPageImg"
                  :before-upload="beforeUploadPageImg"
                  :disabled="isDisabled"
                >
                  <img
                    v-if="formData.pageImg"
                    :src="formData.pageImg"
                    class="avatar"
                  />
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
              </template>
              <p class="tips">
                图片尺寸要求：494px*312px，支持的图片格式：jpg/jpeg/png，图片大小不超1M
              </p>
            </el-form-item>
            <el-form-item label="活动页面引导文案" prop="pageGuideText">
              <el-input
                v-model="formData.pageGuideText"
                placeholder="不超32个字，示例：免费手机壳兑换卡送不停~"
              />
            </el-form-item>
            <el-form-item label="活动福利类型" prop="type">
              <el-radio-group v-model="formData.type" size="mini">
                <el-radio :label="1">电子兑换卡</el-radio>
              </el-radio-group>
            </el-form-item>

            <!-- 关联兑换卡活动（只能关联一个） -->
            <el-form-item label="关联兑换卡活动" prop="activityData">
              <el-button
                class="mini-search-btn add-goods-btn"
                icon="el-icon-plus"
                @click="activityShow = true"
                >关联兑换卡活动</el-button
              >
              <el-table
                v-if="activityData.length > 0"
                :data="activityData"
                header-row-class-name="header-row"
                border
                row-key="id"
                class="tableCenter"
              >
                <el-table-column
                  label="兑换卡活动名称"
                  align="center"
                  prop="activityName"
                ></el-table-column>
                <el-table-column
                  label="数量（可用）"
                  align="center"
                  prop="activityCount"
                ></el-table-column>
                <el-table-column label="操作" align="center">
                  <template slot-scope="scope">
                    <el-button
                      class="mini-delete-btn"
                      @click="handleDeleteActivity(scope.$index)"
                      >移除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <!-- 添加兑换卡活动 -->
              <el-dialog
                :modal-append-to-body="false"
                :visible="activityShow"
                :before-close="activityCancel"
                width="80%"
              >
                <select-activity
                  :activityData="activityData"
                  :selectType="'electric'"
                  ref="selectActivity"
                  @disClose="activityClose"
                  @disSubmit="activitySubmit"
                >
                </select-activity>
              </el-dialog>
            </el-form-item>

            <el-form-item label="转发文案" prop="forwardText">
              <el-input
                v-model="formData.forwardText"
                placeholder="不超32个字，示例：【转赠】您的好友向你赠送了一张'手机壳'兑换卡，赶紧查收吧~"
              />
            </el-form-item>
            <!-- 活动转发封面 -->
            <el-form-item label="活动转发封面" prop="forwardImg">
              <template>
                <el-upload
                  class="avatar-uploader size2"
                  :action="actionImg"
                  :show-file-list="false"
                  :on-success="handleSuccessForwardImg"
                  :before-upload="beforeUploadForwardImg"
                  :disabled="isDisabled"
                >
                  <img
                    v-if="formData.forwardImg"
                    :src="formData.forwardImg"
                    class="avatar"
                  />
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
              </template>
              <p class="tips">
                图片尺寸要求：450px*360px，支持的图片格式：jpg/jpeg/png，图片大小不超1M
              </p>
            </el-form-item>
            <!-- 朋友圈分享图 -->
            <el-form-item label="朋友圈分享图" prop="friendImg">
              <template>
                <el-upload
                  class="avatar-uploader size3"
                  :action="actionImg"
                  :show-file-list="false"
                  :on-success="handleSuccessFriendImg"
                  :before-upload="beforeUploadFriendImg"
                  :disabled="isDisabled"
                >
                  <img
                    v-if="formData.friendImg"
                    :src="formData.friendImg"
                    class="avatar"
                  />
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
              </template>
              <p class="tips">
                图片尺寸要求：540px*800px，支持的图片格式：jpg/jpeg/png，图片大小不超1M
              </p>
            </el-form-item>

            <!-- 发卡分销商（可关联多个分销商） -->
            <el-form-item label="发卡分销商">
              <el-button
                class="mini-search-btn add-goods-btn"
                icon="el-icon-plus"
                @click="distriShow = true"
                >添加分销商</el-button
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
                  label="B2B编码"
                  align="center"
                  prop="distributorId"
                  width="100"
                ></el-table-column>
                <el-table-column
                  label="分销名称"
                  align="center"
                  prop="distributorName"
                ></el-table-column>
                <el-table-column
                  label="状态"
                  align="center"
                  width="100"
                  v-if="Number(checkMsg) !== 1"
                >
                  <template slot-scope="scope">
                    <span v-if="scope.row.status === 1">启用</span>
                    <span v-else>停用</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="操作"
                  align="center"
                  v-if="Number(checkMsg) !== 1"
                >
                  <template slot-scope="scope">
                    <el-button
                      class="mini-freeze-btn"
                      v-if="Number(scope.row.status) === 1"
                      @click="handleUpdateDistri(scope.row.id, 0)"
                      >停用
                    </el-button>
                    <el-button
                      class="mini-tableadd-btn"
                      v-else
                      @click="handleUpdateDistri(scope.row.id, 1)"
                      >启用
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
              <!-- 添加分销商 -->
              <el-dialog
                :modal-append-to-body="false"
                :visible="distriShow"
                :before-close="distriCancel"
                width="80%"
              >
                <select-distributor
                  :distributorData="distriData"
                  :isSelectable="true"
                  ref="selectDistributor"
                  @disClose="distriClose"
                  @disSubmit="distriSubmit"
                >
                </select-distributor>
              </el-dialog>
            </el-form-item>

            <el-form-item label="结束时间" prop="endTime">
              <el-date-picker
                v-model="formData.endTime"
                type="datetime"
                :picker-options="expireTimeOption"
                @input="handleDate"
                placeholder="选择时间"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div class="clearfix footbtn">
        <div>
          <el-button
            class="mini-search-btn box-btn"
            @click="handleSave('ruleForm')"
            :loading="loading"
            v-if="!isDisabled"
            >保存提交</el-button
          >
          <el-button size="mini" @click="handleBack">返回</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import selectDistributor from "@/views/storeLayout/rxComponents/selectDistributor";
import selectActivity from "@/views/rxManage/components/selectActivity";
import {monthDay} from '@/utils/timeFormat.js'
export default {
  name: "cardSpecialActivityDetail",
  components: {
    selectDistributor,
    selectActivity,
  },
  data() {
    // 关联兑换卡活动
    var validateActivityData = (rule, value, callback) => {
      if (this.activityData && this.activityData.length <= 0) {
        callback(new Error("请关联兑换卡活动"));
      } else {
        callback();
      }
    };

    return {
      actionImg: process.env.BASE_API + "system/v1/web/admin/oss/sts",
      loading: false,
      id: this.$route.query.id,
      checkMsg: this.$route.query.checkMsg,
      isDisabled: false,
      formData: {
        activityPlatform: 1,
        title: "",
        pageImg: "",
        pageGuideText: "",
        type: 1,
        forwardText: "",
        forwardImg: "",
        friendImg: "",
        endTime: "",
        exchangeId: "",
        distributorIds: [],
      },
      // 关联兑换卡活动
      activityData: [],
      activityShow: false,
      // 发卡分销商
      distriData: [],
      distriShow: false,
      expireTimeOption: {
        // 限制可选日期，不小于当天
        disabledDate(date) {
          return date.getTime() < Date.now() - 24 * 60 * 60 * 1000;
        },
      },
      rules: {
        activityPlatform: [
          {
            required: true,
            message: "请选择活动所属平台",
            trigger: ["blur", "change"],
          },
        ],
        title: [
          {
            required: true,
            message: "请输入活动标题",
            trigger: ["blur", "change"],
          },
          {
            validator: (rule, value, callback) => {
              if (value) {
                var str = value + "";
                var len = 0;
                for (var i = 0; i < str.length; i++) {
                  var c = str.charCodeAt(i);
                  // 单字节加1
                  if (
                    (c >= 0x0001 && c <= 0x007e) ||
                    (0xff60 <= c && c <= 0xff9f)
                  ) {
                    len++;
                  } else {
                    len += 2;
                  }
                }
                if (len > 60) {
                  callback(new Error("最多展示30个字，60个字符"));
                } else {
                  callback();
                }
              } else {
                callback();
              }
            },
          },
        ],
        pageImg: [
          {
            required: true,
            message: "请上传活动页面配图",
            trigger: "blur",
          },
        ],
        pageGuideText: [
          {
            required: true,
            message: "请输入活动页面引导文案",
            trigger: ["blur", "change"],
          },
          {
            validator: (rule, value, callback) => {
              if (value) {
                var str = value + "";
                var len = 0;
                for (var i = 0; i < str.length; i++) {
                  var c = str.charCodeAt(i);
                  // 单字节加1
                  if (
                    (c >= 0x0001 && c <= 0x007e) ||
                    (0xff60 <= c && c <= 0xff9f)
                  ) {
                    len++;
                  } else {
                    len += 2;
                  }
                }
                if (len > 64) {
                  callback(new Error("最多展示32个字，64个字符"));
                } else {
                  callback();
                }
              } else {
                callback();
              }
            },
          },
        ],
        type: [
          {
            required: true,
            message: "请选择活动福利类型",
            trigger: ["blur", "change"],
          },
        ],
        activityData: [
          {
            required: true,
            validator: validateActivityData,
            trigger: "change",
          },
        ],
        forwardText: [
          {
            required: true,
            message: "请输入转发文案",
            trigger: ["blur", "change"],
          },
          {
            validator: (rule, value, callback) => {
              if (value) {
                var str = value + "";
                var len = 0;
                for (var i = 0; i < str.length; i++) {
                  var c = str.charCodeAt(i);
                  // 单字节加1
                  if (
                    (c >= 0x0001 && c <= 0x007e) ||
                    (0xff60 <= c && c <= 0xff9f)
                  ) {
                    len++;
                  } else {
                    len += 2;
                  }
                }
                if (len > 64) {
                  callback(new Error("最多展示32个字，64个字符"));
                } else {
                  callback();
                }
              } else {
                callback();
              }
            },
          },
        ],
        forwardImg: [
          {
            required: true,
            message: "请上传活动转发封面",
            trigger: "blur",
          },
        ],
        friendImg: [
          {
            required: true,
            message: "请上传朋友圈分享图",
            trigger: "blur",
          },
        ],
        endTime: [
          {
            required: true,
            message: "请选择结束时间",
            trigger: ["blur", "change"],
          },
        ],
      },
      hostname: "",
    };
  },
  created() {
    if (Number(this.checkMsg) === 2) {
      this.initData(Number(this.id));
    }
  },
  methods: {
    // 初始化数据
    initData(id) {
      // 查看详情
      this.$http.exchangeSpecialDetail(this, { id: id }).then((res) => {
        if (res.success) {
          this.formData = res.data;
          // 分销商详情
          this.distriData = res.data.exchangeSpecialDistributorCOS;
          if (this.distriData && this.distriData.length > 0) {
            this.distriData.forEach((distri) => {
              this.getDistributorByIds([distri.distributorId], distri);
            });
          }
          // 兑换卡活动详情
          this.getExchangeCardDetail(res.data.exchangeId);
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
    // 活动页面配图
    handleSuccessPageImg(response, file) {},
    beforeUploadPageImg(file) {
      if (
        file.type != "image/jpg" &&
        file.type != "image/jpeg" &&
        file.type != "image/png"
      ) {
        this.$message.error("上传图片只能是 jpg、jpeg、png 格式!");
        return false;
      }
      const isLt1M = file.size / 1024 / 1024 < 1;
      if (!isLt1M) {
        this.$message.error("上传图片大小不能超过 1MB!");
        return isLt1M;
      }
      // 随机命名
      let random_name =
        this.random_string(6) +
        "_" +
        new Date().getTime() +
        "." +
        file.name.split(".").pop();
      this.$http.getFileSts(this).then((result) => {
        const client = new OSS.Wrapper({
          region: result.data.region,
          accessKeyId: result.data.accessKeyId,
          accessKeySecret: result.data.accessKeySecret,
          stsToken: result.data.securityToken,
          bucket: result.data.bucketName,
          endpoint: result.data.endpoint,
          secure: true,
        });
        this.hostname = result.data.hostname;
        // 上传
        client
          .multipartUpload('flexible/other/' + monthDay(new Date()) + '/' + random_name, file, {})
          .then((results) => {
            return new Promise((resolve, reject) => {
              this.$set(this.formData, "pageImg", this.hostname + results.name),
                resolve(true);
            });
          })
          .catch((err) => {
            console.log(err);
          });
      });
    },
    // 活动转发封面
    handleSuccessForwardImg(response, file) {},
    beforeUploadForwardImg(file) {
      if (
        file.type != "image/jpg" &&
        file.type != "image/jpeg" &&
        file.type != "image/png"
      ) {
        this.$message.error("上传图片只能是 jpg、jpeg、png 格式!");
        return false;
      }
      const isLt1M = file.size / 1024 / 1024 < 1;
      if (!isLt1M) {
        this.$message.error("上传图片大小不能超过 1MB!");
        return isLt1M;
      }
      // 随机命名
      let random_name =
        this.random_string(6) +
        "_" +
        new Date().getTime() +
        "." +
        file.name.split(".").pop();
      this.$http.getFileSts(this).then((result) => {
        const client = new OSS.Wrapper({
          region: result.data.region,
          accessKeyId: result.data.accessKeyId,
          accessKeySecret: result.data.accessKeySecret,
          stsToken: result.data.securityToken,
          bucket: result.data.bucketName,
          endpoint: result.data.endpoint,
          secure: true,
        });
        this.hostname = result.data.hostname;
        // 上传
        client
          .multipartUpload('flexible/other/' + monthDay(new Date()) + '/' + random_name, file, {})
          .then((results) => {
            return new Promise((resolve, reject) => {
              this.$set(
                this.formData,
                "forwardImg",
                this.hostname + results.name
              ),
                resolve(true);
            });
          })
          .catch((err) => {
            console.log(err);
          });
      });
    },
    // 朋友圈分享图
    handleSuccessFriendImg(response, file) {},
    beforeUploadFriendImg(file) {
      if (
        file.type != "image/jpg" &&
        file.type != "image/jpeg" &&
        file.type != "image/png"
      ) {
        this.$message.error("上传图片只能是 jpg、jpeg、png 格式!");
        return false;
      }
      const isLt1M = file.size / 1024 / 1024 < 1;
      if (!isLt1M) {
        this.$message.error("上传图片大小不能超过 1MB!");
        return isLt1M;
      }
      // 随机命名
      let random_name =
        this.random_string(6) +
        "_" +
        new Date().getTime() +
        "." +
        file.name.split(".").pop();
      this.$http.getFileSts(this).then((result) => {
        const client = new OSS.Wrapper({
          region: result.data.region,
          accessKeyId: result.data.accessKeyId,
          accessKeySecret: result.data.accessKeySecret,
          stsToken: result.data.securityToken,
          bucket: result.data.bucketName,
          endpoint: result.data.endpoint,
          secure: true,
        });
        this.hostname = result.data.hostname;
        // 上传
        client
          .multipartUpload('flexible/other/' + monthDay(new Date()) + '/' + random_name, file, {})
          .then((results) => {
            return new Promise((resolve, reject) => {
              this.$set(
                this.formData,
                "friendImg",
                this.hostname + results.name
              ),
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
    // 保存提交
    handleSave(formName) {
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false;
        } else {
          if (Number(this.checkMsg) === 1) {
            // 新增
            this.$http.addExchangeSpecial(this, this.formData).then((res) => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "活动新增成功",
                });
                this.clickLeave();
              }
            });
          } else {
            // 修改
            delete this.formData.exchangeSpecialDistributorCOS;
            this.$http.editExchangeSpecial(this, this.formData).then((res) => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "活动修改成功",
                });
                this.clickLeave();
              }
            });
          }
        }
      });
    },
    // 重置表单验证
    resetValidate(formName) {
      this.$refs[formName].resetFields();
    },
    // 返回
    handleBack() {
      this.resetValidate("ruleForm");
      this.clickLeave();
    },
    // 页面跳转
    clickLeave() {
      this.$router.go(-1);
    },

    // 关联兑换卡活动
    activitySubmit(msg) {
      if (msg.length > 1) {
        this.$message({
          type: "warning",
          message: "只能关联一个兑换卡活动！",
        });
      } else {
        this.activityData = msg;
        this.activityShow = false;
        if (this.activityData.length > 0) {
          this.formData.exchangeId = this.activityData[0].id;
          this.$refs["ruleForm"].clearValidate(["activityData"]);
        }
      }
    },
    activityCancel() {
      this.$refs.selectActivity.handleCancel();
    },
    activityClose() {
      this.activityShow = false;
    },
    // 移除关联兑换卡活动
    handleDeleteActivity(index) {
      this.activityData.splice(index, 1);
    },

    // 发卡分销商
    distriSubmit(msg) {
      if (Number(this.checkMsg) !== 1) {
        // 非新增状态
        if (msg.length > 0) {
          let ids = [];
          msg.forEach((distri) => {
            let isExist = false;
            this.distriData.forEach((item) => {
              if (item.distributorId === distri.distributorId) {
                isExist = true;
              }
            });

            if (!isExist) {
              ids.push(distri.distributorId);
            }
          });

          // 专题修改 - 添加分销商
          if (ids.length > 0) {
            this.$confirm("请确保添加无误，确认后不能移除哦~", "提示", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning",
              center: true,
            })
              .then((_) => {
                this.handleAddDistri(ids);

                // 处理分销商数据
                this.distriData = msg;
                this.distriShow = false;
                if (this.distriData.length > 0) {
                  let distributorIds = [];
                  this.distriData.forEach((distri) => {
                    distributorIds.push(distri.distributorId);
                  });

                  this.formData.distributorIds = distributorIds;
                }
              })
              .catch((_) => {
                this.$message({
                  type: "info",
                  message: "已取消操作",
                });
              });
          }
        }
      } else {
        // 新增
        // 处理分销商数据
        this.distriData = msg;
        this.distriShow = false;
        if (this.distriData.length > 0) {
          let distributorIds = [];
          this.distriData.forEach((distri) => {
            distributorIds.push(distri.distributorId);
          });

          this.formData.distributorIds = distributorIds;
        }
      }
    },
    distriCancel() {
      this.$refs.selectDistributor.handleCancel();
    },
    distriClose() {
      this.distriShow = false;
    },

    // 监听时间选择
    handleDate(value) {
      this.formData.endTime = new Date(value).getTime();
      this.$forceUpdate();
    },

    // 兑换卡活动详情
    getExchangeCardDetail(id) {
      this.$http.exchangeCardDetail(this, { id: id }).then((res) => {
        if (res.success) {
          this.activityData = [];
          let activityData = {
            activityName: res.data.codeName,
            activityCount: res.data.codeQuantity - res.data.saleQuantity,
          };
          this.activityData.push(activityData);
        }
      });
    },
    // 专题修改 - 添加分销商
    handleAddDistri(distributorIds) {
      this.$http
        .exchangeSpecialDistriAdd(this, {
          exchangeSpecialId: this.id,
          distributorIds: distributorIds,
        })
        .then((res) => {
          if (res.success) {
            this.$message({
              type: "success",
              message: "添加成功",
            });
          }
        });
    },
    // 专题修改 - 启用/停用分销商
    handleUpdateDistri(id, status) {
      let msg = "";
      if (status === 1) {
        msg = "此操作将启用该发卡分销商，是否继续？";
      } else {
        msg = "此操作将停用该发卡分销商，是否继续？";
      }
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          this.$http
            .exchangeSpecialDistriUpdate(this, {
              id: id,
              status: status,
            })
            .then((res) => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "操作成功",
                });
                this.initData(Number(this.id));
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
  },
};
</script>

<style rel="stylesheet/scss" lang="scss">
.add-wrap {
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
    min-width: 900px;
    .footbtn {
      padding-top: 30px;
      padding-bottom: 30px;
      text-align: center;
      .box-btn + .box-btn {
        margin-left: 10px;
      }
    }
  }
}

.avatar-uploader {
  .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;

    &:hover {
      border-color: rgb(33, 184, 203);
    }
  }

  &.size1 {
    .avatar-uploader-icon {
      width: 247px;
      height: 156px;
      line-height: 156px;
    }

    .avatar {
      width: 247px;
      height: 156px;
    }
  }

  &.size2 {
    .avatar-uploader-icon {
      width: 225px;
      height: 180px;
      line-height: 180px;
    }

    .avatar {
      width: 225px;
      height: 180px;
    }
  }

  &.size3 {
    .avatar-uploader-icon {
      width: 270px;
      height: 400px;
      line-height: 400px;
    }

    .avatar {
      width: 270px;
      height: 400px;
    }
  }
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  text-align: center;
}

.avatar {
  display: block;
}

.tips {
  padding-bottom: 10px;
  color: #666666;
  line-height: 20px;
  &.red {
    color: #e8574f;
  }
}
</style>