package com.bat.goods.dao.goods;

import com.bat.goods.dao.goods.dataobject.GoodsSaleDataDO;
import java.util.List;

public interface GoodsSaleDataMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsSaleDataDO record);

    GoodsSaleDataDO selectByPrimaryKey(Integer id);

    List<GoodsSaleDataDO> selectAll();

    int updateByPrimaryKey(GoodsSaleDataDO record);
}