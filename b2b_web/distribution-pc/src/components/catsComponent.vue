<template>
  <div>
    <div
      v-for="(shopItem, shopIndex) in shopCartData"
      :key="shopItem.goodsType"
    >
      <div
        class="rl-clear"
        @click="selectAllGou(shopItem.goodsType, shopIndex)"
      >
        <div class="spe rl-fl">
          <span
            :class="{ gou: shopItem.allCkeck }"
            class="cursor-pointer"
          ></span>
        </div>
        <div class="rl-fl table-name">
          <span class="cursor-pointer">{{
            orderTypes(shopItem.goodsType)
          }}</span>
          <span v-if="shopItem.goodsType === 1 && onWayAttendEventFlag === 0"
            >({{ $t("ShopCarts.NoAvailableA") }})</span
          >
          <!--在途商品不参与活动-->
          <!-- <span v-if="shopItem.goodsType === 2 ">({{ $t("ShopCarts.NoAvailableC") }})</span>定制商品不参与活动 -->
          <span class="tips" v-if="shopItem.goodsType === 2"
            >(如定制总数超过10个，请先与客服联系确认，否则存在色差问题不予退货。客服微信号：
            W-666357。)</span
          >
        </div>
      </div>
      <div class="shopcar-table rl-bg-white rl-margin-bottom-default">
        <!-- 表头 -->
        <el-table
          border
          header-row-class-name="header-row"
          :cell-style="{ 'text-align': 'center' }"
          :header-cell-style="getRowClass"
          class="activity-el-table rl-tc"
        >
          <!-- checkout -->
          <el-table-column label width="50"></el-table-column>
          <!-- 图片 -->
          <el-table-column
            :label="$t('ShopCarts.Picture')"
            :min-width="60"
          ></el-table-column>
          <!-- 货品编码 -->
          <el-table-column
            :label="$t('ShopCarts.ItemNo')"
            :min-width="80"
          ></el-table-column>
          <!-- 货品名称 -->
          <el-table-column
            :label="$t('ShopCarts.ItemName')"
            :min-width="130"
          ></el-table-column>
          <!-- 规格 -->
          <el-table-column
            :label="$t('ShopCarts.Spe')"
            width="98"
            v-if="shopItem.goodsType == 1"
          ></el-table-column>
          <!-- 材质 -->
          <el-table-column
            v-if="shopItem.goodsType == 2"
            :label="$t('ShopCarts.Material')"
            width="98"
          ></el-table-column>
          <!-- 材质 -->
          <el-table-column
            v-if="shopItem.goodsType == 2"
            label="机型"
            width="98"
          ></el-table-column>
          <!-- 颜色 -->
          <el-table-column
            v-if="shopItem.goodsType == 1"
            :label="$t('ShopCarts.Colors')"
            width="70"
          ></el-table-column>
          <!-- 会员价 -->
          <el-table-column label="会员价" width="110"></el-table-column>
          <!-- 在库/在途数量 -->
          <el-table-column
            :label="$t('ShopCarts.AllQuantity')"
            width="180"
            v-if="shopItem.goodsType === 1"
          ></el-table-column>
          <!-- 数量 -->
          <el-table-column
            :label="$t('ShopCarts.Quantity')"
            width="180"
            v-if="shopItem.goodsType !== 1 && groupFlag !== 'group'"
          ></el-table-column>
          <!-- 起拼量/限购量 -->
          <el-table-column
            :label="$t('ShopCarts.AllQuantity1')"
            width="180"
            v-if="groupFlag === 'group'"
          ></el-table-column>
          <!-- 合计 -->
          <el-table-column
            :label="$t('ShopCarts.Total')"
            width="100"
          ></el-table-column>
          <!-- 操作 -->
          <el-table-column
            :label="$t('ShopCarts.Operation')"
            :min-width="90"
          ></el-table-column>
        </el-table>
        <div v-for="listItem in shopItem.list" :key="listItem.goodsPromotionId">
          <!--订单活动放最前面-->
          <div
            v-if="listItem.goodsPromotionId != 'no' || listItem.groupSeckillId"
          >
            <div class="activity app-flex app-justify-content-start">
              <div
                @click="lookRuleDetail(listItem.children[0])"
                class="
                  btn
                  rl-text-white rl-text-xxss rl-margin-right-default rl-tc
                  app-flex app-align-items-flex-center
                  cursor-pointer
                "
              >
                <span class="rl-bg-orange-mm">{{ listItem.ruleName }}</span>
              </div>
              <div class="item" v-if="listItem.rules">
                <div
                  v-for="(conditionsItems, index) in listItem.rules.conditions"
                  :key="index"
                >
                  <span class="rl-margin-right-default"
                    >{{ $t("ShopCarts.Condition") }}{{ index + 1 }}:</span
                  >
                  <span>{{ conditionsItems.conditionName }}</span>
                  <!-- <span v-if="items.meetMark === true" class="chenked"></span> -->
                </div>
              </div>
              <div
                class="checkgifts-btn"
                v-if="listItem.reduceOrPresent == 2"
                @click="
                  getPresentsData(
                    listItem.reduceOrPresentId,
                    listItem.rules.conditions,
                    listItem
                  )
                "
              >
                查看赠品
              </div>
              <div
                class="rl-text-orange-sm"
                style="margin-left: 30px; cursor: pointer"
                @click="
                  pieceTogether(
                    listItem.goodsPromotionId
                      ? listItem.goodsPromotionId
                      : listItem.groupSeckillId,
                    listItem.ruleName,
                    listItem.rules.conditions
                  )
                "
              >
                去凑单 >
              </div>
            </div>
          </div>

          <!-- 表格内容 -->
          <el-table
            :data="listItem.children"
            :show-header="showHeader"
            border
            header-row-class-name="header-row"
            :header-cell-style="getRowClass"
            class="activity-el-table rl-tc"
          >
            <!-- 标志，单选 -->
            <el-table-column label width="50">
              <template slot-scope="scope">
                <!-- 失效 -->
                <div v-if="scope.row.openFlag == 0" class="song">
                  <h1
                    class="
                      rl-bg-orange-mm
                      rl-text-white
                      rl-text-xxss
                      rl-margin-zero
                      rl-tc
                    "
                  >
                    {{ $t("ShopCarts.Expired") }}
                  </h1>
                </div>
                <div
                  v-if="scope.row.itemType == 2 && scope.row.openFlag == 1"
                  class="song"
                >
                  <h1
                    class="
                      rl-bg-orange-mm
                      rl-text-white
                      rl-text-xxss
                      rl-margin-zero
                      rl-tc
                    "
                  >
                    赠品
                  </h1>
                </div>

                <!-- 标品判断库存，定制判断是否缺货  openFlag商品是否有效 是否可视visible-->
                <div
                  v-else-if="
                    ((scope.row.goodsType == 1
                      ? scope.row.onWayUsableCount +
                          scope.row.inStockUsableCount <
                        scope.row.count * scope.row.choosedBoxNum
                      : scope.row.diyUnderStockFlag == 0) &&
                    scope.row.openFlag == 1 &&
                    scope.row.mtoFlag !== 1) ||
                    scope.row.visible !== 1
                  "
                  class="song"
                >
                  <h1
                    class="
                      rl-bg-orange-mm
                      rl-text-white
                      rl-text-xxss
                      rl-margin-zero
                      rl-tc
                    "
                  >
                    {{ scope.row.visible !== 1 ? '已下架' : '缺货' }}
                  </h1>
                </div>
                <div
                  v-else
                  @click="chooseGou(scope.row, shopIndex, listItem)"
                  class="spe"
                >
                  <span
                    class="cursor-pointer"
                    :class="{ gou: scope.row.isCheck }"
                  ></span>
                </div>
              </template>
            </el-table-column>
            <!-- 图片 -->
            <el-table-column label="图片" :min-width="60">
              <template slot-scope="scope">
                <!-- 可视 -->
                <div
                  v-if="scope.row.visible === 1"
                  @click="goDoodsDetail(scope.row.goodsId, shopItem)"
                  class="shop-img cursor-pointer"
                >
                  <img
                    width="100%"
                    style="object-fit: contain"
                    :src="
                      scope.row.diy
                        ? scope.row.diy.previewImage
                        : scope.row.imageUrl
                    "
                    alt
                  />
                </div>
                <!-- 不可视 -->
                <div
                  v-else
                  class="shop-img"
                >
                  <img
                    width="100%"
                    style="object-fit: contain"
                    :src="
                      scope.row.diy
                        ? scope.row.diy.previewImage
                        : scope.row.imageUrl
                    "
                    alt
                  />
                </div>
              </template>
            </el-table-column>
            <el-table-column
              label="货品编码"
              prop="itemCode"
              :min-width="80"
            ></el-table-column>
            <el-table-column label="货品名称" :min-width="130">
              <template slot-scope="scope">
                <div
                  class="rl-text-xxs"
                  v-show="
                    $i18n.locale === 'zh' || !scope.row.itemNameEn == true
                  "
                >
                  {{ scope.row.itemName }}
                </div>
                <div class="rl-text-xxs" v-show="$i18n.locale === 'en'">
                  {{ scope.row.itemNameEn }}
                </div>
              </template>
            </el-table-column>
            <el-table-column
              label="规格"
              width="98"
              v-if="shopItem.goodsType == 1"
            >
              <template slot-scope="scope">
                <div class="rl-text-xxs" v-show="$i18n.locale === 'zh'">
                  {{ scope.row.specsName }}
                </div>
                <div class="rl-text-xxs" v-show="$i18n.locale === 'en'">
                  {{ scope.row.specsNameEn }}
                </div>
              </template>
            </el-table-column>
            <el-table-column
              v-if="shopItem.goodsType == 2"
              label="材质"
              width="98"
            >
              <template slot-scope="scope">
                <div class="rl-text-xxs">
                  {{ scope.row.diy ? scope.row.diy.materialName : "" }}
                </div>
              </template>
            </el-table-column>
            <el-table-column
              v-if="shopItem.goodsType == 2"
              label="机型"
              width="98"
            >
              <template slot-scope="scope">
                <div class="rl-text-xxs">
                  {{ scope.row.diy ? scope.row.diy.modelName : "" }}
                </div>
              </template>
            </el-table-column>
            <el-table-column
              v-if="shopItem.goodsType == 1"
              label="颜色"
              width="70"
            >
              <template slot-scope="scope">
                <div class="rl-text-xxs" v-show="$i18n.locale === 'zh'">
                  {{ scope.row.colorName }}
                </div>
                <div class="rl-text-xxs" v-show="$i18n.locale === 'en'">
                  {{ scope.row.colorNameEn }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="会员价" prop="salePrice" width="110">
              <template slot-scope="scope">
                <i v-show="$root.currency === 'CNY'" class="asmd"
                  >￥{{ scope.row.salePrice | keepTwoNum }}</i
                >
                <i v-show="$root.currency === 'USD'" class="asmd"
                  >${{
                    (Number(scope.row.salePrice) * exchange) | keepTwoNum
                  }}</i
                >
              </template>
            </el-table-column>
            <el-table-column label="数量" :width="160">
              <template slot-scope="scope">
                <div :class="{ 'choose-unit': scope.row.boxs }">
                  <!-- 在途、在库数量 -->
                  <div
                    class="
                      app-flex app-justify-content-space-between
                      rl-padding-left-default rl-padding-right-default
                    "
                    v-if="shopItem.goodsType === 1 && scope.row.itemType == 1"
                  >
                    <div class="rl-text-xxs">
                      {{ scope.row.zaiKuCount }}
                    </div>
                    <div class="rl-text-xxs">/</div>
                    <div class="rl-text-xxs">
                      {{ scope.row.zaiTuCount }}
                    </div>
                  </div>
                  <!-- 定制计算器 -->
                  <div
                    @click="lookDiyItem(scope.row, scope.row.diyList)"
                    v-if="scope.row.diyList"
                    class="rl-relative"
                  >
                    <el-input-number
                      v-model="scope.row.num"
                      :min="1"
                      label="描述文字"
                      size="mini"
                      @keyup="this.value = this.value.replace(/\D/g, '')"
                    ></el-input-number>
                    <span class="diy-gai"></span>
                  </div>
                  <!-- 普通计算器 -->
                  <div class="input-wrap" v-else>
                    <!-- <el-input-number @change="handleChange($event, scope.row,listItem.rules?listItem.rules.conditions:'',listItem)" 
                    v-model="scope.row.count" :min="1" label="描述文字" 
                    size="mini" :disabled="scope.row.itemType==1?false:true" v-if="scope.row.openFlag==1"></el-input-number> -->
                    <!-- 自封装计数器 -->
                    <div
                      class="el-input-number el-input-number--mini"
                      v-if="scope.row.openFlag == 1"
                    >
                      <!--减号 -->
                      <span
                        class="el-input-number__decrease"
                        :class="
                          scope.row.count == 0 || scope.row.itemType == 2 ||
                          scope.row.visible !== 1
                            ? 'is-disabled'
                            : ''
                        "
                        v-if="scope.row.itemType != 2"
                        @click="
                          handleChange(
                            scope.row.count,
                            scope.row,
                            listItem.rules && listItem.rules.conditions
                              ? listItem.rules.conditions
                              : '',
                            listItem,
                            1
                          )
                        "
                        ><i class="el-icon-minus"></i
                      ></span>
                      <!-- 加号 -->
                      <span
                        class="el-input-number__increase"
                        :class="
                          scope.row.count ==
                            scope.row.inStockUsableCount +
                              scope.row.onWayUsableCount ||
                          scope.row.itemType == 2 ||
                          scope.row.visible !== 1
                            ? 'is-disabled'
                            : ''
                        "
                        v-if="scope.row.itemType != 2"
                        @click="
                          handleChange(
                            scope.row.count,
                            scope.row,
                            listItem.rules && listItem.rules.conditions
                              ? listItem.rules.conditions
                              : '',
                            listItem,
                            2
                          )
                        "
                        ><i class="el-icon-plus"></i
                      ></span>
                      <!-- 数字框 -->
                      <div class="el-input el-input--mini">
                        <input
                          v-model="scope.row.count"
                          :min="1"
                          :max="
                            scope.row.inStockUsableCount +
                            scope.row.onWayUsableCount
                          "
                          @blur="
                            handleChange(
                              scope.row.count,
                              scope.row,
                              listItem.rules && listItem.rules.conditions
                                ? listItem.rules.conditions
                                : '',
                              listItem,
                              3
                            )
                          "
                          onkeyup="this.value=this.value.replace(/\D/g,'')"
                          v-if="scope.row.itemType == 1"
                          class="el-input__inner"
                        />
                        <!-- <input
                          v-model="scope.row.count"
                          :min="1"
                          :max="
                            scope.row.inStockUsableCount +
                            scope.row.onWayUsableCount
                          "
                          disabled
                          v-if="scope.row.itemType == 2"
                          class="el-input__inner"
                        /> -->
                        <span class="gift-num" v-if="scope.row.itemType == 2">{{
                          scope.row.count
                        }}</span>
                      </div>
                    </div>

                    <el-input-number
                      label="描述文字"
                      size="mini"
                      disabled
                      v-if="scope.row.openFlag == 0"
                    ></el-input-number>
                  </div>
                  <!-- 批量下单 - 包装 -->
                  <div class="box-info" v-if="scope.row.boxs">
                    <el-popover
                      placement="bottom"
                      width="220"
                      class="cursor-pointer"
                      popper-class="box-info-table"
                      trigger="click"
                      :ref="refNamePopover + scope.row.id"
                    >
                      <el-table
                        :data="scope.row.boxs"
                        @row-click="
                          (row, column, e) =>
                            handleCheck(row, column, e, scope.row)
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
                      <span slot="reference">{{
                        scope.row.choosedBoxInfo
                          ? scope.row.choosedBoxInfo
                          : $t("P.Pieces")
                      }}</span>
                    </el-popover>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="合计" width="110">
              <template slot-scope="scope">
                <div>
                  <div v-show="$root.currency === 'CNY'" class="rl-text-xxs">
                    ￥{{ scope.row.totalPrice | keepTwoNum }}
                  </div>
                  <div v-show="$root.currency === 'USD'" class="rl-text-xxs">
                    ${{
                      (Number(scope.row.totalPrice) * exchange) | keepTwoNum
                    }}
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作" :min-width="90">
              <template slot-scope="scope">
                <!-- 切换活动 -->
                <el-button
                  v-if="
                    shopItem.goodsType == 1 &&
                    ((scope.row.promotions &&
                      scope.row.promotions.length > 0) ||
                      (scope.row.groupSeckills &&
                        scope.row.groupSeckills.length > 0))
                  "
                  @click.native.prevent="
                    changeActivity(scope.row, listItem.children)
                  "
                  class="mini-search-btn"
                  >{{ $t("ShopCarts.Switch") }}</el-button
                >
                <!-- 查看图片 -->
                <el-button
                  v-if="shopItem.goodsType == 2"
                  @click.native.prevent="lookDiyImage(scope.row.diy)"
                  class="mini-search-btn"
                  >{{ $t("ShopCarts.View") }}</el-button
                >
                <!-- 删除 -->
                <el-button
                  @click.native.prevent="
                    deleteShopCarItems(
                      scope.row.id,
                      scope.row,
                      listItem,
                      'delItem'
                    )
                  "
                  class="mini-search-btn"
                  >{{ $t("ShopCarts.Delete") }}</el-button
                >
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>

    <!--切换活动-弹框-Y-->
    <el-dialog
      class="alls"
      :title="$t('ShopCarts.SwitchSum')"
      :visible.sync="changeDialogVisible"
    >
      <div
        class="
          change-activity
          rl-bdt-gray-sm
          max-height300
          rl-padding-left-default rl-padding-right-default rl-clear
        "
      >
        <div
          @click="changeActivityLabel(item)"
          class="item app-flex"
          v-for="item in changeActivityData"
          :key="item.id"
        >
          <div
            class="
              app-flex
              app-align-items-flex-center
              app-justify-content-space-between
            "
          >
            <span
              class="chenked"
              :class="{ active: item.checked === true }"
            ></span>
          </div>
          <div class="cons">
            <div>
              <el-button class="cons-btn">{{
                item.isPingTuan == 1 ? item.name : item.ruleName
              }}</el-button>
            </div>
            <div
              class="rule"
              v-for="(value, cindex) in item.conditions"
              :key="value.id"
            >
              {{ $t("ShopCarts.Condition") }}{{ cindex + 1 }}:{{
                value.conditionName
              }}
            </div>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer rl-margin-top-xxxs rl-tc">
        <el-button @click="changeDialogVisible = false">{{
          $t("P.Cancel")
        }}</el-button>
        <el-button @click="confirmChangeActivity()" type="primary">{{
          $t("P.OK")
        }}</el-button>
      </div>
    </el-dialog>

    <!--查看详细规则-Y-->
    <el-dialog
      class="alls"
      :title="$t('ShopCarts.RuleDetails')"
      :visible.sync="ruleDetailDialogVisible"
    >
      <div
        class="
          change-activity
          rl-bdt-gray-sm
          max-height300
          rl-padding-left-default rl-padding-right-default rl-clear
        "
      >
        <!-- 规则名称 -->
        <div class="rule-item rl-margin-bottom-xxxs">
          <span>{{ $t("ShopCarts.RuleName") }}:</span>
          <span class="rl-margin-left-xxxs">{{ ruleDetail.ruleName }}</span>
        </div>
        <!-- 规则描述 -->
        <div class="rule-item rl-margin-bottom-xxxs">
          <span>{{ $t("ShopCarts.RuleDescription") }}:</span>
          <span class="rl-margin-left-xxxs">{{ ruleDetail.ruleDescribe }}</span>
        </div>
        <!-- 规则条件列表 -->
        <div>
          <p
            class="rl-margin-bottom-xxxs"
            v-for="(item, index) in ruleDetail.conditions"
            :key="index"
          >
            <span>{{ $t("ShopCarts.Condition") }}{{ index + 1 }}:</span>
            <span class="rl-margin-left-xxxs">{{ item.conditionName }}</span>
          </p>
        </div>
      </div>
      <div slot="footer" class="dialog-footer rl-margin-top-xxxs rl-tc">
        <el-button @click="ruleDetailDialogVisible = false">{{
          $t("P.Cancel")
        }}</el-button>
      </div>
    </el-dialog>

    <!--查看diy图片-Y-->
    <el-dialog class="diy-all" title :visible.sync="diyDialogVisible">
      <div class="diy-css rl-clear">
        <div class="banner rl-fl">
          <el-carousel
            trigger="click"
            :loop="falseState"
            :autoplay="falseState"
            height="740px"
            arrow="always"
            indicator-position="none"
          >
            <el-carousel-item v-for="(item, index) in diyImages" :key="index">
              <div class="banner-img cursor-pointer">
                <img :src="item" alt />
              </div>
              <div class="rl-tc rl-text-base sum">x{{ diyImages.lenght }}</div>
            </el-carousel-item>
          </el-carousel>
        </div>
      </div>
      <div slot="footer" class="dialog-footer rl-tc">
        <el-button @click="diyDialogVisible = false">{{
          $t("P.Cancel")
        }}</el-button>
      </div>
    </el-dialog>

    <!--删除diy货品-->
    <el-dialog
      class="alls"
      :title="$t('ShopCarts.DeleteItem')"
      :visible.sync="deleteDiyDialogVisible"
    >
      <div
        class="
          diy-css
          rl-bdt-gray-sm
          max-height300
          rl-padding-left-default rl-padding-right-default rl-clear
        "
      >
        <table>
          <thead>
            <tr>
              <th class="rl-padding-top-xxs rl-padding-bottom-xxs diy-gou"></th>
              <th class="rl-padding-top-xxs rl-padding-bottom-xxs">
                {{ $t("ShopCarts.Picture") }}
              </th>
              <th class="rl-padding-top-xxs rl-padding-bottom-xxs">
                {{ $t("ShopCarts.Quantity") }}
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in deleteDiyItem" :key="item.id">
              <td width="100px" class="diy-gou">
                <span
                  class="cursor-pointer"
                  :class="{ gou: item.gou === 1 }"
                  @click="chooseDiyGou(item, item.gou)"
                ></span>
              </td>
              <td width="250px" class="rl-tc">
                <div class="diy-delete-Img rl-margin-zero">
                  <img :src="item.diyPic" alt />
                </div>
              </td>
              <td width="250px" class="rl-tc">x{{ item.diyNum }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div slot="footer" class="dialog-footer rl-margin-top-xxxs">
        <div class="rl-fl dialog-footer-div">
          <span
            class="cursor-pointer"
            :class="{ gou: diyAll === true }"
            @click="selectDiyAll"
            >{{ $t("ShopCarts.SelectAll") }}</span
          >
        </div>
        <el-button @click="deleteDiyDialogVisible = false">{{
          $t("P.Cancel")
        }}</el-button>
        <el-button @click="confirmDeleteDiyItem" type="danger">{{
          $t("P.OK")
        }}</el-button>
      </div>
    </el-dialog>

    <!--查看diy货品-->
    <el-dialog
      class="alls"
      :title="$t('ShopCarts.Ordered')"
      :visible.sync="lookDiyDialogVisible"
    >
      <div
        class="
          diy-css
          rl-bdt-gray-sm
          max-height300
          rl-padding-left-default rl-padding-right-default rl-clear
        "
      >
        <table>
          <thead>
            <tr>
              <th class="rl-padding-top-xxs rl-padding-bottom-xxs">
                {{ $t("ShopCarts.Picture") }}
              </th>
              <th class="rl-padding-top-xxs rl-padding-bottom-xxs">
                {{ $t("ShopCarts.Ordered") }}
              </th>
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
                <el-input-number
                  @change="handleChangeDiy($event, item)"
                  v-model="item.diyNum"
                  :min="0"
                  label="描述文字"
                  size="mini"
                ></el-input-number>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div slot="footer" class="dialog-footer rl-margin-top-xxxs rl-tc">
        <el-button @click="lookDiyDialogVisible = false">{{
          $t("P.Cancel")
        }}</el-button>
        <el-button @click="confirmAdjustDiyItem" type="primary">{{
          $t("P.OK")
        }}</el-button>
      </div>
    </el-dialog>

    <!--查看满赠商品-Y-->
    <div class="cover" v-if="showcov"></div>
    <div class="pro-cover cover-box rl-relative" v-if="showcov">
      <!--<span @click="shutLog" class="shut cursor-pointer"></span>-->
      <div
        class="
          rl-bg-green_xs
          rl-padding-left-default rl-padding-bottom-xxxs rl-padding-top-xxxs
        "
      >
        {{ $t("P.Receive") }} {{ presentCount }} {{ $t("P.MostPieces") }}，{{
          $t("P.HaveSelected")
        }}{{ selectSongCount }} {{ $t("P.Pieces") }}
      </div>
      <div
        class="rl-tc rl-padding-top-default rl-text-gray"
        v-if="songGoodsList.length === 0"
      >
        {{ $t("P.NoData") }}
      </div>
      <div
        class="shop-table rl-padding-left-default rl-padding-right-default"
        :class="{ 'scroll-y': this.songGoodsList.length > 5 }"
      >
        <el-table
          :data="songGoodsList"
          border
          max-height="900"
          header-row-class-name="header-row"
          class="activity-el-table rl-tc"
        >
          <!-- 图片 -->
          <el-table-column :label="$t('ShopCarts.Picture')" :min-width="60">
            <template slot-scope="scope">
              <div class="shop-img cursor-pointer">
                <img width="100%" :src="scope.row.itemImg" alt="" />
              </div>
            </template>
          </el-table-column>
          <!-- 货品编码 -->
          <el-table-column
            :label="$t('ShopCarts.ItemNo')"
            prop="itemCode"
            :min-width="90"
          >
          </el-table-column>
          <!-- 货品名称 -->
          <el-table-column
            :label="$t('ShopCarts.ItemName')"
            prop="itemName"
            :min-width="110"
          >
          </el-table-column>
          <!-- 规格 -->
          <el-table-column :label="$t('ShopCarts.Spe')" prop="specsName">
          </el-table-column>
          <!-- 颜色 -->
          <el-table-column
            :label="$t('ShopCarts.Colors')"
            prop="colorName"
            width="55"
          >
          </el-table-column>
          <!-- 库存 -->
          <el-table-column
            :label="$t('P.Inventory')"
            prop="stockNum"
            min-width="59"
          ></el-table-column>
          <!-- 最多可选 -->
          <!-- <el-table-column label="最多可选" prop="presentCount" min-width="59"></el-table-column> -->
          <!-- 已选 -->
          <el-table-column :label="$t('ShopCarts.Quantity')" :min-width="120">
            <template slot-scope="scope">
              <!-- <el-input-number v-model="scope.row.count" :max="scope.row.stockCoun" @change="songHandleChange($event,scope.row)" :min="0"  label="描述文字" size="mini" ></el-input-number> -->
              <!-- <songSum ref="songSum" :songShop="item" :maxSongSum="presentCount" :hasChoose="hasChoose"></songSum> -->
              <!-- 自封装计数器 -->
              <div class="el-input-number el-input-number--mini">
                <!--减号 -->
                <span
                  class="el-input-number__decrease"
                  :class="scope.row.itemCount <= 0 ? 'is-disabled' : ''"
                  @click="songHandleChange(scope.row.itemCount, scope.row, 1)"
                  ><i class="el-icon-minus"></i
                ></span>
                <!-- 加号 -->
                <span
                  class="el-input-number__increase"
                  :class="
                    scope.row.num == scope.row.stockNum ? 'is-disabled' : ''
                  "
                  @click="songHandleChange(scope.row.itemCount, scope.row, 2)"
                  ><i class="el-icon-plus"></i
                ></span>
                <!-- 数字框 -->
                <div class="el-input el-input--mini">
                  <input
                    v-model="scope.row.num"
                    :min="0"
                    :max="scope.row.stockNum"
                    @blur="songHandleChange(scope.row.itemCount, scope.row, 3)"
                    onkeyup="this.value=this.value.replace(/\D/g,'')"
                    class="el-input__inner"
                  />
                </div>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="btn rl-margin-zero">
        <span
          @click="confirmSongShop"
          class="
            rl-tc rl-bg-blue-mm rl-text-white rl-margin-right-default
            cursor-pointer
          "
          >{{ $t("P.OK") }}</span
        >
        <span
          @click="shutLog"
          class="rl-tc rl-bg-gray-sm rl-text-white cursor-pointer"
          >{{ $t("P.Cancel") }}</span
        >
      </div>
    </div>

    <!--去凑单-Y-->
    <div v-if="activityDialogVisible === true">
      <addOnItem
        @cancelDialog="cancelDialog"
        :activityDialogVisible="activityDialogVisible"
        :pieceRule="pieceRule"
      ></addOnItem>
    </div>
  </div>
</template>

<script>
import Vue from "vue";
import sonComponent from "@/components/sonComponent.vue";
import addOnItem from "@/components/addOnItem.vue";
import songSum from "@/components/songSum.vue";
import {} from "@/assets/js/common.js";
import GD from "@/assets/js/globalData";
import { Calendar } from "element-ui";
// liu
import { MessageBox } from "element-ui";
import {
  addShoppingcartOne,
  deleteShoppingcartOne,
  deleteShoppingcart,
  promotionPresentList,
  ModifyShoppingcart,
  listStockByCondition,
  addShoppingcart,
} from "@/apiService/api";
import { fomatFloat } from "@/assets/js/common.js";
export default {
  name: "catsComponent",
  components: { sonComponent, addOnItem, songSum },
  props: {
    shopCartData: {
      type: Array,
    },
    onWayAttendEventFlag: {
      type: Number,
      default: 0,
    },
    groupFlag: {
      type: String,
    },
  },
  data() {
    return {
      allcheck: false, //全选
      changeActivityGoods: {}, //切换活动时的当前货品
      falseState: false,
      showHeader: false,
      showcov: false, // 查看满赠商品
      activityDialogVisible: false, // 去凑单
      ruleDetailDialogVisible: false,
      deleteDiyDialogVisible: false,
      diyDialogVisible: false,
      changeDialogVisible: false,
      changeActivityData: [],
      diyAll: true,
      diyImages: [],
      lookDiyDialogVisible: false,
      deleteDiyId: "",
      adjustDiyCarId: "",
      ruleDetail: {},
      pieceRule: {}, // 去凑单
      deleteDiyItem: [],
      adjustDiyItem: [],
      lookSongId: "",
      deletePresentItems: [], // 提交赠品前删除赠品
      presentCount: 0, // 赠品最大选择数量
      selectSongCount: 0, //赠品选了多少件
      songGoodsList: [], // 满送商品列表
      presentsPromotionId: "", //赠品活动id
      deletePresentsIds: [], //需要删除的赠品ids
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: "zh-RMB", // 语种
      totalMoneys: [],
      totalCounts: [],
      refNamePopover: "popover-", // popover ref名称前缀
      exchange: 1, //汇率
      handleGoodTimer: null, // 监听商品更新
      handleSongTimer: null, // 监听赠品更新
      currentCount: 0,
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
      this.songGoodsList.forEach((item) => {
        if (item.num) {
          total += Number(item.num);
        }
      });
      return total;
    },
  },
  mounted() {
    console.log("---", this.shopCartData);
  },
  methods: {
    // 保持输入框为正整数
    preventPoint(type, evt) {
      let newVal = parseInt(evt.target.value);
      evt.target.value = ""; // 这句话看起来多此一举，但不写的话就是会出问题，但是为啥会出问题呢，我不知道!!!
      evt.target.value = newVal;
      this[type] = newVal;
    },

    fixNum(time) {
      var timer = (Array(2).join(0) + time).slice(-2);
      if (timer > 0) {
        return timer;
      } else {
        return 0;
      }
    },
    fLangChange(value) {
      window.localStorage.setItem("bLang", value);
      this.$i18n.locale = value.split("-")[0];
    },
    // 表头换行
    renderheader(h, { column, $index }) {
      return h("span", {}, [
        h("span", {}, column.label.split("/")[0]),
        h("br"),
        h("span", {}, column.label.split("/")[1]),
      ]);
    },
    // 订单是否同享状态
    getStatus(row) {
      switch (row) {
        case 0:
          return "(" + this.$t("ShopCarts.Different") + ")";
        case 1:
          return "(" + this.$t("ShopCarts.Enjoy") + ")";
      }
    },
    goDiscountActivity() {
      this.$router.push({ name: "DiscountActivity" });
    },
    // 普通商品加减号以及输入框输入操作
    handleChange(val, item, conditionsList, goodList, type, delFlag) {
      this.currentCount = item.itemCount; // 记录数量
      console.log("加减：", val);
      console.log(item);
      console.log("规则条件列表：", conditionsList);
      console.log("当前组货品列表", goodList);

      if (item.visible !== 1) {
        // 不可视
        this.$message.warning("该商品已下架!");
        return;
      }

      if (item.itemType == 2) {
        return;
      } //赠品不能加减
      let inputCount =
        type == 3 ? Number(val) : type == 1 ? Number(val) - 1 : Number(val) + 1; //当前加减数
      let count = inputCount * (item.choosedBoxNum ? item.choosedBoxNum : 1); //货品实际数量
      let totalPrice = item.salePrice * count; //货品价格
      let goodsPromotionId = "no";
      let groupSeckillId = "";
      let reduceOrPresent = 1; //是否是赠品  2：是  1：否
      let reduceOrPresentId = "";
      let presentCount = 0;
      this.presentCount = 0;
      let conditionsId = "";
      let zaiKuCount =
        count <= item.inStockUsableCount ? count : item.inStockUsableCount; //在库下单数量
      let zaiTuCount =
        count <= item.inStockUsableCount ? 0 : count - item.inStockUsableCount; //在途下单数量
      // 普通货品
      if (item.goodsType == 1) {
        // 拼团
        if (item.groupSeckillId && item.groupSeckillId != "") {
          item.groupSeckills.forEach((groupItem) => {
            if (
              item.groupSeckillId == groupItem.groupSeckillId &&
              groupItem.groupSeckillStatus == 1
            ) {
              if (count > groupItem.minNum) {
                totalPrice = count * groupItem.groupSeckillPrice;
                if (count > item.inStockUsableCount + item.onWayUsableCount) {
                  // 是否支持超卖或预售 1、支持 0、不支持
                  if (groupItem.mtoFlag == 1) {
                    let itemSurplusNum = groupItem.maxNum - groupItem.realSum;
                    if (
                      groupItem.maxNum &&
                      item.itemSurplusNum != 0 &&
                      count > itemSurplusNum
                    ) {
                      this.$message.warning(
                        "货品订购数量不能大于拼团限购量：" + itemSurplusNum
                      );
                      inputCount = itemSurplusNum;
                      count =
                        inputCount *
                        (item.choosedBoxNum ? item.choosedBoxNum : 1); //货品实际数量
                      zaiKuCount =
                        count <= item.inStockUsableCount
                          ? count
                          : item.inStockUsableCount; //在库下单数量
                      zaiTuCount =
                        count <= item.inStockUsableCount
                          ? 0
                          : count - item.inStockUsableCount; //在途下单数量
                      return;
                    }
                  } else {
                    this.$message.warning("购买数量不能大于库存!");
                    return;
                  }
                }
              }
            }
          });
        } else {
          if (count > item.inStockUsableCount + item.onWayUsableCount) {
            this.$message.warning("购买数量不能大于库存!");
            return;
          }
        }

        // 促销活动
        if (item.goodsPromotionId && item.goodsPromotionId != "no") {
          if (item.promotions) {
            item.promotions.forEach((promoItem) => {
              let onWayFlag = promoItem.onWayFlag; //在途商品是否参与活动 1.参与，0.不参与
              let promoStatus = promoItem.promoStatus; //促销状态：0 未开始, 1 促销中 2 已过期 3 提前结束
              let promoType = promoItem.promoType; //活动类型，1 普通活动，2 阶梯活动
              promoItem.rules.forEach((ruleItem) => {
                if (item.goodsPromotionId == ruleItem.id && promoStatus == 1) {
                  let isOneBuyCount = onWayFlag == 0 ? zaiKuCount : count;
                  let isOneBuyMoney =
                    onWayFlag == 0
                      ? zaiKuCount * item.salePrice
                      : count * item.salePrice;
                  //货品活动是否累计 addUpFlag，1是 0否( ruleType 规则对象是2或3时有效)
                  if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
                    let childrenList = goodList.children;
                    let zCoun = zaiKuCount;
                    let tCount = zaiTuCount;
                    let zPrice = zaiKuCount * item.salePrice;
                    let tPrice = zaiTuCount * item.salePrice;
                    for (let i = 0; i < childrenList.length; i++) {
                      if (
                        childrenList[i].itemId != item.itemId &&
                        childrenList[i].itemType != 2
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
                  // 是否累计 --END
                  ruleItem.conditions.forEach((cItem) => {
                    goodsPromotionId = ruleItem.id;
                    conditionsId = cItem.id;
                    // specialFlag 是否特价，1是 0否
                    if (cItem.specialFlag == 1) {
                      totalPrice =
                        onWayFlag == 0
                          ? zaiKuCount * cItem.specialPrice +
                            zaiTuCount * item.salePrice
                          : count * cItem.specialPrice;
                      //  活动累计情况下，勾选上的货品都按特价计算
                      if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
                        let childrenList = goodList.children;
                        for (let i = 0; i < childrenList.length; i++) {
                          if (
                            childrenList[i].isCheck &&
                            childrenList[i].itemId != item.itemId
                          ) {
                            let childrenTotalPrice =
                              onWayFlag == 0
                                ? childrenList[i].zaiKuCount *
                                    cItem.specialPrice +
                                  childrenList[i].zaiTuCount *
                                    childrenList[i].salePrice
                                : childrenList[i].itemCount *
                                  cItem.specialPrice;
                            this.$set(
                              childrenList[i],
                              "totalPrice",
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
                      //reduceOrPresent 促销统计方式：1满减 2满赠
                      if (cItem.reduceOrPresent == 1) {
                        // reduceType 满减类型， 1减免 2折扣
                        if (cItem.reduceType == 1) {
                          //减免金额
                          let reduction = cItem.reduction;
                          // 判断是否叠加的减免金额
                          if (cItem.reductionPresentAddFlag == 1) {
                            reduction =
                              ruleItem.moneyOrCount == 1
                                ? onWayFlag == 0
                                  ? zaiKuCount *
                                    item.salePrice *
                                    (cItem.reduction / cItem.oneBuyMoney)
                                  : count *
                                    item.salePrice *
                                    (cItem.reduction / cItem.oneBuyMoney)
                                : onWayFlag == 0
                                ? zaiKuCount *
                                  (cItem.reduction / cItem.oneBuyCount)
                                : count *
                                  (cItem.reduction / cItem.oneBuyCount);
                          }
                          let cutAmount =
                            Number(count * item.salePrice) -
                              Number(reduction) >
                            0
                              ? Number(count * item.salePrice) -
                                Number(reduction)
                              : 0;
                          totalPrice = fomatFloat(Number(cutAmount), 2);
                          if (ruleItem.ruleType == 1) {
                            let orserIndex = 0;
                            goodList.children.forEach((orderItem) => {
                              //  if(orderItem.isCheck){
                              if (orderItem.itemId != item.itemId) {
                                this.$set(
                                  orderItem,
                                  "totalPrice",
                                  Number(
                                    orderItem.salePrice * orderItem.itemCount
                                  )
                                );
                              }
                              orserIndex += 1;

                              //  }
                            });
                          }

                          //  活动累计情况下，勾选上的货品都按减免计算
                          if (
                            ruleItem.addUpFlag == 1 &&
                            ruleItem.ruleType != 1
                          ) {
                            let childrenList = goodList.children;
                            for (let i = 0; i < childrenList.length; i++) {
                              if (
                                childrenList[i].isCheck &&
                                childrenList[i].itemId != item.itemId
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
                                        (cItem.reduction / cItem.oneBuyCount)
                                      : childrenList[i].itemCount *
                                        (cItem.reduction / cItem.oneBuyCount);
                                }
                                let childrenTotalPrice = fomatFloat(
                                  childrenList[i].totalPrice - reduction2,
                                  2
                                );
                                this.$set(
                                  childrenList[i],
                                  "totalPrice",
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
                          //折扣
                          let zhekouPrice = fomatFloat(
                            (item.salePrice * cItem.discount) / 100,
                            2
                          );
                          totalPrice =
                            onWayFlag == 0
                              ? zaiKuCount * zhekouPrice +
                                zaiTuCount * item.salePrice
                              : count * zhekouPrice;
                          //  活动累计情况下，勾选上的货品都按减免计算
                          if (
                            ruleItem.addUpFlag == 1 &&
                            ruleItem.ruleType != 1
                          ) {
                            let childrenList = goodList.children;
                            for (let i = 0; i < childrenList.length; i++) {
                              if (
                                childrenList[i].isCheck &&
                                childrenList[i].itemId != item.itemId
                              ) {
                                let zhekouPrice2 = fomatFloat(
                                  (childrenList[i].salePrice *
                                    cItem.discount) /
                                    100,
                                  2
                                );
                                let childrenTotalPrice =
                                  onWayFlag == 0
                                    ? childrenList[i].zaiKuCount *
                                        zhekouPrice2 +
                                      childrenList[i].zaiTuCount *
                                        childrenList[i].salePrice
                                    : childrenList[i].itemCount *
                                      zhekouPrice2;
                                this.$set(
                                  childrenList[i],
                                  "totalPrice",
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
                      } else {
                        //赠品
                        reduceOrPresent = 2;
                        reduceOrPresentId = cItem.id;
                        // 不叠加时（默认），判断购买数量与满赠数量是否匹配（购买金额与满赠金额是否匹配）
                        presentCount = ruleItem.moneyOrCount == 1
                          ? (isOneBuyMoney >= cItem.oneBuyMoney ? cItem.presentCount: 0)
                          : (isOneBuyCount >= cItem.oneBuyCount ? cItem.presentCount : 0);
                        // 判断是否叠加
                        if (cItem.reductionPresentAddFlag == 1) {
                          //计算叠加的数量
                          let dieJiaNum =
                            ruleItem.moneyOrCount == 1
                              ? parseInt(isOneBuyMoney / cItem.oneBuyMoney)
                              : parseInt(isOneBuyCount / cItem.oneBuyCount);
                          presentCount = cItem.presentCount * dieJiaNum;
                          this.presentCount += presentCount;
                        } else {
                          this.presentCount = presentCount;
                        }
                      }
                    }
                  });
                }
              });
            });
          }
        }
        this.$set(item, "zaiKuCount", zaiKuCount);
        this.$set(item, "zaiTuCount", zaiTuCount);
        this.$set(item, "itemCount", Number(count));
        this.$set(item, "count", inputCount);
        this.$set(item, "totalPrice", Number(totalPrice));
        this.$set(item, "conditionsId", conditionsId);
        this.$set(item, "reduceOrPresent", reduceOrPresent);
        this.$set(item, "reduceOrPresentId", reduceOrPresentId);
        this.$set(item, "presentCount", presentCount);
        this.$set(goodList, "reduceOrPresent", reduceOrPresent);
        this.$set(goodList, "reduceOrPresentId", reduceOrPresentId);
        this.$set(goodList, "presentCount", this.presentCount);
      } else {
        //定制货品
        let count = inputCount * (item.choosedBoxNum ? item.choosedBoxNum : 1);
        if (item.diyUnderStockFlag == 1) {
          //不缺货
          this.$set(item, "zaiKuCount", count);
          this.$set(item, "zaiTuCount", 0);
          this.$set(item, "itemCount", inputCount);
          this.$set(item, "count", inputCount);
          this.$set(item, "totalPrice", Number(inputCount * item.salePrice));
        } else {
          //缺货
          this.$message.warning("购买数量不能大于库存!");
          this.$set(item, "itemCount", val);
          this.$set(item, "count", val);
          return;
        }
      }
      
      if (!delFlag) {
        if ((type == 1 && val <= 1) || (type == 3 && val < 1)) {
          // 删除货品
          this.deleteShopCarItems(item.id, item, goodList, delFlag);
          return;
        }
      }

      clearTimeout(this.handleGoodTimer);
      this.handleGoodTimer = setTimeout(() => {
        this.$parent.showLoading = true;
        if (count > 0) {
          let params = {
            diyType: item.diyType, //定制类型 0-标准定制 1-DIY定制
            goodsPromotionId:
              item.goodsPromotionId &&
                item.goodsPromotionId != "no" &&
                item.goodsPromotionId != ""
                ? item.goodsPromotionId
                : "", //商品促销活动Id(活动条件id)
            goodsType: item.goodsType, //商品类型 1-普通 2-定制
            groupSeckillId:
              item.groupSeckillId != "no" ? item.groupSeckillId : "",
            itemCount: count, //购物数量
            itemType: item.itemType, //是否赠品 1 非赠品 2 赠品
            id: item.id,
            unit: item.boxId, // 装箱规格id
          };
          //修改购物车
          ModifyShoppingcart(params).then((res) => {
            if (res.success) {
            }
          });
        }

        let ids = [];
        goodList.children.forEach((gItem, gIndex) => {
          if (gItem.count === 0) {
            goodList.children.splice(gIndex, 1); // 删除货品
          }

          if (item.goodsPromotionId == "no" && gItem.itemType == 2) {
            ids.push(gItem.id);
            goodList.children.splice(gIndex, 1); // 前端删除赠品
          }
        });
        if (ids.length > 0) {
          this.deleteShoppingcartFun(ids);
        }

        // 赠品活动
        if (reduceOrPresent == 2) {
          if (goodList.reduceOrPresentId) {
            clearTimeout(this.handleSongTimer);
            this.handleSongTimer = setTimeout(() => {
              this.getPresentsData(
                goodList.reduceOrPresentId,
                goodList.rules.conditions,
                goodList,
                "handleChange"
              );
            }, 300);
          }
        } else {
          this.$parent.showLoading = false;
        }
      }, 300);
    },

    // 更新赠品数量
    handleSongNum(goodList) {
      let presentCount = this.presentCount; // 可选赠品数
      let selectSongCount = 0; // 已选赠品数
      let presentStockNum = 0; // 赠品库存总数
      let presentList = []; // 赠品列表
      for (let i = 0; i < goodList.children.length; i++) {
        if (goodList.children[i].itemType == 2) {
          selectSongCount += goodList.children[i].itemCount;

          let kucunCount =
            goodList.children[i].inStockUsableCount +
            goodList.children[i].onWayUsableCount;
          presentStockNum += kucunCount;
          goodList.children[i].kucunCount = kucunCount;
          presentList.push(goodList.children[i]);
        }
      }

      let yuCount = presentCount - selectSongCount; // 需新增赠品数
      let hasChange = false;
      // 可选赠品数跟已选赠品数不相等
      if (presentCount != selectSongCount) {
        // 判断可选数量与赠品库存总数
        if (presentCount >= presentStockNum) {
          // 超出总库存，分配给所有赠品
          presentList.forEach((pre) => {
            pre.itemCount = pre.kucunCount;
          });
        } else {
          // 低于，判断赠品列表单个库存与可选赠品数，默认给第一个，不足分配给下一个
          presentList.forEach((pre) => {
            if (yuCount > 0) {
              hasChange = true;
              // 剩余库存数大于或等于需新增赠品数
              if (pre.itemCount + yuCount <= pre.kucunCount) {
                pre.itemCount = pre.itemCount + yuCount;
                yuCount -= yuCount;
              } else {
                let num = pre.itemCount + yuCount - pre.kucunCount;
                pre.itemCount = pre.kucunCount;
                yuCount = num;
              }
            } else if (yuCount < 0) {
              hasChange = true;
              if (pre.itemCount + yuCount <= 0) {
                let num = pre.itemCount + yuCount;
                pre.itemCount = 0;
                yuCount = num;
              } else {
                let num = pre.itemCount + yuCount;
                pre.itemCount = num;
                yuCount -= yuCount;
              }
            }
          });
        }

        // 更新购物车赠品数量
        if (hasChange && presentList.length > 0) {
          let ids = []; // 删除赠品
          // 已修改赠品列表
          presentList.forEach((pre) => {
            if (pre.itemCount > 0) {
              let songParams = {
                diyType: pre.diyType, //定制类型 0-标准定制 1-DIY定制
                goodsPromotionId:
                  pre.goodsPromotionId &&
                  pre.goodsPromotionId != "no" &&
                  pre.goodsPromotionId != ""
                    ? pre.goodsPromotionId
                    : "", //商品促销活动Id(活动条件id)
                goodsType: pre.goodsType, //商品类型 1-普通 2-定制
                groupSeckillId: "",
                itemCount: pre.itemCount, //购物数量
                itemType: pre.itemType, //是否赠品 1 非赠品 2 赠品
                id: pre.id,
              };
              // 修改购物车
              ModifyShoppingcart(songParams).then((res) => {
                if (res.success) {
                }
              });
            } else {
              ids.push(pre.id);
            }
          });

          if (ids.length > 0) {
            // 删除赠品
            this.deleteShoppingcartFun(ids);
          }

          setTimeout(() => {
            this.$parent.showLoading = false;
            this.$parent.getShopCarList(); // 重新拉取购物车列表
          }, 1000);
        } else {
          this.$parent.showLoading = false;
          this.$parent.getShopCarList(); // 重新拉取购物车列表
        }
      } else {
        this.$parent.getShopCarList(); // 重新拉取购物车列表
      }
    },

    // 批量删除购物车
    deleteShoppingcartFun(ids, type) {
      deleteShoppingcart({ ids: ids }).then((res) => {
        if (res.success) {
          if (type && type === "refreshCart") {
            this.$parent.getShopCarList(); // 重新拉取购物车列表
          }
        }
      });
    },

    // 定制加减号操作
    handleChangeDiy(val, item) {
      this.$nextTick(() => {
        if (val === 0) {
          item.diyNum = 1;
          if (this.$i18n.locale === "zh") {
            this.$message.warning(this.$t("P.GoodsItemNull"));
          } else {
            this.$message.warning(
              "The order quantity of products cannot be null."
            );
          }
        }
      });
    },

    // 查看diy图片
    lookDiyImage(item) {
      this.diyDialogVisible = true;
      this.diyImages.push(item.previewImage);
    },
    lookDiyItem(item, itemList) {
      // 查看diy货品
      this.lookDiyDialogVisible = true;
      this.adjustDiyCarId = item.id;
      this.adjustDiyItem = itemList;
    },
    // 查看规则详情
    lookRuleDetail(val) {
      console.log(val);
      let id = val.goodsPromotionId;
      let list = val.promotions;
      list.forEach((item) => {
        item.rules.forEach((items) => {
          if (id == items.id) {
            this.ruleDetail = items;
          }
        });
      });
      this.ruleDetailDialogVisible = true;
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
      this.deleteDiyItem.forEach((item) => {
        if (item.gou === 1) {
          ids = ids + "," + item.diyId;
        }
      });
      ids = ids.substr(1); // 删除字符串第一位
      if (ids === "") {
        if (this.$i18n.locale === "zh") {
          this.$message.success("请勾选货品");
        } else {
          this.$message.success("Please check the items");
        }
        return false;
      }
      if (this.diyAll === true) {
        temp = { ids: this.deleteDiyId };
      } else {
        temp = { ids: this.deleteDiyId, diyIds: ids };
      }
      this.$api.delete(this, "user/u/shoppingCart", temp).then((res) => {
        if (res.code === 0) {
          if (this.$i18n.locale === "zh") {
            this.$message.success(this.$t("ShopCarts.SuccessfullyDeleted"));
          } else {
            this.$message.success("Delete successfully");
          }
          this.deleteDiyDialogVisible = false;
          this.$emit("reloadCart", true);
        }
      });
    },
    confirmAdjustDiyItem() {
      let totolCount = 0;
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
            this.$emit("reloadCart", true);
          }
        });
    },
    // 订单类型--Y
    orderTypes(row) {
      switch (row) {
        case 1:
          return this.$t("ShopCarts.Common");
        case 2:
          return this.$t("ShopCarts.newCustomized");
      }
    },
    // 全选反选--Y
    selectAllGou(diyType, index) {
      let shopList = this.shopCartData;

      this.shopCartData[index].allCkeck = !this.shopCartData[index].allCkeck;
      let allFlag = this.shopCartData[index].allCkeck;
      this.shopCartData[index].list.forEach((item) => {
        let isOneBuyCount = 0;
        let isOneBuyPrice = 0;
        let presentIds = [];
        let zCoun = 0;
        let tCount = 0;
        let reduceOrPresent = 1; //是否是赠品 1：否  2：是
        let reduceOrPresentId = "";
        let presentCount = 0;
        let conditionsId = "";
        item.children.forEach((gItem) => {
          this.$set(gItem, "conditionsId", "");

          // 只累加不是赠品的货品 (scope.row.goodsType==1?((scope.row.onWayUsableCount+scope.row.inStockUsableCount)<(scope.row.count*scope.row.choosedBoxNum)):(scope.row.diyUnderStockFlag==0))
          if (gItem.itemType == 1) {
            //itemType:是否赠品 1 非赠品 2 赠品
            //  onWayFlag:在途商品是否参与活动 1.参与，0.不参与
            zCoun += gItem.zaiKuCount;
            tCount += gItem.zaiTuCount;
            console.log(zCoun);
          }
          if (gItem.reduceOrPresent == 2) {
            reduceOrPresent = 2;
          }
          if (gItem.itemType == 2) {
            presentIds.push(gItem.id);
          }
        });

        item.children.forEach((items) => {
          this.$set(items, "isCheck", allFlag);

          // 判断是否缺货：有效 标品库存 定制diyUnderStockFlag 拼团mtoFlag 是否可视visible（非赠品且不可视）
          if (
            !items.openFlag ||
            ((items.goodsType == 1
              ? items.onWayUsableCount + items.inStockUsableCount <
                items.count * items.choosedBoxNum
              : items.diyUnderStockFlag == 0) &&
              items.openFlag == 1 &&
              items.mtoFlag !== 1)  ||
              (items.itemType !== 2 && items.visible !== 1)
          ) {
            // 失效或者缺货
            this.$set(items, "isCheck", false);
          }

          if (
            items.isCheck &&
            items.goodsPromotionId &&
            items.goodsPromotionId != "no" &&
            items.goodsPromotionId != "" &&
            items.itemType == 1
          ) {
            items.promotions.forEach((promoItem) => {
              let onWayFlag = promoItem.onWayFlag; //在途商品是否参与活动 1.参与，0.不参与
              let promoStatus = promoItem.promoStatus; //促销状态：0 未开始, 1 促销中 2 已过期 3 提前结束
              let promoType = promoItem.promoType; //活动类型，1 普通活动，2 阶梯活动
              promoItem.rules.forEach((ruleItem) => {
                if (item.goodsPromotionId == ruleItem.id && promoStatus == 1) {
                  let isOneBuyCount =
                    onWayFlag == 0 ? items.zaiKuCount : items.itemCount;
                  let isOneBuyMoney =
                    onWayFlag == 0
                      ? item.zaiKuCount * items.salePrice
                      : items.itemCount * items.salePrice;
                  //货品活动是否累计 addUpFlag，1是 0否( ruleType 规则对象是2或3时有效)
                  if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
                    isOneBuyCount = onWayFlag == 0 ? zCoun : zCoun + tCount;
                    isOneBuyMoney =
                      onWayFlag == 0
                        ? zCoun * items.salePrice
                        : (zCoun + tCount) * items.salePrice;
                  }
                  // 是否累计 --END
                  ruleItem.conditions.forEach((cItem) => {
                    //moneyOrCount 规则形式：1金额 2数量
                    if (
                      (ruleItem.moneyOrCount == 1 &&
                        isOneBuyMoney >= cItem.oneBuyMoney) ||
                      (ruleItem.moneyOrCount == 2 &&
                        isOneBuyCount >= cItem.oneBuyCount)
                    ) {
                      conditionsId = cItem.id;
                      if (cItem.reduceOrPresent == 2) {
                        reduceOrPresent = 2;
                        reduceOrPresentId = cItem.id;
                        presentCount = cItem.presentCount;
                        // 判断是否叠加
                        if (cItem.reductionPresentAddFlag == 1) {
                          //计算叠加的数量
                          let dieJiaNum =
                            ruleItem.moneyOrCount == 1
                              ? parseInt(isOneBuyMoney / cItem.oneBuyMoney)
                              : parseInt(isOneBuyCount / cItem.oneBuyCount);
                          presentCount = cItem.presentCount * dieJiaNum;
                        }
                        this.$set(items, "reduceOrPresent", reduceOrPresent);
                        this.$set(
                          items,
                          "reduceOrPresentId",
                          reduceOrPresentId
                        );
                        this.$set(items, "presentCount", presentCount);
                      }
                      // 累计 勾选上的货品都加上规则条件id
                      if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
                        item.children.forEach((gItem2) => {
                          if (gItem2.isCheck || gItem2.itemType == 2) {
                            this.$set(gItem2, "conditionsId", cItem.id);
                          }
                        });
                      }
                    } else {
                      //不满足条件的把勾选上的货品都去掉规则条件id
                      item.children.forEach((gItem2) => {
                        this.$set(gItem2, "conditionsId", "");
                      });
                    }
                  });
                }
              });
            });

            this.$set(item, "reduceOrPresent", reduceOrPresent);
            this.$set(item, "reduceOrPresentId", reduceOrPresentId);
            this.$set(item, "presentCount", presentCount);
          }

          // reduceOrPresent = presentCount > 0 ? 2 : 1; // 判断是否可添加赠品，显示查看赠品按钮

          // 有赠品但是不满足条件的删除赠品商品（反选）
          // if (!allFlag && presentIds.length > 0) {
          //   this.deletePresentFun(presentIds);
          //   shopList[index].list.forEach((sItem) => {
          //     if (sItem.goodsPromotionId == item.goodsPromotionId) {
          //       let arrList = sItem.children.filter(
          //         (fItem) => fItem.itemType == 1
          //       );
          //       this.$set(sItem, "children", arrList);
          //     }
          //   });
          // }
        });
      });

      this.shopCartData[index].allCkeck = allFlag;

      console.log("全选----", this.shopCartData);
    },

    // 货品选择 --单选--7.28
    chooseGou(item, index, goodList) {
      console.log("货品单选", item);
      console.log(goodList);
      let shopList = this.shopCartData;
      this.$set(item, "isCheck", !item.isCheck);
      // 处理活动
      let presentIds = []; //赠品ids列表
      let reduceOrPresent = 1; //是否是赠品 1：否  2：是
      let reduceOrPresentId = "";
      let presentCount = 0;
      let conditionsId = "";
      let childrenList = goodList.children;
      let zCoun = 0; // 已选在库数量
      let tCount = 0; // 已选在途数量
      // let choosedPresentCount = 0; // 已选赠品数量
      for (let i = 0; i < childrenList.length; i++) {
        if (childrenList[i].isCheck && childrenList[i].itemType != 2) {
          zCoun += childrenList[i].zaiKuCount;
          tCount += childrenList[i].zaiTuCount;
        }
        if (childrenList[i].reduceOrPresent == 2) {
          reduceOrPresent = 2;
        }
        if (childrenList[i].itemType == 2) {
          presentIds.push(childrenList[i].id);
          // choosedPresentCount += childrenList[i].itemCount;
        }
      }
      if (goodList.goodsPromotionId && goodList.goodsPromotionId != "no") {
        item.promotions.forEach((promoItem) => {
          let onWayFlag = promoItem.onWayFlag; //在途商品是否参与活动 1.参与，0.不参与
          let promoStatus = promoItem.promoStatus; //促销状态：0 未开始, 1 促销中 2 已过期 3 提前结束
          let promoType = promoItem.promoType; //活动类型，1 普通活动，2 阶梯活动
          promoItem.rules.forEach((ruleItem) => {
            if (item.goodsPromotionId == ruleItem.id && promoStatus == 1) {
              let isOneBuyCount =
                onWayFlag == 0 ? item.zaiKuCount : item.itemCount;
              let isOneBuyMoney =
                onWayFlag == 0
                  ? item.zaiKuCount * item.salePrice
                  : item.itemCount * item.salePrice;
              //货品活动是否累计 addUpFlag，1是 0否( ruleType 规则对象是2或3时有效)
              if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
                isOneBuyCount = onWayFlag == 0 ? zCoun : zCoun + tCount;
                isOneBuyMoney =
                  onWayFlag == 0
                    ? zCoun * item.salePrice
                    : (zCoun + tCount) * item.salePrice;
              }

              // 是否累计 --END
              ruleItem.conditions.forEach((cItem) => {
                //moneyOrCount 规则形式：1金额 2数量
                if (
                  (ruleItem.moneyOrCount == 1 &&
                    isOneBuyMoney >= cItem.oneBuyMoney) ||
                  (ruleItem.moneyOrCount == 2 &&
                    isOneBuyCount >= cItem.oneBuyCount)
                ) {
                  conditionsId = cItem.id;

                  if (cItem.reduceOrPresent == 2) {
                    reduceOrPresent = 2;
                    reduceOrPresentId = cItem.id;
                    presentCount = cItem.presentCount;

                    // 判断是否叠加
                    if (cItem.reductionPresentAddFlag == 1) {
                      //计算叠加的数量
                      let dieJiaNum =
                        ruleItem.moneyOrCount == 1
                          ? parseInt(isOneBuyMoney / cItem.oneBuyMoney)
                          : parseInt(isOneBuyCount / cItem.oneBuyCount);
                      presentCount = cItem.presentCount * dieJiaNum;
                    }

                    this.$set(item, "reduceOrPresent", reduceOrPresent);
                    this.$set(item, "presentCount", presentCount);
                  }
                  // 累计 勾选上的货品都加上规则条件id
                  if (ruleItem.addUpFlag == 1 && ruleItem.ruleType != 1) {
                    goodList.children.forEach((gItem2) => {
                      if (gItem2.isCheck || gItem2.itemType == 2) {
                        this.$set(gItem2, "conditionsId", cItem.id);
                        if (gItem2.itemType == 2) {
                          this.$set(gItem2, "isCheck", true);
                        }
                      }
                    });
                  }
                } else {
                  //不满足条件的把勾选上的货品都去掉规则条件id
                  goodList.children.forEach((gItem2) => {
                    this.$set(gItem2, "conditionsId", "");
                  });
                }
              });
            }
          });
        });
      }

      reduceOrPresent = presentCount > 0 ? 2 : 1; // 判断是否可添加赠品，显示查看赠品按钮

      this.$set(goodList, "conditionsId", conditionsId);
      this.$set(goodList, "reduceOrPresent", reduceOrPresent);
      this.$set(goodList, "reduceOrPresentId", reduceOrPresentId);
      this.$set(goodList, "presentCount", presentCount);
      console.log("dsdsdsd----", goodList);

      let flag = true;
      shopList[index].list.forEach((item1) => {
        item1.children.forEach((items) => {
          if (!items.isCheck) {
            flag = false;
            // return;
          }
          //有赠品的时候，勾选货品把赠品也勾选上
          if (
            items.itemId == item.itemId &&
            items.itemType == 1 &&
            items.goodsPromotionId != "no" &&
            items.goodsPromotionId != ""
          ) {
            item1.children.forEach((item3) => {
              if (item3.itemType == 2) {
                if (reduceOrPresent == 2) {
                  // 有赠品
                  this.$set(item3, "isCheck", item.isCheck);
                  this.$set(item3, "conditionsId", conditionsId);
                } else {
                  // 无赠品
                  this.$set(item3, "isCheck", false);
                  this.$set(item3, "conditionsId", conditionsId);
                }
              }
            });
          }
        });
      });
      this.shopCartData[index].allCkeck = flag;
    },

    getRowClass({ rowIndex }) {
      // 改变表头样式
      if (rowIndex === 0) {
        return "background:#15BED6;color:#fff;border-right: none;font-weight: bold;";
      } else {
        return "";
      }
    },
    // 通知父组件
    reloadFatherCart(target) {
      if (target) {
        this.$emit("reloadCart", true);
      }
    },

    // 切换活动-Y
    changeActivity(item, goodList) {
      console.log("切换活动：", item);
      console.log("切换活动下的赠品", goodList);
      // 有赠品的时候，切换为不参与活动需要把赠品全删除
      goodList.forEach((pItem) => {
        if (pItem.itemType == 2) {
          this.deletePresentsIds.push(pItem.id);
        }
      });

      this.changeActivityGoods = item;
      this.changeDialogVisible = true;
      this.changeActivityData = [
        { ruleName: "不参与活动", isPingTuan: 3, id: "noId", conditions: [] },
      ];
      this.$nextTick(() => {
        if (item.promotions && item.promotions.length > 0) {
          item.promotions.forEach((val) => {
            this.$set(val, "ruleType", 1); // 1活动规则 2等级折扣

            val.rules.forEach((ruleItem) => {
              this.$set(ruleItem, "isPingTuan", 2); // 是否是拼团活动 1拼团 2普通 3：无活动
              this.changeActivityData.push(ruleItem);
            });
          });
        }
        if (item.groupSeckills && item.groupSeckills.length) {
          item.groupSeckills.forEach((groupItem) => {
            this.$set(groupItem, "isPingTuan", 1); // 是否是拼团活动 1拼团 2普通 3：无活动
            this.changeActivityData.push(groupItem);
          });
        }

        this.changeActivityData.forEach((items) => {
          if (
            item.goodsPromotionId &&
            item.goodsPromotionId != "" &&
            items.id == item.goodsPromotionId
          ) {
            Vue.set(items, "checked", true);
          } else if (
            items.groupSeckillId &&
            item.groupSeckillId != "" &&
            items.groupSeckillId == item.groupSeckillId
          ) {
            Vue.set(items, "checked", true);
          } else {
            Vue.set(items, "checked", false);
          }
        });
      });
    },
    // 切换活动--选择活动-Y
    changeActivityLabel(item) {
      console.log("选择活动", item);
      this.changeActivityData.forEach((val) => {
        let id = item.isPingTuan == 1 ? item.groupSeckillId : item.id;
        let valId = item.isPingTuan == 1 ? val.groupSeckillId : val.id;
        if (id === valId) {
          val.checked = true;
        } else {
          val.checked = false;
        }
      });
    },

    // 选择活动确定按钮 --Y-计算价格可能可去掉
    confirmChangeActivity() {
      let product = {};
      let promoItem = {};
      let goodItem = this.changeActivityGoods;

      let promotionId = "";
      let isPingTuan = 0;
      this.changeActivityData.forEach((item) => {
        if (item.checked === true) {
          console.log("选择活动确定按钮：", item);
          isPingTuan = item.isPingTuan;
          promotionId = item.isPingTuan == 1 ? item.groupSeckillId : item.id;
        }
      });
      if (promotionId != "") {
        let promotionId2 = promotionId != "noId" ? promotionId : "";
        let params = {
          diyType: goodItem.diyType, //定制类型 0-标准定制 1-DIY定制
          goodsPromotionId: isPingTuan == 2 ? promotionId2 : "", //商品促销活动Id(活动条件id)
          goodsType: goodItem.goodsType, //商品类型 1-普通 2-定制
          groupSeckillId: isPingTuan == 1 ? promotionId2 : "",
          itemCount: goodItem.itemCount, //购物数量
          itemType: goodItem.itemType, //是否赠品 1 非赠品 2 赠品
          id: goodItem.id,
        };
        //添加购物车
        ModifyShoppingcart(params).then((res) => {
          if (res.success) {
            this.changeDialogVisible = false;
            if (this.$i18n.locale === "zh") {
              this.$message.success("切换活动成功!");
            } else {
              this.$message.success("Switch successfully.");
            }
            if (this.deletePresentsIds.length > 0) {
              // 删除赠品
              this.deletePresentFun(this.deletePresentsIds, "refreshCart");
            } else {
              this.$parent.getShopCarList(); //重新拉取购物车列表
            }
          } else {
            this.$message(res.errMessage);
          }
        });
      } else {
        this.$message.success("请选择要切换的活动!");
      }
    },
    //当有赠品的活动选择切换为不参与活动--- 删除赠品--即调用批量删除
    deletePresentFun(ids, type) {
      this.deleteShoppingcartFun(ids, type);
    },
    // 删除购物车（单条）---Y
    deleteShopCarItems(id, row, goodList, delFlag) {
      this.$parent.showLoading = true;

      if (delFlag && delFlag === 'delPreItem') {
        // 更新赠品
        if (row) {
          this.handleChange(
            0,
            row,
            goodList.rules && goodList.rules.conditions
              ? goodList.rules.conditions
              : "",
            goodList,
            3
          );
        }
      } else {
        let info = "";
        if (this.$i18n.locale === "zh") {
          info = "此操作将删除该货品, 是否继续?";
        } else {
          info = "The product will be deleted. Do you want to continue?";
        }
        this.$confirm(info, this.$t("P.Prompt"), {
          confirmButtonText: this.$t("P.OK"),
          cancelButtonText: this.$t("P.Cancel"),
          type: "warning",
          closeOnClickModal: false,
        })
          .then(() => {
            deleteShoppingcartOne({ id: id }).then((res) => {
              if (res.success) {
                this.$message("删除成功！");

                if (row) {
                  if (row.itemType === 2) {
                    // 删除赠品/货品
                    this.$parent.getShopCarList(); //重新拉取购物车列表
                    this.$parent.showLoading = false;
                  } else {
                    // 更新赠品
                    this.handleChange(
                      0,
                      row,
                      goodList.rules && goodList.rules.conditions
                        ? goodList.rules.conditions
                        : "",
                      goodList,
                      3,
                      "delPreItem"
                    );
                  }
                }
              } else {
                this.$parent.showLoading = false;
              }
            });
          })
          .catch(() => {
            if (row.itemType === 2 || (delFlag && delFlag === 'delItem')) {
              // 取消删除赠品/货品
              this.$parent.showLoading = false;
            } else {
              // 取消删除，商品输入框输入更新
              this.handleChange(
                this.currentCount,
                row,
                goodList.rules && goodList.rules.conditions
                  ? goodList.rules.conditions
                  : "",
                goodList,
                3
              );
            }
          });
      }
    },

    // 去凑单-y
    pieceTogether(rulesId, rulesName, conditions) {
      console.log("规则id：", rulesId);
      console.log("规则名称：", rulesName);
      console.log("规则条件列表：", conditions);
      this.songGoodsList = [];
      this.pieceRule = {
        rulesId: rulesId,
        rulesName: rulesName,
        conditions: conditions,
      };
      this.activityDialogVisible = true;
    },

    // 查看赠品-y
    async getPresentsData(id, conditions, goodItem, type) {
      this.presentsPromotionId = goodItem.goodsPromotionId;
      this.presentCount = goodItem.presentCount;
      this.selectSongCount = 0;
      for (let i = 0; i < goodItem.children.length; i++) {
        if (goodItem.children[i].itemType == 2) {
          this.selectSongCount += goodItem.children[i].itemCount;
        }
      }

      let res = await promotionPresentList({ id: id });
      let ids = [];
      if (res.success) {
        res.data.forEach((item) => {
          this.$set(item, "num", 0);
          ids.push(item.itemId);
          // 已选赠品数量回显
          goodItem.children.forEach((good) => {
            if (good.itemType == 2 && good.itemCode === item.itemCode) {
              item.num = good.itemCount;
            }
          });
        });
        this.songGoodsList = res.data;
      }

      let nesIds = [...new Set(ids)]; //去重
      let userId = localStorage.getItem("userId");
      let stockRes = await listStockByCondition({
        distributorId: userId,
        itemIdList: nesIds,
      }); //库存
      this.songGoodsList.forEach((item) => {
        this.$set(item, "itemCount", item.num);
        stockRes.data.forEach((item2) => {
          if (item.itemId == item2.itemId) {
            this.$set(
              item,
              "stockNum",
              item2.inStockUsableCount + (item2.numOnWaySum || 0)
            );
          }
        });
      });

      // 更新赠品数量
      if (type && type === "handleChange") {
        this.handleSongNum(goodItem);
      } else {
        this.$parent.showLoading = false;
        this.showcov = true;
      }
    },
    // 赠品弹框加减
    songHandleChange(count, item, type) {
      if (type == 1 && Number(item.num) <= 0) {
        return;
      }

      type == 3
        ? (this.selectSongCount =
            this.selectSongCount + (Number(item.num) - item.itemCount))
        : type == 1
        ? (this.selectSongCount -= 1)
        : (this.selectSongCount += 1);

      // 判断已选数量
      if (this.selectSongCount > 0) {
        // 大于0
        if (
          Number(item.num) < item.stockNum &&
          this.selectSongCount <= this.presentCount
        ) {
          type == 3
            ? (item.num = item.num)
            : type == 1
            ? (item.num -= 1)
            : (item.num += 1);
          this.$set(item, "num", Number(item.num));
          this.$set(item, "itemCount", Number(item.num));
        } else {
          if (type == 3) {
            this.$set(item, "num", Number(item.itemCount));
            this.$set(item, "itemCount", Number(item.num));
          }

          this.$message("已超出最多可选的赠品数");
        }
      } else {
        // 小于或等于0，设置为0
        this.$set(item, "num", 0);
        this.$set(item, "itemCount", Number(item.num));
      }

      let selectSongCount = 0;
      this.songGoodsList.forEach((song) => {
        selectSongCount += song.num;
      });
      this.selectSongCount = selectSongCount;
    },
    // 提交确认赠品
    confirmSongShop() {
      let list = this.songGoodsList;
      let presentsList = [];
      list.forEach((item) => {
        item.goodsPromotionId = this.presentsPromotionId;
        if (item.itemCount > 0 || item.num > 0) {
          let objL = {
            imageUrl: item.itemImg ? item.itemImg : item.imageUrl1,
            cartType: 1, //加购类型：1 分销商 2 C端客户
            diyType: 1, //定制类型 0-标准定制 1-DIY定制
            goodsId: item.goodsId, //商品id
            goodsName: item.goodsName,
            goodsNo: item.goodsNo, //商品编码
            goodsPromotionId: this.presentsPromotionId, //商品促销活动Id(活动条件id)
            goodsType: 1, //商品类型 1-普通 2-定制
            itemCode: item.itemCode, //货品编码
            itemCount: item.itemCount, //购物数量
            itemNum: item.itemCount, //购物数量
            itemId: item.itemId, //货品id
            itemName:
              this.$i18n.locale === "zh"
                ? item.itemName
                : item.itemNameEn
                ? item.itemNameEn
                : item.itemName, //货品名称
            itemNameEn: item.itemNameEn ? item.itemNameEn : "", //货品英文名称
            itemType: 2, //是否赠品 1 非赠品 2 赠品
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
            unit: item.boxId, // 装箱规格id
          };

          presentsList.push(objL);
        }
      });

      let changedPreList = []; // 已修改赠品
      let addPreList = []; // 新增赠品
      let ids = []; // 需删除赠品id
      this.shopCartData.forEach((cart) => {
        if (cart.goodsType === 1 && cart.list.length > 0) {
          cart.list.forEach((item) => {
            // 购物车参与活动货品
            if (item.goodsPromotionId && item.goodsPromotionId !== "no") {
              item.children.forEach((child) => {
                // 已选赠品
                presentsList.forEach((present) => {
                  // 赠品弹窗已选货品编码与购物车赠品货品编码匹配
                  if (
                    child.itemType == 2 &&
                    present.itemCode === child.itemCode &&
                    present.goodsPromotionId === child.goodsPromotionId
                  ) {
                    present.id = child.id;
                    present.itemCount = present.itemCount - child.itemCount; // 需减去购物车已选数量，以免超出可选总数

                    changedPreList.push(present);
                  }

                  // 赠品弹窗已选货品编码与购物车赠品货品编码不匹配
                  if ((present.itemCode !== child.itemCode) || (child.itemType != 2 && present.itemCode === child.itemCode)) {
                    addPreList.push(present);
                  }
                });

                // 赠品列表
                list.forEach((song) => {
                  // 判断赠品弹窗列表货品编码与购物车赠品货品编码是否匹配，并且数量是否为0
                  if (
                    child.itemType == 2 &&
                    song.itemCode === child.itemCode &&
                    song.goodsPromotionId === child.goodsPromotionId &&
                    (song.num === 0 || song.itemCount == 0)
                  ) {
                    ids.push(child.id);
                  }
                });
              });
            }
          });
        }
      });

      // 数组去重
      changedPreList = [...new Set(changedPreList)];
      addPreList = [...new Set(addPreList)];
      
      // 判断赠品已选数量与赠品可选数量
      if (this.selectSongCount <= this.presentCount) {
        // 判断是否选择赠品
        if (presentsList.length > 0) {
          // 新增赠品
          if (addPreList.length > 0) {
            // 加入购物车
            addShoppingcart(addPreList).then((res) => {
              if (res.success) {
                this.$message.success("加入购物车成功");
                this.showcov = false;

                // 判断已修改赠品
                if (changedPreList.length > 0) {
                  // 修改购物车
                  changedPreList.forEach((item) => {
                    let params = {
                      diyType: item.diyType, //定制类型 0-标准定制 1-DIY定制
                      goodsPromotionId:
                        item.goodsPromotionId &&
                        item.goodsPromotionId != "no" &&
                        item.goodsPromotionId != ""
                          ? item.goodsPromotionId
                          : "", //商品促销活动Id(活动条件id)
                      goodsType: item.goodsType, //商品类型 1-普通 2-定制
                      groupSeckillId:
                        item.groupSeckillId != "no" ? item.groupSeckillId : "",
                      itemCount: item.itemNum, //购物数量
                      itemType: item.itemType, //是否赠品 1 非赠品 2 赠品
                      id: item.id,
                    };
                    ModifyShoppingcart(params).then((res) => {
                      if (res.success) {
                        this.showcov = false;
                      }
                    });
                  });

                  // 删除赠品
                  if (ids.length > 0) {
                    this.deletePresentFun(ids, "refreshCart");
                  } else {
                    this.$parent.getShopCarList(); // 重新拉取购物车列表
                  }
                } else {
                  // 删除赠品
                  if (ids.length > 0) {
                    this.deletePresentFun(ids, "refreshCart");
                  } else {
                    this.$parent.getShopCarList(); // 重新拉取购物车列表
                  }
                }
              } else {
                this.$message(res.errMessage);
              }
            });
          } else {
            // 判断已修改赠品
            if (changedPreList.length > 0) {
              // 修改购物车
              changedPreList.forEach((item) => {
                let params = {
                  diyType: item.diyType, //定制类型 0-标准定制 1-DIY定制
                  goodsPromotionId:
                    item.goodsPromotionId &&
                    item.goodsPromotionId != "no" &&
                    item.goodsPromotionId != ""
                      ? item.goodsPromotionId
                      : "", //商品促销活动Id(活动条件id)
                  goodsType: item.goodsType, //商品类型 1-普通 2-定制
                  groupSeckillId:
                    item.groupSeckillId != "no" ? item.groupSeckillId : "",
                  itemCount: item.itemNum, //购物数量
                  itemType: item.itemType, //是否赠品 1 非赠品 2 赠品
                  id: item.id,
                };
                ModifyShoppingcart(params).then((res) => {
                  if (res.success) {
                    this.showcov = false;
                  }
                });
              });

              // 删除赠品
              if (ids.length > 0) {
                this.deletePresentFun(ids, "refreshCart");
              } else {
                this.$parent.getShopCarList(); // 重新拉取购物车列表
              }
            } else {
              // 无新增，无修改
              this.showcov = false;
              // 删除赠品
              if (ids.length > 0) {
                this.deletePresentFun(ids, "refreshCart");
              } else {
                this.$parent.getShopCarList(); // 重新拉取购物车列表
              }
            }
          }
        } else {
          // 未选
          MessageBox.confirm(this.$t("Message.One"), this.$t("Message.Two"), {
            confirmButtonText: this.$t("Message.Confirm"),
            cancelButtonText: this.$t("Message.Cancel"),
            type: "warning",
          }).then(() => {
            // 删除赠品
            if (ids.length > 0) {
              this.deletePresentFun(ids, "refreshCart");
            }
            this.showcov = false;
          });
        }
      } else {
        this.$message("选择的赠品数量超出了最多可领取的件数");
      }
    },
    // 赠品弹框取消
    shutLog() {
      this.showcov = false;
    },

    goDoodsDetail(id, item) {
      // 去商品详情
      if (item.orderType === 3) {
        // 定制商品
        let routeData = this.$router.resolve({
          name: "ShopDetail",
          query: { good_id: id, goodsType: 2, accessType: 0 },
        });
        window.open(routeData.href, "_blank");
      } else {
        let routeData = this.$router.resolve({
          name: "ShopDetail",
          query: { good_id: id, goodsType: 1, accessType: 0 },
        });
        window.open(routeData.href, "_blank");
      }
    },
    cancelDialog(target) {
      if (target === 0) {
        this.activityDialogVisible = false;
      } else {
        this.activityDialogVisible = false;
        this.$parent.getShopCarList();
      }
    },
    // 批量下单 - 包装  --Y
    handleCheck(row, column, e, item) {
      this.$set(item, "choosedBoxInfo", row.boxType);
      this.$set(item, "choosedBoxNum", row.boxNum);
      this.$set(item, "boxId", row.id);

      // 修改在途、在库的数量
      if (item.zaiTuCount > 0) {
        this.$set(
          item,
          "totalPrice",
          (item.totalPrice / item.zaiTuCount) * item.count * row.boxNum
        );
        this.$set(item, "zaiTuCount", item.count * row.boxNum);
      }
      if (item.zaiKuCount > 0) {
        this.$set(
          item,
          "totalPrice",
          (item.totalPrice / item.zaiKuCount) * item.count * row.boxNum
        );
        this.$set(item, "zaiKuCount", item.count * row.boxNum);
      }
      let refName = this.refNamePopover + item.id;
      this.$refs[refName][0].doClose();
    },
  },
  created() {
    this.exchange = Number(localStorage.getItem("exchange"));
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem("bLang")
      ? window.localStorage.getItem("bLang")
      : "zh-RMB";
  },
};
</script>
<style lang='less'>
.checkgifts-btn {
  background: #15bed6;
  color: #fff;
  font-size: 12px;
  padding: 0 5px;
  line-height: 26px;
  height: 26px;
  margin-left: 30px;
  border-radius: 4px;
  cursor: pointer;
}
.el-dialog__wrapper {
  .el-dialog {
    width: 800px;
    overflow-y: auto;
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
.el-table::before {
  height: 0px !important;
}
.activity-el-table {
  .el-table__body-wrapper {
    .el-table__empty-block {
      height: 0 !important;
      min-height: 0;
    }
  }
}
</style>
<style scoped lang='less'>
@import url("../assets/less/variable.less");
.table-name {
  height: 45px;
  line-height: 45px;
}
.diy-gai {
  position: absolute;
  top: 0;
  left: 0;
  display: block;
  width: 145px;
  height: 30px;
  z-index: 1;
  cursor: pointer;
}
.mini-search-btn {
  font-size: 12px;
  padding: 5px;
}
.mini-search-btn-other {
  margin-left: 0;
  font-size: 12px;
  padding: 5px;
}
.shop-img {
  margin: auto;
  margin-top: 5px;
  width: 55px;
  img {
    width: 100%;
    height: 55px;
  }
}
.change-activity {
  max-height: 500px;
  padding-top: 20px;
  overflow-y: scroll;
  .item {
    margin-bottom: 20px;
    .chenked {
      width: 19px;
      height: 19px;
      display: inline-block;
      vertical-align: -5px;
      -webkit-appearance: none;
      appearance: none;
      outline: none;
      font-size: 14px;
      color: #333;
      background: url(../../src/assets/images/yuan3.png) no-repeat center center;
    }
    .chenked.active {
      background: url(../../src/assets/images/yuan4.png) no-repeat center center;
    }
    .cons {
      width: 100%;
      margin-left: 10px;
      padding-top: 10px;
      padding-bottom: 10px;
      padding-left: 20px;
      padding-right: 20px;
      border: 1px solid #ccc;
      .cons-btn {
        font-size: 12px;
        padding: 5px;
      }
      .rule {
        line-height: 30px;
        font-size: 12px;
      }
    }
  }
}
.activity {
  padding-top: 15px;
  padding-bottom: 15px;
  padding-left: 20px;
  line-height: 26px;
  background-color: #e5f9fb;
  .btn {
    span {
      display: inline-block;
      min-width: 50px;
      padding-left: 5px;
      padding-right: 5px;
      border-radius: 5px;
      line-height: 18px;
    }
  }
  .item {
    .chenked {
      width: 19px;
      height: 19px;
      display: inline-block;
      vertical-align: -5px;
      -webkit-appearance: none;
      appearance: none;
      outline: none;
      margin-left: 15px;
      font-size: 14px;
      color: #333;
      background: url(../../src/assets/images/selected.png) no-repeat center
        center;
    }
  }
}
.spe {
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
    max-height: 720px;
    .banner {
      width: 780px;
      position: relative;
      .banner-img {
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 0;
        img {
          height: 700px;
        }
      }
      .el-carousel {
        z-index: 0;
      }
    }
  }
  .dialog-footer {
    .el-button {
      margin-top: 10px;
    }
  }
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
    min-width: 30px;
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
  width: 1000px;
  border: 1px solid #ccc;
  border-radius: 5px;
  z-index: 99;
  background: #fefefe;
  .shop-table {
    margin: 20px 0;
    max-height: 300px;
    overflow-y: auto;
  }
}
.cover-box {
  box-sizing: border-box;
  position: fixed;
  top: 50%;
  left: 0;
  right: 0;
  margin: auto;
  z-index: 99;
  transform: translateY(-50%);
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
    padding-bottom: 20px;
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
.song {
  h1 {
    width: 22px;
    border-radius: 5px;
    word-wrap: break-word;
  }
}

// 拼团
.group-info {
  padding: 10px 15px;
  font-size: 12px;
  color: @red;
  &.green {
    color: @green;
  }
  .count-down {
    font-size: 12px;
  }
  .surplus {
    font-size: 12px;
  }
}

// 批量下单 - 包装
.choose-unit {
  position: relative;
  text-align: left;
  .app-flex {
    width: 110px;
  }
  .input-wrap {
    display: inline-block;
  }
  /deep/ .cell {
    padding: 0;
  }
  /deep/ .el-input-number--mini {
    width: 100px;
    .el-input-number__decrease,
    .el-input-number__increase {
      width: 20px;
    }
  }

  .box-info {
    display: inline-block;
    position: absolute;
    top: auto;
    bottom: 4px;
    right: 0;
    padding: 0 2px;
    min-width: 16px;
    height: 24px;
    font-size: 12px;
    color: @blue;
    text-align: center;
    line-height: 20px;
    border: 1px solid @blue;
    border-radius: 22px;
    cursor: pointer;
  }
}
.tips {
  color: #f7353e;
}

.gift-num {
  display: inline-block;
  width: 100px;
  height: 28px;
  text-align: center;
}
</style>
<style lang="less">
.el-popper {
  padding: 0;
  border: none;
}
.el-popper[x-placement^="bottom"] {
  margin-top: 22px;
}
</style>
