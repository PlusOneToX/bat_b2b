<template>
  <div>
    <div class="index rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState"></Header>
      <!--主内容-->
      <div class="main rl-margin-zero rl-padding-top-default">
        <div class="detail rl-bg-white rl-padding-horizontal-lllg">
          <div class="head rl-tc rl-bdb-gray-sm rl-text-sm" v-if="announceDetail">
            <span v-show="$i18n.locale === 'zh' || !announceDetail.titleEn == true">{{announceDetail.title}}</span>
            <span v-show="$i18n.locale === 'en'">{{announceDetail.titleEn}}</span>
          </div>
          <div class="content rl-bdb-gray-sm">
            <div class="rl-tc rl-text-gray rl-padding-top-default rl-padding-bottom-default">
              <i v-show="$i18n.locale === 'zh'">发布日期</i><i v-show="$i18n.locale === 'en'">date issued</i>:{{new Date(parseInt(announceDetail.createTime)).toLocaleDateString()}}
            </div>
            <p class="html-p" v-html="announceDetail.content"></p>
          </div>
          <div class="attachment cursor-pointer" @click="downLoad" v-if="announceDetail.attachmentUrl !== ''">附件：{{announceDetail.attachmentName}}</div>
        </div>
        <div class="choose rl-clear rl-margin-zero rl-padding-top-default rl-padding-bottom-default">
          <div class="rl-fl rl-tc cursor-pointer" @click="goUp" v-if="this.up === true">
            《<i v-show="$i18n.locale === 'zh'">上一篇</i><i v-show="$i18n.locale === 'en'">In the previous</i>
          </div>
          <div class="rl-fl rl-tc cursor-pointer" @click="firstPage" v-else>
            《<i v-show="$i18n.locale === 'zh'">上一篇</i><i v-show="$i18n.locale === 'en'">In the previous</i>
          </div>
          <div class="rl-fl rl-tc cursor-pointer" @click="goDown" v-if="this.down === true">
            <i v-show="$i18n.locale === 'zh'">下一篇</i><i v-show="$i18n.locale === 'en'">The next article</i>》
          </div>
          <div class="rl-fl rl-tc cursor-pointer" @click="lastPage" v-else>
            <i v-show="$i18n.locale === 'zh'">下一篇</i><i v-show="$i18n.locale === 'en'">The next article</i>》
          </div>
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
import GD from '@/assets/js/globalData'
// liu
import {noticeDetail,noticeList} from '@/apiService/api'
export default {
  name: 'NoticeDetail',
  components: {
    Footer,
    Header
  },
  data () {
    return {
      userState: 2,
      id: this.$route.query.notice_id,
      noticeIndex: this.$route.query.notice_index,
      noticeList: [],
      announceDetail: {},
      up: true,
      down: true,
      useLang: false, // 是否启用多语种
      langList: GD.langList, // 语种列表
      lang: 'zh-CNY' // 语种
    }
  },
  methods: {
    // 公告列表
    noticeData () {
       noticeList({page:1,size:500}).then(res => {
        if (res.success) {
          this.noticeList = res.data.list
        }
      })
    },
    // 公告详情
    getNoticeDetail () {
      // this.$api.get(this, 'user/store/announcement/detail', {id: this.id})
      noticeDetail({id: this.id}).then(res => {
        if (res.success) {
          window.localStorage.setItem('notice_id', this.id)
          this.$nextTick(() => {
            this.$route.query.notice_id = this.id
          })
          // console.log(this.$route.query.notice_id)
          this.announceDetail = res.data
        }
      })
    },
    // 上一篇
    goUp () {
      var Index = Number(this.noticeIndex)
      this.down = true
      // this.$api.get(this, 'user/store/announcement/list')
       noticeList({page:1,size:500}).then(res => {
        if (res.success) {
          this.noticeList = res.data.list;
          this.noticeList.forEach((item, index) => {
            if (Index === (index + 1)) {
              this.noticeIndex = index
              window.localStorage.setItem('notice_index', this.noticeIndex)
              this.id = item.id
              this.getNoticeDetail()
            } else if (Index === 0) {
              this.up = false
              if (this.$i18n.locale === 'zh') {
                this.$message.warning('已经是第一篇了!')
              } else { this.$message.warning('This is already the first one.') }
            }
          })
        }
      })
    },
    firstPage () {
      if (this.$i18n.locale === 'zh') {
        this.$message.warning('已经是第一篇了!')
      } else { this.$message.warning('This is already the first one.') }
    },
    // 下一篇
    goDown () {
      var Index = Number(this.noticeIndex)
      this.up = true
      noticeList({page:1,size:500}).then(res => {
        if (res.success) {
          this.noticeList = res.data.list
          this.noticeList.forEach((item, index) => {
            if (Index === (index - 1)) {
              this.noticeIndex = index
              window.localStorage.setItem('notice_index', this.noticeIndex)
              this.id = item.id
              this.getNoticeDetail()
            } else if (Index === Number(this.noticeList.length - 1)) {
              this.down = false
              if (this.$i18n.locale === 'zh') {
                this.$message.warning('已经是最后一篇了!')
              } else { this.$message.warning('This is already the last one.') }
            }
          })
        }
      })
    },
    lastPage () {
      if (this.$i18n.locale === 'zh') {
        this.$message.warning('已经是最后一篇了!')
      } else { this.$message.warning('This is already the last one.') }
    },
    // 下载附件
    downLoad () {
      window.open(this.announceDetail.attachmentUrl)
    }
  },
  created () {
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem('bLang') ? window.localStorage.getItem('bLang') : 'zh_CNY';
  },
  mounted () {
    var val = window.localStorage.getItem('notice_id')
    var vals = window.localStorage.getItem('notice_index')
    if (val !== '' && val !== undefined && val !== null) {
      this.id = val
    }
    if (vals !== '' && val !== undefined && val !== null) {
      this.noticeIndex = vals
    }
    if (this.noticeIndex === 0) {
      this.up = false
    }
    if (this.noticeIndex === (this.noticeList.length - 1)) {
      this.down = false
    }
    this.getNoticeDetail()
  }
}
</script>
<style scoped lang='less'>
  .html-p{
    p{
      img{width: 100%}
      font{
        font-size: 18px;
      }
    }
    ul{
      li{
        font{
          font-size: 18px;
        }
      }
    }
  }
</style>
<style scoped="scoped" lang='less'>
  .index{
    width: 100%;
  }
  .main{
    width: 1190px;
    .detail{
      .head{
        height: 62px;
        line-height: 62px;
      }
      .content{
        p{
          text-indent: 30px;
        }
      }
      .attachment{
        height: 60px;
        line-height: 60px;
      }
    }
    .choose{
      width: 500px;
      div{width: 250px}
    }
  }
</style>
