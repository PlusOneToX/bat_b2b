package com.bat.distributor.service.platform.wxplatform.executor;

import static com.bat.distributor.service.platform.executor.ErrorCode.B_DISTRIBUTOR_PLATFORM_ERROR;

import java.util.List;

import javax.annotation.Resource;

import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.platform.dto.WxPlatformListQry;
import com.bat.distributor.api.platform.dto.data.WxPlatformDTO;
import com.bat.distributor.api.platform.dto.data.WxPlatformListDTO;
import com.bat.distributor.dao.platform.WxPlatformDistributorMapper;
import com.bat.distributor.dao.platform.WxPlatformMapper;
import com.bat.distributor.dao.platform.dataobject.WxPlatformDO;
import com.bat.distributor.dao.platform.dataobject.WxPlatformDistributorDO;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.distributor.service.platform.convertor.PlatformConvertor;

@Component
public class WxPlatformQryExe {

    @Resource
    private WxPlatformMapper wxPlatformMapper;

    @Resource
    private WxPlatformDistributorMapper wxPlatformDistributorMapper;

    /**
     * 查询分销商微信平台列表（分页）
     * 
     * @param qry
     * @return
     */
    public PageInfo<WxPlatformListDTO> executeList(WxPlatformListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<WxPlatformDO> wxPlatformDOS = wxPlatformMapper.listWxPlatform(qryMap);
        PageInfo pageInfo = new PageInfo(wxPlatformDOS);
        List<WxPlatformListDTO> platformDTOS = PlatformConvertor.toWxPlatformListDTOList(pageInfo.getList());
        pageInfo.setList(platformDTOS);
        return pageInfo;
    }

    /**
     * 根据分销商微信平台ID查询分销商微信平台详情
     * 
     * @param qry
     * @return
     */
    public WxPlatformDTO execute(BaseId qry) {
        WxPlatformDO wxPlatformDO = wxPlatformMapper.selectByPrimaryKey(qry.getId());
        if (wxPlatformDO == null) {
            throw DistributorException.buildException(B_DISTRIBUTOR_PLATFORM_ERROR);
        }
        WxPlatformDTO dto = PlatformConvertor.toWxPlatformDTO(wxPlatformDO);
        getWxPlatformData(dto);
        return dto;
    }

    private void getWxPlatformData(WxPlatformDTO dto) {
        List<WxPlatformDistributorDO> distributorDOS = wxPlatformDistributorMapper.listByWxPlatformId(dto.getId());
        dto.setDistributors(PlatformConvertor.toPlatformDistributorDTOList(distributorDOS));
    }

    public WxPlatformDO getByAppId(String appId) {
        return wxPlatformMapper.getByAppId(appId);
    }

    public List<WxPlatformDO> listByDistributorIdAndType(Integer distributorId, Short type) {
        return wxPlatformMapper.listWxPlatformByDistributorId(distributorId, type);
    }
}
