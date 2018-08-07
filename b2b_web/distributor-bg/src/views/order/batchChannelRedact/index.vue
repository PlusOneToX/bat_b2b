<template>
  <div class="order-detail">
    <header>
      <span v-if="noHandle">未下单订单查看</span>
      <span v-if="orderHandle">已下单订单查看</span>
      <el-button
        icon="el-icon-d-arrow-left"
        @click="cancel()"
        class="mini-back-btn btn-home"
      >
        返回批量导入订单列表
      </el-button>
    </header>

    <div v-loading="loading2">
      <!-- 订单基本信息 -->
      <div class="box-has-border">
        <div class="text-center title" style="margin-top: 0px">
          <span>订单基本信息</span>
        </div>
        <div class="half-width">
          <el-form
            :model="formData"
            status-icon
            :rules="rules"
            label-width="200px"
            label-position="right"
            ref="formData1"
            v-if="formData"
          >
            <el-form-item
              label="订单号:"
              v-if="checkhHandleFlag == 1"
              class="el-form-pad"
            >
              <template>
                <el-button
                  size="mini"
                  type="text"
                  @click="onEdit(item)"
                  v-for="(item, index) in allIds"
                  :key="index"
                >
                  {{ item }}
                </el-button>
              </template>
            </el-form-item>
            <!-- <el-form-item label="ERP订单号:" v-if="checkhHandleFlag == 1">
              <template>
                <span>{{
                  formData.orderNo == "null" ? "-" : formData.orderNo
                }}</span>
              </template>
            </el-form-item> -->

            <el-form-item label="分销商用户名:">
              <el-input
                v-model="formData.distributorName"
                size="mini"
                class="box-width"
                :disabled="true"
              />
            </el-form-item>

            <el-form-item label="支付方式:" prop="payWay">
              <el-select
                placeholder="请选择"
                size="mini"
                v-model="formData.payWay"
                :disabled="disableIsShow"
              >
                <el-option :value="1" label="支付宝支付"> </el-option>
                <el-option :value="2" label="微信支付"> </el-option>
                <el-option :value="3" label="区间支付"> </el-option>
                <el-option :value="4" label="线下转账"> </el-option>
                <el-option :value="5" label="余额支付"> </el-option>
                <el-option :value="6" label="网银支付"> </el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="配送方式:" prop="distributionId">
              <el-select
                v-model="formData.distributionId"
                placeholder="请选择"
                size="mini"
                :disabled="disableIsShow"
              >
                <el-option
                  v-for="item in orderDistriList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </div>

        <div class="half-width">
          <el-form
            :model="formData"
            status-icon
            :rules="rules"
            label-width="200px"
            label-position="right"
            ref="formData2"
          >
            <el-form-item label="订单类型:" prop="orderTypeValue">
              <el-select
                v-model="formData.orderTypeValue"
                placeholder="请选择"
                size="mini"
                :disabled="disableIsShow"
                @change="orderTypeValueChange"
              >
                <el-option
                  v-for="item in orderTypeList"
                  :key="item.erpType"
                  :label="item.name"
                  :value="item.erpType"
                ></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="下单时间:" v-if="checkhHandleFlag == 1">
              {{ timeFormatter(formData.orderCreateTime) }}
            </el-form-item>

            <el-form-item label="是否开票:">
              <el-radio-group v-model="formData.isInvoice">
                <el-radio :label="1" :disabled="disableIsShow">是</el-radio>
                <el-radio :label="0" :disabled="disableIsShow">否</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item label="发票类型:" v-if="invoiceShow">
              <el-select
                placeholder="请选择"
                size="mini"
                v-model="formData.invoiceType"
                v-if="invoiceShow"
                :disabled="disableIsShow"
              >
                <el-option :value="1" label="普通"> </el-option>
                <el-option :value="2" label="增值税发票"> </el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </div>
      </div>

      <!-- 收货信息 -->
      <div class="box-has-border">
        <div class="text-center title">
          <span>收货信息</span>
        </div>
        <div class="half-width">
          <el-form
            :model="formData"
            status-icon
            :rules="rules"
            label-width="200px"
            label-position="right"
            ref="formData3"
          >
            <el-form-item label="收货人:" prop="userName">
              <el-input
                v-model="formData.userName"
                size="mini"
                class="box-width"
                :disabled="disableIsShow"
              />
            </el-form-item>

            <el-form-item label="地区" prop="countryId">
              <el-select
                v-model="formData.countryId"
                placeholder="请选择"
                class="box-width"
                size="mini"
                :disabled="disableIsShow"
                @change="countryIdChange($event)"
              >
                <el-option
                  v-for="province in this.RegionForChose.countryId"
                  :key="province.id"
                  :label="province.regionName"
                  :value="province.id"
                >
                </el-option>
              </el-select>
              <el-select
                v-model="formData.provinceId"
                v-if="provinceShow"
                placeholder="请选择"
                class="box-width"
                size="mini"
                :disabled="disableIsShow"
                @change="provinceIdChange($event)"
              >
                <el-option
                  v-for="province in this.RegionForChose.provinceId"
                  :key="province.id"
                  :label="province.regionName"
                  :value="province.id"
                >
                </el-option>
              </el-select>
              <el-select
                v-model="formData.cityId"
                v-if="cityShow"
                placeholder="请选择"
                class="box-width"
                size="mini"
                :disabled="disableIsShow"
                @change="cityIdChange($event)"
              >
                <el-option
                  v-for="city in this.RegionForChose.cityId"
                  :key="city.id"
                  :label="city.regionName"
                  :value="city.id"
                >
                </el-option>
              </el-select>
              <el-select
                v-model="formData.districtId"
                v-if="townShow"
                placeholder="请选择"
                class="box-width"
                size="mini"
                :disabled="disableIsShow"
                @change="districtIdChange($event)"
              >
                <el-option
                  v-for="districtId in this.RegionForChose.districtId"
                  :key="districtId.id"
                  :label="districtId.regionName"
                  :value="districtId.id"
                >
                </el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="电话:">
              <el-input
                v-model="formData.phone"
                size="mini"
                class="box-width"
                :disabled="disableIsShow"
              />
            </el-form-item>

            <el-form-item label="分销商留言:">
              <el-input
                v-model="formData.remark"
                size="mini"
                class="box-width"
                :disabled="disableIsShow"
              />
            </el-form-item>
          </el-form>
        </div>
        <div class="half-width">
          <el-form
            :model="formData"
            status-icon
            :rules="rules"
            label-width="200px"
            label-position="right"
            ref="formData4"
          >
            <el-form-item label="邮编:">
              <el-input
                v-model="formData.zipCode"
                size="mini"
                class="box-width"
                :disabled="disableIsShow"
              />
            </el-form-item>

            <el-form-item label="街道地址:" prop="address">
              <el-input
                v-model="formData.address"
                size="mini"
                class="box-width"
                :disabled="disableIsShow"
              />
            </el-form-item>

            <el-form-item label="手机:" prop="mobile">
              <el-input
                v-model="formData.mobile"
                size="mini"
                class="box-width"
                :disabled="disableIsShow"
              />
            </el-form-item>
          </el-form>
        </div>
      </div>

      <!-- 商品信息 -->
      <div class="box-has-border">
        <div class="text-center title">
          <span>商品信息</span>
          <!-- <span v-if="checkhHandleFlag == 1" style="position: relative;left: 115px;">商品信息</span> -->
        </div>
        <!-- <div>
          <h4 class="order-message">在库商品数量不足时，订单会自动拆分成在库订单和在途订单。</h4>
          <button
            v-if="checkhHandleFlag == 1"
            class="mini-search-btn educe"
            @click="exportDownload()"
          >
            导出下单清单
          </button>
          <button
            class="mini-search-btn educe"
            v-if="orderShow"
            @click="exportExcel()"
          >
            导出缺货清单
          </button>
          <button
            class="mini-present-btn lookPromotion"
            v-if="
              (promotionIds.ruleIds !== undefined &&
                promotionIds.ruleIds !== null &&
                promotionIds.ruleIds.length > 0) ||
              (promotionIds.gradeRuleId !== undefined &&
                promotionIds.gradeRuleId !== null)
            "
            @click="lookPromotion()"
          >
            查看活动
          </button>
        </div> -->
        <el-table
          :data="formData.orderGoods"
          border
          style="width: 100%"
          header-row-class-name="header-row"
          class="tableCenter orderGoods"
          id="formData"
        >
          <el-table-column
            align="center"
            type="index"
            fixed
            :min-width="50"
          ></el-table-column>
          <el-table-column
            align="center"
            prop="itemCode"
            fixed
            label="存货编码"
            :min-width="120"
          ></el-table-column>
          <el-table-column align="center" label="下架状态" :min-width="100">
            <template slot-scope="scope">
              <el-button size="mini" disabled>{{
                saleStatusFormatter(scope.row.saleStatus)
              }}</el-button>
            </template>
          </el-table-column>
          <el-table-column
            v-if="
              formData.orderGoods &&
              formData.orderGoods.length > 0 &&
              formData.orderGoods[0].goodsType === 3
            "
            align="center"
            label="图片"
            :width="80"
          >
            <template slot-scope="scope">
              <el-image
                v-if="scope.row.newDiy.image"
                style="width: 50px; height: 50px"
                :src="scope.row.newDiy.image"
                fit="contain"
                :preview-src-list="[scope.row.newDiy.image]"
              >
              </el-image>
              <el-button v-else type="text" @click="diyPicture(scope.row)"
                >定制图片</el-button
              >
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            prop="itemName"
            label="存货名称"
            :min-width="120"
          ></el-table-column>
          <el-table-column
            align="center"
            v-if="
              formData.orderGoods &&
              formData.orderGoods.length > 0 &&
              formData.orderGoods[0].goodsType === 3
            "
            label="材质"
            :min-width="110"
          >
            <template slot-scope="scope">
              <span>{{ scope.row.newDiy.materialName }}</span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            v-if="
              formData.orderGoods &&
              formData.orderGoods.length > 0 &&
              formData.orderGoods[0].goodsType === 3
            "
            label="型号"
            :min-width="110"
          >
            <template slot-scope="scope">
              <span>{{ scope.row.newDiy.modelName }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="价格" :min-width="120">
            <template slot-scope="scope">
              <i class="asmd"
                >{{ currencyType === "CNY" ? "￥:" : "$:" }}&nbsp;</i
              >
              {{ scope.row.distributorPrice | NumFormat }}
            </template>
          </el-table-column>

          <el-table-column
            align="center"
            label="价格折扣"
            :min-width="120"
            :render-header="renderHeader"
          >
            <template slot-scope="scope">
              <i class="asmd"
                >{{ currencyType === "CNY" ? "￥:" : "$:" }}&nbsp;</i
              >
              {{ scope.row.promotionAmount | NumFormat }}
            </template>
          </el-table-column>

          <el-table-column
            align="center"
            prop="itemCount"
            label="初始导入数量"
            :min-width="120"
          >
          </el-table-column>
          <el-table-column align="center" label="总订购数量" :min-width="120">
            <template slot-scope="scope">
              <div class="batch_input_number">
                <span class="batch_minus" @click="handelMinus(scope.row)">
                  -
                </span>
                <div>
                  <!-- checkhHandleFlag 1 已下单 2 未下单 -->
                  <input
                    class="batch_input1"
                    v-if="checkhHandleFlag == 1"
                    @blur="handelChangeNum(scope.row)"
                    type="text"
                    v-model="scope.row.actualOrderCount"
                    maxlength="8"
                    onkeyup="this.value=this.value.replace(/\D/g,'')"
                    :disabled="
                      disableIsShow ||
                      (scope.row.itemType !== undefined &&
                        scope.row.itemType !== null &&
                        scope.row.itemType === 2)
                    "
                  />
                  <input
                    class="batch_input1"
                    v-else
                    :max="
                      scope.row.goodsType == 1 ? scope.row.maxCount : Infinity
                    "
                    @blur="handelChangeNum(scope.row)"
                    type="text"
                    v-model="scope.row.actualOrderCount"
                    maxlength="8"
                    onkeyup="this.value=this.value.replace(/\D/g,'')"
                    :disabled="
                      disableIsShow ||
                      (scope.row.itemType !== undefined &&
                        scope.row.itemType !== null &&
                        scope.row.itemType === 2)
                    "
                  />
                </div>
                <span class="batch_plusSign" @click="handelPlusSing(scope.row)"
                  >+</span
                >
              </div>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            v-if="orderShow"
            prop="inWarehouseCount"
            label="订购在库数量"
            :min-width="120"
          ></el-table-column>
          <el-table-column
            align="center"
            v-if="orderShow"
            prop="onWayCount"
            label="订购在途数量"
            :min-width="120"
          ></el-table-column>
          <el-table-column
            align="center"
            v-if="orderShow"
            prop="itemLoseCount"
            label="缺货数量"
            :min-width="120"
          ></el-table-column>
          <el-table-column align="center" label="小计" :min-width="120">
            <template slot-scope="scope">
              <i class="asmd"
                >{{ currencyType === "CNY" ? "￥:" : "$:" }}&nbsp;</i
              >
              {{ scope.row.amountTotal | NumFormat }}
            </template>
          </el-table-column>

          <el-table-column
            align="center"
            label="操作"
            v-if="checkhHandleFlag == 2"
            :min-width="150"
          >
            <template slot-scope="scope">
              <el-button
                type="text"
                v-if="
                  (scope.row.itemType === undefined ||
                    scope.row.itemType === null ||
                    scope.row.itemType !== 2) &&
                  scope.row.rules !== undefined &&
                  (scope.row.rules.length > 1 ||
                    (scope.row.rules.length === 1 &&
                      (scope.row.ruleId === undefined ||
                        scope.row.ruleId === null) &&
                      (scope.row.gradeDiscountId === undefined ||
                        scope.row.gradeDiscountId === null)))
                "
                @click="changePromotion(scope.row)"
                >切换活动</el-button
              >
              <el-button
                class="mini-delete-btn"
                type="text"
                @click="handleDelete(scope.$index, scope.row)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>

        <div class="orderMoney" v-if="checkhHandleFlag == 1">
          <span class="cost-info">
            <span class="cost-label">
              &nbsp; 订单总金额（包含配送费） :<i class="asmd"
                >{{ currencyType === "CNY" ? "￥:" : "$:" }} &nbsp;</i
              ></span
            >
            <span class="cost-val asmd" style="font-size: 20px">
              {{ formData.orderAmount | NumFormat }}
            </span>
          </span>
        </div>
      </div>

      <!-- 费用信息 -->
      <div class="box-has-border" v-if="checkhHandleFlag == 1">
        <!-- <div class="text-center title">
					<span>费用信息</span>
				</div> -->
        <!-- <div class="cost-line">
					<span class="cost-info">
						<span class="cost-label">商品总金额 : ￥&nbsp;</span>
						<span class="cost-val"> {{formData.goodsAmount | NumFormat}} </span>
					</span> -
					<span class="cost-info">
						<span class="cost-label">订单折扣 : ￥&nbsp;</span>
						<span class="cost-val"> {{formData.orderDiscount | NumFormat}} </span>
					</span> -
					<span class="cost-info">
						<span class="cost-label">商品折扣 : ￥&nbsp;</span>
						<span class="cost-val"> {{formData.goodsDiscount | NumFormat}} </span>
					</span> +
					<span class="cost-info">
						<span class="cost-label">发票税额 : ￥&nbsp;</span>
						<span class="cost-val"> {{formData.invoiceTax | NumFormat}} </span>
					</span> +
					<span class="cost-info">
						<span class="cost-label">配送费用 : ￥&nbsp;</span>
						<span class="cost-val"> {{formData.distributionMoney | NumFormat}} </span>
					</span>
				</div> -->
        <!-- <div class="cost-line align-right">
					<span class="cost-info ">
						<span class="cost-label"> =&nbsp; 在库订单总额 : ￥&nbsp; </span>
						<span class="cost-val"> {{formData.amountSumInWarehouse | NumFormat}} +</span>
					</span>
					<span class="cost-info ">
						<span class="cost-label"> 在途订单总额 : ￥&nbsp; </span>
						<span class="cost-val"> {{formData.amountSumOnWay | NumFormat}} </span>
					</span>
				</div> -->
        <!-- <div class="cost-line align-right">
					<span class="cost-info">
						<span class="cost-label"> =&nbsp; 订单总金额 : ￥&nbsp; </span>
						<span class="cost-val"> {{formData.orderAmount | NumFormat}} </span>
					</span>
				</div> -->
      </div>

      <div v-if="checkhHandleFlag == 2">
        <div class="tableCenters">
          <el-button
            class="mini-search-btn"
            @click="handleSave('formData')"
            :disabled="disabledShow"
          >
            保存订单
          </el-button>
          <el-button
            class="mini-search-btn"
            @click="handleSubmit('formData')"
            :disabled="disabledShow"
          >
            提交订单
          </el-button>
        </div>
      </div>
    </div>

    <el-dialog :visible="changePromotionShow" :before-close="cancelChange">
      <el-table
        :data="goods.rules"
        :show-header="false"
        ref="table"
        highlight-current-row
        style="text-align: center"
      >
        <el-table-column align="center" width="60">
          <template slot-scope="scope">
            <el-radio
              @change="changeRule(scope.row)"
              style="padding-left: 10px"
              v-model="ruleId"
              :label="scope.row.id"
              >{{ "" }}</el-radio
            >
          </template>
        </el-table-column>
        <el-table-column>
          <template slot-scope="scope">
            <div class="pomotion-rule">
              <span style="font-weight: 500">{{
                "规则：" + scope.row.label
              }}</span>
              <span v-if="scope.row.target !== 1" class="present-label">{{
                !scope.row.isGrade && scope.row.isEnjoy === 1
                  ? "订单同享"
                  : "订单不同享"
              }}</span>
              <div v-if="scope.row.isGrade">
                <span class="span"
                  >{{ "条件："
                  }}{{
                    scope.row.discountBeforeAfter === 1
                      ? "折扣前一次性购买"
                      : "折扣后一次性购买"
                  }}{{
                    scope.row.moneyOrCount === 1
                      ? scope.row.oneBuyMoney + "元"
                      : scope.row.oneBuyCount + "个"
                  }}{{ "折扣：" + scope.row.discount + "%" }}</span
                >
              </div>
              <div v-else>
                <span
                  class="span"
                  v-for="(ruleCondition, index) in scope.row.ruleConditions"
                  :key="ruleCondition.id"
                >
                  {{ "条件" + (index + 1) + "：" + ruleCondition.label }}
                </span>
              </div>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <div class="foot-btn" style="text-align: center">
        <el-button class="mini-search-btn" @click="submitChange"
          >确定</el-button
        >
        <el-button size="mini" @click="cancelChange">取消</el-button>
      </div>
    </el-dialog>

    <el-dialog :visible="promotionShow" :before-close="cancelLook">
      <!-- <el-table :data="goods.rules" border :show-header="true" ref="table" height="40" header-row-class-name="header-row" class="tableCenter orderGoods" style="text-align:center;">
        <el-table-column align="center" label="货品编号"></el-table-column>
        <el-table-column align="center" label="货品名称"></el-table-column>
        <el-table-column align="center" label="数量"></el-table-column>
        <el-table-column align="center" label="价格"></el-table-column>
        <el-table-column align="center" label="折后价格"></el-table-column>
      </el-table> -->
      <div v-loading="loading3">
        <div v-if="gradeRule !== null">
          <el-row align="middle" type="flex" class="bg-purple-light">
            <el-col :span="8" class="bg-purple">
              <div>
                <div style="font-weight: 500">
                  {{ "等级规则：" + gradeRule.policyName }}
                </div>
              </div>
            </el-col>
            <el-col :span="16">
              <div style="padding: 10px">
                <div>{{ gradeRule.description }}</div>
              </div>
            </el-col>
          </el-row>
          <!-- <el-container>
          <el-aside>
            <div class="v-auto-out">
              <div class="auto-in">{{gradeRule.policyName}}</div>
            </div>
          </el-aside>
          <el-main>
            <div class="pomotion-look">
              <div class="half-left">
                <div>{{gradeRule.description}}</div>
              </div>
              <div style="font: 0px/0px sans-serif;clear: both;display: block"> </div>
            </div>
          </el-main>
        </el-container> -->
          <el-table
            border
            :data="gradeRule.goods"
            ref="table"
            header-row-class-name="header-row"
            class="tableCenter orderGoods"
            style="text-align: center"
          >
            <el-table-column
              align="center"
              label="货品编号"
              prop="itemCode"
            ></el-table-column>
            <el-table-column
              align="center"
              label="货品名称"
              prop="itemName"
            ></el-table-column>
            <el-table-column
              align="center"
              label="数量"
              prop="itemCount"
            ></el-table-column>
            <el-table-column
              align="center"
              label="价格"
              prop="distributorPrice"
            >
              <template slot-scope="scope">
                <i class="asmd"
                  >{{ currencyType === "CNY" ? "￥:" : "$:" }}&nbsp;</i
                >
                {{ scope.row.distributorPrice | NumFormat }}
              </template>
            </el-table-column>
            <el-table-column align="center" label="折后价格" prop="actualPrice">
              <template slot-scope="scope">
                <i class="asmd"
                  >{{ currencyType === "CNY" ? "￥:" : "$:" }}&nbsp;</i
                >
                <!-- <i class="asmd">￥:&nbsp;</i> -->
                {{ scope.row.actualPrice | NumFormat }}
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div v-if="promotionRules.length > 0">
          <div v-for="promotionRule in promotionRules" :key="promotionRule.id">
            <el-row align="middle" type="flex" class="bg-purple-light">
              <el-col :span="8" class="bg-purple">
                <div>
                  <div style="font-weight: 500">
                    {{ "活动规则：" + promotionRule.label }}
                  </div>
                </div>
              </el-col>
              <el-col :span="13">
                <div style="padding: 10px">
                  <div
                    v-for="(condition, index) in promotionRule.conditions"
                    :key="condition.id"
                  >
                    {{ "活动条件" + (index + 1) + "：" + condition.label }}
                  </div>
                </div>
              </el-col>
              <el-col :span="3">
                <div
                  style="text-align: center"
                  v-if="
                    promotionRule.selectCondition !== undefined &&
                    ((promotionRule.selectCondition.isSpecial !== undefined &&
                      promotionRule.selectCondition.isSpecial === 1 &&
                      promotionRule.selectCondition.isEnjoy === 1 &&
                      promotionRule.selectCondition.reduceOrPresent === 2) ||
                      ((promotionRule.selectCondition.isSpecial === undefined ||
                        promotionRule.selectCondition.isSpecial === 0) &&
                        promotionRule.selectCondition.reduceOrPresent === 2))
                  "
                >
                  <el-button
                    class="mini-present-btn"
                    :loading="selectPresentLoading"
                    @click="selectPresent(promotionRule)"
                    >查看赠品</el-button
                  >
                </div>
              </el-col>
            </el-row>
            <!-- <el-container >
            <el-aside>
              <div class="v-auto-out">
                <div class="auto-in">{{"规则："+promotionRule.label}}</div>
              </div>
            </el-aside>
            <el-main>
              <div class="pomotion-look">
                <div class="half-left">
                  <div v-for="(condition,index) in promotionRule.conditions" :key="condition.id">{{"条件"+(index+1)+"："+condition.label}}</div>
                </div>
                <div class="half-right" v-if="promotionRule.selectCondition !== undefined && ((promotionRule.selectCondition.isSpecial !== undefined && promotionRule.selectCondition.isSpecial === 1 && promotionRule.selectCondition.isEnjoy === 1 && promotionRule.selectCondition.reduceOrPresent === 2) || ((promotionRule.selectCondition.isSpecial === undefined || promotionRule.selectCondition.isSpecial === 0) && promotionRule.selectCondition.reduceOrPresent === 2))">
                  <el-button class="mini-present-btn" :loading="selectPresentLoading" @click="selectPresent(promotionRule)">查看赠品</el-button>
                </div>
                <div style="font: 0px/0px sans-serif;clear: both;display: block"> </div>
              </div>
            </el-main>
          </el-container> -->
            <el-table
              border
              :data="promotionRule.goods"
              ref="table"
              header-row-class-name="header-row"
              class="tableCenter orderGoods"
              style="text-align: center"
            >
              <el-table-column
                align="center"
                label="货品编号"
                prop="itemCode"
              ></el-table-column>
              <el-table-column
                align="center"
                label="货品名称"
                prop="itemName"
              ></el-table-column>
              <el-table-column
                align="center"
                label="数量"
                prop="actualOrderCount"
              >
                <template slot-scope="scope">
                  {{
                    scope.row.onWayAttendEventFlag === 1
                      ? scope.row.actualOrderCount
                      : scope.row.inWarehouseCount
                  }}
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="价格"
                prop="distributorPrice"
              >
                <template slot-scope="scope">
                  <i class="asmd"
                    >{{ currencyType === "CNY" ? "￥:" : "$:" }}&nbsp;</i
                  >
                  {{ scope.row.distributorPrice | NumFormat }}
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="折后价格"
                prop="actualPrice"
              >
                <template slot-scope="scope">
                  <i class="asmd"
                    >{{ currencyType === "CNY" ? "￥:" : "$:" }}&nbsp;</i
                  >
                  {{ scope.row.actualPrice | NumFormat }}
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
    </el-dialog>

    <el-dialog :visible="selectPresentShow" :before-close="cancelPresentChange">
      <div v-loading="selectPresentLoading">
        <div class="pomotion-present" v-if="presentRule !== null">
          <div class="half-left">
            <span class="present-label">{{ this.presentRule.label }}</span>
          </div>
          <div class="half-right">
            <div style="margin-left: 20px; font-weight: 500">
              最多可选{{ presentTotal }}件，已选{{ selectCount }}件
            </div>
          </div>
          <div
            style="font: 0px/0px sans-serif; clear: both; display: block"
          ></div>
        </div>
        <el-table
          :data="presentRule.presentGoodss"
          :show-header="true"
          ref="table"
          header-row-class-name="header-row"
          class="tableCenter orderGoods"
          style="text-align: center"
        >
          <el-table-column
            align="center"
            label="货品编号"
            prop="itemCode"
          ></el-table-column>
          <el-table-column
            align="center"
            label="货品名称"
            prop="itemName"
          ></el-table-column>
          <el-table-column align="center" label="总订购数量" :min-width="120">
            <template slot-scope="scope">
              <div class="batch_input_number">
                <span
                  class="batch_minus"
                  @click="handelPresentMinus(scope.row)"
                >
                  -
                </span>
                <div>
                  <!-- checkhHandleFlag 1 已下单 2 未下单 -->
                  <input
                    class="batch_input1"
                    @blur="handelPresentNum(scope.row)"
                    type="text"
                    v-model="scope.row.itemCount"
                    maxlength="8"
                    onkeyup="this.value=this.value.replace(/\D/g,'')"
                    :disabled="disableIsShow"
                  />
                </div>
                <span
                  class="batch_plusSign"
                  @click="handelPresentPlusSing(scope.row)"
                  >+</span
                >
              </div>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="剩余可选数量"
            prop="presentCount"
          ></el-table-column>
          <el-table-column
            align="center"
            label="在库数量"
            prop="numInWarehouse"
          ></el-table-column>
        </el-table>
        <div class="foot-btn" style="text-align: center">
          <el-button
            :loading="changePresentLoading"
            class="mini-search-btn"
            @click="submitPresentChange"
            >确定</el-button
          >
          <el-button size="mini" @click="cancelPresentChange">取消</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// import FileSaver from 'file-saver'
// import XLSX from 'xlsx'
// import Vue from 'vue'
// import axios from 'axios'
import { parseTime } from "@/utils/index";
import { getToken } from "@/utils/auth";
import { saleStatusFormatter } from "@/views/order/orderUtils";

export default {
  name: "batchChannelRedact",
  data() {
    // 手机正则验证
    const validatePhone = (rule, value, callback) => {
      if (
        /^(13[0-9]|14[5-9]|15[012356789]|166|17[0-8]|18[0-9]|19[8-9])[0-9]{8}$/.test(
          value
        ) === false
      ) {
        callback(new Error("请输入正确的手机号"));
      } else {
        callback();
      }
    };
    return {
      allIds: [],
      moqCount: false,
      provinceShow: false,
      cityShow: false,
      townShow: false,
      invoiceShow: true,
      disableIsShow: false,
      editorShow: false,
      noHandle: false,
      orderHandle: false,
      disabledShow: false,
      changePromotionShow: false,
      isChangePromotion: false,
      promotionShow: false,
      currencyType: "CNY",
      goods: [
        {
          rules: [],
        },
      ],
      ruleId: "",
      rule: "",
      Num: [],
      orderTypeList: [], // 订单类型arr
      orderDistriList: [], // 配送方式arr
      orderDeleteIds: "", // 货品删除ids
      regionParams: {
        // 用来请求region数据
        parentId: 0,
        size: 300,
        page: 1,
      },
      RegionForChose: {
        // 国家
        countryId: [],
        // 被选region
        provinceId: [],
        cityId: [],
        districtId: [],
      },
      pageInfo: {
        page: 1,
        count: 10,
      },
      FirstReq: true, // 生命周期加载初始数据时，cityId不会被watcher清洗
      tempData: {},
      formData: {
        orderTypeValue: "", // 订单类型
        distributionId: "", // 配送方式
        invoiceType: "", // 发票类型
        payWay: "", // 支付方式
        isInvoice: 0, // 是否开票
        countryId: 0, // 国家id
        provinceId: "", // 省份id
        cityId: "", // 城市id
        districtId: "", // 区id
        importOrderId: "",
        zipCode: "", // 邮编
      },
      rules: {
        // 必填输入提示
        payWay: [
          { required: true, message: "请选择支付方式", trigger: "change" },
        ],
        distributionId: [
          { required: true, message: "请选择配送方式", trigger: "change" },
        ],
        orderTypeValue: [
          { required: true, message: "请选择订单类型", trigger: "change" },
        ],
        userName: [
          { required: true, message: "请输入收货人名字", trigger: "blur" },
        ],
        countryId: [
          { required: true, message: "请选择地区", trigger: "change" },
        ],
        address: [
          { required: true, message: "请输入街道地址", trigger: "blur" },
        ],
        mobile: [
          { required: true, message: "请输入手机号码", trigger: "blur" },
          { validator: validatePhone },
        ],
      },
      arr1: [],
      Authorization: "",
      importHeaders: {
        // 'content-disposition':'attachment',
        // 'content-disposition':'attachment;filename=total.xls',
        // dataType: 'jsonp',
        // 'Access-Control-Allow-Origin': '*',
        Accept: "application/json",
        enctype: "multipart/form-data",
        Platform: "web",
        Version: "1.0.0",
        Authorization: "",
        "Content-Type": "application/x-www-form-urlencoded",
      },
      data: {
        importOrderId: "",
      },
      loading: "",
      loading2: "",
      loading1: "",
      loading3: false,
      selectPresentLoading: false,
      selectPresentShow: false,
      modifyGoodsList: [],
      maxCount: "",
      saveWin: false, // 提交订单时保存订单是否成功
      orderShow: "",
      promotionIds: {
        ruleIds: [],
      },
      promotionRules: [],
      gradeRule: null,
      selectCount: 0,
      presentTotal: 0,
      presentRule: {
        presentGoodss: [],
      },
      isPresentChange: false,
      changePresentLoading: false,
      changeCount: false,
      counts: [],
      isError: false,
      orderTypeValue: "",
      isDistribution: false,
    };
  },
  computed: {
    checkhHandleFlag() {
      return this.$route.query.handleFlag; // 1、已下单 2、未下单
    },
  },
  created() {
    this.orderTypeArr(); // 订单类型
    this.orderImportOrder(); // 订单详情
    this.importHeaders.Authorization = getToken();
    if (this.$route.query.handleFlag == 1) {
      // 1、已下单 2、未下单
      this.editorShow = true;
      this.disableIsShow = true;
      this.orderHandle = true;
      this.noHandle = false;
    } else if (this.$route.query.handleFlag == 2) {
      this.noHandle = true;
      this.orderHandle = false;
      this.disableIsShow = false;
      this.disabledShow = false;
    }
    this.$http.regionListById(this, this.regionParams).then((res) => {
      // 获取国家
      this.RegionForChose.countryId = res.data.list;
    });
  },
  methods: {
    diyPicture(row) {
      var host = window.location.host;
      if (host === "admin.bat.com") {
        // 正式
        window.open(
          "https://www.bat.com/#/customizeDiy?goodsId=" +
            row.goodsId +
            "&modelId=" +
            row.newDiy.modelId +
            "&brandId=" +
            row.newDiy.brandId +
            "&materialId=" +
            row.newDiy.materialId +
            "&pictureId=" +
            row.newDiy.pictureId +
            "&url=" +
            window.location.href
        );
      } else {
        // 测试
        window.open(
          "https://test.bat.com/proscenium/#/customizeDiy?goodsId=" +
            row.goodsId +
            "&modelId=" +
            row.newDiy.modelId +
            "&brandId=" +
            row.newDiy.brandId +
            "&materialId=" +
            row.newDiy.materialId +
            "&pictureId=" +
            row.newDiy.pictureId +
            "&url=" +
            window.location.href
        );
      }
    },
    selectPresent(rule) {
      this.presentRule = rule;
      this.selectPresentLoading = true;
      this.selectPresentShow = true;
      let selectRatio = 0.0;
      if (this.presentRule.target !== 1 && this.presentRule.isCompose === 1) {
        // 组合活动
        this.presentRule.selectCondition.groupConditions.forEach(
          (groupCondition) => {
            if (
              this.presentRule.moneyOrCount === 1 &&
              (selectRatio === 0 ||
                selectRatio >
                  groupCondition.totalMoney / groupCondition.oneBuyMoney)
            ) {
              if (
                this.presentRule.selectCondition.reductionPresentIsAdd === 1
              ) {
                // 累计且叠加
                selectRatio =
                  groupCondition.totalMoney / groupCondition.oneBuyMoney;
              } else {
                selectRatio = 1;
              }
            } else if (
              this.presentRule.moneyOrCount === 2 &&
              (selectRatio === 0 ||
                selectRatio >
                  groupCondition.totalCount / groupCondition.oneBuyCount)
            ) {
              if (
                this.presentRule.selectCondition.reductionPresentIsAdd === 1
              ) {
                // 累计且叠加
                selectRatio =
                  groupCondition.totalCount / groupCondition.oneBuyCount;
              } else {
                selectRatio = 1;
              }
            }
          }
        );
      } else {
        if (this.presentRule.moneyOrCount === 1) {
          // 金额
          if (this.presentRule.selectCondition.reductionPresentIsAdd === 1) {
            // 累计且叠加
            selectRatio =
              this.presentRule.selectCondition.totalMoney /
              this.presentRule.selectCondition.oneBuyMoney;
          } else {
            // 不叠加
            if (
              this.presentRule.target === 1 ||
              this.presentRule.isAddUp === 1
            ) {
              selectRatio = 1;
            } else {
              this.presentRule.goods.forEach((item) => {
                if (
                  item.itemType === undefined ||
                  item.itemType === null ||
                  item.itemType !== 2
                ) {
                  selectRatio += 1;
                }
              });
            }
          }
        } else {
          // 数量
          if (this.presentRule.selectCondition.reductionPresentIsAdd === 1) {
            // 累计且叠加
            selectRatio =
              this.presentRule.selectCondition.totalCount /
              this.presentRule.selectCondition.oneBuyCount;
          } else {
            // 不叠加
            if (
              this.presentRule.target === 1 ||
              this.presentRule.isAddUp === 1
            ) {
              selectRatio = 1;
            } else {
              this.presentRule.goods.forEach((item) => {
                if (
                  item.itemType === undefined ||
                  item.itemType === null ||
                  item.itemType !== 2
                ) {
                  selectRatio += 1;
                }
              });
            }
          }
        }
      }
      this.presentTotal = Math.floor(
        this.presentRule.selectCondition.presentCount * selectRatio
      ); // 赠送总数
      this.presentRule.presentGoodss = []; // 赠品列表
      this.selectCount = 0; // 已选赠品总数
      this.$api
        .get(this, "admin/u/po/marketing/promotion/getPresentGoods", {
          conditionId: this.presentRule.selectCondition.id,
          distributorId: this.formData.distributorId,
        })
        .then((res) => {
          // 货品赠品列表
          if (res.code === 0) {
            res.goods.forEach((item) => {
              let b = true;
              this.presentRule.goods.forEach((goodsItem) => {
                if (
                  goodsItem.itemType === 2 &&
                  item.itemId === goodsItem.itemId
                ) {
                  // 已选赠品
                  this.selectCount = this.selectCount + goodsItem.itemCount;
                  item.itemCount = goodsItem.itemCount;
                  b = false;
                  this.presentRule.presentGoodss.push(item);
                }
              });
              if (b) {
                item.itemCount = 0;
                this.presentRule.presentGoodss.push(item);
              }
            });
          }
          this.selectPresentLoading = false;
        });
    },
    cancelLook() {
      this.promotionShow = false;
    },
    lookPromotion() {
      if (this.changeCount) {
        this.$message.info({
          message: "订购数量有更新,请先保存订单重新计算后再查看活动",
          duration: 3 * 1000,
        });
        return;
      }
      if (this.isChangePromotion) {
        this.$message.info({
          message: "有切换过活动,请刷新重新计算活动后再查看活动！",
          duration: 3 * 1000,
        });
        return;
      }
      this.loading3 = true;
      this.promotionShow = true;
      this.gradeRule = null;
      this.promotionRules = [];
      this.$api
        .post(
          this,
          "admin/u/po/marketing/promotion/lookRule",
          this.promotionIds
        )
        .then((res) => {
          if (res.code === 0) {
            if (
              res.rules !== undefined &&
              res.rules !== null &&
              res.rules.length > 0
            ) {
              res.rules.forEach((rule) => {
                rule.goods = [];
                let totalCount = 0.0;
                let totalMoney = 0.0;
                let totalCounts = [];
                let totalMoneys = [];
                if (rule.target !== 1 && rule.isCompose === 1) {
                  // 组合情况
                  rule.conditions[0].groupConditions.forEach(
                    (groupCondition) => {
                      totalCounts.push(0.0);
                      totalMoneys.push(0.0);
                    }
                  );
                }
                this.formData.orderGoods.forEach((item) => {
                  // 组装活动规则对应的货品列表
                  if (
                    (rule.target === 1 &&
                      item.orderRuleId !== undefined &&
                      item.orderRuleId !== null &&
                      item.orderRuleId === rule.id) ||
                    (rule.target !== 1 &&
                      item.ruleId !== undefined &&
                      item.ruleId !== null &&
                      item.ruleId === rule.id)
                  ) {
                    if (
                      item.itemType === undefined ||
                      item.itemType === null ||
                      item.itemType !== 2
                    ) {
                      if (rule.target !== 1 && rule.isCompose === 1) {
                        // 组合情况
                        rule.conditions[0].groupConditions.forEach(
                          (groupCondition, index) => {
                            groupCondition.goodsItemIds =
                              groupCondition.goodsItemIds instanceof Array
                                ? groupCondition.goodsItemIds
                                : groupCondition.goodsItemIds.split(",");
                            if (
                              (rule.target === 2 &&
                                groupCondition.goodsItemIds.indexOf(
                                  String(item.goodsId)
                                ) >= 0) ||
                              (rule.target === 3 &&
                                groupCondition.goodsItemIds.indexOf(
                                  String(item.itemId)
                                ) >= 0)
                            ) {
                              // 商品
                              if (
                                item.onWayAttendEventFlag === 1 ||
                                item.inWarehouseCount === undefined ||
                                item.inWarehouseCount === null ||
                                item.inWarehouseCount === 0
                              ) {
                                // 在途也参与活动
                                totalCounts[index] =
                                  totalCounts[index] + item.actualOrderCount;
                                totalMoneys[index] =
                                  totalMoneys[index] +
                                  item.actualOrderCount * item.actualPrice;
                              } else {
                                totalCounts[index] =
                                  totalCounts[index] + item.inWarehouseCount;
                                totalMoneys[index] =
                                  totalMoneys[index] +
                                  item.inWarehouseCount * item.actualPrice;
                              }
                            }
                          }
                        );
                      } else {
                        if (item.onWayAttendEventFlag === 1) {
                          // 在途也参与活动
                          totalCount = totalCount + item.actualOrderCount;
                          totalMoney =
                            totalMoney +
                            item.actualOrderCount * item.actualPrice;
                        } else {
                          totalCount = totalCount + item.inWarehouseCount;
                          totalMoney =
                            totalMoney +
                            item.inWarehouseCount * item.actualPrice;
                        }
                      }
                    }
                    rule.goods.push(item);
                  }
                });
                let selectTotal = 0.0; // 选择的条件
                rule.conditions.forEach((condition) => {
                  // 针对赠品计算，选出最优活动规则条件
                  let conditionTotal = 0.0;
                  let isCheck = true;
                  if (rule.moneyOrCount === 1) {
                    // 按金额
                    if (rule.target !== 1 && rule.isCompose === 1) {
                      // 组合情况
                      condition.groupConditions.forEach(
                        (groupCondition, index) => {
                          if (totalMoneys[index] < groupCondition.oneBuyMoney) {
                            isCheck = false;
                            return;
                          }
                          groupCondition.totalMoney = totalMoneys[index];
                          conditionTotal =
                            conditionTotal + groupCondition.oneBuyMoney;
                        }
                      );
                    } else {
                      if (totalMoney < condition.oneBuyMoney) {
                        isCheck = false;
                      }
                      condition.totalMoney = totalMoney;
                      conditionTotal = condition.oneBuyMoney;
                    }
                  } else {
                    // 按数量
                    if (rule.target !== 1 && rule.isCompose === 1) {
                      // 组合情况
                      condition.groupConditions.forEach(
                        (groupCondition, index) => {
                          if (totalCounts[index] < groupCondition.oneBuyCount) {
                            isCheck = false;
                            return;
                          }
                          groupCondition.totalCount = totalCounts[index];
                          conditionTotal =
                            conditionTotal + groupCondition.oneBuyCount;
                        }
                      );
                    } else {
                      if (totalCount < condition.oneBuyCount) {
                        isCheck = false;
                      }
                      condition.totalCount = totalCount;
                      conditionTotal = condition.oneBuyCount;
                    }
                  }
                  if (isCheck && selectTotal < conditionTotal) {
                    selectTotal = conditionTotal;
                    rule.selectCondition = condition;
                  }
                });
              });
              this.promotionRules = res.rules;
            }
            if (res.gradeRule !== undefined && res.gradeRule !== null) {
              res.gradeRule.goods = [];
              this.formData.orderGoods.forEach((item) => {
                if (
                  item.gradeDiscountId !== undefined &&
                  item.gradeDiscountId !== null
                ) {
                  // 商品等级折扣活动Id
                  res.gradeRule.goods.push(item);
                }
              });
              this.gradeRule = res.gradeRule;
            }
          }
          this.loading3 = false;
          this.selectPresentLoading = false;
        });
    },
    submitChange() {
      let request = {
        id: this.goods.id,
        importOrderId: this.formData.importOrderId,
        count: this.goods.itemCount,
      };
      if (!this.rule.isGrade) {
        this.goods.ruleId = this.rule.id;
        this.goods.gradeDiscountId = null;
        request.ruleId = this.rule.id;
      } else {
        this.goods.ruleId = null;
        this.goods.gradeDiscountId = this.rule.id;
        request.gradeDiscountId = this.rule.id;
      }
      this.$api
        .put(this, "admin/u/p/importOrder/order/change/promotion", request)
        .then((res) => {
          if (res.code === 0) {
            this.$message.success({
              message:
                "切换活动完成,如所有活动商品已切换完成，请刷新重新进行活动计算,注意：所切换的活动如不满足，将系统重新推荐",
              duration: 3 * 1000,
            });
            // this.loading2 = true
            this.changePromotionShow = false;
            this.isChangePromotion = true;
            // this.orderImportOrder();
          }
        });
    },
    cancelChange() {
      this.changePromotionShow = false;
      this.ruleId = "";
      this.rule = "";
    },
    submitPresentChange() {
      if (this.isPresentChange) {
        this.changePresentLoading = true;
        let changePresent = {
          importOrderId: this.formData.importOrderId,
          presentGoodss: [],
        };
        this.presentRule.presentGoodss.forEach((presentGoods) => {
          if (
            presentGoods.itemCount !== undefined &&
            presentGoods.itemCount !== null &&
            presentGoods.itemCount !== "" &&
            presentGoods.itemCount !== "0" &&
            presentGoods.itemCount !== 0
          ) {
            changePresent.presentGoodss.push({
              actualOrderCount: presentGoods.itemCount,
              goodsId: presentGoods.goodsId,
              itemId: presentGoods.itemId,
              itemType: 2,
              ruleId: this.presentRule.id,
            });
          }
        });
        this.$api
          .put(this, "admin/u/p/importOrder/present", changePresent)
          .then((res) => {
            if (res.code === 0) {
              this.$message.success({
                message: "赠品更新成功",
                duration: 3 * 1000,
              });
              this.selectPresentShow = false;
              this.loading2 = true;
              this.selectPresentLoading = true;
              this.orderImportOrder();
            }
            this.changePresentLoading = false;
          });
      } else {
        this.changePresentLoading = false;
        this.selectPresentShow = false;
      }
    },
    cancelPresentChange() {
      this.selectPresentShow = false;
      this.isPresentChange = false;
    },
    changeRule(val) {
      //切换活动
      this.rule = val;
      this.ruleId = this.rule.id;
    },
    changePromotion(raw) {
      this.goods = raw;
      if (this.goods.ruleId !== undefined && this.goods.ruleId !== null) {
        this.ruleId = this.goods.ruleId;
      } else if (
        this.goods.gradeDiscountId !== undefined &&
        this.goods.gradeDiscountId !== null
      ) {
        this.ruleId = this.goods.gradeDiscountId;
      }
      this.changePromotionShow = true;
    },
    handelPresentPlusSing(row) {
      if (this.checkhHandleFlag == 2) {
        row.itemCount = Number(row.itemCount) + 1;
        this.selectCount = this.selectCount + 1;
        if (row.itemCount > row.presentCount) {
          // 赠送数量不能大于赠送总数量
          this.$message.warning(
            "货品" + row.itemCode + "赠送数量不能大于" + row.presentCount
          );
          row.itemCount = Number(row.itemCount) - 1;
          this.selectCount = this.selectCount - 1;
        }
        if (this.selectCount > this.presentTotal) {
          this.$message.warning("赠送数量不能大于赠送总数量");
          row.itemCount = Number(row.itemCount) - 1;
          this.selectCount = this.selectCount - 1;
        }
        this.isPresentChange = true;
      }
    },
    handelPresentMinus(row) {
      if (this.checkhHandleFlag == 2) {
        if (row.itemCount > 0) {
          this.isPresentChange = true;
          row.itemCount = Number(row.itemCount) - 1;
          this.selectCount = this.selectCount - 1;
        }
      }
    },
    // ======== 操作 ========
    handelMinus(row) {
      // ..减号操作
      if (
        this.checkhHandleFlag == 2 &&
        (row.itemType === undefined ||
          row.itemType === null ||
          row.itemType !== 2)
      ) {
        // ..已下单不能操作
        row.actualOrderCount--;
        if (this.formData.orderTypeValue === "XSDD10_SYS") {
          // ..订单为MTO订单
          if (row.actualOrderCount < Number(row.moq)) {
            this.$message.warning("订购数量不可小于" + Number(row.moq));
            row.actualOrderCount = Number(row.moq);
          }
        } else if (this.formData.orderTypeValue === "XSDD04_SYS") {
          // ..订单为直运订单
          if (row.actualOrderCount < Number(row.moq)) {
            this.$message.warning("订购数量不可小于" + Number(row.moq));
            row.actualOrderCount = Number(row.moq);
          }
        } else {
          if (row.actualOrderCount > row.maxCount) {
            this.$message({
              message: "库存不足",
              type: "warning",
              duration: 3 * 1000,
            });
            row.actualOrderCount = row.maxCount;
          }
        }
      }
      this.changeCount = false;
      this.formData.orderGoods.forEach((item, index) => {
        if (this.counts[index] !== item.actualOrderCount) {
          this.changeCount = true;
        }
      });
    },
    handelPlusSing(row) {
      // ..加号操作
      if (
        this.checkhHandleFlag == 2 &&
        (row.itemType === undefined ||
          row.itemType === null ||
          row.itemType !== 2)
      ) {
        // ..已下单不能操作
        row.actualOrderCount++;
        if (this.formData.orderTypeValue === "XSDD10_SYS") {
          // ..订单为MTO订单
          if (row.actualOrderCount < Number(row.moq)) {
            this.$message.warning("订购数量不可小于" + Number(row.moq));
            row.actualOrderCount = Number(row.moq);
          }
        } else if (this.formData.orderTypeValue === "XSDD04_SYS") {
          // ..订单为直运订单
          if (row.actualOrderCount < Number(row.moq)) {
            this.$message.warning("订购数量不可小于" + Number(row.moq));
            row.actualOrderCount = Number(row.moq);
          }
        } else {
          if (row.actualOrderCount > row.maxCount) {
            this.$message({
              message: "库存不足",
              type: "warning",
              duration: 3 * 1000,
            });
            row.actualOrderCount = row.maxCount;
          }
        }
      }
      this.changeCount = false;
      this.formData.orderGoods.forEach((item, index) => {
        if (this.counts[index] !== item.actualOrderCount) {
          this.changeCount = true;
        }
      });
    },
    handelPresentNum(row) {
      if (row.itemCount > row.presentCount) {
        // 赠送数量不能大于赠送总数量
        this.$message.warning(
          "货品" + row.itemCode + "赠送数量不能大于" + row.presentCount
        );
        row.itemCount = row.presentCount;
      }
      let count = 0;
      this.presentRule.presentGoodss.forEach((goods) => {
        count = count + Number(goods.itemCount);
      });
      if (count > this.presentTotal) {
        this.$message.warning("赠送数量不能大于赠送总数量");
        row.itemCount = Number(row.itemCount) - (count - this.presentTotal);
        this.selectCount = this.presentTotal;
      } else {
        this.selectCount = count;
      }
      this.isPresentChange = true;
    },
    handelChangeNum(row, index) {
      // ..总订购数量
      if (this.formData.orderTypeValue === "XSDD10_SYS") {
        // ..订单为MTO订单
        if (row.actualOrderCount < Number(row.moq)) {
          this.$message.warning("订购数量不可小于" + Number(row.moq));
          setTimeout(() => {
            row.actualOrderCount = Number(row.moq);
          }, 300);
        }
        if (row.actualOrderCount === "") {
          setTimeout(() => {
            row.actualOrderCount = Number(row.moq);
          }, 300);
        }
      } else if (this.formData.orderTypeValue === "XSDD04_SYS") {
        // ..订单为直运订单
        if (row.actualOrderCount < Number(row.moq)) {
          this.$message.warning("订购数量不可小于" + Number(row.moq));
          setTimeout(() => {
            row.actualOrderCount = Number(row.moq);
          }, 300);
        }
        if (row.actualOrderCount == "") {
          setTimeout(() => {
            row.actualOrderCount = Number(row.moq);
          }, 300);
        }
      } else if (this.formData.orderTypeValue === "DIY_SYS") {
        // 定制订单
      } else {
        if (row.actualOrderCount > row.maxCount) {
          this.$message.warning(
            row.itemName + "库存不足," + "当前最大可购数量为" + row.maxCount
          );
          this.isError = true;
          setTimeout(() => {
            row.actualOrderCount = row.maxCount;
            this.isError = false;
          }, 300);
        }
        if (row.actualOrderCount == "") {
          setTimeout(() => {
            row.actualOrderCount = Number(row.moq);
          }, 300);
        }
      }
      this.changeCount = false;
      this.formData.orderGoods.forEach((item, index) => {
        if (this.counts[index] !== Number(item.actualOrderCount)) {
          this.changeCount = true;
        }
      });
    },

    orderTypeValueChange(val) {
      // ..订单类型变化
      if (val === "XSDD10_SYS") {
        // ..MTO订单
        this.$message.warning(
          "订单类型已变更为MTO订单，可享有的活动可能发生变化"
        );
      } else if (val === "XSDD04_SYS") {
        // ..直运订单
        this.$message.warning(
          "订单类型已变更为直运订单，可享有的活动可能发生变化"
        );
      } else {
        this.$message.warning("订单类型已变更，可享有的活动可能发生变化");
      }
    },
    countryIdChange(event) {
      this.$http
        .regionListById(this, {
          page: 1,
          size: 300,
          parentId: event,
        })
        .then((res) => {
          // 获取省份
          if (res.data.list.length > 0) {
            this.provinceShow = true;

            this.RegionForChose.provinceId = res.data.list;
            this.formData.provinceId = res.data.list[0].id;
            if (res.data.list[0].haveNext === 1) {
              this.$http
                .regionListById(this, {
                  page: 1,
                  size: 200,
                  parentId: this.formData.provinceId,
                })
                .then((res) => {
                  this.RegionForChose.cityId = res.data.list;
                  this.formData.cityId = this.RegionForChose.cityId[1].id;
                });
              this.cityShow = true;
            } else {
              this.formData.cityId = 0;
              this.cityShow = false;
            }
          } else {
            this.provinceShow = false;
            this.cityShow = false;
            this.townShow = false;

            this.formData.provinceId = 0;
            this.formData.cityId = 0;
            this.formData.districtId = 0;
          }
        });
    },
    provinceIdChange(event) {
      // 省
      this.$http
        .regionListById(this, {
          page: 1,
          size: 200,
          parentId: event,
        })
        .then((res) => {
          if (res.success) {
            this.cityShow = true;
            this.RegionForChose.cityId = res.data.list;
            this.formData.cityId = res.data.list[0].id;
            if (res.data.list[0].haveNext === 1) {
              this.$http
                .regionListById(this, {
                  page: 1,
                  size: 200,
                  parentId: this.formData.cityId,
                })
                .then((res) => {
                  this.RegionForChose.districtId = res.data.list;
                  this.formData.districtId =
                    this.RegionForChose.districtId[1].id;
                });
              this.townShow = true;
            } else {
              this.formData.districtId = 0;
              this.townShow = false;
            }
          }
        });
      this.orderDistribution();
    },
    cityIdChange(event) {
      // 市
      this.$http
        .regionListById(this, {
          page: 1,
          size: 200,
          parentId: event,
        })
        .then((res) => {
          if (res.data.list.length === 0) {
            this.townShow = false;
            this.formData.districtId = 0;
          } else {
            this.RegionForChose.districtId = res.data.list;
            this.formData.districtId = this.RegionForChose.districtId[1].id;
          }
        });
      this.orderDistribution();
    },

    districtIdChange(event) {
      // 区
      this.orderDistribution();
    },

    exportExcel() {
      // 导出缺货清单
      this.$api
        .exportData(this, "admin/u/p/importOrder/orderDownLoad", {
          importOrderId: this.formData.importOrderId,
          orderFlag: 2,
        })
        .then((res) => {
          const content = res;
          const blob = new Blob([content], {
            type: "application/ms-excel",
          });
          const url = window.URL.createObjectURL(blob);
          if ("download" in document.createElement("a")) {
            const link = document.createElement("a");
            link.style.display = "none";
            link.href = url;
            link.setAttribute("download", "excel.xls");
            document.body.appendChild(link);
            link.click();
          } else {
            navigator.msSaveBlob(blob, "excel.xls");
          }
        });
    },

    exportDownload() {
      // 导出下单清单操作
      this.$api
        .exportData(this, "admin/u/p/importOrder/orderDownLoad", {
          importOrderId: this.formData.importOrderId,
          orderFlag: 1,
        })
        .then((res) => {
          const content = res;
          const blob = new Blob([content], {
            type: "application/ms-excel",
          });
          const url = window.URL.createObjectURL(blob);
          if ("download" in document.createElement("a")) {
            const link = document.createElement("a");
            link.style.display = "none";
            link.href = url;
            link.setAttribute("download", "excel.xls");
            document.body.appendChild(link);
            link.click();
          } else {
            navigator.msSaveBlob(blob, "excel.xls");
          }
        });
    },
    onEdit(item) {
      // 订单号查看操作
      this.$router.push({
        name: "orderDetail",
        query: { orderId: item, batchPage: true, type: 1 },
      });
    },

    fileDownload(data, fileName) {
      const blob = new Blob([data], {
        type: "application/ms-excel",
      });
      const filename = fileName || "filename.xls";
      if (typeof window.navigator.msSaveBlob !== "undefined") {
        window.navigator.msSaveBlob(blob, filename);
      } else {
        var blobURL = window.URL.createObjectURL(blob);
        var tempLink = document.createElement("a");
        tempLink.style.display = "none";
        tempLink.href = blobURL;
        tempLink.setAttribute("download", filename);
        if (typeof tempLink.download === "undefined") {
          tempLink.setAttribute("target", "_blank");
        }
        document.body.appendChild(tempLink);
        tempLink.click();
        document.body.removeChild(tempLink);
        window.URL.revokeObjectURL(blobURL);
      }
    },

    cancel() {
      // 返回操作
      this.$router.push({ name: "batchChannel" });
    },

    handleDelete(index, row) {
      // 商品信息删除操作
      this.arr1.push(row.id);
      this.orderDeleteIds = this.arr1;
      this.formData.orderGoods.splice(index, 1);
      this.changeCount = true;
    },

    handleSave(formData) {
      // 保存操作
      this.saveWin = false;
      this.saveOrderForm();
    },

    handleSubmit() {
      // 提交操作
      if (this.formData.orderGoods.length !== 0) {
        for (let i = 0; i < this.formData.orderGoods.length; i++) {
          let item = this.formData.orderGoods[i];
          if (
            item.goodsType === 3 &&
            (item.newDiy.image === undefined ||
              item.newDiy.image === null ||
              item.newDiy.image === "")
          ) {
            // 新定制,定制图片为空
            this.$message.error(
              "订单详情第" +
                (i + 1) +
                "行还未定制图片，请先定制图片完成后在提交订单"
            );
            return;
          }
        }
      }
      this.saveWin = true;
      this.saveOrderForm();
    },

    saveOrderForm() {
      // 保存订单操作
      if (this.isError) {
        return;
      }
      const formArr = ["formData1", "formData2", "formData3", "formData4"]; // 四个form表单的ref
      var resultArr = []; // 用来接受返回结果的数组
      var _self = this;
      function checkForm(formName) {
        // 封装验证表单的函数
        var result = new Promise(function (resolve, reject) {
          _self.$refs[formName].validate((valid) => {
            if (valid) {
              resolve();
            } else {
              reject();
            }
          });
        });
        resultArr.push(result); // push 得到promise的结果
      }
      formArr.forEach((item) => {
        // 根据表单的ref校验
        checkForm(item);
      });
      Promise.all(resultArr)
        .then((values) => {
          // XSDD10_SYS MTO订单
          // XSDD04_SYS 直运订单
          // XSDD02_SYS 寄售订单
          // advanceSaleFlag 1 MTO需判断是否为1，是否支持预售
          let moqCount = 0;
          if (this.formData.orderGoods.length == 0) {
            this.$message.error(
              "订单的商品明细不能为空，至少有一条数据，请重新刷新数据进行修改！"
            );
            return;
          }
          for (let i = 0; i < this.formData.orderGoods.length; i++) {
            if (
              this.formData.orderGoods[i].actualOrderCount === undefined ||
              this.formData.orderGoods[i].actualOrderCount === 0 ||
              this.formData.orderGoods[i].actualOrderCount <= 0
            ) {
              this.$message.error(
                this.formData.orderGoods[i].itemName + "数量必须大于零！"
              );
              return;
            }
          }
          if (this.formData.orderTypeValue === "XSDD10_SYS") {
            // ..MTO订单类型
            this.formData.orderGoods.forEach((item) => {
              if (item.advanceSaleFlag == 1) {
                // ..订单为MTO订单，并且支持预售的商品
                if (item.actualOrderCount < Number(item.moq)) {
                  this.$message.warning(
                    "预售商品" +
                      item.itemCode +
                      "订购数量不可少于" +
                      Number(item.moq)
                  );
                  item.actualOrderCount = Number(item.moq);
                  moqCount++;
                }
              }
            });
          } else if (this.formData.orderTypeValue === "XSDD04_SYS") {
            // ..直运订单类型
            this.formData.orderGoods.forEach((item) => {
              if (item.actualOrderCount < Number(item.moq)) {
                this.$message.warning(
                  "直运订单商品" +
                    item.itemName +
                    "订购数量不可少于" +
                    Number(item.moq)
                );
                item.actualOrderCount = Number(item.moq);
                moqCount++;
              }
            });
          } else if (this.formData.orderTypeValue === "DIY_SYS") {
            // 定制订单
          } else {
            this.formData.orderGoods.forEach((item) => {
              if (item.actualOrderCount > item.maxCount) {
                this.$message.warning(
                  item.itemName +
                    "库存不足," +
                    "当前最大可购数量为" +
                    item.maxCount
                );
                moqCount++;
              }
            });
          }
          if (!moqCount) {
            this.submitForm(); // ..保存成功时帮用户进行提交操作，便于后台做取数据
            moqCount = 0;
          }
          this.changeCount = false;
        })
        .catch((_) => {
          console.log("用户必填项没有填写完整");
        });
    },

    submitForm() {
      // 保存订单请求
      this.disabledShow = true;
      if (!this.saveWin) {
        // 首次未保存做提交动作不做保存loading（保存）
        this.loading = this.$loading({
          lock: true,
          text: "保存订单中....",
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, 0.7)",
        });
      } else if (this.saveWin) {
        // ..提交
        this.loading1 = this.$loading({
          lock: true,
          text: "提交订单中....",
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, 0.7)",
        });
      }
      if (this.formData.invoiceType === "请选择") {
        // 发票类型
        this.formData.invoiceType = 0;
      }
      this.modifyGoodsList = this.formData.orderGoods.map((item) => ({
        importOderGoodsId: item.id,
        actualOrderCount: item.actualOrderCount,
      }));
      this.$http
        .updateImportOrderDetail(this, {
          id: parseInt(this.$route.query.id), // 导入订单id
          orderTypeValue: this.formData.orderTypeValue, // 订单类型
          countryId: this.formData.countryId, // 国家
          provinceId: this.formData.provinceId, // 省份id
          cityId: this.formData.cityId, // 市id
          districtId: this.formData.districtId, // 区/县id
          address: this.formData.address, // 地址
          userName: this.formData.userName, // 收货人
          mobile: this.formData.mobile, // 手机
          phone: this.formData.phone, // 电话
          payWay: this.formData.payWay, // 支付方式
          zipCode: this.formData.zipCode, // 邮编
          distributionId: this.formData.distributionId, // 配送方式id
          isInvoice: this.formData.isInvoice, // 是否开具发票 0.否，1.是
          invoiceType: this.formData.invoiceType, // 发票类型 1.普通 2.增值税发票
          remark: this.formData.remark, // 配送方式id
          goodsDelIds: this.orderDeleteIds.toString(), // 货品删除ids，多个以','分隔
          modifyGoodsList: this.modifyGoodsList,
        })
        .then((res) => {
          if (res.success) {
            // Promise.all([
            //   this.$api
            //     .get(this, "admin/u/p/importOrder", {
            //       id: this.$route.query.id,
            //     })
            //     .then((res) => {
            //       this.promotionIds.ruleIds = [];
            //       res.order.orderGoods.forEach((item) => {
            //         // 组合活动规则Id
            //         if (
            //           item.ruleId !== undefined &&
            //           item.ruleId !== null &&
            //           this.promotionIds.ruleIds.indexOf(item.ruleId) < 0
            //         ) {
            //           this.promotionIds.ruleIds.push(item.ruleId);
            //           if (
            //             item.orderRuleId !== undefined &&
            //             item.orderRuleId !== null &&
            //             this.promotionIds.ruleIds.indexOf(item.orderRuleId) < 0
            //           ) {
            //             this.promotionIds.ruleIds.push(item.orderRuleId);
            //           }
            //         } else if (
            //           item.gradeDiscountId !== undefined &&
            //           item.gradeDiscountId !== null
            //         ) {
            //           // 商品等级折扣活动Id
            //           this.promotionIds.gradeRuleId = item.gradeDiscountId;
            //         }
            //         if (item.rules.length > 0) {
            //           item.rules.forEach((raw) => {
            //             raw.isGrade = false;
            //           });
            //         }
            //         if (
            //           item.gradeRule !== undefined &&
            //           item.gradeRule !== null
            //         ) {
            //           item.rules.push({
            //             id: item.gradeRule.id,
            //             label: item.gradeRule.policyName,
            //             isGrade: true,
            //             description: item.gradeRule.description,
            //             discount: item.gradeRule.discount,
            //             moneyOrCount: item.gradeRule.moneyOrCount,
            //             oneBuyMoney: item.gradeRule.oneBuyMoney,
            //             discountBeforeAfter: item.gradeRule.discountBeforeAfter,
            //             oneBuyCount: item.gradeRule.oneBuyCount,
            //           });
            //         }
            //       });
            //       this.formData = res.order;
            //       this.promotionRules.forEach((item) => {
            //         item.goods = this.formData.orderGoods;
            //       });
            //       if (
            //         res.order.provinceId === 0 &&
            //         res.order.cityId === 0 &&
            //         res.order.districtId === 0
            //       ) {
            //         this.formData.provinceId = "";
            //         this.formData.cityId = "";
            //         this.formData.districtId = "";
            //       } else {
            //         if (
            //           res.order.provinceId !== 0 ||
            //           res.order.provinceId !== ""
            //         ) {
            //           this.cityShow = true;
            //           this.$api
            //             .get(this, "region", {
            //               parentId: this.formData.provinceId,
            //             })
            //             .then((res) => {
            //               res.regions.forEach((item) => {
            //                 if (this.formData.cityId === item.id) {
            //                   this.RegionForChose.cityId = res.regions;
            //                   this.townShow = true;
            //                   this.$api
            //                     .get(this, "region", {
            //                       parentId: this.formData.cityId,
            //                     })
            //                     .then((res) => {
            //                       if (this.formData.districtId === 0) {
            //                         this.townShow = false;
            //                         this.formData.districtId = 0;
            //                       } else {
            //                         res.regions.forEach((item) => {
            //                           if (
            //                             this.formData.districtId === item.id
            //                           ) {
            //                             this.RegionForChose.districtId =
            //                               res.regions;
            //                           }
            //                         });
            //                       }
            //                     });
            //                 }
            //               });
            //             });
            //         }
            //       }
            //       this.isChangePromotion = false;
            //       res.code === 0
            //         ? (this.loading2 = false)
            //         : (this.loading2 = false);
            //       // TODO
            //       if (res.order.distributionId == 0) {
            //         // 配送方式
            //         this.formData.distributionId = "";
            //       }
            //       if (res.order.invoiceType == 0) {
            //         // 发票类型
            //         this.invoiceShow = false;
            //         this.formData.invoiceType = "";
            //       }
            //       if (res.order.payWay == 0) {
            //         // 支付方式
            //         this.formData.payWay = "";
            //       }

            //       if (this.checkhHandleFlag == 1) {
            //         // 订单号拆分
            //         this.allIds = res.order.orderId.split(",");
            //       }
            //       if (this.isPresentChange && res.code === 0) {
            //         this.isPresentChange = false;
            //         this.lookPromotion();
            //       } else if (this.isPresentChange) {
            //         this.isPresentChange = false;
            //         this.selectPresentLoading = false;
            //       }
            //     }),
            // ]).then((values) => {
            //   if (!this.saveWin) {
            //     // 首次未保存做提交动作不提示保存成功 （保存）
            //     this.$message.success({
            //       message:
            //         "保存订单成功,活动已重新计算,注意：活动数据可能已变化",
            //       duration: 3 * 1000,
            //     });
            //     this.loading.close();
            //     this.disabledShow = false;
            //   } else if (this.saveWin) {
            //     // ..提交
            //     this.importOrderForm();
            //   }
            // });

            if (!this.saveWin) {
              // 首次未保存做提交动作不提示保存成功 （保存）
              this.$message.success({
                message: "保存订单成功,活动已重新计算,注意：活动数据可能已变化",
                duration: 3 * 1000,
              });
              this.loading.close();
              this.disabledShow = false;
            } else if (this.saveWin) {
              // ..提交
              this.importOrderForm();
            }
          } else {
            if (this.saveWin) {
              this.loading1.close();
              this.disabledShow = false;
            } else {
              this.loading.close();
              this.disabledShow = false;
            }
          }
        });
    },

    importOrderForm() {
      // 提交订单请求
      this.$http
        .ordersImportOrder(this, {
          ids: parseInt(this.$route.query.id),
        })
        .then((res) => {
          this.saveWin = false;
          if (res.success) {
            this.$message.success({
              message: "提交订单成功",
              duration: 3 * 1000,
              onClose: () => {},
            });
            this.disabledShow = true;
            this.loading1.close();
            this.$router.push({ name: "batchChannel" });
          } else {
            this.loading1.close();
            this.disabledShow = false;
          }
        });
    },

    // ======== 数据 ========
    orderImportOrder() {
      // 订单详情
      console.log("dddd");
      this.$http
        .getImportOrderDetail(this, { id: Number(this.$route.query.id) })
        .then((res) => {
          if (res.success) {
            // this.promotionIds.ruleIds = [];
            // res.data.orderGoods.forEach((item) => {
            //   // 组合活动规则Id
            //   this.counts.push(item.actualOrderCount);
            //   if (
            //     item.ruleId !== undefined &&
            //     item.ruleId !== null &&
            //     this.promotionIds.ruleIds.indexOf(item.ruleId) < 0
            //   ) {
            //     this.promotionIds.ruleIds.push(item.ruleId);
            //     if (
            //       item.orderRuleId !== undefined &&
            //       item.orderRuleId !== null &&
            //       this.promotionIds.ruleIds.indexOf(item.orderRuleId) < 0
            //     ) {
            //       this.promotionIds.ruleIds.push(item.orderRuleId);
            //     }
            //   } else if (
            //     item.gradeDiscountId !== undefined &&
            //     item.gradeDiscountId !== null
            //   ) {
            //     // 商品等级折扣活动Id
            //     this.promotionIds.gradeRuleId = item.gradeDiscountId;
            //   }
            //   if (item.rules.length > 0) {
            //     item.rules.forEach((raw) => {
            //       raw.isGrade = false;
            //     });
            //   }
            //   if (item.gradeRule !== undefined && item.gradeRule !== null) {
            //     item.rules.push({
            //       id: item.gradeRule.id,
            //       label: item.gradeRule.policyName,
            //       isGrade: true,
            //       description: item.gradeRule.description,
            //       discount: item.gradeRule.discount,
            //       moneyOrCount: item.gradeRule.moneyOrCount,
            //       oneBuyMoney: item.gradeRule.oneBuyMoney,
            //       discountBeforeAfter: item.gradeRule.discountBeforeAfter,
            //       oneBuyCount: item.gradeRule.oneBuyCount,
            //     });
            //   }
            // });
            this.formData = res.data;
            this.currencyType = this.formData.currencyType;
            this.orderTypeValue = this.formData.orderTypeValue;
            // this.promotionRules.forEach((item) => {
            //   item.goods = this.formData.orderGoods;
            // });
            if (
              res.data.countryId === 0 &&
              res.data.provinceId === 0 &&
              res.data.cityId === 0 &&
              res.data.districtId === 0
            ) {
              this.formData.countryId = "";
              this.formData.provinceId = "";
              this.formData.cityId = "";
              this.formData.districtId = "";
            } else {
              if (res.data.countryId !== 0 || res.data.countryId !== "") {
                this.provinceShow = true;
                this.$http
                  .regionListById(this, {
                    page: 1,
                    size: 200,
                    parentId: this.formData.countryId,
                  })
                  .then((res) => {
                    if (res.data.list.length <= 0) {
                      this.provinceShow = false;
                      this.formData.provinceId = 0;
                    } else {
                      res.data.list.forEach((item) => {
                        if (this.formData.provinceId === item.id) {
                          this.RegionForChose.provinceId = res.data.list;
                          this.cityShow = true;
                          this.$http
                            .regionListById(this, {
                              page: 1,
                              size: 200,
                              parentId: this.formData.provinceId,
                            })
                            .then((res) => {
                              if (this.formData.cityId === 0) {
                                this.cityShow = false;
                                this.formData.cityId = 0;
                              } else {
                                res.data.list.forEach((item) => {
                                  if (this.formData.cityId === item.id) {
                                    this.RegionForChose.cityId = res.data.list;
                                    this.townShow = true;
                                    this.$http
                                      .regionListById(this, {
                                        page: 1,
                                        size: 200,
                                        parentId: this.formData.cityId,
                                      })
                                      .then((res) => {
                                        if (this.formData.districtId === 0) {
                                          this.townShow = false;
                                          this.formData.districtId = 0;
                                        } else {
                                          res.data.list.forEach((item) => {
                                            if (
                                              this.formData.districtId ===
                                              item.id
                                            ) {
                                              this.RegionForChose.districtId =
                                                res.data.list;
                                            }
                                          });
                                        }
                                      });
                                  }
                                });
                              }
                            });
                        }
                      });
                    }
                  });
              }
            }
            this.isChangePromotion = false;

            // TODO
            if (res.data.distributionId == 0) {
              // 配送方式
              this.formData.distributionId = "";
            }
            if (res.data.invoiceType == 0) {
              // 发票类型
              this.invoiceShow = false;
              this.formData.invoiceType = "";
            }
            if (res.data.payWay == 0) {
              // 支付方式
              this.formData.payWay = "";
            }

            if (this.checkhHandleFlag == 1) {
              // 订单号拆分
              this.allIds = res.data.orderId ? res.data.orderId.split(",") : [];
            }
            if (this.isPresentChange) {
              this.isPresentChange = false;
              this.lookPromotion();
            } else if (this.isPresentChange) {
              this.isPresentChange = false;
              this.selectPresentLoading = false;
            }
            // 定制商品工厂
            this.orderDistribution();
          }
          this.loading2 = false;
        });
    },

    orderDistribution() {
      // 配送方式
      if (!this.isDistribution) {
        this.isDistribution = true;
        return this.$http
          .logisticsPoList(this, {
            countryId: this.formData.countryId,
            provinceId: this.formData.provinceId,
            cityId: this.formData.cityId,
            districtId: this.formData.districtId,
            // useRange: this.formData.orderTypeValue === "DIY_SYS" ? 2 : 1,
            // distributorInfoId: this.formData.distributorId,
            manufactor: this.formData.manufactor,
            enable: 1,
            page: 1,
            size: 100,
          })
          .then((res) => {
            this.orderDistriList = res.data.list;
            // this.orderImportOrder() // 订单详情
            this.isDistribution = false;
            let isExit = false;
            if (
              this.orderDistriList !== undefined &&
              this.orderDistriList !== null &&
              this.orderDistriList.length > 0
            ) {
              this.orderDistriList.forEach((item) => {
                if (item.id === this.formData.distributionId) {
                  isExit = true;
                }
              });
            }
            if (!isExit) {
              this.$message.error(
                "根据订单类型、地区或定制工厂条件查询不到对应的配送方式，请重新选择配送方式"
              );
              this.formData.distributionId = undefined;
            }
          });
      }
    },
    // ======== 转换 ========
    orderTypeArr() {
      // 订单类型
      this.loading2 = true;
      return this.$http
        .orderTypeList(this, { page: 1, size: 1000 })
        .then((res) => {
          if (res.success) {
            this.orderTypeList = res.data.list;
          }
        });
    },

    renderHeader(h, col) {
      // 页面右上角的规格值
      if (
        this.formData.orderTypeValue === "DIY_SYS" &&
        this.formData.orderGoods !== undefined &&
        this.formData.orderGoods.length > 0 &&
        this.formData.orderGoods[0].goodsType == 3
      ) {
        // 新柔性定制
        switch (col.$index) {
          case 8:
            return h("div", [
              h("span", "价格折扣"),
              h(
                "el-tooltip",
                {
                  props: {
                    content: "价格折扣为货品在该订单中优惠的金额。",
                    effect: "light",
                    placement: "top",
                  },
                },
                [h("span", { class: "el-icon-question question-color" })]
              ),
            ]);
            break;
          case 10:
            return h("div", [
              h("span", "缺货数量"),
              h(
                "el-tooltip",
                {
                  props: {
                    content:
                      "下单时会自动扣除缺货数量，点击右上角可导出缺货清单",
                    effect: "light",
                    placement: "top",
                  },
                },
                [h("span", { class: "el-icon-question question-color" })]
              ),
            ]);
            break;
        }
      } else {
        switch (col.$index) {
          case 5:
            return h("div", [
              h("span", "价格折扣"),
              h(
                "el-tooltip",
                {
                  props: {
                    content: "价格折扣为货品在该订单中优惠的金额。",
                    effect: "light",
                    placement: "top",
                  },
                },
                [h("span", { class: "el-icon-question question-color" })]
              ),
            ]);
            break;
          case 7:
            return h("div", [
              h("span", "缺货数量"),
              h(
                "el-tooltip",
                {
                  props: {
                    content:
                      "下单时会自动扣除缺货数量，点击右上角可导出缺货清单",
                    effect: "light",
                    placement: "top",
                  },
                },
                [h("span", { class: "el-icon-question question-color" })]
              ),
            ]);
            break;
        }
      }
    },

    timeFormatter(rawData) {
      // 时间格式化
      this.timeFormatter = parseTime;
      return this.timeFormatter(rawData);
    },

    saleStatusFormatter(stateCode) {
      // 导入订单详情上下架状态
      this.saleStatusFormatter = saleStatusFormatter;
      return this.saleStatusFormatter(stateCode);
    },
  },
  watch: {
    "formData.isInvoice": function () {
      // ..是否开票 0 否 1 是
      if (this.formData.isInvoice == 0) {
        this.invoiceShow = false;
      } else {
        this.invoiceShow = true;
      }
    },
    "formData.orderTypeValue": function (val) {
      console.log(val);
      // ..订单类型
      if (
        this.formData.orderTypeValue === "XSDD10_SYS" ||
        this.formData.orderTypeValue === "XSDD04_SYS" ||
        this.formData.orderTypeValue === "DIY_SYS"
      ) {
        // MTO订单 || 直运订单
        this.orderShow = false;
      } else {
        this.orderShow = true;
      }
      if (
        (this.orderTypeValue !== "DIY_SYS" &&
          this.formData.orderTypeValue === "DIY_SYS") ||
        (this.orderTypeValue === "DIY_SYS" &&
          this.formData.orderTypeValue !== "DIY_SYS")
      ) {
        // 普通订单和定制订单之间不能互相切换
        this.$message.error("普通订单和定制订单之间不能互相切换!");
        this.formData.orderTypeValue = this.orderTypeValue;
      } else {
        this.orderTypeValue = this.formData.orderTypeValue;
      }
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.v-auto-out {
  position: relative;
  margin-left: 10px;
  height: 100%;
  font-weight: 500;
}
.v-auto-out .auto-in {
  position: absolute;
  top: 50%;
  /* 这里有兼容性问题 */
  -webkit-transform: translateY(-50%);
  -ms-transform: translateY(-50%);
  -o-transform: translateY(-50%);
  transform: translateY(-50%);
}
.foot-btn {
  margin-top: 20px;
}
.el-aside {
  background-color: #d3dce6;
  color: #333;
}

.el-main {
  background-color: #e9eef3;
  color: #333;
  text-align: left;
}
.pomotion-rule {
  border-radius: 5px;
  text-align: left;
  padding: 10px 10px 10px 10px;
  margin-bottom: 5px;
  border: 1px solid #ccc;
  background-color: #f8f8f8;
  .span {
    word-break: normal;
    width: 100%;
    display: block;
    white-space: normal;
    word-wrap: break-word;
    overflow: hidden;
  }
  .present-label {
    margin: 10px;
    background-color: #fab42d !important;
    border: 1px solid #fab42d;
    padding: 3px 5px;
    font-size: 12px;
    color: white !important;
    border-color: #fab42d;
    font-weight: 500;
    border-radius: 4px;
  }
}
.pomotion-present {
  vertical-align: middle;
  position: relative;
  background-color: rgb(172, 242, 255);
  color: #333;
  .half-left {
    width: 30%;
    float: left;
  }
  .half-right {
    width: 70%;
    // float: right;
    position: absolute;
    text-align: left;
    margin-left: 30%;
    top: 50%;
    /* 这里有兼容性问题 */
    -webkit-transform: translateY(-50%);
    -ms-transform: translateY(-50%);
    -o-transform: translateY(-50%);
    transform: translateY(-50%);
  }
  .present-label {
    margin: 10px;
    background-color: #fab42d !important;
    border: 1px solid #fab42d;
    // padding: 7px 10px;
    padding: 5px;
    font-size: 14px;
    color: white !important;
    border-color: #fab42d;
    display: block;
    font-weight: 500;
    line-height: 1.1em;
    -webkit-appearance: none;
    text-align: left;
    box-sizing: border-box;
    outline: none;
    font-weight: 500;
    user-select: none;
    -moz-user-select: none;
    -webkit-user-select: none;
    -ms-user-select: none;
    border-radius: 4px;
  }
}
.pomotion-look {
  vertical-align: middle;
  position: relative;
  .half-left {
    width: 80%;
    float: left;
  }
  .half-right {
    width: 20%;
    // float: right;
    position: absolute;
    text-align: center;
    margin-left: 80%;
    top: 50%;
    /* 这里有兼容性问题 */
    -webkit-transform: translateY(-50%);
    -ms-transform: translateY(-50%);
    -o-transform: translateY(-50%);
    transform: translateY(-50%);
  }
}
.el-table .cell div {
  padding-right: 0px !important;
}

.order-detail .el-form div.el-form-item {
  text-align: left;
  margin: auto;
  margin-bottom: 14px !important;
}
.batch_input_number {
  display: flex;
  text-align: center;
  margin: 0 auto;
  .batch_minus {
    width: 25px;
    padding: 0px;
    cursor: pointer;
    line-height: 22px;
    font-size: 12px;
    background-color: #f5f7fa;
    text-align: center;
    border-radius: 4px 0 0 4px;
    border: 1px solid #dcdfe6;
    border-right: 0;
  }
  .batch_minus:disabled {
    pointer-events: none;
  }
  .batch_input1 {
    text-align: center;
    width: 70px;
    border: 1px solid #dcdfe6;
    line-height: 22px;
    margin: 0 auto;
  }
  .batch_input1:disabled {
    border: 1px solid #ddd;
    background-color: #f5f5f5;
    color: #aca899;
    margin: 0 auto;
    cursor: no-drop;
  }
  .batch_plusSign {
    padding: 0px;
    width: 25px;
    cursor: pointer;
    line-height: 22px;
    font-size: 12px;
    background-color: #f5f7fa;
    text-align: center;
    border-radius: 0 4px 4px 0;
    border: 1px solid #dcdfe6;
    border-left: 0;
  }
  .batch_plusSign:disabled {
    pointer-events: none;
  }
}
.order-detail {
  height: 100%;
  background-color: white;
  padding-bottom: 30px;
  .el-form div.el-form-item {
    margin-bottom: 5px;
  }
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
    span {
      margin-left: 30px;
    }
    .btn-home {
      text-align: center;
      float: right;
      padding: 5px;
      margin-top: 7px;
      margin-right: 8px;
    }
  }
  form.el-form {
    margin-top: 0;
  }
  .half-width {
    width: 50%;
    box-sizing: border-box;
    float: left;
  }
  .text-center {
    text-align: center;
    overflow: hidden;
    margin-bottom: 10px;
  }
  .text-center.title {
    padding-top: 10px;
    padding-bottom: 10px;
  }
  .order-message {
    color: #333;
    margin-left: 20px;
    float: left;
    margin-top: 10px;
  }
  .box-btn-top {
    padding: 20px;
  }
  .box-has-border {
    overflow: hidden;
    .cost-line {
      padding-bottom: 10px;
      padding-top: 10px;
      border-bottom: 1px solid $tableColor;
      padding-left: 30px;
      span.cost-info {
        margin-left: 5px;
      }
      span.cost-info:last-child {
        margin-right: 35px;
      }
    }
    .cost-line:last-child {
      border-bottom: none;
    }
    .align-right {
      text-align: right;
    }
    .orderMoney {
      text-align: right;
      margin-top: 20px;
      margin-right: 20px;
      font-weight: 20px;
      font-weight: bold;
    }
    div.form {
      margin-top: 30px;
      margin-bottom: 40px;
      form.el-form {
        margin-right: 0;
        width: 80%;
        min-width: 800px;
        max-width: 1000px;
      }
    }
    .lookPromotion {
      position: relative;
      float: right;
      margin-right: 10px;
      margin-bottom: 10px;
    }
    .educe {
      position: relative;
      float: right;
      margin-right: 20px;
      margin-bottom: 10px;
      // left: 37%;
    }
  }
  .tableCenters {
    text-align: center;
    margin: 20px;
  }
  .function {
    padding: 0px 16px 16px 16px;
    background-color: white;
    .btn-export {
      background-color: lighten(grey, 40%);
    }
    .search {
      float: right;
    }
    .btn-search {
      background-color: $lakeBlue;
      color: white;
    }
  }
}
.bg-purple {
  padding: 10px;
  background: #d3dce6;
}
.bg-purple-light {
  background: #e5e9f2;
}
</style>
