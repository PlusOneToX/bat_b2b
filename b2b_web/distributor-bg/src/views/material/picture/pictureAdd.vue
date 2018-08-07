<template>
  <div class="add-productline">
    <header v-if="append">
      <h4>添加图片</h4>
      <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="handleCancel">
        返回列表
      </el-button>
    </header>
    <header v-if="looking">
      <h4>查看图片</h4>
      <el-button class="btn-home" icon="el-icon-d-arrow-left" @click="handleCancel">
        返回列表
      </el-button>
    </header>

    <el-form label-width="150px" :rules="rules"  ref="formData" :model="formData" v-loading="vloading">
      <el-form-item label="图片分类" prop="categoryId">
        <el-select v-model="formData.categoryId" placeholder="图片分类" filterable clearable size="mini"
          @change="changeType">
          <el-option v-for="item in pictureCategoryList" :key="item.id" :label="item.name" :disabled="item.disabled"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="图片名称" prop="name">
        <el-input size="mini" style="width:300px;" v-model="formData.name" placeholder="不超过40个字" maxlength="40">
        </el-input>
      </el-form-item>
      <el-form-item label="图片英文名称" prop="englishName">
        <el-input size="mini" style="width:500px;" v-model="formData.englishName" placeholder="不超过80个字" maxlength="80">
        </el-input>
      </el-form-item>
      <el-form-item label="图片价格" prop="copyrightCost">
        <el-input size="mini" style="width:500px;" type="number"  min="0" v-model="formData.copyrightCost" placeholder="输入图片价格（可精确到0.01元）">元
        </el-input>
      </el-form-item>
      <el-form-item label="图片类型" prop="type">
        <el-select v-model="formData.type" size="mini">
          <el-option :label="item.label" :value="item.type" :key="item.type" v-for="item in types"
            :disabled="item.disabled"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="图片编码" prop="code">
        <el-input size="mini" style="width:200px;" v-model="formData.code" placeholder="不超过32个字" maxlength="32">
        </el-input>
      </el-form-item>

      <el-form-item label="图片原图" prop="originImage">
        <template>
          <el-upload class="avatar-uploader" :action="action" :show-file-list="false" :on-remove="handleRemove"
           :before-upload="beforeUploadOriginImage">
            <img v-if="formData.originImage" :src="formData.originImage" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </template>
      </el-form-item>
      <el-form-item label="图片缩略图" prop="thumbnail">
        <template>
          <el-upload class="avatar-uploader" :action="action" :show-file-list="false" :on-remove="handleRemove"
           :before-upload="beforeUploadThumbnail">
            <img v-if="formData.thumbnail" :src="formData.thumbnail" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </template>
      </el-form-item>
      <el-form-item label="带背景色预览图" prop="backgroundPreviewUrl">
        <template>
          <el-upload class="avatar-uploader" :action="action" :show-file-list="false" :on-remove="handleRemove"
           :before-upload="beforeUploadBackUrl">
            <img v-if="formData.backgroundPreviewUrl" :src="formData.backgroundPreviewUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </template>
      </el-form-item>
      <el-form-item label="无相机空位预览图" prop="noCameraVacancyPreview">
        <template>
          <el-upload class="avatar-uploader" :action="action" :show-file-list="false" :on-remove="handleRemove"
           :before-upload="beforeUploadNoCamera">
            <img v-if="formData.noCameraVacancyPreview" :src="formData.noCameraVacancyPreview" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </template>
      </el-form-item>

      <div v-if="Number(formData.type) === 3">
        <el-form-item label="模板中心点" required>
          <el-col :span="5">
            <el-form-item prop="templateCenterX">
              &nbsp;X&nbsp;<el-input style="width:80%" size="mini" v-model="formData.templateCenterX" type="number"
                @input="formData.templateCenterX=/^\d+\.?\d{0,2}$/.test(formData.templateCenterX)||formData.templateCenterX == '' ? formData.templateCenterX:Number(formData.templateCenterX.toString().match(/^\d+(?:\.\d{0,2})?/))"
                min="0"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item prop="templateCenterY">
              &nbsp;Y&nbsp;<el-input style="width:80%" size="mini" v-model="formData.templateCenterY" type="number"
                @input="formData.templateCenterY=/^\d+\.?\d{0,2}$/.test(formData.templateCenterY)||formData.templateCenterY == '' ? formData.templateCenterY:Number(formData.templateCenterY.toString().match(/^\d+(?:\.\d{0,2})?/))"
                min="0"></el-input>
            </el-form-item>
          </el-col>
        </el-form-item>

        <div v-for="(item, index) in formData.templateEditList" :key="index">
          <el-form-item label="可编辑区域中心点" required>
            <el-col :span="5">
              <el-form-item :prop="'templateEditList.' + index + '.editCenterX'" :rules="rules.editCenterX">
                &nbsp;X&nbsp;<el-input style="width:80%" size="mini" v-model="item.editCenterX" type="number"
                  @input="item.editCenterX=/^\d+\.?\d{0,2}$/.test(item.editCenterX)||item.editCenterX == '' ? item.editCenterX:Number(item.editCenterX.toString().match(/^\d+(?:\.\d{0,2})?/))"
                  min="0"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item :prop="'templateEditList.' + index + '.editCenterY'" :rules="rules.editCenterY">
                &nbsp;Y&nbsp;<el-input style="width:80%" size="mini" v-model="item.editCenterY" type="number"
                  @input="item.editCenterY=/^\d+\.?\d{0,2}$/.test(item.editCenterY)||item.editCenterY == '' ? item.editCenterY:Number(item.editCenterY.toString().match(/^\d+(?:\.\d{0,2})?/))"
                  min="0"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item :prop="'templateEditList.' + index + '.width'" :rules="rules.editWidth">
                &nbsp;宽&nbsp;<el-input style="width:76%" size="mini" v-model="item.width" type="number"
                  @input="item.width=/^\d+\.?\d{0,2}$/.test(item.width)||item.width == '' ? item.width:Number(item.width.toString().match(/^\d+(?:\.\d{0,2})?/))"
                  min="0"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item :prop="'templateEditList.' + index + '.length'" :rules="rules.editLength">
                &nbsp;长&nbsp;<el-input style="width:76%" size="mini" v-model="item.length" type="number"
                  @input="item.length=/^\d+\.?\d{0,2}$/.test(item.length)||item.length == '' ? item.length:Number(item.length.toString().match(/^\d+(?:\.\d{0,2})?/))"
                  min="0"></el-input>
              </el-form-item>
            </el-col>
            <el-button v-if="index !== 0" class="mini-delete-btn" @click="handleDeleteEdit(index)">删除</el-button>
          </el-form-item>
          <el-form-item label="内部编辑图" :prop="'templateEditList.' + index + '.internalEditUrl'"
            :rules="rules.internalEditUrl">
            <div @click="getImageTypeIndex(index)">
              <el-upload class="avatar-uploader" :action="action" :show-file-list="false" :on-remove="handleRemove"
                :on-success="handleSuccessEditUrl" :before-upload="beforeUploadEditUrl">
                <img v-if="item.internalEditUrl" :src="item.internalEditUrl" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </div>
          </el-form-item>
        </div>
        <el-form-item v-show="!this.$route.params.id">
          <el-tooltip content="点击新增可编辑区域中心点以及内部编辑图" placement="top">
            <el-button class="mini-search-btn" @click="handleAddEdit">+ 新增可编辑区域</el-button>
          </el-tooltip>
        </el-form-item>
      </div>

      <el-form-item label="主题链接" prop="themeUrl">
        <el-tooltip content="主题链接，针对主题图片需维护主题链接" placement="top">
          <el-input size="mini" style="width:500px;" v-model="formData.themeUrl"></el-input>
        </el-tooltip>
      </el-form-item>
      <el-form-item label="图片描述" prop="description">
        <el-input style="width:50%;" size="mini" type="textarea" :rows="4" v-model="formData.description"
          placeholder="不超过200个字" maxlength="200"></el-input>
      </el-form-item>
      <el-form-item label="状态" prop="openFlag">
        <el-radio-group v-model="formData.openFlag" size="mini">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">停用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="适用材质：" style="margin-bottom: 0px;">
        <el-table :data="materialTable" border header-row-class-name="header-row" class="tableCenter goods-table"
          style="width:60%" max-height="400" ref="multipleSelectMaterial" @select="selectMaterial" @select-all="selectMaterialAll" 
           @selection-change="handleSelecMaterialChange">
          <el-table-column type="selection" width="50" align="center"></el-table-column>
          <el-table-column align="center" label="材质编码" prop="materialNo"></el-table-column>
          <el-table-column align="center" label="材质名称">
            <template slot-scope="scope">
              <div>{{scope.row.parentName}}-{{scope.row.name}}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="状态" prop="openFlag" :formatter="formatStatus"> </el-table-column>
        </el-table>
        <page style="width:60%" :page="pageInfo.page" :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
      </el-form-item>
      <el-form-item label="标签设置：" style="margin-bottom: 0px;">
        <el-button @click="addLabels" class="mini-search-btn"><i class="el-icon-plus el-icon--left"></i>添加标签</el-button>
      </el-form-item>
      <el-form-item>
        <el-table :data="labelsTable" border header-row-class-name="header-row" class="tableCenter goods-table"
          style="width:60%" max-height="400">
          <el-table-column align="center" label="标签名称" prop="name"> </el-table-column>
          <el-table-column align="center" label="标签类型" prop="type" :formatter="formatType"> </el-table-column>
          <el-table-column align="center" label="状态" prop="openFlag" :formatter="formatStatus"></el-table-column>
          <el-table-column align="center" key="8" label="操作" width="150">
            <template slot-scope="scope">
              <el-button class="mini-delete-btn" @click="handleDeleteLabels(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
      <el-form-item label="适用型号" prop="modelScope" style="margin-bottom: 0px;">
        <el-radio-group v-model="formData.modelScope" size="mini">
          <el-radio :label="1">全部可用</el-radio>
          <el-radio :label="3">指定类型可用</el-radio>
          <el-radio :label="2">指定型号可用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-show="formData.modelScope === 3" style="margin-bottom: 0px;">
        <el-checkbox-group style="margin-bottom: 0px;" v-model="formData.productCategoryIdList">
          <el-checkbox :label="item.id" :key="item.id" v-for="item in categoryTypes">{{item.name}}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item v-show="formData.modelScope === 2">
        <el-select size="mini" class="modelSelect" v-model="searchModel.categoryId" placeholder="请选择" clearable>
          <el-option v-for="item in categoryTypes" :key="item.id" :label="item.name" :value="item.id">
          </el-option>
        </el-select>
        <el-input size="mini" clearable @focus="categoryFocus" @blur="focus=false" placeholder="请选择型号分类筛选"
          class="category-input" v-model="searchModel.parentName"></el-input>
        <transition name="el-zoom-in-top">
          <div v-clickoutside="handleClose" class="category-box" v-if="categoryShow">
            <el-tree :data="modelCategoryList" @node-click="categorySelect" :props='props' ref="tree">
            </el-tree>
          </div>
        </transition>
        <el-table :data="modelList" tooltip-effect="dark" ref="multipleSelectMode" @select="select"
          @select-all="selectAll" @selection-change="handleSelectionChange" border header-row-class-name="header-row"
          class="tableCenter goods-table" style="width:60%" max-height="400">
          <el-table-column type="selection" width="50" align="center"></el-table-column>
          <el-table-column align="center" label="型号ID" prop="id"> </el-table-column>
          <el-table-column align="center" label="型号名称" prop="name"> </el-table-column>
          <el-table-column align="center" label="型号类型" prop="categoryName"> </el-table-column>
          <el-table-column align="center" label="状态" prop="openFlag" :formatter="formatStatus" show-overflow-tooltip>
          </el-table-column>
        </el-table>
        <page style="width:60%" :page="searchModel.page" :total="mtotal" @sizeChange="sizeModelChange" @currentChange="currentModelChange"></page>
      </el-form-item>
      <distributor ref="distributor" :distributorType="formData.resellerScope" :distributors="distributors"
        @change="getChange" :disabled="false"></distributor>
      <el-form-item label="不适用范围：" style="width:800px" max-height="400">
        <div>
          <el-button icon="el-icon-plus" class="mini-search-btn" @click="distributorShow=true">
            添加分销商
          </el-button>
          <el-table :data="distributorData" border header-row-class-name="header-row" max-height="600"
            class="tableCenter">
            <el-table-column align="center" label="分销商用户名" prop="name"></el-table-column>
            <el-table-column align="center" label="公司名" prop="companyName"></el-table-column>
            <el-table-column align="center" label="操作" width="80">
              <template slot-scope="scope">
                <el-button style="margin-top:0px;margin-bottom:0px;" class="mini-delete-btn"
                  @click="handleDeleteDistributor(scope.$index)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-form-item>
      <el-button style="margin-left: 25%; margin-top:30px;" class="mini-search-btn" :loading="loading"
        @click="handleSubmit('formData')">保存</el-button>
      <el-button style="margin-bottom:30px;" size="mini" class="mini-search-btn" @click="handleCancel">返回</el-button>
    </el-form>
    <!-- 导入标签dialog -->
    <el-dialog :modal-append-to-body="false" :visible="addLabel" :before-close="cancelLabels" width="60%">
      <selectLabel :selectItemsData="labelsTable" ref="selectLabels" @submit="closeLabels" @cancel="closeLabels">
      </selectLabel>
    </el-dialog>
    <el-dialog :modal-append-to-body="false" :visible="distributorShow" :before-close="disCancel" width="80%">
      <select-distributor :distributorData="distributorData" ref="selectDistributor" @cancel="cancel"
        @submit="disSubmit"> </select-distributor>
    </el-dialog>
  </div>
</template>
<script>
  import selectLabel from './selectLabel.vue'
  import distributor from '@/views/material/components/distributor'
  import selectDistributor from "@/views/goods/components/selectDistributorAll"
  import page from "@/components/pagination"
  import {setArr2} from '@/utils/common.js'
  import {monthDay} from '@/utils/timeFormat.js'
  export default {
    data() {
      return {
        action: process.env.BASE_API + 'system/v1/web/admin/oss/sts',
        loading: false,
        vloading: false,
        append: true, // 添加材料
        looking: false, // 查看材料
        types: [{
          type: 1,
          label: '普通素材',
          disabled: false
        }, {
          type: 2,
          label: 'IP素材',
          disabled: false
        }, {
          type: 3,
          label: '模板',
          disabled: false
        }, {
          type: 4,
          label: '贴图',
          disabled: false
        }],
        categoryTypes: [],
        formData: {
          name: '',
          englishName: '',
          type: '',
          description: '',
          sequence: 0,
          originImage: '',
          thumbnail: '',
          backgroundPreviewUrl: '',
          noCameraVacancyPreview: '',
          content: '',
          openFlag: 1,
          atLastTrademark: 1,
          modelScope: 1,
          resellerScope: 1,
          distributorIds: '',
          productCategoryIdList: [], // 指定产品类型
          materialIdList: [], // 适用材质
          distributorIdApplyList: [], // 适用分销商
          distributorIdExcludeList: [], // 不适用分销商
          // distributors:[],
          templateCenterX: '', // 模板中心点x
          templateCenterY: '', // 模板中心点y
          templateEditList: [
          //   {
          //   id: '',
          //   editCenterX: '',
          //   editCenterY: '',
          //   length: '',
          //   width: '',
          //   internalEditUrl: ''
          // }
          ], // 模板热区信息
        },
        rules: {
          name: [{
            required: true,
            message: '请输入图片名称',
            trigger: 'blur'
          }],
          categoryId: [{
            required: true,
            message: '请选择图片分类',
            trigger: 'blur'
          }],
          type: [{
            required: true,
            message: '请选择图片类型',
            trigger: 'blur'
          }],
          sequence: [{
            required: true,
            message: '请输入排序',
            trigger: 'blur'
          }],
          originImage: [{
            required: true,
            message: '请选择原图',
            trigger: 'blur'
          }],
          backgroundPreviewUrl: [{
            required: true,
            message: '请选择带背景色预览图',
            trigger: 'blur'
          }],
          noCameraVacancyPreview: [{
            required: true,
            message: '请选择无相机空位预览图',
            trigger: 'blur'
          }],
          templateCenterX: [{
            required: true,
            message: '请输入x轴坐标',
            trigger: 'blur'
          }],
          templateCenterY: [{
            required: true,
            message: '请输入y轴坐标',
            trigger: 'blur'
          }],
          editCenterX: [{
            required: true,
            message: '请输入x轴坐标',
            trigger: 'blur'
          }],
          editCenterY: [{
            required: true,
            message: '请输入y轴坐标',
            trigger: 'blur'
          }],
          editLength: [{
            required: true,
            message: '请输入长度值',
            trigger: 'blur'
          }],
          editWidth: [{
            required: true,
            message: '请输入宽度值',
            trigger: 'blur'
          }],
          internalEditUrl: [{
            required: true,
            message: '请选择内部编辑图',
            trigger: 'change'
          }]
        },
        disShow: false,
        imageObj: '',
        focus: false,
        categoryShow: false,
        props: {
          label: 'name',
          children: 'childrenList',
          isLeaf: 'leaf'
        },
        pageInfo: {
          page: 1,
          size: 10,
          categoryId: undefined,
          categoryName: undefined
        },
        total:0,
        addGoodsItem: false,
        addLabel: false,
        pictureCategoryList: [],
        modelList: [],
        searchModel: {
          page:1,
          size: 10,
          parentName: '',
          parentId: undefined,
          categoryId: undefined,
          atLastTrademark: 1
        },
        mtotal: 0,
        materialIds: [], // 选中材质列表
        isSelect: false,
        isMaterialSelect: false, 
        distributors: [],
        models: [],
        materialTable: [], // 材质列表
        labelsTable: [],
        modelCategoryList: [], // 型号分类列表
        category: null,
        distributorData: [],
        distributorShow: false,
        uploadImageType: 0, // 当前内部编辑图index
      }
    },
    components: {
      selectLabel,
      distributor,
      selectDistributor,
      page
    },
    created() {
      this.getModel() // 获取机型
      let p1 = this.getPictureCategory() // 获取图片分类
      let p2 = this.getProCategoryList() // 获取产品类型
      let p3 = this.getMaterialList() // 获取适用材质
      let p4 = this.getModelCategoryList() // 获取型号分类列表
      Promise.all([p1,p2,p3,p4]).then(result => {
        this.getData() // 获取详情
      })
    },
    directives: { //..事件绑定
      clickoutside: {
        bind: function (el, binding, vnode) {
          function documentHandler(e) {
            if (el.contains(e.target)) { //..这里判断点击的元素是否是本身，是本身，则返回
              return false;
            }
            if (binding.expression) { //..判断指令中是否绑定了函数
              binding.value(e) //..如果绑定了函数 则调用那个函数，此处binding.value就是handleClose方法
            }
          }
          el._vueClickOutside_ = documentHandler; //..给当前元素绑定个私有变量，方便在unbind中可以解除事件监听
          document.addEventListener('click', documentHandler);
        },
        unbind: function (el, binding) {
          document.removeEventListener('click', el._vueClickOutside_);
          delete el._vueClickOutside_;
        }
      }
    },
    methods: {
      // 获取型号列表数据
      getModel() { 
        this.$http.modelAllList(this, this.searchModel).then(res => {  
          if (res.success) {
            this.modelList = []
            if (res.data.list && res.data.list !== undefined && res.data.list !== null && res.data.list.length > 0) {
              this.modelList = res.data.list
              this.mtotal = res.data.total <= 10 ? undefined : res.data.total
              this.selectModelList()
            }
          }
        })
      },
      // 图片分类
      getPictureCategory() {
        return new Promise((resolve, reject) => {
          this.$http.pictureCategoryPoList(this, {
            page:1,
            size:1000, 
            atLastTrademark: 1,
            openFlag: 1
          }).then(res => {
            if (res.success) {
              if (res.data.list !== undefined && res.data.list.length > 0) {
                res.data.list.forEach(item => {
                  if (item.openFlag === 0) {
                    item.disabled = true
                  }
                })
                this.pictureCategoryList = res.data.list
              }
              resolve(true)
            }
          })
        })
      },
      // 型号分类列表
      getModelCategoryList() {
        return new Promise((resolve, reject) => {
          this.$http.modelPoList(this, {
            size:1000,
            page:1,
            parentId: 0,
            // atLastTrademark: 0,
            openFlag: 1
          }).then(res => {
            if (res.success) {
              this.modelCategoryList = res.data.list
              resolve(true)
            } else {
              reject()
            }
          })
        })
      },
      // 材质列表
      getMaterialList () {
        return new Promise((resolve, reject) => {
          this.$http.materialLowestList(this, this.pageInfo).then(res => {
            if (res.success) {
              this.materialTable = res.data.list
              this.total = res.data.total <= 10 ? undefined : res.data.total
              resolve(true)
            } else {
              reject()
            }
          })
        })
      },
      getProCategoryList() {
        return new Promise((resolve, reject) => {
          this.$http.productUsableList(this).then(res => {
            if (res.success) {
              this.categoryTypes = res.data
              resolve(true)
            } else {
              reject()
            }
          })
        })
      },
      // 修改图片分类，设置可选图片类型
      changeType(value) {
        let curType = 0;
        // 获取当前图片类型
        this.pictureCategoryList.forEach((item) => {
          if (item.id === value) {
            curType = item.type;
            this.formData.type = curType;
          }
        })
        if (curType !== 0) {
          // 设置当前图片类型，其他不可选
          this.types.forEach((item) => {
            if (item.type === this.formData.type) {
              item.disabled = false;
            } else {
              item.disabled = true;
            }
          })
        } else {
          // 未获取到图片类型，全部可选
          this.types.forEach((item) => {
            item.disabled = false;
          })
        }
      },
      // 不适用分销商
      initDistributor() {
        this.distributorData = []
        if (this.formData.distributorExcludeList !== undefined && this.formData.distributorExcludeList !== null && this.formData
          .distributorExcludeList.length > 0) {
          this.formData.distributorExcludeList.forEach(item => {
            this.distributorData.push({
              id: item.distributorId,
              name: item.distributorName,
              companyName: item.distributorCompanyName
            })
          })
        }
      },
      handleDeleteDistributor(index) {
        this.distributorData.splice(index, 1)
      },
      selectMaterialList () {
        if (this.materialIds.length > 0 && this.materialIds.length > 0 && this.$refs.multipleSelectMaterial !== undefined && this
          .$refs.multipleSelectMaterial !== null) {
          this.materialIds.forEach(row1 => {
            this.materialTable.forEach(row2 => {
              if (row1 === row2.id) {
                this.$nextTick(() => {
                  this.$refs.multipleSelectMaterial.toggleRowSelection(row2);
                })
              }
            })
          })
        }
      },
      selectMaterial(selection, row){ // 材质单选时调用
        this.isMaterialSelect = true
        let d = false
        for(let i = 0;i<this.materialIds.length;i++){
          if(this.materialIds[i] === row.id){
            this.materialIds.splice(i,1)
            d = true
            break
          }
        }
        if(!d){
          this.materialIds.push(row.id)
          this.materialIds = setArr2(this.materialIds)
        }
      },
      selectMaterialAll(selection){ // 材质全选时调用
        this.isMaterialSelect = true
        if(selection.length === 0){
          this.materialTable.forEach(row => {
            for(let i = 0;i<this.materialIds.length;i++){
              if(this.materialIds[i] === row.id){
                this.materialIds.splice(i,1)
                break
              }
            }
          })
        }else{
          selection.forEach(item =>{
            this.materialIds.push(item.id)
          })
          this.materialIds = setArr2(this.materialIds)
        }
      },
      handleSelecMaterialChange(val) { // 材质当切换页面时的作用
        if(val.length === 0 && this.materialIds.length != 0 && !this.isSelect){
          this.materialIds.forEach(row1 => {
            this.materialTable.forEach(row2 => {
              if(row1 === row2.id){
                this.$refs.multipleSelectMaterial.toggleRowSelection(row2);
              }
            })
          });
          this.isSelect = true
        }
      },
      // 材质
      sizeChange(size) {
        this.pageInfo.page = 1
        this.pageInfo.size = size;
        this.getPictureList()
      },
      // 材质
      currentChange(page) {
        this.pageInfo.page = page;
        this.getPictureList()
      },
      // 型号
      sizeModelChange(size) {
        this.searchModel.page = 1
        this.searchModel.size = size;
        this.getModel()
      },
      // 型号
      currentModelChange(page) {
        this.searchModel.page = page;
        this.getModel()
      },
      disSubmit(msg) {
        this.distributorData = msg;
        this.distributorShow = false;
      },
      disCancel() {
        this.$refs.selectDistributor.handleCancel()
      },
      cancel() {
        this.distributorShow = false;
      },
      formatStatus(row, col, val) {
        switch (val) {
          case 0:
            return "停用";
            break;
          case 1:
            return "启用";
            break;
        }
      },
      formatType(row, col, val) {
        switch (val) {
          case 1:
            return "标准标签";
            break;
          case 2:
            return "定制标签";
            break;
        }
      },
      getChange(val) {
        this.formData.resellerScope = val.distributorScope
        this.distributors = []
        this.distributors = this.distributors.concat(val.distributorData)
      },
     
      selectModelList() {
        if (this.modelList.length > 0 && this.models.length > 0 && this.$refs.multipleSelectMode !== undefined && this
          .$refs.multipleSelectMode !== null) {
          this.models.forEach(row1 => {
            this.modelList.forEach(row2 => {
              if (row1 === row2.id) {
                this.$nextTick(() => {
                  this.$refs.multipleSelectMode.toggleRowSelection(row2);
                })
              }
            })
          })
        }
      },
      selectRow() { // 请求数据变化时，重新选择行（如，删除、数据变化）
        this.$refs.multipleSelectMode.clearSelection();
        this.selectModelList()
      },
      select(selection, row) { // 型号单选时调用
        this.isSelect = true
        let d = false
        for (let i = 0; i < this.models.length; i++) {
          if (this.models[i] === row.id) {
            this.models.splice(i, 1)
            d = true
            break
          }
        }
        if (!d) {
          this.models.push(row.id)
          this.models = setArr2(this.models)
        }
      },

      selectAll(selection) { // 型号全选时调用
        this.isSelect = true
        if (selection.length === 0) {
          this.modelList.forEach(row => {
            for (let i = 0; i < this.models.length; i++) {
              if (this.models[i] === row.id) {
                this.models.splice(i, 1)
                break
              }
            }
          })
        } else {
          selection.forEach(item => {
            this.models.push(item.id)
          })
          this.models = setArr2(this.models)
        }
      },
      handleSelectionChange(val) { // 型号当切换页面时的作用
        if (val.length === 0 && this.models.length != 0 && !this.isSelect && this.modelCategory !== undefined && this
          .modelCategory !== null && this.modelCategory.length > 0) {
          this.models.forEach(row1 => {
            this.modelCategory.forEach(row2 => {
              if (row1 === row2.id) {
                this.$refs.multipleSelectMode.toggleRowSelection(row2);
              }
            })
          });
          this.isSelect = true
        }
      },
      handleClose() {
        if (this.categoryShow && !this.focus) {
          this.categoryShow = false
        }
      },
      categoryFocus() {
        this.focus = true
        this.categoryShow = true
      },
      getData() {
        if (this.$route.params.id != undefined) {
          this.vloading = true
          this.append = false;
          this.looking = true;
          this.$http.pictureDetail(this, {id: this.$route.params.id}).then(res => {
            if (res.success) {
              // 指定产品类型
              if (res.data.productCategoryRelaList === undefined) {
                res.data.productCategoryIdList = []
              } else {
                 res.data.productCategoryIdList = res.data.productCategoryRelaList
              }
              this.formData = res.data
              // 适用范围
              this.distributors = []
              if (this.formData.resellerScope === 4 && this.formData.distributorApplyList !== undefined &&
                this.formData.distributorApplyList !== null && this.formData.distributorApplyList !== '' && this.formData
                .distributorApplyList.length > 0) { // 指定分销商
                // this.distributors = JSON.parse(JSON.stringify(this.formData.distributorApplyList))
                this.formData.distributorApplyList.forEach(item => {
                  this.distributors.push({
                    id: item.distributorId,
                    name: item.distributorName,
                    companyName: item.distributorCompanyName
                  })
                })
              }
              // 适用型号
              this.models = []
              if (this.formData.modelScope === 2 && this.formData.modelApplyList !== undefined &&
                this.formData.modelApplyList !== null && this.formData.modelApplyList !== '' && this.formData.modelApplyList.length > 0) {
                // this.models = JSON.parse(JSON.stringify(this.formData.modelApplyList))
                this.formData.modelApplyList.forEach(item => {
                  this.models.push(item.modelId)
                })
                this.selectModelList()
              } else if (this.formData.modelScope === 3 && this.formData.productCategoryIdList && this.formData.productCategoryIdList.length>0) {
                this.formData.productCategoryIdList = []
                this.formData.productCategoryRelaList.forEach(item => {
                  this.formData.productCategoryIdList.push(item.productCategoryId)
                })
                // this.formData.productCategoryIdList = JSON.parse(JSON.stringify(this.formData.productCategoryIdList))
              }
              // 标签
              this.labelsTable = []
              if (this.formData.labelRelaList !== undefined &&
                this.formData.labelRelaList !== null && this.formData.labelRelaList !== '' && this.formData.labelRelaList.length > 0) {
                // this.labelsTable = JSON.parse(JSON.stringify(this.formData.labelRelaList))
                this.formData.labelRelaList.forEach(item => {
                  this.labelsTable.push({
                    id: item.labelId,
                    name: item.name,
                    type: item.type,
                    openFlag: item.openFlag
                  })
                })
              }

              // 适用材质
              this.materialIds = []
              if (this.formData.materialRelaList && this.formData.materialRelaList.length>0) {
                // this.materialIds = JSON.parse(JSON.stringify(this.formData.materialRelaList))
                this.formData.materialRelaList.forEach(item => {
                  this.materialIds.push(item.materialId)
                })
                this.selectMaterialList()
              }

              // 设置当前图片类型，其他不可选
              this.types.forEach((item) => {
                if (item.type === this.formData.type) {
                  item.disabled = false;
                } else {
                  item.disabled = true;
                }
              })
            }
            this.vloading = false
            // 不适用分销商
            this.initDistributor()
          })
        } else {
          this.looking = false;
          this.initDistributor()
        }
      },
      
      // 删除标签
      handleDeleteLabels(index) {
        this.labelsTable.splice(index, 1)
      },
      // 取消标签
      cancelLabels() {
        this.$refs.selectLabels.handleCancel()
      },
      // 关闭标签
      closeLabels(val) {
        this.addLabel = false
        this.labelsTable = []
        this.labelsTable = this.labelsTable.concat(val)
      },
      // 标签
      addLabels() {
        this.addLabel = true
      },
      // 选择型号
      categorySelect(data, checked, indeterminate) {
        this.searchModel.parentName = data.name
        this.searchModel.parentId = data.id
        this.categoryShow = false
      },
      categoryFocus() {
        this.focus = true
        this.categoryShow = true
      },
      // 原图
      beforeUploadOriginImage(file) {
        if (file.type != 'image/jpeg' && file.type != 'image/bmp' &&
          file.type != 'image/jpg' && file.type != 'image/jpeg' &&
          file.type != 'image/png' && file.type != 'image/gif') {
          this.$message.error('上传图片只能是bmp、jpg、jpeg、png、gif格式!')
          return false
        }
        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
          return isLt2M
        }
        // 随机命名
        let random_name = this.random_string(6) + '_' + new Date().getTime() + '.' + file.name.split('.').pop()
        this.$http.getFileSts(this).then(result => { 
		  console.log(result);
          const client = new OSS.Wrapper({
            region: result.data.region,
            accessKeyId: result.data.accessKeyId,
            accessKeySecret: result.data.accessKeySecret,
            stsToken: result.data.securityToken,
            bucket: result.data.bucketName,
            endpoint: result.data.endpoint,
            secure: true
          })
		  
          // 上传
          client.multipartUpload('flexible/picture/' + monthDay(new Date()) + '/' + random_name, file, {}).then((results) => {
            return new Promise((resolve, reject) => {
			  console.log(result.data.hostname + results.name);
              this.formData.originImage = result.data.hostname + results.name,
              resolve(true)
            })
          }).catch((err) => {
            console.log(err)
          })
        })
      },
      // 缩略图
      beforeUploadThumbnail(file) {
        if (file.type != 'image/jpeg' && file.type != 'image/bmp' &&
          file.type != 'image/jpg' && file.type != 'image/jpeg' &&
          file.type != 'image/png' && file.type != 'image/gif') {
          this.$message.error('上传图片只能是bmp、jpg、jpeg、png、gif格式!')
          return false
        }
        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
          return isLt2M
        }
        // 随机命名
        let random_name = this.random_string(6) + '_' + new Date().getTime() + '.' + file.name.split('.').pop()
        this.$http.getFileSts(this).then(result => { 	
          const client = new OSS.Wrapper({
            region: result.data.region,
            accessKeyId: result.data.accessKeyId,
            accessKeySecret: result.data.accessKeySecret,
            stsToken: result.data.securityToken,
            bucket: result.data.bucketName,
            endpoint: result.data.endpoint,
            secure: true
          })
          // 上传
          client.multipartUpload('flexible/picture/' + monthDay(new Date()) + '/' + random_name, file, {}).then((results) => {
            return new Promise((resolve, reject) => {
              this.formData.thumbnail = result.data.hostname + results.name,
                resolve(true)
            })
          }).catch((err) => {
            console.log(err)
          })
        })
      },
      // 带背景色预览图
      beforeUploadBackUrl(file) {
        if (file.type != 'image/jpeg' && file.type != 'image/bmp' &&
          file.type != 'image/jpg' && file.type != 'image/jpeg' &&
          file.type != 'image/png' && file.type != 'image/gif') {
          this.$message.error('上传图片只能是bmp、jpg、jpeg、png、gif格式!')
          return false
        }
        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
          return isLt2M
        }
        // 随机命名
        let random_name = this.random_string(6) + '_' + new Date().getTime() + '.' + file.name.split('.').pop()
        this.$http.getFileSts(this).then(result => { 	
          const client = new OSS.Wrapper({
            region: result.data.region,
            accessKeyId: result.data.accessKeyId,
            accessKeySecret: result.data.accessKeySecret,
            stsToken: result.data.securityToken,
            bucket: result.data.bucketName,
            endpoint: result.data.endpoint,
            secure: true
          })
          // 上传
          client.multipartUpload('flexible/picture/' + monthDay(new Date()) + '/' + random_name, file, {}).then((results) => {
            return new Promise((resolve, reject) => {
              this.formData.backgroundPreviewUrl = result.data.hostname + results.name,
               console.log('etn==', this.formData.backgroundPreviewUrl);
                resolve(true)
            })
          }).catch((err) => {
            console.log(err)
          })
        })
      },
       // 无相机空位预览图
      beforeUploadNoCamera(file) {
        if (file.type != 'image/jpeg' && file.type != 'image/bmp' &&
          file.type != 'image/jpg' && file.type != 'image/jpeg' &&
          file.type != 'image/png' && file.type != 'image/gif') {
          this.$message.error('上传图片只能是bmp、jpg、jpeg、png、gif格式!')
          return false
        }
        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
          return isLt2M
        }
        // 随机命名
        let random_name = this.random_string(6) + '_' + new Date().getTime() + '.' + file.name.split('.').pop()
        this.$http.getFileSts(this).then(result => { 	
          const client = new OSS.Wrapper({
            region: result.data.region,
            accessKeyId: result.data.accessKeyId,
            accessKeySecret: result.data.accessKeySecret,
            stsToken: result.data.securityToken,
            bucket: result.data.bucketName,
            endpoint: result.data.endpoint,
            secure: true
          })
          // 上传
          client.multipartUpload('flexible/picture/' + monthDay(new Date()) + '/' + random_name, file, {}).then((results) => {
            return new Promise((resolve, reject) => {
              this.formData.noCameraVacancyPreview = result.data.hostname + results.name,
                resolve(true)
            })
          }).catch((err) => {
            console.log(err)
          })
        })
      },
      // 随机生成文件名
      random_string(len) {
        len = len || 32;
        var chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';
        var maxPos = chars.length;
        var pwd = '';
        for (let i = 0; i < len; i++) {
          pwd += chars.charAt(Math.floor(Math.random() * maxPos));
        }
        return pwd;
      },
      handleRemove(file) {
        this.formData.image = ''
      },
      // 保存
      handleSubmit(data) {
        this.$refs[data].validate(valid => {
          if (valid) {
            this.loading = true
            // 图片价格未填默认为0
            this.formData.copyrightCost = (this.formData.copyrightCost === undefined || this.formData.copyrightCost === '') ? 0 : this.formData.copyrightCost
            // 不适用范围处理
            this.formData.distributorIdExcludeList = []
            if (this.distributorData !== undefined && this.distributorData !== null && this.distributorData.length >
              0) {
              this.distributorData.forEach(item => {
                this.formData.distributorIdExcludeList.push(item.id)
              })
            }
            // 适用范围
            let distributors1 = []
            this.formData.resellerScope = this.$refs.distributor.formData.distributorScope
            if (this.$refs.distributor.formData.distributorData !== undefined && this.$refs.distributor.formData
              .distributorData !== null && this.$refs.distributor.formData.distributorData.length > 0) {
              distributors1 = JSON.parse(JSON.stringify(this.$refs.distributor.formData.distributorData))
            }
            if (this.formData.modelScope === 2 && (this.models === undefined || this.models === null || this
                .models === '' || this.models.length === 0)) {
              this.$message.error("至少指定一个适用型号可用")
              this.loading = false
              return
            }
            if (this.formData.modelScope === 3 && (this.formData.productCategoryIdList === undefined || this.formData
                .productCategoryIdList === null || this.formData.productCategoryIdList === '' || this.formData
                .productCategoryIdList.length === 0)) {
              this.$message.error("至少指定一个型号类型可用")
              this.loading = false
              return
            }
            if (this.formData.resellerScope === 4) { // 指定分销商
              if (distributors1 === undefined || distributors1 === null || distributors1 === '' || distributors1
                .length === 0) {
                this.$message.error("至少指定一个分销商")
                this.loading = false
                return
              }
              this.formData.distributorIdApplyList = []
              if (distributors1 !== undefined && distributors1 !== null && distributors1 !== '' && distributors1
                .length > 0) { // 添加可视范围
                distributors1.forEach(item => {
                  this.formData.distributorIdApplyList.push(item.id)
                })
              }
            }
            // 适用型号
            if (this.formData.modelScope === 2 && this.models !== undefined && this.models !== null && this
              .models !== '' && this.models.length > 0) { // 添加适用型号
              this.formData.modelIdApplyList = []
              this.formData.modelIdApplyList = this.models
            }
            // 适用材质
            if (this.materialIds.length > 0) {
              this.formData.materialIdList = this.materialIds
            }
            // 模板
            if (this.formData.type === 2) {
              this.formData.templateEditList = undefined
            }
            // 标签类型
            this.formData.labelIdList = []
            if (this.labelsTable !== undefined && this.labelsTable !== null && this.labelsTable !== '' && this
              .labelsTable.length !== 0) {
              this.labelsTable.forEach(item => {
                this.formData.labelIdList.push(item.id)
              })
            }

            
            if (this.formData.id !== undefined && this.formData.id !== null && this.formData.id !== '') { // 编辑	
              // 删除不必要数据
              delete this.formData.distributorExcludeList
              delete this.formData.labelRelaList
              delete this.formData.modelApplyList
              delete this.formData.materialRelaList
              delete this.formData.productCategoryRelaList

              this.$http.editPicture(this, this.formData).then(res => {   
                this.loading = false
                if (res.success) {
                  this.$message.success("编辑成功")
                  this.$router.go(-1)
                }
              })
            } else { // 新增
              // if (this.labelsTable !== undefined && this.labelsTable !== null && this.labelsTable !== '' && this
              //   .labelsTable.length > 0) { // 添加标签设置
              //   this.formData.labelIdList = []
              //   this.labelsTable.forEach(item => {
              //     this.formData.labelIdList.push(item.id)
              //   })
              // }
              // if (this.formData.modelScope === 2 && this.models !== undefined && this.models !== null && this
              //   .models !== '' && this.models.length > 0) { // 添加适用型号
              //   this.formData.modelIdApplyList = []
              //   this.models.forEach(item => {
              //     this.formData.modelIdApplyList.push(item)
              //   })
              // }
              this.$http.addPicture(this, this.formData).then(res => { 
                this.loading = false
                if (res.success) {
                  this.$message.success("新增成功")
                  this.$router.go(-1)
                }
              })
            }
          }
        })
      },
      handleCancel() { // 返回操作
        this.$router.go(-1)
      },
      // 获取当前点击内部编辑图index
      getImageTypeIndex(index) {
        this.uploadImageType = index;
      },
      // 上传内部编辑图
      handleSuccessEditUrl(response, file) {},
      beforeUploadEditUrl(file) {
        if (file.type != 'image/jpeg' && file.type != 'image/bmp' &&
          file.type != 'image/jpg' && file.type != 'image/jpeg' &&
          file.type != 'image/png' && file.type != 'image/gif') {
          this.$message.error('上传图片只能是bmp、jpg、jpeg、png、gif格式!')
          return false
        }
        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
          return isLt2M
        }
        // 随机命名
        let random_name = this.random_string(6) + '_' + new Date().getTime() + '.' + file.name.split('.').pop()
        this.$http.getFileSts(this).then(result => { 	
          const client = new OSS.Wrapper({
            region: result.data.region,
            accessKeyId: result.data.accessKeyId,
            accessKeySecret: result.data.accessKeySecret,
            stsToken: result.data.securityToken,
            bucket: result.data.bucketName,
            endpoint: result.data.endpoint,
            secure: true
          })
          // 上传
          client.multipartUpload('flexible/picture/' + monthDay(new Date()) + '/' + random_name, file, {}).then((results) => {
            return new Promise((resolve, reject) => {
              let curImg = result.data.hostname + results.name;
              this.$set(this.formData.templateEditList[this.uploadImageType], "internalEditUrl", curImg);
              resolve(true)
            })
          }).catch((err) => {
            console.log(err)
          })
        })
      },
      // 新增可编辑区域
      handleAddEdit() {
        let option = {
          id: '',
          editCenterX: '',
          editCenterY: '',
          length: '',
          width: '',
        }
        this.formData.templateEditList.push(option)
      },
      // 删除可编辑区域
      handleDeleteEdit(index) {
        this.formData.templateEditList.splice(index, 1)
      }
    },
    watch: {
      'searchModel.parentName': {
        handler() {
          if (this.searchModel.parentName === undefined || this.searchModel.parentName === null || this.searchModel
            .parentName === '') {
            this.searchModel.parentId = undefined
          }
          this.getModel()
        },
        deep: true
      },
      'formData.modelScope': {
        handler(val) {
          if (val===1 || val===2) {
            this.formData.productCategoryIdList = []
          }
        },
        deep: true
      }
    }
  }

</script>
<style lang="scss" scoped>
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

    .distributor-content {
      margin-top: 10px;
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

    .category-input {
      width: 193px;
    }

    .category-box {
      border: 1px solid #ccc;
      // margin-left: 150px;
      min-width: 193px;
      padding: 10px;
      background-color: white;
      position: absolute;
      left: 0;
      top: 45px;
      z-index: 99;
      border-radius: 10px;
    }
    .modelSelect{
      width:150px;
      /deep/.el-input{
        width:150px;
      }
    }
  }

</style>
