<template>
  <div class="picture-wrapper">
    <div style="margin-bottom:10px;">
			<el-select v-model="pageInfo.type" placeholder="图片类型" clearable size="mini" style="width: 100px;">
				<el-option
				v-for="item in types"
				:key="item.type"
				:label="item.label"
				:value="item.type">
				</el-option>
			</el-select>
			<el-select v-model="pageInfo.openFlag" placeholder="图片状态" clearable size="mini" style="width: 100px;">
				<el-option
				v-for="item in statuss"
				:key="item.status"
				:label="item.label"
				:value="item.status">
				</el-option>
			</el-select>
			<el-select class="custom_select" placeholder="图片分类" size="mini" v-model="pageInfo.categoryId" clearable>
				<el-option :key="item.id" :label="item.name" :value="item.id" v-for="item in pictureCategoryList"></el-option>
			</el-select>
			<el-button style="margin-left: 10px;float: right; " class="mini-search-btn" @click="filter()">搜索</el-button>
			<el-input size="mini" style="width: 200px;float: right;" placeholder="图片名称" clearable v-model.trim="pageInfo.content" @change="changeContent" />
		</div>
		<el-table
			border
			class="tableCenter"
			:data="tableData"
			ref="multipleSelect"
			tooltip-effect="dark"
			@select="select"
			@select-all="selectAll"
			header-row-class-name="header-row"
			:height="tableHeight"
			@selection-change="handleSelectionChange" v-loading="loading">
			<el-table-column align="center" type="selection" width="55" ></el-table-column>
			<el-table-column align="center" label="图片名称" prop="name" show-overflow-tooltip></el-table-column>
      <el-table-column align="center" label="图片类型" prop="type" :formatter="formatType"></el-table-column>
			<el-table-column align="center" label="图片分类" prop="categoryName"></el-table-column>
			<el-table-column align="center" label="图片状态" prop="openFlag" :formatter="formatStatus"></el-table-column>
		</el-table>
		<page :page="pageInfo.page"  :total="total" @sizeChange="changeFormCount" @currentChange="changeFormPage"></page>
		<div class="foot-btn" style="text-align: center;">
			<el-button class="mini-search-btn" :loading="loading" @click="handleSubmit()">确定</el-button>
			<el-button  size="mini"  @click="handleCancel">返回</el-button>
		</div>
  </div>
</template>

<script>
import page from "@/components/pagination/index"
export default {
  components: { page },
	props: {
		selectItem: undefined
	},
  	data() {
		return {
			tableHeight: 600, //给表格高度一个默认高度，以防没有计算到表格高度
			content: '',
			pageInfo: {
				page: 1,
				size: 10,
				type:'',// 图片类型
				openFlag:1,// 图片状态
				categoryId:'' // 图片分类
			},
			tableData: [],
			total: 0,
			loading: false,
			selectPictureIds:[],
			references:[],
			multipleSelect:[],
			selected:[],
			isSelect:false,
			grades:[],
			types:[{
				type:1,
				label:'普通素材'
			},{
				type:2,
				label:'IP素材'
			}],
			statuss:[{
				status:1,
				label:'启用'
			},{
				status:0,
				label:'停用'
			}],
			pictureCategoryList:[], // 图片分类
			deletePictureGoodsReference:[], // 删除关系
			addPictureGoodsReference:[], // 增加关系
			isNetworkGallery:0,
			oldNetworkGallery:0,
		}
	},
	created() {
		const documentHeight = document.body.clientHeight;
		this.tableHeight = parseInt(documentHeight  *  0.8  -  100); // 计算表高度，固定表头
		this.getPictureCategoryList()
    this.getPictureList()
	},
	methods: {
		formatStatus(row, col, val){
			switch(val) {
				case 0:
				return "停用";
				break;
				case 1:
				return "启用";
				break;
			}
		},
		formatType(row, col, val){
			switch(val) {
				case 1:
				return "普通素材";
				break;
				case 2:
				return "IP素材";
				break;
			}
		},
		changeContent(val){
			if(val === undefined || val === '' || val === null){
				this.filter()
			}
		},
    // 获取图片最终分类列表
		getPictureCategoryList(){
			this.$http.pictureCategoryPoList(this, {page:1,size:1000,atLastTrademark:1}).then(res => {
				if (res.success) {
					this.pictureCategoryList = res.data.list
				}
			})
		},

		getPictureList(){// 获取图片列表
			this.loading = true
			this.$http.pictureList(this, this.pageInfo).then(res => {	
				if(res.success){
					this.tableData = res.data.list
					this.total = res.data.total
				}
				this.selectRow()
				this.loading = false
			})
		},
		changeFormCount(val){
			this.pageInfo.page = 1
			this.pageInfo.size = val
			this.getPictureList()
		},
		changeFormPage(val){
			this.pageInfo.page = val
			this.getPictureList()
		},
		selectable(row, index) { //..是否可勾选
			return true
		},
		selectRow(){ // 请求数据变化时，重新选择行（如，删除、数据变化）
			this.$refs.multipleSelect.clearSelection();
			this.$nextTick(() => {
				this.multipleSelect.forEach(row1 => {
					this.tableData.forEach(row2 => {
						if(row1.id === row2.id){
							this.$refs.multipleSelect.toggleRowSelection(row2);
						}
					})
				});
			})
		},
		select(selection, row){ // 单选时调用
			this.isSelect = true
			let d = false;
      if (this.multipleSelect.length > 0) {
        for (let i = 0; i < this.multipleSelect.length; i++) {
          if (this.multipleSelect[i].id === row.id) {
            this.multipleSelect.splice(i, 1);
            d = true;
            break;
          }
        }
      }
      if (!d) {
        this.multipleSelect.push(row);
        this.multipleSelect = this.setArr(this.multipleSelect);
      }
			
		},
		selectAll(selection){ // 全选时调用
			// 全选时调用
      this.isSelect = true;
      if (selection.length === 0) {
        this.tableData.forEach((row) => {
          for (let i = 0; i < this.multipleSelect.length; i++) {
            if (this.multipleSelect[i].id === row.id) {
              this.multipleSelect.splice(i, 1);
              break;
            }
          }
        });
      } else {
        this.multipleSelect = this.setArr(
          this.multipleSelect.concat(selection)
        );
      }
		},
		setArr(arr){ // 去重
			let obj ={};
			let temp=[];
			for( let i = 0; i < arr.length; i++ ) {
				let type= Object.prototype.toString.call(arr[i].pictureId);//不加类型 分不清 1 '1'
					if( !obj[ arr[i].pictureId +type] ) {
						temp.push( arr[i] );
						obj[ arr[i].pictureId+ type ] =true;//这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
					}
				}
			return temp;
		},
		handleSelectionChange(val) { // 当切换页面时的作用
			 // 当切换页面时的作用
      if (
        val.length === 0 &&
        this.multipleSelect.length !== 0 &&
        !this.isSelect
      ) {
        this.multipleSelect.forEach((row1) => {
          this.tableData.forEach((row2) => {
            if (row1.id === row2.id) {
              this.$refs.multipleSelect.toggleRowSelection(row2);
            }
          });
        });
        this.isSelect = true;
      }
		},
		filter() { // 搜索操作
			this.pageInfo.page = 1
			this.pageInfo.size = 10
			this.getPictureList()
		},
		handleSubmit() { // 确定操作
      this.$emit('submit',this.multipleSelect)
		},
		handleCancel() { // 返回操作
			this.$emit('cancel')
		}
	},
	watch: {
		selectItem(val) {// 货品ID变化时重新请求
			this.multipleSelect = []
      this.multipleSelect = this.multipleSelect.concat(this.selectItem)
      this.selectRow();
		},
		'pageInfo.type': {
			handler() {
				this.pageInfo.page = 1
				this.getPictureList()
			},
			deep: true
		},
		'pageInfo.openFlag': {
			handler() {
				this.pageInfo.page = 1
				this.getPictureList()
			},
			deep: true
		},
		'pageInfo.categoryId': {
			handler() {
				this.pageInfo.page = 1
				this.getPictureList()
			},
			deep: true
		},
	}
}
</script>

<style lang="scss" scoped>
.picture-wrapper {
	background-color: white;
	.header-row {
		@include table-header-color;
	}
	.foot-btn {

		margin-top:50px;
	}
	.el-dialog__body{
		padding: 10px 20px;
	}
}
</style>
