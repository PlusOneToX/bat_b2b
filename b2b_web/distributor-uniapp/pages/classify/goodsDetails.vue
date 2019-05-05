<template>
  <view class="goodsDetails">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>商品详情</view>
      </view>
    </view>

    <!-- top -->
    <view class="gd-top">
      <swiper
        class="swiper"
        :indicator-dots="indicatorDots"
        :autoplay="autoplay"
        :interval="interval"
        circular
      >
        <swiper-item>
          <image
            :src="detailsObj.imageUrl1"
            class="goodsImg"
            mode="aspectFit"
          ></image>
        </swiper-item>
        <swiper-item v-if="detailsObj.imageUrl2 && detailsObj.imageUrl2 != ''">
          <image
            :src="detailsObj.imageUrl2"
            class="goodsImg"
            mode="aspectFit"
          ></image>
        </swiper-item>
        <swiper-item v-if="detailsObj.imageUrl3 && detailsObj.imageUrl3 != ''">
          <image
            :src="detailsObj.imageUrl3"
            class="goodsImg"
            mode="aspectFit"
          ></image>
        </swiper-item>
        <swiper-item v-if="detailsObj.imageUrl4 && detailsObj.imageUrl4 != ''">
          <image
            :src="detailsObj.imageUrl4"
            class="goodsImg"
            mode="aspectFit"
          ></image>
        </swiper-item>
        <swiper-item v-if="detailsObj.imageUrl5 && detailsObj.imageUrl5 != ''">
          <image
            :src="detailsObj.imageUrl5"
            class="goodsImg"
            mode="aspectFit"
          ></image>
        </swiper-item>
        <swiper-item v-if="detailsObj.imageUrl6 && detailsObj.imageUrl6 != ''">
          <image
            :src="detailsObj.imageUrl6"
            class="goodsImg"
            mode="aspectFit"
          ></image>
        </swiper-item>
      </swiper>
      <!-- 无阶梯价 -->
      <view
        class="gd-topInfrom"
        :class="{
          'padding-bottom': detailsObj.tags && detailsObj.tags.length <= 0,
        }"
        v-if="rulesList.length == 0"
      >
        <view class="gd-topName">{{ detailsObj.goodsName }}</view>
        <view
          class="gd-topLabel"
          v-if="detailsObj.tags && detailsObj.tags.length > 0"
        >
          <view class="label-module">
            <text
              class="orangeLabel"
              v-for="(item, index) in detailsObj.tags"
              :key="index"
              >{{ item.name }}</text
            >
            <!-- <text class="redLabel">标签1</text>
						<text class="greenLabel">标签1</text> -->
          </view>
          <!-- <view class="gd-topShare" @click="shareFun"><text class="iconfont icon-share"></text></view> -->
        </view>
        <view class="gd-top-btm">
          <template v-if="userId != ''">
            <view class="gd-topPrice" v-if="detailsObj.promotionType == 0"
              ><text>￥</text
              >{{
                detailsObj.minPrice
                  ? detailsObj.minPrice
                  : detailsObj.maxPrice
                  ? detailsObj.maxPrice
                  : "暂未定价"
              }}
            </view>
            <view class="gd-topPrice" v-else
              ><text>{{
                detailsObj.promotionMinPrice ? "￥" : "暂未定价"
              }}</text
              >{{
                detailsObj.promotionMinPrice ? detailsObj.promotionMinPrice : ""
              }}
            </view>
          </template>
          <template v-else>
            <view class="gd-topPrice"><text>登录后查看</text> </view>
          </template>
        </view>

        <view
          class="gd-topShare"
          v-if="!isH5"
          :style="{ background: themeColor }"
        >
          <button
            open-type="share"
            v-bind:data-student="detailsObj"
            class="share"
          >
            <image src="../../static/imgs/shareICon.png"></image>
          </button>
        </view>

        <view class="gd-num">
          <view v-if="detailsObj.saleNum > 10000" class="gd-topNum"
            >已售 {{ (detailsObj.saleNum / 10000).toFixed(2) }}万+</view
          >
          <view
            v-else-if="
              detailsObj.saleNum == null ||
              detailsObj.saleNum == undefined ||
              detailsObj.saleNum == ''
            "
            class="gd-topNum"
            >已售 0</view
          >
          <view v-else class="gd-topNum">已售{{ detailsObj.saleNum }}</view>
        </view>
      </view>
      <!-- 有阶梯价 -->
      <view class="gd-topInfrom2" v-if="userId != '' && rulesList.length > 0">
        <!-- 拼团-start  -->
        <template v-for="(item, index) in groupGoodList">
          <view class="gd-topTime" :key="index">
            <view>
              <text>{{ item.name }}</text>
              <!-- <text v-if="item.groupSeckillType==1">拼团抢新</text>
					  <text v-if="item.groupSeckillType==2">限时抢新</text> -->
              <text
                class="iconfont icon-Packup gd-topTimeIcon"
                @click="openPromotionPopup"
              ></text>
            </view>
            <text class="gd-xiexian"></text>
            <text class="gd-tims">{{ promotionTime }}</text>
          </view>
          <view class="gd-topSpell" :key="index">
            <view class="gd-topSpell-lf">
              <view class="gd-topSpell-price">
                <view
                  ><text
                    >{{
                      item.groupSeckillType == 1 ? "拼团价" : "秒杀价"
                    }}：</text
                  ><text>￥</text><text>{{ item.groupSeckillPrice }}</text>
                </view>
                <text class="through-text">￥{{ detailsObj.minPrice }}</text>
              </view>
              <view class="gd-topSpell-num">
                <text v-if="item.groupSeckillType == 1"
                  >起拼量：{{ item.minNum }}</text
                >
                <text>限购量：{{ item.maxNum }}</text>
              </view>
            </view>
            <view class="gd-topSpell-residueNum" v-if="item.maxNum > 0"
              >剩余{{ item.itemSurplusNumLast }}件</view
            >
          </view>
        </template>
        <!-- 拼团-end  -->

        <!-- 活动规格列表 -->
        <view
          class="gd-topTime"
          v-if="ladderPriceList.length > 0 || promotionsList.length > 0"
          style="margin-bottom: 20rpx"
        >
          <view @click="openPromotionPopup">
            <text>{{ ruleName }}</text>
            <text class="iconfont icon-Packup gd-topTimeIcon"></text>
          </view>
          <text class="gd-xiexian"></text>
          <text class="gd-tims">{{ promotionTime }}</text>
        </view>

        <!-- 阶梯价 -->
        <view class="gd-topPriceList" v-if="ladderPriceList.length > 0">
          <view
            class="gd-topPriceList-line"
            v-for="(item, index) in ladderPriceList"
            :key="index"
          >
            <view class="gd-topPrice"><text>￥</text>{{ item.price }}</view>
            <view class="gd-topNum" v-if="index < ladderPriceList.length - 1"
              >{{ item.oneBuyCount }}-{{
                ladderPriceList[index + 1].oneBuyCount - 1
              }}件</view
            >
            <view class="gd-topNum" v-else>{{ item.oneBuyCount }}+件</view>
          </view>
        </view>

        <view class="gd-topPrice" v-if="detailsObj.promotionType == 0"
          ><text>￥</text
          >{{ detailsObj.minPrice ? detailsObj.minPrice : detailsObj.maxPrice }}
        </view>
        <view
          class="gd-topPrice2"
          v-else-if="ladderPriceList.length == 0 && groupGoodList.length == 0"
        >
          <view
            >活动价：<text>￥</text
            ><text>{{
              goodsInfo.promotionPrice ? goodsInfo.promotionPrice : ""
            }}</text></view
          >
          <view class="through-text"
            >￥{{
              goodsInfo.salePrice ? goodsInfo.salePrice : "暂未定价"
            }}</view
          >
        </view>
        <view
          class="gd-topLabel"
          :class="{ 'no-label': !detailsObj.goodsName }"
        >
          <view class="gd-topName2">{{ detailsObj.goodsName }}</view>
          <view
            class="gd-topShare"
            v-if="!isH5"
            :style="{ background: themeColor }"
          >
            <button
              open-type="share"
              v-bind:data-student="detailsObj"
              class="share"
            >
              <image src="../../static/imgs/shareICon.png"></image>
            </button>
          </view>
        </view>
        <view class="gd-top-btm">
          <view class="label-module">
            <text
              class="orangeLabel"
              v-for="item in detailsObj.tags"
              :key="item.id"
              >{{ item.name }}</text
            >
            <!-- <text class="redLabel">标签1</text>
					<text class="greenLabel">标签1</text> -->
          </view>
          <view v-if="detailsObj.saleNum > 10000" class="gd-topNum"
            >已售 {{ (detailsObj.saleNum / 10000).toFixed(2) }}万+</view
          >
          <view
            v-else-if="
              detailsObj.saleNum == null ||
              detailsObj.saleNum == undefined ||
              detailsObj.saleNum == ''
            "
            class="gd-topNum"
            >已售 0</view
          >
          <view v-else class="gd-topNum">已售{{ detailsObj.saleNum }}</view>
        </view>
      </view>
    </view>

    <!-- 已为您选择赠品数量 -->
    <view class="gift-num-wrap" v-if="presentList.length > 0 && giftNum > 0">
      <view class="gift-num" @click="openGiftsPopup()">
        <text>活动</text>
        <text class="gift-label">【赠品】</text>
        <text>已为您优选 {{ giftNum }} 个赠品</text>
        <image
          src="../../static/imgs/personalMore.png"
          class="arrow-right"
        ></image>
      </view>
    </view>

    <!-- buy-goodList -->
    <view class="gd-buyGood">
      <view class="gd-buyGood-tip">
        <view class="gd-bG-tipTitle">省心购</view>
        <view class="gd-bG-tipLabel" :style="{ color: themeColor }">
          <view><text class="sxIcon plIcon"></text><text>品类齐全</text></view>
          <view><text class="sxIcon pzIcon"></text><text>品质保证</text></view>
          <view><text class="sxIcon shIcon"></text><text>售后服务</text></view>
        </view>
      </view>
      <view class="gd-buyGood-numLine" v-if="goodsInfo.retailPrice"
        ><text>建议零售价</text><text>￥{{ goodsInfo.retailPrice }}</text></view
      >
      <view class="gd-buyGood-numLine"
        ><text>商品编码</text><text>{{ goodsInfo.itemCode }}</text></view
      >
      <view class="gd-buyGood-numLine">
        <view v-if="goodsInfo.boxNum"
          >购买单位：{{ goodsInfo.boxNum }}/{{ goodsInfo.boxType }}
        </view>
      </view>
      <view
        class="gd-buyGood-zaitu"
        v-if="onWayFlag == 1 && detailsObj.diyType !== 1"
        @click="choiceTutype()"
      >
        <text
          class="iconfont"
          :class="
            isInTransit
              ? 'tuTypeHover icon-icon_selected'
              : 'icon-icon_selected_def'
          "
        ></text>
        <text>包含在途库存</text>
      </view>
      <view class="gd-buyGood-list">
        <!-- v-for="(item,index) in goodsItems.slice(0,showLength)" :key="item.id" -->
        <view v-for="(item, index) in goodsItems" :key="item.id">
          <view
            class="gd-buyGood-line"
            :class="goodsItemsIndex == index ? 'categoryList-Hover' : ''"
            @click="goodsItemsClick(index, item)"
            v-if="index < 3"
          >
            <image
              :src="item.itemImg"
              @error="imageError($event, index, i)"
              mode="aspectFit"
            ></image>
            <view class="gd-buyGood-lineRg">
              <view class="gd-buyGood-lineCenter">
                <view>颜色：{{ item.colorName ? item.colorName : "" }}</view>
                <view v-if="item.specsName && item.specsName != ''"
                  >规格：{{ item.specsName }}</view
                >
                <view class="gd-buyGood-linePrice" v-if="userId != ''"
                  ><text>￥</text
                  >{{ item.salePrice ? item.salePrice : "暂未定价" }}</view
                >
                <view class="gd-buyGood-linePrice" v-else
                  ><text>登录后查看</text></view
                >
              </view>
              <view
                class="gd-buyGood-lineNum"
                v-if="userId != '' && detailsObj.diyType !== 1"
              >
                <!-- :value="item.buyNum" -->
                <!-- <view @click.stop >
									<uni-number-box @change="numberChange($event,item)" :min="0"
									:max="(isInTransit?(item.onWayUsableCount+item.inStockUsableCount):item.inStockUsableCount)" >
									</uni-number-box>
								</view> -->
                <view class="self-numberBox2">
                  <text @click="numberChange(item.itemCount, item, 1)">-</text>
                  <input
                    v-model="item.buyNum"
                    @blur="numberChange(item.itemCount, item, 3)"
                  />
                  <text @click="numberChange(item.itemCount, item, 2)">+</text>
                </view>
                <template v-if="!isInTransit">
                  <view class="gd-buyGood-lineKucun" v-if="stockShowFlag == 0"
                    >库存：{{ item.inStockUsableCount }}</view
                  >
                  <view
                    class="gd-buyGood-lineKucun"
                    v-else-if="
                      stockShowFlag == 1 &&
                      stockShowNumber < item.inStockUsableCount
                    "
                    >库存：{{ item.inStockUsableCount }}</view
                  >
                  <view
                    class="gd-buyGood-lineKucun"
                    v-else-if="
                      stockShowFlag == 1 && item.inStockUsableCount == 0
                    "
                    >库存：无货</view
                  >
                  <view
                    class="gd-buyGood-lineKucun"
                    v-else-if="
                      stockShowFlag == 1 &&
                      0 < item.inStockUsableCount <= stockShowNumber
                    "
                    >库存紧张</view
                  >
                </template>
                <template v-if="isInTransit">
                  <view class="gd-buyGood-lineKucun" v-if="stockShowFlag == 0"
                    >库存：{{
                      item.onWayUsableCount + item.inStockUsableCount
                    }}</view
                  >
                  <view
                    class="gd-buyGood-lineKucun"
                    v-else-if="
                      stockShowFlag === 1 &&
                      stockShowNumber <
                        item.onWayUsableCount + item.inStockUsableCount
                    "
                    >库存：
                    {{ item.onWayUsableCount + item.inStockUsableCount }}</view
                  >
                  <view
                    class="gd-buyGood-lineKucun"
                    v-else-if="
                      stockShowFlag === 1 &&
                      item.onWayUsableCount + item.inStockUsableCount === 0
                    "
                    >无货</view
                  >
                  <view
                    class="gd-buyGood-lineKucun"
                    v-else-if="
                      stockShowFlag === 1 &&
                      0 <
                        item.onWayUsableCount + item.inStockUsableCount <=
                        stockShowNumber
                    "
                    >库存紧张</view
                  >
                </template>
              </view>
            </view>
            <view
              v-if="item.rulesList && item.rulesList.length > 0"
              class="gd-buyGood-activityTip"
              >活动</view
            >
          </view>
          <view
            class="gd-buyGood-line"
            :class="goodsItemsIndex == index ? 'categoryList-Hover' : ''"
            @click="goodsItemsClick(index, item)"
            v-if="index > 2 && showLength > 3"
          >
            <image :src="item.itemImg"></image>
            <view class="gd-buyGood-lineRg">
              <view class="gd-buyGood-lineCenter">
                <view>颜色：{{ item.colorName ? item.colorName : "" }}</view>
                <view v-if="item.specsName && item.specsName != ''"
                  >规格：{{ item.specsName }}</view
                >
                <view class="gd-buyGood-linePrice" v-if="userId != ''"
                  ><text>￥</text
                  >{{ item.salePrice ? item.salePrice : "暂未定价" }}</view
                >
                <view class="gd-buyGood-linePrice" v-else
                  ><text>登录后查看</text></view
                >
              </view>
              <view
                class="gd-buyGood-lineNum"
                v-if="userId != '' && detailsObj.diyType !== 1"
              >
                <!-- :value="item.buyNum" -->
                <!-- <view @click.stop >
									<uni-number-box @change="numberChange($event,item)" :min="0"
									:max="(isInTransit?(item.onWayUsableCount+item.inStockUsableCount):item.inStockUsableCount)" >
									</uni-number-box>
								</view> -->
                <view class="self-numberBox2">
                  <text @click="numberChange(item.itemCount, item, 1)">-</text>
                  <input
                    v-model="item.buyNum"
                    @blur="numberChange(item.itemCount, item, 3)"
                  />
                  <text @click="numberChange(item.itemCount, item, 2)">+</text>
                </view>
                <template v-if="!isInTransit">
                  <view class="gd-buyGood-lineKucun" v-if="stockShowFlag == 0"
                    >库存：{{ item.inStockUsableCount }}</view
                  >
                  <view
                    class="gd-buyGood-lineKucun"
                    v-else-if="
                      stockShowFlag == 1 &&
                      stockShowNumber < item.inStockUsableCount
                    "
                    >库存：{{ item.inStockUsableCount }}</view
                  >
                  <view
                    class="gd-buyGood-lineKucun"
                    v-else-if="
                      stockShowFlag == 1 && item.inStockUsableCount == 0
                    "
                    >库存：无货</view
                  >
                  <view
                    class="gd-buyGood-lineKucun"
                    v-else-if="
                      stockShowFlag == 1 &&
                      0 < item.inStockUsableCount <= stockShowNumber
                    "
                    >库存紧张</view
                  >
                </template>
                <template v-if="isInTransit">
                  <view class="gd-buyGood-lineKucun" v-if="stockShowFlag == 0"
                    >库存：{{
                      item.onWayUsableCount + item.inStockUsableCount
                    }}</view
                  >
                  <view
                    class="gd-buyGood-lineKucun"
                    v-else-if="
                      stockShowFlag === 1 &&
                      stockShowNumber <
                        item.onWayUsableCount + item.inStockUsableCount
                    "
                    >库存：
                    {{ item.onWayUsableCount + item.inStockUsableCount }}</view
                  >
                  <view
                    class="gd-buyGood-lineKucun"
                    v-else-if="
                      stockShowFlag === 1 &&
                      item.onWayUsableCount + item.inStockUsableCount === 0
                    "
                    >无货</view
                  >
                  <view
                    class="gd-buyGood-lineKucun"
                    v-else-if="
                      stockShowFlag === 1 &&
                      0 <
                        item.onWayUsableCount + item.inStockUsableCount <=
                        stockShowNumber
                    "
                    >库存紧张</view
                  >
                </template>
              </view>
            </view>
            <view
              v-if="item.rulesList && item.rulesList.length > 0"
              class="gd-buyGood-activityTip"
              >活动</view
            >
          </view>
        </view>
        <view class="gd-buyGood-ShowM" v-if="goodsItems.length > 3">
          <image
            src="../../static/imgs/up.png"
            @click="showMoreList(1)"
            v-if="showLength == 3"
          ></image>
          <image
            src="../../static/imgs/down.png"
            @click="showMoreList(2)"
            v-if="showLength > 3"
          ></image>
        </view>
      </view>
    </view>

    <view class="gd-parameter">
      <view
        class="gd-parameter-view"
        :class="
          detailsObj.params && detailsObj.params.length <= 0 ? 'only' : ''
        "
      >
        <view
          ><text>品牌</text><text>{{ detailsObj.brandName }}</text></view
        >
        <view v-for="(item, index) in detailsObj.params" :key="index"
          ><text>{{ item.paramName }}</text
          ><text>{{ item.paramValueName }}</text></view
        >

        <!--  <view><text>材质</text><text>TPU</text></view>
			   <view><text>机型</text><text>IphoneX</text></view> -->
      </view>
      <!-- <image src="../../static/img/auditTo_icon.png" class="toIcon" @click="openPropertyPopup"></image> -->
    </view>
    <view class="gd-detailsImg" v-html="datailContent" v-if="isH5"></view>
    <view class="gd-detailsImg" v-if="!isH5">
      <Parser :html="datailContent"></Parser
    ></view>

    <view class="gd-text">
      <view class="gd-text-title">配送说明</view>
      <view class="gd-text-content"
        >1.
        商品发货后，通常预计2-5个工作日到货（受地域远近、相关管制等各因素影响，时效可能存在迟延，请随时关注后台物流更新轨迹并以此为准）</view
      >
      <view class="gd-text-content"
        >2.
        请您于签收时开箱查验确认内物商品完好，若出现问题，尽可能完整拍摄外箱及面单、箱内清单及实物问题照片或视频，联系商务为您解决。
      </view>
      <view class="gd-text-content"
        >更多内容您可查看平台公示的相关规则、服务帮助，或联系全国统一客服电话0755-21011282</view
      >
      <view class="gd-text-title">价格说明</view>
      <view class="gd-text-content"
        >1.建议零售价：由品牌供应商提供的正品零售价（如厂商指导价、建议零售价等）或该商品曾经展示过的销售价；由于地区、时间的差异性和市场行情的波动，品牌专柜标价、商品吊牌价等可能会与您下单时展示的不一致，该价格仅供您参考。</view
      >
      <view class="gd-text-content"
        >2.会员价：如无特殊说明，会员价是您专属的价格。如有疑问，您可以与商务联系。</view
      >
      <view class="gd-text-content"
        >3.阶梯价：商品促销时，会根据您下单的数量给予一定的优惠。</view
      >
      <view class="gd-text-content"
        >4.价格异常：因可能存在系统缓存、页面更新延迟等不确定性情况，导致价格显示异常，商品具体售价请以订单结算页价格为准。如您发现异常情况出现，请立即联系我们补正，以便您能顺利购物。</view
      >
    </view>

    <!-- 底部购买 -->
    <view class="gd-btmBtn" v-if="userId != ''">
      <view class="gd-btmBtn-icon" @click="toHomePage"
        ><text class="iconfont icon-home1"></text><text>首页</text></view
      >
      <view
        class="gd-btmBtn-icon"
        :class="isCollect ? 'dg-IconFont-hover' : ''"
        @click="collectClickFun(isCollect ? 'DELETE' : 'POST')"
      >
        <text
          :class="isCollect ? 'iconfont icon-icon3' : 'iconfont icon-collect'"
        ></text
        ><text>收藏</text>
      </view>
      <view class="gd-btmBtn-icon" @click="toShoppingCart"
        ><text class="iconfont icon-cart"></text><text>购物车</text></view
      >
      <view class="gd-btmBtn-btn" v-if="detailsObj.diyType !== 1">
        <text
          @click="addShopCart"
          :style="{ color: themeColor, border: '1rpx solid' + themeColor }"
          >加入购物车</text
        >
        <text @click="buyClick" :style="{ background: themeColor }"
          >立即购买</text
        >
      </view>
      <view class="gd-custom-btn" v-else>
        <text @click="goCustom">进入定制</text>
      </view>
    </view>

    <!--活动弹框-liu -->
    <uni-popup ref="activityPopup" type="bottom" class="myCollect-popup">
      <view class="myCollect-popup-content property-popup">
        <view class="myCollect-popup-title">
          <text>请选择活动</text>
          <image
            src="../../static/imgs/icon_close.png"
            class="popup-colose"
            @click="closePromitionPopup"
          ></image>
        </view>

        <view class="gd-property">
          <view
            class="gd-property-line2"
            v-for="(item, index) in rulesList"
            :key="index"
          >
            <text
              class="iconfont"
              :class="
                rulesIndex == index
                  ? 'font-colorHoverL icon-Checkthe'
                  : 'icon-uncheck'
              "
              @click="choicePromition(item, index)"
            ></text>
            <text :class="rulesIndex == index ? 'font-colorHoverL' : ''">{{
              item.ruleName
            }}</text>
          </view>
        </view>
      </view>
    </uni-popup>

    <!--产品属性弹框 -->
    <uni-popup ref="propertyPopup" type="bottom" class="myCollect-popup">
      <view class="myCollect-popup-content property-popup">
        <view class="myCollect-popup-title">
          <text>产品属性</text>
          <image
            src="../../static/imgs/icon_close.png"
            class="popup-colose"
            @click="closePropertyPopup"
          ></image>
        </view>

        <view class="gd-property">
          <!-- <view class="gd-property-line"><text>属性名</text><text>属性值</text></view> -->
          <view
            v-for="(item, index) in detailsObj.params"
            class="gd-property-line"
            :key="index"
            ><text>{{ item.paramName }}</text
            ><text>{{ item.paramValueName }}</text></view
          >
        </view>
      </view>
    </uni-popup>

    <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>

    <!--选择赠品 弹框 -y-->
    <uni-popup
      ref="giftsPopup"
      type="bottom"
      class="myCollect-popup gift-popup"
    >
      <view class="sc-aCivityPopup-content">
        <view class="myCollect-popup-title"
          ><text>请选择赠品</text
          ><image
            src="../../static/imgs/icon_close.png"
            class="popup-colose"
            @click="closeGiftsPopup"
          ></image
        ></view>
        <scroll-view scroll-y="true" class="myCollect-scroll-Y">
          <view
            class="popup-giftsList"
            v-for="(present, index) in presentList"
            :key="index"
          >
            <view class="popup-giftsList-activity">
              <text
                class="label"
                :style="{ 'background-color': themeColor }"
              ></text>
              按照活动规则最多可领取
              <text class="num">{{ present.presentCount }}</text>
              件赠品，已选
              <text class="num">{{ present.count }}</text>
              件
            </view>
            <view
              class="popup-giftsList-line"
              v-for="item in present.presents"
              :key="item.id"
            >
              <image
                :src="item.itemImg ? item.itemImg : item.imageUrl1"
              ></image>
              <view class="giftsList-line-rg">
                <view class="popup-giftsList-top">
                  <view class="popup-giftsList-name">{{ item.itemName }}</view>
                  <view class="self-numberBox2">
                    <text @click="giftHandleChange(item, present, 1)">-</text>
                    <input
                      type="number"
                      v-model="item.num"
                      min="0"
                      :max="item.kucunCount"
                      @blur="giftHandleChange(item, present, 3)"
                      onkeyup="this.value=this.value.replace(/\D/g,'')"
                    />
                    <text @click="giftHandleChange(item, present, 2)">+</text>
                  </view>
                </view>
                <view class="popup-giftsList-num"
                  ><text>编码：{{ item.itemCode }}</text
                  ><text>库存：{{ item.kucunCount }}</text></view
                >
              </view>
            </view>
          </view>
        </scroll-view>

        <view class="popup-giftsList-btm">
          <!-- <view class="popup-giftsList-total"
            >最多可领取<text>{{ presentCount }}</text
            >件，已选<text>{{ selectSongCount }}</text
            >件</view
          > -->
          <view
            class="popup-giftsList-btn"
            :style="{ 'background-color': themeColor }"
            @click="confirmGiftShop"
            >确定</view
          >
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<script>
import {
  goodsDetails,
  userShopSetting,
  listStockByCondition,
  goodsCollection,
  priceGoodsList,
  priceItemList,
  promotiongroupseckill,
  addShoppingcart,
} from "../../common/api.js";
import { Parser } from "../../components/jyf-Parser";
import { isH5, isMpWeixin } from "../../common/common.js";
export default {
  components: {
    Parser,
  },
  data() {
    return {
      themeColor: "",
      indicatorDots: true,
      autoplay: true,
      interval: 2000,
      showLength: 3,
      groupGoodList: [], //单个货品拼团活动列表
      isLadderPrice: false, //是否有阶梯价
      ladderPriceList: [], //阶梯价列表
      promotionsList: [], //普通活动列表
      specialsList: [], //特价货品列表
      rulesList: [], //合并后所有活动列表
      ladderActivityList: [], //阶梯活动列表
      activityList: [], //活动列表
      promotionTime: "", //活动倒计时
      promotionName: "", //活动名称
      ruleName: "", //活动名称
      rulesIndex: 0,
      intervals: null, // 倒计时

      detailId: "",
      detailsObj: {}, //详情
      goodsItems: [], //详情列表
      isInTransit: false, //是否包含在途
      onWayFlag: null, //是否可包含在途库存

      stockShowFlag: 0, // 0 实际库存 1模糊库存
      stockShowNumber: 0, // 库存临界值
      totalNum: 0, //共几件
      totalPrice: 0, //共计金额
      datailContent: "",
      isCollect: false, //是否收藏
      goodsItemsIndex: 0,
      goodsInfo: {},
      //
      spellIndex: 0, //选择的活动index
      spellInterval: null, //倒计时
      spellTime: "", //拼团秒杀时间
      isH5: false,
      userId: "",
      tipTextShow: false,
      tipText: "",

      // 赠品
      presentBuyGoodsList: [], // 已选赠品列表
      giftNum: 0, // 已为您选择赠品数量
      presentCount: 0, // 赠品的最大可选数量
      selectSongCount: 0, // 赠品的已选数量
      presentList: [], // 赠品列表
      goodsType: 1, // 商品类型：1 普通， 2 定制
    };
  },
  onLoad(option) {
    this.themeColor = uni.getStorageSync("themeColor");
    let userId = uni.getStorageSync("userId");
    if (userId && userId != "" && userId != "undefined") {
      this.userId = userId;
    }
    this.detailId = option.id;
    this.getDetails();
    if (this.userId != "") {
      this.isCollectFun();
    }

    this.isH5 = isH5;
  },
  onShow() {
    this.getDetails();
  },
  methods: {
    // 轻提示弹框
    tipFun(text) {
      let that = this;
      this.tipText = text;
      this.tipTextShow = true;
      setTimeout(function () {
        that.tipTextShow = false;
      }, 3000);
    },
    //返回
    toback() {
      uni.navigateBack({
        delta: 1,
      });
    },
    //
    imageError(e, index, i) {
      this.goodsItems[index]["itemImg"] = "../../static/api.png";
    },
    // 处理html图片宽度的js
    formatRichText(html) {
      let newContent = html.replace(/<img[^>]*>/gi, function (match, capture) {
        match = match
          .replace(/style="[^"]+"/gi, "")
          .replace(/style='[^']+'/gi, "");
        match = match
          .replace(/width="[^"]+"/gi, "")
          .replace(/width='[^']+'/gi, "");
        match = match
          .replace(/height="[^"]+"/gi, "")
          .replace(/height='[^']+'/gi, "");
        return match;
      });
      newContent = newContent.replace(
        /style="[^"]+"/gi,
        function (match, capture) {
          match = match
            .replace(/width:[^;]+;/gi, "max-width:100%;")
            .replace(/width:[^;]+;/gi, "max-width:100%;");
          return match;
        }
      );
      newContent = newContent.replace(/<br[^>]*\/>/gi, "");
      newContent = newContent.replace(
        /\<img/gi,
        '<img style="max-width:100%;height:auto;display:block;margin-top:0;margin-bottom:0;"'
      );
      return newContent;
    },

    // 获取详情数据
    getDetails() {
      let that = this;
      let id = this.detailId;

      goodsDetails({ id: id }).then((res) => {
        let detais = res.data;
        that.detailsObj = res.data;
        this.$set(that.detailsObj, "minPrice", 0);
        this.$set(that.detailsObj, "maxPrice", 0);

        this.goodsType = res.data.goodsType;

        let goodsIdList = [];
        let twoGoodsIdList = [];
        that.goodsInfo = detais.goodsItems[0];
        let goodsItems = detais.goodsItems;
        for (let i = 0; i < goodsItems.length; i++) {
          that.$set(goodsItems[i], "buyNum", 0);
          that.$set(goodsItems[i], "itemCount", 0);
          that.$set(goodsItems[i], "itemId", goodsItems[i].id);
          that.$set(goodsItems[i], "imageUrl", goodsItems[i].itemImg);
          that.$set(goodsItems[i], "diyType", detais.diyType);
          that.$set(goodsItems[i], "goodsId", detais.id);
          that.$set(goodsItems[i], "goodsName", detais.goodsName);
          that.$set(goodsItems[i], "goodsNo", detais.goodsNo);
          that.$set(goodsItems[i], "goodsType", detais.goodsType);
          that.$set(goodsItems[i], "itemType", 1);
          // 在途的判断
          let sysAutoDelivery = uni.getStorageSync("autoDelivery"); //分销商是否是直发客户：1 是，0 否
          let sysOnWayFlag = uni.getStorageSync("onWayFlag"); //分销商是否支持在途库存 1是 0否 默认是1
          let onWayFlag = 0;
          //onwaySaleFlag 直发客户是否支持在途：0-否 1-是
          if (sysAutoDelivery == 1) {
            onWayFlag =
              sysOnWayFlag == 1
                ? goodsItems[i].onwaySaleFlag == 1
                  ? 1
                  : 0
                : 0;
          } else {
            onWayFlag = sysOnWayFlag;
          }
          that.$set(goodsItems[i], "sysOnWayFlag", Number(onWayFlag));
          // 默认取第一个货品的在途值
          if (i == 0) {
            that.onWayFlag = goodsItems[i].sysOnWayFlag;
          }
          goodsIdList.push(goodsItems[i].id);
          // 装箱规格
          let boxObj = [
            {
              boxNum: 1,
              boxType: "件",
              defaultFlag: 0,
            },
          ];
          if (!goodsItems[i].boxs) {
            that.$set(goodsItems[i], "boxs", []);
          }
          goodsItems[i].boxs = [...boxObj, ...goodsItems[i].boxs];
          that.$set(goodsItems[i], "boxNum", 1);
          that.$set(goodsItems[i], "boxType", "件");
          goodsItems[i].boxs.forEach((boxItem) => {
            if (boxItem.defaultFlag == 1) {
              that.$set(goodsItems[i], "boxNum", boxItem.boxNum);
              that.$set(goodsItems[i], "boxType", boxItem.boxType);
            }
          });

          let itemsIdObj = {
            goodsId: id,
            itemId: goodsItems[i].id,
          };
          twoGoodsIdList.push(itemsIdObj);
        }
        this.goodsItems = detais.goodsItems;
        // detais.goodsItems.forEach((item,index)=>{
        // 	that.$set(item,'buyNum',0);
        // 	that.$set(item,'itemCount',0);
        // 	that.$set(item,'itemId',item.id);
        // 	that.$set(item,'imageUrl',item.itemImg);
        // 	that.$set(item,'diyType',detais.diyType);
        // 	that.$set(item,'goodsId',detais.id);
        // 	that.$set(item,'goodsName',detais.goodsName);
        // 	that.$set(item,'goodsNo',detais.goodsNo);
        // 	that.$set(item,'goodsType',detais.goodsType);
        // 	that.$set(item,'itemType',1);
        // 	// 在途的判断
        // 	let sysAutoDelivery = uni.getStorageSync('autoDelivery')  //分销商是否是直发客户：1 是，0 否
        // 	let sysOnWayFlag = uni.getStorageSync('onWayFlag');  //分销商是否支持在途库存 1是 0否 默认是1
        // 	let onWayFlag=0;
        // 	 //onwaySaleFlag 直发客户是否支持在途：0-否 1-是
        // 	if(sysAutoDelivery==1){
        // 		  onWayFlag =(sysOnWayFlag==1?((item.onwaySaleFlag==1)?1:0):0);
        // 	}else{
        // 		  onWayFlag =sysOnWayFlag;
        // 	}
        // 	that.$set(item,'sysOnWayFlag',Number(onWayFlag));
        // 	// 默认取第一个货品的在途值
        // 	if(index==0){
        // 		that.onWayFlag =item.sysOnWayFlag;
        // 	}
        // 	goodsIdList.push(item.id);
        // 	// 装箱规格
        // 	let boxObj=[{
        // 		boxNum:1,
        // 		boxType:'件',
        // 		defaultFlag:0
        // 	}]
        // 	if(!item.boxs){
        // 		that.$set(item,'boxs',[]);
        // 	}
        // 	item.boxs=[...boxObj,...item.boxs];
        // 	that.$set(item,'boxNum',1);
        // 	that.$set(item,'boxType','件')
        // 	item.boxs.forEach(boxItem=>{
        // 		if(boxItem.defaultFlag==1){
        // 			that.$set(item,'boxNum',boxItem.boxNum);
        // 			that.$set(item,'boxType',boxItem.boxType)
        // 		}
        // 	})

        // 	let itemsIdObj={
        // 		goodsId:id,
        // 		itemId:item.id
        // 	}
        // 	twoGoodsIdList.push(itemsIdObj)
        // })

        this.datailContent = this.formatRichText(res.data.contentUrl);
        console.log(this.datailContent);

        // console.log('详情内容：',this.datailContent);
        if (this.userId != "") {
          this.getInventoryFun(goodsIdList); //获取商品库存
          this.getPriceData(id); //获取商品价格
          this.getPriceItemListFun(twoGoodsIdList); //获取货品价格
          this.getActivityFun(id); // 根据商品ID获取活动
        }
        this.goodsItems = detais.goodsItems;
      });
    },
    // 根据商品ID获取活动
    getActivityFun(id) {
      let that = this;
      promotiongroupseckill({ id: id }).then((res) => {
        if (res.success) {
          let list = res.data;
          let goodsItems = that.goodsItems;
          console.log("货品--列表：--", goodsItems);
          // 先把各种活动插入到单个活中--start
          //判断货品是否有活动
          if (res.data.goodsItemPromotions.length > 0) {
            let PromoList = res.data.goodsItemPromotions;
            PromoList.forEach((promoItem) => {
              goodsItems.forEach((goodItem) => {
                if (promoItem.id == goodItem.id) {
                  //拼团
                  if (
                    promoItem.groupSeckills &&
                    promoItem.groupSeckills.length > 0 &&
                    promoItem.groupSeckills[0].groupSeckillStatus == 1
                  ) {
                    that.$set(
                      goodItem,
                      "groupSeckills",
                      promoItem.groupSeckills
                    );
                  }
                  //促销活动
                  if (promoItem.promotions && promoItem.promotions.length > 0) {
                    that.$set(goodItem, "promotions", promoItem.promotions);
                  }
                }
              });
            });
          }
          // 判断有没有订单活动

          if (res.data.orderPromotions && res.data.orderPromotions.length > 0) {
            goodsItems.forEach((goodItem) => {
              console.log("---ppp", goodItem.promotions);
              this.$set(goodItem, "promotions", []);
              if (!goodItem.groupSeckills && !goodItem.promotions) {
                that.$set(goodItem, "promotions", res.data.orderPromotions);
              } else {
                that.$set(goodItem, "promotions", [
                  ...goodItem.promotions,
                  ...res.data.orderPromotions,
                ]);
              }
            });
          }
          // 先把各种活动插入到单个活中--end
          console.log("活动调试---:", goodsItems);
          setTimeout(() => {
            goodsItems.forEach((goodItem, index) => {
              console.log("活动调试66:", goodItem.salePrice);
              let rulesList = []; //合并后的所有活动规则
              that.$set(goodItem, "groupSeckillId", "");
              that.$set(goodItem, "goodsPromotionId", "");
              that.$set(goodItem, "acVisible", false);
              // 拼团处理--start
              if (goodItem.groupSeckills) {
                that.$set(
                  goodItem,
                  "groupSeckillId",
                  goodItem.groupSeckills[0].groupSeckillId
                ); ////拼团秒杀id
                that.$set(
                  goodItem,
                  "groupId",
                  goodItem.groupSeckills[0].groupSeckillId
                ); ////拼团秒杀id
                that.$set(
                  goodItem,
                  "groupSeckillStatus",
                  goodItem.groupSeckills[0].groupSeckillStatus
                );
                that.$set(
                  goodItem,
                  "groupSeckillPrice",
                  goodItem.groupSeckills[0].groupSeckillPrice
                );
                that.$set(goodItem, "isGroupItem", 1); //货品是否有活动
                that.$set(
                  goodItem,
                  "mtoFlag",
                  goodItem.groupSeckills[0].mtoFlag
                ); // 是否支持预售：0-否 1-是
                that.$set(goodItem, "maxNum", goodItem.groupSeckills[0].maxNum);
                that.$set(
                  goodItem,
                  "realSum",
                  goodItem.groupSeckills[0].realSum
                );
                that.$set(goodItem, "onWayUsableCount", 0);
                let itemSurplusNum =
                  goodItem.groupSeckills[0].maxNum -
                  goodItem.groupSeckills[0].realSum;
                if (itemSurplusNum > 0 && goodItem.inStockUsableCount > 0) {
                  that.$set(goodItem, "inStockUsableCount", itemSurplusNum);
                }
                if (index == 0) {
                  that.groupGoodList.push(goodItem.groupSeckills[0]); //选择第一个货品拼团活动
                  that.intervalTimes(goodItem.groupSeckills[0]); //倒计时
                }
                goodItem.groupSeckills.forEach((groupItem) => {
                  that.$set(
                    groupItem,
                    "itemSurplusNum",
                    groupItem.maxNum - groupItem.realSum > -1
                      ? groupItem.maxNum - groupItem.realSum
                      : 0
                  );
                  that.$set(groupItem, "salePrice", goodItem.salePrice);
                  that.$set(
                    groupItem,
                    "itemSurplusNumLast",
                    groupItem.itemSurplusNum > -1 ? groupItem.itemSurplusNum : 0
                  );
                  //  把活动合并
                  let obj = {
                    isCheck: false,
                    promoType: 3, //活动类型，1 普通活动，2 阶梯活动 3 拼团
                    ruleName: groupItem.name,
                    id: groupItem.groupSeckillId,
                    groupRules: groupItem,
                  };
                  rulesList.push(obj);
                });
              }
              // 拼团处理--end

              // 促销活动处理--start
              that.$set(goodItem, "activityList", []);
              that.$set(goodItem, "giftsList", []); //赠品
              let jieTiList = [];
              let activityList = [];
              let giftsList = [];
              let specialsList = [];
              let activityPrice = goodItem.salePrice; //活动价格
              if (goodItem.promotions && goodItem.promotions.length > 0) {
                // goodItem.promotions.forEach((proItem,proIndex)
                goodItem.promotions.forEach((proItem, proIndex) => {
                  //阶梯活动
                  if (proItem.promoType == 2) {
                    proItem.rules.forEach((ruleItem, ruleIndex) => {
                      that.$set(ruleItem, "isCheck", false);
                      that.$set(ruleItem, "promoType", proItem.promoType); //活动类型，1 普通活动，2 阶梯活动 3 拼团

                      ruleItem.conditions.forEach((cItem) => {
                        //moneyOrCount 规则形式：1金额 2数量
                        // reduceType  满减类型，1减免 2折扣
                        let price = 0;
                        if (cItem.reduceType == 1) {
                          price =
                            ruleItem.moneyOrCount == 1
                              ? goodItem.salePrice -
                                goodItem.salePrice *
                                  (cItem.reduction / cItem.oneBuyMoney)
                              : goodItem.salePrice -
                                (cItem.reduction / cItem.oneBuyCount).toFixed(
                                  2
                                );
                        } else {
                          price = (
                            (cItem.discount / 100) *
                            goodItem.salePrice
                          ).toFixed(2);
                        }
                        that.$set(cItem, "price", price);
                      });
                      if (
                        proIndex == 0 &&
                        ruleIndex == 0 &&
                        !goodItem.groupSeckills
                      ) {
                        this.$set(goodItem, "goodsPromotionId", ruleItem.id); //默认第一个活动规则id
                      }
                      rulesList.push(ruleItem); //把活动合并到一起
                    });
                    jieTiList.push(proItem);
                  } else {
                    //普通活动

                    proItem.rules.forEach((ruleItem, ruleIndex) => {
                      that.$set(ruleItem, "promoType", proItem.promoType); //活动类型，1 普通活动，2 阶梯活动 3 拼团
                      that.$set(ruleItem, "isCheck", false);

                      let promobj = {
                        id: ruleItem.id,
                        promoType: proItem.promoType,
                        ruleName: ruleItem.ruleName,
                        ruleNameEn: ruleItem.ruleNameEn,
                        endTime: proItem.endTime,
                        startTime: proItem.startTime,
                        conditions: ruleItem.conditions,
                      };
                      activityList.push(promobj);
                      ruleItem.conditions.forEach((cItem) => {
                        // 赠品
                        if (cItem.presents && cItem.presents.length > 0) {
                          giftsList.push(cItem.presents);
                        }
                        // 计算活动价格--特价活动
                        if (cItem.specialFlag == 1 && cItem.specials) {
                          specialsList.push(cItem.specials);
                          if (proIndex == 0 && ruleIndex == 0) {
                            activityPrice = cItem.specialPrice; //特价价格

                            that.$set(
                              goodItem,
                              "promotionPrice",
                              activityPrice
                            ); //活动价格
                          }
                        } else {
                          // 促销统计方式：1满减 2满赠 reduceOrPresent
                          if (cItem.reduceOrPresent == 1) {
                            if (cItem.specialFlag == 1) {
                              if (proIndex == 0 && ruleIndex == 0) {
                                that.$set(
                                  goodItem,
                                  "promotionPrice",
                                  cItem.specialPrice
                                ); //活动价格
                              }
                              console.log("活动调试1", cItem.specialPrice);
                            } else {
                              // 满减类型，1减免 2折扣
                              if (cItem.reduceType == 1) {
                                let jianPrice = (
                                  parseFloat(ruleItem.moneyOrCount) == 1
                                    ? parseFloat(cItem.reduction) /
                                      parseFloat(cItem.oneBuyMoney)
                                    : Number(cItem.reduction) /
                                      Number(cItem.oneBuyCount)
                                ).toFixed(2);
                                if (proIndex == 0 && ruleIndex == 0) {
                                  activityPrice =
                                    goodItem.salePrice - jianPrice;
                                  that.$set(
                                    goodItem,
                                    "promotionPrice",
                                    activityPrice
                                  ); //活动价格
                                }
                              } else {
                                if (proIndex == 0 && ruleIndex == 0) {
                                  activityPrice = (
                                    (parseFloat(goodItem.salePrice) *
                                      parseFloat(cItem.discount)) /
                                    100
                                  ).toFixed(2);

                                  that.$set(
                                    goodItem,
                                    "promotionPrice",
                                    activityPrice
                                  ); //活动价格
                                }
                                // 没计算完
                              }
                            }
                          }
                        }
                      });
                      if (
                        proIndex == 0 &&
                        ruleIndex == 0 &&
                        !goodItem.groupSeckills
                      ) {
                        that.$set(goodItem, "goodsPromotionId", ruleItem.id); //默认第一个活动规则id
                        //
                      }

                      rulesList.push(ruleItem); //把活动合并到一起
                    });
                  }
                });
              }
              that.$set(goodItem, "giftsList", giftsList); //赠品列表
              that.$set(goodItem, "specialsList", specialsList); //特价货品列表
              that.$set(goodItem, "activityList", activityList); //普通促销活动
              that.$set(goodItem, "rulesList", rulesList); //合并的所有活动
              that.$set(goodItem, "jieTiList", jieTiList); //阶梯活动列表

              // 促销活动处理--end
              if (index == 0) {
                if (goodItem.jieTiList && goodItem.jieTiList.length > 0) {
                  that.ladderPriceList =
                    goodItem.jieTiList.length > 0
                      ? goodItem.jieTiList[0].rules[0].conditions
                      : []; //第一个货品的阶梯价
                  clearInterval(that.intervals);
                  that.intervalTimes(goodItem.jieTiList[0]); //倒计时
                  that.ruleName = goodItem.jieTiList[0].rules[0].ruleName;
                }

                if (
                  goodItem.activityList.length > 0 &&
                  goodItem.groupSeckillId == ""
                ) {
                  that.promotionsList = goodItem.activityList; //第一个货品普通促销活动
                  clearInterval(that.intervals);
                  that.intervalTimes(goodItem.activityList[0]);
                  that.ruleName = goodItem.activityList[0].ruleName;
                  console.log("促销活动名称：", goodItem.activityList[0]);
                }
                // 特价活动
                that.specialsList = goodItem.specialsList
                  ? goodItem.specialsList
                  : [];
                // 货品所有活动
                that.rulesList = goodItem.rulesList;
                that.goodsInfo = goodItem; //第一个货品信息
              }
            });
            console.log("所有货品列表：", goodsItems);
          }, 100);
        }
      });
    },
    //
    fixNum(time) {
      var timer = (Array(2).join(0) + time).slice(-2);
      if (timer > 0) {
        return timer;
      } else {
        return "00";
      }
    },

    // 把日期转化为时间戳
    getTimestamp(date) {
      // var date = '2015-03-05 17:59:00.0';
      date = date.substring(0, 19);
      date = date.replace(/-/g, "/"); //必须把日期'-'转为'/'
      let timestamp = new Date(date).getTime();
      return timestamp;
    },

    // 定时器
    intervalTimes(item) {
      if (this.intervals !== null) {
        clearInterval(this.intervals);
      }
      let that = this;

      this.intervals = setInterval(() => {
        // 获取当前时间，同时得到订单付款截止时间数组
        let newTime = new Date().getTime();
        // 计算相差天数
        let diff = "";
        let title = "";
        if (that.getTimestamp(item.startTime) - newTime > 0) {
          diff = that.getTimestamp(item.startTime) - newTime;
          title = "距离活动开始还剩：";
        } else {
          diff = that.getTimestamp(item.endTime) - newTime;
          title = "距离活动结束还剩：";
        }
        if (diff <= 0) {
          clearInterval(that.intervals);
        }
        let day =
          Math.floor(diff / (24 * 3600 * 1000)) > 0
            ? Math.floor(diff / (24 * 3600 * 1000))
            : 0;
        let hour = that.fixNum(Math.floor((diff / (1000 * 60 * 60)) % 24));
        let minute = that.fixNum(Math.floor((diff / (1000 * 60)) % 60));
        let seconds = that.fixNum(Math.floor((diff / 1000) % 60));
        that.promotionTime =
          title + " " + day + "天" + hour + ":" + minute + ":" + seconds;
      }, 1000);
    },

    // 根据ids查询价格
    getPriceData(id) {
      let that = this;
      priceGoodsList({ goodsIds: [id] }).then((res) => {
        if (res.success) {
          let objArr = {
            minPrice: res.data[0].minPrice,
            maxPrice: res.data[0].maxPrice,
            promotionMaxPrice: res.data[0].promotionMaxPrice
              ? res.data[0].promotionMaxPrice
              : 0,
            promotionMinPrice: res.data[0].promotionMinPrice
              ? res.data[0].promotionMinPrice
              : 0,
            promotionType: res.data[0].promotionType, //活动类型，0 没活动 1促销活动 2拼团 3秒杀
          };
          that.detailsObj = { ...that.detailsObj, ...objArr };
        }
      });
    },

    // 根据商品货品ids查询价格
    getPriceItemListFun(ids) {
      let that = this;
      let params = {
        goodsItemIds: ids,
      };
      priceItemList(params).then((res) => {
        if (res.success) {
          let list = res.data;
          that.goodsItems.forEach((item) => {
            list.forEach((items) => {
              if (items.itemId == item.id) {
                that.$set(item, "retailPrice", items.retailPrice);
                that.$set(item, "salePrice", items.salePrice);
              }
            });
          });
        }
      });
    },
    // 获取是否 0 实际库存 1模糊库存
    userShopSettingFun() {
      let that = this;
      userShopSetting().then((res) => {
        if (res.success) {
          that.stockShowFlag = res.data.stockShowFlag;
          that.stockShowNumber = res.data.stockShowNumber;
        }
        //  console.log('实际库存:',res.data);
      });
    },

    // 获取商品库存
    getInventoryFun(ids) {
      let that = this;
      let userid = uni.getStorageSync("userId");
      let params = {
        distributorId: userid,
        itemIdList: ids,
      };
      listStockByCondition(params).then((stockRes) => {
        if (stockRes.success) {
          let stockResData = stockRes.data;
          let goodItem = JSON.parse(JSON.stringify(that.goodsItems));
          for (let i = 0; i < stockResData.length; i++) {
            goodItem.forEach((items) => {
              if (items.id === stockResData[i].itemId) {
                let inStockUsableCount = stockResData[i].inStockUsableCount; // 在库库存
                let onWayUsableCount =
                  stockResData[i].onWayUsableCount >
                  stockResData[i].inStockUsableCount
                    ? stockResData[i].onWayUsableCount -
                      stockResData[i].inStockUsableCount
                    : 0; // 在途库存
                that.$set(items, "inStockUsableCount", inStockUsableCount);
                that.$set(items, "onWayUsableCount", onWayUsableCount);
              }
            });
          }
          that.goodsItems = goodItem;
        }
      });
    },

    // 是否选择在途
    choiceTutype() {
      this.isInTransit = !this.isInTransit;
    },
    // 切换货品
    goodsItemsClick(index, item) {
      console.log("进入切换货品", item);
      this.rulesIndex = 0; //选择活动index
      this.goodsItemsIndex = index;
      this.goodsInfo = item;
      this.ladderPriceList = [];
      this.groupGoodList = [];
      this.promotionsList = [];
      this.rulesList = [];
      if (item.rulesList) {
        this.rulesList = item.rulesList;
      }

      this.onWayFlag = item.sysOnWayFlag;

      //判断是否有拼团活动
      if (item.groupSeckills && item.groupSeckills.length > 0) {
        this.groupGoodList.push(item.groupSeckills[0]);
        clearInterval(this.intervals);
        this.intervalTimes(item.groupSeckills[0]);
      }

      // 阶梯活动
      if (!item.groupSeckills && item.jieTiList && item.jieTiList.length > 0) {
        this.ladderPriceList = item.jieTiList[0].rules[0].conditions;
        clearInterval(this.intervals);
        this.intervalTimes(item.jieTiList[0]); //倒计时
        this.ruleName = item.jieTiList[0].rules[0].ruleName;
      }

      //普通活动
      if (
        item.activityList &&
        item.activityList.length > 0 &&
        item.groupSeckillId == "" &&
        item.jieTiList.length == 0
      ) {
        this.promotionsList = item.activityList; //第一个货品普通促销活动
        clearInterval(this.intervals);
        this.intervalTimes(item.activityList[0]);
        this.ruleName = item.activityList[0].ruleName;
      }
    },
    // 展开更多货品
    showMoreList(num) {
      if (num == 1) {
        this.showLength = this.goodsItems.length;
      } else {
        this.showLength = 3;
      }
    },

    // 商品加减操作
    numberChange(oldCount, item, type) {
      let that = this;

      console.log("当前货品：", oldCount);
      if (type == 1 && oldCount == 0) {
        return;
      } //零不能减
      let val =
        type == 3
          ? item.buyNum
          : type == 1
          ? parseInt(oldCount / item.boxNum) - 1
          : parseInt(oldCount / item.boxNum) + 1;
      console.log("数量：", parseInt(oldCount / item.boxNum));
      // this.$set(item,'buyNum',val);
      // 计算在途在库下单数量
      let buyNum = val * item.boxNum; //下单数量

      // if(item.buyNum>0){

      // 计算是否活动条件--不满足需去掉活动id
      let lastPrice = buyNum * item.salePrice; //购买的单个货品总价
      let stockNum = this.isInTransit
        ? item.inStockUsableCount + item.onWayUsableCount
        : item.inStockUsableCount;
      // 拼团
      if (item.groupSeckillId && item.groupSeckillId != "") {
        item.groupSeckills.forEach((groupItem) => {
          if (groupItem.groupSeckillId == item.groupSeckillId) {
            if (
              buyNum >= groupItem.minNum &&
              (groupItem.mtoFlag == 1
                ? buyNum <= groupItem.maxNum - groupItem.realSum
                : true)
            ) {
              lastPrice = buyNum * groupItem.groupSeckillPrice;
              if (buyNum > stockNum) {
                // 是否支持超卖或预售 1、支持 0、不支持
                if (groupItem.mtoFlag == 1) {
                  if (
                    groupItem.maxNum &&
                    childItem.maxNum != 0 &&
                    numCount > groupItem.maxNum
                  ) {
                    that.$set(item, "itemCount", oldCount);
                    that.$set(item, "buyNum", parseInt(oldCount / item.boxNum));
                    let title =
                      "货品订购数量不能大于拼团限购量：" + groupItem.maxNum;
                    that.tipFun(title);
                    return;
                  }
                } else {
                  that.$set(item, "itemCount", oldCount);
                  that.$set(item, "buyNum", parseInt(oldCount / item.boxNum));
                  that.tipFun("购买数量不能大于库存");
                  return;
                }
              }
            } else {
              that.$set(item, "groupSeckillId", "");
            }
          }
        });
      } else {
        if (buyNum > stockNum || stockNum == 0) {
          that.$set(item, "itemCount", oldCount);
          that.$set(item, "buyNum", parseInt(oldCount / item.boxNum));
          that.tipFun("购买数量不能大于库存");
          return;
        }
      }

      this.$set(item, "buyNum", Number(val));
      that.$set(item, "itemCount", buyNum);
      if (buyNum <= item.inStockUsableCount) {
        that.$set(item, "zaiKuCount", buyNum);
        that.$set(item, "zaiTuCount", 0);
      } else {
        that.$set(item, "zaiKuCount", item.inStockUsableCount);
        that.$set(item, "zaiTuCount", buyNum - item.inStockUsableCount);
      }
      // 促销活动
      if (item.promotions && item.promotions.length > 0) {
        item.promotions.forEach((pItem) => {
          let onWayFlag = pItem.onWayFlag; //在途商品是否参与活动 1.参与，0.不参与
          let promoStatus = pItem.promoStatus; //促销状态：0 未开始, 1 促销中 2 已过期 3 提前结束
          let promoType = pItem.promoType; //活动类型，1 普通活动，2 阶梯活动
          pItem.rules.forEach((ruleItem, rulesIndex) => {
            let isOneBuyCount = onWayFlag == 0 ? item.zaiKuCount : buyNum;
            let isOneBuyMoney =
              onWayFlag == 0
                ? item.zaiKuCount * item.salePrice
                : buyNum * item.salePrice;

            // if(item.goodsPromotionId==ruleItem.id){
            if (rulesIndex == 0) {
              if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
                let childrenList = this.goodsItems;
                let zCoun = item.zaiKuCount;
                let tCount = item.zaiTuCount;
                let zPrice = item.zaiKuCount * item.salePrice;
                let tPrice = item.zaiTuCount * item.salePrice;
                for (let i = 0; i < childrenList.length; i++) {
                  // 判断同个活动累计数量
                  if (
                    childrenList[i].id != item.id &&
                    childrenList[i].goodsPromotionId == item.goodsPromotionId &&
                    childrenList[i].buyNum > 0
                  ) {
                    zCoun += childrenList[i].zaiKuCount;
                    tCount += childrenList[i].zaiTuCount;
                    zPrice +=
                      childrenList[i].zaiKuCount * childrenList[i].salePrice;
                    tPrice +=
                      childrenList[i].zaiTuCount * childrenList[i].salePrice;
                    console.log(childrenList[i].zaiKuCount);
                  }
                }
                isOneBuyCount = onWayFlag == 0 ? zCoun : zCoun + tCount;
                isOneBuyMoney = onWayFlag == 0 ? zPrice : zPrice + tPrice;
              }

              ruleItem.conditions.forEach((cItem) => {
                if (promoStatus == 1) {
                  //moneyOrCount 规则形式：1金额 2数量
                  if (
                    (ruleItem.moneyOrCount == 1 &&
                      isOneBuyMoney >= cItem.oneBuyMoney) ||
                    (ruleItem.moneyOrCount == 2 &&
                      isOneBuyCount >= cItem.oneBuyCount)
                  ) {
                    that.$set(item, "rulesId", ruleItem.id);
                    that.$set(item, "conditionsId", cItem.id);
                    that.$set(item, "goodsPromotionId", ruleItem.id);

                    //specialFlag是否特价，1是 0否
                    if (cItem.specialFlag == 1) {
                      console.log("特价：");
                      lastPrice =
                        onWayFlag == 0
                          ? Number(item.zaiKuCount * cItem.specialPrice) +
                            Number(item.zaiTuCount * item.salePrice)
                          : Number(item.zaiKuCount + item.zaiTuCount) *
                            Number(cItem.specialPrice);
                      //  活动累计情况下，相同的货品都按特价计算
                      if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
                        let childrenList = that.goodsItems;
                        for (let i = 0; i < childrenList.length; i++) {
                          if (
                            childrenList[i].id != item.id &&
                            childrenList[i].goodsPromotionId ==
                              item.goodsPromotionId &&
                            childrenList[i].buyNum > 0
                          ) {
                            let childrenTotalPrice =
                              onWayFlag == 0
                                ? childrenList[i].zaiKuCount *
                                    cItem.specialPrice +
                                  childrenList[i].zaiTuCount *
                                    childrenList[i].salePrice
                                : childrenList[i].itemCount *
                                  cItem.specialPrice;
                            that.$set(
                              childrenList[i],
                              "lastPrice",
                              childrenTotalPrice
                            );
                            that.$set(
                              childrenList[i],
                              "conditionsId",
                              cItem.id
                            );
                          }
                        }
                      }
                    } else {
                      //不是特价
                      //reduceOrPresent 促销统计方式：1满减 2满赠
                      if (cItem.reduceOrPresent == 1) {
                        // reduceType 满减类型， 2折扣  1减免
                        if (cItem.reduceType == 2) {
                          let zheKouPrice = (
                            (item.salePrice * cItem.discount) /
                            100
                          ).toFixed(2);
                          console.log("折扣价格：", zheKouPrice);
                          lastPrice =
                            onWayFlag == 0
                              ? item.zaiKuCount * zheKouPrice +
                                item.zaiTuCount * item.salePrice
                              : (item.zaiKuCount + item.zaiTuCount) *
                                zheKouPrice;
                          //  活动累计情况下，相同的货品都按减免计算
                          if (
                            ruleItem.addUpFlag == 1 &&
                            ruleItem.ruleType != 1
                          ) {
                            let childrenList = that.goodsItems;
                            for (let i = 0; i < childrenList.length; i++) {
                              if (
                                childrenList[i].id != item.id &&
                                childrenList[i].goodsPromotionId ==
                                  item.goodsPromotionId &&
                                childrenList[i].buyNum > 0
                              ) {
                                let zheKouPrice2 = (
                                  (childrenList[i].salePrice * cItem.discount) /
                                  100
                                ).toFixed(2);
                                let childrenTotalPrice =
                                  onWayFlag == 0
                                    ? childrenList[i].zaiKuCount *
                                        zheKouPrice2 +
                                      childrenList[i].zaiTuCount *
                                        childrenList[i].salePrice
                                    : childrenList[i].itemCount * zheKouPrice2;
                                that.$set(
                                  childrenList[i],
                                  "lastPrice",
                                  childrenTotalPrice
                                );
                                that.$set(
                                  childrenList[i],
                                  "conditionsId",
                                  cItem.id
                                );
                                that.$set(
                                  childrenList[i],
                                  "goodsPromotionId",
                                  childrenList[i].goodsPromotionId
                                );
                              }
                            }
                          }
                        } else {
                          //  活动累计情况下，勾选上的货品都按减免计算
                          if (
                            ruleItem.addUpFlag == 1 &&
                            ruleItem.ruleType != 1
                          ) {
                            let childrenList = that.goodsItems;
                            console.log("sdsdsdsd-----", childrenList);
                            for (let i = 0; i < childrenList.length; i++) {
                              if (
                                childrenList[i].goodsPromotionId ==
                                  item.goodsPromotionId &&
                                childrenList[i].buyNum > 0
                              ) {
                                let reduction2 = cItem.reduction;
                                if (cItem.reductionPresentAddFlag == 1) {
                                  reduction2 =
                                    ruleItem.moneyOrCount == 1
                                      ? onWayFlag == 0
                                        ? childrenList[i].zaiKuCount *
                                          childrenList[i].salePrice *
                                          (cItem.reduction / cItem.oneBuyMoney)
                                        : childrenList[i].itemCount *
                                          childrenList[i].salePrice *
                                          (cItem.reduction / cItem.oneBuyMoney)
                                      : onWayFlag == 0
                                      ? childrenList[i].zaiKuCount *
                                        (cItem.reduction / cItem.oneBuyCount)
                                      : childrenList[i].itemCount *
                                        (cItem.reduction / cItem.oneBuyCount);
                                } else {
                                  reduction2 =
                                    (reduction2 / isOneBuyCount) *
                                    childrenList[i].itemCount;
                                }
                                console.log("减免价格--：", reduction2);
                                let childrenTotalPrice = (
                                  childrenList[i].itemCount *
                                    childrenList[i].salePrice -
                                  reduction2
                                ).toFixed(2);
                                if (childrenList[i].id == item.id) {
                                  lastPrice = childrenTotalPrice;
                                }
                                that.$set(
                                  childrenList[i],
                                  "lastPrice",
                                  childrenTotalPrice
                                );
                                that.$set(
                                  childrenList[i],
                                  "conditionsId",
                                  cItem.id
                                );
                              }
                            }
                          } else {
                            let reduction = cItem.reduction;
                            // 判断是否叠加的减免金额
                            if (cItem.reductionPresentAddFlag == 1) {
                              reduction =
                                ruleItem.moneyOrCount == 1
                                  ? onWayFlag == 0
                                    ? item.zaiKuCount *
                                      item.salePrice *
                                      (cItem.reduction / cItem.oneBuyMoney)
                                    : item.itemCount *
                                      item.salePrice *
                                      (cItem.reduction / cItem.oneBuyMoney)
                                  : onWayFlag == 0
                                  ? item.zaiKuCount *
                                    (cItem.reduction / cItem.oneBuyCount)
                                  : item.itemCount *
                                    (cItem.reduction / cItem.oneBuyCount);
                            }

                            lastPrice = (
                              item.itemCount * item.salePrice -
                              reduction
                            ).toFixed(2);
                          }
                        }
                      }
                    }
                  } else {
                    // that.$set(item,'goodsPromotionId','')
                  }
                }
              });
            }
          });
        });
      }
      console.log("lastPrice:", lastPrice);
      this.$set(item, "lastPrice", lastPrice);

      // }
      console.log(this.goodsItems);
      // 赠品
      this.handleSongData();
    },

    // 选择活动
    choiceActivity2(item, index) {
      let that = this;
      this.spellIndex = index;
      this.rulesIndex = 0; //选择活动index
      this.$refs.groupPopup.close();
      if (this.spellInterval !== null) {
        clearInterval(this.spellInterval);
        this.spellTime = "";
      }

      this.spellInterval = setInterval(() => {
        // 获取当前时间，同时得到订单付款截止时间数组
        let newTime = new Date().getTime();
        // 计算相差天数
        let diff = "";
        let title = "";
        if (that.getTimestamp(item.startTime) - newTime > 0) {
          diff = that.getTimestamp(item.startTime) - newTime;
          title = "距离活动开始还剩：";
        } else {
          diff = that.getTimestamp(item.endTime) - newTime;
          title = "距离活动结束还剩：";
        }
        if (diff <= 0) {
          clearInterval(that.spellInterval);
        }
        let day =
          Math.floor(diff / (24 * 3600 * 1000)) > 0
            ? Math.floor(diff / (24 * 3600 * 1000))
            : 0;
        let hour = that.fixNum(Math.floor((diff / (1000 * 60 * 60)) % 24));
        let minute = that.fixNum(Math.floor((diff / (1000 * 60)) % 60));
        let seconds = that.fixNum(Math.floor((diff / 1000) % 60));
        that.spellTime =
          title + " " + day + "天" + hour + ":" + minute + ":" + seconds;
      }, 1000);
    },

    // 打开活动弹框
    openPromotionPopup() {
      this.$refs.activityPopup.open();
    },
    // 关闭活动弹框
    closePromitionPopup() {
      this.$refs.activityPopup.close();
    },
    // 选择活动
    choicePromition(proItem, index) {
      console.log(proItem, index)
      let that = this;
      // promoType:3,  活动类型，1 普通活动，2 阶梯活动 3 拼团
      this.rulesIndex = index;
      this.promotionTime = "";
      this.$refs.activityPopup.close();
      let item = this.goodsItems[this.goodsItemsIndex];
      this.groupGoodList = [];
      this.ladderPriceList = [];
      this.$set(item, "groupSeckillId", "");
      this.$set(item, "goodsPromotionId", "");
      clearInterval(this.intervals);

      //判断是否有拼团活动
      if (proItem.promoType == 3) {
        item.groupSeckills.forEach((groupItem) => {
          if (groupItem.groupSeckillId == proItem.id) {
            that.groupGoodList.push(groupItem);
            that.intervalTimes(groupItem);
            that.$set(item, "groupSeckillId", groupItem.groupSeckillId);
          }
        });
      }
      // 阶梯活动
      if (proItem.promoType == 2) {
        console.log("阶梯活动：", item.jieTiList);
        item.jieTiList.forEach((jieItem) => {
          jieItem.rules.forEach((ruleItem, ruleIndex) => {
            if (ruleItem.id == proItem.id) {
              that.ladderPriceList = ruleItem.conditions;
              that.intervalTimes(jieItem); //倒计时
              that.ruleName = ruleItem.ruleName;
              that.$set(item, "goodsPromotionId", ruleItem.id);
            }
          });
        });
      }

      //普通活动
      if (proItem.promoType == 1) {
        console.log("普通活动：", item.activityList);
        item.activityList.forEach((ruleItem, ruleIndex) => {
          if (ruleItem.id == proItem.id) {
            that.intervalTimes(ruleItem);
            that.ruleName = ruleItem.ruleName;
            that.$set(item, "goodsPromotionId", ruleItem.id);
          }
        });
      }

      console.log("选择活动", item);
    },

    // 是否收藏
    isCollectFun() {
      let that = this;
      goodsCollection("GET", { id: that.detailId }).then((res) => {
        console.log("收藏状态", res.data);
        that.isCollect = res.data;
      });
    },

    //点击收藏
    collectClickFun(methods) {
      let that = this;
      goodsCollection(methods, { id: that.detailId }).then((res) => {
        console.log("收藏状态", res.data);
        if (res.success) {
          that.isCollect = !that.isCollect;
          if (that.isCollect) {
            that.tipFun("收藏成功");
          } else {
            that.tipFun("取消收藏成功");
          }
        }
      });
    },

    // 跳转首页
    toHomePage() {
      uni.switchTab({
        url: "../index/index",
      });
    },

    // 跳转购物车
    toShoppingCart() {
      uni.switchTab({
        url: "../shoppingCart/shoppingCart",
      });
    },

    // 购买
    buyClick() {
      this.promotionJiSuanFun(); //计算活动

      let list = this.goodsItems;
      let isAllType = false; //货品在库在途是否同时存在
      let zaiKuAllType = false;
      let zaiTuAllType = false;
      let orderList = [];
      list.forEach((item) => {
        // onWayUsableCount判断是否缺货
        // if(item3.isCheck&&(item3.goodsType==1?(item3.onWayUsableCount>0):true)){
        if (item.buyNum > 0) {
          if (item.zaiKuCount > 0) {
            zaiKuAllType = true;
          }
          if (item.zaiTuCount > 0) {
            zaiTuAllType = true;
          }
          orderList.push(item);
        }
      });

      isAllType = zaiKuAllType & zaiTuAllType ? true : false;
      if (orderList.length == 0) {
        this.tipFun("请选择至少一个货品！");
      } else {
        if (this.presentList.length > 0) {
          this.confirmGiftShop(); // 赠品确定
          if (this.presentBuyGoodsList.length > 0) {
            orderList = [...orderList, ...this.presentBuyGoodsList];
            uni.setStorageSync("shopOrderList", JSON.stringify(orderList));
            uni.navigateTo({
              url: "/pages/shoppingCart/confirmOrder?isTwoWay=" + isAllType,
            });
            this.giftNum = 0;
          } else {
            // 未选赠品
            let that = this;
            uni.showModal({
              title: "确定不需要赠品",
              content: "您还未选择赠品，可以取消重新选择赠品",
              success: function (mRes) {
                if (mRes.confirm) {
                  uni.setStorageSync(
                    "shopOrderList",
                    JSON.stringify(orderList)
                  );
                  uni.navigateTo({
                    url:
                      "/pages/shoppingCart/confirmOrder?isTwoWay=" + isAllType,
                  });
                  this.giftNum = 0;
                  that.closeGiftsPopup();
                } else if (mRes.cancel) {
                  that.openGiftsPopup();
                }
              },
            });
          }
        } else {
          uni.setStorageSync("shopOrderList", JSON.stringify(orderList));
          console.log("购买的商品列表：", this.goodsItems);
          uni.navigateTo({
            url: "/pages/shoppingCart/confirmOrder?isTwoWay=" + isAllType,
          });
          this.giftNum = 0;
        }
      }
    },

    // 加入购物车
    addShopCart() {
      let usertId = uni.getStorageSync("userId");
      this.promotionJiSuanFun(); //活动计算
      console.log("货品信息：", this.detailsObj);
      console.log("加入购物车原货品列表：", this.goodsItems);
      let paramsList = [];
      let details = this.detailsObj;
      let list = this.goodsItems;
      for (let i = 0; i < list.length; i++) {
        if (Number(list[i].buyNum) > 0) {
          let objItem = {
            barCode: list[i].barCode, //货品条码
            colorName: list[i].colorName, //货品颜色
            diyType: list[i].diyType, //定制类型 0-标准定制 1-DIY定制
            goodsId: list[i].goodsId, //商品id
            goodsName: list[i].goodsName, //商品名称
            goodsNo: list[i].goodsNo, //商品编码
            goodsPromotionId:
              list[i].goodsPromotionId && list[i].goodsPromotionId != ""
                ? list[i].goodsPromotionId
                : "", //活动条件id
            goodsType: list[i].goodsType, //商品类型 1-普通 2-定制
            groupSeckillId:
              list[i].groupSeckillId && list[i].groupSeckillId != ""
                ? list[i].groupSeckillId
                : "", //拼团秒杀活动id
            height: list[i].height,
            imageUrl: list[i].itemImg,
            itemCode: list[i].itemCode, //货品编码
            itemCount: list[i].boxNum * Number(list[i].buyNum),
            itemId: list[i].id,
            itemName: list[i].itemName,
            itemType: 1, //是否赠品 1 非赠品 2 赠品
            length: list[i].length,
            specsName: list[i].specsName, //货品规格
            weight: list[i].weight,
            width: list[i].width,
          };
          paramsList.push(objItem);
        }
      }

      // 赠品
      if (this.presentList.length > 0) {
        this.confirmGiftShop(); // 赠品确定
        this.presentBuyGoodsList.forEach((item) => {
          if (item.num > 0) {
            let objL = {
              imageUrl: item.itemImg ? item.itemImg : item.imageUrl1,
              cartType: 1, //加购类型：1 分销商 2 C端客户
              diyType: 1, //定制类型 0-标准定制 1-DIY定制
              goodsId: item.goodsId, //商品id
              goodsName: item.goodsName,
              goodsNo: item.goodsNo, //商品编码
              goodsPromotionId: item.goodsPromotionId, //商品促销活动Id(活动条件id)
              goodsType: item.goodsType, //商品类型 1-普通 2-定制
              itemCode: item.itemCode, //货品编码
              itemCount: item.num, //购物数量
              itemNum: item.num, //购物数量
              itemId: item.itemId, //货品id
              itemName: item.itemName, //货品名称
              itemNameEn: item.itemNameEn ? item.itemNameEn : "", //货品英文名称
              itemType: item.itemType, //是否赠品 1 非赠品 2 赠品
              specsName: item.specsName, //货品规格
              colorName: item.colorName, //货品颜色
              weight: item.weight, //重量
              lenght: item.length,
              width: item.width,
              height: item.height,
              barCode: item.barCode,
            };
            paramsList.push(objL);
          }
        });
      }

      if (paramsList.length < 1) {
        this.tipFun("请选择要加入购物车货品！");
        return;
      }
      addShoppingcart(paramsList).then((res) => {
        if (res.success) {
          this.tipFun("加入购物车成功！");
        } else {
          this.tipFun(res.errMessage);
        }
      });
    },

    // 加购和购买活动计算
    promotionJiSuanFun() {
      console.log("货品信息：", this.detailsObj);
      console.log("货品列表：", this.goodsItems);
      let that = this;
      let paramsList = [];
      let details = this.detailsObj;
      let list = this.goodsItems;
      list.forEach((item, index) => {
        if (item.buyNum > 0) {
          // 计算在途在库下单数量
          let buyNum = Number(item.buyNum) * item.boxNum; //下单数量
          that.$set(item, "itemCount", buyNum);
          if (buyNum <= item.inStockUsableCount) {
            that.$set(item, "zaiKuCount", buyNum);
            that.$set(item, "zaiTuCount", 0);
          } else {
            that.$set(item, "zaiKuCount", item.inStockUsableCount);
            that.$set(item, "zaiTuCount", buyNum - item.inStockUsableCount);
          }
        }
      });
      list.forEach((item, index) => {
        if (item.buyNum > 0) {
          let buyNum = Number(item.buyNum) * item.boxNum; //下单数量
          // 计算是否活动条件--不满足需去掉活动id
          let lastPrice = buyNum * item.salePrice; //购买的单个货品总价
          // 拼团
          if (item.groupSeckillId && item.groupSeckillId != "") {
            item.groupSeckills.forEach((groupItem) => {
              if (groupItem.groupSeckillId == item.groupSeckillId) {
                if (
                  buyNum >= groupItem.minNum &&
                  (groupItem.mtoFlag == 1
                    ? buyNum <= groupItem.maxNum - groupItem.realSum
                    : true)
                ) {
                  lastPrice = buyNum * groupItem.groupSeckillPrice;
                } else {
                  that.$set(item, "groupSeckillId", "");
                }
              }
            });
          }

          // 促销活动
          if (item.goodsPromotionId && item.goodsPromotionId != "") {
            item.promotions.forEach((pItem) => {
              let onWayFlag = pItem.onWayFlag; //在途商品是否参与活动 1.参与，0.不参与
              let promoStatus = pItem.promoStatus; //促销状态：0 未开始, 1 促销中 2 已过期 3 提前结束
              let promoType = pItem.promoType; //活动类型，1 普通活动，2 阶梯活动
              pItem.rules.forEach((ruleItem) => {
                let isOneBuyCount = onWayFlag == 0 ? item.zaiKuCount : buyNum;
                let isOneBuyMoney =
                  onWayFlag == 0
                    ? item.zaiKuCount * item.salePrice
                    : buyNum * item.salePrice;
                if (item.goodsPromotionId == ruleItem.id) {
                  if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
                    let childrenList = this.goodsItems;
                    let zCoun = item.zaiKuCount;
                    let tCount = item.zaiTuCount;
                    let zPrice = item.zaiKuCount * item.salePrice;
                    let tPrice = item.zaiTuCount * item.salePrice;
                    for (let i = 0; i < childrenList.length; i++) {
                      // 判断同个活动累计数量
                      if (
                        childrenList[i].id != item.id &&
                        childrenList[i].goodsPromotionId ==
                          item.goodsPromotionId &&
                        childrenList[i].buyNum > 0
                      ) {
                        zCoun += childrenList[i].zaiKuCount;
                        tCount += childrenList[i].zaiTuCount;
                        zPrice +=
                          childrenList[i].zaiKuCount *
                          childrenList[i].salePrice;
                        tPrice +=
                          childrenList[i].zaiTuCount *
                          childrenList[i].salePrice;
                        console.log(childrenList[i].zaiKuCount);
                      }
                    }
                    isOneBuyCount = onWayFlag == 0 ? zCoun : zCoun + tCount;
                    isOneBuyMoney = onWayFlag == 0 ? zPrice : zPrice + tPrice;
                  }

                  ruleItem.conditions.forEach((cItem) => {
                    if (promoStatus == 1) {
                      //moneyOrCount 规则形式：1金额 2数量
                      if (
                        (ruleItem.moneyOrCount == 1 &&
                          isOneBuyMoney >= cItem.oneBuyMoney) ||
                        (ruleItem.moneyOrCount == 2 &&
                          isOneBuyCount >= cItem.oneBuyCount)
                      ) {
                        that.$set(item, "rulesId", ruleItem.id);
                        that.$set(item, "conditionsId", cItem.id);
                        // this.$set(item,'goodsPromotionId',cItem.id);
                        //specialFlag是否特价，1是 0否
                        if (cItem.specialFlag == 1) {
                          lastPrice =
                            onWayFlag == 0
                              ? Number(item.zaiKuCount * cItem.specialPrice) +
                                Number(item.zaiTuCount * item.salePrice)
                              : Number(item.zaiKuCount + item.zaiTuCount) *
                                Number(cItem.specialPrice);
                          //  活动累计情况下，相同的货品都按特价计算
                          if (
                            ruleItem.addUpFlag == 1 &&
                            ruleItem.ruleType != 1
                          ) {
                            let childrenList = that.goodsItems;
                            for (let i = 0; i < childrenList.length; i++) {
                              if (
                                childrenList[i].id != item.id &&
                                childrenList[i].goodsPromotionId ==
                                  item.goodsPromotionId &&
                                childrenList[i].buyNum > 0
                              ) {
                                let childrenTotalPrice =
                                  onWayFlag == 0
                                    ? childrenList[i].zaiKuCount *
                                        cItem.specialPrice +
                                      childrenList[i].zaiTuCount *
                                        childrenList[i].salePrice
                                    : childrenList[i].itemCount *
                                      cItem.specialPrice;
                                that.$set(
                                  childrenList[i],
                                  "lastPrice",
                                  childrenTotalPrice
                                );
                                that.$set(
                                  childrenList[i],
                                  "conditionsId",
                                  cItem.id
                                );
                              }
                            }
                          }
                        } else {
                          //不是特价
                          //reduceOrPresent 促销统计方式：1满减 2满赠
                          if (cItem.reduceOrPresent == 1) {
                            // reduceType 满减类型， 2折扣  1减免
                            if (cItem.reduceType == 2) {
                              let zheKouPrice = (
                                (item.salePrice * cItem.discount) /
                                100
                              ).toFixed(2);
                              lastPrice =
                                onWayFlag == 0
                                  ? item.zaiKuCount * zheKouPrice +
                                    item.zaiTuCount * item.salePrice
                                  : (item.zaiKuCount + item.zaiTuCount) *
                                    zheKouPrice;

                              //  活动累计情况下，相同的货品都按减免计算
                              if (
                                ruleItem.addUpFlag == 1 &&
                                ruleItem.ruleType != 1
                              ) {
                                let childrenList = that.goodsItems;
                                for (let i = 0; i < childrenList.length; i++) {
                                  if (
                                    childrenList[i].id != item.id &&
                                    childrenList[i].goodsPromotionId ==
                                      item.goodsPromotionId &&
                                    childrenList[i].buyNum > 0
                                  ) {
                                    let zheKouPrice2 = (
                                      (childrenList[i].salePrice *
                                        cItem.discount) /
                                      100
                                    ).toFixed(2);
                                    let childrenTotalPrice =
                                      onWayFlag == 0
                                        ? childrenList[i].zaiKuCount *
                                            zheKouPrice2 +
                                          childrenList[i].zaiTuCount *
                                            childrenList[i].salePrice
                                        : childrenList[i].itemCount *
                                          zheKouPrice2;
                                    that.$set(
                                      childrenList[i],
                                      "lastPrice",
                                      childrenTotalPrice
                                    );
                                    that.$set(
                                      childrenList[i],
                                      "conditionsId",
                                      cItem.id
                                    );
                                  }
                                }
                              }
                            } else {
                              //  活动累计情况下，勾选上的货品都按减免计算
                              if (
                                ruleItem.addUpFlag == 1 &&
                                ruleItem.ruleType != 1
                              ) {
                                let childrenList = that.goodsItems;
                                for (let i = 0; i < childrenList.length; i++) {
                                  if (
                                    childrenList[i].goodsPromotionId ==
                                      item.goodsPromotionId &&
                                    childrenList[i].buyNum > 0
                                  ) {
                                    let reduction2 = cItem.reduction;
                                    if (cItem.reductionPresentAddFlag == 1) {
                                      reduction2 =
                                        ruleItem.moneyOrCount == 1
                                          ? onWayFlag == 0
                                            ? childrenList[i].zaiKuCount *
                                              childrenList[i].salePrice *
                                              (cItem.reduction /
                                                cItem.oneBuyMoney)
                                            : childrenList[i].itemCount *
                                              childrenList[i].salePrice *
                                              (cItem.reduction /
                                                cItem.oneBuyMoney)
                                          : onWayFlag == 0
                                          ? childrenList[i].zaiKuCount *
                                            (cItem.reduction /
                                              cItem.oneBuyCount)
                                          : childrenList[i].itemCount *
                                            (cItem.reduction /
                                              cItem.oneBuyCount);
                                    } else {
                                      reduction2 =
                                        (reduction2 / isOneBuyCount) *
                                        childrenList[i].itemCount;
                                    }
                                    console.log("减免价格--：", reduction2);
                                    let childrenTotalPrice = (
                                      childrenList[i].itemCount *
                                        childrenList[i].salePrice -
                                      reduction2
                                    ).toFixed(2);
                                    if (childrenList[i].id == item.id) {
                                      lastPrice = childrenTotalPrice;
                                    }
                                    that.$set(
                                      childrenList[i],
                                      "lastPrice",
                                      childrenTotalPrice
                                    );
                                    that.$set(
                                      childrenList[i],
                                      "conditionsId",
                                      cItem.id
                                    );
                                  }
                                }
                              } else {
                                let reduction = cItem.reduction;
                                // 判断是否叠加的减免金额
                                if (cItem.reductionPresentAddFlag == 1) {
                                  reduction =
                                    ruleItem.moneyOrCount == 1
                                      ? onWayFlag == 0
                                        ? item.zaiKuCount *
                                          item.salePrice *
                                          (cItem.reduction / cItem.oneBuyMoney)
                                        : item.itemCount *
                                          item.salePrice *
                                          (cItem.reduction / cItem.oneBuyMoney)
                                      : onWayFlag == 0
                                      ? item.zaiKuCount *
                                        (cItem.reduction / cItem.oneBuyCount)
                                      : item.itemCount *
                                        (cItem.reduction / cItem.oneBuyCount);
                                }

                                lastPrice = (
                                  item.itemCount * item.salePrice -
                                  reduction
                                ).toFixed(2);
                              }
                            }
                          }
                        }
                      } else {
                        that.$set(item, "goodsPromotionId", "");
                      }
                    }
                  });
                }
              });
            });
          }
          this.$set(item, "lastPrice", lastPrice);
          console.log("最后的价格：", lastPrice);
        }
      });
    },

    // 提交订单
    submitOrder() {
      uni.navigateTo({
        url: "checkstand",
      });
    },

    // 打开配送方式弹框
    openPropertyPopup() {
      this.$refs.propertyPopup.open();
    },
    // 关闭配送方式弹框
    closePropertyPopup() {
      this.$refs.propertyPopup.close();
    },

    // 进入定制
    goCustom() {
      let goodsName = this.detailsObj.goodsName;
      let goodsId = this.detailId;
      let itemId = this.goodsInfo.itemId;
      let itemCode = this.goodsInfo.itemCode;
      let tokenVal = uni.getStorageSync("Font-Token");
      let userId = uni.getStorageSync("userId");

      let enterParams = JSON.stringify({
        goodsName: goodsName,
        goodsId: goodsId,
        itemId: itemId,
        itemCode: itemCode,
        tokenVal: tokenVal,
        userId: userId,
      });

      uni.navigateTo({
        url:
          "/pages/custom/webview?enterFlag=custom&enterParams=" + enterParams,
      });
    },

    /* 赠品 */
    // 赠品
    handleSongData() {
      let list = this.goodsItems;
      this.presentList = []; //赠品列表
      let ptesentIds = []; //赠品ids
      list.forEach((item) => {
        if (item.buyNum && item.buyNum > 0) {
          // 集合赠品列表
          if (item.goodsPromotionId && item.goodsPromotionId != "no") {
            let promitonList = item.promotions;
            // 对应活动的赠品

            promitonList.forEach((preItem) => {
              let onWayFlag = preItem.onWayFlag; //在途商品是否参与活动 1.参与，0.不参与
              let promoStatus = preItem.promoStatus; //促销状态：0 未开始, 1 促销中 2 已过期 3 提前结束
              let promoType = preItem.promoType; //活动类型，1 普通活动，2 阶梯活动
              preItem.rules.forEach((ruleItem) => {
                if (item.goodsPromotionId == ruleItem.id) {
                  let preList = {
                    id: ruleItem.id,
                    ruleName: ruleItem.ruleName,
                    conditionName: "", //条件名称
                    presentCount: 0, //满赠数量
                    count: 0,
                    presents: [],
                  };
                  let isOneBuyCount =
                    onWayFlag == 0
                      ? item.zaiKuCount
                      : item.zaiKuCount + item.zaiTuCount;
                  let isOneBuyMoney =
                    onWayFlag == 0
                      ? item.zaiKuCount * item.salePrice
                      : item.count * item.salePrice;
                  //货品活动是否累计 addUpFlag，1是 0否( ruleType 规则对象是2或3时有效)
                  if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
                    let childrenList = this.goodsItems;
                    let zCoun = item.zaiKuCount;
                    let tCount = item.zaiTuCount;
                    let zPrice = item.zaiKuCount * item.salePrice;
                    let tPrice = item.zaiTuCount * item.salePrice;
                    for (let i = 0; i < childrenList.length; i++) {
                      // 判断同个活动累计数量
                      if (
                        childrenList[i].id != item.id &&
                        childrenList[i].goodsPromotionId ==
                          item.goodsPromotionId &&
                        childrenList[i].count > 0
                      ) {
                        zCoun += childrenList[i].zaiKuCount;
                        tCount += childrenList[i].zaiTuCount;
                        zPrice +=
                          childrenList[i].zaiKuCount *
                          childrenList[i].salePrice;
                        tPrice +=
                          childrenList[i].zaiTuCount *
                          childrenList[i].salePrice;
                      }
                    }
                    isOneBuyCount = onWayFlag == 0 ? zCoun : zCoun + tCount;
                    isOneBuyMoney = onWayFlag == 0 ? zPrice : zPrice + tPrice;
                  }
                  ruleItem.conditions.forEach((cItem) => {
                    // moneyOrCount 规则形式：1金额 2数量
                    //  onWayFlag 在途商品是否参与活动 1.参与，0.不参与
                    if (
                      (ruleItem.moneyOrCount == 1 &&
                        isOneBuyMoney >= cItem.oneBuyMoney) ||
                      (ruleItem.moneyOrCount == 2 &&
                        isOneBuyCount >= cItem.oneBuyCount)
                    ) {
                      // reduceOrPresent:促销统计方式：1满减 2满赠
                      let totalPresentCount2 = 0;
                      if (cItem.reduceOrPresent == 2) {
                        this.$set(item, "conditionsId", cItem.id);
                        // 是否叠加 reductionPresentAddFlag  满减满赠是否叠加，1是 0否
                        let itemCountP =
                          onWayFlag == 0 ? item.zaiKuCount : item.itemCount;
                        let parseIntCount =
                          ruleItem.moneyOrCount == 1
                            ? parseInt(
                                (itemCountP * item.salePrice) /
                                  cItem.oneBuyMoney
                              ) > 0
                              ? parseInt(
                                  (itemCountP * item.salePrice) /
                                    cItem.oneBuyMoney
                                )
                              : 0
                            : parseInt(itemCountP / cItem.oneBuyCount) > 0
                            ? parseInt(itemCountP / cItem.oneBuyCount)
                            : 1;

                        let totalPresentCount3 =
                          cItem.reductionPresentAddFlag == 1
                            ? parseIntCount * cItem.presentCount
                            : cItem.presentCount;
                        cItem.presents.forEach((preSentItem) => {
                          this.$set(preSentItem, "num", 0);
                        });

                        totalPresentCount2 =
                          totalPresentCount3 > cItem.presents[0].buyNum
                            ? cItem.presents[0].buyNum
                            : totalPresentCount3;

                        // 判断是否叠加
                        if (
                          cItem.reductionPresentAddFlag == 1 &&
                          ruleItem.moneyOrCount == 1
                        ) {
                          // 计算叠加的数量（金额）
                          let dieJiaNum = parseInt(
                            isOneBuyMoney / cItem.oneBuyMoney
                          );
                          totalPresentCount2 = cItem.presentCount * dieJiaNum;
                        }

                        preList.presents = cItem.presents;
                        preList.conditionName = cItem.conditionName;
                        preList.presentCount = totalPresentCount2;
                        preList.presentStockNum = totalPresentCount2; // 赠品库存总数（默认可选数量）
                        preList.id = item.goodsPromotionId;
                      }
                    }
                  });

                  if (preList.presents.length > 0) {
                    this.presentList.push(preList);

                    preList.presents.forEach((itemP) => {
                      ptesentIds.push(itemP.itemId);
                    });
                  }
                }
              });
            });

            if (ptesentIds.length > 0) {
              // 获取商品库存
              let stockParams = {
                distributorId: this.userId,
                itemIdList: ptesentIds,
              };

              listStockByCondition(stockParams).then((stockRes) => {
                if (stockRes.success) {
                  let stockResData = stockRes.data;
                  for (let i = 0; i < stockResData.length; i++) {
                    this.presentList.forEach((item1) => {
                      item1.presents.forEach((item2, item2Index) => {
                        this.$set(item2, "isDisabled", false);
                        if (item2.itemId === stockResData[i].itemId) {
                          item2.inStockUsableCount =
                            stockResData[i].inStockUsableCount > 0
                              ? stockResData[i].inStockUsableCount
                              : 0; // 在库库存
                          item2.onWayUsableCount =
                            stockResData[i].numOnWaySum > 0
                              ? stockResData[i].numOnWaySum
                              : 0; // 在途库存
                          let kucunCount =
                            item2.inStockUsableCount + item2.onWayUsableCount;
                          this.$set(item2, "kucunCount", kucunCount);
                          this.$set(item2, "presentCount", item1.presentCount);
                        }
                      });
                    });
                  }

                  this.presentList.forEach((present) => {
                    let presentStockNum = 0; // 赠品库存数
                    present.presents.forEach((child) => {
                      presentStockNum += child.kucunCount;
                    });
                    this.$set(present, "presentStockNum", presentStockNum); // 赠品库存总数
                    this.$set(present, "count", presentStockNum > present.presentCount ? present.presentCount : presentStockNum); // 赠品库存总数
                  });

                  this.handleGiftCount(); // 赠品默认值
                }
              });
            }
            console.log("赠品列表", this.presentList);
          }
        }
      });
    },
    // 赠品默认值
    handleGiftCount() {
      this.presentCount = 0; // 赠品的最大可选数量
      this.selectSongCount = 0; // 赠品的已选数量
      let giftNum = 0; // 已为您选择赠品数量默认值
      let presentStockNum = 0; // 赠品总库存数
      this.presentList.forEach((present) => {
        this.presentCount += present.presentCount;

        giftNum += present.presentCount;
        presentStockNum += present.presentStockNum;

        // 判断可选数量
        if (present.presentCount > 0) {
          // 平均分配数
          let count = Math.floor(
            present.presentCount / present.presents.length
          );
          // 平均分配剩余数量
          let yuCount = present.presentCount % present.presents.length;

          let queCount = 0; // 库存不足时，缺少数量值
          // 平均分配选择赠品数量
          present.presents.forEach((child) => {
            // 库存判断
            if (count > child.kucunCount) {
              // 库存不足，取库存数
              this.$set(child, "num", child.kucunCount);

              queCount += count - child.kucunCount;
            } else {
              // 库存充足，取平均值
              this.$set(child, "num", count);
            }
            this.$set(child, "itemCount", child.num);
          });

          // 多出的，判断库存，分配给库存充足货品
          let totalCount = count + yuCount + queCount;
          present.presents.forEach((child) => {
            // 判断多出数量
            if (totalCount > 0) {
              // 判断货品库存与多出库数量
              if (child.kucunCount > totalCount) {
                // 库存充足，直接赋值
                this.$set(child, "num", totalCount);
                totalCount -= totalCount;
              } else {
                // 库存不足，判断货品库存与已选数量差值
                let num = child.kucunCount - child.num;
                if (num > 0) {
                  this.$set(child, "num", child.kucunCount);
                  totalCount -= num;
                }
              }
              this.$set(child, "itemCount", child.num);
            }

            this.selectSongCount += child.num;
          });
        }
      });

      // 判断赠品总库存数与选择赠品数，设置已选赠品值
			console.log(presentStockNum, giftNum)
      if (presentStockNum >= giftNum) {
        this.giftNum = giftNum;
      } else {
        this.giftNum = presentStockNum;
      }
      console.log(this.giftNum);
    },
    // 赠品弹框加减
    giftHandleChange(item, items, type) {
      let disCount = Number(item.itemCount) - Number(item.num);

      if (type == 1) {
        // 减
        if (item.num > 0) {
          this.$set(item, "num", Number(item.num) - 1);
          this.$set(item, "itemCount", item.num);
          this.$set(items, "count", Number(items.count) - 1);
        }
      } else if (type == 2) {
        // 加，判断已选数量与可选数量或者已选数量与库存数量
        if (items.count >= items.presentCount || item.num >= item.kucunCount) {
          this.$set(item, "num", Number(item.num));
          this.$set(item, "itemCount", item.num);
          this.tipFun("赠品数量已达上限");
        } else {
          this.$set(item, "num", Number(item.num) + 1);
          this.$set(item, "itemCount", Number(item.num));
          this.$set(items, "count", Number(items.count) + 1);
        }
      } else {
        // 输入框输入
        if (Number(item.num) >= 0) {
          // 判断输入数量与当前数量
          if (Number(item.num) > Number(item.itemCount)) {
            // 大于，判断已选数量加当前数量与可选数量或者已选数量与库存数量
            if (
              items.count + Number(item.num) > items.presentCount ||
              item.num > item.kucunCount
            ) {
              // 大于
              this.$set(item, "num", item.itemCount);
              this.$set(item, "itemCount", item.num);
              this.tipFun("赠品数量已达上限");
            } else {
              // 小于或等于
              this.$set(item, "num", Number(item.num));
              this.$set(item, "itemCount", item.num);
              this.$set(items, "count", Number(items.count) - disCount);
            }
          } else {
            // 小于或等于，更新已选数量
            this.$set(item, "num", Number(item.num));
            this.$set(item, "itemCount", item.num);
            this.$set(items, "count", Number(items.count) - disCount);
          }
        } else {
          // 小于或等于，设置为当前数量
          this.$set(item, "num", item.itemCount);
        }
      }

      // 更新赠品的已选数量
      this.selectSongCount = 0;
      this.presentList.forEach((present) => {
        present.presents.forEach((child) => {
          this.selectSongCount += child.num;
        });
      });
    },
    // 提交确认赠品-y
    confirmGiftShop() {
      this.closeGiftsPopup();

      let list = this.presentList;
      this.presentBuyGoodsList = [];

      let giftNum = 0;
      list.forEach((item) => {
        item.presents.forEach((item2) => {
          if (item2.num > 0) {
            giftNum += item2.num;
            this.$set(item2, "goodsType", this.goodsType);
            this.$set(item2, "itemCount", item2.num);
            this.$set(item2, "conditionsId", item2.promotionRuleConditionId);
            this.$set(item2, "goodsPromotionId", item.id);
            this.$set(item2, "totalPrice", 0);
            this.$set(item2, "itemType", 2); //是否赠品 1 非赠品 2 赠品
            if (item2.num <= item2.inStockUsableCount) {
              this.$set(item2, "zaiKuCount", item2.num);
              this.$set(item2, "zaiTuCount", 0);
            } else {
              this.$set(item2, "zaiKuCount", item2.inStockUsableCount);
              this.$set(
                item2,
                "zaiTuCount",
                item2.num - item2.inStockUsableCount
              );
            }
            this.presentBuyGoodsList.push(item2);
          }
        });
      });

      this.giftNum = giftNum;
    },
    // 赠品弹窗 - 显示
    openGiftsPopup() {
      this.$refs.giftsPopup.open();
    },
    // 赠品弹窗 - 隐藏
    closeGiftsPopup() {
      this.$refs.giftsPopup.close();
    },
    /* 赠品 */
  },
};
</script>

<style lang="scss">
.self-numberBox2 {
  display: flex;
  align-items: center;
  text:nth-child(1),
  text:nth-child(3) {
    display: block;
    font-size: 40rpx;
    color: #999;
    width: 50rpx;
    height: 45rpx;
    line-height: 45rpx;
    margin-left: 10rpx;
    background: #fafafc;
    text-align: center;
    border-radius: 5rpx;
  }
  input {
    width: 80rpx;
    height: 45rpx;
    line-height: 45rpx;
    background: #fff;
    border-radius: 10rpx;
    margin-left: 10rpx;
    text-align: center;
    font-size: 26rpx;
  }
}

.tuTypeHover {
  color: $base-color1 !important;
}
.swiper {
  height: 750rpx;
}

.goodsDetails {
  overflow-x: hidden;
  font-size: 26rpx;
  overflow-x: hidden;
  .gd-top {
    padding-top: calc(116rpx + var(--status-bar-height));
    // #ifdef H5
    padding-top: 104rpx;
    // #endif
    background: #fff;
    .goodsImg {
      width: 750rpx;
      height: 750rpx;
    }
    .gd-topInfrom {
      position: relative;

      .gd-topShare {
        position: absolute;
        right: 0;
        bottom: 60rpx;
        image {
          width: 50rpx;
          height: 50rpx;
          margin-right: 50rpx;
        }
      }

      .gd-num {
        position: absolute;
        right: 30rpx;
        bottom: 20rpx;
      }

      &.padding-bottom {
        padding-bottom: 45rpx;
      }
    }
    .gd-topName {
      font-size: 32rpx;
      font-weight: 400;
      padding: 20rpx 30rpx 0 30rpx;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 2;
      overflow: hidden;
    }
    .gd-topLabel {
      padding-top: 15rpx;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding-left: 30rpx;

      &.no-label {
        .gd-topShare {
          position: absolute;
          right: 0;
        }
      }
    }
    .gd-topShare {
      border-top-left-radius: 40rpx;
      border-bottom-left-radius: 40rpx;
      width: 85rpx;
      height: 70rpx;
      padding-left: 25rpx;
      position: relative;
      button {
        background: rgba($color: #000000, $alpha: 0);
        left: -25rpx;
        width: 115rpx;
        height: 70rpx;
        border: none;
      }
      button::after {
        border: none;
      }
      text {
        font-size: 45rpx;
        color: #fff;
        position: absolute;
        top: -20rpx;
        right: 75rpx;
      }
      image {
        width: 50rpx;
        height: 50rpx;
        margin-right: 50rpx;
      }
    }
    .gd-top-btm {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 15rpx 30rpx;
      .gd-topPrice {
        padding-left: 0;
      }
    }
    .gd-topPrice {
      padding: 0 30rpx;
      color: $base-color2;
      font-size: 48rpx;
      font-weight: 500;
      text {
        font-size: 26rpx;
      }
    }
    .gd-topPrice2 {
      color: $base-color2;
      font-weight: 500;
      margin-left: 30rpx;
      display: flex;
      align-items: center;
      margin-bottom: 20rpx;
      view:nth-child(1) {
        text:nth-child(1) {
          font-size: 26rpx;
        }
        text:nth-child(2) {
          font-size: 48rpx;
        }
      }
      view:nth-child(2) {
        color: #999;
        font-size: 32rpx;
        margin-left: 30rpx;
      }
    }
    .gd-topNum {
      font-size: 24rpx;
      color: $base-color3;
    }
    .gd-topTime {
      width: 630rpx;
      height: 80rpx;
      line-height: 80rpx;
      background: linear-gradient(90deg, #fee40a 0%, #ffe880 100%);
      border-radius: 10rpx;
      padding: 0 30rpx;
      margin-left: 30rpx;
      margin-top: 20rpx;
      display: flex;
      justify-content: space-between;
      font-size: 26rpx;
      position: relative;
      overflow: hidden;
      view {
        text-align: center;
        width: 200rpx;
        display: flex;
        text:nth-child(1) {
          width: 180rpx;
          display: block;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }
      .gd-xiexian {
        display: block;
        position: absolute;
        left: 240rpx;
        top: -15rpx;
        width: 4px;
        height: 60px;
        background-color: #ffcf3b;
        transform: rotate(30deg);
        border-top-left-radius: 10rpx;
        border-bottom-right-radius: 10rpx;
      }
      .gd-topTimeIcon {
        font-size: 22rpx;
        color: #333;
        margin-left: 15rpx;
      }
      .gd-tims {
        font-size: 22rpx;
      }
    }

    .gd-topInfrom2 {
      padding-bottom: 20rpx;
      .through-text {
        text-decoration: line-through; //删除线
        color: #999;
        font-size: 36rpx;
        margin-left: 20rpx;
      }
      .gd-topSpell {
        padding: 30rpx;
        display: flex;
        align-items: center;
        justify-content: space-between;
        .gd-topSpell-lf {
          .gd-topSpell-price {
            display: flex;
            align-items: center;
            view {
              text:nth-child(1) {
                color: #999;
                font-size: 22rpx;
              }
              text:nth-child(2) {
                color: #ed5407;
                font-size: 22rpx;
              }
              text:nth-child(3) {
                color: #ed5407;
                font-size: 48rpx;
              }
            }
          }
          .gd-topSpell-num {
            font-size: 22rpx;
            color: #999;
            margin-top: 15rpx;
            text:nth-child(2) {
              margin-left: 30rpx;
            }
          }
        }
        .gd-topSpell-residueNum {
          width: 160rpx;
          height: 56rpx;
          line-height: 56rpx;
          background: #f0f0f0;
          border-radius: 5rpx;
          text-align: center;
          color: #999;
        }
      }
      .gd-topPriceList {
        display: flex;
        align-items: center;
        justify-content: space-around;
        padding: 10rpx 20rpx 5rpx;
        .gd-topPriceList-line {
          text-align: center;
        }
      }
      .gd-topName2 {
        font-size: 32rpx;
        width: 550rpx;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
      }

      .gd-top-btm {
        padding-top: 23rpx;
      }

      .label-module {
        margin-top: 0;
      }
    }
  }
  .gd-buyGood {
    background: #fff;
    margin: 20rpx 0;
    width: 100%;
    padding: 30rpx;
    font-size: 24rpx;
    box-sizing: border-box;
    .gd-buyGood-tip {
      display: flex;
      align-items: center;
      font-size: 24rpx;
      .gd-bG-tipLabel {
        display: flex;

        view {
          display: flex;
          align-items: center;
          margin-left: 35rpx;
          .sxIcon {
            width: 30rpx;
            height: 30rpx;
            margin-right: 4rpx; 
          }
          .plIcon {
            background: url(../../static/imgs/pl_icon.png) no-repeat;
            background-size: 30rpx 30rpx; 
			margin-top: 2rpx;
          }
          .pzIcon {
            background: url(../../static/imgs/pz_icon.png) no-repeat;
            background-size: 30rpx 30rpx;
			margin-top: 3rpx
          }
          .shIcon {
            background: url(../../static/imgs/sh_icon.png) no-repeat;
            background-size: 30rpx 30rpx;
			margin-top: 3rpx
          }
        }
      }
    }
    .gd-buyGood-numLine {
      color: #999;
      font-size: 24rpx;
      display: flex;
      margin-top: 20rpx;
      text:nth-child(1) {
        display: block;
        width: 150rpx;
      }
      view:nth-child(2) {
        margin-left: 50rpx;
      }
    }
    .gd-buyGood-zaitu {
      display: flex;
      align-items: center;
      padding-top: 45rpx;
      font-size: 26rpx;
      color: #999;
      .iconfont {
        position: relative;
        top: 3rpx;
        font-size: 36rpx;
        margin-right: 15rpx;
      }
    }
    .gd-buyGood-list {
      padding-top: 30rpx;
      .gd-buyGood-line {
        display: flex;
        margin-bottom: 30rpx;
        border-radius: 10rpx;
        padding: 10rpx;
        position: relative;
        image {
          width: 120rpx;
          height: 120rpx;
          border-radius: 10rpx;
          overflow: hidden;
        }
        .gd-buyGood-lineRg {
          margin-left: 25rpx;
          width: 500rpx;
          display: flex;
          align-items: center;
          justify-content: space-between;
          .gd-buyGood-lineCenter {
            view:nth-child(2) {
              margin: 8rpx 0;
            }
            .gd-buyGood-linePrice {
              color: $base-color2;
              font-size: 30rpx;
              text {
                font-size: 24rpx;
              }
            }
          }
          .gd-buyGood-lineNum {
            text-align: right;
            .gd-buyGood-lineKucun {
              margin-top: 10rpx;
              color: #999;
            }
          }
        }
        .gd-buyGood-activityTip {
          position: absolute;
          top: 0;
          left: 0;
          font-size: 20rpx;
          color: #fff;
          padding: 5rpx 7rpx;
          text-align: center;
          background: #ff585d;
          border-top-left-radius: 10rpx;
          border-bottom-right-radius: 10rpx;
        }
      }
      .categoryList-Hover {
        background: #f2f3f8;
      }
      .gd-buyGood-ShowM {
        text-align: center;
        margin: 30rpx 0;
        image {
          width: 40rpx;
          height: 40rpx;
        }
        text {
          font-size: 30rpx;
          color: $base-color1;
        }
      }
    }
  }
  .gd-parameter {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    margin: 20rpx 0;
    background: #fff;
    padding: 20rpx;
    box-sizing: border-box;
    .gd-parameter-view {
      // display: flex;
      // align-items: center;
      // justify-content: space-around;

      &.only {
        width: 100%;
        // justify-content: center;
        // text-align: center;
      }
      view {
        display: flex;
        align-items: center;
      }
      text {
        display: block;
        height: 60rpx;
        line-height: 60rpx;
      }
      text:nth-child(1) {
        color: #999;
        margin-right: 30rpx;
      }
      text:nth-child(2) {
        width: 500rpx;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
  }
  .gd-detailsImg {
    border-radius: 10rpx;
    width: 100%;
    image {
      width: 100%;
    }
    img {
      width: 100% !important;
    }
  }
  .gd-text {
    background: #fff;
    margin-top: 20rpx;
    padding: 20rpx 20rpx 180rpx;
    .gd-text-title {
      color: #333;
      font-size: 26rpx;
      margin-top: 20rpx;
    }
    .gd-text-content {
      color: #999;
      font-size: 22rpx;
      margin-top: 15rpx;
    }
  }
  .property-popup {
    height: 600rpx;
  }
  .gd-property {
    padding: 30rpx 60rpx;
    height: 400rpx;
    overflow-y: auto;
    .gd-property-line {
      display: flex;
      font-size: 28rpx;
      align-items: center;
      text:nth-child(1) {
        width: 200rpx;
        color: #999;
      }
    }
    .gd-property-line2 {
      display: flex;
      font-size: 28rpx;
      align-items: center;
      padding: 20rpx 0;
      text:nth-child(1) {
        margin-top: 5rpx;
        color: #999;
      }
      text:nth-child(2) {
        margin-left: 20rpx;
      }
    }
  }
  .gd-btmBtn {
    display: flex;
    align-items: center;
    background: #fff;
    padding: 20rpx 30rpx;
    position: fixed;
    bottom: 0;
    width: 100%;
    color: #666;
    box-shadow: 0px 9px 19px 1px rgba(8, 100, 111, 0.2);
    box-sizing: border-box;
    z-index: 99;
    .gd-btmBtn-icon {
      text-align: center;
      .iconfont {
        font-size: 40rpx;
      }
      & + .gd-btmBtn-icon {
        margin-left: 40rpx;
      }
      text {
        display: block;
      }
    }
    .dg-IconFont-hover {
      color: $base-color1 !important;
    }
    .gd-btmBtn-btn {
      margin-left: 45rpx;
      display: flex;
      text {
        display: block;
        width: 190rpx;
        height: 75rpx;
        line-height: 75rpx;
        text-align: center;
        box-sizing: border-box;
      }
      text:nth-child(1) {
        border-radius: 40rpx 0rpx 0rpx 40rpx;
      }
      text:nth-child(2) {
        color: #fff;
        border-radius: 0px 40rpx 40rpx 0px;
      }
    }

    .gd-custom-btn {
      margin-left: 45rpx;
      text {
        display: block;
        width: 380rpx;
        height: 75rpx;
        line-height: 75rpx;
        text-align: center;
        color: #fff;
        background: $bg-gradient-btn;
        border-radius: 40rpx;
      }
    }
  }
}

// 赠品弹窗
.sc-aCivityPopup-content {
  background: #fff;
  border-radius: 30rpx 30rpx 0px 0px;
  padding-bottom: 50rpx;
  .myCollect-scroll-Y {
    max-height: 52vh;
    height: auto;
  }
  .sc-activity-list {
    .sc-activity-listBox {
      margin: 0 30rpx;
      padding: 20rpx 0;

			& + .sc-activity-listBox {
				border-top: 1rpx solid $opacity-border;
			}

      .sc-activity-listName {
        color: #333;
        font-size: 26rpx;
        font-weight: bold;
        margin-left: 20rpx;
      }
      .sc-activity-listGuizeBox {
        margin-left: 50rpx;
        margin-top: 20rpx;
        font-size: 24rpx;
        color: #999;
        display: flex;
        .sc-activity-listGuizeTitle {
          width: 160rpx;
        }
        .sc-activity-listGuizeList {
          text {
            display: block;
            padding: 5rpx 0;
          }
        }
      }
    }
  }
  .sc-activity-listBtn {
		color: #fff;
		text-align: center;
		width: 690rpx;
		height: 80rpx;
		line-height: 80rpx;
		font-size: 28rpx;
		margin: 30px auto 0;
		border-radius: 40rpx;
	}
}

// 选择赠品
.popup-giftsList {
  padding: 30rpx;
  .popup-giftsList-activity {
    position: relative;
    padding-left: 30rpx;
    font-size: 24rpx;
    .label {
      position: absolute;
      top: 50%;
      left: 0;
      width: 10rpx;
      height: 10rpx;
      border-radius: 50%;
      transform: translateY(-50%);
    }
    .num {
      padding: 8rpx;
      font-size: 28rpx;
      color: #f94021;
    }
  }
  .popup-giftsList-line {
    display: flex;
    align-items: center;
    padding: 30rpx 0;
    & + .popup-giftsList-line {
      border-top: 1rpx solid $opacity-border;
    }
    image {
      width: 111rpx;
      height: 111rpx;
      margin-left: 15rpx;
    }
    .giftsList-line-rg {
      margin-left: 20rpx;
      .popup-giftsList-top {
        display: flex;
        justify-content: space-between;
        .popup-giftsList-name {
          width: 320rpx;
          font-weight: bold;
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 2;
          overflow: hidden;
        }
        .popup-giftsList-numBox {
          margin-left: 25rpx;
          text-align: right;
          width: 200rpx !important;
        }
      }
      .popup-giftsList-num {
        font-size: 22rpx;
        color: #999;
        margin-top: 10rpx;
        display: flex;
        justify-content: space-between;
      }
    }
  }
}
.popup-giftsList-btm {
  padding: 30rpx;
  .popup-giftsList-total {
    margin-bottom: 30rpx;
    display: flex;
    justify-content: flex-end;
    color: #999;
    text {
      color: #ed5307;
      padding: 0 5rpx;
    }
  }
  .popup-giftsList-btn {
    width: 100%;
    height: 80rpx;
    line-height: 80rpx;
    font-size: 28rpx;
    color: #fff;
    border-radius: 40rpx;
    text-align: center;
  }
}

.gift-num-wrap {
  position: relative;
  margin-top: 20rpx;
  padding: 20rpx 30rpx;
  background-color: #fff;

  .gift-label {
    margin-left: 35rpx;
    margin-right: 5rpx;
    color: #f94021;
  }

  .arrow-right {
    position: absolute;
    top: 50%;
    right: 30rpx;
    width: 24rpx;
    height: 24rpx;
    transform: translateY(-50%);
  }
}
</style>
