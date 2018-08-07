<template>
  <scroll-bar>
  <!-- <router-view name="sidebar"></router-view> -->
  <el-menu mode="vertical" background-color="#304156" text-color="#bfcbd9" active-text-color="#21B8CB" :default-openeds="openeds" :default-active="onRoutes">
    <el-submenu :index="item.title" :key="item.title" v-for="item in sidebar">
      <template slot="title">
        <span>{{item.title}}</span>
      </template>
      <el-menu-item-group>
        <!-- <router-link :to="{name:val.link}" :key="val.name" v-for="val in item.children"> -->
          <el-menu-item :index="val.link" :key="val.name" v-for="val in item.children" @click="linkTo(val.link, val.query)">{{val.name}}</el-menu-item>
        <!-- </router-link> -->
      </el-menu-item-group>
    </el-submenu>
  </el-menu>
  </scroll-bar>
</template>

<script>
import { mapGetters } from 'vuex'
// import SidebarItem from './SidebarItem'
import ScrollBar from '@/components/ScrollBar'
import { warehSidebarFilter, distributorSidebarFilter,sidebarFilter } from '@/utils/permissionUtils'

export default {
  components: { ScrollBar },
  computed: {
    // 作用大概是引入store中的sidebar属性对应的getter
    // mapGetters 辅助函数仅仅是将 store 中的 getter 映射到局部计算属性
    // ...mapState([''])
    // 可访问this.sidebar 得[{'title','children'},*n]
    // ...mapGetters([
    //   'sidebar'
    // ]),
    /** 点击nav上的按钮切换sidebar */
    sidebar(){
      
      let sidebar = this.$store.getters.sidebar && JSON.parse(JSON.stringify(this.$store.getters.sidebar)), // 深拷贝侧栏数据,防止篡改vuex的数据
        currPermissions = this.permissions[this.currentModule];
      // 一个模块对应一个筛选函数(这里是warehouse对应warehSidebarFilter)
      // 目前只筛选仓库的, 要添加筛选则在if()添加`||this.currentModule == 'module'`
      if(this.currentModule != 'dashboard'){
        sidebar = sidebarFilter(sidebar, this.$store.getters.hasPermis)
      }
      
      // 分销商模块
      // if(this.currentModule == 'distributor'){
      //   sidebar = distributorSidebarFilter(sidebar, currPermissions.menus)
      // }
      /** sidebar: {title:string, children:{name:string, link:string}[]} []
       *  title 汉字 name 汉字 link 路由名
       */
      return sidebar
    },
    /** return string
     *  表示当前所处模块
     */
    currentModule() {
      return this.$store.getters.currentModule
    },
    /** 当前用户的所有权限(包含所有模块的权限)
     * permissions['name']: {module: string}
     */
    permissions() {
      return this.$store.getters.permissions
    },
    onRoutes() {
      return this.$route.name
    }
  },
  data() {
    return {
      // isCollapse: true
      openeds: [
        '商品管理','商品分类管理','品牌品类管理','商品属性管理',
        '分销商设置','一级分销商','多级分销商','分销商审批','合作平台管理','用户管理','分账管理',
        '图库管理','标签管理','字体管理','定制商品管理','柔性店铺配置','卡券兑换','营销&专题活动','产品类型',
        '仓库库存','仓库设置',
        '订单管理','C端订单','异常订单','订单统计','单据管理','订单设置',
        '基本设置','平台账户','分销商账户','预存款','预存款明细','代金券','销售往来单据',
        '促销活动','优惠券','拼团','活动审批',
        '组织架构','权限管理','配送管理','消息通知','系统日志',
        '商店配置','全站设置','培训中心管理','下载中心管理',
      ],
    }
  },
  methods:{
    linkTo(link, val){
      // link come from sidebarItems.warehouse.children.link
      if (val) {
        this.$router.push({name:link, query: {type: val.type}})
      } else {
        this.$router.push({name:link})
      }
      
    }
  },
  handleOpen(key, keyPath) {
    
  },
}
</script>

<style rel="stylesheet/scss" lang="scss">

.el-menu {
    border-right: 0;
}
.el-menu-item-group {
  .el-menu-item-group__title {
    padding: 0!important;
  }
}
</style>
