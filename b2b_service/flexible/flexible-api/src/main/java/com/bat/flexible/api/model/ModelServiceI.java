package com.bat.flexible.api.model;

import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.model.dto.ModelDetailQry;
import com.bat.flexible.api.model.dto.ModelPageQry;
import com.bat.flexible.api.base.common.dto.FlexibleUpOrDownDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.model.dto.ModelCmd;
import com.bat.flexible.api.model.dto.ModelQry;
import com.bat.flexible.dao.model.co.ModelPageCO;
import com.bat.flexible.dao.model.co.ModelSimpleCO;
import com.bat.flexible.dao.model.co.ModelTreeCO;
import com.bat.flexible.dao.model.dataobject.ModelDO;

import java.util.List;

public interface ModelServiceI {
    List<ModelDO> listByModelIdList(List<Integer> modelIdList);

    List<ModelDO> listByIdList(List<Integer> idList);

    Response create(ModelCmd modelCmd, AdminResponse currentAdmin);

    Response update(ModelCmd modelCmd, AdminResponse currentAdmin);

    /**
     * 查询型号详情
     * @param id
     * @return
     */
    Response<ModelDetailQry> detailById(Integer id);

    Response upOrDown(FlexibleUpOrDownDTO flexibleUpOrDownDTO, AdminResponse currentAdmin);

    Response updateOpenFlag(FlexibleUpdateStatusDTO flexibleUpdateStatusDTO, AdminResponse currentAdmin);

    Response deleteById(Integer id, AdminResponse currentAdmin);

    /**
     * 置顶
     * @param id
     * @param currentAdmin
     * @return
     */
    Response top(Integer id, AdminResponse currentAdmin);

    ModelDO getById(Integer modelId);

    PageInfo<ModelPageCO> page(ModelPageQry modelPageQry);

    /**
     * 树形型号列表
     * @param modelQry
     * @return
     */
    List<ModelTreeCO> tree(ModelQry modelQry);

    List<ModelTreeCO> treeNew(ModelQry modelQry);

    List<ModelDO> listAll();

    /**
     * 分页查询、非树形结构
     * @param modelPageQry
     * @return
     */
    PageInfo<ModelSimpleCO> pageSimpleCO(ModelPageQry modelPageQry);

    Response test();

    ModelDO getOneByNetworkModel(String networkModel);

}
