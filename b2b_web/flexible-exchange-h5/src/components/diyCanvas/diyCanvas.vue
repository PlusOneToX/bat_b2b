<!--
 * @Author: yaowei
 * @Date: 2019-05-08 10:22:11
 * @LastEditors: yaowei
 * @LastEditTime: 2019-12-09 10:49:49
-->
<template>
  <div>
    <div
      class="canvas-wrapper"
      v-show="!showTemplate"
      :style="'height:' + wheight + 'px'"
    >
      <!-- 框图 -->
      <img
        :src="bgimg"
        alt
        class="bgimg"
        id="bgimg"
        :width="pwidth"
        :height="pheight"
      />
      <!-- 打印 canvas -->
      <canvas
        id="printCanvas"
        class="canvas"
        :class="{
          clip: isZFold,
        }"
      ></canvas>

      <canvas
        id="myCanvas"
        class="canvas"
        :class="{
          module: curPicType === 3,
          show: showDashBorder && curPicType !== 3 && this.dragArr.length > 0,
          clip: isZFold,
        }"
      ></canvas>
      <canvas
        id="upCanvas"
        class="upcanvas"
        :style="{
          marginLeft: isZFold ? -pwidth / 4 + 'px' : 0,
        }"
      ></canvas>
      <!-- 虚线框 -->
      <div
        v-show="showDashBorder && curPicType !== 3 && this.dragArr.length > 0"
        class="dash-border"
        :style="{
          width: isZFold ? pwidth / 2 + 'px' : pwidth + 'px',
          height: pheight + 'px',
          marginLeft: isZFold ? -pwidth / 4 + 'px' : 0,
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
              left: item.x1 + 'px',
              width: item.x2 - item.x1 + 'px',
              height: item.y2 - item.y1 + 'px',
            }"
          >
            <span
              class="sprite-icon icon-add_picture"
              :class="{ none: item.url !== '' }"
            ></span>
            <div class="tips" :class="{ show: hasClickedArea }">
              <p @click="handleReplace()">
                <span class="sprite-icon icon-tihuan"></span>换图
              </p>
              <p @click="goEdit(item)">
                <span class="sprite-icon icon-bianji"></span>编辑
              </p>
            </div>
          </div>
        </template>

        <!-- 添加图片 -->
        <div
          class="add-img"
          :style="{
            marginLeft: isZFold ? -pwidth / 4 + 'px' : 0,
          }"
          v-show="
            canUpload &&
            canEdit &&
            Number(curPicType) !== 3 &&
            dragArr.length <= 0
          "
          @click="chooseImg"
        ></div>
      </div>

      <p
        class="move-tips"
        v-show="
          canEdit &&
          showDashBorder &&
          Number(curPicType) !== 3 &&
          dragArr.length > 0
        "
      >
        提示：请确保图片铺满红色虚线框，
        <br />图片未铺满区域不打印
      </p>

      <template v-if="canEdit && Number(curPicType) === 1">
        <!-- 图层图标 -->
        <div
          class="handle-wrap"
          v-show="!showHandleWrap && dragArr && dragArr.length > 0"
          @click="handleShowWrap"
        >
          <div class="layer-icon">
            <span class="sprite-icon icon-layer"></span>
          </div>
        </div>
        <!-- 图层列表 -->
        <div class="handle-layer" :class="{ show: showHandleWrap }">
          <div class="title">
            <div class="text">调整图层</div>
            <van-icon name="close" @click="handleCloseWrap" />
          </div>
          <div class="handle-list-wrap">
            <div
              class="handle-list"
              v-for="(item, index) in reverseData"
              :key="index"
            >
              <span
                class="sprite-icon icon-delete close-btn"
                @click="handleDelete(item)"
              ></span>
              <template v-if="item.type === 1">
                <span class="type" v-if="item.cateType === 1">本地</span>
                <span class="type" v-else-if="item.cateType === 2">贴纸</span>
                <span class="type" v-else>素材</span>
                <div class="img">
                  <div class="img-wrap">
                    <img :src="item.url.src" alt="" />
                  </div>
                </div>
              </template>
              <template v-else>
                <span class="type">文字</span>
                <span class="text">{{ item.filltext }}</span>
              </template>
              <div class="move" @click="handleMove(item, 'up')">
                <span
                  class="sprite-icon icon-move_up"
                  v-show="item.index !== dragArr.length - 1"
                ></span>
              </div>
              <div class="move" @click="handleMove(item, 'down')">
                <span
                  class="sprite-icon icon-move_down"
                  v-show="item.index !== 0"
                ></span>
              </div>
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
// 组件
import editKonva from "./editTemplate.vue";
// api
import api from "api/allApi.js";

var WIDTH = 0; // myCanvas画布宽
var HEIGHT = 0; // myCanvas画布高
var PWIDTH = 0; // 手机模型宽
var PHEIGHT = 0; // 手机模型高
var RPWIDTH = 0; // ratio手机模型宽
var RPHEIGHT = 0; // ratio手机模型高
var c2, ctx2, c, ctx, printC, printCtx;
var delIcon = new Image();
var scaleIcon = new Image();
var bimg = new Image();
var obj = {}; // 定义一个对象
var istouch = false;
var canvasRatio = 1;
var modelCenter = {}; // 模板中心点位置，用于计算画布可点击区域
var modelArea = []; // 模板热区位置，用于计算画布可点击区域
let canEdit = true; // 是否可编辑图层（详情也不可编辑）
let isZFold = false; 

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
    printCtx.save();
    this.centerX = this.x * canvasRatio + (this.w * canvasRatio) / 2;
    this.centerY = this.y * canvasRatio + (this.h * canvasRatio) / 2;
    // 旋转元素
    ctx.translate(this.centerX, this.centerY);
    ctx.rotate((this.rotate * Math.PI) / 180);
    ctx.translate(-this.centerX, -this.centerY);
    printCtx.translate(this.centerX, this.centerY);
    printCtx.rotate((this.rotate * Math.PI) / 180);
    printCtx.translate(-this.centerX, -this.centerY);
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
    if (isZFold) {
      c3.width = (WIDTH - PWIDTH / 2) * canvasRatio;
      c3.style.width = WIDTH - PWIDTH / 2 + "px";
    }
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
      printCtx.drawImage(
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
      if (canEdit) {
        this.drawIcon(this, ctx3);
      }
      let pat = ctx2.createPattern(c3, "no-repeat");
      ctx2.fillStyle = pat;
      ctx2.globalCompositeOperation = "source-atop";

      ctx2.fillRect(0, 0, RPWIDTH, RPHEIGHT);
      ctx3.clearRect(0, 0, c3.width, c3.height);
    } else if (this.type === 2) {
      this.fontsize = this.h * canvasRatio;
      ctx.font = this.fontsize + "px " + this.fontfamily;
      ctx.fillStyle = this.fillstyle;
      ctx.textBaseline = "top";
      ctx.textAlign = "left";
      ctx.fillText(this.filltext, this.x * canvasRatio, this.y * canvasRatio);
      printCtx.font = this.fontsize + "px " + this.fontfamily;
      printCtx.fillStyle = this.fillstyle;
      printCtx.textBaseline = "top";
      printCtx.textAlign = "left";
      printCtx.fillText(
        this.filltext,
        this.x * canvasRatio,
        this.y * canvasRatio
      );
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
      if (canEdit) {
        this.drawIcon(this, ctx2);
      }
    }
    // 如果是选中状态，绘制选择虚线框，和缩放图标、删除图标
    this.drawIcon(this, null);
    c.style.zIndex = "5";
    c.style.opacity = 1;
    printC.style.zIndex = "5";
    printC.style.opacity = 1;
    ctx.restore();
    ctx2.restore();
    printCtx.restore();
  }
  // 绘制IP图/模板图
  paintIpImage() {
    ctx.save();
    ctx2.save();
    printCtx.save();
    this.centerX = this.x + this.w / 2;
    this.centerY = this.y + this.h / 2;
    // 旋转元素
    ctx.translate(this.centerX, this.centerY);
    ctx.rotate((this.rotate * Math.PI) / 180);
    ctx.translate(-this.centerX, -this.centerY);
    printCtx.translate(this.centerX, this.centerY);
    printCtx.rotate((this.rotate * Math.PI) / 180);
    printCtx.translate(-this.centerX, -this.centerY);
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
    if (isZFold) {
      c3.width = (WIDTH - PWIDTH / 2) * canvasRatio;
      c3.style.width = WIDTH - PWIDTH / 2 + "px";
    }
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
      printCtx.drawImage(
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
      ctx2.fillRect(0, 0, RPWIDTH, RPHEIGHT);
      ctx3.clearRect(0, 0, c3.width, c3.height);
    }
    c.style.zIndex = "-1";
    c.style.opacity = 0;
    printC.style.zIndex = "-1";
    printC.style.opacity = 0;
    ctx.restore();
    ctx2.restore();
    printCtx.restore();
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
    // 图库类型
    curPicType: {
      type: Number,
      default: 0,
    },
    // 是否可编辑（详情不可编辑）
    canEdit: {
      type: Boolean,
      default: true,
    },
    // 是否允许用户上传
    canUpload: {
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
      isDown: false,
      isDrag: false, // 图片类型是否可编
      showHandleWrap: false, // 是否显示图层列表
      clickedArea: [], // 点击热区
      clickedNum: 0, // 点击次数，用于判断单击/双击
      clickedTimer: null,
      curClicked: "", // 当前点击热区
      showTemplate: false, // 是否显示编辑模板
      curItem: {}, // 当前点击热区信息
      hasClickedArea: false, // 是否点击热区，用于显示“双击进入编辑”提示语
      reverseData: [], // 图层列表（倒序显示）
      showDashBorder: false, // 是否显示虚线框
      curTimeout: "", // 编辑器隐藏定时器
      delIcon:
        "data:image/png;base64,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",
      scaleIcon:
        "data:image/png;base64,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",
      // 透明间距
      frameValue: {},
      isZFold: false, 
    };
  },
  mounted() {
    canEdit = this.canEdit;
    // 画布大小
    WIDTH = window.screen.width;
    HEIGHT = this.wheight;

    delIcon.src = this.delIcon;
    scaleIcon.src = this.scaleIcon;
    this.userId = localStorage.getItem("userId");

    // 获取当前的屏幕像素密度比例
    canvasRatio = this.getPixelRatio();
  },
  activated() {
    canEdit = this.canEdit;
    // 画布大小
    WIDTH = window.screen.width;
    HEIGHT = this.wheight;
  },
  methods: {
    draw() {
      this.clear();
      if (this.dragArr.length > 0) {
        ctx2.save();
        bimg = document.getElementById("bgimg");
        ctx2.drawImage(bimg, 0, 0, RPWIDTH, RPHEIGHT);
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
      bimg = document.getElementById("bgimg");
      ctx2.drawImage(bimg, 0, 0, RPWIDTH, RPHEIGHT);
      this.dragArr.forEach((item) => {
        item.paintIpImage();
      });
    },
    start(e) {
      // e.preventDefault();
      if (this.canEdit && this.isDrag && this.dragArr.length > 0) {
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
          let place = item.isInWhere(x, y);
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

            // 判断是否删除的是素材图
            if (lastImg.type === 1 && lastImg.picId) {
              // 是，清空图片id，重新获取型号
              this.$parent.picId = "";
              this.$parent.getBrand("changePic");
            }

            if (this.dragArr.length <= 0) {
              this.showHandleWrap = false;
            }

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
      if (this.canEdit && this.isDown && this.isDrag) {
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
      if (this.dragArr.length > 0) {
        // 剪切图片
        let cutImage = printCtx.getImageData(
          c.width / 2 - RPWIDTH / 2,
          c.height / 2 - RPHEIGHT / 2,
          RPWIDTH,
          RPHEIGHT
        );
        let testC = document.createElement("canvas");
        let testCtx = testC.getContext("2d");
        testC.width = RPWIDTH;
        testC.height = RPHEIGHT;

        // 将截图内容转化base64
        testCtx.putImageData(cutImage, 0, 0, 0, 0, RPWIDTH, RPHEIGHT);
        let newImage = testC.toDataURL("image/png", 0.8);
        testCtx.clearRect(0, 0, RPWIDTH, RPHEIGHT);

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
      nCanvas.width = RPWIDTH;
      nCanvas.height = RPHEIGHT;
      Promise.all([
        this.loadImage(this.bgimg, 0),
        this.loadImage(newImage, 1),
        this.loadImage(this.upimg, 2),
      ])
        .then((imgs) => {
          nCtx.drawImage(imgs[0], 0, 0, RPWIDTH, RPHEIGHT);
          // 图片背景
          let scanvas = document.createElement("canvas");
          let sctx = scanvas.getContext("2d");
          scanvas.width = RPWIDTH;
          scanvas.height = RPHEIGHT;
          scanvas.style.opacity = 0;
          nCtx.globalCompositeOperation = "source-atop";
          let cutX = 0;
          if (this.isZFold) {
            cutX = -imgs[1].width / 4;
          }
          sctx.drawImage(imgs[1], cutX, 0, RPWIDTH, RPHEIGHT);
          let pat = nCtx.createPattern(scanvas, "no-repeat");
          nCtx.fillStyle = pat;
          nCtx.fillRect(0, 0, RPWIDTH, RPHEIGHT);
          if (this.isZFold) {
            nCtx.globalCompositeOperation = "source-over";
          }
          nCtx.drawImage(imgs[2], 0, 0, RPWIDTH, RPHEIGHT);
          let image = this.convertCanvasToImage(nCanvas);
          sctx.clearRect(0, 0, RPWIDTH, RPHEIGHT);
          nCtx.clearRect(0, 0, RPWIDTH, RPHEIGHT);

          if (Number(this.materialsType) === 2 && this.manufactor === "MK") {
            this.getCutImg(image);
          } else {
            let pdfImg = this.getCutImg(); // 剪切图
            this.importFile(image, pdfImg);
          }
        })
        .catch((error) => {
          this.message = "";
          this.isLoading = false;
          this.$toast("您手机当前的网络较差，图片上传失败，请重试~");
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
      let cX = (this.frameValue.leftFrame / 25.4) * this.imgDpi;
      let cY = (this.frameValue.topFrame / 25.4) * this.imgDpi;
      let cwidth = ((this.mw - this.frameValue.vFrame) / 25.4) * this.imgDpi;
      let cheight = ((this.mh - this.frameValue.hFrame) / 25.4) * this.imgDpi;

      let mwidth = (this.mw / 25.4) * this.imgDpi;
      let mheight = (this.mh / 25.4) * this.imgDpi;
      nCanvas.width = mwidth;
      nCanvas.height = mheight;

      Promise.all([this.loadImage(this.bgimg, 0), this.loadImage(newImage, 1)])
        .then((imgs) => {
          // 底图（TPU圆角）
          nCtx.drawImage(imgs[0], cX, cY, cwidth, cheight);
          // 切图
          let scanvas = document.createElement("canvas");
          let sctx = scanvas.getContext("2d");
          scanvas.width = mwidth;
          scanvas.height = mheight;
          scanvas.style.opacity = 0;
          sctx.drawImage(imgs[1], 0, 0, mwidth, mheight);
          let pat = nCtx.createPattern(scanvas, "no-repeat");
          nCtx.fillStyle = pat;
          nCtx.globalCompositeOperation = "source-in";
          nCtx.fillRect(0, 0, mwidth, mheight);

          let cutImage = nCtx.getImageData(0, 0, mwidth, mheight);
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
          tC.width = mwidth;
          tC.height = mheight;
          tCtx.putImageData(cutImage, 0, 0, cX, cY, cwidth, cheight);

          // 将截图内容转化base64
          let image = tC.toDataURL("image/png", 0.8);
          tCtx.clearRect(0, 0, mwidth, mheight);

          // 合成后上传
          this.importFile(imgUrl, image);
        })
        .catch((error) => {
          this.message = "";
          this.isLoading = false;
          this.$toast("您手机当前的网络较差，图片上传失败，请重试~");
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
      let mwidth = (this.mw / 25.4) * this.imgDpi;
      if (this.isZFold) {
        mwidth = (this.mw / 2 / 25.4) * this.imgDpi;
      }
      let mheight = (this.mh / 25.4) * this.imgDpi;
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
          let wpx2 = mwidth;
          let hpx2 = mheight;
          wpx = ((this.nw + 2) / 25.4) * this.imgDpi;
          hpx = ((this.nh + 2) / 25.4) * this.imgDpi;
          if (this.isZFold) {
            wpx = ((this.nw + 2) / 2 / 25.4) * this.imgDpi;
          }
          this.diffX = (wpx2 - wpx) / 2;
          this.diffY = (hpx2 - hpx) / 2;

          // MK 玻璃壳/热升华---水平镜像
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
          wpx = mwidth;
          hpx = mheight;
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
          tC.width = mwidth;
          tC.height = mheight;
          // 透明边距
          let difX = (this.frameValue.leftFrame / 25.4) * this.imgDpi;
          let difY = (this.frameValue.topFrame / 25.4) * this.imgDpi;
          let w = ((this.mw - this.frameValue.vFrame) / 25.4) * this.imgDpi;
          let h = ((this.mh - this.frameValue.hFrame) / 25.4) * this.imgDpi;
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
      if (this.isZFold) {
        phonex = c.width / canvasRatio / 2 - this.pwidth2 / 4;
      }
      let phoney = c.height / canvasRatio / 2 - this.pheight2 / 2;
      // 手机当前dpi的尺寸
      let phonew = (this.mw / 25.4) * this.imgDpi;
      if (this.isZFold) {
        phonew = (this.mw / 2 / 25.4) * this.imgDpi;
      }
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
      if (this.isZFold) {
        icw = this.mw / 2 / 25.4;
      }
      let ich = this.mh / 25.4;
      // 得到缩放倍数
      let scalew = item.initw / item.w;
      let scaleh = item.inith / item.h;
      let scale = scalew > scaleh ? scaleh : scalew;
      let dpi = 0;
      if (item.url.naturalWidth / icw > item.url.naturalHeight / ich) {
        // 获取图片dpi和获取计算dpi的手机尺寸
        dpi = item.url.naturalHeight / ich;
      } else {
        dpi = item.url.naturalWidth / icw;
      }
      this.imgDpi = Math.round(dpi * scale);
      // 设置图片当前dpi
      this.$set(item, "dpi", this.imgDpi);

      // 限制图片dpi不能过大，最大dpi 300
      this.imgDpi = this.imgDpi > 300 ? 300 : this.imgDpi;
      this.$emit("dpi", this.imgDpi, scale);
    },
    importFile(image, pdfimg) {
      // 折叠屏
      let cutInfo = sessionStorage.getItem("cutInfo");
      if (cutInfo) {
        cutInfo = JSON.parse(cutInfo);
      }

      let nw = Number(sessionStorage.getItem("nw"));
      let nh = Number(sessionStorage.getItem("nh"));

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

        let width = nw;
        if (
          cutInfo &&
          Number(cutInfo.cutType) === 2 &&
          cutInfo.crosscuttingWhite
        ) {
          // 折叠屏--横向
          width = nw + 2 + cutInfo.crosscuttingWhite;
        } else {
          if (
            Number(this.materialsType === 2) ||
            Number(this.materialsType === 4)
          ) {
            width = nw;
          } else {
            // 玻璃壳/热升华+2mm
            width = nw + 2;
          }
        }

        let height = nh;
        if (cutInfo && Number(cutInfo.cutType) === 1 && cutInfo.slittingWhite) {
          // 折叠屏--纵向
          height = nh + 2 + cutInfo.slittingWhite;
        } else {
          if (
            Number(this.materialsType === 2) ||
            Number(this.materialsType === 4)
          ) {
            height = nh;
          } else {
            // 玻璃壳/热升华+2mm
            height = nh + 2;
          }
        }

        if (this.isZFold) {
          width = width / 2;
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
        this.uploadFile(imgFile, pdfFile, "duihuan");
      } else {
        this.uploadFile(imgFile, pdfFile, "duihuan_test");
      }
    },
    uploadFile(img, pdf, name) {
      this.$emit("handleLoading", true, "作品上传中");

      let that = this;
      let imgUrl, pdfUrl;
      let uid = window.localStorage.getItem("userId");

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
              "customize/" +
                name +
                "/" +
                currentDate +
                "/" +
                uid +
                "/" +
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
                    "customize/" +
                      name +
                      "/" +
                      currentDate +
                      "/" +
                      uid +
                      "/" +
                      randomNamePDF,
                    pdf,
                    {
                      // // 上传文件进度
                      // progress: function* (percentage) {
                      //   let fileloadingNum = Math.ceil(percentage * 100) + "%";
                      //   that.$emit("handleLoading", true, fileloadingNum);
                      //   console.log(fileloadingNum);
                      // },
                    }
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
        let allCtx = canvas[i].getContext("2d");
        allCtx.clearRect(0, 0, canvas[i].width, canvas[i].height);
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

      this.$parent.showModel = false;
      this.$parent.showMask = false;
      this.$parent.showMaterial = false;
      this.$parent.showPicture = false;
      this.$parent.getDrawSize();
      this.$parent.textDialog = false;
      this.$parent.mapDialog = false;
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
                item.x1 <= e.x &&
                e.x <= item.x2 &&
                item.y1 <= e.y &&
                e.y <= item.y2
              ) {
                this.curClicked = index;
                // 显示换图/编辑提示窗
                if (item.url) {
                  this.hasClickedArea = true;
                } else {
                  // 上传本地图片
                  this.chooseImg();
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
        this.chooseImg();
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

      let cwidth = WIDTH * canvasRatio;
      let cheight = HEIGHT * canvasRatio;
      c = document.getElementById("myCanvas");
      ctx = c.getContext("2d");
      c.width = cwidth;
      c.height = cheight;
      c.style.width = WIDTH + "px";
      c.style.height = HEIGHT + "px";
      if (this.isZFold) {
        c.width = (WIDTH - this.pwidth / 2) * canvasRatio;
        c.style.width = WIDTH - this.pwidth / 2 + "px";
      }

      c2 = document.getElementById("upCanvas");
      ctx2 = c2.getContext("2d");
      c2.width = RPWIDTH;
      c2.height = RPHEIGHT;
      c2.style.width = this.pwidth + "px";
      c2.style.height = this.pheight + "px";
      if (this.isZFold) {
        c2.width = RPWIDTH / 2;
        c2.style.width = this.pwidth / 2 + "px";
      }

      // 打印 canvas
      printC = document.getElementById("printCanvas");
      printCtx = printC.getContext("2d");
      printC.width = cwidth;
      printC.height = cheight;
      printC.style.width = WIDTH + "px";
      printC.style.height = HEIGHT + "px";
      if (this.isZFold) {
        printC.width = (WIDTH - this.pwidth / 2) * canvasRatio;
        printC.style.width = WIDTH - this.pwidth / 2 + "px";
      }
    },
    // 限制移动范围
    limitMove(touchEvent, initial, startTouch, lastImg) {
      // 获取画布坐标
      let canvasPoint = this.getCanvasPoint();

      let { x, y } = touchEvent;
      let { initialX, initialuX } = initial;
      let { startX, startY } = startTouch;

      // 限制垂直移动范围
      this.handleMoveY(y, initial, startY, lastImg, canvasPoint);
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
        }
      } else {
        lastImg.x = initialX + (x - startX);
        lastImg.ux = initialuX + (x - startX);
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
      if (this.isZFold) {
        centerCX = (window.screen.width - this.pwidth / 2) / 2;
      }
      let centerCY = this.$parent.wheight / 2;
      // 计算画布坐标
      return {
        centerCX: centerCX, // 画布中心点x
        centerCY: centerCY, // 画布中心点y
        x1: this.isZFold
          ? centerCX - this.pwidth2 / 4
          : centerCX - this.pwidth2 / 2, // 左上角x
        x2: this.isZFold
          ? centerCX + this.pwidth2 / 4
          : centerCX + this.pwidth2 / 2, // 右上角x
        y1: centerCY - this.pheight2 / 2, // 左上角y
        y2: centerCY + this.pheight2 / 2, // 左下角y
      };
    },
    // 上传本地图片
    chooseImg() {
      this.$emit("chooseImg");
    },
  },
  watch: {
    dragArr(arr) {
      arr.forEach((item, index) => {
        item.index = index;
      });
      this.reverseData = arr.slice().reverse(); // 将当前图层倒序
      return arr;
    },
    // 监听画布宽度
    wwidth(value) {
      this.wwidth = value;
    },
    spriteArr(arr) {
      this.initCanvas();
      if (arr.length > 0) {
        this.spriteArr = arr;
        // 获取最新添加的数据
        let newImg = arr.slice(-1)[0];
        let item = new DragImg(newImg, ctx);
        // 判断图片类型  0-网络图 1-普通素材 2-IP素材（不可更改） 3-模板（不可更改）
        if (parseInt(newImg.picType) === 2) {
          // 重置点击热区
          this.clickedArea = [];

          // IP图，直接替换，不可移动
          if (parseInt(newImg.cateType) === 1) {
            this.isDrag = true;

            this.$parent.curPicType = 1;
          } else {
            this.isDrag = false;
          }
          this.dragArr = [];
          this.dragArr.push(item);
          this.drawIpImage();
          this.getImgDPI(item);
        } else if (parseInt(newImg.picType) === 3) {
          // 模板
          this.isDrag = true;
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
          let cy = this.wheight / 2 - modelCenter.uy;
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
          // 重置点击热区
          this.clickedArea = [];
          // 普通素材/贴图
          this.isDrag = true;

          // 普通素材，清空画布
          if (newImg.picId && newImg.picType !== 4) {
            this.dragArr = [];
          }
          if (this.dragArr.length > 0) {
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
                  if (
                    parseInt(newImg.picType) !== 4 &&
                    parseInt(item.picType) !== 4
                  ) {
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
          }

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
      isZFold = Number(sessionStorage.getItem("isZFold")); 
      this.isZFold = isZFold;

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
      RPWIDTH = value * canvasRatio;
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
      RPHEIGHT = value * canvasRatio;
    },
    bgimg(value) {
      this.bgimg = value;
      if (
        Number(this.materialsType) === 2 ||
        Number(this.materialsType) === 4
      ) {
        // 透明边距
        let frameValue = JSON.parse(sessionStorage.getItem("frameValue"));
        let vFrame = 0;
        let hFrame = 0;
        if (frameValue) {
          vFrame = Number(frameValue.leftFrame) + Number(frameValue.rightFrame);
          hFrame = Number(frameValue.topFrame) + Number(frameValue.underFrame);
        }

        this.frameValue = {
          topFrame: Number(frameValue.topFrame) || 0,
          rightFrame: Number(frameValue.rightFrame) || 0,
          underFrame: Number(frameValue.underFrame) || 0,
          leftFrame: Number(frameValue.leftFrame) || 0,
          vFrame: vFrame,
          hFrame: hFrame,
        };
      }
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

<style lang="stylus" scoped>
@import '~common/styles/variable.styl';
@import '~common/styles/mixin.styl';

.canvas-wrapper {
  position: relative;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;

  .bgimg {
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate3d(-50%, -50%, 0);
    background-repeat: no-repeat;
  }

  #myCanvas, #printCanvas {
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

    &.clip {
      left: 0;
      transform: translateX(0);
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
    background-repeat: no-repeat;
    background-position: 50% 50%;
    background-clip: content-box;
    z-index: 10;

    .icon {
      position: absolute;
      top: -10px;
      left: -10px;
      font-size: $font-xl;
      color: $color-font-white;
      background-color: $color-red;
      border-radius: 50%;
    }
  }

  .add-img {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 80px;
    lineHeight(80px);
    align(center);
    border: 1px dashed $color-line;
    border-radius: $radius-sm;
    transform: translate3d(-50%, -50%, 0);

    &::before, &::after {
      display: block;
      content: '';
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate3d(-50%, -50%, 0);
      background-color: $color-line;
      border-radius: 3px;
    }

    &::before {
      width: 2px;
      height: 60%;
    }

    &::after {
      width: 60%;
      height: 3px;
    }
  }

  .dash-border {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate3d(-50%, -50%, 0);
    border: 1px dashed $color-red;
    z-index: 9;
  }

  .move-tips {
    position: absolute;
    top: 6px;
    left: 50%;
    width: 100%;
    font-size: $font-sm;
    color: $color-red;
    align(center);
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

  .icon-add_picture {
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
    font-size: $font-sm;
    color: $color-font-white;
    text-align: center;
    background-color: rgba(49, 49, 49, 0.9);
    border-radius: 10px;
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

.handle-wrap {
  position: fixed;
  top: 200px;
  right: 0;
  height: 124px;
  z-index: 20;

  .layer-icon {
    position: absolute;
    top: 0;
    right: 0;
    width: 60px;
    height: 48px;
    background-color: $color-main-lighter;
    border-radius: 48px 0 0 48px;

    > .sprite-icon {
      position: absolute;
      top: 50%;
      left: 16px;
      transform: translateY(-50%);
    }
  }
}

.handle-layer {
  position: fixed;
  left: 0;
  right: 0;
  bottom: -100%;
  padding-bottom: 30px;
  background-color: $color-bg-white;
  border-radius: $radius-lg $radius-lg 0 0;
  z-index: 101;
  box-shadow: -1px -2px 8px -2px rgba(153, 153, 153, 0.3);
  transition: all 0.3s;
  overflow: hidden;

  &.show {
    bottom: 0;
  }

  .title {
    position: relative;
    width: 100%;
    padding: $spacing-lg $spacing-base;
    lineHeight(25px);
    margin-bottom: $spacing-base;
    background-color: $color-bg-white;

    .text {
      display: inline-block;
      font-size: $font-lg;
      color: $color-font-base;
      font-weight: 500;
    }

    .van-icon {
      display: inline-block;
      font-size: 25px;
      float: right;
    }
  }

  .handle-list-wrap {
    position: relative;
    max-height: 350px;
    padding: 0 $spacing-base;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;

    &::-webkit-scrollbar {
      display: none;
    }
  }

  .handle-list {
    padding: 0 $spacing-base;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: $font-base;

    &:hover {
      background-color: rgbaMain(0.2);
      border-radius: $radius-base;
    }

    .sprite-icon {
      top: 0;
    }

    .move {
      flex: 2;
      align(center);
    }

    .type {
      flex: 2;
      align(center);
    }

    .img, .text {
      flex: 5;
      text-align: center;
    }

    .img-wrap {
      position: relative;
      left: 50%;
      width: 40px;
      height: 40px;
      border-radius: $radius-xs;
      overflow: hidden;
      transform: translateX(-50%);
    }

    img {
      position: absolute;
      top: 50%;
      left: 0;
      width: 100%;
      transform: translateY(-50%);
    }

    .text {
      padding: 0 $spacing-base;
      white-space: nowrap;
      text-overflow: ellipsis;
      overflow: hidden;
    }
  }
}
</style>
