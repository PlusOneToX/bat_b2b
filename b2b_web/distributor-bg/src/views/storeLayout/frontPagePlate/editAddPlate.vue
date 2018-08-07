<template>
  <div class="section-add-edit" @click="cancelB(false)">
      <header>
        <h4>首页板块</h4>
        <el-button class="mini-add-btn btn-home" icon="el-icon-d-arrow-left" @click="cancel">
          返回首页板块
        </el-button>
      </header>
      <div v-loading="loading">
        <el-form ref="formData" :model="formData" label-width="140px" class="el-form1" :rules="rules">
          <el-form-item label="版本：" prop="sectionArea">
            <el-radio v-model="formData.sectionArea" :label="0">国内</el-radio>
            <el-radio v-model="formData.sectionArea" :label="1">海外</el-radio>
            <el-radio v-model="formData.sectionArea" :label="2">国内和海外</el-radio>
          </el-form-item>
          <el-form-item label="板块标题：" prop="title">
              <el-input v-model.trim="formData.title"></el-input>
          </el-form-item>
          <el-form-item label="板块英文标题：" prop="titleEn">
            <el-input v-model="formData.titleEn"></el-input>
          </el-form-item>
          <el-form-item label="序号：" prop="sort">
            <el-input v-model="formData.sort" type="number" min="0" style="width:80px" @keyup.native="proving"></el-input>
            <span class="place-holder">注意：序号越小，排在前面</span>
          </el-form-item>
          <el-form-item label="是否发布：">
            <el-radio-group v-model="formData.releaseStatus">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="指定商品：">
            <el-button class="mini-search-btn"  @click="goodsShow=true">添加商品</el-button>
            <el-button class="mini-search-btn" @click.stop="cancelB(true)">导入分类</el-button>
            <transition name="el-zoom-in-top">
              <div class="category-box" v-if="categoryShow" @click.stop="cancelB(false)">
                <el-tree :data='categorylist' ref="tree" show-checkbox node-key="id" :default-checked-keys="categoryIds" @check-change="handleNodeClick"></el-tree>
                <el-col :span="4" :offset="10">
                  <el-button class="mini-search-btn" id='cate-click' @click="getCheckedNodes" @click.stop="cancelB(false)">确定</el-button>
                </el-col>
              </div>
            </transition>
          </el-form-item>
          <el-form-item>
            <el-row style="width:700px">
              <el-table :data="goods" border header-row-class-name="header-row" class="goods-table" height="300">
                <el-table-column  label="商品编号" prop="goodsNo" align="center"> </el-table-column>
                <el-table-column  label="商品名称" prop="goodsName" align="center"> </el-table-column>
                <!-- <el-table-column label="商品分类" prop="categoryName" align="center"> </el-table-column> -->
                <el-table-column align="center" label="操作" width="220">
                  <template slot-scope="scope">
                    <el-button class="mini-tableadd-btn" @click="handleUp(scope.$index,scope.row)">上移</el-button>
                    <el-button class="mini-freeze-btn" @click="handleDown(scope.$index,scope.row)">下移</el-button>
                    <el-button class="mini-delete-btn" @click="handleDelete(scope.$index,scope.row)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-row>
            <!-- <span class="place-holder place-holder-danger">*商品aa名称、商品分类默认显示为中文名称，海外版新增的栏目则前端页面会自动显示为英文商品名称及商品分类名称。</span> -->
          </el-form-item>
        </el-form>
        <span class="place-holder place-holder-danger">*商品名称、商品分类默认显示为中文名称，海外版新增的栏目则前端页面会自动显示为英文商品名称及商品分类名称。</span>

        <!-----板块样式(国内)----->
        <div class="select-box">
          <div class="title">板块样式（国内）：</div>
          <div class="radio-list">
            <el-radio v-model="radio" label="1">样式一</el-radio>
            <el-radio v-model="radio" label="2">样式二</el-radio>
            <el-radio v-model="radio" label="3">样式三</el-radio>
            <el-radio v-model="radio" label="4">样式四</el-radio>
          </div>
          <div class="form-plate-list" v-show="radio==1">
            <el-form :model="selectData" class="el-form-plate radio">
              <el-form-item>
                <div class="upload-big-image">
                  <div class = "img-content-plate">
                    <div class = "img-uploader-big">
                      <upload-img class="avatar-uploader-plate"
                                  :isEdit ='isEdit'
                                  :area = "'zh'"
                                  :index="1"
                                  :curUrl = "selectData.imageUrl1"
                                  @getImgUrl = "getImgUrl"></upload-img>
                        <div class="img-hint-plate" style="color: #bbb;">
                          <div class="text">(图片建议尺寸：594*320)</div>
                          <el-button class="mini-delete-btn delete-img" @click.prevent="clear('zh', 1)" >清除</el-button>
                        </div>
                    </div>
                    <div class="img-url-plate">
                      <span class="input-hint-plate">链接跳转</span>
                      <input class = "input-url-plate" v-model='selectData.extensionUrl1' placeholder="http://"/>
                    </div>
                  </div>
                </div>
              </el-form-item>
            </el-form>
            <el-form :model="selectData" class="el-form-plate radio">
              <el-form-item>
                <div class="upload-big-image">
                  <div class = "img-content-plate">
                    <div class = "img-uploader-big">
                      <upload-img class="avatar-uploader-plate"
                                  :isEdit = 'isEdit'
                                  :area = "'zh'"
                                  :index = '2'
                                  :curUrl = "selectData.imageUrl2"
                                  @getImgUrl = "getImgUrl"></upload-img>
                      <div class="img-hint-plate" style="color: #bbb;">
                        <div class="text">(图片建议尺寸：594*320)</div>
                        <el-button class="mini-delete-btn delete-img" @click.prevent="clear('zh', 2)" >清除</el-button>
                      </div>
                    </div>
                    <div class="img-url-plate">
                      <span class="input-hint-plate">链接跳转</span>
                      <input class = "input-url-plate" v-model='selectData.extensionUrl2' placeholder="http://"/>
                    </div>
                  </div>

                </div>
              </el-form-item>
            </el-form>
          </div>
          <div class="form-plate-list" v-show="radio==2" >
            <el-form :model="selectData" class="el-form-plate radio">
              <el-form-item>
                <div class="upload-big-image">
                  <div class = "img-content-plate">
                    <div class = "img-uploader-big">
                      <upload-img class="avatar-uploader-plate"
                                  :isEdit ='isEdit'
                                  :area = "'zh'"
                                  :index = '3'
                                  :curUrl = "selectData.imageUrl3"
                                  @getImgUrl = "getImgUrl"></upload-img>
                      <div class="img-hint-plate" style="color: #bbb;">
                        <div class="text">(图片建议尺寸：230*652)</div>
                        <el-button class="mini-delete-btn delete-img" @click.prevent="clear('zh', 3)" >清除</el-button>
                      </div>
                    </div>

                    <div class="img-url-plate">
                      <span class="input-hint-plate">链接跳转</span>
                      <input class = "input-url-plate" v-model='selectData.extensionUrl3' placeholder="http://"/>
                    </div>
                  </div>
                </div>
              </el-form-item>
            </el-form>
            <el-form :model="selectData" class="el-form-plate radio">
              <el-form-item>
                <div class="upload-big-image">
                  <div class = "img-content-plate">
                    <div class = "img-uploader-big">
                      <upload-img class="avatar-uploader-plate"
                                  :isEdit = 'isEdit'
                                  :area = "'zh'"
                                  :index = '4'
                                  :curUrl = "selectData.imageUrl4"
                                  @getImgUrl = "getImgUrl"></upload-img>
                      <div class="img-hint-plate" style="color: #bbb;">
                        <div class="text">(图片建议尺寸：472*320)</div>
                        <el-button class="mini-delete-btn delete-img" @click.prevent="clear('zh', 4)" >清除</el-button>
                      </div>
                    </div>
                    <div class="img-url-plate">
                      <span class="input-hint-plate">链接跳转</span>
                      <input class = "input-url-plate" v-model='selectData.extensionUrl4' placeholder="http://"/>
                    </div>
                  </div>

                </div>
              </el-form-item>
            </el-form>
          </div>
          <div class="form-plate-list" v-show="radio==3" >
            <el-form :model="selectData" class="el-form-plate radio">
              <el-form-item>
                <div class="upload-big-image">
                  <div class = "img-content-plate">
                    <div class = "img-uploader-big">
                      <upload-img class="avatar-uploader-plate"
                                  :isEdit ='isEdit'
                                  :area = "'zh'"
                                  :index = '5'
                                  :curUrl = "selectData.imageUrl5"
                                  @getImgUrl = "getImgUrl"></upload-img>
                      <div class="img-hint-plate" style="color: #bbb;">
                        <div class="text">(图片建议尺寸：230*652)</div>
                        <el-button class="mini-delete-btn delete-img" @click.prevent="clear('zh', 5)" >清除</el-button>
                      </div>
                    </div>

                    <div class="img-url-plate">
                      <span class="input-hint-plate">链接跳转</span>
                      <input class = "input-url-plate" v-model='selectData.extensionUrl5' placeholder="http://"/>
                    </div>
                  </div>
                </div>
              </el-form-item>
            </el-form>
          </div>
          <div class="form-plate-list" v-show="radio==4" >
            <el-form  :model="selectData" class="el-form-plate radio">
              <el-form-item>
                <div class="upload-big-image">
                  <div class = "img-content-plate">
                    <div class = "img-uploader-big">
                      <upload-img class="avatar-uploader-plate"
                                  :isEdit ='isEdit'
                                  :area = "'zh'"
                                  :index = '6'
                                  :curUrl = "selectData.imageUrl6"
                                  @getImgUrl = "getImgUrl"></upload-img>
                      <div class="img-hint-plate" style="color: #bbb;">
                        <div class="text">(图片建议尺寸：230*320)</div>
                        <el-button class="mini-delete-btn delete-img" @click.prevent="clear('zh', 6)" >清除</el-button>
                      </div>
                    </div>
                    <div class="img-url-plate">
                      <span class="input-hint-plate">链接跳转</span>
                      <input class = "input-url-plate" v-model='selectData.extensionUrl6' placeholder="http://"/>
                    </div>
                  </div>
                </div>
              </el-form-item>
            </el-form>
          </div>
        </div>

        <!-----板块样式(国外)----->
        <div class="select-box">
          <div class="title">板块样式（国外）：</div>
          <div class="radio-list">
            <el-radio v-model="radioEn" label="1">样式一</el-radio>
            <el-radio v-model="radioEn" label="2">样式二</el-radio>
            <el-radio v-model="radioEn" label="3">样式三</el-radio>
            <el-radio v-model="radioEn" label="4">样式四</el-radio>
          </div>
          <div class="form-plate-list" v-show="radioEn==1">
            <el-form :model="selectData" class="el-form-plate radio">
              <el-form-item>
                <div class="upload-big-image">
                  <div class = "img-content-plate">
                    <div class = "img-uploader-big">
                      <upload-img class="avatar-uploader-plate"
                                  :isEdit ='isEdit'
                                  :area = "'en'"
                                  :index="1"
                                  :curUrl = "selectData.imageUrl1En"
                                  @getImgUrl = "getImgUrl"></upload-img>
                      <div class="img-hint-plate" style="color: #bbb;">
                        <div class="text">(图片建议尺寸：594*320)</div>
                        <el-button class="mini-delete-btn delete-img" @click.prevent="clear('en', 1)" >清除</el-button>
                      </div>
                    </div>
                    <div class="img-url-plate">
                      <span class="input-hint-plate">链接跳转</span>
                      <input class = "input-url-plate" v-model='selectData.extensionUrl1En' placeholder="http://"/>
                    </div>
                  </div>
                </div>
              </el-form-item>
            </el-form>
            <el-form :model="selectData" class="el-form-plate radio">
              <el-form-item>
                <div class="upload-big-image">
                  <div class = "img-content-plate">
                    <div class = "img-uploader-big">
                      <upload-img class="avatar-uploader-plate"
                                  :isEdit = 'isEdit'
                                  :area = "'en'"
                                  :index = '2'
                                  :curUrl = "selectData.imageUrl2En"
                                  @getImgUrl = "getImgUrl"></upload-img>
                      <div class="img-hint-plate" style="color: #bbb;">
                        <div class="text">(图片建议尺寸：594*320)</div>
                        <el-button class="mini-delete-btn delete-img" @click.prevent="clear('en', 2)" >清除</el-button>
                      </div>
                    </div>
                    <div class="img-url-plate">
                      <span class="input-hint-plate">链接跳转</span>
                      <input class = "input-url-plate" v-model='selectData.extensionUrl2En' placeholder="http://"/>
                    </div>
                  </div>

                </div>
              </el-form-item>
            </el-form>
          </div>
          <div class="form-plate-list" v-show="radioEn==2" >
            <el-form :model="selectData" class="el-form-plate radio" >
              <el-form-item>
                <div class="upload-big-image">
                  <div class = "img-content-plate">
                    <div class = "img-uploader-big">
                      <upload-img class="avatar-uploader-plate"
                                  :isEdit ='isEdit'
                                  :area = "'en'"
                                  :index = '3'
                                  :curUrl = "selectData.imageUrl3En"
                                  @getImgUrl = "getImgUrl"></upload-img>
                      <div class="img-hint-plate" style="color: #bbb;">
                        <div class="text">(图片建议尺寸：230*652)</div>
                        <el-button class="mini-delete-btn delete-img" @click.prevent="clear('en', 3)" >清除</el-button>
                      </div>
                    </div>

                    <div class="img-url-plate">
                      <span class="input-hint-plate">链接跳转</span>
                      <input class = "input-url-plate" v-model='selectData.extensionUrl3En' placeholder="http://"/>
                    </div>
                  </div>
                </div>
              </el-form-item>
            </el-form>
            <el-form :model="selectData" class="el-form-plate radio">
              <el-form-item>
                <div class="upload-big-image">
                  <div class = "img-content-plate">
                    <div class = "img-uploader-big">
                      <upload-img class="avatar-uploader-plate"
                                  :isEdit = 'isEdit'
                                  :area = "'en'"
                                  :index = '4'
                                  :curUrl = "selectData.imageUrl4En"
                                  @getImgUrl = "getImgUrl"></upload-img>
                      <div class="img-hint-plate" style="color: #bbb;">
                        <div class="text">(图片建议尺寸：472*320)</div>
                        <el-button class="mini-delete-btn delete-img" @click.prevent="clear('en', 4)" >清除</el-button>
                      </div>
                    </div>
                    <div class="img-url-plate">
                      <span class="input-hint-plate">链接跳转</span>
                      <input class = "input-url-plate" v-model='selectData.extensionUrl4En' placeholder="http://"/>
                    </div>
                  </div>

                </div>
              </el-form-item>
            </el-form>
          </div>
          <div class="form-plate-list" v-show="radioEn==3" >
            <el-form :model="selectData" class="el-form-plate radio">
              <el-form-item>
                <div class="upload-big-image">
                  <div class = "img-content-plate">
                    <div class = "img-uploader-big">
                      <upload-img class="avatar-uploader-plate"
                                  :isEdit ='isEdit'
                                  :area = "'en'"
                                  :index = '5'
                                  :curUrl = "selectData.imageUrl5En"
                                  @getImgUrl = "getImgUrl"></upload-img>
                      <div class="img-hint-plate" style="color: #bbb;">
                        <div class="text">(图片建议尺寸：230*652)</div>
                        <el-button class="mini-delete-btn delete-img" @click.prevent="clear('en', 5)" >清除</el-button>
                      </div>
                    </div>

                    <div class="img-url-plate">
                      <span class="input-hint-plate">链接跳转</span>
                      <input class = "input-url-plate" v-model='selectData.extensionUrl5En' placeholder="http://"/>
                    </div>
                  </div>
                </div>
              </el-form-item>
            </el-form>
          </div>
          <div class="form-plate-list" v-show="radioEn==4" >
            <el-form :model="selectData" class="el-form-plate radio">
              <el-form-item>
                <div class="upload-big-image">
                  <div class = "img-content-plate">
                    <div class = "img-uploader-big">
                      <upload-img class="avatar-uploader-plate"
                                  :isEdit ='isEdit'
                                  :area = "'en'"
                                  :index = '6'
                                  :curUrl = "selectData.imageUrl6En"
                                  @getImgUrl = "getImgUrl"></upload-img>
                      <div class="img-hint-plate" style="color: #bbb;">
                        <div class="text">(图片建议尺寸：230*320)</div>
                        <el-button class="mini-delete-btn delete-img" @click.prevent="clear('en', 6)" >清除</el-button>
                      </div>
                    </div>
                    <div class="img-url-plate">
                      <span class="input-hint-plate">链接跳转</span>
                      <input class = "input-url-plate" v-model='selectData.extensionUrl6En' placeholder="http://"/>
                    </div>
                  </div>
                </div>
              </el-form-item>
            </el-form>
          </div>
        </div>

        <!-----板块推广----->
        <el-form :model="formData" label-width="140px" class="el-form-plate">
          <el-form-item label="板块推广（国内）：">
            <div class="upload-big-image">
              <div class = "img-content-plate">
                <div class = "img-uploader-big">
                  <upload-img class="avatar-uploader-plate"
                              :isEdit ='isEdit'
                              :area = "'zh'"
                              :curUrl = "formData.imageUrl"
                              @getImgUrl = "getImgUrl"></upload-img>
                </div>
                <span class="img-hint-plate" style="color: #bbb;">(图片建议尺寸：1200*200)
                  <el-button class="mini-delete-btn delete-img" @click.prevent="clear('zh', 0)" >清除</el-button>
                </span>
                <div class="img-url-plate">
                  <span class="input-hint-plate">点击跳转</span>
                  <input class = "input-url-plate" v-model='formData.extensionUrl' placeholder="http://"/>
                </div>
              </div>
            </div>
          </el-form-item>
        </el-form>
        <el-form :model="formData" label-width="140px" class="el-form-plate">
          <el-form-item label="板块推广（海外）：">
            <div class="upload-big-image">
              <div class = "img-content-plate">
                <div class = "img-uploader-big">
                  <upload-img class="avatar-uploader-plate"
                              :isEdit = 'isEdit'
                              :area = "'en'"
                              :curUrl = "formData.imageUrlEn"
                              @getImgUrl = "getImgUrl"></upload-img>
                </div>
                <span class="img-hint-plate" style="color: #bbb;">(图片建议尺寸：1200*200)
                  <el-button class="mini-delete-btn delete-img" @click.prevent="clear('en', 0)" >清除</el-button>
                </span>
                <div class="img-url-plate">
                  <span class="input-hint-plate">点击跳转</span>
                  <input class = "input-url-plate" v-model='formData.extensionUrlEn' placeholder="http://"/>
                </div>
              </div>

            </div>
          </el-form-item>
        </el-form>

        <div class="clearfix footbtn">
          <el-col :span="1" :offset="12">
            <el-button class="mini-search-btn" @click="handleSave">保存</el-button>
          </el-col>
        </div>
        <!-----添加商品----->
        <el-dialog :modal-append-to-body="false" :visible="goodsShow" width="80%" :before-close="closeDialog">
          <select-goods ref="selectGoods" :selectGoodsData="goods" @submit="submit" @cancel="goodsShow=false" :saleStatus = "3"></select-goods>
        </el-dialog>
      </div>
  </div>
</template>

<script>
import selectGoods from "@/views/goods/components/selectGoods"
import { swapItem } from "@/utils/index";
import uploadImg from "@/views/storeLayout/compomemts/uploadImg"
  export default {
    data() {
      return {
        loading: false,
        categoryShow: false,
        goodsShow: false,
        goodsAddIds: [],  // 添加商品
        goodsDelIds: [], // 删除商品
        goodsUpIds: [], // 更新商品
        originGoods: [], // 初始数据
        formData: {
          sectionArea: 2,
          title: '',
          titleEn: '',
          releaseStatus: 1,
          sort: 0,
          imageUrl:'',
          imageUrlEn:'',
          extensionUrl:'',
          extensionUrlEn: '',
          styleUrl: '', // 国内图片1
          styleUrl1: '', // 国内图片2
          styleUrl2: '',  // 国外图片1
          styleUrl3: '', // 国外图片2
          styleExtensionUrl:'', // 国内链接1
          styleExtensionUrl1: '', // 国内链接2
          styleExtensionUrl:'', // 国外链接1
          styleExtensionUrl1: '', // 国外链接2
          styleType: 0, // 样式类型
          styleTypeEn: 0,  // 英文样式类型
          goodsIds:''
        },
        selectData: {
          imageUrl1: '',
          imageUrl1En: '',
          imageUrl2: '',
          imageUrl2En: '',
          imageUrl3: '',
          imageUrl3En: '',
          imageUrl4: '',
          imageUrl4En: '',
          imageUrl5: '',
          imageUrl5En: '',
          imageUrl6: '',
          imageUrl6En: '',
          extensionUrlEn: '',
          extensionUrl1:'',
          extensionUrl1En: '',
          extensionUrl2:'',
          extensionUrl2En: '',
          extensionUrl3:'',
          extensionUrl3En: '',
          extensionUrl4:'',
          extensionUrl4En: '',
          extensionUrl5:'',
          extensionUrl5En: '',
          extensionUrl6:'',
          extensionUrl6En: '',
        },
        rules: {
          sectionArea: [{
            required: true,
            message: '请选择版本',
            trigger: 'blur'
          }],
          title: [{
            required: true,
            message: '请输入板块标题',
            trigger: 'blur'
          }],
          titleEn: [{
            required: true,
            message: '请输入板块英文标题',
            trigger: 'blur'
          }]
        },
        maxFiles: {
            type: Number,
            default: 1,
        },
        percentage: [],
        goods:[],
        categoryIds: [],
        category: [],
        isEdit: true,
        radio: '1',
        radioEn: '1'
      }
    },
    components: {
        selectGoods,
        uploadImg
    },
    created() {
      this.$http.getAllClassifyPoList(this).then(res => { 
          this.categorylist = res;
      })
    },
    mounted() {
      this.getParams();
    },
    activated() {
      this.getParams();
    },
    methods: {
      changeData (type, area, value) {
        if (area === 'zh') {
          if (type === 1) {
            this.selectData.imageUrl1 = value
            value ? '' : this.selectData.extensionUrl1 = value

          } else if (type === 2) {
            this.selectData.imageUrl2 = value
            value ? '' : this.selectData.extensionUrl2 = value
          } else if (type === 3) {
            this.selectData.imageUrl3 = value
            value ? '' : this.selectData.extensionUrl3 = value
          } else if (type === 4) {
            this.selectData.imageUrl4 = value
            value ? '' : this.selectData.extensionUrl4 = value
          } else if (type === 5) {
            this.selectData.imageUrl5 = value
            value ? '' : this.selectData.extensionUrl5 = value
          } else if (type === 6) {
            this.selectData.imageUrl6 = value
            value ?  '' : this.selectData.extensionUrl6 = value
          } else {
            this.formData.imageUrl = value
            value ? '' : this.formData.extensionUrl = value
          }
        } else if (area === 'en'){
          if (type === 1) {
            this.selectData.imageUrl1En = value
            value ? '' : this.selectData.extensionUrl1En = value
          } else if (type === 2) {
            this.selectData.imageUrl2En = value
            value ? '' : this.selectData.extensionUrl2En = value
          } else if (type === 3) {
            this.selectData.imageUrl3En = value
            value ? '' : this.selectData.extensionUrl3En = value
          } else if (type === 4) {
            this.selectData.imageUrl4En = value
            value ? '' : this.selectData.extensionUrl4En = value
          } else if (type === 5) {
            this.selectData.imageUrl5En = value
            value ? '' : this.selectData.extensionUrl5En = value
          } else if (type === 6) {
            this.selectData.imageUrl6En = value
            value ? '' : this.selectData.extensionUrl6En = value
          } else {
            this.formData.imageUrlEn = value
            value ? '' : this.formData.extensionUrlEn = value
          }
        }
      },
      getFormData () {
        // 验证
        if (this.radio == 1) {
          this.formData.styleUrl = this.selectData.imageUrl1
          this.formData.styleUrl1 = this.selectData.imageUrl2
          this.formData.styleExtensionUrl = this.selectData.extensionUrl1
          this.formData.styleExtensionUrl1 = this.selectData.extensionUrl2
        } else if (this.radio == 2) {
          this.formData.styleUrl = this.selectData.imageUrl3
          this.formData.styleUrl1 = this.selectData.imageUrl4
          this.formData.styleExtensionUrl = this.selectData.extensionUrl3
          this.formData.styleExtensionUrl1 = this.selectData.extensionUrl4
        } else if (this.radio == 3) {
          this.formData.styleUrl = this.selectData.imageUrl5
          this.formData.styleExtensionUrl = this.selectData.extensionUrl5
          this.formData.styleUrl1 = ''
          this.formData.styleExtensionUrl1 = ''
        } else if (this.radio == 4) {
          this.formData.styleUrl = this.selectData.imageUrl6
          this.formData.styleExtensionUrl = this.selectData.extensionUrl6
          this.formData.styleUrl1 = ''
          this.formData.styleExtensionUrl1 = ''
        }

        if (this.radioEn == 1) {
          this.formData.styleUrl2 = this.selectData.imageUrl1En
          this.formData.styleUrl3 = this.selectData.imageUrl2En
          this.formData.styleExtensionUrl2 = this.selectData.extensionUrl1En
          this.formData.styleExtensionUrl3 = this.selectData.extensionUrl2En
        } else if (this.radioEn == 2) {
          this.formData.styleUrl2 = this.selectData.imageUrl3En
          this.formData.styleUrl3 = this.selectData.imageUrl4En
          this.formData.styleExtensionUrl2 = this.selectData.extensionUrl3En
          this.formData.styleExtensionUrl3 = this.selectData.extensionUrl4En
        } else if (this.radioEn == 3) {
          this.formData.styleUrl2 = this.selectData.imageUrl5En
          this.formData.styleExtensionUrl2 = this.selectData.extensionUrl5En
          this.formData.styleUrl3 = ''
          this.formData.styleExtensionUrl3 = ''
        } else if (this.radioEn == 4) {
          this.formData.styleUrl2 = this.selectData.imageUrl6En
          this.formData.styleExtensionUrl2 = this.selectData.extensionUrl6En
          this.formData.styleUrl3 = ''
          this.formData.styleExtensionUrl3 = ''
        }
        this.formData.styleType = parseInt(this.radio)
        this.formData.styleTypeEn = parseInt(this.radioEn)
      },
      setFormData () {
        this.radio = this.formData.styleType.toString()
        this.radioEn = this.formData.styleTypeEn.toString()
        if (this.formData.styleType === 1) {
          this.selectData.imageUrl1 = this.formData.styleUrl
          this.selectData.imageUrl2 = this.formData.styleUrl1
          this.selectData.extensionUrl1 = this.formData.styleExtensionUrl
          this.selectData.extensionUrl2 = this.formData.styleExtensionUrl1
        } else if (this.formData.styleType === 2) {
          this.selectData.imageUrl3 = this.formData.styleUrl
          this.selectData.imageUrl4 = this.formData.styleUrl1
          this.selectData.extensionUrl3 = this.formData.styleExtensionUrl
          this.selectData.extensionUrl4 = this.formData.styleExtensionUrl1
        } else if (this.formData.styleType === 3) {
          this.selectData.imageUrl5 = this.formData.styleUrl
          this.selectData.extensionUrl5 = this.formData.styleExtensionUrl
        } else if (this.formData.styleType === 4) {
          this.selectData.imageUrl6 = this.formData.styleUrl
          this.selectData.extensionUrl6 = this.formData.styleExtensionUrl
        }

        if (this.formData.styleTypeEn === 1) {
          this.selectData.imageUrl1En = this.formData.styleUrl2
          this.selectData.imageUrl2En = this.formData.styleUrl3
          this.selectData.extensionUrl1En = this.formData.styleExtensionUrl2
          this.selectData.extensionUrl2En = this.formData.styleExtensionUrl3
        } else if (this.formData.styleTypeEn === 2) {
          this.selectData.imageUrl3En = this.formData.styleUrl2
          this.selectData.imageUrl4En = this.formData.styleUrl3
          this.selectData.extensionUrl3En = this.formData.styleExtensionUrl2
          this.selectData.extensionUrl4En = this.formData.styleExtensionUrl3
        } else if (this.formData.styleTypeEn === 3) {
          this.selectData.imageUrl5En = this.formData.styleUrl2
          this.selectData.extensionUrl5En = this.formData.styleExtensionUrl2
        } else if (this.formData.styleTypeEn === 4) {
          this.selectData.imageUrl6En = this.formData.styleUrl2
          this.selectData.extensionUrl6En = this.formData.styleExtensionUrl2
        }
      },
      /**
       * 清除图片和图片路径
       * 施义煌修改，区分中英文板块
       * @param value
       */
      clear(value, type){
        this.changeData(type, value, '')
      },
      proving(){
        this.formData.sort=this.formData.sort.replace(/[^\.\d]/g,'');
        this.formData.sort=this.formData.sort.replace('.','');
      },
      cancelB(b){
        this.categoryShow = b;
        this.categoryIds = [];
      },
      handleNodeClick(raw) {
        let goodsIds = [];
        this.$http.getCategoryGoods(this, {ids: [raw.id]}).then(res => {  
            if(res.data.length != 0){
                res.data.forEach(item => {
                  goodsIds.push(item.id)
                })
                this.$http.getGoodsByIds(this, {ids: goodsIds}).then(result => { 
                    this.goods = this.goods.concat(result.data);
                    this.setArr(this.goods);
                });
            }
        })
      },
      /**
       * 清除图片和图片路径
       * 施义煌修改，区分中英文板块
       * @param index
       * @param value
       */
      getImgUrl(index, url, type){
        this.changeData(index, type, url)
      },
      // 保存
      handleSave(){
        this.$refs['formData'].validate(valid => {
          if (valid) {
            // 判断获取国内外板块样式
            this.getFormData()
            if(this.goods.length === 0){
                this.$message('至少要有一个指定商品！');
                return
            }
            if(this.formData.id != undefined){  // 修改
              this.goods.map((item, index) => { // 排序
                item.sort = index + 1
              })
              this.goodsAddIds = []
              this.goodsUpIds = []
              // 过滤添加、修改、删除商品
              this.filterGoods(this.goods, this.originGoods)
              
              this.$http.editSection(this, this.formData).then(res => {    
                if(res.success) {
                  this.$message({
                      message: '保存成功',
                      type: 'success',
                      duration: 3 * 1000,
                      onClose: () => { }
                  })
                  this.cancel()
                }
              })
            }else{
              this.formData.goodsIds = []
              this.goods.forEach((item, index) => {
                this.formData.goodsIds.push({
                  goodsId: item.id,
                  sort: index + 1,
                  operationType: 1 // 添加
                })
              })
                this.$http.addSection(this, this.formData).then(res => {  
                    if(res.success) {
                        this.$message({
                            message: '保存成功',
                            type: 'success',
                            duration: 3 * 1000,
                            onClose: () => { }
                        })
                        this.cancel()
                    }
                })
            }
          }
        })
      },
      // 对比数组筛选添加和修改商品
      filterGoods(item1, item2) {
        // item1 更新后的数组  item2 原始数组
        for (let j=0; j< item1.length; j++) {
          let va = item1[j]
          let isExit = false
            for (let i=0; i< item2.length; i++) {
            let item = item2[i]
              if (va.id === item.id) {
                // 判断是否更新
                if (va.sort !== item.sort) {
                  this.goodsUpIds.push({
                    sectionId: this.formData.id,
                    goodsId: va.id,
                    sort: va.sort,
                    operationType: 2 // 更新
                  })
                }
                isExit = true
                break
              }
          }
          if (!isExit) {
            this.goodsAddIds.push({
              sectionId: this.formData.id,
              goodsId:va.id,
              sort: va.sort,
              operationType: 1 // 添加
            })
          }
        }
        // 删除的
        let list = item2.filter(item => !item1.some(ele=>ele.id===item.id));
        list.forEach(val => {
          this.goodsDelIds.push({
            sectionId: this.formData.id,
            goodsId: val.id,
            sort: val.sort,
            operationType: 3 // 删除
          })
        })
        this.formData.goodsIds = this.goodsAddIds.concat(this.goodsDelIds.concat(this.goodsUpIds))
      },
      getCheckedNodes() {
        let ary1 = [];
        let ary2 = [];
        this.$refs.tree.getCheckedNodes().forEach(item => {
            ary1.push(item.id)
            ary2.push(item.label)
        })
        this.categoryName = ary2.join(',')
        this.categoryIds = ary1
        let goodsIds = [];
        if(this.categoryIds.length != 0){
          this.$http.getCategoryGoods(this, {ids: this.categoryIds}).then(res => {    
            if(res.data.length != 0){
              // goodsIds = res.goodsIds.join(",");
              res.data.forEach(item => {
                goodsIds.push(item.id)
              })
              // ids获取商品
              this.$http.getGoodsByIds(this, {ids: goodsIds}).then(result => {  
                this.goods = this.goods.concat(result.data);
                this.setArr(this.goods);
              })
            }
          })
        }
      },
      cancel(){
        this.$router.push({name: 'frontPagePlate'})
      },
      closeDialog() {
          this.$refs.selectGoods.handleCancel()
            // this.goodsShow = false;
      },
      submit(val) {
        this.goods = this.goods.concat(val);
        this.setArr(this.goods);
        this.goodsShow = false;
      },
      setArr(arr){
          let obj ={};
          let temp=[];
          for( let i = 0; i < arr.length; i++ ) {
          let type= Object.prototype.toString.call(arr[i].id);//不加类型 分不清 1 '1'
          if( !obj[ arr[i].id +type] ) {
            temp.push( arr[i] );
            obj[ arr[i].id+ type ] =true;//这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
          }
          this.goods = temp;
        }
      },
      // 上移
      handleUp(index, row) {
        if(this.goods.length > 1 && index !== 0) {
            this.goods = swapItem(this.goods, index, index - 1)　　
        }
      },
      // 下移
      handleDown(index, row) {
        if(this.goods.length > 1 && index !== (this.goods.length - 1)) {
            this.goods = swapItem(this.goods, index, index + 1)　　
        }
      },
      // 移除商品列表
      handleDelete(index,row) {
        this.goods.splice(index, 1)
      },
      getParams(){
        this.goods = []
        this.categoryIds = []
        if(this.$route.params.id != undefined){
          this.loading = true;
            this.$http.sectionDetail(this, {id: this.$route.params.id}).then(res => {    
                this.formData = res.data;
                res.success ? this.loading = false : this.looking = false
                this.$http.sectionListByIds(this, {page:1,size:10000,sectionId:this.$route.params.id}).then(res => {
                  if (res.success) {
                    this.goods = this.goods.concat(res.data.list);
                    this.setArr(this.goods);
                    res.data.list.forEach((item, index) => {
                      this.originGoods.push({
                        id: item.id,
                        sort: index + 1
                      })
                    })
                  } 
                })
                // 获取板块样式
                this.setFormData()
            })
        }else{
          this.formData.id = undefined
          this.formData.title='',
          this.formData.releaseStatus=1,
          // this.formData.status="是",
          this.formData.sort= 0,
          this.formData.imageUrl='',
          this.formData.extensionUrl='',
          this.formData.styleUrl='',
          this.formData.styleUrl1='',
          this.formData.styleUrl2='',
          this.formData.styleUrl3='',
          this.formData.styleExtensionUrl='',
          this.formData.styleExtensionUrl1='',
          this.formData.styleExtensionUrl2='',
          this.formData.styleExtensionUrl3='',
          this.formData.styleType = 1,
          this.formData.styleTypeEn = 1,
          this.formData.goodsIds=[]
        }
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
      }
    },
    watch:{
        categorys(val){
            this.formData.categoryIds=val.map(v=>{return v.categoryId})
            this.categoryShow=true;
            setTimeout(function(){
            document.getElementById('cate-click').click();
            },500)
        },
      radio(val) {
          this.radio = val
      }
    }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-table .cell{
  overflow: hidden;
  white-space:nowrap;
}
.section-add-edit {
  background-color: white;
  min-height: 100%;
  padding-bottom: 30px;
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
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
//   .header {
//     background-color: $lakeBlue;
//     line-height: 40px;
//     color: white;
//     padding-left: 20px;
//   }
}
.el-form1{
    width: 700px;
    margin-top: 30px;
    padding-left: 20px;
}
.el-form-plate{
    margin-right: 60px;
    margin-top: 30px;
    padding-left: 20px;
}

.file{
    position: absolute;
    width: 290px;
    height: 40px;
    top: 0;
    opacity: 0;
    padding: 0px;
    filter: alpha(opacity=0);
    cursor: pointer
    // font-size: 200px;
}
.file-name{
    top: 0;
    position: relative;
    display: inline-block;
    overflow: hidden;
}

.el-icon-document::before {
    content: url("/src/assets/images/attachment.png");
}
.footbtn {
    padding-top: 30px;
    padding-bottom: 30px;
}
.place-holder {
    margin-left: 10px;
    color: #ccc;
    font-size: 14px;
}
.category-box {
        border: 1px solid #ccc;
        width: 300px;
        padding: 10px;
        background-color: white;
        position: absolute;
        left: 0;
        top: 45px;
        z-index: 99;
        border-radius: 10px;
}
.img-hint-plate{
    margin-left: 15px;
    font-size: 12px;
}
.img-content-plate{
      margin-top: 15px;
      .img-uploader-big{
         display: inline;
        .avatar-uploader-plate {
          margin-left: 15px;
          margin-right: 15px;
          background-color:#bfbfbf;
          border-radius: 5px;
          height: 90px;
          cursor: pointer;
          overflow: hidden;
          text-align: center;
          display: flex;
          justify-content: center;
          align-items: center;
        }
      }
      .img-url-plate{
        display: flex;
        height: 30px;
        width: 100%;
        margin-left: 15px;
        margin-bottom: 15px;
        .input-hint-plate{
          display: flex;
          justify-content: center;
          align-items: center;
          font-size: 12px;
          font-weight: bold;
        }
        .input-url-plate{
          height: 30px;
          font-size: 14px;
          margin-left: 15px;
          border-radius: 5px;
          border: 1px solid #ccc;
          font-weight: bold;
          width: 90%;
        }

      }
    }
.upload-big-image{
   border-radius: 5px;
   border: 1px solid rgb(187, 187, 187);
   /*background-color: #f8f8f8;*/
   &.radio{
     display: inline-block;
     width: 600px;
   }
 }
.delete-img{
    float: right;
    margin-top: 5px;
    margin-right: 15px;
}
.place-holder-danger{
  padding-left: 150px;
  color: #F56C6C;
}
  .select-box{
    margin: 80px 60px 80px 0;
    padding-left: 20px;
    .title{
      display: inline-block;
      font-size: 14px;
      color: #606266;
      line-height: 40px;
      padding: 0 12px 0 0;
      -webkit-box-sizing: border-box;
      box-sizing: border-box;
    }
    .radio-list{
      display: inline-block;
    }
    .form-plate-list{
      margin-left:120px;
      .el-form-plate{
        display: inline-block;
        margin-right:0;
        margin-top:10px;
        &.radio{
          .upload-big-image{
            display: inline-block;
            width: 600px;
            .img-uploader-big{
              display: flex;
              display: -webkit-flex;
              flex-direction: row;
              align-items: flex-end;
              margin-bottom:20px;
              height: 300px;
              line-height: 300px;
              .index_big{
                display: inline-block;
                height: 300px;
                flex:1;
                /deep/.oss_file_big{
                  display: inline-block;
                  width:416px;
                  height: 300px;
                  span{
                    display: inline-block;
                    height: 100%;
                    width: 100%;
                    img{
                      display: inline-block;
                      width:auto;
                      max-height:100%;
                    }
                  }

                }
              }
              .img-hint-plate{
                display: inline-block;
                line-height: 30px;
                margin: 0 15px 0 0;
              }
            }
            .img-url-plate{
              width:calc(100% - 30px);
              .input-hint-plate{
                display: inline-block;
                line-height: 30px;
                width: 60px;
              }
            }
          }
        }
      }
    }
  }
</style>

