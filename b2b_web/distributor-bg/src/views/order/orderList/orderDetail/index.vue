<template>
  <div>
    <section class="order-detail">
      <header>
        <span>订单详情</span>
        <el-button
          v-if="orderBatchPage"
          icon="el-icon-d-arrow-left"
          @click="cancen()"
          class="mini-back-btn btn-home"
        >
          返回导入订单详情
        </el-button>
        <el-button
          v-if="checkOrderG"
          icon="el-icon-d-arrow-left"
          @click="orderListCancen()"
          class="mini-back-btn btn-home"
        >
          返回订单列表
        </el-button>
        <el-button
          v-if="checkOrderDistrG"
          icon="el-icon-d-arrow-left"
          @click="orderDistrListCancen()"
          class="mini-back-btn btn-home"
        >
          返回分销订单列表
        </el-button>
        <el-button
          v-if="checkOrderCustG"
          icon="el-icon-d-arrow-left"
          @click="orderCustListCancen()"
          class="mini-back-btn btn-home"
        >
          返回柔性定制订单列表
        </el-button>
        <el-button
          v-if="checkOrderCustSyncG"
          icon="el-icon-d-arrow-left"
          @click="orderCustSyncListCancen()"
          class="mini-back-btn btn-home"
        >
          返回柔性同步订单列表
        </el-button>
        <el-button
          v-if="checkSyncErpFailG"
          icon="el-icon-d-arrow-left"
          @click="failOrderCancen(1)"
          class="mini-back-btn btn-home"
        >
          返回同步ERP失败订单列表
        </el-button>
        <el-button
          v-if="checkSyncFacFailG"
          icon="el-icon-d-arrow-left"
          @click="failOrderCancen(2)"
          class="mini-back-btn btn-home"
        >
          返回同步工厂失败订单列表
        </el-button>
        <el-button
          v-if="checkSyncUndeliverG"
          icon="el-icon-d-arrow-left"
          @click="failOrderCancen(3)"
          class="mini-back-btn btn-home"
        >
          返回长时间未发货订单列表
        </el-button>
        <el-button
          v-if="checkStockG"
          icon="el-icon-d-arrow-left"
          @click="stockListCancen()"
          class="mini-back-btn btn-home"
        >
          返回商品库存列表
        </el-button>
        <el-button
          v-if="checkVmiG"
          icon="el-icon-d-arrow-left"
          @click="vmiListCancen()"
          class="mini-back-btn btn-home"
        >
          返回VMI订单列表
        </el-button>
        <el-button
          v-if="checkRefundApply"
          icon="el-icon-d-arrow-left"
          @click="refundApplyListCancen()"
          class="mini-back-btn btn-home"
        >
          返回退款申请单列表
        </el-button>
      </header>
      <div class="box-btn-top">
        <div class="text-center">
          <div>
            <div>
              <!-- <h4 class="order_head_body" v-if="chosenData.orderGoodsType == 4 || chosenData.orderGoodsType == 5">{{orderGoodsTypeFormatter(chosenData.orderGoodsType)}}</h4> -->
            </div>
            <div class="text-headerOne">
              <h4 class="text-headerTwo">
                {{chosenData.orderInfo.orderTypeName}} - {{ stockTypeFormatter(chosenData.orderInfo.stockType) }}
              </h4>
              <div style="float: right">
                <el-button
                  :loading="synCYLoading"
                  class="mini-search-btn"
                  @click="syncYCOrder()"
                  v-if="synchronousYC == 0"
                  >同步工厂</el-button
                >
                <!-- <el-button
                  :loading="synIsProduct"
                  class="mini-search-btn"
                  @click="synProduct()"
                  v-if="isProduct == 0"
                  >发生产</el-button
                > -->
                <el-button
                  :loading="synLoading"
                  class="mini-search-btn"
                  v-if="synchronousERP == 0"
                  @click="syncOrder()"
                  >同步ERP</el-button
                >
              </div>
            </div>
          </div>
        </div>
      </div>
      <!---v-if="synchronousERP == 0"----->
      <div v-loading="loading" v-show="chosenData">
        <div class="box-has-border">
          <div class="text-center title" style="margin-top: 10px">
            <span>基本信息</span>
          </div>
          <div class="half-width">
            <el-form ref="chosenData" label-width="280px">
              <el-form-item label="订单号:" > {{ chosenData.orderInfo.orderNo }} </el-form-item>
              <el-form-item label="ERP订单号:" v-if="chosenData.orderExtendData">
                {{ chosenData.orderExtendData.orderErpNo }}
              </el-form-item>
              <el-form-item
                v-if="chosenData.orderExtendData && chosenData.orderExtendData.orderFactoryNo"
                label="工厂订单号:"
              >
                {{ chosenData.orderExtendData.orderFactoryNo }}
              </el-form-item>
              <el-form-item
                label="第三方编号:"
                v-if=" chosenData.orderExtendData"
              >
                {{ chosenData.orderExtendData.orderThirdpartyNo }}
              </el-form-item>
              <el-form-item label="购买者:" v-if="chosenData.orderDistributor">
                {{ chosenData.orderDistributor.orderDistributorDataDTO.distributorName }}
              </el-form-item>
              <el-form-item label="业务员:">
                {{ chosenData.orderInfo.salesName }}
              </el-form-item>
              <el-form-item label="支付方式:" v-if="chosenData.orderDistributor">
                {{ orderPaymentFormatter(chosenData.orderDistributor.orderDistributorDataDTO.payWay) }}
                <el-button
                  class="mini-search-btn currency-btn"
                  v-if="chosenData.voucherImg && chosenData.voucherImg !== null"
                  @click="showRecordModel = true"
                  >查看支付凭证</el-button
                >
              </el-form-item>
              <el-form-item label="支付币种:" v-if="chosenData.orderDistributor">
                {{ chosenData.orderDistributor.orderDistributorDataDTO.currencyType }}
                <!-- <el-button
                  class="mini-search-btn currency-btn"
                  @click="currencyChangeConfirmOpen"
                  >更改支付币种</el-button
                > -->
              </el-form-item>
            </el-form>
          </div>
          <div class="half-width">
            <el-form ref="chosenData" label-width="190px">
              <el-form-item label="订单类型:">
                {{ chosenData.orderInfo.orderTypeName }}
              </el-form-item>
              <el-form-item label="订单来源:">
                {{ chosenData.orderInfo.orderSourceName }}
              </el-form-item>
              <el-form-item label="订单状态:" v-if="chosenData.orderDistributor">
                {{ orderStatusFormatter(chosenData.frontOrderStatus) }}
              </el-form-item>
              <el-form-item label="下单时间:" v-if="chosenData.orderDistributor">
                {{ timeFormatter(chosenData.orderDistributor.orderDistributorDataDTO.createTime) }}
              </el-form-item>
              <el-form-item label="付款时间:" v-if="chosenData.orderDistributor.orderDistributorDataDTO.payTime && checkPayStatus != 1 && chosenData.orderDistributor.orderDistributorDataDTO.payWay !== 3">
                <div v-if="chosenData.orderDistributor.orderDistributorDataDTO">
                  {{ timeFormatter(chosenData.orderDistributor.orderDistributorDataDTO.payTime) }}
                  <el-button
                    class="mini-search-btn"
                    v-if="
                      chosenData.orderDistributor.orderDistributorDataDTO.payTime != undefined &&
                      chosenData.orderDistributor.orderDistributorDataDTO.payTime != null &&
                      chosenData.orderDistributor.orderDistributorDataDTO.payWay !== 5
                    "
                    @click="handleReceopt()"
                    style="margin-left: 10px"
                    >查看收款单</el-button
                  >
                  <el-button
                    class="mini-search-btn"
                    @click="handleRefundApplies()"
                    v-if="
                      chosenData.orderDistributor.orderDistributorDataDTO.payStatus &&
                      chosenData.orderDistributor.orderDistributorDataDTO.payStatus >= 4
                    "
                    style="margin-left: 10px"
                    >查看退款申请单</el-button
                  >
                </div>
                <div v-else>-</div>
              </el-form-item>
              <el-form-item label="付款时间:" v-if="checkPayStatus === 1 && chosenData.orderDistributor.orderDistributorDataDTO.payWay !== 3">
                {{ "未付款" }}
              </el-form-item>
              <el-form-item
                label="发货时间:"
                v-if="
                  chosenData.orderInfo.deliverTime != null &&
                  chosenData.orderInfo.deliverTime != undefined &&
                  chosenData.orderInfo.deliverTime != 0
                "
              >
                {{ timeFormatter(chosenData.orderInfo.deliverTime) }}
                <el-button
                  class="mini-search-btn"
                  @click="handleInvoice(chosenData.orderInfo)"
                  style="margin-left: 10px"
                  >查看发货单</el-button
                >
              </el-form-item>
              <el-form-item label="发货时间:" v-else>
                {{ "未发货" }}
              </el-form-item>
              <el-form-item label="配送方式:">
                <span v-if="chosenData.orderDelivery.distributionName">
                  {{ chosenData.orderDelivery.distributionName }}
                </span>
              </el-form-item>
            </el-form>
          </div>
        </div>
        <!--------发票信息------->
        <div class="box-has-border" v-show="chosenData.orderInvoice">
          <div class="text-center title">
            <span>发票信息</span>
          </div>
          <div class="half-width">
            <el-form ref="chosenData" label-width="280px">
              <el-form-item label="是否开票:">{{booleanFormatter(chosenData.orderInfo.invoiceFlag)}}</el-form-item>
              <el-form-item label="发票类型:" v-if="orPersonalInvoice == 0">
                {{ "普通个人发票" }}
              </el-form-item>
              <el-form-item label="发票类型:" v-if="orPersonalInvoice == 1">
                {{ "普通单位发票" }}
              </el-form-item>
              <el-form-item label="发票类型:" v-if="orPersonalInvoice == 2">
                {{ "增值税发票" }}
              </el-form-item>
              <el-form-item label="纳税人识别号:" v-if="taxNo == 0">
                {{ chosenData.orderInvoice.taxpayerNumber }}
              </el-form-item>
              <el-form-item label="增值税发票注册电话:" v-if="VATNumber == 0">
                {{ chosenData.orderInvoice.registerPhone }}
              </el-form-item>
              <el-form-item label="增值税发票银行账户:" v-if="VATNumber == 0">
                {{ chosenData.orderInvoice.bankAccount }}
              </el-form-item>
            </el-form>
          </div>
          <div class="half-width">
            <el-form ref="chosenData" label-width="190px">
              <el-form-item label="分销商留言:">
                {{ chosenData.orderDistributor.orderDistributorDataDTO.remark }}
              </el-form-item>
              <el-form-item label="发票抬头:" v-if="invoiceUp == 0">
                {{ chosenData.orderInvoice.name }} </el-form-item
              >
              <el-form-item label="增值税发票注册地址:" v-if="VATNumber == 0">
                {{ chosenData.orderInvoice.registerAddress }}
              </el-form-item>
              <el-form-item label="增值税发票开户银行:" v-if="VATNumber == 0">
                {{ chosenData.orderInvoice.bankAccountName }}
              </el-form-item>
            </el-form>
          </div>
        </div>
        <!--------收货人信息------->
        <div class="box-has-border" v-if="chosenData.orderDelivery">
          <div class="text-center title">
            <span>收货人信息</span>
          </div>
          <div class="half-width">
            <el-form ref="chosenData" label-width="280px">
              <el-form-item label="收货人:">
                {{ chosenData.orderDelivery.userName }}
              </el-form-item>
              <el-form-item label="地址:">
                {{ chosenData.orderDelivery.countryName }}
                {{ chosenData.orderDelivery.provinceName }}
                {{ chosenData.orderDelivery.cityName }}
                {{ chosenData.orderDelivery.districtName }}
                {{ chosenData.orderDelivery.address }}
              </el-form-item>
              <!-- <el-form-item label="电话:">
                {{ chosenData.orderDelivery.phone }}
              </el-form-item> -->
            </el-form>
          </div>
          <div class="half-width">
            <el-form ref="chosenData" label-width="190px">
              <el-form-item label="邮编:" v-if="chosenData.orderDelivery">
                {{ chosenData.orderDelivery.zipCode }}
              </el-form-item>
              <el-form-item label="手机:" v-if="chosenData.orderDelivery">
                {{ chosenData.orderDelivery.mobile }}
              </el-form-item>
            </el-form>
          </div>
        </div>
        <!--------商品信息------->
        <div class="box-has-border" style="position: relative">
          <div class="text-center title">
            <span>商品信息</span>
          </div>
          <div style="position: absolute; top: 40px; right: 20px">
            <button
              class="mini-present-btn lookPromotion"
              v-if="
                (promotionIds !== undefined &&
                  promotionIds.conditionIds !== undefined &&
                  promotionIds.conditionIds !== null &&
                  promotionIds.conditionIds.length > 0)
              "
              @click="lookPromotion()"
            >
              查看活动
            </button>
            <button class="mini-search-btn educe" @click="exportDownload()">
              导出下单清单
            </button>
          </div>
          <el-table
            border
            :data="chosenData.orderInfoDetailGoods"
            style="width: 100%"
            header-row-class-name="header-row"
            class="tableCenter"
            max-height="600"
          >
           
            <el-table-column
              align="center"
              type="index"
              fixed
              :min-width="50"
            ></el-table-column>
           
            <el-table-column
              align="center"
              prop="orderGoods.goodsNo"
              label="商品编号"
              :min-width="140"
            >
            </el-table-column>
            <el-table-column
              align="center"
              prop="orderGoods.itemCode"
              label="存货编码"
              :min-width="120"
            >
            </el-table-column>
            <el-table-column
              align="center"
              prop="orderGoods.itemName"
              label="存货名称"
              show-overflow-tooltip
              :min-width="120"
            >
            </el-table-column>
            <el-table-column
              align="center"
              v-if="orderGoods.specsName"
              prop="orderGoods.specsName"
              key="orderGoods.specsName"
              label="规格"
            >
            </el-table-column>
            <el-table-column
              align="center"
              v-if="lookImage"
              prop="orderGoodsDiy.materialName"
              key="orderGoodsDiy.materialName"
              label="材质"
              :min-width="120"
            >
              <template slot-scope="scope">
                {{ scope.row.orderGoodsDiy ? scope.row.orderGoodsDiy.materialName : '' }}
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              v-if="lookImage"
              key="orderGoodsDiy.modelName"
              prop="orderGoodsDiy.modelName"
              label="型号"
              :min-width="120"
            >
              <template slot-scope="scope">
                {{ scope.row.orderGoodsDiy ?  scope.row.orderGoodsDiy.modelName : ''}}
              </template>
            </el-table-column>
             <el-table-column align="center" label="价格" :min-width="120">
              <template slot-scope="scope">
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                  >￥:&nbsp;</i
                >
                <i class="asmd" v-else-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                  >$:&nbsp;</i
                >
                <span v-if="scope.row.orderGoodsDistributorCost">{{ scope.row.orderGoodsDistributorCost.salePrice | NumFormat }}</span>
                <span v-else>{{ scope.row.orderGoodsCustomerCost.salePrice | NumFormat }}</span>
              </template>
            </el-table-column>
            <el-table-column align="center" label="价格折扣" :min-width="100">
              <template slot-scope="scope">
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                  >￥:&nbsp;</i
                >
                <i class="asmd" v-else-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                  >$:&nbsp;</i
                >
                <span>{{scope.row.orderDiscount}}</span>
              </template>
            </el-table-column>

            <template v-if="chosenData.orderSource === 28">
              <el-table-column
                align="center"
                label="兑换卡归属订单号"
                prop="exchangeOrderId"
                show-overflow-tooltip
                :min-width="150"
              >
                <template slot-scope="scope">
                  {{ scope.row.exchangeOrderId || "--" }}
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="发卡分销商"
                prop="exchangeDistributorName"
                show-overflow-tooltip
                :min-width="120"
              >
                <template slot-scope="scope">
                  {{ scope.row.exchangeDistributorName || "--" }}
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="发卡业务员"
                prop="exchangeSalesManName"
                show-overflow-tooltip
                :min-width="120"
              >
                <template slot-scope="scope">
                  {{ scope.row.exchangeSalesManName || "--" }}
                </template>
              </el-table-column>
            </template>

            <el-table-column
              align="center"
              label="兑换卡归属订单号"
              v-if="lookExchange"
              prop="orderGoodsDiy.exchangeOrderId"
              key="orderGoodsDiy.exchangeOrderId"
              width="120"
            >
            </el-table-column>
            <el-table-column
              align="center"
              label="发卡分销商"
              v-if="lookExchange"
              prop="orderGoodsDiy.distributorCompanyName"
              key="orderGoodsDiy.distributorCompanyName"
              width="120"
            >
            </el-table-column>
            <el-table-column
              align="center"
              label="发卡业务员"
              v-if="lookExchange"
              prop="orderGoodsDiy.salesName"
              key="orderGoodsDiy.salesName"
              width="120"
            >
            </el-table-column>
            <el-table-column
              align="center"
              label="订购数量"
              v-if="!lookImage"
              prop="orderGoods.itemCount"
              key="orderGoods.itemCount"
              :min-width="140"
            >
              <el-table-column label="在库" prop="orderGoods.itemInCount" width="80" align="center">
              </el-table-column>
              <el-table-column label="在途" prop="orderGoods.itemOnWayCount" width="80" align="center">
              </el-table-column>
              <el-table-column
                align="center"
                label="VMI"
                prop="orderGoods.itemVmiCount"
                width="80"
              >
                <template slot-scope="scope">
                  <div>{{scope.row.orderGoods.itemVmiCount}}</div>
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column
              v-if="lookImage"
              align="center"
              label="订购数量"
              :min-width="140"
            > 
              <template slot-scope="scope">
                <span>{{scope.row.orderGoods.itemCount}}</span>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              prop="orderGoods.deliverCount"
              key="orderGoods.deliverCount"
              label="已发数量"
              :min-width="100"
            >
              <template slot-scope="scope">
                <div v-if="scope.row.orderGoods.deliverCount">{{scope.row.orderGoods.deliverCount}}</div>
                <div v-else>0</div>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              prop="orderGoods.unDeliverCount"
              key="orderGoods.unDeliverCount"
              label="未发数量"
              :min-width="100"
            >
              <template slot-scope="scope">
                <div v-if="scope.row.orderGoods.unDeliverCount">{{scope.row.orderGoods.unDeliverCount}}</div>
                <div v-else>0</div>
              </template>
            </el-table-column>
            <!-- <el-table-column
              align="center"
              v-if="lookImage"
              prop="orderGoodsDiy.previewImage"
              key="orderGoodsDiy.previewImage"
              label="图片"
              :min-width="120"
            >
              <template
                slot-scope="scope"
                v-if="
                  scope.row.orderGoodsDiy.previewImage !== undefined
                "
              >
                <el-button type="text" @click="showImage(scope.row.orderGoodsDiy)"
                  >查看图片</el-button
                >
              </template>
            </el-table-column> -->
            <el-table-column
              align="center"
              v-if="lookImage"
              label="图片"
              prop="orderGoodsDiy.previewImage"
              key="orderGoodsDiy.previewImage"
              :min-width="80"
            >
              <template slot-scope="scope">
                <el-image
                  style="width: 50px; height: 50px"
                  :src="scope.row.orderGoodsDiy.previewImage"
                  fit="contain"
                  :preview-src-list="[scope.row.orderGoodsDiy.previewImage]"
                >
                </el-image>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              v-if="lookImage"
              label="生产图片"
              :min-width="100"
            >
              <template slot-scope="scope">
                <el-button size="mini" type="text" @click="downImage(scope.row.orderGoodsDiy)"
                  >下载</el-button
                >
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              v-if="lookImage"
              label="标签"
              :min-width="80"
            >
              <template slot-scope="scope">
                <el-image
                  v-if="
                    scope.row.orderGoodsDiy.labelUrl &&
                    scope.row.orderGoodsDiy.labelUrl.indexOf('jpg') !== -1
                  "
                  style="width: 50px; height: 50px"
                  :src="scope.row.orderGoodsDiy.labelUrl"
                  fit="contain"
                  :preview-src-list="[scope.row.orderGoodsDiy.labelUrl]"
                >
                </el-image>
                <el-button
                  v-if="
                    scope.row.orderGoodsDiy.labelUrl &&
                    scope.row.orderGoodsDiy.labelUrl.indexOf('jpg') === -1
                  "
                  size="mini"
                  type="text"
                  @click="labelPreview(scope.row)"
                  >查看</el-button
                >
                <el-button
                  v-if="!scope.row.orderGoodsDiy.labelUrl"
                  class="mini-freeze-btn"
                  @click="label(scope.row, scope.$index)"
                  >生成</el-button
                >
              </template>
            </el-table-column>
            <el-table-column align="center" label="小计" :min-width="120">
              <template slot-scope="scope">
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                  >￥:&nbsp;</i
                >
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                  >$:&nbsp;</i
                >
                {{ saleTotalAmount(scope.row) }}
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-------费用信息------>
        <div class="box-has-border" v-if="chosenData.orderDistributor">
          <div class="text-center title">
            <span>费用信息</span>
          </div>
          <div class="cost-line">
            <span class="cost-info">
              <span class="cost-label"
                >商品总金额
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                  >￥:&nbsp;</i
                >
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                  >$:&nbsp;</i
                >
              </span>
              <span class="cost-val">{{
                chosenData.orderDistributor.orderDistributorCostDTO.goodsAmount | NumFormat
              }}</span>
            </span>
            <!-- <i class="asmd"> - </i>
            <span class="cost-info">
              <span class="cost-label"
                >商品等级折扣
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                  >￥:&nbsp;</i
                >
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                  >$:&nbsp;</i
                >
              </span>
              <span class="cost-val">{{
                chosenData.orderDistributor.itemGradeDiscount | NumFormat
              }}</span>
            </span> -->
            <i class="asmd"> - </i>
            <span class="cost-info">
              <span class="cost-label"
                >订单折扣
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                  >￥:&nbsp;</i
                >
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                  >$:&nbsp;</i
                >
              </span>
              <span class="cost-val">{{
                chosenData.orderDistributor.orderDistributorCostDTO.orderPromotionAmount | NumFormat
              }}</span>
            </span>
            <i class="asmd"> - </i>
            <span class="cost-info">
              <span class="cost-label"
                >商品促销折扣
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                  >￥:&nbsp;</i
                >
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                  >$:&nbsp;</i
                >
              </span>
              <span class="cost-val">{{
                chosenData.orderDistributor.orderDistributorCostDTO.goodsPromotionAmount | NumFormat
              }}</span>
            </span>
            <!-- <i class="asmd"> + </i>
            <span class="cost-info">
              <span class="cost-label"
                >发票税额
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                  >￥:&nbsp;</i
                >
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                  >$:&nbsp;</i
                >
              </span>
              <span class="cost-val">{{
                chosenData.orderDistributor.invoiceTax | NumFormat
              }}</span>
            </span> -->
            <i class="asmd"> + </i>
            <span class="cost-info">
              <span class="cost-label"
                >配送费用
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                  >￥:&nbsp;</i
                >
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                  >$:&nbsp;</i
                >
              </span>
              <span class="cost-val">{{
                chosenData.orderDistributor.orderDistributorCostDTO.distributionAmount | NumFormat
              }}</span>
            </span>
            <span class="cost-info" v-if="chosenData.orderInfoDetailGoods">
              <span class="cost-label">
                （商品重量: &nbsp;&nbsp;{{totalWeight}}g
                <!-- <span v-if="chosenData.isCollectFee === 1"
                  >&nbsp;&nbsp;服务费总额：<i class="asmd">{{
                    chosenData.orderDistributor.orderDistributorDataDTO.currencyType === "CNY" ? "￥：" : "$："
                  }}</i
                  >{{ chosenData.cost.goodsServiceAmount }}</span
                > -->
                <span v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'">，
                  汇率：{{chosenData.orderDistributor.orderDistributorDataDTO.currentRates}}</span>
                ）</span
              >
            </span>
            <!-- 代金券 -->
            <template v-if="chosenData.orderDistributor.orderDistributorCostDTO.rebateVoucherAmount">
              <i class="asmd"> - </i>
              <span class="cost-info">
                <span class="cost-label"
                  >代金券
                  <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                    >￥:&nbsp;</i
                  >
                  <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                    >$:&nbsp;</i
                  >
                </span>
                <span class="cost-val">{{
                  chosenData.orderDistributor.orderDistributorCostDTO.rebateVoucherAmount | NumFormat
                }}</span>
                <i class="el-icon-warning-outline" @click="handleVoucherUsedData"></i>
              </span>
            </template>
          </div>

          <div class="cost-line align-right">
            <span class="cost-info">
              <span class="cost-label">
                <i class="asmd"> =&nbsp; </i> 订单总金额 :
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                  >￥:&nbsp;</i
                >
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                  >$:&nbsp;</i
                >
              </span>
              <span class="cost-val">
                {{ chosenData.orderDistributor.orderDistributorCostDTO.payAmount | NumFormat }}
                <span v-show="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'">元</span>
              </span>
            </span>
          </div>

          <div class="cost-line">
             <span class="cost-info">
              <span class="cost-label">订单总金额</span>
              <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                >￥:&nbsp;</i
              >
              <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                >$:&nbsp;</i
              >
              <span class="cost-val">{{ chosenData.orderDistributor.orderDistributorCostDTO.payAmount | NumFormat }}</span>
            </span>
            <i class="asmd"> - </i>
            <span class="cost-info">
              <span class="cost-label">已付款金额</span>
              <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                >￥:&nbsp;</i
              >
              <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                >$:&nbsp;</i
              >
              <span class="cost-val" v-if="chosenData.orderDistributor.orderDistributorCostDTO.paidAmount">{{
                chosenData.orderDistributor.orderDistributorCostDTO.paidAmount | NumFormat
              }}</span>
              <span v-else>0</span>
            </span>
            <i class="asmd"> - </i>
            <span class="cost-info">
              <span class="cost-label">使用余额</span>
              <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                >￥:&nbsp;</i
              >
              <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                >$:&nbsp;</i
              >
              <span class="cost-val" v-if="chosenData.orderDistributor.orderDistributorCostDTO.depositAmount">{{
                chosenData.orderDistributor.orderDistributorCostDTO.depositAmount | NumFormat
              }}</span>
              <span v-else>0</span>
            </span>
            <!-- <i class="asmd"> - </i>
            <span class="cost-info">
              <span class="cost-label">促销金额--??</span>
              <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                >￥:&nbsp;</i
              >
              <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                >$:&nbsp;</i
              >
              <span class="cost-val">{{
                chosenData.orderDistributor.orderDistributorDataDTO.promotionAmount | NumFormat
              }}</span>
            </span> -->
          </div>

          <div class="cost-line align-right">
            <span class="cost-info">
              <span class="cost-label">
                <i class="asmd"> =&nbsp; </i> 未付款金额 :
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                  >￥:&nbsp;</i
                >
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                  >$:&nbsp;</i
                >
              </span>
              <span class="cost-val">
                {{ totalPrice(chosenData.orderDistributor.orderDistributorCostDTO)}}
                <span v-show="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'">元</span>
              </span>
            </span>
          </div>
        </div>
        <!-------分销商结算信息（销售价）------>
        <div class="box-has-border" style="position: relative" v-if="checkOrderDistrG">
          <div class="text-center title">
            <span>分销商结算信息（销售价）</span>
          </div>
          <el-table
            border
            :data="chosenData.orderInfoDetailGoodsList"
            :summary-method="getSummaries"
            style="width: 100%"
            header-row-class-name="header-row"
            class="tableCenter"
            max-height="600"
            show-summary
          >
            <el-table-column
              align="center"
              type="index"
              fixed
              :min-width="50"
            >
            </el-table-column>
            <el-table-column
              align="center"
              prop="orderGoods.itemCode"
              label="存货编码"
              :min-width="120"
            >
            </el-table-column>
            <el-table-column
              align="center"
              prop="orderGoods.itemName"
              label="存货名称"
              show-overflow-tooltip
              :min-width="120"
            >
            </el-table-column>
            <el-table-column
                align="center"
                v-for="(item, index) in disGradeOption" :key="item.id" :label="item.name"
                :min-width="120"
              >
                <template slot-scope="scope">
                  <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                  >￥:&nbsp;</i
                >
                <i class="asmd" v-else-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                  >$:&nbsp;</i
                >
                  <span v-if="scope.row.disGradeArr.length>0">
                    <span v-if="scope.row.disGradeArr[index]">{{scope.row.disGradeArr[index].salePrice}}</span>
                    <span v-else>-</span>
                  </span>
                </template>
            </el-table-column>
            <!-- <el-table-column
                align="center"
                v-for="(item,index) in disGradeArr" :key="item.id" :label="item.name"
                prop="salePrice"
                :min-width="120"
              >
                <template slot-scope="scope">
                  <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                  >￥:&nbsp;</i
                >
                <i class="asmd" v-else-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                  >$:&nbsp;</i
                >
                  <span>{{scope.row.salePrice}}</span>
                </template>
            </el-table-column> -->
            <!-- <el-table-column align="center" label="公司结算价" :min-width="120">
              <template slot-scope="scope">
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                  >￥:&nbsp;</i
                >
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                  >$:&nbsp;</i
                >
                {{ scope.row.orderGoodsDistributorCosts.salePrice | NumFormat}}
              </template>
            </el-table-column> -->
          </el-table>
        </div>
        <!-------操作信息------>
        <div class="box-has-border">
          <div class="box-has-border">
            <div class="text-center title">
              <span>操作信息</span>
            </div>
            <div
              v-if="checkInvalid === 0 || checkCancel === 0 || checkclose === 0"
            >
              <el-form
                ref="form"
                :model="form"
                label-width="180px"
                :rules="rules"
              >
                <el-form-item
                  prop="remark"
                  label="操作备注:"
                  style="width: 80%; margin-top: 20px"
                >
                  <el-input
                    v-model="form.remark"
                    type="textarea"
                    :rows="5"
                    placeholder="请输入内容"
                  >
                  </el-input>
                </el-form-item>
              </el-form>
            </div>

           <div class="operation" style="margin: 20px 0">
              <span
                class="instruction"
                style="margin-left: 48px; margin-top: 20px"
                v-if="
                  checkInvalid === 0 || checkCancel === 0 || checkclose === 0
                "
              >
                当前可执行操作：
              </span>
              <span class="operation-part">
                <el-button
                  :loading="operateLoading"
                  class="mini-search-btn"
                  @click="onSubmit()"
                  v-if="checkCancel == 0 || checkInvalid == 0 || checkclose == 0"
                  >取消订单</el-button
                >
                <!-- <el-button
                  class="mini-browse-btn"
                  @click="handleDelivery"
                  v-if="
                    chosenData.orderSource === 50 &&
                    (chosenData.frontOrderStatus === 2 ||
                      chosenData.frontOrderStatus === 3)
                  "
                  >发货</el-button
                > -->
              </span>
            </div>

            <!-- 订单操作日志 -->
            <el-table
              :data="logs"
              border
              style="width: 100%"
              header-row-class-name="header-row"
              class="tableCenter"
            >
              <el-table-column align="center" label="操作者" prop="operator">
              </el-table-column>
              <el-table-column
                align="center"
                label="操作时间"
                prop="operateTime"
                :formatter="timeFormat"
              >
              </el-table-column>
              <!-- :formatter="formatStatus" -->
              <el-table-column
                align="center"
                label="操作类型"
                prop="operateType"
              >
              </el-table-column>
              <el-table-column
                align="center"
                show-overflow-tooltip
                label="操作说明"
              >
              <!---scope.row.operateType == 13---->
                <template slot-scope="scope">
                  <el-button
                    v-if="scope.row.type == 13"
                    size="mini"
                    type="text"
                    @click="handlePreviewStock(scope.row)"
                  >
                    查看详情
                  </el-button>
                  <span v-else size="mini" type="text">
                    {{ scope.row.operateData }}
                  </span>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>

        <!-- ERP操作的日志查看 -->
        <el-dialog :visible="logsShow" :before-close="closeLogsShow">
          <h3 style="text-align: center;margin-bottom:10px;">商品信息修改详情</h3>
          <div style="line-height:30px;">{{operateData}}</div>
          <!-- <h3 style="text-align: center;margin-bottom:10px;">商品信息修改详情</h3> -->
          <!-- <el-table
            border
            :data="modifyLogs"
            style="width: 100%"
            header-row-class-name="header-row"
            class="tableCenter"
          >
            <el-table-column
              align="center"
              label="ERP变更单号"
              prop="xOrderNo"
            ></el-table-column>
            <el-table-column
              align="center"
              label="修改类型"
              prop="updateType"
              :formatter="amendStatus"
            ></el-table-column>
            <el-table-column
              align="center"
              label="存货编码"
              prop="itemCode"
            ></el-table-column>
            <el-table-column
              align="center"
              label="存货名称"
              prop="itemName"
            ></el-table-column>
            <el-table-column align="center" label="价格">
              <template slot-scope="scope">
                <p
                  style="color: red; text-decoration: line-through"
                  v-if="scope.row.originalPrice != null"
                >
                  <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                    >￥:&nbsp;</i
                  >
                  <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                    >$:&nbsp;</i
                  >
                  {{ scope.row.originalPrice }}
                </p>
                <p>
                  <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                    >￥:&nbsp;</i
                  >
                  <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                    >$:&nbsp;</i
                  >
                  {{ scope.row.itemPrice }}
                </p>
              </template>
            </el-table-column>
            <el-table-column align="center" label="订购数量">
              <template slot-scope="scope">
                <p
                  style="color: red; text-decoration: line-through"
                  v-if="scope.row.originalCount != null"
                >
                  <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                    >￥:&nbsp;</i
                  >
                  <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                    >$:&nbsp;</i
                  >
                  {{ scope.row.originalCount }}
                </p>
                <p>{{ scope.row.itemCount }}</p>
              </template>
            </el-table-column>
            <el-table-column align="center" label="小计">
              <template slot-scope="scope">
                <p
                  style="color: red; text-decoration: line-through"
                  v-if="scope.row.originalAmountSum != null"
                >
                  <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                    >￥:&nbsp;</i
                  >
                  <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                    >$:&nbsp;</i
                  >
                  {{ scope.row.originalAmountSum }}
                </p>
                <p>
                  <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                    >￥:&nbsp;</i
                  >
                  <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                    >$:&nbsp;</i
                  >
                  {{ scope.row.amountSum }}
                </p>
              </template>
            </el-table-column>
          </el-table> -->
        </el-dialog>
      </div>
    </section>

    <!-- 查看收款单 -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="receoptShow"
      width="60%"
      :before-close="closeReceopt"
    >
      <el-table
        :data="chosenData.vouchers"
        border
        header-row-class-name="header-row"
        class="tableCenter"
        v-loading="loading"
      >
        <el-table-column align="center" label="收款单号：">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              @click="handleVouchers(scope.row)"
              >{{ scope.row.id }}</el-button
            >
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="ERP收款单编号"
          prop="voucherErpNo"
        >
        </el-table-column>
        <el-table-column align="center" label="支付金额">
          <template slot-scope="scope">
            <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
              >￥:&nbsp;</i
            >
            <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
              >$:&nbsp;</i
            >
            {{ scope.row.amount | NumFormat }}
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 查看退款申请单 -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="refundApplyShow"
      width="60%"
      :before-close="closeRefundApply"
    >
      <el-table
        :data="refundApplies"
        border
        header-row-class-name="header-row"
        class="tableCenter"
        v-loading="loading"
      >
        <el-table-column align="center" label="退款单号：">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              @click="handleRefundApply(scope.row)"
              >{{ scope.row.id }}</el-button
            >
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="业务类型"
          prop="businessTypes"
          :formatter="businessTypes"
        >
        </el-table-column>
        <el-table-column align="center" label="支付金额">
          <template slot-scope="scope">
            <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
              >￥:&nbsp;</i
            >
            <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
              >$:&nbsp;</i
            >
            {{ scope.row.amount | NumFormat }}
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 查看发货单 -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="InvoiceShow"
      width="60%"
      :before-close="closeDialog"
    >
      <el-dialog width="35%" :visible.sync="innerVisible" append-to-body>
        <!-- 物流 -->
        <div class="logistics" v-loading="loading">
          <div>
            <div class="log-right">
              <div style="max-height: 500px">
                <el-steps direction="vertical" :active="1" align-center>
                  <el-step
                    icon="el-icon-circle-check"
                    v-for="item in logistics.deliveryTrace"
                    :key="item.id"
                    style="margin-bottom: 15px"
                  >
                    <template slot="description">
                      <h4 class="span-left">
                        {{ timeFormatter(item.acceptTime) }}
                      </h4>
                      <span class="span-right">{{ item.acceptStation }}</span>
                      <div>&nbsp;</div>
                    </template>
                  </el-step>
                </el-steps>
              </div>
            </div>
          </div>
        </div>
      </el-dialog>
      <h4 class="beAmend">查看发货单</h4>
      <el-table
        :data="orderDeliveryList"
        border
        header-row-class-name="header-row"
        class="tableCenter"
      >
        <el-table-column align="center" label="发货单号">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              @click="handlePreview(scope.row)"
              >{{ scope.row.id }}</el-button
            >
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="配送方式"
          prop="distributionName"
        ></el-table-column>
        <el-table-column align="center" label="物流单号">
          <template slot-scope="scope">
            <div slot="reference" class="name-wrapper">
              <el-button
                size="mini"
                type="text"
                @click="handleLogisticsNo(scope.row)"
                >{{ scope.row.logisticsNo }}</el-button
              >
            </div>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="发货时间"
          prop="deliverTime"
          :formatter="timeFormat"
        ></el-table-column>
      </el-table>
    </el-dialog>

    <!-- 查看采购进度 -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="purchaseShow"
      width="60%"
      :before-close="closePurchaseShow"
    >
      <h4 class="beAmend">查看采购进度</h4>
      <el-table
        :data="purchaseData"
        v-loading="loading2"
        border
        header-row-class-name="header-row"
        class="tableCenter"
      >
        <el-table-column align="center" label="ERP采购单号"></el-table-column>
        <el-table-column
          align="center"
          label="日期"
          prop="time"
          :formatter="timeFormat"
        ></el-table-column>
        <el-table-column align="center" label="货品编码"></el-table-column>
        <el-table-column align="center" label="货品名称"></el-table-column>
        <el-table-column align="center" label="该单采购数量"></el-table-column>
        <el-table-column align="center" label="交货日期"></el-table-column>
      </el-table>
    </el-dialog>

    <el-dialog
      :modal-append-to-body="false"
      :visible="imageShow"
      width="30%"
      :before-close="closeimageShow"
    >
      <el-table :data="diyList" border header-row-class-name="header-row">
        <el-table-column
          align="center"
          label="diyId"
          prop="diyId"
        ></el-table-column>
        <el-table-column align="center" label="图片">
          <template slot-scope="scope">
            <el-image
              style="width: 50px; height: 50px"
              :src="scope.row.diyPic"
              fit="contain"
              :preview-src-list="[scope.row.diyPic]"
            >
            </el-image>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="数量"
          prop="diyNum"
        ></el-table-column>
      </el-table>
    </el-dialog>
    <!-- 查看活动 -->
    <el-dialog :visible="promotionShow" :before-close="cancelLook">
      <!-- <el-table border :show-header="true" ref="table" height="40" header-row-class-name="header-row" class="tableCenter orderGoods" style="text-align:center;">
        <el-table-column align="center" label="货品编号"></el-table-column>
        <el-table-column align="center" label="货品名称"></el-table-column>
        <el-table-column align="center" label="数量"></el-table-column>
        <el-table-column align="center" label="价格"></el-table-column>
        <el-table-column align="center" label="折后价格"></el-table-column>
      </el-table> -->
      <div v-loading="loading3">
        <!-- <div v-if="gradeRule !== null">
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
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                  >￥:&nbsp;</i
                >
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                  >$:&nbsp;</i
                >
                {{ scope.row.distributorPrice | NumFormat }}
              </template>
            </el-table-column>
            <el-table-column align="center" label="折后价格" prop="actualPrice">
              <template slot-scope="scope">
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                  >￥:&nbsp;</i
                >
                <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                  >$:&nbsp;</i
                >
                {{ scope.row.actualPrice | NumFormat }}
              </template>
            </el-table-column>
          </el-table>
        </div> -->
        <div v-if="promotions.length > 0">
          <div v-for="promotion in promotions" :key="promotion.id">
            <h4 style="margin:20px 0 10px;">活动名称：{{promotion.name}}</h4>
            <div v-if="promotion.rules.length>0">
              <div v-for="promotionRule in promotion.rules" :key="promotionRule.id">
                <el-row align="middle" type="flex" class="bg-purple-light">
                  <el-col :span="8" class="bg-purple">
                    <div>
                      <div style="font-weight: 500">
                        {{ "活动规则：" + promotionRule.ruleName }}
                      </div>
                    </div>
                  </el-col>
                  <el-col :span="16">
                    <div style="padding: 10px">
                      <div
                        v-for="condition in promotionRule.conditions"
                        :key="condition.id"
                      >
                        {{ "活动条件" + "：" + condition.conditionName }}
                      </div>
                    </div>
                  </el-col>
                </el-row>
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
                  <el-table-column align="center" label="数量" prop="itemCount">
                    <template slot-scope="scope">
                      <span>{{
                        scope.row.itemCount
                      }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column align="center" label="价格" prop="salePrice">
                    <template slot-scope="scope">
                      <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                        >￥:&nbsp;</i
                      >
                      <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                        >$:&nbsp;</i
                      >
                      {{ scope.row.salePrice | NumFormat }}
                    </template>
                  </el-table-column>
                  <el-table-column
                    align="center"
                    label="折后价格"
                    prop="actualPrice"
                  >
                    <template slot-scope="scope">
                      <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                        >￥:&nbsp;</i
                      >
                      <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                        >$:&nbsp;</i
                      >
                      {{ scope.row.actualPrice | NumFormat }}
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </div>
          </div>
        </div>
        <div v-if="spellGroups.length > 0">
          <div v-for="spellGroup in spellGroups" :key="spellGroup.id">
            <h4 style="margin:20px 0 10px;">拼团名称：{{spellGroup.name}}</h4>
            <div v-if="spellGroup.goods && spellGroup.goods.length>0">
              <el-table
                border
                :data="spellGroup.goods"
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
                <el-table-column align="center" label="拼团价" prop="groupSeckillPrice">
                  <template slot-scope="scope">
                    <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY'"
                      >￥:&nbsp;</i
                    >
                    <i class="asmd" v-if="chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'USD'"
                      >$:&nbsp;</i
                    >
                    {{ scope.row.groupSeckillPrice | NumFormat }}
                  </template>
                </el-table-column>
                <el-table-column align="center" label="虚拟拼团数" prop="virtualSum">
                  <template slot-scope="scope">
                    <span>{{
                      scope.row.virtualSum
                    }}</span>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="最小起拼团数" prop="minNum">
                  <template slot-scope="scope">
                    <span>{{
                      scope.row.minNum
                    }}</span>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="最大拼团数" prop="maxNum">
                  <template slot-scope="scope">
                    <span>{{
                      scope.row.maxNum
                    }}</span>
                  </template>
                </el-table-column>
              </el-table>
            </div>    
          </div>
        </div>
      </div>
    </el-dialog>
    <!-- 修改币种弹窗 -->
    <el-dialog
      title="修改支付币种"
      :visible="currencyChangeConfirm"
      :show-close="false"
      width="40%"
    >
      <div class="dialog-row">
        <span class="dialog-row-title">币种：</span>
        <el-select v-model="currencyType" placeholder="请选择币种">
          <el-option
            v-for="(item, index) in currencyTypeList"
            :key="index"
            :label="item.name"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </div>
      <div class="dialog-row">
        <span class="dialog-row-title">计算汇率：</span>
        <el-radio v-model="exchangeRateType" label="0">下单时汇率</el-radio>
        <el-radio v-model="exchangeRateType" label="1">当前汇率</el-radio>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button
          size="small"
          @click="currencyChangeConfirmClose"
          :disabled="currencyChangeLoading"
          >取 消</el-button
        >
        <el-button
          type="primary"
          size="small"
          @click="currencyChangeSure"
          :loading="currencyChangeLoading"
          >确 定</el-button
        >
      </div>
    </el-dialog>

    <!-- 支付凭证 -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="showRecordModel"
      width="30%"
      class="record-wrap"
      @close="showRecordModel = false"
    >
      <div class="record-img">
        <img :src="chosenData.voucherImg" alt="支付凭证" />
      </div>
    </el-dialog>

    <!-- 发货 -->
    <deliveryDialog
      v-if="deliveryShow"
      :pageFlag="'orderDetail'"
      :curOrderId="curOrderId"
      :curProductOrderNo="curProductOrderNo"
    ></deliveryDialog>

    <!-- 代金券 -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="historyDialog"
      width="60%"
      class="record-wrap"
      @close="historyDialog = false"
    >
      <el-table
          :data="historyData"
          header-row-class-name="header-row"
          border
          class="tableCenter"
        >
          <el-table-column
            align="center"
            label="序号"
            type="index"
            width="80"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            align="center"
            label="券号"
            prop="rebateVoucherNo"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            align="center"
            label="使用金额"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <span>{{ scope.row.amountFlag }} ¥{{ scope.row.useAmount }}</span>
              <span v-if="scope.row.amountFlag === '+'">(退还)</span>
            </template>
          </el-table-column>
        </el-table>
    </el-dialog>
  </div>
</template>

<script>
import {
  stockTypeFormatter,
  orderGoodsTypeFormatter,
  orderPaymentFormatter,
  orderStatusFormatter,
  orderSourceFormatter,
  booleanFormatter,
  invoiceTypeFormatter,
  invoiceTitleFormatter,
} from "@/views/order/orderUtils";
import { parseTime } from "@/utils/index";
import { timeFormat } from "@/utils/timeFormat";
// ======引用组件======
import editDistributionMode from "@/views/order/orderList/edit/editDistributionMode"; // 编辑配送方式
import editOtherInfo from "@/views/order/orderList/edit/editOtherInfo";
import editCostInfo from "@/views/order/orderList/edit/editCostInfo"; // 编辑费用信息
import editDeliveryInfo from "@/views/order/orderList/edit/editDeliveryInfo"; // 编辑收货信息
import editGoodsInfo from "@/views/order/orderList/edit/editGoodsInfo"; // 编辑收货信息
import invalid from "@/views/order/orderList/operation/invalid"; // 取消订单
import deliveryDialog from "../orderDelivery";
import { formatJson, setArr2, setArr } from "@/utils/common";
const _ = require("ramda");

export default {
  name: "orderDetail",
  components: {
    editDistributionMode,
    editOtherInfo,
    editCostInfo,
    editDeliveryInfo,
    editGoodsInfo,
    invalid,
    deliveryDialog,
  },
  computed: {
    /**
     * chosenData 下的字段
     * orderStatus:  订单状态 1、7待确认，2.已确认 3. 已完成 4. 退换货 5. 无效
     * frontOrderStatus: 0.全部 1.待确认 2待发货 3部分发货 4待收货 5已关闭 6已完成
     * factorySyncFlag 定制商品订单未同步工厂标志  0. 未同步 1. 已同步
     * this.chosenData.orderGoodsType订单商品类型：1-在库 2-在途  3-制定 4 MTO订单 5 直运订单
     * payWay：付款方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付,10.头条支付
     */
    synchronousERP() {
      // 是否有同步ERP按钮
      // 微信支付宝快钱，三个不显示同步按钮
      // payWay 支付方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付,10.头条支付
        if (
          (this.chosenData.orderExtendData && !this.chosenData.orderExtendData.orderErpNo) &&
          (this.chosenData.frontOrderStatus !== 5) &&
          (this.chosenData.orderDistributor.orderDistributorDataDTO.payWay == 3 || 
          this.chosenData.orderDistributor.orderDistributorDataDTO.payWay == 4)
        ) {
          return 0; // ==== 非现款订单，未同步erp是需手工同步
        } else if (
          (this.chosenData.orderExtendData && !this.chosenData.orderExtendData.orderErpNo) &&
          (this.chosenData.frontOrderStatus!== 5) &&
          this.chosenData.orderDistributor.orderDistributorDataDTO.payStatus === 3 &&
          (this.chosenData.orderDistributor.orderDistributorDataDTO.payWay === 1 ||
            this.chosenData.orderDistributor.orderDistributorDataDTO.payWay === 2 ||
            this.chosenData.orderDistributor.orderDistributorDataDTO.payWay === 5 ||
            this.chosenData.orderDistributor.orderDistributorDataDTO.payWay === 6 ||
            this.chosenData.orderDistributor.orderDistributorDataDTO.payWay === 7 || 
            this.chosenData.orderDistributor.orderDistributorDataDTO.payWay === 8 ||
            this.chosenData.orderDistributor.orderDistributorDataDTO.payWay === 9 ||
            this.chosenData.orderDistributor.orderDistributorDataDTO.payWay === 10)
        ) {
          return 0; // ==== 现款订单，已付款，且未同步erp订单
        }
    },
    synchronousYC() {
        if (
          (this.lookImage) &&
          (this.chosenData.orderExtendData) && (this.chosenData.orderExtendData.orderFactoryNo === undefined ||
            this.chosenData.orderExtendData.orderFactoryNo === "") &&
          (this.chosenData.frontOrderStatus !== 5) &&
          (this.chosenData.orderDistributor.orderDistributorDataDTO.payWay == 3 || 
          this.chosenData.orderDistributor.orderDistributorDataDTO.payWay == 4)
        ) {
          return 0; // ==== 非现款订单，未同步erp是需手工同步
        } else if (
          (this.lookImage) &&
          (this.chosenData.orderExtendData && !this.chosenData.orderExtendData.orderFactoryNo) &&
          (this.chosenData.frontOrderStatus !== 5) &&
          this.chosenData.orderDistributor.orderDistributorDataDTO.payStatus === 3 &&
          (this.chosenData.orderDistributor.orderDistributorDataDTO.payWay === 1 ||
            this.chosenData.orderDistributor.orderDistributorDataDTO.payWay === 2 ||
            this.chosenData.orderDistributor.orderDistributorDataDTO.payWay === 5 ||
            this.chosenData.orderDistributor.orderDistributorDataDTO.payWay === 6 ||
            this.chosenData.orderDistributor.orderDistributorDataDTO.payWay === 7 || 
            this.chosenData.orderDistributor.orderDistributorDataDTO.payWay === 8 ||
            this.chosenData.orderDistributor.orderDistributorDataDTO.payWay === 9 ||
            this.chosenData.orderDistributor.orderDistributorDataDTO.payWay === 10)
        ) {
          return 0; // ==== 现款订单，已付款，且未同步erp订单
        }
    },
    isProduct() {
      if (
        (this.lookImage) &&
        this.chosenData.orderExtendData.orderFactoryNo !== undefined &&
        this.chosenData.orderExtendData.orderFactoryNo !== null &&
        this.chosenData.orderExtendData.orderFactoryNo !== "" &&
        this.chosenData.orderExtendData.orderFactoryNo.indexOf("O") !== -1 &&
        (this.chosenData.isHaixingProduct === undefined ||
          this.chosenData.isHaixingProduct === null ||
          this.chosenData.isHaixingProduct === 0)
      ) {
        return 0; // ==== 需发生产订单，未发生产
      }
    },
    /**
     * invoiceFlag 是否开票
     * invoiceType 发票类型 1: 普通发票 2: 增值税发票
     * invoiceTitleType 发票抬头 1: 个人 2: 单位
     */
    orPersonalInvoice() {
      // 开具什么发票
      if (
        this.chosenData.orderInfo.invoiceFlag === 1 &&
        this.chosenData.orderInvoice.invoiceType == 1 &&
        this.chosenData.orderInvoice.invoiceTitleType == 1
      ) {
        return 0; // ==== 开发票 && 普通发票 && 个人 ====
      }
      if (
        this.chosenData.orderInfo.invoiceFlag === 1 &&
        this.chosenData.orderInvoice.invoiceType == 1 &&
        this.chosenData.orderInvoice.invoiceTitleType == 2
      ) {
        return 1; // ==== 开发票 && 普通发票 && 单位 ====
      }
      if (this.chosenData.orderInfo.invoiceFlag === 1 && this.chosenData.orderInvoice.invoiceType == 2) {
        return 2; // ==== 开发票 && 增值税发票 ====
      }
    },
    taxNo() {
      // 纳税人识别号
      if (this.chosenData.orderInfo.invoiceFlag === 1 && this.chosenData.orderInvoice.invoiceType == 2) {
        return 0; // ==== 开发票 && 增值税发票 ====
      } else if (
        this.chosenData.orderInfo.invoiceFlag === 1 &&
        this.chosenData.orderInvoice.invoiceType == 1 &&
        this.chosenData.orderInvoice.invoiceTitleType == 2
      ) {
        return 0; // ==== 开发票 && 普通发票 && 单位 ====
      }
    },
    VATNumber() {
      // 增值税发票注册电话 || 增值税发票银行账户 || 增值税发票注册地址 || 增值税发票开户银行
      if (this.chosenData.orderInfo.invoiceFlag === 1 && this.chosenData.orderInvoice.invoiceType == 2) {
        return 0; // ==== 开发票 && 增值税发票 ====
      }
    },
    // 发票抬头
    invoiceUp() {
      if (
        this.chosenData.orderInfo.invoiceFlag === 1 &&
        this.chosenData.orderInvoice.invoiceType == 1 &&
        this.chosenData.orderInvoice.invoiceTitleType == 1
      ) {
        return 0; // ==== 开发票 && 普通发票 && 个人 =====
      } else if (
        this.chosenData.orderInfo.invoiceFlag === 1 &&
        this.chosenData.orderInvoice.invoiceType == 1 &&
        this.chosenData.orderInvoice.invoiceTitleType == 2
      ) {
        return 0; // ==== 开发票 && 普通发票 && 单位 =====
      } else if (this.chosenData.orderInfo.invoiceFlag === 1 && this.chosenData.orderInvoice.invoiceType == 2) {
        return 0; // ==== 开发票 && 增值税 =====
      }
    },
    checkOperation() {
      // ..当前可执行操作
      // frontOrderStatus 订单状态 1、7: 待确认 2: 待发货 3: 部分发货 4: 已发货 5:已关闭 6: 已完成
      // this.chosenData.orderGoodsType 订单商品类型：1-在库 2-在途  3-制定 4 MTO订单 5 直运订单
      if (
        this.chosenData.frontOrderStatus == 1 &&
        !this.lookImage
      ) {
        return 0; // ..在库订单 && 在库
      } else if (
        this.chosenData.frontOrderStatus == 2 &&
        !this.lookImage
      ) {
        return 0; // ..在途订单 && 在库
      }

      if (
        this.chosenData.frontOrderStatus == 1 &&
        this.chosenData.orderGoodsType == 1
      ) {
        return 0;
      } else if (
        this.chosenData.frontOrderStatus == 1 &&
        this.chosenData.orderGoodsType == 2
      ) {
        return 0;
      } else if (
        this.chosenData.frontOrderStatus == 2 &&
        this.chosenData.orderGoodsType == 1
      ) {
        return 0;
      } else if (
        this.chosenData.frontOrderStatus == 2 &&
        this.chosenData.orderGoodsType == 2
      ) {
        return 0;
      } else if (
        this.chosenData.frontOrderStatus == 3 &&
        this.chosenData.orderGoodsType != 3
      ) {
        return 0;
      }
    },

    checkInvalid() {
      // ..作废订单是否显示
      // frontOrderStatus 订单状态 1、7.待确认2.已确认 3. 部分发货 4. 全部发货 5.已关闭  6.已完成
      // orderGoodsType  订单商品类型：1-在库 2-在途  3-制定 4 MTO订单 5 直运订单
      // stockType: 库存类型：1在库 2在途 3预售(mto) 4 在途+在库 5、委外
      if (
        (this.chosenData.frontOrderStatus == 1 || this.chosenData.frontOrderStatus == 7) &&
        this.chosenData.orderInfo.stockType != 3
      ) {
        return 0;
      }
    },

    checkCancel() {
      // ..取消订单是否显示
      // if (
      //   this.chosenData.frontOrderStatus == 2 &&
      //   this.chosenData.orderInfo.stockType != 3
      // ) {
      if (
        this.chosenData.frontOrderStatus == 2
      ) {
        return 0;
      }
    },

    checkclose() {
      if (
        this.chosenData.frontOrderStatus == 3 &&
        this.chosenData.orderInfo.stockType != 3
      ) {
        return 0;
      }
    },
    // 计算商品重量
    totalWeight() {
      let wegiht = 0
      this.chosenData.orderInfoDetailGoods.forEach(item => {
        if (item.orderGoods.weight) {
          wegiht += Number(item.orderGoods.weight) * Number(item.orderGoods.itemCount)
        }
      })
      return wegiht
    },
    checkPermission() {
      // 订单状态 1、7: 待确认 2: 待发货 3: 部分发货 4: 待收货 5:已关闭 6: 已完成
      return this.chosenData.frontOrderStatus;
    },
    checkOrderGoodsType() {
      // 订单商品类型：1-在库 2-在途  3-制定
      return this.chosenData.orderGoodsType;
    },
    checkPayStatus() {
      // 付款状态 case 1: 未付款 2: 部分付款 3: 已付款
      return this.chosenData.payStatus;
    },
    payWay() {
      // payWay 支付方式 1: 支付宝 2: 微信 3: 区间结算 4: 银行转账 5: 余额支付
      return this.chosenData.payWay;
    },
    priceCount() {
      // 价格计算
      return this.chosenData.goods.salePrice;
    },
    orderId() {
      return this.$store.getters.orderId;
    },
    orderDetail() {
      return this.$store.getters.orderDetail;
    },
    orderBatchPage() {
      return this.$route.query.batchPage;
    },
    checkOrderG() {
      return this.$route.query.orderG;
    },
    checkOrderDistrG() {
      return this.$route.query.orderDistrG;
    },
    checkOrderCustG() {
      return this.$route.query.orderCustG;
    },
    checkOrderCustSyncG() {
      return this.$route.query.orderCustSyncG;
    },
    checkSyncErpFailG() {
      return this.$route.query.syncErpFailG;
    },
    checkSyncFacFailG() {
      return this.$route.query.syncFacFailG;
    },
    checkSyncUndeliverG() {
      return this.$route.query.syncUndeliverG;
    },
    checkStockG() {
      return this.$route.query.stockG;
    },
    checkRefundApply() {
      return this.$route.query.RefundApply;
    },
    checkVmiG() {
      return this.$route.query.vmiG;
    },
  },
  created() {
    this.getOrderTypes()
    this.requestData()
    // 弹框显示暂不需要
    // if (this.$route.query.recopetDia) {
    //   this.receoptShow = true;
    // }
  },
  activated() {
    this.requestData()
  },
  data() {
    return {
      recopetDia: false,
      diyList: [],
      newDiyList: [],
      lookImage: false, //是否展示查看图片
      lookExchange: false, // 是否展示兑换卡信息
      imageShow: false, //图片查看
      newDiyShow: false, // 查看新定制信息
      loading: false,
      loading2: false,
      loading3: false,
      synLoading: false,
      synCYLoading: false,
      operateLoading: false,
      cancelLoading: false,
      operateLoading: false,
      applyId: null,
      orderIdList: [],
      chosenData: {
        orderDelivery:{},
        orderDistributor:{
          orderDistributorCostDTO:{},
          orderDistributorDataDTO:{}
        },
        orderDistributors:[
          {
            orderDistributorCostDTO:{},
            orderDistributorDataDTO:{}
          }
        ],
        orderExtendData:{},
        orderInfo:{},
        orderInfoDetailGoods: [{
          orderGoods:{},
          orderGoodsDistributorCost:{
            salePrice: 0
          },
          orderGoodsCustomerCost:{
            salePrice: 0
          },
          orderGoodsDiy:{}
        }],
        orderInfoDetailGoodsList:[{
          orderGoods:{},
          orderGoodsDistributorCosts:[],
          orderGoodsDiy:{}
        }],
        orderInvoice:{}
      },
      summariesData: {},
      orderGoods: [], // 商品信息
      // approvalFlows: [],
      // distributions: [],
      logs: [], // 订单日志
      checkDetail: {}, // 审批数据
      // deliverBills: [],
      form: {
        remark: "",
      },
      invoice: {},
      logsShow: false,
      operateData: '',
      modifyLogs: [],
      InvoiceShow: false, // ..发货单弹框
      receoptShow: false, //, .收款弹窗
      purchaseShow: false, // ..采购进度弹框
      innerVisible: false,
      refundApplyShow: false, // 退款申请单
      promotionIds: {
        conditionIds: [],
      },
      rules: {
        remark: [
          { required: true, message: "请输入操作说明", trigger: "blur" },
        ],
      },
      logistics: [],
      purchaseData: [
        {
          time: 1553164124000,
        },
      ],
      promotionShow: false,
      promotions: [], // 促销活动
      spellGroups: [], // 秒杀拼团
      gradeRule: null,
      currencyChangeConfirm: false, // 是否显示修改币种确认框
      currencyTypeList: [
        { value: "CNY", name: "人民币" },
        { value: "USD", name: "美元" },
      ],
      exchangeRateType: "0",
      currencyType: "",
      currencyChangeLoading: false, // 修改币种是否处于加载状态
      showRecordModel: false, // 支付凭证
      synIsProduct: false, // 是否已发生产
      deliveryShow: false,
      curOrderId: "",
      curProductOrderNo: "",
      orderTypes:[], // 订单类型
      disGradeOption: [], // 分销商等级名称
      orderDeliveryList: [], // 发货单列表
      refundApplies: [], // 退款申请单列表
      // 代金券
      historyData: [],
      historyDialog: false,
    };
  },
  methods: {
    // 计算订单总金额(未付款金额)
    totalPrice (row) {
      row.payAmount = row.payAmount ? row.payAmount : 0
      row.paidAmount = row.paidAmount ? row.paidAmount : 0
      row.depositAmount = row.depositAmount ? row.depositAmount : 0
      let price = this.FloorNum(row.payAmount - row.paidAmount - row.depositAmount).toFixed(2)
      return price
    },
    // 计算单个商品价格（小计）
    saleTotalAmount (row) {
      let itemVmiCount = row.orderGoods.itemVmiCount ? row.orderGoods.itemVmiCount : 0
      let salePrice = 0
      if (row.orderGoodsDistributorCost) {
        // 标品
        salePrice = parseFloat(row.orderGoodsDistributorCost.salePrice)
      } else {
        // 定制
        salePrice = parseFloat(row.orderGoodsCustomerCost.salePrice)
      }
      let total = this.FloorNum(Number(Number((salePrice - parseFloat(row.orderDiscount)).toFixed(2))) * (parseInt(row.orderGoods.itemCount) + itemVmiCount)).toFixed(2)
      return total
    },
    // 分销商结算总价
    getSummaries (param) {
      let summariesData = this.chosenData.orderInfoDetailGoodsList
      let tname = this.chosenData.orderDistributor.orderDistributorDataDTO.currencyType === 'CNY' ? '合计（￥）' : '合计（$）'
        const sums = ['','',tname,0,0,0,0,0,0,0,0,0,0,0,0,0];
        if (summariesData.length>0) {
          summariesData.map(item => {
            if (item.disGradeArr && item.disGradeArr.length>0) {
              item.disGradeArr.map((obj, index) => {
                sums[index + 3] += parseFloat(obj.salePrice)
              })
            }
          })
        }
        return sums;
    },
    getOrderTypes () {
      // 订单类型
      this.$http.orderTypeList(this, {page:1, size:1000}) 
        .then((result) => {
          if (result.success) {
            this.orderTypes = result.data.list
          }
        });
    },
    cancelLook() {
      this.promotionShow = false;
    },
    // 查看商品活动
    lookPromotion() {
      this.promotionShow = true;
      this.loading3 = true;
      this.gradeRule = null;
      this.promotions = []
      let info = {
        promotionIds: [],
        spellGroupIds: []
      }
      this.promotionIds.conditionIds.forEach(item => {
        if (item.goodsPromotionId) {
          info.promotionIds.push(item.goodsPromotionId)
        }
        if (item.spellGroupId) {
          info.spellGroupIds.push(item.spellGroupId)
        }
      })
      if (info.promotionIds.length > 0) {
        info.promotionIds = setArr2(info.promotionIds)
      }
      if (info.spellGroupIds.length > 0) {
        info.spellGroupIds = setArr2(info.spellGroupIds)
      }
      
      this.$http.orderPromotion(this, info)  
        .then((res) => {
          if (res.success) {
            // 促销活动
            if (
              res.data.promotions !== undefined &&
              res.data.promotions !== null &&
              res.data.promotions.length > 0
            ) {
              res.data.promotions.forEach((promotion) => {
                promotion.rules.forEach(rule => {
                  rule.goods = [];
                  rule.conditions.forEach((condition) => {
                  // 判断条件是否一样
                  // this.chosenData.goods.forEach((item) => {
                  //   // 组装活动规则对应的货品列表
                  //   if (
                  //     (rule.target === 1 &&
                  //       item.orderPromotionId !== undefined &&
                  //       item.orderPromotionId !== null &&
                  //       item.orderPromotionId === condition.id) ||
                  //     (rule.target !== 1 &&
                  //       item.goodsPromotionId !== undefined &&
                  //       item.goodsPromotionId !== null &&
                  //       item.goodsPromotionId === condition.id)
                  //   ) {
                  //     // 活动促销
                  //     rule.goods.push(item);
                  //   }
                  // });
                  this.chosenData.orderInfoDetailGoods.forEach(item => {
                    let activityId = item.orderGoodsDistributorCost.goodsPromotionId ? item.orderGoodsDistributorCost.goodsPromotionId : item.orderGoodsDistributorCost.spellGroupId
                    if (activityId === condition.id) {
                      rule.goods.push({
                        itemCode: item.orderGoods.itemCode,
                        itemName: item.orderGoods.itemName,
                        itemCount: item.orderGoods.itemCount,
                        salePrice: item.orderGoodsDistributorCost.salePrice,
                        actualPrice: item.orderGoodsDistributorCost.actualPrice
                      })
                    }
                  })
                });
                })
               
              });
              this.promotions = res.data.promotions;
            }
           
            // 拼团秒杀活动
            if (res.data.spellGroups && res.data.spellGroups.length > 0) {
              let spellArr = res.data.spellGroups
              spellArr.forEach((spell, index) => {
                this.$http.groupseckillDetail(this, {id: spell.id}).then(element => {
                  if (element.success) {
                    spell.goods = element.data.goods
                    if(index === spellArr.length - 1) {
                      this.spellGroups = spellArr
                    }
                  }
                })
              })
            }
          }
          this.loading3 = false;
        });
    },
    getSpellGroups (data) {
      return new Promose((resolve, reject) => {
        let flag = false
        data.forEach((spell, index) => {
          this.$http.groupseckillDetail(this, {id: spell.id}).then(element => {
            if (element.success) {
              spell.goods = element.data.goods
              if(index === data.length) {
                flag = true
              }
            }
          })
        })
        if(flag) {
        }
      })
    },
    businessTypes(r, c, v) {
      // 业务类型
      switch (v) {
        case 1:
          return "订单取消";
          break;
        case 2:
          return "订单变更";
          break;
        default:
          return "-";
      }
    },
    showImage(raw) {
      this.diyList = raw.diyList;
      this.imageShow = true;
    },
    // 下载生产图片
    downImage(row) {
      let url = row.generateImage;
      if (url) {
        if (
          url.indexOf(".png") > -1 ||
          url.indexOf(".jpeg") > -1 ||
          url.indexOf(".jpg") > -1
        ) {
          this.handledownImg(url, "生产图");
        } else {
          window.open(url);
        }
      } else {
        this.$message({
          type: "info",
          message: "生产图信息错误！",
        });
      }
    },
    // 保存图片
    handledownImg(imgsrc, name) {
      var userAgent = navigator.userAgent; // 取得浏览器的 userAgent 字符串
      var isOpera = userAgent.indexOf("Opera") > -1;
      let image = new Image();
      // 解决跨域 Canvas 污染问题
      image.setAttribute("crossOrigin", "anonymous");
      image.onload = function () {
        let canvas = document.createElement("canvas");
        canvas.width = image.width;
        canvas.height = image.height;
        let context = canvas.getContext("2d");
        context.drawImage(image, 0, 0, image.width, image.height);
        let url = canvas.toDataURL("image/png"); // 得到图片的 base64 编码数据
        if (
          userAgent.indexOf("Trident") > -1 ||
          (userAgent.indexOf("compatible") > -1 &&
            userAgent.indexOf("MSIE") > -1 &&
            !isOpera)
        ) {
          // 判断是否 Edge/IE 浏览器
          var bstr = atob(url.split(",")[1]);
          var n = bstr.length;
          var u8arr = new Uint8Array(n);
          while (n--) {
            u8arr[n] = bstr.charCodeAt(n);
          }
          // 创建blob对象
          var blob = new Blob([u8arr]);
          // 调用浏览器的方法，调起 IE 的下载流程
          window.navigator.msSaveOrOpenBlob(blob, name + ".png");
        } else {
          let a = document.createElement("a"); // 生成一个 a 元素
          let event = new MouseEvent("click"); // 创建一个单击事件
          a.download = name || "photo"; // 设置图片名称
          a.href = imgsrc; // 将图片 URL 设置为 a.href 属性
          a.dispatchEvent(event); // 触发 a 的单击事件
        }
      };
      image.src = imgsrc;
    },
    // 查看标签
    labelPreview(raw) {
      this.$router.push({
        name: "pdfPreview",
        params: { url: raw.orderGoodsDiy.labelUrl },
      });
      // window.open('/static/pdf/web/viewer.html?file=' + raw.newDiy.labelUrl)
      // this.$router.push({ name: 'labelPreview', params: { url: raw.newDiy.labelUrl} })
    },
    // 生成标签
    label(raw, index) {
      this.$http.createLable(this, {id: raw.orderGoodsDiy.id})  
        .then((res) => {
          if (res.success) {
            raw.orderGoodsDiy.labelUrl = res.data[0].labelUrl;
          }
        });
    },
    onSubmit() {
      this.$refs["form"].validate((valid) => {
        // 必填验证
        if (valid) {
          this.operateOrder();
        } else {
          return false;
        }
      });
    },
    operateOrder() {
      // 操作类型：1.作废订单（未确认） 2.取消订单（已确认，待发货） 3.关闭订单（部分发货）
      let str = "确定取消订单吗？";
      let hint = "订单取消成功。";
      this.$confirm(str, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          this.operateLoading = true;
          this.$http.orderCancel(this, {
             id: this.chosenData.orderInfo.id,
            remark: this.form.remark
          })  
            .then((res) => {
              this.operateLoading = false;
              if (res.success) {
                this.$message({
                  message: hint,
                  type: "success",
                  duration: 3 * 1000,
                });
                this.requestData();
              }
            });
        })
        .catch((_) => {});
    },
    requestData() {
      // 页面数据请求
      this.updateMainData(); // 若当前orderId与vuex的不同，则更新vuex中的id和detail
      this.orderLog(); // 操作日志
    },

    updateMainData() {
      // 订单数据
      this.loading = true;
      this.lookImage = false;
      this.dataDetail().then(res => {
        if (res.success && res.data.orderInfo) {
          // 订单类型
          this.orderTypes.forEach(item => {
            if(item.id === res.data.orderInfo.orderTypeId) {
              res.data.orderInfo.orderTypeName = item.name
            }
          })
          // 当前汇率
          let currentRates = res.data.orderDistributor.orderDistributorDataDTO.currentRates
          // 商品信息
          if (res.data.orderInfoDetailGoods !== undefined) {
            res.data.orderInfoDetailGoods.forEach(element => {
              // 价格折扣
              let salePrice = 0, actualPrice = 0, orderCost = {}
              if (element.orderGoodsCustomerCost) {
                // 定制
                // salePrice = this.FloorNum(element.orderGoodsCustomerCost.salePrice / currentRates)
                // actualPrice = this.FloorNum(element.orderGoodsCustomerCost.actualPrice / currentRates)
                salePrice = element.orderGoodsCustomerCost.salePrice
                actualPrice = element.orderGoodsCustomerCost.actualPrice
                orderCost = element.orderGoodsCustomerCost
              } else {
                // 标品
                // salePrice = this.FloorNum(element.orderGoodsDistributorCost.salePrice / currentRates)
                // actualPrice = this.FloorNum(element.orderGoodsDistributorCost.actualPrice / currentRates)
                salePrice = element.orderGoodsDistributorCost.salePrice
                actualPrice = element.orderGoodsDistributorCost.actualPrice
                orderCost = element.orderGoodsDistributorCost
              }
             
              element.orderDiscount = this.FloorNum(salePrice - actualPrice)
              if(element.orderGoods.goodsType === 1 && element.itemVmiCount) {
                element.itemCount = element.itemCount - element.itemVmiCount;
              }
              
              if (element.orderGoodsDiy !== undefined) {
                // 定制商品
                this.lookImage = true;
                if (element.orderGoodsDiy.exchangeOrderId !== undefined) {
                  this.lookExchange = true
                }
                let labelUrl = element.orderGoodsDiy.labelUrl ? element.orderGoodsDiy.labelUrl : ''
                this.$set(element.orderGoodsDiy, 'labelUrl', labelUrl)
              }
              // 查看活动 -- 促销活动
              let activity = orderCost.goodsPromotionId ? orderCost.goodsPromotionId : orderCost.spellGroupId
              if (
                orderCost.goodsPromotionId !== undefined &&
                orderCost.goodsPromotionId !== null &&
                this.promotionIds.conditionIds.indexOf(orderCost.goodsPromotionId) < 0
              ) {
                this.promotionIds.conditionIds.push({
                  goodsPromotionId: orderCost.goodsPromotionId 
                });
              } else if (
                orderCost.spellGroupId !== undefined &&
                orderCost.spellGroupId !== null &&
                this.promotionIds.conditionIds.indexOf(orderCost.spellGroupId) < 0
              ) {
                this.promotionIds.conditionIds.push({
                  spellGroupId: orderCost.spellGroupId
                });
              }
              if (
                orderCost.orderPromotionId !== undefined &&
                orderCost.orderPromotionId !== null &&
                this.promotionIds.conditionIds.indexOf(
                  orderCost.orderPromotionId
                ) < 0
              ) {
                this.promotionIds.conditionIds.push(orderCost.orderPromotionId);
              }
            })
          }

          // 费用信息
          // let costData = res.data.orderDistributor.orderDistributorCostDTO
          // costData.goodsAmount = costData.goodsAmount ? this.FloorNum(costData.goodsAmount / currentRates) : 0 // 商品总金额
          // costData.orderPromotionAmount = costData.orderPromotionAmount ? this.FloorNum(costData.orderPromotionAmount / currentRates) : 0  // 订单折扣
          // costData.goodsPromotionAmount = costData.goodsPromotionAmount ? this.FloorNum(costData.goodsPromotionAmount / currentRates) : 0 // 商品促销折扣
          // costData.distributionAmount = costData.distributionAmount ? this.FloorNum(costData.distributionAmount / currentRates) : 0  // 配送费
          // costData.payAmount = costData.payAmount ? this.FloorNum(costData.payAmount / currentRates) : 0  // 订单总金额
          // costData.paidAmount = costData.paidAmount ? this.FloorNum(costData.paidAmount / currentRates) : 0 // 已付款金额
          // costData.depositAmount = costData.depositAmount ? this.FloorNum(costData.depositAmount / currentRates) : 0  // 使用余额

          // 分销商物流费用
          let logisticsArr = [] //分销商物流费用
          let disGradeArr = [] // 分销商等级列表
          if (res.data.orderInfoDetailGoodsList && res.data.orderInfoDetailGoodsList.length > 0) {
             this.summariesData = res.data.orderInfoDetailGoodsList
             res.data.orderInfoDetailGoodsList.push({
              orderGoods: {
                itemCode: '',
                itemName: '运费'
              },
              orderGoodsDistributorCost: [],
              disGradeArr: []
            })   
          }
          
          if (res.data.orderDistributors && res.data.orderDistributors.length>0) {
            res.data.orderDistributors.forEach(item => {
              logisticsArr.push({
                id: item.orderDistributorCostDTO.distributorId,
                name: item.orderDistributorDataDTO.distributorName,
                // freight: this.FloorNum(item.orderDistributorCostDTO.distributionAmount / currentRates) // 物流费用
                freight: item.orderDistributorCostDTO.distributionAmount // 物流费用
              })
            })
           
          }
          // 分销商结算信息（销售价）
          let logiArr = []  // 运费
          if (res.data.orderInfoDetailGoodsList && res.data.orderInfoDetailGoodsList.length > 0) {
            res.data.orderInfoDetailGoodsList.forEach((order, index) => {
              disGradeArr = []
              if (order.orderGoodsDistributorCosts && order.orderGoodsDistributorCosts.length>0) {
                order.orderGoodsDistributorCosts.forEach(item => {
                    // 分销商等级价表头名称
                    this.disGradeOption.push({
                      id: item.distributorId,
                      name: item.distributorName
                    })
                    // 分销商等级价
                    disGradeArr.push({
                      id: item.distributorId,
                      name: item.distributorName,
                      salePrice: item.actualPrice * order.orderGoods.itemCount
                    })
                    // 分销商运费
                    logisticsArr.forEach(log => {
                      if (item.distributorId === log.id) {
                        logiArr.push({
                          id: item.distributorId,
                          name: item.distributorName,
                          salePrice: log.freight
                        })
                      }
                    })
                })
                this.disGradeOption = setArr(this.disGradeOption)
              }
              // disGradeArr = setArr(disGradeArr)
              logiArr = setArr(logiArr)
                if (index < res.data.orderInfoDetailGoodsList.length - 1) {
                  order.disGradeArr = disGradeArr
                } else if (index === res.data.orderInfoDetailGoodsList.length - 1) {
                  order.disGradeArr = logiArr
                }
                order.disGradeArr = setArr(order.disGradeArr)
                // 排序
                this.disGradeOption.sort((a, b) => {
                  return a.id - b.id
                })
                order.disGradeArr.sort(function(a, b) {
                  return a.id - b.id
                })
            })
          }
         
          this.chosenData = res.data;
        }
        res.success ? (this.loading = false) : (this.loading = false);
      })
    },
    FloorNum(num) {
      return Math.floor(num * 10000) / 10000
    },
    dataDetail() {
       return new Promise((resolve, reject) => {
        // 判断详情来源
        let type = Number(this.$route.query.type)
        if (type === 1) {
          // 订单详情
          this.$http.orderDetail(this, { id: this.$route.query.orderId }).then(res => {
            resolve(res)
          }).catch(() => {
            reject()
          })
        } else if (type === 2) {
          // 分销订单详情
          this.$http.distributionOrderDetail(this, { id: this.$route.query.orderId }).then(res => {
            resolve(res)
          }).catch(() => {
            reject()
          })
        } else if (type === 3) {
          // 柔性定制订单详情
          this.$http.customerDiyOrderDetail(this, { id: this.$route.query.orderId }).then(res => {
            resolve(res)
          }).catch(() => {
            reject()
          })
        } else if (type === 4) {
          // 柔性同步订单详情
          this.$http.customerSyncOrderDetail(this, { id: this.$route.query.orderId }).then(res => {
            resolve(res)
          }).catch(() => {
            reject()
          })
        } else if (type === 5) {
          // 同步ERP失败订单详情
          this.$http.syncERPFailDetail(this, { id: this.$route.query.orderId }).then(res => {
            resolve(res)
          }).catch(() => {
            reject()
          })
        } else if (type === 6) {
          // 同步工厂失败订单详情
          this.$http.syncFactoryFailDetail(this, { id: this.$route.query.orderId }).then(res => {
            resolve(res)
          }).catch(() => {
            reject()
          })
        } else if (type === 7) {
          // 长时间未发货订单详情
          this.$http.syncUndeliveredFailDetail(this, { id: this.$route.query.orderId }).then(res => {
            resolve(res)
          }).catch(() => {
            reject()
          })
        } else if (type === 8) {
          // VMI订单详情
          this.$http.vimOrderDetail(this, { id: this.$route.query.orderId }).then(res => {
            resolve(res)
          }).catch(() => {
            reject()
          })
        }
       })
    },

    exportDownload() {
      // 导出下单清单操作
      let goodsData = []
      this.chosenData.orderInfoDetailGoods.forEach(item => {
        goodsData.push({
          goodsNo: item.orderGoods.goodsNo,
          itemCode: item.orderGoods.itemCode,
          itemName: item.orderGoods.itemName,
          specsName: item.orderGoods.specsName,
          materialName: item.orderGoodsDiy ? item.orderGoodsDiy.materialName : '',
          modelName: item.orderGoodsDiy ? item.orderGoodsDiy.modelName : '',
          salePrice: item.orderGoodsDistributorCost ? item.orderGoodsDistributorCost.salePrice : item.orderGoodsCustomerCost.salePrice,
          orderDiscount: item.orderDiscount,
          exchangeOrderId: item.orderGoodsDiy ? item.orderGoodsDiy.exchangeOrderId: '',
          distributorCompanyName: item.orderGoodsDiy ? item.orderGoodsDiy.distributorCompanyName : '',
          salesName: item.orderGoodsDiy ? item.orderGoodsDiy.salesName : '',
          itemCount: item.orderGoods.itemCount,
          itemVmiCount: item.orderGoods.itemVmiCount ? item.orderGoods.itemVmiCount : 0,
          deliverCount: item.orderGoods.deliverCount ? item.orderGoods.deliverCount : 0,
          unDeliverCount: item.orderGoods.unDeliverCount ? item.orderGoods.unDeliverCount : 0,
          previewImage: item.orderGoodsDiy ? item.orderGoodsDiy.previewImage : '',
          labelUrl: item.orderGoodsDiy ? item.orderGoodsDiy.labelUrl : '',
          actualPrice: item.orderGoodsDistributorCost ? item.orderGoodsDistributorCost.actualPrice : item.orderGoodsCustomerCost.actualPrice
        })
      })
      import('@/utils/Export2Excel').then(excel => {
          let tHeader = [], filterVal = []
          if (this.lookImage) {
            // 定制
            if (this.lookExchange) {
              tHeader = ['商品编号', '存货编码', '存货名称', '材质', '型号', '价格', '价格折扣', '兑换卡归属订单号', '发卡分销商', '发卡业务员', '订购数量', '已发数量', '未发数量','图片','标签','小计']
              filterVal = ['goodsNo', 'itemCode', 'itemName', 'materialName', 'modelName', 'salePrice', 'orderDiscount', 'exchangeOrderId', 'distributorCompanyName', 'salesName', 'itemCount', 'deliverCount', 'unDeliverCount','previewImage','labelUrl','actualPrice']
            } else {
              tHeader = ['商品编号', '存货编码', '存货名称', '材质', '型号', '价格', '价格折扣', '订购数量', '已发数量', '未发数量','图片','标签','小计']
              filterVal = ['goodsNo', 'itemCode', 'itemName', 'materialName', 'modelName', 'salePrice', 'orderDiscount', 'itemCount', 'deliverCount', 'unDeliverCount','previewImage','labelUrl','actualPrice']
            }
          } else  {
            // 标品
            tHeader = ['商品编号', '存货编码', '存货名称', '价格', '价格折扣', '订购数量（在库）', '订购数量（VMI）', '已发数量', '未发数量', '小计']
            filterVal = ['goodsNo', 'itemCode', 'itemName', 'salePrice', 'orderDiscount', 'itemCount', 'itemVmiCount', 'deliverCount', 'unDeliverCount', 'actualPrice']
          } 
          const data = formatJson(filterVal, goodsData)
          excel.export_json_to_excel({
            header: tHeader,
            data,
            filename: '下单商品清单'
          })
        })
    },
    /**
     * ERP操作日志
     */

    handlePreviewStock(row) {
      // 订单操作日志查看详情
      this.logsShow = true
      this.operateData = row.operateDes + '：' +row.operateData
      // this.$message.info(row.operateData)

      // 查看详情，暂未对接
      
      // this.$http.orderLogList(this, {  
      //     orderId: this.$route.query.orderId,
      //     startTime: row.createTime,
      //   })
      //   .then((res) => {
      //     if (res.success) {
      //       this.modifyLogs = res.data;
      //       this.logsShow = false;
      //     }
      //   });
    },

    orderLog() {
      // 订单操作日志
      this.$http.orderLogList(this, {  
        page:1,
        size:100,  
        orderId: this.$route.query.orderId,
      }).then((res) => {
        if (res.success) {
          this.logs = res.data.list;
          this.logs.forEach(log => {
            if (log.operateType === '1-ERP订单变更') {
              log.type = 13
              log.operateType = log.operateType.replace('1-', '')
            }
          })
          this.logsShow = false;
        }
      });
    },

    closeLogsShow() {
      // 价格等级-品牌价格等级(关闭弹窗)
      this.logsShow = false;
    },

    /**
     * 查看采购进度
     */

    handlePurchase() {
      // ..查看采购进度
      this.purchaseShow = true;
      this.loading2 = true;
      setTimeout(() => {
        this.loading2 = false;
      }, 1000);
    },

    closePurchaseShow() {
      // 关闭dialog的X
      this.purchaseShow = false;
    },

    closeimageShow() {
      this.imageShow = false;
    },
    /**
     * 收款单操作
     */

    handleReceopt() {
      // 查看收款单
      // if (this.chosenData.vouchers) {
        // 单条收款数据
        this.$router.push({
          name: "receiptDetail",
          query: {
            id: this.chosenData.vouchers,
            orderId: this.$route.query.orderId,
            voucherG: true
          },
        });
      // } 
    },

    handleVouchers(row) {
      // ..多条收款单查看
      this.$router.push({
        name: "receiptDetail",
        query: {
          id: row.id,
          orderId: this.$route.query.orderId,
          voucherG: true,
        },
      });
      this.receoptShow = false;
    },

    handleRefundApplies() {
      let userInfo = this.$store.getters.userinfo
      let orderId = this.$route.query.orderId
      let refundArr = []
      this.$http.refundApplyById(this, {page:1, size:100, businessId: orderId, userId: userInfo.id}).then(res => {
        if (res.success) {
          let arr1 = [], arr2 = []
          refundArr = res.data.list
          if (res.data.refundCustomerApplyDTOS.length > 0) {
            // 用户申请
            res.data.refundCustomerApplyDTOS.forEach(item => {
              item.applyType = 2
            })
            arr1 = res.data.refundCustomerApplyDTOS
          }
          if (res.data.refundDistributorApplyDTOS.length > 0) {
            // 分销商申请
            res.data.refundDistributorApplyDTOS.forEach(item => {
              item.applyType = 1
            })
            arr2 = res.data.refundDistributorApplyDTOS
          }
           refundArr = arr1.concat(arr2)
          if (refundArr.length == 1) {
            let id = refundArr[0].id
            this.$router.push({
              name: "refundApply",
              params: { id: id, applyType:  refundArr[0].applyType },
            });
          } else if (refundArr.length > 1) {
            this.refundApplies = refundArr
            this.refundApplyShow = true;
          }
        }
      })
    },

    handleRefundApply(row) {
      // 退款申请单
      this.$router.push({ name: "refundApply", params: { id: row.id, applyType: row.applyType } });
      this.refundApplyShow = false;
    },

    closeReceopt() {
      // ..收款单dialog关闭
      this.receoptShow = false;
    },

    closeRefundApply() {
      this.refundApplyShow = false;
    },

    /**
     * 发货单操作
     */

    handleInvoice(row) {
      // 查看发货单
      this.$http.orderDeliverBillList(this, {page:1, size:100, orderNo: row.orderNo}).then(res => { 
        if (res.success) {
          let total = res.data.total
          if (total === 1) {
            let deliveryId = res.data.list[0].id
            this.$router.push({
              name: "deliverGoodsDetail",
              query: {
                id: deliveryId,
                orderId: this.$route.query.orderId,
                deliverG: true,
              },
            });
          } else {
            this.orderDeliveryList = res.data.list
            this.InvoiceShow = true;
          }
        }
      })
    },
    handlePreview(row) {
      // 多条发货单的查看操作
      this.$router.push({
        name: "deliverGoodsDetail",
        query: {
          // id: row.deliveryId,
          id: row.id,
          orderId: this.$route.query.orderId,
          deliverG: true,
        },
      });
      this.InvoiceShow = false;
    },

    closeDialog() {
      // 发货单的dialog关闭
      this.InvoiceShow = false;
    },

    /**
     * 物流操作
     */

    handleLogisticsNo(row) {
      // 多条发货单物流查看
      this.innerVisible = true;
      this.loading = true;
      this.$http.orderLogistics(this, { 
          id: row.deliveryId,
        })
        .then((res) => {
          if (res.success) {
             this.logistics = res.data;
          }
          res.success ? (this.loading = false) : (this.loading = false);
        });
    },

    /**
     * 右上角返回操作
     */

    cancen() {
      // ..返回导入订单详情
      this.$router.go(-1);
    },

    orderListCancen() {
      // ..返回订单列表
      this.$router.push({ name: "orderList" });
    },

    orderDistrListCancen() {
      // ..返回分销订单列表
      this.$router.push({ name: "orderDistrList" });
    },
    orderCustListCancen() {
      // ..返回柔性定制订单列表
      this.$router.push({ name: "customerDiyOrderList" });
    },
    orderCustSyncListCancen() {
      // ..返回柔性同步订单列表
      this.$router.push({ name: "customerSyncOrderList" });
    },
    failOrderCancen (type) {
      if (type === 1) {
        // ..返回同步ERP失败订单
        this.$router.push({ name: "syncERPFailList" });
      } else if (type === 2) {
        // ..返回同步工厂失败订单
        this.$router.push({ name: "syncFactoryFailList" });
      } else if (type ===3) {
        // ..返回长时间未发货订单
        this.$router.push({ name: "syncUndeliveredFailList" });
      }
    },
    stockListCancen() {
      // ..返回商品库存列表
      this.$router.push({ name: "stockList" });
    },
    vmiListCancen() {
      // ..返回Vmi订单列表
      this.$router.push({ name: "orderVmi" });
    },
    refundApplyListCancen() {
      // 返回退款申请单列表
      this.$router.push({ name: "refundApply" });
    },

    /**
     * 页面中业务操作
     */

    syncOrder() {
      // 确认操作
      this.$confirm("确定将此订单同步到ERP，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          this.synLoading = true;
          this.$http.syncOrderToERP(this, { id: this.chosenData.orderInfo.id })  
            .then((res) => {
              this.synLoading = false;
              if (res.success) {
                this.$message({
                  message: "同步到ERP成功",
                  type: "success",
                  duration: 3 * 1000,
                });
                this.requestData();
              } else {
                this.requestData();
              }
            });
        })
        .catch((_) => {
          this.$message({
            type: "info",
            message: "已取消同步",
          });
        });
    },
    syncYCOrder() {
      this.$confirm("确定将此订单同步到工厂，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          this.synCYLoading = true;
          // 定制同步工厂
          this.$http.syncOrderToFactory(this, {id: this.chosenData.orderInfo.id})  
            .then((res) => {
              this.synCYLoading = false;
              if (res.success) {
                this.$message({
                  message: "同步到工厂成功",
                  type: "success",
                  duration: 3 * 1000,
                });
                this.requestData();
              } else {
                this.requestData();
              }
            });
        })
        .catch((_) => {
          this.synCYLoading = false;
          this.$message({
            type: "info",
            message: "已取消同步",
          });
        });
    },

    operateInvalid() {
      // 作废订单操作
      this.$router.push({
        name: "cancellation",
        query: {
          id: this.chosenData.id,
        },
      });
    },

    operateCancel() {
      // 取消付款操作
      this.$router.push({
        name: "invalid",
        query: {
          id: this.$route.query.orderId,
          orderStatus: this.checkPermission, // ..订单状态 1、7.待确认，2.已确认 3. 已完成 4. 退换货 5. 无效
          payStatus: this.checkPayStatus, // ..付款状态 case 1: 未付款 2: 部分付款 3: 已付款
          payWay: this.payWay, // ..支付方式 1: 支付宝 2: 微信 3: 区间结算 4: 银行转账 5: 余额支付
          splitFlag: this.chosenData.splitFlag,
        },
      });
    },

    operateClose() {
      // 提前关闭订单
    },

    /**
     * 类型转换
     */

    invoiceTypeFormatter(rawData) {
      // 其他类型 > 发票类型的switch语句
      this.invoiceTypeFormatter = invoiceTypeFormatter;
      return this.invoiceTypeFormatter(rawData);
    },
    invoiceTitleFormatter(rawData) {
      // 其他类型 > 发票抬头的switch语句
      this.invoiceTitleFormatter = invoiceTitleFormatter;
      return this.invoiceTitleFormatter(rawData);
    },
    booleanFormatter(rawData) {
      // 其他类型 > 是否开发票的switch语句
      this.booleanFormatter = booleanFormatter;
      return this.booleanFormatter(rawData);
    },
    timeFormatter(rawData) {
      // 时间格式化
      this.timeFormatter = parseTime;
      return this.timeFormatter(rawData);
    },
    orderStatusFormatter(stateCode) {
      // 基本信息  > 订单状态的switch语句
      this.orderStatusFormatter = orderStatusFormatter;
      return this.orderStatusFormatter(stateCode);
    },
    orderSourceFormatter(stateCode) {
      // 基本信息  > 订单来源的switch语句
      this.orderSourceFormatter = orderSourceFormatter;
      return this.orderSourceFormatter(stateCode);
    },
    orderGoodsTypeFormatter(stateCode) {
      // 订单类型
      this.orderGoodsTypeFormatter = orderGoodsTypeFormatter;
      return this.orderGoodsTypeFormatter(stateCode);
    },

    stockTypeFormatter(stateCode) {
      // 库存类型
      return stockTypeFormatter(stateCode);
    },
    orderPaymentFormatter(stateCode) {
      // 基本信息  > 支付方式的switch语句
      this.orderPaymentFormatter = orderPaymentFormatter;
      this.orderPaymentFormatter(stateCode);
    },

    formatStatus(row, col, val) {
      // 日志状态
      switch (val) {
        case 1:
          return "确认";
          // eslint-disable-next-line no-unreachable
          break;
        case 2:
          return "付款";
          // eslint-disable-next-line no-unreachable
          break;
        case 3:
          return "设为未付款";
        case 4:
          return "生成发货单";
        case 5:
          return "取消发货单";
        case 6:
          return "完成";
        case 7:
          return "取消";
        case 8:
          return "作废";
        case 9:
          return "同步erp";
        case 10:
          return "erp设置为未确认";
          // eslint-disable-next-line no-unreachable
          break;
        case 11:
          return "工厂取消订单";
          // eslint-disable-next-line no-unreachable
          break;
        case 12:
          return "订单同步工厂";
        case 13:
          return "修改商品信息";
          // eslint-disable-next-line no-unreachable
          break;
        case 14:
          return "关闭";
          break;
        case 15:
          return "修改信息";
          break;
      }
    },
    amendStatus(row) {
      // 修改状态
      switch (row.updateType) {
        case 1:
          return "修改";
        case 2:
          return "新增";
        case 3:
          return "删除";
      }
    },
    timeFormat(row, col, val) {
      // 时间戳转换
      return timeFormat(val);
    },
    /**
     * 打开变更支付币种确认框
     */
    currencyChangeConfirmOpen() {
      this.currencyChangeConfirm = true;
      this.currencyType = this.chosenData.orderDistributor.orderDistributorDataDTO.currencyType;
    },
    /**
     * 关闭变更支付币种确认框
     */
    currencyChangeConfirmClose() {
      this.currencyChangeConfirm = false;
    },
    /**
     * 变更支付币种确认框 - 确认变更
     */
    currencyChangeSure() {
      this.currencyChangeLoading = true;
      const params = {
        id: this.$route.query.orderId,
        currencyType: this.currencyType,
        rateType: this.exchangeRateType,
      };
      this.$api
        .get(this, "admin/u/p/order/changeCurrency", params)
        .then((res) => {
          if (res.code === 0) {
            this.$message({
              message: "币种变更成功",
              type: "success",
              duration: 3 * 1000,
            });
            this.currencyChangeLoading = false;
            this.currencyChangeConfirmClose();
            this.requestData();
          } else {
            this.currencyChangeLoading = false;
          }
        });
    },
    // 发货
    handleDelivery() {
      if (
        this.chosenData.orderExtendData.orderFactoryNo === undefined ||
        this.chosenData.orderExtendData.orderFactoryNo === ""
      ) {
        this.$message({
          type: "warning",
          message: "暂无工厂订单号，请先同步工厂",
        });
      } else {
        this.curOrderId = this.$route.query.orderId; // 获取当前操作订单号
        this.curProductOrderNo = this.chosenData.orderExtendData.orderFactoryNo; // 获取当前操作工厂订单号
        this.deliveryShow = true;
      }
    },
    // 代金券-使用记录
    handleVoucherUsedData(type) {
      this.$http.getVoucherUsedList(this, {
        orderId: this.$route.query.orderId,
        page: 1,
        size: 10,
      }).then((res) => {
        if (res.success) {
          if (res.data.list && res.data.list.length > 0) {
            let historyData = res.data.list;
            historyData.forEach((item) => {
              this.$set(item, "amountFlag", "+")
              if (item.useAmount < 0) {
                this.$set(item, "amountFlag", "-")
                this.$set(item, "useAmount", -item.useAmount)
              }
            })

            this.historyData = historyData;
            this.historyTotal = res.data.total;

            // 点击使用记录按钮时，弹窗显示
            this.historyDialog = true;
          }
        }
      });
    },

  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-step.is-vertical {
  margin-bottom: 0 !important;
}
.v-auto-out {
  // position: relative;
  // margin-left: 10px;
  // height:100%;
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
  .box-btn-top {
    padding: 10px 10px 0 10px;
    position: relative;
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
    .order_head_body {
      position: absolute;
      left: 47.5%;
      top: -50%;
    }
    .text-headerOne {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .text-headerTwo {
        margin: 0;
        font-size: 14px;
        float: left;
        color: $lakeBlue;
        margin-left: 20px;
      }
    }
  }
  .box-has-border {
    overflow: hidden;
    .cost-line {
      padding-bottom: 10px;
      font-weight: bold;
      padding-top: 10px;
      font-size: 14px;
      color: #333;
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
  }
}
.beAmend {
  text-align: center;
  font-size: 16px;
  font-weight: 700;
  margin-top: 0;
}
.modifierStyle {
  width: 100%;
  text-align: center;
  border-collapse: collapse;
  border: 1px solid #ebeef5;
  .modifierSelect {
    text-align: center;
    text-align-last: center;
  }
  tr {
    th {
      background-color: $table-header-bg;
      color: #333;
      padding: 10px;
    }
    td {
      padding: 10px 0 10px 0;
      border-bottom: 1px solid #ebeef5;
    }
  }
}
/*物流*/
.logistics {
  background-color: #fff;
  .log-right {
    overflow-y: auto;
    .span-left {
      display: inline-block;
      width: 120px;
      margin: 0;
      vertical-align: top;
    }
    .span-right {
      display: inline-block;
      width: 350px;
    }
  }
  .log-right::-webkit-scrollbar {
    /*滚动条整体样式*/
    width: 7px; /*高宽分别对应横竖滚动条的尺寸*/
    height: 9px;
  }
  .log-right::-webkit-scrollbar-thumb {
    /*滚动条里面小方块*/
    border-radius: 5px;
    box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
    background: rgba(0, 0, 0, 0.2);
  }
  .log-right::-webkit-scrollbar-track {
    /*滚动条里面轨道*/
    box-shadow: inset 0 0 5px rgba(0, 0, 0, 0);
    border-radius: 0;
    background: rgba(0, 0, 0, 0);
  }
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
.bg-purple {
  padding: 10px;
  background: #d3dce6;
}
.bg-purple-light {
  background: #e5e9f2;
}
.currency-btn {
  margin-left: 10px;
}
.dialog-row {
  padding: 10px;
  .dialog-row-title {
    display: inline-block;
    width: 100px;
    text-align: right;
  }
}
.is-disabled:hover {
  color: #c0c4cc;
  background-color: #fff;
  border-color: #ebeef5;
}

// 支付凭证
.record-wrap {
  .record-img {
    text-align: center;
    img {
      max-width: 100%;
      max-height: 100%;
    }
  }
}

.el-icon-warning-outline {
  font-size: 14px;
  color: #f00;
  font-weight: bold;
  cursor: pointer;
}
</style>
