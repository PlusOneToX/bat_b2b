<template>
  <div>
    <div class="index rl-margin-zero">
      <!--公共头部-->
      <Header
        :value="value"
        :userState="userState"
        :logOuts="logOut"
        :indexPage="indexPage"
        :currentTab="'index'"
      ></Header>
      <!--主内容-->
      <div class="main rl-margin-zero">
       
        <!-- banner -->
        <div class="banner-content rl-clear">
          <div class="banner rl-fl">
            <el-carousel trigger="click" height="550px">
              <el-carousel-item v-for="item in promoteList" :key="item.id" :style="'background: url(' + item.imageUrl + ') 50% 0px no-repeat'">
                <template v-if="($i18n.locale == 'zh'&&item.bannerArea!= 1)|| ($i18n.locale == 'en' && item.bannerArea != 0)">
                  <a :href="item.bannerUrl" class="banner-img cursor-pointer" target="_blank"></a>
                </template>
                
              </el-carousel-item>
            </el-carousel>
            <div class="banner-radius"></div>

            <!-- 快捷入口 -->
            <quickEntry :hui_id="huiId"></quickEntry>
          </div>
        </div>

        <div class="home-new" ref="newProduct" v-if="newshopList && newshopList.length > 0">
          <!--新品-->
          <div class="slider-list" ref="newImg" v-show="newshopList && newshopList.length > 0">
            <good :showLoading="showLoading" :newshopList="newshopList" :type="5" :typeEn="5" @input="handleInput"></good>
          </div>
          <div class="nav-left" @click="switchImg(-1)" v-show="newshopList && newshopList.length > 5">
            <i class="iconfont icon-left"></i>
          </div>
          <div class="nav-right" @click="switchImg(-2)" v-show="newshopList && newshopList.length > 5">
            <i class="iconfont icon-right"></i>
          </div>
        </div>

        <!-- 拼团 还没接口-->
        <groupBuy></groupBuy>

        <div class="list-content">
          <!--产品列表-->
          <div class="shop-list home-goods" v-for="(section, index) in sectionsList" :key="section.id">
            <div class="shop-nav rl-clear"
              :class="[
                { 'line-graA': index % 5 === 0 },
                { 'line-graB': index % 5 === 1 },
                { 'line-graC': index % 5 === 2 },
                { 'line-graD': index % 5 === 3 },
                { 'line-graE': index % 5 === 4 },]">
              <div class="rl-fl goods-title">
                <span class="rl-margin-right-xxxxs rl-text-llg" v-if="$i18n.locale === 'zh' || !section.titleEn == true">{{ section.title }}</span>
                <span class="rl-margin-right-xxxxs rl-text-llg" v-else>{{section.titleEn}}</span>
              </div>
              <div @click="goSectionCate(section.id, section.title, section.titleEn) " class="more rl-fr cursor-pointer">
                <span class="text">{{ $t("OrderSuccess.More") }}</span>
                <i class="el-icon-arrow-right"></i>
              </div>
              
              <sectionCate :itemId="section.id"></sectionCate>
              
            </div>
           
            <div class="shop">
              <good
                :showLoading="showLoading"
                :itemId="section.id"
                :section_index="index"
                :section_img="section.styleUrl"
                :section_img1="section.styleUrl1"
                :section_img2="section.styleUrl2"
                :section_img3="section.styleUrl3"
                :section_url="section.styleExtensionUrl"
                :section_url1="section.styleExtensionUrl1"
                :section_url2="section.styleExtensionUrl2"
                :section_url3="section.styleExtensionUrl3"
                :section_length="sectionsList.length"
                :type="section.styleType"
                :typeEn="section.styleTypeEn"
                @input="handleInput"
              ></good>
              
            </div>
            <a target="_blank" :href="section.extensionUrl" class="nav-img adver"
              v-show="($i18n.locale === 'zh' || !section.imageUrlEn == true) &&section.imageUrl">
              <img :src="section.imageUrl" alt />
            </a>
            <a target="_blank" :href="section.extensionUrlEn" class="nav-img adver"
              v-show="($i18n.locale === 'en' || !section.imageUrl == true) &&section.imageUrlEn">
              <img :src="section.imageUrlEn" alt />
            </a>
          </div>
        </div>
      </div>
      <!--公共底部-->
      <Footer></Footer>
    </div>

    <!-- 悬浮窗 -->
    <floating></floating>

    <!-- 产品推广 -->
		<productPromotion v-if="showProPromotion"></productPromotion>

    <!--加载动画-->
    <loading v-if="showLoading === true"></loading>
  </div>
</template>

<script>
import Footer from "@/components/Footer.vue";
import Header from "@/components/Header.vue";
import sectionCate from "@/components/sectionCate.vue";
import good from "@/components/good.vue";
import loading from "@/components/loading.vue";
import quickEntry from "@/components/quickEntry/quickEntry.vue";
import floating from "@/components/quickEntry/floatingBar.vue";
import groupBuy from "@/components/groupBuy/groupBuy.vue";
import productPromotion from '@/components/dialog/productPromotion.vue'

// liu--
import { sectionList,bannerList,goodsList,priceGoodsList} from '@/apiService/api'
export default {
  name: "Index",
  components: {
    Footer,
    Header,
    sectionCate,
    good,
    loading,
    quickEntry,
    floating,
    groupBuy,
    productPromotion,
  },
  data() {
    return {
      userId: "",
      indexPage: true,
      showLoading: false, // 展示加载动画
      logOut: true, // 被登出状态
      value: 1,
      userState: this.$route.params.user_state,
      promoteList: [], // 广告推广列表
      sectionsList: [], // 首页商品分类列表
      pageNo: 1,
      pageSize: 8,
      items: [1, 2, 3, 4, 5, 6, 7, 8],
      newshopList: [], // 新品列表
      selected: 0, // 当前新品展示列表
      imgWidth: 1240,
      flag: true,
      pingId: "", // 拼团id
      huiId: "", // 阶梯价特惠id
      showProPromotion: false,
    };
  },
  mounted() {
    this.userId = window.localStorage.getItem("userId");
    this.promoteData();
    // 新品列表
    this.newData();
    this.getSectionListFun();

    if (this.userId) {
      this.showProPromotion = true;
    }
  },
  methods: {
    switchImg(index) {
      // 切换图片，index传入-1代表上一张，-2代表下一张
      if (index === -1) {
        // 上一张
        // 如果是第一张
        if (this.selected === 0) {
          // 防止连点bug
          if (!this.flag) return;
          index = Math.ceil(this.newshopList.length / 5);
          // 显示至最后一张
          this.$refs.newImg.style.left = -index * this.imgWidth + "px";
          this.animate(this.imgWidth, () => {
            // 显示上一张
            this.selected = Math.ceil(this.newshopList.length / 5) - 1;
            this.flag = true;
          });
        } else {
          // 显示上一张
          this.animate(this.imgWidth, () => {
            this.selected--;
            this.flag = true;
          });
        }
      } else if (index === -2) {
        // 下一张
        if (this.selected === Math.ceil(this.newshopList.length / 5) - 1) {
          // 如果是倒数第二张
          index = Math.ceil(this.newshopList.length / 5);
          // 显示下一张
          this.animate(-this.imgWidth, () => {
            this.selected = 0;
            this.$refs.newImg.style.left = 0;
            this.flag = true;
          });
        } else {
          this.animate(-this.imgWidth, () => {
            this.selected++; // 显示至第一张
            this.flag = true;
          });
        }
      }
    },
    animate(moveX, callback) {
      if (this.flag) {
        // 关闭节流阀，每次调用动画函数回调函数中打开
        this.flag = false;
        let ulX = this.$refs.newImg.offsetLeft;
        // 求出移动的目标位置
        let target = ulX + moveX;
        // 创建定时器移动元素
        this.swiper = setInterval(() => {
          let step = (target - this.$refs.newImg.offsetLeft) / 10; // 实现由快到慢的过渡效果
          step = step > 0 ? Math.ceil(step) : Math.floor(step); // 对每次移动的距离取整
          let startX = this.$refs.newImg.offsetLeft; // 求出元素的当前位置
          if (this.$refs.newImg.offsetLeft === target) {
            // 移动完成
            clearInterval(this.swiper); // 清除先前已经创建的定时器
            callback && callback();
            return;
          }
          // 移动
          this.$refs.newImg.style.left = startX + step + "px";
        }, 5);
      }
    },
    handleInput(target) {
      this.showLoading = target;
    },

    // 获取板块数据-y
    getSectionListFun(){
       sectionList({page:1,size:500}).then(res=>{
         if(res.success){
              this.sectionsList=res.data.list;
              res.data.list.forEach((item) => {
                if (item.title === "阶梯价特惠") {
                  this.huiId = item.id;
                }
              });
              if (this.sectionsList.length === 0) {
                this.showLoading = false;
              }
              this.sectionsList.sort(this.theSort("sectionOrder"));
              window.localStorage.setItem(
                "sectionsList",
                JSON.stringify(this.sectionsList)
              );
           }
        })
      
    },

   // 广告推广列表--轮播图-y
    promoteData() { 
      bannerList({size:100,page:1}).then((res) => {
        if (res.success) {
          if(this.$i18n.locale === 'zh'){
             this.promoteList = res.data.list.filter(item=>item.bannerArea!=1);
          }else{
            this.promoteList = res.data.list.filter(item=>item.bannerArea!=0);
          }
          
        }
      });
    },

    // 根据ids查询价格--y
    getPriceData(goodsIdList){
         priceGoodsList({goodsIds:goodsIdList}).then((res) => {
            if ( res.success ) {
              let list=JSON.parse(JSON.stringify(this.newshopList));
              list.forEach((item) => {
                for (let i = 0; i < res.data.length; i++) {
                  if (item.id === res.data[i].goodsId) {
                    item.minPrice = res.data[i].minPrice; 
                    item.maxPrice = res.data[i].maxPrice;
                  }
                }
              });
              this.newshopList=list;
            }
          });
          //  item.labelNames.splice(
          //     0,
          //     0,
          //     '低至' + res.goodsList[i].discount + '折'
          //   );
          //   item.labelNameEns.splice(
          //     0,
          //     0,
          //     'Up to ' +
          //       (100 - res.goodsList[i].discount * 10) +
          //       '% off'
          //   );
    },

   //获取 新品列表  --y 
    newData() { 
      let goodsIdList = [];
      let goodsIds = "";
      let params={
        page:1,
        size:5,
        collectionFlag:0,
        goodsType:1,
        newFlag:1,
      };
      
      goodsList(params).then((res) => {
          if (res.success) {
            this.newshopList = res.data.list;
            // 新品背景色循环处理
            let bgColor = "#F0F0FF,#FFF4E5,#E9F7F2,#F0F0FF,#FBEDEF,";
            let len = this.newshopList.length;
            let num = Math.ceil(len / 5);
            function repeat(str, n) {
              return new Array(n + 1).join(str);
            }
            var str = repeat(bgColor, num);
            let bgColorArr = str.split(",");
            bgColorArr = bgColorArr.slice(0, len);

            if (this.newshopList.length > 0) {
              this.newshopList.forEach((item) => {
                goodsIdList.push(item.id);
                goodsIds += item.id + ",";
              });

              for (let i = 0; i < bgColorArr.length; i++) {
                // 设置背景色
                this.newshopList[i].bgColor = bgColorArr[i];
              }
            }
            
            if (
              this.userId !== undefined &&
              this.userId !== null &&
              this.userId !== "" &&
              goodsIdList.length > 0
            ) {
             this.getPriceData(goodsIdList) 
            }
          }
        });
    },
  
   

    
    // 排序从小到大
    theSort(property) {
      return function (a, b) {
        var value1 = a[property];
        var value2 = b[property];
        return value1 - value2;
      };
    },
    // 去版块更多
    goSectionCate(id, name, nameEn) {
      this.$router.push({
        name: "SectionCate",
        query: { section_id: id, section_name: name, section_name_en: nameEn },
      });
    },
  },
  beforeMount() {
    this.showLoading = true;
  },
  
};
</script>

<style scoped="scoped" lang='less'>
@import url("../assets/less/variable.less");
.main {
  width: 100%;
  background-color: @white;
  .banner-content {
    width: 100%;
    min-width: 1260px;
    max-width: 1920px;
    margin: 0 auto;
    .banner {
      width: 100%;
      padding-bottom: 32px;
      .banner-radius {
        position: absolute;
        left: 0;
        bottom: 31px;
        width: 100%;
        height: 56px;
        background: url("../assets/images/index/radius.png") no-repeat center;
        background-size: 100% 56px;
      }
      /deep/.el-carousel {
        .el-carousel__arrow--left {
          left: calc((100% - 1200px) / 2 + 236px);
        }
        .el-carousel__arrow--right {
          right: calc((100% - 1200px) / 2 + 16px);
        }
        .el-carousel__indicators {
          bottom: 42px;
          text-align: right;
          padding: 12px 6px;
          cursor: pointer;
          .el-carousel__button {
            width: 12px;
            height: 12px;

            border-radius: 50%;
          }
        }
        .el-carousel__arrow {
          font-size: 22px;
          background-color: rgba(0, 0, 0, 0.2);
        }
      }
      .banner-img {
        width: 100%;
        height: 100%;
        display: block;
      }
      .el-carousel {
        z-index: 0;
      }
    }
    .banner-right {
      width: 218px;
      height: 413px;
      border-right: 1px solid @bdColor;
      border-bottom: 1px solid @bdColor;
    }
  }
  .home-new {
    position: relative;
    width: 1258px;
    max-height: 332px;
    margin: 40px auto 0;
    overflow: hidden;
    box-sizing: border-box;
    .slider-list {
      width: 600%;
      padding: 0 30px;
      z-index: 0;
    }
    &:hover {
      .nav-left,
      .nav-right {
        display: inline-block;
        text-align: center;
      }
    }
    .nav-left {
      position: absolute;
      left: 30px;
      top: 50%;
      width: 30px;
      height: 66px;
      line-height: 66px;
      transform: translateY(-50%);
      background-color: rgba(0, 0, 0, 0.2);
      cursor: pointer;
      display: none;
      .iconfont {
        color: @white;
        font-size: 24px;
      }
    }
    .nav-right {
      position: absolute;
      right: 30px;
      top: 50%;
      width: 30px;
      height: 66px;
      line-height: 66px;
      transform: translateY(-50%);
      background-color: rgba(0, 0, 0, 0.2);
      cursor: pointer;
      display: none;
      .iconfont {
        color: @white;
        font-size: 24px;
      }
    }
  }
  .list-content {
    position: relative;
    width: 1200px;
    margin: 0 auto;
    .recommend-goods,
    .home-goods {
      margin: 30px 0 0;
      width: 100%;
      .goods-title {
        margin-bottom: 14px;
        font-size: 26px;
        color: @lightBlack;
      }
      .more {
        display: inline-block;
        margin-top: 10px;
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
      .recommend-goods-list {
        width: 100%;
        font-size: 0;
        .grid-promo {
          display: inline-block;
          width: 230px;
          height: 706px;
          border-radius: 16px;
          background-color: @proBg;
          cursor: pointer;
        }
        .grid-list {
          display: inline-block;
          width: calc(100% - 230px);
          vertical-align: top;
          .grid-hot {
            position: relative;
            margin-left: 12px;
            width: 472px;
            height: 320px;
            padding: 30px;
            font-size: 0;
            box-sizing: border-box;
            border-radius: 16px;
            background-color: @proBg;
            vertical-align: top;
            float: left;
            .item-tag {
              position: absolute;
              top: 0;
              left: 50%;
              padding: 0 5px;
              height: 20px;
              line-height: 20px;
              font-size: 12px;
              text-align: center;
              transform: translateX(-50%);
              color: @white;
              background-color: @redLabel;
              border-radius: 0 0 4px 4px;
            }
            .item-box {
              display: inline-block;
              width: calc(100% - 230px);
              vertical-align: top;
              .title {
                display: inline-block;
                margin-top: 30px;
                font-size: 20px;
                color: #333333;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
              }
              .desc {
                display: inline-block;
                width: 100%;
                font-size: 12px;
                color: @lightGray;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
              }
              .price {
                display: block;
                margin-top: 36px;
                font-size: 14px;
                color: @lightBlack;
                span {
                  display: inline-block;
                  font-size: 12px;
                  color: @lighterBlack;
                }
              }
              .salePrice {
                display: inline-block;
                margin-top: 3px;
                font-size: 26px;
                color: @blue;
                em {
                  display: inline-block;
                  font-size: 16px;
                  color: @red;
                }
              }
              .btn-sel {
                display: inline-block;
                margin-top: 30px;
                padding: 6px 30px;
                font-size: 14px;
                color: @white;
                background-color: @blue;
                border-radius: 16px;
              }
            }
            .item-img {
              display: inline-block;
              width: 230px;
              img {
                width: 260px;
                height: auto;
              }
            }
          }
          .grid-item {
            position: relative;
            width: 230px;
            height: 347px;
            padding: 15px;
            margin: 0 0 12px 12px;
            box-sizing: border-box;
            background-color: @proBg;
            border-radius: 16px;
            float: left;
            &:first-child,
            &:nth-child(5) {
              margin-left: 14px;
            }
            &:hover {
              box-shadow: 0 7px 17px 2px rgba(0, 0, 0, 0.11);
            }
            .item-tag {
              position: absolute;
              top: 0;
              left: 50%;
              padding: 0 5px;
              height: 20px;
              line-height: 20px;
              font-size: 12px;
              text-align: center;
              transform: translateX(-50%);
              color: @white;
              background-color: @redLabel;
              border-radius: 0 0 4px 4px;
            }
            .item-img {
              width: 100%;
              height: auto;
              img {
                width: 100%;
                height: 100%;
              }
            }
            .item-box {
              display: block;
              width: 100%;
              margin-top: 8px;
              .title {
                display: inline-block;
                width: 100%;
                font-size: 14px;
                color: @black;
                text-overflow: ellipsis;
                overflow: hidden;
                white-space: nowrap;
              }
              .desc {
                display: inline-block;
                width: 100%;
                font-size: 12px;
                color: @lightBlack;
                text-overflow: ellipsis;
                overflow: hidden;
                white-space: nowrap;
              }
              .moneyLabel {
                width: 100%;
                display: block;
                margin-top: 12px;
                .price {
                  display: inline-block;
                  .currency {
                    display: inline-block;
                    font-size: 16px;
                    font-weight: 550;
                    color: @red;
                  }
                  .money {
                    display: inline-block;
                    margin-left: 6px;
                    font-size: 12px;
                    color: @lightGray;
                  }
                }
                .sale {
                  float: right;
                  .saleCount {
                    display: inline-block;
                    font-size: 12px;
                    color: @lightGray;
                  }
                }
              }
            }
          }
        }
      }
    }
    .shop-list {
      margin-top: 50px;
      .adver {
        display: inline-block;
        width: 100%;
        height: 200px;
        cursor: pointer;
        img {
          width: 100%;
          height: 100%;
          border-radius: 16px;
        }
      }
      .shop-nav {
        background-color: @white;
        .more:hover {
          color: @blue;
        }
        ul {
          li {
            padding-left: 15px;
            padding-right: 15px;
            float: left;
            color: @white;
            font-size: 10px;
            background: url("../../src/assets/images/icon-shu-hao2.png")
              no-repeat right center;
            cursor: pointer;
          }
          li:hover {
            opacity: 0.5;
          }
        }
      }
      .shop {
        margin-bottom: 12px;
      }
    }
  }
}
/*颜色渐变*/
/*.line-graA{background: linear-gradient(to right, #fe4d30 , #fe353f);}
  .line-graB{background: linear-gradient(to right, #a62eff , #8037ff);}
  .line-graC{background: linear-gradient(to right, #38d2fb , #2ea7fe);}
  .line-graD{background: #fd9636;}
  .line-graE{background: #32b8fd;}*/
</style>
