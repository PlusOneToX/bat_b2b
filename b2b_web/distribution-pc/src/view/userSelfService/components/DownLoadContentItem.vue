<template>
  <div class="download-content-box">
    <div class="content-item-1">
      <div class="download-content-item" v-for="(item, i) in childrens1" :key="item.id">
        <div class="content-item-item" v-if="$i18n.locale === 'zh'&&item.contentUrlZh">
          <div class="num">{{ formatNum(i + 1) }}</div>
          <div class="text">
            <span>{{ item.titleZh }}</span>
            <div class="line"></div>
          </div>
          <el-button class="gotoDownLoad" @click="handleClick(item)">
            <i class="el-icon-download-myself el-icon--left"></i
            >去下载</el-button
          >
        </div>
        <div class="content-item-item" v-if="$i18n.locale === 'en'&&item.contentUrlEn">
          <div class="num">{{ formatNum(i + 1) }}</div>
          <div class="text">
            <span>{{ item.titleEn }}</span>
            <div class="line"></div>
          </div>
          <el-button class="gotoDownLoad" @click="handleClick(item)">
            <i class="el-icon-download-myself el-icon--left"></i
            >download</el-button
          >
        </div>
      </div>
    </div>
    <div class="content-item-2">
      <div class="download-content-item" v-for="(item, i) in childrens2" :key="i">
        <div class="content-item-item" v-if="$i18n.locale === 'zh'&&item.contentUrlZh">
          <div class="num">{{ formatNum(childrens1.length + i + 1) }}</div>
          <div class="text">
            <span>{{ item.titleZh }}</span>
            <div class="line"></div>
          </div>
          <el-button class="gotoDownLoad" @click="handleClick(item)">
            <i class="el-icon-download-myself el-icon--left"></i
            >去下载</el-button
          >
        </div>
        <div class="content-item-item" v-if="$i18n.locale === 'en'&&item.contentUrlEn">
          <div class="num">{{ formatNum(childrens1.length + i + 1) }}</div>
          <div class="text">
            <span>{{ item.titleEn }}</span>
            <div class="line"></div>
          </div>
          <el-button class="gotoDownLoad" @click="handleClick(item)">
            <i class="el-icon-download-myself el-icon--left"></i
            >download</el-button
          >
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TrainContentItem',
  props: {
    childrens: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  created () {
    let middle = Math.ceil(this.childrens.length / 2)
    this.childrens1 = this.childrens.slice(0, middle)
    this.childrens2 = this.childrens.slice(middle)
  },
  data () {
    return {
      childrens1: [],
      childrens2: []
    }
  },
  methods: {
    handleClick (item) {
      if (this.$i18n.locale === 'zh') {
        window.open(item.contentUrlZh)
      } else if (this.$i18n.locale === 'en') {
        window.open(item.contentUrlEn)
      }
    },
    // 将一位数补零
    formatNum (num) {
      if (typeof num === 'number' && !isNaN(num)) {
        if (parseInt(num / 10) === 0) {
          return '0' + num
        } else {
          return num
        }
      }
    }
  }
}
</script>

<style lang="less">
@import url("../../../assets/less/variable.less");
.download-content-box {
  padding: 30px 50px;
  .content-item-1,.content-item-2{
    width: 540px;
    display: inline-block;
    vertical-align: top;
  }
  .download-content-item {
    // display: inline-block;
    // width: 500px;
    margin: 10px 20px;
    .content-item-item {
      width: 500px;
      display: inline-flex;
      align-items: center;
      justify-content: space-between;
      .num {
        padding: 0px 2px;
        height: 22px;
        font-size: 16px;
        line-height: 22px;
        display: inline-block;
        color: #fff;
        background: linear-gradient(#23cbe3, #3aa9ef);
      }
      .text {
        display: inline-flex;
        height: 22px;
        flex: 1;
        font-size: 16px;
        line-height: 22px;
        padding-left: 12px;
        align-items: center;
        .line {
          display: inline-block;
          margin: 0px 10px;
          flex: 1;
          height: 1px;
          border-top: 1px dashed #979797;
        }
      }
      .gotoDownLoad {
        display: inline-block;
        height: 30px;
        background-color: #3ec9e0;
        color: #fff;
        border: none;
        padding: 0 10px;
        box-sizing: border-box;
        .el-icon-download-myself::before {
          display: inline-block;
          vertical-align: middle;
          width: 14px;
          height: 14px;
          content: "";
          background: url("../../../assets/images/userSelfService/download@2x.png")
            no-repeat center center / 14px 14px;
        }
      }
    }
  }
}
</style>
