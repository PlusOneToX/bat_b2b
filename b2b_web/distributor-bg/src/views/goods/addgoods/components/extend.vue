<template>
  <div class="extend">
    <el-form>
      <el-form-item label="商品类型">
        <template>
          <el-radio-group v-model="goodType" :disabled="redact" @change="goodTypeChange">
            <el-radio :label="1">普通商品</el-radio>
            <el-radio :label="2">新定制商品</el-radio>
            <!-- <el-radio :label="2">旧定制商品</el-radio> -->
          </el-radio-group>
        </template>
      </el-form-item>
      <!--旧定制商品暂不要--->
      <!-- <el-form-item label="定制类型" v-show="goodType == 3">
        <template>
          <el-radio-group v-model="diyType" :disabled="redact">
            <el-radio :label="0">标准定制</el-radio>
            <el-radio :label="1">DIY定制</el-radio>
          </el-radio-group>
        </template>
      </el-form-item> -->

      <el-form-item label="定制类型" v-show="goodType == 2">
        <template>
          <el-radio-group v-model="diyType" :disabled="redact">
            <el-radio :label="1">DIY定制</el-radio>
          </el-radio-group>
        </template>
      </el-form-item>
      <el-form-item label="材料类型" v-show="goodType == 2">
        <template>
          <el-radio-group v-model="materialType" :disabled="redact">
            <el-radio :label="10010">手机壳</el-radio>
            <el-radio :label="10020">手机背膜</el-radio>
          </el-radio-group>
        </template>
      </el-form-item>
    </el-form>
     <!--货品分组暂不要--->
    <!-- <div v-if="goodType == 3">
      <el-form>
        <el-form-item label="货品分组" label-width="68px">
            <template>
              <div>
                <div v-show="tableDatas.length>0">
                  <div v-for="(item,index) in tableDatas" :key="Number(item.itemId)">
                    <el-input maxlength="20" style="width:150px;" size="mini" placeholder="分组名称" v-model="item.itemGroup"></el-input>
                    <el-input maxlength="50" style="width:300px;" size="mini" placeholder="备注(选填)" v-model="item.itemGroupRemark"></el-input>
                    <el-button v-show="tableDatas.length>1" class="mini-delete-btn" @click.prevent="deleteGroup(index)">删除</el-button>
                    <el-button v-show="tableDatas.length>1 && index>0" class="mini-tableadd-btn" @click="handleUp(index)">上移</el-button>
										<el-button v-show="tableDatas.length>1 && index<(tableDatas.length-1)" class="mini-freeze-btn" @click="handleDown(index)">下移</el-button>
                  </div>
                </div>
                <div>
                  <el-button size="mini" @click.prevent="addGroup()">添加分组</el-button>
                </div>
              </div>
            </template>
        </el-form-item>
      </el-form>
    </div> -->
    <div>
      <div style="padding-bottom:10px;">
        <el-button class="mini-search-btn" @click="addChannel()">导入货品</el-button>
        <el-button class="mini-search-btn" @click="specifacationShow=true">设置规格和颜色</el-button>
        <el-button class="mini-search-btn" :loading="syncItemLoading" v-if="!isAdd" @click="syncItem">同步货品</el-button>
        <el-button class="mini-search-btn" :loading="syncItemBoxLoading" v-if="!isAdd" @click="syncItemBox">同步货品装箱</el-button>
        <el-button class="mini-search-btn" :loading="synPriceLoading" v-if="!isAdd" @click="syncGoodsPrice">同步价格</el-button>
      </div>
      <el-tabs v-if="goodType == 3" type="border-card" v-model="itemId">
        <el-tab-pane  :key="Number(item.itemId)" v-for="item in tableDatas" :label="item.itemGroup" :name="item.itemId"></el-tab-pane>
      </el-tabs>

      <!-- 导入货品的表格数据 -->
      <el-table :data="tableData" border header-row-class-name="header-row" :cell-style="{'text-align':'center'}" class="tableCenter">
        <el-table-column align="center" key="1" label="存货编码" prop="itemCode" fixed :width="140">
          <template slot-scope="scope">
            <el-input size="mini" v-model="scope.row.itemCode" :disabled="disableIsShow"></el-input>
          </template>
        </el-table-column>

        <!-- <el-table-column align="center" v-if="goodType==3" label="工厂编码" prop="productNo" fixed :width="150">
          <template slot-scope="scope">
            <el-input size="mini" v-model="scope.row.productNo" :disabled="disableIsShow"></el-input>
          </template>
        </el-table-column> -->

        <el-table-column align="center" key="2" label="存货名称" prop="itemName" :width="200">
          <template slot-scope="scope">
            <el-input size="mini" v-model="scope.row.itemName" :disabled="disableIsShow"></el-input>
          </template>
        </el-table-column>

        <el-table-column align="center" key="3" label="英文名称" prop="itemNameEn" :width="200">
          <template slot-scope="scope">
            <el-input size="mini" v-model="scope.row.itemNameEn"></el-input>
          </template>
        </el-table-column>

        <!-- 材质 -->
        <!-- <el-table-column align="center" key="4" v-if="goodType===2" label="材质" prop="materialId" :width="120">
          <template slot-scope="scope">
            <el-cascader
              expand-trigger="hover"
              v-model="scope.row.materialId"
              :options="materialList"
              :props="optionProps"
              placeholder="选择材质"
              :title="getMaterialValue(scope.row.materialId)"
               @change="handleChange(scope.$index)">
            </el-cascader>
          </template>
        </el-table-column> -->

        <!-- <el-table-column align="center"  label="品类" prop="categoryName" v-if="false">
          <template slot-scope="scope">
            <el-input size="mini" v-model="scope.row.categoryName" :disabled="disableIsShow"></el-input>
          </template>
        </el-table-column> -->
        <el-table-column align="center" key="5" label="图片" :width="80">
          <template slot-scope="scope">
            <upload-small-img class="avatar-uploader" :item="scope.row" :isEdit ="true" :index="scope.$index" @getImgUrl ="getImgUrl"></upload-small-img>
          </template>
        </el-table-column>

        <el-table-column align="center" key="6" v-if="goodType === 2" label="图库关联" :width="120">
          <template slot-scope="scope" style="text-align: center;">
            <el-button :disabled="scope.row.id === undefined" type="text" @click="relevancePicture(scope.row)" >关联</el-button>
          </template>
        </el-table-column>

        <!-- 规格 -->
        <el-table-column align="center" label="规格" key="7"  :render-header="renderHeader" :width="150">
          <template slot-scope="scope">
            <el-button type="text" @click.stop="cancelB(true)" @click="checkSpecification(scope.$index,$event)" v-if="!scope.row.specsId || scope.row.specsId === '' || scope.row.specsId === undefined">请选择</el-button>
            <span class="el-tag sp-tag" v-else @click.stop="cancelB(true)" @click="checkSpecification(scope.$index,$event)">{{scope.row.specsName !== '' ? scope.row.specsName :'规格已删除'}}</span>
          </template>
        </el-table-column>

        <!-- 颜色 -->
        <el-table-column align="center" label="颜色"  key="8" :render-header="renderHeader" :width="100">
          <template slot-scope="scope">
            <el-button type="text" @click.stop="cancelC(true)" @click="checkColor(scope.$index,$event)" v-if="!scope.row.colorId || scope.row.colorId === '' || scope.row.colorId === undefined">请选择</el-button>
            <span class="el-tag sp-tag" v-else @click.stop="cancelC(true)" @click="checkColor(scope.$index,$event)">{{scope.row.colorName !== '' ? scope.row.colorName :'颜色已删除'}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center"  label="条形码" prop="barCode" :width="140">
          <template slot-scope="scope">
            <el-input size="mini" v-model="scope.row.barCode" :disabled="disableIsShow"></el-input>
          </template>
        </el-table-column>

        <!-- 默认销售价格 -->
        <el-table-column align="center" :render-header="renderHeader" prop="salePrice" :width="140">
          <template slot-scope="scope">
            <i class="asmd">￥:&nbsp;</i>
            {{scope.row.salePrice|NumFormat}}
          </template>
        </el-table-column>

        <!-- 批量下单、装箱规格 -->
        <el-table-column align="center" label="批量下单设置" :width="120" >
          <template slot-scope="scope">
            <el-button v-if="scope.row.boxs && scope.row.boxs.length>0" type="text" @click="setBatchOrder(scope.row)">批量下单</el-button>
            <span v-else>不可设置</span>
          </template>
        </el-table-column>

        <!-- 分销商价 -->
        <el-table-column align="center" key="14" label="分销商价"  :render-header="renderHeader" :width="120">
          <template slot-scope="scope">
            <el-button type="text" @click="setPrice(scope.$index,scope.row)">分销商价</el-button>
          </template>
        </el-table-column>

        <!-- MOQ -->
        <el-table-column align="center" key="15" :render-header="renderHeader"  :width="100">
          <template slot-scope="scope">
            <el-input size="mini" v-model="scope.row.moq" ></el-input>
          </template>
        </el-table-column>
        <!-- 是否支持预售 -->
        <el-table-column align="center" key="16" v-if="goodType===1" label="是否支持预售" prop="advanceSaleFlag" :width="120">
          <template slot-scope="scope">
            <el-select size="mini" v-model="scope.row.advanceSaleFlag" placeholder="请选择">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </template>
        </el-table-column>
        <!-- 直发用户支持在途 -->
        <el-table-column align="center" key="17" v-if="goodType===1" label="直发用户支持在途" prop="onwaySaleFlag" :width="150">
          <template slot-scope="scope">
            <el-select size="mini" v-model="scope.row.onwaySaleFlag" placeholder="请选择">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </template>
        </el-table-column>

        <!-- 生命周期 -->
        <el-table-column align="center" key="18" v-if="goodType===1" label="生命周期" prop="lifeCycle" :width="120">
          <template slot-scope="scope">
            <el-select size="mini" v-model="scope.row.lifeCycle" placeholder="请选择">
              <el-option
                v-for="item in lifeCycles"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </template>
        </el-table-column>

        <el-table-column align="center" key="19" label="重量(克)" prop="weight" :width="100">
          <template slot-scope="scope">
            <el-input size="mini" v-model="scope.row.weight" type="number" @input="scope.row.weight=/^\d+\.?\d{0,2}$/.test(scope.row.weight)||scope.row.weight == '' ? scope.row.weight:Number(scope.row.weight.toString().match(/^\d+(?:\.\d{0,1})?/))" min="0"></el-input>
          </template>
        </el-table-column>

        <el-table-column align="center" key="20" label="长" prop="length" :width="100" v-if="nature">
          <template slot-scope="scope">
            <el-input size="mini" v-model="scope.row.length" type="number" @input="scope.row.length=/^\d+\.?\d{0,2}$/.test(scope.row.length)||scope.row.length == '' ? scope.row.length:Number(scope.row.length.toString().match(/^\d+(?:\.\d{0,1})?/))" min="0"></el-input>
          </template>
        </el-table-column>

        <el-table-column align="center" key="21" label="宽" prop="width" :width="100" v-if="nature">
          <template slot-scope="scope">
            <el-input size="mini" v-model="scope.row.width" type="number" @input="scope.row.width=/^\d+\.?\d{0,2}$/.test(scope.row.width)||scope.row.width == '' ? scope.row.width:Number(scope.row.width.toString().match(/^\d+(?:\.\d{0,1})?/))" min="0"></el-input>
          </template>
        </el-table-column>

        <el-table-column align="center" key="22" label="高" prop="height" :width="100" v-if="nature">
          <template slot-scope="scope">
            <el-input size="mini" v-model="scope.row.height" type="number" @input="scope.row.height=/^\d+\.?\d{0,2}$/.test(scope.row.height)||scope.row.height == '' ? scope.row.height:Number(scope.row.height.toString().match(/^\d+(?:\.\d{0,1})?/))" min="0"></el-input>
          </template>
        </el-table-column>

        <el-table-column align="center" key="23" label="尺寸单位" prop="unit" :width="100">
          <template slot-scope="scope">
            <el-input size="mini" v-model="scope.row.unit"></el-input>
          </template>
        </el-table-column>
        <!-- <el-table-column align="center" key="24" label="装箱规格" prop="unit" :width="100">
          <template slot-scope="scope">
            <el-button type="text" @click="lookBoxInfo(scope.row)" >查看详情</el-button>
          </template>
        </el-table-column> -->

        <!-- 清仓状态 -->
        <el-table-column align="center" key="25" v-if="goodType===1" label="清仓状态" prop="promotionStatus" :width="120">
          <template slot-scope="scope">
            <el-select size="mini" v-model="scope.row.promotionStatus" placeholder="请选择" :disabled="true">
              <el-option
                v-for="item in promotionStatuss"
                :key="item.promotionStatus"
                :label="item.label"
                :value="item.promotionStatus">
              </el-option>
            </el-select>
          </template>
        </el-table-column>

        <el-table-column align="center" key="26" :formatter="formatStatus" label="上架状态" prop="saleStatus" :min-width="100"> </el-table-column>

        <el-table-column align="center" key="27" label="操作" fixed="right" width="150">
          <template slot-scope="scope">
            <el-button class="mini-pulloff-btn" @click="handleShelve(scope.row,1)"  v-if="scope.row.saleStatus === 3"> 下架 </el-button>
            <el-button class="mini-browse-btn" @click="handleShelve(scope.row,3)" v-if="scope.row.saleStatus === 1"> 上架 </el-button>
            <el-button class="mini-delete-btn" @click="handleDel(scope.row)" v-if="scope.row.saleStatus === 1 || scope.row.id === undefined">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 设置规格时弹出的小框 -->
    <el-dialog :modal-append-to-body="false" class="specifacationDialog" :visible="specifacationShow" :before-close="closeDialog" width="80%">
      <select-specification @submit="getSpecification" @cancel="specifacationShow=false" :specificationId="specificationId" :colorId="colorId"></select-specification>
    </el-dialog>

    <!-- 关联图片 -->
    <!-- <el-dialog :modal-append-to-body="false" class="relevancePictureDialog" :visible="relevancePictureShow" :before-close="closeRelevancePictureDialog" width="80%">
      <relevance-picture @submit="closeRelevancePicture" ref="selectRelevancePicture" @cancel="closeRelevancePicture" :selectItem="selectItem"></relevance-picture>
    </el-dialog> -->

    <!-- 分销商价dialog -->
    <el-dialog :modal-append-to-body="false" :visible="priceShow" :show-close="false" :before-close="closeDialog" :close-on-click-modal="false">
      <grade-price @submit="getGprice" :gprices="gprice" :chosenItem="chosenItem" />
    </el-dialog>

    <div class="tag-content1" :style="tagPosition" v-if="specificationData.length>0 && speClick">
      <span v-for="item in specificationData" :key="item.id" class="sp-tag el-tag" @click="handleSelectSpe(item.id,item.name)">{{item.name}}</span>
    </div>
    <div class="tag-content2" :style="tagPosition" v-if="colorData.length>0 && colClick">
      <span v-for="item in colorData" :key="item.id" class="sp-tag el-tag" @click="handleSelectCol(item.id,item.name)">{{item.name}}</span>
    </div>

    <!-- 导入货品dialog -->
    <el-dialog :modal-append-to-body="false" :visible="addGoodsShow" :before-close="closeGoods" width="90%">
      <channel :productlinelist="productlinelist" :goodsId = "goodsId" :selectItemsData="tableData" ref="selectGoodItems" @submit="closeGoods" @cancel="closeGoods"></channel>
    </el-dialog>

    <!-- 装箱规格dialog -->
    <el-dialog :modal-append-to-body="false" :visible="boxInfoShow" :before-close="closeBoxInfo" width="60%">
      <el-table :data="boxInfoList" border :show-header="true" ref="table" header-row-class-name="header-row" class="tableCenter orderGoods" style="text-align:center;">
        <el-table-column align="center" label="箱子名称" prop="boxName"></el-table-column>
        <el-table-column align="center" label="箱子类型" prop="boxType"></el-table-column>
        <el-table-column align="center" label="尺寸(长*宽*高)">
          <template slot-scope="scope">
            {{scope.row.boxLength+'*'+scope.row.boxWidth+'*'+scope.row.boxHeight}}
          </template>
        </el-table-column>
        <el-table-column align="center" label="重量(g)" prop="boxWeight"></el-table-column>
        <el-table-column align="center" label="装箱数量(个)" prop="boxNum"></el-table-column>
      </el-table>
    </el-dialog>

    <!-- 批量下单 -->
    <el-dialog title="批量下单设置" :modal-append-to-body="false" :visible="batchOrderShow" :before-close="closeBatchOrder" width="60%">
      <el-table :data="batchOrderList" border :show-header="true" ref="table" header-row-class-name="header-row" class="tableCenter orderGoods" style="text-align:center;">
        <el-table-column align="center" label="箱子名称" prop="boxName"></el-table-column>
        <el-table-column align="center" label="箱子类型" prop="boxType"></el-table-column>
        <el-table-column align="center" label="尺寸(长*宽*高)">
          <template slot-scope="scope">
            {{scope.row.boxLength+'*'+scope.row.boxWidth+'*'+scope.row.boxHeight}}
          </template>
        </el-table-column>
        <el-table-column align="center" label="重量(g)" prop="boxWeight"></el-table-column>
        <el-table-column align="center" label="装箱数量(个)" prop="boxNum"></el-table-column>
        <el-table-column align="center" label="设为默认下单形式">
          <template slot-scope="scope">
            <el-radio-group v-model="scope.row.defaultFlag" @change="handleDefault(scope.row.id, scope.row.defaultFlag)">
              <el-radio :label="0">否</el-radio>
              <el-radio :label="1">是</el-radio>
            </el-radio-group>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" v-if="batchOrderList.length>1">
          <template slot-scope="scope">
            <el-button
              class="mini-browse-btn"
              @click="handleBoxUp(scope.$index, scope.row)"
            >上移</el-button>
            <el-button
              class="mini-pulloff-btn"
              @click="handleBoxDown(scope.$index, scope.row)"
            >下移</el-button>
          </template>
        </el-table-column>
      </el-table>
      <p class="red-tips">注：默认下单形式只能设置1个，设置后只能以该形式下单，其他下单形式无效。</p>
      <div class="btn-footer">
        <el-button class="mini-search-btn" @click="batchOrderShow = false">取 消</el-button>
        <el-button class="mini-search-btn" @click="batchOrderSubmit" :loading="loading">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script type="text/javascript">
import selectSpecification from "./selectSpecification"
import gradePrice from './gradePrice'
import relevancePicture from './relevancePicture'
import channel from './channel.vue'
import uploadSmallImg from './uploadSmallImg.vue'
import Vue from 'vue'
import { swapItem } from "@/utils/index";
import eventBus from '@/views/order/eventBus'
import api from '@/api/allApi'

// 组件位置 》 商品管理/添加商品/扩展数据
export default {
  props: ['items', 'gradePrices','goodsType','customizedItems','redact','productlinelist','isAdd','diysType','materialsType'],
  data() {
    return {
      disableIsShow: true,
      tableData: [], // 该数据被父组件抓取用于POST
      extendData: [], // 保持数据原来的类型，储存规格的临时数组
      count:'0',
      options: [{
          value: 1,
          label: '是'
        }, {
          value: 0,
          label: '否'
        }],
      lifeCycles: [{
        value: 1,
        label: '导入初期'
      }, {
        value: 2,
        label: '成长期'
      }, {
        value: 3,
        label: '成熟期'
      }, {
        value: 4,
        label: '衰退期'
      }, {
        value: 5,
        label: '项目终止'
      }],
      promotionStatuss: [{
        promotionStatus: '5caebbaa6c7861',
        label: '正常'
      }, {
        promotionStatus: '5caebbb86c7863',
        label: '清仓'
      }, {
        promotionStatus: '5e9fec57c26d75',
        label: 'B'
      }, {
        promotionStatus: '5f0c2feda00210',
        label: 'C'
      }, {
        promotionStatus: '5f0c3003a00212',
        label: 'D'
      }],
      tableDatas:[{
        itemId:'0',
        itemGroup:'默认分组',
        itemGroupRemark:'',
        items:[]
      }],//定制商品数据
      specifacationShow: false,
      specificationData: [],
      colorData: [],
      tagPosition: '',
      speIndex: '',
      colIndex: '',
      priceShow: false,
      speClick: false,
      colClick:false,
      gprice:[],
      chosenItem: {},
      specificationId: "",
      colorId: "",
      addGoodsShow: false,
      checkList: [],
      price: [],
      grades:[],
      nature: true,
      format:'',
      itemId:'0',
      goodType:1,
      diyType:0,
      goodsId:0,
      materialType:10010,
      materialId:'',
      deleteGoods:[],
      synCYLoading:false,
      syncItemLoading: false,
      syncItemBoxLoading: false,
      synPriceLoading:false,
      loading:false,
      row:null,
      index:0,
      boxInfoShow:false,
      boxInfoList:[],
      materialList:[],
      materialValue: '',
      selectItem:{},
      relevancePictureShow:false,
      optionProps: { // 材质
        value: 'id',
        label: 'name',
        colour: 'colour',
        disabled: 'disabled',
        children: 'child'
      },
      // 批量下单
      batchOrderShow: false,
      curItemCode: "",
      batchOrderList: [{
        defaultFlag: 1
      }],
    }
  },
  created(){
    this.getGrade()
    this.getMaterial()
  },
  mounted() {
    eventBus.$on('handleShelveOn', payLoad => {
      // 下架所有货品
      this.$http.updateItemsaleStatus(this, {id:this.row.id, saleStatus: 1}).then(res => {  
          this.loading = false
          if(res.success) {
            this.$message({
              message: this.checkSuccess,
              type: 'success',
              duration: 3 * 1000,
              onClose: () => { }
            })
            this.row.saleStatus = 1
            eventBus.$emit('handleShelveSuccess')
          }
        })
    })
  },
  components: {
    selectSpecification,  gradePrice, channel,uploadSmallImg,relevancePicture
  },
  methods: {
    // 选择材质
    handleChange (index) {
      let materialIdArr = this.tableData[index].materialId
      if (materialIdArr.length > 1) {
        this.tableData[index].materialId = this.tableData[index].materialId[materialIdArr.length - 1]
      } else {
        this.tableData[index].materialId = this.tableData[index].materialId[0]
      }
    },
    getMaterialValue (mid) {
      let value
      for(let i = 0; i < this.materialList.length; i++) {
        if (this.materialList[i].id === mid) {
          value = this.materialList[i].name
          return value
        }
        if (this.materialList[i].child && this.materialList[i].child.length > 0) {
          for(let j = 0; j < this.materialList[i].child.length; j++) {
            if (this.materialList[i].child[j].id === mid) {
              value = this.materialList[i].name + ' - ' + this.materialList[i].child[j].name
              return value
            }
          }
        }
      }
    },
    getMaterial(){
      this.$http.materialTreeList(this, {openFlag: 1, parentId:0}).then(res => {  
        if(res.success){
          if(res.data !== undefined && res.data !== null && res.data.length>0){
            this.materialList = []
            res.data.forEach((item, index) =>{
              this.materialList.push({
                id: item.id,
                name: item.name,
                disabled: item.status===0?true:false
              })
              if (item.childrenList && item.childrenList.length > 0) {
                this.materialList[index].child = []
                item.childrenList.forEach((child, k) => {
                  this.materialList[index].child.push({
                    id: child.id,
                    name: child.name,
                    // colour: child.colour,
                    disabled: child.status===0?true:false
                  })
                })
              }
            })
          }
        }
      })
    },
    closeBoxInfo(){
      this.boxInfoShow = false
    },
    lookBoxInfo(row){
      this.boxInfoShow = true
      this.boxInfoList = row.boxs
    },
    handleUp(index) {  // 上移
      let groupSort1 = this.tableDatas[index].groupSort
      let groupSort2 = this.tableDatas[index - 1].groupSort
      this.tableDatas[index].groupSort = groupSort2
      this.tableDatas[index].items.forEach(item =>{
        item.groupSort = groupSort2
      })
      this.tableDatas[index - 1].groupSort = groupSort1
      this.tableDatas[index - 1].items.forEach(item =>{
        item.groupSort = groupSort1
      })
      if(this.goodsId !== undefined && this.goodsId !== null && this.goodsId !== 0){
        let groupSorts = []
        groupSorts.push({
          goodsId:this.goodsId,
          groupName:this.tableDatas[index].itemGroup,
          groupSort:this.tableDatas[index].groupSort
        })
        groupSorts.push({
          goodsId:this.goodsId,
          groupName:this.tableDatas[index - 1].itemGroup,
          groupSort:this.tableDatas[index - 1].groupSort
        })
        this.changeGroupSorts(groupSorts)
      }
      if (this.tableDatas.length > 1 && index !== 0) {
        this.tableDatas = swapItem(this.tableDatas, index, index - 1);
      }
    },
    handleDown(index) {  // 下移
      let groupSort1 = this.tableDatas[index].groupSort
      let groupSort2 = this.tableDatas[index + 1].groupSort
      this.tableDatas[index].groupSort = groupSort2
      this.tableDatas[index].items.forEach(item =>{
        item.groupSort = groupSort2
      })
      this.tableDatas[index + 1].groupSort = groupSort1
      this.tableDatas[index + 1].items.forEach(item =>{
        item.groupSort = groupSort1
      })
      if(this.goodsId !== undefined && this.goodsId !== null && this.goodsId !== 0){
        let groupSorts = []
        groupSorts.push({
          goodsId:this.goodsId,
          groupName:this.tableDatas[index].itemGroup,
          groupSort:this.tableDatas[index].groupSort
        })
        groupSorts.push({
          goodsId:this.goodsId,
          groupName:this.tableDatas[index + 1].itemGroup,
          groupSort:this.tableDatas[index + 1].groupSort
        })
        this.changeGroupSorts(groupSorts)
      }
      if (this.tableDatas.length > 1 && index !== this.tableDatas.length - 1) {
        this.tableDatas = swapItem(this.tableDatas, index, index + 1);
      }
    },
    // 货品分组暂不要，此接口暂不使用
    changeGroupSorts(val){
      // this.$api.put(this, api.changeGroupSort, {groupSorts:val}).then(res => {
      // })
    },
    syncItem(){// 货品更新
      this.syncItemLoading = true
      this.$http.syncBatchItem(this, { ids: [this.$route.query.id] }).then(res => {
        this.syncItemLoading = false
        if (res.success) {
          this.$message({
            message: '同步货品成功',
            type: 'success',
            duration: 3 * 1000
          })
          //eventBus.$emit('refreshData')
        }
      })
    },
    syncItemBox(){// 货品装箱更新
      this.syncItemBoxLoading = true
      this.$http.syncBatchItemBox(this, { ids: [this.$route.query.id] }).then(res => {
        this.syncItemBoxLoading = false
        if (res.success) {
          this.$message({
            message: '同步货品装箱成功',
            type: 'success',
            duration: 3 * 1000
          })
          //eventBus.$emit('refreshData')
        }
      })
    },
    syncGoodsPrice(){// 价格更新
      this.synPriceLoading = true
      this.$http.syncBatchPrice(this, { ids: [this.$route.query.id] }).then(res => {
        this.synPriceLoading = false
        if (res.success) {
          this.$message({
            message: '同步价格成功',
            type: 'success',
            duration: 3 * 1000
          })
          //eventBus.$emit('refreshData')
        }
      })
    },
    formatStatus(row, col, val) { // 上架状态
      switch(val) {
        case 1:
          return "下架";
          break;
        case 2:
          return "上架审批中";
          break;
        case 3:
          return "上架";
          break;
        case 4:
          return "下架审批中";
          break;

      }
    },
    sleep(n) {
      var start = new Date().getTime();
      while(true)  if(new Date().getTime()-start > n) break;
    },
    handleShelve(row, type) { // 上下架操作
      this.loading = true
      if(type === 1){
        this.checkSuccess = '下架成功'
      }else if(type === 3){
        this.checkSuccess = '上架成功'
      }
      let hintStr = "此操作将改变选中货品的上下架状态，是否继续?"
      if(this.goodType === 2 || row.advanceSaleFlag === 1 || this.goodType === 3){
        hintStr = '当前商品为预售/定制商品，是否确定要下架/上架？'
      }
      this.$confirm(hintStr, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        this.$http.itemSaleStatus(this, {id:row.id, saleStatus:type}).then(res => {  
          this.loading = false
          if(res.success) {
            this.$message({
              message: this.checkSuccess,
              type: 'success',
              duration: 3 * 1000,
              onClose: () => { }
            })
            row.saleStatus = type
          }
        })
      }).catch(_ => {
        this.loading = false
      })
    },
    proving() { //..MOQ验证
      /^[0-9]*[1-9][0-9]*$/;
    },
    goodTypeChange() { //..通用信息的是否支持预售  是否显示判断
      eventBus.$emit('goodTypeData',{
				goodTypeData : this.goodType, //..商品类型
      });
      this.tableData = []
    },
    getImgUrl(index,url){//获取图片链接
      this.tableData[index].itemImg = url
    },
    addGroup(){ // 添加分组
      this.count = (Number(this.count) + 1)+''
      let groupSort = 0
      if(this.tableDatas.length != undefined && this.tableDatas.length>0){
        groupSort = this.tableDatas[this.tableDatas.length-1].groupSort + 1
      }
      this.tableDatas.push({
        itemId:this.count,
        itemGroup:'',
        itemGroupRemark: '',
        groupSort:groupSort,
        items:[]
      })
    },
    deleteGroup(index){ // 定制商品内表格的删除分组操作
      if(this.tableDatas[index].items.length>0){
        this.$confirm('被删除的分组下还有货品，是否一并删除？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(() => {
          this.tableDatas[index].items.forEach(obj =>{ // 对删除的货品打上标示
            if(obj.id !== undefined){
              this.extendData.forEach(items =>{
                  items.items.forEach(item =>{
                    if(item.id == obj.id){
                      item.operationType = 3;
                    }
                  })
              })
            }
          })
          this.tableDatas.splice(index,1)
        })
      }else{
        this.tableDatas.splice(index,1)
      }
      if(index>1){
        this.itemId = this.tableDatas[index-1].itemId
      }else{
        this.itemId = this.tableDatas[0].itemId
      }
    },
    cancelBC(b){
      this.speClick = b;
      this.colClick = b;
    },
    cancelB(b){
      this.speClick = b;
      this.colClick = !b;
    },
    cancelC(b){
      this.colClick = b;
      this.speClick = !b;
    },
    getGrade(){
      this.$http.getGradePoList(this, { page: 1, size: 10000, openFlag: 1 }).then(res => { 
        this.grades = res.data.list
      })
    },
		verify1(row) {  // 验证只能输入数字（erp物料Id）
			row.itemErpId = row.itemErpId.replace(/\D/g,'')
		},
		verify3(row) {  // 验证只能输入数字（装箱数）
			row.boxNum = row.boxNum.replace(/\D/g,'')
		},
		verify4(row) {  // 验证只能输入数字（默认销售价格）
			row.salePrice = row.salePrice.replace(/\D/g,'')
		},
		verify5(row) {  // 验证只能输入数字（重量，克）
			row.weight = row.weight.replace(/\D/g,'')
    },
    addChannel() { // 导入货品
      this.addGoodsShow = true;
    },
    closeGoods(val) { // 导入货品数据
      this.addGoodsShow = false;
      if(val != undefined && val.length != 0){
        for(var i = 0;i < val.length;i++) {
          if(val[i] != undefined){
            if(val[i].priceList === undefined || val[i].priceList === null){
              val[i].priceList = []
            }
            if(val[i].priceList != undefined){
              let isExit = false
              // 普通商品和旧定制商品、新定制商品
              if(this.goodType === 1 || this.goodType === 3 || this.goodType === 2){
                for(let j = 0;j<this.extendData.length;j++){//判断是否已经存在
                  if(val[i].itemErpId === this.extendData[j].itemErpId){
                    val[i].advanceSaleFlag = this.extendData[j].advanceSaleFlag
                    val[i].onwaySaleFlag = this.extendData[j].onwaySaleFlag
                    isExit = true
                    break
                  }
                }
              }

              if(isExit){
                val[i].operationType =  2;  // 修改标志: 1增加, 2修改, 3删除  ============
              }else{
                val[i].operationType =  1;  // 修改标志: 1增加, 2修改, 3删除  ============
              }
              if(val[i].specsName === undefined){
                val[i].specsName = "";
              }
              if(val[i].colorName === undefined){
                val[i].colorName = "";
              }
              if(val[i].advanceSaleFlag === undefined){
                val[i].advanceSaleFlag=0
              }
              if(val[i].onwaySaleFlag === undefined){
                val[i].onwaySaleFlag = 0
              }
              val[i].sizeStr = val[i].length+"*"+val[i].width+"*"+val[i].height
              let b = false
              for(let j=0;j<this.tableData.length;j++){//判断是否已经存在
                if(val[i].itemErpId === this.tableData[j].itemErpId){
                  b = true
                  break
                }
              }
              if(!b){
                this.tableData.push(val[i])
                this.tableData = Array.from(new Set(this.tableData.concat(val[i]))) // 添加导入的每一项，保留原有添加的
                // this.tableData.push(val[i])
                // 添加数组里的对象里的每一项属性(保证规格值可以选择)
                Vue.set(this.tableData[i], 'specsId', val[i].specsId)
                Vue.set(this.tableData[i], 'advanceSaleFlag', val[i].advanceSaleFlag)
                Vue.set(this.tableData[i], 'onwaySaleFlag', val[i].onwaySaleFlag)
                Vue.set(this.tableData[i], 'colorId', val[i].colorId)
                Vue.set(this.tableData[i], 'itemImg', val[i].itemImg)
              }
            }
          }
        }
      }
      if(this.goodType === 2){
        this.changeTableDatas()
      }
    },
    changeTableDatas(){
      for(let i = 0;i<this.tableDatas.length;i++){
        if(this.tableDatas[i].itemId === this.itemId){
          this.tableDatas[i].items = this.tableData
          break
        }
      }
    },
    handleDel(obj){ // 单个扩展数据的软删除
      let index = this.tableData.indexOf(obj);
      this.tableData.splice(index, 1);
      if(this.goodType === 1 || this.goodType === 3 || this.goodType === 2){
        if(obj.id !== undefined){
          if(obj.id != undefined){ //判断删除的时候是否为原来数组的时候有的，如果有，则为删除
            for(var i = 0; i < this.extendData.length; i++) {
              if(this.extendData[i].id == obj.id){
                this.extendData[i].operationType = 3;
              }
            }
          }
        }
      }
	  // else {
   //      if(obj.id !== undefined){
   //        this.extendData.forEach(items =>{
   //            items.items.forEach(item =>{
   //              if(item.id == obj.id){
   //                item.operationType = 3;
   //                items.itemId = this.itemId
   //              }
   //            })
   //        })
   //      }
   //      this.changeTableDatas()
   //    }
    },

    closeDialog() { // 关闭dialog的X按钮
      this.priceShow = false;
      this.specifacationShow = false;
    },
    closeRelevancePictureDialog(){
      this.$refs.selectRelevancePicture.handleCancel()
    },
    closeRelevancePicture(){
      this.relevancePictureShow = false
    },
    renderHeader(h, col) { //..页面右上角的规格值
      if(this.goodType === 2){
        switch(col.$index) {
          case 5:
            return h("div", [h("span","规格"),
            h("el-tooltip",{props:{content:"需先点击上方‘设置规格’添加规格才可设置",effect:"light",placement:"top"}},[h("span",{"class":"el-icon-question question-color"})])])
            break;
          case 6:
            return h("div", [h("span","颜色"),
            h("el-tooltip",{props:{content:"需先点击上方‘设置规格’添加颜色才可设置",effect:"light",placement:"top"}},[h("span",{"class":"el-icon-question question-color"})])])
            break;
          case 8:
            return h("div", [h("span","默认销售价"),
            h("el-tooltip",{props:{content:"默认销售价，产品为单独维护分销商等级价格时所有分销商价都为默认销售价",effect:"light",placement:"top"}},[h("span",{"class":"el-icon-question question-color"})])])
            break;
          case 10:
            return h("div", [h("span","分销商价"),
            h("el-tooltip",{props:{content:"分销商对应商品等级价格",effect:"light",placement:"top"}},[
            h("span",{"class":"el-icon-question question-color"})])])
            break;
          case 11:
            return h("div", [h("span","MOQ"),
            h("el-tooltip",{"class":{"content":"预售（MTO订单）、直运订单最小起订量","effect":"light","placement":"top"}},[h("span",{"class":{"class":"el-icon-question question-color"}})])])
            break;
        }
      }else{
        switch(col.$index) {
          case (this.goodType ===1?4:5):
            return h("div", [h("span","规格"),
            h("el-tooltip",{props:{content:"需先点击上方‘设置规格’添加规格才可设置",effect:"light",placement:"top"}},[h("span",{"class":"el-icon-question question-color"})])])
            break;
          case (this.goodType ===1?5:6):
            return h("div", [h("span","颜色"),
            h("el-tooltip",{props:{content:"需先点击上方‘设置规格’添加颜色才可设置",effect:"light",placement:"top"}},[h("span",{"class":"el-icon-question question-color"})])])
            break;
          case (this.goodType ===1?7:8):
            return h("div", [h("span","默认销售价"),
            h("el-tooltip",{props:{content:"默认销售价，产品为单独维护分销商等级价格时所有分销商价都为默认销售价",effect:"light",placement:"top"}},[h("span",{"class":"el-icon-question question-color"})])])
            break;
          case (this.goodType ===1?9:10):
            return h("div", [h("span","分销商价"),
            h("el-tooltip",{props:{content:"分销商对应商品等级价格",effect:"light",placement:"top"}},[
            h("span",{"class":"el-icon-question question-color"})])])
            break;
          case (this.goodType ===1?10:11):
            return h("div", [h("span","MOQ"),
            h("el-tooltip",{"class":{"content":"预售（MTO订单）、直运订单最小起订量","effect":"light","placement":"top"}},[h("span",{"class":{"class":"el-icon-question question-color"}})])])
            break;
        }
      }
    },

    add() { // 新增货品操作
      let obj = {
        itemName: '',
        itemNameEn: '',
        itemCode: '',
        productNo:'',
        itemErpId: '',
        specsId: '',
        categoryErpId:'',
        categoryName:'',
        colorId:'',
        specsName: '', //..规格
        specsId: '',
        colorName:'', //..颜色
        colorId: '',
        boxNum: '', //..装箱数
        barCode: '', //..条形码
        moq: '', //..MOQ
        salePrice: '',
        costPrice:'',
        gradePrices: [],
        scalePrices: [],
        weight: '',
        operationType: 1  // 修改标志: 1增加,2修改,3删除,
      }
      this.tableData.push(obj)
    },
    // val implements Specification
    getSpecification(spe,col) {
      this.specificationId = spe.id
      this.specificationData = spe.values
      this.colorId = col.id
      this.colorData = col.values
      this.specifacationShow = false;
    },
    relevancePicture(row){
      this.selectItem = row
      this.relevancePictureShow = true
    },
    checkSpecification(index, event) { // 规格
      if(this.specificationData.length == 0) {
        this.$message.warning('请先点击上方设置规格')
      } else {
        this.speIndex = index;
        this.speClick = true;
        let left = event.clientX
        let top = event.clientY
        this.tagPosition = 'left:' + (left + 10) + 'px;top:' + (top - 20) + 'px'
      }
    },
    checkColor(index, event) { // 颜色
      if(this.colorData.length == 0) {
        this.$message.warning('请先点击上方设置颜色')
      } else {
        this.colIndex = index;
        this.colClick = true;
        let left = event.clientX
        let top = event.clientY
        this.tagPosition = 'left:' + (left + 10) + 'px;top:' + (top - 20) + 'px'
      }
    },
    handleSelectSpe(id, name) {  // 规格dialog的值
      this.tableData[this.speIndex].specsId = id;
      this.tableData[this.speIndex].specsName = name;
      this.speIndex = ''
      this.speClick = false;
    },
    handleSelectCol(id, name) {  // 颜色dialog的值
      this.tableData[this.colIndex].colorId = id;
      this.tableData[this.colIndex].colorName = name;
      this.colIndex = ''
      this.colClick = false;
    },
    setPrice(index,row) { // 分销商价格，将写好的价格判断进行填入弹出框的价格input里面
      this.index = index;
      let ary=[];
      // if (Array.isArray(this.gradePrices)){  // Array.isArray() 用于确定传递的值是否是一个 数组
      //   this.gradePrices.forEach(item =>{ // 如果有价格，写入
      //     // 找到row对应的item
      //     if(row.id == undefined && item.itemId == row.itemErpId){ // 判断是编辑数据还是新导入数据
      //       ary.push(item)
      //     } else if (item.itemId == row.id ) {
      //       ary.push(item)
      //     }
      //   })
      // }
      this.chosenItem = this.tableData[this.index]; // 如果无价格，默认价格写入
      this.gprice = row.scalePrices;
      this.priceShow = true;
    },
    getGprice(val) { // 将分销商价格写入tableData
      // val.forEach(item => {
      //   let b = false
      //   for(let i = 0;i<this.gradePrices.length;i++){ // 对价格进行循环，判断
      //     if(item.itemId == this.gradePrices[i].itemId && item.id == this.gradePrices[i].distributorGradeId){
      //       this.gradePrices[i].salePrice = item.value
      //       this.tableData[this.index].gradePrices.forEach(price =>{
      //         if(price.itemId == this.gradePrices[i].itemId && price.id == this.gradePrices[i].distributorGradeId){
      //           price.salePrice = item.value
      //         }
      //       })
      //       b = true
      //       break
      //     }
      //   }
      //   if(!b){ // 如果没有填写分销商价，重新赋值需要的三个参数
      //     let price = {
      //       itemId:item.itemId,
      //       distributorGradeId:item.id,
      //       salePrice:item.value
      //     }
      //     this.gradePrices.push(price)
      //     this.tableData[this.index].gradePrices.push(price)
      //   }
      // })
      this.priceShow = false;
    },
    // 批量下单弹框
    setBatchOrder(row) {
      this.batchOrderShow = true;
      this.curItemCode = row.itemCode;
      row.boxs.forEach(item => {
        if (item.defaultFlag === undefined || item.defaultFlag === '') {
          this.$set(item, 'defaultFlag', 0)
        }
      })
      this.batchOrderList = row.boxs
    },
    closeBatchOrder() {
      this.batchOrderShow = false;
    },
    handleBoxUp(index, row) {  // 上移
      if (this.batchOrderList.length > 1 && index !== 0) {
        this.batchOrderList = swapItem(this.batchOrderList, index, index - 1);
      }
    },
    handleBoxDown(index, row) {  // 下移
      if (this.batchOrderList.length > 1 && index !== this.batchOrderList.length - 1) {
        this.batchOrderList = swapItem(this.batchOrderList, index, index + 1);
      }
    },
    // 设置默认下单形式
    handleDefault(id, isDefault) {
      // 只能设置一个默认
      if (isDefault===1) {
        this.batchOrderList.forEach(item => {
          if (item.id !== id ) {
            item.defaultFlag = 0
          }
        })
      }
    },
    // 批量下单设置--修改货品装箱
    batchOrderSubmit () {
      // 排序
      this.batchOrderList.forEach((item, index) => {
        item.sort = index + 1
      })
      this.$http.editGoodsItemBox(this, this.batchOrderList).then(res => {
        if (res.success) {
          this.$message({
              message: '设置成功',
              type: 'success',
              duration: 3 * 1000
            })
          this.batchOrderShow = false
        }
      })
    },
  },
  watch: {
    items(val) { //..扩展数据详情
	  console.log("扩展数据详情");
      this.extendData = val;
      this.tableData = [];
	  console.log(this.extendData);
      for(var i = 0;i < this.extendData.length; i++) {  // 判断修改类型赋于默认值
        this.extendData[i].operationType = 2
        this.extendData[i].categoryName='';
        if(this.extendData[i].itemErpId == undefined){
          this.extendData[i].itemErpId = '';
        }
        for(let j=0;j<this.productlinelist.length;j++){
          if(this.productlinelist[j].id === this.extendData[i].categoryId){
            this.extendData[i].categoryName = this.productlinelist[j].name
            break
          }
        }
        // 图片
        if (!this.extendData[i].itemImg) {
          this.$set(this.extendData[i], 'itemImg', '')
        }
        if(this.extendData[i].categoryName === ''){
          this.extendData[i].categoryName = "品类不存在"
        }
        this.extendData[i].sizeStr = this.extendData[i].length+"*"+this.extendData[i].width+"*"+this.extendData[i].height
        this.tableData.push(this.extendData[i])
      }
      
      if(this.tableData.length > 0){
        this.goodsId = this.tableData[0].goodsId
        this.$http.getAttributePoList(this, {page:1,size:10000,openFlag:1}).then(res => {
          this.checkList = res.data.list
          this.checkList.forEach(item =>{
            this.$http.getAllvalueList(this, { id: item.id }).then(res2 => {
              if (res2.success) {
                let values = res2.data
                if (values && values.length > 0) {
                  values.forEach(v =>{
                    for(var i = 0; i< this.tableData.length;i ++) {
                      if(this.tableData[i].specsId == v.id) {
                        this.tableData[i].specsName = v.name
                        this.tableData[i].specsId = v.id
                        this.specificationData = values;
                        this.specificationId = item.id
                      }
                    }
                    for(var i = 0; i< this.tableData.length;i ++) {
                      if(this.tableData[i].colorId == v.id) {
                        this.tableData[i].colorName = v.name
                        this.tableData[i].colorId	 = v.id
                        this.colorData = values;
                        this.colorId = item.id
                      }
                    }
                  })
                }
              }
            })
          })
        })
      }
    },
    customizedItems(val) {
      this.extendData = val.concat()
      this.tableData = []
      this.tableDatas = []
      this.extendData.forEach(items =>{
       this.count = (Number(this.count)  + 1)+''
        items.itemId = this.count
        items.items.forEach(goodItem =>{ // 判断修改类型赋于默认值
          let data = {}
          goodItem.operationType = 2
          goodItem.specsName = ''
          goodItem.colorName=''
          goodItem.specsId = ''
          goodItem.colorId=''
          goodItem.categoryName='';
          if(goodItem.itemErpId == undefined){
            goodItem.itemErpId = '';
          }
          // goodItem.gradePrices=[]
          // if (Array.isArray(this.gradePrices)){  // Array.isArray() 用于确定传递的值是否是一个 数组
          //   this.gradePrices.forEach(item =>{ // 如果有价格，写入
          //     if(item.itemId == goodItem.id){ // 判断是编辑数据还是新导入数据
          //       goodItem.gradePrices.push(item)
          //     }
          //   })
          // }
          goodItem.sizeStr = goodItem.length+"*"+goodItem.width+"*"+goodItem.height
          if(goodItem.itemImg === undefined){
            goodItem.itemImg = ''
          }
          for(let j=0;j<this.productlinelist.length;j++){
            if(this.productlinelist[j].id === goodItem.categoryId){
              goodItem.categoryName = this.productlinelist[j].name
              break
            }
          }
          if(goodItem.categoryName === ''){
            goodItem.categoryName = "品类不存在"
          }
        })
      })
      if(this.extendData.length > 0){
        this.goodsId = this.extendData[0].items[0].goodsId
        this.$http.getAttributePoList(this, {page:1, size:10000,openFlag:1}).then(res => {
          this.checkList = res.data.list
          this.checkList.forEach(item =>{
            item.values.forEach(v =>{
              this.extendData.forEach(items =>{
                items.items.forEach(goodItem =>{
                  if(goodItem.specsId == v.id) {
                    goodItem.specsName = v.name
                    goodItem.specsId = v.id
                    this.specificationData = item.values
                    this.specificationId = item.id
                  }
                })
              })
              this.extendData.forEach(items =>{
                items.items.forEach(goodItem =>{
                  if(goodItem.colorId == v.id) {
                    goodItem.colorName = v.name
                    goodItem.colorId = v.id
                    this.colorData = item.values
                    this.colorId = item.id
                  }
                })
              })
            })
          })
        })
      }
      this.tableDatas = []
      this.extendData.forEach(item =>{
        let obj = {
          itemGroup:item.itemGroup,
          itemGroupRemark:item.itemGroupRemark,
          groupSort:item.groupSort,
          itemId:item.itemId,
          items:item.items.concat(),
          userItems:item.userItems
        }
        this.tableDatas.push(obj)
      })
      // this.tableDatas = this.extendData.concat()
      // for(let i = 0;i<this.tableDatas.length;i++){
      //   let items = this.extendData[i].items.concat()
      //   this.extendData[i].items = items.concat()
      //   this.tableDatas[i].items = items.concat()
      // }
      if(this.tableDatas.length >0){
        this.itemId = this.tableDatas[0].itemId
      }
    },
    itemId: {
      handler() {
        for(let i = 0;i<this.tableDatas.length;i++){
          if(this.itemId === this.tableDatas[i].itemId){
            this.tableData = this.tableDatas[i].items
            break
          }
        }
      },
      deep: true
    },
    goodsType(val){
      this.goodType = val
    },
    goodType(val){
      if(val === 2 || val === 3){
        this.diyType = 1
      }
    },
    diysType(val){
      this.diyType = val
    },
    materialsType(val){
      this.materialType = val
    },
    materialType(val){
      if(val !== undefined && val !== null){
        this.getMaterial()
      }
    },
    productlinelist(val){
      if(this.goodsType === 1 && this.tableData != undefined && this.tableData.length>0){
        for(var i = 0; i< this.tableData.length;i ++) {
          for(let j=0;j<this.productlinelist.length;j++){
            if(this.productlinelist[j].id === this.extendData[i].categoryId){
              this.tableData[i].categoryName = this.productlinelist[j].name
              break
            }
          }
          if(this.tableData[i].categoryName === ''){
            this.tableData[i].categoryName = "品类不存在"
          }
        }
      }else if(this.tableDatas != undefined && this.tableDatas.length>0){
        this.tableDatas.forEach(items =>{
          if(items.items != undefined && items.items.length>0){
            items.items.forEach(goodItem =>{
              for(let j=0;j<this.productlinelist.length;j++){
                if(this.productlinelist[j].id === goodItem.categoryId){
                  goodItem.categoryName = this.productlinelist[j].name
                  break
                }
              }
              if(goodItem.categoryName === ''){
                goodItem.categoryName = "品类不存在"
              }
            })
          }
        })
      }
    }
  }
}

</script>
<style rel="stylesheet/scss" lang="scss">
.extend .question-color {
  color: $lakeBlue
}
.extend {
  margin: 10px;
  .el-table .cell div {
      padding-right: 0px;
      overflow: visible;
      text-overflow: ellipsis;
  }
  .nav-bar {
    padding: 10px;
  }
  .title {
    line-height: 35px;
    padding-left: 25px;
  }
  .question-color {
    color: #409EFF;
  }
  .header-row {
    @include table-header-color;
  }
  .tag-content1 {
    position: absolute;
    border: 1px solid #ccc;
    padding: 3px 5px;
    background-color: white;
    cursor: pointer;
    z-index: 9999;
  }
  .tag-content2 {
    position: absolute;
    border: 1px solid #ccc;
    padding: 3px 5px;
    background-color: white;
    cursor: pointer;
    z-index: 9999;
  }
  .sp-tag {
    cursor: pointer;
  }
  .sp-tag:not(:first-child) {
    margin-left: 10px;
  }
  .set-specifacation{
    float: right;
  }
  .el-tabs--border-card {
    background: #fff;
    border: 0px solid #dcdfe6;
    box-shadow: 0 0px 0px 0 rgba(0,0,0,.12), 0 0 0px 0 rgba(0,0,0,.04);
    border-top: 1px solid #dcdfe6;
    border-left: 1px solid #dcdfe6;
    border-right: 1px solid #dcdfe6;
  }
  .el-tabs--border-card>.el-tabs__header {
    background-color: #f5f7fa;
    border-bottom: 0px solid #e4e7ed;
    margin: 0;
}
.but {
    white-space: nowrap;
    position: relative;
    margin-top: 5px;
    margin-left: 20px;
  }
.specifacationDialog{
  .el-dialog__body{
    padding: 0px !important;
  }
}
  .el-dialog{
    margin-top:10px !important;
  }
}
.red-tips {
  padding-top: 15px;
  font-size: 14px;
  color: #f00;
}
.btn-footer{
  margin:40px auto 20px;
  text-align: center;
}
</style>
