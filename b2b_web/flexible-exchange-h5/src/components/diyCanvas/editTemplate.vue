<!--
 * @Author: yaowei
 * @Date: 2019-12-29 14:30:06
 * @LastEditors: yaowei
 * @LastEditTime: 2019-01-13 10:00:14
-->
<template>
  <div class="template-wrap">
    <div class="header">
      <span class="back-btn" @click="handleBack">退出</span>
      <span class="submit-btn" @click="handleSave">保存</span>
    </div>

    <div class="handle-wrap" :style="{height: wheight + 'px'}" ref="handleWrap">
      <div class="cover-wrap">
        <div id="container" class="canvas"></div>
        <img :style="{height: pheight + 'px'}" :src="curFrame" alt="">
      </div>
    </div>

    <ul class="handle-bottom handle-list">
      <li @click="handleReset()">
        <span class="sprite-icon icon-template_icon_Reset"></span>
        <p>重置</p>
      </li>
      <li @click="handleShowTurn()">
        <span class="sprite-icon icon-template_icon_turn"></span>
        <p>旋转</p>
      </li>
      <li @click="handleHSB()">
        <span class="sprite-icon icon-template_icon_hsb"></span>
        <p>HSB调整</p>
      </li>
      <!-- <li @click="handleFilter()">
        <span class="sprite-icon icon-template_icon_Filter"></span>
        <p>滤镜</p>
      </li> -->
    </ul>

    <!-- 翻转 -->
    <div class="turn-wrap" v-show="showTurn">
      <ul class="handle-list">
        <li @click="handleTurn('l-r')">
          <span class="sprite-icon icon-template_icon_Freeflip"></span>
          <p>左右翻转</p>
        </li>
        <li @click="handleTurn('t-b')">
          <span class="sprite-icon icon-template_icon_flip2"></span>
          <p>上下翻转</p>
        </li>
      </ul>
      <div class="bottom-wrap">
        <van-icon name="cross" @click="handleResetTurn()" />
        <span>翻转</span>
        <van-icon name="success" @click="showTurn = false" />
      </div>
    </div>

    <!-- HSB -->
    <div class="hsb-wrap" v-show="showHSB">
      <div class="slider-wrap">
        <!-- 对比度 -->
        <van-slider v-show="curHSB === 0" v-model="curContast" :min="-180" :max="180" :step="1" active-color="#FFDA01"
          @input="getContrast()">
        </van-slider>
        <!-- 亮度 -->
        <van-slider v-show="curHSB === 1" v-model="curBrighten" :min="-1" :max="1" :step="0.05" active-color="#FFDA01"
          @input="getBrighten()">
        </van-slider>
        <!-- 饱和度 -->
        <van-slider v-show="curHSB === 2" v-model="curSaturation" :min="-10" :max="10" :step="0.1"
          active-color="#FFDA01" @input="getSaturation()">
        </van-slider>
      </div>
      <ul class="handle-list">
        <li @click="curHSB = 0">
          <span class="sprite-icon"
            :class="(curHSB === 0) ? 'icon-template_icon_contrast_pre': 'icon-template_icon_contrast'"></span>
          <p>对比度</p>
        </li>
        <li @click="curHSB = 1">
          <span class="sprite-icon "
            :class="(curHSB === 1) ? 'icon-template_icon_brightness_pre': 'icon-template_icon_brightness'"></span>
          <p>亮度</p>
        </li>
        <li @click="curHSB = 2">
          <span class="sprite-icon "
            :class="(curHSB === 2) ? 'icon-template_icon_saturation_pre': 'icon-template_icon_saturation'"></span>
          <p>饱和度</p>
        </li>
      </ul>
      <div class="bottom-wrap">
        <van-icon name="cross" @click="handleResetHSB()" />
        <span>HSB调整</span>
        <van-icon name="success" @click="showHSB = false" />
      </div>
    </div>
  </div>
</template>

<script>
  import Konva from "konva"
  let stage; // 画布
  let layer; // 层
  let imgObj; // 图片
  export default {
    name: "template",
    props: {
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
      // 当前点击热区信息
      curItem: {
        type: Object,
        default: {}
      },
      // 画布高
      wheight: {
        type: Number,
        default: 0,
      },
    },
    data() {
      return {
        curImg: "",
        curFrame: "",
        showTurn: false, // 是否显示翻转操作
        showHSB: false, // 是否显示HSB操作
        originVal: [], // 初始化数据
        curTurn: {
          x: 1,
          y: 1,
        }, // 当前翻转
        tr: null, // 图片操作（旋转，缩放）
        curHSB: 0, // 当前操作HSB
        curContast: 0, // 当前对比度
        curBrighten: 0, // 当前亮度
        curSaturation: 0, // 当前饱和度
      }
    },
    watch: {
      curItem(arr) {
        return arr;
      },
    },
    mounted() {
      // 获取当前操作元素
      this.curImg = this.curItem.url;
      this.curFrame = this.curItem.frame;
      this.originVal = this.curItem; // 保存初始化数据

      // 初始化图片
      this.initData();
    },
    methods: {
      // 初始化图片
      initData() {
        // 获取当前画布高度
        let curHeight = this.$parent.wheight;

        // 创建画布
        stage = new Konva.Stage({
          container: 'container',
          width: window.screen.width,
          height: curHeight
        });
        // 创建层
        layer = new Konva.Layer();
        stage.add(layer);
        // 添加图片
        var imageObj = new Image();
        imageObj.onload = () => {
          imgObj = new Konva.Image({
            // 起始点x,y
            x: this.originVal.x + this.originVal.width / 2,
            y: this.originVal.y + this.originVal.height / 2,
            image: imageObj,
            width: this.originVal.width,
            height: this.originVal.height,
            // 中心点
            offset: {
              x: this.originVal.width / 2,
              y: this.originVal.height / 2,
            },
            draggable: true
          });

          // 设置图片可操作
          this.tr = new Konva.Transformer({
            nodes: [imgObj],
            keepRatio: true,
          });
          layer.add(imgObj);
          layer.add(this.tr);
          layer.draw();
        };
        imageObj.src = this.curImg;

      },
      // 退出
      handleBack() {
        this.$parent.showTemplate = false;
      },
      // 保存
      handleSave() {
        this.tr.detach(); // 移除操作
        var dataURL = stage.toDataURL({
          pixelRatio: this.originVal.scale,
          x: this.originVal.x,
          y: this.originVal.y,
          width: imgObj.width(),
          height: imgObj.height(),
        });
        this.$parent.$parent.changeImage({}, dataURL, 0, 3, 1);

        this.$parent.showTemplate = false;
      },
      // 重置
      handleReset() {
        // 初始化图片
        this.initData();
      },
      // 显示翻转
      handleShowTurn() {
        // 显示翻转操作
        this.showTurn = true;
        // 记录当前翻转
        this.curTurn = {
          x: Number(imgObj.scale().x),
          y: Number(imgObj.scale().y),
        }
      },
      // 翻转
      handleTurn(type) {
        // type：t-b 上下翻转，l-r 左右翻转
        if (type === "t-b") {
          // 上下翻转
          imgObj.scale({
            x: Number(imgObj.scale().x),
            y: -Number(imgObj.scale().y)
          })
        } else if (type === "l-r") {
          // 左右翻转
          imgObj.scale({
            x: -Number(imgObj.scale().x),
            y: Number(imgObj.scale().y)
          })
        }
      },
      // 重置翻转
      handleResetTurn() {
        // 隐藏翻转操作
        this.showTurn = false;
        // 还原翻转
        imgObj.scale({
          x: this.curTurn.x,
          y: this.curTurn.y,
        })
      },
      // 显示HSB
      handleHSB() {
        this.showHSB = true;
        this.curHSB = 0;
      },
      // 调整对比度
      getContrast() {
        imgObj.cache();
        imgObj.filters([Konva.Filters.Contrast]);
        imgObj.contrast(Number(this.curContast));
        layer.batchDraw();
      },
      // 调整亮度
      getBrighten() {
        imgObj.cache();
        imgObj.filters([Konva.Filters.Brighten]);
        imgObj.brightness(Number(this.curBrighten));
        layer.batchDraw();
      },
      // 调整饱和度
      getSaturation() {
        imgObj.cache();
        imgObj.filters([Konva.Filters.HSL]);
        imgObj.saturation(Number(this.curSaturation));
        layer.batchDraw();
      },
      // 重置HSB
      handleResetHSB() {
        this.showHSB = false;
        this.curContast = 0;
        this.curBrighten = 0;
        this.curSaturation = 0;
        imgObj.contrast(Number(this.curContast));
        imgObj.brightness(Number(this.curBrighten));
        imgObj.saturation(Number(this.curSaturation));
        layer.batchDraw();
      },
      // 滤镜
      handleFilter() {},
    }
  }

</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
  @import '~common/styles/variable.styl';
  @import '~common/styles/mixin.styl';

  .template-wrap {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: $color-bg;
    z-index: 1000;

    .header {
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 44px;
      line-height: 44px;
      background-color: $color-bg-white;
      box-shadow: 0px 0px 7px 0px rgba(237, 237, 237, 0.6);
      z-index: 100;

      span {
        position: absolute;
        top: 50%;
        padding: 0 9px;
        height: 26px;
        line-height: 26px;
        font-size: 14px;
        color: $color-font-base;
        transform: translateY(-50%);
      }

      .back-btn {
        left: 10px;
      }

      .submit-btn {
        right: 10px;
      }
    }

    .handle-wrap {
      position: fixed;
      top: 44px;
      width: 100%;

      .cover-wrap {
        width: 100%;
        height: 100%;
        position: absolute;
        top: 50%;
        left: 0;
        transform: translateY(-50%);
        overflow: hidden;

        .canvas {
          height: 100%;
          position: absolute;
          top: 50%;
          left: 50%;
          transform: translate(-50%, -50%);
        }

        img {
          height: 100%;
          position: absolute;
          top: 50%;
          left: 50%;
          transform: translate(-50%, -50%);
        }
      }
    }

    .handle-bottom,
    .turn-wrap,
    .hsb-wrap {
      position: fixed;
      bottom: 0;
      left: 0;
      width: 100%;
      background-color: $color-bg-white;
      z-index: 1;
    }

    .turn-wrap,
    .hsb-wrap {
      z-index: 2;
    }

    .slider-wrap {
      padding: 19px 45px 9px;

      >>>.van-slider {
        height: 3px;
        border-radius: 3px;

        &:before {
          top: 0;
          bottom: 0;
        }
      }

      >>>.van-slider__button {
        position: relative;
        width: 9px;
        height: 9px;
        background-color: $color-main;
        border-radius: 50%;

        &:before {
          content: "";
          position: absolute;
          top: -7px;
          left: -7px;
          width: 23px;
          height: 23px;
          background-color: $color-main;
          opacity: 0.32;
          border-radius: 50%;
        }
      }
    }
  }

  .handle-list {
    height: 75px;
    display: flex;
    justify-content: center;
    align-items: center;

    li {
      flex: 1;
      font-size: 13px;
      color: $color-font-base;
      text-align: center;

      p {
        margin-top: 5px;
      }
    }

  }

  .bottom-wrap {
    position: relative;
    height: 30px;
    line-height: 30px;
    font-size: 13px;
    color: $color-font-base;
    text-align: center;

    .van-icon {
      position: absolute;
      top: 50%;
      font-size: 16px;
      transform: translateY(-50%);
    }

    .van-icon-cross {
      left: 20px;
    }

    .van-icon-success {
      right: 20px;
    }
  }

</style>
