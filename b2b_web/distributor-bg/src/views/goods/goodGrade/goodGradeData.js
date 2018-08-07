/*
 * @Author: Jason
 * @Date:   2018-03-28 09:39:05
 * @Last Modified by: mikey.zhaopeng
 * @Last Modified time: 2018-05-10 09:36:48
 */

// 声明存放将要拼凑的数据
let category, brand, allGrades

// 管理员(货品等级管理) - 查看历史等级
export function getHistoryGrade(vue, params) {
    if (!allGrades) { // if没有数据，就发出请求数据
        allGrades = vue.$api.get(vue, 'admin/u/p/item/grade').then(res => { // 分类列表
            // if (res.code === 0 && res.grades !== undefined && res.grades.otherGrades !== undefined) {
            //     res.grades.otherGrades.splice(0, 0, res.grades.newGoods) // 插入新品
            //     return res.grades.otherGrades
            // }
            if (res.code === 0 && res.grades !== undefined) {
                return res.grades
            }
        })
    }
    var items = vue.$api.get(vue, 'admin/u/p/item/grade/history', params).then(res => { // 货品等级列表
        if (res.code === 0) {
            return {
                itemId: res.itemId,
                itemCode: res.itemCode,
                itemName: res.itemName,
                histories: res.histories
            }
        }
    })
    return Promise.all([allGrades, items]).then(res => {
        res[1].histories.forEach(item1 => {
            let b = false
            res[0].forEach(item2 => {
                if (item1.gradeId === item2.id) {
                    item1.itemGradeName = item2.gradeName
                    b = true
                }
            })
            if (!b) {
                item1.itemGradeName = item1.gradeId
            }
        })
        return {
            items: res[1]
        }
    })
}
// 管理员(货品等级管理) - 根据等级Ids获取等级信息
export function getGradeInfoByIds(vm, params) {
    return vm.$api.get(vm, 'admin/u/p/item/grade/ids', params)
}
// 管理员(货品等级管理) - 根据等级Id获取等级信息
export function getGradeInfoById(vm, params) {
    return vm.$api.get(vm, 'admin/u/p/item/grade', params)
}
// 管理员(货品等级管理) - 等级变动记录列表
export function getGradeChangeList(vm, params) {
    return vm.$api.get(vm, 'admin/u/p/item/grade/change/record', params)
}
// 管理员(货品等级管理) - 等级变动记录列表总数
export function getGradeChangeCount(vm, params) {
    return vm.$api.get(vm, 'admin/u/p/item/grade/change/count', params)
}
// 管理员(货品等级管理) - 等级变动审批详情
export function getGradeChangeCheckDetail(vue, params) {
    return vue.$api.get(vue, 'admin/u/p/item/grade/change/check/detail', params)
}
// 管理员(货品等级管理) - 等级变动记录详情总数
export function getGradeChangeDetailCount(vue, params) {
    return vue.$api.get(vue, 'admin/u/p/item/grade/change/record/detail/count', params)
}

// 管理员(货品等级管理) - 等级变动记录详情
export function getGradeChangeDetail(vue, params) {
    return vue.$api.get(vue, 'admin/u/p/item/grade/change/record/detail', params)
}
// 管理员(货品等级管理) - 获取需要变更等级的货品(调整等级/添加停产产品)
export function getGradeChangeItems(vue, params) {
    if (!allGrades) { // if没有数据，就发出请求数据
        allGrades = vue.$api.get(vue, 'admin/u/po/good/grade/list').then(res => { // 分类列表
            if (res.code === 0 && res.goodGrades !== undefined) {
                return res.goodGrades
            }
        })
    }
    var items = vue.$api.get(vue, 'admin/u/p/item/grade/change', params).then(res => { // 货品等级列表
        if (res.code === 0) {
            return res.items
        }
    })
    return Promise.all([allGrades, items]).then(res => {
        res[1].forEach(item1 => {
            res[0].forEach(item2 => {
                if (item1.gradeId === item2.id) {
                    item1.itemGradeName = item2.gradeName
                }
            })
        })
        return {
            items: res[1]
        }
    })
}
// 管理员(货品等级管理) - 货品周转规则添加
export function turnoverRule(vm, params) {
    return vm.$api.post(vm, 'admin/u/p/item/grade/turnoverRule', params)
}
// 管理员(货品等级管理) - 货品等级信息
export function getGoodGrade(vm, params) {
    return vm.$api.get(vm, 'admin/u/p/good/grade/list', params)
}

// 管理员(货品等级管理) - 货品等级信息
export function getGoodGradePo(vm, params) {
    return vm.$api.get(vm, 'admin/u/po/good/grade/list', params)
}
// 管理员(货品等级管理) - 货品等级列表
export function getGoodGradeList(vue, params) {
    if (!category) { // if没有数据，就发出请求数据
        category = vue.$api.get(vue, 'admin/u/po/productline/list', { page: 1, count: 10000 }).then(res => { // 分类列表
            if (res.code === 0) {
                return res.productlines
            }
        })
    }
    if (!brand) {
        brand = vue.$api.get(vue, 'admin/u/po/brand/list', { page: 1, count: 10000 }).then(res => { // 品牌列表
            if (res.code === 0) {
                return res.brands
            }
        })
    }
    // if (!allGrades) { // if没有数据，就发出请求数据
    //   allGrades = vue.$api.get(vue, 'admin/u/p/item/grade/detail').then(res => { // 分类列表
    //     if (res.code === 0 && res.grades !== undefined && res.grades.otherGrades !== undefined) {
    //       res.grades.otherGrades.splice(0, 0, res.grades.newGoods) // 插入新品
    //       return res.grades.otherGrades
    //     }
    //   })
    // }
    var items = vue.$api.get(vue, 'admin/u/p/item/grade/list', params).then(res => { // 货品等级列表
        if (res.code === 0) {
            return res.items
        }
    })
    var count = vue.$api.get(vue, 'admin/u/p/item/grade/count', params).then(res => { // 货品等级列表总数
            if (res.code === 0) {
                return res.count
            }
        })
        // category: 分类列表, brand: 品牌列表, goods: 货品等级列表, count: 货品等级列表总数
    return Promise.all([category, brand, items, count]).then(res => { // promise.all 将多个数据返回成一个来使用
        res[2].forEach(item1 => { // res[2]: goods商品名称(商品列表)  item1: category分类名称(分类列表)
            res[0].forEach(item2 => {
                if (item2.id === item1.categoryId) {
                    item1.categoryName = item2.name
                }
            })
            res[1].forEach(item4 => {
                    if (item4.id === item1.brandId) {
                        item1.brandName = item4.title
                    }
                })
                // let b = false
                // res[4].forEach(item5 => {
                //   if (item5.id === item1.gradeId) {
                //     b = true
                //     item1.itemGrade = item5.gradeName
                //   }
                // })
                // if (!b) {
                //   item1.itemGrade = '等级已删除'
                // }
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
                // allGrades: res[4]
        }
    })
}
// 管理员(货品等级管理) - 货品等级变更(调整等级/添加停产产品)
export function changeGoodGrade(vm, params) {
    return vm.$api.post(vm, 'admin/u/p/item/grade/change', params)
}
// 管理员(货品等级管理) - 货品等级添加
export function addGoodGrade(vm, params) {
    return vm.$api.post(vm, 'admin/u/p/good/grade/rule', params)
}
// 管理员(货品等级管理) - 货品等级设置详情
export function getGradeDetail(vm, params) {
    return vm.$api.get(vm, 'admin/u/p/item/grade', params)
}

// 商品管理（商品等级规则详情）
export function getGradeListData(vm, params) {
    return vm.$api.get(vm, 'admin/u/p/good/grade/rule', params)
}

// 管理员(货品等级管理) - 货品等级设置详情
export function getPoGradeDetail(vm, params) {
    // TODO
    return vm.$api.get(vm, 'admin/u/p/good/grade/list', params)
}
// 管理员(货品等级管理) - 货品等级设置详情
export function getRuleDetail(vm, params) {
    return vm.$api.get(vm, 'admin/u/p/item/grade/turnoverRule', params)
}

// 管理员(货品等级管理) - 货品等级列表
export function getGradeList(vm, params) {
    return vm.$api.get(vm, 'admin/u/p/item/grade/list', params)
}

// 货品等级重新计算
export function recountGrade(vm, params) {
    return vm.$api.post(vm, 'admin/u/p/item/grade/calculation', params)
}

// 管理员(货品等级管理) - 导出等级变动单
export function exportGrade(vm, params) {
    return vm.$api.post(vm, 'admin/u/p/grade/change/export', params)
}

// 管理员(货品等级管理) - 获取商品等级列表
export function getGradeBaseList(vm, params) {
    return vm.$api.get(vm, 'admin/u/po/good/grade/list', params)
}

// 获取品类
export function getCategoryList(vm, params) {
    return vm.$api.get(vm, 'admin/u/po/productline/list', params)
}

// 获取品牌
export function getBrandList(vm, params) {
    return vm.$api.get(vm, 'admin/u/po/brand/list', params)
}
// 管理员(产品线模块) - 获取商品品类列表
export function getProductlineList(vm) {
    return vm.$api.get(vm, 'admin/u/po/productline/list', { page: 1, count: 1000 }).then(res => {
        const ary = []
        res.productlines.forEach(item => {
            const obj = {
                id: item.id,
                value: '' + item.id,
                label: item.name
            }
            ary.push(obj)
        })
        return ary
    })
}

//商品折扣计算
export function discountCalculation(vm, params) {
    return vm.$api.put(vm, 'admin/u/p/grade/change/calculation/discount', params)
}