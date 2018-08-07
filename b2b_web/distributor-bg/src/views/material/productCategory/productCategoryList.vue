<template>
  <div id="" class="product-category-list">
    <el-row class="header">
        <el-col :span="4"> 产品类型列表 </el-col>
    </el-row>
    <div class="function">
      <button class="mini-search-btn" @click="add(totalCount)">添加</button>
    </div>
    <div v-loading="loading">
      <el-table :data="items" header-row-class-name="header-row" border class="tr-header" max-height="550">
        <el-table-column label="产品类型ID" prop="id" align="center" :width="180"></el-table-column>
        <el-table-column label="产品类型名称" align="center">
          <template slot-scope="scope">
              <input class = "input-url" v-show="scope.row.isEdit" v-model='scope.row.name' maxlength="12" placeholder="请输入产品类型名称，不超过12个字"/>
              <span v-show="!scope.row.isEdit">{{scope.row.name}}</span>
          </template>
        </el-table-column>
        <el-table-column label="产品类型名称(英文)" align="center">
          <template slot-scope="scope">
              <input class = "input-url" v-show="scope.row.isEdit" v-model='scope.row.englishName' maxlength="12" placeholder="请输入产品类型名称，不超过12个字"/>
              <span v-show="!scope.row.isEdit">{{scope.row.englishName}}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" :width="150">
          <template slot-scope="scope">
            <el-select v-if="scope.row.isEdit"
                       v-model="scope.row.openFlag"
                       size="small">
              <el-option v-for="item in status"
                         :key="item.id"
                         :value="item.id"
                         :label="item.value"></el-option>
            </el-select>
            <span v-show="!scope.row.isEdit">{{scope.row.openFlag===1?'启用':'禁用'}}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" :width="350" align="center">
          <template slot-scope="scope">
            <el-button class="mini-search-btn" @click="handleSave(scope.$index, scope.row)" style="background-color:#21b8cb" v-if="scope.row.isEdit">保存</el-button>
            <button class="mini-search-btn" @click="handleEdit(items[scope.$index])" v-else >编辑</button>
            <el-button  size="mini" @click="handleCancel(scope.$index)" v-if="scope.row.isEdit">取消</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>

export default {
  data () {
    return {
      items: [],
      totalCount: 0,
      loading: false,
      status: [
        { id: 1, value: '启用' },
        { id: 0, value: '禁用' },
      ]
    }
  },
  mounted() {
    this.getItems()
  },
  methods: {
    // 获取列表
    getItems(){
      this.loading = true;
      this.$http.productCategoryList(this, {page:1, size:1000}).then(res =>{
        this.items.splice(0,this.items.length);
        if (res.success) {
          res.data.list.forEach(item =>{
            this.items.push({
              id: item.id,
              name: item.name,
              englishName: item.englishName,
              openFlag: item.openFlag,
              isEdit:false
            })
          })
          this.totalCount = this.items.length
        }
         res.success ? this.loading = false : this.looking = false
      })
      
    },
    // 添加
    add(totalCount) {
      let isEdit = false;
      for (var i = 0; i < this.items.length; i++) {
        if (this.items[i].id == undefined) {
          isEdit = true;
          break;
        }
      }
      if (isEdit) {
        this.$message.error("请先保存新添加的名称！");
        return;
      } else {
        this.items.push({
          name: '',
          englishName: '',
          openFlag: 1,
          isEdit: true
        });
      }
      this.totalCount = totalCount + 1;
    },
    // 保存
    handleSave(index, row){
        if (this.items[index].name.replace(/(^s*)|(s*$)/g, "").length === 0 || this.items[index].englishName.replace(/(^s*)|(s*$)/g, "").length === 0){
          this.$message.error('请先输入产品类型名称！');
        } else {
          if(this.items[index].id == undefined){
            const params = {
              name: this.items[index].name,
              englishName: this.items[index].englishName,
              openFlag: this.items[index].openFlag
            }
            this.$http.addProductCategory(this, params).then(res => {  
              if(res.success){
                this.$message.success('保存成功！');
                this.getItems();
              }
            })
          }else{
            const params = {
              id: this.items[index].id,
              name: this.items[index].name,
              englishName: this.items[index].englishName,
              openFlag: this.items[index].openFlag
            }
            this.$http.editProductCategory(this, params).then(res => {    
              if(res.success){
                this.$message.success('保存成功！');
                this.getItems();
              }
            })
          }
        }
    },
    // 取消
    handleCancel(index){
      this.items[index].isEdit = false;
      if(this.items[index].id == undefined){
        this.items.splice(index,1);
      }else{
        this.getItems();
      }
    },
    // 编辑
    handleEdit(obj) {
      let editLen = 0;
      this.items.forEach(item => {
        if (item.isEdit) {
          editLen++;
        }
      })
      if (editLen <= 0) {
        obj.isEdit = true
      } else {
        this.$message.error("请先保存编辑中的名称！");
      }
    }
  }
}



</script>

<style lang="scss" scoped>
.product-category-list{
    background-color: white;
    min-height: 100%;
    .header {
      background-color: $lakeBlue;
      line-height: 40px;
      color: white;
      padding-left: 20px;
    }
}
.function{
  padding-left: 20px;
  padding-bottom: 10px;
  padding-top: 10px;
  background-color: white;
}

.input-url{
  height: 30px;
  font-size: 14px;
  border-radius: 5px;
  border: 1px solid #ccc;
  font-weight: bold;
  width: 60%;
}
</style>
<style>
  .el-input__suffix{
    right: 14px;
  }
</style>


