<!--
 * @Author: yaowei
 * @Date: 2018-05-21 10:17:40
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-07 09:21:54
-->
<template>
  <div class="groupBuy-wrap" v-if="groupData&&groupData.length > 0">
    <div class="group-buy-t rl-clear">
      <span class="rl-fl title">{{$t('GroupBuy.BrandActivity')}}</span>
      <ul class="rl-fl section">
        <li @click="goPath('/GroupBuying')">{{$t('GroupBuy.GroupBuying')}}</li>
        <!-- <li>{{$t('GroupBuy.BulkDeal')}}</li> -->
      </ul>
      <div data-v-f933fd7e class="rl-fr cursor-pointer more" @click="goPath('/GroupBuying')">
        <span data-v-f933fd7e class="text">{{$t('OrderSuccess.More')}}</span>
        <i data-v-f933fd7e class="el-icon-arrow-right"></i>
      </div>
    </div>
    <div class="group-buy-b">
      <groupBuyItem v-if="groupData&&groupData.length > 0" :groupData="groupData" ></groupBuyItem>
    </div>
  </div>
</template>

<script>
import groupBuyItem from '@/components/groupBuy/groupBuyItem.vue';
// liu
import { groupseckillGoodList,priceGoodsList} from '@/apiService/api'

export default {
  name: 'groupBuy',
  components: {
    groupBuyItem
  },
  data () {
    return {
      groupData: [], // 团购列表
    
      interval: null, // 倒计时
      userId:'',
    };
  },
  created () {
    let userId=localStorage.getItem('userId');
    
     if(userId&&userId!=''&&userId!='undefined'){
        this.userId=userId;
        this.initListData(1, 8);
     }
    
  },
  methods: {
    // 数组根据某个属性去重
    unique(arr,val) {
      const res = new Map();
      return arr.filter(item => !res.has(item[val]) && res.set(item[val], 1))
    },
    // 初始化活动列表
    initListData (page, count) {
       let params={
         page: page,
         size: count,
         groupSeckillType:'',
       }
       groupseckillGoodList(params).then(res=>{
         if(res.success &&res.data.list){
           
             let list=this.unique(res.data.list,'id');
             this.groupData =list;
             let goodsIds=[];
             list.forEach(item=>{
               goodsIds.push(item.id);  
             })
           
             if(this.userId&&this.userId!='undefined'&&this.userId!=null&&this.userId!=''&&goodsIds.length>0){
                
                this.getPriceData(goodsIds);
                this.intervalTime();
              }
         }
       })
    },

    // 把日期转化为时间戳
    getTimestamp(date){
        // var date = '2015-03-05 17:59:00.0';
        date = date.substring(0,19);    
        date = date.replace(/-/g,'/'); //必须把日期'-'转为'/'
        let timestamp = new Date(date).getTime();
        return (timestamp);
    },
    
    // 定时器
    intervalTime () {
      if (this.interval !== null) {
        clearInterval(this.interval);
      }
     
      if (this.groupData && this.groupData.length > 0) {

        this.interval = setInterval(() => {
          // 获取当前时间，同时得到订单付款截止时间数组
          let newTime = new Date().getTime();
          // 计算相差天数
          let isInterval = false;
          this.groupData.forEach((item) => {
            isInterval = true;
            let diff = '';
           
            if(this.getTimestamp(item.startTime) - newTime>0){
               item.status = 0;
               diff = this.getTimestamp(item.startTime) - newTime;
            }else{
               item.status = 1;
               diff = this.getTimestamp(item.endTime) - newTime;
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
     // 根据ids查询价格
    getPriceData(goodsIdList){
         priceGoodsList({goodsIds:goodsIdList}).then((res) => {
            if ( res.success ) {
              let list=JSON.parse(JSON.stringify(this.groupData));
              list.forEach((item) => {
                if(item.realSum==undefined || item.realSum==null){
                  item.realSum=0;
                }
                for (let i = 0; i < res.data.length; i++) {
                  
                  if (item.id === res.data[i].goodsId) {
                    item.minPrice = res.data[i].minPrice; 
                    item.maxPrice = res.data[i].maxPrice;
                    item.discount=res.data[i].minPrice/item.groupSeckillPrice;
                  }
                }
              });
              this.groupData=list;
            }
          });  
    },
    // 页面跳转
    goPath (path, query) {
      if (query) {
        this.$router.push({ path: path, query: query });
      } else {
        this.$router.push({ path: path });
      }
    }
  }
};
</script>

<style lang="less" scoped>
@import url("../../assets/less/variable.less");
.groupBuy-wrap {
  width: @center;
  margin: 50px auto 0;
  .group-buy-t {
    position: relative;
    height: 37px;
    &::after {
      content: "";
      position: absolute;
      right: 0;
      bottom: 0;
      left: 0;
      border-bottom: 1px solid @redBd;
    }
    .title {
      position: relative;
      padding-left: 12px;
      padding-right: 35px;
      height: 37px;
      line-height: 37px;
      font-size: 18px;
      color: @white;
      background: linear-gradient(90deg, @groupBtnColor 0%, @red 100%);
      border-radius: 4px 4px 0 0;
      &::after {
        content: "";
        position: absolute;
        right: -10px;
        bottom: 4px;
        width: 25px;
        height: 45px;
        background-color: @white;
        transform: rotate(-35deg);
      }
    }
    .section {
      display: inline-block;
      margin-top: 7px;
      margin-left: 10px;
      padding-bottom: 10px;
      li {
        position: relative;
        float: left;
        padding-left: 15px;
        padding-right: 15px;
        color: @lightGray;
        font-size: 14px;
        line-height: 20px;
        cursor: pointer;
      }
      li:hover {
        color: @blue;
      }
    }
    .more {
      margin-top: 7px;
      padding-bottom: 10px;
      font-size: 12px;
      color: @lighterBlack;
      .text {
        display: inline-block;
        vertical-align: middle;
      }
      i.el-icon-arrow-right {
        position: relative;
        top: 2px;
      }
      &:hover {
        color: @blue;
      }
    }
  }
}
</style>
