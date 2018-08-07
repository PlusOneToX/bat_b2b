const getters = {
  sidebar: state => state.system.sidebar,
  token: state => state.user.token,
  userinfo: state => state.user.userinfo,
  logintime: state => state.user.logintime,
  toUserEdit: state => state.system.toUserEdit,
  roles: state => state.system.roles,
  sales: state => state.system.sales,
  brands: state => state.system.brands,
  areas: state => state.system.areas,
  specification: state => state.goods.specification, // 商品规格值
  orderId: state => state.order.orderId,
  orderDetail: state => state.order.orderDetail,
  distributions: state => state.order.distributions,
  // this.$store.getters.permissions.warehouse
  // permissions > getter.js > getters > permissions
  // permissions: state => state.user.permissions
  // user > store > modules > user.js > const user { > state > permissions}
  permissions: state => state.user.permissions,
  currentModule: state => state.system.currentModule,
  sidebarMap: state => state.system.sidebarMap,
  saleareas: state => state.distributor.saleareas,
  distributors: state => state.distributor.distributors,
  hasPermis: state => state.user.hasPermis,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  permission_routes: state => state.permission.routes,
  // eltabpaneValue: state => state.
}
export default getters
