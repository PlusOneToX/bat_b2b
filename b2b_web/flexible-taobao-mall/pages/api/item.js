import request from "../utils/request"
/**
 * @param {Object} data
 * 获取商品详细信息
 */
export async function listSeller(data) {
	return request({
		url: 'item/seller',
		data,
		method: 'GET',
		jstapi: true
	});
}