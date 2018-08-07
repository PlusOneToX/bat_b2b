<template>
  <div class="add-productline">
    <header v-if="append">
      <h4>添加型号</h4>
      <el-button
        class="mini-back-btn btn-home"
        icon="el-icon-d-arrow-left"
        @click="handleCancel"
      >
        返回列表
      </el-button>
    </header>
    <header v-if="looking">
      <h4>查看型号</h4>
      <el-button
        class="btn-home"
        icon="el-icon-d-arrow-left"
        @click="handleCancel"
      >
        返回列表
      </el-button>
    </header>

		<el-form label-width="150px" ref="formData" :rules="rules" :model="formData" v-loading="vloading">
			<el-form-item label="型号名称" prop="name" style="margin-bottom: 10px;">
				<el-input size="mini" style="width:300px;" v-model="formData.name" placeholder="不超过40个字" maxlength="40"></el-input>
			</el-form-item>
			<el-form-item label="型号英文名称" prop="englishName" style="margin-bottom: 10px;">
				<el-input size="mini" style="width:500px;" v-model="formData.englishName" placeholder="不超过80个字" maxlength="80"></el-input>
			</el-form-item>
			<el-form-item label="型号类型" prop="categoryId" style="margin-bottom: 10px;">
				<el-select @focus="handleClose" @change="changeCategory" v-model="formData.categoryId" size="mini" :disabled="looking">
					<el-option :label="item.name" :value="item.id" :key="item.id" v-for="item in categorys"></el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="是否最终型号" prop="atLastTrademark" style="margin-bottom: 10px;">
				<el-tooltip class="item" effect="dark" content="选择是，为最终型号，选择否，为型号类型" placement="right">
					<el-radio-group v-model="formData.atLastTrademark" @change="changeisCategory"  size="mini">
						<el-radio :label="0" >否</el-radio>
						<el-radio :label="1" >是</el-radio>
					</el-radio-group>
				</el-tooltip>
			</el-form-item>
			<el-form-item label="型号编码" prop="modelNo" style="margin-bottom: 20px;" v-if="formData.atLastTrademark === 1">
				<el-input size="mini" style="width:200px;" v-model="formData.modelNo" placeholder="不超过32个字" maxlength="32"></el-input>
			</el-form-item>
			<el-form-item label="入网型号" prop="networkModel" style="margin-bottom: 10px;" v-if="formData.atLastTrademark === 1">
				<el-input style="width:50%;" size="mini"  type="textarea" :rows="2" v-model="formData.networkModel" placeholder="请输入入网型号，多个请使用分号隔开"></el-input>
			</el-form-item>
			<el-form-item label="上级分类" prop="parentName" style="margin-bottom: 20px;">
				<el-input size="mini" style="width:150px;" clearable  @focus="categoryFocus" @blur="focus=false"  readonly placeholder="请选择" class="category-input" v-model="formData.parentName"></el-input>
				<transition name="el-zoom-in-top">
					<div v-clickoutside="handleClose" class="category-box" v-if="categoryShow">
						<el-tree @node-click="categorySelect" lazy :props='props' :load="getChildren" ref="tree" node-key="id"></el-tree>
					</div>
				</transition>
			</el-form-item>
			<el-form-item label="型号图片" prop="image" style="margin-bottom: 10px;">
				<template>
					<el-upload
						class="avatar-uploader"
						:action="action"
						:show-file-list="false"
						:on-remove="handleRemove"
						:before-upload="beforeUpload">
						<img v-if="formData.image" :src="formData.image" class="avatar">
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				 </template>
			</el-form-item>

			<el-form-item label="型号描述" prop="depict" style="margin-bottom: 10px;">
				<el-input style="width:50%;" size="mini"  type="textarea" :rows="4" v-model="formData.depict" placeholder="不超过200个字" maxlength="200"></el-input>
			</el-form-item>
			<el-form-item label="状态" prop="openFlag">
				<el-radio-group v-model="formData.openFlag" size="mini">
					<el-radio :label="1" >启用</el-radio>
					<el-radio :label="0" >停用</el-radio>
				</el-radio-group>
			</el-form-item>

			
			<el-form-item v-if="formData.atLastTrademark === 1"  label="选择材质：" style="margin-bottom: 0px;" prop="category">
				<el-button @click="addMaterial" class="mini-search-btn"><i class="el-icon-plus el-icon--left"></i>添加材料</el-button>
			</el-form-item>
			<el-form-item v-if="formData.atLastTrademark === 1">
				<el-table :data="goodsTable" border header-row-class-name="header-row" class="tableCenter goods-table" style="width:90%" max-height="400">
					<el-table-column align="center" label="材料名称" prop="materialName"> </el-table-column>
					<el-table-column align="center" label="长(mm)" prop="length">
						<template slot-scope="scope">
							<el-input size="mini" key="1" v-model="scope.row.length" type="number" @input="scope.row.length=/^\d+\.?\d{0,2}$/.test(scope.row.length)||scope.row.length == '' ? scope.row.length:Number(scope.row.length.toString().match(/^\d+(?:\.\d{0,1})?/))" min="0"></el-input>
						</template>
					</el-table-column>
					<el-table-column align="center" label="宽(mm)" prop="width" >
						<template slot-scope="scope">
							<el-input size="mini" key="2" v-model="scope.row.width" type="number" @input="scope.row.width=/^\d+\.?\d{0,2}$/.test(scope.row.width)||scope.row.width == '' ? scope.row.width:Number(scope.row.width.toString().match(/^\d+(?:\.\d{0,1})?/))" min="0"></el-input>
						</template>
					</el-table-column>
					<el-table-column align="center" label="高(mm)" prop="height">
						<template slot-scope="scope">
							<el-input size="mini" key="3" v-model="scope.row.height" type="number" @input="scope.row.height=/^\d+\.?\d{0,2}$/.test(scope.row.height)||scope.row.height == '' ? scope.row.height:Number(scope.row.height.toString().match(/^\d+(?:\.\d{0,1})?/))" min="0"></el-input>
						</template>
					</el-table-column>
					<el-table-column align="center" label="重量(g)" prop="weight">
						<template slot-scope="scope">
							<el-input size="mini" key="4" v-model="scope.row.weight" type="number" @input="scope.row.weight=/^\d+\.?\d{0,2}$/.test(scope.row.weight)||scope.row.weight == '' ? scope.row.weight:Number(scope.row.weight.toString().match(/^\d+(?:\.\d{0,1})?/))" min="0"></el-input>
						</template>
					</el-table-column>
					<el-table-column align="center" label="第三方SKU" prop="thirdSku">
						<template slot-scope="scope">
							<el-input size="mini" v-model="scope.row.thirdSku"></el-input>
						</template>
					</el-table-column>
					<el-table-column align="center" label="透明边距(mm)" prop="frame">
            <template slot-scope="scope">
              <p
                :class="{
                  'frame-data':
                    Number(scope.row.materialId) !== 74 &&
                    Number(scope.row.materialId) !== 84,
                }"
                @click="handleFrame(scope.row.materialId, scope.$index)"
              >
                {{
                  scope.row.frameValue
                    ? scope.row.frameValue
                    : Number(scope.row.materialId) !== 74 &&
                      Number(scope.row.materialId) !== 84
                    ? "请设置"
                    : " - "
                }}
              </p>
            </template>
          </el-table-column>
					<el-table-column align="center" label="BOM编码" prop="bomCode">
						<template slot-scope="scope">
							<el-input size="mini" v-model="scope.row.bomCode"></el-input>
						</template>
					</el-table-column>
					<el-table-column align="center" key="5" label="外框图" :width="80">
						<template slot-scope="scope" style="text-align: center;">
							<upload-small-img :type="1" :item="scope.row" :index ="scope.$index" @getImgUrl ="getOutImage"></upload-small-img>
						</template>
					</el-table-column>
					<el-table-column align="center" key="6" label="底图" :width="80">
						<template slot-scope="scope" style="text-align: center;">
							<upload-small-img :type="2" :item="scope.row" :index ="scope.$index" @getImgUrl ="getFloorImage"></upload-small-img>
						</template>
					</el-table-column>
					<el-table-column align="center" key="9" label="切图信息" :width="100">
            <template slot-scope="scope" style="text-align: center">
              <el-button
                class="mini-search-btn"
                @click="handleCutInfo(scope.$index)"
                >编辑</el-button
              >
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="缺货"
            prop="underStockFlag"
            :width="120"
          >
            <template slot-scope="scope">
							<el-checkbox v-model="scope.row.underStockFlag" :true-label="1" :false-label="0"></el-checkbox>
            </template>
          </el-table-column>
					<el-table-column align="center" label="说明" prop="warnMsg">
            <template slot-scope="scope">
              <el-input size="mini" v-model="scope.row.warnMsg"></el-input>
            </template>
          </el-table-column>
					<el-table-column align="center" key="7" label="状态" prop="openFlag" :formatter="formatStatus"></el-table-column>
					<el-table-column align="center" key="8" label="操作" width="150">
						<template slot-scope="scope">
							<el-button class="mini-tableadd-btn" v-if="scope.row.openFlag !== 1" @click="handleChangeStart(scope.row,1)">启用</el-button>
							<el-button class="mini-freeze-btn" v-else @click="handleChangeStart(scope.row,0)">停用</el-button>
							<el-button class="mini-delete-btn" @click="handleDeleteGood(scope.$index)">删除</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-form-item>
			<el-form-item label="不适用范围：" style="width:800px" max-height="400">
				<div>
					<el-button icon="el-icon-plus" class="mini-search-btn" @click="distributorShow=true">
						添加分销商
					</el-button>
					<el-table :data="distributorData" border header-row-class-name="header-row" max-height="600" class="tableCenter">
						<el-table-column align="center" label="分销商用户名" prop="name"></el-table-column>
						<el-table-column align="center" label="公司名"  prop="companyName"></el-table-column>
						<el-table-column  align="center" label="操作" width="80">
							<template slot-scope="scope">
								<el-button style="margin-top:0px;margin-bottom:0px;" class="mini-delete-btn" @click="handleDeleteDistributor(scope.$index)">
									删除
								</el-button>
							</template>
						</el-table-column>
					</el-table>
				</div>
			</el-form-item>
			<el-button style="margin-left: 46%; margin-top:10px;" class="mini-search-btn" :loading="loading" @click="handleSubmit('formData')">保存</el-button>
			<el-button style="margin-bottom:30px;" size="mini" @click="handleCancel">返回</el-button>
		</el-form>
		<!-- 导入材质dialog -->
		<el-dialog :modal-append-to-body="false" :visible="addMaterials" :before-close="cancelMaterials" width="90%">
			<selectMaterial :sonFlag="sonFlag" :categoryId="formData.categoryId" :selectItemsData="goodsTable" ref="selectGoodItems" @submit="closeMaterials" @cancel="cancelMaterials"></selectMaterial>
		</el-dialog>
			<!-- 不适用分销商 -->
		<el-dialog :modal-append-to-body="false" :visible="distributorShow" :before-close="disCancel" width="80%" >
			<select-distributor :distributorData="distributorData" ref="selectDistributor" @cancel="cancel" @submit="disSubmit"> </select-distributor>
		</el-dialog>
		<!---透明边距弹窗---->
		<el-dialog :modal-append-to-body="false" :visible="frameShow" :before-close="handleCancelFrame" width="500px">
      <el-form label-width="80px" ref="frameData" :model="curFrameData">
        <el-form-item label="上边框" style="margin-bottom: 10px">
          <el-input clearable="" style="width: 80%" size="mini" v-model="curFrameData.top" type="number"
            @input="curFrameData.top =/^\d+\.?\d{0,2}$/.test(curFrameData.top) || 
						curFrameData.top == '' ? curFrameData.top : Number(curFrameData.top.toString().match(/^\d+(?:\.\d{0,1})?/))" min="0" ></el-input>
        </el-form-item>
        <el-form-item label="下边框" style="margin-bottom: 10px">
          <el-input
            style="width: 80%"
            size="mini"
            v-model="curFrameData.bottom"
            type="number"
            @input="curFrameData.bottom = /^\d+\.?\d{0,2}$/.test(curFrameData.bottom) ||
						curFrameData.bottom == '' ? curFrameData.bottom : Number(curFrameData.bottom.toString().match(/^\d+(?:\.\d{0,1})?/))" min="0"
          ></el-input>
        </el-form-item>
        <el-form-item label="左边框" style="margin-bottom: 10px">
          <el-input
            style="width: 80%"
            size="mini"
            v-model="curFrameData.left"
            type="number"
            @input="curFrameData.left = /^\d+\.?\d{0,2}$/.test(curFrameData.left) ||
						curFrameData.left == '' ? curFrameData.left : Number(curFrameData.left.toString().match(/^\d+(?:\.\d{0,1})?/))" min="0"></el-input>
        </el-form-item>
        <el-form-item label="右边框" style="margin-bottom: 10px">
          <el-input
            style="width: 80%"
            size="mini"
            v-model="curFrameData.right"
            type="number"
            @input="
              curFrameData.right =
                /^\d+\.?\d{0,2}$/.test(curFrameData.right) ||
                curFrameData.right == ''
                  ? curFrameData.right
                  : Number(
                      curFrameData.right.toString().match(/^\d+(?:\.\d{0,1})?/)
                    )
            "
            min="0"
          ></el-input>
        </el-form-item>
        <el-form-item label-width="150px !important">
          <el-button size="mini" @click="handleCancelFrame">取消</el-button>
          <el-button
            style="margin-left: 15px"
            class="mini-search-btn"
            :loading="loading"
            @click="handleSubmitFrame('curFrameData')"
            >确定</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>

		<!--切图信息弹窗-->
    <el-dialog
      :modal-append-to-body="false"
      :visible="cutShow"
      :before-close="handleCancelCut"
      width="500px"
      title="切图信息"
    >
      <el-form label-width="150px">
        <el-form-item label="切割类型" style="margin-bottom: 10px">
          <el-radio-group v-model="curCutData.cutType">
            <el-radio :label="1">纵切</el-radio>
            <el-radio :label="2">横切</el-radio>
          </el-radio-group>
        </el-form-item>
        <template v-if="curCutData.cutType === 1">
          <el-form-item label="纵切高度（mm）" style="margin-bottom: 10px">
            <el-input
              style="width: 80%"
              size="mini"
              v-model="curCutData.slittingHeight"
              type="number"
              @input="
                curCutData.slittingHeight =
                  /^\d+\.?\d{0,2}$/.test(curCutData.slittingHeight) ||
                  curCutData.slittingHeight == ''
                    ? curCutData.slittingHeight
                    : Number(
                        curCutData.slittingHeight
                          .toString()
                          .match(/^\d+(?:\.\d{0,1})?/)
                      )
              "
              min="0"
            ></el-input>
          </el-form-item>
          <el-form-item label="留白高度（mm）" style="margin-bottom: 10px">
            <el-input
              style="width: 80%"
              size="mini"
              v-model="curCutData.slittingWhite"
              type="number"
              @input="
                curCutData.slittingWhite =
                  /^\d+\.?\d{0,2}$/.test(curCutData.slittingWhite) ||
                  curCutData.slittingWhite == ''
                    ? curCutData.slittingWhite
                    : Number(
                        curCutData.slittingWhite
                          .toString()
                          .match(/^\d+(?:\.\d{0,1})?/)
                      )
              "
              min="0"
            ></el-input>
          </el-form-item>
        </template>
        <template v-else>
          <el-form-item label="横切宽度（mm）" style="margin-bottom: 10px">
            <el-input
              style="width: 80%"
              size="mini"
              v-model="curCutData.crosscuttingWidth"
              type="number"
              @input="
                curCutData.crosscuttingWidth =
                  /^\d+\.?\d{0,2}$/.test(curCutData.crosscuttingWidth) ||
                  curCutData.crosscuttingWidth == ''
                    ? curCutData.crosscuttingWidth
                    : Number(
                        curCutData.crosscuttingWidth
                          .toString()
                          .match(/^\d+(?:\.\d{0,1})?/)
                      )
              "
              min="0"
            ></el-input>
          </el-form-item>
          <el-form-item label="留白宽度（mm）" style="margin-bottom: 10px">
            <el-input
              style="width: 80%"
              size="mini"
              v-model="curCutData.crosscuttingWhite"
              type="number"
              @input="
                curCutData.crosscuttingWhite =
                  /^\d+\.?\d{0,2}$/.test(curCutData.crosscuttingWhite) ||
                  curCutData.crosscuttingWhite == ''
                    ? curCutData.crosscuttingWhite
                    : Number(
                        curCutData.crosscuttingWhite
                          .toString()
                          .match(/^\d+(?:\.\d{0,1})?/)
                      )
              "
              min="0"
            ></el-input>
          </el-form-item>
        </template>
        <p style="margin-bottom: 10px; color: #f00; text-align: center">
          注：该设置仅适合折叠屏手机，非折叠屏手机不用设置。
        </p>
        <el-form-item label-width="150px !important">
          <el-button size="mini" @click="handleCancelCut">取消</el-button>
          <el-button
            style="margin-left: 15px"
            class="mini-search-btn"
            :loading="loading"
            @click="handleSubmitCut()"
            >确定</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>
	</div>
</template>
<script>
import selectMaterial from './selectMaterial.vue'
import uploadSmallImg from './uploadSmallImg.vue'
import selectDistributor from "@/views/goods/components/selectDistributorAll"
import {monthDay} from '@/utils/timeFormat.js'
export default {
	data() {
		return {
			distributorShow: false,
			action: process.env.BASE_API + 'system/v1/web/admin/oss/sts',
			loading: false,
			vloading: false,
			append: true, // 添加材料
			looking: false, // 查看材料
			categorys:[],
			formData: {
				name: '',
				englishName: '',
				categoryId: undefined,
				remark: '',
				sequence: 0,
				image: '',
				content: '',
				openFlag:1,
				atLastTrademark:1,
				depict:'',
				parentName:'',
				networkModel: '',
				parentId:'',
				distributorIdList:[]
			},
			rules: {
			
				name: [{
					required: true,
					message: '请输入材料名称',
					trigger: 'blur'
				}],
				modelNo: [{
					required: true,
					message: '请输入型号编码',
					trigger: 'blur'
				}],
				parentName: [{
					required: true,
					message: '请选择上级分类',
					trigger: 'change'
				}],
				categoryId: [{
					required: true,
					message: '请选择型号类型',
					trigger: 'blur'
				}],
			},
			disShow:false,
			imageObj:'',
			focus:false,
			categoryShow: false,
			props: {
				label: 'name',
				children: 'childrenList',
				isLeaf: 'leaf'
			},
			goodsTable:[],
			tempGoodsTable:[],
			addMaterials:false,
			distributorData:[],
			sonFlag: false, // 
			cutShow: false, // 切图信息弹窗
      curCutIndex: "", // 当前操作切图信息元素
      // 切图信息
      curCutData: {
        cutType: 1,
        slittingHeight: "",
        slittingWhite: "",
        crosscuttingWidth: "",
        crosscuttingWhite: "",
      },
      frameShow: false, // 透明边距弹窗
      curFrameIndex: "", // 当前操作透明边距元素
      // 透明边距
      curFrameData: {
        top: "",
        bottom: "",
        left: "",
        right: "",
      }
		}
	},
	components: { selectMaterial,uploadSmallImg,selectDistributor },
	created() {
			// 产品类型下拉列表
			this.$http.productUsableList(this).then(res => {
				if (res.success) {
					this.categorys = res.data
				}
			})
		if(this.$route.params.id != undefined) {
			this.vloading = true
			this.append = false;
			this.looking = true;
			this.$http.modelDetail(this, {id:this.$route.params.id}).then(res => {
				if(res.success) {
					res.data.parentName = ''
					this.formData = res.data
					// 材质
					this.goodsTable = []
					if(this.formData.materialRelevanceDTOList !== undefined && this.formData.materialRelevanceDTOList !== null && this.formData.materialRelevanceDTOList.length>0){
						this.formData.materialRelevanceDTOList.forEach(item =>{
							item.openFlag = item.openFlag
							 if (
                  Number(item.materialId) !== 74 &&
                  Number(item.materialId) !== 84
                ) {
                  // TPU 材质，初始化显示透明边距
                  if (item.topFrame != null) {
                    item.frameValue =
                      item.topFrame +
                      "*" +
                      item.underFrame +
                      "*" +
                      item.leftFrame +
                      "*" +
                      item.rightFrame;
                  }
                }
						})
						this.goodsTable = JSON.parse(JSON.stringify(this.formData.materialRelevanceDTOList))
						this.tempGoodsTable = JSON.parse(JSON.stringify(this.formData.materialRelevanceDTOList))
					}
					// 不适用分销商
					this.formData.distributorIdList = []
					if (res.data.distributorExcludeList && res.data.distributorExcludeList.length > 0) {
						res.data.distributorExcludeList.forEach(item => {
							this.formData.distributorIdList.push({
								distributorId: item.distributorId,
								distributorName: item.distributorName,
								companyName: item.distributorCompanyName
							})
						})
					}
					if(this.formData.parentId === 0){
						this.formData.parentName = '顶级分类'
					}else{
						this.$http.modelDetail(this, {id: this.formData.parentId}).then(res => {	
							if(res.success){
								this.formData.parentName = res.data.name
							}
						})
					}
				}
				this.vloading= false
				this.initDistributor()
			})
		}else{
			this.looking = false;
			this.initDistributor()
		}
	},
	directives:{ //..事件绑定
		clickoutside:{
		bind:function(el,binding,vnode){
			function documentHandler(e){
			if(el.contains(e.target)){ //..这里判断点击的元素是否是本身，是本身，则返回
				return false;
			}
			if(binding.expression){ //..判断指令中是否绑定了函数
				binding.value(e) //..如果绑定了函数 则调用那个函数，此处binding.value就是handleClose方法
			}
			}
			el._vueClickOutside_ = documentHandler; //..给当前元素绑定个私有变量，方便在unbind中可以解除事件监听
			document.addEventListener('click',documentHandler);
		},
		unbind:function(el,binding){
			document.removeEventListener('click',el._vueClickOutside_);
			delete el._vueClickOutside_;
		}
		}
	},
	methods: {
		initDistributor(){
			this.distributorData = []
			if(this.formData.distributorIdList !== undefined && this.formData.distributorIdList !== null && this.formData.distributorIdList.length>0){
				this.formData.distributorIdList.forEach(item => {
					this.distributorData.push({
						id:item.distributorId,
						name:item.distributorName,
						companyName:item.companyName
					})
				})
			}
		},
		handleDeleteDistributor(index) {
			this.distributorData.splice(index, 1)
		},
		disSubmit(msg) {
			this.distributorData = msg;
			this.distributorShow = false;
		},
		disCancel() {
			this.$refs.selectDistributor.handleCancel()
		},
		cancel(){
			this.distributorShow = false;
		},
		changeisCategory(val){
			if(this.formData.atLastTrademark === 0){
				this.formData.parentName = '顶级分类'
				this.formData.parentId = 0
			}else {
				this.formData.parentName = ''
				this.formData.parentId = ''
			}
		},
		changeCategory(val){
			this.formData.parentId = undefined
			this.formData.parentName = undefined
		},
		getOutImage(index,url){//外框图
			this.goodsTable[index].outImage = url
		},
		getFloorImage(index,url){//底图
			this.goodsTable[index].floorImage = url
		},
		formatStatus(row, col, val) {
			switch(val) {
				case 1:
				 return '启用';
				 break
				case 0:
				 return '停用';
				 break
			}
		},
		handleChangeStart(row,status){
			row.openFlag = status
		},
		handleDeleteGood(index){
			this.goodsTable.splice(index,1)
		},
		cancelMaterials(){
			this.addMaterials = false
		},
		closeMaterials(val){
			this.addMaterials = false
			this.goodsTable = []
			for (let  i = 0; i < val.length; i++) {
				val[i].id = undefined
			}
			this.goodsTable = this.goodsTable.concat(val)
		},
		addMaterial(){
			this.sonFlag = true;
			this.addMaterials = true
		},
		getChildren(node, resolve){
			if(this.formData.atLastTrademark === 0){ // 非最终型号时
				resolve([{
							id:0,
							name:'顶级分类',
							leaf: true,
						}])
			}else {// 是最终型号时
					let parentId = node.level===0?0:node.data.id
				this.$http.modelPoList(this, {size:1000,page:1,categoryId: this.formData.categoryId,parentId: parentId,atLastTrademark: 0,openFlag:1}).then(res => {	
					if(res.success) {
						if(res.data.list !== undefined && res.data.list !== null && res.data.list.length > 0){
							for(let i = 0;i<res.data.list.length;i++){
								if (res.data.list[i].childrenList.length === 0) {
									res.data.list[i].leaf  = true
								} else {
									res.data.list[i].leaf  = false
								}
							}
						}
						resolve(res.data.list)
					}
					this.loading = false
				})
			}
		},
		categorySelect(data, checked, indeterminate) {
			this.formData.parentName = data.name
			this.formData.parentId = data.id
			if(this.categoryShow && !this.focus){
				this.categoryShow = false
			}
		},	
		handleClose(){
			if(this.categoryShow && !this.focus){
				this.categoryShow = false
			}
		},
		categoryFocus(){
			this.focus = true
			this.categoryShow = true
		},
		beforeUpload(file) {
			if(file.type != 'image/jpeg' && file.type != 'image/bmp' &&
			file.type != 'image/jpg' && file.type != 'image/jpeg' &&
			file.type != 'image/png' && file.type != 'image/gif'){
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
				secure:true
				})
				// 上传
				client.multipartUpload('flexible/model/' + monthDay(new Date()) + '/' + random_name, file, {
				}).then((results) => {
					return new Promise((resolve, reject) => {
						this.formData.image = result.data.hostname + results.name,
						// this.imageObj = {
						// 	hasSuccess: true,
						// 	// url: result.data.hostname + results.name + '?x-oss-process=image/resize,h_120,l_120'
						// }
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
			for(let i = 0; i < len; i++) {　　
				pwd += chars.charAt(Math.floor(Math.random() * maxPos));
			}
			return pwd;
		},
		handleRemove(file) {
			this.formData.image = ''
		},
		proving(){
			this.formData.sequence=this.formData.sequence.replace(/[^\.\d]/g,'');
			this.formData.sequence=this.formData.sequence.replace('.','');
			if(Number(this.formData.sequence) >100000){
				this.formData.sequence = 100000
			}
		},
		handleSubmit(data) {
			this.$refs[data].validate(valid => {
				if(valid) {
					this.loading = true;
					// 不适用范围处理
					this.formData.distributorIdList = []
					if(this.distributorData !== undefined && this.distributorData !== null && this.distributorData.length>0){
						this.formData.distributorExcludeList = undefined
						this.distributorData.forEach(item =>{
							this.formData.distributorIdList.push(item.id)
						})
					}
					if(this.formData.id !== undefined && this.formData.id !== null && this.formData.id !== ''){ // 编辑
						this.formData.deleteIds = []
						this.formData.materialRelevanceDTOList = []
						if(this.tempGoodsTable !== undefined && this.tempGoodsTable !== null && this.tempGoodsTable.length >0){
							this.formData.materialRelevanceDTOList = JSON.parse(JSON.stringify(this.tempGoodsTable))
						}
						for(let i = 0;i<this.formData.materialRelevanceDTOList.length;i++){
							let b = false
							for(let j= 0;j<this.goodsTable.length;j++){ // 循环过滤编辑和删除的项
								this.goodsTable[j].modelId = this.formData.id
								this.goodsTable[j].modelName = this.formData.name
								if(this.goodsTable[j].materialId !== undefined && this.goodsTable[j].materialId === this.formData.materialRelevanceDTOList[i].materialId){// 编辑
									this.goodsTable[j].id = this.formData.materialRelevanceDTOList[i].id
									this.formData.materialRelevanceDTOList[i] = this.goodsTable[j]
									b = true
									break
								}
							}
							if(!b){// 删除情况
								this.formData.deleteIds.push(this.formData.materialRelevanceDTOList[i].id)
								this.formData.materialRelevanceDTOList.splice(i,1)
								i--
							}
						}
						for(let i = 0;i<this.goodsTable.length;i++){ // 关系新增情况
							if(this.goodsTable[i].id === undefined || this.goodsTable[i].id === null || this.goodsTable[i].id === ''){
								this.formData.materialRelevanceDTOList.push(this.goodsTable[i])
							}
						}
					}else{
						// 添加
						this.formData.materialRelevanceDTOList = []
						this.formData.materialRelevanceDTOList = this.formData.materialRelevanceDTOList.concat(this.goodsTable)
					}
					if(this.formData.atLastTrademark === 1){// 最终型号
						if(this.formData.materialRelevanceDTOList.length === 0){
							this.$message.error("至少有一个材料关系")
							this.loading = false
							return
						}
						for(let i = 0;i<this.formData.materialRelevanceDTOList.length;i++){
							if(this.formData.materialRelevanceDTOList[i].length === undefined || this.formData.materialRelevanceDTOList[i].length === null || this.formData.materialRelevanceDTOList[i].length === ''){
								this.$message.error(this.formData.materialRelevanceDTOList[i].materialName+"材料关系的长度不能为空！")
								this.loading = false
								return
							}
							if(this.formData.materialRelevanceDTOList[i].width === undefined || this.formData.materialRelevanceDTOList[i].width === null || this.formData.materialRelevanceDTOList[i].width === ''){
								this.$message.error(this.formData.materialRelevanceDTOList[i].materialName+"材料关系的宽度不能为空！")
								this.loading = false
								return
							}
							if(this.formData.materialRelevanceDTOList[i].height === undefined || this.formData.materialRelevanceDTOList[i].height === null || this.formData.materialRelevanceDTOList[i].height === ''){
								this.$message.error(this.formData.materialRelevanceDTOList[i].materialName+"材料关系的高度不能为空！")
								this.loading = false
								return
							}
							if(this.formData.materialRelevanceDTOList[i].weight === undefined || this.formData.materialRelevanceDTOList[i].weight === null || this.formData.materialRelevanceDTOList[i].weight === ''){
								this.$message.error(this.formData.materialRelevanceDTOList[i].materialName+"材料关系的重量不能为空！")
								this.loading = false
								return
							}
							if(this.formData.materialRelevanceDTOList[i].thirdSku	=== undefined || this.formData.materialRelevanceDTOList[i].thirdSku === null || this.formData.materialRelevanceDTOList[i].thirdSku === ''){
								this.$message.error(this.formData.materialRelevanceDTOList[i].materialName+"第三方SKU不能为空！")
								this.loading = false
								return
							}
							// if(this.formData.materialRelevanceDTOList[i].bomCode === undefined || this.formData.materialRelevanceDTOList[i].bomCode === null || this.formData.materialRelevanceDTOList[i].bomCode === ''){
							// 	this.$message.error(this.formData.materialRelevanceDTOList[i].materialName+"BOM编码不能为空！")
							// 	this.loading = false
							// 	return
							// }
							if(this.formData.materialRelevanceDTOList[i].outImage === undefined || this.formData.materialRelevanceDTOList[i].outImage === null || this.formData.materialRelevanceDTOList[i].outImage === ''){
								this.$message.error(this.formData.materialRelevanceDTOList[i].materialName+"外框图不能为空！")
								this.loading = false
								return
							}
							if(this.formData.materialRelevanceDTOList[i].floorImage === undefined || this.formData.materialRelevanceDTOList[i].floorImage === null || this.formData.materialRelevanceDTOList[i].floorImage === ''){
								this.$message.error(this.formData.materialRelevanceDTOList[i].materialName+"底图不能为空！")
								this.loading = false
								return
							}
						}
					} else {
						this.formData.modelNo=''
						this.formData.materialRelevanceDTOList = []
					}
					if(this.formData.materialRelevanceDTOList != undefined && this.formData.materialRelevanceDTOList != null && this.formData.materialRelevanceDTOList.length>0){
						this.formData.materialRelevanceDTOList.forEach(element => {
							element.openFlag = element.openFlag
							// 透明边距
							if (element.frameValue) {
								let values = element.frameValue.split('*')
								if (element.frameValue.split('*').length > 0) {
										element.topFrame = values[0]
										element.underFrame = values[1]
										element.leftFrame = values[2]
										element.rightFrame = values[3]
								}
							}
						});
					}
				
					if(this.formData.id !== undefined && this.formData.id !== null && this.formData.id !== ''){ // 编辑
						this.$http.editModel(this, this.formData).then(res => {	
							this.loading = false
							if(res.success){
								this.$message.success("编辑成功")
								this.$router.go(-1)
							}
						})
					}else{ // 新增
						if(this.formData.atLastTrademark === 1){// 最终型号
							if(this.formData.materialRelevanceDTOList.length === 0){
								this.$message.error("至少有一个材料关系")
								this.loading = false
								return
							}
						}
						this.$http.addModel(this, this.formData).then(res => {		
							this.loading = false
							if(res.success){
								this.$message.success("新增成功")
								this.$router.go(-1)
							}
						})
					}
				}
			})
		},
		handleCancel() {  // 返回操作
			this.$router.go(-1)
		},
		// 切图信息
    handleCutInfo(index) {
      this.cutShow = true;
      this.curCutIndex = index;
      this.curCutData.cutType = this.goodsTable[index].cutType || 1;
      this.curCutData.slittingHeight = this.goodsTable[index].slittingHeight;
      this.curCutData.slittingWhite = this.goodsTable[index].slittingWhite;
      this.curCutData.crosscuttingWidth = this.goodsTable[
        index
      ].crosscuttingWidth;
      this.curCutData.crosscuttingWhite = this.goodsTable[
        index
      ].crosscuttingWhite;
    },
    // 关闭 - 切图信息弹窗
    handleCancelCut() {
      this.cutShow = false;
    },
    // 确定 - 切图信息弹窗
    handleSubmitCut() {
      this.cutShow = false;
      this.goodsTable.forEach((item, index) => {
        if (index === this.curCutIndex) {
          item.cutType = this.curCutData.cutType || "";
          item.slittingHeight = Number(this.curCutData.slittingHeight) || 0;
          item.slittingWhite = Number(this.curCutData.slittingWhite) || 0;
          item.crosscuttingWidth =
            Number(this.curCutData.crosscuttingWidth) || 0;
          item.crosscuttingWhite =
            Number(this.curCutData.crosscuttingWhite) || 0;
        }
      });
    },
    // 透明边距弹窗
    handleFrame(materialId, index) {
      if (Number(materialId) !== 74 && Number(materialId) !== 84) {
        // TPU 材质，设置透明边距
        this.frameShow = true;
        this.curFrameIndex = index;
        this.curFrameData.top = this.goodsTable[index].topFrame;
        this.curFrameData.bottom = this.goodsTable[index].underFrame;
        this.curFrameData.left = this.goodsTable[index].leftFrame;
        this.curFrameData.right = this.goodsTable[index].rightFrame;
      }
    },
    // 关闭 - 透明边距弹窗
    handleCancelFrame() {
      this.frameShow = false;
    },
    // 确定 - 透明边距弹窗
    handleSubmitFrame() {
      this.frameShow = false;
      this.goodsTable.forEach((item, index) => {
        if (index === this.curFrameIndex) {
          item.topFrame = this.curFrameData.top || 0;
          item.underFrame = this.curFrameData.bottom || 0;
          item.leftFrame = this.curFrameData.left || 0;
          item.rightFrame = this.curFrameData.right || 0;
          // 设置显示透明边距
          let frameValue =
            item.topFrame +
            "*" +
            item.underFrame +
            "*" +
            item.leftFrame +
            "*" +
            item.rightFrame;
          this.$set(item, "frameValue", frameValue);
        }
      });
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
	.btn-home{
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
		width: 193px;
		padding: 10px;
		background-color: white;
		position: absolute;
		left: 0;
		top: 45px;
		z-index: 99;
		border-radius: 10px;
	}
}
.frame-data {
  color: rgb(33, 184, 203);
  cursor: pointer;
}
.frame-data:hover {
  text-decoration: underline;
}

.frame-data {
  color: rgb(33, 184, 203);
  cursor: pointer;
}
.frame-data:hover {
  text-decoration: underline;
}
</style>
