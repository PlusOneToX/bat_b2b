import Vue from 'vue'
import Router from 'vue-router'
// process：当前node进程
// NODE_ENV有两种可能的值：development, production
const _import = require('./_import_' + process.env.NODE_ENV)
// in development-env not use lazy-loading, because lazy-loading too many pages will cause webpack hot update too slow. so only in production use lazy-loading;
// detail: https://panjiachen.github.io/vue-element-admin-site/#/lazy-loading

Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirct in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
**/
// 导出route映射   子页面路由引入路径，显示页面
// 注意：侧栏的路由数据位于 @/views/layout/Sidebar/sideBarMap.js
export const constantRouterMap = [{
  path: '/login',
  component: _import('login/index'),
  hidden: true
},
{
  path: '/404',
  component: _import('404'),
  hidden: true
},
{
  path: '/',
  component: _import('login/index'),
  hidden: true
},
// {
//   path: '/',
//   component: Layout,
//   redirect: '/dashboard',
//   children: [{
//     path: 'dashboard',
//     name: 'dashboard',
//     component: _import('dashboard/index')
//   }]
// },
// system module
{
  path: '/system',
  component: Layout,
  children: [{
    path: '/system/organization',
    component: _import('system/organization/saleOrganization/index'),
    children: [{
      path: '/system/organization/saleOrganization',
      name: 'saleOrganization',
      meta: {
        title: '销售组织',
        noCache: false
      },
      component: _import('system/organization/saleOrganization/index') // 销售组织
    }]
  }, {
    path: '/system/organization',
    component: _import('system/organization/saleDepartment/index'),
    children: [{
      path: '/system/organization/saleDepartment',
      name: 'saleDepartment',
      meta: {
        title: '销售部门',
        noCache: false
      },
      component: _import('system/organization/saleDepartment/index') // 销售部门
    }]
  }, {
    path: '/system/organization',
    component: _import('system/organization/businessUnit/index'),
    children: [{
      path: '/system/organization/businessUnit',
      name: 'businessUnit',
      meta: {
        title: '事业部',
        noCache: false
      },
      component: _import('system/organization/businessUnit/index') // 事业部
    }]
  }, {
    path: '/system/organization/saleOrganization/editAddOrganization/:id',
    name: 'editOrganization',
    meta: {
      title: '销售组织详情',
      noCache: false
    },
    component: _import('system/organization/saleOrganization/editAddOrganization')
  }, {
    path: '/system/organization/saleOrganization/editAddOrganization',
    name: 'addOrganization',
    meta: {
      title: '添加销售组织',
      noCache: false
    },
    component: _import('system/organization/saleOrganization/editAddOrganization')
  }, {
    path: '/system/organization/saleDepartment/editDepartment/:id',
    name: 'editDepartment',
    meta: {
      title: '销售部门详情',
      noCache: false
    },
    component: _import('system/organization/saleDepartment/editAddDepartment')
  }, {
    path: '/system/organization/saleDepartment/addDepartment',
    name: 'addDepartment',
    meta: {
      title: '添加销售部门',
      noCache: false
    },
    component: _import('system/organization/saleDepartment/editAddDepartment')
  }, {
    path: '/system/organization/businessUnit/editBusinessUnit/:id',
    name: 'editBusinessUnit',
    meta: {
      title: '事业部详情',
      noCache: false
    },
    component: _import('system/organization/businessUnit/editAddBusinessUnit')
  }, {
    path: '/system/organization/businessUnit/addBusinessUnit',
    name: 'addBusinessUnit',
    meta: {
      title: '添加事业部',
      noCache: false
    },
    component: _import('system/organization/businessUnit/editAddBusinessUnit')
  }, {
    path: '/system/user',
    component: _import('system/user/index'),
    children: [{
      path: '/system/user/userlist/index',
      name: 'userlist',
      meta: {
        title: '用户列表',
        noCache: false
      },
      component: _import('system/user/userlist/index') // 用户列表
    },
    // {
    //   path: '/system/user/useradd/:id',
    //   name: 'useradd',
    //   meta: {
    //     noCache: false
    //   },
    //   component: _import('system/user/useredit/index') // 用户列表详情
    // },
    {
      path: '/system/user/useredit/index/:id',
      name: 'useredit',
      meta: {
        title: '用户详情',
        noCache: false
      },
      component: _import('system/user/useredit/index') // 添加用户
    }
    ]
  }, {
    path: '/system/role',
    component: _import('system/role/index'),
    children: [{
      path: '/system/role/rolelist/index',
      name: 'rolelist',
      meta: {
        title: '角色列表',
        noCache: true
      },
      component: _import('system/role/rolelist/index') // 权限管理，角色管理列表
    }, {
      path: '/system/role/roleedit/index/:id',
      name: 'roleedit',
      meta: {
        title: '角色详情',
        noCache: true
      },
      component: _import('system/role/roleedit/index') // 管理角色，详情
    }, {
      path: '/system/role/roleedit/index',
      name: 'roleadd',
      meta: {
        title: '添加角色',
        noCache: true
      },
      component: _import('system/role/roleedit/index') // 管理角色，添加
    }]
  }, {
    path: '/system/approve',
    component: _import('system/approve/index'),
    children: [{
      path: '/system/approve/index',
      name: 'approve',
      meta: {
        title: '审批配置',
        noCache: true
      },
      component: _import('system/approve/index') // 权限管理，审批配置
    }]
  }, {
    path: '/distributor/pay/paylist/addpay',
    name: 'addpay',
    meta: {
      title: '添加收款条件',
      noCache: false
    },
    component: _import('system/pay/paylist/addpay')
  }, {
    path: '/distributor/pay/paylist/editPay/:id',
    name: 'editpay',
    meta: {
      title: '收款条件详情',
      noCache: false
    },
    component: _import('system/pay/paylist/addpay')
  }, {
    path: '/distributor/pay/paylist/index',
    name: 'paylist',
    meta: {
      title: '收款条件',
      noCache: false
    },
    component: _import('system/pay/paylist/index') // 收款条件
  }, {
    path: '/system/manage/index',
    component: _import('system/manage/index'),
    children: [{
      path: '/system/manage/index',
      name: 'managelist',
      meta: {
        title: '配送方式列表',
        noCache: false
      },
      component: _import('system/manage/index') // 系统设置，配送管理，配送方式
    }]
  }, {
    path: '/system/manage/addEditShipping',
    component: _import('system/manage/addEditShipping'),
    children: [{
      path: '/system/manage/addEditShipping',
      name: 'addEditShipping',
      meta: {
        title: '配送方式详情',
        noCache: false
      },
      component: _import('system/manage/addEditShipping') // 系统设置，配送管理，配送方式,添加配送方式
    }]
  }, {
    path: '/system/deliveryStop/index',
    name: 'deliveryStopList',
    meta: {
      title: '停发列表',
      noCache: false
    },
    component: _import('system/deliveryStop/index') // 快递 - 停发列表
  }, {
    path: '/system/log/uLog',
    component: _import('system/log/uLog'),
    children: [{
      path: '/system/log/uLog',
      name: 'uLog',
      meta: {
        title: '系统操作日志',
        noCache: false
      },
      component: _import('system/log/uLog') // 系统操作日志
    }]
  }, {
    path: '/system/log',
    component: _import('system/log/interfaceLog'),
    children: [{
      path: '/system/log/interfaceLog',
      name: 'interfaceLog',
      meta: {
        title: '接口调用日志',
        noCache: false
      },
      component: _import('system/log/interfaceLog') // 接口调用日志
    }]
  }, {
    path: '/system/log',
    component: _import('system/log/sentNews'),
    children: [{
      path: '/system/log/sentNews',
      name: 'sentNews',
      meta: {
        title: '消息发送日志',
        noCache: false
      },
      component: _import('system/log/sentNews') // 消息发送日志
    }]
  }, {
    path: '/system/news',
    component: _import('system/news/newsNotice'),
    children: [{
      path: '/system/news/newsNotice',
      name: 'newsNotice',
      meta: {
        title: '消息通知列表',
        noCache: false
      },
      component: _import('system/news/newsNotice') // 消息通知列表
    }]
  }, {
    path: '/system/news',
    component: _import('system/news/editNewsNotice'),
    children: [{
      path: '/system/news/editNewsNotice',
      name: 'editNewsNotice',
      meta: {
        title: '消息通知列表',
        noCache: false
      },
      component: _import('system/news/editNewsNotice') // 消息通知编辑
    }]
  }, {
    path: '/system/news',
    component: _import('system/news/wechatNewsBoard'),
    children: [{
      path: '/system/news/wechatNewsBoard',
      name: 'wechatNewsBoard',
      meta: {
        title: '微信消息模板',
        noCache: false
      },
      component: _import('system/news/wechatNewsBoard') // 微信消息模板
    }]
  }, {
    path: '/system/manage/verify',
    component: _import('system/manage/verify'),
    children: [{
      path: '/system/manage/verify',
      name: 'verify',
      meta: {
        title: '公式验算对话框',
        noCache: false
      },
      component: _import('system/manage/verify') // 系统设置，配送管理，配送方式,公式验证
    }]
  }, {
    path: '/system/manage/expressReadct',
    component: _import('system/manage/expressReadct'),
    children: [{
      path: '/system/manage/expressReadct',
      name: 'expressReadct',
      meta: {
        title: '配送方式详情',
        noCache: false
      },
      component: _import('system/manage/expressReadct') // 系统设置，配送管理，配送方式,配送方式编辑
    }]
  }, {
    path: '/system/manage/region',
    component: _import('system/manage/region'),
    children: [{
      path: '/system/manage/region',
      name: 'region',
      meta: {
        title: '配送地区',
        noCache: false
      },
      component: _import('system/manage/region') // 系统设置，配送管理，配送方式,地区管理
    }]
  }]
},
// goods module
{
  path: '/goods',
  component: Layout,
  children: [{
    path: '/goods/goodscategory/categorylist/index',
    name: 'categorylist',
    meta: {
      title: '商品分类列表',
      noCache: true
    },
    component: _import('goods/goodscategory/categorylist/index')
  }, {
    path: '/goods/goodscategory/addcategory/index',
    name: 'addcategory',
    meta: {
      title: '商品分类详情',
      noCache: true
    },
    component: _import('goods/goodscategory/addcategory/index')
  }, {
    path: '/goods/goodscategory/recommendcategory/index',
    name: 'recommendcategory',
    meta: {
      title: '推荐分类管理',
      noCache: true
    },
    component: _import('goods/goodscategory/recommendcategory/index')
  }, {
    path: '/goods/goodslist/index',
    name: 'goodslist',
    meta: {
      title: '商品列表',
      noCache: false
    },
    component: _import('goods/goodslist/index')
  }, {
    path: '/goods/addgoods/index',
    name: 'addgoods',
    meta: {
      title: '商品详情',
      noCache: true
    },
    component: _import('goods/addgoods/index') // 商品管理，添加商品
  }, {
    path: '/goods/check/checklist/goodInfoCheckList',
    name: 'goodInfoCheckList',
    meta: {
      title: '商品上下架审批列表',
      noCache: false
    },
    component: _import('goods/check/checklist/goodInfoCheckList')
  }, {
    path: '/goods/check/checklist/goodGradeCheckList',
    name: 'goodGradeCheckList',
    meta: {
      title: '商品等级变动审批',
      noCache: true
    },
    component: _import('goods/check/checklist/goodGradeCheckList')
  }, {
    path: '/goods/check/checkdetail/goodInfoCheckDetail/:id',
    name: 'goodInfoCheckDetail',
    meta: {
      title: '商品上下架审批详情',
      noCache: true
    },
    component: _import('goods/check/checkdetail/goodInfoCheckDetail')
  }, {
    path: '/goods/check/checkdetail/goodGradeCheckDetail/:id',
    name: 'goodGradeCheckDetail',
    meta: {
      title: '商品等级变动审批单',
      noCache: true
    },
    component: _import('goods/check/checkdetail/goodGradeCheckDetail')
  }, {
    path: '/goods/freezegoodslist/index',
    name: 'freezegoodslist',
    meta: {
      title: '冻结商品列表',
      noCache: false
    },
    component: _import('goods/freezegoodslist/index')
  }, {
    path: '/goods/brand/brandlist/index',
    name: 'brandlist',
    meta: {
      title: '商品品牌列表',
      noCache: true
    },
    component: _import('goods/brand/brandlist/index') // 商品，商品品牌列表
  }, {
    path: '/goods/brand/editbrand/:id',
    name: 'editbrand',
    meta: {
      title: '商品品牌详情',
      noCache: true
    },
    component: _import('goods/brand/addbrand/index') // 商品，添加商品品牌
  }, {
    path: '/goods/brand/addbrand',
    name: 'addbrand',
    meta: {
      title: '添加商品品牌',
      noCache: true
    },
    component: _import('goods/brand/addbrand/index') // 商品，编辑商品品牌
  }, {
    path: '/goods/specification/specificationlist/index',
    name: 'specificationlist',
    meta: {
      title: '商品属性列表',
      noCache: false
    },
    component: _import('goods/specification/specificationlist/index')
  }, {
    path: '/goods/specification/addspecification/index',
    name: 'addspecification',
    meta: {
      title: '添加商品属性',
      noCache: true
    },
    component: _import('goods/specification/addspecification/index')
  }, {
    path: '/material/diyMaterial/materialList',
    name: 'materialList',
    meta: {
      title: '定制商品材料列表',
      noCache: true
    },
    component: _import('goods/diyMaterial/materialList') // 定制商品材料列表
  }, {
    path: '/material/diyMaterial/materialAdd',
    name: 'materialAdd',
    meta: {
      title: '添加定制商品材料',
      noCache: true
    },
    component: _import('goods/diyMaterial/materialAdd') // 定制商品材料添加
  }, {
    path: '/material/diyMaterial/materialAdd/:id',
    name: 'materialEdit',
    meta: {
      title: '定制商品材料详情',
      noCache: true
    },
    component: _import('goods/diyMaterial/materialAdd') // 定制商品材料编辑
  }, {
    path: '/material/diyModel/modelList',
    name: 'modelList',
    meta: {
      title: '定制商品型号列表',
      noCache: true
    },
    component: _import('goods/diyModel/modelList') // 定制商品型号列表
  }, {
    path: '/material/diyModel/modelAdd',
    name: 'modelAdd',
    meta: {
      title: '添加定制商品型号',
      noCache: true
    },
    component: _import('goods/diyModel/modelAdd') // 定制商品型号添加
  }, {
    path: '/material/diyModel/modelAdd/:id',
    name: 'modelEdit',
    meta: {
      title: '定制商品型号详情',
      noCache: true
    },
    component: _import('goods/diyModel/modelAdd') // 定制商品型号编辑
  }, {
    path: '/material/diyMaterialModel/materialModelList',
    name: 'materialModelList',
    meta: {
      title: '材料型号关联列表',
      noCache: true
    },
    component: _import('goods/diyMaterialModel/materialModelList') // 定制商品材料型号列表
  }, {
    path: '/material/thirdSku/index',
    name: 'thirdSkuList',
    meta: {
      title: '第三方材质型号关联',
      noCache: true
    },
    component: _import('goods/thirdSku/index') // 第三方材质型号关联
  }, {
    path: '/material/thirdSku/import',
    name: 'thirdSkuImport',
    meta: {
      title: '批量导入第三方数据',
      noCache: true
    },
    component: _import('goods/thirdSku/import') // 第三方材质型号关联导入
  }, {
    path: '/material/pictureCategory/pictureCategoryList',
    name: 'pictureCategoryList',
    meta: {
      title: '图库分类列表',
      noCache: true
    },
    component: _import('material/pictureCategory/pictureCategoryList') // 图库分类列表
  }, {
    path: '/material/pictureCategory/pictureCategory',
    name: 'pictureCategoryAdd',
    meta: {
      title: '添加图库分类',
      noCache: true
    },
    component: _import('material/pictureCategory/pictureCategory') // 图库分类添加
  }, {
    path: '/material/pictureCategory/pictureCategory/:id',
    name: 'pictureCategoryEdit',
    meta: {
      title: '图库分类详情',
      noCache: true
    },
    component: _import('material/pictureCategory/pictureCategory') // 图库分类编辑
  }, {
    path: '/material/picture/pictureList',
    name: 'pictureList',
    meta: {
      title: '图片列表',
      noCache: false
    },
    component: _import('material/picture/pictureList') // 图片列表
  }, {
    path: '/material/picture/pictureAdd',
    name: 'pictureAdd',
    meta: {
      title: '添加图片',
      noCache: true
    },
    component: _import('material/picture/pictureAdd') // 图库分类添加
  }, {
    path: '/material/picture/pictureAdd/:id',
    name: 'pictureEdit',
    meta: {
      title: '图片详情',
      noCache: true
    },
    component: _import('material/picture/pictureAdd') // 图库分类编辑
  }, {
    path: '/material/label/labelList',
    name: 'labelList',
    meta: {
      title: '标签列表',
      noCache: false
    },
    component: _import('material/label/labelList') // 标签列表
  }, {
    path: '/material/label/labelAdd',
    name: 'labelAdd',
    meta: {
      title: '添加标签',
      noCache: true
    },
    component: _import('material/label/labelAdd') // 标签添加
  }, {
    path: '/material/label/labelAdd/:id',
    name: 'labelEdit',
    meta: {
      title: '标签详情',
      noCache: true
    },
    component: _import('material/label/labelAdd') // 标签编辑
  }, {
    path: '/material/font/fontList',
    name: 'fontList',
    meta: {
      title: '字体列表',
      noCache: false
    },
    component: _import('material/font/fontList') // 字体列表
  }, {
    path: '/material/font/font',
    name: 'fontAdd',
    meta: {
      title: '添加字体',
      noCache: true
    },
    component: _import('material/font/font') // 字体添加
  }, {
    path: '/material/font/font/:id',
    name: 'fontEdit',
    meta: {
      title: '字体详情',
      noCache: true
    },
    component: _import('material/font/font') // 字体编辑
  }, {
    path: '/material/label/labelPreview',
    name: 'labelPreview',
    meta: {
      title: '标签预览',
      noCache: true
    },
    component: _import('material/label/labelPreview') // 标签预览
  }, {
    path: '/material/label/pdfPreview',
    name: 'pdfPreview',
    meta: {
      title: '标签预览',
      noCache: true
    },
    component: _import('material/label/pdfPreview') // 标签预览
  }, {
    path: '/material/productCategory/productCategoryList',
    name: 'productCategoryList',
    meta: {
      title: '产品类型列表',
      noCache: true
    },
    component: _import('material/productCategory/productCategoryList') // 产品类型列表
  }, {
    path: '/goods/specification/addspecification/index/:id',
    name: 'editspecification',
    meta: {
      title: '商品属性详情',
      noCache: true
    },
    component: _import('goods/specification/addspecification/index')
  }, {
    path: '/goods/productline/addproductline/index',
    name: 'addproductline',
    meta: {
      title: '添加品类',
      noCache: true
    },
    component: _import('goods/productline/addproductline/index')
  }, {
    path: '/goods/productline/addproductline/index/:id',
    name: 'editproductline',
    meta: {
      title: '品类详情',
      noCache: true
    },
    component: _import('goods/productline/addproductline/index')
  }, {
    path: '/goods/productline/productlinelist/index',
    name: 'productlinelist',
    meta: {
      title: '品类列表',
      noCache: false
    },
    component: _import('goods/productline/productlinelist/index')
  }, {
    path: '/goods/goodslabel/goodslabel/index',
    name: 'addgoodslabel',
    meta: {
      title: '添加商品标签',
      noCache: true
    },
    component: _import('goods/goodslabel/goodslabel/index') // 商品标签添加
  }, {
    path: '/goods/goodslabel/goodslabel/index/:id',
    name: 'editgoodslabel',
    meta: {
      title: '商品标签详情',
      noCache: true
    },
    component: _import('goods/goodslabel/goodslabel/index') // 商品标签编辑
  }, {
    path: '/goods/goodslabel/goodslabellist/index',
    name: 'goodslabellist',
    meta: {
      title: '商品标签列表',
      noCache: false
    },
    component: _import('goods/goodslabel/goodslabellist/index') // 商品标签列表
  }, {
    path: '/goods/restriction/restrictionlist/index',
    name: 'restrictionlist',
    meta: {
      title: '限购规则列表',
      noCache: true
    },
    component: _import('goods/restriction/restrictionlist/index') // 商品，限购管理，限购规则列表
  }, {
    path: '/goods/restriction/addrestriction/CheckLin',
    name: 'checklin',
    meta: {
      title: '限购规则详情',
      noCache: true
    },
    component: _import('goods/restriction/addrestriction/CheckLin') // 商品，限购管理，限购规则详情父组件
  }, {
    path: '/goods/restriction/addrestriction/components/addrestriction',
    name: 'editrestriction',
    meta: {
      noCache: true
    },
    component: _import('goods/restriction/addrestriction/components/addrestriction') // 商品，限购管理，规则列表，查看限购规则
  }, {
    path: '/goods/goodGrade/goodGradeList/index',
    name: 'goodGradeList',
    meta: {
      title: '商品等级列表',
      noCache: false
    },
    component: _import('goods/goodGrade/goodGradeList/index') // 商品等级列表
  }, {
    path: '/goods/goodGrade/goodGradeList/goodChangeGrade',
    name: 'goodChangeGrade',
    meta: {
      noCache: true
    },
    component: _import('goods/goodGrade/goodGradeList/goodChangeGrade') // 调整等级
  }, {
    path: '/goods/goodGrade/goodGradeList/goodStopProduction',
    name: 'goodStopProduction',
    meta: {
      noCache: false
    },
    component: _import('goods/goodGrade/goodGradeList/goodStopProduction') // 添加停产商品
  }, {
    path: '/goods/goodGrade/goodGradeList/goodhistoryGrade/:id',
    name: 'goodhistoryGrade',
    meta: {
      title: '查看历史等级',
      noCache: false
    },
    component: _import('goods/goodGrade/goodGradeList/goodhistoryGrade') // 查看历史等级
  }, {
    path: '/goods/goodGrade/goodGradeBase/index',
    name: 'goodGradeBase',
    meta: {
      title: '商品等级列表',
      noCache: true
    },
    component: _import('goods/goodGrade/goodGradeBase/index') // 商品等级列表
  }, {
    path: '/goods/goodGrade/goodGradeBase/goodGradeDetail',
    name: 'addgoodGrade',
    meta: {
      title: '添加商品等级',
      noCache: true
    },
    component: _import('goods/goodGrade/goodGradeBase/goodGradeDetail') // 添加等级
  }, {
    path: '/goods/goodGrade/goodGradeBase/goodGradeDetail/:id',
    name: 'editgoodGrade',
    meta: {
      title: '商品等级详情',
      noCache: true
    },
    component: _import('goods/goodGrade/goodGradeBase/goodGradeDetail') // 编辑等级
  }, {
    path: '/goods/goodGrade/goodGradeRule/index',
    name: 'goodGradeRule',
    meta: {
      title: '商品等级规则设置',
      noCache: true
    },
    component: _import('goods/goodGrade/goodGradeRule/index') // 商品等级变动设置
  }, {
    path: '/goods/goodGrade/goodGradeChangeList/index',
    name: 'goodGradeChangeList',
    meta: {
      title: '商品等级变动列表',
      noCache: false
    },
    component: _import('goods/goodGrade/goodGradeChangeList/index') // 商品等级变动列表
  }, {
    path: '/goods/goodGrade/goodGradeChangeList/goodGradeChangeDetail',
    name: 'goodGradeChangeDetail',
    meta: {
      title: '商品等级变动确认',
      noCache: true
    },
    component: _import('goods/goodGrade/goodGradeChangeList/goodGradeChangeDetail') // 等级变动详情
  }, {
    path: '/goods/salesManage',
    name: 'salesManage',
    meta: {
      title: '商品销量管理',
      noCache: false
    },
    component: _import('goods/salesManage/index') // 商品销量管理
  }]
},
// 分销商/用户
{
  path: '/distributor',
  component: Layout,
  children: [{
    path: '/distributor/grade/index',
    name: 'distributorlist',
    meta: {
      title: '分销商销售区域',
      noCache: false
    },
    // component: _import('distributor/grade/index')
    component: resolve => require(['@/views/distributor/grade/index'], resolve) // 路由懒加载方式 分销商设置，销售区域
  }, {
    path: '/distributor/gradeRedact/index',
    name: 'distributorgraderedact',
    meta: {
      title: '分销商销售区域详情',
      noCache: false
    },
    component: _import('distributor/gradeRedact/index') // 分销商设置，添加销售区域
  }, {
    path: '/goods/area/index',
    name: 'distributorarea',
    meta: {
      title: '分销商价格等级',
      noCache: false
    },
    component: _import('distributor/area/index') // 分销商设置，分销商等级
  }, {
    path: '/goods/areaRedact/index',
    name: 'distributorareaRedact',
    meta: {
      title: '分销商价格等级详情',
      noCache: false
    },
    component: _import('distributor/areaRedact/index') // 分销商设置，编辑分销商等级
  }, {
    path: '/distributor/Grouping/index',
    name: 'distributorGrouping',
    meta: {
      title: '分销商分组',
      noCache: false
    },
    component: _import('distributor/Grouping/index') // 分销商设置，分销商分组
  }, {
    path: '/distributor/GroupingRedact/index',
    name: 'distributorGroupingRedact',
    meta: {
      title: '分销商分组详情',
      noCache: false
    },
    component: _import('distributor/GroupingRedact/index') // 分销商设置，查看分销商分组
  }, {
    path: '/distributor/Classes/index',
    name: 'distributorClasses',
    meta: {
      title: '分销商类别',
      noCache: false
    },
    component: _import('distributor/Classes/index') // 分销商设置，分销商类别
  }, {
    path: '/distributor/ClassesRedact/index',
    name: 'distributorClassesRedact',
    meta: {
      title: '分销商类别详情',
      noCache: false
    },
    component: _import('distributor/ClassesRedact/index') // 分销商设置，查看分销商类别
  }, {
    path: '/distributor/cooperating/cooperatinglist/index',
    name: 'distributorcooperating',
    meta: {
      title: '合作中的分销商列表',
      noCache: false
    },
    component: _import('distributor/cooperating/cooperatinglist/index') // 合作中的分销商列表
  },
  {
    path: '/distributor/cooperating/cooperatingnList/index',
    name: 'distributorcooperatingn',
    meta: {
      title: '合作中的分销商列表',
      noCache: false
    },
    component: _import('distributor/cooperating/cooperatingnList/index') // 合作中的分销商列表（多级）
  },
  {
    path: '/distributor/cooperating/cooperatingadd/index',
    name: 'distributorcooperatingadd',
    meta: {
      title: '分销商详情',
      noCache: true
    },
    component: _import('distributor/cooperating/cooperatingadd/index') // 合作中添加合作分销商
  }, {
    path: '/distributor/application/index',
    name: 'Dapplication',
    meta: {
      title: '分销商申请列表',
      noCache: false
    },
    component: _import('distributor/application/index') // 申请中的分销商列表
  }, {
    path: '/distributor/cooperatingfreeze/index',
    name: 'Dcooperatingfreeze',
    meta: {
      title: '已冻结的分销商列表',
      noCache: false
    },
    component: _import('distributor/cooperatingfreeze/index') // 已冻结分销商列表
  }, {
    path: '/distributor/cooperatingfreeze/multistage',
    name: 'DcooperatingfreezeN',
    meta: {
      title: '已冻结的分销商列表',
      noCache: false
    },
    component: _import('distributor/cooperatingfreeze/multistage') // 已冻结分销商列表（多级）
  }, {
    path: '/distributor/check/index',
    name: 'distributorcheck',
    meta: {
      title: '分销商审批列表',
      noCache: false
    },
    component: _import('distributor/check/index') // 合作中分销商，审批列表
  }, {
    path: '/distributor/check/multistage',
    name: 'distributorcheckN',
    meta: {
      title: '分销商资料审批列表',
      noCache: true
    },
    component: _import('distributor/check/multistage') // 合作中分销商，审批列表（多级）
  }, {
    path: '/distributor/check/checkDetail',
    name: 'checkDetail',
    meta: {
      title: '分销商资料审批详情',
      noCache: true
    },
    component: _import('distributor/check/checkDetail') // 合作中分销商，审批详情（多级）
  }, {
    path: '/distributor/check/checkAdd',
    name: 'checkAdd',
    meta: {
      title: '分销商资料审批详情',
      noCache: true
    },
    component: _import('distributor/check/checkAdd') // 合作中分销商，审批详情（多级）
  }, {
    path: 'distributor/rxCustomer/sysPlatform/index',
    name: 'sysPlatformList',
    meta: {
      title: '系统平台列表',
      noCache: true
    },
    component: _import('distributor/rxCustomer/sysPlatform/index') // 系统平台列表
  }, {
    path: 'distributor/rxCustomer/sysPlatform/editAddSys',
    name: 'editAddSysPlatform',
    meta: {
      title: '系统平台详情',
      noCache: true
    },
    component: _import('distributor/rxCustomer/sysPlatform/editAddSysPlatform') // 系统平台详情
  }, {
    path: '/distributor/ownPlatform/index',
    name: 'ownPlatformList',
    meta: {
      title: '自有平台列表',
      noCache: false
    },
    component: _import('distributor/rxCustomer/ownPlatform/index') // 自有平台列表
  }, {
    path: '/distributor/ownPlatform/detail',
    name: 'ownPlatformDetail',
    meta: {
      title: '自有平台详情',
      noCache: true
    },
    component: _import('distributor/rxCustomer/ownPlatform/detail') // 自有平台详情
  }, {
    path: '/distributor/rxCustomer/wxPlatform/index',
    name: 'wxPlatformList',
    meta: {
      title: '微信公众平台列表',
      noCache: true
    },
    component: _import('distributor/rxCustomer/wxPlatform/index') // 微信公众平台列表
  }, {
    path: 'distributor/rxCustomer/wxPlatform/editAddWx',
    name: 'editAddWxPlatform',
    meta: {
      title: '微信公众平台详情',
      noCache: true
    },
    component: _import('distributor/rxCustomer/wxPlatform/editAddWxPlatform') // 微信公众平台详情
  }, {
    path: '/distributor/customerlist/index',
    name: 'customerlist',
    meta: {
      title: '柔性用户列表',
      noCache: false
    },
    component: _import('distributor/rxCustomer/customerlist/index') // 用户管理 - 柔性用户列表
  }, {
    path: '/distributor/subAccountlist',
    name: 'subAccountlist',
    meta: {
      title: '分账记录',
      noCache: false
    },
    component: _import('distributor/subAccount/recordlist') // 分账管理 - 分账记录
  }, {
    path: '/distributor/subAccountConfig',
    name: 'subAccountConfig',
    meta: {
      title: '分账配置',
      noCache: false
    },
    component: _import('distributor/subAccount/configlist') // 分账管理 - 分账配置
  }, {
    path: '/distributor/subAccountSaleman',
    name: 'subAccountSaleman',
    meta: {
      title: '分账业务员',
      noCache: false
    },
    component: _import('distributor/subAccount/salelist') // 分账管理 - 分账业务员
  }]
},
// warehouse module
{
  path: '/warehouse',
  component: Layout,
  children: [{
    path: '/warehouse/timeSet/index',
    name: 'warehouseTimeSet',
    meta: {
      title: '仓库基本设置',
      noCache: false
    },
    component: _import('warehouse/timeSet/index') // 仓库，基本设置
  }, {
    path: '/warehouse/list/index',
    name: 'warehouseList',
    meta: {
      title: '仓库列表',
      noCache: false
    },
    component: _import('warehouse/list/index') // 仓库，仓库列表
  }, {
    path: '/warehouse/listRedact/index',
    name: 'listRedact',
    meta: {
      noCache: false
    },
    component: _import('warehouse/listRedact/index') // 仓库，仓库查看
  }, {
    path: '/warehouse/add/index',
    name: 'warehouseAdd',
    meta: {
      title: '仓库详情',
      noCache: false
    },
    component: _import('warehouse/add/index') // 仓库，添加仓库页面
  }, {
    path: '/warehouse/recycle/index',
    name: 'warehouseRecycle',
    meta: {
      title: '仓库回收站',
      noCache: false
    },
    component: _import('warehouse/recycle/index')
  }, {
    path: '/warehouse/stocklist/index',
    name: 'stockList',
    meta: {
      title: '商品库存列表',
      noCache: false
    },
    component: _import('warehouse/stocklist/index') // 仓库，仓库库存，商品库存列表
  }, {
    path: '/warehouse/stockAdjust/index',
    name: 'stockAdjust',
    meta: {
      noCache: false
    },
    component: _import('warehouse/stockAdjust/index')
  }, {
    path: '/warehouse/stockReserved/stockReservedDetail',
    name: 'stockReservedDetail',
    meta: {
      title: '库存预留详情',
      noCache: false
    },
    component: _import('warehouse/stockReserved/stockReservedDetail')
  }, {
    path: '/warehouse/stockReserved/index',
    name: 'stockReservedList',
    meta: {
      title: '库存预留明细',
      noCache: false
    },
    component: _import('warehouse/stockReserved/index')
  }, {
    path: '/warehouse/stockDetail/index',
    name: 'stockDetail',
    meta: {
      noCache: false
    },
    component: _import('warehouse/stockDetail/index')
  }, {
    path: '/warehouse/reserveApproval/index',
    name: 'reserveApproval',
    meta: {
      noCache: false
    },
    component: _import('warehouse/reserveApproval/index')
  }, {
    path: '/warehouse/adjustApproval/index',
    name: 'adjustApproval',
    meta: {
      noCache: false
    },
    component: _import('warehouse/adjustApproval/index')
  }, {
    path: '/warehouse/stockAllocationList/index',
    name: 'stockAllocationList',
    meta: {
      noCache: false
    },
    component: _import('warehouse/stockAllocationList/index')
  }, {
    path: '/warehouse/stockAllocation/index',
    name: 'stockAllocation',
    meta: {
      noCache: false
    },
    component: _import('warehouse/stockAllocation/index')
  }]
},
// 财务
{
  path: '/financial',
  component: Layout,
  children: [{
    path: '/financial/account/index',
    name: 'account',
    meta: {
      noCache: true
    },
    component: _import('financial/account/index')
  }, {
    path: '/financial/addaccount/index',
    name: 'addaccount',
    meta: {
      noCache: false
    },
    component: _import('financial/addaccount/index')
  }, {
    path: '/financial/accountdetail/index',
    name: 'accountdetail',
    meta: {
      noCache: false
    },
    component: _import('financial/accountdetail/index')
  }, {
    path: '/financial/rechargeSet/index',
    name: 'rechargeSet',
    meta: {
      noCache: false
    },
    component: _import('financial/rechargeSet/index') // 预存款设置
  }, {
    path: '/financial/underlineSet/index',
    name: 'underlineSet',
    meta: {
      title: '线下账户设置列表',
      noCache: false
    },
    component: _import('financial/underlineSet/index') // 线下账户设置
  }, {
    path: '/financial/exchangeRate/index',
    name: 'exchangeRate',
    meta: {
      title: '汇率设置列表',
      noCache: false
    },
    component: _import('financial/exchangeRate/index') // 汇率设置
  }, {
    path: '/financial/exchangeRate/editAddRate',
    name: 'editAddRate',
    meta: {
      title: '汇率设置',
      noCache: false
    },
    component: _import('financial/exchangeRate/editAddRate') // 添加汇率
  }, {
    path: '/financial/platformAccount/wxAccount/index',
    name: 'wxPayList',
    meta: {
      title: '微信支付账户',
      noCache: true
    },
    component: _import('financial/platformAccount/wxAccount/index') // 平台帐户--微信支付账户
  }, {
    path: '/financial/platformAccount/wxAccount/editWx',
    name: 'editWxPay',
    meta: {
      title: '微信账户详情',
      noCache: true
    },
    component: _import('financial/platformAccount/wxAccount/editWx') // 平台帐户--添加编辑微信支付账户
  }, {
    path: '/financial/platformAccount/alipayAccount/index',
    name: 'alipayList',
    meta: {
      title: '支付宝账户',
      noCache: true
    },
    component: _import('financial/platformAccount/alipayAccount/index') // 平台帐户--支付宝账户
  }, {
    path: '/financial/platformAccount/alipayAccount/editAlipay',
    name: 'editAlipay',
    meta: {
      title: '支付宝账户详情',
      noCache: true
    },
    component: _import('financial/platformAccount/alipayAccount/editAlipay') // 平台帐户--添加编辑支付宝账户
  }, {
    path: '/financial/platformAccount/quickAccount/index',
    name: 'quickPayList',
    meta: {
      title: '快钱账户',
      noCache: true
    },
    component: _import('financial/platformAccount/quickAccount/index') // 平台帐户--快钱账户
  }, {
    path: '/financial/platformAccount/quickAccount/editQuick',
    name: 'editQuick',
    meta: {
      title: '快钱账户详情',
      noCache: true
    },
    component: _import('financial/platformAccount/quickAccount/editQuick') // 平台帐户--添加编辑快钱账户
  }, {
    path: '/financial/platformAccount/offlineAccount/index',
    name: 'offlineList',
    meta: {
      title: '线下转账账户',
      noCache: true
    },
    component: _import('financial/platformAccount/offlineAccount/index') // 平台帐户--线下转账账户
  }, {
    path: '/financial/platformAccount/offlineAccount/editOffline',
    name: 'editOffline',
    meta: {
      title: '线下转账账户详情',
      noCache: true
    },
    component: _import('financial/platformAccount/offlineAccount/editOffline') // 平台帐户--添加编辑线下转账账户
  }, {
    path: '/financial/distributorAccount/wxAccount/index',
    name: 'wxAccountList',
    meta: {
      title: '微信支付账户列表',
      noCache: true
    },
    component: _import('financial/distributorAccount/wxAccount/index') // 微信账户设置
  }, {
    path: '/financial/distributorAccount/wxAccount/editAddWx',
    name: 'editAddWx',
    meta: {
      title: '微信账户详情',
      noCache: true
    },
    component: _import('financial/distributorAccount/wxAccount/editAddWx') // 添加编辑微信账户
  }, {
    path: '/financial/distributorAccount/alipayAccount/index',
    name: 'alipayAccountList',
    meta: {
      title: '支付宝账户列表',
      noCache: true
    },
    component: _import('financial/distributorAccount/alipayAccount/index') // 支付宝账户设置
  }, {
    path: '/financial/distributorAccount/alipayAccount/editAddAlipay',
    name: 'editAddAlipay',
    meta: {
      title: '支付宝账户详情',
      noCache: true
    },
    component: _import('financial/distributorAccount/alipayAccount/editAddAlipay') // 添加编辑支付宝账户
  }, {
    path: '/financial/currency/index',
    name: 'currency',
    meta: {
      title: '币别',
      noCache: false
    },
    component: _import('financial/currency/index') // 币别
  }, {
    path: '/financial/underlineSetList/index',
    name: 'underlineSetList',
    meta: {
      title: '线下账户设置',
      noCache: false
    },
    component: _import('financial/underlineSetList/index') // 线下账户设置,添加页面
  }, {
    path: '/financial/receipt/index',
    name: 'receipt',
    meta: {
      title: '收款单列表',
      noCache: false
    },
    component: _import('financial/receipt/index') // 财务系统，销售往来单据，收款单页面
  }, {
    path: '/financial/receiptDetail/index',
    name: 'receiptDetail',
    meta: {
      title: '收款单详情',
      noCache: false
    },
    component: _import('financial/receiptDetail/index') // 财务系统，销售往来单据，收款单详情页面
  }, {
    path: '/financial/refundOrder/index',
    name: 'refundOrder',
    meta: {
      title: '退款单列表',
      noCache: false
    },
    component: _import('financial/refundOrder/index') // 财务系统，销售往来单据，退款单列表页面
  }, {
    path: '/financial/refundApply/index',
    name: 'refundApply',
    meta: {
      title: '退款申请单列表',
      noCache: false
    },
    component: _import('financial/refundApply/index') // 财务系统，销售往来单据，退款申请单列表
  }, {
    path: '/financial/refundOrderDetail/index',
    name: 'refundOrderDetail',
    meta: {
      title: '退款单详情',
      noCache: false
    },
    component: _import('financial/refundOrderDetail/index') // 财务系统，销售往来单据，退款单详情页面
  }, {
    path: '/financial/refundOrderHandle/index',
    name: 'refundOrderHandle',
    meta: {
      title: '退款操作',
      noCache: false
    },
    component: _import('financial/refundOrderHandle/index') // 财务系统，销售往来单据，退款单列表退货操作页面
  }, {
    path: '/financial/PrechargeList/index',
    name: 'PrechargeList',
    meta: {
      title: '收款列表',
      noCache: false
    },
    component: _import('financial/PrechargeList/index') // 财务系统，预存款，收款列表
  }, {
    path: '/financial/PrechargeList/prechargeData',
    name: 'prechargeData',
    meta: {
      title: '收款详情',
      noCache: false
    },
    component: _import('financial/PrechargeList/prechargeData') // 财务系统，预存款，收款详情页面
  }, {
    path: '/financial/frozen/index',
    name: 'frozen',
    meta: {
      title: '冻结列表',
      noCache: false
    },
    component: _import('financial/frozen/index')
  }, {
    path: '/financial/drawing/index',
    name: 'drawing',
    meta: {
      title: '提现列表',
      noCache: false
    },
    component: _import('financial/drawing/index') // 订单，提现列表
  }, {
    path: '/financial/drawing/withdrawDetail',
    name: 'withdrawDetail',
    meta: {
      title: '提现详情',
      noCache: false
    },
    component: _import('financial/drawing/withdrawDetail') // 订单，提现列表，提现详情
  }, {
    path: '/financial/prechargeDetail/index',
    name: 'prechargeDetail',
    meta: {
      title: '预存款明细账',
      noCache: false
    },
    component: _import('financial/prechargeDetail/index')
  }, {
    path: '/financial/balanceList/index',
    name: 'balanceList',
    meta: {
      title: '预存款余额表',
      noCache: false
    },
    component: _import('financial/balanceList/index')
  }, {
    path: '/financial/voucher/list',
    name: 'voucherList',
    meta: {
      title: '代金券列表',
      noCache: false
    },
    component: _import('financial/voucher/list')
  }, {
    path: '/financial/voucher/check',
    name: 'voucherCheck',
    meta: {
      title: '代金券审批',
      noCache: false
    },
    component: _import('financial/voucher/check')
  }, {
    path: '/financial/voucher/check/detail',
    name: 'voucherCheckDetail',
    meta: {
      title: '代金券审批详情',
      noCache: false
    },
    component: _import('financial/voucher/checkDetail')
  }, {
    path: '/financial/invoiceList/index',
    name: 'invoiceList',
    meta: {
      title: '发票管理列表',
      noCache: false
    },
    component: _import('financial/invoiceList/index')
  }, {
    path: '/financial/billsAge/index',
    name: 'billsAge',
    meta: {
      title: '账龄分析列表',
      noCache: false
    },
    component: _import('financial/billsAge/index')
  }, {
    path: '/financial/invoiceList/createInvoice',
    props: true,
    name: 'createInvoice',
    meta: {
      title: '添加发票管理',
      noCache: false
    },
    component: _import('financial/invoiceList/createInvoice')
  }, {
    path: '/financial/invoiceList/checkInvoice/:invoId',
    props: true,
    name: 'checkInvoice',
    meta: {
      title: '发票管理详情',
      noCache: false
    },
    component: _import('financial/invoiceList/checkInvoice')
  }, {
    path: '/financial/invoiceList/components/receiveMoney/:invoId',
    props: true,
    name: 'receiveMoney',
    meta: {
      title: '发票收款详情',
      noCache: false
    },
    component: _import('financial/invoiceList/components/receiveMoney')
  }]
},
// order module
{
  path: '/order',
  component: Layout,
  children: [{
    path: '/order/orderList/index',
    name: 'orderList',
    meta: {
      title: '订单列表',
      noCache: false
    },
    component: _import('order/orderList/index') // 订单，订单列表/
  }, {
    path: '/order/diyOrderList/index',
    name: 'diyOrderList',
    meta: {
      title: '柔性定制订单列表',
      noCache: false
    },
    component: _import('order/diyOrderList/index') // 柔性定制订单列表
  }, {
    path: '/order/diyStoreOrderList/index',
    name: 'diyStoreOrderList',
    meta: {
      title: '柔性定制店铺订单列表',
      noCache: false
    },
    component: _import('order/diyStoreOrderList/index') // 柔性定制店铺订单列表
  }, {
    path: '/order/orderList/orderDetail/index',
    name: 'orderDetail',
    meta: {
      title: '订单详情',
      noCache: true
    },
    component: _import('order/orderList/orderDetail/index') // 订单，查看订单详情
  }, {
    path: '/order/orderDistrList/index',
    name: 'orderDistrList',
    meta: {
      title: '分销订单列表',
      noCache: false
    },
    component: _import('order/orderDistrList/index') // 分销订单列表
  }, {
    path: '/order/customerDiyOrderList/index',
    name: 'customerDiyOrderList',
    meta: {
      title: '柔性定制订单',
      noCache: false
    },
    component: _import('order/customerDiyOrderList/index') // 柔性定制订单
  }, {
    path: '/order/customerSyncOrderList/index',
    name: 'customerSyncOrderList',
    meta: {
      title: '柔性同步订单',
      noCache: false
    },
    component: _import('order/customerSyncOrderList/index') // 柔性同步订单
  }, {
    path: '/order/exceptionOrderList/syncErpList',
    name: 'syncERPFailList',
    meta: {
      title: '同步erp失败订单',
      noCache: false
    },
    component: _import('order/exceptionOrderList/syncErpList') // 同步erp失败订单
  }, {
    path: '/order/exceptionOrderList/syncFactoryList',
    name: 'syncFactoryFailList',
    meta: {
      title: '同步工厂失败订单',
      noCache: false
    },
    component: _import('order/exceptionOrderList/syncFactoryList') // 同步工厂失败订单
  }, {
    path: '/order/exceptionOrderList/syncUndeliveredList',
    name: 'syncUndeliveredFailList',
    meta: {
      title: '长时间未发货订单',
      noCache: false
    },
    component: _import('order/exceptionOrderList/syncUndeliveredList') // 长时间未发货订单
  }, {
    path: '/order/orderList/edit/editDistributionMode',
    name: 'editDistributionMode',
    meta: {
      noCache: true
    },
    component: _import('order/orderList/edit/editDistributionMode')
  }, {
    path: '/order/orderList/edit/editOtherInfo',
    name: 'editOtherInfo',
    meta: {
      noCache: true
    },
    component: _import('order/orderList/edit/editOtherInfo')
  }, {
    path: '/order/orderList/edit/editDeliveryInfo',
    name: 'editDeliveryInfo',
    meta: {
      noCache: true
    },
    component: _import('order/orderList/edit/editDeliveryInfo')
  }, {
    path: '/order/orderList/edit/editGoodsInfo',
    name: 'editGoodsInfo',
    meta: {
      noCache: true
    },
    component: _import('order/orderList/edit/editGoodsInfo')
  }, {
    path: '/order/orderList/edit/editCostInfo',
    name: 'editCostInfo',
    meta: {
      noCache: true
    },
    component: _import('order/orderList/edit/editCostInfo')
  }, {
    path: '/order/orderList/operation/noPay',
    name: 'noPay',
    meta: {
      noCache: true
    },
    component: _import('order/orderList/operation/noPay')
  }, {
    path: '/order/orderList/operation/createDelivery',
    name: 'createDelivery',
    meta: {
      noCache: true
    },
    component: _import('order/orderList/operation/createDelivery')
  }, {
    path: '/order/orderList/operation/cancellation',
    name: 'cancellation',
    meta: {
      noCache: true
    },
    component: _import('order/orderList/operation/cancellation') // 订单系统，详情，作废订单页面
  }, {
    path: '/order/orderList/operation/invalid',
    name: 'invalid',
    meta: {
      noCache: true
    },
    component: _import('order/orderList/operation/invalid') // 订单系统，详情，取消订单操作页面
  }, {
    path: '/order/orderExchangeGoodsList/index',
    name: 'orderExchangeGoodsList',
    meta: {
      noCache: false
    },
    component: _import('order/orderExchangeGoodsList/index') // 订单系统，退换货申请列表页面
  }, {
    path: '/order/orderExchangeGoodsList/orderRedact',
    name: 'orderRedact',
    meta: {
      noCache: true
    },
    component: _import('order/orderExchangeGoodsList/orderRedact') // 订单系统，退换货编辑
  }, {
    path: 'order/orderExchangeGoodsList/edit/editSure',
    name: 'editsure',
    meta: {
      noCache: false
    },
    component: _import('order/orderExchangeGoodsList/edit/editSure')
  }, {
    path: 'order/orderExchangeGoodsList/edit/editRedact',
    name: 'editRedact',
    meta: {
      noCache: false
    },
    component: _import('order/orderExchangeGoodsList/edit/editRedact') // 订单系统，退换货，换货收货人编辑
  },
  // {
  //   path: 'order/orderExchangeGoodsList/index',
  //   name: 'orderExchangeGoodsList',
  //   meta: {
  //     noCache: false
  //   },
  //   component: _import('order/orderExchangeGoodsList/index') // 订单系统，退换货申请详情列表页面
  // },
  {
    path: 'order/orderExchangeGoodsList/edit/editReturn',
    name: 'editReturn',
    meta: {
      noCache: false
    },
    component: _import('order/orderExchangeGoodsList/edit/editReturn') // 订单系统，退换货，退货页面
  }, {
    path: 'order/orderExchangeGoodsList/edit/editAOG',
    name: 'editAOG',
    meta: {
      noCache: false
    },
    component: _import('order/orderExchangeGoodsList/edit/editAOG') // 订单系统，退换货，到货页面
  }, {
    path: 'order/orderExchangeGoodsList/edit/editExchange',
    name: 'editExchange',
    meta: {
      noCache: false
    },
    component: _import('order/orderExchangeGoodsList/edit/editExchange') // 订单系统，退换货，换货页面
  }, {
    path: 'order/orderExchangeGoodsList/edit/editGetout',
    name: 'editGetout',
    meta: {
      noCache: false
    },
    component: _import('order/orderExchangeGoodsList/edit/editGetout') // 订单系统，退换货，违规页面
  }, {
    path: '/order/bills/index',
    name: 'orderbills',
    meta: {
      noCache: false
    },
    component: _import('order/bills/index') // 订单系统，退货单列表页面
  }, {
    path: '/order/bills/billsindex',
    name: 'billsindex',
    meta: {
      noCache: false
    },
    component: _import('order/bills/billsindex') // 订单系统，退货单列表查看页面
  }, {
    path: '/order/exchangeGoods/index',
    name: 'exchangeGoods',
    meta: {
      noCache: false
    },
    component: _import('order/exchangeGoods/index') // 订单系统，单据数据管理，换货单页面
  }, {
    path: '/order/exchangeGoods/exchangeReadct',
    name: 'exchangeReadct',
    meta: {
      noCache: false
    },
    component: _import('order/exchangeGoods/exchangeReadct') // 订单系统，单据数据管理，换货单详情编辑页面
  }, {
    path: '/order/swapOrderList/index',
    name: 'swapOrderList',
    meta: {
      noCache: false
    },
    component: _import('order/swapOrderList/index')
  }, {
    path: '/order/billList/index',
    name: 'billList',
    meta: {
      noCache: false
    },
    component: _import('order/billList/index')
  }, {
    path: '/order/applyForPrice/index',
    name: 'applyForPrice',
    meta: {
      noCache: false
    },
    component: _import('order/applyForPrice/index')
  }, {
    path: '/order/orderList/orderDetail/index/:orderId',
    name: 'applyForPriceDetail',
    meta: {
      noCache: false
    },
    component: _import('order/orderList/orderDetail/index')
  }, {
    path: '/order/loseGoods/index',
    name: 'loseGoods',
    meta: {
      title: '缺货登记',
      noCache: false
    },
    component: _import('order/loseGoods/index') // 订单，缺货登记列表
  }, {
    path: '/order/loseGoods/loseDetail',
    name: 'loseDetail',
    meta: {
      title: '缺货登记详情',
      noCache: true
    },
    component: _import('order/loseGoods/loseDetail') // 订单，缺货登记详情页面
  }, {
    path: '/order/batchChannel/index',
    name: 'batchChannel',
    meta: {
      title: '批量导入订单列表',
      noCache: false
    },
    component: _import('order/batchChannel/index') // 订单，订单管理，批量导入订单列表
  }, {
    path: '/order/batchChannelLead/index',
    name: 'batchChannelLead',
    meta: {
      title: '批量导入订单操作页面',
      noCache: true
    },
    component: _import('order/batchChannelLead/index') // 订单，订单管理，批量导入订单操作页面
  }, {
    path: '/order/batchChannelLead/declare',
    name: 'declare',
    meta: {
      title: '批量导入订单填写说明页面',
      noCache: true
    },
    component: _import('order/batchChannelLead/declare') // 订单，订单管理，批量导入订单填写说明页面
  }, {
    path: '/order/batchChannelRedact/index',
    name: 'batchChannelRedact',
    meta: {
      title: '批量导入订单详情',
      noCache: true
    },
    component: _import('order/batchChannelRedact/index') // 订单，订单管理，编辑导入订单
  }, {
    path: '/order/deliverGoodsList/index',
    name: 'deliverGoods',
    meta: {
      title: '发货单',
      noCache: false
    },
    component: _import('order/deliverGoodsList/index') // 订单，单据管理，发货单
  }, {
    path: '/order/orderIncreaseList/index',
    name: 'orderIncreaseList',
    meta: {
      noCache: true
    },
    component: _import('order/orderIncreaseList/index') // 订单统计，订单增长率，列表
  }, {
    path: '/order/orderIncreaseRedact/index',
    name: 'orderIncreaseRedact',
    meta: {
      noCache: false
    },
    component: _import('order/orderIncreaseRedact/index') // 订单统计，订单增长率，详情
  }, {
    path: '/order/orderDiscount/index',
    name: 'orderDiscount',
    meta: {
      title: '订单明细导出',
      noCache: false
    },
    component: _import('order/orderDiscount/index') // 订单统计，订单明细导出，列表
  }, {
    path: '/order/orderVmi/index',
    name: 'orderVmi',
    meta: {
      title: 'VIM订单明细',
      noCache: false
    },
    component: _import('order/orderVmi/index') // 订单统计，Vmi订单明细，列表
  }, {
    path: '/order/deliverGoodsList/detail/index',
    name: 'deliverGoodsDetail',
    meta: {
      title: '发货单详情',
      noCache: false
    },
    component: _import('order/deliverGoodsList/detail/index') // 订单，单据管理，发货单详情
  }, {
    path: '/order/orderSetting/index',
    name: 'orderSetting',
    meta: {
      title: '订单类型',
      noCache: false
    },
    component: _import('order/orderSetting/index') // 订单，订单设置，订单类型
  }, {
    path: '/order/orderSettingRedact/index',
    name: 'orderSettingRedact',
    meta: {
      title: '订单类型设置',
      noCache: false
    },
    component: _import('order/orderSettingRedact/index') // 订单，订单设置，订单类型设置
  },
  // 同步订单列表
  {
    path: '/order/abnormalOrder',
    name: 'abnormalOrder',
    meta: {
      title: '同步订单列表',
      noCache: false
    },
    component: _import('order/abnormalOrder/abnormalOrder')
  }
  ]
},
// 商店首页配置
{
  path: '/storeLayout',
  component: Layout,
  children: [{
    path: '/storeLayout/homePagePromotion/index',
    name: 'homePagePromotion',
    meta: {
      title: '首页推广',
      noCache: false
    },
    component: _import('storeLayout/homePagePromotion/index')
  }, {
    path: '/storeLayout/productPromotion/index',
    name: 'productPromotion',
    meta: {
      title: '产品推广',
      noCache: true
    },
    component: _import('storeLayout/productPromotion/index')
  }, {
    path: '/storeLayout/productPromotion/detail',
    name: 'productPromotionDetail',
    meta: {
      title: '产品推广详情',
      noCache: true
    },
    component: _import('storeLayout/productPromotion/detail')
  }, {
    path: '/storeLayout/homePageAnnouncement/index',
    name: 'homePageAnnouncement',
    meta: {
      title: '首页公告',
      noCache: false
    },
    component: _import('storeLayout/homePageAnnouncement/index')
  }, {
    path: '/storeLayout/frontPageColumn/index',
    name: 'frontPageColumn',
    meta: {
      title: '首页栏目',
      noCache: false
    },
    component: _import('storeLayout/frontPageColumn/index')
  }, {
    path: '/storeLayout/frontPagePlate/index',
    name: 'frontPagePlate',
    meta: {
      title: '首页板块',
      noCache: false
    },
    component: _import('storeLayout/frontPagePlate/index')
  }, {
    path: '/storeLayout/frontPageColumn/editAddColumn',
    name: 'addColumn',
    meta: {
      title: '添加首页栏目',
      noCache: true
    },
    component: _import('storeLayout/frontPageColumn/editAddColumn')
  }, {
    path: '/storeLayout/frontPageColumn/editAddColumn/:id',
    name: 'editColumn',
    meta: {
      title: '首页栏目详情',
      noCache: true
    },
    component: _import('storeLayout/frontPageColumn/editAddColumn')
  }, {
    path: '/storeLayout/homePageAnnouncement/editAddAnnouncement',
    name: 'addAnnouncement',
    meta: {
      title: '添加首页公告',
      noCache: true
    },
    component: _import('storeLayout/homePageAnnouncement/editAddAnnouncement')
  }, {
    path: '/storeLayout/homePageAnnouncement/editAddAnnouncement/:id',
    name: 'editAnnouncement',
    meta: {
      title: '首页公告详情',
      noCache: true
    },
    component: _import('storeLayout/homePageAnnouncement/editAddAnnouncement')
  }, {
    path: '/storeLayout/frontPagePlate/editAddPlate',
    name: 'addPlate',
    meta: {
      title: '添加首页板块',
      noCache: true
    },
    component: _import('storeLayout/frontPagePlate/editAddPlate')
  }, {
    path: '/storeLayout/frontPagePlate/editAddPlate/:id',
    name: 'editPlate',
    meta: {
      title: '编辑首页板块',
      noCache: true
    },
    component: _import('storeLayout/frontPagePlate/editAddPlate')
  }, {
    path: '/storeLayout/mobile/index',
    name: 'mobile',
    meta: {
      title: '移动端首页',
      noCache: true
    },
    component: _import('storeLayout/mobile/index')
  }, {
    path: '/storeLayout/mobile/editMobile',
    name: 'editMobile',
    meta: {
      title: '编辑移动端首页',
      noCache: true
    },
    component: _import('storeLayout/mobile/editMobile')
  }, {
    path: '/storeLayout/totalStationSet/index',
    name: 'totalStationSet',
    meta: {
      title: '购物设置',
      noCache: true
    },
    component: _import('storeLayout/totalStationSet/index') // 全站设置，购物设置
  }, {
    path: '/storeLayout/factorySet/index',
    name: 'factorySet',
    meta: {
      title: '工厂设置',
      noCache: true
    },
    component: _import('storeLayout/factorySet/index') // 全站设置，工厂设置
  }, {
    path: '/storeLayout/baseSet/index',
    name: 'baseSet',
    meta: {
      title: '基本设置',
      noCache: true
    },
    component: _import('storeLayout/baseSet/index') // 全站设置，基本设置
  }, {
    path: '/storeLayout/agreementSet/index',
    name: 'agreementSet',
    meta: {
      title: '协议设置',
      noCache: true
    },
    component: _import('storeLayout/agreementSet/index') // 全站设置，协议设置
  }, {
    path: '/storeLayout/trainingCenter/trainList',
    name: 'trainList',
    meta: {
      title: '培训中心列表',
      noCache: true
    },
    component: _import('storeLayout/trainingCenter/trainList') // 培训中心列表
  }, {
    path: '/storeLayout/trainingCenter/trainAdd',
    name: 'trainAdd',
    meta: {
      title: '添加培训中心',
      noCache: true
    },
    component: _import('storeLayout/trainingCenter/trainAdd') // 添加培训中心
  }, {
    path: '/storeLayout/trainingCenter/trainAdd/:id',
    name: 'trainEdit',
    meta: {
      title: '培训中心详情',
      noCache: true
    },
    component: _import('storeLayout/trainingCenter/trainAdd') // 编辑培训中心
  }, {
    path: '/storeLayout/trainingCenter/trainCategoryList',
    name: 'trainCategoryList',
    meta: {
      title: '培训中心分类列表',
      noCache: true
    },
    component: _import('storeLayout/trainingCenter/trainCategoryList') // 培训中心分类列表
  }, {
    path: '/storeLayout/trainingCenter/trainCategoryAdd',
    name: 'trainCategoryAdd',
    meta: {
      title: '添加培训中心分类',
      noCache: true
    },
    component: _import('storeLayout/trainingCenter/trainCategoryAdd') // 添加培训中心分类
  }, {
    path: '/storeLayout/downloadCenter/downList',
    name: 'downList',
    meta: {
      title: '下载中心列表',
      noCache: true
    },
    component: _import('storeLayout/downloadCenter/downList') // 下载中心列表
  }, {
    path: '/storeLayout/downloadCenter/downloadAdd',
    name: 'downloadAdd',
    meta: {
      title: '添加下载中心',
      noCache: true
    },
    component: _import('storeLayout/downloadCenter/downloadAdd') // 添加下载中心
  }, {
    path: '/storeLayout/downloadCenter/downloadAdd/:id',
    name: 'downloadEdit',
    meta: {
      title: '下载中心详情',
      noCache: true
    },
    component: _import('storeLayout/downloadCenter/downloadAdd') // 编辑下载中心
  }, {
    path: '/storeLayout/downloadCenter/downCategoryList',
    name: 'downCategoryList',
    meta: {
      title: '下载中心分类列表',
      noCache: true
    },
    component: _import('storeLayout/downloadCenter/downCategoryList') // 下载中心分类列表
  }, {
    path: '/storeLayout/downloadCenter/downCategoryAdd',
    name: 'downCategoryAdd',
    meta: {
      title: '添加下载中心分类',
      noCache: true
    },
    component: _import('storeLayout/downloadCenter/downCategoryAdd') // 添下载中心分类
  }, {
    path: '/material/rxBanner/index',
    name: 'rxBanner',
    meta: {
      title: '轮播图管理',
      noCache: true
    },
    component: _import('storeLayout/rxBanner/index') // 柔性配置 - 轮播图管理
  }, {
    path: '/material/rxBanner/detail',
    name: 'bannerDetail',
    meta: {
      title: '轮播图管理详情',
      noCache: true
    },
    component: _import('storeLayout/rxBanner/detail') // 柔性配置 - 轮播图管理详情
  }, {
    path: '/material/rxCategory/index',
    name: 'rxCategory',
    meta: {
      title: '系列展示管理',
      noCache: true
    },
    component: _import('storeLayout/rxCategory/index') // 柔性配置 - 系列展示管理
  }, {
    path: '/material/rxCategory/detail',
    name: 'categoryDetail',
    meta: {
      title: '系列展示管理详情',
      noCache: true
    },
    component: _import('storeLayout/rxCategory/detail') // 柔性配置 - 系列展示管理详情
  }, {
    path: '/material/rxRecommend/index',
    name: 'rxRecommend',
    meta: {
      title: '推荐管理',
      noCache: true
    },
    component: _import('storeLayout/rxRecommend/index') // 柔性配置 - 推荐管理
  }, {
    path: '/material/rxRecommend/detail',
    name: 'recommendDetail',
    meta: {
      title: '推荐管理详情',
      noCache: true
    },
    component: _import('storeLayout/rxRecommend/detail') // 柔性配置 - 推荐管理详情
  }, {
    path: '/material/storeManage/index',
    name: 'storeManage',
    meta: {
      title: '店铺管理',
      noCache: true
    },
    component: _import('storeLayout/storeManage/index') // 柔性配置 - 店铺管理
  }, {
    path: '/material/storeManage/detail',
    name: 'storeDetail',
    meta: {
      title: '店铺详情',
      noCache: true
    },
    component: _import('storeLayout/storeManage/detail') // 柔性配置 - 店铺管理详情
  }, {
    path: '/material/storeManage/import',
    name: 'storeImport',
    meta: {
      title: '导入店铺',
      noCache: true
    },
    component: _import('storeLayout/storeManage/import') // 柔性配置 - 导入店铺
  }, {
    path: '/material/rxTheme/index',
    name: 'rxTheme',
    meta: {
      title: '官方主题配置',
      noCache: true
    },
    component: _import('storeLayout/rxTheme/index') // 柔性配置 - 官方主题配置
  }, {
    path: '/material/rxTheme/add',
    name: 'addTheme',
    meta: {
      title: '创建主题项目',
      noCache: true
    },
    component: _import('storeLayout/rxTheme/add') // 柔性配置 - 官方主题配置 - 创建主题项目
  }, {
    path: '/material/rxTheme/bind',
    name: 'bindTheme',
    meta: {
      title: '关联主题',
      noCache: true
    },
    component: _import('storeLayout/rxTheme/bind') // 柔性配置 - 官方主题配置 - 关联主题
  }, {
    path: '/material/rxNotice/index',
    name: 'rxNotice',
    meta: {
      title: '公告管理',
      noCache: true
    },
    component: _import('storeLayout/rxNotice/index') // 柔性配置 - 公告管理
  }, {
    path: '/material/rxNotice/detail',
    name: 'noticeDetail',
    meta: {
      title: '公告详情',
      noCache: true
    },
    component: _import('storeLayout/rxNotice/detail') // 柔性配置 - 公告详情
  }]
},
// 营销推广
{
  path: '/marketingPromotion',
  component: Layout,
  children: [{
    path: '/marketingPromotion/salesPromotion/index',
    name: 'salesPromotion',
    meta: {
      title: '促销活动列表',
      noCache: false
    },
    component: _import('marketingPromotion/salesPromotion/index')
  }, {
    path: '/marketingPromotion/promotionImport/index',
    name: 'promotionImportList',
    meta: {
      title: '批量活动导入列表',
      noCache: false
    },
    component: _import('marketingPromotion/promotionImport/index')
  }, {
    path: '/marketingPromotion/promotionImport/import',
    name: 'promotionImport',
    meta: {
      title: '批量活动导入',
      noCache: false
    },
    component: _import('marketingPromotion/promotionImport/import')
  }, {
    path: '/marketingPromotion/addPromotion/index/:id',
    name: 'editPromotion',
    meta: {
      title: '促销活动详情',
      noCache: true
    },
    component: _import('marketingPromotion/addPromotion/index') // 查看促销活动详情
  }, {
    path: '/marketingPromotion/addPromotion/index',
    name: 'addPromotion',
    meta: {
      title: '添加促销活动',
      noCache: true
    },
    component: _import('marketingPromotion/addPromotion/index') // 添加促销活动
  }, {
    path: '/marketingPromotion/checkPromotion/checkSalesPromotion',
    name: 'checkSalesPromotion',
    meta: {
      title: '促销活动审批列表',
      noCache: true
    },
    component: _import('marketingPromotion/checkPromotion/checkSalesPromotion')
  }, {
    path: '/marketingPromotion/checkPromotion/checkCoupon',
    name: 'checkCoupon',
    meta: {
      title: '优惠券审批列表',
      noCache: true
    },
    component: _import('marketingPromotion/checkPromotion/checkCoupon')
  }, {
    path: '/marketingPromotion/checkPromotion/checkGroupSeckill',
    name: 'checkGroupSeckill',
    meta: {
      title: '拼团秒杀审批列表',
      noCache: true
    },
    component: _import('marketingPromotion/checkPromotion/checkGroupSeckill')
  }, {
    path: '/marketingPromotion/checkPromotion/checkPolicyPromotion',
    name: 'checkPolicyPromotion',
    meta: {
      title: '统一政策审批列表',
      noCache: true
    },
    component: _import('marketingPromotion/checkPromotion/checkPolicyPromotion')
  }, {
    path: '/marketingPromotion/approvePolicyPromotion/index/:id',
    name: 'approvePolicyPromotion',
    meta: {
      title: '统一政策审批单',
      noCache: false
    },
    component: _import('marketingPromotion/approvePolicyPromotion/index')
  }, {
    path: '/marketingPromotion/approvePickUpRatePromotion/index/:id',
    name: 'approvePickUpRatePromotion',
    meta: {
      title: '统一政策审批单',
      noCache: false
    },
    component: _import('marketingPromotion/approvePickUpRatePromotion/index')
  }, {
    path: '/marketingPromotion/policyPromotion/policyGradePromotion',
    name: 'policyGradePromotion',
    meta: {
      title: '商品等级折扣',
      noCache: true
    },
    component: _import('marketingPromotion/policyPromotion/policyGradePromotion') // 商品等级折扣页面
  }, {
    path: '/marketingPromotion/pickUpRatePromotion/index',
    name: 'pickUpRatePromotion',
    meta: {
      title: '提货增长返利',
      noCache: true
    },
    component: _import('marketingPromotion/pickUpRatePromotion/index') // 提货增长返利
  }, {
    path: '/marketingPromotion/coupon',
    name: 'coupon',
    meta: {
      title: '优惠券列表',
      noCache: false
    },
    component: _import('marketingPromotion/coupon/index') // 优惠券 - 优惠券列表
  }, {
    path: '/marketingPromotion/addCoupon',
    name: 'addCoupon',
    meta: {
      title: '优惠券详情',
      noCache: true
    },
    component: _import('marketingPromotion/coupon/addCoupon/index') // 优惠券 - 添加优惠券
  },
  {
    path: '/marketingPromotion/groupBuy',
    name: 'groupBuy',
    meta: {
      title: '拼团列表',
      noCache: false
    },
    component: _import('marketingPromotion/groupBuy/index') // 拼团 - 拼团列表
  }, {
    path: '/marketingPromotion/addGroupBuy',
    name: 'addGroupBuy',
    meta: {
      title: '拼团详情',
      noCache: true
    },
    component: _import('marketingPromotion/groupBuy/addGroupBuy/index') // 拼团 - 添加拼团
  }, {
    path: '/marketingPromotion/seckill',
    name: 'seckill',
    meta: {
      title: '秒杀列表',
      noCache: false
    },
    component: _import('marketingPromotion/seckill/index') // 秒杀列表
  }, {
    path: '/marketingPromotion/addSeckill',
    name: 'addSeckill',
    meta: {
      title: '秒杀详情',
      noCache: true
    },
    component: _import('marketingPromotion/seckill/addSeckill') // 添加秒杀
  }, {
    path: '/material/exchangeActivity',
    name: 'exchangeActivity',
    meta: {
      title: '兑换活动列表',
      noCache: false
    },
    component: _import('marketingPromotion/exchange/exchangeActivity/index') // 卡券兑换 - 兑换活动列表
  }, {
    path: '/material/exchangeDetail',
    name: 'exchangeDetail',
    meta: {
      title: '兑换活动详情',
      noCache: true
    },
    component: _import('marketingPromotion/exchange/exchangeActivity/detail') // 卡券兑换 - 添加兑换码
  }, {
    path: '/material/exchangeCode',
    name: 'exchangeCode',
    meta: {
      title: '兑换码列表',
      noCache: true
    },
    component: _import('marketingPromotion/exchange/exchangeCode/index') // 卡券兑换 - 兑换码列表
  }, {
    path: '/material/exchangeCodeOrder',
    name: 'exchangeCodeOrder',
    meta: {
      title: '兑换数据列表',
      noCache: true
    },
    component: _import('marketingPromotion/exchange/exchangeCodeOrder/index') // 卡券兑换 - 兑换数据列表
  }, {
    path: '/marketingPromotion/serviceFee',
    name: 'serviceFeeList',
    meta: {
      title: '服务费活动列表',
      noCache: true
    },
    component: _import('marketingPromotion/serviceFee/index') // 服务费活动专区 - 服务费活动列表
  }, {
    path: '/marketingPromotion/serviceFeeAdd',
    name: 'serviceAdd',
    meta: {
      title: '服务费活动详情',
      noCache: true
    },
    component: _import('marketingPromotion/serviceFee/serviceAdd') // 服务费活动专区 - 添加服务费活动专区
  }, {
    path: '/marketingPromotion/serviceImport',
    name: 'serviceImport',
    meta: {
      title: '批量服务活动导入',
      noCache: true
    },
    component: _import('marketingPromotion/serviceFee/serviceImport') // 服务费活动专区 - 批量导入
  }, {
    path: '/material/exportElectricCard/index',
    name: 'exchangeExport',
    meta: {
      title: '电子兑换卡导出记录表',
      noCache: true
    },
    component: _import('rxManage/exportElectricCard/index') // 电子兑换卡导出记录表
  }, {
    path: '/material/exportElectricCard/detail',
    name: 'exportElectricCardDetail',
    meta: {
      title: '创建导出记录表',
      noCache: true
    },
    component: _import('rxManage/exportElectricCard/detail') // 电子兑换卡导出记录表 - 创建导出记录表
  }, {
    path: '/material/equityExchange/index',
    name: 'equityExchange',
    meta: {
      title: '权益兑换列表',
      noCache: true
    },
    component: _import('rxManage/equityExchange/index') // 权益兑换列表
  }, {
    path: '/material/cardShareActivity/index',
    name: 'exchangeShare',
    meta: {
      title: '转发活动配置',
      noCache: true
    },
    component: _import('rxManage/cardShareActivity/index') // 营销&专题活动 - 转发活动配置
  }, {
    path: '/material/cardShareActivity/detail',
    name: 'cardShareActivityDetail',
    meta: {
      title: '转发活动配置详情',
      noCache: true
    },
    component: _import('rxManage/cardShareActivity/detail') // 营销&专题活动 - 转发活动配置 - 转发活动配置详情
  }, {
    path: '/material/cardSpecialActivity/index',
    name: 'exchangeSpecial',
    meta: {
      title: '营销专题活动',
      noCache: true
    },
    component: _import('rxManage/cardSpecialActivity/index') // 营销&专题活动 - 营销专题活动
  }, {
    path: '/material/cardSpecialActivity/detail',
    name: 'cardSpecialActivityDetail',
    meta: {
      title: '营销专题活动详情',
      noCache: true
    },
    component: _import('rxManage/cardSpecialActivity/detail') // 营销&专题活动 - 营销专题活动 - 营销专题活动详情
  }]
}, {
  path: '/success',
  component: Layout,
  children: [{
    path: '/successPage/index',
    name: 'success',
    meta: {
      noCache: false
    },
    component: _import('successPage/index')
  }]
}, {
  path: '*',
  redirect: '/404',
  hidden: true
}
]




export default new Router({
  // mode: 'history', // 后端支持可开
  // base: '/backstage/',
  // mode: 'history', // 后端支持可开 还有hash模式/#/
  scrollBehavior: () => ({
    y: 0
  }),
  routes: constantRouterMap
})

