<template>
  <div>
    <div class="user rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState"></Header>
      <!--主内容-->
      <div class="main rl-padding-top-lllg rl-margin-zero">
        <div class="classify rl-margin-bottom-double">
          <div class="item rl-clear rl-bdt-gray-sm">
            <div class="rl-fl left rl-text-bold rl-bg-gray-mm rl-padding-left-default">品牌</div>
            <div class="rl-fl right rl-text-xxs rl-bg-white rl-padding-left-double">
              <span>BAT</span>
            </div>
          </div>
          <div class="item rl-clear rl-bdt-gray-sm rl-bdb-gray-sm">
            <div class="rl-fl left rl-text-bold rl-bg-gray-mm rl-padding-left-default">全部视频</div>
            <div class="rl-fl right rl-text-xxs rl-bg-white rl-padding-left-double">
              <span>广告视频</span>
              <span>测评视频</span>
              <span>培训视频</span>
            </div>
          </div>
        </div>
        <div class="info-list">
          <ul class="rl-clear">
            <li v-for="item in items" :key="item.id">
              <div class="content rl-bg-white">
                <div class="rl-text-bold rl-text-xxs rl-padding-top-mid rl-padding-bottom-xxxs rl-bdb-gray-sm rl-padding-left-mid">广告视频</div>
                <div class="rl-text-xxs rl-padding-top-xxxs rl-padding-left-mid"></div>
                <!--<div class="img"><img src="../../src/assets/images/product-info1.png" alt=""></div>-->
              </div>
            </li>
          </ul>
        </div>
        <div class="rl-tr rl-margin-top-default">
          <el-pagination
            background
            @current-change ="handleCurrentChange"
            @size-change="handleSizeChange"
            layout="prev, pager, next"
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
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'
export default {
  name: 'Information',
  components: {
    Header,
    Footer
  },
  data () {
    return {
      userState: 2,
      totalCount: 15,
      cur_page: 1,
      pagesize: 3,
      items: [1, 2, 3, 4, 5, 6, 7, 8]
    }
  },
  methods: {
    // 当前页码
    handleCurrentChange (val) {
      this.cur_page = val
      this.getDatas()
    },
    // 每页条数
    handleSizeChange (val) {
      this.pagesize = val
      this.getDatas()
    }
  },
  mounted () {
  }
}
</script>

<style scoped="scoped" lang='less'>
  .user{width: 100%;}
  .main{
    width: 1024px;
    padding-bottom: 20px;
    .classify{
      .item{
        div{
          height: 48px;
          line-height: 48px;
        }
        .left{
          width: 115px;
        }
        .right{
          width: 849px;
          span{
            margin-right: 15px;
          }
        }
      }
    }
    .info-list{
      ul{
        li{
          margin-bottom: 30px;
          float: left;
          width: 25%;
          .content{
            margin-left: 15px;
            width: 222px;
            .img{
              img{width: 100%}
            }
          }
        }
      }
    }
  }
</style>
