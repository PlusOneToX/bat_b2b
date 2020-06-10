<template>
  <div class="wrap" ref="pageMainColor">
    <div class="customize">
      <!--头部-->
      <div class="header">
        <div class="title">
          <span>{{ materialsName }}</span>
          <span class="price" v-show="price">¥{{ price }}</span>
        </div>
        <div class="next" @click="make()">
          下一步
          <i class="el-icon-d-arrow-right"></i>
        </div>
      </div>
      <div class="container">
        <!------选择列表----->
        <div class="left-content">
          <el-tabs :tab-position="tabPosition" v-model="activeTab">
            <!-----手机型号------>
            <el-tab-pane label="手机型号" name="tabbrand">
              <span slot="label"
                ><i class="icon el-icon-mobile-phone"></i>手机型号</span
              >
              <div class="item-box">
                <el-tabs
                  :tab-position="tabPosition"
                  v-model="brandName"
                  @tab-click="handleClick"
                >
                  <div class="search-box">
                    <i class="icon el-icon-search"></i>
                    <input
                      type="text"
                      ref="query"
                      v-model="query"
                      class="box"
                      placeholder="请输入手机型号"
                    />
                    <i
                      class="icon el-icon-close"
                      v-show="query"
                      @click="clear"
                    ></i>
                  </div>
                  <!----手机型号列表------>
                  <el-tab-pane
                    v-for="brand in modelData"
                    :label="brand.name"
                    :mid="brand.modelId"
                    :key="'brand' + brand.modelId"
                    :name="brand.name"
                  >
                    <div v-if="modelData.length > 0" class="type-list">
                      <ul class="item-ul">
                        <li
                          v-for="(model, idx) in brand.childrenList"
                          :key="model.modelId"
                          class="item-li"
                          :class="{
                            active:
                              brand.modelId == brandId &&
                              model.modelId == modelId,
                            disable: model.underStockFlag,
                          }"
                          @click="changeModel(model, idx)"
                        >
                          {{
                            model.underStockFlag
                              ? model.name + "(缺货)"
                              : model.name
                          }}
                        </li>
                      </ul>
                    </div>
                  </el-tab-pane>
                </el-tabs>
              </div>
            </el-tab-pane>
            <!-----图库选择------>
            <el-tab-pane label="图库选择" name="tabpic">
              <span slot="label"
                ><i class="el-icon-picture-outline"></i>图库选择</span
              >
              <div class="item-box">
                <el-tabs :tab-position="tabPosition" class="pic">
                  <el-tab-pane
                    v-for="item in picData"
                    :label="item.name"
                    :key="'data' + item.id"
                    class="tab-box"
                  >
                    <el-tabs
                      :tab-position="tabPosition"
                      v-if="item.childrenList.length > 0"
                    >
                      <el-tab-pane
                        v-for="child in item.childrenList"
                        :key="child.id"
                        :label="child.name"
                      >
                        <div
                          v-if="
                            child.pictureList && child.pictureList.length > 0
                          "
                          class="pic-list"
                        >
                          <div
                            class="pic-li"
                            v-for="pic in child.pictureList"
                            :key="'child' + pic.id"
                            @click="change(pic)"
                          >
                            <div class="pic-item">
                              <img
                                class="pic-item"
                                :src="
                                  pic.originImage +
                                  '?x-oss-process=image/resize,h_400,l_400'
                                "
                                alt=""
                              />
                              <span class="tag" v-if="pic.type === 2"
                                >版权</span
                              >
                            </div>
                            <div v-if="pic.type === 2" class="pic-code">
                              编号:{{ pic.id }}
                            </div>
                          </div>
                        </div>
                      </el-tab-pane>
                    </el-tabs>
                    <div v-else class="pic-list">
                      <template
                        v-if="item.pictureList && item.pictureList.length > 0"
                      >
                        <div
                          class="pic-li"
                          v-for="pic in item.pictureList"
                          :key="'pic' + pic.id"
                          @click="change(pic)"
                        >
                          <div class="pic-item">
                            <img
                              class="pic-item"
                              :src="
                                pic.originImage +
                                '?x-oss-process=image/resize,h_400,l_400'
                              "
                              alt=""
                            />
                            <span class="tag" v-if="pic.type === 2">版权</span>
                          </div>
                          <div v-if="pic.type === 2" class="pic-code">
                            编号:{{ pic.id }}
                          </div>
                        </div>
                      </template>
                    </div>
                  </el-tab-pane>
                </el-tabs>
              </div>
            </el-tab-pane>
            <!-----添加图片------>
            <el-tab-pane
              label="添加图片"
              name="tabupload"
              :disabled="Number(uploadValid) !== 1"
            >
              <span slot="label" @click="handleUploadValid"
                ><i class="icon el-icon-upload"></i>添加图片</span
              >
              <div class="item-box upload">
                <div class="upload-box">
                  <el-upload
                    id="upload"
                    class="upload-demo"
                    drag
                    action="#"
                    :before-upload="handleBeforeUpload"
                    :on-preview="handlePreview"
                    :http-request="handleSuccess"
                    :on-remove="handleRemove"
                    :file-list="fileList"
                    list-type="picture"
                    accept="image/bmp,image/jpeg,image/jpg,image/png"
                  >
                    <el-button>
                      <i class="icon el-icon-s-platform"></i>本地上传
                    </el-button>
                    <div class="el-upload__text el-upload__tip">
                      支持上传10MB以下的JPEG/PNG
                    </div>
                    <div class="el-upload__text el-upload__tip note">
                      提示：小于100dpi，非常模糊，不建议印刷。
                    </div>
                    <div class="el-upload__tip title" slot="tip">
                      已上传图片
                    </div>
                  </el-upload>
                </div>
              </div>
            </el-tab-pane>
            <!-----编辑文字------>
            <el-tab-pane label="编辑文字" name="tabtext">
              <span slot="label"
                ><i class="icon text icon-text"></i>编辑文字</span
              >
              <div class="item-box font">
                <div class="title">
                  <div class="line"></div>
                  <div class="text">字体编辑</div>
                  <div class="line"></div>
                </div>
                <div class="font-box">
                  <el-select v-model="font" placeholder="字体">
                    <template v-for="family in fontFamily">
                      <el-option
                        v-if="family.englishName !== ''"
                        :key="family.id"
                        :label="family.name"
                        :value="family.englishName"
                      >
                      </el-option>
                    </template>
                  </el-select>
                  <el-input
                    type="textarea"
                    :rows="2"
                    placeholder="请输入文字"
                    v-model="textarea"
                  >
                  </el-input>
                </div>
                <div class="color-box">
                  <div class="title">
                    <div class="line"></div>
                    <div class="text">选择颜色</div>
                    <div class="line"></div>
                  </div>
                  <colorPicker v-model="color" />
                </div>
                <div class="btn-box">
                  <el-button @click="addText">确定</el-button>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
        <!-------定制部分--------->
        <div class="right-content">
          <div class="top-box">
            <div class="reset" :class="{ hasDes: dpiDesc }" @click="reset">
              <i class="icon el-icon-refresh"></i>重置
            </div>
            <div v-if="showDpi" class="img-dpi" :class="getIcon(dpiValue)">
              <div class="top">
                <span class="text">当前图片分辨率</span>
                <i class="icon" :class="getIcon(dpiValue)"></i>
                <span class="text">{{ dpiValue }}dpi</span>
              </div>
              <span class="desc">{{ dpiDesc }}</span>
            </div>
          </div>
          <div class="main-content" ref="mainRef">
            <WebCanvas
              ref="mcanvas"
              @submit="submit"
              @clear="clearText"
              @select="selectText"
              @dpi="getdpi"
              :materialsColorValue="materialsColorValue"
              :materialsType="materialsType"
              :manufactor="manufactor"
              :mw="mw"
              :mh="mh"
              :nw="nw"
              :nh="nh"
              :spriteArr="spriteArr"
              :bgimg="bgImg"
              :upimg="frameImg"
              :pwidth="phonenWidth"
              :pheight="phonenHeight"
              :pwidth2="phoneWidth"
              :pheight2="phoneHeight"
              :canvasHeight="canvasHeight"
            ></WebCanvas>
          </div>
        </div>
      </div>
    </div>

    <!-- 确认提交 -->
    <el-dialog
      :close-on-click-modal="false"
      title="请确认商品定制信息"
      :visible.sync="diyDialogVisible"
      @close="diyCancel"
    >
      <div class="diy-css">
        <div class="left-img">
          <div class="diy-images">
            <img
              :src="diyPic + '?x-oss-process=image/resize,h_300,l_300'"
              alt=""
            />
          </div>
        </div>
        <div class="right-cons">
          <div class="item">
            <span class="item-label">型号:</span><span>{{ modelName }}</span>
          </div>
          <div class="item">
            <span class="item-label">材质:</span
            ><span>{{ materialsName }}</span>
          </div>
          <!-- <div class="item">
            <span class="item-label">购买数量:</span>
            <el-input-number
              v-model="diyNum"
              :min="1"
              label="描述文字"
              size="mini"
              @keyup.native="numChange($event)"
              @change="handleChange"
            ></el-input-number>
          </div> -->
        </div>
      </div>
      <p v-show="isMaxNum" class="tips">
        订购数量过多，请先与客服联系，否则有色差问题不予退货。客服微信号：
        W-666357。
      </p>
      <div slot="footer" class="dialog-footer">
        <el-button @click="diyCancel">我再想想</el-button>
        <el-button
          class="submit-btn"
          :loading="btnLoading"
          @click.native.prevent="diySubmit"
          >确认提交</el-button
        >
      </div>
    </el-dialog>

    <!--加载动画-->
    <WebLoading v-show="isLoading" :mainColor="mainColor"></WebLoading>
  </div>
</template>

<script>
// 组件
import WebCanvas from "components/canvasDrag/web-canvas.vue";
import WebLoading from "components/loading/web-loading.vue";

import { debounce } from "common/js/util";

// 取色
import Vue from "vue";
import vcolorpricker from "vcolorpicker";
Vue.use(vcolorpricker);

// api
import api from "api/allApi.js";

export default {
  name: "webCustom",
  data() {
    return {
      isLoading: false, // 展示加载动画
      distributorId: "",
      winWidth: 0, // 窗口宽
      leftWidth: 620, // 左侧宽
      canvasHeight: 0, // 画布高
      tabPosition: "left",
      activeTab: "tabbrand", // 左侧tab
      picData: [], // 图库数据
      picId: 0, // 当前图片ID
      pictureName: "", // 当前图片名称
      picType: 0, // 当前图片类型
      picUrl: "", // 当前图片路径
      materialData: [], // 材质数据
      materialsId: "", // 材质ID
      materialNo: "", // 材质编码
      materialsType: 0, // 材质类型
      materialsName: "", // 材质名
      materialsNameEn: "", // 材质英文名
      materialsColorValue: "", // 手机材质颜色值
      skuNo: "", // 货品编码
      modelData: [], // 品牌型号数据
      brandId: "", // 品牌ID
      brandName: "", // 品牌名
      modelId: "", // 型号ID
      modelName: "", // 型号名
      modelNameEn: "", // 型号英文名
      manufactor: "", // 第三方工厂, 上传类型: LHW-图片  YC-pdf
      query: "", // 搜索手机型号
      modelInfo: {}, // 定制信息
      fileList: [], // 上传图片列表
      fontFamily: [], // 字体数据
      color: "#ff0000",
      font: "", // 字体
      textarea: "", // 添加文字
      bgImg: "", // 手机底图
      frameImg: "", // 手机边框图
      spriteArr: [],
      phonenWidth: 0, // 手机宽度(原始宽)
      phonenHeight: 0, // 手机高度(原始高)
      phoneWidth: 0, // 底图宽(加宽2mm后)
      phoneHeight: 0, // 底图高(加宽2mm后)
      imgData: {
        // 图片数据
        posX: null,
        posY: null,
        posuX: null,
        posuY: null,
        sizeW: 20,
        sizeH: 20,
        initW: 20,
        initH: 20,
        rotate: 0,
      },
      textData: {
        // 文字数据
        posX: null,
        posY: null,
        posuX: null,
        posuY: null,
        sizeW: 20,
        sizeH: 20,
        rotate: 0,
      },
      index: null, // 当前选择图层
      client: null,
      hostname: "",
      files: [],
      showDpi: false, // 是否显示 dpi
      dpiValue: "", // dpi 值
      dpiDesc: "", // dpi 描述
      // action: process.env.BASE_API + "file/upload",
      mw: 0,
      mh: 0,
      nw: 0, // 未加宽的手机尺寸
      nh: 0, // 未加宽的手机尺寸
      pscale: 0,
      ratio: 1,
      isMake: false,
      diyDialogVisible: false,
      diyNum: 1,
      diyPic: "",
      diyPdf: "",
      btnLoading: false, // 提交按钮loading
      uploadValid: 1, // 是否允许用户上传（0 不允许，1 允许）
      isMaxNum: false, // 真彩创意壳（设置最大10个提示语）
      isZFold: false, 
      underStockFlag: false, // 是否缺货
      copyrightCost: 0, // 图片版权费
      price: 0, // 价格
      mainColor: "#02bbcc", // 页面主色调
    };
  },
  components: {
    WebLoading,
    WebCanvas,
  },
  created() {
    // 搜索手机型号
    this.$watch(
      "query",
      debounce((newQuery) => {
        this.getModelList(newQuery, true);
      }, 200)
    );

    // 分销商ID
    this.distributorId = this.$route.query.distributorId;
    // 订单来源
    let orderSource = this.$route.query.orderSource;
    // 平台
    let Platform = this.$route.query.Platform
      ? this.$route.query.Platform
      : orderSource;
    if (this.distributorId && orderSource) {
      localStorage.setItem("distributorId", this.distributorId);
      localStorage.setItem("orderSource", orderSource);
      localStorage.setItem("Platform", Platform);
    } 
    localStorage.setItem("distributorId", this.distributorId);
    localStorage.setItem("orderSource", orderSource);
    localStorage.setItem("Platform", Platform);
    localStorage.setItem("distributor", this.distributor);

    // 记录数据埋点
    let hasRecord = sessionStorage.getItem("hasRecord");
    if (!hasRecord) {
      this.handleRecordData();
    }

    this.initScale();

    // 页面主色调
    // this.mainColor = "#F00";
  },
  mounted() {
    this.winWidth = document.body.clientWidth;
    this.canvasHeight = document.body.scrollHeight - 118;

    this.init();

    // 页面主色调
    this.$refs.pageMainColor.style.setProperty("--mainColor", this.mainColor);
  },
  methods: {
    // 记录埋点数据
    handleRecordData() {
      let userId = localStorage.getItem("userId") || "";
      this.$api
        .post(this, api.recordPoint, {
          source: 1,
          userId: userId,
          distributorId: this.distributorId,
          networkType: 1,
        })
        .then((res) => {
          if (res.success) {
            sessionStorage.setItem("hasRecord", 1);
          }
        });
    },
    detectZoom() {
      // 判断PC浏览器是否缩放，若返回100则为默认无缩放，如果大于100则是放大，否则缩小
      var ratio = 0,
        screen = window.screen,
        ua = navigator.userAgent.toLowerCase();

      if (window.devicePixelRatio !== undefined) {
        ratio = window.devicePixelRatio;
      } else if (~ua.indexOf("msie")) {
        if (screen.deviceXDPI && screen.logicalXDPI) {
          ratio = screen.deviceXDPI / screen.logicalXDPI;
        }
      } else if (
        window.outerWidth !== undefined &&
        window.innerWidth !== undefined
      ) {
        ratio = window.outerWidth / window.innerWidth;
      }

      if (ratio) {
        ratio = Math.round(ratio * 100);
      }
      return ratio;
    },
    isScale() {
      // 判断PC端浏览器缩放比例不是100%时的情况
      var rate = this.detectZoom(),
        isMac = /macintosh|mac os x/i.test(navigator.userAgent); // Mac默认缩放值为200，windows默认为100，需要分开判断
      if ((isMac && rate !== 200) || (!isMac && rate !== 100)) {
        // 提示让用户如果想使用100%的比例手动去触发按ctrl+0
        alert(
          "当前页面不是100%显示，请按键盘ctrl/command + 0恢复100%显示标准，以防页面显示错乱！"
        );
      }
    },
    initScale() {
      // window.onresize 事件可用于检测页面是否触发了放大或缩小。
      window.addEventListener("resize", () => {
        // 页面缩放，重新加载页面，以免编辑器生成图片错乱
        let timer;
        if (timer) {
          clearTimeout(timer);
        }
        timer = setTimeout(() => {
          location.reload();
        }, 200);

        this.isScale();
      });

      // 禁用缩放
      const keyCodeMap = {
        91: true, // command
        61: true,
        107: true, // 数字键盘 +
        109: true, // 数字键盘 -
        173: true, // 火狐 - 号
        187: true, // +
        189: true, // -
      };
      // 覆盖ctrl||command + ‘+’/‘-’
      document.onkeydown = function (event) {
        const e = event || window.event;
        const ctrlKey = e.ctrlKey || e.metaKey;
        if (ctrlKey && keyCodeMap[e.keyCode]) {
          e.preventDefault();
        } else if (e.detail) {
          // Firefox
          event.returnValue = false;
        }
      };
      // 覆盖鼠标滑动
      document.body.addEventListener(
        "wheel",
        (e) => {
          if (e.ctrlKey) {
            if (e.deltaY < 0) {
              e.preventDefault();
              return false;
            }
            if (e.deltaY > 0) {
              e.preventDefault();
              return false;
            }
          }
        },
        { passive: false }
      );
    },
    init() {
      // 获取数据
      this.getData();
      this.getFiles();
    },
    // 获取 OSS 信息
    getFiles() {
      this.$api
        .get(this, api.getOSSSts, {
          userId: this.distributorId,
        })
        .then((result) => {
          if (result.data) {
            this.client = new OSS.Wrapper({
              region: result.data.region,
              accessKeyId: result.data.accessKeyId,
              accessKeySecret: result.data.accessKeySecret,
              stsToken: result.data.securityToken,
              bucket: result.data.bucketName,
              endpoint: result.data.endpoint,
              secure: true,
            });
            this.hostname = result.data.hostname;
            let dir = "customize/" + this.distributorId + "/image";
            this.client
              .list({
                prefix: dir,
              })
              .then((res) => {
                this.fileList = res.objects;
              })
              .catch((err) => {
                console.log(err);
              });
          }
        });
    },
    getData() {
      this.isLoading = true;
      // 根据型号获取材质
      this.getMaterialList();
      // 获取所有字体
      if (this.fontFamily.length <= 0) {
        this.getFontList();
      }
    },
    // 获取字体列表
    getFontList() {
      this.$api.get(this, api.getFontList).then((res) => {
        if (res.success) {
          if (res.data.length > 0) {
            this.fontFamily = res.data;
            // 引入字体文件
            let style = document.createElement("style");
            style.type = "text/css";
            let str = "";
            this.fontFamily.forEach((font) => {
              str +=
                '@font-face {font-family: "' +
                font.englishName +
                '";src: url("' +
                font.fontFile +
                '"); font-weight: normal;font-style: normal;}';
            });
            style.innerText = str;
            document.getElementsByTagName("head")[0].appendChild(style);
          }
        } else {
          this.isLoading = false;
          this.$message.warning(res.errMessage);
        }
      });
    },
    dataUp(x, y) {
      return x.sequence - y.sequence;
    },
    // 获取手机型号列表
    getModelList(query, type) {
      let picId = this.picId === 0 ? "" : this.picId;
      let materialId = this.materialsId === 0 ? "" : this.materialsId;
      let materialNo = this.$route.query.materialsNo || "";
      let platform = localStorage.getItem("Platform") || 1;

      this.$api
        .get(this, api.getModelList, {
          categoryId: 1, // 产品类型id：1 手机壳
          distributorId: this.distributorId,
          materialId: materialId,
          materialNo: materialNo,
          pictureId: picId,
          platform: platform, // 平台
          search: query, // 搜索型号
        })
        .then((res) => {
          if (res.success) {
            if (res.data && res.data.length > 0) {
              this.modelData = res.data;
              // 初始化品牌ID和名字
              this.brandId = this.modelData[0].modelId;
              this.brandName = this.modelData[0].name;
              if (this.modelData[0].childrenList.length > 0) {
                let curIndex = 0;
                for (
                  var i = 0;
                  i < this.modelData[0].childrenList.length;
                  i++
                ) {
                  if (!this.modelData[0].childrenList[i].underStockFlag) {
                    curIndex = i;
                    break;
                  }
                }
                this.modelId = this.modelData[0].childrenList[curIndex].modelId;
                this.modelName = this.modelData[0].childrenList[curIndex].name;
                this.modelNameEn =
                  this.modelData[0].childrenList[curIndex].englishName;

                // 是否缺货
                this.underStockFlag =
                  this.modelData[0].childrenList[curIndex].underStockFlag;
              }
              // 根据手机型号和材质获取手机定制信息（非搜索状态）
              if (!type) {
                this.getCustomInfo();
              }
            }
          } else {
            this.isLoading = false;
            this.$message.warning(res.errMessage);
          }
        });
    },
    // 获取图库列表
    getPicList() {
      this.$api
        .get(this, api.getPictureList, {
          distributorId: this.distributorId,
          materialId: this.materialsId,
          materialNo: this.materialNo,
          modelId: this.modelId,
          productCategoryId: 1, // 产品类型id：1 手机壳
        })
        .then((res) => {
          if (res.success) {
            this.isLoading = false;
            this.picData = res.data;

            let flag = this.checkPic();
            if (!flag && this.picId !== 0) {
              this.$message.warning("当前图片不可用");
              this.picId = 0;
              this.pictureName = "";
              this.imgData.posX = null;
              this.picUrl = "";
              this.reset();
            } else {
              if (
                this.picUrl !== "" &&
                this.picId !== 0 &&
                this.picType !== 0
              ) {
                this.changeImage(
                  this.picUrl,
                  this.picId,
                  this.picType,
                  this.pictureName
                );
              } else {
                // 重绘
                this.$refs.mcanvas.initCanvas();
                if (Number(this.picType) === 2) {
                  this.$refs.mcanvas.drawIpImage();
                } else {
                  this.$refs.mcanvas.draw();
                }
              }
            }
          } else {
            this.isLoading = false;
            this.$message.warning(res.errMessage);
          }
        });
    },
    // 获取材质列表
    getMaterialList() {
      // 获取传过来的材质参数
      let materialNo = this.$route.query.materialsNo || "";
      let isZFold = this.modelName.toUpperCase().indexOf("FOLD3") >= 0 ? 1 : 0;
      let picId = this.picId === 0 || isZFold ? "" : this.picId;
      let platform = localStorage.getItem("Platform") || 5;

      this.$api
        .get(this, api.getMaterialList, {
          pictureId: picId,
          categoryId: 1, // 产品类型id：1 手机壳
          distributorId: this.distributorId,
          modelId: this.modelId,
          platform: platform, // 平台
        })
        .then((res) => {
          if (res.success) {
            if (res.data && res.data.length > 0) {
              let materialArr = res.data;
              let hasMaterial = false; // 是否有匹配

              if (materialNo) {
                // 材质编码
                materialArr.forEach((material) => {
                  if (
                    material.childrenList &&
                    material.childrenList.length > 0
                  ) {
                    material.childrenList.forEach((child) => {
                      if (child.materialNo === materialNo) {
                        this.materialsId = child.materialId;
                        this.materialNo = child.materialNo;
                        this.skuNo = child.itemCode;
                        this.materialsName = material.name + "-" + child.name;
                        this.materialsNameEn =
                          material.englishName + "-" + child.englishName;
                        // 定制后需要给工厂的文件类型：LHW-图片  YC-pdf
                        this.manufactor = child.manufactor;
                        // 材质颜色值
                        this.materialsColorValue = child.colour;

                        // 获取是否允许用户上传
                        this.uploadValid = child.allowUploadFlag;
                        // 是否强制铺满血位：1 是，0 否
                        let isAllPlace = child.mandatoryCoveredBleedFlag;
                        sessionStorage.setItem("isAllPlace", isAllPlace);

                        hasMaterial = true; // 有匹配
                      }
                    });
                  }
                });
              } else {
                hasMaterial = true; // 有匹配

                this.materialData = materialArr;
                if (
                  res.data[0].childrenList &&
                  res.data[0].childrenList.length > 0
                ) {
                  this.materialsId = res.data[0].childrenList[0].materialId;
                  this.materialNo = res.data[0].childrenList[0].materialNo;
                  this.skuNo = res.data[0].childrenList[0].itemCode;
                  this.materialsName =
                    res.data[0].name + "-" + res.data[0].childrenList[0].name;
                  this.materialsNameEn =
                    res.data[0].englishName +
                    "-" +
                    res.data[0].childrenList[0].englishName;
                  // 定制后需要给工厂的文件类型：LHW-图片  YC-pdf
                  this.manufactor = res.data[0].childrenList[0].manufactor;
                  // 材质颜色值
                  this.materialsColorValue = res.data[0].childrenList[0].colour;

                  // 获取是否允许用户上传
                  this.uploadValid =
                    res.data[0].childrenList[0].allowUploadFlag;
                  // 是否强制铺满血位：1 是，0 否
                  let isAllPlace =
                    res.data[0].childrenList[0].mandatoryCoveredBleedFlag;
                  sessionStorage.setItem("isAllPlace", isAllPlace);
                } else {
                  this.materialsId = res.data[0].materialId;
                  this.materialNo = res.data[0].materialNo;
                  this.skuNo = res.data[0].itemCode;
                  this.materialsName = res.data[0].name;
                  this.materialsNameEn = res.data[0].englishName;
                  // 定制后需要给工厂的文件类型：LHW-图片  YC-pdf
                  this.manufactor = res.data[0].manufactor;
                  // 材质颜色值
                  this.materialsColorValue = res.data[0].colour;
                }
              }

              if (!hasMaterial) {
                // 无匹配
                this.$message.warning(
                  "暂无关联的材质数据，请选择其他机型或联系客服处理~"
                );
              } else {
                // 获取所有手机型号
                this.getModelList(this.query);
              }
            } else {
              this.materialData = [];
              this.picData = [];
              this.materialsId = 0;
              this.materialNo = "";
              this.skuNo = "";
              this.materialsName = "";
              this.materialsNameEn = "";
              this.materialsColorValue = "";
            }
          } else {
            this.isLoading = false;
            this.$message.warning(res.errMessage);
          }
        });
    },
    // 根据手机型号和材质获取手机定制信息
    getCustomInfo() {
      this.$api
        .get(this, api.getCustomInfo, {
          modelId: this.modelId,
          materialId: this.materialsId,
        })
        .then((res) => {
          if (res.success) {
            if (res.data) {
              this.modelinfo = res.data;
              this.bgImg = this.modelinfo.floorImage;
              this.frameImg = this.modelinfo.outImage;
              // 手机图长宽比
              this.pscale =
                this.pm2px(this.modelinfo.width) /
                this.pm2px(this.modelinfo.length);
              this.phonenHeight = Math.round(this.canvasHeight * 0.7);
              this.phonenWidth = Math.round(this.phonenHeight * this.pscale);

              /**
               * 判断材质类型：
               *  74    玻璃壳
               *  82    TPU（亮透彩印软壳）
               *  84    热升华（真彩创意壳）
               *  86    亲肤壳(晶透浮雕壳)
               *  91    防摔壳
               *  108   炫彩肤感壳-古董白
               *  109   炫彩肤感壳-经典黑
               */
              switch (Number(this.materialsId)) {
                case 74:
                  this.materialsType = 1;
                  break;
                case 82:
                case 91:
                case 108:
                case 109:
                case 874:
                  this.materialsType = 2;
                  break;
                case 84:
                  this.materialsType = 3;
                  break;
                case 86:
                  this.materialsType = 4;
                  break;
                default:
                  this.materialsType = 2;
                  break;
              }

              if (
                Number(this.materialsId) === 74 ||
                Number(this.materialsId) === 84
              ) {
                // 玻璃壳/热升华+2mm
                this.phoneHeight = this.canvasHeight * 0.7 + this.pm2px(2);
                this.phoneWidth = this.phoneHeight * this.pscale;
                let mscale = this.modelinfo.width / this.modelinfo.length;
                if (this.modelinfo.width > this.modelinfo.length) {
                  this.mh = this.modelinfo.length + 2;
                  this.mw = this.mh * mscale;
                } else {
                  this.mw = this.modelinfo.width + 2;
                  this.mh = this.mw / mscale;
                }
                this.nh = this.modelinfo.length;
                this.nw = this.modelinfo.width;
              } else {
                // TPU/亲肤壳
                this.phoneHeight = this.canvasHeight * 0.7;
                this.phoneWidth = this.phoneHeight * this.pscale;
                this.mh = this.nh = this.modelinfo.length;
                this.mw = this.nw = this.modelinfo.width;
              }

              // 透明间距
              let frameValue = {
                topFrame: this.modelinfo.topFrame || 0,
                underFrame: this.modelinfo.underFrame || 0,
                leftFrame: this.modelinfo.leftFrame || 0,
                rightFrame: this.modelinfo.rightFrame || 0,
              };
              sessionStorage.setItem("frameValue", JSON.stringify(frameValue));

              // 判断是否有切图信息（折叠屏）
              if (
                (this.modelinfo.cutType === 1 &&
                  this.modelinfo.slittingHeight) ||
                (this.modelinfo.cutType === 2 &&
                  this.modelinfo.crosscuttingWidth)
              ) {
                let cutInfo = JSON.stringify({
                  cutType: this.modelinfo.cutType,
                  slittingHeight: this.modelinfo.slittingHeight,
                  slittingWhite: this.modelinfo.slittingWhite,
                  crosscuttingWidth: this.modelinfo.crosscuttingWidth,
                  crosscuttingWhite: this.modelinfo.crosscuttingWhite,
                });
                sessionStorage.setItem("cutInfo", cutInfo);
              } else {
                sessionStorage.setItem("cutInfo", "");
              }

              let isZFold =
                this.modelName.toUpperCase().indexOf("FOLD3") >= 0 ? 1 : 0;
              this.isZFold = isZFold;
              if (Number(sessionStorage.getItem("isZFold")) !== isZFold) {
                this.$refs.mcanvas.dragArr.forEach((item, index) => {
                  if (item.type === 2) {
                    // 清空文字
                    this.$refs.mcanvas.dragArr.splice(index, 1);
                  }
                });
              }
              sessionStorage.setItem("isZFold", isZFold);

              if (this.materialsId !== 0) {
                // 根据型号和材质获取图库
                this.getPicList();
                // 获取价格
                this.getPrice();
              }
            }
          } else {
            this.$message.warning(res.errMessage);
          }
          this.isLoading = false;
        });
    },
    // 更换型号
    changeModel(item, idx) {
      if (!item.underStockFlag) {
        this.modelId = item.modelId;
        this.modelName = item.name;
        this.modelNameEn = item.englishName;
        // 是否缺货
        this.underStockFlag = item.underStockFlag;
        // 重置图片，文字
        this.imgData.posX = null;
        this.textData.posX = null;
        // 根据手机型号和材质获取手机定制信息
        this.getCustomInfo();
      }
    },
    // 获取价格
    getPrice() {
      let orderSource = localStorage.getItem("orderSource") || 5;
      // 获取传过来的价格参数
      let price = this.$route.query.price;
      if (price) {
        this.price = price;
      } else {
        this.$api
          .get(this, api.getPrice, {
            distributorId: this.distributorId,
            orderSource: orderSource,
            materialId: this.materialsId,
          })
          .then((res) => {
            if (res.success) {
              if (res.data) {
                this.price = (res.data + this.copyrightCost).toFixed();
              } else {
                this.price = this.copyrightCost.toFixed();
              }
            } else {
              this.price = 0;
              this.$message.error(res.errMessage);
              this.reset();
            }
          })
          .catch((err) => {
            console.log(err);
          });
      }
    },
    getIcon(value) {
      if (value >= 250) {
        this.dpiDesc = "";
        return "el-icon-success";
      } else if (value >= 100 && value < 250) {
        this.dpiDesc = "100-250dpi，有点模糊，效果一般";
        return "el-icon-warning";
      } else {
        this.dpiDesc = "小于100dpi，非常模糊，不建议印刷";
        return "el-icon-remove";
      }
    },
    handleClick(tab, event) {
      this.brandId = tab.$attrs.mid;
      this.brandName = tab.name;
    },
    getdpi(value, ratio) {
      this.dpiValue = value;
      this.ratio = ratio;
    },
    change(pic) {
      this.imgData.posX = null;

      // 计算价格（加上图片版权费），如果已传价格，不计算
      this.copyrightCost = 0;
      if (!this.price && pic.copyrightCost) {
        this.copyrightCost = pic.copyrightCost;
      }

      this.changeImage(pic.originImage, pic.id, pic.type, pic.name);
    },
    // 添加、更换图片
    changeImage(url, id, type, name) {
      if (this.materialsId !== 0) {
        let img = new Image();
        img.crossOrigin = "anonymous";
        img.src = url + "?t=" + new Date().getTime();
        this.picUrl = url;
        this.picId = id;
        this.pictureName = name;
        // 图片类型 0-网络图 1-普通素材 2-IP素材（不可更改）
        this.picType = type;
        img.onload = () => {
          let w = img.width;
          let h = img.height;
          let diffX = 0;
          let diffY = 0;
          let scale = this.getScale(w, h);
          this.showDpi = true;
          let centerX, centeruX;
          let centerY, centeruY;
          this.imgData.initW = w / scale;
          this.imgData.initH = h / scale;
          if (this.imgData.posX == null || type === 2) {
            centerX = (this.winWidth - this.leftWidth) / 2;
            if (this.isZFold) {
              centerX =
                (this.winWidth - this.leftWidth - this.phonenWidth / 2) / 2;
            }
            centerY = this.canvasHeight / 2;
            this.imgData.sizeW = w / scale;
            this.imgData.sizeH = h / scale;
            this.imgData.posX = centerX - w / scale / 2;
            this.imgData.posY = centerY - h / scale / 2;
            centeruX = this.phoneWidth / 2;
            centeruY = this.phoneHeight / 2;
            diffX = (this.phoneWidth - this.phonenWidth) / 2;
            if (this.isZFold) {
              diffX = (this.phoneWidth - this.phonenWidth / 2) / 2;
            }
            diffY = (this.phoneHeight - this.phonenHeight) / 2;
            this.imgData.posuX = centeruX - w / scale / 2 - diffX;
            this.imgData.posuY = centeruY - h / scale / 2 - diffY;
            this.imgData.rotate = 0;
            this.imgData.linew = 0;
            this.imgData.lineh = 0;
          } else {
            // 确定中心坐标
            centerX = this.imgData.posX + this.imgData.sizeW / 2;
            centerY = this.imgData.posY + this.imgData.sizeH / 2;
            centeruX = this.imgData.posuX + this.imgData.sizeW / 2;
            centeruY = this.imgData.posuY + this.imgData.sizeH / 2;

            // 缩放更换型号再换图片
            this.imgData.sizeW = w / scale / this.ratio;
            this.imgData.sizeH = h / scale / this.ratio;
            this.imgData.posX = centerX - this.imgData.sizeW / 2;
            this.imgData.posY = centerY - this.imgData.sizeH / 2;
            this.imgData.posuX = centeruX - this.imgData.sizeW / 2;
            this.imgData.posuY = centeruY - this.imgData.sizeH / 2;
            this.imgData.linew = 0;
            this.imgData.lineh = 0;
          }

          let imgObj = {
            x: this.imgData.posX,
            y: this.imgData.posY,
            initx: this.imgData.posX,
            inity: this.imgData.posY,
            ux: this.imgData.posuX,
            uy: this.imgData.posuY,
            url: img,
            width: this.imgData.sizeW,
            height: this.imgData.sizeH,
            initW: this.imgData.initW,
            initH: this.imgData.initH,
            // 最小宽高
            minW: this.imgData.sizeW,
            minH: this.imgData.sizeH,
            type: 1,
            picType: this.picType,
            picId: this.picId,
            rotate: this.imgData.rotate,
            selected: true,
            index: null,
          };
          this.spriteArr = [];
          this.spriteArr.push(imgObj);
        };
      }
    },
    // 更改选择文字
    selectText(sprite, index) {
      // 图片，清空文字信息
      if (sprite.type === 1 && index === null) {
        this.clearText(sprite.type);
        this.imgData.posX = sprite.x;
        this.imgData.posY = sprite.y;
        this.imgData.posuX = sprite.ux;
        this.imgData.posuY = sprite.uy;
        this.imgData.sizeW = sprite.w;
        this.imgData.sizeH = sprite.h;
        this.imgData.rotate = sprite.rotate;
        this.imgData.linew = sprite.linew;
        this.imgData.lineh = sprite.lineh;
        this.textData.posX = null;
        this.showDpi = true;
      } else if (sprite.type === 2) {
        // 切换至文字
        this.activeTab = "tabtext";
        this.textData.posX = sprite.x;
        this.textData.posY = sprite.y;
        this.textData.posuX = sprite.ux;
        this.textData.posuY = sprite.uy;
        this.textData.sizeW = sprite.w;
        this.textData.sizeH = sprite.h;
        this.font = sprite.fontfamily;
        this.textarea = sprite.filltext;
        this.color = sprite.fillstyle;
        this.textData.rotate = sprite.rotate;
        this.index = index;

        this.$refs.mcanvas.draw(); // 重绘，以免删减文字错位
      }
    },
    // 添加、更改文字
    addText() {
      if (this.validateText()) {
        if (this.textData.posX === null) {
          // 获取中心坐标
          this.textData.posX = (this.winWidth - this.leftWidth) / 2;
          this.textData.posY = this.canvasHeight / 2;
          this.textData.posuX = this.phonenWidth / 2;
          this.textData.posuY = this.phonenHeight / 2;
          this.textData.sizeW = 20;
          this.textData.sizeH = 20;
          this.textData.rotate = 0;
          if (this.isZFold) {
            this.textData.posX =
              (this.winWidth - this.leftWidth - this.phonenWidth / 2) / 2;
            this.textData.posuX = this.phonenWidth / 4;
          }
          this.index = this.index === null ? this.index : null;
        }
        let textObj = {
          x: this.textData.posX,
          y: this.textData.posY,
          ux: this.textData.posuX,
          uy: this.textData.posuY,
          url: "",
          width: this.textData.sizeW,
          height: this.textData.sizeH,
          initW: this.textData.sizeW,
          initH: this.textData.sizeH,
          minW: 10,
          minH: 10,
          fillstyle: this.color,
          filltext: this.textarea,
          fontsize: this.textData.sizeH,
          fontfamily: this.font,
          type: 2,
          picType: 0,
          rotate: this.textData.rotate,
          selected: true,
          index: this.index,
        };
        this.spriteArr = [];
        this.spriteArr.push(textObj);
      }
    },
    // 验证文字信息
    validateText() {
      if (this.font === "") {
        this.$message.warning("请选择字体");
        return false;
      }
      if (this.textarea === "") {
        this.$message.warning("请输入文字");
        return false;
      }
      return true;
    },
    getImgDPI(w, h, r) {
      // 根据图片像素率（px）和图片尺寸（英寸）计算图片dpi
      let icw = this.modelinfo.width / 25.4;
      let ich = this.modelinfo.length / 25.4;
      let dpi = w / icw > h / ich ? h / ich : w / icw;
      let imgDpi = Math.round(dpi);
      this.dpiValue = imgDpi;
    },
    // mm转px
    pm2px(d) {
      var iswindow = /windows|win32|win64/i.test(navigator.userAgent);
      if (iswindow) {
        return Math.round((d * 96) / 25.4);
      } else {
        return Math.round((d * 72) / 25.4);
      }
    },
    // px转mm
    px2pm(d) {
      var iswindow = /windows|win32|win64/i.test(navigator.userAgent);
      if (iswindow) {
        return Math.round((d * 25.4) / 96); // 电脑端
      } else {
        return Math.round((d * 25.4) / 72); // 手机端
      }
    },
    // 计算图片缩放比
    getScale(width, height) {
      let phoneWidth = this.phoneWidth;
      if (this.isZFold) {
        phoneWidth = this.phoneWidth / 2;
      }
      return height / this.phoneHeight > width / phoneWidth
        ? width / phoneWidth
        : height / this.phoneHeight;
    },
    // 判断当前图片是否可用
    checkPic() {
      let flag = false;
      this.picData.forEach((item) => {
        if (item.childrenList.length > 0) {
          item.childrenList.forEach((child) => {
            if (child.pictureList && child.pictureList.length > 0) {
              child.pictureList.forEach((pic) => {
                if (pic.id === this.picId) {
                  flag = true;
                }
              });
            }
          });
        } else {
          if (item.pictureList && item.pictureList.length > 0) {
            item.pictureList.forEach((pic) => {
              if (pic.id === this.picId) {
                flag = true;
              }
            });
          }
        }
      });
      return flag;
    },
    // 完成定制
    make() {
      if (this.underStockFlag) {
        this.$message.warning({
          title: "警告",
          message: "该机型缺货暂时无法下单",
        });
        return;
      }

      let hasPic = false;
      if (this.$refs.mcanvas.dragArr && this.$refs.mcanvas.dragArr.length > 0) {
        let arr = this.$refs.mcanvas.dragArr;
        arr.forEach((item) => {
          if (Number(item.type) === 1 && Number(item.picType) !== 4) {
            // 有图片
            hasPic = true;
          }
        });
      }

      if (!hasPic) {
        this.$message.warning({
          title: "警告",
          message: "请选择图片",
        });
        return;
      }


      this.isLoading = true;

      // 判断IP图片是否已经定制
      if (this.picType === 2) {
        // 获取缓存IP图
        this.$api
          .get(this, api.getCacheIP, {
            pictureId: this.picId,
            materialId: this.materialsId,
            modelId: this.modelId,
          })
          .then((res) => {
            if (res.success) {
              if (
                res.data &&
                (res.data.generateImage.indexOf(".pdf") > -1 ||
                  res.data.generateImage.indexOf(".png") > -1)
              ) {
                // // pdf 或 png
                let imgUrl = res.data.previewImage;
                let pdfUrl = res.data.generateImage;
                this.isMake = true;
                this.submit(imgUrl, pdfUrl);
              } else {
                this.$refs.mcanvas.make();
              }
            }
          });
      } else {
        this.$refs.mcanvas.make();
      }
    },
    // 定制成功并跳转
    submit(imgUrl, pdfUrl) {
      let flag = true;
      // IP图
      if (this.picType === 2 && !this.isMake) {
        // 缓存IP图
        this.$api
          .post(this, api.handleCacheIP, {
            generateImage: pdfUrl,
            materialId: this.materialsId,
            modelId: this.modelId,
            pictureId: this.picId,
            previewImage: imgUrl,
          })
          .then((res) => {
            if (res.success) {
              flag = true;
            } else {
              flag = false;
            }
          })
          .catch((err) => {
            flag = false;
            console.log(err);
          });
      }
      if (flag) {
        this.diyPic = imgUrl;
        this.diyPdf = pdfUrl;
        this.diyDialogVisible = true;
      }
    },
    // 加载图片manufactor
    loadImage(url) {
      return new Promise((resolve) => {
        let img = new Image();
        img.crossOrigin = "anonymous";
        img.onload = () => resolve(img);
        img.src = url;
      });
    },
    // 搜索清空
    clear() {
      this.query = "";
    },
    // 重置
    reset() {
      this.$refs.mcanvas.reset();
    },
    // 清空文字
    clearText(type) {
      // 1--图片  2--文字 0--未选中
      if (type === 1) {
        this.imgData.posX = null;
        this.showDpi = false;
        this.picUrl = "";
      } else if (type === 2) {
        this.textarea = "";
        this.font = "";
        this.color = this.color;
        this.textData.posX = null;
      } else if (type === 0) {
        this.imgData.posX = null;
        this.textData.posX = null;
        this.textarea = "";
        this.font = "";
        this.color = this.color;
        this.showDpi = false;
      }
    },
    // 移除上传图片
    handleRemove(file) {
      this.client.delete(file.name);
    },
    // 选中上传图片
    handlePreview(file) {
      this.files.forEach((item) => {
        if (file.uid === item.uid) {
          file.url = item.url;
        }
      });
      this.imgData.posX = null;
      this.changeImage(file.url, 0, 1, ""); // 上传图片为普通图片类型1
    },
    // 上传成功
    handleSuccess(response, file, fileList) {},
    // 上传图片前
    handleBeforeUpload(file) {
      let type =
        file.type === "image/png" ||
        file.type === "image/jpg" ||
        file.type === "image/jpeg" ||
        file.type === "image/bmp";
      let size = file.size / 1024 / 1024 / 2;
      if (!type) {
        this.$message.warning({
          title: "警告",
          message: "请上传格式为image/png, image/jpg, image/jpeg的图片",
        });
        return false;
      }
      if (size > 10) {
        this.$message.warning({
          title: "警告",
          message: "图片大小必须小于10MB",
        });
        return size;
      }
      let uid = window.localStorage.getItem("userId");
      // 随机命名
      let randomName =
        this.random_string(6) +
        "_" +
        new Date().getTime() +
        "." +
        file.name.split(".").pop();
      // 上传
      this.client
        .multipartUpload("customize/" + uid + "/image/" + randomName, file, {})
        .then((results) => {
          let url = this.hostname + results.name;
          this.files.push({ uid: file.uid, url: url });
        })
        .catch((err) => {
          console.log(err);
        });
    },
    // 随机生成文件名
    random_string(len) {
      len = len || 32;
      var chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678";
      var maxPos = chars.length;
      var pwd = "";
      for (let i = 0; i < len; i++) {
        pwd += chars.charAt(Math.floor(Math.random() * maxPos));
      }
      return pwd;
    },
    diyCancel() {
      this.diyDialogVisible = false;
      this.btnLoading = false;
      this.isLoading = false;
    },
    diySubmit() {
      let skuId = this.$route.query.skuId; 
      let product = {
        image: this.diyPic,
        generateImage: this.diyPdf,
        manufactor: this.manufactor,
        materialId: this.materialsId,
        material: this.materialsName,
        brandId: this.brandId,
        brandName: this.brandName,
        modelId: this.modelId,
        modelName: this.modelName,
        pictureId: this.picId,
        price: this.price,
        skuNo: this.skuNo,
        skuId: skuId,
        // count: this.diyNum,
        // totalPrice: this.price * this.diyNum,
        count: 1,
        totalPrice: this.price,
      };
      this.btnLoading = true;

      this.$api
        .post(this, api.handleOrder, {
          orderDetail: product,
        })
        .then((res) => {
          if (res.success) {
            let url = res.data.url;
            this.isLoading = false;

            window.location.href = url;
          } else {
            this.isLoading = false;
            this.$message.warning(res.errMessage);
          }

          this.btnLoading = false;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    handleUploadValid() {
      // 判断是否允许用户上传
      if (Number(this.uploadValid) !== 1) {
        this.$message.warning("当前材质不支持图片上传");
      }
    },
    // 监听键盘输入
    numChange(e) {
      let targetVal = e.target.value.replace(/[^0-9]/g, "");
      // 每次keyup时把值赋给 v-model
      let value = 0;
      if (!targetVal) {
        // 这里给undefined是因为不给值就会默认变为1，不会为空，导致用户输入不了别的数值
        value = undefined;
        this.diyNum = value;
      } else {
        value = targetVal;
        this.diyNum = value;
      }

      this.handleChange(value);
    },
    handleChange(value) {
      // 真彩创意壳立即购买是判断数量是否大于等于10
      if (this.buyFlag && Number(this.materialsId) === 84 && value >= 10) {
        this.isMaxNum = true;
      } else {
        this.isMaxNum = false;
      }
    },
  },
  watch: {
    materialsId(value) {
      this.materialsId = value;
      // 判断当前图片是否可用
      this.checkPic();
    },
    modelId(value) {
      this.modelId = value;
      this.checkPic();
    },
    fileList(list) {
      this.fileList = list;
    },
    dpiValue(value) {
      this.dpiValue = value > 300 ? 300 : value;
    },
    diyNum(value) {
      // 真彩创意壳立即购买是判断数量是否大于等于10
      if (this.buyFlag && Number(this.materialsId) === 84 && value >= 10) {
        this.isMaxNum = true;
      } else {
        this.isMaxNum = false;
      }
    },
  },
};
</script>

<style lang='stylus'>
$white = #ffffff;
$font-base = #333333;
$border-color = #e5e5e5;

.el-dialog__wrapper {
  .el-dialog {
    width: 500px;

    .el-dialog__header {
      text-align: center;
    }

    .el-dialog__body {
      padding: 20px 0;
    }

    .el-dialog__footer {
      text-align: center;

      .el-button {
        color: $font-base;
        border-color: $border-color;
        background-color: transparent;

        &:hover {
          opacity: 0.75;
        }
      }

      .submit-btn {
        color: $white;
        border-color: var(--mainColor);
        background-color: var(--mainColor);
      }
    }
  }
}
</style>

<style scoped lang='stylus'>
$bg = #f1f3f9;
$white = #ffffff;
$red = #ef2049;
$orange = #e68402;
$green = #0bab1d;
$font-base = #333333;
$font-grey = #999999;
$note-color = #da0b0b;
$border-color = #e5e5e5;
$close-bg = #ff6665;

html, body {
  background-color: $bg;
  min-width: 1260px;
}

.wrap {
  width: 100%;
  height: 100%;
}

.customize {
  width: 100%;
  height: 100%;
  background-color: $bg;
  overflow: hidden;

  .header {
    width: 100%;
    height: 62px;
    padding: 0 80px 0 20px;
    box-sizing: border-box;
    line-height: 62px;
    font-size: 18px;
    color: $white;
    background-color: var(--mainColor);
    overflow: hidden;
    z-index: 12;

    .title {
      float: left;

      .price {
        padding-left: 61px;
        position: relative;

        &::before {
          content: '';
          position: absolute;
          top: 50%;
          left: 30px;
          width: 1px;
          height: 16px;
          background-color: $white;
          transform: translateY(-50%);
        }
      }
    }

    .next {
      float: right;
      font-size: 18px;
      color: $white;
      cursor: pointer;

      .el-icon-d-arrow-right {
        position: relative;
        top: 2px;
        font-size: 12px;
      }
    }
  }

  .container {
    display: flex;
    margin-top: 16px;

    .left-content {
      height: calc(100vh - 78px);
      z-index: 12;

      >>>.el-tabs {
        display: flex;

        .el-tabs__header {
          display: inline-block;
          width: 120px;
          margin-right: 20px;

          .el-tabs__nav-wrap::after {
            width: 0;
          }

          .el-tabs__active-bar {
            height: 0 !important;
          }

          .el-tabs__item {
            margin-bottom: 6px;
            text-align: center;
            font-size: 14px;
            padding: 0 20px;
            color: $font-base;
            background-color: $white;

            i {
              margin-right: 10px;
              color: $font-base;

              &.icon-text {
                display: inline-block;
                width: 14px;
                height: 14px;
                vertical-align: middle;
                margin-top: -2px;
                background: url('../../common/images/p-icon/icon-text.png') no-repeat;
                background-size: 14px 14px;
              }
            }

            &.is-disabled, &.is-disabled:hover {
              padding: 0;
              color: $font-grey !important;

              i {
                color: $font-grey;
              }

              span {
                padding: 0 20px;
                display: block;
              }
            }

            &.is-active {
              color: $white !important;
              background-color: var(--mainColor);

              i {
                color: $white;

                &.icon-text {
                  background: url('../../common/images/p-icon/icon-text-active.png') no-repeat;
                  background-size: 14px 14px;
                }
              }
            }
          }
        }

        .el-tabs__content {
          display: inline-block;
          background-color: $white;
          overflow-y: scroll;
          -ms-scroll-chaining: chained;
          -ms-overflow-style: none;
          -ms-content-zooming: zoom;
          -ms-scroll-rails: none;
          -ms-content-zoom-limit-min: 100%;
          -ms-content-zoom-limit-max: 500%;
          -ms-scroll-snap-type: proximity;
          -ms-scroll-snap-points-x: snapList(100%, 200%, 300%, 400%, 500%);
          -ms-overflow-style: none;
          overflow-y: scroll;
          scrollbar-width: none;

          &::-webkit-scrollbar {
            width: 0;
            height: 0;
            background: none;
            display: none;
          }

          &.-o-scrollbar {
            -moz-appearance: none !important;
            background: rgba(0, 255, 0, 0) !important;
          }

          .item-box {
            width: 460px;
            min-height: auto;
            max-height: calc(100vh - 102px);

            .pic {
              max-height: calc(100vh - 102px);
              overflow: hidden;
            }

            &.upload {
              padding: 0;
              box-sizing: border-box;
              background-color: $white;

              .upload-box {
                padding: 40px 30px 20px;
                text-align: center;
                box-sizing: border-box;

                label {
                  background-color: var(--mainColor);
                  z-index: 9;

                  .icon {
                    font-size: 18px;
                    color: $white;
                  }

                  .text {
                    display: inline-block;
                    font-size: 14px;
                    color: $white;
                  }
                }

                .tip {
                  margin-top: 20px;
                  font-size: 12px;
                  color: $font-grey;
                }

                .el-upload {
                  width: 100%;

                  .el-button {
                    color: $white;
                    background-color: var(--mainColor);
                    border-color: var(--mainColor);

                    &:hover {
                      opacity: 0.75;
                    }

                    .icon {
                      margin-right: 5px;
                    }
                  }

                  .el-upload-dragger {
                    width: 100%;
                    height: auto;
                    padding: 40px 30px 20px;
                    box-sizing: border-box;

                    .el-button {
                      padding: 0 20px;

                      span {
                        vertical-align: middle;
                        line-height: 2;

                        .icon {
                          margin: 0 5px 0 0;
                          font-size: 18px;
                          line-height: 2;
                          color: $white;
                          vertical-align: middle;
                        }
                      }
                    }

                    .el-upload__tip {
                      margin-top: 20px;

                      &.note {
                        font-size: 12px;
                        color: $note-color;
                      }
                    }
                  }
                }

                .el-upload__tip {
                  margin-top: 20px;
                  font-size: 14px;
                  color: $font-grey;

                  &.title {
                    margin: 50px 0 20px;
                    text-align: left;
                    font-size: 14px;
                  }

                  &.note {
                    margin-top: 10px;
                    color: $note-color;
                  }
                }

                .el-upload-list {
                  padding: 0 10px 10px;
                  min-height: 200px;
                  max-height: calc(100vh - 422px);
                  background-color: $bg;
                  -ms-scroll-chaining: chained;
                  -ms-overflow-style: none;
                  -ms-content-zooming: zoom;
                  -ms-scroll-rails: none;
                  -ms-content-zoom-limit-min: 100%;
                  -ms-content-zoom-limit-max: 500%;
                  -ms-scroll-snap-type: proximity;
                  -ms-scroll-snap-points-x: snapList(
                    100%,
                    200%,
                    300%,
                    400%,
                    500%
                  );
                  -ms-overflow-style: none;
                  overflow-y: scroll;
                  scrollbar-width: none;

                  &::-webkit-scrollbar {
                    width: 0;
                    height: 0;
                    background: none;
                    display: none;
                  }

                  &.-o-scrollbar {
                    -moz-appearance: none !important;
                    background: rgba(0, 255, 0, 0) !important;
                  }

                  .el-upload-list__item {
                    width: 31.2%;
                    height: 200px;
                    padding: 0;
                    margin: 10px 4px 0;
                    border: 1px dotted $border-color;
                    border-radius: 0;
                    background-color: transparent;
                    float: left;

                    .el-upload-list__item-name {
                      position: absolute;
                      top: 0;
                      left: 0;
                      right: 0;
                      margin: 0;
                      bottom: 0;
                      padding: 0;
                      color: transparent;
                      z-index: 9;

                      .el-icon-document {
                        display: none;
                      }
                    }

                    .el-upload-list__item-status-label {
                      display: none;
                    }

                    .el-icon-close {
                      top: 0;
                      right: 0;
                      font-size: 18px;
                      opacity: 1;
                      color: $white;
                      background-color: $close-bg;
                      z-index: 9;
                    }

                    img {
                      width: 100%;
                      height: auto;
                      margin: 0;
                      top: 50%;
                      transform: translateY(-50%);
                    }
                  }
                }

                .pic-list {
                  display: flex;
                  flex-direction: row;
                  flex-wrap: wrap;
                  justify-content: flex-start;
                  padding: 0 10px 10px;
                  min-height: 200px;
                  max-height: 600px;
                  background-color: $bg;
                  -ms-scroll-chaining: chained;
                  -ms-overflow-style: none;
                  -ms-content-zooming: zoom;
                  -ms-scroll-rails: none;
                  -ms-content-zoom-limit-min: 100%;
                  -ms-content-zoom-limit-max: 500%;
                  -ms-scroll-snap-type: proximity;
                  -ms-scroll-snap-points-x: snapList(
                    100%,
                    200%,
                    300%,
                    400%,
                    500%
                  );
                  -ms-overflow-style: none;
                  overflow-y: scroll;
                  scrollbar-width: none;

                  &::-webkit-scrollbar {
                    width: 0;
                    height: 0;
                    background: none;
                    display: none;
                  }

                  &.-o-scrollbar {
                    -moz-appearance: none !important;
                    background: rgba(0, 255, 0, 0) !important;
                  }

                  .pic-item {
                    width: 100%;
                    height: 140px;
                    padding: 0;
                    margin: 10px 4px 0;
                    border: 1px dotted $border-color;
                    border-radius: 0;
                    box-sizing: border-box;
                    cursor: pointer;
                    background-color: transparent;
                    overflow: hidden;

                    .el-icon-close {
                      position: absolute;
                      top: 0;
                      right: 0;
                      font-size: 18px;
                      display: none;
                      color: $white;
                      background-color: $close-bg;
                      z-index: 9;
                    }

                    &:hover {
                      .el-icon-close {
                        display: inline-block;
                      }
                    }

                    img {
                      position: absolute;
                      width: 100%;
                      height: auto;
                      margin: 0;
                      left: 0;
                      top: 50%;
                      transform: translateY(-50%);
                    }
                  }
                }
              }
            }

            .el-tabs {
              display: flex;
              background-color: $white;

              .el-tabs__header {
                display: inline-block;
                width: auto;
                min-width: 82px;
                margin-right: 0;
                background-color: $white;
                max-height: calc(100vh - 102px);
                -ms-scroll-chaining: chained;
                -ms-overflow-style: none;
                -ms-content-zooming: zoom;
                -ms-scroll-rails: none;
                -ms-content-zoom-limit-min: 100%;
                -ms-content-zoom-limit-max: 500%;
                -ms-scroll-snap-type: proximity;
                -ms-scroll-snap-points-x: snapList(
                  100%,
                  200%,
                  300%,
                  400%,
                  500%
                );
                -ms-overflow-style: none;
                overflow-y: scroll;
                scrollbar-width: none;

                &::-webkit-scrollbar {
                  width: 0;
                  height: 0;
                  background: none;
                  display: none;
                }

                &.-o-scrollbar {
                  -moz-appearance: none !important;
                  background: rgba(0, 255, 0, 0) !important;
                }

                .el-tabs__active-bar {
                  margin: 13px 0;
                  height: 14px !important;
                  right: 98%;
                  background-color: var(--mainColor);
                }

                .el-tabs__item {
                  margin-bottom: 0;
                }

                .el-tabs__item.is-active {
                  color: var(--mainColor) !important;
                  background-color: $white;
                }
              }

              .el-tabs__content {
                flex: 1;
                padding: 14px 20px 40px;
                box-sizing: border-box;
                border-left: 2px solid $bg;
                background-color: $white;

                .search-box {
                  display: flex;
                  padding: 0 15px;
                  height: 30px;
                  line-height: 30px;
                  font-size: 12px;
                  box-sizing: border-box;
                  border: none;
                  color: $font-grey;
                  background-color: $bg;

                  .box {
                    flex: 1;
                    text-align: center;
                    outline: none;

                    &::placeholder {
                      color: $font-grey;
                    }
                  }

                  .el-icon-search, .el-icon-close {
                    display: inline-block;
                    line-height: 30px;
                    color: $font-grey;
                  }

                  .el-icon-close {
                    cursor: pointer;
                  }
                }

                .el-input__inner, .box {
                  height: 30px;
                  line-height: 30px;
                  font-size: 12px;
                  text-align: center;
                  border: none;
                  color: $font-grey;
                  background-color: $bg;

                  &::placeholder {
                    color: $font-grey;
                  }
                }

                .el-input__icon {
                  line-height: 30px;
                }

                .type-list {
                  display: block;
                  width: 100%;
                  margin-top: 48px;

                  .title {
                    display: flex;
                    margin: 48px 0 30px;
                    font-size: 14px;
                    color: $font-base;

                    .line {
                      position: relative;
                      top: -8px;
                      flex: 1;
                      border-bottom: 1px solid rgba(229, 229, 229, 1);
                    }

                    .text {
                      display: inline-block;
                      padding: 0 20px;
                    }
                  }

                  .item-ul {
                    display: flex;
                    flex-direction: row;
                    flex-wrap: wrap;

                    .item-li {
                      display: inline-block;
                      width: 32.3%;
                      font-size: 12px;
                      margin-bottom: 20px;
                      margin-right: 1%;
                      text-align: left;
                      cursor: pointer;

                      &.active, &:hover {
                        color: var(--mainColor);
                      }

                      &.disable, &.disable:hover {
                        color: $font-grey;
                        cursor: default;
                      }
                    }
                  }
                }
              }

              &.pic {
                .el-tabs__content {
                  padding: 0;
                  flex: 1;

                  .el-tabs__active-bar {
                    height: 0 !important;
                  }

                  .el-tab-pane {
                    padding: 15px 20px;

                    .el-tabs {
                      margin-left: -20px;
                      margin-right: -20px;

                      .el-tabs__content {
                        padding: 0;

                        .pic-item {
                          img {
                            width: 100%;
                          }
                        }
                      }
                    }

                    .pic-list {
                      display: flex;
                      flex-wrap: wrap;
                      justify-content: flex-start;
                      padding: 5px;
                      max-height: calc(100vh - 142px);
                      background-color: $bg;
                      -ms-scroll-chaining: chained;
                      -ms-overflow-style: none;
                      -ms-content-zooming: zoom;
                      -ms-scroll-rails: none;
                      -ms-content-zoom-limit-min: 100%;
                      -ms-content-zoom-limit-max: 500%;
                      -ms-scroll-snap-type: proximity;
                      -ms-scroll-snap-points-x: snapList(
                        100%,
                        200%,
                        300%,
                        400%,
                        500%
                      );
                      -ms-overflow-style: none;
                      overflow-y: scroll;
                      scrollbar-width: none;

                      &::-webkit-scrollbar {
                        width: 0;
                        height: 0;
                        background: none;
                        display: none;
                      }

                      &.-o-scrollbar {
                        -moz-appearance: none !important;
                        background: rgba(0, 255, 0, 0) !important;
                      }

                      .pic-li {
                        display: inline-block;
                        width: 30%;
                        margin: 0 1.5% 5px;
                      }

                      .pic-item {
                        position: relative;
                        width: 100%;
                        height: 140px;
                        cursor: pointer;
                        overflow: hidden;

                        .tag {
                          display: block;
                          position: absolute;
                          top: 5px;
                          left: -15px;
                          width: 60px;
                          height: 18px;
                          font-size: 12px;
                          color: $white;
                          text-align: center;
                          line-height: 18px;
                          background-color: $red;
                          transform: rotate(-45deg);
                        }

                        img {
                          position: absolute;
                          display: inline-block;
                          width: 100%;
                          height: auto;
                          top: 50%;
                          transform: translateY(-50%);
                          overflow: hidden;
                        }
                      }

                      .pic-code {
                        width: 100%;
                        height: 30px;
                        line-height: 30px;
                        text-align: center;
                        color: $font-base;
                        font-size: 12px;
                        overflow: hidden;
                        text-overflow: ellipsis;
                        white-space: nowrap;
                        cursor: pointer;
                      }
                    }
                  }

                  .el-tabs__content {
                    padding: 15px 20px;

                    .pic-list {
                      display: flex;
                      flex-wrap: wrap;
                      justify-content: flex-start;
                      padding: 5px;
                      max-height: calc(100vh - 172px);
                      background-color: $bg;

                      .pic-li {
                        display: inline-block;
                        width: 30%;
                        margin: 0 1.5% 5px;

                        .pic-item {
                          display: inline-block;
                          width: 100%;
                          cursor: pointer;
                          overflow: hidden;
                          height: auto;
                          min-height: 120px;

                          img {
                            position: absolute;
                            display: inline-block;
                            width: 100%;
                            height: auto;
                            top: 50%;
                            transform: translateY(-50%);
                            overflow: hidden;
                          }
                        }

                        .pic-code {
                          width: 100%;
                          height: 30px;
                          line-height: 30px;
                          text-align: center;
                          color: $font-base;
                          font-size: 12px;
                          overflow: hidden;
                          text-overflow: ellipsis;
                          white-space: nowrap;
                        }
                      }
                    }
                  }

                  .el-tabs__item {
                    padding: 0 15px;
                    height: 30px;
                    line-height: 30px;
                    font-size: 12px;
                  }
                }
              }
            }

            &.font {
              padding: 16px 10% 100px;
              box-sizing: border-box;
              background-color: $white;

              .title {
                display: flex;
                margin-bottom: 30px;

                .line {
                  position: relative;
                  flex: 1;
                  top: -8px;
                  border-bottom: 1px solid $border-color;
                }

                .text {
                  display: inline-block;
                  margin: 0 20px;
                  font-size: 14px;
                  color: $font-base;
                }
              }

              .font-box {
                .el-select {
                  width: 240px;

                  &.font-size {
                    width: 120px;
                    float: right;
                  }

                  .el-input__inner {
                    height: 36px;
                    line-height: 36px;
                  }
                }

                .el-textarea {
                  margin-top: 25px;

                  .el-textarea__inner {
                    height: 90px;
                    padding: 5px 12px;
                    color: $font-base;
                    background-color: $bg;
                    border: none;
                  }
                }
              }

              .color-box {
                margin-top: 100px;

                .m-colorPicker {
                  width: 100%;
                  height: 300px;

                  .colorBtn {
                    opacity: 0;
                  }

                  .box, .box.open {
                    width: 100%;
                    box-sizing: border-box;
                    opacity: 1;
                    visibility: visible;
                  }
                }
              }

              .btn-box {
                margin-top: 20px;
                text-align: center;

                .el-button {
                  width: 120px;
                  color: $white;
                  border-color: var(--mainColor);
                  background-color: var(--mainColor);

                  &:hover {
                    opacity: 0.75;
                  }
                }
              }
            }
          }
        }
      }
    }

    .right-content {
      flex: 1;
      margin-left: 20px;

      .top-box {
        display: flex;
        padding-top: 15px;
        height: 40px;
        z-index: 12;

        .img-dpi {
          display: -webkit-flex;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          margin-left: 30px;
          font-size: 0px;
          color: $font-base;
          height: 40px;

          .top {
            line-height: 20px;
          }

          .icon {
            font-size: 16px;
            margin: 0 5px;
          }

          &.el-icon-success .icon, &.el-icon-success .desc {
            color: $green;
          }

          &.el-icon-warning .icon, &.el-icon-warning .desc {
            color: $orange;
          }

          &.el-icon-remove .icon, &.el-icon-remove .desc {
            color: $red;
          }

          .text {
            display: inline-block;
            font-size: 14px;
            color: $font-base;
          }

          .desc {
            display: block;
            line-height: 20px;
            font-size: 12px;
          }
        }

        .reset {
          display: inline-block;
          font-size: 12px;
          color: $font-base;
          line-height: 34px;
          cursor: pointer;

          &.hasDes {
            line-height: 17px;
          }

          .icon {
            margin-right: 6px;
          }
        }
      }

      .main-content {
        position: relative;
        width: 100%;
        height: calc(100vh - 118px);
        float: none;
        text-align: center;

        &::after {
          content: '';
          position: absolute;
          top: 50%;
          right: 0;
          transform: translateY(-50%);
          width: 0;
          height: 0;
          border-top: 400px solid transparent;
          border-right: 600px solid var(--mainColor);
          border-bottom: 400px solid transparent;
          opacity: 0.11;
          z-index: 0;
        }

        &::before {
          content: 'WEBDIYCUSTOM';
          position: absolute;
          top: 50%;
          left: 50%;
          transform: translate3D(-50%, -50%, 0);
          font-size: 5vw;
          color: var(--mainColor);
          opacity: 0.3;
          z-index: 1;
        }

        .bg-img {
          position: absolute;
          display: inline-block;
          width: 252px;
          height: 496px;
          top: 100px;
          left: 50%;
          transform: translateX(-50%);
          z-index: 2;
        }

        .canvas {
          position: absolute;
          top: 0;
          left: 50%;
          transform: translateX(-50%);
          z-index: 5;
        }

        .shade-box {
          position: absolute;
          top: -100px;
          left: 0;
          right: 0;
          bottom: 0;
          width: 252px;
          height: 496px;
          border-radius: 30px;
          margin: auto;
          box-shadow: 0 0 0 9999px rgba(241, 243, 249, 0.8);
          z-index: 8;
        }

        .up-box {
          position: absolute;
          top: 0;
          left: 0;
          right: 0;
          max-height: calc(100vh - 172px);
          background-size: 252px 496px;
          background-repeat: no-repeat;
          background-position: 50% 100px;
          background-clip: content-box;
          z-index: 10;

          .icon {
            position: absolute;
            top: -10px;
            left: -10px;
            font-size: 18px;
            color: $white;
            background-color: red;
            border-radius: 50%;
          }
        }
      }
    }
  }
}

.diy-css {
  padding-left: 20px;
  overflow: hidden;

  .left-img {
    float: left;
    margin-right: 20px;
    width: 130px;
    height: 130px;

    .diy-images {
      display: flex;
      align-items: center;
      justify-content: center;

      img {
        height: 130px;
      }
    }
  }

  .right-cons {
    float: left;
    padding-top: 10px;

    .item {
      width: 300px;
      margin-bottom: 20px;
    }

    .item-label {
      width: 100px;
      margin-right: 20px;
    }
  }
}

p.tips {
  padding: 10px 0;
  color: $red;
  text-align: center;
}
</style>
