<template>
  <div>
    <div class="user rl-margin-zero" v-if="Number(source) === 0">
      <!--公共头部-->
      <Header :userState="userState" :loginState="login_state"></Header>
      <div v-if="showDetail === true" class="nav-bottom rl-bg-gray-mm rl-padding-top-xxxs rl-padding-bottom-xxxs rl-margin-zero">
        <div class="rl-text-xxs bleed">
          <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{name: 'Index'}">首页</el-breadcrumb-item>
            <el-breadcrumb-item>DIY定制商品</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
      </div>
      <!--主内容-->
      <div v-if="showDetail === true" class="mainOne rl-padding-top-default rl-padding-bottom-default rl-bg-white rl-clear rl-margin-zero">
        <div class="product-all rl-clear">
          <div class="pic-box rl-fl">
            <div class="pic-move rl-margin-bottom-xxxxs rl-relative">
              <pic-zoom :url="shopImg" :scale="3" :scroll="true"></pic-zoom>
            </div>
            <div class="pic-silde rl-clear">
              <!-- <div class="turn-left rl-fl"></div>-->
              <div class="silde-img rl-fl" v-if="goodsDetail">
                <div v-if="goodsDetail.imageUrl1 !== ''" @click="chooseImg(goodsDetail.imageUrl1,0)" class="item rl-fl"><img :class="{'rl-bd-blue-xs':colorBlue === 0}" width="100%" :src="goodsDetail.imageUrl1 + '?x-oss-process=image/resize,h_104,l_104'" alt=""></div>
                <div v-if="goodsDetail.imageUrl2 !== ''" @click="chooseImg(goodsDetail.imageUrl2,1)" class="item rl-fl"><img :class="{'rl-bd-blue-xs':colorBlue === 1}" width="100%" :src="goodsDetail.imageUrl2 + '?x-oss-process=image/resize,h_104,l_104'" alt=""></div>
                <div v-if="goodsDetail.imageUrl3 !== ''" @click="chooseImg(goodsDetail.imageUrl3,2)" class="item rl-fl"><img :class="{'rl-bd-blue-xs':colorBlue === 2}" width="100%" :src="goodsDetail.imageUrl3 + '?x-oss-process=image/resize,h_104,l_104'" alt=""></div>
                <div v-if="goodsDetail.imageUrl4 !== ''" @click="chooseImg(goodsDetail.imageUrl4,3)" class="item rl-fl"><img :class="{'rl-bd-blue-xs':colorBlue === 3}" width="100%" :src="goodsDetail.imageUrl4 + '?x-oss-process=image/resize,h_104,l_104'" alt=""></div>
                <div v-if="goodsDetail.imageUrl5 !== ''" @click="chooseImg(goodsDetail.imageUrl5,4)" class="item rl-fl"><img :class="{'rl-bd-blue-xs':colorBlue === 4}" width="100%" :src="goodsDetail.imageUrl5 + '?x-oss-process=image/resize,h_104,l_104'" alt=""></div>
                <div v-if="goodsDetail.imageUrl6 !== ''" @click="chooseImg(goodsDetail.imageUrl6,5)" class="item rl-fl"><img :class="{'rl-bd-blue-xs':colorBlue === 5}" width="100%" :src="goodsDetail.imageUrl6 + '?x-oss-process=image/resize,h_104,l_104'" alt=""></div>
              </div>
              <!--<div class="turn-right rl-fr"></div>-->
            </div>
          </div>
          <div class="text-box rl-fl" v-if="goodsDetail">
            <div class="head rl-clear">
              <div class="rl-fl goodsName">{{goodsDetail.goodsName}}</div>
              <div class="rl-fr collection" @click="joinCollect" v-if="!this.userId == '' && this.isCollection === true">
                <img src="../../src/assets/images/no_collection.png" alt=""> {{$t('P.Collect')}}
                <span style="text-align: center;"></span>
              </div>
              <div class="rl-fr collection" @click="cancelCollect" v-if="!this.userId == '' && this.isCollection === false">
                <img src="../../src/assets/images/yes_collection.png" alt=""> {{$t('P.Collect')}}
                <span style="text-align: center;"></span>
              </div>
            </div>
            <div class="introduce">
              <span v-show="$i18n.locale === 'zh' ">{{goodsDetail.introduce}}</span>
              <span v-show="$i18n.locale === 'en'">{{goodsDetail.introduceEn}}</span>
            </div>
            <!--商品等级折扣和提货增长返利-->
            <div class="content rl-relative">
              <div class="item spe rl-clear">
                <div class="rl-fl rl-tl rl-text-xxss rl-text-gray">商品编号：</div>
                <div class="rl-fl">{{goodsDetail.goodsNo}}</div>
              </div>
              <div class="item rl-clear">
                <div class="rl-fl rl-tl rl-text-xxss rl-text-gray">品牌：</div>
                <div class="rl-fl">{{goodsDetail.brandName}}</div>
              </div>
              <div class="item rl-clear" v-if="!this.userId == ''">
                <div class="rl-fl rl-tl rl-text-xxss rl-text-gray">上架时间：</div>
                <div class="rl-fl">{{goodsDetail.saleTime | formatDate}}</div>
              </div>
              <div class="item rl-clear" v-else>
                <div class="rl-fl rl-tl rl-text-xxss rl-text-gray">上架时间：</div>
                <div class="rl-fl">登录后查看</div>
              </div>
              <div class="item rl-clear" v-if="!this.userId == ''">
                <div class="rl-fl rl-tl rl-text-xxss rl-text-gray">销量：</div>
                <div class="rl-fl">{{goodsDetail.saleCount}}</div>
              </div>
              <div class="item rl-clear" v-else>
                <div class="rl-fl rl-tl rl-text-xxss rl-text-gray">销量：</div>
                <div class="rl-fl">登录后查看</div>
              </div>
              <div class="customize rl-clear" >
                <div @click="goCustomize" class="button">定制</div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div v-if="showDetail === true" class="mainTwo rl-margin-zero">
        <!--<div class="shop-table rl-relative">
          <table>
            <tr>
              <th width="110px">图片</th>
              <th width="100px">货号</th>
              <th width="100px">条形码</th>
              <th width="75px">规格</th>
              <th width="75px">颜色</th>
              <th width="105px" v-if="showRetailPrice === true">建议零售价</th>
              <th width="105px">会员价</th>
              <th width="140px">订购数量</th>
              <th width="80px">装箱数</th>
              <th width="80px">尺寸</th>
              <th width="80px">重量(g)</th>
              <th width="60px">操作</th>
            </tr>
            <tr>
              <td colspan="12" class="fen-group rl-clear">
                <div class="btn-prev rl-fl" @click="btnPrev"></div>
                <div class="fen-group-ul rl-fl rl-relative">
                  <ul id="boxUl">
                    <li id="boxLi" @click="changeTab(index)" v-for="(item,index) in customizedItems" :key="item.id" :class ="{'current':tabType === index}">
                      <div v-if="item.itemGroupRemark !== null && item.itemGroupRemark !== ''">
                        <el-tooltip class="item" effect="dark" :content="item.itemGroupRemark" placement="bottom" >
                          <el-button>{{item.itemGroup}}</el-button>
                        </el-tooltip>
                      </div>
                      <div v-else>{{item.itemGroup}}</div>
                    </li>
                  </ul>
                </div>
                <div class="btn-next rl-fl" @click="btnNext"></div>
              </td>
            </tr>
          </table>
          <table v-for="(item,index) in customizedItems" :key="item.id">
            <tr v-for="goods in item.userItems" :key="goods.id" class="rl-bg-white rl-bdb-gray-sm rl-relative" v-show="tabType === index">
              <td width="110px"><div class="shop-img cursor-pointer"><img @click="magnify(goods.itemImg)" width="100%" :src="goods.itemImg" alt=""></div></td>
              <td width="100px">{{goods.itemCode}}</td>
              <td width="100px">{{goods.barCode}}</td>
              <td width="75px">
                <el-tooltip class="item" effect="dark" :content="goods.specificationValueName" placement="bottom">
                  <el-button>{{goods.specificationValueName}}</el-button>
                </el-tooltip>
              </td>
              <td width="75px">
                <el-tooltip class="item" effect="dark" :content="goods.colorValueName" placement="bottom">
                  <el-button>{{goods.colorValueName}}</el-button>
                </el-tooltip>
              </td>
              <td width="105px" v-if="!userId == '' && showRetailPrice === true"><span v-if="goods.retailPrice !== null && Number(goods.retailPrice) !== 0">￥{{goods.retailPrice}}</span>
                <span v-else>&#45;&#45;</span></td>
              <td width="105px" v-else-if="(userId === ''|| userId === null) && showRetailPrice === true" class="rl-text-xxss">登录后查看</td>
              <td width="105px" v-if="!userId == ''"><span v-if="Number(goods.salePrice) !== 0 && goods.salePrice !== null">￥{{goods.salePrice}}</span><span v-else>暂未定价</span></td>
              <td width="105px" v-else class="rl-text-xxss">登录后查看</td>
              <td width="140px" v-if="!userId == ''">
                <div class="rl-margin-left-xxxs">
                  <el-input-number v-model="diyNum" :min="1"  label="描述文字" size="mini" ></el-input-number>
                </div>
              </td>
              <td width="140px" v-else class="rl-text-xxss">登录后查看</td>
              <td width="80px"><el-button @click.native.prevent="lookBox(goods.itemCode)" style="color: #00c9dc">查看详情</el-button></td>
              <td width="80px" v-if="goods.sizeStr !== null && goods.unit !== null">
                <el-tooltip class="item" effect="dark" :content="goods.sizeStr + goods.unit" placement="bottom">
                  <el-button>{{goods.sizeStr}}({{goods.unit}})</el-button>
                </el-tooltip>
              </td>
              <td width="80px" v-else></td>
              <td width="80px" class="rl-bdr-gray-sm">{{goods.weight}}</td>
              <td @click="goCustrom(goods.itemCode)" width="60px" class="rl-bdr-gray-sm"><div class="dingzhi-btn">定制</div></td>
            </tr>
          </table>
        </div>
        <div class="buy-list rl-clear rl-padding-top-mid rl-padding-bottom-mid rl-padding-left-default rl-padding-right-double">
          <div class="rl-fl">采购清单</div>
          <div class="rl-fr">共<span class="rl-text-orange-mm rl-text-mid rl-padding-left-xxxxs rl-padding-right-xxxxs rl-text-bold">{{totalCount}}</span>件<span class="rl-text-orange-mm rl-text-mid rl-padding-left-xxxs rl-padding-right-xxxxs rl-text-bold">{{totalPrice | keepTwoNum}}</span>元</div>
        </div>-->
        <div class="shop-descripe rl-margin-top-default">
          <div class="head rl-bg-blue-xs rl-text-white rl-padding-left-default">商品描述</div>
          <div class="img-box-p" v-html="goodsDetail.contentUrl"></div>
        </div>
      </div>
      <!--找不到商品-->
      <div class="empty-car rl-margin-zero" v-if="showDetail === false">
        <div class="empty-car-img rl-margin-zero"><img width="100%" src="../assets/images/goods-empty.png" alt=""></div>
        <p class="rl-tc rl-margin-top-default rl-margin-bottom-default rl-text-sm rl-text-gray">找不到商品，商品可能已下架</p>
      </div>
    </div>
    <!--    选择赠品-->
    <el-dialog class="alls" title="选择赠品" :visible.sync="dialogVisible">
      <div class="rl-padding-left-default rl-padding-bottom-xxxs rl-padding-top-xxxs rl-bd-gray-sm">可领取最多{{presentCount}}件，已选{{hasChoose}}件</div>
      <div class="shop-table max-height300 overflow-y rl-padding-left-default rl-padding-right-default">
        <table>
          <tbody>
          <tr v-for="item in showSongGoodsList" :key="item.id">
            <td width="150px" class="rl-text-xxs">{{item.itemCode}}</td>
            <td width="150px" class="rl-tc"><div class="songImg rl-margin-zero"><img width="100%" :src="item.imgUrl" alt=""></div></td>
            <td width="150px" class="rl-text-xxs">{{item.goodsName}}</td>
            <td width="150px">
              <songSum ref="songSum" :songShop="item" :maxSongSum="presentCount" :hasChoose="hasChoose"></songSum>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="confirmSongShop">确 定</el-button>
        <el-button @click="dialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>
    <!--        DIY定制商品选择框-->
    <el-dialog class="alls" :close-on-click-modal="diymodal" title="" :visible.sync="diyDialogVisible">
      <div class="shop-table diy-css max-height300 rl-padding-left-default rl-padding-right-default rl-clear">
        <div class="left-img rl-fl">
          <div class="diy-images"><img :src="diyPic  + '?x-oss-process=image/resize,h_300,l_300'" alt=""></div>
        </div>
        <div class="right-cons rl-fl">
          <div class="item item-name"><span class="rl-margin-right-default">货品名称:</span><span>{{diyItems.itemName}}</span></div>
          <div class="item"><span class="rl-margin-right-default">货品编码:</span><span>{{diyItems.itemCode}}</span></div>
          <div class="item"><span class="rl-margin-right-default">型号:</span><span>{{diyItems.specValueName}}</span></div>
          <div class="item"><span class="rl-margin-right-default">材质:</span><span>{{diyItems.materialValueName}}</span></div>
          <div class="rl-clear">
            <div class="rl-fl rl-margin-right-default">购买数量:</div>
            <div class="rl-fl"><el-input-number v-model="diyNum" :min="1"  label="描述文字" size="mini" ></el-input-number></div>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button @click="diyBuy" type="warning">立即购买</el-button>
        <el-button :loading="loadsing"  @click.native.prevent="diyJoinCat" type="primary">加入购物车</el-button>
      </div>
    </el-dialog>
    <!--    装箱规格-->
    <!--<el-dialog class="alls" title="装箱规格" :visible.sync="boxDialogVisible">
      <div class="box-table rl-padding-left-default rl-padding-right-default">
        <el-table :data="boxData" border max-height="650" :header-cell-style="getRowClass" header-row-class-name="header-row" class="activity-el-table rl-tc">
          &lt;!&ndash;          <el-table-column label="箱子名称" prop="boxName"> </el-table-column>&ndash;&gt;
          <el-table-column label="箱子类型" prop="boxType"> </el-table-column>
          <el-table-column label="尺寸(长*宽*高)" prop="size"> </el-table-column>
          <el-table-column label="重量(g)" prop="boxWeight"> </el-table-column>
          <el-table-column label="装箱数量(个)" prop="boxNum"> </el-table-column>
        </el-table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="boxDialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>-->
    <!--加载动画-->
    <loading v-if="this.showLoading === true"></loading>
  </div>
</template>

<script>
  import Vue from 'vue'
  import Header from '@/components/Header.vue'
  import BuySum from '@/components/BuySum.vue'
  import presellNum from '@/components/presellNum.vue'
  import GoodsBuySum from '@/components/GoodsBuySum.vue'
  import {MessageBox} from 'element-ui'
  import PicZoom from 'vue-piczoom'
  import {formatDate, } from '@/assets/js/common.js'
  import songSum from '@/components/songSum.vue'
  import loading from '@/components/loading.vue'
  export default {
    name: 'ShopDetail',
    components: {
      Header,
      BuySum,
      presellNum,
      GoodsBuySum,
      PicZoom,
      songSum,
      loading
    },
    data () {
      return {
        userId: '',
        boxData: {},
        boxDialogVisible: false,
        source: 0, // 0：b2b diy定制 1：收单diy定制
        diymodal: false, // 通过点击 modal 关闭 Dialog
        showLoading: false, // 展示加载动画
        clickAgain: true, // 用于防止二次点击
        showDetail: true, // 展示商品详情
        capitalStatus: 0, // 资质状态:0,未提交 1,申请中,2,合作中 3,申请失败'
        freezeStatus: 0, // 冻结状态:1,冻结  2,未冻结',
        activityType: this.$route.query.activityType, // 接收活动
        userState: 2,
        login_state: true, // 后台跳入商品详情在登录后在返回商品详情
        id: this.$route.query.good_id,
        value: 1,
        goodsDetail: {},
        goodsItems: [],
        isCollection: true,
        shopImg: '',
        imgurl: '',
        showImgurl: false,
        colorBlue: 0, // 控制商品图片选中状态
        values: 0, // 跳转下单
        tuType: false, // 是否包含在途库存
        numInWarehouse: '',
        stockItemCount: '',
        stockoutCount: 0, // 缺货登记数量
        collecGoodstList: [], // 商品收藏列表
        onceAgain: true,
        promotion: {},
        promotionRules: {},
        accessType: this.$route.query.accessType, // 访问类型 0：前台访问 1后台访问
        goodsType: 2, // 2 定制商品
        customizedItems: [], // 定制商品货品列表
        tabType: 0, // 控制定制商品分组
        moreLabel: false, // 更多活动标签显隐
        songGoodsList: [], // 满赠货品
        showSongGoodsList: [],
        presentCountMap: {},
        presentCount: 0,
        promotionRuleId: '', // 活动规则id
        songGoods: 0, // 是否有赠品
        dialogVisible: false, // 赠品选择框显隐
        diyDialogVisible: false, // DIY定制商品选择框显隐
        hasActivity: false, // 是否有参加活动
        orderBuyMoney: 0,
        orderBuyCount: 0,
        distributeList: [], // 配送方式列表
        limitPurchase: [], // 限购商品列表
        gradeDiscountsRule: [], // 商品等级折扣规则
        pickUpRateRule: {}, // 提货增长返利规则
        stockShowFlag: 0, // 0 实际库存 1模糊库存
        stockShowNumber: 0, // 库存临界值
        showRetailPrice: true, // 是否展示零售价
        reduceOrPresent: 0, // 1满减2满赠3特价
        offsetLeft: '',
        setNum: 0,
        signature: '',
        diyId: '',
        diyNum: 1,
        goodsCode: '',
        diyDate: '',
        diyPic: '',
        diyPdf: '',
        diyItems: {},
        loadsing: false,
        diyItemCode: '',
        info: {} // 定制型号材质信息
      }
    },
    filters: {
      keepTwoNum (value) {
        value = Number(value)
        return value.toFixed(2)
      },
      formatDate (time) {
        var date = new Date(time)
        return formatDate(date, 'yyyy-MM-dd')
        // return formatDate(date, 'yyyy-MM-dd hh:mm:ss')
      }
    },
    computed: {
      totalCount () {
        var total = 0
        this.customizedItems.forEach((item) => {
          item.userItems.forEach((goods) => {
            if (goods.count) {
              total += Number(goods.count)
            }
          })
        })
        return total
      },
      totalPrice () {
        var total = 0
        this.customizedItems.forEach((item) => {
          item.userItems.forEach((goods) => {
            if (goods.count) {
              total += Number(goods.salePrice * goods.count)
            }
          })
        })
        return total
      },
      hasChoose () { // 已选择赠品数量
        var total = 0
        this.showSongGoodsList.forEach((item) => {
          if (item.num) {
            total += Number(item.num)
          }
        })
        return total
      }
    },
    methods: {
      goCustomize () {
        this.$router.push({path: '/customize', query: {goodsId: this.id}})
      },
      chooseImg (img, num) {
        this.shopImg = img
        this.colorBlue = num
      },
      changeTu () {
        this.tuType = !this.tuType
      },
      changeTab (index) { // 切换定制商品分组
        this.tabType = index
      },
      handleAdd () {
        this.stockoutCount++
      },
      handleReduce () {
        if (this.stockoutCount > 0) {
          this.stockoutCount--
        }
      },
      theSort (property) {
        return function (a, b) {
          var value1 = Number(a[property])
          var value2 = Number(b[property])
          return value1 - value2
        }
      },
      // 加入购物车
      joinShopCar () {
        if (this.userId === null || this.userId === '') {
        
        } else if (Number(this.capitalStatus) === 2 && Number(this.freezeStatus) === 2) {
          var tempArray = [] // 临时数组
          var type = 0
          var tuType = this.tuType
          // this.goodsItems.forEach((goods) => {
          // goods.goodsId = this.id
          // })
          this.clickAgain = false
          if (Number(this.goodsType) === 1) {
            if (this.goodsItems !== null) {
              this.goodsItems.forEach((obj) => {
                if (obj.count !== undefined && Number(obj.count) !== 0) {
                  if (tuType === false) {
                    if (Number(obj.count) > obj.numInWarehouse) {
                      type = 1
                      return false
                    }
                  } else {
                    if (Number(obj.count) > obj.stockItemCount) {
                      type = 1
                      return false
                    }
                  }
                  var product = {'goodsId': Number(obj.goodsId), 'itemId': obj.id, 'itemType': 1, 'num': obj.count, 'orderType': 1}
                  tempArray.push(product)
                }
              })
            }
          } else if (Number(this.goodsType) === 2) {
            this.customizedItems.forEach((item) => {
              item.userItems.forEach((obj) => {
                if (obj.count !== undefined && obj.count !== 0) {
                  var product = {'goodsId': obj.goodsId, 'itemId': obj.id, 'itemType': 3, 'num': obj.count, 'orderType': 3}
                  tempArray.push(product)
                }
              })
            })
          }
          var secondsArea = 1 // 设置0.5秒 延时加载
          this.intervalid = setInterval(() => {
            secondsArea--
            if (secondsArea === 0) {
              if (type === 0) {
                if (tempArray.length === 0) {
                  this.clickAgain = true
                  this.$message.warning(this.$t('P.GoodsItemNull'))
                  return false
                } else {
                  this.$api.post(this, 'user/u/shoppingCarts', {shoppingCars: tempArray}).then(res => {
                    if (res.code === 0) {
                      this.clickAgain = true
                      if (this.$i18n.locale === 'zh') {
                        this.$message.success('加入购物车成功')
                      } else {
                        this.$message.success('Add to shopping cart successfully.')
                      }
                    } else if (res.code === 3) {
                      this.clickAgain = true
                      
                    } else { this.clickAgain = true }
                  }).catch(() => { this.clickAgain = true })
                }
              } else {
                this.clickAgain = true
                this.$message.warning(this.$t('P.GoodsItemQuantityError'))
                return false
              }
              clearInterval(this.intervalid)
            }
          }, 500)
        } else if (Number(this.freezeStatus) === 1) {
          this.$message.warning('账号已冻结，无法进行此操作，请联系商务。')
        } else if (Number(this.capitalStatus) === 1) {
          this.$message.warning('账号仍在审核中，无法进行此操作，请等待审核通过。')
        } else if (Number(this.capitalStatus) === 3) {
          this.$message.warning('账号审核未通过，无法进行此操作。请重新发起申请。')
        } else if (Number(this.capitalStatus) === 4) {
          this.$message.warning('账号仍在审批中，无法进行此操作。请重新发起申请。')
        }
      },
      // 是否加入收藏(是否收藏)
      isNotJoinCollect () {
        var myDate = Math.random()
        this.$api.get(this, 'user/u/collection/check?' + myDate, {goodsId: Number(this.id)}).then(res => {
          if (res.code === 0) {
            this.isCollection = res.collection
          } 
        })
      },
      // 加入收藏(收藏添加)
      joinCollect () {
        this.$api.post(this, 'user/u/collection', {goodsId: Number(this.id)}).then(res => {
          if (res.code === 0) {
            this.$message.success('商品收藏成功!')
            this.isNotJoinCollect()
          } 
        })
      },
      cancel () {
        this.diyDialogVisible = false
      },
      // 取消收藏
      cancelCollect () {
        if (this.userId !== '') {
          var myDate = Math.random()
          this.$api.get(this, 'user/u/collection/list?' + myDate, {page: this.cur_page, count: this.pagesize}).then(res => {
            if (res.code === 0) {
              this.collecGoodstList = res.list
              var ids = this.id // 定义赋值，以免undefined取不到值
              this.collecGoodstList.forEach(item => {
                if (Number(item.goodsId) === Number(ids)) {
                  this.$api.delete(this, 'user/u/collection?' + myDate, {id: item.id}).then(res => {
                    if (res.code === 0) {
                      this.$message.success('商品取消收藏成功!')
                      this.isNotJoinCollect()
                      this.isCollection = true
                    } 
                  })
                }
              })
            }
          })
        }
      },
      // 立即购买
      buyNow () {
        var tempArray = [] // 临时数组
        var tempArrayOne = []
        var type = 0 // 判定货品订购数量不能大于库存，定制商品不需要考虑
        window.localStorage.removeItem('songGoodsShop') // 确认赠品之前先移除缓存赠品信息
        this.clickAgain = false
        if (this.customizedItems !== null) {
          this.customizedItems.forEach((item) => {
            item.userItems.forEach((obj) => {
              if (obj.count !== undefined && obj.count !== 0) {
                var limit = {'goodsId': obj.goodsId, 'itemId': obj.id, 'itemCount': obj.count, 'itemType': 3}
                var product = {'num': obj.count}
                var products = {'itemType': 3, 'count': obj.count, 'goodsName': obj.goodsName, 'barCode': obj.barCode, 'salePrice': obj.salePrice, 'retailPrice': obj.retailPrice, 'defaultPrice': obj.defaultPrice, 'goodsId': obj.goodsId, 'id': obj.id, 'itemCode': obj.itemCode, 'itemName': obj.itemName, 'specificationValueName': obj.specificationValueName, 'colorValueName': obj.colorValueName}
                this.limitPurchase.push(limit)
                tempArray.push(product)
                tempArrayOne.push(products)
              }
            })
          })
        }
        if (this.userId === null || this.userId === '') {
          this.clickAgain = true
          
        } else if (Number(this.capitalStatus) === 2 && Number(this.freezeStatus) === 2) {
          var secondsArea = 1 // 设置0.5秒
          this.intervalid = setInterval(() => {
            secondsArea--
            if (secondsArea === 0) {
              if (type === 0) {
                if (tempArray.length === 0) {
                  this.clickAgain = true
                  this.$message.warning(this.$t('P.GoodsItemNull'))
                  return false
                } else {
                  this.$api.post(this, 'user/u/order/restriction', {goods: this.limitPurchase}).then(res => { // 限购规则判断
                    if (res.code === 0) {
                      this.$api.get(this, 'user/u/distribution/list', {useRange: 2}).then(res => { // 判断是否有配送方式支持商品下单
                        if (res.code === 0) {
                          var songGoodsShop = []
                          this.clickAgain = true
                          this.distributeList = res.distributions
                          if (this.distributeList.length === 0) {
                            MessageBox.confirm('定制商品订单当前未配置配送方式，无法下单，请联系管理员', {
                              confirmButtonText: '确定',
                              cancelButtonText: '取消',
                              type: 'warning'
                            })
                            return false
                          }
                          window.localStorage.removeItem('shopCatShop')
                          window.localStorage.removeItem('spesShopCatShop')
                          window.localStorage.setItem('songGoodsShop', JSON.stringify(songGoodsShop)) // 赠品空数组
                          window.localStorage.setItem('goodsItems', JSON.stringify(tempArrayOne))
                          this.$router.push({name: 'ConsigneeInfor', query: {good_id: this.id, goodsType: this.goodsType, values: this.values}})
                        } else { this.clickAgain = true }
                      }).catch(() => { this.clickAgain = true })
                    } else if (res.code === 3) {
                     
                    } else {
                      this.clickAgain = true
                      this.limitPurchase = []
                    }
                  })
                }
              } else {
                this.$message.warning(this.$t('P.GoodsItemQuantityError'))
                return false
              }
              clearInterval(this.intervalid)
            }
          }, 500)
        } else if (Number(this.freezeStatus) === 1) {
          this.clickAgain = true
          this.$message.warning('账号已冻结，无法进行此操作，请联系商务。')
        } else if (Number(this.capitalStatus) === 1) {
          this.clickAgain = true
          this.$message.warning('账号仍在审核中，无法进行此操作，请等待审核通过。')
        } else if (Number(this.capitalStatus) === 3) {
          this.clickAgain = true
          this.$message.warning('账号审核未通过，无法进行此操作。请重新发起申请。')
        } else if (Number(this.capitalStatus) === 4) {
          this.clickAgain = true
          this.$message.warning('账号仍在审批中，无法进行此操作。请重新发起申请。')
        }
      },
      // 显示赠品
      lookSongGoods () {
        var myDate = Math.random()
        var tempArray = []
        var tempArrayTwo = []
        var tempArrayThr = []
        this.goodsItems.forEach((item) => {
          if (item.count && item.count > 0) {
            var product = {'itemType': 1, 'itemCount': item.count, 'goodsId': item.goodsId, 'itemId': item.id}
            tempArray.push(product)
          }
        })
        this.$api.post(this, 'user/u/marketing/promotion/getPresentItems?' + myDate, {goods: tempArray}).then(res => {
          if (res.code === 0) {
            this.presentCountMap = res.presentCountMap
            this.songGoodsList = res.goods
            for (var key in this.presentCountMap) { // 赠品最大数量
              this.presentCount = this.presentCountMap[key]
            }
            if (this.presentCount !== 0) {
              if (!this.songGoodsList) {
                return false
              }
              this.dialogVisible = true
              this.songGoodsList.forEach((items, index) => {
                this.$api.get(this, 'user/goods/items', {ids: items.itemId}).then(res => {
                  if (res.code === 0) {
                    tempArrayTwo = res.goodsList
                    if (!tempArrayTwo) {
                      return
                    }
                    tempArrayTwo.forEach(temp => {
                      var numInWarehouse = '' // 在库
                      var stockItemCount = '' // 在途
                      this.$api.get(this, 'user/u/warehouse/stock/distributor', {distributorId: this.userId, itemIds: items.itemId}).then(res => {
                        if (res.code === 0) {
                          if (res.stockItemCounts.length > 0) {
                            numInWarehouse = res.stockItemCounts[0].numInWarehouse + res.stockItemCounts[0].numVmi - res.stockItemCounts[0].numLock - res.stockItemCounts[0].numVmiLock - res.stockItemCounts[0].numOnWayLock - res.stockItemCounts[0].numReserved// 在库
                            stockItemCount = res.stockItemCounts[0].numInWarehouse + res.stockItemCounts[0].numVmi + res.stockItemCounts[0].numOnWay - res.stockItemCounts[0].numLock - res.stockItemCounts[0].numVmiLock - res.stockItemCounts[0].numOnWayLock - res.stockItemCounts[0].numReserved // 在库+在途
                            if (numInWarehouse <= 0) {
                              numInWarehouse = 0
                            }
                            if (stockItemCount <= 0) {
                              stockItemCount = 0
                            }
                          } else {
                            numInWarehouse = 0
                            stockItemCount = 0
                          }
                          temp.items.forEach((obj, index) => {
                            if (items.itemId === obj.id) {
                              Vue.set(obj, 'num', 0)
                              Vue.set(obj, 'goodsName', temp.goods.goodsName)
                              Vue.set(obj, 'imgUrl', ((this.$i18n.locale === 'zh' || !temp.goods.imageUrl1en == true)?temp.goods.imageUrl1:temp.goods.imageUrl1en))
                              Vue.set(obj, 'imgUrlen', temp.goods.imageUrl1en)
                              Vue.set(obj, 'itemId', items.itemId)
                              Vue.set(obj, 'goodsId', items.goodsId)
                              Vue.set(obj, 'numInWarehouse', numInWarehouse)
                              Vue.set(obj, 'stockItemCount', stockItemCount)
                              Vue.set(obj, 'presentCount', items.presentCount)
                              Vue.set(obj, 'ruleId', this.promotionRuleId)
                              tempArrayThr.push(obj)
                            }
                          })
                        }
                      })
                    })
                  }
                })
              })
              this.showSongGoodsList = tempArrayThr
            } else {
              this.$router.push({name: 'ConsigneeInfor', query: {good_id: this.id, values: this.values, goodsType: this.goodsType}})
            }
          } else {
            this.$router.push({name: 'ConsigneeInfor', query: {good_id: this.id, values: this.values, goodsType: this.goodsType}})
          }
        })
      },
      // 确认赠品
      confirmSongShop () {
        var tempArray = []
        if (this.presentCount < this.hasChoose) {
          this.$message({
            type: 'warning',
            message: '赠品数量已达上限!'
          })
          return false
        } else {
          if (this.hasChoose === 0) {
            MessageBox.confirm('您还未选择赠品，可以取消重新选择赠品', '确定不需要赠品', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              this.dialogVisible = false
              this.$router.push({name: 'ConsigneeInfor', query: {good_id: this.id, values: this.values, goodsType: this.goodsType}})
            }).catch(() => {});
          } else if (this.hasChoose > 0) {
            this.dialogVisible = false
            this.songGoods = 1
            this.showSongGoodsList.forEach((item) => {
              if (item.num > 0) {
                tempArray.push(item)
              }
            })
            window.localStorage.setItem('songGoodsShop', JSON.stringify(tempArray)) // 赠品
            this.$router.push({name: 'ConsigneeInfor', query: {good_id: this.id, values: this.values, goodsType: this.goodsType, songGoods: this.songGoods}})
          }
        }
      },
      // diy定制商品数据
      diyShopData () {
        this.showLoading = true
        this.$api.post(this, 'user/goodsnewdiy/getDiyGoodsInfo', {goodsCode: this.goodsCode}).then(res => {
          if (res.code === 0) {
            this.showLoading = false
            this.stockShowFlag = res.stockShowFlag
            this.stockShowNumber = res.stockShowNumber
            this.goodsDetail = res.goods
            // 定制列表
            // this.customizedItems = res.customizedItems
            this.shopImg = ((this.$i18n.locale === 'zh' || !res.goods.imageUrl1en === true) ? res.goods.imageUrl1 : res.goods.imageUrl1en)
            this.diyItems = res.diyItems
            if (res.diyItems) {
              this.diyDialogVisible = true
              Vue.set(this.diyItems, 'count', 0)
              Vue.set(this.diyItems, 'itemType', 3)
              Vue.set(this.diyItems, 'newdiyList', [])
              Vue.set(this.diyItems, 'brandId', res.goods.brandId)
              Vue.set(this.diyItems, 'diyPic', this.diyPic)
              Vue.set(this.diyItems, 'specValueName', this.info.specName)
              Vue.set(this.diyItems, 'specValueNameEn', this.info.specNameEn)
              Vue.set(this.diyItems, 'materialValueName', this.info.materialsName)
              Vue.set(this.diyItems, 'materialValueNameEn', this.info.materialsNameEn)
              Vue.set(this.diyItems, 'manufactor', this.info.manufactor)
            }
            this.gradeDiscountsRule = res.gradeDiscountsRule
            this.pickUpRateRule = res.pickUpRateRule
            if (res.promotionRules) {
              this.promotion = res.promotion
              this.promotionRules = res.promotionRules
              this.promotionRules.forEach(item => {
                if (item.reduceOrPresent === 2) { // 满赠
                  this.promotionRuleId = item.id
                } else if (item.reduceOrPresent === 3) { // 特价
                  this.reduceOrPresent = item.reduceOrPresent
                }
              })
            }
          }
        }).catch(err => {
          console.log(err)
        })
      },
      diyBuy () {
        let tempArrayOne = []
        let songGoodsShop = []
        this.diyItems.count = this.diyNum
        this.diyItems.num = this.diyNum
        this.diyItems.orderType = 4
        this.diyItems.itemId = this.diyItems.id
        let product = {'diyNum': this.diyItems.count, 'diyPic': this.diyPic, 'diyPdf': this.diyPdf, 'manufactor': this.info.manufactor, 'materialId': this.info.materialsId, 'materialName': this.info.materialsName, 'modelId': this.info.specId, 'modelName': this.info.specName, 'pictureId': this.info.pictureId, 'brandId': this.info.brandId, 'brandName': this.info.brandName}
        this.diyItems.newdiyList.push(product)
        var limit = {'goodsId': this.diyItems.goodsId, 'itemId': this.diyItems.id, 'itemCount': this.diyItems.count, 'itemType': 4}
        this.limitPurchase.push(limit)
        tempArrayOne.push(this.diyItems)
        this.$api.post(this, 'user/u/order/restriction', {goods: this.limitPurchase}).then(res => { // 限购规则判断
          if (res.code === 0) {
            window.localStorage.setItem('songGoodsShop', JSON.stringify(songGoodsShop)) // 赠品空数组
            window.localStorage.setItem('goodsItems', JSON.stringify(tempArrayOne))
            this.$router.push({name: 'ConsigneeInfor', query: {good_id: this.diyItems.goodsId, goodsType: 7, orderType: 4, values: this.values}})
          }
        })
      },
      diyJoinCat () {
        let diyItemList = []
        let product = {'diyNum': this.diyNum, 'diyPic': this.diyPic, 'diyPdf': this.diyPdf, 'manufactor': this.info.manufactor, 'materialId': this.info.materialsId, 'materialName': this.info.materialsName, 'modelId': this.info.specId, 'modelName': this.info.specName, 'pictureId': this.info.pictureId, 'brandId': this.info.brandId, 'brandName': this.info.brandName}
        diyItemList.push(product)
        this.loadsing = true
        this.$api.post(this, 'user/u/shoppingCart', {goodsId: this.diyItems.goodsId, itemId: this.diyItems.id, num: this.diyNum, itemType: 4, orderType: 4, newDiyList: diyItemList}).then(res => {
          if (res.code === 0) {
            this.diyDialogVisible = false
            if (this.$i18n.locale === 'zh') {
              this.$message.success('加入购物车成功')
            } else {
              this.$message.success('Add to shopping cart successfully.')
            }
          } else if (res.code === 3) {
            this.loadsing = false
            
          }
        }).catch(() => {
          this.loadsing = false
        })
      }
    },
    created () {
      var one = window.localStorage.getItem('userId')
      var state = window.localStorage.getItem('capitalStatus')
      var fstate = window.localStorage.getItem('freezeStatus')
      this.userId = one
      this.capitalStatus = state
      this.freezeStatus = fstate
      this.goodsCode = this.$route.query.code
      this.id = this.$route.query.goodsId
      this.diyPic = this.$route.query.diyPic
      this.diyPdf = this.$route.query.diyPdf
      this.source = this.$route.query.source
      this.info = JSON.parse(this.$route.query.info)
      if (Number(this.source) === 0) {
        this.diyShopData()
      } else {
        this.$router.push({path: '/customize', query: {goodsId: this.id}})
      }
    },
    mounted () {
    }
  }
</script>
<style lang='less'>
  .img-box-p{
    img{
      max-width: 1190px;
      display: block;
      margin: 0 auto;
    }
  }
  .el-dialog__wrapper{
    .el-dialog{
      width: 600px;
      .el-dialog__header{
        text-align: center;
      }
      .el-dialog__body{
        padding: 0;
        padding-bottom: 10px;
      }
      .el-dialog__footer{
        text-align: center;
      }
    }
  }
</style>
<style scoped="scoped" lang='less'>
  .user{width: 100%;}
  .dingzhi-btn{
    margin: 0 auto;
    width: 50px;
    height: 30px;
    line-height: 30px;
    border: 1px solid #ccc;
    border-radius: 5px;
    font-size: 12px;
    cursor: pointer;
  }
  .diy-css{
    padding-left: 20px;
    .left-img{
      margin-right: 20px;
      width: 168px;
      height: 168px;
      .diy-images{
        display: flex;
        align-items: center;
        justify-content: center;
        img{
          height: 168px;
        }
      }
    }
    .right-cons{
      .item{
        width: 350px;
        margin-bottom:10px;
      }
      .item-name{
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
      }
    }
  }
  .overflow-y{ overflow-y: scroll;}
  .nav-bottom{
    width: 1190px;
    .bleed{
      padding-left: 55px;
      .el-breadcrumb{font-size: 12px}
    }
  }
  .mainOne{
    width: 1190px;
    .product-all{
      .pic-box{
        margin-right: 35px;
        width: 360px;
        .pic-move{
          width: 360px;
          height: 360px;
        }
        .pic-silde{
          width: 360px;
          .silde-img{
            .item{
              margin-right: 9px;
              width: 52px;
              cursor: pointer;
              &:last-child{margin-right: 0}
              img{height: 52px}
            }
          }
          .turn-left{
            margin-top: 15px;
            width: 16px;
            height: 26px;
            vertical-align: 5px;
            background: url("../../src/assets/images/turn-left.png") no-repeat center center;
            cursor: pointer;
          }
          .turn-right{
            margin-top: 15px;
            width: 16px;
            height: 26px;
            background: url("../../src/assets/images/turn-right.png") no-repeat center center;
            cursor: pointer;
          }
        }
      }
      .text-box{
        width:calc(100% - 395px);
        padding-right:18px;
        box-sizing: border-box;
        .head{
          .ding-zhi{
            position: relative;
            padding-right: 8px;
            width: 60px;
            height: 28px;
            background:rgba(0,201,220,1);
            border-radius:5px;
            .img{
              position: absolute;
              top: 6px;
              left: 9px;
              display: inline-block;
              width: 16px;
              height: 16px;
              background: url("../../src/assets/images/ding-zhi.png") no-repeat center center;
              background-size: 100%;
            }
            .spc{
              display: inline-block;
              float: right;
              height: 28px;
              line-height: 28px;
              font-size:15px;
              font-family:FZLTHJW--GB1-0;
              font-weight:400;
              color:rgba(255,255,255,1)
            }
          }
          .goodsName{
            padding-left: 18px;
            max-width: 716px;
            font-size: 20px;
          }
          .collection{
            align-items: center;
            justify-content: center;
            flex: 1;
            display: flex;
            font-size:16px;
            font-family:FZHei-B01;
            font-weight:400;
            color:rgba(0,0,0,1);
            cursor: pointer;
          }
        }
        .introduce{
          font-size:12px;
          max-width: 650px;
          font-family:FZHei-B01;
          font-weight:400;
          padding-left: 18px;
          color:rgba(239,12,35,1);
        }
        .content{
          margin-top:25px;
          min-height: 302px;
          .item{
            padding-top: 8px;
            padding-bottom: 8px;
            padding-left: 18px;
            line-height: 25px;
            div{
              &:first-child{
                margin-right: 20px;
                width: 65px
              }
            }
            .activity{
              width: 530px;
              div{
                width: 100%;
                .label{
                  padding-left: 5px;
                  padding-right: 5px;
                  display: inline-block;
                  min-width: 50px;
                  height: 20px;
                  line-height: 20px;
                  border-radius: 5px;
                  background-color: #ff7900;
                  font-size: 12px;
                  text-align: center;
                  color: #fff;
                }
              }
              .more-label{
                .icon{
                  margin-right: 5px;
                  display: inline-block;
                  width: 12px;
                  height: 10px;
                  background: url("../../src/assets/images/turn-downs.png") no-repeat center center;
                  background-size: 12px;
                }
                .rotate{transform:rotate(180deg);}
              }
            }
          }
          .spe{
            padding-top: 13px;
            padding-bottom: 13px;
            background-color: #fff8f2;
          }
          .customize{
            margin:10px 0 0 18px;
            .button{
              display: inline-block;
              margin-top: 20px;
              padding: 0 40px;
              height: 35px;
              line-height: 35px;
              text-align: center;
              color: #fff;
              background: #00c9dc;
              cursor: pointer;
            }
          }
          .operate{
            position: absolute;
            left:0;
            bottom:10px;
            padding-left: 18px;
            div{
              height: 35px;
              line-height: 35px;
            }
            .collect1{
              padding-left: 62px;
              width: 74px;
              .img1{
                left: 35px;
              }
            }
            .collect2{
              padding-left: 52px;
              width: 84px;
              .img2{
                left: 25px;
              }
            }
            .collect{
              position: relative;
              .img{
                position: absolute;
                top: 8px;
                display: inline-block;
                width: 18px;
                height: 17px;
                background: url("../../src/assets/images/star.png") no-repeat center center;
              }
              .word{
                float: left;
                height: 35px;
                line-height: 35px;
              }
            }
            .share{
              span{
                display: inline-block;
                width: 18px;
                height: 17px;
                background: url("../../src/assets/images/share.png") no-repeat center center;
              }
            }
          }
        }
      }
    }
  }
  .mainTwo{
    width: 1190px;
    padding-bottom: 40px;
    .shop-table{
      .zaitu{
        position: relative;
        width: 120px;
        // top:-33px;
        left:65%;
        height: 45px;
        line-height: 45px;
        span{
          display: block;
          width: 16px;
          height: 45px;
          background: url("../assets/images/choose.png") no-repeat center center;
        }
        span.gou{
          background: url("../assets/images/gou.png") no-repeat center center;
        }
      }
      width: 100%;
      table{
        table-layout: fixed;
        word-wrap: break-word;
        word-break: break-all;
        border-collapse: collapse;
        width: 100%;
        tr{
          th,td.spe{
            span{
              display: block;
              width: 30px;
              height: 45px;
              background: url("../../src/assets/images/choose.png") no-repeat center center;
            }
          }
          th{
            height: 45px;
            line-height: 45px;
            text-align: center;
            background-color: #00c9dc;
            font-size: 13px;
          }
          td.td-h1{
            white-space: normal;
            border-left: 0;
            .activity-label{
              width: 22px;
              height: 50px;
              line-height: 25px;
              border-radius: 5px;
              word-wrap: break-word;
            }
          }
          td{
            height: 60px;
            max-height: 60px;
            text-align: center;
            overflow: hidden; white-space: nowrap;text-overflow: ellipsis;
            color:#000;
            font-size: 12px;
            button{
              padding: 0;
              border:0;
              background-color: #f5f5f5;
              font-size: 12px;
              color:#000;
            }
            .register{
              width: 55px;
              color: #ff7900;
              border-bottom: 1px solid #ff7900;;
            }
          }
        }
      }
    }
    .buy-list{
      border-left:1px solid #ccc;
      border-right:1px solid #ccc;
      background-color: #ccf4f8;
    }
    .buy-now{
      border:1px solid #ccc;
      .buy{
        width:240px;
        div{
          width: 112px;
          height: 35px;
          line-height: 35px;
        }
        .buy-btn{
          background-color: #ff7900;
        }
        .join-btn{
          background-color: #00c9dc;
        }
      }
    }
    .shop-descripe{
      .head{
        height: 48px;
        line-height: 48px;
        margin-bottom: 20px;
      }
    }
  }
  .withoutGoods{
    width: 1190px;
  }
  /*弹框*/
  .cover{
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: #000;
    z-index: 99;
    opacity: 0.6;
  }
  .pro-cover{
    width: 555px;
    height: 400px;
    border: 1px solid #ccc;
    border-radius: 5px;
    z-index: 99;
    background: #fefefe;
    .item{
      padding-left: 50px;
      .left{
        line-height: 25px;
      }
      .common-text{
        width: 350px;
        height: 80px;
        padding-left: 10px;
      }
      .goodsName{width: 400px}
    }
    .button{
      margin: 0 auto;
      width: 100px;
      span{
        display: block;
        width: 100px;
        height: 35px;
        line-height: 35px;
        text-align: center;
        border: 1px solid #ccc;
        background-color: #00c9dc;
        color: #fff;
        border-radius: 5px;
      }
    }
    .button-presell{
      padding-left: 120px;
      width: 100px;
      span{
        display: block;
        width: 100px;
        height: 35px;
        line-height: 35px;
        text-align: center;
        border: 1px solid #ccc;
        background-color: #00c9dc;
        color: #fff;
        border-radius: 5px;
      }
    }
  }
  .cover-box{
    box-sizing: border-box;
    position: fixed;
    top: 0;
    left: 0;
    bottom: 0;
    right: 0;
    margin: auto;
    z-index: 99;
    .shut{
      position: absolute;
      top:-8px;
      right:-8px;
      display: block;
      width: 18px;
      height: 18px;
      background: url("../../src/assets/images/shut.png") no-repeat center center;
    }
  }
  .buy-sum{
    width: 92px;
    height: 22px;
    line-height: 22px;
    border: 1px solid #EBEFF5;
    div{
      height: 22px;
      box-sizing: border-box;
      background-color: #fff;
      input {
        width: 62px;
      }
    }
    .buyac{
      width: 22px;
      font-size: 22px;
      color: #9B9B9B;
      cursor: pointer;
      text-align: center;
    }
    .buyb{
      width: 48px;
      line-height: 22px;
      color: #3A3A3A;
      border-left: 1px solid #EBEFF5;
      border-right: 1px solid #EBEFF5;
      input{
        width: 46px;
      }
    }
  }
  /*分组*/
  .mainTwo .shop-table table tr td.fen-group{
    height: 50px;
    background-color: #e7e7e7;
    .btn-prev{
      width: 20px;
      height: 50px;
      float: left;
      cursor: pointer;
      background: url("../../src/assets/images/btn-prev.jpg") center center no-repeat;
    }
    .btn-next{
      width: 20px;
      height: 50px;
      float: left;
      cursor: pointer;
      background: url("../../src/assets/images/btn-next.jpg") center center no-repeat;
    }
    .fen-group-ul{
      width: 1148px;
      overflow: hidden;
      height: 50px;
      ul{
        width: 10000px;
        position: absolute;
        top: 0;
        left: 0;
        height: 50px;
        overflow: hidden;
        li{
          display: inline-block;
          width: 143.5px;
          height: 50px;
          line-height: 50px;
          float: left;
          cursor: pointer;
          overflow: hidden;
          button{
            padding: 0;
            border:0;
            background-color: #e7e7e7;
            font-size: 15px;
            color:#000;
          }
        }
        li.current{
          background-color: #ccf4f8;
          button{
            padding: 0;
            border:0;
            background-color: #ccf4f8;
            font-size: 15px;
            color:#000;
          }
        }
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
  /*弹框图片放大*/
  .pro-cover-img{
    position: fixed;
    top: 0;
    left: 0;
    bottom: 0;
    right: 0;
    margin: auto;
    width: 500px;
    height: 500px;
    z-index: 99;
    img{height: 500px;}
    .shut{
      position: absolute;
      top:-8px;
      right:-8px;
      display: block;
      width: 18px;
      height: 18px;
      background: url("../../src/assets/images/shut.png") no-repeat center center;
    }
  }
  /*选择赠品*/
  .max-height300{max-height: 300px;}
  .shop-table{
    margin-bottom: 20px;
    table{
      tr{
        td{
          height: 45px;
          text-align: center;
          border-bottom: 1px solid #ccc;
          .buy-sum{
            div{
              height: 25px;
              box-sizing: border-box;
              border:1px solid #b6b6b6;
            }
            .buyac{
              width: 25px;
              line-height: 20px;
              font-size: 24px;
              background-color: #eeeeee;
              cursor: pointer;
              text-align: center;
            }
            .buyb{
              width: 40px;
              line-height: 25px;
              margin-left: -1px;
              margin-right: -1px;
              input{
                width: 40px;
              }
            }
          }
          .songImg{
            width: 50px;
            img{
              margin-top: 5px;
              height: 50px;
            }
          }
        }
      }
    }
  }
  /*找不到商品*/
  .empty-car{
    padding-bottom: 180px;
    .empty-car-img{
      margin-top: 120px;
      width: 153px;
    }
  }
  /*预售*/
  .blank-span{
    position: absolute;
    display: block;
    width: 100%;
    height: 30px;
    background-color: #fff;
    z-index: 1;
    opacity: 0;
  }
</style>
