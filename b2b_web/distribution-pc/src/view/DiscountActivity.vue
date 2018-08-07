<template>
  <div>
    <div class="index rl-margin-zero">
      <!--公共头部-->
      <Header :logOuts="logOut" :discountActivity="discountActivity"></Header>
      <!--主内容-->
      <div class="main rl-margin-zero rl-padding-top-default">
        <div class="sort rl-clear">
          <!--<div class="sort-left rl-fl">-->
            <!--<div class="rl-text-xxs rl-bg-gray-mm syn rl-text-blue-mm rl-tc rl-fl">综合排序</div>-->
            <!--<div class="rl-bg-white rl-fl">-->
              <!--<div @click="orderBy(1)" class="rl-text-xxs rl-tc rl-text-gray rl-bd-gray-sm time cursor-pointer"><span>上架时间</span><span class="icon" :class="upDownOne === true ? 'rotate': ''"></span></div>-->
            <!--</div>-->
            <!--&lt;!&ndash;<div @click="orderBy(3)" class="rl-bg-white rl-fl">-->
              <!--<div class="rl-text-xxs rl-tc rl-text-gray rl-bd-gray-sm common cursor-pointer"><span>价格</span><span class="icon" :class="upDownTwo === true ? 'rotate': ''"></span></div>-->
            <!--</div>&ndash;&gt;-->
            <!--<div class="rl-bg-white rl-fl">-->
              <!--<div @click="orderBy(2)"  class="rl-text-xxs rl-tc rl-text-gray rl-bd-gray-sm common cursor-pointer"><span>销量</span><span class="icon" :class="upDownThr === true ? 'rotate': ''"></span></div>-->
            <!--</div>-->
          <!--</div>-->
          <!--分页-->
          <div class="logo-box rl-clear rl-fl">
            <div class="rl-fl input-box rl-bg-white"><input @keyup.enter="getSearch" v-model="searchName" :placeholder="$t('Activitys.Search')" type="text" class="rl-padding-left-lllg"></div>
            <span @click="getSearch" class="rl-fl search rl-tc rl-text-white cursor-pointer">{{$t('P.Search')}}</span>
          </div>
          <div class="sort-right rl-fr">
            <!--<div @click="toShowImg" class="rl-fl rl-bg-gray-mm rl-tc rl-text-xxs cursor-pointer" :class ="{'current':tabs === 'showImg' }">图片展示</div>-->
            <div class="rl-fl rl-bg-gray-mm rl-tc rl-text-xxs cursor-pointer current">{{$t('P.QuickOrder')}}</div>
          </div>
        </div>
        <div v-if=" this.goodsList.length > 0" class="rl-bg-white rl-padding-top-xxs rl-padding-bottom-xxs rl-padding-left-default rl-bg-green_xs rl-margin-bottom-default">
          <div class="rl-text-xxs rl-margin-bottom-xxxxs" v-if="gradePromotionInfo">
            <div v-if="gradePromotionInfo.discountBeforeAfter === 1">
              <span v-if="gradePromotionInfo.moneyOrCount === 1">
                <i v-show="$i18n.locale === 'zh'">{{$t('Index.Discount')}}：折扣前金额满{{gradePromotionInfo.oneBuyMoney}}元，参与折扣活动</i>
                <i v-show="$i18n.locale === 'en'">{{$t('Index.Discount')}}：Participate in discount activities with a discount amount of $ {{gradePromotionInfo.oneBuyMoney}}</i>
              </span>
              <span v-else-if="gradePromotionInfo.moneyOrCount === 2">{{$t('Index.Discount')}}：数量满{{gradePromotionInfo.oneBuyCount}}件，参与折扣活动</span>
            </div>
            <div v-else-if="gradePromotionInfo.discountBeforeAfter === 2">
              <span v-if="gradePromotionInfo.moneyOrCount === 1">
                <i v-show="$i18n.locale === 'zh'">{{$t('Index.Discount')}}：折扣后金额满{{gradePromotionInfo.oneBuyMoney}}元，参与折扣活动</i>
                <i v-show="$i18n.locale === 'en'">{{$t('Index.Discount')}}：Participate in discount activities with a discount amount of $ {{gradePromotionInfo.oneBuyMoney}}</i>
              </span>
              <span v-else-if="gradePromotionInfo.moneyOrCount === 2">{{$t('Index.Discount')}}：{{$t('Promotion.QuantityReaches')}}{{gradePromotionInfo.oneBuyCount}}{{$t('Promotion.ParticipatePieces')}}</span>
            </div>
          </div>
          <!--<div class="rl-text-xxs" v-if="gradePromotionInfo">折扣时间：{{gradePromotionInfo.startTime | formatDate }}&#45;&#45;{{gradePromotionInfo.endTime | formatDate }} </div>-->
        </div>
        <!--快速订货-->
        <div v-if=" this.goodsList.length > 0">
          <div class="rl-clear rl-bg-white rl-margin-bottom-default" v-for="goods in goodsList" :key="goods.id" v-show="$i18n.locale === 'zh' || ($i18n.locale === 'en' && goods.goodsType === 1)" >
            <div class="fast-img rl-fl cursor-pointer" @click="goDoodsDetail(goods, goods.id, goods.goodsType)"><img width="100%" :src="(($i18n.locale === 'zh' || !goods.imageUrl1en == true)?goods.imageUrl1:goods.imageUrl1en)" alt=""></div>
            <div class="fast-table rl-fl">
              <div class="goodsName rl-bg-blue-mm rl-text-white rl-clear">
                <div class="rl-fl rl-text-xxs">
                  <span v-show="$i18n.locale === 'zh' || !goods.goodsNameEn == true">{{goods.goodsName}}</span>
                  <span v-show="$i18n.locale === 'en'">{{goods.goodsNameEn}}</span>
                </div>
                <div class="rl-fl rl-tc rl-text-xxs rl-margin-left-default cursor-pointer" @click="lookBox(goods.items)"><span class="gui-ge">{{$t('P.PCSCTN')}}</span></div>
                <div class="rl-fl dingzhi rl-tc rl-text-xxs rl-margin-left-default" v-if="goods.goodsType === 2">{{$t('Index.Customized')}}</div>
                <div v-if="goods.goodsType === 1" @click="changeTu(goods,goods.tuType)" class="rl-fr rl-text-xxs zaitu rl-clear cursor-pointer"><span class="rl-fl" :class ="{'gou':goods.tuType === true}"></span>{{$t('P.ConTran')}}</div>
              </div>
              <table>
                <tr>
                  <th>{{$t('ShopCarts.ItemNo')}}</th>
                  <th>{{$t('ShopCarts.BarCode')}}</th>
                  <th>{{$t('ShopCarts.Spe')}}</th>
                  <th>{{$t('ShopCarts.Colors')}}</th>
                  <th>{{$t('ShopCarts.RecommendedPrice')}}</th>
                  <th>{{$t('ShopCarts.MemPrice')}}</th>
                  <th>{{$t('ShopCarts.DiscountPrice')}}</th>
                  <th width="13%">{{$t('ShopCarts.Quantity')}}</th>
                  <th v-if="goods.goodsType === 1">{{$t('P.Inventory')}}</th>
                  <th>{{$t('P.Dimension')}}</th>
<!--                  <th>装箱数</th>-->
                  <th>{{$t('P.Weight')}}(g)</th>
                  <th v-if="goods.diyType === 1">{{$t('ShopCarts.Operation')}}</th>
                </tr>
                <tr v-for="item in goods.items" :key="item.id">
                  <td>{{item.itemCode}}</td>
                  <td>{{item.barCode}}</td>
                  <td>
                    <el-tooltip class="item" effect="dark" :content="item.specificationName" placement="bottom">
                      <el-button>
                        <span v-show="$i18n.locale === 'zh' || !item.specificationNameEn == true">{{item.specificationName}}</span>
                        <span v-show="$i18n.locale === 'en'">{{item.specificationNameEn}}</span>
                      </el-button>
                    </el-tooltip>
                  </td>
                  <td>
                    <el-tooltip class="item" effect="dark" :content="item.colorValueName" placement="bottom">
                      <el-button>
                        <span v-show="$i18n.locale === 'zh' || !item.colorValueNameEn == true">{{item.colorValueName}}</span>
                        <span v-show="$i18n.locale === 'en'">{{item.colorValueNameEn}}</span>
                      </el-button>
                    </el-tooltip>
                  </td>
                  <td v-if="!userId == ''">
                    <span v-if="item.retailPrice !== null && Number(item.retailPrice) !== 0">
                      <i v-show="$root.currency === 'CNY'" >￥{{item.retailPrice}}</i>
                      <i v-show="$root.currency === 'USD'" >${{item.retailPriceEn}}</i>
                    </span><span v-else>--</span></td>
                  <td v-if="!userId == ''">
                    <span v-if="Number(item.salePrice) !== 0 && item.salePrice !== null">
                      <i v-show="$root.currency === 'CNY'" >￥{{item.salePrice}}</i>
                      <i v-show="$root.currency === 'USD'" >${{item.salePriceEn}}</i>
                    </span><span v-else>{{item.salePrice === undefined || item.salePrice === null ? (!userId == '' &&
                        freezeStatus !== undefined && freezeStatus !== null && freezeStatus === '2' &&
                        capitalStatus !== undefined && capitalStatus !== null && capitalStatus === '2'? $t('P.Calculating') : $t('P.ViewAfterLogin')) : $t('P.NoPricing')}}</span></td>
                  <td v-if="!userId == ''">
                    <i v-show="$root.currency === 'CNY'" >￥{{item.promotionPrice}}</i>
                    <i v-show="$root.currency === 'USD'" >${{item.promotionPriceEn}}</i>
                  </td>
                  <td v-if="!userId == ''">
                    <div class="rl-margin-left-xxxs" v-if="goods.diyType === 0">
                      <BuySum ref="BuySum" :item="item" :tuType="goods.tuType" @itemInput="handleInput" @visible="getVisible"></BuySum>
                    </div>
                    <div class="rl-margin-left-xxxs" v-else>
                      <el-input-number disabled :min="0"  label="描述文字" size="mini" ></el-input-number>
                    </div>
                  </td>
                  <td class="rl-relative" v-if="!userId == '' && goods.tuType === false && goods.goodsType === 1">
                    <span class="blank-span"></span>
                    <el-popover
                      placement="bottom"
                      width="230"
                      v-model="item.visible"
                      @show="showPop(item)"
                      @hide="hidePop(item)"
                      trigger="click">
                      <p class="rl-margin-bottom-xxs rl-tc rl-text-bold">{{$t('P.ShortInventory')}}</p>
                      <p class="rl-margin-bottom-xxxxs rl-tc rl-text-xxs">{{$t('ShopDetail.Proceed')}}<span class="cursor-pointer rl-margin-left-ls rl-text-blue-xs rl-bdb-blue-xs" @click="wantBook(item,item.type)">{{$t('UserCenter.Shortage')}}</span></p>
                      <p class="rl-tc rl-text-xxs" v-if="item.advanceSaleFlag === 1">{{$t('ShopDetail.OrChoose')}}<span class="cursor-pointer rl-margin-left-ls rl-text-blue-xs rl-bdb-blue-xs" @click="forwardSale(item,item.presell)">{{$t('P.AdvanceSales')}}</span></p>
                      <div class="rl-text-xxs" slot="reference" v-if="stockShowFlag === 0">{{item.numInWarehouse}}</div>
                      <div class="rl-text-xxs" slot="reference" v-else-if="stockShowFlag === 1 && (stockShowNumber < item.numInWarehouse)">{{item.numInWarehouse}}</div>
                      <div class="rl-text-xxs" slot="reference" v-else-if="stockShowFlag === 1 && item.numInWarehouse === 0">无货</div>
                      <div class="rl-text-xxs" slot="reference" v-else-if="stockShowFlag === 1 && (0 < item.numInWarehouse <= stockShowNumber)">库存紧张</div>
                    </el-popover>
                  </td>
                  <td class="rl-relative" v-else-if="!userId == '' && goods.tuType === true && goods.goodsType === 1">
                    <span class="blank-span"></span>
                    <el-popover
                      placement="bottom"
                      width="230"
                      v-model="item.visible"
                      @show="showPop(item)"
                      @hide="hidePop(item)"
                      trigger="click">
                      <p class="rl-margin-bottom-xxs rl-tc rl-text-bold">{{$t('P.ShortInventory')}}</p>
                      <p class="rl-margin-bottom-xxxxs rl-tc rl-text-xxs">{{$t('ShopDetail.Proceed')}}<span class="cursor-pointer rl-margin-left-ls rl-text-blue-xs rl-bdb-blue-xs" @click="wantBook(item,item.type)">{{$t('UserCenter.Shortage')}}</span></p>
                      <p class="rl-tc rl-text-xxs" v-if="item.advanceSaleFlag === 1">{{$t('ShopDetail.OrChoose')}}<span class="cursor-pointer rl-margin-left-ls rl-text-blue-xs rl-bdb-blue-xs" @click="forwardSale(item,item.presell)">{{$t('P.AdvanceSales')}}</span></p>
                      <div class="rl-text-xxs" slot="reference" v-if="stockShowFlag === 0">{{item.stockItemCount}}</div>
                      <div class="rl-text-xxs" slot="reference" v-else-if="stockShowFlag === 1 && (stockShowNumber < item.stockItemCount)">{{item.stockItemCount}}</div>
                      <div class="rl-text-xxs" slot="reference" v-else-if="stockShowFlag === 1 && item.stockItemCount === 0">无货</div>
                      <div class="rl-text-xxs" slot="reference" v-else-if="stockShowFlag === 1 && (0 < item.stockItemCount <= stockShowNumber)">库存紧张</div>
                    </el-popover>
                  </td>
                  <td v-else></td>
                  <td v-if="item.length !== null && item.width !== null && item.height !== null && item.unit !== null">
                    <el-tooltip class="item" effect="dark" :content="item.length + '*' + item.width + '*' + item.height + '(' + item.unit + ')'" placement="bottom">
                      <el-button>{{item.length}}*{{item.width}}*{{item.height}}({{item.unit}})</el-button>
                    </el-tooltip>
                  </td>
                  <td v-else></td>
<!--                  <td><el-button @click.native.prevent="lookBox(item.itemCode)" style="color: #00c9dc">查看详情</el-button></td>-->
                  <td class="rl-bdr-gray-sm">{{item.weight}}</td>
                  <td v-if="goods.diyType === 1" class="rl-bdr-gray-sm"><div @click="goCustrom(item.itemCode)" class="dingzhi-btn">{{$t('Index.Customized')}}</div></td>
                  <!--缺货登记弹框-->
                  <div class="cover" v-if="item.type === 1"></div>
                  <div class="pro-cover cover-box rl-padding-bottom-lllg rl-padding-top-default rl-relative" v-if="item.type === 1">
                    <div class="rl-padding-bottom-default rl-tc rl-text-mid">{{$t('UserCenter.Shortage')}}</div>
                    <span @click="shutLog(item,item.type)" class="shut cursor-pointer"></span>
                    <div class="item rl-text-gray rl-clear rl-margin-bottom-xxxs">
                      <div class="rl-fl rl-margin-right-double">{{$t('ShopCarts.ItemNo')}}</div>
                      <div class="rl-fl">{{item.itemCode}}</div>
                    </div>
                    <div class="item rl-text-gray rl-clear rl-margin-bottom-xxxs">
                      <div class="rl-fl rl-margin-right-double">{{$t('ShopCarts.Spe')}}</div>
                      <div v-if="$i18n.locale === 'en' && item.specificationNameEn !== undefined && item.specificationNameEn !== null && item.specificationNameEn !== ''" class="rl-fl">{{item.specificationNameEn}}</div>
                      <div v-else class="rl-fl">{{item.specificationName}}</div>
                    </div>
                    <div class="item rl-text-gray rl-clear rl-margin-bottom-xxxs">
                      <div class="rl-fl rl-margin-right-double">{{$t('ShopCarts.Colors')}}</div>
                      <div v-if="$i18n.locale === 'en' && item.colorValueNameEn !== undefined && item.colorValueNameEn !== null && item.colorValueNameEn !== '' " class="rl-fl">{{item.colorValueNameEn}}</div>
                      <div v-else class="rl-fl">{{item.colorValueName}}</div>
                    </div>
                    <div v-show="false" class="item rl-text-gray rl-clear rl-margin-bottom-xxxs">
                      <div class="rl-fl rl-margin-right-default">{{$t('ShopDetail.CommodityNo')}}</div>
                      <div class="rl-fl">{{item.goodsNo}}</div>
                    </div>
                    <div class="item rl-text-gray rl-clear rl-margin-bottom-xxxs">
                      <div class="rl-fl rl-margin-right-default">{{$t('ShopCarts.ItemName')}}</div>
                      <div v-if="$i18n.locale === 'en' && item.itemNameEn !== undefined && item.itemNameEn !== null && item.itemNameEn !== ''" class="rl-fl">{{item.itemNameEn}}</div>
                      <div v-else class="rl-fl">{{item.itemName}}</div>
                    </div>
                    <div class="item rl-text-gray rl-clear rl-margin-bottom-xxxs">
                      <div class="rl-fl rl-margin-right-default left">{{$t('ShopCarts.Quantity')}}</div>
                      <div class="rl-fl">
                        <div class="buy-sum rl-clear">
                          <div @click.stop.prevent="handleReduce()" class="rl-fl buyac buya rl-text-gray">-</div>
                          <div class="rl-fl buyb"><input v-model="stockoutCount" class="rl-tc" type="text" maxlength="8" onkeyup="this.value=this.value.replace(/\D/g,'')"></div>
                          <div @click.stop.prevent="handleAdd()" class="rl-fl buyac buyc rl-text-gray">+</div>
                        </div>
                      </div>
                    </div>
                    <div class="item rl-text-gray rl-clear">
                      <div class="rl-fl rl-margin-right-default">{{$t('UserCenter.Remarkes')}}</div>
                      <div class="rl-fl">
                        <textarea v-model="item.remark" name="" id="" cols="30" rows="10" class="common-text"></textarea>
                      </div>
                    </div>
                    <div v-if="onceAgain === true" class="button"><span @click="stockout(item)" class="rl-margin-top-default cursor-pointer">{{$t('P.OK')}}</span></div>
                    <div v-else class="button"><span class="rl-margin-top-default">{{$t('P.OK')}}</span></div>
                  </div>
                  <!--预售弹框-->
                  <div class="cover" v-if="item.presell === 1"></div>
                  <div style="height:260px" class="pro-cover cover-box rl-padding-bottom-lllg rl-padding-top-default rl-relative" v-if="item.presell === 1">
                    <div class="rl-padding-bottom-default rl-tc rl-text-mid">{{$t('P.AdvanceSales')}}</div>
                    <span @click="shutLog(item,item.presell)" class="shut cursor-pointer"></span>
                    <div class="item rl-text-gray rl-clear rl-margin-bottom-xxxs">
                      <div class="rl-fl rl-margin-right-double">{{$t('ShopCarts.ItemNo')}}</div>
                      <div class="rl-fl">{{item.itemCode}}</div>
                    </div>
                    <div class="item rl-text-gray rl-clear rl-margin-bottom-xxxs">
                      <div class="rl-fl rl-margin-right-double">{{$t('ShopCarts.Spe')}}</div>
                      <div class="rl-fl">{{item.specificationValueName}}</div>
                    </div>
                    <div class="item rl-text-gray rl-clear rl-margin-bottom-xxxs">
                      <div class="rl-fl rl-margin-right-double">{{$t('ShopCarts.Colors')}}</div>
                      <div class="rl-fl">{{item.colorValueName}}</div>
                    </div>
                    <div class="item rl-text-gray rl-clear rl-margin-bottom-xxxs">
                      <div class="rl-fl rl-margin-right-default left">{{$t('ShopCarts.Quantity')}}</div>
                      <div class="rl-fl" v-if="item.moq >= 0">
                        <presellNum ref="presellNum" :item="item"></presellNum>
                      </div>
                      <div v-else>{{item.moq}}</div>
                    </div>
                    <div v-if="onceAgain === true && item.moq >= 0" class="button-presell rl-fl"><span @click="buyPresell(item)" class="rl-margin-top-default cursor-pointer">{{$t('P.BuyNow')}}</span></div>
                    <div v-else class="button-presell rl-fl"><span class="rl-margin-top-default">{{$t('P.BuyNow')}}</span></div>
                    <div v-if="onceAgain === true && item.moq >= 0" class="button-presell rl-fl"><span @click="joincarPresell(item)" class="rl-margin-top-default cursor-pointer">{{$t('P.AddToCart')}}</span></div>
                    <div v-else class="button-presell rl-fl"><span class="rl-margin-top-default">{{$t('P.AddToCart')}}</span></div>
                  </div>
                </tr>
              </table>
            </div>
            <div class="fast-table rl-fl" v-if="goods.goodsType === 2">
<!--              <div class="goodsName rl-bg-blue-mm rl-text-white rl-clear">-->
<!--                <div class="rl-fl rl-text-xxs">{{goods.goodsName}}</div>-->
<!--                <div class="rl-fl dingzhi rl-tc rl-text-xxs rl-margin-left-default">定制</div>-->
<!--              </div>-->
              <!--<table>-->
                <!--<tr>-->
                  <!--<td colspan="12" class="fen-group"><ul><li @click="changeTab(goods,index)" v-for="(item,index) in goods.customizedItems" :key="item.id" :class ="{'current':goods.tabType === index}">-->
                    <!--<div v-if="item.itemGroupRemark !== null && item.itemGroupRemark !== ''">-->
                      <!--<el-tooltip class="item" effect="dark" :content="item.itemGroupRemark" placement="bottom" >-->
                        <!--<el-button>{{item.itemGroup}}</el-button>-->
                      <!--</el-tooltip>-->
                    <!--</div>-->
                    <!--<div v-else>{{item.itemGroup}}</div>-->
                  <!--</li></ul></td>-->
                <!--</tr>-->
              <!--</table>-->
              <!--<table v-for="(item,index) in goods.customizedItems" :key="item.id">-->
                <!--<tr v-show="goods.tabType === index">-->
                  <!--<th>货号</th>-->
                  <!--<th>条形码</th>-->
                  <!--<th>规格</th>-->
                  <!--<th>颜色</th>-->
                  <!--<th v-if="!userId == '' && goods.showRetailPrice === true">建议零售价</th>-->
                  <!--<th>会员价</th>-->
                  <!--<th width="13%">订购数量</th>-->
                  <!--<th>图片</th>-->
                  <!--<th>尺寸</th>-->
                  <!--<th>装箱数</th>-->
                  <!--<th>重量(g)</th>-->
                <!--</tr>-->
                <!--<tr v-for="value in item.userItems" :key="value.id" v-show="goods.tabType === index">-->
                  <!--<td>{{value.itemCode}}</td>-->
                  <!--<td>{{value.barCode}}</td>-->
                  <!--<td>{{value.specificationValueName}}</td>-->
                  <!--<td>{{value.colorValueName}}</td>-->
                  <!--<td v-if="!userId == '' && goods.showRetailPrice === true"><span v-if="value.retailPrice !== null && Number(value.retailPrice) !== 0">￥{{value.retailPrice}}</span>-->
                    <!--<span v-else>&#45;&#45;</span></td>-->
                  <!--<td v-else-if="userId === '' && goods.showRetailPrice === true" class="rl-text-xxss">登录后查看</td>-->
                  <!--<td v-if="!userId == ''">￥{{value.salePrice}}</td>-->
                  <!--<td v-else class="rl-text-xxss">登录后查看</td>-->
                  <!--<td width="140px" v-if="!userId == ''">-->
                    <!--<div class="rl-margin-left-xxxs">-->
                      <!--<GoodsBuySum ref="GoodsBuySum" :item="value" @itemInput="handleSpeInput"></GoodsBuySum>-->
                    <!--</div>-->
                  <!--</td>-->
                  <!--<td v-else class="rl-text-xxss">登录后查看</td>-->
                  <!--<td width="50px"><div class="shop-img cursor-pointer"><img @click="magnify(value.itemImg)" width="100%" :src="value.itemImg" alt=""></div></td>-->
                  <!--<td v-if="value.sizeStr !== null && value.unit !== null">{{value.sizeStr}}({{value.unit}})</td>-->
                  <!--<td v-else></td>-->
                  <!--<td>{{value.boxNum}}</td>-->
                  <!--<td class="rl-bdr-gray-sm">{{value.weight}}</td>-->
                <!--</tr>-->
              <!--</table>-->
            </div>
          </div>
          <!-- syh-20180606:start -->
          <!-- 加入购物车 -->
          <div class="rl-tr rl-margin-bottom-default rl-clear">
            <div class="rl-fl join-shopcars rl-text-white rl-bg-blue-xs rl-tc rl-text-mid cursor-pointer rl-margin-left-double" @click="joinShopCar">{{$t('P.AddToCart')}}</div>
            <div class="rl-fl join-shopcars rl-text-white rl-bg-orange rl-tc rl-text-mid cursor-pointer rl-margin-left-double" @click="buyNow">{{$t('P.BuyNow')}}</div>
            <!-- 分页 -->
            <div class="rl-tr rl-margin-bottom-default rl-clear" v-if="goodsList.length !== 0">
              <div class="rl-fr"><el-pagination
                background
                @current-change ="getColumnGoodsListPageChange"
                @size-change="getColumnGoodsListPageSizeChange"
                layout="prev, pager, next, jumper"
                :page-size="pagesize"
                :total="totalCount"
              >
              </el-pagination></div>
            </div>
          </div>
          <!-- syh-20180606:end -->
        </div>
      </div>
      <!--点击图片放大-->
      <div class="cover" v-if="showImgurl" @click="shutLogImg"></div>
      <div class="pro-cover-img rl-relative"  v-if="showImgurl">
        <img  width="100%" :src="imgurl" alt="">
        <span @click="shutLogImg" class="shut cursor-pointer"></span>
      </div>
      <!--商品为空-->
      <div class="empty-car rl-margin-zero" v-if="(this.goodsList === null || this.goodsList === undefined || this.goodsList.length === 0) && this.showLoading === false">
        <div class="empty-car-img rl-margin-zero"><img width="100%" src="../assets/images/goods-empty.png" alt=""></div>
        <p class="rl-tc rl-margin-top-default rl-margin-bottom-default rl-text-sm rl-text-gray">{{$t('P.NoCommodities')}}</p>
      </div>
      <!--悬浮球-->
      <div v-if=" this.goodsList.length > 0" class="join-ball-img rl-text-white rl-tc rl-text-mid cursor-pointer">
        <div class="rl-text-xxss rl-text-black ball-img" v-if="gradePromotionInfo.moneyOrCount === 2">
          <div class="ball-img-top" :class="gradePromotionInfo.oneBuyCount > chooseTotalCount ? 'rl-bg-orange-mm' : 'rl-bg-blue-xs'">
            <div class="rl-text-white rl-padding-top-xxxs rl-text-xxs">{{$t('Index.Reach')}}</div>
            <div class="rl-text-white rl-text-xxss">{{chooseTotalCount}}</div>
          </div>
          <div class="ball-img-bottom">
            <div class="rl-text-white rl-padding-top-xxxxs rl-text-xxss">{{gradePromotionInfo.oneBuyCount}}</div>
            <div class="rl-text-white rl-text-xxs">{{$t('Index.Target')}}</div>
          </div>
        </div>
        <div class="rl-text-xxss rl-text-black ball-img" v-if="gradePromotionInfo.moneyOrCount === 1">
          <div class="ball-img-top" :class="gradePromotionInfo.oneBuyMoney > chooseTotalMoney ? 'rl-bg-orange-mm' : 'rl-bg-blue-xs'">
            <div class="rl-text-white rl-padding-top-xxxs rl-text-xxs">{{$t('Index.Reach')}}</div>
            <div class="rl-text-white rl-text-xxss">{{chooseTotalMoney.toFixed(2)}}</div>
          </div>
          <div class="ball-img-bottom">
            <div class="rl-text-white rl-padding-top-xxxxs rl-text-xxss">{{gradePromotionInfo.oneBuyMoney}}</div>
            <div class="rl-text-white rl-text-xxs">{{$t('Index.Target')}}</div>
          </div>
        </div>
      </div>
      <!--公共底部-->
      <Footer></Footer>
    </div>
    <!--    装箱规格-->
    <el-dialog class="alls rl-tc" :title="$t('P.PCSCTN')" :visible.sync="boxDialogVisible">
      <div class="box-table rl-padding-left-default rl-padding-right-default">
        <el-table :data="boxData" border max-height="650" header-row-class-name="header-row" class="activity-el-table rl-tc">
          <el-table-column :label="$t('P.Carton')" prop="boxType"> </el-table-column>
          <el-table-column :label="$t('P.DIM')" prop="size"> </el-table-column>
          <el-table-column :label="$t('P.QTY')" prop="boxNum"> </el-table-column>
          <el-table-column :label="$t('P.WeighG')" prop="boxWeight"> </el-table-column>
        </el-table>
      </div>
      <div slot="footer" class="dialog-footer rl-tc">
        <el-button @click="boxDialogVisible = false">{{$t('P.Cancel')}}</el-button>
      </div>
    </el-dialog>
    <!--加载动画-->
    <loading v-if="this.showLoading === true"></loading>
  </div>
</template>

<script>
import Footer from '@/components/Footer.vue'
import Header from '@/components/Header.vue'
import {formatDate,} from '@/assets/js/common.js'
import BuySum from '@/components/BuySum.vue'
import presellNum from '@/components/presellNum.vue'
import GoodsBuySum from '@/components/GoodsBuySum.vue'
import loading from '@/components/loading.vue'
import sha1 from 'js-sha1'
import Vue from 'vue'
import GD from '@/assets/js/globalData'
export default {
  name: 'DiscountActivity',
  components: {Footer, Header, BuySum, GoodsBuySum, loading, presellNum},
  data () {
    return {
      searchName: '',
      boxData: {},
      boxDialogVisible: false,
      activityType: 0, // 普通页面页跳商品详情
      logOut: true, // 被登出状态
      columnId: this.$route.query.column_id,
      discountActivity: true,
      reLoad: true,
      userState: 2,
      totalCount: 0,
      cur_page: 1,
      pagesize: 10,
      goodsList: [],
      orderField: '', // 排序类型
      orderType: '', // 排序顺序
      upDownOne: true,
      upDownTwo: true,
      upDownThr: true,
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
      goodsItems: [], // 立即购买
      confirmBuyItem: [], // 最终购买的货品
      stockShowFlag: 0, // 0 实际库存 1模糊库存
      stockShowNumber: 0, // 库存临界值
      onWayAttendEventFlag: 0, // 在途商品是否参与活动 1.参与，0.不参与
      gradePromotionInfo: {}, // 折扣活动信息
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: 'zh-RMB' // 语种
    }
  },
  filters: {
    formatDate (time) {
      var date = new Date(time)
      return formatDate(date, 'yyyy-MM-dd hh:mm:ss')
    }
  },
  computed: {
    chooseTotalCount () {
      var total = 0
      console.log(this.joinShopShopcar)
      this.confirmBuyItem.forEach((item) => {
        if (item.count > 0) {
          if (this.onWayAttendEventFlag === 0) {
            if (item.goodsType === 1) {
              total = total + parseInt(item.numInWarehouse)
            } else {
              total = total + parseInt(item.count)
            }
          } else {
            total = total + parseInt(item.count)
          }
        }
      })
      return total
    },
    chooseTotalMoney () {
      var total = 0
      this.confirmBuyItem.forEach((item) => {
        if (item.count > 0) {
          if (this.onWayAttendEventFlag === 0) {
            if (this.gradePromotionInfo.discountBeforeAfter === 1) {
              if (item.goodsType === 1) {
                if (item.count <= item.numInWarehouse) { // 当购买数量小于等于在库库存
                  total = total + parseInt(item.count) * item.salePrice
                } else {
                  total = total + parseInt(item.numInWarehouse) * item.salePrice
                }
              } else {
                total = total + parseInt(item.count) * item.salePrice
              }
            } else if (this.gradePromotionInfo.discountBeforeAfter === 2) {
              if (item.goodsType === 1) {
                if (item.count <= item.numInWarehouse) {
                  total = total + parseInt(item.count) * item.promotionPrice
                } else {
                  total = total + parseInt(item.numInWarehouse) * item.promotionPrice
                }
              } else {
                total = total + parseInt(item.count) * item.promotionPrice
              }
            }
          } else {
            if (this.gradePromotionInfo.discountBeforeAfter === 1) {
              total = total + parseInt(item.count) * item.salePrice
            } else if (this.gradePromotionInfo.discountBeforeAfter === 2) {
              total = total + parseInt(item.count) * item.promotionPrice
            }
          }
        }
      })
      return total
    }
  },
  methods: {
    fLangChange (value) {
      window.localStorage.setItem('bLang', value);
      this.$i18n.locale = value.split('-')[0];
    },
    goCustrom (code) { // 获取DIY编辑器地址
      var timestamp = Date.parse(new Date()) // 获取时间戳
      let diyItemCode = code
      let Source = 0 // 0：b2b diy定制 1：收单diy定制
      let Signature = ''
      let AppID = '5652338659'
      let AppKey = '14b94847d9744cf6988526fcac9b6107'
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
    },
    lookBox (itemData) { // 装箱规格
      this.boxData = [] // 清空
      for (let i = 0; i < itemData.length; i++) {
        if (this.boxData.length === 0) {
          this.$api.get(this, 'user/goods/getBoxInfo', {itemCode: itemData[i].itemCode}).then(res => {
            if (res.code === 0) {
              if (res.data.length > 0) {
                this.boxDialogVisible = true
                this.boxData = res.data
                let innerWeight = 0 // 内盒重量
                let innerNum = 0 // 内盒数量
                this.boxData.forEach(item => {
                  Vue.set(item, 'size', '') // 尺寸
                  item.size = item.boxLength + '*' + item.boxWidth + '*' + item.boxHeight
                  if (item.boxType === '内盒' || item.boxType === 'inner box') {
                      if(this.$i18n.locale === 'en'){
                        item.boxType = 'inner box'
                      }
                    innerWeight = item.boxWeight
                    innerNum = item.boxNum
                    item.boxWeight = itemData[i].weight * item.boxNum + item.boxWeight
                  } else if (item.boxType === '外盒' || item.boxType === 'outer box') {
                    if(this.$i18n.locale === 'en'){
                      item.boxType = 'outer box'
                    }
                    if (innerNum > 0) { // 当内盒数量大于0
                      item.boxWeight = itemData[i].weight * item.boxNum + innerWeight * (item.boxNum / innerNum) + item.boxWeight
                    } else {
                      item.boxWeight = itemData[i].weight * item.boxNum + item.boxWeight
                    }
                    item.boxWeight = item.boxWeight.toFixed(0)
                  }
                })
              }
              if (i === itemData.length - 1 && this.boxData.length === 0) {
                this.$message.warning('暂无装箱规格数据')
              }
            }
          })
        }
      }
    },
    lookBoxSpe (groupData) { // 装箱规格
      this.boxData = [] // 清空
      for (let i = 0; i < groupData.length; i++) {
        for (let j = 0; j < groupData[i].userItems.length; j++) {
          if (this.boxData.length === 0) {
            this.$api.get(this, 'user/goods/getBoxInfo', {itemCode: groupData[i].userItems[j].itemCode}).then(res => {
              if (res.code === 0) {
                if (res.data.length > 0) {
                  this.boxDialogVisible = true
                  this.boxData = res.data
                  let innerWeight = 0 // 内盒重量
                  let innerNum = 0 // 内盒数量
                  this.boxData.forEach(item => {
                    Vue.set(item, 'size', '') // 尺寸
                    item.size = item.boxLength + '*' + item.boxWidth + '*' + item.boxHeight
                    if (item.boxType === '内盒' || item.boxType === 'inner box') {
                      if(this.$i18n.locale === 'en'){
                        item.boxType = 'inner box'
                      }
                      innerWeight = item.boxWeight
                      innerNum = item.boxNum
                      item.boxWeight = groupData[i].userItems[j].weight * item.boxNum + item.boxWeight
                    } else if (item.boxType === '外盒' || item.boxType === 'outer box') {
                      if(this.$i18n.locale === 'en'){
                        item.boxType = 'outer box'
                      }
                      if (innerNum > 0) { // 当内盒数量大于0
                        item.boxWeight = groupData[i].userItems[j].weight * item.boxNum + innerWeight * (item.boxNum / innerNum) + item.boxWeight
                      } else {
                        item.boxWeight = groupData[i].userItems[j].weight * item.boxNum + item.boxWeight
                      }
                      item.boxWeight = item.boxWeight.toFixed(0)
                    }
                  })
                }
                if (i === groupData.length - 1 && j === groupData[i].userItems.length - 1 && this.boxData.length === 0) {
                  this.$message.warning('暂无装箱规格数据')
                }
              } 
            })
          }
          if (this.boxDialogVisible === true) {
            break
          }
        }
        if (this.boxDialogVisible === true) {
          break
        }
      }
    },
    addFood (target) { // 切换栏目ID
      this.columnId = target
      this.getColumnGoodsList()
      this.getColumnGoodsSum()
    },
    bddFood (target) { // 栏目index
      this.nav_index = target + 1
    },
    // 当前页码
    handleCurrentChange (val) {
      this.cur_page = val
      window.scrollTo(0, 0)
      this.getColumnGoodsList()
    },
    // 每页条数
    handleSizeChange (val) {
      this.pagesize = val
      this.getColumnGoodsList()
    },
    getSearch () { // 搜索
      this.goodsList = []
      this.getColumnGoodsList()
      this.getColumnGoodsSum()
    },
    // 获取栏目商品列表
    getColumnGoodsList () {
      var myDate = new Date()
      this.showLoading = true
      this.$api.get(this, 'user/goods/grade/discount/list?' + myDate.getMinutes() + myDate.getSeconds(), {page: this.cur_page, count: this.pagesize, content: this.searchName}).then(res => {
        if (res.code === 0) {
          this.showLoading = false
          if (res.gradePromotion) {
            this.onWayAttendEventFlag = res.onWayAttendEventFlag // 在途商品是否参与活动
            this.goodsList = res.goods // 折扣活动商品
            this.gradePromotionInfo = res.gradePromotion // 折扣活动信息
            if (this.goodsList !== undefined) {
              this.goodsList.forEach((item) => {
                if (item.goodsType === 1) { // 普通商品
                  item.tuType = false // 未勾选包含在途库存
                  item.items.forEach((val) => {
                    val.itemType = item.goodsType
                    val.numInWarehouse = 0 // 在库实际库存
                    val.stockItemCount = 0 // 在库+在途实际库存
                    val.lookNumInWarehouse = '' // 购买的在库库存
                    val.lookStockItemCount = '' // 购买的在途库存
                    val.gradeDiscountId = this.gradePromotionInfo.id
                    this.$api.get(this, 'user/u/warehouse/stock/distributor', {distributorId: this.userId, itemIds: val.id}).then(res => {
                      if (res.code === 0) {
                        this.$forceUpdate() // 更新页面数据
                        if (res.stockItemCounts.length > 0) {
                          val.numInWarehouse = res.stockItemCounts[0].numInWarehouse + res.stockItemCounts[0].numVmi - res.stockItemCounts[0].numLock - res.stockItemCounts[0].numVmiLock - res.stockItemCounts[0].numOnWayLock - res.stockItemCounts[0].numReserved// 在库
                          val.stockItemCount = res.stockItemCounts[0].numInWarehouse + res.stockItemCounts[0].numVmi + res.stockItemCounts[0].numOnWay - res.stockItemCounts[0].numLock - res.stockItemCounts[0].numVmiLock - res.stockItemCounts[0].numOnWayLock - res.stockItemCounts[0].numReserved // 在库+在途
                          if (val.numInWarehouse <= 0) {
                            val.numInWarehouse = 0
                          }
                          if (val.stockItemCount <= 0) {
                            val.stockItemCount = 0
                          }
                        } else {
                          val.numInWarehouse = 0
                          val.stockItemCount = 0
                        }
                      }
                    })
                  })
                }
              })
            }
          }
        } else if (res.code === 3) {
          this.showLoading = false
          
        } else {
          this.showLoading = false
        }
      })
    },
    // 获取栏目商品总数
    getColumnGoodsSum () {
      var myDate = new Date()
      this.$api.get(this, 'user/goods/grade/discount/list/count?' + myDate.getMinutes() + myDate.getSeconds(), {content: this.searchName}).then(res => {
        if (res.code === 0) {
          this.totalCount = res.totalCount
        }
      })
    },
    // 获取栏目商品列表页码改变-syh
    getColumnGoodsListPageChange (page) {
      this.cur_page = page
      window.scrollTo(0, 0)
      this.getColumnGoodsList()
    },
    // 获取栏目商品列表每页显示商品数量改变-syh
    getColumnGoodsListPageSizeChange (size) {
      this.pagesize = size
      this.getColumnGoodsList()
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
        if (this.tabs === 'showImg') {
          this.getColumnGoodsList()()
        } else {
          this.getFastOrderList()
        }
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
        if (this.tabs === 'showImg') {
          this.getColumnGoodsList()()
        } else {
          this.getFastOrderList()
        }
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
        if (this.tabs === 'showImg') {
          this.getColumnGoodsList()()
        } else {
          this.getFastOrderList()
        }
      }
    },
    // 去商品详情
    goDoodsDetail (item, id, goodsType) {
      // this.$router.push({name: 'ShopDetail', query: {good_id: id, activityType: this.activityType, goodsType: goodsType}})
      // console.log(item)
      let routeData = null
      if (item.promotion) {
        routeData = this.$router.resolve({
          name: 'ShopDetail',
          query: {good_id: id, activityType: 1, goodsType: goodsType, accessType: 0}
        })
      } else {
        routeData = this.$router.resolve({
          name: 'ShopDetail',
          query: {good_id: id, goodsType: goodsType}
        })
      }
      window.open(routeData.href, '_blank')
    },
    // 获取快速订货商品列表
    getFastOrderList () {
      var myDate = new Date()
      this.showLoading = true
      this.$api.get(this, 'user/goods/list?' + myDate.getMinutes() + myDate.getSeconds(), {page: this.fastPage, count: this.fastPagesize, columnId: this.columnId, orderField: this.orderField, orderType: this.orderType}).then(res => {
        if (res.code === 0) {
          this.fastOrderGoodsList = res.goodsList
          if (this.fastOrderGoodsList.length === 0) {
            this.showLoading = false
            return false
          }
          this.fastOrderGoodsList.forEach((val, index) => {
            // if (val.goods.goodsType === 1) { // 普通商品
            val.goodsItem = []
            val.tuType = false // 未勾选包含在途库存
            val.showRetailPrice = true // 是否展示零售价
            this.$api.get(this, 'user/goods', {id: val.goods.id}).then(res => {
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
                  if (this.userId === '' || this.userId === null) { // 游客
                    if (index === this.fastOrderGoodsList.length - 1) {
                      this.showLoading = false
                    }
                  } else { // 用户登录
                    this.$api.get(this, 'user/u/warehouse/stock/distributor', {distributorId: this.userId, itemIds: item.id}).then(res => {
                      if (res.code === 0) {
                        if (index === this.fastOrderGoodsList.length - 1) {
                          this.showLoading = false
                        }
                        this.$forceUpdate() // 更新页面数据
                        if (res.stockItemCounts.length > 0) {
                          item.numInWarehouse = res.stockItemCounts[0].numInWarehouse + res.stockItemCounts[0].numVmi - res.stockItemCounts[0].numLock - res.stockItemCounts[0].numVmiLock - res.stockItemCounts[0].numOnWayLock - res.stockItemCounts[0].numReserved// 在库
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
                  }
                })
              }
            })
            // else { // 定制商品
            //   val.customizedItems = []
            //   val.tabType = 0 // 定制商品分组切换
            //   val.showRetailPrice = true // 是否展示零售价
            //   this.$api.get(this, 'user/goods', {id: val.goods.id}).then(res => {
            //     if (res.code === 0) {
            //       if (index === this.fastOrderGoodsList.length - 1) {
            //         this.showLoading = false
            //       }
            //       this.$forceUpdate() // 更新页面数据
            //       val.customizedItems = res.customizedItems
            //     }
            //   })
            // }
          })
        } else if (res.code === 3) {
          this.showLoading = false
          
        }
      })
    },
    getFastOrderSum () {
      var myDate = new Date()
      this.$api.get(this, 'user/goods/count?' + myDate.getMinutes() + myDate.getSeconds(), {columnId: this.columnId}).then(res => {
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
      this.$forceUpdate() // 更新页面数据
      this.tabs = 'showImg'
      this.getColumnGoodsList()
      this.getColumnGoodsSum()
      if (this.userId !== '' && this.userId !== null) { // 游客
        this.isLoginNot()
      }
    },
    toFastOrder () {
      this.$forceUpdate() // 更新页面数据
      this.tabs = 'fastOrder'
      this.getFastOrderList()
      this.getFastOrderSum()
      if (this.userId !== '' && this.userId !== null) { // 游客
        this.isLoginNot()
      }
    },
    // 缺货登记（添加）
    wantBook (goods, type) {
      this.$forceUpdate() // 更新页面数据
      this.stockoutCount = 1
      goods.remark = ''
      goods.type = 1
      if ((Number(this.capitalStatus) === 2 && Number(this.freezeStatus) === 2)) {
      } else if (Number(this.freezeStatus) === 1) {
        if (this.$i18n.locale === 'zh') { this.$message.warning('账号已冻结，无法进行此操作，请联系商务。') } else { this.$message.warning('The account has been frozen and cannot perform this operation. Please contact business personnel.') }
      } else if (Number(this.capitalStatus) === 1) {
        if (this.$i18n.locale === 'zh') { this.$message.warning('账号仍在审核中，无法进行此操作，请等待审核通过。') } else { this.$message.warning('The account fails to be reviewed and cannot perform this operation. Please launch application again') }
      } else if (Number(this.capitalStatus) === 3) {
        if (this.$i18n.locale === 'zh') { this.$message.warning('账号审核未通过，无法进行此操作。请重新发起申请。') } else { this.$message.warning('The account is still being approved and cannot perform this operation. Please launch application again') }
      } else if (Number(this.capitalStatus) === 4) {
        if (this.$i18n.locale === 'zh') { this.$message.warning('账号仍在审批中，无法进行此操作。请重新发起申请。') } else { this.$message.warning('The account is still being reviewed and cannot perform this operation. Please wait for approval.') }
      }
    },
    // 缺货登记(提交)
    stockout (goods) {
      this.onceAgain = false
      if (Number(this.stockoutCount) === 0) {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('缺货商品登记数量不能为0!')
        } else { this.$message.warning('The registered quantity of commodities out of stock cannot be 0!') }
        this.onceAgain = true
        return false
      } else {
        var json = {goodsId: goods.goodsId, itemId: goods.id, specificationName: goods.specificationName, remark: goods.remark, needNumber: Number(this.stockoutCount)}
        this.$api.post(this, 'user/u/losegoods', json).then(res => {
          if (res.code === 0) {
            this.stockoutCount = 0
            if (this.$i18n.locale === 'zh') {
              this.$message.success('缺货商品添加成功!')
            } else { this.$message.success('Add products out of stock successfully!') }
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
      var product = {'gradeDiscountId': target.gradeDiscountId, 'goodsId': Number(target.goodsId), 'itemId': target.id, 'itemType': target.itemType, 'num': target.count}
      var addItem = true
      var addItems = true
      this.joinShopShopcar.forEach((item) => {
        if (item.itemId === target.id) {
          item.num = target.count
          addItem = false
        }
      })
      this.confirmBuyItem.forEach((item) => {
        if (item.id === target.id) {
          item.count = target.count
          addItems = false
        }
      })
      if (addItem === true) {
        this.joinShopShopcar.push(product)
      }
      if (addItems === true) {
        this.confirmBuyItem.push(target)
      }
      console.log(this.confirmBuyItem)
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
    // 判断是否登录状态
    isLoginNot () {
      var myDate = new Date()
      this.$api.get(this, 'user/u/user?' + myDate.getMinutes() + myDate.getSeconds()).then(res => {
        if (res.code === 0) {
        } 
      })
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
            var product = {'gradeDiscountId': obj.gradeDiscountId, 'goodsId': Number(obj.goodsId), 'itemId': obj.itemId, 'itemType': obj.itemType, 'num': Number(obj.num), 'orderType': orderType}
            tempArray.push(product)
          }
        })
        if (tempArray.length <= 0) {
          this.clickAgain = true
          if (this.$i18n.locale === 'zh') {
            this.$message.warning('货品订购数量不能为空')
          } else { this.$message.warning('The order quantity of products cannot be null.') }
          return false
        } else {
          this.$api.post(this, 'user/u/shoppingCarts', {shoppingCars: tempArray}).then(res => {
            if (res.code === 0) {
              this.joinShopShopcar = []
              this.confirmBuyItem = []
              this.clickAgain = true
              if (this.$i18n.locale === 'zh') {
                this.$message.success('加入购物车成功')
              } else { this.$message.success('Add to shopping cart successfully') }
              this.goodsList = []
              this.getColumnGoodsList()
            } else if (res.code === 3) {
              this.clickAgain = true
              
            } else { this.clickAgain = true }
          }).catch(() => { this.clickAgain = true })
        }
      } else if (Number(this.freezeStatus) === 1) {
        if (this.$i18n.locale === 'zh') { this.$message.warning('账号已冻结，无法进行此操作，请联系商务。') } else { this.$message.warning('The account has been frozen and cannot perform this operation. Please contact business personnel.') }
      } else if (Number(this.capitalStatus) === 1) {
        if (this.$i18n.locale === 'zh') { this.$message.warning('账号仍在审核中，无法进行此操作，请等待审核通过。') } else { this.$message.warning('The account fails to be reviewed and cannot perform this operation. Please launch application again') }
      } else if (Number(this.capitalStatus) === 3) {
        if (this.$i18n.locale === 'zh') { this.$message.warning('账号审核未通过，无法进行此操作。请重新发起申请。') } else { this.$message.warning('The account is still being approved and cannot perform this operation. Please launch application again') }
      } else if (Number(this.capitalStatus) === 4) {
        if (this.$i18n.locale === 'zh') { this.$message.warning('账号仍在审批中，无法进行此操作。请重新发起申请。') } else { this.$message.warning('The account is still being reviewed and cannot perform this operation. Please wait for approval.') }
      }
    },
    buyPresell (item) { // 购买预售商品
      var songGoodsShop = []
      var term = []
      var limitPresellPurchase = []
      if (item.count < item.moq) {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('订购数量不可小于' + item.moq)
        } else { this.$message.warning('The order quantity shall not be less than' + item.moq) }
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
          } else { this.$message.success('Add to shopping cart successfully') }
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
        if (this.$i18n.locale === 'zh') { this.$message.warning('账号已冻结，无法进行此操作，请联系商务。') } else { this.$message.warning('The account has been frozen and cannot perform this operation. Please contact business personnel.') }
      } else if (Number(this.capitalStatus) === 1) {
        if (this.$i18n.locale === 'zh') { this.$message.warning('账号仍在审核中，无法进行此操作，请等待审核通过。') } else { this.$message.warning('The account fails to be reviewed and cannot perform this operation. Please launch application again') }
      } else if (Number(this.capitalStatus) === 3) {
        if (this.$i18n.locale === 'zh') { this.$message.warning('账号审核未通过，无法进行此操作。请重新发起申请。') } else { this.$message.warning('The account is still being approved and cannot perform this operation. Please launch application again') }
      } else if (Number(this.capitalStatus) === 4) {
        if (this.$i18n.locale === 'zh') { this.$message.warning('账号仍在审批中，无法进行此操作。请重新发起申请。') } else { this.$message.warning('The account is still being reviewed and cannot perform this operation. Please wait for approval.') }
      }
    },
    // 登录验证
    checkLogin () {
      if (this.userId === null || this.userId === '') {
        return false
      } else if (Number(this.capitalStatus) === 2 && Number(this.freezeStatus) === 2) {
        return true
      } else if (Number(this.freezeStatus) === 1) {
        if (this.$i18n.locale === 'zh') { this.$message.warning('账号已冻结，无法进行此操作，请联系商务。') } else { this.$message.warning('The account has been frozen and cannot perform this operation. Please contact business personnel.') }
      } else if (Number(this.capitalStatus) === 1) {
        if (this.$i18n.locale === 'zh') { this.$message.warning('账号仍在审核中，无法进行此操作，请等待审核通过。') } else { this.$message.warning('The account fails to be reviewed and cannot perform this operation. Please launch application again') }
      } else if (Number(this.capitalStatus) === 3) {
        if (this.$i18n.locale === 'zh') { this.$message.warning('账号审核未通过，无法进行此操作。请重新发起申请。') } else { this.$message.warning('The account is still being approved and cannot perform this operation. Please launch application again') }
      } else if (Number(this.capitalStatus) === 4) {
        if (this.$i18n.locale === 'zh') { this.$message.warning('账号仍在审批中，无法进行此操作。请重新发起申请。') } else { this.$message.warning('The account is still being reviewed and cannot perform this operation. Please wait for approval.') }
      }
    },
    // 立即购买
    buyNow () {
      console.log(this.goodsList)
      if (this.checkLogin()) {
        var tempArray = [] // 临时数组
        var songGoodsShop = []
        this.confirmBuyItem.forEach((obj) => {
          if (obj.count !== undefined && Number(obj.count) !== 0) {
            if (Number(obj.count) > obj.numInWarehouse) {
              obj.lookNumInWarehouse = obj.numInWarehouse
              obj.lookStockItemCount = Number(obj.count) - obj.numInWarehouse
            } else {
              obj.lookNumInWarehouse = Number(obj.count)
              obj.lookStockItemCount = 0
            }
            obj.num = obj.count
            tempArray.push(obj)
          }
        })
        if (tempArray.length === 0) {
          if (this.$i18n.locale === 'zh') {
            this.$message.warning('货品订购数量不能为空')
          } else { this.$message.warning('The order quantity of products cannot be null.') }
          return false
        } else {
          window.localStorage.setItem('songGoodsShop', JSON.stringify(songGoodsShop)) // 赠品空数组
          window.localStorage.setItem('goodsItems', JSON.stringify(tempArray))
          this.$router.push({name: 'ConsigneeInfor', query: {values: 0, goodsType: 1}})
        }
      }
    }
  },
  created () {
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem('bLang') ? window.localStorage.getItem('bLang') : 'zh-RMB';
    var one = window.localStorage.getItem('userId')
    var state = window.localStorage.getItem('capitalStatus')
    var fstate = window.localStorage.getItem('freezeStatus')
    this.userId = one
    this.capitalStatus = state
    this.freezeStatus = fstate
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
    if (this.userId !== '' && this.userId !== null) { // 游客
      this.isLoginNot()
    }
    this.getColumnGoodsList()
    this.getColumnGoodsSum()
  }
}
</script>

<style scoped="scoped" lang='less'>
  .logo-box{
    .input-box{
      width: 330px;
      height: 30px;
      input{
        width: 300px;
        height: 30px;
        line-height: 30px;
        background: url(../../src/assets/images/search.png) no-repeat 5px center;
      }
    }
    .search{
      display: block;
      width: 76px;
      height: 30px;
      line-height: 30px;
      background-color: #0392a6;
    }
  }
  .index{
    width: 100%;
  }
  .dingzhi-btn{
    margin: 0 auto;
    min-width: 40px;
    height: 30px;
    line-height: 30px;
    border: 1px solid #ccc;
    border-radius: 5px;
    font-size: 12px;
    cursor: pointer;
  }
  .join-ball-img{
    position: fixed;
    bottom: 5%;
    right: 19%;
    width: 93px;
    height: 93px;
    border-radius: 50%;
    background-color: #fff;
    z-index: 1;
    box-shadow:0px 0px 16px 0px rgba(0, 0, 0, 0.16);
    .ball-img{
      .ball-img-top{
        height:46px;
        width: 93px;
        border-radius: 93px 93px 0 0;
      }
      .ball-img-bottom{
        height:46px;
        width: 93px;
        border-radius: 0 0 93px 93px;
        background-color: #888888;
      }
    }
  }
  .main{
    width: 1190px;
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
        margin-left: 50px;
        margin-bottom: 20px;
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
    }
    .classify-list{
      margin-top: 25px;
      ul{
        li{
          margin-bottom: 20px;
          float: left;
          margin-left: 21px;
          cursor: pointer;
          transition: all .2s linear;
          .content{
            padding-bottom: 10px;
            width: 219px;
            background-color: #fff;
            border: 1px solid #f5f5f5;
            .img{
              width: 219px;
              height: 219px;
              img{height: 219px}
            }
            .brand{
              // width: 229px;
              height: 21px;
              margin-left: 6px;
              margin-right: 6px;
              overflow: hidden;
              white-space: nowrap;
              text-overflow: ellipsis;
            }
            .word{
              padding-left: 10px;
              margin-right: 10px;
              font-size: 12px;
              height: 17px;
              overflow: hidden; 
              white-space: nowrap;
              text-overflow: ellipsis;
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
        padding-left: 5px;
        padding-right: 5px;
        min-width:39px;
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
          max-width: 98px;
          height: 30px;
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
  /*syh-20180606*/
  .rl-bg-orange{
    background-color: #FF7900;
  }
  .join-ball-img{
    z-index: 99;
  }
  .rl-text-success-sm{
    color: #259B24;
  }
</style>
