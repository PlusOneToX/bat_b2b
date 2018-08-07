<!--
 * @Author: litian
 * @Date: 2018-02-22 16:35:22
 * @LastEditors: litian
-->
<template>
  <div class="group-buying">
    <!--公共头部-->
    <Header :userState="userState"></Header>
    <div class="content-wrap" v-if="groupData.length > 0">
      <div class="banner">
        <div class="inner" :class="{'en': $i18n.locale === 'en'}"></div>
      </div>
      <div class="content">
        <!-- 拼团商品/拼团货品 -->
        <div 
          class="sort-header">
          <div
            @click="toOne"
            class="rl-tc rl-text-xxs cursor-pointer"
            :class="{ current: tabs === 'one' }"
          >{{ $t("ShopCarts.GroupBuying") }} </div>
          <!-- 拼团货品 -->
          <!-- <div
            @click="toTwo"
            class="rl-tc rl-text-xxs cursor-pointer"
            :class="{ current: tabs === 'two' }"
          >{{ $t("ShopCarts.GroupBuyingN") }}</div> -->
        </div>
        <div class="pro-list">
          <groupBuyPro :groupData="groupData" :cateFlag="'groupList'" :tabs="tabs"></groupBuyPro>
        </div>

        <!-- 分页 -->
        <div class="rl-tr rl-margin-top-default" v-if="groupData.length > 0 && totalCount > 5">
          <el-pagination
            background
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
            layout="prev, pager, next, jumper"
            :page-size="pageSize"
            :total="totalCount"
          ></el-pagination>
        </div>
      </div>
    </div>

    <!-- 暂无数据 -->
    <div class="no-goods" v-show="isGroupList">
      <div class="empty-car-img">
        <img width="100%" src="../assets/images/goods-empty.png" alt="BAT商城 暂无商品" />
      </div>
      <p class="tips">{{ ($i18n.locale === 'zh') ? "暂无商品哦！" : $t("P.NoCommodities") }}</p>
      <span class="go-around" @click="walkingAround">{{$t('ShopCarts.Around')}}</span>
    </div>
    <!--公共底部-->
    <Footer :cateFlag="'group'"></Footer>

    <!--加载动画-->
    <loading v-if="this.showLoading === true"></loading>
  </div>
</template>

<script>
import Footer from '@/components/Footer.vue';
import Header from '@/components/Header.vue';
import loading from '@/components/loading.vue';
import groupBuyPro from '@/components/groupBuy/groupBuyPro.vue';
import { } from '@/assets/js/common.js';
// liu
import { groupseckillGoodList,priceGoodsList} from '@/apiService/api'

export default {
  name: 'GroupBuying',
  components: { Footer, Header, loading, groupBuyPro },
  data () {
    return {
      tabs: 'one',
      userState: 2,
      cur_page: 1,
      pageSize: 5,
      groupData: [], // 团购列表
      isGroupList: false, // 是否有团购商品
      interval: null, // 倒计时
      showLoading: false
    };
  },
  created () {
    let userId=localStorage.getItem('userId');
    
     if(userId&&userId!=''&&userId!='undefined'){
        this.userId=userId;
       this.initListData(this.cur_page, this.pageSize);
     }
    
  },
  methods: {
    // 数组根据某个属性去重
    unique(arr,val) {
      const res = new Map();
      return arr.filter(item => !res.has(item[val]) && res.set(item[val], 1))
    },
    // 初始化活动列表
    initListData (page, count, activityTyp) {
      this.showLoading = true;
      let params={
         page: page,
         size: count,
         groupSeckillType:'',
       }
       groupseckillGoodList(params).then(res=>{
         if(res.success){
             let list=res.data.list;
             console.log('---shujusu',res.data.list);
             list=this.unique(res.data.list,'id');
             this.showLoading = false;
            if (list.length > 0) {
              this.isGroupList = false;
              this.groupData =list;
              this.totalCount = res.data.pages;
              let goodsIds=[];
              list.forEach(item=>{
                goodsIds.push(item.id);
              })
              
              if(this.userId&&this.userId!=undefined&&this.userId!=null&&this.userId!=''&&goodsIds.length){    
                this.getPriceData(goodsIds);
              }
            
            } else {
              this.isGroupList = true;
            }
             if(this.userId&&this.userId!=undefined&&this.userId!=null&&this.userId!=''){   
                this.intervalTime();
              }
             
         }
       })
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
    toOne () {
      this.tabs = 'one';
    },
    toTwo () {
      this.tabs = 'two';
    },
    // 当前页码
    handleCurrentChange (val) {
      this.cur_page = val;
      this.initListData(val, this.pageSize);
    },
    // 每页条数
    handleSizeChange (val) {
      this.pagesize = val;
      this.initListData(this.cur_page, val);
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
    // 到处逛逛
    walkingAround () {
      this.$router.push({ name: 'Index' });
    }
  }
};
</script>

<style lang="less" scoped>
@import url("../assets/less/variable.less");
.content-wrap {
  position: relative;
  min-width: 1260px;
  min-height: 465px;
  padding-bottom: 80px;
  margin: 0 auto;
  background-color: @groupBg;
  .banner {
    height: 465px;
    .inner {
      height: 600px;
      background: url("../assets/images/groupBuying/group-banner.png") no-repeat
        top center;
      &.en {
        background: url("../assets/images/groupBuying/group-banner-en.png")
          no-repeat top center;
      }
    }
  }
  .content {
    position: relative;
    top: 0;
    width: 1200px;
    margin: 0 auto;
    .sort-header {
      position: absolute;
      left: 50%;
      top:-90px;
      transform: translateX(-50%);
      border-radius: 4px;
      div {
        display: inline-block;
        padding:10px;
        color: @white;
        &:first-child {
          border-top-left-radius: 4px;
          border-bottom-left-radius: 4px;
        }
        &:last-child {
          border-top-right-radius: 4px;
          border-bottom-right-radius: 4px;
        }
      }
      div.current {
        position:relative;
        font-size:26px;
        &::after{
          position: absolute;
          content: '';
          width:42px;
          height: 4px;
          border-radius: 6px;
          left:50%;
          bottom:-2px;
          transform: translateX(-50%);
          background-color:@white;
        }
      }
    }
    .pro-list{
      margin-top:60px;
    }
  }
}
.no-goods {
  padding: 100px 0;
  text-align: center;
  background-color: @grayBg;
  min-height: 500px;
  .empty-car-img {
    margin: 0 auto;
    width: 300px;
  }
  .tips {
    margin-top: 30px;
    font-size: 18px;
    color: @lighterGray;
  }
  .go-around {
    display: inline-block;
    margin-top: 20px;
    width: 130px;
    height: 40px;
    line-height: 40px;
    font-size: 18px;
    color: @lighterGray;
    border: 1px solid @lighterGray;
    border-radius: 5px;
    cursor: pointer;
    &:hover {
      color: @blue;
      border: 1px solid @blue;
    }
  }
}
</style>
