import store from '@/store'

export function sidebarFilter(sidebarData, hasPermis) {
  if (sidebarData) {
    sidebarData = JSON.parse(JSON.stringify(sidebarData))
  }
  // 先判断是否超级管理员
  if (store.getters.userinfo.adminType === 1) return sidebarData
  if (!sidebarData) return
  sidebarData.forEach(item => {
    for (var i = 0; i < item.children.length; i++) {
      // if (!hasPermis.menus[item.children[i].permission]) {
      if (!hasPermis.menus[item.children[i].link]) {
        item.children.splice(i, 1)
        i--
      }
    }
  })
  return sidebarData.filter(arr => arr.children.length)
}
