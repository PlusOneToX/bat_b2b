<template>
  <div class="navbar">
    <div class="nav" v-if="computerShow">
      <el-tabs v-model="activeName" class="nav_header" @tab-click="handleClick">
        <el-tab-pane :label="item.title" :key="item.default" v-for="item in navlist" :name="item.default" :value="item.default"> </el-tab-pane>
      </el-tabs>
    </div>

    <div class = "avatar-content" v-if="computerShow">
      <!-- <el-button class= "store-layout" type="primary" @click="storeLayoutClick()">{{ storeLayout.title }}</el-button> -->
      <el-dropdown class="avatar-container" trigger="click" >
        <div class="avatar-wrapper">
          <img :src="header_img" class="user-avatar">
          <i class="el-icon-caret-bottom"></i>
        </div>
        <el-dropdown-menu class="user-dropdown" slot="dropdown">
          <el-dropdown-item>
            <img :src="header_img" class="user-avatar"><span class="user-name">{{userinfo.userName}}</span>
          </el-dropdown-item>
          <router-link class="inlineBlock" to="/">
            <!-- <el-dropdown-item divided>
              首页
            </el-dropdown-item> -->
            <el-dropdown-item divided>
              <span @click="logout" style="display:block;">
                退出
              </span>
            </el-dropdown-item>
          </router-link>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
import header_img from '@/assets/images/header.gif'
export default {
  data() {
    return {
      movementShow: false,
      computerShow: true,
      header_img,
      navlist: [
        {
          title: '商品管理',
          router: 'goods',
          default:'goodslist'
        },
        {
          title: '客户管理',
          router: 'distributor',
          default: 'distributorcooperating'
        },
        {
          title: '柔性管理',
          router: 'material',
          default: 'pictureList'
        },
        {
          title: '仓库',
          router: 'warehouse',
          default:'stockList'
        },
        {
          title: '订单',
          router: 'order',
          default:'orderList'
        },
        {
          title: '财务',
          router: 'financial',
          default:'exchangeRate'
        },
        {
          title: '营销推广',
          router: 'marketingPromotion',
          default: 'salesPromotion'
        },
        {
          title: '系统',
          router: 'system',
          default:'saleOrganization'
        },
        {
          title: '商店配置',
          router: 'storeLayout',
          default: 'homePagePromotion'
        }
      ],
      navindex: 0,
      activeName: this.$route.name,
      style: {
        'transform': 'translate(15px,0)',
      }
    }
  },
  mounted() {
    //用户心跳
    // setInterval(() => {
    //   this.$api.post(this, 'admin/u/heartbeat')
    // }, 60000);
    this.$store.dispatch('updateDistributors');
    // 刷新时，根据url刷新sidebar
    if(this.$route.path.match(/^\/([a-z]|[A-Z])+\//, '')){
      let routeName = this.$route.path.match(/^\/([a-z]|[A-Z])+\//, '')[0].replace(/\//g, '')
      if(routeName){
        this.$store.dispatch('getSideBarItem',routeName);
      }
    }
  },
  computed: {
    ...mapGetters(['userinfo', ]),
    /** 是否超级管理员 */
    isSuperAdmin() {
      return this.$store.getters.userinfo.adminType === 1
    },
    sideBarMap(){
      return this.$store.getters.sidebarMap
    },
  },
  watch: {
    sideBarMap: {
      // 检测sideBarMap,如果不是超管,一旦有变则检测空侧栏
      handler(){
        !this.isSuperAdmin && this.deleteEmpty()
      },
      deep: true // 深度监控对象上的属性
    },
    $route: {
      // 监听路由变化
      handler(val){
        if(val.path.match(/^\/([a-z]|[A-Z])+\//, '')){
          // 匹配当前路由在哪个 navbar
          let routeName = val.path.match(/^\/([a-z]|[A-Z])+\//, '')[0].replace(/\//g, '')
          if (routeName) {
            // 获取当前 navbar 侧边栏
            this.$store.dispatch('getSideBarItem',routeName);
            // navbar 选中
            this.navlist.forEach((nav) => {
              if (nav.router == routeName) {
                this.activeName = nav.default
              }
            })
          }
        }
      },
      deep: true
    }
  },
  methods: {
    // 判断侧栏权限，如果没有，就删除单个导航栏
    deleteEmpty(){
      for (const module in this.sideBarMap) {
        if (this.sideBarMap[module].length == 0){
          // 删除没有侧栏内容(空数组)的按钮
          /** @var { {title:string, router:string}[] } navlist */
          for (let i = 0, len = this.navlist.length; i < len; i++) {
            if (this.navlist[i].router == module) {
              this.navlist.splice(i,1)
              break
            }
          }
        } else {
          for (let i = 0, len = this.navlist.length; i < len; i++) { // 定位缺省页面
            if (this.navlist[i].router == module) {
              this.navlist[i].default = this.sideBarMap[module][0].children[0].link
              break
            }
          }
        }
      }
      this.navlist = this.navlist.filter(item => item)
      this.$store.dispatch('getSideBarItem',this.navlist[0].router)
      this.$store.dispatch('changeModule',this.navlist[0].router)
      this.$router.push({name:this.navlist[0].default})
    },
    logout() {
      this.$store.dispatch('LogOut').then(() => {
        location.reload() // 为了重新实例化vue-router对象 避免bug
      })
    },
    handleClick(tab, event) {  // 获取左侧菜单栏
      this.$store.dispatch('getSideBarItem',this.navlist[tab.index].router)
      this.$store.dispatch('changeModule',this.navlist[tab.index].router)
      this.$router.push({name:this.navlist[tab.index].default})
    },
    storeLayoutClick() {
      this.activeName=""
      this.$store.dispatch('getSideBarItem',this.storeLayout.router)
      this.$router.push({name:this.storeLayout.default})
    }
  }
}

</script>
<style lang="scss" rel="stylesheet/scss" scoped>
.navbar {
  // color: rgba(36, 180, 176, 1);
  position: relative;
  height: 50px;
  line-height: 50px;
  border-radius: 0px !important;
  border-bottom: 1px solid #ccc;
  .hamburger-container {
    line-height: 58px;
    height: 50px;
    float: left;
    padding: 0 10px;
  }
  .nav {
    padding-right: 150px;
    .nav_header {
      padding: 0 20px;
    }
    /deep/.el-tabs__header {
      margin: 0;
    }
    /deep/.el-tabs__nav-wrap::after {
      height: 0;
    }
  }
  .el-tabs__item.is-active{
    color: rgba(36, 180, 176, 1) !important;
  }
  // .nav .nav-line {
  //   border: 3px solid rgba(36, 180, 176, 1);
  //   position: absolute;
  //   top: 90%;
  //   width: 60px;
  //   -webkit-transition: -webkit-transform 0.5s;
  //   transition: transform 0.5s;
  //   -webkit-transition-timing-function: cubic-bezier(1, 0.01, 0, 1.22);
  //   transition-timing-function: cubic-bezier(1, 0.01, 0, 1.22);
  // }
  // .screenfull {
  //   position: absolute;
  //   right: 90px;
  //   top: 16px;
  //   color: red;
  // }
  .avatar-content{
    position: absolute;
    top: 0;
    right: 0;
    width: 150px;
    height: 100%;
    .store-layout{
      height: 30px;
      padding-top: 5px;
      padding-bottom: 5px;
      padding-left: 10px;
      padding-right: 10px;
      position: absolute;
      margin: 10px;
      right: 150px;
      top: 0px;
      font-size: 12px;
      background-color: #21b8cb;
      color: white;
      border-radius: 20px
    }
    .avatar-container {
      height: 50px;
      position: absolute;
      right: 35px;
      top: 0px;
      .avatar-wrapper {
        cursor: pointer;
        margin-top: 5px;
        position: relative;
        .user-avatar {
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }
        .el-icon-caret-bottom {
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 10px;
}

.user-name {
  display: inline-block;
  position: relative;
  top: -15px;
  margin-left: 10px;
}

</style>
