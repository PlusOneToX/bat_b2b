<!--
 * @Author: litian
 * @Date: 2018-06-04 09:23:00
 * @LastEditors: litian
 * @LastEditTime: 2018-06-04 11:52:01
-->

<template>
  <div class="add-wrap">
    <header>
      <h4 class="header_h4" v-if="Number(checkMsg) === 1">添加秒杀</h4>
      <h4 class="header_h4" v-if="Number(checkMsg) === 2">查看秒杀</h4>
      <h4 class="header_h4" v-if="Number(checkMsg) === 3">编辑秒杀</h4>
      <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="handleBack">返回秒杀列表</el-button>
    </header>

    <div class="content">
      <el-form :model="groupData" :rules="rules" label-width="20%" label-position="right" ref="ruleForm">
        <el-row>
          <el-col :span="18">
            <div class="tip-left">
              <p>
                <code>基本信息</code>
              </p>
            </div>
            <el-form-item label="秒杀名称:" prop="name">
              <el-input v-model="groupData.name" placeholder="最多展示20个字" :disabled="isDisabled" maxlength="20" style="width:500px;" />
              <el-button class="mini-search-btn" icon="el-icon-plus" @click="copyPromotion()" v-if="isDisabled"> 复制添加 </el-button>
            </el-form-item>
            <el-form-item label="描述:" prop="groupSeckillDesc">
              <el-input type="textarea" :rows="2" placeholder="仅内部可见，不展示到前台" v-model="groupData.groupSeckillDesc" :disabled="isDisabled" />
            </el-form-item>
            <el-form-item label="排序:" prop="sort">
              <el-input v-model="groupData.sort" :disabled="isDisabled" onkeyup="value=value.replace(/[^\d]/g,'')" style="width:100px;" />
            </el-form-item>
            <el-form-item label="有效时间:" prop="validTime">
              <el-date-picker v-model="groupData.validTime" type="datetimerange" range-separator="至"
                start-placeholder="开始日期" end-placeholder="结束日期" value-format="timestamp"
                :picker-options="expireTimeOption" :disabled="isDisabled" @input="handleDate"></el-date-picker>
            </el-form-item>

            <div class="tip-left">
              <p>
                <code>秒杀规则</code>
              </p>
            </div>
            <el-form-item label="秒杀货品:">
              <el-button class="mini-search-btn add-goods-btn" @click="assignGoods()" v-show="!isDisabled">添加货品
              </el-button>
              <!-- 货品展示 -->
              <div class="pro-wrap" v-if="goodsItems.length > 0">
                <el-table :data="goodsItems"  border header-row-class-name="header-row" class="tableCenter goods-table"
                  style="width: 100%" max-height="300">
                  <el-table-column width="90" align="center" label="秒杀价">
                    <template slot-scope="scope" prop="groupSeckillPrice">
                      <el-input size="mini" v-model="scope.row.groupSeckillPrice" min="0" :disabled="isDisabled"></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column width="110" align="center" label="虚拟秒杀数" prop="virtualSum">
                    <template slot-scope="scope">
                      <el-input size="mini" v-model="scope.row.virtualSum" :disabled="isDisabled"></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column width="150" label="最小起拼量" prop="minNum">
                    <template slot-scope="scope">
                      <el-input size="mini" placeholder="默认0为不限制" :disabled="isDisabled" v-model="scope.row.minNum" onkeyup="value=value.replace(/[^\d]/g,'')" min="0" />
                    </template>
                  </el-table-column>
                  <el-table-column width="150" label="最大秒杀数" prop="maxNum">
                    <template slot-scope="scope">
                      <el-input size="mini" placeholder="默认0为不限制" :disabled="isDisabled" v-model="scope.row.maxNum" onkeyup="value=value.replace(/[^\d]/g,'')" min="0" />
                    </template>
                  </el-table-column>
                  <el-table-column width="100" label="排序" prop="sort">
                    <template slot-scope="scope">
                      <el-input size="mini" v-model="scope.row.sort" :disabled="isDisabled" onkeyup="value=value.replace(/[^\d]/g,'')" />
                    </template>
                  </el-table-column>
                  <el-table-column  width="150" label="优先使用现有库存" prop="existFlag">
                    <template slot-scope="scope">
                      <el-select size="mini" class="select-item" style="width:100px;margin-right:0;" :disabled="isDisabled"  v-model="scope.row.existFlag" placeholder="请选择">
                        <el-option label="开启" :value="1"> </el-option>
                        <el-option label="关闭" :value="0"> </el-option>
                      </el-select>
                     </template>
                  </el-table-column>
                  <el-table-column  width="160" label="支持预售" prop="mtoFlag">
                    <template slot-scope="scope">
                      <el-select size="mini" class="select-item" style="width:100px;margin-right:0;" :disabled="isDisabled"  v-model="scope.row.mtoFlag" placeholder="请选择">
                        <el-option label="支持" :value="1"> </el-option>
                        <el-option label="不支持" :value="0"> </el-option>
                      </el-select>
                     </template>
                  </el-table-column>
                  <el-table-column width="120" align="center" show-overflow-tooltip label="商品名称" prop="goodsName">
                  </el-table-column>
                  <el-table-column width="115" align="center" label="货品编码" prop="itemCode"></el-table-column>
                  <el-table-column width="125" align="center" label="条形码" prop="barCode"></el-table-column>
                   <el-table-column width="80" align="center" show-overflow-tooltip label="规格" prop="specsName">
                     <template slot-scope="scope">
                      <div v-if="scope.row.specsName">{{scope.row.specsName}}</div>
                      <div v-else>--</div>
                    </template>
                  </el-table-column>
                  <el-table-column width="70" align="center" label="颜色" prop="colorName">
                    <template slot-scope="scope">
                      <div v-if="scope.row.colorName">{{scope.row.colorName}}</div>
                      <div v-else>--</div>
                    </template>
                  </el-table-column>
                  <!-- <el-table-column width="110" align="center" label="建议零售价" prop="retailPrice"></el-table-column>
                  <el-table-column width="110" align="center" label="建议批发价" prop="wholesalePrice"></el-table-column> -->
                  <el-table-column width="90" align="center" label="在库数量" prop="numInWarehouseSum"></el-table-column>
                  <el-table-column width="90" align="center" label="在途数量" prop="numOnWay"></el-table-column>
                  <el-table-column align="center" label="操作" width="80" v-if="!isDisabled">
                    <template slot-scope="scope">
                      <el-button class="mini-delete-btn" @click="handleDeleteGood(scope.$index)">删除</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-form-item>
            <!-- 活动范围 -->
            <div class="scope-wrap">
              <span class="red">*</span>
              <distributor ref="distributor" v-if="disShow" :type="groupData.distributorScope" :gIds="formData.gradeIds"
                :businessIds="formData.businessIds" :departmentIds="formData.departmentIds"
                :adminIds="formData.adminIds" :dIds="formData.distributorIds" :brandGrade="formData.brandGradeData"
                @change="getChange" :disabled="isDisabled"></distributor>
            </div>

            <div class="tip-left">
              <p>
                <code>其他设置</code>
              </p>
            </div>
            <el-form-item label="秒杀预告:">
              <el-radio-group v-model="groupData.advanceFlag" :disabled="isDisabled">
                <el-radio :label="1">开启</el-radio>
                <el-radio :label="0">关闭</el-radio>
              </el-radio-group>
              <span class="blue">(开启秒杀预告，未开始的秒杀活动商品将会在前端展示)</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div class="clearfix footbtn">
        <div>
          <el-button class="mini-search-btn box-btn" @click="handleSave('ruleForm')" :loading="loading"
            v-if="!isDisabled">保存提交</el-button>
          <el-button size="mini" @click="handleBack">返回</el-button>
        </div>
      </div>
    </div>

    <!-- 添加单个商品组件 -->
    <el-dialog :modal-append-to-body="false" :visible="goodsShow" width="80%" :before-close="closeGoodsDialog">
      <select-goods :saleStatus="3" :goodsType="goodsType" :selectGoodsData="goodss" ref="selectGoods"
        @submit="getDoodsData" @cancel="goodsShow = false"></select-goods>
    </el-dialog>

    <!-- 添加单个货品组件 -->
    <el-dialog :modal-append-to-body="false" :visible="itemsShow" width="80%" :before-close="closeItemsDialog">
      <select-item :saleStatus="3" :goodsType="goodsType" :selectItemsData="goodsItems" ref="selectGoodItems"
        @cancel="itemsShow = false" @submit="getItemsData"></select-item>
    </el-dialog>
  </div>
</template>

<script>
  import selectGoods from "@/views/marketingPromotion/groupBuy/components/selectGoods";
  import selectItem from "@/views/marketingPromotion/groupBuy/components/selectItem";
  import distributor from "@/views/marketingPromotion/groupBuy/components/discountPromotion";
  import {
    timeFormat
  } from "@/utils/timeFormat.js";
  export default {
    name: "addGroupBuy",
    components: {
      selectGoods,
      selectItem,
      distributor,
    },
    data() {
      return {
        loading: false,
        disShow: false,
        id: this.$route.query.id,
        checkMsg: this.$route.query.checkMsg,
        groupData: {
          id: "",
          name: "",
          groupSeckillDesc: "",
          cnExplain: "",
          validTime: [],
          distributorScope: 1,
          distributorScopeNo: 0,  // 分销商不可视范围 0.不指定
          groupGoodsBeanList: [],
          advanceFlag: 1,
          brandGradeBeanList: [],
          distributors: [], // 可视分销商id列表
          scalePriceIds: [], // 分销商价格等级id列表
          adminIds: [], // 业务员id列表
          groupSeckillType: 2, // 秒杀
          goods: []
        },
        formData: {
          distributorIds: [],
          businessIds: [],
          departmentIds: [],
          adminIds: [],
          gradeIds: [],
          brandGradeData: [],
        },
        distributorData: [],
        brandGradeData: [],
        rules: {
          name: [{
              required: true,
              message: "请输入秒杀名称",
              trigger: "blur"
            },
            {
              max: 20,
              message: "最多展示20个字",
              trigger: "blur"
            },
          ],
          sort: [{
            required: true,
            message: "请输入排序",
            trigger: "blur"
          }, ],
          validTime: [{
            required: true,
            message: "请选择有效时间",
            trigger: "change"
          }, ]
        },
        isDisabled: false, // 是否可编辑
        expireTimeOption: {
          // 限制可选日期
          disabledDate(date) {
            return date.getTime() < Date.now() - 24 * 60 * 60 * 1000;
          },
        },
        action: process.env.BASE_API + "system/v1/web/admin/oss/sts",
        goodsShow: false,
        itemsShow: false,
        goodsType: 1,
        goodss: {},
        goodssItem: [],
        goodsItems: [],
      };
    },
    methods: {
        // 复制
        copyPromotion(){
          this.groupData.id = undefined
          if (this.groupData.goods.length > 0) {
              this.groupData.goods.forEach(item => {
              item.id = undefined
            })
          }
          this.checkMsg = 1
          this.isDisabled = false
        },
      // 初始化详情数据
      initDetail(id) {
        // 禁止用户编辑
        if (Number(this.checkMsg) === 2) {
          this.isDisabled = true;
        }

        this.getGroupBuyDetail(id);
      },
      // 查看秒杀详情
      getGroupBuyDetail(id) {
        this.$http.groupseckillDetail(this, {id: id}).then(res => {  
          if (res.success) {
            this.groupData = res.data;
            // 有效时间
            this.groupData.validTime = [
              new Date(res.data.startTime).getTime(),
              new Date(res.data.endTime).getTime(),
            ];
            if (res.data.goods && res.data.goods.length > 0) {
              this.goodsItems = res.data.goods;

              this.getItemsData(res.data.goods, "detail");
            }
            // 活动范围
            if (res.data.distributorScope === 1) {
              this.formData.gradeIds = [];
              this.formData.businessIds = [];
              this.formData.departmentIds = [];
              this.formData.adminIds = [];
              this.formData.distributorIds = [];
              this.formData.brandGradeData = [];
            } else if (res.data.distributorScope === 2) {
              this.formData.businessIds = [];
              this.formData.departmentIds = [];
              this.formData.adminIds = [];
              this.formData.distributorIds = [];
              this.formData.brandGradeData = [];
              this.formData.gradeIds = res.data.scalePriceIds;
            } else if (this.groupData.distributorScope === 4) {
              this.formData.gradeIds = [];
              this.formData.businessIds = [];
              this.formData.adminIds = [];
              this.formData.distributorIds = [];
              this.formData.brandGradeData = [];

              this.formData.departmentIds = res.data.departmentIds;
            } else if (this.groupData.distributorScope === 5) {
              this.formData.gradeIds = [];
              this.formData.businessIds = [];
              this.formData.departmentIds = [];
              this.formData.distributorIds = [];
              this.formData.brandGradeData = [];

              this.formData.adminIds = res.data.adminIds;
            } else if (this.groupData.distributorScope === 3) {
              this.formData.gradeIds = [];
              this.formData.businessIds = [];
              this.formData.departmentIds = [];
              this.formData.adminIds = [];
              this.formData.brandGradeData = [];
              res.data.distributors.forEach(item => {
                this.formData.distributorIds.push(item.distributorId)
              })
            } else if (this.groupData.distributorScope === 7) {
              this.formData.gradeIds = [];
              this.formData.businessIds = [];
              this.formData.departmentIds = [];
              this.formData.adminIds = [];
              this.formData.distributorIds = [];

              this.formData.brandGradeData = res.data.brandGradeBeanList;
            }
            this.disShow = true;
          }
        });
      },
      // 返回秒杀列表
      handleBack() {
        this.resetValidate("ruleForm");
        this.clickLeave();
      },
      // 页面跳转
      clickLeave() {
        this.$router.go(-1);
      },
      // 保存提交
      handleSave(formName) {
        this.$refs[formName].validate((valid) => {
          if (!valid) {
            return false;
          } else {
            if (
              this.groupData.distributorScope === 0 ||
              this.groupData.distributorScope === 1
            ) {
              this.formData.gradeIds = [];
              this.formData.businessIds = [];
              this.formData.departmentIds = [];
              this.formData.adminIds = [];
              this.formData.distributorIds = [];
              this.formData.brandGradeData = [];

            } else if (this.groupData.distributorScope === 2) {
              if (
                this.formData.gradeIds === undefined ||
                this.formData.gradeIds === "" ||
                this.formData.gradeIds.length === 0
              ) {
                this.$message.error("活动范围，请至少指定一个分销商等级！");
                this.loading = false;
                return;
              } else {
                this.formData.businessIds = [];
                this.formData.departmentIds = [];
                this.formData.adminIds = [];
                this.formData.distributorIds = [];
                this.formData.brandGradeData = [];

                this.groupData.scalePriceIds = this.formData.gradeIds;
              }
            } else if (this.groupData.distributorScope === 4) {
              if (
                this.formData.departmentIds === undefined ||
                this.formData.departmentIds === "" ||
                this.formData.departmentIds.length === 0
              ) {
                this.$message.error("活动范围，请至少指定一个销售部门！");
                this.loading = false;
                return;
              } else {
                this.formData.gradeIds = [];
                this.formData.businessIds = [];
                this.formData.adminIds = [];
                this.formData.distributorIds = [];
                this.formData.brandGradeData = [];

                this.groupData.departmentIds = this.formData.departmentIds;
              }
            } else if (this.groupData.distributorScope === 5) {
              if (
                this.formData.adminIds === undefined ||
                this.formData.adminIds === "" ||
                this.formData.adminIds.length === 0
              ) {
                this.$message.error("活动范围，请至少指定一个业务员！");
                this.loading = false;
                return;
              } else {
                this.formData.gradeIds = [];
                this.formData.businessIds = [];
                this.formData.departmentIds = [];
                this.formData.distributorIds = [];
                this.formData.brandGradeData = [];

                this.groupData.adminIds = this.formData.adminIds;
              }
            } else if (this.groupData.distributorScope === 3) {
              if (this.distributorData.length === 0) {
                this.$message.error("活动范围，请至少指定一个分销商！");
                this.loading = false;
                return;
              } else {
                this.groupData.distributors = [];
                for (let i = 0; i < this.distributorData.length; i++) {
                  this.groupData.distributors.push({
                    distributorId: this.distributorData[i].id,
                    name: this.distributorData[i].name,
                    companyName: this.distributorData[i].companyName
                  });
                }
                this.formData.gradeIds = [];
                this.formData.businessIds = [];
                this.formData.departmentIds = [];
                this.formData.adminIds = [];
                this.formData.brandGradeIds = [];
              }
            } else if (this.groupData.distributorScope === 7) {
              if (this.brandGradeData.length === 0) {
                this.$message.error("活动范围，请至少指定一个品牌等级！");
                this.loading = false;
                return;
              } else {
                this.formData.gradeIds = [];
                this.formData.businessIds = [];
                this.formData.departmentIds = [];
                this.formData.adminIds = [];
                this.formData.distributorIds = [];

                this.groupData.brandGradeBeanList = this.brandGradeData;
              }
            }

            // 货品
            if (this.goodsItems.length > 0) {
              this.goodsItems.forEach(item => {
                if (item.groupSeckillPrice === '' || item.groupSeckillPrice === undefined || item.groupSeckillPrice === null) {
                  this.$message.error("请填写秒杀价");
                  this.loading = false;
                  return;
                } else if (item.sort === '' || item.sort === undefined || item.sort === null) {
                  this.$message.error("请填写排序");
                  this.loading = false;
                  return;
                } else if (item.minNum === '' || item.minNum === undefined || item.minNum === null) {
                  item.minNum = 0
                } else if (item.maxNum === '' || item.maxNum === undefined || item.maxNum === null) {
                  item.maxNum = 0
                }
              })
              this.groupData.goods = this.goodsItems
            } else {
              this.$message.error("请选择秒杀货品");
              this.loading = false;
              return;
            }

            this.groupData.startTime = timeFormat(this.groupData.validTime[0]);
            this.groupData.endTime = timeFormat(this.groupData.validTime[1]);
            if (Number(this.checkMsg) === 1) {
              // 新增
              this.$http.addGroupseckill(this, this.groupData).then((res) => {
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
              this.groupData.id = this.id;
              this.$http.editGroupseckill(this, this.groupData).then((res) => {  
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
      // 重置表单验证
      resetValidate(formName) {
        this.$refs[formName].resetFields();
      },
      // 选择活动范围
      getChange(val) {
        this.groupData.distributorScope = val.distributorType;
        this.formData.gradeIds = val.distributorGradeIds;
        this.formData.businessIds = val.distributorBusinessIds;
        this.formData.departmentIds = val.distributorDepartmentIds;
        this.formData.adminIds = val.distributorAdminIds;
        this.formData.distributorIds = val.distributorIds;
        this.distributorData = val.distributorData;
        this.brandGradeData = val.brandGradeData;
      },
      // 监听时间选择
      handleDate(value) {
        this.groupData.validTime = value;
        this.$forceUpdate();
      },

      // 添加 单个货品
      assignGoods() {
        // 单个货品
        this.itemsShow = true;
      },

      // 单个商品
      closeGoodsDialog() {
        this.$refs.selectGoods.handleCancel();
      },
       // 获取选中商品
      getDoodsData(val) {
        this.goodss = val;
        if (val.itemList && val.itemList.length > 0) {
          this.goodssItem = [];
          this.goodssItem = this.goodssItem.concat(val.itemList);
          val.itemList.forEach((item) => {
            this.$set(item, "goodsName", this.goodss.goodsName);
          });
        }
        this.goodsShow = false;
      },
      // 根据货品查询库存
      stockList(ids) {
        return new Promise((resolve, reject) => {
          this.$http.stockListByitemId(this, {itemIdList: ids}).then(res => {
            if (res.success) {
              resolve(res)
            } else {
              reject()
            }
          })
        }).catch(() => {
          reject()
        })
      },
      // 单个货品
      closeItemsDialog() {
        this.$refs.selectGoodItems.handleCancel();
      },
      // 获取选中货品
      getItemsData(val, type) {
        let idArr = []
        val.forEach(item => {
          idArr.push(item.itemId)
        })
        this.stockList(idArr).then(res => {
          if (res.success) {
            val.forEach(item2 => {
              if (!type) {
                // 添加货品时，初始化
                item2.existFlag = 1  // 使用现有库存
                item2.mtoFlag = 0 // 不支持预售
              }
              res.data.forEach(item => {
                if (item2.itemId === item.itemId) {
                  item2.itemId = item.itemId
                  item2.numInWarehouseSum = item.numInWarehouseSum // 在库数量
                  item2.numOnWay = item.numOnWay // 在途库存
                }
              })
            })
          }
          val.forEach(item3 => {
            delete item3.id
          })
          this.goodsItems = [];
          this.goodsItems = this.goodsItems.concat(val);
          this.itemsShow = false;
        })
      },

      // 删除 单个商品/货品
      handleDeleteGood(index) {
        this.goodsItems.splice(index, 1);
      },
    },
    created() {
      this.checkMsg = this.$route.query.checkMsg;
      this.id = this.$route.query.id;
      if (Number(this.checkMsg) === 2 || Number(this.checkMsg) === 3) {
        this.initDetail(Number(this.id));
      } else {
        this.disShow = true;
      }
    },
  };

</script>

<style rel="stylesheet/scss" lang="scss" scoped>
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
      min-width: 1200px;

      .add-goods-btn {
        margin-left: 20px;
      }

      .footbtn {
        padding-bottom: 30px;
        text-align: center;

        .box-btn+.box-btn {
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
  .el-table__body-wrapper{
    .select-item{
      /deep/.el-input{
          width:120px;
        .el-input__inner{
          width:120px;
          margin-right: 0;
        }
      }
    }
  }

</style>
