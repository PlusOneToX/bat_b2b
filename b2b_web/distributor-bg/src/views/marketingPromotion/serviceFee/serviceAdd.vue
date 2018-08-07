<template>
  <div class="serviec-add">
    <header>
      <h4>添加服务费活动</h4>
      <el-button class="mini-add-btn btn-home" @click="handleBack">返回服务费列表</el-button>
    </header>
    <div v-loading="loading2" class="service-content">
      <el-form
        :model="serviceData"
        :rules="rules"
        label-width="20%"
        label-position="right"
        ref="ruleForm"
      >
        <el-row>
          <el-col :span="21">
            <div class="tip-left">
              <p>
                <code>基本信息</code>
              </p>
            </div>
            <el-form-item label="活动名称:" prop="name">
              <el-input
                v-model="serviceData.name"
                placeholder="最多展示10个字"
                maxlength="10"
              />
            </el-form-item>
            <el-form-item label="活动英文名称:" prop="enName">
              <el-input
                type="textarea"
                :rows="2"
                placeholder="最多展示10个字"
                v-model="serviceData.enName"
              />
            </el-form-item>
            <el-form-item label="活动描述:" prop="represent">
              <el-input
                type="textarea"
                :rows="6"
                placeholder="仅内部可见，不展示至前台"
                maxlength="200"
                v-model="serviceData.represent"
              />
            </el-form-item>
            <!-- <el-form-item label="活动时间:" prop="validTime">
              <el-date-picker
                v-model="serviceData.validTime"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="timestamp"
                :picker-options="expireTimeOption"
                @input="handleDate"
                :disabled="isDisabled"
              ></el-date-picker>
            </el-form-item> -->

            <div class="tip-left">
              <p>
                <code>规则</code>
              </p>
            </div>
            <el-form-item label="服务费次数:" prop="needChargeNum">
              <el-input size="mini" v-model="serviceData.needChargeNum"  @input="serviceData.needChargeNum=/^\d+\.?\d{0,0}$/.test(serviceData.needChargeNum)||serviceData.needChargeNum == '' ? serviceData.needChargeNum:Number(serviceData.needChargeNum.toString().match(/^\d+(?:\.\d{0,0})?/))"  style="width: 100px;color: #606266;font-weight: 500;" :disabled = "disabled" type="number" min="0" />
            </el-form-item>
            <el-form-item label="服务费商品:" prop="saveType">
              <!-- <el-radio-group v-model="serviceData.saveType" :disabled="isDisabled">
                <el-radio :label="1">按商品选择</el-radio>
                <el-radio :label="2">按货品选择</el-radio>
              </el-radio-group> -->
              <el-button
                class="mini-search-btn add-goods-btn"
                @click="assignGoods()"
                >添加货品</el-button>
              <!-- 商品展示 -->
              <div
                class="pro-wrap"
                v-if="this.goodssItems.length > 0"
              >
                <el-table
                  :data="goodssItems"
                  border
                  header-row-class-name="header-row"
                  class="tableCenter goods-table"
                  style="width: 100%"
                  max-height="200"
                >
                  <el-table-column
                    width="120"
                    align="center"
                    show-overflow-tooltip
                    label="商品名称"
                    prop="goodsName"
                  ></el-table-column>
                  <el-table-column
                    width="115"
                    align="center"
                    label="货品编码"
                    prop="itemCode"
                  ></el-table-column>
                  <el-table-column
                    width="125"
                    align="center"
                    label="条形码"
                    prop="barCode"
                  ></el-table-column>
                  <el-table-column
                    width="80"
                    align="center"
                    show-overflow-tooltip
                    label="规格"
                    prop="specificationsName"
                  ></el-table-column>
                  <el-table-column
                    width="70"
                    align="center"
                    label="颜色"
                    prop="colorName"
                  ></el-table-column>
                  <el-table-column
                    width="110"
                    align="center"
                    label="建议零售价"
                    prop="retailPrice"
                  ></el-table-column>
                  <el-table-column
                    width="110"
                    align="center"
                    label="建议批发价"
                    prop="wholesalePrice"
                  ></el-table-column>
                  <el-table-column width="90" align="center" label="服务费单价">
                    <template slot-scope="scope" prop="serviceFee">
                      <el-input
                        size="mini"
                        v-model="scope.row.serviceFee"
                      ></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column
                    width="90"
                    align="center"
                    label="库存数"
                    prop="numInWarehouse"
                  ></el-table-column>
                  <el-table-column
                    width="180"
                    align="center"
                    label="单位（盒内数量）"
                    prop="serviceBoxNum"
                  >
                    <template slot-scope="scope">
                      <el-input
                        size="mini"
                        v-model="scope.row.serviceBoxNum"
                      ></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column
                    align="center"
                    label="操作"
                    width="80"
                  >
                    <template slot-scope="scope">
                      <el-button
                        v-if="scope.row.id === undefined || scope.row.id === null || scope.row.id === ''"
                        class="mini-delete-btn"
                        @click="handleDeleteGood(scope.$index)"
                        >删除</el-button
                      >
                    </template>
                  </el-table-column>
                </el-table>
              </div>
              <!-- 货品展示 -->
              <div
                class="pro-wrap"
                v-if="serviceData.saveType === 2 || serviceData.saveType === 1"
              >
                <el-table
                  :data="goodsItems"
                  border
                  header-row-class-name="header-row"
                  class="tableCenter goods-table"
                  style="width: 100%"
                  max-height="300"
                >
                  <el-table-column
                    width="120"
                    align="center"
                    show-overflow-tooltip
                    label="商品名称"
                    prop="goodsName"
                  ></el-table-column>
                  <el-table-column
                    width="115"
                    align="center"
                    label="货品编码"
                    prop="itemCode"
                  ></el-table-column>
                  <el-table-column
                    width="125"
                    align="center"
                    label="条形码"
                    prop="barCode"
                  ></el-table-column>
                  <el-table-column
                    width="80"
                    align="center"
                    show-overflow-tooltip
                    label="规格"
                    prop="specificationsName"
                  ></el-table-column>
                  <el-table-column
                    width="70"
                    align="center"
                    label="颜色"
                    prop="colorName"
                  ></el-table-column>
                  <el-table-column
                    width="110"
                    align="center"
                    label="建议零售价"
                    prop="retailPrice"
                  ></el-table-column>
                  <el-table-column
                    width="110"
                    align="center"
                    label="建议批发价"
                    prop="wholesalePrice"
                  ></el-table-column>
                  <el-table-column width="110" align="center" label="服务费单价">
                    <template slot-scope="scope" prop="serviceFee">
                      <el-input
                        size="mini"
                        v-model="scope.row.serviceFee"
                      ></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column
                    width="90"
                    align="center"
                    label="库存数"
                    prop="numInWarehouse"
                  ></el-table-column>
                  <el-table-column
                    width="150"
                    align="center"
                    label="单位（盒内数量）"
                    prop="serviceBoxNum"
                  >
                    <template slot-scope="scope">
                      <el-input
                        size="mini"
                        v-model="scope.row.serviceBoxNum"
                      ></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column
                    align="center"
                    label="操作"
                    width="80"
                  >
                    <template slot-scope="scope">
                      <el-button
                        v-if="scope.row.id === undefined || scope.row.id === null || scope.row.id === ''"
                        class="mini-delete-btn"
                        @click="handleDeleteGood(scope.$index)"
                        >删除</el-button
                      >
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-form-item>
            <el-form-item label="关联分销商:" prop="relationDistributorIds" :key="1">
              <el-button
                :disabled="isDisabled"
                v-if="!isDisabled"
                class="mini-search-btn add-goods-btn"
                icon="el-icon-plus"
                @click="addDistr(1)"
                >添加分销商</el-button
              >
              <el-table
                :data="relDistriData"
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
                      :disabled="scope.row.id !== undefined && scope.row.id !== null && scope.row.id !== ''"
                      v-if="!isDisabled"
                      class="mini-delete-btn"
                      @click="handleDeleteDistributor(scope.$index, 1)"
                      >删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-form-item>
            <el-form-item label="指定分销商可视范围:" prop="appointDistributorId" :key="2">
              <el-button
                :disabled="isDisabled"
                v-if="!isDisabled"
                class="mini-search-btn add-goods-btn"
                icon="el-icon-plus"
                @click="addDistr(2)"
                >添加分销商</el-button
              >
              <el-table
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
                      :disabled="scope.row.id !== undefined && scope.row.id !== null && scope.row.id !== ''"
                      v-if="!isDisabled"
                      class="mini-delete-btn"
                      @click="handleDeleteDistributor(scope.$index, 2)"
                      >删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-form-item>
            <!-- 活动范围 -->
            <div class="scope-wrap">
              <span class="red">*</span>
            </div>
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
          <el-button
            class="mini-search-btn box-btn"
            @click="handleSave('ruleForm')"
            :loading="loading"
            v-else
            >编辑提交</el-button
          >
          <el-button size="mini" @click="handleBack">返回</el-button>
        </div>
      </div>
    </div>
     <!-- 添加单个商品组件 -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="goodsShow"
      width="80%"
      :before-close="closeGoodsDialog"
    >
      <select-goods
        :saleStatus="3"
        :goodsType="goodsType"
        :selectGoodsData="goodss"
        ref="selectGoods"
        @submit="getDoodsData"
        @cancel="goodsShow = false"
      ></select-goods>
    </el-dialog>

    <!-- 添加单个货品组件 -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="itemsShow"
      width="80%"
      :before-close="closeItemsDialog"
    >
      <select-item
        :goodsType="1"
        :selectItemsData="goodsItems"
        ref="selectGoodItems"
        @cancel="itemsShow = false"
        @submit="getItemsData"
      ></select-item>
    </el-dialog>

    <!-- 添加关联分销商 -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="relDistributorShow"
      :before-close="disCancel1"
      width="80%"
    >
      <select-distributor
        :distributorData="relDistriData"
        ref="selectDistributor1"
        @disClose="disClose(1)"
        @disSubmit="relDisSubmit"
      >
      </select-distributor>
    </el-dialog>

    <!-- 添加指定分销商 -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="distributorShow"
      :before-close="disCancel2"
      width="80%"
    >
      <select-distributor
        :distributorData="distriData"
        ref="selectDistributor2"
        @disClose="disClose(2)"
        @disSubmit="disSubmit"
      >
      </select-distributor>
    </el-dialog>
  </div>
</template>

<script type="text/javascript">
import selectGoods from "@/views/marketingPromotion/groupBuy/components/selectGoods"
import selectItem from "@/views/marketingPromotion/groupBuy/components/selectItem"
import selectDistributor from "@/views/storeLayout/rxComponents/selectDistributor"
import {
  addServiceFee,
  getServiceFeeDetail,
  editServiceFee
} from "@/views/marketingPromotion/serviceFee/serviceData";
import { timeFormat } from "@/utils/timeFormat.js";
export default {
  data () {
    // 关联分销商
    var validateDistriData = (rule, value, callback) => {
      if (this.relDistriData && this.relDistriData.length <= 0) {
        callback(new Error("请选择关联分销商"));
      } else {
        callback();
      }
    }
    // 指定分销商
    var validateDistriData2 = (rule, value, callback) => {
      if (this.distriData && this.distriData.length <= 0) {
        callback(new Error("请选择指定分销商"));
      } else {
        callback();
      }
    }
    return {
      id: this.$route.query.id,
      loading: false,
      loading2: false,
      disShow: false,
      itemsShow: false,
      goodsShow: false,
      goodsType: 0,
      goodss: {},
      goodssItems: [], // 商品列表
      goodsItems: [], // 货品列表
      serviceData: {
        name: "",
        enName: '',
        represent: "",
        validTime: [],
        startTime: '',
        endTime: '',
        saveType: 2,
        relationDistributorIds: [],
        appointDistributorId: 0,
        activityFeeRelationBeans: []
      },
      rules: {
        name: [
          { required: true, message: "请输入活动名称", trigger: "blur" },
          { max: 10, message: "最多展示10个字", trigger: "blur" },
        ],
        represent: [
          { required: true, message: "请输入活动描述", trigger: "blur" }
        ],
        needChargeNum: [
          { required: true, message: "请输入服务费次数", trigger: "blur" }
        ],
        validTime: [
          { required: true, message: "请选择有效时间", trigger: "change" }
        ],
        saveType: [
          { required: true, message: "请选择添加商品", trigger: "change" }
        ],
        relationDistributorIds: [
          { required: true,  validator: validateDistriData, trigger: "change" }
        ],
        appointDistributorId: [
          { required: true,  validator: validateDistriData2, trigger: "change" }
        ]
      },
      expireTimeOption: {
        // 限制可选日期
        disabledDate(date) {
          return date.getTime() < Date.now() - 24 * 60 * 60 * 1000;
        },
      },
      isDisabled: false, // 是否可编辑
      disabled: false,
      relDistriData: [], // 关联分销商列表
      distriData: [], // 指定分销商列表
      relDistributorShow: false, //关联
      distributorShow: false, // 指定
      checkMsg: 0,
      sid: null
    }
  },
  components: {
    selectGoods,
    selectItem,
    selectDistributor
  },
  mounted () {
    this.sid = this.$route.query.sid
    this._initDetail()
  },
  methods: {
    _initDetail () {
      // 禁止用户编辑
      if (this.sid) {
        this.isDisabled = true;
        this.loading2 = true
         this.getDataDetail()
      }
    },
    // 获取详情
    getDataDetail () {
      getServiceFeeDetail(this, {id:this.sid}).then(res => {
        if (res.code === 0) {
          this.loading2 = false
          this.serviceData = res.data
          this.serviceData.saveType = 2
          // 有效时间
          // this.serviceData.validTime = [
          //   new Date(res.data.startTime).getTime(),
          //   new Date(res.data.endTime).getTime(),
          // ]
          // 商品、货品选择
          this.goodsItems = this.serviceData.activityFeeRelations;
          // if (Number(res.data.saveType) === 1) {
          //   // 按商品选择回显
          //   // this.goodss.goodsId = res.data.activityFeeRelations[0].goodsId;
          // } else {
          //   // 货品
          //   this.goodsItems = this.serviceData.activityFeeRelationBeans;
          // }
          //关联分销商,缺少公司名
          this.relDistriData.push({
            id: this.serviceData.relationDistributorId,
            distributorName: this.serviceData.relationDistributorName,
            companyName: this.serviceData.relationCompanyName
          })
          //指定分销商
          this.distriData.push({
            id: this.serviceData.relationDistributorId,
            distributorName: this.serviceData.appointDistributorName,
            companyName: this.serviceData.appointCompanyName
          })
        }
      }).catch(err => {
        console.log(err);
      })
    },
    validatePro(data){
      let flag = false
      data.forEach(item => {
        if (!item.serviceBoxNum){
          this.$message({
            type: "error",
            message: "请填写服务费盒内数量",
          });
          flag = false
        } else if (!item.serviceFee) {
          this.$message({
            type: "error",
            message: "请填写服务费单价",
          });
          flag = false
        } else {
          flag = true
        }
      })
      return flag
    },
    // 提交保存
    handleSave (formName) {
       this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false;
        } else {
          if (this.serviceData.saveType === 1) {
            // 单个商品
            if (this.goodssItems.length > 0) {
              if (this.validatePro(this.goodssItems)) {
                this.serviceData.activityFeeRelationBeans = this.goodssItems;
              }else {
                return
              }
            } else {
              this.$message({
                type: "error",
                message: "请选择商品",
              });
              return false
            }
          } else {
            // 单个货品
            if (this.goodsItems.length > 0) {
              if (this.validatePro(this.goodsItems)) {
                this.serviceData.activityFeeRelationBeans = this.goodsItems;
              }else {
                return
              }
            } else {
              this.$message({
                type: "warn",
                message: "请选择货品",
              });
              return false
            }
          }

          let relArr = []
          this.relDistriData.forEach(rel => {
            relArr.push(rel.distributorId)
          })
          this.serviceData.relationDistributorIds = relArr
          // this.serviceData.startTime = this.serviceData.validTime[0]
          // this.serviceData.endTime = this.serviceData.validTime[1]
          this.serviceData.appointDistributorId = this.distriData[0].distributorId

          if (!this.sid) {
            // 新增
            addServiceFee(this, this.serviceData).then((res) => {
              if (Number(res.code) === 0) {
                this.$message({
                  type: "success",
                  message: "新增成功",
                });
                this.clickLeave();
              }
            });
          } else {
            // 编辑
            editServiceFee(this, this.serviceData).then((res) => {
              if (Number(res.code) === 0) {
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
    // 页面跳转
    clickLeave() {
      this.$router.push({ name: 'serviceFeeList' });
    },
    // 添加分销商
    addDistr (type) {
      if (type === 1) {
        this.relDistributorShow = true
      } else {
        this.distributorShow = true
      }
    },
    // 监听时间选择
    handleDate(value) {
      this.serviceData.validTime = value;
      this.$forceUpdate();
    },
    // 添加 单个商品/货品
    assignGoods() {
      if (this.serviceData.saveType === 1) {
        // 单个商品
        this.goodsShow = true;
      } else {
        // 单个货品
        this.itemsShow = true;
      }
    },
    getDoodsData(val) {
      this.goodss = val;
      this.goodssItems = [];
      this.goodssItems = this.goodssItems.concat(val.itemList);
      val.itemList.forEach((item) => {
        this.$set(item, "goodsName", this.goodss.goodsName);
        this.$set(item, "goodsNo", this.goodss.goodsNo);
      });
      this.goodsShow = false;
    },
    getItemsData(val) {
      this.goodsItems = [];
      // this.goodsItems = val
      this.goodsItems = this.goodsItems.concat(val);
      this.itemsShow = false;
    },
     // 单个商品
    closeGoodsDialog() {
      this.$refs.selectGoods.handleCancel();
    },
    // 单个货品
    closeItemsDialog() {
      this.$refs.selectGoodItems.handleCancel();
    },
     // 选择关联分销商
    relDisSubmit(msg) {
      if (msg.length > 1) {
        this.$message({
          type: 'warning',
          message: '只能选择一个分销商！'
        })
      } else {
        this.relDistriData = msg
        this.relDistributorShow = false
      }
    },
    // 选择指定分销商可视范围
    disSubmit (msg) {
      if (msg.length > 1) {
        this.$message({
          type: 'warning',
          message: '只能选择一个分销商！'
        })
      } else {
        this.distriData = msg
        this.distributorShow = false
      }
    },
    // 删除商品/货品列表
    handleDeleteGood (index) {
      if (this.serviceData.saveType === 1) {
        // 商品
        this.goodssItems.splice(index, 1);
        if (this.goodssItems.length === 0) {
          this.goodss = {};
        }
      } else {
        // 货品
        this.goodsItems.splice(index, 1)
      }
    },
    // 删除关联/指定分销商列表
    handleDeleteDistributor (index, type) {
      if (type === 1) {
        this.relDistriData.splice(index, 1)
      } else {
        this.distriData.splice(index, 1)
      }
    },
    disClose(type) {
      type===1 ? this.relDistributorShow = false : this.distributorShow = false
    //  this.relDistributorShow = false
    },
    disCancel1() {
      // type===1 ? this.$refs.selectDistributor1.handleCancel() : this.$refs.selectDistributor2.handleCancel()
      this.$refs.selectDistributor1.handleCancel();
    },
    disCancel2() {
      this.$refs.selectDistributor2.handleCancel();
    },
    handleBack() {
      this.$router.push({ name: 'serviceFeeList' })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .serviec-add{
    height: 100%;
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
      .btn-home{
        float: right;
        padding: 5px;
        margin-top: 8px;
        margin-right: 8px;
        margin-left: 0;
        font-size: 12px;
      }
    }
    .service-content {
      padding-top: 30px;
      min-width: 1200px;
      .el-radio-group{
        margin-right:20px;
      }
      .footbtn {
        padding-bottom: 30px;
        text-align: center;
        .box-btn + .box-btn {
          margin-left: 10px;
        }
      }
      .scope-wrap {
        position: relative;
        /deep/ .el-form-item__label {
          line-height: 28px;
        }
        .red {
          position: absolute;
          top: 8px;
          left: 20%;
          margin-left: -80px;
          font-size: 12px;
          color: #f56c6c;
        }
      }
      .blue {
        color: #21b8cb;
      }
      span.blue {
        margin-left: 10px;
      }
      p.blue {
        line-height: 28px;
      }
    }
  }
</style>