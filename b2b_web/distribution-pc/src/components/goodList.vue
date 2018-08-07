<template>
    <div class="goodslist">
      <!---上下分布---->
      <ul v-if="type === 1" class="home-list rl-clear">
        <div class="nav-top">
          <div class="nav-item">
            <img :src="section_img" alt="">
          </div>
          <div class="nav-item">
            <img :src="section_img1" alt="">
          </div>
        </div>
        <li @click="goDoodsDetail(shop,shop.goods.id,shop.goods.goodsType)" v-for="(shop, index) in shopList" :key="shop.id" v-if="index < 5">
          <div class="content" v-if="shop.goods">
            <div class="item-tag" v-show="shop.goods.newProductStatus === 1 && shop.goods.promotionStatus !== 1">新品</div>
            <div class="item-tag" v-show="shop.goods.promotionStatus === 1">热销</div>
            <div class="img">
              <img :src="(($i18n.locale === 'zh' || !shop.goods.imageUrl1en == true)?shop.goods.imageUrl1:shop.goods.imageUrl1en) + '?x-oss-process=image/resize,h_458,l_458'" alt="">
            </div>
            <div>
              <div v-if="$i18n.locale === 'zh'">
                <div v-if="(shop.goods.labelNames === undefined || shop.goods.labelNames === null || shop.goods.labelNames.length === 0) &&
            (shop.goods.introduce === undefined || shop.goods.introduce === null || shop.goods.introduce.length === 0)" class="brand rl-tc rl-margin-top-xxxs" >
                  <img class="imgLable" v-show="shop.goods.promotionStatus === 1" src="../../src/assets/images/hot-big.png" alt="">
                  <img class="imgLable1" v-show="shop.goods.newProductStatus === 1 && shop.goods.promotionStatus !== 1" src="../../src/assets/images/new-big.png" alt="">
                  <a>{{($i18n.locale === 'zh' || !shop.goods.goodsNameEn == true) ? shop.goods.goodsName:shop.goods.goodsNameEn}}</a>
                </div>
                <div v-else class="brand1 rl-tc rl-margin-top-xxxs" >
                  <img class="imgLable" v-show="shop.goods.promotionStatus === 1" src="../../src/assets/images/hot-big.png" alt="">
                  <img class="imgLable1" v-show="shop.goods.newProductStatus === 1 && shop.goods.promotionStatus !== 1" src="../../src/assets/images/new-big.png" alt="">
                  <a>{{($i18n.locale === 'zh' || !shop.goods.goodsNameEn == true) ? shop.goods.goodsName:shop.goods.goodsNameEn}}</a>
                </div>
                <div class="word1 rl-tc" v-show="$i18n.locale === 'zh' && shop.goods.labelNames !== undefined && shop.goods.labelNames !== null && shop.goods.labelNames.length>0">
                  <span class="label" v-for="(labelName,index) in shop.goods.labelNames" :key="index">{{labelName}}</span>
                </div>
                <div class="word rl-tc" v-show="$i18n.locale === 'zh' && (shop.goods.labelNames === undefined || shop.goods.labelNames === null || shop.goods.labelNames.length === 0)
            && shop.goods.introduce !== undefined && shop.goods.introduce !== null && shop.goods.introduce !== ''">
                  <span>{{shop.goods.introduce}}</span>
                </div>
              </div>
              <div v-else>
                <div v-if="(shop.goods.labelNameEns === undefined || shop.goods.labelNameEns === null || shop.goods.labelNameEns.length === 0) &&
            (shop.goods.introduceEn === undefined || shop.goods.introduceEn === null || shop.goods.introduceEn.length === 0)" class="brand rl-tc rl-margin-top-xxxs" >
                  <img class="imgLable" v-show="shop.goods.promotionStatus === 1" src="../../src/assets/images/hot-big.png" alt="">
                  <img class="imgLable1" v-show="shop.goods.newProductStatus === 1 && shop.goods.promotionStatus !== 1" src="../../src/assets/images/new-big.png" alt="">
                  <a>{{($i18n.locale === 'zh' || !shop.goods.goodsNameEn == true) ? shop.goods.goodsName:shop.goods.goodsNameEn}}</a>
                </div>
                <div v-else class="brand1 rl-tc rl-margin-top-xxxs" >
                  <img class="imgLable" v-show="shop.goods.promotionStatus === 1" src="../../src/assets/images/hot-big.png" alt="">
                  <img class="imgLable1" v-show="shop.goods.newProductStatus === 1 && shop.goods.promotionStatus !== 1" src="../../src/assets/images/new-big.png" alt="">
                  <a>{{($i18n.locale === 'zh' || !shop.goods.goodsNameEn == true) ? shop.goods.goodsName:shop.goods.goodsNameEn}}</a>
                </div>
                <div class="word rl-tc" v-show="$i18n.locale === 'en' && shop.goods.labelNameEns !== undefined && shop.goods.labelNameEns !== null && shop.goods.labelNameEns.length>0">
                  <span class="label" v-for="(labelNameEns,index) in shop.goods.labelNameEns" :key="index">{{labelNameEns}}</span>
                </div>
                <div class="word rl-tc" v-show="$i18n.locale === 'en' && (shop.goods.labelNameEns === undefined || shop.goods.labelNameEns === null || shop.goods.labelNameEns.length === 0)
            && shop.goods.introduceEn !== undefined && shop.goods.introduceEn !== null && shop.goods.introduceEn !== ''">
                  <span>{{shop.goods.introduceEn}}</span>
                </div>
              </div>
              <div class="moneyLabel">
                <div style="float: left;" v-if="Number(fomatFloat(shop.goods.salePrice,2)) !== 0">
                  <span class="currency">{{($i18n.locale === 'en' && $root.currency === 'USD') ? '$':'¥'}}</span>
                  <span class="money">{{($i18n.locale === 'en' && $root.currency === 'USD') ?fomatFloat(shop.goods.salePriceEn,2):fomatFloat(shop.goods.salePrice,2)}}</span>
                </div>
                <div style="float: left;" v-else>
                  <span class="money1">{{$t('P.NoPricing')}}</span>
                </div>
                <div v-if="$i18n.locale === 'zh'" style="float: right;">
                  <span v-if="shop.goods.saleCount > 10000" class="saleCount">{{$t('P.sale')}} {{fomatFloor(shop.goods.saleCount / 10000,2)}} 万+</span>
                  <span v-else class="saleCount">{{$t('P.sale')}} {{shop.goods.saleCount}}</span>
                </div>
                <div v-else style="float: right;">
                  <span v-if="shop.goods.saleCount > 1000" class="saleCount">{{fomatFloor(shop.goods.saleCount / 1000,1)}}K+ {{$t('P.sale')}}</span>
                  <span v-else class="saleCount">{{shop.goods.saleCount}} {{$t('P.sale')}}</span>
                </div>
              </div>
            </div>
          </div>
        </li>
      </ul>
      <!---左右分布---->
      <div v-if="type === 2" class="recommend-goods-list rl-clear">
        <div class="grid-promo">
          <img src="../assets/images/index/banner.png" alt="" class="banner-img">
        </div>
        <div class="grid-list">
          <div class="grid-hot">
            <div class="item-tag">{{$t('P.SellWell')}}</div>
            <div class="item-box">
              <div class="title">手机壳限时优惠</div>
              <div class="desc">苹果TPU电镀保护壳苹果TPU电镀苹果TPU电镀保护壳苹果TPU电镀</div>
              <div class="price">原价 <span>¥88.99</span></div>
              <div class="salePrice">仅需<em>¥59.99</em></div>
              <div class="btn-sel">立即查看</div>
            </div>
            <div class="item-img">
              <img src="../assets/images/index/product.png" alt="">
            </div>
          </div>
          <div class="grid-item">
            <div class="item-tag">{{$t('P.SellWell')}}</div>
            <div class="item-img">
              <img src="../assets/images/index/product.png" alt="">
            </div>
            <div class="item-box">
              <div class="title">手机壳限时优惠</div>
              <div class="desc">苹果TPU电镀保护壳苹果TPU电镀苹果TPU电镀保护壳苹果TPU电镀</div>
              <div class="moneyLabel">
                <div class="price">
                  <span class="currency">¥99.99</span>
                  <span class="money">¥69.88</span>
                </div>
                <div class="sale">
                  <span class="saleCount">{{$t('P.sale')}} 1523</span>
                </div>
              </div>
            </div>
          </div>
          <div class="grid-item">
            <div class="item-tag">热销</div>
            <div class="item-img">
              <img src="../assets/images/index/product.png" alt="">
            </div>
            <div class="item-box">
              <div class="title">手机壳限时优惠</div>
              <div class="desc">苹果TPU电镀保护壳苹果TPU电镀苹果TPU电镀保护壳苹果TPU电镀</div>
              <div class="moneyLabel">
                <div class="price">
                  <span class="currency">¥99.99</span>
                  <span class="money">¥69.88</span>
                </div>
                <div class="sale">
                  <span class="saleCount">{{$t('P.sale')}} 1523</span>
                </div>
              </div>
            </div>
          </div>
          <div class="grid-item">
            <div class="item-tag">热销</div>
            <div class="item-img">
              <img src="../assets/images/index/product.png" alt="">
            </div>
            <div class="item-box">
              <div class="title">手机壳限时优惠</div>
              <div class="desc">苹果TPU电镀保护壳苹果TPU电镀苹果TPU电镀保护壳苹果TPU电镀</div>
              <div class="moneyLabel">
                <div class="price">
                  <span class="currency">¥99.99</span>
                  <span class="money">¥69.88</span>
                </div>
                <div class="sale">
                  <span class="saleCount">{{$t('P.sale')}} 1523</span>
                </div>
              </div>
            </div>
          </div>
          <div class="grid-item">
            <div class="item-tag">热销</div>
            <div class="item-img">
              <img src="../assets/images/index/product.png" alt="">
            </div>
            <div class="item-box">
              <div class="title">手机壳限时优惠</div>
              <div class="desc">苹果TPU电镀保护壳苹果TPU电镀苹果TPU电镀保护壳苹果TPU电镀</div>
              <div class="moneyLabel">
                <div class="price">
                  <span class="currency">¥99.99</span>
                  <span class="money">¥69.88</span>
                </div>
                <div class="sale">
                  <span class="saleCount">{{$t('P.sale')}} 1523</span>
                </div>
              </div>
            </div>
          </div>
          <div class="grid-item">
            <div class="item-tag">热销</div>
            <div class="item-img">
              <img src="../assets/images/index/product.png" alt="">
            </div>
            <div class="item-box">
              <div class="title">手机壳限时优惠</div>
              <div class="desc">苹果TPU电镀保护壳苹果TPU电镀苹果TPU电镀保护壳苹果TPU电镀</div>
              <div class="moneyLabel">
                <div class="price">
                  <span class="currency">¥99.99</span>
                  <span class="money">¥69.88</span>
                </div>
                <div class="sale">
                  <span class="saleCount">{{$t('P.sale')}} 1523</span>
                </div>
              </div>
            </div>
          </div>
          <div class="grid-item">
            <div class="item-tag">热销</div>
            <div class="item-img">
              <img src="../assets/images/index/product.png" alt="">
            </div>
            <div class="item-box">
              <div class="title">手机壳限时优惠</div>
              <div class="desc">苹果TPU电镀保护壳苹果TPU电镀苹果TPU电镀保护壳苹果TPU电镀</div>
              <div class="moneyLabel">
                <div class="price">
                  <span class="currency">¥99.99</span>
                  <span class="money">¥69.88</span>
                </div>
                <div class="sale">
                  <span class="saleCount">{{$t('P.sale')}} 1523</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!---左右列表分布---->
      <div v-if="type === 3" class="recommend-goods-list rl-clear">
        <div class="grid-promo">
          <img src="../assets/images/index/banner.png" alt="" class="banner-img">
        </div>
        <div class="grid-list">
          <div class="grid-item" @click="goDoodsDetail(shop,shop.goods.id,shop.goods.goodsType)" v-for="(shop, index) in shopList" :key="shop.id" v-if="index<8">
            <div class="item-tag">{{$t('P.SellWell')}}</div>
            <div class="item-img">
              <img :src="(($i18n.locale === 'zh' || !shop.goods.imageUrl1en == true)?shop.goods.imageUrl1:shop.goods.imageUrl1en) + '?x-oss-process=image/resize,h_458,l_458'" alt="">
<!--              <img src="../assets/images/index/product.png" alt="">-->
            </div>
            <div class="item-box">
                <div class="title">{{($i18n.locale === 'zh' || !shop.goods.goodsNameEn == true) ? shop.goods.goodsName:shop.goods.goodsNameEn}}</div>
                <div class="desc">{{shop.goods.introduce}}</div>
                <div class="moneyLabel">
                <div class="price" v-if="Number(fomatFloat(shop.goods.salePrice,2)) !== 0">
                  <span class="currency">{{($i18n.locale === 'en' && $root.currency === 'USD') ? '$':'¥'}}{{($i18n.locale === 'en' && $root.currency === 'USD') ?fomatFloat(shop.goods.salePriceEn,2):fomatFloat(shop.goods.salePrice,2)}}</span>
                  <span class="money">¥69.88</span>
                </div>
                  <div v-else class="price">
                    <span class="money1">{{$t('P.NoPricing')}}</span>
                  </div>
                <div class="sale">
                  <span class="saleCount">{{$t('P.sale')}} 1523</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!---列表分布---->
      <ul v-if="type === 4" class="rl-clear">
        <li @click="goDoodsDetail(shop,shop.goods.id,shop.goods.goodsType)" v-for="shop in shopList" :key="shop.id">
          <div class="content" v-if="shop.goods">
            <div class="img"><img :src="(($i18n.locale === 'zh' || !shop.goods.imageUrl1en == true)?shop.goods.imageUrl1:shop.goods.imageUrl1en) + '?x-oss-process=image/resize,h_458,l_458'" alt=""></div>
            <div >
              <div v-if="$i18n.locale === 'zh'">
                <div v-if="(shop.goods.labelNames === undefined || shop.goods.labelNames === null || shop.goods.labelNames.length === 0) &&
            (shop.goods.introduce === undefined || shop.goods.introduce === null || shop.goods.introduce.length === 0)" class="brand rl-tc rl-margin-top-xxxs" >
                  <img class="imgLable" v-show="shop.goods.promotionStatus === 1" src="../../src/assets/images/hot-big.png" alt="">
                  <img class="imgLable1" v-show="shop.goods.newProductStatus === 1 && shop.goods.promotionStatus !== 1" src="../../src/assets/images/new-big.png" alt="">
                  <a>{{($i18n.locale === 'zh' || !shop.goods.goodsNameEn == true) ? shop.goods.goodsName:shop.goods.goodsNameEn}}</a>
                </div>
                <div v-else class="brand1 rl-tc rl-margin-top-xxxs" >
                  <img class="imgLable" v-show="shop.goods.promotionStatus === 1" src="../../src/assets/images/hot-big.png" alt="">
                  <img class="imgLable1" v-show="shop.goods.newProductStatus === 1 && shop.goods.promotionStatus !== 1" src="../../src/assets/images/new-big.png" alt="">
                  <a>{{($i18n.locale === 'zh' || !shop.goods.goodsNameEn == true) ? shop.goods.goodsName:shop.goods.goodsNameEn}}</a>
                </div>
                <div class="word1 rl-tc" v-show="$i18n.locale === 'zh' && shop.goods.labelNames !== undefined && shop.goods.labelNames !== null && shop.goods.labelNames.length>0">
                  <span class="label" v-for="(labelName,index) in shop.goods.labelNames" :key="index">{{labelName}}</span>
                </div>
                <div class="word rl-tc" v-show="$i18n.locale === 'zh' && (shop.goods.labelNames === undefined || shop.goods.labelNames === null || shop.goods.labelNames.length === 0)
            && shop.goods.introduce !== undefined && shop.goods.introduce !== null && shop.goods.introduce !== ''">
                  <span>{{shop.goods.introduce}}</span>
                </div>
              </div>
              <div v-else>
                <div v-if="(shop.goods.labelNameEns === undefined || shop.goods.labelNameEns === null || shop.goods.labelNameEns.length === 0) &&
            (shop.goods.introduceEn === undefined || shop.goods.introduceEn === null || shop.goods.introduceEn.length === 0)" class="brand rl-tc rl-margin-top-xxxs" >
                  <img class="imgLable" v-show="shop.goods.promotionStatus === 1" src="../../src/assets/images/hot-big.png" alt="">
                  <img class="imgLable1" v-show="shop.goods.newProductStatus === 1 && shop.goods.promotionStatus !== 1" src="../../src/assets/images/new-big.png" alt="">
                  <a>{{($i18n.locale === 'zh' || !shop.goods.goodsNameEn == true) ? shop.goods.goodsName:shop.goods.goodsNameEn}}</a>
                </div>
                <div v-else class="brand1 rl-tc rl-margin-top-xxxs" >
                  <img class="imgLable" v-show="shop.goods.promotionStatus === 1" src="../../src/assets/images/hot-big.png" alt="">
                  <img class="imgLable1" v-show="shop.goods.newProductStatus === 1 && shop.goods.promotionStatus !== 1" src="../../src/assets/images/new-big.png" alt="">
                  <a>{{($i18n.locale === 'zh' || !shop.goods.goodsNameEn == true) ? shop.goods.goodsName:shop.goods.goodsNameEn}}</a>
                </div>
                <div class="word rl-tc" v-show="$i18n.locale === 'en' && shop.goods.labelNameEns !== undefined && shop.goods.labelNameEns !== null && shop.goods.labelNameEns.length>0">
                  <span class="label" v-for="(labelNameEns,index) in shop.goods.labelNameEns" :key="index">{{labelNameEns}}</span>
                </div>
                <div class="word rl-tc" v-show="$i18n.locale === 'en' && (shop.goods.labelNameEns === undefined || shop.goods.labelNameEns === null || shop.goods.labelNameEns.length === 0)
            && shop.goods.introduceEn !== undefined && shop.goods.introduceEn !== null && shop.goods.introduceEn !== ''">
                  <span>{{shop.goods.introduceEn}}</span>
                </div>
              </div>
              <div class="moneyLabel">
                <div style="float: left;" v-if="Number(fomatFloat(shop.goods.salePrice,2)) !== 0">
                  <span class="currency">{{($i18n.locale === 'en' && $root.currency === 'USD') ? '$':'¥'}}</span>
                  <span class="money">{{($i18n.locale === 'en' && $root.currency === 'USD') ?fomatFloat(shop.goods.salePriceEn,2):fomatFloat(shop.goods.salePrice,2)}}</span>
                </div>
                <div style="float: left;" v-else>
                  <span class="money1">{{$t('P.NoPricing')}}</span>
                </div>
                <div v-if="$i18n.locale === 'zh'" style="float: right;">
                  <span v-if="shop.goods.saleCount > 10000" class="saleCount">{{$t('P.sale')}} {{fomatFloor(shop.goods.saleCount / 10000,2)}} 万+</span>
                  <span v-else class="saleCount">{{$t('P.sale')}} {{shop.goods.saleCount}}</span>
                </div>
                <div v-else style="float: right;">
                  <span v-if="shop.goods.saleCount > 1000" class="saleCount">{{fomatFloor(shop.goods.saleCount / 1000,1)}}K+ {{$t('P.sale')}}</span>
                  <span v-else class="saleCount">{{shop.goods.saleCount}} {{$t('P.sale')}}</span>
                </div>
              </div>
            </div>
          </div>
        </li>
      </ul>
    </div>
</template>

<script>
  import GD from '@/assets/js/globalData'
  import {fomatFloat, fomatFloor} from '@/assets/js/common.js'
    export default {
      name: 'goodList',
      props: {
        itemId: {
          type: Number,
          default: 0
        },
        section_index: {
          type: Number,
          default: 0
        },
        section_img: {
          type: String,
          default: ''
        },
        section_img1: {
          type: String,
          default: ''
        },
        section_url: {
          type: String,
          default: ''
        },
        section_url1: {
          type: String,
          default: ''
        },
        section_length: {
          type: Number,
          default: 0
        },
        showLoading: {
          type: Boolean
        },
        type: {
          type: Number,
          default: 0
        }
      },
      data () {
        return {
          shopList: [], // 首页商品名称,
          pageNo: 1,
          pageSize: 10,
          useLang: false, // 是否使用多语种
          langList: GD.langList, // 语种列表
          lang: 'zh-RMB' // 语种
        }
      },
      methods: {
        fomatFloor (val, n) {
          return fomatFloor(val, n)
        },
        fomatFloat (val, n) {
          return fomatFloat(val, n)
        },
        fLangChange (value) {
          window.localStorage.setItem('bLang', value);
          this.$i18n.locale = value.split('_')[0];
        },
        shopData () { // 商品分类
          var myDate = new Date()
          let goodsIdList = []
          let goodsIds = "";
          this.$api.get(this, 'user/goods/list?' + myDate.getMinutes() + myDate.getSeconds(), {page: this.pageNo, count: this.pageSize, sectionId: this.itemId}).then(res => {
            if (this.section_index === (this.section_length - 1)) {
              this.$emit('input', false)
            }
            if (res.code === 0) {
              this.shopList = res.goodsList
              if (this.shopList.length > 0) {
                this.shopList.forEach(item => {
                  goodsIdList.push(item.goods.id)
                  goodsIds += item.goods.id + ",";
               })
                if (this.userId !== undefined && this.userId !== null && this.userId !== '' && goodsIdList.length > 0) {
                  this.$api.post(this, 'user/goods/price/list?' + myDate.getMinutes() + myDate.getSeconds(), {goodsIdList: goodsIdList}).then(res => {
                    if (res.code === 0 && res.goodsList !== undefined && res.goodsList !== null && res.goodsList.length > 0) {
                      this.shopList.forEach(item => {
                        for (let i = 0; i < res.goodsList.length; i++) {
                          if (item.goods.id === res.goodsList[i].goodsId) {
                            item.goods.labelNames = res.goodsList[i].labelNames
                            item.goods.labelNameEns = res.goodsList[i].labelNameEns
                            item.goods.salePrice = res.goodsList[i].minPrice
                            item.goods.salePriceEn = res.goodsList[i].minPriceEn
                            if (res.goodsList[i].discount !== undefined && res.goodsList[i].discount !== null && res.goodsList[i].discount > 0) {
                              item.goods.labelNames.splice(0, 0, '低至' + res.goodsList[i].discount + '折');
                              item.goods.labelNameEns.splice(0, 0, 'Up to ' + (100 - (res.goodsList[i].discount * 10)) + '% off')
                            }
                            res.goodsList.splice(i, 1)
                            i--;
                          }
                        }
                      })
                    }
                  })
                }

                // 获取销量（包含虚拟销量）
                if (goodsIds) {
                  goodsIds = goodsIds.substring(0, goodsIds.length - 1);
                  this.$api
                    .get(this, "user/goods/sale/list", { goodsIds: goodsIds })
                    .then((res) => {
                      if (res.code === 0) {
                        if (
                          res.goodsSaleCounts &&
                          res.goodsSaleCounts.length > 0
                        ) {
                          this.shopList.forEach((item) => {
                            item.goods.saleCount = 0;
                            for (let i = 0; i < res.goodsSaleCounts.length; i++) {
                              if (item.goods.id === res.goodsSaleCounts[i].goodsId) {
                                item.goods.saleCount = res.goodsSaleCounts[i].saleCount;
                              }
                            }
                          });
                        } else {
                          this.shopList.forEach((item) => {
                            item.goods.saleCount = 0;
                          });
                        }
                      }
                    });
                }
              }
            }
          })
        },
        // 去商品详情
        goDoodsDetail (item, id, goodsType) {
          // this.$router.push({name: 'ShopDetail', query: {good_id: id, activityType: this.activityType, goodsType: goodsType}})
          if (item.promotion) {
            let routeData = this.$router.resolve({
              name: 'ShopDetail',
              query: {good_id: id, activityType: 1, goodsType: item.goods.goodsType, accessType: 0}
            })
            window.open(routeData.href, '_blank')
            // this.$router.push({name: 'ShopDetail', query: {good_id: id, activityType: 1, goodsType: item.goods.goodsType}}) // 跳转活动商品
          } else {
            let routeData = this.$router.resolve({
              name: 'ShopDetail',
              query: {good_id: id, goodsType: item.goods.goodsType, accessType: 0}
            })
            window.open(routeData.href, '_blank')
            // this.$router.push({name: 'ShopDetail', query: {good_id: id, goodsType: item.goods.goodsType}})
          }
        }
      },
      created () {
        this.useLang = this.$b2bConfig.lang;
        this.lang = window.localStorage.getItem('bLang') ? window.localStorage.getItem('bLang') : 'zh-RMB';
      },
      mounted () {
        this.shopData()
      }
    }
</script>

<style scoped lang='less'>
  .goodslist{
    .recommend-goods-list{
      width:100%;
      font-size: 0;
      .grid-promo{
        display: inline-block;
        width:230px;
        height: 652px;
        border-radius: 16px;
        background-color: #F5F7FA;
        cursor: pointer;
        &:hover{
          box-shadow: 0 15px 30px rgba(0,0,0,0.1);
          transform: translate3d(0,-2px,0);
        }
        .banner-img{
          display: inline-block;
          width:100%;
          height: 100%;
        }
      }
      .grid-list{
        display: inline-block;
        width:calc(100% - 230px);
        vertical-align: top;
        .grid-hot{
          position: relative;
          display: inline-block;
          margin-left: 12px;
          width:472px;
          height: 347px;
          padding:30px;
          font-size: 0;
          box-sizing: border-box;
          border-radius: 16px;
          background-color: #F5F7FA;
          vertical-align: top;
          cursor: pointer;
          float:left;
          .item-tag{
            position: absolute;
            top:0;
            left:50%;
            width:40px;
            height: 20px;
            line-height: 20px;
            font-size: 12px;
            text-align: center;
            transform: translateX(-50%);
            color:#ffffff;
            background-color: #F16E7B;
            border-radius:0 0 4px 4px;
            z-index: 99;
          }
          .item-box{
            display: inline-block;
            width:calc(100% - 230px);
            vertical-align: top;
            .title{
              display: inline-block;
              margin-top:30px;
              font-size: 20px;
              color:#333333;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            }
            .desc{
              display: inline-block;
              width: 100%;
              font-size: 12px;
              color:#777777;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            }
            .price{
              display: block;
              margin-top:36px;
              font-size: 14px;
              color:#3A3A3A;
              span{
                display: inline-block;
                font-size: 12px;
                color: #4A4A4A;
              }
            }
            .salePrice{
              display: inline-block;
              margin-top:3px;
              font-size: 26px;
              color:#00B8D0;
              em{
                display: inline-block;
                font-size: 16px;
                color: #CB3342;
              }
            }
            .btn-sel{
              display: inline-block;
              margin-top:30px;
              padding:6px 30px;
              font-size: 14px;
              color: #ffffff;
              background-color: #00B8D0;
              border-radius: 16px;
            }
          }
          .item-img{
            display: inline-block;
            width:230px;
            border-radius: 16px 16px 0 0;
            img{
              width:230px;
              height: auto;
            }
          }
          &:hover{
            box-shadow: 0 15px 30px rgba(0,0,0,0.1);
            transform: translate3d(0,-2px,0);
          }
        }
        .grid-item{
          position: relative;
          display: inline-block;
          width:230px;
          height: 320px;
          margin: 0 0 12px 12px;
          box-sizing: border-box;
          background-color: #F5F7FA;
          border-radius: 16px;
          cursor: pointer;
          float:left;
          &:first-child, &:nth-child(5){
            margin-left:14px;
          }
          &:hover{
            box-shadow: 0 15px 30px rgba(0,0,0,0.1);
            transform: translate3d(0,-2px,0);
          }
          .item-tag{
            position: absolute;
            top:0;
            left:50%;
            width:40px;
            height: 20px;
            line-height: 20px;
            font-size: 12px;
            text-align: center;
            transform: translateX(-50%);
            color:#ffffff;
            background-color: #F16E7B;
            border-radius:0 0 4px 4px;
            z-index: 99;
          }
          .item-img{
            position:relative;
            width:230px;
            height: 230px;
            overflow: hidden;
            border-radius: 16px 16px 0 0;
            img{
              position: absolute;
              top:50%;
              left:50%;
              transform: translate3d(-50%, -50%, 0);
              width:100%;
              height: auto;
            }
          }
          .item-box{
            display: block;
            width:100%;
            height: 90px;
            padding: 8px 15px 11px;
            box-sizing: border-box;
            font-size: 0;
            .title{
              display: inline-block;
              width:100%;
              font-size: 14px;
              line-height: 20px;
              color:#000000;
              text-overflow: ellipsis;
              overflow: hidden;
              white-space: nowrap;
              border-radius: 0;
            }
            .desc{
              display: inline-block;
              width:100%;
              font-size: 12px;
              line-height: 20px;
              color:#777777;
              text-overflow: ellipsis;
              overflow: hidden;
              white-space: nowrap;
              border-radius: 0;
            }
            .moneyLabel{
              position: absolute;
              width:calc(100% - 30px);
              left:15px;
              bottom:11px;
              display: block;
              .price{
                display: inline-block;
                .currency{
                  display: inline-block;
                  font-size: 16px;
                  color: #CB3342;
                }
                .money{
                  display: inline-block;
                  margin-left:6px;
                  font-size: 12px;
                  color: #777777;
                }
              }
              .sale{
                display: inline-block;
                float:right;
                .saleCount{
                  display: inline-block;
                  font-size: 12px;
                  color: #777777;
                }
              }
            }
          }
        }
      }
    }
    .home-list{
      width:100%;
      .nav-top{
        width: 100%;
        font-size: 0;
        .nav-item{
          display: inline-block;
          width:594px;
          height: 320px;
          margin-bottom: 12px;
          border-radius: 16px;
          background-color: #F5F7FA;
          cursor: pointer;
          img{
            display: inline-block;
            width:100%;
            height: 100%;
          }
          &:hover{
            box-shadow: 0 7px 15px rgba(0,0,0,0.1);
            transform: translate3d(0,-2px,0);
          }
          &:last-child{
            margin-left: 12px;
          }
        }
      }
    }
  }
  .shop{
    ul{
      li{
        margin-bottom: 12px;
        margin-right: 12px;
        width: 230px;
        height: 320px;
        float: left;
        cursor: pointer;
        transition: all .2s linear;
        box-sizing: border-box;
        background-color: #F5F7FA;
        border-radius: 16px;
        &:last-child{
          margin-right:0
        }
        &:hover{
          box-shadow: 0 15px 30px rgba(0,0,0,0.1);
          transform: translate3d(0,-2px,0);
        }
      .content{
        display: inline-block;
        width: 100%;
        .item-tag{
          position: absolute;
          top:0;
          left:50%;
          width:40px;
          height: 20px;
          line-height: 20px;
          font-size: 12px;
          text-align: center;
          transform: translateX(-50%);
          color:#ffffff;
          background-color: #F16E7B;
          border-radius:0 0 4px 4px;
          z-index: 99;
        }
      .img{
        position: relative;
        width: 230px;
        height: 230px;
        display: table-cell; /*主要是这个属性*/
        vertical-align: middle;
        text-align: center;
        overflow: hidden;
        border-radius: 16px 16px 0 0;
        img{
          position: absolute;
          top:50%;
          left:50%;
          transform: translate3d(-50%, -50%, 0);
          width:100%;
          height: auto;
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
        font-size:12px;
        height: 50px;
        width: 100%;
        padding: 0 15px;
        box-sizing: border-box;
        font-family:FZHei-B01;
        font-weight:400;
        color:rgba(51,51,51,1);
        line-height:22px;
        overflow: hidden;
        float: left;
        text-align:left;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
      }
      .brand1{
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
        width: 100%;
        float: left;
        padding: 0 15px;
        box-sizing: border-box;
        line-height: 22px;
        text-align: left;
        vertical-align: bottom;
        background-color: #F5F7FA;
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
      }
    }
</style>
