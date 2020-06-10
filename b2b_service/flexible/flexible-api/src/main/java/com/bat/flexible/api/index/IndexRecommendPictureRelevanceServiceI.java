package com.bat.flexible.api.index;


import com.bat.flexible.api.base.common.dto.FlexibleUpOrDownDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.dao.index.co.IndexRecommendRelaCO;
import com.bat.flexible.dao.index.dataobject.IndexRecommendPictureRelevanceDO;

import java.util.List;

public interface IndexRecommendPictureRelevanceServiceI {
    void setAdminMsg(IndexRecommendPictureRelevanceDO pictureRela, AdminResponse currentAdmin);

    void add(List<Integer> pictureIdList, Integer indexRecommendId, AdminResponse currentAdmin);

    void update(List<Integer> pictureIdList, Integer indexRecommendId, AdminResponse currentAdmin);

    Response updateSortNo(FlexibleUpOrDownDTO flexibleUpOrDownDTO, AdminResponse currentAdmin);

    List<IndexRecommendRelaCO> listByIndexRecommendId(Integer indexRecommendId);

    Response delete(List<Integer> list);
}
