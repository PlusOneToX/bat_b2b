package com.bat.flexible.api.material;

import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleUpOrDownDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.material.dto.*;
import com.bat.flexible.dao.material.co.MaterialPageCO;
import com.bat.flexible.dao.material.co.MaterialSimpleTreeCO;
import com.bat.flexible.dao.material.co.MaterialTreeCO;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import com.bat.flexible.api.material.dto.*;

import java.util.List;

public interface MaterialServiceI {
    /**
     * 根据材质id查询材质列表
     * @param materialIdList
     * @return
     */
    List<MaterialDO> listByMaterialIdList(List<Integer> materialIdList);

    /**
     * 新增材质
     * @param materialSaveCmd
     * @param currentAdmin
     * @return
     */
    Response create(MaterialSaveCmd materialSaveCmd, AdminResponse currentAdmin);

    /**
     * 修改材质
     * @param materialSaveCmd
     * @param currentAdmin
     * @return
     */
    Response update(MaterialSaveCmd materialSaveCmd, AdminResponse currentAdmin);

    /**
     * 材质详情
     * @param id
     * @return
     */
    Response<MaterialDetailQry> detailById(Integer id);

    /**
     * 上下移动
     * @param flexibleUpOrDownDTO
     * @param currentAdmin
     * @return
     */
    Response upOrDown(FlexibleUpOrDownDTO flexibleUpOrDownDTO, AdminResponse currentAdmin);

    /**
     * 启用禁用
     * @param flexibleUpdateStatusDTO
     * @param currentAdmin
     * @return
     */
    Response updateOpenFlag(FlexibleUpdateStatusDTO flexibleUpdateStatusDTO, AdminResponse currentAdmin);

    /**
     * 删除
     * @param id
     * @param currentAdmin
     * @return
     */
    Response deleteById(Integer id, AdminResponse currentAdmin);

    MaterialDO getById(Integer materialId);

    PageInfo<MaterialPageCO> page(MaterialPageQry materialPageQry);

    List<MaterialTreeCO> tree(MaterialTreeQry materialTreeQry);

    void swapTree(List<MaterialTreeCO> list,Integer distributorId);

    List<MaterialTreeCO> treeNew(MaterialTreeQry materialTreeQry);

    List<MaterialDO> listAll();

    MaterialDO getByMaterialNo(String materialNo);



    Response<List<MaterialSimpleTreeCO>> treeAdmin(MaterialTreeAdminQry materialTreeAdminQry);

    /**
     * 根据货品id查询材质
     * @param itemId
     * @return
     */
    MaterialDO getByItemId(Integer itemId);

    /**
     * 获取材质对应的货品信息
     * @param materialItemQry
     * @return
     */
    GoodsItemRpcQry getItemMsgForMaterial(MaterialItemQry materialItemQry);

    /**
     * 计算材质价格（不计算图片版权费）
     * @param distributorId
     * @param materialId
     * @param orderSouce
     * @return
     */
    Response getPriceByCondition(Integer distributorId,Integer materialId,String orderSouce);

    /**
     * 后台查询材质列表（非树形）
     * @param materialTreeAdminQry
     * @return
     */
    PageInfo<MaterialSimpleDTO> pageWithoutTree(MaterialTreeAdminQry materialTreeAdminQry);

    List<MaterialSimpleDTO> listWithoutTree(MaterialTreeAdminQry materialTreeAdminQry);

    void test();

    /**
     * 根据货品编码查询材质
     * @param itemCode
     * @return
     */
    MaterialDO getByItemCode(String itemCode);

}
