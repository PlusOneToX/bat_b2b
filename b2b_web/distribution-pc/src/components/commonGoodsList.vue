<template>
  <div>
    <!--主内容-->
    <div class="main rl-padding-top-default">
      <!-- 面包屑 -->
      <div class="breed rl-text-xxs">
        <el-breadcrumb
          separator-class="el-icon-arrow-right"
          class="selectClassify"
        >
          <el-breadcrumb-item class="allResults" v-if="cateFlag == 'classify'">
            <span @click="getAllResult">{{ $t("P.AllResults") }}</span>
          </el-breadcrumb-item>
          <el-breadcrumb-item
            v-else
            class="allResults"
            :to="{ name: 'Index' }"
            >{{ $t("P.HomePage") }}</el-breadcrumb-item
          >
          <template v-if="cateFlag === 'custom'">
            <el-breadcrumb-item>{{
              getDiyType(Number(diyType))
            }}</el-breadcrumb-item>
          </template>
          <template v-else-if="cateFlag === 'new'">
            <el-breadcrumb-item>{{
              $t("Index.NewProduct")
            }}</el-breadcrumb-item>
          </template>
          <template v-else>
            <el-breadcrumb-item
              v-for="(item, index) in selectClassifys"
              :key="item.id"
            >
              <span
                tabindex="0"
                hidefocus="true"
                @click="breadcrumbClassify(index)"
                class="classify"
              >
                {{
                  $i18n.locale === "zh" || !item.nameEn == true
                    ? item.name
                    : item.nameEn
                }}
              </span>
            </el-breadcrumb-item>
          </template>
        </el-breadcrumb>
        <!-- 视图模式/订货模式 -->
        <div
          class="sort-right"
          v-if="
            userId !== '' &&
            userId !== null &&
            freezeStatus !== undefined &&
            freezeStatus !== null &&
            freezeStatus === '1' &&
            capitalStatus !== undefined &&
            capitalStatus !== null &&
            capitalStatus === '2'
          "
        >
          <!-- 图片展示 -->
          <div
            @click="toShowImg"
            class="rl-fl rl-bg-gray-mm rl-tc rl-text-xxs cursor-pointer"
            :class="{ current: tabs === 'showImg' }"
          >
            {{ $t("P.PictureDisplay") }}
          </div>
          <!-- 快速订货 -->
          <div
            @click="toFastOrder"
            class="rl-fl rl-bg-gray-mm rl-tc rl-text-xxs cursor-pointer"
            :class="{ current: tabs === 'fastOrder' }"
          >
            {{ $t("P.QuickOrder") }}
          </div>
        </div>
      </div>

      <!-- 分类列表 -->
      <div class="sort-wrap">
        <el-form ref="form" label-width="80px" label-position="left">
          <el-form-item
            v-show="classifyList.length > 0"
            class="classifyList"
            :label="$t('P.Classify') + '：'"
          >
            <span
              v-for="(item, index) in classifyList"
              tabindex="0"
              hidefocus="true"
              @click="selectClassify(index)"
              :key="item.id"
              class="classify"
              >{{
                $i18n.locale === "zh" || !item.nameEn == true
                  ? item.name
                  : item.nameEn
              }}</span
            >
          </el-form-item>
          <el-form-item class="classifyList" :label="$t('P.Popular') + '：'">
            <span
              v-for="(item, index) in hotList"
              :class="{ bor: hotClicked == index }"
              @click="selectHot(index)"
              tabindex="0"
              hidefocus="true"
              :key="item.type"
              class="hot"
              >{{
                $i18n.locale === "zh" || !item.typeNameEn == true
                  ? item.typeName
                  : item.typeNameEn
              }}</span
            >
          </el-form-item>
          <el-form-item class="classifyList" :label="$t('P.Sort') + '：'">
            <span
              v-for="(item, index) in sortList"
              :class="{ bor: sortClicked == index }"
              @click="selectSort(index)"
              tabindex="0"
              hidefocus="true"
              :key="item.type"
              class="sort"
            >
              {{
                $i18n.locale === "zh" || !item.typeNameEn == true
                  ? item.typeName
                  : item.typeNameEn
              }}
              <img
                class="img"
                v-if="
                  item.orderType === 'desc' && sortClicked > 0 && item.isShow
                "
                src="../assets/images/dow-pre.png"
                alt
              />
              <img
                class="img"
                v-if="
                  item.orderType === 'asc' && sortClicked > 0 && item.isShow
                "
                src="../assets/images/up-pre.png"
                alt
              />
            </span>
          </el-form-item>
          <el-form-item
            class="classifyList"
            :label="$i18n.locale === 'zh' ? '其他：' : 'Other：'"
          >
            <span
              :class="underStockFlag == 0 ? 'hot bor' : 'hot'"
              @click="underStockFlagFun"
              >{{ $i18n.locale === "zh" ? "只看有货" : "In stock" }}</span
            >
          </el-form-item>
        </el-form>
      </div>

      <!--图片展示-->
      <div v-show="tabs == 'showImg'">
        <div class="classify-list">
          <ul class="rl-clear">
            <li
              v-for="item in classifyGoodsList"
              :key="item.id"
              @click="goDoodsDetail(item.id, item.goodsType)"
            >
              <div class="content cursor-pointer">
                <!-- 图片 -->
                <div class="img">
                  <img
                    :src="
                      ($i18n.locale === 'zh' || !item.imageUrl1en == true
                        ? item.imageUrl1
                        : item.imageUrl1en) +
                      '?x-oss-process=image/resize,h_342,l_342'
                    "
                    alt
                  />
                </div>
                <!-- 活动 -->
                <span
                  class="imgLable hotLable"
                  v-show="
                    item.seckillFlag == 1 ||
                    item.promotionFlag == 1 ||
                    item.groupFlag == 1
                  "
                  >{{ $t("P.SellWell") }}</span
                >
                <!-- 新品 -->
                <span class="imgLable newLable" v-show="item.newFlag == 1">{{
                  $t("P.NewProducts")
                }}</span>

                <!-- 底部商品信息 -->
                <div class="classify-info">
                  <div>
                    <!--商品名称  -->
                    <div
                      v-if="
                        (item.tags === undefined ||
                          item.tags === null ||
                          item.tags.length === 0) &&
                        (item.introduce === undefined ||
                          item.introduce === null ||
                          item.introduce.length === 0)
                      "
                      class="brand rl-margin-top-xxxs"
                    >
                      <el-tooltip
                        class="item"
                        effect="dark"
                        :content="
                          $i18n.locale === 'zh' || !item.goodsName == true
                            ? item.goodsName
                            : item.goodsNameEn
                        "
                        placement="bottom"
                      >
                        <span>{{
                          $i18n.locale === "zh" || !item.goodsName == true
                            ? item.goodsName
                            : item.goodsNameEn
                        }}</span>
                      </el-tooltip>
                    </div>

                    <div v-else class="brand brand1 rl-tc rl-margin-top-xxxs">
                      <el-tooltip
                        class="item"
                        effect="dark"
                        :content="
                          $i18n.locale === 'zh' || !item.goodsName == true
                            ? item.goodsName
                            : item.goodsNameEn
                        "
                        placement="bottom"
                      >
                        <span>{{
                          $i18n.locale === "zh" || !item.goodsName == true
                            ? item.goodsName
                            : item.goodsNameEn
                        }}</span>
                      </el-tooltip>
                    </div>
                    <!-- 标签 -->
                    <div
                      class="word rl-tc"
                      v-show="item.tags !== undefined && item.tags.length > 0"
                    >
                      <span
                        class="label"
                        v-for="(tagItem, index) in item.tags"
                        :key="index"
                      >
                        <template v-if="$i18n.locale == 'zh'">{{
                          tagItem.name
                        }}</template>
                        <template v-if="$i18n.locale == 'en'">{{
                          tagItem.nameEn
                        }}</template>
                      </span>
                    </div>
                    <!-- 商品简介- -->
                    <div
                      class="word rl-tc"
                      v-show="
                        item.introduceEn !== undefined &&
                        item.introduceEn !== null &&
                        item.introduceEn !== ''
                      "
                    >
                      <span v-if="$i18n.locale === 'en'">{{
                        item.introduceEn
                      }}</span>
                      <span v-if="$i18n.locale === 'zh'">{{
                        item.introduce
                      }}</span>
                    </div>
                  </div>

                  <div
                    class="moneyLabel"
                    v-if="
                      (freezeStatus === '1' || freezeStatus === null) &&
                      (capitalStatus === '2' || capitalStatus === null)
                    "
                  >
                    <!-- 价格 v-if="Number(fomatFloat(item.minPrice, 2)) !== 0 ||Number(fomatFloat(item.maxPrice, 2)) !==0 "-->
                    <div
                      style="float: left"
                      v-if="
                        item.minPrice ||
                        item.minPrice == 0 ||
                        item.maxPrice ||
                        item.maxPrice == 0
                      "
                    >
                      <span class="currency">{{
                        $i18n.locale === "en" && $root.currency === "USD"
                          ? "$"
                          : "¥"
                      }}</span
                      ><!-- ￥符号  -->
                      <span class="money">{{
                        $i18n.locale === "en" && $root.currency === "USD"
                          ? fomatFloat(
                              (item.minPrice ? item.minPrice : item.maxPrice) *
                                exchange,
                              2
                            )
                          : item.minPrice
                          ? item.minPrice
                          : item.maxPrice
                      }}</span
                      ><!-- 价格 -->
                    </div>
                    <div style="float: left" v-else>
                      <span class="money1">
                        {{
                          item.minPrice === undefined || item.minPrice === null
                            ? userId != "" &&
                              userId != null &&
                              freezeStatus === "1" &&
                              capitalStatus === "2"
                              ? $t("P.Calculating")
                              : $t("P.ViewAfterLogin")
                            : $t("P.NoPricing")
                        }}
                      </span>
                    </div>
                    <!-- 销量 -->
                    <div
                      v-if="$i18n.locale === 'zh' && !userId == ''"
                      style="float: right"
                    >
                      <span v-if="item.saleNum > 10000" class="saleCount">
                        {{ $t("P.sale") }}
                        {{ fomatFloor(item.saleNum / 10000, 2) }}万+
                      </span>
                      <span v-else class="saleCount"
                        >{{ $t("P.sale") }} {{ item.saleNum }}</span
                      >
                    </div>
                    <div
                      v-if="$i18n.locale === 'en' && !userId == ''"
                      style="float: right"
                    >
                      <span v-if="item.saleNum > 1000" class="saleCount">
                        {{ fomatFloor(item.saleNum / 1000, 1) }}K+{{
                          $t("P.sale")
                        }}
                      </span>
                      <span v-else class="saleCount"
                        >{{ item.saleNum }} {{ $t("P.sale") }}</span
                      >
                    </div>
                  </div>
                </div>
              </div>
            </li>
          </ul>
        </div>
        <!-- 分页 -->
        <div
          class="rl-tr rl-margin-bottom-default"
          v-if="this.classifyGoodsList.length !== 0"
        >
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

      <!--快速订货-->
      <div v-show="tabs == 'fastOrder'">
        <div
          class="quick-wrap rl-bg-white"
          v-for="goods in fastOrderGoodsList"
          :key="goods.id"
        >
          <div class="rl-clear">
            <!-- 商品图片 -->
            <div
              class="fast-img cursor-pointer rl-fl"
              @click="goDoodsDetail(goods.id, goods.goodsType)"
            >
              <img
                :src="
                  $i18n.locale === 'zh' || !goods.imageUrl1en == true
                    ? goods.imageUrl1
                    : goods.imageUrl1en
                "
                alt
              />
            </div>
            <div class="goods-details rl-fl">
              <div class="goodsName rl-clear">
                <!-- 商品名称 -->
                <div
                  class="pro-name rl-fl cursor-pointer"
                  @click="goDoodsDetail(goods.id, goods.goodsType)"
                >
                  <span v-show="$i18n.locale === 'zh'">{{
                    goods.goodsName
                  }}</span>
                  <span v-show="$i18n.locale === 'en'">{{
                    goods.goodsNameEn
                  }}</span>
                </div>
                <!-- 商品标志 -->
                <div class="rl-fl">
                  <!-- 热销-->
                  <!-- <el-tag v-show="goods.promotionStatus === 1" class="promotionStatus" size="small">{{ $t("P.SellWell") }}</el-tag> -->
                  <el-tag
                    v-show="
                      goods.seckillFlag == 1 ||
                      goods.promotionFlag == 1 ||
                      goods.groupFlag == 1
                    "
                    class="promotionStatus"
                    size="small"
                    >{{ $t("P.SellWell") }}</el-tag
                  >
                  <!-- 新品-->
                  <el-tag
                    v-show="goods.newFlag == 1"
                    class="newProductStatus"
                    size="small"
                    >{{ $t("P.NewProducts") }}</el-tag
                  >
                </div>
                <!-- 收藏商品 -->
                <div
                  class="rl-fr collection"
                  @click="collectSetFun('POST', goods.id)"
                  v-if="userId && goods.collectionFlag === 0"
                >
                  <i class="el-icon-star-off"></i>
                  {{ $t("P.Collect") }}
                  <span style="text-align: center"></span>
                </div>
                <div
                  class="rl-fr collection"
                  @click="collectSetFun('DELETE', goods.id)"
                  v-if="userId && goods.collectionFlag === 1"
                >
                  <i class="el-icon-star-on"></i>
                  {{ $t("P.Collect") }}
                  <span style="text-align: center"></span>
                </div>
              </div>
              <!-- 商品介绍 -->
              <div class="introduce">
                <span v-show="$i18n.locale === 'zh'">{{
                  goods.introduce
                }}</span>
                <span v-show="$i18n.locale === 'en'">{{
                  goods.introduceEn
                }}</span>
              </div>
              <!-- 商品编号/上架时间 -->
              <div class="brand rl-clear">
                <div class="title rl-fl">
                  {{ $t("ShopDetail.OnSaleTime") }}：
                  <span class="titleContent">{{
                    goods.saleTime | formatDate
                  }}</span>
                </div>
                <div class="title rl-fl">
                  {{ $t("ShopDetail.CommodityNo") }}：
                  <span class="titleContent">{{ goods.goodsNo }}</span>
                </div>
                <div class="title rl-fl">
                  {{ $t("ShopDetail.Brand") }}:
                  <span class="titleContent">{{ goods.brandName }}</span>
                </div>
              </div>
            </div>
          </div>
          <!-- 在途 -->
          <div
            @click="changeTu(goods, goods.tuType)"
            class="rl-fr rl-text-xxs zaitu rl-clear cursor-pointer"
            v-if="goods.sysOnWayFlag == 1"
          >
            <span class="rl-fl" :class="{ gou: goods.tuType == true }"></span>
            {{ $t("P.ConTran") }}
          </div>
          <!-- 普通商品列表 -->
          <div class="fast-table">
            <table class="table-content">
              <tr>
                <!-- 图片 -->
                <th width="60">{{ $t("ShopCarts.Picture") }}</th>
                <!-- 条形码、编码 -->
                <th width="180">
                  {{ $i18n.locale === "zh" ? "编码/条形码" : "Code" }}
                </th>
                <!-- 规格 -->
                <th>{{ $t("ShopCarts.Spe") }}</th>
                <!-- 颜色 -->
                <th>{{ $t("ShopCarts.Colors") }}</th>
                <!-- 建议零售价 -->
                <th v-if="!userId == '' && goods.showRetailPrice === true">
                  {{ $t("ShopCarts.RecommendedPrice") }}
                </th>
                <!-- 会员价 -->
                <th>{{ $t("ShopCarts.MemPrice") }}</th>
                <!-- 服务单价 -->
                <th
                  v-show="
                    goods.activityFeeRelations &&
                    goods.activityFeeRelations.length > 0
                  "
                >
                  {{ $t("Quotation.SERVICEFEE") }}
                </th>
                <!-- 单位 -->
                <th
                  v-show="
                    goods.activityFeeRelations &&
                    goods.activityFeeRelations.length > 0
                  "
                >
                  {{ $t("Quotation.UNIT") }}
                </th>
                <!-- 库存 -->
                <th>{{ $t("P.Inventory") }}</th>
                <!-- 数量 -->
                <th width="13%">
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
                    <span
                      class="packing-unit"
                      :class="$i18n.locale === 'zh' ? '' : 'packing-en'"
                      slot="reference"
                      @click="lookBox(goods.goodsItems)"
                      >{{ $i18n.locale === "zh" ? "箱" : $t("P.PCSCTN") }}</span
                    >
                  </el-popover>
                </th>
              </tr>
              <tr
                class="goods-item"
                v-for="item in goods.goodsItems"
                :key="item.id"
              >
                <template
                  v-if="
                    (underStockFlag == 0 &&
                      (goods.tuType
                        ? item.onWayUsableCount + item.inStockUsableCount > 0
                        : item.inStockUsableCount > 0)) ||
                    underStockFlag == undefined
                  "
                >
                  <!-- 图片 -->
                  <td>
                    <div class="shop-img cursor-pointer">
                      <img
                        @click="magnify(item.itemImg)"
                        width="100%"
                        :src="item.itemImg"
                        alt
                      />
                    </div>
                  </td>
                  <!-- ERP物料编码、条形码 -->
                  <td>
                    <span class="code">{{ item.itemCode }}</span>
                    <br />
                    <span class="code">{{ item.barCode }}</span>
                  </td>
                  <!-- 规格 -->
                  <td>
                    <el-tooltip
                      class="item"
                      effect="dark"
                      :content="item.specsName"
                      placement="bottom"
                    >
                      <el-button>
                        <span v-show="$i18n.locale === 'zh'">{{
                          item.specsName
                        }}</span>
                        <span v-show="$i18n.locale === 'en'">{{
                          item.specsNameEn
                        }}</span>
                      </el-button>
                    </el-tooltip>
                  </td>
                  <!-- 颜色 -->
                  <td>
                    <el-tooltip
                      class="item"
                      effect="dark"
                      :content="item.colorName"
                      placement="bottom"
                    >
                      <el-button>
                        <span
                          v-show="
                            $i18n.locale === 'zh' || !item.colorNameEn == true
                          "
                          >{{ item.colorName }}</span
                        >
                        <span v-show="$i18n.locale === 'en'">{{
                          item.colorNameEn
                        }}</span>
                      </el-button>
                    </el-tooltip>
                  </td>
                  <!-- 建议零售价--登录 -->
                  <td v-if="!userId == '' && goods.showRetailPrice === true">
                    <span
                      v-if="
                        item.retailPrice !== null &&
                        Number(item.retailPrice) !== 0
                      "
                    >
                      <i v-show="$root.currency === 'CNY'">{{
                        item.salePrice && item.salePrice > item.retailPrice
                          ? $t("P.NoPricing")
                          : "￥" + item.retailPrice
                      }}</i>
                      <i v-show="$root.currency === 'USD'">{{
                        item.salePrice && item.salePrice > item.retailPrice
                          ? $t("P.NoPricing")
                          : "$" +
                            fomatFloat(Number(item.retailPrice) * exchange, 2)
                      }}</i>
                    </span>
                    <span v-else>--</span>
                  </td>
                  <!-- 建议零售价--未登录 -->
                  <td
                    v-else-if="userId === '' && goods.showRetailPrice === true"
                    class="rl-text-xxss"
                  >
                    {{ $t("P.ViewAfterLogin") }}
                  </td>
                  <!-- 会员价--登录 -->
                  <td v-if="!userId == ''">
                    <span
                      v-if="
                        Number(item.salePrice) !== 0 && item.salePrice !== null
                      "
                    >
                      <i v-show="$root.currency === 'CNY'"
                        >￥{{ item.salePrice }}</i
                      >
                      <i v-show="$root.currency === 'USD'"
                        >${{
                          fomatFloat(Number(item.salePrice) * exchange, 2)
                        }}</i
                      >
                    </span>
                    <span v-else>{{
                      item.salePrice === undefined || item.salePrice === null
                        ? !userId == ""
                          ? $t("P.Calculating")
                          : $t("P.ViewAfterLogin")
                        : $t("P.NoPricing")
                    }}</span>
                  </td>
                  <!-- 会员价--未登录 -->
                  <td v-else class="rl-text-xxss">
                    {{ $t("P.ViewAfterLogin") }}
                  </td>
                  <!--服务单价-登录--->
                  <td
                    v-show="
                      !userId == '' &&
                      goods.activityFeeRelations &&
                      goods.activityFeeRelations.length > 0
                    "
                  >
                    <span
                      v-if="
                        Number(item.serviceFee) !== 0 &&
                        item.serviceFee !== null
                      "
                    >
                      <i v-show="$root.currency === 'CNY'"
                        >￥{{ item.serviceFee }}/片</i
                      >
                      <i v-show="$root.currency === 'USD'"
                        >${{ item.serviceFeeEn }}/pc</i
                      >
                    </span>
                    <span v-else>--</span>
                  </td>
                  <!--服务单价-未登录登录--->
                  <td
                    v-show="
                      userId == '' &&
                      goods.activityFeeRelations &&
                      goods.activityFeeRelations.length > 0
                    "
                    class="rl-text-xxss"
                  >
                    {{ $t("P.ViewAfterLogin") }}
                  </td>
                  <!--单位 -登录--->
                  <td
                    v-show="
                      !userId == '' &&
                      goods.activityFeeRelations &&
                      goods.activityFeeRelations.length > 0
                    "
                  >
                    <span
                      v-if="
                        Number(item.serviceBoxNum) !== 0 &&
                        item.serviceBoxNum !== null
                      "
                    >
                      <i v-show="$root.currency === 'CNY'"
                        >{{ item.serviceBoxNum }}片/盒</i
                      >
                      <i v-show="$root.currency === 'USD'"
                        >{{ item.serviceBoxNum }}pc/box</i
                      >
                    </span>
                    <span v-if="item.unit != ''">{{ item.unit }}</span>
                    <span v-else>--</span>
                  </td>
                  <!--单位 -未登录--->
                  <td
                    v-show="
                      userId == '' &&
                      goods.activityFeeRelations &&
                      goods.activityFeeRelations.length > 0
                    "
                    class="rl-text-xxss"
                  >
                    {{ $t("P.ViewAfterLogin") }}
                  </td>
                  <!-- 库存--登录--不是在途 -->
                  <td class="rl-relative" v-if="!userId == '' && !goods.tuType">
                    <span class="blank-span"></span>
                    <el-popover
                      placement="bottom"
                      width="230"
                      v-model="item.visible"
                      trigger="click"
                    >
                      <!-- 库存不足 -->
                      <p class="rl-margin-bottom-xxs rl-tc rl-text-bold">
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
                          stockShowFlag == 1 && item.inStockUsableCount === 0
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
                  <!-- 库存--登录--在途 -->
                  <td
                    class="rl-relative"
                    v-else-if="!userId == '' && goods.tuType"
                  >
                    <span class="blank-span"></span>
                    <el-popover
                      placement="bottom"
                      width="230"
                      v-model="item.visible"
                      trigger="click"
                    >
                      <p class="rl-margin-bottom-xxs rl-tc rl-text-bold">
                        {{ $t("P.ShortInventory") }}
                      </p>

                      <div
                        class="rl-text-xxs"
                        slot="reference"
                        v-if="stockShowFlag === 0"
                      >
                        {{ item.onWayUsableCount + item.inStockUsableCount }}
                      </div>
                      <div
                        class="rl-text-xxs"
                        slot="reference"
                        v-else-if="
                          stockShowFlag === 1 &&
                          stockShowNumber <
                            item.onWayUsableCount + item.inStockUsableCount
                        "
                      >
                        {{ item.onWayUsableCount + item.inStockUsableCount }}
                      </div>
                      <div
                        class="rl-text-xxs"
                        slot="reference"
                        v-else-if="
                          stockShowFlag === 1 &&
                          item.onWayUsableCount + item.inStockUsableCount === 0
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
                            item.onWayUsableCount + item.inStockUsableCount <=
                            stockShowNumber
                        "
                      >
                        库存紧张
                      </div>
                    </el-popover>
                  </td>

                  <!-- 库存--未登录- -->
                  <td v-else class="rl-text-xxss">
                    {{ $t("P.ViewAfterLogin") }}
                  </td>
                  <!-- 数量--登录 -->
                  <td
                    v-if="!userId == ''"
                    :width="Number(item.status) === 1 ? 100 : ''"
                    :class="{ 'choose-unit': Number(item.status) !== 1 }"
                  >
                    <div class="rl-margin-left-xxxs">
                      <BuySum
                        ref="BuySum"
                        :item="item"
                        :tuType="goods.tuType"
                        :goodId="goods.id"
                        @itemInput="handleInput"
                      ></BuySum>
                      <!-- 批量下单 - 包装 -->
                      <div
                        class="box-info"
                        v-if="
                          Number(item.status) !== 1 &&
                          item.boxs &&
                          item.boxs.length > 0
                        "
                      >
                        <el-popover
                          placement="bottom"
                          width="220"
                          class="cursor-pointer"
                          popper-class="box-info-table"
                          trigger="click"
                          :ref="refNamePopover + item.id"
                        >
                          <el-table
                            :data="item.boxs"
                            @row-click="
                              (row, column, e) =>
                                handleCheck(row, column, e, item)
                            "
                            highlight-current-row
                          >
                            <el-table-column
                              :label="$t('Quotation.CTN')"
                              prop="boxNum"
                            ></el-table-column>
                            <el-table-column
                              :label="$t('Quotation.UNIT')"
                              prop="boxType"
                            ></el-table-column>
                          </el-table>
                          <span slot="reference">{{
                            item.choosedBoxInfo !== ""
                              ? item.choosedBoxInfo
                              : $t("P.Pieces")
                          }}</span>
                        </el-popover>
                      </div>
                    </div>
                  </td>
                  <!-- 数量--未登录 -->
                  <td v-else class="rl-text-xxss">
                    {{ $t("P.ViewAfterLogin") }}
                  </td>
                </template>
              </tr>
            </table>
          </div>
        </div>
        <!-- 加入购物车 -->
        <div
          class="rl-tr rl-padding-top-default rl-margin-bottom-default rl-clear"
          v-if="this.fastOrderGoodsList.length !== 0"
        >
          <div v-if="clickAgain === true" @click="addToShopingCart">
            <div
              v-if="scrollBottom === false"
              class="
                rl-fr
                join-shopcars
                rl-text-white rl-bg-blue-xs rl-tc rl-text-mid
                cursor-pointer
                rl-margin-left-double
              "
              :class="{ 'scroll-bottom': scrollBottom === true }"
            >
              {{ $t("P.AddToCart") }}
            </div>
            <div
              v-else
              class="
                rl-fr
                join-shopcars-img
                rl-text-white rl-tc rl-text-mid
                cursor-pointer
                rl-margin-left-double
              "
              :class="{ 'scroll-bottom': scrollBottom === true }"
            >
              <img width="100%" src="../assets/images/join-carts.png" />
            </div>
          </div>
          <div v-else>
            <div
              v-if="scrollBottom === false"
              class="
                rl-fr
                join-shopcars
                rl-text-white rl-bg-blue-xs rl-tc rl-text-mid
                cursor-pointer
                rl-margin-left-double
              "
              :class="{ 'scroll-bottom': scrollBottom === true }"
            >
              {{ $t("P.AddToCart") }}
            </div>
            <div
              v-else
              class="
                rl-fr
                join-shopcars-img
                rl-text-white rl-tc rl-text-mid
                cursor-pointer
                rl-margin-left-double
              "
              :class="{ 'scroll-bottom': scrollBottom === true }"
            >
              <img width="100%" src="../assets/images/join-carts.png" />
            </div>
          </div>
          <!-- 分页 -->
          <div class="rl-fr">
            <el-pagination
              background
              @current-change="fastHandleCurrentChange"
              @size-change="fastHandleSizeChange"
              layout="prev, pager, next, jumper"
              :page-size="fastPagesize"
              :current-page.sync="fastPage"
              :total="totalCount"
            ></el-pagination>
          </div>
        </div>
      </div>

      <!--商品为空-->
      <div
        class="empty-car rl-margin-zero"
        v-if="
          this.classifyGoodsList.length === 0 &&
          this.fastOrderGoodsList.length === 0 &&
          this.showLoading === false
        "
      >
        <div class="empty-car-img rl-margin-zero">
          <img width="100%" src="../assets/images/goods-empty.png" alt />
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
      <!--点击图片放大-->
      <div class="cover" v-if="showImgurl" @click="shutLogImg"></div>
      <div class="pro-cover-img rl-relative" v-if="showImgurl">
        <img width="100%" :src="imgurl" alt />
        <span @click="shutLogImg" class="shut cursor-pointer"></span>
      </div>
      <!--加载动画-->
      <loading v-if="this.showLoading === true"></loading>
    </div>
  </div>
</template>

<script>
import BuySum from "@/components/BuySum.vue";
import presellNum from "@/components/presellNum.vue";
import GoodsBuySum from "@/components/GoodsBuySum.vue";
import loading from "@/components/loading.vue";
import sha1 from "js-sha1";
import Vue from "vue";
import GD from "@/assets/js/globalData";
import { fomatFloat, fomatFloor, formatDate } from "@/assets/js/common.js";
import { getCartNum } from "@/api/cart";

// LIU
import {
  sectionGoodsList,
  columnGoodsList,
  columnClassifyList,
  subclassifyList,
  goodsList,
  priceGoodsList,
  goodsDetails,
  classifyList,
  goodsCollection,
  listStockByCondition,
  userShopSetting,
  priceItemList,
  addShoppingcart,
  shoppingcartList,
} from "@/apiService/api";
export default {
  name: "commonGoodsList",
  components: {
    BuySum,
    GoodsBuySum,
    loading,
    presellNum,
  },
  props: [
    "prosName",
    "prosNameEn",
    "cateId",
    "cateFlag",
    "classifyType",
    "searchCon",
    "diyType",
  ],
  data() {
    return {
      isAll: false,
      boxData: [],
      boxDialogVisible: false,
      activityType: 0, // 普通页面页跳商品详情
      totalCount: 0,
      cur_page: 1,
      pagesize: 25,
      fastPage: 1,
      fastPagesize: 10,
      classifyGoodsList: [],
      orderField: "", // 排序类型
      orderType: "", // 排序顺序
      upDownOne: true,
      upDownTwo: true,
      upDownThr: true,
      userId: "",
      capitalStatus: 0, // 资质状态:0,未提交 1,申请中,2,合作中 3,申请失败'
      freezeStatus: 0, // 冻结状态:2,冻结  1,未冻结',
      tabs: "showImg",
      imgurl: "",
      showImgurl: false,

      fastTotalCount: 0,
      fastOrderGoodsList: [],
      numInWarehouse: "", // 在库数量
      stockItemCount: "", // 在途数量

      onceAgain: true,
      scrollBottom: false, // 页面滚动是否到底部
      showLoading: false, // 展示加载动画
      clickAgain: true, // 用于防止二次点击
      joinShopShopcar: [], // 加入购物车
      stockShowFlag: 0, // 0 实际库存 1模糊库存
      stockShowNumber: 0, // 库存临界值
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: "zh-RMB", // 语种
      classifyId: undefined,
      selectClassifys: [], // 已选择的分类
      classifyList: [], // 分类列表
      hotList: [
        {
          type: undefined,
          typeName: "全部",
          typeNameEn: "All",
        },
        {
          type: 1,
          typeName: "活动热销",
          typeNameEn: "Bestselling",
        },
        {
          type: 2,
          typeName: "新品上市",
          typeNameEn: "Newest",
        },
        // {
        //   type: 3,
        //   typeName: "清仓热卖",
        //   typeNameEn: "Clearance",
        // },
      ], // 热门列表
      sortList: [
        {
          type: "",
          typeName: "综合",
          typeNameEn: "Comprehensive",
          orderType: undefined,
          isShow: false,
        },
        {
          type: 1,
          typeName: "价格",
          typeNameEn: "Sales volume",
          orderType: "desc",
          isShow: false,
        },
        {
          type: 2,
          typeName: "销量",
          typeNameEn: "Sales volume",
          orderType: "desc",
          isShow: false,
        },
        {
          type: 3,
          typeName: "时间",
          typeNameEn: "Time",
          orderType: "desc",
          isShow: false,
        },
      ],
      breadcrumbList: [],
      hotClicked: 0,
      sortClicked: 0,
      searchName: "",
      refNamePopover: "popover-", // popover ref名称前缀
      isCollectFee: null, // 是否有服务费

      // liu
      goodsIdList: [], //商品列表id集合
      exchange: 1, //汇率
      underStockFlag: undefined, //只看有货
    };
  },
  filters: {
    formatDate(time) {
      var date = new Date(time);
      return formatDate(date, "yyyy-MM-dd hh:mm:ss");
    },
  },
  methods: {
    // liu

    // 根据ids查询价格
    getPriceData(goodsIdList) {
      if (
        this.userId != null &&
        this.userId != "undefined" &&
        this.userId != "" &&
        this.freezeStatus == "1" &&
        this.capitalStatus == "2"
      ) {
        priceGoodsList({ goodsIds: goodsIdList }).then((res) => {
          if (res.success) {
            let list = JSON.parse(JSON.stringify(this.classifyGoodsList));
            list.forEach((item) => {
              for (let i = 0; i < res.data.length; i++) {
                if (item.id === res.data[i].goodsId) {
                  this.$set(item, "minPrice", res.data[i].minPrice);
                  this.$set(item, "maxPrice", res.data[i].maxPrice);
                }
              }
            });
            this.classifyGoodsList = list;
            console.log("商品列表：", this.classifyGoodsList);
            this.showLoading = false;
          }
        });
      }
    },

    // 只看有货的click
    underStockFlagFun() {
      this.underStockFlag = this.underStockFlag == 0 ? undefined : 0;
      if (this.tabs == "fastOrder") {
        this.getFastOrderList();
      } else if (this.tabs == "showImg") {
        if (this.cateFlag == "column") {
          this.getSectionCateList();
        } else {
          this.getGoodListFun();
        }
      }
    },

    // 个性和新品商品列表
    getGoodListFun() {
      this.showLoading = true;
      let goodsIdList = [];
      let goodsIds = "";
      let that = this;
      let size = 10;
      let page = 1;
      if (this.tabs == "fastOrder") {
        size = this.fastPagesize;
        page = this.fastPage;
      } else {
        size = this.pagesize;
        page = this.cur_page;
      }
      let hotTypeV = "";
      if (this.cateFlag == "new") {
        hotTypeV = 2;
      } else if (this.hotClicked >= 0) {
        hotTypeV = this.hotList[this.hotClicked].type;
      } else {
        hotTypeV = "";
      }
      let parmas = {
        page: page,
        size: size,
        goodsType: this.cateFlag == "custom" ? 2 : undefined,
        classifyId: this.classifyId === "" ? undefined : this.classifyId,
        content: this.cateFlag === "search" ? this.searchName : undefined,
        hotType: hotTypeV,
        sortType: this.sortList[this.sortClicked].type,
        // orderType: this.sortList[this.sortClicked].orderType,  //排序的高低 (高：desc, 低：asc)
        sortWay: this.sortList[this.sortClicked].orderType == "desc" ? 1 : 2,
        // newFlag:this.cateFlag=='new'?1:0,   //是否筛选新品 0-否 1-是
        underStockFlag: this.underStockFlag, //只看有货
      };
      goodsList(parmas).then((res) => {
        if (res.success) {
          this.classifyGoodsList = res.data.list;
          this.totalCount = res.data.total;
          if (this.classifyGoodsList.length > 0) {
            that.classifyGoodsList.forEach((item) => {
              goodsIdList.push(item.id);
              goodsIds += item.id + ",";
            });

            if (
              this.userId !== null &&
              this.userId !== "" &&
              this.freezeStatus == "1" &&
              this.capitalStatus == "2" &&
              goodsIdList.length > 0
            ) {
              // 用户已登录且未冻结合作中的分销商获取价格
              that.getPriceData(goodsIdList);
            } else {
              this.showLoading = false;
            }
            this.goodsIdList = goodsIdList;
            if (this.tabs == "fastOrder") {
              this.fastOrderFun(goodsIdList, res.data.list);
            }
          } else {
            this.showLoading = false;
          }
        }
      });
    },
    // 根据栏目获取商品列表
    getSectionCateList() {
      this.showLoading = true;
      let goodsIdList = [];
      let goodsIds = "";
      let size = 10;
      let page = 1;
      if (this.tabs == "fastOrder") {
        size = this.fastPagesize;
        page = this.fastPage;
      } else {
        size = this.pagesize;
        page = this.cur_page;
      }
      let that = this;
      let parmas = {
        page: page,
        size: size,
        classifyId: this.classifyId === "" ? undefined : this.classifyId,
        columnId: this.cateFlag === "column" ? this.prosId : undefined,
        content: this.cateFlag === "search" ? this.searchName : undefined,
        hotType:
          this.hotClicked >= 0 ? this.hotList[this.hotClicked].type : undefined,
        sortType: this.sortList[this.sortClicked].type,
        // orderType: this.sortList[this.sortClicked].orderType  //排序的高低 (高：desc, 低：asc)
        sortWay: this.sortList[this.sortClicked].orderType == "desc" ? 1 : 2,
        underStockFlag: this.underStockFlag, //只看有货
      };
      columnGoodsList(parmas).then((res) => {
        console.log(res.data);
        if (res.success) {
          this.showLoading = false;
          this.totalCount = res.data.total;
          this.classifyGoodsList = res.data.list;
          if (this.classifyGoodsList.length > 0) {
            that.classifyGoodsList.forEach((item) => {
              goodsIdList.push(item.id);
              goodsIds += item.id + ",";
            });
            // 用户已登录且未冻结合作中的分销商获取价格
            if (
              this.userId !== "" &&
              this.freezeStatus == "1" &&
              this.capitalStatus == "2" &&
              goodsIdList.length > 0
            ) {
              that.getPriceData(goodsIdList);
            }
            this.goodsIdList = goodsIdList;
            if (this.tabs == "fastOrder") {
              this.fastOrderFun(goodsIdList, res.data.list);
            }
          } else {
            this.showLoading = false;
          }
        }
      });
    },

    // 根据板块获取商品列表
    getSectionGoodsList() {
      this.showLoading = true;
      let goodsIdList = [];
      let goodsIds = "";
      let size = 10;
      let page = 1;
      if (this.tabs == "fastOrder") {
        size = this.fastPagesize;
        page = this.fastPage;
      } else {
        size = this.pagesize;
        page = this.cur_page;
      }
      let that = this;
      let parmas = {
        page: page,
        size: size,
        classifyId: this.classifyId === "" ? undefined : this.classifyId,
        sectionId: this.prosId,
        content: this.cateFlag === "search" ? this.searchName : undefined,
        hotType:
          this.hotClicked >= 0 ? this.hotList[this.hotClicked].type : undefined,
        sortType: this.sortList[this.sortClicked].type,
        sortWay: this.sortList[this.sortClicked].orderType == "desc" ? 1 : 2,
      };
      sectionGoodsList(parmas).then((res) => {
        if (res.success) {
          this.showLoading = false;
          this.totalCount = res.data.total;
          this.classifyGoodsList = res.data.list;
          if (this.classifyGoodsList.length > 0) {
            that.classifyGoodsList.forEach((item) => {
              goodsIdList.push(item.id);
              goodsIds += item.id + ",";
            });
            // 用户已登录且未冻结合作中的分销商获取价格
            if (
              this.userId &&
              this.freezeStatus === "1" &&
              this.capitalStatus === "1" &&
              goodsIdList.length > 0
            ) {
              that.getPriceData(goodsIdList);
            }
            this.goodsIdList = goodsIdList;
            if (this.tabs == "fastOrder") {
              this.fastOrderFun(goodsIdList, res.data.list);
            }
          } else {
            this.showLoading = false;
          }
        }
      });
    },
    // 切换页码
    handleCurrentChange(val) {
      this.cur_page = val;
      if (this.cateFlag == "column") {
        this.getSectionCateList();
      } else {
        this.getGoodListFun();
      }
    },
    // 切换条数
    handleSizeChange(val) {
      this.pagesize = val;
      if (this.cateFlag == "column") {
        this.getSectionCateList();
      } else {
        this.getGoodListFun();
      }
    },

    // 定制商品类型
    getDiyType(row) {
      switch (row) {
        case 0:
          return "IP " + this.$t("Index.Customized");
        case 1:
          return "DIY " + this.$t("Index.Customized");
        case 2:
          return this.$t("Index.Customized");
      }
    },

    // 其他栏目类型
    breadcrumbClassify(index) {
      if (this.selectClassifys.length - 1 !== index) {
        if (index === 0) {
          this.classifyId = undefined;
          this.getSectionCateClassifyList();
        } else {
          this.classifyId = this.selectClassifys[index].id;
          this.getClassifyList();
        }
        this.selectClassifys.splice(index + 1, this.selectClassifys.length);
        if (this.tabs == "fastOrder") {
          this.fastPage = 1;
          this.getFastOrderList();
        } else if (this.tabs == "showImg") {
          this.cur_page = 1;
          if (this.cateFlag == "column") {
            this.getSectionCateList();
          } else {
            this.getGoodListFun();
          }
        }
      }
    },
    // 所有商品---全部结果
    getAllResult() {
      this.classifyId = undefined;
      this.getSectionCateClassifyList();
      this.selectClassifys = [];
      if (this.tabs == "fastOrder") {
        this.fastPage = 1;
        this.getFastOrderList();
      } else if (this.tabs == "showImg") {
        this.cur_page = 1;
        this.getSectionCateList();
      }
    },
    // 选择分类
    selectClassify(index) {
      this.classifyId = this.classifyList[index].id;
      this.selectClassifys.push({
        id: this.classifyList[index].id,
        name: this.classifyList[index].name,
        nameEn: this.classifyList[index].nameEn,
      });
      this.getClassifyList();
      if (this.tabs == "fastOrder") {
        this.fastPage = 1;
        this.getFastOrderList();
      } else if (this.tabs == "showImg") {
        this.cur_page = 1;
        if (this.cateFlag == "column") {
          this.getSectionCateList();
        } else {
          this.getGoodListFun();
        }
      }
    },
    fomatFloor(val, n) {
      return fomatFloor(val, n);
    },
    fomatFloat(val, n) {
      return fomatFloat(val, n);
    },
    // 获取分类
    getSectionCateClassifyList(target) {
      if (target && this.cateFlag === "column") {
        this.prosId = target;
      }
      columnClassifyList({ columnId: this.prosId }).then((res) => {
        if (res.success) {
          this.classifyList = [];
          res.data.forEach((item) => {
            this.classifyList.push({
              id: item.id,
              name: item.name,
              nameEn: item.nameEn,
            });
          });
        }
      });
    },
    // 根据父级id获取分类列表
    getClassifyList() {
      subclassifyList({ classifyId: this.classifyId }).then((res) => {
        if (res.success) {
          console.log("fenlei", res.data);
          this.classifyList = [];
          res.data.forEach((item) => {
            this.classifyList.push({
              id: item.id,
              name: item.name,
              nameEn: item.nameEn,
            });
          });
        }
      });
    },

    // 获取全部分类
    getAllClassifyList() {
      classifyList().then((res) => {
        if (res.success) {
          console.log("fenlei", res.data);
          this.classifyList = [];
          res.data.forEach((item) => {
            this.classifyList.push({
              id: item.id,
              name: item.name,
              nameEn: item.nameEn,
            });
          });
        }
      });
    },

    // 选择热门类型
    selectHot(index) {
      if (this.hotClicked !== index) {
        this.hotClicked = index;
        if (this.tabs == "fastOrder") {
          this.fastPage = 1;
          this.getFastOrderList();
        } else if (this.tabs == "showImg") {
          this.cur_page = 1;
          if (this.cateFlag == "column") {
            this.getSectionCateList();
          } else {
            this.getGoodListFun();
          }
        }
      }
    },
    // 选择排序
    selectSort(index) {
      this.sortList[this.sortClicked].isShow = false;
      if (this.sortClicked === index && index > 0) {
        this.sortList[this.sortClicked].orderType =
          this.sortList[this.sortClicked].orderType === "desc" ? "asc" : "desc";
      } else {
        this.sortClicked = index;
      }
      this.sortList[this.sortClicked].isShow = true;
      if (this.tabs == "fastOrder") {
        this.getFastOrderList();
      } else if (this.tabs == "showImg") {
        if (this.cateFlag == "column") {
          this.getSectionCateList();
        } else {
          this.getGoodListFun();
        }
      }
    },
    fLangChange(value) {
      window.localStorage.setItem("bLang", value);
      this.$i18n.locale = value.split("-")[0];
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
          let list = JSON.parse(JSON.stringify(this.fastOrderGoodsList));
          list.forEach((item) => {
            if (id == item.id) {
              item.collectionFlag = methods == "POST" ? 1 : 0;
            }
          });
          this.fastOrderGoodsList = list;
        } else {
          this.$message(res.errMessage);
        }
      });
    },

    // 货品加减
    handleInput(item, goodId) {
      this.$set(item, "counts", Number(item.count) * item.choosedBoxNum);
    },
    //  货品-选择什么包装下单
    handleCheck(row, column, e, item) {
      this.$set(item, "choosedBoxInfo", row.boxType);
      this.$set(item, "choosedBoxNum", row.boxNum);
      this.$set(item, "boxId", row.id);
      this.$set(item, "counts", Number(item.count) * row.boxNum);
      let refName = this.refNamePopover + item.id;
      this.$refs[refName][0].doClose();
      this.$forceUpdate();
    },

    // 加入购物车
    addToShopingCart() {
      console.log(this.fastOrderGoodsList);
      let list = this.fastOrderGoodsList;
      let paramsList = [];
      list.forEach((item) => {
        item.goodsItems.forEach((items) => {
          if (Number(items.count) > 0) {
            console.log(items.weight);
            let objL = {
              imageUrl: items.itemImg,
              cartType: 1, //加购类型：1 分销商 2 C端客户
              diyType: item.diyType, //定制类型 0-标准定制 1-DIY定制
              goodsId: item.id, //商品id
              goodsName: item.goodsName,
              goodsNo: item.goodsNo, //商品编码
              goodsPromotionId: items.goodsPromotionId
                ? items.goodsPromotionId
                : "", //商品促销活动Id(活动条件id)
              goodsType: item.goodsType, //商品类型 1-普通 2-定制
              groupSeckillId: items.groupSeckillId ? items.groupSeckillId : "",
              itemCode: items.itemCode, //货品编码
              itemCount: items.count, //购物数量
              itemId: items.id, //货品id
              itemName:
                this.$i18n.locale === "zh"
                  ? items.itemName
                  : items.itemNameEn
                  ? items.itemNameEn
                  : items.itemName, //货品名称
              itemNameEn: items.itemNameEn ? items.itemNameEn : "", //货品英文名称
              itemType: items.itemType, //是否赠品 1 非赠品 2 赠品
              specsName:
                this.$i18n.locale === "zh"
                  ? items.specsName
                  : items.specsNameEn
                  ? items.specsNameEn
                  : items.specsName, //货品规格
              specsNameEn: items.specsNameEn ? items.specsNameEn : "",
              colorName:
                this.$i18n.locale === "zh"
                  ? items.colorName
                  : items.colorNameEn
                  ? items.colorNameEn
                  : items.colorName, //货品颜色
              colorNameEn: items.colorNameEn ? items.colorNameEn : "",
              weight: items.weight, //重量
              lenght: items.length,
              width: items.width,
              height: items.height,
              barCode: items.barCode, // 条形码
              unit: items.boxId, // 装箱规格id
            };
            paramsList.push(objL);
          }
        });
      });
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
    },
    // 查询购物车列表
    shoppingcartListFun() {
      shoppingcartList().then((res) => {
        if (res.success) {
          // this.$refs.header.cartCount = res.data.length;
        }
      });
    },

    lookBox(itemData) {
      let that = this;
      console.log("装箱规格", itemData);
      // 装箱规格
      this.boxData = []; // 清空
      let boxDataList = [];
      itemData.forEach((goods) => {
        let boxsL = goods.boxs;
        let innerWeight = 0; // 内盒重量
        let innerNum = 0; // 内盒数量
        boxsL.forEach((item) => {
          // if(item.defaultFlag==1){
          Vue.set(item, "size", ""); // 尺寸
          item.size =
            item.boxLength + "*" + item.boxWidth + "*" + item.boxHeight;
          if (item.boxType === "内盒" || item.boxType === "inner box") {
            if (this.$i18n.locale === "en") {
              item.boxType = "inner box";
            }
            innerWeight = item.boxWeight;
            innerNum = item.boxNum;
            item.boxWeight = goods.weight * item.boxNum;
          } else if (item.boxType === "外盒" || item.boxType === "outer box") {
            if (this.$i18n.locale === "en") {
              item.boxType = "outer box";
            }
            if (innerNum > 0) {
              // 当内盒数量大于0
              // +Number(item.boxWeight)
              item.boxWeight =
                goods.weight * item.boxNum +
                innerWeight * (item.boxNum / innerNum);
            } else {
              item.boxWeight = goods.weight * item.boxNum;
            }
            item.boxWeight = item.boxWeight.toFixed(0);
          }
          console.log(item);
          if (
            item.boxType === "内盒" ||
            item.boxType === "inner box" ||
            item.boxType === "外盒" ||
            item.boxType === "outer box"
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
      console.log("装箱规格2：", that.boxData);
    },

    // 去商品详情
    goDoodsDetail(id, goodsType) {
      let routeData = this.$router.resolve({
        name: "ShopDetail",
        query: {
          good_id: id,
          activityType: 4, //1:拼团  2：秒杀  3：活动  4：其他
          goodsType: goodsType,
          accessType: 0,
        },
      });
      window.open(routeData.href, "_blank");
    },

    // 获取是否 0 实际库存 1模糊库存
    userShopSettingFun() {
      userShopSetting().then((res) => {
        if (res.success) {
          this.stockShowFlag = res.data.stockShowFlag;
          this.stockShowNumber = res.data.stockShowNumber;
        }
        //  console.log('实际库存:',res.data);
      });
    },

    //获取快速订单 （即商品详情接口）
    getFastOrderList() {
      this.fastOrderGoodsList = [];
      let that = this;
      this.showLoading = true;
      let cateFlag = this.cateFlag;
      if (this.userId && this.userId !== "" && this.userId !== "undefined") {
        // 用户已登录且未冻结合作中的分销商获取价格
        this.userShopSettingFun();
      }

      this.tabs = "fastOrder";
      if (
        cateFlag == "custom" ||
        cateFlag == "new" ||
        cateFlag == "search" ||
        cateFlag == "classify"
      ) {
        this.getGoodListFun();
      } else if (cateFlag == "column") {
        this.getSectionCateList();
      } else if (cateFlag == "section") {
        this.getSectionGoodsList();
      }
    },
    // 快速下单接口封装
    fastOrderFun(goodsIdList, dataList) {
      let fastDataList = dataList;
      let fastOrderGoodsList = [];
      this.fastOrderGoodsList = [];
      let list = JSON.parse(JSON.stringify(goodsIdList));
      // 在途的判断
      let sysAutoDelivery = localStorage.getItem("autoDelivery"); //分销商是否是直发客户：1 是，0 否
      let sysOnWayFlag = localStorage.getItem("onWayFlag"); //分销商是否支持在途库存 1是 0否 默认是1
      let onWayFlag = 0;

      for (let i = 0; i < list.length; i++) {
        goodsDetails({ id: list[i] }).then((res) => {
          if (res.success) {
            let data = res.data;
            if (
              this.userId &&
              this.userId !== "" &&
              this.userId !== "undefined"
            ) {
              // 根据id查询收藏状态
              goodsCollection("GET", { id: res.data.id }).then((collectRes) => {
                if (collectRes.success) {
                  if (collectRes.data) {
                    data.collectionFlag = 1;
                  } else {
                    data.collectionFlag = 0;
                  }
                }
              });
            }

            data.showRetailPrice = true; // 是否展示零售价
            let ids = [];
            let twoGoodsIdList = []; //查询货品的价格
            if (data.goodsItems && data.goodsItems.length > 0) {
              data.goodsItems.forEach((item, index) => {
                //onwaySaleFlag 直发客户是否支持在途：0-否 1-是
                if (sysAutoDelivery == 1) {
                  onWayFlag =
                    sysOnWayFlag == 1 ? (item.onwaySaleFlag == 1 ? 1 : 0) : 0;
                } else {
                  onWayFlag = sysOnWayFlag;
                }
                this.$set(item, "sysOnWayFlag", Number(onWayFlag));
                // 默认取第一个货品的在途值
                if (index == 0) {
                  data.sysOnWayFlag = Number(onWayFlag);
                }
                let itemsIdObj = {
                  goodsId: list[i], //商品id
                  itemId: item.id, //货品id
                };
                this.$set(item, "retailPrice", 0);
                this.$set(item, "salePrice", 0);
                twoGoodsIdList.push(itemsIdObj);
                ids.push(item.id); // 获取货品id
                item.type = 0; // 缺货登记
                item.remark = ""; // 缺货登记备注
                item.presell = 0; // 预售
                item.visible = false; // 预售库存不足弹框效果
                item.itemType = data.goodsType; // 货品类型
                if (item.moq === "停产" || item.moq === "" || item.moq === null) {
                  item.advanceSaleFlag = 0;
                }
                if (
                  item.salePrice > item.retailPrice &&
                  item.retailPrice !== null
                ) {
                  // 当存在会员价大于建议零售价
                  val.showRetailPrice = false;
                }

                // 批量下单 - 包装
                this.$set(item, 'choosedBoxNum',1);
                // 处理装箱规格
                let objs={
                  boxType: '件',
                  boxNum: 1,
                }
                if(item.boxs&&item.boxs.length>0){
                  // 有装箱规格
                  this.$set(item, 'boxId', '');

                  // 判断是否有设置批量下单-默认下单形式：1 是，0 否
                  let hasDefaultFlag = false;
                  let defaultItem = {};
                  item.boxs.forEach((box) => {
                    if (box.defaultFlag == 1) {
                      hasDefaultFlag = true;
                      defaultItem = box;
                    }
                  })

                  if (hasDefaultFlag) {
                    // 有设置批量下单-默认下单形式，只能选择该形式
                    this.$set(item, 'choosedBoxNum', defaultItem.boxNum);
                    this.$set(item, 'choosedBoxInfo', defaultItem.boxType);
                    this.$set(item, 'boxId', defaultItem.id);
                    
                    let boxs = [];
                    boxs.push(defaultItem);
                    this.$set(item, "boxs", boxs);
                  } else {
                    // 无设置批量下单-默认下单形式
                    let boxList=[];
                    this.$set(item, 'choosedBoxNum', 1);
                    this.$set(item, 'choosedBoxInfo', '件');
                    item.boxs.forEach((box) => {
                      boxList.push(box)
                    })
                    boxList.push(objs);
                    this.$set(item, "boxs", boxList);
                  }
                } else {
                  // 无装箱规格，默认件
                  this.$set(item, 'choosedBoxInfo', '件');
                  this.$set(item, 'choosedBoxNum', 1);

                  let boxs = [];
                  boxs.push(objs);
                  this.$set(item, "boxs", boxs);
                }
              });
            }
            let itemIds = ids;

            setTimeout(() => {
              let stockDataGoodsList = JSON.parse(JSON.stringify(data));
              if (
                this.userId &&
                this.userId !== "" &&
                this.userId !== "undefined"
              ) {
                // 获取商品库存
                let params = {
                  distributorId: this.userId,
                  itemIdList: itemIds,
                };
                if (itemIds && itemIds.length > 0) {
                  listStockByCondition(params).then((stockRes) => {
                    if (stockRes.success) {
                      let stockResData = stockRes.data;

                      for (let i = 0; i < stockResData.length; i++) {
                        stockDataGoodsList.goodsItems.forEach((items) => {
                          if (items.id === stockResData[i].itemId) {
                            this.$set(
                              items,
                              "inStockUsableCount",
                              stockResData[i].inStockUsableCount > 0
                                ? stockResData[i].inStockUsableCount
                                : 0
                            ); // 在库库存
                            this.$set(
                              items,
                              "onWayUsableCount",
                              stockResData[i].onWayUsableCount -
                                stockResData[i].inStockUsableCount >
                                0
                                ? stockResData[i].onWayUsableCount -
                                    stockResData[i].inStockUsableCount
                                : 0
                            ); // 在途库存
                          }
                        });
                      }
                    }
                  });
                }
                //  获取货品的价格
                let paramsPrice = {
                  goodsItemIds: twoGoodsIdList,
                };
                if (twoGoodsIdList && twoGoodsIdList.length > 0) {
                  priceItemList(paramsPrice).then((res) => {
                    if (res.success) {
                      this.showLoading = false;
                      let priceList = res.data;
                      stockDataGoodsList.goodsItems.forEach((item) => {
                        priceList.forEach((items) => {
                          if (items.itemId == item.id) {
                            this.$set(item, "retailPrice", items.retailPrice);
                            this.$set(item, "salePrice", items.salePrice);
                          }
                        });
                      });
                    } else {
                      this.$message(e);
                    }
                  });
                }
                fastOrderGoodsList.push(stockDataGoodsList);
              } else {
                this.showLoading = false;
                fastOrderGoodsList.push(stockDataGoodsList);
              }

              // 根据后台返回商品列表拼接商品id
              let curArr = [];
              dataList.find((item) => {
                curArr.push(item.id);
              });
              // 根据已拼接商品id数组的顺序对当前查询到商品详情的数组进行排序
              let newArr = [];
              fastOrderGoodsList.sort((a, b) => {
                const prev = curArr.indexOf(a.id);
                const next = curArr.indexOf(b.id);
                return prev - next;
              });
              fastOrderGoodsList.forEach((item) => {
                newArr.push(item);
              });
              // 展示商品列表（保持排序与后台返回一致）
              this.fastOrderGoodsList = newArr;
            }, 200);
          }
        });
      }
    },

    fastHandleCurrentChange(val) {
      this.fastPage = val;

      window.scrollTo(0, 0);
      this.getFastOrderList();
    },
    fastHandleSizeChange(val) {
      this.fastPagesize = val;
      this.getFastOrderList();
    },
    // 图片展示和快速订货tab切换
    changeTu(goods, type) {
      this.$forceUpdate(); // 更新页面数据
      goods.tuType = !type;
    },
    toShowImg() {
      this.$forceUpdate(); // 更新页面数据
      this.tabs = "showImg";

      if (this.cateFlag == "column") {
        this.getSectionCateList();
      } else {
        this.getGoodListFun();
      }
    },
    toFastOrder() {
      this.$forceUpdate(); // 更新页面数据
      this.tabs = "fastOrder";
      this.getFastOrderList();
    },
    magnify(img) {
      // 点击放大上传的图片
      this.showImgurl = true;
      this.imgurl = img;
    },
    shutLogImg() {
      this.showImgurl = false;
    },
  },
  created() {
    this.exchange = Number(localStorage.getItem("exchange"));
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem("bLang")
      ? window.localStorage.getItem("bLang")
      : "zh-RMB";
  },
  mounted() {
    this.userId = window.localStorage.getItem("userId");
    this.capitalStatus = window.localStorage.getItem("capitalStatus");
    this.freezeStatus = window.localStorage.getItem("freezeStatus");
    let self = this;
    window.onscroll = function () {
      var scrollTop =
        document.documentElement.scrollTop || document.body.scrollTop;
      var windowHeight =
        document.documentElement.clientHeight || document.body.clientHeight;
      var scrollHeight =
        document.documentElement.scrollHeight || document.body.scrollHeight;
      if (scrollTop + windowHeight >= scrollHeight - 522) {
        self.scrollBottom = false;
      } else {
        self.scrollBottom = true;
      }
    };
    this.prosId = this.cateId; //分类id
    if (this.prosId) {
      this.selectClassifys.push({
        id: "",
        name: this.prosName,
        nameEn: this.prosNameEn,
      });
    }

    if (this.$route.query.classsity_id) {
      this.classifyId = this.$route.query.classsity_id;
      this.selectClassifys.push({
        id: this.$route.query.classsity_id,
        name: this.$route.query.classsity_name,
        nameEn: this.$route.query.classsity_name_en,
      });
    }

    if (this.classifyType == "classify") {
      this.getSectionCateClassifyList();
    }

    if (this.cateFlag === "search") {
      this.searchName = this.searchCon;
    }
    if (this.cateFlag === "column") {
      // 图片展示
      this.tabs = "showImg";

      this.getSectionCateList();
    } else if (this.cateFlag == "custom" || this.cateFlag == "new") {
      this.getGoodListFun();
    } else if (this.cateFlag == "classify") {
      this.tabs = "fastOrder";
      this.getFastOrderList();
      // this.getGoodListFun();
      this.getAllClassifyList();
    } else {
      // 快速订货
      this.tabs = "fastOrder";
      this.getFastOrderList();
    }
  },
  watch: {
    $route(to, from) {
      if (to.name === from.name) {
        this.tabs = "showImg";
        if (to.name === "ColumnOne") {
          this.selectClassifys[0] = {
            id: "",
            name: to.query.title,
            nameEn: to.query.titleEn,
          };
        } else if (to.name === "SearchPage") {
          this.searchName = to.query.search_name;
          this.selectClassifys[0] = {
            id: "",
            name: this.searchName,
            nameEn: this.searchName,
          };
        }
        // 图片展示
        this.tabs = "fastOrder";
        // 快速订货
        this.getFastOrderList();
      }
    },
  },
};
</script>

<style scoped="scoped" lang="less">
@import url("../assets/less/variable.less");
.index {
  width: 100%;
}
.dingzhi-btn {
  margin: 0 auto;
  width: 60px;
  height: 30px;
  line-height: 30px;
  border: 1px solid @bdColor;
  border-radius: 5px;
  font-size: 12px;
  cursor: pointer;
}
.main {
  width: @center;
  margin: 0 auto;
  .breed {
    position: relative;
    padding-top: 10px;
    padding-bottom: 16px;
    .allResults {
      cursor: pointer;
      /deep/ .el-breadcrumb__inner:hover {
        color: @blue;
      }
    }
    .el-breadcrumb {
      font-size: 12px;
      color: @lighterBlack;
    }
  }
  .sort-right {
    position: absolute;
    right: 0;
    bottom: 10px;
    border: 1px solid @blue;
    border-radius: 4px;
    div {
      padding: 6px 19px;
      color: @blue;
      background-color: @white;
      &:first-child {
        border-top-left-radius: 4px;
        border-bottom-left-radius: 4px;
      }
      &:last-child {
        border-top-right-radius: 4px;
        border-bottom-right-radius: 4px;
      }
    }
    div.current {
      color: @white;
      background-color: @blue;
    }
  }
  .sort-wrap {
    padding: 15px 40px;
    margin-bottom: 40px;
    background-color: @grayBg;
  }
  .classify-list {
    ul {
      li {
        margin-bottom: 12px;
        margin-left: 12px;
        float: left;
        cursor: pointer;
        transition: all 0.2s linear;
        border-radius: 16px;
        overflow: hidden;
        .content {
          height: 325px;
          width: 230px;
          background-color: @proBg;
          .img {
            width: 230px;
            height: 230px;
            display: table-cell; /*主要是这个属性*/
            vertical-align: middle;
            text-align: center;
            img {
              height: 230px;
              width: 100%;
              vertical-align: middle;
            }
          }
          .imgLable {
            position: absolute;
            top: 0;
            left: 50%;
            padding: 0 5px;
            height: 20px;
            font-size: 10px;
            color: @white;
            transform: translateX(-50%);
            border-radius: 0 0 4px 4px;
            &.hotLable {
              background-color: @red;
            }
            &.newLable {
              background-color: @blue;
            }
          }
          .classify-info {
            width: 192px;
            padding-left: 19px;
            padding-right: 19px;
          }
          .brand {
            font-size: 14px;
            width: 100%;
            height: 44px;
            font-family: FZHei-B01;
            font-weight: 400;
            color: @lightBlack;
            line-height: 20px;
            overflow: hidden;
            float: left;
            text-align: left;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
          }
          .brand1 {
            height: 22px;
            -webkit-line-clamp: 1;
          }
          .word {
            float: left;
            line-height: 22px;
            text-align: left;
            font-size: 12px;
            font-family: FZHei-B01;
            font-weight: 400;
            color: @gray;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
            .label:nth-child(n + 2) {
              margin-left: 4px;
            }
          }
          .moneyLabel {
            width: 192px;
            float: left;
            line-height: 22px;
            padding-top: 5px;
            text-align: left;
            vertical-align: bottom;
          }
          .money {
            font-size: 20px;
            font-family: Myriad Pro;
            font-weight: 550;
            color: @red;
          }
          .money1 {
            font-size: 14px;
            font-family: FZHei-B01;
            font-weight: 400;
            color: @red;
          }
          .currency {
            font-size: 12px;
            font-family: Myriad Pro;
            font-weight: 550;
            color: @red;
          }
          .currency1 {
            font-size: 13px;
            font-family: Myriad Pro;
            font-weight: 400;
            color: @gray;
            margin-left: 5px;
            text-decoration: line-through;
          }
          .label {
            border-radius: 18px;
            padding-left: 6px;
            padding-right: 6px;
            font-size: 12px;
            line-height: 18px;
            font-family: FZHei-B01;
            font-weight: 400;
            color: @red;
            -webkit-text-size-adjust: none;
            display: inline-block;
            border: 1px solid @red;
          }
          .saleCount {
            font-size: 12px;
            font-family: FZHei-B01;
            font-weight: 400;
            float: right;
            color: @gray;
          }
        }
        .content:hover {
          border-radius: 4px;
        }
      }
      li:hover {
        box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
        transform: translate3d(0, -2px, 0);
      }
      li:nth-child(5n + 1) {
        margin-left: 0px;
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
// 在途
.zaitu {
  height: 30px;
  line-height: 30px;
  span {
    display: block;
    width: 16px;
    height: 30px;
    background: url("../assets/images/choose.png") no-repeat center center;
  }
  span.gou {
    background: url("../assets/images/gou.png") no-repeat center center;
  }
}
/*快速订货*/
.quick-wrap {
  padding: 30px 37px;
  border: 2px solid rgba(209, 209, 209, 0.4);
  border-radius: 8px;
  & + .quick-wrap {
    margin-top: 30px;
  }
  .goods-details {
    width: calc(100% - 115px);
    padding-left: 35px;
    .dingzhi {
      position: relative;
      top: 5px;
      margin-right: 5px;
      padding: 1px 5px;
      font-size: 12px;
      color: @red;
      border: 1px solid @red;
      border-radius: 2px;
    }
    .pro-name {
      font-size: 20px;
      line-height: 28px;
      &:hover {
        color: @blue;
      }
    }
    .introduce {
      margin-top: 5px;
      font-size: 12px;
      font-family: FZHei-B01;
      font-weight: 400;
      color: @red;
    }
    .brand {
      margin-top: 30px;
      .title {
        font-size: 12px;
        color: @lighterBlack;
        & + .title {
          margin-left: 120px;
        }
      }
      .titleContent {
        margin-left: 15px;
        color: @lightBlack;
      }
    }
    .goodsName {
      position: relative;
      .collection {
        position: absolute;
        top: 9px;
        right: 0;
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
  }
  .goods-item:hover,
  .goods-item:active {
    background-color: @grayBg;
  }
  .promotionStatus,
  .newProductStatus {
    position: relative;
    top: 5px;
    margin-left: 10px;
  }
  .promotionStatus {
    background-color: @redLabel;
  }
  .newProductStatus {
    background-color: @blue;
  }
  .el-input-number {
    width: 110px;
  }
}
.fast-img {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  overflow: hidden;
  img {
    width: 100%;
    transition: all 0.2s linear;
  }
  img:hover {
    box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
    transform: translate3d(0, -2px, 0);
  }
}
.fast-table {
  margin-top: 28px;
  table {
    word-wrap: break-word;
    word-break: break-all;
    border-collapse: collapse;
    width: 100%;
    .code {
      line-height: 20px;
    }
    tr {
      th {
        max-width: 98px;
        height: 30px;
        text-align: center;
        font-weight: 400;
        color: #626162;
        font-size: 12px;
      }
      td.td-h1 {
        white-space: normal;
        .activity-label {
          width: 22px;
          height: 50px;
          line-height: 25px;
          border-radius: 5px;
          word-wrap: break-word;
        }
      }
      td {
        text-align: center;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        color: #000;
        font-size: 12px;
        button {
          padding: 0;
          border: 0;
          background-color: #f5f5f5;
          font-size: 12px;
          color: #000;
        }
        .register {
          width: 55px;
          font-size: 12px;
          color: #ff7900;
        }
      }
      & + tr {
        border-top: 1px dashed @bdColor;
      }
    }
  }
  .table-content {
    td {
      height: 50px;
    }
    .buy-sum {
      margin: 0 auto;
    }
    .shop-img {
      width: 40px;
      height: 40px;
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
}
.buy-sum {
  width: 92px;
  height: 22px;
  line-height: 22px;
  border: 1px solid @bdColor;
  div {
    height: 22px;
    box-sizing: border-box;
    background-color: @white;
    input {
      width: 62px;
    }
  }
  .buyac {
    width: 22px;
    font-size: 22px;
    color: @gray;
    cursor: pointer;
    text-align: center;
  }
  .buyb {
    width: 48px;
    line-height: 22px;
    color: @lightBlack;
    border-left: 1px solid @bdColor;
    border-right: 1px solid @bdColor;
    input {
      width: 46px;
    }
  }
}
/*加入购物车*/
.join-shopcars {
  width: 112px;
  height: 35px;
  line-height: 35px;
  border-radius: 6px;
  z-index: 1;
}
.join-shopcars-img {
  width: 51px;
  z-index: 1;
}
.join-shopcars-img.scroll-bottom {
  position: fixed;
  bottom: 5%;
  left: 50%;
  margin-left: 550px;
}
/*缺货登记弹框*/
.cover {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: @black;
  z-index: 99;
  opacity: 0.6;
}
.pro-cover {
  width: 560px;
  height: 450px;
  border: 1px solid @bdColor;
  border-radius: 5px;
  z-index: 99;
  background: @white;
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
      background-color: @blue;
      color: @white;
      border-radius: 5px;
    }
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
    background: url("../assets/images/shut.png") no-repeat center center;
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
    background: url("../assets/images/shut.png") no-repeat center center;
  }
}
/*定制商品分组样式*/
.shop-img {
  padding: 2px;
  img {
    vertical-align: middle;
  }
}
.fen-group {
  background-color: @grayBg;
  ul {
    li {
      float: left;
      width: 120px;
      height: 30px;
      line-height: 30px;
      cursor: pointer;
      button {
        padding: 0;
        border: 0;
        background-color: @grayBg;
        font-size: 12px;
        color: @lightBlack;
      }
    }
    li.current {
      background-color: @blueLabel;
      button {
        background-color: @blueLabel;
        color: @lightBlack;
      }
    }
  }
}
/*预售*/
.button-presell {
  margin: 0 auto;
  width: 220px;
  span {
    display: inline-block;
    width: 100px;
    height: 35px;
    line-height: 35px;
    text-align: center;
    background-color: @blue;
    color: @white;
    border-radius: 5px;
  }
}
.blank-span {
  position: absolute;
  display: block;
  width: 100%;
  height: 30px;
  background-color: @white;
  z-index: 1;
  opacity: 0;
}
// 批量下单 - 包装
.choose-unit {
  .inner-content {
    display: inline-block;
  }
  .buy-sum {
    float: left;
    margin-right: 10px;
  }
  .box-info {
    float: left;
    position: relative;
    left: 3px;
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
</style>
