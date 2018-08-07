<template>
  <div>
    <div class="index rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState"></Header>
      <!--主内容-->
      <div class="main rl-padding-top-default rl-padding-horizontal-lllg rl-margin-zero">
        <div class="breed rl-padding-left-mid rl-text-xxs rl-margin-bottom-default">
          <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item>全部结果</el-breadcrumb-item>
            <el-breadcrumb-item>活动</el-breadcrumb-item>
            <el-breadcrumb-item v-if="activityState === 1">满减</el-breadcrumb-item>
            <el-breadcrumb-item v-else-if="activityState === 2">满赠</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="sort rl-clear">
          <div class="sort-left rl-fl">
            <div class="rl-text-xxs rl-bg-gray-mm syn rl-text-blue-mm rl-tc rl-fl">综合排序</div>
            <div class="rl-bg-white rl-fl">
              <div class="rl-text-xxs rl-tc rl-text-gray rl-bd-gray-sm time cursor-pointer">上架时间</div>
            </div>
            <!--<div class="rl-bg-white rl-fl">
              <div class="rl-text-xxs rl-tc rl-text-gray rl-bd-gray-sm common cursor-pointer">价格</div>
            </div>-->
            <div class="rl-bg-white rl-fl">
              <div class="rl-text-xxs rl-tc rl-text-gray rl-bd-gray-sm common cursor-pointer">销量</div>
            </div>
          </div>
        </div>
        <div class="classify-list">
          <ul class="rl-clear">
            <li v-for="item in activityGoodsList" :key="item.id" @click="goDoodsDetail(item.goodsId,item.goodsType)">
              <div class="content cursor-pointer">
                <div class="img"><img width="100%" :src="(($i18n.locale === 'zh' || !item.goods.imageUrl1en == true)?item.goods.imageUrl1:item.goods.imageUrl1en) + '?x-oss-process=image/resize,h_342,l_342'" alt=""></div>
                <div>
                    <div v-if="$i18n.locale === 'zh'">
                      <div v-if="(item.goods.labelNames === undefined || item.goods.labelNames === null || item.goods.labelNames.length === 0) &&
                      (item.goods.introduce === undefined || item.goods.introduce === null || item.goods.introduce.length === 0)" class="brand rl-tc rl-margin-top-xxxs" >
                          <img class="imgLable" v-show="item.goods.promotionStatus === 1" src="../../src/assets/images/hot-big.png" alt="">
                          <img class="imgLable1" v-show="item.goods.newProductStatus === 1 && item.goods.promotionStatus !== 1" src="../../src/assets/images/new-big.png" alt="">
                          <span>{{($i18n.locale === 'zh' || !item.goods.goodsNameEn == true) ? item.goods.goodsName:item.goods.goodsNameEn}}</span>
                      </div>
                      <div v-else class="brand1 rl-tc rl-margin-top-xxxs" >
                          <img class="imgLable" v-show="item.goods.promotionStatus === 1" src="../../src/assets/images/hot-big.png" alt="">
                          <img class="imgLable1" v-show="item.goods.newProductStatus === 1 && item.goods.promotionStatus !== 1" src="../../src/assets/images/new-big.png" alt="">
                          <a>{{($i18n.locale === 'zh' || !item.goods.goodsNameEn == true) ? item.goods.goodsName:item.goods.goodsNameEn}}</a>
                      </div>
                      <div class="word rl-tc" v-show="$i18n.locale === 'zh' && item.goods.labelNames !== undefined && item.goods.labelNames !== null && item.goods.labelNames.length>0">
                        <span class="label" v-for="(labelName,index) in item.goods.labelNames" :key="index">{{labelName}}</span>
                      </div>
                      <div class="word rl-tc" v-show="$i18n.locale === 'zh' && (item.goods.labelNames === undefined || item.goods.labelNames === null || item.goods.labelNames.length === 0)
                      && item.goods.introduce !== undefined && item.goods.introduce !== null && item.goods.introduce !== ''">
                        <span>{{item.goods.introduce}}</span>
                      </div>
                    </div>
                    <div v-else>
                      <div v-if="(item.goods.labelNameEns === undefined || item.goods.labelNameEns === null || item.goods.labelNameEns.length === 0) &&
                      (item.goods.introduceEn === undefined || item.goods.introduceEn === null || item.goods.introduceEn.length === 0)" class="brand rl-tc rl-margin-top-xxxs" >
                          <img class="imgLable" v-show="item.goods.promotionStatus === 1" src="../../src/assets/images/hot-big.png" alt="">
                          <img class="imgLable1" v-show="item.goods.newProductStatus === 1 && item.goods.promotionStatus !== 1" src="../../src/assets/images/new-big.png" alt="">
                          <a>{{($i18n.locale === 'zh' || !item.goods.goodsNameEn == true) ? item.goods.goodsName:item.goods.goodsNameEn}}</a>
                      </div>
                      <div v-else class="brand1 rl-tc rl-margin-top-xxxs" >
                          <img class="imgLable" v-show="item.goods.promotionStatus === 1" src="../../src/assets/images/hot-big.png" alt="">
                          <img class="imgLable1" v-show="item.goods.newProductStatus === 1 && item.goods.promotionStatus !== 1" src="../../src/assets/images/new-big.png" alt="">
                          <a>{{($i18n.locale === 'zh' || !item.goods.goodsNameEn == true) ? item.goods.goodsName:item.goods.goodsNameEn}}</a>
                      </div>
                      <div class="word1 rl-tc" v-show="$i18n.locale === 'en' && item.goods.labelNameEns !== undefined && item.goods.labelNameEns !== null && item.goods.labelNameEns.length>0">
                        <span class="label" v-for="(labelNameEns,index) in item.goods.labelNameEns" :key="index">{{labelNameEns}}</span>
                      </div>
                      <div class="word rl-tc" v-show="$i18n.locale === 'en' && (item.goods.labelNameEns === undefined || item.goods.labelNameEns === null || item.goods.labelNameEns.length === 0)
                      && item.goods.introduceEn !== undefined && item.goods.introduceEn !== null && item.goods.introduceEn !== ''">
                        <span>{{item.goods.introduceEn}}</span>
                      </div>
                    </div>
                    <div class="moneyLabel" v-if="((freezeStatus !== undefined && freezeStatus !== null && freezeStatus === '2') || freezeStatus === null) &&
                        ((capitalStatus !== undefined && capitalStatus !== null && capitalStatus === '2') || capitalStatus === null)">
                      <div style="float: left;" v-if="Number(fomatFloat(item.goods.salePrice,2)) !== 0">
                        <span class="currency">{{($i18n.locale === 'en' && $root.currency === 'USD') ? '$':'¥'}}</span>
                        <span class="money">{{($i18n.locale === 'en' && $root.currency === 'USD') ?fomatFloat(item.goods.salePriceEn,2):fomatFloat(item.goods.salePrice,2)}}</span>
                      </div>
                      <div style="float: left;" v-else>
                        <span class="money1">{{item.goods.salePrice === undefined || item.goods.salePrice === null ? (!userId == '' &&
                        freezeStatus !== undefined && freezeStatus !== null && freezeStatus === '2' &&
                        capitalStatus !== undefined && capitalStatus !== null && capitalStatus === '2'? $t('P.Calculating') : $t('P.ViewAfterLogin')) : $t('P.NoPricing')}}</span>
                      </div>
                      <div v-if="$i18n.locale === 'zh' && (!userId == '')" style="float: right;">
                        <span v-if="item.goods.saleCount > 10000" class="saleCount">{{$t('P.sale')}} {{fomatFloor(item.goods.saleCount / 10000,2)}}万+</span>
                        <span v-else class="saleCount">{{$t('P.sale')}} {{item.goods.saleCount}}</span>
                      </div>
                      <div v-if="$i18n.locale === 'en' && (!userId == '')" style="float: right;">
                        <span v-if="item.goods.saleCount > 1000" class="saleCount">{{fomatFloor(item.goods.saleCount / 1000,1)}}K+ {{$t('P.sale')}}</span>
                        <span v-else class="saleCount">{{item.goods.saleCount}} {{$t('P.sale')}}</span>
                      </div>
                    </div>
                  </div>
              </div>
            </li>
          </ul>
        </div>
        <div class="rl-tr rl-margin-bottom-default" v-if="this.activityGoodsList.length !== 0">
          <el-pagination
            background
            @current-change ="handleCurrentChange"
            @size-change="handleSizeChange"
            layout="prev, pager, next, jumper"
            :page-size="pagesize"
            :total="totalCount"
          >
          </el-pagination>
        </div>
        <!--商品为空-->
        <div class="empty-car rl-margin-zero" v-if="this.activityGoodsList === null || this.activityGoodsList === undefined || this.activityGoodsList.length === 0">
          <div class="empty-car-img rl-margin-zero"><img width="100%" src="../assets/images/goods-empty.png" alt=""></div>
          <p class="rl-tc rl-margin-top-default rl-margin-bottom-default rl-text-sm rl-text-gray">暂无商品</p>
        </div>
      </div>
      <!--公共底部-->
      <Footer></Footer>
    </div>
    <!--加载动画-->
    <loading v-if="this.showLoading === true"></loading>
  </div>
</template>

<script>
import Footer from '@/components/Footer.vue'
import Header from '@/components/Header.vue'
import {  fomatFloat, fomatFloor } from '@/assets/js/common.js'
import loading from '@/components/loading.vue'
export default {
  name: 'PieceTogether',
  components: {
    Footer,
    Header,
    loading
  },
  data () {
    return {
      activityType: 1, // 活动页跳商品详情
      showLoading: false, // 展示加载动画
      userId: '',
      gradeId: '',
      ruleId: this.$route.query.ruleId,
      activityState: this.$route.query.activityState,
      activityName: '活动',
      userState: 2,
      totalCount: 0,
      cur_page: 1,
      pagesize: 25,
      activityList: [],
      activityGoodsList: []
    }
  },
  methods: {
    fomatFloor (val, n) {
      return fomatFloor(val, n)
    },
    fomatFloat (val, n) {
      return fomatFloat(val, n)
    },
    // 当前页码
    handleCurrentChange (val) {
      this.cur_page = val
      this.getClassifyList()
    },
    // 每页条数
    handleSizeChange (val) {
      this.pagesize = val
      this.getClassifyList()
    },
    // 活动商品列表
    getActivityGoodsList () {
      this.showLoading = true
      var secondsArea = 1
      this.intervalid = setInterval(() => {
        secondsArea--
        if (secondsArea === 0) {
          this.showLoading = false
          clearInterval(this.intervalid)
        }
      }, 800)
      this.$api.get(this, 'user/u/marketing/promotion/pieceTogether', {ruleId: this.ruleId}).then(res => {
        if (res.code === 0) {
          this.activityGoodsList = res.togetherGoods
        } 
      })
    },
    // 去活动商品详情
    goDoodsDetail (id, goodsType) {
      let routeData = this.$router.resolve({
        name: 'ShopDetail',
        query: {good_id: id, activityType: this.activityType, goodsType: 1, accessType: 0}
      })
      window.open(routeData.href, '_blank')
    }
  },
  mounted () {
    var id = window.localStorage.getItem('userId')
    var gid = window.localStorage.getItem('gradeId')
    this.userId = id
    this.gradeId = gid
    this.getActivityGoodsList()
  }
}
</script>

<style scoped="scoped" lang='less'>
  .index{
    width: 100%;
  }
  .main{
    width: 1190px;
    .breed{
      .el-breadcrumb{font-size: 12px}
    }
    .activity{
      ul{
        li{
          display: inline-block;
          width: 291px;
          height: 100px;
        }
        li.current{
          background-color: #00c9dc;
          div{color:#fff;}
        }
      }
    }
    .price{
      .item{
        div{
          height: 48px;
          line-height: 48px;
        }
        .left{
          width: 115px;
        }
        .right{
          width: 849px;
          span{
            margin-right: 15px;
          }
        }
      }
    }
    .sort{
      .sort-left{
        height: 30px;
        line-height: 30px;
        .syn{
          width: 90px;
        }
        .common{
          margin-left: -1px;
          padding-right: 10px;
          width: 50px;
          height: 28px;
          line-height: 28px;
          background: url("../../src/assets/images/icon-up-down.png") no-repeat 45px center;
        }
        .time{
          margin-left: -1px;
          padding-right: 10px;
          width: 75px;
          height: 28px;
          line-height: 28px;
          background: url("../../src/assets/images/icon-up-down.png") no-repeat 70px center;
        }
      }
      .sort-right{
        .fen-ye{
          line-height: 28px;
        }
        .common{
          width: 30px;
          height: 28px;
          line-height: 28px;
          background: url("../../src/assets/images/icon-next.png") no-repeat center center;
        }
        .spe{ transform:rotate(180deg);}
      }
    }
    .classify-list{
      margin-top: 25px;
      ul{
        li{
          margin-bottom: 20px;
          display: inline-block;
          margin-left: 21px;
          cursor: pointer;
          transition: all .2s linear;
          .content{
            padding-bottom: 8px;
            height: 310px;
            width: 219px;
            background-color: #fff;
            border: 1px solid #f5f5f5;
            .img{
              width: 219px;
              height: 219px;
              display: table-cell; /*主要是这个属性*/
              vertical-align: middle;
              text-align: center;
              img{
                height: 219px;
                width:100%;
              }
            }
            .imgLable{
              width: 22px;
              height: 22px;
              padding: 0px;
              margin-left: -5px;
              margin-top: -3px;
              vertical-align: text-top;
            }
            .imgLable1{
              width: 22px;
              height: 22px;
              padding: 0px;
              margin-left: -3px;
              margin-top: -2px;
              vertical-align: text-top;
            }
            .brand{
              background-color: #fff;
              font-size:12px;
              height: 49px;
              width: 193px;
              font-family:FZHei-B01;
              font-weight:400;
              color:rgba(51,51,51,1);
              line-height:22px;
              padding-left: 13px;
              padding-right: 13px;
              overflow: hidden;
              float: left;
              text-align:left;
              text-overflow: ellipsis;
              display: -webkit-box;
              -webkit-line-clamp: 2;
              -webkit-box-orient: vertical;
            }
            .brand1{
              background-color: #fff;
              font-size:12px;
              width: 193px;
              font-family:FZHei-B01;
              font-weight:400;
              color:rgba(51,51,51,1);
              line-height:22px;
              padding-left: 13px;
              padding-right: 13px;
              overflow: hidden;
              float: left;
              text-align:left;
              text-overflow: ellipsis;
              display: -webkit-box;
              -webkit-line-clamp: 1;
              -webkit-box-orient: vertical;
            }
            .word{
              background-color: #fff;
              width: 193px;
              padding-left: 13px;
              padding-right: 13px;
              float: left;
              line-height: 22px;
              // padding-top:5px;
              text-align: left;
              font-size:12px;
              font-family:FZHei-B01;
              font-weight:400;
              color:rgba(153,153,153,1);
              overflow: hidden;
              white-space: nowrap;
              text-overflow: ellipsis;
            }
            .word1{
              background-color: #fff;
              width: 206px;
              padding-left: 13px;
              float: left;
              line-height: 22px;
              // padding-top:5px;
              text-align: left;
              font-size:12px;
              font-family:FZHei-B01;
              font-weight:400;
              color:rgba(153,153,153,1);
              overflow: hidden;
              white-space: nowrap;
              text-overflow: ellipsis;
              .label:nth-child(n+2){
                margin-left:4px;
              }
            }
            .moneyLabel{
              background-color: #fff;
              width: 193px;
              padding-left: 13px;
              padding-right: 13px;
              float: left;
              line-height: 22px;
              padding-top:5px;
              text-align: left;
              vertical-align: bottom;
            }
            .money{
              font-size:20px;
              font-family:Myriad Pro;
              font-weight:400;
              color:rgba(252,76,58,1);
            }
            .money1{
              font-size:14px;
              font-family:FZHei-B01;
              font-weight:400;
              color:rgba(252,76,58,1);
            }
            .currency{
              font-size:12px;
              font-family:Myriad Pro;
              font-weight:400;
              color:rgba(252,76,58,1);
            }
            .label{
              background:rgba(253,184,138,0.35);
              border-radius:2px;
              padding-left: 4px;
              padding-right: 4px;
              font-size:12px;
              line-height: 18px;
              font-family:FZHei-B01;
              font-weight:400;
              color:rgba(237,74,77,1);
              -webkit-text-size-adjust:none;
              display: inline-block;
            }
            .saleCount{
              font-size:12px;
              font-family:FZHei-B01;
              font-weight:400;
              float: right;
              color:rgba(153,153,153,1);
            }
          }
          .content:hover{
            border-radius: 4px;
          }
        }
        li:hover{
          box-shadow: 0 15px 30px rgba(0,0,0,0.1);
          transform: translate3d(0,-2px,0);
        }
        li:nth-child(5n + 1){
          margin-left: 0px;
        }
      }
    }
  }
  .empty-car{
    padding-bottom: 180px;
    .empty-car-img{
      margin-top: 120px;
      width: 153px;
    }
  }
</style>
