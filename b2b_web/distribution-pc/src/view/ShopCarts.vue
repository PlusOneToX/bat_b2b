<template>
  <div>
    <div class="user rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState" :shopCarPage="shopCarPage" :totalCount="totalCount"></Header>
      <!--二级导航-->
      <div class="shop-car rl-margin-zero">
        <div class="rl-text-xxs bleed rl-padding-top-xxxs rl-padding-bottom-xxxs rl-bg-gray-mm">
          <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ name: 'Index' }">{{
              $t("P.HomePage")
            }}</el-breadcrumb-item>
            <el-breadcrumb-item>{{
              $t("ShopCarts.ShopCar")
            }}</el-breadcrumb-item>
            <el-breadcrumb-item>{{
              $t("ShopCarts.HaveBeenAdded")
            }}</el-breadcrumb-item> 
          </el-breadcrumb>
        </div>
      </div>
      
      <div class="user-main rl-clear rl-padding-top-xxxs rl-margin-zero" v-if="shopCartList.length !== 0 ">
        <!-- 系统提示 -->
        <div class="shopcar-activity rl-clear">
          <div class="shopcar-activity-img rl-fl">
            <i class="el-icon-warning"></i>
          </div>
          <div class="shopCart-activity-state rl-fl">
            <p v-show="$i18n.locale === 'zh'">
              系统支持自由选择货品参与活动，当不满足活动条件时，不参与活动。若与订单活动同享，则自动计算优惠；若不同享，请自主决定参与的活动。
            </p>
            <p v-show="$i18n.locale === 'en'">
              You can choose sales campaign for goods added to cart as required.
              System will automatically convert the discount when meeting
              conditions.
            </p>
          </div>
        </div>
        <!-- 列表 -->
        <div class="cats-box">
          <catsComponent v-if="allShopingCartList.length !== 0" :onWayAttendEventFlag="onWayAttendEventFlag"
            :shopCartData="allShopingCartList"></catsComponent> 
        </div>
        <div class="shop-operate rl-clear rl-padding-right-double rl-padding-left-default rl-padding-top-xxxs rl-padding-bottom-xxxs rl-bdb-gray-sm rl-bg-white">
          <div class="rl-fl operate">
            <!-- 更新购物车 -->
            <div @click="updateShopCar" class="rl-fl rl-text-mid rl-margin-right-double cursor-pointer">
              {{ $t("ShopCarts.Update") }}
            </div>
            <!-- 批量删除 -->
            <div @click="batchDelete" class="rl-fl rl-text-mid rl-margin-right-double rl-text-orange-sm cursor-pointer">
              {{ $t("ShopCarts.BatDelete") }}
            </div>
            <!-- 清空购物车 -->
            <div @click="emptyShopCar" class="rl-fl rl-text-mid rl-text-orange-sm cursor-pointer">
              {{ $t("ShopCarts.Empty") }}
            </div>
          </div>
        </div>
        <div class="shop-foot rl-padding-left-double rl-bg-white">
          <div class="rl-fl">
            <!-- 继续购物 -->
            <div @click="goWalkingAround" class="rl-text-mid rl-margin-right-double cursor-pointer">
              {{ $t("ShopCarts.ConShopping") }}
            </div>
          </div>
          <div class="rl-fl rl-margin-left-default">
            <div class="rl-padding-left-default rl-text-xxss"> 
              {{ $t("ShopCarts.TotalWeight") }}：{{ totalWeight | keepTwoNum }}<!-- 商品总重 -->
              <span>{{ $t("ShopCarts.ke") }}</span>
              ，{{ $t("ShopCarts.TotalSuch") }}：
              <span>（{{ $t("ShopCarts.ShopSum") }}：{{ totalCount }}）</span> <!-- 商品总数量-->
              <span v-show="$root.currency === 'CNY'" class="rl-text-orange-sm">￥{{ totalAllPrice | keepTwoNum }}</span><!-- 商品总金额-中文 -->
              <span v-show="$root.currency === 'USD'"
                class="rl-text-orange-sm">${{ Number(totalAllPrice)*exchange | keepTwoNum }}</span> <!-- 商品总金额--英文 -->
            </div>
          </div>
          <!-- 结账 -->
          <div @click="goTheOrder" class="rl-fr settle rl-bg-orange-mm rl-tc rl-text-white cursor-pointer">
            {{ $t("ShopCarts.Checkout") }}
          </div>
        </div>
      </div>
      <!--购物车为空-y-->
      <div class="empty-car rl-margin-zero" v-if="allShopingCartList.length === 0">
        <div class="empty-car-img rl-margin-zero">
          <img width="100%" src="../assets/images/empty-car.png" alt />
        </div>
        <p class="rl-tc rl-margin-top-default rl-margin-bottom-default rl-text-sm rl-text-gray">
          {{ $t("ShopCarts.ShopcarEmpty") }}
        </p>
        <div class="go-around rl-margin-zero">
          <span @click="walkingAround"
            class="rl-bd-gray-sm rl-tc rl-text-mid rl-text-gray cursor-pointer">{{ $t("ShopCarts.Around") }}</span>
        </div>
      </div>
    </div>
    <!--加载动画-->
    <loading v-if="this.showLoading === true"></loading>

    <!-- 配送方式选择-Y -->
    <chooseDelivery v-show="showChooseDelivery" :deliveryData="orderGoodList" :values="1" :orderhint="orderhint" @handleClose="handleClose">
    </chooseDelivery>

  </div>
</template> 

<script>
  import Vue from 'vue';
  import { MessageBox} from 'element-ui';
  import Header from '@/components/Header.vue';
  import { orderHint,fomatFloat} from '@/assets/js/common.js';
  import catsComponent from '@/components/catsComponent.vue';
  import loading from '@/components/loading.vue';
  import GD from '@/assets/js/globalData';
  import chooseDelivery from '@/components/dialog/chooseDelivery.vue';

  // liu
  import {shoppingcartList,listStockByCondition,deleteShoppingcart,userShopSetting,diyGetByModelIdAndMaterialId} from '@/apiService/api'

  
  export default {
    name: 'ShopCarts',
    components: {
      Header: (resolve) => require(['@/components/Header.vue'], resolve),
      // Header,
      catsComponent,
      loading,
      chooseDelivery
    },
    data () {
      return {
        //liu -start
        
         orderhint:'',//下单提示语
         shopCarPage: true,
         userState: 2,
         showLoading: false,
         
         shopCartList:{
           allCkeck:false,
           diyType:0,
           goodsType:1,
           list:[],
           promoList:[],  //有活动的货品
           groundList:[],
         },  //普通购物车列表
         
         dzShopCartList:{
           allCkeck:false,
           goodsType:2,
           list:[],
         },  //定制购物车列表
         allShopingCartList:[],  //所有列表
         onWayAttendEventFlag: 0, // 在途商品是否参与活动 1.参与，0.不参与
         orderGoodList:[],  //下单的货品组合
        //liu -end
        orderAgain: 0, // 是否来自再下一单
        orderAgainType: 0,
        orderItem: [], // 来自再下一单货品
        shopCartCommomList: {}, // 普通商品列表
        shopCartnewCustormlList: {}, // 新定制商品列表
        
        groupFlag: '',
        useLang: false, // 是否使用多语种
        langList: GD.langList, // 语种列表
        lang: 'zh-RMB', // 语种
        interval: null, // 拼团倒计时
        showChooseDelivery: false, // 配送方式选择弹窗
        deliveryData: [], // 配送方式选择弹窗列表数据
        exchange:1, //汇率
      };
    },
    filters: {
      keepTwoNum (value) {
        value = Number(value);
        return value.toFixed(2);
      },
      keepTwoHead (value) {
        return value.substr(0, 2);
      },
      deleteFivHead (value) {
        return value.slice(5);
      }
    },
    computed: {
      // 总数-Y
      totalCount () {
        var total = 0;
        this.allShopingCartList.forEach(item=>{
          item.list.forEach(item2=>{
            item2.children.forEach(item3=>{
              if(item3.isCheck){
                 total+=item3.zaiKuCount+item3.zaiTuCount;
              }
            })
          })
        })
        
        return total;
      },
      // 总金额-y
      totalAllPrice () {
        var total = 0;
        this.allShopingCartList.forEach(item=>{
          item.list.forEach(item2=>{
            item2.children.forEach(item3=>{
              if(item3.isCheck){
                 total+=item3.totalPrice;
              }
            })
          })
        })
        
        return total;
      },
      
      // 总重量--Y
      totalWeight () {
        console.log('zongj---',this.allShopingCartList);
        var total = 0;
        this.allShopingCartList.forEach(item=>{
          item.list.forEach(item2=>{
            item2.children.forEach(item3=>{
              if(item3.isCheck){
                 total+=((item3.weight!=0?item3.weight:1)*(item3.zaiKuCount+item3.zaiTuCount));
              }
            })
          })
        })
        return total;
      }
    },
    mounted(){
      var userId = window.localStorage.getItem('userId');
      if(userId&&userId!=''&&userId!='undefined'){
         this.userShopSettingFun();  //获取系统配置
      }
       
    },
    methods: {
      fLangChange (value) {
        window.localStorage.setItem('bLang', value);
        this.$i18n.locale = value.split('-')[0];
      },
       
      // 获取是否 0 实际库存 1模糊库存-y  // 直发和非直发客户下单提示语
      userShopSettingFun(){
        let tip=localStorage.getItem('autoDelivery');
        userShopSetting().then(res=>{
          if(res.success){
              this.stockShowFlag = res.data.stockShowFlag;
              this.stockShowNumber = res.data.stockShowNumber;
              if((tip==1&&res.data.stiffUseHint==1)||(tip==0&&res.data.noStiffUseHint==1)){
                this.orderhint=res.data.hint;
              }
              
          }
        })
      },
       // 购物车货品列表-y
      async getShopCarList(){  
         this.showLoading = true;
         this.shopCartList={
           allCkeck:false,
           diyType:0,
           goodsType:1,
           list:[],
           promoList:[], 
           groundList:[],
         };
         this.dzShopCartList={
           allCkeck:false,
           goodsType:2,
           list:[],
         };
        
         this.allShopingCartList=[];  
         let res=await  shoppingcartList(); //购物车列表接口
         let ids=[]; 
          if(res.success){
              let list=res.data;
              for(let i=0;i<list.length;i++){
                  if(list[i].goodsType==1){  //标品
                    ids.push(list[i].itemId)
                  }else{  //定制
                     console.log('定制货品：',list[i]);
                     if(list[i].diy){
                      let diyParams={
                        materialId:list[i].diy.materialId,  //材质id
                        modelId:list[i].diy.modelId,  //型号id
                      }
                      let diyStockRes=await diyGetByModelIdAndMaterialId(diyParams);
                      console.log('定制库存',diyStockRes.data);
                      if(diyStockRes.success){
                          let diyStockData=diyStockRes.data;
                          // openFlag 状态值 1、启用 0、禁用  underStockFlag  是否缺货 0否 1是
                          this.$set(list[i],'diyUnderStockFlag',((diyStockData.openFlag==1&&diyStockData.underStockFlag==0)?1:0));  
                          //定制添加是否缺货  diyUnderStockFlag  1：不缺  0：缺
                      }
                     }

                  }
              }
              let stockRes={};
              let id=localStorage.getItem('userId');
              //  普通商品查库存，定制没有库存
              if(ids.length>0){
                let nesIds=[...new Set(ids)];  //去重
                console.log(nesIds);
                stockRes= await listStockByCondition({distributorId: id,itemIdList : nesIds});  //库存
                if(stockRes.success){
                    let stockResData=stockRes.data;
                    for ( let i = 0;i < stockResData.length;i++) {
                      list.forEach((items,index) => {
                        if ( items.itemId === stockResData[i].itemId) {
                          items.inStockUsableCount =stockResData[i].inStockUsableCount>0?stockResData[i].inStockUsableCount:0;// 在库库存
                          items.onWayUsableCount = stockResData[i].onWayUsableCount>0?(stockResData[i].onWayUsableCount-stockResData[i].inStockUsableCount):0;// 在途库存 
                          if(stockResData[i].inStockUsableCount>0){
                            if(stockResData[i].inStockUsableCount>=items.itemCount){
                               items.zaiTuCount=0;  
                               items.zaiKuCount=items.itemCount;
                            }else{
                               items.zaiTuCount=items.itemCount-stockResData[i].inStockUsableCount;  
                               items.zaiKuCount=stockResData[i].inStockUsableCount;
                            }
                            
                          }else{                      
                            items.zaiTuCount=items.itemCount;
                            items.zaiKuCount=0;
                          }
                          
                        }
                        
                      });
                    }
                }
              }
          

              let puList=[];
              let groupList=[];
              // 购买数量/购买金额
              let isOneBuyCount = 0;
              let isOneBuyMoney = 0;
              list=list.filter(item=>item.salePrice);  //过滤掉没价格的货品
              list.forEach((item,index)=>{ 
                if(item.goodsType==2){
                  this.$set(item,'zaiTuCount',0);
                  this.$set(item,'zaiKuCount',item.itemCount);
                  this.$set(item,'conditionsId','');
                }
                
                // this.$set(item,'isCheck',item.itemType==1?false:true);
                this.$set(item,'isCheck',false);
                this.$set(item,'boxsList',[]);
                this.$set(item,'count',item.itemCount);

                // 处理装箱规格
                let objs={
                  boxType: '件',
                  boxNum: 1,
                }
                if(item.boxs&&item.boxs.length>0){
                  // 有装箱规格
                  if (item.unit) {
                    // 有unit
                    let boxs = []; // 获取加入购物车时所选单位（不允许切换装箱规格）
                    item.boxs.forEach((box) => {
                      if (Number(item.unit) === Number(box.id)) {
                        // unit的值与装箱id一致
                        this.$set(item, 'choosedBoxInfo', box.boxType);
                        this.$set(item, 'choosedBoxNum', box.boxNum);
                        this.$set(item, "boxId", box.id);
                        this.$set(item, 'count', parseInt(item.itemCount/box.boxNum));
                        boxs.push(box);
                      }
                    })
                    this.$set(item, "boxs", boxs); // 设置装箱规格
                  } else {
                    // 没有unit
                    this.$set(item, 'choosedBoxInfo', '件');
                    this.$set(item, 'choosedBoxNum', 1);
                    this.$set(item, "boxId", '');
                    this.$set(item, 'count', item.itemCount);
                    
                    let boxs = [];
                    boxs.push(objs);
                    this.$set(item, "boxs", boxs);
                  }
                } else {
                  // 无装箱规格，默认件
                  this.$set(item, 'choosedBoxInfo', '件');
                  this.$set(item, 'choosedBoxNum', 1);
                  this.$set(item, "boxId", '');
                  this.$set(item, 'count', item.itemCount);

                  let boxs = [];
                  boxs.push(objs);
                  this.$set(item, "boxs", boxs);
                }
                
                if(item.goodsType){ 
                   
                  // 设置默认在途商品参与活动
                  this.$set(item,'onWayFlag',1); 
                  let totalPrice=item.salePrice*item.itemCount;
                  let reduceOrPresent=1;  //促销统计方式：1满减 2满赠
                  let reduceOrPresentId=''; //活动条件id
                  let presentCount=0; //满赠数量
                  //  计算拼团秒杀价格 
                  if(item.groupSeckillId){
                      item.groupSeckills.forEach(groupItem=>{
                          if(groupItem.groupSeckillId==item.groupSeckillId&&groupItem.groupSeckillStatus==1){
                              if(item.itemCount>=groupItem.minNum){
                                  totalPrice=item.itemCount*groupItem.groupSeckillPrice;
                                  // this.$set(item,'conditionsId',groupItem.groupSeckillId);
                              }
                              
                          } 
                      })  
                  }

                 
                  // 活动价
                  if(item.goodsPromotionId&&item.goodsPromotionId!='no'){
                      if(item.promotions){
                        item.promotions.forEach(promoItem=>{
                            let onWayFlag=promoItem.onWayFlag;  //在途商品是否参与活动 1.参与，0.不参与
                            let promoStatus=promoItem.promoStatus; //促销状态：0 未开始, 1 促销中 2 已过期 3 提前结束
                            let promoType=promoItem.promoType;  //活动类型，1 普通活动，2 阶梯活动
                            promoItem.rules.forEach(ruleItem=>{
                                if(item.goodsPromotionId==ruleItem.id){
                                  // 货品活动是否累计 addUpFlag，1是 0否
                                  if (ruleItem.addUpFlag == 1) {
                                    isOneBuyCount+=onWayFlag==0?item.zaiKuCount:(item.zaiKuCount+item.zaiTuCount);
							                      isOneBuyMoney+=onWayFlag==0?(item.zaiKuCount*item.salePrice):(item.itemCount*item.salePrice);
                                  } else {
                                    isOneBuyCount=onWayFlag==0?item.zaiKuCount:(item.zaiKuCount+item.zaiTuCount);
							                      isOneBuyMoney=onWayFlag==0?(item.zaiKuCount*item.salePrice):(item.itemCount*item.salePrice);
                                  }
                                  
                                  ruleItem.conditions.forEach(cItem=>{
                                     if(promoStatus==1){
                                        //moneyOrCount 规则形式：1金额 2数量
									                      if((ruleItem.moneyOrCount==1&&isOneBuyMoney>=cItem.oneBuyMoney)||(ruleItem.moneyOrCount==2&&isOneBuyCount>=cItem.oneBuyCount)){
                                            this.$set(item,'conditionsId',cItem.id);
                                            // specialFlag 是否特价，1是 0否
                                            if(cItem.specialFlag==1){
                                              let specialPrice = cItem.specialPrice ? cItem.specialPrice : 0
                                              totalPrice=onWayFlag==0
                                              ?((item.zaiKuCount*specialPrice)+item.zaiTuCount*item.salePrice)
                                              :((item.zaiKuCount+item.zaiTuCount)*specialPrice)
                                            }else{
                                              
                                                //reduceOrPresent 促销统计方式：1满减 2满赠 
                                                if(cItem.reduceOrPresent==1){
                                                  // reduceType 满减类型， 2折扣  1减免
                                                  if(cItem.reduceType==2){
                                                    let zhekouPrice=fomatFloat((item.salePrice*cItem.discount/100),2);
                                                    totalPrice=onWayFlag==0
                                                    ?((item.zaiKuCount*zhekouPrice)+item.zaiTuCount*item.salePrice)
                                                    :((item.zaiKuCount+item.zaiTuCount)*zhekouPrice);
                                                  }else{   
                                                    
                                                    let reduction=cItem.reduction;
                                                    // 判断是否叠加的减免金额
                                                    if(cItem.reductionPresentAddFlag==1){
                                                        reduction=ruleItem.moneyOrCount==1?
                                                          (onWayFlag==0?((item.zaiKuCount*item.salePrice)*(cItem.reduction/cItem.oneBuyMoney)):(item.itemCount*item.salePrice*(cItem.reduction/cItem.oneBuyMoney))):
                                                          (onWayFlag==0?(item.zaiKuCount*(cItem.reduction/cItem.oneBuyCount)):(item.itemCount*(cItem.reduction/cItem.oneBuyCount)));
                                                    }
                                                    let cutAmount = Number(item.itemCount*item.salePrice) - Number(reduction) > 0 ? Number(item.itemCount*item.salePrice) - Number(reduction): 0;
                                                    totalPrice=fomatFloat(cutAmount,2);  
                                                    if(ruleItem.ruleType==1){   
                                                           let orserIndex=0; 
                                                            list.forEach((orderItem,orserIndex)=>{
                                                              console.log(orserIndex);
                                                               if(orderItem.itemId!=item.itemId&&orderItem.goodsPromotionId==item.goodsPromotionId){  
                                                                    this.$set(orderItem,'totalPrice',Number(orderItem.salePrice*orderItem.itemCount))
                                                                    console.log(orderItem.salePrice*orderItem.itemCount);
                                                               }
                                                                
                                                            })
                                                            
                                                      }
                                                    // totalPrice=onWayFlag==0
                                                    // ?((item.zaiKuCount*item.salePrice-reduction)+item.zaiTuCount*item.salePrice)
                                                    // :((item.zaiKuCount+item.zaiTuCount)*item.salePrice-reduction);
                                                  }
                                                }else{
                                                    reduceOrPresent=cItem.reduceOrPresent;
                                                    reduceOrPresentId=cItem.id;
                                                    presentCount=cItem.presentCount;
                                                    // 判断是否叠加
                                                    if(cItem.reductionPresentAddFlag==1){
                                                      //计算叠加的数量
                                                      let dieJiaNum=ruleItem.moneyOrCount==1?parseInt(isOneBuyMoney/cItem.oneBuyMoney):parseInt(isOneBuyCount/cItem.oneBuyCount);
                                                      presentCount=cItem.presentCount*dieJiaNum;
                                                    }
                                                }
                                            }
                                        }
                                     }
                                  })
                                  




                                  
                                } 
                            })
                            
                        })  
                      } 
                  }
                  console.log(item)
                  this.$set(item,'totalPrice',Number(totalPrice));
                  this.$set(item,'reduceOrPresent',reduceOrPresent);
                  this.$set(item,'reduceOrPresentId',reduceOrPresentId);
                  this.$set(item,'presentCount',presentCount);
                  this.$set(item,'conditionsId','');
                 
                  if(item.goodsPromotionId){  
                    puList.push(item)
                  }else if(!item.groupSeckillId&&!item.goodsPromotionId){
                      item.goodsPromotionId="no";
                      puList.push(item)
                  }
                  if(item.groupSeckillId){
                    groupList.push(item)
                  }
                  
                  
                }
                
              })
              console.log('普通：',puList);
              console.log('拼团活动groupList：',groupList);
                // 促销活动
              if(puList.length>0){
                    let pList=puList.filter(item=>item.goodsType==1);  //普通活动
                    // 把没有活动的商品排序到最前面
                    let pList1=[]; //没活动的商品
                    let pList2=[]; //有活动的商品
                    let newPlist=[]; //新组合
                    console.log(pList);
                    pList.forEach(pItem=>{
                       if(pItem.goodsPromotionId=='no'){
                          pList1.push(pItem);
                       }else{
                          pList2.push(pItem);
                       }
                    })
                    newPlist=[...pList1,...pList2];
                    let dList=puList.filter(item=>item.goodsType==2);  //定制
                    this.shopCartList.list=[...this.shopCartList.list,...this.sort_pro(newPlist ,['goodsPromotionId'])];
                    this.dzShopCartList.list=[...this.dzShopCartList.list,...this.sort_pro(dList ,['goodsPromotionId'])];
              }
              // 拼团秒杀  
              if(groupList.length>0){
                    let pList=groupList.filter(item=>item.goodsType==1);  //普通活动
                    let dList=groupList.filter(item=>item.goodsType==2);  //定制
                    this.shopCartList.list=[...this.shopCartList.list,...this.sort_pro(pList ,['groupSeckillId'])];
                    this.dzShopCartList.list=[...this.dzShopCartList.list,...this.sort_pro(dList ,['goodsPromotionId'])];
              }
              console.log('LLL:',this.shopCartList);
              console.log('定制：',this.dzShopCartList);
              
              console.log(this.shopCartList.list)
              this.shopCartList.list.forEach(item=>{
                  //  拼团活动
                  if (item.children && item.children.length > 0) {
                    item.children.forEach((child) => {
                      if (child.groupSeckills && child.groupSeckills.length > 0) {
                        this.$set(child, "mtoFlag", child.groupSeckills[0].mtoFlag); // 是否支持预售
                      }
                    })
                  }
                  if(item.children[0].groupSeckills){
                    item.children[0].groupSeckills.forEach(groupItem=>{
                      this.$set(item.children[0], "itemSurplusNum", (groupItem.maxNum - groupItem.realSum) || 0); // 可购买剩余数量
                      if(groupItem.groupSeckillId==item.groupSeckillId){
                          this.$set(item,'ruleName',groupItem.name?groupItem.name:'拼团活动');
                          this.$set(item,'ruleNameEn',groupItem.name?groupItem.name:'拼团活动')
                          this.$set(item,'rules',groupItem);
                      }
                    })
                  }
                  
                  // 判断是否有赠品 1满减 2满赠
                  this.$set(item,'reduceOrPresent',1); 
                  item.children.forEach(preItem=>{
                    if(preItem.reduceOrPresent==2){
                     this.$set(item,'reduceOrPresent',2);
                     this.$set(item,'reduceOrPresentId',preItem.reduceOrPresentId);
                     
                     this.$set(item,'presentCount',preItem.presentCount);
                    }
                  })

                  //  促销活动
                  if(item.children[0].promotions){
                    item.children[0].promotions.forEach(promotionItem=>{
                        promotionItem.rules.forEach(rulItem=>{
                          if(rulItem.id==item.goodsPromotionId){
                            this.$set(item,'ruleName',rulItem.ruleName);
                            this.$set(item,'ruleNameEn',rulItem.ruleNameEn)
                            this.$set(item,'rules',rulItem);
                            // 在途不参与活动
                            if(promotionItem.onWayFlag==0){
                                item.children.forEach(childrenItem=>{
                                  this.$set(childrenItem,'onWayFlag',0);
                                  // this.$set(childrenItem,'zaiTuCount',0);  
                                })    
                            }    
                          }
                        })   
                    }) 
                  }           
                  
              })
             
              this.allShopingCartList.push(this.shopCartList);
              this.allShopingCartList.push(this.dzShopCartList);
              this.showLoading = false;
              console.log('所有列表：',this.allShopingCartList);
          }
      
      },
     
      // 把对象数组按照某一个属性（或某几个属性）进行分类-Y
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
                          
      // 更新购物车-y
      updateShopCar () {
        this.$message.success(this.$t('ShopCarts.UpdateCompleted'));
        this.getShopCarList();
      },
      // 到处逛逛
      walkingAround () {   
        this.$router.push({
          name: 'Index'
        });
      },
      // 清空购物车-Y
      emptyShopCar () {
        let ids = [];
        this.allShopingCartList.forEach(item=>{
           item.list.forEach(item2=>{
               item2.children.forEach(item3=>{
                  ids.push(item3.id)
               })
           })
        })
        console.log('清空购物车',ids);
        
        if (ids && ids.length > 0) {
          this.$confirm(this.$t('ShopCarts.EmptyShopCarts'), this.$t('P.Prompt'), {
              confirmButtonText: this.$t('Message.Confirm'),
              cancelButtonText: this.$t('P.Cancel'),
              type: 'warning'
            })
            .then(() => {
                deleteShoppingcart({ids: ids}).then((res) => {
                  if (res.success) {
                    this.$message.success(this.$t('ShopCarts.SuccessfullyDeleted'));
                    this.getShopCarList();
                  }
                });
            })
            .catch(() => {
              this.$message.info(this.$t('P.Canceled'));
            });
        }
      },
      // （批量）删除购物车货品-Y
      batchDelete () {
        let ids = [];
        console.log('批量删除：',this.allShopingCartList);
        this.allShopingCartList.forEach(item=>{
           item.list.forEach(item2=>{
               item2.children.forEach(item3=>{
                 if(item3.isCheck==true){
                   ids.push(item3.id);
                 }
                  
               })
           })
        })
        console.log('批量删除：',ids);
        if(ids.length==0){
           this.$message.warning(this.$t('ShopCarts.PleaseTickShoppingCart'));
           return false;
        }
        this.$confirm(
            this.$t('ShopCarts.DatchesDeleteoOperation'),
            this.$t('P.Prompt'), {
              confirmButtonText: this.$t('Message.Confirm'),
              cancelButtonText: this.$t('P.Cancel'),
              type: 'warning'
            }
          )
          .then(() => {
              deleteShoppingcart({ids: ids}).then((res) => {
                if (res.success) {
                  this.$message.success(this.$t('ShopCarts.SuccessfullyDeleted'));
                  this.getShopCarList();
                }
              });
          })
          .catch(() => {
            this.$message.info(this.$t('P.Canceled'));
          });
      },
      // 继续购物-y
      goWalkingAround () {
        this.$router.push({name: 'Index'});
      },

      // 结账-y
      goTheOrder(){
        console.log('结账：',this.allShopingCartList);
        let orderGood=[];
        let shopList=JSON.parse(JSON.stringify(this.allShopingCartList)) ;
        let isAllType=false;  //货品在库在途是否同时存在
        let zaiKuAllType=false;
        let zaiTuAllType=false;
        let flag6=false;
        shopList.forEach(item=>{
           item.list.forEach(item2=>{
             item2.children.forEach(item3=>{
                // 在库、在途是否同时存在
                if(item3.isCheck){
                    //判断库存是否足够
                    // 拼团-需要是否支持预售
                    let totalPrice=item3.itemCount*item3.salePrice;
                    if(item3.groupSeckillId&&item3.groupSeckillId!=''){
                       item3.groupSeckills.forEach(groupItem=>{
                           if(item3.groupSeckillId==groupItem.groupSeckillId&&groupItem.groupSeckillStatus==1){
                              if(item3.itemCount>groupItem.minNum){
                                  if(item3.itemCount>(item.inStockUsableCount+item.onWayUsableCount)){
                                      // 是否支持超卖或预售 1、支持 0、不支持
                                      if(groupItem.mtoFlag==1){
                                        if(groupItem.maxNum&&item.maxNum!=0&&item3.itemCount>groupItem.maxNum){
                                            this.$message.warning('编号：'+item3.itemCode+ "货品订购数量不能大于拼团限购量：" + groupItem.maxNum);
                                            this.$set(item3,'isCheck',false);
                                        }else{
                                            totalPrice=item3.itemCount*groupItem.groupSeckillPrice;
                                            this.$set(item3,'totalPrice',totalPrice);
                                        }
                                      }else{
                                        this.$message.warning('编号：'+item3.itemCode+"的货品库存不足!");
                                        this.$set(item3,'isCheck',false);
                                      }   
                                  }else{
                                    totalPrice=item3.itemCount*groupItem.groupSeckillPrice 
                                     this.$set(item3,'totalPrice',totalPrice);
                                  }
                              }else{
                                 this.$set(item3,'groupSeckillId','');
                              }
                             
                           }
                       })
                       
                    }else{
                        if(item3.itemCount>(item3.inStockUsableCount+item3.onWayUsableCount)){
                          this.$message('编号：'+item3.itemCode+"的货品库存不足!");
                          flag6=true;
                          this.$set(item3,'isCheck',false);
                          
                        }
                    }     
                }
             })

            //  上面已经判断过是否够库存--直接计算活动
             item2.children.forEach(item3=>{
                if(item3.isCheck){
                    // 在库、在途是否同时存在--start
                    if(item3.zaiKuCount>0){
                       zaiKuAllType=true;
                    }
                    if(item3.zaiTuCount>0){
                       zaiTuAllType=true;
                    } 
                    // 如果赠品的货品数量大于可选的最大数，把下单的赠品数量改为可选的最大数
                    // if(item3.itemType==2){
                    //   if(item3.itemCount>item3.presentCount){
                    //     let itemCount=0;
                    //     for(let i=0;i<item2.children.length;i++){
                    //       if(item2.children[i].isCheck&&item2.children[i].itemType==1){
                    //         itemCount+=item2.children[i].presentCount
                    //       }
                    //     }
                    //     this.$set(item3,'itemCount',itemCount);  
                    //   }

                    // }
                    // 在库、在途是否同时存在--end 
                    let totalPrice=item3.itemCount*item3.salePrice;
                    // 促销活动
                    if(item3.goodsPromotionId&&item3.goodsPromotionId!='no'&&item3.goodsPromotionId!=''&&item3.itemType==1){
                     
                        let conditionsId='';
                        if(item3.promotions){
                            item3.promotions.forEach(promoItem=>{
                              let onWayFlag=promoItem.onWayFlag;  //在途商品是否参与活动 1.参与，0.不参与
                              let promoStatus=promoItem.promoStatus; //促销状态：0 未开始, 1 促销中 2 已过期 3 提前结束
                              let promoType=promoItem.promoType;  //活动类型，1 普通活动，2 阶梯活动
                              promoItem.rules.forEach(ruleItem=>{
                                if(item3.goodsPromotionId==ruleItem.id){

                                  let isOneBuyCount=(onWayFlag==0)?item3.zaiKuCount:item3.itemCount;
                                  let isOneBuyMoney=(onWayFlag==0)?(item3.zaiKuCount*item3.salePrice):(item3.itemCount*item3.salePrice);
                                  //货品活动是否累计 addUpFlag，1是 0否( ruleType 规则对象是2或3时有效)
                                  // if(ruleItem.addUpFlag==1&&ruleItem.ruleType!=1){
                                  if(ruleItem.addUpFlag==1){
                                      let childrenList=item2.children;
                                      let zCoun=item3.zaiKuCount;
                                      let tCount=item3.zaiTuCount;
                                      let zPrice=item3.zaiKuCount*item3.salePrice;
                                      let tPrice=item3.zaiTuCount*item3.salePrice;
                                      for(let i=0;i<childrenList.length;i++){
                                        if(childrenList[i].isCheck&&childrenList[i].itemId!=item3.itemId&&childrenList[i].itemType!=2){
                                          zCoun+=childrenList[i].zaiKuCount;
                                          tCount+=childrenList[i].zaiTuCount;
                                          zPrice+=(childrenList[i].zaiKuCount*childrenList[i].salePrice)
                                          tPrice+=(childrenList[i].zaiTuCount*childrenList[i].salePrice)
                                        }
                                      }
                                      isOneBuyCount=(onWayFlag==0)?zCoun:(zCoun+tCount); 
                                      isOneBuyMoney=(onWayFlag==0)?zPrice:(zPrice+tPrice);
                                     
                                  }
                                   // 是否累计 --END
                                  ruleItem.conditions.forEach(cItem=>{
                                    //moneyOrCount 规则形式：1金额 2数量
                                    if((ruleItem.moneyOrCount==1&&isOneBuyMoney>=cItem.oneBuyMoney)||(ruleItem.moneyOrCount==2&&isOneBuyCount>=cItem.oneBuyCount)){
                                                                 
                                        conditionsId=cItem.id;  
                                        // specialFlag 是否特价，1是 0否
                                        if(cItem.specialFlag==1){
                                          totalPrice=(onWayFlag==0)?(item3.zaiKuCount*cItem.specialPrice+item3.zaiTuCount*item3.salePrice):(item3.itemCount*cItem.specialPrice);
                                            //  活动累计情况下，勾选上的货品都按特价计算
                                            if(ruleItem.addUpFlag==1&&ruleItem.ruleType!=1){
                                              let childrenList=item2.children;
                                              for(let i=0;i<childrenList.length;i++){
                                                if(childrenList[i].isCheck&&childrenList[i].itemId!=item3.itemId){
                                                    // let childrenTotalPrice=onWayFlag==0?
                                                    //   (childrenList[i].zaiKuCount*cItem.specialPrice+childrenList[i].zaiTuCount*childrenList[i].salePrice):
                                                    //   (childrenList[i].itemCount*cItem.specialPrice);
                                                    // this.$set(item3,'totalPrice',childrenTotalPrice);
                                                    // this.$set(item3,'conditionsId',cItem.id);
                                                    // ----
                                                    for(let j=0;j<childrenList[i].promotions.length;j++){
                                                      let rulesF=childrenList[i].promotions[j].rules;
                                                      for(let a=0;a<rulesF.length;a++){
                                                        if(item3.goodsPromotionId==rulesF[a].id){
                                                          let conditions=rulesF[a].conditions;
                                                          for(let b=0;b<conditions.length;b++){
                                                            let childrenTotalPrice=onWayFlag==0?
                                                              (childrenList[i].zaiKuCount*conditions[b].specialPrice+childrenList[i].zaiTuCount*childrenList[i].salePrice):
                                                              (childrenList[i].itemCount*conditions[b].specialPrice);
                                                             this.$set(item3,'totalPrice',childrenTotalPrice);
                                                             this.$set(item3,'conditionsId',cItem.id);
                                                          }
                                                        }
                                                      }	
                                                    }
                                                    // ----
                                                }
                                              }
                                            }
                                        }else{
                                            //reduceOrPresent 促销统计方式：1满减 2满赠
                                            if(cItem.reduceOrPresent==1){
                                              // reduceType 满减类型， 1减免 2折扣
                                              if(cItem.reduceType==1){
                                          
                                                  //  活动累计情况下，勾选上的货品都按减免计算
                                                  if(ruleItem.addUpFlag==1&&ruleItem.ruleType!=1){
                                                      let childrenList=item2.children;
                                                      for(let i=0;i<childrenList.length;i++){
                                                        if(childrenList[i].isCheck){
                                                          let reduction2=cItem.reduction;
                                                          if(cItem.reductionPresentAddFlag==1){
                                                            reduction2=ruleItem.moneyOrCount==1? 
                                                            (onWayFlag==0?((childrenList[i].zaiKuCount*childrenList[i].salePrice)*(cItem.reduction/cItem.oneBuyMoney)):(childrenList[i].itemCount*childrenList[i].salePrice*(cItem.reduction/cItem.oneBuyMoney))):
                                                            (onWayFlag==0?(childrenList[i].zaiKuCount*(cItem.reduction/cItem.oneBuyCount)):(childrenList[i].itemCount*(cItem.reduction/cItem.oneBuyCount)));
                                                          }else{
                                                             reduction2=(reduction2/isOneBuyCount*childrenList[i].itemCount);
                                                          }
                                                            
                                                          let childrenTotalPrice=fomatFloat((childrenList[i].itemCount*childrenList[i].salePrice-reduction2),2);
                                                          if(childrenList[i].itemId==item3.itemId){
                                                             totalPrice=childrenTotalPrice;
                                                          }
                                                          this.$set(item3,'totalPrice',childrenTotalPrice);
                                                          this.$set(item3,'conditionsId',cItem.id);
                                                        }
                                                      }
                                                  }else{
                                                    
                                                    //减免金额 
                                                    let reduction=cItem.reduction;
                                                    // 判断是否叠加的减免金额
                                                    if(cItem.reductionPresentAddFlag==1){
                                                        reduction=ruleItem.moneyOrCount==1?
                                                          (onWayFlag==0?((item3.zaiKuCount*item3.salePrice)*((cItem.oneBuyMoney-cItem.reduction)/cItem.oneBuyMoney)):(item3.itemCount*item3.salePrice*((cItem.oneBuyMoney-cItem.reduction)/cItem.oneBuyMoney))):
                                                          (onWayFlag==0?(item3.zaiKuCount*(cItem.reduction/cItem.oneBuyCount)):(item3.itemCount*(cItem.reduction/cItem.oneBuyCount)));
                                                    }
                                                    console.log('----===}}}}',reduction);  
                                                  
                                                    // totalPrice=fomatFloat((item3.itemCount*item3.salePrice-reduction),2);
                                                    if(ruleItem.ruleType==1){
                                                        if(cItem.reductionPresentAddFlag!=1){
                                                          this.$set(item3,'allOrderMoney',cItem.reduction);   
                                                        
                                                          // let junReduction=(ruleItem.moneyOrCount==1)?(reduction/cItem.oneBuyMoney):(reduction/oneBuyCount); 
                                                           let junReduction=(ruleItem.moneyOrCount==1)?(reduction/cItem.oneBuyMoney):(reduction/oneBuyCount);
                                                          console.log('减免金额',junReduction);
                                                          item2.children.forEach((orderItem,orserIndex)=>{
                                                           
                                                              // if(orderItem.itemId!=item3.itemId&&orderItem.goodsPromotionId==item3.goodsPromotionId){  
                                                              //     this.$set(orderItem,'totalPrice',Number(orderItem.salePrice*orderItem.itemCount))
                                                              //     console.log(orderItem.salePrice*orderItem.itemCount);
                                                              // }
                                                              if(orderItem.goodsPromotionId==item3.goodsPromotionId){  
                                                                  let childTotalPrice=Number(orderItem.salePrice*orderItem.itemCount)-((ruleItem.moneyOrCount==1)?(Number(orderItem.salePrice*orderItem.itemCount)*junReduction):(orderItem.itemCount*junReduction));
                                                                  this.$set(orderItem,'totalPrice',childTotalPrice)
                                                                  this.$set(item3,'conditionsId',cItem.id);
                                                                  if(orderItem.itemId==item3.itemId){
                                                                      totalPrice=childTotalPrice;
                                                                  }
                                                                  console.log('减免后的金额',childTotalPrice);
                                                              }
                                                              
                                                          })
                                                        }else{
                                                            totalPrice=reduction;
                                                        }
                                                          
                                                    }else{
                                                        totalPrice=fomatFloat((item3.itemCount*item3.salePrice-reduction),2);
                                                    }
                                                  }
                                                  console.log(totalPrice);
                                            
                                              }else{ //折扣
                                                let zhekouPrice=fomatFloat((item3.salePrice*cItem.discount/100),2)
                                                totalPrice=(onWayFlag==0)
                                                    ?((item3.zaiKuCount*zhekouPrice)+item3.zaiTuCount*item3.salePrice)
                                                    :(item3.itemCount*zhekouPrice);
                                                   
                                                    //  活动累计情况下，勾选上的货品都按减免计算
                                                      if(ruleItem.addUpFlag==1&&ruleItem.ruleType!=1){
                                                          let childrenList=item2.children;
                                                          for(let i=0;i<childrenList.length;i++){
                                                            if(childrenList[i].isCheck&&childrenList[i].itemId!=item3.itemId){
                                                              let zhekouPrice2=fomatFloat((childrenList[i].salePrice*cItem.discount/100),2)
                                                              console.log('zhekouPrice2',zhekouPrice2);
                                                              let childrenTotalPrice=(onWayFlag==0)
                                                                ?((childrenList[i].zaiKuCount*zhekouPrice2)+childrenList[i].zaiTuCount*childrenList[i].salePrice)
                                                                :(childrenList[i].itemCount*zhekouPrice2);
                                                                this.$set(item3,'totalPrice',childrenTotalPrice);
                                                                this.$set(item3,'conditionsId',cItem.id);
                                                            }
                                                          }
                                                      }
                                              }
                                            }else{                                                
                                                item2.children.forEach(item4=>{
                                                    if(item4.itemType==2){
                                                      this.$set(item4,'conditionsId',cItem.id);
                                                  
                                                    }
                                                })
                                            }
                                        }
                                    }

                                  })
                                }
                              })
                            })
                        }
                        this.$set(item3,'conditionsId',conditionsId);
                        this.$set(item3,'totalPrice',totalPrice);
                    }
                    
                    
                   
                    orderGood.push(item3);
                }
             })
           })
        })
        isAllType=(zaiKuAllType&zaiTuAllType)?true:false;
      
        
        this.orderGoodList=orderGood;
        console.log('要下单的货品：',orderGood);
        console.log('是否包含在途在库:', isAllType);
        if(orderGood.length==0){
          if(!flag6){
             this.$message.info(this.$t('ShopCarts.SelectProductsHint'));
          }
           
           this.showLoading = false;
           return false;
        }
        //在库在途同时存在
        if(isAllType){   
           this.showChooseDelivery=true;  //配送方式弹框
        }else{
          // 如果后台设置计算提示，则弹出
          if(this.orderhint!=''){
            MessageBox.confirm(this.orderhint, '提示', {
              confirmButtonText: '确定提交',
              cancelButtonText: '我再想想',
              customClass: 'orderHint',
              closeOnClickModal:false,
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

      


      // 倒计时（拼团开始/结束）
      intervalTimer (obj) {
        if (this.interval !== null) {
          clearInterval(this.interval);
        }
        if (obj && obj.length > 0) {
          this.interval = setInterval(() => {
            // 获取当前时间，同时得到订单付款截止时间数组
            let newTime = new Date().getTime();
            // 计算相差天数
            let isInterval = false;
            obj.forEach((item) => {
              isInterval = true;
              let diff = '';
              if (item.status === 0) {
                diff = item.startTime - newTime;
              }
              if (item.status === 1) {
                diff = item.endTime - newTime;
              }

              if (diff <= 0) {
                diff = 0;
              }
              this.$set(item, 'diff', diff);
            });
            if (!isInterval) {
              clearInterval(this.interval);
            }
          }, 1000);
        }
      },
      
    
      // 配送方式选择弹窗关闭
      handleClose () {
        this.showChooseDelivery = false;
        this.showLoading = false;
      }
    },
    created () {
      this.exchange=Number( localStorage.getItem('exchange')); 
      this.useLang = this.$b2bConfig.lang;
      this.lang = window.localStorage.getItem('bLang')
        ? window.localStorage.getItem('bLang')
        : 'zh-RMB';
      var id = window.localStorage.getItem('userId');
      let list = localStorage.getItem('orderItem');
      if (list === '' || list === undefined || list === null || list === 'null') {
        // console.log(list)
      } else {
        this.orderItem = JSON.parse(list);
      }
      this.orderAgain = this.$route.query.orderAgain;
      this.orderAgainType = Number(this.$route.query.orderType);
      this.userId = id;
      if(id&&id!=''&&id!='undefined'){
        this.getShopCarList();
      }
      
    }
  };

</script>

<style scoped="scoped" lang='less'>
  @import url("../assets/less/variable.less");

  .user {
    width: 100%;
  }

  .shop-car {
    width: 1200px;

    .bleed {
      padding-left: 55px;

      .el-breadcrumb {
        font-size: 12px;
      }
    }
  }

  .user-main {
    padding-bottom: 40px;
    width: 1190px;

    .shopcar-activity {
      padding-left: 20px;
      line-height: 35px;
      background-color: #e5f9fb;

      .shopcar-activity-img {
        margin-right: 10px;
        font-size: 22px;
        color: @orange;
      }

      .shopCart-activity-state {
        font-size: 13px;
        line-height: 35px;
      }
    }
    .cats-box{
      max-height: 500px;
      overflow-y: scroll;
      &::-webkit-scrollbar{
        display:none;
      }
    }
  }

  .shop-operate {
    .downroad {
      width: 140px;
      height: 35px;
      line-height: 35px;
    }

    .operate {
      height: 35px;
      line-height: 35px;
    }
  }

  .shop-foot {
    height: 50px;
    line-height: 50px;

    .settle {
      min-width: 115px;
      padding-left: 5px;
      padding-right: 5px;
      height: 50px;
      line-height: 50px;
    }
  }

  .empty-car {
    .empty-car-img {
      margin-top: 120px;
      width: 125px;
    }

    .go-around {
      width: 130px;

      span {
        display: block;
        width: 130px;
        height: 40px;
        line-height: 40px;
        border-radius: 5px;
      }
    }
  }

</style>
