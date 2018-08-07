<template>
	<div class="add-productline">
		 <header v-if="append">
		  <h4>添加标签</h4>
		  <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="handleCancel">
			返回列表
		  </el-button>
		</header>
		 <header v-if="looking">
		  <h4>查看标签</h4>
		  <el-button class="btn-home" icon="el-icon-d-arrow-left" @click="handleCancel">
			返回列表
		  </el-button>
		</header>

		<el-form label-width="150px" :rules="rules" ref="formData" :model="formData" v-loading="vloading">
			<h4 style="color: #000;font-weight: 800; font-size: 14px;margin-bottom: 20px; margin-top: 10px; ">标签信息</h4>
			<el-form-item label="标签名称" prop="name" style="margin-bottom: 10px;">
				<el-input size="mini" style="width:300px;" v-model="formData.name" placeholder="不超过40个字" maxlength="40"></el-input>
			</el-form-item>
			<el-form-item label="第三方编码" prop="thirdSkuNo" style="margin-bottom: 10px;">
				<el-input size="mini" style="width:300px;" v-model="formData.thirdSkuNo" placeholder="请输入"></el-input>
			</el-form-item>
			<el-form-item label="适用类型" prop="categoryId" style="margidin-bottom: 10px;">	
				<el-select v-model="formData.categoryId" size="mini" style="width:200px;" clearable>
					<el-option :label="item.name" :value="item.id" :key="item.id" v-for="item in categorys"></el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="标签模版文件" prop="templateFile" style="margin-bottom: 10px;">
				<template>
					<el-upload
						class="avatar-uploader"
						:action="action"
						:show-file-list="false"
						:on-remove="handleRemove"
						:before-upload="beforeUpload">
						<canvas style="width:120px;height:120px" v-if="formData.templateFile" id="canvas"></canvas>
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				 </template>
			</el-form-item>
			
			<el-form-item label="标签类型" prop="type" style="margin-bottom: 10px;">	
				<el-select v-model="formData.type" size="mini" style="width:200px;" clearable>
					<el-option :label="item.label" :value="item.type" :key="item.type" v-for="item in types"></el-option>
				</el-select>
			</el-form-item>
			<h4 style="color: #000;font-weight: 800; font-size: 14px;margin-bottom: 20px; margin-top: 15px; ">位置信息</h4>
			<div v-if="formData.type === 2">
				<el-form-item label="产品名称位置" prop="productNamePositionX"  style="margin-bottom: 10px;font-weight: 800;width:700px">
					<template>
						<el-tooltip class="item" effect="dark" content="(x, y, 长, 宽)参与值(12, 108, 0, 0)，具体位置可根据参考值进去调整" placement="right-start">
							<div style="font-size: 14px;color: #606266;font-weight: 350;">
								&nbsp;X&nbsp;<el-input style="width:100px;" size="mini"  v-model="formData.productNamePositionX" type="number" @input="formData.productNamePositionX=/^\d+\.?\d{0,4}$/.test(formData.productNamePositionX)||formData.productNamePositionX == '' ? formData.productNamePositionX:Number(formData.productNamePositionX.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
								&nbsp;Y&nbsp;<el-input style="width:100px;" size="mini"  v-model="formData.productNamePositionY" type="number" @input="formData.productNamePositionY=/^\d+\.?\d{0,4}$/.test(formData.productNamePositionY)||formData.productNamePositionY == '' ? formData.productNamePositionY:Number(formData.productNamePositionY.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
								&nbsp;长&nbsp;<el-input style="width:100px;" size="mini" v-model="formData.productNamePositionWidth" type="number" @input="formData.productNamePositionWidth=/^\d+\.?\d{0,4}$/.test(formData.productNamePositionWidth)||formData.productNamePositionWidth == '' ? formData.productNamePositionWidth:Number(formData.productNamePositionWidth.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
								&nbsp;高&nbsp;<el-input style="width:100px;" size="mini" v-model="formData.productNamePositionHeight" type="number" @input="formData.productNamePositionHeight=/^\d+\.?\d{0,4}$/.test(formData.productNamePositionHeight)||formData.productNamePositionHeight == '' ? formData.productNamePositionHeight:Number(formData.productNamePositionHeight.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
							</div>
						</el-tooltip>
					</template>
				</el-form-item>

				<el-form-item label="字体信息" prop="productNamePositionFont" style="margin-bottom: 10px;">
					<el-select v-model="formData.productNamePositionFont" size="mini" style="width:200px;" clearable>
						<el-option :label="item.name" :disabled="item.disabled" :value="item.id" :key="item.id" v-for="item in fontList"></el-option>
					</el-select>
				</el-form-item>

				<el-form-item label="字体大小" prop="productNamePositionFontSize" style="margin-bottom: 20px;">
					<el-input style="width:200px;" size="mini" v-model="formData.productNamePositionFontSize" type="number" @input="formData.productNamePositionFontSize=/^\d+\.?\d{0,4}$/.test(formData.productNamePositionFontSize)||formData.productNamePositionFontSize == '' ? formData.productNamePositionFontSize:Number(formData.productNamePositionFontSize.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
				</el-form-item>

				<el-form-item label="英文名称位置"  style="margin-bottom: 10px;font-weight: 800;width:700px">
					<template>
						<el-tooltip class="item" effect="dark" content="(x, y, 长, 宽)参与值(12, 108, 0, 0)，具体位置可根据参考值进去调整" placement="right-start">
							<div style="font-size: 14px;color: #606266;font-weight: 350;">
								&nbsp;X&nbsp;<el-input style="width:100px;" size="mini"  v-model="formData.enNamePositionX" type="number" @input="formData.enNamePositionX=/^\d+\.?\d{0,4}$/.test(formData.enNamePositionX)||formData.enNamePositionX == '' ? formData.enNamePositionX:Number(formData.enNamePositionX.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
								&nbsp;Y&nbsp;<el-input style="width:100px;" size="mini"  v-model="formData.enNamePositionY" type="number" @input="formData.enNamePositionY=/^\d+\.?\d{0,4}$/.test(formData.enNamePositionY)||formData.enNamePositionY == '' ? formData.enNamePositionY:Number(formData.enNamePositionY.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
								&nbsp;长&nbsp;<el-input style="width:100px;" size="mini" v-model="formData.enNamePositionWidth" type="number" @input="formData.enNamePositionWidth=/^\d+\.?\d{0,4}$/.test(formData.enNamePositionWidth)||formData.enNamePositionWidth == '' ? formData.enNamePositionWidth:Number(formData.enNamePositionWidth.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
								&nbsp;高&nbsp;<el-input style="width:100px;" size="mini" v-model="formData.enNamePositionHeight" type="number" @input="formData.enNamePositionHeight=/^\d+\.?\d{0,4}$/.test(formData.enNamePositionHeight)||formData.enNamePositionHeight == '' ? formData.enNamePositionHeight:Number(formData.enNamePositionHeight.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
							</div>
						</el-tooltip>
					</template>
				</el-form-item>

				<el-form-item label="字体信息" style="margin-bottom: 10px;">
					<el-select v-model="formData.enNamePositionFont" size="mini" style="width:200px;" clearable>
						<el-option :label="item.name" :disabled="item.disabled" :value="item.id" :key="item.id" v-for="item in fontList"></el-option>
					</el-select>
				</el-form-item>

				<el-form-item label="字体大小" style="margin-bottom: 20px;">
					<el-input style="width:200px;" size="mini" v-model="formData.enNamePositionFontSize" type="number" @input="formData.enNamePositionFontSize=/^\d+\.?\d{0,4}$/.test(formData.enNamePositionFontSize)||formData.enNamePositionFontSize == '' ? formData.enNamePositionFontSize:Number(formData.enNamePositionFontSize.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
				</el-form-item>

				<el-form-item label="产品型号位置" prop="modelPositionX"  style="margin-bottom: 10px;font-weight: 800;width:700px">
					<template>
						<el-tooltip class="item" effect="dark" content="(x, y, 长, 宽)参与值(140, 128, 0, 0)，具体位置可根据参考值进去调整" placement="right-start">
							<div style="font-size: 14px;color: #606266;font-weight: 350;">
								&nbsp;X&nbsp;<el-input style="width:100px;" size="mini"  v-model="formData.modelPositionX" type="number" @input="formData.modelPositionX=/^\d+\.?\d{0,4}$/.test(formData.modelPositionX)||formData.modelPositionX == '' ? formData.modelPositionX:Number(formData.modelPositionX.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
								&nbsp;Y&nbsp;<el-input style="width:100px;" size="mini"  v-model="formData.modelPositionY" type="number" @input="formData.modelPositionY=/^\d+\.?\d{0,4}$/.test(formData.modelPositionY)||formData.modelPositionY == '' ? formData.modelPositionY:Number(formData.modelPositionY.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
								&nbsp;长&nbsp;<el-input style="width:100px;" size="mini" v-model="formData.modelPositionWidth" type="number" @input="formData.modelPositionWidth=/^\d+\.?\d{0,4}$/.test(formData.modelPositionWidth)||formData.modelPositionWidth == '' ? formData.modelPositionWidth:Number(formData.modelPositionWidth.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
								&nbsp;高&nbsp;<el-input style="width:100px;" size="mini" v-model="formData.modelPositionHeight" type="number" @input="formData.modelPositionHeight=/^\d+\.?\d{0,4}$/.test(formData.modelPositionHeight)||formData.modelPositionHeight == '' ? formData.modelPositionHeight:Number(formData.modelPositionHeight.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
							</div>
						</el-tooltip>
					</template>
				</el-form-item>

				<el-form-item label="字体信息" prop="modelPositionFont" style="margin-bottom: 10px;">
					<el-select v-model="formData.modelPositionFont" size="mini" style="width:200px;" clearable>
						<el-option :label="item.name" :value="item.id" :key="item.id" v-for="item in fontList"></el-option>
					</el-select>
				</el-form-item>

				<el-form-item label="字体大小" prop="modelPositionFontSize" style="margin-bottom: 20px;">
					<el-input style="width:200px;" size="mini" v-model="formData.modelPositionFontSize" type="number" @input="formData.modelPositionFontSize=/^\d+\.?\d{0,4}$/.test(formData.modelPositionFontSize)||formData.modelPositionFontSize == '' ? formData.modelPositionFontSize:Number(formData.modelPositionFontSize.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
				</el-form-item>
			</div>
			<el-form-item label="产品材质位置" prop="stuffNamePositionX"  style="margin-bottom: 10px;font-weight: 800;width:700px">
				<template>
					<el-tooltip class="item" effect="dark" content="(x, y, 长, 宽)参与值(140, 128, 0, 0)，具体位置可根据参考值进去调整" placement="right-start">
						<div style="font-size: 14px;color: #606266;font-weight: 350;">
							&nbsp;X&nbsp;<el-input style="width:100px;" size="mini"  v-model="formData.stuffNamePositionX" type="number" @input="formData.stuffNamePositionX=/^\d+\.?\d{0,4}$/.test(formData.stuffNamePositionX)||formData.stuffNamePositionX == '' ? formData.stuffNamePositionX:Number(formData.stuffNamePositionX.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
							&nbsp;Y&nbsp;<el-input style="width:100px;" size="mini"  v-model="formData.stuffNamePositionY" type="number" @input="formData.stuffNamePositionY=/^\d+\.?\d{0,4}$/.test(formData.stuffNamePositionY)||formData.stuffNamePositionY == '' ? formData.stuffNamePositionY:Number(formData.stuffNamePositionY.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
							&nbsp;长&nbsp;<el-input style="width:100px;" size="mini" v-model="formData.stuffNamePositionWidth" type="number" @input="formData.stuffNamePositionWidth=/^\d+\.?\d{0,4}$/.test(formData.stuffNamePositionWidth)||formData.stuffNamePositionWidth == '' ? formData.stuffNamePositionWidth:Number(formData.stuffNamePositionWidth.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
							&nbsp;高&nbsp;<el-input style="width:100px;" size="mini" v-model="formData.stuffNamePositionHeight" type="number" @input="formData.stuffNamePositionHeight=/^\d+\.?\d{0,4}$/.test(formData.stuffNamePositionHeight)||formData.stuffNamePositionHeight == '' ? formData.stuffNamePositionHeight:Number(formData.stuffNamePositionHeight.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
						</div>
					</el-tooltip>
				</template>
			</el-form-item>

			<el-form-item label="字体信息" prop="stuffNamePositionFont" style="margin-bottom: 10px;">
				<el-select v-model="formData.stuffNamePositionFont" size="mini" style="width:200px;" clearable>
					<el-option :label="item.name" :value="item.id" :key="item.id" v-for="item in fontList"></el-option>
				</el-select>
			</el-form-item>

			<el-form-item label="字体大小" prop="stuffNamePositionFontSize" style="margin-bottom: 20px;">
				<el-input style="width:200px;" size="mini" v-model="formData.stuffNamePositionFontSize" type="number" @input="formData.stuffNamePositionFontSize=/^\d+\.?\d{0,4}$/.test(formData.stuffNamePositionFontSize)||formData.stuffNamePositionFontSize == '' ? formData.stuffNamePositionFontSize:Number(formData.stuffNamePositionFontSize.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
			</el-form-item>

			<el-form-item label="英文材质位置" prop="stuffEnNamePositionX"  style="margin-bottom: 10px;font-weight: 800;width:700px">
				<template>
					<el-tooltip class="item" effect="dark" content="(x, y, 长, 宽)参与值(140, 128, 0, 0)，具体位置可根据参考值进去调整" placement="right-start">
						<div style="font-size: 14px;color: #606266;font-weight: 350;">
							&nbsp;X&nbsp;<el-input style="width:100px;" size="mini"  v-model="formData.stuffEnNamePositionX" type="number" @input="formData.stuffEnNamePositionX=/^\d+\.?\d{0,4}$/.test(formData.stuffEnNamePositionX)||formData.stuffEnNamePositionX == '' ? formData.stuffEnNamePositionX:Number(formData.stuffEnNamePositionX.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
							&nbsp;Y&nbsp;<el-input style="width:100px;" size="mini"  v-model="formData.stuffEnNamePositionY" type="number" @input="formData.stuffEnNamePositionY=/^\d+\.?\d{0,4}$/.test(formData.stuffEnNamePositionY)||formData.stuffEnNamePositionY == '' ? formData.stuffEnNamePositionY:Number(formData.stuffEnNamePositionY.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
							&nbsp;长&nbsp;<el-input style="width:100px;" size="mini" v-model="formData.stuffEnNamePositionWidth" type="number" @input="formData.stuffEnNamePositionWidth=/^\d+\.?\d{0,4}$/.test(formData.stuffEnNamePositionWidth)||formData.stuffEnNamePositionWidth == '' ? formData.stuffEnNamePositionWidth:Number(formData.stuffEnNamePositionWidth.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
							&nbsp;高&nbsp;<el-input style="width:100px;" size="mini" v-model="formData.stuffEnNamePositionHeight" type="number" @input="formData.stuffEnNamePositionHeight=/^\d+\.?\d{0,4}$/.test(formData.stuffEnNamePositionHeight)||formData.stuffEnNamePositionHeight == '' ? formData.stuffEnNamePositionHeight:Number(formData.stuffEnNamePositionHeight.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
						</div>
					</el-tooltip>
				</template>
			</el-form-item>

			<el-form-item label="字体信息" prop="stuffEnNamePositionFont" style="margin-bottom: 10px;">
				<el-select v-model="formData.stuffEnNamePositionFont" size="mini" style="width:200px;" clearable>
					<el-option :label="item.name" :value="item.id" :key="item.id" v-for="item in fontList"></el-option>
				</el-select>
			</el-form-item>

			<el-form-item label="字体大小" prop="stuffEnNamePositionFontSize" style="margin-bottom: 20px;">
				<el-input style="width:200px;" size="mini" v-model="formData.stuffEnNamePositionFontSize" type="number" @input="formData.stuffEnNamePositionFontSize=/^\d+\.?\d{0,4}$/.test(formData.stuffEnNamePositionFontSize)||formData.stuffEnNamePositionFontSize == '' ? formData.stuffEnNamePositionFontSize:Number(formData.stuffEnNamePositionFontSize.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
			</el-form-item>
			<el-form-item label="条形码位置" prop="barCodePositionX"  style="margin-bottom: 10px;width:700px">
				<template>
					<el-tooltip class="item" effect="dark" content="(x, y, 长, 宽)参与值(150, 5, 70, 25)，具体位置可根据参考值进去调整" placement="right-start">
						<div style="font-size: 14px;color: #606266;">
							&nbsp;X&nbsp;<el-input style="width:100px;" size="mini"  v-model="formData.barCodePositionX" type="number" @input="formData.barCodePositionX=/^\d+\.?\d{0,4}$/.test(formData.barCodePositionX)||formData.barCodePositionX == '' ? formData.barCodePositionX:Number(formData.barCodePositionX.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
							&nbsp;Y&nbsp;<el-input style="width:100px;" size="mini"  v-model="formData.barCodePositionY" type="number" @input="formData.barCodePositionY=/^\d+\.?\d{0,4}$/.test(formData.barCodePositionY)||formData.barCodePositionY == '' ? formData.barCodePositionY:Number(formData.barCodePositionY.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
							&nbsp;长&nbsp;<el-input style="width:100px;" size="mini" v-model="formData.barCodePositionWidth" type="number" @input="formData.barCodePositionWidth=/^\d+\.?\d{0,4}$/.test(formData.barCodePositionWidth)||formData.barCodePositionWidth == '' ? formData.barCodePositionWidth:Number(formData.barCodePositionWidth.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
							&nbsp;高&nbsp;<el-input style="width:100px;" size="mini" v-model="formData.barCodePositionHeight" type="number" @input="formData.barCodePositionHeight=/^\d+\.?\d{0,4}$/.test(formData.barCodePositionHeight)||formData.barCodePositionHeight == '' ? formData.barCodePositionHeight:Number(formData.barCodePositionHeight.toString().match(/^\d+(?:\.\d{0,4})?/))" min="0"></el-input>
						</div>
					</el-tooltip>
				</template>
			</el-form-item>

			<el-form-item label="状态" prop="openFlag">
				<el-radio-group v-model="formData.openFlag" size="mini">
					<el-radio :label="1" >启用</el-radio>
					<el-radio :label="0" >停用</el-radio>
				</el-radio-group>
			</el-form-item>
			<h4 style="color: #000;font-weight: 800; font-size: 14px;margin-bottom: 20px; margin-top: 15px; ">其他设置</h4>
			<el-form-item label="支持用户自定义图片使用：" prop="relevanceUserUpload" v-if="formData.type === 2" style="margin-bottom: 0;">
				<el-radio-group v-model="formData.relevanceUserUpload" size="mini">
					<el-radio :label="1" >是</el-radio>
					<el-radio :label="0" >否</el-radio>
				</el-radio-group>
			</el-form-item>
			<distributor ref="distributor" :distributorType="formData.scope" :distributors="distributors"  @change="getChange" :disabled="false"></distributor>
			<el-form-item  label="图片关联" style="margin-bottom: 20px;width:1070px">
				<el-input size="mini" clearable  @focus="categoryFocus" @blur="focus=false"  placeholder="请选择图片分类筛选" class="category-input" v-model="pageInfo.categoryName"></el-input>
				<transition name="el-zoom-in-top">
					<div v-clickoutside="handleClose" class="category-box" v-if="categoryShow">
						<el-tree :data="categoryPicList" @node-click="categorySelect" :props='props' ref="tree"></el-tree>
					</div>
				</transition>
				<el-table :data="pictureList" tooltip-effect="dark" ref="multipleSelect" @select="select" @select-all="selectAll"  @selection-change="handleSelectionChange" border header-row-class-name="header-row" class="tableCenter goods-table"  max-height="460">
					<el-table-column type="selection" width="50" align="center"></el-table-column>
					<el-table-column align="center" label="图片名称" prop="name"> </el-table-column>
					<el-table-column align="center" label="图片分类" prop="categoryName"> </el-table-column>
					<el-table-column align="center" label="图片状态" prop="openFlag" :formatter="formatStatus"> </el-table-column>
				</el-table>
				<page :page="pageInfo.page"  :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
			</el-form-item>
			<el-button style="margin-left: 30%; margin-bottom:30px;" class="mini-search-btn" :loading="loading" @click="handleSubmit('formData')">保存</el-button>
			<el-button style="margin-bottom:30px;" size="mini" v-if="looking" :loading="handlePreviewLoading" class="mini-browse-btn" @click="handlePreview">预览</el-button>
			<el-button size="mini" @click="handleCancel">返回</el-button>
		</el-form>
	</div>
</template>
<script>
import relevancePicture from './relevancePicture.vue'
import distributor from '@/views/material/components/distributor'
import page from "@/components/pagination"
import PDFJS from 'pdfjs-dist'
import {setArr2} from '@/utils/common.js'
import {monthDay} from '@/utils/timeFormat.js'
export default {
	data() {
		return {
			action: process.env.BASE_API + 'system/v1/web/admin/oss/sts',
			loading: false,
			vloading: false,
			append: true, // 添加标签
			looking: false, // 查看标签
			handlePreviewLoading:false,
			pageInfo: {
				page: 1,
				size: 10,
				categoryId:undefined,
				categoryName:undefined,
				content:undefined
			},
			total:0,
			types:[{
				type:1,
				label:'标准标签'
			},{
				type:2,
				label:'定制标签'
			}],
			categorys:[],
			formData: {
				name: '',
				thirdSkuNo: '',
				categoryId:'',
				templateFile:'',
				englishName: '',
				type:1,
				barCodePositionX: '',
				barCodePositionY: '',
				barCodePositionWidth: '',
				barCodePositionHeight:'',
				productNamePositionX: '',
				productNamePositionY:'',
				productNamePositionWidth:'',
				productNamePositionHeight:'',
				enNamePositionLeft: '',
				enNamePositionTop:'',
				enproductNamePositionWidth:'',
				enproductNamePositionHeight:'',
				modelPositionX:'',
				modelPositionY:'',
				modelPositionWidth:'',
				modelPositionHeight:'',
				nameFont:'',
				enNameFont:'',
				modelFont:'',
				nameFontSize:'',
				enNameFontSize:'',
				modelPositionFontSize:'',
				scope:1,
				pictureIdList:[],
				distributionIds:[],
				relevanceUserUpload: 0,
				openFlag:1,
			},
			fontList:[],
			distributors:[],
			pictureIds:[],
			rules: {
				name: [{
					required: true,
					message: '请输入标签名称',
					trigger: 'blur'
				}],
				categoryId: [{
					required: true,
					message: '请选择适用类型',
					trigger: 'blur'
				}],
				type: [{
					required: true,
					message: '请选择标签类型',
					trigger: 'blur'
				}],
				productNamePositionX: [{
					required: true,
					message: '请输入产品名称位置信息',
				}],
				productNamePositionFont: [{
					required: true,
					message: '请选择字体信息',
				}],
				productNamePositionFontSize: [{
					required: true,
					message: '请输入字体大小',
				}],
				modelPositionX:[{
					required: true,
					message: '请输入产品型号位置信息',
					trigger: 'blur'
				}],
				modelPositionFont: [{
					required: true,
					message: '请选择字体信息',
				}],
				modelPositionFontSize: [{
					required: true,
					message: '请输入字体大小',
				}],
				barCodePositionX:[{
					required: true,
					message: '请输入条形码位置信息',
					trigger: 'blur'
				}],
				modelFont: [{
					required: true,
					message: '请输选择字体信息',
				}],
				modelPositionFontSize: [{
					required: true,
					message: '请输入字体大小',
				}],
				templateFile:[{
					required: true,
					message: '请上传标签模版文件',
					trigger: 'blur'
				}],
			},
			disShow:false,
			focus:false,
			categoryShow: false,
			categoryPicList: [],
			props: {
				label: 'name',
				children: 'childrenList',
				isLeaf: 'leaf'
			},
			addLabel:false,
			pictureCategoryList:[],
			pictureList:[],
			isSelect:false,
			pdfDoc:''
		}
	},
	components: { relevancePicture,distributor,page },
	created() {
		this.getData()
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
		handlePreview(){
			this.handlePreviewLoading = true
			this.$http.tabPreview(this, {id: this.formData.id}).then(res => {
				this.handlePreviewLoading = false
				if(res === undefined || res.success == undefined) {
					this.timeData = [] //..成功下载Excel后初始化选中时间
				}
				const content = res
				let blob = new Blob([content],{
					type: "application/pdf"
				})
				let url = window.URL.createObjectURL(blob)
				this.$router.push({ name: 'pdfPreview', params: { url: url } })
			})
		},
		labelPreview(){
			this.$router.push({ name: 'labelPreview', params: { labelId: this.formData.id } })
		},
		formatStatus(row, col, val){
			switch(val) {
				case 0:
					return "停用";
					break;
				case 1:
					return "启用";
					break;
				default:
					return "-";
			}
		},
		sizeChange(size) {
			this.pageInfo.page = 1
			this.pageInfo.size = size;
			this.getPictures()
		},
		currentChange(page) {
			this.pageInfo.page = page;
			this.getPictures()
		},
		renderPage () {
			this.pdfDoc.getPage(1).then((page) => {
				let canvas = document.getElementById('canvas')
				let ctx = canvas.getContext('2d')
				let dpr = window.devicePixelRatio || 1
				let bsr = ctx.webkitBackingStorePixelRatio ||
					ctx.mozBackingStorePixelRatio ||
					ctx.msBackingStorePixelRatio ||
					ctx.oBackingStorePixelRatio ||
					ctx.backingStorePixelRatio || 1
				let ratio = dpr / bsr
				let viewport = page.getViewport(screen.availWidth / page.getViewport(1).width)
				canvas.width = viewport.width * ratio
				canvas.height = viewport.height * ratio
				canvas.style.width = '120px'
				canvas.style.height = '120px'
				ctx.setTransform(ratio, 0, 0, ratio, 0, 0)
				let renderContext = {
					canvasContext: ctx,
					viewport: viewport
				}
				page.render(renderContext)
			})
		},
		loadFile (url) {
			PDFJS.getDocument(url).then((pdf) => {
				this.pdfDoc = pdf
				this.$nextTick(() => {
					this.renderPage()
				})
			})
		},
		getChange(val) {
			this.formData.scope = val.distributorScope
			this.formData.distributors = val.distributorData
		},
		getPictures(){ // 获取图片列表数据
			this.isSelect = false
		  this.$http.picturePoList(this, this.pageInfo).then(res => {
				if(res.success) {
					this.pictureList = []
					if(res.data.list !== undefined && res.data.list !== null && res.data.list.length > 0){
						this.pictureList = res.data.list
						// if(this.pageInfo.page === 1){
						// 	this.pictureList.splice(0, 0, {
						// 		id:0,
						// 		name: '客户上传图片',
						// 		categoryName: '-',
						// 		openFlag: '-'
						// 	})
						// }
						this.pictureIds.forEach(row1 => {//重新获取数据时，判断哪些选中了
							this.pictureList.forEach(row2 => {
								if(row1 === row2.id){
									this.$refs.multipleSelect.toggleRowSelection(row2);
								}
							})
						});
					}
					
					this.total = res.data.total
				}
			})
		},
		select(selection, row){ // 单选时调用
			this.isSelect = true
			let d = false
			for(let i = 0;i<this.pictureIds.length;i++){
				if(this.pictureIds[i] === row.id){
					this.pictureIds.splice(i,1)
					d = true
					break
				}
			}
			if(!d){
				this.pictureIds.push(row.id)
				this.pictureIds = setArr2(this.pictureIds)
			}
		},
		selectAll(selection){ // 全选时调用
			this.isSelect = true
			if(selection.length === 0){
				this.pictureList.forEach(row => {
					for(let i = 0;i<this.pictureIds.length;i++){
						if(this.pictureIds[i] === row.id){
							this.pictureIds.splice(i,1)
							break
						}
					}
				})
			}else{
				selection.forEach(item =>{
					this.pictureIds.push(item.id)
				})
				this.pictureIds = setArr2(this.pictureIds)
			}
		},
		handleSelectionChange(val) { // 当切换页面时的作用
			if(val.length === 0 && this.pictureIds.length != 0 && !this.isSelect){
				this.pictureIds.forEach(row1 => {
					this.pictureList.forEach(row2 => {
						if(row1 === row2.id){
							this.$refs.multipleSelect.toggleRowSelection(row2);
						}
					})
				});
				this.isSelect = true
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
		getData(){
			// 产品材料类型下拉列表
			this.$http.productUsableList(this).then(res => {
				if (res.success) {
					this.categorys = res.data
				}
			})
			// 字体信息
			this.$http.fontList(this, {page:1,size:1000}).then(res => {	
				if(res.success && res.data.list !== undefined && res.data.list !== null && res.data.list.length > 0){
					res.data.list.forEach(item => {
						if(item.openFlag === 1){
							item.disabled = false
						}else{
							item.disabled = true
						}
					})
					this.fontList = res.data.list
				}
			})
			// 图片分类
			this.$http.pictureCategoryPoList(this, {page:1,size:1000,atLastTrademark:1,openFlag:1}).then(res => {
				if(res.success) {
					this.categoryPicList = res.data.list
				}
			})
		
			if(this.$route.params.id != undefined) {
				this.vloading = true
				this.append = false
				this.looking = true
				this.$http.labelDetail(this, {id: this.$route.params.id}).then(res => {			
					if(res.success) {
						this.formData = res.data
						// 允许用户上传图片
						if (this.formData.type === 2) {
							this.formData.relevanceUserUpload = (this.formData.relevanceUserUpload === 1 || this.formData.relevanceUserUpload === 0) ? this.formData.relevanceUserUpload : 0
						}
						this.loadFile(this.formData.templateFile)
						// 分销商
						this.distributors = []
						if (res.data.distributorRelaList && res.data.distributorRelaList.length > 0) {
							res.data.distributorRelaList.forEach(item => {
								this.distributors.push({
									id: item.distributorId,
									name: item.distributorName,
									companyName: item.distributorCompanyName
								})
							})
						}
						let picList = res.data.pictureRelaList
						this.multipleSelect = []
						if(picList && picList.length > 0){
							picList.forEach(item => {
								this.pictureIds.push(item.pictureId)
							})
						}
					}
					this.vloading= false
					// 图片列表
					this.getPictures()
				})
			}else{
				this.append = true
				this.looking = false
				// 图片列表
					this.getPictures()
			}
		},
		categorySelect(data, checked, indeterminate) {
			this.pageInfo.categoryName = data.name
			this.pageInfo.categoryId = data.id
			if(this.categoryShow && !this.focus){
				this.categoryShow = false
			}
		},
		beforeUpload(file) {
			if(file.type != 'application/pdf'){
				this.$message.error('上传只能是pdf格式!')
				return false
			}
			const isLt2M = file.size / 1024 / 1024 < 2;
			if (!isLt2M) {
				this.$message.error('上传文件大小不能超过 2MB!');
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
				client.multipartUpload('flexible/label/' + monthDay(new Date()) + '/' + random_name, file, {
				}).then((results) => {
					return new Promise((resolve, reject) => {
						this.formData.templateFile = result.data.hostname + results.name
						this.loadFile(this.formData.templateFile)
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
		handleSubmit(data) {
			this.$refs['formData'].validate(valid => {
				if(valid) {
					this.loading = true
					if(this.formData.type === 2){// 定制标签情况
						if(this.formData.productNamePositionX === undefined || this.formData.productNamePositionX === null || this.formData.productNamePositionX === '' ||
							this.formData.productNamePositionY === undefined || this.formData.productNamePositionY === null || this.formData.productNamePositionY === '' ||
							this.formData.productNamePositionWidth === undefined || this.formData.productNamePositionWidth === null || this.formData.productNamePositionWidth === '' ||
							this.formData.productNamePositionHeight === undefined || this.formData.productNamePositionHeight === null || this.formData.productNamePositionHeight === '' ||
							this.formData.productNamePositionFont === undefined || this.formData.productNamePositionFont === null || this.formData.productNamePositionFont === '' ||
							this.formData.productNamePositionFontSize === undefined || this.formData.productNamePositionFontSize === null || this.formData.productNamePositionFontSize === ''){
							this.$message.error('产品名称位置和字体信息不完整，请填充完整')
							this.loading = false
							return
						}
						if(this.formData.modelPositionX === undefined || this.formData.modelPositionX === null || this.formData.modelPositionX === '' ||
							this.formData.modelPositionY === undefined || this.formData.modelPositionY === null || this.formData.modelPositionY === '' ||
							this.formData.modelPositionWidth === undefined || this.formData.modelPositionWidth === null || this.formData.modelPositionWidth === '' ||
							this.formData.modelPositionHeight === undefined || this.formData.modelPositionHeight === null || this.formData.modelPositionHeight === '' ||
							this.formData.modelPositionFont === undefined || this.formData.modelPositionFont === null || this.formData.modelPositionFont === '' ||
							this.formData.modelPositionFontSize === undefined || this.formData.modelPositionFontSize === null || this.formData.modelPositionFontSize === ''){
							this.$message.error('产品型号位置和字体信息不完整，请填充完整')
							this.loading = false
							return
						}
					}else {
						this.formData.productNamePositionX = undefined
						this.formData.productNamePositionY = undefined
						this.formData.productNamePositionWidth = undefined
						this.formData.productNamePositionHeight = undefined
						this.formData.nameFont = undefined
						this.formData.nameFontSize = undefined
						this.formData.enNamePositionLeft = undefined
						this.formData.enNamePositionTop = undefined
						this.formData.enproductNamePositionWidth = undefined
						this.formData.enproductNamePositionHeight = undefined
						this.formData.enNameFont = undefined
						this.formData.enNameFontSize = undefined
						this.formData.modelPositionX = undefined
						this.formData.modelPositionY = undefined
						this.formData.modelPositionWidth = undefined
						this.formData.modelPositionHeight = undefined
						this.formData.modelFont = undefined
						this.formData.modelPositionFontSize = undefined
					}
					if(this.formData.barCodePositionX === undefined || this.formData.barCodePositionX === null || this.formData.barCodePositionX === '' ||
						this.formData.barCodePositionY === undefined || this.formData.barCodePositionY === null || this.formData.barCodePositionY === '' ||
						this.formData.barCodePositionWidth === undefined || this.formData.barCodePositionWidth === null || this.formData.barCodePositionWidth === '' ||
						this.formData.barCodePositionHeight === undefined || this.formData.barCodePositionHeight === null || this.formData.barCodePositionHeight === ''){
						this.$message.error('条形码信息不完整，请填充完整')
						this.loading = false
						return
					}
					this.formData.scope = this.$refs.distributor.formData.distributorScope
					this.distributors = this.$refs.distributor.formData.distributorData
					if(this.formData.scope === 4){
						if(this.distributors === undefined || this.distributors === null || this.distributors === '' || this.distributors.length === 0){
							this.$message.error('至少指定一个分销商')
							this.loading = false
							return
						}
						this.formData.distributorIdList = []
						this.distributors.forEach(item =>{
							this.formData.distributorIdList.push(item.id)
						})
					}
					this.formData.pictureIdList = this.pictureIds
					if(this.formData.id !== undefined && this.formData.id !== null && this.formData.id !== ''){ // 编辑
						this.$http.editLabel(this, this.formData).then(res => {		
							this.loading = false
							if(res.success){
								this.$message.success("编辑成功")
								this.$router.go(-1)
							}
						})
					}else{ // 新增
						this.$http.addLabel(this, this.formData).then(res => {		
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
	},
	watch: {
		'pageInfo.categoryName': {
			handler() {
				if(this.pageInfo.categoryName === undefined || this.pageInfo.categoryName === null || this.pageInfo.categoryName === ''){
					this.pageInfo.categoryId = undefined
				}
				this.pageInfo.page = 1
				this.getPictures()
			},
			deep: true
		},
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

</style>
