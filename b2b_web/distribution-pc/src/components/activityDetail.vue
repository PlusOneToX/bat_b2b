<template>
  <div>
    <el-dialog class="activityAlls" title="" :visible.sync="activityDialogVisible" :closeOnClickModal="noClick" :lockScroll="noClick">
<!--      <div class="rl-tc rl-padding-top-default rl-padding-bottom-default rl-text-bold">活动详情</div>-->
      <div class="column-right rl-bg-white">
        <div class="right-cons"  v-for="item in activityData" :key="item.id">
          <div class="rule-name rl-clear"><div class="rl-fl rl-text-xxss"><span v-if="item.activityType === 1">{{item.label}}</span><span v-else>{{item.policyName}}</span></div><div class="rl-fr rl-text-xxss"></div></div>
          <div class="rule-describe rl-padding-left-default rl-bg-gray-mm">
            <div class="name rl-margin-right-default" v-show="$i18n.locale === 'zh'">规则详情</div>
            <div class="name rl-margin-right-default" v-show="$i18n.locale === 'en'">detail of the activity</div>
            <div class="item">
              <div v-if="item.activityType === 1" v-for="(value,index) in item.conditions" :key="index">
                <span class="rl-margin-right-default">{{$t('ShopCarts.Condition')}}{{index + 1}}:</span>
                <span v-show="$i18n.locale === 'zh' || !value.labelEn == true">{{value.label}}</span>
                <span v-show="$i18n.locale === 'en'">{{value.labelEn}}</span>
              </div>
              <div v-if="item.activityType === 2">{{item.rule}}</div>
            </div>
          </div>
          <div class="user-table">
            <el-table :data="item.itemData" border max-height="300" header-row-class-name="header-row" class="activity-el-table rl-tc">
              <el-table-column :label="$t('ShopCarts.Picture')" :min-width="60">
                <template slot-scope="scope">
                  <div class="shop-img cursor-pointer" ><img width="100%" :src="scope.row.imageUrl1" alt=""></div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('ShopCarts.ItemNo')" prop="itemCode" :min-width="90"> </el-table-column>
              <el-table-column :label="$t('ShopCarts.ItemName')" :min-width="110">
                <template slot-scope="scope">
                  <div class="rl-text-xxs">
                    <span v-show="$i18n.locale === 'zh' || !scope.row.itemNameEn == true">{{scope.row.itemName}}</span>
                    <span v-show="$i18n.locale === 'en'">{{scope.row.itemNameEn}}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('ShopCarts.Spe')">
                <template slot-scope="scope">
                  <div class="rl-text-xxs">
                    <span v-show="$i18n.locale === 'zh' || !scope.row.specificationValueNameEn == true">{{scope.row.specificationValueName}}</span>
                    <span v-show="$i18n.locale === 'en'">{{scope.row.specificationValueNameEn}}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('ShopCarts.Colors')" width="55">
                <template slot-scope="scope">
                  <div class="rl-text-xxs">
                    <span v-show="$i18n.locale === 'zh' || !scope.row.colorValueNameEn == true">{{scope.row.colorValueName}}</span>
                    <span v-show="$i18n.locale === 'en'">{{scope.row.colorValueNameEn}}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('ShopCarts.MemPrice')" width="75">
                <template slot-scope="scope">
                  <i v-if="currencyType === 'CNY'">￥</i><i v-else>$</i>{{ scope.row.salePrice| keepTwoNum}}
                </template>
              </el-table-column>
              <el-table-column :label="$t('ShopCarts.DiscountPrice')" width="75">
                <template slot-scope="scope">
                  <i v-if="currencyType === 'CNY'">￥</i><i v-else>$</i>{{ scope.row.actualPrice| keepTwoNum}}
                </template>
              </el-table-column>
              <el-table-column :label="$t('ShopCarts.Quantity')" prop="itemCount" width="75"> </el-table-column>
              <el-table-column :label="$t('UserCenter.Remarkes')" width="75">
                <template slot-scope="scope">
                  <div v-if="item.isEnjoy === 1" class="rl-text-xxs">{{$t('ShopCarts.Enjoy')}}</div>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
<!--        <div>-->
<!--          <div class="rl-padding-top-xxs rl-padding-bottom-xxs">未参与活动商品</div>-->
<!--          <div class="user-table">-->
<!--            <el-table :data="withoutActivityItem" border max-height="300" header-row-class-name="header-row" class="activity-el-table rl-tc">-->
<!--              <el-table-column label="图片" :min-width="60">-->
<!--                <template slot-scope="scope">-->
<!--                  <div class="shop-img cursor-pointer" v-if="scope.row.itemImg !== null"><img width="100%" :src="scope.row.itemImg" alt=""></div>-->
<!--                </template>-->
<!--              </el-table-column>-->
<!--              <el-table-column label="货品编码" prop="itemCode" :min-width="90"> </el-table-column>-->
<!--              <el-table-column label="货品名称" prop="itemName" :min-width="110"> </el-table-column>-->
<!--              <el-table-column label="规格" prop="specificationValueName"> </el-table-column>-->
<!--              <el-table-column label="颜色" prop="colorValueName" width="50"> </el-table-column>-->
<!--              <el-table-column label="会员价格" prop="salePrice" width="70">-->
<!--                <template slot-scope="scope">-->
<!--                  <i class="asmd">￥</i>-->
<!--                  {{ scope.row.salePrice| keepTwoNum}}-->
<!--                </template>-->
<!--              </el-table-column>-->
<!--              <el-table-column label="数量" prop="totalNum" width="50"> </el-table-column>-->
<!--            </el-table>-->
<!--          </div>-->
<!--        </div>-->
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelGoOrder">{{$t('P.Cancel')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Vue from 'vue'
import {formatDate} from '@/assets/js/common.js'
import GD from '@/assets/js/globalData'
export default {
  name: 'activityDetail',
  props: {
    conditionIds: {
      type: Array
    },
    commonGoodsList: {
      type: Array
    },
    currencyType: {
      type: String
    },
    gradeDiscountId: {
      type: Number
    }
  },
  data () {
    return {
      activityDialogVisible: false,
      activityData: [],
      noClick: false,
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: 'zh-CNY' // 语种
    }
  },
  filters: {
    keepTwoNum (value) {
      value = Number(value)
      return value.toFixed(2)
    },
    formatDate (time) {
      var date = new Date(time)
      return formatDate(date, 'yyyy-MM-dd hh:mm:ss')
    }
  },
  methods: {
    fLangChange (value) {
      window.localStorage.setItem('bLang', value);
      this.$i18n.locale = value.split('_')[0];
    },
    getData () {
      this.$api.post(this, 'user/u/marketing/promotion/lookCondition', {conditionIds: this.conditionIds, gradeRuleId: this.gradeDiscountId}).then(res => {
        if (res.code === 0) {
          this.activityDialogVisible = true
          if (res.rules && res.rules.length > 0) {
            for (let i = 0; i < res.rules.length; i++) {
              Vue.set(res.rules[i], 'activityType', 1) // 普通活动
              Vue.set(res.rules[i], 'itemData', []) // 参与活动的货品
              for (let j = 0; j < res.rules[i].conditions.length; j++) {
                this.commonGoodsList.forEach(item => {
                  Vue.set(res.rules[i], 'isEnjoy', res.rules[i].isEnjoy) // 与订单是否同享
                  if (res.rules[i].conditions[j].id === item.orderPromotionId) {
                    res.rules[i].itemData.push(item)
                  }
                  if (res.rules[i].conditions[j].id === item.goodsPromotionId) {
                    res.rules[i].itemData.push(item)
                  }
                })
              }
              this.activityData.push(res.rules[i])
            }
            console.log(this.activityData)
          }
          if (res.gradeRule) {
            Vue.set(res.gradeRule, 'activityType', 2) // 等级折扣活动
            Vue.set(res.gradeRule, 'rule', '') // 等级折扣活动
            Vue.set(res.gradeRule, 'itemData', []) // 参与活动的货品
            if (res.gradeRule.discountBeforeAfter === 1) {
              if (res.gradeRule.moneyOrCount === 1) {
                res.gradeRule.rule = this.$t('Promotion.BeforeDiscount') + res.gradeRule.oneBuyMoney + this.$t('Promotion.ParticipateDiscount')
              } else {
                res.gradeRule.rule = this.$t('Promotion.QuantityReaches') + res.gradeRule.oneBuyCount + this.$t('Promotion.ParticipatePieces')
              }
            } else {
              if (res.gradeRule.moneyOrCount === 1) {
                res.gradeRule.rule =  this.$t('Promotion.AfterDiscount') + res.gradeRule.oneBuyMoney + this.$t('Promotion.ParticipateDiscount')
              } else {
                res.gradeRule.rule = this.$t('Promotion.QuantityReaches') + res.gradeRule.oneBuyCount + this.$t('Promotion.ParticipatePieces')
              }
            }
            this.commonGoodsList.forEach(item => {
              if (res.gradeRule.id === item.gradeDiscountId) {
                res.gradeRule.itemData.push(item)
              }
            })
            this.activityData.push(res.gradeRule)
            console.log(res.gradeRule)
          }
        }
      })
    },
    cancelGoOrder () {
      this.activityDialogVisible = false
      this.$emit('shutDialog', true)
    }
  },
  created: function () {
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem('bLang') ? window.localStorage.getItem('bLang') : 'zh-CNY';
    this.getData()
  }
}
</script>

<style lang='less'>
  .activityAlls{
    overflow: visible;
    top:-12%;
    .el-dialog{
      width: 900px!important;
      max-height: 900px;
      overflow-y: scroll;
      .el-dialog__header{
        display: none;
      }
      .el-dialog__body{
        padding: 0;
        padding-left: 20px;
        padding-right: 20px;
        padding-bottom: 10px;
      }
      .el-dialog__footer{
        text-align: center;
      }
    }
  }
</style>
<style  scoped lang='less'>
  .asmd {
    color:#ffa800;
    font-weight: bolder;
  }
  .right-cons {
    .rule-name {
      width: 100%;
      padding-top: 10px;
      padding-bottom: 10px;
    }
    .rule-describe {
      padding-top: 10px;
      padding-bottom: 10px;
      display: flex;
      height: 100%;
      align-items: center;
      .name {
        display: flex;
        align-items: center;
        justify-content: center;
        min-width: 58px;
      }
      .item {
        .chenked {
          width: 19px;
          height: 19px;
          display: inline-block;
          vertical-align: -5px;
          -webkit-appearance: none;
          appearance: none;
          outline: none;
          margin-left: 15px;
          font-size: 14px;
          color: #333;
          background: url(../../src/assets/images/selected.png) no-repeat center center;
        }
      }
    }

    .user-table {
      .group {
        display: flex;

        div {
          width: 120px;
          height: 30px;
          line-height: 30px;
          text-align: center;
          font-size: 13px;
        }

        div.active {
          background-color: #00c9dc;
          color: #fff;
        }
      }

      table {
        width: 840px;
        word-wrap: break-word;
        word-break: break-all;
        border-collapse: collapse;

        tr {
          th {
            height: 45px;
            line-height: 45px;
          }

          td {
            height: 80px;
            line-height: 80px;
            font-size: 12px;
            text-align: center;

            .delet {
              display: inline-block;
              width: 68px;
              height: 35px;
              line-height: 35px;
              border-radius: 5px;

              .el-button--text {
                color: #fff;
                width: 68px;
                height: 35px;
              }
            }
          }
        }
      }
    }

    .total {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }
  }
  .shop-img{
    margin: auto;
    margin-top: 5px;
    width: 55px;
    img{
      width: 100%;
      height: 55px;
    }
  }
</style>
