<template>
  <div class="rule-condition">
    <el-form class="pomotion" ref="rule" :model="condition"  :rules="rules">
      <el-form-item class="condition" v-show="!disabled" :label="'条件'+conditionCount">
        <el-button v-show="conditionCount !== 1 || conditionTotal > 1" class="mini-delete-btn rule-delete" size="mini" type="danger" @click="handleDeleteCondition(conditionCount)">删除</el-button>
      </el-form-item>

      <el-form-item label="条件标签" class="pomoElItem" prop="conditionName" label-width="100px">
        <el-input v-model="condition.conditionName" :disabled="disabled" type="textarea" maxlength="200" placeholder="请输入条件标签" size="mini" style="width: 400px;" />
      </el-form-item>

      <el-form-item label="条件英文标签" class="pomoElItem" prop="conditionNameEn" label-width="100px">
        <el-input v-model="condition.conditionNameEn" :disabled="disabled" type="textarea" maxlength="200" placeholder="请输入条件标签" size="mini" style="width: 400px;" />
      </el-form-item>

      <el-form-item class="pomoElItem" v-if="moneyOrCount === 1" label="一次性购买满" prop="oneBuyMoney" v-show="isCompose === 0">
        <el-input size="mini" v-model="condition.oneBuyMoney" @input="condition.oneBuyMoney=/^\d+\.?\d{0,2}$/.test(condition.oneBuyMoney)||condition.oneBuyMoney == '' ? condition.oneBuyMoney:Number(condition.oneBuyMoney.toString().match(/^\d+(?:\.\d{0,1})?/))"  style="width: 100px;" :disabled = "disabled" type="number" min="0" step="0.01" />
        <span style="color: #606266;font-weight: 500;"> 元</span>
      </el-form-item>

      <el-form-item class="pomoElItem" v-else label="一次性购买满" prop="oneBuyCount" v-show="isCompose === 0">
        <el-input size="mini" v-model="condition.oneBuyCount" @input="condition.oneBuyCount=/^\d+\.?\d{0,0}$/.test(condition.oneBuyCount)||condition.oneBuyCount == '' ? condition.oneBuyCount:Number(condition.oneBuyCount.toString().match(/^\d+(?:\.\d{0,0})?/))" style="width: 100px;" :disabled = "disabled" type="number" min="0" />
        <span style="color: #606266;font-weight: 500;"> 数量</span>
      </el-form-item>



      <el-form-item label="是否特价:" class="pomoElItem" v-show="target === 3" >
        <el-tooltip content="选择组合，可将商品或货品组合后设置条件，组合内的货品合并购买满一定的条件，即可享受活动" placement="right">
          <el-radio-group v-model="condition.specialFlag" :disabled="disabled">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-tooltip>
      </el-form-item>
      <div class="pomoElItem" v-show="target === 2 && isCompose === 1">
        <!-- <el-tabs type="border-card" v-model="goodsAbleTabsValue">
          <el-tab-pane
            :key="group.id"
            v-for="(group, index) in groups"
            :name="group.id">
            <span slot="label" class="input"  >{{group.label}}</span>
            <el-table :data="condition.groupConditions[index].goodss" :span-method="objectSpanMethod" border header-row-class-name="header-row" class="tableCenter goods-table" style="width: 100%" max-height="200">
              <el-table-column align="center" label="商品编码" show-overflow-tooltip prop="goodsNo"> </el-table-column>
              <el-table-column align="center" label="商品名称" show-overflow-tooltip prop="goodsName"> </el-table-column>
              <el-table-column align="center" label="一次性购买满">
                <template slot-scope="scope">
                  <div>
                    <el-input size="mini" v-if="moneyOrCount === 1" v-model="condition.groupConditions[index].oneBuyMoney" @input="condition.groupConditions[index].oneBuyMoney=/^\d+\.?\d{0,2}$/.test(condition.groupConditions[index].oneBuyMoney)||condition.groupConditions[index].oneBuyMoney == '' ? condition.groupConditions[index].oneBuyMoney:Number(condition.groupConditions[index].oneBuyMoney.toString().match(/^\d+(?:\.\d{0,1})?/))"  style="width: 100px;" :disabled = "disabled" type="number" min="0" step="0.01" />
                    <el-input size="mini" v-else v-model="condition.groupConditions[index].oneBuyCount" @input="condition.groupConditions[index].oneBuyCount=/^\d+\.?\d{0,0}$/.test(condition.groupConditions[index].oneBuyCount)||condition.groupConditions[index].oneBuyCount == '' ? condition.groupConditions[index].oneBuyCount:Number(condition.groupConditions[index].oneBuyCount.toString().match(/^\d+(?:\.\d{0,0})?/))" style="width: 100px;" :disabled = "disabled" type="number" min="0" />
                    <span style="color: #606266;font-weight: 500;"> 元/数量</span>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs> -->
      </div>

      <div class="pomoElItem" v-show="target === 3">
        <div v-show="isCompose === 0 && condition.specialFlag==1">
          <el-table :data="condition.specials" border header-row-class-name="header-row" class="tableCenter goods-table" style="width: 100%" max-height="200">
            <el-table-column align="center" label="货品编码" show-overflow-tooltip prop="itemCode"> </el-table-column>
            <el-table-column align="center" label="货品名称" show-overflow-tooltip prop="itemName"> </el-table-column>
            <el-table-column align="center" v-if="condition.specialFlag==1" label="特价" >
              <template slot-scope="scope">
                <el-input style="width: 100px;" size="mini" v-model.trim="scope.row.specialPrice" @input="scope.row.specialPrice=/^\d+\.?\d{0,2}$/.test(scope.row.specialPrice)||scope.row.specialPrice == '' ? scope.row.specialPrice:Number(scope.row.specialPrice.toString().match(/^\d+(?:\.\d{0,1})?/))" :disabled="disabled" type="number" min="0" step="0.01" />
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div v-show="isCompose === 1">
          <!-- <el-tabs type="border-card" v-model="goodsAbleTabsValue">
            <el-tab-pane
              :key="group.id"
              v-for="(group, index) in groups"
              :name="group.id">
              <span slot="label" class="input" >{{group.label}}</span>
              <el-table :data="condition.groupConditions[index].goodsItems" :span-method="objectSpanMethod" border header-row-class-name="header-row" class="tableCenter goods-table" style="width: 100%" max-height="200">
                <el-table-column align="center" label="货品编码aa" show-overflow-tooltip prop="itemCode"> </el-table-column>
                <el-table-column align="center" label="货品名称" show-overflow-tooltip prop="itemName"> </el-table-column>
                <el-table-column align="center" v-if="condition.specialFlag === 1" label="特价" v-show="condition.specialFlag==1">
                  <template slot-scope="scope">
                    <el-input style="width: 100px;" size="mini" v-model.trim="scope.row.specialPrice" @input="scope.row.specialPrice=/^\d+\.?\d{0,2}$/.test(scope.row.specialPrice)||scope.row.specialPrice == '' ? scope.row.specialPrice:Number(scope.row.specialPrice.toString().match(/^\d+(?:\.\d{0,1})?/))" :disabled="disabled" type="number" min="0" step="0.01" />
                  </template>
                </el-table-column>
                <el-table-column align="center" label="一次性购买满">
                  <template slot-scope="scope">
                    <div>
                      <el-input size="mini" v-if="moneyOrCount === 1" v-model="condition.groupConditions[index].oneBuyMoney" @input="condition.groupConditions[index].oneBuyMoney=/^\d+\.?\d{0,2}$/.test(condition.groupConditions[index].oneBuyMoney)||condition.groupConditions[index].oneBuyMoney == '' ? condition.groupConditions[index].oneBuyMoney:Number(condition.groupConditions[index].oneBuyMoney.toString().match(/^\d+(?:\.\d{0,1})?/))"  style="width: 100px;" :disabled = "disabled" type="number" min="0" step="0.01" />
                      <el-input size="mini" v-else v-model="condition.groupConditions[index].oneBuyCount" @input="condition.groupConditions[index].oneBuyCount=/^\d+\.?\d{0,0}$/.test(condition.groupConditions[index].oneBuyCount)||condition.groupConditions[index].oneBuyCount == '' ? condition.groupConditions[index].oneBuyCount:Number(condition.groupConditions[index].oneBuyCount.toString().match(/^\d+(?:\.\d{0,0})?/))" style="width: 100px;" :disabled = "disabled" type="number" min="0" />
                      <span style="color: #606266;font-weight: 500;"> 元/数量</span>
                    </div>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
          </el-tabs> -->
        </div>
      </div>

      <!-- <el-form-item label="是否参与其他活动" class="pomoElItem" v-show="target === 3 && condition.specialFlag === 1">
        <el-tooltip content="如特价和满赠满减同享情况" placement="right">
          <el-radio-group v-model="condition.isEnjoy" :disabled="disabled">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-tooltip>
      </el-form-item> -->
    </el-form>

    <el-form class="pomotion" ref="rule" :model="condition"  :rules="rules" v-show="target === 1 ||target === 2 || (target === 3 && condition.specialFlag === 0) || (target === 3 && condition.specialFlag === 1 && condition.isEnjoy == 1)">
      <el-form-item class="pomoElItem" prop="reduceOrPresent" label="请选择活动形式">
        <el-radio-group v-model="condition.reduceOrPresent" :disabled="disabled">
          <el-radio :label="1">满减</el-radio>
          <el-radio :label="2">满赠</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item class="pomoElItem" prop="reduceType" label="满减方式"  v-show="condition.reduceOrPresent === 1">
        <el-radio-group v-model="condition.reduceType" :disabled="disabled">
          <el-radio :label="1" >减免</el-radio>
          <el-radio :label="2" >打折</el-radio>
        </el-radio-group>
        <el-tooltip content="打折数（如商品原价1000元，填70，则商品优惠后的价格为700元）" placement="right" v-show="condition.reduceType===2" >
          <el-input size="mini" v-model="condition.discount"  @keyup.native="proving" style="width: 100px;margin-left:20px;color: #606266;font-weight: 500;" :disabled="disabled" />
        </el-tooltip>
        <span v-show="condition.reduceType===2" style="color: #606266;font-weight: 500;" > %</span>
        <el-tooltip content="订单总价减免的价格" placement="right" v-show="condition.reduceType===1" >
          <el-input size="mini" v-model="condition.reduction" @input="condition.reduction=/^\d+\.?\d{0,2}$/.test(condition.reduction)||condition.reduction == '' ? condition.reduction:Number(condition.reduction.toString().match(/^\d+(?:\.\d{0,1})?/))"  style="width: 100px;margin-left:20px;color: #606266;font-weight: 500;" :disabled = "disabled" type="number" min="0" step="0.01" />
        </el-tooltip>
        <span style="color: #606266;font-weight: 500;" v-show="condition.reduceType===1"> 元</span>
      </el-form-item>

      <el-form-item class="pomoElItem" label="满减是否叠加" v-show="condition.reduceOrPresent === 1 && condition.reduceType === 1">
        <el-tooltip content="满赠可以叠加，则不限制单笔订单减免上限。（如规则设置满1000减100，客户指定商品总额2000元，则订单减免200元，反之最多减100）" placement="bottom">
          <el-radio-group v-model="condition.reductionPresentAddFlag" :disabled="disabled">
            <el-radio :label="1" >是</el-radio>
            <el-radio :label="0" >否</el-radio>
          </el-radio-group>
        </el-tooltip>
      </el-form-item>

      <el-form-item prop="presentCount" label="满赠方式" class="pomoElItem" v-show="condition.reduceOrPresent === 2">
        <span style="color: #606266;font-weight: 500;">送</span>
        <el-input size="mini" v-model="condition.presentCount"  
        @input="condition.presentCount=/^\d+\.?\d{0,0}$/.test(condition.presentCount)||condition.presentCount == '' 
        ? condition.presentCount:Number(condition.presentCount.toString().match(/^\d+(?:\.\d{0,0})?/))"  
        style="width: 100px;color: #606266;font-weight: 500;" :disabled = "disabled" type="number" min="0" /><span style="color: #333333;" > 件赠品</span>
        <el-button class="mini-search-btn"  v-show="!disabled" @click="addPresent()">添加商品</el-button>
      </el-form-item>

      <el-form-item class="pomoElItem" v-show="condition.reduceOrPresent === 2" label="满赠是否叠加">
        <el-tooltip content="满赠可以叠加，则不限制单笔订单减免上限（如规则设置满3赠2，客户购买9个商品，则赠送6个赠品）。如果满赠不可以叠加，则限制单笔订单赠送上限（如规则设置满3赠2，客户购买9个商品，只赠送2个赠品）。" placement="bottom">
          <el-radio-group v-model="condition.reductionPresentAddFlag" style="margin-left:10px;" :disabled="disabled">
            <el-radio :label="1" >是</el-radio>
            <el-radio :label="0" >否</el-radio>
          </el-radio-group>
        </el-tooltip>
      </el-form-item>

      <el-form-item prop="presents" class="pomoElItem" v-show="condition.reduceOrPresent === 2" >
        <span style="color: #606266;font-weight: 500;">赠送商品，多种赠品，由客户自主选择赠品，活动总赠品数（赠完即止）</span>
        <el-table :data="condition.presents" border header-row-class-name="header-row" class="tableCenter goods-table" style="width: 100%" max-height="200">
          <el-table-column align="center" label="存货编码" prop="itemCode"> </el-table-column>
          <el-table-column align="center" label="货品名称" prop="itemName" show-overflow-tooltip> </el-table-column>
          <el-table-column align="center" label="总赠品数">
            <template slot-scope="scope">
              <el-input size="mini" :disabled="disabled" style="color: #606266;font-weight: 500;" type="number" min="0" v-model="scope.row.totalCount" 
              @input="scope.row.totalCount=/^\d+\.?\d{0,0}$/.test(scope.row.totalCount)||scope.row.totalCount == '' 
              ? scope.row.totalCount:Number(scope.row.totalCount.toString().match(/^\d+(?:\.\d{0,0})?/))" />
            </template>
          </el-table-column>
          <el-table-column align="center" label="每次最多可选">
            <template slot-scope="scope">
              <el-input :disabled="disabled" type="number" min="0" v-model="scope.row.count" placeholder="不限制" 
              @input="scope.row.count=/^\d+\.?\d{0,0}$/.test(scope.row.count)||scope.row.count == '' 
              ? scope.row.count:Number(scope.row.count.toString().match(/^\d+(?:\.\d{0,0})?/))" />
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
    </el-form>


		<!-- 添加赠品组件 -->
		<el-dialog :modal-append-to-body="false" :visible="presentShow" width="80%" :before-close="closePresentDialog">
		  <add-item :saleStatus="3" :goodsType="goodsType" :selectItemsData="presents" ref="selectPresents" @cancel="presentShow=false" @submit="getPresentData"></add-item>
		</el-dialog>
	</div>
</template>


<script type="text/javascript">
import addItem from '@/views/marketingPromotion/compomemts/selectItem/addItem'

export default {
  props: {
    groups:{},
    conditionTotal: {
      type: Number,
      default: 0
    },
    conditionCount: {
      type: Number,
      default: 0
    },
    isCompose:{
      type:Number,
      default:0
    },
    condition: {
      conditionName:'',
      conditionNameEn:'',
      specialFlag:0,
      isEnjoy:0,
      goodss:[],
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
    },
    disabled: true,
    moneyOrCount:{
      type: Number,
      default: 1
    },
    target:{
      type: Number,
      default: 1
    },

  },
  components: {
    addItem
  },
  data() {
    return {
      goodsAbleTabsValue:0,
      isFirst:true,
      isItemIds:true,
      goodsType: '',
      presents: [],
      presentShow: false,
      itemIds:'',
      rules: {
        conditionName: [{
          required: true,
          message: '请输入条件标签',
          trigger: 'blur'
        }],
        goods: [{
          required: true,
          trigger: 'blur'
        }],
        oneBuyMoney: [{
          required: true,
          message: '一次性购买满不能为空',
          trigger: 'blur'
        }],
        oneBuyCount: [{
          required: true,
          message: '一次性购买满不能为空',
          trigger: 'blur'
        }],
        presentCount: [{
          required: true,
          trigger: 'blur'
        }],
        presents: [{
          required: true,
          trigger: 'blur'
        }]
      }
    }
  },
  created() {
    // this.getSysConf()
    this.changeItems();
    this.getItemsStock();
    if(this.target !==1 && this.isCompose == 1 && this.groups != undefined && this.groups.length>0){
      this.goodsAbleTabsValue = this.groups[0].id
    }
  },
  methods: {
    // objectSpanMethod({ row, column, rowIndex, columnIndex }) {
    //   let length = 0;
    //   this.groups.forEach((group,index) =>{
    //     if(group.id === this.goodsAbleTabsValue){
    //       if(this.target === 2 && this.isCompose === 1 && this.condition.groupConditions[index].goodss !== undefined){
    //         length = this.condition.groupConditions[index].goodss.length
    //       }else if(this.target === 3 && this.isCompose === 1 && this.condition.groupConditions[index].goodsItems !== undefined){
    //         length = this.condition.groupConditions[index].goodsItems.length
    //       }
    //     }
    //   })
    //   if( this.target=== 3 && this.condition.specialFlag === 1 && columnIndex === 3){
    //     if(rowIndex === 0){
    //       return {
    //         rowspan: length,
    //         colspan: 1
    //       }
    //     }else{
    //       return {
    //         rowspan: length,
    //         colspan: 0
    //       }
    //     }
    //   }else if(((this.target=== 3 && this.condition.specialFlag === 0) || this.target === 2) &&  columnIndex === 2){
    //     if(rowIndex === 0){
    //       return {
    //         rowspan: length,
    //         colspan: 1
    //       }
    //     }else{
    //       return {
    //         rowspan: length,
    //         colspan: 0
    //       }
    //     }
    //   }
    // },
    addPresent(){
      this.presentShow = true
      this.presents = this.condition.presents
    },
    proving() {
      this.condition.discount = this.condition.discount.replace(/[^\.\d]/g, '')
      this.condition.discount = this.condition.discount.replace('.', '')
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
      if(this.itemIds !== undefined && this.itemIds !== ""){
        this.$http.stockListByitemId(this, {itemIdList: this.itemIds.split(',')}).then(res => {  
          res.data.forEach(stock => {
            for (var i = 0; i < this.condition.presents.length; i++) {
              if (this.condition.presents[i].itemId === stock.itemId) {
                this.condition.presents[i].totalNum = stock.inStockUsableCount
                break
              }
            }
          })
        })
      }
    },
    closePresentDialog() {
      this.$refs.selectPresents.handleCancel()
    },
    getPresentData(val) {
      val.forEach(item =>{
        if(item.itemId === undefined){
          item.itemId = item.id
          item.goodsId = item.goodsId
        }
      })
      this.condition.presents = []
      this.condition.presents = this.condition.presents.concat(val)
      this.presentShow = false
      this.changeItems()
      this.getItemsStock()
    },
    setPresentArr(arr){
      const obj = {}
      const temp = []
      for (let i = 0; i < this.items.length; i++) {
        const type = Object.prototype.toString.call(this.items[i].itemId)// 不加类型 分不清 1 '1'
        if (!obj[ this.items[i].itemId + type]) {
          temp.push(this.items[i])
          obj[ this.items[i].itemId + type ] = true// 这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
        }
      }
    },
    changeItems() {
      if (this.condition.presents && this.condition.presents.length > 0) {
        for (let i = 0; i < this.condition.presents.length; i++) {
          if (i === 0) {
            this.itemIds = String(this.condition.presents[i].itemId)
          } else {
            this.itemIds = this.itemIds + ',' + String(this.condition.presents[i].itemId)
          }
        }
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
    handleItemDelete(index) {
      this.condition.presents.splice(index, 1)
    },
    handleDeleteCondition(index) { // 条件删除操作
      this.$emit('deleteCondition', index)
    }
  },
  // watch: {
  //   groups: {
  //     handler() {
  //       if(this.groups &&　this.groups.length>0 && this.isFirst){
  //         this.goodsAbleTabsValue = this.groups[0].id
  //         this.isFirst = false
  //       }else if(this.groups &&　this.groups.length>0){
  //         let b = false
  //         this.groups.forEach(item =>{
  //           if(item.id === this.goodsAbleTabsValue){
  //             b = true
  //           }
  //         })
  //         if(!b){
  //           this.goodsAbleTabsValue = this.groups[this.groups.length-1].id.toString()
  //         }
  //       }
  //       // this.condition.specials = this.groups
  //     },
  //     deep: true
  //   }
  // }
}
</script>

<style lang="scss">
.rule-condition{
   border-radius: 5px;
   padding: 16px;
   margin-bottom: 5px;
   border: 1px solid #ccc;
   background-color: #ffffff;
   .el-tabs__nav {
      margin-left: 0px;
    }
   .pomotion{
    width: 100%;
    margin-bottom: 10px;
    .pomoElItem {
      margin-bottom:10px;
    }
    .condition{
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
}



.input{
  border: 0px;
  outline-style: none;
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
