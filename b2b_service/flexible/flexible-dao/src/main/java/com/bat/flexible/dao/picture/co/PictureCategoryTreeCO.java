package com.bat.flexible.dao.picture.co;

import com.bat.flexible.dao.picture.dataobject.PictureCategoryDO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PictureCategoryTreeCO extends PictureCategoryDO {


    private BigDecimal oldSequence;
    
    private List<PictureCategoryTreeCO> childrenList;
}
