<!-- 因为有“上一条”“下一条”功能，故不选择路由id传参，而是props传参 -->
<template>
  <main class="edit-cost">
    <header>
      <span>生成发货单</span>
    </header>
    <div class="box-has-border">
      <div class="text-center title">
        <span>基本信息</span>
      </div>

      <div class="half-width">
        <el-form ref="formData" label-width="280px">
          <el-form-item label="订单号">{{formData.id}}</el-form-item>
          <el-form-item label="购买者">{{formData.distributorName || '该字段为空'}}</el-form-item>
        </el-form>
      </div>

      <div class="half-width">
        <el-form ref="formData" label-width="190px">
          <el-form-item label="下单时间">{{timeFormatter(formData.createTime)}}</el-form-item>
          <el-form-item label="配送方式">
            <span v-if="formData.distribution">{{distributionsFormatter(formData.distribution.id)}}</span>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <div class="box-has-border" v-if="formData.delivery">
      <div class="text-center title">
        <span>收货人信息</span>
      </div>
      <div class="half-width">
        <el-form ref="formData" label-width="280px">
          <el-form-item label="收货人">{{formData.delivery.userName || '空字符串或不存在...'}}</el-form-item>
          <el-form-item label="地址">{{formData.delivery.address || '空字符串或不存在...'}}</el-form-item>
          <el-form-item label="电话">{{formData.delivery.phone || '空字符串或不存在...'}}</el-form-item>
          <el-form-item label="最近送货时间">{{formData.delivery.deliveryTime || '空字符串或不存在...'}}</el-form-item>
        </el-form>
      </div>

      <div class="half-width">
        <el-form ref="formData" label-width="190px">
          <el-form-item label="电子邮件">接口未提供</el-form-item>
          <el-form-item label="邮编" v-if="formData.delivery" >{{formData.delivery.zipCode || '空字符串或不存在...'}}</el-form-item>
          <el-form-item label="手机" v-if="formData.delivery" >{{formData.delivery.mobile || '空字符串或不存在...'}}</el-form-item>
          <el-form-item label="分销商留言">{{formData.remark || '空字符串或不存在...'}}</el-form-item>
        </el-form>
      </div>
    </div>

    <div class="box-has-border">
      <div class="text-center title">
        <span>商品信息</span>
        <el-button size="mini" @click="_ => {$emit('switchPageState', 'editGoodsInfo')}">编辑</el-button>
      </div>
      <el-table border :data="formData.goods" style="width: 100%" header-row-class-name="header-row" >
        <el-table-column align="center" prop="goodsName" label="商品名称"></el-table-column>
        <el-table-column align="center" prop="goodsId" label="商品编号"></el-table-column>
        <el-table-column align="center" prop="itemId" label="货号"></el-table-column>
        <el-table-column align="center" prop="itemCode" label="货品号"></el-table-column>
        <el-table-column align="center" prop="specificationName" label="规格"></el-table-column>
        <el-table-column align="center" prop="salePrice" label="价格"></el-table-column>
        <el-table-column align="center" prop="itemDiscount" label="价格折扣"></el-table-column>
        <el-table-column align="center" prop="saleTotalAmount" label="数量"></el-table-column>
        <el-table-column align="center" prop="deliverCount" label="已发数量"></el-table-column>
        <el-table-column align="center" prop="unDeliverCount" label="此单发货数量">
          <template slot-scope="scope">
            <el-input v-model="scope.row.sendCount"></el-input>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="saleTotalAmount" label="小计"></el-table-column>
      </el-table>
    </div>

    <div class="box-has-border operation">

      <div class="text-center title">
        <span>操作信息</span>
      </div>
      <div class="form">
        <el-form label-width="180px">
          <el-form-item label="操作备注">
            <el-input v-model="remark" type="textarea" :rows="5" placeholder="请输入内容"/>
          </el-form-item>
        </el-form>
      </div>

      <div class="operation">
        <span class="instruction">当前可执行操作：</span>
        <span class="operation-part">
          <el-button size="mini" @click="onConfirm" type="primary">确认生成发货单</el-button>
          <el-button size="mini" @click="onCancel">取消</el-button>
        </span>
      </div>
      
    </div>
  </main>
</template>

<script>
import eventBus from "@/views/order/eventBus";
import { parseTime } from "@/utils/index";
import success from "@/views/order/components/oprationSuccess";
import {
  editOrder,
  operateOrder,
  getdistributions
} from "@/views/order/orderData";
import { confirmCreator,  chooseBus, orderStatusFormatter } from "@/views/order/orderUtils";

export default {
  name: "createDelivery",
  created() {
    this.updateMainData();

    getdistributions(this).then(res => {
      this.distributions = JSON.parse(JSON.stringify(res.distributions));
    });
  },
  components: {
    success
  },
  data() {
    return {
      successState: false,
      formData: {},
      distributions: [],
      remark: ""
    };
  },
  computed: {
    orderId() {
      return this.$store.getters.orderId;
    },
    orderDetail() {
      return this.$store.getters.orderDetail;
    }
  },
  methods: {
    updateMainData() {
      const currOrderId = this.$route.query.orderId;
      if (this.orderId != currOrderId) {
        this.$store.dispatch("updateOrderId", currOrderId).then(_ => {
          this.formData = JSON.parse(JSON.stringify(this.orderDetail));
          this.formData.goods.forEach((obj, index, arr) => {
            this.$set(obj, "sendCount", 0);
          });
        });
      } else {
        this.formData = JSON.parse(JSON.stringify(this.orderDetail));
        this.formData.goods.forEach((obj, index, arr) => {
          this.$set(obj, "sendCount", 0);
        });
      }
    },
    onConfirm() {
      // 点击事件，生成发货单
      const delivers = this.formData.goods.map(obj => ({
        id: obj.id,
        count: obj.sendCount
      }));

      confirmCreator(this)("生成发货单", _ => {
        return operateOrder(this, {
          id: this.orderId,
          operateType: 4,
          delivers,
          remark: this.remark
        });
      }).then(_ => eventBus.$emit("shouldUpdateData")); // 操作成功后 更新根数据
    },
    onCancel() {
      eventBus.$emit("switchPageState", "orderDetail");
    },

    distributionsFormatter(id) {
      const distributions = this.distributions;
      for (let distribution of distributions) {
        if (distribution.id == id) {
          return distribution.name;
        }
      }
      return id;
    },
    orderStatusFormatter(stateCode) {
      this.orderStatusFormatter = orderStatusFormatter;
      return this.orderStatusFormatter(stateCode);
    },
    timeFormatter(rawData) {
      this.timeFormatter = parseTime;
      return this.timeFormatter(rawData);
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.edit-cost {
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
  .operation {
    .instruction {
      padding-left: 50px;
    }
    padding-bottom: 50px;
    background-color: white;
  }
  .box-has-border {
    overflow: hidden;
    background-color: white;
    .cost-line {
      padding-bottom: 10px;
      padding-top: 10px;
      border-bottom: 1px solid $tableColor;
      padding-left: 30px;
      span.cost-info {
        margin-left: 5px;
      }
      span.cost-info:last-child {
        margin-right: 35px;
      }
    }
    .cost-line:last-child {
      border-bottom: none;
    }

    .align-right {
      text-align: right;
    }
    div.form {
      margin-top: 30px;
      margin-bottom: 40px;
      form.el-form {
        margin-right: 0;
        width: 80%;
        min-width: 800px;
        max-width: 1000px;
      }
    }
  }
  .text-center {
    text-align: center;
  }
  .text-center.title {
    // margin-bottom: 25px;
    background-color: $bg;
    padding-top: 10px;
    padding-bottom: 10px;
    border-bottom: 1px solid $tableColor;
    border-top: 1px solid $tableColor;
  }
  .half-width {
    width: 50%;
    box-sizing: border-box;
    float: left;
  }
  div.btns {
    text-align: center;
    margin-top: 30px;
  }
}
</style>
