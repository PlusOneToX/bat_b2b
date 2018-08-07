<template>
  <div>
    <div class="content-item" v-for="item in childrens" :key="item.id">
      <div v-if="$i18n.locale === 'zh'&&item.contentUrlZh">
        <div class="img-box" @click="handleClick(item)">{{item.thumbnailUrlZh?'':item.titleZh}}</div>
        <el-image :src="item.thumbnailUrlZh? item.thumbnailUrlZh: require('../../../assets/images/userSelfService/003copy.png')" fit="contain">
        </el-image>
        <div class="video-title-box">{{ item.titleZh }}</div>
      </div>
      <div v-if="$i18n.locale === 'en'&&item.contentUrlEn">
        <div class="img-box" @click="handleClick(item)">{{item.thumbnailUrlEn?'':item.titleEn}}</div>
        <el-image :src="item.thumbnailUrlEn? item.thumbnailUrlEn: require('../../../assets/images/userSelfService/003copy.png')" fit="contain">
        </el-image>
        <div class="video-title-box">{{ item.titleEn }}</div>
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
  data () {
    return {

    }
  },
  methods: {
    handleClick (item) {
      if (this.$i18n.locale === 'zh') {
        window.open(item.contentUrlZh)
      } else if (this.$i18n.locale === 'en') {
        window.open(item.contentUrlEn)
      }
    }
  }
}
</script>

<style lang="less">
@import url("../../../assets/less/variable.less");
.content-item {
  vertical-align: top;
  display: inline-block;
  margin-right: 22px;
  background-color: @proBg;
  &:nth-child(3n) {
    margin-right: 0px;
  }
  &:hover {
    box-shadow: 0px 1px 21px 0px rgba(108, 108, 108, 0.5);
  }
  margin-bottom: 21px;
  .el-image {
    width: 385px;
    height: 200px;
    border-radius: 8px 8px 0px 0px;
    .el-image__inner{
      width: 100%;
      height: auto;
    }
  }
  .img-box {
    position: absolute;
    width: 385px;
    height: 200px;
    line-height:200px;
    font-size:32px;
    color:#ffffff;
    text-align: center;
    cursor: pointer;
    z-index: 10;
    &:hover {
      background: rgba(0, 0, 0, 0.5)
        url("../../../assets/images/userSelfService/icon-bofang1@2x.png")
        no-repeat center center / 83px 83px;
    }
  }
  .video-title-box {
    display: block;
    height: 53px;
    font-size: 16px;
    font-family: PingFangSC-Medium, PingFang SC;
    line-height: 53px;
    padding: 0 15px;
    color: #000000;
    font-weight: 500;
  }
}
</style>
