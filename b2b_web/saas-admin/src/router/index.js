import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    noCache: true                if set true, the page will no be cached(default is false)
    affix: true                  if set true, the tag will affix in the tags-view
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/auth-redirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error-page/401'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: { title: '首页', icon: 'el-icon-s-home', affix: true }
      }
    ]
  }
]

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
export const asyncRoutes = [
  // 租户管理 tenant
  {
    path: '/tenant',
    component: Layout,
    redirect: 'noRedirect',
    name: 'Tenant',
    meta: {
      title: '租户管理',
      icon: 'el-icon-s-custom'
    },
    children: [
      {
        path: 'list',
        component: () => import('@/views/tenant/index'),
        name: 'TenantList',
        meta: { title: '租户列表', noCache: true }
      },
      {
        path: 'detail',
        component: () => import('@/views/tenant/detail'),
        name: 'TenantDetail',
        meta: { title: '租户详情', noCache: true },
        hidden: true
      },
      {
        path: 'setting',
        component: () => import('@/views/tenant/setting/index'),
        name: 'TenantSetting',
        meta: { title: '租户配置', noCache: true },
        hidden: true
      },
      {
        path: 'setting/common',
        component: () => import('@/views/tenant/setting/commonSetting'),
        name: 'CommonSetting',
        meta: { title: '基础信息配置', noCache: true },
        hidden: true
      },
      {
        path: 'setting/db',
        component: () => import('@/views/tenant/setting/dbSetting'),
        name: 'DBSetting',
        meta: { title: '数据库配置', noCache: true },
        hidden: true
      },
      {
        path: 'setting/diy',
        component: () => import('@/views/tenant/setting/diySetting'),
        name: 'DiySetting',
        meta: { title: '定制信息配置', noCache: true },
        hidden: true
      },
      {
        path: 'setting/erp',
        component: () => import('@/views/tenant/setting/erpSetting'),
        name: 'ErpSetting',
        meta: { title: 'ERP信息配置', noCache: true },
        hidden: true
      },
      {
        path: 'setting/oss',
        component: () => import('@/views/tenant/setting/ossSetting'),
        name: 'OssSetting',
        meta: { title: '文件存储配置', noCache: true },
        hidden: true
      },
      {
        path: 'setting/sms',
        component: () => import('@/views/tenant/setting/smsSetting'),
        name: 'SmsSetting',
        meta: { title: '短信配置', noCache: true },
        hidden: true
      }
    ]
  },
  // 系统管理
  {
    path: '/system',
    component: Layout,
    redirect: 'noRedirect',
    name: 'System',
    meta: {
      title: '系统设置',
      icon: 'el-icon-s-tools'
    },
    children: [
      {
        path: 'admin/list',
        component: () => import('@/views/system/admin/index'),
        name: 'AdminList',
        meta: { title: '管理员列表', noCache: true }
      },
      {
        path: 'admin/detail',
        component: () => import('@/views/system/admin/detail'),
        name: 'AdminDetail',
        meta: { title: '管理员详情', noCache: true },
        hidden: true
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
