package com.bat.distributor.service.platform.executor;

import static com.bat.distributor.service.platform.executor.ErrorCode.B_DISTRIBUTOR_PLATFORM_ERROR;

import java.util.List;

import javax.annotation.Resource;

import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.platform.dto.PlatformListQry;
import com.bat.distributor.api.platform.dto.data.PlatformDTO;
import com.bat.distributor.dao.platform.PlatformMapper;
import com.bat.distributor.dao.platform.dataobject.PlatformDO;
import com.bat.distributor.service.platform.convertor.PlatformConvertor;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.distributor.platform.dto.PlatformRpcDTO;

@Component
public class PlatformQryExe {

    @Resource
    private PlatformMapper mapper;

    /**
     * 查询分销商平台列表（分页）
     * 
     * @param qry
     * @return
     */
    public PageInfo<PlatformDTO> executeList(PlatformListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<PlatformDO> platformDOS = mapper.listPlatform(qryMap);
        PageInfo pageInfo = new PageInfo(platformDOS);
        List<PlatformDTO> platformDTOS = PlatformConvertor.toPlatformDTOList(pageInfo.getList());
        pageInfo.setList(platformDTOS);
        return pageInfo;
    }

    /**
     * 根据分销商平台ID查询分销商平台详情
     * 
     * @param qry
     * @return
     */
    public PlatformDTO execute(BaseId qry) {
        PlatformDO platformDO = mapper.selectByPrimaryKey(qry.getId());
        if (platformDO == null) {
            throw DistributorException.buildException(B_DISTRIBUTOR_PLATFORM_ERROR);
        }
        PlatformDTO dto = PlatformConvertor.toPlatformDTO(platformDO);
        return dto;
    }

    public List<PlatformRpcDTO> listPlatformByPlatformNos(List<String> platformNos) {
        List<PlatformDO> platformDOS = mapper.listByPlatformNos(platformNos);
        List<PlatformRpcDTO> rpcDTOS = PlatformConvertor.toPlatformRpcDTOList(platformDOS);
        return rpcDTOS;
    }

    public List<PlatformRpcDTO> listByOpenFlag(Short openFlag) {
        List<PlatformDO> list = mapper.listByOpenFlag(openFlag);
        return PlatformConvertor.toPlatformRpcDTOList(list);
    }

    public List<PlatformRpcDTO> listByOpenFlagAndNameLike(Short openFlag, String name) {
        List<PlatformDO> list = mapper.listByOpenFlagAndNameLike(openFlag, name);
        return PlatformConvertor.toPlatformRpcDTOList(list);
    }
}
