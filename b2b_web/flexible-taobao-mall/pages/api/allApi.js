import request from "../utils/request"

// 获取租户地址
export async function getTenantInfo(data) {
	return request({
		url: 'platform/v1/web/tenant/url',
		data,
		method: 'GET',
		diyapi: true
	});
}
// 获取型号列表
export async function getModelList(data) {
	return request({
		url: 'flexible/v1/web/user/u/model/tree',
		data,
		method: 'GET',
		diyapi: true
	});
}
// 获取材质列表
export async function getMaterialList(data) {
	return request({
		url: 'flexible/v1/web/user/u/material/tree',
		data,
		method: 'GET',
		diyapi: true
	});
}
// 根据型号、材质获取信息（框图、底图等）
export async function getPhoneInfo(data) {
	return request({
		url: 'flexible/v1/web/user/u/modelMaterialRelevance/getByModelIdAndMaterialId',
		data,
		method: 'GET',
		diyapi: true
	});
}
// 获取图库分类
export async function getPictureCategory(data) {
	return request({
		url: 'flexible/v1/web/user/distributorSeriesTheme/detail/tab/list',
		data,
		method: 'GET',
		diyapi: true
	});
}
// 获取图库
export async function getPictureList(data) {
	return request({
		url: 'flexible/v1/web/user/u/picture/detail/picture/list',
		data,
		method: 'GET',
		diyapi: true
	});
}
// 获取价格
export async function getPrice(data) {
	return request({
		url: 'flexible/v1/web/user/u/material/price',
		data,
		method: 'GET',
		diyapi: true
	});
}
// 获取字体
export async function getFontList() {
	return request({
		url: 'flexible/v1/web/user/u/font/listUsable',
		method: 'GET',
		diyapi: true
	});
}
// 获取已生成定制信息
export async function getCustom(data) {
	return request({
		url: 'flexible/v1/web/user/u/pictureModelMaterialDiy',
		data,
		method: 'GET',
		diyapi: true
	});
}

// 获取淘宝配置信息
export async function getTConfig(data) {
	return request({
		url: 'distributor/v1/web/user/distributorElectricityRelationMapping/taobao/getConfig',
		data,
		method:'GET',
		diyapi: true
	});
}

/**
 * @param {Object} data
 * 获取商品详细信息（聚石塔）
 */
export async function listSeller(data) {
	return request({
		url: 'flexible/v1/web/user/u/taoBao/item/seller',
		data,
		method: 'GET',
		diyapi: true
	});
}