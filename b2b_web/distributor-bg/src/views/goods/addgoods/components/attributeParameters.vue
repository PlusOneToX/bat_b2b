<template>
  <div class="components-container">
    <el-form class="editor-content" label-width="150px">
      <el-form-item label="商品标签" style="margin-bottom: 0px;">
        <el-button class="mini-search-btn" @click="addLabel()" icon="el-icon-plus" >添加标签</el-button>
      </el-form-item>
      <el-form-item>
        <el-table :data="labels" border header-row-class-name="header-row" class="tableCenter goods-table" style="width: 70%" max-height="200">
          <el-table-column align="center" label="标签名称" prop="name"> </el-table-column>
          <el-table-column align="center" label="标签英文名称" prop="nameEn"> </el-table-column>
          <el-table-column align="center" label="操作" width="250" >
            <template slot-scope="scope">
              <el-button class="mini-tableadd-btn" @click="handleUp(scope.$index,scope.row)">上移</el-button>
							<el-button class="mini-freeze-btn" @click="handleDown(scope.$index,scope.row)">下移</el-button>
              <el-button class="mini-delete-btn" @click="handleDelete(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
      <el-form-item label="商品参数" style="margin-bottom: 0px;" >
        <el-button class="mini-search-btn" @click="addParams()" icon="el-icon-plus" >添加参数</el-button>
      </el-form-item>
      <el-form-item>
        <el-table :data="params" border header-row-class-name="header-row" class="tableCenter goods-table" style="width: 70%" max-height="200">
          <el-table-column align="center" label="参数名称" prop="paramName">
            <template slot-scope="scope">
              <el-select @change="changeSpe(scope.$index)" size="mini"  v-model="scope.row.id" placeholder="请选择">
                <el-option
                  v-for="item in speList"
                  :key="item.id"
                  :label="item.name"
                  :disabled="item.disabled"
                  :value="item.id">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column align="center" label="参数英文名称" prop="paramNameEn"></el-table-column>
          <el-table-column align="center" label="参数值" prop="paramValueName">
            <template slot-scope="scope">
              <el-select size="mini" @change="changeSpeValue(scope.$index)" v-model="scope.row.valueId" placeholder="请选择">
                <el-option
                  v-for="item in scope.row.spe.values"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column align="center" label="参数英文值" prop="paramValueNameEn"> </el-table-column>
          <el-table-column align="center" label="操作" width="250">
            <template slot-scope="scope">
              <el-button class="mini-delete-btn" @click="handleDeleteSpe(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
    </el-form>
    <!-- 查看商品dialog -->
		<el-dialog :modal-append-to-body="false" :visible="showAddLabels" :before-close="closeAddLabels">
			<el-table :data="labelData" ref="labelSelect"
          tooltip-effect="dark"
          @select="select"
          @select-all="selectAll"
          style="text-align:center;"
          border header-row-class-name="header-row" 
          @selection-change="handleSelectionChange">
        <el-table-column align="center" type="selection" width="55"></el-table-column>
				<el-table-column align="center" label="标签名称" prop="name"></el-table-column>
				<el-table-column align="center" label="标签英文名称" prop="nameEn"></el-table-column>
        <el-table-column align="center" label="标签状态" prop="openFlag" :formatter="formatStatus"></el-table-column>
			</el-table>
      <page :page="pageInfo.page" :total="atotalCount" @sizeChange="handleSizeChange" @currentChange="handleCurrentChange"></page>
      <el-button class="mini-search-btn" style="margin-left:47%;" @click="handleSubmit">确定</el-button>
			<el-button size="mini" @click="closeAddLabels">返回</el-button>
		</el-dialog>
  </div>
</template>

<script>
import page from '@/components/pagination'
import { swapItem } from "@/utils/index";
export default {
  name: 'attribute',
  props:['goodsLabelRefs','goodsParam'],
  components: {
		page
  },
  data() {
    return {
      labels:[],
      labelData:[],
      multipleSelect:[],
      speList:[],
      speValueList:[],
      params:[],
      showAddLabels:false,
      pageInfo: {
				page: 1,
				size: 10,
				// name: '',
				// status:undefined
        content: '',
        openFlag: undefined
      },
      atotalCount:0
    }
  },
  created(){
    this.getSpe()
  },
  methods:{
    changeSpe(index){
      this.params[index].valueId = ''
      this.params[index].paramValueNameEn = ''
      for(let i = 0;i<this.speList.length;i++){
        if(this.speList[i].id === this.params[index].id){
          if(this.params[index].spe.id !== undefined){
            this.params[index].spe.disabled = false
          }
          this.params[index].spe = this.speList[i]
          this.params[index].spe.disabled = true
          this.params[index].paramNameEn = this.params[index].spe.nameEn
          break
        }
      }
      if(this.params[index].spe !== undefined && this.params[index].spe.id !== undefined && this.params[index].spe.values.length === 0){
        this.getSpeValue(this.params[index].spe)
      }
    },
    changeSpeValue(index){
      for(let i = 0;i<this.params[index].spe.values.length;i++){
        if(this.params[index].spe.values[i].id === this.params[index].valueId){
          this.params[index].paramValueNameEn = this.params[index].spe.values[i].nameEn
          break
        }
      }
    },
    getSpe(){
      this.$http.getAttributePoList(this, {page:1,size:1000,attributeType: 3}).then(res => {  
        if(res.success){
          res.data.list.forEach(item =>{
            item.values = []
            if(this.params !== undefined && this.params.length>0){
              for(let i = 0;i<this.params.length;i++){
                if(item.id === this.params[i].id){
                  this.params[i].spe = item
                  break
                }
              }
            }
          })
          this.speList = res.data.list;
        }
      })
    },
    getSpeValue(spe){
      this.$http.getAttributeDetail(this, {id:spe.id}).then(res => {
        spe.values = res.values;
      })
    },
    handleUp(index, row) {  // 上移
      if (this.labels.length > 1 && index !== 0) {
      this.labels = swapItem(this.labels, index, index - 1);
      }
    },
    handleDown(index, row) {  // 下移
      if (this.labels.length > 1 && index !== this.labels.length - 1) {
        this.labels = swapItem(this.labels, index, index + 1);
      }
    },
    handleDelete(index){
      this.labels.splice(index,1)
    },
    handleDeleteSpe(index){
      this.params[index].spe.disabled = false
      this.params.splice(index,1)
    },
    selectRow(){//商品请求数据变化时，重新选择行（如，删除、数据变化）
      this.$refs.labelSelect.clearSelection();
      this.multipleSelect.forEach(row1 => {
        this.labelData.forEach(row2 => {
          if(row1.id === row2.id){
            this.$refs.labelSelect.toggleRowSelection(row2);
          }
        })
      });
    },
    handleSizeChange(val){
      this.pageInfo.size = val
      this.pageInfo.page = 1
      this.getTableData()
    },
    handleCurrentChange(val){
      this.pageInfo.page = val
      this.getTableData()
    },
    getTableData() {
      this.$http.getTagList(this, this.pageInfo).then(res => {
        if (res.success) {
          this.labelData = res.data.list
          this.atotalCount = res.data.total
          this.multipleSelect.forEach(row1 => {//重新获 取数据时，判断哪些选中了
            this.labelData.forEach(row2 => {
              if(row1.id === row2.id){
                this.$refs.labelSelect.toggleRowSelection(row2);
              }
            })
          })
        }
      })
		},
    select(selection, row){//单选时调用
      let d = false
      for(let i = 0;i<this.multipleSelect.length;i++){
        if(this.multipleSelect[i].id === row.id){
          this.multipleSelect.splice(i,1)
          d = true
          break
        }
      }
      if(!d){
        if(row.status === 0){
					this.$message.error("停用的标签不能被选择！")
					this.selectRow()
					return
				}
        this.multipleSelect.push(row)
        this.multipleSelect = this.setArr(this.multipleSelect)
      }
    },

    selectAll(selection){//全选时调用
      let arr = []
      arr = arr.concat(selection)
      if(selection.length === 0){
        this.labelData.forEach(row => {
          for(let i = 0;i<this.multipleSelect.length;i++){
            if(this.multipleSelect[i].id === row.id){
              this.multipleSelect.splice(i,1)
              break
            }
          }
        })
      }else{
        for(let j = 0;j<arr.length;j++){
					if(arr[j].status === 0){
            arr.splice(j,1)
            j--
            continue
					}
        }
        this.multipleSelect = this.setArr(this.multipleSelect.concat(arr))
				if(arr.length != selection.length){
					this.selectRow()
				}
      }
    },

    setArr(arr){ //去重
      let obj ={};  
      let temp=[];  
      for( let i = 0; i < arr.length; i++ ) {  
        let type= Object.prototype.toString.call(arr[i].id);//不加类型 分不清 1 '1'    
          if( !obj[ arr[i].id +type] ) {  
            temp.push( arr[i] );  
            obj[ arr[i].id+ type ] =true;//这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读  
          }
        } 
      return temp; 
    },
    
    handleSelectionChange(val) {//当切换页面时的作用
      if(val.length === 0 && this.multipleSelect.length != 0){
        this.multipleSelect.forEach(row1 => {
          this.labelData.forEach(row2 => {
            if(row1.id === row2.id){
              this.$refs.labelSelect.toggleRowSelection(row2);
            }
          })
        });
      }
    },
    formatStatus(row, col, val) {
			if(val) {
				return '启用'
			} else {
				return '停用'
			}
		},
    addLabel(){
      this.showAddLabels = true
      this.multipleSelect = []
      this.multipleSelect = this.multipleSelect.concat(this.labels)
      this.getTableData()
    },
    closeAddLabels(){
      this.showAddLabels = false
      this.multipleSelect = []
      this.multipleSelect = this.multipleSelect.concat(this.labels)
      this.selectRow()
    },
    handleSubmit(){
      this.showAddLabels = false
      this.labels = []
      this.labels = this.labels.concat(this.multipleSelect)
    },
    addParams(){
      this.params.push({
        id:'',
        valueId:'',
        paramNameEn:'',
        paramValueNameEn:'',
        spe:{
          values:[]
        }
        
      })
    }
  },
  watch:{
    goodsLabelRefs(val){
      this.labels = []
      this.labels = this.labels.concat(val)
      this.multipleSelect = []
      this.multipleSelect = this.multipleSelect.concat(this.labels)
    },
    goodsParam(val){
      val.forEach(item =>{
        item.spe = {
          values:[]
        }
      })
      this.params = []
      this.params = this.params.concat(val)
    }
  }
}
</script>

<style scoped>
.editor-content{
  margin-top: 20px;
}
.title{
  padding: 10px;
  color: #000;
}
</style>


