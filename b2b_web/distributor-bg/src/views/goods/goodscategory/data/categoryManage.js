/*
 * @Author: 陈良顺
 * @Date:   2018-03-28 09:39:05
 * @Last Modified by:   陈良顺
 * @Last Modified time: 2018-04-08 15:43:41
 */
import url from '@/api/allUrl.js'
// 获取分类列表数据
export function getCategoryList(vue, params) {
  const a = vue.$api.get(vue, 'admin/u/p/category/list', params).then(res => {
    const ary = []
    res.categorys.forEach(item => {
      if (item.parentId === 1) {
        item.children = []
        ary.push(item)
      }
    })
    ary.sort((a, b) => {
      return a.sort - b.sort > 0
    })
    ary.forEach((item) => {
      res.categorys.forEach(val => {
        if (val.parentId === item.id) {
          item.children.push(val)
        }
      })
      item.children.sort((a, b) => {
        return a.sort - b.sort > 0
      })
    })
    return ary
  })
  const b = vue.$api.get(vue, 'admin/u/p/category/count', { parentId: 0 }).then(res => {
    return res.count
  })
  return Promise.all([a, b]).then(res => {
    return {
      tableData: res[0],
      total: res[1]
    }
  })
}

export function getCategoryPoList(vue, params) {
  const a = vue.$api.get(vue, 'admin/u/po/category/list', params).then(res => {
    const ary = []
    res.categorys.forEach(item => {
      if (item.parentId === 1) {
        item.children = []
        ary.push(item)
      }
    })
    ary.sort((a, b) => {
      return a.sort - b.sort > 0
    })
    ary.forEach((item) => {
      res.categorys.forEach(val => {
        if (val.parentId === item.id) {
          item.children.push(val)
        }
      })
      item.children.sort((a, b) => {
        return a.sort - b.sort > 0
      })
    })
    return ary
  })
  const b = vue.$api.get(vue, 'admin/u/po/category/count', { parentId: 0 }).then(res => {
    return res.count
  })
  return Promise.all([a, b]).then(res => {
    return {
      tableData: res[0],
      total: res[1]
    }
  })
}

// 获取所有分类级联
export function getAllCategory(vue) {
  // return vue.$api.get(vue, 'admin/u/po/category/list', { page: 1, count: 10000, parentId: 0 }).then(res => {
  return vue.$api.get(vue, url.getClassifyPoList, { page: 1, size: 10000 }).then(res => {
    const ary = []
    res.data.list.forEach(item => {
      const obj = {
        id: '',
        value: '',
        label: '',
        sort: '',
        parentId: 1
      }
      if (item.parentId === 1 && item.status) {
        obj.children = []
        obj.id = item.id
        obj.value = '' + item.id
        obj.label = item.name
        obj.sort = item.sort
        obj.parentId = item.parentId
        ary.push(obj)
      }
    })
    ary.sort((a, b) => {
      return a.sort - b.sort > 0
    })
    ary.forEach((item) => {
      res.categorys.forEach(val => {
        const obj = {
          id: '',
          value: '',
          label: '',
          sort: '',
          parentId: 1
        }
        if (val.parentId === item.id && val.status) {
        // item.children=[];
          obj.id = val.id
          obj.value = '' + val.id
          obj.label = val.name
          obj.sort = val.sort
          obj.parentId = val.parentId
          item.children.push(obj)
        }
      })
      if (item.children) {
        item.children.sort((a, b) => {
          return a.sort - b.sort > 0
        })
      }
    })
    return ary
  })
}
