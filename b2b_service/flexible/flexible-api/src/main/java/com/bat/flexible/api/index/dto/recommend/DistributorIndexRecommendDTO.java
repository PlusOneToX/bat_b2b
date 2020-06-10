package com.bat.flexible.api.index.dto.recommend;


import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.dao.index.co.IndexRecommendRelaCO;
import lombok.Data;

import java.util.List;

@Data
public class DistributorIndexRecommendDTO {


    private Integer id;

    private List<IndexRecommendRelaCO> pictureList;

    private List<DistributorSimpleRelaQry> distributorList;


}
