<template>
  <div>
    <div class="user rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState"></Header>
      <!--主内容-->
      <div class="user-main rl-clear rl-margin-zero">
        <!--公共左边-->
        <Left></Left>
        <div class="user-right rl-fr rl-bg-white rl-margin-top-default rl-padding-bottom-double">
          <div class="content">
            <h6 class="user-right-title">{{$t('UserCenter.Favorites')}}</h6>
            <ul class="collect-list rl-clear rl-margin-top-default" v-if="this.goodstList.length > 0">
              <li @click="goDoodsDetail(item,item.id)" v-for="item in goodstList" :key="item.id">
                <div
                  class="product cursor-pointer"
                  :class="{'rl-bg-gray-mm':item.saleStatus != 3 }"
                >
                  <div class="img">
                    <img :src="item.imageUrl1" alt />
                  </div>
                  <div
                    class="name"
                  >{{$i18n.locale === 'en'&& item.goodsNameEn !== undefined && item.goodsNameEn !== null && item.goodsNameEn !== '' ? item.goodsNameEn:item.goodsName}}</div>
                  <div class="price-wrap rl-clear">
                    <div class="rl-fl red" v-if="item.saleStatus != 3">商品已下架</div>
                    <div class="rl-fl red" v-else>
                      <span v-if="$i18n.locale === 'en' && $root.currency === 'USD'" class="units">$</span>
                      <span v-else class="units">￥</span>
                      <span>{{$i18n.locale === "en" && $root.currency === "USD"?(Number(item.minPrice?item.minPrice:item.maxPrice)*exchange).toFixed(2):(item.minPrice?item.minPrice:item.maxPrice)}}</span>
                    </div>
                    <div class="rl-fr">
                      <span @click.stop="deleteCollect(item.listId)" class="delect"><i class="iconfont icon-delete"></i></span>
                      <!--<span @click="joinShopCar(item.id)" class="rl-margin-left-mid cursor-pointer"></span>-->
                    </div>
                  </div>
                </div>
              </li>
            </ul>
            <div
              class="empty-car rl-margin-zero"
              v-else
            >
              <div class="empty-car-img rl-margin-zero">
                <img width="100%" src="../../assets/images/empty-car.png" alt />
              </div>
              <p
                class="rl-tc rl-margin-top-default rl-margin-bottom-default rl-text-sm rl-text-gray"
              >{{$t('P.NoCommodities')}}</p>
            </div>
          </div>
          <div class="rl-tr rl-margin-top-default" v-if="this.goodstList.length > 0">
            <el-pagination
              background
              @current-change="handleCurrentChange"
              @size-change="handleSizeChange"
              layout="prev, pager, next, jumper"
              :page-size="pagesize"
              :total="totalCount"
            ></el-pagination>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import Left from "@/components/Left.vue";
import {  } from "@/assets/js/common.js";
import GD from "@/assets/js/globalData";
import {goodsList,goodsDetails,priceGoodsList} from '@/apiService/api'
export default {
  name: "MyCollect",
  components: {
    Header,
    Left,
  },
  data() {
    return {
      userState: 2,
      totalCount: 8,
      cur_page: 1,
      pagesize: 10,
      collecGoodstList: [],
      goodstList: [],
      promotion: {},
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: "zh-RMB", // 语种
      exchange:1,  //汇率
    };
  },
  methods: {
    fLangChange(value) {
      window.localStorage.setItem("bLang", value);
      this.$i18n.locale = value.split("-")[0];
    },
    // 当前页码
    handleCurrentChange(val) {
      this.cur_page = val;
      this.getCollectList();
    },
    // 每页条数
    handleSizeChange(val) {
      this.pagesize = val;
      this.getCollectList();
    },
    // 收藏列表
    getCollectList() {
      var myDate = new Date();
      var tempArray = []; // 临时数组
      var tempGoods = {}; // 临时对象
      var tempItems = []; // 临时数组
      let parmas={
          page: this.cur_page,
          size:this.pagesize,
          collectionFlag:1,
          goodsType:1,
          sortWay:1,
          newFlag:0,   //是否筛选新品 0-否 1-是
        }
       goodsList(parmas).then((res) => {
          if (res.success) {
            console.log('dsdsd :==',res.data.list);
            this.collecGoodstList = res.data.list;
            let ids=[];
            this.collecGoodstList.forEach((item) => {
               
                ids.push(item.id)
                goodsDetails({ id: item.id }).then((resDetail) => {
                  if (resDetail.success) {
                    tempGoods = resDetail.data;
                    tempItems = resDetail.data ;
                    console.log('收藏列表',tempItems);
                    this.promotion = resDetail.promotion;
                    var product = {
                      goodsType: tempGoods.goodsType,
                      listId: item.id,
                      id: tempGoods.id,
                      saleStatus: tempGoods.saleStatus,
                      goodsName: tempGoods.goodsName,
                      imageUrl1: tempGoods.imageUrl1,
                      imageUrl1en: tempGoods.imageUrl1,
                      needSale:
                        this.$root.currency === "USD" ? 0: 0,
                      goodsNameEn: tempGoods.goodsName,
                    };
                    tempArray.push(product);
                  }
                });
            });
            
            this.goodstList = tempArray;
            if(ids.length>0){
                this.priceGoodsListFun(ids,this.goodstList);
            }
            
            console.log(this.goodstList)
          } 
        });
    },

    //查询商品价格
    priceGoodsListFun(ids,list){
       priceGoodsList({goodsIds:ids}).then(res=>{
           if(res.success){
                console.log('价格：',res.data); 
                let priceList=res.data;
                priceList.forEach(priceItem=>{
                   list.forEach(item=>{
                     if(item.id==priceItem.goodsId){
                         this.$set(item,'maxPrice',priceItem.maxPrice);
                         this.$set(item,'minPrice',priceItem.minPrice);
                     }
                   })
                })
           }
       })
    },
    // 删除收藏
    deleteCollect(id) {
      this.$confirm(
        this.$t("UserCenter.OperationDeletedProductCollection"),
        this.$t("P.Prompt"),
        {
          confirmButtonText: this.$t("Message.Confirm"),
          cancelButtonText: this.$t("P.Cancel"),
          type: "warning",
        }
      ).then(() => {
             goodsCollection('DELETE',{id:id}).then((res) => {
              if (res.success) {
                this.$message.success(
                  this.$t("UserCenter.DeletedProductCollection")
                );
                this.getCollectList();
              }else{
               this.$message(res.errMessage);
              } 
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: this.$t("P.Canceled"),
          });
        });
    },
   
    // 去商品详情
    goDoodsDetail(item, id) {
      let routeData = null;

      if (item.saleStatus == 3) {
        if (item.goodsType === 1 && this.promotion !== {}) {
          routeData = this.$router.resolve({
            name: "ShopDetail",
            query: {
              good_id: id,
              activityType: 1,
              goodsType: item.goodsType,
              accessType: 0,
            },
          }); // 跳转活动商品
        } else {
          routeData = this.$router.resolve({
            name: "ShopDetail",
            query: { good_id: id, goodsType: item.goodsType, accessType: 0 },
          });
        }
      }
      window.open(routeData.href, "_blank");
    },
    // 加入购物车
    joinShopCar(id) {
      this.$api
        .post(this, "user/u/shoppingCart", { goods_id: id })
        .then((res) => {
          if (res.code === 0) {
          } 
        });
    },
  },
  created() {
    this.exchange=Number( localStorage.getItem('exchange')); 
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem("bLang")
      ? window.localStorage.getItem("bLang")
      : "zh-RMB";
    this.getCollectList();
  },
};
</script>

<style scoped="scoped" lang='less'>
@import url("../../assets/less/variable.less");
.user {
  width: 100%;
}
.user-main {
  width: 1200px;
}
.user-right {
  width: 1030px;
  .user-right-title {
    padding-bottom: 10px;
    border-bottom: 1px solid @bdLightColor;
    font-size: 20px;
  }
  .content {
    padding: 24px 40px 0;
    border: 2px solid @bdLightColor;
    border-radius: 8px;
    .collect-list {
      padding-bottom: 20px;
      li {
        margin-bottom: 10px;
        float: left;
        margin-right: 10px;
        &:nth-child(5n) {
          margin-right: 0;
        }
      }
      .product {
        width: 181px;
        height: 273px;
        border: 1px solid @bdLighterColor;
        border-radius: 8px;
        overflow: hidden;
        box-sizing: border-box;
        img {
          width: 100%;
          height: 179px;
          vertical-align: middle;
        }
        .name {
          padding: 15px 12px 0;
          height: 36px;
          line-height: 18px;
          font-size: 12px;
          color: @lightBlack;
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
        }
        .price-wrap {
          padding: 15px 12px 0;
          .red {
            font-size: 14px;
            color: @red;
            .units {
              font-size: 12px;
            }
          }
        }
        .delect {
          display: inline-block;
          font-size: 16px;
          color: @lighterGray;
        }
      }
    }
  }
}
.empty-car {
  padding-bottom: 180px;
  .empty-car-img {
    margin-top: 120px;
    width: 153px;
  }
}
</style>
