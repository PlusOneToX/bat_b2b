<template>
  <div class="train-category-add">
     <header class="header">
      <h4 class="title">添加</h4>
      <el-button class="mini-add-btn btn-right" icon="el-icon-back" @click="cancel">返回列表</el-button>
    </header>
    <div v-loading="loading">
      <el-form ref="form" :model="formData" label-width="150px" class="el-form1" :rules="rules">
        <el-form-item>
         <el-radio-group v-model="radioMenu" @change="ChangeRadio">
          <el-radio :label="1">一级分类：</el-radio>
          <el-radio :label="2">二级分类：</el-radio>
        </el-radio-group>
        </el-form-item>
        <el-form-item label="一级菜单：" prop="parentId" v-if="radioMenu===2 && firstMenu">
          <el-select v-model="formData.parentId" placeholder="一级菜单"  size="mini" @change="handleSelectFirst">
            <el-option
            v-show="item.titleZh || item.titleEn"
            v-for="item in firstMenu"
            :key="item.id"
            :label="item.titleZh ? item.titleZh: item.titleEn"
            :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="一级分类中文名称：" prop="titleZh" v-if="radioMenu===1">
          <el-input v-model.trim="formData.titleZh"></el-input>
        </el-form-item>
        <el-form-item label="二级分类中文名称：" prop="titleZh" v-else>
          <el-input v-model.trim="formData.titleZh"></el-input>
        </el-form-item>
        <el-form-item label="一级分类英文名称：" prop="titleEn" v-if="radioMenu===1">
          <el-input v-model="formData.titleEn"></el-input>
        </el-form-item>
        <el-form-item label="二级分类英文名称：" prop="titleEn" v-else>
          <el-input v-model="formData.titleEn"></el-input>
        </el-form-item>
      </el-form>
     
      <div class="clearfix footbtn">
        <el-col :span="1" :offset="12">
          <el-button class="mini-search-btn" @click="handleSave('form')" >保存</el-button>
        </el-col>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return{
      loading: false,
      radioMenu: 1,
      firstMenu: [],
      formData: {
        parentId: null,
        titleZh: '',
        titleEn: ''
      },
      rules: {
        parentId: [{
          required: true,
          message: '请选择一级菜单',
          trigger: 'blur'
        }],
        titleZh: [{
          required: true,
          message: '请输入中文名称',
          trigger: 'blur'
        }],
        titleEn: [{
          required: true,
          message: '请输入英文名称',
          trigger: 'blur'
        }]
      }
    }
  },
  created() {
    this.getMenuList()
  },
  methods: {
    ChangeRadio(val) {
      if (val === 2) {
        this.formData.parentId = this.firstMenu.length>0 ? this.firstMenu[0].id: null
      } else {
        this.formData.parentId = 0
      }
    },
    getMenuList() {
      this.$http.trainingPoList(this, {  
        parentId: 0
      }).then(res => {
        if (res.success) {
          this.firstMenu = res.data
          let sid = (this.firstMenu.length>0) ? this.firstMenu[0].id : null
          this.formData.parentId = this.radioMenu === 1 ? 0 : sid
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    // 保存
    handleSave() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.$http.addTraining(this, this.formData).then(res => {    
            if (res.success) {
              this.$message.success('添加成功')
              this.$router.push({name: 'trainCategoryList'})
            }
          })
        }
      })
    },
    // 更换一级菜单
    handleSelectFirst(val) {
      this.formData.parentId = val
    },
    // 返回列表
    cancel() {
      this.$router.push({name: 'trainCategoryList'})
    }
  }
}
</script>

<style lang="scss">
  .train-category-add{
    .el-form1{
      width: 700px;
      margin: 30px 0 0 40px;
    }
    .footbtn{
      width:50%;
    }
  }
</style>