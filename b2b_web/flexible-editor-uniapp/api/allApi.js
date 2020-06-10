let originBaseURL = "https://test.bat.com/"; // 测试

let newBaseURL = "https://test.bat.com/";

module.exports = {
    // 获取租户地址
    getTenant: originBaseURL + "platform/v1/web/tenant/url", // 获取租户地址
    // 获取OSS
    uploadFile: originBaseURL + "system/v1/web/admin/oss/sts", //  获取OSS

    // 编辑器
    getMaterialList: newBaseURL + "flexible/v1/web/user/u/material/tree", // 获取材质列表
    getModelList: newBaseURL + "flexible/v1/web/user/u/model/tree", // 获取型号列表
    getPictureList: newBaseURL + "flexible/v1/web/user/u/picture/tree", // 获取图片素材
    getCustomInfo:
        newBaseURL +
        "flexible/v1/web/user/u/modelMaterialRelevance/getByModelIdAndMaterialId", // 获取定制信息
    getPrice: newBaseURL + "flexible/v1/web/user/u/material/price", // 获取价格
    getCacheIP: newBaseURL + "flexible/v1/web/user/u/pictureModelMaterialDiy", // 获取缓存IP图
    handleCacheIP:
        newBaseURL + "flexible/v1/web/user/u/pictureModelMaterialDiy", // 缓存IP图
    recordPoint: newBaseURL + "flexible/v1/web/user/buryingPoint", // 记录数据埋点
    getModelByNetwork:
        newBaseURL + "flexible/v1/web/user/u/model/getOneByNetworkModel", // 根据入网型号查询机型

    getOSSSts: newBaseURL + "system/v1/web/admin/oss/sts", // 获取OSS对象存储

    // 订单
    handleOrder: newBaseURL + "thirdparty/v1/web/open/order/create", // 创建临时订单、提交到第三方接口

    //定制商品作品收藏
    collection: newBaseURL + "goods/v1/web/customer/goods/diy/collection", // 定制商品作品收藏

    // 购物车
    addToShopcart: newBaseURL + "order/v1/web/user/customer/shoppingcart/diy", // 加入购物车

    //型号材质关联前台管理接口
    getByModelIdAndMaterialId:
        newBaseURL +
        "flexible/v1/web/user/u/modelMaterialRelevance/getByModelIdAndMaterialId", // 型号材质关联前台管理接口

    //根据机型编码或机型名称获取型号和材质数据
    defaultmodelMaterialRelevance:
        newBaseURL +
        "flexible/v1/web/user/u/model/default/modelMaterialRelevance", // 根据机型编码或机型名称获取型号和材质数据

    getPrice: newBaseURL + "flexible/v1/web/user/u/material/price", // 获取价格

    //根据父级获取子级材质分类或材质列表
    getNewmaterialList: newBaseURL + "flexible/v1/web/user/u/material/list", //根据父级获取子级材质分类或材质列表

    //获取手机型号
    getphoneList: newBaseURL + "flexible/v1/web/user/u/model/list", // 获取手机型号

    //根据图片分类Id和材质、型号查找图片列表
    getImageList:
        newBaseURL + "flexible/v1/web/user/u/picture/category/picture/list", //根据图片分类Id和材质、型号查找图片列表

    getFontList: newBaseURL + "flexible/v1/web/user/u/font/listUsable", // 获取字体

    //根据父级ID查找图片分类列表
    getImageTypeList:
        newBaseURL + "flexible/v1/web/user/u/picture/category/list", //根据父级ID查找图片分类列表
    //根据文字生成图片
    txtPicture:
        newBaseURL + "flexible/v1/web/user/u/picture/characters/picture",
    //查询iPhone
    getIphonedata:
        newBaseURL +
        "flexible/v1/web/user/u/model/getModelByModelNoAndModelName",
};
