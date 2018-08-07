<!--
 * @Author: yaowei
 * @Date: 2018-05-21 09:14:13
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-26 13:37:50
-->
<template>
  <div class="detail-wrap">
    <header>
      <h4 class="header_h4" v-if="Number(checkMsg) === 1">新增兑换活动</h4>
      <h4 class="header_h4" v-if="Number(checkMsg) === 2">查看兑换活动</h4>
      <h4 class="header_h4" v-if="Number(checkMsg) === 3">编辑兑换活动</h4>
      <el-button
        class="mini-back-btn btn-home"
        icon="el-icon-d-arrow-left"
        @click="handleBack"
        >返回兑换活动列表</el-button
      >
    </header>

    <div class="content">
      <el-form
        :model="detailData"
        :rules="rules"
        label-width="22%"
        label-position="right"
        ref="ruleForm"
      >
        <el-row>
          <el-col :span="18">
            <div class="tip-left">
              <p>
                <code>基本信息</code>
              </p>
            </div>
            <el-form-item label="兑换码名称:" prop="codeName">
              <el-input
                class="mini-input"
                v-model="detailData.codeName"
                placeholder="最多展示10个字"
                :disabled="isDisabled"
              />
              <el-button
                v-if="Number(checkMsg) !== 1"
                class="mini-search-btn copy-btn"
                icon="el-icon-plus"
                @click="handleCopy"
                >复制添加</el-button
              >
            </el-form-item>
            <el-form-item label="描述:">
              <el-input
                type="textarea"
                :rows="3"
                placeholder="仅内部可见，不展示到前台"
                v-model="detailData.codeDesc"
                :disabled="isDisabled"
              />
            </el-form-item>
            <el-form-item label="券码类型:" prop="type">
              <el-radio-group v-model="detailData.type" :disabled="isDisabled">
                <el-radio :label="1">专属码</el-radio>
                <!-- <el-radio :label="2">通用码</el-radio> -->
              </el-radio-group>
            </el-form-item>
            <!-- 生成实物卡为否 -->
            <template v-if="detailData.isEntity !== 1">
              <el-form-item
                label="生成方式:"
                prop="source"
                @change="clearVal"
                key="1"
              >
                <el-radio-group
                  v-model="detailData.source"
                  :disabled="isDisabled"
                >
                  <el-radio :label="1">系统生成</el-radio>
                  <el-radio :label="2">手动导入</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item
                label="生成数量:"
                prop="codeKey"
                v-if="detailData.source === 2"
              >
                <div class="import-wrap">
                  <el-input class="mini-input" v-model="moduleName" disabled />
                  <el-upload
                    class="upload-demo"
                    :headers="importHeaders"
                    :action="action + '?isEntity=0'"
                    :auto-upload="true"
                    :show-file-list="false"
                    :before-upload="beforeUpload"
                    :on-error="uploadFail"
                    :on-progress="onProgress"
                  >
                    <el-button
                      class="mini-search-btn"
                      icon="el-icon-plus"
                      :disabled="isDisabled"
                      >导入</el-button
                    >
                  </el-upload>
                  <span class="download" @click="handleDownload(5)"
                    >下载模板</span
                  >
                </div>
              </el-form-item>
            </template>
            <el-form-item
              label="生成数量:"
              prop="codeQuantity"
              v-if="
                (detailData.isEntity !== 1 && detailData.source === 1) ||
                (detailData.isEntity === 1 && detailData.cardType === 1)
              "
              key="2"
            >
              <el-input
                class="mini-input"
                placeholder="请输入活动兑换码生成数量，单次最大为10000个；电子卡请输入0，表示默认不限制"
                v-model.number="detailData.codeQuantity"
                :disabled="isDisabled || detailData.exchangeWay === 2"
              />
            </el-form-item>

            <el-form-item label="限用数量:" prop="limitQuantity">
              <el-input
                class="mini-input"
                placeholder="请输入单个用户使用的数量上限，默认为不限制"
                v-model.number="detailData.limitQuantity"
                :disabled="isDisabled"
              />
            </el-form-item>

            <el-form-item label="有效时间:" prop="validTime">
              <el-date-picker
                v-show="detailData.validFlag"
                v-model="validStart"
                type="datetime"
                placeholder="开始日期"
                value-format="timestamp"
                :picker-options="expireTimeOption"
                @change="handleStart"
                :disabled="isDisabled"
              ></el-date-picker>
              <el-date-picker
                v-show="!detailData.validFlag"
                v-model="detailData.validTime"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="timestamp"
                :picker-options="expireTimeOption"
                @change="handleDate"
                :disabled="isDisabled"
              ></el-date-picker>
              <el-checkbox style="margin-left: 30px" v-model="detailData.validFlag">无固定期限</el-checkbox>
            </el-form-item>

            <div class="tip-left">
              <p>
                <code>优惠规则</code>
              </p>
            </div>
            <el-form-item label="兑换形式:" prop="exchangeWay">
              <el-radio-group
                v-model="detailData.exchangeWay"
                :disabled="isDisabled"
              >
                <el-radio :label="1">兑换</el-radio>
                <el-radio :label="2">权益</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="使用条件:" prop="orderUseThreshold">
              <p class="order-amount">
                <span class="title">订单满额</span>
                <el-input
                  placeholder="请输入8位以下数字，最大为99999999，默认不限制"
                  v-model="detailData.orderUseThreshold"
                  :disabled="isDisabled"
                />
                <span>元</span>
              </p>
            </el-form-item>

            <div class="tip-left">
              <p>
                <code>其他设置</code>
              </p>
            </div>

            <!-- 兑换卡标识头 -->
            <el-form-item label="兑换卡标识头:" prop="headImg">
              <template>
                <el-upload
                  class="avatar-uploader"
                  :action="actionImg"
                  :show-file-list="false"
                  :on-success="handleSuccessHeadImg"
                  :before-upload="beforeUploadHeadImg"
                  :disabled="isDisabled"
                >
                  <img
                    v-if="detailData.headImg"
                    :src="detailData.headImg"
                    class="avatar"
                  />
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
              </template>
            </el-form-item>

            <!-- <el-form-item label="是否生成实物卡:">
              <el-radio-group
                v-model="detailData.isEntity"
                :disabled="true"
              >
                <el-radio :label="0">否</el-radio>
                <el-radio :label="1">是</el-radio>
              </el-radio-group>
            </el-form-item> -->
            <el-form-item label="兑换卡属性:">
              <el-radio-group
                v-model="detailData.isEntity"
                :disabled="isDisabled || canSelect"
              >
                <el-radio :label="0">电子卡</el-radio>
                <el-radio :label="1">实体卡</el-radio>
              </el-radio-group>
            </el-form-item>
            <!-- 生成实物卡 -->
            <template v-if="detailData.isEntity === 1">
              <el-form-item label="卡码设置:" prop="cardType" key="3">
                <el-radio-group
                  v-model="detailData.cardType"
                  @change="clearVal"
                  :disabled="true"
                >
                  <el-radio :label="1">系统生成</el-radio>
                  <el-radio :label="2">手动导入</el-radio>
                </el-radio-group>
                <span class="red ml-30"
                  >注：兑换码、暗码（卡片码）仅支持同一种生成模式（系统生成或手动导入）。</span
                >

                <!-- 手动导入 -->
                <div class="code-set-wrap" v-if="detailData.cardType === 2">
                  <el-upload
                    class="upload-demo"
                    ref="uploadFiles"
                    :headers="importHeaders"
                    :action="action + '?isEntity=1'"
                    :auto-upload="true"
                    :show-file-list="true"
                    :before-upload="beforeUpload"
                    :on-error="uploadFail"
                    :on-progress="onProgress"
                  >
                    <el-button
                      class="mini-search-btn"
                      icon="el-icon-plus"
                      :disabled="isDisabled"
                      >导入</el-button
                    >
                  </el-upload>
                  <span class="download" @click="handleDownload(6)"
                    >下载模板</span
                  >
                </div>
              </el-form-item>
              <!-- 系统生成 -->
              <template v-if="detailData.cardType === 1">
                <el-form-item label="卡片码生成规则:" prop="ruleType" key="4">
                  <el-radio-group
                    v-model="detailData.ruleType"
                    :disabled="true"
                  >
                    <el-radio :label="1">系统随机</el-radio>
                    <el-radio :label="2">按规则生成</el-radio>
                  </el-radio-group>
                </el-form-item>
                <!-- 系统随机 -->
                <template v-if="detailData.ruleType === 1">
                  <el-form-item label="随机位数:" prop="randomValue" key="5">
                    <el-input
                      class="mini-input"
                      placeholder="请输入6或者7"
                      v-model.number="detailData.randomValue"
                      :disabled="isDisabled"
                    />
                    <span>位</span>
                  </el-form-item>
                </template>

                <!-- 按规则生成 -->
                <template v-if="detailData.ruleType === 2">
                  <el-form-item label="抬头值设置:" prop="riseValue" key="6">
                    <el-input
                      class="mini-input"
                      placeholder="请输入1~16位大写英文字母或数字"
                      v-model="detailData.riseValue"
                      :disabled="isDisabled"
                    />
                    <p class="blue">
                      明码 = （抬头值（固定不变） + （浮动值）） -
                      （抬头值（固定不变）） + （浮动值 + 生成数量 - 1））；
                    </p>
                  </el-form-item>
                  <el-form-item label="浮动值设置:" prop="floatValue" key="7">
                    <el-input
                      class="mini-input"
                      placeholder="请输入1~16位数字"
                      v-model="detailData.floatValue"
                      :disabled="isDisabled"
                    />
                    <p class="blue">
                      举例：抬头值（BAT） +
                      浮动值（00001），生成数量10000，则明码为BAT0001-BAT10000；
                    </p>
                  </el-form-item>
                </template>
              </template>

              <el-form-item
                label="是否B2B线上推送工厂:"
                prop="isSyncFactory"
                key="8"
              >
                <el-radio-group
                  v-model="detailData.isSyncFactory"
                  :disabled="true"
                >
                  <el-radio :label="1">是</el-radio>
                  <el-radio :label="0">否</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="每盒装数量:" prop="boxNum" key="9">
                <el-input
                  class="mini-input"
                  placeholder="请输入每盒装数量"
                  v-model.number="detailData.boxNum"
                  :disabled="true"
                />
              </el-form-item>
            </template>
            <el-form-item label="是否使用兑换商城:">
              <el-radio-group
                v-model="detailData.isUseMall"
                @change="clearValidate"
                :disabled="isDisabled"
              >
                <el-radio :label="0">否</el-radio>
                <el-radio :label="1">是</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item
              label="兑换商城类型:"
              prop="mallType"
              v-if="detailData.isUseMall === 1"
            >
              <el-select
                v-model="detailData.mallType"
                placeholder="兑换商城类型"
                :disabled="isDisabled"
              >
                <el-option label="定制商城" :value="1"></el-option>
              </el-select>
              <span class="red ml-30">注：定制/普通商品需分开进行设置；</span>
            </el-form-item>
            <!-- 兑换材质设置 -->
            <add-material
              v-show="detailData.isUseMall === 1"
              ref="materialSetting"
              class="btn-wrap"
              :exchangeMaterial="exchangeMaterial"
              :isDisabled="curStatus == 4"
              :initMaterialItems="initMaterialItems"
              :editStatus="editStatus"
              :modelIdList="modelIdList"
              :pictureIdList="pictureIdList"
              @getMaterialData="getMaterialData"
              @getModelData="getModelData"
              @getPicData="getPicData"
            ></add-material>
            <!-- 定制型号权限 -->
            <el-form-item
              label="定制型号设置:"
              prop="modelUseType"
              v-if="detailData.isUseMall === 1"
            >
              <el-radio-group
                v-model="detailData.modelUseType"
                @change="clearValidate"
                :isDisabled="curStatus == 4"
              >
                <el-radio :label="1">全部定制型号</el-radio>
                <el-radio :label="2">指定定制型号</el-radio>
              </el-radio-group>
            </el-form-item>
            <add-model
              v-show="
                detailData.isUseMall === 1 && detailData.modelUseType === 2
              "
              ref="modelSetting"
              class="btn-wrap"
              :exchangeModel="exchangeModel"
              :isDisabled="curStatus == 4"
              :initModelItems="initModelItems"
              :editStatus="editStatus"
              :materialIdList="materialIdList"
              :pictureIdList="pictureIdList"
              @getModelData="getModelData"
              @getPicData="getPicData"
            ></add-model>
            <!-- 定制图片权限 -->
            <el-form-item
              label="定制图片设置:"
              prop="pictureUseType"
              v-if="detailData.isUseMall === 1"
            >
              <el-radio-group
                v-model="detailData.pictureUseType"
                @change="clearValidate"
                :isDisabled="curStatus == 4"
              >
                <el-radio :label="1">全部定制图片</el-radio>
                <el-radio :label="2">指定定制图片</el-radio>
              </el-radio-group>
            </el-form-item>
            <add-picture
              v-show="
                detailData.isUseMall === 1 && detailData.pictureUseType === 2
              "
              ref="pictureSetting"
              class="btn-wrap"
              :exchangePicture="exchangePicture"
              :isDisabled="curStatus == 4"
              :initPicItems="initPicItems"
              :editStatus="editStatus"
              :materialIdList="materialIdList"
              :modelIdList="modelIdList"
              :modelUseType="detailData.modelUseType"
              @getPicData="getPicData"
            ></add-picture>

            <!-- 适用分销商 -->
            <el-form-item
              label="适用分销商:"
              prop="distriData"
              v-if="detailData.isUseMall === 1"
            >
              <el-radio-group
                v-model="detailData.distributorScope"
                @change="clearValidate"
                :disabled="curStatus && Number(curStatus) !== 0"
              >
                <el-radio :label="1">全部分销商</el-radio>
                <el-radio :label="2">指定分销商</el-radio>
              </el-radio-group>
              <div v-if="detailData.distributorScope === 2">
                <el-button
                  class="mini-search-btn add-goods-btn"
                  @click="distributorShow = true"
                  >添加分销商</el-button
                >
                <el-table
                  v-if="distriData.length > 0"
                  :data="distriData"
                  header-row-class-name="header-row"
                  border
                  row-key="id"
                  class="tableCenter"
                >
                  <el-table-column
                    label="B2B编码"
                    align="center"
                    prop="distributorId"
                  ></el-table-column>
                  <el-table-column
                    label="分销商用户名"
                    align="center"
                    prop="distributorName"
                  ></el-table-column>
                  <el-table-column
                    label="公司名"
                    align="center"
                    prop="companyName"
                  ></el-table-column>
                  <el-table-column label="操作" align="center">
                    <template slot-scope="scope">
                      <el-button
                        class="mini-delete-btn"
                        @click="handleDeleteDistributor(scope.$index)"
                        >删除
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-form-item>
            <el-form-item v-if="detailData.distributorScope === 2">
              <div class="tips">
                <p>
                  注意：指定分销商，添加材质时，请确保你已按照以下流程创建了对应的80码和商品。
                </p>
                <p>1、在ERP新增一个材质80码，且该80码属性为：兑换卡。</p>
                <p>2、创建一个新的商品，并指定对应的分销商。</p>
              </div>
            </el-form-item>

            <!-- 兑换卡片关联：使用兑换商城且是实体卡 -->
            <add-card
              v-if="detailData.isUseMall === 1 && detailData.isEntity === 1"
              ref="cardSetting"
              class="btn-wrap"
              :exchangeCard="exchangeCard"
              :isDisabled="isDisabled"
              :initCardItems="initCardItems"
              :editStatus="editStatus"
              :distriStatus="detailData.distributorScope"
              :distributorId="detailData.distributorId"
              @getCardData="getCardData"
            ></add-card>
            <el-form-item
              label="兑换卡片型号:"
              prop="modelNo"
              v-if="detailData.isUseMall === 1"
              key="10"
            >
              <el-input
                class="mini-input"
                placeholder="请输入兑换卡片型号"
                v-model.number="detailData.modelNo"
                :disabled="isDisabled"
              />
            </el-form-item>

            <!-- 适用商品 -->
            <!-- <el-form-item
              label="适用商品:"
              prop="goodsScope"
              class="no-margin"
              v-show="detailData.isUseMall === 0"
            >
              <el-radio-group
                v-model="detailData.goodsScope"
                @change="clearValidate"
                :disabled="isDisabled"
              >
                <el-radio :label="1">全部商品可见</el-radio>
                <el-radio :label="2">指定商品可见</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label prop="goodsItems">
              <div
                class="pro-wrap"
                v-show="
                  detailData.isUseMall === 0 && detailData.goodsScope === 2
                "
              >
                <el-button
                  class="mini-search-btn"
                  @click="itemsShow = true"
                  v-if="!isDisabled"
                  >添加商品</el-button
                >
                <el-button
                  class="mini-delete-btn"
                  @click="handleDeleteChoose"
                  v-if="!isDisabled"
                  >批量删除</el-button
                >

                <el-table
                  v-if="goodsItems.length > 0"
                  :data="goodsItems"
                  ref="multipleGoodsTable"
                  border
                  header-row-class-name="header-row"
                  class="tableCenter goods-table"
                  style="width: 100%"
                  max-height="400"
                  @selection-change="handleSelectionChange"
                >
                  <el-table-column
                    align="center"
                    type="selection"
                    width="55"
                  ></el-table-column>
                  <el-table-column
                    align="center"
                    label="商品编码"
                    prop="goodsNo"
                  ></el-table-column>
                  <el-table-column
                    align="center"
                    show-overflow-tooltip
                    label="商品名称"
                    prop="goodsName"
                  ></el-table-column>
                  <el-table-column
                    width="80"
                    align="center"
                    label="操作"
                    v-if="!isDisabled"
                  >
                    <template slot-scope="scope">
                      <el-button
                        class="mini-delete-btn"
                        @click="handleDeleteGood(scope.$index)"
                        >删除</el-button
                      >
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-form-item> -->
            <!-- 邮费设置 -->
            <template
              v-if="!curStatus || (curStatus && detailData.mailSetting)"
            >
              <el-form-item label="邮费设置:" prop="mailSetting">
                <el-radio-group
                  v-model="detailData.mailSetting"
                  @change="clearValidate"
                  :disabled="isDisabled || canSelectMail"
                >
                  <el-radio :label="1">包邮</el-radio>
                  <el-radio :label="2">收运费（赠卡模式）</el-radio>
                  <!-- <el-radio :label="3">收运费（卖卡模式）</el-radio> -->
                </el-radio-group>

                <p class="blue" v-show="detailData.mailSetting === 1">
                  表示分销商从B2B前台购买兑换卡后，把卡片卖给给用户。用户在使用兑换卡兑换时，不需要额外支付运费。
                </p>
              </el-form-item>
              <el-form-item
                v-if="
                  detailData.mailSetting === 2 || detailData.mailSetting === 3
                "
                label="设置运费："
                prop="mailFee"
              >
                <el-input
                  class="mini-input"
                  placeholder="请输入大于0的8位以下数字，最大为99999999，不能为空"
                  v-model="detailData.mailFee"
                  :disabled="isDisabled"
                />
                <p class="blue" v-show="detailData.mailSetting === 2">
                  表示分销商以制卡成本购买到兑换卡，免费赠送给用户。用户在使用兑换卡兑换时，需要额外支付运费。
                </p>
                <p class="blue" v-show="detailData.mailSetting === 3">
                  表示分销商从B2B前台购买兑换卡后，把卡片卖给给用户。同时用户在使用兑换卡兑换时，需要额外支付运费。
                </p>
              </el-form-item>
            </template>

            <div class="tip-left">
              <p>
                <code>转赠配置</code>
              </p>
            </div>
            <el-form-item
              label="是否允许转赠:"
              prop="switchFlag"
            >
              <el-radio-group
                v-model="detailData.switchFlag"
              >
                <el-radio :label="0">否</el-radio>
                <el-radio :label="1">是</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="转赠文案:" prop="transferText">
              <el-input
                class="mini-input"
                v-model="detailData.transferText"
                placeholder="最多32个字"
              />
            </el-form-item>
            <!-- 转赠封面图 -->
            <el-form-item label="转赠封面图:" prop="transferImg">
              <template>
                <el-upload
                  class="avatar-uploader size1"
                  :action="actionImg"
                  :show-file-list="false"
                  :on-success="handleSuccessTransferImg"
                  :before-upload="beforeUploadTransferImg"
                >
                  <img
                    v-if="detailData.transferImg"
                    :src="detailData.transferImg"
                    class="avatar"
                  />
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
              </template>
              <p class="tips grey">
                图片尺寸要求：450px*360px，支持的图片格式：jpg/jpeg/png，图片大小不超1M
              </p>
            </el-form-item>
            <el-form-item label="收取页面文案:" prop="receiveText">
              <el-input
                class="mini-input"
                v-model="detailData.receiveText"
                placeholder="最多32个字"
              />
            </el-form-item>
            <!-- 收取卡片页面配图 -->
            <el-form-item label="收取卡片页面配图:" prop="receiveImg">
              <template>
                <el-upload
                  class="avatar-uploader size2"
                  :action="actionImg"
                  :show-file-list="false"
                  :on-success="handleSuccessReceiveImg"
                  :before-upload="beforeUploadReceiveImg"
                >
                  <img
                    v-if="detailData.receiveImg"
                    :src="detailData.receiveImg"
                    class="avatar"
                  />
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
              </template>
              <p class="tips grey">
                图片尺寸要求：494px*312px，支持的图片格式：jpg/jpeg/png，图片大小不超1M
              </p>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div class="clearfix footbtn">
        <!-- <el-button
          class="mini-freeze-btn box-btn"
          :loading="loading"
          v-if="!isDisabled"
          @click="handleSave('ruleForm')"
          >保存草稿</el-button
        > -->
        <el-button
          class="mini-search-btn box-btn"
          :loading="loading"
          v-if="curStatus != 4"
          @click="handleSubmit('ruleForm')"
          >保存提交</el-button
        >
        <el-button size="mini" @click="handleBack">返回</el-button>
      </div>
    </div>

    <el-dialog
      :modal-append-to-body="false"
      :visible="itemsShow"
      width="80%"
      :before-close="closeItemsDialog"
    >
      <select-item
        :goodsType="goodsType"
        :selectItemsData="goodsItems"
        :conFlag="'exchangeActivity'"
        ref="selectGoodItems"
        @cancel="itemsShow = false"
        @submit="getItemsData"
      ></select-item>
    </el-dialog>

    <!-- 添加分销商 -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="distributorShow"
      :before-close="disCancel"
      width="80%"
    >
      <select-distributor
        :distributorData="distriData"
        ref="selectDistributor"
        @disClose="disClose"
        @disSubmit="disSubmit"
      >
      </select-distributor>
    </el-dialog>
  </div>
</template>

<script>
import selectItem from "@/views/marketingPromotion/groupBuy/components/selectItem";
import addMaterial from "@/views/marketingPromotion/exchange/components/addMaterial";
import addModel from "@/views/marketingPromotion/exchange/components/addModel";
import addPicture from "@/views/marketingPromotion/exchange/components/addPicture";
import addCard from "@/views/marketingPromotion/exchange/components/addCard";
import selectDistributor from "@/views/storeLayout/rxComponents/selectDistributor";
import { getToken } from "@/utils/auth";
import {monthDay} from '@/utils/timeFormat.js'
export default {
  name: "exchangeDetail",
  components: {
    selectItem,
    addMaterial,
    addModel,
    addPicture,
    addCard,
    selectDistributor,
  },
  data() {
    // 指定商品验证
    var validateGoodsItem = (rule, value, callback) => {
      if (
        this.detailData.isUseMall === 0 &&
        this.detailData.goodsScope === 2 &&
        this.goodsItems.length <= 0
      ) {
        callback(new Error("请添加指定商品"));
      } else {
        callback();
      }
    };
    // 兑换材质验证
    var validateMaterial = (rule, value, callback) => {
      if (
        this.detailData.isUseMall === 1 &&
        this.detailData.materialIdList.length <= 0
      ) {
        callback(new Error("请设置兑换材质"));
      } else {
        callback();
      }
    };
    // 定制型号验证
    var validateModel = (rule, value, callback) => {
      if (
        this.detailData.isUseMall === 1 &&
        this.detailData.modelUseType === 2 &&
        this.detailData.modelIdList.length <= 0
      ) {
        callback(new Error("请设置指定定制型号"));
      } else {
        callback();
      }
    };
    // 定制图片验证
    var validatePic = (rule, value, callback) => {
      if (
        this.detailData.isUseMall === 1 &&
        this.detailData.pictureUseType === 2 &&
        this.detailData.pictureIdList.length <= 0
      ) {
        callback(new Error("请设置指定定制图片"));
      } else {
        callback();
      }
    };
    // 关联卡片验证
    var validateCard = (rule, value, callback) => {
      if (this.detailData.isUseMall === 1 && this.detailData.itemId === "") {
        callback(new Error("请关联兑换卡片"));
      } else {
        callback();
      }
    };

    // 适用分销商分销商
    var validateDistriData = (rule, value, callback) => {
      if (
        this.detailData.distributorScope === 2 &&
        this.distriData &&
        this.distriData.length <= 0
      ) {
        callback(new Error("请选择分销商"));
      } else {
        callback();
      }
    };

    return {
      actionImg: process.env.BASE_API + "system/v1/web/admin/oss/sts",
      loading: false,
      checkMsg: this.$route.query.checkMsg,
      id: this.$route.query.id,
      detailData: {
        codeName: "",
        codeDesc: "",
        type: 1,
        source: 1,
        codeQuantity: "",
        codeKey: "",
        limitQuantity: "",
        validTime: [],
        validFlag: false, // 无固定期限
        exchangeWay: 1,
        orderUseThreshold: "",
        distributorScope: 1, // 适用分销商：1 全部， 2 指定
        goodsScope: 1,
        headImg: "",
        isEntity: 1,
        cardType: 1,
        ruleType: 2,
        randomValue: "",
        isSyncFactory: 0,
        boxNum: 1,
        riseValue: "",
        floatValue: "",
        isUseMall: 1,
        mallType: 1,
        itemList: [],
        materialIdList: [],
        modelIdList: [],
        pictureIdList: [],
        itemId: "",
        modelNo: "",
        modelUseType: 1,
        pictureUseType: 1,
        distributorList: [],
        // 邮费设置
        mailSetting: 1,
        mailFee: "",
        // 转赠配置
        switchFlag: 1,
        transferText: "",
        transferImg: "",
        receiveText: "",
        receiveImg: "",
      },
      validStart: "", // 无固定期限，开始日期
      distriData: [],
      distributorShow: false,
      rules: {
        codeName: [
          {
            required: true,
            message: "请输入兑换活动名称",
            trigger: ["blur", "change"],
          },
          {
            validator: (rule, value, callback) => {
              if (value) {
                var str = value + "";
                var len = 0;
                for (var i = 0; i < str.length; i++) {
                  var c = str.charCodeAt(i);
                  // 单字节加1
                  if (
                    (c >= 0x0001 && c <= 0x007e) ||
                    (0xff60 <= c && c <= 0xff9f)
                  ) {
                    len++;
                  } else {
                    len += 2;
                  }
                }
                if (len > 20) {
                  callback(new Error("最多展示10个字，20个字符"));
                } else {
                  callback();
                }
              } else {
                callback();
              }
            },
          },
        ],
        type: [
          {
            required: true,
            message: "请选择券码类型",
            trigger: "change",
          },
        ],
        source: [
          {
            required: true,
            message: "请选择生成方式",
            trigger: "change",
          },
        ],
        codeQuantity: [
          {
            required: true,
            message: "请输入活动兑换码生成数量",
            trigger: ["blur", "change"],
          },
          {
            type: "number",
            message: "生成数量必须为数字",
          },
          {
            validator: (rule, value, callback) => {
              if (value && value > 10000) {
                callback(new Error("单次最大为10000个"));
              } else {
                callback();
              }
            },
          },
        ],
        limitQuantity: [
          {
            validator: (rule, value, callback) => {
              if (value) {
                if (!Number.isInteger(value)) {
                  callback(new Error("限用数量必须为数字"));
                } else {
                  callback();
                }
              } else {
                callback();
              }
            },
          },
        ],
        codeKey: [
          {
            required: true,
            message: "请导入兑换码",
            trigger: "change",
          },
        ],
        validTime: [
          {
            required: true,
            message: "请选择有效时间",
            trigger: "change",
          },
        ],
        exchangeWay: [
          {
            required: true,
            message: "请选择兑换形式",
            trigger: "change",
          },
        ],
        orderUseThreshold: [
          {
            required: true,
            message: "请输入使用条件",
            trigger: ["blur", "change"],
          },
          {
            validator: (rule, value, callback) => {
              var reg = /^([0-9]{0,8}|0)(\.[\d]{1,2})?$/;
              if (value) {
                if (!reg.test(value)) {
                  callback(new Error("请输入正确的使用条件"));
                } else if (value > 99999999) {
                  callback(new Error("最大值为99999999"));
                } else {
                  callback();
                }
              } else {
                callback();
              }
            },
          },
        ],
        headImg: [
          {
            required: true,
            message: "请上传兑换卡标识头",
            trigger: "blur",
          },
        ],
        goodsScope: [
          {
            required: true,
            message: "请选择适用商品",
            trigger: "change",
          },
        ],
        goodsItems: [
          {
            required: true,
            validator: validateGoodsItem,
            trigger: "change",
          },
        ],
        cardType: [
          {
            required: true,
            message: "请选择卡码设置",
            trigger: "change",
          },
        ],
        ruleType: [
          {
            required: true,
            message: "请选择卡片码生成规则",
            trigger: "change",
          },
        ],
        randomValue: [
          {
            required: true,
            validator: (rule, value, callback) => {
              if (value) {
                if (!Number.isInteger(value)) {
                  callback(new Error("随机位数必须为数字"));
                } else if (value < 6 || value > 7) {
                  callback(new Error("请输入6或者7"));
                } else {
                  callback();
                }
              } else {
                callback(new Error("请输入大于0的数字"));
              }
            },
            trigger: ["blur", "change"],
          },
        ],
        isSyncFactory: [
          {
            required: true,
            message: "请选择是否B2B线上推送工厂",
            trigger: "change",
          },
        ],
        boxNum: [
          {
            required: true,
            validator: (rule, value, callback) => {
              if (value) {
                if (!Number.isInteger(value)) {
                  callback(new Error("每盒装数量必须为数字"));
                } else {
                  callback();
                }
              } else {
                callback(new Error("请输入大于0的数字"));
              }
            },
            trigger: ["blur", "change"],
          },
        ],
        riseValue: [
          {
            required: true,
            message: "请设置抬头值",
            trigger: ["blur", "change"],
          },
          {
            validator: (rule, value, callback) => {
              var reg = /^[A-Z0-9]{1,16}$/;
              if (value) {
                if (!reg.test(value)) {
                  callback(new Error("请输入1~16位大写英文字母或数字"));
                } else {
                  callback();
                }
              } else {
                callback();
              }
            },
          },
        ],
        floatValue: [
          {
            required: true,
            message: "请设置浮动值",
            trigger: ["blur", "change"],
          },
          {
            validator: (rule, value, callback) => {
              if (value) {
                var valStr = value + "";
                if (value != 0 && !Number(value)) {
                  callback(new Error("浮动值必须为数字"));
                } else if (valStr.length < 1 || valStr.length > 16) {
                  callback(new Error("请输入1~16位数字"));
                } else {
                  callback();
                }
              } else {
                callback();
              }
            },
          },
        ],
        mallType: [
          {
            required: true,
            message: "请选择兑换商城类型",
            trigger: "change",
          },
        ],
        exchangeMaterial: [
          {
            required: true,
            validator: validateMaterial,
            trigger: "change",
          },
        ],
        exchangeModel: [
          {
            required: true,
            validator: validateModel,
            trigger: "change",
          },
        ],
        exchangePicture: [
          {
            required: true,
            validator: validatePic,
            trigger: "change",
          },
        ],
        exchangeCard: [
          {
            required: true,
            validator: validateCard,
            trigger: "change",
          },
        ],
        modelNo: [
          {
            required: true,
            message: "请输入兑换卡片型号",
            trigger: ["blur", "change"],
          },
        ],
        modelUseType: [
          {
            required: true,
            message: "请设置定制型号",
            trigger: "change",
          },
        ],
        pictureUseType: [
          {
            required: true,
            message: "请设置定制图片",
            trigger: "change",
          },
        ],
        distriData: [
          {
            required: true,
            validator: validateDistriData,
            trigger: "change",
          },
        ],
        // 邮费设置
        mailSetting: [
          {
            required: true,
            message: "请设置邮费",
            trigger: "change",
          },
        ],
        mailFee: [
          {
            required: true,
            message: "请设置邮费",
            trigger: ["change", "blur"],
          },
          {
            validator: (rule, value, callback) => {
              var valStr = value + "";
              if (
                (Number(value) === 0 && valStr.length > 0) ||
                valStr.length < 1 ||
                valStr.length > 8
              ) {
                callback(new Error("请输入大于0的8位以下数字，最大为99999999"));
              } else {
                callback();
              }
            },
          },
        ],
        // 转赠配置
        switchFlag: [
          {
            required: true,
            message: "请选择是否允许转赠",
            trigger: "change",
          },
        ],
        transferText: [
          {
            required: true,
            message: "请输入转赠文案",
            trigger: ["blur", "change"],
          },
          {
            validator: (rule, value, callback) => {
              if (value) {
                var str = value + "";
                var len = 0;
                for (var i = 0; i < str.length; i++) {
                  var c = str.charCodeAt(i);
                  // 单字节加1
                  if (
                    (c >= 0x0001 && c <= 0x007e) ||
                    (0xff60 <= c && c <= 0xff9f)
                  ) {
                    len++;
                  } else {
                    len += 2;
                  }
                }
                if (len > 64) {
                  callback(new Error("最多展示32个字，64个字符"));
                } else {
                  callback();
                }
              } else {
                callback();
              }
            },
          },
        ],
        transferImg: [
          {
            required: true,
            message: "请上传转赠封面图",
            trigger: "blur",
          },
        ],
        receiveText: [
          {
            required: true,
            message: "请输入收取页面文案",
            trigger: ["blur", "change"],
          },
          {
            validator: (rule, value, callback) => {
              if (value) {
                var str = value + "";
                var len = 0;
                for (var i = 0; i < str.length; i++) {
                  var c = str.charCodeAt(i);
                  // 单字节加1
                  if (
                    (c >= 0x0001 && c <= 0x007e) ||
                    (0xff60 <= c && c <= 0xff9f)
                  ) {
                    len++;
                  } else {
                    len += 2;
                  }
                }
                if (len > 64) {
                  callback(new Error("最多展示32个字，64个字符"));
                } else {
                  callback();
                }
              } else {
                callback();
              }
            },
          },
        ],
        receiveImg: [
          {
            required: true,
            message: "请上传收取卡片页面配图",
            trigger: "blur",
          },
        ],
      },
      isDisabled: false, // 是否可编辑
      expireTimeOption: {
        // 限制可选日期
        disabledDate(date) {
          return date.getTime() < Date.now() - 24 * 60 * 60 * 1000;
        },
      },
      goodsType: 0,
      goodsItems: [],
      itemsShow: false,
      multipleSelection: [],
      exchangeMaterial: [],
      exchangeModel: [],
      exchangePicture: [],
      exchangeCard: [],
      materialIdList: [],
      modelIdList: [],
      pictureIdList: [],
      // 查看
      editStatus: false,
      initMaterialItems: [],
      initModelItems: [],
      initPicItems: [],
      initCardItems: [],
      // 上传
      processLoading: "",
      importHeaders: {
        Accept: "application/json",
        enctype: "multipart/form-data",
        Platform: "web",
        Version: "1.0.0",
        Authorization: "",
      },
      action: process.env.BASE_API + "admin/u/p/exchangeCode/import",
      moduleName: "",
      codeModuleName: "",
      curStatus: "", // 活动状态
      isCopy: false, // 是否是复制添加
      canSelectMail: false, // 邮费设置（选择电子卡且非权益，不能选择包邮）
      canSelect: false, // 兑换卡属性（选择权益，只能选择电子卡）
      hostname: "",
      codeQuantity: "", // 生成数量（暂存）
    };
  },
  methods: {
    // 返回兑换码列表
    handleBack() {
      this.clickLeave();
    },
    // 页面跳转
    clickLeave() {
      this.$router.go(-1);
    },
    // 重置表单验证
    resetValidate(formName) {
      this.$refs[formName].resetFields();
    },
    // 监听时间选择
    handleStart(value) {
      this.validStart = value;
      // 无固定期限
      this.detailData.validTime = [value, "32503478400000"];
      this.$forceUpdate();
    },
    handleDate(value) {
      this.detailData.validTime = value;
      this.$forceUpdate();
    },

    // 指定商品
    closeItemsDialog() {
      this.$refs.selectGoodItems.handleCancel();
    },
    getItemsData(val) {
      this.goodsItems = [];
      this.goodsItems = this.goodsItems.concat(val);
      this.itemsShow = false;
    },
    // 删除指定商品
    handleDeleteGood(index) {
      this.goodsItems.splice(index, 1);
    },
    // 指定商品选择
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 指定商品 - 批量删除
    handleDeleteChoose() {
      if (this.multipleSelection) {
        this.multipleSelection.forEach((item1, index2) => {
          this.goodsItems.forEach((item2, index2) => {
            if (item1 === item2) {
              this.goodsItems.splice(index2, 1);
            }
          });
        });
      }
      this.$refs.multipleGoodsTable.clearSelection();
    },
    // getSysConf() {
    //   this.$api.get(this, "/admin/u/po/systemConfig").then((res) => {
    //     if (res.config.customizedAttendEventFlag === 1) {
    //       this.goodsType = 0;
    //     } else {
    //       this.goodsType = 1;
    //     }
    //   });
    // },

    // 保存草稿
    handleSave(formName) {
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false;
        } else {
          this.handleAdd();
        }
      });
    },
    // 保存提交
    handleSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false;
        } else {
          this.handleAdd();
        }
      });
    },
    // 新增/编辑活动
    handleAdd() {
      this.detailData.startTime = this.detailData.validTime[0];
      this.detailData.endTime = this.detailData.validTime[1];

      // 编辑状态，设置id
      if (Number(this.checkMsg) === 3) {
        this.detailData.id = this.id;
      }

      // 复制添加，id设为空
      if (this.isCopy) {
        this.detailData.id = "";
      }

      // 适用商品
      var itemArr = [];
      if (
        this.detailData.isUseMall === 0 &&
        this.detailData.goodsScope === 2 &&
        this.goodsItems.length > 0
      ) {
        this.goodsItems.forEach((item) => {
          var options = {
            goodsId: item.goodsId,
            itemId: item.itemId,
          };
          itemArr.push(options);
        });
      }
      this.detailData.itemList = itemArr;

      if (this.detailData.modelUseType === 1) {
        this.detailData.modelIdList = [];
      }
      if (this.detailData.pictureUseType === 1) {
        this.detailData.pictureIdList = [];
      }

      // 转赠配置
      this.detailData.transfer = {
        switchFlag: this.detailData.switchFlag,
        transferText: this.detailData.transferText,
        transferImg: this.detailData.transferImg,
        receiveText: this.detailData.receiveText,
        receiveImg: this.detailData.receiveImg,
      };
      if (Number(this.checkMsg) === 1 || this.isCopy) {
        // 新增
        this.$http.addExchangeCard(this, this.detailData).then((res) => {
          if (res.success) {
            this.$message({
              type: "success",
              message: "新增成功",
            });
            this.clickLeave();
          }
        });
      } else if (Number(this.curStatus) !== 4) {
        this.detailData.transfer.id = this.detailData.exchangeCardTransferId;
        if (this.detailData.codeKey === "编辑") {
          this.detailData.codeKey = "";
        }
        // 编辑
        this.$http.editExchangeCard(this, this.detailData).then((res) => {
          if (res.success) {
            this.$message({
              type: "success",
              message: "编辑成功",
            });
            this.clickLeave();
          } else if (Number(res.code) === 10112) {
            // 需二次确认
            this.$confirm(
              res.msg +
                "；<br/><span class='red'>若点击确定，将会覆盖该卡片对应的其他兑换卡活动（已发布、进行中、暂停中）的材质、型号、图片</span>",
              "提示",
              {
                dangerouslyUseHTMLString: true,
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
                center: true,
              }
            )
              .then((_) => {
                this.$set(this.detailData, "isConfirm", 1);
                this.$http
                  .editExchangeCard(this, this.detailData)
                  .then((res) => {
                    if (res.success) {
                      this.$message({
                        type: "success",
                        message: "编辑成功",
                      });
                      this.clickLeave();
                    }
                  });
              })
              .catch((_) => {
                this.$message({
                  type: "info",
                  message: "已取消操作",
                });
              });
          }
        });
      }
    },
    // 清除验证提示
    clearValidate() {
      if (Number(this.distriStatus) === 1) {
        this.detailData.distributorId = "";
        this.detailData.distributorList = [];
      }

      this.$refs["ruleForm"].clearValidate(["goodsItems"]);
    },
    // 获取兑换材质
    getMaterialData(val) {
      this.materialIdList = [];
      if (val && val.length !== 0) {
        val.forEach((item) => {
          this.materialIdList.push(item.id);
        });
      }
      this.detailData.materialIdList = this.materialIdList;
      this.$refs["ruleForm"].clearValidate(["exchangeMaterial"]);
    },
    // 获取定制型号
    getModelData(val) {
      this.modelIdList = [];
      this.detailData.modelIdList = [];
      if (val && val.length !== 0) {
        val.forEach((item) => {
          this.modelIdList.push(item.id);
          this.detailData.modelIdList.push(item.id);
        });
      } else {
        // 清空定制型号/定制图片
        this.$nextTick(() => {
          if (this.$refs.modelSetting.modelItems !== undefined) {
            this.$refs.modelSetting.modelItems = [];
          }
          if (this.$refs.pictureSetting.picItems !== undefined) {
            this.$refs.pictureSetting.picItems = [];
          }
        });
      }
      this.$refs["ruleForm"].clearValidate(["exchangeModel"]);
    },
    // 获取定制图片
    getPicData(val) {
      this.pictureIdList = [];
      this.detailData.pictureIdList = [];
      if (val && val.length !== 0) {
        val.forEach((item) => {
          this.pictureIdList.push(item.id);
          this.detailData.pictureIdList.push(item.id);
        });
      } else {
        // 清空定制图片
        this.$nextTick(() => {
          this.$refs.pictureSetting.picItems = [];
        });
      }
      this.$refs["ruleForm"].clearValidate(["exchangePicture"]);
    },
    // 关联卡片
    getCardData(val) {
      if (val && val.length !== 0) {
        val.forEach((item) => {
          this.detailData.itemId = item.itemId;
        });
      }
      this.$refs["ruleForm"].clearValidate(["exchangeCard"]);
    },
    // 查看详情
    initDetail(id) {
      this.$http
        .exchangeCardDetail(this, {
          id: id,
        })
        .then((res) => {
          if (res.success) {
            this.detailData = res.data;
            this.detailData.codeKey = "编辑";
            this.editStatus = true;
            // 有效时间
            if (res.data.endTime == "32503478400000") {
              // 无固定期限
              this.validStart = new Date(res.data.startTime).getTime();
              this.detailData.validFlag = true;
            } else {
              // 有结束日期
              this.detailData.validTime = [
                new Date(res.data.startTime).getTime(),
                new Date(res.data.endTime).getTime(),
              ];
            }
            

            // 兑换材质
            if (
              res.data.materialPageBeanList &&
              res.data.materialPageBeanList.length > 0
            ) {
              this.initMaterialItems = res.data.materialPageBeanList;
              this.materialIdList = [];
              this.initMaterialItems.forEach((item) => {
                this.materialIdList.push(item.materialId);
              });
              this.detailData.materialIdList = this.materialIdList;
            }
            // 定制型号
            if (res.data.modelBeanList && res.data.modelBeanList.length > 0) {
              this.initModelItems = res.data.modelBeanList;
              this.modelIdList = [];
              this.initModelItems.forEach((item) => {
                this.modelIdList.push(item.modelId);
              });
              this.detailData.modelIdList = this.modelIdList;
            }
            // 定制图片
            if (
              res.data.pictureBeanList &&
              res.data.pictureBeanList.length > 0
            ) {
              this.initPicItems = res.data.pictureBeanList;
              this.pictureIdList = [];
              this.initPicItems.forEach((item) => {
                this.pictureIdList.push(item.pictureId);
              });
              this.detailData.pictureIdList = this.pictureIdList;
            }
            // 关联卡片
            if (
              res.data.chooseCardItemList &&
              res.data.chooseCardItemList.length > 0
            ) {
              this.initCardItems = res.data.chooseCardItemList;
              this.initCardItems.forEach((item) => {
                this.detailData.itemId = item.itemId;
              });
            }
            // 指定商品
            // if (
            //   res.exchangeCardDetailDTO.itemList &&
            //   res.exchangeCardDetailDTO.itemList !== []
            // ) {
            //   this.goodsItems = [];
            //   this.goodsItems = this.goodsItems.concat(
            //     res.exchangeCardDetailDTO.itemList
            //   );
            // }

            // 适用分销商
            if (res.data.distributorScope === 2) {
              let distributor = res.data.distributorList[0];
              if (distributor.id) {
                this.detailData.distributorScope = 2;
                let obj = {
                  distributorId: distributor.distributorId,
                  distributorName: distributor.distributorName,
                  companyName: distributor.distributorCompanyName,
                };
                this.detailData.distributorId = distributor.distributorId;
                this.distriData = [].concat(obj);
              }
            } else {
              this.distriData = [];
            }
          }
        });
    },
    // 下载模板
    handleDownload(type) {
      // type：5 虚拟卡，6 实体卡
      var wordName = "";
      switch (type) {
        case 5:
          wordName = "兑换码模板";
          break;
        case 6:
          wordName = "兑换码&卡片码模板";
          break;
        default:
          break;
      }
      this.$http
        .exchangeTempDownLoad(this, {
          type: type,
        })
        .then((res) => {
          const content = res;
          let blob = new Blob([content], {
            type: "application/ms-excel",
          });
          let url = window.URL.createObjectURL(blob);
          if ("download" in document.createElement("a")) {
            let link = document.createElement("a");
            link.style.display = "none";
            link.href = url;
            link.setAttribute("download", wordName + ".xls");
            document.body.appendChild(link);
            link.click();
          } else {
            navigator.msSaveBlob(blob, wordName + ".xls");
          }
        });
    },
    // 导入
    beforeUpload(file) {
      //上传前配置
      let excelfileExtend = ".xls,.xlsx"; //设置文件格式
      let fileExtend = file.name
        .substring(file.name.lastIndexOf("."))
        .toLowerCase();
      if (excelfileExtend.indexOf(fileExtend) <= -1) {
        this.$message.error("只能上传.xls,.xlsx格式");
        return false;
      }
      this.$http.exchangeImport(this, { isEntity: 0 }).then((res) => {
        if (res.success) {
          if (this.detailData.cardType === 2) {
            var uploadFiles = this.$refs.uploadFiles.uploadFiles;
            if (uploadFiles.length > 1) {
              uploadFiles.splice(0, 1);
            }
          }
          //上传成功
          if (res.success) {
            this.$message.success({
              message: "上传成功",
              duration: 3 * 1000,
              onClose: () => {},
            });
            this.detailData.codeKey = res.data;
            // 虚拟卡
            if (this.detailData.source === 2) {
              this.moduleName = file.name;
            }
            // 实体卡
            if (this.detailData.cardType === 2) {
              this.codeModuleName = file.name;
            }
            this.processLoading.close();
          } else {
            this.$message.error({
              message: res.errMessage,
              duration: 3 * 1000,
              onClose: () => {},
            });
            this.processLoading.close();
          }
        }
      });
    },
    onProgress(event, file, fileList) {
      // 上传
      this.processLoading = this.$loading({
        lock: true,
        text: "文件上传中....",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
    },
    uploadFail(err, file, fileList) {
      //上传错误
      this.$message.error({
        message: err.msg,
        duration: 3 * 1000,
        onClose: () => {},
      });
      this.processLoading.close();
    },
    clearVal() {
      // 虚拟卡
      if (this.detailData.source !== 2) {
        this.detailData.codeKey = "";
      }
      // 实体卡
      if (this.detailData.cardType !== 2) {
        this.detailData.codeKey = "";
      }
    },
    // 复制添加
    handleCopy() {
      this.isDisabled = false;
      this.isCopy = true;
      this.$message.success("复制成功");
    },
    // 兑换卡标识头
    handleSuccessHeadImg(response, file) {},
    beforeUploadHeadImg(file) {
      if (
        file.type != "image/bmp" &&
        file.type != "image/jpg" &&
        file.type != "image/jpeg" &&
        file.type != "image/png" &&
        file.type != "image/gif"
      ) {
        this.$message.error("上传图片只能是bmp、jpg、jpeg、png、gif格式!");
        return false;
      }
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error("上传图片大小不能超过 2MB!");
        return isLt2M;
      }
      // 随机命名
      let random_name =
        this.random_string(6) +
        "_" +
        new Date().getTime() +
        "." +
        file.name.split(".").pop();
      this.$http.getFileSts(this).then((result) => {
        const client = new OSS.Wrapper({
          region: result.data.region,
          accessKeyId: result.data.accessKeyId,
          accessKeySecret: result.data.accessKeySecret,
          stsToken: result.data.securityToken,
          bucket: result.data.bucketName,
          endpoint: result.data.endpoint,
          secure: true,
        });
        this.hostname = result.data.hostname;
        // 上传
        client
          .multipartUpload('flexible/other/' + monthDay(new Date()) + '/' + random_name, file, {})
          .then((results) => {
            return new Promise((resolve, reject) => {
              this.$set(
                this.detailData,
                "headImg",
                this.hostname + results.name
              ),
                resolve(true);
            });
          })
          .catch((err) => {
            console.log(err);
          });
      });
    },
    // 随机生成文件名
    random_string(len) {
      len = len || 32;
      var chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678";
      var maxPos = chars.length;
      var pwd = "";
      for (let i = 0; i < len; i++) {
        pwd += chars.charAt(Math.floor(Math.random() * maxPos));
      }
      return pwd;
    },
    // 选择分销商
    disSubmit(msg) {
      if (msg.length > 1) {
        this.$message({
          type: "warning",
          message: "只能选择一个分销商！",
        });
      } else {
        this.distriData = msg;
        this.detailData.distributorId = this.distriData[0].distributorId;
        this.detailData.distributorList = [
          {
            distributorCompanyName: this.distriData[0].companyName,
            distributorId: this.distriData[0].distributorId,
            distributorName: this.distriData[0].distributorName,
          },
        ];
        this.distributorShow = false;
        this.clearValidate();
      }
    },
    disCancel() {
      this.$refs.selectDistributor.handleCancel();
    },
    disClose() {
      this.distributorShow = false;
    },
    handleDeleteDistributor(index) {
      this.distriData.splice(index, 1);
    },
    // 转赠封面图
    handleSuccessTransferImg(response, file) {},
    beforeUploadTransferImg(file) {
      if (
        file.type != "image/jpg" &&
        file.type != "image/jpeg" &&
        file.type != "image/png"
      ) {
        this.$message.error("上传图片只能是bmp、jpg、jpeg、png、gif格式!");
        return false;
      }
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error("上传图片大小不能超过 2MB!");
        return isLt2M;
      }
      // 随机命名
      let random_name =
        this.random_string(6) +
        "_" +
        new Date().getTime() +
        "." +
        file.name.split(".").pop();
      this.$http.getFileSts(this).then((result) => {
        const client = new OSS.Wrapper({
          region: result.data.region,
          accessKeyId: result.data.accessKeyId,
          accessKeySecret: result.data.accessKeySecret,
          stsToken: result.data.securityToken,
          bucket: result.data.bucketName,
          endpoint: result.data.endpoint,
          secure: true,
        });
        this.hostname = result.data.hostname;
        // 上传
        client
          .multipartUpload('flexible/other/' + monthDay(new Date()) + '/' + random_name, file, {})
          .then((results) => {
            return new Promise((resolve, reject) => {
              this.$set(
                this.detailData,
                "transferImg",
                this.hostname + results.name
              ),
                resolve(true);
            });
          })
          .catch((err) => {
            console.log(err);
          });
      });
    },
    // 收取卡片页面配图
    handleSuccessReceiveImg(response, file) {},
    beforeUploadReceiveImg(file) {
      if (
        file.type != "image/jpg" &&
        file.type != "image/jpeg" &&
        file.type != "image/png"
      ) {
        this.$message.error("上传图片只能是bmp、jpg、jpeg、png、gif格式!");
        return false;
      }
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error("上传图片大小不能超过 2MB!");
        return isLt2M;
      }
      // 随机命名
      let random_name =
        this.random_string(6) +
        "_" +
        new Date().getTime() +
        "." +
        file.name.split(".").pop();
      this.$http.getFileSts(this).then((result) => {
        const client = new OSS.Wrapper({
          region: result.data.region,
          accessKeyId: result.data.accessKeyId,
          accessKeySecret: result.data.accessKeySecret,
          stsToken: result.data.securityToken,
          bucket: result.data.bucketName,
          endpoint: result.data.endpoint,
          secure: true,
        });
        this.hostname = result.data.hostname;
        // 上传
        client
          .multipartUpload('flexible/other/' + monthDay(new Date()) + '/' + random_name, file, {})
          .then((results) => {
            return new Promise((resolve, reject) => {
              this.$set(
                this.detailData,
                "receiveImg",
                this.hostname + results.name
              ),
                resolve(true);
            });
          })
          .catch((err) => {
            console.log(err);
          });
      });
    },
  },
  created() {
    this.importHeaders.Authorization = getToken();

    this.checkMsg = this.$route.query.checkMsg;
    this.id = this.$route.query.id;
    this.curStatus = this.$route.query.status;
    if (Number(this.checkMsg) === 2) {
      this.isDisabled = true;
    }
    if (Number(this.checkMsg) === 2 || Number(this.checkMsg) === 3) {
      this.initDetail(Number(this.id));
    }
    // this.getSysConf();
  },
  watch: {
    // 兑换卡属性
    "detailData.isEntity": {
      handler(newVal, oldVal) {
        if (newVal === 0 && this.detailData.exchangeWay === 1) {
          // 电子卡且兑换，邮费设置只能选择：收邮费（赠卡模式）
          this.$set(this.detailData, "mailSetting", 2);
          this.canSelectMail = true;
        } else {
          // 实体卡
          this.canSelectMail = false;
        }
      },
      deep: true,
    },
    // 兑换形式
    "detailData.exchangeWay": {
      handler(newVal, oldVal) {
        if (newVal === 2) {
          // 权益，只能选择电子卡
          this.$set(this.detailData, "isEntity", 0);
          this.canSelect = true;
          this.canSelectMail = false;
          this.codeQuantity = this.detailData.codeQuantity;
          this.detailData.codeQuantity = 0;
        } else {
          // 兑换
          if (this.detailData.isEntity === 0) {
            // 电子卡，邮费设置只能选择：收邮费（赠卡模式）
            this.$set(this.detailData, "mailSetting", 2);
            this.canSelectMail = true;
          }
          this.canSelect = false;
          this.detailData.codeQuantity = this.codeQuantity ? this.codeQuantity : "";
        }
      },
      deep: true,
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.detail-wrap {
  background-color: #fff;
  position: relative;

  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;

    .header_h4 {
      margin: 0 0 0 30px;
    }

    h4 {
      margin-left: 30px;
      display: inline-block;
      font-weight: 400;
    }

    .btn-home {
      float: right;
      padding: 5px;
      margin-top: 7px;
      margin-right: 8px;
      margin-left: 0;
    }
  }

  .content {
    padding-top: 30px;
    min-width: 1200px;

    .mini-input {
      width: 560px;
    }

    .copy-btn {
      margin-left: 8px;
    }

    .upload-demo {
      display: inline-block;
    }

    .import-wrap {
      .mini-search-btn {
        margin: 0 20px;
        height: 40px;
        font-size: 14px;
      }
    }

    .order-amount {
      position: relative;
      margin: 0;
      padding: 0 20px 0 65px;
      width: 450px;

      span {
        position: absolute;
        right: 0;
        color: #606266;
      }

      .title {
        left: 0;
      }
    }

    .download {
      cursor: pointer;
      color: #21b8cb;

      &:hover {
        opacity: 0.6;
      }
    }

    .red {
      color: #e8574f;
    }

    .blue {
      color: #21b8cb;
    }

    .ml-30 {
      margin-left: 30px;
    }

    .no-margin {
      margin: 0;
    }

    .code-set-wrap {
      /deep/ .upload-demo {
        .el-upload-list {
          position: absolute;
          top: 40px;
          left: 200px;
        }
      }

      .download {
        margin-left: 20px;
      }
    }

    .footbtn {
      padding-bottom: 30px;
      text-align: center;

      .box-btn + .box-btn {
        margin-left: 10px;
      }
    }
  }
}

.avatar-uploader {
  /deep/ .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;

    &:hover {
      border-color: rgb(33, 184, 203);
    }
  }

  &.size1 {
    .avatar-uploader-icon {
      width: 225px;
      height: 180px;
      line-height: 180px;
    }

    .avatar {
      width: 225px;
      height: 180px;
    }
  }

  &.size2 {
    .avatar-uploader-icon {
      width: 247px;
      height: 156px;
      line-height: 156px;
    }

    .avatar {
      width: 247px;
      height: 156px;
    }
  }
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 270px;
  height: 90px;
  line-height: 90px;
  text-align: center;
}

.avatar {
  width: 270px;
  height: 90px;
  display: block;
}

.tips {
  padding: 10px 0;
  color: #e8574f;
  line-height: 20px;

  &.grey {
    padding-top: 0;
    color: #666666;
  }
}
</style>
<style lang="scss">
.red {
  color: #e8574f;
}
</style>
