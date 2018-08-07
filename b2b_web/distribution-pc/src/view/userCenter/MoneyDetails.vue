<template>
  <div>
    <div class="user rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState"></Header>
      <!--主内容-->
      <div class="user-main rl-clear rl-margin-zero">
        <!--公共左边-->
        <Left></Left>
        <div class="user-right rl-fr rl-bg-white rl-margin-top-default rl-padding-bottom-double">
          <div class="content">
            <h6 class="user-right-title">{{$t('UserCenter.AssetDetails')}}</h6>
            <!-- tab选项 -->
            <div class="nav rl-clear rl-margin-top-default">
              <ul class="rl-fl">
                <li
                  :class="moneyStatus === 0? 'current': ''"
                  @click="changeState(0)"
                >{{$t('UserCenter.Alls')}}</li>
                <li
                  :class="moneyStatus === 1? 'current': ''"
                  @click="changeState(1)"
                >{{$t('UserCenter.Recharge')}}</li>
                <li
                  :class="moneyStatus === 2? 'current': ''"
                  @click="changeState(2)"
                >{{$t('UserCenter.Cash')}}</li>
                <li
                  :class="moneyStatus === 3? 'current': ''"
                  @click="changeState(3)"
                >{{$t('P.Payments')}}</li>
                <li
                  :class="moneyStatus === 4? 'current': ''"
                  @click="changeState(4)"
                >{{$t('UserCenter.ExpenditureAmount')}}</li>
              </ul>
            </div>
            <!-- 列表 -->
            <div class="table">
              <table>
                <tr>
                  <th>{{$t('UserCenter.Time')}}</th>
                  <th>{{$t('UserCenter.Event')}}</th>
                  <th>{{$t('UserCenter.DepositMmount')}}</th>
                  <th>{{$t('UserCenter.ExpenditureMmount')}}</th>
                  <th>{{$t('UserCenter.AccountBalance')}}</th>
                  <th>{{$t('UserCenter.Remarkes')}}</th>
                </tr>
                <tr class="rl-bdb-gray-sm rl-bdl-gray-sm rl-bdr-gray-sm"  v-for="item in moneyDetailList" :key="item.id">
                  <td>{{item.createTime}}</td>
                  <!-- 事件 -->
                  <td>{{getStatus(item.businessType)}}</td>
                  <!-- 存入 -->
                  <td  v-if="item.changeType==1">{{$i18n.locale === 'en' && $root.currency === 'USD'?'$':'￥'}}{{$i18n.locale === 'en' && $root.currency === 'USD'?(Number(item.amount)*exchange).toFixed(2):item.amount}}</td>
                  <td v-else>￥0</td>
                  <!-- 支出金额 -->
                  <td v-if="item.changeType==2">{{$i18n.locale === 'en' && $root.currency === 'USD'?'$':'￥'}}{{$i18n.locale === 'en' && $root.currency === 'USD'?(Number(item.amount)*exchange).toFixed(2):item.amount}}</td>
                  <td v-else>￥0</td>
                  <!-- 当前金额 -->
                  <td>{{$i18n.locale === 'en' && $root.currency === 'USD'?'$':'￥'}}{{$i18n.locale === 'en' && $root.currency === 'USD'?(Number(item.afterDepositAmount)*exchange).toFixed(2):item.afterDepositAmount}}</td>
                  <!-- 备注 -->
                  <td v-if="item.remark !== ''">{{item.remark}}</td>
                  <td v-else>-</td>
                </tr>
              </table>
            </div>
          </div>
          <div class="rl-tr rl-margin-top-default">
            <el-pagination
              v-if="paginationShow"
              background
              :current-page.sync="cur_page"
              @current-change="handleCurrentChange"
              @size-change="handleSizeChange"
              layout="prev, pager, next, jumper"
              :page-size="pagesize"
              :total="totalCount"
            ></el-pagination>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import Left from "@/components/Left.vue";
import GD from "@/assets/js/globalData";
// liu  --
import {depositDetailList} from '@/apiService/api' 
export default {
  name: "MoneyDetails",
  components: {
    Header,
    Left,
  },
  data() {
    return {
      userState: 2,
      totalCount: 0,
      cur_page: 1,
      pagesize: 10,
      moneyDetailList: [],
      moneyStatus: 0, // 资金交易状态
      paginationShow: true, // 分页控制
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: "zh-RMB", // 语种
      exchange:1,//汇率
    };
  },
  methods: {
    fLangChange(value) {
      window.localStorage.setItem("bLang", value);
      this.$i18n.locale = value.split("-")[0];
    },
    getStatus(row) {
      // 事件状态
      switch (row) {
        case 1:
          return this.$t("UserCenter.Recharge");  //充值
        case 2:
          return this.$t("UserCenter.Cash");  //提现
        case 3:
           return this.$t("P.Payments");  //支付
        case 4: 
          return this.$t("UserCenter.ExpenditureAmount");  //退款
      }
    },
    // 当前页码
    handleCurrentChange(val) {
      this.cur_page = val;
      this.getMoneyDetailList();
      this.getMoneyDetailList();
    },
    // 每页条数
    handleSizeChange(val) {
      this.pagesize = val;
      this.getMoneyDetailList();
      this.getMoneyDetailList();
    },
    // 资金明细列表
    getMoneyDetailList() {
        let userId=localStorage.getItem('userId');
        let businessType='';
        if(this.moneyStatus!=0){
          businessType=this.moneyStatus;
        }
        let parmas={
          page: this.cur_page,
          size: this.pagesize,
          distributorId:userId,
          businessType: businessType,
        };
        depositDetailList(parmas).then((res) => {
          if (res.success) {
            this.moneyDetailList = res.data.list;
            this.totalCount=res.data.total;
          } 
        });
    },
   
    // 改变资金状态（交易记录）
    changeState(type) {
      this.paginationShow = false;
      this.moneyStatus = type;
      this.cur_page = 1;
      this.getMoneyDetailList();
      // this.getMoneyDetaiSum();
      this.$nextTick(function () {
        this.paginationShow = true;
      });
    },
  },
  created() {
    this.exchange=Number( localStorage.getItem('exchange')); 
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem("bLang")
      ? window.localStorage.getItem("bLang")
      : "zh-RMB";
    this.getMoneyDetailList();
    // this.getMoneyDetaiSum();
  },
};
</script>

<style scoped="scoped" lang='less'>
@import url("../../assets/less/variable.less");
.user {
  width: 100%;
}
.user-main {
  width: 1200px;
}
.user-right {
  width: 1030px;
  .user-right-title {
    padding-bottom: 10px;
    border-bottom: 1px solid @bdLightColor;
    font-size: 20px;
  }
  .content {
    padding: 24px 40px 0;
    border: 2px solid @bdLightColor;
    border-radius: 8px;
  }
  .nav {
    margin-bottom: 15px;
    width: 100%;
    height: 40px;
    line-height: 40px;
    ul {
      overflow: hidden;
      background-color: @bdLightColor;
      border-radius: 4px;
      &.navEn {
        li {
          width: auto;
          padding: 0 12px;
          font-size: 13px;
        }
      }
      li {
        float: left;
        width: 93px;
        cursor: pointer;
        font-size: 14px;
        color: @lighterBlack;
        text-align: center;
        &:hover,
        &.current {
          color: @white;
          background-color: @blue;
        }
      }
    }
  }
  .table {
    width: 100%;
    margin-bottom: 30px;
    table {
      width: 100%;
      word-wrap: break-word;
      word-break: break-all;
      border-collapse: collapse;
      tr {
        &:hover {
          background-color: @lightGrayBg;
        }
        & + tr {
          border-top: 1px dashed @bdLighterColor;
        }
        th {
          height: 30px;
          line-height: 30px;
          text-align: center;
          background-color: @bdLightColor;
          font-size: 12px;
          color: @gray;
          font-weight: normal;
        }
        td {
          height: 50px;
          text-align: center;
          font-size: 12px;
          color: @lightBlack;
        }
      }
    }
  }
}
</style>
