<template>
    <div>
      <el-table :data="this.shopCartSonData" :show-header="showHeader" border header-row-class-name="header-row" class="activity-el-table rl-tc">
        <el-table-column label="" width="50">
          <template slot-scope="scope">
            <div v-if="scope.row.itemType === 2"><div class="song"><h1 class="rl-bg-orange-mm rl-text-white rl-text-xxss rl-margin-zero rl-tc">赠品</h1></div></div>
            <div v-else>
              <div v-if="scope.row.gou === 2" class="song"><h1 class="rl-bg-gray-sm rl-text-white rl-text-xxss rl-margin-zero rl-tc">{{$t('ShopCarts.Expired')}}</h1></div>
              <div v-else @click="chooseGou(scope.row)" class="spe"><span class="cursor-pointer" :class ="{'gou':scope.row.gou === 1}"></span></div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="图片" :min-width="60">
          <template slot-scope="scope">
            <div @click="goDoodsDetail(scope.row.goodsId)" class="shop-img cursor-pointer"><img width="100%" :src="scope.row.imageUrl1" alt=""></div>
          </template>
        </el-table-column>
        <el-table-column label="货品编码" prop="itemCode" :min-width="90"> </el-table-column>
        <el-table-column label="货品名称" :min-width="130">
          <template slot-scope="scope">
            <div class="rl-text-xxs" v-show="$i18n.locale === 'zh' || !scope.row.itemNameEn == true">{{scope.row.itemName}}</div>
            <div class="rl-text-xxs" v-show="$i18n.locale === 'en'">{{scope.row.itemNameEn}}</div>
          </template>
        </el-table-column>
        <el-table-column label="规格" width="90">
          <template slot-scope="scope">
            <div class="rl-text-xxs" v-show="$i18n.locale === 'zh' || !scope.row.specificationValueNameEn == true">{{scope.row.specificationValueName}}</div>
            <div class="rl-text-xxs" v-show="$i18n.locale === 'en'">{{scope.row.specificationValueNameEn}}</div>
          </template>
        </el-table-column>
        <el-table-column label="颜色" width="70">
          <template slot-scope="scope">
            <div class="rl-text-xxs" v-show="$i18n.locale === 'zh' || !scope.row.colorValueNameEn == true">{{scope.row.colorValueName}}</div>
            <div class="rl-text-xxs" v-show="$i18n.locale === 'en'">{{scope.row.colorValueNameEn}}</div>
          </template>
        </el-table-column>
        <el-table-column label="会员价" prop="salePrice" width="80">
          <template slot-scope="scope">
            <i v-show="$root.currency === 'CNY'" class="asmd">￥{{ scope.row.salePrice| keepTwoNum}}</i>
            <i v-show="$root.currency === 'USD'" class="asmd">${{ scope.row.salePriceEn| keepTwoNum}}</i>
          </template>
        </el-table-column>
<!--        <el-table-column label="优惠价" prop="salePrice" width="80">-->
<!--          <template slot-scope="scope">-->
<!--            <i class="asmd">￥</i>-->
<!--            {{ scope.row.salePrice| keepTwoNum}}-->
<!--          </template>-->
<!--        </el-table-column>-->
        <el-table-column label="数量" :min-width="100">
          <template slot-scope="scope">
            <div class="app-flex app-justify-content-space-between rl-padding-left-default rl-padding-right-default" v-if="ruleData && scope.row.orderType === 1">
              <div class="rl-text-xxs">{{scope.row.showNumInWarehouse}}</div><div class="rl-text-xxs">/</div><div class="rl-text-xxs">{{scope.row.showStockItemCount}}</div>
            </div>
            <div @click="lookDiyItem(scope.row,scope.row.diyList)" v-if="scope.row.diyList">
              <el-input-number v-model="scope.row.num" :min="1"  label="描述文字" size="mini"></el-input-number>
              <span class="diy-gai"></span>
            </div>
            <div v-else-if="ruleData"><el-input-number @change="handleChange($event,scope.row)" v-model="scope.row.num" :min="1"  label="描述文字" size="mini" ></el-input-number></div>
            <div v-else><el-input-number @change="handleChange($event,scope.row)" v-model="scope.row.num" :min="1"  label="描述文字" size="mini" disabled></el-input-number></div>
          </template>
        </el-table-column>
        <el-table-column label="合计" width="90">
          <template slot-scope="scope">
            <div v-show="$root.currency === 'CNY'" class="rl-text-xxs">￥{{scope.row.salePrice*scope.row.num | keepTwoNum}}</div>
            <div v-show="$root.currency === 'USD'" class="rl-text-xxs">${{scope.row.salePriceEn*scope.row.num | keepTwoNum}}</div>
          </template>
        </el-table-column>
        <el-table-column label="操作" :min-width="90">
          <template slot-scope="scope">
            <div v-if="scope.row.diyList && (scope.row.rules.length > 0 || scope.row.gradeRule !== null)">
              <el-button v-if="(scope.row.rules.length > 0 || scope.row.gradeRule !== null) && scope.row.itemType !== 2" @click.native.prevent="changeActivity(scope.row)" class="mini-search-btn-other">{{$t('ShopCarts.Switch')}}</el-button>
              <el-button v-if="scope.row.diyList" @click.native.prevent="lookDiyImage(scope.row.diyList)" class="mini-search-btn-other">{{$t('ShopCarts.View')}}</el-button>
              <el-button @click.native.prevent="deleteShopCarItems(scope.row.id, scope.row, scope.$index)" class="mini-search-btn-other">{{$t('ShopCarts.Delete')}}</el-button>
            </div>
            <div v-else>
              <el-button v-if="(scope.row.rules.length > 0 || scope.row.gradeRule !== null) && scope.row.itemType !== 2" @click.native.prevent="changeActivity(scope.row)" class="mini-search-btn">{{$t('ShopCarts.Switch')}}</el-button>
              <el-button v-if="scope.row.diyList" @click.native.prevent="lookDiyImage(scope.row.diyList)" class="mini-search-btn">{{$t('ShopCarts.View')}}</el-button>
              <el-button @click.native.prevent="deleteShopCarItems(scope.row.id, scope.row, scope.$index)" class="mini-search-btn">{{$t('ShopCarts.Delete')}}</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <!--切换活动-->
      <el-dialog class="alls" :title="$t('ShopCarts.SwitchSum')" :visible.sync="changeDialogVisible">
        <div class="change-activity rl-bdt-gray-sm max-height300 rl-padding-left-default rl-padding-right-default rl-clear">
          <div @click="changeActivityLabel(item)" class="item app-flex" v-for="item in changeActivityData" :key="item.id">
            <div class="app-flex app-align-items-flex-center app-justify-content-space-between"><span class="chenked" :class="{'active': item.checked === true}"></span></div>
            <div class="cons">
              <div v-if="item.ruleType === 1"><el-button class="cons-btn" >{{item.label}}</el-button></div>
              <div v-else><el-button class="cons-btn">{{item.policyName}}</el-button></div>
              <div v-if="item.ruleType === 1" class="rule" v-for="(value,cindex) in item.ruleConditions" :key="cindex">条件{{cindex + 1}}:{{value.label}}</div>
              <div v-if="item.ruleType === 2" class="rule">{{item.ruleConditions}}</div>
            </div>
          </div>
        </div>
        <div slot="footer" class="dialog-footer rl-margin-top-xxxs rl-tc">
          <el-button @click="changeDialogVisible = false">{{$t('P.Cancel')}}</el-button>
          <el-button @click="confirmChangeActivity" type="primary">{{$t('P.OK')}}</el-button>
        </div>
      </el-dialog>
      <!--查看diy图片-->
      <el-dialog class="diy-all" title="" :visible.sync="diyDialogVisible">
        <div class="diy-css rl-padding-top-default rl-padding-left-double rl-padding-left-double rl-clear">
          <div class="banner rl-fl">
            <el-carousel trigger="click" :loop="falseState" :autoplay="falseState"  height="414px" arrow="always" indicator-position="none">
              <el-carousel-item v-for="item in diyImages" :key="item.id">
                <div class="banner-img cursor-pointer"><img :src="item.diyPic + '?x-oss-process=image/resize,h_200,l_400'" alt=""></div>
                <div class="rl-tc rl-text-base sum">x{{item.diyNum}}</div>
              </el-carousel-item>
            </el-carousel>
          </div>
        </div>
        <div slot="footer" class="dialog-footer rl-tc">
          <el-button @click="diyDialogVisible = false">{{$t('P.Cancel')}}</el-button>
        </div>
      </el-dialog>
      <!--删除diy货品-->
      <el-dialog class="alls" :title="$t('ShopCarts.DeleteItem')" :visible.sync="deleteDiyDialogVisible">
        <div class="diy-css rl-bdt-gray-sm max-height300 rl-padding-left-default rl-padding-right-default rl-clear">
          <table>
            <thead>
            <tr>
              <th class="rl-padding-top-xxs rl-padding-bottom-xxs diy-gou"></th>
              <th class="rl-padding-top-xxs rl-padding-bottom-xxs">{{$t('ShopCarts.Picture')}}</th>
              <th class="rl-padding-top-xxs rl-padding-bottom-xxs">{{$t('ShopCarts.Quantity')}}</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="item in deleteDiyItem" :key="item.id">
              <td width="100px" class="diy-gou"><span class="cursor-pointer" :class ="{'gou':item.gou === 1}" @click="chooseDiyGou(item,item.gou)"></span></td>
              <td width="250px" class="rl-tc"><div class="diy-delete-Img rl-margin-zero"><img :src="item.diyPic" alt=""></div></td>
              <td width="250px" class="rl-tc">x{{item.diyNum}}</td>
            </tr>
            </tbody>
          </table>
        </div>
        <div slot="footer" class="dialog-footer rl-margin-top-xxxs">
          <div class="rl-fl dialog-footer-div"><span class="cursor-pointer" :class="{'gou':diyAll === true}" @click="selectDiyAll">{{$t('ShopCarts.SelectAll')}}</span></div>
          <el-button @click="deleteDiyDialogVisible = false">{{$t('P.Cancel')}}</el-button>
          <el-button @click="confirmDeleteDiyItem" type="danger">{{$t('P.OK')}}</el-button>
        </div>
      </el-dialog>
      <!--查看diy货品-->
      <el-dialog class="alls" :title="$t('ShopCarts.Ordered')" :visible.sync="lookDiyDialogVisible">
        <div class="diy-css rl-bdt-gray-sm max-height300 rl-padding-left-default rl-padding-right-default rl-clear">
          <table>
            <thead>
            <tr>
              <th class="rl-padding-top-xxs rl-padding-bottom-xxs">{{$t('ShopCarts.Picture')}}</th>
              <th class="rl-padding-top-xxs rl-padding-bottom-xxs">{{$t('ShopCarts.Quantity')}}</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="item in adjustDiyItem" :key="item.id">
              <td width="300px" class="rl-tc"><div class="diy-delete-Img rl-margin-zero"><img :src="item.diyPic" alt=""></div></td>
              <td width="300px" class="rl-tc"><el-input-number @change="handleChangeDiy($event,item)" v-model="item.diyNum" :min="0"  label="描述文字" size="mini" ></el-input-number></td>
            </tr>
            </tbody>
          </table>
        </div>
        <div slot="footer" class="dialog-footer rl-margin-top-xxxs rl-tc">
          <el-button @click="lookDiyDialogVisible = false">{{$t('P.Cancel')}}</el-button>
          <el-button @click="confirmAdjustDiyItem" type="primary">{{$t('P.OK')}}</el-button>
        </div>
      </el-dialog>
    </div>
</template>

<script>
import Vue from 'vue'
import {} from '@/assets/js/common.js'
import GD from '@/assets/js/globalData'
export default {
  name: 'sonComponent',
  props: {
    shopCartSonData: {
      type: Array
    },
    ruleData: {
      type: Object
    },
    orderType: {
      type: Number
    }
  },
  data () {
    return {
      showHeader: false,
      changeDialogVisible: false,
      falseState: false,
      diyDialogVisible: false,
      deleteDiyDialogVisible: false,
      lookDiyDialogVisible: false,
      diyAll: true,
      diyImages: [],
      deleteDiyId: '',
      adjustDiyCarId: '',
      deleteDiyItem: [],
      adjustDiyItem: [],
      changeActivityData: [],
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: 'zh-RMB' // 语种
    }
  },
  filters: {
    keepTwoNum (value) {
      value = Number(value)
      return value.toFixed(2)
    },
    keepTwoHead (value) {
      return value.substr(0, 2)
    },
    deleteFivHead (value) {
      return value.slice(5)
    }
  },
  methods: {
    fLangChange (value) {
      window.localStorage.setItem('bLang', value);
      this.$i18n.locale = value.split('-')[0];
      this.$i18n.curren = value.slice(3, 6);
    },
    chooseGou (item) {
      // console.log(this.ruleData)
      // console.log(this.shopCartSonData)
      // console.log(item)
      if (item.gou === 0) {
        item.gou = 1
        if (this.ruleData.policyId) { // 折扣活动
          this.ruleData.totalCount = this.ruleData.totalCount + item.num
          this.ruleData.totalMoney = this.ruleData.totalMoney + (item.salePrice * item.num)
          this.ruleData.meetMark = false
          if (this.ruleData.discountBeforeAfter === 1) { // 折扣前
            if ((this.ruleData.moneyOrCount === 1 && this.ruleData.totalMoney > this.ruleData.oneBuyMoney) || (this.ruleData.moneyOrCount === 2 && this.ruleData.totalCount > this.ruleData.oneBuyCount)) {
              this.ruleData.meetMark = true
            }
          } else {
            if ((this.ruleData.moneyOrCount === 1 && this.ruleData.totalMoney > this.ruleData.oneBuyMoney) || (this.ruleData.moneyOrCount === 2 && this.ruleData.totalCount > this.ruleData.oneBuyCount)) {
              this.ruleData.meetMark = true
            }
          }
        } else {
          if (this.ruleData.isAddUp === 0) { // 活动商品是否累计，1是 0否
            let maxNum = 0
            let maxPrice = 0
            let x = 0
            for (var i = 0; i < this.shopCartSonData.length; i++) {
              if (this.shopCartSonData[i].gou === 1) {
                x = i + 1
                maxNum = this.shopCartSonData[i].num
                maxPrice = this.shopCartSonData[i].num * this.shopCartSonData[i].salePrice
                break
              }
            }
            for (var n = x; n < this.shopCartSonData.length; n++) {
              if (this.shopCartSonData[n].gou === 1 && this.shopCartSonData[n].num > maxNum) {
                maxNum = this.shopCartSonData[n].num
                maxPrice = this.shopCartSonData[n].num * this.shopCartSonData[n].salePrice
              }
            }
            this.ruleData.totalCount = maxNum
            this.ruleData.totalMoney = maxPrice
          } else {
            this.ruleData.totalCount = this.ruleData.totalCount + item.num
            this.ruleData.totalMoney = this.ruleData.totalMoney + (item.salePrice * item.num)
          }
          let ruleCondition = null // 判断满足条件选择最优
          for (let i = 0; i < this.ruleData.ruleConditions.length; i++) {
            this.ruleData.ruleConditions[i].meetMark = false
            if (this.ruleData.ruleConditions[i].groupConditions.length > 0) { // 有组合活动
              this.ruleData.ruleConditions[i].ruleConditionsCount = 0 // 组合计算总数
              this.ruleData.ruleConditions[i].ruleConditionsMoney = 0 // 组合计算总金额
              this.ruleData.ruleConditions[i].groupConditions.forEach((obj) => {
                let temp = []
                obj.groupTotalCount = 0
                obj.groupTotalMoney = 0
                temp = obj.goodsItemIds.split(',')
                for (let m = 0; m < temp.length; m++) {
                  for (let j = 0; j < this.ruleData.items.length; j++) {
                    if (Number(temp[m]) === Number(this.ruleData.items[j].itemId) && this.ruleData.items[j].gou === 1) {
                      obj.groupTotalCount += this.ruleData.items[j].num
                      obj.groupTotalMoney += this.ruleData.items[j].num * this.ruleData.items[j].salePrice
                    }
                  }
                }
                if (this.ruleData.moneyOrCount === 1) { // 一次性购买按金额还是数量 1金额 2数量
                  this.ruleData.ruleConditions[i].ruleConditionsMoney += obj.oneBuyMoney
                  if (obj.groupTotalMoney >= obj.oneBuyMoney) {
                    obj.meetType = true
                  } else {
                    obj.meetType = false
                  }
                } else if (this.ruleData.moneyOrCount === 2) {
                  this.ruleData.ruleConditions[i].ruleConditionsCount += obj.oneBuyCount
                  if (obj.groupTotalCount >= obj.oneBuyCount) {
                    obj.meetType = true
                  } else {
                    obj.meetType = false
                  }
                }
              })
              let b = true
              for (let n = 0; n < this.ruleData.ruleConditions[i].groupConditions.length; n++) {
                if (this.ruleData.ruleConditions[i].groupConditions[n].meetType === false) {
                  b = false
                  break
                }
              }
              if (b) {
                if (ruleCondition === null ||
                  (ruleCondition !== null && ((this.ruleData.moneyOrCount === 2 && ruleCondition.ruleConditionsCount < this.ruleData.ruleConditions[i].ruleConditionsCount) || (this.ruleData.moneyOrCount === 1 && ruleCondition.ruleConditionsMoney < this.ruleData.ruleConditions[i].ruleConditionsMoney)))) {
                  ruleCondition = this.ruleData.ruleConditions[i]
                }
              }
            } else {
              if ((this.ruleData.moneyOrCount === 1 && this.ruleData.totalMoney >= this.ruleData.ruleConditions[i].oneBuyMoney) || (this.ruleData.moneyOrCount === 2 && this.ruleData.totalCount >= this.ruleData.ruleConditions[i].oneBuyCount)) { // 一次性购买按金额还是数量 1金额 2数量
                if (ruleCondition === null) {
                  ruleCondition = this.ruleData.ruleConditions[i]
                } else if (ruleCondition !== null && ((this.ruleData.moneyOrCount === 1 && this.ruleData.ruleConditions[i].oneBuyMoney > ruleCondition.oneBuyMoney) || (this.ruleData.moneyOrCount === 2 && this.ruleData.ruleConditions[i].oneBuyCount > ruleCondition.oneBuyCount))) {
                  ruleCondition = this.ruleData.ruleConditions[i]
                }
              }
            }
          }
          if (ruleCondition !== null) {
            ruleCondition.meetMark = true
          }
        }
      } else if (item.gou === 1) {
        item.gou = 0
        if (this.ruleData.policyId) { // 折扣活动
          this.ruleData.totalCount = this.ruleData.totalCount - item.num
          this.ruleData.totalMoney = this.ruleData.totalMoney - (item.salePrice * item.num)
          this.ruleData.meetMark = false
          if (this.ruleData.discountBeforeAfter === 1) { // 折扣前
            if ((this.ruleData.moneyOrCount === 1 && this.ruleData.totalMoney > this.ruleData.oneBuyMoney) || (this.ruleData.moneyOrCount === 2 && this.ruleData.totalCount > this.ruleData.oneBuyCount)) {
              this.ruleData.meetMark = true
            }
          } else {
            if ((this.ruleData.moneyOrCount === 1 && this.ruleData.totalMoney > this.ruleData.oneBuyMoney) || (this.ruleData.moneyOrCount === 2 && this.ruleData.totalCount > this.ruleData.oneBuyCount)) {
              this.ruleData.meetMark = true
            }
          }
        } else {
          if (this.ruleData.isAddUp === 0) { // 活动商品是否累计，1是 0否
            let maxNum = 0
            let maxPrice = 0
            let x = 0
            for (var a = 0; a < this.shopCartSonData.length; a++) {
              if (this.shopCartSonData[a].gou === 1) {
                x = a + 1
                maxNum = this.shopCartSonData[a].num
                maxPrice = this.shopCartSonData[a].num * this.shopCartSonData[a].salePrice
                break
              }
            }
            for (var b = x; b < this.shopCartSonData.length; b++) {
              if (this.shopCartSonData[b].gou === 1 && this.shopCartSonData[b].num > maxNum) {
                maxNum = this.shopCartSonData[b].num
                maxPrice = this.shopCartSonData[b].num * this.shopCartSonData[b].salePrice
              }
            }
            this.ruleData.totalCount = maxNum
            this.ruleData.totalMoney = maxPrice
          } else {
            this.ruleData.totalCount = this.ruleData.totalCount - item.num
            this.ruleData.totalMoney = this.ruleData.totalMoney - (item.salePrice * item.num)
          }
          let ruleCondition = null // 判断满足条件选择最优
          for (let i = 0; i < this.ruleData.ruleConditions.length; i++) {
            this.ruleData.ruleConditions[i].meetMark = false
            if (this.ruleData.ruleConditions[i].groupConditions.length > 0) { // 有组合活动
              this.ruleData.ruleConditions[i].ruleConditionsCount = 0 // 组合计算总数
              this.ruleData.ruleConditions[i].ruleConditionsMoney = 0 // 组合计算总金额
              this.ruleData.ruleConditions[i].groupConditions.forEach((obj) => {
                let temp = []
                obj.groupTotalCount = 0
                obj.groupTotalMoney = 0
                temp = obj.goodsItemIds.split(',')
                for (let m = 0; m < temp.length; m++) {
                  for (let j = 0; j < this.ruleData.items.length; j++) {
                    if (Number(temp[m]) === Number(this.ruleData.items[j].itemId) && this.ruleData.items[j].gou === 1) {
                      obj.groupTotalCount += this.ruleData.items[j].num
                      obj.groupTotalMoney += this.ruleData.items[j].num * this.ruleData.items[j].salePrice
                    }
                  }
                }
                if (this.ruleData.moneyOrCount === 1) { // 一次性购买按金额还是数量 1金额 2数量
                  this.ruleData.ruleConditions[i].ruleConditionsMoney += obj.oneBuyMoney
                  if (obj.groupTotalMoney >= obj.oneBuyMoney) {
                    obj.meetType = true
                  } else {
                    obj.meetType = false
                  }
                } else if (this.ruleData.moneyOrCount === 2) {
                  this.ruleData.ruleConditions[i].ruleConditionsCount += obj.oneBuyCount
                  if (obj.groupTotalCount >= obj.oneBuyCount) {
                    obj.meetType = true
                  } else {
                    obj.meetType = false
                  }
                }
              })
              let b = true
              for (let n = 0; n < this.ruleData.ruleConditions[i].groupConditions.length; n++) {
                if (this.ruleData.ruleConditions[i].groupConditions[n].meetType === false) {
                  b = false
                  break
                }
              }
              if (b) {
                if (ruleCondition === null ||
                  (ruleCondition !== null && ((this.ruleData.moneyOrCount === 2 && ruleCondition.ruleConditionsCount < this.ruleData.ruleConditions[i].ruleConditionsCount) || (this.ruleData.moneyOrCount === 1 && ruleCondition.ruleConditionsMoney < this.ruleData.ruleConditions[i].ruleConditionsMoney)))) {
                  ruleCondition = this.ruleData.ruleConditions[i]
                }
              }
            } else {
              if ((this.ruleData.moneyOrCount === 1 && this.ruleData.totalMoney >= this.ruleData.ruleConditions[i].oneBuyMoney) || (this.ruleData.moneyOrCount === 2 && this.ruleData.totalCount >= this.ruleData.ruleConditions[i].oneBuyCount)) { // 一次性购买按金额还是数量 1金额 2数量
                if (ruleCondition === null) {
                  ruleCondition = this.ruleData.ruleConditions[i]
                } else if (ruleCondition !== null && ((this.ruleData.moneyOrCount === 1 && this.ruleData.ruleConditions[i].oneBuyMoney > ruleCondition.oneBuyMoney) || (this.ruleData.moneyOrCount === 2 && this.ruleData.ruleConditions[i].oneBuyCount > ruleCondition.oneBuyCount))) {
                  ruleCondition = this.ruleData.ruleConditions[i]
                }
              }
            }
          }
          if (ruleCondition !== null) {
            ruleCondition.meetMark = true
          } else {
            this.$api.delete(this, 'user/u/shoppingCart/delPresent', {ruleId: this.ruleData.id}).then(res => {
              if (res.code === 0) { this.ruleData.presentItems = [] }  // 清空赠品
            })
          }
        }
      }
    },
    handleChangeDiy (val, item) {
      this.$nextTick(() => {
        if (val === 0) {
          item.diyNum = 1
          if (this.$i18n.locale === 'zh') {
            this.$message.warning('货品订购数量不能为空!')
          } else { this.$message.warning('The order quantity of products cannot be null.') }
        }
      })
    },
    handleChange (val, item) { // 改变货品数量
      if (val === undefined) {
        this.$nextTick(() => {
          item.num = 1
          this.$api.put(this, 'user/u/shoppingCart', {id: item.id, num: item.num}).then(res => {
            if (res.code === 0) {
              item.showNumInWarehouse = 1
              item.showStockItemCount = 0
            } 
          })
        })
      }
      if (item.itemType === 1) { // 普通商品
        if (item.stockItemCount === 0) {
          if (this.$i18n.locale === 'zh') {
            this.$message.warning('货品订购数量不能为空!')
          } else { this.$message.warning('The order quantity of products cannot be null.') }
          this.$nextTick(() => { item.num = 0 })
          this.$api.put(this, 'user/u/shoppingCart', {id: item.id, num: item.num}).then(res => {
            if (res.code === 0) {
              item.showNumInWarehouse = 0
              item.showStockItemCount = 0
            } 
          })
          return false
        }
        if (item.num > item.stockItemCount) {
          if (this.$i18n.locale === 'zh') {
            this.$message.warning('购买数量不能大于库存!')
          } else { this.$message.warning('The order quantity of products shall not be greater than inventory.') }
          this.$nextTick(() => { item.num = item.stockItemCount })
          this.$api.put(this, 'user/u/shoppingCart', {id: item.id, num: item.num}).then(res => {
            if (res.code === 0) {
              item.showNumInWarehouse = item.numInWarehouse
              item.showStockItemCount = item.num - item.numInWarehouse
              if (item.gou === 1) {
                this.changeMark(item.itemType)
              }
            }
          })
        } else {
          this.$api.put(this, 'user/u/shoppingCart', {id: item.id, num: item.num}).then(res => {
            if (res.code === 0) {
              if (item.num > item.numInWarehouse) { // 当购买数量大于在库数量
                item.showNumInWarehouse = item.numInWarehouse
                item.showStockItemCount = item.num - item.numInWarehouse
              } else {
                item.showNumInWarehouse = item.num
                item.showStockItemCount = 0
              }
              if (item.gou === 1) {
                this.changeMark(item.itemType)
              }
            } 
          })
        }
      } else { // 定制商品
        this.$api.put(this, 'user/u/shoppingCart', {id: item.id, num: item.num}).then(res => {
          if (res.code === 0) {
            if (item.gou === 1) {
              this.changeMark(item.itemType)
            }
          }
        })
      }
    },
    changeMark (itemType) {
      this.ruleData.totalCount = 0
      this.ruleData.totalMoney = 0
      let maxNum = 0
      let maxPrice = 0
      if (this.ruleData.policyId) { // 折扣活动
        this.ruleData.items.forEach((item) => {
          if (itemType === 1) { // 普通商品
            if (this.ruleData.onWayFlag === 0) { // 在途商品是否参与活动 0不参与 1参与
              this.ruleData.totalCount += item.showNumInWarehouse
              this.ruleData.totalMoney += item.showNumInWarehouse * item.salePrice
            } else {
              this.ruleData.totalCount += item.num
              this.ruleData.totalMoney += item.num * item.salePrice
            }
          } else {
            this.ruleData.totalCount += item.num
            this.ruleData.totalMoney += item.num * item.salePrice
          }
        })
        this.ruleData.meetMark = false
        if (this.ruleData.discountBeforeAfter === 1) { // 折扣前
          if ((this.ruleData.moneyOrCount === 1 && this.ruleData.totalMoney > this.ruleData.oneBuyMoney) || (this.ruleData.moneyOrCount === 2 && this.ruleData.totalCount > this.ruleData.oneBuyCount)) {
            this.ruleData.meetMark = true
          }
        } else {
          if ((this.ruleData.moneyOrCount === 1 && this.ruleData.totalMoney > this.ruleData.oneBuyMoney) || (this.ruleData.moneyOrCount === 2 && this.ruleData.totalCount > this.ruleData.oneBuyCount)) {
            this.ruleData.meetMark = true
          }
        }
      } else {
        if (this.ruleData.isAddUp === 0) { // 活动商品是否累计，1是 0否
          if (itemType === 1) { // 普通商品
            if (this.ruleData.onWayFlag === 1) { // 在途参与活动
              maxNum = this.ruleData.items[0].num;
              maxPrice = this.ruleData.items[0].num * this.ruleData.items[0].salePrice;
              for (var i = 1; i < this.ruleData.items.length; i++) {
                if (this.ruleData.items[i].num > maxNum) {
                  maxNum = this.ruleData.items[i].num
                  maxPrice = this.ruleData.items[i].num * this.ruleData.items[i].salePrice
                }
              }
            } else {
              maxNum = this.ruleData.items[0].showNumInWarehouse;
              maxPrice = this.ruleData.items[0].showNumInWarehouse * this.ruleData.items[0].salePrice;
              for (var j = 1; j < this.ruleData.items.length; j++) {
                if (this.ruleData.items[j].showNumInWarehouse > maxNum) {
                  maxNum = this.ruleData.items[j].showNumInWarehouse
                  maxPrice = this.ruleData.items[j].showNumInWarehouse * this.ruleData.items[j].salePrice
                }
              }
            }
          } else { // 定制商品
            maxNum = this.ruleData.items[0].num;
            maxPrice = this.ruleData.items[0].num * this.ruleData.items[0].salePrice;
            for (var l = 1; l < this.ruleData.items.length; l++) {
              if (this.ruleData.items[l].num > maxNum) {
                maxNum = this.ruleData.items[l].num
                maxPrice = this.ruleData.items[l].num * this.ruleData.items[l].salePrice
              }
            }
          }
          this.ruleData.totalCount = maxNum
          this.ruleData.totalMoney = maxPrice
        } else {
          this.ruleData.items.forEach((item) => {
            if (itemType === 1) { // 普通商品
              if (this.ruleData.onWayFlag === 0) { // 在途商品是否参与活动 0不参与 1参与
                this.ruleData.totalCount += item.showNumInWarehouse
                this.ruleData.totalMoney += item.showNumInWarehouse * item.salePrice
              } else {
                this.ruleData.totalCount += item.num
                this.ruleData.totalMoney += item.num * item.salePrice
              }
            } else {
              this.ruleData.totalCount += item.num
              this.ruleData.totalMoney += item.num * item.salePrice
            }
          })
        }
        let ruleCondition = null // 判断满足条件选择最优
        this.ruleData.ruleConditions.forEach((item) => {
          item.meetMark = false
          if (item.groupConditions.length > 0) { // 有组合活动
            item.ruleConditionsCount = 0
            item.ruleConditionsMoney = 0
            item.groupConditions.forEach((obj) => {
              let temp = []
              obj.groupTotalCount = 0
              obj.groupTotalMoney = 0
              temp = obj.goodsItemIds.split(',')
              for (let i = 0; i < temp.length; i++) {
                for (let j = 0; j < this.ruleData.items.length; j++) {
                  if (Number(temp[i]) === Number(this.ruleData.items[j].itemId) && this.ruleData.items[j].gou === 1) {
                    obj.groupTotalCount += this.ruleData.items[j].num
                    obj.groupTotalMoney += this.ruleData.items[j].num * this.ruleData.items[j].salePrice
                  }
                }
              }
              if (this.ruleData.moneyOrCount === 1) { // 一次性购买按金额还是数量 1金额 2数量
                item.ruleConditionsMoney += obj.oneBuyMoney
                if (obj.groupTotalMoney >= obj.oneBuyMoney) {
                  obj.meetType = true
                } else {
                  obj.meetType = false
                }
              } else if (this.ruleData.moneyOrCount === 2) {
                item.ruleConditionsCount += obj.oneBuyCount
                if (obj.groupTotalCount >= obj.oneBuyCount) {
                  obj.meetType = true
                } else {
                  obj.meetType = false
                }
              }
            })
            let b = true
            for (let n = 0; n < item.groupConditions.length; n++) {
              if (item.groupConditions[n].meetType === false) {
                b = false
                break
              }
            }
            if (b) {
              if (ruleCondition === null ||
                (ruleCondition !== null && ((this.ruleData.moneyOrCount === 2 && ruleCondition.ruleConditionsCount < item.ruleConditionsCount) || (this.ruleData.moneyOrCount === 1 && ruleCondition.ruleConditionsMoney < item.ruleConditionsMoney)))) {
                ruleCondition = item
              }
            }
          } else {
            if ((this.ruleData.moneyOrCount === 1 && this.ruleData.totalMoney >= item.oneBuyMoney) || (this.ruleData.moneyOrCount === 2 && this.ruleData.totalCount >= item.oneBuyCount)) { // 一次性购买按金额还是数量 1金额 2数量
              if (ruleCondition === null) {
                ruleCondition = item
              } else if (ruleCondition !== null && ((this.ruleData.moneyOrCount === 1 && item.oneBuyMoney > ruleCondition.oneBuyMoney) || (this.ruleData.moneyOrCount === 2 && item.oneBuyCount > ruleCondition.oneBuyCount))) {
                ruleCondition = item
              }
            }
          }
        })
        if (ruleCondition !== null) {
          ruleCondition.meetMark = true
        } else {
          this.$api.delete(this, 'user/u/shoppingCart/delPresent', {ruleId: this.ruleData.id}).then(res => {
            if (res.code === 0) { this.ruleData.presentItems = [] }  // 清空赠品
          })
        }
      }
    },
    changeActivity (item) { // 切换活动
      this.changeDialogVisible = true
      this.changeActivityData = []
      this.$nextTick(() => {
        item.rules.forEach((val) => {
          Vue.set(val, 'ruleType', 1) // 1活动规则 2等级折扣
          this.changeActivityData.push(val)
        })
        if (item.gradeRule) {
          Vue.set(item.gradeRule, 'ruleType', 2)
          Vue.set(item.gradeRule, 'ruleConditions', '')
          if (item.gradeRule.discountBeforeAfter === 1) {
            if (item.gradeRule.moneyOrCount === 1) {
              item.gradeRule.rule = this.$t('Promotion.BeforeDiscount') + item.gradeRule.oneBuyMoney + this.$t('Promotion.ParticipateDiscount')
            } else {
              item.gradeRule.rule = this.$t('Promotion.QuantityReaches') + item.gradeRule.oneBuyCount + this.$t('Promotion.ParticipatePieces')
            }
          } else {
            if (item.gradeRule.moneyOrCount === 1) {
              item.gradeRule.rule =  this.$t('Promotion.AfterDiscount') + item.gradeRule.oneBuyMoney + this.$t('Promotion.ParticipateDiscount')
            } else {
              item.gradeRule.rule = this.$t('Promotion.QuantityReaches') + item.gradeRule.oneBuyCount + this.$t('Promotion.ParticipatePieces')
            }
          }
          this.changeActivityData.push(item.gradeRule)
        }
        this.changeActivityData.forEach((items, index) => {
          Vue.set(items, 'cartId', item.id)
          if (index === 0) {
            Vue.set(items, 'checked', true)
          } else {
            Vue.set(items, 'checked', false)
          }
        })
      })
    },
    changeActivityLabel (item) { // 切换活动选择
      this.changeActivityData.forEach((val, index) => {
        if (item.id === val.id) {
          val.checked = true
        } else {
          val.checked = false
        }
      })
    },
    confirmChangeActivity () {
      let product = {}
      this.changeActivityData.forEach((item, index) => {
        if (item.checked === true) {
          if (item.ruleType === 1) {
            product = {'id': item.cartId, 'ruleId': item.id,'orderType': this.orderType}
          } else {
            product = {'id': item.cartId, 'gradeDiscountId': item.id,'orderType': this.orderType}
          }
          this.$api.put(this, 'user/u/shoppingCart/changePromotion', product).then(res => {
            if (res.code === 0) {
              this.changeDialogVisible = false
              if (this.$i18n.locale === 'zh') {
                this.$message.success('切换活动成功!')
              } else { this.$message.success('Switch successfully.') }
              this.$emit('reloadFatherCart', true)
            } 
          })
        }
      })
    },
    goDoodsDetail (id) { // 去商品详情
    let routeData = null
      if (this.orderType === 3) { // 定制商品
        routeData = this.$router.resolve({
          name: 'ShopDetail',
          query: {good_id: id, goodsType: 2, accessType: 0}
        })
      } else {
        routeData = this.$router.resolve({
          name: 'ShopDetail',
          query: {good_id: id, activityType: 1, goodsType: 1, accessType: 0}
        })
      }
      window.open(routeData.href, '_blank')
    },
    deleteShopCarItems (id, item, $index) { // 删除购物车（单条）
      if (item.itemType === 3) { // 定制商品
        if (item.diyList) {
          this.deleteDiyId = id
          this.deleteDiyItem = item.diyList
          this.deleteDiyDialogVisible = true
          this.diyAll = true
          this.deleteDiyItem.forEach((item) => {
            Vue.set(item, 'gou', 1) // 删除货品勾选
          })
          return false
        }
      }
      let info = ''
      if (this.$i18n.locale === 'zh') {
        info = '此操作将删除该货品, 是否继续?'
      } else { info = 'The product will be deleted. Do you want to continue?' }
      this.$confirm(info, this.$t('P.Prompt'), {
        confirmButtonText: this.$t('P.OK'),
        cancelButtonText: this.$t('P.Cancel'),
        type: 'warning'
      }).then(() => {
        this.$api.delete(this, 'user/u/shoppingCart', {ids: item.id}).then(res => {
          if (res.code === 0) {
            if (this.$i18n.locale === 'zh') {
              this.$message.success('删除成功')
            } else { this.$message.success('Delete successfully') }
            this.shopCartSonData.forEach((val, index) => {
              if (index === $index) {
                this.shopCartSonData.splice(index, 1)
              }
            })
          } 
        })
      }).catch(() => {
        if (this.$i18n.locale === 'zh') {
          this.$message.info('已取消退出')
        } else { this.$message.info('Withdrawal canceled') }
      })
    },
    // diy相关
    lookDiyImage (imgList) { // 查看diy图片
      this.diyDialogVisible = true
      this.diyImages = imgList
    },
    lookDiyItem (item, itemList) { // 查看diy货品
      this.lookDiyDialogVisible = true
      this.adjustDiyCarId = item.id
      this.adjustDiyItem = itemList
    },
    chooseDiyGou (item, gou) {
      let type = 0
      if (gou === 1) {
        item.gou = 0
      } else {
        item.gou = 1
      }
      for (let i = 0; i < this.deleteDiyItem.length; i++) {
        if (this.deleteDiyItem[i].gou === 0) { // 未全选
          this.diyAll = false
          type = 1
          return
        }
      }
      if (type === 0) {
        this.diyAll = true // 全选
      } else {
        this.diyAll = false // 未全选
      }
    },
    selectDiyAll () {
      if (this.diyAll === true) { // 反选
        this.diyAll = false
        this.deleteDiyItem.forEach((item) => {
          item.gou = 0
        })
      } else {
        this.diyAll = true
        this.deleteDiyItem.forEach((item) => {
          item.gou = 1
        })
      }
    },
    confirmDeleteDiyItem () { // 删除diy货品
      let ids = ''
      let temp = []
      this.deleteDiyItem.forEach((item) => {
        if (item.gou === 1) {
          ids = ids + ',' + item.diyId
        }
      })
      ids = ids.substr(1) // 删除字符串第一位
      if (ids === '') {
        if (this.$i18n.locale === 'zh') {
          this.$message.success('请勾选货品')
        } else { this.$message.success('Please check the items') }
        return false
      }
      if (this.diyAll === true) {
        temp = {ids: this.deleteDiyId}
      } else {
        temp = {ids: this.deleteDiyId, diyIds: ids}
      }
      this.$api.delete(this, 'user/u/shoppingCart', temp).then(res => {
        if (res.code === 0) {
          if (this.$i18n.locale === 'zh') {
            this.$message.success('删除成功')
          } else { this.$message.success('Delete successfully') }
          this.$emit('reloadFatherCart', true)
          this.deleteDiyDialogVisible = false
        } 
      })
    },
    confirmAdjustDiyItem () {
      let totolCount = 0
      this.adjustDiyItem.forEach((item) => {
        totolCount += item.diyNum
      })
      this.$api.put(this, 'user/u/shoppingCart', {id: this.adjustDiyCarId, num: totolCount, diyList: this.adjustDiyItem}).then(res => {
        if (res.code === 0) {
          this.lookDiyDialogVisible = false
          this.$emit('reloadFatherCart', true)
        }
      })
    }
  },
  created () {
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem('bLang') ? window.localStorage.getItem('bLang') : 'zh-RMB';
    // this.shopCartSonData.forEach((val) => {
    //   Vue.set(val, 'gou', 1) // 默认购物车选中
    // })
  }
}
</script>

<style lang='less'>
  .el-dialog__wrapper{
    .el-dialog{
      width: 600px;
      .el-dialog__header{
        text-align: center;
      }
      .el-dialog__body{
        padding: 0;
        padding-bottom: 10px;
      }
      .el-dialog__footer{
        text-align: center;
      }
    }
  }
  .activity-el-table{
    .el-table__body-wrapper{
      .el-table__empty-block{
        height: 0!important;
        min-height: 0;
      }
    }
  }
</style>
<style scoped="scoped" lang='less'>
  .table-name{
    height: 45px;
    line-height: 45px;
  }
  .diy-gai{
    position: absolute;
    top: 0;
    left: 0;
    display: block;
    width: 145px;
    height: 30px;
    z-index: 1;
    cursor: pointer;
  }
  .mini-search-btn{
    font-size: 12px;
    padding: 5px;
  }
  .mini-search-btn-other{
    margin-left: 0;
    font-size: 12px;
    padding: 5px;
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
  .wen-hao1{
    display: block;
    width: 14px;
    height: 14px;
    background: url("../../src/assets/images/step/wen-hao.png") center center no-repeat;
  }
  .wen-hao2{
    top: 0;
    right: -15px;
  }
  .change-activity{
    max-height: 500px;
    padding-top: 20px;
    overflow-y: scroll;
    .item {
      margin-bottom: 20px;
      .chenked {
        width: 19px;
        height: 19px;
        display: inline-block;
        vertical-align: -5px;
        -webkit-appearance: none;
        appearance: none;
        outline: none;
        font-size: 14px;
        color: #333;
        background: url(../../src/assets/images/yuan3.png) no-repeat center center;
      }
      .chenked.active{
        background: url(../../src/assets/images/yuan4.png) no-repeat center center;
      }
      .cons{
        width: 100%;
        margin-left: 10px;
        padding-top: 10px;
        padding-bottom: 10px;
        padding-left: 20px;
        padding-right: 20px;
        border: 1px solid #ccc;
        .cons-btn{
          font-size: 12px;
          padding: 5px;
        }
        .rule{
          line-height: 30px;
          font-size: 12px;
        }
      }
    }
  }
  .spe{
    span{
      display: block;
      width: 30px;
      height: 45px;
      background: url("../../src/assets/images/choose.png") no-repeat center center;
    }
    span.gou{
      background: url("../../src/assets/images/gou.png") no-repeat center center;
    }
  }
  .song{
    h1{
      width: 22px;
      border-radius: 5px;
      word-wrap: break-word;
    }
  }
  /*diy*/
  .diy-all{
    .diy-css{
      max-height: 300px;
      .banner{
        width: 515px;
        position: relative;
        .banner-img{
          display: flex;
          align-items: center;
          justify-content: center;
          z-index: 0;
          img{
            height: 397px;
          }
        }
        .el-carousel{
          z-index: 0;
        }
      }
    }
    .dialog-footer{
      .el-button {
        margin-top: 10px;
      }
    }
  }
  .diy-delete-Img{
    width: 50px;
    img{
      margin-top: 5px;
      height: 50px;
    }
  }
  .dialog-footer-div{
    padding-left: 9px;
    span{
      padding-left: 20px;
      display: block;
      min-width: 30px;
      height: 45px;
      line-height: 45px;
      background: url("../../src/assets/images/choose.png") no-repeat left center;
    }
    span.gou{
      background: url("../../src/assets/images/gou.png") no-repeat left center;
    }
  }
  .diy-gou{
    span{
      display: block;
      width: 30px;
      height: 45px;
      background: url("../../src/assets/images/choose.png") no-repeat center center;
    }
    span.gou{
      background: url("../../src/assets/images/gou.png") no-repeat center center;
    }
  }
</style>
