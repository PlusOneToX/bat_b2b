<template>
  <div
    class="phone"
    id="phone"
    :class="{ 'ry-style': Number(distributorId) === 4378, noclick: isLoading }"
  >
    <div class="content-wrapper" :class="isLoading ? 'noclick' : ''">
      <!--手机定制面板-->
      <div
        class="phone-wrapper"
        ref="phoneWrapper"
        :style="{ height: wheight + 'px' }"
      >
        <canvas-drag
          ref="mcanvas"
          @submit="submit"
          @clear="clearText"
          @select="selectText"
          @dpi="getdpi"
          :materialsColorValue="materialsColorValue"
          :delIcon="delIcon"
          :scaleIcon="scaleIcon"
          :spriteArr="spriteArr"
          :manufactor="manufactor"
          :materialsType="materialsType"
          :nw="nw"
          :nh="nh"
          :mw="mw"
          :mh="mh"
          :bgimg="phoneImg"
          :upimg="frameImg"
          :pwidth="phonenWidth"
          :pheight="phonenHeight"
          :wheight="wheight"
          :pwidth2="phoneWidth"
          :pheight2="phoneHeight"
          :distributor="distributor"
        ></canvas-drag>
      </div>
      <!---手机型号材质选择---->
      <div class="brand-type" ref="brandType">
        <div class="item dpi-type" :class="getDpiValue(dpiValue)">
          <span class="title">打印效果：</span>
          <van-rate v-model="dpiRate" :color="dpiColor" readonly />
          <span class="name" v-if="dpiRate > 0"
            >(<i class="dpi-desc" :style="'color: ' + dpiColor">{{ dpiDesc }}</i
            >)</span
          >
        </div>
        <div class="item phone-brand">
          <span class="title">更换机型：</span>
          <span class="name" @click="pickerDialog">{{ brand }}</span>
        </div>
        <div class="item phone-type">
          <span
            class="title"
            v-if="
              materialData.length > 1 ||
              (materialData.length === 1 &&
                materialData[0].children &&
                materialData[0].children.length > 1)
            "
            >更换材质：</span
          >
          <span
            class="name"
            :class="
              materialData.length > 1 ||
              (materialData.length === 1 &&
                materialData[0].children &&
                materialData[0].children.length > 1)
                ? 'border-bottom'
                : ''
            "
            @click="MaterialDialog"
            >{{ materialsName }} <span v-show="colorName">-</span>
            {{ colorName }}</span
          >
          <!--            <span class="name">{{materialsName}}</span>-->
        </div>
      </div>
    </div>
    <!--tab栏-->
    <div
      class="nav-wrapper"
      :class="isLoading ? 'noclick' : ''"
      ref="navWrapper"
    >
      <div class="nav-list">
        <div class="nav-item" @click="openPic">
          <span class="icon icon-picture"></span>
          <div class="text">图库</div>
        </div>
        <div
          class="nav-item"
          @click.prevent="chooseImg"
          :class="{ disable: Number(uploadValid) !== 1 }"
        >
          <input
            type="file"
            style="display: none; height: 0; width: 0"
            accept="image/*"
            id="picfile"
            ref="file"
            @change="picUpload($event)"
          />
          <span class="icon icon-camera"></span>
          <div class="text">图片</div>
        </div>
        <div
          class="nav-item"
          :class="{
            disable: Number(materialsId) === 86 || Number(picType) === 2,
          }"
          @click="openText"
        >
          <span class="icon icon-edit2"></span>
          <div class="text">文字</div>
        </div>
       <div
          class="nav-item"
          :class="{ disable: Number(uploadValid) !== 1 }"
          @click="handleCanva"
          v-if="platform === '7' && smVersion > 23"
        >
          <span class="icon icon-custom"></span>
          <div class="text">自定义</div>
        </div>
      </div>
      <div
        class="btn-submit"
        :class="{
          diable: isModelStockOut || isMaterialStockOut || materialData.length <= 0,
        }"
        @click="openSubmit"
        v-if="Number(distributorId) === 4378"
      >
        <div class="text">
          {{
            isModelStockOut || isMaterialStockOut
              ? "请重新选择型号"
              : "设计完成"
          }}
        </div>
      </div>

      <div
        class="btn-submit"
        :class="{
          diable: isModelStockOut || isMaterialStockOut || materialData.length <= 0,
        }"
        @click="openSubmit"
        v-else
      >
        <div class="price">
          ¥
          {{ isModelStockOut || isMaterialStockOut ? price + "(缺货)" : price }}
        </div>
        <div class="text">
          {{
            isModelStockOut || isMaterialStockOut
              ? "请重新选择型号"
              : "设计完成"
          }}
        </div>
      </div>
    </div>
    <!-- 添加文字 -->
    <transition name="fadeText">
      <div
        class="text-wrapper modalDialog"
        id="textWrapper"
        ref="textWrapper"
        v-show="textDialog"
      >
        <div class="title">
          <span class="line"></span>
          <div class="text">添加文字</div>
          <i class="icon icon-close" @click="close"></i>
        </div>
        <van-field
          class="input"
          v-model="inputtext"
          placeholder="在此输入文字内容,请勿选择贴纸和GIF图"
          @focus="focusInput"
          clearable
		  type="text"
		  @blur="blurInput"
        ></van-field>
        <div class="select-box">
          <van-field
            class="select"
            :value="getName(family)"
            placeholder="请选择"
            right-icon="arrow-down"
            @click="toggle"
            readonly
          >
          </van-field>
          <div class="select-list" v-show="showSelct">
            <div
              class="select-item"
              v-for="item in fontFamily"
              :key="item.id"
              @click="handleSelect(item)"
            >
              {{ item.name }}
            </div>
          </div>
        </div>
        <div class="color-box">
          <div class="color" :style="{ backgroundColor: fontColor }"></div>
          <slider-picker v-model="colors" :swatches="swatches"></slider-picker>
        </div>
        <div class="btn-box">
          <div class="btn btn-cancel" @click="removeText">取消</div>
          <div class="btn btn-submit" @click="addText">确定</div>
        </div>
      </div>
    </transition>
    <!--图库-->
    <transition name="fadePic">
      <div class="pic-wrapper modalDialog" v-if="picDialog">
        <!--          <v-header :back="true" :title="'图库'" @back="closePic"></v-header>-->
        <div class="title">
          <div class="text">图库{{ samTitle }}</div>
          <i class="icon icon-close" @click="closePic"></i>
        </div>
        <picture-list
          ref="picList"
          v-show="picData"
          :picData="picData"
          :picname="false"
          @change="change"
        ></picture-list>
      </div>
    </transition>
    <!--选择手机型号弹出框-->
    <van-popup
      v-model="showModelPicker"
      round
      class="picker-wrap"
      :class="{ 'has-stock': isModelStockOut }"
      :close-on-click-overlay="false"
      :lazy-render="false"
    >
      <van-picker
        show-toolbar
        title="选择手机型号"
        ref="picker"
        :confirm-button-text="isModelStockOut ? '缺货' : '确定'"
        :columns="columnsModel"
        @cancel="showModelPicker = false"
        @confirm="handleModelConfirm"
        @change="modelChange"
      >
      </van-picker>
    </van-popup>
    <!--确认弹出框-->
    <van-dialog
      v-model="submitDialogVisible"
      title="请确认商品定制信息"
      show-cancel-button
      cancel-button-text="返回设计"
      confirm-button-text="生成预览"
      class="material-dialog confirm"
      @confirm="make"
    >
      <div class="content" @touchmove.prevent>
        <div class="item-content no-shadow">
          <div class="item-box">
            <div class="name xs">
              材质：<span class="text"
                >{{ materialsName }} <span v-show="colorName">-</span>
                {{ colorName }}</span
              >
            </div>
            <div class="dec">
              <span class="left">型号：</span
              ><span class="text">{{ brand }}</span>
            </div>
          </div>
        </div>
      </div>
    </van-dialog>
    <!--选择手机材质、颜色弹出框-->
    <van-popup
      v-model="showPicker"
      round
      class="picker-wrap"
      :class="{ 'has-stock': isMaterialStockOut }"
      :close-on-click-overlay="false"
      :lazy-render="false"
    >
      <van-picker
        show-toolbar
        title="请选择材质、颜色"
        ref="mpicker"
        :confirm-button-text="isMaterialStockOut ? '缺货' : '确定'"
        :columns="columns"
        @cancel="showPicker = false"
        @confirm="handleMaterialConfirm"
        @change="materialChange"
      >
        <template slot="option" slot-scope="scope">
          {{ scope.text }}
          <span
            v-if="scope.color"
            class="picker-item-color"
            :style="'background-color: ' + scope.color"
          ></span>
        </template>
      </van-picker>
    </van-popup>
    <van-dialog v-model="show" class="msg-dialog" @confirm="show = false">
      <div class="title">
        <span class="icon remind"></span>
        <span class="text">{{ dtitle }}</span>
      </div>
      <div class="info">
        <div class="text">{{ text }}</div>
        <div class="text">{{ text2 }}</div>
      </div>
    </van-dialog>
    <van-dialog v-model="showMsg" class="msg-dialog" @confirm="showMsg = false">
      <div class="title">
        <span class="icon remind"></span>
        <span class="text">{{ dtitle }}</span>
      </div>
      <div class="info">
        <div class="error">{{ error }}</div>
      </div>
    </van-dialog>
    <div
      ref="mask"
      id="mask"
      class="mask"
      v-show="mask"
      @click.stop="closeMask"
    ></div>
	
	<!-- 柔性关闭弹窗 -->
	<div class="flexible-dialog" v-show="showFlexible">
	  <div class="flexible-dialog-content">
	      <div class="flexible-cotent-top">
	      	<img class="flexible-cotent-top-img" src="../../assets/images/flexible-logo.png" mode="widthFix"/>
	      </div>
		  
		  <div class="flexible-cotent-middle-title">
		  	 通知提醒
		  </div>
		  <div class="flexible-cotent-middle-info">
		  	 尊敬的客户，本商城将于6月30日停止运营，仅保留订单查询服务。感谢您的信任和支持。
		  </div>
		  <div class="flexible-cotent-bottom" @click="clickFlexible">
		  	 知道了
		  </div>
	  </div>
	</div>
	
    <Loading v-show="isLoading" :message="message"></Loading>
	
	<Loading v-show="isLoading2" :message="message2"></Loading>
  </div>
</template>
<script type="text/ecmascript-6">
import VHeader from "components/v-header/v-header";
import pictureList from "base/picture-list/picture-list";
import canvasDrag from "components/canvasDrag/canvas-drag";
import Loading from "base/loading/loading";
import { Slider } from "vue-color";
import { mapState } from "vuex";
import MobileDetect from "common/js/lib/mobile-detect.min.js";
import { getRenderer } from "common/js/lib/renderer.min.js";
import { getModel } from "common/js/iphone-device.js";
import api from "common/js/allApi.js";
import EXIF from "exif-js";
import { getClientOsVersion } from "common/js/saApi";
import { getLocalStorageItem } from "common/js/common";

// 使用Vue.mixin的方法拦截了路由离开事件，并在该拦截方法中实现了销毁页面缓存的功能
Vue.mixin({
  beforeRouteLeave: function (to, from, next) {
    let enterFlag = this.$route.query.enterFlag;
    // 监听canva iframe元素
    let canvaIframe = document.querySelector('[dir="ltr"]');
    if (canvaIframe && canvaIframe.style.display === "block") {
      // 如果当前显示，直接隐藏不跳转
      canvaIframe.style.display = "none";
      next(false);
    } else if (enterFlag && enterFlag === "login" && to.name === "login") {
      if (Number(this.platform) === 7) {
        window.location.href = "action://finish";
      }
    } else if (this.mask) {
      // 图库/文字弹窗显示，直接关闭不跳转
      this.closeMask();
      next(false);
    } else if (this.hasDialog || this.submitDialogVisible) {
      // 图片温馨提示/确认定制信息弹窗，直接关闭不跳转
      this.hasDialog = false;
      this.submitDialogVisible = false;
      next(false);
    } else {
      // 否则直接跳转
      if (to.path === "/previewer" || to.path === "/preview") {
        from.meta.keepAlive = true;
      } else {
        if (this.$vnode && this.$vnode.data.keepAlive) {
          if (
            this.$vnode.parent &&
            this.$vnode.parent.componentInstance &&
            this.$vnode.parent.componentInstance.cache
          ) {
            if (this.$vnode.componentOptions) {
              var key =
                this.$vnode.key == null
                  ? this.$vnode.componentOptions.Ctor.cid +
                    (this.$vnode.componentOptions.tag
                      ? `::${this.$vnode.componentOptions.tag}`
                      : "")
                  : this.$vnode.key;
              var cache = this.$vnode.parent.componentInstance.cache;
              var keys = this.$vnode.parent.componentInstance.keys;
              if (cache[key]) {
                if (keys.length) {
                  var index = keys.indexOf(key);
                  if (index > -1) {
                    keys.splice(index, 1);
                  }
                }
                delete cache[key];
              }
            }
          }
          from.meta.keepAlive = true;
        }
      }
      next();
    }
  },
});

export default {
  name: "phone",
  data() {
    return {
      title: "定制",
      dtitle: "温馨提示",
      text: "1.请不要上传违反国家法律法规、精度不足背景虚化的图片， 由此产生的后果我方不予承担",
      text2: "2.印刷制作时都会有一定色差，如介意请勿下单。",
      lenw: "",
      lenh: "",
      len2w: "",
      len2h: "",
      mobile: "", // 自动获取的机型
      show: false,
      showMsg: false,
      isLoading: false,
	  isLoading2: false,
      isPic: false,
      isFlag: 0,
      userNo: "",
      dialogVisible: false,
      platform: "", // 平台
      distributorId: "", // 分销商ID
      distributor: "", // 分销商名字
      inputtext: "", // 添加文字
      fontFamily: [], // 字体列表
      colors: {
        hex: "#194d33",
        hsl: { h: 150, s: 0.5, l: 0.2, a: 1 },
        hsv: { h: 150, s: 0.66, v: 0.3, a: 1 },
        rgba: { r: 25, g: 77, b: 51, a: 1 },
        a: 1,
      },
      swatches: ["100", ".80", ".65", ".50", ".35", ".20", "0"], // 颜色板
      family: "", // 当前字体
      fontColor: "",
      categoryDialog: false, // 切换商品
      textDialog: false, // 文字弹框
      picDialog: false, // 图库弹框
      uploadDialog: false, // 上传图片弹框
      sxPicsDialog: false, 
      submitDialogVisible: false, // 确认弹框
      picData: [],
      picname: false, // 是否显示图片名称
      loading: false,
      isData: true, // 检难数据是否完善
      count: 30,
      materialColorList: [], // 材质颜色属性列表
      materialColor: 0, // 材质颜色ID
      showColorIcon: false, // 材质颜色图标
      modelinfo: [],
      modelData: [], // 品牌型号列表
      materialData: [], // 材质列表
      anchor: [],
      materialList: [], // 材质列表，原始数据
      brandId: 0, // 品牌ID
      modelId: 0, // 型号ID
      materialsId: "", // 材质ID
      materialNo: "", // 材质编码
      materialsType: 0, // 材质类型
      manufactor: "", // 第三方工厂,上传类型：LWH-图片  YC-pdf
      brand: "", // 手机品牌和型号
      brandname: "", // 手机品牌名
      modelName: "", // 手机型号名
      materialsName: "", // 手机材质名
      materialsColorValue: "", // 手机材质颜色值
      materialsImage: "", // 材质图片
      colorName: "", // 手机材质颜色属性名
      picId: 0, // 图片ID
      picName: "",
      picType: 0, // 图片类型
      price: 0,
      spriteArr: [], // 添加的数组
      phoneImg: "", // 手机模型图
      frameImg: "", // 手机框图
      wallpImg: "", // 当前使用的定制图片
      phonenWidth: 0, // 手机宽度
      phonenHeight: 0, // 手机高度
      phoneWidth: 0, // 手机宽度(加宽后)
      phoneHeight: 0, // 手机高度(加宽后)
      mask: false, // 遮罩
      isMake: false, // IP图是否已有
      isPrice: true, // 是否有价格
      showSelct: false,
      showPicker: false,
      showModelPicker: false,
      columns: [],
      columnsModel: [],
      imgData: {
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
        posX: null,
        posY: null,
        posuX: null,
        posuY: null,
        sizeW: 20,
        sizeH: 20,
        rotate: 0,
      },
      wheight: 0, // 画布高
      index: null, // 当前选中图层
      mw: 0,
      mh: 0,
      nw: 0, // 未加宽的手机尺寸
      nh: 0, // 未加宽的手机尺寸
      pscale: 0,
      dpiValue: 0, // 图片dpi值
      dpiDesc: "", // dpi描述
      dpiRate: 0, // dpi评分
      dpiColor: "", // dpi颜色
      dpiFlag: false,
      ratio: 1, // 缩放比
      skuNo: 0, // skuNo
      message: "", // loading提示
	  message2: "", // loading提示
      error: "", // 错误提示信息
      isError: false,
      delIcon:
        "data:image/png;base64,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",
      scaleIcon:
        "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAYAAADDPmHLAAAAAXNSR0IArs4c6QAAEO9JREFUeAHtXX10FNUVv29mN8kmJNEIUitJTNCgRdp60BYO2iJHRa1fRa1FKiqo1KqAqP1DWkVbaE/Br6JVLKBiLdpWxR4PrcpBPOqRWjm29YCESmIS8KNqlHxtkt2Z13sn7DG72Zl5szufuzN/ZOfjzn33/n438968jzsA4RYiECIQIhAiECIQIhAiECIQIhAiUFwIsEJ0l0+eHB3o7GxUIDmBq3wCAJvAOYzjjFcyzio5g0rguM9YJfnPOe8GxroZh26UwV/cZ7APrzQziTXLEGkuralpYTt2JAoNr4IIgN76+iM4KDOQ6FNV4CcjkeM5QMROshCoJAbOXgnYaxgYLzOQt1a0tX1oZxle6ApkAPDp0yN9ra0zVVDPRjJm4H/3sZ6Ax2A3Bt1WCaTN5Q0NL7Bt25Je2JFPmYEKgN66usmqpM4FDrOR9DH5OG73vVhlfAIMNkqqtKGivX2H3fqd0uf7AOCNjdW9yuA1HNgVWFd/zSkg7NSLbYtdDPijFXLJw6yl5YCduu3W5dsA6GpqGs0G4ouwLr8BW2nVdjvuij7GDiDAq3lp7L6qPXs+daVMi4X4LgCoQaeAejOSvgB9qbDoj1/Fe/EtY40M0iq/NRx9EwB84sSSnp6uW7B+X8qBx/zKZD52MWBxbCcsHzWqaiXbuXMwH1123euLAOipqztNZeoDSH6TXY75Wg+DZolL149qb9/itZ2eBkDvhNqvqnG4G//jL/EaCC/KxyfCU1IMllQ0d3zgRflUpmcBgK905yjAH8P3+BqvnPdHuaxTBnY5vjo+74U9ktuFUjdtV33tSuzE+WtIPqHPawgLwoSwcZsPV58A8cbGuoQy+BTW9VPcdjQQ5THYHpVLLom1tLS7Za9rAdDVUHsKKLAp/K83o5Z1ggwXVLV2vGomacd1V6qArqNqz2cqvBiSL0IZryGsCDMR6XxlHA+Arvpx85nKn8Zu3LJ8jS2W+wkrwoywc9pnRwMAGza3Yn2/FrtzZacdKTT9GmaInYahg8451gYYIp8vd9D24lHN2NKqto4VTjjsSABojy6MXicMLlqdDK6qatu3zm7/bQ+AoQYf1vnhY99WrpAohUvswqr3O56zU7GtAUCvetSCDRt8dlL0pS6cZ9DPJTjDzldE2wJA6+RJJt4OX/W+JMyZPdYZjURPsKuzyJa3AOrC1Hr4ir5f3xnK07XyGsLarm5jWwKg+9P/rQi7d9NpcvQIu9I1zG0oJO8qgEb1aDADG31567LBn6JRgWBjc0A6L99RxLxIo/F8JQ7vhPW+V3GHQ8kxmJTPfIK8qgCazBGS7xX5VC4OJWsc5G5Dzk8AbRoXqC/lXnR4p10IYFVweq7Ty3IKAJrA2d1z4B0n5/BFLvg+lMybB9KxxwErLbULqxF61I8/AmX7dhhYsRz4Rx+NuB6IEwz2VI6qnpTLRNOcAqC7vnYpdvb80ilwShYthtIlNzmlPqte/vnn0DvzDOAYEEHcsJPoZ5VtHZbHXiwHAM3bV7m616mp22zcOKh45VVgEVvXdgpxmtj0LPQvWigk6zchmnIuMWm81XUHlhuBtGjDKfIJVPnEkzwhXyt76lS/8SpsD3GiLagRvmNI0FIA0HKtgyt2LBYjLs7GjhUXDiXTEcDVVBpH6WcNjywFAK3VQ22FslxrBDDKG2+MOBewExUHORI2WzgAaJUu9vbdIKw5YILUCBxY4cicC1eRII6IK9FChQOAlmgHdpWuARrqxx9D4rlN0HtmcN8A0tzDldQaV2kn9Q+Em9q0Ph97nvQ1eXxlYOVvYPD+1R5b4Y/ih7iClSLWCD0BKDMHvvf7OjlD6S0/heilc0R8LngZ4oo4E3FUKAC0tCwi2jyWKV2+AiLnnOuxFf4oXpQz0wCghEz45J/tD7eMrWCSBGX33gfyd6cbCxbDVcqjRNyZbKYBQNm4/JaQycgnFo1CbM3D2KF0opFYwV8jzog7M0dNA2AoFZuZGveuqx9+aFoYi8Ugtv5RkI47zlS2kAVEuDMNAGz5z/ATSMm/bQZl505Tk1h1NcQefwJY/VGmsoUrYM6dYQBoGTg9SsKoS8rgIMQvmwNqa6uuSOqCNGYMlD/xR2Bjv5I6VVS/lECTODRy2jAAhtKvGt3uzTX+2WfQN2c2iFQHUm0txP7wBMAhh3hjrMelmnFoGACUe9dj+3WL5/v3Q/xHl4La2akrk7ogNzVB+YbHAcrLU6eK6NeYQ8MAoMTLfkZKfe89iM+9DHhPj6mZ8je+CbG16wFKSkxlC0nAjEPdAKCFB5R12+9gqO/8B+JXzQPe329qamTaNCi7/wEA7C8olk3LnG6Qe0gXCcq3jz3/ph0JfgCShnH7r78OeNI8WXd05plQtnKVH8x2xQbikLjUK0w3AOhjC3o3+fF88qUXof8WyjBrPmAVvehiKL3tdj+64YhNRlzqB4DCPcnBnw8CyWeehoE7lgmpKJl/FdDk02LYhr6akt1T3QDA5MaBegKk3Es8sh4G7sH1KgIbzTyOzr1cQDLYIiow3X9m3QDAxsO4oLo9eO89MIiBILKV3vkLoDUIhbwZcakbAPSBpSCDMrDsdkg884ypCzifHsruuhvkGTNMZYMqYMSlbgDgl7NGeeKwSUueq6qwWf03L4HkFvPVa7QGIfbgQyB/69vCuoMkaMSlbgBon1bzwEvq3DHa1P/uMbqcfk1RIP6TayG5/Y3081mOWBmNID4C0sSJWa4G+5QRl7oBgO9TnlQBypv/AHXfvqyI08xdZdu2rNd0Tw4MQHz+PFCww8hsY5WVQyOIDQ1moqbX5dNOh9i69VC+ZSuUPYTzE6Z4mB7ZgEvdAMC60ZMAgHgc4guvB/WTT9JA5gcOQHzxQuACff9pN9IBdhVTl7G6d++IS5knpMMOwxHEjcCOMBxEy7wt7bhk4SIoR/IjGATyMcdA9KyzIPbknzybs2jEpe7aQFwAOoCdKp51nLNDD4XIueeBdPTR2tBvcvPmvBduEqnlTz8L0pFHphGW7YCCpe+iWZYDjuwtf3ELMHlkclSOwd17yjTgGcGdrXw7z2EADOLC0dJsOnWfAEh+d7Yb3DpHj/vEhsdg4LafA73b27Fql+NsIm0YGYeTzTZp/HiI0QjiKGttYXnayVnJp/JoppJ80klmRdt+3YhL3QCgb+nabokPFHKcSEITSni3uXvypK9r9ThYyE9g9nRh42rdR8GAS90AwM4Dc4Tcd8WWElWcUhafdyWOIMZN9UWmTIXY7x7EZcsjH+mmN/tEwIhL3QCgr2j7xH5HzKC3jfi1PxYaQaTGXNkqse5lR4zNU6kRl7oBQJ9Qz7Nc39+ubN0K/TctERtBnDULSpfd4XufshloxKVuAGDnwf5sygrtXBKzglBDU2QruXIelNy4RETUVzLY2529YwWt1A0ACfhuX3nhoDHa28YqobWUULr4RohiIARr48169uoGAJOY7k16yoJ8fnD1b2Fw3VohF0pvXwaRWRcKyfpByIhL3QCQIVJUAUBEDdx5ByT+8mdTzrQRRJxWFjn9DFNZPwgYcakbAKU1NS3YTWg+yc4PHtpoA00rS7zwd1ONNIJIE0xlnyeWIg6JSz2HdAOA7diRwIageee5nuagnsfhZppgmnz9dVMPWFmZNtVcwg4jv27EIXGpZ59uANANErDX9G4s6PO0/Aynmiv//pepmwy7iqnLmMYA/LiZcWgYALgw9GU/OuWKTX190IcjiMoe8/kHUk0NLj/DNYgCg0yu2J5WiDGHhgHAQN6apqvYDr74Apef4ULUjg5TzyUaacRhZMABHz9tZhwaBgClHcVOhKLpD8hGHI1C9s3BNYgCQ7gSTiSJnP29bGo8OUfcmaWONQyAIatZcT8FEATe9v7QCCJOSjHbpNGjzURcvG7OnWkAYC76zS5a7Nui1HffxRHEK4AmdQRlE+HONADKGxpewEdJ+vysoCBgs53KW29BfAHmy0zovlXZXGLu6ogz4s5Mg2kAsG3bkvg5KGzdhBshoLyyDfoXLwIr09M9QQ4507gzKTxicl27LKnSBkxFvlBENlAy+G8iNTZiCpmxlszmnZ9BYt06KLn6akv3uSlMnImUJxQA+GmyHThJdBfOLfN1tlARh1MyNG277Fe/Bunww1OnCuYXxyp2VbS17xBxyLQKSClhwB9N7Qf9lz5KEfv92oIkn7ixwpVwAFTIJQ/jRFHz96AAREfJTTcDZRUtyA050rgSdE4YBdbScgBHllYL6vW1mHz88d7Zh4NNTm7EEXElWoZwAJBCXhq7D396RZX7Vc7thRnDcRDJbzhc3uJ+70GOhG+zFABVe/Z8itXAGmHtPhUUGe93wnQVF6Qo/3zTCdVDOpEbjSMLJVgKANIrg7SKPlFmoQzfiVICiaSTRGTxmOPoIi1Xh66uLFfzP0WcEDdWNWGVYX1z+sOR1i3K4Q5sBEYv/gGu2p1quR/AUmm4OlnZ/S4kNm4E3t5m6VYrwvjq586HI8koNz4da8X5opfN49OxlqsAApu+UStx6bqiB94nABAXuXw3mMzPKQDoRvpaNdY7T9F+uHmHAHGQ65fDyeqcA0C7OQbYqmGd3rlf7CWzTknjIHcc8gqAiuaOD2Rgl2NL0jw9Z+42hndmQYAwJ+yJgyyXhU/lFQBUCg4UPc8Zu0u4xFDQFgQIc8I+X2V5BwAZUDn68FtxBGJ7vsaE9wsigFhrmAuKG4nl1A+QTWG8sbEukUy8jU+mmmzXw3N2IcA6o5HoCbGWlnY7NNryBCBDNINkuAA7JPrtMCzUMRIBDVvE2C7yqQTbAoCUVbV2vIpLkX6IjxWFjsPNPgQIU8KWMLZPq80BQIZVvd/xHBq6wE4jQ11YsSKmhK3dWNj6BEgZV9W2bx2OGi5NHYe/eSKAWGqY5qkm2+22NQKzKe+qr70VE/Asz3YtPCeIgEZ+xwpBactijgYAWdNVP24+pilbgz1Fwc2zZhnW/G84WOcvcOo/P2Wh4wFABXUdVXs+BsGTOKu4LFVw+KuPALX2tQafA3V+ZqmuBAAV2tVQewq+G2wK+wkyKcg8xrEVfNWzu7WfWUrq2JFGYEr58F9yiDowwh7D4ahk7GMPH2HkFvlUumsBQIVRB0bl6LHfwTcEnFYWDiARJrRpWCAmhI2dnTxD2o3/ulYFZJrRW1d3jgL8sbBKYJ3aqJ4NAzuZGIscu/oEGG4QjWTJMZhUzJNKyHfCwI5RveHYWtn37Akw3MieurrTVKY+gJVC0/DzBbuPc/hoGlc+M3nswsazJ8BwBwiIylHVk2hmK/5XBHrK+XC/MvfJN/KRfPUD+WSfL54Aw4Hqra8/Apei00eAaTyhYvi1AO/30oIamrdvlrPHbR99FwApALqamkazgfgi7EG8AYOhOnU+UL+4UBMBXk3Ltayu2HHLT98GQAoA3thY3asMXsOBXRGU/AT4mN9FS7Rpla6VhZopn9389X0ADAcDXx0nq5I6FxuLs/Er8WOGX/N6X8ujhGlZKDMHtuqFkjN4bTOVH6gASAHGp0+P9LW2zlRBPRv7EWZgMOh+HTt1jxO/SDrmUGRbKRuXlkyL8ikFbAtkAGRiTA1HDsoMJONUFfjJOPA0HtsOQulvMnXpHSNQSRyg2TuUe5e/TBk4/dag07Pd6HxBBECmg3zy5OhAZ2ejAskJisKPxRb4BAyKI/HjSVX0IWUkshIblpVYV1fSvdp39fDTaijTjTI9KNOFMvvxQrMss92Ub19Ln2+QdTvThvA4RCBEIEQgRCBEIEQgRCBEIEQgRCBEwJ8I/B+JlWlX4LdY9gAAAABJRU5ErkJggg==",
      exportUrl: "", // Canva 图片
      designId: "", // Canva 图片id
      smVersion: "",
      samTitle: "", 
      hasDialog: false, // 是否有弹窗
      isModelStockOut: false, //是否缺货（型号）
      isMaterialStockOut: false, //是否缺货（材质）
      isFirstClick: true,
      uploadValid: 1, // 是否允许用户上传（0 不允许，1 允许）
      copyrightCost: 0, // 图片版权费
      isZFold: false, 
	  
	  distributorId: "", // 分销商ID
	  showFlexible: false //是否显示柔性关闭弹窗
    };
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      if (from.name === "preview" || from.name === "previewer") {
        vm.message = "";
        vm.isLoading = false;
        vm.isBack = true;
      }
      vm._getQueryData();
    });
  },
  created() {
	console.log("页面创建");
    this.userNo = getLocalStorageItem("userNo") || "b2bsamsung";
    this.platform = localStorage.getItem("platform");
    this.distributorId = localStorage.getItem("distributorId");
    this.message = "载入中";
    this.isLoading = true;
    if (this.platform === "7") {
      this.samTitle = "(送同款手机主题)";
      let userAgent = navigator.userAgent;
      this.smVersion = getClientOsVersion(userAgent); // 23：Android 6.0

      if (this.smVersion && this.smVersion > 23) {
        // Canva JS
        if (this.$store.state.user.firstLoadCanva) {
          let canvaJS = document.createElement("script");
          canvaJS.type = "text/javascript";
          canvaJS.src = "https://sdk.canva.cn/designbutton/v2/api.js";

          var _self = this;
          canvaJS.onload = function () {
            // API 初始化
            _self.initCanva(_self.userNo);
          };
          document.body.appendChild(canvaJS);
          this.$store.commit("handleNotFirstLoadCanva");
        }
      }
    }

    // 记录数据埋点
    let hasRecord = sessionStorage.getItem("hasRecord");
    if (!hasRecord) {
      this.handleRecordData();
    }
  },
  mounted() {
    // 画布宽高
    this.getDrawSize();

    if (this.$route.query.key === "-1") {
      this.wallpImg = "";
      this.reset();
    } else if (
      this.$route.query.pid !== "" &&
      this.$route.query.pid !== undefined
    ) {
      this.picId = this.$route.query.pid;
      // 以防未登录情况，需记录登陆后调整页面和数据
      if(this.picId){
        localStorage.setItem("comingFlag", "phone");
        localStorage.setItem("picId", this.$route.query.pid);
        localStorage.setItem("pUrl", this.$route.query.url);
        localStorage.setItem("pType", this.$route.query.type);
      }
      this.reset();
    } else {
      // this.message = ''
      // this.isLoading = false
    }
  },
  activated() {
    // 画布宽高
    this.getDrawSize();
  },
  computed: {
    ...mapState(["loading"]),
  },
  methods: {
    // 记录埋点数据
    handleRecordData() {
      let userId = getLocalStorageItem("userId") || "";
      this.$api
        .post(this, api.recordPoint, {
          source: this.platform,
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
    // 画布宽高
    getDrawSize() {
      // 画布高
      var brandH = parseInt(
        window.getComputedStyle(this.$refs.brandType).height
      );
      var navH = parseInt(
        window.getComputedStyle(this.$refs.navWrapper).height
      );
      setTimeout(() => {
        let sHeight = `${document.documentElement.clientHeight}`;
        let H = Number(sHeight) - brandH - navH - 20;
        this.wheight = H;
      }, 20);
    },
    initCanva(userNo) {
      let _self = this;
      var apiKey = "";
      var host = window.location.host;
      if (host === "diy.bat.com") {
        apiKey = "_e38j4MhC7jdyXQoJ7Is7GQ8"; // 正式
      } else {
        apiKey = "7fyFZMishUEZeNTQ68POjxHv"; // 测试
      }
      this.$api.get(this, api.getCanvaToken, { userNo: userNo }).then((res) => {
        if (res.success) {
          // Canva
          if (window.Canva && window.Canva.DesignButton) {
            window.Canva.DesignButton.initialize({
              apiKey: apiKey,
              autoAuthToken: res.data,
            }).then(function (api) {
              // 使用 canvaApi 对象或将其保存下来以供日后使用
              _self.$store.state.user.canvaApi = api;
            });
          }
        }
      });
    },
    _getQueryData() {
      // 判断是否有图片id
      let pid = this.$route.query.pid;
      let url = this.$route.query.url;
      let type = this.$route.query.type;
      if(this.$route.query.platform){
        this.platform = this.$route.query.platform
        localStorage.setItem("platform", this.platform);
      }
      if(this.$route.query.distributorId){
        this.distributorId = this.$route.query.distributorId
        localStorage.setItem("distributorId", this.distributorId);
      }
      if (this.platform && this.platform === "7") {
        this.distributor = "samsung";
        if (pid && url && type) {
          this.isPic = true;
        }
      } else {
        // bat.com
        if (this.distributorId === "1783") {
          this.distributor = "zhuanz";
        } else if (this.distributorId === "2596") {
          this.distributor = "linzhi";
        } else {
          this.distributor = "bat";
        }
      }
      localStorage.setItem("distributor", this.distributor);
      this.initData();
    },
    // 初始化
    initData() {
      // 请求所有品牌型号
      // if (this.$route.query.pid || this.isBack) {
      if (this.isBack) {
        this.getBrand();
      } else {
        this.getMobileDevice("device");
      }
    },
    // 自动获取机型
    getMobileDevice(device) {
      // 判断数组中是否包含某字符串（安卓机型获取用到）
      /* eslint-disable */
      Array.prototype.contains = function (needle) {
        for (i in this) {
          if (this[i].toString().indexOf(needle) >= 0) {
            return i;
          }
        }
        return -1;
      };
      // 获取 UA
      let userAgent = navigator.userAgent;
      // 初始化 mobile-detect
      var md = new MobileDetect(userAgent);
      var os = md.os(); // 获取系统
      // iOS 系统的处理
      if (os === "iOS") {
        os = md.os() + md.version("iPhone");
        // 通过 renderer.js 获取苹果机具体的 GPU
        let rendererVal;
        getRenderer((renderer) => {
          rendererVal = renderer;
          if (renderer === "Unknown") {
            debugger;
            var canvas = document.createElement("canvas");
            if (canvas != null) {
              var context = canvas.getContext("experimental-webgl");
              if (context) {
                var info = context.getExtension("WEBGL_debug_renderer_info");
                if (info) {
                  rendererVal = context.getParameter(
                    info.UNMASKED_RENDERER_WEBGL // GPU 型号
                  );
                }
              }
            }
          }
        });

        // 通过 iphone-device.js 获取苹果机具体的机型
        let phone = getModel(rendererVal);
        if (phone.indexOf(",") > 0) {
          this.$toast("机型适配可能存在偏差，请确认或手动选择！");
          this.mobile = phone.substring(0, phone.indexOf(","));
        } else {
          this.mobile = phone;
        }
        this.getBrand(device);
      } else if (os === "AndroidOS") {
        // Android 系统的处理
        os = md.os() + md.version("Android");
        // 判断 UA 里边有没有 Build 或者 AppleWebkit信息，通过这个拿到安卓的具体机型
        let str;
        if (userAgent.indexOf("Build/") > -1) {
          str = "Build/";
        } else {
          str = "AppleWebKit";
        }
        var sss = userAgent.split(";");
        var i = sss.contains(str);
        if (i > -1) {
          // Android 入网型号
          let networkModel;
          if (str === "Build/") {
            networkModel = sss[i].substring(0, sss[i].indexOf(str));
          } else {
            if (sss[i - 1].indexOf("Android") > 0) {
              let strArr = userAgent.split(" ");
              let j = strArr.contains(str);
              let len = strArr[j - 1];
              networkModel = len.substring(0, len.length - 1);
            } else {
              networkModel = sss[i - 1];
            }
          }
          this.getAndroidMobile(networkModel.trim()).then((res) => {
            if (res.success && res.data) {
              this.mobile = res.data.name;
            } else {
              this.mobile = "";
              this.$toast.fail("未匹配到当前手机型号，已展示默认机型");
            }
            this.getBrand(device);
          });
        }
      }
    },

    // 获取 Android 手机型号
    getAndroidMobile(networkModel) {
      return new Promise((resolve, reject) => {
        this.$api
          .get(this, api.getModelByNetwork, { networkModel: networkModel })
          .then((res) => {
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // 字体选择
    handleSelect(item) {
      this.showSelct = false;
      this.family = item.englishName;
      this.initFont(item, this.family);
    },
    // 引入字体文件，并初始化字体
    initFont(item, fontFamily) {
      let style = document.createElement("style");
      style.type = "text/css";
      let str =
        '@font-face {font-family: "' +
        item.englishName +
        '"; src: url("' +
        item.fontFile +
        '"); font-weight: normal; font-style: normal;}';
      style.innerText = str;
      document.getElementsByTagName("head")[0].appendChild(style);

      let dom = document.createElement("div");
      dom.style.fontFamily = fontFamily;
      dom.style.fontSize = "14px";
      dom.style.opacity = "0";
      dom.innerHTML = "test";
      document.getElementById("phone").appendChild(dom);
    },
    // 添加文字高度自适应
    focusInput() {
	  console.log("获取焦点");
      if (window.screen.width > 500) {
        this.$refs.textWrapper.style.height = 350 + "px";
      }
    },
	
	//失去焦点
	blurInput() {
	  console.log("失去焦点");
	  //this.$toast("温馨提示：请不要选择贴纸、GIF等内容，文本框将会自动过滤掉这些内容");
	},
	
    // 根据品牌ID查询品牌名称
    getNameById() {
      let bName, mName;
      let bid = parseInt(this.brandId);
      let sid = parseInt(this.modelId);
      this.modelData.forEach((item) => {
        if (item.id === bid) {
          bName = item.text;
          if (item.children) {
            item.children.forEach((child) => {
              if (child.id === sid) {
                mName = child.text;
              }
            });
          }
        }
      });
      let str = bName + " " + mName;
      return str;
    },
    toggle() {
      this.$refs.textWrapper.style.height = "auto";
      this.showSelct = !this.showSelct;
    },
    // 获取所有品牌型号
    getBrand(type) {
      let picId = "";
      if (type && type === "device") {
        picId = "";
      } else {
        picId = this.picId === 0 ? "" : this.picId;
      }
      this.$api
        .get(this, api.getModelList, {
          categoryId: 1, // 产品类型id：1 手机壳
          distributorId: this.distributorId,
          materialId: this.materialsId,
          pictureId: picId,
          platform: this.platform, // 平台
        })
        .then((res) => {
          if (res.success) {
            if (res.data && res.data.length > 0) {
              let dataArr = res.data;
              // 重组数据
              let modelArr = [];
              this.modelData = [];
              dataArr.forEach((item, k) => {
                item.childrenList.forEach((model, r) => {
                  modelArr.push({
                    id: model.modelId,
                    text: model.name,
                    englishValue: model.englishName,
                    underStockFlag: model.underStockFlag || 0,
                  });
                });
                this.modelData.push({
                  id: item.modelId,
                  text: item.name,
                  englishValue: item.englishName,
                  children: modelArr,
                });
                modelArr = [];
              });

              this.$refs.picker.setColumnValues(0, this.modelData[0]);
              this.$refs.picker.setColumnValues(1, this.modelData[0].children);
              this.columnsModel = [
                {
                  values: this.modelData,
                },
                {
                  values: this.modelData[0].children,
                },
              ];

              // 点击更换机型
              if (type && type === "show") {
                // 判断型号列表第一项是否缺货
                if (dataArr[0].childrenList[0].underStockFlag) {
                  // 是
                  this.isModelStockOut = true;
                } else {
                  // 否
                  this.isModelStockOut = false;
                }
                this.showModelPicker = true;
              } else {
                // 根据型号查询品牌并显示
                if (this.mobile !== "" && this.mobile !== undefined) {
                  this.getModelId(this.mobile);
                } else {
                  // 判断型号列表第一项是否缺货
                  if (dataArr[0].childrenList[0].underStockFlag) {
                    // 是
                    this.isModelStockOut = true;
                  } else {
                    // 否
                    this.isModelStockOut = false;
                  }

                  this.brandId = dataArr[0].modelId;
                  this.brandname = dataArr[0].name;
                  if (dataArr[0].childrenList.length > 0) {
                    this.modelId = dataArr[0].childrenList[0].modelId;
                    this.modelName = dataArr[0].childrenList[0].name;
                    this.mobile = dataArr[0].childrenList[0].name;
                  }
                }
                this.getMaterialList();
                if (this.brand === "") {
                  this.brand = this.getNameById();
                }
              }
            }
          } else {
            this.message = "";
            this.isLoading = false;
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    // 根据手机型号查询型号及品牌
    getModelId(mname) {
      if (this.modelData.length > 0) {
        let bIndex = 0;
        let flag = false;
        try {
          this.modelData.forEach((brand) => {
            if (brand.children.length > 0) {
              brand.children.forEach((model, index) => {
                if (
                  model.text.replace(/\s*/g, "").toLowerCase() ===
                  mname.replace(/\s*/g, "").toLowerCase()
                ) {
                  this.modelId = model.id;
                  this.modelName = model.text;
                  this.mobile = model.text;
                  this.brandId = brand.id;
                  this.brandname = brand.text;
                  bIndex = index;
                  flag = true;

                  this.brand = this.brandname + " " + this.modelName;

                  // 判断是否缺货
                  if (model.underStockFlag) {
                    // 是
                    this.isModelStockOut = true;
                  } else {
                    // 否
                    this.isModelStockOut = false;
                  }

                  throw new Error("end");
                }
              });
            }
          });
          if (!flag) {
            // 判断型号列表第一项是否缺货
            if (this.modelData[0].children[0].underStockFlag) {
              // 是
              this.isModelStockOut = true;
            } else {
              // 否
              this.isModelStockOut = false;
            }

            this.brandId = this.modelData[0].id;
            this.brandname = this.modelData[0].text;
            this.modelId = this.modelData[0].children[0].id;
            this.modelName = this.modelData[0].children[0].text;
            this.mobile = this.modelData[0].children[0].text;

            this.brand = this.brandname + " " + this.modelName;

            this.$toast.fail("未匹配到当前手机型号，已展示默认机型");
          }
        } catch (e) {}
      }
    },
    // 根据型号获取材质列表
    getMaterialList() {
      let flag = false;
      let isZFold = this.modelName.toUpperCase().indexOf("FOLD3") >= 0 ? 1 : 0;
      this.$api
        .get(this, api.getMaterialList, {
          pictureId: this.picId === 0 || isZFold ? "" : this.picId,
          categoryId: 1, // 产品类型id：1 手机壳
          distributorId: this.distributorId,
          modelId: this.modelId,
          platform: this.platform, // 平台
        })
        .then((res) => {
          if (res.success) {
            if (res.data && res.data.length > 0) {
              this.isData = true;
              let dataArr = res.data;
              this.materialList = res.data;
              let materialArr = [];

              let materialNo = this.$route.query.materialNo;
              if (materialNo) {
                this.materialData = [];
                // 材质编号
                dataArr.forEach((item) => {
                  if (item.childrenList && item.childrenList.length > 0) {
                    item.childrenList.forEach((material, r) => {
                      if (material.materialNo === materialNo) {
                        this.materialData.push(material);
                        this.materialsId = material.materialId;
                        this.materialsName = material.name;
                        this.materialNo = material.materialNo;
                        this.skuNo = material.itemCode;
                        this.materialsImage = material.image;
                        this.materialsColorValue = material.colour;
                        this.manufactor = material.manufactor;

                        // 获取是否允许用户上传
                        this.uploadValid = material.allowUploadFlag;
                        // 是否强制铺满血位：1 是，0 否
                        let isAllPlace = material.mandatoryCoveredBleedFlag;
                        sessionStorage.setItem("isAllPlace", isAllPlace);

                        // 是否缺货
                        this.isMaterialStockOut = material.underStockFlag
                          ? true
                          : false;
                        return;
                      }
                      return;
                    });
                  } else {
                    if (item.materialNo === materialNo) {
                      this.materialData.push(item);
                      this.materialsId = item.materialId;
                      this.materialsName = item.name;
                      this.materialNo = "";
                      this.skuNo = "";
                      this.materialsImage = item.image;
                      this.materialsColorValue = item.colour;
                      this.manufactor = item.manufactor;
                      return;
                    }
                  }
                });
                this.getMaterialById(this.materialsId);
                // 根据手机型号和材质获取手机定制信息
                this.getInfo();

                if (this.materialData.length <= 0) {
                  this.message = "";
                  this.isLoading = false;
                  this.materialsName = "暂无关联材质";
                  this.colorName = "";

                  this.$dialog
                    .confirm({
                      title: "温馨提示",
                      message:
                        "暂无关联的材质数据，请选择其他机型或联系客服处理~",
                      className: "confirm-v-dialog tl",
                      confirmButtonText: "关闭",
                      showCancelButton: false,
                    })
                    .then(() => {})
                    .catch((error) => {
                      console.log(error);
                    });
                }
              } else {
                this.materialData = [];
                // picker：数据处理
                dataArr.forEach((item, k) => {
                  if (item.childrenList && item.childrenList.length > 0) {
                    item.childrenList.forEach((material, r) => {
                      materialArr.push({
                        id: material.materialId,
                        materialNo: material.materialNo,
                        text: material.name,
                        englishValue: material.englishName,
                        color: material.colour || "",
                        manufactor: material.manufactor,
                        underStockFlag: material.underStockFlag || 0,
                        allowUploadFlag: material.allowUploadFlag,
                        mandatoryCoveredBleedFlag:
                          material.mandatoryCoveredBleedFlag,
                        skuNo: material.itemCode,
                      });
                    });

                    this.materialData.push({
                      id: item.materialId,
                      text: item.name,
                      englishValue: item.englishName,
                      color: item.colour || "",
                      manufactor: item.manufactor,
                      children: materialArr,
                    });
                  } else {
                    this.materialData.push({
                      id: item.materialId,
                      text: item.name,
                      englishValue: item.englishName,
                      color: item.colour || "",
                      manufactor: item.manufactor,
                      children: [
                        {
                          id: item.materialId,
                          materialNo: item.materialNo,
                          text: item.name,
                          englishValue: item.englishName,
                          color: item.colour || "",
                          manufactor: item.manufactor,
                          underStockFlag: item.underStockFlag || 0,
                          allowUploadFlag: item.allowUploadFlag,
                          mandatoryCoveredBleedFlag:
                            item.mandatoryCoveredBleedFlag,
                          skuNo: material.itemCode,
                        },
                      ],
                    });
                  }
                  materialArr = [];
                });

                this.$refs.mpicker.setColumnValues(0, this.materialData[0]);
                this.$refs.mpicker.setColumnValues(
                  1,
                  this.materialData[0].children
                );
                this.columns = [
                  {
                    values: this.materialData,
                  },
                  {
                    values: this.materialData[0].children,
                  },
                ];
                // 添加manufactor参数，及材质二级分类
                if (this.materialsId) {
                  this.getMaterialById(this.materialsId);
                } else {
                  this.materialsName = dataArr[0].name;
                  // 默认第一个
                  if (
                    dataArr[0].childrenList &&
                    dataArr[0].childrenList.length > 0
                  ) {
                    this.colorName = dataArr[0].childrenList[0].name;
                    this.materialsId = dataArr[0].childrenList[0].materialId;
                    this.materialNo = dataArr[0].childrenList[0].materialNo;
                    this.skuNo = dataArr[0].childrenList[0].itemCode;
                    this.materialsColorValue =
                      dataArr[0].childrenList[0].colour;
                    this.manufactor = dataArr[0].childrenList[0].manufactor;

                    // 获取是否允许用户上传
                    this.uploadValid =
                      dataArr[0].childrenList[0].allowUploadFlag;
                    // 是否强制铺满血位：1 是，0 否
                    let isAllPlace =
                      dataArr[0].childrenList[0].mandatoryCoveredBleedFlag;
                    sessionStorage.setItem("isAllPlace", isAllPlace);

                    // 是否缺货
                    this.isMaterialStockOut = dataArr[0].childrenList[0]
                      .underStockFlag
                      ? true
                      : false;
                  } else {
                    this.materialsId = dataArr[0].materialId;
                    this.materialNo = "";
                    this.skuNo = "";
                    this.colorName = "";
                    this.materialsColorValue = dataArr[0].colour;
                    this.manufactor = dataArr[0].manufactor;
                  }
                }
                // 根据手机型号和材质获取手机定制信息
                this.getInfo();
              }
            } else {
              this.materialData = [];
              this.picData = [];
              this.materialsId = "";
              this.materialNo = "";
              this.skuNo = "";
              this.materialsName = "";
              this.colorName = "";
              this.materialsColorValue = "";
              this.materialsImage = "";
              this.manufactor = "";
              this.message = "";
              this.isLoading = false;
              this.isData = false;
              this.reset();
              this.$toast.fail("暂无关联的材质数据");
            }
          } else {
            this.isData = false;
            this.$toast.fail(res.errMessage);
            this.message = "";
            this.isLoading = false;
            this.getBrand();
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    // 根据材质ID获取材质信息
    getMaterialById(id) {
      let flag = false;
      this.materialList.forEach((item, index) => {
        if (item.childrenList && item.childrenList.length > 0) {
          item.childrenList.forEach((second) => {
            if (second.materialId === id) {
              this.materialsName = item.name;
              this.materialNo = second.materialNo;
              this.skuNo = second.itemCode;
              this.colorName = second.name;
              this.materialsColorValue = second.colour;
              this.manufactor = second.manufactor;

              // 获取是否允许用户上传
              this.uploadValid = second.allowUploadFlag;
              // 是否强制铺满血位：1 是，0 否
              let isAllPlace = second.mandatoryCoveredBleedFlag;
              sessionStorage.setItem("isAllPlace", isAllPlace);

              // 是否缺货
              this.isMaterialStockOut = second.underStockFlag ? true : false;

              flag = true;
            }
          });
        } else {
          if (item.materialId === id) {
            this.materialsName = item.name;
            this.materialNo = "";
            this.skuNo = "";
            this.colorName = "";
            this.materialsColorValue = item.colour;
            this.manufactor = item.manufactor;
            flag = true;
          }
        }
      });

      if (!flag) {
        this.materialsId =
          this.materialList[0].childrenList.length > 0
            ? this.materialList[0].childrenList[0].materialId
            : this.materialList[0].materialId;
        this.materialNo =
          this.materialList[0].childrenList.length > 0
            ? this.materialList[0].childrenList[0].materialNo
            : "";
        this.skuNo =
          this.materialList[0].childrenList.length > 0
            ? this.materialList[0].childrenList[0].itemCode
            : "";
        this.materialsName = this.materialList[0].name;
        this.colorName =
          this.materialList[0].childrenList.length > 0
            ? this.materialList[0].childrenList[0].name
            : "";
        this.materialsColorValue =
          this.materialList[0].childrenList.length > 0
            ? this.materialList[0].childrenList[0].colour
            : this.materialList[0].colour;
        this.manufactor =
          this.materialList[0].childrenList.length > 0
            ? this.materialList[0].childrenList[0].manufactor
            : this.materialList[0].manufactor;

        // 获取是否允许用户上传
        this.uploadValid = this.materialList[0].childrenList[0].allowUploadFlag;
        // 是否强制铺满血位：1 是，0 否
        let isAllPlace =
          this.materialList[0].childrenList[0].mandatoryCoveredBleedFlag;
        sessionStorage.setItem("isAllPlace", isAllPlace);

        // 是否缺货
        this.isMaterialStockOut = this.materialList[0].childrenList[0]
          .underStockFlag
          ? true
          : false;
      }
    },
    // 获取图片列表
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
            this.message = "";
            this.isLoading = false;
            if (res.data && res.data.length > 0) {
              this.picData = res.data;

              let flag = this.checkPic();
              if (flag === false && this.picId !== 0) {
                this.isError = true;
                this.$toast.fail(
                  "当前图片不可用，请点击左下角“图库”重新选择图片~"
                );
                this.picId = 0;
                this.picName = "";
                this.imgData.posX = null;
                this.wallpImg = "";
                this.reset();
              } else {
                let url = this.$route.query.url;
                let pid = this.$route.query.pid;
                let type = this.$route.query.type;
                if (!this.wallpImg && this.isPic) {
                  this.changeImage(url, pid, type);
                } else {
                  if (this.wallpImg && this.wallpImg.indexOf("canva") !== -1) {
                    this.picId = 0;
                  }
                  // 重绘
                  this.$refs.mcanvas.initCanvas();
                  // 判断是否允许用户上传
                  if (Number(this.uploadValid) !== 1) {
                    // 否
                    this.$refs.mcanvas.dragArr.forEach((item, index) => {
                      if (item.picId === 0) {
                        // 清空已上传图片
                        this.$refs.mcanvas.dragArr.splice(index, 1);
                      }
                    });
                  }
                  if (Number(this.picType) === 2) {
                    this.$refs.mcanvas.drawIpImage();
                  } else {
                    this.$refs.mcanvas.draw();
                  }
                  this.$refs.phoneWrapper.dispatchEvent(
                    new MouseEvent("click")
                  );
                }
              }
            }
          } else {
            this.picData = [];
            this.$toast(res.errMessage);
            this.message = "";
            this.isLoading = false;
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    // 根据手机型号和材质获取手机定制信息
    getInfo() {
      let isZFold = this.modelName.toUpperCase().indexOf("FOLD3") >= 0 ? 1 : 0;
      this.isZFold = isZFold;
      if (Number(sessionStorage.getItem("isZFold")) !== isZFold) {
        // 清空画布
        this.$refs.mcanvas.dragArr = [];
        this.$refs.mcanvas.clear();
      }
      sessionStorage.setItem("isZFold", isZFold);

      this.$api
        .get(this, api.getCustomInfo, {
          modelId: this.modelId,
          materialId: this.materialsId,
        })
        .then((res) => {
          if (res.success) {
            if (res.data) {
              this.isData = true;
              this.modelinfo = res.data;

              this.phoneImg = this.modelinfo.floorImage;
              this.frameImg = this.modelinfo.outImage;
              // 手机图长宽比
              this.pscale =
                this.pm2px(this.modelinfo.width) /
                this.pm2px(this.modelinfo.length);
              this.phonenHeight = this.wheight * 0.65;
              this.phonenWidth = this.phonenHeight * this.pscale;

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
                this.phoneHeight = this.wheight * 0.65 + this.pm2px(2);
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
                this.phoneHeight = this.wheight * 0.65;
                this.phoneWidth = this.phoneHeight * this.pscale;
                this.mh = this.nh = this.modelinfo.length;
                this.mw = this.nw = this.modelinfo.width;
              }

              sessionStorage.setItem("nw", this.nw.toFixed(2));
              sessionStorage.setItem("nh", this.nh.toFixed(2));

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

              if (this.materialsId !== "") {
                this.getPicList();
                this.getPrice();
              }
            }
          } else {
            this.isData = false;
            this.$toast(res.errMessage);
            this.message = "";
            this.isLoading = false;
          }
        });
    },
    getPrice() {
      this.$api
        .get(this, api.getPrice, {
          distributorId: this.distributorId,
          orderSource: this.platform,
          materialId: this.materialsId,
        })
        .then((res) => {
          if (res.success) {
            this.isData = true;
            if (res.data) {
              this.price = res.data + this.copyrightCost;
            } else {
              this.price = this.copyrightCost;
            }
            this.isPrice = true;
          } else {
            this.wallpImg = "";
            this.picId = 0;
            this.message = "";
            this.isLoading = false;
            this.isPrice = false;
            this.price = 0;
            this.isData = false;
            this.picData = [];
            this.$toast(res.errMessage);
            this.reset();
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    // 获取字体列表
    getFont() {
      this.$api.get(this, api.getFontList).then((res) => {
        if (res.success) {
          if (res.data && res.data.length > 0) {
            this.fontFamily = res.data;
            this.family = this.family
              ? this.family
              : this.fontFamily[0].englishName;
            this.initFont(this.fontFamily[0], this.family);
            this.fontColor = this.colors.hex;
          }
        }
      });
    },
    getdpi(value, ratio) {
      this.dpiValue = value;
      this.ratio = ratio;
    },
    getDpiValue(value) {
      this.dpiValue = value;
      this.dpiRate =
        Number(value) / 50 >= 5 ? 5 : Math.ceil(Number(value) / 50);
      if (this.dpiRate >= 5) {
        this.dpiDesc = "清晰可使用";
        this.dpiColor = "#1EC15F";
        return "clearly";
      } else if (this.dpiRate > 2 && this.dpiRate < 5) {
        this.dpiDesc = "模糊，画质一般";
        this.dpiColor = "#FCDF4F";
        return "general";
      } else if (this.dpiRate > 0 && this.dpiRate <= 2) {
        this.dpiDesc = "图片无法使用";
        this.dpiColor = "#FF5061";
        return "dim";
      }
    },
    getScale(width, height) {
      let phoneWidth = this.phoneWidth;
      if (this.isZFold) {
        phoneWidth = this.phoneWidth / 2;
      }
      return height / this.phoneHeight > width / phoneWidth
        ? width / phoneWidth
        : height / this.phoneHeight;
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
    change(url, pic) {
      this.picDialog = false;
      this.mask = false;
      this.picId = pic.id;
      this.picName = pic.name;

      // 图片版权费
      this.copyrightCost = 0;
      if (pic.copyrightCost) {
        this.copyrightCost = pic.copyrightCost;
      }
      // 图片类型 1-普通图片 2-IP图片（不可更改）
      if (this.isPrice) {
        this.imgData.posX = null;
        this.message = "载入中";
        this.isLoading = true;
        // store.commit('showLoading')
        this.getBrand();
        this.changeImage(url, pic.id, pic.type);
      }
    },
    changeImage(url, id, type) {
      this.isPic = false;
      this.wallpImg = url;
      if (this.materialsId !== "") {
        this.wallpImg = url;
        this.picId = id;
        // 图片类型 1-普通图片 2-IP图片（不可更改） 0-Canva
        this.picType = type;
        Promise.all([this.loadImage(url, type)]).then((img) => {
          this.isLoading = false;
          let w = img[0].width;
          let h = img[0].height;
          let diffX = 0;
          let diffY = 0;
          let scale = this.getScale(w, h);
          let centerX, centeruX;
          let centerY, centeruY;
          this.imgData.initW = w / scale;
          this.imgData.initH = h / scale;

          if (this.imgData.posX == null || this.picType === 2) {
            centerX = window.screen.width / 2;
            if (this.isZFold) {
              centerX = (window.screen.width - this.phonenWidth / 2) / 2;
            }
            centerY = this.wheight / 2;
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
          // let canvasRatio = this.getPixelRatio()
          let imgObj = {
            x: this.imgData.posX,
            y: this.imgData.posY,
            initx: this.imgData.posX,
            inity: this.imgData.posY,
            ux: this.imgData.posuX,
            uy: this.imgData.posuY,
            url: img[0],
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
        });
      }
    },
    // 加载图片
    loadImage(url, type) {
      let img = new Image();
      img.crossOrigin = "anonymous";
      if ((url && url.indexOf("data:") === 0) || Number(type) === 0) {
        img.src = url;
      } else {
        img.src = url + "?t=" + new Date().getTime();
      }
      return new Promise((resolve) => {
        if (img.complete) {
          resolve(img);
        }
        img.onload = () => resolve(img);
      });
    },
    // 添加文字
    addText() {
      if (this.validateText()) {
        if (this.textData.posX === null) {
          // 获取中心坐标
          this.textData.posX = window.screen.width / 2;
          this.textData.posY = this.wheight / 2;
          this.textData.posuX = this.phonenWidth / 2;
          this.textData.posuY = this.phonenHeight / 2;
          this.textData.sizeW = 20;
          this.textData.sizeH = 20;
          this.textData.rotate = 0;
          if (this.isZFold) {
            this.textData.posX =
              (window.screen.width - this.phonenWidth / 2) / 2;
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
          fillstyle: this.fontColor,
          filltext: this.inputtext,
          fontsize: this.textData.sizeH,
          fontfamily: this.family,
          type: 2,
          picType: 0,
          rotate: this.textData.rotate,
          selected: true,
          index: this.index,
        };
        this.spriteArr = [];
        this.spriteArr.push(textObj);
        this.textDialog = false;
        this.mask = false;
      }
    },
    getName(val) {
      let name;
      this.fontFamily.forEach((item) => {
        if (item.englishName === val) {
          name = item.name;
        }
      });
      return name;
    },
    // 获取字体英文名
    getFamilyEn(name) {
      let nameEn;
      this.fontFamily.forEach((item) => {
        if (item.name === name) {
          nameEn = item.englishName;
        }
      });
      return nameEn;
    },
    selectText(sprite, index) {
      // 图片，清空文字信息
      if (sprite.type === 1 && index === null) {
        // this.clearText(sprite.type)
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
      } else if (sprite.type === 2) {
        // 切换至文字
        this.textData.posX = sprite.x;
        this.textData.posY = sprite.y;
        this.textData.posuX = sprite.ux;
        this.textData.posuY = sprite.uy;
        this.textData.sizeW = sprite.w;
        this.textData.sizeH = sprite.h;
        this.family = sprite.fontfamily;
        this.inputtext = sprite.filltext;
        this.fontColor = sprite.fillstyle;
        this.textData.rotate = sprite.rotate;
        this.index = index;

        this.$refs.mcanvas.draw(); // 重绘，以免删减文字错位
      }
    },
    // 清空文字
    clearText(type) {
      // 1--图片  2--文字 0--未选中
      if (type === 1) {
        this.imgData.posX = null;
        this.wallpImg = "";
        this.dpiValue = 0;
      } else if (type === 2) {
        this.inputtext = "";
        this.family = "";
        this.fontColor = this.colors.hex;
        this.textData.posX = null;
      } else if (type === 0) {
        this.imgData.posX = null;
        this.textData.posX = null;
        this.inputtext = "";
        this.family = "";
        // this.dpiValue = 0
        this.fontColor = this.colors.hex;
      } else if (type === 4) {
        this.imgData.posX = null;
        this.textData.posX = null;
        this.inputtext = "";
        this.family = "";
        this.fontColor = this.colors.hex;
        this.dpiValue = 0;
        this.showMsg = true;
      }
    },
    reset() {
      this.$refs.mcanvas.reset();
    },
    // 完成定制
    make() {
      this.submitDialogVisible = false;
      if (this.wallpImg !== "") {
        if (this.isData) {
          if (this.skuNo === "0" || this.skuNo === 0 || this.skuNo === "") {
            this.$toast.warning("信息异常, 请稍后重试！");
            return;
          }
          this.message2 = "正在生成预览";
          this.isLoading2= true;
		  
          // 判断IP图片是否已经定制
          if (this.picType === 2) {
            this.isMake = false;
            // 获取是否已生成定制信息
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
                    ((this.manufactor === "MK" &&
                      res.data.generateImage.indexOf(".pdf") > -1) ||
                      (this.manufactor === "LHW" &&
                        res.data.generateImage.indexOf(".png") > -1))
                  ) {
                    // pdf 或 png
                    let image = res.data.previewImage;
                    let generateImage = res.data.generateImage;
                    this.isMake = true;
                    this.submit(image, generateImage);
                  } else {
                    this.isMake = false;
                    this.$refs.mcanvas.make();
                  }
                } else {
                  this.$toast(res.errMessage);
                }
              })
              .catch((err) => {
                console.log(err);
              });
          } else {
            this.$refs.mcanvas.make();
          }
        } else {
          this.$toast("请先完善数据！");
        }
      } else {
        this.$toast("请选择图片！");
      }
    },
    // 定制成功并跳转
    submit(image, generateImage) {
	  this.message2 = "";
	  this.isLoading2= false;
      // 保存定制数据
      let materialsValue = this.colorName
        ? this.materialsName + " - " + this.colorName
        : this.materialsName;
      var enterParams = JSON.stringify({
        picType: this.picType,
        isMake: this.isMake,
        diyPic: image,
        diyPdf: generateImage,
        materialId: this.materialsId,
        modelId: this.modelId,
        materialsName: encodeURI(materialsValue),
        modelName: encodeURI(this.modelName),
        brandId: this.brandId,
        brandName: encodeURI(this.brandname),
        pictureId: this.picId,
        picType: this.picType,
        picture: encodeURI(this.picName),
        price: this.price,
        sku: this.skuNo,
        manufactor: this.manufactor,
        materialsType: this.materialsType,
        designId: this.designId,
      });
      let path = this.platform === "7" ? "/previewer" : "/preview";
      this.$router.push({
        path: path,
        query: {
          enterParams: enterParams,
        },
      });
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
    // 拍照
    takePhoto(e) {
      let file = e.target.files[0];
      this.getUploadImg(file);
    },
    // 上传图片
    picUpload(event) {
      let file = event.target.files[0];
      this.getUploadImg(file);
    },
    getUploadImg(file) {
      this.message = "载入中";
      this.isLoading = true;

      let reader = this.handleBeforeUpload(file);
      reader.addEventListener("load", () => {
        Promise.all([this.loadImage(reader.result)]).then((img) => {
          // let pic = this.handleImgRotate(img[0]); // 验证图片是否需要旋转
          let pic = this.handleImage(img[0]); // 不旋转图片
          this.uploadDialog = false;
          this.mask = false;
          this.wallpImg = pic;
          this.imgData.posX = null;
          // id： 0-网络图片 1-普通图片 type: 1-普通图片 2-IP图
          this.picName = "";
          this.changeImage(this.wallpImg, 0, 1);
        });
      });
    },
    // 上传图片验证
    handleBeforeUpload(file) {
      let reader = new FileReader();
      if (
        file.type &&
        !(
          file.type === "image/png" ||
          file.type === "image/jpg" ||
          file.type === "image/jpeg"
        )
      ) {
        this.$toast.fail({
          title: "警告",
          message: "请上传格式为image/png, image/jpg, image/jpeg的图片",
        });

        this.message = "";
        this.isLoading = false;
        return;
      } else {
        // reader.onload = function () {
        //   document.getElementById('picfile').setAttribute('type', 'text')
        // }
        reader.readAsDataURL(file);
      }
      let isLt10M = file.size / 1024 / 1024 < 10;
      if (!isLt10M) {
        this.$roast.fail("上传图片大小不能超过 10MB!");

        this.message = "";
        this.isLoading = false;
        return;
      }
      return reader;
    },
    // 不翻转
    handleImage(imgObj) {
      let img;
      let canvas = document.createElement("canvas");
      let ctx = canvas.getContext("2d");

      EXIF.getData(imgObj, function () {
        canvas.width = imgObj.width;
        canvas.height = imgObj.height;

        ctx.drawImage(imgObj, 0, 0, imgObj.width, imgObj.height);
        img = canvas.toDataURL("image/png");
      });
      return img;
    },
    // 翻转图片
    handleImgRotate(imgObj) {
      let img;
      let canvas = document.createElement("canvas");
      let ctx = canvas.getContext("2d");
      let orientation = null;

      EXIF.getData(imgObj, function () {
        // 获取某个数据方向参数
        orientation = EXIF.getTag(this, "Orientation");
        // 为6或8的时候，图片需宽高反转
        if (orientation && (orientation == 6 || orientation == 8)) {
          canvas.width = imgObj.height;
          canvas.height = imgObj.width;
        } else {
          canvas.width = imgObj.width;
          canvas.height = imgObj.height;
        }
        ctx.drawImage(imgObj, 0, 0, imgObj.width, imgObj.height);
        if (orientation) {
          // 为1，正常
          switch (Number(orientation)) {
            case 3: // 需要180度旋转
              ctx.rotate((180 * Math.PI) / 180);
              ctx.drawImage(
                imgObj,
                -imgObj.width,
                -imgObj.height,
                imgObj.width,
                imgObj.height
              );
              break;
            case 6: // 需要顺时针（向左）90度旋转
              ctx.rotate((90 * Math.PI) / 180);
              ctx.drawImage(imgObj, 0, 0, imgObj.width, -imgObj.height);
              break;
            case 8: // 需要逆时针（向右）90度旋转
              ctx.rotate((270 * Math.PI) / 180);
              ctx.drawImage(
                imgObj,
                -imgObj.height,
                0,
                imgObj.height,
                imgObj.width
              );
              break;
            default:
              ctx.drawImage(imgObj, 0, 0, imgObj.width, imgObj.height);
              break;
          }
        }
        img = canvas.toDataURL("image/png");
      });
      return img;
    },
    pickerDialog() {
      if (!this.isLoading) {
        this.getBrand("show");
        if (this.isFirstClick) {
          this.isFirstClick = false;
        } else {
          this.showModelPicker = true;
        }
      }
    },
    MaterialDialog() {
      if (
        !this.isLoading &&
        (this.materialData.length > 1 ||
          (this.materialData.length === 1 &&
            this.materialData[0].children &&
            this.materialData[0].children.length > 1))
      ) {
        this.showPicker = true;
      }
    },
    handleModelConfirm(arr) {
      // 判断是否缺货
      if (!this.isModelStockOut) {
        // 否
        this.message = "载入中";
        this.isLoading = true;
        this.showModelPicker = false;
        // 获取品牌型号
        this.brand = arr[0].text + " " + arr[1].text;
        this.brandId = arr[0].id;
        this.brandname = arr[0].text;
        this.modelId = arr[1].id;
        this.modelName = arr[1].text;
        this.mobile = arr[1].text;
        // 重置图片，文字
        this.imgData.posX = null;
        this.textData.posX = null;
        this.getMaterialList();
      }
    },
    handleMaterialConfirm(arr) {
      // 判断是否缺货
      if (!this.isMaterialStockOut) {
        // 自定义模式下，编辑器有内容时，材质切换
        if (
          this.picType === 1 &&
          arr[1].id !== this.materialsId &&
          this.$refs.mcanvas.dragArr &&
          this.$refs.mcanvas.dragArr.length > 0
        ) {
          // 按钮颜色
          let buttonColor = "#0076A5";
          if (Number(this.distributorId) === 4378) {
            // 荣耀
            buttonColor = "#256FFF";
          }

          // 切换提示
          this.$dialog
            .confirm({
              title: "温馨提示",
              message: "切换材质后，将会清空已选图片，确认切换吗？",
              className: "confirm-v-dialog tl",
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              cancelButtonColor: buttonColor,
            })
            .then(() => {
              // 重置画布
              this.picId = 0;
              this.$toast("材质属性已更变，请重新选择图片");
              this.reset();
			  
			  this.getDpiValue(0);

              // 加载数据
              this.message = "载入中";
              this.isLoading = true;
              this.showPicker = false;
              // 获取材质
              this.materialsName = arr[0].text;
              this.materialsId = arr[1].id ? arr[1].id : arr[0].id;
              this.materialNo = arr[1].materialNo;
              this.skuNo = arr[1].skuNo;
              this.colorName = arr[1].text === arr[0].text ? "" : arr[1].text;
              this.materialsColorValue = arr[1].color
                ? arr[1].color
                : arr[0].color;
              this.manufactor = arr[1].manufactor;

              // 获取是否允许用户上传
              this.uploadValid = arr[1].allowUploadFlag;
              // 是否强制铺满血位：1 是，0 否
              let isAllPlace = arr[1].mandatoryCoveredBleedFlag;
              sessionStorage.setItem("isAllPlace", isAllPlace);

              // 是否缺货
              this.isMaterialStockOut = arr[1].underStockFlag ? true : false;

              this.getBrand();
              this.$refs.phoneWrapper.dispatchEvent(new MouseEvent("click"));
            })
            .catch((error) => {
              console.log(error);
            });
        } else {
          this.message = "载入中";
          this.isLoading = true;
          this.showPicker = false;
          // 获取材质
          this.materialsName = arr[0].text;
          this.materialsId = arr[1].id ? arr[1].id : arr[0].id;
          this.materialNo = arr[1].materialNo;
          this.skuNo = arr[1].skuNo;
          this.colorName = arr[1].text === arr[0].text ? "" : arr[1].text;
          this.materialsColorValue = arr[1].color ? arr[1].color : arr[0].color;
          this.manufactor = arr[1].manufactor;

          // 获取是否允许用户上传
          this.uploadValid = arr[1].allowUploadFlag;
          // 是否强制铺满血位：1 是，0 否
          let isAllPlace = arr[1].mandatoryCoveredBleedFlag;
          sessionStorage.setItem("isAllPlace", isAllPlace);

          // 是否缺货
          this.isMaterialStockOut = arr[1].underStockFlag ? true : false;

          this.getBrand();
          this.$refs.phoneWrapper.dispatchEvent(new MouseEvent("click"));
        }
      }
    },
    modelChange(picker, values) {
      picker.setColumnValues(1, values[0].children);
      // 判断是否缺货
      if (values[1]) {
        // 已选子类，判断子类跟父类是否匹配
        let curType = 0;
        values[0].children.forEach((item) => {
          if (item.id === values[1].id) {
            curType = 1;
          }
        });

        if (curType) {
          // 匹配，判断子类是否缺货
          if (values[1].underStockFlag) {
            // 是
            this.isModelStockOut = true;
          } else {
            // 否
            this.isModelStockOut = false;
          }
        } else {
          // 不匹配，判断父类第一项是否缺货
          if (values[0].children[0] && values[0].children[0].underStockFlag) {
            // 是
            this.isModelStockOut = true;
          } else {
            // 否
            this.isModelStockOut = false;
          }
        }
      } else {
        // 暂无选择，判断父类第一项是否缺货
        if (values[0].children[0] && values[0].children[0].underStockFlag) {
          // 是
          this.isModelStockOut = true;
        } else {
          // 否
          this.isModelStockOut = false;
        }
      }
    },
    materialChange(picker, values) {
      picker.setColumnValues(1, values[0].children);
      // 判断是否缺货
      if (values[1]) {
        // 已选子类，判断子类跟父类是否匹配
        let curType = 0;
        values[0].children.forEach((item) => {
          if (item.id === values[1].id) {
            curType = 1;
          }
        });

        if (curType) {
          // 匹配，判断子类是否缺货
          if (values[1].underStockFlag) {
            // 是
            this.isMaterialStockOut = true;
          } else {
            // 否
            this.isMaterialStockOut = false;
          }
        } else {
          // 不匹配，判断父类第一项是否缺货
          if (values[0].children[0] && values[0].children[0].underStockFlag) {
            // 是
            this.isMaterialStockOut = true;
          } else {
            // 否
            this.isMaterialStockOut = false;
          }
        }
      } else {
        // 暂无选择，判断父类第一项是否缺货
        if (values[0].children[0] && values[0].children[0].underStockFlag) {
          // 是
          this.isMaterialStockOut = true;
        } else {
          // 否
          this.isMaterialStockOut = false;
        }
      }
    },
    load() {
      this.loading = true;
      setTimeout(() => {
        this.loading = false;
      }, 2000);
    },
    removeText() {
      this.clearText();
      this.mask = false;
      this.textDialog = false;
    },
    // 判断当前图片是否可用
    checkPic() {
      let flag = false;
      this.picData.forEach((item) => {
        if (item.childrenList && item.childrenList.length > 0) {
          item.childrenList.forEach((child) => {
            if (child.pictureList && child.pictureList.length > 0) {
              child.pictureList.forEach((pic) => {
                if (pic.id == this.picId) {
                  flag = true;
                }
              });
            }
          });
        } else {
          item.pictureList.forEach((pic) => {
            if (pic.id == this.picId) {
              flag = true;
            }
          });
        }
      });
      return flag;
    },
    checkVersion() {
      let u = navigator.userAgent;
      let isAndroid = u.indexOf("Android") > -1 || u.indexOf("Linux") > -1; // android终端或者uc浏览器
      let type = u.toLowerCase().match(/QQ/i);
      // let isiOS = !!u.match(/\(i[^]+( U)? CPU.+Mac OS X/) // ios终端
      if (isAndroid && type === "qq") {
        this.$refs.photoFile.setAttribute("capture", "camera");
      }
    },
    validateText() {
      if (this.family === "") {
        this.$toast("请选择字体");
        return false;
      }
      if (this.inputtext === "") {	  
        this.$toast("请输入文字");
        return false;
      }
	  
	  
      return true;
    },
	
    // 打开确认弹框
    openSubmit() {
	   // 遍历canvas画布中所有图层，判断是否有图片
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
	   
	   if (!(this.isModelStockOut || this.isMaterialStockOut || this.materialData.length <= 0)) {
		 if (hasPic) {
			 if (this.isData) {
			   if (this.skuNo === "0" || this.skuNo === 0 || this.skuNo === "") {
				 this.$toast.warning("信息异常, 请稍后重试！");
				 return;
			   } else {
				 this.submitDialogVisible = true;
			   }
			 } else {
			   this.$toast("请先完善数据！");
			 }
		 } else {
			 this.$toast("请选择图片！");
		 }
	   }
    },
    openPic(e) {
      if (this.picData.length > 0) {
        this.mask = true;
        this.picDialog = true;
      } else {
        this.$toast("暂无图片！");
      }
    },
    openImage(e) {
      this.mask = true;
      this.uploadDialog = true;
    },
    openText(e) {
      if (Number(this.materialsId) === 86) {
        this.$toast("当前材质暂不支持文字功能，如有疑问请联系客服~");
      } else {
        if (this.picType !== 2) {
          // 获取字体
          if (this.fontFamily.length === 0) {
            this.getFont();
          }

          this.$refs.textWrapper.style.height = "auto";
          this.mask = true;
          this.textDialog = true;
          this.clearText();
          if (this.spriteArr.length > 0) {
            this.spriteArr.forEach((item) => {
              if (item.selected && item.type === 0) {
                this.inputtext = item.filltext;
                this.family = item.fontFamily;
                this.fontColor = item.fontColor;
              }
            });
          }
        } else {
          this.error = "IP图受版权保护不可编辑哦～";
          this.showMsg = true;
        }
      }
    },
    openCategory() {
      this.mask = true;
      this.categoryDialog = true;
    },
    close() {
      this.mask = false;
      this.textDialog = false;
      this.showSelct = false;
    },
    closePic() {
      this.mask = false;
      this.picDialog = false;
    },
    closesxPics() {
      this.mask = false;
      this.sxPicsDialog = false;
    },
    closeUp() {
      this.mask = false;
      this.uploadDialog = false;
    },
    closeCategory() {
      this.mask = false;
      this.categoryDialog = false;
    },
    closeMask() {
      this.mask = false;
      this.textDialog = false;
      this.uploadDialog = false;
      this.picDialog = false;
      this.categoryDialog = false;
      this.sxPicsDialog = false;
      this.dialogVisible = false;
    },
    toback() {
      this.$router.push("/index");
    },
    // 上传图片 - 提示
    chooseImg() {
      // 判断是否允许用户上传
      if (Number(this.uploadValid) === 1) {
        // 是
        if (this.isFlag > 0) {
          this.$refs.file.value = ""; // 再次点击清空input="file"数据，以免同一图片不能重复上传
          this.$refs.file.dispatchEvent(new MouseEvent("click"));
        } else {
          // 按钮颜色
          let buttonColor = "#0076A5";
          if (Number(this.distributorId) === 4378) {
            // 荣耀
            buttonColor = "#256FFF";
          }

          this.hasDialog = true;
          this.$dialog
            .confirm({
              title: "温馨提示",
              message:
                "尊敬的用户你好，请保证图片清晰可用，不可上传违反国家法律法规图片，对此造成的后果我方不予承担，印刷制作存在些许误差，介意请勿下单喔。",
              className: "confirm-v-dialog tl",
              confirmButtonText: "我知道了",
              confirmButtonColor: buttonColor,
              cancelButtonText: "返回",
            })
            .then(() => {
              this.hasDialog = false;
              this.isFlag++;
              this.$refs.file.dispatchEvent(new MouseEvent("click"));
            })
            .catch((error) => {
              this.hasDialog = false;
              console.log(error);
            });
        }
      } else {
        this.$toast("当前材质不支持图片上传");
      }
    },
    // Canva
    handleCanva() {
      // 判断是否允许用户上传
      if (Number(this.uploadValid) === 1) {
        // 是
        // 监听canva iframe元素，点击canva按钮设置block
        let canvaIframe = document.querySelector('[dir="ltr"]');
        canvaIframe.style.display = "block";
        // 判断是否存在 Canva designId
        if (this.designId) {
          this.handleEditCanva();
        } else {
          this.handleCreateCanva();
        }
      } else {
        this.$toast("当前材质不支持自定义");
      }
    },
    handleCreateCanva() {
      var _self = this;
      // 新建 Canva
      this.$store.state.user.canvaApi.createDesign({
        design: {
          type: "PhoneCase",
          dimensions: {
            units: "px",
            width: 882,
            height: 1887,
          },
        },
        onDesignOpen: function (options) {
          // 编辑完成，保存 designId
          _self.designId = options.designId;
        },
        onDesignPublish: function (options) {
          // 在设计导出到图片上时触发
          _self.exportUrl = options.exportUrl;
          _self.designId = options.designId;

          _self.changeImage(_self.exportUrl, 0, 0);
          sessionStorage.setItem("canvaUrl", _self.exportUrl);
          let canvaIframe = document.querySelector('[dir="ltr"]');
          canvaIframe.style.display = "";
        },
        onDesignClose: function () {
          // 关闭编辑器时触发
          // 监听canva iframe元素，点击关闭设置为""
          let canvaIframe = document.querySelector('[dir="ltr"]');
          canvaIframe.style.display = "";
        },
      });
    },
    handleEditCanva() {
      var _self = this;
      // 编辑 Canva
      this.$store.state.user.canvaApi.editDesign({
        design: {
          type: "PhoneCase",
          id: _self.designId,
          dimensions: {
            units: "px",
            width: 882,
            height: 1887,
          },
        },
        onDesignOpen: function (options) {
          // 编辑完成，保存 designId
          _self.designId = options.designId;
        },
        onDesignPublish: function (options) {
          // 在设计导出到图片上时触发
          _self.exportUrl = options.exportUrl;
          _self.designId = options.designId;

          _self.changeImage(_self.exportUrl, 0, 0);
          sessionStorage.setItem("canvaUrl", _self.exportUrl);
          let canvaIframe = document.querySelector('[dir="ltr"]');
          canvaIframe.style.display = "";
        },
        onDesignClose: function () {
          // 关闭编辑器时触发
          // 监听canva iframe元素，点击关闭设置为""
          let canvaIframe = document.querySelector('[dir="ltr"]');
          canvaIframe.style.display = "";
        },
      });
    },
	
	//点击知道了隐藏柔性关闭弹窗
	clickFlexible(){
	  this.showFlexible = false;
	}
  },
  watch: {
    colors(value) {
      // 字体颜色
      this.fontColor = value.hex;
    },
    materialsId(value) {
      // 材质ID
      this.materialsId = value;
    },
    modelId(value) {
      // 型号ID
      this.modelId = value;
    },
    wallpImg(value) {
      this.wallpImg = value;
    },
    wheight(value) {
      this.wheight = value;
      this.phoneHeight = this.wheight * 0.65;
      this.phoneWidth = this.phoneHeight * this.pscale;
    },
    mask(value) {
      if (value) {
        document.body.style.cssText = "overflow: hidden";
        // document.getElementById('app').setAttribute('style', 'position:fixed')
      } else {
        document.body.style.cssText = "overflow: auto";
        // document.getElementById('app').removeAttribute('style')
        // document.getElementById('picfile').setAttribute('type', 'file')
      }
    },
  },
  components: {
    VHeader,
    pictureList,
    canvasDrag,
    Loading,
    "slider-picker": Slider,
  },
};
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
@import '~common/styles/mixin.styl';
@import '~common/styles/variable';

.noclick {
  pointer-events: none;
}

.phone {
  position: fixed;
  width: 100%;
  top: 0;
  bottom: 54px;
  background-color: $color-background;

  .content-wrapper {
    position: relative;
    width: 100%;
    height: 100%;

    .phone-wrapper {
      position: relative;
      width: 100%;
      overflow: hidden;

      .bgimg {
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate3d(-50%, -50%, 0);
        background-repeat: no-repeat;
      }

      >>> .canvas-container {
        margin: 0 auto;
      }

      .bg-box {
        position: absolute;
        left: 0;
        top: 0;
        right: 0;
        bottom: 0;
        height: 466px;
        background-size: 186px 388px;
        z-index: 1;
      }

      .up-box {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 466px;
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

      .activeBox {
        position: absolute;
        left: 20%;
        top: 0;
        border: 1px solid red;
        opacity: 1;

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
    }

    .brand-type {
      display: flex;
      flex-direction: column;
      font-size: 14px;

      .item {
        display: inline-block;
        line-height: 30px;
        text-align: center;
        font-size: 14px;
        color: #333333;
      }

      .dpi-type {
        margin-bottom: 6px;

        >>>.van-rate {
          display: inline-block;

          .van-rate__item {
            display: inline-block;
            top: 3px;
          }

          .van-rate__icon {
            font-size: 16px;
            line-height: 30px;
          }
        }

        .name {
          padding: 0 5px;
        }

        .van-rate__icon {
          font-size: 16px;
          line-height: 30px;
        }
      }

      .name {
        padding: 0 5px;
      }

      .dpi-desc {
        font-size: 12px;
        font-style: normal;
      }
    }

    .phone-brand {
      .name {
        padding-bottom: 2px;
        border-bottom: 1px solid #0076A5;
      }
    }

    .phone-type {
      .name {
        padding-bottom: 2px;

        &.border-bottom {
          border-bottom: 1px solid #0076A5;
        }
      }
    }
  }

  .dpi-box {
    position: absolute;
    top: 20px;
    right: 20px;
    border-radius: 50%;
    z-index: 999;

    .dpi-icon {
      position: relative;
      display: inline-block;
      width: 30px;
      height: 30px;
      line-height: 30px;
      padding: 3px;
      box-sizing: border-box;
      border-radius: 50%;
      text-align: center;
      z-index: 1000;

      .icon {
        position: absolute;
        display: inline-block;
        top: 50%;
        left: 50%;
        width: 24px;
        height: 24px;
        line-height: 24px;
        border-radius: 50%;
        font-size: 18px;
        color: #ffffff;
        transform: translate3d(-50%, -50%, 0);
      }

      &.clearly {
        background-color: #a0eea8;

        .icon {
          background-color: #2AC466;
        }
      }

      &.general {
        background-color: #ffcc77;

        .icon {
          background-color: #f6a92b;
        }
      }

      &.dim {
        background-color: #ffab9b;

        .icon {
          background-color: #f07159;
        }
      }
    }

    .desc {
      position: absolute;
      top: 0;
      right: 1px;
      width: 0;
      height: 30px;
      padding-right: 30px;
      line-height: 30px;
      font-size: $font-size-small;
      color: $color-text-w;
      text-align: center;
      border-radius: 72px;
      transition: all 0.4s;
      opacity: 0;
      overflow: hidden;
      z-index: 998;

      &.clearly {
        background-color: #2AC466;
      }

      &.general {
        background-color: rgba(246, 169, 43, 1);
      }

      &.dim {
        background-color: rgba(240, 113, 89, 1);
      }
    }

    .text {
      margin-top: 4px;
      text-align: center;
      font-size: $font-size-small;
      color: $color-text;
    }
  }

  .color-icon {
    display: inline-block;
    position: absolute;
    top: 80px;
    right: 20px;
    width: 30px;
    height: 30px;
    bg-image('color');
    background-size: 30px 30px;
    z-index: 99;
  }
}

.nav-wrapper {
  flex: none;
  display: flex;
  height: 54px;
  padding: 0 5px;
  align-items: center;
  background: rgba(255, 255, 255, 0.9);
  box-shadow: 0 2px 12px 0 rgba(235, 232, 232, 0.5);
  z-index: 98;

  .nav-list {
    display: flex;
    flex: 1;

    .nav-item {
      flex: 1;
      text-align: center;

      &.disable {
        .icon {
          color: #999999;
        }

        .text {
          color: #999999;
        }
      }

      input {
        display: none;
      }

      .icon {
        display: inline-block;
        font-size: 24px;
        color: #333333;
        width: 24px;
        height: 24px;
        margin-bottom: 10px;
        background-size: 24px 24px;
        background-repeat: no-repeat;

        &.category {
          display: inline-block;
          width: 24px;
          height: 24px;
          bg-image('category');
        }
      }

      .text {
        margin-top: -8px;
        font-size: 10px;
        text-align: center;
        color: #4A4A4A;
      }
    }
  }

  .btn-submit {
    display: inline-block;
    width: 40%;
    height: 36px;
    margin: 0 5px;
    line-height: 36px;
    font-size: 16px;
    color: #ffffff;
    text-align: center;
    border-radius: 20px;
    background: #0076A5;

    .price {
      display: block;
      margin-top: 2px;
      font-size: 14px;
      line-height: 16px;
    }

    .text {
      font-size: 12px;
      line-height: 16px;
    }

    &.diable {
      color: #333;
      background: #f4f4f4;

      .text {
        color: #f00;
      }
    }
  }
}

.text-wrapper {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 24px 15px;
  box-sizing: border-box;
  border-radius: 16px 16px 0 0;
  background-color: #ffffff;
  overflow-y: scroll;
  z-index: 100;
  -webkit-overflow-scrolling: touch;

  &::-webkit-scrollbar {
    display: none;
  }

  .title {
    text-align: left;

    .line {
      display: inline-block;
      width: 3px;
      height: 14px;
      margin-left: -15px;
      border-radius: 0 50% 50% 0;
      background: $color-theme;
    }

    .text {
      display: inline-block;
      margin-left: 15px;
      font-size: 16px;
      color: #4A4A4A;
    }

    .icon {
      display: inline-block;
      font-size: 20px;
      float: right;
    }
  }

  .input {
    width: 100%;
    margin-top: 30px;
    padding: 8px 16px;
    border-radius: 6px;
    box-sizing: border-box;
    border: none;
    background-color: rgba(155, 155, 155, 0.09);

    >>> .van-cell__value {
      height: 20px;
      line-height: 20px;
    }
  }

  .select-box {
    position: relative;

    &::after {
      position: absolute;
      top: -6px;
      left: 10px;
      margin-right: 3px;
      border-top-width: 0;
      border-bottom-color: #EBEEF5;
      background-color: red;
    }

    .select {
      width: 100%;
      margin-top: 20px;
      height: 36px;
      line-height: 36px;
      padding: 0 16px;
      border-radius: 6px;
      border: none;
      box-sizing: border-box;
      background-color: rgba(155, 155, 155, 0.09);
      -webkit-appearance: none;
      -moz-appearance: none;
      appearance: non;
      cursor: pointer;
      outline: none;

      .text {
        font-size: 14px;
      }
    }

    .select-list {
      position: absolute;
      top: 40px;
      left: 0;
      width: 100%;
      padding: 0 16px;
      border: 1px solid #E4E7ED;
      border-radius: 4px;
      background-color: #FFF;
      -webkit-box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      -webkit-box-sizing: border-box;
      box-sizing: border-box;
      float: left;
      z-index: 22;

      .select-item {
        height: 36px;
        line-height: 36px;
        font-size: $font-size-medium;
        color: $color-text;
      }
    }
  }

  .el-radio-group {
    display: flex;
    flex-wrap: wrap;
  }

  >>> .el-radio {
    position: relative;
    display: inline-block;
    width: 16.6%;
    margin: 28px 0 0 0;

    .el-radio__input {
      display: none;
    }

    .el-radio__label {
      padding: 0;
    }

    .color {
      display: inline-block;
      padding: 0;
      width: 18px;
      height: 18px;
      border-radius: 50%;
    }

    .iscolor {
      position: relative;
      display: inline-block;
      padding: 0;
      width: 18px;
      height: 18px;
      border-radius: 50%;

      &:after {
        content: '';
        position: absolute;
        left: 50%;
        top: 50%;
        width: 24px;
        height: 24px;
        border-radius: 50%;
        transform: translate3d(-50%, -50%, 0);
        border: 1px solid;
        border-color: inherit;
      }
    }
  }

  .color-box {
    width: 100%;
    margin: 40px 0 50px;
    display: flex;

    .color {
      display: inline-block;
      margin-top: 2px;
      width: 40px;
      height: 40px;
      border-radius: 2px;
      box-shadow: 1px 1px 5px rgba(0, 0, 0, 0.2);
    }

    .vc-slider {
      flex: 1;
      margin-left: 30px;

      >>> .vc-slider-hue-warp, >>> .vc-slider-swatches {
        box-shadow: 1px 1px 5px rgba(0, 0, 0, 0.2);
      }
    }
  }

  .btn-box {
    display: flex;
    flex-wrap: nowrap;
    align-content: flex-center;
    align-items: center;
    justify-content: space-around;
    margin-top: 36px;

    .btn {
      display: inline-block;
      width: 140px;
      height: 36px;
      line-height: 36px;
      font-size: 14px;
      text-align: center;
      border-radius: 20px;
      color: #ffffff;
    }

    .btn-cancel {
      background-color: rgba(0, 0, 0, 0.2);
    }

    .btn-submit {
      background: $color-theme;
    }
  }
}

.color-wrapper {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 15px 15px 20px;
  box-sizing: border-box;
  border-radius: 16px 16px 0 0;
  background-color: #ffffff;
  overflow-y: scroll;
  z-index: 100;
  -webkit-overflow-scrolling: touch;

  &::-webkit-scrollbar {
    display: none;
  }

  .title {
    text-align: left;

    .line {
      display: inline-block;
      width: 3px;
      height: 14px;
      margin-left: -15px;
      border-radius: 0 50% 50% 0;
      background: $color-theme;
      vertical-align: middle;
    }

    .text {
      display: inline-block;
      margin-left: 15px;
      font-size: 16px;
      color: #4A4A4A;
      vertical-align: middle;
    }

    .icon {
      display: inline-block;
      font-size: 20px;
      float: right;
    }
  }

  .color-list {
    width: 100%;
    margin: 40px 0 0;
    padding: 0 20px;
    box-sizing: border-box;

    .van-radio-group {
      display: flex;
      flex-direction: row;
      flex-wrap: wrap;

      &::after {
        content: '';
        flex: auto;
      }

      .van-radio {
        display: inline-block;
        margin: 0 auto 30px;
        width: 16.66%;
        height: 40px;
        text-align: center;

        >>>.van-radio__icon {
          display: inline-block;
          height: auto;

          .radio-icon {
            display: inline-block;
            position: relative;
            width: 27px;
            height: 27px;
            border-radius: 50%;
            border: 1px solid #ffffff;

            .radio-icon-r {
              display: inline-block;
              position: absolute;
              top: 50%;
              left: 50%;
              width: 20px;
              height: 20px;
              border-radius: 50%;
              transform: translate3d(-50%, -50%, 0);
            }
          }

          &.van-radio__icon--checked {
            .radio-icon {
              border-color: inherit;
            }
          }
        }

        >>> .van-radio__label {
          display: inline-block;
          margin: 0;
          height: auto;

          .radio-icon {
            position: relative;
            display: inline-block;
            width: 26px;
            height: 26px;
            border-radius: 50%;

            .radio-icon-r {
              display: inline-block;
              position: absolute;
              top: 50%;
              left: 50%;
              width: 20px;
              height: 20px;
              border-radius: 50%;
              transform: translate3d(-50%, -50%, 0);
            }
          }
        }
      }

      .color-item {
        display: inline-block;
        margin-bottom: 30px;
        width: 16.66%;
        height: 30px;
        text-align: center;

        .color-div {
          display: inline-block;
          width: 30px;
          height: 30px;
          background-color: #333;
          border-radius: 50%;
        }
      }
    }
  }

  .btn-box {
    display: flex;
    flex-wrap: nowrap;
    align-content: flex-center;
    align-items: center;
    justify-content: space-around;
    margin-top: 20px;

    .btn {
      display: inline-block;
      width: 140px;
      height: 36px;
      line-height: 36px;
      font-size: 14px;
      text-align: center;
      border-radius: 20px;
      color: #ffffff;
    }

    .btn-cancel {
      background-color: rgba(0, 0, 0, 0.2);
    }

    .btn-submit {
      background: $color-theme;
    }
  }
}

.category-wrapper {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 15px 15px 20px;
  box-sizing: border-box;
  border-radius: 16px 16px 0 0;
  background-color: #ffffff;
  z-index: 100;

  .title {
    text-align: left;

    .line {
      display: inline-block;
      width: 3px;
      height: 14px;
      margin-left: -15px;
      border-radius: 0 50% 50% 0;
      background: $color-theme;
    }

    .text {
      display: inline-block;
      margin-left: 15px;
      font-size: 16px;
      color: #4A4A4A;
    }

    .icon {
      display: inline-block;
      font-size: 20px;
      float: right;
    }
  }

  .item-ul {
    display: flex;
    display: -webkit-flex;
    flex-direction: row;
    flex-wrap: wrap;
    margin: 32px 0 60px;

    .item-li {
      display: inline-block;
      flex: 0 0 33.33%;
      text-align: center;
      margin-bottom: 12px;
      overflow: hidden;

      .item-img {
        position: relative;
        display: inline-block;
        width: 72px;
        height: 72px;
        border-radius: 8px;
        background-color: #F3F3F3;

        .img {
          position: absolute;
          left: 50%;
          top: 50%;
          transform: translate3d(-50%, -50%, 0);
        }
      }

      .name {
        margin-top: 12px;
        text-align: center;
        font-size: $font-size-medium;
        color: $color-text;
      }
    }
  }
}

.picSx-wrapper {
  position: fixed;
  left: 0;
  right: 0;
  top: 50px;
  bottom: 0;
  padding: 15px 15px 20px;
  box-sizing: border-box;
  border-radius: 16px 16px 0 0;
  background-color: #ffffff;
  z-index: 100;

  .title {
    height: 30px;
    line-height: 30px;
    text-align: center;

    .text {
      display: inline-block;
      font-size: 16px;
      color: #4A4A4A;
    }

    .icon {
      display: inline-block;
      font-size: 20px;
      float: right;
    }
  }

  .pics-ul {
    height: 100%;
    display: flex;
    display: -webkit-flex;
    flex-direction: row;
    flex-wrap: wrap;
    align-items: center;
    padding: 30px 0 40px;
    box-sizing: border-box;
    overflow: scroll;
    -webkit-overflow-scrolling: touch;

    &::-webkit-scrollbar {
      display: none;
    }

    .pic-li {
      display: inline-block;
      flex: 0 0 33.33%;
      text-align: center;
      margin-bottom: 10px;

      .pic-img {
        width: 110px;
        height: 150px;
        margin: 0 auto;
        overflow: hidden;
        border-radius: 8px;
        background-color: #999999;

        .img {
          position: absolute;
          left: 50%;
          top: 50%;
          transform: translate3d(-50%, -50%, 0);
        }
      }

      .name {
        margin-top: 12px;
        text-align: center;
        font-size: $font-size-medium;
        color: $color-text;
      }
    }
  }
}

.pic-wrapper {
  position: fixed;
  left: 0;
  bottom: 0;
  width: 100%;
  height: calc(100vh - 150px);
  box-sizing: border-box;
  border-radius: 16px 16px 0 0;
  background-color: #ffffff;
  z-index: 1000;

  .title {
    text-align: left;
    padding: 0 15px;
    height: 40px;
    line-height: 40px;

    .text {
      display: inline-block;
      font-size: 16px;
      font-weight: 600;
      color: #333333;
    }

    .icon {
      display: inline-block;
      font-size: 24px;
      height: 40px;
      line-height: 40px;
      color: $color-text-h;
      float: right;
    }
  }
}

.image-wrapper {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  max-height: 644px;
  padding: 20px 15px 20px;
  box-sizing: border-box;
  border-radius: 16px 16px 0 0;
  background-color: #ffffff;
  z-index: 100;

  .title {
    text-align: left;

    .line {
      display: inline-block;
      width: 3px;
      height: 14px;
      margin-left: -15px;
      border-radius: 0 50% 50% 0;
      background: $color-theme;
    }

    .text {
      display: inline-block;
      margin-left: 15px;
      font-size: 16px;
      color: #4A4A4A;
    }

    .icon {
      display: inline-block;
      font-size: 20px;
      float: right;
    }
  }

  .photograph {
    margin: 40px 20px 0;
    padding-bottom: 25px;
    text-align: left;
    border-bottom: 1px solid #F4F4F4;

    input {
      display: none;
    }

    .text {
      display: block;
      height: 18px;
      line-height: 18px;
      font-size: 14px;
      color: #4A4A4A;
    }

    .icon {
      display: inline-block;
      width: 20px;
      height: 20px;
      font-size: 20px;
      float: right;
    }
  }

  .photo {
    margin: 25px 20px;
    text-align: left;

    input {
      display: none;
    }

    .text {
      display: block;
      height: 18px;
      line-height: 18px;
      font-size: 14px;
      color: #4A4A4A;
    }

    .icon {
      display: inline-block;
      width: 20px;
      height: 20px;
      font-size: 20px;
      float: right;
    }

    .upload-pic {
      >>> .el-upload-list {
        display: none;
      }

      .van-button {
        flex: 1;
        justify-content: space-between;
        display: inline-block;
        margin: 0 10px;
        height: 40px;
        line-height: 40px;
        text-align: center;
        font-size: 14px;
        color: #ffffff;
        border-radius: 20px;

        &.van-dialog__confirm {
          background: #0076A5;
          margin: 0 auto;
        }

        &.van-dialog__cancel {
          color: #333333;
          background: #F4F4F4;

          >>> .el-upload {
            width: 100%;
            text-align: left;

            .text {
              display: inline-block;
              height: 18px;
              line-height: 18px;
              font-size: 14px;
              color: #4A4A4A;
            }

            .icon {
              display: inline-block;
              width: 18px;
              height: 18px;
              float: right;
              background-size: 18px 18px;
              background-repeat: no-repeat;
            }
          }
        }
      }
    }
  }
}

.msg-dialog {
  display: inline-block;
  width: 90%;
  padding: 18px 0 0;
  text-align: center;
  box-sizing: border-box;
  border-radius: 8px;

  >>> .van-dialog__confirm {
    color: #0076A5;
  }

  .title {
    display: inline-block;
    font-weight: bold;
    color: $color-text;
    font-size: $font-size-large;
    text-align: center;

    .icon {
      display: inline-block;
      margin-top: -2px;
      width: 20px;
      height: 20px;
      bg-image('remind');
      background-size: 20px 20px;
      vertical-align: middle;
    }

    .text {
      vertical-align: middle;
    }
  }

  .info {
    display: block;
    margin: 16px 24px 20px;

    .text {
      display: inline-block;
      margin-top: 4px;
      color: $color-text-b;
      font-size: $font-size-medium;
      line-height: 1.5;
      text-align: justify;
    }

    .error {
      display: inline-block;
      margin-top: 4px;
      color: $color-text-b;
      font-size: $font-size-medium-x;
      line-height: 1.5;
      text-align: justify;
    }
  }
}

.material-dialog {
  width: 100%;
  box-sizing: border-box;
  padding: 20px 25px;
  background-color: #ffffff;
  border-radius: 10px;
  overflow: hidden;

  &.confirm {
    width: 92%;
    background-color: #FEFEFE;
  }

  >>> .van-dialog__header {
    padding: 0 0 20px;
  }

  >>> .van-dialog__content {
    .item-content {
      display: flex;
      padding: 10px 15px;
      margin-bottom: 20px;
      background-color: #ffffff;
      border-radius: 8px;
      align-items: center;
      box-shadow: 0px 2px 15px 0px rgba(227, 224, 228, 1);

      &.no-shadow {
        box-shadow: none;
      }

      .item-img {
        display: inline-block;
        width: 65px;
        height: 65px;
        margin-right: 16px;
        border-radius: 4px;
        background-color: #999;
      }

      .item-box {
        flex: 1;
        text-align: left;
        overflow: hidden;
        margin-left: 30px;
        padding: 10px 0;

        .name {
          font-size: 16px;
          color: #333333;

          &.xs {
            font-size: 14px;
          }
        }

        .text {
          color: #0076A5;
        }

        .dec {
          display: flex;
          margin-top: 14px;
          font-size: 14px;
          color: #333333;

          .left {
            display: inline-block;
          }

          .text {
            flex: 1;
            flex-wrap: wrap;
            line-height: 1.5;
            margin-top: -4px;
          }
        }
      }
    }
  }

  >>> .van-dialog__footer {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    text-align: center;
    padding: 30px 0 10px;

    &:after {
      border: none;
    }

    .van-button {
      display: inline-block;
      flex: 1;
      margin: 0 10px;
      height: 40px;
      line-height: 40px;
      text-align: center;
      font-size: 14px;
      border-radius: 20px;

      &.van-dialog__confirm {
        color: #ffffff;
        background: #0076A5;
      }

      &.van-dialog__cancel {
        color: #333333;
        background-color: #F4F4F4;
      }
    }
  }
}

.mask {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  z-index: 99;
  background: rgba(0, 0, 0, 0.2);
}

.phone-picker, .picker-wrap {
  >>> .picker {
    top: 50%;
    padding-bottom: 50px;
    transform: translateY(-50%);
    border-radius: 10px;

    .picker-title {
      &::after {
        border-bottom: none;
      }

      .pt-cancel {
        display: none;
      }

      .pt-submit {
        position: fixed;
        bottom: 20px;
        left: 50%;
        border-radius: 20px;
        color: #ffffff !important;
        transform: translateX(-50%);
        width: 140px;
        background: #0076A5;
        height: 36px;
        line-height: 36px;
        text-align: center;
        z-index: 10001;
      }
    }

    .picker-panel {
      .picker-wheel-wrapper {
        .picker-wheel {
          flex: none;

          &:nth-child(1) {
            width: 35%;
          }

          &:nth-child(2) {
            width: 65%;
          }
        }

        .wheel-color {
          display: inline-block;
          width: 14px;
          height: 14px;
        }
      }
    }
  }
}

.fadeColor-enter-active, .fadeColor-leave-active {
  transition: all 0.3s;
}

.fadeColor-enter, .fadeColor-leave-to {
  transform: translate3d(0, 100%, 0);
}

.fadeText-enter-active, .fadeText-leave-active {
  transition: all 0.3s;
}

.fadeText-enter, .fadeText-leave-to {
  transform: translate3d(0, 100%, 0);
}

.fadePic-enter-active, .fadePic-leave-active {
  transition: all 0.3s;
}

.fadePic-enter, .fadePic-leave-to {
  transform: translate3d(0, 100%, 0);
}

.fadeImage-enter-active, .fadeImage-leave-active {
  transition: all 0.3s;
}

.fadeImage-enter, .fadeImage-leave-to {
  transform: translate3d(0, 100%, 0);
}

.fadeCategory-enter-active, .fadeCategory-leave-active {
  transition: all 0.3s;
}

.fadeCategory-enter, .fadeCategory-leave-to {
  transform: translate3d(0, 100%, 0);
}

@media (prefers-color-scheme: dark) {
  .phone {
    background-color: #ffffff;
  }
}

.picker-wrap {
  width: 100%;

  .picker-item-color {
    display: inline-block;
    margin-left: 5px;
    width: 16px;
    height: 16px;
  }

  >>> .van-picker {
    padding-bottom: 66px;

    .van-picker__cancel {
      display: none;
    }

    .van-picker__title {
      width: 100%;
      max-width: 100%;
    }

    .van-picker__confirm {
      position: absolute;
      bottom: 15px;
      left: 50%;
      border-radius: 20px;
      color: #ffffff !important;
      transform: translateX(-50%);
      width: 140px;
      background: #0076A5;
      height: 36px;
      line-height: 36px;
      text-align: center;
      z-index: 10001;
    }
  }

  &.has-stock {
    >>>.van-picker__confirm {
      color: #333 !important;
      background: #f4f4f4;
    }
  }
}

>>>.van-hairline--left::after {
  border: 0;
}
</style>

<style scoped lang="stylus" rel="stylesheet/stylus">
$color-bg = #F1F3F5;
$color-main = #256FFF;

// 荣耀
.phone.ry-style {
  background-color: $color-bg;

  .content-wrapper {
    .phone-brand {
      .name {
        border-bottom: 1px solid $color-main;
      }
    }

    .phone-type {
      .name {
        &.border-bottom {
          border-bottom: 1px solid $color-main;
        }
      }
    }
  }

  .picker-wrap {
    >>>.van-picker {
      .van-picker__confirm {
        background: $color-main;
      }
    }
  }

  .nav-wrapper .btn-submit {
    display: flex;
    justify-content: center;
    align-items: center;
    background: $color-main;

    &.diable {
      color: #333;
      background: #f4f4f4;

      .text {
        color: #333;
      }
    }
  }

  .text-wrapper {
    .title {
      .line {
        background: $color-main;
      }
    }

    .btn-box {
      .btn-submit {
        background: $color-main;
      }
    }
  }

  .material-dialog {
    >>>.van-dialog__content .item-content .item-box .text {
      color: $color-main;
    }

    >>> .van-dialog__footer {
      .van-dialog__confirm {
        background: $color-main;
      }
    }
  }

  .msg-dialog {
    >>> .van-dialog__confirm {
      color: #0076A5;
    }
  }
}

.flexible-dialog{
  	position: fixed;
  	top: 0;
  	left: 0;
  	width: 100%;
  	height: 100%;
  	background-color: rgba(0, 0, 0, 0.5);
  	z-index: 1000;
	display: flex;
	justify-content center;
	align-items center;
	.flexible-dialog-content {
	  position: absolute;
	  width: 90%;
	  background-size: 100% 100%;
	  height: 390px;
	  background-color: #ffffff;
	  border-radius: 12px
	  display: flex;
	  align-items: center;
	  justify-content: center;
	  flex-direction: column;
	  .flexible-cotent-top{
		  width: 90%;
		  height: 100px;
	    display: flex;
		  align-items: center;
		  justify-content: center;
		  .flexible-cotent-top-img{
			  width: 100%;
			  height: auto;
		  }
	  }
	  
	  .flexible-cotent-middle-title{
		  width: 85%;
		  height: 50px;
	    display: flex;
		  align-items: center;
		  justify-content: center;
		  margin-top: 20px;
		  font-size: 18px;
		  color: #333333;
		  font-weight: bold;
	  }
	  
	  .flexible-cotent-middle-info{
		  width: 85%;
	    display: flex;
		  justify-content space-between;
		  font-size: 15px;
		  color: #666666;
		  letter-spacing: 0.8px;
		  margin-top: 5px;
		  line-height: 22.5px;
		  text-align: center;
	  }
	  
	  .flexible-cotent-bottom{
		  width: 195px;
		  height: 43px;
		  line-height: 43px;
		  text-align: center;
	    display: flex;
		  justify-content: center;
		  font-size: 18px;
		  color: #ffffff;
		  background-color: #0076A5;
		  border-radius: 21.5px
		  margin-top: 40px;
	  }
	}
}
</style>

<style lang="stylus" rel="stylesheet/stylus">
@import '../../common/styles/mixin.styl';

[v-cloak] {
  display: none !important;
}

.van-overlay {
  background-color: rgba(0, 0, 0, 0.5);
}

.confirm-v-dialog {
  width: 90%;
  border-radius: 8px;

  &.tl {
    .van-dialog__message {
      padding: 12px 24px;
      font-size: 14px;
      color: #666666;
      text-align: left;
      line-height: 24px;
    }

    .van-dialog__header {
      padding-left: 11px;
      line-height: 25px;
      bg-image('remind');
      background-size: 20px 20px;
      background-position: calc(50% - 44px) 20px !important;
    }
  }

  .van-dialog__header {
    padding-top: 18px;
    font-size: 18px;
    color: #4A4A4A;
  }

  .van-button--default {
    font-size: 18px;
    color: #4A4A4A;
  }
}

</style>
