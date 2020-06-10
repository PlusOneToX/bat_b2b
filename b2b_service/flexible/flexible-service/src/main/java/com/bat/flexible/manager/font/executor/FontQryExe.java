package com.bat.flexible.manager.font.executor;

import com.alicp.jetcache.anno.Cached;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.redis.FlexibleKeyConstant;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.font.FontDOMapper;
import com.bat.flexible.dao.font.co.FontCO;
import com.bat.flexible.dao.font.dataobject.FontDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FontQryExe {

    @Autowired
    private FontDOMapper fontDOMapper;

    public List<FontCO> listCOByOpenFlagAndContent(Short openFlag, String content) {
        return fontDOMapper.listCOByOpenFlagAndContent(openFlag,content);
    }

    @Cached(name = FlexibleKeyConstant.FONT_DO_PRE,key = "#id")
    public FontDO getById(Integer id) {
        if(id ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL);
        }
        FontDO fontDO = fontDOMapper.selectByPrimaryKey(id);
        if(fontDO ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
        }
        if(FlexibleCommonConstant.COMMON_DEL_FLAG_YES.equals(fontDO.getDelFlag())){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_DATA_DEL_ALREADY);
        }
        return fontDO;
    }

    public Integer findMaxSequence() {
        return fontDOMapper.findMaxSequence();
    }

    /**
     * 查找上下移动影响的字体
     * @param sequence
     * @param flag
     * @return
     */
    public FontDO getEffect(Integer sequence, Boolean flag) {
        return fontDOMapper.getEffect(sequence,flag);
    }
}
