<template>
  <div class="download-add">
     <header class="header">
      <h4 class="title">{{title}}</h4>
      <el-button class="mini-add-btn btn-right" icon="el-icon-back" @click="cancel">返回列表</el-button>
    </header>
     <div v-loading="loading">
      <el-form ref="form" :model="formData" label-width="150px" class="el-form1" :rules="rules">
        <el-form-item label="一级菜单：" prop="fid">
          <el-select v-model="formData.fid" placeholder="一级菜单"  size="mini" @change="handleSelectFirst">
            <el-option
            v-show="item.titleZh || item.titleEn"
            v-for="item in firstMenu"
            :key="item.id"
            :label="item.titleZh ? item.titleZh: item.titleEn"
            :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="二级菜单：" prop="parentId">
          <el-select v-model="formData.parentId" placeholder="二级菜单" :disabled="disSmenu"  size="mini" @change="handleSelectSecond">
            <el-option
            v-show="item.titleZh || item.titleEn"
            v-for="item in secondMenu"
            :key="item.id"
            :label="item.titleZh ? item.titleZh: item.titleEn"
            :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="标题中文名称：" prop="titleZh">
          <el-input v-model.trim="formData.titleZh"></el-input>
        </el-form-item>
        <el-form-item label="标题英文名称：" prop="titleEn">
          <el-input v-model="formData.titleEn"></el-input>
        </el-form-item>
        <el-form-item label="国内视频链接：" prop="contentUrlZh">
            <el-input v-model.trim="formData.contentUrlZh"></el-input>
        </el-form-item>
        <el-form-item label="国外视频链接：" prop="contentUrlEn">
          <el-input v-model="formData.contentUrlEn"></el-input>
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
    return {
      loading: false,
      id: null,
      title: '',
      action: process.env.BASE_API + 'system/v1/web/admin/oss/sts',
      formData: {},
      rules: {
        fid: [{
          required: true,
          message: '请选择一级菜单',
          trigger: 'blur'
        }],
        parentId: [{
          required: true,
          message: '请选择二级菜单',
          trigger: 'blur'
        }],
        titleZh: [{
          required: false,
          trigger: 'blur'
        }],
        titleEn: [{
          required: false,
          trigger: 'blur'
        }],
        contentUrlZh: [{
          required: false,
          message: '请输入国内视频链接',
          trigger: 'blur'
        }],
        contentUrlEn: [{
          required: false,
          message: '请输入国外视频链接',
          trigger: 'blur'
        }]
      },
      firstMenu: [],
      secondMenu: [],
      selectFid: null,
      disSmenu: false
    }
  },
  created() {
    this.loading = true
    // 获取菜单数据列表
    this.id = this.$route.params.id
    if (this.id) {
      // 编辑
      this.title = '编辑'
      this.getFMenu(0).then(res => {
        if(res.success) {
          this.getDataById(this.id)
        }
      })
    } else {
      this.getFMenu(0)
      // 添加
      this.title = '添加'
      this.formData = {
        fid: null,
        parentId: null,
        titleZh: '',
        titleEn: '',
        contentUrlZh: '',
        contentUrlEn: '',
        thumbnailUrlZh: '',
        thumbnailUrlEn: ''
      }
    }
  },
  methods: {
    // 保存
    handleSave() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.id) {
            // 更新
            this.updateTrain()
          } else {
            // 添加
            this.addTrain()
          }
        } else {
          console.log('error')
          return false;
        }
      })
    },
     // 添加
    addTrain() {
      this.$http.addDownload(this, this.formData).then(res => {  
        if (res.success) {
          this.$router.push({ name: 'downList' })
          this.$message.success('添加成功')
        }
      })
    },
    // 更新
    updateTrain() {
      this.$http.editDownload(this, {    
        id: this.id,
        parentId: this.formData.parentId,
        titleZh: this.formData.titleZh,
        titleEn: this.formData.titleEn,
        contentUrlZh: this.formData.contentUrlZh,
        contentUrlEn: this.formData.contentUrlEn,
        thumbnailUrlZh: this.formData.thumbnailUrlZh,
        thumbnailUrlEn: this.formData.thumbnailUrlEn
      }).then(res => {
        if(res.success) {
          this.$router.push({ name: 'downList' })
          this.$message.success('更新成功')
        }
      })
    },
    // 获取一级菜单列表
    getFMenu(id) {
      return new Promise((resolve, reject) => {
        this.$http.downloadPoList(this, {   
          parentId: id
        }).then(res => {
        if (res.success) {
          this.firstMenu = res.data
          if (this.firstMenu.length > 0) {
            let fid = this.$route.params.fid
            this.selectFid = fid ? fid : this.firstMenu[0].id
            this.formData.fid = this.selectFid
            this.getSMenu(this.selectFid).then(res2 => {
              if (res2.success) {
                resolve(res2) 
              } else {
                reject(res2)
              }
            })
          } else {
            this.loading = false
            this.formData.parentId = null
            resolve(res.code)
          }
        } else {
          this.$message.error(res.msg)
          reject()
        }
      })
      })
    },
    // 获取二级菜单列表
    getSMenu(id) {
      return new Promise((resolve, reject) => {
        this.$http.downloadPoList(this, {   
          parentId: id
        }).then(res => {
          if (res.success) {
            this.disSmenu = false
            this.secondMenu = res.data
            if (this.secondMenu === undefined || this.secondMenu === null || this.secondMenu.length === 0) {
              this.formData.parentId = null
              this.secondMenu = []
              this.$message.warning('暂无数据')
              reject()
            } else {
              this.loading = false
              this.formData.parentId = this.secondMenu[0].id
              resolve(res)
            }
          } else {
            this.loading = false
            reject()
          }
        })
      })
    },
    // 根据Id获取数据信息
    getDataById(id) {
      return new Promise((resolve, reject) => {
        this.$http.downloadDetail(this, {  
          id: id
        }).then(res => {
          if (res.success) {
            this.loading = false
            this.formData = res.data
            this.formData.fid = this.selectFid
            resolve()
          } else {
            this.loading = false
            this.$message.error(res.msg)
          }
        })
      })
    },
    // 选择一级菜单
    handleSelectFirst(val) {
      this.disSmenu = true
      this.getSMenu(val)
    },
    // 选择二级菜单
    handleSelectSecond(val) {
      this.formData.parentId = val
    },
    cancel() {
      this.$router.push({ name: 'downList' })
    }
  }
}
</script>

<style>

</style>