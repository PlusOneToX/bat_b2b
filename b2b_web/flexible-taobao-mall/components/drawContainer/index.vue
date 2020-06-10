<!--
 * @Author: yaowei
 * @Date: 2019-12-22 16:36:50
 * @LastEditors: yaowei
 * @LastEditTime: 2019-05-24 11:37:06
-->

<template>
  <div>
    <div class="canvas-wrapper" v-show="!showTemplate">
      <!-- 底图 -->
      <image
        mode="aspectFill"
        class="bgimg"
        id="bgimg"
        :src="bgimg"
        :style="{ width: pwidth + 'px', height: pheight + 'px' }"
      ></image>
      <!-- 折叠屏 -->
      <canvas
        canvas-id="foldingCanvas"
        id="foldingCanvas"
        class="foldingCanvas"
        :style="{ width: foldingWidth + 'px', height: foldingHeight + 'px' }"
      ></canvas>
      <!-- 隐藏打印canvas -->
      <canvas
        canvas-id="hiddenCanvas"
        id="hiddenCanvas"
        class="hiddenCanvas"
        :style="{ width: hiddenWidth + 'px', height: hiddenHeight + 'px' }"
      ></canvas>
      <!-- 隐藏预览canvas -->
      <canvas
        canvas-id="previewCanvas"
        id="previewCanvas"
        class="previewCanvas"
        :width="parseInt(pwidth * pixelRatio)"
        :height="parseInt(pheight * pixelRatio)"
        :style="{ width: pwidth + 'px', height: pheight + 'px' }"
      ></canvas>
      <!-- 移动元素列表 -->
      <view
        v-if="lastImg && isMove"
        class="moveList"
        :class="{ hidden: !isMove }"
        :style="{ width: myWidth + 'px', height: myHeight + 'px' }"
      >
        <view v-for="(item, index) in dragArr" :key="index">
          <!-- 当前移动元素 -->
          <view v-if="lastImg.index === index">
            <!-- 操作图标（删除/缩放） -->
            <view
              :style="{
                position: 'absolute',
                top: lastImg.y + 'px',
                left: lastImg.x + 'px',
                width: lastImg.w + 'px',
                height: lastImg.h + 'px',
                transform: 'rotate(' + lastImg.rotate + 'deg)',
                'z-index': 1,
              }"
            >
              <image
                src="data:image/png;base64,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"
                :style="{
                  position: 'absolute',
                  top: '-12px',
                  left: '-12px',
                  width: '24px',
                  height: '24px',
                  'z-index': 2,
                }"
                mode="aspectFill"
              ></image>
              <image
                src="data:image/png;base64,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"
                :style="{
                  position: 'absolute',
                  right: '-12px',
                  bottom: '-12px',
                  width: '24px',
                  height: '24px',
                  'z-index': 2,
                }"
                mode="aspectFill"
              ></image>
            </view>

            <image
              v-show="lastImg.url"
              :src="lastImg.url.src"
              :style="{
                position: 'absolute',
                top: lastImg.y + 'px',
                left: lastImg.x + 'px',
                width: lastImg.w + 'px',
                height: lastImg.h + 'px',
                transform: 'rotate(' + lastImg.rotate + 'deg)',
              }"
              mode="aspectFill"
            >
            </image>
            <text
              v-show="lastImg.filltext"
              :style="{
                position: 'absolute',
                top: lastImg.y + 'px',
                left: lastImg.x + 'px',
                'font-size': lastImg.h + 'px',
                'font-family': lastImg.fontfamily,
                color: lastImg.fillstyle,
                transform: 'rotate(' + lastImg.rotate + 'deg)',
              }"
              >{{ lastImg.filltext }}</text
            >
          </view>
          <!-- 其他元素 -->
          <view v-else>
            <image
              v-show="item.url"
              :src="item.url.src"
              :style="{
                position: 'absolute',
                top: item.y + 'px',
                left: item.x + 'px',
                width: item.w + 'px',
                height: item.h + 'px',
                transform: 'rotate(' + item.rotate + 'deg)',
              }"
              mode="aspectFill"
            ></image>
            <text
              v-show="item.filltext"
              :style="{
                position: 'absolute',
                top: item.y + 'px',
                left: item.x + 'px',
                'font-size': item.h + 'px',
                'font-family': item.fontfamily,
                color: item.fillstyle,
                transform: 'rotate(' + item.rotate + 'deg)',
              }"
              >{{ item.filltext }}</text
            >
          </view>
        </view>
      </view>
      <canvas
        canvas-id="myCanvas"
        id="myCanvas"
        class="myCanvas"
        :class="[{ module: curPicType !== 1 }, { hidden: isMove }]"
        :style="{
          width: myWidth + 'px',
          height: myHeight + 'px',
        }"
      ></canvas>
      <canvas
        canvas-id="upCanvas"
        id="upCanvas"
        class="upCanvas"
        :class="{ hidden: isMove }"
        :width="parseInt(pwidth * pixelRatio)"
        :height="parseInt(pheight * pixelRatio)"
        :style="{
          width: pwidth + 'px',
          height: pheight + 'px',
        }"
      ></canvas>
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
        <!-- 添加图片 -->
        <div
          class="add-img"
          v-show="canUpload && Number(curPicType) !== 3 && dragArr.length <= 0"
          @click.stop="chooseImg"
        ></div>
      </div>
    </div>

    <!-- 多图层 -->
    <!-- <template v-if="Number(curPicType) === 1">
			<div class="layer-icon" v-show="!showHandleWrap && dragArr && dragArr.length > 0" @click="handleShowWrap">
				<span class="sprite-icon icon_layer"></span>
			</div>
			<div class="handle-layer" :class="{show: showHandleWrap && dragArr && dragArr.length > 0}">
				<div class="close-handle" @click="handleCloseWrap">
					<u-icon name="close" />
				</div>
				<div class="handle-list" v-for="(item, index) in reverseData" :key="index">
					<view class="close-btn">
						<u-icon name="close-circle-fill" @click="handleDelete(item)" />
					</view>
					<template v-if="item.type === 1">
						<span class="type" v-if="item.cateType === 1">本地</span>
						<span class="type" v-else-if="item.cateType === 2">贴图</span>
						<span class="type" v-else>素材</span>
						<div class="img">
							<image mode="aspectFill" :src="item.url.src" alt="" />
						</div>
					</template>
					<template v-else>
						<span class="type">文字</span>
						<span class="text">{{ item.filltext }}</span>
					</template>
					<div class="move" @click="handleMove(item, 'up')">
						<view class="sprite-icon layer_icon_Moveup" v-show="item.index !== dragArr.length - 1"></view>
					</div>
					<div class="move" @click="handleMove(item, 'down')">
						<view class="sprite-icon layer_icon_Movedown" v-show="item.index !== 0"></view>
					</div>
				</div>
			</div>
		</template> -->

    <view class="loading-wrap" v-show="showLoading">
      <view class="loading-content">
        <u-loading mode="flower" size="100"></u-loading>
        <text class="loading-msg" v-show="loadingMsg">{{ loadingMsg }}</text>
      </view>
    </view>

    <!-- 提示 -->
    <u-toast ref="uToast" />
  </div>
</template>

<script type="text/ecmascript-6">
import store from "../../store";
import { mapState, mapGetters, mapActions, mapMutations } from "vuex";

var WIDTH = 0; // myCanvas画布宽
var HEIGHT = 0; // myCanvas画布高
var PWIDTH = 0; // 手机模型宽
var PHEIGHT = 0; // 手机模型高
var ctx, ctx2, ctx3, ctx4;
var delIcon =
  "data:image/png;base64,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";
var scaleIcon =
  "data:image/png;base64,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";
var istouch = false;
var cutWidth = 0; // 导出图片宽
var cutHeight = 0; // 导出图片高
var cutScale = 1; // 导出图片倍数
var fontScale = 1; // 导出字体倍数
var bgImg = "";
var pixelRatio = 1; // 屏幕密度比dpr

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
    ctx3.save();
    ctx4.save();
    this.centerX = this.x + this.w / 2;
    this.centerY = this.y + this.h / 2;
    // 旋转元素
    ctx.translate(this.centerX, this.centerY);
    ctx.rotate((this.rotate * Math.PI) / 180);
    ctx.translate(-this.centerX, -this.centerY);
    // 显示壁纸层
    let centeruX = this.ux + this.w / 2;
    let centeruY = this.uy + this.h / 2;
    // 渲染元素 1-图片 2-文字
    if (this.type === 1) {
      let imgWidth = this.w;
      let imgheight = this.h;
      ctx.drawImage(this.url.src, this.x, this.y, imgWidth, imgheight);

      // 添加显示层
      ctx2.globalCompositeOperation = "source-atop";
      // #ifdef H5
      ctx2.translate(centeruX, centeruY);
      ctx2.rotate((this.rotate * Math.PI) / 180);
      ctx2.translate(-centeruX, -centeruY);
      ctx2.drawImage(this.url.src, this.ux, this.uy, imgWidth, imgheight);
      this.drawIcon(this, ctx2, 1);
      // #endif
      // #ifndef H5
      ctx2.translate(centeruX * pixelRatio, centeruY * pixelRatio);
      ctx2.rotate((this.rotate * Math.PI) / 180);
      ctx2.translate(-centeruX * pixelRatio, -centeruY * pixelRatio);
      ctx2.drawImage(
        this.url.src,
        this.ux * pixelRatio,
        this.uy * pixelRatio,
        imgWidth * pixelRatio,
        imgheight * pixelRatio
      );
      this.drawIcon(this, ctx2, pixelRatio);
      // #endif

      // 隐藏打印canvas
      let hiddenUx = this.ux * cutScale + (imgWidth * cutScale) / 2;
      let hiddenUy = this.uy * cutScale + (imgheight * cutScale) / 2; // 添加显示层
      ctx3.translate(hiddenUx, hiddenUy);
      ctx3.rotate((this.rotate * Math.PI) / 180);
      ctx3.translate(-hiddenUx, -hiddenUy);
      ctx3.drawImage(
        this.url.src,
        this.ux * cutScale,
        this.uy * cutScale,
        imgWidth * cutScale,
        imgheight * cutScale
      );

      // 隐藏预览canvas
      ctx4.globalCompositeOperation = "source-atop";
      // #ifdef H5
      ctx4.translate(centeruX, centeruY);
      ctx4.rotate((this.rotate * Math.PI) / 180);
      ctx4.translate(-centeruX, -centeruY);
      ctx4.drawImage(this.url.src, this.ux, this.uy, imgWidth, imgheight);
      // #endif
      // #ifndef H5
      ctx4.translate(centeruX * pixelRatio, centeruY * pixelRatio);
      ctx4.rotate((this.rotate * Math.PI) / 180);
      ctx4.translate(-centeruX * pixelRatio, -centeruY * pixelRatio);
      ctx4.drawImage(
        this.url.src,
        this.ux * pixelRatio,
        this.uy * pixelRatio,
        imgWidth * pixelRatio,
        imgheight * pixelRatio
      );
      // #endif
    } else if (this.type === 2) {
      // 文字
      this.fontsize = parseInt(this.h);
      ctx.font = this.fontsize + "px " + this.fontfamily;
      ctx.setFillStyle(this.fillstyle);
      ctx.setTextBaseline("top");
      ctx.setTextAlign("left");
      ctx.fillText(this.filltext, this.x, this.y);
      this.w = ctx.measureText(this.filltext).width;

      // 添加显示层
      // #ifdef H5
      ctx2.translate(centeruX, centeruY);
      ctx2.rotate((this.rotate * Math.PI) / 180);
      ctx2.translate(-centeruX, -centeruY);
      ctx2.font = this.fontsize + "px " + this.fontfamily;
      ctx2.setFillStyle(this.fillstyle);
      ctx2.setTextBaseline("top");
      ctx2.setTextAlign("left");
      ctx2.fillText(this.filltext, this.ux, this.uy);
      this.drawIcon(this, ctx2, 1);
      // #endif
      // #ifndef H5
      ctx2.translate(centeruX * pixelRatio, centeruY * pixelRatio);
      ctx2.rotate((this.rotate * Math.PI) / 180);
      ctx2.translate(-centeruX * pixelRatio, -centeruY * pixelRatio);
      ctx2.font = this.fontsize * pixelRatio + "px " + this.fontfamily;
      ctx2.setFillStyle(this.fillstyle);
      ctx2.setTextBaseline("top");
      ctx2.setTextAlign("left");
      ctx2.fillText(this.filltext, this.ux * pixelRatio, this.uy * pixelRatio);
      this.drawIcon(this, ctx2, pixelRatio);
      // #endif

      // 隐藏打印canvas
      let hiddenUx = this.ux * cutScale + (this.w * cutScale) / 2;
      let hiddenUy = this.uy * cutScale + (this.h * cutScale) / 2;
      ctx3.translate(hiddenUx, hiddenUy);
      ctx3.rotate((this.rotate * Math.PI) / 180);
      ctx3.translate(-hiddenUx, -hiddenUy);
      ctx3.font = parseInt(this.fontsize * cutScale) + "px " + this.fontfamily;
      ctx3.setFillStyle(this.fillstyle);
      ctx3.setTextBaseline("top");
      ctx3.setTextAlign("left");
      ctx3.fillText(this.filltext, this.ux * cutScale, this.uy * cutScale);

      // 隐藏预览canvas
      // #ifdef H5
      ctx4.translate(centeruX, centeruY);
      ctx4.rotate((this.rotate * Math.PI) / 180);
      ctx4.translate(-centeruX, -centeruY);
      ctx4.font = this.fontsize + "px " + this.fontfamily;
      ctx4.setFillStyle(this.fillstyle);
      ctx4.setTextBaseline("top");
      ctx4.setTextAlign("left");
      ctx4.fillText(this.filltext, this.ux, this.uy);
      // #endif
      // #ifndef H5
      ctx4.translate(centeruX * pixelRatio, centeruY * pixelRatio);
      ctx4.rotate((this.rotate * Math.PI) / 180);
      ctx4.translate(-centeruX * pixelRatio, -centeruY * pixelRatio);
      ctx4.font = this.fontsize * pixelRatio + "px " + this.fontfamily;
      ctx4.setFillStyle(this.fillstyle);
      ctx4.setTextBaseline("top");
      ctx4.setTextAlign("left");
      ctx4.fillText(this.filltext, this.ux * pixelRatio, this.uy * pixelRatio);
      // #endif
    }
    // 如果是选中状态，绘制选择虚线框，和缩放图标、删除图标
    this.drawIcon(this, null, 1);
    ctx.restore();
    ctx2.restore();
    ctx3.restore();
    ctx4.restore();
  }
  // 绘制IP图/模板图
  paintIpImage() {
    ctx.save();
    ctx2.save();
    ctx3.save();
    ctx4.save();
    this.centerX = this.x + this.w / 2;
    this.centerY = this.y + this.h / 2;
    // 旋转元素
    ctx.translate(this.centerX, this.centerY);
    ctx.rotate((this.rotate * Math.PI) / 180);
    ctx.translate(-this.centerX, -this.centerY);
    // 显示壁纸层
    let centeruX = this.ux + this.w / 2;
    let centeruY = this.uy + this.h / 2;
    // 渲染元素 1-图片 2-文字
    if (this.type === 1) {
      let imgWidth = this.w;
      let imgheight = this.h;
      ctx.drawImage(this.url.src, this.x, this.y, imgWidth, imgheight);

      // 添加显示层
      ctx2.globalCompositeOperation = "source-atop";
      // #ifdef H5
      ctx2.translate(centeruX, centeruY);
      ctx2.rotate((this.rotate * Math.PI) / 180);
      ctx2.translate(-centeruX, -centeruY);
      ctx2.drawImage(this.url.src, this.ux, this.uy, imgWidth, imgheight);
      // #endif
      // #ifndef H5
      ctx2.translate(centeruX * pixelRatio, centeruY * pixelRatio);
      ctx2.rotate((this.rotate * Math.PI) / 180);
      ctx2.translate(-centeruX * pixelRatio, -centeruY * pixelRatio);
      ctx2.drawImage(
        this.url.src,
        this.ux * pixelRatio,
        this.uy * pixelRatio,
        imgWidth * pixelRatio,
        imgheight * pixelRatio
      );
      // #endif

      // 隐藏打印canvas
      let hiddenUx = this.ux * cutScale + (imgWidth * cutScale) / 2;
      let hiddenUy = this.uy * cutScale + (imgheight * cutScale) / 2;
      ctx3.translate(hiddenUx, hiddenUy);
      ctx3.rotate((this.rotate * Math.PI) / 180);
      ctx3.translate(-hiddenUx, -hiddenUy);
      ctx3.drawImage(
        this.url.src,
        this.ux * cutScale,
        this.uy * cutScale,
        imgWidth * cutScale,
        imgheight * cutScale
      );

      // 隐藏预览canvas
      ctx4.globalCompositeOperation = "source-atop";
      // #ifdef H5
      ctx4.translate(centeruX, centeruY);
      ctx4.rotate((this.rotate * Math.PI) / 180);
      ctx4.translate(-centeruX, -centeruY);
      ctx4.drawImage(this.url.src, this.ux, this.uy, imgWidth, imgheight);
      // #endif
      // #ifndef H5
      ctx4.translate(centeruX * pixelRatio, centeruY * pixelRatio);
      ctx4.rotate((this.rotate * Math.PI) / 180);
      ctx4.translate(-centeruX * pixelRatio, -centeruY * pixelRatio);
      ctx4.drawImage(
        this.url.src,
        this.ux * pixelRatio,
        this.uy * pixelRatio,
        imgWidth * pixelRatio,
        imgheight * pixelRatio
      );
      // #endif
    }

    ctx.restore();
    ctx2.restore();
    ctx3.restore();
    ctx4.restore();
  }
  isInWhere(x, y) {
    // 变换区域左上角的坐标和区域的高度宽度
    const transformW = 24;
    const transformH = 24;
    let transformX = this.x + this.w;
    let transformY = this.y + this.h;
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
  drawIcon(sprite, ct, dpr) {
    let x, y, ctx;
    if (sprite.selected) {
      if (ct === null) {
        x = sprite.x * dpr;
        y = sprite.y * dpr;
        ctx = sprite.ctx;
      } else {
        x = sprite.ux * dpr;
        y = sprite.uy * dpr;
        ctx = ct;
      }
      let swidth = sprite.w * dpr;
      let sheight = sprite.h * dpr;
      let radius = 12 * dpr;
      let iconWidth = 24 * dpr;

      ctx.save();
      ctx.setLineDash([5, 5]);
      ctx.lineWidth = 1;
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
      ctx.restore();
    }
  }
}

export default {
  name: "canvas-drag",
  props: {
    // 上传文件类型
    manufactor: {
      type: String,
      default: "",
    },
    // 手机底图materialsColorValue
    materialsColorValue: {
      type: String,
      default: "white",
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
    // 切图信息
    cutInfo: {
      type: String,
      default: "",
    },
    // 透明间距
    frameValue: {
      type: String,
      default: "",
    },
    // 是否铺满血位
    isAllPlace: {
      type: Number,
      default: 0,
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
      hasClickedArea: false, // 是否点击热区，用于显示“双击进入编辑”提示语
      reverseData: [], // 图层列表（倒序显示）
      myWidth: 0,
      myHeight: 0,
      spriteArr: [],
      hiddenWidth: 0, // 隐藏打印canvas宽
      hiddenHeight: 0, // 隐藏打印canvas高
      isMove: false, // 是否有移动
      showLoading: false, // 是否显示loading
      loadingMsg: "", // loading信息
      pixelRatio: 1, // 屏幕密度比
      foldingWidth: 0, // 隐藏打印canvas宽（折叠屏）
      foldingHeight: 0, // 隐藏打印canvas高（折叠屏）
    };
  },
  computed: {
    ...mapState(["systemInfo"]),
  },
  mounted() {
    // 画布大小
    WIDTH = this.systemInfo.windowWidth;
    HEIGHT = this.wheight;
    // 屏幕密度比dpr
    this.pixelRatio = this.systemInfo.pixelRatio;
    pixelRatio = this.systemInfo.pixelRatio;

    ctx = uni.createCanvasContext("myCanvas");
    ctx2 = uni.createCanvasContext("upCanvas");
    ctx3 = uni.createCanvasContext("hiddenCanvas");
    ctx4 = uni.createCanvasContext("previewCanvas");
  },
  methods: {
    http2Https(url) {
      if (url.indexOf("http:") != -1) {
        return url.replace(/^http:/, "https:");
      } else {
        return url.replace(/^https:/, "http:");
      }
    },
    draw() {
      this.clear();
      if (this.dragArr.length > 0) {
        PWIDTH = this.pwidth;
        PHEIGHT = this.pheight;
        let bgImg = this.http2Https(this.bgimg);
        let upImg = this.http2Https(this.upimg);

        if (Number(this.materialsType) === 2 && this.manufactor === "MK") {
          // TPU圆角处理

          // 透明边距
          let frameValue = JSON.parse(this.frameValue);
          let vFrame = 0;
          let hFrame = 0;
          if (frameValue) {
            vFrame =
              Number(frameValue.leftFrame) + Number(frameValue.rightFrame);
            hFrame =
              Number(frameValue.topFrame) + Number(frameValue.underFrame);
          }
          let cX = (Number(frameValue.leftFrame) / 25.4) * this.imgDpi;
          let cY = (Number(frameValue.topFrame) / 25.4) * this.imgDpi;
          let cwidth = ((this.mw - vFrame) / 25.4) * this.imgDpi;
          let cheight = ((this.mh - hFrame) / 25.4) * this.imgDpi;

          ctx3.drawImage(bgImg, cX, cY, cwidth, cheight);
          ctx3.globalCompositeOperation = "source-in";
        } else {
          if (this.materialsColorValue) {
            // 打印图填充背景色
            ctx3.setFillStyle(this.materialsColorValue);
            ctx3.fillRect(0, 0, this.hiddenWidth, this.hiddenHeight);
          }
        }

        // 预览图填充背景
        // #ifdef H5
        ctx4.drawImage(bgImg, 0, 0, PWIDTH, PHEIGHT);
        // #endif
        // #ifndef H5
        ctx4.drawImage(
          bgImg,
          0,
          0,
          parseInt(PWIDTH * pixelRatio),
          parseInt(PHEIGHT * pixelRatio)
        );
        // #endif

        // 显示图
        // #ifdef H5
        ctx2.drawImage(bgImg, 0, 0, PWIDTH, PHEIGHT);
        // #endif
        // #ifndef H5
        ctx2.drawImage(
          bgImg,
          0,
          0,
          parseInt(PWIDTH * pixelRatio),
          parseInt(PHEIGHT * pixelRatio)
        );
        // #endif

        this.dragArr.forEach((item) => {
          item.paint();
        });

        // 预览图
        // #ifdef H5
        ctx4.drawImage(upImg, 0, 0, PWIDTH, PHEIGHT);
        // #endif
        // #ifndef H5
        ctx4.drawImage(
          upImg,
          0,
          0,
          parseInt(PWIDTH * pixelRatio),
          parseInt(PHEIGHT * pixelRatio)
        );
        // #endif

        ctx.draw();
        ctx2.draw();
        ctx3.draw();
        ctx4.draw();
      }
    },
    drawIpImage() {
      this.clear();
      PWIDTH = this.pwidth;
      PHEIGHT = this.pheight;
      let bgImg = this.http2Https(this.bgimg);
      let upImg = this.http2Https(this.upimg);

      if (Number(this.materialsType) === 2 && this.manufactor === "MK") {
        // TPU圆角处理

        // 透明边距
        let frameValue = JSON.parse(this.frameValue);
        let vFrame = 0;
        let hFrame = 0;
        if (frameValue) {
          vFrame = Number(frameValue.leftFrame) + Number(frameValue.rightFrame);
          hFrame = Number(frameValue.topFrame) + Number(frameValue.underFrame);
        }
        let cX = (Number(frameValue.leftFrame) / 25.4) * 300;
        let cY = (Number(frameValue.topFrame) / 25.4) * 300;
        let cwidth = ((this.mw - vFrame) / 25.4) * 300;
        let cheight = ((this.mh - hFrame) / 25.4) * 300;

        ctx3.drawImage(bgImg, cX, cY, cwidth, cheight);
        ctx3.globalCompositeOperation = "source-in";
      } else {
        if (this.materialsColorValue) {
          // 打印图填充背景色
          ctx3.setFillStyle(this.materialsColorValue);
          ctx3.fillRect(0, 0, this.hiddenWidth, this.hiddenHeight);
        }
      }

      // 预览图填充背景
      // #ifdef H5
      ctx4.drawImage(bgImg, 0, 0, PWIDTH, PHEIGHT);
      // #endif
      // #ifndef H5
      ctx4.drawImage(
        bgImg,
        0,
        0,
        parseInt(PWIDTH * pixelRatio),
        parseInt(PHEIGHT * pixelRatio)
      );
      // #endif

      // 显示图
      // #ifdef H5
      ctx2.drawImage(bgImg, 0, 0, PWIDTH, PHEIGHT);
      // #endif
      // #ifndef H5
      ctx2.drawImage(
        bgImg,
        0,
        0,
        parseInt(PWIDTH * pixelRatio),
        parseInt(PHEIGHT * pixelRatio)
      );
      // #endif
      this.dragArr.forEach((item) => {
        item.paintIpImage();
      });

      // 预览图
      // #ifdef H5
      ctx4.drawImage(upImg, 0, 0, PWIDTH, PHEIGHT);
      // #endif
      // #ifndef H5
      ctx4.drawImage(
        upImg,
        0,
        0,
        parseInt(PWIDTH * pixelRatio),
        parseInt(PHEIGHT * pixelRatio)
      );
      // #endif

      ctx.draw();
      ctx2.draw();
      ctx3.draw();
      ctx4.draw();
    },
    start(e) {
      // e.preventDefault();
      if (this.isDrag && this.spriteArr.length > 0) {
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
          // x轴,y轴
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
            // 重新绘制
            this.draw();
            if (this.dragArr.length === 0) {
              this.$emit("dpi", 0, 1); // 图层清空（dpi设为0），不显示打印效果
              this.clear();
              ctx.draw();
              ctx2.draw();
              ctx3.draw();
              ctx4.draw();
            }
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
        let x, y;
        let now = [];
        let start = [];
        if (e === null || e.length === 0) return;
        let startX, startY;
        let lastImg = this.lastImg;
        if (e.touches.length === 1 && lastImg) {
          x = e.touches[0].pageX;
          y = e.touches[0].pageY;
          let pageX = lastImg.centerX;
          let pageY = lastImg.centerY;
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
            if (this.lastImg.place === "move" && e.touches.length === 1) {
              // 算出移动后的xy坐标与点击时xy坐标的差（即平移量）与图片对象的初始坐标相加即可
              lastImg.x = initialX + (x - startX);
              lastImg.y = initialY + (y - startY);
              lastImg.ux = initialuX + (x - startX);
              lastImg.uy = initialuY + (y - startY);
            }
            if (
              this.lastImg.place === "transform" ||
              (e.touches.length >= 2 && istouch)
            ) {
              // 旋转部分
              let { initialRotate } = this.initial;
              // 旋转的角度
              let rotation =
                this.getAngle(now[0], now[1]) -
                this.getAngle(start[0], start[1]);
              lastImg.rotate = initialRotate + rotation;
              // 缩放部分
              let { initialH, initialW } = this.initial;
              let lineA = this.getDistance(start[0], start[1]);
              let lineB = this.getDistance(now[0], now[1]);
              // 用勾股定理算出距离
              let w = initialW + lineB - lineA;
              // 由于是等比缩放，所以乘一个宽高比例。
              let h = initialH + (lineB - lineA) * (initialH / initialW);

              // 定义最小宽高
              lastImg.w = w <= lastImg.minW ? lastImg.minW : w;

              lastImg.h = h <= lastImg.minH ? lastImg.minH : h;
              lastImg.linew = lineB - lineA;
              lastImg.lineh = (lineB - lineA) * (initialH / initialW);
              if (w > lastImg.minW && h > lastImg.minH) {
                // 放大 或 缩小
                lastImg.x = initialX - lastImg.linew / 2;
                lastImg.y = initialY - lastImg.lineh / 2;
                lastImg.ux = initialuX - lastImg.linew / 2;
                lastImg.uy = initialuY - lastImg.lineh / 2;
              }
              if (this.lastImg.type === 1) {
                this.getImgDPI(lastImg);
              }
            }
            this.isMove = true;
          }
        }
      }
    },
    end(e) {
      // e.preventDefault();
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

        this.draw();
        this.isMove = false;
      }
    },
    getDistance(p1, p2) {
      let x = parseInt(p2.pageX - p1.pageX);
      let y = parseInt(p2.pageY - p1.pageY);
      return Math.sqrt(x * x + y * y);
    },
    getAngle(p1, p2) {
      let x = parseInt(p1.pageX - p2.pageX);
      let y = parseInt(p1.pageY - p2.pageY);
      return (Math.atan2(y, x) * 180) / Math.PI;
    },
    // 定制
    make() {
      if (this.dragArr.length > 0) {
        this.showLoading = true;
        this.loadingMsg = "设计中，请稍候~";

        this.$emit("handleClick", true);
        // 去除图标和虚线
        this.dragArr.forEach((item) => {
          if (item.selected) {
            item.selected = false;
          }
        });

        let cloud = this.$cloud;
        // 预览图
        let previewImg = "";
        uni.canvasToTempFilePath({
          x: 0,
          y: 0,
          canvasId: "previewCanvas",
          success: (res) => {
            cloud.file
              .uploadFile({
                filePath: res.tempFilePath,
                fileType: "image",
                fileName:
                  this.distributor +
                  "/" +
                  store.state.taobao.onLaunchParams.sellerNick +
                  "/" +
                  new Date().getTime() +
                  ".png",
              })
              .then((file) => {
                previewImg = file.url;
              });
          },
        });

        let printImg = "";
        // 折叠屏
        if (this.cutInfo) {
          let ctx5 = uni.createCanvasContext("foldingCanvas");
          let _this = this;
          let cutInfo = JSON.parse(_this.cutInfo);
          if (Number(cutInfo.cutType) === 1) {
            // 纵切
            let slittingHeight = 0; // 纵切高度
            if (this.materialsType === 1 || this.materialsType === 3) {
              // 玻璃壳/热升华+1mm（需多切1mm）
              slittingHeight =
                ((cutInfo.slittingHeight + 1) / 25.4) * this.imgDpi;
            } else {
              slittingHeight = (cutInfo.slittingHeight / 25.4) * this.imgDpi;
            }
            let slittingWhite = (cutInfo.slittingWhite / 25.4) * this.imgDpi; // 纵切留白
            // 折叠屏宽高
            _this.foldingWidth = parseInt(_this.hiddenWidth);
            _this.foldingHeight = parseInt(_this.hiddenHeight) + slittingWhite;
            // 获取上半部分数据
            ctx3.getImageData({
              x: 0,
              y: 0,
              width: parseInt(_this.hiddenWidth),
              height: parseInt(slittingHeight),
              success(res) {
                let data1 = res;
                // 获取下半部分数据
                ctx3.getImageData({
                  x: 0,
                  y: parseInt(slittingHeight),
                  width: parseInt(_this.hiddenWidth),
                  height:
                    parseInt(_this.hiddenHeight) - parseInt(slittingHeight),
                  success(res) {
                    let data2 = res;
                    // 绘制上半部分数据
                    ctx5.putImageData({
                      x: 0,
                      y: 0,
                      width: data1.width,
                      height: data1.height,
                      data: new Uint8ClampedArray(data1.data),
                      success(res) {
                        // 绘制下半部分数据
                        ctx5.putImageData({
                          x: 0,
                          y: data1.height + slittingWhite,
                          width: data2.width,
                          height: data2.height,
                          data: new Uint8ClampedArray(data2.data),
                          success(res) {
                            ctx5.draw(); // 绘图
                            // 导出图片
                            setTimeout(() => {
                              uni.canvasToTempFilePath({
                                x: 0,
                                y: 0,
                                canvasId: "foldingCanvas",
                                success: (res) => {
                                  let filePath = res.tempFilePath;
                                  uni.getFileInfo({
                                    filePath: filePath,
                                    success: (res) => {
                                      let isLt = res.size / 1024 < 50;
                                      if (isLt) {
                                        // 判断图片大小是否小于50KB
                                        _this.$refs.uToast.show({
                                          title: "网络错误，请稍后重试~",
                                          type: "warning",
                                        });
                                        _this.showLoading = false;
                                        _this.loadingMsg = "";
                                        this.$emit("handleClick", false);
                                        clearInterval(interval);
                                      } else {
                                        cloud.file
                                          .uploadFile({
                                            filePath: filePath,
                                            fileType: "image",
                                            fileName:
                                              _this.distributor +
                                              "/" +
                                              store.state.taobao.onLaunchParams
                                                .sellerNick +
                                              "/" +
                                              new Date().getTime() +
                                              ".png",
                                          })
                                          .then((file) => {
                                            printImg = file.url;
                                          });
                                      }
                                    },
                                  });
                                },
                              });
                            }, 500);
                          },
                        });
                      },
                    });
                  },
                });
              },
            });
          } else if (Number(cutInfo.cutType) === 2) {
            // 横切
            let crosscuttingWidth = 0; // 横切宽度
            if (this.materialsType === 1 || this.materialsType === 3) {
              // 玻璃壳/热升华+1mm（需多切1mm）
              crosscuttingWidth =
                ((cutInfo.crosscuttingWidth + 1) / 25.4) * this.imgDpi;
            } else {
              crosscuttingWidth =
                (cutInfo.crosscuttingWidth / 25.4) * this.imgDpi;
            }
            let crosscuttingWhite =
              (cutInfo.crosscuttingWhite / 25.4) * this.imgDpi; // 横切留白
            // 折叠屏宽高
            _this.foldingWidth =
              parseInt(_this.hiddenWidth) + crosscuttingWhite;
            _this.foldingHeight = parseInt(_this.hiddenHeight);
            // 获取上半部分数据
            ctx3.getImageData({
              x: 0,
              y: 0,
              width: parseInt(crosscuttingWidth),
              height: parseInt(_this.hiddenHeight),
              success(res) {
                let data1 = res;
                // 获取下半部分数据
                ctx3.getImageData({
                  x: crosscuttingWidth,
                  y: 0,
                  width:
                    parseInt(_this.hiddenWidth) - parseInt(crosscuttingWidth),
                  height: parseInt(_this.hiddenHeight),
                  success(res) {
                    let data2 = res;
                    // 绘制上半部分数据
                    ctx5.putImageData({
                      x: 0,
                      y: 0,
                      width: data1.width,
                      height: data1.height,
                      data: new Uint8ClampedArray(data1.data),
                      success(res) {
                        // 绘制下半部分数据
                        ctx5.putImageData({
                          x: data1.width + crosscuttingWhite,
                          y: 0,
                          width: data2.width,
                          height: data2.height,
                          data: new Uint8ClampedArray(data2.data),
                          success(res) {
                            ctx5.draw(); // 绘图
                            // 导出图片
                            setTimeout(() => {
                              uni.canvasToTempFilePath({
                                x: 0,
                                y: 0,
                                canvasId: "foldingCanvas",
                                success: (res) => {
                                  let filePath = res.tempFilePath;
                                  uni.getFileInfo({
                                    filePath: filePath,
                                    success: (res) => {
                                      let isLt = res.size / 1024 < 50;
                                      if (isLt) {
                                        // 判断图片大小是否小于50KB
                                        _this.$refs.uToast.show({
                                          title: "网络错误，请稍后重试~",
                                          type: "warning",
                                        });
                                        _this.showLoading = false;
                                        _this.loadingMsg = "";
                                        this.$emit("handleClick", false);
                                        clearInterval(interval);
                                      } else {
                                        cloud.file
                                          .uploadFile({
                                            filePath: filePath,
                                            fileType: "image",
                                            fileName:
                                              _this.distributor +
                                              "/" +
                                              store.state.taobao.onLaunchParams
                                                .sellerNick +
                                              "/" +
                                              new Date().getTime() +
                                              ".png",
                                          })
                                          .then((file) => {
                                            printImg = file.url;
                                          });
                                      }
                                    },
                                  });
                                },
                              });
                            }, 500);
                          },
                        });
                      },
                    });
                  },
                });
              },
            });
          }
        } else {
          let _this = this;
          // 非折叠屏
          setTimeout(() => {
            uni.canvasToTempFilePath({
              x: 0,
              y: 0,
              canvasId: "hiddenCanvas",
              success: (res) => {
                let filePath = res.tempFilePath;
                uni.getFileInfo({
                  filePath: filePath,
                  success: (res) => {
                    let isLt = res.size / 1024 < 50;
                    if (isLt) {
                      // 判断图片大小是否小于50KB
                      _this.$refs.uToast.show({
                        title: "网络错误，请稍后重试~",
                        type: "warning",
                      });
                      _this.showLoading = false;
                      _this.loadingMsg = "";
                      this.$emit("handleClick", false);
                      clearInterval(interval);
                    } else {
                      cloud.file
                        .uploadFile({
                          filePath: filePath,
                          fileType: "image",
                          fileName:
                            _this.distributor +
                            "/" +
                            store.state.taobao.onLaunchParams.sellerNick +
                            "/" +
                            new Date().getTime() +
                            ".png",
                        })
                        .then((file) => {
                          printImg = file.url;
                        });
                    }
                  },
                });
              },
            });
          }, 500);
        }

        let interval = setInterval(() => {
          if (previewImg && printImg) {
            this.showLoading = false;
            this.loadingMsg = "";
            this.$emit("submit", previewImg, printImg);
            clearInterval(interval);
          }
        }, 1000);
      } else {
        this.$refs.uToast.show({
          title: "请选择图片！",
          type: "warning",
        });
      }
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
      let naturalWidth = 0;
      let naturalHeight = 0;
      // #ifndef H5
      naturalWidth = item.url.width;
      naturalHeight = item.url.height;
      // #endif
      // #ifdef H5
      naturalWidth = item.url.naturalWidth;
      naturalHeight = item.url.naturalHeight;
      // #endif

      if (naturalWidth / icw > naturalHeight / ich) {
        // 获取图片dpi和获取计算dpi的手机尺寸
        dpi = naturalHeight / ich;
        this.naturalSize = ich;
        this.dpiType = 2;
      } else {
        dpi = naturalWidth / icw;
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
      let naturalWidth = 0;
      let naturalHeight = 0;
      // #ifndef H5
      naturalWidth = item.url.width;
      naturalHeight = item.url.height;
      // #endif
      // #ifdef H5
      naturalWidth = item.url.naturalWidth;
      naturalHeight = item.url.naturalHeight;
      // #endif

      this.naturalWidth = naturalWidth;
      this.naturalHeight = naturalHeight;
      if (this.imgDpi > 300) {
        // 超过300dpi时，取固定300dpi
        this.imgDpi = 300;
        if (this.dpiType === 1) {
          this.naturalWidth = (this.imgDpi / scale) * this.naturalSize;
          this.naturalHeight =
            naturalHeight * (this.naturalWidth / naturalWidth);
        } else {
          this.naturalHeight = (this.imgDpi / scale) * this.naturalSize;
          this.naturalWidth =
            naturalWidth * (this.naturalHeight / naturalHeight);
        }
      }

      this.hiddenWidth = (this.nw / 25.4) * 300;
      this.hiddenHeight = (this.nh / 25.4) * 300;
      if (
        Number(this.materialsType) === 1 ||
        Number(this.materialsType) === 3
      ) {
        // 玻璃壳/热升华+2mm
        this.hiddenWidth = ((this.nw + 2) / 25.4) * 300;
        this.hiddenHeight = ((this.nh + 2) / 25.4) * 300;
      }
      cutWidth = this.hiddenWidth;
      cutHeight = this.hiddenHeight;

      if (cutHeight / this.pheight > cutWidth / this.pwidth) {
        cutScale = cutHeight / this.pheight;
      } else {
        cutScale = cutWidth / this.pwidth;
      }
    },
    // 清空画布
    clear() {
      ctx.clearRect(0, 0, this.myWidth, this.myHeight);
      ctx2.clearRect(0, 0, this.pwidth, this.pheight);
      ctx3.clearRect(0, 0, this.hiddenWidth, this.hiddenHeight);
      ctx4.clearRect(0, 0, this.pwidth, this.pheight);
      ctx.restore();
      ctx2.restore();
      ctx3.restore();
      ctx4.restore();
    },
    // 重置
    reset() {
      this.$emit("clear", 0);
      this.curClicked = "";
      this.dragArr.length = 0;
      this.dragArr = [];
      // 重新绘制
      this.clear();
      ctx.draw();
      ctx2.draw();
      ctx3.draw();
      ctx4.draw();
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
        ctx.draw();
        ctx2.draw();
        ctx3.draw();
        ctx4.draw();
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
              this.$set(this.dragArr, curIndex + 1, this.dragArr[curIndex]);
              this.$set(this.dragArr, curIndex, temp);
            }
          } else {
            // 下移，调整位置
            if (curIndex > 0) {
              let temp = this.dragArr[curIndex - 1];
              this.$set(this.dragArr, curIndex - 1, this.dragArr[curIndex]);
              this.$set(this.dragArr, curIndex, temp);
            }
          }
        }
      });

      // 重新绘制
      this.draw();
    },
    initCanvas() {
      // 画布大小
      WIDTH = this.systemInfo.windowWidth;
      HEIGHT = this.wheight;
      this.myWidth = WIDTH;
      this.myHeight = HEIGHT;
    },
    // 上传本地图片
    chooseImg() {
      this.$emit("chooseImg");
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
    // 监听画布宽度
    wwidth(value) {
      return value;
    },
    spriteArr(arr) {
      this.initCanvas();
      if (arr.length > 0) {
        // 获取最新添加的数据
        let newImg = arr.slice(-1)[0];
        // 判断图片类型  0-网络图 1-普通素材 2-IP素材（不可更改）
        if (parseInt(newImg.picType) === 2) {
          // IP图，直接替换，不可移动
          this.isDrag = false;
          let item = new DragImg(newImg, ctx);
          this.dragArr = [];
          this.dragArr.push(item);
          this.getImgDPI(item);
          this.drawIpImage();
        } else {
          // 普通素材/贴图
          this.isDrag = true;
          this.dragArr.forEach((item, index) => {
            // type 1--图片
            if (newImg.type === 1 && item.type === 1) {
              // 多图层，判断当前图片是否是素材图片（素材图片id），同时不是贴图
              // if (newImg.picId && newImg.picType !== 4) {
              // 	// 判断是否已经有素材图片
              // 	if (item.picId && item.picType !== 4) {
              // 		// 有直接替换
              // 		this.dragArr.splice(index, 1);
              // 	}
              // }
              // 单图层，非贴图，替换图片
              if (newImg.picType !== 4) {
                this.dragArr.splice(index, 1);
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
            this.getImgDPI(item);
            item.picType === 1 ? this.draw() : this.drawIpImage();
          }
        });
      }
      PWIDTH = value;
      return value;
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
      PHEIGHT = value;
      return value;
    },
    nw(value) {
      this.hiddenWidth = (value / 25.4) * this.imgDpi;
      if (
        Number(this.materialsType) === 1 ||
        Number(this.materialsType) === 3
      ) {
        // 玻璃壳/热升华+2mm
        this.hiddenWidth = ((value + 2) / 25.4) * this.imgDpi;
      }
      cutWidth = this.hiddenWidth;
    },
    nh(value) {
      this.hiddenHeight = (value / 25.4) * this.imgDpi;
      if (
        Number(this.materialsType) === 1 ||
        Number(this.materialsType) === 3
      ) {
        // 玻璃壳/热升华+2mm
        this.hiddenHeight = ((value + 2) / 25.4) * this.imgDpi;
      }
      cutHeight = this.hiddenHeight;
    },
    cutInfo(value) {
      return value;
    },
  },
};
</script>
<style scoped lang="stylus" rel="stylesheet/stylus">
$white = #fff;
$gray = #5A5A5A;
$light-gray = #787878;
$gray-bd = #F1F1F1;
$blue = #15BDD7;
$color-line = #D8D8D8;
$radius-sm = 5px;

.sprite-icon {
  position: relative;
  top: 2px;
}

.canvas-wrapper {
  .bgimg {
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    background-repeat: no-repeat;
    z-index: 1;
  }

  .hiddenCanvas, .previewCanvas, .foldingCanvas {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    filter: opacity(0);
    z-index: 1;
  }

  .moveList {
    position: absolute;
    top: 0;
    left: 50%;
    transform: translateX(-50%);
    filter: opacity(0.3);
    z-index: 5;

    &.hidden {
      filter: opacity(0);
    }
  }

  .myCanvas {
    position: absolute;
    top: 0;
    left: 50%;
    transform: translateX(-50%);
    filter: opacity(0.3);
    z-index: 5;

    &.module, &.hidden {
      filter: opacity(0);
    }
  }

  .upCanvas {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    filter: opacity(1);
    z-index: 10;

    &.hidden {
      filter: opacity(0);
    }
  }

  .up-box {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    background-repeat: no-repeat;
    background-position: 50% 50%;
    background-clip: content-box;
    pointer-events: all;
    z-index: 11;

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

  // 添加图片
  .add-img {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 80px;
    height: 80px;
    line-height: 80px;
    text-align: center;
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
  box-sizing: border-box;
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

    .close-btn {
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

    image {
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

.loading-wrap {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 100;
  pointer-events: none;

  .loading-content {
    position: absolute;
    top: 50%;
    left: 50%;
    min-width: 120px;
    min-height: 120px;
    padding: 20px 10px;
    color: $white;
    text-align: center;
    background-color: rgba(0, 0, 0, 0.8);
    border-radius: 8px;
    box-sizing: border-box;
    transform: translate(-50%, -50%);
  }

  .loading-msg {
    display: block;
    margin-top: 10px;
    font-size: 14px;
  }
}

html，body {
  -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
}
</style>
