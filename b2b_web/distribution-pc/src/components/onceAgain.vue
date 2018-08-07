<template>
  <div>
    <el-dialog class="activityAlls" title
      :visible.sync="activityDialogVisible"
      :closeOnClickModal="noClick"
      :lockScroll="noClick">
      <div class="rl-tc rl-padding-top-default rl-padding-bottom-default rl-text-bold">
        {{ $t("OrderSuccess.ActivityDetails") }}
      </div>
      <!-- 提示 -->
      <div class="shopcar-activity app-flex">
        <div class="shopcar-activity-img">
          <i class="el-icon-warning"></i>
        </div>
        <div class="shopCart-activity-state app-flex">
          <div class="rl-text-xxs" v-show="$i18n.locale === 'zh'">
            请确认商品参与活动的情况,若不满意，则可切换活动。确认后,当不满足条件时，系统会自动推荐活动。
          </div>
          <div class="rl-text-xxs" v-show="$i18n.locale === 'en'">
            Please confirm the participation of goods in the activity. If you
            are not satisfied, you can switch the activity. After confirmation,
            the system will automatically recommend activities when conditions
            are not met
          </div>
          <div class="shopCart-activity-state" v-if=" onWayAttendEventFlag === 0 || mtoAttendEventFlag === 0 ||customizedAttendEventFlag === 0">
            <!-- <span class="rl-text-xxs" v-if="onWayAttendEventFlag === 0">{{$t('Activitys.InTransitGoods')}}</span> -->
            <span class="rl-text-xxs" v-if="mtoAttendEventFlag === 0">{{$t("Activitys.CommoditiesAdvanceSales")}}</span>
            <span class="rl-text-xxs" v-if="customizedAttendEventFlag === 0">{{$t("Activitys.CustomizedCommodities")}}</span>
            <span class="rl-text-xxs" v-show="$i18n.locale === 'zh'">不参与活动。</span>
            <span class="rl-text-xxs" v-show="$i18n.locale === 'en'">not be involved in the activitie</span>
          </div>
        </div>
      </div>
      <div class="column-right rl-bg-white">
        <!-- 有活动的列表 -->
        <div class="right-cons" v-for="(item, index) in dataList" :key="index">
          <div class="rule-name rl-clear">
            <div class="rl-fl rl-text-xxss">
              <span>{{ item.ruleName }}</span>  
              <!-- 是否与订单同享 -->
              <span>{{ getStatus(item.isEnjoy) }}</span>
            </div>
          </div>
          <div class="rule-describe app-flex app-justify-content-space-between rl-padding-left-default rl-bg-gray-mm">
            <div class="app-flex">
              <div class="name rl-margin-right-default">
                <span v-show="$i18n.locale === 'zh'">规则详情</span>
                <span v-show="$i18n.locale === 'en'">The rules for details</span>
              </div>
              <div class="item">
                <div v-for="(value, index) in item.conditions" :key="index">
                  <span class="rl-margin-right-default">{{ $t("ShopCarts.Condition") }}{{ index + 1 }}:</span>
                  <span>{{ value.conditionName }}</span>
                  <!-- <span v-if="value.meet === true" class="chenked"></span> -->
                </div>
              </div>
            </div>
            <!-- 是否包含在途 -->
            <div @click="changeTu(zaituType)" class="zaitu rl-clear cursor-pointer rl-fr" v-if="onWayFlag">
              <span class="rl-fl rl-text-xxs" :class="{ gou: zaituType === true }"></span>
              {{ $t("P.ConTran") }}
            </div>
          </div>
          <div class="user-table">
            <el-table :data="item.children" border max-height="300"  header-row-class-name="header-row" class="activity-el-table rl-tc">
              <el-table-column :label="$t('ShopCarts.Picture')" :min-width="80">
                <template slot-scope="scope">
                  <div class="shop-img">
                    <img v-if="scope.row.goodsType == 1" :src="scope.row.imageUrl" alt />
                    <img v-if="scope.row.goodsType == 2" :src="scope.row.diy.previewImage" alt />
                  </div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('ShopCarts.ItemNo')" prop="itemCode" :min-width="80"></el-table-column>
              <el-table-column
                :label="$t('ShopCarts.ItemName')"
                prop="itemName"
                :min-width="110"></el-table-column>
              <el-table-column
                :label="$t('ShopCarts.Spe')"
                prop="specsName"
                width="90"
              ></el-table-column>
              <el-table-column :label="$t('ShopCarts.Colors')" prop="colorName" width="60"></el-table-column>
              <el-table-column
                :label="$t('P.model')"
                width="90"
              >
                <template slot-scope="scope">
                  <span>{{scope.row.diy && scope.row.diy.modelName}}</span>
                </template>
              </el-table-column>
              <el-table-column
                :label="$t('ShopCarts.Material')"
                width="90"
              >
                <template slot-scope="scope">
                  <span>{{scope.row.diy && scope.row.diy.materialName}}</span>
                </template>
              </el-table-column>
              <!-- 在途在库数量 -->
              <el-table-column :label="$t('ShopCarts.AllQuantity')" :min-width="155" :render-header="renderheader">
                <template slot-scope="scope">
                  <!-- 可视 -->
                  <template v-if="scope.row.visible">
                    <div class="app-flex app-justify-content-space-between rl-padding-left-default rl-padding-right-default">
                      <div class="rl-text-xxs">
                        {{ scope.row.zaiKuCount }}
                      </div>
                      <div class="rl-text-xxs">/</div>
                      <div class="rl-text-xxs">
                        {{ scope.row.zaiTuCount }}
                      </div>
                    </div>
                    <el-input-number v-model="scope.row.itemCount" :max="zaituType?(scope.row.inStockUsableCount+scope.row.onWayUsableCount):scope.row.inStockUsableCount" @change="handleChange($event, scope.row)" :min="0" label="描述文字" size="mini"></el-input-number>
                  </template>
                  <!-- 不可视 -->
                  <template v-else>
                    <span>已下架</span>
                  </template>
                  <!-- 批量下单 -未参加活动的才有 包装 -->
                  <!-- <div class="box-info" v-if="scope.row.boxs && scope.row.boxs.length > 0">
                    <el-popover placement="bottom" width="220" class="cursor-pointer" popper-class="box-info-table" trigger="click" :ref="refNamePopover">
                      <el-table :data="scope.row.boxs"
                        @row-click=" (row, column, e) =>handleCheck(row, column, e, scope.row)" highlight-current-row>
                        <el-table-column :label="'包装数量（件）'" prop="boxNum"></el-table-column>
                        <el-table-column :label="'单位'" prop="boxType"></el-table-column>
                      </el-table>
                      <span slot="reference">{{scope.row.choosedBoxInfo !== ""? scope.row.choosedBoxInfo: $t("P.Pieces")}}</span>
                    </el-popover>
                  </div> -->
                </template>
              </el-table-column>
              <!-- 库存 -->
              <el-table-column :label="$t('P.Inventory')" prop="numInWarehouse" width="60">
                <template slot-scope="scope">
                  <div v-if="scope.row.goodsType === 2">
                    <el-button class="mini-search-btn" @click="goCustrom(scope.row.itemCode)">{{ $t("ShopDetail.Customized") }}</el-button>
                  </div>
                  <div class="rl-text-xxs" v-else>
                    <!-- 可视 -->
                    <template v-if="scope.row.visible">
                      <span v-if="zaituType">{{ scope.row.inStockUsableCount+scope.row.onWayUsableCount }}</span>
                      <span v-else>{{ scope.row.inStockUsableCount }}</span>
                    </template>
                    <!-- 不可视 -->
                    <template v-else>
                      <span>已下架</span>
                    </template>
                  </div>
                </template>
              </el-table-column>
              <!-- 操作 -->
              <el-table-column :label="$t('ShopCarts.Operation')" :min-width="60">
                <template slot-scope="scope">
                  <div v-if="scope.row.activityList.length > 1">
                    <!-- 切换活动 -->
                    <el-button @click.native.prevent="changeActivity(item.id, scope.row)"
                      class="mini-search-btn rl-margin-bottom-xxxxs">{{ $t("ShopCarts.Switch") }}</el-button>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>  
      </div>
      <div slot="footer" class="dialog-footer">
        <!-- 取消 -->
        <el-button @click="cancelGoOrder">{{ $t("P.Cancel") }}</el-button>
        <!-- 加入购物车 -->
        <el-button :loading="loadsing" @click.native.prevent="confirmJoinShopCar"
          type="warning" >{{ $t("P.AddToCart") }}</el-button >
        <!-- 确定下单 -->
        <el-button :loading="loadsing" @click.native.prevent="confirmGoOrder"
          type="primary">{{ $t("P.ConfirmOrder") }}</el-button>
      </div>
    </el-dialog>


    <!-- 选择赠品--liu-->
    <el-dialog class="alls" :title="$t('P.ChooseGift')" :visible.sync="presentDialogVisible">
      <div v-for="(item,index) in presentList" :key="index">
        <div  class="rl-padding-left-default rl-padding-bottom-xxxs rl-padding-top-xxxs rl-bd-gray-sm">
          {{ $t("P.Receive") }} {{ item.presentCount }} {{ $t("P.MostPieces") }}，{{$t("P.HaveSelected")}}
          {{ item.hasChoose }} {{ $t("P.Pieces") }}
        </div>
        <div class="shop-table max-height300 overflow-y rl-padding-left-default rl-padding-right-default">
          <div class="rl-tc rl-padding-top-default rl-text-gray" v-if="item.presents.length === 0">
            {{ $t("P.NoData") }}
          </div>
          <table>
            <tbody>
              <tr v-for="(items, indexs) in item.presents" :key="indexs">
                <td width="120px" class="rl-text-xxs">{{ items.itemCode }}</td>
                <td width="120px" class="rl-tc">
                  <div class="songImg rl-margin-zero">
                    <img width="100%" :src="items.imageUrl1" alt />
                  </div>
                </td>
                <td width="180px" class="rl-text-xxs">
                  {{$i18n.locale === "en" &&!items.itemNameEn !== undefined &&
                    items.itemNameEn !== null &&items.itemNameEn !== ""? items.itemNameEn: items.itemName}}
                </td>
                <td width="150px">
                  <!-- <songSum ref="songSum" :songShop="items" :maxSongSum="item.presentCount" :hasChoose="item.hasChoose"></songSum> -->
                  <div class="buy-sum rl-clear rl-fr">
                    <div @click.stop.prevent="handleReduce(item,items)" class="rl-fl buyac buya rl-text-gray">-</div>
                    <div class="rl-fl buyb">
                      <input class="rl-tc" type="text"  @blur="changeNum(index,indexs,item,items)" v-model="items.itemCount" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
                    </div>
                    <div @click.stop.prevent="handleAdd(item,items)" class="rl-fl buyac buyc rl-text-gray">+</div>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="confirmSongShop">{{$t("P.OK")}}</el-button>
        <el-button @click="cacelSongShop">{{ $t("P.Cancel") }}</el-button>
      </div>
    </el-dialog>



    <!--切换活动-liu-->
    <el-dialog class="alls" :title="$t('ShopCarts.SwitchSum')" :visible.sync="changeDialogVisible">
      <div  class="change-activity rl-bdt-gray-sm max-height300 rl-padding-left-default rl-padding-right-default rl-clear">
        <div @click="changeActivityLabel(item)" v-for="(item, index) in activityList" :key="index" class="item app-flex">
          <div class="app-flex app-align-items-flex-center app-justify-content-space-between">
            <span class="chenked" :class="{ active: item.isChecked === true }"></span>
          </div>
          <div class="cons">
            <div>
              <el-button class="cons-btn">{{ item.ruleName }}</el-button>
            </div>
            <div class="rule" v-for="(value, cindex) in item.conditions" :key="cindex">
              {{ $t("ShopCarts.Condition") }}{{ cindex + 1 }}:{{ value.label }}
            </div>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer rl-margin-top-xxxs rl-tc">
        <!-- 取消 -->
        <el-button @click="changeDialogVisible = false">{{$t("P.Cancel")}}</el-button>
        <!-- 确定 -->
        <el-button @click="confirmChangeActivity" type="primary">{{$t("P.OK")}}</el-button>
      </div>
    </el-dialog>

    <!-- 配送方式选择-liu -->
    <chooseDelivery v-show="showChooseDelivery" :deliveryData="deliveryData" :values="3" @handleClose="handleCloseDelivery"></chooseDelivery>
  </div>
</template>

<script>
import Vue from 'vue';
import { formatDate,  orderHint } from '@/assets/js/common.js';
import songSum from '@/components/songSum.vue';
import { MessageBox } from 'element-ui';
import GD from '@/assets/js/globalData';
import chooseDelivery from '@/components/dialog/chooseDelivery.vue';
// liu--
import { listStockByCondition,userShopSetting,priceItemList,addShoppingcart,shoppingcartList} from '@/apiService/api'
export default {
  name: 'buyNow',
  components: { songSum, chooseDelivery },
  props: {
    orderType: {
      type: Number
    },
    activityPromotions: {
      type: Array
    },
   
    activityDialogIsVisible: {
      type: Boolean
    },
    onWayAttendEventFlag: {
      type: Number
    },
    mtoAttendEventFlag: {
      type: Number
    },
    customizedAttendEventFlag: {
      type: Number
    }
  },
  data () {
    return {
      onWayFlag:false,  //是否能勾选在途
      zaituType: false,
      presentDialogVisible: false,
      changeDialogVisible: false,
      noClick: false,
      currentType: 0, // 当前选中的商品类型 0 未选择 1 参与活动的商品 2 未参与活动的商品   
      presentGoodsList: [],
      loadsing: false,
      activityItems: [],
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: 'zh-RMB', // 语种
      showChooseDelivery: false, // 配送方式选择弹窗
      deliveryData: [], // 配送方式选择弹窗列表数据
      refNamePopover: 'popover-', // popover ref名称前缀
      presentCount: 0, //赠品最大数
      presentList:[],  //赠品列表
      activityList:[],  //活动列表
      activityItemId:'',  //选择活动对应的货品id
      dataList: [],  //表格数据
      orderhint:'',  //后台设置的下单提示
      orderDataList:[], //要下单的数据
      zaikuAndZaitu:false,  //下单中是否包含在库在途产品
      userId:'',
    };
  },
  filters: {
    keepTwoNum (value) {
      value = Number(value);
      return value.toFixed(2);
    },
    formatDate (time) {
      var date = new Date(time);
      return formatDate(date, 'yyyy-MM-dd hh:mm:ss');
    }
  },
  computed: {
    hasChoose () {
      var total = 0;
      this.presentGoodsList.forEach((item) => {
        if (item.num) {
          total += Number(item.num);
        }
      });
      return total;
    }
  },
  created: function () {
    this.userId = window.localStorage.getItem('userId');
      this.useLang = this.$b2bConfig.lang;
      this.lang = window.localStorage.getItem('bLang')
        ? window.localStorage.getItem('bLang')
        : 'zh-CNY';

      // 在途的判断
      let sysAutoDelivery= localStorage.getItem('autoDelivery')  //分销商是否是直发客户：1 是，0 否
      let sysOnWayFlag=localStorage.getItem('onWayFlag');  //分销商是否支持在途库存 1是 0否 默认是1
      let onWayFlag=0;
      //onwaySaleFlag 直发客户是否支持在途：0-否 1-是
      let onwaySaleFlag=this.activityPromotions[0].children[0].onwaySaleFlag;
      if(sysAutoDelivery==1){
          this.onWayFlag =(sysOnWayFlag==1?(onwaySaleFlag?true:false):false);
      }else{
          this.onWayFlag =sysOnWayFlag;
      } 
      this.activityDialogVisible = this.activityDialogIsVisible;
      this.getData();
      if (this.userId !== ''){
        this.userShopSettingFun();
      }
      
  },
  methods: {
    fLangChange (value) {
      window.localStorage.setItem('bLang', value);
      this.$i18n.locale = value.split('_')[0];
      this.$i18n.curren = value.slice(3, 6);
    },
    renderheader (h, { column, $index }) {
      // 表头换行
      return h('span', {}, [
        h('span', {}, column.label.split('/')[0]),
        h('br'),
        h('span', {}, column.label.split('/')[1])
      ]);
    },
    getStatus (row) {
      // 订单是否同享状态
      switch (row) {
        case 0:
          return '(与订单活动不同享)';
        case 1:
          return '(与订单活动同享)';
      }
    },
    // 获取是否 0 实际库存 1模糊库存-y  // 直发和非直发客户下单提示语--liu
    userShopSettingFun(){
      let tip=localStorage.getItem('autoDelivery');
      userShopSetting().then(res=>{
        if(res.success){  
            if((tip==1&&res.data.stiffUseHint==1)||(tip==0&&res.data.noStiffUseHint==1)){
              this.orderhint=res.data.hint;
            }   
        }
      })
    },
    // 表格数据
    getData () {
        let id=localStorage.getItem('userId');
        let ids=[];  
        this.dataList=JSON.parse(JSON.stringify(this.activityPromotions));
        this.dataList.forEach(item=>{
           item.children.forEach(childItem=>{
              ids.push(childItem.itemId);
           })
        })
        let nesIds=[...new Set(ids)];  //去重         
        listStockByCondition({distributorId: id,itemIdList : nesIds})
          .then(stockRes=>{
              if(stockRes.success){
                let stockResData=stockRes.data;
                for ( let i = 0;i < stockResData.length;i++) {
                    this.dataList.forEach(item=>{
                        item.children.forEach(items => {
                            if ( items.itemId === stockResData[i].itemId) {
                              let inStockUsableCount =stockResData[i].inStockUsableCount>0?stockResData[i].inStockUsableCount:0;// 在库库存
                              let onWayUsableCount = stockResData[i].onWayUsableCount>0?(stockResData[i].onWayUsableCount-stockResData[i].inStockUsableCount):0;// 在途库存 
                              this.$set(items,'inStockUsableCount',inStockUsableCount);
                              this.$set(items,'zaiKuCount',((inStockUsableCount>items.itemCount)?items.itemCount:inStockUsableCount));
                              this.$set(items,'onWayUsableCount',onWayUsableCount);
                              this.$set(items,'zaiTuCount',((inStockUsableCount>items.itemCount)?0:(items.itemCount-inStockUsableCount)))
                            }
                        })
                    })
                }
              }
          }) 
    },
    // 切换活动-liu
    changeActivity (ruleId, item) {
      this.changeDialogVisible = true;
      this.activityList = [];
      this.activityList=item.activityList;
      this.activityItemId=item.itemId;
    },
    // 切换活动选择-liu
    changeActivityLabel (cItem) { 
      this.activityList.forEach(val => {
        if (cItem.id === val.id) {  
          val.isChecked = true;
        } else {
          val.isChecked = false;
        }
      });
      
    },
    // 切换活动--确定--liu
    confirmChangeActivity () {   
      let hasCheck=false;
      let list= this.activityList;
      let cItem={};
      for(let i=0;i<list.length;i++){
         if(list[i].isChecked){
            cItem=list[i];
            hasCheck=true;
         }
      } 
      if (!hasCheck) {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('请选择活动切换!');
        } else {
          this.$message.warning('Please select activity switch.');
        }
        return
      }
      let goodItemList=JSON.parse(JSON.stringify(this.dataList));
      let goodItemList2=[];
      let goodItemList3=[];
      let goodItemList4=[];
      let newGoodItemList=[];
      this.dataList=[];
      goodItemList.forEach(item=>{
        item.children.forEach(items=>{
           if(this.activityItemId==items.itemId){
              this.$set(items,'ruleName',cItem.ruleName);
              if(cItem.ruleType==1){  //活动类型  1：促销 2：拼团
                 this.$set(items,'goodsPromotionId',cItem.rulesId);
                 this.$set(items,'groupSeckillId','');
                 this.$set(items,'conditions',cItem.conditions)
              }else{
                 this.$set(items,'groupSeckillId',cItem.rulesId);
                 this.$set(items,'goodsPromotionId','');
                 this.$set(items,'conditions',[])
              }  
           }
           if(items.groupSeckillId&&items.groupSeckillId!=''){
              goodItemList4.push(items);
           }

           if(items.goodsPromotionId&&items.goodsPromotionId!=''&&items.goodsPromotionId!='no'){
              goodItemList2.push(items);
           }
           if(items.goodsPromotionId=='no'){
             goodItemList3.push(items);
           }
           newGoodItemList=[...goodItemList2,...goodItemList3]
        })
      })
      // 拼团秒杀  
      if(goodItemList4.length>0){ 
          let newGroup=  this.sort_pro(goodItemList4 ,['groupSeckillId']);
          newGroup.forEach(grItem=>{
              this.$set(grItem,'ruleName',grItem.children[0].ruleName);
              this.$set(grItem,'conditions',[]);
          })
          this.dataList=[...this.dataList,...newGroup];
            
      }
      if(newGoodItemList.length>0){
          let newList=this.sort_pro(newGoodItemList ,['goodsPromotionId']);
          newList.forEach(newItem=>{
              if(newItem.goodsPromotionId!='no'){
                  this.$set(newItem,'ruleName',newItem.children[0].ruleName);
                  this.$set(newItem,'conditions',newItem.children[0].conditions);
              }else{
                  this.$set(newItem,'未参加活动',newItem.children[0].ruleName);
                  this.$set(newItem,'conditions',[]);
              }
              
          })
          this.dataList=[...this.dataList,...newList];
      }

      this.changeDialogVisible = false;
      console.log('切换活动后的货品列表：',this.dataList);
    },
    // 把对象数组按照某一个属性（或某几个属性）进行分类-liu
    sort_pro(data, keys = []) {     //keys可以传一个数组
      var c = [];
      var d = {};
      for (var element of data) {
        let element_keyStr = "";
        let element_key = [];
        let element_keyObj = {};
        for (var key of keys) {
          element_key.push(element[key]);
          element_keyObj[key] = element[key];
        }
        element_keyStr = element_key.join("_");
        if (!d[element_keyStr]) {
          c.push({
            ...element_keyObj,
            children: [element]
          });
          d[element_keyStr] = element;
        } else {
          for (var ele of c) {
            let isTrue = keys.some(key => {
              return ele[key] != element[key];
            });
            if (!isTrue) {
              ele.children.push(element);
            }
          }
        }
      }
      return c;
    },
     // 在途在库切换
    changeTu (type) {
     
      this.$forceUpdate(); // 更新页面数据
      if (type === true) {
        this.zaituType = false;
      } else {
        this.zaituType = true;
      }
    },

    // 货品数量 加减---liu
    handleChange (val, item) {
      
       if(item.itemType==2){return};
       let count=val;
        //普通商品
        if(item.goodsType==1){  
          
          if(item.inStockUsableCount>=count){
              this.$set(item,'zaiKuCount',count);
              this.$set(item,'zaiTuCount',0);
              this.$set(item,'conditionsId',''); 
          }else{  
            if(item.onWayUsableCount>(count-item.inStockUsableCount)){
                this.$set(item,'zaiKuCount',item.inStockUsableCount);
                this.$set(item,'zaiTuCount',(count-item.inStockUsableCount));
                this.$set(item,'conditionsId',''); 
              }else{
                  this.$message.warning("购买数量不能大于库存!");
                  return
              }
          }
        }else{  //定制商品
            if(item.diyUnderStockFlag==1){  //不缺货
                this.$set(item,'zaiKuCount',count);
                this.$set(item,'zaiTuCount',0);       
            }else{  //缺货
              this.$message.warning("购买数量不能大于库存!");
              return
            }
        }
    },
    
    // 活动取消-liu
    cancelGoOrder () {
      this.activityDialogVisible = false;
      this.$emit('shutDialog', true);
    },

     // 加入购物车-liu
    confirmJoinShopCar () {
      let orderGood=[];
      let list=this.dataList;
      list.forEach(item=>{
         item.children.forEach(items=>{
           if(items.itemCount>0){           
                orderGood.push(items);
            }
         })
      })
      let paramsList=[];
        orderGood.forEach(item=>{
          // 判断是否可视visible
          if (item.visible) {
            let objL={
              imageUrl:item.imageUrl,
              cartType:1,  //加购类型：1 分销商 2 C端客户
              diyType:item.diyType,  //定制类型 0-标准定制 1-DIY定制
              goodsId:item.goodsId, //商品id
              goodsName:item.goodsName,
              goodsNo:item.goodsNo,  //商品编码
              goodsPromotionId:item.goodsPromotionId&&item.goodsPromotionId!='no'?item.goodsPromotionId:'',   //商品促销活动Id(活动条件id)
              goodsType:item.goodsType,  //商品类型 1-普通 2-定制
              groupSeckillId:item.groupSeckillId?item.groupSeckillId:'',
              itemCode:item.itemCode,  //货品编码
              itemCount:item.itemCount,  //购物数量
              itemId:item.itemId,  //货品id
              itemName:item.itemName,  //货品名称
              itemNameEn:item.itemNameEn?item.itemNameEn:'',  //货品英文名称
              itemType:item.itemType,  //是否赠品 1 非赠品 2 赠品
              specsName:item.specsName,  //货品规格
              colorName:item.colorName,  //货品颜色
              weight:item.weight,  //重量
              lenght:item.length,
              width:item.width,
              height:item.height,
              barCode:item.barCode,
            }
            if (item.goodsType == 2) {
              objL.diy = item.diy;
            }
            paramsList.push(objL); 
          }
        })
        if(paramsList.length>0){ 
          addShoppingcart(paramsList).then(res=>{
            if(res.success){
              if (this.$i18n.locale === 'zh') {
                this.$message.success('加入购物车成功');
              } else {
                this.$message.success('Add to shopping cart successfully.');
              }
              this.loadsing = false;
              this.activityDialogVisible = false;
              this.$emit('shutDialog', true);
              this.$parent.shoppingcartListFun();
              this.shoppingcartListFun();  //通过查购物车列表的长度来更新购物车的数量
            }else{
              this.$message(res.errMessage);
            }
          })
        }else{
          //  货品订购数量不能为空
           this.$message.warning(this.$t('P.GoodsItemNull'));
        }
     
    },
    
    // 确认下单--liu
    confirmGoOrder(){
       console.log('货品列表',this.dataList);
       let list=this.dataList;
       let orderGood=[];
       let isAllType=false;  //货品在库在途是否同时存在
       let zaiKuAllType=false;
       let zaiTuAllType=false;
       let presentList=[];
       this.presentList = [];
       list.forEach(item=>{
         item.children.forEach(items=>{
           // 判断数量itemCount 是否可视visible
           if(items.itemCount>0 && items.visible){
                let totalPrice=items.salePrice*items.itemCount;
                // 计算促销价
                if(items.goodsPromotionId&&items.goodsPromotionId!='no'){
                  items.promotions.forEach(proItem=>{
                      let onWayFlag=proItem.onWayFlag;  //在途商品是否参与活动 1.参与，0.不参与
                      let promoStatus=proItem.promoStatus; //促销状态：0 未开始, 1 促销中 2 已过期 3 提前结束
                      let promoType=proItem.promoType;  //活动类型，1 普通活动，2 阶梯活动
                      proItem.rules.forEach(ruleItem=>{
                        if(ruleItem.id==items.goodsPromotionId){
                          let isOneBuyCount=onWayFlag==0?items.zaiKuCount:(items.zaiKuCount+items.zaiTuCount);
                          let isOneBuyMoney=onWayFlag==0?(items.zaiKuCount*items.salePrice):(items.itemCount*items.scaleprice);
                          ruleItem.conditions.forEach(cItem=>{
                                if(promoStatus==1){
                                    //moneyOrCount 规则形式：1金额 2数量
                                    if((ruleItem.moneyOrCount==1&&isOneBuyMoney>=cItem.oneBuyMoney)||(ruleItem.moneyOrCount==2&&isOneBuyCount>=cItem.oneBuyCount)){
                                      this.$set(items,'conditionsId',cItem.id);
                                      // specialFlag 是否特价，1是 0否
                                      if(cItem.specialFlag==1){
                                      totalPrice=onWayFlag==0
                                        ?((items.zaiKuCount*cItem.specialPrice)+items.zaiTuCount*items.salePrice)
                                        :((items.zaiKuCount+cItem.specialPrice)*cItem.specialPrice)
                                      }else{
                                          //计算叠加的数量
                                          let dieJiaNum=ruleItem.moneyOrCount==1?parseInt(isOneBuyMoney/cItem.oneBuyMoney):parseInt(isOneBuyCount/cItem.oneBuyCount); 
                                          //reduceOrPresent 促销统计方式：1满减 2满赠
                                          if(cItem.reduceOrPresent==1){
                                            // reduceType 满减类型， 2折扣  1减免
                                            if(cItem.reduceType==2){
                                              totalPrice=onWayFlag==0
                                              ?((items.zaiKuCount*items.salePrice*cItem.discount/100)+items.zaiTuCount*items.salePrice)
                                              :((items.zaiKuCount+items.zaiTuCount)*items.salePrice*cItem.discount/100);
                                            }else{ 
                                              // 判断是否叠加的减免金额
                                              let reduction=cItem.reductionPresentAddFlag==1?(dieJiaNum*cItem.reduction):cItem.reduction;
                                              totalPrice=onWayFlag==0
                                              ?((items.zaiKuCount*items.salePrice-reduction)+items.zaiTuCount*items.salePrice)
                                              :((items.zaiKuCount+items.zaiTuCount)*items.salePrice-reduction);
                                            }
                                          }else{
                                            if(cItem.presents&&cItem.presents.length>0){
                                              // let presentCount= cItem.reductionPresentAddFlag==1?((dieJiaNum*presentCount>cItem.presents[0].count)?dieJiaNum*presentCount:cItem.presents[0].count):presentCount;
                                              
                                              // 是否叠加 reductionPresentAddFlag  满减满赠是否叠加，1是 0否
                                              let itemCountP =
                                                onWayFlag == 0 ? items.zaiKuCount : items.itemCount;
                                              let parseIntCount =
                                                ruleItem.moneyOrCount == 1
                                                  ? parseInt(
                                                      (itemCountP * items.salePrice) /
                                                        cItem.oneBuyMoney
                                                    ) > 0
                                                    ? parseInt(
                                                        (itemCountP * items.salePrice) /
                                                          cItem.oneBuyMoney
                                                      )
                                                    : 0
                                                  : parseInt(itemCountP / cItem.oneBuyCount) > 0
                                                  ? parseInt(itemCountP / cItem.oneBuyCount)
                                                  : 1;
                                                  
                                              let totalPresentCount3 =
                                                cItem.reductionPresentAddFlag == 1
                                                  ? parseIntCount * cItem.presentCount
                                                  : cItem.presentCount;

                                              let totalPresentCount2 =
                                                totalPresentCount3 > cItem.presents[0].count
                                                  ? cItem.presents[0].count
                                                  : totalPresentCount3;

                                              // 判断是否叠加
                                              if (
                                                cItem.reductionPresentAddFlag == 1 &&
                                                ruleItem.moneyOrCount == 1
                                              ) {
                                                // 计算叠加的数量（金额）
                                                let dieJiaNum = parseInt(
                                                  isOneBuyMoney / cItem.oneBuyMoney
                                                );
                                                totalPresentCount2 = cItem.presentCount * dieJiaNum;
                                              }
                                              
                                              cItem.presents.forEach(presentItem=>{
                                                this.$set(presentItem,'itemCount',0);
                                                this.$set(presentItem,'conditionsId',cItem.id);
                                                this.$set(presentItem,'goodsPromotionId',ruleItem.id);
                                                this.$set(presentItem,'itemType',2);
                                                this.$set(presentItem,'goodsType',items.goodsType);
                                              })
                                              let objPresent={
                                                // presentCount:presentCount,
                                                presentCount:totalPresentCount2,
                                                hasChoose:0,
                                                presents:cItem.presents
                                              }
                                              this.presentList.push(objPresent);
                                            }
                                          }
                                      }
                                  }else{
                                      this.$set(items,'goodsPromotionId','no');
                                  }
                                }
                          })
                        }
                      })
                  })
                }
                // 计算拼团价
                if(items.groupSeckillId&&items.groupSeckillId!=''){
                  items.groupSeckills.forEach(groupItem=>{
                    if(items.groupSeckillId==groupItem.groupSeckillId){
                      if(items.itemCount>groupItem.minNum){
                        totalPrice=items.itemCount*groupItem.groupSeckillPrice;
                        this.$set(items,'mtoFlag',goodItem.groupSeckills.mtoFlag);   // 是否支持预售：0-否 1-是  
                      }else{
                        this.$set(items,'groupSeckillId','')
                      }
                    }
                  })
                }
                console.log('jiage价格：--',totalPrice);
                this.$set(items,'totalPrice',totalPrice);
                if(items.zaiKuCount>0){
                    zaiKuAllType=true;
                }
                if(items.zaiTuCount>0){
                    zaiTuAllType=true;
                }
                orderGood.push(items);
                this.orderDataList.push(items);
            }
         })
       })

       isAllType=(zaiKuAllType&zaiTuAllType)?true:false;
       this.zaikuAndZaitu=isAllType;
       this.deliveryData=orderGood;  //配送列表数据
       
        // 赠品
        if(this.presentList.length>0){
           let ids=[];
           let priceIds=[];
           this.presentList.forEach(preItem=>{
              preItem.presents.forEach(preItems=>{
                let itemsIdObj={
                  goodsId:preItems.goodsId,  //商品id
                  itemId:preItems.itemId   //货品id
                }
                ids.push(preItems.itemId);
                priceIds.push(itemsIdObj);
              })
           })
           let itemIds=[...new Set(ids)];
           // 获取货品价格
          let paramsPrice={
            goodsItemIds:priceIds
          }
          priceItemList(paramsPrice).then(priceRes=>{
              if(priceRes.success){
                let priceList=priceRes.data;
                this.presentList.forEach(item=>{
                  item.presents.forEach(item2=>{
                      priceList.forEach(items=>{
                        if(items.itemId==item2.itemId){
                          this.$set(item2,'salePrice',items.salePrice);
                        }
                      })
                  })
                })
              }
          })
          // 获取商品库存
          let params={
              distributorId: localStorage.getItem('userId'),
              itemIdList : itemIds
          };
          
          listStockByCondition(params).then((stockRes) => {
                if (stockRes.success) {
                  let stockResData=stockRes.data;
                  for ( let i = 0;i < stockResData.length;i++) {
                    this.presentList.forEach((items) => {
                       items.presents.forEach(item2=>{
                        if ( item2.itemId === stockResData[i].itemId) {   
                          let inStockUsableCount =stockResData[i].inStockUsableCount>0?stockResData[i].inStockUsableCount:0;// 在库库存
                          let onWayUsableCount = stockResData[i].numOnWaySum>0?stockResData[i].numOnWaySum:0;// 在途库存
                          this.$set(item2,'inStockUsableCount',inStockUsableCount);
                          this.$set(item2,'onWayUsableCount',onWayUsableCount); 
                        }
                      })
                    });
                  }
                }
          });
          console.log('赠品列表：---',this.presentList);
           this.presentDialogVisible=true;
           return  false;   
        }
        
        console.log('要下单的货品：',orderGood);
        console.log('是否包含在途在库:', isAllType);
        if(orderGood.length==0){
           this.$message.info(this.$t('ShopCarts.SelectProductsHint'));
           this.showLoading = false;
           return false;
        }
        //在库在途同时存在
        if(isAllType){   
           this.activityDialogVisible=false;
           this.showChooseDelivery=true;  //配送方式弹框 
        }else{
          // 如果后台设置计算提示，则弹出
          if(this.orderhint!=''){
            MessageBox.confirm(this.orderhint, '提示', {
              confirmButtonText: '确定提交',
              cancelButtonText: '我再想想',
              customClass: 'orderHint'
            }).then(() => {
               localStorage.setItem('shopOrderList',JSON.stringify(orderGood) );
               this.$router.push({name: 'ConsigneeInfor',query: {values: 1,onWaySplitFlag: 0,isTwoWay:0,}});  //跳转下单页；
            })
          }else{
            localStorage.setItem('shopOrderList',JSON.stringify(orderGood));
            this.$router.push({name: 'ConsigneeInfor',query: {values: 1,onWaySplitFlag: 0,isTwoWay:0,}});  //跳转下单页；
          }
        }
    },
    // 赠品减法--liu
    handleReduce(item,items){
      console.log(items.itemCount);
      if(items.itemCount>0){
        this.$set(items,'itemCount',Number(items.itemCount)-1);
        this.$set(item,'hasChoose',(item.hasChoose-1))
      }
      
    },
    // 赠品加法--liu
    handleAdd(item,items){
        console.log(items.itemCount);
        let syNum=item.presentCount-item.hasChoose;
        if(items.itemCount>(items.inStockUsableCount+items.onWayUsableCount)){
            this.$message.warning('库存不足');
            return
        }
        if(syNum>0){     
             this.$set(item,'hasChoose',(item.hasChoose+1))
             this.$set(items,'itemCount',(Number(items.itemCount)+1));   
        }else{
          this.$message.warning(this.$t("Activitys.UpperLimitGifts"));
        }
    },
    // 赠品input输入的算法--liu
    changeNum(index,indexs,item,items){
        let totalChoose=0;
        let hasNum=0;
        if(items.itemCount>(items.inStockUsableCount+items.onWayUsableCount)){
            this.$message.warning('库存不足');
            return
        }
        this.presentList[index].presents.forEach((items2,indexs2)=>{
            totalChoose+= Number(items2.itemCount) ;
            if(indexs!=indexs2){
              hasNum+= Number(items2.itemCount);
            }
        })
        if(totalChoose>this.presentList[index].presentCount){
            this.$set(items,'itemCount',0);
            this.$set(item,'hasChoose',hasNum);
            this.$message.warning(this.$t("Activitys.UpperLimitGifts"));
        }else{
            this.$set(item,'hasChoose',totalChoose); 
        }
    },
    // 赠品--确定--liu
    confirmSongShop () {
      let tempArray = [];
      let orderList=[];
      let zaiKuFlag=false;
      let zaiTuFlag=false;
      
      this.presentList.forEach(item=>{
        item.presents.forEach(items=>{
           if(items.itemCount>0){
              orderList.push(items);
              this.$set(items,'zaiKuCount',((items.itemCount<=items.inStockUsableCount)?items.itemCount:items.inStockUsableCount));
              this.$set(items,'zaiTuCount',((items.itemCount<=items.inStockUsableCount)?0:items.itemCount-items.inStockUsableCount)); 
              this.$set(items,'totalPrice',(items.itemCount*items.salePrice));  
              zaiKuFlag=(items.zaiKuCount>0)?true:false;
              zaiTuFlag=(items.zaiTuCount>0)?true:false;
           }
        })
      })
      let twoFlag=(zaiKuFlag&&zaiTuFlag)?true:false;
      if (orderList.length == 0) {
        let info = '';
        let infoTwo = '';
        if (this.$i18n.locale === 'zh') {
          info = '您还未选择赠品，可以取消重新选择赠品';
          infoTwo = '确定不需要赠品';
        } else {
          info ='You have not chosen the gift yet. You can cancel and re-choose the gift';
          infoTwo = 'make sure you do not need the gift.';
        }
        MessageBox.confirm(info, infoTwo, {
          confirmButtonText: this.$t('P.OK'),
          cancelButtonText: this.$t('P.Cancel'),
          type: 'warning'
        })
        .then(() => {
          this.presentDialogVisible = false;  
          this.handleChooseDelivery(this.zaikuAndZaitu);    
        }) 
        return false;
      }else{
          this.orderDataList=[...this.orderDataList,...orderList]; 
          console.log('赠品整合后的集合数据：',this.orderDataList)
          this.deliveryData=this.orderDataList; 
          let flag=(twoFlag||this.zaikuAndZaitu)?true:false;
          this.handleChooseDelivery(flag);
      }
     
      
    },
    // 赠品取消--liu
    cacelSongShop () {
      this.presentDialogVisible = false;
      this.handleChooseDelivery(this.zaikuAndZaitu); 
    },
    //赠品-- 配送方式选择弹窗显示-liu
    handleChooseDelivery (flag) {
       this.showLoading = false;
       if(flag){
           this.activityDialogVisible=false;
           this.showChooseDelivery=true;  //配送方式弹框 
       }else{
          // 如果后台设置计算提示，则弹出
          if(this.orderhint!=''){
            MessageBox.confirm(this.orderhint, '提示', {
              confirmButtonText: '确定提交',
              cancelButtonText: '我再想想',
              customClass: 'orderHint'
            }).then(() => {
               localStorage.setItem('shopOrderList',JSON.stringify(this.orderDataList) );
               this.$router.push({name: 'ConsigneeInfor',query: {values: 1,onWaySplitFlag: 0,isTwoWay:0,}});  //跳转下单页；
            })
          }else{
            localStorage.setItem('shopOrderList',JSON.stringify(this.orderDataList));
            this.$router.push({name: 'ConsigneeInfor',query: {values: 1,onWaySplitFlag: 0,isTwoWay:0,}});  //跳转下单页；
          }
       }
    },
    //赠品-- 配送方式选择弹窗关闭--liu
    handleCloseDelivery () {
      this.showChooseDelivery = false;
      this.showLoading = false;
    },



  
   
    
    
    // 批量下单 - 包装
    handleCheck (row, column, e, item) {
      this.$set(item, 'choosedBoxInfo', row.boxType);
      this.$set(item, 'choosedBoxNum', row.boxNum);
      this.$set(item, 'boxId', row.id);
      let refName = this.refNamePopover;
      this.$refs[refName].doClose();
    }
  },
 
};
</script>
<style lang='less'>
.activityAlls {
  overflow: visible;
  top: -12%;
  .el-dialog {
    width: 900px !important;
    max-height: 80%;
    overflow-y: scroll;
    .el-dialog__header {
      display: none;
    }
    .el-dialog__body {
      padding: 0;
      padding-left: 20px;
      padding-right: 20px;
      padding-bottom: 10px;
    }
    .el-dialog__footer {
      text-align: center;
    }
  }
}
</style>
<style scoped="scoped" lang='less'>
    @import url("../assets/less/variable.less");
    /*活动*/
    .overflow-y {
      overflow-y: scroll;
    }
    .shopcar-activity {
      padding-top: 5px;
      padding-bottom: 3px;
      padding-left: 20px;
      margin-bottom: 7px;
      background-color: #e5f9fb;
      display: flex;
      justify-content: start;
      .shopcar-activity-img {
        margin-right: 10px;
        font-size: 22px;
        color: @orange;
        vertical-align: middle;
      }
      .shopCart-activity-state {
        vertical-align: middle;
        display: inline-block;
        font-size: 12px;
      }
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
        height: 100%;
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
            background: url(../../src/assets/images/selected.png) no-repeat center
              center;
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
    .shop-img {
      position: relative;
      height: 55px;
      img {
        position: absolute;
        top: 50%;
        left: 50%;
        max-width: 100%;
        max-height: 100%;
        transform: translate(-50%, -50%);
      }
    }
    /*选择赠品*/
    .max-height300 {
      max-height: 300px;
    }
    .shop-table {
      margin-bottom: 20px;
      table {
        tr {
          td {
            height: 45px;
            text-align: center;
            border-bottom: 1px solid #ccc;
            .buy-sum {
              width: 92px;
              height: 22px;
              line-height: 22px;
              border: 1px solid #ebeff5;
              div {
                height: 22px;
                box-sizing: border-box;
                background-color: #fff;
                input {
                  width: 62px;
                }
              }
              .buyac {
                width: 22px;
                font-size: 22px;
                color: #9b9b9b;
                cursor: pointer;
                text-align: center;
              }
              .buyb {
                width: 48px;
                line-height: 22px;
                color: #3a3a3a;
                border-left: 1px solid #ebeff5;
                border-right: 1px solid #ebeff5;
                input {
                  width: 46px;
                }
              }
            }
            .songImg {
              width: 50px;
              img {
                margin-top: 5px;
                height: 50px;
              }
            }
          }
        }
      }
    }
    .zaitu {
      padding-right: 20px;
      span {
        display: block;
        width: 16px;
        height: 20px;
        background: url("../assets/images/choose.png") no-repeat center center;
      }
      span.gou {
        background: url("../assets/images/gou.png") no-repeat center center;
      }
    }
    .mini-search-btn {
      font-size: 12px;
      padding: 5px;
    }
    /*切换活动*/
    .change-activity {
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
        .chenked.active {
          background: url(../../src/assets/images/yuan4.png) no-repeat center center;
        }
        .cons {
          width: 100%;
          margin-left: 10px;
          padding-top: 10px;
          padding-bottom: 10px;
          padding-left: 20px;
          padding-right: 20px;
          border: 1px solid #ccc;
          .cons-btn {
            font-size: 12px;
            padding: 5px;
          }
          .rule {
            line-height: 30px;
            font-size: 12px;
          }
        }
      }
    }

    // 批量下单 - 包装
    .box-info {
      display: inline-block;
      position: relative;
      top: 2px;
      padding: 0 2px;
      min-width: 16px;
      min-height: 18px;
      font-size: 12px;
      color: @blue;
      text-align: center;
      line-height: 18px;
      border: 1px solid @blue;
      border-radius: 22px;
      cursor: pointer;
    }
</style>
