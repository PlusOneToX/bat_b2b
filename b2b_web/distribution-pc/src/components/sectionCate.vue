<template>
  <div class="section">
    <ul>
      <li
        v-show="index <= 5"
        @click="goCategory(sectionCate)"
        v-for="(sectionCate,index) in sectionCateList"
        :key="sectionCate.id"
      >
        <i
          v-show="$i18n.locale === 'zh' || !sectionCate.nameEn == true"
        >{{sectionCate.name}}</i>
        <i v-show="$i18n.locale === 'en'">{{sectionCate.nameEn}}</i>
      </li>
    </ul>
  </div>
</template>

<script>
import GD from "@/assets/js/globalData";
// liu
import {sectionClassifyList} from '@/apiService/api'
export default {
  name: "sectionCate",
  props: {
    itemId: {
      type: Number,
      default: 0,
    },
  },
  data() {
    return {
      sectionCateList: [], // 首页商品分类名称
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: "zh-RMB", // 语种
    };
  },
  methods: {
    fLangChange(value) {
      window.localStorage.setItem("bLang", value);
      this.$i18n.locale = value.split("-")[0];
      this.$i18n.curren = value.slice(3, 6);
    },
    shopData() {
      // 商品分类
      // this.$api
      //   .get(this, "user/section/category", { sectionIds: this.itemId })
        sectionClassifyList({sectionId:this.itemId  }).then((res) => {
          if (res.success) {
            this.sectionCateList = res.data;
          }
        });
    },
    // 去分类更多
    goCategory(item) {
      this.$router.push({
        name: "Classify",
        query: {
          classsity_id: item.id,
          classsity_name: item.name,
          classsity_name_en: item.nameEn,
        },
      });
    },
  },
  created() {
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem("bLang")
      ? window.localStorage.getItem("bLang")
      : "zh-RMB";
  },
  mounted() {
    this.shopData();
  },
};
</script>
<style  scoped="scoped" lang='less'>
@import url("../assets/less/variable.less");
.section {
  display: inline-block;
  margin-top: 12px;
  margin-left: 15px;
  li {
    position: relative;
    padding-left: 15px;
    padding-right: 15px;
    float: left;
    color: @lightGray;
    font-size: 14px;
    background: url("../assets/images/icon-shu-hao2.png") no-repeat right center;
    cursor: pointer;
  }
  li:hover {
    color: @blue;
  }
}
</style>
