/*
 * @Author: 许昌亨
 * @Date:   2018-03-28 09:39:05
 * @Last Modified by:   许昌亨
 * @Last Modified time: 2018-04-08 15:43:41
 */



export function getBrandPoList(vue) {
    return vue.$api.get(vue, 'user/brand/list', { page: 1, count: 10000 })
}

export function getPriceList(vue, params) {
    let a = vue.$api.get(vue, 'user/u/user/goods/getPrice', params).then(res => {
        if (res.code === 3) {
            
        }else {
            return res
        }
    })
    let b = vue.$api.get(vue, 'user/u/user/goods/getPriceCount', params).then(res => {
        return res
    })
    return Promise.all([a, b]).then(res => {
        return {
            code:res[0].code,
            tableData: res[0].goods,
            canExportPrice: res[0].canExportPrice,
            count: res[1].count
        }
    })
}

// 获取所有分类级联
export function getAllCategory(vue) {
    const ary = vue.$api.get(vue, 'user/category/list', { page: 1, count: 10000, parentId: 0 }).then(res => {
        if (res.code === 0) {
            const ary = []
            res.categorys.forEach(item => {
                const obj = {
                    id: '',
                    value: '',
                    label: '',
                    labelEn: '',
                    sort: '',
                    parentId: 1
                }
                if (item.parentId === 1 && item.status) {
                    obj.children = []
                    obj.id = item.id
                    obj.value = '' + item.id
                    obj.label = item.name
                    obj.labelEn = item.nameEn
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
                        labelEn: '',
                        sort: '',
                        parentId: 1
                    }
                    if (val.parentId === item.id && val.status) {
                        // item.children=[];
                        obj.id = val.id
                        obj.value = '' + val.id
                        obj.label = val.name
                        obj.labelEn = val.nameEn
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
        }
        if (res.code === 3) {
           
            return []
        }
    })
    return ary
}