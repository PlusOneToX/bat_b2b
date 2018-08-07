<template>
  <div class="good">
    <!---上下分布---->
    <div v-if="type === 1" class="recommend-goods-list rl-clear one">
      <div class="nav-top">
        <a target="_blank" :href="section_url" class="nav-item" v-if="$i18n.locale === 'zh' || !section_img2 == true">
          <img :src="section_img" alt />
        </a>
        <a target="_blank" :href="section_url2" class="nav-item" v-else>
          <img :src="section_img2" alt />
        </a>
        <a target="_blank" :href="section_url1" class="nav-item" v-if="$i18n.locale === 'zh' || !section_img3 == true">
          <img :src="section_img1" alt />
        </a>
        <a target="_blank" :href="section_url3" class="nav-item" v-else>
          <img :src="section_img3" alt />
        </a>
      
      </div>
      <div @click="goGoodsDetail(shop, shop.id, shop.goodsType)" v-for="(shop, index) in shopList"
        :key="shop.id"  class="good-item"  v-if="index < 5">
        <div class="content" >
          <div class="item-tag" v-show="shop.promotionFlag === 1">
            {{ $t("P.SellWell") }}
          </div>
          <div class="item-tag new" v-show="shop.newFlag === 1 &&shop.promotionFlag !== 1">
            {{ $t("P.NewProducts") }}
          </div>
          <div class="img">
            <img :src="($i18n.locale === 'zh' || !shop.imageUrl1en == true? shop.imageUrl1: shop.imageUrl1en) +'?x-oss-process=image/resize,h_458,l_458'" alt/>
          </div>
          <div class="item-info">
            <div v-if="$i18n.locale === 'zh'">
              <div class="brand rl-tc rl-margin-top-xxxs">
                <el-tooltip class="item"  effect="dark" :content="$i18n.locale === 'zh' || !shop.goodsNameEn == true? shop.goodsName: shop.goodsNameEn" placement="bottom">
                  <span>{{$i18n.locale === "zh" || !shop.oodsNameEn == true? shop.goodsName: shop.goodsNameEn}}</span>
                </el-tooltip>
              </div>
            </div> 
            <div v-else>
              <div
                v-if="
                  (shop.goods.tags === undefined ||
                    shop.goods.tags === null ||
                    shop.goods.tags.length === 0) &&
                  (shop.goods.introduce === undefined ||
                    shop.goods.introduce === null ||
                    shop.goods.introduce.length === 0)" class="brand rl-margin-top-xxxs">
                <el-tooltip
                  class="item"
                  effect="dark"
                  :content="
                    $i18n.locale === 'zh' || !shop.goods.goodsNameEn == true
                      ? shop.goods.goodsName
                      : shop.goods.goodsNameEn
                  "
                  placement="bottom">
                  <span>{{
                    $i18n.locale === "zh" || !shop.goods.goodsNameEn == true
                      ? shop.goods.goodsName
                      : shop.goods.goodsNameEn
                  }}</span>
                </el-tooltip>
              </div>
              <div v-else class="brand brand1 rl-margin-top-xxxs">
                <el-tooltip
                  class="item"
                  effect="dark"
                  :content="
                    $i18n.locale === 'zh' || !shop.goods.goodsNameEn == true
                      ? shop.goods.goodsName
                      : shop.goods.goodsNameEn
                  "
                  placement="bottom"
                >
                  <span>{{
                    $i18n.locale === "zh" || !shop.goods.goodsNameEn == true
                      ? shop.goods.goodsName
                      : shop.goods.goodsNameEn
                  }}</span>
                </el-tooltip>
              </div>
              <div
                class="word rl-tc"
                v-show="
                  $i18n.locale === 'en' &&
                  shop.goods.tags !== undefined &&
                  shop.goods.tags !== null &&
                  shop.goods.tags.length > 0
                "
              >
                <span
                  class="label"
                  v-for="(tagItem, index) in shop.goods.tags"
                  :key="index"
                  >{{ tagItem.name }}</span
                >
              </div>
              <div
                class="word rl-tc"
                v-show="
                  $i18n.locale === 'en' &&
                  (shop.goods.tags === undefined ||
                    shop.goods.tags === null ||
                    shop.goods.tags.length === 0) &&
                  shop.goods.introduceEn !== undefined &&
                  shop.goods.introduceEn !== null &&
                  shop.goods.introduceEn !== ''
                "
              >
                <span>{{ shop.goods.introduceEn }}</span>
              </div>
            </div>
            <div class="moneyLabel">
              <div v-if="Number(fomatFloat(shop.minPrice, 2)) !== 0"  style="float: left">
                <span class="currency">{{$i18n.locale === "en" && $root.currency === "USD"? "$": "¥"}}
                      {{$i18n.locale === "en" && $root.currency === "USD"? fomatFloat(shop.minPrice*exchange, 2): fomatFloat(shop.minPrice, 2)}}</span> 
              </div>
              <div style="float: left" v-else>
                <span class="money1">{{shop.minPrice === undefined ||shop.minPrice === null? !userId == ""? $t("P.Calculating"): $t("P.ViewAfterLogin"): $t("P.NoPricing")}}</span>
              </div>
              <div v-if="$i18n.locale === 'zh' && !userId == ''" style="float: right">
                <span v-if="shop.saleNum > 10000" class="saleCount" >{{ $t("P.sale") }}
                  {{ fomatFloor(shop.saleNum / 10000, 2) }} 万+</span>
                <span v-else class="saleCount">{{ $t("P.sale") }} {{ shop.saleNum || 0 }}</span>
              </div>
              <div v-if="$i18n.locale === 'en' && !userId == ''" style="float: right">
                <span v-if="shop.saleNum > 1000" class="saleCount">{{ fomatFloor(shop.saleNum / 1000, 1) }}K+ {{ $t("P.sale") }}</span>
                <span v-else class="saleCount">{{ shop.saleNum || 0 }} {{ $t("P.sale") }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    
    <!---左右分布---->
    <div v-if="type === 2" class="recommend-goods-list rl-clear">
      <a
        target="_blank"
        :href="section_url"
        class="grid-promo"
        v-if="$i18n.locale === 'zh' || !section_img2 == true"
      >
        <img :src="section_img" alt class="banner-img" />
      </a>
      <a target="_blank" :href="section_url2" class="grid-promo" v-else>
        <img :src="section_img2" alt class="banner-img" />
      </a>
      <div class="grid-list">
        <a
          target="_blank"
          :href="section_url1"
          class="grid-hot"
          v-if="$i18n.locale === 'zh' || !section_img3 == true"
        >
          <img :src="section_img1" alt class="grid-hot-img" />
        </a>
        <a target="_blank" :href="section_url3" class="grid-hot" v-else>
          <img :src="section_img3" alt class="grid-hot-img" />
        </a>
        <div class="grid-item"  @click="goGoodsDetail(shop, shop.id, shop.goodsType)"
          v-for="(shop, index) in shopList" :key="shop.id" v-if="index < 6">
          <div class="item-tag" v-show="shop.promotionFlag === 1">
            {{ $t("P.SellWell") }}
          </div>
          <div class="item-tag new" v-show="shop.newFlag === 1 &&shop.promotionFlag !== 1">
            {{ $t("P.NewProducts") }}
          </div>
          <div class="item-img">
            <img :src="($i18n.locale === 'zh' || !shop.imageUrl1en == true? shop.imageUrl1: shop.imageUrl1en) +'?x-oss-process=image/resize,h_458,l_458'" alt/>
          </div>
          <div class="item-box">
            <div class="brand rl-margin-top-xxxs">
              <el-tooltip
                class="item"
                effect="dark"
                :content="$i18n.locale === 'zh' || !shop.goodsNameEn == true? shop.goodsName: shop.goodsNameEn" placement="bottom">
                <span>{{$i18n.locale === "zh" || !shop.goodsNameEn == true? shop.goodsName: shop.goodsNameEn}}</span>
              </el-tooltip>
            </div>
            
            <div
              class="desc"
              v-show="
                $i18n.locale === 'zh' &&
                shop.tags !== undefined &&
                shop.tags !== null &&
                shop.tags.length > 0
              "
            >
              <span
                class="label"
                v-for="(tagsItem, index) in shop.tags"
                :key="index"
                >{{ tagsItem.name }}</span
              >
            </div>
            <div
              class="desc"
              v-show="
                $i18n.locale === 'en' &&
                shop.tags !== undefined &&
                shop.tags !== null &&
                shop.tags.length > 0
              "
            >
              <span
                class="label"
                v-for="(tagsItem, index) in shop.tags"
                :key="index"
                >{{ tagsItem.nameEn }}</span
              >
            </div>
            <div
              class="desc"
              v-show="
                $i18n.locale === 'zh' &&
                (shop.tags === undefined ||
                  shop.tags === null ||
                  shop.tags.length === 0) &&
                shop.introduce !== undefined &&
                shop.introduce !== null &&
                shop.introduce !== ''
              "
            >
              {{ shop.introduce }}
            </div>
            <div
              class="desc"
              v-show="
                $i18n.locale === 'en' &&
                (shop.tags === undefined ||
                  shop.tags === null ||
                  shop.tags.length === 0) &&
                shop.introduceEn !== undefined &&
                shop.introduceEn !== null &&
                shop.introduceEn !== ''
              "
            >
              {{ shop.introduceEn }}
            </div>
            <div class="moneyLabel">
              <div
                v-if="Number(fomatFloat(shop.minPrice, 2)) !== 0"
                class="price"
              >
                <span class="currency"
                  >{{
                    $i18n.locale === "en" && $root.currency === "USD"
                      ? "$"
                      : "¥"
                  }}{{
                    $i18n.locale === "en" && $root.currency === "USD"
                      ? fomatFloat(shop.minPrice, 2)
                      : fomatFloat(shop.minPrice, 2)
                  }}</span
                >
                <!-- <span
                  class="money"
                  v-if="($i18n.locale === 'zh' && $root.currency === 'CNY') && shop.goods.retailPrice"
                >¥{{fomatFloat(shop.goods.retailPrice,2)}}</span>
                <span
                  class="money"
                  v-if="($i18n.locale === 'en' && $root.currency === 'USD') && shop.goods.retailPriceEn"
                >${{fomatFloat(shop.goods.retailPriceEn,2)}}</span>-->
              </div>
              <div v-else class="price">
                <span class="text">{{
                  shop.minPrice === undefined ||
                  shop.minPrice === null
                    ? !userId == ""
                      ? $t("P.Calculating")
                      : $t("P.ViewAfterLogin")
                    : $t("P.NoPricing")
                }}</span>
              </div>
              <div v-if="$i18n.locale === 'zh' && !userId == ''" class="sale">
                <span v-if="shop.saleNum > 10000" class="saleCount"
                  >{{ $t("P.sale") }}
                  {{ fomatFloor(shop.saleNum / 10000, 2) }} 万+</span
                >
                <span v-else class="saleCount"
                  >{{ $t("P.sale") }} {{ shop.saleNum || 0 }}</span
                >
              </div>
              <div v-if="$i18n.locale === 'en' && !userId == ''" class="sale">
                <span v-if="shop.saleNum > 1000" class="saleCount"
                  >{{ fomatFloor(shop.saleNum / 1000, 1) }}K+
                  {{ $t("P.sale") }}</span
                >
                <span v-else class="saleCount"
                  >{{ shop.saleNum || 0 }} {{ $t("P.sale") }}</span
                >
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!---左右列表分布---->
    <div v-if="type === 3" class="recommend-goods-list rl-clear">
      <!-- 左边大图 -->
      <a target="_blank" :href="section_url" class="grid-promo" v-if="$i18n.locale === 'zh' || !section_img2 == true">
        <img :src="section_img" alt class="banner-img" />
      </a>
      <a target="_blank" :href="section_url2" class="grid-promo" v-else>
        <img :src="section_img2" alt class="banner-img" />
      </a>
      <!-- 右边列表 -->
      <div class="grid-list">
        <div class="grid-item" @click="goGoodsDetail(shop, shop.id, shop.goodsType)"
          v-for="(shop, index) in shopList" :key="shop.id" v-if="index < 8">
          <div class="item-tag" v-show="shop.promotionStatus === 1">
            {{ $t("P.SellWell") }}
          </div>
          <div class="item-tag new" v-show="shop.newProductStatus === 1 &&shop.promotionStatus !== 1">
            {{ $t("P.NewProducts") }}
          </div>
          <div class="item-img">
            <img :src="($i18n.locale === 'zh' || !shop.imageUrl1en == true? shop.imageUrl1: shop.imageUrl1en) +'?x-oss-process=image/resize,h_458,l_458'" alt/>
          </div>
          <div class="item-box">
            <div
              v-if="
                (shop.tags === undefined ||
                  shop.tags === null ||
                  shop.tags.length === 0) &&
                (shop.introduce === undefined ||shop.introduce === null ||shop.introduce.length === 0)" class="brand rl-margin-top-xxxs">
              <el-tooltip class="item" effect="dark"
                :content="$i18n.locale === 'zh' || !shop.goodsNameEn == true? shop.goodsName: shop.goodsNameEn" placement="bottom">
                <span>{{$i18n.locale === "zh" || !shop.goodsNameEn == true? shop.goodsName: shop.goodsNameEn}}</span>
              </el-tooltip>
            </div>
            <div v-else class="brand brand1 rl-margin-top-xxxs">
              <el-tooltip
                class="item"
                effect="dark"
                :content="$i18n.locale === 'zh' || !shop.goodsNameEn == true? shop.goodsName: shop.goodsNameEn" placement="bottom">
                <span>{{$i18n.locale === "zh" || !shop.goodsNameEn == true? shop.goodsName: shop.goodsNameEn}}</span>
              </el-tooltip>
            </div>
            <div class="desc"
              v-show="
                $i18n.locale === 'zh' &&
                shop.tags !== undefined &&
                shop.tags !== null &&
                shop.tags.length > 0">
              <span class="label" v-for="(tagsItem, index) in shop.tags" :key="index">{{ tagsItem.name }}</span>
            </div>
            <div class="desc" v-show="$i18n.locale === 'en' &&shop.tags !== undefined &&shop.tags !== null &&shop.tags.length > 0">
              <span class="label" v-for="(tagsItem, index) in shop.tags" :key="index">{{ tagsItem.nameEn }}</span>
            </div>
            <div
              class="desc"
              v-show="
                $i18n.locale === 'zh' &&
                (shop.tags === undefined ||
                  shop.tags === null ||
                  shop.tags.length === 0) &&
                shop.introduce !== undefined &&
                shop.introduce !== null &&
                shop.introduce !== ''
              "
            >
              {{ shop.introduce }}
            </div>
            <div
              class="desc"
              v-show="
                $i18n.locale === 'en' &&
                (shop.tags === undefined ||
                  shop.tags === null ||
                  shop.tags.length === 0) &&
                shop.introduceEn !== undefined &&
                shop.introduceEn !== null &&
                shop.introduceEn !== ''
              "
            >
              {{ shop.introduceEn }}
            </div>
            <!-- 价格 -->
            <div class="moneyLabel">
              <div
                v-if="Number(fomatFloat(shop.minPrice, 2)) !== 0"
                class="price"
              >
                <span class="currency"
                  >{{
                    $i18n.locale === "en" && $root.currency === "USD"
                      ? "$"
                      : "¥"
                  }}{{
                    $i18n.locale === "en" && $root.currency === "USD"
                      ? fomatFloat(shop.minPrice*exchange, 2)
                      : fomatFloat(shop.minPrice, 2)
                  }}</span
                >

              </div>
              <div v-else class="price">
                <span class="text">{{
                  shop.minPrice === undefined ||
                  shop.minPrice === null
                    ? !userId == ""
                      ? $t("P.Calculating")
                      : $t("P.ViewAfterLogin")
                    : $t("P.NoPricing")
                }}</span>
              </div>
              <!-- 销售 -->
              <div v-if="$i18n.locale === 'zh' && !userId == ''" class="sale">
                <span v-if="shop.saleNum > 10000" class="saleCount">{{ $t("P.sale") }}
                  {{ fomatFloor(shop.saleNum / 10000, 2) }} 万+</span>
                <span v-else class="saleCount">{{ $t("P.sale") }} {{ shop.saleNum || 0}}</span>
              </div>
              <div v-if="$i18n.locale === 'en' && !userId == ''" class="sale">
                <span v-if="shop.saleNum > 1000" class="saleCount">{{ fomatFloor(shop.saleNum / 1000, 1) }}K+{{ $t("P.sale") }}</span>
                <span v-else class="saleCount">{{ shop.saleNum || 0 }} {{ $t("P.sale") }}</span>
              </div>
            </div>
          </div>
        </div>
       
      </div>
    </div>
    <!---列表分布---->
    <div v-if="type === 4" class="recommend-goods-list rl-clear four">
      <a target="_blank" :href="section_url" class="good-item" v-if="$i18n.locale === 'zh' || !section_img2 == true">
        <img :src="section_img" alt class="good-img" />
      </a>
      <a target="_blank" :href="section_url2" class="good-item" v-else>
        <img :src="section_img2" alt class="good-img" />
      </a>
      <div
        @click="goGoodsDetail(shop, shop.id, shop.goodsType)" v-for="(shop, index) in shopList" :key="shop.id"
        class="good-item" v-if="index < 9">
        <div class="content" v-if="shop">
          <div class="item-tag" v-show="shop.promotionStatus === 1">
            {{ $t("P.SellWell") }}
          </div>
          <div
            class="item-tag new"
            v-show="
              shop.newProductStatus === 1 &&
              shop.promotionStatus !== 1
            "
          >
            {{ $t("P.NewProducts") }}
          </div>
          <div class="img">
            <img
              :src="
                ($i18n.locale === 'zh' || !shop.imageUrl1en == true
                  ? shop.imageUrl1
                  : shop.imageUrl1en) +
                '?x-oss-process=image/resize,h_458,l_458'
              "
              alt
            />
          </div>
          <div class="item-info">
            <div v-if="$i18n.locale === 'zh'">
              <div
                v-if="
                  (shop.tags === undefined ||
                    shop.tags === null ||
                    shop.tags.length === 0) &&
                  (shop.introduce === undefined ||
                    shop.introduce === null ||
                    shop.introduce.length === 0)
                "
                class="brand rl-margin-top-xxxs"
              >
                <el-tooltip
                  class="item"
                  effect="dark"
                  :content="
                    $i18n.locale === 'zh' || !shop.goodsNameEn == true
                      ? shop.goodsName
                      : shop.goodsNameEn
                  "
                  placement="bottom"
                >
                  <span>{{
                    $i18n.locale === "zh" || !shop.goodsNameEn == true
                      ? shop.goodsName
                      : shop.goodsNameEn
                  }}</span>
                </el-tooltip>
              </div>
              <div v-else class="brand brand1 rl-margin-top-xxxs">
                <el-tooltip
                  class="item"
                  effect="dark"
                  :content="
                    $i18n.locale === 'zh' || !shop.goodsNameEn == true
                      ? shop.goodsName
                      : shop.goodsNameEn
                  "
                  placement="bottom"
                >
                  <span>{{
                    $i18n.locale === "zh" || !shop.goodsNameEn == true
                      ? shop.goodsName
                      : shop.goodsNameEn
                  }}</span>
                </el-tooltip>
              </div>
              <div
                class="word rl-tc"
                v-show="
                  $i18n.locale === 'zh' &&
                  shop.tags !== undefined &&
                  shop.tags !== null &&
                  shop.tags.length > 0
                "
              >
                <span
                  class="label"
                  v-for="(tagsItem, index) in shop.tags"
                  :key="index"
                  >{{ tagsItem.name }}</span
                >
              </div>
              <div
                class="word rl-tc"
                v-show="
                  $i18n.locale === 'zh' &&
                  (shop.tags === undefined ||
                    shop.tags === null ||
                    shop.tags.length === 0) &&
                  shop.introduce !== undefined &&
                  shop.introduce !== null &&
                  shop.introduce !== ''
                "
              >
                <span>{{ shop.introduce }}</span>
              </div>
            </div>
            <div v-else>
              <div
                v-if="
                  (shop.tags === undefined ||
                    shop.tags === null ||
                    shop.tags.length === 0) &&
                  (shop.introduce === undefined ||
                    shop.introduce === null ||
                    shop.introduce.length === 0)
                "
                class="brand rl-margin-top-xxxs"
              >
                <el-tooltip
                  class="item"
                  effect="dark"
                  :content="
                    $i18n.locale === 'zh' || !shop.goodsNameEn == true
                      ? shop.goodsName
                      : shop.goodsNameEn
                  "
                  placement="bottom"
                >
                  <span>{{
                    $i18n.locale === "zh" || !shop.goodsNameEn == true
                      ? shop.goodsName
                      : shop.goodsNameEn
                  }}</span>
                </el-tooltip>
              </div>
              <div v-else class="brand brand1 rl-margin-top-xxxs">
                <el-tooltip
                  class="item"
                  effect="dark"
                  :content="
                    $i18n.locale === 'zh' || !shop.goodsNameEn == true
                      ? shop.goodsName
                      : shop.goodsNameEn
                  "
                  placement="bottom"
                >
                  <span>{{
                    $i18n.locale === "zh" || !shop.goodsNameEn == true
                      ? shop.goodsName
                      : shop.goodsNameEn
                  }}</span>
                </el-tooltip>
              </div>
              <div
                class="word rl-tc"
                v-show="
                  $i18n.locale === 'en' &&
                  shop.tags !== undefined &&
                  shop.tags !== null &&
                  shop.tags.length > 0
                "
              >
                <span
                  class="label"
                  v-for="(tagsItem, index) in shop.tags"
                  :key="index"
                  >{{ tagsItem.nameEn }}</span
                >
              </div>
              <div
                class="word rl-tc"
                v-show="
                  $i18n.locale === 'en' &&
                  (shop.tags === undefined ||
                    shop.tags === null ||
                    shop.tags.length === 0) &&
                  shop.introduceEn !== undefined &&
                  shop.introduceEn !== null &&
                  shop.introduceEn !== ''
                "
              >
                <span>{{ shop.introduceEn }}</span>
              </div>
            </div>
            <div
              class="moneyLabel"
              v-if="
                ((freezeStatus !== undefined &&
                  freezeStatus !== null &&
                  freezeStatus === '1') ||
                  freezeStatus === null) &&
                ((capitalStatus !== undefined &&
                  capitalStatus !== null &&
                  capitalStatus === '2') ||
                  capitalStatus === null)
              "
            >
              <div
                v-if="Number(fomatFloat(shop.minPrice, 2)) !== 0"
                style="float: left"
              >
                <span class="currency"
                  >{{
                    $i18n.locale === "en" && $root.currency === "USD"
                      ? "$"
                      : "¥"
                  }}{{
                    $i18n.locale === "en" && $root.currency === "USD"
                      ? fomatFloat(shop.minPrice*exchange, 2)
                      : fomatFloat(shop.minPrice, 2)
                  }}</span
                >

              </div>
              <div style="float: left" v-else>
                <span class="money1">{{
                  shop.minPrice === undefined ||
                  shop.minPrice === null
                    ? !userId == ""
                      ? $t("P.Calculating")
                      : $t("P.ViewAfterLogin")
                    : $t("P.NoPricing")
                }}</span>
              </div>
              <div v-if="$i18n.locale === 'zh' && !userId == ''" style="float: right">
                <span v-if="shop.saleNum > 10000" class="saleCount">{{ $t("P.sale") }}
                  {{ fomatFloor(shop.saleNum / 10000, 2) }} 万+</span>
                <span v-else class="saleCount">{{ $t("P.sale") }} {{ shop.saleNum || 0 }}</span>
              </div>
              <div v-if="$i18n.locale === 'en' && !userId == ''" style="float: right">
                <span v-if="shop.saleNum > 1000" class="saleCount">{{ fomatFloor(shop.saleNum / 1000, 1) }}K+
                  {{ $t("P.sale") }}</span>
                <span v-else class="saleCount">{{ shop.saleNum || 0 }} {{ $t("P.sale") }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!---新品-y--->
    <div v-if="type === 5" class="recommend-goods-list rl-clear new">
      <div @click="goGoodsDetail(shop, shop.id, shop.goodsType)" v-for="(shop, index) in newshopList" :key="shop.id" class="good-item" v-if="index < 30">
        <div class="content" v-if="shop">
          <!-- <div class="item-tag" v-show="shop.newProductStatus === 1 && shop.promotionStatus !== 1">{{$t('P.NewProducts')}}</div> -->
          <div class="item-tag">{{ $t("P.NewProducts") }}</div>
          <div class="img" :style="'background-color: ' + shop.bgColor">
            <img :src="($i18n.locale === 'zh' || !shop.imageUrl1en == true? shop.imageUrl1: shop.imageUrl1en) +'?x-oss-process=image/resize,h_458,l_458'" alt/>
          </div>
          <div class="item-info">
            <div v-if="$i18n.locale === 'zh'">
              <div
                v-if="(shop.tags === undefined ||shop.tags === null ||shop.tags.length === 0) &&
                  (shop.introduce === undefined ||shop.introduce === null ||shop.introduce.length === 0)" class="brand rl-margin-top-xxxs">
                <el-tooltip class="item" effect="dark"
                  :content="$i18n.locale === 'zh' || !shop.goodsNameEn == true? shop.goodsName: shop.goodsNameEn" placement="bottom">
                  <span>{{$i18n.locale === "zh" || !shop.goodsNameEn == true? shop.goodsName: shop.goodsNameEn}}</span>
                </el-tooltip>
              </div>
              <div v-else class="brand brand1 rl-margin-top-xxxs">
                <el-tooltip class="item" effect="dark"
                  :content="$i18n.locale === 'zh' || !shop.goodsNameEn == true? shop.goodsName: shop.goodsNameEn" placement="bottom">
                  <span>{{$i18n.locale === "zh" || !shop.goodsNameEn == true? shop.goodsName: shop.goodsNameEn}}</span>
                </el-tooltip>
              </div>
              <!-- 标签 -->
              <div class="word rl-tc" v-show="$i18n.locale === 'zh' &&shop.tags !== undefined &&shop.tags !== null &&shop.tags.length > 0">
                <span class="label" v-for="(tagsItem, index) in shop.tags" :key="index">{{ tagsItem.name }}</span>
              </div>
              <!-- 简介 -->
              <div
                class="word rl-tc"
                v-show="$i18n.locale === 'zh' &&(shop.tags === undefined ||
                    shop.tags === null ||
                    shop.tags.length === 0) &&
                  shop.introduce !== undefined &&
                  shop.introduce !== null &&
                  shop.introduce !== ''">
                <span>{{ shop.introduce }}</span>
              </div>
            </div>
            <div v-else>
              <div
                v-if="
                  (shop.tags === undefined ||
                    shop.tags === null ||
                    shop.tags.length === 0) &&
                  (shop.introduce === undefined ||
                    shop.introduce === null ||
                    shop.introduce.length === 0)
                "
                class="brand rl-margin-top-xxxs">
                <el-tooltip
                  class="item"
                  effect="dark"
                  :content="$i18n.locale === 'zh' || !shop.goodsNameEn == true? shop.goodsName: shop.goodsNameEn" placement="bottom">
                  <span>{{$i18n.locale === "zh" || !shop.goodsNameEn == true? shop.goodsName: shop.goodsNameEn}}</span>
                </el-tooltip>
              </div>
              <div v-else class="brand brand1 rl-margin-top-xxxs">
                <el-tooltip
                  class="item"
                  effect="dark"
                  :content="
                    $i18n.locale === 'zh' || !shop.goodsNameEn == true
                      ? shop.goodsName
                      : shop.goodsNameEn
                  "
                  placement="bottom"
                >
                  <span>{{
                    $i18n.locale === "zh" || !shop.goodsNameEn == true
                      ? shop.goodsName
                      : shop.goodsNameEn
                  }}</span>
                </el-tooltip>
              </div>
              <div
                class="word rl-tc"
                v-show="
                  $i18n.locale === 'en' &&
                  shop.tags !== undefined &&
                  shop.tags !== null &&
                  shop.tags.length > 0
                "
              >
                <span
                  class="label"
                  v-for="(tagsItem, index) in shop.tags"
                  :key="index"
                  >{{ tagsItem.name }}</span
                >
              </div>
              <div
                class="word rl-tc"
                v-show="
                  $i18n.locale === 'en' &&
                  (shop.tags === undefined ||
                    shop.tags === null ||
                    shop.tags.length === 0) &&
                  shop.introduceEn !== undefined &&
                  shop.introduceEn !== null &&
                  shop.introduceEn !== ''
                "
              >
                <span>{{ shop.introduceEn }}</span>
              </div>
            </div>
            <div class="moneyLabel">
              <!-- 价格 -->
              <div v-if="Number(fomatFloat(shop.minPrice, 2)) !== 0" style="float: left">
                <span class="currency">
                  {{$i18n.locale === "en" && $root.currency === "USD"? "$": "¥"}}{{$i18n.locale === "en" && $root.currency === "USD"? fomatFloat(shop.minPrice*exchange, 2): fomatFloat(shop.minPrice, 2)}}
                </span>
              </div>
              <div style="float: left" v-else>
                <span class="money1">{{shop.minPrice === undefined || shop.minPrice === null? !userId == ""? $t("P.Calculating"): $t("P.ViewAfterLogin"): $t("P.NoPricing")}}</span>
              </div>
              <!-- 销量 -->
              <div v-if="$i18n.locale === 'zh' && !userId == ''" style="float: right">
                <span v-if="shop.saleNum > 10000" class="saleCount">{{ $t("P.sale") }}
                  {{ fomatFloor(shop.saleNum / 10000, 2) }} 万+</span>
                <span v-else class="saleCount">{{ $t("P.sale") }} {{ shop.saleNum || 0 }}</span>
              </div>
              <div v-if="$i18n.locale === 'en' && !userId == ''" style="float: right">
                <span v-if="shop.saleNum > 1000" class="saleCount">{{ fomatFloor(shop.saleNum / 1000, 1) }}K+
                  {{ $t("P.sale") }}</span>
                <span v-else class="saleCount">{{ shop.saleNum || 0 }} {{ $t("P.sale") }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div @click="goGoodsDetail(shop, shop.id, shop.goodsType)"  v-for="(shop, index) in newshopList" :key="'add' + shop.id" class="good-item" :value="'add' + shop.id"
        v-if=" newshopList.length > 5 &&newshopList.length % 5 !== 0 && index < 5 - (newshopList.length % 5)">
        <div class="content" v-if="shop">
          <!-- <div class="item-tag" v-show="shop.newProductStatus === 1 && shop.promotionStatus !== 1">{{$t('P.NewProducts')}}</div> -->
          <div class="item-tag">{{ $t("P.NewProducts") }}</div>
          <div class="img" :style="'background-color: ' + shop.bgColor">
            <img :src="($i18n.locale === 'zh' || !shop.imageUrl1en == true? shop.imageUrl1: shop.imageUrl1en) +'?x-oss-process=image/resize,h_458,l_458'" alt/>
          </div>
          <div class="item-info">
            <div v-if="$i18n.locale === 'zh'">
              <!-- 商品名称 -->
              <div class="brand brand1 rl-margin-top-xxxs">
                <el-tooltip class="item" effect="dark"
                  :content="$i18n.locale === 'zh' || !shop.goodsNameEn == true? shop.goodsName: shop.goodsNameEn" placement="bottom">
                  <span>{{$i18n.locale === "zh" || !shop.goodsNameEn == true? shop.goodsName: shop.goodsNameEn}}</span>
                </el-tooltip>
              </div>
              <!-- 标签 -->
              <div class="word rl-tc" v-show="$i18n.locale === 'zh' &&shop.tags !== undefined &&shop.tags.length > 0">
                <span class="label" v-for="(tagItem, index) in shop.tags" :key="index">{{ tagItem.name }}</span>
              </div>
              <!-- 商品简介- -->
              <div class="word rl-tc" v-show="$i18n.locale === 'zh'&&shop.introduce !== undefined &&shop.introduce !== null &&shop.introduce !== ''">
                <span>{{ shop.introduce }}</span>
              </div>
            </div>
            <!-- 英文版 -->
            <div v-else>  
              <div  class="brand brand1 rl-margin-top-xxxs">
                <el-tooltip class="item" effect="dark"
                  :content="$i18n.locale === 'zh' || !shop.goodsNameEn == true? shop.goodsName: shop.goodsNameEn" placement="bottom">
                  <span>{{$i18n.locale === "zh" || !shop.goodsNameEn == true? shop.goodsName: shop.goodsNameEn}}</span>
                </el-tooltip>
              </div>
              <div class="word rl-tc" v-show="$i18n.locale === 'en' &&shop.tags !== undefined &&shop.tags !== null &&shop.tags.length > 0">
                <span class="label"  v-for="(labelNameEns, index) in shop.labelNameEns" :key="index">{{ labelNameEns }}</span>
              </div>
              <div
                class="word rl-tc"
                v-show="
                  $i18n.locale === 'en' &&
                  shop.introduceEn !== undefined &&
                  shop.introduceEn !== null &&
                  shop.introduceEn !== ''">
                <span>{{ shop.introduceEn }}</span>
              </div>
            </div>
            <div class="moneyLabel">
              <div v-if="Number(fomatFloat(shop.salePrice, 2)) !== 0" style="float: left">
                <span class="currency"
                  >{{$i18n.locale === "en" && $root.currency === "USD"? "$": "¥"}}{{
                    $i18n.locale === "en" && $root.currency === "USD"
                      ? fomatFloat(shop.salePriceEn, 2)
                      : fomatFloat(shop.salePrice, 2)}}</span>
                <!-- <span
                  class="money"
                  v-if="($i18n.locale === 'zh' && $root.currency === 'CNY') && shop.retailPrice"
                >¥{{fomatFloat(shop.retailPrice,2)}}</span>
                <span
                  class="money"
                  v-if="($i18n.locale === 'en' && $root.currency === 'USD') && shop.retailPriceEn"
                >${{fomatFloat(shop.retailPriceEn,2)}}</span>-->
              </div>
              <div style="float: left" v-else>
                <span class="money1">{{
                  shop.salePrice === undefined || shop.salePrice === null
                    ? !userId == ""
                      ? $t("P.Calculating")
                      : $t("P.ViewAfterLogin")
                    : $t("P.NoPricing")
                }}</span>
              </div>
              <div
                v-if="$i18n.locale === 'zh' && !userId == ''"
                style="float: right"
              >
                <span v-if="shop.saleNum > 10000" class="saleCount"
                  >{{ $t("P.sale") }}
                  {{ fomatFloor(shop.saleNum / 10000, 2) }} 万+</span
                >
                <span v-else class="saleCount"
                  >{{ $t("P.sale") }} {{ shop.saleNum || 0 }}</span
                >
              </div>
              <div
                v-if="$i18n.locale === 'en' && !userId == ''"
                style="float: right"
              >
                <span v-if="shop.saleNum > 1000" class="saleCount"
                  >{{ fomatFloor(shop.saleNum / 1000, 1) }}K+
                  {{ $t("P.sale") }}</span
                >
                <span v-else class="saleCount"
                  >{{ shop.saleNum || 0 }} {{ $t("P.sale") }}</span
                >
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import GD from "@/assets/js/globalData";
import { fomatFloat, fomatFloor } from "@/assets/js/common.js";
// liu
import {sectionGoodsList,priceGoodsList} from '@/apiService/api'
export default {
  name: "good",
  props: {
    newshopList: {
      type: Array,
      default: null,
    },
    itemId: {
      type: Number,
      default: 0,
    },
    section_index: {
      type: Number,
      default: 0,
    },
    section_img: {
      type: String,
      default: "",
    },
    section_img1: {
      type: String,
      default: "",
    },
    section_img2: {
      type: String,
      default: "",
    },
    section_img3: {
      type: String,
      default: "",
    },
    section_url: {
      type: String,
      default: "",
    },
    section_url1: {
      type: String,
      default: "",
    },
    section_url2: {
      type: String,
      default: "",
    },
    section_url3: {
      type: String,
      default: "",
    },
    section_length: {
      type: Number,
      default: 0,
    },
    showLoading: {
      type: Boolean,
    },
    type: {
      type: Number,
      default: 0,
    },
    typeEn: {
      type: Number,
      default: 0,
    },
  },
  data() {
    return {
      shopList: [], // 首页商品名称,
      pageNo: 1,
      pageSize: 10,
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: "zh-RMB", // 语种
      userId: "",
      freezeStatus: undefined,
      capitalStatus: undefined,
      exchange:1, //汇率
    };
  },
  methods: {
    fomatFloor(val, n) {
      return fomatFloor(val, n);
    },
    fomatFloat(val, n) {
      return fomatFloat(val, n);
    },
    fLangChange(value) {
      window.localStorage.setItem("bLang", value);
      this.$i18n.locale = value.split("_")[0];
    },
    // 根据ids查询价格--y
    getPriceData(goodsIdList){
       console.log('商品分类列表：9999',this.shopList);
         priceGoodsList({goodsIds:goodsIdList}).then((res) => {
            if ( res.success ) {
              let list=JSON.parse(JSON.stringify(this.shopList));
              list.forEach((item) => {
                for (let i = 0; i < res.data.length; i++) {
                  if (item.id === res.data[i].goodsId) {
                    item.minPrice = res.data[i].minPrice; 
                    item.maxPrice = res.data[i].maxPrice;
                  }
                }
              });
              this.shopList=list;
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
    shopData() {
      // 商品分类
      let goodsIdList = [];
      let goodsIds = "";
        let params={
            page: this.pageNo,
            size: this.pageSize,
            sectionId: this.itemId,
        };
        sectionGoodsList(params).then((res) => {
          if (this.section_index === this.section_length - 1) {
            this.$emit("input", false);
          }
          if (res.success) {
            this.shopList = res.data.list;
            if (this.shopList.length > 0) {
              this.shopList.forEach((item) => {
                goodsIdList.push(item.id);
                goodsIds += item.id + ",";
              });
              
              if (this.userId !== ""&&this.userId !=undefined&&this.userId !=null &&this.freezeStatus == 1 &&this.capitalStatus == 2 &&goodsIdList.length > 0) {
                 // 用户已登录且未冻结合作中的分销商获取价格
                this.getPriceData(goodsIdList);
               
              }

              
            }
          }
        });
    },
    // 去商品详情
    goGoodsDetail(item, id, goodsType) {
      let { href } = this.$router.resolve({
        path: "/ShopDetail",
        query: {
          good_id: id,
          activityType: item.promotion ? 1 : "",
          goodsType: goodsType,
          accessType: 0,
        },
      });
      window.open(href, "_blank");
    },
  },
  created() {
    this.exchange=Number( localStorage.getItem('exchange')); 
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem("bLang")
      ? window.localStorage.getItem("bLang")
      : "zh-RMB";
  },
  mounted() {
    this.userId = window.localStorage.getItem("userId");
    this.capitalStatus = window.localStorage.getItem("capitalStatus");
    this.freezeStatus = window.localStorage.getItem("freezeStatus");
    this.shopData();
  },
};
</script>
<style  scoped="scoped" lang='less'>
@import url("../assets/less/variable.less");
.good {
  .recommend-goods-list {
    width: 100%;
    font-size: 0;
    .nav-top {
      width: 100%;
      font-size: 0;
      .nav-item {
        display: inline-block;
        width: 594px;
        height: 320px;
        margin-bottom: 12px;
        border-radius: 16px;
        background-color: @proBg;
        cursor: pointer;
        overflow: hidden;
        img {
          display: inline-block;
          width: 100%;
          height: 100%;
          border-radius: 16px;
        }
        &:hover {
          box-shadow: 0 7px 15px rgba(0, 0, 0, 0.1);
          transform: translate3d(0, -2px, 0);
        }
        &:last-child {
          margin-left: 12px;
        }
      }
    }
    .grid-promo {
      display: inline-block;
      width: 230px;
      height: 652px;
      border-radius: 16px;
      background-color: @proBg;
      overflow: hidden;
      cursor: pointer;
      &:hover {
        box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
        transform: translate3d(0, -2px, 0);
      }
      .banner-img {
        display: inline-block;
        width: 100%;
        height: 100%;
      }
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
        border-radius: 16px;
        background-color: @proBg;
        vertical-align: top;
        overflow: hidden;
        cursor: pointer;
        float: left;
        .grid-hot-img {
          display: inline-block;
          width: 100%;
          height: 100%;
        }
        &:hover {
          box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
          transform: translate3d(0, -2px, 0);
        }
      }
      .grid-item {
        position: relative;
        width: 230px;
        height: 320px;
        margin: 0 0 12px 12px;
        box-sizing: border-box;
        background-color: @proBg;
        border-radius: 16px;
        overflow: hidden;
        cursor: pointer;
        float: left;
        &:first-child,
        &:nth-child(5) {
          margin-left: 14px;
        }
        &:hover {
          box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
          transform: translate3d(0, -2px, 0);
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
          z-index: 99;
          &.new {
            background-color: @blueLabel;
          }
        }
        .item-img {
          position: relative;
          width: 230px;
          height: 230px;
          overflow: hidden;
          border-radius: 16px 16px 0 0;
          img {
            width: 100%;
            height: 100%;
          }
        }
        .item-box {
          display: block;
          width: 100%;
          height: 90px;
          padding: 8px 15px 11px;
          box-sizing: border-box;
          font-size: 0;
          .title {
            display: inline-block;
            width: 100%;
            font-size: 14px;
            height: 46px;
            line-height: 22px;
            color: @black;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
            border-radius: 0;
          }
          .title1 {
            display: inline-block;
            width: 100%;
            font-size: 14px;
            line-height: 22px;
            color: @black;
            text-overflow: ellipsis;
            overflow: hidden;
            white-space: nowrap;
            border-radius: 0;
          }
          .desc {
            display: inline-block;
            width: 100%;
            font-size: 12px;
            line-height: 20px;
            color: @lightGray;
            text-overflow: ellipsis;
            overflow: hidden;
            white-space: nowrap;
            border-radius: 0;
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
          }
          .moneyLabel {
            position: absolute;
            width: calc(100% - 30px);
            left: 15px;
            bottom: 11px;
            display: block;
            .text {
              display: inline-block;
              font-size: 14px;
              color: @red;
            }
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
                color: @gray;
                text-decoration: line-through;
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
    &.one {
      .good-item:nth-child(6n) {
        margin-right: 0;
      }
    }
    &.four {
      .good-item:nth-child(5n) {
        margin-right: 0;
      }
    }
    &.new {
      .good-item {
        &:nth-child(5n) {
          margin-right: 42px;
        }
      }
    }
    .label + .label {
      margin-left: 4px;
    }
    .good-item {
      width: 230px;
      height: 320px;
      margin-bottom: 12px;
      margin-right: 12px;
      float: left;
      cursor: pointer;
      border-radius: 16px;
      overflow: hidden;
      transition: all 0.2s linear;
      .good-img {
        display: inline-block;
        width: 230px;
        height: 320px;
      }
      .content {
        height: 320px;
        width: 230px;
        border-radius: 16px;
        background-color: @proBg;
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
          background-color: @blueLabel;
          border-radius: 0 0 4px 4px;
          z-index: 99;
        }
        .img {
          position: relative;
          width: 230px;
          height: 230px;
          display: table-cell; /*主要是这个属性*/
          vertical-align: middle;
          text-align: center;
          overflow: hidden;
          border-radius: 16px 16px 0 0;
          img {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate3d(-50%, -50%, 0);
            width: 100%;
            height: auto;
          }
        }
        .item-info {
          display: inline-block;
          width: 100%;
          height: 90px;
          padding: 6px 15px 8px;
          box-sizing: border-box;
          overflow: hidden;
          &:hover {
            border-radius: 16px;
          }
        }
        .imgLable {
          width: 22px;
          height: 22px;
          padding: 0;
          margin-left: -5px;
          margin-top: -3px;
          vertical-align: text-top;
        }
        .imgLable1 {
          width: 22px;
          height: 22px;
          padding: 0px;
          margin-left: -3px;
          margin-top: -2px;
          vertical-align: text-top;
        }
        .word {
          width: 100%;
          float: left;
          line-height: 22px;
          text-align: left;
          box-sizing: border-box;
          font-size: 12px;
          font-family: FZHei-B01;
          font-weight: 400;
          color: @gray;
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
        }
        .moneyLabel {
          position: absolute;
          left: 15px;
          bottom: 8px;
          width: calc(100% - 30px);
          float: left;
          line-height: 22px;
          text-align: left;
          vertical-align: bottom;
        }
        .money {
          margin-left: 6px;
          font-size: 12px;
          font-family: Myriad Pro;
          color: @gray;
          text-decoration: line-through;
        }
        .money1 {
          font-size: 14px;
          font-family: FZHei-B01;
          font-weight: 400;
          color: @red;
        }
        .currency {
          font-size: 16px;
          font-family: Myriad Pro;
          font-weight: 550;
          color: @red;
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
        border-radius: 16px;
      }
    }
    .good-item:hover {
      box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
      transform: translate3d(0, -2px, 0);
    }
    .good-item:nth-child(1),
    .good-item:nth-child(6) {
      margin-left: 0;
    }
  }
  /*.home-list{
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
    }*/
}
</style>
