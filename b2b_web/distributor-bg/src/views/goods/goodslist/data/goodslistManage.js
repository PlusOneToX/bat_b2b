/*
 * @Author: 陈良顺
 * @Date:   2018-03-31 11:18:20
 * @Last Modified by: li.tian
 * @Last Modified time: 2018-04-Fr 05:01:41
 */
import api from '@/api/allApi'
import url from '@/api/allUrl'
// 声明存放将要拼凑的数据
let category, brand, needCheckFlag

// 获取未冻结商品
export function getGoodsList(vue, params) {
  params.freezeStatus = 1
  if (!category) { // if没有数据，就发出请求数据
    // category = vue.$api.get(vue, 'admin/u/po/category/list', { page: 1, count: 10000 }).then(res => { // 分类列表
    //   if (res.code === 0) {
    //     return res.categorys
    //   }
    category = vue.$api.get(vue, url.getClassifyPoList, { page: 1, size: 10000 }).then(res => { // 分类列表
      const ary = []
      if (res.success) {
        res.data.list.forEach(item => {
          ary.push(item)
        })
        ary.forEach((item) => {
          if (item.subClassifies && item.subClassifies.length > 0) {
            item.subClassifies.forEach(val => {
              ary.push(val)
            })
          }
        })
        ary.sort((a, b) => {
          return a.sort - b.sort > 0
        })
      }
      return ary
    })
  }
  if (!brand) {
    // brand = vue.$api.get(vue, api.getBrandList, { page: 1, size: 10000 }).then(res => { // 品牌列表
    brand = vue.$api.get(vue, url.getBrandList, { page: 1, size: 10000 }).then(res => { // 品牌列表
      if (res.success) {
        return res.data.list
      }
    })
  }
  // var goods = vue.$api.get(vue, api.getGoodsList, params).then(res => { // 商品列表
  var goods = vue.$api.get(vue, url.getGoodsList, params).then(res => { // 商品列表
    if (res.success) {
      needCheckFlag = res.needCheckFlag
      return res.data
      // return res.goods
    }
  })

  var count = 0

  // var count = vue.$api.get(vue, url.getGoodsList, params).then(res => {
  //   if (res.success) {
  //     return res.data.total
  //   }
  // })
  // var count = vue.$api.get(vue, 'admin/u/p/goods/count', params).then(res => { // 商品列表总数
  //   if (res.code === 0) {
  //     return res.count
  //   }
  // })
  // category: 分类列表, brand: 品牌列表, goods: 商品列表, count: 商品列表总数
  return Promise.all([category, brand, goods, count]).then(res => { // promise.all 将多个数据返回成一个来使用
    res[2].list.forEach(item1 => { // res[2]: goods商品名称(商品列表)  item1: category分类名称(分类列表)
      let ary = []
      // console.log(res[0])
      res[0].filter(item2 => { // 分类名称(分类列表)
        if (item1.classifyIds) {
          // return item1.classifyIds.splitnum(',').indexOf(item2.id) >= 0 // 查找到item2(商品列表)的id，并用逗号隔开 splitnum:外部导入
          return item1.classifyIds.join(',').indexOf(item2.id) >= 0
        }
      }).forEach(item3 => {
        ary.push(item3.name)
      })
      ary = ary.join(',')
      item1.categoryName = ary
      res[1].forEach(item4 => {
        if (item4.id === item1.brandId) {
          item1.brandName = item4.name
        }
      })
    })
    category = res[0]
    brand = res[1]
    return {
      tableData: res[2],
      // total: res[2].total,
      // total: res[3],
      needCheckFlag: needCheckFlag
    }
  })
}

// 获取未冻结商品
export function getGoodsPoList(vue, params) {
  params.freezeStatus = 1
  if (!category) { // if没有数据，就发出请求数据
    // category = vue.$api.get(vue, 'admin/u/po/category/list', { page: 1, count: 10000 }).then(res => { // 分类列表
    //   if (res.code === 0) {
    //     return res.categorys
    //   }
    // })
    category = vue.$api.get(vue, url.getClassifyPoList, { page: 1, size: 10000 }).then(res => { // 分类列表
      const ary = []
      if (res.success) {
        res.data.list.forEach(item => {
          ary.push(item)
        })
        ary.forEach((item) => {
          if (item.subClassifies && item.subClassifies.length > 0) {
            item.subClassifies.forEach(val => {
              ary.push(val)
            })
          }
        })
        ary.sort((a, b) => {
          return a.sort - b.sort > 0
        })
      }
      return ary
    })
  }
  if (!brand) {
    // brand = vue.$api.get(vue, 'admin/u/po/brand/list', { page: 1, count: 10000 }).then(res => { // 品牌列表
    brand = vue.$api.get(vue, url.getBrandList, { page: 1, size: 10000 }).then(res => { // 品牌列表
      if (res.success) {
        return res.data.list
      }
    })
  }
  // var goods = vue.$api.get(vue, 'admin/u/po/goods/list', params).then(res => { // 商品列表
  var goods = vue.$api.get(vue, url.getGoodsList, params).then(res => { // 商品列表
    if (res.success) {
      needCheckFlag = res.needCheckFlag
      return res.data
      // return res.goods
    }
  })
  var count = 0
  // var count = vue.$api.get(vue, 'admin/u/po/goods/count', params).then(res => { // 商品列表总数
  //   if (res.code === 0) {
  //     return res.count
  //   }
  // })
  // category: 分类列表, brand: 品牌列表, goods: 商品列表, count: 商品列表总数
  return Promise.all([category, brand, goods, count]).then(res => { // promise.all 将多个数据返回成一个来使用
    res[2].list.forEach(item1 => { // res[2]: goods商品名称(商品列表)  item1: category分类名称(分类列表)
      let ary = []
      res[0].filter(item2 => { // 分类名称(分类列表)
        if (item1.categoryIds) {
          // 查找到item2(商品列表)的id，并用逗号隔开 splitnum:外部导入
          // return item1.categoryIds.splitnum(',').indexOf(item2.id) >= 0
          return item1.categoryIds.join(',').indexOf(item2.id) >= 0
        }
      }).forEach(item3 => {
        ary.push(item3.name)
      })
      ary = ary.join(',')
      item1.categoryName = ary
      if (res[1].length > 0) {
        res[1].forEach(item4 => {
          if (item4.id === item1.brandId) {
            item1.brandName = item4.name
          }
        })
      }
    })
    category = res[0]
    brand = res[1]
    return {
      tableData: res[2],
      // total: res[3],
      needCheckFlag: needCheckFlag
    }
  })
}

// ==============================

export function getFreezeGoodsList(vue, params) {
  params.freezeStatus = 2
  if (!category) {
    category = vue.$api.get(vue, 'admin/u/po/category/list', { page: 1, count: 10000 }).then(res => {
      if (res.code === 0) {
        return res.categorys
      }
    })
  }
  if (!brand) {
    brand = vue.$api.get(vue, 'admin/u/po/brand/list', { page: 1, count: 10000 }).then(res => {
      if (res.code === 0) {
        return res.brands
      }
    })
  }
  const goods = vue.$api.get(vue, api.getGoodsList, params).then(res => {
    if (res.code === 0) {
      return res.goods
    }
  })
  const count = vue.$api.get(vue, 'admin/u/p/goods/count', params).then(res => {
    if (res.code === 0) {
      return res.count
    }
  })
  return Promise.all([category, brand, goods, count]).then(res => {
    res[2].forEach(item1 => {
      let ary = []
      res[0].filter(item2 => {
        if (item1.categoryIds) {
          return item1.categoryIds.splitnum(',').indexOf(item2.id) >= 0
        }
      }).forEach(item3 => {
        ary.push(item3.name)
      })
      ary = ary.join(',')
      item1.categoryName = ary
      res[1].forEach(item4 => {
        if (item4.id === item1.brandId) {
          item1.brandName = item4.title
        }
      })
    })
    category = res[0]
    brand = res[1]
    return {
      tableData: res[2],
      total: res[3]
    }
  })
}

// ==============================

// ids获取商品
export function getIdsGoods(vue, params) {
  if (!category) {
    category = vue.$api.get(vue, 'admin/u/p/category/list', { page: 1, count: 10000 }).then(res => {
      if (res.code === 0) {
        return res.categorys
      }
    })
  }
  if (!brand) {
    brand = vue.$api.get(vue, url.getBrandList, { page: 1, size: 10000 }).then(res => {
      if (res.code === 0) {
        return res.brands
      }
    })
  }
  const goods = vue.$api.get(vue, 'admin/u/po/goods/ids', params).then(res => {
    if (res.code === 0) {
      return res.goods
    }
  })
  return Promise.all([category, brand, goods]).then(res => {
    res[2].forEach(item1 => {
      let ary = []
      res[0].filter(item2 => {
        if (item1.categoryIds) {
          return item1.categoryIds.splitnum(',').indexOf(item2.id) >= 0
        }
      }).forEach(item3 => {
        ary.push(item3.name)
      })
      ary = ary.join(',')
      item1.categoryName = ary
      res[1].forEach(item4 => {
        if (item4.id === item1.brandId) {
          item1.brandName = item4.title
        }
      })
    })
    category = res[0]
    brand = res[1]
    return {
      goods: res[2]
    }
  })
}

export function importGoods(vue, params) {
  if (!category) {
    category = vue.$api.get(vue, 'admin/u/p/category/list', { page: 1, count: 10000 }).then(res => {
      if (res.code === 0) {
        return res.categorys
      }
    })
  }
  const goods = vue.$api.get(vue, 'admin/u/p/goods/items/clearance', params).then(res => {
    if (res.code === 0) {
      return res.data
    }
  })
  return Promise.all([category, goods]).then(res => {
    res[1].forEach(item1 => {
      let ary = []
      res[0].filter(item2 => {
        if (item1.categoryIds) {
          return item1.categoryIds.splitnum(',').indexOf(item2.id) >= 0
        }
      }).forEach(item3 => {
        ary.push(item3.name)
      })
      ary = ary.join(',')
      item1.categoryName = ary
    })
    category = res[0]
    return {
      goods: res[1]
    }
  })
}
// 管理员(商品模块) - 同步货品信息
export function syncGoodsItem(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/goods/items/syncGoodsItem', params)
}

export function syncGoodsItemPrice(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/goods/syncAllGoodsPrice', params)
}

// 商品信息同步到收单工具
export function syncCollect(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/goods/synCollect', params)
}

// 清空产品和分类
export function clearBrandCategory() {
  category = undefined
  brand = undefined
}
