package com.bat.flexible.api.index.dto.recommend;


import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
public class DistributorPictureIdListDTO implements Serializable {

    private static final long serialVersionUID = -2496261429793179892L;

    /**
     * 分销商id
     */
    @NotEmpty(message = "分销商id列表不能为空")
    private List<DistributorSimpleRelaQry> distributorList;

    private Integer id;

    @NotEmpty(message = "图片必须要选择")
    private List<Integer> pictureIdList;



}
