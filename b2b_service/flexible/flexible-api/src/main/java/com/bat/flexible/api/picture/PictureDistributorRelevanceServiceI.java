package com.bat.flexible.api.picture;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.dao.picture.dataobject.PictureDistributorRelevanceDO;

import java.util.List;

public interface PictureDistributorRelevanceServiceI {
    void saveRela(Integer pictureId, List<Integer> distributorIdApplyList, Boolean isAdd, AdminResponse adminResponse,  Short resellerScope);

    List<DistributorSimpleRelaQry> listByPictureIdAndDelFlag(Integer pictureId, Short delFlag);

    List<PictureDistributorRelevanceDO> listByCondition(Integer pictureId,List<Integer> distributorIds);
}
