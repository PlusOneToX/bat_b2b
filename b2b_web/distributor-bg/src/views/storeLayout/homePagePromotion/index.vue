<template>
  <div id="" class="home-promotion-list">
    <el-row class="header">
        <el-col :span="2"> 首页推广 </el-col>
    </el-row>
    <div class="function">
      <button class="mini-search-btn" @click="add(totalCount)">添加</button>
      <span style="color:#777;font-size: 14px">（图片建议尺寸：1920*550）</span>
    </div>
    <div v-loading="loading">
      <el-table :data="items" header-row-class-name="header-row" border class="tr-header" max-height="550">
      <!-- <el-table-column label="序号" prop="id" align="center" :width="100"></el-table-column> -->
        <el-table-column label="图片" :width="200" align="center">
          <template slot-scope="scope">
            <upload-small-img class="avatar-uploader"
                              :item = 'scope.row'
                              :isEdit ='scope.row.isEdit'
                              :index = 'scope.$index'
                              @getImgUrl = "getImgUrl">
            </upload-small-img>
          </template>
        </el-table-column>
        <el-table-column label="推广区域" align="center" :width="150">
          <template slot-scope="scope">
            <el-select v-if="scope.row.isEdit"
                       v-model="scope.row.bannerArea"
                       size="small">
              <el-option v-for="item in areaList"
                         :key="item.id"
                         :value="item.id"
                         :label="item.value"></el-option>
            </el-select>
            <span v-show="!scope.row.isEdit">{{getAreaValue(scope.row.bannerArea)}}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column label="排序" align="center" prop="sort">
        </el-table-column> -->
        <el-table-column label="链接地址" align="center">
          <template slot-scope="scope">
              <input class = "input-url" v-model='scope.row.bannerUrl' placeholder="http://" v-show="scope.row.isEdit"/>
              <span v-show="!scope.row.isEdit">{{scope.row.bannerUrl}}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" :width="350" align="center">
          <template slot-scope="scope">
            <el-button class="mini-tableadd-btn" @click="handleUp(scope.$index,scope.row)">上移</el-button>
            <el-button class="mini-freeze-btn" @click="handleDown(scope.$index,scope.row)">下移</el-button>
            <el-button class="mini-search-btn" @click="handleSave(scope.$index, scope.row)" style="background-color:#21b8cb" v-if="scope.row.isEdit">保存</el-button>
            <button class="mini-search-btn" @click="handleEdit(items[scope.$index])" v-else >编辑</button>
            <el-button  size="mini" @click="handleCancel(scope.$index)" v-if="scope.row.isEdit">取消</el-button>
            <el-button v-else class="mini-delete-btn" @click="handleDelete(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import addImg from './addImg'
import uploadSmallImg from "@/views/storeLayout/compomemts/uploadSmallImg"

export default {
  name: 'homePagePromotion',
  components: {
  addImg,
  uploadSmallImg
  },
  data () {
    return {
      items: [],
      totalCount: 0,
      loading: false,
      areaList: [
        { id: 0, value: '国内' },
        { id: 1, value: '国外' },
        { id: 2, value: '国内外' }
      ],
    }
  },
  mounted() {
    this.getItems()
  },
  methods: {
    add(totalCount) {
      if (this.items.length > 12) {
        this.$message.error("最多只能添加12张推广图片！");
      } else {
        let isEdit = false;
        for (var i = 0; i < this.items.length; i++) {
          if (this.items[i].id == undefined) {
            isEdit = true;
            break;
          }
        }
        if (isEdit) {
          this.$message.error("请先保存新添加的推广图片！");
          return;
        } else {
          this.items.push({
            component: "add-img",
            imageUrl: "",
            bannerUrl: "",
            isEdit: true,
          });
        }
        this.totalCount = totalCount + 1;
      }
    },
    deleteImg(index){
        this.items.splice(index-1,1);
    },
    getItems(){
      this.loading = true;
      this.$http.bannerList(this, {page:1, size:1000}).then(res =>{
        this.items.splice(0,this.items.length);
        if (res.success) {
          res.data.list.forEach(item =>{
            this.items.push({
              id: item.id,
              component: "add-img",
              bannerArea: item.bannerArea,
              imageUrl: item.imageUrl,
              bannerUrl: item.bannerUrl,
              sort: item.sort,
              isEdit:false
            })
          })
          this.totalCount = this.items.length
        }
         res.success ? this.loading = false : this.looking = false
      })
      
    },
    getImgUrl(index,url){
      this.items[index].imageUrl = url
    },
    handleUp(index, row) {  // 上移
      this.$http.bannerUp(this, {id: row.id}).then(res => {
        if (res.success) {
          this.getItems()
        }
      })
    },
    handleDown(index, row) {  // 下移
      this.$http.bannerDown(this, {id: row.id}).then(res => {
        if (res.success) {
          this.getItems()
        }
      })
    },
    handleDelete(index) {
      if(this.items[index].id != undefined){
          this.$confirm('删除此推广图片?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning',
              center: true
          }).then(() => {
              this.$http.delBanner(this, { id: this.items[index].id }).then(res => {
                if(res.success) {
                    this.$message.success({
                        message: '删除成功',
                        duration: 3 * 1000,
                        onClose: () => { }
                    })
                    this.items.splice(index,1);
                } else {
                  this.$confirm(
                    item.id + '此推广图片删除失败',
                    '提示', {
                        center: true,
                        showClose: false,
                        showCancelButton: false
                    })
                }
              })
          })
      }else {
        this.items.splice(index,1);
      }
    },
    handleSave(index, row){
        if(this.items[index].imageUrl.replace(/(^s*)|(s*$)/g, "").length === 0){
          this.$message.error('请先选择推广图片！');
        }else if (this.items[index].bannerUrl.replace(/(^s*)|(s*$)/g, "").length === 0){
          this.$message.error('请先输入点击跳转链接！');
        }else{
          if(this.items[index].id == undefined){
            const params = {
              imageUrl: this.items[index].imageUrl,
              bannerUrl: this.items[index].bannerUrl,
              bannerArea: this.items[index].bannerArea,
              sort: 0
            }
            this.$http.addBanner(this, params).then(res => {  
              if(res.success){
                this.$message.success('保存成功！');
                this.getItems();
              }
            })
          }else{
            const params = {
              id: this.items[index].id,
              imageUrl: this.items[index].imageUrl,
              bannerUrl: this.items[index].bannerUrl,
              bannerArea: this.items[index].bannerArea,
              sort: this.items[index].sort
            }
            this.$http.editBanner(this, params).then(res => {    
              if(res.success){
                this.$message.success('保存成功！');
                this.getItems();
              }
            })
          }
        }
    },
    handleCancel(index){
      this.items[index].isEdit = false;
      if(this.items[index].id == undefined){
        this.items.splice(index,1);
      }else{
        this.getItems();
      }
    },
    /**
     * 推广地区描述转换
     * 施义煌
     * @param id
     */
    getAreaValue(id) {
      let returnStr = '';
      this.areaList.forEach(item => {
        if (item.id === Number(id)) {
          returnStr = item.value;
        }
      })
      return returnStr;
    },
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
        this.$message.error("请先保存编辑中的推广图片！");
      }
    }
  }
}



</script>

<style lang="scss" scoped>
.home-promotion-list{
    background-color: white;
    min-height: 100%;
    .header {
    background-color: $lakeBlue;
    line-height: 40px;
    color: white;
    padding-left: 20px;
    }
}
.upload-image{
   margin-left: 10px;
   margin-right: 10px;
   border-radius: 5px;
   border: 1px solid #ccc;
   background-color: #f8f8f8;
   .btn-delete{
        color: #fff;
        background-color: #e6a23c;
        border-radius: 6px;
    }
    .btn-add{
      color: #fff;
      background-color: #21b8cb;
      border-radius: 6px;
    }
    .no-image{
      margin: 20px;
    }
 }
.function{
      padding-left: 20px;
      padding-bottom: 10px;
      padding-top: 10px;
      background-color: white;
      .btn-delete{
        color: #fff;
        background-color: #e6a23c;
        border-radius: 6px;
      }
      .btn-add{
        color: #fff;
        background-color: #21b8cb;
        border-radius: 6px;
      }
    }

.input-url{
  height: 30px;
  font-size: 14px;
  border-radius: 5px;
  border: 1px solid #ccc;
  font-weight: bold;
  width: 100%;
}
</style>
<style>
  .el-input__suffix{
    right: 14px;
  }
</style>



