<!--
 * @Author: yaowei
 * @Date: 2018-05-14 11:19:49
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-14 17:00:28
-->
<template>
  <div>
    <el-form-item label="活动范围:">
      <el-radio-group v-model="formData.distributorType">
        <div class="dis-item1">
          <el-radio :label="1" :disabled="disabled">全部分销商</el-radio>
        </div>

        <div class="dis-item2">
          <div>
            <el-radio :label="2" :disabled="disabled">指定分销商等级</el-radio>
          </div>

          <!-- 指定分销商等级 -->
          <el-col v-if="formData.distributorType === 2">
            <el-form-item>
              <el-checkbox-group
                v-model="formData.distributorGradeIds"
                :disabled="disabled"
              >
                <el-checkbox
                  v-for="item in distributorGradesList"
                  :label="item.id"
                  :key="item.id"
                  >{{ item.name }}</el-checkbox
                >
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </div>

        <!-- 指定事业部 -->
        <!-- <div class="dis-item2">
          <div>
            <el-radio  :label="3" :disabled="disabled">指定事业部</el-radio>
          </div>
          <el-col v-if="formData.distributorType === 3">
            <el-form-item>
              <el-checkbox-group v-model="formData.distributorBusinessIds" :disabled="disabled">
                <el-checkbox v-for="item in businesses" :label="item.id" :key="item.id">{{item.name}}</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </div> -->

        <div class="dis-item2">
          <div>
            <el-radio :label="4" :disabled="disabled">指定销售部门</el-radio>
          </div>

          <!-- 指定销售部门 -->
          <el-col v-if="formData.distributorType === 4">
            <el-form-item>
              <el-checkbox-group
                v-model="formData.distributorDepartmentIds"
                :disabled="disabled"
              >
                <el-checkbox
                  v-for="item in departments"
                  :label="item.id"
                  :key="item.id"
                  >{{ item.departmentName }}</el-checkbox
                >
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </div>

        <div class="dis-item2">
          <div>
            <el-radio :label="5" :disabled="disabled">指定业务员</el-radio>
          </div>
          <!-- 指定业务员-->
          <el-col v-if="formData.distributorType === 5">
            <transition mode="out-in" name="fade">
              <div class="sales">
                <el-transfer
                  v-model="formData.distributorAdminIds"
                  :data="admins"
                  :titles="['业务员列表', '选中列表']"
                  :button-texts="['取消', '选中']"
                ></el-transfer>
              </div>
            </transition>
          </el-col>
        </div>

        <div class="dis-item3">
          <el-radio :label="3" :disabled="disabled">指定分销商</el-radio>
          <div
            class="distributor-content"
            v-if="formData.distributorType === 3"
          >
            <el-button
              class="mini-search-btn"
              icon="el-icon-plus"
              @click="distributorShow = true"
              v-if="!disabled"
            >
              添加分销商
            </el-button>
            <el-table
              class="goods-table"
              :data="formData.distributorData"
              border
              header-row-class-name="header-row"
              style="width: 100%"
              max-height="300"
            >
              <el-table-column
                align="center"
                label="分销商用户名"
                width="150"
                prop="name"
              ></el-table-column>
              <el-table-column
                align="center"
                label="公司名"
                show-overflow-tooltip
                width="300"
                prop="companyName"
              ></el-table-column>
              <el-table-column align="center" label="操作" width="80" v-if="!disabled">
                <template slot-scope="scope" v-if="!disabled">
                  <el-button
                    style="margin-top: 0px; margin-bottom: 0px"
                    class="mini-delete-btn"
                    @click="handleDeleteDistributor(scope.$index)"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
        <!-- 指定品牌等级 -->
        <!-- <div class="dis-item3">
          <div>
            <el-radio :label="7" :disabled="disabled">指定品牌等级</el-radio>
          </div>
          <el-col v-if="formData.distributorType === 7" class="brand-wrap">
            <el-button class="mini-search-btn" @click="addGoods"
              >添加品牌</el-button
            >
            <span class="place_holder">(名称/等级都需选择才能保存成功)</span>
            <table class="productData" v-if="arr.length > 0">
              <tr>
                <th>品牌名称</th>
                <th>价格等级</th>
                <th>操作</th>
              </tr>
              <tr v-for="(item, index) in arr" :key="index" :vlaue="item.id">
                <td prop="territory">
                  <el-form-item>
                    <el-select
                      v-model="item.brandId"
                      size="mini"
                      placeholder="请选择"
                      @change="changes"
                    >
                      <el-option
                        v-for="(brand, index) in brands"
                        :key="index"
                        :label="brand.name"
                        :value="brand.id"
                        :disabled="brand.disabled"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                </td>
                <td>
                  <el-form-item>
                    <el-select
                      v-model="item.gradeId"
                      size="mini"
                      placeholder="请选择"
                    >
                      <el-option
                        v-for="(grade, index) in grades"
                        :key="index"
                        :label="grade.name"
                        :value="grade.id"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                </td>
                <td>
                  <el-button class="mini-delete-btn" @click="delectBrand(item)"
                    >删除</el-button
                  >
                </td>
              </tr>
            </table>
          </el-col>
        </div> -->
      </el-radio-group>
    </el-form-item>
    <el-dialog
      :modal-append-to-body="false"
      :visible="distributorShow"
      :before-close="disCancel"
      width="80%"
    >
      <select-distributor
        :distributorData="formData.distributorData"
        ref="selectDistributor"
        @cancel="cancel"
        @submit="disSubmit"
      >
      </select-distributor>
    </el-dialog>
  </div>
</template>
<script>
import selectDistributor from "@/views/goods/components/selectDistributor";
export default {
  props: [
    "ruleType",
    "type",
    "gIds",
    "dIds",
    "disabled",
    "businessIds",
    "departmentIds",
    "adminIds",
    "brandGrade",
  ],
  data() {
    return {
      distributorGradesList: [],
      departments: [],
      businesses: [],
      distributors: [],
      admins: [],
      formData: {
        distributorType: 1,
        distributorGradeIds: [],
        distributorBusinessIds: [],
        distributorDepartmentIds: [],
        distributorAdminIds: [],
        distributorIds: [],
        distributorData: [],
        brandGradeData: [],
      },
      pageInfo: {
        page: 1,
        count: 10000,
        status: 1,
      },
      distributorShow: false,
      multipleSelection: [],
      isChange: false,
      grades: [], // 默认价格等级
      arr: [], // 添加品牌arr
      brands: [], // 产品线品牌下拉数据
    };
  },
  components: { selectDistributor },
  created() {
    this.getGrades();
    // this.getBusinesses();
    this.getDepartments();
    this.getAllSales();
    this.formData.distributorData = [];
    this.formData.distributorType = this.type;
    this.formData.distributorGradeIds =
      this.gIds instanceof Array
        ? this.gIds
        : this.gIds == undefined
        ? []
        : this.gIds.splitnum(",");
    this.formData.distributorBusinessIds =
      this.businessIds instanceof Array
        ? this.businessIds
        : this.businessIds == undefined
        ? []
        : this.businessIds.splitnum(",");
    this.formData.distributorDepartmentIds =
      this.departmentIds instanceof Array
        ? this.departmentIds
        : this.departmentIds == undefined
        ? []
        : this.departmentIds.splitnum(",");
    this.formData.distributorAdminIds =
      this.adminIds instanceof Array
        ? this.adminIds
        : this.adminIds == undefined
        ? []
        : this.adminIds.splitnum(",");
    this.formData.distributorIds =
      this.dIds instanceof Array
        ? this.dIds
        : this.dIds == undefined
        ? []
        : this.dIds.splitnum(",");
    this.distributors = this.$store.getters.distributors;
    this.initDistributor();
    this.arr = this.brandGrade ? this.brandGrade : [];
    this.formData.brandGradeData = this.arr;
    this.productBrand(); // 添加品牌 => 品牌名称 || 添加品类 => 品牌名称
  },
  methods: {
    getAllSales() {
      //..用户列表
      this.$http.salesList(this, {page:1, size:10000, saleFlag: 1}).then(res => {    
          res.data.list.forEach((item) => {
            let obj = {
              key: item.id,
              label: item.realName,
              disabled: false,
            };
            this.admins.push(obj);
          });
        });
    },
    // getBusinesses() {
    //   // 事业部
    //   this.$api.get(this, "admin/u/po/businessUnit/list").then((res) => {
    //     if (res.code === 0) {
    //       this.businesses = res.businessUnits;
    //     }
    //   });
    // },
    getGrades() {
      this.$http.getGradePoList(this, { page:1, size:10000, openFlag: 1 }).then(res => {    
          if (res.success) {
            this.distributorGradesList = res.data.list;
            this.grades = res.data.list;
          }
        });
    },
    getDepartments() {
      //销售部门
      this.$http.getDepartmentPoList(this, {page:1, size: 1000, saleType: 1}).then(res => {    
          if (res.success) {
            this.departments = res.data.list;
          }
        });
    },

    handleDeleteDistributor(index) {
      this.formData.distributorData.splice(index, 1);
      // this.$refs.selectDistributor.selectRow()
    },
    initDistributor() {
      if (
        this.formData.distributorIds !== undefined &&
        this.formData.distributorIds !== null &&
        this.formData.distributorIds.length > 0
      ) {
        if (this.distributors === undefined || this.distributors.length === 0) {
          this.$http.getDistributorPoList(this, { page:1, size:10000,freezeStatus: 1,profileStatus: 2}).then(res => {	    
            this.distributors = res.data.list;
            this.$store.commit("GET_DISTRIBUTORS", res.data.list);
            this.formData.distributorIds.forEach((item) => {
              this.distributors.forEach((val) => {
                if (val.id === Number(item)) {
                  this.formData.distributorData.push(val);
                }
              });
            });
            this.formData.distributorData = this.setArr(
              this.formData.distributorData
            );
          });
        } else {
          this.formData.distributorIds.forEach((item) => {
            this.distributors.forEach((val) => {
              if (val.id === Number(item)) {
                this.formData.distributorData.push(val);
              }
            });
          });
          this.formData.distributorData = this.setArr(
            this.formData.distributorData
          );
        }
      }
    },
    add() {
      this.$emit("add");
    },
    disCancel() {
      this.$refs.selectDistributor.handleCancel();
    },
    cancel() {
      this.distributorShow = false;
    },
    disSubmit(msg) {
      this.formData.distributorData = msg;
      this.formData.distributorIds = []
      if (msg.length > 0) {
        msg.forEach(item => {
          this.formData.distributorIds.push(item.id)
        })
      }
      this.distributorShow = false;
    },
    setArr(arr) {
      // 去重
      const obj = {};
      const temp = [];
      for (let i = 0; i < arr.length; i++) {
        const type = Object.prototype.toString.call(arr[i].id); // 不加类型 分不清 1 '1'
        if (!obj[arr[i].id + type]) {
          temp.push(arr[i]);
          obj[arr[i].id + type] = true; // 这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
        }
      }
      return temp;
    },

    addGoods() {
      // 添加品牌操作
      var addManObject = {};
      this.arr.push(addManObject);
      this.formData.brandGradeData = this.arr;
    },

    delectBrand(item) {
      // 删除品牌操作
      this.arr.splice(this.arr.indexOf(item), 1);
      this.formData.brandGradeData = this.arr;
    },

    productBrand() {
      // 添加品牌 => 品牌名称 || 添加品类 => 品牌名称
      this.$http.getBrandPoList(this, {
        page: 1,
        size: 10000,
        openFlag: "1",
      }).then(res => {
        if (res.success) {
          for (var i = 0; i < res.data.list.length; i++) {
            res.data.list[i].disabled = false;
          }
          this.brands = res.data.list;
          for (let i = 0; i < this.arr.length; i++) {
            // 添加品牌默认禁用
            let b = false;
            for (var j = 0; j < this.brands.length; j++) {
              if (this.arr[i].brandId === this.brands[j].id) {
                this.brands[j].disabled = true;
                b = true;
                break;
              }
            }
            if (!b) {
              this.arr[i].brandId = "已删除或停用";
            }
          }
          this.count--;
          if (this.count === 0) {
            this.loading = false;
          }
        }
      })
    },

    changes(val) {
      // 添加品牌价格等级
      this.brands.forEach((item) => {
        // 重置空可以选择
        item.disabled = false;
      });
      for (let i = 0; i < this.arr.length; i++) {
        // 重复不能选择
        for (let j = 0; j < this.brands.length; j++) {
          if (this.arr[i].brandId === this.brands[j].id) {
            this.brands[j].disabled = true;
            break;
          }
        }
      }
    },
  },
  watch: {
    formData: {
      handler() {
        this.$emit("change", this.formData);
      },
      deep: true,
    },
    type(val) {
      this.formData.distributorType = val;
    },
    dIds(val) {
      this.formData.distributorIds =
        val instanceof Array
          ? val
          : val == undefined || val === null
          ? []
          : val.splitnum(",");
      this.initDistributor();
    },
    gIds(val) {
      this.formData.distributorGradeIds =
        val instanceof Array
          ? val
          : val === undefined || val === null
          ? []
          : val.splitnum(",");
    },
    businessIds(val) {
      this.formData.distributorBusinessIds =
        val instanceof Array
          ? val
          : val == undefined || val === null
          ? []
          : val.splitnum(",");
    },
    departmentIds(val) {
      this.formData.distributorDepartmentIds =
        val instanceof Array
          ? val
          : val === undefined || val === null
          ? []
          : val.splitnum(",");
    },
    adminIds(val) {
      this.formData.distributorAdminIds =
        val instanceof Array
          ? val
          : val === undefined || val === null
          ? []
          : val.splitnum(",");
    },
  },
};
</script>
<style lang="scss" scoped>
.el-radio-group .el-checkbox {
  margin-left: 30px;
  margin-right: 0px;
}

.sales {
  margin: 30px;
  .el-button--primary {
    color: #fff;
    background-color: $lakeBlue !important;
    border-color: $lakeBlue !important;
  }
  .el-transfer__button.is-disabled,
  .el-transfer__button.is-disabled:hover {
    background-color: #CCCCCC !important;
    border-color: #CCCCCC !important;
  }
}

.dis-item2 {
  margin-top: 16px;
}
.dis-item1 {
  margin-top: 8px;
}
.dis-item3 {
  margin-top: 16px;
}

.distributor-content {
  margin-left: 26px;
  margin-top: 10px;
}
.mini-search-btn {
  margin-bottom: 10px;
}
.brand-wrap {
  padding-top: 10px;
  font-size: 14px;
  table {
    th,
    td {
      padding: 8px 16px;
    }
    th {
      font-size: 14px;
    }
  }
}
.el-form-item{
  font-size:12px;
  /deep/.el-input--suffix{
    width:120px;
  }
}

</style>
