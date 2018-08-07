<template>
	<div class="add-specification">
		<header v-if="append">
			<h4>{{formData.attributeType == 1?'添加商品规格':(formData.attributeType == 2?'添加商品颜色':'添加商品参数')}}</h4>
			<el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="backpuls">
				返回列表
			</el-button>
		</header>
		<header v-if="looking">
			<h4>{{formData.attributeType == 1?'查看商品规格':(formData.attributeType == 2?'查看商品颜色':'查看商品参数')}}</h4>
			<el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="backpuls">
				返回列表
			</el-button>
		</header>
		<div style="width: 70%;margin-left: 30px;padding-bottom:30px">
			<el-form label-width="110px" :model="formData" :rules="rules" ref="formData" label-position="right">
						<el-form-item :label="formData.attributeType == 1?'规格名称':(formData.attributeType == 2?'颜色名称':'参数名称')" prop="name">
							<el-input v-model="formData.name" placeholder="不超过40个字" maxlength="40">
							</el-input>
						</el-form-item>
            <el-form-item :label="formData.attributeType == 1?'规格英文名称':(formData.attributeType == 2?'颜色英文名称':'参数英文名称')" prop="nameEn">
              <el-input v-model="formData.nameEn" placeholder="不超过80个字" maxlength="80">
              </el-input>
            </el-form-item>
						<el-form-item :label="formData.attributeType == 1?'规格备注':(formData.attributeType == 2?'颜色备注':'参数备注')" prop="description">
							<el-input type="textarea" :rows="5" v-model="formData.description" placeholder="不超过200个字" maxlength="200">
							</el-input>
						</el-form-item>
						<el-form-item label="类型">
							<el-radio-group v-model="formData.attributeType">
								<el-radio :label="1">规格</el-radio>
								<el-radio :label="2">颜色</el-radio>
								<el-radio :label="3">参数</el-radio>
							</el-radio-group>
						</el-form-item>
						<el-form-item :label="formData.attributeType == 1?'规格值':(formData.attributeType == 2?'颜色值':'参数值')" prop="value">
							<el-button class="mini-tableadd-btn" @click="addValue">{{formData.attributeType == 1?'添加规格值':(formData.attributeType == 2?'添加颜色值':'添加参数值')}}</el-button>
						</el-form-item>
						<el-form-item>
							<el-table :data="speciValue" border v-if="speciValue.length">
								<el-table-column align="center" :label="formData.attributeType == 1?'规格值':(formData.attributeType == 2?'颜色值':'参数值')">
									<template slot-scope="scope">
										<el-input placeholder="不超过40个字" maxlength="40" v-model="scope.row.name" :readonly="!scope.row.operationType" @blur="valueBlur(scope.$index)" class="value-input"></el-input>
									</template>
								</el-table-column>
                <el-table-column align="center" :label="formData.attributeType == 1?'英文规格值':(formData.attributeType == 2?'英文颜色值':'英文参数值')">
                  <template slot-scope="scope">
                    <el-input placeholder="不超过80个字" maxlength="80" v-model="scope.row.nameEn" :readonly="!scope.row.operationType" @blur="valueBlur(scope.$index)" class="value-input"></el-input>
                  </template>
                </el-table-column>
								<el-table-column align="center" label="操作" width="220">
									<template slot-scope="scope">
										<el-button class="mini-tableadd-btn" @click="handleUp(scope.$index,scope.row)">上移</el-button>
										<el-button class="mini-freeze-btn" @click="handleDown(scope.$index,scope.row)">下移</el-button>
										<!-- <el-button class="mini-search-btn" @click="handleEdit(scope.$index,scope.row)">编辑</el-button> -->
										<el-button class="mini-delete-btn" @click="handleDelete(scope.$index,scope.row)">删除</el-button>
									</template>
								</el-table-column>
							</el-table>
						</el-form-item>
						<el-button style="margin-left: 50%;margin-top:30px;" class="mini-search-btn" @click="handleSubmit('formData')" :loading="loading">保存</el-button>
						<el-button size="mini" @click="cancel">返回</el-button>
			</el-form>
		</div>
	</div>
</template>
<script type="text/javascript">
import { swapItem } from "@/utils/index";
export default {
  name: "addspecification",
  data() {
		return {
			append: true, // 添加商品品牌
			looking: false, // 查看商品牌品详情
			formData: {
        name: "",
        nameEn: '',
        description: "",
        showType: this.showType,
        values: [],
        // valueEn: '',
			  attributeType:1,
				openFlag: 0
			},
			speciValue: [{
				name: "",
        nameEn: '',
				operationType:1, // 1.新增 2.修改 3.删除
				sort: 1,
				// edit: true,
			}],
			speciValueTemp:[],
			loading: false,
			rules: {
				name: [
					{required: true, message: "请输入名称", trigger: "blur"}
				],
        nameEn: [
          {required: true, message: "请输入英文名称", trigger: "blur"}
        ],
				values: [
					{required: true, message: "至少添加一条数据", trigger: "blur"}
				]
			}
		};
  },
  created() {
		if (this.$route.params.id != undefined) {
			this.append = false;
			this.looking = true;
			this.$http.getAttributeDetail(this, { id: this.$route.params.id }).then(res => {
					this.formData = res.data
						this.speciValue = []
					this.formData.values.forEach(item => {
						let obj = {
							id:item.id,
							operationType:2,
							name: item.name,
							nameEn: item.nameEn,
							sort: item.sort,
							edit: true,
						};
						this.speciValue.push(obj);
					});
			})
		}
  },
  methods: {
	// 返回按钮
	backpuls() {
		this.$router.go(-1)
	},
	handleSubmit(data) { // 保存按钮
		this.formData.values = '';
		// this.formData.valueEn = '';
		if (this.speciValue.length > 0) {
      this.formData.values = [];
			for(var i = 0; i < this.speciValue.length; i++) {
			  const temp = {
			    name: this.speciValue[i].name,
			    nameEn: this.speciValue[i].nameEn,
					operationType: this.speciValue[i].operationType,
					sort: this.speciValue[i].sort
        }
        this.formData.values.push(temp);
				// if(i === 0){
				// 		this.formData.value = this.speciValue[i].name;
				// 		if (this.speciValue[i].nameEn) {
        //       this.formData.valueEn = this.speciValue[i].nameEn;
        //     }
				// }else{
				// 		this.formData.value = this.formData.value + ','+this.speciValue[i].name;
        //   if (this.speciValue[i].nameEn) {
        //     this.formData.valueEn = this.formData.valueEn + ',' + this.speciValue[i].nameEn;
        //   }
				// }
			}
		}
	  this.$refs[data].validate(valid => { // 验证必填项
			if (valid) {
				this.loading = true;
				if (this.$route.params.id) {
					// const params = JSON.parse(JSON.stringify(this.formData))  // 拷贝
					// params.value = params.values || params.value;
					this.speciValue.forEach((item, index) =>{
						item.operationType = item.operationType === 1 ? item.operationType : 2
						item.sort = index + 1
						this.speciValueTemp.push(item);
					})
					this.formData.values = this.speciValueTemp;
					this.$http.editAttribute(this, this.formData).then(res => {
						if (res.success) {
							this.$message({
								message: "修改成功",
								type: "success",
								duration: 3 * 1000,
								onClose: () => { }
							});
							this.$router.push({ name: "specificationlist" });
						}
						this.loading = false;
						this.speciValueTemp = []
					})
				} else {
					this.$http.addAttribute(this, this.formData).then(res => {
						if (res.success) {
							this.$message({
									message: "添加成功",
									type: "success",
									duration: 3 * 1000,
									onClose: () => { }
							});
							this.$router.push({ name: "specificationlist" });
						}
						this.loading = false;
					})
				}
			} else {
				this.loading = false;
				return false;
			}
	  });
	},
	cancel() {
	  this.$router.push({ name: "specificationlist" });
	},
	addValue() { // 添加规格值
	  let obj = {
		  name: "",
      nameEn: '',
		  operationType:1,
			sort:this.speciValue[this.speciValue.length-1].sort + 1
		  // edit: true,
	  };
	  this.speciValue.push(obj);
	},
	valueBlur(index) {
	  // this.speciValue[index].edit = true;
		// this.speciValue[index].operationType = 2
	},
	handleUp(index, row) {  // 上移
	  if (this.speciValue.length > 1 && index !== 0) {
		this.speciValue = swapItem(this.speciValue, index, index - 1);
	  }
	},
	handleDown(index, row) {  // 下移
	  if (this.speciValue.length > 1 && index !== this.speciValue.length - 1) {
		this.speciValue = swapItem(this.speciValue, index, index + 1);
	  }
	},
	handleEdit(index, row) {  // 编辑
	  // this.speciValue[index].edit = true;
		this.speciValue[index].operationType = 2
	},
	handleDelete(index, row) {  // 删除
		this.speciValue[index].operationType = 3
		if(this.speciValue[index].id !== undefined)
			this.speciValueTemp.push(this.speciValue[index])
	  this.speciValue.splice(index, 1)
	}
  }
};
</script>
<style rel="stylesheet/scss" lang="scss">
.el-table .cell{
  overflow: hidden;
  white-space:nowrap;
}
.add-specification {
  background-color: white;
	min-height: 100%;
	// height: 90vh;
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
  .foot-btn {
	padding: 20px 0;
  }
  .value-input .el-input__inner {
	background-color: white;
  }
}
</style>
