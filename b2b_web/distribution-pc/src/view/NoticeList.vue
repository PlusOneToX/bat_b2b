<template>
  <div>
    <div class="index rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState"></Header>
      <!--主内容-->
      <div class="main rl-margin-zero rl-bg-white">
        <div class="notice rl-margin-top-xxxs">
          <div @click="goNoticeDetail(item.id,index)" class="item rl-clear rl-bdb-gray-sm rl-padding-horizontal-lllg" v-for="(item,index) in noticeList" :key="item.id">
            <div class="rl-fl rl-text-xs rl-text-gray">
              <span v-show="$i18n.locale === 'zh' || !item.titleEn == true">{{item.title}}</span>
              <span v-show="$i18n.locale === 'en'">{{item.titleEn}}</span></div>
            <div class="rl-fr rl-text-gray">{{new Date(parseInt(item.createTime)).toLocaleDateString()}}</div>
          </div>
        </div>
        <div class="rl-tr rl-margin-bottom-default rl-margin-right-lllg">
          <el-pagination
            background
            @current-change ="handleCurrentChange"
            @size-change="handleSizeChange"
            layout="prev, pager, next, jumper"
            :page-size="pagesize"
            :total="totalCount"
          >
          </el-pagination>
        </div>
      </div>
      <!--公共底部-->
      <Footer></Footer>
    </div>
  </div>
</template>

<script>
import Footer from '@/components/Footer.vue'
import Header from '@/components/Header.vue'
// liu
import {noticeDetail,noticeList} from '@/apiService/api'
export default {
  name: 'NoticeList',
  components: {
    Footer,
    Header
  },
  data () {
    return {
      userState: 2,
      totalCount: 8,
      cur_page: 1,
      pagesize: 8,
      noticeList: []
    }
  },
  methods: {
    // 当前页码
    handleCurrentChange (val) {
      this.cur_page = val
      this.getColumnGoodsList()
    },
    // 每页条数
    handleSizeChange (val) {
      this.pagesize = val
      this.getColumnGoodsList()
    },
    // 公告列表
    noticeData () {
      noticeList({page:1,size:500}).then(res => {
        if (res.code === 0) {
          this.noticeList = res.announcements
        }
      })
    },
    // 前往公告详情
    goNoticeDetail (id, index) {
      window.localStorage.removeItem('notice_id')
      window.localStorage.removeItem('notice_index')
      this.$router.push({name: 'NoticeDetail', query: {notice_id: id, notice_index: index}})
    }
  },
  mounted () {
    this.noticeData()
  }
}
</script>

<style scoped="scoped" lang='less'>
  .index{
    width: 100%;
  }
  .main{
    padding-bottom: 20px;
    width: 1190px;
    .notice{
      padding-bottom: 40px;
      .item{
        height: 60px;
        line-height: 60px;
        cursor: pointer;
      }
    }
  }
</style>
