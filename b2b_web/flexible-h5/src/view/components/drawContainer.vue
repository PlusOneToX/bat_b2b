<!--
 * @Author: yaowei
 * @Date: 2019-12-22 16:36:50
 * @LastEditors: yaowei
 * @LastEditTime: 2019-12-08 10:02:46
-->
<template>
  <div>
    <div class="canvas-wrapper" v-show="!showTemplate">
      <!-- 框图 -->
      <img
        :src="bgimg"
        alt
        class="bgimg"
        id="bgimg"
        :width="pwidth"
        :height="pheight"
      />
      <canvas
        id="myCanvas"
        class="canvas"
        :class="{
          module: curPicType === 3,
          show: showDashBorder && curPicType !== 3 && this.dragArr.length > 0,
        }"
      ></canvas>
      <canvas id="upCanvas" class="upcanvas"></canvas>
      <!-- 虚线框 -->
      <div
        v-show="showDashBorder && curPicType !== 3 && this.dragArr.length > 0"
        class="dash-border"
        :style="{
          width: pwidth + 'px',
          height: pheight + 'px',
        }"
      ></div>
      <!-- 框图 -->
      <div
        class="up-box"
        ref="wrapCanvas"
        :style="{
          backgroundImage: 'url(' + upimg + ')',
          backgroundSize: pwidth + 'px ' + pheight + 'px',
          height: wheight + 'px',
        }"
        @touchstart="start($event)"
        @touchmove="move($event)"
        @touchend="end($event)"
      >
        <!-- 可点击热区 -->
        <template v-if="Number(curPicType) === 3 && clickedArea.length > 0">
          <div
            @click.stop="handlePic($event, item)"
            class="clicked-area"
            v-for="(item, index) in clickedArea"
            :key="index"
            :style="{
              top: item.y1 + 'px',
              left: curLeft + item.x1 + 'px',
              width: item.x2 - item.x1 + 'px',
              height: item.y2 - item.y1 + 'px',
            }"
          >
            <span
              class="sprite-icon icon_picture"
              :class="{ none: item.url !== '' }"
            ></span>
            <div class="tips" :class="{ show: hasClickedArea }">
              <p @click="handleReplace()">
                <span class="sprite-icon icon_tihuan"></span>换图
              </p>
              <p @click="goEdit(item)">
                <span class="sprite-icon icon_bianji"></span>编辑
              </p>
            </div>
          </div>
        </template>
      </div>

      <p
        class="move-tips"
        v-show="showDashBorder && curPicType !== 3 && this.dragArr.length > 0"
      >
        提示：请确保图片铺满红色虚线框，
        <br />图片未铺满区域不打印
      </p>

      <template v-if="Number(curPicType) === 1">
        <!-- 图层图标 -->
        <div
          class="layer-icon"
          v-show="!showHandleWrap && dragArr && dragArr.length > 0"
          @click="handleShowWrap"
        >
          <span class="sprite-icon icon_layer"></span>
        </div>
        <!-- 图层列表 -->
        <div
          class="handle-layer"
          :class="{ show: showHandleWrap && dragArr && dragArr.length > 0 }"
        >
          <div class="close-handle" @click="handleCloseWrap">
            <van-icon name="cross" />
          </div>
          <div
            class="handle-list"
            v-for="(item, index) in reverseData"
            :key="index"
          >
            <van-icon
              class="close-btn"
              name="clear"
              @click="handleDelete(item)"
            />
            <template v-if="item.type === 1">
              <span class="type" v-if="item.cateType === 1">本地</span>
              <span class="type" v-else-if="item.cateType === 2">贴图</span>
              <span class="type" v-else>素材</span>
              <div class="img"><img :src="item.url.src" alt="" /></div>
            </template>
            <template v-else>
              <span class="type">文字</span>
              <span class="text">{{ item.filltext }}</span>
            </template>
            <div class="move" @click="handleMove(item, 'up')">
              <span
                class="sprite-icon layer_icon_Moveup"
                v-show="item.index !== dragArr.length - 1"
              ></span>
            </div>
            <div class="move" @click="handleMove(item, 'down')">
              <span
                class="sprite-icon layer_icon_Movedown"
                v-show="item.index !== 0"
              ></span>
            </div>
          </div>
        </div>
      </template>
    </div>

    <editKonva
      v-if="showTemplate"
      :pwidth="pwidth"
      :pheight="pheight"
      :curItem="curItem"
      :wheight="wheight"
    ></editKonva>
  </div>
</template>
<script type="text/ecmascript-6">
import editKonva from "./editTemplate.vue";
import api from "common/js/allApi.js";
import { getLocalStorageItem } from "common/js/common";
var WIDTH = 0; // myCanvas画布宽
var HEIGHT = 0; // myCanvas画布高
var TOPVALUE = 0; // 距离顶部距离
var LEFTVALUE = 0; // 距离左侧距离
var PWIDTH = 0; // 手机模型宽
var PHEIGHT = 0; // 手机模型高
var c2, ctx2, c, ctx;
var delIcon = new Image();
var scaleIcon = new Image();
var bimg = new Image();
var obj = {}; // 定义一个对象
var istouch = false;
var canvasRatio = 1;
var modelCenter = {}; // 模板中心点位置，用于计算画布可点击区域
var modelArea = []; // 模板热区位置，用于计算画布可点击区域
class DragImg {
  constructor(props, canvas) {
    this.x = props.x;
    this.y = props.y;
    this.ux = props.ux;
    this.uy = props.uy;
    this.w = props.width;
    this.h = props.height;
    this.initw = props.initW;
    this.inith = props.initH;
    this.minW = props.minW;
    this.minH = props.minH;
    this.url = props.url;
    this.fontsize = props.fontsize;
    this.fontfamily = props.fontfamily;
    this.fillstyle = props.fillstyle;
    this.filltext = props.filltext;
    this.type = props.type;
    this.picType = props.picType;
    this.ctx = canvas;
    this.rotate = props.rotate;
    this.selected = props.selected;
    this.index = props.index;
    this.picId = props.picId;
    this.cateType = props.cateType;
    this.hotClick = props.hotClick;
    this.scale = props.scale;
  }
  async paint() {
    ctx.save();
    ctx2.save();
    this.centerX = this.x * canvasRatio + (this.w * canvasRatio) / 2;
    this.centerY = this.y * canvasRatio + (this.h * canvasRatio) / 2;
    // 旋转元素
    ctx.translate(this.centerX, this.centerY);
    ctx.rotate((this.rotate * Math.PI) / 180);
    ctx.translate(-this.centerX, -this.centerY);
    // 显示壁纸层
    let c3 = document.createElement("canvas");
    let ctx3 = c3.getContext("2d");
    let cwidth = WIDTH * canvasRatio;
    let cheight = HEIGHT * canvasRatio;
    c3.width = cwidth;
    c3.height = cheight;
    c3.style.opacity = 0;
    c3.style.width = WIDTH + "px";
    c3.style.height = HEIGHT + "px";
    let centeruX = this.ux * canvasRatio + (this.w * canvasRatio) / 2;
    let centeruY = this.uy * canvasRatio + (this.h * canvasRatio) / 2;
    // 渲染元素 1-图片 2-文字
    if (this.type === 1) {
      let imgWidth = this.w * canvasRatio;
      let imgheight = this.h * canvasRatio;
      ctx.drawImage(
        this.url,
        this.x * canvasRatio,
        this.y * canvasRatio,
        imgWidth,
        imgheight
      );
      // 添加显示层
      ctx3.translate(centeruX, centeruY);
      ctx3.rotate((this.rotate * Math.PI) / 180);
      ctx3.translate(-centeruX, -centeruY);
      ctx3.drawImage(
        this.url,
        this.ux * canvasRatio,
        this.uy * canvasRatio,
        imgWidth,
        imgheight
      );
      // 显示层图标和虚线
      this.drawIcon(this, ctx3);
      let pat = ctx2.createPattern(c3, "no-repeat");
      ctx2.fillStyle = pat;
      ctx2.globalCompositeOperation = "source-atop";

      ctx2.fillRect(0, 0, PWIDTH * canvasRatio, PHEIGHT * canvasRatio);
      ctx3.clearRect(0, 0, c3.width, c3.height);
    } else if (this.type === 2) {
      this.fontsize = this.h * canvasRatio;
      ctx.font = this.fontsize + "px " + this.fontfamily;
      ctx.fillStyle = this.fillstyle;
      ctx.textBaseline = "top";
      ctx.textAlign = "left";
      ctx.fillText(this.filltext, this.x * canvasRatio, this.y * canvasRatio);
      this.w = ctx.measureText(this.filltext).width / canvasRatio;

      // 添加显示层
      ctx2.translate(centeruX, centeruY);
      ctx2.rotate((this.rotate * Math.PI) / 180);
      ctx2.translate(-centeruX, -centeruY);
      ctx2.textBaseline = "top";
      ctx2.textAlign = "left";
      ctx2.font = this.fontsize + "px " + this.fontfamily;
      ctx2.fillStyle = this.fillstyle;
      ctx2.fillText(
        this.filltext,
        this.ux * canvasRatio,
        this.uy * canvasRatio
      );
      ctx2.globalCompositeOperation = "source-atop";
      // 显示层绘制图标和虚线
      this.drawIcon(this, ctx2);
    }
    // 如果是选中状态，绘制选择虚线框，和缩放图标、删除图标
    this.drawIcon(this, null);
    c.style.zIndex = "5";
    c.style.opacity = 1;
    ctx.restore();
    ctx2.restore();
  }
  // 绘制IP图/模板图
  paintIpImage() {
    ctx.save();
    ctx2.save();
    this.centerX = this.x + this.w / 2;
    this.centerY = this.y + this.h / 2;
    // 旋转元素
    ctx.translate(this.centerX, this.centerY);
    ctx.rotate((this.rotate * Math.PI) / 180);
    ctx.translate(-this.centerX, -this.centerY);
    // 显示壁纸层
    let c3 = document.createElement("canvas");
    let ctx3 = c3.getContext("2d");
    let cwidth = WIDTH * canvasRatio;
    let cheight = HEIGHT * canvasRatio;
    c3.width = cwidth;
    c3.height = cheight;
    c3.style.opacity = 0;
    c3.style.width = WIDTH + "px";
    c3.style.height = HEIGHT + "px";
    let centeruX = this.ux + this.w / 2;
    let centeruY = this.uy + this.h / 2;
    let imgWidth = this.w * canvasRatio;
    let imgheight = this.h * canvasRatio;

    if (this.hotClick && this.hotClick.length > 0) {
      modelArea = [];
      // 图片中心点坐标
      modelCenter = {
        x: imgWidth / 2 + this.ux * canvasRatio, // 图片缩放后的中心点x（以图片缩放左上角为原点）
        y: imgheight / 2 + this.uy * canvasRatio, // 图片缩放后的中心点y（以图片缩放左上角为原点）
        ux: (imgWidth / 2 + this.ux * canvasRatio) / canvasRatio - this.ux, // 中心点x（以底图左上角为原点）
        uy: (imgheight / 2 + this.uy * canvasRatio) / canvasRatio - this.uy, // 中心点y（以底图左上角为原点）
      };
      // 设置热区位置（图片缩放后）
      this.hotClick.forEach((area) => {
        let option = {
          // 设置显示热区
          x: modelCenter.x + area.hotX * canvasRatio, // 图片缩放后热区左上角x
          y: modelCenter.y + area.hotY * canvasRatio, // 图片缩放后热区左上角y
          width: area.hotW * canvasRatio, // 图片缩放后热区宽度
          height: area.hotH * canvasRatio, // 图片缩放后热区高度
          // 设置点击热区
          ux1: modelCenter.ux + area.hotX, // 可点击起始范围x（原始大小）
          uy1: modelCenter.uy + area.hotY, // 可点击起始范围y（原始大小）
          ux2: modelCenter.ux + area.hotX + area.hotW, // 可点击终点范围x（原始大小）
          uy2: modelCenter.uy + area.hotY + area.hotH, // 可点击终点范围y（原始大小）
          imgWidth: this.w,
          imgHeight: this.h,
          imgX: this.x,
          imgY: this.y,
          frame: area.frame,
        };
        modelArea.push(option);
      });
    }
    // 渲染元素 1-图片 2-文字
    if (this.type === 1) {
      ctx.drawImage(
        this.url,
        this.x * canvasRatio,
        this.y * canvasRatio,
        imgWidth,
        imgheight
      );
      // 添加显示层
      ctx3.translate(centeruX, centeruY);
      ctx3.rotate((this.rotate * Math.PI) / 180);
      ctx3.translate(-centeruX, -centeruY);
      ctx3.drawImage(
        this.url,
        this.ux * canvasRatio,
        this.uy * canvasRatio,
        imgWidth,
        imgheight
      );

      let pat = ctx2.createPattern(c3, "no-repeat");
      ctx2.fillStyle = pat;
      ctx2.globalCompositeOperation = "source-atop";
      ctx2.fillRect(0, 0, PWIDTH * canvasRatio, PHEIGHT * canvasRatio);
      ctx3.clearRect(0, 0, c3.width, c3.height);
    }
    c.style.zIndex = "-1";
    c.style.opacity = 0;
    ctx.restore();
    ctx2.restore();
  }
  isInWhere(x, y) {
    // 变换区域左上角的坐标和区域的高度宽度
    const transformW = 24;
    const transformH = 24;
    let transformX = this.x + this.w;
    let transformY = this.y + this.h;
    this.centerX = this.centerX / canvasRatio;
    this.centerY = this.centerY / canvasRatio;
    const transformAngle =
      (Math.atan2(transformY - this.centerY, transformX - this.centerX) /
        Math.PI) *
        180 +
      this.rotate;
    const transformXY = this.getTransform(
      transformX,
      transformY,
      transformAngle
    );
    transformX = transformXY.x;
    transformY = transformXY.y;
    // 删除区域左上角的坐标和区域的高度宽度
    const delW = 24;
    const delH = 24;
    let delX = this.x;
    let delY = this.y;
    const delAngle =
      (Math.atan2(delY - this.centerY, delX - this.centerX) / Math.PI) * 180 +
      this.rotate;
    const delXY = this.getTransform(delX, delY, delAngle);
    delX = delXY.x;
    delY = delXY.y;

    // 右上
    let rightX = this.x + this.w;
    let rightY = this.y;
    let rightAngle =
      (Math.atan2(rightY - this.centerY, rightX - this.centerX) / Math.PI) *
        180 +
      this.rotate;
    let rightXY = this.getTransform(rightX, rightY, rightAngle, 0, 0);
    rightX = rightXY.x;
    rightY = rightXY.y;
    // 左下
    let leftdX = this.x;
    let leftdY = this.y + this.h;
    let leftdAngle =
      (Math.atan2(leftdY - this.centerY, leftdX - this.centerX) / Math.PI) *
        180 +
      this.rotate;
    let leftdXY = this.getTransform(leftdX, leftdY, leftdAngle, 0, 0);
    leftdX = leftdXY.x;
    leftdY = leftdXY.y;

    // 移动区域的坐标
    let v1 = [delX + 12 - x, delY + 12 - y];
    let v2 = [rightX - x, rightY - y];
    let v3 = [leftdX - x, leftdY - y];
    let v4 = [transformX + 12 - x, transformY + 12 - y];

    if (
      x - transformX >= 0 &&
      y - transformY >= 0 &&
      transformX + transformW - x >= 0 &&
      transformY + transformH - y >= 0
    ) {
      // 缩放区域
      return "transform";
    } else if (
      x - delX >= 0 &&
      y - delY >= 0 &&
      delX + delW - x >= 0 &&
      delY + delH - y >= 0
    ) {
      // 删除区域
      return "del";
    } else if (
      v1[0] * v2[1] - v2[0] * v1[1] > 0 &&
      v2[0] * v4[1] - v4[0] * v2[1] > 0 &&
      v4[0] * v3[1] - v3[0] * v4[1] > 0 &&
      v3[0] * v1[1] - v1[0] * v3[1] > 0
    ) {
      // 移动区域
      return "move";
    }
    // 不在选择区域里面
    return false;
  }
  getTransform(x, y, rotate) {
    // 将角度化为弧度
    var angle = (Math.PI / 180) * rotate;
    // 初始坐标与中点形成的直线长度不管怎么旋转都是不会变的，用勾股定理求出然后将其作为斜边
    var r = Math.sqrt(
      Math.pow(x - this.centerX, 2) + Math.pow(y - this.centerY, 2)
    );
    // 斜边乘sin值等于即可求出y坐标
    var a = Math.sin(angle) * r;
    // 斜边乘cos值等于即可求出x坐标
    var b = Math.cos(angle) * r;
    // 目前的xy坐标是相对于图片中点为原点的坐标轴，而我们的主坐标轴是canvas的坐标轴，所以要加上中点的坐标值才是标准的canvas坐标
    return {
      x: this.centerX + b - 12,
      y: this.centerY + a - 12,
    };
  }
  // 绘制图标和虚线
  drawIcon(sprite, ct) {
    let x, y, ctx;
    if (sprite.selected) {
      if (ct === null) {
        x = sprite.x * canvasRatio;
        y = sprite.y * canvasRatio;
        ctx = sprite.ctx;
      } else {
        x = sprite.ux * canvasRatio;
        y = sprite.uy * canvasRatio;
        ctx = ct;
      }
      let swidth = sprite.w * canvasRatio;
      let sheight = sprite.h * canvasRatio;
      let radius = 12 * canvasRatio;
      let iconWidth = 24 * canvasRatio;

      ctx.setLineDash([2, 5]);
      ctx.lineWidth = 2;
      ctx.strokeStyle = "#999999";
      ctx.lineDashOffset = 10;
      ctx.strokeRect(x, y, swidth, sheight);
      ctx.drawImage(delIcon, x - radius, y - radius, iconWidth, iconWidth);
      ctx.drawImage(
        scaleIcon,
        x + swidth - radius,
        y + sheight - radius,
        iconWidth,
        iconWidth
      );
    }
  }
}
export default {
  name: "canvas-drag",
  components: {
    editKonva,
  },
  props: {
    // 添加的定制数组（图片，文字）
    spriteArr: {
      type: Array,
      default() {
        return [];
      },
    },
    // 上传文件类型
    manufactor: {
      type: String,
      default: "",
    },
    // 手机底图materialsColorValue
    materialsColorValue: {
      type: String,
      default: "",
    },
    materialsType: {
      type: Number,
      default: 0,
    },
    bgimg: {
      type: String,
      default: "",
    },
    // 手机框图
    upimg: {
      type: String,
      default: "",
    },
    // 图片宽px
    pwidth: {
      type: Number,
      default: 0,
    },
    // 图片高px
    pheight: {
      type: Number,
      default: 0,
    },
    // 图片宽px，加2mm后
    pwidth2: {
      type: Number,
      default: 0,
    },
    // 图片高px，加2mm后
    pheight2: {
      type: Number,
      default: 0,
    },
    // 画布宽
    wwidth: {
      type: Number,
      default: 0,
    },
    // 画布高
    wheight: {
      type: Number,
      default: 0,
    },
    // 图片宽mm,2mm后
    mw: {
      type: Number,
      default: 0,
    },
    // 图片高mm,2mm后
    mh: {
      type: Number,
      default: 0,
    },
    // 图片宽mm
    nw: {
      type: Number,
      default: 0,
    },
    // 图片高mm
    nh: {
      type: Number,
      default: 0,
    },
    // 删除图标
    delIcon: {
      type: String,
      default: "",
    },
    // 缩放图标
    scaleIcon: {
      type: String,
      default: "",
    },
    // 分销商
    distributor: {
      type: String,
      default: "",
    },
    // 图库类型
    curPicType: {
      type: Number,
      default: 0,
    },
  },
  data() {
    return {
      userId: "",
      ctx: null,
      dragArr: [], // 图形数组
      clickedkArr: [], // 选中
      c: null,
      lastImg: null,
      initial: null,
      startTouch: null,
      page: null,
      diffX: 0,
      diffY: 0,
      scale: 0,
      difScale: 0,
      imgDpi: 0,
      ratio: 0,
      ratio2: 0,
      isDown: false,
      isDrag: false, // 图片类型是否可编
      naturalWidth: 0, // 图片原像素
      naturalHeight: 0, // 图片原像素
      naturalSize: 0, // 计算图片Dpi的尺寸
      dpiType: 0, // type = 1 情况按宽计算，type = 2 情况按长计算
      hostname: "", // oss 域名
      showHandleWrap: false, // 是否显示图层列表
      clickedArea: [], // 点击热区
      clickedNum: 0, // 点击次数，用于判断单击/双击
      clickedTimer: null,
      curClicked: "", // 当前点击热区
      showTemplate: false, // 是否显示编辑模板
      curItem: {}, // 当前点击热区信息
      curLeft: 0, // 素材显示/隐藏 left 值
      hasClickedArea: false, // 是否点击热区，用于显示“双击进入编辑”提示语
      reverseData: [], // 图层列表（倒序显示）
      showDashBorder: false, // 是否显示虚线框
      curTimeout: "", // 编辑器隐藏定时器
    };
  },
  mounted() {
    // 画布大小
    WIDTH = window.screen.width;
    HEIGHT = this.wheight;
    TOPVALUE = this.$parent.headerH;

    delIcon.src = this.delIcon;
    scaleIcon.src = this.scaleIcon;
    this.userId = getLocalStorageItem("userId");

    // 获取当前的屏幕像素密度比例
    canvasRatio = this.getPixelRatio();
  },
  activated() {
    // 画布大小
    WIDTH = window.screen.width;
    HEIGHT = this.wheight;
    TOPVALUE = this.$parent.headerH;
  },
  methods: {
    draw() {
      this.clear();
      if (this.dragArr.length > 0) {
        ctx2.save();
        PWIDTH = this.pwidth;
        PHEIGHT = this.pheight;
        bimg = document.getElementById("bgimg");
        ctx2.drawImage(bimg, 0, 0, PWIDTH * canvasRatio, PHEIGHT * canvasRatio);
        this.dragArr.forEach((item) => {
          if (item.picType === 3 && item.type === 1) {
            // 模板-图片
            item.paintIpImage();
          } else {
            // 自定义/模板-文字/贴图
            item.paint();
          }
        });
      }
    },
    drawIpImage() {
      this.clear();
      ctx2.save();
      PWIDTH = this.pwidth;
      PHEIGHT = this.pheight;
      bimg = document.getElementById("bgimg");
      ctx2.drawImage(bimg, 0, 0, PWIDTH * canvasRatio, PHEIGHT * canvasRatio);
      this.dragArr.forEach((item) => {
        item.paintIpImage();
      });
    },
    start(e) {
      // e.preventDefault();
      if (this.isDrag && this.dragArr.length > 0) {
        this.showDashBorder = true;
        clearTimeout(this.curTimeout);
        this.isDown = true;
        // 初始化一个数组用于存放所有被点击到的图片对象
        this.clickedkArr = [];
        let x, y;
        if (e === null || e.length === 0) return;
        if (e.touches.length === 1) {
          x = e.touches[0].pageX;
          y = e.touches[0].pageY;
        } else if (e.touches.length >= 2) {
          // 两指操作
          istouch = true;
          this.page = e.touches; // 得到第一组两个点
          x = e.touches[0].pageX;
          y = e.touches[0].pageY;
        }

        this.dragArr.forEach((item, index) => {
          // x轴需去除素材显示后左侧的left值（定位LEFTVALUE），除2是因为画布定位居中，不然位置会偏移
          // y轴需去除顶部栏高（定位TOPVALUE），不然位置会偏移
          let place = item.isInWhere(x - LEFTVALUE / 2, y - TOPVALUE);
          if (!(item.picType === 3 && item.type === 1)) {
            item.place = place;
            item.index = index;
            // 先将所有的item的selected变为flase
            item.selected = false;
            if (place) {
              this.clickedkArr.push(item);
            } else {
              // 未选中
              this.$emit("clear", 0);
            }
          }
        });
        let length = this.clickedkArr.length;
        if (length) {
          // cavans绘制的图片的层级是越来越高的，因此我们取这个数组的最后一项，保证取到的图片实例是层级最高的
          let lastImg = this.clickedkArr[length - 1];
          let type = this.dragArr[lastImg.index].type;
          // 将该实例的被选值设为true，下次重新绘制将绘制边框
          lastImg.selected = true;
          // 保存这个选中的实例
          this.lastImg = lastImg;
          // 如果是删除的话就移除
          if (lastImg.place === "del") {
            this.$emit("clear", type);
            this.dragArr.splice(lastImg.index, 1);
            // 重新绘制
            this.draw();
            return;
          }
          if (this.lastImg.type === 1) {
            this.getImgDPI(lastImg);
          }
          // 保存这个实例的初始值，以后会用上
          this.initial = {
            initialX: lastImg.x,
            initialY: lastImg.y,
            initialH: lastImg.h,
            initialW: lastImg.w,
            initialuX: lastImg.ux,
            initialuY: lastImg.uy,
            initialRotate: lastImg.rotate,
          };
        } else {
          this.$emit("clear", 0);
        }
        // 重新绘制
        this.draw();
        // 保存点击的坐标，move时要用
        this.startTouch = {
          pageX: x,
          pageY: y,
        };
      }
    },
    move(e) {
      // e.preventDefault();
      if (this.isDown && this.isDrag) {
        this.showDashBorder = true;
        clearTimeout(this.curTimeout);
        let x, y;
        let now = [];
        let start = [];
        if (e === null || e.length === 0) return;
        let startX, startY;
        let lastImg = this.lastImg;
        if (e.touches.length === 1 && lastImg) {
          x = e.touches[0].pageX;
          y = e.touches[0].pageY;
          let pageX = lastImg.centerX / canvasRatio;
          let pageY = lastImg.centerY / canvasRatio;
          startX = this.startTouch.pageX;
          startY = this.startTouch.pageY;
          now[0] = e.touches[0];
          now[1] = {
            pageX,
            pageY,
          };
          start[0] = this.startTouch;
          start[1] = {
            pageX,
            pageY,
          };
        } else if (e.touches.length >= 2 && istouch && lastImg) {
          // 两指操作, 得到第一组两个点
          startX = this.page[0].pageX;
          startY = this.page[0].pageY;
          now[0] = e.touches[0];
          now[1] = e.touches[1];
          start[0] = this.page[0];
          start[1] = this.page[1];
        }
        if (this.initial) {
          let { initialX, initialY, initialuX, initialuY } = this.initial;
          if (this.clickedkArr.length) {
            // 是否强制铺满血位
            let isAllPlace = parseInt(sessionStorage.getItem("isAllPlace"));

            if (this.lastImg.place === "move" && e.touches.length === 1) {
              if (
                Number(lastImg.type) === 1 &&
                Number(lastImg.picType) !== 4 &&
                isAllPlace
              ) {
                // 强制铺满，限制移动范围
                let touchEvent = {
                  x: x,
                  y: y,
                };
                let startTouch = {
                  startX: startX,
                  startY: startY,
                };
                this.limitMove(touchEvent, this.initial, startTouch, lastImg);
              } else {
                lastImg.x = initialX + (x - startX);
                lastImg.ux = initialuX + (x - startX);
                lastImg.y = initialY + (y - startY);
                lastImg.uy = initialuY + (y - startY);
              }
            }
            if (
              this.lastImg.place === "transform" ||
              (e.touches.length >= 2 && istouch)
            ) {
              // 旋转（type: 2 文字，强制铺满血位并且type 1/picType 4 贴图，不强制铺满血位）
              if (
                Number(lastImg.type) === 2 ||
                !isAllPlace ||
                (isAllPlace &&
                  Number(lastImg.type) === 1 &&
                  Number(lastImg.picType) === 4)
              ) {
                // 旋转部分
                let { initialRotate } = this.initial;
                // 旋转的角度
                let rotation =
                  this.getAngle(now[0], now[1]) -
                  this.getAngle(start[0], start[1]);
                lastImg.rotate = initialRotate + rotation;
              }

              // 缩放部分
              let touchEvent = {
                x: now[0],
                y: now[1],
              };
              let startTouch = {
                startX: start[0],
                startY: start[1],
              };
              this.limitScale(touchEvent, this.initial, startTouch, lastImg);

              if (this.lastImg.type === 1) {
                this.getImgDPI(lastImg);
              }
            }
            this.draw();
          }
        }
      }
    },
    end(e) {
      // e.preventDefault();
      this.curTimeout = setTimeout(() => {
        this.showDashBorder = false;
      }, 3000);
      this.isDown = false;
      if (this.isDrag) {
        if (istouch) {
          istouch = false;
        }
        if (
          this.lastImg &&
          this.lastImg.selected &&
          this.lastImg.place !== "del"
        ) {
          let selIndex = this.lastImg.type === 1 ? null : this.lastImg.index;
          this.$emit("select", this.lastImg, selIndex);
        }
      }
    },
    getDistance(p1, p2) {
      let x = p2.pageX - p1.pageX;
      let y = p2.pageY - p1.pageY;
      return Math.sqrt(x * x + y * y);
    },
    getAngle(p1, p2) {
      let x = p1.pageX - p2.pageX;
      let y = p1.pageY - p2.pageY;
      return (Math.atan2(y, x) * 180) / Math.PI;
    },
    // 获取坐标
    normalizeTouchEvent(e) {
      let el = this.$refs.wrapCanvas;
      if (e === null || e.length === 0) return;
      let x = e.touches[0].clientX;
      let y = e.touches[0].clientY;
      // 判断是否是双指操作
      if (e.touches.length >= 2) {
        istouch = true;
        obj.getsturestart && obj.gesturestart.call(el);
      }
      return {
        x,
        y,
      };
    },
    // 删除图片
    deletePic() {
      if (this.dragArr.length > 0 && this.dragArr[0].type === 1) {
        this.dragArr.splice(0, 1);
        if (this.dragArr.length > 0) {
          this.draw();
        } else {
          // 清空canvas
          this.clear();
        }
      }
    },
    // 定制
    make() {
      this.$store.state.user.spriteArr = this.dragArr;
      if (this.dragArr.length > 0) {
        // 去除图标和虚线
        this.dragArr.forEach((item) => {
          if (item.selected) {
            item.selected = false;
          }
          this.isDrag ? item.paint() : item.paintIpImage();
        });

        // 剪切图片
        let cutImage = ctx.getImageData(
          c.width / 2 - (this.pwidth * canvasRatio) / 2,
          c.height / 2 - (this.pheight * canvasRatio) / 2,
          this.pwidth * canvasRatio,
          this.pheight * canvasRatio
        );
        let testC = document.createElement("canvas");
        let testCtx = testC.getContext("2d");
        testC.width = PWIDTH * canvasRatio;
        testC.height = PHEIGHT * canvasRatio;

        // 将截图内容转化base64
        testCtx.putImageData(
          cutImage,
          0,
          0,
          0,
          0,
          PWIDTH * canvasRatio,
          PHEIGHT * canvasRatio
        );
        let newImage = testC.toDataURL("image/png", 0.8);
        testCtx.clearRect(0, 0, PWIDTH * canvasRatio, PHEIGHT * canvasRatio);

        // 合并并导出文件
        this.compoundImg(newImage);
      } else {
        this.$message.warning("请选择图片！");
      }
    },
    compoundImg(newImage) {
      // 新的canvas存储壁纸
      let nCanvas = document.createElement("canvas");
      let nCtx = nCanvas.getContext("2d");
      let nwidth = PWIDTH * canvasRatio;
      let nheight = PHEIGHT * canvasRatio;
      nCanvas.width = nwidth;
      nCanvas.height = nheight;
      Promise.all([
        this.loadImage(this.bgimg, 0),
        this.loadImage(newImage, 1),
        this.loadImage(this.upimg, 2),
      ]).then((imgs) => {
        nCtx.drawImage(imgs[0], 0, 0, nwidth, nheight);
        // 图片背景
        let scanvas = document.createElement("canvas");
        let sctx = scanvas.getContext("2d");
        scanvas.width = nwidth;
        scanvas.height = nheight;
        scanvas.style.opacity = 0;
        sctx.drawImage(imgs[1], 0, 0, nwidth, nheight);
        let pat = nCtx.createPattern(scanvas, "no-repeat");
        nCtx.fillStyle = pat;
        nCtx.globalCompositeOperation = "source-atop";
        nCtx.fillRect(0, 0, nwidth, nheight);
        nCtx.drawImage(imgs[2], 0, 0, nwidth, nheight);
        let image = this.convertCanvasToImage(nCanvas);
        sctx.clearRect(0, 0, nwidth, nheight);
        nCtx.clearRect(0, 0, nwidth, nheight);

        if (Number(this.materialsType) === 2 && this.manufactor === "MK") {
          this.getCutImg(image);
        } else {
          let pdfImg = this.getCutImg(); // 剪切图
          this.importFile(image, pdfImg);
        }
      });
    },
    // 颜色转换
    colorRgba(str) {
      // 十六进制颜色值的正则表达式
      var reg = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/;
      var sColor = str.toLowerCase();
      // 十六进制颜色转换为RGB格式
      if (sColor && reg.test(sColor)) {
        if (sColor.length === 4) {
          var sColorNew = "#";
          for (let i = 1; i < 4; i += 1) {
            // 例如：#eee,#fff等
            sColorNew += sColor.slice(i, i + 1).concat(sColor.slice(i, i + 1));
          }
          sColor = sColorNew;
        }
        // 处理六位颜色值
        var sColorChange = [];
        for (let i = 1; i < 7; i += 2) {
          sColorChange.push(parseInt("0x" + sColor.slice(i, i + 2)));
        }
        return sColorChange;
      } else {
        return [0, 0, 0];
      }
    },
    // 合成生产图
    compoundPdf(newImage, imgUrl) {
      let nCanvas = document.createElement("canvas");
      let nCtx = nCanvas.getContext("2d");

      // 透明边距
      let frameValue = JSON.parse(sessionStorage.getItem("frameValue"));
      let vFrame = 0;
      let hFrame = 0;
      if (frameValue) {
        vFrame = Number(frameValue.leftFrame) + Number(frameValue.rightFrame);
        hFrame = Number(frameValue.topFrame) + Number(frameValue.underFrame);
      }
      let cX = (Number(frameValue.leftFrame) / 25.4) * this.imgDpi;
      let cY = (Number(frameValue.topFrame) / 25.4) * this.imgDpi;
      let cwidth = ((this.mw - vFrame) / 25.4) * this.imgDpi;
      let cheight = ((this.mh - hFrame) / 25.4) * this.imgDpi;

      let nwidth = (this.mw / 25.4) * this.imgDpi;
      let nheight = (this.mh / 25.4) * this.imgDpi;
      nCanvas.width = nwidth;
      nCanvas.height = nheight;

      Promise.all([
        this.loadImage(this.bgimg, 0),
        this.loadImage(newImage, 1),
      ]).then((imgs) => {
        // 底图（TPU圆角）
        nCtx.drawImage(imgs[0], cX, cY, cwidth, cheight);
        // 切图
        let scanvas = document.createElement("canvas");
        let sctx = scanvas.getContext("2d");
        scanvas.width = nwidth;
        scanvas.height = nheight;
        scanvas.style.opacity = 0;
        sctx.drawImage(imgs[1], 0, 0, nwidth, nheight);
        let pat = nCtx.createPattern(scanvas, "no-repeat");
        nCtx.fillStyle = pat;
        nCtx.globalCompositeOperation = "source-in";
        nCtx.fillRect(0, 0, nwidth, nheight);

        let cutImage = nCtx.getImageData(0, 0, nwidth, nheight);
        // 将canvas的透明背景设置相应颜色
        if (this.materialsColorValue) {
          let colorArr = this.colorRgba(this.materialsColorValue);
          for (let i = 0; i < cutImage.data.length; i += 4) {
            // 当该像素是透明的,则设置成白色
            if (cutImage.data[i + 3] === 0) {
              cutImage.data[i] = colorArr[0];
              cutImage.data[i + 1] = colorArr[1];
              cutImage.data[i + 2] = colorArr[2];
              cutImage.data[i + 3] = 255;
            }
          }
        }

        let tC = document.createElement("canvas");
        let tCtx = tC.getContext("2d");
        tC.width = (this.mw / 25.4) * this.imgDpi;
        tC.height = (this.mh / 25.4) * this.imgDpi;
        let difX = (Number(frameValue.leftFrame) / 25.4) * this.imgDpi;
        let difY = (Number(frameValue.topFrame) / 25.4) * this.imgDpi;
        let w = ((this.mw - vFrame) / 25.4) * this.imgDpi;
        let h = ((this.mh - hFrame) / 25.4) * this.imgDpi;
        tCtx.putImageData(cutImage, 0, 0, difX, difY, w, h);
        // 将截图内容转化base64
        let image = tC.toDataURL("image/png", 0.8);
        tCtx.clearRect(0, 0, nwidth, nheight);

        // 合成后上传
        this.importFile(imgUrl, image);
      });
    },
    // 切图
    getCutImg(imgUrl) {
      let fCanvas = document.createElement("canvas");
      let fCtx = fCanvas.getContext("2d");
      this.paintPrintImg(fCanvas, fCtx);

      if (Number(this.materialsType) === 2 && this.manufactor === "MK") {
        // TPU（防摔壳），合成生产图
        let curPdf = fCanvas.toDataURL("image/png", 0.8);
        this.compoundPdf(curPdf, imgUrl);
      } else {
        let pdfImg = this.handlePdf(fCanvas, fCtx);
        return pdfImg;
      }
    },
    // 生产图处理
    handlePdf(fCanvas, fCtx) {
      let wpx, hpx;
      if (this.imgDpi === 0) {
        wpx = bimg.naturalWidth;
        hpx = bimg.naturalHeight;
      } else {
        if (
          Number(this.materialsType) === 1 ||
          Number(this.materialsType) === 3
        ) {
          // 玻璃壳/热升华+2mm
          let wpx2 = (this.mw / 25.4) * this.imgDpi;
          let hpx2 = (this.mh / 25.4) * this.imgDpi;
          wpx = ((this.nw + 2) / 25.4) * this.imgDpi;
          hpx = ((this.nh + 2) / 25.4) * this.imgDpi;
          this.diffX = (wpx2 - wpx) / 2;
          this.diffY = (hpx2 - hpx) / 2;

          // 玻璃壳/热升华---水平镜像（MK）
          if (this.manufactor === "MK") {
            let imgData = fCtx.getImageData(
              this.diffX,
              this.diffY,
              wpx,
              hpx + 10
            );
            let newImgData = fCtx.getImageData(
              this.diffX,
              this.diffY,
              wpx,
              hpx + 10
            );
            fCtx.putImageData(
              this.imageDataHRevert(newImgData, imgData),
              0,
              0,
              this.diffX,
              this.diffY,
              wpx,
              hpx
            );
          }
        } else if (
          Number(this.materialsType) === 2 ||
          Number(this.materialsType) === 4
        ) {
          // TPU 透明边距
          wpx = (this.mw / 25.4) * this.imgDpi;
          hpx = (this.mh / 25.4) * this.imgDpi;
          this.diffX = this.diffY = 0;
        }
      }

      let cutImage = fCtx.getImageData(this.diffX, this.diffY, wpx, hpx);
      // 将canvas的透明背景设置相应颜色
      if (this.materialsColorValue) {
        let colorArr = this.colorRgba(this.materialsColorValue);
        for (let i = 0; i < cutImage.data.length; i += 4) {
          // 当该像素是透明的,则设置成白色
          if (cutImage.data[i + 3] === 0) {
            cutImage.data[i] = colorArr[0];
            cutImage.data[i + 1] = colorArr[1];
            cutImage.data[i + 2] = colorArr[2];
            cutImage.data[i + 3] = 255;
          }
        }
      }
      fCtx.clearRect(0, 0, fCanvas.width, fCanvas.height);

      let tC = document.createElement("canvas");
      let tCtx = tC.getContext("2d");

      // 折叠屏
      let cutInfo = sessionStorage.getItem("cutInfo");
      if (cutInfo) {
        cutInfo = JSON.parse(cutInfo);
        if (Number(cutInfo.cutType) === 1) {
          // 纵切
          let slittingHeight = 0;
          if (
            Number(this.materialsType) === 1 ||
            Number(this.materialsType) === 3
          ) {
            // 玻璃壳/热升华+1mm（需多切1mm）
            slittingHeight =
              ((cutInfo.slittingHeight + 1) / 25.4) * this.imgDpi;
          } else {
            slittingHeight = (cutInfo.slittingHeight / 25.4) * this.imgDpi;
          }
          let slittingWhite = (cutInfo.slittingWhite / 25.4) * this.imgDpi;
          tC.width = wpx;
          tC.height = hpx + slittingWhite;
          tCtx.putImageData(cutImage, 0, 0, 0, 0, wpx, slittingHeight);
          tCtx.putImageData(
            cutImage,
            0,
            slittingWhite,
            0,
            slittingHeight,
            wpx,
            hpx - slittingHeight
          );
        } else if (Number(cutInfo.cutType) === 2) {
          // 横切
          let crosscuttingWidth = 0;
          if (
            Number(this.materialsType) === 1 ||
            Number(this.materialsType) === 3
          ) {
            // 玻璃壳/热升华+1mm（需多切1mm）
            crosscuttingWidth =
              ((cutInfo.crosscuttingWidth + 1) / 25.4) * this.imgDpi;
          } else {
            crosscuttingWidth =
              (cutInfo.crosscuttingWidth / 25.4) * this.imgDpi;
          }
          let crosscuttingWhite =
            (cutInfo.crosscuttingWhite / 25.4) * this.imgDpi;
          tC.width = wpx + crosscuttingWhite;
          tC.height = hpx;
          tCtx.putImageData(cutImage, 0, 0, 0, 0, crosscuttingWidth, hpx);
          tCtx.putImageData(
            cutImage,
            crosscuttingWhite,
            0,
            crosscuttingWidth,
            0,
            wpx - crosscuttingWidth,
            hpx
          );
        }
      } else {
        // 非折叠屏
        if (
          Number(this.materialsType) === 2 ||
          Number(this.materialsType) === 4
        ) {
          // 透明边距
          let frameValue = JSON.parse(sessionStorage.getItem("frameValue"));
          let vFrame = 0;
          let hFrame = 0;
          if (frameValue) {
            vFrame =
              Number(frameValue.leftFrame) + Number(frameValue.rightFrame);
            hFrame =
              Number(frameValue.topFrame) + Number(frameValue.underFrame);
          }
          tC.width = (this.mw / 25.4) * this.imgDpi;
          tC.height = (this.mh / 25.4) * this.imgDpi;
          let difX = (Number(frameValue.leftFrame) / 25.4) * this.imgDpi;
          let difY = (Number(frameValue.topFrame) / 25.4) * this.imgDpi;
          let w = ((this.mw - vFrame) / 25.4) * this.imgDpi;
          let h = ((this.mh - hFrame) / 25.4) * this.imgDpi;
          tCtx.putImageData(cutImage, 0, 0, difX, difY, w, h);
        } else {
          tC.width = wpx;
          tC.height = hpx;
          tCtx.putImageData(cutImage, 0, 0, 0, 0, wpx, hpx);
        }
      }
      // 将截图内容转化base64
      let newImage = tC.toDataURL("image/png", 0.8);
      tCtx.clearRect(0, 0, wpx, hpx);

      return newImage;
    },
    // 横向镜像翻转
    imageDataHRevert(sourceData, newData) {
      for (var i = 0, h = sourceData.height; i < h; i++) {
        for (var j = 0, w = sourceData.width; j < w; j++) {
          newData.data[i * w * 4 + j * 4 + 0] =
            sourceData.data[i * w * 4 + (w - j) * 4 + 0];
          newData.data[i * w * 4 + j * 4 + 1] =
            sourceData.data[i * w * 4 + (w - j) * 4 + 1];
          newData.data[i * w * 4 + j * 4 + 2] =
            sourceData.data[i * w * 4 + (w - j) * 4 + 2];
          newData.data[i * w * 4 + j * 4 + 3] =
            sourceData.data[i * w * 4 + (w - j) * 4 + 3];
        }
      }
      return newData;
    },
    paintPrintImg(fCanvas, fCtx) {
      let dx = 0;
      let dy = 0;
      // 手机的左上角坐标
      let phonex = c.width / canvasRatio / 2 - this.pwidth2 / 2;
      let phoney = c.height / canvasRatio / 2 - this.pheight2 / 2;
      // 手机当前dpi的尺寸
      let phonew = (this.mw / 25.4) * this.imgDpi;
      let phoneh = (this.mh / 25.4) * this.imgDpi;
      fCanvas.width = phonew;
      fCanvas.height = phoneh;

      this.diffX = 0;
      this.diffY = 0;
      this.dragArr.forEach((item, index) => {
        fCtx.save();
        // 1-图片 2-文字
        if (item.type === 1) {
          // 需要绘制的宽高(px)，根据当前dpi计算
          let width = (item.url.naturalWidth / item.dpi) * this.imgDpi;
          let height = (item.url.naturalHeight / item.dpi) * this.imgDpi;
          // 图片缩放比例
          this.scale =
            ((item.url.naturalWidth / item.dpi) * this.imgDpi) / item.w;
          // 判断放大还是缩小
          this.difScale = item.initw / item.w;
          dx = (item.x - phonex) * this.scale;
          dy = (item.y - phoney) * this.scale;

          fCtx.translate(dx + width / 2, dy + height / 2);
          fCtx.rotate((item.rotate * Math.PI) / 180);
          fCtx.translate(-(dx + width / 2), -(dy + height / 2));
          fCtx.drawImage(item.url, dx, dy, width, height);
        } else if (item.type === 2) {
          if (this.difScale === 0) {
            // 没有图片（第一个图层是文字），以底图为画布，计算缩放比例
            this.scale = phonew / bimg.width;
          }
          dx = (item.x - phonex) * this.scale;
          dy = (item.y - phoney) * this.scale;

          let ratio = item.w / item.h;
          // 需要绘制的宽高(px)
          let heightt = item.h * this.scale;
          fCtx.font = heightt + "px " + item.fontfamily;
          let widtht = fCtx.measureText(item.filltext).width;
          heightt = widtht / ratio;

          fCtx.translate(dx + widtht / 2, dy + heightt / 2);
          fCtx.rotate((item.rotate * Math.PI) / 180);
          fCtx.translate(-(dx + widtht / 2), -(dy + heightt / 2));

          fCtx.font = heightt + "px " + item.fontfamily;
          fCtx.fillStyle = item.fillstyle;
          fCtx.textBaseline = "top";
          fCtx.textAlign = "left";
          fCtx.fillText(item.filltext, dx, dy);
        }
        fCtx.restore();
      });
    },
    getImgDPI(item) {
      // 得到手机尺寸（英寸）
      let icw = this.mw / 25.4;
      let ich = this.mh / 25.4;
      // 得到缩放倍数
      let scalew = item.initw / item.w;
      let scaleh = item.inith / item.h;
      let scale = scalew > scaleh ? scaleh : scalew;
      let dpi = 0;
      if (item.url.naturalWidth / icw > item.url.naturalHeight / ich) {
        // 获取图片dpi和获取计算dpi的手机尺寸
        dpi = item.url.naturalHeight / ich;
        this.naturalSize = ich;
        this.dpiType = 2;
      } else {
        dpi = item.url.naturalWidth / icw;
        this.naturalSize = icw;
        this.dpiType = 1;
      }
      this.imgDpi = Math.round(dpi * scale);
      this.$set(item, "dpi", this.imgDpi);
      // 限制图片dpi不能过大，最大dpi 300
      this.limitImgDpi(scale, item);
      this.$emit("dpi", this.imgDpi, scale);
    },
    // type = 1 情况按宽计算，type = 2 情况按长计算
    limitImgDpi(scale, item) {
      this.naturalWidth = item.url.naturalWidth;
      this.naturalHeight = item.url.naturalHeight;
      if (this.imgDpi > 300) {
        // 超过300dpi时，取固定300dpi
        this.imgDpi = 300;
        if (this.dpiType === 1) {
          this.naturalWidth = (this.imgDpi / scale) * this.naturalSize;
          this.naturalHeight =
            item.url.naturalHeight *
            (this.naturalWidth / item.url.naturalWidth);
        } else {
          this.naturalHeight = (this.imgDpi / scale) * this.naturalSize;
          this.naturalWidth =
            item.url.naturalWidth *
            (this.naturalHeight / item.url.naturalHeight);
        }
      }
    },
    importFile(image, pdfimg) {
      // 折叠屏
      let cutInfo = sessionStorage.getItem("cutInfo");
      if (cutInfo) {
        cutInfo = JSON.parse(cutInfo);
      }

      // image-合成图 pdfimg-pdf图
      // this.clear();
      // base64转换成blob
      let _fileBlob = this.dataURLtoBlob(image.src);
      // blob转换成file文件
      let imgFile = new File([_fileBlob], new Date().getTime() + ".png");
      // 判断manufactor类型
      let pdfFile;
      if (this.manufactor === "MK") {
        // 初始化pdf,设置相应格式，生成pdf
        let pdf;

        let width = this.nw;
        if (
          cutInfo &&
          Number(cutInfo.cutType) === 2 &&
          cutInfo.crosscuttingWhite
        ) {
          // 折叠屏--横向
          width = this.nw + 2 + cutInfo.crosscuttingWhite;
        } else {
          if (
            Number(this.materialsType === 2) ||
            Number(this.materialsType === 4)
          ) {
            width = this.nw;
          } else {
            // 玻璃壳/热升华+2mm
            width = this.nw + 2;
          }
        }

        let height = this.nh;
        if (cutInfo && Number(cutInfo.cutType) === 1 && cutInfo.slittingWhite) {
          // 折叠屏--纵向
          height = this.nh + 2 + cutInfo.slittingWhite;
        } else {
          if (
            Number(this.materialsType === 2) ||
            Number(this.materialsType === 4)
          ) {
            height = this.nh;
          } else {
            // 玻璃壳/热升华+2mm
            height = this.nh + 2;
          }
        }

        /* eslint-disable */
        if (width > height) {
          pdf = new jsPDF("l", "mm", [width, height]);
        } else {
          pdf = new jsPDF("p", "mm", [width, height]);
        }

        pdf.addImage(pdfimg, "jpeg", 0, 0, width, height, "", "FAST");

        // 将pdf输入为base格式的字符串
        let buffer = pdf.output("datauristring");
        // 将base64格式的字符串转换为file文件
        pdfFile = this.dataURLtoFile(buffer, "report.pdf");
      } else {
        // 图片
        // base64转换成blob
        let _fileBlob = this.dataURLtoBlob(pdfimg);
        // blob转换成file文件
        pdfFile = new File(
          [_fileBlob],
          new Date().getTime() + Math.round(Math.random() * 10) + ".png"
        );
      }

      // 上传文件
      let baseUrl = window.location.href;
      if (
        baseUrl.indexOf("diy.bat.com") > 0
      ) {
        this.uploadFile(imgFile, pdfFile, "bat");
      } else {
        this.uploadFile(imgFile, pdfFile, + "bat" + "_test");
      }
    },
    uploadFile(img, pdf, name) {
      let that = this;
      let imgUrl, pdfUrl;
      let uid = getLocalStorageItem("userId");

      // 随机命名
      let randomNameImg =
        this.random_string(6) +
        "_" +
        new Date().getTime() +
        "." +
        img.name.split(".").pop();
      let randomNamePDF = null;
      if (pdf !== null) {
        randomNamePDF =
          this.random_string(6) +
          "_" +
          new Date().getTime() +
          "." +
          pdf.name.split(".").pop();
      }
      that.$api
        .get(this, api.getOSSSts, {
          userId: uid,
        })
        .then((result) => {
          let client = new OSS.Wrapper({
            region: result.data.region,
            accessKeyId: result.data.accessKeyId,
            accessKeySecret: result.data.accessKeySecret,
            stsToken: result.data.securityToken,
            bucket: result.data.bucketName,
            endpoint: result.data.endpoint,
            secure: true,
          });

          let currentDate = this.getCurrentDate();
          // 上传定制图片
          client
            .multipartUpload(
               "flexible/diyPicture/" +
               name +
               "/" +
               currentDate +
			    "/" +
               uid +
               "/img/" +
               randomNameImg,
              img,
              {}
            )
            .then((results) => {
              imgUrl = result.data.hostname + results.name;
              // 上传pdf文件
              if (pdf !== null) {
                client
                  .multipartUpload(
                    "flexible/diyPicture/pdf/" +
                      name +
                      "/" +
                      currentDate +
					  "/" +
                      uid +
                      "/pdf/" +
                      randomNamePDF,
                    pdf,
                    {}
                  )
                  .then((results) => {
                    if (results.res.status === 200) {
                      this.showLoading = false;
                      pdfUrl = result.data.hostname + results.name;
                      this.$emit("submit", imgUrl, pdfUrl);
                    }
                  })
                  .catch((err) => {
                    console.log(err);
                  });
              }
            })
            .catch((err) => {
              console.log(err);
            });
        })
        .catch((err) => {
          console.log(err);
        });
    },
    // 将base64转换为文件对象
    dataURLtoFile(dataurl, filename) {
      let arr = dataurl.split(",");
      let mime = arr[0].match(/:(.*?);/)[1];
      let bstr = atob(arr[1]);
      let n = bstr.length;
      let u8arr = new Uint8Array(n);
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
      }
      // 转换成file对象
      return new File([u8arr], filename, {
        type: mime,
      });
    },
    // base64转换成blob
    dataURLtoBlob(dataurl) {
      let arr = dataurl.split(",");
      let mime = arr[0].match(/:(.*?);/)[1];
      let bstr = atob(arr[1]);
      let n = bstr.length;
      let u8arr = new Uint8Array(n);
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
      }
      return new Blob([u8arr], {
        type: mime,
      });
    },
    // 随机生成文件名
    random_string(len) {
      len = len || 32;
      let chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678";
      let maxPos = chars.length;
      let pwd = "";
      for (let i = 0; i < len; i++) {
        pwd += chars.charAt(Math.floor(Math.random() * maxPos));
      }
      return pwd;
    },
    pm2px(d) {
      // mm转px
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
    convertCanvasToImage(canvas) {
      // 新Image对象
      let image = new Image();
      image.crossOrigin = "";
      let dataUrl = canvas.toDataURL("image/png", 0.8);
      image.src = dataUrl;
      return image;
    },
    // 加载图片
    loadImage(url, index) {
      return new Promise((resolve) => {
        let img = new Image();
        img.crossOrigin = "";
        if (index === 0 || index === 2) {
          img.src = url + "?t=" + new Date().getTime();
        } else {
          img.src = url;
        }
        if (img.complete) {
          resolve(img);
        }
        img.onload = () => resolve(img);
      });
    },
    getCurrentDate() {
      let timeStr = "-";
      let curDate = new Date();
      let curYear = curDate.getFullYear();
      let curMonth = curDate.getMonth() + 1;
      let curDay = curDate.getDate();
      let Current = curYear + timeStr + curMonth + timeStr + curDay;
      return Current;
    },
    // 清空画布
    clear() {
      let canvas = document.getElementsByTagName("canvas");
      for (let i = 0; i < canvas.length; i++) {
        let ctx = canvas[i].getContext("2d");
        ctx.clearRect(0, 0, canvas[i].width, canvas[i].height);
      }
    },
    // 重置
    reset() {
      this.$emit("clear", 0);
      this.curClicked = "";
      this.dragArr.length = 0;
      // 重新绘制
      this.draw();
    },
    getPixelRatio() {
      let canvas = document.createElement("canvas");
      let context = canvas.getContext("2d");
      let backingStore =
        context.backingStorePixelRatio ||
        context.webkitBackingStorePixelRatio ||
        context.mozBackingStorePixelRatio ||
        context.msBackingStorePixelRatio ||
        context.oBackingStorePixelRatio ||
        context.backingStorePixelRatio ||
        1;
      return (window.devicePixelRatio || 1) / backingStore;
    },
    // 图层列表 - 显示
    handleShowWrap() {
      this.showHandleWrap = true;
    },
    // 图层列表 - 关闭
    handleCloseWrap() {
      this.showHandleWrap = false;
    },
    // 图层 - 删除
    handleDelete(curItem) {
      this.dragArr.forEach((item, index) => {
        if (JSON.stringify(curItem) === JSON.stringify(item)) {
          this.dragArr.splice(index, 1);
        }
      });
      if (this.dragArr.length > 0) {
        this.draw();
      } else {
        // 清空canvas
        this.clear();
        // 重置点击热区
        this.clickedArea = [];
        this.showHandleWrap = false;
      }
    },
    // 图层 - 上/下移
    handleMove(curItem, type) {
      let len = this.dragArr.length;
      let curIndex = 0;
      this.dragArr.forEach((item, index) => {
        this.$set(item, "selected", false);

        if (JSON.stringify(curItem) === JSON.stringify(item)) {
          // 获取当前选中
          this.$set(item, "selected", true);
          curIndex = index;

          if (type === "up") {
            // 上移，调整位置
            if (curIndex < len - 1) {
              let temp = this.dragArr[curIndex + 1];
              Vue.set(this.dragArr, curIndex + 1, this.dragArr[curIndex]);
              Vue.set(this.dragArr, curIndex, temp);
            }
          } else {
            // 下移，调整位置
            if (curIndex > 0) {
              let temp = this.dragArr[curIndex - 1];
              Vue.set(this.dragArr, curIndex - 1, this.dragArr[curIndex]);
              Vue.set(this.dragArr, curIndex, temp);
            }
          }
        }
      });

      // 重新绘制
      this.draw();
    },
    // 点击热区
    handlePic(e, item) {
      if (this.clickedNum === 0) {
        this.clickedNum++;
        this.clickedTimer = setTimeout(() => {
          // 单击
          if (this.curPicType === 3) {
            this.clickedArea.forEach((item, index) => {
              if (
                item.x1 <= e.x - this.curLeft &&
                e.x - this.curLeft <= item.x2 &&
                item.y1 <= e.y &&
                e.y <= item.y2
              ) {
                this.curClicked = index;
                // 显示换图/编辑提示窗
                if (item.url) {
                  this.hasClickedArea = true;
                } else {
                  // 上传本地图片
                  this.$parent.chooseImg();
                }
              }
            });
          }
          this.clickedNum = 0;
        }, 300);
      } else {
        // 双击
        clearTimeout(this.clickedTimer);
        if (item.url !== "") {
          // 隐藏换图/编辑提示窗
          this.hasClickedArea = false;
          // 获取当前点击热区信息
          this.curItem = item;
          // 显示模板编辑
          this.showTemplate = true;
        }
        this.clickedNum = 0;
      }
    },
    // 替换
    handleReplace() {
      if (this.hasClickedArea) {
        // 上传本地图片
        this.$parent.chooseImg();
        // 隐藏换图/编辑提示窗
        this.hasClickedArea = false;
      }
    },
    // 跳转模板编辑
    goEdit(item) {
      if (item.url !== "" && this.hasClickedArea) {
        // 隐藏换图/编辑提示窗
        this.hasClickedArea = false;
        // 获取当前点击热区信息
        this.curItem = item;
        // 显示模板编辑
        this.showTemplate = true;
      }
    },
    initCanvas() {
      // 画布大小
      WIDTH = window.screen.width;
      HEIGHT = this.wheight;
      TOPVALUE = this.$parent.headerH;

      c = document.getElementById("myCanvas");
      ctx = c.getContext("2d");
      let cwidth = WIDTH * canvasRatio;
      let cheight = HEIGHT * canvasRatio;
      c.width = cwidth;
      c.height = cheight;
      c.style.width = WIDTH + "px";
      c.style.height = HEIGHT + "px";
      c2 = document.getElementById("upCanvas");
      ctx2 = c2.getContext("2d");
      c2.width = this.pwidth * canvasRatio;
      c2.height = this.pheight * canvasRatio;
      c2.style.width = this.pwidth + "px";
      c2.style.height = this.pheight + "px";
    },
    // 限制移动范围
    limitMove(touchEvent, initial, startTouch, lastImg) {
      // 获取画布坐标
      let canvasPoint = this.getCanvasPoint();

      let { x, y } = touchEvent;
      let { initialX, initialuX } = initial;
      let { startX, startY } = startTouch;

      // 判断水平移动
      if (x - startX > lastImg.x - initialX) {
        // 右移
        lastImg.x = initialX + (x - startX);
        lastImg.ux = initialuX + (x - startX);

        // 判断图片左上角x是否跟画布左上角x重合
        if (canvasPoint.x1 - lastImg.x <= 0) {
          // 重合
          lastImg.x = canvasPoint.x1;
          lastImg.ux = initialuX + (canvasPoint.x1 - initialX);
          // 限制垂直移动范围
          this.handleMoveY(y, initial, startY, lastImg, canvasPoint);
        }
      } else if (x - startX < lastImg.x - initialX) {
        // 左移
        lastImg.x = initialX + (x - startX);
        lastImg.ux = initialuX + (x - startX);

        // 判断图片右上角x是否跟画布右上角x重合
        if (canvasPoint.x2 - lastImg.w - lastImg.x >= 0) {
          // 重合
          lastImg.x = canvasPoint.x2 - lastImg.w;
          lastImg.ux = initialuX + (canvasPoint.x2 - lastImg.w - initialX);
          // 限制垂直移动范围
          this.handleMoveY(y, initial, startY, lastImg, canvasPoint);
        }
      } else {
        lastImg.x = initialX + (x - startX);
        lastImg.ux = initialuX + (x - startX);
        // 限制垂直移动范围
        this.handleMoveY(y, initial, startY, lastImg, canvasPoint);
      }
    },
    // 限制垂直移动范围
    handleMoveY(y, initial, startY, lastImg, canvasPoint) {
      let { initialY, initialuY } = initial;
      // 判断垂直移动
      if (y - startY > lastImg.y - initialY) {
        // 下移
        lastImg.y = initialY + (y - startY);
        lastImg.uy = initialuY + (y - startY);

        // 判断图片左上角y是否跟画布左上角y重合
        if (canvasPoint.y1 - lastImg.y <= 0) {
          // 重合
          lastImg.y = canvasPoint.y1;
          lastImg.uy = initialuY + (canvasPoint.y1 - initialY);
        }
      } else if (y - startY < lastImg.y - initialY) {
        // 上移
        lastImg.y = initialY + (y - startY);
        lastImg.uy = initialuY + (y - startY);

        // 判断图片左下角y是否跟画布左下角y重合
        if (canvasPoint.y2 - lastImg.h - lastImg.y >= 0) {
          // 重合
          lastImg.y = canvasPoint.y2 - lastImg.h;
          lastImg.uy = initialuY + (canvasPoint.y2 - lastImg.h - initialY);
        }
      } else {
        lastImg.y = initialY + (y - startY);
        lastImg.uy = initialuY + (y - startY);
      }
    },
    // 限制缩放
    limitScale(touchEvent, initial, startTouch, lastImg) {
      // 获取画布坐标
      let canvasPoint = this.getCanvasPoint();

      let { x, y } = touchEvent;
      let { initialH, initialW, initialX, initialY, initialuX, initialuY } =
        initial;
      let { startX, startY } = startTouch;

      // 用勾股定理算出距离
      let lineA = this.getDistance(startX, startY);
      let lineB = this.getDistance(x, y);
      // 缩放后宽高
      let w = initialW + lineB - lineA;
      let h = initialH + (lineB - lineA) * (initialH / initialW); // 由于是等比缩放，所以乘一个宽高比例
      // 缩放后距离
      lastImg.linew = lineB - lineA;
      lastImg.lineh = (lineB - lineA) * (initialH / initialW);

      // 是否强制铺满血位
      let isAllPlace = parseInt(sessionStorage.getItem("isAllPlace"));
      // 获取最小缩放宽高
      let minW = isAllPlace ? lastImg.minW : 20;
      let minH = isAllPlace ? lastImg.minH : 20 * (lastImg.minW / lastImg.minH);
      // 判断当前缩放宽高是否大于最小宽高
      if (w > minW && h > minH) {
        // 大于，设置宽高/坐标值
        lastImg.w = w;
        lastImg.h = h;
        lastImg.x = initialX - lastImg.linew / 2;
        lastImg.ux = initialuX - lastImg.linew / 2;
        lastImg.y = initialY - lastImg.lineh / 2;
        lastImg.uy = initialuY - lastImg.lineh / 2;
      } else {
        // 小于或等于，设置为初始化值
        lastImg.w = minW;
        lastImg.h = minH;
      }

      if (
        Number(lastImg.type) === 1 &&
        Number(lastImg.picType) !== 4 &&
        isAllPlace
      ) {
        // 强制铺满血位，限制缩放范围
        this.handleScale(initial, lastImg, canvasPoint);
      }
    },
    // 限制缩放范围
    handleScale(initial, lastImg, canvasPoint) {
      let { initialX, initialY, initialuX, initialuY } = initial;

      if (canvasPoint.x1 - lastImg.x <= 0) {
        // 判断图片左上角x是否跟画布左上角x重合
        lastImg.x = canvasPoint.x1;
        lastImg.ux = initialuX + (canvasPoint.x1 - initialX);
      } else if (canvasPoint.x2 - lastImg.w - lastImg.x >= 0) {
        // 判断图片右上角x是否跟画布右上角x重合
        lastImg.x = canvasPoint.x2 - lastImg.w;
        lastImg.ux = initialuX + (canvasPoint.x2 - lastImg.w - initialX);
      }
      if (canvasPoint.y1 - lastImg.y <= 0) {
        // 判断图片左上角y是否跟画布左上角y重合
        lastImg.y = canvasPoint.y1;
        lastImg.uy = initialuY + (canvasPoint.y1 - initialY);
      } else if (canvasPoint.y2 - lastImg.h - lastImg.y >= 0) {
        // 判断图片左下角y是否跟画布左下角y重合
        lastImg.y = canvasPoint.y2 - lastImg.h;
        lastImg.uy = initialuY + (canvasPoint.y2 - lastImg.h - initialY);
      }
    },
    // 获取画布坐标
    getCanvasPoint() {
      // 获取画布中心点坐标
      let centerCX = window.screen.width / 2;
      let centerCY = this.$parent.wheight / 2;
      // 计算画布坐标
      return {
        centerCX: centerCX, // 画布中心点x
        centerCY: centerCY, // 画布中心点y
        x1: centerCX - this.pwidth / 2, // 左上角x
        x2: centerCX + this.pwidth / 2, // 右上角x
        y1: centerCY - this.pheight / 2, // 左上角y
        y2: centerCY + this.pheight / 2, // 左下角y
      };
    },
  },
  watch: {
    dragArr(arr) {
      if (arr.length === 1) {
        // 第一次添加图层，显示图层列表
        this.showHandleWrap = true;
      }
      arr.forEach((item, index) => {
        item.index = index;
      });
      this.reverseData = arr.slice().reverse(); // 将当前图层倒序
      return arr;
    },
    // 监听画布宽度以及距离左侧left值
    wwidth(value) {
      this.wwidth = value;
      LEFTVALUE = WIDTH - this.wwidth;
      // left 值
      this.curLeft = LEFTVALUE / 2;
    },
    spriteArr(arr) {
      this.initCanvas();
      if (arr.length > 0) {
        this.spriteArr = arr;
        // 获取最新添加的数据
        let newImg = arr.slice(-1)[0];
        // 判断图片类型  0-网络图 1-普通素材 2-IP素材（不可更改） 3-模板（不可更改）
        if (parseInt(newImg.picType) === 2) {
          // 重置点击热区
          this.clickedArea = [];
          
          // IP图，直接替换，不可移动
          this.isDrag = false;
          let item = new DragImg(newImg, ctx);
          this.dragArr = [];
          this.dragArr.push(item);
          this.drawIpImage();
          this.getImgDPI(item);
        } else if (parseInt(newImg.picType) === 3) {
          // 模板
          this.isDrag = true;
          let item = new DragImg(newImg, ctx);
          if (newImg.type === 1) {
            // 图片
            if (newImg.cateType === 1) {
              // 获取本地图片数量
              let localImgNum = 0;
              this.dragArr.forEach((curItem) => {
                if (curItem.picId === 0) {
                  localImgNum++;
                }
              });
              if (localImgNum === 0) {
                // 未上传本地图片，直接添加
                this.dragArr.splice(this.curClicked, 0, item);
              } else {
                // 已上传，替换当前点击区域图片
                this.dragArr.splice(this.curClicked, 1, item);
              }
            } else {
              // 模板素材，重置数据
              this.dragArr = [];
              this.curClicked = "";
              this.dragArr.push(item);
            }
          } else {
            this.dragArr.forEach((item, index) => {
              // type 2--文字
              if (
                (newImg.index === index && item.type === 2) ||
                (newImg.index === null && newImg === 2)
              ) {
                this.dragArr.splice(index, 1);
              }
            });

            // 其他元素，直接添加
            this.dragArr.push(item);
          }

          this.draw();
          // 获取图片中心点位置
          let cx = window.screen.width / 2 - modelCenter.ux;
          let cy = this.wheight / 2 - modelCenter.uy + TOPVALUE;
          if (newImg.type !== 2) {
            this.clickedArea = [];
            // 设置点击热区范围
            if (modelArea && modelArea.length > 0) {
              modelArea.forEach((curArea) => {
                let area = {
                  // 起始点x,y
                  x1: cx + curArea.ux1,
                  y1: cy + curArea.uy1,
                  // 结束点x,y
                  x2: cx + curArea.ux2,
                  y2: cy + curArea.uy2,
                  // 图片（链接，宽高，x，y）
                  url: newImg.cateType === 1 ? newImg.url.src : "",
                  width: newImg.width,
                  height: newImg.height,
                  x: newImg.x,
                  y: newImg.y,
                  scale: newImg.scale,
                  frame: curArea.frame,
                };
                this.clickedArea.push(area);
              });
            }
          }
          this.getImgDPI(item);
        } else {
          // 普通素材/贴图
          this.isDrag = true;
          this.dragArr.forEach((item, index) => {
            // type 1--图片
            if (newImg.type === 1 && item.type === 1) {
              // 判断是否铺满血位
              let isAllPlace = parseInt(sessionStorage.getItem("isAllPlace"));
              if (!isAllPlace) {
                // 否，多图层
                // 判断当前图片是否是素材图片（素材图片id），同时不是贴图
                if (newImg.picId && newImg.picType !== 4) {
                  // 判断是否已经有素材图片
                  if (item.picId && item.picType !== 4) {
                    // 有直接替换
                    this.dragArr.splice(index, 1);
                  }
                }
              } else {
                // 是，直接替换图片
                if (newImg.picType !== 4) {
                  this.dragArr.splice(index, 1);
                }
              }
            }

            // type 2--文字
            if (
              (newImg.index === index && item.type === 2) ||
              (newImg.index === null && newImg === 2)
            ) {
              this.dragArr.splice(index, 1);
            }
          });
          let item = new DragImg(newImg, ctx);
          if (newImg.type === 1) {
            // 图片
            item.linew = 0;
            item.lineh = 0;
            this.dragArr.push(item);
            this.getImgDPI(item);
          } else {
            // 文字
            this.dragArr.push(item);
            this.$emit("select", item, this.dragArr.length - 1);
          }
          this.draw();
        }
      }
    },
    pwidth(value) {
      if (this.dragArr.length > 0) {
        this.dragArr.map((item) => {
          if (item.type === 1) {
            // 图片
            item.ux = item.ux - (PWIDTH - value) / 2;
            item.picType === 1 ? this.draw() : this.drawIpImage();
            this.getImgDPI(item);
          }
        });
      }
      this.pwidth = value;
      PWIDTH = value;
    },
    pheight(value) {
      if (this.dragArr.length > 0) {
        this.dragArr.map((item) => {
          if (item.type === 1) {
            // 图片
            item.uy = item.uy - (PHEIGHT - value) / 2;
          }
        });
      }
      this.pheight = value;
      PHEIGHT = value;
    },
    bgimg(value) {
      this.bgimg = value;
    },
    upimg(value) {
      this.upimg = value;
    },
    mw(value) {
      // 手机尺寸宽（mm）
      this.mw = value;
    },
    mh(value) {
      // 手机尺寸高（mm）
      this.mh = value;
    },
  },
};
</script>
<style scoped lang="stylus" rel="stylesheet/stylus">
$white = #fff;
$gray = #5A5A5A;
$light-gray = #787878;
$gray-bd = #F1F1F1;
$blue = #0076A5;

.canvas-wrapper {
  .bgimg {
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate3d(-50%, -50%, 0);
    background-repeat: no-repeat;
  }

  #myCanvas {
    &.module {
      filter: opacity(0);
    }

    position: absolute;
    top: 0;
    left: 50%;
    transform: translateX(-50%);
    filter: opacity(0);
    z-index: 5;

    &.show {
      filter: opacity(0.3);
    }
  }

  #nCanvas {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate3d(-50%, -50%, 0);
  }

  .upcanvas {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate3d(-50%, -50%, 0);
    z-index: 8;
  }

  .up-box {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    background-size: 186px 388px;
    background-repeat: no-repeat;
    background-position: 50% 50%;
    background-clip: content-box;
    z-index: 10;

    .icon {
      position: absolute;
      top: -10px;
      left: -10px;
      font-size: 18px;
      color: #fff;
      background-color: red;
      border-radius: 50%;
    }
  }

  .dash-border {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate3d(-50%, -50%, 0);
    border: 1px dashed #f00;
    z-index: 9;
  }

  .move-tips {
    position: absolute;
    bottom: 35px;
    left: 50%;
    width: 100%;
    font-size: 12px;
    color: #f00;
    text-align: center;
    line-height: 16px;
    transform: translateX(-50%);
  }
}

.clicked-area {
  position: fixed;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 100;

  .icon_picture {
    &.none {
      display: none;
    }
  }

  .tips {
    display: flex;
    align-items: center;
    justify-content: center;
    position: absolute;
    top: -30px;
    left: 50%;
    width: 115px;
    padding: 0 5px;
    line-height: 33px;
    font-size: 12px;
    color: $white;
    text-align: center;
    background-color: rgba(49, 49, 49, 0.9);
    border-radius: 20px;
    transform: translateX(-50%);
    opacity: 0;
    transition: all 0.8s;

    &:before {
      content: '';
      position: absolute;
      bottom: -6px;
      left: 50%;
      margin-left: -6px;
      width: 0;
      height: 0;
      border-right: 6px solid transparent;
      border-left: 6px solid transparent;
      border-top: 6px solid rgba(49, 49, 49, 0.9);
    }

    &.show {
      opacity: 1;
    }

    p {
      flex: 1;

      .sprite-icon {
        top: 3px;
        margin-right: 3px;
      }
    }
  }
}

.layer-icon {
  position: fixed;
  bottom: 255px;
  right: 0;
  padding-left: 5px;
  width: 31px;
  height: 35px;
  font-size: 18px;
  color: $white;
  text-align: center;
  line-height: 35px;
  background-color: $blue;
  box-shadow: 0px 2px 3px 0px rgba(48, 156, 206, 0.36);
  border-radius: 45px 0 0 45px;
  z-index: 100;

  &.hidden {
    display: none;
  }
}

.handle-layer {
  position: fixed;
  right: 0;
  bottom: -100%;
  left: 0;
  padding: 0 15px;
  font-size: 15px;
  color: $gray;
  background-color: $white;
  z-index: 101;
  transition: all 0.3s;

  &.show {
    bottom: 0;
  }

  .close-handle {
    position: absolute;
    top: -25px;
    right: 15px;
    width: 35px;
    height: 25px;
    line-height: 25px;
    font-size: 14px;
    color: $light-gray;
    text-align: center;
    background-color: $white;
    border-radius: 5px 5px 0 0;

    .van-icon {
      top: 2px;
    }
  }

  .handle-list {
    height: 55px;
    display: flex;
    align-items: center;
    justify-content: center;

    .van-icon {
      flex: 1;
      top: 1px;
      font-size: 20px;
      color: $blue;
    }

    .move {
      flex: 2;
      text-align: center;
    }

    .type {
      flex: 2;
      text-align: center;
    }

    .img, .text {
      flex: 6;
      text-align: center;
    }

    img {
      max-width: 25px;
      max-height: 30px;
    }

    .text {
      word-break: break-word;
      text-overflow: ellipsis;
      overflow: hidden;
    }

    &+.handle-list {
      border-top: 1px solid $gray-bd;
    }
  }
}
</style>
