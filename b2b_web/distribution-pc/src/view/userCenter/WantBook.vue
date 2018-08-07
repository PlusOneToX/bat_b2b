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
            <div class="title rl-bg-gray-mm">
              <div class="rl-padding-left-lllg title-cons rl-text-white rl-text-mid">{{$t('UserCenter.Shortage')}}</div>
            </div>
            <div class="user-table rl-margin-top-default">
              <table>
                <tr class="rl-bg-blue-xs">
                  <th>{{$t('ShopCarts.ItemNo')}}</th>
                  <th>{{$t('ShopCarts.Spe')}}</th>
                  <th>{{$t('ShopCarts.Quantity')}}</th>
                  <th>{{$t('UserCenter.RegistrationTime')}}</th>
                  <th>{{$t('UserCenter.ProcessingRemarks')}}</th>
                  <th>{{$t('UserCenter.Operation')}}</th>
                </tr>
                <tr class="rl-bdb-gray-sm rl-bdl-gray-sm rl-bdr-gray-sm" v-for="goods in losegoodsList" :key="goods.id">
                  <td>{{goods.goodsLoseErpNo}}</td>
                  <td>{{goods.specificationValueName}}</td>
                  <td>{{goods.needNumber}}</td>
                  <td>{{new Date(parseInt(goods.createTime)).toLocaleDateString()}}</td>
                  <td>{{goods.remark}}</td>
                  <td><span class="delet rl-bg-orange-sm rl-tc rl-text-white rl-margin-zero cursor-pointer"><el-button type="text" @click="deleteLosegoods(goods.id)">{{$t('ShopCarts.Delete')}}</el-button></span></td>
                </tr>
              </table>
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
import {loginOut} from '@/assets/js/common.js'
import GD from '@/assets/js/globalData'
export default {
  name: 'WantBook',
  components: {
    Header,
    Left
  },
  data () {
    return {
      userState: 2,
      totalCount: 0,
      cur_page: 1,
      pagesize: 10,
      losegoodsList: [],
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: 'zh-RMB' // 语种
    }
  },
  methods: {
    fLangChange (value) {
      window.localStorage.setItem('bLang', value);
      this.$i18n.locale = value.split('-')[0];
    },
    // 当前页码
    handleCurrentChange (val) {
      this.cur_page = val
      this.getLosegoodsList()
    },
    // 每页条数
    handleSizeChange (val) {
      this.pagesize = val
      this.getLosegoodsList()
    },
    // 缺货登记删除
    deleteLosegoods (id) {
      this.$confirm(this.$t('UserCenter.OperationDeletedProductShortage'), this.$t('P.Prompt'), {
        confirmButtonText: this.$t('Message.Confirm'),
        cancelButtonText: this.$t('P.Cancel'),
        type: 'warning'
      }).then(() => {
        this.$api.delete(this, 'user/u/losegoods', {ids: Number(id)}).then(res => {
          if (res.code === 0) {
            this.$message({
              type: 'success',
              message: this.$t('ShopCarts.SuccessfullyDeleted')
            })
            this.getLosegoodsList()
            this.getLosegoodsListSum()
          } else if (res.code === 3) {
           
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: this.$t('P.Canceled')
        })
      })
    },
    // 缺货登记列表
    getLosegoodsList () {
      var myDate = new Date()
      this.$api.get(this, 'user/u/losegoods/list?' + myDate.getMinutes() + myDate.getSeconds(), {page: this.cur_page, count: this.pagesize}).then(res => {
        if (res.code === 0) {
          this.losegoodsList = res.userLoseGoods
        } else if (res.code === 3) {
          
        }
      })
    },
    // 缺货登记列表总数查询
    getLosegoodsListSum () {
      var myDate = new Date()
      this.$api.get(this, 'user/u/losegoods/count?' + myDate.getMinutes() + myDate.getSeconds()).then(res => {
        if (res.code === 0) {
          this.totalCount = res.count
        }
      })
    }
  },
  created () {
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem('bLang') ? window.localStorage.getItem('bLang') : 'zh-RMB';
    this.getLosegoodsList()
    this.getLosegoodsListSum()
  }
}
</script>

<style scoped="scoped" lang='less'>
  .user{width: 100%;}
  .user-main{width: 1190px}
  .user-right {
    width: 960px;
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
      .user-table{
        table{
          width: 960px;
          word-wrap: break-word;
          word-break: break-all;
          border-collapse: collapse;
          tr{
            th{
              height: 45px;
              line-height: 45px;
              width: 131px;
            }
            td{
              height: 80px;
              line-height: 80px;
              text-align: center;
              .delet{
                display: block;
                width: 68px;
                height: 35px;
                line-height: 35px;
                border-radius: 5px;
                .el-button--text{
                  color:#fff;
                  width: 68px;
                  height: 35px;
                }
              }
            }
          }
        }
      }
    }
  }
</style>
