<template>
  <div>
    <div class="index rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState" :activitys="activitys"></Header>
      <!--主内容-->
      <div class="main rl-padding-top-default rl-padding-horizontal-lllg rl-margin-zero">
        <div class="rl-clear rl-margin-bottom-xxxs">
          <div class="breed rl-padding-left-mid rl-text-xxs rl-margin-bottom-default rl-fl">
            <el-breadcrumb separator-class="el-icon-arrow-right">
              <el-breadcrumb-item>全部结果</el-breadcrumb-item>
              <el-breadcrumb-item>"{{activityName}}"</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <!--分页-->
          <div class="sort-right rl-fr" v-if="userId !== ''&&  userId!== null &&
            freezeStatus !== undefined && freezeStatus !== null && freezeStatus === '2' &&
            capitalStatus !== undefined && capitalStatus !== null && capitalStatus === '2'">
            <div @click="toShowImg" class="rl-fl rl-bg-gray-mm rl-tc rl-text-xxs cursor-pointer" :class ="{'current':tabs === 'showImg' }">图片展示</div>
            <div @click="toFastOrder" class="rl-fl rl-bg-gray-mm rl-tc rl-text-xxs cursor-pointer" :class ="{'current':tabs === 'fastOrder' }">快速订货</div>
          </div>
        </div>
        <div class="scrolls">
          <div class="btn-prev rl-fl" @click="btnPrev"></div>
          <div class="activity rl-margin-bottom-default rl-relative rl-fl" ref="foodList">
            <ul id="box">
              <li @click="chooseActivity(item.id,index)" :class="{'current':chooseIndex === index}" id="boxLi" class="cursor-pointer rl-bg-white rl-padding-left-default rl-padding-right-default rl-padding-top-xxxs rl-padding-bottom-xxxs rl-margin-right-xxxs rl-margin-bottom-xxxs" v-for="(item,index) in activityList" :key="item.id">
                <div class="name rl-tl rl-margin-bottom-default rl-text-gray">{{item.name}}</div>
                <div class="time rl-tr rl-text-gray rl-text-xxs">{{item.startTime | formatDate}}-{{item.endTime | formatDate}}</div>
              </li>
            </ul>
          </div>
          <div class="btn-next rl-fl" @click="btnNext"></div>
        </div>
        <!--<div class="sort rl-clear">
          <div class="sort-left rl-fl">
            <div class="rl-text-xxs rl-bg-gray-mm syn rl-text-blue-mm rl-tc rl-fl">综合排序</div>
            <div class="rl-bg-white rl-fl">
              <div @click="orderBy(1)" class="rl-text-xxs rl-tc rl-text-gray rl-bd-gray-sm time cursor-pointer"><span>上架时间</span><span class="icon" :class="upDownOne === true ? 'rotate': ''"></span></div>
            </div>
            <div @click="orderBy(3)" class="rl-bg-white rl-fl">
              <div class="rl-text-xxs rl-tc rl-text-gray rl-bd-gray-sm common cursor-pointer"><span>价格</span><span class="icon" :class="upDownTwo === true ? 'rotate': ''"></span></div>
            </div>
            <div class="rl-bg-white rl-fl">
              <div @click="orderBy(2)"  class="rl-text-xxs rl-tc rl-text-gray rl-bd-gray-sm common cursor-pointer"><span>销量</span><span class="icon" :class="upDownThr === true ? 'rotate': ''"></span></div>
            </div>
          </div>
        </div>-->
        <!--图片展示-->
        <div v-show="tabs=='showImg'">
          <div class="classify-list">
            <ul class="rl-clear">
              <li v-for="item in activityGoodsList" :key="item.id" @click="goDoodsDetail(item.goodsId,item.goodsType)">
                <div class="content cursor-pointer">
                  <div class="img"><img width="100%" :src="item.goodsImage + '?x-oss-process=image/resize,h_342,l_342'" alt=""></div>
                  <div class="word rl-text-gray rl-margin-top-mid rl-tl">
                    {{item.goodsName}}
                  </div>
                  <div class="rl-tl rl-padding-left-xxxs rl-margin-top-xxxxs rl-text-blue-xs">
                    <span>￥</span>
                    <span class="rl-text-sm" v-if="item.specialPrice !== null">{{item.specialPrice}}</span>
                    <span class="rl-text-sm" v-else>{{item.salePrice}}</span>
                  </div>
                  <div class="lable rl-padding-left-xxxs" v-if="item.label">
                    <!--<span v-if="item.reduceOrPresent === 1">满减</span>
                    <span v-if="item.reduceOrPresent === 2">满赠</span>-->
                    <span v-for="label in item.labelList" :key="label.id">{{label}}</span>
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
        </div>
        <!--快速订货-->
        <div v-show="tabs=='fastOrder'">
          <div class="rl-clear rl-margin-bottom-default" v-for="goods in fastOrderGoodsList" :key="goods.id">
            <div class="fast-img rl-bg-white rl-fl cursor-pointer"  @click="goDoodsDetail(goods.goodsId,goods.goodsType)"><img width="100%" :src="goods.goodsImage" alt=""></div>
            <div class="fast-table rl-bg-white rl-fl">
              <div class="goodsName rl-bg-blue-mm rl-text-white rl-clear">
                <div class="rl-fl rl-text-xxs">{{goods.goodsName}}</div>
                <div @click="changeTu(goods,goods.tuType)" class="rl-fr rl-text-xxs zaitu rl-clear cursor-pointer"><span class="rl-fl" :class ="{'gou':goods.tuType === true}"></span>包含在途库存</div>
              </div>
              <table>
                <tr>
                  <th>货号</th>
                  <th>条形码</th>
                  <th>规格</th>
                  <th>颜色</th>
                  <th v-if="!userId == '' && goods.showRetailPrice === true">建议零售价</th>
                  <th>会员价</th>
                  <th v-if="goods.reduceOrPresent === 3">折扣价</th>
                  <th width="13%">订购数量</th>
                  <th>库存</th>
                  <th>尺寸</th>
                  <th>装箱数</th>
                  <th>重量(g)</th>
                </tr>
                <tr  v-for="item in goods.goodsItem" :key="item.id">
                  <td>{{item.itemCode}}</td>
                  <td>{{item.barCode}}</td>
                  <td>
                    <el-tooltip class="item" effect="dark" :content="item.specificationValueName" placement="bottom">
                      <el-button>{{item.specificationValueName}}</el-button>
                    </el-tooltip>
                  </td>
                  <td>
                    <el-tooltip class="item" effect="dark" :content="item.colorValueName" placement="bottom">
                      <el-button>{{item.colorValueName}}</el-button>
                    </el-tooltip>
                  </td>
                  <td v-if="!userId == '' && goods.showRetailPrice === true"><span v-if="item.retailPrice !== null && Number(item.retailPrice) !== 0">￥{{item.retailPrice}}</span><span v-else>--</span></td>
                  <td v-else-if="userId === '' && goods.showRetailPrice === true" class="rl-text-xxss">登录后查看</td>
                  <td v-if="!userId == ''"><span v-if="Number(item.salePrice) !== 0 && item.salePrice !== null">￥{{item.salePrice}}</span><span v-else>暂未定价</span></td>
                  <td v-else class="rl-text-xxss">登录后查看</td>
                  <td v-if="goods.reduceOrPresent === 3 && item.specialPrice !== null">￥{{item.specialPrice}}</td>
                  <td v-if="goods.reduceOrPresent === 3 && item.specialPrice === null">--</td>
                  <td v-if="!userId == ''">
                    <div class="rl-margin-left-xxxs">
                      <BuySum ref="BuySum" :item="item" :tuType="goods.tuType" @itemInput="handleInput" @visible="getVisible"></BuySum>
                    </div>
                  </td>
                  <td v-else class="rl-text-xxss">登录后查看</td>
                  <td class="rl-relative" v-if="!userId == '' && goods.tuType === false">
                    <span class="blank-span"></span>
                    <el-popover
                      placement="bottom"
                      width="150"
                      v-model="item.visible"
                      @show="showPop(item)"
                      @hide="hidePop(item)"
                      trigger="click">
                      <p class="rl-margin-bottom-xxs rl-tc rl-text-bold">库存不足</p>
                      <p class="rl-margin-bottom-xxxxs rl-tc rl-text-xxs">您可以进行<span class="cursor-pointer rl-margin-left-ls rl-text-blue-xs rl-bdb-blue-xs" @click="wantBook(item,item.type)">缺货登记</span></p>
                      <p class="rl-tc rl-text-xxs" v-if="item.advanceSaleFlag === 1">或选择<span class="cursor-pointer rl-margin-left-ls rl-text-blue-xs rl-bdb-blue-xs" @click="forwardSale(item,item.presell)">预售</span></p>
                      <div class="rl-text-xxs" slot="reference" v-if="stockShowFlag === 0">{{item.numInWarehouse}}</div>
                      <div class="rl-text-xxs" slot="reference" v-else-if="stockShowFlag === 1 && (stockShowNumber < item.numInWarehouse)">{{item.numInWarehouse}}</div>
                      <div class="rl-text-xxs" slot="reference" v-else-if="stockShowFlag === 1 && item.numInWarehouse === 0">无货</div>
                      <div class="rl-text-xxs" slot="reference" v-else-if="stockShowFlag === 1 && (0 < item.numInWarehouse <= stockShowNumber)">库存紧张</div>
                    </el-popover>
                  </td>
                  <td class="rl-relative" v-else-if="!userId == '' && goods.tuType === true">
                    <span class="blank-span"></span>
                    <el-popover
                      placement="bottom"
                      width="150"
                      v-model="item.visible"
                      @show="showPop(item)"
                      @hide="hidePop(item)"
                      trigger="click">
                      <p class="rl-margin-bottom-xxs rl-tc rl-text-bold">库存不足</p>
                      <p class="rl-margin-bottom-xxxxs rl-tc rl-text-xxs">您可以进行<span class="cursor-pointer rl-margin-left-ls rl-text-blue-xs rl-bdb-blue-xs" @click="wantBook(item,item.type)">缺货登记</span></p>
                      <p class="rl-tc rl-text-xxs" v-if="item.advanceSaleFlag === 1">或选择<span class="cursor-pointer rl-margin-left-ls rl-text-blue-xs rl-bdb-blue-xs" @click="forwardSale(item,item.presell)">预售</span></p>
                      <div class="rl-text-xxs" slot="reference" v-if="stockShowFlag === 0">{{item.stockItemCount}}</div>
                      <div class="rl-text-xxs" slot="reference" v-else-if="stockShowFlag === 1 && (stockShowNumber < item.stockItemCount)">{{item.stockItemCount}}</div>
                      <div class="rl-text-xxs" slot="reference" v-else-if="stockShowFlag === 1 && item.stockItemCount === 0">无货</div>
                      <div class="rl-text-xxs" slot="reference" v-else-if="stockShowFlag === 1 && (0 < item.stockItemCount <= stockShowNumber)">库存紧张</div>
                    </el-popover>
                  </td>
                  <td v-else class="rl-text-xxss">登录后查看</td>
                  <!--<td v-if="(item.numInWarehouse == 0 || item.invent === 1) && !userId == '' && goods.tuType === false" @click="wantBook(item,item.type)"><div class="register rl-tc cursor-pointer rl-margin-zero">缺货登记</div></td>-->
                  <!--<td v-else-if="(item.stockItemCount == 0 || item.invent === 0) && !userId == '' && goods.tuType === true" @click="wantBook(item,item.type)"><div class="register rl-tc cursor-pointer rl-margin-zero">缺货登记</div></td>-->
                  <!--<td v-else-if="!item.numInWarehouse == 0 && !userId == ''&& goods.tuType === false">库存充足</td>-->
                  <!--<td v-else-if="!item.stockItemCount == 0 && !userId == '' && goods.tuType === true">库存充足</td>-->
                  <!--<td v-else class="rl-text-xxss">登录后查看</td>-->
                  <td v-if="item.sizeStr !== null && item.unit !== null">
                    <el-tooltip class="item" effect="dark" :content="item.sizeStr + item.unit" placement="bottom">
                      <el-button>{{item.sizeStr}}({{item.unit}})</el-button>
                    </el-tooltip>
                  </td>
                  <td v-else></td>
                  <td>{{item.boxNum}}</td>
                  <td class="rl-bdr-gray-sm">{{item.weight}}</td>
                  <!--缺货登记弹框-->
                  <div class="cover" v-if="item.type === 1"></div>
                  <div class="pro-cover cover-box rl-padding-bottom-lllg rl-padding-top-default rl-relative" v-if="item.type === 1">
                    <div class="rl-padding-bottom-default rl-tc rl-text-mid">缺货登记</div>
                    <span @click="shutLog(item,item.type)" class="shut cursor-pointer"></span>
                    <div class="item rl-text-gray rl-clear rl-margin-bottom-xxxs">
                      <div class="rl-fl rl-margin-right-double">货 号</div>
                      <div class="rl-fl">{{item.itemCode}}</div>
                    </div>
                    <div class="item rl-text-gray rl-clear rl-margin-bottom-xxxs">
                      <div class="rl-fl rl-margin-right-double">规 格</div>
                      <div class="rl-fl">{{item.specificationValueName}}</div>
                    </div>
                    <div class="item rl-text-gray rl-clear rl-margin-bottom-xxxs">
                      <div class="rl-fl rl-margin-right-double">颜 色</div>
                      <div class="rl-fl">{{item.colorValueName}}</div>
                    </div>
                    <div class="item rl-text-gray rl-clear rl-margin-bottom-xxxs">
                      <div class="rl-fl rl-margin-right-default">商品编号</div>
                      <div class="rl-fl">{{goods.goods.goodsNo}}</div>
                    </div>
                    <div class="item rl-text-gray rl-clear rl-margin-bottom-xxxs">
                      <div class="rl-fl rl-margin-right-default">商品名称</div>
                      <div class="rl-fl">{{goods.goods.goodsName}}</div>
                    </div>
                    <div class="item rl-text-gray rl-clear rl-margin-bottom-xxxs">
                      <div class="rl-fl rl-margin-right-default left">缺货数量</div>
                      <div class="rl-fl">
                        <div class="buy-sum rl-clear">
                          <div @click.stop.prevent="handleReduce()" class="rl-fl buyac buya rl-text-gray">-</div>
                          <div class="rl-fl buyb"><input v-model="stockoutCount" class="rl-tc" type="text" maxlength="8" onkeyup="this.value=this.value.replace(/\D/g,'')"></div>
                          <div @click.stop.prevent="handleAdd()" class="rl-fl buyac buyc rl-text-gray">+</div>
                        </div>
                      </div>
                    </div>
                    <div class="item rl-text-gray rl-clear">
                      <div class="rl-fl rl-margin-right-default">缺货备注</div>
                      <div class="rl-fl">
                        <textarea v-model="item.remark" name="" id="" cols="30" rows="10" class="common-text"></textarea>
                      </div>
                    </div>
                    <div v-if="onceAgain === true" class="button"><span @click="stockout(item,goods.goods.id)" class="rl-margin-top-default cursor-pointer">提交</span></div>
                    <div v-else class="button"><span class="rl-margin-top-default">提交</span></div>
                  </div>
                  <!--预售弹框-->
                  <div class="cover" v-if="item.presell === 1"></div>
                  <div style="height:260px" class="pro-cover cover-box rl-padding-bottom-lllg rl-padding-top-default rl-relative" v-if="item.presell === 1">
                    <div class="rl-padding-bottom-default rl-tc rl-text-mid">预售</div>
                    <span @click="shutLog(item,item.presell)" class="shut cursor-pointer"></span>
                    <div class="item rl-text-gray rl-clear rl-margin-bottom-xxxs">
                      <div class="rl-fl rl-margin-right-double">货 号</div>
                      <div class="rl-fl">{{item.itemCode}}</div>
                    </div>
                    <div class="item rl-text-gray rl-clear rl-margin-bottom-xxxs">
                      <div class="rl-fl rl-margin-right-double">规 格</div>
                      <div class="rl-fl">{{item.specificationValueName}}</div>
                    </div>
                    <div class="item rl-text-gray rl-clear rl-margin-bottom-xxxs">
                      <div class="rl-fl rl-margin-right-double">颜 色</div>
                      <div class="rl-fl">{{item.colorValueName}}</div>
                    </div>
                    <div class="item rl-text-gray rl-clear rl-margin-bottom-xxxs">
                      <div class="rl-fl rl-margin-right-default left">订购数量</div>
                      <div class="rl-fl" v-if="item.moq >= 0">
                        <presellNum ref="presellNum" :item="item"></presellNum>
                      </div>
                      <div v-else>{{item.moq}}</div>
                    </div>
                    <div v-if="onceAgain === true && item.moq >= 0" class="button-presell rl-fl"><span @click="buyPresell(item)" class="rl-margin-top-default cursor-pointer">立即购买</span></div>
                    <div v-else class="button-presell rl-fl"><span class="rl-margin-top-default">立即购买</span></div>
                    <div v-if="onceAgain === true && item.moq >= 0" class="button-presell rl-fl"><span @click="joincarPresell(item)" class="rl-margin-top-default cursor-pointer">加入购物车</span></div>
                    <div v-else class="button-presell rl-fl"><span class="rl-margin-top-default">加入购物车</span></div>
                  </div>
                </tr>
              </table>
            </div>
          </div>
          <div class="rl-tr rl-margin-bottom-default rl-clear" v-if="this.fastOrderGoodsList.length !== 0">
            <div v-if="clickAgain === true" @click="joinShopCar">
              <div v-if="scrollBottom === false" class="rl-fr join-shopcars rl-text-white rl-bg-blue-xs rl-tc rl-text-mid cursor-pointer rl-margin-left-double" :class="{'scroll-bottom':scrollBottom === true }">加入购物车</div>
              <div v-else class="rl-fr join-shopcars-img rl-text-white rl-tc rl-text-mid cursor-pointer rl-margin-left-double" :class="{'scroll-bottom':scrollBottom === true }">
                <img width="100%" src="../assets/images/join-carts.png">
              </div>
            </div>
            <div v-else>
              <div v-if="scrollBottom === false" class="rl-fr join-shopcars rl-text-white rl-bg-blue-xs rl-tc rl-text-mid cursor-pointer rl-margin-left-double" :class="{'scroll-bottom':scrollBottom === true }">加入购物车</div>
              <div v-else class="rl-fr join-shopcars-img rl-text-white rl-tc rl-text-mid cursor-pointer rl-margin-left-double" :class="{'scroll-bottom':scrollBottom === true }">
                <img width="100%" src="../assets/images/join-carts.png">
              </div>
            </div>
            <div class="rl-fr"><el-pagination
              background
              @current-change ="fastHandleCurrentChange"
              @size-change="fastHandleSizeChange"
              layout="prev, pager, next, jumper"
              :page-size="fastPagesize"
              :total="fastTotalCount"
            >
            </el-pagination></div>
          </div>
        </div>
        <!--商品为空-->
        <div class="empty-car rl-margin-zero" v-if="(this.activityGoodsList === null || this.activityGoodsList === undefined || this.activityGoodsList.length === 0) && this.showLoading === false">
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
import { formatDate,  } from '@/assets/js/common.js'
import BuySum from '@/components/BuySum.vue'
import presellNum from '@/components/presellNum.vue'
import Vue from 'vue'
import loading from '@/components/loading.vue'
export default {
  name: 'Activity',
  components: {Footer, Header, loading, BuySum, presellNum},
  data () {
    return {
      activitys: true,
      activityType: 1, // 活动页跳商品详情
      gradeId: '',
      promotionId: '',
      activityName: '活动',
      userState: 2,
      totalCount: 0,
      cur_page: 1,
      pagesize: 10,
      activityList: [],
      activityGoodsList: [],
      upDownOne: true, // 排序
      upDownTwo: true,
      upDownThr: true,
      offsetLeft: '',
      setNum: 0,
      chooseIndex: 0, // 选中效果
      userId: '',
      capitalStatus: 0, // 资质状态:0,未提交 1,申请中,2,合作中 3,申请失败'
      freezeStatus: 0, // 冻结状态:1,冻结  2,未冻结',
      tabs: 'showImg',
      imgurl: '',
      showImgurl: false,
      fastPage: 1,
      fastPagesize: 10,
      fastTotalCount: 0,
      fastOrderGoodsList: [],
      numInWarehouse: '', // 在库数量
      stockItemCount: '', // 在途数量
      stockoutCount: 0, // 缺货登记数量
      onceAgain: true,
      scrollBottom: false, // 页面滚动是否到底部
      showLoading: false, // 展示加载动画
      clickAgain: true, // 用于防止二次点击
      joinShopShopcar: [], // 加入购物车
      stockShowFlag: 0, // 0 实际库存 1模糊库存
      stockShowNumber: 0 // 库存临界值
    }
  },
  filters: {
    formatDate (time) {
      var date = new Date(time)
      return formatDate(date, 'yyyy-MM-dd hh:mm:ss')
    }
  },
  methods: {
    btnPrev () {
      var box = document.getElementById('box')
      var boxLi = document.getElementById('boxLi')
      if (this.setNum > 0) {
        this.offsetLeft = Number(this.offsetLeft) - Number(boxLi.offsetWidth + 8)
        box.style.left = -(this.offsetLeft) + 'px'
        this.setNum--
      }
    },
    btnNext () {
      // let foodList = this.$refs.foodList.getElementsByClassName('activity-lists')
      var box = document.getElementById('box')
      var boxLi = document.getElementById('boxLi')
      if (this.setNum < this.activityList.length - 3) {
        this.offsetLeft = Number(this.offsetLeft) + Number(boxLi.offsetWidth + 8)
        box.style.left = -(this.offsetLeft) + 'px'
        this.setNum++
      }
    },
    // 当前页码
    handleCurrentChange (val) {
      this.cur_page = val
      this.showLoading = true
      this.getActivityGoodsList()
    },
    // 每页条数
    handleSizeChange (val) {
      this.pagesize = val
      this.showLoading = true
      this.getActivityGoodsList()
    },
    // 选择活动
    chooseActivity (id, index) {
      this.chooseIndex = index
      this.promotionId = id
      this.showLoading = true
      if (this.tabs === 'showImg') {
        this.cur_page = 1
        this.getActivityGoodsList()
        this.getActivityGoodsSum()
      } else {
        this.fastPage = 1
        this.getFastOrderList()
        this.getFastOrderSum()
      }
    },
    // 活动促销列表
    getActivityList () {
      var tempArray = [] // 临时数组
      this.$api.get(this, 'user/u/marketing/promotion/list', {distributorId: this.userId, gradeId: this.gradeId}).then(res => {
        if (res.code === 0) {
          this.activityList = res.promotions
          this.activityList.forEach((item, index) => {
            // item.inSale 0 未开始, 1 促销中 2 已结束
            tempArray.push(item)
          })
          this.activityList = tempArray
          this.activityList.forEach((item, index) => {
            if (index === 0) {
              this.promotionId = item.id
              this.getActivityGoodsList()
              this.getActivityGoodsSum()
            }
          })
        }
      })
    },
    // 活动商品列表
    getActivityGoodsList () {
      this.showLoading = true
      this.$api.get(this, 'user/u/marketing/promotion/goods', {promotionId: this.promotionId, page: this.cur_page, count: this.pagesize}).then(res => {
        if (res.code === 0) {
          this.activityGoodsList = res.goodsList
          if (this.activityGoodsList.length === 0) {
            this.showLoading = false
            return false
          }
          this.activityGoodsList.forEach((item, index) => {
            if (index === this.activityGoodsList.length - 1) {
              this.showLoading = false
            }
            Vue.set(item, 'labelList', [])
            item.labelList = item.label.split(',')
          })
        } else if (res.code === 3) {
          this.showLoading = false
        
        } else {
          this.showLoading = false
        }
      })
    },
    // 活动商品列表总数
    getActivityGoodsSum () {
      var myDate = new Date()
      this.$api.get(this, 'user/u/marketing/promotion/goods/count?' + myDate.getMinutes() + myDate.getSeconds(), {promotionId: this.promotionId}).then(res => {
        if (res.code === 0) {
          this.totalCount = res.count
        }
      })
    },
    // 去活动商品详情
    goDoodsDetail (id, goodsType) {
      let routeData = this.$router.resolve({
        name: 'ShopDetail',
        query: {good_id: id, activityType: this.activityType, goodsType: goodsType, accessType: 0}
      })
      window.open(routeData.href, '_blank')
    },
    // 排序
    orderBy (type) {
      if (type === 1) {
        this.orderField = 'sale_time'
        this.upDownOne = !this.upDownOne
        if (this.orderType === '') {
          this.orderType = 'desc'
        } else if (this.orderType === 'desc') {
          this.orderType = 'asc'
        } else {
          this.orderType = 'desc'
        }
        this.getActivityGoodsList()
      } else if (type === 2) {
        this.orderField = 'sale_count'
        this.upDownThr = !this.upDownThr
        if (this.orderType === '') {
          this.orderType = 'desc'
        } else if (this.orderType === 'desc') {
          this.orderType = 'asc'
        } else {
          this.orderType = 'desc'
        }
        this.getActivityGoodsList()
      } else {
        this.orderField = 'price'
        this.upDownTwo = !this.upDownTwo
        if (this.orderType === '') {
          this.orderType = 'desc'
        } else if (this.orderType === 'desc') {
          this.orderType = 'asc'
        } else {
          this.orderType = 'desc'
        }
        this.getActivityGoodsList()
      }
    },
    // 获取快速订货商品列表
    getFastOrderList () {
      var myDate = new Date()
      this.showLoading = true
      this.$api.get(this, 'user/u/marketing/promotion/goods?' + myDate.getMinutes() + myDate.getSeconds(), {page: this.fastPage, count: this.fastPagesize, promotionId: this.promotionId}).then(res => {
        if (res.code === 0) {
          this.fastOrderGoodsList = res.goodsList
          if (this.fastOrderGoodsList.length === 0) {
            this.showLoading = false
          } else {
            this.fastOrderGoodsList.forEach((val, index) => {
              val.goodsItem = []
              val.tuType = false // 未勾选包含在途库存
              val.showRetailPrice = true // 是否展示零售价
              this.$api.get(this, 'user/goods', {id: val.goodsId}).then(res => {
                if (res.code === 0) {
                  this.$forceUpdate() // 更新页面数据
                  val.goodsItem = res.items
                  if (index === 0) {
                    this.stockShowFlag = res.stockShowFlag
                    this.stockShowNumber = res.stockShowNumber
                  }
                  val.goodsItem.forEach(item => {
                    item.numInWarehouse = 0 // 在库库存
                    item.stockItemCount = 0 // 在途库存
                    item.type = 0 // 缺货登记
                    item.remark = '' // 缺货登记备注
                    item.presell = 0 // 预售
                    item.visible = false // 预售库存不足弹框效果
                    item.itemType = res.goods.goodsType // 货品类型
                    if (item.moq === '停产' || item.moq === '' || item.moq === null) {
                      item.advanceSaleFlag = 0
                    }
                    if (item.salePrice > item.retailPrice && item.retailPrice !== null) { // 当存在会员价大于建议零售价
                      val.showRetailPrice = false
                    }
                    this.$api.get(this, 'user/u/warehouse/stock/distributor', {distributorId: this.userId, itemIds: item.id}).then(res => {
                      if (res.code === 0) {
                        if (index === this.fastOrderGoodsList.length - 1) {
                          this.showLoading = false
                        }
                        this.$forceUpdate() // 更新页面数据
                        if (res.stockItemCounts.length > 0) {
                          item.numInWarehouse = res.stockItemCount.stockItemCounts[0] + res.stockItemCounts[0].numVmi - res.stockItemCounts[0].numLock - res.stockItemCounts[0].numVmiLock - res.stockItemCounts[0].numOnWayLock - res.stockItemCounts[0].numReserved// 在库
                          item.stockItemCount = res.stockItemCounts[0].numInWarehouse + res.stockItemCounts[0].numVmi + res.stockItemCounts[0].numOnWay - res.stockItemCounts[0].numLock - res.stockItemCounts[0].numVmiLock - res.stockItemCounts[0].numOnWayLock - res.stockItemCounts[0].numReserved // 在库+在途
                          if (item.numInWarehouse <= 0) {
                            item.numInWarehouse = 0
                          }
                          if (item.stockItemCount <= 0) {
                            item.stockItemCount = 0
                          }
                        } else {
                          item.numInWarehouse = 0
                          item.stockItemCount = 0
                        }
                      }
                    })
                  })
                }
              })
            })
          }
        } else if (res.code === 3) {
          this.showLoading = false
          
        }
      })
    },
    getFastOrderSum () {
      var myDate = new Date()
      this.$api.get(this, 'user/u/marketing/promotion/goods/count?' + myDate.getMinutes() + myDate.getSeconds(), {promotionId: this.promotionId}).then(res => {
        if (res.code === 0) {
          this.fastTotalCount = res.count
        }
      })
    },
    fastHandleCurrentChange (val) {
      this.fastPage = val
      window.scrollTo(0, 0)
      this.getFastOrderList()
    },
    fastHandleSizeChange (val) {
      this.fastPagesize = val
      this.getFastOrderList()
    },
    // 图片展示和快速订货tab切换
    changeTu (goods, type) {
      this.$forceUpdate() // 更新页面数据
      if (type === true) {
        goods.tuType = false
      } else {
        goods.tuType = true
      }
    },
    toShowImg () {
      this.tabs = 'showImg'
    },
    toFastOrder () {
      this.$forceUpdate() // 更新页面数据
      this.tabs = 'fastOrder'
      this.getFastOrderList()
      this.getFastOrderSum()
    },
    // 缺货登记（添加）
    wantBook (goods, type) {
      this.$forceUpdate() // 更新页面数据
      this.stockoutCount = 1
      goods.remark = ''
      goods.type = 1
      if ((Number(this.capitalStatus) === 2 && Number(this.freezeStatus) === 2)) {
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
    // 缺货登记(提交)
    stockout (goods, goodsId) {
      this.onceAgain = false
      if (Number(this.stockoutCount) === 0) {
        this.$message.warning('缺货商品登记数量不能为0!')
        this.onceAgain = true
        return false
      } else {
        var json = {goodsId: goodsId, itemId: goods.id, specificationValueName: goods.specificationValueName, remark: goods.remark, needNumber: Number(this.stockoutCount)}
        this.$api.post(this, 'user/u/losegoods', json).then(res => {
          if (res.code === 0) {
            this.stockoutCount = 0
            this.$message.success('缺货商品添加成功!')
            goods.type = 0
            this.onceAgain = true
          } else {
            this.onceAgain = true
          }
        })
      }
    },
    handleAdd () {
      this.stockoutCount++
    },
    handleReduce () {
      if (this.stockoutCount > 0) {
        this.stockoutCount--
      }
    },
    handleInput (target) {
      var product = {'goodsId': Number(target.goodsId), 'itemId': target.id, 'itemType': 1, 'num': target.count}
      var addItem = true
      this.joinShopShopcar.forEach((item) => {
        if (item.itemId === target.id) {
          item.num = target.count
          addItem = false
        }
      })
      if (addItem === true) {
        this.joinShopShopcar.push(product)
      }
    },
    handleSpeInput (target) {
      var product = {'goodsId': Number(target.goodsId), 'itemId': target.id, 'itemType': 3, 'num': target.count}
      var addItem = true
      this.joinShopShopcar.forEach((item) => {
        if (item.itemId === target.id) {
          item.num = target.count
          addItem = false
        }
      })
      if (addItem === true) {
        this.joinShopShopcar.push(product)
      }
    },
    changeTab (item, index) { // 切换定制商品分组
      this.$forceUpdate() // 更新页面数据
      item.tabType = index
    },
    magnify (img) { // 点击放大上传的图片
      this.showImgurl = true
      this.imgurl = img
    },
    shutLogImg () {
      this.showImgurl = false
    },
    // 预售
    getVisible (target) {
      this.$forceUpdate()
    },
    shutLog (goods, type) { // 缺货登记预售关闭
      this.$forceUpdate()
      goods.type = 0
      goods.presell = 0
    },
    showPop (item) {
      this.$forceUpdate()
      item.visible = true
    },
    hidePop (item) {
      this.$forceUpdate()
      item.visible = false
    },
    // 加入购物车
    joinShopCar () { // (非预售商品)
      if (this.userId === null || this.userId === '') {
        return false
      } else if (Number(this.capitalStatus) === 2 && Number(this.freezeStatus) === 2) {
        var tempArray = [] // 临时数组
        this.clickAgain = false
        this.joinShopShopcar.forEach((obj) => {
          let orderType = 0 // 1.普通订单 2.预售订单 3.定制订单
          if (obj.itemType === 1) {
            orderType = 1
          } else if (obj.itemType === 3) {
            orderType = 3
          }
          if (obj.num !== undefined && Number(obj.num) !== 0) {
            var product = {'goodsId': Number(obj.goodsId), 'itemId': obj.itemId, 'itemType': obj.itemType, 'num': Number(obj.num), 'orderType': orderType}
            tempArray.push(product)
          }
        })
        if (tempArray.length <= 0) {
          this.clickAgain = true
          this.$message.warning(this.$t('P.GoodsItemNull'))
          return false
        } else {
          this.$api.post(this, 'user/u/shoppingCarts', {shoppingCars: tempArray}).then(res => {
            if (res.code === 0) {
              this.joinShopShopcar = []
              this.clickAgain = true
              if (this.$i18n.locale === 'zh') {
                this.$message.success('加入购物车成功')
              } else {
                this.$message.success('Add to shopping cart successfully.') 
              }
              this.getFastOrderList()
            } else if (res.code === 3) {
              this.clickAgain = true
             
            } else { this.clickAgain = true }
          }).catch(() => { this.clickAgain = true })
        }
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
    buyPresell (item) { // 购买预售商品
      var songGoodsShop = []
      var term = []
      var limitPresellPurchase = []
      if (item.count < item.moq) {
        this.$message.warning('订购数量不可小于' + item.moq)
        return false
      }
      var limitCustomized = {'goodsId': item.goodsId, 'itemId': item.id, 'itemCount': item.count, 'itemType': 1}
      limitPresellPurchase.push(limitCustomized)
      term.push(item)
      this.$api.post(this, 'user/u/order/restriction', {goods: limitPresellPurchase}).then(res => { // 限购规则判断
        if (res.code === 0) {
          window.localStorage.removeItem('shopCatShop')
          window.localStorage.removeItem('spesShopCatShop')
          window.localStorage.setItem('songGoodsShop', JSON.stringify(songGoodsShop)) // 赠品空数组
          window.localStorage.setItem('goodsItems', JSON.stringify(term))
          this.$router.push({name: 'ConsigneeInfor', query: {good_id: item.goodsId, goodsType: 4, values: 0}})
        } else if (res.code === 3) {
         
        }
      })
    },
    joincarPresell (goods) { // 预售商品加入购物车
      var tempArray = []
      var product = {'goodsId': goods.goodsId, 'itemId': goods.id, 'itemType': 1, 'num': Number(goods.count), 'orderType': 2}
      tempArray.push(product)
      this.$api.post(this, 'user/u/shoppingCarts', {shoppingCars: tempArray}).then(res => {
        if (res.code === 0) {
          if (this.$i18n.locale === 'zh') {
            this.$message.success('加入购物车成功')
          } else {
            this.$message.success('Add to shopping cart successfully.') 
          }
          goods.presell = 0
        } else if (res.code === 3) {
         
        }
      })
    },
    forwardSale (goods, type) {
      this.$forceUpdate()
      if ((Number(this.capitalStatus) === 2 && Number(this.freezeStatus) === 2)) {
        this.presellCount = goods.moq
        this.stockoutCount = 1
        goods.remark = ''
        goods.presell = 1
      } else if (Number(this.freezeStatus) === 1) {
        this.$message.warning('账号已冻结，无法进行此操作，请联系商务。')
      } else if (Number(this.capitalStatus) === 1) {
        this.$message.warning('账号仍在审核中，无法进行此操作，请等待审核通过。')
      } else if (Number(this.capitalStatus) === 3) {
        this.$message.warning('账号审核未通过，无法进行此操作。请重新发起申请。')
      } else if (Number(this.capitalStatus) === 4) {
        this.$message.warning('账号仍在审批中，无法进行此操作。请重新发起申请。')
      }
    }
  },
  mounted () {
    var state = window.localStorage.getItem('capitalStatus')
    var fstate = window.localStorage.getItem('freezeStatus')
    let self = this
    window.onscroll = function () {
      var scrollTop = document.documentElement.scrollTop || document.body.scrollTop
      var windowHeight = document.documentElement.clientHeight || document.body.clientHeight
      var scrollHeight = document.documentElement.scrollHeight || document.body.scrollHeight
      if (scrollTop + windowHeight >= scrollHeight - 300) {
        self.scrollBottom = false
      } else {
        self.scrollBottom = true
      }
    }
    this.capitalStatus = state
    this.freezeStatus = fstate
    var id = window.localStorage.getItem('userId')
    var gid = window.localStorage.getItem('gradeId')
    this.userId = id
    this.gradeId = gid
    this.getActivityList()
    this.showLoading = true
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
    .scrolls{
      width: 1190px;
      height: 80px;
      .btn-prev{
        width: 20px;
        height: 80px;
        float: left;
        cursor: pointer;
        background: url("../../src/assets/images/btn-prev.jpg") center center no-repeat;
      }
      .btn-next{
        width: 20px;
        height: 80px;
        float: left;
        cursor: pointer;
        background: url("../../src/assets/images/btn-next.jpg") center center no-repeat;
      }
      .activity{
        margin-left: 10px;
        margin-right: 10px;
        width: 1124px;
        overflow: hidden;
        height: 80px;
        ul{
          width: 5000px;
          position: absolute;
          top: 0;
          left: 0;
          height: 100px;
          overflow: hidden;
          li{
            display: inline-block;
            width: 328.5px;
            height: 80px;
            .name{
              overflow: hidden; white-space: nowrap;text-overflow: ellipsis;
              font-size: 18px;
            }
          }
          li.current{
            background-color: #00c9dc;
            div{color:#fff;}
          }
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
          .icon{
            position: absolute;
            top:0;
            right: -25px;
            display: block;
            width: 75px;
            height: 28px;
            background: url("../../src/assets/images/icon-up-down.png") no-repeat center center;
          }
        }
        .time{
          position: relative;
          margin-left: -1px;
          padding-right: 10px;
          width: 75px;
          height: 28px;
          line-height: 28px;
          .icon{
            position: absolute;
            top:0;
            right: -25px;
            display: block;
            width: 75px;
            height: 28px;
            background: url("../../src/assets/images/icon-up-down.png") no-repeat center center;
          }
        }
        .rotate{transform:rotate(180deg);}
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
      /*margin-top: 25px;*/
      ul{
        li{
          margin-bottom: 20px;
          display: inline-block;
          margin-left: 21px;
          cursor: pointer;
          transition: all .2s linear;
          .content{
            padding-bottom: 10px;
            width: 219px;
            background-color: #fff;
            img{height: 219px}
            .word{
              padding-left: 10px;
              margin-right: 5px;
              font-size: 12px;
              overflow: hidden; white-space: nowrap;text-overflow: ellipsis;
              button{
                padding: 0;
                border: 0;
                background-color: #f5f5f5;
                color: #000;
              }
            }
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
      .word{
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
      }
      .lable{
        margin-top: 5px;
        max-height: 22px;
        overflow: hidden;
        span{
          margin-right: 3px;
          padding-left: 5px;
          padding-right: 5px;
          display: inline-block;
          height: 20px;
          line-height: 20px;
          text-align: center;
          border: 1px solid #fd9636;
          border-radius: 5px;
          color:#fd9636;
          font-size: 12px;
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
  .sort-right{
    z-index: 1;
    background-color: #e7e7e7;
    border:1px solid rgba(191,191,191,1);
    div{
      width: 75px;
      height: 28px;
      line-height: 28px;
    }
    div.current{
      color: #00c9dc;
      background-color: #fff;
    }
  }
  /*快速订货*/
  .fast-img{
    width: 130px;
    margin-right: -1px;
    img{
      height: 130px;
      margin-left: -1px;
      margin-right: -1px;
      transition: all .2s linear;
    }
    img:hover{
      box-shadow: 0 15px 30px rgba(0,0,0,0.1);
      transform: translate3d(0,-2px,0);
    }
  }
  .fast-table{
    width: 1059px;
    .goodsName{
      padding-left: 22px;
      padding-right: 15px;
      height: 30px;
      line-height: 30px;
      .zaitu{
        height: 30px;
        line-height: 30px;
        span{
          display: block;
          width: 16px;
          height: 30px;
          background: url("../assets/images/choose.png") no-repeat center center;
        }
        span.gou{
          background: url("../assets/images/gou.png") no-repeat center center;
        }
      }
      .dingzhi{
        margin-top: 4px;
        width:49px;
        height:20px;
        line-height: 20px;
        color:rgba(253,121,35,1);
        background:rgba(255,255,255,1);
        border:1px solid rgba(253,121,35,1);
        border-radius:2px;
      }
    }
    table{
      /*table-layout: fixed;*/
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
          height: 30px;
          line-height: 30px;
          text-align: center;
          font-weight:400;
          color: #626162;
          font-size: 12px;
          border: 1px solid #BFBFBF;
        }
        td.td-h1{
          white-space: normal;
          .activity-label{
            width: 22px;
            height: 50px;
            line-height: 25px;
            border-radius: 5px;
            word-wrap: break-word;
          }
        }
        td{
          height: 30px;
          line-height: 30px;
          text-align: center;
          overflow: hidden; white-space: nowrap;text-overflow: ellipsis;
          color:#000;
          font-size: 12px;
          border: 1px solid #BFBFBF;
          button{
            padding: 0;
            border:0;
            background-color: #f5f5f5;
            font-size: 12px;
            color:#000;
          }
          .register{
            width: 55px;
            font-size: 12px;
            color: #ff7900;
          }
        }
      }
    }
  }
  /*加入购物车*/
  .join-shopcars{
    width:112px;
    height:35px;
    line-height: 35px;
    z-index: 98;
  }
  .join-shopcars-img{
    width:51px;
    z-index: 98;
  }
  .join-shopcars-img.scroll-bottom{
    position: fixed;
    bottom: 5%;
    right: 19%
  }
  /*缺货登记弹框*/
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
  /*定制商品分组样式*/
  .shop-img{
    padding: 2px;
    img{
      vertical-align: middle
    }
  }
  .fen-group{
    height: 50px;
    background-color: #e7e7e7;
    ul{
      li{
        display: inline-block;
        width: 120px;
        height: 30px;
        line-height: 30px;
        float: left;
        cursor: pointer;
        button{
          padding: 0;
          border:0;
          background-color: #e7e7e7;
          font-size: 12px;
          color:#000;
        }
      }
      li.current{
        background-color: #ccf4f8;
        button{
          padding: 0;
          border:0;
          background-color: #ccf4f8;
          font-size: 12px;
          color:#000;
        }
      }
    }
  }
  @media screen and (max-width: 1280px) {
    .join-shopcars-img.scroll-bottom{
      position: fixed;
      bottom: 5%;
      right: 5%
    }
  }
  /*预售*/
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
