<template>
  <div>
    <div class="user rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState"></Header>
      <!--主内容-->
      <div class="main rl-margin-zero">
        <!-- <div class="rl-padding-top-xxxs rl-padding-bottom-xxxs">{{$t('ConfirmOrder.ShippingAddress')}}</div> -->
        <div class="main-one">
          <div class="confirm-address rl-clear rl-bg-white">
            <div class="confirm-title rl-clear">
              <div class="title" v-show="$i18n.locale === 'zh'">确认收货地址</div>
              <!-- 添加收货地址 -->
              <div @click="addNewAddress" class="add-btn cursor-pointer"><i class="el-icon-plus"></i>{{ $t("P.AddShippingAddress") }}</div>
            </div>
            <!-- 收货地址列表 -->
            <p class="addr-tips">{{ "（" + $t("P.OverseasDeliveryTipsTks") + "）" }}</p>
            <ul class="addr-list">
              <template v-for="(address, index) in addressLists">
                <li v-if="(index < 3 && moreAddress === false) || moreAddress === true"
                  class="rl-clear rl-margin-bottom-xxxs"
                  :class="index <3? address.currentChoose === true || addressIndex === index? 'front-addr-list active': 'front-addr-list': address.currentChoose === true || addressIndex === index? 'list-wrap yuan-gou': 'list-wrap'"
                  :key="address.id"
                  @click="getDeliveryId(address,index)">
                  <div :class="index <3? 'front-addr': ''">
                    <!-- 显示前三个的样式 -->
                    <div v-if="index <3" class="cons">
                      <p class="addr-name">{{ address.userName }}<span>{{ address.phone }}</span></p>
                      <p class="addr-info">{{address.provinceName}}{{address.cityName}}{{address.districtName}}{{ address.address }}</p>
                    </div>
                    <!-- 后面的地址 -->
                    <div v-else class="rl-fl cons rl-margin-left-lllg rl-margin-right-default">
                      {{address.provinceName}}{{address.cityName}}{{address.districtName}}{{ address.address }} 
                      ({{$t("UserCenter.RecipientName")}}：{{ address.userName }} {{ $t("UserCenter.Phone") }}：{{address.phone}}
                      {{ $t("UserCenter.ZipCode") }}：{{ address.zipCode }}）
                    </div>
                    <div class="rl-fl default rl-tc cursor-pointer" :class="address.defaultFlag === 1 ? '' : 'no-word'">
                      {{address.defaultFlag === 1? $t("ConfirmOrder.DefaultAddress"): ""}}
                    </div>
                    <!-- 编辑地址 -->
                    <div @click.stop="goEditor(address)" class="rl-fl editor cursor-pointer">
                      <i class="el-icon-edit-outline"></i>{{ $t("ConfirmOrder.Edit") }}
                    </div>
                  </div>
                
                </li>
              </template>
            </ul>
           
            <div v-if="addressLists.length > 3" @click="showMoreAddress" class="more-address cursor-pointer">
              <span>{{ $t("ConfirmOrder.MoreAddress") }}</span>
              <span class="el-icon-arrow-down" :class="moreAddress === true ? 'rotate' : ''"></span>
            </div>
          </div>

          
         <!-- 订单留言 --Y-->
          <div class="write-address" style="margin-top:20px;">
            <div class="write-cons rl-bg-white rl-padding-bottom-lllg">
              <div class="items">
                <p class="item-title">{{ $t("ConfirmOrder.Remarks") }}</p>
                <div class="write-text">
                  <textarea name v-model="remark" :placeholder="$i18n.locale === 'zh' ? '请输入订单留言' : ''"></textarea>
                </div>
              </div>
            </div>
          </div>
          <!--  选择支付的货币-y--->
          <div class="addr-title-wrap rl-padding-top-xxxs" v-show="$root.currency === 'USD'">
            <div class="addr-title rl-padding-top-xxxs rl-padding-bottom-xxxs">Choose Payment Currency</div>
            <div class="rl-bg-white rl-padding-left-default rl-padding-top-xxs rl-padding-bottom-lllg">
              <el-radio-group v-model="radio" @click="changeCurrency">
                <el-radio :label="1">USD</el-radio>
                <el-radio :label="2">CNY</el-radio>
              </el-radio-group>
            </div>
          </div>
           
        </div>

        <!-- 货品列表-Y -->   
        <div class="main-one rl-margin-top-default"   v-if="splitItemList.length > 0||presentList.length > 0"> 
          <div class="cd-title">包裹1-标准商品订单</div> 
          <!-- 选择配送范式-y -->
          <div class="addr-title-wrap post-style rl-padding-top-xxxs" v-if="addressLists.length > 0 ">
            <div class="addr-title rl-padding-top-xxxs rl-padding-bottom-xxxs" >
              {{ $t("ConfirmOrder.ChooseDeliveryMethod") }}
            </div>
            <!-- 是否拆单选择 -->
            <div class="courier" v-if="isTwoWay==1">
              <div class="ps-tipDiv">
                <el-select v-model="onWaySplitFlag" :placeholder="$t('ConfirmOrder.ChooseDeliveryMethod')" value-key="name"  @change="splitFlagFun( $event)">
                    <el-option v-for="item in splitFlagList" :key="item.id" :label="item.name" :value="item.id"></el-option>
                  </el-select>
                  <div class="ps-tip">
                    <span>您所选的商品现有在库库存不足，商品需分批次发出，请选择您想要的发货形式</span>
                    <span>（ 注：拆单后将分为在库/在途两个批次进行发货，运费分别计算）</span>
                  </div>
              </div>
            </div>
            <!-- 配送方式 -->
            <div class="courier">
              <el-select v-model="curVal" :placeholder="$t('ConfirmOrder.ChooseDeliveryMethod')" value-key="name"  @change="getPostId('', $event, $event.id)" >
                <el-option v-for="item in distributeList" :key="item.id" :label="item.name" :value="item"></el-option>
              </el-select>
              <div class="current-post-style" v-for="item in distributeList" :key="item.id" v-show="curId === item.id">
                <div class="post-price">
                  <span>{{ $t("ConfirmOrder.DeliveryCharge") }}：</span>
                  <span v-if="radio === 2">￥{{ item.cost }}</span>
                  <span v-else>${{ (Number(item.cost)*exchange).toFixed(2) }}</span>
                </div>
                <div class="post-info">{{ item.description }}</div>
              </div>
            </div> 
          </div>
          <div class="shop-list rl-padding-top-xxxs"> 
              <!-- 非赠品---Y -->    
              <template v-if="splitItemList.length > 0">
                <div class="rl-padding-top-xxxs rl-padding-bottom-xxxs">
                  {{ $t("ConfirmOrder.OrderingList") }}
                  <span class="blue">（在库/在途数量）</span>
                  
                </div>
                <el-table :data="splitItemList" :header-cell-style="getRowClass" header-row-class-name="header-row" class="activity-el-table rl-tc">
                  <!-- 货品名称 -->
                  <el-table-column :label="$t('ShopCarts.ItemName')">
                    <template slot-scope="scope">
                      <div class="rl-text-xxs" v-if="$i18n.locale === 'zh' || !scope.row.itemNameEn == true">
                        {{ scope.row.itemName }}
                      </div>
                      <div class="rl-text-xxs" v-show="$i18n.locale === 'en'">
                        {{ scope.row.itemNameEn }}
                      </div>
                    </template>
                  </el-table-column>
                  <!-- 货品编码 -->
                  <el-table-column :label="$t('ShopCarts.ItemNo')" prop="itemCode"></el-table-column>
                  <!-- 条形码 -->
                  <el-table-column :label="$t('ShopCarts.BarCode')" prop="barCode"></el-table-column>
                  <!-- 规格 -->
                  <el-table-column :label="$t('ShopCarts.Spe')">
                    <template slot-scope="scope">
                      <div class="rl-text-xxs" v-if=" $i18n.locale === 'zh' ||!scope.row.specsName == true">
                        {{ scope.row.specsName }}
                      </div>
                      <div class="rl-text-xxs" v-show="$i18n.locale === 'en'">
                        {{ scope.row.specsNameEn }}
                      </div>
                    </template>
                  </el-table-column>
                  <!-- 材质 -->
                  <el-table-column v-if="showItemList.length > 0 && showItemList[0].goodsType ===2" :label="$t('ShopCarts.Material')">
                    <template slot-scope="scope">
                      <div class="rl-text-xxs" v-if="$i18n.locale === 'zh' || !scope.row.materialName">
                        {{ scope.row.materialName }}
                      </div>
                      <div class="rl-text-xxs" v-show="$i18n.locale === 'en'">
                        {{ scope.row.materialName }}
                      </div>
                    </template>
                  </el-table-column>
                  <!-- 颜色 -->
                  <el-table-column v-if="showItemList.length > 0 && showItemList[0].goodsType!== 2" :label="$t('ShopCarts.Colors')">
                    <template slot-scope="scope">
                      <div class="rl-text-xxs" v-if="$i18n.locale === 'zh' ||!scope.row.colorNameEn == true">
                        {{ scope.row.colorName }}
                      </div>
                      <div class="rl-text-xxs" v-show="$i18n.locale === 'en'">
                        {{ scope.row.colorNameEn }}
                      </div>
                    </template>
                  </el-table-column>
                  <!-- 会员价 -->
                  <el-table-column :label="$t('ShopCarts.MemPrice')">
                    <template slot-scope="scope">
                      <i v-if="radio === 2" class="asmd">￥{{ scope.row.salePrice | keepTwoNum }}</i>
                      <i v-else class="asmd">${{ Number(scope.row.salePrice)*exchange | keepTwoNum }}</i>
                    </template>
                  </el-table-column>
                  <!-- 优惠价 -->
                  <el-table-column :label="$t('ShopCarts.DiscountPrice')">
                    <template slot-scope="scope">
                      <div v-if="scope.row.inStockUsableCount&&scope.row.inStockUsableCount!=0">
                        <span>
                          <i v-if="radio === 2" class="asmd rl-text-xxs">￥{{ (Number(scope.row.totalPrice)/(scope.row.zaiTuCount+scope.row.zaiKuCount)) | keepTwoNum }}</i>
                          <i v-else class="asmd rl-text-xxs">${{ Number(scope.row.totalPrice/(scope.row.zaiTuCount+scope.row.zaiKuCount))*exchange | keepTwoNum }}</i>
                        </span>
                        
                        
                      </div>
                      <div v-else class="rl-text-xxs">--</div>
                    </template>
                  </el-table-column>
                  <!-- 数量 -->
                  <!-- <el-table-column :label="$t('ShopCarts.Quantity')" prop="num" ></el-table-column> -->
                  <!-- 在库 -->
                  <el-table-column :label="$t('ShopCarts.InventoryQuantity')" prop="zaiKuCount"></el-table-column>
                  <!-- 在途库存 -->
                  <el-table-column :label="$t('ShopCarts.IntransitQuantity')" prop="zaiTuCount"></el-table-column>
                  <!-- 合计 -->
                  <el-table-column :label="$t('ShopCarts.Total')">
                    <template slot-scope="scope">
                      <i v-if="radio === 2" class="asmd">￥{{Number(scope.row.totalPrice)| keepTwoNum}}</i>
                      <i v-else class="asmd">${{Number(scope.row.totalPrice)*exchange| keepTwoNum}}</i>
                    </template>
                  </el-table-column>
                </el-table>
              </template>
              <!-- 赠品-Y -->
              <template v-if="presentList.length > 0">
                  <div class="rl-padding-top-xxxs rl-padding-bottom-xxxs">赠品列表</div>
                  <div class="shop-table rl-bg-white">
                    <el-table :data="presentList" :header-cell-style="getRowClass"
                        header-row-class-name="header-row" class="activity-el-table rl-tc">
                        <el-table-column :label="$t('ShopCarts.ItemName')">
                          <!-- 货品名称 -->
                          <template slot-scope="scope">
                            <div class="rl-text-xxs" v-if="$i18n.locale === 'zh' || !scope.row.itemNameEn == true">
                              {{ scope.row.itemName }}
                            </div>
                            <div class="rl-text-xxs" v-show="$i18n.locale === 'en'">
                              {{ scope.row.itemNameEn }}
                            </div>
                          </template>
                        </el-table-column>
                        <!-- 货品编码 -->
                        <el-table-column :label="$t('ShopCarts.ItemNo')" prop="itemCode"></el-table-column>
                        <!-- 条形码 -->
                        <el-table-column :label="$t('ShopCarts.BarCode')" prop="barCode"></el-table-column>
                        <!-- 规格 -->
                        <el-table-column :label="$t('ShopCarts.Spe')">
                          <template slot-scope="scope">
                            <div class="rl-text-xxs" v-if="$i18n.locale === 'zh' ||!scope.row.specsName == true">
                              {{ scope.row.specsName }}
                            </div>
                            <div class="rl-text-xxs" v-show="$i18n.locale === 'en'">
                              {{ scope.row.specsName }}
                            </div>
                          </template>
                        </el-table-column>
                        <!-- 颜色 -->
                        <el-table-column :label="$t('ShopCarts.Colors')">
                          <template slot-scope="scope">
                            <div class="rl-text-xxs" v-if="$i18n.locale === 'zh' ||!scope.row.colorName == true">
                              {{ scope.row.colorName }}
                            </div>
                            <div class="rl-text-xxs" v-show="$i18n.locale === 'en'">
                              {{ scope.row.colorName }}
                            </div>
                          </template>
                        </el-table-column>
                        <!-- 数量 -->
                        <el-table-column :label="$t('ShopCarts.Quantity')" prop="itemCount"></el-table-column>
                      </el-table>
                  </div>
              </template>
            
          </div>  
        </div>

        <!-- 定制-diyGoodsList -->
        <template v-if="diyGoodsList.length>0">
        <div class="main-one song-shop-list rl-margin-top-default"  v-for="(diyItem,diyIndex) in diyGoodsList" :key="diyIndex" >
          
          <div class="cd-title" v-if="splitItemList.length > 0||presentList.length > 0">包裹{{diyIndex+2}}-个性定制订单</div>
          <div class="cd-title" v-else>包裹{{diyIndex+1}}-个性定制订单</div>
          
           <!-- 选择配送范式-y -->
          <div class="addr-title-wrap post-style rl-padding-top-xxxs" v-if="addressLists.length > 0 ">
            <div class="addr-title rl-padding-top-xxxs rl-padding-bottom-xxxs">
              {{ $t("ConfirmOrder.ChooseDeliveryMethod") }}
            </div>
            <!-- 配送方式 -->
            <div class="courier">
              <el-select v-model="diyItem.curVal" :placeholder="$t('ConfirmOrder.ChooseDeliveryMethod')" value-key="name"  @change="DiyGetPostId(diyItem,$event)">
                <el-option v-for="item in diyItem.distributeList" :key="item.id" :label="item.name" :value="item"></el-option>
              </el-select>
              <div class="current-post-style" v-for="item in diyItem.distributeList" :key="item.id" v-show="diyItem.curId === item.id">
                <div class="post-price">
                  <span>{{ $t("ConfirmOrder.DeliveryCharge") }}：</span>
                  <span  v-if="radio === 2">￥{{ item.cost }}</span>
                  <span  v-else>${{ Number(item.cost)*exchange }}</span>
                </div>
                <div class="post-info">{{ item.description }}</div>
              </div>
            </div> 
          </div>
          <div class="rl-padding-top-xxxs rl-padding-bottom-xxxs">
           {{ $t("ConfirmOrder.OrderingList") }}
          </div>
          <div class="shop-table rl-bg-white">
               <el-table :data="diyItem.children" :header-cell-style="getRowClass" header-row-class-name="header-row" class="activity-el-table rl-tc">
                  <!-- 货品名称 -->
                  <el-table-column :label="$t('ShopCarts.ItemName')">
                    <template slot-scope="scope">
                      <div class="rl-text-xxs" v-if="$i18n.locale === 'zh' || !scope.row.itemNameEn == true">
                        {{ scope.row.itemName }}
                      </div>
                      <div class="rl-text-xxs" v-show="$i18n.locale === 'en'">
                        {{ scope.row.itemNameEn }}
                      </div>
                    </template>
                  </el-table-column>
                  <!-- 货品编码 -->
                  <el-table-column :label="$t('ShopCarts.ItemNo')" prop="itemCode"></el-table-column>
                  <!-- 条形码 -->
                  <el-table-column :label="$t('ShopCarts.BarCode')" prop="barCode"></el-table-column>
                  <!-- 规格 -->
                  <el-table-column :label="$t('ShopCarts.Spe')">
                    <template slot-scope="scope">
                      <div class="rl-text-xxs" v-if=" $i18n.locale === 'zh' ||!scope.row.specsName == true">
                        {{ scope.row.diy.modelName }}
                      </div>
                      <div class="rl-text-xxs" v-show="$i18n.locale === 'en'">
                        {{ scope.row.diy.modelName }}
                      </div>
                    </template>
                  </el-table-column>
                  <!-- 材质 -->
                  <el-table-column v-if="showItemList.length > 0 && showItemList[0].goodsType ===2" :label="$t('ShopCarts.Material')">
                    <template slot-scope="scope">
                      <div class="rl-text-xxs" v-if="$i18n.locale === 'zh' || !scope.row.diy.materialName">
                        {{ scope.row.diy.materialName }}
                      </div>
                      <div class="rl-text-xxs" v-show="$i18n.locale === 'en'">
                        {{ scope.row.diy.materialName }}
                      </div>
                    </template>
                  </el-table-column>
                  <!-- 颜色 -->
                  <el-table-column v-if="showItemList.length > 0 && showItemList[0].goodsType!== 2" :label="$t('ShopCarts.Colors')">
                    <template slot-scope="scope">
                      <div class="rl-text-xxs" v-if="$i18n.locale === 'zh' ||!scope.row.colorNameEn == true">
                        {{ scope.row.colorName }}
                      </div>
                      <div class="rl-text-xxs" v-show="$i18n.locale === 'en'">
                        {{ scope.row.colorNameEn }}
                      </div>
                    </template>
                  </el-table-column>
                  <!-- 会员价 -->
                  <el-table-column :label="$t('ShopCarts.MemPrice')">
                    <template slot-scope="scope">
                      <i v-if="radio === 2" class="asmd">￥{{ scope.row.salePrice | keepTwoNum }}</i>
                      <i v-else class="asmd">${{ Number(scope.row.salePrice)*exchange | keepTwoNum }}</i>
                    </template>
                  </el-table-column>
                  <!-- 优惠价 -->
                  <el-table-column :label="$t('ShopCarts.DiscountPrice')">
                    <template slot-scope="scope">
                      <div v-if="scope.row.inStockUsableCount&&scope.row.inStockUsableCount!=0">
                        <span>
                          <i v-if="radio === 2" class="asmd rl-text-xxs">￥{{ (scope.row.totalPrice/(scope.row.zaiTuCount+scope.row.zaiKuCount)) | keepTwoNum }}</i>
                          <i v-else class="asmd rl-text-xxs">${{ Number(scope.row.totalPrice/(scope.row.zaiTuCount+scope.row.zaiKuCount))*exchange | keepTwoNum }}</i>
                        </span>
                        
                      </div>
                      <div v-else class="rl-text-xxs">--</div>
                    </template>
                  </el-table-column>
                  <!-- 数量 -->
                  <el-table-column :label="$t('ShopCarts.Quantity')" prop="itemCount" ></el-table-column>
                  <!-- 合计 -->
                  <el-table-column :label="$t('ShopCarts.Total')">
                    <template slot-scope="scope">
                      <i v-if="radio === 2" class="asmd">￥{{Number(scope.row.totalPrice)| keepTwoNum}}</i>
                      <i v-else class="asmd">${{Number(scope.row.totalPrice)*exchange| keepTwoNum}}</i>
                    </template>
                  </el-table-column>
                </el-table>
          </div>
        </div>
        </template>
        <!-- 发票-Y -->
        <div class="main-one rl-margin-top-default">
          <div class="invoice-info" v-show="$i18n.locale === 'zh'">
            <div class="title rl-padding-top-xxxs rl-padding-bottom-xxxs">
              发票信息(
              <span class="state rl-text-xxss">如果需要发票请勾选开具发票，也可以在会员中心-发票管理里，合并开票。（首次开发票的客户，请提供相关证件和合同原件给我司，以避免造成无法开票问题)</span>)
            </div>
            <div class="contents rl-bg-white rl-padding-bottom-lllg">
              <div class="issue-bill rl-clear rl-padding-top-double rl-padding-left-xxxs">
                <div class="rl-fl">开具发票</div>
                <div class="rl-fl rl-margin-left-xxxs">
                  <input type="checkbox" id="ios-checkbox" name="emulate-ios-button" class="raw-checkbox"  style="display: none"/>
                  <label for="ios-checkbox" class="emulate-ios-button" @click="getInvoice"></label>
                </div>
              </div>
              <div v-show="isInvoice === 1" class="send-province rl-clear rl-padding-left-xxxs rl-margin-top-default rl-margin-bottom-xxxs" >
                <div class="rl-fl send-title">寄往省份</div>
                <div class="rl-clear checkedInput">
                  <div class="rl-fl check-item"  v-for="item in courierList" :key="item.id">
                    <span  @click="getCourier(item.index)" name="showpri" checked="checked" class="chenked" :class="{ haschecked: item.index === courier }"></span>
                    <span class="rl-margin-right-mid">{{ item.text }}</span>
                  </div>
                </div>
              </div>
              <div v-show="isInvoice === 1" class="send-province rl-clear rl-padding-left-xxxs rl-margin-bottom-xxxs">
                <div class="rl-fl send-title">发票类型</div>
                <div class="rl-clear checkedInput">
                  <div class="rl-fl check-item" v-for="item in invoiceTypeList" :key="item.id">
                    <span @click="getInvoiceType(item.index)" name="showpri" checked="checked" class="chenked" :class="{ haschecked: item.index === invoiceType }"></span>
                    <span class="rl-margin-right-mid">{{ item.text }}</span>
                  </div>
                </div>
              </div>
              <div class="look-up rl-padding-left-xxxs rl-margin-bottom-xxxs">
                <div class="look-head rl-clear rl-padding-bottom-xxxs" v-show="isInvoice === 1 && invoiceType === 1">
                  <div class="rl-fl send-title">发票抬头</div>
                  <div class="rl-clear checkedInput">
                    <div  class="rl-fl check-item" v-for="item in invoiceTitleList" :key="item.id">
                      <span
                        @click="getInvoiceTitle(item.index)"
                        name="showpri"
                        checked="checked"
                        class="chenked"
                        :class="{ haschecked: item.index === invoiceTitle }"></span>
                      <span class="rl-margin-right-mid">{{ item.text }}</span>
                    </div>
                  </div>
                </div>
                <!-- invoiceTitle: 发票抬头 -->
                <div class="look-foot" v-if=" invoiceTitle === 1 && invoiceType === 1 && isInvoice === 1">
                  <div class="items rl-clear rl-padding-bottom-xxxs">
                    <span class="rl-fl send-title">姓名</span>
                    <div class="search-input rl-fl">
                      <input type="text" placeholder="单行输入" v-model="userInfos.name" readonly="readonly"/>
                    </div>
                  </div>
                </div>
                <div class="look-foot" v-if="invoiceTitle === 2 && invoiceType === 1 && isInvoice === 1">
                  <div class="items rl-clear rl-margin-bottom-xxxs">
                    <span class="rl-fl send-title">单位名称</span>
                    <div class="search-input rl-fl">
                      <input type="text" placeholder="单行输入" v-model="userInfos.financial.invoiceTitleName" readonly="readonly"/>
                    </div>
                  </div>
                  <div class="items rl-clear rl-margin-bottom-xxxs">
                    <span class="rl-fl send-title">纳税人识别号</span>
                    <div class="search-input rl-fl">
                      <input type="text" placeholder="单行输入" v-model="userInfos.financial.taxpayerNumber" readonly="readonly"/>
                    </div>
                  </div>
                </div>
                <!-- invoiceType：增值税 -->
                <div class="look-foot" v-if="invoiceType === 2">
                  <div class="items rl-clear rl-margin-bottom-xxxs">
                    <span class="rl-fl send-title">单位名称</span>
                    <div class="search-input rl-fl">
                      <input type="text" placeholder="单行输入" v-model="userInfos.financial.invoiceTitleName" readonly="readonly"/>
                    </div>
                  </div>
                  <div class="items rl-clear rl-margin-bottom-xxxs">
                    <span class="rl-fl send-title">纳税人识别号</span>
                    <div class="search-input rl-fl">
                      <input type="text" placeholder="单行输入" v-model="userInfos.financial.taxpayerNumber" readonly="readonly"/>
                    </div>
                  </div>
                  <div class="items rl-clear rl-margin-bottom-xxxs">
                    <span class="rl-fl send-title">开票地址</span>
                    <div class="search-input rl-fl">
                      <input type="text" placeholder="单行输入" v-model="userInfos.financial.registeredAddress" readonly="readonly"/>
                    </div>
                  </div>
                  <div class="items rl-clear rl-margin-bottom-xxxs">
                    <span class="rl-fl send-title">电话</span>
                    <div class="search-input rl-fl">
                      <input type="text" placeholder="单行输入" v-model="userInfos.financial.registeredPhone" readonly="readonly"/>
                    </div>
                  </div>
                  <div class="items rl-clear rl-margin-bottom-xxxs">
                    <span class="rl-fl send-title">开户银行</span>
                    <div class="search-input rl-fl">
                      <input type="text" placeholder="单行输入" v-model="userInfos.financial.registeredBankDepositName" readonly="readonly"/>
                    </div>
                  </div>
                  <div class="items rl-clear rl-margin-bottom-xxxs">
                    <span class="rl-fl send-title">银行账户</span>
                    <div class="search-input rl-fl">
                      <input type="text" placeholder="单行输入" v-model="userInfos.financial.registeredBankAccount" readonly="readonly"/>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 选择支付方式--y -->
        <div class="main-one rl-margin-top-default">
          <div class="addr-title-wrap pay-style rl-padding-top-xxxs">
            <div class="addr-title rl-padding-top-xxxs rl-padding-bottom-xxxs">
              {{ $t("ConfirmOrder.ChoosePaymentMethod") }}
            </div>
            <div class="rl-bg-white pay-money rl-padding-top-default rl-padding-bottom-double">             
              <ul>
                <!-- 区间结算 -->
                <!-- v-if="userTradeType === 2 &&($i18n.locale === 'zh' || Number(radio) === 2)" -->
                <li class="space-pay" :class="{ current: this.payWay === 3 }"  @click="getPayWay(3)"
                  v-if="userTradeType === 2">
                  {{ $t("P.RangeCheckout") }}
                </li>
                <!-- 银行转账 -->
                <li class="bank-pay" :class="{ current: this.payWay === 4 }" @click="getPayWay(4)">
                  {{ $t("P.BankTransfer") }}
                </li>
                <!-- 网银支付 -->
                <li class="net-pay" :class="{ current: this.payWay === 6 }" @click="getPayWay(6)" v-if="$i18n.locale === 'zh'">
                  网银支付
                </li>
                <!-- 余额支付 -->
                <li class="balance-pay"  :class="{ current: this.payWay === 5 }" @click="getPayWay(5)" v-if="$i18n.locale === 'zh' && userTradeType === 1 && Number(summation.orderPrice - rebateVoucherAmount).toFixed(2) <= balancePrice">
                 <span> 余额支付</span>
                 <span>余额：{{balancePrice}}</span>
                </li>
                <li class=" balance-payN" v-if="$i18n.locale === 'zh' && userTradeType === 1 && Number(summation.orderPrice - rebateVoucherAmount).toFixed(2) > balancePrice">
                  <img src="../assets/images/order/balance-pay.png">
                  <div>
                    <span> 余额支付</span>
                    <span>余额：{{balancePrice}}</span>
                  </div>
                </li>
                <!-- 支付宝支付 -->
                <li class="zhi-pay" :class="{ current: this.payWay === 1 }" @click="getPayWay(1)" v-if="$i18n.locale === 'zh' || Number(radio) === 2">
                  {{ $t("P.Alipay") }}
                </li>
                <!-- 微信支付 -->
                <li class="wechat-pay" :class="{ current: this.payWay === 2 }" @click="getPayWay(2)" v-if="$i18n.locale === 'zh' || Number(radio) === 2">
                  {{ $t("P.WechatPay") }}
                </li>
              </ul>
            </div>
          </div>
        </div>

        <!-- 代金券 -->
        <div class="main-one rl-margin-top-default" v-if="voucherList.length > 0">
          <div class="addr-title-wrap pay-style rl-padding-top-xxxs">
            <div class="addr-title rl-padding-top-xxxs rl-padding-bottom-xxxs">
              {{ $t("UserCenter.Voucher") }}
            </div>
            <div class="rl-bg-white pay-money voucher-list rl-padding-top-default rl-padding-bottom-double">             
              <ul>
                <li class="voucher-list-item" :class="{current: item.choosed}" v-for="item in voucherList" :key="item.id" @click="chooseVoucher(item)">
                  <p class="name">{{item.name}}</p>
                  <p class="no">{{ $t("UserCenter.VoucherNo") }}: {{item.voucherNo}}</p>
                  <p class="time">{{ $t("UserCenter.VoucherValid") }}: {{item.startTime | formatDate }} 至 {{item.endTime | formatDate}}</p>
                  <p class="amount">
                    {{
                      $i18n.locale === "en" && $root.currency === "USD"
                        ? "$"
                        : "￥"
                    }}<span>{{item.balance}}</span>
                  </p>
                </li>
              </ul>
            </div>
          </div>
        </div>

        <!----计算价格--z---->
        <div class="totals rl-tr rl-bg-white rl-margin-top-default">
          <!----商品总价---->     
          <div class="item">
            {{ $t("P.AggregatePriceA") }}：
            <span class="red">
              <i v-show="radio === 1">{{ Number(summation.totalPrice)*exchange | keepTwoNum }}</i>
              <i v-show="radio === 2">{{ summation.totalPrice | keepTwoNum }}{{$i18n.locale === 'zh' ? '元' : ''}}</i>
            </span>
          </div>
          <!----商品优惠---->
          <div class="item" v-if="this.summation.discountPrice !== 0 ">
            {{ $t("P.AggregatePriceB") }}：
            <span class="red">
              <i v-show="radio === 1">${{ Number(this.summation.discountPrice)*exchange | keepTwoNum }}</i>
              <i v-show="radio === 2">{{ this.summation.discountPrice | keepTwoNum }}{{$i18n.locale === 'zh' ? '元' : ''}}</i>
            </span>
          </div>
          <!-- 商品分类折扣 -->
          <!-- <div class="item" v-if="this.summation.gradeDiscountMoney !== 0">
            {{ $t("P.AggregatePriceC") }}：
            <span class="red">
              <i v-show="radio === 1">${{ this.summation.foreignGradeDiscountMoney | keepTwoNum }}</i>
              <i v-show="radio === 2">{{ this.summation.gradeDiscountMoney | keepTwoNum }}元</i>
            </span>
          </div> -->
          <!----订单折扣金额---->
          <!-- <div class="item" v-if="this.summation.orderDiscountMoney !== 0">
            {{ $t("P.AggregatePriceD") }}：
            <span class="red">
              <i v-show="radio === 1">${{ this.summation.foreignOrderDiscountMoney | keepTwoNum }}</i>
              <i v-show="radio === 2">{{ this.summation.orderDiscountMoney | keepTwoNum }}元</i>
            </span>
          </div> -->
          <!----提货增长返利---->
          <!-- <div class="item" v-if="this.summation.pickUpRateMoney !== 0">
            {{ $t("P.AggregatePriceE") }}：
            <span class="red">
              <i v-show="radio === 1">${{ this.summation.foreignPickUpRateMoney | keepTwoNum }}</i>
              <i v-show="radio === 2" >{{ this.summation.pickUpRateMoney | keepTwoNum }}元</i>
            </span>
          </div> -->
          
          <!----配送费用-y---->
          <div class="item">
            {{ $t("P.AggregatePriceF") }}：
            <span class="red">
              <i v-show="radio === 1">${{Number(summation.shippingPrice)*exchange | keepTwoNum }}</i>
              <i v-show="radio === 2">{{ summation.shippingPrice | keepTwoNum }}{{$i18n.locale === 'zh' ? '元' : ''}}</i>
            </span>
          </div>

          <!-- 代金券 -->
          <div class="item">
            {{ $t("UserCenter.Voucher") }}：
            <span class="red">
              <i v-show="radio === 1">${{ Number(rebateVoucherAmount)*exchange | keepTwoNum }}</i>
              <i v-show="radio === 2">{{ rebateVoucherAmount | keepTwoNum }}{{$i18n.locale === 'zh' ? '元' : ''}}</i>
            </span>
          </div>
         
          
          <!----订单总金额-y---->
          <div class="item item-total">
            {{ $t("P.AggregatePriceG") }}：
            <span class="red" v-if="Number(summation.orderPrice)  > 0">
              <span v-show="radio === 1">
                 $<i class="item-num">{{Number(summation.orderPrice - rebateVoucherAmount)*exchange | keepTwoNum}}</i>
              </span>
              <span v-show="radio === 2">
                <i class="item-num">{{Number(summation.orderPrice - rebateVoucherAmount) | keepTwoNum}}</i>{{$i18n.locale === 'zh' ? '元' : ''}}
              </span>
            </span>
            <span class="red" v-else>
              <i v-show="radio === 1">$</i>
              <i class="item-num">0</i>
              <i v-show="radio === 2">{{$i18n.locale === 'zh' ? '元' : ''}}</i>
            </span>
          </div>
        </div>
        <!-- 下单 -->
        <div class="comfirm-order rl-clear" v-if="onceAgain === true">
          <div @click="confirmOrder" class="rl-fr rl-tc rl-text-white cursor-pointer">
            {{ $t("ConfirmOrder.SubmitOrder") }}
          </div>
        </div>
        <div class="comfirm-order rl-clear" v-else>
          <div class="rl-fr rl-tc rl-text-white cursor-pointer">
            {{ $t("ConfirmOrder.SubmitOrder") }}
          </div>
        </div>
      </div>
      <!--公共底部-->
      <Footer></Footer>
    </div>

    <!--新增or编辑地址弹框--Y-->
    <div class="cover" v-if="showcov"></div>
    <div class="pro-cover cover-box rl-padding-bottom-lllg rl-padding-top-default rl-relative" style="text-align: center" v-if="showcov">
      <div class="rl-margin-bottom-default rl-bdb-gray-sm rl-padding-left-default rl-padding-bottom-xxxs" >
        {{addressType==1? $t("P.AddShippingAddress"):$t('P.EditShippingAddress') }}
      </div>
      <span @click="shutLog" class="shut cursor-pointer"></span>
      <div class="address-info rl-bg-white rl-padding-left-default">
        <div class="items rl-clear rl-margin-bottom-xxxs">
          <span class="rl-fl rl-text-xxs rl-tr rl-margin-right-double">
            <em class="rl-text-blue-xs">*</em>
            {{$i18n.locale === "zh"? $t("UserCenter.Location"): $t("Register.Country")}}
          </span>
          <!-- 国家 -->
          <div class="item rl-fl rl-relative rl-margin-right-mid">
            <el-select  v-model="countryId" @change="getCountryId" :placeholder="$t('P.pleaseSelect')" size="mini" style="width: 100px" >
              <el-option v-for="item in countryList" :key="item.id"
                :label="$i18n.locale === 'zh' || !item.regionNameEn == true? item.regionName: item.regionNameEn" :value="item.id"></el-option>
            </el-select>
          </div>
          <!-- 省 -->
          <div class="item rl-fl rl-relative rl-margin-right-mid" v-show="showProvince === true"> 
            <el-select  v-model="provinceId" @change="getProvinceId" :placeholder="$t('P.pleaseSelect')" size="mini" style="width: 100px" >
              <el-option v-for="item in provinceList" :key="item.id"
                :label="$i18n.locale === 'zh' || !item.regionNameEn == true? item.regionName: item.regionNameEn" :value="item.id"></el-option>
            </el-select>
          </div>
          <!-- 市 -->
          <div class="item rl-fl rl-relative rl-margin-right-mid" v-show="showCity === true">
            <el-select v-model="cityId" @change="getCityId" :placeholder="$t('P.pleaseSelect')" size="mini" style="width: 100px">
              <el-option v-for="item in cityList" :key="item.id"
                :label="$i18n.locale === 'zh' || !item.regionNameEn == true? item.regionName: item.regionNameEn" :value="item.id"></el-option>
            </el-select>
          </div>
          <!-- 区 -->
          <div class="item rl-fl rl-relative" v-show="showArea === true" style="margin-top:15px;">
            <el-select v-model="townId" :placeholder="$t('P.pleaseSelect')" size="mini" style="width: 100px">
              <el-option v-for="item in areaList" :key="item.id"
                :label="$i18n.locale === 'zh' || !item.regionNameEn == true? item.regionName: item.regionNameEn" :value="item.id"></el-option>
            </el-select> 
          </div>
        </div>
        <!-- 地址 -->
        <div class="items rl-clear rl-margin-bottom-xxxs">
          <span class="rl-fl rl-text-xxs rl-tr rl-margin-right-double">
            <em class="rl-text-blue-xs">*</em>
            {{ $t("UserCenter.Address") }}
          </span>
          <div class="search-input rl-fl">
            <el-input size="mini" type="text" v-model="address" :placeholder="$t('P.PleaseEnter')"/>
          </div>
        </div>
        <!-- 邮编 -->
        <div class="items rl-clear rl-margin-bottom-xxxs">
          <span class="rl-fl rl-text-xxs rl-tr rl-margin-right-double">{{ $t("UserCenter.ZipCode")}}</span>
          <div class="search-input rl-fl">
            <el-input size="mini" type="text" v-model="zipCode" :placeholder="$t('P.PleaseEnter')" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
          </div>
        </div>
        <!-- 收货人 -->
        <div class="items rl-clear rl-margin-bottom-xxxs">
          <span class="rl-fl rl-text-xxs rl-tr rl-margin-right-double">
            <em class="rl-text-blue-xs">*</em>{{ $t("UserCenter.RecipientName") }}
          </span>
          <div class="search-input rl-fl">
            <el-input size="mini" type="text" v-model="userName" :placeholder="$t('P.PleaseEnter')"/>
          </div>
        </div>
        <!-- 电话 -->
        <!-- <div class="items rl-clear rl-margin-bottom-xxxs">
          <span class="rl-fl rl-text-xxs rl-tr rl-margin-right-double">{{
            $t("UserCenter.Tel")}}</span>
          <div class="search-input rl-fl">
            <el-input size="mini" type="text" v-model="phone" :placeholder="$t('P.PleaseEnter')" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
          </div>
        </div> -->
        <!-- 手机号 -->
        <div class="items rl-clear rl-margin-bottom-xxxs">
          <span class="rl-fl rl-text-xxs rl-tr rl-margin-right-double">
            <em class="rl-text-blue-xs">*</em>
            {{ $t("UserCenter.Phone") }}
          </span>
          <div class="search-input rl-fl">
            <el-input size="mini" type="text"  v-model="mobile" :placeholder="$t('P.PleaseEnter')" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
          </div>
        </div>

        <div class="items rl-clear rl-margin-bottom-xxxs" style="margin-top: 15px; margin-bottom: 15px">
          <div class="rl-fl">
            <span style="width: 340px;color: #606266;font-size: 12px;word-wrap: break-word;
                text-align: left;margin-left: 160px;">{{ $t("P.OverseasDeliveryTips") }}</span>
          </div>
        </div>
      </div>
      <!-- 保存 -->
      <el-button class="mini-search-btn" style="width: 276px; margin-left: 80px" @click="addAddress">{{ $t("P.Save") }}</el-button>
    </div>



    
    <!--加载动画-->
    <loading v-if="this.showLoading === true"></loading>
    
    <!--定制商品弹框-->
    <proCustomizeDialog
      :showOpen="showOpenPro"
      :select="true"
      @close="agreeGoon(3)"
    ></proCustomizeDialog>
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';
import Left from '@/components/Left.vue';

import Vue from 'vue';
import { dateToNum,fomatFloat } from '@/assets/js/common.js';
import { MessageBox } from 'element-ui';
import loading from '@/components/loading.vue';
import GD from '@/assets/js/globalData';
import cutFilmMachineDialog from '@/components/cutFilmMachineDialog';
import proCustomizeDialog from '@/components/proCustomizeDialog';
// liu
import { region, formatDate } from "@/assets/js/common.js";
import{
  addressList,addressApi,logisticsCalculations,paymentMode,
  userDeposit,userInfo,placeOrder,agreementSignStatus,agreementSignList,
  orderResults,payCreateTrade,logisticsCalculationss,deleteShoppingcart,
  voucherList,
} from '@/apiService/api' 

export default {
  name: 'ConsigneeInfor',
  components: {
    Header,
    Footer,
    Left,
    loading,
    cutFilmMachineDialog,
    proCustomizeDialog
  },
  data () {
    return {  
      // liu
      userState: 2,
      currencyType: 'CNY', // 币种
      addressType:1,
      showLoading: false, // 展示加载动画
      // 订单是否拆分
      isTwoWay:this.$route.query.isTwoWay, //是否有在途和在库商品
      onWaySplitFlag:this.$route.query.onWaySplitFlag,
      splitFlag: this.$route.query.onWaySplitFlag, // 是否拆单：1, 拆；0, 不拆
      splitFlagList:[
        {id:1,name:'库存拆分发货'},
        {id:0,name:'到货统一发货'},
      ],
      deliveryId: '', // 收货地址编号
      addressId:'',//编辑时的收货地址
      addressLists: [],  //收货地址 
      distributeList: [],  // 配送方式列表
      curVal: '',// 选择的配送方式名称
      curId: '', // 选择的配送方式id
      remark: '', // 留言
      values: this.$route.query.values, // 0, 详情立即购买；1, 购物车进入；2, 活动页立即购买；3, 订单 - 再来一单 - 确认下单
      summation: {
         totalPrice:0,  //下单原总价
         discountPrice:0, //优惠价格
         shippingPrice:0,  //配送费用 
         orderPrice:0,  //订单总金额
         useRange:'',  //适用范围 1.普通商品，2.定制商品 3 普通商品和定制商品
         weight:0,  //订单总重量
      }, // 总计obj
      shippingPrice:0,  //普通商品--配送费用
      diyShippingPrice:0,  //定制-配送费用
      showCity: false,
      showProvince:false, 
      showArea: false,
      countryList:[], //国家列表
      countryName:'',
      countryId:'',  //国家
      provinceId: '', // 省市区上级id
      cityId: '',
      townId: '',
      addressIndex: 100, // 地址选中索引
      address: '', // 添加地址
      addState: true,
      zipCode: '',
      userName: '',
      mobile: '',
      phone: '',
      showcov: false, // 是否显示新增地址信息
      postIndex: 0, // 配送方式选中索引
      materialId:[], //定制订单材质id 
      radio: 2, //支付的货币
      userTradeType: 1, // 1为现款支付，2为期间结算
      balancePrice:0,// 余额
      payTotalPrice:0,  //支付总金额
      orderType: 1, // 订单类型 1普通 2定制 4MTO（预售） 5直运
      postPrompt: false, // 配送不支持报错显示
      courier: 0, // 选择快递省内外
      courierList: [
        { index: 0, text: '广东省内' },
        { index: 1, text: '广东省外' }
      ],
      payWay: 1, // 支付方式
      userInfos: {}, // 用户信息
      isInvoice: 0, // 是否开具发票
      invoiceType: 1, // 发票类型 1.普通 2.增值税发票
      invoiceTypeList: [
        // 发票类型选择
        { index: 1, text: '普通发票' },
        { index: 2, text: '增值税发票' }
      ],
      invoiceTitle: 2, // 发票抬头 1.个人 2.单位
      invoiceTitleList: [
        // 发票抬头选择
        { index: 1, text: '个人' },
        { index: 2, text: '单位' }
      ],
      diyGoodsList:[],  //定制货品列表
      moreAddress: false, // 更多加地址显隐
      onceAgain: true, // 控制下单多次点击
      brandList:[],  //品牌协议
      
      // liu--end
    
      value: 0,
      provinceList: [],
      cityList: [],
      areaList: [],
      postShow: false, // 是否修改配送方式
      deleteIds: '', // 删除购物车
      acticityItem: [], // 选择完活动
      showSongItemList: [],
      showItemList: [],
      brandIds: [], // 当前商品品牌
      showOpenCut: false, // 膜切机协议弹框判断
      showOpenPro: false, // 定制协议弹框
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: 'zh-CNY', // 语种 
      addressInfo:{},  //地址信息 
      exchange:1,  //汇率

      // 代金券
      voucherList: [], // 代金券列表
      choosedVoucherAmount: 0, // 已选择代金券总金额
      rebateVoucherAmount: 0, // 代金券使用总金额
      rebateVoucherIds: [], // 代金券使用id列表
    };
  },
  filters: {
    keepTwoNum (value) {
      value = Number(value);
      return value.toFixed(2);
    },
    formatDate(time) {
      var date = new Date(time);
      return formatDate(date, "yyyy-MM-dd");
    },
  },
  computed: {
    // 购买商品列表(非赠品)
    splitItemList () {
      return this.showItemList.filter((data) => {
        return data.itemType == 1&&data.goodsType==1;
      });
    },
    // 购买商品列表（赠品）
    presentList () {
      return this.showItemList.filter((data) => {
        return data.itemType == 2&&data.goodsType==1;
      });
    }
    
  },
  mounted () {
    // liu
    this.exchange=Number( localStorage.getItem('exchange')); 
    this.getCountry();  //获取国家列表，获取收货地址
    this.goodListFun(); //处理货品数据
    this.getUserTradeType();  //获取支付方式
    this.getDeposit();  //获取用户余额
    this.getUserInfos(); //获取用户信息
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem('bLang')
      ? window.localStorage.getItem('bLang')
      : 'zh-CNY';

    this.getVoucherList(); // 代金券

    // 根据选择语言，默认结算方式
    if (this.$root.currency === 'USD') {
      this.radio = 1
    } else {
      this.radio = 2;
    }
  },
  methods: {
    // 处理下单的货品
    goodListFun(){
       let goodList=JSON.parse(localStorage.getItem('shopOrderList'));
       this.showItemList=goodList;
       this.summation={
         totalPrice:0,  //下单原总价
         discountPrice:0, //优惠价格
         shippingPrice:0,  //配送费用
         orderPrice:0,  //订单总金额
         useRange:1,  //适用范围 1.普通商品，2.定制商品 3 普通商品和定制商品
         weight:0,  //订单总重量
       }
       let promitionPrice=0;
       let puType=false;
       let dzType=false;
       goodList.forEach(item=>{ 
          if(item.itemType==1){
            this.summation.totalPrice+=item.salePrice*(item.zaiKuCount+item.zaiTuCount);
            promitionPrice+=Number(item.totalPrice); //优惠后的总价
            this.summation.discountPrice=item.allOrderMoney?item.allOrderMoney:(this.summation.totalPrice-promitionPrice);
            if(item.goodsType==2){
                this.materialId.push(item.diy.materialId)//定制订单材质id  
                dzType=true;
            }else{
              puType=true;
            }
            if(item.weight&&item.weight>0){
              this.summation.weight+=item.weight*(item.zaiKuCount+item.zaiTuCount);
            }
          }
       })
       if(puType&&!dzType){
          this.summation.useRange=1;
       }else if(!puType&&dzType){
           this.summation.useRange=2;
       }else if(puType&&dzType){
           this.summation.useRange=3;
       } 
      //  this.summation.orderPrice=this.summation.totalPrice-this.summation.discountPrice+
    },

    // 购买商品列表（定制）
    async diyListFun () {
      let userId=localStorage.getItem('userId');
      let goodList=JSON.parse(localStorage.getItem('shopOrderList'));
      let list=goodList.filter((data) => {return data.goodsType == 2;})
      if(list.length>0){
          list.forEach(item=>this.$set(item,'manufactors',item.diy.manufactors))
          this.diyGoodsList=this.sort_pro(list ,['manufactors']) ;
          let diyList=this.diyGoodsList;
          let wuliuPrice=0;
          for(let i=0;i<diyList.length;i++){
            let weight=0;
            let price=0;
            for(let j=0;j<diyList[i].children.length;j++){
              weight+=(diyList[i].children[j].weight*diyList[i].children[j].itemCount);
              price+=Number(diyList[i].children[j].totalPrice);
            }
              let params={
                distributorId:userId,  //分销商id
                billingMethod:1, //订单结算方式，1重量计费，2体积计费
                countryId:this.countryId,
                provinceId:this.provinceId,
                cityId:this.cityId,
                manufactors:diyList[i].manufactors, //工厂--生产商
                price:fomatFloat(price,2),  //订单总金额
                useRange:2,  //适用范围 1.普通商品，2.定制商品 3 普通商品和定制商品
                weight:weight,  //订单总重量 
              }
             
              // 获取配送方式
              let wuliuData=await logisticsCalculations(params);
              if(wuliuData.success){
                    let ocjV={
                      logisticsId:wuliuData.data[0].id, //配送方式id
                      logisticsName:wuliuData.data[0].name, //配送方式名称
                      logisticsType:2,  //配送方式类型：1 普通商品（标品） 2 定制工厂
                      manufactors:diyList[i].manufactors,  //生产商 YC云创 MK麦客 LHW联辉王
                    }
                    this.$set(diyList[i],'distributeList',wuliuData.data);  //配送方式列表
                    this.$set(diyList[i],'logisticss',ocjV);
                    this.$set(diyList[i],'cost',wuliuData.data[0].cost);
                    this.$set(diyList[i],'curVal',wuliuData.data[0].name);
                    this.$set(diyList[i],'curId',wuliuData.data[0].id)
                    this.$set(diyList[i],'postPrompt',false);
                    wuliuPrice+=wuliuData.data[0].cost;
              }
            
          }
          this.diyShippingPrice=wuliuPrice;  //定制物流费用
          this.summation.shippingPrice=wuliuPrice+(this.shippingPrice?this.shippingPrice:0);
          this.summation.orderPrice=this.summation.totalPrice-this.summation.discountPrice+wuliuPrice+(this.shippingPrice?this.shippingPrice:0);   //订单总价
      }

      
    },
    // 把对象数组按照某一个属性（或某几个属性）进行分类
    sort_pro(data, keys = []) {     //keys可以传一个数组
      var c = [];
      var d = {};
      for (var element of data) {
        let element_keyStr = "";
        let element_key = [];
        let element_keyObj = {};
        for (var key of keys) {
          element_key.push(element[key]);
          element_keyObj[key] = element[key];
        }
        element_keyStr = element_key.join("_");
        if (!d[element_keyStr]) {
          c.push({
            ...element_keyObj,
            children: [element]
          });
          d[element_keyStr] = element;
        } else {
          for (var ele of c) {
            let isTrue = keys.some(key => {
              return ele[key] != element[key];
            });
            if (!isTrue) {
              ele.children.push(element);
            }
          }
        }
      }
      return c;
    },
    
    // 地址列表
    addressList () {
        let id=localStorage.getItem('userId');
        let params={
           id:id,
           page:1,
           size:100
        }
        addressList(params).then((res) => {
          if (res.success) {
               let addressList=res.data.list;
              //  provinceName cityName districtName
               addressList.forEach((item,index)=>{
                 Vue.set(item, 'type', 0); // 控制编辑地址框显隐  
                 // 默认选中第一条
                 if (index === 0) { 
                   this.addressInfo=item; 
                    this.addressIndex = 0;
                    this.countryId=item.countryId;
                    this.deliveryId = item.id;
                    this.provinceId = item.provinceId;
                    this.cityId = item.cityId;
                    this.townId = item.districtId; 
                    this.userName =item.userName;
                    this.zipCode=item.zipCode;
                    this.phone=item.phone;
                    let countryList=this.countryList;
                    countryList.forEach(items=>{
                      if(items.id==item.countryId){
                        this.countryName=items.regionName;
                      }
                    })
                  }
                  if(item.countryId){

                  
                    region(item.countryId).then(countryRes => {
                      if (countryRes.success) {
                        let provinceList = countryRes.data.list;
                        provinceList.forEach((item2) => { 
                          if (item2.id==item.provinceId) {   
                            item.provinceName = item2.regionName;
                            region(item.provinceId).then(provinRes => {
                                if (provinRes.success) {
                                  let cityList = provinRes.data.list;
                                  cityList.forEach((item3) => {
                                    if (item3.id == item.cityId) {
                                      item.cityName = item3.regionName;
                                      
                                      region(item.cityId).then(cityRes => {
                                          if (cityRes.success) {
                                            let areaList = cityRes.data.list;
                                            areaList.forEach((item4) => {
                                              if (item4.id == item.districtId) {
                                                item.districtName = item4.regionName;
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

               
               
               })
                
                setTimeout(()=>{
                  this.addressLists = addressList;
                  this.diyListFun();  //处理定制货品列表（包括物流信息）
                  this.postList();
                },300)  
            
          } 
        });
    },
    // 新增地址信息
    addNewAddress () {
      // this.addressIndex = 100
      this.addressType=1;
      this.showcov = true;
      this.countryId='';
      this.provinceId = '';
      this.cityId = '';
      this.townId = '';
      this.countryName = '';
      this.address = '';
      this.zipCode = '';
      this.userName = '';
      this.mobile = '';
      this.phone = '';
      this.addState = true;
    },
    // 获取区域列表type:(1:国家，2：省，3：市，4：区)-y
    regionListFun(parentId,id,type,noAddress){

        region(parentId).then(res=>{
           let list=res.data.list;
           if(type==1){
                this.countryList = list;   
                if(noAddress!=='noList'||noAddress==null||noAddress=='undefined'||noAddress==''){
                   this.addressList();  //获取收货地址列表
                }
                
                console.log('国家：',res.data.list);
            }else if(type==2){
                this.provinceList =list;
                console.log('省：',res.data.list);  
            }else if(type==3){
                this.cityList = list;
                console.log('市：',res.data.list);  
            }else if(type==4){
                this.areaList =list;
                console.log('区：',res.data.list);  
            }
        })
    },
    // 获取国家列表-y
    getCountry(noList){
        this.regionListFun(0,-1,1,noList);
        // this.countryId=37;        
    },
    // 选择国家出现省会-y
    getCountryId(val){
      this.countryId = val;
      this.provinceId='';
      this.cityId='';
      this.townId='';
      this.showProvince=false;
       this.showCity = false;
       this.showArea = false; 
      if(val==37){
         this.showProvince=true;
         this.regionListFun(val,-1,2)
      }

        // this.countryName = name;  
        
    },
    // 选择省会--出现市级-y
    getProvinceId (val) {
       this.regionListFun(val,-1,3)
       this.showCity = true;
       this.showArea = false; 
       this.provinceId=val;
       this.cityId='';
       this.townId='';
      
    },
    // 选择市级--出现区域-y
    getCityId (val) {
      this.cityId=val;
      this.townId='';
      let list=this.cityList;
      this.showArea = false; 
      list.forEach(item=>{
        if(item.id==val){
           if(item.haveNext==1){
               this.showArea = true; 
               this.regionListFun(val,-1,4)
           }
        }
      })  
    },

    // 添加or编辑收货地址--Y
    addAddress () {
      var pattern = /^1[3456789]\d{9}$/; // 手机号
      var patternTwo = /[1-9]{1}(\d+){5}/; // 邮编
      if (this.cityId === ''&&this.countryId==37) {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('请选择省市区');
        } else {
          this.$message.warning('Please choose province, city and district.');
        }
        return false;
      }
      if ( this.address === '') {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('请输入详细地址');
        } else {
          this.$message.warning('Please enter detailed address.');
        }
        return false;
      }
      if ( this.userName === '') {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('请输入收货人姓名');
        } else {
          this.$message.warning('Please enter recipient name.');
        }
        return false;
      }
      if (this.mobile === '') {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('请输入手机号');
        } else {
          this.$message.warning('Please enter mobile phone number.');
        }
        return false;
      }
      this.mobile = this.mobile.replace(/\D/g, '');
      if (this.mobile.length !== 11) {
        if (this.mobile.length !== 11) {
          this.$message.warning('请输入正确的手机号');
          return false;
        }
        if (!pattern.test(this.mobile)) {
          this.$message.warning('请输入正确的手机号');
          return false;
        }
        if (!patternTwo.test(this.zipCode) && this.zipCode !== '') {
          this.$message.warning('请输入正确的邮编');
          return false;
        }
      }
      let id=localStorage.getItem('userId');
      var json ={
        address: this.address,
        addressType: 2,   //地址类型 1.公司地址 2.收货地址（固定传2
        cityId: this.cityId,
        countryId:this.countryId,  //国家id
        defaultFlag: 0,
        distributorId: id,
        districtId: this.townId,
        id: this.addressId,
        phone:this.mobile,
        provinceId: this.provinceId,
        userName: this.userName,
        zipCode: this.zipCode
      }
      let methods=this.addressType==1?'POST':'PUT';
      addressApi(methods, json).then((res) => {
        if (res.success) {
         
          if (this.$i18n.locale === "zh") {
             let textVal=this.addressType==1?'添加':'编辑'
            this.$message.success(textVal+"添收货地址成功");
          } else {  
             let textVal=this.addressType==1?'Add ':'Modify'
            this.$message.success(textVal+"shipping address successfully.");
          }
          this.showcov=false;
          
          this.addressList();
        } else  {
          that.$message(res.errMessage);
        }
      });
     
    },
    // 编辑地址-Y
    goEditor (item) {
      this.getCountry('noList');
      this.addressType=2;
      this.countryId=item.countryId;     
      this.provinceId=item.provinceId;
      this.cityId=item.cityId;
      this.townId=item.districtId;
      this.addressId=item.id;
      this.address=item.address;
      this.zipCode=item.zipCode;
      this.userName=item.userName;
      this.phone=item.phone;
      this.mobile=item.phone;
      if (item.countryId === 37) {
        // 国内
        this.showProvince=true;
        this.regionListFun(item.countryId,-1,2,'noList');
      }
      if (item.provinceId) {
        this.showCity = true;
        this.regionListFun(item.provinceId,-1,3,'noList');
      }
      if(item.districtId&&item.districtId!=''){
        this.showArea = true; 
        this.regionListFun(item.cityId,-1,4,'noList');
      }
      this.showcov = true;
    },

    //选择- 收获地址-Y
    getDeliveryId (item, index) {
      this.deliveryId = item.id;
      this.countryId=item.countryId;
      this.provinceId = item.provinceId;
      this.cityId = item.cityId;
      this.townId = item.districtId;
      this.addressIndex = index;
      this.userName =item.userName;
      this.zipCode=item.zipCode;
      this.phone=item.phone;
      let countryList=this.countryList;
      countryList.forEach(items=>{
        if(items.id==item.countryId){
           this.countryName=items.regionName;
        }
      })
      this.addressInfo=item;

      // 获得配送费用 (最新)   
        this.diyListFun();
        this.postList();
      
    },

     // 获取用户预存款方式--Y
    getUserTradeType () {  
        paymentMode().then((res) => {
          if (res.success) {
            // 1为现款支付，2为期间结算
            this.userTradeType = res.data.payWay; 
            if (this.$i18n.locale === 'zh') {
              if (this.userTradeType === 2) {
                this.payWay = 3; // 区间结算
              } else {
                this.payWay = 4; // 银行转账
              }
            } else {
              this.payWay = 4; // 银行转账
            }
          }
        });
    },

    // 获取账户余额--y
    getDeposit() {
      let id=localStorage.getItem('userId');
      userDeposit({id:id}).then((res) => {
          if (res.success) {
            this.balancePrice = res.data.accountBalance;
          }
        });
    },
    // 选择支付方式-Y
    getPayWay (type) {
      this.payWay = type;
    },
    // 获取配送方式列表--普通商品-Y
    postList () {
      let goodList1=JSON.parse(localStorage.getItem('shopOrderList'));
      
      let goodList=goodList1.filter(item=>item.goodsType==1);
      if(goodList.length>0){
        let price=0;
        let zaituWeight=0;
        let zaikuWeight=0;
        let zaituPrice=0;
        let zaikuPrice=0;
        for(let i=0;i<goodList.length;i++){
          if(goodList[i].itemType==1){
            price+=Number(goodList[i].totalPrice);
            zaituPrice+=(Number(goodList[i].totalPrice)/goodList[i].itemCount*goodList[i].zaiTuCount);
            zaikuPrice+=(Number(goodList[i].totalPrice)/goodList[i].itemCount*goodList[i].zaiKuCount);
          }
          if(goodList[i].weight&&goodList[i].weight!=0){
            zaituWeight+=(goodList[i].weight*goodList[i].zaiTuCount);
            zaikuWeight+=(goodList[i].weight*goodList[i].zaiKuCount);
          }
        }   
        let userId=localStorage.getItem('userId'); 
        
        let obj1=[{
          distributorId:userId,  //分销商id
          billingMethod:1, //订单结算方式，1重量计费，2体积计费
          countryId:this.countryId,
          provinceId:this.provinceId,
          cityId:this.cityId,
          price:zaituPrice,  //订单总金额
          useRange:1,  //适用范围 1.普通商品，2.定制商品 3 普通商品和定制商品
          weight:zaituWeight,  //订单总重量 
        }];
       
        let params=[{
          distributorId:userId,  //分销商id
          billingMethod:1, //订单结算方式，1重量计费，2体积计费
          countryId:this.countryId,
          provinceId:this.provinceId,
          cityId:this.cityId,
          price:this.onWaySplitFlag==1?zaikuPrice:price,  //订单总金额
          useRange:1,  //适用范围 1.普通商品，2.定制商品 3 普通商品和定制商品
          weight:this.onWaySplitFlag==1?zaikuWeight:(zaikuWeight+zaituWeight),  //订单总重量 
        }]
       params= this.onWaySplitFlag==1?[...params,...obj1]:[...params]
       
        logisticsCalculationss(params).then(res=>{
          if(res.success){  
              this.distributeList = res.data;
              this.curVal=res.data[0].name;
              this.curId=res.data[0].id;
              
              this.shippingPrice = res.data[0].cost;
              if(this.diyGoodsList.length>0){
                  let list=this.diyGoodsList;
                  let costPrice=0;
                  for(let i=0;i<list.length;i++){
                    costPrice+=list[i].cost;
                  }
                  this.summation.shippingPrice=costPrice+res.data[0].cost;
                  this.summation.orderPrice=this.summation.totalPrice-this.summation.discountPrice+res.data[0].cost+costPrice;
              }else{
                   this.summation.shippingPrice=res.data[0].cost;
                   this.summation.orderPrice=this.summation.totalPrice-this.summation.discountPrice+res.data[0].cost;
              }
                    
          }else{
             this.summation.orderPrice=this.summation.totalPrice-this.summation.discountPrice;
          }        
        })
        
      }
      
   
      
    },
    // 定制选择配送方式
    DiyGetPostId(diyItem,item){
       this.$set(diyItem,'cost',item.cost);
       this.$set(diyItem,'curVal',item.name);
       this.$set(diyItem,'postPrompt',true);  
       this.$set(diyItem,'curId',item.id)
       let objV={
          logisticsId:item.id, //配送方式id
          logisticsName:item.name, //配送方式名称
          logisticsType:2,  //配送方式类型：1 普通商品（标品） 2 定制工厂
          manufactors:diyItem.logisticss.manufactors,  //生产商 YC云创 MK麦客 LHW联辉王
        }
        
        this.$set(diyItem,'logisticss',objV);
        let list=this.diyGoodsList;
        let cost=0;
        for(let i=0;i<list.length;i++){
           cost+=list[i].cost;
        }
        this.summation.shippingPrice = this.shippingPrice+cost;
        this.summation.orderPrice=this.summation.totalPrice-this.summation.discountPrice+this.summation.shippingPrice;

    },
    // 选择是否拆弹
    splitFlagFun(val,id){
       this.postList()
    },
    // 选择配送方式-y
    getPostId (index, item, id) {
      this.curVal = item.name;
      this.curId = id;
      
      this.postPrompt = true;
      let list=this.diyGoodsList;
      let cost=0;
      for(let i=0;i<list.length;i++){
          cost+=list[i].cost;
      }
      this.summation.shippingPrice =item.cost+cost;
      this.summation.orderPrice=this.summation.totalPrice-this.summation.discountPrice+this.summation.shippingPrice;
     
      if (index !== '') {
        this.postIndex = index;
      }
      
    },

    // 获得用户信息-Y
    getUserInfos () {
        let id=localStorage.getItem('userId');
        userInfo({id:id}).then((res) => {
          if (res.success) {
            this.userInfos = res.data;
          }
        });
    },
    // 发票类型-Y
    getInvoiceType (index) {
      this.invoiceType = index;
    },
    // 发票抬头-Y
    getInvoiceTitle (index) {
      this.invoiceTitle = index;
    },
    // 是否开具发票-Y
    getInvoice () {
      if (this.isInvoice === 0) {
        this.isInvoice = 1;
      } else {
        this.isInvoice = 0;
      }
    },
    // 选择寄往的省份
    getCourier (index) {
      this.courier = index;
    },
    // 选择支付的货币-Y
    changeCurrency () {},

    // 删除货品
    deleteShoppingcartFun(ids){
        deleteShoppingcart({ids: ids}).then((res) => {
          if (res.success) {
           
          }
        });
    },

     // 确认下单
    confirmOrder () {
      // this.showLoading = true;
      this.onceAgain = false;
      if (this.$i18n.locale === 'zh') {
        this.currencyType = 'CNY';
      } else {
        if (this.radio === 1) {
          // 币种
          this.currencyType = 'USD';
        } else {
          this.currencyType = 'CNY';
        }
      } 
      // 收货地址编号--判断是否选择收货地址
      if (this.deliveryId === '') { 
        this.onceAgain = true;
        this.showLoading = false;
        this.$message.warning(this.$t('ConfirmOrder.ShippingAddress'));
        return
      }
      
      let list1=localStorage.getItem('shopOrderList');
      let list=JSON.parse(list1);

      let goodList=[];
      let delegetIds=[];
      for(let i=0;i<list.length;i++){ 
        delegetIds.push(list[i].id)
        let objL={
          diyType:list[i].diyType,
          goodsPromotionId:(list[i].goodsPromotionId&&list[i].goodsPromotionId!='no'&&list[i].conditionsId)?list[i].conditionsId:'',
          goodsType:list[i].goodsType,
          groupSeckillId:(list[i].groupSeckillId&&list[i].groupSeckillId!='no')?list[i].groupSeckillId:'',
          itemCode:list[i].itemCode,
          itemCount:list[i].zaiTuCount+list[i].zaiKuCount,
          itemId:list[i].itemId,
          itemInCount:list[i].zaiKuCount, //下单在库数量
          itemOnWayCount:list[i].zaiTuCount, //下单在途数量
          itemType:list[i].itemType,
          itemMtoCount:0,  //货品预售数量
          mtoType:list[i].mtoFlag?list[i].mtoFlag:0, //是否预售 1 是 0 否
          oversoldFlag:0, //是否支持超卖 1、支持 0 不支持
        }
        if(list[i].goodsType==2){
          objL={...objL,...{diy:list[i].diy}}
        }
        goodList.push(objL);
      }
      let diyList=this.diyGoodsList;
      let logisticss=[];
      for(let i=0;i<diyList.length;i++){
        logisticss.push(diyList[i].logisticss)
      }
      // 普通商品物流信息
      if(this.curId!=''){
        let ptLogisticss={
          logisticsId:this.curId,
          logisticsName:this.curVal,
          logisticsType:1,
          
        }
        logisticss.push(ptLogisticss);
      }
      

      let params={
        currencyType:this.currencyType, //币种
        //收货信息 --Y
        delivery:{  
          userName:this.userName,
          countryId:this.countryId,
          countryName:this.countryName,
          provinceId:this.provinceId?this.provinceId:0,
          provinceName:this.addressInfo.provinceName,
          cityId:this.cityId?this.cityId:0,
          cityName:this.addressInfo.cityName,
          districtId:this.townId?this.townId:0,
          districtName:this.addressInfo.districtName,
          address:this.addressInfo.address,
          mobile:this.phone,
          zipCode:this.zipCode?this.zipCode:'',
        },
        distributionAmount:fomatFloat(this.summation.shippingPrice,2),  //物流费用-y
        // orderAmount:fomatFloat(this.summation.orderPrice,2),  //商品结算金额(优惠后金额)-y
        orderAmount:Number(fomatFloat(this.summation.orderPrice,2) - this.rebateVoucherAmount).toFixed(2),  //商品结算金额(优惠后金额)-y
        //  订单发票信息
         invoiceFlag:this.isInvoice,  //是否开具发票 0.否，1.是-y
         invoice:{
           invoiceType:this.invoiceType, //发票类型 1.普通 2.增值税发票-y
           invoiceTitleType:this.invoiceTitle, //发票抬头类型 1.个人 2.单位-y
           name:(this.invoiceTitle==2&&this.userInfos.financial) ? this.userInfos.financial.invoiceTitleName : this.userInfos.name,  //发票名称 (个人姓名或单位名称)-Y
           taxpayerNumber:(this.invoiceTitle==2&&this.userInfos.financial)?this.userInfos.financial.taxpayerNumber:'',  //纳税人识别号(发票抬头为2时必填)-Y
           bankAccount:(this.invoiceTitle==2&&this.userInfos.financial)?this.userInfos.financial.registeredBankAccount:'',  //银行账号(发票抬头为2时必填)-Y
           bankAccountName:(this.invoiceTitle==2&&this.userInfos.financial)?this.userInfos.financial.registeredBankDepositName:'',  //银行账户名(发票抬头为2时必填)-Y
           registerPhone:(this.invoiceTitle==2&&this.userInfos.financial)?this.userInfos.financial.registeredPhone:'',  //公司注册电话(发票抬头为2时必填)-Y
           registerAddress:(this.invoiceTitle==2&&this.userInfos.financial)?this.userInfos.financial.registeredAddress:'',  //公司注册电话(发票抬头为2时必填)-Y
         }, 
         logisticss:logisticss?logisticss:[], //配送方式列表
         onWaySplitFlag:this.onWaySplitFlag,  //订单是否拆分(在途在库) 1、拆 0、否-Y
         orderSource:'GF60001',  //订单来源：平台编码 分销PC端B2Bpc和分销移动端B2Bh5, GF60001
         payWay:this.payWay, //1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,-Y
         remark:this.remark,  //订单备注-Y 
         goodss:goodList,//  商品明细列表
        // 代金券
        rebateVoucherAmount: Number(this.rebateVoucherAmount).toFixed(2), // 代金券使用总金额
        rebateVoucherIds: this.rebateVoucherIds, // 代金券使用id列表
      }
      // value 下单来源：0, 详情立即购买；1, 购物车进入；2, 活动页立即购买；3, 订单 - 再来一单 - 确认下单
      placeOrder(params).then(res=>{
        if(res.success){
            this.onceAgain = false;
            this.deleteShoppingcartFun(delegetIds);
            // this.goShopOrder(jsonp);  //跳转下单
            let ids=res.data.ids.join(',');
            let userId=localStorage.getItem('userId');
            let userName=localStorage.getItem('userName');
            // 快钱支付
            if(this.payWay==6||this.payWay==5){
               let payParams={
                 customerFlag:0,
                 orderId:ids,
                 payMethod:this.payWay==6?'kuaiqian_merchant':'balance',
                 payerId:userId,
                 payerName:userName,
                 redirectUrl: "https://www.bat.com/#/OrderManage"
               }
               payCreateTrade(payParams).then(payRes=>{
                 if(payRes.success){
                   if(this.payWay==6){
                      window.location.href = payRes.data.kuaiQianReap.h5Url; // 跳转链接
                   }else{
                      this.$router.push({name: 'OrderSuccess',query: { orderId:ids }});
                   }
                   
                  //  window.open(payRes.data.kuaiQianReap.h5Url);
                 }
               });
            }else{
                 this.$router.push({name: 'OrderSuccess',query: { orderId:ids }});
            }
        }else{
            this.onceAgain = true;
            this.$message(res.errMessage);
            // this.$router.push({name:'ShopCarts'})
        }
      })
  
    },  
    // 改变表头样式-Y
    getRowClass ({ rowIndex }) {
      
      if (rowIndex === 0) {
        return 'border-right: none;';
      } else {
        return '';
      }
    },
    // 更多地址显隐-y
    showMoreAddress () {
      this.moreAddress = !this.moreAddress;
    },
    // 关闭编辑和添加收货地址弹框-Y
    shutLog () {
      this.showcov = false;
    },
    
    
    dateToNum,
    
  
    
   
   
    deleteShop (ids) {
      this.$api
        .delete(this, 'user/u/shoppingCart', { ids: ids })
        .then((res) => {
          if (res.code === 0) {
          }
        });
    },
    // 重新下单
    reConfirmOrder () {
      this.postShow = false;
      this.confirmOrder();
    },
    // 下单
    goShopOrder (jsonp) {
      let url;
      if (jsonp.goodsType === 7) {
        url = 'commission/order/add';
      } else {
        url = 'user/u/order';
      }
      this.$api
        .post(this, url, jsonp)
        .then((res) => {
          var secondsArea = 1;
          this.intervalid = setInterval(() => {
            secondsArea--;
            if (secondsArea === 0) {
              this.showLoading = false;
              clearInterval(this.intervalid);
              if (res.code === 0) {
                if (this.$i18n.locale === 'zh') {
                  this.$message.success('订单提交成功');
                } else {
                  this.$message.success('Order submitted successfully');
                }
                if (this.deleteIds !== '') {
                  this.deleteShop(this.deleteIds); // 删除购物车将购买的商品
                  window.localStorage.removeItem('shopCatShop');
                  window.localStorage.removeItem('spesShopCatShop');
                  window.localStorage.removeItem('spesShopCatnewShop');
                }
                window.localStorage.removeItem('songGoodsShop');
                window.localStorage.removeItem('presellShop');
                window.localStorage.removeItem('groupBuyingShop'); // 拼团商品
                var allIds = [];
                let ids = jsonp.goodsType === 7 ? res.data.ids : res.ids;
                allIds = ids.indexOf(',') === -1 ? ids : ids.split(','); // 单个或多个订单
                if (this.payWay === 6) {
                  // 快钱支付
                  let ids = '';
                  if (allIds instanceof Array) {
                    ids = allIds[0];
                    for (var i = 1; i < allIds.length; i++) {
                      ids = ids + ',' + allIds[i];
                    }
                  } else {
                    ids = allIds;
                  }
                  this.$api
                    .post(this, 'user/u/order/kuaiQianPay', { ids: ids })
                    .then((res) => {
                      if (res.code === 0) {
                        window.location.href = res.redirectUrl; // 跳转链接
                      }
                    });
                } else {
                  this.$router.push({
                    name: 'OrderSuccess',
                    query: { order_ids: allIds }
                  });
                }
              } else if (
                res.code === 1125 ||
                res.code === 1115 ||
                res.code === 1121 ||
                res.code === 1122
              ) {
                // 配送方式不支持
                this.postShow = true;
              } else if (res.code === 120035) {
                // 重复提交，跳转我的订单
                this.$router.push({
                  name: 'OrderManage'
                });
              } else {
                this.onceAgain = true;
              }
            }
          }, 1000);
        })
        .catch((error) => {
          this.onceAgain = true;
          this.showLoading = false;
          clearInterval(this.intervalid);
        });
    },
    // 代金券
    getVoucherList() {
      let userId = localStorage.getItem("userId");
      let parmas = {
        page: 1,
        size: 5,
        voucherStatusStr: "5", // 可用
        distributorId: userId,
        // 优先显示快到期的代金券
        sortType: 1, // 1 正序, 2 倒序
        sortColumn: 4, // 1 创建时间, 2 更新时间, 3 开始时间, 4 结束时间
      };
      voucherList(parmas).then((res) => {
        if (res.success) {
          let voucherList = res.data.list;
          
          voucherList.forEach((item, index) => {
            this.$set(item, "choosed", false);
            // 默认第一个选中
            if (index === 0) {
              this.$set(item, "choosed", true);
            }
          })

          this.voucherList = voucherList;
          this.calcVoucher(voucherList);
        }
      });
    },
    calcVoucher(arr) {
      if (arr && arr.length > 0) {
        this.rebateVoucherIds = [];
        this.choosedVoucherAmount = 0;
        arr.forEach((item) => {
          if (item.choosed) {
            // 已选择代金券总金额
            this.choosedVoucherAmount += item.balance;
            // 代金券使用id列表
            this.rebateVoucherIds.push(item.id);
          }
        })
      }
    },
    // 选择代金券
    chooseVoucher(item) {
      if (item.choosed) {
        this.$set(item, "choosed", false);
      } else {
        if (this.choosedVoucherAmount > this.summation.orderPrice) {
          this.$set(item, "choosed", false);
        } else {
          this.$set(item, "choosed", true);
        }
      }

      this.calcVoucher(this.voucherList);
    }
  },
  watch: {
    "choosedVoucherAmount": {
      handler(val) {
        if (val > 0) {
          // 代金券使用总金额
          if (val >= this.summation.orderPrice) {
            this.rebateVoucherAmount = this.summation.orderPrice;
          } else {
            this.rebateVoucherAmount = val;
          }
        } else {
          this.rebateVoucherAmount = 0;
        }
      }
    },
    "summation.orderPrice": {
      handler(val) {
        if (val > 0) {
          // 代金券使用总金额
          if (this.choosedVoucherAmount >= val) {
            this.rebateVoucherAmount = val;
          } else {
            this.rebateVoucherAmount = this.choosedVoucherAmount;
          }
        }
      }
    }
  },
  
  beforeRouteEnter (to, from, next) {
    if (from.name === null && to.name === 'ConsigneeInfor') {
      next((vm) => {
        next({ path: '/shopCarts' })
      });
    } else {
      next();
    }
  }
};
</script>
<style scoped="scoped" lang='less'>
@import url("../assets/less/variable.less");
.el-select{
  width:470px;
}
.cd-title{
   color: #00c9dc;
   font-size: 16px;
}
.ps-tipDiv{
  display: flex;
  align-items: center;
  .ps-tip{
    margin-left: 20px;
    span{
      display: block;
    }
    span:nth-child(1){
      color: #888;
    }
    span:nth-child(2){
      color: red;
      font-size: 12px;
      margin-top: 5px;
    }
  }
}
.user {
  width: 100%;
}
.main {
  margin-top: 37px;
  width: 1200px;
  .main-one {
    border: 2px solid @lightGrayBg;
    border-radius: 8px;
    padding: 20px 30px 0;
  }
  .confirm-address {
    .confirm-title {
      position: relative;
      .title {
        font-size: 20px;
        padding-left: 8px;
        padding-bottom: 20px;
        border-bottom: 1px solid @lightGrayBg;
      }
      .add-btn {
        position: absolute;
        right: 0;
        bottom: 8px;
        padding: 0 10px;
        height: 36px;
        line-height: 36px;
        color: @sLightGray;
        border: 1px solid @bdColor;
        .el-icon-plus {
          margin-right: 5px;
        }
      }
    }
    .addr-tips {
      margin-top: 20px;
      font-size: 12px;
      color: @redLabel;
      line-height: 17px;
    }
    .addr-list {
      margin-top: 10px;
      font-size: 14px;
      color: @lightGray;
      li {
        cursor: pointer;
        &.list-wrap {
          padding-left: 28px;
          background: url("../../src/assets/images/yuan3.png") no-repeat 30px
            center;
          &.yuan-gou {
            background: url("../../src/assets/images/yuan4.png") no-repeat 30px
              center;
          }
        }
        &.front-addr-list {
          float: left;
          width: 374px;
          height: 142px;
          & + .list-wrap {
            clear: both;
          }
          .front-addr {
            position: relative;
            margin-top: 8px;
            padding-left: 27px;
            padding-right: 27px;
            width: 306px;
            height: 120px;
            border: 1px solid @bdLighterColor;
            .addr-name {
              padding-top: 25px;
              font-size: 14px;
              color: @lightBlack;
              span {
                float: right;
              }
            }
            .addr-info {
              margin-top: 5px;
              font-size: 14px;
              color: @lightGray;
              height: 36px;
              line-height: 18px;
              text-overflow: ellipsis;
              display: -webkit-box;
              -webkit-line-clamp: 2;
              -webkit-box-orient: vertical;
              overflow: hidden;
            }
            .default {
              position: absolute;
              top: 0;
              left: 0;
              width: 78px;
              height: 20px;
              line-height: 20px;
              font-size: 12px;
              color: @white;
              background-color: @bdLighterColor;
              border: none;
              border-radius: 0;
              &.no-word {
                background-color: @white;
              }
            }
            .editor {
              margin-top: 5px;
              color: @lightBlack;
              i {
                color: @blue;
              }
            }
          }
          &.active {
            background: url("../../src/assets/images/order/addr-bg.png")
              no-repeat;
            background-size: 100% 100%;
            & + .front-addr-list {
              margin-left: 14px;
            }
            .front-addr {
              margin-top: 10px;
              padding-left: 37px;
              border: none;
              .default {
                left: 21px;
                background-color: @blue;
                &.no-word {
                  background-color: transparent;
                }
              }
            }
          }
        }
      }
    }
    .more-address {
      padding-top: 20px;
      font-size: 14px;
      color: @lightGray;
      text-align: center;
      clear: left;
      .el-icon-arrow-down {
        line-height: 20px;
      }
      .rotate {
        transform: rotate(180deg);
      }
    }
    .cons {
      line-height: 20px;
    }
    .editor {
      font-size: 14px;
      color: @blue;
      .el-icon-edit-outline {
        margin-right: 2px;
        line-height: 20px;
      }
    }
    .default {
      width: 70px;
      height: 20px;
      line-height: 20px;
      border-radius: 5px;
      display: inline-block;
      &.no-word {
        display: none;
      }
    }
    .yuan {
      margin-top: 4px;
      width: 14px;
      height: 14px;
    }
    .yuan-gou {
      background: url("../../src/assets/images/yuan4.png") no-repeat 30px center;
    }
  }
  .addr-title-wrap {
    .addr-title {
      border-bottom: 1px solid @lightGrayBg;
    }
  }
  .post-style {
    .courier {
      margin-top: 20px;
      width: 100%;
      overflow: hidden;
      /deep/ .el-input__inner {
        padding-left: 20px;
        border: 1px solid @bdLighterColor;
        border-radius: 0;
      }
      .current-post-style {
        margin-top: 10px;
        margin-bottom: 30px;
        width: 435px;
        padding: 10px 20px;
        border: 1px solid @bdLighterColor;
        line-height: 24px;
        .post-price,
        .post-info {
          color: @lightBlack;
        }
      }
    }
    .package-mail {
      line-height: 40px;
    }
    .urgent {
      .checkedInput {
        margin-left: 40px;
        height: 30px;
        .chenked {
          cursor: pointer;
          width: 19px;
          height: 19px;
          display: inline-block;
          vertical-align: -5px;
          -webkit-appearance: none;
          appearance: none;
          outline: none;
          margin-right: 5px;
          font-size: 14px;
          color: #333;
          background: url(../../src/assets/images/un-selected.png) no-repeat
            center center;
        }
        .haschecked {
          background: url(../../src/assets/images/selected.png) no-repeat center
            center;
        }
      }
    }
  }
  .write-address {
    .write-cons {
      overflow: hidden;
      .items {
        span {
          display: block;
          width: 80px;
          line-height: 30px;
        }
        .search-input {
          width: 328px;
          height: 28px;
          border: 1px solid #ccc;
          input {
            padding-top: 5px;
            padding-left: 20px;
            width: 215px;
            border: 0;
          }
        }
        .item {
          input.trade-input {
            padding-left: 10px;
            width: 80px;
            height: 30px;
            line-height: 30px;
            box-sizing: border-box;
            border: 1px solid #ccc;
            font-size: 13px;
            background: url("../../src/assets/images/selectUl.png") no-repeat
              68px center;
            border-radius: 5px;
          }
          .selectUl {
            position: absolute;
            top: 30px;
            left: 0;
            z-index: 11;
            width: 80px;
            box-sizing: border-box;
            border: 1px solid #ccc;
            li {
              padding-left: 10px;
              line-height: 30px;
              cursor: pointer;
            }
            li:hover {
              background: #00c9dc;
              color: #fff;
            }
          }
        }
        .time-input {
          input {
            padding-left: 20px;
            width: 138px;
            height: 30px;
            box-sizing: border-box;
            border: 1px solid #ccc;
          }
        }
        .time-state {
          line-height: 30px;
        }
        .item-title {
          font-size: 14px;
          color: @lightBlack;
        }
        .write-text {
          margin-top: 10px;
          textarea {
            display: block;
            padding: 10px 15px;
            width: 442px;
            height: 71px;
            border: 1px solid @bdLighterColor;
          }
        }
      }
    }
  }
  .pay-style {
    .pay-money {
      overflow: hidden;
      ul {
        li {
          margin-bottom: 10px;
          float: left;
          width: 125px;
          height: 58px;
          padding-left: 35px;
          line-height: 58px;
          font-size: 14px;
          color: @lighterBlack;
          text-align: center;
          border: 1px solid @payBd;
          cursor: pointer;
            span{
              display: block;
              height: 20px;
            }
            span:nth-child(1){
               margin-top:10px;
            }
            span:nth-child(2){
               color: red;
               font-size: 12px;
            }
            
         
          & + li {
            margin-left: 30px;
          }
          &.space-pay {
            background: url("../assets/images/order/space-pay.png") no-repeat
              14px center;
          }
          &.bank-pay {
            background: url("../assets/images/order/bank-pay.png") no-repeat
              14px center;
          }
          &.net-pay {
            padding-left: 14px;
            background: url("../assets/images/order/net-pay.png") no-repeat 14px
              center;
          }
          &.balance-pay {
            padding-left: 14px;
            background: url("../assets/images/order/balance-pay.png") no-repeat
              14px center;
              line-height: 22px;
          }
          
          
          &.zhi-pay {
            padding-left: 7px;
            background: url("../assets/images/order/zhi-pay.png") no-repeat 14px
              center;
          }
          &.wechat-pay {
            padding-left: 14px;
            background: url("../assets/images/order/wechat-pay.png") no-repeat
              14px center;
          }
        }
        .balance-payN{
             background: #F6F6F6!important;
             display: flex;
             align-items: center;
             line-height: 10px; 
             div{
               width: 120px;
               text-align: left;
               margin-left: 5px;
             }  
        }
        li:hover,
        li.current {
          position: relative;
          border: 1px solid @blue;
          overflow: hidden;
          &::after {
            content: "";
            position: absolute;
            right: 0;
            bottom: 0;
            width: 30px;
            height: 27px;
            background: url("../assets/images/order/pay-active.png") no-repeat;
          }
        }
        .balance-payN:hover,.balance-payN.current{
          border:1px solid #E3E3E3!important;
          &::after {
            content: "";
            position: absolute;
            right: 0;
            bottom: 0;
            width: 30px;
            height: 27px;
            background: none!important;
          }
        }
      }

      &.voucher-list {
        ul {
          padding: 0 15px;
          li {
            padding: 19px 23px;
            width: 350px;
            height: 118px;
            box-sizing: border-box;

            & + li {
              margin-left: 28px;
            }

            &:nth-child(3n + 1) {
              margin-left: 0;
            }
          }
          li:hover,
          li.current {
            border: 1px solid @redLabel;
            &::after {
              background: url("../assets/images/order/voucher-active.png") no-repeat;
            }
          }
        }

        .voucher-list-item {
          position: relative;
          font-size: 12px;
          text-align: left;
          line-height: 17px;
          background: url("../assets/images/order/voucher-bg.png") no-repeat;
          background-size: 81px 81px;
          background-position: 23px 19px;

          .name {
            font-size: 16px;
            color: @lightBlack;
            line-height: 22px;
          }

          .no {
            margin-top: 12px;
            color: @darkGray;
          }

          .time {
            margin-top: 12px;
            color: @lighterGray;
          }

          .amount {
            position: absolute;
            top: 50%;
            right: 20px;
            color: @redLabel;
            transform: translateY(-50%);

            span {
              display: inline;
              font-size: 32px;
            }
          }
        }
      }
    }
  }
  /deep/ .el-table {
    width: 100%;
    overflow: hidden;
    padding-bottom: 20px;
    border: none;
    &::before {
      background-color: @white;
    }
    tr {
      border: none;
      & + tr {
        border-top: 1px dashed @bdLighterColor;
      }
    }
    th {
      height: 30px;
      line-height: 30px;
      font-size: 12px;
      color: @gray;
      font-weight: normal;
      text-align: center;
      background-color: @bdLightColor;
      border: none;
    }
    td {
      height: 60px;
      font-size: 12px;
      color: @lightBlack;
      text-align: center;
      border: none;
    }
  }
  .shop-list {
    .blue {
      color: @blue;
    }
    .red {
      color: @red;
    }
    .el-table {
      .item {
        padding: 0;
        border: 0;
        background-color: @blue;
        color: @lightBlack;
        font-weight: bold;
        font-size: 13px;
      }
    }
  }
  .invoice-info {
    .title {
      border-bottom: 1px solid @lightGrayBg;
      .state {
        color: @redLabel;
      }
    }
    .contents {
      overflow: hidden;
      .issue-bill {
        height: 30px;
        line-height: 30px;
      }
      .send-title {
        margin-right: 18px;
        font-size: 12px;
        color: @lightBlack;
      }
      .checkedInput {
        .check-item {
          width: 120px;
          font-size: 12px;
          color: @lightBlack;
        }
        .chenked {
          cursor: pointer;
          width: 19px;
          height: 19px;
          display: inline-block;
          vertical-align: -5px;
          -webkit-appearance: none;
          appearance: none;
          outline: none;
          margin-right: 5px;
          font-size: 14px;
          color: #333;
          background: url("../../src/assets/images/yuan3.png") no-repeat center
            center;
        }
        .haschecked {
          background: url("../../src/assets/images/yuan4.png") no-repeat center
            center;
        }
      }
      .search-input {
        width: 248px;
        height: 31px;
        line-height: 31px;
        border: 1px solid @bdLightColor;
        input {
          padding-left: 20px;
          border: 0;
        }
      }
      .send-province {
        height: 20px;
        line-height: 20px;
        .yuan {
          margin-top: 2px;
        }
      }
      .look-up {
        .look-head {
          .yuan {
            margin-top: 8px;
          }
        }
      }
      .look-foot {
        .items {
          line-height: 33px;
        }
      }
    }
  }
  .totals {
    border: 2px solid @lightGrayBg;
    border-radius: 8px;
    padding: 20px 30px;
    .item {
      font-size: 14px;
      color: @lightBlack;
      line-height: 25px;
      .red {
        width: 80px;
        margin-left: 8px;
        color: @lighterRed;
      }
      &.item-total {
        margin-top: 20px;
        .item-num {
          font-size: 18px;
          font-weight: bold;
        }
      }
    }
  }
  .comfirm-order {
    margin-top: 15px;
    div {
      width: 154px;
      height: 40px;
      line-height: 40px;
      background-color: @blue;
      border-radius: 4px;
    }
  }
}
.yuan {
  width: 14px;
  height: 14px;
  background: url("../../src/assets/images/yuan3.png") no-repeat center center;
}
.yuan-gou {
  background: url("../../src/assets/images/yuan4.png") no-repeat center center;
}
.line-height30 {
  line-height: 30px;
}
.mar-left16 {
  margin-left: 16px;
}
.shop-operate {
  padding-left: 220px;
  .operate {
    span {
      display: inline-block;
      min-width: 85px;
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
  // max-height: 380px;
  // height:auto!important;
  border: 1px solid #ccc;
  border-radius: 5px;
  z-index: 99;
  background: #fefefe;
  display: table;
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
  .address-info {
    .items {
      span {
        display: block;
        width: 120px;
        line-height: 30px;
      }
      .search-input {
        width: 328px;
        height: 28px;
        // border:1px solid #ccc;
        input {
          padding-top: 5px;
          padding-left: 20px;
          width: 308px;
          border: 0;
        }
      }
      .item {
        input.trade-input {
          padding-left: 10px;
          width: 100px;
          height: 30px;
          line-height: 30px;
          box-sizing: border-box;
          border: 1px solid #ccc;
          font-size: 12px;
          background: url("../../src/assets/images/selectUl.png") no-repeat 88px
            center;
          border-radius: 5px;
        }
        .selectUl {
          position: absolute;
          top: 30px;
          left: 0;
          z-index: 11;
          width: 85px;
          max-height: 250px;
          box-sizing: border-box;
          border: 1px solid #ccc;
          overflow-y: auto;
          li {
            padding-left: 10px;
            line-height: 30px;
            cursor: pointer;
            box-sizing: border-box;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
          li:hover {
            background: #00c9dc;
            color: #fff;
          }
        }
      }
      .search-choose {
        .yuan {
          margin-top: 4px;
          width: 14px;
          height: 14px;
        }
        .yuan-nogou {
          background: url("../../src/assets/images/yuan3.png") no-repeat center
            center;
        }
        .yuan-gou {
          background: url("../../src/assets/images/yuan4.png") no-repeat center
            center;
        }
        .save {
          line-height: 22px;
        }
      }
    }
  }
  .button {
    margin: 0 auto;
    width: 276px;
    span {
      margin-top: 20px;
      display: block;
      width: 330px;
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
/*修改配送方式*/
.modify-postStyle {
  height: 220px;
  ul {
    li {
      .chenked-div {
        padding-left: 20px;
        padding-top: 12px;
        position: relative;
        .chenked {
          position: absolute;
          top: 12px;
          left: 0;
          cursor: pointer;
          width: 19px;
          height: 19px;
          display: inline-block;
          vertical-align: -5px;
          -webkit-appearance: none;
          -moz-appearance: none;
          appearance: none;
          outline: none;
          margin-right: 5px;
          font-size: 14px;
          color: #333;
          background: url("../../src/assets/images/yuan3.png") no-repeat center
            center;
        }
        .without-chenked {
          display: inline-block;
          width: 200px;
        }
      }
    }
    li.current {
      .chenked-div {
        .haschecked {
          background: url("../../src/assets/images/yuan4.png") no-repeat center
            center;
        }
      }
    }
  }
}
.scroll-y {
  overflow-y: scroll;
}
.order-btn {
  padding-top: 30px;
  width: 190px;
  span {
    display: inline-block;
    width: 83px;
    height: 30px;
    line-height: 30px;
    border-radius: 5px;
  }
}
.emulate-ios-button {
  display: block;
  width: 50px;
  height: 30px;
  background: #ccc;
  border-radius: 15px;
  cursor: pointer;
  position: relative;
  -webkit-transition: all 0.3s ease;
  transition: all 0.3s ease;
}
.emulate-ios-button:after {
  content: "";
  display: block;
  width: 25px;
  height: 25px;
  border-radius: 100%;
  background: #fff;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.1);
  position: absolute;
  left: 0;
  top: 3px;
  -webkit-transform: translateZ(0);
  transform: translateZ(0);
  -webkit-transition: all 0.3s ease;
  transition: all 0.3s ease;
}

.emulate-ios-button:active:after {
  width: 11px;
}
.raw-checkbox {
  height: 20px;
  width: 20px;
}

.raw-checkbox:checked + label {
  background: @blue;
}

/* 这里是伪元素偏移,初始是0.9+0.05 ,所以这里1.05rem */
.raw-checkbox:checked + label:after {
  left: 24px;
}

.raw-checkbox:checked + label:active:after {
  left: 5px;
}
.raw-checkbox:disabled + label {
  background: #d5d5d5;
  pointer-events: none;
}
.raw-checkbox:disabled + label:after {
  background: #bcbdbc;
}
.wen-hao1 {
  top: 10px;
  right: 10px;
}

.wen-hao2 {
  top: 10px;
  right: 35px;
}
.wen-hao3 {
  top: 10px;
  right: 30px;
}
</style>
