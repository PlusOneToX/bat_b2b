<template>
  <div class="content-wrap">
    <el-tabs
      v-model="activeName[i]"
      @tab-click="handleClick"
      v-for="(item, i) in menu"
      :key="item.id"
    >
      <el-tab-pane
        v-if="$i18n.locale === 'zh' ? item.titleZh : item.titleEn"
        :label="$i18n.locale === 'zh' ? item.titleZh : item.titleEn"
        name="title"
        :disabled="true"
      />
      <el-tab-pane
        v-show="$i18n.locale === 'zh' ? item.titleZh : item.titleEn"
        v-for="item1 in item.childrens"
        :key="item1.id"
        :label="$i18n.locale === 'zh' ? item1.titleZh : item1.titleEn"
        :name="item1.titleEn + item1.id"
        :lazy="true"
        :disabled="item1.childrens.length === 0"
      >
        <DownLoadContentItem :childrens="item1.childrens"></DownLoadContentItem>
      </el-tab-pane>
    </el-tabs>
    <!--加载动画-->
    <loading v-if="showLoading === true"></loading>
  </div>
</template>

<script>
import DownLoadContentItem from './DownLoadContentItem'
import loading from '@/components/loading.vue'
// liu-
import {downloadCenterList} from '@/apiService/api'
export default {
  name: 'DownLoadContentBox',
  components: {DownLoadContentItem, loading},
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
       this.menu = await downloadCenterList( {parentId: parentId, page: 1, size: 1000}).then(res => {
        if (res.success) {
          res.data.list.map(val => {
            let val1 = val.childrens
            if (val1.length > 0 && val1[0].childrens && val1[0].childrens.length > 0) {
              this.activeName.push(val1[0].titleEn + val1[0].id)
            } else {
              this.activeName.push('null')
            }
            return val;
          })
          return res.data.list
        }
      }).finally(() => {
          this.showLoading = false
      })
    }
  }
}
</script>

<style lang="less" >
@import url("../../../assets/less/variable.less");
.content-wrap {
  margin-top: 48px;
  .el-tabs {
    margin-bottom: 27px;
    .el-tabs__header {
      margin: 0 0 18px;
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
    .el-tabs__content {
      .el-tab-pane {
        border-radius: 8px;
        background-color: #e7e7e7;
      }
    }
  }
}
</style>
