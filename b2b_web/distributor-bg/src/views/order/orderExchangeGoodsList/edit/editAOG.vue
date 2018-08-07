<template>
  <div class="warehouse-list-wrap">
      <div>
          <header><span>退换货单操作: 到货</span></header>
      </div>

      <div class="box-has-border">
        <div class="text-center title">
            <span>基本信息</span>
        </div>
        <!-- 左侧 -->
        <div class="half-width">
            <el-form ref="chosen" label-width="280px">
            <el-form-item label="包裹单号:" >
                {{afterServiceBean.courierNo}}
            </el-form-item>
            <el-form-item label="配送方式:">
                {{manner(afterServiceBean.courierType)}}
            </el-form-item>
            </el-form>
        </div>
        <!-- 右侧 -->
        <div class="half-width">
            <el-form ref="chosen" label-width="280px">
            <el-form-item label="包裹状态:">
                {{forstatus(afterServiceBean.status)}}
            </el-form-item>
            <el-form-item label="登记时间:">
                <span>
                {{timeFormatter(afterServiceBean.createTime)}}
                </span>
            </el-form-item>
            <el-form-item label="到货时间:"> {{timeFormatter(afterServiceBean.arriveTime)}} </el-form-item>
            </el-form>
        </div>
      </div>
        

        <!-- 商品信息 -->
        <div class="box-has-border">
          <div class="text-center title">
            <span>商品信息</span>
          </div>
          <div class="edit-distribution">
              <table>
                  <tr>
                      <th>商品名称</th>
                      <th>商品编号</th>
                      <th>订单号</th>
                      <th>规格</th>
                      <th>申请数量</th>
                      <th style="width:20%;">到货数量</th>
                  </tr>
                  <tr v-for="Aoge in afterServiceBean.list" :key="Aoge.id">
                      <td>{{Aoge.goodsName}}</td>
                      <td>{{Aoge.goodsNo}}</td>
                      <td>{{Aoge.orderId}}</td>
                      <td>{{Aoge.specificationValueName}}</td>
                      <td>{{Aoge.applyCount}}</td>
                      <td>
                          <el-input size="small" v-model="Aoge.arrivalCount"   style="max-width:200px;"></el-input>
                      </td>
                  </tr>
              </table>
          </div>

        </div>

        <!-- 操作信息 -->
        <div class="box-has-border">
            <div class="text-center title">
              <span>操作信息</span>
            </div>
            <div class="form">
              <el-form label-width="180px">
                <el-form-item label="操作备注:" >
                  <el-input type="textarea" :rows="5" v-model="mytext"></el-input>
                </el-form-item>
              </el-form>
              <el-form label-width="180px" align="center">
                <el-form-item label="当前可执行操作:">
                  <el-button type="primary" @click="affirmAog()">确认到货</el-button>
                  <el-button type="primary" @click="onEscPress()">取消</el-button>
                </el-form-item>
              </el-form>
            </div>
        </div>

      </div>
</template>

<script>
import { getOrderDetail } from "@/views/order/orderData";
import procedure from "@/components/Procedure/index";
import { parseTime } from "@/utils/index";
import { timeFormat } from "@/utils/timeFormat";

export default {
  name: "editAOG",
  mounted() {
    this.aogData();
  },
  data() {
    return {
      mytext: "",
      count: "",
      afterServiceBean: {},
     operateBean : [],
     Aoge: {}
    };
  },
  methods: {
    // watchDiscount(data) {},
    // 到货数据
    aogData() {
      let query = this.$route.query;
      this.$api.get(this, "admin/u/afterservice", { id: query.id }).then(res => {
          this.afterServiceBean = res.afterServiceBean;
          return res.afterServiceBean;
        });
    },
    
    // 确认到货
    affirmAog() {
      let query = this.$route.query;
      for(let i = 0, len = this.afterServiceBean.list.length; i < len; i++){   
        var product = {
          detailId: this.afterServiceBean.list[i].id, 
          count: this.afterServiceBean.list[i].arrivalCount
        }
        this.operateBean.push(product)
      }  
      this.$api.put(this,'admin/u/afterservice',{
        id: query.id,
        operateId: 2,
        remark: this.mytext,
        operateBeans: this.operateBean
      }).then(res => {
        if(res.code == 0) {
          this.$message.success({
              message: '成功操作',
              duration: 3 * 1000,
              onClose: () => { }
          })
          this.$router.go(-1)
          // this.$router.go(-1)
        }else {
          this.$message.success({
              message: '操作失败',
              duration: 3 * 1000,
              onClose: () => {
                  // this.$router.go(-1)
              }
          })
        }
      })
    },
    // 取消按钮
    onEscPress() {
      this.$router.go(-1);
    },
    
    // 时间格式化
    timeFormatter(cellValue) {
      return parseTime(cellValue);
    },
    // 配送方式
    manner(row) {
      switch (row) {
        case 1:
          return "顺丰快递";
        case 2:
          return "申通快递";
        case 3:
          return "圆通快递";
        case 4:
          return "中通快递";
        case 5:
          return "百世快递";
        case 6:
          return "韵达快递";
        case 7:
          return "天天快递";
        case 8:
          return "中国邮政";
        case 9:
          return "EMS";
        case 10:
          return "宅急送";
        case 11:
          return "德邦物流";
        case 12:
          return "全峰快递";
        case 13:
          return "优速快递";
        case 14:
          return "汇通快递";
        case 15:
          return "速尔快递";
      }
    },

    // 包裹状态
    forstatus(row) {
      switch (row) {
        case 0:
          return "申请中";
        case 1:
          return "待到货";
        case 2:
          return "部分到货";
        case 3:
          return "处理中";
        case 4:
          return "已完成";
        case 5:
          return "已拒绝";
        case 6:
          return "退款中";
      }
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.warehouse-list-wrap {
  min-width: 1050px;
  background-color: #fff;
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
    span {
      margin-left: 30px;
    }
    .btn-add {
      float: right;
      padding: 5px;
      margin-top: 7px;
      margin-right: 8px;
    }
  }
  .box-has-border {
    overflow: hidden;
    margin-top: 15px;
    background-color: #fff;
    .title {
      padding-bottom: 10px;
      padding-top: 10px;
      border-top: 1px solid $tableColor;
      border-bottom: 1px solid $tableColor;
      text-align: center;
      background-color: lighten(grey, 40%);
    }
    .header-row {
      th {
        text-align: center;
      }
    }
    .form {
      width: 80%;
      padding: 20px 0 0 0;
    }
    .edit-distribution {
      table {
        width: 100%;
        border-collapse: collapse;
        tr {
          border-bottom: 1px solid $tableColor;
          th {
            padding: 20px 0;
            width: calculate-width(7);
          }
          td {
            padding: 20px 0;
            text-align: center;
          }
        }
      }
    }

    .half-width {
      width: 50%;
      box-sizing: border-box;
      float: left;
      .half_search {
        width: 35%;
      }
      .el-form-item {
        margin-bottom: 10px;
      }
    }
  }
}
</style>