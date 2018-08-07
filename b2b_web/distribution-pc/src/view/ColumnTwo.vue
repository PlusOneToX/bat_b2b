<template>
  <div>
    <div class="index rl-margin-zero">
      <!--公共头部-->
      <Header></Header>
      <!--主内容-->
      <div class="main rl-margin-zero rl-padding-top-default">
        <count-down v-on:start_callback="countDownS_cb(1)" v-on:end_callback="countDownE_cb(1)" :currentTime="1481450100" :startTime="1481450110" :endTime="1489999995" tipText="距离开始文字1" :tipTextEnd="'距离结束文字1'" :endText="'结束自定义文字2'" :dayTxt="'天'" :hourTxt="'小时'" :minutesTxt="'分'" :secondsTxt="'秒'"></count-down>
        <pop>
          <input v-verify length="10" err-msg="请输入正确的卡号">
        </pop>
        <el-popover
          placement="top"
          width="160"
          v-model="visible2">
          <p>这是一段内容这是一段内容确定删除吗？</p>
          <div style="text-align: right; margin: 0">
            <el-button size="mini" type="text" @click="visible2 = false">取消</el-button>
            <el-button type="primary" size="mini" @click="visible2 = false">确定</el-button>
          </div>
          <el-button slot="reference">{{visible2}}删除</el-button>
        </el-popover>
        <div class="vueBox" >
          <div class="marquee">
            <div class="marquee_title">
              <span>最新战报</span>
            </div>
            <div class="marquee_box">
              <ul class="marquee_list rl-clear" :style="{ left: -num + 'px'}" :class="{marquee_top:num}">
                <!-- 当显示最后一条的时候（num=0转换布尔类型为false）去掉过渡效果-->
                <li v-for="item in marqueeList" :key="item.id">
                  <span>{{item.name}}</span>
                  <span>在</span>
                  <span class="red"> {{item.city}}</span>
                  <span>杀敌</span>
                  <span class="red"> {{item.amount}}</span>
                  <span>万</span>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
      <!--公共底部-->
      <Footer></Footer>
    </div>
    <el-dialog class="alls" title="" :visible.sync="diyDialogVisible">
      <div class="diy-css max-height300 rl-padding-left-default rl-padding-right-default rl-clear">
        <div class="banner rl-fl">
          <el-carousel autoplay="false" loop="false" trigger="click"  interval="3000000" height="414px">
            <el-carousel-item v-for="item in images" :key="item.id">
              <div class="banner-img cursor-pointer"><img width="100%" :src="item.img" alt=""></div>
            </el-carousel-item>
          </el-carousel>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="diyDialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Footer from '@/components/Footer.vue'
import Header from '@/components/Header.vue'
import CountDown from 'vue2-countdown'
export default {
  name: 'ColumnTwo',
  components: {
    Footer,
    Header,
    CountDown
  },
  data () {
    return {
      diyDialogVisible: true,
      num: 0,
      visible2: true,
      images: [
        {
          img: require('../assets/images/help/zh/help1.png')
        },
        {
          img: require('../assets/images/help/zh/help2.png')
        },
        {
          img: require('../assets/images/help/zh/help4.png')
        }
      ],
      marqueeList: [
        {
          name: '1军',
          city: '北京',
          amount: '10'
        },
        {
          name: '2军',
          city: '上海',
          amount: '20'
        },
        {
          name: '3军',
          city: '广州',
          amount: '30'
        },
        {
          name: '4军',
          city: '重庆',
          amount: '40'
        },
        {
          name: '5军',
          city: '福建',
          amount: '50'
        }
      ]
    }
  },
  methods: {
    showMarquee (num) {
      this.marqueeList.push(this.marqueeList[0])
      var max = this.marqueeList.length
      var that = this
      setInterval(function () {
        num++
        if (num >= max) {
          num = 0
        }
        that.num = num * 30
      }, 2000)
    },
    numStrock () {
      let itemId = '19,15'
      this.$api.get(this, 'user/u/warehouse/stock/distributor', {distributorId: 378, itemIds: itemId}).then(res => {
        if (res.code === 0) {
          console.log(res)
        }
      })
    }
  },
  created: function () {
    this.showMarquee(this.num)
    this.numStrock()
    var yourString = '12,25,24,234,235'
    var result = yourString.split(',')
    for (var i = 0; i < result.length; i++) {
      console.log(result[i])
    }
  }
}
</script>

<style scoped="scoped" lang='less'>
  .index{
    width: 100%;
  }
  .main {
    width: 1024px;
  }
  .banner{
    width: 750px;
    .banner-img{
      width: 750px;
      z-index: 0;
      img{
        height: 414px;
      }
    }
    .el-carousel{
      z-index: 0;
    }
  }
  .marquee{
    width: 100%;
    height: 50px;
    align-items: center;
    color: #3A3A3A;
    background-color: aqua;
    display: flex;
    box-sizing: border-box;
  }
  .marquee_title{
    padding: 0 20px;
    height: 30px;/*关键样式*/
    font-size: 14px;
    border-right: 1px solid #d8d8d8;
    align-items: center;
  }

  .marquee_box{
    display: block;
    position: relative;
    width: 900px;
    height: 30px;/*关键样式*/
    overflow: hidden;
  }
  .marquee_list{
    width: 100%;
    height: 30px;
    display: block;
    position: absolute;
    top:0;
    left: 0;
  }
  .marquee_top{transition: left 0.5s ;}/*关键样式*/
  .marquee_list li{
    float: left;
    height: 30px;/*关键样式*/
    line-height: 30px;/*关键样式*/
    font-size: 14px;
    padding-left: 20px;
    background-color: #fff;
  }
  .marquee_list li span{
    padding:0 2px;
  }
  .red{
    color: #FF0101;
  }
</style>
