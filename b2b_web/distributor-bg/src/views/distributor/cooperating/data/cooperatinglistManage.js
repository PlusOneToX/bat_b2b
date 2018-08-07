/*
 * @Author: 李杰敏
 * @Date:   2018-05-31 16:02:14
 * @Last Modified by:   李杰敏
 * @Last Modified time: 2018-05-31 16:02:14
 */

// 声明存放将要拼凑的数据
let category, brand

// 获取未冻结商品
export function getGoodsList(vue, params) {
  params.freezeStatus = 1
  if (!category) { // if没有数据，就发出请求数据
    category = vue.$api.get(vue, 'admin/u/p/category/list', { page: 1, count: 10000 }).then(res => { // 分类列表
      if (res.code === 0) {
        return res.categorys
      }
    })
  }
  if (!brand) {
    brand = vue.$api.get(vue, 'admin/u/p/brand/list', { page: 1, count: 10000 }).then(res => { // 品牌列表
      if (res.code === 0) {
        return res.brands
      }
    })
  }
  var goods = vue.$api.get(vue, 'admin/u/p/goods/list', params).then(res => { // 商品列表
    if (res.code === 0) {
      return res.goods
    }
  })
  var count = vue.$api.get(vue, 'admin/u/p/goods/count', params).then(res => { // 商品列表总数
    if (res.code === 0) {
      return res.count
    }
  })
  // category: 分类列表, brand: 品牌列表, goods: 商品列表, count: 商品列表总数
  return Promise.all([category, brand, goods, count]).then(res => { // promise.all 将多个数据返回成一个来使用
    res[2].forEach(item1 => { // res[2]: goods商品名称(商品列表)  item1: category分类名称(分类列表)
      let ary = []
      res[0].filter(item2 => { // 分类名称(分类列表)
        if (item1.categoryIds) {
          return item1.categoryIds.splitnum(',').indexOf(item2.id) >= 0 // 查找到item2(商品列表)的id，并用逗号隔开 splitnum:外部导入
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

export function getFreezeGoodsList(vue, params) {
  params.freezeStatus = 2
  if (!category) {
    category = vue.$api.get(vue, 'admin/u/p/category/list', { page: 1, count: 10000 }).then(res => {
      if (res.code === 0) {
        return res.categorys
      }
    })
  }
  if (!brand) {
    brand = vue.$api.get(vue, 'admin/u/p/brand/list', { page: 1, count: 10000 }).then(res => {
      if (res.code === 0) {
        return res.brands
      }
    })
  }
  const goods = vue.$api.get(vue, 'admin/u/p/goods/list', params).then(res => {
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
    brand = vue.$api.get(vue, 'admin/u/p/brand/list', { page: 1, count: 10000 }).then(res => {
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
