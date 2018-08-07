/*
 * @Author: litian
 * @Date: 2018-04-Tu 08:00:43
 * @Last Modified by:   li.tian
 * @Last Modified time: 2018-04-Tu 08:00:43
 */
import url from './allUrl'
// 声明存放将要拼凑的数据
let category, brand, needCheckFlag

const SERVICE_API = {
    // 获取域名api
    tenantUrl: function(vm, params) {
        return vm.$api.get(vm, url.tenantUrl, params)
    },
    // 用户列表
    getUserList: function(vm, params) {
        return vm.$api.get(vm, url.getUserList, params)
    },
    // 用户列表(不受权限控制)
    getUserPoList: function(vm, params) {
        return vm.$api.get(vm, url.getUserPoList, params)
    },
    // 用户详情
    userDetail: function(vm, params) {
        return vm.$api.get(vm, url.user, params)
    },
    // 添加用户
    addUser: function(vm, params) {
        return vm.$api.post(vm, url.user, params)
    },
    // 更新用户
    editUser: function(vm, params) {
        return vm.$api.put(vm, url.user, params)
    },
    // 删除用户
    delUser: function(vm, params) {
        return vm.$api.delete(vm, url.user, params)
    },
    // 用户状态更新
    userStatus: function(vm, params) {
        return vm.$api.put(vm, url.userStatus, params)
    },
    // 账户中心员工信息列表
    rockAccountInfoList: function(vm, params) {
        return vm.$api.get(vm, url.rockAccountInfoList, params)
    },
    // 用户列表(分页)(仅返回id名称)
    salesList: function(vm, params) {
        return vm.$api.get(vm, url.salesList, params)
    },
    // 获取品牌列表
    getBrandList: function(vm, params) {
        return vm.$api.get(vm, url.getBrandList, params)
    },
    // 获取品牌列表(不受权限控制)
    getBrandPoList: function(vm, params) {
        return vm.$api.get(vm, url.getBrandPoList, params)
    },
    // 获取品牌详情
    getBrandDetail: function(vm, params) {
        return vm.$api.get(vm, url.brand, params)
    },
    // 添加品牌
    addBrand: function(vm, params) {
        return vm.$api.post(vm, url.brand, params)
    },
    // 修改品牌
    editBrand: function(vm, params) {
        return vm.$api.put(vm, url.brand, params)
    },
    // 删除品牌
    deleteBrand: function(vm, params) {
        return vm.$api.delete(vm, url.brand, params)
    },
    // 更新品牌状态
    brandStatus: function(vm, params) {
        return vm.$api.put(vm, url.brandStatus, params)
    },
    // 更新商品推荐分类信息
    recommendInfo: function(vm, params) {
        return vm.$api.post(vm, url.recommendInfo, params)
    },
    // 获取商品推荐分类信息
    getRecommendInfo: function(vm, params) {
        return vm.$api.get(vm, url.getRecommendInfo, params);
    },
    // 获取品类列表
    getCategoryList: function(vm, params) {
        return vm.$api.get(vm, url.getCategoryList, params)
    },
    // 获取品类列表(不受权限控制)
    getCategoryPoList: function(vm, params) {
        return vm.$api.get(vm, url.getCategoryPoList, params)
    },
    // 获取品类详情
    getCategoryDetail: function(vm, params) {
        return vm.$api.get(vm, url.category, params)
    },
    // 添加品类
    addCategory: function(vm, params) {
        return vm.$api.post(vm, url.category, params)
    },
    // 修改品类
    editCategory: function(vm, params) {
        return vm.$api.put(vm, url.category, params)
    },
    // 删除品类
    deleteCategory: function(vm, params) {
        return vm.$api.delete(vm, url.category, params)
    },
    // 更新品类状态
    categoryStatus: function(vm, params) {
        return vm.$api.put(vm, url.categoryStatus, params)
    },
    // 获取商品分类列表
    getClassifyList: function(vm, params) {
        let c = 0
        const a = vm.$api.get(vm, url.getClassifyList, params).then(res => {
            const ary = []
            if (res.success) {
                c = res.data.total
                res.data.list.forEach(item => {
                    item.children = []
                    ary.push(item)
                })
                ary.sort((a, b) => {
                    return a.sort - b.sort > 0
                })
                ary.forEach((item) => {
                    if (item.subClassifies && item.subClassifies.length > 0) {
                        item.subClassifies.forEach(val => {
                            if (val.parentId === item.id) {
                                item.children.push(val)
                            }
                        })
                        item.children.sort((a, b) => {
                            return a.sort - b.sort > 0
                        })
                    }
                })
            }
            return ary
        })
        return Promise.all([a]).then(res => {
            return {
                tableData: res[0],
                total: c
            }
        })
    },
    // 获取商品分类列表（不受权限控制）
    getClassifyPoList: function(vm, params) {
        const a = vm.$api.get(vm, url.getClassifyPoList, params).then(res => {
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
        return Promise.all([a]).then(res => {
            return {
                tableData: res[0],
                total: res[0].length
            }
        })
    },
    // 获取所有商品分类级联（不受权限控制, 有层级关系child）
    getAllClassifyPoList: function(vm) {
        return vm.$api.get(vm, url.getClassifyPoList, {
            page: 1,
            size: 10000,
            openFlag: 1
        }).then(res => {
            const ary = []
            if (res.success) {
                res.data.list.forEach(item => {
                    const obj = {
                        id: '',
                        value: '',
                        label: '',
                        sort: '',
                        parentId: 0
                    }
                    if (item.parentId === 0) {
                        obj.children = item.subClassifies ? item.subClassifies : ''
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
                    const arr2 = []
                    if (item.children && item.children.length > 0) {
                        item.children.forEach(val => {
                            const obj = {
                                id: '',
                                value: '',
                                label: '',
                                sort: '',
                                parentId: 1
                            }
                            if (val.parentId === item.id) {
                                obj.id = val.id
                                obj.value = '' + val.id
                                obj.label = val.name
                                obj.sort = val.sort
                                obj.parentId = val.parentId
                                arr2.push(obj)
                            }
                        })
                    }
                    if (arr2) {
                        arr2.sort((a, b) => {
                            return a.sort - b.sort > 0
                        })
                    }
                    item.children = arr2
                })
            }
            return ary
        })
    },
    // 获取商品子分类列表
    getClassifySubList: function(vm, params) {
        return vm.$api.get(vm, url.getClassifySubList, params)
    },
    // 获取商品分类详情
    getClassifyDetail: function(vm, params) {
        return vm.$api.get(vm, url.Classify, params)
    },
    // 添加商品分类
    addClassify: function(vm, params) {
        return vm.$api.post(vm, url.Classify, params)
    },
    // 修改商品分类
    editClassify: function(vm, params) {
        return vm.$api.put(vm, url.Classify, params)
    },
    // 删除商品分类
    deleteClassify: function(vm, params) {
        return vm.$api.delete(vm, url.Classify, params)
    },
    // 更新商品分类状态
    classifyStatus: function(vm, params) {
        return vm.$api.put(vm, url.classifyStatus, params)
    },
    // 获取商品列表
    getGoodsList: function(vm, params) {
        return vm.$api.get(vm, url.getGoodsList, params)
    },
    // 获取商品列表
    getGoodsListP: function(vm, params) {
        // params.freezeStatus = 1
        var category = vm.$api.get(vm, url.getClassifyPoList, {
            page: 1,
            size: 10000
        }).then(res => { // 分类列表
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
        var brand = vm.$api.get(vm, url.getBrandList, {
            page: 1,
            size: 10000
        }).then(res => { // 品牌列表
            if (res.success) {
                return res.data.list
            }
        })
        var goods = vm.$api.get(vm, url.getGoodsList, params).then(res => { // 商品列表
            if (res.success) {
                needCheckFlag = res.needCheckFlag
                return res.data
            }
        })
        var count = 0
            // category: 分类列表, brand: 品牌列表, goods: 商品列表, count: 商品列表总数
        return Promise.all([category, brand, goods, count]).then(res => { // promise.all 将多个数据返回成一个来使用
            res[2].list.forEach(item1 => { // res[2]: goods商品名称(商品列表)  item1: category分类名称(分类列表)
                    let ary = []
                    const arr = []
                    res[0].filter(item2 => { // 分类名称(分类列表)
                        arr.push(item2.id)
                        if (item1.classifyIds) {
                            return item1.classifyIds.join(',').indexOf(item2.id) >= 0
                        }
                    }).forEach(item3 => {
                        ary.push(item3.name)
                    })
                    ary = ary.join(',')
                    item1.categoryName = ary
                    res[1].forEach(item4 => {
                        if (Number(item4.id) === Number(item1.brandId)) {
                            item1.brandName = item4.name
                        }
                    })
                })
                // category = res[0]
                // brand = res[1]
            return {
                tableData: res[2],
                needCheckFlag: needCheckFlag
            }
        })
    },
    // 获取商品列表（不受权限控制）
    getGoodsPoList: function(vm, params) {
        return vm.$api.get(vm, url.getGoodsPoList, params)
    },
    // 查找erp货品列表
    goodsErpList: function(vm, params) {
        return vm.$api.get(vm, url.goodsErpList, params)
    },
    // 分销商指定的货品列表(兑换卡关联商品)
    goodsByDistributorId: function(vm, params) {
        return vm.$api.get(vm, url.goodsByDistributorId, params)
    },
    // 货品列表（定制商品）
    diyItemList: function(vm, params) {
        return vm.$api.get(vm, url.diyItemList, params)
    },
    // 货品(SKU)装箱信息列表
    goodsItemBoxList: function(vm, params) {
        return vm.$api.get(vm, url.goodsItemBox, params)
    },
    // 修改货品(SKU)装箱信息
    editGoodsItemBox: function(vm, params) {
        return vm.$api.put(vm, url.goodsItemBox, params)
    },
    // 获取货品列表
    getGoodsItemList: function(vm, params) {
        return vm.$api.get(vm, url.getGoodsItemList, params)
    },
    // 获取货品列表（不受权限控制）
    getGoodsItemPoList: function(vm, params) {
        return vm.$api.get(vm, url.getGoodsItemPoList, params)
    },
    // 获取商品详情
    getGoodsDetail: function(vm, params) {
        return vm.$api.get(vm, url.goods, params)
    },
    // 添加商品
    addGoods: function(vm, params) {
        return vm.$api.post(vm, url.goods, params)
    },
    // 修改商品列表
    editGoods: function(vm, params) {
        return vm.$api.put(vm, url.goods, params)
    },
    // 删除商品
    deleteGoods: function(vm, params) {
        return vm.$api.delete(vm, url.goods, params)
    },
    // 冻结、解冻商品
    freezeStatus: function(vm, params) {
        return vm.$api.put(vm, url.freezeStatus, params)
    },
    // 上架、下架商品
    goodsSaleStatus: function(vm, params) {
        return vm.$api.put(vm, url.updateGoodsaleStatus, params)
    },
    // 上架、下架货品
    itemSaleStatus: function(vm, params) {
        return vm.$api.put(vm, url.updateItemsaleStatus, params)
    },
    // 获取商品属性列表
    getAttributeList: function(vm, params) {
        return vm.$api.get(vm, url.getAttributeList, params)
    },
    // 获取不受权限控制商品属性列表
    getAttributePoList: function(vm, params) {
        return vm.$api.get(vm, url.getAttributePoList, params)
    },
    // 根据商品属性ID查找所有商品属性值列表(不分页)
    getAllvalueList: function(vm, params) {
        return vm.$api.get(vm, url.getAllvalueList, params)
    },
    // 获取商品属性详情
    getAttributeDetail: function(vm, params) {
        const a = vm.$api.get(vm, url.attribute, params).then(res1 => {
            if (res1.success) {
                return res1.data
            }
        })
        const b = vm.$api.get(vm, url.getAllvalueList, params).then(res2 => {
            if (res2.success) {
                return res2.data
            }
        })

        return Promise.all([a, b]).then(res => {
            return {
                data: res[0],
                values: res[1]
            }
        })
    },
    // 添加商品属性
    addAttribute: function(vm, params) {
        return vm.$api.post(vm, url.attribute, params)
    },
    // 修改商品属性
    editAttribute: function(vm, params) {
        return vm.$api.put(vm, url.attribute, params)
    },
    // 删除商品属性
    deleteAttribute: function(vm, params) {
        return vm.$api.delete(vm, url.attribute, params)
    },
    // 更新商品属性状态
    attributeStatus: function(vm, params) {
        return vm.$api.put(vm, url.attributeStatus, params)
    },
    // 价格等级列表
    getGradeList: function(vm, params) {
        return vm.$api.get(vm, url.getGradeList, params)
    },
    // 价格等级列表(不受权限控制)
    getGradePoList: function(vm, params) {
        return vm.$api.get(vm, url.getGradePoList, params)
    },
    // 添加价格等级
    addGrade: function(vm, params) {
        return vm.$api.post(vm, url.disGrade, params)
    },
    // 修改价格等级
    editGrade: function(vm, params) {
        return vm.$api.put(vm, url.disGrade, params)
    },
    // 删除价格等级
    deleteGrade: function(vm, params) {
        return vm.$api.delete(vm, url.disGrade, params)
    },
    // 获取价格等级详情
    getGradeDetail: function(vm, params) {
        return vm.$api.get(vm, url.disGrade, params)
    },
    // 更新价格等级状态
    gradeStatus: function(vm, params) {
        return vm.$api.put(vm, url.gradeStatus, params)
    },
    // 价格商品标签
    getTagList: function(vm, params) {
        return vm.$api.get(vm, url.getTagList, params)
    },
    // 添加商品标签
    addTag: function(vm, params) {
        return vm.$api.post(vm, url.goodsTag, params)
    },
    // 修改商品标签
    editTag: function(vm, params) {
        return vm.$api.put(vm, url.goodsTag, params)
    },
    // 删除商品标签
    deleteTag: function(vm, params) {
        return vm.$api.delete(vm, url.goodsTag, params)
    },
    // 获取商品标签详情
    getTagDetail: function(vm, params) {
        return vm.$api.get(vm, url.goodsTag, params)
    },
    // 更新商品标签状态
    tagStatus: function(vm, params) {
        return vm.$api.put(vm, url.updateTagStatus, params)
    },
    // 标签预览
    tabPreview: function(vm, params) {
        return vm.$api.get(vm, url.tabPreview, params)
    },
    // 手动生成标签
    createLable: function(vm, params) {
        return vm.$api.put(vm, url.createLable, params)
    },
    // 分销商设置，销售区域，销售列表
    getSalesareaList: function(vm, params) {
        return vm.$api.get(vm, url.getSalesareaList, params)
    },
    // 分销商设置，销售区域，销售列表（不受权限控制）
    getSalesareaPoList: function(vm, params) {
        return vm.$api.get(vm, url.getSalesareaPoList, params)
    },
    // 分销商设置，销售区域状态更新
    salesareaStatus: function(vm, params) {
        return vm.$api.put(vm, url.salesareaStatus, params)
    },
    // 分销商设置，销售区域详情
    salesareaDetail: function(vm, params) {
        return vm.$api.get(vm, url.salesarea, params)
    },
    // 分销商设置，添加销售区域
    addSalesarea: function(vm, params) {
        return vm.$api.post(vm, url.salesarea, params)
    },
    // 分销商设置，修改销售区域
    editSalesarea: function(vm, params) {
        return vm.$api.put(vm, url.salesarea, params)
    },
    // 分销商设置，删除销售区域
    deleteSalesarea: function(vm, params) {
        return vm.$api.delete(vm, url.salesarea, params)
    },
    // 分销商类别列表
    getDistCategoryList: function(vm, params) {
        return vm.$api.get(vm, url.getDistCategoryList, params)
    },
    // 分销商类别列表(不受权限控制)
    getDistCategoryPoList: function(vm, params) {
        return vm.$api.get(vm, url.getDistCategoryPoList, params)
    },
    // 分销商类别状态
    distCategoryStatus: function(vm, params) {
        return vm.$api.put(vm, url.distCategoryStatus, params)
    },
    // 分销商类别详情
    distCategoryDetail: function(vm, params) {
        return vm.$api.get(vm, url.distCategory, params)
    },
    // 添加分销商类别
    addDistCategory: function(vm, params) {
        return vm.$api.post(vm, url.distCategory, params)
    },
    // 修改分销商类别
    editDistCategory: function(vm, params) {
        return vm.$api.put(vm, url.distCategory, params)
    },
    // 删除分销商类别
    deleteDistCategroy: function(vm, params) {
        return vm.$api.delete(vm, url.distCategory, params)
    },
    // 分销商分组列表
    getDistGroupList: function(vm, params) {
        return vm.$api.get(vm, url.getDistGroupList, params)
    },
    // 分销商分组列表（不受权限控制）
    getDistGroupPoList: function(vm, params) {
        return vm.$api.get(vm, url.getDistGroupPoList, params)
    },
    // 分销商分组状态
    distGroupStatus: function(vm, params) {
        return vm.$api.put(vm, url.distGroupStatus, params)
    },
    // 根据分销商id拒绝申请中的分销商
    refuseDistributor: function(vm, params) {
        return vm.$api.put(vm, url.refuseDistributor, params)
    },
    // 分销商资料审批列表
    distributorCheckList: function(vm, params) {
        return vm.$api.get(vm, url.distributorCheckList, params)
    },
    // 分销商资料审批详情
    distributorCheckDetail: function(vm, params) {
        return vm.$api.get(vm, url.distributorCheck, params)
    },
    // 分销商资料审批
    distributorCheck: function(vm, params) {
        return vm.$api.put(vm, url.distributorCheck, params)
    },
    // 新增店铺分账
    subAccountAdd: function(vm, params) {
        return vm.$api.post(vm, url.subAccount, params)
    },
    // 修改店铺分账
    subAccountEdit: function(vm, params) {
        return vm.$api.put(vm, url.subAccount, params)
    },
    // 店铺分账详情
    subAccountDetail: function(vm, params) {
        return vm.$api.get(vm, url.subAccount, params)
    },
    // 分账记录列表
    orderSubAccountList: function(vm, params) {
        return vm.$api.get(vm, url.orderSubAccountList, params)
    },
    // 查看分账记录详情
    orderSubAccountDetail: function(vm, params) {
        return vm.$api.get(vm, url.orderSubAccountDetail, params)
    },
    // 分账配置列表
    subAccountConfigList: function(vm, params) {
        return vm.$api.get(vm, url.subAccountConfigList, params)
    },
    // 分账配置下拉列表
    subConfigListByCondition: function(vm, params) {
        return vm.$api.get(vm, url.subConfigListByCondition, params)
    },
    // 分账业务员列表
    subAccountSaleList: function(vm, params) {
        return vm.$api.get(vm, url.subAccountSaleList, params)
    },
    // 分账业务员下拉列表
    subSalesmanByCondition: function(vm, params) {
        return vm.$api.get(vm, url.subSalesmanByCondition, params)
    },
    // 分销商分组详情
    distGroupDetail: function(vm, params) {
        return vm.$api.get(vm, url.distGroup, params)
    },
    // 添加分销商分组
    addDistGroup: function(vm, params) {
        return vm.$api.post(vm, url.distGroup, params)
    },
    // 修改分销商分组
    editDistGroup: function(vm, params) {
        return vm.$api.put(vm, url.distGroup, params)
    },
    // 删除分销商分组
    deleteDistGroup: function(vm, params) {
        return vm.$api.delete(vm, url.distGroup, params)
    },
    // 收款条件列表
    tradeList: function(vm, params) {
        return vm.$api.get(vm, url.tradeList, params)
    },
    // 收款条件列表（不受权限控制）
    tradePoList: function(vm, params) {
        return vm.$api.get(vm, url.tradePoList, params)
    },
    // 收款条件状态
    tradeStatus: function(vm, params) {
        return vm.$api.put(vm, url.tradeStatus, params)
    },
    // 收款条件详情
    tradeDetail: function(vm, params) {
        return vm.$api.get(vm, url.trade, params)
    },
    // 添加收款条件
    addTrade: function(vm, params) {
        return vm.$api.post(vm, url.trade, params)
    },
    // 修改收款条件
    editTrade: function(vm, params) {
        return vm.$api.put(vm, url.trade, params)
    },
    // 删除收款条件
    delTrade: function(vm, params) {
        return vm.$api.delete(vm, url.trade, params)
    },
    // 分销商列表(一级)
    getDistributorFList: function(vm, params) {
        return vm.$api.get(vm, url.getDistributorFList, params)
    },
    // 分销商列表(多级)
    getDistributorNList: function(vm, params) {
        return vm.$api.get(vm, url.getDistributorNList, params)
    },
    // 分销商列表(一级，不受权限控制)
    getDistributorPoList: function(vm, params) {
        return vm.$api.get(vm, url.getDistributorPoList, params)
    },
    // 分销商列表(多级，不受权限控制)
    getDistributorNPoList: function(vm, params) {
        return vm.$api.get(vm, url.getDistributorNPoList, params)
    },
    // 合作中分销商，分销商列表冻结
    distributorStatus: function(vm, params) {
        return vm.$api.put(vm, url.distributorStatus, params)
    },
    // 合作中分销商，分销商详情
    distributorDetail: function(vm, params) {
        return vm.$api.get(vm, url.distributor, params)
    },
    // 合作中分销商，分销商详情
    distributorDelete: function(vm, params) {
        return vm.$api.delete(vm, url.distributor, params)
    },
    // 合作中分销商，添加分销商
    addDistributor: function(vm, params) {
        return vm.$api.post(vm, url.distributor, params)
    },
    // 合作中分销商，修改分销商
    editDistributor: function(vm, params) {
        return vm.$api.put(vm, url.distributor, params)
    },
    // 合作中分销商，删除分销商
    deleteDistributor: function(vm, params) {
        return vm.$api.delete(vm, url.distributor, params)
    },
    disNextCheck: function(vm, params) {
        return vm.$api.put(vm, url.disNextCheck, params)
    },
    // 分销商联系人角色列表
    getDisRoleList: function(vm, params) {
        return vm.$api.get(vm, url.getDisRoleList, params)
    },
    // 根据分销商id查询列表
    disCustomPriceById: function(vm, params) {
        return vm.$api.get(vm, url.distributorCustomPrice, params)
    },
    // 保存分销商定制价格、B2B前后台
    distributorCustomPrice: function(vm, params) {
        return vm.$api.post(vm, url.distributorCustomPrice, params)
    },
    // 销售组织列表
    getOrganizationList: function(vm, params) {
        return vm.$api.get(vm, url.getOrganizationList, params)
    },
    // 销售组织列表（不受权限控制）
    getOrganizationPoList: function(vm, params) {
        return vm.$api.get(vm, url.getOrganizationPoList, params)
    },
    // 销售组织列表
    getOrganizationByIds: function(vm, params) {
        return vm.$api.get(vm, url.getOrganizationByIds, params)
    },
    // 销售组织添详情
    organizationDetail: function(vm, params) {
        return vm.$api.get(vm, url.organization, params)
    },
    // 添加销售组织列表
    addOrganization: function(vm, params) {
        return vm.$api.post(vm, url.organization, params)
    },
    // 更新销售组织列表
    editOrganization: function(vm, params) {
        return vm.$api.put(vm, url.organization, params)
    },
    // 删除销售组织
    deleteOrganization: function(vm, params) {
        return vm.$api.delete(vm, url.organization, params)
    },
    // 销售部门列表
    getDepartmentList: function(vm, params) {
        return vm.$api.get(vm, url.getDepartmentList, params)
    },
    // 销售部门列表(不受权限控制)
    getDepartmentPoList: function(vm, params) {
        return vm.$api.get(vm, url.getDepartmentPoList, params)
    },
    // 销售部门详情
    departmentDetail: function(vm, params) {
        return vm.$api.get(vm, url.department, params)
    },
    // 添加销售部门
    addDepartment: function(vm, params) {
        return vm.$api.post(vm, url.department, params)
    },
    // 更新销售部门
    editDepartment: function(vm, params) {
        return vm.$api.put(vm, url.department, params)
    },
    // 删除销售部门
    deleteDepartment: function(vm, params) {
        return vm.$api.delete(vm, url.department, params)
    },
    // 组织下的部门列表
    getDepartmentPoById: function(vm, params) {
        return vm.$api.get(vm, url.getDepartmentPoById, params)
    },
    // 角色列表
    getRoleList: function(vm, params) {
        return vm.$api.get(vm, url.getRoleList, params)
    },
    // 角色详情
    roleDetail: function(vm, params) {
        return vm.$api.get(vm, url.role, params)
    },
    // 添加角色
    addRole: function(vm, params) {
        return vm.$api.post(vm, url.role, params)
    },
    // 更新角色
    editRole: function(vm, params) {
        return vm.$api.put(vm, url.role, params)
    },
    // 删除角色
    deleteRole: function(vm, params) {
        return vm.$api.delete(vm, url.role, params)
    },
    // 权限列表（不受权限控制）
    permissionPoList: function(vm, params) {
        return vm.$api.get(vm, url.permissionPoList, params)
    },
    // 菜单列表（不受权限控制）
    menuPoList: function(vm, params) {
        return vm.$api.get(vm, url.menuPoList, params)
    },
    // 根据角色id 查询权限
    roleIds: function(vm, params) {
        return vm.$api.get(vm, url.roleIds, params)
    },
    // 审批配置详情
    checkDetail: function(vm, params) {
        return vm.$api.get(vm, url.checkDetail, params)
    },
    // 通过ID集合查询多个后台用户
    getUserIds: function(vm, params) {
        return vm.$api.get(vm, url.getUserIds, params)
    },
    // 审批配置详情(不受权限控制)
    checkPoDetail: function(vm, params) {
        return vm.$api.get(vm, url.checkPoDetail, params)
    },
    // 审批配置修改
    editCheck: function(vm, params) {
        return vm.$api.put(vm, url.check, params)
    },
    // 配送列表
    logisticsList: function(vm, params) {
        return vm.$api.get(vm, url.logisticsList, params)
    },
    // 配送列表（不受权限控制）
    logisticsPoList: function(vm, params) {
        return vm.$api.get(vm, url.logisticsPoList, params)
    },
    // 配送详情
    logisticsDetail: function(vm, params) {
        return vm.$api.get(vm, url.logistics, params)
    },
    // 添加配送
    addLogistics: function(vm, params) {
        return vm.$api.post(vm, url.logistics, params)
    },
    // 更新配送
    editLogistics: function(vm, params) {
        return vm.$api.put(vm, url.logistics, params)
    },
    // 删除配送
    deleteLogistics: function(vm, params) {
        return vm.$api.delete(vm, url.logistics, params)
    },
    // 公式校验
    formula: function(vm, params) {
        return vm.$api.post(vm, url.formula, params)
    },
    // 仓库列表
    warehouseList: function(vm, params) {
        return vm.$api.get(vm, url.warehouseList, params)
    },
    // 仓库列表(不受权限控制)
    warehousePoList: function(vm, params) {
        return vm.$api.get(vm, url.warehousePoList, params)
    },
    // 仓库详情
    warehouseDetail: function(vm, params) {
        return vm.$api.get(vm, url.warehouse, params)
    },
    // 添加仓库
    addWarehouse: function(vm, params) {
        return vm.$api.post(vm, url.warehouse, params)
    },
    // 更新仓库
    editWarehouse: function(vm, params) {
        return vm.$api.put(vm, url.warehouse, params)
    },
    // 删除仓库
    deleteWarehouse: function(vm, params) {
        return vm.$api.delete(vm, url.warehouse, params)
    },
    // 货品库存列表
    warehouseStockList: function(vm, params) {
        return vm.$api.get(vm, url.warehouseStockList, params)
    },
    // 同步库存
    syncStock: function(vm, params) {
        return vm.$api.put(vm, url.syncStock, params)
    },
    // 仓库上下移动
    warehouseUoD: function(vm, params) {
        return vm.$api.put(vm, url.warehouseUoD, params)
    },
    // 启用、禁用仓库
    warehouseStatus: function(vm, params) {
        return vm.$api.put(vm, url.warehouseStatus, params)
    },
    // 仓库设置列表
    warehouseSettingList: function(vm, params) {
        return vm.$api.get(vm, url.warehouseSettingList, params)
    },
    // 修改仓库设置
    editWarehouseSetting: function(vm, params) {
        return vm.$api.put(vm, url.warehouseSetting, params)
    },
    // 库存预留列表
    stockReservedList: function(vm, params) {
        return vm.$api.get(vm, url.stockReservedList, params)
    },
    // 库存预留详情
    stockReservedDetail: function(vm, params) {
        return vm.$api.get(vm, url.stockReserved, params)
    },
    // 添加库存预留
    addStockReserved: function(vm, params) {
        return vm.$api.post(vm, url.stockReserved, params)
    },
    // 修改库存预留
    editStockReserved: function(vm, params) {
        return vm.$api.put(vm, url.stockReserved, params)
    },
    // 释放预留
    stockRelease: function(vm, params) {
        return vm.$api.put(vm, url.stockRelease, params)
    },
    // 货品id列表查询货品库存
    stockListByitemId: function(vm, params) {
        return vm.$api.post(vm, url.stockListByitemId, params)
    },
    // 同步单个库存
    syncStockByItemErpId: function(vm, params) {
        return vm.$api.put(vm, url.syncStockByItemErpId, params)
    },
    // 重置锁库数量
    resetLockStock: function(vm, params) {
        return vm.$api.get(vm, url.resetLockStock, params)
    },
    // 区域递归树形列表
    regionList: function(vm, params) {
        return vm.$api.get(vm, url.regionList, params)
    },
    // 区域列表
    regionListById: function(vm, params) {
        return vm.$api.get(vm, url.regionListById, params)
    },
    // 查询设置
    baseSetting: function(vm, params) {
        return vm.$api.get(vm, url.baseSetting, params)
    },
    // 更改基本设置
    editBaseSetting: function(vm, params) {
        return vm.$api.put(vm, url.baseSetting, params)
    },
    // 推广列表
    bannerList: function(vm, params) {
        return vm.$api.get(vm, url.bannerList, params)
    },
    // 推广详情
    bannerDetail: function(vm, params) {
        return vm.$api.put(vm, url.banner, params)
    },
    // 添加推广
    addBanner: function(vm, params) {
        return vm.$api.post(vm, url.banner, params)
    },
    // 修改推广
    editBanner: function(vm, params) {
        return vm.$api.put(vm, url.banner, params)
    },
    // 删除推广
    delBanner: function(vm, params) {
        return vm.$api.delete(vm, url.banner, params)
    },
    // 修改下移
    bannerDown: function(vm, params) {
        return vm.$api.put(vm, url.bannerDown, params)
    },
    // 推广上移
    bannerUp: function(vm, params) {
        return vm.$api.put(vm, url.bannerUp, params)
    },
    // 公告列表
    noticeList: function(vm, params) {
        return vm.$api.get(vm, url.noticeList, params)
    },
    // 公告详情
    noticeDetail: function(vm, params) {
        return vm.$api.get(vm, url.notice, params)
    },
    // 添加公告
    addNotice: function(vm, params) {
        return vm.$api.post(vm, url.notice, params)
    },
    // 修改公告
    editNotice: function(vm, params) {
        return vm.$api.put(vm, url.notice, params)
    },
    // 删除公告
    delNotice: function(vm, params) {
        return vm.$api.delete(vm, url.notice, params)
    },
    // 发布、取消公告
    noticeStatus: function(vm, params) {
        return vm.$api.put(vm, url.noticeStatus, params)
    },
    // 栏目列表
    columnList: function(vm, params) {
        return vm.$api.get(vm, url.columnList, params)
    },
    // 栏目详情
    columnDetail: function(vm, params) {
        return vm.$api.get(vm, url.column, params)
    },
    // 添加栏目
    addColumn: function(vm, params) {
        return vm.$api.post(vm, url.column, params)
    },
    // 修改栏目
    editColumn: function(vm, params) {
        return vm.$api.put(vm, url.column, params)
    },
    // 删除栏目
    delColumn: function(vm, params) {
        return vm.$api.delete(vm, url.column, params)
    },
    // 栏目上下移
    columnSort: function(vm, params) {
        return vm.$api.put(vm, url.columnSort, params)
    },
    // 发布、取消栏目
    columnStatus: function(vm, params) {
        return vm.$api.put(vm, url.columnStatus, params)
    },
    // 栏目id获取商品列表
    columnListByIds: function(vm, params) {
        return vm.$api.get(vm, url.columnListByIds, params)
    },
    // 一键导入清仓商品（栏目）
    clearanceGoodsStoreColumn: function(vm, params) {
        return vm.$api.put(vm, url.clearanceGoodsStoreColumn, params)
    },
    // 板块列表
    sectionList: function(vm, params) {
        return vm.$api.get(vm, url.sectionList, params)
    },
    // 板块详情
    sectionDetail: function(vm, params) {
        return vm.$api.get(vm, url.section, params)
    },
    // 添加板块
    addSection: function(vm, params) {
        return vm.$api.post(vm, url.section, params)
    },
    // 修改板块
    editSection: function(vm, params) {
        return vm.$api.put(vm, url.section, params)
    },
    // 删除板块
    delSection: function(vm, params) {
        return vm.$api.delete(vm, url.section, params)
    },
    // 板块上下移
    sectionSort: function(vm, params) {
        return vm.$api.put(vm, url.sectionSort, params)
    },
    // 发布、取消板块
    sectionStatus: function(vm, params) {
        return vm.$api.put(vm, url.sectionStatus, params)
    },
    // 板块id获取商品列表
    sectionListByIds: function(vm, params) {
        return vm.$api.get(vm, url.sectionListByIds, params)
    },
    // 根据分类ids获取商品列表
    getCategoryGoods: function(vm, params) {
        return vm.$api.post(vm, url.getCategoryGoods, params)
    },
    // 根据商品ids获取商品列表
    getGoodsByIds: function(vm, params) {
        return vm.$api.post(vm, url.getGoodsByIds, params)
    },
    // 移动端首页列表
    mobileList: function(vm, params) {
        return vm.$api.get(vm, url.mobileList, params)
    },
    // 移动端配置id获取商品列表
    mobileListByIds: function(vm, params) {
        return vm.$api.get(vm, url.mobileListByIds, params)
    },
    // 移动端配置id获取商品列表
    mobileListByIdss: function(vm, params) {
        return vm.$api.get(vm, url.mobileListByIdss, params)
    },
    // 移动端首页详情
    mobileDetail: function(vm, params) {
        return vm.$api.get(vm, url.mobile, params)
    },
    // 添加移动端首页
    addMobile: function(vm, params) {
        return vm.$api.post(vm, url.mobile, params)
    },
    // 修改移动端首页
    editMobile: function(vm, params) {
        return vm.$api.put(vm, url.mobile, params)
    },
    // 删除移动端首页
    delMobile: function(vm, params) {
        return vm.$api.delete(vm, url.mobile, params)
    },
    // 查询购物设置
    getShopSetting: function(vm, params) {
        return vm.$api.get(vm, url.shopSetting, params)
    },
    // 更新购物设置
    editShopSetting: function(vm, params) {
        return vm.$api.put(vm, url.shopSetting, params)
    },
    // 查询工厂设置
    getFactorySetting: function(vm, params) {
        return vm.$api.get(vm, url.factorySetting, params)
    },
    // 更新工厂设置
    editFactorySetting: function(vm, params) {
        return vm.$api.put(vm, url.factorySetting, params)
    },
    // 查询登录设置
    getLoginSetting: function(vm, params) {
        return vm.$api.get(vm, url.loginSetting, params)
    },
    // 更新登录设置
    editLoginSetting: function(vm, params) {
        return vm.$api.put(vm, url.loginSetting, params)
    },
    // 查询协议设置
    agreementSettingList: function(vm, params) {
        return vm.$api.get(vm, url.agreementSettingList, params)
    },
    // 协议设置详情
    agreementSettingDetail: function(vm, params) {
        return vm.$api.get(vm, url.agreementSetting, params)
    },
    // 添加协议设置
    addAgreementSetting: function(vm, params) {
        return vm.$api.post(vm, url.agreementSetting, params)
    },
    // 修改协议设置
    editAgreementSetting: function(vm, params) {
        return vm.$api.put(vm, url.agreementSetting, params)
    },
    // 删除协议设置
    delAgreementSetting: function(vm, params) {
        return vm.$api.delete(vm, url.agreementSetting, params)
    },
    // 查询可用品牌
    checkBrand: function(vm, params) {
        return vm.$api.get(vm, url.checkBrand, params)
    },
    // 获取OSS
    getFileSts: function(vm, params) {
        return vm.$api.get(vm, url.getFileSts, params)
    },
    // 培训中心列表
    trainingList: function(vm, params) {
        return vm.$api.get(vm, url.trainingList, params)
    },
    // 培训中心列表不分页
    trainingPoList: function(vm, params) {
        return vm.$api.get(vm, url.trainingPoList, params)
    },
    // 培训中心详情
    trainingDetail: function(vm, params) {
        return vm.$api.get(vm, url.training, params)
    },
    // 添加培训中心
    addTraining: function(vm, params) {
        return vm.$api.post(vm, url.training, params)
    },
    // 修改培训中心
    editTraining: function(vm, params) {
        return vm.$api.put(vm, url.training, params)
    },
    // 删除培训中心
    delTraining: function(vm, params) {
        return vm.$api.delete(vm, url.training, params)
    },
    // 培训中心上移
    trainingUp: function(vm, params) {
        return vm.$api.put(vm, url.trainingUp, params)
    },
    // 培训中心下移
    trainingDown: function(vm, params) {
        return vm.$api.put(vm, url.trainingDown, params)
    },
    // 下载中心列表
    downloadList: function(vm, params) {
        return vm.$api.get(vm, url.downloadList, params)
    },
    // 下载中心列表(不受权限控制)
    downloadPoList: function(vm, params) {
        return vm.$api.get(vm, url.downloadPoList, params)
    },
    // 下载中心详情
    downloadDetail: function(vm, params) {
        return vm.$api.get(vm, url.download, params)
    },
    // 添加下载中心
    addDownload: function(vm, params) {
        return vm.$api.post(vm, url.download, params)
    },
    // 修改下载中心
    editDownload: function(vm, params) {
        return vm.$api.put(vm, url.download, params)
    },
    // 删除下载中心
    delDownload: function(vm, params) {
        return vm.$api.delete(vm, url.download, params)
    },
    // 下载中心上移
    downloadUp: function(vm, params) {
        return vm.$api.put(vm, url.downloadUp, params)
    },
    // 下载中心下移
    downloadDown: function(vm, params) {
        return vm.$api.put(vm, url.downloadDown, params)
    },
    // 产品类型列表
    productCategoryList: function(vm, params) {
        return vm.$api.get(vm, url.productCategoryList, params)
    },
    // 产品类型列(不受权限控制)
    productCategoryPoList: function(vm, params) {
        return vm.$api.get(vm, url.productCategoryPoList, params)
    },
    // 产品类型下拉列表
    productUsableList: function(vm, params) {
        return vm.$api.get(vm, url.productUsableList, params)
    },
    // 添加产品类型
    addProductCategory: function(vm, params) {
        return vm.$api.post(vm, url.productCategory, params)
    },
    // 修改产品类型
    editProductCategory: function(vm, params) {
        return vm.$api.put(vm, url.productCategory, params)
    },
    // 图片分类列表
    pictureCategoryList: function(vm, params) {
        return vm.$api.get(vm, url.pictureCategoryList, params)
    },
    // 图片分类列表(不受权限控制)
    pictureCategoryPoList: function(vm, params) {
        return vm.$api.get(vm, url.pictureCategoryPoList, params)
    },
    // 图片分类详情
    pictureCategoryDetail: function(vm, params) {
        return vm.$api.get(vm, url.pictureCategory, params)
    },
    // 添加图片分类
    addPictureCategory: function(vm, params) {
        return vm.$api.post(vm, url.pictureCategory, params)
    },
    // 修改图片分类
    editPictureCategory: function(vm, params) {
        return vm.$api.put(vm, url.pictureCategory, params)
    },
    // 删除图片分类
    delPictureCategory: function(vm, params) {
        return vm.$api.delete(vm, url.pictureCategory, params)
    },
    // 图片分类上移、下移
    pictureCategorySort: function(vm, params) {
        return vm.$api.put(vm, url.pictureCategorySort, params)
    },
    // 图片分类启用、禁用
    pictureCategoryStatus: function(vm, params) {
        return vm.$api.put(vm, url.pictureCategoryStatus, params)
    },
    // 图片列表
    pictureList: function(vm, params) {
        return vm.$api.get(vm, url.pictureList, params)
    },
    // 图片列表(不受权限控制)
    picturePoList: function(vm, params) {
        return vm.$api.get(vm, url.picturePoList, params)
    },
    // 图片详情
    pictureDetail: function(vm, params) {
        return vm.$api.get(vm, url.picture, params)
    },
    // 添加图片
    addPicture: function(vm, params) {
        return vm.$api.post(vm, url.picture, params)
    },
    // 修改图片
    editPicture: function(vm, params) {
        return vm.$api.put(vm, url.picture, params)
    },
    // 删除图片
    delPicture: function(vm, params) {
        return vm.$api.delete(vm, url.picture, params)
    },
    // 图片上移、下移
    pictureSort: function(vm, params) {
        return vm.$api.put(vm, url.pictureSort, params)
    },
    // 图片启用、禁用
    pictureStatus: function(vm, params) {
        return vm.$api.put(vm, url.pictureStatus, params)
    },
    // 分销商图片列表
    picListByDistributor: function(vm, params) {
        return vm.$api.get(vm, url.picListByDistributor, params)
    },
    // 标签列表
    labelList: function(vm, params) {
        return vm.$api.get(vm, url.labelList, params)
    },
    // 标签详情
    labelDetail: function(vm, params) {
        return vm.$api.get(vm, url.label, params)
    },
    // 添加标签
    addLabel: function(vm, params) {
        return vm.$api.post(vm, url.label, params)
    },
    // 修改标签
    editLabel: function(vm, params) {
        return vm.$api.put(vm, url.label, params)
    },
    // 删除标签
    delLabel: function(vm, params) {
        return vm.$api.delete(vm, url.label, params)
    },
    // 标签启用、禁用
    labelStatus: function(vm, params) {
        return vm.$api.put(vm, url.labelStatus, params)
    },
    // 字体列表
    fontList: function(vm, params) {
        return vm.$api.get(vm, url.fontList, params)
    },
    // 字体详情
    fontDetail: function(vm, params) {
        return vm.$api.get(vm, url.font, params)
    },
    // 添加字体
    addFont: function(vm, params) {
        return vm.$api.post(vm, url.font, params)
    },
    // 修改字体
    ediFont: function(vm, params) {
        return vm.$api.put(vm, url.font, params)
    },
    // 删除字体
    delFont: function(vm, params) {
        return vm.$api.delete(vm, url.font, params)
    },
    // 字体启用、禁用
    fontStatus: function(vm, params) {
        return vm.$api.put(vm, url.fontStatus, params)
    },
    // 字体上移、下移
    fontSort: function(vm, params) {
        return vm.$api.put(vm, url.fontSort, params)
    },
    // 型号列表
    modelList: function(vm, params) {
        return vm.$api.get(vm, url.modelList, params)
    },
    // 型号列表(不受权限控制)
    modelPoList: function(vm, params) {
        return vm.$api.get(vm, url.modelPoList, params)
    },
    // 非树形所有型号列表
    modelAllList: function(vm, params) {
        return vm.$api.get(vm, url.modelAllList, params)
    },
    // 型号详情
    modelDetail: function(vm, params) {
        return vm.$api.get(vm, url.model, params)
    },
    // 添加型号
    addModel: function(vm, params) {
        return vm.$api.post(vm, url.model, params)
    },
    // 修改型号
    editModel: function(vm, params) {
        return vm.$api.put(vm, url.model, params)
    },
    // 删除型号
    delModel: function(vm, params) {
        return vm.$api.delete(vm, url.model, params)
    },
    // 型号上移、下移
    modelSort: function(vm, params) {
        return vm.$api.put(vm, url.modelSort, params)
    },
    // 型号启用、禁用
    modelStatus: function(vm, params) {
        return vm.$api.put(vm, url.modelStatus, params)
    },
    // 导出
    modelExport: function(vm, params) {
        return vm.$api.post(vm, url.modelExport, params)
    },
    // 材质列表
    materialList: function(vm, params) {
        return vm.$api.get(vm, url.materialList, params)
    },
    // 材质列表(不受权限控制)
    materialPoList: function(vm, params) {
        return vm.$api.get(vm, url.materialPoList, params)
    },
    // 材质列表（分页）--兑换活动
    materialPageTree: function(vm, params) {
        return vm.$api.get(vm, url.materialPageTree, params)
    },
    // 材质列表（下拉列表，非分页）
    materialUnTree: function(vm, params) {
        return vm.$api.get(vm, url.materialUnTree, params)
    },
    // 最底级材质列表
    materialLowestList: function(vm, params) {
        return vm.$api.get(vm, url.materialLowestList, params)
    },
    // 顶级材质列表
    materialTreeList: function(vm, params) {
        return vm.$api.get(vm, url.materialTreeList, params)
    },
    // 材质详情
    materialDetail: function(vm, params) {
        return vm.$api.get(vm, url.material, params)
    },
    // 添加材质
    addMaterial: function(vm, params) {
        return vm.$api.post(vm, url.material, params)
    },
    // 修改材质
    editMaterial: function(vm, params) {
        return vm.$api.put(vm, url.material, params)
    },
    // 删除材质
    delMaterial: function(vm, params) {
        return vm.$api.delete(vm, url.material, params)
    },
    // 材质上移、下移
    materialSort: function(vm, params) {
        return vm.$api.put(vm, url.materialSort, params)
    },
    // 材质启用、禁用
    materialStatus: function(vm, params) {
        return vm.$api.put(vm, url.materialStatus, params)
    },
    // 型号材质关联列表
    modelAmaterialList: function(vm, params) {
        return vm.$api.get(vm, url.modelAmaterialList, params)
    },
    // 型号材质关联详情
    modelAmaterialListDetail: function(vm, params) {
        return vm.$api.get(vm, url.modelAmaterial, params)
    },
    // 删除型号材质关联
    delModelAmaterial: function(vm, params) {
        return vm.$api.delete(vm, url.modelAmaterial, params)
    },
    // 修改型号材质关联
    editModelAmaterial: function(vm, params) {
        return vm.$api.put(vm, url.modelAmaterial, params)
    },
    // 型号材质关联启用、禁用
    modelAmaterialStatus: function(vm, params) {
        return vm.$api.put(vm, url.modelAmaterialStatus, params)
    },
    // 第三方材质型号关联列表
    thirdSkuList: function(vm, params) {
        return vm.$api.get(vm, url.thirdSkuList, params)
    },
    // 删除第三方材质型号关联
    delThirdSku: function(vm, params) {
        return vm.$api.delete(vm, url.thirdSku, params)
    },
    //  第三方材质型号关联导出
    thirdSkuExport: function(vm, params) {
        return vm.$api.post(vm, url.thirdSkuExport + params)
    },
    //  第三方材质型号关联导入
    thirdSkuImport: function(vm, params) {
        return vm.$api.post(vm, url.thirdSkuImport, params)
    },
    //  第三方材质型号关联模板导入
    thirdSkutempDownload: function(vm, params) {
        return vm.$api.post(vm, url.thirdSkutempDownload, params)
    },
    //  第三方材质型号关联启用、禁用
    thirdSkuStatus: function(vm, params) {
        return vm.$api.put(vm, url.thirdSkuStatus, params)
    },
    // 门店列表
    shopList: function(vm, params) {
        return vm.$api.get(vm, url.shopList, params)
    },
    // 门店列表(不受权限控制)
    // materialPoList: function(vm, params) {
    //   return vm.$api.get(vm, url.materialPoList, params)
    // },
    // 门店详情
    shopDetail: function(vm, params) {
        return vm.$api.get(vm, url.shopDetail, params)
    },
    // 添加门店
    addShop: function(vm, params) {
        return vm.$api.post(vm, url.shop, params)
    },
    // 修改门店
    editShop: function(vm, params) {
        return vm.$api.put(vm, url.shop, params)
    },
    // 删除门店
    delShop: function(vm, params) {
        return vm.$api.delete(vm, url.shop, params)
    },
    // 门店导出
    shopExport: function(vm, params) {
        return vm.$api.post(vm, url.shopExport, params, 'arraybuffer')
    },
    // 门店模板导出
    shopTempDownLoad: function(vm, params) {
        return vm.$api.post(vm, url.shopTempDownLoad, params)
    },
    // 门店启用、禁用
    shopStatus: function(vm, params) {
        return vm.$api.put(vm, url.shopStatus, params)
    },
    // 根据分账配置Id查询门店列表
    shoplistBycId: function(vm, params) {
        return vm.$api.get(vm, url.shoplistBycId, params)
    },
    // 轮播图列表
    disBannerList: function(vm, params) {
        return vm.$api.get(vm, url.disBannerList, params)
    },
    // 轮播图详情
    disBannerDetail: function(vm, params) {
        return vm.$api.get(vm, url.disBannerDetail, params)
    },
    // 添加轮播图
    addDisBanner: function(vm, params) {
        return vm.$api.post(vm, url.disBanner, params)
    },
    // 修改轮播图
    editDisBanner: function(vm, params) {
        return vm.$api.put(vm, url.disBanner, params)
    },
    // 删除轮播图
    delDisBanner: function(vm, params) {
        return vm.$api.delete(vm, url.delDisBanner, params)
    },
    // 批量删除轮播图
    batchDelDisBanner: function(vm, params) {
        return vm.$api.delete(vm, url.batchDelDisBanner, params)
    },
    // 系列展示列表（分页）
    disThemeList: function(vm, params) {
        return vm.$api.get(vm, url.disThemeList, params)
    },
    // 系列展示列表（不分页）
    disThemePoList: function(vm, params) {
        return vm.$api.get(vm, url.disThemePoList, params)
    },
    // 查询主题列表
    disThemeListByCondition: function(vm, params) {
        return vm.$api.get(vm, url.disThemeListByCondition, params)
    },
    // 主题图片列表
    disThemePicList: function(vm, params) {
        return vm.$api.get(vm, url.disThemePicList, params)
    },
    // 系列展示详情
    disThemeDetail: function(vm, params) {
        return vm.$api.get(vm, url.disThemeDetail, params)
    },
    // 添加系列展示
    addDisTheme: function(vm, params) {
        return vm.$api.post(vm, url.disTheme, params)
    },
    // 修改系列展示
    editDisTheme: function(vm, params) {
        return vm.$api.put(vm, url.disTheme, params)
    },
    // 删除系列展示
    delDisTheme: function(vm, params) {
        return vm.$api.delete(vm, url.disTheme, params)
    },
    // 图片关联上下移动
    picRelevanceMove: function(vm, params) {
        return vm.$api.put(vm, url.picRelevanceMove, params)
    },
    // 图片关联删除
    delPicRelevance: function(vm, params) {
        return vm.$api.delete(vm, url.delPicRelevance, params)
    },
    // 图片关联批量删除
    batchPicRelevance: function(vm, params) {
        return vm.$api.delete(vm, url.batchPicRelevance, params)
    },
    // 推荐列表
    disRecommendList: function(vm, params) {
        return vm.$api.get(vm, url.disRecommendList, params)
    },
    // 推荐详情
    disRecommendDetail: function(vm, params) {
        return vm.$api.get(vm, url.disRecommendDetail, params)
    },
    // 添加推荐
    addDisRecommend: function(vm, params) {
        return vm.$api.post(vm, url.disRecommend, params)
    },
    // 修改推荐
    editDisRecommend: function(vm, params) {
        return vm.$api.put(vm, url.disRecommend, params)
    },
    // 推荐图片关联上下移动
    disRecommendMove: function(vm, params) {
        return vm.$api.put(vm, url.disRecommendMove, params)
    },
    // 推荐图片关联删除
    delDisRecommend: function(vm, params) {
        return vm.$api.delete(vm, url.delDisRecommend, params)
    },
    // 推荐图片关联批量删除
    batchDelDisRecommend: function(vm, params) {
        return vm.$api.delete(vm, url.batchDelDisRecommend, params)
    },
    // 官方主题列表列表
    pictureThemeList: function(vm, params) {
        return vm.$api.get(vm, url.pictureThemeList, params)
    },
    // 官方主题列表详情
    pictureThemeDetail: function(vm, params) {
        return vm.$api.get(vm, url.pictureTheme, params)
    },
    // 添加官方主题列表
    addPictureTheme: function(vm, params) {
        return vm.$api.post(vm, url.pictureTheme, params)
    },
    // 修改官方主题列表
    editPictureTheme: function(vm, params) {
        return vm.$api.put(vm, url.pictureTheme, params)
    },
    // 删除官方主题列表
    delPictureTheme: function(vm, params) {
        return vm.$api.delete(vm, url.pictureTheme, params)
    },
    // 官方主题与分类进行关联
    pictureThemeRelation: function(vm, params) {
        return vm.$api.post(vm, url.pictureThemeRelation, params)
    },
    // 兑换活动列表
    exchangeCardList: function(vm, params) {
        return vm.$api.get(vm, url.exchangeCardList, params)
    },
    // 兑换活动列表（不受权限控制）
    exchangeCardPoList: function(vm, params) {
        return vm.$api.get(vm, url.exchangeCardPoList, params)
    },
    // 分销商通用兑换卡关联的货品信息
    exchangeItemList: function(vm, params) {
        return vm.$api.get(vm, url.exchangeItemList, params)
    },
    // 兑换活动详情
    exchangeCardDetail: function(vm, params) {
        return vm.$api.get(vm, url.exchangeCardDetail, params)
    },
    // 添加兑换活动
    addExchangeCard: function(vm, params) {
        return vm.$api.post(vm, url.exchangeCard, params)
    },
    // 修改兑换活动
    editExchangeCard: function(vm, params) {
        return vm.$api.put(vm, url.exchangeCard, params)
    },
    // 删除兑换活动
    delExchangeCard: function(vm, params) {
        return vm.$api.delete(vm, url.exchangeCard, params)
    },
    // 兑换活动状态更新
    exchangeCardStatus: function(vm, params) {
        return vm.$api.put(vm, url.exchangeCardStatus, params)
    },
    // 生成二维码
    qrCode: function(vm, params) {
        return vm.$api.put(vm, url.qrCode, params)
    },
    // 兑换码列表
    exchangeCodeList: function(vm, params) {
        return vm.$api.get(vm, url.exchangeCodeList, params)
    },
    // 导出券码
    exchangeCodeExport: function(vm, params) {
        return vm.$api.post(vm, url.exchangeCodeExport, params)
    },
    // 生成兑换码
    createCode: function(vm, params) {
        return vm.$api.put(vm, url.createCode, params)
    },
    // 兑换码模板导出
    exchangeTempDownLoad: function(vm, params) {
        return vm.$api.post(vm, url.exchangeTempDownLoad, params)
    },
    // 兑换码excel导入
    exchangeImport: function(vm, params) {
        return vm.$api.post(vm, url.exchangeImport, params)
    },
    // 查看暗码列表
    exchangeEncodeList: function(vm, params) {
        return vm.$api.get(vm, url.exchangeEncodeList, params)
    },
    // 兑换码数据列表
    exchangeOrderList: function(vm, params) {
        return vm.$api.get(vm, url.exchangeOrderList, params)
    },
    // 作废兑换码
    exchangeCodeInvalid: function(vm, params) {
        return vm.$api.delete(vm, url.exchangeCodeInvalid, params)
    },
    // 批量作废兑换码
    exchangeCodeInvalidBatch: function(vm, params) {
        return vm.$api.delete(vm, url.exchangeCodeInvalidBatch, params)
    },
    // 兑换码通知列表
    exchangeNoticeList: function(vm, params) {
        return vm.$api.get(vm, url.exchangeNoticeList, params)
    },
    // 兑换码通知详情
    exchangeNoticeDetail: function(vm, params) {
        return vm.$api.get(vm, url.exchangeNotice, params)
    },
    // 添加兑换码通知
    addExchangeNotice: function(vm, params) {
        return vm.$api.post(vm, url.exchangeNotice, params)
    },
    // 修改兑换码通知
    editExchangeNotice: function(vm, params) {
        return vm.$api.put(vm, url.exchangeNotice, params)
    },
    // 删除兑换码通知
    delExchangeNotice: function(vm, params) {
        return vm.$api.delete(vm, url.exchangeNotice, params)
    },
    // 促销活动列表
    promotionList: function(vm, params) {
        return vm.$api.get(vm, url.promotionList, params)
    },
    // 促销活动详情
    promotionDetail: function(vm, params) {
        return vm.$api.get(vm, url.promotion, params)
    },
    // 添加促销活动
    addPromotion: function(vm, params) {
        return vm.$api.post(vm, url.promotion, params)
    },
    // 修改促销活动
    editPromotion: function(vm, params) {
        return vm.$api.put(vm, url.promotion, params)
    },
    // 删除促销活动
    delPromotion: function(vm, params) {
        return vm.$api.delete(vm, url.promotion, params)
    },
    // 促销活动状态变更
    promotionStatus: function(vm, params) {
        return vm.$api.put(vm, url.promotionStatus, params)
    },
    // 获取促销活动导入模板URL
    getPromotionTempUrl: function(vm, params) {
        return vm.$api.get(vm, url.getPromotionTempUrl, params)
    },
    // 促销活动导入
    promotionImport: function(vm, params) {
        return vm.$api.post(vm, url.promotionImport, params)
    },
    // 促销活动导出
    promotionExport: function(vm, params) {
        return vm.$api.post(vm, url.promotionExport, params)
    },
    // 促销活动批量提交
    promotionSubmits: function(vm, params) {
        return vm.$api.put(vm, url.promotionSubmits, params)
    },
    // 促销活动导入
    deletePromotions: function(vm, params) {
        return vm.$api.delete(vm, url.deletePromotions, params)
    },
    // 优惠券列表
    couponList: function(vm, params) {
        return vm.$api.get(vm, url.couponList, params)
    },
    // 优惠券详情
    couponDetail: function(vm, params) {
        return vm.$api.get(vm, url.coupon, params)
    },
    // 添加优惠券
    addCoupon: function(vm, params) {
        return vm.$api.post(vm, url.coupon, params)
    },
    // 修改优惠券
    editCoupon: function(vm, params) {
        return vm.$api.put(vm, url.coupon, params)
    },
    // 删除优惠券
    delCoupon: function(vm, params) {
        return vm.$api.delete(vm, url.coupon, params)
    },
    // 优惠券状态更改
    couponStatus: function(vm, params) {
        return vm.$api.put(vm, url.couponStatus, params)
    },
    // 修改优惠券发放总数量和限购数量
    couponCount: function(vm, params) {
        return vm.$api.put(vm, url.couponCount, params)
    },
    // 券码列表
    couponNoList: function(vm, params) {
        return vm.$api.get(vm, url.couponNoList, params)
    },
    // 根据券码变更客户优惠券状态
    couponNoStatus: function(vm, params) {
        return vm.$api.put(vm, url.couponNoStatus, params)
    },
    // 拼团列表
    groupseckillList: function(vm, params) {
        return vm.$api.get(vm, url.groupseckillList, params)
    },
    // 拼团详情
    groupseckillDetail: function(vm, params) {
        return vm.$api.get(vm, url.groupseckill, params)
    },
    // 添加拼团
    addGroupseckill: function(vm, params) {
        return vm.$api.post(vm, url.groupseckill, params)
    },
    // 修改拼团
    editGroupseckill: function(vm, params) {
        return vm.$api.put(vm, url.groupseckill, params)
    },
    // 删除拼团
    delGroupseckill: function(vm, params) {
        return vm.$api.delete(vm, url.groupseckill, params)
    },
    // 拼团秒杀活动变更状态
    groupseckillStatus: function(vm, params) {
        return vm.$api.put(vm, url.groupseckillStatus, params)
    },
    // 拼团秒杀活动排序
    groupseckillSort: function(vm, params) {
        return vm.$api.put(vm, url.groupseckillSort, params)
    },
    // 促销活动审批
    promotionCheckList: function(vm, params) {
        return vm.$api.get(vm, url.promotionCheckList, params)
    },
    // 促销活动审批
    promotionCheck: function(vm, params) {
        return vm.$api.put(vm, url.promotionCheck, params)
    },
    // 促销活动审批详情
    promotionCheckDetail: function(vm, params) {
        return vm.$api.get(vm, url.promotionCheck, params)
    },
    // 汇率列表
    currencyRateList: function(vm, params) {
        return vm.$api.get(vm, url.currencyRateList, params)
    },
    // 汇率详情
    currencyRateDetail: function(vm, params) {
        return vm.$api.get(vm, url.currencyRate, params)
    },
    // 添加汇率
    addCurrencyRate: function(vm, params) {
        return vm.$api.post(vm, url.currencyRate, params)
    },
    // 修改汇率
    editCurrencyRate: function(vm, params) {
        return vm.$api.put(vm, url.currencyRate, params)
    },
    // 删除汇率
    delCurrencyRate: function(vm, params) {
        return vm.$api.delete(vm, url.currencyRate, params)
    },
    // 同步汇率
    currencyRateSync: function(vm, params) {
        return vm.$api.put(vm, url.currencyRateSync, params)
    },
    // 币别列表
    currencyList: function(vm, params) {
        return vm.$api.get(vm, url.currencyList, params)
    },
    // 同步币别
    currencySync: function(vm, params) {
        return vm.$api.put(vm, url.currencySync, params)
    },
    // 平台账户--添加微信支付账户
    platAccountWxList: function(vm, params) {
        return vm.$api.get(vm, url.platAccountWxList, params)
    },
    // 支付测试
    payTrade: function(vm, params) {
        return vm.$api.post(vm, url.payTrade, params)
    },
    // 根据分销商查询收款帐户
    accountWxById: function(vm, params) {
        return vm.$api.get(vm, url.accountWxById, params)
    },
    // 平台账户-微信支付账户详情
    platAccountWxDetail: function(vm, params) {
        return vm.$api.get(vm, url.platAccountWx, params)
    },
    // 平台账户--添加微信支付账户
    addPlatAccountWx: function(vm, params) {
        return vm.$api.post(vm, url.platAccountWx, params)
    },
    // 平台账户--修改微信支付账户
    editPlatAccountWx: function(vm, params) {
        return vm.$api.put(vm, url.platAccountWx, params)
    },
    // 平台账户--删除微信支付账户
    delPlatAccountWx: function(vm, params) {
        return vm.$api.delete(vm, url.platAccountWx, params)
    },
    // 平台账户--支付宝账户列表
    platAccountAlipayList: function(vm, params) {
        return vm.$api.get(vm, url.platAccountAlipayList, params)
    },
    // 平台账户-支付宝账户详情
    platAccountAlipayDetail: function(vm, params) {
        return vm.$api.get(vm, url.platAccountAlipay, params)
    },
    // 平台账户--添加支付宝账户
    addPlatAccountAlipay: function(vm, params) {
        return vm.$api.post(vm, url.platAccountAlipay, params)
    },
    // 平台账户--修改支付宝账户
    editPlatAccountAlipay: function(vm, params) {
        return vm.$api.put(vm, url.platAccountAlipay, params)
    },
    // 平台账户--删除支付宝账户
    delPlatAccountAlipay: function(vm, params) {
        return vm.$api.delete(vm, url.platAccountAlipay, params)
    },
    // 平台账户--快钱账户列表
    platAccountQuickList: function(vm, params) {
        return vm.$api.get(vm, url.platAccountQuickList, params)
    },
    // 平台账户-快钱账户详情
    platAccountQuickDetail: function(vm, params) {
        return vm.$api.get(vm, url.platAccountQuick, params)
    },
    // 平台账户--添加快钱账户
    addPlatAccountQuick: function(vm, params) {
        return vm.$api.post(vm, url.platAccountQuick, params)
    },
    // 平台账户--修改快钱账户
    editPlatAccountQuick: function(vm, params) {
        return vm.$api.put(vm, url.platAccountQuick, params)
    },
    // 平台账户--删除快钱账户
    delPlatAccountQuick: function(vm, params) {
        return vm.$api.delete(vm, url.platAccountQuick, params)
    },
    // 平台账户--线下转账账户列表
    platAccountOfflineList: function(vm, params) {
        return vm.$api.get(vm, url.platAccountOfflineList, params)
    },
    // 平台账户-线下转账账户详情
    platAccountOfflineDetail: function(vm, params) {
        return vm.$api.get(vm, url.platAccountOffline, params)
    },
    // 平台账户--添加线下转账账户
    addPlatAccountOffline: function(vm, params) {
        return vm.$api.post(vm, url.platAccountOffline, params)
    },
    // 平台账户--修改线下转账账户
    editPlatAccountOffline: function(vm, params) {
        return vm.$api.put(vm, url.platAccountOffline, params)
    },
    // 平台账户--删除线下转账账户
    delPlatAccountOffline: function(vm, params) {
        return vm.$api.delete(vm, url.platAccountOffline, params)
    },
    // 微信支付账户列表
    accountWxList: function(vm, params) {
        return vm.$api.get(vm, url.accountWxList, params)
    },
    // 检查分销商关联微信情况
    checkDistributorWx: function(vm, params) {
        return vm.$api.get(vm, url.checkDistributorWx, params)
    },
    // 微信支付账户详情
    accountWxDetail: function(vm, params) {
        return vm.$api.get(vm, url.accountDetail, params)
    },
    // 添加微信支付账户
    addAccountWx: function(vm, params) {
        return vm.$api.post(vm, url.accountWx, params)
    },
    // 修改微信支付账户
    editAccountWx: function(vm, params) {
        return vm.$api.put(vm, url.accountWx, params)
    },
    // 删除微信支付账户
    delAccountWx: function(vm, params) {
        return vm.$api.delete(vm, url.accountWx, params)
    },
    // 支付宝账户列表
    accountAlipayList: function(vm, params) {
        return vm.$api.get(vm, url.accountAlipayList, params)
    },
    // 检查分销商关联支付宝情况
    checkDistributorAlipay: function(vm, params) {
        return vm.$api.get(vm, url.checkDistributorAlipay, params)
    },
    // 支付宝账户详情
    accountAlipayDetail: function(vm, params) {
        return vm.$api.get(vm, url.accountAlipay, params)
    },
    // 添加支付宝账户
    addAccountAlipay: function(vm, params) {
        return vm.$api.post(vm, url.accountAlipay, params)
    },
    // 修改支付宝账户
    editAccountAlipay: function(vm, params) {
        return vm.$api.put(vm, url.accountAlipay, params)
    },
    // 删除支付宝账户
    delAccountAlipay: function(vm, params) {
        return vm.$api.delete(vm, url.accountAlipay, params)
    },
    // 收款列表
    rechargeList: function(vm, params) {
        return vm.$api.get(vm, url.rechargeList, params)
    },
    // 提现列表
    withdrawal: function(vm, params) {
        return vm.$api.get(vm, url.withdrawal, params)
    },
    // 冻结列表
    freezingList: function(vm, params) {
        return vm.$api.get(vm, url.freezingList, params)
    },
    // 批量添加冻结
    batchFreezing: function(vm, params) {
        return vm.$api.put(vm, url.batchFreezing, params)
    },
    // 解除冻结
    freezingThaw: function(vm, params) {
        return vm.$api.put(vm, url.freezingThaw, params)
    },
    // 冻结列表获取分销商余额信息
    freezingBalance: function(vm, params) {
        return vm.$api.get(vm, url.freezingBalance, params)
    },
    // 明细账列表
    depositDetailList: function(vm, params) {
        return vm.$api.get(vm, url.depositDetailList, params)
    },
    // 余额列表
    depositAvailableList: function(vm, params) {
        return vm.$api.get(vm, url.depositAvailableList, params)
    },
    // 余额详情
    depositAvailableDetail: function(vm, params) {
        return vm.$api.get(vm, url.depositAvailable, params)
    },
    // 余额同步
    depositAvailableSync: function(vm, params) {
        return vm.$api.get(vm, url.depositAvailableSync, params)
    },
    // 收款单列表
    voucherList: function(vm, params) {
        return vm.$api.get(vm, url.voucherList, params)
    },
    // 收款单详情
    voucherDetail: function(vm, params) {
        return vm.$api.get(vm, url.voucher, params)
    },
    // 退款单列表
    refundList: function(vm, params) {
        return vm.$api.get(vm, url.refundList, params)
    },
    // 退款单详情
    refundDetail: function(vm, params) {
        return vm.$api.get(vm, url.refund, params)
    },
    // 添加退款单
    addRefund: function(vm, params) {
        return vm.$api.post(vm, url.refund, params)
    },
    // 修改退款单
    editRefund: function(vm, params) {
        return vm.$api.put(vm, url.refund, params)
    },
    // 删除退款单
    delRefund: function(vm, params) {
        return vm.$api.delete(vm, url.refund, params)
    },
    // 退款单申请列表(分销商申请)
    refundApplyList: function(vm, params) {
        return vm.$api.get(vm, url.refundApplyList, params)
    },
    // 退款单申请列表(用户申请)
    refundApplyCustomerList: function(vm, params) {
        return vm.$api.get(vm, url.refundApplyCustomerList, params)
    },
    // 退款单申请更新 /退回账户余额/其他操作/拒绝操作
    refundApply: function(vm, params) {
        return vm.$api.put(vm, url.refundApply, params)
    },
    // 根据订单ID查询退款单订单
    refundApplyById: function(vm, params) {
        return vm.$api.get(vm, url.refundApplyById, params)
    },
    // 客户管理 - 合作平台管理

    // 系统平台列表
    getSysPlatformList: function(vm, params) {
        return vm.$api.get(vm, url.getSysPlatformList, params)
    },
    // 系统平台列表（不受权限控制）
    getSysPlatformPoList: function(vm, params) {
        return vm.$api.get(vm, url.getSysPlatformPoList, params)
    },
    // 系统平台详情
    getSysPlatformDetail: function(vm, params) {
        return vm.$api.get(vm, url.sysPlatformDetail, params)
    },
    // 更新系统平台
    updateSysPlatform: function(vm, params) {
        return vm.$api.put(vm, url.sysPlatformDetail, params)
    },
    // 更新系统平台状态（启用/停用）
    updateSysPlatformStatus: function(vm, params) {
        return vm.$api.put(vm, url.updateSysPlatformStatus, params)
    },
    // 添加系统平台详情git
    addSysPlatform: function(vm, params) {
        return vm.$api.post(vm, url.sysPlatformDetail, params)
    },
    // 删除系统平台
    deleteSysPlatform: function(vm, params) {
        return vm.$api.delete(vm, url.sysPlatformDetail, params)
    },

    // 自有平台列表
    getOwnPlatformList: function(vm, params) {
        return vm.$api.get(vm, url.getOwnPlatformList, params)
    },
    // 自有平台列表（不受权限控制）
    getOwnPlatformPoList: function(vm, params) {
        return vm.$api.get(vm, url.getOwnPlatformPoList, params)
    },
    // 自有平台详情
    getOwnPlatformDetail: function(vm, params) {
        return vm.$api.get(vm, url.ownPlatformDetail, params)
    },
    // 更新自有平台
    updateOwnPlatform: function(vm, params) {
        return vm.$api.put(vm, url.ownPlatformDetail, params)
    },
    // 添加自有平台
    addOwnPlatform: function(vm, params) {
        return vm.$api.post(vm, url.ownPlatformDetail, params)
    },
    // 删除自有平台
    deleteOwnPlatform: function(vm, params) {
        return vm.$api.delete(vm, url.ownPlatformDetail, params)
    },

    // 微信公众平台列表
    getWxPlatformList: function(vm, params) {
        return vm.$api.get(vm, url.getWxPlatformList, params)
    },
    // 微信公众平台列表（不受权限控制）
    getWxPlatformPoList: function(vm, params) {
        return vm.$api.get(vm, url.getWxPlatformPoList, params)
    },
    // 微信公众平台详情
    getWxPlatformDetail: function(vm, params) {
        return vm.$api.get(vm, url.wxPlatformDetail, params)
    },
    // 更新微信公众平台
    updateWxPlatform: function(vm, params) {
        return vm.$api.put(vm, url.wxPlatformDetail, params)
    },
    // 添加微信公众平台
    addWxPlatform: function(vm, params) {
        return vm.$api.post(vm, url.wxPlatformDetail, params)
    },
    // 删除微信公众平台
    deleteWxPlatform: function(vm, params) {
        return vm.$api.delete(vm, url.wxPlatformDetail, params)
    },

    // 客户管理 - 用户管理

    // 柔性用户列表
    getCutomerList: function(vm, params) {
        return vm.$api.get(vm, url.getCutomerList, params)
    },
    // 更新柔性用户状态（解冻/冻结）
    updataCustomerStatus: function(vm, params) {
        return vm.$api.put(vm, url.updataCustomerStatus, params)
    },
    // 订单列表
    orderList: function(vm, params) {
        return vm.$api.get(vm, url.orderList, params)
    },
    // 订单详情
    orderDetail: function(vm, params) {
        return vm.$api.get(vm, url.orderDetail, params)
    },
    // 根据订单Id查询订单编码
    getOrderIdByOrderNo: function(vm, params) {
        return vm.$api.get(vm, url.getOrderIdByOrderNo, params)
    },
    // 分销订单列表
    distributionOrderList: function(vm, params) {
        return vm.$api.get(vm, url.distributionOrderList, params)
    },
    // 分销订单详情
    distributionOrderDetail: function(vm, params) {
        return vm.$api.get(vm, url.distributionOrderDetail, params)
    },
    // 根据订单ID取消订单
    orderCancel: function(vm, params) {
        return vm.$api.put(vm, url.orderCancel, params)
    },
    // 根据订单活动条件ids获取活动信息
    orderPromotion: function(vm, params) {
        return vm.$api.get(vm, url.orderPromotion, params)
    },
    // 柔性定制订单列表
    customerDiyOrderList: function(vm, params) {
        return vm.$api.get(vm, url.customerDiyOrderList, params)
    },
    // 柔性定制订单详情
    customerDiyOrderDetail: function(vm, params) {
        return vm.$api.get(vm, url.customerDiyOrderDetail, params)
    },
    // 柔性同步订单列表
    customerSyncOrderList: function(vm, params) {
        return vm.$api.get(vm, url.customerSyncOrderList, params)
    },
    // 柔性同步订单详情
    customerSyncOrderDetail: function(vm, params) {
        return vm.$api.get(vm, url.customerSyncOrderDetail, params)
    },
    // 订单类型列表
    orderTypeList: function(vm, params) {
        return vm.$api.get(vm, url.orderTypeList, params)
    },
    // 订单类型详情
    orderTypeDetail: function(vm, params) {
        return vm.$api.get(vm, url.orderType, params)
    },
    // 更新订单类型
    updateOrderType: function(vm, params) {
        return vm.$api.put(vm, url.orderType, params)
    },
    // 添加订单类型
    addWxOrderType: function(vm, params) {
        return vm.$api.post(vm, url.orderType, params)
    },
    // 删除订单类型
    deleteOrderType: function(vm, params) {
        return vm.$api.delete(vm, url.orderType, params)
    },
    // 同步erp失败订单列表
    syncERPFailList: function(vm, params) {
        return vm.$api.get(vm, url.syncERPFailList, params)
    },
    // 同步erp失败订单详情
    syncERPFailDetail: function(vm, params) {
        return vm.$api.get(vm, url.syncERPFailDetail, params)
    },
    // 导出同步erp失败订单
    syncERPFailExport: function(vm, params) {
        return vm.$api.get(vm, url.syncERPFailExport, params)
    },
    // 同步工厂失败订单列表
    syncFactoryFailList: function(vm, params) {
        return vm.$api.get(vm, url.syncFactoryFailList, params)
    },
    // 同步工厂失败订单详情
    syncFactoryFailDetail: function(vm, params) {
        return vm.$api.get(vm, url.syncFactoryFailDetail, params)
    },
    // 导出同步工厂失败订单
    syncFactoryFailExport: function(vm, params) {
        return vm.$api.get(vm, url.syncFactoryFailExport, params)
    },
    // 长时间未发货订单列表
    syncUndeliveredFailList: function(vm, params) {
        return vm.$api.get(vm, url.syncUndeliveredFailList, params)
    },
    // 长时间未发货订单详情
    syncUndeliveredFailDetail: function(vm, params) {
        return vm.$api.get(vm, url.syncUndeliveredFailDetail, params)
    },
    // 导出长时间未发货订单
    syncUndeliveredFailExport: function(vm, params) {
        return vm.$api.get(vm, url.syncUndeliveredFailExport, params)
    },
    // 库存锁库订单列表
    orderGoodsStockList: function(vm, params) {
        return vm.$api.get(vm, url.orderGoodsStockList, params)
    },
    // VMI订单明细列表
    vimOrderList: function(vm, params) {
        return vm.$api.get(vm, url.vimOrderList, params)
    },
    // VMI订单明细详情
    vimOrderDetail: function(vm, params) {
        return vm.$api.get(vm, url.vimOrderDetail, params)
    },
    // 发货单列表
    orderDeliverBillList: function(vm, params) {
        return vm.$api.get(vm, url.orderDeliverBillList, params)
    },
    // 发货单详情
    orderDeliverBillDetail: function(vm, params) {
        return vm.$api.get(vm, url.orderDeliverBill, params)
    },
    // 发货单详情
    orderLogistics: function(vm, params) {
        return vm.$api.get(vm, url.orderLogistics, params)
    },
    // 系统日志列表
    systemLogList: function(vm, params) {
        return vm.$api.get(vm, url.systemLogList, params)
    },
    // 分销商用户操作日志列表
    operationLogList: function(vm, params) {
        return vm.$api.get(vm, url.operationLogList, params)
    },
    // 订单日志列表
    orderLogList: function(vm, params) {
        return vm.$api.get(vm, url.orderLogList, params)
    },
    // 发货单日志列表
    orderDeliverBillLogList: function(vm, params) {
        return vm.$api.get(vm, url.orderDeliverBillLogList, params)
    },
    // 收货单日志列表
    voucherLogList: function(vm, params) {
        return vm.$api.get(vm, url.voucherLogList, params)
    },
    // 退款单日志列表
    refundLogList: function(vm, params) {
        return vm.$api.get(vm, url.refundLogList, params)
    },
    // 分销客户提现申请日志列表
    withdrawApplyLogList: function(vm, params) {
        return vm.$api.get(vm, url.withdrawApplyLogList, params)
    },
    // 业务日志列表
    orderBusinessLogList: function(vm, params) {
        return vm.$api.get(vm, url.orderBusinessLogList, params)
    },
    // 消息发送日志列表
    msgcenterLogList: function(vm, params) {
        return vm.$api.get(vm, url.msgcenterLogList, params)
    },
    // 消息详情
    msgcenterDetail: function(vm, params) {
        return vm.$api.get(vm, url.msgcenterDetail, params)
    },
    // 消息再次推送
    msgcenterSendAgain: function(vm, params) {
        return vm.$api.put(vm, url.msgcenterSendAgain, params)
    },
    // 消息列表
    msgcenterList: function(vm, params) {
        return vm.$api.get(vm, url.msgcenterList, params)
    },
    // 添加消息
    addMsgcenter: function(vm, params) {
        return vm.$api.post(vm, url.addMsgcenter, params)
    },
    // 修改消息
    putMsgcenter: function(vm, params) {
        return vm.$api.post(vm, url.putMsgcenter, params)
    },
    // 删除消息
    deleteMsgcenter: function(vm, params) {
        return vm.$api.delete(vm, url.deleteMsgcenter, params)
    },
    // 微信消息模板更新
    templatePut: function(vm, params) {
        return vm.$api.put(vm, url.templatePut, params)
    },
    // 微信消息模板
    templatelist: function(vm, params) {
        return vm.$api.get(vm, url.templatelist, params)
    },

    // 修改订单地址
    updateAddress: function(vm, params) {
        return vm.$api.put(vm, url.updateAddress, params)
    },
    // 同步销售单给ERP
    syncOrderToERP: function(vm, params) {
        return vm.$api.put(vm, url.syncOrderToERP, params)
    },
    // 同步订单到工厂
    syncOrderToFactory: function(vm, params) {
        return vm.$api.put(vm, url.syncOrderToFactory, params)
    },
    // 同步收款单给ERP
    syncVouchersToERP: function(vm, params) {
        return vm.$api.put(vm, url.syncVouchersToERP, params)
    },
    // 同步发货单给ERP
    syncDiyDeliveryOrderToERP: function(vm, params) {
        return vm.$api.put(vm, url.syncDiyDeliveryOrderToERP, params)
    },
    // 同步订单物流给第三方
    syncLogisticsToThird: function(vm, params) {
        return vm.$api.put(vm, url.syncLogisticsToThird, params)
    },
    // ERP分销商同步
    syncDistributorToERP: function(vm, params) {
        return vm.$api.post(vm, url.syncDistributorToERP, params)
    },
    // 同步ERP快递单号
    syncExpressNoToERP: function(vm, params) {
        return vm.$api.post(vm, url.syncExpressNoToERP, params)
    },
    // 同步订单-柔性
    orderPushAgain: function(vm, params) {
        return vm.$api.put(vm, url.orderPushAgain, params)
    },
    // 全量同步货品信息
    syncAllItem: function(vm, params) {
        return vm.$api.put(vm, url.syncAllItem, params)
    },
    // 全量同步货品装箱信息
    syncAllItemBox: function(vm, params) {
        return vm.$api.put(vm, url.syncAllItemBox, params)
    },
    // 全量同步商品价格
    syncAllPrice: function(vm, params) {
        return vm.$api.put(vm, url.syncAllPrice, params)
    },
    // 同步货品信息
    syncBatchItem: function(vm, params) {
        return vm.$api.put(vm, url.syncBatchItem, params)
    },
    // 同步货品装箱信息
    syncBatchItemBox: function(vm, params) {
        return vm.$api.put(vm, url.syncBatchItemBox, params)
    },
    // 同步商品价格
    syncBatchPrice: function(vm, params) {
        return vm.$api.put(vm, url.syncBatchPrice, params)
    },
    // 同步退款单给ERP
    syncRefundBillToERP: function(vm, params) {
        return vm.$api.put(vm, url.syncRefundBillToERP, params)
    },

    // 兑换卡营销专题
    addExchangeSpecial: function(vm, params) {
        return vm.$api.post(vm, url.exchangeSpecial, params)
    }, // 新增营销专题活动
    editExchangeSpecial: function(vm, params) {
        return vm.$api.put(vm, url.exchangeSpecial, params)
    }, // 修改/启用/停用营销专题活动
    exchangeSpecialList: function(vm, params) {
        return vm.$api.get(vm, url.exchangeSpecialList, params)
    }, // 营销专题活动列表
    exchangeSpecialDetail: function(vm, params) {
        return vm.$api.get(vm, url.exchangeSpecialDetail, params)
    }, // 营销专题活动
    exchangeSpecialDistriList: function(vm, params) {
        return vm.$api.get(vm, url.exchangeSpecialDistriList, params)
    }, // 专题列表 - 查看分销商
    exchangeSpecialDistriAdd: function(vm, params) {
        return vm.$api.post(vm, url.exchangeSpecialDistri, params)
    }, // 专题修改 - 添加分销商
    exchangeSpecialDistriUpdate: function(vm, params) {
        return vm.$api.put(vm, url.exchangeSpecialDistri, params)
    }, // 专题修改 - 启用/停用分销商
    exchangeSpecialDistriQrcode: function(vm, params) {
        return vm.$api.post(vm, url.exchangeSpecialDistriQrcode, params)
    }, // 专题列表 - 查看分销商 - 下载二维码
    exchangeSpecialDistriShortLink: function(vm, params) {
        return vm.$api.post(vm, url.exchangeSpecialDistriShortLink, params)
    }, // 专题列表 - 查看分销商 - 查看短链接
    exchangeSpecialDistriLink: function(vm, params) {
        return vm.$api.post(vm, url.exchangeSpecialDistriLink, params)
    }, // 专题列表 - 查看分销商 - 查看专题链接

    // 兑换卡转发活动配置
    addExchangeShare: function(vm, params) {
        return vm.$api.post(vm, url.exchangeShare, params)
    }, // 新增转发活动配置
    editExchangeShare: function(vm, params) {
        return vm.$api.put(vm, url.exchangeShare, params)
    }, // 修改/启用/停用转发活动配置
    deleteExchangeShare: function(vm, params) {
        return vm.$api.delete(vm, url.exchangeShare, params)
    }, // 删除转发活动配置
    exchangeShareList: function(vm, params) {
        return vm.$api.get(vm, url.exchangeShareList, params)
    }, // 转发活动配置列表
    exchangeShareDetail: function(vm, params) {
        return vm.$api.get(vm, url.exchangeShareDetail, params)
    }, // 转发活动配置详情

    // 电子兑换卡导出
    addExchangeExport: function(vm, params) {
        return vm.$api.post(vm, url.exchangeExport, params)
    }, // 新增电子兑换卡导出
    editExchangeExport: function(vm, params) {
        return vm.$api.put(vm, url.exchangeExport, params)
    }, // 修改/审核电子兑换卡导出
    deleteExchangeExport: function(vm, params) {
        return vm.$api.delete(vm, url.exchangeExport, params)
    }, // 删除电子兑换卡导出
    exchangeExportList: function(vm, params) {
        return vm.$api.get(vm, url.exchangeExportList, params)
    }, // 电子兑换卡导出列表
    exchangeExportOut: function(vm, params) {
        return vm.$api.post(vm, url.exchangeExportOut, params)
    }, // 导出电子兑换卡

    // 根据ids查找分销商基本数据
    getDistributorByIds: function(vm, params) {
        return vm.$api.post(vm, url.getDistributorByIds, params)
    },

    // 批量导入订单
    getImportOrderDetail: function(vm, params) {
        return vm.$api.get(vm, url.importOrderDetail, params)
    }, // 导入订单详情
    updateImportOrderDetail: function(vm, params) {
        return vm.$api.put(vm, url.importOrderDetail, params)
    }, // 导入订单详情
    deleteImportOrder: function(vm, params) {
        return vm.$api.delete(vm, url.deleteImportOrder, params)
    }, // 删除导入项
    importOrder: function(vm, params) {
        return vm.$api.post(vm, url.importOrder, params)
    }, // 订单导入接口
    getImportOrder: function(vm, params) {
        return vm.$api.get(vm, url.getImportOrder, params)
    }, // 导入订单列表
    ordersImportOrder: function(vm, params) {
        return vm.$api.put(vm, url.ordersImportOrder, params)
    }, // 导入订单批量下单
    getTempDownload: function(vm, params) {
        return vm.$api.get(vm, url.getTempDownload, params)
    }, // 获取下载模板URL

    // 权益兑换列表
    getEquityList: function(vm, params) {
        return vm.$api.get(vm, url.getEquityList, params)
    }, // 权益兑换列表
    getEquityLogList: function(vm, params) {
        return vm.$api.get(vm, url.getEquityLogList, params)
    }, // 权益兑换日志列表
    handleEquityStatus: function(vm, params) {
        return vm.$api.put(vm, url.handleEquityStatus, params)
    }, // 权益作废
    handleEquityStatusCancel: function(vm, params) {
        return vm.$api.put(vm, url.handleEquityStatusCancel, params)
    }, // 权益恢复

    // 判断当前订单状态
    getOrderStatus: function(vm, params) {
        return vm.$api.get(vm, url.getOrderStatus, params)
    }, // 判断当前订单状态

    // 停发列表（快递停发区域）
    getDeliveryStopList: function(vm, params) {
        return vm.$api.get(vm, url.getDeliveryStopList, params)
    }, // 停发列表
    addDeliveryStop: function(vm, params) {
        return vm.$api.post(vm, url.handleDeliveryStop, params)
    }, // 添加
    editDeliveryStop: function(vm, params) {
        return vm.$api.put(vm, url.handleDeliveryStop, params)
    }, // 更新
    deleteDeliveryStop: function(vm, params) {
        return vm.$api.delete(vm, url.handleDeliveryStop, params)
    }, // 删除

    // 代金券
    batchCreateVoucher: function(vm, params) {
        return vm.$api.post(vm, url.batchCreateVoucher, params)
    }, // 批量新增返利代金券
    getVoucherList: function(vm, params) {
        return vm.$api.get(vm, url.getVoucherList, params)
    }, // 根据搜索条件查找返利代金券列表(分页)
    handleVoucherStatus: function(vm, params) {
        return vm.$api.put(vm, url.handleVoucherStatus, params)
    }, // 更新代金券（冻结/解冻）
    getVoucherUsedList: function(vm, params) {
        return vm.$api.get(vm, url.getVoucherUsedList, params)
    }, // 代金券使用记录列表(分页)
    voucherDownloadTemp: function(vm, params) {
        return vm.$api.get(vm, url.voucherDownloadTemp, params)
    }, // 获取下载模板URL
    voucherImport: function(vm, params) {
        return vm.$api.get(vm, url.voucherImport, params)
    }, // 返利金导入接口

    // 解绑兑换码
    exchangeUnbound: function(vm, params) {
        return vm.$api.delete(vm, url.exchangeUnbound, params)
    },

    // 产品推广（商店配置 - 商品推广）
    proPromotionList: function(vm, params) {
        return vm.$api.get(vm, url.proPromotionList, params)
    }, // 商品推广列表
    addProPromotion: function(vm, params) {
        return vm.$api.post(vm, url.addProPromotion, params)
    }, // 添加商品推广
    proPromotionDetail: function(vm, params) {
        return vm.$api.get(vm, url.proPromotionDetail, params)
    }, // 根据推广商品id获取推广商品详情
    handleProPromotion: function(vm, params) {
        return vm.$api.put(vm, url.handleProPromotion, params)
    }, // 修改商品推广
    deleteProPromotion: function(vm, params) {
        return vm.$api.delete(vm, url.deleteProPromotion, params)
    }, // 根据商品推广id删除商品推广记录
    invalidProPromotion: function(vm, params) {
        return vm.$api.put(vm, url.invalidProPromotion, params)
    }, // 商品推广记录作废

    // 同步物流
    handleSyncLogistics: function(vm, params) {
        return vm.$api.post(vm, url.handleSyncLogistics, params)
    }, // 手动输入同步物流信息
    handleSyncLogisticsById: function(vm, params) {
        return vm.$api.put(vm, url.handleSyncLogisticsById, params)
    }, // 根据订单Id同步物流信息
}
export default SERVICE_API