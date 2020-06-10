<template>
  <div
    class="phone"
    id="phone"
    :class="{ 'show-pic': picDialog || mapDialog, noclick: isLoading }"
  >
    <div class="header" ref="headerH">
      <van-icon name="arrow-left" @click="handleBack" />
      <ul class="pic-category">
        <li @click="changeImgPic(1)">
          <span :class="{ active: curPicType === 1 }">自定义</span>
        </li>
        <li @click="changeImgPic(2)">
          <span :class="{ active: curPicType === 2 }">IP图库</span>
        </li>
        <li @click="changeImgPic(3)">
          <span :class="{ active: curPicType === 3 }">模板</span>
        </li>
      </ul>
      <span
        class="submit-btn"
        :class="{
          diable: isModelStockOut || isMaterialStockOut,
        }"
        @click="openSubmit"
        >{{ isModelStockOut || isMaterialStockOut ? "缺货" : "完成" }}</span
      >
    </div>
    <div
      class="content-wrapper"
      :class="isLoading ? 'noclick' : ''"
      @click="handleContent()"
    >
      <!---手机定制面板---->
      <div
        class="phone-wrapper"
        ref="phoneWrapper"
        :style="{ height: wheight + 'px' }"
      >
        <draw-container
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
          :wwidth="wwidth"
          :wheight="wheight"
          :pwidth2="phoneWidth"
          :pheight2="phoneHeight"
          :distributor="distributor"
          :curPicType="curPicType"
        >
        </draw-container>
      </div>

      <!-- 材质/型号 -->
      <div class="m-m-wrap" v-show="showBtnList">
        <div
          class="m-m-icon"
          :class="{ hidden: showMMWrap }"
          @click.stop="showMMWrap = true"
        >
          <span class="sprite-icon icon_replace"></span>
        </div>
        <div class="m-m-content" :class="{ active: showMMWrap }">
          <span
            class="sprite-icon icon_shouhui"
            @click="showMMWrap = false"
          ></span>
          <span class="type" @click.stop="pickerDialog">更换机型</span>
          <span
            class="type"
            v-if="this.materialData.length > 1 || (materialData.length === 1 && materialData[0].children && materialData[0].children.length > 1)"
            @click.stop="materialDialog"
            >更换材质</span
          >
        </div>
      </div>

      <!-- 开始定制 -->
      <div class="custom-b-wrap" ref="customBtnWrap">
        <!-- <span class="sprite-icon start_button_star" v-show="!showBtnList" @click="showBtnList = true"><i>开始定制</i></span> -->

        <!-- 机型/材质 -->
        <div class="current-mm" v-show="showBtnList">
          <span>{{ brand }}</span>
          <span>{{ materialsName + "-" + colorName }}</span>
        </div>

        <!-- 打印效果 -->
        <ul
          class="print-result"
          :class="[getDpiValue(dpiValue), { active: showBtnList }]"
        >
          <li class="print-info" :class="{ 'cur-print1': dpiLevel === 1 }">
            <p class="intro" v-show="dpiLevel === 1">{{ dpiDesc }}</p>
            <span class="circle"></span>
            <p class="info">模糊</p>
          </li>
          <li class="print-info" :class="{ 'cur-print2': dpiLevel === 2 }">
            <p class="intro" v-show="dpiLevel === 2">{{ dpiDesc }}</p>
            <span class="circle"></span>
            <p class="info">一般</p>
          </li>
          <li class="print-info" :class="{ 'cur-print3': dpiLevel === 3 }">
            <p class="intro" v-show="dpiLevel === 3">{{ dpiDesc }}</p>
            <span class="circle"></span>
            <p class="info">清晰</p>
          </li>
        </ul>

        <!-- 操作按钮 - 素材/文字等 -->
        <ul class="btn-list" :class="{ active: showBtnList }">
          <!-- canva -->
          <li
            class="btn-item"
            @click="handleCanva"
            v-if="platform === '7' && smVersion > 23 && curPicType === 1"
          >
            <span class="sprite-icon customized_button_canva"></span>
          </li>
          <!-- 素材/模板 -->
          <li
            class="btn-item"
            v-show="curPicType !== 2"
            @click="openPic(curPicType)"
          >
            <span
              class="sprite-icon"
              :class="
                Number(curPicType) === 3
                  ? 'customized_button_Template'
                  : 'customized_button_material'
              "
            ></span>
          </li>
          <!-- 文字 -->
          <li class="btn-item" @click="openText" v-show="curPicType !== 2 && Number(materialsId) !== 86">
            <span class="sprite-icon customized_button_word"></span>
          </li>
          <!-- 上传 -->
          <li
            class="btn-item"
            @click.prevent="chooseImg()"
            v-show="curPicType !== 2"
          >
            <span class="sprite-icon customized_button_upload"> </span>
            <input
              type="file"
              style="display: none; height: 0; width: 0"
              accept="image/*"
              id="picfile"
              ref="file"
              @change="picUpload($event)"
            />
          </li>
          <!-- 贴图 -->
          <li class="btn-item" @click="openMap(4)" v-show="curPicType !== 2">
            <span class="sprite-icon customized_button_Mapping"></span>
          </li>
        </ul>
      </div>
    </div>

    <!---添加文字---->
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
          placeholder="在此输入文字内容"
          @focus="focusInput"
          clearable
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
    <!---素材图库---->
    <div class="piclist-dialog" v-show="picDialog">
      <div class="hasMore" v-show="showMoreAnimate">
        <span
          class="sprite-icon arrow_left"
          :class="{ animate: showMoreAnimate }"
        ></span>
        <span
          class="sprite-icon arrow_right"
          :class="{ animate: showMoreAnimate }"
        ></span>
      </div>

      <c-picture
        ref="picList"
        v-show="picData"
        :topValue="headerH"
        :picData="picData"
        @change="change"
      ></c-picture>
    </div>
    <!---贴图---->
    <div class="piclist-dialog" v-show="mapDialog">
      <c-picture
        ref="mapList"
        v-show="mapData"
        :topValue="headerH"
        :picData="mapData"
        @change="change"
      ></c-picture>
    </div>
    <!---选择手机型号弹出框---->
    <van-popup
      v-model="showModelPicker"
      round
      class="picker-wrap"
      :class="{ 'has-stock': isModelStockOut }"
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
    <!---确认弹出框---->
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
    <van-popup
      v-model="showPicker"
      round
      class="picker-wrap"
      :class="{ 'has-stock': isMaterialStockOut }"
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
    <Loading v-show="isLoading" :message="message"></Loading>
  </div>
</template>
<script type="text/ecmascript-6">
import Loading from "base/loading/loading";
import { Slider } from "vue-color";
import { mapState } from "vuex";
import MobileDetect from "common/js/lib/mobile-detect.min.js";
import { getRenderer } from "common/js/lib/renderer.min.js";
import { getModel } from "common/js/iphone-device.js";
import api from "common/js/allApi.js";
import { getClientOsVersion } from "common/js/saApi";
import CPicture from "../components/cPicture.vue";
import DrawContainer from "../components/drawContainer.vue";
import { getLocalStorageItem } from "common/js/common";

// 使用Vue.mixin的方法拦截了路由离开事件，并在该拦截方法中实现了销毁页面缓存的功能
Vue.mixin({
  beforeRouteLeave: function (to, from, next) {
    if (to.path === "/previewer" || to.path === "/template") {
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
  },
});

export default {
  name: "custom",
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
      isPic: false,
      isFlag: 0,
      userNo: "",
      dialogVisible: false,
      platform: "", // 平台
      distributorId: 0, // 分销商ID
      distributor: "", // 分销商名字
      inputtext: "", // 添加文字
      fontFamily: [], // 字体列表
      colors: {
        hex: "#194d33",
        hsl: {
          h: 150,
          s: 0.5,
          l: 0.2,
          a: 1,
        },
        hsv: {
          h: 150,
          s: 0.66,
          v: 0.3,
          a: 1,
        },
        rgba: {
          r: 25,
          g: 77,
          b: 51,
          a: 1,
        },
        a: 1,
      },
      swatches: ["100", ".80", ".65", ".50", ".35", ".20", "0"], // 颜色板
      family: "", // 当前字体
      fontColor: "",
      textDialog: false, // 文字弹框
      picDialog: false, // 图库弹框
      mapDialog: false, // 贴图弹框
      uploadDialog: false, // 上传图片弹框
      submitDialogVisible: false, // 确认弹框
      picData: [], // 图库列表
      mapData: [], // 贴图列表
      picname: false, // 是否显示图片名称
      loading: false,
      isData: true, // 检难数据是否完善
      count: 30,
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
      showModelPicker: false,
      showPicker: false,
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
      wwidth: 0, // 画布宽
      wheight: 0, // 画布高
      headerH: 0, // 顶部栏高
      index: null, // 当前选中图层
      mw: 0,
      mh: 0,
      nw: 0, // 未加宽的手机尺寸
      nh: 0, // 未加宽的手机尺寸
      pscale: 0,
      dpiValue: 0, // 图片dpi值
      dpiDesc: "", // dpi描述
      dpiRate: 0, // dpi评分
      dpiLevel: 0, // dpi等级
      dpiFlag: false,
      ratio: 1, // 缩放比
      skuNo: 0, // skuNo
      skuName: "",
      message: "", // loading提示
      error: "", // 错误提示信息
      delIcon:
        "data:image/png;base64,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",
      scaleIcon:
        "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAYAAADDPmHLAAAAAXNSR0IArs4c6QAAEO9JREFUeAHtXX10FNUVv29mN8kmJNEIUitJTNCgRdp60BYO2iJHRa1fRa1FKiqo1KqAqP1DWkVbaE/Br6JVLKBiLdpWxR4PrcpBPOqRWjm29YCESmIS8KNqlHxtkt2Z13sn7DG72Zl5szufuzN/ZOfjzn33/n438968jzsA4RYiECIQIhAiECIQIhAiECIQIhAiUFwIsEJ0l0+eHB3o7GxUIDmBq3wCAJvAOYzjjFcyzio5g0rguM9YJfnPOe8GxroZh26UwV/cZ7APrzQziTXLEGkuralpYTt2JAoNr4IIgN76+iM4KDOQ6FNV4CcjkeM5QMROshCoJAbOXgnYaxgYLzOQt1a0tX1oZxle6ApkAPDp0yN9ra0zVVDPRjJm4H/3sZ6Ax2A3Bt1WCaTN5Q0NL7Bt25Je2JFPmYEKgN66usmqpM4FDrOR9DH5OG73vVhlfAIMNkqqtKGivX2H3fqd0uf7AOCNjdW9yuA1HNgVWFd/zSkg7NSLbYtdDPijFXLJw6yl5YCduu3W5dsA6GpqGs0G4ouwLr8BW2nVdjvuij7GDiDAq3lp7L6qPXs+daVMi4X4LgCoQaeAejOSvgB9qbDoj1/Fe/EtY40M0iq/NRx9EwB84sSSnp6uW7B+X8qBx/zKZD52MWBxbCcsHzWqaiXbuXMwH1123euLAOipqztNZeoDSH6TXY75Wg+DZolL149qb9/itZ2eBkDvhNqvqnG4G//jL/EaCC/KxyfCU1IMllQ0d3zgRflUpmcBgK905yjAH8P3+BqvnPdHuaxTBnY5vjo+74U9ktuFUjdtV33tSuzE+WtIPqHPawgLwoSwcZsPV58A8cbGuoQy+BTW9VPcdjQQ5THYHpVLLom1tLS7Za9rAdDVUHsKKLAp/K83o5Z1ggwXVLV2vGomacd1V6qArqNqz2cqvBiSL0IZryGsCDMR6XxlHA+Arvpx85nKn8Zu3LJ8jS2W+wkrwoywc9pnRwMAGza3Yn2/FrtzZacdKTT9GmaInYahg8451gYYIp8vd9D24lHN2NKqto4VTjjsSABojy6MXicMLlqdDK6qatu3zm7/bQ+AoQYf1vnhY99WrpAohUvswqr3O56zU7GtAUCvetSCDRt8dlL0pS6cZ9DPJTjDzldE2wJA6+RJJt4OX/W+JMyZPdYZjURPsKuzyJa3AOrC1Hr4ir5f3xnK07XyGsLarm5jWwKg+9P/rQi7d9NpcvQIu9I1zG0oJO8qgEb1aDADG31567LBn6JRgWBjc0A6L99RxLxIo/F8JQ7vhPW+V3GHQ8kxmJTPfIK8qgCazBGS7xX5VC4OJWsc5G5Dzk8AbRoXqC/lXnR4p10IYFVweq7Ty3IKAJrA2d1z4B0n5/BFLvg+lMybB9KxxwErLbULqxF61I8/AmX7dhhYsRz4Rx+NuB6IEwz2VI6qnpTLRNOcAqC7vnYpdvb80ilwShYthtIlNzmlPqte/vnn0DvzDOAYEEHcsJPoZ5VtHZbHXiwHAM3bV7m616mp22zcOKh45VVgEVvXdgpxmtj0LPQvWigk6zchmnIuMWm81XUHlhuBtGjDKfIJVPnEkzwhXyt76lS/8SpsD3GiLagRvmNI0FIA0HKtgyt2LBYjLs7GjhUXDiXTEcDVVBpH6WcNjywFAK3VQ22FslxrBDDKG2+MOBewExUHORI2WzgAaJUu9vbdIKw5YILUCBxY4cicC1eRII6IK9FChQOAlmgHdpWuARrqxx9D4rlN0HtmcN8A0tzDldQaV2kn9Q+Em9q0Ph97nvQ1eXxlYOVvYPD+1R5b4Y/ih7iClSLWCD0BKDMHvvf7OjlD6S0/heilc0R8LngZ4oo4E3FUKAC0tCwi2jyWKV2+AiLnnOuxFf4oXpQz0wCghEz45J/tD7eMrWCSBGX33gfyd6cbCxbDVcqjRNyZbKYBQNm4/JaQycgnFo1CbM3D2KF0opFYwV8jzog7M0dNA2AoFZuZGveuqx9+aFoYi8Ugtv5RkI47zlS2kAVEuDMNAGz5z/ATSMm/bQZl505Tk1h1NcQefwJY/VGmsoUrYM6dYQBoGTg9SsKoS8rgIMQvmwNqa6uuSOqCNGYMlD/xR2Bjv5I6VVS/lECTODRy2jAAhtKvGt3uzTX+2WfQN2c2iFQHUm0txP7wBMAhh3hjrMelmnFoGACUe9dj+3WL5/v3Q/xHl4La2akrk7ogNzVB+YbHAcrLU6eK6NeYQ8MAoMTLfkZKfe89iM+9DHhPj6mZ8je+CbG16wFKSkxlC0nAjEPdAKCFB5R12+9gqO/8B+JXzQPe329qamTaNCi7/wEA7C8olk3LnG6Qe0gXCcq3jz3/ph0JfgCShnH7r78OeNI8WXd05plQtnKVH8x2xQbikLjUK0w3AOhjC3o3+fF88qUXof8WyjBrPmAVvehiKL3tdj+64YhNRlzqB4DCPcnBnw8CyWeehoE7lgmpKJl/FdDk02LYhr6akt1T3QDA5MaBegKk3Es8sh4G7sH1KgIbzTyOzr1cQDLYIiow3X9m3QDAxsO4oLo9eO89MIiBILKV3vkLoDUIhbwZcakbAPSBpSCDMrDsdkg884ypCzifHsruuhvkGTNMZYMqYMSlbgDgl7NGeeKwSUueq6qwWf03L4HkFvPVa7QGIfbgQyB/69vCuoMkaMSlbgBon1bzwEvq3DHa1P/uMbqcfk1RIP6TayG5/Y3081mOWBmNID4C0sSJWa4G+5QRl7oBgO9TnlQBypv/AHXfvqyI08xdZdu2rNd0Tw4MQHz+PFCww8hsY5WVQyOIDQ1moqbX5dNOh9i69VC+ZSuUPYTzE6Z4mB7ZgEvdAMC60ZMAgHgc4guvB/WTT9JA5gcOQHzxQuACff9pN9IBdhVTl7G6d++IS5knpMMOwxHEjcCOMBxEy7wt7bhk4SIoR/IjGATyMcdA9KyzIPbknzybs2jEpe7aQFwAOoCdKp51nLNDD4XIueeBdPTR2tBvcvPmvBduEqnlTz8L0pFHphGW7YCCpe+iWZYDjuwtf3ELMHlkclSOwd17yjTgGcGdrXw7z2EADOLC0dJsOnWfAEh+d7Yb3DpHj/vEhsdg4LafA73b27Fql+NsIm0YGYeTzTZp/HiI0QjiKGttYXnayVnJp/JoppJ80klmRdt+3YhL3QCgb+nabokPFHKcSEITSni3uXvypK9r9ThYyE9g9nRh42rdR8GAS90AwM4Dc4Tcd8WWElWcUhafdyWOIMZN9UWmTIXY7x7EZcsjH+mmN/tEwIhL3QCgr2j7xH5HzKC3jfi1PxYaQaTGXNkqse5lR4zNU6kRl7oBQJ9Qz7Nc39+ubN0K/TctERtBnDULSpfd4XufshloxKVuAGDnwf5sygrtXBKzglBDU2QruXIelNy4RETUVzLY2529YwWt1A0ACfhuX3nhoDHa28YqobWUULr4RohiIARr48169uoGAJOY7k16yoJ8fnD1b2Fw3VohF0pvXwaRWRcKyfpByIhL3QCQIVJUAUBEDdx5ByT+8mdTzrQRRJxWFjn9DFNZPwgYcakbAKU1NS3YTWg+yc4PHtpoA00rS7zwd1ONNIJIE0xlnyeWIg6JSz2HdAOA7diRwIageee5nuagnsfhZppgmnz9dVMPWFmZNtVcwg4jv27EIXGpZ59uANANErDX9G4s6PO0/Aynmiv//pepmwy7iqnLmMYA/LiZcWgYALgw9GU/OuWKTX190IcjiMoe8/kHUk0NLj/DNYgCg0yu2J5WiDGHhgHAQN6apqvYDr74Apef4ULUjg5TzyUaacRhZMABHz9tZhwaBgClHcVOhKLpD8hGHI1C9s3BNYgCQ7gSTiSJnP29bGo8OUfcmaWONQyAIatZcT8FEATe9v7QCCJOSjHbpNGjzURcvG7OnWkAYC76zS5a7Nui1HffxRHEK4AmdQRlE+HONADKGxpewEdJ+vysoCBgs53KW29BfAHmy0zovlXZXGLu6ogz4s5Mg2kAsG3bkvg5KGzdhBshoLyyDfoXLwIr09M9QQ4507gzKTxicl27LKnSBkxFvlBENlAy+G8iNTZiCpmxlszmnZ9BYt06KLn6akv3uSlMnImUJxQA+GmyHThJdBfOLfN1tlARh1MyNG277Fe/Bunww1OnCuYXxyp2VbS17xBxyLQKSClhwB9N7Qf9lz5KEfv92oIkn7ixwpVwAFTIJQ/jRFHz96AAREfJTTcDZRUtyA050rgSdE4YBdbScgBHllYL6vW1mHz88d7Zh4NNTm7EEXElWoZwAJBCXhq7D396RZX7Vc7thRnDcRDJbzhc3uJ+70GOhG+zFABVe/Z8itXAGmHtPhUUGe93wnQVF6Qo/3zTCdVDOpEbjSMLJVgKANIrg7SKPlFmoQzfiVICiaSTRGTxmOPoIi1Xh66uLFfzP0WcEDdWNWGVYX1z+sOR1i3K4Q5sBEYv/gGu2p1quR/AUmm4OlnZ/S4kNm4E3t5m6VYrwvjq586HI8koNz4da8X5opfN49OxlqsAApu+UStx6bqiB94nABAXuXw3mMzPKQDoRvpaNdY7T9F+uHmHAHGQ65fDyeqcA0C7OQbYqmGd3rlf7CWzTknjIHcc8gqAiuaOD2Rgl2NL0jw9Z+42hndmQYAwJ+yJgyyXhU/lFQBUCg4UPc8Zu0u4xFDQFgQIc8I+X2V5BwAZUDn68FtxBGJ7vsaE9wsigFhrmAuKG4nl1A+QTWG8sbEukUy8jU+mmmzXw3N2IcA6o5HoCbGWlnY7NNryBCBDNINkuAA7JPrtMCzUMRIBDVvE2C7yqQTbAoCUVbV2vIpLkX6IjxWFjsPNPgQIU8KWMLZPq80BQIZVvd/xHBq6wE4jQ11YsSKmhK3dWNj6BEgZV9W2bx2OGi5NHYe/eSKAWGqY5qkm2+22NQKzKe+qr70VE/Asz3YtPCeIgEZ+xwpBactijgYAWdNVP24+pilbgz1Fwc2zZhnW/G84WOcvcOo/P2Wh4wFABXUdVXs+BsGTOKu4LFVw+KuPALX2tQafA3V+ZqmuBAAV2tVQewq+G2wK+wkyKcg8xrEVfNWzu7WfWUrq2JFGYEr58F9yiDowwh7D4ahk7GMPH2HkFvlUumsBQIVRB0bl6LHfwTcEnFYWDiARJrRpWCAmhI2dnTxD2o3/ulYFZJrRW1d3jgL8sbBKYJ3aqJ4NAzuZGIscu/oEGG4QjWTJMZhUzJNKyHfCwI5RveHYWtn37Akw3MieurrTVKY+gJVC0/DzBbuPc/hoGlc+M3nswsazJ8BwBwiIylHVk2hmK/5XBHrK+XC/MvfJN/KRfPUD+WSfL54Aw4Hqra8/Apei00eAaTyhYvi1AO/30oIamrdvlrPHbR99FwApALqamkazgfgi7EG8AYOhOnU+UL+4UBMBXk3Ltayu2HHLT98GQAoA3thY3asMXsOBXRGU/AT4mN9FS7Rpla6VhZopn9389X0ADAcDXx0nq5I6FxuLs/Er8WOGX/N6X8ujhGlZKDMHtuqFkjN4bTOVH6gASAHGp0+P9LW2zlRBPRv7EWZgMOh+HTt1jxO/SDrmUGRbKRuXlkyL8ikFbAtkAGRiTA1HDsoMJONUFfjJOPA0HtsOQulvMnXpHSNQSRyg2TuUe5e/TBk4/dag07Pd6HxBBECmg3zy5OhAZ2ejAskJisKPxRb4BAyKI/HjSVX0IWUkshIblpVYV1fSvdp39fDTaijTjTI9KNOFMvvxQrMss92Ub19Ln2+QdTvThvA4RCBEIEQgRCBEIEQgRCBEIEQgRCBEwJ8I/B+JlWlX4LdY9gAAAABJRU5ErkJggg==",
      exportUrl: "", // Canva 图片
      designId: "", // Canva 图片id
      smVersion: "",
      curPicType: 2, // 当前素材分类 - 1 自定义 2 IP图库 3 模板
      showBtnList: true, // 操作按钮 - 素材/文字等
      showMMWrap: true, // 材质/型号
      comingFlag: this.$route.query.comingFlag, // 跳转来源
      isClicked: 2, // 用于标识自定义/IP是否点击
      firstEnter: 0,
      showMoreAnimate: false,
      isModelStockOut: false, //是否缺货（型号）
      isMaterialStockOut: false, //是否缺货（材质）
      uploadValid: 1, // 是否允许用户上传（0 不允许，1 允许）
      copyrightCost: 0, // 图片版权费
    };
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      if (from.name === "preview" || from.name === "previewer") {
        vm.picDialog = false;
        vm.mapDialog = false;
        vm.message = "";
        vm.isLoading = false;
      }
      vm._getQueryData();
    });
  },
  created() {
    this.userNo = getLocalStorageItem("userNo") || "b2bsamsung";
    this.platform = localStorage.getItem("platform");
    this.distributorId = localStorage.getItem("distributorId");
    if (this.platform === "7") {
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

    // 存储当前类型以及是否第一次点击
    sessionStorage.setItem("curPicType", this.curPicType);
    sessionStorage.setItem("curPicClicked", "");

    // 记录数据埋点
    let hasRecord = sessionStorage.getItem("hasRecord");
    if (!hasRecord) {
      this.handleRecordData();
    }
  },
  mounted() {
    // 画布宽高
    this.getDrawSize();

    if (this.comingFlag !== undefined && this.comingFlag !== null && this.comingFlag !== "null" && this.comingFlag !== "" && this.comingFlag && this.comingFlag === "mmPage") {
      this.firstEnter = 1;
      // 获取图片id
      if (sessionStorage.getItem("imageInfo")) {
        let imageInfo = JSON.parse(sessionStorage.getItem("imageInfo"));
        this.picId = imageInfo.pictureId;
      }
      // 获取型号id/型号名称
      if (sessionStorage.getItem("modelInfo")) {
        let modelInfo = JSON.parse(sessionStorage.getItem("modelInfo"));
        this.mobile = modelInfo.name;
        this.modelId = modelInfo.modelId;
      }
      // 获取材质id
      if (sessionStorage.getItem("materialInfo")) {
        let materialInfo = JSON.parse(sessionStorage.getItem("materialInfo"));
        this.materialsId = materialInfo.item.materialId;
      }
    }

    if (this.$route.query.key === "-1") {
      this.message = "";
      this.wallpImg = "";
      this.isLoading = false;
      this.reset();
    } else if (
      this.$route.query.pid !== "" &&
      this.$route.query.pid !== undefined
    ) {
      this.reset();
    } else {
      this.message = "";
      this.isLoading = false;
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
      var headerH = parseInt(
        window.getComputedStyle(this.$refs.headerH).height
      );
      var customBtnH = parseInt(
        window.getComputedStyle(this.$refs.customBtnWrap).height
      );
      let sHeight = `${document.documentElement.clientHeight}`;
      let H = Number(sHeight) - customBtnH - 20;
      this.headerH = headerH;
      this.wwidth = window.screen.width;
      this.wheight = H;
    },
    initCanva(userNo) {
      let _self = this;
      var apiKey = "";
      var host = window.location.host;
      if (host === "diy.bat.com") {
        apiKey = "kYGQOtqClWGpGOwOZXZXf4mS"; // 正式
      } else {
        apiKey = "rr-29uUyTyZMOS8TJm_5wqWQ"; // 测试
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
      this.message = "载入中";
      this.isLoading = true;
      // 判断是否有图片id
      let pid = this.$route.query.pid;
      let url = this.$route.query.url;
      let type = this.$route.query.type;
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
      if (this.comingFlag !== undefined && this.comingFlag !== null && this.comingFlag !== "null" && this.comingFlag !== "" && this.comingFlag && this.comingFlag === "mmPage") {
        this.getBrand();
      } else {
        this.getMobileDevice();
      }
    },
    // 自动获取机型
    getMobileDevice() {
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
        this.getBrand();
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
            this.getBrand();
          });
        }
      }
    },

    // 获取 Android 手机型号
    getAndroidMobile(networkModel) {
      return new Promise((resolve, reject) => {
        this.$api
          .get(this, api.getModelByNetwork, {
            networkModel: networkModel,
          })
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
      if (window.screen.width > 500) {
        this.$refs.textWrapper.style.height = 350 + "px";
      }
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
    // 升序
    dataUp(x, y) {
      return x.sequence - y.sequence;
    },
    // 获取所有品牌型号
    getBrand() {
      this.$api
        .get(this, api.getModelList, {
          categoryId: 1, // 产品类型id：1 手机壳
          distributorId: this.distributorId,
          materialId: this.materialsId,
          pictureId: this.picId,
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
            } else {
              this.message = "";
              this.isLoading = false;
              this.$toast.fail("暂无关联的型号数据");
            }
          }
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
        } catch (e) {
          this.message = "";
          this.isLoading = false;
        }
      }
    },
    // 根据型号获取材质列表
    getMaterialList() {
      let flag = false;
      this.$api
        .get(this, api.getMaterialList, {
          pictureId: this.picId === 0 ? "" : this.picId,
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
              this.materialData = [];
              // picker：数据处理
              dataArr.forEach((item, k) => {
                if (item.childrenList) {
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

              // 判断材质列表第一项是否缺货
              if (dataArr[0].childrenList[0].underStockFlag) {
                // 是
                this.isMaterialStockOut = true;
              } else {
                // 否
                this.isMaterialStockOut = false;
              }

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
                  this.materialsColorValue = dataArr[0].childrenList[0].colour;
                  this.manufactor = dataArr[0].childrenList[0].manufactor;

                  // 获取是否允许用户上传
                  this.uploadValid = dataArr[0].childrenList[0].allowUploadFlag;
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
          }
        })
        .catch((err) => {
          console.log(err);
          this.message = "";
          this.isLoading = false;
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
    getPicList(curPicType) {
      if (
        this.comingFlag !== undefined && this.comingFlag !== null && this.comingFlag !== "" && this.comingFlag &&
        this.comingFlag === "mmPage" &&
        this.isClicked === 2
      ) {
        this.curPicType = 2;
      }

      this.$api
        .get(this, api.getPictureList, {
          distributorId: this.distributorId,
          materialId: this.materialsId,
          materialNo: this.materialNo,
          modelId: this.modelId,
          productCategoryId: 1, // 产品类型id：1 手机壳
          type: curPicType, // 图片类型：1 为普通素材，2.IP素材，3、模板，4、贴图（默认不传，查全部）
        })
        .then((res) => {
          if (res.success) {
            this.message = "";
            this.isLoading = false;
            if (curPicType === 4) {
              this.mapData = res.data;
            } else {
              this.picData = res.data;
            }

            if (this.curPicType === 2 && !this.picDialog) {
              // 图库箭头动画
              if (!this.showMoreAnimate) {
                this.showMoreAnimate = true;
                setTimeout(() => {
                  this.showMoreAnimate = false;
                }, 1600);
              }

              this.picDialog = true;
              sessionStorage.setItem("curPicClicked", 1);
            }

            if (res.data.length > 0) {
              let flag = this.checkPic();
              if (flag === false && this.picId !== 0) {
                if (curPicType === 2) {
                  this.$toast.fail("当前图片不可用");
                }
                this.picId = 0;
                this.picName = "";
                this.imgData.posX = null;
                this.wallpImg = "";
                this.reset();
              } else {
                if (
                  this.firstEnter === 1 &&
                  sessionStorage.getItem("imageInfo") &&
                  this.curPicType === 2
                ) {
                  // 获取从推荐页进入传递的图片信息
                  let imageInfo = JSON.parse(
                    sessionStorage.getItem("imageInfo")
                  );
                  this.changeImage(
                    {},
                    imageInfo.originImage,
                    imageInfo.pictureId,
                    this.curPicType
                  );
                } else {
                  if (this.isPic) {
                    let url = this.$route.query.url;
                    let pid = this.$route.query.pid;
                    let type = this.$route.query.type;
                    this.changeImage({}, url, pid, type);
                  } else {
                    if (this.curPicType === 2) {
                      // IP图直接替换
                      this.changeImage(
                        {},
                        this.wallpImg,
                        this.picId,
                        this.curPicType
                      );
                    } else {
                      // 自定义/模板重绘
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
                      this.$refs.mcanvas.draw();
                      this.$refs.phoneWrapper.dispatchEvent(
                        new MouseEvent("click")
                      );
                    }
                  }
                }
              }
            } else {
              this.wwidth = window.screen.width;
              this.picDialog = false;
              this.mapDialog = false;
              this.$refs.mcanvas.dragArr.forEach((item, index) => {
                if (item.picId !== 0) {
                  this.$refs.mcanvas.dragArr.splice(index, 1);
                } else {
                  // 判断是否允许用户上传
                  if (Number(this.uploadValid) !== 1) {
                    // 否，清空已上传图片
                    this.$refs.mcanvas.dragArr.splice(index, 1);
                  }
                }
              });
              this.$refs.mcanvas.initCanvas();
              this.$refs.mcanvas.draw();
              this.$refs.phoneWrapper.dispatchEvent(new MouseEvent("click"));
            }
          } else {
            this.$toast(res.errMessage);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    // 根据手机型号和材质获取手机定制信息
    getInfo() {
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
              this.phonenHeight = this.wheight * 0.75;
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
                this.phoneHeight = this.wheight * 0.75 + this.pm2px(2);
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
                this.phoneHeight = this.wheight * 0.75;
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

              if (this.materialsId !== 0) {
                this.getPicList(this.curPicType);
                this.getPrice();
              }
            }
          } else {
            this.message = "";
            this.isLoading = false;
            this.isData = false;
            this.$toast(res.msg);
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
        this.dpiDesc = "可打印";
        this.dpiLevel = 3;
        return "cur-print";
      } else if (this.dpiRate > 2 && this.dpiRate < 5) {
        this.dpiDesc = "画质一般";
        this.dpiLevel = 2;
        return "cur-print";
      } else if (this.dpiRate > 0 && this.dpiRate <= 2) {
        this.dpiDesc = "无法使用";
        this.dpiLevel = 1;
        return "cur-print";
      }
    },
    getScale(width, height) {
      return height / this.phoneHeight > width / this.phoneWidth
        ? width / this.phoneWidth
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
    // 图库
    change(item) {
      this.mask = false;

      // 图片版权费
      this.copyrightCost = 0;
      if (item.copyrightCost) {
        this.copyrightCost = item.copyrightCost;
      }
      // type 1-普通图片 2-IP图片（不可编辑） 3-模板 4-贴图
      if (this.isPrice) {
        this.imgData.posX = null;
        this.message = "载入中";
        this.isLoading = true;
        if (item.type === 4) {
          // 贴图
          this.getBrand();
          this.changeImage(item, item.originImage, item.id, item.type, 2);
        } else {
          this.picId = item.id;
          this.picName = item.name;
          // 素材/模板
          this.getBrand();
          this.changeImage(item, item.originImage, item.id, item.type);
        }
      }
    },
    changeImage(item, url, id, type, cateType) {
      // cateType：1（支持多次上传）例：本地图片，2 贴图
      this.isPic = false;
      if (this.materialsId !== 0) {
        this.wallpImg = url;
        if (type !== 4) {
          // 非贴图
          this.picId = id;
        }
        if (type !== 1 || type !== 2) {
          this.picName = "";
        }
        // type 0-Canva 1-普通素材 2-IP图片（不可更改） 3-模板（不可更改） 4-贴图
        this.picType = type;
        Promise.all([this.loadImage(url, type)]).then((img) => {
          let w = img[0].width;
          let h = img[0].height;
          let diffX = 0;
          let diffY = 0;
          let scale = this.getScale(w, h);
          let centerX, centeruX;
          let centerY, centeruY;
          this.imgData.initW = w / scale;
          this.imgData.initH = h / scale;

          if (
            this.imgData.posX === null ||
            this.picType === 2 ||
            this.picType === 3
          ) {
            // 画布中心点
            centerX = window.screen.width / 2;
            centerY = this.wheight / 2;
            this.imgData.sizeW = w / scale;
            this.imgData.sizeH = h / scale;
            this.imgData.posX = centerX - w / scale / 2;
            this.imgData.posY = centerY - h / scale / 2;
            // 手机中心点
            centeruX = this.phoneWidth / 2;
            centeruY = this.phoneHeight / 2;
            diffX = (this.phoneWidth - this.phonenWidth) / 2;
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

          // 模板 - 计算点击热区
          let hotClick = [];
          if (type === 3 && item !== {}) {
            if (item.templateEditList && item.templateEditList.length > 0) {
              item.templateEditList.forEach((edit, index) => {
                // 计算热区到中心点间距（mm）
                let distanceX =
                  Number(edit.editCenterX) -
                  Number(item.templateCenterX) -
                  Number(edit.width) / 2;
                let distanceY =
                  Number(edit.editCenterY) -
                  Number(item.templateCenterY) -
                  Number(edit.length) / 2;
                let hotPoint = {
                  hotX:
                    ((w / (Number(item.templateCenterX) * 2)) * distanceX) /
                    scale, // x轴
                  hotY:
                    ((h / (Number(item.templateCenterY) * 2)) * distanceY) /
                    scale, // y轴
                  hotW:
                    ((w / (Number(item.templateCenterX) * 2)) *
                      Number(edit.width)) /
                    scale, // 宽
                  hotH:
                    ((h / (Number(item.templateCenterY) * 2)) *
                      Number(edit.length)) /
                    scale, // 高
                  frame: edit.internalEditUrl, // 热区框图
                };
                hotClick.push(hotPoint);
              });
            }
          }

          // 贴图
          if (type === 4) {
            this.imgData.posX = window.screen.width / 2 - 50;
            this.imgData.posY = this.wheight / 2 - 50;
            this.imgData.posuX = this.phonenWidth / 2 - 50;
            this.imgData.posuY = this.phonenHeight / 2 - 50;
            this.imgData.sizeW = 45;
            this.imgData.sizeH = 45;
            this.imgData.rotate = 0;
          }

          let imgObj = {
            x: this.imgData.posX,
            y: this.imgData.posY,
            ux: this.imgData.posuX,
            uy: this.imgData.posuY,
            url: img[0],
            width: this.imgData.sizeW,
            height: this.imgData.sizeH,
            initW: this.imgData.initW,
            initH: this.imgData.initH,
            // 最小宽高
            minW: type !== 4 ? this.imgData.sizeW : 20,
            minH:
              type !== 4
                ? this.imgData.sizeH
                : 20 / (this.imgData.sizeW / this.imgData.sizeH),
            type: 1,
            picType: this.picType,
            rotate: this.imgData.rotate,
            selected: true,
            index: null,
            cateType: cateType ? cateType : "",
            picId: this.picId ? this.picId : 0,
            hotClick: hotClick,
            scale: scale,
          };

          this.spriteArr = [];
          this.spriteArr.push(imgObj);
          this.isLoading = false;
        });
      }
    },
    // 加载图片
    loadImage(url, type) {
      let img = new Image();
      img.crossOrigin = "anonymous";
      if (url.indexOf("data:") === 0 || type === 0) {
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
          picType: this.curPicType,
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

        this.$refs.mcanvas.draw();  // 重绘，以免删减文字错位
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

      if (hasPic) {
        if (this.isData) {
          if (this.skuNo === "0" || this.skuNo === 0 || this.skuNo === "") {
            this.$toast.fail("信息异常, 请稍后重试！");
            return;
          }
          this.message = "正在生成预览";
          this.isLoading = true;
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
        if (Number(this.curPicType) === 1) {
          this.$toast("请先选择素材！");
        } else if (Number(this.curPicType) === 3) {
          this.$toast("请先选择模板！");
        }
      }
    },
    // 定制成功并跳转
    submit(image, generateImage) {
      // 保存定制数据
      let materialsValue = this.colorName
        ? this.materialsName + " - " + this.colorName
        : this.materialsName;
      var enterParams = JSON.stringify({
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
        skuName: encodeURI(this.skuName),
        manufactor: this.manufactor,
        materialsType: this.materialsType,
        designId: this.designId,
      });
      // 隐藏图层列表
      this.$refs.mcanvas.showHandleWrap = false;
      // 跳转预览
      this.$router.push({
        path: "/previewer",
        query: {
          enterParams: enterParams,
        },
      });
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
        this.uploadDialog = false;
        this.mask = false;
        this.wallpImg = reader.result;
        this.imgData.posX = null;
        // id： 0-网络图片 1-普通图片 type: 1-普通素材 2-IP图 3-模板 4-贴图
        this.changeImage({}, this.wallpImg, 0, this.curPicType, 1);
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
        reader.readAsDataURL(file);
      }
      let isLt10M = file.size / 1024 / 1024 < 10;
      if (!isLt10M) {
        this.$toast.fail("上传图片大小不能超过 10MB!");

        this.message = "";
        this.isLoading = false;
        return;
      }
      return reader;
    },
    pickerDialog() {
      this.showModelPicker = true;
      this.showMMWrap = false;
      this.getBrand();
    },
    materialDialog() {
      if (this.materialData.length > 1 || (this.materialData.length === 1 && this.materialData[0].children && this.materialData[0].children.length > 1)) {
        this.showPicker = true;
        this.showMMWrap = false;
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
        this.getMaterialList();
        this.$refs.phoneWrapper.dispatchEvent(new MouseEvent("click"));

        // 存储已选机型，用于记录最近使用
        localStorage.setItem("usedItem", arr[1].value);
      }
    },
    handleMaterialConfirm(arr) {
      // 判断是否缺货
      if (!this.isMaterialStockOut) {
        // 自定义模式下，编辑器有内容时，材质切换
        if (
          this.curPicType === 1 &&
          arr[1].id !== this.materialsId &&
          this.$refs.mcanvas.dragArr &&
          this.$refs.mcanvas.dragArr.length > 0
        ) {
          // 切换提示
          this.$dialog
            .confirm({
              title: "温馨提示",
              message: "切换材质后，将会清空已选图片，确认切换吗？",
              className: "confirm-v-dialog tl",
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              cancelButtonColor: "#0076A5",
            })
            .then(() => {
              // 重置画布
              this.$toast("材质属性已更变，请重新选择图片");
              this.reset();

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

      if (!(this.isModelStockOut || this.isMaterialStockOut)) {
        if (hasPic) {
          if (this.isData) {
            if (this.skuNo === "0" || this.skuNo === 0 || this.skuNo === "") {
              this.$toast.fail("信息异常, 请稍后重试！");
              return;
            } else {
              this.submitDialogVisible = true;
            }
          } else {
            this.$toast("请先完善数据！");
          }
        } else {
          if (Number(this.curPicType) === 1 || Number(this.curPicType) === 2) {
            this.$toast("请先选择素材！");
          } else if (Number(this.curPicType) === 3) {
            this.$toast("请先选择模板！");
          }
        }
      }
    },
    // 素材/模板
    openPic(curPicType) {
      if (
        !Number(sessionStorage.getItem("curPicClicked")) ||
        curPicType !== Number(sessionStorage.getItem("curPicType"))
      ) {
        // 非当前类型或者不是第一次点击，请求素材/模板数据
        this.getPicList(curPicType);
        sessionStorage.setItem("curPicClicked", 1);
      } else {
        if (this.picData.length === 0) {
          this.$toast("暂无关联的素材数据");
        }
      }
      if (this.picData && this.picData.length > 0) {
        if (this.picDialog) {
          this.wwidth = window.screen.width;
          this.picDialog = false;
        } else {
          this.wwidth = window.screen.width - this.headerH * 2;
          this.picDialog = true;
          this.mapDialog = false;
        }
      }
    },
    // 贴图
    openMap(curPicType) {
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

      if (hasPic) {
        if (this.mapDialog) {
          this.wwidth = window.screen.width;
          this.mapDialog = false;
        } else {
          if (this.mapData.length === 0) {
            // 没有贴图数据，请求贴图数据
            this.getPicList(curPicType);
          }
          this.wwidth = window.screen.width - this.headerH * 2;
          this.mapDialog = true;
          this.picDialog = false;
        }
      } else {
        if (Number(this.curPicType) === 1) {
          this.$toast("请先选择素材！");
        } else if (Number(this.curPicType) === 3) {
          this.$toast("请先选择模板！");
        }
        if (!this.picDialog) {
          this.getPicList(this.curPicType); // 显示素材/模板
        }
      }
    },
    openImage(e) {
      this.mask = true;
      this.uploadDialog = true;
    },
    // 文字
    openText(e) {
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

      if (hasPic) {
        // 获取字体
        if (this.fontFamily.length === 0) {
          this.getFont();
        }

        this.$refs.textWrapper.style.height = "auto";
        this.mask = true;
        this.textDialog = true;
        this.clearText();
        this.spriteArr.forEach((item) => {
          if (item.selected && item.type === 0) {
            this.inputtext = item.filltext;
            this.family = item.fontFamily;
            this.fontColor = item.fontColor;
          }
        });
      } else {
        if (Number(this.curPicType) === 1) {
          this.$toast("请先选择素材！");
        } else if (Number(this.curPicType) === 3) {
          this.$toast("请先选择模板！");
        }
        if (!this.picDialog) {
          this.openPic(this.curPicType); // 显示素材/模板
        }
      }
    },
    close() {
      this.mask = false;
      this.textDialog = false;
      this.showSelct = false;
    },
    closeUp() {
      this.mask = false;
      this.uploadDialog = false;
    },
    closeMask() {
      this.mask = false;
      this.textDialog = false;
      this.uploadDialog = false;
      this.picDialog = false;
      this.mapDialog = false;
      this.wwidth = window.screen.width;
      this.dialogVisible = false;
    },
    toback() {
      this.$router.push("/index");
    },
    // 上传图片 - 提示
    chooseImg() {
      if (Number(this.uploadValid) === 1) {
        // 统计已上传本地图片数量
        let count = 0;
        this.$refs.mcanvas.dragArr.forEach((item) => {
          if (Number(item.picId) === 0) {
            count++;
          }
        });
        if (count >= 3) {
          this.$toast("本地图片最多只支持上传3张哦~");
        } else {
          if (this.isFlag > 0) {
            this.$refs.file.value = ""; // 再次点击清空input="file"数据，以免同一图片不能重复上传
            this.$refs.file.dispatchEvent(new MouseEvent("click"));
          } else {
            this.$dialog
              .confirm({
                title: "温馨提示",
                message:
                  "尊敬的用户你好，请保证图片清晰可用，不可上传违反国家法律法规图片，对此造成的后果我方不予承担，印刷制作存在些许误差，介意请勿下单喔。",
                className: "confirm-v-dialog tl",
                confirmButtonText: "我知道了",
                confirmButtonColor: "#0076A5",
                cancelButtonText: "返回",
              })
              .then(() => {
                this.isFlag++;
                this.$refs.file.dispatchEvent(new MouseEvent("click"));
              })
              .catch((error) => {
                console.log(error);
              });
          }
        }
      } else {
        this.$toast("当前材质不支持图片上传");
      }
    },
    // Canva
    handleCanva() {
      // 判断是否允许用户上传
      if (Number(this.uploadValid) === 1) {
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

          _self.changeImage({}, _self.exportUrl, 0, 0);
          sessionStorage.setItem("canvaUrl", _self.exportUrl);
        },
        onDesignClose: function () {
          // 关闭编辑器时触发
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

          _self.changeImage({}, _self.exportUrl, 0, 0);
          sessionStorage.setItem("canvaUrl", _self.exportUrl);
        },
        onDesignClose: function () {
          // 关闭编辑器时触发
        },
      });
    },
    // 返回上一页
    handleBack() {
      this.$router.back("-1");
    },
    handleContent() {
      // 隐藏更换型号/材质
      this.showMMWrap = false;
      // 隐藏图库（双列 - 单列）
      this.$refs.picList.showMore = false;
      // 隐藏换图/编辑提示窗
      this.$refs.mcanvas.hasClickedArea = false;
    },
    // 切换图库
    changeImgPic(picType) {
      if (this.$refs.mcanvas.dragArr.length > 0) {
        if (picType !== Number(sessionStorage.getItem("curPicType"))) {
          this.$dialog
            .confirm({
              title: "温馨提示",
              message: "切换模式将不保存之前的操作，是否继续？",
              className: "confirm-v-dialog tl",
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              cancelButtonColor: "#0076A5",
            })
            .then(() => {
              this.initCanvasDate(picType);
            })
            .catch((error) => {
              console.log(error);
            });
        }
      } else {
        this.initCanvasDate(picType);
      }
    },
    initCanvasDate(picType) {
      if (picType !== Number(sessionStorage.getItem("curPicType"))) {
        // 关闭所有显示图层
        this.closeMask();
        // 重置画布
        this.picId = 0;
        this.picName = "";
        this.imgData.posX = null;
        this.wallpImg = "";
        this.reset();
        // 清空模板可点击热区数据
        this.$refs.mcanvas.clickedArea = [];
        this.$refs.mcanvas.curClicked = "";

        // 打开素材/模板
        this.openPic(picType);

        // 当前图库分类，默认第一项
        this.$refs.picList.chooseTab(0);
        this.$refs.picList.curCategory = 0;

        // 图库箭头动画
        if (!this.showMoreAnimate) {
          this.showMoreAnimate = true;
          setTimeout(() => {
            this.showMoreAnimate = false;
          }, 1600);
        }
      }

      this.firstEnter = 0;
      this.curPicType = picType;
      this.isClicked = picType;
      // 记录当前点击类型以及是否第一次点击
      sessionStorage.setItem("curPicType", this.curPicType);
      sessionStorage.setItem("curPicClicked", 1);
    },
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
      this.phoneHeight = this.wheight * 0.75;
      this.phoneWidth = this.phoneHeight * this.pscale;
    },
    mask(value) {
      if (value) {
        document.body.style.cssText = "overflow: hidden";
      } else {
        document.body.style.cssText = "overflow: auto";
      }
    },
  },
  components: {
    Loading,
    "slider-picker": Slider,
    CPicture,
    DrawContainer,
  },
};
</script>

<style scoped lang="stylus">
@import '../../common/styles/mixin.styl';

$white = #fff;
$dark = #000;
$gray = #5A5A5A;
$light-gray = #797878;
$lighter-gray = #B8B8B8;
$blue = #0076A5;
$darkBlue = #009ddb;
$lightBlue = #1275d1;
$gray-circle = #ECECEC;
$blue-circle = #AFE4EC;

.header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 44px;
  line-height: 44px;
  background-color: $white;
  box-shadow: 0px 0px 7px 0px rgba(237, 237, 237, 0.6);
  z-index: 100;

  .van-icon-arrow-left {
    position: absolute;
    top: 50%;
    left: 10px;
    font-size: 20px;
    color: $dark;
    transform: translateY(-50%);
  }

  .submit-btn {
    position: absolute;
    top: 50%;
    right: 10px;
    padding: 0 9px;
    height: 26px;
    line-height: 26px;
    font-size: 14px;
    color: $white;
    background-color: $blue;
    transform: translateY(-50%);
    border-radius: 30px;

    &.diable {
      color: #333;
      background: #f4f4f4;
    }
  }

  .pic-category {
    padding: 0 60px;
    display: flex;

    li {
      flex: 1;
      font-size: 16px;
      color: $light-gray;
      text-align: center;
    }

    span {
      &.active {
        display: inline-block;
        position: relative;

        &:before {
          content: '';
          position: absolute;
          top: 50%;
          right: -5px;
          left: -5px;
          margin-top: 8px;
          padding: 0 5px;
          height: 4px;
          background: linear-gradient(94deg, $darkBlue, $lightBlue);
          opacity: 0.8;
          border-radius: 2px;
        }
      }
    }
  }
}

.m-m-wrap {
  position: fixed;
  bottom: 185px;
  right: 0;
  width: auto;
  height: 35px;
  color: $white;
  text-align: center;
  line-height: 35px;
  z-index: 100;

  .m-m-icon {
    display: block;
    position: absolute;
    right: 0;
    padding-left: 5px;
    width: 31px;
    font-size: 18px;
    background-color: $blue;
    box-shadow: 0px 2px 3px 0px rgba(48, 156, 206, 0.36);
    border-radius: 45px 0 0 45px;

    &.hidden {
      display: none;
    }
  }

  .m-m-content {
    display: flex;
    position: absolute;
    right: 0;
    padding-left: 0;
    width: 0;
    height: 35px;
    font-size: 13px;
    background-color: $blue;
    box-shadow: 0px 1px 2px 0px rgba(48, 156, 206, 0.36);
    border-radius: 45px 0 0 45px;
    transition: all 0.3s;

    &.active {
      padding-left: 5px;
      width: 170px;
    }

    .icon_shouhui {
      position: relative;
      top: 10px;
      left: 10px;
      margin-right: 10px;
    }

    .type {
      flex: 1;

      &+.type {
        position: relative;

        &:before {
          display: inline-block;
          content: '';
          position: absolute;
          top: 10px;
          left: 0;
          bottom: 10px;
          border-left: 1px solid $white;
        }
      }
    }
  }
}

.custom-b-wrap {
  position: absolute;
  bottom: 0;
  width: 100%;
  height: 180px;
  padding-bottom: 10px;
  text-align: center;
  box-sizing: border-box;

  .start_button_star {
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 0 auto;
    font-size: 16px;
    color: $white;
    z-index: 100;

    i {
      display: block;
      position: relative;
      top: -5px;
      width: 40px;
      font-style: normal;
    }
  }

  .current-mm {
    position: relative;
    font-size: 12px;
    color: $gray;
    text-align: center;

    span {
      &+span {
        position: relative;
        margin-left: 15px;

        &:before {
          display: block;
          content: '';
          position: absolute;
          top: 0;
          left: -8px;
          width: 1px;
          height: 100%;
          background: linear-gradient(0deg, rgba(244, 244, 244, 0), $gray, rgba(244, 244, 244, 0));
        }
      }
    }
  }

  .print-result {
    display: flex;
    align-items: flex-end;
    position: absolute;
    width: 100%;
    top: 38px;
    left: 0;
    opacity: 0;
    transition: all 1s;

    &.active {
      opacity: 1;
    }

    .print-info {
      flex: 1;
      font-size: 13px;

      &+.print-info {
        position: relative;

        &:before {
          content: '';
          position: absolute;
          bottom: 33px;
          right: 60%;
          width: 80%;
          height: 4px;
          background-color: $gray-circle;
          border-radius: 4px;
        }
      }

      &.cur-print1, &.cur-print2, &.cur-print3 {
        .circle {
          background-color: $blue-circle;

          &:after {
            background-color: $blue;
          }
        }

        .info {
          position: relative;
          color: $white;
          background-color: $blue;
          box-shadow: 0px 1px 2px 0px rgba(48, 156, 206, 0.36);

          &:before {
            content: '';
            position: absolute;
            top: -5px;
            left: 50%;
            margin-left: -6px;
            width: 0;
            height: 0;
            border-right: 6px solid transparent;
            border-left: 6px solid transparent;
            border-bottom: 6px solid $blue;
          }
        }
      }
    }

    .intro {
      color: $lighter-gray;
    }

    .circle {
      display: block;
      position: relative;
      left: 50%;
      margin: 10px 0;
      width: 14px;
      height: 14px;
      background-color: $gray-circle;
      border-radius: 50%;
      transform: translateX(-50%);

      &:after {
        content: '';
        display: block;
        position: absolute;
        width: 6px;
        height: 6px;
        top: 4px;
        left: 4px;
        background-color: $lighter-gray;
        border-radius: 50%;
      }
    }

    .info {
      display: inline-block;
      padding: 3px 10px;
      color: $gray;
      box-shadow: 0px 1px 2px 0px rgba(255, 255, 255, 0);
      border-radius: 20px;
    }
  }

  .btn-list {
    position: absolute;
    top: 0;
    bottom: auto;
    width: 100%;
    display: flex;
    padding: 0 20px;
    box-sizing: border-box;
    opacity: 0;
    transition: all 1s;

    .btn-item {
      position: absolute;
      top: 0;
      left: 50%;
      transform: translateX(-50%);
      transition: all 1s;
    }

    &.active {
      top: auto;
      bottom: 0;
      opacity: 1;

      .btn-item {
        flex: 1;
        position: relative;
        left: auto;
        transform: translateX(0);
        text-align: center;
      }
    }
  }
}
</style>

<style scoped lang="stylus" rel="stylesheet/stylus">
@import '~common/styles/mixin.styl';
@import '~common/styles/variable';

.noclick {
  pointer-events: none;
}

.phone {
  position: fixed;
  top: 44px;
  bottom: 0;
  left: 0;
  right: 0;
  transition: all 0.6s;

  &.show-pic {
    left: 88px;
  }

  .content-wrapper {
    position: relative;
    right: 0;
    left: 0;
    height: 100%;

    .phone-wrapper {
      position: absolute;
      right: 0;
      left: 0;
      overflow: hidden;
    }
  }
}

.text-wrapper {
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

    >>>.van-cell__value {
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

  >>>.el-radio {
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
      border-radius: 50%;
      box-shadow: 1px 1px 5px rgba(0, 0, 0, 0.2);
    }

    .vc-slider {
      flex: 1;
      margin-left: 30px;

      >>>.vc-slider-hue-warp {
        height: 12px;
      }

      >>>.vc-slider-hue-warp, >>>.vc-slider-swatches {
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

.piclist-dialog {
  position: relative;
  top: 44px;
  right: 0;
  left: 0;
  z-index: 10;
}

.msg-dialog {
  display: inline-block;
  width: 90%;
  padding: 18px 0 0;
  text-align: center;
  box-sizing: border-box;
  border-radius: 8px;

  >>>.van-dialog__confirm {
    color: $blue;
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

  >>>.van-dialog__header {
    padding: 0 0 20px;
  }

  >>>.van-dialog__content {
    .item-content {
      display: flex;
      padding: 10px 15px;
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
        margin-left: 15px;

        .name {
          font-size: 16px;
          color: #333333;

          &.xs {
            font-size: 14px;
          }

          .text {
            color: #0076A5;
          }
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
            color: #0076A5;
          }
        }
      }
    }
  }

  >>>.van-dialog__footer {
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
      color: #ffffff;
      background: #0076A5;
      border-radius: 20px;

      &.van-dialog__cancel {
        color: #333333;
        background-color: #F4F4F4;
      }

      &:before, &:after {
        border: none;
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
  >>>.picker {
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

  >>>.van-picker {
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

.hasMore {
  position: fixed;
  top: 44px;
  left: 0;
  width: 100%;
  height: 46px;
  z-index: 100;

  .sprite-icon {
    position: absolute;
    top: 50%;
    font-size: 16px;
    transform: translateY(-50%) scale(0.8);
    z-index: 13;

    &.arrow_left {
      left: 6px;

      &.animate {
        animation: animateArrowL 0.8s linear 2;
      }

      @keyframes animateArrowL {
        from {
          left: 6px;
          opacity: 1;
        }

        to {
          left: -3px;
          opacity: 0.6;
        }
      }
    }

    &.arrow_right {
      right: 6px;

      &.animate {
        animation: animateArrowR 0.8s linear 2;
      }

      @keyframes animateArrowR {
        from {
          right: 6px;
          opacity: 1;
        }

        to {
          right: -3px;
          opacity: 0.6;
        }
      }
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
    font-size: 16px;
    color: #4A4A4A;
  }
}
</style>
