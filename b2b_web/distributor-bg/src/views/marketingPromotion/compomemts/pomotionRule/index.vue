<template>
  <div class="pomotion-rule">
    <el-form class="pomotion" ref="rule" :model="rule">
      <el-form-item class="rule" v-show="!disabled" :label="'规则'+currentCount">
        <el-button v-show="totalCount > 1 || currentCount !== 1" class="mini-delete-btn rule-delete" size="mini" type="danger" @click="handleDeleteRule(currentCount)">删除</el-button>
      </el-form-item>

      <el-form-item label="规则标签" class="pomoElItem" prop="ruleName" label-width="100px">
        <el-input type="textarea" v-model="rule.ruleName" :disabled="disabled" maxlength="200" placeholder="请输入规则标签" style="width: 400px;" size="mini" />
      </el-form-item>
      <el-form-item label="规则英文标签" class="pomoElItem" prop="ruleNameEn" label-width="100px">
        <el-input type="textarea" v-model="rule.ruleNameEn" :disabled="disabled" maxlength="200" placeholder="请输入规则标签" style="width: 400px;" size="mini" />
      </el-form-item>

      <el-form-item label="规则对象:" class="pomoElItem">
        <el-radio-group v-model="rule.ruleType" :disabled="disabled">
          <el-radio :label='1'>整个订单</el-radio>
          <el-radio :label='2'>单个商品</el-radio>
          <el-radio :label='3'>单个货品</el-radio>
        </el-radio-group>
      </el-form-item>

      <!-- <el-form-item label="是否同时享受整个订单折扣:" class="pomoElItem" v-show="rule.ruleType === 2 || rule.ruleType === 3">
        <el-tooltip content="如果选择是，客户下单时可同时享有商品活动和订单活动，如果选择否，客户下单时只能同时享受一种要优惠活动。" placement="right">
          <el-radio-group v-model="rule.isEnjoy" :disabled="disabled">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-tooltip>
      </el-form-item> -->



      <el-form-item label="按金额还是数量:" class="pomoElItem">
        <el-radio-group v-model="rule.moneyOrCount" :disabled="disabled">
          <el-radio :label="1">金额</el-radio>
          <el-radio :label="2">数量</el-radio>
        </el-radio-group>
      </el-form-item>

      <!-- <el-form-item label="商品是否组合:" class="pomoElItem"  v-show="rule.ruleType === 2 || rule.ruleType === 3">
        <el-tooltip content="选择组合，可将商品或货品组合后设置条件，组合内的货品合并购买满一定的条件，即可享受活动" placement="right">
          <el-radio-group v-model="rule.isCompose" :disabled="disabled">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-tooltip>
      </el-form-item> -->

      <el-form-item label="指定商品是否累计" class="pomoElItem" v-show="rule.ruleType !== 1">
        <el-tooltip content="多个指定货品情况下，如果选择“是”，只要所有指定货品总数/总金额达到条件即可享受" placement="right">
          <el-radio-group v-model="rule.addUpFlag" :disabled="disabled">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-tooltip>
      </el-form-item>

      <el-form-item label="请选择指定商品" prop="goodss" class="pomoElItem" v-show="rule.ruleType === 2 || rule.ruleType === 3" >
        <el-button class="mini-search-btn" @click="assignGoods()" v-show="!disabled">添加</el-button>
        <el-button class="mini-delete-btn" @click="clear()" v-show="!disabled">清空</el-button>
      </el-form-item >

      <div v-show="rule.ruleType === 2">
        <!-- <div v-show="rule.isCompose === 0"> -->
          <el-table :data="goodss" border header-row-class-name="header-row" class="tableCenter goods-table" style="width: 100%" max-height="200">
            <el-table-column align="center" label="商品编码" prop="goodsNo"> </el-table-column>
            <el-table-column align="center" label="商品名称" prop="goodsName"> </el-table-column>
            <el-table-column align="center" label="操作" width="220" v-if="!disabled">
              <template slot-scope="scope">
                <el-button class="mini-delete-btn" @click="handleDeleteGood(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        <!-- </div> -->
        <!-- <div v-show="rule.isCompose === 1">
          <el-tabs type="border-card" v-model="goodsAbleTabsValue" :editable="!disabled" @edit="handleTabsEdit">
            <el-tab-pane
              :key="group.id"
              v-for="(group, index) in rule.groups"
              :name="group.id">
              <span slot="label" class="input" @focus="getFocus" @blur="romveFocus($event,index)" :contenteditable="!disabled" >{{group.label}}</span>
              <el-table :data="group.goodss" border header-row-class-name="header-row" class="tableCenter goods-table" style="width: 100%" max-height="200">
                <el-table-column align="center" label="商品编码" show-overflow-tooltip prop="goodsNo"> </el-table-column>
                <el-table-column align="center" label="商品名称" show-overflow-tooltip prop="goodsName"> </el-table-column>
                <el-table-column align="center" label="操作" width="220" v-if="!disabled">
                  <template slot-scope="scope">
                    <el-button class="mini-delete-btn" @click="handleDeleteItem(scope.$index,index)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
          </el-tabs>
        </div> -->
      </div>

      <div v-show="rule.ruleType === 3">
        <!-- <div v-show="rule.isCompose === 0"> -->
          <el-table :data="goodsItems" border header-row-class-name="header-row" class="tableCenter goods-table" style="width: 100%" max-height="200">
            <el-table-column align="center" label="货品编码" show-overflow-tooltip prop="itemCode"> </el-table-column>
            <el-table-column align="center" label="货品名称" show-overflow-tooltip prop="itemName"> </el-table-column>
            <el-table-column align="center" label="操作" width="220" v-if="!disabled">
              <template slot-scope="scope">
                <el-button class="mini-delete-btn" @click="handleDeleteGood(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        <!-- </div> -->
        <!-- <div v-show="rule.isCompose === 1"> -->
          <!-- <el-tabs type="border-card" v-model="goodsAbleTabsValue" :editable="!disabled" @edit="handleTabsEdit">
            <el-tab-pane
              :key="group.id"
              v-for="(group, index) in rule.groups"
              :name="group.id">
              <span slot="label" class="input" @focus="getFocus" @blur="romveFocus($event,index)" :contenteditable="!disabled" >{{group.label}}</span>
              <el-table :data="group.goodsItems" border header-row-class-name="header-row" class="tableCenter goods-table" style="width: 100%" max-height="200">
                <el-table-column align="center" label="货品编码" show-overflow-tooltip prop="itemCode"> </el-table-column>
                <el-table-column align="center" label="货品名称" show-overflow-tooltip prop="itemName"> </el-table-column>
                <el-table-column align="center" label="操作" width="220" v-if="!disabled">
                  <template slot-scope="scope">
                    <el-button class="mini-delete-btn" @click="handleDeleteItem(scope.$index,index)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
          </el-tabs> -->
        <!-- </div> -->
      </div>

      <el-form-item label="设置条件" class="setCondition">
      </el-form-item>

      <div class="add-condition">
        <div v-if="totalCount > 0">
           <!-- :groups='rule.groups' -->
          <component @deleteCondition = "deleteCondition" v-model="rule.conditions" :is="ruleCondition" :conditionTotal='conditionTotal' :condition ='condition' :disabled="disabled" :groups='rule.goods'
          v-for="(condition,index) in rule.conditions" :key="condition.id" :conditionCount='index+1' :moneyOrCount='rule.moneyOrCount' :target='rule.ruleType' >
          </component>
        </div>
        <div v-else class="no-image">暂时没有添加规则，请先添加新规则</div>
      </div>

      <div class="operation" v-show="!disabled">
          <button class="mini-search-btn"  @click.prevent="add(conditionTotal)">新增条件</button>
      </div>
    </el-form>

    <!-- 添加单个商品组件 -->
    <el-dialog :modal-append-to-body="false" :visible="goodsShow" width="80%" :before-close="closeGoodsDialog">
      <!-- <select-goods :saleStatus="3" :goodsType="goodsType" :selectGoodsData="goodss" ref="selectGoods" @submit="submit" @cancel="goodsShow=false"></select-goods> -->
      <select-goods :saleStatus="3" :selectGoodsData="goodss" ref="selectGoods" @submit="submit" @cancel="goodsShow=false"></select-goods>
    </el-dialog>

    <!-- 添加单个货品组件 -->
		<el-dialog :modal-append-to-body="false" :visible="itemsShow" width="80%" :before-close="closeItemsDialog">
		  <!-- <add-item :saleStatus="3" :goodsType="goodsType" :selectItemsData="goodsItems" ref="selectGoodItems" @cancel="itemsShow=false" @submit="getItemsData"></add-item> -->
      <add-item :saleStatus="3" :selectItemsData="goodsItems" ref="selectGoodItems" @cancel="itemsShow=false" @submit="getItemsData"></add-item>
    </el-dialog>
	</div>
</template>


<script type="text/javascript">
import selectGoods from '@/views/goods/components/selectGoods'
import ruleCondition from '@/views/marketingPromotion/compomemts/ruleCondition'
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
    rule: {
      ruleName:"",
      ruleNameEn:"",
      ruleType:1,
      isEnjoy:0,
      // isCompose:0,
      addUpFlag:0,
      moneyOrCount:1,
      groups:[{
        id:"1",
        isAdd:true,
        label:"分组1",
        goodsItemIds:'',
        goodss:[],
        goodsItems:[]
      },{
        id:'2',
        isAdd:true,
        label:"分组2",
        goodsItemIds:'',
        goodss:[],
        goodsItems:[]
      }],
      goods:[],
      conditions:[{
        conditionName:'',
        conditionNameEn:'',
        specialFlag:0,
        moneyOrCount:1,
        isEnjoy:0,
        goodsItems:[],
        oneBuyCount:'',
        oneBuyMoney:'',
        reduceOrPresent:1,
        reductionPresentAddFlag:1,
        reduceType:1,
        discount:'',
        reduction:'',
        presents:[],
        groupConditions:[{
          promotionGroupId:1,
          oneBuyCount:'',
          oneBuyMoney:'',
          goodss:[],
          goodsItems:[],
        },{
          promotionGroupId:2,
          oneBuyCount:'',
          oneBuyMoney:'',
          goodss:[],
          goodsItems:[],
        }],
        specials:[]
      }]
    },
    disabled: {
      type: Boolean,
      default: true
    }
  },
  components: {
    selectGoods,
    addItem,
    ruleCondition
  },
  data() {
    return {
      focusIndex:0,
      ruleCondition: "rule-condition",
      changeLabel:false,
      goodsAbleTabsValue:1,
      isFirst:true,
      goodsType: '',
      goodsShow: false,
      conditionTotal:1,
      presentShow: false,
      itemsShow: false,
      isDiscount: 1,
      specialFlag:0,
      goodss:[], // 商品
      goodsItems:[],  // 货品
      rules: {
        label: [{
          required: true,
          message: '请输入活动标签',
          trigger: 'blur'
        }],
        goods: [{
          required: true,
          trigger: 'blur'
        }],
        moneyOrCount: [{
          required: true,
          message: '一次性购买满不能为空',
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
    if(this.rule.ruleType !==1 && this.rule.groups != undefined && this.rule.groups.length>0){
      this.goodsAbleTabsValue = this.rule.groups[0].id
    } else if (this.rule.ruleType === 2) {
      // 商品 
      this.goodss = this.rule.goods
    } else if (this.rule.ruleType === 3) {
      // 货品
      this.goodsItems = this.rule.goods
      if (this.rule.conditions.length>0) {
        this.rule.conditions.forEach(item => {
          if (item.specialFlag === 0 && !item.specials) {
            item.specials = this.rule.goods
          }
        })
      }
    }
  },
  methods: {
    getFocus(){
      this.changeLabel = true
    },
    romveFocus($event,index){
      this.rule.groups[index].label = $event.target.innerText
      this.changeLabel = false
    },
    // handleTabsEdit(targetName, action){
    //   if (action === 'add') {
    //     this.rule.groups.push({
    //         id:(Number(this.rule.groups[this.rule.groups.length-1].id)+1) +'',
    //         isAdd:true,
    //         label:"分组"+(Number(this.rule.groups.length+1)),
    //         goodsItemIds:'',
    //         goodss:[],
    //         goodsItems:[],
    //     })
    //     this.rule.conditions.forEach(condition =>{
    //       condition.groupConditions.push({
    //         promotionGroupId:this.rule.groups[this.rule.groups.length-1],
    //         oneBuyCount:'',
    //         oneBuyMoney:'',
    //         goodss:[],
    //         goodsItems:[],
    //       })
    //     })
    //     this.goodsAbleTabsValue = this.rule.groups[this.rule.groups.length-1].id
    //   }
    //   if (action === 'remove' && !this.changeLabel) {
    //     if(this.rule.groups.length>2){
    //       for(let i=0;i<this.rule.groups.length;i++){
    //         if(this.rule.groups[i].id === targetName){
    //           this.rule.groups.splice(i, 1)
    //           if(i === this.rule.groups.length){
    //             this.goodsAbleTabsValue = this.rule.groups[i-1].id
    //           }else{
    //             this.goodsAbleTabsValue = this.rule.groups[i].id
    //           }
    //           this.rule.conditions.forEach(condition =>{
    //             condition.groupConditions.splice(i, 1)
    //           })
    //           break
    //         }
    //       }
    //     }else{
    //       this.$message.error('至少两个分组');
    //     }
    //   }
    // },
    proving() {
      this.rule.discount = this.rule.discount.replace(/[^\.\d]/g, '')
      this.rule.discount = this.rule.discount.replace('.', '')
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
    closeGoodsDialog() {
      this.$refs.selectGoods.handleCancel()
    },
    closeItemsDialog() {
      this.$refs.selectGoodItems.handleCancel()
    },
    // 商品
    submit(val) {
      let arr = []
      val.forEach(item =>{
        arr.push({
          // id: item.id,
          goodsId: item.goodsId ? item.goodsId : item.id,
          goodsName: item.goodsName,
          goodsNo: item.goodsNo,
          itemId: item.itemId,
          itemCode: item.itemCode,
          promotionId: item.promotionId
        })
      })
      this.rule.goods = []
      this.rule.goods = this.rule.goods.concat(arr)
      this.goodss = this.rule.goods
      this.goodsShow = false
    },
    // 货品
    getItemsData(val) {
      let arr = []
      val.forEach(item =>{
        arr.push({
          // id: item.id,
          goodsId: item.goodsId,
          goodsNo: item.goodsNo,
          itemId: item.itemId ? item.itemId : item.id,
          itemCode: item.itemCode,
          itemName: item.itemName,
          promotionId: item.promotionId
        })
      })
      this.rule.goods = []
      this.rule.goods = this.rule.goods.concat(arr)
      this.goodsItems = this.rule.goods
      this.rule.conditions.forEach(condition =>{
        let specials = JSON.parse(JSON.stringify(this.rule.goods))
        if(condition.specials === undefined){
          condition.specials = []
        }
        let count = specials.length - condition.specials.length
        for(let i = 0; i<count ;i++){
          specials[i].specialPrice = ''
          condition.specials.splice(i,0,specials[i])
        }
      })
      this.itemsShow = false
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
        this.goodss = temp
      }
    },

    setItemArr(arr){
      const obj = {}
      const temp = []
      for (let i = 0; i < arr.length; i++) {
        const type = Object.prototype.toString.call(arr[i].itemId)// 不加类型 分不清 1 '1'
        if (!obj[ arr[i].itemId + type]) {
          temp.push(arr[i])
          obj[arr[i].itemId + type ] = true// 这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
        }
      }
    },
    clear() { // 清空选定商品操作
      // if(this.rule.isCompose == 1){
      //   this.rule.groups.forEach(group => {
      //     if(group.goodss instanceof Array){
      //       group.goodss.splice(0, group.goodss.length)
      //     }
      //     if(group.goodsItems instanceof Array){
      //       group.goodsItems.splice(0, group.goodsItems.length)
      //     }
      //   });
      //   this.rule.conditions.forEach(condition =>{
      //     condition.groupConditions.forEach(groupCondition =>{
      //       if(groupCondition.goodss instanceof Array){
      //         groupCondition.goodss.splice(0, groupCondition.goodss.length)
      //       }
      //       if(groupCondition.goodsItems instanceof Array){
      //         groupCondition.goodsItems.splice(0, groupCondition.goodsItems.length)
      //       }
      //     })
      //   })
      // }else{
        // if(this.rule.goodss instanceof Array){
        //   this.rule.goodss.splice(0, this.rule.goodss.length)
        // }
        if(this.rule.goods instanceof Array){
          this.rule.goods.splice(0, this.rule.goods.length)
        }
        this.rule.conditions.forEach(condition =>{
          // if(condition.goodss instanceof Array){
          //   condition.goodss.splice(0, condition.goodss.length)
          // }
          // if(condition.goodsItems instanceof Array){
          //   condition.goodsItems.splice(0, condition.goodsItems.length)
          // }
          if(condition.specials instanceof Array){
            condition.specials.splice(0, condition.specials.length)
          }
        })
      // }
    },
    assignGoods() { // 添加指定商品操作
      let goodsArr = []
      if(this.rule.ruleType === 2){
        if (this.goodss && this.goodss.length > 0) {
          this.goodss.forEach(item => {
            goodsArr.push({
              id: item.goodsId,
              goodsId: item.goodsId,
              goodsName: item.goodsName,
              goodsNo: item.goodsNo,
              promotionId: item.promotionId,
              promotionRuleId: item.promotionRuleId
            })
          })
          this.goodss = goodsArr
        }
      } else if(this.rule.ruleType === 3){
        let itemArr = []
        if (this.goodsItems && this.goodsItems.length>0) {
          this.goodsItems.forEach(item => {
            itemArr.push({
              id: item.itemId,
              goodsId: item.goodsId,
              goodsNo: item.goodsNo,
              goodsName: item.goodsName,
              itemCode: item.itemCode,
              itemId: item.itemId,
              itemName: item.itemName,
              promotionId: item.promotionId,
              promotionRuleId: item.promotionRuleId
            })
          })
           this.goodsItems = itemArr
        }
      }
      this.rule.ruleType === 2 ? this.goodsShow = true : this.itemsShow = true
    },
    handleDeleteGood(index) { // 非组合商品删除
      if(this.rule.ruleType === 2){
        this.goodss.splice(index, 1)
        this.rule.goods.splice(index, 1)
      }else if(this.rule.ruleType === 3){
        this.rule.conditions.forEach(condition =>{
          if (condition.specials && condition.specials.length>0) {
            condition.specials.splice(index, 1)
          }
        })
        this.goodsItems.splice(index, 1)
        this.rule.goods.splice(index, 1)
      }
    },
    // handleDeleteItem(index1,index2) {// 组合商品删除
    //   if(this.rule.ruleType === 2){
    //     this.rule.groups[index2].goodss.splice(index1, 1)
    //     this.rule.conditions.forEach(condition =>{
    //       condition.groupConditions[index2].goodss.splice(index1, 1)
    //     })
    //   }else if(this.rule.ruleType === 3){
    //     this.rule.groups[index2].goodsItems.splice(index1, 1)
    //     this.rule.conditions.forEach(condition =>{
    //       condition.groupConditions[index2].goodsItems.splice(index1, 1)
    //     })
    //   }
    // },
    handleItemDelete(index) {
      this.items.splice(index, 1)
      this.changeItems()
    },
    handleDeleteRule(index) { // 规则删除操作
      this.$emit('deleteRules', index)
    },
    deleteCondition(index){
      this.rule.conditions.splice(index-1,1);
      this.conditionTotal = this.conditionTotal -1;
    },
    add (count) { //..新增条件操作
      let condition = {
          conditionName:'',
          conditionNameEn: '',
          discount:'',
          oneBuyCount:'',
          oneBuyMoney:'',
          presentCount:0, // 满赠数量
          presents:[], // 赠品列表
          reduceOrPresent:1, // 促销统计方式
          reduceType:1, // 满减类型
          reduction:'', // 减免
          reductionPresentAddFlag:1, // 满减满赠是否叠加
          specialFlag:0, // 是否特价
          specials:[], // 特价商品列表
          // moneyOrCount:1,
          // isEnjoy:0,
          // goodsItems:[],
          // addUpFlag:0,
          // groupConditions:[],
      }
      if(this.rule.ruleType === 3){ // 货品
        if (this.goodsItems && this.goodsItems.length > 0) {
          condition.specials = JSON.parse(JSON.stringify(this.goodsItems))
          condition.specials.forEach(item =>{
            item.specialPrice = ''
          })
        }
      }

      // if (this.rule.groups && this.rule.groups.length>0) {
      //   this.rule.groups.forEach(item =>{
      //     let groupCondition = {
      //         promotionGroupId:Number(item.id),
      //         oneBuyCount:'',
      //         oneBuyMoney:'',
      //         goodss:[],
      //         goodsItems:[]
      //     }
      //     if(this.rule.ruleType === 2){
      //       groupCondition.goodss = JSON.parse(JSON.stringify(item.goodss))
      //     }else if(this.rule.ruleType === 3){
      //       groupCondition.goodsItems = JSON.parse(JSON.stringify(item.goodsItems))
      //       groupCondition.goodsItems.forEach(item =>{
      //         item.specialPrice = ''
      //       })
      //     }
      //     condition.groupConditions.push(groupCondition)
      //   })
      // }
     
      this.rule.conditions.push(condition)
      this.conditionTotal = count+1
    }
  },
  watch: {
    // "rule.isCompose":{
    //   handler() {
    //     if(this.rule.isCompose === 1 && this.rule.groups.length === 0){
    //       this.rule.groups.push({
    //         id:'1',
    //         isAdd:true,
    //         label:"分组1",
    //         goodsItemIds:'',
    //         goodss:[],
    //         goodsItems:[],
    //       })
    //       this.rule.groups.push({
    //         id:'2',
    //         isAdd:true,
    //         label:"分组2",
    //         goodsItemIds:'',
    //         goodss:[],
    //         goodsItems:[],
    //       })
    //       this.rule.conditions.forEach(condition =>{
    //         condition.groupConditions.push({
    //           promotionGroupId:1,
    //           oneBuyCount:'',
    //           oneBuyMoney:'',
    //           goodss:[],
    //           goodsItems:[],
    //         })
    //         condition.groupConditions.push({
    //           promotionGroupId:2,
    //           oneBuyCount:'',
    //           oneBuyMoney:'',
    //           goodss:[],
    //           goodsItems:[],
    //         })
    //       })
    //     }
    //   },
    //   deep: true
    // },
    rule: {
      handler() {
        this.conditionTotal = this.rule.conditions.length
        if(this.rule.groups && this.rule.groups.length>0 && this.isFirst){
          this.goodsAbleTabsValue = this.rule.groups[0].id
          this.isFirst = false
        }
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
    specialFlag: {
      handler() {
        if (this.specialFlag) {
          this.rule.reduceOrPresent = 3
        } else {
          this.rule.reduceOrPresent = 1
        }
      },
      deep: true
    },
    'rule.ruleType': {
      handler() {
        if(this.rule.ruleType !== 3){
          this.rule.conditions.forEach(item => {
            this.$set(item, 'specialFlag', 0)
          })
        }
      },
      deep: true
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
   .el-tabs__nav {
      margin-left: 0px;
    }
   .pomotion{
    width: 100%;
    margin-bottom: 10px;
    .pomoElItem {
      margin-bottom:10px;
    }
    .rule{
      margin-bottom:10px;
      .el-form-item__label{
        text-align: right;
        float: left;
        font-size: 14px;
        font-weight: 500;
        color: #21b8cb;
        padding: 0 12px 0 0;
        box-sizing: border-box;
      }
    }
  }
   .hint{
	   padding-top: 10px;
	   font-size: 14px;
	   padding-bottom: 10px;
   }
   .rule-delete{
	   float: left;
   }
   .el-tabs__new-tab {
    float: right;
    border: 1px solid #d3dce6;
    height: 18px;
    width: 18px;
    line-height: 18px;
    margin: 12px 10px 9px 10px;
    border-radius: 3px;
    text-align: center;
    font-size: 12px;
    color: #d3dce6;
    cursor: pointer;
    -webkit-transition: all .15s;
    transition: all .15s;
  }
  .add-condition{
    border-radius: 5px;
    background-color: #f8f8f8;
    .no-image{
      margin-left: 16px;
    }
  }
}

.input{
  border: 0px;
  outline-style: none;
  font-size: 14px;
  font-weight: 500;
  overflow: visible;
  width: 100px;
}

.operation{
	margin-bottom: 10px;
	.btn-add{
		color: #fff;
		background-color: #21b8cb;
		border-radius: 6px;
	}
}
.setCondition{
  margin-top:10px;
}
</style>
