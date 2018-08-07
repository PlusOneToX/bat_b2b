<template>
  <div class="add-productline">
    <header v-if="append">
      <h4>添加图库分类</h4>
      <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="handleCancel">
        返回列表
      </el-button>
    </header>
    <header v-if="looking">
      <h4>查看图库分类</h4>
      <el-button class="btn-home" icon="el-icon-d-arrow-left" @click="handleCancel">
        返回列表
      </el-button>
    </header>

    <el-form label-width="150px" :rules="rules" ref="formData" :model="formData" v-loading="vloading">
      <el-form-item label="上级分类" prop="parentName" style="margin-bottom: 10px;">
        <el-input size="mini" clearable @focus="categoryFocus" @blur="focus=false" readonly placeholder="请选择"
          class="category-input" v-model="formData.parentName"></el-input>
        <transition name="el-zoom-in-top">
          <div v-clickoutside="handleClose" class="category-box" v-if="categoryShow">
            <el-tree :data="picCategoryData" :props='props' @node-click="categorySelect" ref="tree" node-key="id">
            </el-tree>
          </div>
        </transition>
      </el-form-item>
      <el-form-item label="图片类型" prop="type" style="margin-bottom: 10px;">
        <el-select v-model="formData.type" placeholder="图片类型" clearable size="mini">
          <el-option v-for="item in types" :key="item.type" :label="item.label" :value="item.type"
            :disabled="item.disabled">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="分类名称" prop="name" style="margin-bottom: 10px;">
        <el-input size="mini" style="width:300px;" v-model="formData.name" placeholder="不超过40个字" maxlength="40">
        </el-input>
      </el-form-item>
      <el-form-item label="分类英文名称" prop="englishName" style="margin-bottom: 10px;">
        <el-input size="mini" style="width:500px;" v-model="formData.englishName" placeholder="不超过80个字" maxlength="80">
        </el-input>
      </el-form-item>
      <el-form-item label="分类图片" prop="image" style="margin-bottom: 10px;">
        <template>
          <el-upload class="avatar-uploader" :action="action" :show-file-list="false" :on-remove="handleRemove"
            :before-upload="beforeUpload">
            <img v-if="formData.image" :src="formData.image" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </template>
      </el-form-item>

      <el-form-item label="分类描述" prop="description" style="margin-bottom: 10px;">
        <el-input style="width:50%;" size="mini" type="textarea" :rows="4" v-model="formData.description"
          placeholder="不超过200个字" maxlength="200"></el-input>
      </el-form-item>
      <el-form-item label="状态" prop="openFlag">
        <el-radio-group v-model="formData.openFlag" size="mini">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">停用</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="是否最终分类" prop="atLastTrademark">
        <el-tooltip class="item" effect="dark" content="选择是，分类下不再有子分类且可作为图片的直接分类，选择否，分类下还有子图片分类且不可作为图片的直接分类"
          placement="right">
          <el-radio-group v-model="formData.atLastTrademark" size="mini">
            <el-radio :label="0">否</el-radio>
            <el-radio :label="1">是</el-radio>
          </el-radio-group>
        </el-tooltip>
      </el-form-item>
      <el-button style="margin-left: 25%; margin-top:30px;" class="mini-search-btn" :loading="loading"
        @click="handleSubmit('formData')">保存</el-button>
      <el-button style="margin-bottom:30px;" size="mini" class="mini-search-btn" @click="handleCancel">返回</el-button>
    </el-form>
  </div>
</template>
<script>
  import uploadSmallImg from './uploadSmallImg.vue'
  import {monthDay} from '@/utils/timeFormat.js'
  export default {
    data() {
      return {
        action: process.env.BASE_API + 'system/v1/web/admin/oss/sts',
        loading: false,
        vloading: false,
        append: true, // 添加材料
        looking: false, // 查看材料
        categorys: [{
          id: 10010,
          name: '手机壳'
        }, {
          id: 10020,
          name: '手机背膜'
        }],
        types: [{
          type: 1,
          label: '普通素材',
          disabled: false
        }, {
          type: 2,
          label: 'IP素材',
          disabled: false
        }, {
          type: 3,
          label: '模板',
          disabled: false
        }, {
          type: 4,
          label: '贴图',
          disabled: false
        }],
        formData: {
          name: '',
          englishName: '',
          parentName: '',
          type: '',
          parentId: '',
          description: '',
          sequence: 0,
          image: '',
          openFlag: 1,
          atLastTrademark: 1,
        },
        rules: {
          type: [{
            required: true,
            message: '请选择图片类型',
          }],
          name: [{
            required: true,
            message: '请输入分类名称',
            trigger: 'blur'
          }],
          parentName: [{
            required: true,
            message: '请选择上级分类',
          }],
          image: [{
            required: true,
            message: "请选择分类图片",
            trigger: "blur",
          }],
        },
        disShow: false,
        imageObj: '',
        focus: false,
        categoryShow: false,
        props: {
          label: 'name',
          children: 'childrenList',
          isLeaf: 'leaf'
        },
        goodsTable: [],
        addMaterials: false,
        picCategoryData: []
      }
    },
    components: {
      uploadSmallImg
    },
    created() {
      this.getTree()
      if (this.$route.params.id != undefined) {
        this.vloading = true
        this.append = false;
        this.looking = true;
        this.$http.pictureCategoryDetail(this, {id: this.$route.params.id}).then(res => { 
          if (res.success) {
            this.formData = res.data
            // 设置当前图片类型，其他不可选
            this.types.forEach((item) => {
              if (item.type === this.formData.type) {
                item.disabled = false;
              } else {
                item.disabled = true;
              }
            })
          }
          this.vloading = false
        })
      } else {
        this.looking = false;
      }
    },
    directives: { //..事件绑定
      clickoutside: {
        bind: function (el, binding, vnode) {
          function documentHandler(e) {
            if (el.contains(e.target)) { //..这里判断点击的元素是否是本身，是本身，则返回
              return false;
            }
            if (binding.expression) { //..判断指令中是否绑定了函数
              binding.value(e) //..如果绑定了函数 则调用那个函数，此处binding.value就是handleClose方法
            }
          }
          el._vueClickOutside_ = documentHandler; //..给当前元素绑定个私有变量，方便在unbind中可以解除事件监听
          document.addEventListener('click', documentHandler);
        },
        unbind: function (el, binding) {
          document.removeEventListener('click', el._vueClickOutside_);
          delete el._vueClickOutside_;
        }
      }
    },
    methods: {
      getTree () {
        this.$http.pictureCategoryPoList(this, {  
          page: 1,
          size: 1000,
          openFlag: 1,
          parentId: 0
        }).then(res => {
          if (res.success) {
            if (res.data.list !== undefined && res.data.list !== null && res.data.list.length > 0) {
              for (let i = 0; i < res.data.list.length; i++) {
                if (res.data.list[i].childrenList.length === 0) {
                  res.data.list[i].leaf = true
                }
              }
               res.data.list.unshift({
                id: 0,
                name: '顶级分类',
                leaf: true,
              })
            }
           this.picCategoryData = res.data.list
          }
          this.loading = false
        })
      },
      getOutImage(index, url) { //外框图
        this.goodsTable[index].outImage = url
      },
      getFloorImage(index, url) { //底图
        this.goodsTable[index].floorImage = url
      },
      formatStatus(row, col, val) {
        switch (val) {
          case 1:
            return '启用';
            break
          case 2:
            return '停用';
            break
        }
      },
      handleChangeStart(row, status) {
        row.openFlag = status
      },
      handleDeleteGood(index) {
        this.goodsTable.splice(index, 1)
      },
      cancelMaterials() {
        this.addMaterials = false
      },
      closeMaterials(val) {
        this.addMaterials = false
        this.goodsTable = []
        for (let i = 0; i < val.length; i++) {
          val[i].id = undefined
        }
        this.goodsTable = this.goodsTable.concat(val)
      },
      addMaterial() {
        this.addMaterials = true
      },
      categorySelect(data, checked, indeterminate) {
        if (data.type) {
          // 获取当前图片类型
          this.formData.type = data.type;
          // 设置当前图片类型，其他不可选
          this.types.forEach((item) => {
            if (item.type === this.formData.type) {
              item.disabled = false;
            } else {
              item.disabled = true;
            }
          })
        } else {
          // 未获取到图片类型，全部可选
          this.types.forEach((item) => {
            item.disabled = false;
          })
        }
        this.formData.parentName = data.name
        this.formData.parentId = data.id
        if (this.categoryShow && !this.focus) {
          this.categoryShow = false
        }
      },
      handleClose() {
        if (this.categoryShow && !this.focus) {
          this.categoryShow = false
        }
      },
      categoryFocus() {
        this.focus = true
        this.categoryShow = true
      },
      beforeUpload(file) {
        if (file.type != 'image/jpeg' && file.type != 'image/bmp' &&
          file.type != 'image/jpg' && file.type != 'image/jpeg' &&
          file.type != 'image/png' && file.type != 'image/gif') {
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
            secure: true
          })
          // 上传
          client.multipartUpload('flexible/picture/' + monthDay(new Date()) + '/' + random_name, file, {}).then((results) => {
            return new Promise((resolve, reject) => {
              this.formData.image = result.data.hostname + results.name
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
        for (let i = 0; i < len; i++) {
          pwd += chars.charAt(Math.floor(Math.random() * maxPos));
        }
        return pwd;
      },
      handleRemove(file) {
        this.formData.image = ''
      },
      proving() {
        this.formData.sequence = this.formData.sequence.replace(/[^\.\d]/g, '');
        this.formData.sequence = this.formData.sequence.replace('.', '');
        if (Number(this.formData.sequence) > 100000) {
          this.formData.sequence = 100000
        }
      },
      // 保存
      handleSubmit(data) {
        this.$refs[data].validate(valid => {
          if (valid) {
            this.loading = true;
            if (this.formData.id !== undefined && this.formData.id !== null && this.formData.id !== '') { // 编辑
              this.$http.editPictureCategory(this, this.formData).then(res => { 
                if (res.success) {
                  this.$message.success("编辑成功")
                  this.$router.go(-1)
                }
                res.success ? this.loading = false : this.loading = false
              })
            } else { // 新增
              this.$http.addPictureCategory(this, this.formData).then(res => { 
                if (res.success) {
                  this.$message.success("新增成功")
                  this.$router.go(-1)
                }
                res.success ? this.loading = false : this.loading = false
              })
            }
          }
        })
      },
      handleCancel() { // 返回操作
        this.$router.go(-1)
      },
    }
  }

</script>
<style lang="scss">
  .add-productline {
    background-color: white;
    min-height: 100%;

    // padding-top: 20px;
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

    .btn-home {
      float: right;
      padding: 5px;
      margin-top: 8px;
      margin-right: 8px;
      margin-left: 0;
      font-size: 12px;
    }

    .avatar-uploader .el-upload {
      width: 120px;
      height: 120px;
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
