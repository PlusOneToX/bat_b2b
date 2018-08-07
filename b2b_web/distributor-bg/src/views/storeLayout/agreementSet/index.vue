<template>
  <div class="agreement-wrap">
    <header class="header">
      <h4 class="title">协议配置</h4>
      <el-button class="mini-add-btn btn-right" icon="el-icon-plus" @click="handleAdd">添加</el-button>
    </header>
    <!----协议配置数据列表----->
    <el-table :data="tableData" header-row-class-name="header-row" border style="text-align:center;"  v-loading="loading">
      <el-table-column align="center" label="ID" prop="id" width="60"></el-table-column>
      <el-table-column align="center" label="协议名称" prop="name"  width="260">
      </el-table-column>
      <el-table-column align="center" label="类型" :formatter="formatType" prop="type">
      </el-table-column>
      <el-table-column align="center" label="所属品牌" prop="brandName" width="280" show-overflow-tooltip>
      </el-table-column>
      <el-table-column align="center" label="发布区域" prop="agreementArea" :formatter="formatArea" width="120" show-overflow-tooltip>
      </el-table-column>
      <el-table-column align="center" label="协议内容" prop="agreementArea" width="100" show-overflow-tooltip>
        <template slot-scope="scope">
          <div class="item-select" @click="handleSelect(scope.row)">查看</div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="状态" prop="status" :formatter="formatStatus" show-overflow-tooltip>
      </el-table-column>
      <el-table-column label="操作" :width="350" align="center">
        <template slot-scope="scope">
          <button class="mini-search-btn" @click="handleEdit(scope.row)">编辑</button>
          <el-button class="mini-delete-btn" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
    <!---添加、编辑协议---->
    <el-dialog :loading="loading" :visible.sync="dialogVisible" :title="dialogTitle">
       <el-form :model="formData" :rules="rules" label-width="100px" ref="formData">
         <el-form-item label="发布区域" prop="agreementArea">
          <el-radio-group v-model="formData.agreementArea">
            <el-radio :label="0">国内</el-radio>
            <el-radio :label="1">国外</el-radio>
            <!-- <el-radio :label="2">国内和国外</el-radio> -->
          </el-radio-group>
        </el-form-item>
        <el-form-item label="协议标题" prop="name">
          <el-input v-model="formData.name" size="mini" maxlength="20" clearable/>
        </el-form-item>
        <el-form-item label="协议类型" prop="type">
          <el-radio-group v-model="formData.type">
            <el-radio :label="2">用户协议</el-radio>
            <el-radio :label="1">品牌协议</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="所属品牌" prop="brandId" v-if="formData.type===1">
          <el-input @focus="brandFocus" @blur="focus=false"  readonly placeholder="请选择" class="category-input" v-model="brandName"></el-input>
          <transition name="el-zoom-in-top">
            <div v-clickoutside="handleClose" class="category-box" v-if="brandShow">
              <el-tree @check-change="brandChange" show-checkbox :data='brandList' :props="defaultProps" node-key="id" :default-checked-keys="brandIds"  ref="tree" ></el-tree>
            </div>
          </transition>
        </el-form-item>
        <el-form-item label="协议内容" prop="content" class="item-content">
          <tinymce :height="300" v-model="formData.content" :key="formData.id"></tinymce>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div class="button">
        <el-button size="mini" @click="handleCancel">取 消</el-button>
        <el-button class="mini-search-btn" @click="handleSubmit" :loading="loading">确 定</el-button>
      </div>
    </el-dialog>
    <!---协议内容---->
    <el-dialog class="dialog-content-agreen" :modal-append-to-body="false" :visible.sync="dialogVisible2" title="查看协议">
        <div class="mask"></div>
        <tinymce :height="300" v-model="agreContent" ref="tinymce"></tinymce>
    </el-dialog>
  </div>
</template>

<script>
import page from "@/components/pagination";
import Tinymce from '@/components/Tinymce'
export default {
  data() {
    const validateContent = (rule, value, callback) => {
			let content = this.formData.content.replace(/<(?!img).*?>/ig,"").trim()
			if (content === '') {
				callback(new Error('请输入协议内容'))
			} else {
				callback()
			}
		}
    return {
      aId: null,
      loading: false,
      tableData: [],
      formData: {
        agreementArea: 0,
        name: '',
        type: 2,
        brandId: '',
        status: 1,
        content: ''
      },
      rules: {
        name: [{
          required: true,
          message: '请填写协议标题',
          trigger: 'blur'
        }],
        brandId: [{
          required: true,
          message: '请选择所属品牌',
          trigger: 'blur'
        }],
        content: [{
          required: true,
          message: '请填写协议内容',
          trigger: 'blur'
        },
        {validator: validateContent}]
      },
      pageInfo: {
        page:1,
        size: 10
      },
      total: 0,
      brandName: '',
      agreContent: '',
      brandIds: [],
      brandList: [], // 品牌列表
      brandListInit: [], // 品牌列表初始数据
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      dialogTitle: '', // 弹框标题
      dialogVisible: false,
      dialogVisible2: false, // 协议
      focus: false,
      brandShow: false
    }
  },
  created() {
    this.initData()
  },
  activated() {
    this.initData()
  },
  methods: {
    initData() {
      // 协议列表
      this.$http.agreementSettingList(this, this.pageInfo).then(res => {
        if (res.success) {
          // 品牌列表
          this.$http.getBrandPoList(this, {page:1, size:1000}).then(res2 => {
            if (res2.success) {
              let tabArr = res.data.list
              this.brandList = res2.data.list
              this.brandListInit = res2.data.list
              tabArr.forEach(item => {
                let brandArr = item.brandId.split(',')
                let agrr = []
                res2.data.list.forEach(brand => {
                  brandArr.forEach(val => {
                    if (Number(val) === brand.id) {
                      agrr.push(brand.name)
                    }
                  })
                })
                item.brandName = agrr.join(',')
              })

              this.tableData = tabArr
            }
          })
        }
      })
    },
    // 保存
    handleSubmit () {
      this.$refs['formData'].validate(valid => {
        if (valid) {
          if (this.formData.type === 1 && this.formData.brandId === '') {
            this.$message.error('请选择所属品牌')
            return
          }
          this.$set(this.formData, 'content', this.formData.content)
          // 用户协议只能添加一个（国内、国外和一个  或者  国内外）
          let areaArr = [], eId = 0, areaId = null
          if (this.formData.type === 2) { // 用户协议
            this.tableData.forEach(item => {
              if (item.type === 2) {
                eId = item.id
                areaArr.push(item.agreementArea)
              }
               if (item.id === this.aId) {
                areaId = item.agreementArea
              }
            })

            // 判断是否有修改发布区域
            if ((this.aId && areaId !== this.formData.agreementArea) || !this.aId) {
              if (this.formData.agreementArea === 0 && (areaArr.join(',').indexOf('0')>-1 || areaArr.join(',').indexOf('2')>-1)) {
                this.$message.error('用户协议已存在！')
                return
              } else if (this.formData.agreementArea === 1 && (areaArr.join(',').indexOf('2')>-1 || areaArr.join(',').indexOf('1')>-1)) {
                this.$message.error('用户协议已存在！')
                return
              } else if (this.formData.agreementArea === 2 && areaArr.length > 0) {
                this.$message.error('用户协议已存在！')
                return
              }
            }
          }
          this.loading = true
          if (this.aId) {
            // 修改
            this.formData.id = this.aId
            if (this.formData.type === 2) {
              this.formData.brandName = ''
              this.formData.brandId = ''
            }
            this.$http.editAgreementSetting(this, this.formData).then(res => {
              if (res.success) {
                this.$message({
                  message: '修改成功',
                  type: 'success',
                  duration: 3 * 1000,
                })
                this.dialogVisible = false
                this.initData()
              }
              res.success ? this.loading = false : this.loading = false
            })
          } else {
            // 添加
            this.formData.id = undefined
            this.$http.addAgreementSetting(this, this.formData).then(res => {
              if (res.success) {
                this.$message({
                  message: '添加成功',
                  type: 'success',
                  duration: 3 * 1000,
                })
                this.dialogVisible = false
                this.initData()
              }
              res.success ? this.loading = false : this.loading = false
            })
          }
        }
      })
    },
    // 添加
    handleAdd () {
      this.aId = undefined
      this.formData.name = ''
      this.formData.agreementArea = 0
      this.formData.type = 2
      this.formData.brandId = ''
      this.formData.status = 1
      this.formData.content = ''
      this.brandName = ''
      this.dialogTitle = '新增协议'
      this.dialogVisible = true
    },
    // 查看
    handleSelect(row) {
       this.$http.agreementSettingDetail(this, {id: row.id}).then(res => {
         if (res.success) {
           this.agreContent = res.data.content
           this.dialogVisible2 = true
         }
       })
    },
    // 编辑
    handleEdit(row) {
      this.loading = true
      this.aId = row.id
      this.dialogTitle = '编辑协议'
      this.$http.agreementSettingDetail(this, {id: row.id}).then(res => {
        if (res.success) {
          this.formData = res.data
          this.formData.content = res.data.content
          if (res.data.brandId !== '') {
            let arr = []
            this.brandIds = res.data.brandId.split(',')
            this.brandList.forEach(item => {
              this.brandIds.forEach(val => {
                if (item.id === Number(val)) {
                  arr.push(item.name)
                }
              })
            })
             this.brandName = arr.join(',')
          }
          this.loading = false
          this.dialogVisible =true
        }
      })
    },
    // 删除
    handleDelete(row) {
      this.$confirm('此操作将删除该协议，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(_ => {
        this.$http.delAgreementSetting(this, {id: row.id}).then(res => {
          if (res.success) {
            this.$message.success({
              message: "删除成功",
              duration: 3 * 1000,
              onClose: () => {
              }
            })
            this.initData()
          }
          this.loading = false
        })
      }).catch(_ => {
        this.$message({
          type: 'info',
          message: '已取消'
        })
      })
    },
    // 触发品牌列表
    brandFocus(){
      // 过滤品牌
      this.$http.checkBrand(this, {agreementArea: this.formData.agreementArea}).then(res => {
        if (res.success) {
          let result = this.brandListInit.filter(item1 => {
            return res.data.every(item2 => {
              return Number(item1.id) !== Number(item2)
            })
          })
          this.brandList = result
          this.focus = true
          this.brandShow = true
        }
      })
    },
    brandChange(data, checked, indeterminate) {
      let ary1 = [];
      let ary2 = [];
      this.$refs.tree.getCheckedNodes().forEach(item => {
        ary1.push(item.id)
        ary2.push(item.name)
      })
      this.brandName = ary2.join(',')
      this.formData.brandId = ary1.join(',')
    },
    handleClose(){
      if(this.brandShow && !this.focus){
        this.brandShow = false
      }
    },
    handleCancel () {
      this.dialogVisible = false
    },
    formatType(val) {
      switch(val.type) {
        case 1:
          return '品牌协议';
          break;
        case 2:
          return '用户协议';
          break;
      }
    },
    formatStatus(val) {
      switch(val.status) {
        case 1:
          return '启用';
          break;
        case 0:
          return '停用';
          break;
      }
    },
    formatArea(val) { // 发布区域
      switch(val.agreementArea) {
        case 0:
          return '国内';
          break;
        case 1:
          return '国外';
          break;
        case 2:
          return '国内和国外';
          break;  
      }
    },
    sizeChange(size) {
			this.pageInfo.page = 1
			this.pageInfo.size = size
			this.initData()
		},
    currentChange(page) {
			this.pageInfo.page = page
			this.initData()
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
  watch: {
    'formData.type': {
      handler(val) {
        if (val === 2) {
          this.brandIds = []
          this.brandName = ''
        }
      },
      deep:true
    }
  },
  components: {
    page,
    Tinymce
  },
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .agreement-wrap{
    .item-select{
      color:#21b8cb;
      cursor: pointer;
    }
    .button{
      text-align: center;
    }
    .el-dialog{
      width:70%;
      .el-input{
        width:100%;
        input{
          height: 40px;
          line-height: 40px;
        }
      }
      .item-content{
        /deep/.el-form-item__content{
          margin-left:0 !important;
          margin-top:10px;
          display: inline-block;
          width:80% !important;
        }
        /deep/.mce-edit-area{
          max-height: 300px;
        }
      }
      .category-input{
        width:100%;
      }
    }
    .category-box {
      border: 1px solid #ccc;
      width: 360px;
      max-height: 300px;
      overflow: scroll;
      padding: 10px;
      background-color: white;
      position: absolute;
      left: 0;
      top: 45px;
      z-index: 99;
      border-radius: 10px;
    }
    .conInfo{
      max-height: 400px;
      overflow-y: scroll;
    }
    .tinymce-textarea{
      width:100%;
      height: 500px !important;
      background-color: transparent;
      border: none;
      &::-webkit-scrollbar{
        display: none
  　　}
    }
  }
  .dialog-content-agreen{
    .tinymce-container{
      .mce-top-part{
        display: none;
      }
      .mce-tinymce{
        border: none;
        border-width:0px;
        box-shadow: none;
      }
      .mce-edit-area{
        border-width:0px !important;
      }
    }
    .editor-custom-btn-container{
      display: none;
    }
    .mce-statusbar{
      display: none;
    }
    .mask{
      position: absolute;
      width: calc(100% - 100px);
      top:0;
      bottom:0;
      overflow: scroll;
      z-index: 1;
      filter: alpha(opacity=0);
      opacity: 0;
      background: #ffffff;
    }
  }
  .tinymce-container{
     .editor-custom-btn-container{
      display: none;
    }
  }
</style>