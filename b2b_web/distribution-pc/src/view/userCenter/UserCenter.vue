<template>
  <div>
    <div class="user rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState"></Header>
      <!--主内容-->
      <div class="user-main rl-clear rl-margin-zero">
        <!--公共左边-->
        <Left></Left>
        <div class="user-right rl-fr rl-padding-right-lllg rl-bg-white rl-padding-top-lllg rl-padding-bottom-double">
          <div class="content">
            <div class="title rl-bg-gray-xm">
              <div class="rl-padding-left-lllg title-cons rl-text-white rl-text-mid">用户中心</div>
            </div>
            <div class="cons rl-padding-top-mid rl-padding-left-lllg rl-padding-bottom-double">
              <div class="item rl-clear rl-margin-bottom-xxxxs">
                <div class="rl-fl left-info"><span class="rl-text-gray">您的账户目前总积分：</span><span class="rl-text-gray"><i class="rl-text-blue-xs">0</i>分</span></div>
                <div class="rl-fl find rl-tc rl-text-white rl-bg-blue-mm rl-text-xxs rl-padding-horizontal-xxxs cursor-pointer">查看积分历史</div>
              </div>
              <div class="item rl-clear rl-margin-bottom-xxxxs">
                <div class="rl-fl left-info"><span class="rl-text-gray">预存款余额：</span><span class="rl-text-gray"><i class="rl-text-blue-xs"> ￥</i><i class="rl-text-blue-xs">0</i>元</span><span class="rl-text-gray">(可用余额 <i class="rl-text-blue-xs"> ￥</i><i class="rl-text-blue-xs">0</i> 元)</span></div>
              </div>
              <div class="item rl-clear rl-margin-bottom-xxxxs">
                <div class="rl-fl left-info"><span class="rl-text-gray">您的未读消息：</span><span class="rl-text-gray"><i class="rl-text-blue-xs">0</i>条</span></div>
                <div class="rl-fl find rl-tc rl-text-white rl-bg-blue-mm rl-text-xxs rl-padding-horizontal-xxxs cursor-pointer">查看</div>
              </div>
            </div>
          </div>
          <div class="content">
            <div class="title rl-bg-gray-mm">
              <div class="rl-padding-left-lllg title-cons rl-text-white rl-text-mid"><img class="rl-margin-right-xxxxs" src="../../assets/images/bell.png" alt="">通知</div>
            </div>
            <div class="cons rl-padding-top-mid rl-padding-left-lllg rl-padding-bottom-double">
              <div class="item rl-clear rl-margin-bottom-xxxxs">
                <div class="rl-fl rl-margin-right-xxs rl-text-gray">2015-05-05 14:58   -   2018-05-31 00:00</div>
                <div class="rl-fl rl-text-gray">原石10000套餐版含Lightning数</div>
                <div class="rl-fr rl-text-gray rl-margin-right-mid cursor-pointer">收起</div>
              </div>
              <div class="item rl-clear rl-margin-bottom-xxxxs">
                <div class="rl-fl rl-margin-right-xxs rl-text-gray">2015-05-05 14:58   -   2018-05-31 00:00</div>
                <div class="rl-fl rl-text-gray">原石10000套餐版含Lightning数</div>
                <div class="rl-fr rl-text-gray rl-margin-right-mid cursor-pointer">收起</div>
              </div>
              <div class="item rl-clear rl-margin-bottom-xxxxs">
                <div class="rl-fl rl-margin-right-xxs rl-text-gray">2015-05-05 14:58   -   2018-05-31 00:00</div>
                <div class="rl-fl rl-text-gray">原石10000套餐版含Lightning数</div>
                <div class="rl-fr rl-text-gray rl-margin-right-mid cursor-pointer">收起</div>
              </div>
              <div class="item rl-clear rl-margin-bottom-xxxxs">
                <div class="rl-fl rl-margin-right-xxs rl-text-gray">2015-05-05 14:58   -   2018-05-31 00:00</div>
                <div class="rl-fl rl-text-gray">原石10000套餐版含Lightning数</div>
                <div class="rl-fr rl-text-gray rl-margin-right-mid cursor-pointer">收起</div>
              </div>
              <div class="item rl-clear rl-margin-bottom-xxxxs">
                <div class="rl-fl rl-margin-right-xxs rl-text-gray">2015-05-05 14:58   -   2018-05-31 00:00</div>
                <div class="rl-fl rl-text-gray">原石10000套餐版含Lightning数</div>
                <div class="rl-fr rl-text-gray rl-margin-right-mid">收起</div>
              </div>
            </div>
          </div>
          <div class="content">
            <div class="title rl-bg-gray-mm">
              <div class="rl-padding-left-lllg title-cons rl-text-white rl-text-mid">订单中心</div>
            </div>
            <div class="cons rl-padding-top-mid rl-padding-left-lllg rl-padding-bottom-double">
              <div class="item rl-clear rl-margin-bottom-xxxxs">
                <div class="rl-fl left-info"><span class="rl-text-gray">您的未付款订单总数量：</span><span class="rl-text-gray"><i class="rl-text-blue-xs">0</i>个</span></div>
                <div class="rl-fl find rl-tc rl-text-white rl-bg-blue-mm rl-text-xxs rl-padding-horizontal-xxxs cursor-pointer">查看</div>
              </div>
              <div class="item rl-clear rl-margin-bottom-xxxxs">
                <div class="rl-fl left-info"><span class="rl-text-gray">您的已发货订单总数量：</span><span class="rl-text-gray"><i class="rl-text-blue-xs">0</i>个</span></div>
                <div class="rl-fl find rl-tc rl-text-white rl-bg-blue-mm rl-text-xxs rl-padding-horizontal-xxxs cursor-pointer">查看</div>
              </div>
              <div class="item rl-clear rl-margin-bottom-xxxxs">
                <div class="rl-fl left-info"><span class="rl-text-gray">您的已作废订单总数量：</span><span class="rl-text-gray"><i class="rl-text-blue-xs">0</i>个</span></div>
                <div class="rl-fl find rl-tc rl-text-white rl-bg-blue-mm rl-text-xxs rl-padding-horizontal-xxxs cursor-pointer">查看</div>
              </div>
              <div class="item rl-clear rl-margin-bottom-xxxxs">
                <div class="rl-fl left-info"><span class="rl-text-gray">您的待付尾款订单总数量：</span><span class="rl-text-gray"><i class="rl-text-blue-xs">0</i>个</span></div>
                <div class="rl-fl find rl-tc rl-text-white rl-bg-blue-mm rl-text-xxs rl-padding-horizontal-xxxs cursor-pointer">查看</div>
              </div>
            </div>
          </div>
          <div class="content">
            <div class="title rl-bg-gray-mm">
              <div class="rl-padding-left-lllg title-cons rl-text-white rl-text-mid">分销商品相关</div>
            </div>
            <div class="cons rl-padding-top-mid rl-padding-left-lllg rl-padding-bottom-double">
              <div class="item rl-clear rl-margin-bottom-xxxxs">
                <div class="rl-fl left-info"><span class="rl-text-gray">新商品上架：</span><span class="rl-text-gray"><i class="rl-text-blue-xs">760</i>件</span></div>
                <div class="rl-fl find rl-tc rl-text-white rl-bg-blue-mm rl-text-xxs rl-padding-horizontal-xxxs cursor-pointer">我要上架</div>
              </div>
              <div class="item rl-clear rl-margin-bottom-xxxxs">
                <div class="rl-fl left-info"><span class="rl-text-gray">商品已补货：</span><span class="rl-text-gray"><i class="rl-text-blue-xs">658</i>件</span></div>
                <div class="rl-fl find rl-tc rl-text-white rl-bg-blue-mm rl-text-xxs rl-padding-horizontal-xxxs cursor-pointer">更新库存</div>
              </div>
              <div class="item rl-clear rl-margin-bottom-xxxxs">
                <div class="rl-fl left-info"><span class="rl-text-gray">商品已下架：</span><span class="rl-text-gray"><i class="rl-text-blue-xs">376</i>件</span></div>
                <div class="rl-fl find rl-tc rl-text-white rl-bg-blue-mm rl-text-xxs rl-padding-horizontal-xxxs cursor-pointer">我要下架</div>
              </div>
              <div class="item rl-clear rl-margin-bottom-xxxxs">
                <div class="rl-fl left-info"><span class="rl-text-gray">商品已缺货：</span><span class="rl-text-gray"><i class="rl-text-blue-xs">130</i>件</span></div>
                <div class="rl-fl find rl-tc rl-text-white rl-bg-blue-mm rl-text-xxs rl-padding-horizontal-xxxs cursor-pointer">更新库存</div>
              </div>
              <div class="item rl-clear rl-margin-bottom-xxxxs">
                <div class="rl-fl left-info"><span class="rl-text-gray">预售中商品：</span><span class="rl-text-gray"><i class="rl-text-blue-xs">130</i>件</span></div>
                <div class="rl-fl find rl-tc rl-text-white rl-bg-blue-mm rl-text-xxs rl-padding-horizontal-xxxs cursor-pointer">查看</div>
              </div>
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
import {loginOut} from '@/assets/js/common.js'
export default {
  name: 'UserCenter',
  components: {
    Header,
    Left
  },
  data () {
    return {
      userState: 2
    }
  },
  mounted () {
    this.$api.get(this, 'user/u/user').then(res => {
      if (res.code === 0) {
        this.$message({
          message: '请求成功',
          type: 'success',
          duration: 1000
        })
      } else if (res.code === 3) {
        
      }
    })
  }
}
</script>

<style scoped="scoped" lang='less'>
  .user{width: 100%;}
  .user-main{width: 1024px}
  .user-right{
    width: 824px;
    .content{
      .title{
        width: 100%;
        .title-cons{
          width: 105px;
          height: 35px;
          line-height: 35px;
          background: url("../../assets/images/jiao-gray.png") center center no-repeat;
          img{
            vertical-align: -3px;
          }
        }
      }
      .cons{
        .item{
          .left-info{width: 305px}
          .find{
            height: 20px;
            line-height: 20px;
            border-radius: 5px;
          }
        }
      }
    }
  }
</style>
