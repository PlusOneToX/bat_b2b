<template>
  <div class="canvas-wrapper">
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
        show: showDashBorder && this.dragArr.length > 0,
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
      v-show="showDashBorder && this.dragArr.length > 0"
      class="dash-border"
      :style="{
        width: isZFold ? pwidth / 2 + 'px' : pwidth + 'px',
        height: pheight + 'px',
        marginLeft: isZFold ? -pwidth / 4 + 'px' : 0,
      }"
    ></div>
    <div
      class="up-box"
      ref="wrapCanvas"
      :style="{
        backgroundImage: 'url(' + upimg + ')',
        backgroundSize: pwidth + 'px ' + pheight + 'px',
      }"
      @mousedown.prevent="start($event)"
      @mousemove.prevent="move($event)"
      @mouseup.prevent="end($event)"
    ></div>

    <p class="tips" v-show="showDashBorder && this.dragArr.length > 0">
      提示：请确保图片铺满红色虚线框
    </p>
    <!--加载动画-->
    <loading v-if="this.showLoading"></loading>
  </div>
</template>

<script type="text/ecmascript-6">
import loading from "@/components/loading.vue";
// api
import { getOSSSts } from "@/apiService/custom";

const DEL_ICON =
  "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAAAXNSR0IArs4c6QAABNNJREFUaAXtWU9oHUUc/mbfSxprbGlJUk2rSRMoWG39jwe9ea9Q8OZBLFIQUU/iJQXbU4/iQVQsIp6FRjx600OhtaCoB81L/UMMjWgpr6EmeW/9fZvMe7OzM7uzu2lqIT9IdmZn5vf7vtn5+z1g27Z7oFYPqFqtzcYX546go45BxYeBeBxQ44j5FFNqQd7Jnzxj9SMa8SyenP7ebF41XY8AQXfxMmI8LwAPlgOh5qFwHhHO1SFTjcDFuQfQic9Iz74ovRyVA27VVqorPj5DQ80Ikd+s0sJsOQI/xINYbp2WgG9Kr+8o9F6qgropX+Nd7Jw6hYfUSmjTcALf/jmK1eXPZag8G+q8Wj31NQZ2Hsfj9y2FtA8jcKl1FGsy8RBPhDitX0f9iqYsCE9MfVfkq5hAAr77jTgaLnK2yeVtNKNnikjkT0AOm6Tntxw8+2I4iU0MOeYnwAm7Pua3aNi4UMqQJQZi8ZifQLt1xjdh2Whfs+FxWf71SDNC09tMFg2ufB5zE+A6D7zuasMGlx8+gIXHJvDexIjsRfXs1PgeLDw6idYjE7g78njr4g2sY8oEcxPgJoV4KFNbXtw70MCRuwYRKYXX9u3GB5OjlUmc3r8H7xzYiwEBfv+OJg4NDbhCyjvBkmDKFmcJ8HjAHdZjC6sdfLh0vVf6ytiuSiQIfmb/3p6f2X9u4PJyzv5FTMRmWZZAcrbJPx68euUvfFKDhA3+y2s38MIvixY0K8sjC7FZliWQHMysWla2K/kT80uVSLjAH/95ESuxFcSVdWBLE+CmFXiqrEKiFviEkJx4E4x9dmkCne6xflFxqgyJ+uA38FgY0wSgHiyGna4RQmLTwCeh0xit/WPjBpXGWJjTJFjxpdFdSX2uTrTF1bXUasMJGzzmEw/2vzRGi4BcA+WgX8XySGh/9cHTEzH2LT2E9B22X14qpUmYS6x2sDngxZuFMU1AR6vxJInfV9YyHhZXOlit9nEzvswXaQKJemAWl0/bE1Z7OFFxx9bte08LY5pAIn30qpZO2OA5bD6tsWO7AVCe6Zs9ianf9EtLpFzgudqsiTsOK3t1OnllqWIkakx9swjEP/WLwlM+8Pp4wGMHbVNIUBgzLD2EInXeKAtKFoGnE9fqVPUUm6h6BrLsDeLCXCv0PBQC3oglsg/w8cHR3pdg2UdXryN8OKl5PD09Zfs087JPiNwXYGXB02XtL+HAlh5CjEKtknJfjvEaaF5GymxSPhLvT47kRJQiYiI2y7IEqBpTq/TYmFzmZ+Q2pa0MeN3GReLk2G4c9l4ppSUxORTtLAFGodCq8K8OaD6vdTq4KtdK2hdyDax6MNMkzm3sE+1OVw5+637NeOtp0U2JyWHZSawrXZg7K7Tf0lnzeY9cwg8NDeLSspOjWTUofVREgj/k+PG3kHBapM7iqem3XWV+AhST2q2vhMQtFnNdsMx3IvYOTz3nU6zdQ4jtKXFTJYYIrbfNJDYx5MjtfgIETYmbKjHQvg0cRNyV2AUyez4BoqbETZV4S78E5fViZZrw/HOApab9T3/gKP4CmgQ/JScTVwTPEqurVnvyJybxzRgFw8b0H/4FzFZ37I98Jgmm79ifWW0izFMxS0Qn6jY5P3RD7hyNaLbopyNXiO132z1wC3rgP+34Hkili2z3AAAAAElFTkSuQmCC";
const SCALE_ICON =
  "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAAAwNJREFUWAnlV81rE1EQ/73NpmmJGvErwapppVbQg4jE6kE8iVex1YN/gacePHsR/AsqiDdvgmiLBxHBk/RgbYSieCnaphHrV9FIiTVpPtaZ3Wz27e5bu/mAHjoQ3r5582Z+Oztv3i/AVhfRUgKySydh1C7BMM5BiH4a95v7hfhCz8ukm4aIPEFmYC6s33AAsotjFOA2/Y6GcizEPIG5iczhxxvZ/x/A26VBlKoPyMmZjRwFrM+gV7+GEwO5gHUEA5jNnQdqkzCwO2hzKL3ATyAyitODL1X2agBW8BcUPKra1LJOoEIgLqhA+AFw2svVbMdv7kXJmYjpGe/n0L12jW/uS/tIPIZDMR3H+3qwM6Lh1nIBhVrdtz1QwZ/Sqqezso07A1zt9foj2YCf3xzrx6ltvS712IdvmCz8celCTTTtinw6NNcmPmoKSfZYiTIMA89/r+EyBZ9qJzj79sRwAJhNRn3Ot2tWooQQuJjoQzIaoRJpU7iXcKyGOAC4wykkTsETegSleh3j+RXqLwJ303twfd8OhXVIlRTLKUJurwop1Q3kyxW8/7uOO99XTYuJ9F4TBE/u/bB0iq3BKimWA8Dq7b5NNdIcefcJlUbOuwKCYzXEAWBfLPaKNNrBbVXHIKRYDgDbe8ixQxDNGnaKkK/UFoVBqApzPJnAs+EUou4u43gX4qs9cTLA9zkwbC+EHVWZuJFKIB2L4kBUR2696ndlxTL1cgam/ZbhNN5MHKTGVaOm9bmiCM4umbg0RAJATKYDyRbLuL+yijy9sUa9Il+uNk+Ozy2zpoY4n4Bp1OzCfGjWY3ug8equOB4OpSQNMLdWds2bE2ZLEmVzALAF0yjD8F1Gzc0BD/z2U7+KKFLTWixVsECN6yndGUrhGJL46/T1x1e03i4Fk1wrH2cwMuS6jp0asO2Zw5k0ylZ0aWSf7NsjfgAmgSQOZ9Ioj3W7U4uSjXrZELvzA2CtSSCJw3UjE6YPNR/kUP4aYK0tm0rLbRA8btofExmECaT7f828Ibbe/B+T5SiYqww5HwAAAABJRU5ErkJggg==";
const MARGIN_LEFT = 620; // 与左侧边栏距离
var HEIGHT = 0;
const POSY = 118; // 与上边栏距离
var PWIDTH = 0; // 手机模型宽
var PHEIGHT = 0; // 手机模型高
var RPWIDTH = 0; // ratio手机模型宽
var RPHEIGHT = 0; // ratio手机模型高
var delIcon = new Image();
var scaleIcon = new Image();
var bimg = new Image();
var upimg = new Image();
delIcon.src = DEL_ICON;
scaleIcon.src = SCALE_ICON;
var c2, ctx2, c, ctx;
var canvasRatio = 2;
let isZFold = false; 

class DragImg {
  constructor(props, canvas) {
    this.x = props.x;
    this.y = props.y;
    this.initx = props.initx;
    this.inity = props.inity;
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
    this.picId = props.picId;
    this.ctx = canvas;
    this.rotate = props.rotate;
    this.selected = props.selected;
    this.index = props.index;
  }
  // 绘制
  paint() {
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
    c3.width = (document.body.clientWidth - MARGIN_LEFT) * canvasRatio;
    c3.height = HEIGHT * canvasRatio;
    c3.style.opacity = 0;
    c3.style.width = document.body.clientWidth - MARGIN_LEFT + "px";
    c3.style.height = HEIGHT + "px";
    if (this.isZFold) {
      c3.width =
        (document.body.clientWidth - MARGIN_LEFT - PWIDTH / 2) * canvasRatio;
      c3.style.width =
        document.body.clientWidth - MARGIN_LEFT - PWIDTH / 2 + "px";
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
      ctx2.fillRect(0, 0, RPWIDTH, RPHEIGHT);
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
    ctx.restore();
    ctx2.restore();
  }
  // 绘制IP图
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
    c3.width = (document.body.clientWidth - MARGIN_LEFT) * canvasRatio;
    c3.height = HEIGHT * canvasRatio;
    c3.style.opacity = 0;
    c3.style.width = document.body.clientWidth - MARGIN_LEFT + "px";
    c3.style.height = HEIGHT + "px";
    if (this.isZFold) {
      c3.width =
        (document.body.clientWidth - MARGIN_LEFT - PWIDTH / 2) * canvasRatio;
      c3.style.width =
        document.body.clientWidth - MARGIN_LEFT - PWIDTH / 2 + "px";
    }
    let centeruX = this.ux + this.w / 2;
    let centeruY = this.uy + this.h / 2;
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
    // this.drawIcon(this, ctx3)
    let pat = ctx2.createPattern(c3, "no-repeat");
    ctx2.fillStyle = pat;
    ctx2.globalCompositeOperation = "source-atop";
    ctx2.fillRect(0, 0, RPWIDTH, RPHEIGHT);
    ctx3.clearRect(0, 0, c3.width, c3.height);
    c.style.zIndex = "-1";
    ctx.restore();
    ctx2.restore();
  }
  // 判断点击坐标位置
  isInWhere(x, y) {
    // 变换区域左上角的坐标和区域的高度宽度 --右下
    let transformW = 24;
    let transformH = 24;
    let transformX = this.x + this.w;
    let transformY = this.y + this.h;
    this.centerX = this.centerX / canvasRatio;
    this.centerY = this.centerY / canvasRatio;
    let transformAngle =
      (Math.atan2(transformY - this.centerY, transformX - this.centerX) /
        Math.PI) *
        180 +
      this.rotate;
    let transformXY = this.getTransform(
      transformX,
      transformY,
      transformAngle,
      12,
      12
    );
    transformX = transformXY.x;
    transformY = transformXY.y;
    // 删除区域左上角的坐标和区域的高度宽度 -- 左上
    let delW = 24;
    let delH = 24;
    let delX = this.x;
    let delY = this.y;
    let delAngle =
      (Math.atan2(delY - this.centerY, delX - this.centerX) / Math.PI) * 180 +
      this.rotate;
    let delXY = this.getTransform(delX, delY, delAngle, 12, 12);
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

    // 移动区域的坐标 (四个顶点的坐标)
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
  // 获取旋转后的坐标位置
  getTransform(x, y, rotate, w, h) {
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
      x: this.centerX + b - w,
      y: this.centerY + a - h,
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
      // ctx.strokeRect(x, y, sprite.w, sprite.h)
      // ctx.drawImage(delIcon, x - 12, y - 12, 24, 24)
      // ctx.drawImage(scaleIcon, x + sprite.w - 12, y + sprite.h - 12, 24, 24)
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
  name: "cnavas-drag",
  props: {
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
    // 材质颜色值
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
    upimg: {
      type: String,
      default: "",
    },
    pwidth: {
      type: Number,
      default: 0,
    },
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
  },
  data() {
    return {
      ctx: null,
      dragArr: [],
      clickedkArr: [],
      c: null,
      lastImg: null,
      initial: null,
      startTouch: null,
      diffX: 0,
      diffY: 0,
      scale: 0,
      difScale: 0,
      imgDpi: 0,
      showLoading: false, // 展示加载动画
      isDown: false,
      isDrag: true, // 图片类型是否可编辑
      showDashBorder: false, // 是否显示虚线框
      curTimeout: "", // 编辑器隐藏定时器
      // 透明间距
      frameValue: {},
      isZFold: false, 
    };
  },
  methods: {
    draw() {
      this.clear();
      if (this.dragArr.length > 0) {
        ctx2.save();
        bimg = document.getElementById("bgimg");
        ctx2.drawImage(bimg, 0, 0, RPWIDTH, RPHEIGHT);
        this.dragArr.forEach((item) => {
          item.paint();
        });
      }
    },
    drawIpImage() {
      // 设置为不可编辑
      this.isDrag = false;
      // 清空canvas内容
      this.clear();
      ctx2.save();
      bimg = document.getElementById("bgimg");
      ctx2.drawImage(bimg, 0, 0, RPWIDTH, RPHEIGHT);
      this.dragArr.forEach((item) => {
        item.paintIpImage();
      });
    },
    start(e) {
      if (this.isDrag && this.dragArr.length > 0) {
        this.showDashBorder = true;
        clearTimeout(this.curTimeout);
        this.isDown = true;
        // 初始化一个数组用于存放所有被点击到的图片对象
        this.clickedkArr = [];
        let touchEvent = this.normalizeTouchEvent(e);
        if (!touchEvent) {
          return;
        }
        let { x, y } = touchEvent;
        this.dragArr.forEach((item, index) => {
          const place = item.isInWhere(x, y);
          item.place = place;
          item.index = index;
          // 先将所有的item的selected变为flase
          item.selected = false;
          if (place) {
            this.clickedkArr.push(item);
          } else {
            this.$emit("clear", item.type);
          }
        });
        let length = this.clickedkArr.length;
        if (length) {
          // 我们知道cavans绘制的图片的层级是越来越高的，因此我们取这个数组的最后一项，保证取到的图片实例是层级最高的
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
        this.startTouch = { startX: x, startY: y };
      }
    },
    move(e) {
      if (this.isDown && this.isDrag) {
        let touchEvent = this.normalizeTouchEvent(e);
        if (!touchEvent) {
          return;
        }
        this.showDashBorder = true;
        clearTimeout(this.curTimeout);
        let { x, y } = touchEvent;
        if (this.initial) {
          let { initialX, initialY, initialuX, initialuY } = this.initial;
          let { startX, startY } = this.startTouch;
          let lastImg = this.lastImg;
          if (this.clickedkArr.length) {
            // 是否强制铺满血位
            let isAllPlace = parseInt(sessionStorage.getItem("isAllPlace"));

            if (this.lastImg.place === "move") {
              if (
                Number(lastImg.type) === 1 &&
                Number(lastImg.picType) !== 4 &&
                isAllPlace
              ) {
                // 强制铺满，限制移动范围
                this.limitMove(
                  touchEvent,
                  this.initial,
                  this.startTouch,
                  lastImg
                );
              } else {
                lastImg.x = initialX + (x - startX);
                lastImg.ux = initialuX + (x - startX);
                lastImg.y = initialY + (y - startY);
                lastImg.uy = initialuY + (y - startY);
              }
            }
            if (this.lastImg.place === "transform") {
              // 旋转（type: 2 文字，强制铺满血位并且type 1/picType 4 贴图，不强制铺满血位）
              if (
                Number(lastImg.type) === 2 ||
                !isAllPlace ||
                (isAllPlace &&
                  Number(lastImg.type) === 1 &&
                  Number(lastImg.picType) === 4)
              ) {
                let { centerX, centerY } = lastImg;
                centerX = centerX / canvasRatio;
                centerY = centerY / canvasRatio;
                // 旋转部分
                let { initialRotate } = this.initial;
                let angleBefore =
                  (Math.atan2(startY - centerY, startX - centerX) / Math.PI) *
                  180;
                let angleAfter =
                  (Math.atan2(y - centerY, x - centerX) / Math.PI) * 180;
                // 旋转的角度
                lastImg.rotate = initialRotate + angleAfter - angleBefore;
              }

              // 缩放部分
              this.limitScale(
                touchEvent,
                this.initial,
                this.startTouch,
                lastImg
              );
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
      this.curTimeout = setTimeout(() => {
        this.showDashBorder = false;
      }, 3000);
      this.isDown = false;
      if (this.isDrag) {
        // 返回最后点击图层信息
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
    // 获取点击坐标
    normalizeTouchEvent(e) {
      if (e === null || e.length === 0) return;
      let x = e.pageX - MARGIN_LEFT;
      let y = e.pageY - POSY;
      return {
        x,
        y,
      };
    },
    // 定制
    make() {
      if (this.dragArr.length > 0) {
        // 1--普通图片 2-IP图
        let type = this.isDrag ? 1 : 2;
        this.showLoading = true;
        // 去除图标和虚线
        this.dragArr.forEach((item) => {
          if (item.selected) {
            item.selected = false;
          }
          this.isDrag ? item.paint() : item.paintIpImage();
        });

        // 剪切图片
        let cutImage = ctx.getImageData(
          c.width / 2 - RPWIDTH / 2,
          c.height / 2 - RPHEIGHT / 2,
          RPWIDTH,
          RPHEIGHT
        );
        let teC = document.createElement("canvas");
        let teCtx = teC.getContext("2d");
        teC.width = RPWIDTH;
        teC.height = RPHEIGHT;
        // 将截图内容转化base64
        teCtx.putImageData(cutImage, 0, 0, 0, 0, RPWIDTH, RPHEIGHT);
        let newImage = teC.toDataURL("image/png", 1.0);
        teCtx.clearRect(0, 0, RPWIDTH, RPHEIGHT);
        // 合并并导出文件
        this.compoundImg(newImage, type);
      } else {
        this.$message.warning("请选择图片！");
      }
    },
    compoundImg(newImage, type) {
      // 新的canvas存储壁纸
      let nCanvas = document.createElement("canvas");
      let nCtx = nCanvas.getContext("2d");
      let nwidth = RPWIDTH;
      let nheight = RPHEIGHT;
      nCanvas.width = nwidth;
      nCanvas.height = nheight;
      let cutX = 0;
      if (this.isZFold) {
        cutX = -PWIDTH / 2;
      }
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
        nCtx.globalCompositeOperation = "source-atop";
        sctx.drawImage(imgs[1], cutX, 0, nwidth, nheight);
        let pat = nCtx.createPattern(scanvas, "no-repeat");
        nCtx.fillStyle = pat;
        nCtx.fillRect(0, 0, nwidth, nheight);
        nCtx.globalCompositeOperation = "source-over";
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

        if (type === 1) {
          // 普通图片，保存原图
          let natImg = this.ImageToBase64();
          let _fileBlob = this.dataURLtoBlob(natImg); // base64转换成blob
          let imgFile = new File([_fileBlob], new Date().getTime() + ".png"); // blob转换成file文件
          this.uploadFile(imgFile, null); // 上传
        }
      });
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
        tC.width = nwidth;
        tC.height = nheight;
        tCtx.putImageData(cutImage, 0, 0, cX, cY, cwidth, cheight);
        // 将截图内容转化base64
        let image = tC.toDataURL("image/png", 0.8);
        tCtx.clearRect(0, 0, nwidth, nheight);

        // 合成后上传
        this.importFile(imgUrl, image);
      });
    },
    ImageToBase64() {
      // IP图片转base64
      let img = this.dragArr[0].url;
      let newCanvas = document.createElement("canvas");
      let newCtx = newCanvas.getContext("2d");
      newCanvas.width = img.naturalWidth;
      newCanvas.height = img.naturalHeight;
      newCtx.drawImage(img, 0, 0, newCanvas.width, newCanvas.height);
      // 转成jpeg图片格式base64没那么长
      let pdfImg = newCanvas.toDataURL("image/jpeg", 0.8);
      return pdfImg;
    },
    // 颜色转换
    colorRgba(str) {
      // 十六进制颜色值的正则表达式
      let reg = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/;
      let sColor = str.toLowerCase();
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
        let sColorChange = [];
        for (let i = 1; i < 7; i += 2) {
          sColorChange.push(parseInt("0x" + sColor.slice(i, i + 2)));
        }
        return sColorChange;
      } else {
        return [0, 0, 0];
      }
    },
    // 切图
    getCutImg(imgUrl) {
      let fCanvas = document.createElement("canvas");
      let fCtx = fCanvas.getContext("2d");
      this.paintPrintImg(fCanvas, fCtx);

      if (Number(this.materialsType) === 2 && this.manufactor === "MK") {
        // MK TPU（防摔壳），合成生产图（圆角）
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
      // 判断manufactor类型（MK pdf，其余 png）
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
        // png 图片
        // base64转换成blob
        let _fileBlob = this.dataURLtoBlob(pdfimg);
        // blob转换成file文件
        pdfFile = new File(
          [_fileBlob],
          new Date().getTime() + Math.round(Math.random() * 10) + ".png"
        );
      }

      // 上传文件
      this.uploadFile(imgFile, pdfFile);
    },
    uploadFile(img, pdf) {
      let that = this;
      let imgUrl, pdfUrl;
      let uid = window.localStorage.getItem("userId") || "";
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

      getOSSSts({ userId: uid })
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
              "customize/" + uid + "/make/" + currentDate + "/" + randomNameImg,
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
                      uid +
                      "/make/" +
                      currentDate +
                      "/" +
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
    // 获取当前日期
    getCurrentDate() {
      let timeStr = "-";
      let curDate = new Date();
      let curYear = curDate.getFullYear();
      let curMonth = curDate.getMonth() + 1;
      let curDay = curDate.getDate();
      let Current = curYear + timeStr + curMonth + timeStr + curDay;
      return Current;
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
      let dpi =
        item.url.naturalWidth / icw > item.url.naturalHeight / ich
          ? item.url.naturalHeight / ich
          : item.url.naturalWidth / icw;
      this.imgDpi = Math.round(dpi * scale);
      this.$set(item, "dpi", this.imgDpi);

      // 限制图片dpi不能过大，最大dpi 300
      this.imgDpi = this.imgDpi > 300 ? 300 : this.imgDpi;
      this.$emit("dpi", this.imgDpi, scale);
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
      return new File([u8arr], filename, { type: mime });
    },
    // base64转换成blob
    dataURLtoBlob(dataurl) {
      let arr = dataurl.split(",");
      let mime = arr[0].match(/:(.*?);/)[1];
      let bstr = atob(arr[1]);
      let n = bstr.length,
        u8arr = new Uint8Array(n);
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
      }
      return new Blob([u8arr], { type: mime });
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
    // canvas转图片
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
    // 清空canvas
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
      this.dragArr.length = 0;
      // 重新绘制
      this.draw();
    },
    getPixelRatio() {
      var canvas = document.createElement("canvas");
      var context = canvas.getContext("2d");
      var backingStore =
        context.backingStorePixelRatio ||
        context.webkitBackingStorePixelRatio ||
        context.mozBackingStorePixelRatio ||
        context.msBackingStorePixelRatio ||
        context.oBackingStorePixelRatio ||
        context.backingStorePixelRatio ||
        1;
      return (window.devicePixelRatio || 1) / backingStore;
    },
    // 删除定制
    delete() {
      this.reset();
    },
    initCanvas() {
      let width = document.body.clientWidth;
      HEIGHT = document.body.scrollHeight - 118;

      c = document.getElementById("myCanvas");
      ctx = c.getContext("2d");
      c.width = (width - MARGIN_LEFT) * canvasRatio;
      c.height = HEIGHT * canvasRatio;
      c.style.width = width - MARGIN_LEFT + "px";
      c.style.height = HEIGHT + "px";
      if (this.isZFold) {
        c.width = (width - MARGIN_LEFT - this.pwidth / 2) * canvasRatio;
        c.style.width = width - MARGIN_LEFT - this.pwidth / 2 + "px";
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
      let { centerX, centerY } = lastImg;
      centerX = centerX / canvasRatio;
      centerY = centerY / canvasRatio;

      // 用勾股定理算出距离
      let lineA = Math.sqrt(
        Math.pow(centerX - startX, 2) + Math.pow(centerY - startY, 2)
      );
      let lineB = Math.sqrt(
        Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2)
      );
      // 缩放后宽高
      let w = initialW + (lineB - lineA);
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

      // 是否强制铺满血位
      if (
        Number(lastImg.type) === 1 &&
        Number(lastImg.picType) !== 4 &&
        isAllPlace
      ) {
        // 是，限制缩放范围
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
      let centerCX = (document.body.clientWidth - MARGIN_LEFT) / 2;
      if (this.isZFold) {
        centerCX =
          (document.body.clientWidth - MARGIN_LEFT - this.pwidth / 2) / 2;
      }
      let centerCY = (document.body.scrollHeight - 118) / 2;
      // 计算画布坐标
      return {
        centerCX: centerCX, // 画布中心点x
        centerCY: centerCY, // 画布中心点y
        x1: this.isZFold
          ? centerCX - this.pwidth / 4
          : centerCX - this.pwidth / 2, // 左上角x
        x2: this.isZFold
          ? centerCX + this.pwidth / 4
          : centerCX + this.pwidth / 2, // 右上角x
        y1: centerCY - this.pheight / 2, // 左上角y
        y2: centerCY + this.pheight / 2, // 左下角y
      };
    },
  },
  watch: {
    dragArr(arr) {
      return arr;
    },
    spriteArr(arr) {
      this.initCanvas();
      if (arr.length) {
        this.spriteArr = arr;
        // 获取最新添加的数据
        let newImg = arr.slice(-1)[0];
        // 判断是否为IP图  0-网络图 1-普通素材 2-IP素材（不可更改）
        if (newImg.picType === 2) {
          this.isDrag = false;
          let item = new DragImg(newImg, ctx);
          this.dragArr = [];
          this.dragArr.push(item);
          this.drawIpImage();
          this.getImgDPI(item);
          // 清空文字
          this.$emit("clear", 2);
        } else if (newImg.picType === 0 && this.dragArr.length > 0) {
          this.dragArr.forEach((item) => {
            if (item.picType === 2) {
              this.isDrag = false;
              this.$message.warning("当前为IP素材不可添加文字");
              this.drawIpImage();
              // 清空文字
              this.$emit("clear", 0);
            }
          });
        } else {
          this.isDrag = true;
        }
        if (this.isDrag) {
          // 判断，如果是图片则替换，文字判断是替换或者新添加
          this.dragArr.forEach((item, index) => {
            // type 1--图片  2--文字
            if (newImg.type === 1 && item.type === 1) {
              this.dragArr.splice(index, 1);
            }
            if (index < this.dragArr.length) {
              item.selected = false;
            }
            if (
              (newImg.index === index && item.type === 2) ||
              (newImg.index === null && newImg === 2)
            ) {
              this.dragArr.splice(index, 1);
            }
          });
          let item = new DragImg(newImg, ctx);
          if (newImg.type === 1) {
            item.linew = 0;
            item.lineh = 0;
            this.dragArr.unshift(item);
            this.getImgDPI(item);
          } else {
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
  components: {
    loading,
  },
};
</script>

<style scoped lang="less">
.canvas-wrapper {
  position: relative;
  width: 100%;
  height: 100%;
  .bgimg {
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate3d(-50%, -50%, 0);
    z-index: 2;
  }
  #myCanvas {
    width: 100%;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate3d(-50%, -50%, 0);
    filter: opacity(0);
    z-index: 5;

    &.show {
      filter: opacity(0.3);
    }

    &.clip {
      left: 0;
      transform: translate3d(0, -50%, 0);
    }
  }
  #nCanvas {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate3d(-50%, -50%, 0);
    /*z-index: 15;*/
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
    width: 100%;
    height: 100%;
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

  .tips {
    position: absolute;
    top: 50px;
    left: 50%;
    font-size: 16px;
    color: #f00;
    transform: translateX(-50%);
  }
}
</style>
