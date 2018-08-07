/*
 * @Author: 李杰敏
 * @Date:   2018-04-06 12:13:20
 * @Last Modified by:   李杰敏
 * @Last Modified time: 2018-04-06 12:13:20
 */
import api from '@/api/allApi'

/*
 * 分销商管理，分销商设置
 */
//  分销商设置，销售区域，编辑请求
export function postredactData(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/distributor/salearea', params)
}

// 分销商设置，销售区域，销售列表
export function getdatalistData(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/distributor/salearea/list', params)
}

// 分销商设置，销售区域，列表页总数
export function getnumberData(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/distributor/salearea/count', params)
}

// 分销商设置，销售区域，列表删除
export function deleteData(vm, params) {
  return vm.$api.delete(vm, 'admin/u/p/distributor/salearea', params)
}

// 分销商设置，销售区域，列表停用
export function putstopData(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/distributor/salearea/status', params)
}

// 分销商设置，销售区域，列表启用
export function putstarData(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/distributor/salearea/status', params)
}

// 分销商设置，销售区域，添加
export function postaddData(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/distributor/salearea', params)
}

// 分销商设置，销售区域，销售列表
export function distributorlistData(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/distributor/salearea/list', params)
}

// 分销商设置，分销商等级
export function gradeData(vm, params) {
  return vm.$api.get(vm, api.getGradeList, params)
}

// 分销商设置，分销商等级，总列数
export function gradeNumberData(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/distributor/grade/count', params)
}

// 分销商设置，分销商等级，编辑请求
export function gradeEdit(vm, params) {
  return vm.$api.put(vm, api.DisGrade, params)
}

// 分销商设置，分销商等级，等级添加
export function addGrade(vm, params) {
  return vm.$api.post(vm, api.DisGrade, params)
}

// 分销商设置，分销商等级，列表删除
export function gradeDelete(vm, params) {
  return vm.$api.delete(vm, api.DisGrade, params)
}

// 分销商设置，分销商等级，列表停用
export function gradeStop(vm, params) {
  return vm.$api.put(vm, api.updateGradeStatus, params)
}

// 分销商设置，分销商等级，列表启用
export function gradeStar(vm, params) {
  return vm.$api.put(vm, api.updateGradeStatus, params)
}

/**
  * 分销商管理，合作中分销商设置
  */

// 合作中分销商，分销商列表
export function consociationData(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/distributor/cooperating/list', params)
}

// 合作中分销商，分销商列表总数
export function conlistData(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/distributor/cooperating/count', params)
}
// 合作中分销商，分销商列表冻结
export function freezeData(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/distributor/cooperating/freeze', params)
}

// 合作中分销商，添加合作分销商
export function distributorsaddData(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/distributor/cooperating', params)
}

// 合作中分销商，编辑修改合作分销商
export function exchangeData(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/distributor/cooperating', params)
}

// 合作中分销商，申请中的分销商审核
export function applyData(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/distributor/check', params)
}

// 合作中分销商，审核分销商
export function distributorsauditData(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/distributor/cooperating', params)
}

// 合作中分销商，修改合作分销商
export function distributorsData(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/distributor/cooperating', params)
}

// 合作中分销商，修改合作分销商
export function distributorsDetails(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/distributor/cooperating', params)
}

let category, brand
// 获取未冻结商品
export function getGoodsList(vue, params) {
  params.freezeStatus = 1
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

export function checkNumLet(value) {
  var regD = /^[A-Za-z0-9]+$/
  var reg = regD
  if (reg.test(value)) {
    return true
  } else {
    return false
  }
}
