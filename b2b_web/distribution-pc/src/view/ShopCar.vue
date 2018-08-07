<template>
  <div>
    <div class="user rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState"></Header>
      <!--主内容-->
      <div class="shop-car rl-margin-zero">
        <div class="rl-text-xxs bleed rl-padding-top-xxxs rl-padding-bottom-xxxs rl-bg-gray-mm">
          <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{name: 'Index'}">首页</el-breadcrumb-item>
            <el-breadcrumb-item>购物车</el-breadcrumb-item>
            <el-breadcrumb-item
              v-if="this.shopCartIdList !== null && this.shopCartIdList.length !== 0"
            >已放入购物车的商品</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
      </div>
      <div
        class="user-main rl-clear rl-padding-top-xxxs rl-margin-zero"
        v-if="this.shopCartIdList !== null && this.shopCartIdList.length !== 0 && this.showCar === true"
      >
        <div class="shopcar-activity rl-clear">
          <div class="shopcar-activity-img rl-fl">
            <i class="el-icon-warning"></i>
          </div>
          <div
            class="shopCart-activity-state rl-fl"
          >由于部分优惠活动不同享，购物车显示的活动非等同于最终活动，系统会根据最终下单的情况，按优惠金额以及活动规则，择优参与订单活动。</div>
        </div>
        <div class="shopcar-table rl-bg-white">
          <table>
            <thead>
              <tr>
                <th class="spe" width="35px">
                  <span class="cursor-pointer" :class="{'gou':all === true}" @click="selectAll"></span>
                </th>
                <th width="65px">图片</th>
                <th width="145px">货号</th>
                <th width="145px">货品名称</th>
                <th width="145px">条形码</th>
                <th width="145px">会员价</th>
                <th class="rl-relative" width="130px">
                  优惠价
                  <el-tooltip class="item" effect="dark" content="优惠价只有在库商品才可享有" placement="bottom">
                    <el-button>
                      <i class="wen-hao1"></i>
                    </el-button>
                  </el-tooltip>
                </th>
                <th width="150px">
                  <el-tooltip
                    class="item"
                    effect="dark"
                    content="订购商品的在库数量不足时，会自动帮用户选中在途数量，在途的商品不参与活动计算，下单默认只享有会员价"
                    placement="bottom"
                  >
                    <el-button>
                      <div>总数量</div>
                      <div class="rl-relative">
                        <i>在库数量</i>
                        <i class="rl-margin-left-xxxxs rl-margin-right-xxxxs">/</i>
                        <i>在途数量</i>
                        <i class="wen-hao wen-hao2"></i>
                      </div>
                    </el-button>
                  </el-tooltip>
                </th>
                <th width="110px">合计</th>
                <th width="110px">操作</th>
              </tr>
            </thead>
            <tbody>
              <!--等级折扣-->
              <tr>
                <td
                  colspan="10"
                  class="dis-td rl-bg-green_xs rl-padding-top-xxxs rl-padding-bottom-xxxs"
                  v-if="this.gradeType === true || this.pickUpType === true"
                >
                  <div
                    v-for="(item,index) in this.gradeDiscounts"
                    :key="index"
                    class="rl-text-xxss rl-tl"
                  >
                    <span
                      class="rl-margin-left-default"
                      v-if="item.moneyOrCount === 1 && totalPrice >= item.oneBuyMoney"
                    >单笔订单商品合计已满{{item.oneBuyMoney}}元，全场商品可享受折扣价！</span>
                    <span
                      class="rl-margin-left-default"
                      v-else-if="item.moneyOrCount === 1 && totalPrice < item.oneBuyMoney"
                    >单笔订单商品满{{item.oneBuyMoney}}元，全场商品可享受折扣价！</span>
                    <span
                      class="rl-margin-left-default"
                      v-else-if="item.moneyOrCount === 2 && totalCount >= item.oneBuyCount"
                    >单笔订单商品合计已满{{item.oneBuyCount}}件，全场商品可享受折扣价！</span>
                    <span
                      class="rl-margin-left-default"
                      v-else-if="item.moneyOrCount === 2 && totalCount < item.oneBuyCount"
                    >单笔订单商品满{{item.oneBuyCount}}件，全场商品可享受折扣价！</span>
                  </div>
                  <div class="rl-text-xxss rl-tl" v-if="this.pickUpRateRule">
                    <span
                      class="rl-margin-left-default"
                      v-if="this.pickUpRateRule.discount !== null && this.pickUpRateRule.discount !== '' && this.pickUpRateRule.discount !== undefined"
                    >当月订单付款总额>前{{this.pickUpRateRule.monthNum}}个月平均下单总额，下月每笔订单均可享受{{this.pickUpRateRule.discount}}%折扣！</span>
                  </div>
                  <div class="rl-text-xxss rl-tl" v-if="this.pickUpRateRule">
                    <span
                      class="rl-margin-left-default"
                      v-if="this.pickUpRateRule.reduction !== null && this.pickUpRateRule.reduction !== '' && this.pickUpRateRule.reduction !== undefined"
                    >当月订单付款总额>前{{this.pickUpRateRule.monthNum}}个月平均下单总额，下月每笔订单均可享受减{{this.pickUpRateRule.reduction}}元！</span>
                  </div>
                </td>
              </tr>
              <!--全场优惠-->
              <tr>
                <td
                  colspan="10"
                  class="spc-td rl-bg-green_xs rl-padding-left-default"
                  v-if="this.orderDiscountRule != null"
                >
                  <div class="activity rl-clear rl-margin-bottom-xxxxs">
                    <div
                      class="btn rl-bg-orange-mm rl-text-white rl-text-xxss rl-fl"
                      v-if="this.orderDiscountRule"
                    >{{this.orderDiscountRule.label}}</div>
                    <div v-if="this.orderDiscountRule.discount !== 0">
                      <div
                        class="rl-fl rl-text-xxss rl-margin-left-lllg"
                        v-if="this.orderDiscountRule.moneyOrCount === 1 && totalPrice >= this.orderDiscountRule.oneBuyMoney"
                      >
                        订单已满{{this.orderDiscountRule.oneBuyMoney}}元，已打{{this.orderDiscountRule.discount}}%折
                        <span
                          v-if="this.orderDiscountRule.isEnjoy === false"
                        >(部分商品与订单活动不同享)</span>
                      </div>
                      <div
                        class="rl-fl rl-text-xxss rl-margin-left-lllg"
                        v-if="this.orderDiscountRule.moneyOrCount === 1 && totalPrice < this.orderDiscountRule.oneBuyMoney"
                      >
                        订单满{{this.orderDiscountRule.oneBuyMoney}}元，可打{{this.orderDiscountRule.discount}}%折
                        <span
                          v-if="this.orderDiscountRule.isEnjoy === false"
                        >(部分商品与订单活动不同享)</span>
                      </div>
                      <div
                        class="rl-fl rl-text-xxss rl-margin-left-lllg"
                        v-if="this.orderDiscountRule.moneyOrCount === 2 && totalCount >= this.orderDiscountRule.oneBuyCount"
                      >
                        订单已满{{this.orderDiscountRule.oneBuyCount}}件，已打{{this.orderDiscountRule.discount}}%折
                        <span
                          v-if="this.orderDiscountRule.isEnjoy === false"
                        >(部分商品与订单活动不同享)</span>
                      </div>
                      <div
                        class="rl-fl rl-text-xxss rl-margin-left-lllg"
                        v-if="this.orderDiscountRule.moneyOrCount === 2 && totalCount < this.orderDiscountRule.oneBuyCount"
                      >
                        订单满{{this.orderDiscountRule.oneBuyCount}}件，可打{{this.orderDiscountRule.discount}}%折
                        <span
                          v-if="this.orderDiscountRule.isEnjoy === false"
                        >(部分商品与订单活动不同享)</span>
                      </div>
                    </div>
                    <div v-else>
                      <div
                        class="rl-fl rl-text-xxss rl-margin-left-lllg"
                        v-if="this.orderDiscountRule.moneyOrCount === 1 && totalPrice >= this.orderDiscountRule.oneBuyMoney && this.orderDiscountRule.presentIsAdd === true"
                      >
                        订单已满{{this.orderDiscountRule.oneBuyMoney*parseInt(totalPrice/this.orderDiscountRule.oneBuyMoney)}}元，已减{{this.orderDiscountRule.reduction*(totalPrice/orderDiscountRule.oneBuyMoney).toFixed(2)}}元现金
                        <span
                          v-if="this.orderDiscountRule.isEnjoy === false"
                        >(部分商品与订单活动不同享)</span>
                      </div>
                      <div
                        class="rl-fl rl-text-xxss rl-margin-left-lllg"
                        v-if="this.orderDiscountRule.moneyOrCount === 1 && totalPrice >= this.orderDiscountRule.oneBuyMoney && this.orderDiscountRule.presentIsAdd === false"
                      >
                        订单已满{{this.orderDiscountRule.oneBuyMoney}}元，已减{{this.orderDiscountRule.reduction}}元现金
                        <span
                          v-if="this.orderDiscountRule.isEnjoy === false"
                        >(部分商品与订单活动不同享)</span>
                      </div>
                      <div
                        class="rl-fl rl-text-xxss rl-margin-left-lllg"
                        v-if="this.orderDiscountRule.moneyOrCount === 1 && totalPrice < this.orderDiscountRule.oneBuyMoney"
                      >
                        订单满{{this.orderDiscountRule.oneBuyMoney}}元，可减{{this.orderDiscountRule.reduction}}元现金
                        <span
                          v-if="this.orderDiscountRule.isEnjoy === false"
                        >(部分商品与订单活动不同享)</span>
                      </div>
                      <div
                        class="rl-fl rl-text-xxss rl-margin-left-lllg"
                        v-if="this.orderDiscountRule.moneyOrCount === 2 && totalCount >= this.orderDiscountRule.oneBuyCount && this.orderDiscountRule.presentIsAdd === true"
                      >
                        订单已满{{totalCount}}件，已减{{(orderDiscountRule.reduction*(totalCount/orderDiscountRule.oneBuyCount)).toFixed(2)}}元现金
                        <span
                          v-if="this.orderDiscountRule.isEnjoy === false"
                        >(部分商品与订单活动不同享)</span>
                      </div>
                      <div
                        class="rl-fl rl-text-xxss rl-margin-left-lllg"
                        v-if="this.orderDiscountRule.moneyOrCount === 2 && totalCount >= this.orderDiscountRule.oneBuyCount && this.orderDiscountRule.presentIsAdd === false"
                      >
                        订单已满{{this.orderDiscountRule.oneBuyCount}}件，已减{{this.orderDiscountRule.reduction}}元现金
                        <span
                          v-if="this.orderDiscountRule.isEnjoy === false"
                        >(部分商品与订单活动不同享)</span>
                      </div>
                      <div
                        class="rl-fl rl-text-xxss rl-margin-left-lllg"
                        v-if="this.orderDiscountRule.moneyOrCount === 2 && totalCount < this.orderDiscountRule.oneBuyCount"
                      >
                        订单满{{this.orderDiscountRule.oneBuyCount}}件，可减{{this.orderDiscountRule.reduction}}元现金
                        <span
                          v-if="this.orderDiscountRule.isEnjoy === false"
                        >(部分商品与订单活动不同享)</span>
                      </div>
                    </div>
                  </div>
                </td>
              </tr>
            </tbody>
            <tbody v-for="items in shopCartIdList" :key="items.id">
              <!--定制商品-->
              <tr v-if="items.orderType === 3 && items.customMark === 1 && items.items.length > 0">
                <td colspan="10" class="rl-bg-green_xs rl-padding-left-default spc-td">
                  <div class="rl-fl rl-text-blue-mm">定制商品</div>
                </td>
              </tr>
              <!--预售商品-->
              <tr v-if="items.orderType === 2 && items.presellMark === 1 && items.items.length > 0">
                <td colspan="10" class="rl-bg-green_xs rl-padding-left-default spc-td">
                  <div class="rl-fl rl-text-blue-mm">预售商品</div>
                </td>
              </tr>
              <!--满减-->
              <tr
                v-if="items.promotionRule !== null && items.promotionRule.reduceOrPresent === 1 && items.items.length > 0 && items.items.length > 0"
              >
                <td colspan="10" class="spc-td rl-bg-green_xs rl-padding-left-default">
                  <div
                    class="activity rl-clear rl-margin-bottom-xxxxs"
                    v-if="items.promotionRule.discount !== 0"
                  >
                    <div
                      class="btn rl-bg-orange-mm rl-text-white rl-text-xxss rl-fl"
                    >{{items.promotionRule.label}}</div>
                    <div
                      class="rl-fl rl-text-xxss rl-margin-left-lllg"
                      v-if="items.totalPrice >= items.promotionRule.oneBuyMoney && items.promotionRule.moneyOrCount === 1"
                    >
                      活动商品已购满{{items.promotionRule.oneBuyMoney}}元，已打{{items.promotionRule.discount}}%折
                      <span
                        v-if="items.promotionRule.isEnjoy === false"
                      >(与订单活动不同享)</span>
                    </div>
                    <div
                      class="rl-fl rl-text-xxss rl-margin-left-lllg"
                      v-else-if="items.totalCount >= items.promotionRule.oneBuyCount && items.promotionRule.moneyOrCount === 2"
                    >
                      活动商品已购满{{items.promotionRule.oneBuyCount}}件，已打{{items.promotionRule.discount}}%折
                      <span
                        v-if="items.promotionRule.isEnjoy === false"
                      >(与订单活动不同享)</span>
                    </div>
                    <div
                      class="rl-fl rl-text-xxss rl-margin-left-lllg"
                      v-else-if="items.totalPrice < items.promotionRule.oneBuyMoney && items.promotionRule.moneyOrCount === 1"
                    >
                      活动商品满{{items.promotionRule.oneBuyMoney}}元，打{{items.promotionRule.discount}}%折
                      <span
                        v-if="items.promotionRule.isEnjoy === false"
                      >(与订单活动不同享)</span>
                    </div>
                    <div
                      class="rl-fl rl-text-xxss rl-margin-left-lllg"
                      v-else-if="items.totalCount < items.promotionRule.oneBuyCount && items.promotionRule.moneyOrCount === 2"
                    >
                      活动商品满{{items.promotionRule.oneBuyCount}}件，打{{items.promotionRule.discount}}%折
                      <span
                        v-if="items.promotionRule.isEnjoy === false"
                      >(与订单活动不同享)</span>
                    </div>
                    <div
                      @click="pieceTogether(items.promotionRule.id,1)"
                      class="rl-fl rl-text-xxss rl-margin-left-lllg rl-text-orange-sm cursor-pointer"
                      v-if="(items.totalPrice < items.promotionRule.oneBuyMoney) || (items.totalCount < items.promotionRule.oneBuyCount)"
                    >>去凑单</div>
                  </div>
                  <div class="activity rl-clear rl-margin-bottom-xxxxs" v-else>
                    <div
                      class="btn rl-bg-orange-mm rl-text-white rl-text-xxss rl-fl"
                    >{{items.promotionRule.label}}</div>
                    <div
                      class="rl-fl rl-text-xxss rl-margin-left-lllg"
                      v-if="items.totalPrice >= items.promotionRule.oneBuyMoney && items.promotionRule.moneyOrCount === 1 && items.promotionRule.reductionIsAdd === true"
                    >
                      活动商品已购满{{parseInt(items.promotionRule.oneBuyMoney*(items.totalPrice/items.promotionRule.oneBuyMoney))}}元，已减{{items.promotionRule.reduction*(items.totalPrice/items.promotionRule.oneBuyMoney).toFixed(2)}}元
                      <span
                        v-if="items.promotionRule.isEnjoy === false"
                      >(与订单活动不同享)</span>
                    </div>
                    <div
                      class="rl-fl rl-text-xxss rl-margin-left-lllg"
                      v-else-if="items.totalCount >= items.promotionRule.oneBuyCount && items.promotionRule.moneyOrCount === 2 && items.promotionRule.reductionIsAdd === true"
                    >
                      活动商品已购满{{parseInt(items.promotionRule.oneBuyCount*(items.totalCount/items.promotionRule.oneBuyCount))}}件，已减{{items.promotionRule.reduction*(items.totalCount/items.promotionRule.oneBuyCount).toFixed(2)}}元
                      <span
                        v-if="items.promotionRule.isEnjoy === false"
                      >(与订单活动不同享)</span>
                    </div>
                    <div
                      class="rl-fl rl-text-xxss rl-margin-left-lllg"
                      v-else-if="items.totalPrice >= items.promotionRule.oneBuyMoney && items.promotionRule.moneyOrCount === 1 && items.promotionRule.reductionIsAdd === false"
                    >
                      活动商品已购满{{items.promotionRule.oneBuyMoney}}元，已减{{items.promotionRule.reduction}}元
                      <span
                        v-if="items.promotionRule.isEnjoy === false"
                      >(与订单活动不同享)</span>
                    </div>
                    <div
                      class="rl-fl rl-text-xxss rl-margin-left-lllg"
                      v-else-if="items.totalCount >= items.promotionRule.oneBuyCount && items.promotionRule.moneyOrCount === 2 && items.promotionRule.reductionIsAdd === false"
                    >
                      活动商品已购满{{items.promotionRule.oneBuyCount}}件，已减{{items.promotionRule.reduction}}元
                      <span
                        v-if="items.promotionRule.isEnjoy === false"
                      >(与订单活动不同享)</span>
                    </div>
                    <div
                      class="rl-fl rl-text-xxss rl-margin-left-lllg"
                      v-else-if="items.totalPrice < items.promotionRule.oneBuyMoney && items.promotionRule.moneyOrCount === 1"
                    >
                      活动商品满{{items.promotionRule.oneBuyMoney}}元，减{{items.promotionRule.reduction}}元
                      <span
                        v-if="items.promotionRule.isEnjoy === false"
                      >(与订单活动不同享)</span>
                    </div>
                    <div
                      class="rl-fl rl-text-xxss rl-margin-left-lllg"
                      v-else-if="items.totalCount < items.promotionRule.oneBuyCount && items.promotionRule.moneyOrCount === 2"
                    >
                      活动商品满{{items.promotionRule.oneBuyCount}}件，减{{items.promotionRule.reduction}}元
                      <span
                        v-if="items.promotionRule.isEnjoy === false"
                      >(与订单活动不同享)</span>
                    </div>
                    <div
                      @click="pieceTogether(items.promotionRule.id,1)"
                      class="rl-fl rl-text-xxss rl-margin-left-lllg rl-text-orange-sm cursor-pointer"
                      v-if="items.promotionRule.reductionIsAdd === true || ((items.totalPrice < items.promotionRule.oneBuyMoney) || (items.totalCount < items.promotionRule.oneBuyCount) && items.promotionRule.reductionIsAdd === false)"
                    >>去凑单</div>
                  </div>
                </td>
              </tr>
              <!-- 满赠-->
              <tr
                v-if="items.promotionRule !== null && items.promotionRule.reduceOrPresent === 2 && items.items.length > 0"
              >
                <td
                  colspan="10"
                  class="spc-td rl-bg-green_xs rl-padding-left-default"
                  :class="{'spc-tdsp':items.promotionRule.freeShipping === true}"
                >
                  <div class="activity rl-clear rl-margin-bottom-xxxxs">
                    <div
                      class="btn rl-bg-orange-mm rl-text-white rl-text-xxss rl-fl"
                    >{{items.promotionRule.label}}</div>
                    <div
                      class="rl-fl rl-text-xxss rl-margin-left-lllg"
                      v-if="items.totalPrice >= items.promotionRule.oneBuyMoney && items.promotionRule.moneyOrCount === 1 && items.promotionRule.presentIsAdd === true"
                    >
                      活动商品已购满{{parseInt(items.promotionRule.oneBuyMoney*(items.totalPrice/items.promotionRule.oneBuyMoney))}}元，可领取赠品{{parseInt(items.promotionRule.presentCount*(items.totalPrice/items.promotionRule.oneBuyMoney))}}件
                      <span
                        v-if="items.promotionRule.isEnjoy === false"
                      >(与订单活动不同享)</span>
                    </div>
                    <div
                      class="rl-fl rl-text-xxss rl-margin-left-lllg"
                      v-else-if="items.totalCount >= items.promotionRule.oneBuyCount && items.promotionRule.moneyOrCount === 2 && items.promotionRule.presentIsAdd === true"
                    >
                      活动商品已购满{{parseInt(items.promotionRule.oneBuyCount*(items.totalCount/items.promotionRule.oneBuyCount))}}件，可领取赠品{{parseInt(items.promotionRule.presentCount*(items.totalCount/items.promotionRule.oneBuyCount))}}件
                      <span
                        v-if="items.promotionRule.isEnjoy === false"
                      >(与订单活动不同享)</span>
                    </div>
                    <div
                      class="rl-fl rl-text-xxss rl-margin-left-lllg"
                      v-else-if="items.totalPrice >= items.promotionRule.oneBuyMoney && items.promotionRule.moneyOrCount === 1 && items.promotionRule.presentIsAdd === false"
                    >
                      活动商品已购满{{items.promotionRule.oneBuyMoney}}元，可领取赠品{{items.promotionRule.presentCount}}件
                      <span
                        v-if="items.promotionRule.isEnjoy === false"
                      >(与订单活动不同享)</span>
                    </div>
                    <div
                      class="rl-fl rl-text-xxss rl-margin-left-lllg"
                      v-else-if="items.totalCount >= items.promotionRule.oneBuyCount && items.promotionRule.moneyOrCount === 2 && items.promotionRule.presentIsAdd === false"
                    >
                      活动商品已购满{{items.promotionRule.oneBuyCount}}件，可领取赠品{{items.promotionRule.presentCount}}件
                      <span
                        v-if="items.promotionRule.isEnjoy === false"
                      >(与订单活动不同享)</span>
                    </div>
                    <div
                      class="rl-fl rl-text-xxss rl-margin-left-lllg"
                      v-else-if="items.totalPrice < items.promotionRule.oneBuyMoney && items.promotionRule.moneyOrCount === 1"
                    >
                      活动商品满{{items.promotionRule.oneBuyMoney}}元，即可领取赠品{{items.promotionRule.presentCount}}件
                      <span
                        v-if="items.promotionRule.isEnjoy === false"
                      >(与订单活动不同享)</span>
                    </div>
                    <div
                      class="rl-fl rl-text-xxss rl-margin-left-lllg"
                      v-else-if="items.totalCount < items.promotionRule.oneBuyCount && items.promotionRule.moneyOrCount === 2"
                    >
                      活动商品满{{items.promotionRule.oneBuyCount}}件，即可领取赠品{{items.promotionRule.presentCount}}件
                      <span
                        v-if="items.promotionRule.isEnjoy === false"
                      >(与订单活动不同享)</span>
                    </div>
                    <div
                      @click="lookSong(items.promotionRule.id, items)"
                      class="btn rl-bg-orange-mm rl-text-white rl-text-xxss rl-fl rl-margin-left-lllg cursor-pointer"
                      v-if="((items.totalPrice >= items.promotionRule.oneBuyMoney) && items.promotionRule.oneBuyCount === null) || ((items.totalCount >= items.promotionRule.oneBuyCount) && items.promotionRule.oneBuyMoney === null)"
                    >查看赠品</div>
                    <div
                      @click="pieceTogether(items.promotionRule.id,2)"
                      class="rl-fl rl-text-xxss rl-margin-left-lllg rl-text-orange-sm cursor-pointer"
                      v-if="items.promotionRule.presentIsAdd === true || ((items.totalPrice < items.promotionRule.oneBuyMoney) || (items.totalCount < items.promotionRule.oneBuyCount) && items.promotionRule.presentIsAdd === false)"
                    >>去凑单</div>
                  </div>
                </td>
              </tr>
              <tr
                v-if="items.promotionRule !== null && items.promotionRule.freeShipping === true && items.items.length > 0"
              >
                <td
                  colspan="10"
                  class="rl-bg-green_xs rl-padding-left-default"
                  :class="{'spc-tds':items.promotionRule.freeShipping === true}"
                >
                  <div class="activity rl-clear">
                    <div class="btn rl-bg-orange-mm rl-text-white rl-text-xxss rl-fl">包邮</div>
                    <div
                      class="rl-fl rl-text-xxss rl-margin-left-lllg"
                      v-if="items.totalPrice >= items.promotionRule.oneBuyMoney && items.promotionRule.moneyOrCount === 1"
                    >
                      活动商品已购满{{items.promotionRule.oneBuyMoney}}元，可享包邮
                      <span
                        v-if="items.promotionRule.isEnjoy === false"
                      >(与订单活动不同享)</span>
                    </div>
                    <div
                      class="rl-fl rl-text-xxss rl-margin-left-lllg"
                      v-else-if="items.totalCount >= items.promotionRule.oneBuyCount && items.promotionRule.moneyOrCount === 2"
                    >
                      活动商品已购满{{items.promotionRule.oneBuyCount}}件，可享包邮
                      <span
                        v-if="items.promotionRule.isEnjoy === false"
                      >(与订单活动不同享)</span>
                    </div>
                    <div
                      class="rl-fl rl-text-xxss rl-margin-left-lllg"
                      v-else-if="items.totalPrice < items.promotionRule.oneBuyMoney && items.promotionRule.moneyOrCount === 1"
                    >
                      活动商品满{{items.promotionRule.oneBuyMoney}}元，即可享包邮
                      <span
                        v-if="items.promotionRule.isEnjoy === false"
                      >(与订单活动不同享)</span>
                    </div>
                    <div
                      class="rl-fl rl-text-xxss rl-margin-left-lllg"
                      v-else-if="items.totalCount < items.promotionRule.oneBuyCount && items.promotionRule.moneyOrCount === 2"
                    >
                      活动商品满{{items.promotionRule.oneBuyCount}}件，即可享包邮
                      <span
                        v-if="items.promotionRule.isEnjoy === false"
                      >(与订单活动不同享)</span>
                    </div>
                  </div>
                </td>
              </tr>
              <!-- 商品折扣-->
              <tr
                v-if="items.promotionRule !== null && items.promotionRule.reduceOrPresent === 3 && items.items.length > 0 && items.items.length > 0"
              >
                <td colspan="10" class="spc-td rl-bg-green_xs rl-padding-left-default">
                  <div class="activity rl-clear rl-margin-bottom-xxxxs">
                    <div
                      class="btn rl-bg-orange-mm rl-text-white rl-text-xxss rl-fl"
                    >{{items.promotionRule.label}}</div>
                  </div>
                </td>
              </tr>
              <!--折扣活动-->
              <tr v-if="items.ruleType === 2 && items.items.length > 0">
                <td colspan="10" class="rl-bg-green_xs rl-padding-left-default spc-td">
                  <div class="rl-fl rl-text-blue-mm">折扣活动</div>
                  <div
                    class="rl-fl btn rl-bg-orange-mm rl-text-white rl-margin-left-default"
                  >{{items.gradeDiscountsRule.policyName}}</div>
                  <div v-if="items.gradeDiscountsRule.discountBeforeAfter === 1">
                    <div
                      class="rl-fl rl-text-xxss rl-margin-left-lllg"
                      v-if="items.totalPrice >= items.gradeDiscountsRule.oneBuyMoney && items.gradeDiscountsRule.moneyOrCount === 1"
                    >
                      <span>等级折扣前活动已购满{{items.gradeDiscountsRule.oneBuyMoney}}元，可参与活动(与其他活动不同享)</span>
                    </div>
                    <div
                      class="rl-fl rl-text-xxss rl-margin-left-lllg"
                      v-else-if="items.totalPrice < items.gradeDiscountsRule.oneBuyMoney && items.gradeDiscountsRule.moneyOrCount === 1"
                    >
                      <span>等级折扣前活动已购满{{items.totalPrice}}元，再凑{{items.gradeDiscountsRule.oneBuyMoney - items.totalPrice}}元可参与活动(与其他活动不同享)</span>
                    </div>
                  </div>
                  <div v-else-if="items.gradeDiscountsRule.discountBeforeAfter === 2">
                    <div
                      class="rl-fl rl-text-xxss rl-margin-left-lllg"
                      v-if="items.gradeDiscountTotalPrice >= items.gradeDiscountsRule.oneBuyMoney && items.gradeDiscountsRule.moneyOrCount === 1"
                    >
                      <span>等级折扣后活动已购满{{items.gradeDiscountsRule.oneBuyMoney}}元，可参与活动(与其他活动不同享)</span>
                    </div>
                    <div
                      class="rl-fl rl-text-xxss rl-margin-left-lllg"
                      v-else-if="items.gradeDiscountTotalPrice < items.gradeDiscountsRule.oneBuyMoney && items.gradeDiscountsRule.moneyOrCount === 1"
                    >
                      <span>等级折扣后活动已购满{{items.gradeDiscountTotalPrice.toFixed(2)}}元，再凑{{(items.gradeDiscountsRule.oneBuyMoney - items.gradeDiscountTotalPrice).toFixed(2)}}元可参与活动(与其他活动不同享)</span>
                    </div>
                  </div>
                  <div
                    class="rl-fl rl-text-xxss rl-margin-left-lllg"
                    v-else-if="items.totalCount >= items.gradeDiscountsRule.oneBuyCount && items.gradeDiscountsRule.moneyOrCount === 2"
                  >等级折扣活动已购满{{items.gradeDiscountsRule.oneBuyCount}}件，可参与活动(与其他活动不同享)</div>
                  <div
                    class="rl-fl rl-text-xxss rl-margin-left-lllg"
                    v-else-if="items.totalCount < items.gradeDiscountsRule.oneBuyCount && items.gradeDiscountsRule.moneyOrCount === 2"
                  >等级折扣活动已购满{{items.totalCount}}件，再凑{{items.gradeDiscountsRule.oneBuyCount - items.totalCount}}可参与活动(与其他活动不同享)</div>
                  <div
                    @click="pieceTogether(items.gradeDiscountsRule.id,3)"
                    class="rl-fl rl-text-xxss rl-margin-left-lllg rl-text-orange-sm cursor-pointer"
                  >>去凑单</div>
                </td>
              </tr>
              <tr
                class="rl-bdb-gray-sm"
                :class="{'rl-bg-gray-mm':item.saleStatus !== 3 && item.saleStatus !== 4}"
                v-for="item in items.items"
                :key="item.id"
                v-if="(item.itemType === 1 || item.itemType === 3 || item.itemType === 4)"
              >
                <td class="song" v-if="item.saleStatus !== 3 && item.saleStatus !== 4">
                  <h1 class="rl-bg-gray-sm rl-text-white rl-text-xxss rl-margin-zero rl-tc">失效</h1>
                </td>
                <td v-else-if="item.canBuy > 0 && items.orderType === 1" class="spe">
                  <span
                    class="cursor-pointer"
                    :class="{'gou':item.gou === 1 && Number(item.canBuy) > 0 }"
                    @click="chooseGou(items,items.totalPrice,items.totalCount,item,item.gou,item.canBuy)"
                  ></span>
                </td>
                <td v-else-if="items.orderType === 2 || items.orderType === 3" class="spe">
                  <span
                    class="cursor-pointer"
                    :class="{'gou':item.gou === 1}"
                    @click="chooseGou(items,items.totalPrice,items.totalCount,item,item.gou)"
                  ></span>
                </td>
                <td v-else class="spe">
                  <span></span>
                </td>
                <td>
                  <div class="shop-img">
                    <img width="100%" :src="item.imageUrl1" alt />
                  </div>
                </td>
                <td>{{item.itemCode}}</td>
                <td v-if="item.saleStatus !== 3 && item.saleStatus !== 4">{{item.itemName}}</td>
                <td
                  v-else
                  @click="goDoodsDetail(item.goodsId,items.promotionRule)"
                  class="cursor-pointer"
                >{{item.itemName}}</td>
                <td>{{item.barCode}}</td>
                <td>￥{{item.salePrice}}</td>
                <td
                  v-if="item.discount !== null"
                >￥{{(item.salePrice * (item.discount / 100 )).toFixed(2)}}</td>
                <td
                  v-else-if="item.discount === null && item.specialPrice !== null"
                >￥{{item.specialPrice}}</td>
                <td v-else>--</td>
                <td v-if="item.saleStatus !== 3 && item.saleStatus !== 4">{{item.num}}</td>
                <td
                  class="rl-text-orange-sm"
                  v-else-if="item.canBuy <= 0 && items.orderType === 1"
                >没有库存</td>
                <td v-else-if="item.itemType === 3 && item.diyList">
                  <div
                    class="buy-sum rl-clear rl-margin-left-xxxs"
                    @click="lookDiyItem(item,item.diyList)"
                  >
                    <div class="rl-fl buyac buya rl-text-gray">-</div>
                    <div class="rl-fl buyb">
                      <input class="rl-tc" type="text" v-model="item.num" readonly />
                    </div>
                    <div class="rl-fl buyac buyc rl-text-gray">+</div>
                  </div>
                </td>
                <td v-else class="rl-relative">
                  <div class="rl-margin-left-xxxs">
                    <shopQiu
                      ref="shopQiu"
                      :items="item"
                      :itemsCount="items"
                      @limits="handleLimit"
                      @changePresent="changePresent"
                    ></shopQiu>
                  </div>
                  <div class="rl-margin-top-xxxs stock" v-if="items.orderType === 1">
                    <span>{{item.numInWarehouse}}</span>
                    <i>/</i>
                    <span>{{item.stockItemCount}}</span>
                  </div>
                </td>
                <td
                  class="rl-text-orange-sm"
                  v-if="item.canBuy > 0 || items.orderType === 2 || items.orderType === 3"
                >￥{{item.salePrice*item.num | keepTwoNum}}</td>
                <td class="rl-text-orange-sm" v-else>￥0</td>
                <td class="cursor-pointer rl-text-xxs">
                  <span
                    @click="lookDiyImage(item,item.diyList)"
                    v-if="item.diyList"
                    class="rl-margin-right-xxs"
                  >查看图片</span>
                  <span @click="deleteShopCarItems(item.id,item,items)">删除</span>
                </td>
              </tr>
              <tr class="rl-bdb-gray-sm" v-for="item in items.presentItems" :key="item.id">
                <td class="song">
                  <h1 class="rl-bg-orange-mm rl-text-white rl-text-xxss rl-margin-zero rl-tc">赠品</h1>
                </td>
                <td>
                  <div class="shop-img">
                    <img width="100%" :src="item.imageUrl1" alt />
                  </div>
                </td>
                <td>{{item.itemCode}}</td>
                <td @click="goDoodsDetail(item.goodsId)" class="cursor-pointer">{{item.itemName}}</td>
                <td>{{item.barCode}}</td>
                <td>￥{{item.salePrice}}</td>
                <td>--</td>
                <td>{{item.num}}</td>
                <td class="rl-text-orange-sm">￥{{item.salePrice*item.num | keepTwoNum}}</td>
                <td>--</td>
              </tr>
            </tbody>
          </table>
        </div>
        <div
          class="shop-operate rl-clear rl-padding-right-double rl-padding-left-default rl-padding-top-xxxs rl-padding-bottom-xxxs rl-bdb-gray-sm rl-bg-white"
        >
          <!-- <div class="rl-fl downroad rl-tc rl-text-white rl-bg-blue-xs rl-text-mid cursor-pointer">商品清单下载</div>-->
          <div class="rl-fr operate">
            <div
              @click="updateShopCar"
              class="rl-fl rl-text-mid rl-margin-right-double cursor-pointer"
            >更新购物车</div>
            <div
              @click="batchDelete"
              class="rl-fl rl-text-mid rl-text-orange-sm cursor-pointer"
            >批量删除</div>
          </div>
        </div>
        <div class="shop-foot rl-padding-left-double rl-bg-white">
          <div class="rl-fl">
            <div
              @click="goWalkingAround"
              class="rl-fl rl-text-mid rl-margin-right-double cursor-pointer"
            >继续购物</div>
            <div
              @click="emptyShopCar"
              class="rl-fl rl-text-mid rl-text-orange-sm cursor-pointer"
            >清空购物车</div>
          </div>
          <div class="rl-fl rl-margin-left-double">
            <div class="rl-padding-left-double">
              商品总重：
              <span>{{totalWeight | keepTwoNum}}克</span>，此笔商品总计：
              <span>（商品数量：{{allTotalCount}}）</span>
              <span class="rl-text-orange-sm">￥{{allTotalPrice | keepTwoNum}}</span>
            </div>
          </div>
          <div
            @click="goTheOrder"
            class="rl-fr settle rl-bg-orange-mm rl-tc rl-text-white rl-text-sm cursor-pointer"
            v-if="this.limit === true"
          >去结账</div>
          <div class="rl-fr settle rl-bg-orange-mm rl-tc rl-text-white rl-text-sm" v-else>去结账</div>
        </div>
      </div>
      <!--查看满赠商品-->
      <div class="cover" v-if="showcov"></div>
      <div class="pro-cover cover-box rl-padding-bottom-default rl-relative" v-if="showcov">
        <!--<span @click="shutLog" class="shut cursor-pointer"></span>-->
        <div
          class="rl-bg-green_xs rl-padding-left-default rl-padding-bottom-xxxs rl-padding-top-xxxs"
        >可领取最多{{maxSongSum}}件，已选{{hasChoose}}件</div>
        <div
          class="shop-table rl-padding-left-default rl-padding-right-default"
          :class="{'scroll-y': this.showSongGoodsList.length > 5}"
        >
          <table>
            <tbody>
              <tr v-for="item in showSongGoodsList" :key="item.id">
                <td width="120px" class="rl-text-xxs">{{item.itemCode}}</td>
                <td width="130px" class="rl-tc">
                  <div class="songImg rl-margin-zero">
                    <img width="100%" :src="item.imgUrl" alt />
                  </div>
                </td>
                <td width="130px" class="rl-text-xxs">{{item.itemName}}</td>
                <td width="130px">
                  <songSum
                    ref="songSum"
                    :songShop="item"
                    :maxSongSum="maxSongSum"
                    :hasChoose="hasChoose"
                  ></songSum>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="btn rl-margin-zero">
          <span
            @click="confirmSongShop"
            class="rl-tc rl-bg-blue-mm rl-text-white rl-margin-right-default cursor-pointer"
          >确定</span>
          <span @click="shutLog" class="rl-tc rl-bg-gray-sm rl-text-white cursor-pointer">取消</span>
        </div>
      </div>
      <!--购物车为空-->
      <div
        class="empty-car rl-margin-zero"
        v-if="this.shopCartIdList === null || this.shopCartIdList === undefined || this.shopCartIdList.length === 0 || this.showCar === false"
      >
        <div class="empty-car-img rl-margin-zero">
          <img width="100%" src="../assets/images/empty-car.png" alt />
        </div>
        <p
          class="rl-tc rl-margin-top-default rl-margin-bottom-default rl-text-sm rl-text-gray"
        >购物车空空如也</p>
        <div class="go-around rl-margin-zero">
          <span
            @click="walkingAround"
            class="rl-bd-gray-sm rl-tc rl-text-mid rl-text-gray cursor-pointer"
          >到处逛逛</span>
        </div>
      </div>
    </div>
    <!--查看diy图片-->
    <el-dialog class="diy-all" title :visible.sync="diyDialogVisible">
      <div
        class="diy-css max-height300 rl-padding-top-default rl-padding-left-double rl-padding-left-double rl-clear"
      >
        <div class="banner rl-fl">
          <el-carousel
            trigger="click"
            :loop="falseState"
            :autoplay="falseState"
            height="414px"
            arrow="always"
            indicator-position="none"
          >
            <el-carousel-item v-for="item in diyImages" :key="item.id">
              <div class="banner-img cursor-pointer">
                <img :src="item.diyPic + '?x-oss-process=image/resize,h_200,l_400'" alt />
              </div>
              <div class="rl-tc rl-text-base sum">x{{item.diyNum}}</div>
            </el-carousel-item>
          </el-carousel>
        </div>
      </div>
      <div slot="footer" class="dialog-footer rl-tc">
        <el-button @click="diyDialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
    <!--删除diy货品-->
    <el-dialog class="alls" title="删除货品" :visible.sync="deleteDiyDialogVisible">
      <div
        class="diy-css rl-bdt-gray-sm max-height300 rl-padding-left-default rl-padding-right-default rl-clear"
      >
        <table>
          <thead>
            <tr>
              <th class="rl-padding-top-xxs rl-padding-bottom-xxs diy-gou"></th>
              <th class="rl-padding-top-xxs rl-padding-bottom-xxs">图片</th>
              <th class="rl-padding-top-xxs rl-padding-bottom-xxs">数量</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in deleteDiyItem" :key="item.id">
              <td width="100px" class="diy-gou">
                <span
                  class="cursor-pointer"
                  :class="{'gou':item.gou === 1}"
                  @click="chooseDiyGou(item,item.gou)"
                ></span>
              </td>
              <td width="250px" class="rl-tc">
                <div class="diy-delete-Img rl-margin-zero">
                  <img :src="item.diyPic" alt />
                </div>
              </td>
              <td width="250px" class="rl-tc">x{{item.diyNum}}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div slot="footer" class="dialog-footer rl-margin-top-xxxs">
        <div class="rl-fl dialog-footer-div">
          <span class="cursor-pointer" :class="{'gou':diyAll === true}" @click="selectDiyAll">全选</span>
        </div>
        <el-button @click="deleteDiyDialogVisible = false">取 消</el-button>
        <el-button @click="confirmDeleteDiyItem" type="danger">确定删除</el-button>
      </div>
    </el-dialog>
    <!--查看diy货品-->
    <el-dialog class="alls" title="调整数量" :visible.sync="lookDiyDialogVisible">
      <div
        class="diy-css rl-bdt-gray-sm max-height300 rl-padding-left-default rl-padding-right-default rl-clear"
      >
        <table>
          <thead>
            <tr>
              <th class="rl-padding-top-xxs rl-padding-bottom-xxs">图片</th>
              <th class="rl-padding-top-xxs rl-padding-bottom-xxs">数量</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in adjustDiyItem" :key="item.id">
              <td width="300px" class="rl-tc">
                <div class="diy-delete-Img rl-margin-zero">
                  <img :src="item.diyPic" alt />
                </div>
              </td>
              <td width="300px" class="rl-tc">
                <el-input-number v-model="item.diyNum" :min="1" label="描述文字" size="mini"></el-input-number>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div slot="footer" class="dialog-footer rl-margin-top-xxxs">
        <el-button @click="lookDiyDialogVisible = false">取 消</el-button>
        <el-button @click="confirmAdjustDiyItem" type="primary">确 定</el-button>
      </div>
    </el-dialog>
    <!--加载动画-->
    <loading v-if="this.showLoading === true"></loading>
  </div>
</template>

<script>
import Vue from "vue";
import { MessageBox } from "element-ui";
import Header from "@/components/Header.vue";
import shopQiu from "@/components/shopQiu.vue";
import songSum from "@/components/songSum.vue";
import loading from "@/components/loading.vue";
import GD from "@/assets/js/globalData";
export default {
  name: "ShopCar",
  components: {
    Header,
    shopQiu,
    songSum,
    loading,
  },
  data() {
    return {
      falseState: false,
      fullscreenLoading: false,
      diyImages: [],
      diyDialogVisible: false,
      deleteDiyDialogVisible: false,
      lookDiyDialogVisible: false,
      showLoading: false, // 展示加载动画
      activityType: 0, // 普通页面页跳商品详情
      activityState: 0, // 活动状态 1满减 2满赠
      userState: 2,
      userId: "",
      ids: {},
      shopCartIdList: [], // 购物车商品列表
      presentCountMap: {},
      maxSongSum: 0,
      commonShopCartList: [], // 未参加活动的商品
      reduceShopCartList: [], // 满减活动的商品
      songShopCartList: [], // 满赠活动的商品
      songGoodsList: [], // 查看赠品列表
      deleteSongGoodsList: [], // 删除赠品列表
      showSong: true, // 展示赠品(勾选)
      showSongGoodsList: [],
      lookSongId: "", // 查看赠品的id
      showChooseSongGoodsList: [], // 展示赠品(购物车)
      songCount: 0, // 赠品数量
      shopCartGoodsList: [],
      all: true, // 控制全选反选
      showcov: false,
      values: 1, // 跳转下单,
      numInWarehouse: "", // 在途在库
      stockItemCount: "",
      limit: true, // 在限制input失焦库存不足去结账
      orderDiscount: false, // 整个订单是否优惠
      orderDiscountMoney: 0,
      orderReduction: 0,
      orderDiscountSum: 0,
      packageMail: false, // 是否包邮
      gradeDiscounts: [], // 等级折扣
      gradeType: false, // 判断是否有等级折扣
      orderDiscountRule: {}, // 订单折扣
      pickUpRateRule: {}, // 提货增长返利折扣
      pickUpType: false, // 判断是否有提货增长返利
      showDiscount: true, // 订单折扣描述显隐
      showCar: true, // 整个购物车显隐
      Network: true, // 网络状态
      limitRule: false, // 限购判断
      limitPurchase: [], // 限购商品列表--普通
      limitCustomizedPurchase: [], // 限购商品列表-定制
      limitPresellPurchase: [], // 限购商品列表-预售
      distributeList: [], // 配送方式列表
      songOrderType: 0, // 0普通订单，1预售订单 2定制订单
      orderAgain: 0, // 是否来自再下一单
      orderItem: [], // 来自再下一单货品
      onWayAttendEventFlag: 0, // 在途商品是否参与活动 1.参与，0.不参与
      customizedAttendEventFlag: 0, // 定制商品是否参与活动 1.参与，0.不参与
      mtoAttendEventFlag: 0, // mto订单是否参与活动 1.参与，0.不参与
      deleteDiyId: "",
      diyAll: true,
      deleteDiyItem: [],
      adjustDiyCarId: "",
      adjustDiyItem: [],
      langList: GD.langList, // 语种列表
      lang: "zh-RMB", // 语种
    };
  },
  filters: {
    keepTwoNum(value) {
      value = Number(value);
      return value.toFixed(2);
    },
    keepTwoHead(value) {
      return value.substr(0, 2);
    },
    deleteFivHead(value) {
      return value.slice(5);
    },
  },
  computed: {
    hasChoose() {
      var total = 0;
      this.showSongGoodsList.forEach((item) => {
        if (item.num) {
          total += Number(item.num);
        }
      });
      return total;
    },
    totalWeight() {
      var total = 0;
      this.shopCartIdList.forEach((goods) => {
        goods.items.forEach((item, index) => {
          if (
            ((item.canBuy > 0 && item.orderType === 1) ||
              item.orderType === 2 ||
              item.orderType === 3) &&
            (item.saleStatus === 3 || item.saleStatus === 4) &&
            item.gou === 1
          ) {
            total += Number(item.weight * item.num);
          }
        });
      });
      return total;
    },
    totalCount() {
      // 普通商品（全场优惠）
      var total = 0;
      this.shopCartIdList.forEach((goods) => {
        goods.items.forEach((item, index) => {
          if (
            ((item.canBuy > 0 && item.orderType === 1) ||
              (item.orderType === 2 && this.mtoAttendEventFlag === 1) ||
              (item.orderType === 3 && this.customizedAttendEventFlag === 1)) &&
            (item.saleStatus === 3 || item.saleStatus === 4) &&
            item.gou === 1
          ) {
            total += Number(item.num);
          }
        });
      });
      return total;
    },
    allTotalCount() {
      // 全部商品（总数）
      var total = 0;
      this.shopCartIdList.forEach((goods) => {
        goods.items.forEach((item, index) => {
          if (
            ((item.canBuy > 0 && item.orderType === 1) ||
              item.orderType === 2 ||
              item.orderType === 3) &&
            (item.saleStatus === 3 || item.saleStatus === 4) &&
            item.gou === 1
          ) {
            total += Number(item.num);
          }
        });
      });
      return total;
    },
    totalPrice() {
      // 普通商品（全场优惠）
      var total = 0;
      this.shopCartIdList.forEach((goods) => {
        goods.items.forEach((item, index) => {
          if (
            ((item.canBuy > 0 && item.orderType === 1) ||
              (item.orderType === 2 && this.mtoAttendEventFlag === 1) ||
              (item.orderType === 3 && this.customizedAttendEventFlag === 1)) &&
            (item.saleStatus === 3 || item.saleStatus === 4) &&
            item.gou === 1
          ) {
            total += item.salePrice * item.num;
          }
        });
      });
      return total;
    },
    allTotalPrice() {
      // 全部商品（总数）
      var total = 0;
      this.shopCartIdList.forEach((goods) => {
        goods.items.forEach((item, index) => {
          if (
            ((item.canBuy > 0 && item.orderType === 1) ||
              item.orderType === 2 ||
              item.orderType === 3) &&
            (item.saleStatus === 3 || item.saleStatus === 4) &&
            item.gou === 1
          ) {
            total += item.salePrice * item.num;
          }
        });
      });
      return total;
    },
  },
  methods: {
    lookDiyImage(item, imgList) {
      // 查看diy图片
      if (Number(this.orderAgain) === 1) {
        this.shopCartIdList.forEach((items) => {
          // 再下一单过来的商品和购物车列表商品
          items.items.forEach((obj, index) => {
            if (item.itemId === obj.itemId) {
              this.diyDialogVisible = true;
              this.diyImages = obj.diyList;
            }
          });
        });
      } else {
        this.diyDialogVisible = true;
        this.diyImages = imgList;
      }
    },
    lookDiyItem(item, itemList) {
      // 查看diy货品
      if (Number(this.orderAgain) === 1) {
        // 再来一单
        this.shopCartIdList.forEach((items) => {
          // 再下一单过来的商品和购物车列表商品
          items.items.forEach((obj, index) => {
            if (item.itemId === obj.itemId) {
              this.lookDiyDialogVisible = true;
              this.adjustDiyCarId = item.id;
              this.adjustDiyItem = obj.diyList;
            }
          });
        });
      } else {
        this.lookDiyDialogVisible = true;
        this.adjustDiyCarId = item.id;
        this.adjustDiyItem = itemList;
      }
    },
    chooseDiyGou(item, gou) {
      let type = 0;
      if (gou === 1) {
        item.gou = 0;
      } else {
        item.gou = 1;
      }
      for (let i = 0; i < this.deleteDiyItem.length; i++) {
        if (this.deleteDiyItem[i].gou === 0) {
          // 未全选
          this.diyAll = false;
          type = 1;
          return;
        }
      }
      if (type === 0) {
        this.diyAll = true; // 全选
      } else {
        this.diyAll = false; // 未全选
      }
    },
    selectDiyAll() {
      if (this.diyAll === true) {
        // 反选
        this.diyAll = false;
        this.deleteDiyItem.forEach((item) => {
          item.gou = 0;
        });
      } else {
        this.diyAll = true;
        this.deleteDiyItem.forEach((item) => {
          item.gou = 1;
        });
      }
    },
    confirmDeleteDiyItem() {
      // 删除diy货品
      let ids = "";
      let temp = [];
      let totolCount = 0;
      if (Number(this.orderAgain) === 1) {
        // 再来一单diy定制商品
        this.deleteDiyItem.forEach((item) => {
          if (item.gou === 0) {
            // 删除
            temp.push(item);
            totolCount += item.diyNum;
          }
        });
        this.deleteDiyItem = temp;
        this.shopCartIdList.forEach((items) => {
          items.items.forEach((item, index) => {
            if (item.id === this.deleteDiyId) {
              if (this.deleteDiyItem.length === 0) {
                items.items.splice(index, 1); // 删除diy商品
              } else {
                item.num = totolCount;
                item.diyList = this.deleteDiyItem;
              }
            }
          });
        });
        this.$message.success(this.$t("ShopCarts.SuccessfullyDeleted"));
        this.deleteDiyDialogVisible = false;
      } else {
        this.deleteDiyItem.forEach((item) => {
          if (item.gou === 1) {
            ids = ids + "," + item.diyId;
          }
        });
        ids = ids.substr(1); // 删除字符串第一位
        if (ids === "") {
          this.$message.warning("请勾选货品!");
          return false;
        }
        if (this.diyAll === true) {
          temp = { ids: this.deleteDiyId };
        } else {
          temp = { ids: this.deleteDiyId, diyIds: ids };
        }
        this.$api.delete(this, "user/u/shoppingCart", temp).then((res) => {
          if (res.code === 0) {
            this.$message.success(this.$t("ShopCarts.SuccessfullyDeleted"));
            this.deleteDiyDialogVisible = false;
            this.getShopCarList();
          }
        });
      }
    },
    confirmAdjustDiyItem() {
      let totolCount = 0;
      if (Number(this.orderAgain) === 1) {
        // 再来一单
        this.adjustDiyItem.forEach((item) => {
          totolCount += item.diyNum;
        });
        this.shopCartIdList.forEach((items) => {
          items.items.forEach((item, index) => {
            if (item.id === this.adjustDiyCarId) {
              item.diyList = this.adjustDiyItem;
              item.num = totolCount;
            }
          });
        });
        this.lookDiyDialogVisible = false;
      } else {
        this.adjustDiyItem.forEach((item) => {
          totolCount += item.diyNum;
        });
        this.$api
          .put(this, "user/u/shoppingCart", {
            id: this.adjustDiyCarId,
            num: totolCount,
            diyList: this.adjustDiyItem,
          })
          .then((res) => {
            if (res.code === 0) {
              this.lookDiyDialogVisible = false;
              this.getShopCarList();
            }
          });
      }
    },
    handleLimit(target) {
      // 监听，字组件input自定义事件
      this.limit = target;
    },
    changePresent(target) {
      // 监听，购物车满赠数量变更
      if (target === 2) {
        this.getShopCarList();
      }
    },
    // 赠品数量加减
    handleAdd() {
      this.songCount++;
    },
    handleReduce() {
      if (this.songCount > 0) {
        this.songCount--;
      }
    },
    // 选中购物车商品
    chooseGou(items, totalPrice, totalCount, item, gou, canBuy) {
      console.log(item);
      if (canBuy <= 0 && item.orderType === 1) {
        item.gou = 0;
        items.totalPrice = totalPrice - item.salePrice * item.num;
        items.totalCount = totalCount - item.num;
      } else {
        if (gou === 1) {
          if (
            (item.canBuy > 0 && item.orderType === 1) ||
            item.orderType === 2 ||
            item.orderType === 3
          ) {
            // 普通商品,预售商品和定制商品
            item.gou = 0;
            if (this.onWayAttendEventFlag === 0) {
              // 在途不参与活动
              if (items.gradeDiscountTotalPrice) {
                // 折扣活动后计算商品总价
                items.gradeDiscountTotalPrice =
                  items.gradeDiscountTotalPrice -
                  item.salePrice * item.numInWarehouse * (item.discount / 100);
              }
              if (
                items.promotionRule === null &&
                items.gradeDiscountsRule === null
              ) {
                // 商品不参与活动
                items.totalPrice = totalPrice - item.salePrice * item.num;
                items.totalCount = totalCount - item.num;
              } else {
                items.totalPrice =
                  totalPrice - item.salePrice * item.numInWarehouse;
                items.totalCount = totalCount - item.numInWarehouse;
              }
            } else {
              items.totalPrice = totalPrice - item.salePrice * item.num;
              items.totalCount = totalCount - item.num;
              if (items.gradeDiscountTotalPrice) {
                // 折扣活动后计算商品总价
                items.gradeDiscountTotalPrice =
                  items.gradeDiscountTotalPrice -
                  item.salePrice * item.num * (item.discount / 100);
              }
            }
            if (items.promotionRule !== null) {
              if (
                (items.totalCount < items.promotionRule.oneBuyCount ||
                  items.totalPrice < items.promotionRule.oneBuyMoney) &&
                items.promotionRule.reduceOrPresent === 2 &&
                items.presentItems.length > 0
              ) {
                this.$api
                  .delete(this, "user/u/shoppingCart/delPresent", {
                    ruleId: items.promotionRule.id,
                  })
                  .then((res) => {
                    if (res.code === 0) {
                      items.presentItems = [];
                    }
                  });
              }
            }
          }
          // else if (item.orderType === 2) { // 定制商品
          //   item.gou = 0
          // } else if (item.orderType === 3) { // 预售商品
          //   item.gou = 0
          // }
        } else {
          if (item.canBuy > 0 && item.orderType === 1) {
            // 普通商品
            item.gou = 1;
            if (this.onWayAttendEventFlag === 0) {
              // 在途不参与活动
              if (
                items.gradeDiscountTotalPrice ||
                items.gradeDiscountTotalPrice === 0
              ) {
                // 折扣活动后计算商品总价
                items.gradeDiscountTotalPrice =
                  items.gradeDiscountTotalPrice +
                  item.salePrice * item.numInWarehouse * (item.discount / 100);
              }
              if (
                items.promotionRule === null &&
                items.gradeDiscountsRule === null
              ) {
                // 商品不参与活动
                items.totalPrice = totalPrice + item.salePrice * item.num;
                items.totalCount = totalCount + item.num;
              } else {
                items.totalPrice =
                  totalPrice + item.salePrice * item.numInWarehouse;
                items.totalCount = totalCount + item.numInWarehouse;
              }
            } else {
              items.totalPrice = totalPrice + item.salePrice * item.num;
              items.totalCount = totalCount + item.num;
              if (
                items.gradeDiscountTotalPrice ||
                items.gradeDiscountTotalPrice === 0
              ) {
                // 折扣活动后计算商品总价
                items.gradeDiscountTotalPrice =
                  items.gradeDiscountTotalPrice +
                  item.salePrice * item.num * (item.discount / 100);
              }
            }
          } else if (item.orderType === 2) {
            // 定制商品
            item.gou = 1;
            items.totalPrice = totalPrice + item.salePrice * item.num;
            items.totalCount = totalCount + item.num;
          } else if (item.orderType === 3) {
            // 预售商品
            item.gou = 1;
            items.totalPrice = totalPrice + item.salePrice * item.num;
            items.totalCount = totalCount + item.num;
          }
        }
      }
      var i = 0;
      if (!this.shopCartIdList) {
        return;
      }
      this.shopCartIdList.forEach((items) => {
        items.items.forEach((item, index) => {
          if (
            item.canBuy > 0 ||
            item.orderType === 2 ||
            items.orderType === 3
          ) {
            if (item.gou === 0) {
              // 未全选
              this.all = false;
              i = 1;
            }
          }
        });
      });
      if (i === 0) {
        this.all = true; // 全选
      } else {
        this.all = false; // 未全选
      }
      // console.log(Items.totalPrice)
    },
    shutLog() {
      this.showcov = false;
    },
    // 继续购物
    goWalkingAround() {
      this.$router.push({ name: "Index" });
    },
    // 更新购物车
    updateShopCar() {
      this.$message({
        type: "success",
        message: "更新成功!",
      });
      this.getShopCarList();
    },
    // 删除购物车货品（单条）
    deleteShopCarItems(id, item, Items) {
      if (item.itemType === 3) {
        // 定制商品
        // if (Number(this.orderAgain) === 1) { // 再来一单删除diy定制
        //   for (let i = 0; i < this.orderItem.length; i++) {
        //     if (item.itemId === this.orderItem[i].itemId) {
        //       this.deleteDiyItem = this.orderItem[i].diyList
        //     }
        //   }
        //   this.deleteDiyId = id
        //   this.deleteDiyDialogVisible = true
        //   this.diyAll = true
        //   this.deleteDiyItem.forEach((item) => {
        //     Vue.set(item, 'gou', 1) // 删除货品勾选
        //   })
        //   return false
        // }
        if (item.diyList) {
          this.deleteDiyId = id;
          this.deleteDiyItem = item.diyList;
          this.deleteDiyDialogVisible = true;
          this.diyAll = true;
          this.deleteDiyItem.forEach((item) => {
            Vue.set(item, "gou", 1); // 删除货品勾选
          });
          return false;
        }
      }
      this.$confirm("此操作将删除该货品, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          var t = 0; // 用于判断全场优惠和订单折扣描述显隐
          var m = 0; // 用于判断购物车有无商品信息显隐
          this.$api
            .delete(this, "user/u/shoppingCart", { ids: id })
            .then((res) => {
              if (res.code === 0) {
                this.$message({
                  type: "success",
                  message: this.$t("ShopCarts.SuccessfullyDeleted"),
                });
                Items.items.forEach((item, index) => {
                  if (item.id === id) {
                    Items.totalPrice =
                      Items.totalPrice - item.salePrice * item.num;
                    Items.totalCount = Items.totalCount - item.num;
                    if (Items.promotionRule !== null) {
                      if (
                        (Items.totalCount < Items.promotionRule.oneBuyCount ||
                          Items.totalPrice < Items.promotionRule.oneBuyMoney) &&
                        Items.promotionRule.reduceOrPresent === 2 &&
                        Items.presentItems !== null
                      ) {
                        this.$api
                          .delete(this, "user/u/shoppingCart/delPresent", {
                            ruleId: Items.promotionRule.id,
                          })
                          .then((res) => {
                            if (res.code === 0) {
                              Items.presentItems = [];
                            }
                          });
                      }
                    }
                    Items.items.splice(index, 1);
                    this.shopCartIdList.forEach((items, index) => {
                      if (items.promotionRule !== null) {
                        // 判断全场优惠和订单折扣描述显隐
                        if (items.items.length > 0) {
                          t = 1;
                        }
                      }
                      if (items.items.length > 0) {
                        // 判断购物车有无商品信息显隐
                        m = 1;
                      }
                    });
                    if (t === 0) {
                      this.showDiscount = false;
                    } else {
                      this.showDiscount = true;
                    }
                    if (m === 0) {
                      this.showCar = false;
                    } else {
                      this.showCar = true;
                    }
                  }
                });
                // console.log(this.shopCartIdList)
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
    // 删除购物车货品（批量）
    batchDelete() {
      var ids = "";
      if (!this.shopCartIdList) {
        return;
      }
      this.shopCartIdList.forEach((goods) => {
        goods.items.forEach((item, index) => {
          if (
            (item.canBuy > 0 && item.orderType === 1) ||
            item.orderType === 2 ||
            item.orderType === 3
          ) {
            if (item.gou === 1) {
              // 购物车商品选择
              ids = ids + "," + item.id;
            }
          }
        });
      });
      ids = ids.substr(1); // 删除字符串第一位
      // console.log(ids)
      if (ids === "") {
        this.$message.warning("请勾选购物车货品!");
        return false;
      }
      this.$confirm(this.$t("ShopCarts.EmptyShopCarts"), this.$t("P.Prompt"), {
        confirmButtonText: this.$t("Message.Confirm"),
        cancelButtonText: this.$t("P.Cancel"),
        type: "warning",
      })
        .then(() => {
          this.$api
            .delete(this, "user/u/shoppingCart", { ids: ids })
            .then((res) => {
              if (res.code === 0) {
                this.$message({
                  type: "success",
                  message: this.$t("ShopCarts.SuccessfullyDeleted"),
                });
                this.getShopCarList();
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
    // 更新购物车赠品
    updatePresentItems() {
      var myDate = new Date();
      this.$api
        .get(
          this,
          "user/u/marketing/promotion/getShoppingCart?" +
            myDate.getMinutes() +
            myDate.getSeconds()
        )
        .then((res) => {
          if (res.code === 0) {
            res.list.forEach((items, index) => {
              // 购物车列表排序
              if (this.lookSongId === items.promotionRule.id) {
              }
            });
          }
        });
    },
    // 购物车货品列表
    getShopCarList() {
      var myDate = new Date();
      var userArr = [];
      var platArr = [];
      var blatArr = [];
      var alatArr = [];
      var clatArr = [];
      var dlatArr = [];
      var flatArr = [];
      this.showLoading = true;
      this.$api
        .get(
          this,
          "user/u/marketing/promotion/getShoppingCart?" +
            myDate.getMinutes() +
            myDate.getSeconds()
        )
        .then((res) => {
          if (res.code === 0) {
            let merge = true; // 用于合并预售标题
            let combine = true; // 用于合并定制标题
            this.showLoading = false;
            this.shopCartIdList = res.list;
            if (this.shopCartIdList.length === 0) {
              this.showLoading = false;
              return false;
            }
            this.shopCartIdList.forEach((items, index) => {
              // 购物车列表排序
              if (index === this.shopCartIdList.length - 1) {
                this.showLoading = false;
              }
              if (items.orderType === 1) {
                // 1.普通订单 2.预售订单 3.定制订单
                if (items.ruleType === 0) {
                  // 0.没有活动，1.促销活动（promotionRule有值），2.商品等级折扣(gradeDiscountsRule有值)
                  userArr.push(items); // 未参加活动的商品
                } else if (items.ruleType === 1) {
                  if (items.promotionRule.reduceOrPresent === 1) {
                    // 满减
                    platArr.push(items);
                  } else if (items.promotionRule.reduceOrPresent === 2) {
                    // 满赠
                    blatArr.push(items);
                  } else if (items.promotionRule.reduceOrPresent === 3) {
                    // 商品折扣
                    flatArr.push(items);
                  }
                } else {
                  // 商品等级折扣
                  dlatArr.push(items);
                }
              } else if (items.orderType === 2) {
                // 预售
                clatArr.push(items);
              } else {
                // 定制
                alatArr.push(items);
              }
            });
            userArr = userArr.concat(platArr); // 未参加活动的商品排序在最前面
            userArr = userArr.concat(blatArr); // 满减活动排在满赠活动前面
            userArr = userArr.concat(flatArr); // 满赠活动排在折扣活动前面
            userArr = userArr.concat(dlatArr); // 折扣活动活动排在定制商品前面
            userArr = userArr.concat(alatArr); // 定制商品排在预售商品前面
            userArr = userArr.concat(clatArr); // 预售商品排在最后面
            this.shopCartIdList = userArr;
            this.orderDiscountRule = res.orderDiscountRule; // 订单活动
            this.onWayAttendEventFlag = res.onWayAttendEventFlag; // 在途商品是否参与活动
            this.customizedAttendEventFlag = res.customizedAttendEventFlag; // 定制商品是否参与活动
            this.mtoAttendEventFlag = res.mtoAttendEventFlag; // mto订单是否参与活动
            this.shopCartIdList.forEach((items, index) => {
              Vue.set(items, "totalCount", 0); // 满赠数量判断
              Vue.set(items, "totalPrice", 0); // 满减金额判断
              Vue.set(items, "presentItemsCount", 0); // 货品赠品数量
              if (items.gradeDiscountsRule !== null) {
                if (items.gradeDiscountsRule.discountBeforeAfter === 2) {
                  Vue.set(items, "gradeDiscountTotalPrice", 0); // 等级折扣满减金额判断
                }
              }
              if (
                items.presentItems !== null &&
                items.presentItems.length > 0
              ) {
                // 当有赠品
                items.presentItems.forEach((item, index) => {
                  items.presentItemsCount += item.num; // 赠品总数
                });
              }
              if (merge === true && items.orderType === 2) {
                Vue.set(items, "presellMark", 1); // 预售标志
                merge = false;
              }
              if (combine === true && items.orderType === 3) {
                Vue.set(items, "customMark", 1); // 定制标志
                combine = false;
              }
              items.items.forEach((item, index) => {
                Vue.set(item, "promotionPrice", ""); // 优惠价
                if (Number(this.orderAgain) === 1) {
                  Vue.set(item, "gou", 0);
                  this.orderItem.forEach((obj) => {
                    if (item.itemId === obj.itemId) {
                      // 再下一单过来的商品和购物车列表商品
                      // this.$api.put(this, 'user/u/shoppingCart', {id: item.id, num: obj.num}).then(res => {})
                      item.num = obj.num; // 购买数量为再下一单的货品数量
                      item.gou = 1;
                      if (item.diyList) {
                        // diy定制
                        item.diyList = obj.diyList;
                      }
                    }
                  });
                } else {
                  Vue.set(item, "gou", 1); // 购物车勾选
                }
                if (items.orderType === 1) {
                  // 普通订单
                  Vue.set(item, "numInWarehouse", 0); // 购买商品的在库数量
                  Vue.set(item, "stockItemCount", 0); // 购买商品的在途数量
                  Vue.set(item, "realnumInWarehouse", 0); // 在库数量（仅仅用于计算）
                  Vue.set(item, "realstockItemCount", 0); // 在库+在途数量（仅仅用于计算）
                  Vue.set(item, "canBuy", 0); // 一共可以购买总数
                  Vue.set(
                    item,
                    "onWayAttendEventFlag",
                    this.onWayAttendEventFlag
                  ); // 在途商品是否参与活动
                  this.$api
                    .get(this, "user/u/warehouse/stock/distributor", {
                      distributorId: this.userId,
                      itemIds: item.itemId,
                    })
                    .then((res) => {
                      // 在途和在库数量
                      if (res.code === 0) {
                        if (res.stockItemCounts.length > 0) {
                          item.realnumInWarehouse =
                            res.stockItemCounts[0].numInWarehouse +
                            res.stockItemCounts[0].numVmi -
                            res.stockItemCounts[0].numLock -
                            res.stockItemCounts[0].numVmiLock -
                            res.stockItemCounts[0].numOnWayLock -
                            res.stockItemCounts[0].numReserved; // 在库
                          item.realstockItemCount =
                            res.stockItemCounts[0].numInWarehouse +
                            res.stockItemCounts[0].numVmi +
                            res.stockItemCounts[0].numOnWay -
                            res.stockItemCounts[0].numLock -
                            res.stockItemCounts[0].numVmiLock -
                            res.stockItemCounts[0].numOnWayLock -
                            res.stockItemCounts[0].numReserved; // 在库+在途
                          if (
                            item.realnumInWarehouse <= 0 &&
                            item.realstockItemCount <= 0
                          ) {
                            item.canBuy = 0;
                          } else {
                            item.canBuy = item.realstockItemCount;
                          }
                          if (item.realnumInWarehouse >= item.num) {
                            // 在库数量大于等于购买数量
                            item.numInWarehouse = item.num;
                            item.stockItemCount = 0;
                          } else if (item.realnumInWarehouse <= 0) {
                            // 在库数量小于等于0
                            item.realnumInWarehouse = 0;
                            item.numInWarehouse = 0;
                            item.stockItemCount = item.num;
                          } else {
                            item.numInWarehouse = item.realnumInWarehouse;
                            item.stockItemCount =
                              item.num - item.realnumInWarehouse;
                          }
                          if (this.onWayAttendEventFlag === 1) {
                            items.totalPrice =
                              items.totalPrice + item.num * item.salePrice; // 在库和在途商品参与活动计算
                            items.totalCount = items.totalCount + item.num;
                            if (items.gradeDiscountsRule !== null) {
                              if (
                                items.gradeDiscountsRule.discountBeforeAfter ===
                                2
                              ) {
                                items.gradeDiscountTotalPrice +=
                                  item.num *
                                  item.salePrice *
                                  (item.discount / 100);
                              }
                            }
                            if (
                              items.promotionRule !== null &&
                              items.presentItemsCount > 0 &&
                              index === items.items.length - 1
                            ) {
                              if (
                                items.promotionRule.oneBuyCount === undefined ||
                                items.promotionRule.oneBuyCount === null
                              ) {
                                if (
                                  items.totalPrice <
                                    items.promotionRule.oneBuyMoney ||
                                  items.presentItemsCount >
                                    parseInt(
                                      items.promotionRule.presentCount *
                                        (items.totalPrice /
                                          items.promotionRule.oneBuyMoney)
                                    )
                                ) {
                                  // 当不满足满赠条件
                                  this.$api
                                    .delete(
                                      this,
                                      "user/u/shoppingCart/delPresent",
                                      { ruleId: items.promotionRule.id }
                                    )
                                    .then((res) => {
                                      // 删除赠品
                                      if (res.code === 0) {
                                        items.presentItems = [];
                                      }
                                    });
                                }
                              } else if (
                                items.promotionRule.oneBuyMoney === undefined ||
                                items.promotionRule.oneBuyMoney === null
                              ) {
                                if (
                                  items.totalCount <
                                    items.promotionRule.oneBuyCount ||
                                  items.presentItemsCount >
                                    parseInt(
                                      items.promotionRule.presentCount *
                                        (items.totalCount /
                                          items.promotionRule.oneBuyCount)
                                    )
                                ) {
                                  // 当不满足满赠条件
                                  this.$api
                                    .delete(
                                      this,
                                      "user/u/shoppingCart/delPresent",
                                      { ruleId: items.promotionRule.id }
                                    )
                                    .then((res) => {
                                      // 删除赠品
                                      if (res.code === 0) {
                                        items.presentItems = [];
                                      }
                                    });
                                }
                              }
                            }
                          } else {
                            items.totalPrice +=
                              item.numInWarehouse * item.salePrice; // 在库商品参与活动计算，在途商品不参与
                            items.totalCount += item.numInWarehouse;
                            if (items.gradeDiscountsRule !== null) {
                              if (
                                items.gradeDiscountsRule.discountBeforeAfter ===
                                2
                              ) {
                                items.gradeDiscountTotalPrice +=
                                  item.numInWarehouse *
                                  item.salePrice *
                                  (item.discount / 100);
                              }
                            }
                            if (
                              items.promotionRule !== null &&
                              items.presentItemsCount > 0 &&
                              index === items.items.length - 1
                            ) {
                              if (
                                items.promotionRule.oneBuyCount === undefined ||
                                items.promotionRule.oneBuyCount === null
                              ) {
                                if (
                                  items.totalPrice <
                                    items.promotionRule.oneBuyMoney ||
                                  items.presentItemsCount >
                                    parseInt(
                                      items.promotionRule.presentCount *
                                        (items.totalPrice /
                                          items.promotionRule.oneBuyMoney)
                                    )
                                ) {
                                  // 当不满足满赠条件
                                  this.$api
                                    .delete(
                                      this,
                                      "user/u/shoppingCart/delPresent",
                                      { ruleId: items.promotionRule.id }
                                    )
                                    .then((res) => {
                                      // 删除赠品
                                      if (res.code === 0) {
                                        items.presentItems = [];
                                      }
                                    });
                                }
                              } else if (
                                items.promotionRule.oneBuyMoney === undefined ||
                                items.promotionRule.oneBuyMoney === null
                              ) {
                                if (
                                  items.totalCount <
                                    items.promotionRule.oneBuyCount ||
                                  items.presentItemsCount >
                                    parseInt(
                                      items.promotionRule.presentCount *
                                        (items.totalCount /
                                          items.promotionRule.oneBuyCount)
                                    )
                                ) {
                                  // 当不满足满赠条件
                                  this.$api
                                    .delete(
                                      this,
                                      "user/u/shoppingCart/delPresent",
                                      { ruleId: items.promotionRule.id }
                                    )
                                    .then((res) => {
                                      // 删除赠品
                                      if (res.code === 0) {
                                        items.presentItems = [];
                                      }
                                    });
                                }
                              }
                            }
                          }
                        }
                      }
                    });
                } else if (items.orderType === 2 || items.orderType === 3) {
                  // 预售或定制订单
                  items.totalPrice += item.num * item.salePrice; // 在库和在途商品参与活动计算
                  items.totalCount += item.num;
                  if (
                    items.promotionRule !== null &&
                    items.presentItemsCount > 0 &&
                    index === items.items.length - 1
                  ) {
                    if (
                      items.promotionRule.oneBuyCount === undefined ||
                      items.promotionRule.oneBuyCount === null
                    ) {
                      if (
                        items.totalPrice < items.promotionRule.oneBuyMoney ||
                        items.presentItemsCount >
                          parseInt(
                            items.promotionRule.presentCount *
                              (items.totalPrice /
                                items.promotionRule.oneBuyMoney)
                          )
                      ) {
                        // 当不满足满赠条件
                        this.$api
                          .delete(this, "user/u/shoppingCart/delPresent", {
                            ruleId: items.promotionRule.id,
                          })
                          .then((res) => {
                            // 删除赠品
                            if (res.code === 0) {
                              items.presentItems = [];
                            }
                          });
                      }
                    } else if (
                      items.promotionRule.oneBuyMoney === undefined ||
                      items.promotionRule.oneBuyMoney === null
                    ) {
                      if (
                        items.totalCount < items.promotionRule.oneBuyCount ||
                        items.presentItemsCount >
                          parseInt(
                            items.promotionRule.presentCount *
                              (items.totalCount /
                                items.promotionRule.oneBuyCount)
                          )
                      ) {
                        // 当不满足满赠条件
                        this.$api
                          .delete(this, "user/u/shoppingCart/delPresent", {
                            ruleId: items.promotionRule.id,
                          })
                          .then((res) => {
                            // 删除赠品
                            if (res.code === 0) {
                              items.presentItems = [];
                            }
                          });
                      }
                    }
                  }
                }
              });
            });
            console.log(this.shopCartIdList);
          } else if (res.code === 3) {
            this.showLoading = false;
            
          }
        })
        .catch((error) => {
          console.log(error);
          this.Network = false;
          this.showLoading = false;
        });
    },
    // 清空购物车
    emptyShopCar() {
      var ids = "";
      if (!this.shopCartIdList) {
        return;
      }
      this.shopCartIdList.forEach((goods) => {
        goods.items.forEach((item, index) => {
          ids = ids + "," + item.id;
        });
        if (goods.presentItems !== null) {
          // 删除赠品
          goods.presentItems.forEach((item, index) => {
            ids = ids + "," + item.id;
          });
        }
      });
      ids = ids.substr(1); // 删除字符串第一位
      this.$confirm(this.$t("ShopCarts.EmptyShopCarts"), this.$t("P.Prompt"), {
        confirmButtonText: this.$t(""),
        cancelButtonText: this.$t("P.Cancel"),
        type: "warning",
      })
        .then(() => {
          this.$api
            .delete(this, "user/u/shoppingCart", { ids: ids })
            .then((res) => {
              if (res.code === 0) {
                this.$message({
                  type: "success",
                  message: this.$t("ShopCarts.SuccessfullyDeleted"),
                });
                this.getShopCarList();
              }
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: this.$t("P.Cancel"),
          });
        });
    },
    // 全选和反选
    selectAll() {
      if (this.all === true) {
        // 反选
        this.all = false;
        if (!this.shopCartIdList) {
          return;
        }
        this.shopCartIdList.forEach((items) => {
          items.totalCount = 0;
          items.totalPrice = 0;
          if (
            items.gradeDiscountTotalPrice ||
            items.gradeDiscountTotalPrice === 0
          ) {
            // 折扣活动后计算商品总价
            items.gradeDiscountTotalPrice = 0;
          }
          if (items.promotionRule !== null) {
            // 删除赠品
            if (
              (items.totalCount < items.promotionRule.oneBuyCount ||
                items.totalPrice < items.promotionRule.oneBuyMoney) &&
              items.promotionRule.reduceOrPresent === 2 &&
              items.presentItems !== null
            ) {
              this.$api
                .delete(this, "user/u/shoppingCart/delPresent", {
                  ruleId: items.promotionRule.id,
                })
                .then((res) => {
                  if (res.code === 0) {
                    items.presentItems = [];
                  }
                });
            }
          }
          items.items.forEach((item, index) => {
            if (
              (item.canBuy > 0 ||
                item.orderType === 2 ||
                item.orderType === 3) &&
              (item.saleStatus === 3 || item.saleStatus === 4)
            ) {
              item.gou = 0; // 未选中
            }
          });
        });
      } else {
        // 全选
        this.all = true;
        if (!this.shopCartIdList) {
          return;
        }
        this.shopCartIdList.forEach((items) => {
          items.items.forEach((item, index) => {
            items.totalPrice = items.totalPrice + item.salePrice * item.num;
            items.totalCount = items.totalCount + item.num;
            if (
              items.gradeDiscountTotalPrice ||
              items.gradeDiscountTotalPrice === 0
            ) {
              // 折扣活动后计算商品总价
              items.gradeDiscountTotalPrice =
                items.gradeDiscountTotalPrice +
                item.salePrice * item.num * (item.discount / 100);
            }
            if (
              (item.canBuy > 0 ||
                item.orderType === 2 ||
                item.orderType === 3) &&
              (item.saleStatus === 3 || item.saleStatus === 4)
            ) {
              item.gou = 1; // 全选中
            }
          });
        });
      }
    },
    // 去下单
    goTheOrder() {
      this.showLoading = true;
      var tempArray = []; // 临时数组 非定制商品
      var spesTempArray = []; // 临时数组 定制商品
      var spesPresellArray = []; // 临时数组 预售商品
      var tempArrayTwo = []; // 临时数组
      var hasGou = 0; // 判断是否有勾选商品
      // var adjustSum = true // 调整购物车数量
      var temp = false;
      var presellState = true;
      var lengths = this.shopCartIdList.length;
      if (!this.shopCartIdList) {
        return;
      }
      // console.log(this.shopCartIdList)
      // console.log(this.showChooseSongGoodsList)
      // console.log(this.shopCartIdList.length)
      this.shopCartIdList.forEach((items, index) => {
        if (index === Number(lengths) - 1) {
          temp = true;
        }
        if (items.presentItems) {
          // 赠品
          items.presentItems.forEach((item, index) => {
            tempArrayTwo.push(item);
          });
          this.showChooseSongGoodsList = tempArrayTwo;
        }
        if (items.orderType === 1) {
          // 普通订单
          items.items.forEach((item, index) => {
            if (
              item.gou === 1 &&
              item.canBuy > 0 &&
              (item.saleStatus === 3 || item.saleStatus === 4)
            ) {
              hasGou = 1;
              if (
                item.num > item.lookNumInWarehouse &&
                item.num <= item.lookStockItemCount
              ) {
                // adjustSum = false
                // this.showLoading = false
                this.$message.warning(
                  "货号" +
                    item.itemCode +
                    "的商品在库存库存仅剩" +
                    item.lookNumInWarehouse +
                    ",不足数量自动为您选择在途库存"
                );
              } else if (item.num > item.canBuy) {
                // adjustSum = false
                // this.showLoading = false
                item.num = item.canBuy;
                item.lookStockItemCount = item.num - item.lookNumInWarehouse;
                this.$message.warning(
                  "货号" +
                    item.itemCode +
                    "的商品在库存库存仅剩" +
                    item.lookNumInWarehouse +
                    ",已自动为您调整数量"
                );
                this.$api
                  .put(this, "user/u/shoppingCart", {
                    id: item.id,
                    num: item.num,
                  })
                  .then((res) => {
                    if (res.code === 0) {
                      this.getShopCarList();
                    }
                  });
              }
              var limit = {
                goodsId: item.goodsId,
                itemId: item.itemId,
                itemCount: item.num,
                itemType: 1,
              };
              this.limitPurchase.push(limit);
              tempArray.push(item);
            }
          });
        } else if (items.orderType === 3) {
          // 定制商品
          items.items.forEach((item, index) => {
            if (
              item.gou === 1 &&
              (item.saleStatus === 3 || item.saleStatus === 4)
            ) {
              hasGou = 1;
              var limitCustomized = {
                goodsId: item.goodsId,
                itemId: item.itemId,
                itemCount: item.num,
                itemType: 3,
              };
              this.limitCustomizedPurchase.push(limitCustomized);
              spesTempArray.push(item);
            }
          });
        } else if (items.orderType === 2) {
          // 预售商品
          items.items.forEach((item, index) => {
            if (
              item.gou === 1 &&
              (item.saleStatus === 3 || item.saleStatus === 4)
            ) {
              hasGou = 1;
              var limitCustomized = {
                goodsId: item.goodsId,
                itemId: item.itemId,
                itemCount: item.num,
                itemType: 1,
              };
              this.limitPresellPurchase.push(limitCustomized);
              spesPresellArray.push(item);
            }
          });
        }
      });
      if (hasGou === 1 && temp === true) {
        var secondsArea = 2;
        window.localStorage.removeItem("goodsItems");
        window.localStorage.removeItem("shopCatShop");
        window.localStorage.removeItem("spesShopCatShop");
        window.localStorage.removeItem("songGoodsShop");
        window.localStorage.removeItem("presellShop");
        if (
          (tempArray.length > 0 && spesTempArray.length > 0) ||
          (tempArray.length > 0 && spesPresellArray.length > 0) ||
          (spesTempArray.length > 0 && spesPresellArray.length > 0)
        ) {
          MessageBox.confirm(
            "普通商品、定制商品和预售商品不允许同时下单，请分开下单",
            {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning",
            }
          );
          // this.$message.warning('普通商品和定制商品不允许同时下单，请分开下单')
          this.showLoading = false;
          return false;
          // this.postList(t)
        } else if (
          tempArray.length > 0 &&
          spesTempArray.length <= 0 &&
          spesPresellArray.length <= 0
        ) {
          // 普通商品
          this.$api
            .post(this, "user/u/order/restriction", {
              goods: this.limitPurchase,
            })
            .then((res) => {
              // 限购规则判断
              if (res.code === 0) {
                this.limitRule = true;
                this.postList(1);
              } else if (res.code === 3) {
               
              } else {
                this.showLoading = false;
                this.limitPurchase = [];
              }
            });
        } else if (
          tempArray.length <= 0 &&
          spesTempArray.length > 0 &&
          spesPresellArray.length <= 0
        ) {
          // 定制商品
          this.$api
            .post(this, "user/u/order/restriction", {
              customizedGoods: this.limitCustomizedPurchase,
            })
            .then((res) => {
              // 限购规则判断
              if (res.code === 0) {
                this.limitRule = true;
                this.postList(2);
              } else if (res.code === 3) {
                
              } else {
                this.showLoading = false;
                this.limitCustomizedPurchase = [];
              }
            });
        } else if (
          tempArray.length <= 0 &&
          spesTempArray.length <= 0 &&
          spesPresellArray.length > 0
        ) {
          // 预售商品
          this.$api
            .post(this, "user/u/order/restriction", {
              goods: this.limitPresellPurchase,
            })
            .then((res) => {
              // 限购规则判断
              if (res.code === 0) {
                this.limitRule = true;
                this.postList(1);
                spesPresellArray.forEach((item, index) => {
                  if (item.advanceSaleFlag === 0) {
                    this.$message.warning(
                      "预售商品" + item.itemCode + "不支持预售，请重新购买"
                    );
                    this.showLoading = false;
                    presellState = false;
                  } else if (item.num < item.moq) {
                    this.$message.warning(
                      "预售商品" + item.itemCode + "订购数量不可少于" + item.moq
                    );
                    presellState = false;
                    this.showLoading = false;
                    item.num = item.moq;
                  }
                });
              } else if (res.code === 3) {
              
              } else {
                this.showLoading = false;
                this.limitPresellPurchase = [];
              }
            });
        }
        let self = this;
        this.intervalid = setInterval(() => {
          secondsArea--;
          if (
            secondsArea === 0 &&
            self.limitRule === true &&
            presellState === true
          ) {
            if (self.Network === false) {
              // 网络异常
              self.showLoading = false;
              return false;
            }
            if (self.distributeList.length === 0 && tempArray.length > 0) {
              MessageBox.confirm(
                "普通商品订单当前未配置配送方式，无法下单，请联系管理员",
                {
                  confirmButtonText: "确定",
                  cancelButtonText: "取消",
                  type: "warning",
                }
              );
              self.showLoading = false;
              return false;
            } else if (
              self.distributeList.length === 0 &&
              spesTempArray.length > 0
            ) {
              MessageBox.confirm(
                "定制商品订单当前未配置配送方式，无法下单，请联系管理员",
                {
                  confirmButtonText: "确定",
                  cancelButtonText: "取消",
                  type: "warning",
                }
              );
              self.showLoading = false;
              return false;
            } else if (
              self.distributeList.length === 0 &&
              spesPresellArray.length > 0
            ) {
              MessageBox.confirm(
                "预售商品订单当前未配置配送方式，无法下单，请联系管理员",
                {
                  confirmButtonText: "确定",
                  cancelButtonText: "取消",
                  type: "warning",
                }
              );
              self.showLoading = false;
              return false;
            }
            self.showLoading = false;
            window.localStorage.setItem(
              "shopCatShop",
              JSON.stringify(tempArray)
            ); // 普通商品
            window.localStorage.setItem(
              "spesShopCatShop",
              JSON.stringify(spesTempArray)
            ); // 定制商品
            window.localStorage.setItem(
              "presellShop",
              JSON.stringify(spesPresellArray)
            ); // 预售商品
            window.localStorage.setItem(
              "songGoodsShop",
              JSON.stringify(self.showChooseSongGoodsList)
            ); // 赠品
            if (spesPresellArray.length > 0) {
              // 预售商品跳转
              self.$router.push({
                name: "ConsigneeInfor",
                query: { values: self.values, goodsType: 4 },
              });
            } else if (tempArray.length > 0) {
              // 普通商品跳转
              self.$router.push({
                name: "ConsigneeInfor",
                query: { values: self.values, goodsType: 1 },
              });
            } else if (spesTempArray.length > 0) {
              // 定制商品跳转
              self.$router.push({
                name: "ConsigneeInfor",
                query: { values: self.values, goodsType: 2 },
              });
            }
            clearInterval(this.intervalid);
          }
        }, 1000);
      } else if (hasGou !== 1 || temp === false) {
        this.$message.warning("结算前请勾选商品");
        this.showLoading = false;
      }
    },
    // 配送方式列表
    postList(type) {
      var myDate = new Date();
      this.$api
        .get(
          this,
          "user/u/distribution/list?" +
            myDate.getMinutes() +
            myDate.getSeconds(),
          { useRange: type }
        )
        .then((res) => {
          if (res.code === 0) {
            this.distributeList = res.distributions;
            this.Network = true;
          } else if (res.code === 3) {
            this.Network = false;
           
          } else {
            this.Network = false;
          }
        })
        .catch((error) => {
          console.log(error);
          this.Network = false;
        });
    },
    // 到处逛逛
    walkingAround() {
      this.$router.push({ name: "Index" });
    },
    // 去商品详情
    goDoodsDetail(id, rule) {
      let routeData = null;
      if (rule === null) {
        // 普通商品
        this.activityType = 0;
        routeData = this.$router.resolve({
          name: "ShopDetail",
          query: {
            good_id: id,
            activityType: this.activityType,
            goodsType: 1,
            accessType: 0,
          },
        });
      } else if (rule === 1) {
        // 定制商品
        routeData = this.$router.resolve({
          name: "ShopDetail",
          query: { good_id: id, goodsType: 2, accessType: 0 },
        });
      } else {
        // 参与活动商品
        this.activityType = 1;
        routeData = this.$router.resolve({
          name: "ShopDetail",
          query: {
            good_id: id,
            activityType: this.activityType,
            goodsType: 1,
            accessType: 0,
          },
        });
      }
      window.open(routeData.href, "_blank");
    },
    // 查看赠品
    lookSong(id, items) {
      this.showLoading = true;
      this.lookSongId = id;
      this.songOrderType = items.orderType;
      if (
        items.promotionRule.oneBuyMoney !== null &&
        items.promotionRule.oneBuyCount === null
      ) {
        // 按金额满赠
        this.maxSongSum = parseInt(
          items.promotionRule.presentCount *
            (items.totalPrice / items.promotionRule.oneBuyMoney)
        );
      } else if (
        items.promotionRule.oneBuyMoney === null &&
        items.promotionRule.oneBuyCount !== null
      ) {
        // 按数量满赠
        this.maxSongSum = parseInt(
          items.promotionRule.presentCount *
            (items.totalCount / items.promotionRule.oneBuyCount)
        );
      }
      this.showcov = true;
      var tempArray = []; // 临时数组
      var tempArrayTwo = []; // 临时数组
      this.$api
        .get(this, "user/u/marketing/promotion/getPresentGoods", {
          ruleId: this.lookSongId,
        })
        .then((res) => {
          if (res.code === 0) {
            this.songGoodsList = res.goods;
            if (!this.songGoodsList) {
              return false;
            }
            this.songGoodsList.forEach((items, index) => {
              this.$api
                .get(this, "user/goods/items", { ids: items.itemId })
                .then((res) => {
                  if (res.code === 0) {
                    tempArrayTwo = res.goodsList;
                    if (!tempArrayTwo) {
                      return;
                    }
                    tempArrayTwo.forEach((temp) => {
                      var numInWarehouse = ""; // 在库
                      var stockItemCount = ""; // 在途
                      this.$api
                        .get(this, "user/u/warehouse/stock/distributor", {
                          distributorId: this.userId,
                          itemIds: items.itemId,
                        })
                        .then((res) => {
                          if (res.code === 0) {
                            if (res.stockItemCounts.length > 0) {
                              numInWarehouse =
                                res.stockItemCounts[0].numInWarehouse +
                                res.stockItemCounts[0].numVmi -
                                res.stockItemCounts[0].numLock -
                                res.stockItemCounts[0].numVmiLock -
                                res.stockItemCounts[0].numOnWayLock -
                                res.stockItemCounts[0].numReserved; // 在库
                              stockItemCount =
                                res.stockItemCounts[0].numInWarehouse +
                                res.stockItemCounts[0].numVmi +
                                res.stockItemCounts[0].numOnWay -
                                res.stockItemCounts[0].numLock -
                                res.stockItemCounts[0].numVmiLock -
                                res.stockItemCounts[0].numOnWayLock -
                                res.stockItemCounts[0].numReserved; // 在库+在途
                              if (numInWarehouse <= 0) {
                                numInWarehouse = 0;
                              }
                              if (stockItemCount <= 0) {
                                stockItemCount = 0;
                              }
                            } else {
                              numInWarehouse = 0;
                              stockItemCount = 0;
                            }
                            temp.items.forEach((obj, index) => {
                              if (items.itemId === obj.id) {
                                Vue.set(obj, "num", 0);
                                var product = {
                                  itemCode: items.itemCode,
                                  itemName: obj.itemName,
                                  presentCount: items.presentCount,
                                  numInWarehouse: numInWarehouse,
                                  stockItemCount: numInWarehouse,
                                  goodsId: items.goodsId,
                                  itemId: items.itemId,
                                  ruleId: id,
                                  imgUrl:
                                    this.$i18n.locale === "zh" ||
                                    !temp.goods.imageUrl1en == true
                                      ? temp.goods.imageUrl1
                                      : temp.goods.imageUrl1en,
                                  goodsName: temp.goods.goodsName,
                                  specificationValueId:
                                    obj.specificationValueId,
                                  specificationValueName:
                                    obj.specificationValueName,
                                  boxNum: obj.boxNum,
                                  barCode: obj.barCode,
                                  weight: obj.weight,
                                  num: obj.num,
                                  defaultPrice: obj.defaultPrice,
                                  promotionPrice: obj.promotionPrice,
                                  salePrice: obj.salePrice,
                                };
                                tempArray.push(product);
                              }
                            });
                          }
                        });
                    });
                  }
                  if (index === this.songGoodsList.length - 1) {
                    this.showLoading = false;
                  }
                });
            });
          }
        });
      this.showSongGoodsList = tempArray;
      // console.log(this.showSongGoodsList)
    },
    // 去凑单
    pieceTogether(id, reduceOrPresent) {
      if (reduceOrPresent === 1) {
        this.activityState = 1;
        this.$router.push({
          name: "PieceTogether",
          query: { ruleId: id, activityState: this.activityState },
        });
      } else if (reduceOrPresent === 2) {
        this.activityState = 2;
        this.$router.push({
          name: "PieceTogether",
          query: { ruleId: id, activityState: this.activityState },
        });
      } else {
        this.$router.push({ name: "DiscountActivity" });
      }
    },
    // 提交确认赠品
    confirmSongShop() {
      if (this.maxSongSum < this.hasChoose) {
        this.$message({
          type: "warning",
          message: "赠品数量已达上限!",
        });
        return false;
      }
      var tempArray = []; // 临时数组
      var secondsArea = 1;
      // console.log(this.showSongGoodsList)
      if (
        this.showSongGoodsList !== null &&
        this.showSongGoodsList !== "" &&
        this.showSongGoodsList !== undefined &&
        this.showSongGoodsList.length !== 0
      ) {
        this.showSongGoodsList.forEach((obj) => {
          if (obj.num !== undefined && obj.num !== 0) {
            var product = {
              ruleId: obj.ruleId,
              goodsId: Number(obj.goodsId),
              itemId: obj.itemId,
              num: Number(obj.num),
              itemType: 2,
              orderType: this.songOrderType,
            };
            tempArray.push(product);
          }
        });
        if (tempArray.length === 0) {
          this.$message.warning("赠品数量不能为空");
          return false;
        } else {
          if (!this.shopCartIdList) {
            return false;
          }
          this.shopCartIdList.forEach((item) => {
            // 提交赠品之前先删除原先该规则下勾选赠品
            if (item.presentItems !== null && item.presentItems.length !== 0) {
              if (item.promotionRule.id === this.lookSongId) {
                item.presentItems.forEach((shop, index) => {
                  this.$api
                    .delete(this, "user/u/shoppingCart", { ids: shop.id })
                    .then((res) => {
                      if (res.code === 0) {
                      }
                    });
                });
              }
            }
          });
          this.showLoading = true;
          this.intervalid = setInterval(() => {
            secondsArea--;
            if (secondsArea === 0) {
              this.showLoading = false;
              this.$api
                .post(this, "user/u/shoppingCarts", { shoppingCars: tempArray })
                .then((res) => {
                  if (res.code === 0) {
                    this.$message.success(this.$t("ShopCarts.SubmitGiveaway"));
                    this.getShopCatSongShop(); // 更新该活动规则下购物车的赠品
                    this.showcov = false;
                    this.showSong = true;
                  }
                });
              clearInterval(this.intervalid);
            }
          }, 800);
        }
      }
    },
    // 购物车赠品更新
    getShopCatSongShop() {
      var myDate = new Date();
      this.$api
        .get(
          this,
          "user/u/marketing/promotion/getShoppingCart?" +
            myDate.getMinutes() +
            myDate.getSeconds()
        )
        .then((res) => {
          if (res.code === 0) {
            for (var i = 0; i < res.list.length; i++) {
              let b = false; // 用于跳出循环
              for (var j = 0; j < this.shopCartIdList.length; j++) {
                if (
                  res.list[i].promotionRule !== null &&
                  this.shopCartIdList[j].promotionRule !== null
                ) {
                  if (
                    this.lookSongId === res.list[i].promotionRule.id &&
                    this.lookSongId ===
                      this.shopCartIdList[j].promotionRule.id &&
                    this.songOrderType === res.list[i].orderType &&
                    this.songOrderType === this.shopCartIdList[j].orderType
                  ) {
                    this.$forceUpdate();
                    this.shopCartIdList[j].presentItems =
                      res.list[i].presentItems;
                    this.shopCartIdList[j].presentItemsCount = 0; // 改规则下的赠品货品数量清零
                    this.shopCartIdList[j].presentItems.forEach((item) => {
                      // 改规则下的赠品货品数量
                      this.shopCartIdList[j].presentItemsCount += item.num;
                    });
                    b = true;
                    break;
                  }
                }
              }
              if (b) {
                break;
              }
            }
          } 
        });
    },
    theSort(property) {
      return function (a, b) {
        var value1 = a[property];
        var value2 = b[property];
        return value1 - value2;
      };
    },
  },
  created() {
    var id = window.localStorage.getItem("userId");
    let list = localStorage.getItem("orderItem");
    if (list === "" || list === undefined || list === null || list === "null") {
      // console.log(list)
    } else {
      this.orderItem = JSON.parse(list);
    }
    this.orderAgain = this.$route.query.orderAgain;
    this.userId = id;
    this.getShopCarList();
  },
};
</script>
<style lang='less'>
.el-dialog__wrapper {
  .el-dialog {
    width: 600px;
    .el-dialog__header {
      text-align: center;
    }
    .el-dialog__body {
      padding: 0;
      padding-bottom: 10px;
    }
    .el-dialog__footer {
      text-align: center;
    }
  }
}
</style>
<style scoped="scoped" lang='less'>
@import url("../assets/less/variable.less");
.user {
  width: 100%;
}
.diy-delete-Img {
  width: 50px;
  img {
    margin-top: 5px;
    height: 50px;
  }
}
.dialog-footer-div {
  padding-left: 9px;
  span {
    padding-left: 20px;
    display: block;
    width: 30px;
    height: 45px;
    line-height: 45px;
    background: url("../../src/assets/images/choose.png") no-repeat left center;
  }
  span.gou {
    background: url("../../src/assets/images/gou.png") no-repeat left center;
  }
}
.diy-gou {
  span {
    display: block;
    width: 30px;
    height: 45px;
    background: url("../../src/assets/images/choose.png") no-repeat center
      center;
  }
  span.gou {
    background: url("../../src/assets/images/gou.png") no-repeat center center;
  }
}
.diy-all {
  .diy-css {
    .banner {
      width: 515px;
      position: relative;
      .banner-img {
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 0;
        img {
          height: 397px;
        }
      }
      .el-carousel {
        z-index: 0;
      }
    }
  }
}
.shop-car {
  width: 1190px;
  .bleed {
    padding-left: 55px;
    .el-breadcrumb {
      font-size: 12px;
    }
  }
}
.user-main {
  padding-bottom: 40px;
  width: 1190px;
  .shopcar-activity {
    padding-top: 5px;
    padding-left: 20px;
    margin-bottom: 10px;
    background-color: #e5f9fb;
    .shopcar-activity-img {
      margin-right: 10px;
      font-size: 22px;
      color: @orange;
    }
    .shopCart-activity-state {
      font-size: 13px;
      line-height: 20px;
    }
  }
  .shopcar-table {
    width: 100%;
    table {
      width: 1190px;
      word-wrap: break-word;
      word-break: break-all;
      border-collapse: collapse;
      tr {
        th,
        td.spe {
          span {
            display: block;
            width: 30px;
            height: 45px;
            background: url("../../src/assets/images/choose.png") no-repeat
              center center;
          }
          span.gou {
            background: url("../../src/assets/images/gou.png") no-repeat center
              center;
          }
        }
        th {
          height: 45px;
          line-height: 45px;
          text-align: center;
          background-color: #00c9dc;
          font-size: 13px;
          .item {
            padding: 0;
            border: 0;
            background-color: #00c9dc;
            color: #000;
            font-weight: bold;
            font-size: 12px;
          }
          div {
            height: 30px;
            line-height: 30px;
            font-size: 12px;
          }
        }
        td {
          height: 80px;
          max-height: 80px;
          text-align: center;
          .stock {
            width: 100%;
            span {
              display: inline-block;
              width: 60px;
              text-align: center;
            }
          }
          .shop-img {
            width: 59px;
            img {
              width: 100%;
              height: 59px;
            }
          }
          .activity {
            .btn {
              min-width: 50px;
              padding-left: 5px;
              padding-right: 5px;
              border-radius: 5px;
            }
            div {
              height: 20px;
              line-height: 20px;
            }
          }
        }
        td.spc-td {
          padding-top: 15px;
          padding-bottom: 15px;
          height: 0;
          .btn {
            min-width: 50px;
            padding-left: 5px;
            padding-right: 5px;
            border-radius: 5px;
          }
        }
        td.dis-td {
          height: 0;
        }
        td.spc-tdsp {
          padding-top: 10px;
          padding-bottom: 0;
        }
        td.spc-tds {
          padding-bottom: 10px;
          height: 0;
        }
        td.song {
          height: 40px;
          max-height: 40px;
        }
        .song {
          h1 {
            width: 22px;
            height: 50px;
            line-height: 25px;
            border-radius: 5px;
            word-wrap: break-word;
          }
        }
      }
    }
  }
  .shop-operate {
    .downroad {
      width: 140px;
      height: 35px;
      line-height: 35px;
    }
    .operate {
      height: 35px;
      line-height: 35px;
    }
  }
  .shop-foot {
    height: 50px;
    line-height: 50px;
    .settle {
      width: 125px;
      height: 50px;
      line-height: 50px;
    }
  }
}
.empty-car {
  .empty-car-img {
    margin-top: 120px;
    width: 125px;
  }
  .go-around {
    width: 130px;
    span {
      display: block;
      width: 130px;
      height: 40px;
      line-height: 40px;
      border-radius: 5px;
    }
  }
}
/*弹框*/
.cover {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: #000;
  z-index: 99;
  opacity: 0.5;
}
.pro-cover {
  width: 555px;
  border: 1px solid #ccc;
  border-radius: 5px;
  z-index: 99;
  background: #fefefe;
  .shop-table {
    margin-bottom: 20px;
    max-height: 300px;
    table {
      tr {
        td {
          height: 45px;
          text-align: center;
          border-bottom: 1px solid #ccc;
          .buy-sum {
            width: 92px;
            height: 22px;
            line-height: 22px;
            border: 1px solid #ebeff5;
            div {
              height: 22px;
              box-sizing: border-box;
              background-color: #fff;
              input {
                width: 62px;
              }
            }
            .buyac {
              width: 22px;
              font-size: 22px;
              color: #9b9b9b;
              cursor: pointer;
              text-align: center;
            }
            .buyb {
              width: 48px;
              line-height: 22px;
              color: #3a3a3a;
              border-left: 1px solid #ebeff5;
              border-right: 1px solid #ebeff5;
              input {
                width: 46px;
              }
            }
          }
          .songImg {
            width: 50px;
            img {
              height: 50px;
            }
          }
        }
      }
    }
  }
}
.cover-box {
  box-sizing: border-box;
  position: fixed;
  top: 40%;
  left: 0;
  right: 0;
  margin: auto;
  z-index: 99;
  .shut {
    position: absolute;
    top: -8px;
    right: -8px;
    display: block;
    width: 18px;
    height: 18px;
    background: url("../../src/assets/images/shut.png") no-repeat center center;
  }
  .btn {
    width: 180px;
    span {
      display: inline-block;
      width: 70px;
      height: 30px;
      line-height: 30px;
      border-radius: 5px;
    }
  }
}
.scroll-y {
  overflow-y: scroll;
}
.wen-hao1 {
  display: block;
  width: 14px;
  height: 14px;
  background: url("../../src/assets/images/step/wen-hao.png") center center
    no-repeat;
}
.wen-hao2 {
  top: 0;
  right: -15px;
}
.buy-sum {
  div {
    height: 25px;
    box-sizing: border-box;
    border: 1px solid #b6b6b6;
  }
  .buyac {
    width: 25px;
    line-height: 20px;
    font-size: 24px;
    background-color: #eeeeee;
    text-align: center;
  }
  .buyb {
    width: 68px;
    line-height: 25px;
    margin-left: -1px;
    margin-right: -1px;
    input {
      width: 68px;
    }
  }
}
</style>
