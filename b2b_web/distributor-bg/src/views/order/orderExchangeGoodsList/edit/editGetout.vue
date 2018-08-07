<template>
  <div class="warehouse-list-wrap">
      <div>
          <header><span>违规</span></header>
      </div>

      <div class="box-has-border">
        <div class="text-center title">
            <span>基本信息</span>
        </div>
        <!-- 左侧 -->
        <div class="half-width">
            <el-form ref="chosen" label-width="280px">
            <el-form-item label="退换货申请单号:"> {{afterServiceBean.courierNo}}</el-form-item>
            <el-form-item label="配送方式">{{manner(afterServiceBean.courierType)}}</el-form-item>
            </el-form>
        </div>
        <!-- 右侧 -->
        <div class="half-width">
            <el-form ref="chosen" label-width="280px">
              <el-form-item label="申请时间:">{{timeFormatter(afterServiceBean.createTime)}}</el-form-item>
              <el-form-item label="申请人:">{{afterServiceBean.userName}} </el-form-item>
              <!-- <el-form-item label="下单时间:">{{timeFormatter(afterServiceBean.createTime)}}</el-form-item> -->
            </el-form>
        </div>
      </div>

      <div class="box-has-border">
        <div class="text-center title">
            <span>收货人信息</span>
        </div>
        <!-- 左侧 -->
        <div class="half-width">
            <el-form ref="chosen" label-width="280px">
            <el-form-item label="收货人:">
              {{order.delivery.userName || '空字符串或不存在...'}}
            </el-form-item>
            <el-form-item label="地址:">
              {{order.delivery.address || '空字符串或不存在...'}}
            </el-form-item>
            <el-form-item label="电话">
              {{order.delivery.phone || '空字符串或不存在...'}}
            </el-form-item>
            <el-form-item label="最近送货时间">
              {{order.delivery.deliveryTime  || '空字符串或不存在...'}}
            </el-form-item>
            </el-form>
        </div>
        <!-- 右侧 -->
        <div class="half-width">
            <el-form ref="chosen" label-width="280px">
              <el-form-item label="电子邮件:"> 
                
              </el-form-item>
              <el-form-item label="邮编:">
                {{order.delivery.zipCode || '空字符串或不存在...'}}
              </el-form-item>
              <el-form-item label="手机：">
                {{order.delivery.mobile  || '空字符串或不存在...'}}
              </el-form-item>
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
                <th>货号</th>
                <th>货品号</th>
                <th>规格</th>
                <th>未处理数量</th>
                <th>违规数量</th>
              </tr>
              <tr v-for="Aoge in afterServiceBean.list" :key="Aoge.id">
                <td>{{Aoge.goodsName}}</td>
                <td>{{Aoge.goodsNo}}</td>
                <td>{{Aoge.orderId}}</td>
                <td>{{Aoge.itemId}}</td>
                <td>{{Aoge.itemCode}}</td>
                <td>{{Aoge.specificationName}}</td>
                <td>{{Aoge.payAmountB}}</td>
                <td>
                    <el-input v-model="Aoge.againstCount"></el-input>
                </td>
              </tr>
            </table>

            <el-form label-width="180px" align="center" class="actionButtons">
                <!-- <el-form-item> -->
                  <el-button @click="affirmAog()">确认</el-button>
                  <el-button @click="onEscPress()">取消</el-button>
                <!-- </el-form-item> -->
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
  name: "editGetout",
  mounted() {
    this.aogData();
  },
  data() {
    return {
      mytext: "",
      order : {},
      afterServiceBean: {},
      operateBean: [], // 最后确认数量
      operateBeanOne: [], // 一开始存储数量
      operationData: [],
      operateBeanZero: [], // 申请数量
      productOne: [], // 到货数量
      productTwo: [], // 退货数量
      productThree: [], // 换货数量
      productFour: [], // 违规数量
      payAmount: [], // 未处理数量
    };
  },
  computed: {
    // 未处理货品数量 payAmount
    payAmountData() {
      return this.payAmount
    },
  },
  methods: {
    affirmAog() {
      let query = this.$route.query;
      for(let i = 0, len = this.afterServiceBean.list.length; i < len; i++){   
        var product = {
          detailId: this.afterServiceBean.list[i].id, 
          count: this.afterServiceBean.list[i].againstCount
        }
        this.operateBean.push(product)
      }
      // let long = this.operateBean.filter( (bean, i) => {
      //   return bean.count == this.afterServiceBean.list[i].againstCount
      // })
      this.$api.put(this, "admin/u/afterservice", {
          id: query.id,
          operateId: 2,
          remark: this.mytext,
          operateBeans: this.operateBean
          // operateBeans: long
        }).then(res => {
          if (res.code == 0) {
            this.$message.success({
              message: '成功操作',
              duration: 3 * 1000,
              onClose: () => { }
            })
            this.$router.go(-1)
          } else {
            this.$message.success({
              message: '操作失败',
              duration: 3 * 1000,
              onClose: () => {
                  // this.$router.go(-1)
              }
          })
          }
        });
    },
    onEscPress() {
      this.$router.go(-1);
    },
    // 违规数据
    aogData() {
      let query = this.$route.query;
      this.$api.get(this, "admin/u/afterservice", { id: query.id }).then(res => {
          this.afterServiceBean = res.afterServiceBean;
          
          // for(let i = 0, len = this.afterServiceBean.list.length; i < len; i++){   
          //   var productOne = {
          //     detailId: this.afterServiceBean.list[i].id, 
          //     count: this.afterServiceBean.list[i].againstCount
          //   }
          //   this.operateBeanOne.push(productOne)
          // }

          for(let i = 0, len = this.afterServiceBean.list.length; i < len; i++){   
            var product = {applyCount: this.afterServiceBean.list[i].applyCount} // 申请数量
            var productOne = {arrivalCount: this.afterServiceBean.list[i].arrivalCount} // 到货数量
            var productTwo = {returnCount: this.afterServiceBean.list[i].returnCount} // 退货数量
            var productThree = {exchangeCount: this.afterServiceBean.list[i].exchangeCount} // 换货数量
            var productFour = {againstCount: this.afterServiceBean.list[i].againstCount} // 违规数量
            this.operateBeanZero.push(product)  // 申请数量
            this.productOne.push(productOne)  // 到货数量
            this.productTwo.push(productTwo)  // 退货数量
            this.productThree.push(productThree)  // 换货数量
            this.productFour.push(productFour)  // 违规数量
            // 未处理数量
            this.payAmount = this.afterServiceBean.list[i].arrivalCount - this.afterServiceBean.list[i].returnCount - this.afterServiceBean.list[i].exchangeCount - this.afterServiceBean.list[i].againstCount;
            this.afterServiceBean.list[i].payAmountB = this.payAmount
          }
          return res.afterServiceBean;
        });
        // 订单详情数据，譬如，收货人信息，发票，金额，商品等信息
        this.$api.get(this,'admin/u/p/order',{id: query.orderId}).then(res => {
          this.order = res.order
          var DeliverBeans = {
            provinceId: this.order.delivery.provinceId,         // 省id
            // provinceName: this.order.delivery.provinceId,    // 省名字
            cityId: this.order.delivery.cityId,                 // 城市id
            // cityName: this.order.delivery.cityName,          // 城市名字
            townId: this.order.delivery.townId,                 // 市id
            // townName: this.order.delivery.townName,          // 市名字
            name: this.order.delivery.userName,                 // 名字
            address:  this.order.delivery.address,              // 地址
            phone: this.order.delivery.phone,                   // 电话
            deliveryType: this.order.delivery.deliveryType,     // 1工作日, 2任意时间, 3双休, 4指定日期'
            deliveryTime: this.order.delivery.deliveryTime,     // 送货时间
            email: this.order.delivery.email,                   // 邮箱
            zipCode: this.order.delivery.zipCode,               // 邮编
            mobile: this.order.delivery.mobile,                 // 手机
          }
          this.DeliverBean.push(DeliverBeans)
      })
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