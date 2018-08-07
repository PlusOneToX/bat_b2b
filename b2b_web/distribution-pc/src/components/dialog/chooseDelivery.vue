<!--
 * @Author: liumeili
 * @Date: 2018-05-07 08:58:46
 * @LastEditors: liumeili
 * @LastEditTime: 2018-06-22 15:27:06
-->
<template>
  <div class="choose-wrap">
    <div class="inner-content">
      <span class="el-icon-close" @click="handleClose()"></span>
      <p class="tips" v-if="$i18n.locale === 'zh'">
        您所选的商品现有在库库存不足，商品需分批次发出，请选择您想要的发货形式（
        <span class="red">注：拆单后将分为在库/在途两个批次进行发货，运费分别计算</span>）
      </p>
      <p class="tips" v-else>
        The goods you selected are not in enough stock. Please select the
        delivery method you want. (
        <span class="red">Note: split shipment means the current inventory will be shipped
          firstly and in-transit inventory will be shipped later, and the
          freight will be calculated separately</span>)
      </p>
      <div class="table-wrap">
        <table>
          <thead>
            <tr>
              <th width="50">{{ $t("ShopCarts.Picture") }}</th>
              <th>{{ $i18n.locale === "zh" ? "编码/条形码" : "Code" }}</th>
              <th>{{ $t("ShopCarts.ItemName") }}</th>
              <th>{{ $t("ShopCarts.Spe") }}</th>
              <th>{{ $t("ShopCarts.Colors") }}</th>
              <th>
                {{ $i18n.locale === "zh" ? "订购数量" : "Ordering Quantity" }}
              </th>
              <th>{{ $t("ShopCarts.InventoryQuantity") }}</th>
              <th>{{ $t("ShopCarts.IntransitQuantity") }}</th>
            </tr>
          </thead>
          <tbody>
            <tr class="goods-item" v-for="(item, index) in deliveryData" :key="index">
              <td>
                <img width="40" :src="item.imageUrl"  />   
              </td>
              <td>{{ item.itemCode }}</td>
              <td>{{ item.itemName }}</td>
              <td>
                {{$i18n.locale === "zh"? item.specsName: item.specsName}}
              </td>
              <td>
                {{$i18n.locale === "zh"? item.colorName: item.colorName}}
              </td>
              <td>{{ item.zaiKuCount+item.zaiTuCount }}</td>
              <td>{{ item.zaiKuCount }}</td>
              <td class="red">{{ item.zaiTuCount }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="btn-wrap">
        <!-- 库存拆分发货 -->
        <span class="red-btn cursor-pointer" @click="goConsigneeInfor(1)">{{$i18n.locale === "zh" ? "库存拆分发货" : "Split Shipment"}}</span>
        <!-- 到货统一发货 -->
        <span class="blue-btn cursor-pointer" v-if="Number(autoDelivery) !== 1" @click="goConsigneeInfor(0)">{{$i18n.locale === "zh" ? "到货统一发货" : "Combined Shipment"}}</span>
      </div>
    </div>
  </div>
</template>

<script>
import { MessageBox} from 'element-ui';
export default { 
  name: "chooseDelivery",
  props: ["deliveryData", "values",'orderhint'],
  data() {
    return {
      goodsType: 1,  // 拼团商品
      autoDelivery: "",
    };
  },
  created() {
    // 是否直发：1 是，0 否（直发不显示“到货统一发货”按钮）
    this.autoDelivery = localStorage.getItem("autoDelivery");
    
  },
  methods: {
    // 关闭弹窗
    handleClose() {
      this.$emit("handleClose");
    },
    // 跳转下单页面
    goConsigneeInfor(splitFlag) {
      if(this.orderhint!=''){
            MessageBox.confirm(this.orderhint, '提示', {
              confirmButtonText: '确定提交',
              cancelButtonText: '我再想想',
              customClass: 'orderHint'
            }).then(() => {
               this.$emit("handleClose");
               localStorage.setItem('shopOrderList',JSON.stringify(this.deliveryData));
               this.$router.push({name: 'ConsigneeInfor',query: {values: 1,onWaySplitFlag:splitFlag,isTwoWay:1, }});  //跳转下单页；
            })
      }else{
         localStorage.setItem('shopOrderList',JSON.stringify(this.deliveryData));
          // splitFlag：1, 拆；0, 不拆
          this.$router.push({
            name: "ConsigneeInfor",
            query: {
              values: this.values,
              onWaySplitFlag:splitFlag,
              isTwoWay:1,
            },
          });
      }
      
    },
  },
};
</script>

<style lang="less" scoped>
@import url("../../assets/less/variable.less");
.choose-wrap {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1001;
  .inner-content {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 1078px;
    padding: 40px 60px 30px;
    background-color: @white;
    border-radius: 8px;
    transform: translate(-50%, -50%);
    box-shadow: 0px 6px 17px 0px rgba(255, 255, 255, 0.5);
    box-sizing: border-box;
    .el-icon-close {
      position: absolute;
      top: 10px;
      right: 15px;
      font-size: 30px;
      color: @lighterGray;
      cursor: pointer;
      z-index: 111;
    }
    .tips {
      font-size: 14px;
      color: @lighterBlack;
      .red {
        color: @red;
      }
    }
    .table-wrap {
      margin-top: 20px;
      margin-bottom: 20px;
      max-height: 335px;
      overflow-y: auto;
    }
    table {
      table-layout: fixed;
      word-wrap: break-word;
      word-break: break-all;
      border-collapse: collapse;
      width: 100%;
      tr {
        &.goods-item:hover,
        &.goods-item:active {
          background-color: @lightGrayBg;
        }
        th {
          height: 35px;
          text-align: center;
          background-color: @lightGrayBg;
          font-size: 12px;
          color: @darkGray;
          font-weight: 400;
          word-wrap: break-word;
          word-break: break-word;
        }
        td {
          height: 60px;
          max-height: 60px;
          text-align: center;
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
          color: @lightBlack;
          font-size: 12px;
          border-top: 1px dashed @bdColor;
          &.red {
            color: @red;
          }
        }
      }
    }
    .btn-wrap {
      text-align: center;
      span {
        display: inline-block;
        padding: 0 28px;
        height: 40px;
        line-height: 40px;
        font-size: 14px;
        color: @white;
        border-radius: 4px;
        & + span {
          margin-left: 15px;
        }
      }
      .red-btn {
        text-align: center;
        background-color: @redLabel;
      }
      .blue-btn {
        text-align: center;
        background-color: @blueLabel;
      }
    }
  }
}
</style>