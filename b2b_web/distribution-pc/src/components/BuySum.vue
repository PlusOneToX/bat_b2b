<template>
  <div class="buy-sum rl-clear">
    <div @click="handleReduce" class="rl-fl buyac buya rl-text-gray cursor-pointer">-</div>
    <div class="rl-fl buyb">
      <input
        type="text"
        v-if="this.item.salePrice !== 0"
        class="rl-tc rl-text-xxs"
        @input="changeNum"
        v-model="quantitys"
        maxlength="8"
        onkeyup="this.value=this.value.replace(/\D/g,'')"
      />
      <input
        @input="getFocus"
        v-else
        class="rl-tc rl-text-xxs"
        type="text"
        v-model="quantitys"
        maxlength="8"
        readonly
      />
    </div>
    <div @click="handleAdd" class="rl-fl buyac buyc rl-text-gray cursor-pointer">+</div>
  </div>
</template>

<script>
import Vue from 'vue';
import GD from '@/assets/js/globalData';
export default {
  name: 'BuySum',
  props: {
    item: {
      type: Object
    },
    confirmShop: {
      type: Object
    },
    tuType: {
      type: Boolean
    },
    goodId:{
      type: Number
    }
  },
  data () {
    return {
      quantitys: 0,
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: 'zh-RMB' // 语种
    };
  },
  watch: {
    // quantitys (val) {
    //   this.quantitys = this.quantitys.toString().replace(/\D/g, '');
    //   var secondsArea = 5; // 设置5秒
    //   clearInterval(this.intervalid);
    //   // 拼团未开始
    //   if (Number(this.item.groupSeckillStatus) === 0) {
    //     let stockNum=this.tuType?this.item.onWayUsableCount+this.item.inStockUsableCount:this.item.inStockUsableCount;   
    //     if (stockNum < this.quantitys*this.item.choosedBoxNum ) {
    //       this.item.visible = true;
         
    //       this.$emit('input', this.quantitys-1);
    //       this.quantitys-=1;
    //       this.intervalidTwo = setInterval(() => {
    //         secondsArea--;
    //         if (secondsArea === 0) {
    //           this.item.visible = false;
              
    //           clearInterval(this.intervalidTwo);
    //         }
    //       }, 1000);
    //       Vue.set(this.item, 'invent', 1); // 控制缺货登记显隐 0：包含在途 1：不包含在途 2：库存充足          
    //     } else {
    //       Vue.set(this.item, 'invent', 2);
          
    //     }

    //   }else {  //拼团进行中 
        
    //      this.item.groupSeckills.forEach((gItem,index) => {
    //        if(index==0){
    //           Vue.set(gItem, 'itemSurplusNum', (gItem.itemSurplusNum-this.item.choosedBoxNum));
    //         }
    //      });
         
    //      Vue.set(this.item, 'invent', 2);
    //      Vue.set(this.item, 'count', this.quantitys);
    //   }

      

    //   this.$emit('input', this.quantitys,this.item.id);
    //   this.$emit('itemInput', this.item,this.goodId);
    // }
  },
  methods: {
    fLangChange (value) {
      window.localStorage.setItem('bLang', value);
      this.$i18n.locale = value.split('-')[0];
    },
    getFocus () {
      if (this.$i18n.locale === 'zh') {
        this.$message.warning(
          '当前产品暂未定价，无法购买，如有需要，请联系商务人员'
        );
      } else {
        this.$message.warning(
          'The current products have not been priced and cannot be purchased. If necessary, please contact business personnel.'
        );
      }
    },
    handleAdd () {
      // 子组件通过emit将数据传递出去
      if (Number(this.item.salePrice) === 0) {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning(
            '当前产品暂未定价，无法购买，如有需要，请联系商务人员'
          );
        } else {
          this.$message.warning(
            'The current products have not been priced and cannot be purchased. If necessary, please contact business personnel.'
          );
        }
        return false;
      }
      
      if (!this.item.count) {
        
        // Vue.set(this.item, 'count', 1); // 设置对象的属性。
        this.quantitys++;
      } else {
        // this.item.count++;
        this.quantitys++;
        this.item.count=Number(this.quantitys)*this.item.choosedBoxNum;
        
      }
      this.changeNum(1);
      // this.$emit('itemInput', this.item);
      // this.$emit('input', this.quantitys);
    },
    handleReduce () {
      if (this.quantitys > 0) {
        // this.item.count--;
        this.quantitys = this.quantitys - 1;
        this.item.count=Number(this.quantitys)*this.item.choosedBoxNum;
        // this.$emit('input', this.quantitys);
        // this.$emit('itemInput', this.item,this.goodId);
      }
      this.changeNum(2);
    },
    changeNum (type) {
      
      this.quantitys = this.quantitys.toString().replace(/\D/g, '');
     
      var secondsArea = 5; // 设置5秒
      clearInterval(this.intervalidTwo);
      

      // 拼团未开始
      if (this.item.groupSeckillStatus== 0) {
        let stockNum=this.tuType?(this.item.onWayUsableCount+this.item.inStockUsableCount):this.item.inStockUsableCount;   
        if (stockNum < this.quantitys*this.item.choosedBoxNum ) {
          this.item.visible = true;
          this.$emit('input', (type==2?(this.quantitys-1):0),this.item.id);
          this.quantitys=(type==1?(this.quantitys-1):0);
          this.intervalidTwo = setInterval(() => {
            secondsArea--;
            if (secondsArea === 0) {
              this.item.visible = false;
             
              clearInterval(this.intervalidTwo);
            }
          }, 1000);
          Vue.set(this.item, 'invent', 1); // 控制缺货登记显隐 0：包含在途 1：不包含在途 2：库存充足   
               
        } else {
          if (!this.item.count) {
            Vue.set(this.item, 'count', 1);
          }
          Vue.set(this.item, 'invent', 2);
        }
       
      } else {
        
        // 拼团进行中
        if (Number(this.item.quantityIsMerge) === 0) {
          // 拼团数量不合并计算
          if (this.item.purchaseNum) {
            // 限购（限购量）
            if (this.quantitys >= this.item.purchaseNum) {
              this.quantitys = this.item.purchaseNum;
            }
          } else {
            // 不限购（剩余件数）
            if (this.quantitys >= this.item.itemSurplusNum) {
              this.quantitys = this.item.itemSurplusNum;
            }
          }
        } else { // 拼团数量合并计算
          // 判断是否为清仓模式
          if (this.quantitys >= this.item.itemSurplusNum) {
            this.quantitys = this.item.itemSurplusNum;
          }
        }
        let stockNum=0;
        let stockTotalCount=this.tuType?(this.item.onWayUsableCount+this.item.inStockUsableCount):this.item.inStockUsableCount;
        if(this.item.mtoFlag==1){
            stockNum=(this.item.maxNum>stockTotalCount)?(this.item.maxNum-this.item.realSum):stockTotalCount;
             
        }else{
            stockNum=stockTotalCount;
        }
        console.log(this.quantitys*this.item.choosedBoxNum);
       
        if ((this.item.mtoFlag !=1 || (this.item.mtoFlag==1 && this.item.maxNum)) && stockNum < this.quantitys*this.item.choosedBoxNum ) {
          this.item.visible = true;   
          this.$emit('input', (type==2?(this.quantitys-1):0));
          this.quantitys=(type==1?(this.quantitys-1):0);
          this.intervalidTwo = setInterval(() => {
            secondsArea--;
            if (secondsArea === 0) {
              this.item.visible = false;
             
              clearInterval(this.intervalidTwo);
            }
          }, 1000);
          
        }
        if(this.item.groupSeckills){
          this.item.groupSeckills.forEach((gItem,index) => {
            if(index==0){
              if(gItem.minNum?(this.quantitys*this.item.choosedBoxNum>gItem.minNum):true){
                  let itemSurplusNumLast=gItem.itemSurplusNum-this.quantitys*this.item.choosedBoxNum;
                  Vue.set(gItem, 'itemSurplusNumLast', (itemSurplusNumLast>0?itemSurplusNumLast:0));
                }
              }
          });
        }
        
        Vue.set(this.item, 'invent', 2);
        
      }
      console.log('填入：',this.quantitys);
      Vue.set(this.item, 'count', Number(this.quantitys)*this.item.choosedBoxNum);
      
      this.$emit('input', this.quantitys,this.item.id);  //普通
      this.$emit('itemInput', this.item,this.goodId);  //定制
      
    }
  },
  created () {
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem('bLang')
      ? window.localStorage.getItem('bLang')
      : 'zh-RMB';
  }
};
</script>

<style scoped lang='less'>
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
      width: 46px;
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
</style>
