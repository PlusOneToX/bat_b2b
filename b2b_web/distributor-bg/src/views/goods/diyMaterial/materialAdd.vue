<template>
  <div class="add-productline">
    <header v-if="append">
      <h4>添加材料</h4>
      <el-button
        class="mini-back-btn btn-home"
        icon="el-icon-d-arrow-left"
        @click="handleCancel"
      >
        返回列表
      </el-button>
    </header>
    <header v-if="looking">
      <h4>查看材料</h4>
      <el-button
        class="btn-home"
        icon="el-icon-d-arrow-left"
        @click="handleCancel"
      >
        返回列表
      </el-button>
    </header>

    <el-form
      label-width="150px"
      :rules="rules"
      ref="formData"
      :model="formData"
      v-loading="vloading"
    >
      <el-form-item
        label="是否最终材质"
        prop="atLastTrademark"
        style="margin-bottom: 10px"
      >
        <el-tooltip
          class="item"
          effect="dark"
          content="选择是，不可选上级材料分类，选择否，必选上级材料分类"
          placement="right"
        >
          <el-radio-group
            v-model="formData.atLastTrademark"
            @change="changeCategory"
            size="mini"
          >
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-tooltip>
      </el-form-item>
      <el-form-item
        label="关联货品："
        style="margin-bottom: 0px"
        v-if="formData.atLastTrademark === 1"
      >
        <el-button @click="assignGoods" class="mini-search-btn"
          ><i class="el-icon-plus el-icon--left"></i>导入货品
        </el-button>
      </el-form-item>
      <el-form-item v-if="formData.atLastTrademark === 1">
        <el-table
          :data="goodsItems"
          border
          header-row-class-name="header-row"
          class="tableCenter goods-table"
          style="width: 80%"
          max-height="400"
        >
          <el-table-column align="center" label="货品编码" prop="itemCode">
          </el-table-column>
          <el-table-column align="center" label="货品名称" prop="itemName">
          </el-table-column>
          <!-- <el-table-column align="center" label="品类" prop="itemName"> </el-table-column> -->
          <!-- <el-table-column align="center" label="默认销售价格" prop="salePrice">
						<template slot-scope="scope">
							<div>￥:{{scope.row.salePrice}}</div>
						</template>
					</el-table-column> -->
          <el-table-column
            align="center"
            label="上架状态"
            prop="saleStatus"
            :formatter="formatSaleStatus"
          >
          </el-table-column>
          <el-table-column align="center" key="8" label="操作" width="150">
            <template slot-scope="scope">
              <el-button
                class="mini-delete-btn"
                @click="handleDeleteGoodsItems(scope.$index)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
      <el-form-item label="材料名称：" prop="name" style="margin-bottom: 10px">
        <el-input
          size="mini"
          style="width: 300px"
          v-model="formData.name"
          placeholder="不超过40个字"
          maxlength="40"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="材料英文名称："
        prop="englishName"
        style="margin-bottom: 10px"
      >
        <el-input
          size="mini"
          style="width: 500px"
          v-model="formData.englishName"
          placeholder="不超过80个字"
          maxlength="80"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="材料术语："
        prop="stuffName"
        style="margin-bottom: 10px"
      >
        <el-tooltip content="用于定制标签材质中文展示" placement="right">
          <el-input
            size="mini"
            style="width: 300px"
            v-model="formData.stuffName"
            placeholder="不超过40个字"
            maxlength="40"
          ></el-input>
        </el-tooltip>
      </el-form-item>
      <el-form-item
        label="材料英文术语："
        prop="stuffEnName"
        style="margin-bottom: 10px"
      >
        <el-tooltip content="用于定制标签材质英文展示" placement="right">
          <el-input
            size="mini"
            style="width: 500px"
            v-model="formData.stuffEnName"
            placeholder="不超过80个字"
            maxlength="80"
          ></el-input>
        </el-tooltip>
      </el-form-item>
      <el-form-item
        label="材料类型："
        prop="categoryId"
        style="margin-bottom: 10px"
      >
        <el-select
          v-model="formData.categoryId"
          size="mini"
          :disabled="looking"
          @change="selectCategory"
        >
          <el-option
            :label="item.name"
            :value="item.id"
            :key="item.id"
            v-for="item in categorys"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        label="第三方工厂："
        prop="manufactor"
        style="margin-bottom: 10px"
      >
        <el-select
          v-model="formData.manufactor"
          size="mini"
          @change="selectFactory"
        >
          <el-option
            :label="item.name"
            :value="item.value"
            :key="item.id"
            v-for="item in manufactors"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        label="材料编码："
        prop="materialNo"
        v-if="formData.atLastTrademark === 1"
      >
        <el-input
          size="mini"
          style="width: 200px"
          v-model="formData.materialNo"
          placeholder="不超过32个字"
          maxlength="32"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="材料分类："
        prop="parentId"
        v-if="formData.atLastTrademark === 1"
      >
        <el-select
          v-model="formData.parentId"
          size="mini"
          @change="selectChildCategory"
        >
          <el-option
            :label="item.name"
            :value="item.materialId"
            :key="item.materialId"
            v-for="item in materialClasses"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        label="材料颜色："
        prop="colour"
        v-if="formData.atLastTrademark === 1"
      >
        <el-color-picker
          v-model="formData.colour"
          size="small"
        ></el-color-picker>
        <div class="color-value">{{ formData.colour }}</div>
      </el-form-item>
      <el-form-item label="材料图片：" prop="image">
        <template>
          <el-upload
            class="avatar-uploader"
            :action="action"
            :show-file-list="false"
            :on-remove="handleRemove"
            :before-upload="beforeUpload"
          >
            <img v-if="formData.image" :src="formData.image" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </template>
      </el-form-item>

      <el-form-item label="材料副标题：" prop="subtitle">
        <el-input
          size="mini"
          style="width: 300px"
          v-model="formData.subtitle"
          placeholder="不超过20个字（定制-选择材质时展示）"
          maxlength="40"
        ></el-input>
      </el-form-item>
      <el-form-item label="材料描述：" prop="description">
        <el-input
          style="width: 50%"
          size="mini"
          type="textarea"
          :rows="4"
          v-model="formData.description"
          placeholder="不超过100个字（定制-选择材质时展示）"
          maxlength="200"
        ></el-input>
      </el-form-item>
      <el-form-item label="状态：" prop="openFlag">
        <el-radio-group v-model="formData.openFlag" size="mini">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">停用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="材料详情：" prop="content">
        <tinymce
          style="width: 1000px"
          v-model="formData.content"
          ref="tinymce"
        ></tinymce>
      </el-form-item>
      <el-form-item label="选择型号：" v-show="formData.atLastTrademark === 1">
        <el-button @click="addModel" class="mini-search-btn"
          ><i class="el-icon-plus el-icon--left"></i>添加型号</el-button
        >
        <!-- 选择型号 - 筛选（编辑时显示） -->
        <el-select
          v-if="looking"
          v-model="currentBrand"
          size="mini"
          placeholder="请选择型号筛选"
          style="width: 150px"
          clearable
        >
          <el-option
            v-for="(brand, index) in brandList"
            :key="index"
            :label="brand.name"
            :value="brand.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item v-show="formData.atLastTrademark === 1">
        <el-table
          :data="tempGoodsTable"
          border
          header-row-class-name="header-row"
          class="tableCenter goods-table"
          style="width: 90%"
          max-height="400"
        >
          <el-table-column
            align="center"
            label="型号名称"
            prop="modelName"
            :width="150"
            show-overflow-tooltip
            fixed
          >
          </el-table-column>
          <el-table-column
            align="center"
            label="长(mm)"
            prop="length"
            :width="120"
          >
            <template slot-scope="scope">
              <el-input
                size="mini"
                key="1"
                v-model="scope.row.length"
                type="number"
                @input="
                  scope.row.length =
                    /^\d+\.?\d{0,2}$/.test(scope.row.length) ||
                    scope.row.length == ''
                      ? scope.row.length
                      : Number(
                          scope.row.length
                            .toString()
                            .match(/^\d+(?:\.\d{0,1})?/)
                        )
                "
                min="0"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="宽(mm)"
            prop="width"
            :width="120"
          >
            <template slot-scope="scope">
              <el-input
                size="mini"
                key="2"
                v-model="scope.row.width"
                type="number"
                @input="
                  scope.row.width =
                    /^\d+\.?\d{0,2}$/.test(scope.row.width) ||
                    scope.row.width == ''
                      ? scope.row.width
                      : Number(
                          scope.row.width.toString().match(/^\d+(?:\.\d{0,1})?/)
                        )
                "
                min="0"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="高(mm)"
            prop="height"
            :width="120"
          >
            <template slot-scope="scope">
              <el-input
                size="mini"
                key="3"
                v-model="scope.row.height"
                type="number"
                @input="
                  scope.row.height =
                    /^\d+\.?\d{0,2}$/.test(scope.row.height) ||
                    scope.row.height == ''
                      ? scope.row.height
                      : Number(
                          scope.row.height
                            .toString()
                            .match(/^\d+(?:\.\d{0,1})?/)
                        )
                "
                min="0"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="重量(g)"
            prop="weight"
            :width="120"
          >
            <template slot-scope="scope">
              <el-input
                size="mini"
                key="4"
                v-model="scope.row.weight"
                type="number"
                @input="
                  scope.row.weight =
                    /^\d+\.?\d{0,2}$/.test(scope.row.weight) ||
                    scope.row.weight == ''
                      ? scope.row.weight
                      : Number(
                          scope.row.weight
                            .toString()
                            .match(/^\d+(?:\.\d{0,1})?/)
                        )
                "
                min="0"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="第三方SKU"
            prop="thirdSku"
            :width="120"
          >
            <template slot-scope="scope">
              <el-input
                key="5"
                size="mini"
                v-model="scope.row.thirdSku"
                min="0"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="BOM编码"
            prop="bomCode"
            :width="120"
          >
            <template slot-scope="scope">
              <el-input
                key="5"
                size="mini"
                v-model="scope.row.bomCode"
                min="0"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column align="center" key="5" label="外框图" :width="80">
            <template slot-scope="scope" style="text-align: center">
              <upload-small-img
                :type="1"
                :item="scope.row"
                :index="scope.$index"
                @getImgUrl="getOutImage"
              ></upload-small-img>
            </template>
          </el-table-column>
          <el-table-column align="center" key="6" label="底图" :width="80">
            <template slot-scope="scope" style="text-align: center">
              <upload-small-img
                :type="2"
                :item="scope.row"
                :index="scope.$index"
                @getImgUrl="getFloorImage"
              ></upload-small-img>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="缺货"
            prop="underStockFlag"
            :width="120"
          >
            <template slot-scope="scope">
              <el-checkbox
                v-model="scope.row.underStockFlag"
                :true-label="1"
                :false-label="0"
              ></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            key="7"
            label="状态"
            prop="openFlag"
            :formatter="formatStatus"
          ></el-table-column>
          <el-table-column align="center" key="8" label="操作" width="150">
            <template slot-scope="scope">
              <el-button
                class="mini-tableadd-btn"
                v-if="scope.row.openFlag !== 1"
                @click="handleChangeStart(scope.row, 1)"
                >启用</el-button
              >
              <el-button
                class="mini-freeze-btn"
                v-else
                @click="handleChangeStart(scope.row, 0)"
                >停用</el-button
              >
              <el-button
                class="mini-delete-btn"
                @click="handleDeleteGood(scope.$index)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
      <el-form-item
        label="允许用户上传图片："
        prop="allowUploadFlag"
        v-if="formData.atLastTrademark === 1"
      >
        <el-radio-group v-model="formData.allowUploadFlag" size="mini">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        label="图片强制铺满血位："
        prop="mandatoryCoveredBleedFlag"
        v-if="formData.atLastTrademark === 1"
      >
        <el-radio-group
          v-model="formData.mandatoryCoveredBleedFlag"
          size="mini"
        >
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        label="图片关联"
        style="margin-bottom: 20px; width: 1070px"
        v-if="formData.atLastTrademark === 1"
      >
        <el-input
          size="mini"
          clearable
          style="width: 200px"
          @focus="categoryFocus"
          @blur="focus = false"
          placeholder="请选择图片分类筛选"
          class="category-input"
          v-model="pageInfo.categoryName"
        ></el-input>
        <transition name="el-zoom-in-top">
          <div
            v-clickoutside="handleClose"
            class="category-box"
            v-if="categoryShow"
          >
            <el-tree
              :data="categoryPicList"
              @node-click="categorySelect"
              :props="props"
              ref="tree"
            ></el-tree>
          </div>
        </transition>
        <el-table
          :data="pictureList"
          tooltip-effect="dark"
          ref="multipleSelect"
          @select="select"
          @select-all="selectAll"
          @selection-change="handleSelectionChange"
          border
          header-row-class-name="header-row"
          class="tableCenter goods-table"
          max-height="460"
        >
          <el-table-column
            type="selection"
            width="50"
            align="center"
          ></el-table-column>
          <el-table-column align="center" label="id" prop="id">
          </el-table-column>
          <el-table-column align="center" label="图片名称" prop="name">
          </el-table-column>
          <el-table-column align="center" label="图片分类" prop="categoryName">
          </el-table-column>
          <el-table-column
            align="center"
            label="图片状态"
            prop="openFlag"
            :formatter="formatStatus"
          >
          </el-table-column>
        </el-table>
        <page
          :page="pageInfo.page"
          :total="total"
          @sizeChange="sizeChange"
          @currentChange="currentChange"
        ></page>
      </el-form-item>
      <el-form-item label="不适用范围：" style="width: 800px" max-height="400">
        <div>
          <el-button
            icon="el-icon-plus"
            class="mini-search-btn"
            @click="distributorShow = true"
          >
            添加分销商
          </el-button>
          <el-table
            :data="distributorData"
            border
            header-row-class-name="header-row"
            max-height="600"
            class="tableCenter"
          >
            <el-table-column
              align="center"
              label="分销商用户名"
              prop="name"
            ></el-table-column>
            <el-table-column
              align="center"
              label="公司名"
              prop="companyName"
            ></el-table-column>
            <el-table-column align="center" label="操作" width="80">
              <template slot-scope="scope">
                <el-button
                  style="margin-top: 0px; margin-bottom: 0px"
                  class="mini-delete-btn"
                  @click="handleDeleteDistributor(scope.$index)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-form-item>
    </el-form>
    <el-button
      style="margin-left: 25%; margin-top: 30px"
      class="mini-search-btn"
      :loading="loading"
      @click="handleSubmit('formData')"
      >保存</el-button
    >
    <el-button
      style="margin-bottom: 30px"
      size="mini"
      class="mini-search-btn"
      @click="handleCancel"
      >返回</el-button
    >
    <!-- 导入型号dialog -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="addModels"
      :before-close="cancelModels"
      width="90%"
    >
      <selectModel
        :categoryId="formData.categoryId"
        :selectItemsData="goodsTable"
        ref="selectGoodItems"
        @submit="closeModels"
        @cancel="cancelModels"
      ></selectModel>
    </el-dialog>
    <!-- 不适用分销商 -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="distributorShow"
      :before-close="disCancel"
      width="80%"
    >
      <select-distributor
        :distributorData="distributorData"
        ref="selectDistributor"
        @cancel="cancel"
        @submit="disSubmit"
      >
      </select-distributor>
    </el-dialog>
    <!-- 关联货品 -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="itemsShow"
      width="80%"
      :before-close="closeItemsDialog"
    >
      <select-item
        :goodsType="2"
        :relevanceMaterialFlag="0"
        :selectItemsData="goodsItems"
        ref="selectGoodItems"
        @cancel="itemsShow = false"
        @submit="getItemsData"
      ></select-item>
    </el-dialog>
  </div>
</template>
<script>
import Tinymce from "@/components/Tinymce";
import selectModel from "./selectModel.vue";
import selectPicture from "./selectPicture.vue";
import uploadSmallImg from "./uploadSmallImg.vue";
import selectDistributor from "@/views/goods/components/selectDistributorAll";
import selectItem from "@/views/marketingPromotion/groupBuy/components/selectItem";
import page from "@/components/pagination";
import { setArr2 } from "@/utils/common.js";
import {monthDay} from '@/utils/timeFormat.js'
export default {
  data() {
    const validateContent = (rule, value, callback) => {
      let content = this.formData.content.replace(/<(?!img).*?>/gi, "").trim();
      if (content === "") {
        callback(new Error("请输入材料详情"));
      } else {
        callback();
      }
    };
    return {
      distributorShow: false,
      action: process.env.BASE_API + "system/v1/web/admin/oss/sts",
      loading: false,
      vloading: false,
      append: true, // 添加材料
      looking: false, // 查看材料
      itemsShow: false, // 货品显示
      goodsItems: [], // 关联货品
      categorys: [], // 材料类型
      manufactors: [
        {
          id: 1001,
          name: "云创",
          value: "YC",
        },
        {
          id: 1002,
          name: "联辉王",
          value: "LHW",
        },
        {
          id: 1003,
          name: "麦客",
          value: "MK",
        },
        {
          id: 1004,
          name: "多鸿欧丽科",
          value: "DH_OLK",
        },
		{
		  id: 1005,
		  name: "壳大师(飞快)",
		  value: "KDS_FK",
		}
      ],
      materialClasses: [],
      isParent: 0, // 是否顶级分类
      distributorData: [], // 不适用分销商
      pictureIds: [], // 关联图片
      pageInfo: {
        page: 1,
        size: 10,
        categoryId: undefined,
        categoryName: undefined,
      },
      total: 0,
      formData: {
        name: "",
        englishName: "",
        categoryId: null,
        manufactor: "", // 第三方工厂
        atLastTrademark: 1, // 是否最终材质
        colour: "", // 材料颜色
        materialNo: "", // 材料编码
        parentId: "", // 上级分类id
        subtitle: "",
        description: "",
        allowUploadFlag: 0, // 是否允许用户上传图片
        mandatoryCoveredBleedFlag: 0, // 图片是否强制铺满血位
        image: "",
        content: "",
        openFlag: 1,
        itemId: undefined,
        itemCode: undefined,
        distributorIdList: [], // 不适用分销商
        materialRelevanceDTOList: [], // 关联型号
        pictureIdList: [], // 关联图片
      },
      rules: {
        name: [
          {
            required: true,
            message: "请输入材料名称",
            trigger: "blur",
          },
        ],
        categoryId: [
          {
            required: true,
            message: "请选择材料类型",
            trigger: "blur",
          },
        ],
        manufactor: [
          {
            required: true,
            message: "请选择第三方工厂",
            trigger: "blur",
          },
        ],
        stuffName: [
          {
            required: true,
            message: "请输入材料术语",
            trigger: "blur",
          },
        ],
        materialNo: [
          {
            required: true,
            message: "请输入材料编码",
            trigger: "blur",
          },
        ],
        parentId: [
          {
            required: true,
            message: "请选择材料分类",
            trigger: ["blur", "change"],
          },
        ],
        image: [
          {
            required: true,
            message: "请上传材料图片",
            trigger: "blur",
          },
        ],
        subtitle: [
          {
            required: true,
            message: "请输入材料副标题",
            trigger: "blur",
          },
        ],
        description: [
          {
            required: true,
            message: "请输入材料描述",
            trigger: "blur",
          },
        ],
        content: [
          {
            required: true,
            message: "请输入材料详情",
            trigger: "blur",
          },
          { validator: validateContent },
        ],
      },
      disShow: false,
      imageObj: "",
      addModels: false, // 关联型号
      categoryShow: false, // 图片分类
      goodsTable: [], //型号列表
      categoryPicList: [],
      pictureList: [],
      props: {
        label: "name",
        children: "childrenList",
        isLeaf: "leaf",
      },
      // 选择型号
      currentBrand: "", // 当前筛选型号
      brandList: [], // 型号列表
    };
  },
  components: {
    Tinymce,
    selectModel,
    uploadSmallImg,
    selectDistributor,
    selectItem,
    selectPicture,
    page,
  },
  created() {
    this.initData().then((result) => {
      // if (result[0] && result[1] && result[2] && result[3]) {
      if (result[0] && result[1] && result[2]) {
        if (this.$route.params.id != undefined) {
          this.vloading = true;
          this.append = false;
          this.looking = true;
          this.$http
            .materialDetail(this, { id: this.$route.params.id })
            .then((res) => {
              if (res.success) {
                this.formData = res.data;
                // 关联货品
                if (this.formData.itemSimplePageBean) {
                  this.goodsItems.push({
                    goodsId: this.formData.itemSimplePageBean.goodsId,
                    itemId: this.formData.itemSimplePageBean.itemId,
                    itemCode: this.formData.itemSimplePageBean.itemCode,
                    itemName: this.formData.itemSimplePageBean.itemName,
                    saleStatus: this.formData.itemSimplePageBean.saleStatus,
                  });
                }
                // 材料分类
                if (this.formData.categoryId) {
                  this.selectCategory(this.formData.categoryId);
                }
                // 型号
                this.goodsTable = [];
                if (
                  this.formData.materialRelevanceDTOList !== undefined &&
                  this.formData.materialRelevanceDTOList !== null &&
                  this.formData.materialRelevanceDTOList.length > 0
                ) {
                  this.formData.materialRelevanceDTOList.forEach((item) => {
                    item.openFlag = item.openFlag;
                  });
                  this.goodsTable = JSON.parse(
                    JSON.stringify(this.formData.materialRelevanceDTOList)
                  );
                }

                // 选择型号 - 筛选
                if (
                  this.formData.parentModel &&
                  this.formData.parentModel.length > 0
                ) {
                  this.brandList = this.formData.parentModel;
                }

                //关联图片
                this.multipleSelect = [];
                if (
                  this.formData.pictureRelaList &&
                  this.formData.pictureRelaList.length > 0
                ) {
                  this.formData.pictureRelaList.forEach((item) => {
                    this.pictureIds.push(item.pictureId);
                    this.pictureList.forEach((row2) => {
                      if (item.pictureId === row2.id) {
                        this.$refs.multipleSelect.toggleRowSelection(row2);
                      }
                    });
                  });
                }
                // 不适用分销商
                this.formData.distributorIdList = [];
                this.formData.distributorIdList =
                  res.data.distributorExcludeList;
              }
              res.success ? (this.vloading = false) : (this.vloading = false);
              this.initDistributor();
            });
        } else {
          this.looking = false;
          this.initDistributor();
        }
      }
    });
  },
  directives: {
    //..事件绑定
    clickoutside: {
      bind: function (el, binding, vnode) {
        function documentHandler(e) {
          if (el.contains(e.target)) {
            //..这里判断点击的元素是否是本身，是本身，则返回
            return false;
          }
          if (binding.expression) {
            //..判断指令中是否绑定了函数
            binding.value(e); //..如果绑定了函数 则调用那个函数，此处binding.value就是handleClose方法
          }
        }
        el._vueClickOutside_ = documentHandler; //..给当前元素绑定个私有变量，方便在unbind中可以解除事件监听
        document.addEventListener("click", documentHandler);
      },
      unbind: function (el, binding) {
        document.removeEventListener("click", el._vueClickOutside_);
        delete el._vueClickOutside_;
      },
    },
  },
  methods: {
    initData() {
      // 产品材料类型
      let a = this.$http.productUsableList(this).then((res) => {
        if (res.success) {
          this.categorys = res.data;
        }
        return res.success;
      });
      // 材料分类
      let b = this.$http
        .materialTreeList(this, {
          atLastTrademark: 0,
          openFlag: 1,
          parentId: 0,
        })
        .then((res) => {
          if (res.success) {
            this.materialClasses = res.data;
          }
          return res.success;
        });
      // 图片分类
      // let c = this.$http.pictureCategoryPoList(this, {page:1,size:1000,atLastTrademark:1,openFlag:1}).then(res => {
      // 	if(res.success) {
      // 		this.categoryPicList = res.data.list
      // 	}
      // 	return res.success
      // })
      // 图片列表
      let d = this.$http.picturePoList(this, this.pageInfo).then((res) => {
        if (res.success) {
          this.pictureList = res.data.list;
          this.total = res.data.total;
        }
        return res.success;
      });
      // return Promise.all([a, b, c, d]).then(res => {
      return Promise.all([a, b, d]).then((res) => {
        return res;
      });
    },
    getPictureList() {
      this.isSelect = false;
      this.$http.picturePoList(this, this.pageInfo).then((res) => {
        if (res.success) {
          this.pictureList = [];
          if (
            res.data.list !== undefined &&
            res.data.list !== null &&
            res.data.list.length > 0
          ) {
            this.pictureList = res.data.list;
            this.pictureIds.forEach((row1) => {
              //重新获取数据时，判断哪些选中了
              this.pictureList.forEach((row2) => {
                if (row1 === row2.id) {
                  this.$refs.multipleSelect.toggleRowSelection(row2);
                }
              });
            });
          }
          this.total = res.data.total;
        }
      });
    },
    initDistributor() {
      this.distributorData = [];
      if (
        this.formData.distributorIdList !== undefined &&
        this.formData.distributorIdList !== null &&
        this.formData.distributorIdList.length > 0
      ) {
        this.formData.distributorIdList.forEach((item) => {
          this.distributorData.push({
            id: item.distributorId,
            name: item.distributorName,
            companyName: item.distributorCompanyName,
          });
        });
      }
    },
    handleDeleteDistributor(index) {
      this.distributorData.splice(index, 1);
    },
    disSubmit(msg) {
      this.distributorData = msg;
      this.distributorShow = false;
    },
    disCancel() {
      this.$refs.selectDistributor.handleCancel();
    },
    cancel() {
      this.distributorShow = false;
    },
    // 是否最终材质分类
    changeCategory(val) {
      if (val === 0) {
        this.formData.parentId = 0;
        this.formData.colour = "";
      } else {
        this.formData.parentId = "";
      }
    },
    // 选中颜色
    changeColour(value) {
      this.formData.colour = value;
    },
    // 选择材料类型
    selectCategory(cId) {
      this.formData.categoryId = cId;
    },
    // 选择材料分类
    selectChildCategory(value) {
      this.formData.parentId = value;
    },
    // 选择第三方工厂
    selectFactory(val) {
      if (val === 1001) {
        this.formData.manufactor = "YC";
      } else if (val === 1002) {
        this.formData.manufactor = "LHW";
      } else if (val === 1003) {
        this.formData.manufactor = "MK";
      } else if (val === 1004) {
        this.formData.manufactor = "DH_OLK";
      } else if (val === 1005) {
        this.formData.manufactor = "KDS_FK";
      }
    },
    // 删除货品
    handleDeleteGoodsItems(index) {
      this.goodsItems.splice(index, 1);
    },
    cancelGoodsItems() {
      this.$refs.selectGoodItems.handleCancel();
    },
    // 关闭关联货品
    closeItemsDialog() {
      this.$refs.selectGoodItems.handleCancel();
    },
    // 关联货品弹框
    assignGoods() {
      this.itemsShow = true;
    },
    getItemsData(val) {
      if (val && val.length > 1) {
        this.$message.error("最多关联一个货品");
      } else {
        this.goodsItems = [];
        this.goodsItems = this.goodsItems.concat(val);
        this.itemsShow = false;
      }
    },
    getOutImage(index, url) {
      //外框图
      this.goodsTable[index].outImage = url;
    },
    getFloorImage(index, url) {
      //底图
      this.goodsTable[index].floorImage = url;
    },
    formatStatus(row, col, val) {
      switch (val) {
        case 1:
          return "启用";
          break;
        case 0:
          return "停用";
          break;
      }
    },
    formatSaleStatus(row, col, val) {
      switch (val) {
        case 1:
          return "未上架";
          break;
        case 2:
          return "审批中";
          break;
        case 3:
          return "已上架";
          break;
      }
    },
    handleChangeStart(row, status) {
      row.openFlag = status;
    },
    handleDeleteGood(index) {
      this.goodsTable.splice(index, 1);
    },
    handleClose() {
      if (this.categoryShow && !this.focus) {
        this.categoryShow = false;
      }
    },
    // 触发图片分类
    categoryFocus() {
      this.focus = true;
      // 图片分类
      let c = this.$http
        .pictureCategoryPoList(this, {
          page: 1,
          size: 1000,
          atLastTrademark: 1,
          openFlag: 1,
        })
        .then((res) => {
          if (res.success) {
            this.categoryPicList = res.data.list;
            this.categoryShow = true;
          }
        });
    },
    // 选择图片分类
    categorySelect(data, checked, indeterminate) {
      this.pageInfo.categoryName = data.name;
      this.pageInfo.categoryId = data.id;
      if (this.categoryShow && !this.focus) {
        this.categoryShow = false;
      }
    },
    select(selection, row) {
      // 单选时调用
      this.isSelect = true;
      let d = false;
      for (let i = 0; i < this.pictureIds.length; i++) {
        if (this.pictureIds[i] === row.id) {
          this.pictureIds.splice(i, 1);
          d = true;
          break;
        }
      }
      if (!d) {
        this.pictureIds.push(row.id);
        this.pictureIds = setArr2(this.pictureIds);
      }
    },
    selectAll(selection) {
      // 全选时调用
      this.isSelect = true;
      if (selection.length === 0) {
        this.pictureList.forEach((row) => {
          for (let i = 0; i < this.pictureIds.length; i++) {
            if (this.pictureIds[i] === row.id) {
              this.pictureIds.splice(i, 1);
              break;
            }
          }
        });
      } else {
        selection.forEach((item) => {
          this.pictureIds.push(item.id);
        });
        this.pictureIds = setArr2(this.pictureIds);
      }
    },
    handleSelectionChange(val) {
      // 当切换页面时的作用
      if (val.length === 0 && this.pictureIds.length != 0 && !this.isSelect) {
        this.pictureIds.forEach((row1) => {
          this.pictureList.forEach((row2) => {
            if (row1 === row2.id) {
              this.$refs.multipleSelect.toggleRowSelection(row2);
            }
          });
        });
        this.isSelect = true;
      }
    },
    sizeChange(size) {
      this.pageInfo.page = 1;
      this.pageInfo.size = size;
      this.getPictureList();
    },
    currentChange(page) {
      this.pageInfo.page = page;
      this.getPictureList();
    },
    // 关闭型号弹框
    cancelModels() {
      this.addModels = false;
    },
    closeModels(val) {
      this.addModels = false;
      this.goodsTable = [];
      for (let i = 0; i < val.length; i++) {
        if (val[i].id !== undefined) val[i].id = undefined;
      }
      this.goodsTable = JSON.parse(JSON.stringify(val));
    },
    addModel() {
      this.addModels = true;
    },
    beforeUpload(file) {
      if (
        file.type != "image/jpeg" &&
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
        this.$message.error("上传头像图片大小不能超过 2MB!");
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
        // 上传
        client
          .multipartUpload('flexible/material/' + monthDay(new Date()) + '/' + random_name, file, {})
          .then((results) => {
            return new Promise((resolve, reject) => {
              (this.formData.image = result.data.hostname + results.name),
                // this.imageObj = {
                // 	hasSuccess: true,
                // 	// url: result.data.hostname + results.name + '?x-oss-process=image/resize,h_120,l_120'
                // }
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
    handleRemove(file) {
      this.formData.image = "";
    },
    // 保存
    handleSubmit(data) {
      this.$refs[data].validate((valid) => {
        if (valid) {
          this.loading = true;

          if (this.formData.atLastTrademark === 0) {
            this.formData.parentId = 0; // 材料分类
            this.formData.materialNo = "";
          }

          if (this.formData.atLastTrademark === 1) {
            // 关联货品
            if (this.goodsItems.length > 0) {
              this.formData.itemId = this.goodsItems[0].id
                ? this.goodsItems[0].id
                : this.goodsItems[0].itemId;
              this.formData.itemCode = this.goodsItems[0].itemCode;
            } else {
              this.$message.error("请选择关联货品");
              this.loading = false;
              return;
            }

            // 选择型号
            this.formData.materialRelevanceDTOList = [];
            this.goodsTable.forEach((good) => {
              good.materialId = this.formData.id;
              good.materialName = this.formData.name;
              this.formData.materialRelevanceDTOList.push(good);
            });

            if (
              this.formData.materialRelevanceDTOList !== undefined &&
              this.formData.materialRelevanceDTOList.length > 0
            ) {
              for (
                let i = 0;
                i < this.formData.materialRelevanceDTOList.length;
                i++
              ) {
                if (
                  this.formData.materialRelevanceDTOList[i].length ===
                    undefined ||
                  this.formData.materialRelevanceDTOList[i].length === null ||
                  this.formData.materialRelevanceDTOList[i].length === ""
                ) {
                  this.$message.error(
                    this.formData.materialRelevanceDTOList[i].materialName +
                      "材料关系的长度不能为空！"
                  );
                  this.loading = false;
                  return;
                }
                if (
                  this.formData.materialRelevanceDTOList[i].width ===
                    undefined ||
                  this.formData.materialRelevanceDTOList[i].width === null ||
                  this.formData.materialRelevanceDTOList[i].width === ""
                ) {
                  this.$message.error(
                    this.formData.materialRelevanceDTOList[i].materialName +
                      "材料关系的宽度不能为空！"
                  );
                  this.loading = false;
                  return;
                }
                if (
                  this.formData.materialRelevanceDTOList[i].height ===
                    undefined ||
                  this.formData.materialRelevanceDTOList[i].height === null ||
                  this.formData.materialRelevanceDTOList[i].height === ""
                ) {
                  this.$message.error(
                    this.formData.materialRelevanceDTOList[i].materialName +
                      "材料关系的高度不能为空！"
                  );
                  this.loading = false;
                  return;
                }
                if (
                  this.formData.materialRelevanceDTOList[i].weight ===
                    undefined ||
                  this.formData.materialRelevanceDTOList[i].weight === null ||
                  this.formData.materialRelevanceDTOList[i].weight === ""
                ) {
                  this.$message.error(
                    this.formData.materialRelevanceDTOList[i].materialName +
                      "材料关系的重量不能为空！"
                  );
                  this.loading = false;
                  return;
                }
                if (
                  this.formData.materialRelevanceDTOList[i].thirdSku ===
                    undefined ||
                  this.formData.materialRelevanceDTOList[i].thirdSku === null ||
                  this.formData.materialRelevanceDTOList[i].thirdSku === ""
                ) {
                  this.$message.error(
                    this.formData.materialRelevanceDTOList[i].materialName +
                      "第三方SKU不能为空！"
                  );
                  this.loading = false;
                  return;
                }
                // if (
                //   this.formData.materialRelevanceDTOList[i].bomCode ===
                //     undefined ||
                //   this.formData.materialRelevanceDTOList[i].bomCode === null ||
                //   this.formData.materialRelevanceDTOList[i].bomCode === ""
                // ) {
                //   this.$message.error(
                //     this.formData.materialRelevanceDTOList[i].materialName +
                //       "BOM编码不能为空！"
                //   );
                //   this.loading = false;
                //   return;
                // }
                if (
                  this.formData.materialRelevanceDTOList[i].outImage ===
                    undefined ||
                  this.formData.materialRelevanceDTOList[i].outImage === null ||
                  this.formData.materialRelevanceDTOList[i].outImage === ""
                ) {
                  this.$message.error(
                    this.formData.materialRelevanceDTOList[i].materialName +
                      "外框图不能为空！"
                  );
                  this.loading = false;
                  return;
                }
                if (
                  this.formData.materialRelevanceDTOList[i].floorImage ===
                    undefined ||
                  this.formData.materialRelevanceDTOList[i].floorImage ===
                    null ||
                  this.formData.materialRelevanceDTOList[i].floorImage === ""
                ) {
                  this.$message.error(
                    this.formData.materialRelevanceDTOList[i].materialName +
                      "底图不能为空！"
                  );
                  this.loading = false;
                  return;
                }
              }
            } else {
              this.$message.error("请选择型号");
              this.loading = false;
              return;
            }
          }
          // 关联图片
          this.formData.pictureIdList = this.pictureIds;
          // 不适用范围处理
          this.formData.distributorIdList = [];
          if (
            this.distributorData !== undefined &&
            this.distributorData !== null &&
            this.distributorData.length > 0
          ) {
            this.distributorData.forEach((item) => {
              this.formData.distributorIdList.push(item.id);
            });
          }
          // 关联型号
          if (
            this.formData.materialRelevanceDTOList != undefined &&
            this.formData.materialRelevanceDTOList != null &&
            this.formData.materialRelevanceDTOList.length > 0
          ) {
            this.formData.materialRelevanceDTOList.forEach((element) => {
              element.openFlag = element.openFlag;
            });
          }

          if (
            this.formData.id !== undefined &&
            this.formData.id !== null &&
            this.formData.id !== ""
          ) {
            // 编辑
            this.$http.editMaterial(this, this.formData).then((res) => {
              this.loading = false;
              if (res.success) {
                this.$message.success("编辑成功");
                this.$router.go(-1);
              }
            });
          } else {
            // 新增
            this.$http.addMaterial(this, this.formData).then((res) => {
              this.loading = false;
              if (res.success) {
                this.$message.success("新增成功");
                this.$router.go(-1);
              }
            });
          }
        }
      });
    },
    handleCancel() {
      // 返回操作
      this.$router.go(-1);
    },
  },
  watch: {
    "pageInfo.categoryName": {
      handler() {
        if (
          this.pageInfo.categoryName === undefined ||
          this.pageInfo.categoryName === null ||
          this.pageInfo.categoryName === ""
        ) {
          this.pageInfo.categoryId = undefined;
        }
        this.pageInfo.page = 1;
        this.getPictureList();
      },
      deep: true,
    },
  },
  computed: {
    tempGoodsTable() {
      var search = this.currentBrand;
      if (search) {
        return this.goodsTable.filter(function (dataNews) {
          // return Object.keys(dataNews).some(function (key) {
          return ["modelParentId"].some(function (key) {
            return String(dataNews[key]).toLowerCase().indexOf(search) > -1;
          });
        });
      }
      return this.goodsTable;
    },
  },
};
</script>
<style lang="scss">
.add-productline {
  background-color: white;
  min-height: 100%;
  .hint-msg {
    color: #ccc;
    font-size: 12px;
    line-height: 40px;
    margin-left: 10px;
  }
  .el-input__inner,
  .el-textarea__inner {
    background-color: white;
  }
  .header-row {
    @include table-header-color;
  }
  .distributor-table {
    margin-top: 20px;
  }
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
    margin-bottom: 20px;
  }
  h4 {
    display: inline-block;
    font-weight: 350;
    font-size: 16px;
    margin: 0 20px;
  }
  .btn-home {
    float: right;
    padding: 5px;
    margin-top: 8px;
    margin-right: 8px;
    margin-left: 0;
    font-size: 12px;
  }
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: rgb(33, 184, 203);
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 120px;
    height: 120px;
    line-height: 120px;
    text-align: center;
  }
  .avatar {
    width: 120px;
    height: 120px;
    display: block;
  }
  .el-color-picker {
    vertical-align: middle;
  }
  .color-value {
    display: inline-block;
    line-height: 40px;
    vertical-align: middle;
  }
  .category-input {
    width: 150px;
    float: left;
    margin-right: 5px;
    position: relative;
  }
  .category-box {
    border: 1px solid #ccc;
    min-width: 220px;
    height: 300px;
    overflow-y: scroll;
    padding: 10px;
    background-color: white;
    position: absolute;
    left: 0;
    top: 45px;
    z-index: 99;
    border-radius: 10px;
  }
}
</style>
