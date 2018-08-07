<template>
  <div class="content-wrap-t">
    <el-tabs v-model="activeName[i]" @tab-click="handleClick" v-for="(item, i) in menu" :key="item.id"> 
      <el-tab-pane v-if="$i18n.locale === 'zh' ? item.titleZh : item.titleEn" :label="$i18n.locale === 'zh' ? item.titleZh : item.titleEn" name="title" :disabled="true"/>
      <el-tab-pane
        v-show="$i18n.locale === 'zh' ? item.titleZh : item.titleEn"
        v-for="item1 in item.childrens"
        :key="item1.id"
        :label="$i18n.locale === 'zh' ? item1.titleZh : item1.titleEn"
        :name="item1.titleEn + item1.id"
        :lazy="true"
        :disabled="item1.childrens.length === 0"
      >
        <TrainContentItem :childrens="item1.childrens"></TrainContentItem>
      </el-tab-pane>
    </el-tabs>
    <!--加载动画-->
    <loading v-if="showLoading === true"></loading>
  </div>
</template>

<script>
import TrainContentItem from './TrainContentItem'
import loading from '@/components/loading.vue'
// liu
import {trainingCenterList}  from '@/apiService/api'
export default {
  name: 'TrainContentBox',
  components: {TrainContentItem, loading},
  data () {
    return {
      activeName: [],
      menu: [],
      showLoading: false
    }
  },
  beforeMount () {
    this.showLoading = true
  },
  created () {
   this.getMenu(0)
  },
  methods: {
    handleClick (tab, event) {
    },
    async getMenu (parentId) {
      // 根据父id获取菜单
      let dataRes=await trainingCenterList({parentId: parentId, page: 1, size: 1000});
      if (dataRes.success) {
          dataRes.data.list.map(val => {
            let val1 = val.childrens
            if (val1.length > 0 && val1[0].childrens && val1[0].childrens.length > 0) {
              this.activeName.push(val1[0].titleEn + val1[0].id)
            } else {
              this.activeName.push('null')
            }
            return val;
          })
          this.menu=dataRes.data.list;
          this.showLoading = false
        }

      //  this.menu = await this.$api.get(this, '/user/u/trainingCenter/list/', {parentId: parentId, page: 1, size: 1000}).then(res => {
      //   if (res.code === 0) {
      //     res.data.content.map(val => {
      //       let val1 = val.childrens
      //       if (val1.length > 0 && val1[0].childrens && val1[0].childrens.length > 0) {
      //         this.activeName.push(val1[0].titleEn + val1[0].id)
      //       } else {
      //         this.activeName.push('null')
      //       }
      //       return val;
      //     })
      //     return res.data.content
      //   }
      // }).finally(() => {
      //   this.showLoading = false
      // })
    }
  }
}
</script>

<style lang="less">
@import url("../../../assets/less/variable.less");
.content-wrap-t {
  margin-top: 48px;
  .el-tabs {
    margin-bottom: 27px;
    .el-tabs__header {
      margin: 0 0 18px;
      .el-tabs__nav-wrap {
        &.is-scrollable {
          padding: 0;
        }
      }
      .el-tabs__nav-prev,
      .el-tabs__nav-next {
        display: none;
      }
      .el-tabs__nav {
        display: flex;
        align-items: center;
        #tab-title {
          height: 33px;
          font-size: 24px;
          font-family: PingFangSC-Medium, PingFang SC;
          color: @lightBlack;
          line-height: 33px;
          font-weight: 500;
          padding: unset;
          padding-right: 16px;
          &:hover {
            color: @lightBlack !important;
          }
        }
      }
      .el-tabs__item {
        padding: 0px 16px;
        height: 20px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        font-size: 14px;
        line-height: 20px;
        color: #333;
        &.is-disabled {
          &:hover {
            color: #333 !important;
          }
        }
      }
    }
    .el-tabs__active-bar {
      display: none;
    }
    .el-tabs__nav-wrap::after {
      display: none;
    }
  }
}
</style>
