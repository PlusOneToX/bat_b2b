<template>
  <div class="abnormal-wrap">
    <header>
      <h4 class="header_h4">柔性同步订单列表</h4>
    </header>
    <div class="order-list">
      <div class="order-list-fun">
        <div class="order-list-search">
          <div class="order-list-header">
            <div>
              <el-select
                size="mini"
                v-model="pageInfo.platform"
                placeholder="渠道来源"
                style="width: 140px"
                clearable
              >
                <el-option
                  v-for="item in platforms"
                  :label="item.name"
                  :value="item.platformNo"
                  :key="item.platformNo"
                ></el-option>
              </el-select>
              <el-select
                size="mini"
                v-model="pageInfo.status"
                placeholder="同步状态"
                style="width: 100px"
                clearable
              >
                <el-option label="同步成功" value="1"></el-option>
                <el-option label="同步失败" value="0"></el-option>
              </el-select>
              <el-date-picker
                size="mini"
                v-model="pageInfo.time"
                style="width: 330px"
                type="datetimerange"
                value-format="timestamp"
                range-separator="至"
                start-placeholder="下单开始日期"
                end-placeholder="下单结束日期"
              ></el-date-picker>
            </div>
            <div class="order-list-sear">
              <el-select
                size="mini"
                v-model="pageInfo.searchType"
                placeholder="类型"
                style="width: 120px"
                clearable
              >
                <el-option label="渠道订单号" value="1"></el-option>
                <el-option label="收货人" value="2"></el-option>
                <el-option label="手机号" value="3"></el-option>
              </el-select>
              <el-input
                placeholder="请输入搜索关键字"
                @keyup.enter.native="onSearch()"
                clearable
                v-model.trim="pageInfo.content"
                size="mini"
                class="box-search"
              ></el-input>
              <button
                class="mini-search-btn btn-box"
                @click.prevent="onSearch()"
              >
                搜索
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <el-row>
      <el-table
        :data="tableData"
        header-row-class-name="header-row"
        border
        ref="multipleTable"
        class="tableCenter"
        v-loading="loading"
        :height="tableHeight"
      >
        <el-table-column
          label="序号"
          align="center"
          prop="id"
          min-width="60"
        ></el-table-column>
        <el-table-column
          label="渠道来源"
          align="center"
          prop="platform"
          min-width="90"
        ></el-table-column>
        <el-table-column
          label="渠道订单号"
          align="center"
          prop="otherOrderNo"
          min-width="120"
        ></el-table-column>
        <el-table-column
          label="收货人"
          align="center"
          prop="receiver"
          min-width="120"
        ></el-table-column>
        <el-table-column
          label="手机号"
          align="center"
          prop="mobile"
          min-width="110"
        ></el-table-column>
        <el-table-column
          label="地址"
          align="center"
          prop="address"
          min-width="120"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          label="下单时间"
          align="center"
          prop="createTime"
          :formatter="formatTime"
          min-width="150"
        ></el-table-column>
        <el-table-column
          label="订单同步状态"
          align="center"
          prop="status"
          min-width="120"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.status == 0 ? "同步失败" : "同步成功" }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="异常原因"
          align="center"
          prop="errorMsg"
          min-width="150"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column label="操作" align="center" min-width="180">
          <template slot-scope="scope">
            <el-button @click="handleDetail(scope.row)" class="mini-search-btn"
              >查看</el-button
            >
            <el-button
              @click="handleOrder(scope.row)"
              class="mini-freeze-btn"
              v-show="scope.row.status == 0"
              :loading="!scope.row.flag"
              >同步订单</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <pagination
        :total="total"
        :page="pageInfo.page"
        @sizeChange="onSizeCHange"
        @currentChange="onCurrentChange"
      ></pagination>
    </el-row>

    <!-- 查看 -->
    <el-dialog
      title="同步订单详情"
      width="65%"
      :visible.sync="dialogVisible"
      @close="clearInput('ruleForm')"
    >
      <div class="details-wrap">
        <h6>订单信息</h6>
        <p>
          <label>
            <span>渠道单号：</span>
            {{ detailsData.otherOrderNo }}
          </label>
          <label>
            <span>渠道来源：</span>
            {{ detailsData.platform }}
          </label>
        </p>
        <p>
          <label>
            <span>下单时间：</span>
            {{ curCreateTime }}
          </label>
        </p>
        <p>
          <label>
            <span>同步状态：</span>
            {{ detailsData.status == 0 ? "同步失败" : "同步成功" }}
          </label>
          <label>
            <span>异常原因：</span>
            {{ detailsData.errorMsg }}
          </label>
        </p>
        <h6>收货人信息</h6>
        <p>
          <label>
            <span>收件人：</span>
            {{ detailsData.receiver }}
          </label>
          <label>
            <span>手机号：</span>
            {{ detailsData.mobile }}
          </label>
        </p>
        <p>
          <label>
            <span>收货地址：</span>
            {{ detailsData.address }}
          </label>
          <el-button
            class="mini-search-btn"
            size="mini"
            @click="editAddr()"
            v-show="detailsData.status == 0"
            >修改收货地址</el-button
          >
        </p>
        <h6 v-show="editAddrVisible">修改地址</h6>
        <el-form
          :model="formData"
          :rules="rules"
          ref="ruleForm"
          v-show="editAddrVisible"
        >
          <el-form-item label="所在地区：" required>
            <el-col :span="4">
              <el-form-item prop="countryId">
                <el-select
                  v-model="formData.countryId"
                  placeholder="请选择"
                  size="mini"
                  filterable
                  @change="countryChange($event)"
                >
                  <el-option
                    v-for="country in this.RegionForChose.id"
                    :key="country.id"
                    :label="country.regionName"
                    :value="country.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item prop="provinceId">
                <el-select
                  v-model="formData.provinceId"
                  v-show="provinceShow"
                  placeholder="请选择"
                  size="mini"
                  style="margin-left: 10px"
                  filterable
                  @change="provinceIdChange($event)"
                >
                  <el-option
                    v-for="province in this.RegionForChose.provinceId"
                    :key="province.id"
                    :label="province.regionName"
                    :value="province.id"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item>
                <el-select
                  v-model="formData.cityId"
                  v-show="cityShow"
                  placeholder="请选择"
                  size="mini"
                  style="margin-left: 10px"
                  filterable
                  @change="cityIdChange($event)"
                >
                  <el-option
                    v-for="city in this.RegionForChose.cityId"
                    :key="city.id"
                    :label="city.regionName"
                    :value="city.id"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item>
                <el-select
                  v-model="formData.districtId"
                  v-if="townShow"
                  placeholder="请选择"
                  size="mini"
                  style="margin-left: 10px"
                  filterable
                  @change="townIdChange($event)"
                >
                  <el-option
                    v-for="town in this.RegionForChose.districtId"
                    :key="town.id"
                    :label="town.regionName"
                    :value="town.id"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item label="收货地址：" prop="detail" class="detailAddr">
            <el-input
              v-model="formData.detail"
              size="mini"
              placeholder="请输入收货地址"
              clearable
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="btn-wrap">
        <el-button
          @click="handleSave('ruleForm')"
          class="mini-search-btn"
          v-show="detailsData.status == 0"
          >确定</el-button
        >
        <el-button size="mini" @click="clearInput('ruleForm')">返回</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script type="text/javascript">
import { timeFormat } from "@/utils/timeFormat";
import pagination from "@/components/pagination/index";
export default {
  name: "customerlist",
  components: { pagination },
  data() {
    /**
     * 验证国内客户省市县必填
     * @param rule
     * @param value
     * @param callback
     */
    const validateChinaProvince = (rule, value, callback) => {
      if (this.formData.countryId === 37) {
        if (!this.formData.provinceId) {
          callback(new Error("中国地区，请选择省市"));
        } else {
          callback();
        }
      } else {
        callback();
      }
    };
    return {
      pageInfo: {
        page: 1,
        size: 10,
        platform: undefined,
        searchType: undefined,
        content: undefined, // 搜索用关键词
      },
      total: 0,
      loading: false,
      tableHeight: 600,
      tableData: [],
      dialogVisible: false,
      editAddrVisible: false,
      detailsData: {}, // 当前查看 id 详情
      formData: {
        countryId: "",
        provinceId: "",
        cityId: "",
        districtId: "",
        detail: "",
      },
      provinceShow: false,
      cityShow: false,
      townShow: false,
      RegionForChose: {
        // 当前可选地址
        id: [],
        provinceId: [],
        cityId: [],
        districtId: [],
      },
      curAddr: {
        // 修改后地址
        country: "",
        province: "",
        city: "",
        area: "",
      },
      curId: "", // 当前查看 id
      curCreateTime: "", // 当前查看 id 下单时间
      rules: {
        countryId: [
          { required: true, message: "请选择国家", trigger: "change" },
        ],
        provinceId: [
          { required: true, message: "请选择省市", trigger: "change" },
          { validator: validateChinaProvince },
        ],
        detail: [
          { required: true, message: "请输入收货地址", trigger: "blur" },
        ],
      },
      flag: true, // 防重复点击
      platforms: [], // 渠道来源
    };
  },
  created() {
    this.getForm();
    this.initData();
  },
  activated() {
    this.initData();
  },
  methods: {
    onSearch() {
      // 搜索操作
      this.pageInfo.page = 1;
      this.initData();
    },
    // 获取渠道来源
    getForm() {
      this.$http
        .getSysPlatformPoList(this, { page: 1, size: 100 })
        .then((res) => {
          if (res.success) {
            this.platforms = res.data.list;
          }
        });
    },
    // 初始化数据
    initData() {
      this.$http.customerSyncOrderList(this, this.pageInfo).then((res) => {
        if (res.success) {
          this.tableData = res.data.list;

          this.tableData.forEach((item) => {
            this.$set(item, "flag", true);
          });
          this.total = res.data.total;
        }
      });
    },
    onSizeCHange(val) {
      // 分页方法
      this.pageInfo.size = val;
      this.pageInfo.page = 1;
      this.initData();
    },

    onCurrentChange(val) {
      // 分页方法
      this.pageInfo.page = val;
      this.initData();
    },
    // 查看
    handleDetail(row) {
      this.curId = row.id;
      this.curCreateTime = timeFormat(row.createTime);
      this.detailsData = row;
      this.dialogVisible = true;
    },
    // 修改地址
    editAddr() {
      this.editAddrVisible = true;
      let count = 2;
      this.$http
        .regionListById(this, {
          page: 1,
          size: 200,
          parentId: 0,
        })
        .then((res) => {
          if (res.success) {
            this.RegionForChose.id = res.data.list;
            count--;
            this.$http
              .regionListById(this, {
                page: 1,
                size: 200,
                parentId: 37,
              })
              .then((res) => {
                this.RegionForChose.provinceId = res.data.list;
                count--;
              });
          }
        });
    },
    // 同步订单
    handleOrder(obj) {
      // obj.json.orderNo = obj.orderNo;
      // let distributorId = obj.distributorId;
      // let orderSource = obj.orderSource;
      // let excaptionId = obj.id;
      this.$set(obj, "flag", true);
      var _this = this;
      if (obj.flag) {
        // _this.flag = false;
        _this.$set(obj, "flag", false);
        _this.$http
          .orderPushAgain(_this, { id: obj.id })
          .then(function (res) {
            if (res.success) {
              _this.$message({
                message: "同步成功",
                type: "success",
                duration: 3 * 1000,
                onClose: () => {
                  _this.initData();
                },
              });
            } else {
              _this.$message({
                message: res.errMessage,
                type: "error",
                duration: 3 * 1000,
                onClose: () => {
                  _this.initData();
                },
              });
            }
            // _this.flag = true;
            _this.$set(obj, "flag", true);
          })
          .catch(function (error) {
            _this.$message({
              message: error,
              type: "error",
              duration: 3 * 1000,
              onClose: () => {
                _this.initData();
              },
            });
            // _this.flag = true;
            _this.$set(obj, "flag", true);
          });
      }
    },
    // 更新
    handleSave(formName) {
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false;
        } else {
          this.curAddr.area =
            this.curAddr.province.indexOf("市") > -1
              ? this.curAddr.city
              : this.curAddr.area;
          this.curAddr.city =
            this.curAddr.province.indexOf("市") > -1
              ? this.curAddr.province
              : this.curAddr.city;
          let params = {
            address: {
              province: this.curAddr.province,
              city: this.curAddr.city,
              area: this.curAddr.area,
              detail: this.formData.detail,
            },
            id: this.curId,
          };
          this.$http
            .updateAddress(this, params)
            .then((res) => {
              if (res.success) {
                this.dialogVisible = false;
                this.editAddrVisible = false;
                // 重置表单
                this.$refs[formName].resetFields();
                this.formData.countryId = "";
                this.provinceShow = false;
                this.cityShow = false;
                this.townShow = false;
                this.$message({
                  message: "更新成功",
                  type: "success",
                  duration: 3 * 1000,
                  onClose: () => {
                    this.initData();
                  },
                });
              } else {
                this.$message.error(res.errMessage);
              }
            })
            .catch((error) => {
              console.log(error);
            });
        }
      });
    },
    // 关闭/返回
    clearInput(formName) {
      this.dialogVisible = false;
      this.editAddrVisible = false;
      // 重置表单
      this.$refs[formName].resetFields();
      this.provinceShow = false;
      this.cityShow = false;
      this.townShow = false;
    },
    // 表格时间格式化
    formatTime(row, col, val) {
      return timeFormat(val);
    },
    countryChange(event) {
      // 国家
      this.formData.countryId = event;
      if (event !== 37) {
        // 国外
        this.provinceShow = false;
        this.cityShow = false;
        this.townShow = false;
        this.formData.provinceId = "-";
        this.formData.cityId = "-";
        this.formData.districtId = "-";
        this.curAddr.province = "-";
        this.curAddr.city = "-";
        this.curAddr.area = "-";
      } else if (event === 37) {
        // 37中国
        this.provinceShow = true; // 中国显示省份
        this.formData.provinceId = "";
        this.formData.cityId = "";
        this.formData.districtId = "";
        this.curAddr.province = "";
        this.curAddr.city = "";
        this.curAddr.area = "";
      }
    },
    // 省
    provinceIdChange(event) {
      this.RegionForChose.provinceId.forEach((element) => {
        if (element.id == event) {
          this.curAddr.province = element.regionName;
        }
      });
      this.$http
        .regionListById(this, {
          page: 1,
          size: 200,
          parentId: event,
        })
        .then((res) => {
          if (res.success) {
            this.cityShow = true;
            this.RegionForChose.cityId = res.data.list;
            this.formData.cityId = res.data.list[0].id;
            this.curAddr.city = res.data.list[0].regionName;
            if (res.data.list[0].haveNext === 1) {
              this.$http
                .regionListById(this, {
                  page: 1,
                  size: 200,
                  parentId: this.formData.cityId,
                })
                .then((res) => {
                  this.RegionForChose.districtId = res.data.list;
                  this.formData.districtId =
                    this.RegionForChose.districtId[1].id;
                  this.curAddr.area =
                    this.RegionForChose.districtId[1].regionName;
                });
              this.townShow = true;
            } else {
              this.townShow = false;
              this.formData.districtId = 0;
            }
          }
        });
    },
    // 市
    cityIdChange(event) {
      this.RegionForChose.cityId.forEach((element) => {
        if (element.id == event) {
          this.curAddr.city = element.regionName;
        }
      });
      this.$http
        .regionListById(this, {
          page: 1,
          size: 200,
          parentId: event,
        })
        .then((res) => {
          if (res.success) {
            if (res.data.list.length === 0) {
              this.townShow = false;
              this.formData.districtId = 0;
            } else {
              this.RegionForChose.districtId = res.data.list;
              this.formData.districtId = this.RegionForChose.districtId[1].id;
              this.curAddr.area = this.RegionForChose.districtId[1].regionName;
              this.townShow = true;
            }
          }
        });
    },
    // 区
    townIdChange(event) {
      this.RegionForChose.districtId.forEach((element) => {
        if (element.id == event) {
          this.curAddr.area = element.regionName;
        }
      });
    },
  },
  watch: {
    "pageInfo.platform": {
      // 付款状态
      handler() {
        this.pageInfo.page = 1;
        this.initData();
      },
      deep: true,
    },
    "pageInfo.status": {
      // 付款状态
      handler() {
        this.pageInfo.page = 1;
        this.initData();
      },
      deep: true,
    },
    "pageInfo.time": {
      // 时间
      handler(val) {
        if (val) {
          this.pageInfo.startTime = timeFormat(val[0]);
          this.pageInfo.endTime = timeFormat(val[1]);
        } else {
          this.pageInfo.startTime = undefined;
          this.pageInfo.endTime = undefined;
        }
        this.pageInfo.page = 1;
        this.initData();
      },
      deep: true,
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.abnormal-wrap {
  .header_h4 {
    margin: 0 0 0 20px;
  }
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
    h4 {
      font-weight: 400;
    }
  }
}
.cover {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1000;
}
.details-wrap {
  h5 {
    padding-left: 30px;
    font-size: 14px;
    line-height: 40px;
    color: #21b8cb;
  }
  h6 {
    padding-top: 25px;
    padding-bottom: 10px;
    font-size: 18px;
    text-align: center;
    border-top: 1px solid #dcdcdc;
  }
  p {
    padding: 10px;
    font-size: 14px;
    display: flex;
    label {
      flex: 1;
      color: #333;
      span {
        display: inline-block;
        width: 70px;
        color: #606266;
      }
    }
    & + h6 {
      margin-top: 25px;
    }
  }
  .el-form {
    .el-form-item {
      margin-bottom: 0;
    }
    .el-input {
      width: 80%;
    }
  }
}
.btn-wrap {
  text-align: center;
}
</style>
<style rel="stylesheet/scss" lang="scss">
.detailAddr {
  margin-top: 10px;
  .el-form-item__error {
    left: 90px;
  }
}
.order-list {
  background-color: white;
  height: 100%;
  margin-top: 20px;
  .order-list-fun {
    padding: 0px 10px 10px 10px;
    overflow: hidden;
    .order-list-search {
      overflow: hidden;
      .order-list-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        .order-list-sear {
          .box-search {
            width: 180px;
          }
          .btn-box {
            position: relative;
            top: -1px;
          }
        }
      }
    }
  }
}
</style>
