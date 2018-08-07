<template>
  <div class="discount">
    <header>
      <h4>订单明细导出</h4>
    </header>

    <div
      class="dis_body"
      v-loading="loading"
      element-loading-text="正在导出..."
      element-loading-spinner="el-icon-loading"
      element-loading-background="rgba(0, 0, 0, 0.8)"
    >
      <el-row class="dis_title">
        请选择条件，系统将根据所选条件导出订单明细
      </el-row>

      <el-form
        ref="formData"
        :model="formData"
        label-width="100px"
        size="mini"
        class="el-form1"
      >
        <el-form-item label="导出范围">
          <el-select v-model="formData.type" style="width: 230px">
            <el-option
              v-for="item in orderTypeList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="分销商类型" >
          <el-radio-group v-model="distributorType" @change="handleDisType">
            <el-radio :label="1">一级分销商</el-radio>
            <el-radio :label="2">多级分销商</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="分销商">
          <el-autocomplete
            v-model="searchDistributor"
            :fetch-suggestions="querySearch"
            placeholder="分销商用户名"
            :trigger-on-focus="false"
            @select="handleSelect"
            clearable
            size="mini"
            style="width: 230px"
          ></el-autocomplete>

          <el-table
            v-if="distributorData && distributorData.length > 0"
            :data="distributorData"
            header-row-class-name="header-row"
            border
            class="tableCenter"
            style="margin-top: 10px"
          >
            <el-table-column
              align="center"
              label="分销商用户名"
              prop="name"
            ></el-table-column>
            <el-table-column
              label="B2B编码"
              align="center"
              prop="id"
            ></el-table-column>
            <el-table-column
              label="公司名"
              align="center"
              prop="companyName"
            ></el-table-column>
          </el-table>
        </el-form-item>

        <el-form-item label="订单来源">
          <el-select
            class="custom_select"
            filterable
            placeholder="订单来源"
            size="mini"
            v-model="formData.orderSource"
            multiple
            clearable
            style="width: 230px"
          >
            <el-option
              :key="item.platformNo"
              :label="item.name"
              :value="item.platformNo"
              v-for="item in orderSources"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="订单类型">
          <el-select
            class="custom_select"
            placeholder="订单类型"
            size="mini"
            v-model="formData.orderType"
            multiple
            clearable
            style="width: 230px"
          >
            <el-option
              :key="item.id"
              :label="item.name"
              :value="item.id"
              v-for="item in orderTypes"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="订单状态">
          <el-checkbox-group v-model="formData.status">
            <el-checkbox
              v-for="item in statusList"
              :label="item.id"
              :key="item.id"
              :disabled="item.id>0 && isOrderShow"
              >{{ item.name }}</el-checkbox
            >
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="发货状态">
          <el-checkbox-group v-model="formData.deliverStatus">
            <el-checkbox
              v-for="item in deliverList"
              :label="item.id"
              :key="item.id"
              :disabled="item.id>0 && isDeliverShow"
              >{{ item.name }}</el-checkbox
            >
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="付款状态">
          <el-checkbox-group v-model="formData.payStatus">
            <el-checkbox
              v-for="item in payList"
              :label="item.id"
              :key="item.id"
              :disabled="item.id>0 && isPayShow"
              >{{ item.name }}</el-checkbox
            >
          </el-checkbox-group>
        </el-form-item>

        <el-form-item label="订单时间">
          <el-date-picker
            v-model="formData.time"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="pickerOptions"
            value-format="timestamp"
          >
          </el-date-picker>
          <h4 class="time_hint">
            为防止数据量过大，当前限制一次最多只能导出两个月数据。
          </h4>
        </el-form-item>

        <!-- <el-form-item label="事业部">
          <el-select
            v-model="formData.businessUnitId"
            clearable
            style="width: 230px"
          >
            <el-option
              v-for="item in businessUnitList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item> -->

        <el-form-item label="货品编码">
          <el-input
            v-model="formData.itemCode"
            placeholder="不超过15个字"
            maxlength="15"
            style="width: 350px"
          />
          <p class="el_form_span">留空默认不限制</p>
        </el-form-item>

        <el-form-item label="导出字段">
          <el-checkbox
            :indeterminate="isIndeterminate"
            v-model="checkAll"
            @change="handleCheckAllChange"
            >全选/取消全选</el-checkbox
          >
          <div v-for="(item, index) in exportField" :key="item.name">
            <el-checkbox
              :indeterminate="item.isItemIndeterminate"
              :label="item.name"
              v-model="item.checkAllItem"
              @change="handleCheckAllItemChange(index)"
              >{{ item.name }}</el-checkbox
            >
            <div style="margin-left: 25px">
              <el-checkbox
                v-for="child in item.children"
                :label="child.name"
                :key="child.name"
                v-model="child.isChecked"
                @change="handleCheckedItemChange(index, child)"
                >{{ child.name }}</el-checkbox
              >
            </div>
          </div>
        </el-form-item>

        <el-form-item label="">
          <el-button class="mini-search-btn" @click="exportDownload()"
            >导出订单明细</el-button
          >
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import url from '@/api/allUrl'
import { getToken } from '@/utils/auth'
import {monthDay} from '@/utils/timeFormat'
export default {
  name: "orderDiscount",
  // activated() {
  //   this.getDepList(); //..事业部
  // },
  data() {
    var vue = this;
    return {
      loading: false,
      pickerOptions: {
        //..设置禁用状态，参数为当前日期，要求返回 Boolean
        disabledDate(time) {
          let timeOptionRange = vue.timeOptionRange;
          let secondNum = 60 * 60 * 24 * 60 * 1000;
          if (timeOptionRange) {
            return (
              time.getTime() > timeOptionRange.getTime() + secondNum ||
              time.getTime() < timeOptionRange.getTime() - secondNum
            );
          }
        },
        onPick(time) {
          //..选中日期后会执行的回调
          if (time.minDate && !time.maxDate) {
            //当第一时间选中才设置禁用
            vue.timeOptionRange = time.minDate;
          }
          if (time.maxDate) {
            vue.timeOptionRange = null;
          }
        },
      },
      distributorType: 1,
      formData: {
        type: 0, //..订单类型
        distributorId: "", //..分销商id
        orderSource: "", //..订单来源
        orderType: "", //..订单类型
        // time: [new Date().getTime(), new Date().getTime()], //..订单时间
        // businessUnitId: "", //..事业部
        itemCode: "", //..货品编码
        status: [], //..订单状态
        deliverStatus: [], // 发货状态
        payStatus: [] // 付款状态
      },
      statusList: [
        { id: 0, name: "全部" },
        { id: 1, name: "待确认" },
        { id: 2, name: "已确认" },
        { id: 3, name: "已拒绝" },
        { id: 4, name: "已取消" },
        { id: 5, name: "已完成" },
        
      ],
      deliverList: [
        { id: 0, name: "全部" },
        { id: 1, name: "未发货" },
        { id: 2, name: "出库中" },
        { id: 3, name: "部分发货" },
        { id: 4, name: "已发货" }
      ],
      payList: [
        { id: 0, name: "全部" },
        { id: 1, name: "未付款" },
        { id: 2, name: "部分付款" },
        { id: 3, name: "已付款" },
        { id: 4, name: "部分退款" },
        { id: 5, name: "退款申请中" },
        { id: 6, name: "已退款" }
      ],
      isOrderShow: false,
      isDeliverShow: false,
      isPayShow: false,
      timeData: [],
      orderTypeList: [
        //..订单类型
        { value: 0, label: "全部订单" },
        { value: 1, label: "全部参与活动订单" },
        { value: 2, label: "全部未参与活动订单" },
      ],
      businessUnitList: [], //..事业部
      businessUnitList1: [
        {
          name: "全部事业部",
          id: 0,
        },
      ],
      pageInfo: {
        page: 1,
        size: 1000,
      },
      orderSources: [], //订单来源
      orderTypes: [], //订单类型
      checkAll: false,
      exportField: [
        {
          name: "订单信息",
          isItemIndeterminate: false,
          checkAllItem: false,
          children: [
            {
              name: "下单日期",
              isChecked: true,
              value: "CREATE_TIME",
            },
            {
              name: "B2B订单号",
              isChecked: true,
              value: "ORDER_NO",
            },
            {
              name: "订单类型",
              isChecked: false,
              value: "ORDER_TYPE_ID",
            },
            {
              name: "订单状态",
              isChecked: true,
              value: "ORDER_STATUS",
            },
            {
              name: "订单币种",
              isChecked: true,
              value: "CURRENCY_TYPE",
            },
            {
              name: "订单汇率",
              isChecked: true,
              value: "CURRENCY_RATES",
            },
            {
              name: "付款状态",
              isChecked: false,
              value: "PAY_STATUS",
            },
            {
              name: "支付方式",
              isChecked: false,
              value: "PAY_WAY",
            },
            {
              name: "订单备注",
              isChecked: true,
              value: "REMARK",
            },
          ],
        },
        {
          name: "关联信息",
          isItemIndeterminate: false,
          checkAllItem: false,
          children: [
            {
              name: "ERP订单号",
              isChecked: true,
              value: "ORDER_ERP_NO",
            },
            {
              name: "ERP出库单号",
              isChecked: true,
              value: "ERP_OUTBOUND_NO",
            },
            {
              name: "B2B发货单号",
              isChecked: true,
              value: "ORDER_DELIVER_BILL_ID",
            },
            {
              name: "分销商ID（B2B编码）",
              isChecked: true,
              value: "DISTRIBUTOR_ID",
            },
            {
              name: "分销商ID（ERP客户编号）",
              isChecked: true,
              value: "DISTRIBUTOR_ERP_NO",
            },
            {
              name: "客户名称",
              isChecked: true,
              value: "DISTRIBUTOR_NAME",
            },
            // {
            //   name: "事业部",
            //   isChecked: true,
            //   value: "causeDepartment",
            // },
            {
              name: "部门名称",
              isChecked: true,
              value: "DEPARTMENT_NAME",
            },
            {
              name: "销售员",
              isChecked: true,
              value: "SALES_NAME",
            },
          ],
        },
        {
          name: "收货信息",
          isItemIndeterminate: false,
          checkAllItem: false,
          children: [
            {
              name: "收货人",
              isChecked: false,
              value: "RECEIVER",
            },
            {
              name: "收货地址",
              isChecked: false,
              value: "RECEIVE_ADDRESS",
            },
            {
              name: "联系电话",
              isChecked: false,
              value: "CONTACT_MOBILE",
            },
            {
              name: "配送方式",
              isChecked: false,
              value: "DISTRIBUTION_NAME",
            },
            {
              name: "物流单号",
              isChecked: false,
              value: "LOGISTICS_NO",
            },
          ],
        },
        {
          name: "商品信息",
          isItemIndeterminate: false,
          checkAllItem: false,
          children: [
            {
              name: "货品编码",
              isChecked: true,
              value: "ITEM_CODE",
            },
            {
              name: "货品名称",
              isChecked: true,
              value: "ITEM_NAME",
            },
            {
              name: "销售数量",
              isChecked: true,
              value: "ITEM_COUNT",
            },
            {
              name: "已发货数",
              isChecked: true,
              value: "DELIVER_COUNT",
            },
          ],
        },
        {
          name: "定制信息",
          isItemIndeterminate: false,
          checkAllItem: false,
          children: [
            {
              name: "工厂订单号",
              isChecked: false,
              value: "ORDER_FACTORY_NO",
            },
            {
              name: "第三方订单号",
              isChecked: false,
              value: "ORDER_THIRDPARTY_NO",
            },
            {
              name: "材质",
              isChecked: false,
              value: "MATERIAL_NAME",
            },
            {
              name: "型号",
              isChecked: false,
              value: "MODEL_NAME",
            },
            {
              name: "门店名称",
              isChecked: false,
              value: "SHOP_NAME",
            },
            {
              name: "门店编码",
              isChecked: false,
              value: "SHOP_CODE",
            },
            {
              name: "图片编码",
              isChecked: false,
              value: "PICTURE_ID",
            }
          ],
        },
        {
          name: "价格信息",
          isItemIndeterminate: false,
          checkAllItem: false,
          children: [
            {
              name: "价格（含税单价）",
              isChecked: true,
              value: "SALE_PRICE",
            },
            {
              name: "折后价（折扣后含税单价）",
              isChecked: true,
              value: "ACTUAL_PRICE",
            },
            {
              name: "总额（折前含税总额）",
              isChecked: true,
              value: "SALE_PRICE_SUM",
            },
            {
              name: "总额（折后含税总额）",
              isChecked: true,
              value: "ACTUAL_PRICE_SUM",
            },
            {
              name: "C端下单总额（实付金额）",
              isChecked: true,
              value: "USER_ACTUAL_PRICE",
            },
            {
              name: "促销活动编码",
              isChecked: true,
              value: "PROMOTION_ID",
            }
            // {
            //   name: "提货增长返利",
            //   isChecked: true,
            //   value: "pickUpRebate",
            // },
            // {
            //   name: "商品等级折扣",
            //   isChecked: true,
            //   value: "gradeDiscount",
            // },
          ],
        },
      ],
      isIndeterminate: true,
      searchDistributor: "", //..分销商搜索
      distributorData: "", //..分销商列表
    };
  },
  created() {
    this.getOrderSource();
    this.getOrderTypes();

    // 初始化已选导出字段
    this.exportField.forEach((item) => {
      if (item.children && item.children.length > 0) {
        let len = 0;
        item.children.forEach((child) => {
          if (child.isChecked) {
            len++;
            item.isItemIndeterminate = true;
          }
        });
        if (len >= item.children.length) {
          item.isItemIndeterminate = false;
          item.checkAllItem = true;
        }
      }
    });
  },
  methods: {
    // 获取订单来源
    getOrderSource() {
      this.$http.getSysPlatformList(this, this.pageInfo).then((res) => {
        if (res.success) {
          this.orderSources = res.data.list
        }
      })
    },
    // 获取订单类型
    getOrderTypes() {
      this.$http.orderTypeList(this, this.pageInfo) 
        .then((res) => {
          if (res.success) {
            this.orderTypes = res.data.list;
          }
        });
    },
    // 分销商类型
    handleDisType (type) {
      this.searchDistributor = ''
      this.formData.distributorId = ''
      this.distributorData = []
    },
    // 订单导出明细
    exportDownload() {
      // let exportOption = {}; //..导出字段
      let fieldList = []
      let hasExportOption = false; //..是否有选中导出字段
      this.exportField.forEach((item) => {
        if (item.children && item.children.length > 0) {
          item.children.forEach((child) => {
            // 设置导出字段
            // exportOption[child.value] = child.isChecked;
            if (child.isChecked) {
              // 导出字段
              fieldList.push(child.value)
              // 有，设置为true
              hasExportOption = true;
            }
          });
        }
      });
      if (!hasExportOption) {
        // 为false时，提示用户选择
        this.$message.warning("请选择导出字段");
        return;
      }

      if (this.formData.time == undefined) {
        this.$message.warning("请选择订单时间");
        return;
      }

      if (this.formData.status.length == 0) {
        this.$message.warning("请选择订单状态");
        return;
      }

      //..导出订单折扣明细表
      this.timeData = []; //..清空时间
      this.loading = true;
      this.timeData.push(this.formData.time[0]); //..开始时间
      this.timeData.push(this.formData.time[1] + 24 * 60 * 60 * 1000); //..结束时间选择当天加上一天

      // 范围
      let type
      if (this.formData.type === 0) {
        type = undefined
      } else if (this.formData.type === 1) {
        type = true
      } else if (this.formData.type === 2) {
        type = false
      }
      
      let dataInfo = {
        activityFlag: type, //..活动参与
        startTime: monthDay(this.timeData[0]).trim(),
        endTime: monthDay(this.timeData[1]).trim(),
        // time: this.timeData, //..订单时间
        // businessUnitId: this.formData.businessUnitId, //..事业部ID
        itemCode: this.formData.itemCode, //..货品编码
        orderStatusList: this.formData.status.length === 1 && this.formData.status[0] === 0 ? undefined : this.formData.status, //..订单状态
        deliverStatus: this.formData.deliverStatus.length === 1 && this.formData.deliverStatus[0] === 0 ? undefined : this.formData.deliverStatus, // 发货状态
        payStatusList: this.formData.payStatus.length === 1 && this.formData.payStatus[0] === 0 ? undefined : this.formData.payStatus, // 付款状态
        orderSourceList: this.formData.orderSource, //..订单来源
        orderTypeIdList: this.formData.orderType, //..订单类型
        fieldList: fieldList, //..导出字段
        distributorId: this.formData.distributorId, //..分销商id
      }

      let tenantUrl = localStorage.getItem('tenantUrl');
      axios({
        method: 'post',
        url:tenantUrl + '/' + 'order/v1/web/admin/orderInfo/export' ,
        responseType: 'arraybuffer',
        headers: {
          'Content-Type': 'application/json',
          token: getToken()
        },
        data: dataInfo
      }).then((res) => {
        if (res === undefined) {
          this.loading = false;
          this.timeData = [] //..成功下载Excel后初始化选中时间
          this.formData.status = [] //..成功夏侯Excel后初始化订单状态
        }else {
          const content = res.data;
          let blob = new Blob([content], {
            type: "application/ms-excel",
          });
          if (blob.size <= 83) {
            this.loading = false;
            this.$message.warning("无符合条件数据")
            return
          } else {
            let url = window.URL.createObjectURL(blob);
            if ("download" in document.createElement("a")) {
              let link = document.createElement("a");
              link.style.display = "none";
              link.href = url;
              link.setAttribute("download", "订单明细表.xls");
              document.body.appendChild(link);
              link.click();
            } else {
              navigator.msSaveBlob(blob, "订单明细表.xls");
            }
            this.loading = false;
          }
        }
      });
    },

    getDepList() {
      //..事业部
      this.$api.get(this, "admin/u/po/businessUnit/list").then((res) => {
        let ary = [];
        res.businessUnits.forEach((item) => {
          item.children = [];
          ary.push(item);
        });
        ary.sort((a, b) => {
          return a.sort - b.sort > 0;
        });
        this.businessUnitList = this.businessUnitList1.concat(ary);
      });
      this.formData.businessUnitId = 0; //..默认选中事业部
    },
    // 导出字段 -- 全选/取消全选
    handleCheckAllChange(val) {
      this.exportField.forEach((item) => {
        if (item.children && item.children.length > 0) {
          item.isItemIndeterminate = false;
          if (val) {
            // 全选
            item.children.forEach((child) => {
              child.isChecked = true;
            });
            item.checkAllItem = true;
          } else {
            // 取消全选
            item.children.forEach((child) => {
              child.isChecked = false;
            });
            item.checkAllItem = false;
          }
        }
      });
      this.isIndeterminate = false;
    },
    // 是否全选
    isCheckAll() {
      let total = 0;
      let len = 0;
      this.exportField.forEach((item) => {
        if (item.children && item.children.length > 0) {
          item.children.forEach((child) => {
            // 获取导出字段总数量
            total++;
            if (child.isChecked) {
              // 获取已选中数量
              len++;
            }
          });
        }
      });
      if (len === 0) {
        // 已选数量为0，取消全选
        this.checkAll = false;
        this.isIndeterminate = false;
      } else {
        // 设置全选样式
        this.checkAll = len > 0 && len < total ? false : true;
        this.isIndeterminate = len > 0 && len < total ? true : false;
      }
    },
    // 导出字段 -- 某类信息全选/取消全选
    handleCheckAllItemChange(index) {
      this.exportField[index].isItemIndeterminate = false;
      if (this.exportField[index].checkAllItem) {
        // 全选某类
        this.exportField[index].children.forEach((child) => {
          child.isChecked = true;
        });
      } else {
        // 取消全选某类
        this.exportField[index].children.forEach((child) => {
          child.isChecked = false;
        });
      }
      // 是否全选
      this.isCheckAll();
    },
    // 导出字段 -- 某一选项
    handleCheckedItemChange(index, item) {
      // 获取当前类所有已选长度
      let len = 0;
      this.exportField[index].children.forEach((child) => {
        if (child.isChecked) {
          len++;
        }
      });
      if (len <= 0) {
        // 小于等于0，取消选中当前类并清空复选框样式
        this.exportField[index].isItemIndeterminate = false;
        this.exportField[index].checkAllItem = false;
      } else {
        // 大于0
        if (len >= this.exportField[index].children.length) {
          // 当前类全选，选中当前类并清空复选框样式
          this.exportField[index].isItemIndeterminate = false;
          this.exportField[index].checkAllItem = true;
        } else {
          // 设置复选框样式
          this.exportField[index].isItemIndeterminate = true;
          this.exportField[index].checkAllItem = false;
        }
      }
      // 是否全选
      this.isCheckAll();
    },
    // 重置
    handleReset() {
      this.reload();
    },
    // 查询合作中分销商列表
    querySearch(queryString, cb) {
      if (queryString != "") {
        let callBackArr = [];
        // let pageInfo = {
        //   content: this.searchDistributor,
        // };
        if(this.distributorType === 1) {
          // 一级分销商
          this.$http.getDistributorFList(this, {
            page: 1,
            size: 1000,
            profileStatus: 2,
            freezeStatus: 1,
            contentType: 1,
            content: this.searchDistributor
          }).then(res => {
            const ary = res.data.list;
            ary.forEach((item) => {
              this.$set(item, "value", item.name);
            });
            ary.forEach((item) => {
              if (
                item.name.indexOf(queryString) > -1 ||
                item.companyName.indexOf(queryString) > -1 ||
                item.id == queryString
              ) {
                callBackArr.push(item);
              }
            });
            if (callBackArr.length == 0) {
              cb([{ value: "暂无数据" }]);
            } else {
              cb(callBackArr);
            }
          })
        }else {
          // 多级分销商
          this.$http.getDistributorNList(this, {
            page: 1, 
            size: 1000,
            contentType: 1,
            content: this.searchDistributor
          }).then(res => {
            const ary = res.data.list;
            ary.forEach((item) => {
              this.$set(item, "value", item.name);
            });
            ary.forEach((item) => {
              if (
                item.name.indexOf(queryString) > -1 ||
                item.companyName.indexOf(queryString) > -1 ||
                item.id == queryString
              ) {
                callBackArr.push(item);
              }
            });
            if (callBackArr.length == 0) {
              cb([{ value: "暂无数据" }]);
            } else {
              cb(callBackArr);
            }
          })
        }  
        // searchDistributor(this, pageInfo).then((res) => {
        //   const ary = res.distributorSearches;
        //   ary.forEach((item) => {
        //     this.$set(item, "value", item.name);
        //   });
        //   ary.forEach((item) => {
        //     if (
        //       item.name.indexOf(queryString) > -1 ||
        //       item.companyName.indexOf(queryString) > -1 ||
        //       item.id == queryString
        //     ) {
        //       callBackArr.push(item);
        //     }
        //   });
        //   if (callBackArr.length == 0) {
        //     cb([{ value: "暂无数据" }]);
        //   } else {
        //     cb(callBackArr);
        //   }
        // });
      }
    },
    // 获取点击分销商信息
    handleSelect(item) {
      this.distributorData = [];
      this.distributorData.push(item);
      this.formData.distributorId = item.id;
    },
  },
  watch: {
    searchDistributor: {
      handler(val) {
        if (val === '') {
          this.formData.distributorId = ''
          this.distributorData = []
        }
      },
      deep:true
    },
    'formData.status': {
      handler(val) {
        if (val.join(',').indexOf(0) > -1 && val.length > 1) {
          this.isOrderShow = true
          this.formData.status = [0]
        } else if (val.join(',').indexOf(0) > -1 && val.length === 1) {
          this.isOrderShow = true
        } else {
          this.isOrderShow = false
        }
      },
      deep: true
    },
    'formData.deliverStatus': {
      handler(val) {
        if (val.join(',').indexOf(0) > -1 && val.length > 1) {
          this.isDeliverShow = true
          this.formData.deliverStatus = [0]
        } else if (val.join(',').indexOf(0) > -1 && val.length === 1) {
          this.isDeliverShow = true
        } else {
          this.isDeliverShow = false
        }
      },
      deep: true
    },
    'formData.payStatus': {
      handler(val) {
        if (val.join(',').indexOf(0) > -1 && val.length > 1) {
          this.isPayShow = true
          this.formData.payStatus = [0]
        } else if (val.join(',').indexOf(0) > -1 && val.length === 1) {
          this.isPayShow = true
        } else {
          this.isPayShow = false
        }
      },
      deep: true
    }
  },
  inject: ["reload"],
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.discount {
  background-color: #fff;
  min-width: 1050px;
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
  }
  .dis_body {
    padding-bottom: 30px;
    .dis_title {
      font-size: 12px;
      color: $lakeBlue;
      line-height: 40px;
      margin-top: 30px;
      padding-left: 50px;
    }
    .el-form1 {
      margin-top: 0;
      width: 100%;
      .time_hint {
        color: #aaa;
        font-size: 12px;
        margin: 0;
      }
      .el_form_span {
        color: #aaa;
        font-size: 12px;
      }
    }
  }
}
</style>

