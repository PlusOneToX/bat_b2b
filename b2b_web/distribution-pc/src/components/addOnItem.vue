<template>
  <el-dialog class="activityAlls" title="" :visible.sync="activityDialogVisible">
    <div class="rule-item rl-padding-top-xxxs rl-padding-bottom-xxxs">
      <span>{{$t('ShopCarts.RuleName')}}:</span>
      <span class="rl-margin-left-xxxs">{{pieceRule.rulesName}}</span>
    </div>
    <div class="rule-describe rl-padding-left-default rl-bg-gray-mm">
      <div class="name rl-margin-right-default">{{$t('ShopCarts.RuleDescription')}}</div>
      <div class="item">
        <div v-for="(cdItem,cindex) in pieceRule.conditions" :key="cindex">
          <span class="rl-margin-right-default">{{$t('ShopCarts.Condition')}}{{cindex + 1}}:</span
          ><span>{{cdItem.conditionName}}</span>
        </div>
      </div>
    </div>
    <div class="column-right rl-bg-white">
      <div class="right-cons">
        <div class="rl-padding-top-xxxs rl-padding-bottom-xxxs">{{$t('ShopCarts.Participate')}}</div>
        <div class="user-table">
          <!-- liu 这个暂时不知道是啥，先注释吧 -->
          <!-- <div class="group" v-if="pieceRule.conditions.length > 0">
            <div @click="changeGroup(index, group)" class="rl-bg-gray-xm cursor-pointer" :class ="{'active':groupIndex === index}" v-for="(group,index) in pieceRule.conditions" :key="index">{{group.conditionName}}</div>
          </div> -->
          <el-table :data="dataList" border max-height="900" header-row-class-name="header-row" class="activity-el-table rl-tc">
            <!-- 图片 -->
            <el-table-column :label="$t('ShopCarts.Picture')" :min-width="60">
              <template slot-scope="scope">
                <div class="shop-img cursor-pointer"><img width="100%" :src="scope.row.itemImg" alt=""></div>
              </template>
            </el-table-column>
            <!-- 货品编码 -->
            <el-table-column :label="$t('ShopCarts.ItemNo')" prop="itemCode" :min-width="90"> </el-table-column>
            <!-- 货品名称 -->
            <el-table-column :label="$t('ShopCarts.ItemName')" prop="itemName" :min-width="110"> </el-table-column>
            <!-- 规格 -->
            <el-table-column :label="$t('ShopCarts.Spe')" prop="specsName"> </el-table-column>
            <!-- 颜色 -->
            <el-table-column :label="$t('ShopCarts.Colors')" prop="colorName" width="55"> </el-table-column>
            <!-- 会员价格 -->
            <el-table-column :label="$t('ShopCarts.MemPrice')" prop="salePrice" width="73">
              <template slot-scope="scope">
                <i class="asmd">￥</i>
                {{ scope.row.salePrice| keepTwoNum}}
              </template>
            </el-table-column>
            <!-- 数量 -->
            <el-table-column :label="$t('ShopCarts.Quantity')" :min-width="120">
              <template slot-scope="scope"> 
                <div v-if="scope.row.goodsType === 1">
                  <el-input-number v-model="scope.row.count" :max="scope.row.stockCoun" @change="handleChange($event,scope.row)" :min="0" :step="1" :step-strictly="true" label="描述文字" size="mini"  @keyup="this.value=this.value.replace(/\D/g,'')"></el-input-number>
                </div>
                <div v-else><el-input-number v-model="scope.row.num" :min="0"  label="描述文字" size="mini" disabled=""></el-input-number></div>
              </template>
            </el-table-column>
            <!-- 库存 -->
            <el-table-column :label="$t('P.Inventory')" min-width="59">
              <template slot-scope="scope">
                <div v-if="scope.row.goodsType === 1" class="rl-text-xxs">{{scope.row.stockCount}}</div>
                <div v-else><el-button class="mini-search-btn" @click="goCustrom(scope.row.itemCode)">{{$t('Index.Customized')}}</el-button></div>
              </template>
            </el-table-column>
          </el-table>
          <div class="rl-margin-top-default" style="height: 32px">
            <div class="rl-fr"><el-pagination
              background
              @current-change ="handleCurrentChange"
              @size-change="handleSizeChange"
              layout="prev, pager, next, jumper"
              :page-size="pageSize"
              :total="totalCount"
            >
            </el-pagination></div>
          </div>
        </div>
      </div>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button :loading="loadsing" @click.native.prevent="confirmSubmit" type="primary">{{$t('P.OK')}}</el-button>
      <el-button @click="cancel">{{$t('P.Cancel')}}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import Vue from 'vue'
import {formatDate, } from '@/assets/js/common.js'
import sha1 from 'js-sha1'
import GD from '@/assets/js/globalData'
// liu 
import {promotionGoodsList, promotionPresentList,listStockByCondition,priceItemList,addShoppingcart} from  '@/apiService/api'
export default {
  name: 'addOnItem',
  props: {
    pieceRule: {
      type: Object
    },
    activityDialogVisible: {
      type: Boolean
    }
  },
  data () {
    return {
      pageNo: 1,
      pageSize: 5,
      totalCount: 0,
      loadsing: false,
      dataList: [],  //表格数据
      activityGroups: [],
      currentGroupId: '',
      groupIndex: 0,
      joinCart: [],
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
    formatDate (time) {
      var date = new Date(time)
      return formatDate(date, 'yyyy-MM-dd hh:mm:ss')
    }
  },
  methods: {
    fLangChange (value) {
      window.localStorage.setItem('bLang', value);
      this.$i18n.locale = value.split('-')[0];
    },
    cancel () {
      this.$emit('cancelDialog', 0)
    },
    handleSizeChange (val) {
      this.pageSize = val
      this.getData()
    },
    handleCurrentChange (val) {
      this.pageNo = val
      this.getData()
    },
    
    setJoinData (item) {
      let t = 0
      let product = {'ruleId': this.pieceRule.id, 'goodsId': Number(item.goodsId), 'itemId': item.id, 'itemType': item.itemType, 'num': item.num, 'orderType': item.orderType}
      this.joinCart.forEach(obj => {
        if (item.id === obj.itemId) { // 重复添加
          t = 1
          obj.num = item.num
        }
      })
      if (t === 0) {
        this.joinCart.push(product)
      }
    },
    changeGroup (index, group) { // 切换分组
      this.$nextTick(() => {
        this.groupIndex = index
        this.currentGroupId = group.id
        this.$api.get(this, 'user/u/marketing/promotion/goods', {page: this.pageNo, count: this.pageSize, ruleId: this.pieceRule.id, ruleGroupId: group.id}).then(res => { // 根据规则id获取货品
          if (res.code === 0) {
            this.dataList = res.goodss
            if (res.goodss) {
              res.goodss.forEach(item => {
                Vue.set(item, 'num', 0)
                Vue.set(item, 'stocks', 0)
                item.stocks = item.stock.numInWarehouse + item.stock.numOnWay + item.stock.numVmi - item.stock.numOnWayLock - item.stock.numLock - item.stock.numVmiLock
                if (item.stocks <= 0) { item.stocks = 0 }
                if (item.diyList) { this.pieceRule.isNotShowImg = true }
              })
            }
          }
        })
        this.$api.get(this, 'user/u/marketing/promotion/goods/count', {ruleId: this.pieceRule.id, ruleGroupId: group.id}).then(res => { // 获取总数
          if (res.code === 0) {
            this.totalCount = res.count
          }
        })
      })
    },

    // 获取赠品

    // 获取活动下的货品-Y
    async getData(){
            let params={
              id:this.pieceRule.rulesId,
              page:this.pageNo,
              size:this.pageSize,
            }
              
            let res=await promotionGoodsList(params);
            
            if(res.success&&res.data.list){
                this.dataList =res.data.list;
                console.log('活动货品列表：',res.data.list);
            
                let list=this.dataList;
                let ids=[];
                let priceIds=[];
                for(let i=0;i<list.length;i++){
                    this.$set(list[i],'goodsPromotionId',list[i].promotionRuleId);
                    this.$set(list[i],'count',0);
      
                    if(list[i].goodsType==1){
                      ids.push(list[i].id);
                    }
                    let objP={
                      goodsId:list[i].goodsId,
                      itemId:list[i].id
                    }
                    priceIds.push(objP);
                }
                
                //  普通商品查库存，定制没有库存
                let stockRes={};
                if(ids.length>0){
                  let id=localStorage.getItem('userId')
                  let priceRes=await priceItemList({goodsItemIds:priceIds}); //价格
                  let priceResDta=[];
                  if(priceRes.success){
                    priceResDta=priceRes.data;
                  }
                  // retailPrice  salePrice itemId

                  let nesIds=[...new Set(ids)];  //去重
                  stockRes= await listStockByCondition({distributorId: id,itemIdList : nesIds});  //库存
                  let stockResData=[];
                  if(stockRes.success){
                      stockResData=stockRes.data;
                  }
                  list.forEach((items) => {
                      // 库存
                      for ( let i = 0;i < stockResData.length;i++) {
                          if ( items.id == stockResData[i].itemId) {
                            let inStockUsableCount =stockResData[i].inStockUsableCount>0?stockResData[i].inStockUsableCount:0;// 在库库存
                            let onWayUsableCount = stockResData[i].onWayUsableCount>0?stockResData[i].onWayUsableCount:0;// 在途库存 
                            let stockCount=inStockUsableCount+onWayUsableCount;
                            this.$set(items,'inStockUsableCount',inStockUsableCount);
                            this.$set(items,'onWayUsableCount',onWayUsableCount);
                            this.$set(items,'stockCount',stockCount)
                          }
                      }

                      // 价格
                      priceResDta.forEach(priceItem=>{
                        if(priceItem.itemId==items.id){
                            this.$set(items,'salePrice',priceItem.salePrice)
                        }
                      })
                  });
                  
                  this.dataList=list;
                  this.totalCount = res.data.total
                  console.log('活动列表：',list);
                }
            }

        
    },
    
     // 改变货品数量
    handleChange (val, item) {
      this.$set(item,'itemCount',val);
      if(val<=item.inStockUsableCount){
         this.$set(item,'zaiKuCount',val);
         this.$set(item,'zaiTuCount',0);
      }else{
         this.$set(item,'zaiKuCount',item.inStockUsableCount);
         this.$set(item,'zaiTuCount',val-item.inStockUsableCount);
      }

      this.$set(item,'itemInCount',item.zaiKuCount);
      this.$set(item,'itemOnWayCount',item.zaiTuCount);
      console.log('货品添加',item);
      
    },

    // 确定
    confirmSubmit(){
       console.log('活动确定：',this.dataList);
       let list=this.dataList;
       let shopList=[];
       list.forEach(item=>{
         if(item.itemCount>0){
            let obj={
               barCode:item.barCode,
               colorName:item.colorName,
               diyType:item.diyType,
               goodsId:item.goodsId,
               goodsName:item.goodsName,
               goodsNo:item.goodsNo,
               goodsPromotionId:item.goodsPromotionId,
               goodsType:item.goodsType,
               imageUrl:item.imageUrl1,
               itemCode:item.itemCode,
               itemCount:item.itemCount,
               itemId:item.id,
               itemName:item.itemName,
               itemNameEn:item.itemNameEn?item.itemNameEn:'',  //货品英文名称
               itemType:1,
               specsName:item.specsName,
               weight:item.weight,
            }
            shopList.push(obj);
         }
       })
       if(shopList.length>0){         
            addShoppingcart(shopList).then(res=>{
              if(res.success){
                this.$emit('cancelDialog', this.pieceRule.id)
                this.$message.success('加入购物车成功');
                
                
              }else{
                this.$message(res.errMessage);
              }
            })
        }else{
          this.$message('请至少添加一个货品')
        }
    },
   
    goCustrom (code) { // 获取DIY编辑器地址
      this.$confirm('该货品为DIY定制商品，暂未定制，是否去定制?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.custromUrl(code)
      }).catch(() => {
        this.$message.info('已取消定制!')
      })
    },
    custromUrl (code) {
      var timestamp = Date.parse(new Date()) // 获取时间戳
      let diyItemCode = code
      let Source = 0 // 0：b2b diy定制 1：收单diy定制
      let Signature = ''
      let AppID = '3893019696'
      let AppKey = 'ac7c381094a149a48be7b6c1a826c74b'
      this.showLoading = true
      this.$api.get(this, 'user/goodsdiy/getEditUrl').then(res => {
        if (res.code === 0) {
          let code80 = AppID + diyItemCode + Source + res.token
          let StringSha1 = AppID + AppKey + code80 + timestamp
          this.showLoading = false
          Signature = sha1(StringSha1)
          Signature = Signature.toLocaleUpperCase() // 小写字母转大写字母
          window.location.href = res.editorUrl + '?Token=' + res.token + '&Timestamp=' + timestamp + '&Code80=' + diyItemCode + '&Signature=' + Signature + '&AppID=' + AppID + '&Source=' + Source // 跳转链接
        }
      })
    }
  },
  created () {
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem('bLang') ? window.localStorage.getItem('bLang') : 'zh-RMB';
    console.log(this.pieceRule)
    this.getData()
  }
}
</script>

<style lang='less'>
  .activityAlls{
    .el-dialog{
      width: 900px!important;
      max-height: 80%;
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
<style scoped lang='less'>
  .mini-search-btn{
    font-size: 12px;
    padding: 5px;
  }
  .right-cons {
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
