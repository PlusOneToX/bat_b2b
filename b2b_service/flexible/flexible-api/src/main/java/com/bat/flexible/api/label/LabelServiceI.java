package com.bat.flexible.api.label;

import com.bat.flexible.api.label.dto.LabelCmd;
import com.bat.flexible.api.label.dto.LabelDetailQry;
import com.bat.flexible.api.label.dto.LabelPageQry;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.dao.label.dataobject.LabelDO;

import java.io.File;
import java.util.List;

public interface LabelServiceI {
    List<LabelDO> listByLabelIdList(List<Integer> labelIdList);

    /**
     * 新增标签
     * @param labelCmd
     * @param currentAdmin
     * @return
     */
    Response create(LabelCmd labelCmd, AdminResponse currentAdmin);

    /**
     * 修改标签
     * @param labelCmd
     * @param currentAdmin
     * @return
     */
    Response update(LabelCmd labelCmd, AdminResponse currentAdmin);

    LabelDetailQry detailById(Integer id);

    /**
     * 分页查询标签
     * @param labelPageQry
     * @return
     */
    Response page(LabelPageQry labelPageQry);

    /**
     * 标签启用禁用
     * @param flexibleUpdateStatusDTO
     * @param currentAdmin

     * @return
     */
    Response upOrDown(FlexibleUpdateStatusDTO flexibleUpdateStatusDTO, AdminResponse currentAdmin);

    /**
     * 删除标签
     * @param id
     * @param currentAdmin
     * @return
     */
    Response delete(Integer id, AdminResponse currentAdmin);

    LabelDO getById(Integer id);

    String generateBasicLabel(File file, LabelDO label, String fileName);

    /**
     * 条件查询分销商标签列表
     * @param distributorIds
     * @param categoryId
     * @param pictureId
     * @param distributorScope
     * @return
     */
    List<LabelDO> listDistributorsDiyLableByCondition(List<Integer> distributorIds, Integer categoryId, Integer pictureId, Short distributorScope);
}
