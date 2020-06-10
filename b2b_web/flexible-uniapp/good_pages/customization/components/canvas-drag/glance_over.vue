<template>
    <view>
        <canvas canvas-id="canvas-drag" :drawData="drawArr" disable-scroll="true" :style="{width:widthVar+ 'rpx', height: modelHeight+'rpx',zIndex: 11,left: ((750-widthVar)/2)+'rpx',top: '100rpx',position: absolute,borderRadius: '80rpx'}"></canvas>
    </view>
</template>

<script>
// components/canvas-drag/index.js
import { countTextLength } from './canvasUtil'
let DELETE_ICON = '../../../../../static/icons/close.png'; // 删除按钮 地址改成直接的项目地址不然跨域会导致web端不能使用
// let DELETE_ICON = 'https://oss.123tool.cn/client/canvas/close.png'; // 删除按钮
// 删除按钮
let DRAG_ICON = '../../../../../static/icons/scale.png'; // 缩放按钮
// let DRAG_ICON = 'https://oss.123tool.cn/client/canvas/scale.png'; // 缩放按钮
// 缩放按钮
// 虚线
const STROKE_COLOR = '#0076A4';
const ROTATE_ENABLED = true;
let isMove = false; // 标识触摸后是否有移动，用来判断是否需要增加操作历史
// 标识触摸后是否有移动，用来判断是否需要增加操作历史
const DEBUG_MODE = false; // 打开调试后会渲染操作区域边框（无背景时有效）
// 打开调试后会渲染操作区域边框（无背景时有效）
const dragGraph = function ({
    x = 30,
    y = 30,
    w,
    h,
    type,
    text,
    fontSize = 20,
    color = 'red',
    url = null,
    rotate = 0,
    sourceId = null,
    selected = false,
    wordStyle = {}
}, canvas, factor) {
    if (type === 'text') {
        canvas.setFontSize(fontSize);
        let textWidth = canvas.measureText(text).width;
        // #ifdef APP-PLUS
        textWidth = countTextLength(this.text, canvas)
        // #endif
        const textHeight = fontSize + 10;
        this.centerX = x + textWidth / 2;
        this.centerY = y + textHeight / 2;
        this.w = textWidth;
        this.h = textHeight;

    } else {
        this.centerX = x + w / 2;
        this.centerY = y + h / 2;
        this.w = w;
        this.h = h;
    }

    this.x = x;
    this.y = y; // 4个顶点坐标

    this.square = [[this.x, this.y], [this.x + this.w, this.y], [this.x + this.w, this.y + this.h], [this.x, this.y + this.h]];
    this.fileUrl = url;
    this.text = text;
    this.fontSize = fontSize;
    this.color = color;
    this.wordStyle = wordStyle;
    this.ctx = canvas;
    this.rotate = rotate;
    this.type = type;
    this.selected = selected;
    this.factor = factor;
    this.sourceId = sourceId;
    this.MIN_WIDTH = 20;
    this.MIN_FONTSIZE = 10;

};
dragGraph.prototype = {
    /**
     * 绘制元素
     */
    paint() {
        this.ctx.save(); // 由于measureText获取文字宽度依赖于样式，所以如果是文字元素需要先设置样式
        let textWidth = 0;
        let textHeight = 0;
        if (this.type === 'text') {
            var fontStyle = '';
            if (this.wordStyle.fontStyle == 'normal') {
                fontStyle = 'normal'
            } else {
                fontStyle = 'italic'
            }
            if (this.wordStyle.fontWeight == 600) {
                this.ctx.font = fontStyle + ' bold 20px sans-serif';
            } else {
                this.ctx.font = fontStyle + ' 400 20px sans-serif';
            }


            this.ctx.setFontSize(this.fontSize);
            this.ctx.setTextBaseline('middle');
            this.ctx.setTextAlign('center');
            this.ctx.setFillStyle(this.color);

            textWidth = this.ctx.measureText(this.text).width + 10;
            // #ifdef APP-PLUS || APP-NVUE
            textWidth = countTextLength(this.text, this.ctx)
            // #endif



            textHeight = this.fontSize + 10; // 字体区域中心点不变，左上角位移
            this.x = this.centerX - textWidth / 2;
            this.y = this.centerY - textHeight / 2;


        } // 旋转元素


        this.ctx.translate(this.centerX, this.centerY);
        this.ctx.rotate(this.rotate * Math.PI / 180);
        this.ctx.translate(-this.centerX, -this.centerY); // 渲染元素
        if (this.type === 'text') {
            this.ctx.fillText(this.text, this.centerX, this.centerY);
        } else if (this.type === 'image') {
            this.ctx.drawImage(this.fileUrl, this.x, this.y, this.w, this.h);

        } // 如果是选中状态，绘制选择虚线框，和缩放图标、删除图标

        if (this.selected) {
            this.ctx.setLineDash([2, 5]);
            this.ctx.setLineWidth(2);
            this.ctx.setStrokeStyle(STROKE_COLOR);
            this.ctx.lineDashOffset = 6;

            if (this.type === 'text') {
                this.ctx.strokeRect(this.x, this.y, textWidth, textHeight);
                this.ctx.drawImage(DELETE_ICON, this.x - 15, this.y - 15, 30, 30);
                this.ctx.drawImage(DRAG_ICON, this.x + textWidth - 15, this.y + textHeight - 15, 30, 30);
            } else {
                this.ctx.strokeRect(this.x, this.y, this.w, this.h);
                this.ctx.drawImage(DELETE_ICON, this.x - 15, this.y - 15, 30, 30);
                this.ctx.drawImage(DRAG_ICON, this.x + this.w - 15, this.y + this.h - 15, 30, 30);
            }
        }

        this.ctx.restore();
    },
    /**
     * 给矩形描边
     * @private
     */
    _drawBorder() {
        let p = this.square;
        let ctx = this.ctx;
        this.ctx.save();
        this.ctx.beginPath();
        ctx.setStrokeStyle('orange');

        this._draw_line(this.ctx, p[0], p[1]);

        this._draw_line(this.ctx, p[1], p[2]);

        this._draw_line(this.ctx, p[2], p[3]);

        this._draw_line(this.ctx, p[3], p[0]);

        ctx.restore();
    },

    /**
     * 画一条线
     * @param ctx
     * @param a
     * @param b
     * @private
     */
    _draw_line(ctx, a, b) {
        ctx.moveTo(a[0], a[1]);
        ctx.lineTo(b[0], b[1]);
        ctx.stroke();
    },

    /**
     * 判断点击的坐标落在哪个区域
     * @param {*} x 点击的坐标
     * @param {*} y 点击的坐标
     */
    isInGraph(x, y) {
        console.log('isInGraph')
        // 删除区域左上角的坐标和区域的高度宽度
        const delW = 30;
        const delH = 30; // 旋转后的删除区域坐标

        const transformedDelCenter = this._rotatePoint(this.x, this.y, this.centerX, this.centerY, this.rotate);

        const transformDelX = transformedDelCenter[0] - delW / 2;
        const transformDelY = transformedDelCenter[1] - delH / 2; // 变换区域左上角的坐标和区域的高度宽度

        const scaleW = 30;
        const scaleH = 30;
        // #ifdef APP-PLUS || APP-VUE
        this.w = countTextLength(this.text, this.ctx)
        // #endif
        const transformedScaleCenter = this._rotatePoint(this.x + this.w, this.y + this.h, this.centerX, this.centerY, this.rotate); // 旋转后的变换区域坐标

        let transformScaleX = transformedScaleCenter[0] - scaleW / 2;
        let transformScaleY = transformedScaleCenter[1] - scaleH / 2; // 调试使用，标识可操作区域

        console.log(x, y, this.w, this.h, this.square, this.insidePolygon(this.square, [x, y]))

        if (DEBUG_MODE) {
            // 标识删除按钮区域
            this.ctx.setLineWidth(1);
            this.ctx.setStrokeStyle('red');
            this.ctx.strokeRect(transformDelX, transformDelY, delW, delH); // 标识旋转/缩放按钮区域

            this.ctx.setLineWidth(1);
            this.ctx.setStrokeStyle('black');
            this.ctx.strokeRect(transformScaleX, transformScaleY, scaleW, scaleH); // 标识移动区域

            this._drawBorder();
        }

        // this.selected = true
        if (x - transformScaleX >= 0 && y - transformScaleY >= 0 && transformScaleX + scaleW - x >= 0 && transformScaleY + scaleH - y >= 0) {
            console.log('缩放区域')
            // 缩放区域
            return 'transform';
        } else if (x - transformDelX >= 0 && y - transformDelY >= 0 && transformDelX + delW - x >= 0 && transformDelY + delH - y >= 0) {
            console.log('删除区域')
            // 删除区域
            return 'del';
        } else if (this.insidePolygon(this.square, [x, y])) {
            console.log('在选择区域里面')
            return 'move';
        } else { // 不在选择区域里面
            // this.selected = false
        }

        return false;
    },

    /**
     *  判断一个点是否在多边形内部
     *  @param points 多边形坐标集合
     *  @param testPoint 测试点坐标
     *  返回true为真，false为假
     *  */
    insidePolygon(points, testPoint) {
        let x = testPoint[0],
            y = testPoint[1];
        let inside = false;

        for (let i = 0, j = points.length - 1; i < points.length; j = i++) {
            let xi = points[i][0],
                yi = points[i][1];
            let xj = points[j][0],
                yj = points[j][1];
            let intersect = yi > y != yj > y && x < (xj - xi) * (y - yi) / (yj - yi) + xi;
            if (intersect) inside = !inside;
        }

        return inside;
    },

    /**
     * 计算旋转后矩形四个顶点的坐标（相对于画布）
     * @private
     */
    _rotateSquare() {
        this.square = [this._rotatePoint(this.x, this.y, this.centerX, this.centerY, this.rotate), this._rotatePoint(this.x + this.w, this.y, this.centerX, this.centerY, this.rotate), this._rotatePoint(this.x + this.w, this.y + this.h, this.centerX, this.centerY, this.rotate), this._rotatePoint(this.x, this.y + this.h, this.centerX, this.centerY, this.rotate)];
    },

    /**
     * 计算旋转后的新坐标（相对于画布）
     * @param x
     * @param y
     * @param centerX
     * @param centerY
     * @param degrees
     * @returns {*[]}
     * @private
     */
    _rotatePoint(x, y, centerX, centerY, degrees) {
        let newX = (x - centerX) * Math.cos(degrees * Math.PI / 180) - (y - centerY) * Math.sin(degrees * Math.PI / 180) + centerX;
        let newY = (x - centerX) * Math.sin(degrees * Math.PI / 180) + (y - centerY) * Math.cos(degrees * Math.PI / 180) + centerY;
        return [newX, newY];
    },

    /**
     *
     * @param {*} px 手指按下去的坐标
     * @param {*} py 手指按下去的坐标
     * @param {*} x 手指移动到的坐标
     * @param {*} y 手指移动到的坐标
     * @param {*} currentGraph 当前图层的信息
     */
    transform(px, py, x, y, currentGraph) {
        // 获取选择区域的宽度高度
        if (this.type === 'text') {
            this.ctx.setFontSize(this.fontSize);
            let textWidth = this.ctx.measureText(this.text).width;
            // #ifdef APP-PLUS
            textWidth = countTextLength(this.text, this.ctx)
            // #endif
            console.log('transform:' + textWidth)
            const textHeight = this.fontSize + 10;
            this.w = textWidth;
            this.h = textHeight; // 字体区域中心点不变，左上角位移

            this.x = this.centerX - textWidth / 2;
            this.y = this.centerY - textHeight / 2;
        } else {
            this.centerX = this.x + this.w / 2;
            this.centerY = this.y + this.h / 2;
        }

        const diffXBefore = px - this.centerX;
        const diffYBefore = py - this.centerY;
        const diffXAfter = x - this.centerX;
        const diffYAfter = y - this.centerY;
        const angleBefore = Math.atan2(diffYBefore, diffXBefore) / Math.PI * 180;
        const angleAfter = Math.atan2(diffYAfter, diffXAfter) / Math.PI * 180; // 旋转的角度

        if (ROTATE_ENABLED) {
            this.rotate = currentGraph.rotate + angleAfter - angleBefore;
        }

        const lineA = Math.sqrt(Math.pow(this.centerX - px, 2) + Math.pow(this.centerY - py, 2));
        const lineB = Math.sqrt(Math.pow(this.centerX - x, 2) + Math.pow(this.centerY - y, 2));

        if (this.type === 'image') {
            let resize_rito = lineB / lineA;
            let new_w = currentGraph.w * resize_rito;
            let new_h = currentGraph.h * resize_rito;

            if (currentGraph.w < currentGraph.h && new_w < this.MIN_WIDTH) {
                new_w = this.MIN_WIDTH;
                new_h = this.MIN_WIDTH * currentGraph.h / currentGraph.w;
            } else if (currentGraph.h < currentGraph.w && new_h < this.MIN_WIDTH) {
                new_h = this.MIN_WIDTH;
                new_w = this.MIN_WIDTH * currentGraph.w / currentGraph.h;
            }

            this.w = new_w;
            this.h = new_h;
            this.x = currentGraph.x - (new_w - currentGraph.w) / 2;
            this.y = currentGraph.y - (new_h - currentGraph.h) / 2;
        } else if (this.type === 'text') {
            const fontSize = currentGraph.fontSize * ((lineB - lineA) / lineA + 1);
            this.fontSize = fontSize <= this.MIN_FONTSIZE ? this.MIN_FONTSIZE : fontSize; // 旋转位移后重新计算坐标

            this.ctx.setFontSize(this.fontSize);
            let textWidth = this.ctx.measureText(this.text).width;
            // #ifdef APP-PLUS
            textWidth = countTextLength(this.text, this.ctx)
            // #endif
            const textHeight = this.fontSize + 10;
            this.w = textWidth;
            this.h = textHeight; // 字体区域中心点不变，左上角位移

            this.x = this.centerX - textWidth / 2;
            this.y = this.centerY - textHeight / 2;
        }
    },

    toPx(rpx) {
        return rpx * this.factor;
    }

};
export default {
    data() {
        return {
            bgImage: '',
            history: [],
            heightVar: "",
            Isopacity: false,
            widthVar: 0,
        };
    },
    props: {
        hollowOut: {
            type: String,
            default: ''
        },
        graph: {
            type: Object,
            default: () => ({})
        },
        bgColor: {
            type: String,
            default: ''
        },
        bgSourceId: {
            type: String,
            default: ''
        },
        width: {
            type: Number,
            default: 750
        },
        height: {
            type: Number,
            default: 750
        },
        enableUndo: {
            type: Boolean,
            default: false
        },
        backgroundImage: {
            type: String,
            default: ''
        },
        floorImage: {
            type: String,
            default: ''
        },
        proportion: {
            type: Number,
            default: 1
        },
        windowHeight: {
            type: Number,
            default: 1
        },
        modelHeight: {
            type: Number,
            default: 1
        }
    },
    watch: {

        graph: {
            handler: 'onGraphChange',
            deep: true
        },
        height: {
            handler(newName, oldName) {
                if (oldName != undefined) {
                    this.heightVar = newName * 5;
                    this.marginTopVar = (this.windowHeight - this.heightVar) / 2;
                }
            },
            immediate: true
        },
        proportion: {
            handler(newName, oldName) {
                if (oldName != undefined) {
                    this.widthVar = this.modelHeight / newName;
                    this.leftFrameVar = (750 - this.widthVar) / 2;
                }
            },
            immediate: true
        }
    },
    /**
     * 绘制元素
     */
    paint() {
        this.ctx.save(); // 由于measureText获取文字宽度依赖于样式，所以如果是文字元素需要先设置样式
        let textWidth = 0;
        let textHeight = 0;

        if (this.type === 'text') {
            this.ctx.setFontSize(this.fontSize);
            this.ctx.setTextBaseline('middle');
            this.ctx.setTextAlign('center');
            this.ctx.setFillStyle(this.color);
            textWidth = this.ctx.measureText(this.text).width;
            // #ifdef APP-PLUS
            textWidth = countTextLength(this.text, this.ctx)
            // #endif
            textHeight = this.fontSize + 10; // 字体区域中心点不变，左上角位移

            this.x = this.centerX - textWidth / 2;
            this.y = this.centerY - textHeight / 2;
        } // 旋转元素

        this.ctx.translate(this.centerX, this.centerY);
        this.ctx.rotate(this.rotate * Math.PI / 180);
        this.ctx.translate(-this.centerX, -this.centerY); // 渲染元素

        if (this.type === 'text') {
            this.ctx.fillText(this.text, this.centerX, this.centerY);
        } else if (this.type === 'image') {
            this.ctx.drawImage(this.fileUrl, this.x, this.y, this.w, this.h);
        } // 如果是选中状态，绘制选择虚线框，和缩放图标、删除图标

        if (this.selected == true) {
            this.ctx.setLineDash([2, 5]);
            this.ctx.setLineWidth(2);
            this.ctx.setStrokeStyle(STROKE_COLOR);
            this.ctx.lineDashOffset = 6;

            if (this.type === 'text') {
                this.ctx.strokeRect(this.x, this.y, textWidth, textHeight);
                this.ctx.drawImage(DELETE_ICON, this.x - 15, this.y - 15, 30, 30);
                this.ctx.drawImage(DRAG_ICON, this.x + textWidth - 15, this.y + textHeight - 15, 30, 30);
            } else {
                this.ctx.strokeRect(this.x, this.y, this.w, this.h);
                this.ctx.drawImage(DELETE_ICON, this.x - 15, this.y - 15, 30, 30);
                this.ctx.drawImage(DRAG_ICON, this.x + this.w - 15, this.y + this.h - 15, 30, 30);
            }
        }

        this.ctx.restore();
    },

    /**
     * 给矩形描边
     * @private
     */
    _drawBorder() {
        let p = this.square;
        let ctx = this.ctx;
        this.ctx.save();
        this.ctx.beginPath();
        ctx.setStrokeStyle('orange');

        this._draw_line(this.ctx, p[0], p[1]);

        this._draw_line(this.ctx, p[1], p[2]);

        this._draw_line(this.ctx, p[2], p[3]);

        this._draw_line(this.ctx, p[3], p[0]);

        ctx.restore();
    },

    /**
     * 画一条线
     * @param ctx
     * @param a
     * @param b
     * @private
     */
    _draw_line(ctx, a, b) {
        ctx.moveTo(a[0], a[1]);
        ctx.lineTo(b[0], b[1]);
        ctx.stroke();
    },

    /**
     * 判断点击的坐标落在哪个区域
     * @param {*} x 点击的坐标
     * @param {*} y 点击的坐标
     */
    isInGraph(x, y) {
        // 删除区域左上角的坐标和区域的高度宽度
        const delW = 30;
        const delH = 30; // 旋转后的删除区域坐标

        const transformedDelCenter = this._rotatePoint(this.x, this.y, this.centerX, this.centerY, this.rotate);

        const transformDelX = transformedDelCenter[0] - delW / 2;
        const transformDelY = transformedDelCenter[1] - delH / 2; // 变换区域左上角的坐标和区域的高度宽度

        const scaleW = 30;
        const scaleH = 30;

        const transformedScaleCenter = this._rotatePoint(this.x + this.w, this.y + this.h, this.centerX, this.centerY, this.rotate); // 旋转后的变换区域坐标


        const transformScaleX = transformedScaleCenter[0] - scaleW / 2;
        const transformScaleY = transformedScaleCenter[1] - scaleH / 2; // 调试使用，标识可操作区域

        if (DEBUG_MODE) {
            // 标识删除按钮区域
            this.ctx.setLineWidth(1);
            this.ctx.setStrokeStyle('red');
            this.ctx.strokeRect(transformDelX, transformDelY, delW, delH); // 标识旋转/缩放按钮区域

            this.ctx.setLineWidth(1);
            this.ctx.setStrokeStyle('black');
            this.ctx.strokeRect(transformScaleX, transformScaleY, scaleW, scaleH); // 标识移动区域

            this._drawBorder();
        }
        // this.selected = true
        if (x - transformScaleX >= 0 && y - transformScaleY >= 0 && transformScaleX + scaleW - x >= 0 && transformScaleY + scaleH - y >= 0) {
            // 缩放区域
            return 'transform';
        } else if (x - transformDelX >= 0 && y - transformDelY >= 0 && transformDelX + delW - x >= 0 && transformDelY + delH - y >= 0) {
            // 删除区域
            return 'del';
        } else if (this.insidePolygon(this.square, [x, y])) {

            return 'move';
        } else { // 不在选择区域里面
            // this.selected = false
        }
        return false;
    },

    /**
     *  判断一个点是否在多边形内部
     *  @param points 多边形坐标集合
     *  @param testPoint 测试点坐标
     *  返回true为真，false为假
     *  */
    insidePolygon(points, testPoint) {
        let x = testPoint[0],
            y = testPoint[1];
        let inside = false;

        for (let i = 0, j = points.length - 1; i < points.length; j = i++) {
            let xi = points[i][0],
                yi = points[i][1];
            let xj = points[j][0],
                yj = points[j][1];
            let intersect = yi > y != yj > y && x < (xj - xi) * (y - yi) / (yj - yi) + xi;
            if (intersect) inside = !inside;
        }

        return inside;
    },

    /**
     * 计算旋转后矩形四个顶点的坐标（相对于画布）
     * @private
     */
    _rotateSquare() {
        this.square = [this._rotatePoint(this.x, this.y, this.centerX, this.centerY, this.rotate), this._rotatePoint(this.x + this.w, this.y, this.centerX, this.centerY, this.rotate), this._rotatePoint(this.x + this.w, this.y + this.h, this.centerX, this.centerY, this.rotate), this._rotatePoint(this.x, this.y + this.h, this.centerX, this.centerY, this.rotate)];
    },

    /**
     * 计算旋转后的新坐标（相对于画布）
     * @param x
     * @param y
     * @param centerX
     * @param centerY
     * @param degrees
     * @returns {*[]}
     * @private
     */
    _rotatePoint(x, y, centerX, centerY, degrees) {
        let newX = (x - centerX) * Math.cos(degrees * Math.PI / 180) - (y - centerY) * Math.sin(degrees * Math.PI / 180) + centerX;
        let newY = (x - centerX) * Math.sin(degrees * Math.PI / 180) + (y - centerY) * Math.cos(degrees * Math.PI / 180) + centerY;
        return [newX, newY];
    },

    /**
     *
     * @param {*} px 手指按下去的坐标
     * @param {*} py 手指按下去的坐标
     * @param {*} x 手指移动到的坐标
     * @param {*} y 手指移动到的坐标
     * @param {*} currentGraph 当前图层的信息
     */
    transform(px, py, x, y, currentGraph) {
        // 获取选择区域的宽度高度
        if (this.type === 'text') {
            this.ctx.setFontSize(this.fontSize);
            let textWidth = this.ctx.measureText(this.text).width;
            // #ifdef APP-PLUS
            textWidth = countTextLength(this.text, this.ctx)
            // #endif
            const textHeight = this.fontSize + 10;
            this.w = textWidth;
            this.h = textHeight; // 字体区域中心点不变，左上角位移

            this.x = this.centerX - textWidth / 2;
            this.y = this.centerY - textHeight / 2;
        } else {
            this.centerX = this.x + this.w / 2;
            this.centerY = this.y + this.h / 2;
        }

        const diffXBefore = px - this.centerX;
        const diffYBefore = py - this.centerY;
        const diffXAfter = x - this.centerX;
        const diffYAfter = y - this.centerY;
        const angleBefore = Math.atan2(diffYBefore, diffXBefore) / Math.PI * 180;
        const angleAfter = Math.atan2(diffYAfter, diffXAfter) / Math.PI * 180; // 旋转的角度

        if (ROTATE_ENABLED) {
            this.rotate = currentGraph.rotate + angleAfter - angleBefore;
        }

        const lineA = Math.sqrt(Math.pow(this.centerX - px, 2) + Math.pow(this.centerY - py, 2));
        const lineB = Math.sqrt(Math.pow(this.centerX - x, 2) + Math.pow(this.centerY - y, 2));

        if (this.type === 'image') {
            let resize_rito = lineB / lineA;
            let new_w = currentGraph.w * resize_rito;
            let new_h = currentGraph.h * resize_rito;
            if (currentGraph.w < currentGraph.h && new_w < this.MIN_WIDTH) {
                new_w = this.MIN_WIDTH;
                new_h = this.MIN_WIDTH * currentGraph.h / currentGraph.w;
            } else if (currentGraph.h < currentGraph.w && new_h < this.MIN_WIDTH) {
                new_h = this.MIN_WIDTH;
                new_w = this.MIN_WIDTH * currentGraph.w / currentGraph.h;
            }
            this.w = new_w;
            this.h = new_h;
            this.x = currentGraph.x - (new_w - currentGraph.w) / 2;
            this.y = currentGraph.y - (new_h - currentGraph.h) / 2;
        } else if (this.type === 'text') {
            const fontSize = currentGraph.fontSize * ((lineB - lineA) / lineA + 1);
            this.fontSize = fontSize <= this.MIN_FONTSIZE ? this.MIN_FONTSIZE : fontSize; // 旋转位移后重新计算坐标
            this.ctx.setFontSize(this.fontSize);
            const textWidth = this.ctx.measureText(this.text).width;
            const textHeight = this.fontSize + 10;
            this.w = textWidth;
            this.h = textHeight; // 字体区域中心点不变，左上角位移
            this.x = this.centerX - textWidth / 2;
            this.y = this.centerY - textHeight / 2;
        }
    },

    toPx(rpx) {
        return rpx * this.factor;
    },

    beforeMount() {
        const sysInfo = wx.getSystemInfoSync();
        const screenWidth = sysInfo.screenWidth;
        this.factor = screenWidth / 750;
        if (typeof this.drawArr === 'undefined') {
            this.drawArr = [];
        }
        this.ctx = uni.createCanvasContext('canvas-drag', this);
        this.draw();
    },

    created() {

    },

    methods: {
        toPx(rpx) {
            return rpx * this.factor;
        },

        initBg() {
            this.bgColor = '';
            this.bgSourceId = '';
            this.bgImage = '';
        },

        initHistory() {
            this.history = [];
        },

        recordHistory() {
            if (!this.enableUndo) {
                return;
            }
            this.exportJson().then(imgArr => {
                this.history.push(JSON.stringify(imgArr));
            }).catch(e => {
                console.error(e);
            });
        },



        onGraphChange(n, o) {
            if (JSON.stringify(n) === '{}') return;
            this.drawArr.push(new dragGraph(Object.assign({
                x: 30,
                y: 30
            }, n), this.ctx, this.factor));
            this.draw(); // 参数有变化时记录历史
            this.recordHistory();
        },


        //绘制draw
        draw() {
            if (this.bgImage !== '') {
                this.ctx.drawImage(this.bgImage, 0, 0, this.toPx(this.width), this.toPx(this.height));
            }

            if (this.bgColor !== '') {
                this.ctx.save();
                this.ctx.setFillStyle(this.bgColor);
                this.ctx.fillRect(0, 0, this.toPx(this.width), this.toPx(this.height));
                this.ctx.restore();
            }

            this.drawArr.forEach(item => {
                item.paint(item);
            });
            return new Promise(resolve => {
                this.ctx.draw(false, () => {
                    resolve();
                });
            });
        },

        start(e) {
            isMove = false; // 重置移动标识
            // const {
            //     x,
            //     y
            // } = e.touches[0];
            const x = e.touches[0].clientX;
            const y = e.touches[0].clientY;
            this.tempGraphArr = [];
            let lastDelIndex = null; // 记录最后一个需要删除的索引
            this.drawArr && this.drawArr.forEach((item, index) => {
                const action = item.isInGraph(x, y);
                if (action) {
                    item.action = action;
                    this.tempGraphArr.push(item); // 保存点击时的坐标
                    this.currentTouch = {
                        x,
                        y
                    };
                    if (action === 'del') {
                        lastDelIndex = index; // 标记需要删除的元素
                        item.selected = true;
                    }
                } else {
                    item.action = false;
                    item.selected = false;
                }
            }); // 保存点击时元素的信息
            if (this.tempGraphArr.length > 0) {
                for (let i = 0; i < this.tempGraphArr.length; i++) {
                    let lastIndex = this.tempGraphArr.length - 1; // 对最后一个元素做操作
                    if (i === lastIndex) {
                        // 未选中的元素，不执行删除和缩放操作
                        if (lastDelIndex !== null && this.tempGraphArr[i].selected) {
                            if (this.drawArr[lastDelIndex].action == 'del') {
                                this.drawArr.splice(lastDelIndex, 1);
                                this.ctx.clearRect(0, 0, this.toPx(this.width), this.toPx(this.height));
                            }
                        } else {
                            this.tempGraphArr[lastIndex].selected = true;
                            this.currentGraph = Object.assign({}, this.tempGraphArr[lastIndex]);
                        }
                    } else {
                        // 不是最后一个元素，不需要选中，也不记录状态
                        this.tempGraphArr[i].action = false;
                        this.tempGraphArr[i].selected = false;
                    }
                }
            }
            this.draw();
        },

        move(e) {
            // const {
            //     x,
            //     y
            // } = e.touches[0];
            const x = e.touches[0].clientX;
            const y = e.touches[0].clientY;
            if (this.tempGraphArr && this.tempGraphArr.length > 0) {
                isMove = true; // 有选中元素，并且有移动时，设置移动标识
                this.Isopacity = true;
                this.selected = true
                const currentGraph = this.tempGraphArr[this.tempGraphArr.length - 1];

                if (currentGraph.action === 'move') {
                    currentGraph.centerX = this.currentGraph.centerX + (x - this.currentTouch.x);
                    currentGraph.centerY = this.currentGraph.centerY + (y - this.currentTouch.y); // 使用中心点坐标计算位移，不使用 x,y 坐标，因为会受旋转影响。

                    if (currentGraph.type !== 'text') {
                        currentGraph.x = currentGraph.centerX - this.currentGraph.w / 2;
                        currentGraph.y = currentGraph.centerY - this.currentGraph.h / 2;
                    }
                } else if (currentGraph.action === 'transform') {
                    currentGraph.transform(this.currentTouch.x, this.currentTouch.y, x, y, this.currentGraph);
                } // 更新4个坐标点（相对于画布的坐标系）


                currentGraph._rotateSquare();

                this.draw();
            }
        },

        end(e) {
            this.tempGraphArr = [];
            if (isMove) {
                isMove = false; // 重置移动标识
                // 用户操作结束时记录历史
                setTimeout(() => {
                    this.Isopacity = false;
                }, 3000);
                this.recordHistory();
            }
        },
        exportFun() {
            return new Promise((resolve, reject) => {
                this.drawArr = this.drawArr.map(item => {
                    item.selected = false;
                    return item;
                });
                // var heightVar = ((window.innerWidth / 750) * (450 * this.proportion));

                var ratio = uni.getStorageSync("ratio");
                var devicePixelRatio = uni.getStorageSync("devicePixelRatio");
                var x = (750 - this.widthVar) / 2
                this.draw().then(() => {
                    uni.canvasToTempFilePath({
                        y: 0,
                        x: 0,
                        width: this.widthVar / ratio, // 选中canvas多宽
                        height: this.modelHeight / ratio, // 选中canvas多高
                        destWidth: (this.widthVar / ratio) * 1,//this.width * 7, // 生成的图片多宽
                        destHeight: (this.modelHeight / ratio) * 1,//this.height * 7, // 生成的图片多高
                        canvasId: 'canvas-drag',
                        quality: 1,
                        success: res => {
                            resolve(res.tempFilePath);
                        },
                        fail: e => {
                            reject(e);
                        }
                    }, this);
                });
            });
        },
        exportJson() {
            return new Promise((resolve, reject) => {
                let exportArr = this.drawArr.map(item => {
                    item.selected = false;
                    switch (item.type) {
                        case 'image':
                            return {
                                type: 'image',
                                url: item.fileUrl,
                                y: item.y,
                                x: item.x,
                                w: item.w,
                                h: item.h,
                                rotate: item.rotate,
                                sourceId: item.sourceId
                            };
                            break;

                        case 'text':
                            return {
                                type: 'text',
                                text: item.text,
                                color: item.color,
                                fontSize: item.fontSize,
                                y: item.y,
                                x: item.x,
                                w: item.w,
                                h: item.h,
                                rotate: item.rotate,
                                wordStyle: item.wordStyle
                            };
                            break;
                    }
                });

                if (this.bgImage) {
                    let tmp_img_config = {
                        type: 'bgImage',
                        url: this.bgImage
                    };

                    if (this.bgSourceId) {
                        tmp_img_config['sourceId'] = this.bgSourceId;
                    }

                    exportArr.unshift(tmp_img_config);
                } else if (this.bgColor) {
                    exportArr.unshift({
                        type: 'bgColor',
                        color: this.bgColor
                    });
                }

                resolve(exportArr);
            });
        },
        //编辑图层
        setCoverage() {
            this.$refs.set_soverage.setCoverage(this.drawArr);
        },
        // 改变文本
        changeText(text) {
            const selected = this.drawArr.filter(item => item.selected);
            if (selected.length > 0) {
                console.log(text, selected)
                selected[0].text = text;
            } else {
                return
            }
            this.draw(); // 改变文字颜色时记录历史
            this.recordHistory();
        },



        clearCanvas() {
            this.ctx.clearRect(0, 0, this.toPx(this.width), this.toPx(this.height));
            this.ctx.draw();
            this.drawArr = [];
            this.initBg(); // 重置绘画背景
            this.initHistory(); // 清空历史记录
        }

    }
};

</script>
<style scoped lang="scss">
.canvas_copy {
    position: absolute;
    top: 0;
    width: 750rpx;
    height: 1100rpx;
    overflow: hidden;
    margin: 0 auto;
    z-index: 1;
}
.iscanvas_copy {
    opacity: 0.9;
}
.canvas_copy:before,
.canvas_copy:after {
    content: "";
    position: absolute;
}
.canvas_copy:before {
    top: 154rpx;
    margin-left: 155rpx;
    width: 444rpx;
    height: var(--proportion);
    border-radius: 60rpx;
    box-shadow: 0rpx 0rpx 0rpx 9999rpx #dfdfdf;
    z-index: -1;
}
.canvas_copy:after {
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
}
.shade {
    position: absolute;
    top: 0;
    filter: alpha(opacity(0.3));
    opacity: 0.7;
}

.up-box {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    background-repeat: no-repeat;
    background-position: 50% 150rpx;
    background-clip: content-box;
    z-index: 10;
}
.up-box1 {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    background-repeat: no-repeat;
    background-position: 50% 150rpx;
    background-clip: content-box;
    z-index: 0;
}
.movable-label {
    margin-top: 300rpx;
    width: 750rpx;
    height: 400rpx;
    background: #eee;
}
.movable-block {
    width: 120rpx;
    height: 120rpx;
    background: #ccc;
}
.movable-block .image-con {
    width: 100%;
    height: 100%;
}
</style>