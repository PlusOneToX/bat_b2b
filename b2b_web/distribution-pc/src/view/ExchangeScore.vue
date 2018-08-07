<template>
  <div>
    <div class="index rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState"></Header>
      <!--主内容-->
      <div class="main rl-padding-bottom-default rl-bdb-gray-xs rl-margin-zero">
        <div class="score rl-text-xxs rl-padding-top-default rl-padding-bottom-default rl-padding-left-mid">您的当前剩余积分：<span class="rl-text-blue-mm rl-text-bold">12999</span>分</div>
        <div class="shop-list">
          <ul>
            <li>
              <div class="product">
                <!--<div class="img"><img src="../../src/assets/images/product2.png" alt=""></div>-->
                <div class="rl-text-xxss rl-text-gray rl-margin-top-xxxs rl-tl"></div>
                <div class="rl-clear rl-margin-top-xxxxs">
                  <div class="rl-fl rl-text-red-sm rl-text-bold"><span class="rl-text-xxss">所需积分：</span><span class="rl-text-xxss">1000</span>分</div>
                  <div class="rl-fr"><span class="rl-text-blue-mm rl-text-xxss rl-text-bold">销售价：</span><span class="rl-text-xxs rl-text-gray">￥12.5</span></div>
                </div>
                <div class="sum rl-clear rl-margin-top-xxxs">
                  <span class="rl-fl rl-margin-right-xxxxs rl-text-xxs">兑换数量</span>
                  <div class="rl-fl"><BuySum></BuySum></div>
                  <div class="rl-fr rl-text-xxs exchange rl-tc rl-text-white cursor-pointer rl-bg-blue-mm">立即兑换</div>
                </div>
              </div>
            </li>
            <li>
              <div class="product">
                <!--<div class="img"><img src="../../src/assets/images/product2.png" alt=""></div>-->
                <div class="rl-text-xxss rl-text-gray rl-margin-top-xxxs rl-tl"></div>
                <div class="rl-clear rl-margin-top-xxxxs">
                  <div class="rl-fl rl-text-red-sm rl-text-bold"><span class="rl-text-xxss">所需积分：</span><span class="rl-text-xxss">1000</span>分</div>
                  <div class="rl-fr"><span class="rl-text-blue-mm rl-text-xxss rl-text-bold">销售价：</span><span class="rl-text-xxs rl-text-gray">￥12.5</span></div>
                </div>
                <div class="sum rl-clear rl-margin-top-xxxs">
                  <span class="rl-fl rl-margin-right-xxxxs rl-text-xxs">兑换数量</span>
                  <div class="rl-fl"><BuySum></BuySum></div>
                  <div class="rl-fr rl-text-xxs exchange rl-tc rl-text-white cursor-pointer rl-bg-blue-mm">立即兑换</div>
                </div>
              </div>
            </li>
            <li>
              <div class="product">
                <!--<div class="img"><img src="../../src/assets/images/product2.png" alt=""></div>-->
                <div class="rl-text-xxss rl-text-gray rl-margin-top-xxxs rl-tl"></div>
                <div class="rl-clear rl-margin-top-xxxxs">
                  <div class="rl-fl rl-text-red-sm rl-text-bold"><span class="rl-text-xxss">所需积分：</span><span class="rl-text-xxss">1000</span>分</div>
                  <div class="rl-fr"><span class="rl-text-blue-mm rl-text-xxss rl-text-bold">销售价：</span><span class="rl-text-xxs rl-text-gray">￥12.5</span></div>
                </div>
                <div class="sum rl-clear rl-margin-top-xxxs">
                  <span class="rl-fl rl-margin-right-xxxxs rl-text-xxs">兑换数量</span>
                  <div class="rl-fl"><BuySum></BuySum></div>
                  <div class="rl-fr rl-text-xxs exchange rl-tc rl-text-white cursor-pointer rl-bg-blue-mm">立即兑换</div>
                </div>
              </div>
            </li>
            <li>
              <div class="product">
                <!--<div class="img"><img src="../../src/assets/images/product2.png" alt=""></div>-->
                <div class="rl-text-xxss rl-text-gray rl-margin-top-xxxs rl-tl"></div>
                <div class="rl-clear rl-margin-top-xxxxs">
                  <div class="rl-fl rl-text-red-sm rl-text-bold"><span class="rl-text-xxss">所需积分：</span><span class="rl-text-xxss">1000</span>分</div>
                  <div class="rl-fr"><span class="rl-text-blue-mm rl-text-xxss rl-text-bold">销售价：</span><span class="rl-text-xxs rl-text-gray">￥12.5</span></div>
                </div>
                <div class="sum rl-clear rl-margin-top-xxxs">
                  <span class="rl-fl rl-margin-right-xxxxs rl-text-xxs">兑换数量</span>
                  <div class="rl-fl"><BuySum></BuySum></div>
                  <div class="rl-fr rl-text-xxs exchange rl-tc rl-text-white cursor-pointer rl-bg-blue-mm">立即兑换</div>
                </div>
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
import Footer from '@/components/Footer.vue'
import Header from '@/components/Header.vue'
import BuySum from '@/components/BuySum.vue'
export default {
  name: 'ExchangeScore',
  components: {
    Footer,
    Header,
    BuySum
  },
  data () {
    return {
      userState: 2,
      totalCount: 15,
      cur_page: 1,
      pagesize: 3
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
  .index{
    width: 100%;
  }
  .main{
    width: 1024px;
    .shop-list{
      overflow: hidden;
      ul{
        li{
          margin-bottom: 30px;
          float: left;
          width: 25%;
        }
        li .product{
          padding-left: 15px;
          width: 222px;
          img{
            width: 100%;
          }
          .sum{
            span{
              line-height: 25px;
            }
            .exchange{
              margin-top: 3px;
              width: 64px;
              height: 20px;
              line-height: 20px;
              border-radius: 5px;
            }
          }
        }
      }
    }
  }
</style>
