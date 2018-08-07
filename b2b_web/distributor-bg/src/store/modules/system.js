/*
* @Author: 陈良顺
* @Date:   2018-03-16 15:44:50
 * @Last Modified by: li.tian
 * @Last Modified time: 2018-06-Th 04:20:41
*/

import { sidebarItems } from '@/views/layout/components/Sidebar/sideBarMap'
import { sidebarFilter } from '../../utils/permissionUtils'
import store from '@/store'
// 调试用Mock数据
const defaultPermissons = {
  warehouse: {
    actions: [
      {
        name: '库存调整',
        module: 'warehousestock-adjust'
      },
      {
        name: '库存查看',
        module: 'warehousestock-get'
      },
      {
        name: '库存预留',
        module: 'warehousestock-reserved'
      },
      {
        name: '库存导出',
        module: 'warehousestock-export'
      },
      {
        name: '库存调拨',
        module: 'warehousestock-allocate'
      },
      {
        name: '新增',
        module: 'warehouse-post'
      },
      {
        name: '编辑',
        module: 'warehouse-put'
      },
      {
        name: '查看',
        module: 'warehouse-get'
      },
      {
        name: '删除',
        module: 'warehouse-delete'
      }
      /** 仓库-系统 */
    ],
    menus: [
      // {
      //   name: '库存调拨',
      //   module: 'warehousestockallocate-add'
      // },
      {
        name: '库存调拨列表',
        module: 'warehousestockallocate-detail'
      },
      {
        name: '仓库列表',
        module: 'warehouse-list'
      },
      {
        name: '添加仓库',
        module: 'warehouse-add'
      },
      {
        name: '调整审批',
        module: 'warehousestock-check'
      },
      {
        name: '库存调整',
        module: 'warehousestock-adjust'
      },
      {
        name: '库存预留',
        module: 'warehousestock-reserved'
      },
      {
        name: '调整审批',
        module: 'warehousestock-adjust'
      },
      {
        name: '库存明细',
        module: 'warehousestock-detail'
      },
      {
        name: '商品库存列表',
        module: 'warehousestock-list'
      }
    ]
  }
}
let routeName = ''

// nav状态初始值，如果路由不再使用哈希模式，则把location.hash替换为location.pathname
if (location.hash.match(/\/([a-z]|[A-Z])+\//, '')) {
  routeName = location.hash.match(/\/([a-z]|[A-Z])+\//, '')[0].replace(/\//g, '')
} else if (location.hash.match(/\/([a-z]|[A-Z])+/, '')) {
  routeName = location.hash.match(/\/([a-z]|[A-Z])+/, '')[0].replace(/\//g, '')
}

const system = {
  state: {
    toUserEdit: '',
    sales: '',
    brands: '',
    roles: '',
    // 侧栏中的数据
    sidebarMap: sidebarItems,
    sidebar: [],
    currentModule: routeName,
    permissions: defaultPermissons, // 这里的权限只是测试用,正牌的在user里
    areas: []
  },
  mutations: {
    GET_ALL_AREAS: (state, areas) => {
      state.areas = areas
    },
    GET_USERLIST_INFO: (state, info) => {
      state.toUserEdit = info
    },
    GET_ALL_SALES: (state, sales) => {
      state.sales = sales
    },
    GET_ALL_ROLES: (state, roles) => {
      state.roles = roles
    },
    GET_ALL_BRANDS: (state, brands) => {
      state.brands = brands
    },
    // 侧栏
    SET_SIDEBARMAP: (state, sidebarMap) => {
      state.sidebarMap = sidebarMap
    },
    // 侧栏
    SET_SIDEBAR: (state, sidebar) => {
      state.sidebar = sidebar
    },
    /** 切换当前layout和侧栏 payLoad: string */
    CHANGE_MODULE: (state, payLoad) => {
      state.currentModule = payLoad
    },
    /**
     * 用权限的menus部分过滤sidebarMap的项目
     * @param { {title:string, children:{name:string, link:string}[]} [] } payLoad
     */
    FILTER_SIDEBARMAP: ({ sidebarMap }, payLoad) => {
      /** @var {string} module 模块名: distributor, goods, warehouse等等  */
      // if (payLoad['warehouse']) {
      //   sidebarMap['warehouse'] = warehSidebarFilter(sidebarMap['warehouse'], payLoad['warehouse'].menus)
      // }
      // sidebarMap['dashboard'] = sidebarFilter(sidebarMap[''], store.getters.hasPermis)
      sidebarMap['warehouse'] = sidebarFilter(sidebarMap['warehouse'], store.getters.hasPermis)
      sidebarMap['goods'] = sidebarFilter(sidebarMap['goods'], store.getters.hasPermis)
      sidebarMap['distributor'] = sidebarFilter(sidebarMap['distributor'], store.getters.hasPermis)
      sidebarMap['order'] = sidebarFilter(sidebarMap['order'], store.getters.hasPermis)
      sidebarMap['material'] = sidebarFilter(sidebarMap['material'], store.getters.hasPermis)
      sidebarMap['financial'] = sidebarFilter(sidebarMap['financial'], store.getters.hasPermis)
      sidebarMap['marketingPromotion'] = sidebarFilter(sidebarMap['marketingPromotion'], store.getters.hasPermis)
      sidebarMap['system'] = sidebarFilter(sidebarMap['system'], store.getters.hasPermis)
      sidebarMap['storeLayout'] = sidebarFilter(sidebarMap['storeLayout'], store.getters.hasPermis)
    }
  },
  actions: {
    // router -> string like 'warehouse' || 'system'
    // 1. 点击导航栏，发出action
    // 2. mutation SET_SIDEBAR 切换store中的sidebar数据
    // 3. 侧栏响应store的改变
    getSideBarItem({ commit, state }, router) {
      // console.log(sidebarItems)
      /**
       * 数组中取出的含有单个模块路由信息的object{'title','children'}
       * @type {object}
       */
      const sidebar = state.sidebarMap[router]
      // mutation SET_SIDEBAR
      commit('SET_SIDEBAR', sidebar)
    },
    // // 获取权限中的侧栏数据
    // getSideBarItems({ commit, state }, hasPermis) {
    //   // 过滤菜单权限
    //   const sidebar = sidebarFilter(state.sidebarMap, hasPermis)
    //   commit('SET_SIDEBAR', sidebar)
    // },
    changeModule({ commit }, payLoad) {
      commit('CHANGE_MODULE', payLoad)
    },
    fiterSideBarMap({ commit }, payLoad) {
      commit('FILTER_SIDEBARMAP', payLoad)
    }
  }
}

export default system
