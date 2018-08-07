package com.bat.distributor.service.platform.sysplatform.executor;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.platform.dto.SysPlatformListQry;
import com.bat.distributor.api.platform.dto.data.SysPlatformDTO;
import com.bat.distributor.api.platform.dto.data.SysPlatformListDTO;
import com.bat.distributor.dao.platform.SysPlatformApiMapper;
import com.bat.distributor.dao.platform.SysPlatformDistributorMapper;
import com.bat.distributor.dao.platform.SysPlatformMapper;
import com.bat.distributor.dao.platform.dataobject.*;
import com.bat.distributor.dao.platform.dataobject.*;
import com.bat.distributor.service.platform.convertor.PlatformConvertor;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.bat.distributor.service.platform.executor.ErrorCode.B_DISTRIBUTOR_PLATFORM_ERROR;

@Component
public class SysPlatformQryExe {

    @Resource
    private SysPlatformMapper sysPlatformMapper;

    @Resource
    private SysPlatformDistributorMapper sysPlatformDistributorMapper;

    @Resource
    private SysPlatformApiMapper sysPlatformApiMapper;

    /**
     * 查询分销商系统平台列表（分页）
     * 
     * @param qry
     * @return
     */
    public PageInfo<SysPlatformListDTO> executeList(SysPlatformListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<SysPlatformListDO> platformDOS = sysPlatformMapper.listSysPlatform(qryMap);
        PageInfo pageInfo = new PageInfo(platformDOS);
        List<SysPlatformListDTO> platformDTOS = PlatformConvertor.toSysPlatformListDTOList(pageInfo.getList());
        pageInfo.setList(platformDTOS);
        return pageInfo;
    }

    /**
     * 根据分销商系统平台ID查询分销商系统平台详情
     * 
     * @param qry
     * @return
     */
    public SysPlatformDTO execute(BaseId qry) {
        SysPlatformDO sysPlatformDO = sysPlatformMapper.selectByPrimaryKey(qry.getId());
        if (sysPlatformDO == null) {
            throw DistributorException.buildException(B_DISTRIBUTOR_PLATFORM_ERROR);
        }
        SysPlatformDTO dto = PlatformConvertor.toSysPlatformDTO(sysPlatformDO);
        getSysPlatformData(dto);
        return dto;
    }

    private void getSysPlatformData(SysPlatformDTO dto) {
        List<SysPlatformApiDO> apiDOS = sysPlatformApiMapper.listBySysPlatformId(dto.getId());
        dto.setApis(PlatformConvertor.toSysPlatformApiDTOList(apiDOS));
        List<SysPlatformDistributorDO> distributorDOS = sysPlatformDistributorMapper.listBySysPlatformId(dto.getId());
        dto.setDistributors(PlatformConvertor.toPlatformDistributorDTOList(distributorDOS));
    }

    public DistributorPlatformApiRpcDO getByDistributorIdAndApiTypeAndPlatform(Integer distributorId, Short apiType, String platform) {
        return sysPlatformDistributorMapper.getByDistributorIdAndApiTypeAndPlatform(distributorId,apiType,platform);
    }

    public SysPlatformApiDO getSysPlatformApiBySysPlatformIdAndApiType(Integer sysPlafformId, Short apiType) {
        return sysPlatformApiMapper.getSysPlatformApiBySysPlatformIdAndApiType(sysPlafformId,apiType);
    }

    public SysPlatformDO getByPlatformAndDistributorId(String platform,Integer distributorId) {
        return sysPlatformMapper.getByPlatformAndDistributorId(platform,distributorId);
    }
}
