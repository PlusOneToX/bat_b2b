<template>
  <div>
    <div class="user rl-margin-zero">
      <!--公共头部-Y-C-->
      <Header
        :userState="userState"
        :loginState="login_state"
        ref="header"
      ></Header>
      <div v-if="showDetail === true" class="nav-bottom rl-margin-zero">
        <div class="rl-text-xxs bleed">
          <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ name: 'Index' }">{{
              $t("P.HomePage")
            }}</el-breadcrumb-item>
            <el-breadcrumb-item>
              <span
                v-show="
                  $i18n.locale === 'zh' || !goodsDetail.goodsNameEn == true
                "
                >{{ goodsDetail.goodsName }}</span
              >
              <span v-show="$i18n.locale === 'en'">{{
                goodsDetail.goodsNameEn
              }}</span>
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
      </div>
      <!--主内容-->
      <div
        v-if="showDetail === true"
        class="mainOne rl-bg-white rl-clear rl-margin-zero"
      >
        <div class="product-all rl-clear">
          <!-- 商品图片-Y-C -->
          <div class="pic-box rl-fl">
            <div class="pic-move rl-relative">
              <pic-zoom :url="shopImg" :scale="3" :scroll="true"></pic-zoom>
            </div>
            <div class="pic-silde rl-clear">
              <div class="silde-img rl-fl" v-if="goodsDetail">
                <div
                  v-if="
                    $i18n.locale === 'zh' || !goodsDetail.imageUrl1en == true
                  "
                  class="rl-fl img-margin-right-xxxs"
                >
                  <!-- <div v-if="!goodsDetail.imageUrl1en == true" class="rl-fl img-margin-right-xxxs"> -->
                  <div
                    v-if="goodsDetail.imageUrl1 !== ''"
                    @click="chooseImg(goodsDetail.imageUrl1, 0)"
                    class="item"
                  >
                    <img
                      :class="{ 'rl-bd-blue-xs': colorBlue === 0 }"
                      width="100%"
                      :src="
                        goodsDetail.imageUrl1 +
                        '?x-oss-process=image/resize,h_104,l_104'
                      "
                    />
                  </div>
                </div>
                <div v-else class="rl-fl img-margin-right-xxxs">
                  <div
                    v-if="
                      goodsDetail.imageUrl1en !== undefined &&
                      goodsDetail.imageUrl1en !== null &&
                      goodsDetail.imageUrl1en !== ''
                    "
                    @click="chooseImg(goodsDetail.imageUrl1en, 0)"
                    class="item rl-fl"
                  >
                    <img
                      :class="{ 'rl-bd-blue-xs': colorBlue === 0 }"
                      width="100%"
                      :src="
                        goodsDetail.imageUrl1en +
                        '?x-oss-process=image/resize,h_104,l_104'
                      "
                    />
                  </div>
                </div>
                <div
                  v-if="
                    $i18n.locale === 'zh' ||
                    (!goodsDetail.imageUrl2en == true &&
                      !goodsDetail.imageUrl1en == true)
                  "
                  class="rl-fl img-margin-right-xxxs"
                >
                  <div
                    v-if="goodsDetail.imageUrl2 && goodsDetail.imageUrl2 !== ''"
                    @click="chooseImg(goodsDetail.imageUrl2, 1)"
                    class="item rl-fl"
                  >
                    <img
                      :class="{ 'rl-bd-blue-xs': colorBlue === 1 }"
                      width="100%"
                      :src="
                        goodsDetail.imageUrl2 +
                        '?x-oss-process=image/resize,h_104,l_104'
                      "
                    />
                  </div>
                </div>
                <div v-else class="rl-fl img-margin-right-xxxs">
                  <div
                    v-if="
                      goodsDetail.imageUrl2en !== undefined &&
                      goodsDetail.imageUrl2en !== null &&
                      goodsDetail.imageUrl2en !== ''
                    "
                    @click="chooseImg(goodsDetail.imageUrl2en, 1)"
                    class="item rl-fl"
                  >
                    <img
                      :class="{ 'rl-bd-blue-xs': colorBlue === 1 }"
                      width="100%"
                      :src="
                        goodsDetail.imageUrl2en +
                        '?x-oss-process=image/resize,h_104,l_104'
                      "
                    />
                  </div>
                </div>
                <div
                  v-if="
                    $i18n.locale === 'zh' ||
                    (!goodsDetail.imageUrl3en == true &&
                      !goodsDetail.imageUrl1en == true)
                  "
                  class="rl-fl img-margin-right-xxxs"
                >
                  <div
                    v-if="goodsDetail.imageUrl3 && goodsDetail.imageUrl3 !== ''"
                    @click="chooseImg(goodsDetail.imageUrl3, 2)"
                    class="item rl-fl"
                  >
                    <img
                      :class="{ 'rl-bd-blue-xs': colorBlue === 2 }"
                      width="100%"
                      :src="
                        goodsDetail.imageUrl3 +
                        '?x-oss-process=image/resize,h_104,l_104'
                      "
                    />
                  </div>
                </div>
                <div v-else class="rl-fl img-margin-right-xxxs">
                  <div
                    v-if="
                      goodsDetail.imageUrl3en !== undefined &&
                      goodsDetail.imageUrl3en !== null &&
                      goodsDetail.imageUrl3en !== ''
                    "
                    @click="chooseImg(goodsDetail.imageUrl3en, 2)"
                    class="item rl-fl"
                  >
                    <img
                      :class="{ 'rl-bd-blue-xs': colorBlue === 2 }"
                      width="100%"
                      :src="
                        goodsDetail.imageUrl3en +
                        '?x-oss-process=image/resize,h_104,l_104'
                      "
                    />
                  </div>
                </div>
                <div
                  v-if="
                    $i18n.locale === 'zh' ||
                    (!goodsDetail.imageUrl4en == true &&
                      !goodsDetail.imageUrl1en == true)
                  "
                  class="rl-fl img-margin-right-xxxs"
                >
                  <div
                    v-if="goodsDetail.imageUrl4 && goodsDetail.imageUrl4 !== ''"
                    @click="chooseImg(goodsDetail.imageUrl4, 3)"
                    class="item rl-fl"
                  >
                    <img
                      :class="{ 'rl-bd-blue-xs': colorBlue === 3 }"
                      width="100%"
                      :src="
                        goodsDetail.imageUrl4 +
                        '?x-oss-process=image/resize,h_104,l_104'
                      "
                    />
                  </div>
                </div>
                <div v-else class="rl-fl img-margin-right-xxxs">
                  <div
                    v-if="
                      goodsDetail.imageUrl4en !== undefined &&
                      goodsDetail.imageUrl4en !== null &&
                      goodsDetail.imageUrl4en !== ''
                    "
                    @click="chooseImg(goodsDetail.imageUrl4en, 3)"
                    class="item rl-fl"
                  >
                    <img
                      :class="{ 'rl-bd-blue-xs': colorBlue === 3 }"
                      width="100%"
                      :src="
                        goodsDetail.imageUrl4en +
                        '?x-oss-process=image/resize,h_104,l_104'
                      "
                    />
                  </div>
                </div>
                <div
                  v-if="
                    $i18n.locale === 'zh' ||
                    (!goodsDetail.imageUrl5en == true &&
                      !goodsDetail.imageUrl1en == true)
                  "
                  class="rl-fl img-margin-right-xxxs"
                >
                  <div
                    v-if="goodsDetail.imageUrl5 && goodsDetail.imageUrl5 !== ''"
                    @click="chooseImg(goodsDetail.imageUrl5, 4)"
                    class="item rl-fl"
                  >
                    <img
                      :class="{ 'rl-bd-blue-xs': colorBlue === 4 }"
                      width="100%"
                      :src="
                        goodsDetail.imageUrl5 +
                        '?x-oss-process=image/resize,h_104,l_104'
                      "
                    />
                  </div>
                </div>
                <div v-else class="rl-fl img-margin-right-xxxs-xxxs">
                  <div
                    v-if="
                      goodsDetail.imageUrl5en !== undefined &&
                      goodsDetail.imageUrl5en !== null &&
                      goodsDetail.imageUrl5en !== ''
                    "
                    @click="chooseImg(goodsDetail.imageUrl5en, 4)"
                    class="item rl-fl"
                  >
                    <img
                      :class="{ 'rl-bd-blue-xs': colorBlue === 4 }"
                      width="100%"
                      :src="
                        goodsDetail.imageUrl5en +
                        '?x-oss-process=image/resize,h_104,l_104'
                      "
                    />
                  </div>
                </div>
                <div
                  v-if="
                    $i18n.locale === 'zh' ||
                    (!goodsDetail.imageUrl6en == true &&
                      !goodsDetail.imageUrl1en == true)
                  "
                  class="rl-fl"
                >
                  <div
                    v-if="goodsDetail.imageUrl6 && goodsDetail.imageUrl6 !== ''"
                    @click="chooseImg(goodsDetail.imageUrl6, 5)"
                    class="item rl-fl"
                  >
                    <img
                      :class="{ 'rl-bd-blue-xs': colorBlue === 5 }"
                      width="100%"
                      :src="
                        goodsDetail.imageUrl6 +
                        '?x-oss-process=image/resize,h_104,l_104'
                      "
                    />
                  </div>
                </div>
                <div v-else class="rl-fl">
                  <div
                    v-if="
                      goodsDetail.imageUrl6en !== undefined &&
                      goodsDetail.imageUrl6en !== null &&
                      goodsDetail.imageUrl6en !== ''
                    "
                    @click="chooseImg(goodsDetail.imageUrl6en, 5)"
                    class="item rl-fl"
                  >
                    <img
                      :class="{ 'rl-bd-blue-xs': colorBlue === 5 }"
                      width="100%"
                      :src="
                        goodsDetail.imageUrl6en +
                        '?x-oss-process=image/resize,h_104,l_104'
                      "
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- 右边商品信息 -->
          <div class="text-box rl-fl">
            <!-- Y-C -->
            <div class="head rl-clear">
              <!-- 商品名称 -->
              <div class="rl-fl goodsName">
                <span
                  v-show="
                    $i18n.locale === 'zh' || !goodsDetail.goodsNameEn == true
                  "
                  >{{ goodsDetail.goodsName }}</span
                >
                <span v-show="$i18n.locale === 'en'">{{
                  goodsDetail.goodsNameEn
                }}</span>
              </div>
              <!-- 是否收藏商品 -->
              <div
                class="rl-fr collection"
                @click="collectSetFun('POST', goodsDetail.id)"
                v-if="!this.userId == '' && goodsDetail.collectionFlag == 0"
              >
                <i class="el-icon-star-off"></i>
                {{ $t("P.Collect") }}
                <span style="text-align: center"></span>
              </div>
              <div
                class="rl-fr collection"
                @click="collectSetFun('DELETE', goodsDetail.id)"
                v-if="!this.userId == '' && goodsDetail.collectionFlag === 1"
              >
                <i class="el-icon-star-on"></i>
                {{ $t("P.Collect") }}
                <span style="text-align: center"></span>
              </div>
            </div>
            <!-- 商品介绍-Y-c -->
            <div class="introduce">
              <span v-show="$i18n.locale === 'zh'">{{
                goodsDetail.introduce
              }}</span>
              <span v-show="$i18n.locale === 'en'">{{
                goodsDetail.introduceEn
              }}</span>
            </div>

            <div class="content rl-relative">
              <!-- 拼团-Y-C -->
              <div
                class="group-detail"
                :class="{ green: item.groupSeckillStatus == 0 }"
                v-for="(item, index) in groupGoodList"
                :key="item.id"
                :data-index="index"
                v-show="isGroupIndex == item.itemId"
              >
                <div class="group-title rl-clear">
                  <span class="rl-fl name">{{
                    $t("GroupBuy.GroupBuying")
                  }}</span>
                  <!--拼团抢新-->
                  <div class="rl-fl count-down">
                    <i class="el-icon-time red"></i>
                    <p v-if="$i18n.locale === 'zh'">
                      <span v-if="item.status === 0">距拼团开始:</span>
                      <span v-if="item.status === 1">距拼团结束:</span>
                      <span class="red">{{
                        Math.floor(item.diff / (24 * 3600 * 1000)) > 0
                          ? Math.floor(item.diff / (24 * 3600 * 1000))
                          : 0
                      }}</span>
                      <span>天</span>
                      <span class="red">{{
                        fixNum(Math.floor((item.diff / (1000 * 60 * 60)) % 24))
                      }}</span>
                      <span>时</span>
                      <span class="red">{{
                        fixNum(Math.floor((item.diff / (1000 * 60)) % 60))
                      }}</span>
                      <span>分</span>
                      <span class="red">{{
                        fixNum(Math.floor((item.diff / 1000) % 60))
                      }}</span>
                      <span>秒</span>
                    </p>
                    <p v-else>
                      <span v-if="item.status === 0">Start after</span>
                      <span class="red">{{
                        "" +
                        (Math.floor(item.diff / (24 * 3600 * 1000)) > 0
                          ? Math.floor(item.diff / (24 * 3600 * 1000))
                          : 0)
                      }}</span>
                      <span>{{
                        Math.floor(item.diff / (24 * 3600 * 1000)) === 1
                          ? " day "
                          : " days "
                      }}</span>
                      <span class="red">{{
                        fixNum(Math.floor((item.diff / (1000 * 60 * 60)) % 24))
                      }}</span>
                      <span>:</span>
                      <span class="red">{{
                        fixNum(Math.floor((item.diff / (1000 * 60)) % 60))
                      }}</span>
                      <span>:</span>
                      <span class="red">{{
                        fixNum(Math.floor((item.diff / 1000) % 60))
                      }}</span>
                      <span v-if="item.status === 1">Left</span>
                    </p>
                  </div>
                  <div class="rl-fl surplus" v-if="item.maxNum > 0">
                    <span v-if="$i18n.locale === 'zh'"
                      >剩余{{ item.itemSurplusNumLast }}件</span
                    >
                    <span v-else>{{ item.itemSurplusNumLast }} units left</span>
                  </div>
                  <span class="rl-fr more" @click="goGroupBuying()">
                    {{ $i18n.locale === "zh" ? "更多活动" : "More" }}
                    <i class="el-icon-arrow-right"></i>
                  </span>
                </div>
                <div class="group-con">
                  <!-- 拼团价 -->
                  <div class="group-list">
                    <span class="title">{{ $t("GroupBuy.Price") }}：</span>
                    <span class="current">
                      {{
                        $i18n.locale === "en" && $root.currency === "USD"
                          ? "$"
                          : "¥"
                      }}
                      <i class="red">{{
                        $i18n.locale === "en" && $root.currency === "USD"
                          ? (Number(item.groupSeckillPrice) * exchange).toFixed(
                              2
                            )
                          : item.groupSeckillPrice
                      }}</i>
                    </span>
                    <span class="origin"
                      >{{
                        $i18n.locale === "en" && $root.currency === "USD"
                          ? "$"
                          : "¥"
                      }}{{
                        $i18n.locale === "zh"
                          ? item.salePrice
                          : (Number(item.salePrice) * exchange).toFixed(2)
                      }}</span
                    >
                  </div>
                  <div class="group-list">
                    <!-- 起拼量 -->
                    <span class="title">{{ $t("GroupBuy.MinOQ") }}：</span>
                    {{
                      item.minNum
                        ? item.minNum + " " + $t("GroupBuy.Units")
                        : $t("GroupBuy.Limited")
                    }}
                    <!-- 限购量 -->
                    <span
                      class="title rl-margin-left-double rl-padding-left-double"
                      >{{ $t("GroupBuy.MaxOQ") }}：</span
                    >
                    {{
                      item.maxNum
                        ? item.maxNum + " " + $t("GroupBuy.Units")
                        : $t("GroupBuy.Limited")
                    }}
                  </div>
                  <!-- 规则-暂时注释 -->
                  <div class="group-list rules" v-if="item.cnExplain">
                    <span class="title">{{ $t("GroupBuy.Rules") }}：</span>
                    <template v-if="$i18n.locale === 'zh'">
                      <p v-for="(rule, idx) in item.cnExplain" :key="idx">
                        {{ rule }}
                      </p>
                    </template>
                    <template v-else>
                      <p v-for="(rule, idx) in item.overseasExplain" :key="idx">
                        {{ rule }}
                      </p>
                    </template>
                  </div>
                </div>
              </div>

              <!-- 阶梯价-Y-C -->
              <div
                class="item rl-clear"
                v-if="!userId == '' && ladderPriceList.length > 0"
              >
                <div class="rl-clear rl-fl">
                  <!-- 价格 -->
                  <div
                    class="title"
                    style="margin-bottom: 5px; margin-top: 6px"
                  >
                    {{ $t("P.Price") + "：" }}
                  </div>
                  <!-- 起批量 -->
                  <div class="title">{{ $t("P.BatchQuantity") }}：</div>
                </div>
                <div v-for="(item, index) in ladderPriceList" :key="item.id">
                  <div
                    class="rl-clear rl-fl"
                    style="margin-right: 90px; text-align: center"
                  >
                    <div
                      style="margin-bottom: 5px"
                      v-if="$i18n.locale === 'en' && $root.currency === 'USD'"
                    >
                      <span class="currency" :class="item.isReach ? 'bold' : ''"
                        >$</span
                      >
                      <span class="money" :class="item.isReach ? 'bold' : ''">{{
                        (Number(item.price) * exchange).toFixed(2)
                      }}</span>
                    </div>
                    <div style="margin-bottom: 5px" v-else>
                      <span class="currency" :class="item.isReach ? 'bold' : ''"
                        >¥</span
                      >
                      <span class="money" :class="item.isReach ? 'bold' : ''">{{
                        Number(item.price).toFixed(2)
                      }}</span>
                    </div>
                    <div
                      v-if="
                        goodsItems[selectIndex].jieTiList[promotionsIndex]
                          .rules[0].moneyOrCount == 2
                      "
                    >
                      <!--按数量打折-->
                      <span
                        class="count"
                        :class="item.oneBuyCount ? 'bold' : ''"
                        v-if="
                          index <
                          goodsItems[selectIndex].jieTiList[promotionsIndex]
                            .rules[0].conditions.length -
                            1
                        "
                        >{{ item.oneBuyCount }}-{{
                          goodsItems[selectIndex].jieTiList[promotionsIndex]
                            .rules[0].conditions[index + 1].oneBuyCount - 1
                        }}</span
                      >
                      <span
                        class="count"
                        :class="item.oneBuyCount ? 'bold' : ''"
                        v-else
                        >{{ item.oneBuyCount }}+</span
                      >
                    </div>
                    <div v-else>
                      <!--按金额减免-->
                      <span
                        class="count"
                        :class="item.oneBuyMoney ? 'bold' : ''"
                        >{{ item.oneBuyMoney }}</span
                      >
                    </div>
                  </div>
                </div>
              </div>

              <!-- 优惠/未参与拼团-Y -->
              <div
                class="preferential rl-clear"
                v-if="
                  !this.userId == '' &&
                  promotionsList.length > 0 &&
                  ladderPriceList.length == 0 &&
                  groupGoodList.length == 0
                "
              >
                <div class="rl-clear rl-fl title">{{ $t("P.Discount") }}：</div>
                <div v-if="!(promotionsList.length > 0)">
                  <!-- 本商品暂无活动 -->
                  <div class="rl-clear rl-fl noActivity">
                    {{ $t("P.NoActivity") }}
                  </div>
                  <!-- 去活动专区 -->
                  <div
                    class="rl-clear rl-fl goActivity"
                    @click="lookActivityDetail(null)"
                  >
                    {{ $t("P.ActivityArea") }}
                    <i class="el-icon-arrow-right"></i>
                  </div>
                </div>
                <div v-else style="position: relative" class="rl-clear">
                  <div class="rl-clear rl-fl" style="height: 54px">
                    <img
                      style="width: 95px; hight: 54px"
                      v-if="$i18n.locale === 'zh'"
                      src="../../src/assets/images/activity.png"
                      alt
                    />
                    <img
                      style="width: 95px; hight: 54px"
                      v-else
                      src="../../src/assets/images/activity_en.png"
                      alt
                    />
                  </div>
                  <div class="rl-clear rl-fl activityName">
                    {{
                      $i18n.locale === "zh" || !promotionsList[0].nameEn == true
                        ? promotionsList[0].name
                        : promotionsList[0].nameEn
                    }}
                  </div>
                  <div
                    class="rl-clear rl-fl CollectBills"
                    tabindex="0"
                    hidefocus="true"
                    @click="showAllPromotions()"
                    @focus="promotionsListFocus"
                    @blur="promotionsFocus = false"
                  >
                    {{ $t("ShopCarts.GoAdd") }}
                    <i v-if="!showPromotions" class="el-icon-arrow-down"></i>
                    <i v-else class="el-icon-arrow-up"></i>
                  </div>
                  <div
                    class="rl-clear rl-fl activityTime4"
                    v-if="promotionsList[0].diff > 0"
                  >
                    {{
                      fixNum(
                        Math.floor(
                          (promotionsList[0].diff / (1000 * 60 * 60)) % 24
                        )
                      ) +
                      ":" +
                      fixNum(
                        Math.floor((promotionsList[0].diff / (1000 * 60)) % 60)
                      ) +
                      ":" +
                      fixNum(Math.floor((promotionsList[0].diff / 1000) % 60))
                    }}
                  </div>
                  <div
                    class="rl-clear rl-fl activityTime3"
                    v-if="promotionsList[0].diff > 0"
                  >
                    {{ $t("P.Day") }}
                  </div>
                  <div
                    class="rl-clear rl-fl activityTime2"
                    v-if="promotionsList[0].diff > 0"
                  >
                    {{
                      Math.floor(promotionsList[0].diff / (24 * 3600 * 1000))
                    }}
                  </div>
                  <div
                    class="rl-clear rl-fl activityTime1"
                    v-if="promotionsList[0].diff > 0"
                  >
                    {{ $t("P.DistanceEndTime") }}
                  </div>
                  <div
                    class="rl-clear rl-fl activityTime4"
                    v-if="
                      promotionsList[0].diff !== undefined &&
                      promotionsList[0].diff <= 0
                    "
                  >
                    {{ $t("P.End") }}
                  </div>
                  <!-- 活动弹框 -->
                  <transition name="el-zoom-in-top">
                    <div
                      v-clickoutside="handleClose"
                      v-show="showPromotions"
                      class="rl-relative"
                    >
                      <div class="activity-box">
                        <div
                          style="height: 25px"
                          v-for="(item, index) in promotionsList"
                          :key="index"
                        >
                          <div
                            class="rl-fl activityName6"
                            style="line-height: 25px"
                            @click="lookActivityDetail(item, index)"
                          >
                            {{
                              $i18n.locale === "zh" || !item.nameEn == true
                                ? item.name
                                : item.nameEn
                            }}
                          </div>
                          <div
                            class="rl-fl activityTime4"
                            v-if="item.diff > 0"
                            style="line-height: 25px"
                          >
                            {{
                              fixNum(
                                Math.floor((item.diff / (1000 * 60 * 60)) % 24)
                              ) +
                              ":" +
                              fixNum(
                                Math.floor((item.diff / (1000 * 60)) % 60)
                              ) +
                              ":" +
                              fixNum(Math.floor((item.diff / 1000) % 60))
                            }}
                          </div>
                          <div
                            class="rl-fl activityTime3"
                            v-if="item.diff > 0"
                            style="line-height: 25px"
                          >
                            {{ $t("P.Day") }}
                          </div>
                          <div
                            class="rl-fl activityTime2"
                            v-if="item.diff > 0"
                            style="line-height: 25px"
                          >
                            {{ Math.floor(item.diff / (24 * 3600 * 1000)) }}
                          </div>
                          <div
                            class="rl-fl activityTime5"
                            v-if="item.diff > 0"
                            style="line-height: 25px"
                          >
                            {{ $t("P.DistanceEndTime") }}
                          </div>
                          <div
                            class="rl-fl activityTime4"
                            v-if="item.diff !== undefined && item.diff <= 0"
                            style="line-height: 25px"
                          >
                            {{ $t("P.End") }}
                          </div>
                        </div>
                      </div>
                    </div>
                  </transition>
                </div>

                <!-- 已为您选择赠品数量 -->
                <div
                  class="rl-clear gift-num-wrap"
                  v-show="presentList.length > 0 && giftNum > 0"
                >
                  <div class="rl-fl title"></div>
                  <div class="rl-fl gift-num cursor-pointer">
                    <span>已为您选择 {{ giftNum }} 个赠品</span>
                    <span class="arrow-wrap" @click="dialogVisible = true"
                      >去筛选<i class="el-icon-arrow-right"></i
                    ></span>
                  </div>
                </div>
              </div>
              <!-- 有特价展示 -->
              <div v-if="specialsList.length > 0">
                <div
                  class="group-list"
                  v-for="specialItem in specialsList"
                  :key="specialItem.itemId"
                  style="margin-top: 15px; margin-left: 10px"
                >
                  <span class="title">促销价：</span>
                  <span class="current">
                    {{
                      $i18n.locale === "en" && $root.currency === "USD"
                        ? "$"
                        : "¥"
                    }}
                    <i class="red">{{
                      $i18n.locale === "en" && $root.currency === "USD"
                        ? (Number(specialItem.specialPrice) * exchange).toFixed(
                            2
                          )
                        : specialItem.specialPrice
                    }}</i>
                  </span>
                  <span class="origin">{{
                    $i18n.locale === "en" && $root.currency === "USD"
                      ? (Number(specialItem.salePrice) * exchange).toFixed(2)
                      : specialItem.salePrice
                  }}</span>
                </div>
              </div>

              <!-- Y-c -->
              <div class="brand rl-clear">
                <div class="rl-clear rl-fl">
                  <div class="title">{{ $t("ShopDetail.OnSaleTime") }}：</div>
                  <!--上架时间-->
                  <div class="title">{{ $t("ShopDetail.CommodityNo") }}：</div>
                  <!--商品编号-->
                </div>
                <!-- 上架时间/商品编号--Y -c-->
                <div class="rl-clear rl-fl">
                  <div class="titleContent">
                    {{ goodsDetail.saleTime | formatDate }}
                  </div>
                  <div class="titleContent">{{ goodsDetail.goodsNo }}</div>
                </div>
                <!-- 销量-y-c -->
                <div class="salse-count rl-clear">
                  <p class="rl-fl" v-if="!userId == ''">
                    {{ $t("P.Bestselling") }}：<span>{{
                      goodsDetail.saleNum || 0
                    }}</span>
                  </p>
                  <p class="rl-fl" v-else>
                    {{ $t("P.Bestselling") }}：<i class="rl-text-xxs">{{
                      $t("P.ViewAfterLogin")
                    }}</i>
                  </p>
                </div>
              </div>
              <!-- 定制按钮 Y-C-->
              <div
                v-if="goodsDetail.goodsType == 2"
                class="btn-normal"
                @click="customize"
              >
                {{ $t("ShopDetail.Customized") }}
              </div>

              <!-- 货品table列表 -->
              <div class="model-wrap">
                <div class="model-wrap-r">
                  <!-- 包含在途--Y-c -->
                  <div
                    @click="changeTu"
                    class="rl-fr rl-text-xxs zaitu rl-clear cursor-pointer"
                    v-if="onWayFlag == 1"
                  >
                    <span
                      class="rl-fl"
                      :class="{ gou: tuType === true }"
                    ></span>
                    {{ $t("P.ConTran") }}
                  </div>
                  <!-- 货品列表 -->
                  <div class="shop-table">
                    <table>
                      <tr>
                        <th width="55">{{ $t("ShopCarts.Picture") }}</th>
                        <th width="70">
                          {{ $i18n.locale === "zh" ? "编码/条形码" : "Code" }}
                        </th>
                        <th width="40">{{ $t("ShopCarts.Spe") }}</th>
                        <th width="40">{{ $t("ShopCarts.Colors") }}</th>
                        <th>{{ $t("ShopCarts.RecommendedPrice") }}</th>
                        <th>{{ $t("ShopCarts.MemPrice") }}</th>
                        <th v-show="isCollectFee === 1">
                          {{ $t("Quotation.SERVICEFEE") }}
                        </th>
                        <th v-show="isCollectFee === 1">
                          {{ $t("Quotation.UNIT") }}
                        </th>
                        <th v-if="goodsDetail.goodsType == 1">
                          {{ $t("P.Inventory") }}
                        </th>
                        <th width="210">
                          {{ $t("ShopCarts.Quantity") }}
                          <el-popover
                            placement="top"
                            width="421"
                            class="cursor-pointer"
                            popper-class="packing-table"
                            trigger="click"
                          >
                            <el-table :data="boxData" max-height="650">
                              <el-table-column
                                :label="$t('P.Carton')"
                                prop="boxType"
                              ></el-table-column>
                              <el-table-column :label="$t('P.DIM')" prop="size">
                              </el-table-column>
                              <el-table-column
                                :label="$t('P.QTY')"
                                prop="boxNum"
                              ></el-table-column>
                              <el-table-column
                                :label="$t('P.WeighG')"
                                prop="boxWeight"
                              ></el-table-column>
                            </el-table>
                            <span
                              class="packing-unit"
                              :class="$i18n.locale === 'zh' ? '' : 'packing-en'"
                              slot="reference"
                              >{{
                                $i18n.locale === "zh" ? "箱" : $t("P.PCSCTN")
                              }}</span
                            >
                          </el-popover>
                        </th>
                      </tr>
                      <tr
                        class="goods-item rl-relative"
                        v-for="(item, index) in goodsItems"
                        :key="item.id"
                        @click="selectGoodsItem($event, index, item)"
                        tabindex="0"
                        hidefocus="true"
                        :class="selectIndex == index ? 'bor borBg' : ''"
                      >
                        <!-- 图片 -->
                        <td>
                          <div class="shop-img cursor-pointer">
                            <!-- 活动 -Y-C-->
                            <!-- &&item.promotions[promotionsIndex].promoType ==1 -->
                            <span
                              class="activity"
                              :class="{
                                'iconfont icon-special': $i18n.locale === 'en',
                              }"
                              v-if="
                                item.promotions &&
                                item.promotions[promotionsIndex].promoStatus ==
                                  1
                              "
                              >{{
                                $i18n.locale === "zh"
                                  ? $t("Activitys.Activ")
                                  : ""
                              }}</span
                            >
                            <!-- 拼团 Y-C-->
                            <span
                              class="activity activity1"
                              :class="{
                                'iconfont icon-special': $i18n.locale === 'en',
                                green: item.groupSeckillStatus == 0,
                              }"
                              v-if="item.groupSeckillStatus == 1"
                              >{{
                                $i18n.locale === "zh" ? "拼团" : "Spell group"
                              }}</span
                            >

                            <img
                              v-if="
                                item.itemImg !== undefined &&
                                item.itemImg !== null &&
                                item.itemImg !== ''
                              "
                              @click="magnify(item.itemImg)"
                              :src="
                                item.itemImg +
                                '?x-oss-process=image/resize,h_104,l_104'
                              "
                            />
                          </div>
                        </td>
                        <!--编码、条形码-Y  -->
                        <td>
                          <span class="code">{{ item.itemCode }}</span>
                          <br />
                          <span class="code">{{ item.barCode }}</span>
                        </td>
                        <!-- 规格 -Y -->
                        <td>
                          <el-tooltip
                            v-show="
                              $i18n.locale === 'zh' || !item.specsName == true
                            "
                            effect="dark"
                            :content="item.specsName"
                            placement="bottom"
                          >
                            <el-button class="rl-single-limit">
                              {{ item.specsName }}
                            </el-button>
                          </el-tooltip>
                          <el-tooltip
                            v-show="$i18n.locale === 'en'"
                            effect="dark"
                            :content="item.specsNameEn"
                            placement="bottom"
                          >
                            <el-button class="rl-single-limit">{{
                              item.specsNameEn
                            }}</el-button>
                          </el-tooltip>
                        </td>
                        <!-- 颜色-Y -->
                        <td>
                          <el-tooltip
                            v-show="
                              $i18n.locale === 'zh' || !item.colorNameEn == true
                            "
                            effect="dark"
                            :content="item.colorName"
                            placement="bottom"
                          >
                            <el-button>
                              <span>{{ item.colorName }}</span>
                            </el-button>
                          </el-tooltip>
                          <el-tooltip
                            v-show="$i18n.locale === 'en'"
                            effect="dark"
                            :content="item.colorNameEn"
                            placement="bottom"
                          >
                            <el-button>
                              <span>{{ item.colorNameEn }}</span>
                            </el-button>
                          </el-tooltip>
                        </td>
                        <!-- 建议零售价-登录 -->
                        <td v-if="!userId == ''">
                          <span
                            v-if="
                              Number(item.retailPrice) !== 0 &&
                              item.retailPrice !== null
                            "
                          >
                            <i
                              v-if="
                                $i18n.locale === 'en' &&
                                $root.currency === 'USD'
                              "
                            >
                              {{
                                item.salePrice &&
                                item.salePrice > item.retailPrice
                                  ? $t("P.NoPricing")
                                  : "$" +
                                    (
                                      Number(item.retailPrice) * exchange
                                    ).toFixed(2)
                              }}
                            </i>
                            <i v-else>{{
                              item.salePrice &&
                              item.salePrice > item.retailPrice &&
                              item.retailPrice != undefined
                                ? $t("P.NoPricing")
                                : "￥" + item.retailPrice
                            }}</i>
                          </span>
                          <span v-else>{{
                            item.retailPrice === undefined ||
                            item.retailPrice === null
                              ? !userId == ""
                                ? $t("P.Calculating")
                                : $t("P.ViewAfterLogin")
                              : $t("P.NoPricing")
                          }}</span>
                        </td>
                        <!-- 建议零售价-未登录 -->
                        <td v-else class="rl-text-xxss">
                          {{ $t("P.ViewAfterLogin") }}
                        </td>
                        <!-- 会员价-登录 -->
                        <td v-if="!userId == ''">
                          <span
                            v-if="
                              Number(item.salePrice) !== 0 &&
                              item.salePrice !== null
                            "
                          >
                            <i
                              v-if="
                                $i18n.locale === 'en' &&
                                $root.currency === 'USD'
                              "
                              >${{
                                (Number(item.salePrice) * exchange).toFixed(2)
                              }}</i
                            >
                            <i v-else>￥{{ item.salePrice }}</i>
                          </span>
                          <!-- 会员价-未登录 -->
                          <span v-else>{{
                            item.salePrice === undefined ||
                            item.salePrice === null
                              ? !userId == ""
                                ? $t("P.Calculating")
                                : $t("P.ViewAfterLogin")
                              : $t("P.NoPricing")
                          }}</span>
                        </td>
                        <td v-else class="rl-text-xxss">
                          {{ $t("P.ViewAfterLogin") }}
                        </td>
                        <!--服务单价-登录-->
                        <td v-show="!userId == '' && isCollectFee === 1">
                          <span
                            v-if="
                              Number(item.serviceFee) !== 0 &&
                              item.serviceFee !== null
                            "
                          >
                            <i
                              v-if="
                                $i18n.locale === 'en' &&
                                $root.currency === 'USD'
                              "
                              >${{ item.serviceFeeEn }}/pc</i
                            >
                            <i v-else>￥{{ item.serviceFee }}/片</i>
                          </span>
                          <!--服务单价-未登录-->
                          <span v-else>{{
                            item.serviceFee === undefined
                              ? !userId == ""
                                ? $t("P.Calculating")
                                : $t("P.ViewAfterLogin")
                              : $t("P.NoPricing")
                          }}</span>
                        </td>
                        <td
                          v-show="userId == '' && isCollectFee === 1"
                          class="rl-text-xxss"
                        >
                          {{ $t("P.ViewAfterLogin") }}
                        </td>
                        <!--单位-登录-->
                        <td v-show="!userId == '' && isCollectFee === 1">
                          <span
                            class="code"
                            v-if="
                              $i18n.locale === 'en' && $root.currency === 'USD'
                            "
                            >{{ item.serviceBoxNum }}pc/box</span
                          >
                          <span class="code" v-else
                            >{{ item.serviceBoxNum }}片/盒</span
                          >
                        </td>
                        <!--单位-未登录-->
                        <td
                          v-show="userId === '' && isCollectFee === 1"
                          class="rl-text-xxss"
                        >
                          {{ $t("P.ViewAfterLogin") }}
                        </td>
                        <template v-if="goodsDetail.goodsType == 1">
                          <!-- 库存---不包含在途--登录 -->
                          <td
                            class="rl-relative"
                            v-if="
                              (!userId == '' && tuType === false) ||
                              (tuType === true && item.sysOnWayFlag != 1)
                            "
                          >
                            <span class="blank-span"></span>
                            <el-popover
                              placement="bottom"
                              width="230"
                              v-model="item.visible"
                              trigger="click"
                            >
                              <!-- 库存不足 -->
                              <p
                                class="rl-margin-bottom-xxs rl-tc rl-text-bold"
                              >
                                {{ $t("P.ShortInventory") }}
                              </p>
                              <div
                                class="rl-text-xxs"
                                slot="reference"
                                v-if="stockShowFlag == 0"
                              >
                                {{ item.inStockUsableCount }}
                              </div>
                              <div
                                class="rl-text-xxs"
                                slot="reference"
                                v-else-if="
                                  stockShowFlag == 1 &&
                                  stockShowNumber < item.inStockUsableCount
                                "
                              >
                                {{ item.inStockUsableCount }}
                              </div>
                              <div
                                class="rl-text-xxs"
                                slot="reference"
                                v-else-if="
                                  stockShowFlag == 1 &&
                                  item.inStockUsableCount === 0
                                "
                              >
                                无货
                              </div>
                              <div
                                class="rl-text-xxs"
                                slot="reference"
                                v-else-if="
                                  stockShowFlag == 1 &&
                                  0 < item.inStockUsableCount <= stockShowNumber
                                "
                              >
                                库存紧张
                              </div>
                            </el-popover>
                          </td>
                          <!-- 库存---包含在途--登录 -->
                          <td
                            class="rl-relative"
                            v-else-if="
                              !userId == '' &&
                              tuType === true &&
                              item.sysOnWayFlag == 1
                            "
                          >
                            <span class="blank-span"></span>
                            <el-popover
                              placement="bottom"
                              width="230"
                              v-model="item.visible"
                              trigger="click"
                            >
                              <p
                                class="rl-margin-bottom-xxs rl-tc rl-text-bold"
                              >
                                {{ $t("P.ShortInventory") }}
                              </p>

                              <div
                                class="rl-text-xxs"
                                slot="reference"
                                v-if="stockShowFlag === 0"
                              >
                                {{
                                  item.onWayUsableCount +
                                  item.inStockUsableCount
                                }}
                              </div>
                              <div
                                class="rl-text-xxs"
                                slot="reference"
                                v-else-if="
                                  stockShowFlag === 1 &&
                                  stockShowNumber <
                                    item.onWayUsableCount +
                                      item.inStockUsableCount
                                "
                              >
                                {{
                                  item.onWayUsableCount +
                                  item.inStockUsableCount
                                }}
                              </div>
                              <div
                                class="rl-text-xxs"
                                slot="reference"
                                v-else-if="
                                  stockShowFlag === 1 &&
                                  item.onWayUsableCount +
                                    item.inStockUsableCount ===
                                    0
                                "
                              >
                                无货
                              </div>
                              <div
                                class="rl-text-xxs"
                                slot="reference"
                                v-else-if="
                                  stockShowFlag === 1 &&
                                  0 <
                                    item.onWayUsableCount +
                                      item.inStockUsableCount <=
                                    stockShowNumber
                                "
                              >
                                库存紧张
                              </div>
                            </el-popover>
                          </td>

                          <!-- 库存-未-登录 -->
                          <td v-else class="rl-text-xxss">
                            {{ $t("P.ViewAfterLogin") }}
                          </td>
                        </template>
                        <!-- 数量 -->
                        <td
                          v-if="!userId == ''"
                          :width="
                            item.groupSeckills &&
                            Number(item.groupSeckillStatus) == 1
                              ? 100
                              : ''
                          "
                          :class="
                            item.groupSeckills && item.groupSeckillStatus == 1
                              ? ''
                              : 'choose-unit'
                          "
                        >
                          <!-- 普通商品 -->
                          <div
                            class="inner-content inner-content2"
                            v-if="goodsDetail.goodsType == 1"
                          >
                            <BuySum
                              ref="BuySum"
                              :value="value"
                              :item="item"
                              :tuType="tuType"
                              @input="handleInput"
                            ></BuySum>
                            <!-- 批量下单 - 包装 -->
                            <div
                              class="box-info"
                              v-if="
                                !item.groupSeckills &&
                                item.boxs &&
                                item.boxs.length > 0
                              "
                            >
                              <el-popover
                                :ref="refNamePopover + item.id"
                                placement="bottom"
                                width="220"
                                class="cursor-pointer"
                                popper-class="box-info-table"
                                trigger="click"
                              >
                                <el-table
                                  :data="item.boxs"
                                  @row-click="
                                    (row, column, e) =>
                                      handleCheck(
                                        row,
                                        column,
                                        e,
                                        item,
                                        item.choosedBoxNum
                                      )
                                  "
                                  highlight-current-row
                                >
                                  <el-table-column
                                    :label="'包装数量（件）'"
                                    prop="boxNum"
                                  ></el-table-column>
                                  <el-table-column
                                    :label="'单位'"
                                    prop="boxType"
                                  ></el-table-column>
                                </el-table>
                                <span slot="reference"
                                  >{{
                                    item.choosedBoxInfo !== ""
                                      ? item.choosedBoxInfo
                                      : $t("P.Pieces")
                                  }}
                                </span>
                              </el-popover>
                            </div>

                            <!-- 切换活动按钮 -->
                            <template
                              v-if="item.rulesList && item.rulesList.length > 1"
                            >
                              <el-popover
                                :ref="refNamePopovers + item.id"
                                placement="bottom"
                                width="350"
                                class="cursor-pointer"
                                popper-class="box-info-table"
                                v-model="item.acVisible"
                              >
                                <div class="detail-qiehuan-list">
                                  <div
                                    v-for="aItem in item.rulesList"
                                    :key="aItem.id"
                                    class="detail-qiehuan-box"
                                  >
                                    <i
                                      :class="
                                        aItem.isCheck
                                          ? 'el-icon-success'
                                          : 'el-icon-Nocheck'
                                      "
                                      @click="
                                        changeActivity(
                                          aItem.id,
                                          item.rulesList,
                                          item
                                        )
                                      "
                                    ></i>
                                    <div>
                                      <span class="detail-qiehuan-ruleName">{{
                                        aItem.ruleName
                                      }}</span>
                                      <div
                                        v-if="aItem.conditions"
                                        class="detail-qiehuan-conditions"
                                      >
                                        <span
                                          v-for="(
                                            cItem, cIndex
                                          ) in aItem.conditions"
                                          :key="cItem.id"
                                          >条件{{ cIndex + 1 }}：{{
                                            cItem.conditionName
                                          }}</span
                                        >
                                      </div>
                                    </div>
                                  </div>
                                </div>
                                <span
                                  slot="reference"
                                  class="detail-qiehuan"
                                  @click.stop="item.acVisible = !item.acVisible"
                                  >切换活动</span
                                >
                              </el-popover>
                            </template>
                          </div>
                          <p
                            class="red-tips"
                            v-if="
                              item.choosedBoxNum &&
                              item.choosedBoxNum > 1 &&
                              item.count > 0
                            "
                          >
                            共计{{ item.count }}件
                          </p>

                          <!-- 定制 -->
                          <template v-if="goodsDetail.goodsType == 2">
                            <div v-if="goodsDetail.diyType === 0">
                              <GoodsBuySum
                                ref="GoodsBuySum"
                                :item="goods"
                                @itemInput="handleInput"
                                :value="value"
                              ></GoodsBuySum>
                            </div>
                            <div v-else>
                              <el-input-number
                                disabled
                                :min="0"
                                label="描述文字"
                                size="mini"
                              ></el-input-number>
                            </div>
                          </template>
                        </td>
                        <!-- 数量--未登录 -->
                        <td v-else class="rl-text-xxss">
                          {{ $t("P.ViewAfterLogin") }}
                        </td>
                      </tr>
                    </table>
                  </div>
                </div>
              </div>
              <!-- 统计- -->
              <div
                class="buy-list rl-clear"
                :class="goodsItems.length > 5 ? 'rl-margin-top-default' : ''"
                v-show="totalCount > 0"
                v-if="goodsItems"
              >
                <div class="rl-fl rl-text-xxs">
                  {{ $t("ShopDetail.PurchasingList") }}
                  <!--采购清单-->
                </div>
                <!-- 已优惠 -->
                <div
                  class="rl-fr rl-padding-left-default rl-text-xxs"
                  v-if="totalPrice - actualSum > 0"
                >
                  {{ $t("ShopDetail.discount") }}
                  <i
                    class="rl-text-red"
                    v-show="$i18n.locale === 'en' && $root.currency === 'USD'"
                    >$</i
                  >
                  <i
                    class="rl-text-red"
                    v-show="$i18n.locale === 'en' && $root.currency === 'CNY'"
                    >￥</i
                  >
                  <span class="rl-text-red rl-text-mid rl-text-bold">{{
                    discountAmount | keepTwoNum
                  }}</span>
                  <i class="rl-text-red" v-show="$i18n.locale === 'zh'">元</i>
                </div>
                <!-- 合计 -->
                <div class="rl-fr rl-text-xxs">
                  {{ $t("ShopCarts.Total") }}：
                  <span class="rl-text-red rl-text-sm rl-text-bold">{{
                    totalCount
                  }}</span>
                  <i class="rl-text-red">{{ $t("P.Pieces") }}</i>
                  <!--件-->
                  <span class="line">|</span>
                  <i
                    class="rl-text-red"
                    v-show="$i18n.locale === 'en' && $root.currency === 'USD'"
                    >$</i
                  >
                  <i
                    class="rl-text-red"
                    v-show="$i18n.locale === 'en' && $root.currency === 'CNY'"
                    >￥</i
                  >
                  <span class="rl-text-red rl-text-sm rl-text-bold">{{
                    actualSum | keepTwoNum
                  }}</span>
                  <i class="rl-text-red" v-show="$i18n.locale === 'zh'">元</i>
                </div>
              </div>
              <!-- 加入购物车/立即购买--只有普通商品-标准定制才展示按钮 && goodsDetail.diyType == 0-->
              <template
                v-if="userId != '' && userId != 'undefined' && userId != null"
              >
                <div
                  class="buy-now"
                  v-if="goodsDetail.goodsType == 1 && goodsDetail.goodsItems"
                >
                  <div class="buy rl-clear rl-margin-zero">
                    <div
                      @click="buyNowFun"
                      class="
                        buy-btn
                        rl-tc rl-text-white rl-fl rl-margin-right-lllg
                        cursor-pointer
                      "
                    >
                      {{ $t("P.BuyNow") }}
                    </div>
                    <div
                      @click="addToShopingCart"
                      class="join-btn rl-tc rl-text-white rl-fl cursor-pointer"
                      v-if="clickAgain === true"
                    >
                      {{ $t("P.AddToCart") }}
                    </div>
                    <div class="join-btn rl-tc rl-text-white rl-fl" v-else>
                      {{ $t("P.AddToCart") }}
                    </div>
                  </div>
                </div>
              </template>
            </div>
          </div>
        </div>
      </div>

      <!-- 商品详情/规格参数/包装售后-Y-C -->
      <div v-if="showDetail === true" class="mainTwo rl-margin-zero">
        <div
          class="details-list"
          :class="fixedTab ? 'fixed' : ''"
          id="fixedNavBar"
        >
          <ul>
            <li
              class="cursor-pointer"
              :class="[
                curTab == 1 ? 'cur' : '',
                $i18n.locale === 'zh' ? '' : 'nav-item-en14',
              ]"
              @click="handleTabChange(1)"
            >
              {{ $i18n.locale === "zh" ? "商品详情" : "Commodity Details" }}
            </li>
            <li
              class="cursor-pointer"
              :class="[
                curTab == 2 ? 'cur' : '',
                $i18n.locale === 'zh' ? '' : 'nav-item-en14',
              ]"
              @click="handleTabChange(2)"
            >
              {{ $t("ShopDetail.SpecificationParameter") }}
            </li>
            <li
              class="cursor-pointer"
              :class="[
                curTab == 3 ? 'cur' : '',
                $i18n.locale === 'zh' ? '' : 'nav-item-en14',
              ]"
              @click="handleTabChange(3)"
            >
              {{
                $i18n.locale === "zh"
                  ? "包装与售后"
                  : "Packaging & After-sales Service"
              }}
            </li>
          </ul>
          <div class="join-btn rl-tc rl-text-white" v-show="fixedTab">
            <div
              @click="addToShopingCart"
              class="cursor-pointer"
              v-if="clickAgain === true"
            >
              {{ $t("P.AddToCart") }}
            </div>
            <div v-else>{{ $t("P.AddToCart") }}</div>
          </div>
        </div>
        <!-- 商品详情-Y-C -->
        <div class="goods-detail" id="goodsDetail">
          <div
            v-show="$i18n.locale === 'zh' || !goodsDetail.contentUrlEn == true"
            class="img-box-p"
            v-html="goodsDetail.contentUrl"
          ></div>
          <div
            v-show="$i18n.locale === 'en'"
            class="img-box-p"
            v-html="goodsDetail.contentUrlEn"
          ></div>
        </div>
        <!-- 规格参数--Y-C -->
        <div id="specDetail">
          <div
            v-if="
              goodsDetail.params !== undefined &&
              goodsDetail.params !== null &&
              goodsDetail.params.length > 0
            "
            class="shop-descripe descriptions"
          >
            <div class="title">
              <span>{{ $t("ShopDetail.SpecificationParameter") }}</span>
            </div>
            <div
              class="goods-attribute"
              v-if="goodsDetail.goodsType === 1 && goodsDetail.diyType === 0"
            >
              <!--普通商品/标准定制-->
              <el-row :gutter="20">
                <el-col
                  :span="10"
                  v-for="item in goodsDetail.params"
                  :key="item.id"
                >
                  <div class="grid-content bg-purple rl-clear">
                    <div class="descrip-item descrip-title rl-fl">
                      {{
                        $i18n.locale === "zh" || !item.paramNameEn == true
                          ? item.paramName
                          : item.paramNameEn
                      }}
                    </div>
                    <div class="descrip-item rl-fl">
                      {{
                        $i18n.locale === "zh" || !item.paramValueNameEn == true
                          ? item.paramValueName
                          : item.paramValueNameEn
                      }}
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </div>
        <!-- 装箱规格-Y-C -->
        <div id="packDetail">
          <div
            class="package-wrap descriptions"
            v-if="boxData.length > 0 && !this.userId == ''"
          >
            <div class="title">
              <span>{{ $i18n.locale === "zh" ? "包装" : "Packaging" }}</span>
            </div>
            <div class="package-table rl-clear" v-if="boxData.length > 0">
              <el-table :data="boxData">
                <el-table-column
                  :label="$t('P.Carton')"
                  prop="boxType"
                ></el-table-column>
                <el-table-column
                  :label="$t('P.DIM')"
                  prop="size"
                ></el-table-column>
                <el-table-column
                  :label="$t('P.QTY')"
                  prop="boxNum"
                ></el-table-column>
                <el-table-column
                  :label="$t('P.WeighG')"
                  prop="boxWeight"
                ></el-table-column>
              </el-table>
            </div>
          </div>
          <!-- 售后服务-y -->
          <div class="descriptions">
            <div class="title">
              <span>{{
                $i18n.locale === "zh" ? "售后服务" : "After-sales Service"
              }}</span>
            </div>
            <p class="service-phone" v-show="$i18n.locale === 'zh'">
              售后电话：0755-21011282
            </p>
            <p class="service-phone" v-show="$i18n.locale === 'zh'">
              咨询电话：0755-21011282
            </p>
            <p class="service-phone" v-show="$i18n.locale === 'en'">
              Email: 7.bat@bat.com.cn
            </p>
          </div>
        </div>
      </div>
      <!--找不到商品-Y-C-->
      <div class="empty-car rl-margin-zero" v-if="showDetail === false">
        <div class="empty-car-img rl-margin-zero">
          <img width="100%" src="../assets/images/goods-empty.png" />
        </div>
        <p
          class="
            rl-tc
            rl-margin-top-default
            rl-margin-bottom-default
            rl-text-sm
            rl-text-gray
          "
        >
          {{ $t("P.NoCommodities") }}
        </p>
      </div>
      <!--公共底部-Y-C-->
      <Footer></Footer>
    </div>
    <!--点击图片放大-Y-C-->
    <div class="cover" v-if="showImgurl"></div>
    <div class="pro-cover-img rl-relative" v-if="showImgurl">
      <img :src="imgurl" alt />
      <span @click="shutLogImg" class="shut cursor-pointer"></span>
    </div>

    <!-- 选择赠品 -->
    <el-dialog
      class="alls alls2"
      :title="$t('P.ChooseGift')"
      :visible.sync="dialogVisible"
    >
      <!-- <div class="rl-padding-left-default rl-padding-bottom-xxxs rl-padding-top-xxxs rl-bd-gray-sm">
        {{ $t("P.Receive") }} {{ presentCount }} {{ $t("P.MostPieces") }},{{ $t("P.HaveSelected")}}
        {{ hasChoose }} {{ $t("P.Pieces") }}
      </div> -->
      <div
        class="
          shop-table
          max-height300
          overflow-y
          rl-padding-left-default rl-padding-right-default
        "
        v-for="item in presentList"
        :key="item.id"
      >
        <div class="present-top-div">
          <span>{{ item.ruleName }}</span
          ><!-- 规则名称   -->
          <span>{{ item.conditionName }}</span>
          <!-- 规则条件名称   -->
          <div>
            按照活动规则最多可领取<i>{{ item.presentCount }}</i
            >件赠品
          </div>
        </div>
        <el-table :data="item.presents" border style="width: 100%">
          <el-table-column
            prop="itemCode"
            label="编码"
            width="180"
          ></el-table-column>
          <el-table-column
            prop="itemName"
            label="货品名称"
            width="180"
          ></el-table-column>
          <el-table-column
            prop="specsName"
            label="规格"
            width="100"
          ></el-table-column>
          <el-table-column
            prop="colorName"
            label="颜色"
            width="100"
          ></el-table-column>
          <el-table-column
            prop="kucunCount"
            label="库存"
            width="100"
          ></el-table-column>
          <el-table-column label="最多可选" width="100">
            <template slot-scope="scope">
              <span>
                {{
                  scope.row.presentCount > scope.row.kucunCount
                    ? scope.row.kucunCount
                    : scope.row.presentCount
                }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="已选" width="180">
            <template slot-scope="scope">
              <!-- 自封装计数器 -->
              <div class="el-input-number el-input-number--mini">
                <!--减号 -->
                <span
                  class="el-input-number__decrease"
                  :class="scope.row.num == 0 ? 'is-disabled' : ''"
                  @click="presentHandleChange(scope.row, item, 1)"
                  ><i class="el-icon-minus"></i
                ></span>
                <!-- 加号 -->
                <span
                  class="el-input-number__increase"
                  :class="
                    scope.row.num == scope.row.presentCount ||
                    scope.row.num >= scope.row.kucunCount
                      ? 'is-disabled'
                      : ''
                  "
                  @click="presentHandleChange(scope.row, item, 2)"
                  ><i class="el-icon-plus"></i
                ></span>
                <!-- 数字框 -->
                <div class="el-input el-input--mini">
                  <input
                    v-model="scope.row.num"
                    min="0"
                    :max="scope.row.kucunCount"
                    class="el-input__inner"
                    @change="presentHandleChange(scope.row, item, 3)"
                  />
                </div>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="presentsConfirmFun">{{
          $t("P.OK")
        }}</el-button>
        <el-button @click="dialogVisible = false">{{
          $t("P.Cancel")
        }}</el-button>
      </div>
    </el-dialog>
    <!--加载动画-->
    <loading v-if="this.showLoading"></loading>

    <!-- 回到顶部 -->
    <div class="back-to-top" v-show="isBackToTop" @click="backToTop">
      <img src="../assets/images/back-to-top.png" alt="BAT商城 回到顶部" />
    </div>

    <!--协议弹框弹框-Y-->
    <div class="cover" v-if="showOpenCut"></div>
    <div
      class="prote-cover cover-box rl-padding-bottom-lllg rl-relative"
      v-if="showOpenCut"
    >
      <div
        class="
          rl-margin-bottom-default
          rl-padding-left-lllg
          rl-padding-top-xxxs
          rl-padding-bottom-xxxs
          rl-text-lg
          rl-text-bold
          rl-bg-gray-mm
        "
      >
        {{ agreementName }}
        <div>{{ $t("Register.cutFilmMachineDesc") }}</div>
      </div>
      <div
        class="
          address-infos
          rl-bg-white rl-padding-left-default rl-clear rl-margin-zero
        "
      >
        <div class="pop-box rl-padding-top-lllg">
          <div class="box">
            <div class="box-word" v-if="$i18n.locale === 'zh'">
              <div v-html="agreementContent"></div>
            </div>
          </div>
        </div>
      </div>
      <div class="box-foot rl-tc rl-margin-top-double">
        <span
          @click="showOpenCut = false"
          class="rl-bg-gray-mm cursor-pointer"
          >{{ $t("P.Cancel") }}</span
        >
        <span
          @click="agreeGoonFun"
          class="
            rl-bg-blue-xs rl-text-white
            cursor-pointer
            rl-margin-left-default
          "
          >{{ $t("Register.AgreeAndContinue") }}</span
        >
        <!-- <span  @click="agreeGoon" class="rl-bg-blue-xs rl-text-white cursor-pointer rl-margin-left-default">{{$t('Message.Confirm')}}</span> -->
      </div>
    </div>

    <!-- 选择活动 -->
    <buyNow
      v-if="activityDialogVisible"
      @shutDialog="shutDialog"
      :onWayAttendEventFlag="onWayAttendEventFlag"
      :goodsType="Number(goodsType)"
      :withoutActivityItem="withoutActivityItem"
      :activityPromotions="activityPromotions"
      :activityDialogVisible="activityDialogVisible"
    ></buyNow>

    <!-- 配送方式选择 -->
    <chooseDelivery
      v-show="showChooseDelivery"
      :deliveryData="deliveryData"
      :orderhint="orderhint"
      :values="0"
      @handleClose="handleCloseDelivery"
    ></chooseDelivery>
  </div>
</template>

<script>
import Vue from "vue";
import sha1 from "js-sha1";
import BuySum from "@/components/BuySum.vue";
import presellNum from "@/components/presellNum.vue";
import GoodsBuySum from "@/components/GoodsBuySum.vue";
import { MessageBox, Alert } from "element-ui";
import PicZoom from "vue-piczoom";
import {
  formatDate,
  loginOut,
  fomatFloat,
  fomatFloor,
  orderHint,
} from "@/assets/js/common.js";
import songSum from "@/components/songSum.vue";
import loading from "@/components/loading.vue";
import buyNow from "@/components/buyNow.vue";
import GD from "@/assets/js/globalData";
import { getCartNum } from "@/api/cart";
import chooseDelivery from "@/components/dialog/chooseDelivery.vue";
// import Router from 'vue-router'

// LIU
import cutFilmMachineDialog from "@/components/cutFilmMachineDialog";
import {
  goodsDetails,
  goodsCollection,
  userShopSetting,
  listStockByCondition,
  priceItemList,
  promotiongroupseckill,
  addShoppingcart,
  shoppingcartList,
  agreementSignList,
  signAgreement,
  agreementSignStatus,
  agreementSignId,
  agreementSignDrandId,
} from "@/apiService/api";

export default {
  name: "ShopDetail",
  components: {
    Header: (resolve) => require(["@/components/Header.vue"], resolve),
    Footer: (resolve) => require(["@/components/Footer.vue"], resolve),
    BuySum,
    presellNum,
    GoodsBuySum,
    PicZoom,
    songSum,
    loading,
    buyNow,
    chooseDelivery,
    cutFilmMachineDialog,
  },
  data() {
    return {
      // liu---start

      login_state: true, // 后台跳入商品详情在登录后在返回商品详情
      userState: 2,
      showDetail: true, // 展示商品详情
      shopImg: "", //商品图片
      goodsDetail: {}, //商品详情
      goodsItems: [], //货品列表
      stockShowFlag: 0, // 0 实际库存 1模糊库存
      stockShowNumber: 0, // 库存临界值
      isCollection: true, //是否收藏
      userId: "", //分销商id
      capitalStatus: 0, // 资质状态:0,未提交 1,申请中,2,合作中 3,申请失败'
      freezeStatus: 0, // 冻结状态:1,冻结  1,未冻结',
      id: this.$route.query.good_id, //商品id
      onWayFlag: null, //是否可包含在途库存
      tuType: false, //  是否勾选在途
      isCollectFee: null, // 是否存在收取服務費
      boxData: [], //装箱规则
      showImgurl: false, //点击货品图片放大图片是否展示
      imgurl: "", //点击货品图片
      clickAgain: true, // 用于防止二次点击
      selectIndex: 0, // 选择的货品的index
      promotionsIndex: 0, //选择活动的index
      promotionsList: [], // 选择商品的活动
      showPromotions: false,
      promotionsFocus: false,
      groupGoodList: [], // 拼团活动列表
      isGroup: false, // 是否是拼团
      isGroupIndex: 0, // 当前选中拼团货品
      isGroupShow: false, // 拼团是否显示
      ladderPriceList: [], //选择展示的货品阶梯价
      ladderPriceId: "", //选择的阶梯id
      showLoading: false, // 展示加载动画
      activityType: this.$route.query.activityType, // //1:拼团  2：秒杀  3：活动  4：其他
      accessType: this.$route.query.accessType, // 访问类型 0：前台访问 1后台访问
      goodsType: this.$route.query.goodsType, //商品类型 1-普通 2-定制
      fixedTab: false, // 是否固定详情tab
      curTab: 1, // 默认详情tab
      isBackToTop: false, // 是否回到顶部
      specialsList: [], //特价活动

      value: 1, //加减号数
      totalCount: 0, //购买的数量总数
      actualSum: 0, //购买的总金额
      totalPrice: 0, //总原价
      discountAmount: 0, //优惠价格
      brandList: [], //品牌协议
      showOpenCut: false, //是否展示
      agreementContent: "", //协议内容
      agreementName: "", //协议名称
      agreementStatus: true, //协议签署状态
      agreementId: "", //协议id
      presentList: [], //赠品列表
      dialogVisible: false, // 赠品选择框显隐
      orderList: [], //立即购买下单的货品列表
      activityDialogVisible: false, //是否展示活动弹框
      activityPromotions: [], //活动列表数据
      // liu---end

      showChooseDelivery: false,

      withoutActivityItem: [],
      onWayAttendEventFlag: 0, // 在途商品是否参与活动 1.参与，0.不参与

      deliveryData: [], // 配送方式选择弹窗列表数据

      colorBlue: 0, // 控制商品图片选中状态
      values: 0, // 跳转下单
      numInWarehouse: "",
      stockItemCount: "",
      customizedItems: [], // 定制商品货品列表
      tabType: 0, // 控制定制商品分组
      moreLabel: false, // 更多活动标签显隐
      showSongGoodsList: [], // 满赠货品
      presentCountMap: {},
      presentCount: 0,
      promotionRuleId: "", // 活动规则id
      songGoods: 0, // 是否有赠品

      hasActivity: false, // 是否有参加活动
      orderBuyMoney: 0,
      orderBuyCount: 0,
      distributeList: [], // 配送方式列表
      limitPurchase: [], // 限购商品列表
      gradeDiscountsRule: [], // 商品等级折扣规则
      pickUpRateRule: {}, // 提货增长返利规则
      showRetailPrice: true, // 是否展示零售价
      reduceOrPresent: 0, // 1满减2满赠3特价
      offsetLeft: "",
      setNum: 0,
      diyItemCode: "",

      promotionsDifferential: {}, // 商品阶梯价
      exchangeRate: 0, // 汇率
      interval: null, // 倒计时
      promotionsDifferentials: [], // 阶梯价格列表
      totalPrice: 0, // 实际金额
      discountAmount: 0, // 阶梯价优惠金额
      tempArrayStockData: {}, // 临时存储检验库存数据
      refNamePopover: "popover-", // popover ref名称前缀
      refNamePopovers: "popovers-", // popover ref名称前缀
      orderhint: "",
      exchange: 1, //汇率
      presentBuyGoodsList: [], // 已选赠品列表
      giftNum: 0, // 已为您选择赠品数量
    };
  },
  directives: {
    // ..事件绑定
    clickoutside: {
      bind: function (el, binding, vnode) {
        function documentHandler(e) {
          if (el.contains(e.target)) {
            // ..这里判断点击的元素是否是本身，是本身，则返回
            return false;
          }
          if (binding.expression) {
            // ..判断指令中是否绑定了函数
            binding.value(e); // ..如果绑定了函数 则调用那个函数，此处binding.value就是handleClose方法
          }
        }
        el._vueClickOutside_ = documentHandler; // ..给当前元素绑定个私有变量，方便在unbind中可以解除事件监听
        document.addEventListener("click", documentHandler);
      },
      unbind: function (el, binding) {
        document.removeEventListener("click", el._vueClickOutside_);
        delete el._vueClickOutside_;
      },
    },
  },
  filters: {
    // 保留两位小数
    keepTwoNum(value) {
      value = Number(value);
      return value.toFixed(2);
    },
    // 日期
    formatDate(time) {
      var date = new Date(time);
      return formatDate(date, "yyyy-MM-dd hh:mm:ss");
    },
  },
  computed: {
    goodsInventory() {
      // 库存
      var total = 0;
      if (Number(this.goodsType) === 1) {
        this.goodsItems.forEach((goods) => {
          total += goods.boxNum;
        });
      }
      return total;
    },
    goodsWeight() {
      // 重量
      var total = 0;
      if (Number(this.goodsType) === 1) {
        this.goodsItems.forEach((goods) => {
          total += goods.weight;
        });
      }
      return total;
    },
    hasChoose() {
      // 已选择赠品数量
      var total = 0;
      this.showSongGoodsList.forEach((item) => {
        if (item.num) {
          total += Number(item.num);
        }
      });
      return total;
    },
  },
  methods: {
    // liu--------6.10-start
    // 根据品牌id查询协议
    getAgreementSignData(brandId) {
      let params = {
        agreementArea: 0, //协议发布区域 0国内 1国外
        brandId: brandId,
      };

      agreementSignDrandId(params).then((res) => {
        if (res.success && res.data) {
          this.brandList = res.data;
          if (res.data.status == 1) {
            this.agreementId = res.data.id;
            this.getAgreementSignStatus(res.data.id);
          }
          console.log("品牌协议：", res.data);
        }
      });
    },
    // 查询协议签署状态
    getAgreementSignStatus(agreementId) {
      let userId = localStorage.getItem("userId");
      agreementSignStatus({
        distributorId: userId,
        agreementId: agreementId,
      }).then((res) => {
        if (res.success) {
          this.agreementStatus = res.data.sign;
          this.agreementSignDetailFun(agreementId);
        }
      });
    },
    // 查询协议详情
    agreementSignDetailFun(id) {
      agreementSignId({ id: id }).then((res) => {
        if (res.success) {
          this.agreementContent = res.data.content;
          this.agreementName = res.data.name;
        }
      });
    },
    // 切换活动
    changeActivity(id, ruleList, goodItem) {
      console.log("货品", goodItem);
      console.log("活动", ruleList);
      let isGroupFlag = false;
      this.groupGoodList = [];
      ruleList.forEach((ruleItem) => {
        if (ruleItem.id == id) {
          this.$set(ruleItem, "isCheck", true);
          //判断是否是拼团活动
          //  promoType //活动类型，1 普通活动，2 阶梯活动 3 拼团
          if (ruleItem.promoType == 1 || ruleItem.promoType == 2) {
            this.$set(goodItem, "goodsPromotionId", id);
            this.$set(goodItem, "groupSeckillId", "");
            if (ruleItem.promoType == 1) {
              let oldVal1 = [];
              let oldVal2 = [];
              let promotionsList = JSON.parse(
                JSON.stringify(this.promotionsList)
              );
              promotionsList.forEach((proItem) => {
                if (proItem.id == id) {
                  oldVal1.push(proItem);
                } else {
                  oldVal2.push(proItem);
                }
              });
              this.$set(goodItem, "activityList", [...oldVal1, ...oldVal2]);
              this.promotionsList = [...oldVal1, ...oldVal2];
              if (this.promotionsList.length > 0) {
                clearInterval(this.interval);
                this.intervalTime();
              }
            } else {
              this.ladderPriceList = ruleItem.conditions;
            }
          } else {
            this.groupGoodList.push(ruleItem.groupRules); //选择第一个货品拼团活动
            this.$set(goodItem, "goodsPromotionId", "");
            this.$set(goodItem, "groupSeckillId", id);
          }
        } else {
          this.$set(ruleItem, "isCheck", false);
        }
      });
      this.handleInput(goodItem.count, goodItem.id);
      let refName = this.refNamePopovers + goodItem.id;
      this.$refs[refName][0].doClose();
    },
    // 货品切换
    selectGoodsItem(event, index, item) {
      this.selectIndex = index;
      // 在途的判断
      this.onWayFlag = item.sysOnWayFlag;
      if (item.groupId) {
        this.groupGoodList = [];
        // 拼团
        this.isGroupIndex = item.id;
        this.isGroupShow = true;
        this.groupGoodList.push(item.groupSeckills[0]);
        this.intervalTime("group"); //倒计时
      } else {
        this.isGroupIndex = 0;
        if (item.activityList) {
          this.promotionsList = item.activityList;
          this.ladderPriceList =
            item.jieTiList.length > 0
              ? item.jieTiList[0].rules[0].conditions
              : []; //第一个货品的阶梯价
          if (this.promotionsList.length > 0) {
            //活动倒计时
            this.intervalTime();
            this.isGroupShow = false;
            if (
              this.promotionsList[0].conditions[0].specials &&
              this.promotionsList[0].conditions[0].specials.length > 0
            ) {
              this.specialsList =
                this.promotionsList[0].conditions[0].specials[0];
            }
          }
        }
        this.ladderPriceId = item.id;
      }
    },
    // 获取详情-
    async getGoodsDetail() {
      let that = this;
      this.showLoading = true;
      if (
        this.userId != "" &&
        this.userId != "undefined" &&
        this.userId != null
      ) {
        this.userShopSettingFun();
      }

      let detailsData = await goodsDetails({ id: this.id }); //详情接口

      let detailsObj = {}; //详情对象
      if (detailsData.success) {
        // 判断是否有商品详情
        if (detailsData.data == "") {
          this.showDetail = false;
          return;
        }
        detailsObj = detailsData.data;
        if (
          this.userId != "" &&
          this.userId != "undefined" &&
          this.userId != null
        ) {
          // //协议状态处理
          this.getAgreementSignData(detailsObj.brandId);
          let collectData = await goodsCollection("GET", { id: this.id }); //获取收藏状态接口
          //判断商品是否被收藏-- 添加收藏字段
          if (collectData.data) {
            detailsObj.collectionFlag = 1;
          } else {
            detailsObj.collectionFlag = 0;
          }
        }

        // 商品大图
        if (this.$i18n.locale === "zh") {
          this.shopImg = detailsObj.imageUrl1;
        } else {
          this.shopImg = detailsObj.imageUrl1en || detailsObj.imageUrl1;
        }

        let itemIds = []; //货品id集合--用于获取库存
        let twoGoodsIdList = []; //存在商品和货品id集合---用于查询货品的价格
        if (detailsObj.goodsItems) {
          for (let i = 0; i < detailsObj.goodsItems.length; i++) {
            itemIds.push(detailsObj.goodsItems[i].id); // 获取货品id集合
            let itemsIdObj = {
              goodsId: this.id, //商品id
              itemId: detailsObj.goodsItems[i].id, //货品id
            };
            twoGoodsIdList.push(itemsIdObj);
            // 在途的判断
            let sysAutoDelivery = localStorage.getItem("autoDelivery"); //分销商是否是直发客户：1 是，0 否
            let sysOnWayFlag = localStorage.getItem("onWayFlag"); //分销商是否支持在途库存 1是 0否 默认是1
            let onWayFlag = 0;
            //onwaySaleFlag 直发客户是否支持在途：0-否 1-是
            if (sysAutoDelivery == 1) {
              onWayFlag =
                sysOnWayFlag == 1
                  ? detailsObj.goodsItems[i].onwaySaleFlag == 1
                    ? 1
                    : 0
                  : 0;
            } else {
              onWayFlag = sysOnWayFlag;
            }
            this.$set(
              detailsObj.goodsItems[i],
              "sysOnWayFlag",
              Number(onWayFlag)
            );
            // 默认取第一个货品的在途值
            if (i == 0) {
              this.onWayFlag = detailsObj.goodsItems[i].sysOnWayFlag;
            }
          }

          this.goodsItems = detailsObj.goodsItems;
          detailsObj.goodsItems.forEach((item) => {
            item.brandId = detailsObj.brandId;
            this.$set(item, "status", 0);
            this.$set(item, "groupSeckillStatus", 0);
            this.$set(item, "count", 0);
            this.$set(item, "inStockUsableCount", 0);
            this.$set(item, "onWayUsableCount", 0);
            this.$set(item, "retailPrice", 0);
            this.$set(item, "salePrice", 0);

            this.$set(item, "itemType", 1); //是否赠品 1 非赠品 2 赠品

            // 批量下单 - 包装
            this.$set(item, "choosedBoxNum", 1);
            // 处理装箱规格
            let objs = {
              boxType: "件",
              boxNum: 1,
            };
            if (item.boxs && item.boxs.length > 0) {
              // 有装箱规格
              this.$set(item, "boxId", "");

              // 判断是否有设置批量下单-默认下单形式：1 是，0 否
              let hasDefaultFlag = false;
              let defaultItem = {};
              item.boxs.forEach((box) => {
                if (box.defaultFlag == 1) {
                  hasDefaultFlag = true;
                  defaultItem = box;
                }
              });

              if (hasDefaultFlag) {
                // 有设置批量下单-默认下单形式，只能选择该形式
                this.$set(item, "choosedBoxNum", defaultItem.boxNum);
                this.$set(item, "choosedBoxInfo", defaultItem.boxType);
                this.$set(item, "boxId", defaultItem.id);

                let boxs = [];
                boxs.push(defaultItem);
                this.$set(item, "boxs", boxs);
              } else {
                // 无设置批量下单-默认下单形式
                let boxList = [];
                this.$set(item, "choosedBoxNum", 1);
                this.$set(item, "choosedBoxInfo", "件");
                item.boxs.forEach((box) => {
                  boxList.push(box);
                });
                boxList.push(objs);
                this.$set(item, "boxs", boxList);
              }
            } else {
              // 无装箱规格，默认件
              this.$set(item, "choosedBoxInfo", "件");
              this.$set(item, "choosedBoxNum", 1);

              let boxs = [];
              boxs.push(objs);
              this.$set(item, "boxs", boxs);
            }
          });
        }
        this.showLoading = false;

        let detailsObj2 = detailsObj;
        this.goodsDetail = detailsObj2; //详情
        if (
          this.userId != "" &&
          this.userId != "undefined" &&
          this.userId != null
        ) {
          // 获取货品价格
          let paramsPrice = {
            goodsItemIds: twoGoodsIdList,
          };
          if (twoGoodsIdList && twoGoodsIdList.length > 0) {
            await priceItemList(paramsPrice).then((res) => {
              if (res.success) {
                let priceList = res.data;
                detailsObj2.goodsItems.forEach((item) => {
                  priceList.forEach((items) => {
                    if (items.itemId == item.id) {
                      this.$set(item, "retailPrice", items.retailPrice); //建议零售价
                      this.$set(item, "salePrice", items.salePrice); //会员价
                    }
                  });
                });
              }
            });
          }
          // 获取商品库存
          let params = {
            distributorId: this.userId,
            itemIdList: itemIds,
          };

          if (itemIds && itemIds.length > 0) {
            await listStockByCondition(params).then((stockRes) => {
              if (stockRes.success) {
                console.log(stockRes.data);
                let stockResData = stockRes.data;
                for (let i = 0; i < stockResData.length; i++) {
                  detailsObj2.goodsItems.forEach((items) => {
                    if (items.id === stockResData[i].itemId) {
                      let inStockUsableCount =
                        stockResData[i].inStockUsableCount > 0
                          ? stockResData[i].inStockUsableCount
                          : 0; // 在库库存
                      let onWayUsableCount =
                        stockResData[i].onWayUsableCount -
                          stockResData[i].inStockUsableCount >
                        0
                          ? stockResData[i].onWayUsableCount -
                            stockResData[i].inStockUsableCount
                          : 0; // 在途库存
                      this.$set(
                        items,
                        "inStockUsableCount",
                        inStockUsableCount
                      );
                      this.$set(items, "onWayUsableCount", onWayUsableCount);
                    }
                  });
                }
              }
            });
          }
        }

        if (detailsObj.goodsItems) {
          that.lookBox(detailsObj.goodsItems); // 装箱规格
        }

        this.isCollectFee = detailsObj.isCollectFee; //是否收取服务费
        this.goodsDetail = detailsObj2; //详情
        this.goodsItems = detailsObj2.goodsItems; //货品
      } else {
        this.$message(detailsData.errMessage);
      }

      if (this.goodsItems) {
        if (
          this.userId != "" &&
          this.userId != "undefined" &&
          this.userId != null
        ) {
          this.getActivityFun(); //获取商品活动
        }
        if (this.goodsItems[0].id) {
          this.isGroupIndex = this.goodsItems[0].id; //选择对应货品的活动
        }
      }
      console.log("打印详情：", this.goodsDetail);
    },
    //根据商品id查找活动--new--7.21
    getActivityFun() {
      promotiongroupseckill({ id: this.id }).then((res) => {
        if (res.success) {
          let goodsItems = this.goodsItems;
          let newTime = new Date().getTime();
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
                    this.$set(
                      goodItem,
                      "groupSeckills",
                      promoItem.groupSeckills
                    );
                  }
                  //促销活动
                  if (promoItem.promotions && promoItem.promotions.length > 0) {
                    this.$set(goodItem, "promotions", promoItem.promotions);
                  }
                }
              });
            });
          }
          // 判断有没有订单活动
          if (res.data.orderPromotions && res.data.orderPromotions.length > 0) {
            goodsItems.forEach((goodItem) => {
              //  if(!goodItem.groupSeckills&&!goodItem.promotions){
              let promotions = [];
              if (goodItem.promotions) {
                promotions = [...promotions, ...goodItem.promotions];
              }
              promotions = [...promotions, ...res.data.orderPromotions];

              this.$set(goodItem, "promotions", promotions);
              //  }
            });
          }
          console.log("订单活动检测", goodsItems);
          // 先把各种活动插入到单个活中--end

          goodsItems.forEach((goodItem, index) => {
            let rulesList = []; //合并后的所有活动规则
            this.$set(goodItem, "groupSeckillId", "");
            this.$set(goodItem, "goodsPromotionId", "");
            this.$set(goodItem, "acVisible", false);
            // 拼团处理--start
            if (goodItem.groupSeckills) {
              this.isGroupShow = true;
              this.$set(
                goodItem,
                "groupSeckillId",
                goodItem.groupSeckills[0].groupSeckillId
              ); ////拼团秒杀id
              this.$set(
                goodItem,
                "groupId",
                goodItem.groupSeckills[0].groupSeckillId
              ); ////拼团秒杀id
              this.$set(
                goodItem,
                "groupSeckillStatus",
                goodItem.groupSeckills[0].groupSeckillStatus
              );
              this.$set(goodItem, "isGroupItem", 1); //货品是否有活动
              this.$set(goodItem, "mtoFlag", goodItem.groupSeckills[0].mtoFlag); // 是否支持预售：0-否 1-是
              this.$set(goodItem, "maxNum", goodItem.groupSeckills[0].maxNum);
              this.$set(goodItem, "realSum", goodItem.groupSeckills[0].realSum);

              let itemSurplusNum =
                goodItem.groupSeckills[0].maxNum -
                goodItem.groupSeckills[0].realSum;
              if (
                itemSurplusNum > 0 &&
                goodItem.inStockUsableCount > 0 &&
                goodItem.groupSeckills[0].mtoFlag == 1
              ) {
                this.$set(goodItem, "inStockUsableCount", itemSurplusNum);
                this.$set(goodItem, "onWayUsableCount", 0);
              }
              if (index == 0) {
                this.groupGoodList.push(goodItem.groupSeckills[0]); //选择第一个货品拼团活动
                this.intervalTime("group"); //倒计时
              }
              goodItem.groupSeckills.forEach((groupItem) => {
                this.$set(
                  groupItem,
                  "itemSurplusNum",
                  groupItem.maxNum - groupItem.realSum > -1
                    ? groupItem.maxNum - groupItem.realSum
                    : 0
                );
                this.$set(groupItem, "salePrice", goodItem.salePrice);
                this.$set(
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

            this.$set(goodItem, "ordinaryActivityList", []);
            this.$set(goodItem, "activityList", []);
            this.$set(goodItem, "giftsList", []); //赠品
            let jieTiList = [];
            let ordinaryActivityList = [];
            let activityList = [];
            let giftsList = [];
            let specialsList = [];
            if (goodItem.promotions && goodItem.promotions.length > 0) {
              goodItem.promotions.forEach((proItem, proIndex) => {
                //阶梯活动
                if (proItem.promoType == 2) {
                  jieTiList.push(proItem);
                  proItem.rules.forEach((ruleItem, ruleIndex) => {
                    this.$set(ruleItem, "isCheck", false);
                    this.$set(ruleItem, "promoType", proItem.promoType); //活动类型，1 普通活动，2 阶梯活动 3 拼团
                    rulesList.push(ruleItem); //把活动合并到一起
                    if (
                      proIndex == 0 &&
                      ruleIndex == 0 &&
                      !goodItem.groupSeckills
                    ) {
                      this.$set(goodItem, "goodsPromotionId", ruleItem.id); //默认第一个活动规则id
                    }
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
                            : fomatFloat(
                                (goodItem.salePrice -
                                  cItem.reduction / cItem.oneBuyCount,
                                2)
                              );
                      } else {
                        price = fomatFloat(
                          (cItem.discount / 100) * goodItem.salePrice,
                          2
                        );
                      }
                      this.$set(cItem, "price", price);
                    });
                  });
                } else {
                  //普通活动

                  proItem.rules.forEach((ruleItem, ruleIndex) => {
                    this.$set(ruleItem, "promoType", proItem.promoType); //活动类型，1 普通活动，2 阶梯活动 3 拼团
                    this.$set(ruleItem, "isCheck", false);
                    rulesList.push(ruleItem); //把活动合并到一起
                    ordinaryActivityList.push(ruleItem);
                    let diff = 0;
                    if (this.getTimestamp(proItem.startTime) - newTime > 0) {
                      diff =
                        this.getTimestamp(salesList[i].startTime) - newTime;
                    } else {
                      diff = this.getTimestamp(proItem.endTime) - newTime;
                    }
                    let promobj = {
                      id: ruleItem.id,
                      activityType: proItem.promoType,
                      name: ruleItem.ruleName,
                      nameEn: ruleItem.ruleNameEn,
                      endTime: proItem.endTime,
                      startTime: proItem.startTime,
                      diff: diff,
                      conditions: ruleItem.conditions,
                    };
                    activityList.push(promobj);

                    ruleItem.conditions.forEach((cItem) => {
                      // 赠品
                      if (cItem.presents && cItem.presents.length > 0) {
                        giftsList.push(cItem.presents);
                      }
                      // 特价活动
                      if (cItem.specialFlag == 1 && cItem.specials) {
                        specialsList.push(cItem.specials);
                      }
                    });
                    if (
                      proIndex == 0 &&
                      ruleIndex == 0 &&
                      !goodItem.groupSeckills
                    ) {
                      this.$set(goodItem, "goodsPromotionId", ruleItem.id); //默认第一个活动规则id
                    }
                  });
                }
              });
            }
            this.$set(goodItem, "giftsList", giftsList);
            this.$set(goodItem, "specialsList", specialsList);
            this.$set(goodItem, "ordinaryActivityList", ordinaryActivityList);
            this.$set(goodItem, "activityList", activityList);
            this.$set(goodItem, "rulesList", rulesList); //合并的所有活动
            this.$set(goodItem, "jieTiList", jieTiList); //阶梯活动列表
            // 促销活动处理--end
            if (index == 0) {
              this.ladderPriceList =
                goodItem.jieTiList.length > 0
                  ? goodItem.jieTiList[0].rules[0].conditions
                  : []; //第一个货品的阶梯价
              this.promotionsList = goodItem.activityList; //第一个货品活动
              if (
                this.promotionsList.length > 0 &&
                goodItem.groupSeckillId == ""
              ) {
                this.intervalTime();
              }
              // 特价活动
              this.specialsList = goodItem.specialsList
                ? goodItem.specialsList
                : [];
            }
          });
        }
      });
    },

    // 左边图片模块---选择图片查看
    chooseImg(img, num) {
      this.shopImg = img;
      this.colorBlue = num;
    },

    // 加入和删除收藏
    collectSetFun(methods, id) {
      goodsCollection(methods, { id: id }).then((res) => {
        if (res.success) {
          if (methods == "POST") {
            // 收藏成功
            this.$message(this.$t("P.CollectSuccessfully"));
          } else {
            // 取消收藏成功
            this.$message(this.$t("P.CancelSuccessfully"));
          }
          this.goodsDetail.collectionFlag = methods == "POST" ? 1 : 0;
        } else {
          this.$message(res.errMessage);
        }
      });
    },

    // 跳转定制
    customize() {
      // 获取当前选中货品
      let item = this.goodsItems[this.selectIndex];
      this.$set(item, "goodsId", this.goodsDetail.id); // 商品id
      this.$set(item, "goodsNo", this.goodsDetail.goodsNo); // 商品编码
      this.$set(item, "goodsName", this.goodsDetail.goodsName); // 商品名称
      this.$set(item, "diyType", this.goodsDetail.diyType); // 定制类型
      this.$set(item, "goodsType", this.goodsDetail.goodsType); // 商品类型
      localStorage.setItem("diyItem", JSON.stringify(item)); // 存储当前选中货品（定制页面需要）
      // 手机定制 1， goodsType : 1-普通商品 2-原定制商品 3-新定制商品
      this.$router.push({
        path: "/customize",
        query: {
          goodsId: this.id, // 商品id
          itemId: item.id, // 货品id
          itemCode: item.itemCode, // 货品编码
        },
      });
    },
    // 选择在途
    changeTu() {
      this.tuType = !this.tuType;
    },
    // 装箱规格
    lookBox(goodsItems) {
      let that = this;
      // 装箱规格
      this.boxData = []; // 清空
      let boxDataList = [];
      goodsItems.forEach((goods) => {
        let boxsL = goods.boxs;
        let innerWeight = 0; // 内盒重量
        let innerNum = 0; // 内盒数量
        boxsL.forEach((item) => {
          console.log("boxsL = ", item);
          // if(item.defaultFlag==1){
          Vue.set(item, "size", ""); // 尺寸
          item.size =
            item.boxLength + "*" + item.boxWidth + "*" + item.boxHeight;
          if (item.boxType == "内盒" || item.boxType === "inner box") {
            if (this.$i18n.locale === "en") {
              item.boxType = "inner box";
            }
            innerWeight = item.boxWeight;
            innerNum = item.boxNum;
            item.boxWeight =
              goods.weight * item.boxNum + Number(item.boxWeight);
          } else if (item.boxType == "外盒" || item.boxType === "outer box") {
            if (this.$i18n.locale === "en") {
              item.boxType = "outer box";
            }
            console.log("sdsdsd", item.boxWeight);
            if (innerNum > 0) {
              // 当内盒数量大于0
              item.boxWeight =
                goods.weight * item.boxNum +
                innerWeight * (item.boxNum / innerNum) +
                Number(item.boxWeight);
            } else {
              item.boxWeight =
                goods.weight * item.boxNum + Number(item.boxWeight);
            }
            item.boxWeight = item.boxWeight.toFixed(0);
          }
          console.log(item);
          if (
            item.boxType == "内盒" ||
            item.boxType == "inner box" ||
            item.boxType == "外盒" ||
            item.boxType == "outer box"
          ) {
            boxDataList.push(item);
          }
          // }
        });
      });
      // 去重装箱规格的数组对象
      var hash = {};
      boxDataList = boxDataList.reduce(function (item, next) {
        hash[next.boxType]
          ? ""
          : (hash[next.boxType] = true && item.push(next));
        return item;
      }, []);
      that.boxData = boxDataList;
      console.log("装箱规格：", that.boxData);
    },
    // 点击放大上传的图片
    magnify(img) {
      this.showImgurl = true;
      this.imgurl = img;
    },
    // 关闭放大的图片
    shutLogImg() {
      this.showImgurl = false;
    },
    // 打开活动弹框--选择活动-Y
    lookActivityDetail(item, index) {
      this.promotionsIndex = 0;
      if (index) {
        this.promotionsIndex = index;
      }
      // 特价活动
      if (this.specialsList.length > 0) {
        this.specialsList = this.promotionsList[index].conditions[0].specials;
      }

      // 赠品
      let goodsItems = this.goodsItems;
      if (
        goodsItems.activityList &&
        goodsItems.activityList.length > 0 &&
        goodsItems[selectIndex].activityList[index].rules[0].conditions[0]
          .presents
      ) {
        this.goodsItems[selectIndex].giftsList =
          this.goodsItems[selectIndex].activityList[
            index
          ].rules[0].conditions[0].presents;
      }
      // 查看活动详情
      let routeData = null;
      if (item === null) {
        routeData = this.$router.resolve({ name: "ActivityColumn" });
      } else {
        let name = null;
        if (this.$i18n.locale === "zh" || !item.nameEn == true) {
          name = item.name;
        } else {
          name = item.nameEn;
        }
        routeData = this.$router.resolve({
          name: "ActivityColumn",
          query: { activityName: name },
        });
      }
      window.open(routeData.href, "_blank");
    },

    // 拼团和阶梯价活动倒计时
    intervalTime(type) {
      if (this.interval !== null) {
        clearInterval(this.interval);
      }
      console.log("pingtuan", this.groupGoodList);
      if (type && type === "group") {
        // 拼团
        if (this.groupGoodList && this.groupGoodList.length > 0) {
          this.interval = setInterval(() => {
            // 获取当前时间，同时得到订单付款截止时间数组
            let newTime = new Date().getTime();
            // 计算相差天数
            let isInterval = false;
            this.groupGoodList.forEach((item) => {
              isInterval = true;
              let diff = "";
              if (this.getTimestamp(item.startTime) - newTime > 0) {
                item.status = 0;
                diff = this.getTimestamp(item.startTime) - newTime;
              } else {
                item.status = 1;
                diff = this.getTimestamp(item.endTime) - newTime;
              }

              if (diff <= 0) {
                diff = 0;
                clearInterval(this.interval);
              }
              this.$set(item, "diff", diff);
            });
            if (!isInterval) {
              clearInterval(this.interval);
            }
          }, 1000);
        }
      } else {
        // 其他活动
        if (
          (this.promotionsDifferential !== null &&
            this.promotionsDifferential.length > 0) ||
          this.promotionsList.length > 0
        ) {
          this.interval = setInterval(() => {
            // 获取当前时间，同时得到订单付款截止时间数组
            let newTime = new Date().getTime();
            // 计算相差天数
            let isInterval = false;
            this.promotionsList.forEach((item) => {
              if (item.diff === undefined || item.diff > 0) {
                isInterval = true;
                item.diff = this.getTimestamp(item.endTime) - newTime;
              }
            });
            if (!isInterval) {
              clearInterval(this.interval);
            }
          }, 1000);
        }
      }
    },

    fixNum(time) {
      var timer = (Array(2).join(0) + time).slice(-2);
      if (timer > 0) {
        return timer;
      } else {
        return 0;
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
    // 货品-选择什么包装下单
    handleCheck(row, column, e, item, num) {
      this.actualSum = 0;
      this.discountAmount = 0;
      this.totalPrice = 0;
      this.totalCount = 0;
      this.$set(item, "count", (item.count / num) * row.boxNum);
      this.$set(item, "choosedBoxInfo", row.boxType);
      this.$set(item, "choosedBoxNum", row.boxNum);
      this.$set(item, "boxId", row.id);
      this.$set(item, "boxNum", row.boxNum);
      this.$set(item, "lastPrice", (item.lastPrice / num) * row.boxNum);
      this.goodsItems.forEach((gItem) => {
        if (gItem.lastPrice) {
          this.actualSum += gItem.lastPrice;
          this.totalPrice += gItem.count * gItem.salePrice;
          this.totalCount += gItem.count;
        }
      });
      this.discountAmount = Number(this.totalPrice) - Number(this.actualSum);
      let refName = this.refNamePopover + item.id;
      this.$refs[refName][0].doClose();
    },
    //获取系统配置
    userShopSettingFun() {
      let tip = localStorage.getItem("autoDelivery");
      userShopSetting().then((res) => {
        if (res.success) {
          this.stockShowFlag = res.data.stockShowFlag; //库存显示方式：0 实际库存 1模糊库存
          this.stockShowNumber = res.data.stockShowNumber; // 库存临界值
          if (
            (tip == 1 && res.data.stiffUseHint == 1) ||
            (tip == 0 && res.data.noStiffUseHint == 1)
          ) {
            this.orderhint = res.data.hint;
          }
        }
      });
    },
    // 固定详情tab
    handleTabFix() {
      var scrollTop =
        window.pageYOffset ||
        document.documentElement.scrollTop ||
        document.body.scrollTop;
      var height = document.getElementById("fixedNavBar").clientHeight + 100;
      var goodsDetailTop = document.getElementById("goodsDetail").offsetTop;
      var specDetailTop = document.getElementById("specDetail").offsetTop;
      var packDetailTop = document.getElementById("packDetail").offsetTop;
      var offsetTop = document.getElementById("fixedNavBar").offsetTop;
      if (scrollTop >= offsetTop) {
        this.fixedTab = true;
        this.isBackToTop = true;
      }
      if (scrollTop - height <= goodsDetailTop) {
        this.fixedTab = false;
        this.isBackToTop = false;
      }

      if (Number(scrollTop + height) > packDetailTop) {
        this.curTab = 3;
      } else if (
        Number(scrollTop + height) > goodsDetailTop &&
        Number(scrollTop + height) <= specDetailTop
      ) {
        this.curTab = 1;
      } else if (
        Number(scrollTop + height) > specDetailTop &&
        Number(scrollTop + height) <= packDetailTop
      ) {
        this.curTab = 2;
      }
    },
    // 详情tab 点击跳转指定位置
    handleTabChange(tab) {
      var height = document.getElementById("fixedNavBar").clientHeight;
      var goodsDetailTop = document.getElementById("goodsDetail").offsetTop;
      var specDetailTop =
        document.getElementById("specDetail").offsetTop - height;
      var packDetailTop =
        document.getElementById("packDetail").offsetTop - height;
      if (tab == 1) {
        window.scrollTo({
          top: goodsDetailTop,
        });
      }
      if (tab == 2) {
        window.scrollTo({
          top: specDetailTop,
        });
      }
      if (tab == 3) {
        window.scrollTo({
          top: packDetailTop,
        });
      }
    },
    // 回到顶部
    backToTop() {
      this.isBackToTop = false;
      window.scrollTo({ top: 0 });
    },
    // 跳转 - 拼团列表
    goGroupBuying() {
      this.$router.push({ name: "GroupBuying" });
    },

    // 商品加减号操作
    handleInput(num, itemId) {
      // 监听，字组件input自定义事件
      this.totalCount = 0;
      this.actualSum = 0;
      this.totalPrice = 0;
      this.discountAmount = 0;
      this.value = num;
      this.goodsItems.forEach((item) => {
        this.totalPrice += item.count * item.salePrice;
        if (item.count && item.count > 0) {
          this.totalCount += item.count;
        }
        if (item.id == itemId) {
          this.$set(item, "conditionsId", "");
          //      item.count=Number(num)*item.choosedBoxNum
          //      console.log('加减单项：',item);

          // 判断库逻辑(//是否包含在途库存)
          let stockNum = this.tuType
            ? item.onWayUsableCount + item.inStockUsableCount
            : item.inStockUsableCount;
          if (item.count > stockNum) {
            //  this.$message('库存不足');
          }

          //  item.choosedBoxNum   装箱规格

          if (item.count && item.count > 0) {
            let lastPrice = item.count * item.salePrice;
            // 计算价格--start

            // 促销活动--需要计算是否累计
            let itemTotalCount = item.count;
            let zaiKuCount =
              itemTotalCount <= item.inStockUsableCount
                ? itemTotalCount
                : item.inStockUsableCount;
            let zaiTuCount =
              itemTotalCount <= item.inStockUsableCount
                ? 0
                : itemTotalCount - item.inStockUsableCount;
            this.$set(item, "zaiKuCount", zaiKuCount);
            this.$set(item, "zaiTuCount", zaiTuCount);

            // goodsPromotionId groupSeckillId
            if (
              item.goodsPromotionId &&
              item.goodsPromotionId != "" &&
              item.groupSeckillId == ""
            ) {
              item.promotions.forEach((promoItem) => {
                let onWayFlag = promoItem.onWayFlag; //在途商品是否参与活动 1.参与，0.不参与
                let promoStatus = promoItem.promoStatus; //促销状态：0 未开始, 1 促销中 2 已过期 3 提前结束
                let promoType = promoItem.promoType; //活动类型，1 普通活动，2 阶梯活动
                promoItem.rules.forEach((ruleItem) => {
                  let isOneBuyCount =
                    onWayFlag == 0 ? zaiKuCount : zaiKuCount + zaiTuCount;
                  let isOneBuyMoney =
                    onWayFlag == 0
                      ? zaiKuCount * item.salePrice
                      : item.count * item.salePrice;
                  if (item.goodsPromotionId == ruleItem.id) {
                    //货品活动是否累计 addUpFlag，1是 0否( ruleType 规则对象是2或3时有效)
                    if (ruleItem.addUpFlag == 1 || ruleItem.ruleType == 1) {
                      let childrenList = this.goodsItems;
                      let zCoun = zaiKuCount;
                      let tCount = zaiTuCount;
                      let zPrice = zaiKuCount * item.salePrice;
                      let tPrice = zaiTuCount * item.salePrice;
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
                      if (promoStatus == 1) {
                        //moneyOrCount 规则形式：1金额 2数量
                        if (
                          (ruleItem.moneyOrCount == 1 &&
                            isOneBuyMoney >= cItem.oneBuyMoney) ||
                          (ruleItem.moneyOrCount == 2 &&
                            isOneBuyCount >= cItem.oneBuyCount)
                        ) {
                          this.$set(item, "conditionsId", cItem.id);
                          // specialFlag 是否特价，1是 0否
                          if (cItem.specialFlag == 1) {
                            lastPrice =
                              onWayFlag == 0
                                ? zaiKuCount * cItem.specialPrice +
                                  zaiTuCount * item.salePrice
                                : (zaiKuCount + zaiTuCount) *
                                  cItem.specialPrice;

                            //  活动累计情况下，相同的货品都按特价计算
                            if (
                              ruleItem.addUpFlag == 1 &&
                              ruleItem.ruleType != 1
                            ) {
                              let childrenList = this.goodsItems;
                              for (let i = 0; i < childrenList.length; i++) {
                                if (
                                  childrenList[i].id != item.id &&
                                  childrenList[i].goodsPromotionId ==
                                    item.goodsPromotionId &&
                                  childrenList[i].count > 0
                                ) {
                                  // let childrenTotalPrice=onWayFlag==0?
                                  //     (childrenList[i].zaiKuCount*cItem.specialPrice+childrenList[i].zaiTuCount*childrenList[i].salePrice):
                                  //     (childrenList[i].count*cItem.specialPrice);
                                  // this.$set(childrenList[i],'lastPrice',childrenTotalPrice);
                                  // this.$set(childrenList[i],'conditionsId',cItem.id);

                                  for (
                                    let j = 0;
                                    j < childrenList[i].promotions.length;
                                    j++
                                  ) {
                                    let rulesF =
                                      childrenList[i].promotions[j].rules;
                                    for (let a = 0; a < rulesF.length; a++) {
                                      if (
                                        item.goodsPromotionId == rulesF[a].id
                                      ) {
                                        let conditions = rulesF[a].conditions;
                                        for (
                                          let b = 0;
                                          b < conditions.length;
                                          b++
                                        ) {
                                          let childrenTotalPrice =
                                            onWayFlag == 0
                                              ? childrenList[i].zaiKuCount *
                                                  conditions[b].specialPrice +
                                                childrenList[i].zaiTuCount *
                                                  childrenList[i].salePrice
                                              : childrenList[i].itemCount *
                                                conditions[b].specialPrice;
                                          this.$set(
                                            childrenList[i],
                                            "lastPrice",
                                            childrenTotalPrice
                                          );
                                          this.$set(
                                            childrenList[i],
                                            "conditionsId",
                                            cItem.id
                                          );
                                        }
                                      }
                                    }
                                  }
                                }
                              }
                            }
                          } else {
                            //reduceOrPresent 促销统计方式：1满减 2满赠
                            if (cItem.reduceOrPresent == 1) {
                              // reduceType 满减类型， 2折扣  1减免
                              if (cItem.reduceType == 2) {
                                let zhekouPrice = fomatFloat(
                                  (item.salePrice * cItem.discount) / 100,
                                  2
                                );
                                lastPrice =
                                  onWayFlag == 0
                                    ? zaiKuCount * zhekouPrice +
                                      zaiTuCount * item.salePrice
                                    : (zaiKuCount + zaiTuCount) * zhekouPrice;

                                //  活动累计情况下，相同的货品都按减免计算
                                if (
                                  ruleItem.addUpFlag == 1 &&
                                  ruleItem.ruleType != 1
                                ) {
                                  let childrenList = this.goodsItems;
                                  for (
                                    let i = 0;
                                    i < childrenList.length;
                                    i++
                                  ) {
                                    if (
                                      childrenList[i].id != item.id &&
                                      childrenList[i].goodsPromotionId ==
                                        item.goodsPromotionId &&
                                      childrenList[i].count > 0
                                    ) {
                                      let zhekouPrice2 = fomatFloat(
                                        (childrenList[i].salePrice *
                                          cItem.discount) /
                                          100,
                                        3
                                      );

                                      let childrenTotalPrice =
                                        onWayFlag == 0
                                          ? childrenList[i].zaiKuCount *
                                              zhekouPrice2 +
                                            childrenList[i].zaiTuCount *
                                              childrenList[i].salePrice
                                          : childrenList[i].count *
                                            zhekouPrice2;
                                      this.$set(
                                        childrenList[i],
                                        "lastPrice",
                                        childrenTotalPrice
                                      );
                                      this.$set(
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
                                  let childrenList = this.goodsItems;
                                  for (
                                    let i = 0;
                                    i < childrenList.length;
                                    i++
                                  ) {
                                    if (
                                      childrenList[i].goodsPromotionId ==
                                        item.goodsPromotionId &&
                                      childrenList[i].count > 0
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
                                              : childrenList[i].count *
                                                childrenList[i].salePrice *
                                                (cItem.reduction /
                                                  cItem.oneBuyMoney)
                                            : onWayFlag == 0
                                            ? childrenList[i].zaiKuCount *
                                              (cItem.reduction /
                                                cItem.oneBuyCount)
                                            : childrenList[i].count *
                                              (cItem.reduction /
                                                cItem.oneBuyCount);
                                      } else {
                                        reduction2 =
                                          (reduction2 / isOneBuyCount) *
                                          childrenList[i].count;
                                      }

                                      let childrenTotalPrice = fomatFloat(
                                        childrenList[i].count *
                                          childrenList[i].salePrice -
                                          reduction2,
                                        2
                                      );
                                      if (childrenList[i].id == item.id) {
                                        lastPrice = childrenTotalPrice;
                                      }
                                      this.$set(
                                        childrenList[i],
                                        "lastPrice",
                                        childrenTotalPrice
                                      );
                                      this.$set(
                                        childrenList[i],
                                        "conditionsId",
                                        cItem.id
                                      );
                                      console.log("计算L:", childrenTotalPrice);
                                    }
                                  }
                                } else {
                                  let reduction = cItem.reduction;
                                  // 判断是否叠加的减免金额
                                  if (cItem.reductionPresentAddFlag == 1) {
                                    reduction =
                                      ruleItem.moneyOrCount == 1
                                        ? onWayFlag == 0
                                          ? zaiKuCount *
                                            item.salePrice *
                                            (cItem.reduction /
                                              cItem.oneBuyMoney)
                                          : item.count *
                                            item.salePrice *
                                            (cItem.reduction /
                                              cItem.oneBuyMoney)
                                        : onWayFlag == 0
                                        ? zaiKuCount *
                                          (cItem.reduction / cItem.oneBuyCount)
                                        : item.count *
                                          (cItem.reduction / cItem.oneBuyCount);
                                  }

                                  // 订单活动--（订单活动没有累计）

                                  if (ruleItem.ruleType == 1) {
                                    let childrenList = this.goodsItems;
                                    for (
                                      let i = 0;
                                      i < childrenList.length;
                                      i++
                                    ) {
                                      // &&childrenList[i].id!==item.id
                                      if (
                                        childrenList[i].goodsPromotionId ==
                                          item.goodsPromotionId &&
                                        childrenList[i].count > 0
                                      ) {
                                        let childrenTotalPrice = fomatFloat(
                                          childrenList[i].count *
                                            childrenList[i].salePrice,
                                          2
                                        );

                                        // 判断是否叠加的减免金额

                                        if (
                                          cItem.reductionPresentAddFlag == 1
                                        ) {
                                          let reduction2 =
                                            ruleItem.moneyOrCount == 1
                                              ? onWayFlag == 0
                                                ? childrenList[i].zaiKuCount *
                                                  childrenList[i].salePrice *
                                                  (cItem.reduction /
                                                    cItem.oneBuyMoney)
                                                : childrenList[i].count *
                                                  childrenList[i].salePrice *
                                                  (cItem.reduction /
                                                    cItem.oneBuyMoney)
                                              : onWayFlag == 0
                                              ? childrenList[i].zaiKuCount *
                                                (cItem.reduction /
                                                  cItem.oneBuyCount)
                                              : childrenList[i].count *
                                                (cItem.reduction /
                                                  cItem.oneBuyCount);
                                          let reduction3 = fomatFloat(
                                            reduction2,
                                            2
                                          );
                                          childrenTotalPrice = fomatFloat(
                                            childrenList[i].count *
                                              childrenList[i].salePrice -
                                              reduction2,
                                            2
                                          );

                                          if (childrenList[i].id == item.id) {
                                            lastPrice = childrenTotalPrice;
                                          }
                                        } else {
                                          this.$set(
                                            childrenList[i],
                                            "allOrderMoney",
                                            cItem.reduction
                                          );
                                          lastPrice = fomatFloat(
                                            item.count * item.salePrice -
                                              reduction,
                                            2
                                          );
                                        }
                                        this.$set(
                                          childrenList[i],
                                          "lastPrice",
                                          childrenTotalPrice
                                        );
                                        this.$set(
                                          childrenList[i],
                                          "conditionsId",
                                          cItem.id
                                        );
                                      }
                                    }
                                  } else {
                                    let reduction4 = fomatFloat(reduction, 2);
                                    lastPrice = fomatFloat(
                                      item.count * item.salePrice - reduction4,
                                      2
                                    );
                                  }
                                }
                              }
                            } else {
                              // reduceOrPresent=cItem.reduceOrPresent;
                              // reduceOrPresentId=cItem.id;
                              // presentCount=cItem.presentCount;
                              if (
                                ruleItem.addUpFlag == 1 &&
                                ruleItem.ruleType != 1
                              ) {
                                let childrenList = this.goodsItems;
                                for (let i = 0; i < childrenList.length; i++) {
                                  if (
                                    childrenList[i].id != item.id &&
                                    childrenList[i].goodsPromotionId ==
                                      item.goodsPromotionId &&
                                    childrenList[i].count > 0
                                  ) {
                                    this.$set(
                                      childrenList[i],
                                      "conditionsId",
                                      cItem.id
                                    );
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    });
                  }
                });
              });
            }

            // 拼团秒杀活动
            if (item.groupSeckillId && item.groupSeckillId != "") {
              item.groupSeckills.forEach((groupItem) => {
                if (groupItem.groupSeckillId == item.groupSeckillId) {
                  if (
                    item.count >= groupItem.minNum &&
                    (groupItem.mtoFlag == 1
                      ? item.count <= groupItem.maxNum - groupItem.realSum
                      : true)
                  ) {
                    lastPrice = item.count * groupItem.groupSeckillPrice;
                    this.$set(item, "groupSeckillId", groupItem.groupSeckillId);
                  } else {
                    this.$set(item, "groupSeckillId", "");
                  }
                }
              });
            }
			
            this.$set(item, "lastPrice", lastPrice);
			
            // 计算价格--end
          }
        }
      });
      console.log("最终封装的goodsItems = ", this.goodsItems);
      this.goodsItems.forEach((item) => {
        if (item.count > 0) {
		  console.log("item.lastPrice = " + item.lastPrice);
          this.actualSum += Number(item.lastPrice);
          this.discountAmount = this.totalPrice - this.actualSum;
        }
      });
      console.log("商品优惠价", this.actualSum);
      console.log("商品添加：", this.goodsItems);

      // 赠品
      this.handleSongData();
    },

    // 赠品
    handleSongData() {
      let list = this.goodsItems;
      this.presentList = []; //赠品列表
      let ptesentIds = []; //赠品ids
      list.forEach((item) => {
        if (item.count && item.count > 0) {
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
                  // 货品活动是否累计 addUpFlag，1是 0否
                  if (ruleItem.addUpFlag == 1) {
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
                      if (cItem.reduceOrPresent == 2) {
                        cItem.presents.forEach((preSentItem) => {
                          this.$set(preSentItem, "num", 0);
                        });

                        this.$set(item, "conditionsId", cItem.id);
                        
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
                            : 0;

                        let totalPresentCount3 = 
                          cItem.reductionPresentAddFlag == 1
                            ? parseIntCount * cItem.presentCount
                            : cItem.presentCount;

                        let totalPresentCount2 =
                          totalPresentCount3 > cItem.presents[0].count
                            ? cItem.presents[0].count
                            : totalPresentCount3;
                            
                        // 是否叠加 reductionPresentAddFlag：1 是，0 否
                        if (cItem.reductionPresentAddFlag == 1) {
                          let dieJiaNum = 0;
                          // 计算叠加的数量/金额
                          if (ruleItem.moneyOrCount == 1) {
                            dieJiaNum = parseInt(isOneBuyMoney / cItem.oneBuyMoney);
                          } else {
                            dieJiaNum = parseInt(isOneBuyCount / cItem.oneBuyCount);
                          }
                          totalPresentCount2 = cItem.presentCount * dieJiaNum;
                        }

                        // preList.presents=[...preList.presents,...cItem.presents];
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

            // 赠品列表去重
            for (let i = 0; i < this.presentList.length; i++) {
              for (let j = i + 1; j < this.presentList.length; j++) {
                  if (this.presentList[i].id == this.presentList[j].id) {
                      this.presentList.splice(j, 1);
                      j--;
                  };
              };
            };

            //  let rulesList=preItem.rules.filter(ruleItem=>ruleItem.id==item.goodsPromotionId);

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
                    this.$set(
                      present,
                      "count",
                      presentStockNum > present.presentCount
                        ? present.presentCount
                        : presentStockNum
                    ); // 赠品库存总数
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
      let giftNum = 0; // 已为您选择赠品数量默认值
      let presentStockNum = 0; // 赠品总库存数
      this.presentList.forEach((present) => {
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
          present.presents.forEach((child, cIndex) => {
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
            }
            this.$set(child, "itemCount", child.num);
          });
        }
      });

      // 判断赠品总库存数与选择赠品数，设置已选赠品值
      if (presentStockNum >= giftNum) {
        this.giftNum = giftNum;
        this.presentList.forEach((present) => {
          this.$set(present, "count", giftNum);
        });
      } else {
        this.giftNum = presentStockNum;
        this.presentList.forEach((present) => {
          this.$set(present, "count", presentStockNum);
        });
      }
    },

    // 加入购物车
    addToShopingCart() {
      if (this.userId === null || this.userId === "") {
        loginOut();
      } else if (this.capitalStatus == 2 && this.freezeStatus == 1) {
        console.log("加入购物车商品列表：", this.goodsItems);
        //  协议弹框
        if (!this.agreementStatus) {
          this.showOpenCut = true;
          return;
        }
        let data = this.goodsDetail;
        let list = this.goodsItems;
        let paramsList = [];
        list.forEach((item) => {
          if (item.count > 0) {
            let objL = {
              imageUrl: item.itemImg,
              cartType: 1, //加购类型：1 分销商 2 C端客户
              diyType: data.diyType, //定制类型 0-标准定制 1-DIY定制
              goodsId: data.id, //商品id
              goodsName: data.goodsName,
              goodsNo: data.goodsNo, //商品编码
              goodsPromotionId: item.goodsPromotionId
                ? item.goodsPromotionId
                : "", //商品促销活动Id(活动条件id)
              goodsType: data.goodsType, //商品类型 1-普通 2-定制
              groupSeckillId: item.groupSeckillId ? item.groupSeckillId : "",
              itemCode: item.itemCode, //货品编码
              itemCount: item.count, //购物数量
              itemId: item.id, //货品id
              itemName:
                this.$i18n.locale === "zh"
                  ? item.itemName
                  : item.itemNameEn
                  ? item.itemNameEn
                  : item.itemName, //货品名称
              itemNameEn: item.itemNameEn ? item.itemNameEn : "", //货品英文名称
              itemType: item.itemType, //是否赠品 1 非赠品 2 赠品
              specsName:
                this.$i18n.locale === "zh"
                  ? item.specsName
                  : item.specsNameEn
                  ? item.specsNameEn
                  : item.specsName, //货品规格
              specsNameEn: item.specsNameEn ? item.specsNameEn : "",
              colorName:
                this.$i18n.locale === "zh"
                  ? item.colorName
                  : item.colorNameEn
                  ? item.colorNameEn
                  : item.colorName, //货品颜色
              colorNameEn: item.colorNameEn ? item.colorNameEn : "",
              weight: item.weight, //重量
              lenght: item.length,
              width: item.width,
              height: item.height,
              barCode: item.barCode,
              unit: item.boxId, // 装箱规格id
            };
            paramsList.push(objL);
          }
        });

        // 赠品
        if (this.presentList.length > 0) {
          this.presentsConfirmFun(); // 赠品确定
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
                itemName:
                  this.$i18n.locale === "zh"
                    ? item.itemName
                    : item.itemNameEn
                    ? item.itemNameEn
                    : item.itemName, //货品名称
                itemNameEn: item.itemNameEn ? item.itemNameEn : "", //货品英文名称
                itemType: item.itemType, //是否赠品 1 非赠品 2 赠品
                specsName:
                  this.$i18n.locale === "zh"
                    ? item.specsName
                    : item.specsNameEn
                    ? item.specsNameEn
                    : item.specsName, //货品规格
                colorName:
                  this.$i18n.locale === "zh"
                    ? item.colorName
                    : item.colorNameEn
                    ? item.colorNameEn
                    : item.colorName, //货品颜色
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

        if (paramsList.length > 0) {
          addShoppingcart(paramsList).then((res) => {
            if (res.success) {
              let title =
                this.$i18n.locale == "zh"
                  ? "加入购物车成功"
                  : "Added to cart successfully";
              this.$message.success(title);
              this.shoppingcartListFun(); //通过查购物车列表的长度来更新购物车的数量
            } else {
              this.$message(res.errMessage);
            }
          });
        } else {
          let title =
            this.$i18n.locale == "zh"
              ? "请添加至少一个商品"
              : "Please add at least one item";
          this.$message(title);
        }
      }
    },

    // 查询购物车列表
    shoppingcartListFun() {
      shoppingcartList().then((res) => {
        if (res.success) {
          this.$refs.header.cartCount = res.data.length;
        }
      });
    },
    // 签署协议-y
    agreeGoonFun() {
      let userId = localStorage.getItem("userId");
      signAgreement({
        agreementId: this.agreementId,
        distributorId: userId,
      }).then((res) => {
        if (res.success) {
          this.showOpenCut = false;
          this.agreementStatus = true;
        }
      });
    },

    // 立即购买
    buyNowFun() {
      if (this.userId === null || this.userId === "") {
        loginOut();
      } else if (this.capitalStatus == 2 && this.freezeStatus == 1) {
        console.log("购买的商品列表：", this.goodsItems);
        let list = this.goodsItems;
        this.orderList = [];

        //  this.presentList=[]; //赠品列表
        //  let ptesentIds=[];  //赠品ids
        list.forEach((item) => {
          if (item.count && item.count > 0) {
            this.$set(item, "goodsType", this.goodsType);
            this.$set(item, "itemCount", item.count);
            this.$set(item, "totalPrice", item.lastPrice);
            if (item.count <= item.inStockUsableCount) {
              this.$set(item, "zaiKuCount", item.count);
              this.$set(item, "zaiTuCount", 0);
            } else {
              this.$set(item, "zaiKuCount", item.inStockUsableCount);
              this.$set(
                item,
                "zaiTuCount",
                item.count - item.inStockUsableCount
              );
            }
            this.orderList.push(item);

            // 集合赠品列表
            // if(item.goodsPromotionId&&item.goodsPromotionId!='no'){
            //     let promitonList= item.promotions;
            //     // 对应活动的赠品

            //     promitonList.forEach(preItem=>{
            //       let onWayFlag=preItem.onWayFlag;  //在途商品是否参与活动 1.参与，0.不参与
            //       let promoStatus=preItem.promoStatus; //促销状态：0 未开始, 1 促销中 2 已过期 3 提前结束
            //       let promoType=preItem.promoType;  //活动类型，1 普通活动，2 阶梯活动
            //        preItem.rules.forEach(ruleItem=>{
            //            if(item.goodsPromotionId==ruleItem.id){
            //                 let preList={
            //                   id:ruleItem.id,
            //                   ruleName:ruleItem.ruleName,
            //                   conditionName:'',  //条件名称
            //                   presentCount:0,  //满赠数量
            //                   count:0,
            //                   presents:[],
            //                 };
            //                 let isOneBuyCount=onWayFlag==0?item.zaiKuCount:(item.zaiKuCount+item.zaiTuCount);
            //                 let isOneBuyMoney=onWayFlag==0?(item.zaiKuCount*item.salePrice):(item.count*item.salePrice);
            //                 //货品活动是否累计 addUpFlag，1是 0否( ruleType 规则对象是2或3时有效)
            //                 if(ruleItem.addUpFlag==1&&ruleItem.ruleType!=1){

            //                     let childrenList=this.goodsItems;
            //                     let zCoun=item.zaiKuCount;
            //                     let tCount=item.zaiTuCount;
            //                     let zPrice=item.zaiKuCount*item.salePrice;
            //                     let tPrice=item.zaiTuCount*item.salePrice;
            //                     for(let i=0;i<childrenList.length;i++){
            //                       // 判断同个活动累计数量
            //                       if(childrenList[i].id!=item.id&&childrenList[i].goodsPromotionId==item.goodsPromotionId&&childrenList[i].count>0){
            //                           zCoun+=childrenList[i].zaiKuCount;
            //                           tCount+=childrenList[i].zaiTuCount;
            //                           zPrice+=(childrenList[i].zaiKuCount*childrenList[i].salePrice)
            //                           tPrice+=(childrenList[i].zaiTuCount*childrenList[i].salePrice)

            //                       }
            //                     }
            //                     isOneBuyCount=(onWayFlag==0)?zCoun:(zCoun+tCount);
            //                     isOneBuyMoney=(onWayFlag==0)?zPrice:(zPrice+tPrice);

            //                 }
            //                 ruleItem.conditions.forEach(cItem=>{
            //                    // moneyOrCount 规则形式：1金额 2数量
            //                    //  onWayFlag 在途商品是否参与活动 1.参与，0.不参与
            //                    if((ruleItem.moneyOrCount==1&&isOneBuyMoney>=cItem.oneBuyMoney)||(ruleItem.moneyOrCount==2&&isOneBuyCount>=cItem.oneBuyCount)){
            //                       // reduceOrPresent:促销统计方式：1满减 2满赠
            //                       let totalPresentCount2=0;
            //                       if(cItem.reduceOrPresent==2){
            //                         this.$set(item,'conditionsId',cItem.id);
            //                         // 是否叠加 reductionPresentAddFlag  满减满赠是否叠加，1是 0否
            //                         let itemCountP=onWayFlag==0?item.zaiKuCount:item.itemCount;
            //                         let parseIntCount=ruleItem.moneyOrCount==1?
            //                         ((parseInt((itemCountP*item.salePrice)/cItem.oneBuyMoney)>0)?parseInt((itemCountP*item.salePrice)/cItem.oneBuyMoney):0)
            //                         :((parseInt(itemCountP/cItem.oneBuyCount)>0)?parseInt(itemCountP/cItem.oneBuyCount):1);

            //                         let totalPresentCount3=(cItem.reductionPresentAddFlag==1?(parseIntCount*cItem.presentCount):cItem.presentCount);
            //                         cItem.presents.forEach(preSentItem=>{
            //                             this.$set(preSentItem,'num',0)
            //                         })

            //                         totalPresentCount2=(totalPresentCount3>cItem.presents[0].count)?(cItem.presents[0].count):totalPresentCount3;
            //                         // preList.presents=[...preList.presents,...cItem.presents];
            //                         preList.presents=cItem.presents;
            //                         preList.conditionName=cItem.conditionName;
            //                         preList.presentCount=totalPresentCount2;
            //                       }
            //                   }
            //                 })

            //                 if(preList.presents.length>0){
            //                   if(ruleItem.addUpFlag==1){
            //                       this.presentList=[]
            //                   }
            //                     this.presentList.push(preList);

            //                   preList.presents.forEach(itemP=>{
            //                     ptesentIds.push(itemP.itemId);
            //                   })
            //                 }
            //            }
            //        })
            //     })

            //       //  let rulesList=preItem.rules.filter(ruleItem=>ruleItem.id==item.goodsPromotionId);

            //     if(ptesentIds.length>0){
            //         // 获取商品库存
            //         let stockParams={
            //             distributorId: this.userId,
            //             itemIdList : ptesentIds
            //         };

            //         listStockByCondition(stockParams).then((stockRes) => {
            //               if (stockRes.success) {
            //                 let stockResData=stockRes.data;
            //                 for ( let i = 0;i < stockResData.length;i++) {
            //                   this.presentList.forEach((item1) => {
            //                       item1.presents.forEach(item2=>{
            //                           this.$set(item2,'isDisabled',false);
            //                           if ( item2.itemId === stockResData[i].itemId) {
            //                               item2.inStockUsableCount =stockResData[i].inStockUsableCount>0?stockResData[i].inStockUsableCount:0;// 在库库存
            //                               item2.onWayUsableCount = (stockResData[i].onWayUsableCount-stockResData[i].inStockUsableCount)>0?(stockResData[i].onWayUsableCount-stockResData[i].inStockUsableCount):0;// 在途库存
            //                               let kucunCount=item2.inStockUsableCount+item2.onWayUsableCount;
            //                               this.$set(item2,'kucunCount',kucunCount);
            //                               this.$set(item2,'presentCount',item1.presentCount);
            //                             }
            //                       })

            //                   });
            //                 }
            //               }
            //         });
            //      }
            //     console.log('赠品列表',this.presentList);

            // }
          }
        });
        if (this.orderList.length == 0) {
          this.$message("请选择至少一个货品！");
        } else {
          //  协议弹框
          if (!this.agreementStatus) {
            this.showOpenCut = true;
            return;
          }
          // 赠品弹框
          if (this.presentList.length > 0) {
            this.presentsConfirmFun(); // 赠品确定
            if (this.presentBuyGoodsList.length > 0) {
              this.orderList = [...this.orderList, ...this.presentBuyGoodsList];
              localStorage.setItem(
                "shopOrderList",
                JSON.stringify(this.orderList)
              );

              this.$router.push({
                name: "ConsigneeInfor",
                query: { values: 1, onWaySplitFlag: 0, isTwoWay: 0 },
              }); //跳转下单页；
            } else {
              MessageBox.confirm(
                this.$t("Message.One"),
                this.$t("Message.Two"),
                {
                  confirmButtonText: this.$t("Message.Confirm"),
                  cancelButtonText: this.$t("Message.Cancel"),
                  type: "warning",
                }
              )
                .then(() => {
                  localStorage.setItem(
                    "shopOrderList",
                    JSON.stringify(this.orderList)
                  );
                  this.$router.push({
                    name: "ConsigneeInfor",
                    query: { values: 1, onWaySplitFlag: 0, isTwoWay: 0 },
                  }); //跳转下单页；
                })
                .catch(() => {
                  this.dialogVisible = true;
                });
            }
          } else {
            // 判断是否在途在库一起下单情况
            let zaiTuFlag = false;
            let zaiKuFlag = false;
            this.orderList.forEach((item) => {
              if (item.zaiKuCount > 0) {
                zaiKuFlag = true;
              }
              if (item.zaiTuCount > 0) {
                zaiTuFlag = true;
              }
            });
            if (zaiKuFlag && zaiTuFlag) {
              this.showChooseDelivery = true;
              this.deliveryData = this.orderList;
            } else {
              localStorage.setItem(
                "shopOrderList",
                JSON.stringify(this.orderList)
              );
              this.$router.push({
                name: "ConsigneeInfor",
                query: { values: 1, onWaySplitFlag: 0, isTwoWay: 0 },
              }); //跳转下单页；
            }
          }
        }
      }
    },

    // 赠品弹框加减号计算
    presentHandleChange(item, items, type) {
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
          this.$message("赠品数量已达上限");
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
              this.$message("赠品数量已达上限");
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
    },

    // 赠品弹框--确定
    presentsConfirmFun() {
      this.dialogVisible = false;

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

    // liu--------6.10-end
    // 关闭活动
    shutDialog(target) {
      if (target) {
        this.activityDialogVisible = false;
      }
    },
    // 配送方式选择弹窗关闭
    handleCloseDelivery() {
      this.showChooseDelivery = false;
      this.showLoading = false;
    },

    promotionsListFocus() {
      this.promotionsFocus = true;
      this.showPromotions = false;
    },
    handleClose() {
      if (this.showPromotions && !this.promotionsFocus) {
        this.showPromotions = false;
      }
    },
    showAllPromotions() {
      this.showPromotions = !this.showPromotions;
    },

    fLangChange(value) {
      window.localStorage.setItem("bLang", value);
      this.$i18n.locale = value.split("-")[0];
    },
    getRowClass({ rowIndex }) {
      // 改变表头样式
      if (rowIndex === 0) {
        return "background:#00c9dc;color:#fff;border-right: none;font-weight: bold;";
      } else {
        return "";
      }
    },

    goCustrom(code) {
      // 获取DIY编辑器地址
      var timestamp = Date.parse(new Date()); // 获取时间戳
      this.diyItemCode = code;
      let Source = 0; // 0：b2b diy定制 1：收单diy定制
      let Signature = "";
      let AppID = this.$AppID;
      let AppKey = this.$AppKey;
      this.showLoading = true;
      this.$api.get(this, "user/goodsdiy/getEditUrl").then((res) => {
        if (res.code === 0) {
          let code80 = AppID + this.diyItemCode + Source + res.token;
          let StringSha1 = AppID + AppKey + code80 + timestamp;
          this.showLoading = false;
          Signature = sha1(StringSha1);
          Signature = Signature.toLocaleUpperCase(); // 小写字母转大写字母
          window.location.href =
            res.editorUrl +
            "?Token=" +
            res.token +
            "&Timestamp=" +
            timestamp +
            "&Code80=" +
            this.diyItemCode +
            "&Signature=" +
            Signature +
            "&AppID=" +
            AppID +
            "&Source=" +
            Source; // 跳转链接
        }
      });
    },

    showMoreLabel() {
      // 更多活动标签显隐
      this.moreLabel = !this.moreLabel;
    },

    changeTab(index, groupName) {
      // 切换定制商品分组
      this.tabType = index;
      if (this.customizedItems[index].userItems.length === 0) {
        this.getGoodsDetail(groupName, index);
      }
    },

    // 立即购买
    buyNow() {
      var tempArray = []; // 临时数组
      var tempArrayOne = [];
      var generalList = []; // 普通商品
      var diyList = []; // 定制商品
      var groupList = []; // 拼团商品
      var curGoodsType = ""; // 商品类型
      var type = 0; // 判定货品订购数量不能大于库存，定制商品不需要考虑
      var tuType = this.tuType; // 是否包含在途库存
      let labelIndexString = "";
      window.localStorage.removeItem("songGoodsShop"); // 确认赠品之前先移除缓存赠品信息
      if (Number(this.goodsType) === 1) {
        if (this.goodsItems !== null) {
          var minTotal = 0; // 拼团起拼量
          var maxTotal = 0; // 拼团限购量
          var curTotal = 0; // 拼团当前数量
          var flag = 0;
          var groupArr = [];
          this.goodsItems.forEach((obj) => {
            if (
              obj.count !== undefined &&
              obj.count !== null &&
              Number(obj.count) !== 0
            ) {
              if (
                obj.labelIndexString &&
                labelIndexString.indexOf(obj.labelIndexString) === -1
              ) {
                labelIndexString += obj.labelIndexString;
              }
              // 拼团未开始
              if (obj.status !== null && Number(obj.status) === 0) {
                if (tuType === false) {
                  if (Number(obj.count) > obj.numInWarehouse) {
                    type = 1;
                    obj.lookNumInWarehouse = obj.numInWarehouse;
                    obj.lookStockItemCount =
                      Number(obj.count) - obj.numInWarehouse;
                  } else {
                    obj.lookNumInWarehouse = Number(obj.count);
                    obj.lookStockItemCount = 0;
                  }
                } else {
                  if (Number(obj.count) > obj.stockItemCount) {
                    type = 1;
                  }
                  if (Number(obj.count) > obj.numInWarehouse) {
                    obj.lookNumInWarehouse = obj.numInWarehouse;
                    obj.lookStockItemCount =
                      Number(obj.count) - obj.numInWarehouse;
                  } else {
                    obj.lookNumInWarehouse = Number(obj.count);
                    obj.lookStockItemCount = 0;
                  }
                }
              }

              // 拼团进行中
              if (Number(obj.status) === 1) {
                if (Number(obj.quantityIsMerge) === 1) {
                  // 合并计算
                  // curTotal += Number(obj.count);
                  if (groupArr.length > 0) {
                    groupArr.forEach((item) => {
                      if (groupArr.length > 0 && item.groupId === obj.groupId) {
                        // curTotal += Number(obj.count);
                        item.curTotal += Number(obj.count);
                      } else {
                        groupArr.push({
                          id: obj.id,
                          groupId: obj.groupId,
                          curTotal: Number(obj.count),
                          minTotal: Number(obj.minQuantity),
                          // maxTotal: Number(obj.itemSurplusNum)
                        });
                        curTotal = Number(obj.count);
                      }
                    });
                  } else {
                    groupArr.push({
                      id: obj.id,
                      groupId: obj.groupId,
                      curTotal: Number(obj.count),
                      minTotal: Number(obj.minQuantity),
                    });
                  }
                } else {
                  // 不合并计算
                  // curTotal = Number(obj.count);
                  groupArr.push({
                    id: obj.id,
                    groupId: obj.groupId,
                    curTotal: Number(obj.count),
                    minTotal: Number(obj.minQuantity),
                  });
                  curTotal = Number(obj.count);
                }
                minTotal = obj.minQuantity;
                if (obj.purchaseNum) {
                  // maxTotal = obj.purchaseNum;
                  groupArr.forEach((group) => {
                    if (group.id === obj.id) {
                      group.maxTotal = obj.purchaseNum;
                    }
                  });
                } else {
                  flag = 1;
                  // maxTotal = obj.itemSurplusNum;
                  groupArr.forEach((group) => {
                    if (group.id === obj.id) {
                      group.maxTotal = obj.itemSurplusNum;
                    }
                  });
                }
              }
            }
          });

          // 筛选判断，vFlag：是否高于限购量/剩余件数 mFlag：是否低于起拼量
          let vFlag = true;
          let mFlag = true;
          try {
            groupArr.forEach((item) => {
              if (item.curTotal > item.maxTotal) {
                vFlag = false;
                maxTotal = item.maxTotal;
                throw new Error("error");
              } else if (item.curTotal < item.minTotal) {
                mFlag = false;
                minTotal = item.minTotal;
                throw new Error("error");
              }
            });
          } catch (e) {
            console.log(e);
          }

          // 拼团在范围内
          // if (
          //   Number(curTotal) <= Number(maxTotal) &&
          //   Number(curTotal) >= Number(minTotal)
          // ) {
          if (vFlag) {
            this.goodsItems.forEach((obj) => {
              if (
                obj.count !== undefined &&
                obj.count !== null &&
                Number(obj.count) !== 0
              ) {
                var limit = {
                  goodsId: obj.goodsId,
                  itemId: obj.id,
                  itemCount:
                    obj.choosedBoxInfo !== ""
                      ? obj.count * obj.choosedBoxNum
                      : obj.count,
                  itemType: obj.itemType,
                };
                var product = {
                  num:
                    obj.choosedBoxInfo !== ""
                      ? obj.count * obj.choosedBoxNum
                      : obj.count,
                };
                this.limitPurchase.push(limit);
                tempArray.push(product);
                tempArrayOne.push(obj);
              }
            });
          }
          // 拼团判断是否高于限购量/剩余件数
          // if (Number(curTotal) > Number(maxTotal)) {
          if (!vFlag) {
            this.clickAgain = true;
            type = 1;
            if (this.$i18n.locale === "zh") {
              if (flag === 1) {
                this.$message.warning(
                  "货品订购数量不能高于剩余数量：" + maxTotal
                );
              } else {
                this.$message.warning(
                  "货品订购数量不能高于限购量：" + maxTotal
                );
              }
            } else {
              if (flag === 1) {
                this.$message.warning(
                  "The order quantity of products shall not be greater than left amount: " +
                    maxTotal
                );
              } else {
                this.$message.warning(
                  "The order quantity of products shall not be greater than maximum order quantity: " +
                    maxTotal
                );
              }
            }
            return false;
          }
          // 拼团判断是否低于起拼量
          // if (Number(curTotal) < Number(minTotal)) {
          if (!mFlag) {
            this.clickAgain = true;
            type = 1;
            if (this.$i18n.locale === "zh") {
              this.$message.warning("货品订购数量不能低于起拼量：" + minTotal);
            } else {
              this.$message.warning(
                "The order quantity of products shall not be lower than mininum order quantity: " +
                  minTotal
              );
            }
            return false;
          }
        }
      } else if (Number(this.goodsType) === 2) {
        if (
          this.customizedItems !== undefined &&
          this.customizedItems !== null
        ) {
          this.customizedItems.forEach((item) => {
            item.userItems.forEach((obj) => {
              if (
                obj.count !== undefined &&
                obj.count !== null &&
                Number(obj.count) !== 0
              ) {
                var limit = {
                  goodsId: obj.goodsId,
                  itemId: obj.id,
                  itemCount: obj.count,
                  itemType: 3,
                };
                var product = { num: obj.count };
                var products = {
                  brandId: this.customizedItems.brandId,
                  itemType: 3,
                  count: obj.count,
                  goodsName: obj.goodsName,
                  barCode: obj.barCode,
                  salePrice: obj.salePrice,
                  retailPrice: obj.retailPrice,
                  defaultPrice: obj.defaultPrice,
                  goodsId: obj.goodsId,
                  id: obj.id,
                  itemCode: obj.itemCode,
                  itemName: obj.itemName,
                  specificationValueName: obj.specificationValueName,
                  specificationValueNameEn: obj.specificationValueNameEn,
                  colorValueName: obj.colorValueName,
                  colorValueNameEn: obj.colorValueNameEn,
                };
                this.limitPurchase.push(limit);
                tempArray.push(product);
                tempArrayOne.push(products);
              }
            });
          });
        }
      }

      if (this.userId === null || this.userId === "") {
      } else if (
        Number(this.capitalStatus) === 2 &&
        Number(this.freezeStatus) === 1
      ) {
        var secondsArea = 1; // 设置0.5秒
        this.intervalid = setInterval(() => {
          secondsArea--;
          if (secondsArea === 0) {
            if (type === 0) {
              if (tempArray.length === 0) {
                if (this.$i18n.locale === "zh") {
                  this.$message.warning("货品订购数量不能为空");
                } else {
                  this.$message.warning(
                    "The order quantity of products cannot be null."
                  );
                }
                return false;
              } else {
                if (Number(this.goodsType) === 1) {
                  // 普通商品
                  if (labelIndexString.length > 1) {
                    // 选中的货品参加多个活动
                    for (let i = 0; i < this.activityPromotions.length; i++) {
                      this.activityPromotions[i].activityItem = []; // 先清空
                      for (let j = 0; j < tempArrayOne.length; j++) {
                        if (
                          tempArrayOne[j].labelIndexString.indexOf(
                            this.activityPromotions[i].labelIndex
                          ) !== -1
                        ) {
                          Vue.set(
                            tempArrayOne[j],
                            "remainNum",
                            tempArrayOne[j].count
                          ); // 剩余数量
                          Vue.set(tempArrayOne[j], "currentNum", 0); // 当前选择数量
                          Vue.set(
                            tempArrayOne[j],
                            "totalNum",
                            tempArrayOne[j].count
                          ); // 选中购买数量
                          this.activityPromotions[i].activityItem.push(
                            tempArrayOne[j]
                          );
                        } else {
                          Vue.set(
                            tempArrayOne[j],
                            "remainNum",
                            tempArrayOne[j].count
                          ); // 剩余数量
                        }
                        if (
                          this.activityPromotions[i].onWayAttendEventFlag ===
                            0 &&
                          tempArrayOne[j].numInWarehouse > 0
                        ) {
                          this.activityDialogVisible = true;
                          this.withoutActivityItem = tempArrayOne;
                        } else {
                          this.activityDialogVisible = true;
                          this.withoutActivityItem = tempArrayOne;
                        }
                      }
                    }
                    if (this.activityDialogVisible) {
                      return !this.activityDialogVisible;
                    }
                  } else if (labelIndexString.length === 1) {
                    this.hasActivity = true;
                  }
                  tempArrayOne.forEach((item) => {
                    if (item.ruleId) {
                      item.ruleId =
                        item.ruleId instanceof Array
                          ? item.ruleId[0]
                          : item.ruleId;
                    }

                    // 检验库存数据列表
                    var option = {
                      itemId: item.id,
                      count: item.choosedBoxNum
                        ? item.count * item.choosedBoxNum
                        : item.count,
                    };
                    var count = item.choosedBoxNum
                      ? item.count * item.choosedBoxNum
                      : item.count;
                    if (item.isGroupItem === 1 && item.status === 1) {
                      // 拼团商品
                      curGoodsType = 8;
                      if (count > 0) {
                        groupList.push(option);
                      }
                    } else {
                      // 普通商品
                      curGoodsType = 1;
                      if (count > 0) {
                        generalList.push(option);
                      }
                    }
                  }); // 转换规则ID

                  this.$api
                    .post(this, "user/u/order/restriction", {
                      goods: this.limitPurchase,
                    })
                    .then((res) => {
                      // 限购规则判断
                      if (res.code === 0) {
                        this.$api
                          .get(this, "user/u/distribution/list", {
                            useRange: 1,
                          })
                          .then((res) => {
                            // 判断是否有配送方式支持商品下单
                            if (res.code === 0) {
                              var songGoodsShop = [];
                              this.distributeList = res.distributions;
                              if (this.distributeList.length === 0) {
                                MessageBox.confirm(
                                  "普通商品订单当前未配置配送方式，无法下单，请联系管理员",
                                  {
                                    confirmButtonText: "确定",
                                    cancelButtonText: "取消",
                                    type: "warning",
                                  }
                                );
                                return false;
                              }

                              // 订单结算提示
                              orderHint().then((res) => {
                                if (res) {
                                  window.localStorage.removeItem("shopCatShop");
                                  window.localStorage.removeItem(
                                    "spesShopCatShop"
                                  );
                                  window.localStorage.setItem(
                                    "songGoodsShop",
                                    JSON.stringify(songGoodsShop)
                                  ); // 赠品空数组
                                  window.localStorage.setItem(
                                    "goodsItems",
                                    JSON.stringify(tempArrayOne)
                                  );
                                  if (this.hasActivity === true) {
                                    // 商品参与活动
                                    this.lookSongGoods(
                                      generalList,
                                      diyList,
                                      groupList,
                                      curGoodsType,
                                      tempArrayOne
                                    );
                                    // 临时存储检验库存数据
                                    this.tempArrayStockData = {
                                      generalList: generalList,
                                      diyList: diyList,
                                      groupList: groupList,
                                      curGoodsType: curGoodsType,
                                      tempArrayOne: tempArrayOne,
                                    };
                                  } else {
                                    // 商品未参与活动
                                    if (this.intervalid !== undefined) {
                                      clearInterval(this.intervalid);
                                    }
                                    this.handleChooseDelivery(
                                      generalList,
                                      diyList,
                                      groupList,
                                      curGoodsType,
                                      tempArrayOne
                                    );
                                  }
                                }
                              });
                            }
                          });
                      } else if (res.code === 3) {
                      } else {
                        this.limitPurchase = [];
                      }
                    });
                } else if (Number(this.goodsType) === 2) {
                  // 定制商品
                  this.$api
                    .post(this, "user/u/order/restriction", {
                      goods: this.limitPurchase,
                    })
                    .then((res) => {
                      // 限购规则判断
                      if (res.code === 0) {
                        this.$api
                          .get(this, "user/u/distribution/list", {
                            useRange: 2,
                          })
                          .then((res) => {
                            // 判断是否有配送方式支持商品下单
                            if (res.code === 0) {
                              var songGoodsShop = [];
                              this.distributeList = res.distributions;
                              if (this.distributeList.length === 0) {
                                MessageBox.confirm(
                                  "定制商品订单当前未配置配送方式，无法下单，请联系管理员",
                                  {
                                    confirmButtonText: "确定",
                                    cancelButtonText: "取消",
                                    type: "warning",
                                  }
                                );
                                return false;
                              }
                              window.localStorage.removeItem("shopCatShop");
                              window.localStorage.removeItem("spesShopCatShop");
                              window.localStorage.setItem(
                                "songGoodsShop",
                                JSON.stringify(songGoodsShop)
                              ); // 赠品空数组
                              window.localStorage.setItem(
                                "goodsItems",
                                JSON.stringify(tempArrayOne)
                              );
                              if (this.intervalid !== undefined) {
                                clearInterval(this.intervalid);
                              }

                              // 订单结算提示
                              orderHint().then((res) => {
                                if (res.code === 0) {
                                  this.$router.push({
                                    name: "ConsigneeInfor",
                                    query: {
                                      good_id: this.id,
                                      goodsType: this.goodsType,
                                      values: this.values,
                                    },
                                  });
                                }
                              });
                            }
                          });
                      } else if (res.code === 3) {
                      } else {
                        this.limitPurchase = [];
                      }
                    });
                }
              }
            } else {
              if (this.$i18n.locale === "zh") {
                this.$message.warning("货品订购数量不能大于库存");
              } else {
                this.$message.warning(
                  "The order quantity of products shall not be greater than inventory."
                );
              }
              return false;
            }
          }
        }, 500);
      } else if (Number(this.freezeStatus) === 2) {
        this.clickAgain = true;
        if (this.$i18n.locale === "zh") {
          this.$message.warning("账号已冻结，无法进行此操作，请联系商务。");
        } else {
          this.$message.warning(
            "The account has been frozen and cannot perform this operation. Please contact business personnel."
          );
        }
      } else if (Number(this.capitalStatus) === 1) {
        this.clickAgain = true;
        if (this.$i18n.locale === "zh") {
          this.$message.warning(
            "账号仍在审核中，无法进行此操作，请等待审核通过。"
          );
        } else {
          this.$message.warning(
            "The account fails to be reviewed and cannot perform this operation. Please launch application again"
          );
        }
      } else if (Number(this.capitalStatus) === 3) {
        this.clickAgain = true;
        if (this.$i18n.locale === "zh") {
          this.$message.warning(
            "账号审核未通过，无法进行此操作。请重新发起申请。"
          );
        } else {
          this.$message.warning(
            "The account is still being approved and cannot perform this operation. Please launch application again"
          );
        }
      } else if (Number(this.capitalStatus) === 4) {
        this.clickAgain = true;
        if (this.$i18n.locale === "zh") {
          this.$message.warning(
            "账号仍在审批中，无法进行此操作。请重新发起申请。"
          );
        } else {
          this.$message.warning(
            "The account is still being reviewed and cannot perform this operation. Please wait for approval."
          );
        }
      }
    },
    // 显示赠品
    lookSongGoods(generalList, diyList, groupList, type, tempArray) {
      var myDate = Math.random();
      var tempSongArray = [];
      let product = {};
      this.goodsItems.forEach((item) => {
        if (item.count && item.count > 0) {
          if (item.ruleId) {
            if (item.ruleId instanceof Array === true) {
              product = {
                ruleId: item.ruleId[0],
                itemCount: item.choosedBoxNum
                  ? item.count * item.choosedBoxNum
                  : item.count,
                goodsId: item.goodsId,
                itemId: item.id,
              };
            } else {
              product = {
                ruleId: item.ruleId,
                itemCount: item.choosedBoxNum
                  ? item.count * item.choosedBoxNum
                  : item.count,
                goodsId: item.goodsId,
                itemId: item.id,
              };
            }
          } else {
            product = {
              ruleId: "",
              itemCount: item.choosedBoxNum
                ? item.count * item.choosedBoxNum
                : item.count,
              goodsId: item.goodsId,
              itemId: item.id,
            };
          }
          tempSongArray.push(product);
        }
      });
      this.$api
        .post(this, "user/u/marketing/promotion/calculatePresent?" + myDate, {
          goodss: tempSongArray,
        })
        .then((res) => {
          if (res.code === 0) {
            if (res.rules.length > 0) {
              this.dialogVisible = true;
              this.showSongGoodsList = [];
              this.presentCount = 0;
              res.rules.forEach((item) => {
                let ablePresentCount = 0;
                item.condition.presents.forEach((val) => {
                  Vue.set(val, "num", 0);
                  Vue.set(val, "ruleId", item.id);
                  this.showSongGoodsList.push(val);
                  ablePresentCount = ablePresentCount + val.presentCount;
                });
                if (item.condition.presentCountTotal > ablePresentCount) {
                  this.presentCount += ablePresentCount;
                } else {
                  this.presentCount += item.condition.presentCountTotal;
                }
              });
            } else {
              if (this.intervalid !== undefined) {
                clearInterval(this.intervalid);
              }
              this.handleChooseDelivery(
                generalList,
                diyList,
                groupList,
                type,
                tempArray
              );
            }
          }
        });
    },
    // 确认赠品
    confirmSongShop() {
      // 获取临时存储检验库存数据
      let generalList = this.tempArrayStockData.generalList;
      let diyList = this.tempArrayStockData.diyList;
      let groupList = this.tempArrayStockData.groupList;
      let type = this.tempArrayStockData.curGoodsType;
      let tempArray = this.tempArrayStockData.tempArrayOne;
      var tempSongArray = [];
      if (this.presentCount < this.hasChoose) {
        if (this.$i18n.locale === "zh") {
          this.$message.warning("赠品数量已达上限!");
        } else {
          this.$message.warning(
            "The quantity of gifts has reached upper limit!"
          );
        }
        return false;
      } else {
        if (this.hasChoose === 0) {
          MessageBox.confirm(this.$t("Message.One"), this.$t("Message.Two"), {
            confirmButtonText: this.$t("Message.Confirm"),
            cancelButtonText: this.$t("Message.Cancel"),
            type: "warning",
          })
            .then(() => {
              this.dialogVisible = false;
              if (this.intervalid !== undefined) {
                clearInterval(this.intervalid);
              }
              this.handleChooseDelivery(
                generalList,
                diyList,
                groupList,
                type,
                tempArray
              );
            })
            .catch({});
        } else if (this.hasChoose > 0) {
          this.dialogVisible = false;
          this.songGoods = 1;
          let songList = [];
          this.showSongGoodsList.forEach((item) => {
            if (item.num > 0) {
              this.$set(item, "id", item.itemId);
              this.$set(item, "count", item.num);
              tempSongArray.push(item);
              var option = {
                itemId: item.itemId,
                count: item.num,
              };
              songList.push(option);
            }
          });
          window.localStorage.setItem(
            "songGoodsShop",
            JSON.stringify(tempSongArray)
          ); // 赠品
          if (this.intervalid !== undefined) {
            clearInterval(this.intervalid);
          }
          let curGeneralList = generalList.concat(songList);
          let curTempArray = tempArray.concat(tempSongArray);
          this.handleChooseDelivery(
            curGeneralList,
            diyList,
            groupList,
            type,
            curTempArray
          );
        }
      }
    },
    // 配送方式选择弹窗显示
    handleChooseDelivery(generalList, diyList, groupList, type, tempArray) {
      // 检验库存
      this.$api
        .post(this, "user/u/checkSku", {
          generalList: generalList,
          diyList: diyList,
          groupList: groupList,
        })
        .then((res) => {
          if (res.code === 0) {
            if (res.data.length === 0) {
              this.$router.push({
                name: "ConsigneeInfor",
                query: {
                  good_id: this.id,
                  goodsType: this.goodsType,
                  values: this.values,
                  splitFlag: 0,
                  isInventoryEnough: true,
                  isOnwayFlag: "notOnwayAll",
                },
              });
            } else {
              // 拼团商品 - 不拆单
              if (groupList.length > 0) {
                let len = 0;
                groupList.forEach((item1) => {
                  res.data.forEach((item2) => {
                    if (item2.itemId === item1.itemId) {
                      if (Number(item2.inStockCount) === 0) {
                        len++;
                      }
                    }
                  });
                });
                this.$router.push({
                  name: "ConsigneeInfor",
                  query: {
                    good_id: this.id,
                    goodsType: this.goodsType,
                    values: this.values,
                    splitFlag: 0,
                    isInventoryEnough: false,
                    isOnwayFlag:
                      len > 0 && groupList.length != len ? "notOnwayAll" : "",
                  },
                });
              } else {
                // 其他商品
                let curTemp = [];
                let len = 0;
                tempArray.forEach((item1) => {
                  this.$set(item1, "inStockCount", item1.count);
                  this.$set(item1, "inWayCount", 0);
                  this.$set(item1, "orderCount", item1.count);
                  res.data.forEach((item2) => {
                    if (item2.itemId === item1.id) {
                      if (item2.inStockCount === 0) {
                        len++;
                      }
                      let inStockCount =
                        item2.inStockCount > 0 ? item2.inStockCount : 0;
                      let inWayCount = item2.orderCount - inStockCount;
                      this.$set(item1, "inStockCount", inStockCount);
                      this.$set(item1, "inWayCount", inWayCount);
                      this.$set(item1, "orderCount", item2.orderCount);
                    }
                  });
                  curTemp.push(item1);
                });
                // 都是在途商品 - 不拆单
                if (tempArray.length > len) {
                  this.deliveryData = curTemp;
                  this.showChooseDelivery = true;
                } else {
                  this.$router.push({
                    name: "ConsigneeInfor",
                    query: {
                      good_id: this.id,
                      goodsType: this.goodsType,
                      values: this.values,
                      splitFlag: 0,
                      isInventoryEnough: false,
                    },
                  });
                }
              }
            }
          } else {
            this.showLoading = false;
          }
        });
    },
  },
  created() {
    let states = window.localStorage.getItem("accessTypeState"); // 0后台访问状态 1前台访问状态
    if (Number(this.accessType) === 1 && Number(states) !== 1) {
      // 后台访问
      window.localStorage.removeItem("name");
      window.localStorage.removeItem("userId");
      window.localStorage.removeItem("gradeId");
      window.localStorage.removeItem("capitalStatus");
      window.localStorage.removeItem("freezeStatus");
      window.localStorage.setItem("accessType", this.accessType); // 给登录页判断
      this.$store.dispatch("FedLogOut").then(() => {});
    } else {
      // 前台访问
      window.localStorage.removeItem("accessType");
      window.localStorage.removeItem("accessTypeState");
    }
  },
  mounted() {
    this.exchange = Number(localStorage.getItem("exchange"));
    this.userId = window.localStorage.getItem("userId"); //获取分销商id
    this.capitalStatus = window.localStorage.getItem("capitalStatus");
    this.freezeStatus = window.localStorage.getItem("freezeStatus");
    this.getGoodsDetail(); //获取商品详情

    // 监听滚动 固定详情tab
    window.addEventListener("scroll", this.handleTabFix, true);
  },
  beforeRouteLeave(to, from, next) {
    window.removeEventListener("scroll", this.handleTabFix, true);
    next();
  },
};
</script>
<style lang="less">
@import url("../assets/less/variable.less");
.img-box-p {
  img {
    max-width: @center;
    display: block;
    margin: 0 auto;
  }
}
.detail-qiehuan-list {
  .el-icon-Nocheck {
    width: 16px;
    height: 16px;
    border: 1px solid #f1f2f9;
    border-radius: 100%;
  }
  .detail-qiehuan-box {
    padding: 10px 15px;
    border-bottom: 1px solid #f1f2f9;
    display: flex;
    align-items: center;
    i {
      color: #f19339;
      margin-right: 15px;
      font-size: 21px;
    }
    span {
      color: #9b9b9b;
      display: block;
    }
    .detail-qiehuan-ruleName {
      color: #f19339;
      font-size: 15px;
    }
    .detail-qiehuan-conditions {
      span {
        padding: 5px 0 0 10px;
        font-size: 12px;
      }
    }
  }
}
.detail-qiehuan {
  background: #f19339;
  font-size: 12px;
  color: #fff;
  border-radius: 6px;
  padding: 3px 5px;
  margin-left: 10px;
}
.alls2 {
  .el-input-number {
    width: 120px !important;
    position: relative !important;
  }
  .el-input,
  .el-input__inner {
    width: 120px !important;
    left: 0px;
  }

  .present-top-div {
    display: flex;
    align-items: center;
    background: #e5f9fb;
    padding: 8px 20px;
    span {
      font-size: 12px;
    }
    span:nth-child(1) {
      background: #ff7900;
      color: #fff;
      padding: 5px 8px;
      border-radius: 5px;
    }
    span:nth-child(2) {
      margin-left: 20px;
      color: #00c9dc;
    }
    div {
      margin-left: 20px;
      i {
        margin: 0 10px;
        color: red;
      }
    }
  }
}
.el-dialog {
  width: 1000px;
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
.package-table {
  .el-table {
    margin-top: 30px;
    &::before {
      height: 0;
    }
    th,
    td {
      border: 0 !important;
      padding: 0;
      height: 50px;
      line-height: 50px;
      color: @lighterBlack;
      .cell {
        font-size: 16px;
        text-align: left;
      }
    }
    th {
      color: @gray;
    }
  }
}
</style>
    <style scoped="scoped" lang="less">
@import url("../assets/less/variable.less");
div,
ol,
dl,
dt,
dd,
h1,
h2,
h3,
h4,
h5,
h6,
form,
input,
p,
body {
  position: initial;
}
.user {
  width: 100%;
}
.img-margin-right-xxxs {
  margin-right: 8px;
}
/*活动*/
.shopcar-activity {
  width: 1110px;
  padding-top: 5px;
  padding-left: 20px;
  padding-bottom: 5px;
  background-color: #e5f9fb;
  .shopcar-activity-img {
    margin-right: 15px;
    width: 20px;
  }
  .shopCart-activity-state {
    font-size: 13px;
    line-height: 20px;
  }
}
.app-cons-content {
  padding-top: 20px;
  .item {
    margin-bottom: -1px;
    border: 1px solid #e0e0e0;
    height: 38px;
    line-height: 38px;
    .item-left {
      width: 40%;
      border-right: 1px solid #e0e0e0;
    }
    .item-right {
      width: 60%;
    }
  }
}
.dingzhi-btn {
  width: 60px;
  height: 30px;
  line-height: 30px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 12px;
  margin: 0 auto;
  cursor: pointer;
}
.diy-css {
  .left-img {
    width: 100px;
  }
}
.overflow-y {
  overflow-y: scroll;
}
.nav-bottom {
  width: @center;
  .bleed {
    padding-top: 29px;
    padding-bottom: 14px;
    .el-breadcrumb {
      font-size: 12px;
    }
  }
}
.mainOne {
  width: @center;
  .product-all {
    width: 100%;
    .pic-box {
      margin-right: 25px;
      width: 450px;
      .pic-move {
        width: 450px;
        height: 450px;
        border-radius: 8px;
        background-color: @proBg;
        overflow: hidden;
      }
      .pic-silde {
        width: 450px;
        margin-top: 20px;
        .silde-img {
          .item {
            width: 68px;
            cursor: pointer;
            &:last-child {
              margin-right: 0;
            }
            img {
              height: 68px;
            }
          }
        }
      }
    }
    .text-box {
      width: calc(100% - 508px);
      box-sizing: border-box;
      .head {
        position: relative;
        .ding-zhi {
          position: relative;
          padding-right: 8px;
          padding-left: 8px;
          margin-right: 10px;
          min-width: 60px;
          height: 28px;
          background: rgba(0, 201, 220, 1);
          border-radius: 5px;
          .img {
            margin-top: 8px;
            margin-right: 8px;
            float: left;
            width: 16px;
            height: 16px;
            background: url("../../src/assets/images/ding-zhi.png") no-repeat
              center center;
            background-size: 100%;
          }
          .spc {
            float: left;
            height: 28px;
            line-height: 28px;
            font-size: 15px;
            font-family: FZLTHJW--GB1-0;
            font-weight: 400;
            color: rgba(255, 255, 255, 1);
          }
        }
        .goodsName {
          padding-left: 8px;
          padding-right: 110px;
          margin-bottom: 5px;
          font-weight: 500;
          font-size: 24px;
          font-family: FZLTHJW--GB1-0;
          color: @lighterBlack;
        }
        .collection {
          position: absolute;
          top: 9px;
          right: 10px;
          align-items: center;
          flex: 1;
          display: flex;
          font-size: 12px;
          font-family: FZHei-B01;
          font-weight: 400;
          color: @lightBlack;
          cursor: pointer;
          i {
            margin-right: 12px;
            font-size: 17px;
            &.el-icon-star-on {
              font-size: 18px;
              color: @blue;
            }
          }
        }
      }
      .introduce {
        padding-left: 8px;
        font-size: 14px;
        font-family: FZHei-B01;
        font-weight: 400;
        color: @red;
      }
      .content {
        .group-detail {
          margin-top: 20px;
          background-color: @pinkBg;
          &.green {
            background-color: @greenLightBg;
            .group-title {
              background-color: @greenBg;
              .name {
                background: linear-gradient(90deg, @lightGreen 1%, @green 100%);
                box-shadow: 0px 2px 10px 0px @greenShadow;
                &::after {
                  background-color: @greenBg;
                }
              }
            }
          }
          .group-title {
            padding-right: 5px;
            height: 50px;
            line-height: 50px;
            background-color: @groupTitle;
            overflow: hidden;
            .name {
              position: relative;
              padding-left: 15px;
              padding-right: 40px;
              margin-right: 35px;
              font-size: 20px;
              color: @white;
              background: linear-gradient(90deg, @groupBtnColor 0%, @red 100%);
              box-shadow: 0px 2px 10px 0px @groupBtnShadow;
              &::after {
                content: "";
                position: absolute;
                right: -20px;
                bottom: -20px;
                width: 30px;
                height: 100px;
                background-color: @groupTitle;
                -webkit-transform: rotate(35deg);
                transform: rotate(30deg);
              }
            }
            .count-down {
              position: relative;
              padding-left: 25px;
              margin-right: 15px;
              font-size: 14px;
              color: @lightBlack;
              .el-icon-time {
                position: absolute;
                top: 16px;
                left: 0;
                font-size: 17px;
              }
              .red {
                color: @red;
              }
              span.red {
                font-size: 14px;
              }
            }
            .surplus {
              span {
                padding: 0 12px;
                height: 20px;
                line-height: 20px;
                font-size: 12px;
                background-color: @yellow;
                border-radius: 20px;
              }
            }
            .more {
              font-size: 12px;
              color: @lightBlack;
              cursor: pointer;
              &:hover {
                color: @blue;
              }
            }
          }
          .group-con {
            padding: 20px 15px;
          }
        }
        .group-list {
          font-size: 12px;
          .title {
            min-width: 60px;
            width: auto;
            display: inline-block;
          }
          .current {
            color: @red;
            i {
              font-size: 18px;
            }
          }
          .origin {
            margin-left: 10px;
            text-decoration: line-through;
          }
          & + .group-list {
            margin-top: 20px;
          }
          &.rules {
            position: relative;
            padding-left: 63px;
            .title {
              position: absolute;
              top: 0;
              left: 0;
              line-height: 17px;
            }
            p {
              font-size: 12px;
              line-height: 17px;
            }
          }
        }
        .title {
          width: 85px;
          font-size: 12px;
          font-family: FZHei-B01;
          font-weight: 400;
          color: @lighterBlack;
        }
        .titleContent {
          font-size: 12px;
          font-family: FZHei-B01;
          font-weight: 400;
          color: @lightBlack;
        }
        .noActivity {
          font-size: 12px;
          font-family: FZHei-B01;
          font-weight: 400;
          color: @lightBlack;
        }
        .money {
          font-size: 24px;
          font-family: Arial;
          font-weight: 400;
          color: @lightRed;
        }
        .currency {
          font-size: 18px;
          font-family: Arial;
          font-weight: 400;
          color: @lightRed;
        }
        .count {
          font-size: 12px;
          font-family: FZHei-B01;
          font-weight: 400;
          color: @lightBlack;
        }
        .bold {
          font-weight: 600;
        }
        .preferential {
          margin-top: 25px;
          padding-left: 8px;
          line-height: 54px;
          background: @pinkBg;
        }
        .CollectBills {
          font-size: 12px;
          margin-left: 5px;
          font-family: FZHei-B01;
          font-weight: 400;
          color: @red;
          cursor: pointer;
        }
        .activityTime1 {
          float: right;
          font-size: 12px;
          font-family: FZHei-B01;
          font-weight: 400;
          color: @darkGray;
        }
        .activityTime5 {
          float: right;
          font-size: 12px;
          font-family: FZHei-B01;
          font-weight: 400;
          color: @darkGray;
        }
        .activityName6 {
          margin-left: 8px;
          max-width: 247px;
          font-size: 12px;
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
          font-family: FZHei-B01;
          font-weight: 400;
          color: @darkGray;
          text-align: center;
          cursor: pointer;
        }
        .activityName6:hover {
          color: @darkGray;
          opacity: 0.6;
        }
        .activityTime2 {
          float: right;
          font-size: 12px;
          font-family: FZHei-B01;
          font-weight: 400;
          color: @red;
        }
        .activityTime3 {
          float: right;
          font-size: 12px;
          font-family: FZHei-B01;
          font-weight: 400;
          color: @darkGray;
        }
        .activityTime4 {
          float: right;
          margin-right: 30px;
          font-size: 12px;
          margin-left: 16px;
          font-family: FZHei-B01;
          font-weight: 400;
          color: @red;
        }
        .activity-box {
          // border: 1px solid #ccc;
          // padding: 10px;
          width: 505px;
          overflow: auto;
          background-color: #fff8f2;
          position: absolute;
          margin-left: 180px;
          height: 116px;
          left: 0;
          top: 45px;
          z-index: 99;
          border-radius: 4px;
          box-shadow: 0px 3px 5px 0px rgba(135, 138, 138, 0.47);
        }
        .activityName {
          margin-left: 8px;
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
          max-width: 190px;
          font-size: 12px;
          font-family: FZHei-B01;
          font-weight: 400;
          color: @lightBlack;
        }
        .goActivity {
          font-size: 12px;
          font-family: FZHei-B01;
          font-weight: 400;
          color: @blue;
          cursor: pointer;
          margin-left: 30px;
        }
        .goActivity:hover {
          color: @blue !important;
          opacity: 0.6;
          color: @white;
        }
        .box {
          padding-left: 18px;
          line-height: 20px;
        }
        .brand {
          margin-top: 20px;
          padding-left: 8px;
          line-height: 28px;
          position: relative;
          .salse-count {
            position: absolute;
            right: 0;
            p {
              font-size: 12px;
              color: @lighterBlack;
              span {
                margin-left: 10px;
                font-size: 14px;
              }
            }
            .line {
              margin: 0 25px;
              font-size: 14px;
              color: @gray;
            }
          }
        }
        .model-wrap {
          position: relative;
          padding-left: 8px;
          margin-top: 6px;
        }
        .btn-normal {
          display: inline-block;
          margin-top: 20px;
          margin-left: 8px;
          padding: 0 40px;
          height: 35px;
          line-height: 35px;
          text-align: center;
          color: @white;
          background: @blue;
          cursor: pointer;
          border-radius: 6px;
        }
        .item {
          margin-top: 32px;
          padding-left: 8px;
          line-height: 25px;
          .activity {
            width: 530px;
            div {
              width: 100%;
              .label {
                padding-left: 5px;
                padding-right: 5px;
                display: inline-block;
                min-width: 50px;
                height: 20px;
                line-height: 20px;
                border-radius: 5px;
                background-color: @red;
                font-size: 12px;
                text-align: center;
                color: @white;
              }
            }
            .more-label {
              .icon {
                margin-right: 5px;
                display: inline-block;
                width: 12px;
                height: 10px;
                background: url("../../src/assets/images/turn-downs.png")
                  no-repeat center center;
                background-size: 12px;
              }
              .rotate {
                transform: rotate(180deg);
              }
            }
          }
        }
        .spe {
          padding-top: 10px;
          padding-bottom: 10px;
          background-color: #caf2f6;
        }
        .operate {
          position: absolute;
          left: 0;
          bottom: 10px;
          padding-left: 18px;
          div {
            height: 35px;
            line-height: 35px;
          }
          .collect1 {
            padding-left: 62px;
            width: 74px;
            .img1 {
              left: 35px;
            }
          }
          .collect2 {
            padding-left: 52px;
            width: 84px;
            .img2 {
              left: 25px;
            }
          }
          .collect {
            position: relative;
            .img {
              position: absolute;
              top: 8px;
              display: inline-block;
              width: 18px;
              height: 17px;
              background: url("../../src/assets/images/star.png") no-repeat
                center center;
            }
            .word {
              float: left;
              height: 35px;
              line-height: 35px;
            }
          }
          .share {
            span {
              display: inline-block;
              width: 18px;
              height: 17px;
              background: url("../../src/assets/images/share.png") no-repeat
                center center;
            }
          }
        }
      }
    }
  }
  .toggle-more {
    position: absolute;
    bottom: -24px;
    right: 36px;
    cursor: pointer;
    i {
      padding: 1px 10px;
      color: #fff;
      border: 1px solid #0db8cf;
      border-top: 0;
      background-color: #0db8cf;
      font-size: 22px;
      border-radius: 0 0 4px 4px;
    }
  }
  .shop-table {
    width: 100%;
    border-bottom: 1px solid @bdColor;
    &.hidden {
      max-height: 352px;
      overflow: hidden;
    }
    .code {
      line-height: 20px;
    }
    table {
      word-wrap: break-word;
      word-break: break-all;
      border-collapse: collapse;
      width: 100%;
      tr {
        &.goods-item:hover,
        &.goods-item:active {
          background-color: @grayBg;
        }
        th,
        td.spe {
          word-wrap: break-word;
          word-break: break-word;
        }
        th {
          height: 35px;
          text-align: center;
          background-color: @white;
          font-size: 13px;
          font-weight: 400;
          word-wrap: break-word;
          word-break: break-word;
        }
        td.td-h1 {
          white-space: normal;
          border-left: 0;
          .activity-label {
            width: 22px;
            height: 50px;
            line-height: 25px;
            border-radius: 5px;
            word-break: break-word;
          }
        }
        td {
          height: 60px;
          max-height: 60px;
          text-align: center;
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
          color: @lightBlack;
          font-size: 12px;
          border-top: 1px dashed @bdColor;
          button {
            padding: 0;
            border: 0;
            font-size: 12px;
            color: @lightBlack;
            background-color: transparent;
          }
          .register {
            width: 55px;
            color: #ff7900;
            border-bottom: 1px solid #ff7900;
          }
        }
      }
      .rl-single-limit {
        width: 80px;
        & + .rl-single-limit {
          margin-left: 0;
        }
      }
    }
    .el-input-number {
      position: relative;
      width: 90px;
    }
  }
}
.buy-list {
  position: relative;
  top: -1px;
  left: 0;
  margin-left: 8px;
  padding: 0 15px;
  height: 40px;
  line-height: 40px;
  font-size: 12px;
  color: @lightBlack;
  background-color: @grayBg;
  &.rl-margin-top-default {
    margin-top: 20px;
  }
  .rl-text-red {
    color: #f16e7b;
  }
  .line {
    margin: 0 15px;
    color: @bdColor;
  }
}
.buy-now {
  padding: 40px 0 20px;
  .buy {
    width: 290px;
    div {
      width: 130px;
      height: 40px;
      line-height: 40px;
      font-size: 16px;
      border-radius: 8px;
    }
    .buy-btn {
      text-align: center;
      background-color: @redLabel;
    }
    .join-btn {
      text-align: center;
      background-color: @blueLabel;
    }
  }
}
.mainTwo {
  width: @center;
  margin-top: 90px;
  padding-bottom: 40px;
  .details-list {
    position: relative;
    width: 100%;
    padding-top: 30px;
    padding-bottom: 70px;
    border-top: 2px solid @grayBg;
    text-align: center;
    z-index: 1;
    &.fixed {
      position: fixed;
      top: 0;
      right: 0;
      left: 0;
      width: 100%;
      height: 40px;
      padding: 0 0 20px;
      background: #fff;
      border-bottom: 1px solid #ccc;
      z-index: 100;
      & + .goods-detail {
        padding-top: 63px;
      }
    }
    li {
      display: inline-block;
      height: 50px;
      line-height: 50px;
      padding: 0 13px;
      font-size: 18px;
      color: @lighterBlack;
      border-bottom: 2px solid @white;
      & + li {
        margin-left: 100px;
      }
      &.cur {
        color: @blue;
        border-bottom: 2px solid @blue;
      }
    }
    .join-btn {
      position: absolute;
      top: 10px;
      left: 50%;
      margin-left: 445px;
      width: 120px;
      height: 35px;
      line-height: 35px;
      background-color: #1bb8ce;
      border-radius: 5px;
    }
  }

  .goods-attribute {
    margin-top: 30px;
    background-color: white;
    .descrip-item {
      font-size: 16px;
      color: @lighterBlack;
      height: 50px;
      line-height: 50px;
      &.descrip-title {
        width: 150px;
        color: @gray;
      }
    }
  }
}
.descriptions {
  padding: 60px 0;
  .title {
    padding-bottom: 30px;
    font-size: 18px;
    line-height: 25px;
    color: @lighterBlack;
    border-bottom: 2px solid #d8d8d8;
    span {
      padding-left: 11px;
      width: 6px;
      height: 25px;
      border-left: 6px solid @blue;
    }
  }
  .service-phone {
    margin-top: 30px;
    font-size: 16px;
    color: @lighterBlack;
  }
}
.withoutGoods {
  width: @center;
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
  opacity: 0.6;
}
.pro-cover {
  width: 560px;
  height: 450px;
  border: 1px solid #ccc;
  border-radius: 5px;
  z-index: 99;
  background: #fefefe;
  .item {
    padding-left: 50px !important;
    margin-top: 0 !important;
    color: @lightBlack;
    .item-label {
      width: 100px;
      color: @lighterBlack;
    }
    .common-text {
      width: 350px;
      height: 80px;
      padding-left: 10px;
    }
    .goodsName {
      max-width: 400px;
    }
  }
  .button {
    margin: 0 auto;
    width: 100px;
    & + .button {
      margin-left: 20px;
    }
    span {
      display: block;
      width: 100px;
      height: 35px;
      line-height: 35px;
      text-align: center;
      background-color: #00c9dc;
      color: #fff;
      border-radius: 5px;
    }
  }
  .button-presell {
    margin: 0 auto;
    width: 220px;
  }
}
.cover-box {
  box-sizing: border-box;
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
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
}
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
/*分组*/
.shop-table table tr td.fen-group {
  height: 50px;
  background-color: #e7e7e7;
  .btn-prev {
    width: 20px;
    height: 50px;
    float: left;
    cursor: pointer;
    background: url("../../src/assets/images/btn-prev.jpg") center center
      no-repeat;
  }
  .btn-next {
    width: 20px;
    height: 50px;
    float: left;
    cursor: pointer;
    background: url("../../src/assets/images/btn-next.jpg") center center
      no-repeat;
  }
  .fen-group-ul {
    position: relative;
    width: calc(100% - 40px);
    overflow: hidden;
    height: 50px;
    ul {
      width: 10000px;
      position: absolute;
      top: 0;
      left: 0;
      height: 50px;
      overflow: hidden;
      li {
        width: 155px;
        height: 50px;
        line-height: 50px;
        float: left;
        cursor: pointer;
        overflow: hidden;
        button {
          padding: 0;
          border: 0;
          background-color: #e7e7e7;
          font-size: 15px;
          color: #000;
        }
      }
      li.current {
        background-color: #ccf4f8;
        button {
          padding: 0;
          border: 0;
          background-color: #ccf4f8;
          font-size: 15px;
          color: #000;
        }
      }
    }
  }
}
.shop-img {
  position: relative;
  height: 100%;
  img {
    margin-top: 4px;
    width: 50px;
    object-fit: cover;
    height: 50px;
  }
  .activity {
    padding: 0 16px;
    position: absolute;
    top: 1px;
    left: -16px;
    font-size: 12px;
    height: 20px;
    line-height: 20px;
    background: @actLabel;
    font-family: FZHei-B01;
    font-weight: 400;
    color: @white;
    transform: rotate(-45deg);
    // 拼团
    &.activity1 {
      background: @red;
      &.green {
        background: @green;
      }
    }
    &.icon-special {
      top: 5px;
      left: -22px;
      font-size: 13px;
      height: 18px;
      color: @white;
      transform: rotate(-45deg);
    }
  }
  .activity2 {
    padding: 0 16px;
    position: absolute;
    top: 1px;
    left: -16px;
    font-size: 12px;
    height: 16px;
    line-height: 16px;
    background: @yellow;
    font-family: FZHei-B01;
    font-weight: 400;
    color: @white;
    transform: rotate(-45deg);
    &.en {
      top: -2px;
    }
  }
}
/*弹框图片放大*/
.pro-cover-img {
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  margin: auto;
  width: 500px;
  height: 500px;
  z-index: 99;
  display: flex;
  background-color: @white;
  justify-content: center;
  img {
    height: 500px;
  }
  .shut {
    position: absolute;
    top: -8px;
    right: -8px;
    display: block;
    width: 18px;
    height: 18px;
    background: url("../../src/assets/images/shut.png") no-repeat center center;
  }
}
/*选择赠品*/
.max-height300 {
  max-height: 300px;
}
.shop-table {
  table {
    tr {
      td {
        position: relative;
        height: 45px;
        text-align: center;
        .songImg {
          width: 50px;
          img {
            margin-top: 5px;
            height: 50px;
          }
        }
      }
    }
  }
}
.packing-unit {
  display: inline-block;
  min-width: 18px;
  min-height: 18px;
  font-size: 12px;
  color: @blue;
  text-align: center;
  line-height: 18px;
  border: 1px solid @blue;
  border-radius: 18px;
  &.packing-en {
    padding: 0 5px;
  }
}
/*找不到商品*/
.empty-car {
  padding-bottom: 180px;
  .empty-car-img {
    margin-top: 120px;
    width: 153px;
  }
}
/*预售*/
.blank-span {
  position: absolute;
  display: block;
  width: 100%;
  height: 30px;
  background-color: #fff;
  z-index: 1;
  opacity: 0;
}
.el-row {
  margin-bottom: 20px;
  &:last-child {
    margin-bottom: 0;
  }
}
.el-col {
  border-radius: 4px;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}

// 在途
.zaitu {
  height: 20px;
  span {
    display: block;
    position: relative;
    top: 3px;
    width: 16px;
    height: 20px;
    background: url("../assets/images/choose.png") no-repeat center top;
    &.gou {
      background: url("../assets/images/gou.png") no-repeat center top;
    }
  }
}

// 回到顶部
.back-to-top {
  position: fixed;
  left: 50%;
  margin-left: 700px;
  bottom: 60px;
}
@media screen and (max-width: 1920px) {
  .back-to-top {
    left: 100%;
    margin-left: -60px;
  }
}

.nav-item-en14 {
  font-size: 14px !important;
}
</style>
<style lang="less">
@import url("../assets/less/variable.less");
// pic-zoom
.mouse-cover-canvas {
  top: 169px !important;
  left: 50% !important;
  margin-left: -147px !important;
  background-color: @white;
}
.test {
  th {
    background-color: @blue;
    color: @black;
    font-weight: bold;
  }
}
</style>
<style lang="less">
@import url("../assets/less/variable.less");
.packing-table {
  padding: 0;
  .el-table__body-wrapper {
    padding: 10px 0;
  }
  th {
    font-size: 12px;
    color: @lightBlack;
    height: 39px;
    line-height: 39px;
    background-color: @grayBg;
    border-bottom: none;
  }
  td {
    font-size: 12px;
    color: @lightBlack;
    line-height: 36px;
    border-bottom: none;
  }
}

.borBg {
  background-color: #ecfdff !important;
}

.inner-content {
  display: inline-block;
}
.inner-content2 {
  display: flex;
  align-items: center;
  justify-content: center;
}
// 批量下单 - 包装
.choose-unit {
  .buy-sum {
    float: left;
    margin-right: 10px;
  }
  .box-info {
    float: left;
    position: relative;
    top: 2px;
    padding: 0 2px;
    min-width: 16px;
    min-height: 18px;
    font-size: 12px;
    color: @blue;
    text-align: center;
    line-height: 18px;
    border: 1px solid @blue;
    border-radius: 22px;
    cursor: pointer;
  }
  .red-tips {
    font-size: 12px;
    color: @red;
  }
}
.el-popper {
  padding: 0;
  border: none;
}
.el-popper[x-placement^="bottom"] {
  margin-top: 22px;
}
</style>
<style scoped lang='less'>
@import url("../assets/less/variable.less");

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
.prote-cover {
  width: 50%;
  height: 70%;
  border: 1px solid #ccc;
  border-radius: 5px;
  z-index: 99;
  background: #fefefe;
  box-shadow: 0 1px 10px 5px rgba(0, 0, 0, 0.3);
  padding: 0;
}
.cover-box {
  box-sizing: border-box;
  position: fixed;
  top: 50%;
  left: 50%;
  bottom: 0;
  transform: translate3D(-50%, -50%, 0);
  z-index: 99;
  .address-info {
    width: 250px;
    .all {
      width: 100px;
      height: 30px;
      line-height: 30px;
      border-radius: 5px;
    }
    .right {
      height: 28px;
      line-height: 28px;
      border: 1px solid #ccc;
    }
  }
  .address-infos {
    padding: 0 30px;
    height: calc(100% - 180px);
    overflow: auto;
    -ms-overflow-style: none;
    scrollbar-width: none;
    &::-webkit-scrollbar {
      display: none;
    }
    .pop-box {
      padding-top: 0;
      width: 100%;
      .box {
        width: 100%;
        height: 100%;
        overflow-y: auto;
        scrollbar-width: none;
        -ms-overflow-style: none;
        &::-webkit-scrollbar {
          display: none;
        }
        .box-word {
          h1 {
            margin: 30px 0 10px;
            line-height: 2;
          }
          p {
            margin-bottom: 14px;
            line-height: 1.8;
            text-indent: 28px;
            text-align: justify;
            &.rl-tr {
              text-align: right;
            }
          }
        }
      }
    }
  }
  .box-foot {
    margin: 30px 0 20px;
    span {
      display: inline-block;
      width: 180px;
      height: 40px;
      line-height: 40px;
      border-radius: 4px;
    }
  }
}
.gift-num-wrap {
  padding-bottom: 15px;
  height: 16px;
  line-height: 16px;
  .title {
    height: 100%;
  }
  .gift-num {
    padding-left: 103px;
    font-size: 12px;
  }
  .arrow-wrap {
    margin-left: 8px;
    color: @red;
  }
}
</style>
