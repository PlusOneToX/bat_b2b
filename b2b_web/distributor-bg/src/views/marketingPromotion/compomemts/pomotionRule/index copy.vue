<template>
  <div class="pomotion-rule">
    <el-form class="pomotion" ref="rule" :model="rule"  :rules="rules">
      <el-form-item class="pomoElItem" v-show="!disabled" :label="currentCount === 1 ? '规则一':(currentCount === 2 ? '规则二':(currentCount === 3 ? '规则三':(currentCount === 4 ? '规则四':(currentCount === 5 ? '规则五':'规则六'))))">
        <el-button class="mini-delete-btn rule-delete" size="mini" type="danger" @click="handleDeleteRule(currentCount)">删除</el-button>
      </el-form-item>

      <el-form-item label="活动标签" class="pomoElItem" prop="label">
        <el-input v-model="rule.label" :disabled="disabled" placeholder="不超过20个字" size="mini" maxlength="20" style="width: 200px;" />
      </el-form-item>

      

      <el-form-item label="活动对象:" class="pomoElItem">
        <el-radio-group v-model="rule.target" :disabled="disabled">
          <el-radio :label="1">单个商品</el-radio>
          <el-radio :label="2">整个订单</el-radio>
          <el-radio :label="3">单个货品</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="是否特价:" class="pomoElItem" v-show="rule.target === 3">
        <el-tooltip content="如果选择“是”，则满足条件时，货品的价格为特价；如果选择“否”，则满足条件时，参与满减满赠促销活动。" placement="right">
          <el-radio-group v-model="isSpecial" :disabled="disabled">
            <el-radio :label="false">否</el-radio>
            <el-radio :label="true">是</el-radio>
          </el-radio-group>
        </el-tooltip>
      </el-form-item>

      <el-form-item label="是否同时享受整个订单折扣:" class="pomoElItem" v-show="rule.target === 1 || rule.target === 3">
        <el-tooltip content="如果选择“是”，则客户可同时享受该单个商品活动和整个订单折扣活动。如果选择“否”，则客户下单默认选择优惠最多的活动使用。" placement="right">
          <el-radio-group v-model="rule.isEnjoy" :disabled="disabled">
            <el-radio :label="true">是</el-radio>
            <el-radio :label="false">否</el-radio>
          </el-radio-group>
        </el-tooltip>
      </el-form-item>

      <el-form-item label="请选择指定商品" prop="goodsItems" class="pomoElItem" v-show="rule.target === 1 || rule.target === 3" >
        <el-button class="mini-search-btn" @click="assignGoods()" v-show="!disabled">添加</el-button>
        <el-button class="mini-delete-btn" @click="clear()" v-show="!disabled">清空</el-button>
      </el-form-item >

      <div v-show="rule.target === 1">
        <el-table :data="goods" border header-row-class-name="header-row" class="tableCenter goods-table" style="width: 100%" max-height="200">
          <el-table-column align="center" label="商品编码" prop="goodsNo"> </el-table-column>
          <el-table-column align="center" label="商品名称" prop="goodsName"> </el-table-column>
          <el-table-column align="center" label="操作" width="220" v-if="!disabled">
            <template slot-scope="scope">
              <el-button class="mini-delete-btn" @click="handleDeleteGood(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div v-show="rule.target === 3">
        <el-table :data="goodItems" border header-row-class-name="header-row" class="tableCenter goods-table" style="width: 100%" max-height="200">
          <el-table-column align="center" label="货品编码" show-overflow-tooltip prop="itemCode"> </el-table-column>
          <el-table-column align="center" label="货品名称" show-overflow-tooltip prop="itemName"> </el-table-column>
          <el-table-column align="center" v-if="isSpecial" label="特价">
          <template slot-scope="scope">
            <el-input style="width: 100px;" size="mini" v-model.trim="scope.row.specialPrice" @input="scope.row.specialPrice=/^\d+\.?\d{0,2}$/.test(scope.row.specialPrice)||scope.row.specialPrice == '' ? scope.row.specialPrice:Number(scope.row.specialPrice.toString().match(/^\d+(?:\.\d{0,1})?/))" :disabled="disabled" type="number" min="0" step="0.01" />
          </template>
        </el-table-column>
          <el-table-column align="center" label="操作" width="220" v-if="!disabled">
            <template slot-scope="scope">
              <el-button class="mini-delete-btn" @click="handleDeleteItem(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-form>

    <el-form ref="rule" :model="rule" class="el-form1" :rules="rules" size="mini" label-width="130px" >
      <el-form-item label="指定商品是否累计" v-show="rule.target === 1 && !isSpecial">
        <el-tooltip content="多个指定商品情况下，如果选择“是”，只要所有指定商品总数/总金额达到条件即可享受" placement="right">
          <el-radio-group v-model="rule.addUpFlag" :disabled="disabled">
            <el-radio :label="true">是</el-radio>
            <el-radio :label="false">否</el-radio>
          </el-radio-group>
        </el-tooltip>
      </el-form-item>

      <el-form-item label="指定货品是否累计" v-show="rule.target === 3">
        <el-tooltip content="多个指定货品情况下，如果选择“是”，只要所有指定货品总数/总金额达到条件即可享受" placement="right">
          <el-radio-group v-model="rule.addUpFlag" :disabled="disabled">
            <el-radio :label="true">是</el-radio>
            <el-radio :label="false">否</el-radio>
          </el-radio-group>
        </el-tooltip>
      </el-form-item>
          
      <el-form-item label="按金额还是数量">
        <el-radio-group v-model="rule.moneyOrCount" :disabled="disabled">
          <el-radio :label="1">金额</el-radio>
          <el-radio :label="2">数量</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="一次性购买满" prop="moneyOrCount">
        <el-input v-if="rule.moneyOrCount === 1" v-model="rule.oneBuyMoney" @input="rule.oneBuyMoney=/^\d+\.?\d{0,2}$/.test(rule.oneBuyMoney)||rule.oneBuyMoney == '' ? rule.oneBuyMoney:Number(rule.oneBuyMoney.toString().match(/^\d+(?:\.\d{0,1})?/))"  style="width: 100px;" :disabled = "disabled" type="number" min="0" step="0.01" />
        <el-input v-else v-model="rule.oneBuyCount" @input="rule.oneBuyCount=/^\d+\.?\d{0,0}$/.test(rule.oneBuyCount)||rule.oneBuyCount == '' ? rule.oneBuyCount:Number(rule.oneBuyCount.toString().match(/^\d+(?:\.\d{0,0})?/))" style="width: 100px;" :disabled = "disabled" type="number" min="0" />
        <span style="color: #333333;"> 元/数量</span>
      </el-form-item>

      <el-form-item label="请选择活动形式" v-show="(rule.target === 1 || rule.target === 3) && !isSpecial">
        <el-radio-group v-model="rule.reduceOrPresent" :disabled="disabled">
          <el-radio :label="1">满减</el-radio>
          <el-radio :label="2">满赠</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item prop="reduceOrPresent" label="满减方式" v-show="((rule.target === 1 || rule.target === 3) && !isSpecial)?((rule.reduceOrPresent === 1 && !isSpecial)):!isSpecial">
        <el-radio-group v-model="isDiscount" :disabled="disabled">
          <el-radio :label="1" >打折</el-radio>
          <el-radio :label="2" >减免</el-radio>
        </el-radio-group>
        <el-tooltip content="打折数（如商品原价1000元，填70，则商品优惠后的价格为700元）" placement="right" v-show="isDiscount===1" >
          <el-input v-model="rule.discount"  @keyup.native="proving" style="width: 100px;margin-left:20px;" :disabled="disabled" />
        </el-tooltip>
        <span v-show="isDiscount===1" > %</span>
        <el-tooltip content="订单总价减免的价格" placement="right" v-show="isDiscount===2" >
          <el-input v-model="rule.reduction" @input="rule.reduction=/^\d+\.?\d{0,2}$/.test(rule.reduction)||rule.reduction == '' ? rule.reduction:Number(rule.reduction.toString().match(/^\d+(?:\.\d{0,1})?/))"  style="width: 100px;margin-left:20px;" :disabled = "disabled" type="number" min="0" step="0.01" />
        </el-tooltip>
        <span style="color: #333333;"> 元</span>
      </el-form-item>

      <el-form-item v-show="rule.reduceOrPresent === 1 && isDiscount === 2  && !isSpecial">
        <span style="margin-right:10px;color: #333333">满减是否叠加</span>
        <el-tooltip content="满赠可以叠加，则不限制单笔订单减免上限。（如规则设置满1000减100，客户指定商品总额2000元，则订单减免200元，反之最多减100）" placement="bottom">
          <el-radio-group v-model="rule.reductionIsAdd" :disabled="disabled">
            <el-radio :label="true" >是</el-radio>
            <el-radio :label="false" >否</el-radio>
          </el-radio-group>
        </el-tooltip>
      </el-form-item>

      <el-form-item prop="reduceOrPresent" label="满赠方式" v-show="((rule.target === 1 || rule.target === 3)  && !isSpecial)?rule.reduceOrPresent === 2:false">
        <span style="color: #333333;">送</span>
        <el-input v-model="rule.presentCount"  @input="rule.presentCount=/^\d+\.?\d{0,0}$/.test(rule.presentCount)||rule.presentCount == '' ? rule.presentCount:Number(rule.presentCount.toString().match(/^\d+(?:\.\d{0,0})?/))"  style="width: 100px;" :disabled = "disabled" type="number" min="0" /><span style="color: #333333;" > 件赠品</span>
        <el-button class="mini-search-btn" @click="presentShow=true" v-show="!disabled">添加商品</el-button>
      </el-form-item>

      <el-form-item v-show="((rule.target === 1 || rule.target === 3) && !isSpecial)?rule.reduceOrPresent === 2:false">
        <span style="color: #333333;">满赠是否叠加</span>
        <el-tooltip content="满赠可以叠加，则不限制单笔订单减免上限（如规则设置满3赠2，客户购买9个商品，则赠送6个赠品）。如果满赠不可以叠加，则限制单笔订单赠送上限（如规则设置满3赠2，客户购买9个商品，只赠送2个赠品）。" placement="bottom">
          <el-radio-group v-model="rule.presentIsAdd" style="margin-left:10px;" :disabled="disabled">
            <el-radio :label="true" >是</el-radio>
            <el-radio :label="false" >否</el-radio>
          </el-radio-group>
        </el-tooltip>
      </el-form-item>

      <el-form-item v-show="((rule.target === 1 || rule.target === 3)&&!isSpecial)?rule.reduceOrPresent === 2:false">
        <span style="color: #333333;">赠送商品，多种赠品，由客户自主选择赠品，活动总赠品数（赠完即止）</span>
        <el-table :data="items" border header-row-class-name="header-row" class="tableCenter goods-table" style="width: 100%" max-height="200">
          <el-table-column align="center" label="存货编码" prop="itemCode"> </el-table-column>
          <el-table-column align="center" label="货品名称" prop="itemName"> </el-table-column>
          <el-table-column align="center" label="总赠品数">
            <template slot-scope="scope">
              <el-input :disabled="disabled" type="number" min="0" v-model="scope.row.presentCount" @input="scope.row.presentCount=/^\d+\.?\d{0,0}$/.test(scope.row.presentCount)||scope.row.presentCount == '' ? scope.row.presentCount:Number(scope.row.presentCount.toString().match(/^\d+(?:\.\d{0,0})?/))" />
            </template> 
          </el-table-column>
          <el-table-column align="center" label="每次最多可选">
            <template slot-scope="scope">
              <el-input :disabled="disabled" type="number" min="0" v-model="scope.row.count" placeholder="不限制" @input="scope.row.count=/^\d+\.?\d{0,0}$/.test(scope.row.count)||scope.row.count == '' ? scope.row.count:Number(scope.row.count.toString().match(/^\d+(?:\.\d{0,0})?/))" />
            </template> 
          </el-table-column>
          <el-table-column align="center" label="库存" prop="totalNum"> </el-table-column>
          <el-table-column align="center" label="操作" width="220" v-if="!disabled">
            <template slot-scope="scope">
              <el-button class="mini-delete-btn" @click="handleItemDelete(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
    
      <!-- <el-form-item label="是否包邮">
        <el-radio-group v-model="rule.freeShipping" :disabled="disabled">
          <el-radio :label="true">是</el-radio>
          <el-radio :label="false">否</el-radio>
        </el-radio-group>
      </el-form-item> -->
    </el-form>

    <!-- 添加单个商品组件 -->
    <el-dialog :modal-append-to-body="false" :visible="goodsShow" width="80%" :before-close="closeGoodsDialog">
      <select-goods :saleStatus="3" :goodsType="goodsType" :selectGoodsData="goodss" ref="selectGoods" @submit="submit" @cancel="goodsShow=false"></select-goods>
    </el-dialog>

    <!-- 添加单个货品组件 -->
		<el-dialog :modal-append-to-body="false" :visible="itemsShow" width="80%" :before-close="closeItemsDialog">
		  <add-item :saleStatus="3" :goodsType="goodsType" :selectItemsData="goodsItems" ref="selectGoodItems" @cancel="itemsShow=false" @submit="getItemsData"></add-item>
		</el-dialog>
    
		<!-- 添加商品组件 -->
		<el-dialog :modal-append-to-body="false" :visible="presentShow" width="80%" :before-close="closePresentDialog">
		  <add-item :saleStatus="3" :goodsType="goodsType" :selectItemsData="goodss" ref="selectPresents" @cancel="presentShow=false" @submit="getPresentData"></add-item>
		</el-dialog>
	</div>
</template>


<script type="text/javascript">
import selectGoods from '@/views/goods/components/selectGoods'
import { getGoods, getItems, getItemsStock } from '@/views/marketingPromotion/promotionData'
import addItem from '@/views/marketingPromotion/compomemts/selectItem/addItem'

export default {
  props: {
    totalCount: {
      type: Number,
      default: 0
    },
    currentCount: {
      type: Number,
      default: 0
    },
    rule: {},
    disabled: true
  },
  components: {
    selectGoods,
    addItem
  },
  data() {
    return {
      goodsType: '',
      goods: [],
      goodItems: [],
      items: [],
      goodsShow: false,
      presentShow: false,
      itemsShow: false,
      isDiscount: 1,
      isSpecial:false,
      rules: {
        label: [{
          required: true,
          message: '请输入活动标签',
          trigger: 'blur'
        }],
        goodsItems: [{
          required: true,
          trigger: 'blur'
        }],
        moneyOrCount: [{
          required: true,
          trigger: 'blur'
        }],
        reduceOrPresent: [{
          required: true,
          trigger: 'blur'
        }]
      }
    }
  },
  created() {
    // this.getSysConf()
    // this.getGoodsItems()
     if(this.rule.ruleType !==1 && this.rule.isCompose == 1 && this.rule.groups != undefined && this.rule.groups.length>0){
      this.goodsAbleTabsValue = this.rule.groups[0].id
    } else if (this.rule.ruleType === 2) {
      // 商品
      this.rule.goodss = this.rule.goods
    } else if (this.rule.ruleType === 3) {
      // 货品
      this.rule.goodsItems = this.rule.goods
    }
  },
  methods: {
    proving() {
      this.rule.discount = this.rule.discount.replace(/[^\.\d]/g, '')
      this.rule.discount = this.rule.discount.replace('.', '')
      // if (this.rule.discount > 100) {
      //   this.rule.discount = 0
      //   this.$message.error('1-100(正整数）!')
      // }
    },
    // getSysConf() {
    //   this.$api.get(this, '/admin/u/po/systemConfig').then(res => {
    //     if (res.config.customizedAttendEventFlag === 1) {
    //       this.goodsType = ''
    //     } else {
    //       this.goodsType = 1
    //     }
    //   })
    // },
    getItemsStock() {
      getItemsStock(this, { ids: this.rule.itemIds }).then(res => {
        res.stocks.forEach(stock => {
          for (var i = 0; i < this.items.length; i++) {
            if (this.items[i].itemId === stock.itemId) {
              this.items[i].totalNum = stock.totalNum
              break
            }
          }
        })
      })
    },
    getGoodsItems() { // 数据
      this.goods = []
      this.goodItems = []
      if (this.rule.goods.length !== 0 && this.rule.target === 1) { // 获取指定商品的详情
        let goodsIds = ''
        for (let i = 0; i < this.rule.goods.length; i++) {
          if (i === 0) {
            goodsIds = String(this.rule.goods[i].goodsId)
          } else {
            goodsIds = goodsIds + ',' + String(this.rule.goods[i].goodsId)
          }
        }
        getGoods(this, { ids: goodsIds }).then(res => {
          this.goods = this.goods.concat(res.goods)
          this.changeGoods()
        })
      } else if (this.rule.goods.length !== 0 && this.rule.target === 3) {
        let itemIds = ''
        for (let i = 0; i < this.rule.goods.length; i++) {
          if (i === 0) {
            itemIds = String(this.rule.goods[i].itemId)
          } else {
            itemIds = itemIds + ',' + String(this.rule.goods[i].itemId)
          }
        }
        getItems(this, { ids: itemIds }).then(res => {
          res.items.forEach(element => {
            if(this.rule.reduceOrPresent === 3){
              for (let i = 0; i < this.rule.goods.length; i++) {
                if(Number(this.rule.goods[i].itemId) === element.item.id){
                  element.item.specialPrice = this.rule.goods[i].specialPrice
                  break
                }
              }
            }
            this.goodItems.push(element.item)
          })
          this.changeGoodItems()
        })
      }
      this.items = []
      if (this.rule.items.length !== 0) { // 获取赠送商品的详情
        this.rule.itemIds = ''
        for (let i = 0; i < this.rule.items.length; i++) {
          if (i === 0) {
            this.rule.itemIds = String(this.rule.items[i].itemId)
          } else {
            this.rule.itemIds = this.rule.itemIds + ',' + String(this.rule.items[i].itemId)
          }
          const item = {
            id: this.rule.items[i].itemId,
            itemId: this.rule.items[i].itemId,
            presentCount: this.rule.items[i].presentCount,
            itemName: '',
            itemCode: '',
            totalNum: 0
          }
          this.items.push(item)
        }
        getItems(this, { ids: this.rule.itemIds }).then(res => {
          res.items.forEach(element => {
            element.item.stockNum = 0
            this.items.forEach(ele => {
              if (ele.id === element.item.id) {
                ele.itemName = element.item.itemName
                ele.itemCode = element.item.itemCode
              }
            })
          })
        })
        this.getItemsStock()
      }
      if (this.rule.discount !== 0) {
        this.isDiscount = 1
      } else {
        this.isDiscount = 2
      }
      if(this.rule.reduceOrPresent === 3){
        this.isSpecial = true
      }else{
        this.isSpecial = false
      }
    },
    closeGoodsDialog() {
      this.$refs.selectGoods.handleCancel()
    // this.goodsShow = false;
    },
    closePresentDialog() {
      this.$refs.selectPresents.handleCancel()
    // this.presentShow = false;
    },
    closeItemsDialog() {
      this.$refs.selectGoodItems.handleCancel()
      // this.itemsShow = false
    },
    submit(val) {
      this.goods = []
      this.goods = this.goods.concat(val)
      this.setArr(this.goods)
      this.goodsShow = false
      this.changeGoods()
    },
    getItemsData(val) {
      this.goodItems = []
      this.goodItems = this.goodItems.concat(val)
      const obj = {}
      const temp = []
      for (let i = 0; i < this.goodItems.length; i++) {
        const type = Object.prototype.toString.call(this.goodItems[i].itemId)// 不加类型 分不清 1 '1'
        if (!obj[ this.goodItems[i].itemId + type]) {
          temp.push(this.goodItems[i])
          obj[ this.goodItems[i].itemId + type ] = true// 这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
        }
      }
      this.goodItems = temp
      this.itemsShow = false
      this.changeGoodItems()
    },
    getPresentData(val) {
      this.items = []
      this.items = this.items.concat(val)
      const obj = {}
      const temp = []
      for (let i = 0; i < this.items.length; i++) {
        const type = Object.prototype.toString.call(this.items[i].itemId)// 不加类型 分不清 1 '1'
        if (!obj[ this.items[i].itemId + type]) {
          temp.push(this.items[i])
          obj[ this.items[i].itemId + type ] = true// 这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
        }
      }
      this.items = temp
      this.presentShow = false
      this.changeItems()
      this.getItemsStock()
    },
    changeGoodItems() {
      this.rule.goods = this.goodItems
      this.rule.goodsItems = []
      for (let i = 0; i < this.goodItems.length; i++) {
        this.rule.goodsItems.push(this.goodItems[i])
        // this.rule.goodsItems.push({ goodsId: this.goodItems[i].goodsId, itemId: this.goodItems[i].itemId })
      }
    },
    changeItems() {
      for (let i = 0; i < this.items.length; i++) {
        if (i === 0) {
          this.rule.itemIds = String(this.items[i].itemId)
        } else {
          this.rule.itemIds = this.rule.itemIds + ',' + String(this.items[i].itemId)
        }
      }
      this.rule.items = this.items
    },
    changeGoods() {
      this.rule.goods = this.goods
      this.rule.goodsItems = []
      for (let i = 0; i < this.goods.length; i++) {
        this.rule.goodsItems.push({ goodsId: this.goods[i].id, itemId: '' })
      }
    },
    setArr(arr) { // 去重
      const obj = {}
      const temp = []
      for (let i = 0; i < arr.length; i++) {
        const type = Object.prototype.toString.call(arr[i].id)// 不加类型 分不清 1 '1'
        if (!obj[ arr[i].id + type]) {
          temp.push(arr[i])
          obj[ arr[i].id + type ] = true// 这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
        }
        this.goods = temp
      }
    },
    clear() { // 清空选定商品操作
      if (this.rule.target === 1) {
        this.goods.splice(0, this.goods.length)
        this.changeGoods()
      } else if (this.rule.target === 3) {
        this.goodItems.splice(0, this.goodItems.length)
        this.changeGoodItems()
      }
    },
    assignGoods() { // 添加指定商品操作
      this.rule.target === 1 ? this.goodsShow = true : this.itemsShow = true
    },
    handleDeleteGood(index) {
      this.goods.splice(index, 1)
      this.changeGoods()
    },
    handleDeleteItem(index) {
      this.goodItems.splice(index, 1)
      this.changeGoodItems()
    },
    handleItemDelete(index) {
      this.items.splice(index, 1)
      this.changeItems()
    },
    handleDeleteRule(index) { // 规则删除操作
      this.$emit('deleteRules', index)
    }
  },
  watch: {
    rule: {
      handler() {
        this.$emit('changeRule', this.rule, this.currentCount)
      },
      deep: true
    },
    isDiscount: {
      handler() {
        if (this.isDiscount === 1) {
          this.rule.reduction = 0
        } else {
          this.rule.discount = 0
        }
      },
      deep: true
    },
    isSpecial: {
      handler() {
        if (this.isSpecial) {
          this.rule.reduceOrPresent = 3
        } else {
          this.rule.reduceOrPresent = 1
        }
      },
      deep: true
    },
    'rule.target': function() {
      if(this.rule.target !== 3){
        this.isSpecial = false
      }
    }
  }
}
</script>

<style lang="scss">
.pomotion-rule{
   border-radius: 5px;
   padding: 16px;
   margin-bottom: 5px;
   border: 1px solid #ccc;
   background-color: #f8f8f8;
   .pomotion{
    width: 100%;
    margin-bottom: 10px;
    .pomoElItem {
      margin-bottom:10px;
    }
  }
   .hint{
	   padding-top: 10px;
	   font-size: 14px;
	   padding-bottom: 10px;
   }
   .rule-delete{
	   float: right;
   }
}
.operation{
	margin-bottom: 10px;
	.btn-add{
		color: #fff;
		background-color: #21b8cb;
		border-radius: 6px;
	}
  }
</style>
