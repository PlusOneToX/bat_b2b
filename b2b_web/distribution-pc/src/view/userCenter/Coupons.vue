<template>
  <div>
    <div class="user rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState"></Header>
      <!--主内容-->
      <div class="user-main rl-clear rl-margin-zero">
        <!--公共左边-->
        <Left></Left>
        <div class="user-right rl-fr rl-padding-horizontal-lllg rl-bg-white rl-padding-top-default rl-padding-bottom-double">
          <div class="content">
            <div class="title rl-bg-gray-xm rl-margin-bottom-default">
              <div class="rl-padding-left-lllg title-cons rl-text-white rl-text-mid">我的优惠券</div>
            </div>
            <div class="case">
              <div class="case-nav rl-bg-gray-xm">
                <ul>
                  <li class="rl-padding-left-default rl-padding-right-default rl-text-bold current" @click="changeState('')">全部</li>
                  <li class="rl-padding-left-default rl-padding-right-default rl-text-black">未使用</li>
                  <li class="rl-padding-left-default rl-padding-right-default rl-text-black">已使用</li>
                  <li class="rl-padding-left-default rl-padding-right-default rl-text-black">已过期</li>
                </ul>
              </div>
              <div class="coupons rl-padding-horizontal-mid rl-padding-top-default rl-bg-gray-xm">
                <ul>
                  <li>
                    <div class="item rl-clear">
                      <div class="item-left rl-fl rl-bg-orange-sm">
                        <div class="rl-text-white"><span class="rl-text-default">¥</span><span class="large rl-margin-left-xxxxs">50</span></div>
                        <div class="rl-text-xxs rl-text-white">满500可用</div>
                      </div>
                      <div class="item-right rl-fl rl-bg-white">
                        <div><span class="rl-text-xxs">劵号:</span><span class="rl-text-xs rl-margin-left-xxxxs">643164613</span></div>
                        <div class="rl-text-xxs rl-text-gray">2017.12.07-2017.12.12</div>
                        <div class="state rl-bd-orange-sm rl-tc rl-text-orange-sm rl-text-xxs rl-fr rl-margin-top-mid">未使用</div>
                      </div>
                    </div>
                  </li>
                  <li>
                    <div class="item rl-clear">
                      <div class="item-left rl-fl rl-bg-gray-xx">
                        <div class="rl-text-white"><span class="rl-text-default">¥</span><span class="large rl-margin-left-xxxxs">50</span></div>
                        <div class="rl-text-xxs rl-text-white">满500可用</div>
                      </div>
                      <div class="item-right rl-fl rl-bg-white">
                        <div><span class="rl-text-xxs">劵号:</span><span class="rl-text-xs rl-margin-left-xxxxs">643164613</span></div>
                        <div class="rl-text-xxs rl-text-gray">2017.12.07-2017.12.12</div>
                        <div class="state rl-bd-gray-xx  rl-tc rl-text-gray-xx rl-text-xxs rl-fr rl-margin-top-mid">已使用</div>
                      </div>
                    </div>
                  </li>
                  <li>
                    <div class="item rl-clear">
                      <div class="item-left rl-fl rl-bg-gray-xx">
                        <div class="rl-text-white"><span class="rl-text-default">¥</span><span class="large rl-margin-left-xxxxs">50</span></div>
                        <div class="rl-text-xxs rl-text-white">满500可用</div>
                      </div>
                      <div class="item-right rl-fl rl-bg-white">
                        <div><span class="rl-text-xxs">劵号:</span><span class="rl-text-xs rl-margin-left-xxxxs">643164613</span></div>
                        <div class="rl-text-xxs rl-text-gray">2017.12.07-2017.12.12</div>
                        <div class="state rl-bd-gray-xx  rl-tc rl-text-gray-xx rl-text-xxs rl-fr rl-margin-top-mid">已使用</div>
                      </div>
                    </div>
                  </li>
                  <li>
                    <div class="item rl-clear">
                      <div class="item-left rl-fl rl-bg-orange-sm">
                        <div class="rl-text-white"><span class="rl-text-default">¥</span><span class="large rl-margin-left-xxxxs">50</span></div>
                        <div class="rl-text-xxs rl-text-white">满500可用</div>
                      </div>
                      <div class="item-right rl-fl rl-bg-white">
                        <div><span class="rl-text-xxs">劵号:</span><span class="rl-text-xs rl-margin-left-xxxxs">643164613</span></div>
                        <div class="rl-text-xxs rl-text-gray">2017.12.07-2017.12.12</div>
                        <div class="state rl-bd-orange-sm rl-tc rl-text-orange-sm rl-text-xxs rl-fr rl-margin-top-mid">未使用</div>
                      </div>
                    </div>
                  </li>
                </ul>
              </div>
            </div>
            <div class="rl-tr rl-margin-top-default">
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
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Header from '@/components/Header.vue'
import Left from '@/components/Left.vue'
export default {
  name: 'Coupons',
  components: {
    Header,
    Left
  },
  data () {
    return {
      userState: 2,
      totalCount: 5,
      cur_page: 1,
      pagesize: 3,
      state: '' // 优惠券状态
    }
  },
  methods: {
    // 当前页码
    handleCurrentChange (val) {
      this.cur_page = val
      this.getCouponsList()
    },
    // 每页条数
    handleSizeChange (val) {
      this.pagesize = val
      this.getCouponsList()
    },
    // 改变状态
    changeState (state) {
      this.state = state
      this.getCouponsList()
    },
    // 优惠券列表
    getCouponsList () {
      this.$api.get(this, '', {page: this.cur_page, count: this.pagesize}).then(res => {
        if (res.code === 0) {
        }
      })
    }
  },
  mounted () {
  }
}
</script>

<style scoped="scoped" lang='less'>
  .user{width: 100%;}
  .user-main{width: 1024px}
  .user-right {
    width: 794px;
    .content {
      .title {
        width: 100%;
        .title-cons {
          width: 117px;
          height: 35px;
          line-height: 35px;
          background: url("../../assets/images/jiao-blue.png") center center no-repeat;
          img {
            vertical-align: -3px;
          }
        }
      }
      .case {
        .case-nav {
          margin-bottom: 1px;
          width: 100%;
          height: 45px;
          line-height: 45px;
          ul {
            li {
              display: inline-block;
              cursor: pointer;
            }
            li.current{
              color: #fff;
              background-color: #00c9dc;
            }
          }
        }
        .coupons{
          ul{
            li{
              margin-bottom: 10px;
              display: inline-block;
              width: 251px;
              border-radius: 5px;
              .item{
                .item-left{
                  padding: 25px 20px;
                  border-top-left-radius: 5px;
                  border-bottom-left-radius: 5px;
                  .large{font-size: 30px}
                }
                .item-right{
                  padding: 25px 10px 8px 10px;
                  border-top-right-radius: 5px;
                  border-bottom-right-radius: 5px;
                  .state{
                    width: 58px;
                    height: 20px;
                    line-height: 20px;
                    border-radius: 20px;
                  }
                }
              }
            }
          }
        }
      }
    }
  }
</style>
